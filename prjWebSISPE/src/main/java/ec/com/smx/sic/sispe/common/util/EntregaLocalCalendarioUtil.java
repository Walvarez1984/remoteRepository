/*
 * EntregaLocalCalendarioUtil.java
 * Creado el 14/09/2012 10:06:14
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PREFIJO_VARIABLE_SESION;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_DOMICIO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_MI_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_OTRO_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.RESPONSABLE_BODEGA_ABASTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.RESPONSABLE_BODEGA_CANASTOS;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.RESPONSABLE_BODEGA_TRANSITO;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.RESPONSABLE_LOCAL;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.annotations.RelationField.JoinType;
import ec.com.kruger.utilitario.dao.commons.dto.ListSet;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.dto.AreaSublugarTrabajoDTO;
import ec.com.smx.corpv2.dto.AreaTrabajoDTO;
import ec.com.smx.corpv2.dto.DivisionGeoPoliticaDTO;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.gestor.util.DateManager;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.BodegaDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.TransporteDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.CantidadCalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.commons.util.dto.EstructuraResponsable;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleHoraCamionLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalID;
import ec.com.smx.sic.sispe.dto.CalendarioHoraCamionLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioHoraLocalDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.CostoEntregasDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.DireccionesDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author bgudino
 *
 */
@SuppressWarnings({"unchecked","deprecation"})
public class EntregaLocalCalendarioUtil {
	
public static final String CALENDARIO_HORA_LOCAL_SELECCIONADO = "ec.com.smx.sic.sispe.calendario.hora.local.seleccionado"; //guarda los datos del dia y hora seleccionado 
public static final String CALENDARIO_HORA_LOCAL_PROCESADO = "ec.com.smx.sic.sispe.calendario.hora.local.procesado"; //guarda los datos del dia y hora seleccionado descontado los camiones del pedido
public static final String CAL_HORA_LOCAL_SELECCIONADOS_COL = "ec.com.smx.sic.sispe.calendario.hora.local.seleccionados.col"; //guarda los datos de todas las entregas del pedido
public static final String CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO = "ec.com.smx.sic.sispe.calendario.detalle.hora.camion.local.seleccionado";//contiene los detalles cuando se elimina la entrega
public static final String DIA_HORA_SELECCIONADO 	= "ec.com.smx.sic.sispe.dia.hora.seleccionado";

public static final String CHECKS_ENTREGAS_TRANSITO = PREFIJO_VARIABLE_SESION.concat("entregas.checksTransito");
public static final String FECHA_MINIMA_DESPACHO = PREFIJO_VARIABLE_SESION.concat("fecha.minima.despacho");

	/**
	 * Calcula la cantidad de bultos disponibles por dia-hora
	 * 
	 * @param formulario
	 * @param fecha
	 * @param localID
	 * @return
	 */
	@Deprecated
	public static Collection<CalendarioHoraLocalDTO> obtenerBultosDisponiblesCiudadSectorFecha(CotizarReservarForm formulario,  Date fecha, LocalID localID, HttpServletRequest request){
		try{	
			LogSISPE.getLog().info("ingresa el metodo obtenerBultosDisponiblesCiudadSectorFecha ");
			//se arma la consulta para ese dia
			CalendarioHoraLocalDTO calendarioHoraConsulta = new CalendarioHoraLocalDTO();
			calendarioHoraConsulta.getId().setCodigoCompania(localID.getCodigoCompania());
			calendarioHoraConsulta.getId().setCodigoLocal(localID.getCodigoLocal());
			calendarioHoraConsulta.getId().setFechaCalendarioDia(new java.sql.Date(fecha.getTime()));	
			calendarioHoraConsulta.setCalendarioHoraCamionLocalCol(new HashSet<CalendarioHoraCamionLocalDTO>());
			
			CalendarioHoraCamionLocalDTO calendarioHoraCamionLocal = new CalendarioHoraCamionLocalDTO();
			calendarioHoraCamionLocal.setTransporteDTO(new TransporteDTO());			
			calendarioHoraCamionLocal.setCalendarioDetalleHoraCamionLocalCol(new HashSet<CalendarioDetalleHoraCamionLocalDTO>());
			calendarioHoraCamionLocal.getCalendarioDetalleHoraCamionLocalCol().add(new CalendarioDetalleHoraCamionLocalDTO());
			
			calendarioHoraConsulta.getCalendarioHoraCamionLocalCol().add(calendarioHoraCamionLocal);
			
			Collection<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol = SISPEFactory.getDataService().findObjects(calendarioHoraConsulta);			
			
			//si ese dia existen detalles
			if(CollectionUtils.isNotEmpty(calendarioHoraLocalDTOCol)){
				calendarioHoraLocalDTOCol = unirCalendariosHoraLocal(calendarioHoraLocalDTOCol, request);
				
				String codigoCiudad =null;
				if(formulario.getSeleccionCiudad() != null){
					if(formulario.getSeleccionCiudad().split("/").length > 1){
						codigoCiudad = formulario.getSeleccionCiudad().split("/")[1];
					}else{
						codigoCiudad = formulario.getSeleccionCiudad();
					}
				}
				String codigoCiudadSector = formulario.getSelecionCiudadZonaEntrega();
				
				Integer totalBultos = 0;		
				
				for(CalendarioHoraLocalDTO calendarioHora : calendarioHoraLocalDTOCol){								
					totalBultos = 0;
					
					Collection<CalendarioHoraCamionLocalDTO>  calendarioHoraCamionLocalCol = calendarioHora.getCalendarioHoraCamionLocalCol();
					if(CollectionUtils.isNotEmpty(calendarioHoraCamionLocalCol)){																									
						
						//se recorren los registros encontrados para esa hora
						for(CalendarioHoraCamionLocalDTO transporteHora: calendarioHoraCamionLocalCol)	{
							
							//quiere decir que esta disponible toda la capacidad del camion
							if(CollectionUtils.isEmpty(transporteHora.getCalendarioDetalleHoraCamionLocalCol())){
								totalBultos += transporteHora.getTransporteDTO().getCantidadBultos();
							}
							//toca sumar solo los camiones que pertenecen a la ciudad-sector
							else{
								int sumaBultosParcial = 0;
								Boolean agregarCamion = Boolean.TRUE;
								for(CalendarioDetalleHoraCamionLocalDTO detalleCamionHora : transporteHora.getCalendarioDetalleHoraCamionLocalCol() ){
									//se valida que este en estado activo
									if(detalleCamionHora.getEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
										agregarCamion = Boolean.FALSE;
																											
										//se buscan solo camiones del mismo sector
										if((StringUtils.isNotEmpty(detalleCamionHora.getCodigoSectorEntrega()) && StringUtils.isNotEmpty(codigoCiudad))
												&& detalleCamionHora.getCodigoSectorEntrega().equals(codigoCiudad)){
											if((!detalleCamionHora.getCodigoCiudadSectorEntrega().isEmpty() &&  !codigoCiudadSector.isEmpty())){
												if(  detalleCamionHora.getCodigoCiudadSectorEntrega().equals(codigoCiudadSector)){
													agregarCamion = Boolean.TRUE;
													sumaBultosParcial += detalleCamionHora.getCapacidadUtilizada();
												}												
											}
											//esa ciudad no tiene sector 
											else{
												agregarCamion = Boolean.TRUE;
												sumaBultosParcial += detalleCamionHora.getCapacidadUtilizada();
											}
										}										
									}
								}
								if(agregarCamion)
									totalBultos += transporteHora.getTransporteDTO().getCantidadBultos() - sumaBultosParcial;
							}
						}					
					}
					calendarioHora.setNpCantidadBultosDisponibles(totalBultos.toString());
				}
			}	
			Set<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux = new HashSet<CalendarioHoraLocalDTO>();
			for(CalendarioHoraLocalDTO horaLocal : calendarioHoraLocalDTOCol)
				calendarioHoraLocalDTOColAux.add(horaLocal);
//			return calendarioHoraLocalDTOCol;
			return calendarioHoraLocalDTOColAux;
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener los bultos disponibles ",e);
			return null;
		}
		
	}
	
	
	/**
	 * Asigna los camiones al pedido
	 * @param request
	 * @param calendarioHoraLocalDTO
	 * @param localId
	 * @param formulario
	 * @param errors
	 * @param warnings
	 * @throws Exception
	 */
	
	@Deprecated
	public static void descontarBultosPorHora(HttpServletRequest request,  CalendarioHoraLocalDTO calendarioHoraLocalDTO, int totalBultosPedido, LocalID localId, CotizarReservarForm formulario, ActionMessages errors, ActionMessages warnings) throws Exception{
		try{
			LogSISPE.getLog().info("ingresa al metodo descontarBultosPorHora");
			//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
			//Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			Collection<DetallePedidoDTO> colDetallePedidoDTO=(Collection<DetallePedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
			//Collection calendarioDetalleHoraCamionLocalCol = (HashSet<CalendarioDetalleHoraCamionLocalDTO>)request.getSession().getAttribute(CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
			
			Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
			if(CollectionUtils.isEmpty(calendarioHoraLocalCol)){
				calendarioHoraLocalCol = new HashSet<CalendarioHoraLocalDTO>();
			}
			
			//se calcula el total de bultos que tiene el pedido
			if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){	
				
				DetallePedidoDTO detallePedidoDTO =  colDetallePedidoDTO.iterator().next();							
				
				//si la cantida de bultos de esa hora alcanza
				if(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) >= totalBultosPedido){

					String codigoCiudad = null;
					if(formulario.getSeleccionCiudad() != null){
						if(formulario.getSeleccionCiudad().split("/").length > 1){
							codigoCiudad = formulario.getSeleccionCiudad().split("/")[1];
						}
						else{
							codigoCiudad = formulario.getSeleccionCiudad();
						}
					}
					String codigoCiudadSector = null;
					if(formulario.getSelecionCiudadZonaEntrega() != null && !formulario.getSelecionCiudadZonaEntrega().isEmpty())
						codigoCiudadSector = formulario.getSelecionCiudadZonaEntrega();
					
					//se asignan los bultos a los camiones
					Collection<CalendarioHoraCamionLocalDTO> listaCamionesHoraLocalAux = calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol();
					ColeccionesUtil.sort(listaCamionesHoraLocalAux, ColeccionesUtil.ORDEN_DESC, "transporteDTO.cantidadBultos");
						//se asignan los bultos a los camiones
					int residuo = -1;						
					
					Collection<CalendarioDetalleHoraCamionLocalDTO> calendarioDetalleHoraCamionCol = new HashSet<CalendarioDetalleHoraCamionLocalDTO>();
					ArrayList<CantidadTransporteCapacidad> confCamiones = new ArrayList<CantidadTransporteCapacidad>();
												
					//se procede a asignar el pedido en varios camiones					
					ArrayList<CantidadTransporteCapacidad> cantidadCamionesCol = crearListaCamionesCapacidad(listaCamionesHoraLocalAux, codigoCiudad, codigoCiudadSector);
					ColeccionesUtil.sort(cantidadCamionesCol, ColeccionesUtil.ORDEN_DESC, "transporteDTO.cantidadBultos");
					LogSISPE.getLog().info(" coleccion camiones ordenados {}",cantidadCamionesCol);
																	
					ArrayList<CantidadTransporteCapacidad> camionesEmpleadosCol = new ArrayList<CantidadTransporteCapacidad>();
											
					String camionesUsados = "";
					
					int residuoTemporal = 0;
					int residuoCamion = 0;
					residuo = -1;//totalBultosPedido;
					//ahora se procede a asignar el pedido en camiones
					for(int i=0; i<cantidadCamionesCol.size(); i++){
						
						camionesEmpleadosCol = new ArrayList<CantidadTransporteCapacidad>();							
						CantidadTransporteCapacidad nivel1 = new CantidadTransporteCapacidad(cantidadCamionesCol.get(i));
													
						if(nivel1.getCantidadDisponible() >= totalBultosPedido){
							nivel1.setCantidadUtilizada(totalBultosPedido);
							residuoCamion = nivel1.getCantidadDisponible().intValue() - totalBultosPedido;
							residuoTemporal = 0;													
						}
						else {
							residuoTemporal = totalBultosPedido-nivel1.getCantidadDisponible();							
							nivel1.setCantidadUtilizada(nivel1.getCantidadDisponible());
							residuoCamion = 0;
						}
						
						camionesEmpleadosCol.add(nivel1);
						camionesUsados = nivel1.getTransporteDTO().getNombreTransporte()+"("+nivel1.getCapacidadCamion()+") :" +nivel1.getCantidadUtilizada() ;
						
						//es la primera vez que asigna una configuracion
						if(residuoTemporal == 0 && residuo == -1){
							residuo = residuoCamion;
							confCamiones = new ArrayList<CantidadTransporteCapacidad>();
							confCamiones.addAll(camionesEmpleadosCol);
							LogSISPE.getLog().info("configuracion optima {}  residuo camion {}", camionesUsados, residuo);
						}
						else if(residuoTemporal == 0 && residuo > residuoCamion){
							residuo = residuoCamion;
							confCamiones = new ArrayList<CantidadTransporteCapacidad>();
							confCamiones.addAll(camionesEmpleadosCol);
							LogSISPE.getLog().info("configuracion optima {}  residuo camion {}", camionesUsados, residuo);
						}
						
						if(residuoTemporal > 0){
							for(int j=0; j<cantidadCamionesCol.size(); j++){
								
								CantidadTransporteCapacidad nivel2 =  new CantidadTransporteCapacidad(cantidadCamionesCol.get(j));
								
								if(j != i){
									//todavia existen bultos sin asignar camiones
									if(residuoTemporal > 0){	
																					
										camionesUsados += " -- "+nivel2.getTransporteDTO().getNombreTransporte()+"("+nivel2.getCapacidadCamion()+"):" ;
										
										if(residuoTemporal >= nivel2.getCantidadDisponible()){	
											nivel2.setCantidadUtilizada(nivel2.getCantidadDisponible());
											camionesEmpleadosCol.add(nivel2);
											residuoTemporal -= nivel2.getCantidadDisponible();
											camionesUsados += nivel2.getCantidadDisponible();
											residuoCamion = 0;
										}
										else{		
											nivel2.setCantidadUtilizada(residuoTemporal);
											camionesEmpleadosCol.add(nivel2);
											residuoCamion = nivel2.getCantidadDisponible() - residuoTemporal;
											camionesUsados += residuoTemporal;
											residuoTemporal = 0;
										}
										
										if(residuoTemporal == 0 && residuo > residuoCamion){
											residuo = residuoCamion;
											confCamiones = new ArrayList<CantidadTransporteCapacidad>();
											confCamiones.addAll(camionesEmpleadosCol);
											LogSISPE.getLog().info("configuracion optima {}  residuo camion {}", camionesUsados, residuo);
										}									
									}else 
										j= cantidadCamionesCol.size();										
								}
							}
						}				
					}
						//se construyen los detalles
					if(CollectionUtils.isNotEmpty(confCamiones)){
						
						//la colleccion resultante de los camiones seleccionados 
						 calendarioDetalleHoraCamionCol = new HashSet<CalendarioDetalleHoraCamionLocalDTO>();
						for(CantidadTransporteCapacidad camionActual : confCamiones){
							CalendarioDetalleHoraCamionLocalDTO calDetalleHoraCamion = new CalendarioDetalleHoraCamionLocalDTO();
							
							//id
							calDetalleHoraCamion.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());
							
							//relacion con el pedido
							calDetalleHoraCamion.setCodigoAreaTrabajo(detallePedidoDTO.getId().getCodigoAreaTrabajo());
							calDetalleHoraCamion.setCodigoPedido(detallePedidoDTO.getId().getCodigoPedido());
							
							//relacion con calendarioHoraCamionLocal
							calDetalleHoraCamion.setFechaCalendarioDia(new java.sql.Date(calendarioHoraLocalDTO.getId().getFechaCalendarioDia().getTime()));
							calDetalleHoraCamion.setCodigoLocal(localId.getCodigoLocal());										
							calDetalleHoraCamion.setHora(calendarioHoraLocalDTO.getId().getHora());		
							calDetalleHoraCamion.setCodigoTransporte(camionActual.getTransporteDTO().getId().getCodigoTransporte());
							calDetalleHoraCamion.setSecuencialCalHorCamLoc(camionActual.getSecuencialHoraCamion());
							
							calDetalleHoraCamion.setEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
							calDetalleHoraCamion.setCapacidadUtilizada((long)camionActual.getCantidadUtilizada());											
							
							//campos de auditoria
							calDetalleHoraCamion.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
							
							//relacion divisionGeopolitica
							calDetalleHoraCamion.setCodigoSectorEntrega(codigoCiudad);
							calDetalleHoraCamion.setCodigoCiudadSectorEntrega(codigoCiudadSector);
							calendarioDetalleHoraCamionCol.add(calDetalleHoraCamion);	
							
							for(CalendarioHoraCamionLocalDTO calendarioCamionHoraActual : listaCamionesHoraLocalAux){
								if(calendarioCamionHoraActual.getId().getCodigoTransporte().intValue() == camionActual.getTransporteDTO().getId().getCodigoTransporte()
										&& calendarioCamionHoraActual.getId().getSecuencialCalHorCamLoc().intValue() == camionActual.getSecuencialHoraCamion() ){
									if(CollectionUtils.isEmpty(calendarioCamionHoraActual.getCalendarioDetalleHoraCamionLocalCol()))
										calendarioCamionHoraActual.setCalendarioDetalleHoraCamionLocalCol(new HashSet<CalendarioDetalleHoraCamionLocalDTO>());
										
									calendarioCamionHoraActual.getCalendarioDetalleHoraCamionLocalCol().add(calDetalleHoraCamion);
									
									break;
								}
							}
						}
						//se setean los datos en del objeto padre
						calendarioHoraLocalDTO.setCalendarioHoraCamionLocalCol(listaCamionesHoraLocalAux); 
						CalendarioDiaLocalDTO calDiaLocal = new CalendarioDiaLocalDTO();
						calDiaLocal.getId().setCodigoCompania(calendarioHoraLocalDTO.getId().getCodigoCompania());
						calDiaLocal.getId().setCodigoLocal(calendarioHoraLocalDTO.getId().getCodigoLocal());
						calDiaLocal.getId().setFechaCalendarioDia(calendarioHoraLocalDTO.getId().getFechaCalendarioDia());
						
						CalendarioDiaLocalDTO calDiaLocalEncontrado = SISPEFactory.getDataService().findUnique(calDiaLocal);
						if(calDiaLocalEncontrado != null){
							calDiaLocalEncontrado.setCantidadDisponible(calDiaLocalEncontrado.getCantidadDisponible() - totalBultosPedido);
						}
						calendarioHoraLocalDTO.setCalendarioDiaLocalDTO(calDiaLocalEncontrado);
						calendarioHoraLocalDTO.setNpCantidadBultosDisponibles(""+(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) - totalBultosPedido));
						//si el registro ya esta en la coleccion, se remplaza el nuevo registro por el anterior
						Boolean campoEncontrado = Boolean.FALSE;
						if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
							//se busca el registro
							for(CalendarioHoraLocalDTO horaLocal : calendarioHoraLocalCol){
								//se comparan los IDs
								if(horaLocal.getId().getCodigoCompania().equals(calendarioHoraLocalDTO.getId().getCodigoCompania())
										&& horaLocal.getId().getCodigoLocal().equals(calendarioHoraLocalDTO.getId().getCodigoLocal().intValue())
										&& horaLocal.getId().getFechaCalendarioDia().toString().equals(calendarioHoraLocalDTO.getId().getFechaCalendarioDia().toString())
										&& horaLocal.getId().getHora().toString().equals(calendarioHoraLocalDTO.getId().getHora().toString())){
									campoEncontrado = Boolean.TRUE;
									horaLocal = calendarioHoraLocalDTO;	
								}
							}
						}
						if(!campoEncontrado){
							//si el campo no existe 
							calendarioHoraLocalCol.add(calendarioHoraLocalDTO);
						}
						request.getSession().setAttribute(CALENDARIO_HORA_LOCAL_PROCESADO, calendarioHoraLocalDTO);
						request.getSession().setAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL, calendarioHoraLocalCol);
					}
				}
				//se agrega el error de capacidad de camiones
				else{
					errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));			
				}
				
			}			
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al descontar los bultos del pedido ",e);	
			errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));
			throw new Exception(e);
		}
		
	}
	
	
	/**
	 * Valida que la cantidad de bultos disponibles para una fecha-sector alcance para cubrir los bultos del pedido
	 * @param request
	 * @param calendarioHoraLocalDTO
	 * @param formulario
	 * @param errors
	 * @param warnings
	 * @param info
	 * @throws SISPEException 
	 */
	@Deprecated
	public static void verificarDisponibilidadCamionesHora(HttpServletRequest request, CalendarioHoraLocalDTO calendarioHoraLocalDTO, CotizarReservarForm formulario, ActionMessages errors, ActionMessages warnings, ActionMessages info) throws SISPEException{
		
		LogSISPE.getLog().info("ingresa al metodo verificarDisponibilidadCamionesHora");
		
		request.getSession().removeAttribute(EntregaLocalCalendarioAction.CALENDARIO_HORA_LOCAL_SELECCIONADO);
		//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
		//Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		Collection<DetallePedidoDTO> colDetallePedidoDTO= (Collection<DetallePedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		
		//se calcula el total de bultos que tiene el pedido
		if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){
			
			//si seleccionaron ciudad de entrega realiza la validacion
			if(formulario.getSeleccionCiudad() != null && !formulario.getSeleccionCiudad().equals("")){
				
				Collection<DivisionGeoPoliticaDTO> zonaCiudadEntrega = (Collection<DivisionGeoPoliticaDTO>) request.getSession().getAttribute(EntregaLocalCalendarioAction.CIUDAD_SECTOR_ENTREGA);
				
				Boolean validarDisponibilidadCamiones = Boolean.TRUE;
				
				//si hay zonas para esa ciudad
				if(CollectionUtils.isNotEmpty(zonaCiudadEntrega)){
					if(formulario.getSelecionCiudadZonaEntrega().isEmpty()){
						//se agrega el error de sector requerido
						warnings.add("campoCiudadRequerido",new ActionMessage("warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora"));	
						validarDisponibilidadCamiones = Boolean.FALSE;
					}
				}
				
				if(validarDisponibilidadCamiones){
					int totalBultosPedido = 0;
					//se obtiene el total de bultos
					for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){								
						if(detalleActual.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0){
							totalBultosPedido += UtilesSISPE.calcularCantidadBultos(detalleActual.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue(), detalleActual.getArticuloDTO());
						}
					}
					
					//si la cantida de bultos de esa hora alcanza
					if(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) >= totalBultosPedido){
						request.getSession().setAttribute(EntregaLocalCalendarioAction.CALENDARIO_HORA_LOCAL_SELECCIONADO, calendarioHoraLocalDTO);
						info.add("seleccionFechaHoraOk" , new ActionMessage("info.seleccion.hora.fecha.entrega"));
					}
					//se agrega el error de capacidad de camiones
					else{
						errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));			
					}						
				}
			} else {
				warnings.add("campoCiudadRequerido",new ActionMessage("warnings.campo.ciudad.requerido"));
			}
		}				
	}
	
	/**
	 * suma los bultos disponibles para una determinada zona  en una fecha y hora 
	 * @param calendarioHoraCamionLocalDTO
	 * @param codigoCiudad
	 * @param codigoCiudadSector
	 * @return
	 */
	public static int sumarBultosDisponiblesCalendarioHoraCamionLocalPorCiudad(CalendarioHoraCamionLocalDTO calendarioHoraCamionLocalDTO, String codigoCiudad, String codigoCiudadSector){
		
		LogSISPE.getLog().info("ingresa el metodo obtenerBultosDisponiblesCiudadSectorFecha ");
		int totalBultos = 0;
		Collection<CalendarioDetalleHoraCamionLocalDTO> detallesHoraCamionCol = calendarioHoraCamionLocalDTO.getCalendarioDetalleHoraCamionLocalCol();
		
		//quiere decir que tiene camiones ocupados
		if(CollectionUtils.isNotEmpty(detallesHoraCamionCol)){
			for(CalendarioDetalleHoraCamionLocalDTO detalleCamionHora : detallesHoraCamionCol){
								
				//se buscan solo camiones del mismo sector
				if( (!detalleCamionHora.getCodigoSectorEntrega().isEmpty() && !codigoCiudad.isEmpty()) && detalleCamionHora.getCodigoSectorEntrega().equals(codigoCiudad) ){
					if((!detalleCamionHora.getCodigoCiudadSectorEntrega().isEmpty() &&  !codigoCiudadSector.isEmpty())){
						if(detalleCamionHora.getCodigoCiudadSectorEntrega().equals(codigoCiudadSector)){
							totalBultos += detalleCamionHora.getCalendarioHoraCamionLocalDTO().getTransporteDTO().getCantidadBultos() - detalleCamionHora.getCapacidadUtilizada();
						}												
					}
					//esa ciudad no tiene sector 
					else{
						totalBultos += detalleCamionHora.getCalendarioHoraCamionLocalDTO().getTransporteDTO().getCantidadBultos() - detalleCamionHora.getCapacidadUtilizada();
					}
				}												
			}			
		}
		//el camion esta disponible en su totalidad
		else
			totalBultos = calendarioHoraCamionLocalDTO.getTransporteDTO().getCantidadBultos();
		return totalBultos;		
	}
	
	/**
	 * Llena una  lista con un objeto auxiliar (CantidadTransporteCapacidad)
	 * @param listaCamionesHoraLocalAux
	 * @param codigoCiudad
	 * @param codigoCiudadSector
	 * @return
	 */
	private static ArrayList<CantidadTransporteCapacidad> crearListaCamionesCapacidad(Collection<CalendarioHoraCamionLocalDTO> listaCamionesHoraLocalAux
			,String codigoCiudad, String codigoCiudadSector){
		
		ArrayList<CantidadTransporteCapacidad> cantidadCamionesCol = new ArrayList<CantidadTransporteCapacidad>();						
		
		//se crea una coleccion solo con los camiones libres y los ocupados para esa zona-sector
		for(CalendarioHoraCamionLocalDTO horaCamion : listaCamionesHoraLocalAux){ 
			
		if(CollectionUtils.isEmpty(horaCamion.getCalendarioDetalleHoraCamionLocalCol())){
												
				cantidadCamionesCol.add(new CantidadTransporteCapacidad( horaCamion.getTransporteDTO().getCantidadBultos(), horaCamion.getTransporteDTO(), horaCamion.getId().getSecuencialCalHorCamLoc()));
			}
			//para esa hora existen camiones ocupados
			else{
				int cantidadDisponible = horaCamion.getTransporteDTO().getCantidadBultos().intValue();
				Boolean encontroRegistro = Boolean.FALSE;
				for(CalendarioDetalleHoraCamionLocalDTO detalleCamionHora : horaCamion.getCalendarioDetalleHoraCamionLocalCol() ){
					
					//solo para camiones activos
					if(detalleCamionHora.getEstado().equals(ConstantesGenerales.ESTADO_ACTIVO)){
						
						
						//se buscan solo camiones del mismo sector
						if( (!detalleCamionHora.getCodigoSectorEntrega().isEmpty() && !codigoCiudad.isEmpty()) && detalleCamionHora.getCodigoSectorEntrega().equals(codigoCiudad) ){
							if((!detalleCamionHora.getCodigoCiudadSectorEntrega().isEmpty() &&  !codigoCiudadSector.isEmpty())){
								if(detalleCamionHora.getCodigoCiudadSectorEntrega().equals(codigoCiudadSector)){
									cantidadDisponible -=  detalleCamionHora.getCapacidadUtilizada().intValue();
									encontroRegistro = Boolean.TRUE;
								}												
							}
							//esa ciudad no tiene sector 
							else{
								cantidadDisponible -=  detalleCamionHora.getCapacidadUtilizada().intValue();
								encontroRegistro = Boolean.TRUE;
							}
						}
					}
					else 
						encontroRegistro = Boolean.TRUE;
				}
				if(encontroRegistro){
					cantidadCamionesCol.add(new CantidadTransporteCapacidad( cantidadDisponible, horaCamion.getTransporteDTO(), horaCamion.getId().getSecuencialCalHorCamLoc()));
				}
			}
		}			
		return cantidadCamionesCol;
	}
	
	/**
	 * Cuando se eliminan las entregas, se quitan los camiones asociados a ese pedido
	 * @param entregasCol
	 * @param request
	 * @throws Exception
	 */
	public static void eliminarCamionesDelPedido(List<EntregaDetallePedidoDTO> entregasCol, HttpServletRequest request)throws Exception{
		try{
			LogSISPE.getLog().info("Ingresa al metodo eliminarCamionesDelPedido");
			
			//fecha, cantidaBultos
			Map<String, Long> mapFechaBultos = new HashMap<String, Long>();
			Integer contextoEntrega = Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"));
			Integer responsableStock = Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
			String separador = ";";
			
			//recorro los detalles
			if(CollectionUtils.isNotEmpty(entregasCol)){
				EntregaDetallePedidoDTO entDetPedDTO = entregasCol.get(0);
				for(EntregaDetallePedidoDTO entDetPedActDTO : entregasCol){
					//valida las entregas que utilizan camiones,  a domicilio y pedir al centro de distribucion
					if(entDetPedActDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(contextoEntrega) && entDetPedActDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(responsableStock)){
						//indica la ciudad y sector donde se realizara la entrega
						String llavesCiudadSector = separador+entDetPedActDTO.getEntregaPedidoDTO().getCodigoDivGeoPol()+separador;
						
						//para el caso que la ciudad seleccionada si tenga un sector
						if(entDetPedActDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega() != null && !entDetPedActDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega().equals("")){
							llavesCiudadSector += entDetPedActDTO.getEntregaPedidoDTO().getCodigoCiudadSectorEntrega();
						}else{
							llavesCiudadSector += "null";
						}
						
						String key = entDetPedActDTO.getEntregaPedidoDTO().getFechaEntregaCliente().toString()+llavesCiudadSector;
						
						if(!mapFechaBultos.containsKey(key)){
							mapFechaBultos.put(key, entDetPedActDTO.getNpCantidadBultos().longValue());
						}
						else{
							Long cantidadParcial = mapFechaBultos.get(key);
							cantidadParcial += entDetPedActDTO.getNpCantidadBultos();
							mapFechaBultos.put(key, cantidadParcial);								
						}	
					}
				}
				
				//se recorren los mapas para buscar los camiones de esa fecha
				Set<Map.Entry<String, Long>> valoresMap = mapFechaBultos.entrySet(); 
				
				if(CollectionUtils.isNotEmpty(valoresMap)){					
					//se obtiene la fecha de la entrega					
					String fechaCompleta = valoresMap.iterator().next().getKey();
					SimpleDateFormat formatoFecha = new SimpleDateFormat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.formato.fechaHora"));
					Date fecha = null;
					
					//si el codigoPedido no esta setedo se recupera de sesion el pedido
					if(entDetPedDTO.getCodigoPedido()==null){
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						entDetPedDTO.setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
					}
					
					//se recupera de sesion los detalles modificados
					Collection<CalendarioDetalleHoraCamionLocalDTO> detalleHoraCamionLocalPorActualizar = 
							(Collection<CalendarioDetalleHoraCamionLocalDTO>) request.getSession().getAttribute(CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
					 if(CollectionUtils.isEmpty(detalleHoraCamionLocalPorActualizar)){
						 detalleHoraCamionLocalPorActualizar = new HashSet<CalendarioDetalleHoraCamionLocalDTO>();				
					 }
					for(Map.Entry<String, Long> campoActual : valoresMap) {
						Long totalBultosRestablecer = campoActual.getValue().longValue();																							
						
						String codigoSectorEntrega = campoActual.getKey().split(separador)[1];
						String codigoCiudadSectorEntrega = campoActual.getKey().split(separador)[2];
						fechaCompleta = campoActual.getKey();						
						fecha = formatoFecha.parse(fechaCompleta);												
						LogSISPE.getLog().info("se restableceran: "+totalBultosRestablecer+" bultos del dia: "+fechaCompleta+ " del pedido: "+entDetPedDTO.getCodigoPedido());
						
						//se recupera los objetos de sesion
						Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
						
						if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
							if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
								for(CalendarioHoraLocalDTO calendarioHoraLocalDTO : calendarioHoraLocalCol){
									//Se busca la fecha de la entrega
									if(CollectionUtils.isNotEmpty(calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol())){
										for(CalendarioHoraCamionLocalDTO horaCamionLocal : calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol()){
											if(CollectionUtils.isNotEmpty(horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol())){
												for(CalendarioDetalleHoraCamionLocalDTO detalleHoraCamionLocal : horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol()){
													
													//se comparan los ID
													if(detalleHoraCamionLocal.getId().getCodigoCompania().equals(entDetPedDTO.getId().getCodigoCompania())
															&& detalleHoraCamionLocal.getCodigoPedido().equals(entDetPedDTO.getCodigoPedido())
															&& detalleHoraCamionLocal.getCodigoSectorEntrega().equals(codigoSectorEntrega)
															&& detalleHoraCamionLocal.getCodigoCiudadSectorEntrega().equals(codigoCiudadSectorEntrega)
															&& detalleHoraCamionLocal.getFechaCalendarioDia().toString().equals(new java.sql.Date(fecha.getTime()).toString())
															&& detalleHoraCamionLocal.getHora().toString().equals(new Time(fecha.getTime()).toString())
															&& detalleHoraCamionLocal.getEstado().equals(ConstantesGenerales.ESTADO_ACTIVO)){
														
														//se unen con los detalles procesados anteriormente						
//														horaCamionLocal.setCalendarioDetalleHoraCamionLocalCol(unirDetallesCamion(horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol(), request));
														
														//se busca de que camion descontar los bultos							
														//ordeno los camiones en forma ascendene de acuerdo a su capacidad
														ColeccionesUtil.sort(horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol(), ColeccionesUtil.ORDEN_ASC, "capacidadUtilizada");														
														
															//se van restando los bultos
														if(totalBultosRestablecer > 0){
															
															//el camion contiene todos los bultos
															if(detalleHoraCamionLocal.getCapacidadUtilizada() >= totalBultosRestablecer){
																//se desactiva el registro para que ese camion este disponible para cualquier sector
																if(totalBultosRestablecer == detalleHoraCamionLocal.getCapacidadUtilizada().longValue()){											
																	detalleHoraCamionLocal.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
																}																			
																detalleHoraCamionLocal.setCapacidadUtilizada(detalleHoraCamionLocal.getCapacidadUtilizada() - totalBultosRestablecer);
																totalBultosRestablecer = 0L;
															}
															//el camion tiene parte de los bultos del pedido
															else{
																totalBultosRestablecer -= detalleHoraCamionLocal.getCapacidadUtilizada();
																detalleHoraCamionLocal.setCapacidadUtilizada(0L);
																
																//se desactiva el registro para que ese camion este disponible para cualquier sector
																detalleHoraCamionLocal.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
															}
															if(detalleHoraCamionLocal.getId().getCodCalDetHorCamLoc()!=null){
																detalleHoraCamionLocalPorActualizar.add(detalleHoraCamionLocal);
															}
														}							
//														detalleHoraCamionLocalPorActualizar.addAll(calendarioDetalleHoraCamionLocalCol);
													}
												}
											}
										}
									}
								}
							}
						}
						
//						CalendarioDetalleHoraCamionLocalDTO calendarioConsulta = new CalendarioDetalleHoraCamionLocalDTO();
//						calendarioConsulta.getId().setCodigoCompania(entregaDTO.getId().getCodigoCompania());
//						calendarioConsulta.setCodigoAreaTrabajo(entregaDTO.getId().getCodigoAreaTrabajo());
//						calendarioConsulta.setCodigoSectorEntrega(codigoSectorEntrega);
//						calendarioConsulta.setCodigoCiudadSectorEntrega(codigoCiudadSectorEntrega);
//						calendarioConsulta.setCodigoPedido(entregaDTO.getId().getCodigoPedido());					
//						calendarioConsulta.setFechaCalendarioDia(new java.sql.Date(fecha.getTime()));
//						calendarioConsulta.setHora(new Time(fecha.getTime()));
//						calendarioConsulta.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
//						calendarioConsulta.setCalendarioHoraCamionLocalDTO(new CalendarioHoraCamionLocalDTO());
//						calendarioConsulta.getCalendarioHoraCamionLocalDTO().setTransporteDTO(new TransporteDTO());
//						calendarioConsulta.getCalendarioHoraCamionLocalDTO().setCalendarioHoraLocalDTO(new CalendarioHoraLocalDTO());	
//						
//						//obtengo los camiones asigados a  esa fecha y hora
//						Collection<CalendarioDetalleHoraCamionLocalDTO> calendarioDetalleHoraCamionLocalCol = SISPEFactory.getDataService().findObjects(calendarioConsulta);

//						//se eliminan los camiones de los objetos de session
//						Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
//						
//						if(CollectionUtils.isEmpty(calendarioDetalleHoraCamionLocalCol)){
//							
//							if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
//								for(CalendarioHoraLocalDTO calendarioHoraLocalDTO : calendarioHoraLocalCol){
//									//Se busca la fecha de la entrega
//									if(CollectionUtils.isNotEmpty(calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol())){
//										for(CalendarioHoraCamionLocalDTO horaCamionLocal : calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol()){
//											if(CollectionUtils.isNotEmpty(horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol())){
//												for(CalendarioDetalleHoraCamionLocalDTO detalleHoraCamionLocal : horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol()){
//													
//													//se comparan los ID
//													if(detalleHoraCamionLocal.getId().getCodigoCompania().equals(calendarioConsulta.getId().getCodigoCompania())
//															&& detalleHoraCamionLocal.getCodigoPedido().equals(calendarioConsulta.getCodigoPedido())
//															&& detalleHoraCamionLocal.getCodigoSectorEntrega().equals(calendarioConsulta.getCodigoSectorEntrega())
//															&& detalleHoraCamionLocal.getCodigoCiudadSectorEntrega().equals(calendarioConsulta.getCodigoCiudadSectorEntrega())
//															&& detalleHoraCamionLocal.getFechaCalendarioDia().toString().equals(calendarioConsulta.getFechaCalendarioDia().toString())
//															&& detalleHoraCamionLocal.getHora().toString().equals(calendarioConsulta.getHora().toString())
//															&& detalleHoraCamionLocal.getEstado().equals(calendarioConsulta.getEstado())){
//														//se agrega el detalle a la coleccion para restablecer los bultos de los camiones
//														calendarioDetalleHoraCamionLocalCol.add(detalleHoraCamionLocal);
//													}
//												}
//											}
//										}
//									}
//								}
//							}	
//						}else if(request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL) == null){
//							//se llena y sube la variable de sesion con los detalles de los camiones modificados
//							calendarioHoraLocalCol = new HashSet<CalendarioHoraLocalDTO>();
//							//se agregan los detalles encontrados a la variable de sesion
//							for(CalendarioDetalleHoraCamionLocalDTO detalleHoraCamion : calendarioDetalleHoraCamionLocalCol){
//								
//								CalendarioHoraLocalDTO consulta = detalleHoraCamion.getCalendarioHoraCamionLocalDTO().getCalendarioHoraLocalDTO();
//								consulta.setCalendarioHoraCamionLocalCol(new HashSet<CalendarioHoraCamionLocalDTO>());
//								CalendarioHoraCamionLocalDTO horaCamionLocal = new CalendarioHoraCamionLocalDTO();
//								horaCamionLocal.setTransporteDTO(detalleHoraCamion.getCalendarioHoraCamionLocalDTO().getTransporteDTO());
//								horaCamionLocal.setCalendarioDetalleHoraCamionLocalCol(calendarioDetalleHoraCamionLocalCol);
//								consulta.getCalendarioHoraCamionLocalCol().add(horaCamionLocal);
//								calendarioHoraLocalCol.add(consulta);
//							}
//							request.getSession().setAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL, calendarioHoraLocalCol);
//						}
						
//						//se unen con los detalles procesados anteriormente						
//						calendarioDetalleHoraCamionLocalCol = unirDetallesCamion(calendarioDetalleHoraCamionLocalCol, request);
						
//						if(CollectionUtils.isNotEmpty(calendarioDetalleHoraCamionLocalCol)){
//								
//							//se busca de que camion descontar los bultos							
//							//ordeno los camiones en forma ascendene de acuerdo a su capacidad
//							ColeccionesUtil.sort((List<CalendarioDetalleHoraCamionLocalDTO>)calendarioDetalleHoraCamionLocalCol, ColeccionesUtil.ORDEN_ASC, "capacidadUtilizada");														
//							
//							for(CalendarioDetalleHoraCamionLocalDTO detalleHoraCamion : calendarioDetalleHoraCamionLocalCol){
//								//se van restando los bultos
//								if(totalBultosRestablecer > 0){
//									
//									//el camion contiene todos los bultos
//									if(detalleHoraCamion.getCapacidadUtilizada() >= totalBultosRestablecer){
//										//se desactiva el registro para que ese camion este disponible para cualquier sector
//										if(totalBultosRestablecer == detalleHoraCamion.getCapacidadUtilizada().longValue()){											
//											detalleHoraCamion.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
//										}																			
//										detalleHoraCamion.setCapacidadUtilizada(detalleHoraCamion.getCapacidadUtilizada() - totalBultosRestablecer);
//										totalBultosRestablecer = 0L;
//									}
//									//el camion tiene parte de los bultos del pedido
//									else{
//										totalBultosRestablecer -= detalleHoraCamion.getCapacidadUtilizada();
//										detalleHoraCamion.setCapacidadUtilizada(0L);
//										
//										//se desactiva el registro para que ese camion este disponible para cualquier sector
//										detalleHoraCamion.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
//									}
//								}							
//							}							
//							detalleHoraCamionLocalPorActualizar.addAll(calendarioDetalleHoraCamionLocalCol);
//						}	
					}
					// se suben a sesion los detalles a actualizar 
					request.getSession().setAttribute(CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO, detalleHoraCamionLocalPorActualizar);
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al descontar los camiones asignados al pedido ",e);
		}
	}
	

	/**
	 * En la coleccion nueva con los registros del calendario de la semana, reemplazo por los datos modificados en sesion
	 * @param coleccionNueva
	 * @param request
	 * @return
	 */
	public static Collection<CalendarioHoraLocalDTO> unirCalendariosHoraLocal(Collection<CalendarioHoraLocalDTO> coleccionNueva,  HttpServletRequest request){
		
		LogSISPE.getLog().info("ingresa el metodo unirCalendariosHoraLocal ");
		Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
		
		if(CollectionUtils.isNotEmpty(coleccionNueva) && CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
			
			Collection<CalendarioHoraLocalDTO> coleccionEliminar = new HashSet<CalendarioHoraLocalDTO>();
			Collection<CalendarioHoraLocalDTO> coleccionReemplazar = new HashSet<CalendarioHoraLocalDTO>();
			//se recorren las colecciones 			
			for(CalendarioHoraLocalDTO horaLocalNuevo : coleccionNueva){
				
				for(CalendarioHoraLocalDTO horaLocalSesion : calendarioHoraLocalCol){
					
					//se verifica si el objeto de sesion es igual al actual			
					if(horaLocalNuevo.getId().getCodigoCompania().intValue() == horaLocalSesion.getId().getCodigoCompania()
							&& horaLocalNuevo.getId().getCodigoLocal().intValue() == horaLocalSesion.getId().getCodigoLocal().intValue()
							&& horaLocalNuevo.getId().getFechaCalendarioDia().equals(horaLocalSesion.getId().getFechaCalendarioDia())
							&& horaLocalNuevo.getId().getHora().equals(horaLocalSesion.getId().getHora())){
						coleccionEliminar.add(horaLocalNuevo);
						coleccionReemplazar.add(horaLocalSesion);
					}					
				}				
			}
			coleccionNueva.removeAll(coleccionEliminar);
			coleccionNueva.addAll(coleccionReemplazar);
		}
		return coleccionNueva;
	}
	
	
	/**
	 * Inicializa las variables que contienen la configuracion de los calendarios Hora Local
	 * @param request
	 * @param fecha
	 * @param hora
	 * @param codigoCompania
	 * @param codigoLocal
	 */
	public static void inicializarVariablesSession(HttpServletRequest request, EntregaDetallePedidoDTO entregaDetallePedidoDTO, Integer codigoLocal){
		
		//se obtiene la coleccion de la configuracion del calendarioHoraLocal
		Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
		if(CollectionUtils.isEmpty(calendarioHoraLocalCol)){
			calendarioHoraLocalCol = new HashSet<CalendarioHoraLocalDTO>();
		}
		LogSISPE.getLog().info("ingresa a inicializar las variables de sesion");
		
		java.sql.Date fechaEntrega = new java.sql.Date(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().getTime());
		Time horaEntrega = new Time(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente().getTime());

		CalendarioHoraLocalDTO  calendarioHorLocConsulta = new CalendarioHoraLocalDTO();
		calendarioHorLocConsulta.setCalendarioHoraCamionLocalCol(new HashSet<CalendarioHoraCamionLocalDTO>());
		
		CalendarioHoraCamionLocalDTO horaCamionLocal = new CalendarioHoraCamionLocalDTO();
		horaCamionLocal.setTransporteDTO(new TransporteDTO());
		horaCamionLocal.setCalendarioDetalleHoraCamionLocalCol(new HashSet<CalendarioDetalleHoraCamionLocalDTO>());
		horaCamionLocal.getCalendarioDetalleHoraCamionLocalCol().add(new CalendarioDetalleHoraCamionLocalDTO());
		
		calendarioHorLocConsulta.getCalendarioHoraCamionLocalCol().add(horaCamionLocal);
		calendarioHorLocConsulta.getId().setCodigoCompania(entregaDetallePedidoDTO.getId().getCodigoCompania());
		calendarioHorLocConsulta.getId().setCodigoLocal(codigoLocal);
		calendarioHorLocConsulta.getId().setFechaCalendarioDia(fechaEntrega);
		calendarioHorLocConsulta.getId().setHora(horaEntrega);
		
		//se busca si el registro ya esta en la coleccion
		Boolean campoEncontrado = Boolean.FALSE;
		if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
			
			for(CalendarioHoraLocalDTO horaLocal : calendarioHoraLocalCol){
				//se comparan los IDs
				if(horaLocal.getId().getCodigoCompania().equals(calendarioHorLocConsulta.getId().getCodigoCompania())
						&& horaLocal.getId().getCodigoLocal().equals(calendarioHorLocConsulta.getId().getCodigoLocal().intValue())
						&& horaLocal.getId().getFechaCalendarioDia().toString().equals(calendarioHorLocConsulta.getId().getFechaCalendarioDia().toString())
						&& horaLocal.getId().getHora().toString().equals(calendarioHorLocConsulta.getId().getHora().toString())){
					campoEncontrado = Boolean.TRUE;
				}
			}
		}
		if(!campoEncontrado){
			LogSISPE.getLog().info("Consultando el CalendarioHoraLocalDTO del dia "+fechaEntrega.toString()+" : "+horaEntrega.toString());
			Collection<CalendarioHoraLocalDTO>  calendarioHoraLocalDTOCol = SISPEFactory.getDataService().findObjects(calendarioHorLocConsulta);
			LogSISPE.getLog().info("detalles encontrados "+calendarioHoraLocalDTOCol.size());
			calendarioHoraLocalCol.addAll(calendarioHoraLocalDTOCol);
			
			//se sube a sesion
			request.getSession().setAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL, calendarioHoraLocalCol);
		}
	}

	
	/*
	 * METODOS MOVIDOS DESDE LA PAGINA entregaLocalCalendarioAction
	 * */
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @throws Exception
	 */
	public static void procesarEntregasParaVerificarTransito(HttpServletRequest request, CotizarReservarForm formulario)throws Exception{
		
		//se obtiene el par\u00E1metro de base para las ciudades de transito
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD", request);
		if(parametroDTO.getValorParametro() != null){
			String[] parametroDiv = parametroDTO.getValorParametro().split(",");
			LocalDTO localFiltro = new LocalDTO();
			localFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			
			int contEntrega = 0;
			int contDetalle = 0;
			String codigoCiudadCheck = "";
			Collection<String> valoresCheck = new ArrayList<String>();
			
			//se obtiene la colecci\u00F3n de los detalles
			Collection<DetallePedidoDTO> detalles = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			for(DetallePedidoDTO detallePedidoDTO : detalles){
				contEntrega = 0;
				if(detallePedidoDTO.getEntregaDetallePedidoCol() != null){
					for(Iterator<EntregaDetallePedidoDTO> it = detallePedidoDTO.getEntregaDetallePedidoCol().iterator(); it.hasNext(); ){
						EntregaDetallePedidoDTO entrega = it.next();
						LogSISPE.getLog().info("SEC: {}",entrega.getCodigoEntregaPedido());
						
						// Si la entrega es a un local
						if(entrega.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null){
							LogSISPE.getLog().info("Se verifican datos desde BD en base a entrega a local...");
							LogSISPE.getLog().info("C\u00F3digo local entrega del registro ya guardado que se va a mostrar: {}",entrega.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
							
							//Se obtiene el c\u00F3digo de la ciudad del local desde BD cuando la entrega es a un local
							localFiltro.getId().setCodigoLocal(entrega.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
							LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalesList(localFiltro).iterator().next();
							LogSISPE.getLog().info("Ciudad destino para entregas: {}",localDTO.getCodigoCiudad());
							codigoCiudadCheck = localDTO.getCodigoCiudad();
						}else{
							// Si la entrega es a domicilio
							LogSISPE.getLog().info("Se verifican datos desde BD en base a entrega a domicilio...");
							
							//Verifico si existe un c\u00F3digo de ciudad en la entrega a domicilio
							if(entrega.getEntregaPedidoDTO().getCodigoDivGeoPol()!=null){
								codigoCiudadCheck = entrega.getEntregaPedidoDTO().getCodigoDivGeoPol();
							}else{
								//Se obtiene el c\u00F3digo de la ciudad del local configurado en la entrega
								localFiltro.getId().setCodigoLocal(entrega.getEntregaPedidoDTO().getCodigoLocalSector());
								LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalesList(localFiltro).iterator().next();	
								LogSISPE.getLog().info("Ciudad destino para entregas: {}",localDTO.getCodigoCiudad());
								codigoCiudadCheck = localDTO.getCodigoCiudad();
							}
						}
						
						//Se comparan c\u00F3digos de ciudad con VALORPARAMETRO 
						for(int j=0; j<parametroDiv.length; j++){
							String[] codigosC = parametroDiv[j].split("-");
							
							if(codigosC[0].equals(codigoCiudadCheck)){
								LogSISPE.getLog().info("Los c\u00F3digos son iguales");
								LogSISPE.getLog().info("Cant. Despacho: {}",entrega.getCantidadDespacho());
								
								//IOnofre. Se verifica si la entrega es a un local para no ponerle el check de transito.
								if(entrega.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()==null){
									entrega.getEntregaPedidoDTO().setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
									//si es articulo perecible
									if(!EntregaLocalCalendarioUtil.verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())) {
										//Obtener el parametro para saber el valor del minimo de canastas que se pueden agregar en las entregas a domicilio
										ParametroDTO parametroMinCanastas = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadArticuloValidaResponsablePedido", request);
										Integer codMinCanastosDomicilio = Integer.parseInt(parametroMinCanastas.getValorParametro());
										String codCanastosCatalogo = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request)).getValorParametro();
										String codCanastosEspeciales = (WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request)).getValorParametro();
										//recorre las entregas que tenga configurado hasta ese momento
										HashMap<String,Long> articuloEncontrado= new HashMap<String, Long>();
										for(EntregaDetallePedidoDTO entregaAux : (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol()){
											//si est\u00E1 habilitado el check de transito
											if(entregaAux.getEntregaPedidoDTO().getNpValidarCheckTransito() != null 
												&& entregaAux.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))
													&& entregaAux.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(CONTEXTO_ENTREGA_DOMICIO)
													){
												
												if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosCatalogo) 
														|| detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanastosEspeciales)){
													entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
													
												}else{
														if(articuloEncontrado.size()==0){
															articuloEncontrado.put(entregaAux.getCodigoArticulo(), entregaAux.getCantidadEntrega().longValue());
															if(entregaAux.getCantidadEntrega().longValue() > 0 && entregaAux.getCantidadEntrega().longValue() >= codMinCanastosDomicilio.longValue()){
																if(entregaAux.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito()==null || entregaAux.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"))){
																	entregaAux.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
																}
															}
														}else{
															Long cantidad=articuloEncontrado.get(entregaAux.getCodigoArticulo());
															entregaAux.setNpPasoObligatorioBodegaTransito(null);
															
															if(cantidad==null){
																articuloEncontrado.put(entregaAux.getCodigoArticulo(), entregaAux.getCantidadEntrega().longValue());
															}else{ 
																long cantidadaActual=entregaAux.getCantidadEntrega().longValue();
																long cantidadParcialTotal=cantidad.longValue()+cantidadaActual;
																articuloEncontrado.remove(entregaAux.getCodigoArticulo());
																articuloEncontrado.put(entregaAux.getCodigoArticulo(),cantidadParcialTotal);
																if(cantidadParcialTotal > 0 && cantidadParcialTotal >= codMinCanastosDomicilio.longValue()){
																	Collection<EntregaDetallePedidoDTO> entregasEncontradas=	ColeccionesUtil.simpleSearch("codigoArticulo", entregaAux.getCodigoArticulo(), (Collection<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol());
																	
																	//indicar que es obligatorio el paso por la bodega de transito de guayaquil
																	for(EntregaDetallePedidoDTO entDetPedDTO : entregasEncontradas){
																		if(entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito()==null || entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"))){
																			entDetPedDTO.setNpPasoObligatorioBodegaTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
																		}
																	}
																}
																else{
																	entregaAux.setNpPasoObligatorioBodegaTransito(null);
																}
															}
														}
												}
											}
										}
									}							
								}
								
								else{
									entrega.getEntregaPedidoDTO().setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
								}
								
								entrega.getEntregaPedidoDTO().setNpValorCodigoTransito(codigosC[1]);
								if(entrega.getEntregaPedidoDTO().getCodigoBodega()!=null){
									LogSISPE.getLog().info("ValorParametro: {}",codigosC[1]);
									LogSISPE.getLog().info("RegistroDetalle: {}",contDetalle);
									LogSISPE.getLog().info("RegistroEntrega: {}",contEntrega);
									
									String cadena = codigosC[1]+"-"+contDetalle+"-"+contEntrega;
									valoresCheck.add(cadena);
								}
								break;
							}
						}
						//Se va aumentando el valor de la variable a medida que se recorren los registros
						contEntrega++;
					}
				}
				contDetalle ++;
			}
			
			//Se setean los valores para el arreglo de checkboxes para mostrar cada registro seleccionado o no, seg\u00FAn el caso
			formulario.setCheckTransitoArray(valoresCheck.toArray(new String [0]));
			request.getSession().setAttribute(CHECKS_ENTREGAS_TRANSITO, formulario.getCheckTransitoArray());
		}
	}
	
	/**
	 * 
	 * @param detallePedidoDTOCol
	 * @param request
	 * @throws Exception
	 */
	public static void procesarResponsablesEntrega(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request) throws Exception{
		
		LocalDTO localFiltro = new LocalDTO();
		
		localFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
		String codClaRecetasNuevas = parametroDTO.getValorParametro();
		
		//se obtiene los parametros desde el properties
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
		String codClaCanastos = parametroDTO.getValorParametro();
		
		//se obtiene los parametros desde el properties para obtener las clasificaciones de los perecibles
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.clasificaciones.asignacion.responsables", request);
		String clasificacionCodPerecibles = parametroDTO.getValorParametro();		
		
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesActivoTransitoCD", request);
		String[] parametroDiv = parametroDTO.getValorParametro().split(",");
		String codigoCiudadCheck = "";
		Boolean flagCiudadTransito = Boolean.FALSE;
		
		request.getSession().removeAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
		
		//Presentacion en el popUpResponsable
		Collection<EstructuraResponsable> colResponsables;
		EstructuraResponsable estructuraRes = null;
				
		if(detallePedidoDTOCol != null && !detallePedidoDTOCol.isEmpty()){
			//obtiene las bodegas			
			BodegaDTO bodDTO= new BodegaDTO();
			bodDTO.setEstadoBodega(ConstantesGenerales.ESTADO_ACTIVO);
			AreaTrabajoDTO areTraDTO= new AreaTrabajoDTO();
			AreaSublugarTrabajoDTO areSubLugTraDTO= new AreaSublugarTrabajoDTO();
			areSubLugTraDTO.setAreaTrabajoDTO(new AreaTrabajoDTO());
			areSubLugTraDTO.getAreaTrabajoDTO().setTipoAreaTrabajoRestriction(null);

			Collection<AreaSublugarTrabajoDTO> areSubLugTraCol= new ArrayList<AreaSublugarTrabajoDTO>();
			areSubLugTraCol.add(areSubLugTraDTO);
			areTraDTO.setAreaLugarTrabajoCol(areSubLugTraCol);
			bodDTO.setAreaTrabajo(areTraDTO);
			bodDTO.addJoin("areaTrabajo", JoinType.INNER);
			
			Collection<BodegaDTO> bodegas = SISPEFactory.getDataService().findObjects(bodDTO);			
			colResponsables = new ArrayList<EstructuraResponsable>();
			
			//obtener area de trabajo para produccion de canastos
			AreaTrabajoDTO areaTrabajoDTOPro = obtenerAreaTrabajoCanastos();
			
			//Ingreso de nuevos detalles
			for(DetallePedidoDTO detallePedidoDTO: detallePedidoDTOCol){ 
				if(!CollectionUtils.isEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
					String descripcionBodega = "";
					Integer codigoBodega = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo();
					//obtiene la bodega responsable del pedido
					for(BodegaDTO bodega: bodegas ){
						if(bodega.getId().getCodigoBodega().equals(detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoBodega())){
							if(bodega.getAreaTrabajo().getTipoAreaTrabajoValor().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.subBodega"))){
								descripcionBodega = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + bodega.getAreaTrabajo().getNombreAreaTrabajo();
								//codigoBodega = detallePedidoDTO.getArticuloDTO().getClasificacionDTO().getCodigoBodega();
								codigoBodega =  bodega.getAreaTrabajo().getId().getCodigoAreaTrabajo();
								break;
							}else{
								if(CollectionUtils.isNotEmpty(bodega.getAreaTrabajo().getAreaLugarTrabajoCol())){
									for(AreaSublugarTrabajoDTO aSlTDTO : bodega.getAreaTrabajo().getAreaLugarTrabajoCol()){
										if(aSlTDTO.getId().getCodigoAreaTrabajo().equals(bodega.getAreaTrabajo().getId().getCodigoAreaTrabajo()) &&
												aSlTDTO.getTipoRelacionValor().equals("JER")){
											descripcionBodega = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + aSlTDTO.getAreaTrabajoDTO().getNombreAreaTrabajo();
											//codigoBodega = relacionBodegaDTO.getBodegaDTO().getId().getCodigoBodega();
											codigoBodega =  aSlTDTO.getAreaTrabajoDTO().getId().getCodigoAreaTrabajo();
											break;
										}
									}
								}
							}
						}
					}
					if(descripcionBodega.equals("")){
						descripcionBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.responsableEntrega.sin.bodega");
						ActionMessages warnings = new ActionMessages();
						warnings.add("articuloSinBodega",new ActionMessage("warnings.articulo.sin.bodega"));
					}
					
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO: detallePedidoDTO.getEntregaDetallePedidoCol()){
						estructuraRes = new EstructuraResponsable();
						estructuraRes.setDetallePedidoDTO(detallePedidoDTO);
						flagCiudadTransito = Boolean.FALSE;
						// verifico Ciudad destino para entregas
						if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega()!=null){
							//Se obtiene el c\u00F3digo de la ciudad del local desde BD cuando la entrega es a un local
							localFiltro.getId().setCodigoLocal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
							LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalesList(localFiltro).iterator().next();
							LogSISPE.getLog().info("Ciudad destino para entregas: {}",localDTO.getCodigoCiudad());
							codigoCiudadCheck = localDTO.getCodigoCiudad();
						}else{
							//Verifico si existe un c\u00F3digo de ciudad en la entrega a domicilio
							if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol()!=null){
								codigoCiudadCheck = entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoDivGeoPol();
							}else{
								//Se obtiene el c\u00F3digo de la ciudad del local configurado en la entrega											
								localFiltro.getId().setCodigoLocal(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoLocalSector());
								LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalesList(localFiltro).iterator().next();
								LogSISPE.getLog().info("Ciudad destino para entregas: {}",localDTO.getCodigoCiudad());
								codigoCiudadCheck = localDTO.getCodigoCiudad();
							}
						}
						//Se comparan c\u00F3digos de ciudad con VALORPARAMETRO 
						for(int j=0; j<parametroDiv.length; j++){
							String[] codigosC = parametroDiv[j].split("-");
							if(codigosC[0].equals(codigoCiudadCheck)){
								flagCiudadTransito = Boolean.TRUE;
								break;
							}
						}
						//Verifico el tipo de entrega
						switch(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"))) ? 1 : 
							entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal"))) ? 2 :
								entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))) ? 3 : 
									//caso nuevo para entrega domicilio desde local
										entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().equals(Integer.valueOf(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)) ? 4 : 5){
						case 1 :
							LogSISPE.getLog().info("-- Lugar de entrega Mi Local --");
							//--------Entrega a mi local------//
							asignarResponsablesLocal(entregaDetallePedidoDTO, request, detallePedidoDTO, estructuraRes, codClaRecetasNuevas, descripcionBodega, codClaCanastos, clasificacionCodPerecibles, codigoBodega,areaTrabajoDTOPro);
							 break;
							 
						case 2:
							LogSISPE.getLog().info("-- Lugar de entrega Otro Local --");
							//--------Entrega a otro local------//
							if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) && 
									flagCiudadTransito.equals(Boolean.TRUE)) {
								
								if(StringUtils.isNotEmpty(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito())
											&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
									
									LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" con paso obligatorio por transito y canastos especiales");
									estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_TRANSITO);
									estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
									estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
									estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_TRANSITO);
									entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaTransito")));
									entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
									entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
									entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaTransito")));
									
								}else{
									
									LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" sin pasar por transito y canastos especiales");
									estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
									estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
									estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
									estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
									entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
									entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());
									entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());
									entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
								}
								
							}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp() != null &&
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local")) && flagCiudadTransito.equals(Boolean.FALSE)) {
								
								LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" con canastos especiales y responsable el local");
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
							}
							else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp() != null &&
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega")) && flagCiudadTransito.equals(Boolean.FALSE)) {
								
								LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" con canastos especiales y responsable la bodega");
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
							}
							else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))) {
								
								LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" con canastos de catalogo");
								estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
								entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
								
							}else if( clasificacionCodPerecibles.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))) {
								
								LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck+" con articulos perecibles");
								estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaPerecibles")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaPerecibles")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
								
							}else if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
								
								LogSISPE.getLog().info("entrega a otro local: "+codigoCiudadCheck);
								estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaAbastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
								
							}
							break;
						case 3:
							LogSISPE.getLog().info("-- Lugar de entrega Domicilio --");
							//--------Entrega a domicilio------//
							if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))){
								Integer codigo= SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo();						
								AreaTrabajoDTO areaTrabajoDTO = new AreaTrabajoDTO();
								areaTrabajoDTO.getId().setCodigoAreaTrabajo(codigo);
								areaTrabajoDTO.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());									
								ArrayList<AreaTrabajoDTO> areaTrabajoDTOCol = (ArrayList<AreaTrabajoDTO>)SISPEFactory.getDataService().findObjects(areaTrabajoDTO);	
								
								estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.responsableEntrega.local") + " " +  areaTrabajoDTOCol.get(0).getId().getCodigoAreaTrabajo() + " - " + areaTrabajoDTOCol.get(0).getNombreAreaTrabajo());//RESPONSABLE_LOCAL
								estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.responsableEntrega.local") + " " +  areaTrabajoDTOCol.get(0).getId().getCodigoAreaTrabajo() + " - " + areaTrabajoDTOCol.get(0).getNombreAreaTrabajo());//RESPONSABLE_LOCAL
								estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigo);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.Local")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigo);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.Local")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
								
							}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.TRUE)){
								
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_TRANSITO);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_TRANSITO);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaTransito")));
								entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaTransito")));
								
							}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.FALSE)){
								
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
								estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_CANASTOS);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaCanastos")));
								
							}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.FALSE)){
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_CANASTOS);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaAbastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));								
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaCanastos")));
								
							}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.TRUE)){
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_TRANSITO);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_TRANSITO
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_TRANSITO
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_TRANSITO);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaTransito")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaTransito")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaTransito")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaTransito")));
								
							}else if(clasificacionCodPerecibles.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.FALSE)){
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_CANASTOS);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaPerecibles")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaPerecibles")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaCanastos")));
								
							}else if( clasificacionCodPerecibles.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) &&
									(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
											entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))  && 
											flagCiudadTransito.equals(Boolean.TRUE)){
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_ABASTOS);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_CANASTOS);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaAbastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaPerecibles")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaPerecibles")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaCanastos")));
								
							}
							else if((entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
									entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))){
								estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_ABASTOS);
								estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
								estructuraRes.setResponsableEntrega(RESPONSABLE_BODEGA_ABASTOS);
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaAbastos")));
								entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaAbastos")));
								entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));
								entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.BodegaAbastos")));
							}
							break;
						case 4 :
							LogSISPE.getLog().info("-- Lugar de entrega a Domicilio desde Local --");
							//--------Entrega a domicilio desde local------//
							asignarResponsablesLocal(entregaDetallePedidoDTO, request, detallePedidoDTO, estructuraRes, codClaRecetasNuevas, descripcionBodega, codClaCanastos, clasificacionCodPerecibles, codigoBodega,areaTrabajoDTOPro);
							break;
						}
						estructuraRes.setEntregaPedidoDTO(entregaDetallePedidoDTO.getEntregaPedidoDTO());
						estructuraRes.setEntregaDetallePedidoDTO(entregaDetallePedidoDTO);
						colResponsables.add(estructuraRes);
					}
				}
			}
			
			if(CollectionUtils.isEmpty(colResponsables)){
				request.getSession().removeAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
			}else {
				request.getSession().setAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS, colResponsables);
			}
		}
	}


	/**
	 * @return
	 */
	public static AreaTrabajoDTO obtenerAreaTrabajoCanastos() {
		AreaTrabajoDTO areaTrabajoDTOPro=null;
		try{
			//Obtenemos del par\u00E1metro la bodega de abastos asignada
			ParametroDTO parametro = new ParametroDTO();
			parametro.getId().setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.areaTrabajoCanastos"));
			parametro.getId().setCodigoCompania(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCompania")));
			ParametroDTO parametroCanastos = SISPEFactory.getDataService().findUnique(parametro);
			
			//AREA DE TRABAJO DEL PARAMETRO
			AreaTrabajoDTO areaTrabajoProDTO = new AreaTrabajoDTO();
			areaTrabajoProDTO.setCodigoReferencia(Integer.valueOf(parametroCanastos.getValorParametro()));
			areaTrabajoProDTO.getId().setCodigoCompania(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoCompania")));
			areaTrabajoProDTO.setTipoAreaTrabajoValor(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.corporativo.tipoAreaTrabajoBodega"));
			areaTrabajoDTOPro = SISPEFactory.getDataService().findUnique(areaTrabajoProDTO);
			
		}catch(Exception ex){
			LogSISPE.getLog().info("Error al buscar la bodega de canastos",ex);
		}
		return areaTrabajoDTOPro;
	}
	
	/**
	 * 
	 * @param session
	 */
	@SuppressWarnings("rawtypes")
	public static void entidadResponsablePedido(HttpSession session){
		
		Collection responsablesPedidoCol = (Collection)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
		if(responsablesPedidoCol !=null && !responsablesPedidoCol.isEmpty()){
			for (Iterator<EstructuraResponsable> it = responsablesPedidoCol.iterator();it.hasNext();) {
				EstructuraResponsable estructuraResponsable = it.next();
				if(!estructuraResponsable.getResponsablePedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.responsable.pedido.local"))){
					session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
					break;
				}
				session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
			}
		}
	}
	
	/**
	 * M&eacute;todo para verificar si en articulo pertenece a la clasificaci&oacute;n de perecibles
	 * @param request
	 * @param codigoClasificacionArticulo
	 * @return
	 * @throws MissingResourceException
	 * @throws Exception
	 */
	public static Boolean verificarArticuloPerecible(HttpServletRequest request, String codigoClasificacionArticulo) throws MissingResourceException, Exception{
		//Colecci\u00F3n con los c\u00F3digos de las clasificaciones que son consideradas como perecibles
		Collection<String> clasificacionesPerecibles = (Collection<String>)request.getSession().getAttribute(EntregaLocalCalendarioAction.CLASIFICACIONES_PERECIBLES);
		
		//verifica si se consult\u00F3 las clasificaciones, caso contrario las busca.
		if(clasificacionesPerecibles == null ){
			//obtiene el c\u00F3digo del especial para perecibles
			ParametroDTO parametro = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialPerecibles", request);
			//Plantilla de b\u00FAsqueda
			EspecialClasificacionDTO especialClasificacionDTO = new EspecialClasificacionDTO();
			especialClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			especialClasificacionDTO.getId().setCodigoEspecial(parametro.getValorParametro());
			especialClasificacionDTO.setEstadoEspecialClasificacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
			//obtiene las clasificaciones
			clasificacionesPerecibles = SISPEFactory.obtenerServicioSispe().transObtenerCodigosEspecialClasificacion(especialClasificacionDTO);
			request.getSession().setAttribute(EntregaLocalCalendarioAction.CLASIFICACIONES_PERECIBLES, clasificacionesPerecibles);
		}
		//encontr\u00F3 clasificaciones
		if(clasificacionesPerecibles != null && !clasificacionesPerecibles.isEmpty()){
			//recorre los c\u00F3digos de las clasificaciones de perecibles
			for(String codClaPerecible : clasificacionesPerecibles){
				//si la clasificaci\u00F3n del art\u00EDculo est\u00E1 en dentro de las clasificaciones de perecibles
				if(codClaPerecible.equals(codigoClasificacionArticulo)){
					return Boolean.TRUE;
				}
			}
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * TODO FALTA IMPLEMENTAR FUNCIONALIDAD
	 * @param request
	 * @param entregaPedidoDTO
	 * @param subTotal
	 */
	public static void cargaDireccionesYCostos(HttpServletRequest request, EntregaDetallePedidoDTO entregaDetallePedidoDTO, Double subTotal){
		LogSISPE.getLog().info("la entrega es a domicilio y va a verificar direcciones y costos");
		HttpSession session=request.getSession();
		//coleccion de costos de entrega de direcciones
		Collection<CostoEntregasDTO> costoEntregasDTOCol = session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA)!=null ? (Collection<CostoEntregasDTO>)session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA) : new ArrayList<CostoEntregasDTO>();
		//coleccion de direcciones de entrega
		Collection<DireccionesDTO> direccionesDTOCol = session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONES)!=null ? (Collection<DireccionesDTO>)session.getAttribute(EntregaLocalCalendarioAction.DIRECCIONES) : new ArrayList<DireccionesDTO>();
		boolean condicionCosto = false;//condicion para ingreso de nuevos costos
		boolean entregaConDomicilioExistente = false;//condicion para ingreso de nuevas direcciones
		
		Boolean esEntregaDomStockLocal = esEntregaDomStockLocal(entregaDetallePedidoDTO);

		//Contador de direcciones
		int secDirecciones=1;
		if(session.getAttribute(EntregaLocalCalendarioAction.SECDIRECCIONES)!=null){
			secDirecciones=((Integer)session.getAttribute(EntregaLocalCalendarioAction.SECDIRECCIONES)).intValue();
		}
		
//		//session del costo total de la entrega a domicilio
//		double costoEntregaParcial = 0;
//		if(session.getAttribute(VALORTOTALENTREGA)!=null){
//			costoEntregaParcial = ((Double)session.getAttribute(VALORTOTALENTREGA)).doubleValue();
//		}
		
		//Saco la direccion de la entrega
		int posicion = entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().indexOf("-");
		String direccion=entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().substring(posicion+1);
		
		try{
			
			/*******************Comprueba si la direccion ya ha sido ingresada al menos una vez*********/
			if(direccionesDTOCol!=null && direccionesDTOCol.size()>0){
				for (DireccionesDTO direccionesDTO: direccionesDTOCol) {
					LogSISPE.getLog().info("npDireccionEntregaDomicilio: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
					LogSISPE.getLog().info("codigoDireccion: {}" , direccionesDTO.getCodigoDireccion());
					Long difFecha=ManejoFechas.restarFechas(direccionesDTO.getFechaEntrega(), entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					if(direccion.equals(direccionesDTO.getDescripcion()) && difFecha==0L){
						LogSISPE.getLog().info("no hay nueva direccion");
						entregaConDomicilioExistente = true;
						//Si existe ya la direccion incrementa su contador
						direccionesDTO.setNumeroDireccion(direccionesDTO.getNumeroDireccion()+1);
						entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpDireccionEntregaDomicilio(direccionesDTO.getCodigoDireccion());
					}
				}
			}
			/*******************Comprueba si el costo ya ha sido ingresado al menos una vez*********/
			if(costoEntregasDTOCol!=null && costoEntregasDTOCol.size()>0){
				for(CostoEntregasDTO costoEntregasDTO:costoEntregasDTOCol){
					LogSISPE.getLog().info("costo.sector: {}" , costoEntregasDTO.getCodigoSector());
					LogSISPE.getLog().info("costo.fechaEntrega: {}" , costoEntregasDTO.getFechaEntrega());
					LogSISPE.getLog().info("entrega.fechaEntrega: {}" , entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
					LogSISPE.getLog().info("entrega.direccion: {}",entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
					Double costoEntregaTemp = costoEntregasDTO.getNpValorParcial()==null?0:costoEntregasDTO.getNpValorParcial().doubleValue();
					if(entregaConDomicilioExistente && entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega().equals(costoEntregasDTO.getCodigoSector()) && costoEntregasDTO.getFechaEntrega().equals(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente())){
						condicionCosto = true;
						LogSISPE.getLog().info("no hay nuevo costo");
						//Si existe ya el costo incremente su contador
						costoEntregaTemp = costoEntregaTemp + (entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoParcialEntrega()==null?0:entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoParcialEntrega().doubleValue());
						costoEntregasDTO.setNpValorParcial(costoEntregaTemp);
						
						Double valorEntregaTotal = Double.valueOf(
								WebSISPEUtil.costoEntregaDistancia(costoEntregasDTO.getDistancia(), request, 
										costoEntregaTemp, entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))));
						
						costoEntregasDTO.setValor(valorEntregaTotal);
						costoEntregasDTO.setNumeroEntregas(costoEntregasDTO.getNumeroEntregas()+1);
						LogSISPE.getLog().info("costoEntrega: {}" , costoEntregasDTO.getNumeroEntregas());
						
						//agregamos la entrega relacionada al costo
						costoEntregasDTO.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
					}
				}
			}
			
			//cuando hay una nueva direcci\u00F3n
			if(!entregaConDomicilioExistente){
				LogSISPE.getLog().info("Es una nueva direcci\u00F3n");
				DireccionesDTO direccionesDTO1 = new DireccionesDTO();
				direccionesDTO1.setCodigoDireccion(Integer.valueOf(secDirecciones).toString());
				LogSISPE.getLog().info("codigoDir: {}" , direccionesDTO1.getCodigoDireccion());
				direccionesDTO1.setDescripcion(direccion);
				direccionesDTO1.setFechaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
				direccionesDTO1.setCodigoSector(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoLocalSector().toString());
				if (entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega()!=null) {
					direccionesDTO1.setDistanciaDireccion(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega().toString());
				} else {
					direccionesDTO1.setDistanciaDireccion(Double.valueOf(WebSISPEUtil.distanciaEntregaCosto(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoEntrega(),request, esEntregaDomStockLocal)).toString());
				}
				direccionesDTO1.setNumeroDireccion(1);
				//aggregar el valor de la entrega
				direccionesDTO1.setValorFlete(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpValorParcialEntrega());
				direccionesDTOCol.add(direccionesDTO1);
				session.setAttribute(EntregaLocalCalendarioAction.DIRECCIONES, direccionesDTOCol);
				session.setAttribute(EntregaLocalCalendarioAction.SECDIRECCIONES, Integer.valueOf(secDirecciones+1));
				entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpDireccionEntregaDomicilio(direccionesDTO1.getCodigoDireccion());
			}
			
			//cuando hay un nuevo costo
			if(!condicionCosto){
				LogSISPE.getLog().info("es un nuevo costo");
				CostoEntregasDTO costoEntregasDTO1 = new CostoEntregasDTO();
				costoEntregasDTO1.setFechaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
				costoEntregasDTO1.setCodigoSector(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
				if (entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega()!=null) {
					costoEntregasDTO1.setDistancia(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega());
				} else {
					costoEntregasDTO1.setDistancia(WebSISPEUtil.distanciaEntregaCosto(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoEntrega(),request, esEntregaDomStockLocal));
				}
				
				Double valorEntregaTotal = Double.valueOf(WebSISPEUtil.costoEntregaDistancia(costoEntregasDTO1.getDistancia(), request, 
						entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoParcialEntrega(), entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local"))));

				//----------------------------------
				ParametroDTO paramMontoMinEntregaDomicioCD = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
				Double montoMinEntregaDomicioCD = Double.valueOf(paramMontoMinEntregaDomicioCD.getValorParametro());
				
				if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock() != null
						&& entregaDetallePedidoDTO.getEntregaPedidoDTO() != null
						&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue() 
						== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega").intValue()
						&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
						
					if(subTotal > 0 && subTotal > montoMinEntregaDomicioCD){
						valorEntregaTotal = 0D;
					}
				}
				//----------------------------------
				
				costoEntregasDTO1.setValor(valorEntregaTotal);
				costoEntregasDTO1.setNumeroEntregas(1);
				costoEntregasDTO1.setNpValorParcial(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoParcialEntrega());
				costoEntregasDTO1.setDescripcion(direccion);
				
				//agregamos la entrega relacionada al costo
				costoEntregasDTO1.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
				costoEntregasDTO1.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
				costoEntregasDTOCol.add(costoEntregasDTO1);
//				costoEntregaParcial = costoEntregaParcial + costoEntregasDTO1.getValor().doubleValue();
//				LogSISPE.getLog().info("costoEntregaParcial: {}",costoEntregaParcial);
				session.setAttribute(EntregaLocalCalendarioAction.COSTOENTREGA, costoEntregasDTOCol);
//				session.setAttribute(VALORTOTALENTREGA, Double.valueOf(costoEntregaParcial));
				
			}
			
//			CotizarReservarForm.asignarCostoEntregaDireccion(request, subTotal, Boolean.FALSE);
			
//			LogSISPE.getLog().info("costo del flete: {}" , session.getAttribute(VALORTOTALENTREGA));
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}
	
	/**
	 * Si le entrega es a domicilio y el stock se va ha tomar del local
	 * @param entregaDTO
	 * @return
	 */
	private static Boolean esEntregaDomStockLocal(EntregaDetallePedidoDTO entregaDetalePedidoDTO){
		
		if(entregaDetalePedidoDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion")) 
				&& entregaDetalePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock()==(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.local"))){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * M&eacute;todo para verificar si en el pedido existen canastas de catalogo, clasificacion 1602
	 * @param request
	 * @return
	 */
	public static Boolean verificarCanastasCatalogo(HttpServletRequest request){
		LogSISPE.getLog().info("--Entro en la funcion verificarCanastasCatalogo");
		Boolean existeCanastaCatalogo=Boolean.FALSE;
		try {
			HttpSession session = request.getSession();
			//se obtiene los parametros desde el properties
			ParametroDTO parametroDTO;
			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
			String codClaCanastos = parametroDTO.getValorParametro();
			LogSISPE.getLog().info("Id clasificacion de canastos de catalago: {}",codClaCanastos);
			Collection<DetallePedidoDTO> detallePedidoCol = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			LogSISPE.getLog().info("Detalles del pedido: {}",detallePedidoCol.size());
			if(detallePedidoCol != null && !detallePedidoCol.isEmpty()){
				//Validacion de DespachosLocal solo canastasCatalago
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
					//verifico que solo sea un canasto de catalago y tipoEntregas.
					if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)){
						LogSISPE.getLog().info("El pedido contiene canastos de catalago");
						existeCanastaCatalogo= Boolean.TRUE;
						break;
					}
				}
			}
		} catch (Exception e) {
			LogSISPE.getLog().info("Error verificarCanastasCatalogo {}",e.getMessage());
		}
		return existeCanastaCatalogo;
	}
	
	/**
	 * 
	 * @param session
	 * @param detPed
	 * @param entrega
	 */
	public static void obtenerResponsableEntrega(HttpSession session, DetallePedidoDTO detPed, EntregaDetallePedidoDTO entrega) {
		
		Collection<EstructuraResponsable> detalleEstructuraCol = (ArrayList<EstructuraResponsable>)session.getAttribute(CotizarReservarAction.COL_RESPONSABLES_ENTREGAS);
		
		if(!CollectionUtils.isEmpty(detalleEstructuraCol)){
			
			for(EstructuraResponsable resEnt:detalleEstructuraCol){
				
				ArticuloDTO artPed=resEnt.getDetallePedidoDTO().getArticuloDTO();
				EntregaDetallePedidoDTO entDetPed=resEnt.getEntregaDetallePedidoDTO();
				
				if(artPed!=null && entDetPed!= null){
					Long diferencia= entDetPed.getEntregaPedidoDTO().getFechaEntregaCliente().getTime()-entrega.getEntregaPedidoDTO().getFechaEntregaCliente().getTime();
					if(diferencia==0L && entDetPed.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim().equals(entrega.getEntregaPedidoDTO().getDireccionEntrega().toUpperCase().trim())){
						if(entDetPed.getCodAreTraResDes().intValue()==entrega.getCodAreTraResDes().intValue() 
								&& entDetPed.getEntregaPedidoDTO().getCodConResEnt().intValue()==entrega.getEntregaPedidoDTO().getCodConResEnt().intValue() 
								&& entDetPed.getCodAreTraResPro().intValue()==entrega.getCodAreTraResPro().intValue()){
								//&& entDetPed.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()==entrega.getEntregaPedidoDTO().getCodigoObtenerStock().intValue()){
							if(detPed.getArticuloDTO().getId().getCodigoArticulo().equals(artPed.getId().getCodigoArticulo()) && detPed.getNpReponsable()==null){
								detPed.setNpReponsable(resEnt);
								break;
							}
						}
					}
				}
			}
			
		}
	}
	
	public static void obtenerEntregasPedido(HttpSession session,Collection<EntregaDetallePedidoDTO> entDetPedCol){
		//se crea la cabecera
		//se verfica si existen entregas configuradas
		Collection<EntregaPedidoDTO> entregasPedido = (Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)==null ? new ArrayList<EntregaPedidoDTO>():(Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO);
//		detallePedidoDTO.setNpContadorEntrega(new Long(detallePedidoDTO.getNpContadorEntrega().longValue()+entregaDetallePedidoDTO.getNpCantidadEntrega().longValue()));
		for(EntregaDetallePedidoDTO entDetPedDTO:entDetPedCol){
			entDetPedDTO.getEntregaPedidoDTO().setEntregaDetallePedidoCol(new ListSet());
			entDetPedDTO.getEntregaPedidoDTO().getEntregaDetallePedidoCol().add(entDetPedDTO);
			
			//verificar si la entregaPedido ya esta configurada
			Collection<EntregaPedidoDTO> entPedCol=buscarEntregasConfiguradas(entregasPedido,entDetPedDTO.getEntregaPedidoDTO());
			
			//Solo si no encuentra la entrega tiene que agregar
			if(CollectionUtils.isEmpty(entPedCol)){
				entregasPedido.add(entDetPedDTO.getEntregaPedidoDTO());
				session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO, entregasPedido);
			}else if(entPedCol.size()==1){
				entPedCol.iterator().next().getEntregaDetallePedidoCol().add(entDetPedDTO);
			}
		}
	}
	
	public static Collection<EntregaPedidoDTO> buscarEntregasConfiguradas(
			Collection<EntregaPedidoDTO> entregasPedido,
			final EntregaPedidoDTO entregaPedidoDTO) {

		Collection<EntregaPedidoDTO> entPedRes= CollectionUtils.select(entregasPedido, new Predicate() {
			
			public boolean evaluate(Object arg0) {
				EntregaPedidoDTO entPedDTO=(EntregaPedidoDTO)arg0;
				
				return //entPedDTO.getCodigoAreaTrabajoEstadoPedido().intValue()==entregaPedidoDTO.getCodigoAreaTrabajoEstadoPedido().intValue() &&
						//entPedDTO.getCodigoPedido().equals(entregaPedidoDTO.getCodigoPedido()) &&
						//entPedDTO.getCodigoEstado().equals(entregaPedidoDTO.getCodigoEstado()) &&
						//entPedDTO.getSecuencialEstadoPedido().equals(entregaPedidoDTO.getSecuencialEstadoPedido()) &&
						//entPedDTO.getCodigoAreaTrabajoEntrega().intValue()==entregaPedidoDTO.getCodigoAreaTrabajoEntrega().intValue() &&
						//entPedDTO.getCodigoBodega().equals(entregaPedidoDTO.getCodigoBodega()) &&
						//entPedDTO.getCodigoDivGeoPol().equals(entregaPedidoDTO.getCodigoDivGeoPol()) &&
						entPedDTO.getDireccionEntrega().equals(entregaPedidoDTO.getDireccionEntrega()) &&
						entPedDTO.getFechaEntregaCliente().toString().equals(entregaPedidoDTO.getFechaEntregaCliente().toString()) &&
						entPedDTO.getFechaDespachoBodega().toString().equals(entregaPedidoDTO.getFechaDespachoBodega().toString()) &&
						entPedDTO.getTipoEntrega().equals(entregaPedidoDTO.getTipoEntrega()) &&
						entPedDTO.getCodigoContextoEntrega().intValue()==entregaPedidoDTO.getCodigoContextoEntrega().intValue() &&
						entPedDTO.getCodigoAlcanceEntrega().intValue()==entregaPedidoDTO.getCodigoAlcanceEntrega().intValue() &&
						entPedDTO.getCodigoObtenerStock().intValue()==entregaPedidoDTO.getCodigoObtenerStock().intValue();
			}
		});
		if(CollectionUtils.isNotEmpty(entPedRes)){
			for(EntregaPedidoDTO entPed:entPedRes){
				entPed.setCalendarioDiaLocalDTO(entregaPedidoDTO.getCalendarioDiaLocalDTO());
			}
		}
		
		return entPedRes;
	}
	
	public static void eliminarEntregasPedido(HttpServletRequest request,
			HttpSession session, ActionMessages errors,
			ActionMessages warnings, CotizarReservarForm formulario) {
		try{
			LogSISPE.getLog().info("va a elminiar un detalle");
			//Obtengo la configuracion actual de reserva a bodega
			String tomarStock=(String)session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA);
			//se utiliza para contar cuantos detalles se eliminaron
			formulario.mantenerValoresEntregas(request);
			List<DetallePedidoDTO> detallePedidoDTOCol = null;
			
			if(formulario.getSeleccionEntregas().length>0){
				Collection<EntregaDetallePedidoDTO> entregasAux = new ArrayList<EntregaDetallePedidoDTO>();//almaceno las entregas de un mismo detalle que van a ser eliminadas
				DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
				int numEntrega=0;//indice de la entrega a eliminar
				int numDetalle=0;//indice del detalle
				long cantidadDespacho=0;
				String[] parametros = formulario.getSeleccionEntregas()[0].split("-");
				//guarda el indice del detalle para saber cuando ya se ha borrado todas la entregas de un mismo detalle
				int numDetalleAnterior = Integer.parseInt(parametros[1]);
				//obtiene la sumatoria
				Long sumaCantSolCD = session.getAttribute(EntregaLocalCalendarioAction.TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE) != null ? (Long)session.getAttribute(EntregaLocalCalendarioAction.TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE): 0L;
				
				//recupero los datos del detalle del pedido
				detallePedidoDTOCol = (List<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
				List<EntregaDetallePedidoDTO> entregasEliminarCol = new ArrayList<EntregaDetallePedidoDTO>();
				detallePedidoDTO = detallePedidoDTOCol.get(numDetalleAnterior);
				
				for(int i=0;i<formulario.getSeleccionEntregas().length;i++){
					LogSISPE.getLog().info("SeleccionEntregas: {}" , formulario.getSeleccionEntregas()[i]);
					parametros=formulario.getSeleccionEntregas()[i].split("-");
					numEntrega = Integer.parseInt(parametros[0]);
					numDetalle = Integer.parseInt(parametros[1]);
					LogSISPE.getLog().info("numeroDetalleAnterior: {}" , numDetalleAnterior);
					LogSISPE.getLog().info("numeroDetalle: {}" , numDetalle);
					LogSISPE.getLog().info("numeroEntrega: {}" , numEntrega);
					
					if(numDetalleAnterior != numDetalle){
						LogSISPE.getLog().info("va a elminar los detalles");
						for (EntregaDetallePedidoDTO entDetPedDTO:entregasAux) {
							//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
							if(entDetPedDTO.getEntregaPedidoDTO().getNpValidarCheckTransito() != null && entDetPedDTO.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
									&& entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
									&& entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito() != null && entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
								sumaCantSolCD = sumaCantSolCD - entDetPedDTO.getCantidadDespacho();
							}
							detallePedidoDTO.getEntregaDetallePedidoCol().remove(entDetPedDTO);
						}
						//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
						//solo en caso que la entrega sea parcial a bodega
						LogSISPE.getLog().info("****tomarStock**** " + tomarStock);
						if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
							cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
							LogSISPE.getLog().info("va a mantener el valor{}" , cantidadDespacho);
							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
						}
						cantidadDespacho = 0;
						entregasAux = new ArrayList<EntregaDetallePedidoDTO>();
						numDetalleAnterior = numDetalle;
						detallePedidoDTO = detallePedidoDTOCol.get(numDetalle);
					}
					

					LogSISPE.getLog().info("articulo detalle: {}" , detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
					List<EntregaDetallePedidoDTO> entregas=(List<EntregaDetallePedidoDTO>)detallePedidoDTO.getEntregaDetallePedidoCol();
					EntregaDetallePedidoDTO entregaDTO = (EntregaDetallePedidoDTO)entregas.get(numEntrega);
					//calculo los bultos
					int numeroBultos = 0;
					if(entregaDTO.getCantidadDespacho().longValue() > 0){
						numeroBultos =  UtilesSISPE.calcularCantidadBultos(entregaDTO.getCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
						entregaDTO.setNpCantidadBultos(numeroBultos);
					}
					entregasEliminarCol.add(entregaDTO);
					if(entregaDTO.getCantidadDespacho().longValue()>0)
						cantidadDespacho = cantidadDespacho + entregaDTO.getCantidadDespacho().longValue();
					detallePedidoDTO.setNpContadorEntrega(Long.valueOf(detallePedidoDTO.getNpContadorEntrega().longValue() - entregaDTO.getCantidadEntrega().longValue()));
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue() + entregaDTO.getCantidadEntrega().longValue()));
					LogSISPE.getLog().info("cantidad detalle: {}" , entregaDTO.getCantidadEntrega());
					
					//se elimina costo y direccion de envio en caso de entregas a domicilio
					formulario.eliminaCostoEntrega(entregaDTO,request);
					
					//Si existio pedido a bodega dentro de la entrega
					if(entregaDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO()!=null){
						detallePedidoDTO.setNpContadorDespacho(Long.valueOf(detallePedidoDTO.getNpContadorDespacho().longValue()-entregaDTO.getCantidadDespacho().longValue()));
						//Si el despacho es parcial desde bodega 
						if(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA)!=null){
							if(!session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega")))
								/*detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(0));
							else*/
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()+entregaDTO.getCantidadDespacho().longValue()));
						}else
							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0l);
						LogSISPE.getLog().info("dial local: {}" , entregaDTO.getEntregaPedidoDTO().getCalendarioDiaLocalDTO().getId().getFechaCalendarioDia());
						/*************************Modifico la CD del local eliminando la entrega************************/
						// Parametros para buscar la capacidad del local en la fecha
						CalendarioDiaLocalID calendarioDiaLocalID = new CalendarioDiaLocalID();
						calendarioDiaLocalID.setCodigoCompania(entregaDTO.getId().getCodigoCompania());
						if(entregaDTO.getEntregaPedidoDTO().getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
							LogSISPE.getLog().info("local a eliminar1: {}" , entregaDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
							calendarioDiaLocalID.setCodigoLocal(entregaDTO.getEntregaPedidoDTO().getCodigoAreaTrabajoEntrega());
						}
						else{
							LogSISPE.getLog().info("local a eliminar2: {}" , entregaDTO.getEntregaPedidoDTO().getCodigoLocalSector());
							calendarioDiaLocalID.setCodigoLocal(new Integer(entregaDTO.getEntregaPedidoDTO().getCodigoLocalSector()));
						}
						//verifica si el pedido es a domicilio para restar la capacidad de furgones solo del dia seleccionado
						Date mes=null;
//						if(entregaDTO.getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))  &&
//								formulario.getOpLocalResponsable().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
						if(entregaDTO.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))){
							 mes=DateManager.getYMDDateFormat().parse(entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente().toString());
						}else{
							mes=DateManager.getYMDDateFormat().parse(entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega().toString());
						}
						
						
						//Date mes=DateManager.getYMDDateFormat().parse(entregaDTO.getFechaDespachoBodega().toString());
						calendarioDiaLocalID.setFechaCalendarioDia(mes);
						CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = new CalendarioConfiguracionDiaLocalDTO();
						if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX)!=null)
							calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX);
						LogSISPE.getLog().info("EntregaLocal - BuscarDiaPorID");
						CalendarioDiaLocalDTO calendarioDiaLocalDTO2=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
						/**************actualizo la capacidad disponible en el local para el despacho***********/
						LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2: {}" , calendarioDiaLocalDTO2);
						LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Compania: {}" , calendarioDiaLocalDTO2.getId().getCodigoCompania());
						LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Local: {}" , calendarioDiaLocalDTO2.getId().getCodigoLocal());
						LogSISPE.getLog().info("EntregaLocal - calendarioDiaLocalDTO2Fecha: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
						LogSISPE.getLog().info("EntregaLocal - entregaDTO.np: {}" , entregaDTO.getNpCantidadBultos());
						LogSISPE.getLog().info("capacidad disponible2: {}" , calendarioDiaLocalDTO2.getCantidadDisponible());
						LogSISPE.getLog().info("CantidadAcumulada: {}" , calendarioDiaLocalDTO2.getCantidadAcumulada());
						LogSISPE.getLog().info("CapacidadAdicional: {}" , calendarioDiaLocalDTO2.getCapacidadAdicional());
						LogSISPE.getLog().info("CapacidadNormal(): {}" , calendarioDiaLocalDTO2.getCapacidadNormal());
						
						//cantidades informativas
						Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
						//cantidad para frio
						CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
						cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
						cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
						cantidadesInformativas.add(cantidadFrio);
						//cantidad para seco
						CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
						cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
						cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
						cantidadesInformativas.add(cantidadSeco);
						
						//veo si existe autorizacion para ese dia
						AutorizacionDTO autorizacionDTO = formulario.buscaAutorizacion(session, calendarioDiaLocalID.getCodigoLocal().toString(), entregaDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
						if(autorizacionDTO!=null)
							calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),true, cantidadesInformativas);
						else
							calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTO2.getId().getCodigoCompania(), calendarioDiaLocalDTO2.getId().getCodigoLocal(), calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),calendarioDiaLocalDTO2.getCapacidadNormal(), calendarioDiaLocalDTO2.getCapacidadAdicional(),new Double(-entregaDTO.getNpCantidadBultos().longValue()), new Double(0),false, cantidadesInformativas);
						/**********Va a modificar la cantidad acumulada***********************/
						LocalID localID=new LocalID();
						localID.setCodigoCompania(calendarioDiaLocalDTO2.getId().getCodigoCompania());
						localID.setCodigoLocal(calendarioDiaLocalDTO2.getId().getCodigoLocal());
						obtenerCalendario(request, localID, errors, calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), null, null, formulario, true);
						Object[] calendarioDiaLocalDTOOBJ=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);						
						int dia=-1;
						//Busco el dia de despacho
						LogSISPE.getLog().info("***********Va a buscar el dia de despacho*******************");
						for(int indice=0;indice<calendarioDiaLocalDTOOBJ.length;indice++){
							CalendarioDiaLocalDTO calDia=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOOBJ[indice];
							if(calDia.getId().getFechaCalendarioDia().equals(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia())){
								dia=indice;
								break;
							}
						
						}
						LogSISPE.getLog().info("dia: {}" , dia);
						LogSISPE.getLog().info("fecha de despacho: {}" , calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
						LogSISPE.getLog().info("fecha de entrega: {}" , entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
						cargaDiasModificarCA(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(), entregaDTO.getEntregaPedidoDTO().getFechaEntregaCliente(), formulario, request, dia);
						//Obtengo los dias que debo modificar su capacidad acumulada
						
						Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
						for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
							LogSISPE.getLog().info("%%%%%%%%%%%%%%%%VA A RESTABLECER LOS VALORES EN EL CALENDARIO%%%%%%%%%%%%%");
							LogSISPE.getLog().info("compania: {}" , calendarioDiaLocalDTOAux.getId().getCodigoCompania());
							LogSISPE.getLog().info("local: {}" , calendarioDiaLocalDTOAux.getId().getCodigoLocal());
							
							//cantidades informativas
							Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
							//cantidad para frio
							CantidadCalendarioDiaLocalDTO cantidadFrioAux = new CantidadCalendarioDiaLocalDTO();
							cantidadFrioAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
							cantidadFrioAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
							cantidadesInformativasAux.add(cantidadFrioAux);
							//cantidad para seco
							CantidadCalendarioDiaLocalDTO cantidadSecoAux = new CantidadCalendarioDiaLocalDTO();
							cantidadSecoAux.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
							cantidadSecoAux.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? -entregaDTO.getNpCantidadBultos().longValue() : 0L );
							cantidadesInformativasAux.add(cantidadSecoAux);
							
							calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), calendarioDiaLocalDTOAux.getCapacidadAdicional(), new Double(0), new Double(-entregaDTO.getNpCantidadBultos().longValue()),false,cantidadesInformativasAux);
							LogSISPE.getLog().info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
						}
						session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalDTO);
						/***************************************************************************************/
		
						/***********************************************************************************************/
						
					}
					entregasAux.add(entregaDTO);
					//..detallePedidoDTO.getEntregas().remove(entregaDTO);
					//seteo la variable de sesion para saber que existieron cambios
					session.setAttribute(EntregaLocalCalendarioAction.EXISTENCAMBIOS,"ok");
					//ordena la coleccion de detalles
					//..ordenarDetalleEntregas(session);
				}
				
				if(entregasAux.size()>0){
					LogSISPE.getLog().info("elimina las entregas:");
					
					for (EntregaDetallePedidoDTO entDetPedDTO:entregasAux) {
						//verifica si elimin\u00F3 una entrega con obligatoriedad de transito
						if(entDetPedDTO.getEntregaPedidoDTO().getNpValidarCheckTransito() != null && entDetPedDTO.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))  
								&& entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito() != null && entDetPedDTO.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) 
								&& entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega() == Integer.parseInt(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio"))
								){
							sumaCantSolCD = sumaCantSolCD - entDetPedDTO.getCantidadDespacho();
						}
						detallePedidoDTO.getEntregaDetallePedidoCol().remove(entDetPedDTO);
					}
					detallePedidoDTO= detallePedidoDTOCol.get(numDetalle);
					session.setAttribute(EntregaLocalCalendarioAction.TOTAL_SOLICITADO_CD_DOMICILIO_NO_PERECIBLE, sumaCantSolCD);
					
					//Dejo la cantidad de despacho con la suma de las cantidades eliminadas
					//solo en caso que la entrega sea parcial a bodega
					LogSISPE.getLog().info("cantidadDespacho: {}" , cantidadDespacho);
					LogSISPE.getLog().info("tomarStock: {}" , tomarStock);
					if(tomarStock!=null && tomarStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
						LogSISPE.getLog().info("va a mantener la cantidad");
						cantidadDespacho=cantidadDespacho+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue();
						detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(cantidadDespacho);
					
					}
					//detallePedidoDTO.getEntregas().remove(entCol);
					LogSISPE.getLog().info("numero de entregas del detalle: {}" , detallePedidoDTO.getEntregaDetallePedidoCol().size());
				}
				
				//valido obligatoriedad de tr\u00E1nsito
				for(DetallePedidoDTO detallePed : detallePedidoDTOCol){
					if(detallePed.getEntregaDetallePedidoCol() != null && !detallePed.getEntregaDetallePedidoCol().isEmpty()){
						for(EntregaDetallePedidoDTO entregaVal : (Collection<EntregaDetallePedidoDTO>)detallePed.getEntregaDetallePedidoCol()){
							if(sumaCantSolCD < Long.parseLong(((String)session.getAttribute(EntregaLocalCalendarioAction.PARAMETRO_LIMITE_INFERIOR_OBLIGATORIO_TRANSITO)))){
								if(entregaVal.getEntregaPedidoDTO().getNpValidarCheckTransito() != null && entregaVal.getEntregaPedidoDTO().getNpValidarCheckTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo")) && 
										entregaVal.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito() != null && entregaVal.getEntregaPedidoDTO().getNpPasoObligatorioBodegaTransito().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
									entregaVal.setNpPasoObligatorioBodegaTransito(null);
								}
							}
						}
					}
				}
				
				//if(session.getAttribute(EXISTENCAMBIOS)!=null)
				//	messages.add("eliminarDetalle",new ActionMessage("messages.eliminarDetalle"));
				
				//elimino los camiones que haran la entrega 
				EntregaLocalCalendarioUtil.eliminarCamionesDelPedido(entregasEliminarCol, request);
				//Itero los detalles para saber si fueron eliminadas todas las entregas
				boolean eliminadasTodas=true;
				LogSISPE.getLog().info("va a ver si se eliminaron todas las entregas");
				for(DetallePedidoDTO detallePedido : detallePedidoDTOCol){
					if(detallePedido.getEntregaDetallePedidoCol().size()>0){
						LogSISPE.getLog().info("Si tiene detalles la entrega");
						eliminadasTodas=false;
					}
				}
				if (eliminadasTodas){
					LogSISPE.getLog().info("todas las entregas fueron eliminadas");
					Integer local = 0;
					if(session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN)!=null){
						local=((VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN)).getId().getCodigoLocal();
					}
					formulario.setLocal(local.toString());
					formulario.setListaLocales(local.toString());
					VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
					vistaLocalDTO.getId().setCodigoLocal(local);
					vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
					LogSISPE.getLog().info("locales: {}" , vistaLocalDTOCol.size());
					if(vistaLocalDTOCol.size()>0){
						VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
						LogSISPE.getLog().info("Local: ...{}" , local);
						LocalID localID=new LocalID();
						localID.setCodigoCompania(vistaLocal.getId().getCodigoCompania());
						localID.setCodigoLocal(local);
						LogSISPE.getLog().info("****local: ****{}" , localID.getCodigoLocal());
						
						session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN,vistaLocal);
						session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO,vistaLocal);
						session.setAttribute(EntregaLocalCalendarioAction.LOCALID, localID);
						//obtenerCalendario(session, request, localID, errors,ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()) , null, null, formulario);
					}					
				}
			}else{
				warnings.add("eliminarEntrega",new ActionMessage("warnings.eliminarEntrega"));
			}
//			if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null && session.getAttribute(EntregaLocalCalendarioAction.LOCALID)!=null){
//			LocalID localID = (LocalID)session.getAttribute(EntregaLocalCalendarioAction.LOCALID);
//			//se verifica si se elimin\u00F3 todo el detalle
//			LogSISPE.getLog().info("va a consultar el calendario despues de eliminar las entregas");
//			//fecha minima
//			Date fechaMinima=(Date)session.getAttribute(EntregaLocalCalendarioAction.FECHAMINIMA);
//			/*****************************************************************************************/
//			//fecha maxima
//			Date fechaMaxima=(Date)session.getAttribute(EntregaLocalCalendarioAction.FECHAMAXIMA);
//			//if(entregaDTO.getTipoEntrega().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
//			//Diferencia entre fecha de entrega y fecha minima de entrega
//			//jmena verifica que la fechaEntregaCliente no sea null
//			if(formulario.getFechaEntregaCliente() == null || formulario.getFechaEntregaCliente().equals("")){
//				formulario.setFechaEntregaCliente((String)(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)));
//			}
//			Date mes=DateManager.getYMDDateFormat().parse(formulario.getFechaEntregaCliente());
//			
//			//jmena validacion para el nuevo calendario de bodega
//			if(vistaLocalDTOAUX != null && vistaLocalDTOAUX.getId().getCodigoLocal().equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))){
//				localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
//				buscaLocalBusqueda(formulario, request,localID.getCodigoLocal().toString());
//				//elimino el dia seleccionado del cal.bodega
//				formulario.setFechaEntregaCliente(null);
//			}
//			obtenerCalendario(request,localID,errors,mes,fechaMinima,fechaMaxima,formulario, true);
//		}
			//Asigno los codigoContextoResponsables.. para guardar los codigos en la tabla Entrega.
			EntregaLocalCalendarioUtil.procesarResponsablesEntrega(detallePedidoDTOCol, request);
			
			if(formulario.getTodo()!=null)
				session.removeAttribute(EntregaLocalCalendarioAction.EXISTENENTREGAS);
			formulario.setSeleccionEntregas(null);
			formulario.setTodo(null);
			LogSISPE.getLog().info("remover PoPuP");	
			session.removeAttribute(SessionManagerSISPE.POPUP);
			session.removeAttribute(EntregaLocalCalendarioAction.EXISTENCAMBIOS);
			session.removeAttribute(EntregaLocalCalendarioUtil.CAL_HORA_LOCAL_SELECCIONADOS_COL);
			session.removeAttribute(EntregaLocalCalendarioUtil.CALENDARIO_HORA_LOCAL_PROCESADO);
			eliminarEntregasPedido(request, detallePedidoDTOCol);
			

		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}


	public static void eliminarEntregasPedido(HttpServletRequest request,
			List<DetallePedidoDTO> detallePedidoDTOCol) {
		//verificar si quedan cabeceras, caso contrario tambien hay que eliminar
		//se verfica si existen entregas configuradas
		Collection<EntregaPedidoDTO> entregasPedido = (Collection<EntregaPedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)==null ? new ArrayList<EntregaPedidoDTO>():(Collection<EntregaPedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO);
		Collection<EntregaPedidoDTO> entregasEliminar= new ArrayList<EntregaPedidoDTO>(entregasPedido.size());
		for(final EntregaPedidoDTO entPedDTO: entregasPedido){
			Boolean eliminarEntrega=Boolean.FALSE;
			if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
				for(DetallePedidoDTO detallePedido : detallePedidoDTOCol){
					if(CollectionUtils.isNotEmpty(detallePedido.getEntregaDetallePedidoCol())){
						Collection<EntregaDetallePedidoDTO> entDetPedBus= CollectionUtils.select(detallePedido.getEntregaDetallePedidoCol(), new Predicate() {
							
							public boolean evaluate(Object arg0) {
								EntregaDetallePedidoDTO entDetPedDTO=(EntregaDetallePedidoDTO)arg0;
								return 	entPedDTO.getDireccionEntrega().equals(entDetPedDTO.getEntregaPedidoDTO().getDireccionEntrega()) &&
										entPedDTO.getFechaEntregaCliente().toString().equals(entDetPedDTO.getEntregaPedidoDTO().getFechaEntregaCliente().toString()) &&
										entPedDTO.getFechaDespachoBodega().toString().equals(entDetPedDTO.getEntregaPedidoDTO().getFechaDespachoBodega().toString()) &&
										entPedDTO.getTipoEntrega().equals(entDetPedDTO.getEntregaPedidoDTO().getTipoEntrega()) &&
										entPedDTO.getCodigoContextoEntrega().intValue()==entDetPedDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() &&
										entPedDTO.getCodigoAlcanceEntrega().intValue()==entDetPedDTO.getEntregaPedidoDTO().getCodigoAlcanceEntrega().intValue() &&
										entPedDTO.getCodigoObtenerStock().intValue()==entDetPedDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue();
							}
						});
						
						if(CollectionUtils.isEmpty(entDetPedBus)){
							eliminarEntrega=Boolean.TRUE;
						}else{
							eliminarEntrega=Boolean.FALSE;
						}
					}else{
						eliminarEntrega=Boolean.TRUE;
					}
				}
			}else{
				eliminarEntrega=Boolean.TRUE;
			}
			if(eliminarEntrega){
				entregasEliminar.add(entPedDTO);
			}
		}
		//eliminar entregas de la coleccion principal
		entregasPedido.removeAll(entregasEliminar);
		request.getSession().setAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO, entregasPedido);
	}
	
	/**
	 * Busca el local en base al que se va a realizar la b&uacute;squeda, sube a sesion el local destino y retorna true si encontr&oacute; el local
	 * @param formulario
	 * @param request
	 * @param local
	 * @return
	 * @throws Exception
	 */
	public static boolean buscaLocalBusqueda(CotizarReservarForm formulario,HttpServletRequest request,String local) throws Exception{
		VistaLocalDTO vistaLocalDTO=new VistaLocalDTO();
		vistaLocalDTO.getId().setCodigoLocal(new Integer(local));
		vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		LogSISPE.getLog().info("va a consultar el local");
		Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
		if(vistaLocalDTOCol!=null && vistaLocalDTOCol.size()>0){
			VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
			request.getSession().setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO, vistaLocal);
			return(true);
		}
		return(false);
	}
	
	/**
	 * 
	 * @param fechaCalendarioDia
	 * @param fechaEntregaCliente
	 * @param formulario
	 * @param request
	 * @param dia
	 */
	public static void cargaDiasModificarCA(Date fechaCalendarioDia, Date fechaEntregaCliente, CotizarReservarForm formulario, HttpServletRequest request,int dia){
		try{
			HttpSession session=request.getSession();
			Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
			//Armo una coleccion con los dias que voy a sumar capacidad acumulada
			Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=new ArrayList<CalendarioDiaLocalDTO>();
			CalendarioDiaLocalDTO calendarioDiaLocalDTOAux=new CalendarioDiaLocalDTO();
			//Obtengo cuantos dias hay desde el dia seleccionado hasta el dia de entrega
			GregorianCalendar fechaDespacho=new GregorianCalendar();
			fechaDespacho.setTime(fechaCalendarioDia);
			GregorianCalendar fechaEntrega=new GregorianCalendar();
			fechaEntrega.setTime(fechaEntregaCliente);
			int numdias=WebSISPEUtil.calcularDiasEntreFechas(fechaDespacho,fechaEntrega);
			int diasCalendario=calendarioDiaLocalDTOObj.length;
			LogSISPE.getLog().info("numero de dias entre la fecha de despacho y la fecha de entrega: {}" , numdias);
			for(int i=0;i<=numdias;i++){
				if((dia+i+1)<diasCalendario){
					calendarioDiaLocalDTOAux=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia + i +1];
					calendarioDiaLocalDTOCol.add(calendarioDiaLocalDTOAux);
				}
				
			}
			LogSISPE.getLog().info("dias a modificar: {}" , calendarioDiaLocalDTOCol.size());
			session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX, calendarioDiaLocalDTOCol);
		}catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}
	
	/**
	 * obtiene datos del calendario
	 * @param request
	 * @param localID
	 * @param errors
	 * @param mes
	 * @param fechaMinima
	 * @param fechaMaxima
	 * @param formulario
	 * @param verCalendario
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void obtenerCalendario(HttpServletRequest request,LocalID localID,ActionMessages errors,Date mes,Date fechaMinima,
			Date fechaMaxima,CotizarReservarForm formulario, Boolean verCalendario) throws Exception{
		
		try{
			//obtenemos la session
			HttpSession session=request.getSession();
			
			LogSISPE.getLog().info("*****entra a cargar el calendario*****");
			LogSISPE.getLog().info("Compania: {}" , localID.getCodigoCompania());
			LogSISPE.getLog().info("Local: {}" , localID.getCodigoLocal());
			CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX);
			LogSISPE.getLog().info("calendarioConfiguracionDiaLocalDTO: {}" , session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX));
			HashMap<Integer, HashMap<Date, List<?>>> calendariosEncontrados =  (HashMap<Integer, HashMap<Date, List<?>>>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOS_ENTREGAS_ELIMINAR);
			List<?> calendarioDiaLocalDTOCol = new ArrayList();
			Boolean buscarCalendario= Boolean.FALSE;
			try{
				if(calendariosEncontrados==null){
					calendariosEncontrados=new HashMap<Integer, HashMap<Date, List<?>>>();
					buscarCalendario= Boolean.TRUE;
				}else{
					HashMap<Date, List<?>> calendarioMes=calendariosEncontrados.get(localID.getCodigoLocal());
					if(calendarioMes==null){
						buscarCalendario= Boolean.TRUE;
					}else{
						calendarioDiaLocalDTOCol=calendarioMes.get(mes);
						
						if(calendarioDiaLocalDTOCol==null){
							buscarCalendario= Boolean.TRUE;
						}else if(localID.getCodigoLocal().intValue()==Integer.parseInt(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos"))){
							boolean hayRangoDespacho=false; 
							for(CalendarioDiaLocalDTO calendarioDia:(Collection<CalendarioDiaLocalDTO>)calendarioDiaLocalDTOCol){
								if(calendarioDia.getNpEstaEnRangoDespacho()){
									hayRangoDespacho=true;
									break;
								}
							}
							if(!hayRangoDespacho){
								buscarCalendario= Boolean.TRUE;	
							}
						}
					}
				}
				
				if(buscarCalendario){
					//Metodo para obtener el detalle del calendario enviando y el mes que deseo consultar
					calendarioDiaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarizacionParaLocalPorFecha(localID,null,mes,fechaMinima,fechaMaxima,calendarioConfiguracionDiaLocalDTO, verCalendario);
					HashMap<Date, List<?>> calendarioMesEncontrado= new  HashMap<Date, List<?>>();
					calendarioMesEncontrado.put(mes, calendarioDiaLocalDTOCol);
					calendariosEncontrados.put( localID.getCodigoLocal(), calendarioMesEncontrado);
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOS_ENTREGAS_ELIMINAR,calendariosEncontrados);
				}
				
				//LogSISPE.getLog().info("lista de calendario: " + calendarioDiaLocalDTOCol.size());
				LogSISPE.getLog().info("minima: {}" , fechaMinima);
				LogSISPE.getLog().info("maxima: {}" , fechaMaxima);
				Object[] calendarioDiaLocalDTOOBJ=calendarioDiaLocalDTOCol.toArray();
				session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL,calendarioDiaLocalDTOOBJ);
				//subo a session el mes de busqueda
				session.setAttribute(EntregaLocalCalendarioAction.MESSELECCIONADO,mes);
				//calculo cuantas semanas tiene el mes
				int maximoSemanas=(new Integer(calendarioDiaLocalDTOCol.size()/7).intValue());
				LogSISPE.getLog().info("numero de semanas: {}", maximoSemanas);
				//subo a sesion el numero de semanas
				session.setAttribute(EntregaLocalCalendarioAction.NUMEROSEMANAS,new Integer(maximoSemanas));
			}catch (Exception e) {
				if(errors != null){
					errors.add("localSinCalendario",new ActionMessage("warnings.configuracion.plantilla.por.defecto",localID.getCodigoLocal()));
				}
			} 
			
		}catch(Exception e){
			LogSISPE.getLog().info("error al cargar calendario: {}" , e.getMessage());  
			errors.add("obtenerCalendario",new ActionMessage("errors.obtener.calendario.local"));
		}
	}
	
	/**
	 * Metodo para reservar el stock de las reservas, crea una entrega por defecto con la configuracion, MI LOCAL - TOTAL - PEDIR AL CD
	 * @param detallePedidoDTOCol - Coleccion con todos los detalles del pedido
	 * @param fechaEntregaCliente - Fecha de entrega al Cliente
	 * @param horaEntregaCliente - Hora de entrega al Cliente 
	 * @param minutoEntregaCliente - minutos de entrega al Cliente
	 */
	public static void reservarStockEntregaDetallePedido(HttpServletRequest request,CotizarReservarForm formulario,
			Collection<DetallePedidoDTO> detallePedidoDTOCol,
			String fechaEntregaCliente, String horaEntregaCliente,String minutoEntregaCliente,ActionMessages errors) throws SISPEException{
		
		try{
			HttpSession session= request.getSession();
			//generar la fecha de entrega al cliente en formato yyyy-MM-dd hh:MM:ss
			GregorianCalendar fechaEntregaCompleta = new GregorianCalendar();
			fechaEntregaCompleta.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
			fechaEntregaCompleta.set(Calendar.HOUR_OF_DAY,Integer.parseInt(horaEntregaCliente));
			fechaEntregaCompleta.set(Calendar.MINUTE,Integer.parseInt(minutoEntregaCliente));
			
			//generar la fecha de entrega al cliente en formato yyyy-MM-dd
			GregorianCalendar fechaSinHora = new GregorianCalendar();
			fechaSinHora.setTime(ConverterUtil.parseStringToDate(fechaEntregaCliente));
			GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
			fechaSinHoraAux.add(Calendar.DAY_OF_MONTH, -1);
			
			//generar la fecha actual
			GregorianCalendar fechaActual = new GregorianCalendar();
			fechaActual.setTime(new Date());
			
			//generar la fecha de despacho al cliente en formato yyyy-MM-dd
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
			int nDiasAntes = Integer.parseInt(parametroDTO.getValorParametro());
			LogSISPE.getLog().info("numeroDiasAntesDeLaFechaEntrega {}",nDiasAntes);
			GregorianCalendar fechaSinHoraAux1 = (GregorianCalendar)fechaSinHora.clone();
			fechaSinHoraAux1.add(Calendar.DAY_OF_MONTH, -nDiasAntes);
			
			//Inicializar variables por defecto
			Boolean entregasDomicilio=Boolean.FALSE;
			String valorCodigoCiudadSector=null; //si sector de entrega
			Collection<CostoEntregasDTO> costoEntregasDTOCol=null;
			Collection<DireccionesDTO> direccionesDTOCol=null;
			String tipoEntrega=MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local");
			int totalBultos=0;
			String validarCheckTransito="";
			session.setAttribute(CotizarReservarAction.TIPO_ENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"));
			
			//Subo a session la configuracion -MI LOCAL-TOTAL-PEDIR AL CD
			session.setAttribute(EntregaLocalCalendarioAction.LUGARENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.miLocal"));
			session.setAttribute(EntregaLocalCalendarioAction.TIPOENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"));
			session.setAttribute(EntregaLocalCalendarioAction.STOCKENTREGA,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
			session.setAttribute(EntregaLocalCalendarioAction.STOCKENTREGAAUX,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"));
			
			//validar si existen canastos especiales deberia ser entidad responsable la bodega
			formulario.setOpElaCanEsp(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
			
			//crea la disponibilidad al local de la cantidad a reservar y setea la informacion para guaradar y enviar al SIC
			VistaLocalDTO vistaLocalDestinoDTO = cargarEntregaAlLocal(request,
					formulario, detallePedidoDTOCol, fechaEntregaCliente,
					errors, session);
			
			//crear la entregaDetallePedido
			crearEntregasDetallePedido(request,formulario, valorCodigoCiudadSector, validarCheckTransito, detallePedidoDTOCol, vistaLocalDestinoDTO,costoEntregasDTOCol,
					direccionesDTOCol, tipoEntrega, totalBultos, fechaEntregaCompleta,fechaSinHoraAux,fechaActual, entregasDomicilio,fechaSinHoraAux1,null,false);
			
			//setear objetos de configuracion de responsables
			procesarResponsablesEntrega(detallePedidoDTOCol,request);
			
		
		}catch (SISPEException ex){
			LogSISPE.getLog().error("Error al crear la entrega con la configuracion MI LOCAL-TOTAL-PEDIR AL CD",ex);		
			throw new SISPEException(ex);
		}
		catch (Exception ex){
			LogSISPE.getLog().error("Error al crear la entrega con la configuracion MI LOCAL-TOTAL-PEDIR AL CD",ex);		
			throw new SISPEException(ex);
		}
	}


	/**
	 * @param request
	 * @param formulario
	 * @param detallePedidoDTOCol
	 * @param fechaEntregaCliente
	 * @param errors
	 * @param session
	 * @return
	 * @throws ParseException
	 * @throws SISPEException
	 * @throws Exception
	 */
	public static VistaLocalDTO cargarEntregaAlLocal(
			HttpServletRequest request, CotizarReservarForm formulario,
			Collection<DetallePedidoDTO> detallePedidoDTOCol,
			String fechaEntregaCliente, ActionMessages errors,
			HttpSession session) throws ParseException, SISPEException,
			Exception {
		VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN);
		
		////
		if(vistaLocalDestinoDTO == null){
			final String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
			Integer local = 0;
			
			if(formulario.getLocalResponsable()!=null){
				//el dato almacenado es de la forma "codigoLocal - descripcionLocal"
				//por lo tanto se toma el c\u00F3digo del local de la posici\u00F3n 0
				local = Integer.parseInt(formulario.getLocalResponsable().split(SEPARADOR_TOKEN)[0].trim());
			}
			
			//Obtengo el local de origen
			VistaLocalDTO vistaLocalDTO = new VistaLocalDTO();
			vistaLocalDTO.getId().setCodigoLocal(local);
			vistaLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			Collection<VistaLocalDTO> vistaLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaLocal(vistaLocalDTO);
			VistaLocalDTO vistaLocal=(VistaLocalDTO)vistaLocalDTOCol.iterator().next();
			session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN, vistaLocal);
			//por defecto el local destino es el mismo de origen
			session.setAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO, vistaLocal);
		}
		vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALORIGEN);
		//////
		
		LocalID localID= new LocalID();
		localID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
		localID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
		
		
		//Parametros para buscar la capacidad del local en la fecha
		CalendarioDiaLocalID calendarioDiaLocalID = new CalendarioDiaLocalID();
		calendarioDiaLocalID.setCodigoCompania(vistaLocalDestinoDTO.getId().getCodigoCompania());
		calendarioDiaLocalID.setCodigoLocal(vistaLocalDestinoDTO.getId().getCodigoLocal());
		//verifica si el pedido es a domicilio para restar la capacidad de furgones solo del dia seleccionado
		Date mes=DateManager.getYMDDateFormat().parse(fechaEntregaCliente);
		calendarioDiaLocalID.setFechaCalendarioDia(mes);
		CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO = new CalendarioConfiguracionDiaLocalDTO();
		LogSISPE.getLog().info("EntregaLocal - BuscarDiaPorID");
		CalendarioDiaLocalDTO calendarioDiaLocalDTO2=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalID,calendarioConfiguracionDiaLocalDTO);
		
		session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX, calendarioConfiguracionDiaLocalDTO);
		
		obtenerCalendario(request,localID,errors, calendarioDiaLocalDTO2.getId().getFechaCalendarioDia(),null,
				null,formulario, Boolean.FALSE);
		
		Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
		
		
		//Obtengo el calendario y reservar la cantidad de almacenamiento en el.
		GregorianCalendar fechaDespacho=new GregorianCalendar();
		fechaDespacho.setTime(calendarioDiaLocalDTO2.getId().getFechaCalendarioDia());
		int dia=fechaDespacho.get(GregorianCalendar.DAY_OF_MONTH);
		
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia];
		LogSISPE.getLog().info("seleccionado: {}" , calendarioDiaLocalDTO.getNpEsSeleccionado());
		LogSISPE.getLog().info("fechaCalendario: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
		
		EntregaLocalCalendarioUtil.cargaDiasModificarCA(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), ConverterUtil.parseStringToDate(fechaEntregaCliente), formulario, request, dia);
		session.setAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO, dia);
		session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL,calendarioDiaLocalDTO);
		calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.TRUE);
		formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
		LogSISPE.getLog().info("dia seleccionado: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
		//session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
		formulario.setCalendarioDiaLocal(calendarioDiaLocalDTOObj);
		
		//Si se va tomar todo de bodega setear el maximo numero de articulos que se pueden pedir a bodega
			LogSISPE.getLog().info("va a tomar todo de bodega");
			
			for(DetallePedidoDTO detallePedidoDTO:detallePedidoDTOCol){
				if(detallePedidoDTO.getNpContadorEntrega()!=null)
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() - detallePedidoDTO.getNpContadorEntrega().longValue()));
				else
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()));
			}
		return vistaLocalDestinoDTO;
	}


	/**
	 * @param request
	 * @param session
	 * @param formulario
	 * @param valorCodigoCiudadSector
	 * @param validarCheckTransito
	 * @param sumaCantSolCD
	 * @param detallePedidoDTOCol
	 * @param vistaLocalDestinoDTO
	 * @param costoEntregasDTOCol
	 * @param direccionesDTOCol
	 * @param tipoEntrega
	 * @param totalBultos
	 * @param fechaEntregaCompleta
	 * @param fechaSinHoraAux
	 * @param fechaActual
	 * @param entregasDomicilio
	 * @param fechaSinHoraAux1
	 * @param editarEntrega 
	 * @throws Exception
	 * @throws SISPEException
	 */
	public static int crearEntregasDetallePedido(HttpServletRequest request,CotizarReservarForm formulario, String valorCodigoCiudadSector, String validarCheckTransito, Collection<DetallePedidoDTO> detallePedidoDTOCol,
			VistaLocalDTO vistaLocalDestinoDTO, Collection<CostoEntregasDTO> costoEntregasDTOCol, Collection<DireccionesDTO> direccionesDTOCol, String tipoEntrega, int totBul, GregorianCalendar fechaEntregaCompleta,
			GregorianCalendar fechaSinHoraAux, GregorianCalendar fechaActual, Boolean entregasDomicilio, GregorianCalendar fechaSinHoraAux1,String valorCodigoTransito, Boolean editarEntrega) throws Exception, SISPEException {
		
		int indexDetalles=0;//indice para las cantidades de los detalles
		int detallesAgregados=0;//contador para saber cuantos detalles nuevos se agregaros
		int totalBultos = totBul;
		HttpSession session= request.getSession();
		
		//Ingreso de nuevos detalles
		for(Iterator<DetallePedidoDTO> it = detallePedidoDTOCol.iterator();it.hasNext();){
			
			DetallePedidoDTO detallePedidoDTO = it.next();
			if(detallePedidoDTO.getNpHabilitarEntregaObsoletos()){//permite solo confugurar articulos obsoletos que tengan la conficuracion de ser entregados por el LOCAL.
				//se verfica si existen entregas configuradas
				Collection<EntregaPedidoDTO> entregasPedido = (Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO)==null ? new ArrayList<EntregaPedidoDTO>():(Collection<EntregaPedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO);
				
				Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
				
				LogSISPE.getLog().info(new StringBuilder().append("-------codArticulo ").append(detallePedidoDTO.getId().getCodigoArticulo())
						.append(" CantidadEstado-------- ").append(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()).toString());
				
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado()>0){
					LogSISPE.getLog().info("cantidadEstado[{}]: {}" ,indexDetalles, detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
					
					//agrego un detalle del pedido
					Collection<EntregaDetallePedidoDTO> entregas = detallePedidoDTO.getEntregaDetallePedidoCol()==null ? new ArrayList<EntregaDetallePedidoDTO>():detallePedidoDTO.getEntregaDetallePedidoCol();
					
					EntregaPedidoDTO entregaPedidoDTO = null;
					EntregaDetallePedidoDTO entregaDetallePedidoDTO=null;
					//se crea la cabecera
					entregaPedidoDTO = crearEntregaPedido(request, formulario, valorCodigoCiudadSector, 
								fechaEntregaCompleta, fechaSinHoraAux, fechaSinHoraAux1, tipoEntrega, entregasDomicilio,valorCodigoTransito,editarEntrega);
					
					//verificar si la entregaPedido ya esta configurada					
					Collection<EntregaPedidoDTO> entPedCol=EntregaLocalCalendarioUtil.buscarEntregasConfiguradas(entregasPedido,entregaPedidoDTO);
					
						//Solo si no encuentra la entrega tiene que agregar
						if(CollectionUtils.isEmpty(entPedCol)){
							
							//creamos el detalle
							entregaDetallePedidoDTO = crearDetalleEntrega(request, formulario, detallePedidoDTO, 
										entregaPedidoDTO, entregasDomicilio, fechaActual, validarCheckTransito);
								
							totalBultos += entregaDetallePedidoDTO.getNpCantidadBultos(); 
							detallePedidoDTO.setNpContadorEntrega(new Long(detallePedidoDTO.getNpContadorEntrega().longValue()+entregaDetallePedidoDTO.getNpCantidadEntrega().longValue()));
								
							entregaPedidoDTO.setEntregaDetallePedidoCol(new ListSet());
							entregaPedidoDTO.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
							
							
							entregasPedido.add(entregaPedidoDTO);
							session.setAttribute(EntregaLocalCalendarioAction.ENTREGAS_PEDIDO, entregasPedido);
							//AGREGAR CODIGO DE DIRECCIONES Y COSTOS
							if(tipoEntrega.toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
								calcularCostoEntrega(request, formulario, entregaPedidoDTO);
								crearDireccionCosto(request, formulario, entregaDetallePedidoDTO, vistaLocalDestinoDTO.getId().getCodigoLocal(),costoEntregasDTOCol,direccionesDTOCol);
							}
						}else if(entPedCol.size()==1){
							EntregaPedidoDTO entPedDTO=entPedCol.iterator().next();

							//creamos el detalle
							entregaDetallePedidoDTO = crearDetalleEntrega(request, formulario, detallePedidoDTO, 
									entPedDTO, entregasDomicilio, fechaActual, validarCheckTransito);
							
							totalBultos += entregaDetallePedidoDTO.getNpCantidadBultos();
							detallePedidoDTO.setNpContadorEntrega(new Long(detallePedidoDTO.getNpContadorEntrega().longValue()+entregaDetallePedidoDTO.getNpCantidadEntrega().longValue()));
							
							entPedDTO.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
							
							Boolean entregaConDomicilioExistente=Boolean.FALSE;
							/*******************Comprueba si la direccion ya ha sido ingresada al menos una vez*********/
							//verificamos solo si la entrega configurada es a domicilio para validar las direcciones repetidas.
							if(tipoEntrega.toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
								if(direccionesDTOCol.size()>0){
									for (DireccionesDTO direccionesDTO:direccionesDTOCol) {
										LogSISPE.getLog().info("NpDireccionEntregaDomicilio: {}" , entPedDTO.getNpDireccionEntregaDomicilio());
										LogSISPE.getLog().info("codigoDireccion: {}" , direccionesDTO.getCodigoDireccion());
										if(entPedDTO.getNpDireccionEntregaDomicilio()!=null && entPedDTO.getNpDireccionEntregaDomicilio().equals(direccionesDTO.getCodigoDireccion())){
											LogSISPE.getLog().info("no hay nueva direccion");
											entregaConDomicilioExistente = true;
											//Si existe ya la direccion incrementa su contador
											direccionesDTO.setNumeroDireccion(direccionesDTO.getNumeroDireccion()+1);
										}
									}
								}
							}
							
							/*******************Comprueba si el costo ya ha sido ingresado al menos una vez*********/
							if(costoEntregasDTOCol!=null && costoEntregasDTOCol.size()>0 && entregaConDomicilioExistente){
								for(CostoEntregasDTO costoEntregasDTO: costoEntregasDTOCol){
									LogSISPE.getLog().info("costo.sector: {}" , costoEntregasDTO.getCodigoSector());
									LogSISPE.getLog().info("costo.fechaEntrega{}" , costoEntregasDTO.getFechaEntrega());
									LogSISPE.getLog().info("entrega.fechaEntrega{}" , entregaPedidoDTO.getFechaEntregaCliente());
									LogSISPE.getLog().info("entrega.direccion: {}", entPedDTO.getDireccionEntrega());
									if(entPedDTO.getDireccionEntrega().equals(costoEntregasDTO.getCodigoSector()) && costoEntregasDTO.getFechaEntrega().equals(entPedDTO.getFechaEntregaCliente())){
										LogSISPE.getLog().info("no hay nuevo costo");
										//Si existe ya el costo incremente su contador
										costoEntregasDTO.setNumeroEntregas(costoEntregasDTO.getNumeroEntregas()+1);
										LogSISPE.getLog().info("numeroEntregas: {}",costoEntregasDTO.getNumeroEntregas());
									}
								}
							}										
						}
						
						//Entregas a Locales
						if(!tipoEntrega.toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
							LogSISPE.getLog().info("Entrega a Domicilio...");
						
							totalEntregaParcialSeleccionada = UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO);
							LogSISPE.getLog().info("Valor entrega parcial {} ", totalEntregaParcialSeleccionada);
							entregaPedidoDTO.setCostoParcialEntrega(totalEntregaParcialSeleccionada);
							//totalEntregaSeleccionada += totalEntregaParcialSeleccionada;
							//-----------------------------------------------------------------------------------------------------------------------------------																		
						}
						
						//Asigna la autorizacion si la tiene //TODO Autorizaciones
	//															if (autorizacionDTO!=null){
	//																entregaPedidoDTO.setAutorizacionDTO(autorizacionDTO);
	//															}
						
						//Llenar la coleccion de entregas
						entregas.add(entregaDetallePedidoDTO);
						
						//A\u00F1ado la entrega
						detallePedidoDTO.setEntregaDetallePedidoCol(entregas);
						//Indico cual es la cantidad que falta por entregar
						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue()));
	//														}
					LogSISPE.getLog().info("agrego un detalle");
					detallesAgregados++;
					//existieron cambios en las entregas
					session.setAttribute(EntregaLocalCalendarioAction.EXISTENCAMBIOS,"ok");
				}else{
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorEntrega().longValue());
				}
				indexDetalles++;
			}
		}//fin for
		session.setAttribute("ec.com.smx.sic.sipse.totalBultos", totalBultos);
		return detallesAgregados; 
	}
	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param valorCodigoCiudadSector
	 * @param fechaEntregaCompleta
	 * @param fechaSinHora
	 * @param fechaSinHoraAux1
	 * @param tipoEntrega
	 * @param entregasDomicilio
	 * @return
	 * @throws Exception
	 */
	private static EntregaPedidoDTO crearEntregaPedido(HttpServletRequest request, CotizarReservarForm formulario, String valorCodigoCiudadSector, 
			GregorianCalendar fechaEntregaCompleta,	GregorianCalendar fechaSinHora, GregorianCalendar fechaSinHoraAux1, String tipoEntrega, Boolean entregasDomicilio,String valorCodigoTransito,Boolean editarEntrega) throws Exception{
		
		HttpSession session = request.getSession();
		
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=null;
		if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL)!=null){
			LogSISPE.getLog().info("si existe calendario");
			//obtengo el dia seleccionado
			calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
		}
		
		//obtengo el local destino
		VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO);
		LogSISPE.getLog().info("local destino: {}" , vistaLocalDestinoDTO.getId().getCodigoLocal());
		
		GregorianCalendar fechaSinHoraAux = (GregorianCalendar)fechaSinHora.clone();
		
		//se crea una nueva entrega
		//cabecera entrega
		EntregaPedidoDTO entregaPedidoDTO = new EntregaPedidoDTO();
		entregaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		entregaPedidoDTO.setCodigoCiudadSectorEntrega(valorCodigoCiudadSector);
		//setear el codigo de la bodega de transito para el caso en que sea una a domicilio de guayaquil
		entregaPedidoDTO.setNpValorCodigoTransito(valorCodigoTransito);
		entregaPedidoDTO.setFechaEntregaCliente(new Timestamp(fechaEntregaCompleta.getTimeInMillis()));
		entregaPedidoDTO.setTipoEntrega(tipoEntrega);
		entregaPedidoDTO.setCodigoAlcanceEntrega(Integer.valueOf((String)(session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA))));
		entregaPedidoDTO.setCodigoContextoEntrega(Integer.valueOf((String)(session.getAttribute(EntregaLocalCalendarioAction.LUGARENTREGA))));
		entregaPedidoDTO.setCodigoObtenerStock(Integer.valueOf((String)(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA))));
		//verifica si se esta editando una entrega y si esta en sesion el codigo previo para la nueva entrega
		if(editarEntrega!=null && editarEntrega  && session.getAttribute(EntregaLocalCalendarioAction.CODIGOENTREGAPREVIO)!=null){
			entregaPedidoDTO.setCodigoEntregaPedidoPrevio((Long) session.getAttribute(EntregaLocalCalendarioAction.CODIGOENTREGAPREVIO));
		}
		
		if(calendarioDiaLocalDTO!=null){
			
			LogSISPE.getLog().info("tiene pedido a bodega");
			//LogSISPE.getLog().info("**CantidadReservarSIC:** {}", detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
			entregaPedidoDTO.setCalendarioDiaLocalDTO(calendarioDiaLocalDTO);
			
			//entregas a domicilio
			if(entregasDomicilio){
				entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux1.getTime().getTime()));
			}else{
				if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
					if(entregaPedidoDTO.getCodigoContextoEntrega().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.otroLocal")))){
						SimpleDateFormat formatoFecha = new SimpleDateFormat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.formato.fecha"));
						String fechaCalendarioString=formatoFecha.format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
						LogSISPE.getLog().info("fechaCalendarioString: "+fechaCalendarioString);
						entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(formatoFecha.parse(fechaCalendarioString).getTime()));
					}else{
						entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux.getTime().getTime()));
					}
				}else{
					SimpleDateFormat formatoFecha = new SimpleDateFormat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.formato.fecha"));
					String fechaCalendarioString=formatoFecha.format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia());
					LogSISPE.getLog().info("fechaCalendarioString: "+fechaCalendarioString);
					entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(formatoFecha.parse(fechaCalendarioString).getTime()));
//					entregaPedidoDTO.setFechaDespachoBodega( (Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
				}
			}
		}else{//si no existe despacho a bodega se setea la fechaDespachoBodega porq es obligatorio
			entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHora.getTime().getTime()));
			//Si la entidad responsable es la bodega y la entrega es a domicilio 
		}
		
		//Entregas a Locales
		if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
			LogSISPE.getLog().info("Entrega a Locales...");
				//validar si las entregas es en el local  y existen canastos especiales, entonces el calendario mostrado es de la 99, pero la direccion de entrega y lugar de entrega es el local
				if(vistaLocalDestinoDTO.getId().getCodigoLocal().intValue()!= Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito")).intValue() && vistaLocalDestinoDTO.getId().getCodigoLocal().intValue()!=Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.codigoCDCanastos")).intValue()){
					entregaPedidoDTO.setCodigoAreaTrabajoEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal());
					entregaPedidoDTO.setDireccionEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.prefijoLocal")+": "+vistaLocalDestinoDTO.getId().getCodigoLocal()+ " - " + vistaLocalDestinoDTO.getNombreLocal());
				}
				else{
					entregaPedidoDTO.setCodigoAreaTrabajoEntrega(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
					entregaPedidoDTO.setDireccionEntrega(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.prefijoLocal")+": "+SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal()+ " - " + SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
				}
				//En caso de ser domicilio desde local
				 if(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL)){
						entregaPedidoDTO.setQuienRecibira(formulario.getQuienRecibeSicmer());
						entregaPedidoDTO.setCodigoVendedor(Long.parseLong(formulario.getCodigoVendedorSicmer()));
						LogSISPE.getLog().info("Entrega a Domicilio desde Local...");
						entregaPedidoDTO.setCodigoLocalSector(vistaLocalDestinoDTO.getId().getCodigoLocal());
						LogSISPE.getLog().info("codigo sector entrega: {}" , entregaPedidoDTO.getCodigoLocalSector());
						entregaPedidoDTO.setDireccionEntrega(formulario.getDireccion());
						entregaPedidoDTO.setCodigoDivGeoPol((String)session.getAttribute(EntregaLocalCalendarioAction.VAR_VALOR_CODIGO_CIUDAD_COMBO));
						
						//asignar las horas de entrega
						entregaPedidoDTO.setHorasEntrega(formulario.getHoraDesde()+" - "+formulario.getHoraHasta());
					}
		}else{//Entregas a domicilio //hasta aca
			LogSISPE.getLog().info("Entrega a Domicilio...");
			
			//Si existe un local intermedio para la entrega a domicilio
			if(session.getAttribute(EntregaLocalCalendarioAction.ENTIDADRESPONSABLELOCAL)!=null){
				entregaPedidoDTO.setCodigoLocalSector(vistaLocalDestinoDTO.getId().getCodigoLocal());
				LogSISPE.getLog().info("codigo sector entrega: {}" , entregaPedidoDTO.getCodigoLocalSector());
				if(session.getAttribute(EntregaLocalCalendarioAction.DIRECCION)==null)
					entregaPedidoDTO.setDireccionEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal()+ ": - " + formulario.getDireccion());
				else{
					DireccionesDTO direccionSeleccionada = (DireccionesDTO)session.getAttribute(EntregaLocalCalendarioAction.DIRECCION);
					entregaPedidoDTO.setDireccionEntrega(vistaLocalDestinoDTO.getId().getCodigoLocal()+ ": - " + direccionSeleccionada.getDescripcion());
				}
			}else{
				//IOnofre. Setea en al objeto EntregaPedido los nuevos campos de la direcion.
				entregaPedidoDTO.setCallePrincipal(formulario.getCallePrincipal());
				entregaPedidoDTO.setNumeroCasa(formulario.getNumeroCasa());
				entregaPedidoDTO.setCalleTransversal(formulario.getCalleTransversal());
				entregaPedidoDTO.setReferencia(formulario.getReferencia());
				entregaPedidoDTO.setDireccionEntrega(formulario.getDireccion());
				entregaPedidoDTO.setCodigoLocalSector(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
				entregaPedidoDTO.setCodigoDivGeoPol((String)session.getAttribute(EntregaLocalCalendarioAction.VAR_VALOR_CODIGO_CIUDAD_COMBO));
			}
		}
		
		//verifico si eligio como responsable de las canastas especiales a la Bod99
		if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
			entregaPedidoDTO.setElaCanEsp(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"));
		}else if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
			entregaPedidoDTO.setElaCanEsp(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"));
		}
		
		return entregaPedidoDTO;
	}
	
	

	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param detallePedidoDTO
	 * @param entregaPedidoDTO
	 * @param numeroBultos
	 * @param entregasDomicilio
	 * @param sumaCantSolCD
	 * @param fechaActual
	 * @return
	 * @throws MissingResourceException
	 * @throws Exception
	 */
	private static EntregaDetallePedidoDTO crearDetalleEntrega(HttpServletRequest request,  CotizarReservarForm formulario, DetallePedidoDTO detallePedidoDTO, 
			EntregaPedidoDTO entregaPedidoDTO, Boolean entregasDomicilio,  GregorianCalendar fechaActual,String validarCheckTransito) throws MissingResourceException, Exception{
		
		HttpSession session = request.getSession();
		
		//se obtienen las claves que indican un estado activo y un estado inactivo
		final String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		final String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		int numeroBultos=0;
		Long sumaCantSolCD =((Long)session.getAttribute("ec.com.smx.sic.sipse.sumaCantSolCD"))==null?0L:(Long)session.getAttribute("ec.com.smx.sic.sipse.sumaCantSolCD");
		
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=null;
		if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL)!=null){
			LogSISPE.getLog().info("si existe calendario");
			//obtengo el dia seleccionado
			calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
		}
		
		//Detalle entrega
		EntregaDetallePedidoDTO entregaDetallePedidoDTO = new EntregaDetallePedidoDTO();
		entregaDetallePedidoDTO.getId().setCodigoCompania(entregaPedidoDTO.getId().getCodigoCompania());
		entregaDetallePedidoDTO.setCodigoArticulo(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
		entregaDetallePedidoDTO.setCantidadEntrega(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
		entregaDetallePedidoDTO.setNpCantidadEntrega(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
		entregaDetallePedidoDTO.setCantidadParcialDespacho(0D);
		entregaDetallePedidoDTO.setNpCantidadBultos(0);
		
		//agregamos la cabecera al detalle
		entregaDetallePedidoDTO.setEntregaPedidoDTO(entregaPedidoDTO);
		
		//Cuando se realizan despachos de bodega
		if(calendarioDiaLocalDTO!=null){
			//entregas a domicilio
			if(entregasDomicilio){
				//entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux1.getTime().getTime()));
				entregaDetallePedidoDTO.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
				entregaDetallePedidoDTO.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
			}else{
				if(formulario.getOpElaCanEsp() != null && formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
					//entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHoraAux.getTime().getTime()));
					entregaDetallePedidoDTO.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
					entregaDetallePedidoDTO.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
				}else{
					//entregaPedidoDTO.setFechaDespachoBodega((Timestamp)convertidor.convert(Timestamp.class,ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia())));
					entregaDetallePedidoDTO.setNpCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
					entregaDetallePedidoDTO.setCantidadDespacho(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
					if(entregaDetallePedidoDTO.getCantidadDespacho().longValue()==0){
						entregaDetallePedidoDTO.setFechaRegistroDespacho(entregaPedidoDTO.getFechaEntregaCliente());
					}
				}
			}
			
			if(detallePedidoDTO.getNpContadorDespacho()==null){
				detallePedidoDTO.setNpContadorDespacho(0L);
			}
			detallePedidoDTO.setNpContadorDespacho(Long.valueOf(detallePedidoDTO.getNpContadorDespacho().longValue()+entregaDetallePedidoDTO.getNpCantidadDespacho().longValue()));
			//Si el despacho es parcial desde bodega 
			if(session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))){
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
			}
			else{
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-detallePedidoDTO.getNpContadorDespacho().longValue()));
			}
			LogSISPE.getLog().info("***CantidadReservarSIC:*** {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC());
			
			if(entregaDetallePedidoDTO.getCantidadDespacho().longValue()>0){
				//calculo los bultos
				numeroBultos = UtilesSISPE.calcularCantidadBultos(entregaDetallePedidoDTO.getCantidadDespacho().longValue(), detallePedidoDTO.getArticuloDTO());
				if(session.getAttribute(EntregaLocalCalendarioAction.MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null){
					//quiere decir que es entregas a domicilio CD, no se calculan los bultos, esta configurando UNA ENTREGA
					numeroBultos = 1;
				}
				entregaDetallePedidoDTO.setNpCantidadBultos(numeroBultos);
			}
			//OANDINO: Verificar si la cantidad de despacho es mayor a cero, seg\u00FAn esto mostrar o no checkboxs de transito ---------
			LogSISPE.getLog().info("Cant. Despacho: {}",entregaDetallePedidoDTO.getCantidadDespacho());
			
			//TODO Validar transito
			if((!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica")) && entregaDetallePedidoDTO.getCantidadDespacho().longValue()>0){																				
				entregaPedidoDTO.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
			}else{
				entregaPedidoDTO.setNpValidarCheckTransito(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
			}
			
		}else{//si no existe despacho a bodega se setea la fechaDespachoBodega porq es obligatorio
			//entregaPedidoDTO.setFechaDespachoBodega(new Timestamp(fechaSinHora.getTime().getTime()));
			//Si la entidad responsable es la bodega y la entrega es a domicilio 
			//se asigna la fechaRegistroDespacho para que la bodega no despache el registro
			entregaDetallePedidoDTO.setFechaRegistroDespacho(new Timestamp(fechaActual.getTime().getTime()));
			entregaDetallePedidoDTO.setNpCantidadDespacho(0L);
			entregaDetallePedidoDTO.setCantidadDespacho(0L);
		}
		
		//Entregas a Locales
		if(session.getAttribute(CotizarReservarAction.TIPO_ENTREGA).toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.direccion"))){
			//OANDINO: Se valida el valor para presentar o no el checkbox de local de tr\u00E1nsito --------------------------------------------------
			if((!validarCheckTransito.equals("") && validarCheckTransito.equals("siAplica")) && entregaDetallePedidoDTO.getCantidadDespacho().longValue()>0){
				entregaPedidoDTO.setNpValidarCheckTransito(estadoActivo);
				//Se suman las cantidades pedidas a bodega
				if(!EntregaLocalCalendarioUtil.verificarArticuloPerecible(request, detallePedidoDTO.getArticuloDTO().getCodigoClasificacion())){
					sumaCantSolCD = sumaCantSolCD + entregaDetallePedidoDTO.getCantidadDespacho();
					
					LogSISPE.getLog().info("**(2)Sumatoria de articulos solicitados al CD: {}", sumaCantSolCD);
				}
			}else{
				entregaPedidoDTO.setNpValidarCheckTransito(estadoInactivo);
			}
			session.setAttribute("ec.com.smx.sic.sipse.sumaCantSolCD", sumaCantSolCD);
		}
		return entregaDetallePedidoDTO;
		
	}
	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param entregaPedidoDTO
	 * @throws MissingResourceException
	 * @throws Exception
	 */
	private static void calcularCostoEntrega(HttpServletRequest request, CotizarReservarForm formulario, EntregaPedidoDTO entregaPedidoDTO) throws MissingResourceException, Exception{
		
		HttpSession session = request.getSession();
		double distancia=0;//distancia entre el local y la direccion
		
		Double totalEntregaParcialSeleccionada = 0D; //queda temporalmente asi
		Double valorParcialEntrega =(Double)session.getAttribute(EntregaLocalCalendarioAction.VALORPARCIALENTREGA);
		//Calculo el costo
		//Si la distancia fue ingresada en tiempo
		if(formulario.getUnidadTiempo().equals("H")){
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.factorConversionEntregasDomicilio", request);
			int prametroConversionAKilometros=(new Integer(parametroDTO.getValorParametro())).intValue();
			//transformo la distancia de tiempo a kilometros
			long hora = (Long.valueOf(formulario.getDistanciaH())).longValue()*60;
			long minutos = (Long.valueOf(formulario.getDistanciaM())).longValue();
			double totalMinutos = hora + minutos;
			distancia = totalMinutos * prametroConversionAKilometros;
			//session.getAttribute(STOCKENTREGA)
			entregaPedidoDTO.setCostoEntrega(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(Double.valueOf(distancia), request, totalEntregaParcialSeleccionada,
					session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
			LogSISPE.getLog().info("costo H****: {}" , entregaPedidoDTO.getCostoEntrega());
			entregaPedidoDTO.setDistanciaEntrega(distancia);
			entregaPedidoDTO.setNpValorParcialEntrega(valorParcialEntrega==null?0:valorParcialEntrega);
		}else{//Si la distancia fue ingresada en kilometros
			entregaPedidoDTO.setCostoEntrega(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(Double.valueOf(formulario.getDistancia()),request, totalEntregaParcialSeleccionada, 
					session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
			distancia = (Double.valueOf(formulario.getDistancia())).doubleValue();
			entregaPedidoDTO.setDistanciaEntrega(distancia);
			entregaPedidoDTO.setNpValorParcialEntrega(valorParcialEntrega==null?0:valorParcialEntrega);
			LogSISPE.getLog().info("costo K****: {}" , entregaPedidoDTO.getCostoEntrega());
		}
		session.removeAttribute(EntregaLocalCalendarioAction.VALORPARCIALENTREGA);
	}
	
	/**
	 * 
	 * @param request
	 * @param formulario
	 * @param entregaPedidoDTO
	 * @param codigoLocal
	 */
private static void crearDireccionCosto(HttpServletRequest request, CotizarReservarForm formulario, EntregaDetallePedidoDTO entregaDetallePedidoDTO, Integer codigoLocal,Collection<CostoEntregasDTO> costoEntregasDTOCol,Collection<DireccionesDTO> direccionesDTOCol){
		
		try{
			HttpSession session = request.getSession();
			
			if(session.getAttribute(EntregaLocalCalendarioAction.DIRECCION)!=null){
				//Si selecciono una de las direcciones ingresadas anteriormente
				DireccionesDTO direccionesDTO2 = (DireccionesDTO)session.getAttribute(EntregaLocalCalendarioAction.DIRECCION);
				LogSISPE.getLog().info("codigo direccion: {}" ,direccionesDTO2.getCodigoDireccion());
				entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpDireccionEntregaDomicilio(direccionesDTO2.getCodigoDireccion());
			}else{
				//Si es una nueva direccion le asigna el secuencial de direcciones
				//Contador de direcciones
				int secDirecciones=1;
				if(session.getAttribute(EntregaLocalCalendarioAction.SECDIRECCIONESAUX)!=null){
					secDirecciones=((Integer)session.getAttribute(EntregaLocalCalendarioAction.SECDIRECCIONESAUX)).intValue();
				}
				LogSISPE.getLog().info("secDirecciones:.- {}" , secDirecciones);
				entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpDireccionEntregaDomicilio(Integer.valueOf(secDirecciones).toString());
			}
			
			LogSISPE.getLog().info("dir registrada: {}", entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
			
			LogSISPE.getLog().info("Registro la direccion, esta es la primera direccion");
			DireccionesDTO direccionesDTO1 = new DireccionesDTO();
			direccionesDTO1.setCodigoDireccion(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpDireccionEntregaDomicilio());
			LogSISPE.getLog().info("codigoDir:  {}" , direccionesDTO1.getCodigoDireccion());
			direccionesDTO1.setDescripcion(formulario.getDireccion());
			direccionesDTO1.setFechaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
			direccionesDTO1.setCodigoSector(codigoLocal.toString());
			direccionesDTO1.setDistanciaDireccion(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega().toString());
			if(formulario.getDistanciaH()!=null && !formulario.getDistanciaH().equals(ConstantesGenerales.ESTADO_INACTIVO))
				direccionesDTO1.setHoras(formulario.getDistanciaH());
			if(formulario.getDistanciaH()!=null)
				direccionesDTO1.setMinutos(formulario.getDistanciaM());
			direccionesDTO1.setNumeroDireccion(1);
			//agregar el valor de la entrega
			direccionesDTO1.setValorFlete(entregaDetallePedidoDTO.getEntregaPedidoDTO().getNpValorParcialEntrega());
			direccionesDTOCol.add(direccionesDTO1);
			session.setAttribute(EntregaLocalCalendarioAction.DIRECCIONESAUX, direccionesDTOCol);
			
			LogSISPE.getLog().info("se crea un nuevo costo");
			CostoEntregasDTO costoEntregasDTO1 = new CostoEntregasDTO();
			costoEntregasDTO1.setFechaEntrega(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaEntregaCliente());
			costoEntregasDTO1.setCodigoSector(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDireccionEntrega());
			LogSISPE.getLog().info("entregaDTO1.getCostoEntrega() {} ", entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoEntrega());
			//costoEntregasDTO1.setValor(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCostoEntrega());
			costoEntregasDTO1.setNumeroEntregas(1);
			costoEntregasDTO1.setDistancia(entregaDetallePedidoDTO.getEntregaPedidoDTO().getDistanciaEntrega());
			costoEntregasDTO1.setDescripcion(formulario.getDireccion());
			//agregamos la entrega relacionada al costo
			costoEntregasDTO1.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
			costoEntregasDTO1.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			costoEntregaDomicilio(request, session, formulario, beanSession,costoEntregasDTO1);
			costoEntregasDTOCol.add(costoEntregasDTO1);
			session.setAttribute(EntregaLocalCalendarioAction.COSTOENTREGAAUX, costoEntregasDTOCol);
		}catch (Exception ex){
			LogSISPE.getLog().info("Error al crear la direcion costo {}",ex);
		}
	}
	
	
	/**
	 * @param request
	 * @param session
	 * @param formulario
	 * @param beanSession
	 * @param costoEntregasDTO
	 * @throws SISPEException
	 * @throws Exception
	 */
	public static void costoEntregaDomicilio(HttpServletRequest request, HttpSession session, CotizarReservarForm formulario,
			BeanSession beanSession, CostoEntregasDTO costoEntregasDTO)	throws SISPEException, Exception {
		
		EntregaLocalCalendarioUtil.asignacionValoresFormulario(session, formulario);
		Collection<CostoEntregasDTO> colCostoEntrega = null;
		
		Collection<DetallePedidoDTO> detallePedidoDTOCLoneCol = (Collection<DetallePedidoDTO>)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		if (!CollectionUtils.isEmpty(detallePedidoDTOCLoneCol)){
//				String tipoEntregaSesion= (String)session.getAttribute(CotizarReservarAction.TIPO_ENTREGA);
			
//				if(!tipoEntregaSesion.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.local"))){
			
			if (formulario.getOpStock()!=null && formulario.getOpLugarEntrega()!=null 
				&& formulario.getOpLugarEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.contextoEntrega.domicilio")) 
					&&  formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))){
				
				Double totalEntregaSeleccionada = 0d;
				Double totalEntregaParcialSeleccionada = 0D;//solo se llena cuando son entregas parciales
				Double distancia = 0d;
				
				colCostoEntrega = new ArrayList<CostoEntregasDTO>();
				
				
				for(DetallePedidoDTO detallePedidoDTO: detallePedidoDTOCLoneCol){
					
					LogSISPE.getLog().info("getCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue());
					LogSISPE.getLog().info("getNpCantidadEstado {}",detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue());
					
					totalEntregaParcialSeleccionada = UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO);	
					LogSISPE.getLog().info("Valor entrega parcial {} ", totalEntregaParcialSeleccionada);
					
					totalEntregaSeleccionada += totalEntregaParcialSeleccionada;
					
					//Calculo el costo
					//Si la distancia fue ingresada en tiempo
					if(formulario.getUnidadTiempo().equals("H")){
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.factorConversionEntregasDomicilio", request);
						int prametroConversionAKilometros=(new Integer(parametroDTO.getValorParametro())).intValue();
						//transformo la distancia de tiempo a kilometros
						long hora = (Long.valueOf(formulario.getDistanciaH())).longValue()*60;
						long minutos = (Long.valueOf(formulario.getDistanciaM())).longValue();
						double totalMinutos = hora + minutos;
						distancia = totalMinutos * prametroConversionAKilometros;
					}
					//Si la distancia fue ingresada en kilometros
					else{
						distancia = Double.valueOf(formulario.getDistancia());
					}
				}
				
				ParametroDTO paramMontoMinEntregaDomicioCD = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioCD", request);
				Double montoMinEntregaDomicioCD = Double.valueOf(paramMontoMinEntregaDomicioCD.getValorParametro());
				
				
				//entregas totales
				if(formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.total"))
						&& totalEntregaSeleccionada >= 0 && totalEntregaSeleccionada< montoMinEntregaDomicioCD){
					LogSISPE.getLog().info("No cumple la condicion que para las entregas total que se pide al CD monto > {} ", montoMinEntregaDomicioCD);
					
					costoEntregasDTO.setValor(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(distancia, request, totalEntregaSeleccionada,
									session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
					colCostoEntrega.add(costoEntregasDTO);
					
					formulario.setCostoEntregaDomicilio(EntregaLocalCalendarioUtil.calcularCostoFlete(request, colCostoEntrega, formulario));
				}
				//entregas parciales
				else if(beanSession.getPaginaTabPopUp().getTabSeleccionado()!=0){
						if (formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))
							&& totalEntregaParcialSeleccionada>0 && totalEntregaParcialSeleccionada < montoMinEntregaDomicioCD) {
						
						LogSISPE.getLog().info("No cumple la condicion que para las entregas parcial que se pide al CD monto > {} ", montoMinEntregaDomicioCD);
						
						costoEntregasDTO.setValor(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(distancia, request, totalEntregaSeleccionada,
										session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
						colCostoEntrega.add(costoEntregasDTO);
						
						formulario.setCostoEntregaDomicilio(EntregaLocalCalendarioUtil.calcularCostoFlete(request, colCostoEntrega, formulario));
						
					}else if (formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))
							&& totalEntregaParcialSeleccionada==0 && totalEntregaParcialSeleccionada < montoMinEntregaDomicioCD ) {
						
						LogSISPE.getLog().info("No cumple la condicion que para las entregas parcial que se pide al CD monto > {} ", montoMinEntregaDomicioCD);
						
						costoEntregasDTO.setValor(Double.valueOf(WebSISPEUtil.costoEntregaDistancia(distancia, request, totalEntregaSeleccionada,
										session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))));
						colCostoEntrega.add(costoEntregasDTO);
						
						formulario.setCostoEntregaDomicilio(EntregaLocalCalendarioUtil.calcularCostoFlete(request, colCostoEntrega, formulario));
						
					}else{
						
						costoEntregasDTO.setValor(0D);
						colCostoEntrega.add(costoEntregasDTO);
						
						formulario.setCostoEntregaDomicilio(0D);
					}
				}else{
					
					costoEntregasDTO.setValor(0D);
					colCostoEntrega.add(costoEntregasDTO);
					
					formulario.setCostoEntregaDomicilio(0D);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param formulario
	 * @param dia
	 * @param session
	 * @param seleccionado
	 * @param errors
	 * @param request
	 * @throws Exception
	 */
	public static void seleccionDia(CotizarReservarForm formulario, int dia,HttpSession session,ActionMessages errors, HttpServletRequest request) throws Exception{
		LogSISPE.getLog().info("entra a seleccionar los dias: " + dia);
		Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
		//jmena bandera para saber si configura para un(cal. bodega o un cal. local)
		boolean flag = false;
		boolean calBodega = false;
		//dia seleccionado
		int diaSeleccionado=0;
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[dia];
		LogSISPE.getLog().info("seleccionado: {}" , calendarioDiaLocalDTO.getNpEsSeleccionado());
		LogSISPE.getLog().info("fechaCalendario: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
		//fecha minima
		Date fechaMinima=(Date)session.getAttribute(EntregaLocalCalendarioAction.FECHAMINIMA);
		/*****************************************************************************************/
		//fecha maxima
		Date fechaMaxima=(Date)session.getAttribute(EntregaLocalCalendarioAction.FECHAMAXIMA);
		/*****************************************************************************************/
		//Diferencia entre fecha minima y fecha seleccionada
		long diferenciaFechasMinima= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaMinima),ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
		//Diferencia entre fecha de entrega y fecha seleccionada
		long diferenciaFechasEntrega= ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()),ConverterUtil.parseDateToString(fechaMaxima));
		LogSISPE.getLog().info("diferenciaFechasMinima {}" , diferenciaFechasMinima);
		LogSISPE.getLog().info("diferenciaFechasEntrega {}" , diferenciaFechasEntrega);
		
		VistaLocalDTO vistaLocalDestinoDTO=(VistaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.VISTALOCALDESTINO);
		
		
		if(vistaLocalDestinoDTO != null && (vistaLocalDestinoDTO.getId().getCodigoLocal().
				equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"))) || vistaLocalDestinoDTO.getId().getCodigoLocal().
				equals(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.bodega.codigoBodegaTransito"))){
			if(formulario.getOpElaCanEsp() != null){
				if((formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_MI_LOCAL) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
				(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_OTRO_LOCAL) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
				(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
				(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))){
					calBodega = true;
					Date fechaMinEnt = ConverterUtil.parseStringToDate(formulario.getBuscaFecha());
					//Verifico si la fecha eccionada en el cal es mayor o igual a la de entrega.
					if(calendarioDiaLocalDTO.getId().getFechaCalendarioDia().getTime() >= fechaMinEnt.getTime()){
						formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
						session.setAttribute(EntregaLocalCalendarioAction.FECHAENTREGACLIENTE,formulario.getFechaEntregaCliente());
						//AYUDA
						if((formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))))||
								(formulario.getOpLugarEntrega().equals(CONTEXTO_ENTREGA_DOMICIO) && (formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))))){
							session.setAttribute(EntregaLocalCalendarioAction.HABILITARDIRECCION, "ok");
							session.setAttribute(EntregaLocalCalendarioAction.COMBOSELECCIONCIUDAD, "ok");
							session.setAttribute(EntregaLocalCalendarioAction.FECHAENTREGACLIENTE,formulario.getFechaEntregaCliente());
							//formulario.setSeleccionCiudad("");
							if(session.getAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL)==null){
								cargaCiudades(request, errors);
							}
						}
						
					}else{
						errors.add("fechaSeleccionada", new ActionMessage("errors.fechaSeleccionadaMinimaCalBod"));
						session.removeAttribute(EntregaLocalCalendarioAction.HABILITARDIRECCION);
						//session.removeAttribute(PASO);
						session.removeAttribute("siDireccion");
						formulario.setFechaEntregaCliente(null);
						flag = true;
					}
				}
			}
		}
		
		//Compara si la fecha elegida es mayor o igual a la fecha de busqueda
		if((diferenciaFechasMinima<0.0 || diferenciaFechasEntrega<0.0) && !flag && !calBodega){
			LogSISPE.getLog().info("error de fechas");
			if(diferenciaFechasMinima<0.0)
				errors.add("fechaMinima", new ActionMessage("errors.fechaSeleccionadaMinima"));
			if(diferenciaFechasEntrega<0.0)
				errors.add("fechaEntrega", new ActionMessage("errors.fechaSeleccionadaEntrega",ConverterUtil.parseDateToString(fechaMaxima)));
		}else if(!flag){
			LogSISPE.getLog().info("va a seleccionar la fecha");
			//jmena verifico de que tipo es
			if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)!=null && session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO) instanceof Integer){
				//diaSeleccionado=((Integer)session.getAttribute(DIASELECCIONADO)).intValue();Aqui
				diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)));
				//Recupero el dia seleccionado anteriormente
				CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
				calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
			}else if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)!=null && session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO) instanceof String){
				diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX)));
				//Recupero el dia seleccionado anteriormente
				CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
				calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
				session.removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO);
				session.removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADOAUX);
			}
			//Cargo los dias a los que les debo modifcar la cantidad acumulada
			EntregaLocalCalendarioUtil.cargaDiasModificarCA(calendarioDiaLocalDTO.getId().getFechaCalendarioDia(), ConverterUtil.parseStringToDate(formulario.getFechaEntregaCliente()), formulario, request, dia);
			session.setAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO, dia);
			session.setAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL,calendarioDiaLocalDTO);
			calendarioDiaLocalDTO.setNpEsSeleccionado(Boolean.TRUE);
			formulario.setFechaDespacho(DateManager.getYMDDateFormat().format(calendarioDiaLocalDTO.getId().getFechaCalendarioDia()));
			LogSISPE.getLog().info("dia seleccionado: {}" , calendarioDiaLocalDTO.getId().getFechaCalendarioDia().toString());
			//session.setAttribute(CALENDARIODIALOCAL,calendarioDiaLocalDTO);
			formulario.setCalendarioDiaLocal(calendarioDiaLocalDTOObj);
			LogSISPE.getLog().info("habilitarDireccion: {}" , session.getAttribute(EntregaLocalCalendarioAction.HABILITARDIRECCION));
			if(session.getAttribute(EntregaLocalCalendarioAction.HABILITARDIRECCION)!=null){
				//AYUDA
				session.setAttribute(EntregaLocalCalendarioAction.MENSAJEPASOS, MessagesWebSISPE.getString("ayuda.mensajePasoConEnt3"));
				//session.setAttribute(PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				if(( (formulario.getOpTipoEntrega()!=null && formulario.getOpTipoEntrega().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial"))) || (formulario.getOpStock()!=null && formulario.getOpStock().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))))  ){
					session.setAttribute(EntregaLocalCalendarioAction.PASO, MessagesWebSISPE.getString("ayuda.paso4"));
				}
				else{
					session.setAttribute(EntregaLocalCalendarioAction.PASO, MessagesWebSISPE.getString("ayuda.paso3"));
				}
				session.setAttribute("siDireccion", "ok");
			}
		}
	}
	
	/**
	 * Funci&oacute;n que carga las ciudades para las entregas a domicilio desde el CD.
	 * @param request
	 * @param errors
	 */
	public static void cargaCiudades(HttpServletRequest request,ActionMessages errors){
		try{
			
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			//se obtienen las ciudades recomendadas desde un par\u00E1metro
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.ciudadesRecomendadasEntregasDomicilio", request);
			String[] ciudadesRecomendadas = parametroDTO.getValorParametro() != null ? parametroDTO.getValorParametro().split(",") : null;
			VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = new VistaEstablecimientoCiudadLocalDTO();
			vistaEstablecimientoCiudadLocalDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			vistaEstablecimientoCiudadLocalDTO.setNpObtenerCiudadesSinLocales("ok");
			Collection<VistaEstablecimientoCiudadLocalDTO> vistaEstablecimientoCiudadLocalDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaEstablecimientoCiudadLocalSector(vistaEstablecimientoCiudadLocalDTO);
			LogSISPE.getLog().info("numero de ciudades: {}" , vistaEstablecimientoCiudadLocalDTOCol.size());
			
			Collection<VistaEstablecimientoCiudadLocalDTO> ciudadesEliminar = new ArrayList<VistaEstablecimientoCiudadLocalDTO>();
			boolean mostrarCalendarioHoras = request.getSession().getAttribute(EntregaLocalCalendarioAction.MOSTRAR_CALENDARIO_BODEGA_POR_HORAS) != null ? true : false;
			
			//Busco las ciudades recomendadas
			for(VistaEstablecimientoCiudadLocalDTO tituloCargaCiudad: vistaEstablecimientoCiudadLocalDTOCol){
				
				if(mostrarCalendarioHoras && tituloCargaCiudad.getNombreCiudad().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.nombre.otras.ciudades"))){
					ciudadesEliminar.add(tituloCargaCiudad);
				}
				
				for(VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudad: (Collection<VistaEstablecimientoCiudadLocalDTO>)tituloCargaCiudad.getVistaLocales()){
					
					vistaEstablecimientoCiudad.setNpCiudadRecomendada(null);
					
					if(ciudadesRecomendadas != null){
					
						for(int i=0;i<ciudadesRecomendadas.length;i++){
						
							if(vistaEstablecimientoCiudad.getId().getCodigoCiudad().equals(ciudadesRecomendadas[i])){
								vistaEstablecimientoCiudad.setNpCiudadRecomendada(estadoActivo);
								LogSISPE.getLog().info("{} es ciudad recomendada", vistaEstablecimientoCiudad.getId().getCodigoCiudad());
								break;
							}
						}
					}
				}
			}
			if(mostrarCalendarioHoras){
				vistaEstablecimientoCiudadLocalDTOCol.removeAll(ciudadesEliminar);
			}
			request.getSession().setAttribute(EntregaLocalCalendarioAction.VISTAESTABLECIMIENTOCIUDADLOCAL, vistaEstablecimientoCiudadLocalDTOCol);
			LogSISPE.getLog().info("ciudades: {}", vistaEstablecimientoCiudadLocalDTOCol.size());
		}catch(Exception e){
			errors.add("ciudades",new ActionMessage("errors.cargarCiudades",e.getStackTrace()));
		}
	}
	
	/**
	 * M&eacute;todo que asigna los valores seleccionados en pantalla de configuraci&oacute;n
	 * de entregas las variables del formulario.
	 * @param session
	 * @param formulario
	 */
	public static void asignacionValoresFormulario(HttpSession session,CotizarReservarForm formulario) {
		
		if((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA)!=null && formulario.getOpLugarEntrega()==null){
			formulario.setOpLugarEntrega((String) session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA));
		}
		
		if((String)session.getAttribute((EntregaLocalCalendarioAction.OPCIONTIPOENTREGA))!=null && formulario.getOpTipoEntrega()==null){
			formulario.setOpTipoEntrega((String) session.getAttribute(EntregaLocalCalendarioAction.OPCIONTIPOENTREGA));
		}
		
		if((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK)!=null && formulario.getOpStock()==null){
			formulario.setOpStock((String) session.getAttribute(EntregaLocalCalendarioAction.OPCIONSTOCK));
		}
		if((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES)!=null && formulario.getOpElaCanEsp()==null){
			formulario.setOpElaCanEsp((String)session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES));
		}
	}
	
	/**
	 * Obtener fecha minima de entrega
	 * @param request
	 * @param errors
	 * @param session
	 * @throws Exception
	 */
	public static void obtenerFechaMinimaEntrega(HttpServletRequest request,
			ActionErrors errors,CotizarReservarForm formulario) throws Exception {
		
		HttpSession session= request.getSession();
		String fechaMinima=session.getAttribute(CotizarReservarAction.FECHA_MIN_ENTREGA_CD_RES).toString();
		LogSISPE.getLog().info("fechaMinima: {}" , fechaMinima);
		LogSISPE.getLog().info("fechaEntrega: {}" , formulario.fechaEntrega);
		if(session.getAttribute(CotizarReservarAction.FECHA_ENTREGA)==null){
			if(ConverterUtil.returnDateDiff(fechaMinima,formulario.fechaEntrega) < 0.0 && 
					AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.disminucionFechaMinimaEntrega"))==null){
				errors.add("fechaMinima",new ActionMessage("errors.fechaMinima",fechaMinima));
				formulario.fechaEntrega = fechaMinima;
			}
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param colCostoEntrega
	 * @param formulario
	 * @return
	 * @throws Exception
	 */
	public static Double calcularCostoFlete(HttpServletRequest request, 
			Collection<CostoEntregasDTO> colCostoEntrega, CotizarReservarForm formulario)throws Exception{
		
		LogSISPE.getLog().info(" -- actualizarCostoTotalPedido -- ");
		
		Double porcentajeCalculoCostoFlete = (Double)request.getSession().getAttribute(CotizarReservarAction.PORCENTAJE_CALCULO_FLETE);
		
		Double valorFlete = 0D;
			
		if (!CollectionUtils.isEmpty(colCostoEntrega) && porcentajeCalculoCostoFlete!=null){
			
			Double porcentajeValorTotal = formulario.getSubTotal() * (porcentajeCalculoCostoFlete/100);
			Double porcentajeValorTotalRound = Util.roundDoubleMath(porcentajeValorTotal,ConstantesGenerales.NUMERO_DECIMALES);
			
			LogSISPE.getLog().info("Porcentaje 2% del total pedido: {}", porcentajeValorTotalRound);
			
			for(CostoEntregasDTO costoEntregaDTO : colCostoEntrega){
				LogSISPE.getLog().info("costo entrega: {}", costoEntregaDTO.getValor());
				
				if(costoEntregaDTO.getValor() > porcentajeValorTotalRound.doubleValue()){
					double diferenciaCostoEntrega = costoEntregaDTO.getValor() - porcentajeValorTotalRound.doubleValue();
//					this.costoFlete = Double.valueOf(diferenciaCostoEntrega);
					valorFlete += Double.valueOf(diferenciaCostoEntrega);
//					costoEntregaDTO.setValorFlete(valorFlete);
//					this.total = this.costoFlete + this.subTotal;
				}else{
//					this.costoFlete = 0D;
					valorFlete += 0D;
//					this.total = this.subTotal;
				}
			}
		}
		
		return valorFlete;
	}
	
	public static void asignarResponsablesLocal(EntregaDetallePedidoDTO entregaDetallePedidoDTO, HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, EstructuraResponsable estructuraRes, String codClaRecetasNuevas, String descripcionBodega, String codClaCanastos, String clasificacionCodPerecibles, Integer codigoBodega,AreaTrabajoDTO areaTrabajoDTOPro){
		if(	entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.local")))){
			
//			Integer codigo = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo();
			Integer codigo = Integer.parseInt(AutorizacionesUtil.asignarAreaTrabajoAutorizacion(request));
			
			AreaTrabajoDTO areaTrabajoDTO = new AreaTrabajoDTO();
			areaTrabajoDTO.getId().setCodigoAreaTrabajo(codigo);
			areaTrabajoDTO.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());									
			ArrayList<AreaTrabajoDTO> areaTrabajoDTOCol = (ArrayList<AreaTrabajoDTO>)SISPEFactory.getDataService().findObjects(areaTrabajoDTO);	
			
			estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
			estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.responsableEntrega.local") + " " + areaTrabajoDTOCol.get(0).getId().getCodigoAreaTrabajo() + " - " + areaTrabajoDTOCol.get(0).getNombreAreaTrabajo());//RESPONSABLE_LOCAL
			estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.responsableEntrega.local") + " " +  areaTrabajoDTOCol.get(0).getId().getCodigoAreaTrabajo() + " - " + areaTrabajoDTOCol.get(0).getNombreAreaTrabajo());//RESPONSABLE_LOCAL
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
			entregaDetallePedidoDTO.setCodAreTraResPro(codigo);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.Local")));
			entregaDetallePedidoDTO.setCodAreTraResDes(codigo);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.Local")));
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
				(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
						entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp() != null &&
								entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))) {
			
			estructuraRes.setResponsablePedido(RESPONSABLE_BODEGA_CANASTOS);
			estructuraRes.setResponsableProduccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
			estructuraRes.setResponsableDespacho(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega") + ". " + areaTrabajoDTOPro.getNombreAreaTrabajo());//RESPONSABLE_BODEGA_CANASTOS
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.BodegaCanastos")));
			entregaDetallePedidoDTO.setCodAreTraResPro(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
			entregaDetallePedidoDTO.setCodAreTraResDes(areaTrabajoDTOPro.getId().getCodigoAreaTrabajo());//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() != null) &&
				(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
						entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))) && entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp() != null &&
								entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))) {
			estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
			estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_CANASTOS
			estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_CANASTOS
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
			entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaCanastos")));
			entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaCanastos")));
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}else if((detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaCanastos)) &&
				(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
						entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))) {
			estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
			estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
			estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
			entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaAbastos")));
			entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));								
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}else if( clasificacionCodPerecibles.contains(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion()) &&
				(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
						entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega"))))) {
			estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
			estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
			estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_PERECIBLES
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
			entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaPerecibles")));
			entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaPerecibles")));
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}else if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))) || 
				entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().equals(Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
			estructuraRes.setResponsablePedido(RESPONSABLE_LOCAL);
			estructuraRes.setResponsableProduccion(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
			estructuraRes.setResponsableDespacho(descripcionBodega);//RESPONSABLE_BODEGA_ABASTOS
			estructuraRes.setResponsableEntrega(RESPONSABLE_LOCAL);
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResPed(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsablePedido.Local")));
			entregaDetallePedidoDTO.setCodAreTraResPro(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableProduccion.BodegaAbastos")));
			entregaDetallePedidoDTO.setCodAreTraResDes(codigoBodega);//(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableDespacho.BodegaAbastos")));								
			entregaDetallePedidoDTO.getEntregaPedidoDTO().setCodConResEnt(Integer.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.responsableEntrega.Local")));
			
		}
	}
	
	
	/**
	 * Se descuenta del calendarioDiaLocal la configuracion seleccionada de las entregas de mi local/otrolocal desde el CD
	 * @param request
	 */
	public static void descontarBultosDiaSeleccionado(HttpServletRequest request){

		HttpSession session = request.getSession();
		
		String stockBodega = (String) session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA);

		//se valida que la entrega actual sea al local desde el CD
		if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA) != null 
				&& (session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(CONTEXTO_ENTREGA_MI_LOCAL) 
						|| session.getAttribute(EntregaLocalCalendarioAction.OPCIONLUGARENTREGA).equals(CONTEXTO_ENTREGA_OTRO_LOCAL))
				
				&& (StringUtils.isNotEmpty(stockBodega) && (stockBodega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")) 
			    	||  stockBodega.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))))){
			
			CalendarioDiaLocalDTO calendarioDiaLocalDTO = (CalendarioDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
			int totalBultos = (Integer) session.getAttribute("ec.com.smx.sic.sipse.totalBultos");
			
			
			if(totalBultos > 0 && calendarioDiaLocalDTO != null){
				calendarioDiaLocalDTO.setCantidadDisponible(calendarioDiaLocalDTO.getCantidadDisponible() - totalBultos);
				calendarioDiaLocalDTO.setCantidadAcumulada(calendarioDiaLocalDTO.getCantidadAcumulada() + totalBultos);
			}
		}
	}
	
	
	/**
	 * Verifica si el detalle tiene entregas configuradas a domicilio desde el CD y esten en estado entregado
	 * @param detallePedidoDTO
	 * @return true si cumple la condici\u00F3n, false caso contrario
	 */
	public static boolean existenEntregasDomicilioCDEntregadas(DetallePedidoDTO detallePedidoDTO){
		
		//se verifica si el pedido tiene entregas a domicilio desde el CD y si se encuentran entregadas
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			
			//se recorren las entregas
			for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
				
				if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock() != null 
						&& (entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue() 
						== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.parcialBodega").intValue()
							|| entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue() 
							== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega").intValue())){
					
					if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() 
							== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.contextoEntrega.domicilio")
							&& entregaDetallePedidoDTO.getFechaRegistroEntregaCliente() != null){
						LogSISPE.getLog().info("El detalle actual: "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" tiene entregas " +
								"a domicilio despachadas");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * retorna la cantidad de entregas configuradas a domicilio desde el cd y que ya ha sido despachada
	 * @param detallePedidoDTO
	 * @return 
	 */
	public static int obtenerCantidadDespachada(DetallePedidoDTO detallePedidoDTO){
		
		Integer cantidadDespachada = 0;
		
		//se verifica si el pedido tiene entregas a domicilio desde el CD y si se encuentran entregadas
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			
			//se recorren las entregas
			for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
				
				//si la entrega ya fue despachada
				if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock() != null 
						&& (entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue() 
						== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.parcialBodega").intValue()
							|| entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock().intValue() 
							== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega").intValue())){
					
					if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoContextoEntrega().intValue() 
							== MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.contextoEntrega.domicilio")
							&& entregaDetallePedidoDTO.getFechaRegistroEntregaCliente() != null){
						
						cantidadDespachada += entregaDetallePedidoDTO.getCantidadEntrega().intValue();
					}
				}
			}
		}
		return cantidadDespachada;
	}
	
	/**
	 * Verifica si las entregas configuradas ya pasaron de la fecha minima parametrizada para el despacho y entrega
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static boolean verificarEntregasProximoDespacho(HttpServletRequest request) throws Exception{
		
		LogSISPE.getLog().info("validando si las fechas de entregas estan proximas a despachar");
		List<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		String accionActual = (String) request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		
		boolean fechaDespachoVencida = false;
		
		if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")) && CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
			
			//se obtiene el valor del parametro de la fecha minima 
			ParametroDTO parametroFechaDespacho = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasObtenerFechaMinimaEntregaResponsableLocal", request);
			ParametroDTO parametroCanastoEspecialDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diazARestarAFechaDespachoAvisoProduccion", request);
			ParametroDTO parametroDiasAntesParaDespachoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.numeroDiasAntesDeLaFechaEntrega", request);
			
			for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
			 
				if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol()) && parametroFechaDespacho != null){
					
					//se recorren las entregas para verificar de donde se tomara la mercaderia y la fecha de despacho
					for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
						
						int numeroDias = Integer.parseInt(parametroFechaDespacho.getValorParametro());
						
						//para el caso de un canasto especial se suman los dias necesarios para producir los canastos
						if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
								&& parametroCanastoEspecialDTO != null && StringUtils.isNotEmpty(parametroCanastoEspecialDTO.getValorParametro())
								&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(ConstantesGenerales.ENTIDAD_RESPONSABLE_BODEGA)){
							
							numeroDias = Integer.parseInt(parametroCanastoEspecialDTO.getValorParametro());
							int nDiasAntes = Integer.parseInt(parametroDiasAntesParaDespachoDTO.getValorParametro());
							numeroDias -= nDiasAntes;
							
							LogSISPE.getLog().info("por ser canasto especial "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" se suman "+numeroDias+" dias");
						}
						
						Calendar fechaMinima = Calendar.getInstance();
						fechaMinima.add(Calendar.DAY_OF_YEAR, numeroDias);
						
						//si la fecha de despacho es el domingo, se resta un dia
						if(EntregaLocalCalendarioAction.obtenerDia(fechaMinima).equals(MessagesWebSISPE.getString("constante.dia.semana"))){
							fechaMinima.add(Calendar.DAY_OF_YEAR, -1);
							LogSISPE.getLog().info("se resta un dia porque no se puede despachar en domingo");
						}	
						
						Timestamp fechaMinimaDespacho = Timestamp.valueOf(UtilesSISPE.getYMDDateFormat().format(fechaMinima.getTime())+" 00:00:00");
						Integer codigoObtenerStock = entregaDetallePedidoDTO.getEntregaPedidoDTO().getCodigoObtenerStock();
						
						//se validan las entregas con stock del CD
						if(codigoObtenerStock != null 
								&& (codigoObtenerStock.intValue() == MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.parcialBodega").intValue()
									|| codigoObtenerStock.intValue() == MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.stock.totalBodega").intValue())){
							
							LogSISPE.getLog().info("fecha minima despacho: "+fechaMinimaDespacho+" fechaDespachoEntrega: "+entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega());
							
							//se valida la fecha
							if(entregaDetallePedidoDTO.getEntregaPedidoDTO().getFechaDespachoBodega().before(fechaMinimaDespacho)){
								
								//se verifica si existen autorizaciones de disminucion de fecha minima
								if(!AutorizacionesUtil.obtenerAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_DISMINUIR_FECHA_MIN_ENTREGA)){
									request.getSession().setAttribute(FECHA_MINIMA_DESPACHO,  formatoFecha.format(fechaMinimaDespacho));
									fechaDespachoVencida = true;
									entregaDetallePedidoDTO.getEntregaPedidoDTO().setNpFechaDespachoVencida(fechaDespachoVencida);
								}else{
									LogSISPE.getLog().info("El pedido cuenta con una autorizacion para disminuir la fecha minima de entrega");
								}
							}
						}
					}
				}
			}
		}
		return fechaDespachoVencida;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	//Metodos para procesar las entregas por numero de entredas y no por bultos/camiones//
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param request
	 * @param calendarioHoraLocalDTO
	 * @param formulario
	 * @param errors
	 * @param warnings
	 * @param info
	 * @throws SISPEException
	 * @author ionofre
	 */
	public static void verificarDisponibilidadEntregasHora(HttpServletRequest request, CalendarioHoraLocalDTO calendarioHoraLocalDTO, CotizarReservarForm formulario, ActionMessages errors, ActionMessages warnings, ActionMessages info) throws SISPEException{
		
		LogSISPE.getLog().info("ingresa al metodo verificarDisponibilidadEntregasHora");
		
		request.getSession().removeAttribute(EntregaLocalCalendarioAction.CALENDARIO_HORA_LOCAL_SELECCIONADO);
		
		//si seleccionaron ciudad de entrega realiza la validacion
		if(formulario.getSeleccionCiudad() != null && !formulario.getSeleccionCiudad().equals("")){
			
			Boolean validarDisponibilidadEntregas = Boolean.TRUE;
			
			//si hay zonas para esa ciudad
			if(formulario.getSelecionCiudadZonaEntrega().isEmpty()){
				//se agrega el error de sector requerido
				warnings.add("campoCiudadRequerido",new ActionMessage("warnings.campo.zona.ciudad.requerido"," para poder elegir la fecha y hora"));	
				validarDisponibilidadEntregas = Boolean.FALSE;
			}
			
			if(validarDisponibilidadEntregas){
				//si la cantida de entregas de esa hora alcanza
				if(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) >= 1){
					request.getSession().setAttribute(EntregaLocalCalendarioAction.CALENDARIO_HORA_LOCAL_SELECCIONADO, calendarioHoraLocalDTO);
					info.add("seleccionFechaHoraOk" , new ActionMessage("info.seleccion.hora.fecha.entrega"));
				}
				//se agrega el error de capacidad de camiones
				else{
					errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));			
				}						
			}
		} else {
			warnings.add("campoCiudadRequerido",new ActionMessage("warnings.campo.ciudad.requerido"));
		}
						
	}
	
	/**
	 * Calcula la cantidad de entregas disponibles por dia-hora
	 * @param formulario
	 * @param fecha
	 * @param localID
	 * @param request
	 * @return
	 * @author ionofre
	 */
	public static Collection<CalendarioHoraLocalDTO> obtenerEntregasDisponiblesFecha(CotizarReservarForm formulario,  Date fecha, LocalID localID, HttpServletRequest request){
		try{	
			LogSISPE.getLog().info("ingresa el metodo obtenerEntregasDisponiblesFecha ");
			//se arma la consulta para ese dia
			CalendarioHoraLocalDTO calendarioHoraConsulta = new CalendarioHoraLocalDTO();
			calendarioHoraConsulta.getId().setCodigoCompania(localID.getCodigoCompania());
			calendarioHoraConsulta.getId().setCodigoLocal(localID.getCodigoLocal());
			calendarioHoraConsulta.getId().setFechaCalendarioDia(new java.sql.Date(fecha.getTime()));	
			calendarioHoraConsulta.setCalendarioHoraCamionLocalCol(new HashSet<CalendarioHoraCamionLocalDTO>());
			
			CalendarioHoraCamionLocalDTO calendarioHoraCamionLocal = new CalendarioHoraCamionLocalDTO();
			calendarioHoraCamionLocal.setTransporteDTO(new TransporteDTO());			
			calendarioHoraCamionLocal.setCalendarioDetalleHoraCamionLocalCol(new HashSet<CalendarioDetalleHoraCamionLocalDTO>());
			calendarioHoraCamionLocal.getCalendarioDetalleHoraCamionLocalCol().add(new CalendarioDetalleHoraCamionLocalDTO());
			
			calendarioHoraConsulta.getCalendarioHoraCamionLocalCol().add(calendarioHoraCamionLocal);
			
			Collection<CalendarioHoraLocalDTO> calendarioHoraLocalDTOCol = SISPEFactory.getDataService().findObjects(calendarioHoraConsulta);			
			
			//si ese dia existen detalles
			if(CollectionUtils.isNotEmpty(calendarioHoraLocalDTOCol)){
				calendarioHoraLocalDTOCol = unirCalendariosHoraLocal(calendarioHoraLocalDTOCol, request);
				
				Integer totalBultos = 0;		
				
				for(CalendarioHoraLocalDTO calendarioHora : calendarioHoraLocalDTOCol){								
					totalBultos = 0;
					
					Collection<CalendarioHoraCamionLocalDTO>  calendarioHoraCamionLocalCol = calendarioHora.getCalendarioHoraCamionLocalCol();
					if(CollectionUtils.isNotEmpty(calendarioHoraCamionLocalCol)){																									
						
						//se recorren los registros encontrados para esa hora
						for(CalendarioHoraCamionLocalDTO transporteHora: calendarioHoraCamionLocalCol)	{
							
							//quiere decir que esta disponible toda la capacidad del camion
							if(CollectionUtils.isEmpty(transporteHora.getCalendarioDetalleHoraCamionLocalCol())){
								totalBultos += transporteHora.getTransporteDTO().getCantidadBultos();
							}
						}					
					}
					calendarioHora.setNpCantidadBultosDisponibles(totalBultos.toString());
				}
			}	
			Set<CalendarioHoraLocalDTO> calendarioHoraLocalDTOColAux = new HashSet<CalendarioHoraLocalDTO>();
			for(CalendarioHoraLocalDTO horaLocal : calendarioHoraLocalDTOCol)
				calendarioHoraLocalDTOColAux.add(horaLocal);
//			return calendarioHoraLocalDTOCol;
			return calendarioHoraLocalDTOColAux;
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener las entregas disponibles ",e);
			return null;
		}
		
	}
	
	/**
	 * Descuenta el numero de entregas por hora
	 * @param request
	 * @param calendarioHoraLocalDTO
	 * @param totalEntregasPedido
	 * @param localId
	 * @param formulario
	 * @param errors
	 * @param warnings
	 * @throws Exception
	 * @author ionofre
	 */
	
	public static void descontarEntregasPorHora(HttpServletRequest request,  CalendarioHoraLocalDTO calendarioHoraLocalDTO, int totalEntregasPedido, LocalID localId, CotizarReservarForm formulario, ActionMessages errors, ActionMessages warnings) throws Exception{
		try{
			LogSISPE.getLog().info("ingresa al metodo descontarEntregasPorHora");
			//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
			//Collection<DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			Collection<DetallePedidoDTO> colDetallePedidoDTO=(Collection<DetallePedidoDTO>)request.getSession().getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
			//Collection calendarioDetalleHoraCamionLocalCol = (HashSet<CalendarioDetalleHoraCamionLocalDTO>)request.getSession().getAttribute(CALENDARIO_DETALLE_HORA_CAMION_LOCAL_MODIFICADO);
			
			Collection<CalendarioHoraLocalDTO> calendarioHoraLocalCol = (Collection<CalendarioHoraLocalDTO>) request.getSession().getAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL);
			if(CollectionUtils.isEmpty(calendarioHoraLocalCol)){
				calendarioHoraLocalCol = new HashSet<CalendarioHoraLocalDTO>();
			}
			
			//se calcula el total de entregas que tiene el pedido
			if(CollectionUtils.isNotEmpty(colDetallePedidoDTO)){	
				
				DetallePedidoDTO detallePedidoDTO =  colDetallePedidoDTO.iterator().next();							
				
				//si la cantida de entregas de esa hora alcanza
				if(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) >= totalEntregasPedido){ //antes preguntaba por el numero de bultos

					String codigoCiudad = null;
					if(formulario.getSeleccionCiudad() != null){
						if(formulario.getSeleccionCiudad().split("/").length > 1){
							codigoCiudad = formulario.getSeleccionCiudad().split("/")[1];
						}
						else{
							codigoCiudad = formulario.getSeleccionCiudad();
						}
					}
					String codigoCiudadSector = null;
					if(formulario.getSelecionCiudadZonaEntrega() != null && !formulario.getSelecionCiudadZonaEntrega().isEmpty())
						codigoCiudadSector = formulario.getSelecionCiudadZonaEntrega();
					
					//se asignan los bultos a los camiones
					Collection<CalendarioHoraCamionLocalDTO> listaCamionesHoraLocalAux = calendarioHoraLocalDTO.getCalendarioHoraCamionLocalCol();
					ColeccionesUtil.sort(listaCamionesHoraLocalAux, ColeccionesUtil.ORDEN_DESC, "transporteDTO.cantidadBultos");
						//se asignan los bultos a los camiones
					int residuo = -1;						
					
					Collection<CalendarioDetalleHoraCamionLocalDTO> calendarioDetalleHoraCamionCol = new HashSet<CalendarioDetalleHoraCamionLocalDTO>();
					ArrayList<CantidadTransporteCapacidad> confCamiones = new ArrayList<CantidadTransporteCapacidad>();
												
					//se procede a asignar el pedido en varios camiones					
					ArrayList<CantidadTransporteCapacidad> cantidadCamionesCol = crearListaCamionesCapacidad(listaCamionesHoraLocalAux, codigoCiudad, codigoCiudadSector);
					ColeccionesUtil.sort(cantidadCamionesCol, ColeccionesUtil.ORDEN_DESC, "transporteDTO.cantidadBultos");
					LogSISPE.getLog().info(" coleccion camiones ordenados {}",cantidadCamionesCol);
																	
					ArrayList<CantidadTransporteCapacidad> camionesEmpleadosCol = new ArrayList<CantidadTransporteCapacidad>();
											
					String entregasUsadas = "";
					
					int residuoTemporal = 0;
					int residuoEntrega = 0;
					residuo = -1;//totalBultosPedido;
					//ahora se procede a asignar el pedido en camiones
					for(int i=0; i<cantidadCamionesCol.size(); i++){
						
						camionesEmpleadosCol = new ArrayList<CantidadTransporteCapacidad>();							
						CantidadTransporteCapacidad nivel1 = new CantidadTransporteCapacidad(cantidadCamionesCol.get(i));
													
						if(nivel1.getCantidadDisponible() >= totalEntregasPedido){
							nivel1.setCantidadUtilizada(totalEntregasPedido);
							residuoEntrega = nivel1.getCantidadDisponible().intValue() - totalEntregasPedido;
							residuoTemporal = 0;													
						}
						else {
							residuoTemporal = totalEntregasPedido-nivel1.getCantidadDisponible();							
							nivel1.setCantidadUtilizada(nivel1.getCantidadDisponible());
							residuoEntrega = 0;
						}
						
						camionesEmpleadosCol.add(nivel1);
						entregasUsadas = "Entrega("+nivel1.getCapacidadCamion()+") :" +nivel1.getCantidadUtilizada() ;
						
						//es la primera vez que asigna una configuracion
						if(residuoTemporal == 0 && residuo == -1){
							residuo = residuoEntrega;
							confCamiones = new ArrayList<CantidadTransporteCapacidad>();
							confCamiones.addAll(camionesEmpleadosCol);
							LogSISPE.getLog().info("configuracion optima {}  residuo entregas {}", entregasUsadas, residuo);
						}
						else if(residuoTemporal == 0 && residuo > residuoEntrega){
							residuo = residuoEntrega;
							confCamiones = new ArrayList<CantidadTransporteCapacidad>();
							confCamiones.addAll(camionesEmpleadosCol);
							LogSISPE.getLog().info("configuracion optima {}  residuo camion {}", entregasUsadas, residuo);
						}
						
						if(residuoTemporal > 0){
							for(int j=0; j<cantidadCamionesCol.size(); j++){
								
								CantidadTransporteCapacidad nivel2 =  new CantidadTransporteCapacidad(cantidadCamionesCol.get(j));
								
								if(j != i){
									//todavia existen bultos sin asignar camiones
									if(residuoTemporal > 0){	
																					
										entregasUsadas += " -- "+nivel2.getTransporteDTO().getNombreTransporte()+"("+nivel2.getCapacidadCamion()+"):" ;
										
										if(residuoTemporal >= nivel2.getCantidadDisponible()){	
											nivel2.setCantidadUtilizada(nivel2.getCantidadDisponible());
											camionesEmpleadosCol.add(nivel2);
											residuoTemporal -= nivel2.getCantidadDisponible();
											entregasUsadas += nivel2.getCantidadDisponible();
											residuoEntrega = 0;
										}
										else{		
											nivel2.setCantidadUtilizada(residuoTemporal);
											camionesEmpleadosCol.add(nivel2);
											residuoEntrega = nivel2.getCantidadDisponible() - residuoTemporal;
											entregasUsadas += residuoTemporal;
											residuoTemporal = 0;
										}
										
										if(residuoTemporal == 0 && residuo > residuoEntrega){
											residuo = residuoEntrega;
											confCamiones = new ArrayList<CantidadTransporteCapacidad>();
											confCamiones.addAll(camionesEmpleadosCol);
											LogSISPE.getLog().info("configuracion optima {}  residuo camion {}", entregasUsadas, residuo);
										}									
									}else 
										j= cantidadCamionesCol.size();										
								}
							}
						}				
					}
						//se construyen los detalles
					if(CollectionUtils.isNotEmpty(confCamiones)){
						
						//la colleccion resultante de los camiones seleccionados 
						 calendarioDetalleHoraCamionCol = new HashSet<CalendarioDetalleHoraCamionLocalDTO>();
						for(CantidadTransporteCapacidad camionActual : confCamiones){
							CalendarioDetalleHoraCamionLocalDTO calDetalleHoraCamion = new CalendarioDetalleHoraCamionLocalDTO();
							
							//id
							calDetalleHoraCamion.getId().setCodigoCompania(detallePedidoDTO.getId().getCodigoCompania());
							
							//relacion con el pedido
							calDetalleHoraCamion.setCodigoAreaTrabajo(detallePedidoDTO.getId().getCodigoAreaTrabajo());
							calDetalleHoraCamion.setCodigoPedido(detallePedidoDTO.getId().getCodigoPedido());
							
							//relacion con calendarioHoraCamionLocal
							calDetalleHoraCamion.setFechaCalendarioDia(new java.sql.Date(calendarioHoraLocalDTO.getId().getFechaCalendarioDia().getTime()));
							calDetalleHoraCamion.setCodigoLocal(localId.getCodigoLocal());										
							calDetalleHoraCamion.setHora(calendarioHoraLocalDTO.getId().getHora());		
							calDetalleHoraCamion.setCodigoTransporte(camionActual.getTransporteDTO().getId().getCodigoTransporte());
							calDetalleHoraCamion.setSecuencialCalHorCamLoc(camionActual.getSecuencialHoraCamion());
							
							calDetalleHoraCamion.setEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
							calDetalleHoraCamion.setCapacidadUtilizada((long)camionActual.getCantidadUtilizada());											
							
							//campos de auditoria
							calDetalleHoraCamion.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
							
							//relacion divisionGeopolitica
							calDetalleHoraCamion.setCodigoSectorEntrega(codigoCiudad);
							calDetalleHoraCamion.setCodigoCiudadSectorEntrega(codigoCiudadSector);
							calendarioDetalleHoraCamionCol.add(calDetalleHoraCamion);	
							
							for(CalendarioHoraCamionLocalDTO calendarioCamionHoraActual : listaCamionesHoraLocalAux){
								if(calendarioCamionHoraActual.getId().getCodigoTransporte().intValue() == camionActual.getTransporteDTO().getId().getCodigoTransporte()
										&& calendarioCamionHoraActual.getId().getSecuencialCalHorCamLoc().intValue() == camionActual.getSecuencialHoraCamion() ){
									if(CollectionUtils.isEmpty(calendarioCamionHoraActual.getCalendarioDetalleHoraCamionLocalCol()))
										calendarioCamionHoraActual.setCalendarioDetalleHoraCamionLocalCol(new HashSet<CalendarioDetalleHoraCamionLocalDTO>());
										
									calendarioCamionHoraActual.getCalendarioDetalleHoraCamionLocalCol().add(calDetalleHoraCamion);
									
									break;
								}
							}
						}
						//se setean los datos en del objeto padre
						calendarioHoraLocalDTO.setCalendarioHoraCamionLocalCol(listaCamionesHoraLocalAux); 
						CalendarioDiaLocalDTO calDiaLocal = new CalendarioDiaLocalDTO();
						calDiaLocal.getId().setCodigoCompania(calendarioHoraLocalDTO.getId().getCodigoCompania());
						calDiaLocal.getId().setCodigoLocal(calendarioHoraLocalDTO.getId().getCodigoLocal());
						calDiaLocal.getId().setFechaCalendarioDia(calendarioHoraLocalDTO.getId().getFechaCalendarioDia());
						
						CalendarioDiaLocalDTO calDiaLocalEncontrado = SISPEFactory.getDataService().findUnique(calDiaLocal);
						if(calDiaLocalEncontrado != null){
							calDiaLocalEncontrado.setCantidadDisponible(calDiaLocalEncontrado.getCantidadDisponible() - totalEntregasPedido);
						}
						calendarioHoraLocalDTO.setCalendarioDiaLocalDTO(calDiaLocalEncontrado);
						calendarioHoraLocalDTO.setNpCantidadBultosDisponibles(""+(Double.parseDouble(calendarioHoraLocalDTO.getNpCantidadBultosDisponibles()) - totalEntregasPedido));
						Boolean campoEncontrado = Boolean.FALSE;
						if(CollectionUtils.isNotEmpty(calendarioHoraLocalCol)){
							//se busca el registro
							for(CalendarioHoraLocalDTO horaLocal : calendarioHoraLocalCol){
								//se comparan los IDs
								if(horaLocal.getId().getCodigoCompania().equals(calendarioHoraLocalDTO.getId().getCodigoCompania())
										&& horaLocal.getId().getCodigoLocal().equals(calendarioHoraLocalDTO.getId().getCodigoLocal().intValue())
										&& horaLocal.getId().getFechaCalendarioDia().toString().equals(calendarioHoraLocalDTO.getId().getFechaCalendarioDia().toString())
										&& horaLocal.getId().getHora().toString().equals(calendarioHoraLocalDTO.getId().getHora().toString())){
									campoEncontrado = Boolean.TRUE;
									horaLocal = calendarioHoraLocalDTO;	
								}
							}
						}
						if(!campoEncontrado){
							//si el campo no existe 
							calendarioHoraLocalCol.add(calendarioHoraLocalDTO);
						}
						request.getSession().setAttribute(CALENDARIO_HORA_LOCAL_PROCESADO, calendarioHoraLocalDTO);
						request.getSession().setAttribute(CAL_HORA_LOCAL_SELECCIONADOS_COL, calendarioHoraLocalCol);
					}
				}
				//se agrega el error de capacidad de camiones
				else{
					errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));			
				}
				
			}			
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al descontar los bultos del pedido ",e);	
			errors.add("capacidadTransportePedido",new ActionMessage("errors.cantidad.camiones.pedido"));
			throw new Exception(e);
		}
		
	}
}
