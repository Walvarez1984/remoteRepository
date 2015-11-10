/**
 * 
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_CON_IVA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase maneja la confirmaci\u00F3n de una reservaci\u00F3n una vez que el pedido ha sido producido,
 * unicamente se permite la actualizaci\u00F3n de los pesos en los art\u00EDculos de peso variable.
 * </p>
 * @author 	fmunoz
 * @version 2.0
 * @since		JSDK 1.5.0
 */
@SuppressWarnings("unchecked")
public class ConfirmarReservacionAction extends BaseAction
{
	//formato de n\u00FAmeros a 2 decimales
	private static final String TIPO_ARTICULO_PAVO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");	
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request 				La petici&oacute;n que estamos procesando
   * @param response				La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)throws Exception{
  	
    HttpSession session= request.getSession();
    CotizarReservarForm formulario = (CotizarReservarForm)form;
    
	//se obtiene el bean que contiene los campos genericos
	BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
	
    //objetos para los mensajes
	ActionMessages errors = new ActionMessages();
	ActionMessages warnings = new ActionMessages();
	ActionMessages infos = new ActionMessages();
	
    String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
    String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
    
    String codigoEstadoPagado =  MessagesWebSISPE.getString("label.codigoEstadoLQD");
    
    String salida = "desplegar";
    
    try
    {
      //--------- procesa la petici\u00F3n cuando se escogi\u00F3 confirmar una reservaci\u00F3n ----------------- 
    	if(request.getParameter("indice")!=null)
    	{
        //se obtiene la colecci\u00F3n de los pedidos 
        List<VistaPedidoDTO> pedidos = (List<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidos");
        //colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
        Collection<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
        Collection<DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
        Collection<DetallePedidoDTO> detallePedidoOriginal = new ArrayList <DetallePedidoDTO>();
        //se verifica si el usuario logeado es de la bodega
        String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
        
        int indice = Integer.parseInt(request.getParameter("indice"));
        
        //creaci\u00F3n del DTO para almacenar el pedido selecionado
        VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
        
        //se verifica si la reserva esta bloqueada porque se esta realizando un pago en el POS
        if(EstadoPedidoUtil.reservaBloqueadaDesdePOS(vistaPedidoDTO.getId().getCodigoAreaTrabajo(), 
  				vistaPedidoDTO.getId().getCodigoCompania(), 
  				vistaPedidoDTO.getId().getCodigoEstado(), vistaPedidoDTO.getId().getCodigoPedido(), 
  				vistaPedidoDTO.getId().getSecuencialEstadoPedido())) {
  			instanciarPopUpNotificacionBloqueoReserva(request, vistaPedidoDTO.getLlaveContratoPOS(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null);
  			salida = "lista";
  			return mapping.findForward(salida);
  		}		
        
		//se valida que el pedido en session sea el actual en la BDD
		Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO); //pedidoActual = false;
		
		if(pedidoActual){
			
			if(vistaPedidoDTO.getCodigoConsolidado() != null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
				session.setAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO, Boolean.TRUE);
			}
			
			//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
	    	
	        //se carga la configuraci\u00F3n de los descuentos
	      	CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
	      	//Obtener el tipo de descuento por cajas y mayorista
	      	CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
	      	
	        if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){      
	          session.setAttribute(CotizarReservarAction.ES_ENTIDAD_BODEGA, "ok");
	        }
	        
	        //se guarda en sesi\u00F3n el pedido seleccionado
	        session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
	        //se guarda el c\u00F3digo del local objetivo de la reservaci\u00F3n
	        session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaPedidoDTO.getId().getCodigoAreaTrabajo());
	        session.removeAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL);
	        
	        if(vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL) 
	        		|| vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO)){
	        	
	        	session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA,"ok");
	            session.removeAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO);
	            
	            if(vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO)){
					codigoEstadoPagado =  MessagesWebSISPE.getString("label.codigoEstadoLQD");
				}else if(vistaPedidoDTO.getCodigoEstadoPagado().equals(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL)){
					codigoEstadoPagado = MessagesWebSISPE.getString("label.codigoEstadoPTO");
				}
	            
		        infos.add("reserva.pagada", new ActionMessage("info.confirmacionPesosFinales.mensaje", codigoEstadoPagado));
		        
	        	salida = "lista";
	        	saveInfos(request, infos);
	        	return mapping.findForward(salida);
	        }
	        
			//se verifica con que precios se guard\u00F3 el pedido
			if(vistaPedidoDTO.getEstadoPreciosAfiliado()!=null && vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
				session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			}else{
				formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
				session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
			}
			
			//se verifica si los calculos realizados fueron hechos con iva o no
			if(vistaPedidoDTO.getEstadoCalculosIVA()!= null && vistaPedidoDTO.getEstadoCalculosIVA().equals(estadoActivo)){
				session.setAttribute(CALCULO_PRECIOS_CON_IVA, estadoActivo);
			}else{
				session.setAttribute(CALCULO_PRECIOS_CON_IVA, estadoInactivo); 
			}
	    		
	        //se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
	        VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
	        consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
	        consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
	        consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
	        consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
	        consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
	        consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
	        detalleVistaPedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
	        
	        //se guarda el c\u00F3digo que identifica a un pedido pagado totalmente
	        String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");

	        //variable utilizada para indicar la posici\u00F3n de los detalles
	        int i = 0;
	        
			//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
			WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request, Boolean.FALSE);
	        
	        //se itera la vistaDetallePedido para crear un DetallePedidoDTO
	        for(VistaDetallePedidoDTO vistaDetallePedidoDTO:detalleVistaPedido)
	        {
	          //boolean existioCambioPrecio = false;
	          DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
	          //se crea el DetallePedidoDTO para poder almacenarlo en la reservaci\u00F3n
	          detallePedidoDTO.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
	          detallePedidoDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
	          detallePedidoDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
	          detallePedidoDTO.getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
	          detallePedidoDTO.setArticuloDTO(vistaDetallePedidoDTO.getArticuloDTO());
	          detallePedidoDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());
	          
	          //creaci\u00F3n del estado del detalle del pedido
	          detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadParcialEstado(vistaDetallePedidoDTO.getCantidadParcialEstado());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(vistaDetallePedidoDTO.getCantidadReservarSIC());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadAjustadaModificacionPeso(vistaDetallePedidoDTO.getCantidadAjustadaModificacionPeso());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setReservarBodegaSIC(vistaDetallePedidoDTO.getReservarBodegaSIC());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setEspecialReservado(vistaDetallePedidoDTO.getEspecialReservado());
	    	  detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(vistaDetallePedidoDTO.getValorPrevioVenta());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(vistaDetallePedidoDTO.getValorFinalEstadoDescuento());
	    	  detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(vistaDetallePedidoDTO.getValorUnitarioPOS());
	    	  detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(vistaDetallePedidoDTO.getEstadoCanCotVacio());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getCantidadMinimaMayoreo());
	          
	          //se inicializan los pesos
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstadoReservado(vistaDetallePedidoDTO.getPesoArticuloEstadoReservado());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoBodega(vistaDetallePedidoDTO.getPesoRegistradoBodega());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
	          
	          //solo si el art\u00EDculo es de peso variable
	          if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
	          	if(vistaDetallePedidoDTO.getPesoRegistradoLocal() == null)
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoArticuloEstado());
	          	else{
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
	          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());
	          	}
	          }
	        //Se consulta si tiene autorizaciones
		      DetalleEstadoPedidoAutorizacionDTO detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
			  EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
			  estadoPedidoAutorizacionDTO.setAutorizacionDTO(new ec.com.smx.autorizaciones.dto.AutorizacionDTO());
			  detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
			  detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
			  detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo());
			  detalleAutorizaciones.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			  detalleAutorizaciones.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
			  detalleAutorizaciones.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			  detalleAutorizaciones.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			  Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesCol = SISPEFactory.getDataService().findObjects(detalleAutorizaciones);
				if(CollectionUtils.isNotEmpty(autorizacionesCol)){
					for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : autorizacionesCol){
						if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null && detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion() != null){
							detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpTipoAutorizacion(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion());
							//para el caso de descuento variable
							if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
								detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpNombreDepartamento(
										detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(EstadoPedidoUtil.SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1]);
							}
						}
					}
					LogSISPE.getLog().info("Tiene {} autorizacion(es) el articulo {}", autorizacionesCol.size(),vistaDetallePedidoDTO.getId().getCodigoArticulo());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(autorizacionesCol);
				}
			
	          //se asigna la cantidad ajustada manualmente
	          if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadAjustadaModificacionPeso() == null){
	          	detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadAjustadaModificacionPeso(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
	          }
	          
	          //llamada a la funci\u00F3n que realiza la asignaci\u00F3n y control de precios
	          CotizacionReservacionUtil.controlPrecios(vistaDetallePedidoDTO, detallePedidoDTO.getEstadoDetallePedidoDTO(), request);
	          
	          //totales del detalle
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstado(vistaDetallePedidoDTO.getValorTotalEstado());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoIVA(vistaDetallePedidoDTO.getValorTotalEstadoIVA());
	          //valor total de la venta
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
	          //campos para el c\u00E1lculo de descuentos
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado());
	          
	          //lamada al m\u00E9todo que determina los totales por detalle      
	          CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, false,false);
	          
	          //valores de los descuentos
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoDescuento(vistaDetallePedidoDTO.getValorTotalEstadoDescuento());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(vistaDetallePedidoDTO.getValorTotalEstadoNeto());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(vistaDetallePedidoDTO.getValorTotalEstadoNetoIVA());
	          //valor de control del iva                   
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(vistaDetallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()?estadoActivo:estadoInactivo);
	          
	          //se guardan los campos de la orden de compra
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setNumeroAutorizacionOrdenCompra(vistaDetallePedidoDTO.getNumeroAutorizacionOrdenCompra());
	          detallePedidoDTO.getEstadoDetallePedidoDTO().setObservacionAutorizacionOrdenCompra(vistaDetallePedidoDTO.getObservacionAutorizacionOrdenCompra());
	          
	          detallePedidoDTO.setNpCodigoClasificacion(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion());
	          detallePedidoDTO.setNpCodigoClasificacionArticulo(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion()
	              + caracterToken
	              + vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
	          detallePedidoDTO.setNpCodigoTipoDescuento(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento());
	          detallePedidoDTO.setNpCodigoTipoDescuentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento()
	              + caracterToken
	              + vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()
	              + caracterToken
	              + i);
	          i++;
	          
	    			//se verifica si el art\u00EDculo es un canasto
	    			if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
	    				//se crea la consulta para traer los items del canasto
	    				ArticuloRelacionDTO consultaRecetaArticuloDTO = new ArticuloRelacionDTO();
	    				consultaRecetaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//	    				consultaRecetaArticuloDTO.getId().setCodigoArticuloRelacionado(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
	    				consultaRecetaArticuloDTO.getId().setCodigoArticulo(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
	    				consultaRecetaArticuloDTO.setEstado(estadoActivo);
	    				//se asigna un indicador para saber si es un canasto de cotizaciones (1: si, 0: no)
	    				consultaRecetaArticuloDTO.setNpEstadoRecetaEspecial(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio());
	    				//se modifica la consulta anterior para obtener el hist\u00F3rico
	    				consultaRecetaArticuloDTO.setNpSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
	    				consultaRecetaArticuloDTO.setArticulo(new ArticuloDTO());
	    				consultaRecetaArticuloDTO.getArticulo().setNpCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
	    				consultaRecetaArticuloDTO.getArticulo().getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    				//se realiza la consulta
	    				Collection<ArticuloRelacionDTO> recetaArticuloEstado = SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticuloDesdeHistoricoReceta(consultaRecetaArticuloDTO);
	    				detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticuloEstado);
	    				//se recalculan los valores
	    				CotizacionReservacionUtil.recalcularPrecioReceta(detallePedidoDTO, request);
	    				CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, false,false);
	    			}
	    			
	          //se almacenan los detalles y los art\u00EDculos
	          detallePedido.add(detallePedidoDTO);
	          detallePedidoOriginal.add((DetallePedidoDTO)SerializationUtils.clone(detallePedidoDTO));
	        }
	        
	        //se sube a sesion la cola de autorizaciones para cuando actualice los descuentos no borre los descuentos variables por no existir cola de autorizaciones	        
	        AutorizacionesUtil.verificarAutorizacionesVariables((List<DetallePedidoDTO>)detallePedido, request, formulario);
	        
	        //cambios para que no se pierdan los descuentos de pedidos consolidados cuando registra pesos finales
	        DescuentoEstadoPedidoDTO descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
	        
	        //se procesa el pedido consolidado
	     	if(vistaPedidoDTO.getCodigoConsolidado() != null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
		  		//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
		  		CotizacionReservacionUtil.construirDetallesPedidoDesdeVista(formulario, request, infos, errors, warnings,false,Boolean.TRUE);
	      	}
	     	else{
	            //llamada a la funcion que realiza el procesamiento de los descuentos
	        	descuentoEstadoPedidoTotalDTO = CotizacionReservacionUtil.procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
	     	}
	     	
	        //variable que almacenar\u00E1 el total del descuento
	        //boolean actualizarDescuento = true;
	        //if(contadorPreciosAlterados > 0)
	        //	actualizarDescuento = true;
	        
	        //llamada a la funcion que realiza el procesamiento de los descuentos
//	    	DescuentoEstadoPedidoDTO descuentoEstadoPedidoTotalDTO = CotizacionReservacionUtil.procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
	        
//	        /*------------------- se asignan los atributos necesarios al formulario --------------------*/
	    	if(vistaPedidoDTO.getTipoDocumentoCliente().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
	    			|| vistaPedidoDTO.getTipoDocumentoCliente().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
	    		if(vistaPedidoDTO.getContactoEmpresa().startsWith(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
					formulario.setNumeroDocumento(vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
					session.setAttribute(ContactoUtil.RUC_PERSONA, vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
					formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				}else{
					formulario.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
				}
//				formulario.setNombrePersona(vistaPedidoDTO.getNombrePersona());
//				formulario.setTelefonoPersona(vistaPedidoDTO.getTelefonoPersona());
//				formulario.setNumeroDocumentoPersona(vistaPedidoDTO.getNumeroDocumentoPersona());
//				formulario.setTipoDocumentoPersona(vistaPedidoDTO.getTipoDocumentoCliente());
//				formulario.setEmailPersona(vistaPedidoDTO.getEmailPersona());
//				formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp()!=null?vistaPedidoDTO.getNumBonNavEmp().toString():"");
//				formulario.setEmailEnviarCotizacion(vistaPedidoDTO.getEmailPersona());
//	            //se eliminan los datos de la empresa
//	            formulario.setRucEmpresa(null);
//	            formulario.setNombreEmpresa(null);
//				formulario.setTipoDocumentoContacto(null);
//				formulario.setNumeroDocumentoContacto(null);
//				formulario.setNombreContacto("SIN CONTACTO");
//				formulario.setTelefonoContacto("SN");
//				
//				PersonaDTO personaDto = new PersonaDTO();
//				personaDto.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
//				personaDto.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoPersona());
//				personaDto.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
//				PersonaDTO personaEncontrada=  SISPEFactory.getDataService().findUnique(personaDto);
//				
//				//Indicamos a la variable de formulario que la persona fue encontrada
//				request.getSession().setAttribute(ContactoUtil.PERSONA, personaEncontrada);
//				request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
//				request.getSession().removeAttribute(ContactoUtil.LOC_GUARDADA);
//				
//				
//				//consultar el contacto principal de la persona
//				DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
//				contactoPersonaLocalizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//				contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
//				contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
//				contactoPersonaLocalizacionDTO.setPersonaContactoDTO(new PersonaDTO());
//				contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//				contactoPersonaLocalizacionDTO.setCriteriaSearch(new CriteriaSearch());
//				
//				ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
//				contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
//				contactoRelacionadoDTO.setCodigoPersona(Long.valueOf(Long.valueOf(vistaPedidoDTO.getCodigoPersona())));
//				contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//				
//				Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactoPersonaLocalizacionCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);
//				
//				DatoContactoPersonaLocalizacionDTO contactoTipoPersona=null;
//				if(CollectionUtils.isNotEmpty(contactoPersonaLocalizacionCol)){
//					contactoRelacionadoDTO = (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactoPersonaLocalizacionCol, 0);
//					contactoTipoPersona = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
//				}			
//				
//				if(contactoTipoPersona!=null){
//					if(contactoTipoPersona.getPersonaContactoDTO()!=null){
//						//cargar la informacion del contacto
//						formulario.setNombreContacto(contactoTipoPersona.getPersonaContactoDTO().getNombreCompleto());
//						formulario.setTipoDocumentoContacto(contactoTipoPersona.getPersonaContactoDTO().getTipoDocumento());
//						formulario.setNumeroDocumentoContacto(contactoTipoPersona.getPersonaContactoDTO().getNumeroDocumento());
//						if(contactoTipoPersona.getEmailPrincipal()!=null){
//							formulario.setEmailContacto(contactoTipoPersona.getEmailPrincipal());
//						} else if(contactoTipoPersona.getEmailTrabajo()!=null){
//							formulario.setEmailContacto(contactoTipoPersona.getEmailTrabajo());
//						}
//						if(formulario.getEmailContacto()!= null){
//							formulario.setEmailEnviarCotizacion(formulario.getEmailContacto());
//						}
//						
//						String telefonoContacto="TD: ";
//						if(contactoTipoPersona.getNumeroTelefonicoPrincipal()!=null){
//							telefonoContacto+=contactoTipoPersona.getNumeroTelefonicoPrincipal();										
//						} else telefonoContacto+=" SN ";	
//						if(contactoTipoPersona.getNumeroTelefonicoTrabajo()!=null){
//							telefonoContacto+=" - TT: "+contactoTipoPersona.getNumeroTelefonicoTrabajo();
//						}else telefonoContacto+=" - TT: SN ";
//						if(contactoTipoPersona.getNumeroTelefonicoCelular()!=null){
//							telefonoContacto+=" - TC: "+contactoTipoPersona.getNumeroTelefonicoCelular();
//						}
//						else telefonoContacto+=" - TC: SN ";									
//						formulario.setTelefonoContacto(telefonoContacto);		
////						formulario.setNombreContacto(formulario.getTipoDocumentoContacto()+": "+formulario.getNumeroDocumentoContacto()
////								+" - NC: "+formulario.getNombreContacto()+" - "+formulario.getTelefonoContacto());
//					}
//				}
//			
	    	}
	    	else 
	    		if(vistaPedidoDTO.getTipoDocumentoCliente().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || vistaPedidoDTO.getTipoDocumentoCliente().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){  

				formulario.setNumeroDocumento(vistaPedidoDTO.getRucEmpresa());
//				formulario.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
//				formulario.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
//				formulario.setTelefonoEmpresa(vistaPedidoDTO.getTelefonoEmpresa());
//				formulario.setNombreContacto(null);
//				formulario.setNumeroDocumentoContacto(null);
//				formulario.setNombrePersona(null);
//				formulario.setNumeroDocumentoPersona(null);
//				formulario.setEmailEnviarCotizacion(null);
//				
//				EmpresaDTO empresaDto = new EmpresaDTO();
//				empresaDto.setNumeroRuc(vistaPedidoDTO.getRucEmpresa());
//				empresaDto.setValorTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
//				empresaDto.setEstadoEmpresa(CorporativoConstantes.ESTADO_ACTIVO);
//				EmpresaDTO empresaEncontrada =  SISPEFactory.getDataService().findUnique(empresaDto);
//
//				//Indicamos a la variable de formulario que la empresa fue encontrada
				request.getSession().setAttribute(ContactoUtil.LOC_GUARDADA, vistaPedidoDTO.getCodigoLocalizacion());
//				request.getSession().setAttribute(ContactoUtil.LOCALIZACION, empresaEncontrada);
//				request.getSession().removeAttribute(ContactoUtil.PERSONA);
//							
//				//consultar el contacto principal de la empresa
//				DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
//				contactoPersonaLocalizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//				contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
//				contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
//				contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
//				contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
////				contactoPersonaLocalizacionDTO.setPersonaContactoDTO(new PersonaDTO());
//
//				ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
//				contactoRelacionadoDTO.setCodigoLocalizacion(Long.valueOf(vistaPedidoDTO.getCodigoLocalizacion()));
//				contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
//				contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//				
//				Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactosRelacionadosCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);					
//				DatoContactoPersonaLocalizacionDTO contactoTipoLocalizacion=new DatoContactoPersonaLocalizacionDTO();
//				if(CollectionUtils.isNotEmpty(contactosRelacionadosCol)){
//					contactoRelacionadoDTO =  (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactosRelacionadosCol, 0);
//					contactoTipoLocalizacion = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
//				}									
//				
//				if(contactoTipoLocalizacion!=null){
//					if(contactoTipoLocalizacion.getPersonaContactoDTO()!=null){
//						//cargar la informacion del contacto
//						formulario.setNombreContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getNombreCompleto());
//						formulario.setTipoDocumentoContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getTipoDocumento());
//						formulario.setNumeroDocumentoContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getNumeroDocumento());
//						if(contactoTipoLocalizacion.getEmailPrincipal()!=null){
//							formulario.setEmailContacto(contactoTipoLocalizacion.getEmailPrincipal());
//						} else if(contactoTipoLocalizacion.getEmailTrabajo()!=null){
//							formulario.setEmailContacto(contactoTipoLocalizacion.getEmailTrabajo());
//						}					
//						
//						String telefonoContacto="TD: ";
//						if(contactoTipoLocalizacion.getNumeroTelefonicoPrincipal()!=null){
//							telefonoContacto+=contactoTipoLocalizacion.getNumeroTelefonicoPrincipal();										
//						} else telefonoContacto+=" SN ";	
//						if(contactoTipoLocalizacion.getNumeroTelefonicoTrabajo()!=null){
//							telefonoContacto+=" - TT: "+contactoTipoLocalizacion.getNumeroTelefonicoTrabajo();
//						}else telefonoContacto+=" - TT: SN ";
//						if(contactoTipoLocalizacion.getNumeroTelefonicoCelular()!=null){
//							telefonoContacto+=" - TC: "+contactoTipoLocalizacion.getNumeroTelefonicoCelular();
//						}
//						else telefonoContacto+=" - TC: SN ";	
//						if(formulario.getEmailContacto()!=null){
//							formulario.setEmailEnviarCotizacion(formulario.getEmailContacto());
//						}
//						formulario.setTelefonoContacto(telefonoContacto);		
////						formulario.setNombreContacto(formulario.getTipoDocumentoContacto()+": "+formulario.getNumeroDocumentoContacto()
////								+" - NC: "+formulario.getNombreContacto()+" - "+formulario.getTelefonoContacto());
//					}
//				}		   		 
	    	}
	        formulario.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
	        if(vistaPedidoDTO.getNumBonNavEmp()!=null){
				formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp().toString());
			}
	      //se cargan los datos del contacto
			
	        ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());
	        
	        PaginaTab tabsPesosFinales = ContactoUtil.construirTabsContactoPesosFinales(request, formulario);		        
	        //ContactoUtil.obtenerDatosPersonaEmpresa(request, formulario, null, beanSession);
	        beanSession.setPaginaTab(tabsPesosFinales);
	        
	      //ejecutar el metodo para inicializar el controlador adecuado
			ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");

	        LogSISPE.getLog().info("** CONTEXTO DEL PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
	        //se asigna el contexto del pedido
	        if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial")))
	          formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
	        else
	          formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
	        
	        //se asigna el local responsable
	        formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()+ " " + caracterToken + " " +
	            vistaPedidoDTO.getNombreLocal());
	        LogSISPE.getLog().info("LOCAL RESPONSABLE OBTENIDO: {}",formulario.getLocalResponsable());
	        
	        //se asigna el costo del flete
	    		if(vistaPedidoDTO.getValorCostoEntregaPedido()!=null){
	    			formulario.setCostoFlete(vistaPedidoDTO.getValorCostoEntregaPedido());
	    		}else{
	    			formulario.setCostoFlete(0D);
	    		}
	    		//se asigna el descuento total
	    		formulario.setDescuentoTotal(descuentoEstadoPedidoTotalDTO.getValorDescuento());
	   		
	    		if(vistaPedidoDTO.getCodigoConsolidado() == null){
		    		//se calculan los valores finales del detalle
		    		CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
	    		} 
	        
	        session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
	        session.setAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL,detallePedidoOriginal);
	        //se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos 
//	        session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
//	        session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
	      //subClasificacion para articulos tipo canastos 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA, valorTipoArticuloCanastas);
			
			//subClasificacion para articulos tipo despensas 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
	  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA, valorTipoArticuloDespensas);
	        
	        //VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
	    	session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Registrar pesos finales");
	        //se inicializa la sub-p\u00E1gina que se mostrar\u00E1
	        session.setAttribute(CotizarReservarAction.SUB_PAGINA,"detalleConfirmarReservacion.jsp");
	        //se actualiza el saldo del pedido
	        actualizarSaldoAbonoPedido(request, formulario);

	        //se elimina de sesion el parametro
	        session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
	        
		} else{
			warnings.add("pedidoModificado", new ActionMessage("warnings.pedido.modificado.anteriormente", vistaPedidoDTO.getId().getCodigoPedido()));
			salida = "listaReservasPorConfirmar";  
			saveWarnings(request, warnings);
			return mapping.findForward(salida);
		}
		
    	        
    	}
      /*------------------------------ cuando se actualiza la lista de detalles ----------------------------------*/
      else if(request.getParameter("actualizarDetalle")!=null)
      {
        //se obtiene de la sesion los datos del detalle y de la lista de articulos.
        ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
        //solo si el detalle del pedido no est\u00E1 vacio
        if(detallePedido!=null && !detallePedido.isEmpty())
        {
          try{
            formulario.actualizarDescuentos(request, warnings);
          }catch(Exception ex){
            errors.add("errorDescuentos",new ActionMessage("errors.llamadaServicio.general"));
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          }
          //llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
          CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
    			
          //lamada al m\u00E9todo que actualiza el valor del saldo del abono
          actualizarSaldoAbonoPedido(request, formulario);
          
          Object numBonosObject = request.getSession().getAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
  			if (numBonosObject != null && ((Integer) numBonosObject) > 0 ) {
  				formulario.setNumBonNavEmp(String.valueOf((Integer) numBonosObject));
  			}
        }
        //se actualiza el detalle de la cotizaci\u00F3n que est\u00E1 en sesi\u00F3n
        session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
        session.removeAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR);
	    
        formulario.setChecksSeleccionar(null);
        formulario.setCheckSeleccionarTodo(null);
        formulario.setCheckActualizarStockAlcance(null);
      }
      //------------ cuando se desea volver a la secci\u00F3n de b\u00FAsqueda
      else if(request.getParameter("volverBuscar")!=null){
      	//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
      	WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.regresarABusqueda", 
      			"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
      }
      //------------ cuando se responde SI para volver a la pantalla de b\u00FAsqueda de cotizaciones y recotizaciones
      else if((formulario.getBotonSi()!=null && formulario.getBotonSi().equals("siVolverBusqueda")) || request.getParameter("siVolverBusqueda") != null){
        session.setAttribute(ListadoPedidosAction.VOLVER_A_BUSQUEDA,"ok");
        session.removeAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO);
        salida = "listaReservasPorConfirmar";
      }
    	//cuando presiona el boton de cancelar la confirmacion de pesos finales
      else if(request.getParameter("cancelarPesosFinales")!=null){
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
      }
      //cuando presiona el boton de aceptar la confirmacion de pesos finales
      else if(request.getParameter("registrarPesosFinales")!=null){
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
    	  boolean existeCambios = false;
      	//primero se verifican los valores del detalle
        	verificarValoresDetallePedido(request, errors, formulario);
        	//Verifico si se realiz\u00F3 algun cambio de pesos en el pedido.
        	if(CotizacionReservacionUtil.verificarPreciosDetallePedido(request)){
        		existeCambios = true;
        	}  
        	
        	VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
        	
        //se valida que el pedido en session sea el actual en la BDD
  		Boolean pedidoActual = CotizacionReservacionUtil.verificarPedidoActual(vistaPedidoDTO); //pedidoActual = false;
  		if(pedidoActual){
  	      	if(existeCambios){
  	          	//solo no hay errores
  	              	if(errors.isEmpty()){
  	              		//se obtiene la vista pedido
//  	              		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
  	            		
  	              		//se formatean los totales del pedido antes de almacenarlos
  	              		Double descuentoFormateado = Util.roundDoubleMath(formulario.getDescuentoTotal(),NUMERO_DECIMALES);
  	              		Double subTotalFormateado = Util.roundDoubleMath(formulario.getSubTotal(),NUMERO_DECIMALES);
  	              		Double subTotalAplicaIVAFormateado = Util.roundDoubleMath(formulario.getSubTotalAplicaIVA(),NUMERO_DECIMALES);
  	              		Double subTotalNoAplicaIVAFormateado = Util.roundDoubleMath(formulario.getSubTotalNoAplicaIVA(),NUMERO_DECIMALES);
  	              		Double ivaTotalFormateado = Util.roundDoubleMath(formulario.getIvaTotal(),NUMERO_DECIMALES);
  	              		Double totalPedidoFormateado = Util.roundDoubleMath(formulario.getTotal(),NUMERO_DECIMALES);

  	              		vistaPedidoDTO.setDescuentoTotalPedido(descuentoFormateado);
  	              		vistaPedidoDTO.setPorcentajeTotalDescuento((Double)session.getAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO));
  	              		LogSISPE.getLog().info("porcentaje total descuento: {}",vistaPedidoDTO.getPorcentajeTotalDescuento());
  	              		double porcentajeSubTotalDescuento= vistaPedidoDTO.getDescuentoTotalPedido().doubleValue()*100 / subTotalFormateado.doubleValue();
  	              		vistaPedidoDTO.setPorcentajeSubTotalDescuento(new Double(porcentajeSubTotalDescuento));
  	              		vistaPedidoDTO.setSubTotalPedido(subTotalFormateado);
  	              		vistaPedidoDTO.setSubTotalAplicaIVA(subTotalAplicaIVAFormateado);
  	              		vistaPedidoDTO.setSubTotalNoAplicaIVA(subTotalNoAplicaIVAFormateado);
  	              		vistaPedidoDTO.setIvaPedido(ivaTotalFormateado);
  	              		vistaPedidoDTO.setTotalPedido(totalPedidoFormateado);
  	              		//se actualizan los descuentos
  	              		Collection<?> descuentos = (Collection<?>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
  	              		vistaPedidoDTO.setDescuentosEstadosPedidos(descuentos);

  	              		//se cargan los datos del contacto a la vista pedido
  	              		if(formulario.getOpTipoDocumento()!=null 
  	        					&& formulario.getOpTipoDocumento().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"))) {
  	              			
  	              			vistaPedidoDTO.setCedulaContacto(formulario.getNumeroDocumentoContacto());
  	                  		vistaPedidoDTO.setNombreContacto(formulario.getNombreContacto());
  	              		}
  	              		else{
  	    	          		vistaPedidoDTO.setCedulaContacto(formulario.getNumeroDocumentoPersona());
  	    	          		vistaPedidoDTO.setNombreContacto(formulario.getNombrePersona());
  	              		}
  	              		
  	              		//se obtiene el detalle del Pedido
  	              		Collection<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
  	              		try{
  	             			
  	              			//lamada al m\u00E9todo que registra la confirmaci\u00F3n de la reservaci\u00F3n
  	              			vistaPedidoDTO.setNpEsModificacionPesos(estadoActivo);
  	              			
  	              			Object numBonosObject = request.getSession().getAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
		  	      			if (numBonosObject != null && ((Integer) numBonosObject) > 0 ) {
		  	      				vistaPedidoDTO.setNumBonNavEmp((Integer) numBonosObject);
		  	      			}
  	              			
  	              			SessionManagerSISPE.getServicioClienteServicio().transRegistrarConfirmacionPesos(vistaPedidoDTO,detallePedido);

  	              			//se elimina la variable de sesi\u00F3n que indica que la reservacion est\u00E1 en proceso
  	              			session.removeAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO);
  	              			session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "B\u00FAsqueda de reservaciones por confirmar");
  	              			//mensaje de exito para el registro
  	              			ControlMensajes controlMensajes = new ControlMensajes();
  	              			controlMensajes.setMessages(session,"message.exito.reservacion.confirmada");
  	              			salida = "listaReservasPorConfirmar";
  	              		}catch(SISPEException ex){
  	              			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
  	              			errors.add("numeroCotizacion",new ActionMessage("errors.llamadaServicio.registrarDatos","la Cotizaci\u00F3n"));
  	              			LogSISPE.getLog().info("CODIGO ERRROR: {}",ex.getCodigoError());
  	              			if(ex.getCodigoError()!=null 
  	              					&& ex.getCodigoError().equals(SessionManagerSISPE.getCodExceptionRegistrarNuevamente(request))){
  	              				errors.add("Exception",new ActionMessage("errors.SISPEException.registrarNuevamente",ex.getDescripcionError(),"Presionando el bot\u00F3n Guardar"));
  	              			}else
  	              				errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));

  	              			salida="desplegar";
  	              		}
  	              	}
  	          	}else{
  	          		errors.add("confirmacionPesos",new ActionMessage("errors.confirmacionPesosFinales.detallePedido"));
  	          	}
  		}else{
  			//cuando el pedido ha sido modificado desde otra sesion
  			CotizacionReservacionUtil.instanciarVentanaPedidoModificado(request, "confirmacionReservacion.do");
  		}
      }
    	
    	//------------ cuando se registra la confirmaci\u00F3n de la reservaci\u00F3n
      else if(request.getAttribute("regConfirmacionReservacion")!=null){
			CotizacionReservacionUtil.instanciarVentanaConfirmacionGuardarPedido(request, "confirmacionReservacion.do");
      }
    	/**
		 * @author bgudino
		 * Control para los tabs de contacto y pedidos
		 */
		else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
			
			if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
				//ContactoUtil.cambiarTabContactoPedidos(beanSession, 0);
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
			}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
				//ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
			}
		}else if (request.getParameter("cerrarPopUpBloqueoReserva") != null) {
			session.removeAttribute(SessionManagerSISPE.POPUP);
			salida = "lista";
		}
    }catch(Exception ex){
    	//excepcion desconocida
    	LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    	salida="errorGlobal";
    }
    //se guardan los mensajes generados
	saveWarnings(request, warnings);
	saveErrors(request, errors);
	saveInfos(request, infos);
	
	
	//se guarda el beanSession
	SessionManagerSISPE.setBeanSession(beanSession, request);
    
    return mapping.findForward(salida);
  }
  
  /**
   * Actualiza el saldo del abono del pedido en base al total del pedido
   * @param  request			La petici\u00F3n manejada actualmente
   * @param  formulario		El formulario asociado a la acci\u00F3n
   * @throws Exception
   */
  private void actualizarSaldoAbonoPedido(HttpServletRequest request,
  		CotizarReservarForm formulario)throws Exception{
    //se obtiene la vistaPedidoDTO
    VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
  	double totalPedido = formulario.getTotal(); //total del pedido
    double valorAbonado = vistaPedidoDTO.getAbonoPedido(); //valor abonado hasta el momento
    double saldoAbono = totalPedido - valorAbonado;
    
  	//se almacena el nuevo saldo
  	request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.saldoAbono",Util.roundDoubleMath(Double.valueOf(saldoAbono),NUMERO_DECIMALES));
  }
  
  /**
   * Verifica que los valores de los pesos en el detalle no se hayan modificado antes de 
   * registrar la confirmaci\u00F3n.
   * @param detallePedido				Colecci\u00F3n del detalle del pedido.
   * @param errors							Collecci\u00F3n de errores manejados en el formulario.
   * @param session							La sesi\u00F3n actual.
   * @param parametroMensaje 		El par\u00E1metro para el <code>ActionMessage</code> en caso de error.
   * @throws Exception.
   */
  public void verificarValoresDetallePedido(HttpServletRequest request, ActionMessages errors, 
  		CotizarReservarForm formulario) throws Exception{
  	
    //se obtiene la colecci\u00F3n de detalles del pedido de la sesi\u00F3n
    Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
    //se obtiene la clave que indica un estado activo
    //String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
  	boolean detalleModificado = false;  //bandera para saber si se realizaron cambios
  	//String filasError = "";
  	//Integer rangoModificacionCantidad = Integer.valueOf((String)request.getSession().getAttribute(VALOR_RANGO_ACTUALIZACION_CANTIDAD));
    if(request.getSession().getAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR)==null){
    	LogSISPE.getLog().info("detalleSinActualizar");
      if(detallePedido!=null && !detallePedido.isEmpty()){
      	//variables auxiliares
      	int indiceVector = 0; //indice de la colecci\u00F3n
      	for(DetallePedidoDTO detallePedidoDTO:detallePedido){
      		EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
      		
      		//se verifica si existen art\u00EDculos con el total en cero
      		if(estadoDetallePedidoDTO.getValorTotalEstado().doubleValue() == 0 || estadoDetallePedidoDTO.getValorTotalEstadoIVA().doubleValue() == 0){
      			errors.add("detallesEnCero",new ActionMessage("errors.detallePedido.valorTotalCero"));
      			break;
      		}
      			
      		try{
      			//solo para los art\u00EDculos que son pavos
      			if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
      				LogSISPE.getLog().info("ARTICULO DE PESO VARIABLE: {}",detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
      				//control de cambios en el campo peso
      				Double peso = Util.roundDoubleMath(Double.valueOf(formulario.getVectorPeso()[indiceVector]),NUMERO_DECIMALES);
      				Double pesoRegistradoLocal = Util.roundDoubleMath(estadoDetallePedidoDTO.getPesoRegistradoLocal(),NUMERO_DECIMALES);
      				LogSISPE.getLog().info("peso ingresado: {}",peso);
      				if(peso.doubleValue() > 0){
      					if(pesoRegistradoLocal!=null && pesoRegistradoLocal != peso.doubleValue()){
      						estadoDetallePedidoDTO.setPesoRegistradoLocal(peso);
      						detalleModificado=true;
      						LogSISPE.getLog().info("PESO MODIFICADO EN LA FILA: {}",indiceVector);
      					}
      					LogSISPE.getLog().info("valor total de venta: {}",estadoDetallePedidoDTO.getValorTotalVenta());
      				}
      				
      				/*estadoDetallePedidoDTO.setNpEstadoInformativo(null);
      				//se actualiza la cantidad en caso de reajustes
      				Long cantidadAjustada = Long.valueOf(formulario.getVectorCanAjuModPesos()[indiceVector]);
      				Long cantidadCalculada = Long.valueOf(formulario.getVectorCantidad()[indiceVector]);
      				if(cantidadAjustada <= 0 || (cantidadAjustada > (cantidadCalculada + rangoModificacionCantidad) || cantidadAjustada < (cantidadCalculada - rangoModificacionCantidad))){
      					filasError = filasError.equals("") ? filasError.concat(String.valueOf(indiceVector)) : filasError.concat(",").concat(String.valueOf(indiceVector));
      					estadoDetallePedidoDTO.setNpEstadoInformativo("1");
      				}
      				estadoDetallePedidoDTO.setCantidadAjustadaModificacionPeso(cantidadAjustada);*/
      			}else{
    					LogSISPE.getLog().info("valor total de venta: {}",estadoDetallePedidoDTO.getValorTotalVenta());
    					estadoDetallePedidoDTO.setPesoRegistradoLocal(null);
    					//estadoDetallePedidoDTO.setCantidadAjustadaModificacionPeso(null);
      			}
          }catch(Exception ex){
          	LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          }
          indiceVector++;
        }
      	
      	//control para mostrar el mensaje de error
      	/*if(!StringUtils.isEmpty(filasError)){
      		errors.add("cantidadModificada",new ActionMessage("errors.filasNoProcesadas.confirmacionPesos", filasError, rangoModificacionCantidad));
      	}*/
      }
    }else
    	detalleModificado = true;
    
    //solo si el detalle fue modificado
    if(detalleModificado){
    	request.getSession().setAttribute(CotizarReservarAction.DETALLE_SIN_ACTUALIZAR,"ok");
      errors.add("detalleModificado",new ActionMessage("errors.detalle.sinActualizar","los Pesos Finales"));
    }
  }
  
  /**
	 * PopUp para notificar que no se puede modificar una reserva
	 * @param request
	 * @param numeroReserva EL numeroReserva que esta bloqueado para modificaciones 
	 * @param bloqueoNotaCredito <code>TRUE</code> si el bloqueo es por nota de credito activa
	 * @param bloqueoPOS <code>TRUE</code> si el bloqueo es porque la reserva se realizando el pago en el pos
	 * @throws Exception
	 */
	private void instanciarPopUpNotificacionBloqueoReserva(HttpServletRequest request, String numeroReserva, Boolean bloqueoNotaCredito, Boolean bloqueoPOS, Boolean bloqueoPedidoEntregado, String tipoDocumento)throws Exception{
		
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		
		if(bloqueoNotaCredito){
			popUp.setTituloVentana("Reserva con " +tipoDocumento+" pendiente");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append(numeroReserva)
					.append(" tiene "+tipoDocumento+" pendiente. Ac\u00E9rquese al Punto de Venta").toString());
		}else if(bloqueoPOS){
			popUp.setTituloVentana("Reserva bloqueada en el punto de venta");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append("<strong>").append(numeroReserva).append("</strong>")
					.append(" actualmente se est\u00E1 pagando en el punto de venta, no se puede modificar").toString());
		}else if(bloqueoPedidoEntregado){
			popUp.setTituloVentana("Reserva entregada parcialmente");
			popUp.setPreguntaVentana(new StringBuilder().append("La reserva No. ").append(numeroReserva)
					.append(" se encuentra ENTREGADA PARCIALMENTE. No puede ser modificada.").toString());
		}
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('confirmacionReservacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cerrarPopUpBloqueoReserva=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;		
	}
}
