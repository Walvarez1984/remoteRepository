/*
 * DescuentosUtil.java
 * Creado el 22/07/2013 11:36:20
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.DEFAULT_COMPANY;
import static ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction.CODIGO_TIPO_DESCUENTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corpv2.dto.FuncionarioDTO;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.MarcaArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author bgudino
 *
 */
@SuppressWarnings("unchecked")
public class DescuentosUtil {
	
	//Variables para descuentos
	private static final String CODIGO_GERENTE_COMERCIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoGerenteComercial");
	private static final String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
	public static final String MENSAJE_NO_APLICA_MARCA_PROPIA = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("mostrar.mensaje.no.aplica.descuento.marcaPropia");
	public static final String RESPALDO_DETALLE_PEDIDO_AUTORIZACION = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("respaldo.detalle.pedido");
	public static final String RESPALDO_DESC_SELECCIONADOS = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("respaldo.descuentos.seleccionados");

	
	/**
	 * procesa primero los descuentos variables, y luego si quedan llaves y detalles sin procesar, procesa los descuentos normales
	 * @param request
	 * @param colDetallePedidoDTO
	 * @param llavesDescuentosAplicados
	 * @param colDescuentoEstadoPedidoDTOVariables
	 * @return coleccion de descuentos aplicados
	 * @throws Exception
	 */
	public static Collection<DescuentoEstadoPedidoDTO> procesarDescuentos(HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedidoDTO, 
			Collection<String> llavDesApl, Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables) throws Exception{
		
		Collection<String> llavesDescuentosAplicados = llavDesApl;
		Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
		
		try{
			//sube a sesion variables necesarias para el siguiente metodo
			CotizarReservarAction.buscarDetallesConDctoAutomaticos((ArrayList<DetallePedidoDTO>) colDetallePedidoDTO,  request);
			AutorizacionesUtil.agregarAutorizacionSimilaresDescVar(request, (ArrayList<DetallePedidoDTO>) colDetallePedidoDTO);
			
//			//se valida si puede aplicar o no el descuento MARCA PROPIA
			llavesDescuentosAplicados = DescuentosUtil.validarSiAplicaDescuentoMarcaPropiaEnConsolidacion(request, llavesDescuentosAplicados, new ActionMessages());
			
			//se clasifican los detalles por departamento
			if(!llavesDescuentosAplicados.isEmpty()){
				
				//llamada al m\u00E9todo que quita los descuentos
				SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(colDetallePedidoDTO,null, null);
				//llaves descuento varible
				ArrayList<String> llaveDescuentoActual;
				//detalles pertenencientes a un departamento
				Collection<DetallePedidoDTO> detallesDepActualCol;
				
				//se recorren las llaves de los descuentos
				for(String llaveActual : llavesDescuentosAplicados){
					
					//solo para el caso de descuentos variables
					if(llaveActual.split(SEPARADOR_TOKEN).length > 3 && llaveActual.contains(CODIGO_GERENTE_COMERCIAL)){
						
						LogSISPE.getLog().info("procesando la llave: "+llaveActual);
						llaveDescuentoActual = new ArrayList<String>();
						detallesDepActualCol = new ArrayList<DetallePedidoDTO>();
						
						llaveDescuentoActual.add(llaveActual);
						
						String codigoAutorizador = AutorizacionesUtil.obtenerCodigoAutorizadorDeLlaveDescuento(llaveActual);
						String codigoClasificacionPadre = AutorizacionesUtil.obtenerCodigoClasificacionPadreDeLlaveDescuento(llaveActual);
						
						//codigoClasificacion,idAutorizador
//						Map<String, String> mapClasificacionAutorizador = new HashMap<String, String>();
//						String key;
						String idAutorizador = "";
						//se recorren los detalles para separar lo de cada departamento
						for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
							
							//solo para el caso de descuentos variable
							if(AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detalleActual, 
									ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue())){
								
								if(StringUtils.isEmpty(detalleActual.getNpIdAutorizador())){
									
//									key = AutorizacionesUtil.obtenerCodigoClasificacion(detalleActual);
									
//									if(!mapClasificacionAutorizador.containsKey(key)){
										FuncionarioDTO funcionarioAutorizador = AutorizacionesUtil.obtenerFuncionarioAutorizadorPorClasificacion(AutorizacionesUtil.obtenerCodigoClasificacion
												(detalleActual), AutorizacionesUtil.obtenerTipoMarca(detalleActual),request, AutorizacionesUtil.obtenerCodigoProcesoAutorizarDescVar(request), new ActionMessages());
										if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
											idAutorizador = funcionarioAutorizador.getUsuarioFuncionario();
//											mapClasificacionAutorizador.put(key, idAutorizador);
										}
//									}else{
//										idAutorizador = mapClasificacionAutorizador.get(key);
//									}
									
									if(StringUtils.isNotEmpty(idAutorizador)){
										detalleActual.setNpIdAutorizador(idAutorizador);
									}
								}
								if(StringUtils.isNotEmpty(detalleActual.getNpIdAutorizador()) && detalleActual.getNpIdAutorizador().equals(codigoAutorizador)
								   && codigoClasificacionPadre.equals(AutorizacionesUtil.obtenerCodigoDepartamento(detalleActual)) && !detallesDepActualCol.contains(detalleActual)){
										detallesDepActualCol.add(detalleActual);
										LogSISPE.getLog().info("detalles "+detalleActual.getId().getCodigoArticulo()+"-"+detalleActual.getArticuloDTO().getDescripcionArticulo());
								}
							}
						}
						
						//si existen detalles de ese departamento se aplican los descuentos
						if(CollectionUtils.isNotEmpty(detallesDepActualCol)){
							DescuentoEstadoPedidoDTO descuentoVar = new  DescuentoEstadoPedidoDTO() ;
							String[] vectorLlaveComprador = llaveDescuentoActual.get(0).split(SEPARADOR_TOKEN);
							DescuentoDTO descuentoVariable = new DescuentoDTO();
							Boolean encontroDcto = Boolean.FALSE;
							if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
								for(DescuentoEstadoPedidoDTO descuentoActual : (ArrayList<DescuentoEstadoPedidoDTO>)colDescuentoEstadoPedidoDTOVariables){
									if(descuentoActual.getLlaveDescuento() != null && descuentoActual.getLlaveDescuento().contains(
											SEPARADOR_TOKEN+vectorLlaveComprador[1]+SEPARADOR_TOKEN+vectorLlaveComprador[2]+SEPARADOR_TOKEN+vectorLlaveComprador[3]+SEPARADOR_TOKEN+vectorLlaveComprador[4].substring(vectorLlaveComprador[4].length()-1))){
										descuentoVariable = descuentoActual.getDescuentoDTO();
										descuentoVariable.setPorcentajeDescuento(descuentoActual.getPorcentajeDescuento());
										encontroDcto = Boolean.TRUE;
										descuentoVar = SerializationUtils.clone(descuentoActual);
										break;
									}										
								}
								if(encontroDcto){
									Collection<DescuentoDTO> colDescuentosVarPedido = new ArrayList<DescuentoDTO>();
									colDescuentosVarPedido.add(descuentoVariable);	
									
									//se realiza una validacion de los detalles que tienen descuento por CAJA y MAYORISTA para que no apliquen variables
									for(DetallePedidoDTO  detalleActual : detallesDepActualCol ){
										if(detalleActual.getArticuloDTO().getHabilitadoPrecioMayoreo()){ 
											if(request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA) == null){
												if(CotizacionReservacionUtil.esArticuloActivoParaPrecioMayorista(detalleActual.getId().getCodigoAreaTrabajo(), 
														detalleActual.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado(),
														detalleActual.getEstadoDetallePedidoDTO().getValorMayorista(), 
														detalleActual.getEstadoDetallePedidoDTO().getCantidadEstado(), detalleActual.getArticuloDTO(), request)){
													detalleActual.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
													detalleActual.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
												}
											}
										}else if(detalleActual.getArticuloDTO().getHabilitadoPrecioCaja() 
												&& detalleActual.getEstadoDetallePedidoDTO().getValorCajaEstado().doubleValue() > 0
												&& request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS) == null
												&& CotizacionReservacionUtil.esArticuloActivoParaPrecioCaja((Integer)request.getSession().getAttribute(CODIGO_LOCAL_REFERENCIA), 
													detalleActual.getEstadoDetallePedidoDTO().getValorCajaEstado(),	detalleActual.getArticuloDTO(), request)){
											detalleActual.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
											detalleActual.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
										}
									}
									
									Collection<DescuentoEstadoPedidoDTO> descuentosProcesados = SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(
											detallesDepActualCol, llaveDescuentoActual, colDescuentosVarPedido);
									
									//se crea el descuento con valores cero
									if(descuentosProcesados.size() == 0){
										descuentoVar.setValorPrevioDescuento(0D);
										descuentoVar.setValorMotivoDescuento(0D);
										descuentoVar.setValorDescuento(0D);
										descuentosProcesados.add(descuentoVar);
										CotizacionReservacionUtil.agregarDescripcionDescuentosVariables(descuentosProcesados, request);
									}
									
									colDescuentoEstadoPedidoDTO.addAll(descuentosProcesados);
								}
							}																											
						}
					}
				}
				
				if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTO)){
					//se sube a sesion los descuentos variables				
					request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES, new ArrayList<DescuentoEstadoPedidoDTO>(colDescuentoEstadoPedidoDTO));
				}
				
				//para el caso de descuentos normales
				Collection<DetallePedidoDTO> detallesSinAutorizaciones = new ArrayList<DetallePedidoDTO>();
				
				//se separan los detalles sin autorizaciones
				for(DetallePedidoDTO detalleActual : colDetallePedidoDTO){
					if(!AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detalleActual, 
							ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE)){
						detallesSinAutorizaciones.add(detalleActual);
					}
				}
										 
				 // se separa las llaves sin autorizaciones
				 Collection<String> llavesSinAutorizaciones = new ArrayList<String>();
				 for(String llaveActual : llavesDescuentosAplicados){
					 if(llaveActual.split(SEPARADOR_TOKEN).length <= 3){
						 llavesSinAutorizaciones.add(llaveActual);
					 }
				 }
				
				if(!detallesSinAutorizaciones.isEmpty() && !llavesSinAutorizaciones.isEmpty()){
					colDescuentoEstadoPedidoDTO.addAll(SessionManagerSISPE.getServicioClienteServicio().
							transProcesarDescuentos(detallesSinAutorizaciones, llavesSinAutorizaciones, null));
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al procesar los descuentos variables ",e);
		}
		
		//se valida si debe mostrar el mensaje 
		if(request.getSession().getAttribute(MENSAJE_NO_APLICA_MARCA_PROPIA) != null){
			
			final String codigoTipoDescuentoMarcaPropia = CODIGO_TIPO_DESCUENTO+obtenerCodigoTipoDescuentoMarcaPropia(request);
			Boolean existeLlaveMarcaPropia = CollectionUtils.exists(llavesDescuentosAplicados, new Predicate() {
				public boolean evaluate(Object arg0) {
					String llaveDescuento = (String) arg0;
					return llaveDescuento.contains(codigoTipoDescuentoMarcaPropia);
				}
			});
			if(existeLlaveMarcaPropia){
				request.getSession().removeAttribute(MENSAJE_NO_APLICA_MARCA_PROPIA);
			}
		}
		return colDescuentoEstadoPedidoDTO;
	}
	
	

	
	/**
	 * Verifica si existen detalles que no seran aplicados ningun descuento para aplicar a esos detalles el descuento general
	 * 
	 * @param detallePedidoDTOCol
	 * @param request
	 * @param opDescuentos
	 * @return true si existen detalles sin descuentos, else caso contrario
	 */
	public static Boolean validarSiAplicaDescuentoGeneral(Collection<DetallePedidoDTO> detallePedidoDTOCol, HttpServletRequest request,
			String[] opDescuentos){
		Boolean aplicarDescuentoGeneral = Boolean.FALSE;
		
		//se hace una copia de todos los detalles
		Collection<DetallePedidoDTO> detallesSinDescuentos = new ArrayList<DetallePedidoDTO>(detallePedidoDTOCol);
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
		
			//se quitan los detalles con descuentos de pavos
			detallesSinDescuentos.removeAll(obtenerDetallesConDescuentoPAvos(request, detallePedidoDTOCol, opDescuentos));
			
			//se quitan los detalles que van a aplicar descuentos variables
			detallesSinDescuentos.removeAll(obtenerDetallesConDescuentoVariable(detallesSinDescuentos,	opDescuentos));
			
			//si existen detalles, se habilita el descuento general
			if(detallesSinDescuentos.size() >  0){
				return Boolean.TRUE;
			}
		}
		return aplicarDescuentoGeneral;
	}
	
	
	/**
	 * Retorna los detalles que seran aplicados descuentos automatico de pavos
	 * @param request
	 * @param detallePedidoDTOCol
	 * @param opDescuentos
	 * @return
	 */
	public static Collection<DetallePedidoDTO> obtenerDetallesConDescuentoPAvos(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoDTOCol,
			String[] opDescuentos){
		
		HttpSession session = request.getSession();

		Collection<TipoDescuentoClasificacionDTO> clasificacionesPavos = (Collection<TipoDescuentoClasificacionDTO>) 
				session.getAttribute(CotizarReservarAction.TIPO_DESCUENTO_CLASIFICACION_PAVOS_COL);	
		
		Collection<DetallePedidoDTO> detallesConDescuentoPavosCol = new ArrayList<DetallePedidoDTO>();

		if(CollectionUtils.isNotEmpty(clasificacionesPavos)){						
			//se recorren los detalles
			if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
				for(DetallePedidoDTO detallePedidoActual : detallePedidoDTOCol){
					
					//se verifica la clasificacion de pavos
					for(TipoDescuentoClasificacionDTO clasificacionPavosActual : clasificacionesPavos){
						
						if(!AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detallePedidoActual, 
								ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue())){
							//se verifica si el detalle es de pavos
							if(detallePedidoActual.getArticuloDTO().getCodigoClasificacion().equals(clasificacionPavosActual.getId().getCodigoClasificacion())){
								for(String opDctoActual : opDescuentos){									
									if(opDctoActual != null && opDctoActual.contains(CotizarReservarAction.CODIGO_TIPO_DESCUENTO+
											clasificacionPavosActual.getId().getCodigoTipoDescuento()+SEPARADOR_TOKEN)){
										detallesConDescuentoPavosCol.add(detallePedidoActual);
										break;
									}
								}
							}
						}
					}								
				}						
			}
		}
		return detallesConDescuentoPavosCol;
	}
	
	
	
	/**
	 * Retorna la coleccion de detalles que seran aplicados descuentos variables
	 * @param detallePedidoDTOCol
	 * @param opDescuentos
	 * @return
	 */
	public static Collection<DetallePedidoDTO> obtenerDetallesConDescuentoVariable(Collection<DetallePedidoDTO> detallePedidoDTOCol, String[] opDescuentos){
		
		Collection<DetallePedidoDTO> detallesConDescuentoVariableCol = new ArrayList<DetallePedidoDTO>();

		//se recorren los detalles
		if(CollectionUtils.isNotEmpty(detallePedidoDTOCol) && opDescuentos != null && opDescuentos.length > 0){
			
			String codigoclasificacionPadre;
			//se recorren los descuentos seleccionados
			for(String opDescuento : opDescuentos){
				
				//solo para el caso de descuentos variables
				if(opDescuento.contains(CODIGO_GERENTE_COMERCIAL)){
					
					//se obtiene el codigo de la clasificacion del departamento del descuento  
					codigoclasificacionPadre = AutorizacionesUtil.obtenerCodigoClasificacionPadreDeLlaveDescuento(opDescuento);
					
					for(DetallePedidoDTO detallePedidoActual : detallePedidoDTOCol){
						
						//se verifica si el detalle pertenece a la clasificacion del descuento variable
//						if(codigoclasificacionPadre.equals(detallePedidoActual.getArticuloDTO().getClasificacionDTO().getCodigoClasificacionPadre())){
						if(codigoclasificacionPadre.equals(AutorizacionesUtil.obtenerCodigoDepartamento(detallePedidoActual))){
							detallesConDescuentoVariableCol.add(detallePedidoActual);
						}
					}
				}
			}
		}
		return detallesConDescuentoVariableCol;
	}
	
	/**
	 * Si el pedido no tiene articulos de tipo caja/mayorista con solicitud de descuento variable se inactiva el descuento automatico
	 * @param detallePedidoDTOCol
	 * @param request
	 * @throws Exception
	 */
	public static void validarSiAplicaDescuentoCajasMayorista(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request) throws Exception{
		
		try{
			LogSISPE.getLog().info("se valida si se desactivan las llaves de descuento de cajas y mayorista");
			if(detallePedidoDTO != null && detallePedidoDTO.getEstadoDetallePedidoDTO() != null){
				HttpSession session = request.getSession();
				Boolean articuloMayorista = Boolean.FALSE;
				Boolean articuloCajas = Boolean.FALSE;
					
					//se valida si el articulo es de tipo caja o mayorista
				
				if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()
						&& 	CotizacionReservacionUtil.esArticuloActivoParaPrecioMayorista(detallePedidoDTO.getId().getCodigoAreaTrabajo(), 
							detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado(),
							detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayorista(), 
							detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado(), detallePedidoDTO.getArticuloDTO(), request)){
					articuloMayorista = Boolean.TRUE;
				}else if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioCaja() 
						&& detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado().doubleValue() > 0
						&& session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS) == null
						&& CotizacionReservacionUtil.esArticuloActivoParaPrecioCaja((Integer)session.getAttribute(CODIGO_LOCAL_REFERENCIA), 
							detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado(),	detallePedidoDTO.getArticuloDTO(), request)){
					articuloCajas = Boolean.TRUE;
				}
					
				if(articuloCajas || articuloMayorista){
					if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
						for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : detallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() 
									== ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue() 
									&& autorizacionActual.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_SOLICITADA)){

								//se deshabilita el descuento de mayorista
								if(articuloMayorista){
									session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
									session.removeAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_MAYORISTA);
									LogSISPE.getLog().info("descuento mayorista deshabilitado");
								}
								//se deshabilita el descuento de cajas
								if(articuloCajas){
									session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
									session.removeAttribute(CotizarReservarAction.EXISTEN_DESCUENTOS_CAJAS);
									LogSISPE.getLog().info("descuento cajas deshabilitado");
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al verificar si el pedido aplica o no descuento automatico de caja y mayorista "+e.getMessage());
		}
	}
	
	
	/**
	 * Inicia el descuento automatico MARCA PROPIA y sube a sesion la llave y el opDescuento si el pedido cumple con las condiciones:
	 * - El pedido debe tener minimo $1000 en productos MARCA PROPIA,
	 * - el total de productos MARCA PROPIA debe ser minimo el 50% del total del pedido,
	 * - El descuento MARCA PROPIA es el unico se se aplica descuento sobre descuento.
	 * 
	 * @param request
	 * @return vector con llaves de descuentos aplicados
	 * @throws Exception
	 */
	public static String[] iniciarDescuentoAutomaticoMarcaPropia(HttpServletRequest request, Boolean subirVariableASesion) throws Exception{
		
		HttpSession session = request.getSession();
		Collection<String> llavesProcesadasCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
		
		//se limpia el valor del total de detalles marca propia
		session.removeAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido");
		session.removeAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia");
		
		//se eliminan las llaves de descuento marca propia
		eliminarLlaveMarcaPropia(request, llavesProcesadasCol);
		String[] llaveDescto = (String[])session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
		llaveDescto = CotizacionReservacionUtil.eliminarLlavesRepetidas(llaveDescto);
		LogSISPE.getLog().info("validar si debe o no aplicar el descuento automatico: MARCA PROPIA");

		try{
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
			String codigoTipoDescuentoMarcaPropia = obtenerCodigoTipoDescuentoMarcaPropia(request);
			//se obtiene de sesion el detalle del pedido
			ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			
			//si es pedido consolidado se suma el pedido consolidado para valide correctamente las cantidades
			if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
					&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)
					&& session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null){
				//se obtiene de sesion el pedido consolidado
				detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			}
			
					
			Double valorTotalPedidoSinIva = 0D;
			Double valorDetallesMarcaPropia = 0D;
			Double porcentajeDescuentoMarcaPropia = 1D;
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentaje.pedidos.marca.propia", request);
			if(parametroDTO.getValorParametro() != null){
				porcentajeDescuentoMarcaPropia = Double.valueOf(parametroDTO.getValorParametro());
			}
			
			Double porcentajeCalculado = porcentajeDescuentoMarcaPropia/100;
			
			DescuentoDTO descuentoDTO = new DescuentoDTO();
			descuentoDTO.getId().setCodigoCompania(DEFAULT_COMPANY);
			TipoDescuentoDTO tipoDescuentoDTO = new  TipoDescuentoDTO();
			tipoDescuentoDTO.getId().setCodigoCompania(DEFAULT_COMPANY);
			tipoDescuentoDTO.getId().setCodigoTipoDescuento(codigoTipoDescuentoMarcaPropia);
			tipoDescuentoDTO.setEstadoPublicacion(SessionManagerSISPE.getEstadoActivo(request));
			tipoDescuentoDTO.setEstadoTipoDescuento(SessionManagerSISPE.getEstadoActivo(request));
			descuentoDTO.setTipoDescuentoDTO(tipoDescuentoDTO);
			descuentoDTO.setEstadoDescuento(SessionManagerSISPE.getEstadoActivo(request));
			
			DescuentoDTO descuentoMarcaPropiaDTO = SISPEFactory.getDataService().findUnique(descuentoDTO);
			
			//bandera que indica si el usuario desea aplicar o no el descuento MARCA PROPIA
			String aplicarDescuentoMarcaPropia = (String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA);
			
			if((aplicarDescuentoMarcaPropia == null || aplicarDescuentoMarcaPropia.equals(ConstantesGenerales.ESTADO_SI)) 
				&&  codigoTipoDescuentoMarcaPropia != null && CollectionUtils.isNotEmpty(detallePedidoCol) && descuentoMarcaPropiaDTO != null){
				
				//se recorre los detalles del pedido para verificar si son marca propia
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
					
					//se suma el total del pedido  sin IVA
					valorTotalPedidoSinIva += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado();
					
					//si el detalle no tiene autorizacion de descuento variable se valida MARCA PROPIA
					if(!AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detallePedidoDTO, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue())){

						//cuando es canasto
						if(CotizacionReservacionUtil.esCanasto(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), request)){
							
							//se verifica si es canasto especial
							if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
									|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
								LogSISPE.getLog().info("validar los detalles de cada canasto");
								
								if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
									if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
										
										//se recorre la receta del canasto
										for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
											
											//se verifica si el detalle esta activado como MARCA PROPIA
											if(articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO() != null 
													&& MarcaArticuloDTO.isLoaded(articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo())
													&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo() != null
													&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca() != null
													&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca().
													equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
												//se suman los detalles marca propia del canasto especial
												LogSISPE.getLog().info(articuloRelacionDTO.getCantidadPrevioEstadoDescuento()+" articulo marca propia del canasto: "+articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().
														getCodigoBarras()+"-"+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+": - $"+articuloRelacionDTO.getValorTotalEstado());
												valorDetallesMarcaPropia += articuloRelacionDTO.getValorTotalEstado();
											}
										}
									}
								}
							}
						}
						//para los detalles normales, se omiten los pavos marca propia
						else if(!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.pavos.marcaPropia"))
								//se verifica si el detalle esta activado como MARCA PROPIA
								&& AutorizacionesUtil.obtenerTipoMarca(detallePedidoDTO).
								equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
							
							LogSISPE.getLog().info(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()+" articulos marca propia: "+detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()
									+"-" +detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+": $"+detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado());
							valorDetallesMarcaPropia += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado(); 
						}
					}
				}
				
				LogSISPE.getLog().info("valor del pedido :"+valorTotalPedidoSinIva+ ", "+porcentajeDescuentoMarcaPropia+"0% del pedido: "+(valorTotalPedidoSinIva*porcentajeCalculado)+", valor de detalles marca propia: "+valorDetallesMarcaPropia);
				
				//se sube a sesion el total de los productos marca propia para imprimirlo  en el log
				session.setAttribute("ec.com.smx.sic.sispe.cantidad.marca.propia", descuentoMarcaPropiaDTO.getRangoInicialDescuento());
				session.setAttribute("ec.com.smx.sic.sispe.porcentaje.pedido.marcaPropia", porcentajeDescuentoMarcaPropia);
				session.setAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido", valorTotalPedidoSinIva);
				session.setAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia", valorDetallesMarcaPropia);
				
				//se verifica si el valor de los productos marca propia es mayor al 50% del pedido
				if(valorDetallesMarcaPropia > 0 && valorDetallesMarcaPropia >= (descuentoMarcaPropiaDTO.getRangoInicialDescuento()) && valorDetallesMarcaPropia >= (valorTotalPedidoSinIva * porcentajeCalculado)){
					
					String llaveDescuentoMarcaPropia = obtenerLlaveDescuentoMarcaPropia(request);
					LogSISPE.getLog().info("el pedido cumple con las condiciones para habilitar el descuento marca propia");
					
					//se agrega la llave MARCA PROPIA al opDescuento
					if(llaveDescto == null){
						llaveDescto = new String[1];
						llaveDescto[0] = llaveDescuentoMarcaPropia;
					}else{
						Boolean llaveRepetida = Boolean.FALSE;
						for(String llaveActual : llaveDescto){
							if(llaveActual.equals(llaveDescuentoMarcaPropia)){
								llaveRepetida = Boolean.FALSE;
								break;
							}
						}
						if(!llaveRepetida){
							String[] llaveDescuentoAux = new String[llaveDescto.length+1];
							for(int i=0; i<llaveDescto.length; i++){
								llaveDescuentoAux[i] = llaveDescto[i];
							}
							llaveDescuentoAux[llaveDescto.length] = llaveDescuentoMarcaPropia;
							llaveDescto = llaveDescuentoAux;
						}
					}
					
					if(subirVariableASesion){
						//se agrega la llave MARCA PROPIA a los descuentos
						if(CollectionUtils.isEmpty(llavesProcesadasCol)){
							llavesProcesadasCol = new ArrayList<String>();
						}
						if(!llavesProcesadasCol.contains(llaveDescuentoMarcaPropia)){
							llavesProcesadasCol.add(llaveDescuentoMarcaPropia);
						}
						session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, llaveDescto);
						session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llavesProcesadasCol);
						session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA, ConstantesGenerales.ESTADO_SI);
					}
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al iniciar el descuento automatico marca propia");
		}
		return llaveDescto;
	}
	

	/**
	 * Obtiene la llave del descuento MARCA PROPIA 
	 * @param request
	 * @throws Exception
	 */
	public static String obtenerLlaveDescuentoMarcaPropia(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String llaveMarcaPropia = (String)session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_MARCA_PROPIA);
		
		if(llaveMarcaPropia == null){
			
			Collection<TipoDescuentoDTO> tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			
			//se inicializa los tipos de descuentos
			if(CollectionUtils.isEmpty(tiposDescuento)){
				CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO));
				tiposDescuento = (Collection<TipoDescuentoDTO>)request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);
			}
			
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
			String codigoTipoDescuentoMarcaPropia = obtenerCodigoTipoDescuentoMarcaPropia(request);
			
			if(codigoTipoDescuentoMarcaPropia != null && CollectionUtils.isNotEmpty(tiposDescuento)){
			
				//verificar si el articulo esta en la clasificacion para dar descuentos automaticos
				int indiceFila=0;
				for (TipoDescuentoDTO tipDesDTO : tiposDescuento) {
					if(codigoTipoDescuentoMarcaPropia.trim().equals(tipDesDTO.getId().getCodigoTipoDescuento())){
						
						//se arma la llave MARCA PROPIA
							llaveMarcaPropia = indiceFila+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+
							MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento")+codigoTipoDescuentoMarcaPropia
							+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken")+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoMotivoDescuento")+
							MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.motivoDescuento.monto");
					
							//Se sube a sesion la llave MARCA PROPIA
							session.setAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_MARCA_PROPIA,llaveMarcaPropia);
							break;	
					}
					indiceFila++;
				}
			}
		}
		return llaveMarcaPropia;
	}
	
	
	/**
	 * Si el pedido no tiene el descuento MARCA PROPIA y cumple las condiciones para aplicarlo, se muestra un mensaje indicando esto
	 * @param request
	 * @param infos
	 * @throws Exception
	 */
	public static void validarSiAplicaDescuentoMarcaPropia(HttpServletRequest request, ActionMessages infos) throws Exception{
		
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
		String codigoTipoDescuentoMarcaPropia = obtenerCodigoTipoDescuentoMarcaPropia(request);
		
		//se verifica que no existe el descuento MARCA PROPIA
		Boolean existeDescuentoMarcaPropia = Boolean.FALSE;
		Collection<String> llavesProcesadasCol = (Collection<String>) request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
		if(CollectionUtils.isNotEmpty(llavesProcesadasCol)){
			for(String llaveActual : llavesProcesadasCol){
				if(StringUtils.isNotEmpty(llaveActual) && StringUtils.isNotEmpty(codigoTipoDescuentoMarcaPropia) 
						&& llaveActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia+SEPARADOR_TOKEN)){
					existeDescuentoMarcaPropia = Boolean.TRUE;
					break;
				}
			}
		}
		
		
		//si no esta aplicado el descuento MARCA PROPIA se verifica si puede aplicar
		if(!existeDescuentoMarcaPropia){
			
			//se inicia el descuentoMarcaPropia
			String[] llavesDescuentos = iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.FALSE);
			
			//se verifica si esta activa la llave MARCA PROPIA
			if(llavesDescuentos != null && llavesDescuentos.length > 0){
			
				for(String llaveActual : llavesDescuentos){
					if(StringUtils.isNotEmpty(llaveActual) && StringUtils.isNotEmpty(codigoTipoDescuentoMarcaPropia) 
							&& llaveActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia+SEPARADOR_TOKEN)){
						//el pedido es activo para aplicar descuento MARCA PROPIA
						LogSISPE.getLog().info("El pedido puede aplicar descuento MARCA PROPIA");
						infos.add("aplicaDescuentoMarcaPropia", new ActionMessage("errors.gerneral", 
								"El pedido cumple con las condiciones para aplicar el descuento: MARCA PROPIA, selecci\u00F3nelo desde la opci\u00F3n de descuentos"));
						
						//por si hay solicitud de descuentos o de stock se respalda el info para que se mantenga hasta que se cierre el popup de autorizaciones 
						request.getSession().setAttribute("ec.com.smx.sic.sispe.respaldo.infos",infos);
						break;
					}
				}
			}
		}
	}
	
	
	/**
	 * Elimina la llave MARCA PROPIA del opDescuento y de las llaves de descuentos 
	 * @param request
	 * @param llavesDescuentosCol
	 * @throws Exception
	 */
	private static void eliminarLlaveMarcaPropia(HttpServletRequest request, Collection<String> llavesDescuentosCol) throws Exception{
		
		String[] opDescuentos = (String[])request.getSession().getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
		LogSISPE.getLog().info("eliminando llave marca propia de los descuentos");
		String codigoTipoDescuentoMarcaPropia = obtenerCodigoTipoDescuentoMarcaPropia(request);
		
		Integer contadorNulos = 0;
		//se elimina la llave del opDescuentos
		if(opDescuentos != null && opDescuentos.length > 0){
			
			for(int i=0; i<opDescuentos.length; i++){
				if(opDescuentos[i] != null && opDescuentos[i].contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia)){
					opDescuentos[i] = null;
					contadorNulos ++;
				}
			}
			
			//se quitan los nulos
			if(contadorNulos > 0){
				String[] opDesuentosAux = new String[opDescuentos.length-contadorNulos];	
				Integer posVector = 0;
				for(int i=0; i<opDescuentos.length; i++){
					if(StringUtils.isNotEmpty(opDescuentos[i])){
						opDesuentosAux[posVector] = opDescuentos[i];
						posVector++;
					}
				}
				opDescuentos =  opDesuentosAux;
				request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
			}
		}
		
		//se elimina la llave de los descuentos
		if(CollectionUtils.isNotEmpty(llavesDescuentosCol)){
			
			for(String llaveActual : llavesDescuentosCol){
				if(llaveActual != null && llaveActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia)){
					llavesDescuentosCol.remove(llaveActual);
					//se muestra el mensaje que no puede aplicar descuento MARCA PROPIA
					request.getSession().setAttribute(MENSAJE_NO_APLICA_MARCA_PROPIA, Boolean.TRUE);
					break;
				}
			}
		}
	}
	
	
	/**
	 * Obtiene el codigo del tipo descuento MARCA PROPIA de la tabla de parametros
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String obtenerCodigoTipoDescuentoMarcaPropia(HttpServletRequest request) throws Exception{
		
		HttpSession session = request.getSession();
		
		//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
		String codigoTipoDescuentoMarcaPropia = (String)request.getSession().getAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_MARCA_PROPIA);
		
		if(codigoTipoDescuentoMarcaPropia == null){
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento marca propia
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigo.tipo.descuento.marcaPropia", request);
			if(parametroDTO.getValorParametro() != null){
				codigoTipoDescuentoMarcaPropia = parametroDTO.getValorParametro();
				session.setAttribute(CotizarReservarAction.CODIGO_TIPO_DESCUENTO_MARCA_PROPIA, codigoTipoDescuentoMarcaPropia);
			}
		}
		return codigoTipoDescuentoMarcaPropia;
	}
	
	
	/**
	 * Si en la coleccion de llaves de descuentos esta seleccionado el descuento marca propia, se valida si todo el pedido consolidado cumple
	 * con las condiciones para aplicar este descuento
	 * (Validacion necesaria por si desea aplicar un descuento MP de uno de los pedidos consolidados pero el total de pedidos no cumple con
	 * la condicion) 
	 * @param request
	 * @param llavesProcesadasCol1
	 * @param infos
	 * @return
	 * @throws Exception
	 */
	public static Collection<String> validarSiAplicaDescuentoMarcaPropiaEnConsolidacion(HttpServletRequest request, Collection<String> llavesProcesadasCol1, ActionMessages infos) throws Exception{
		
		Collection<String> llavesProcesadasCol = llavesProcesadasCol1;
		HttpSession session = request.getSession();
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
				&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)
				&& session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS) != null){
			//se obtiene el par\u00E1metro que indica el c\u00F3digo del tipo de descuento Marca Propia
			String codigoTipoDescuentoMarcaPropia = obtenerCodigoTipoDescuentoMarcaPropia(request);
			
			//se verifica que no existe el descuento MARCA PROPIA
			Boolean existeDescuentoMarcaPropia = Boolean.FALSE;
			if(CollectionUtils.isNotEmpty(llavesProcesadasCol)){
				for(String llaveActual : llavesProcesadasCol){
					if(StringUtils.isNotEmpty(llaveActual) && StringUtils.isNotEmpty(codigoTipoDescuentoMarcaPropia) 
							&& llaveActual.contains(CODIGO_TIPO_DESCUENTO+codigoTipoDescuentoMarcaPropia+SEPARADOR_TOKEN)){
						existeDescuentoMarcaPropia = Boolean.TRUE;
						break;
					}
				}
			}
			//si esta seleccionado el descuento MARCA PROPIA se verifica si puede aplicar
			if(existeDescuentoMarcaPropia){
				
				LogSISPE.getLog().info("validar si debe o no aplicar el descuento automatico: MARCA PROPIA");
				try{
					
					ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
					
					Double valorTotalPedidoSinIva = 0D;
					Double valorDetallesMarcaPropia = 0D;
					Double porcentajeDescuentoMarcaPropia = 1D;
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentaje.pedidos.marca.propia", request);
					if(parametroDTO.getValorParametro() != null){
						porcentajeDescuentoMarcaPropia = Double.valueOf(parametroDTO.getValorParametro());
					}
					Double porcentajeCalculado = porcentajeDescuentoMarcaPropia/100;
					
					DescuentoDTO descuentoDTO = new DescuentoDTO();
					descuentoDTO.getId().setCodigoCompania(DEFAULT_COMPANY);
					TipoDescuentoDTO tipoDescuentoDTO = new  TipoDescuentoDTO();
					tipoDescuentoDTO.getId().setCodigoCompania(DEFAULT_COMPANY);
					tipoDescuentoDTO.getId().setCodigoTipoDescuento(codigoTipoDescuentoMarcaPropia);
					descuentoDTO.setTipoDescuentoDTO(tipoDescuentoDTO);
					
					DescuentoDTO descuentoMarcaPropiaDTO = SISPEFactory.getDataService().findUnique(descuentoDTO);
					
					String aplicarDescuentoMarcaPropia = (String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA);
					
					if((aplicarDescuentoMarcaPropia == null || aplicarDescuentoMarcaPropia.equals(ConstantesGenerales.ESTADO_SI)) 
						&&  codigoTipoDescuentoMarcaPropia != null && CollectionUtils.isNotEmpty(detallePedidoCol) && descuentoMarcaPropiaDTO != null){
						//se recorre los detalles del pedido para verificar si son marca propia
						for(DetallePedidoDTO detallePedidoDTO : detallePedidoCol){
							
							valorTotalPedidoSinIva += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado();
							
							//si el detalle no tiene autorizacion de descuento varaible se valida MARCA PROPIA
							if(!AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detallePedidoDTO, ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue())){

								//cuando es canasto especial
								if(CotizacionReservacionUtil.esCanasto(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), request)){
									//se verifica si es canasto especial
									if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))
											|| detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(ConstantesGenerales.ESTADO_ACTIVO)){
										LogSISPE.getLog().info("validar los detalles de cada canasto");
										
										if(CollectionUtils.isNotEmpty(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
											if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().iterator().next() instanceof ArticuloRelacionDTO){
												for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
													if(articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO() != null 
															&& MarcaArticuloDTO.isLoaded(articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo())
															&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo() != null
															&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca() != null
															&& articuloRelacionDTO.getArticuloRelacion().getArticuloComercialDTO().getMarcaComercialArticulo().getValorTipoMarca().
															equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
														//se suman los detalles marca propia del canasto especial
														LogSISPE.getLog().info(articuloRelacionDTO.getCantidadPrevioEstadoDescuento()+" articulo marca propia: "+articuloRelacionDTO.getArticuloRelacion().getCodigoBarrasActivo().getId().
																getCodigoBarras()+"-"	+articuloRelacionDTO.getArticuloRelacion().getDescripcionArticulo()+": - $"+articuloRelacionDTO.getValorTotalEstado());
														valorDetallesMarcaPropia += articuloRelacionDTO.getValorTotalEstado();
													}
												}
											}
										}
									}
								}
								//para el caso de pavos marca propia
								else if(!detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.pavos.marcaPropia"))
										&& AutorizacionesUtil.obtenerTipoMarca(detallePedidoDTO).
										equals(SICArticuloConstantes.getInstancia().MARCAPROPIA.toString())){
									LogSISPE.getLog().info(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()+" articulos marca propia: "+detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras()
											+"-" +detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+": $"+detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado());
									valorDetallesMarcaPropia += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalEstado(); 
								}
							}
						}
						
						LogSISPE.getLog().info("valor del pedido :"+valorTotalPedidoSinIva+ ", "+porcentajeDescuentoMarcaPropia+"0% del pedido: "+(valorTotalPedidoSinIva*porcentajeCalculado)+", valor de detalles marca propia: "+valorDetallesMarcaPropia);
						
						//se sube a sesion el total de los productos marca propia para imprimirlo  en el log
						session.setAttribute("ec.com.smx.sic.sispe.cantidad.marca.propia", descuentoMarcaPropiaDTO.getRangoInicialDescuento());
						session.setAttribute("ec.com.smx.sic.sispe.porcentaje.pedido.marcaPropia", porcentajeDescuentoMarcaPropia);
						session.setAttribute("ec.com.smx.sic.sispe.valor.detalles.totalPedido", valorTotalPedidoSinIva);
						session.setAttribute("ec.com.smx.sic.sispe.valor.detalles.marcaPropia", valorDetallesMarcaPropia);
						
						//se verifica si el valor de los productos marca propia es mayor al 50% del pedido
						if(valorDetallesMarcaPropia > 0 && valorDetallesMarcaPropia >= (descuentoMarcaPropiaDTO.getRangoInicialDescuento()) && valorDetallesMarcaPropia >= (valorTotalPedidoSinIva * porcentajeCalculado)){
							LogSISPE.getLog().info("el pedido cumple con las condiciones para habilitar el descuento marca propia");
				
							session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS, llavesProcesadasCol);
							session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTO_MARCA_PROPIA, ConstantesGenerales.ESTADO_SI);
							
						}else{
							//se eliminan las llaves de descuento marca propia
							eliminarLlaveMarcaPropia(request, llavesProcesadasCol);
							LogSISPE.getLog().info("no se puede aplicar descuento marca propia porque no cumple con las condiciones");
						}
					}
					
				}catch (Exception e) {
					LogSISPE.getLog().error("Error al iniciar el descuento automatico marca propia");
				}
			}
		}
		return llavesProcesadasCol;
	}
	
	
	/**
	 * Se respaldan los detalles con los datos relacionados a las autorizaciones
	 * @param request
	 */
	public static void respaldarDetallesAutorizaciones(HttpServletRequest request, CotizarReservarForm formulario){
		
		HttpSession session = request.getSession();
		final String keyDetallePedido = "detallePedido";
		final String keyAutDesactivar = "autorizacionesDesactivar";
		final String keyDescVariable = "descVariable";
		final String keyColaAutorizaciones = "colaAutorizaciones";
		final String keyDescSeleccionado = "descSeleccionado";
		final String keyPorcentajeDescuento = "porcentajeDescuento";
		
		LogSISPE.getLog().info("respaldando los detalles con sus autorizaciones");
		
		ArrayList<DetallePedidoDTO> detallePedidoCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		ArrayList<DetalleEstadoPedidoAutorizacionDTO> autorizacionesDesactivarCol = (ArrayList<DetalleEstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL);
		ArrayList<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (ArrayList<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
		ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizaciones = (ArrayList<EstadoPedidoAutorizacionDTO>) session.getAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES);
		
		Map<String, Object> mapRespaldoDetallesAutorizaciones = new HashMap<String, Object>();
		mapRespaldoDetallesAutorizaciones.put(keyDetallePedido, SerializationUtils.clone(detallePedidoCol));
		mapRespaldoDetallesAutorizaciones.put(keyAutDesactivar, SerializationUtils.clone(autorizacionesDesactivarCol));
		mapRespaldoDetallesAutorizaciones.put(keyDescVariable, SerializationUtils.clone(colDescuentoEstadoPedidoDTOVariables));
		mapRespaldoDetallesAutorizaciones.put(keyColaAutorizaciones, SerializationUtils.clone(colaAutorizaciones));
		mapRespaldoDetallesAutorizaciones.put(keyDescSeleccionado, SerializationUtils.clone(formulario.getOpDescuentos()));
		mapRespaldoDetallesAutorizaciones.put(keyPorcentajeDescuento, SerializationUtils.clone(formulario.getPorcentajeVarDescuento()));
		
		session.setAttribute(RESPALDO_DETALLE_PEDIDO_AUTORIZACION, mapRespaldoDetallesAutorizaciones);
		
	}
	
	/**
	 * Se restauran los detalles con los datos relacionados a las autorizaciones
	 * @param request
	 */
	public static void restaurarDetallesAutorizaciones(HttpServletRequest request, CotizarReservarForm formulario){
		
		HttpSession session = request.getSession();
		Map<String, Object> mapRespaldoDetallesAutorizaciones = (Map<String, Object>) session.getAttribute(RESPALDO_DETALLE_PEDIDO_AUTORIZACION);
		
		if(mapRespaldoDetallesAutorizaciones != null){
			
			LogSISPE.getLog().info("restaurando los detalles con sus autorizaciones");
			
			final String keyDetallePedido = "detallePedido";
			final String keyAutDesactivar = "autorizacionesDesactivar";
			final String keyDescVariable = "descVariable";
			final String keyColaAutorizaciones = "colaAutorizaciones";
			final String keyDescSeleccionado = "descSeleccionado";
			final String keyPorcentajeDescuento = "porcentajeDescuento";
			
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, mapRespaldoDetallesAutorizaciones.get(keyDetallePedido));
			session.setAttribute(AutorizacionesUtil.AUTORIZACIONES_DESACTIVAR_COL, mapRespaldoDetallesAutorizaciones.get(keyAutDesactivar));
			session.setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES, mapRespaldoDetallesAutorizaciones.get(keyDescVariable));
			session.setAttribute(AutorizacionesUtil.COLA_AUTORIZACIONES, mapRespaldoDetallesAutorizaciones.get(keyColaAutorizaciones));
			session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, mapRespaldoDetallesAutorizaciones.get(keyDescSeleccionado));
			session.setAttribute(CotizacionReservacionUtil.PORCENTAJE_DESCUENTO_VARIABLE, mapRespaldoDetallesAutorizaciones.get(keyPorcentajeDescuento));
			
			//se setea otra vez el descuento marca propia para que el usuario quite este descuento
			formulario.setOpDescuentos((String[]) mapRespaldoDetallesAutorizaciones.get(keyDescSeleccionado));
			formulario.setPorcentajeVarDescuento((Double[][]) mapRespaldoDetallesAutorizaciones.get(keyPorcentajeDescuento));
			
			session.removeAttribute(RESPALDO_DETALLE_PEDIDO_AUTORIZACION);
		}
	}
	
}
