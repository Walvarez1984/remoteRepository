/*
 * ModificarFechasEntregaForm.java
 * Creado el 18/08/2009 12:43:01
 *   	
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_ESTADO_DETALLE_PEDIDO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.SEPARADOR_PARAMETROS;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.form.BaseForm;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ModificarFechasEntregaAction;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings({"serial","unchecked"})
public class ModificarFechasEntregaForm extends BaseForm
{
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		PropertyValidator validator = new PropertyValidatorImpl();
		String peticion = request.getParameter(Globals.AYUDA);
		
		//cuando se van a registrar las modificaciones
		if(peticion!=null && peticion.equals("guardar")){
			//se obtienen los parametros correspondientes a las fechas de despacho y entrega
			//Collection<String> parFechasDespachoCol = WebSISPEUtil.obtenerParametrosRequestPrefijo(request, "txtfd");
			//Iterator<String> itFecDes = parFechasDespachoCol.iterator();
			List<EstadoDetallePedidoDTO> detalle = (List<EstadoDetallePedidoDTO>)request.getSession().getAttribute(COL_ESTADO_DETALLE_PEDIDO);			
			
			//cambios pantalla cjui\u00F1a------------------
			Collection<String> parFechasDespachoCol = new ArrayList<String>();
			List<EntregaDetallePedidoDTO> entregasResp = (List<EntregaDetallePedidoDTO>)request.getSession().getAttribute(ModificarFechasEntregaAction.ENTREGAS_RESPONSABLES);
			
			int i = 0;
			for(EstadoDetallePedidoDTO estadoDetallePedidoDTO : detalle){
				int j = 0;
				for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : estadoDetallePedidoDTO.getEntregaDetallePedidoCol()){
					parFechasDespachoCol.add("txtfd" + SEPARADOR_PARAMETROS +  i + SEPARADOR_PARAMETROS + j);
					j++;
				}
				i++;
			}
			Iterator<String> itFecDes = parFechasDespachoCol.iterator();
			
			//obtengo la direcci\u00F3n y fechas de las entregas
			List<String> direccionEntregaCol = new ArrayList<String>();
			List<String> fechaDespachoCol = new ArrayList<String>();
			List<String> fechaEntregaCol = new ArrayList<String>();
			SimpleDateFormat formatoFecha = new SimpleDateFormat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.formato.fecha"));
			for(EstadoDetallePedidoDTO estadoDetallePedidoDTO : detalle){
				for(EntregaDetallePedidoDTO entDetPedDTO :estadoDetallePedidoDTO.getEntregaDetallePedidoCol()){
					i = 0;
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : entregasResp){						
						for(DetallePedidoDTO detallePedidoDTO : entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDetallePedido()){
							if(detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras().equals(estadoDetallePedidoDTO.getDetallePedidoDTO().getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()) &&
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().split("-")[0].equals(entDetPedDTO.getEntregaPedidoDTO().getDireccionEntrega().split("-")[0])&&
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().equals(entDetPedDTO.getEntregaPedidoDTO().getFechaEntregaCliente())){
								direccionEntregaCol.add(request.getParameter("txtde" + SEPARADOR_PARAMETROS + i));
								if(request.getParameter("txtfd" + SEPARADOR_PARAMETROS + i)==null){									
									fechaDespachoCol.add(formatoFecha.format(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega()));
								}else{
									fechaDespachoCol.add(request.getParameter("txtfd" + SEPARADOR_PARAMETROS + i));
								}
								if(request.getParameter("txtfe" + SEPARADOR_PARAMETROS + i)==null){
									fechaEntregaCol.add(formatoFecha.format(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente()));
								}else{
									fechaEntregaCol.add(request.getParameter("txtfe" + SEPARADOR_PARAMETROS + i));
								}
							}	
						}
						i++;
					}
				}
			}
			
			Timestamp tfechaDespacho = null;
			Timestamp tfechaEntrega = null;
			boolean esFDespachoMayorAFEntrega = false;
			
			int indice = 0;
			//se validan los formatos de las fechas
			while(itFecDes.hasNext()){
				String nombreParametroFD = itFecDes.next();
				int indiceDetalle = Integer.parseInt(nombreParametroFD.split(SEPARADOR_PARAMETROS)[1]);
				int indiceEntrega = Integer.parseInt(nombreParametroFD.split(SEPARADOR_PARAMETROS)[2]);
				
				String fechaDespacho = fechaDespachoCol.get(indice);//request.getParameter(nombreParametroFD);
				String fechaEntrega = fechaEntregaCol.get(indice);//request.getParameter("txtfe" + SEPARADOR_PARAMETROS + indiceDetalle + SEPARADOR_PARAMETROS + indiceEntrega);
				String direccionEntrega = direccionEntregaCol.get(indice);//request.getParameter("txtde" + SEPARADOR_PARAMETROS + indiceDetalle + SEPARADOR_PARAMETROS + indiceEntrega);
				
				validator.validateFecha(errors,"fechaDespacho",fechaDespacho,true, MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha Despacho");
				validator.validateFecha(errors,"fechaEntrega",fechaEntrega,true, MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha Entrega");
				validator.validateMandatory(errors, "direccionEntrega",direccionEntrega, "errors.requerido", "Direcci\u00F3n Entrega");
				if(errors.isEmpty()){
					EstadoDetallePedidoDTO estadoDetallePedidoDTO = detalle.get(indiceDetalle);
					EntregaDetallePedidoDTO entregaDTO = ((List<EntregaDetallePedidoDTO>)estadoDetallePedidoDTO.getEntregaDetallePedidoCol()).get(indiceEntrega);
					//entregaDTO.setNpCantidadBultos(UtilesSISPE.calcularCantidadBultos(entregaDTO.getCantidadDespacho(), estadoDetallePedidoDTO.getDetallePedidoDTO().getArticuloDTO().getUnidadManejo()));
					try {
						entregaDTO.setNpCantidadBultos(UtilesSISPE.calcularCantidadBultos(entregaDTO.getCantidadDespacho(), estadoDetallePedidoDTO.getDetallePedidoDTO().getArticuloDTO()));
					} catch (SISPEException e) {
						// TODO Bloque catch generado autom\u00E1ticamente
						LogSISPE.getLog().error(e.getMessage());
					}
					//se obtiene la hora de entrega
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					
					//se valida la relaci\u00F3n entre las fechas ingresadas
					tfechaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(fechaDespacho, 0, 0, 0, 0, 0, 0));
					tfechaEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(fechaEntrega, 0, 0, gc.get(GregorianCalendar.HOUR_OF_DAY),	gc.get(GregorianCalendar.MINUTE), 0, 0));
					
					//se respaldan las fechas originales
					entregaDTO.getEntregaPedidoDTO().setNpFechaEntregaInicial(entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					entregaDTO.getEntregaPedidoDTO().setNpFechaDespachoInicial(entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
					
					//la fecha de despacho no puede ser mayor a la fecha de entrega
					if(tfechaDespacho.getTime() > tfechaEntrega.getTime()){
						esFDespachoMayorAFEntrega = true;
						entregaDTO.setNpDescripcionArticulo("error");
					}else{
						entregaDTO.setNpDescripcionArticulo(null);
					}
					
					//se realiza la actualizaci\u00F3n solo si las fechas ingresadas son diferentes a las originales
					if(tfechaEntrega.getTime() != entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()){
						LogSISPE.getLog().info("Cambia la fecha de entrega la cliente:::->{}--{}--",indiceDetalle,indiceEntrega);
						//se respalda la fecha original
						entregaDTO.getEntregaPedidoDTO().setFechaEntregaCliente(tfechaEntrega);
					}
					if(tfechaDespacho.getTime() != entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega().getTime()){
						LogSISPE.getLog().info("Cambia la fecha de despacho bodega:::->{}--{}--",indiceDetalle,indiceEntrega);
						//se respalda la fecha original
						entregaDTO.getEntregaPedidoDTO().setFechaDespachoBodega(tfechaDespacho);
					}
						//se guarda la nueva direccion de entrega
					entregaDTO.getEntregaPedidoDTO().setDireccionEntrega(direccionEntrega);
				}else{
					break;
				}
				indice++;
			}
			
			if(esFDespachoMayorAFEntrega){
				errors.add("fechas", new ActionMessage("errors.rango.fechas2", "La fecha de entrega", "la fecha de despacho", ""));
			}
		}
		
		return errors;
	}
	
}
