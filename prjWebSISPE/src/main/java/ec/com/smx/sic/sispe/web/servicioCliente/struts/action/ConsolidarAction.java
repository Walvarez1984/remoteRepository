/*
 * Clase CotizarReservarAction.java
 * Creado el 27/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_CON_IVA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CHECK_PAGO_EFECTIVO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_ESTABLECIMIENTO_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_PEDIDO_CONSOLIDADOS_AUX;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_TIPOS_PEDIDO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PORCENTAJE_CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.VALOR_PORCENTAJE_IVA;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.FuncionarioDTO;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloCalculo;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloUnidadManejoDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Permite realizar la consolidacion de un pedido especial.
 * @author 	walvarez
 * @version 3.0
 * @since  	JSDK 1.5.0
 */
@SuppressWarnings({"unchecked","deprecation"})
public class ConsolidarAction extends BaseAction
{
	public static final String ACCION_ANTERIOR = "ec.com.smx.sic.sispe.cotizarRecotizar.accionAnterior";
	
	public static final String AUTORIZACION_CONSOLIDAR_MINIMO = "ec.com.smx.sic.sispe.consolidar.minimo.autorizacionDTO";
	
	public static final String ACCION_CERRAR_POPUP = "ec.com.smx.sic.sispe.consolidar.cerrarPopUp";
	
	public static Boolean tieneAutorizacion = Boolean.FALSE;
	private static final String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
	private static final String CODIGO_TIPO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento"); //CTD
	private static final String CODIGO_GERENTE_COMERCIAL = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoGerenteComercial");
	private static final String TIPO_ARTICULO_PAVO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
	public static final String MOSTRAR_AUTORIZACION_CD_ELABORA_CANASTOS = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("mostrar.autorizacion.cd.elabora.canastos");
	
	/**
	 * Consoidar pedidos
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
	 * @param form				El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request 			La petici&oacute;n que estamos procesando
	 * @param response			La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		//String peticion = request.getParameter(Globals.AYUDA);
		
		HttpSession session = request.getSession();
					
		CotizarReservarForm formulario = (CotizarReservarForm) form;
		//SqlTimestampConverter convertidor; 	//convertidor de una fecha de formato String a Timestamp
		String salida = "desplegar"; 		//retorno para forward por defecto

		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		ConsolidarAction.tieneAutorizacion = Boolean.FALSE;
		
		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		try
		{
			//se agrega la acci\u00F3n correpondiente
		  	
		  	if(request.getParameter("salir")!=null){
				LogSISPE.getLog().info("cancela la autorizacion");				
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(SessionManagerSISPE.POPUPAUX);
				session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
				session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
				salida="desplegar";
			} else if(request.getParameter("aceptarAutorizacion")!=null){
				
				try{	
					boolean pasoValidacion= AutorizacionesUtil.validarAutorizacionPorNumeroUsuarioContrasenia(formulario, request, success, infos, errors, 
							ConstantesGenerales.TIPO_AUTORIZACION_CONSOLIDAR_MINIMO_PEDIDOS.longValue());
					
					if(pasoValidacion){
						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerObjAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_CONSOLIDAR_MINIMO_PEDIDOS ,null);
//							AutorizacionDTO autorizacionDTO = AutorizacionesUtil.validarNumeroAutorizacion(request, request.getParameter("numeroAutorizacion").trim(), 
//									request.getParameter("observacionAutorizacion"), tipoAutorizacion, grupoAutorizacion, codigoLocalReferencia, rolId);
						if(autorizacionDTO != null){
							//AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO, request);
							session.setAttribute(AUTORIZACION_CONSOLIDAR_MINIMO, autorizacionDTO);
							//AutorizacionesUtil.agregarAutorizacionASesion(autorizacionDTO, request);
							session.removeAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2);
							session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
							LogSISPE.getLog().info("acepta la autorizacion de modificacion reserva");
//							success.add("exitoAceptarAutorizacion", new ActionMessage("errors.autorizacion.creada.exito"));
							session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
							session.removeAttribute(SessionManagerSISPE.POPUP);
							session.removeAttribute(SessionManagerSISPE.POPUPAUX);
							session.setAttribute("ec.com.smx.banderaModal", "ok");																																
							buscarPedidosConsolidados(request,infos, errors,
									formulario, session);
							
							ConsolidarAction.tieneAutorizacion = Boolean.TRUE;
							session.setAttribute(ACCION_CERRAR_POPUP, "requestAjax('crearCotizacion.do', ['pregunta','mensajes'], {parameters: 'botonNO=ok', popWait:false});ocultarModal();popWait('div_wait');");
							session.setAttribute("ec.com.smx.banderaModal", null);
							
							String mensajeConf=MessagesWebSISPE.getString("message.confirmacion.consolidacion");
							
							//validar que tenga pedidos a consolidar
					    	if(formulario.getDatosConsolidados()==null){
					    		mensajeConf="Est\u00E1 seguro(a) que desea eliminar todos los pedidos a consolidar?";
							    WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"confirmarEliminarConsolidacion",null,request);  
							}
					    	else{
					    		WebSISPEUtil.asignarVariablesPreguntaConfirmacion("",mensajeConf,"confirmarGuardarConsolidacion",null,request);  
					    	}
					    	salida = "desplegar";
						}
					}else{
						//se saca el contenido de los errores en una cadena para imprimirlo en el popUp
						String errores = CotizacionReservacionUtil.obtenerValorDeAcctionMessages(errors);
						errors = new ActionMessages();
						
						LogSISPE.getLog().info("USUARIO NO VALIDO");
						session.removeAttribute(SessionManagerSISPE.POPUP);							
						formulario.setOpAutorizacion(estadoActivo);
						if(StringUtils.isNotEmpty(errores)){
							instanciarPopUpAutorizacion(request,"2",errores.toString());
						}else{
							instanciarPopUpAutorizacion(request,"2","Los datos del usuario son inv\u00E1lidos");	
						}
					}
				}catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci&oacute;n",ex);
					session.removeAttribute(SessionManagerSISPE.POPUP);
					formulario.setOpAutorizacion(estadoInactivo);
					instanciarPopUpAutorizacion(request,"2","El n&uacute;mero de autorizaci&oacute;n es incorrecto");
				}
				
			}else if(request.getParameter("aceptarAutorizacionEliminar")!=null){
				
				try{	
					boolean pasoValidacion= AutorizacionesUtil.validarAutorizacionPorNumeroUsuarioContrasenia(formulario, request, success, infos, errors, 
							ConstantesGenerales.TIPO_AUTORIZACION_CONSOLIDAR_MINIMO_PEDIDOS.longValue());
					
					if(pasoValidacion){
						AutorizacionDTO autorizacionDTO = AutorizacionesUtil.obtenerObjAutorizacionPorTipoDesdeSesion(request, ConstantesGenerales.TIPO_AUTORIZACION_CONSOLIDAR_MINIMO_PEDIDOS ,null);
						
						if(autorizacionDTO != null){
							
							//se elimina la ventana de sesi\u00F3n
							session.removeAttribute(SessionManagerSISPE.POPUP);
							session.removeAttribute(SessionManagerSISPE.POPUPAUX);
							session.setAttribute("ec.com.smx.banderaModal", "ok");
							
							session.setAttribute(AUTORIZACION_CONSOLIDAR_MINIMO, autorizacionDTO);
							session.removeAttribute("ec.com.smx.sic.sispe.mostrarMensaje");
							session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
							
							ConsolidarAction.tieneAutorizacion = Boolean.TRUE;
							
							session.setAttribute(ACCION_CERRAR_POPUP, "requestAjax('crearCotizacion.do', ['pregunta','mensajes'], {parameters: 'botonNO=ok', popWait:false});ocultarModal();popWait('div_wait');");
							session.setAttribute("ec.com.smx.banderaModal", null);
							
							if(session.getAttribute(CotizarReservarAction.TIPO_ELIMINACION).equals(MessagesWebSISPE.getString("message.tipoEliminacionTotal")))
							{
								CotizacionReservacionUtil.instanciarVentanaEliminarPedidoTotal(request, accion);
							}else {
								CotizacionReservacionUtil.instanciarVentanaEliminarPedidos(request, accion);
							}
							
					    	salida = "desplegar";
						}
					}else{
						//se saca el contenido de los errores en una cadena para imprimirlo en el popUp
						String errores = CotizacionReservacionUtil.obtenerValorDeAcctionMessages(errors);
						errors = new ActionMessages();
						
						LogSISPE.getLog().info("USUARIO NO VALIDO");
						session.removeAttribute(SessionManagerSISPE.POPUP);							
						formulario.setOpAutorizacion(estadoActivo);
						if(StringUtils.isNotEmpty(errores)){
							instanciarPopUpAutorizacion(request,"2",errores.toString());
						}else{
							instanciarPopUpAutorizacion(request,"2","Los datos del usuario son inv\u00E1lidos");	
						}
					}
				}catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci&oacute;n",ex);
					session.removeAttribute(SessionManagerSISPE.POPUP);
					formulario.setOpAutorizacion(estadoInactivo);
					instanciarPopUpAutorizacion(request,"2","El n&uacute;mero de autorizaci&oacute;n es incorrecto");
				}
			}
		  	else{
		  		SessionManagerSISPE.removeVarSession(request);
		  		request.getSession().removeAttribute("autorizaGerComConsolidacion");
		  		
		  		cargarPedidosConsolidados(request,formulario, session,estadoActivo);
		  		//session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar"));
		  		LogSISPE.getLog().info("Ingresa por defecto");
		  		
		  		session.setAttribute("ec.com.smx.sic.sispe.estado.actual", "SI");		  		
		  		
			  	// session.setAttribute(SessionManagerSISPE.MOSTRAR_METODO_AUTORIZACION_2,"2");
			  	// LogSISPE.getLog().info("va a abrir la ventana de autorizacion para consolidacion");
			  	
			  	// se llama al popUp de autorizacion
			  	// instanciarPopUpAutorizacion(request,"1","");
		  		
			  	// se cargan los pedidos consolidados que posee el usuario logueado  		
			  	buscarPedidosConsolidados(request, infos, errors, formulario, session);
			  	
			  	//se almacena el c\u00F3digo del local para utilizarlo en futuras consultas CAMBIAR
				session.setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento());
				
				session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
		  		
		  	}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		
		//se guardan los mensajes generados
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);

		return mapping.findForward(salida);
	}


	/**
	 * @param request
	 * @param formulario
	 * @param session
	 * @param estadoActivo
	 * @throws Exception
	 * @throws SISPEException
	 */
	public void cargarPedidosConsolidados(HttpServletRequest request, CotizarReservarForm formulario, HttpSession session, String estadoActivo) throws Exception, SISPEException {
		
		String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
		
		LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		LogSISPE.getLog().info("---Entro en el if de consolidar Pedidos--- {}",formulario.getBotonConsolidarPedidos());
		//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
		session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
		
		session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
		
		session.setAttribute(ACCION_ANTERIOR, session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		session.setAttribute("ec.com.smx.sic.sispe.accion",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"));
		
		//se consultan los tipos de pedido
		TipoPedidoDTO tipoPedidoFiltro = new TipoPedidoDTO(Boolean.TRUE);
		tipoPedidoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		session.setAttribute(COL_TIPOS_PEDIDO, SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoPedido(tipoPedidoFiltro));
		
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
		
		//se verifica si la entidad responsable es un local
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			//se obtienen los locales por ciudad
			SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
			
				//primero se verifica si el rol del usuario logeado est\u00E1 configurado en los par\u00E1metros
				//WebSISPEUtil.verificarUsuarioPedidoEspecial(SessionManagerSISPE.getCurrentEntidadResponsable(request), request);
		}
		
		  //se realiza la consulta de estados
		  EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
		  
	        consultaEstadoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
		  //Obtener datos de la colecci\u00F3n de estados, en la base de datos
		  Collection<EstadoSICDTO> estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
		  //guardar en sesi\u00F3n esta colecci\u00F3n
		  session.setAttribute(SessionManagerSISPE.COL_ESTADOS,estados);
		  	
		  //se inicializan los atributos del formulario
		  	formulario.setDatos(null);
		  	formulario.setEtiquetaFechas("Fecha de Estado");
		  	
		  	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
		  	if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadBodega)){
		  		formulario.setOpEntidadResponsable(entidadBodega);
		  	}
		  	
		  	formulario.setOpEstadoActivo(estadoActivo);
		  	
		  	//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
		  	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
		  			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
		  	
		  	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
		  			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
		  	
		  	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");
		  	//Variable de sesion para que no presente el combo de estados
		  	session.setAttribute(SessionManagerSISPE.ESTADO_CHECK,"ok");
		  	PedidoDTO pedidoDTO= (PedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.clienteOriginal");
		  	if(pedidoDTO!=null){
				//se llenan los campos restantes del cliente
		  		if(pedidoDTO.getNpNombreContacto()==null || pedidoDTO.getNpNombreContacto().equals("")){
		  			pedidoDTO.setNpNombreContacto(formulario.getNombrePersona());
		  		}
		  		if(pedidoDTO.getNpTelefonoContacto()==null || pedidoDTO.getNpTelefonoContacto().equals("")){
		  			pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoPersona());
		  		}
			}
		  	
		  //Seleccionar los pedidos ya consolidados
//			      	Collection<VistaPedidoDTO> visPedCol=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol");
		  	Collection<VistaPedidoDTO> visPedCol=formulario.getDatosConsolidados();
		  	Collection<VistaPedidoDTO> visPedColAux= new ArrayList<VistaPedidoDTO>(visPedCol==null?0:visPedCol.size());
		  //Agregar seleccionado el pedido para consolidar
		  	String []validarPedidos= new String[visPedCol==null?0:visPedCol.size()];
		  	String []numeroPedidoConsolidado=new String[visPedCol==null?0:visPedCol.size()];
		  	if(visPedCol!=null){
		  		int iValPedido=0;
		      	for(VistaPedidoDTO visPedDTO:visPedCol){
		      		if(visPedDTO.getVistaPedidoDTOCol()!=null && visPedDTO.getVistaPedidoDTOCol().size()>0){
		      			int posSeleccionado=0;
			      		String []checkConsolidados=new String[visPedDTO.getVistaPedidoDTOCol().size()];
			      		for(VistaPedidoDTO visPedDTO1:visPedDTO.getVistaPedidoDTOCol()){
			      			checkConsolidados[posSeleccionado]=String.valueOf(posSeleccionado)+"-"+visPedDTO1.getId().getCodigoPedido();
			      			LogSISPE.getLog().info("Pedido a seleccionar {}",checkConsolidados[posSeleccionado]);
			      			posSeleccionado++;
			      		}
			      		formulario.setOpSeleccionPedidosConsolidados(checkConsolidados);
		      		}
		      		LogSISPE.getLog().info("Pedido seleccionado: {}",visPedDTO.getVistaPedidoDTOCol().size());
		      		visPedColAux.add((VistaPedidoDTO)SerializationUtils.clone(visPedDTO));
		      		validarPedidos[iValPedido]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
		      		numeroPedidoConsolidado[iValPedido]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+visPedDTO.getId().getCodigoPedido();
					request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTO);
		      		iValPedido++;
		      	}
		  	}
		  formulario.setDatosConsolidados(visPedColAux);
		  formulario.setPedidosValidados(validarPedidos);
		  formulario.setNumeroConsolidado(numeroPedidoConsolidado);
		  LogSISPE.getLog().info("visPedColAux= {}",visPedColAux==null?visPedColAux:visPedColAux.size());
		  request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarColAux",visPedColAux);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpAutorizacion(HttpServletRequest request, String opcion, String mensaje)throws Exception{
		//se crea la ventana que pide la autorizacion
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Consolidar pedidos");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		
		popUp.setMensajeVentana("Para realizar una consolidaci&oacute;n se requiere la autorizaci&oacute;n del ADMINISTRADOR");
		//popUp.setValorOK("requestAjaxByNameForm('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true},'cotizarRecotizarReservarForm');ocultarModal();");
								
		popUp.setAccionEnvioCerrar("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POS
		popUp.setPreguntaVentana("\u00BFDesea aplicar la autorizaci&oacute;n ingresada?");
		popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
		popUp.setTope(70d);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);

		//se guarda la ventana
		if(opcion.equals("1")){
			popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok', popWait:true, evalScripts: true});");
			popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok', popWait:true, evalScripts: true});");
			request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);			
		}else{
			popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok', popWait:true, evalScripts: true});");
			popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok', popWait:true, evalScripts: true});");
			popUp.setMensajeErrorVentana(mensaje);
			request.getSession().setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
		}
		
		request.getSession().setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
		request.getSession().setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
		popUp=null;
	}
	
	
	/**
	 * @param request
	 * @param infos
	 * @param errors
	 * @param formulario
	 * @param session
	 * @throws Exception
	 */
	public void buscarPedidosConsolidados(HttpServletRequest request, ActionMessages infos, ActionMessages errors, CotizarReservarForm formulario, HttpSession session)	throws Exception {
		
//		String salida;
		LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
		  LogSISPE.getLog().info("Buscando pedidos CotizarRecotizar desde CONSOLIDACION");
			//obtener de sesion en el caso de que ya se haya seleccionado pedidos a cotizar
		  	//Collection<VistaPedidoDTO> visPedCol=(Collection<VistaPedidoDTO>) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarCol");
			Collection<VistaPedidoDTO> visPedCol=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados()==null?0:formulario.getDatosConsolidados().size());
			Collection<VistaPedidoDTO> visPedColeCol=formulario.getDatosConsolidados();
			//clonar cada registro
			if(visPedColeCol!=null){
		    	for(VistaPedidoDTO visPedClone:visPedColeCol){
		    		visPedCol.add((VistaPedidoDTO)SerializationUtils.clone(visPedClone));
		    	}
			}
		  	String excluirPedidos="";
		  	if(visPedCol!=null){
		  		LogSISPE.getLog().info("Pedidos consolidados {}",visPedCol.size());
		  		String []pedidosConsolidados=new String[visPedCol.size()];
		  		int index=0;
		  		String separador="-";
		  		for(VistaPedidoDTO visPedDTO:visPedCol){
		  			String value=String.valueOf(index)+separador+visPedDTO.getId().getCodigoPedido();
		  			excluirPedidos=excluirPedidos+visPedDTO.getId().getCodigoPedido()+",";
		  			LogSISPE.getLog().info("value {}",value);
		  			pedidosConsolidados[index]=value;
		  				index++;
		  		}
		  		formulario.setOpSeleccionPedidosConsolidados(pedidosConsolidados);
		  	}
		  	
		  	//colecci\u00F3n que almacena los pedidos buscados
		    Collection <VistaPedidoDTO> colVistaPedidoDTO = new ArrayList <VistaPedidoDTO>();
		    Collection <VistaPedidoDTO> colVistaPedidoConDTO = new ArrayList <VistaPedidoDTO>();
  			//se incializa la colecci\u00F3n
  			formulario.setDatos(null);
		        try
		        {
		        	//obtener los registros consolidados en el rango establecido
		        	VistaPedidoDTO consultaPedidoConsolidadosDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);		        	
		        	consultaPedidoConsolidadosDTO.setNpConsultarTodosConsolidados(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		        	//consultaPedidoConsolidadosDTO.setNpConsultarTodosConsolidadosSinAnulados("SI");
		        	LogSISPE.getLog().info("N\u00FAmero de meses: {}",formulario.getNumeroMeses());
		        	//rango de fechas para las b\u00FAsquedas
		    		Timestamp fechaInicial = null;
		    		Timestamp fechaFinal = null;
					int meses = Integer.valueOf(formulario.getNumeroMeses());
					try{
						if(formulario.getNumeroMeses()!=null && !formulario.getNumeroMeses().equals("") ){
							//se convierte a entero el n\u00FAmero de meses
							meses = Integer.parseInt(formulario.getNumeroMeses());
						}
						else{
							LogSISPE.getLog().info("Se toma el parametro por default {} meses", meses);
						}
					}catch(NumberFormatException e) {
						LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
					}
					fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
					fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 23, 59, 59, 999));
					consultaPedidoConsolidadosDTO.setNpFechaEstadoInicial(fechaInicial);
					consultaPedidoConsolidadosDTO.setNpFechaEstadoFinal(fechaFinal);
		        	colVistaPedidoConDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoConsolidadosDTO);
		        	
//		          if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
//		            infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos"));
//		          }else{
		            LogSISPE.getLog().info("ENTRO A LA PAGINACION");
		            //crear coleccion de todos los pedidos consolidados
		            Collection<VistaPedidoDTO> visPedColConsolidados=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTO.size());
		            for(VistaPedidoDTO visPedDTO:colVistaPedidoConDTO){
		        		if(visPedDTO.getCodigoConsolidado()!=null && !visPedDTO.getCodigoConsolidado().equals("") ){
		        			LogSISPE.getLog().info("Pedido consolidado: {}",visPedDTO.getId().getCodigoPedido().trim());
		        			Boolean agregarConsolidado=Boolean.TRUE;
		        			if(visPedColConsolidados.size()>0){
		        				//buscar si ya esta agregado el pedido consolidado
		        				for(VistaPedidoDTO visPedConDTO:visPedColConsolidados){
		        					if(visPedConDTO.getCodigoConsolidado().equals(visPedDTO.getCodigoConsolidado())){
		        						agregarConsolidado=Boolean.FALSE;
		        						break;
		        					}
		        				}
		        			}
		        			if(agregarConsolidado){
		    					visPedColConsolidados.add(visPedDTO);
		    				}
		        		}
		        	}
		            
		            //agrupar los pedidos consolidados para mostrar en forma de arbol
		            Collection<VistaPedidoDTO> visPedColConsAux;
		            Collection<VistaPedidoDTO> vistaPedidoDTOCol=new ArrayList<VistaPedidoDTO>();
		            LogSISPE.getLog().info("Total pedidos consolidados: {}",colVistaPedidoConDTO.size());
		            LogSISPE.getLog().info("Total pedidos visPedColConsolidados: {}",visPedColConsolidados.size());
		            
		            for(VistaPedidoDTO visPedDTOCon:visPedColConsolidados){
		            	String codigoConsolidado=visPedDTOCon.getCodigoConsolidado();
		            	VistaPedidoDTO vistaPedidoDTOConsolidado= new VistaPedidoDTO();
						vistaPedidoDTOConsolidado.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
						vistaPedidoDTOConsolidado.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
						vistaPedidoDTOConsolidado.setCodigoConsolidado(codigoConsolidado);
						vistaPedidoDTOConsolidado.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
						//obtener todos los pedidos consolidados
						visPedColConsAux = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosConsolidados(vistaPedidoDTOConsolidado);
						vistaPedidoDTOConsolidado.getId().setCodigoEstado("CSD");
			        	vistaPedidoDTOConsolidado.getId().setCodigoPedido(codigoConsolidado);
			        	LogSISPE.getLog().info("Total pedidos visPedColConsAux: {}",visPedColConsAux.size());
						if(visPedColConsAux.size()>0){
		            		vistaPedidoDTOConsolidado.setVistaPedidoDTOCol(visPedColConsAux);
		            		vistaPedidoDTOCol.add(vistaPedidoDTOConsolidado);
		            		//Eliminar los pedidos que ya estan en la seccion de consolidado
		            		for(VistaPedidoDTO visPedDTOEliminar:visPedColConsAux){
		            			for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
		            				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
		            					LogSISPE.getLog().info("remover de coleccion colVistaPedidoDTO");
		            					colVistaPedidoDTO.remove(visPedDTOEliCon);
		            					break;
		            				}
		            			}	
		            		}
		            	}
		            }
		          //Eliminar los pedidos que ya estan en la seccion de consolidado y vienen de sesion
		    		LogSISPE.getLog().info("Total pedidos vistaPedidoDTOCol: {}",vistaPedidoDTOCol.size());
		    		for(VistaPedidoDTO visPedDTOEliminar:visPedCol){
		    			for(VistaPedidoDTO visPedDTOEliCon:vistaPedidoDTOCol){
		    				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
		    					LogSISPE.getLog().info("remover de coleccion vistaPedidoDTOCol");
		    					vistaPedidoDTOCol.remove(visPedDTOEliCon);
		    					break;
		    				}
		    			}	
		    		}
		    		//agregar  a la coleccion seleccionada como consolidada los nuevos pedidos consolidados consultados
					visPedCol.addAll(vistaPedidoDTOCol);
					//activar el check inicial para indicar q el existe un pedido consolidado
					String []validarPedidos=new String[visPedCol.size()];
			        String []numeroPedidoConsolidado=new String[visPedCol.size()];
			        int iPedidos=0;
			        VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO");
		            for(VistaPedidoDTO visPedDTO:visPedCol){
		            	LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
		            	if(visPedDTO.getId().getCodigoPedido().equals("CONSOLIDADO")){
		            			validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
		            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+formulario.getNumeroAConsolidar();
		            			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",visPedDTO);
		            			//Eliminar los pedidos que ya estan en la seccion de consolidado
				            		for(VistaPedidoDTO visPedDTOEliminar:visPedDTO.getVistaPedidoDTOCol()){
				            			for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
				            				if(visPedDTOEliminar.getId().getCodigoPedido().equals(visPedDTOEliCon.getId().getCodigoPedido())){
				            					colVistaPedidoDTO.remove(visPedDTOEliCon);
				            					break;
				            				}
				            			}	
				            		}
		            	}
		            	else{
		            		if(vistaPedidoDTO!=null){
		            			if(visPedDTO.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
		            				validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			            			numeroPedidoConsolidado[iPedidos]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+vistaPedidoDTO.getId().getCodigoPedido();
		            			}
		            			else{
				            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
				            		numeroPedidoConsolidado[iPedidos]=null;
			            		}
		            		}else{
			            		validarPedidos[iPedidos]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
			            		numeroPedidoConsolidado[iPedidos]=null;
		            		}
		            	}
		            	iPedidos++;
		            }
	            
		            for(VistaPedidoDTO visEliCol:visPedCol){
		            	for(VistaPedidoDTO vistaConsolidadosCol:visEliCol.getVistaPedidoDTOCol()){
		            		for(VistaPedidoDTO visPedDTOEliCon:colVistaPedidoDTO){
				            	if(visPedDTOEliCon.getId().getCodigoPedido().equals(vistaConsolidadosCol.getId().getCodigoPedido())){
				            		colVistaPedidoDTO.remove(visPedDTOEliCon);
		        					break;
		        				}
		            		}
		            	}
		            }
		            
		            formulario.setPedidosValidados(validarPedidos);
		            formulario.setNumeroConsolidado(numeroPedidoConsolidado);
					//agregar a la coleccion para mostrar en la seccion de pedidos consolidados
					formulario.setDatosConsolidados(visPedCol);
					session.setAttribute(COL_PEDIDO_CONSOLIDADOS_AUX, visPedCol);
		            //se elimina la variable del indice de paginaci\u00F3n
		            session.removeAttribute(ListadoPedidosForm.INDICE_PAGINACION);
		            session.setAttribute("ec.com.smx.sic.sispe.listDatosConsolidados",visPedCol);
		            //llamada a la funci\u00F3n que inicializa los datos de ordenamiento
		            this.inicializarDatosOrdenamiento(request);

		        }catch(SISPEException ex){
		          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		          errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos"));
		        }
//     }
//		    salida = "desplegarConsolidarPedido";
	}
	
	
	 /**
	   * Inicializa los datos para el ordenamiento
	   * @param request
	   */
	  private void inicializarDatosOrdenamiento(HttpServletRequest request){
	  	
	    //se incializan los datos de la tabla
	    String[][] datosTabla = {
	      {"id.codigoPedido", "C\u00F3digo del pedido", null},
	      {"llaveContratoPOS", "Llave del contrato", null},
	      {"fechaInicialEstado", "Fecha de pedido", null},
//	      {"nombreContacto", "Nombre del Cliente", null},
	      {"contactoEmpresa", "Nombre del cliente", null},
	      {"totalPedido", "Valor total", null},
	      {"abonoPedido", "Valor abonado", null},
	      {"descripcionEstado", "Estado", null},
	      {"etapaEstadoActual", "Etapa del estado", null},
	      {"estadoActual", "Estado actual", null}
	    };
	    //se guardan los datos en sesi\u00F3n
	    request.getSession().setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
	    //se inicializa los datos de la culumna ordenada 
	    request.getSession().setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {"C\u00F3digo del Pedido", "Ascendente"});
	  }
	  
//	  private void instanciarPopUpAutorizacionEliminar(HttpServletRequest request, String opcion, String mensaje)throws Exception{
//			//se crea la ventana que pide la autorizacion
//			UtilPopUp popUp = new UtilPopUp();
//			popUp.setTituloVentana("Eliminar pedidos consolidados");
//			popUp.setEtiquetaBotonOK("Si");
//			popUp.setEtiquetaBotonCANCEL("No");
//			
//			popUp.setMensajeVentana("Para eliminar uno o m&aacute;s pedidos consolidados se requiere la autorizaci&oacute;n del ADMINISTRADOR");
//			//popUp.setValorOK("requestAjaxByNameForm('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'aceptarAutorizacion=ok',evalScripts: true},'cotizarRecotizarReservarForm');ocultarModal();");
//									
//			popUp.setAccionEnvioCerrar("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
//			popUp.setValorCANCEL("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'salir=exit',evalScripts: true});ocultarModal();");
//			popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
//			//se asigna el mensaje para indicar que el pedido solo se puede anular desde el POSsalida = "desplegarConsolidarPedido";
//			popUp.setPreguntaVentana("\u00BFDesea aplicar la autorizaci&oacute;n ingresada?");
//			popUp.setContenidoVentana("servicioCliente/autorizacion/ingresoAutorizacionPopUp.jsp");
//			popUp.setTope(70d);
//			popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
//
//			//se guarda la ventana
//			if(opcion.equals("1")){
//				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'eliminarPedidosConsolidados=ok',evalScripts: true});");
//				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'eliminarPedidosConsolidados=ok',evalScripts: true});");
//				request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);			
//			}else{
//				popUp.setValorOK("requestAjax('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'eliminarPedidosConsolidados=ok',evalScripts: true});");
//				popUp.setValorKeyPress("requestAjaxEnter('consolidacion.do', ['mensajes','pregunta','div_pagina','seccion_detalle'], {parameters: 'eliminarPedidosConsolidados=ok',evalScripts: true});");
//				popUp.setMensajeErrorVentana(mensaje);
//				request.getSession().setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
//			}
//			
//			request.getSession().setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
//			request.getSession().setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR, "CONSOLIDAR");
//			popUp=null;
//		}
	  
		/**
		 * @param request
		 * @param formulario
		 * @param session
		 * @param estadoActivo
		 * @param estadoInactivo
		 * @throws Exception
		 */
		public static void inicializarVariablesFormulario(HttpServletRequest request, CotizarReservarForm formulario, HttpSession session,	String estadoActivo, 
				String estadoInactivo, ActionMessages infos,ActionErrors errores, ActionMessages errors, ActionMessages warnings,String accion) throws Exception {
			//recuperando las variables de sesion por defecto
			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			
			//se guarda en sesi\u00F3n el registro obtenido
			PedidoDTO pedidoDTO= (PedidoDTO)request.getSession().getAttribute("ec.com.smx.sic.sispe.pedido.clienteOriginal");
			if(pedidoDTO!=null){
				//se llenan los campos restantes del cliente
//				formulario.setCedulaContacto(pedidoDTO.getCedulaContacto());
//				formulario.setNombreContacto(pedidoDTO.getNombreContacto());
//				formulario.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
				formulario.setTipoDocumento(pedidoDTO.getNpTipoDocumento());
				//para el contexto del pedido EMPRESARIAL o INDIVIDUAL
				formulario.setOpTipoDocumento(pedidoDTO.getContextoPedido());

				//datos de la empresa
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					formulario.setRucEmpresa(pedidoDTO.getRucEmpresa());
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					formulario.setNombreEmpresa(pedidoDTO.getNombreEmpresa());
			}
			else{
				String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
				//se guarda en sesi\u00F3n el pedido seleccionado
				VistaPedidoDTO vistaPedidoDTO= (VistaPedidoDTO) session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				if(vistaPedidoDTO!=null){
					/*------------------- se asignan los atributos necesarios al formulario --------------------*/

					LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
					//se asigna el contexto del pedido al formulario
					if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
						formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
						formulario.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
						formulario.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
						formulario.setTelefonoEmpresa(vistaPedidoDTO.getTelefonoEmpresa());
					}else{
						formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
						formulario.setTelefonoPersona(vistaPedidoDTO.getTelefonoContacto());
						formulario.setNumeroDocumento(vistaPedidoDTO.getCedulaContacto());
						formulario.setNombrePersona(vistaPedidoDTO.getNombreContacto());
					}
		
					//se asigna el tipo de documento del cliente al formulario
					if(vistaPedidoDTO.getTipoDocumentoCliente().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"))){
						formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
					}else{
						formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE);
					}
		
					formulario.setOpTipoEspeciales("0");
					formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
					formulario.setOpAutorizacion(estadoInactivo);
		
					//se asigna el local responsable, adicionalmente se agrega 
					formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()
							+ " " + caracterToken + " " + vistaPedidoDTO.getNombreLocal());
					LogSISPE.getLog().info("LOCAL RESPONSABLE OBTENIDO: {}",formulario.getLocalResponsable());
					//se inicializa el costo del flete
					//se asigna el costo del flete
					if(vistaPedidoDTO.getValorCostoEntregaPedido()!=null){
						formulario.setCostoFlete(vistaPedidoDTO.getValorCostoEntregaPedido());
					}else{
						formulario.setCostoFlete(0D);
					}
				}
			}
			//agregar solo el pedido consolidado
			List<VistaPedidoDTO> listDatosConsolidados=new ArrayList<VistaPedidoDTO>(formulario.getDatosConsolidados());
			Collection<VistaPedidoDTO> pedidoConsolidado=new ArrayList<VistaPedidoDTO>(1);
			String []numConsolidados=formulario.getNumeroConsolidado();
			formulario.setNumeroPedidoConsolidado(formulario.getNumeroAConsolidar());
			for(int iConsolidado=0;iConsolidado<numConsolidados.length;iConsolidado++){
				LogSISPE.getLog().info("numConsolidados[{}]: {}",iConsolidado,numConsolidados[iConsolidado]);
				if(numConsolidados[iConsolidado]!=null){
					VistaPedidoDTO visPedDTO=listDatosConsolidados.get(iConsolidado);
					pedidoConsolidado.add(visPedDTO);
					formulario.setNumeroPedidoConsolidado(visPedDTO.getId().getCodigoPedido());
					break;
				}
			}
			  formulario.setDatosConsolidados(pedidoConsolidado);
			  //obtener todos los detalles de los pedidos consolidados
			  //session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
			  LogSISPE.getLog().info("ACCION ACTUAL: {}",session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
			  if(formulario.getDatosConsolidados().size()>0){
				//se obtiene la colecci\u00F3n de los pedidos 
				List<VistaPedidoDTO> pedidos =(List<VistaPedidoDTO>) formulario.getDatosConsolidados();
				LogSISPE.getLog().info("total de pedidos consolidados Util: {}",pedidos.size());
				//creaci\u00F3n del DTO para almacenar el pedido selecionado
				VistaPedidoDTO vistaPedidoActDTO = (VistaPedidoDTO)pedidos.get(0);
				//obtener todos los pedidos consolidados
				Collection<VistaPedidoDTO> visPedColConsAux = vistaPedidoActDTO.getVistaPedidoDTOCol();
				//determinar si se aplican precios de afiliado o no 
				Boolean sinPrecioAfiliado=Boolean.TRUE;
				for(VistaPedidoDTO visPedDTO:visPedColConsAux){
					if(visPedDTO.getEstadoPreciosAfiliado()!=null && visPedDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
			  			session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			  			formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
			  			sinPrecioAfiliado = Boolean.FALSE;
			  			break;
					}
				}
				if(sinPrecioAfiliado){
					session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
					formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
					for(VistaPedidoDTO vistPedDTO : visPedColConsAux){
						vistPedDTO.setEstadoPreciosAfiliado(estadoInactivo);
					}
				}else{
					for(VistaPedidoDTO vistPedDTO : visPedColConsAux){
						vistPedDTO.setEstadoPreciosAfiliado(estadoActivo);
					}
				}
				List<DetallePedidoDTO> detallePedido = construirDetallesPedidoDesdeVistaConsolidados(formulario,request,visPedColConsAux,Boolean.TRUE,Boolean.FALSE, errors);
				session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallePedido);
				
				//se crea y sube a sesion el detalle consolidado
				crearDetalleConsolidadosNoRepetidos(request, detallePedido);
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,visPedColConsAux);
				
				Collection<VistaPedidoDTO> vistaPedidoTotal=new ArrayList<VistaPedidoDTO>(visPedColConsAux.size()+1);
				vistaPedidoTotal.addAll(visPedColConsAux);
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL,vistaPedidoTotal);
				formulario.setDatosConsolidadosTotal(vistaPedidoTotal);
				
				if(formulario.getOpDescuentoSeleccionadoConsolidado()!=null && !formulario.getOpDescuentoSeleccionadoConsolidado().equals("")){
					String descuentoSeleccionado= formulario.getOpDescuentoSeleccionadoConsolidado()[0];
					if(!descuentoSeleccionado.equals("")){
						String[] tipoDescuento=descuentoSeleccionado.split("-");
						if(tipoDescuento[0].equals("PO")){
							Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
							crearParametrosDescuentos(request, session, descuentos);
						}
						else{
//							Integer fila=Integer.valueOf(tipoDescuento[0]);
							String pedido=tipoDescuento[1];
							List<VistaPedidoDTO> visPedConDesc= (List<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
							for(VistaPedidoDTO visPedDTO:visPedConDesc){
								if(visPedDTO.getId().getCodigoPedido().equals(pedido)){
									Collection<DescuentoEstadoPedidoDTO> descuentos =visPedDTO.getDescuentosEstadosPedidos();
									crearParametrosDescuentos(request, session, descuentos);
								}
							}
						}
					}else{
						session.removeAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
						session.removeAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
					  }
			}
			crearPedidoGeneral(request, formulario, session,detallePedido,infos,errores,errors,warnings,accion,estadoActivo,estadoInactivo,Boolean.TRUE);
		  }
		  else{
			  session.removeAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			  session.removeAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
		  }
	}
		
		/**
		 * @param request
		 * @param session
		 * @param descuentoVariableActualCol
		 * @param llavesDescuentosAplicados
		 * @param descuentos
		 * @param codigoTipoDescuentoVariable
		 * @throws Exception
		 */
		public static void crearParametrosDescuentos(HttpServletRequest request, HttpSession session, Collection<DescuentoEstadoPedidoDTO> descuentos) throws Exception {
			//colecciones que se enviar\u00E1n al m\u00E9todo que calcula los descuentos
			Collection<DescuentoDTO> descuentoVariableActualCol = new ArrayList<DescuentoDTO>();
			Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
			String codigoTipoDescuentoVariable = "";
			
			String llaveDescuentoPorCajas = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
			String llaveDescuentoMayorista = (String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
			request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
			request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
			
			//se obtiene el c\u00F3digo del tipo de descuento variable
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
			if(parametroDTO.getValorParametro()!=null){
				codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
			}
			
			//se separan los DescuentoEstadoPedidoDTO  para procesar las autorizaciones de descuento variable
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = new ArrayList<DescuentoEstadoPedidoDTO>(); 
			
			for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentos){
				
				LogSISPE.getLog().info("[codigoTipoDescuento]: {}",descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento());
				//se verifica el c\u00F3digo del descuento variable
				if(descuentoEstadoPedidoDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento())){
					//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
					//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
					descuentoEstadoPedidoDTO.getDescuentoDTO().setPorcentajeDescuento(descuentoEstadoPedidoDTO.getPorcentajeDescuento());
					descuentoVariableActualCol.add(descuentoEstadoPedidoDTO.getDescuentoDTO());
					colDescuentoEstadoPedidoDTOVariables.add(descuentoEstadoPedidoDTO);
				}
				
				//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
				if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
					llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
				}
				else{
					if(descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
						llavesDescuentosAplicados.add(llaveDescuentoPorCajas);
						session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
					}
					else if(descuentoEstadoPedidoDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
						llavesDescuentosAplicados.add(llaveDescuentoMayorista);
						session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
					}
				}
				LogSISPE.getLog().info("LLAVE: {}",descuentoEstadoPedidoDTO.getLlaveDescuento());
			}
			if(llavesDescuentosAplicados.isEmpty()){
				llavesDescuentosAplicados=null;
			}
			if(descuentoVariableActualCol.isEmpty()){
				descuentoVariableActualCol=null;
			}
			
			session.setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, llavesDescuentosAplicados == null ? null : llavesDescuentosAplicados.toArray(new String[llavesDescuentosAplicados.size()])); // validar esta linea
			session.setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llavesDescuentosAplicados);
			session.setAttribute(CotizarReservarAction.COL_DESC_VARIABLES,descuentoVariableActualCol);
			
			//se sube a sesion los descuentos variables
			if(session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES) == null){
				session.setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES,colDescuentoEstadoPedidoDTOVariables);
			}
		}
		
		/**
		 * @param request
		 * @param formulario
		 * @param session
		 * @param detallePedido
		 * @throws SISPEException
		 * @throws Exception
		 */
		public static void crearPedidoGeneral(HttpServletRequest request, CotizarReservarForm formulario, HttpSession session, 
				List<DetallePedidoDTO> detallePedido, ActionMessages infos,ActionErrors errores, ActionMessages errors,
				ActionMessages warnings, String accion, String estadoActivo, String estadoInactivo,Boolean verificarSIC) throws SISPEException,	Exception {
			
			//al crear el pedido general verificamos si se aplica o no descuentos de caja y mayorista
			CotizacionReservacionUtil.verificarSiAplicaPreciosCajaMayorista(request,session);
			
			Collection<DetallePedidoDTO> detallePedidoGeneral = (Collection<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS);
			if(CollectionUtils.isEmpty(detallePedidoGeneral)){
				
				//se crea y sube a sesion el detalle consolidado
				detallePedidoGeneral = crearDetalleConsolidadosNoRepetidos(request, detallePedido);
			}
			
			session.setAttribute("ec.com.smx.sic.sispe.pedioGeneral","pedidoGeneral");

			String []vectorCantidad= new String[detallePedidoGeneral.size()];
			String []vectorPesoPavos= new String[detallePedidoGeneral.size()];
			int pos=0;
			int posPesos=0;
			//String tipoArticuloPavo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
			for(DetallePedidoDTO detPedDTO:detallePedidoGeneral){
				vectorCantidad[pos] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
				//esta condici\u00F3n es para el caso de art\u00EDculos de peso variable que puedan cambiar el peso.
				if(esTipoArticuloPeso(detPedDTO)){
					
					vectorPesoPavos[posPesos] = String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado());
					posPesos++;
				}
				/*if(detPedDTO.getArticuloDTO().getTipoArticuloCalculoPrecio().equals(tipoArticuloPavo)){
					//se inicializa el peso total aproximado
					double pesoTotalAproximado = detPedDTO.getArticuloDTO().getPesoAproximado().doubleValue() * detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					detPedDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));
					vectorPesoPavos[posPesos]=String.valueOf(pesoTotalAproximado);
					posPesos++;
				}*/
				detPedDTO.setNpCantidadTotalConsolidada(0L);
				detPedDTO.setNpValorTotalConsolidado(0D);
				if(detPedDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo) &&
						detPedDTO.getArticuloDTO()!=null && 
						detPedDTO.getArticuloDTO().getArticuloRelacionCol()!=null && !detPedDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty()){
					//Recorro todos los articulos de la receta para verificar cuales tienen precio de caja
//						for (Iterator<ArticuloRelacionDTO> iter = detPedDTO.getArticuloDTO().getArticuloRelacionCol().iterator(); iter.hasNext();){
//							ArticuloRelacionDTO recetaArticuloDTO = iter.next();
						//CotizacionReservacionUtil.calcularValoresDetalleCanastaEspecial(detPedDTO.getEstadoDetallePedidoDTO(), recetaArticuloDTO, request, false);
						DetalleCanastaAction.calcularTotalRecetaPorPreciosEspeciales(request, detPedDTO, Boolean.FALSE);
//						}
				}
				
				pos++;
			}
			formulario.setVectorCantidad(vectorCantidad);
			formulario.setVectorPesoActual(vectorPesoPavos);
			
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedidoGeneral);
			//se actualiza el detalle para verificar si es una empresa que no paga IVA 
			//formulario.actualizarDetalleForm(request, errores, accion, estadoActivo, estadoInactivo);	
//			for(DetallePedidoDTO detPedDTO:detallePedidoGeneral){
//				CotizacionReservacionUtil.calcularValoresDetalle(detPedDTO, request, false,false);
//			}
			
			AutorizacionesUtil.verificarAutorizacionesVariables((List<DetallePedidoDTO>)detallePedidoGeneral, request, formulario);
			
			CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,verificarSIC);
			
			//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos 
			//subClasificacion para articulos tipo canastos 
	  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA, valorTipoArticuloCanastas);
			
			//subClasificacion para articulos tipo despensas 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
	  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA, valorTipoArticuloDespensas);
			
			Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);			
			if(descuentos!=null){
				session.setAttribute(CotizarReservarAction.COL_DESCUENTOS_PEDIDO_GENERAL,descuentos);
				CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
			}
			LogSISPE.getLog().info("PRECIOS ALTERADOS CONSOLIDADOS: {}",session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS));
//				AutorizacionesUtil.verificarAutorizacionesVariables((List<DetallePedidoDTO>)detallePedidoGeneral, request, formulario);
			if(session.getAttribute("ec.com.smx.sic.sispe.primer.ingreso.consolidacion") != null ){
				CotizacionReservacionUtil.generarOpDescuentos(request, descuentos);
				
//					String[] opDescuentos = (String[])session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);

				
				session.removeAttribute("ec.com.smx.sic.sispe.primer.ingreso.consolidacion");
				AutorizacionesUtil.verificarAutorizacionesVariables((List<DetallePedidoDTO>)detallePedidoGeneral, request, formulario);
//					AutorizacionesUtil.verificarClasificacionesPedido(request, errors, warnings, Boolean.FALSE);	
				
				String[] opDescuentos = (String[])session.getAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);
				
				if((session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS) != null && (Boolean) session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS)) 
					|| (session.getAttribute("existe.descuentos.variables.normales") != null)){ 
					
					//solo carga los descuentos variables elegidos, las autorizaciones solicitadas se pierden
					AutorizacionesUtil.eliminarAutorizacionesNoSeleccionadas((ArrayList<DetallePedidoDTO>)detallePedidoGeneral, request, opDescuentos);
					session.removeAttribute("existe.descuentos.variables.normales");
				}
				AutorizacionesUtil.verificarEstadoAutorizaciones(formulario, request, warnings);
			}
		}

		
		/**
		 * en base a los detalles de todos los pedidos arma una coleccion con detalles sumarizados no repetidos
		 * @param request
		 * @param detallePedidoDTOCol
		 * @return
		 * @throws Exception
		 */
		public static List<DetallePedidoDTO> crearDetalleConsolidadosNoRepetidos(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoDTOCol) throws Exception{
			
			LogSISPE.getLog().info("creando los detalles consolidados no repetidos");
			HttpSession session = request.getSession();
			
			//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
			List<DetallePedidoDTO> detallesConsolidadosNoRepetidos = new ArrayList<DetallePedidoDTO>();
			if(CollectionUtils.isNotEmpty(detallePedidoDTOCol)){
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoDTOCol){
					agregarDetallePedidoConsolidado(request, detallesConsolidadosNoRepetidos, detallePedidoDTO);
				}
				request.getSession().setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS, detallesConsolidadosNoRepetidos);
			}
			
			//se elimina de sesion el parametro
			session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
			return detallesConsolidadosNoRepetidos;
		}
		
		
		/**
		 * Crear coleccion de detalles consolidados no repetidos, si existe un detalle repetido se sumarizan los detalles
		 * @param request
		 * @param detallePedidoDTOCol
		 * @param detallePedidoConsolidadoDTO
		 * @throws Exception
		 */
		private static void agregarDetallePedidoConsolidado(HttpServletRequest request, List<DetallePedidoDTO> detallePedidoDTOCol, DetallePedidoDTO detPedDTO) throws Exception{
			
			DetallePedidoDTO detallePedidoDTO = SerializationUtils.clone(detPedDTO);
			
			//para el caso del primer registro
			if(CollectionUtils.isEmpty(detallePedidoDTOCol)){
				detallePedidoDTOCol.add(detallePedidoDTO);
			}else{
				
				int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
				boolean existeArt= Boolean.FALSE;
				
				existeArt = Boolean.FALSE;
				
				for (DetallePedidoDTO detallePedidoConsolidadoDTO : detallePedidoDTOCol){
					
					Long cantidadTotalConsolidada = 0L;
					Double valorTotalConsolidada = 0D;
					Long cantidadTotalEstado = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
					Double pesoCalculoTotal = Util.roundDoubleMath(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado(),numeroDecimales);
					
					if(detallePedidoDTO != null && detallePedidoConsolidadoDTO != null){
											
						if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
							
							cantidadTotalConsolidada = (detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue()==0?detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue():detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
							valorTotalConsolidada = Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
							valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
									detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
							cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue()==0?detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue():detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
							
							cantidadTotalEstado=cantidadTotalEstado+detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(valorTotalConsolidada);
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalConsolidada);
							
							//verificar si son articulos o no que validan cajas
							if(detallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo() && 
									CotizacionReservacionUtil.esArticuloActivoParaPrecioMayorista(detallePedidoDTO.getId().getCodigoAreaTrabajo(), detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado(),
									detallePedidoDTO.getEstadoDetallePedidoDTO().getValorMayorista(), detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado(), 
									detallePedidoDTO.getArticuloDTO(), request)){
								
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalEstado);
								
							}else if(CotizacionReservacionUtil.esArticuloActivoParaPrecioCaja(detallePedidoDTO.getId().getCodigoAreaTrabajo(), detallePedidoDTO.getEstadoDetallePedidoDTO().getValorCajaEstado(), detallePedidoDTO.getArticuloDTO(), request)){
								//Long cantidadEstado=detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalEstado);
							}
							
							//esta condici\u00F3n es para el caso de art\u00EDculos de peso variable que puedan cambiar el peso.
							if(esTipoArticuloPeso(detallePedidoDTO)){
								
								pesoCalculoTotal = Util.roundDoubleMath(pesoCalculoTotal+detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado(),numeroDecimales).doubleValue();
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Util.roundDoubleMath(pesoCalculoTotal,numeroDecimales));
							}
							
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
							existeArt = Boolean.TRUE;
							CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoConsolidadoDTO, request, false,false);
							break;
						}
					}
				}
				if(existeArt == Boolean.FALSE){
					detallePedidoDTOCol.add(detallePedidoDTO);
				}
			}
		}
		
		
		/**
		 * 
		 * @param formulario
		 * @param request
		 * @param infos
		 * @param errors
		 * @param warnings
		 * @param reconstruirDetalles
		 * @param verificarSIC
		 * @return
		 * @throws Exception
		 */
		public static boolean construirDetallesPedidoDesdeVista(CotizarReservarForm formulario, HttpServletRequest request,ActionMessages infos, ActionMessages errors, ActionMessages warnings, boolean reconstruirDetalles,Boolean verificarSIC)throws Exception
		{
			HttpSession session = request.getSession();
			//se obtienen las claves que indican un estado activo y un estado inactivo
			String estadoActivo = ( String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
			String estadoInactivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_INACTIVO);
			
			//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
			String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			VistaPedidoDTO vistaPedidoDTO = null;
			Boolean consultarDescuentos=Boolean.TRUE;
			List<DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
			List<DetallePedidoDTO> detallePedido1 = new ArrayList <DetallePedidoDTO>();
			Collection<String> codigosArticulos = new ArrayList <String>();
			//colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
			List<VistaDetallePedidoDTO> detalleVistaPedido;
			int indice = 0;
			List<VistaPedidoDTO> pedidos = null;
			VistaPedidoDTO consultaPedidoDTOAux = new VistaPedidoDTO();
			Collection <VistaPedidoDTO> colVistaPedidoDTOAux = null;
			
			//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
			String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
			session.setAttribute(CotizarReservarAction.CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);
			//Obtener limite de dias para anular pedidos
			SessionManagerSISPE.getObtenerLimiteTiempoValidezReserva(request);
			
			if(reconstruirDetalles){
				VistaPedidoDTO vistaPedidoDTOAux = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				//indice = Integer.parseInt(String.valueOf(session.getAttribute(WebSISPEUtil.INDICE_BUQUEDA_PEDIDO)));
				//reConstruirDetallesPedidoDesdeVista(formulario, request);
				pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				consultaPedidoDTOAux.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				consultaPedidoDTOAux.getId().setCodigoPedido(vistaPedidoDTOAux.getId().getCodigoPedido());
				consultaPedidoDTOAux.getId().setCodigoAreaTrabajo(vistaPedidoDTOAux.getId().getCodigoAreaTrabajo());
				consultaPedidoDTOAux.setEntidadResponsable(vistaPedidoDTOAux.getEntidadResponsable()); //BOD o LOC
				//se verifica si la consulta se debe filtrar solo los pedidos en el estado actual
				consultaPedidoDTOAux.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				//obtiene solamente los registros en el rango establecido
				colVistaPedidoDTOAux = (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				Collection<VistaPedidoDTO> vistaPedidoTotal= (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
				for(VistaPedidoDTO vistaDTO : vistaPedidoTotal){
					if(vistaDTO.getId().getCodigoPedido().equals(vistaPedidoDTOAux.getId().getCodigoPedido())){
						vistaPedidoTotal.remove(vistaDTO);
						break;
					}
				}
				vistaPedidoTotal.add((VistaPedidoDTO)CollectionUtils.get(colVistaPedidoDTOAux, 0));
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL, vistaPedidoTotal);
				pedidos=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTOAux);
				consultarDescuentos=Boolean.TRUE;
				session.removeAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
				//verificar si debe cargar el pedido en solo lectura
				String soloLectura=(String)request.getParameter("soloLectura");
				if(soloLectura!=null){
					 if(soloLectura.equals("SI")){
						 session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,"CONSOLIDAR"); 
					 }
					 else{
						 session.removeAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
					 }
				}
			}else{
				//se obtiene la colecci\u00F3n de los pedidos 
				pedidos = (List<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				if(request.getParameter("indice")!=null){
					if(((String)request.getParameter("indice")).equals("ok")){
						
						String codigoPedido=(String)request.getParameter("codigoPedido");
				       
						//obtiene solamente los registros en el rango establecido
						colVistaPedidoDTOAux = (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
						if(CollectionUtils.isNotEmpty(colVistaPedidoDTOAux)){
							pedidos=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTOAux);
							
							int i=0;
							for(VistaPedidoDTO visDto : colVistaPedidoDTOAux){
								if(visDto.getId().getCodigoPedido().equals(codigoPedido)){
									indice = i;
									break;
								}
								i++;
							}
						}
						
						 consultarDescuentos=Boolean.FALSE;
						 session.removeAttribute("ec.com.smx.sic.sispe.pedioGeneral"); 
						 session.removeAttribute(CotizarReservarAction.CAMBIO_PESO);
						 //verificar si debe cargar el pedido en solo lectura
						 String soloLectura=(String)request.getParameter("soloLectura");
						 if(soloLectura!=null){
							 if(soloLectura.equals("SI")){
								 session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,"CONSOLIDAR"); 
							 }
							 else{
								 session.removeAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
							 }
						 }
					}
					else{
						indice = Integer.parseInt(request.getParameter("indice"));
					}
				}else{
					//para el caso de la modificaci\u00F3n de una reservaci\u00F3n
					indice = Integer.parseInt((String)session.getAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR));
					//se elimina la variable
					session.removeAttribute(ListaModificarReservacionAction.INDICE_RESERVA_MODIFICAR);
				}
			}
			//creaci\u00F3n del DTO para almacenar el pedido selecionado
			vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
			detalleVistaPedido = (List<VistaDetallePedidoDTO>) vistaPedidoDTO.getVistaDetallesPedidos();
			detallePedido = (List<DetallePedidoDTO>) vistaPedidoDTO.getDetallesPedidosSeleccionados();
			detallePedido1 = (List<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedido);
			String accionConsolidar=(String)session.getAttribute(WebSISPEUtil.ACCION_CONSOLIDAR); 
			if(accionConsolidar==null){
				//se guarda en sesi\u00F3n el pedido seleccionado
				session.setAttribute("ec.com.smx.sic.sispe.vistaPedidoDTOActual", vistaPedidoDTO);
			}
			//se guarda en sesi\u00F3n el pedido seleccionado
			session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
			session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_AUX, vistaPedidoDTO.clone());
			//se guarda el c\u00F3digo del local
			session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			//se guarda el c\u00F3digo de establecimiento
			session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaPedidoDTO.getCodigoEstablecimiento());
			//numero usado para consolidar en el caso que aun no este consolidado
			formulario.setNumeroAConsolidar(vistaPedidoDTO.getId().getCodigoPedido());
			
			//crear coleccion inicial de listado de empresas consolidadas
			if(consultarDescuentos){
				Collection<VistaPedidoDTO> vistaPedidoCol=new ArrayList<VistaPedidoDTO>();
				vistaPedidoCol.add(vistaPedidoDTO);
				session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO_EMPRESAS, vistaPedidoCol);
			}
			
			//se verifica si el usuario logeado es de la bodega
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){      
				session.setAttribute(CotizarReservarAction.ES_ENTIDAD_BODEGA,"ok");

				//se cargan los datos del local
				LocalID localID = new LocalID();
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				localID.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);

				if(localDTO != null){
					//se guarda el telefono y el nombre del administrador del local
					session.setAttribute(CotizarReservarAction.TELEFONO_LOCAL, localDTO.getTelefonoLocal());
					session.setAttribute(CotizarReservarAction.ADMINISTRADOR_LOCAL, localDTO.getNombreAdministrador());
				}
			}

			session.setAttribute(EntregaLocalCalendarioAction.NOMBREENTIDADRESPONSABLE,vistaPedidoDTO.getEntidadResponsable());
			
			//se verifica con que precios se guard\u00F3 el pedido
			if(vistaPedidoDTO.getEstadoPreciosAfiliado()!=null && vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoActivo)){
				formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
				session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
			}else{
				//se verifica con que precios se guard\u00F3 el pedido
				if(session.getAttribute(CALCULO_PRECIOS_AFILIADO) != null && ((String)session.getAttribute(CALCULO_PRECIOS_AFILIADO)).equals("ok")){
					formulario.setCheckCalculosPreciosAfiliado(estadoActivo);
					vistaPedidoDTO.setEstadoPreciosAfiliado(estadoActivo);
				}else{
					formulario.setCheckCalculosPreciosAfiliado(estadoInactivo);
					vistaPedidoDTO.setEstadoPreciosAfiliado(estadoInactivo);
					session.removeAttribute(CALCULO_PRECIOS_AFILIADO);
				}
			}
			
			//se verifica si los calculos se deben realizar con iva o sin iva por el ruc de la empresa
			CotizacionReservacionUtil.verificarEmpresaExentaIVA(request, vistaPedidoDTO.getRucEmpresa(), vistaPedidoDTO.getContextoPedido());
			//se carga la configuraci\u00F3n de los descuentos
			CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, estadoActivo);
			
			Boolean despacho = CotizacionReservacionUtil.validarFechasDespacho(session, detalleVistaPedido);
			
			//si existen despachos y si es la opci\u00F3n modificar reserva
			if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){			
				session.setAttribute(ModificarReservacionAction.MOSTRAR_POPUP_DESPACHO,"ok");
			}
			
			//contador para los precios alterados
			int contadorPreciosAlterados = 0;
			
			//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
			WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
			
			//se recorre nuevamente el detalle del pedido, ahora para verificar su stock
			for(DetallePedidoDTO detallePedidoDTO : detallePedido){
				
				if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null 
						&& detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue() < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
					//se asigna el estado correspondiente al estado del stock
					detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);
					//stock obsoleto
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs() != null){
						if(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs()!=0 && detallePedidoDTO.getArticuloDTO().getClaseArticulo().equals("O")){
							detallePedidoDTO.getArticuloDTO().setNpStockArticulo(detallePedidoDTO.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());				
						}
					}
				}
				
			}

			//se verifica tambi\u00E9n el estado del pago en caso de la modificaci\u00F3n de una reservaci\u00F3n
			//si esta pagado totalmente o liquidado no se debe realizar ning\u00FAn recalculo adicional
			if(contadorPreciosAlterados > 0 && !vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))
					&& !vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))){
				//reprocesarDescuentos = true;
				session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS, "SI");
			}		
			
			//Obtener los pedidos consolidados para valver a calcular y ver si se procesa o no los descuentos
			Collection<DetallePedidoDTO> detallePedidoConsolidado = null;
			if(vistaPedidoDTO.getCodigoConsolidado()!=null && !vistaPedidoDTO.getCodigoConsolidado().equals("")){
				Collection<VistaPedidoDTO> colVistaPedidoDTO= (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
				if(consultarDescuentos==Boolean.TRUE){
					VistaPedidoDTO consultaPedidoDTO = new VistaPedidoDTO();
					consultaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					consultaPedidoDTO.setCodigoConsolidado(vistaPedidoDTO.getCodigoConsolidado()); 
					consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
					consultaPedidoDTO.setDescuentosEstadosPedidos(new ArrayList<DescuentoEstadoPedidoDTO>()); 
					colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
					LogSISPE.getLog().info("TAMANO DE PEDIDOS CONSOLIDADOS DESDE BUSQUEDAS: {}",colVistaPedidoDTO.size());
				}
				
				//obtener todos los pedidos consolidados
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS,colVistaPedidoDTO);
				
				//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
				
				detallePedidoConsolidado = agregarPedidosConsolidados(formulario,	request, session, detallePedido, vistaPedidoDTO,
						 colVistaPedidoDTO, errors);
				
				Collection<VistaPedidoDTO> vistaPedidoTotal=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTO.size()+1);
				vistaPedidoTotal.addAll(colVistaPedidoDTO);
				session.setAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL,vistaPedidoTotal);
				formulario.setDatosConsolidadosTotal(vistaPedidoTotal);
			}
			else{
				//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request,Boolean.FALSE);
				
				if(formulario.getNumeroAConsolidar()!=null){
					Collection<VistaPedidoDTO> vistaPedidoTotal= (Collection<VistaPedidoDTO>) session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS_TOTAL);
					if(vistaPedidoTotal!=null && vistaPedidoTotal.size()>0){
						vistaPedidoDTO.setCodigoConsolidado(formulario.getNumeroAConsolidar());
						detallePedidoConsolidado = agregarPedidosConsolidados(formulario,	request, session, detallePedido, vistaPedidoDTO,
								vistaPedidoTotal, errors);
					}
					else{
						formulario.setNumeroPedidoConsolidado(null);
						formulario.setDatosConsolidados(null);
					}
				}
				else{
					formulario.setNumeroPedidoConsolidado(null);
					formulario.setDatosConsolidados(null);
				}
			}
			//se recalcula los precios del pedido actual una vez que se carg\u00F3 los pedidos consolidados
			//lamada al m\u00E9todo que determina los totales por detalle     
			for(DetallePedidoDTO detallePedidoDTORecorrido : detallePedido){
				
				if(detallePedidoDTORecorrido.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
					CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTORecorrido, request, false,false);
				}
			}
			
			//llamada a la funcion que realiza el procesamiento de los descuentos
			DescuentoEstadoPedidoDTO descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
			//Aplicar descuentos a los pedidos consolidados
			if(detallePedidoConsolidado!=null){

				Collection<DetallePedidoDTO> detallePedidoGeneral=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
				Collection<DetallePedidoDTO> detallePedidoProcesar=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
				for(DetallePedidoDTO detPedDTO : detallePedidoConsolidado){
					detallePedidoProcesar.add((DetallePedidoDTO)SerializationUtils.clone(detPedDTO));
				}
				
				detallePedidoGeneral.add((DetallePedidoDTO)detallePedidoProcesar.iterator().next());
				int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
				Boolean existeArt= Boolean.FALSE;
				int i=0;
				for (DetallePedidoDTO detallePedidoDTO:detallePedidoProcesar){
					Long cantidadTotalConsolidada=0L;
					Double valorTotalConsolidada=0D;
					existeArt= Boolean.FALSE;
					for (DetallePedidoDTO detallePedidoConsolidadoDTO:detallePedidoGeneral){
						if(detallePedidoDTO!= null && detallePedidoConsolidadoDTO!=null){
							
							if(i>0 && detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
								cantidadTotalConsolidada=(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
								valorTotalConsolidada=Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
								valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
										detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
								cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());

								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(valorTotalConsolidada);
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalConsolidada);
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
								existeArt= Boolean.TRUE;
								break;
							}
						}
					}
					
					if(i>0 && existeArt== Boolean.FALSE){
						detallePedidoGeneral.add(detallePedidoDTO);
					}
					i++;
				}
				
				for(DetallePedidoDTO detPedGen:detallePedidoGeneral){
					existeArt= Boolean.FALSE;
					for(DetallePedidoDTO detPed:detallePedido){
						if(detPedGen.getId().getCodigoArticulo().equals(detPed.getId().getCodigoArticulo())){
							existeArt= Boolean.TRUE;
						}
					}
					if(!existeArt){
						boolean esActivoPrecioMayorista = false;
						if(detPedGen.getArticuloDTO().getHabilitadoPrecioMayoreo()){
							esActivoPrecioMayorista = true;
						}
						if(!esActivoPrecioMayorista && detPedGen.getArticuloDTO().getHabilitadoPrecioCaja() && detPedGen.getArticuloDTO().getPrecioCaja() > 0){
							if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null){
								detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado());
								detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorTotalEstado());
							}else{
								detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
								detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
							}
						}else{
							detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
							detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
						}
						detPedGen.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
						detPedGen.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
						detallePedido.add(detPedGen);
					}
				}
				
				//se inicializan las sumatorias
				double valorDescuentoTotal = 0;
				double porcentajeTotalDescuento = 0;
				//obtener llaves de descuentos y valores de descuentos variables 
				CotizacionReservacionUtil.obtenerLLavesDescuentos(request, session, false);
				
				Collection<String> llaveDescuentoCol = (Collection<String>)session.getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
				Collection<DescuentoDTO> descuentoVariableCol = (Collection<DescuentoDTO>)session.getAttribute(CotizarReservarAction.COL_DESC_VARIABLES);
				
					Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = new ArrayList<DescuentoEstadoPedidoDTO>();
					if(llaveDescuentoCol!= null && llaveDescuentoCol.isEmpty()){
						llaveDescuentoCol= null;
					}
					if(descuentoVariableCol!= null && descuentoVariableCol.isEmpty()){
						descuentoVariableCol= null;
					}
					
					if(session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") != null && ((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios")).equals("SI")){
						//Actualizar los precios de los detalles consolidados a procesar
						actualizarDetallePedidosConsolidados(request, estadoActivo, estadoInactivo,vistaPedidoDTO.getRucEmpresa(),vistaPedidoDTO.getTipoDocumentoCliente(),detallePedido);
						//lamada al m\u00E9todo que determina los totales por detalle     
						
						for(DetallePedidoDTO detallePedidoDTORecorrido : detallePedido){
							 
							if(detallePedidoDTORecorrido.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
								CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTORecorrido, request, false,false);
							}
							else{
								if(detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
									//recorre cada item de la canasta
									for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol())){
										recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0L);
										recetaArticuloDTO.setValorPrevioEstadoDescuento(0D);
									}
								}else{
									//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
									detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
									detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
								}
							}
						}
					}
					
					Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTOVariables = (Collection<DescuentoEstadoPedidoDTO>) session.getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
					
					if(CollectionUtils.isNotEmpty(colDescuentoEstadoPedidoDTOVariables)){
						if(CollectionUtils.isEmpty(llaveDescuentoCol))
							llaveDescuentoCol = new ArrayList<String>();
						for(DescuentoEstadoPedidoDTO dsctoVariable : colDescuentoEstadoPedidoDTOVariables){
							if(dsctoVariable.getLlaveDescuento() != null && !dsctoVariable.getLlaveDescuento().equals("") 
									&& !llaveDescuentoCol.contains(dsctoVariable.getLlaveDescuento())){
								llaveDescuentoCol.add(dsctoVariable.getLlaveDescuento());
							}
						}
					}
					
					//cuando no ingresa por la opcion de consolidacion, debe cargarse la coleccion de detalles consolidados con autorizaciones
					if(accionActual != null && !accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
						CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, detallePedido);
					}
					//colDescuentoEstadoPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallePedido, llaveDescuentoCol, descuentoVariableCol);
					colDescuentoEstadoPedidoDTO = CotizacionReservacionUtil.procesarDescuentosPorTipo(request, detallePedido, llaveDescuentoCol);
					
					//si es pedido consolidado se aplica los valores del descuento variable a las autorizaciones
					if(esPedidoConsolidado(request)){
						AutorizacionesUtil.aplicarEstadoDescuentoVariableAutorizacionesPedido(request, detallePedido);
						
						//si viene desde la recotizacion se sube a sesion los DETALLES_CONSOLIDADOS_ACTUALES con autorizaciones
						if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null 
								&& (request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))
								|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
								|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))
								|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")))){
							CotizacionReservacionUtil.obtenerDetallesAutorizacionesActuales(request, null);
						}
					}
					//eliminar los detalles que no corresponden al pedido
					List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
					for(DetallePedidoDTO detPed:detallePedido){
						if(!vistaPedidoDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
							detallesEliminar.add(detPed);
						}
					}
					detallePedido.removeAll(detallesEliminar);
					if(colDescuentoEstadoPedidoDTO!=null && !colDescuentoEstadoPedidoDTO.isEmpty()){
						LogSISPE.getLog().info("descuentos: {}",colDescuentoEstadoPedidoDTO.size());
						//si se alteraron los precios se itera la colecci\u00F3n de descuentos para calcular el total de descuentos
						for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : colDescuentoEstadoPedidoDTO){
							
							valorDescuentoTotal = valorDescuentoTotal + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							porcentajeTotalDescuento = porcentajeTotalDescuento + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
						}
						LogSISPE.getLog().info("porcentajeTotalDescuento: {}",porcentajeTotalDescuento);
					}
					//se inicializa el objeto resultado
					descuentoEstadoPedidoTotalDTO = new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoTotalDTO.setPorcentajeDescuento(Double.valueOf(porcentajeTotalDescuento));
					descuentoEstadoPedidoTotalDTO.setValorDescuento(Double.valueOf(valorDescuentoTotal));
					//se almacena el descuento total y el descuento para los art\u00EDculos con IVA
					session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(valorDescuentoTotal));
					//se guarda en sesi\u00F3n el porcentaje total de los descuentos
					session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO, Double.valueOf(porcentajeTotalDescuento));
					session.setAttribute(CotizarReservarAction.COL_DESCUENTOS,colDescuentoEstadoPedidoDTO);
//				}
					
			}else{
				//Tiene precios alterados, primero actualiza los precios despues deben procesarse los descuentos
				if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS) == null){	
					descuentoEstadoPedidoTotalDTO = CotizacionReservacionUtil.procesarDescuentos(vistaPedidoDTO, detallePedido, true, request);
				}
				else {
//					//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
					descuentoEstadoPedidoTotalDTO = CotizacionReservacionUtil.procesarDescuentos(vistaPedidoDTO, detallePedido, false, request);
					//se verifica la existencia de una colecci\u00F3n de descuentos
					if(vistaPedidoDTO.getDescuentosEstadosPedidos()!=null && !vistaPedidoDTO.getDescuentosEstadosPedidos().isEmpty()){
						
						Collection<DescuentoEstadoPedidoDTO> descuentos = vistaPedidoDTO.getDescuentosEstadosPedidos();												
						Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
						
						for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentos){
							
							//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
							if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null)
								llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());						
						}
						
						if(!llavesDescuentosAplicados.isEmpty()){
							
							//se generan las llaves para setearlas en opDescuentos del formulario
							Collection<TipoDescuentoDTO> tipoDescuento = (Collection<TipoDescuentoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_TIPO_DESCUENTO);					
							String[] opDescuentos = new String[llavesDescuentosAplicados.size()];
							int posVector = 0;
							int posDecuento = 0;					
							for(TipoDescuentoDTO tipoDctoActual : tipoDescuento){
								for(String llaveActual : llavesDescuentosAplicados){
									String nuevaLlave = llaveActual;
									String codigoTipoDescuento = llaveActual.split(SEPARADOR_TOKEN)[1].split("CTD")[1];
									if(tipoDctoActual.getId().getCodigoTipoDescuento().equals(codigoTipoDescuento)){
										nuevaLlave =  nuevaLlave.replace(llaveActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, ""+posDecuento+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO);
										opDescuentos[posVector] = nuevaLlave;
										posVector++;
									}
								}
								posDecuento++;
							}
							request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS, opDescuentos);
							
							//sube a sesion variables necesarias para procesar descuentos
							CotizarReservarAction.buscarDetallesConDctoAutomaticos((ArrayList<DetallePedidoDTO>) detallePedido,  request);		
						}	
					}
					
				}	
			}
			
			//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
			CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
			
			LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
			
			//obtengo el codigoTipDesMax-navidad desde un parametro
			CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
			
			//validacion check pago en efectivo
			if(vistaPedidoDTO.getPagoEfectivo() != null && vistaPedidoDTO.getPagoEfectivo().equals(estadoActivo)){
				//Activa el check Pago en Efectivo
				formulario.setCheckPagoEfectivo(estadoActivo);
				//Presenta el tipo de descuento maxi-navidad de la seccion de descuentos
				session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
			}else if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null){
				formulario.setCheckPagoEfectivo(estadoActivo);
				session.setAttribute(CHECK_PAGO_EFECTIVO, "ok");
			}
			else if(vistaPedidoDTO.getPagoEfectivo() != null && vistaPedidoDTO.getPagoEfectivo().equals(estadoInactivo) && session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) == null){
				formulario.setCheckPagoEfectivo(null);
				session.removeAttribute(CHECK_PAGO_EFECTIVO);
			}else{
				formulario.setCheckPagoEfectivo(null);
				session.removeAttribute(CHECK_PAGO_EFECTIVO);
			}

			//se limpia el valor del abono
			formulario.setValorAbono(null);
			//se asigna el contexto del pedido al formulario
			if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
				formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
			}else{
				formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
			}

			formulario.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
			if(vistaPedidoDTO.getNumBonNavEmp()!=null){
				formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp().toString());
			}

			formulario.setOpTipoEspeciales("0");
			formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
			formulario.setOpAutorizacion(estadoInactivo);
			
			/*------------------- se asignan los atributos necesarios al formulario --------------------*/
			if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
					|| vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
				if(vistaPedidoDTO.getContactoEmpresa().startsWith(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
					formulario.setNumeroDocumento(vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
					session.setAttribute(ContactoUtil.RUC_PERSONA, vistaPedidoDTO.getContactoEmpresa().substring(4, 18).trim());
					formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				}else{
					formulario.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
				}
			}
			else 
				if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) ||
					vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
				
				request.getSession().setAttribute(ContactoUtil.LOC_GUARDADA, vistaPedidoDTO.getCodigoLocalizacion());
				formulario.setNumeroDocumento(vistaPedidoDTO.getRucEmpresa());
			}

			//se asigna el local responsable, adicionalmente se agrega 
			formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()
					+ " " + SEPARADOR_TOKEN + " " + vistaPedidoDTO.getNombreLocal());
			LogSISPE.getLog().info("LOCAL RESPONSABLE OBTENIDO: {}",formulario.getLocalResponsable());

			//se inicializa el costo del flete
	    //se asigna el costo del flete
			if(vistaPedidoDTO.getValorCostoEntregaPedido()!=null){
				formulario.setCostoFlete(vistaPedidoDTO.getValorCostoEntregaPedido());
			}else{
				formulario.setCostoFlete(0D);
			}
			formulario.setDescuentoTotal(descuentoEstadoPedidoTotalDTO.getValorDescuento());
			
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO_VALIDAR_NUMBONOS, detallePedido);
			session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO,detallePedido);
			//se recalculan los valores del pedido completamente
			CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
			
			session.setAttribute(SessionManagerSISPE.DETALLE_PEDIDO_ORIGINAL,detallePedido1);
			
			session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS,codigosArticulos);
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
			//esta variable indica que la reservaci\u00F3n est\u00E1 en proceso
			session.setAttribute(CotizarReservarAction.PEDIDO_EN_PROCESO,"ok");
			//indica que la b\u00FAsqueda es en la pantalla principal
			session.setAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL,"ok");

			//---------------- se cargan todos los especiales -------------------
			//creaci\u00F3n del DTO para el art\u00EDculo
			EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
			especialDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
			especialDTO.setNpCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			especialDTO.setEstadoEspecial(estadoActivo);
			especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			especialDTO.setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
			
			ArrayList<EspecialDTO> especiales = (ArrayList<EspecialDTO>)session.getAttribute(CotizarReservarAction.COL_TIPO_ESPECIALES);
			if(CollectionUtils.isEmpty(especiales)){
				especiales = (ArrayList<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloEspecial(especialDTO);
				session.setAttribute(CotizarReservarAction.COL_TIPO_ESPECIALES,especiales);
				session.setAttribute(CotizarReservarAction.COL_ART_ESPECIALES,((EspecialDTO)especiales.get(0)).getArticulos());
			}
				
			//se inicializa la sub-p\u00E1gina que se mostrar\u00E1
			session.setAttribute(CotizarReservarAction.SUB_PAGINA,"cotizarRecotizarReservar/detallePedido.jsp");

			//se inicializan las variables para el control de la acci\u00F3n CotizarReservarAction
			session.setAttribute(CotizarReservarAction.ACCION_ANTERIOR, "");
			session.setAttribute(CotizarReservarAction.LOCAL_ANTERIOR, "");

			//se desactiva el men\u00FA de opciones
			MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);

			//se llama al m\u00E9todo que verifica la posibilidad que tiene el establecimiento
			//para cambiar el precio
			CotizacionReservacionUtil.verificarFormatoNegocioParaCambioPrecios(vistaPedidoDTO.getCodigoEstablecimiento(), request);
			
			//eliminar entregas solo cuando la opcion es diferente de registro de pesos finales
			//se comenta porque no deveria eliminarse las entregas, porque puede darse el caso que solo vas a agregar un descuento y todas las entregas quedarian igual
			boolean eliminoEntregas =Boolean.FALSE;
			if(!accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
				eliminoEntregas = formulario.cargarConfiguracionInicial(new ActionErrors(), request);
			}
			
			//se procesan las entregas para verificar si se activa el transito o no
			EntregaLocalCalendarioUtil.procesarEntregasParaVerificarTransito(request, formulario);
			
			LogSISPE.getLog().info("PRECIOS ALTERADOS: {}",session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS));
			//se verifica si hubo cambio en los precios
			if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS) != null){
				if(detallePedidoConsolidado==null){
					//se crea la ventana que se indicar\u00E1 el cambio de precios
					UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Precios y recetas modificados");
					popUp.setMensajeVentana("Algunos precios y detalles de recetas son DIFERENTES a los guardados inicialmente, " +
							"si usted desea puede actualizar estos datos haciendo click en SI, si desea conservar los anteriores haga click en NO, " +
						"posteriormente usted puede cambiar estos datos activando la opci\u00F3n <b>\"" + MessagesWebSISPE.getString("label.calculosConDatosActuales") + "\"</b> y haciendo clic en el bot\u00F3n <b>\"Actualizar\"</b>");
					popUp.setPreguntaVentana("\u00BFDesea actualizar el valor de los datos?");
					popUp.setEtiquetaBotonOK("Si");
					popUp.setEtiquetaBotonCANCEL("No");
					//popUp.setValorOK(PRECIOS_ACTUALES);
					if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
						popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&intercambioPrecios=ok&preciosActuales=ok'});");
						popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarActualizarPrecios=ok', popWait:false, evalScripts:true});");
					}else{
						popUp.setValorOK("requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&intercambioPrecios=ok&preciosActuales=ok'});ocultarModal();");
						popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta','div_pagina','mensajes'], {parameters: 'cancelarActualizarPrecios=ok', popWait:false, evalScripts:true});ocultarModal();");
					}				
					popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
					popUp.setAncho(Double.valueOf(60));
					popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
					popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
					
					//se guarda la ventana
					request.setAttribute(SessionManagerSISPE.POPUP, popUp);
				}
				else{
					//verificacion si los pedidos consolidados aplican precio de afiliados o no
					session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS,"CAMBIO_PRECIO");
					//se verifica si hubo cambio en los precios
					String actualizarPrecios=((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios"));
					LogSISPE.getLog().info("ACTUALIZAR PRECIOS: {}",actualizarPrecios);
					if(actualizarPrecios==null){
						if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS) != null && !((String)session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS)).equals("ok")){
							session.removeAttribute(SessionManagerSISPE.POPUP);
							session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS,"ok");
							instanciarVentanaModificacionPreciosConsolidados(request);
						}
					}
				}
			}else{
				//si existen despachos y si es la opci\u00F3n modificar reserva
				if(despacho && session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
					CotizacionReservacionUtil.instanciarVentanaEntregaDespachada(request);
				}
			}
			
			//se elimina de sesion el parametro
			session.removeAttribute(GlobalsStatics.VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
			
			return eliminoEntregas;
		}
		
		
		/**
		 * 
		 * @param detallePedidoDTO
		 * @return true si el articulo es del tipo peso variable, else caso contrario
		 */
		private static boolean esTipoArticuloPeso(DetallePedidoDTO detallePedidoDTO){
			
			if(detallePedidoDTO != null && detallePedidoDTO.getArticuloDTO() != null){
				
				String valorTipoControl = detallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().getValorTipoControlCosto();  
				
				if(valorTipoControl != null && (valorTipoControl.equals(SICArticuloConstantes.TIPCONCOS_PIEPES)
						|| valorTipoControl.equals(SICArticuloConstantes.TIPCONCOS_PESPES)
						|| valorTipoControl.equals(SICArticuloConstantes.TIPCONCOS_PIEPESUM))){
					
					LogSISPE.getLog().info("el articulo "+detallePedidoDTO.getArticuloDTO().getDescripcionArticulo()+" es de peso variable");
					return true;
				}
			}
			
			return false;
		}
		
		
		/**
		 * busca y remplaza los detalles del pedido actual en los detalles consolidados
		 * @param request
		 * @param detallePedidoCol
		 * @throws Exception
		 */
		public static void actualizarDetallesModificadosEnConsolidados(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoCol) throws Exception{
			
			HttpSession session = request.getSession();
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado) && CollectionUtils.isNotEmpty(detallePedidoCol)){
				
				//Se eliminan todos los detalles del pedido actual de la coleccion consolidada
				String codigoPedido = detallePedidoCol.iterator().next().getId().getCodigoPedido();
				Collection<DetallePedidoDTO> detallesEliminar = new ArrayList<DetallePedidoDTO>();
				
				for(DetallePedidoDTO detalleConsolidado : detallePedidoConsolidado){
					
					if(detalleConsolidado.getId().getCodigoPedido().equals(codigoPedido)){
					
						detallesEliminar.add(detalleConsolidado);
					}
				}
				//se eliminan los detalles de ese pedido
				detallePedidoConsolidado.removeAll(detallesEliminar);
				
				//se agregan los nuevos detalles
				detallePedidoConsolidado.addAll((Collection<DetallePedidoDTO>)SerializationUtils.clone((Serializable)detallePedidoCol));
				
				//se actualiza el pedido sumarizado
				crearDetalleConsolidadosNoRepetidos(request, detallePedidoConsolidado);
			}
		}
		
		
		/**
		 * Busca el detalle pedido en los detalles consolidados y lo remplaza por el detalleModificado
		 * @param request
		 * @param detallePedidoDTOModificado
		 * @throws Exception
		 */
		public static void actualizarDetalleModificadoEnConsolidados(HttpServletRequest request, DetallePedidoDTO detallePedidoDTOModificado) throws Exception{
			
			HttpSession session = request.getSession();
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado) && detallePedidoDTOModificado != null){
				
				for(DetallePedidoDTO detalleConsolidado : detallePedidoConsolidado){
					
					if(detalleConsolidado.getId().getCodigoPedido().equals(detallePedidoDTOModificado.getId().getCodigoPedido())
							&& detalleConsolidado.getId().getCodigoArticulo().equals(detallePedidoDTOModificado.getId().getCodigoArticulo())){
					
						LogSISPE.getLog().info("detalle por actualizar: "+detalleConsolidado.getArticuloDTO().getDescripcionArticulo());
						detallePedidoConsolidado.remove(detalleConsolidado);
						break;
					}
				}
				
				//se agregan los nuevos detalles
				detallePedidoConsolidado.add(SerializationUtils.clone(detallePedidoDTOModificado));
				
				//se actualiza el pedido sumarizado
				crearDetalleConsolidadosNoRepetidos(request, detallePedidoConsolidado);
			}
		}
		
		
		/**
		 * @param numeroDecimales
		 * @param detallePedidoConsolidado
		 * @return
		 */
		public static Collection<DetallePedidoDTO> clonarDetallePedidoConsolidado(int numeroDecimales, List<DetallePedidoDTO> detallePedidoConsolidado) {
			
			Collection<DetallePedidoDTO> detallePedidoGeneral=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
			Collection<DetallePedidoDTO> detallePedidoProcesar=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
			
			for(DetallePedidoDTO detPedDTO:detallePedidoConsolidado){
				detallePedidoProcesar.add((DetallePedidoDTO)SerializationUtils.clone(detPedDTO.clone()));
			}
			
			detallePedidoGeneral.add((DetallePedidoDTO)SerializationUtils.clone(detallePedidoConsolidado.get(0)));

			Boolean existeArt= Boolean.FALSE;
			int i=0;
			for (DetallePedidoDTO detallePedidoDTO:detallePedidoProcesar){
				Long cantidadTotalConsolidada=0L;
				Double valorTotalConsolidada=0D;
				existeArt= Boolean.FALSE;
				for (DetallePedidoDTO detallePedidoConsolidadoDTO:detallePedidoGeneral){
					if(detallePedidoDTO != null && detallePedidoConsolidadoDTO != null){
						
						if(i>0 && detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
							
							cantidadTotalConsolidada=(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
							valorTotalConsolidada=Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
							valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
									detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
							cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());


							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(valorTotalConsolidada);
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalConsolidada);
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
							existeArt = Boolean.TRUE;
							break;
						}
					}
				}
				
				if(i>0 && existeArt== Boolean.FALSE){
					detallePedidoGeneral.add(detallePedidoDTO);
				}
				i++;
			}
			return detallePedidoGeneral;
		}
		
		
		/**
		 * @param formulario
		 * @param request
		 * @param session
		 * @param detallePedido
		 * @param vistaPedidoDTO
		 * @param detallePedidoConsolidado
		 * @param colVistaPedidoDTO
		 * @return
		 * @throws Exception
		 */
		public static Collection<DetallePedidoDTO> agregarPedidosConsolidados(CotizarReservarForm formulario, HttpServletRequest request, HttpSession session, List<DetallePedidoDTO> detallePedido,
				VistaPedidoDTO vistaPedidoDTO, Collection<VistaPedidoDTO> colVistaPedidoDTO, ActionMessages errors) throws Exception {
			
			Collection<DetallePedidoDTO> detallePedidoConsolidado = new ArrayList<DetallePedidoDTO>();
			VistaPedidoDTO vistaPedidoDTOConsolidado= new VistaPedidoDTO();
			vistaPedidoDTOConsolidado.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
			vistaPedidoDTOConsolidado.getId().setCodigoEstado("CSD");
			vistaPedidoDTOConsolidado.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
			vistaPedidoDTOConsolidado.getId().setCodigoPedido(vistaPedidoDTO.getCodigoConsolidado());
			//Excluir el pedido seleccionado
			Collection<VistaPedidoDTO> colVistaPedidoActualDTO=new ArrayList<VistaPedidoDTO>(colVistaPedidoDTO.size());
			 for(VistaPedidoDTO visPedDTO:colVistaPedidoDTO){
				 if(!visPedDTO.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
					 colVistaPedidoActualDTO.add(visPedDTO);
					 LogSISPE.getLog().info("PEDIDO AGREGADO: {}",visPedDTO.getId().getCodigoPedido());
				 }
			 }

			vistaPedidoDTOConsolidado.setVistaPedidoDTOCol(colVistaPedidoDTO);
			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.consolidarDTO",vistaPedidoDTOConsolidado);
			Collection<VistaPedidoDTO> listDatosConsolidados= new ArrayList<VistaPedidoDTO>();
			listDatosConsolidados.add(vistaPedidoDTOConsolidado);
			String []validarPedidos=new String[listDatosConsolidados.size()+1];
			String []numeroPedidoConsolidado=new String[listDatosConsolidados.size()+1];
			if(CollectionUtils.isNotEmpty(listDatosConsolidados)){
				VistaPedidoDTO visPedDTO=listDatosConsolidados.iterator().next();
//			for(VistaPedidoDTO visPedDTO:listDatosConsolidados){
				LogSISPE.getLog().info("codigoPedido {}",visPedDTO.getId().getCodigoPedido());
				validarPedidos[0]=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
				numeroPedidoConsolidado[0]="La cotizaci\u00F3n se asociar\u00E1 al No consolidado: "+visPedDTO.getId().getCodigoPedido();
				formulario.setNumeroPedidoConsolidado(visPedDTO.getId().getCodigoPedido());
			}
			formulario.setPedidosValidados(validarPedidos);
			formulario.setNumeroConsolidado(numeroPedidoConsolidado);
			formulario.setDatosConsolidados(listDatosConsolidados);
			if(formulario.getDatosConsolidados().size()>0){
				  LogSISPE.getLog().info("Datos consolidados");
				//se obtiene la colecci\u00F3n de los pedidos 
					List<VistaPedidoDTO> pedidosConsolidados =(List<VistaPedidoDTO>) formulario.getDatosConsolidados();
					LogSISPE.getLog().info("total de pedidos consolidados Util: {}",pedidosConsolidados.size());
					//creaci\u00F3n del DTO para almacenar el pedido selecionado
					VistaPedidoDTO vistaPedidoActDTO = (VistaPedidoDTO)pedidosConsolidados.get(0);
					//obtener todos los pedidos consolidados
					List<DetallePedidoDTO> detallesPedidoConsolidado=(List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
					if(request.getSession().getAttribute(CotizarReservarAction.SIN_DESCUENTOS_CONSOLIDADO)!=null && ((Boolean)request.getSession().getAttribute(CotizarReservarAction.SIN_DESCUENTOS_CONSOLIDADO)) &&
							((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null || 
							((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null ){
						detallesPedidoConsolidado=null;
						request.getSession().setAttribute(CotizarReservarAction.SIN_DESCUENTOS_CONSOLIDADO,Boolean.FALSE);
					}
					
					if(detallesPedidoConsolidado==null || detallesPedidoConsolidado.isEmpty()){
						Collection<VistaPedidoDTO> visPedColConsAux = vistaPedidoActDTO.getVistaPedidoDTOCol(); 
						detallesPedidoConsolidado=construirDetallesPedidoDesdeVistaConsolidados(formulario, request,visPedColConsAux,Boolean.TRUE,Boolean.FALSE, errors);
						//detallesPedidoConsolidado.addAll(detallePedido);
						session.setAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS,detallesPedidoConsolidado);
						
						//se crea y sube a sesion el detalle consolidado
						ConsolidarAction.crearDetalleConsolidadosNoRepetidos(request, detallesPedidoConsolidado);
					}
				  //Sumar los valores y cantidades al pedido general y para verificar si aplica o no un descuento
				  detallePedidoConsolidado = sumarValoresCantidadesPedidoGeneral(formulario, session, detallePedido);
			 }
			return detallePedidoConsolidado;
		}
		
		  
	  /**
		 * @param formulario
		 * @param session
		 * @param detallePedido
		 * @return
		 */
		public static ArrayList<DetallePedidoDTO> sumarValoresCantidadesPedidoGeneral(CotizarReservarForm formulario, HttpSession session,Collection<DetallePedidoDTO> detallePedido) {
			
			int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
			String estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			ArrayList<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			VistaPedidoDTO vistaPedidoActual= (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			if(formulario.getNumeroPedidoConsolidado()!=null){
				
				LogSISPE.getLog().info("Es un pedido consolidado y busca en el resto de pedidos para validar los descuentos");
				if(detallePedido!=null && !detallePedido.isEmpty())	{
					
					LogSISPE.getLog().info("TAMANO DETALLE PEDIDO ACTUAL: {}",detallePedido.size());
					if(detallePedidoConsolidado!=null && !detallePedidoConsolidado.isEmpty()){
						LogSISPE.getLog().info("TAMANO DETALLE PEDIDO CONSOLIDADO ACTUAL: {}",detallePedidoConsolidado.size());
						
						for(DetallePedidoDTO detallePedidoDTO : detallePedido){
							
							Long cantidadTotalConsolidada=0L;
							Double valorTotalConsolidada=0D;
						
							for (DetallePedidoDTO detallePedidoConsolidadoDTO : detallePedidoConsolidado){
								
								if(!detallePedidoConsolidadoDTO.getId().getCodigoPedido().equals(vistaPedidoActual.getId().getCodigoPedido())){
									if(detallePedidoDTO!= null && detallePedidoConsolidadoDTO!=null){
						
										if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
											//Validar si es una canasta cotizaci\u00F3n vac\u00EDa
											if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo) && 
													detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
												consolidarRecetaCanastos(session,numeroDecimales,detallePedidoDTO,detallePedidoConsolidadoDTO);
											}
												valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
														detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
												cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
											
										}
									}
								}
								
							}
					
							detallePedidoDTO.setNpCantidadTotalConsolidada(cantidadTotalConsolidada);
							detallePedidoDTO.setNpValorTotalConsolidado(valorTotalConsolidada);
						}
					} 
				}
				
				Collection<DetallePedidoDTO> detallePedidoGeneral=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
				Collection<DetallePedidoDTO> detallePedidoProcesar=new ArrayList<DetallePedidoDTO>(detallePedidoConsolidado.size());
				
				for(DetallePedidoDTO detPedDTO : detallePedidoConsolidado){
					detallePedidoProcesar.add((DetallePedidoDTO)SerializationUtils.clone(detPedDTO));
				}
				
				
				detallePedidoGeneral.add((DetallePedidoDTO)detallePedidoProcesar.iterator().next());
				Boolean existeArt= Boolean.FALSE;
				int i=0;
				for (DetallePedidoDTO detallePedidoDTO : detallePedidoProcesar){
					Long cantidadTotalConsolidada=0L;
					Double valorTotalConsolidada=0D;
					existeArt= Boolean.FALSE;
					for (DetallePedidoDTO detallePedidoConsolidadoDTO:detallePedidoGeneral){
						
						if(detallePedidoDTO != null && detallePedidoConsolidadoDTO != null){
							
							if(i>0 && detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())){
								cantidadTotalConsolidada=(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue()==0?detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadEstado():detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
								valorTotalConsolidada=Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento()==0?detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getNpValorPrevioEstadoDescuento():detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
								valorTotalConsolidada = Util.roundDoubleMath(new Double(valorTotalConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
										detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
								cantidadTotalConsolidada = cantidadTotalConsolidada + (detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue());
	
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(valorTotalConsolidada);
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(cantidadTotalConsolidada);
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
								existeArt= Boolean.TRUE;
								break;
							}
						}
					}
					
					if(i>0 && existeArt== Boolean.FALSE){
						detallePedidoGeneral.add(detallePedidoDTO);
					}
					i++;
				}
				
				for(DetallePedidoDTO detPedGen:detallePedidoGeneral){
					existeArt= Boolean.FALSE;
					for (DetallePedidoDTO detallePedidoDTO : detallePedido){
					
						if(detPedGen.getId().getCodigoArticulo().equals(detallePedidoDTO.getId().getCodigoArticulo())){
							existeArt= Boolean.TRUE;
							break;
						}
					}
					if(!existeArt){
						boolean esActivoPrecioMayorista = false;
						if(detPedGen.getArticuloDTO().getHabilitadoPrecioMayoreo()){
							esActivoPrecioMayorista = true;
						}
						if(!esActivoPrecioMayorista && detPedGen.getArticuloDTO().getHabilitadoPrecioCaja() && detPedGen.getArticuloDTO().getPrecioCaja() > 0){
							if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null){
								detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado());
								detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorTotalEstado());
							}else{
								detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
								detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
							}
						}else{
							detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
							detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
						}
						
						detPedGen.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
						detPedGen.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
						//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
						detPedGen.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(0D);
						//Validar si es una canasta cotizaci\u00F3n vac\u00EDa
						if(detPedGen.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
							for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detPedGen.getArticuloDTO().getArticuloRelacionCol())){
								
								esActivoPrecioMayorista = false;
								if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
									esActivoPrecioMayorista = true;
								}
								Long cantidadTotalRecetaConsolidada=0L;
								Double valorTotalRecetaConsolidada=0D;
								//se verifica si el art\u00EDculo tiene habilitado el precio por caja
								if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null &&!esActivoPrecioMayorista && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && recetaArticuloDTO.getArticuloRelacion().getPrecioCaja() > 0){
									Long cantEstado=detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
									Double valorPrevio=recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
									valorTotalRecetaConsolidada=cantEstado*valorPrevio;
									cantidadTotalRecetaConsolidada=cantEstado.longValue();
									recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
									recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
								}else{
									valorTotalRecetaConsolidada = Util.roundDoubleMath(new Double(valorTotalRecetaConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
											recetaArticuloDTO.getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
									cantidadTotalRecetaConsolidada = cantidadTotalRecetaConsolidada + (recetaArticuloDTO.getCantidadPrevioEstadoDescuento().longValue());
									recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
									recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
								}
								recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0L);
								recetaArticuloDTO.setValorPrevioEstadoDescuento(0D);
								//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
								recetaArticuloDTO.setNpValorPrevioEstadoDescuento(0D);
							}
						}
						detallePedido.add(detPedGen);
					}
				}
			}
			else{
				for (DetallePedidoDTO detallePedidoDTO : detallePedido){
					 
					if(detallePedidoDTO != null){
						detallePedidoDTO.setNpCantidadTotalConsolidada(0L);
						detallePedidoDTO.setNpValorTotalConsolidado(0D);
					}
				}
			}
			return detallePedidoConsolidado;
		}
		
		
		/**
		 * @param numeroDecimales
		 * @param detallePedidoDTO
		 * @param detallePedidoConsolidadoDTO
		 */
		public static void consolidarRecetaCanastos( HttpSession session,int numeroDecimales,
				DetallePedidoDTO detallePedidoDTO,
				DetallePedidoDTO detallePedidoConsolidadoDTO) {
			if(detallePedidoDTO.getArticuloDTO()!=null && detallePedidoConsolidadoDTO.getArticuloDTO()!=null){
				for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol())){
					Long cantidadTotalRecetaConsolidada=0L;
					Double valorTotalRecetaConsolidada=0D;
					
					for(ArticuloRelacionDTO recetaArticuloConsolidadoDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoConsolidadoDTO.getArticuloDTO().getArticuloRelacionCol())){
						if(recetaArticuloDTO!= null && recetaArticuloDTO.getArticuloRelacion()!=null && recetaArticuloConsolidadoDTO!=null && recetaArticuloConsolidadoDTO.getArticuloRelacion()!=null){
							LogSISPE.getLog().info("detallePedidoRecetaDTO: {}",recetaArticuloDTO.getArticuloRelacion().getId().getCodigoArticulo());
							LogSISPE.getLog().info("detallePedidoRecetaConsolidadoDTO: {}",recetaArticuloConsolidadoDTO.getArticuloRelacion().getId().getCodigoArticulo());
							if(recetaArticuloDTO.getArticuloRelacion().getId().getCodigoArticulo().equals(recetaArticuloConsolidadoDTO.getArticuloRelacion().getId().getCodigoArticulo())){
								boolean esActivoPrecioMayorista = false;
								if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
									esActivoPrecioMayorista = true;
								}
								//se verifica si el art\u00EDculo tiene habilitado el precio por caja
								if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS)) != null &&!esActivoPrecioMayorista && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && recetaArticuloDTO.getArticuloRelacion().getPrecioCaja() > 0){
									
									Long cantEstado=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
									Double valorPrevio=recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
									valorTotalRecetaConsolidada=cantEstado*valorPrevio;
									recetaArticuloDTO.setCantidadPrevioEstadoDescuento(cantEstado);
									recetaArticuloDTO.setValorPrevioEstadoDescuento(valorTotalRecetaConsolidada);
									
									cantEstado=detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
									valorPrevio=recetaArticuloConsolidadoDTO.getArticuloRelacion().getPrecioBase();
									valorTotalRecetaConsolidada=cantEstado*valorPrevio;
									cantidadTotalRecetaConsolidada=cantEstado.longValue();
									break;
									
								}else{
									valorTotalRecetaConsolidada = Util.roundDoubleMath(new Double(valorTotalRecetaConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
											recetaArticuloConsolidadoDTO.getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
									cantidadTotalRecetaConsolidada = cantidadTotalRecetaConsolidada + (recetaArticuloConsolidadoDTO.getCantidadPrevioEstadoDescuento().longValue());
									break;
								}
							}
						}
					}
					LogSISPE.getLog().info("cantidadTotalRecetaConsolidada: {}",cantidadTotalRecetaConsolidada);
					LogSISPE.getLog().info("valorTotalRecetaConsolidada: {}",valorTotalRecetaConsolidada);
					recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
					recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
				}
			}
		}
		
		
	
		/**
		 * @param estadoActivo
		 * @param detallePedidoConsolidado
		 */
	
		public static void eliminarDescuentosConsolidados(ArrayList<DetallePedidoDTO> detallePedidoConsolidado) {
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoActualDTO : detallePedidoConsolidado){
					
					//Eliminar calculo de dsctos de consolidados
					String estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
					
					if(detallePedidoActualDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
						
						for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)detallePedidoActualDTO.getArticuloDTO().getArticuloRelacionCol()){
							recetaArticuloDTO.setValorTotalEstadoDescuento(0D);
							recetaArticuloDTO.setValorTotalEstadoNeto(recetaArticuloDTO.getValorTotalEstado());
							recetaArticuloDTO.setValorTotalEstadoNetoIVA(recetaArticuloDTO.getValorTotalEstadoIVA());
						}
					}
					//se obtiene los datos del estado del detalle
					EstadoDetallePedidoDTO estadoDetallePedidoActualDTO = detallePedidoActualDTO.getEstadoDetallePedidoDTO();
	
					estadoDetallePedidoActualDTO.setValorTotalEstadoDescuento(0D);
					estadoDetallePedidoActualDTO.setValorTotalEstadoNeto(estadoDetallePedidoActualDTO.getValorTotalEstado());
					estadoDetallePedidoActualDTO.setValorTotalEstadoNetoIVA(estadoDetallePedidoActualDTO.getValorTotalEstadoIVA());
					detallePedidoActualDTO.setEstadoDetallePedidoDTO(estadoDetallePedidoActualDTO);
				}
			}
		}
		
		
		public static void aplicarDescuentosConsolidados(HttpServletRequest request, HttpSession session, Collection<DetallePedidoDTO> detallePedido,
				Collection<DescuentoDTO> descuentoVariableCol, Collection <String>llaveDescuentoCol,ActionMessages errors) throws SISPEException, Exception {
			
			//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
				int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
				String estadoActivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
				String estadoInactivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
				Collection<VistaPedidoDTO> pedidosConsolidados = (Collection<VistaPedidoDTO>)session.getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				List<DetallePedidoDTO> detallePedidoConsolidado = (List<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
				//se obtiene de la sesion los datos del detalle y de la lista de articulos.
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				Boolean existeArt;
				try{
					if(pedidosConsolidados!=null && detallePedidoConsolidado!=null){
						//borrar los detalles pedidos actual y actualizar los detalles en la coleccion de pedidos consolidados
						String pedidoGeneral=(String)session.getAttribute("ec.com.smx.sic.sispe.pedioGeneral");
						if(pedidoGeneral==null){
							if(detallePedido!= null && detallePedido.size()>0){
								if(detallePedidoConsolidado!= null && detallePedidoConsolidado.size()>0){
									for(DetallePedidoDTO detallePedidoDTO:detallePedido ){
										for (DetallePedidoDTO detPedDTO : detallePedidoConsolidado){
												 
											if(detPedDTO.getId().getCodigoArticulo().equals(detallePedidoDTO.getId().getCodigoArticulo()) && 
														 detPedDTO.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
												detallePedidoConsolidado.remove(detPedDTO);
													 break;
												 }
											}
										
									}
									for(DetallePedidoDTO detallePedidoDTO:detallePedido ){
										if(detallePedidoDTO.getId().getCodigoPedido().equals("-1")){
											 detallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
										}
										if(detallePedidoDTO.getId().getCodigoPedido().equals(vistaPedidoDTO.getId().getCodigoPedido())){
											detallePedidoConsolidado.add(detallePedidoDTO);
										}
									}
								}
							}
						}
						
						
						for (VistaPedidoDTO visPedDTO: pedidosConsolidados){
							
	//								Collection<DetallePedidoDTO> detallePedidoGeneral = clonarDetallePedidoConsolidado(numeroDecimales, detallePedidoConsolidado);
	//								Collection<DetallePedidoDTO> detallePedidoGeneral = (Collection<DetallePedidoDTO>) session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS);
							Collection<DetallePedidoDTO> detallePedidoGeneral = (Collection<DetallePedidoDTO>) SerializationUtils.clone((Serializable)
									session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS_NO_REPETIDOS));
							Collection<DetallePedidoDTO> detallesPedidoConsolidadosProcesar = new ArrayList<DetallePedidoDTO>(); 
							//se verifica la existencia de una colecci\u00F3n de descuentos
							if(visPedDTO.getDescuentosEstadosPedidos() != null && !visPedDTO.getDescuentosEstadosPedidos().isEmpty()){
								String codigoTipoDescuentoVariable = "";
								//se obtiene el c\u00F3digo del tipo de descuento variable
								parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
								if(parametroDTO.getValorParametro()!=null){
									codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
								}
								//se obtiene la colecci\u00F3n de los estados del descuentos
								Collection<DescuentoEstadoPedidoDTO> descuentos = visPedDTO.getDescuentosEstadosPedidos();
								//colecciones que se enviar\u00E1n al m\u00E9todo que calcula los descuentos
								Collection<DescuentoDTO> descuentoVariableActualCol = new ArrayList<DescuentoDTO>();
								Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
								for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : descuentos){
									
									LogSISPE.getLog().info("[codigoTipoDescuento]: {}",descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento());
									//se verifica el c\u00F3digo del descuento variable
									if(descuentoEstadoPedidoDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(descuentoEstadoPedidoDTO.getDescuentoDTO().getCodigoTipoDescuento())){
										//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
										//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
										descuentoEstadoPedidoDTO.getDescuentoDTO().setPorcentajeDescuento(descuentoEstadoPedidoDTO.getPorcentajeDescuento());
										descuentoVariableActualCol.add(descuentoEstadoPedidoDTO.getDescuentoDTO());
									}
									
									//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
									if(descuentoEstadoPedidoDTO.getLlaveDescuento()!= null){
										llavesDescuentosAplicados.add(descuentoEstadoPedidoDTO.getLlaveDescuento());
									}
									LogSISPE.getLog().info("LLAVE: {}",descuentoEstadoPedidoDTO.getLlaveDescuento());
								}
							}
							//crear del detalle a procesar
							for (DetallePedidoDTO detallePedidoConsolidadoDTO :  detallePedidoConsolidado){
								
								if(visPedDTO.getId().getCodigoPedido().equals(detallePedidoConsolidadoDTO.getId().getCodigoPedido())){
									detallePedidoConsolidadoDTO.setNpCantidadTotalConsolidada(0L);
									detallePedidoConsolidadoDTO.setNpValorTotalConsolidado(0D);
									detallesPedidoConsolidadosProcesar.add(detallePedidoConsolidadoDTO);
								}
							}
							
							for(DetallePedidoDTO detPedGen:detallePedidoGeneral){
								existeArt= Boolean.FALSE;
								for(DetallePedidoDTO detPed:detallesPedidoConsolidadosProcesar){
									if(detPedGen.getId().getCodigoArticulo().equals(detPed.getId().getCodigoArticulo())){
										existeArt= Boolean.TRUE;
										break;
									}
								}
								if(!existeArt){
									boolean esActivoPrecioMayorista = false;
									if(detPedGen.getArticuloDTO().getHabilitadoPrecioMayoreo()){
										esActivoPrecioMayorista = true;
									}
									if(!esActivoPrecioMayorista && detPedGen.getArticuloDTO().getHabilitadoPrecioCaja() && detPedGen.getArticuloDTO().getPrecioCaja() > 0){
										if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null){
											detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado());
											detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorTotalEstado());
										}else{
											detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
											detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
										}
									}else{
										detPedGen.setNpCantidadTotalConsolidada(detPedGen.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento());
										detPedGen.setNpValorTotalConsolidado(detPedGen.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento());
									}
									detPedGen.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
									detPedGen.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
									//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
									detPedGen.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(0D);
									
									//Validar si es una canasta cotizaci\u00F3n vac\u00EDa
									if(detPedGen.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
										//recorre cada item de la canasta
										
										for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detPedGen.getArticuloDTO().getArticuloRelacionCol())){
											Long cantidadTotalRecetaConsolidada=0L;
											Double valorTotalRecetaConsolidada=0D;
											
											esActivoPrecioMayorista = false;
											if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
												esActivoPrecioMayorista = true;
											}
											//se verifica si el art\u00EDculo tiene habilitado el precio por caja
											if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null &&!esActivoPrecioMayorista && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && recetaArticuloDTO.getArticuloRelacion().getPrecioCaja() > 0){
												Long cantEstado=detPedGen.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
												Double valorPrevio=recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
												valorTotalRecetaConsolidada=cantEstado*valorPrevio;
												cantidadTotalRecetaConsolidada=cantEstado.longValue();
												recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
												recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
											}else{
												valorTotalRecetaConsolidada = Util.roundDoubleMath(new Double(valorTotalRecetaConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
														recetaArticuloDTO.getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
												cantidadTotalRecetaConsolidada = cantidadTotalRecetaConsolidada + (recetaArticuloDTO.getCantidadPrevioEstadoDescuento().longValue());
												recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
												recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
											}
											recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0L);
											recetaArticuloDTO.setValorPrevioEstadoDescuento(0D);
											
											//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
											recetaArticuloDTO.setNpValorPrevioEstadoDescuento(0D);
										}
									}
									detallesPedidoConsolidadosProcesar.add(detPedGen);
								}
							}
							//HashMap<String,Collection<ArticuloRelacionDTO>> canastosModificados = (HashMap<String,Collection<ArticuloRelacionDTO>>) session.getAttribute(CANASTOS_MODIFICADOS_CONSOLIDACION);
							for(DetallePedidoDTO detallePedidoDTO : detallePedidoConsolidado){
								
								if(!visPedDTO.getId().getCodigoPedido().equals(detallePedidoDTO.getId().getCodigoPedido())){
									Long cantidadTotalConsolidada=0L;
									Double valorTotalConsolidada=0D;
									Boolean pedidoConsolidado=Boolean.FALSE;
									for(DetallePedidoDTO detallePedidoActual : detallesPedidoConsolidadosProcesar){
	//											LogSISPE.getLog().info("detalle cantidad Consolidada: {}",detallePedidoActual.getNpCantidadTotalConsolidada());
	//											LogSISPE.getLog().info("detalle valor consolidado: {}",detallePedidoActual.getNpValorTotalConsolidado());
										if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoActual.getId().getCodigoArticulo())
												&& detallePedidoActual.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento().doubleValue()>0 
												&& detallePedidoActual.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento().longValue()>0){
											//Validar si es una canasta cotizaci\u00F3n vac\u00EDa
											if(detallePedidoActual.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
												
	//													String codigoArticulo=detallePedidoActual.getArticuloDTO().getId().getCodigoArticulo();
	//													if(canastosModificados==null){
	//														canastosModificados= new HashMap<String, Collection<ArticuloRelacionDTO>>();
	//													}
	//													Collection<ArticuloRelacionDTO> canEncontrado=canastosModificados.get(codigoArticulo);
	//													
	//													//se llama al m\u00E9todo que carga la receta de cada art\u00EDculo que es un canasto
	//													if(detallePedidoActual.getNpExisteCambioCanastoEspecial()!=null && detallePedidoActual.getNpExisteCambioCanastoEspecial()){
	//														if(CollectionUtils.isEmpty(canEncontrado)){
	//															canastosModificados.put(codigoArticulo, detallePedidoActual.getArticuloDTO().getArticuloRelacionCol());
	//															session.setAttribute(CANASTOS_MODIFICADOS_CONSOLIDACION,canastosModificados);
	//														}
	//													}else if(!CollectionUtils.isEmpty(canEncontrado)){
	//														List<UtilCollections.PropertyCompare> listAtt = new ArrayList<UtilCollections.PropertyCompare>();
	//														listAtt.add(UtilCollections.addPropertyCompare("id.codigoCompania"));
	//														listAtt.add(UtilCollections.addPropertyCompare("id.codigoArticulo"));
	//														listAtt.add(UtilCollections.addPropertyCompare("id.codigoArticuloRelacionado"));
	//														//actualizar la receta del canasto que se encuentra en otros pedidos consolidados
	//														List<Collection<ArticuloRelacionDTO>> respuesta = UtilCollections.intersection(canEncontrado, detallePedidoActual.getArticuloDTO().getArticuloRelacionCol(), listAtt);
	//														
	//														//agregar el nuevo item pero modificando las cantidades a las que maneja en el canasto actual;
	//														if(!CollectionUtils.isEmpty(respuesta)){
	//															
	//															//Articulos que solo estan en aumentados
	//															Long cantidadCanasto=detallePedidoActual.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento();
	//															Collection<ArticuloRelacionDTO> artRelNuevos=new ArrayList<ArticuloRelacionDTO>(); 
	//															for (ArticuloRelacionDTO artRel : respuesta.get(0)) {
	//																ArticuloRelacionDTO artRelNuevo= SerializationUtils.clone(artRel);
	//																Double valorPrevio=artRel.getArticuloRelacion().getPrecioBase();
	//																Double valorTotal=cantidadCanasto*valorPrevio;
	//																artRelNuevo.setCantidadPrevioEstadoDescuento(cantidadCanasto);
	//																artRelNuevo.setValorPrevioEstadoDescuento(valorTotal);
	//																artRelNuevos.add(artRelNuevo);
	//															}
	//															detallePedidoActual.getArticuloDTO().getArticuloRelacionCol().addAll(artRelNuevos);
	//															
	//															
	//															//Articulos que solo estan eliminados
	//															if(!CollectionUtils.isEmpty(respuesta.get(1))){
	//																detallePedidoActual.getArticuloDTO().getArticuloRelacionCol().removeAll(respuesta.get(1));
	//															}
	//														}
	//													}
												
												//recorre cada item de la canasta
												for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoActual.getArticuloDTO().getArticuloRelacionCol())){
													Long cantidadTotalRecetaConsolidada=0L;
													Double valorTotalRecetaConsolidada=0D;
													
													boolean esActivoPrecioMayorista = false;
													if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()){
														esActivoPrecioMayorista = true;
													}
													//se verifica si el art\u00EDculo tiene habilitado el precio por caja
													if(((String)session.getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))!=null &&!esActivoPrecioMayorista && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && recetaArticuloDTO.getArticuloRelacion().getPrecioCaja() > 0){
														Long cantEstado= detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
														Double valorPrevio=recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
														valorTotalRecetaConsolidada=cantEstado*valorPrevio;
														recetaArticuloDTO.setCantidadPrevioEstadoDescuento(cantidadTotalRecetaConsolidada);
														recetaArticuloDTO.setValorPrevioEstadoDescuento(valorTotalRecetaConsolidada);
													}else{
														ArticuloRelacionDTO artRelDTOCloneOrg=(ArticuloRelacionDTO)CollectionUtils.find(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol(),new BeanPropertyValueEqualsPredicate("id.codigoArticuloRelacionado",recetaArticuloDTO.getId().getCodigoArticuloRelacionado()));
														if(artRelDTOCloneOrg!=null){
															valorTotalRecetaConsolidada = Util.roundDoubleMath(new Double(valorTotalRecetaConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
																	artRelDTOCloneOrg.getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
															cantidadTotalRecetaConsolidada = cantidadTotalRecetaConsolidada + (artRelDTOCloneOrg.getCantidadPrevioEstadoDescuento()==null?0:artRelDTOCloneOrg.getCantidadPrevioEstadoDescuento().longValue());
															recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
															recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
														}else{
																valorTotalRecetaConsolidada = Util.roundDoubleMath(new Double(valorTotalRecetaConsolidada),numeroDecimales).doubleValue() + Util.roundDoubleMath(
																recetaArticuloDTO.getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
																cantidadTotalRecetaConsolidada = cantidadTotalRecetaConsolidada + (recetaArticuloDTO.getCantidadPrevioEstadoDescuento()==null?0:recetaArticuloDTO.getCantidadPrevioEstadoDescuento().longValue());
																recetaArticuloDTO.setNpCantidadTotalConsolidada(cantidadTotalRecetaConsolidada);
																recetaArticuloDTO.setNpValorTotalConsolidado(valorTotalRecetaConsolidada);
														}
														
													}
												}
											}else{
												valorTotalConsolidada =detallePedidoActual.getNpValorTotalConsolidado();
												valorTotalConsolidada =valorTotalConsolidada+Util.roundDoubleMath(
														detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioEstadoDescuento(),numeroDecimales).doubleValue();
												
												cantidadTotalConsolidada=detallePedidoActual.getNpCantidadTotalConsolidada();
												cantidadTotalConsolidada =cantidadTotalConsolidada+detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadPrevioEstadoDescuento();
											}
											pedidoConsolidado=Boolean.TRUE;
										}
	//											LogSISPE.getLog().info("detalle final  cantidad Consolidada: {}",cantidadTotalConsolidada);
	//											LogSISPE.getLog().info("detalle final valor consolidado: {}",valorTotalConsolidada);
										if(pedidoConsolidado){
											detallePedidoActual.setNpCantidadTotalConsolidada(cantidadTotalConsolidada);
											detallePedidoActual.setNpValorTotalConsolidado(valorTotalConsolidada);
											break;
										}									
									}
								}
							}
							if(detallesPedidoConsolidadosProcesar!=null && detallesPedidoConsolidadosProcesar.size()>0){
								if(session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios") != null && ((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios")).equals("SI")){
									//Actualizar los precios de los detalles consolidados a procesar
									actualizarDetallePedidosConsolidados(request, 
										estadoActivo, estadoInactivo,visPedDTO.getRucEmpresa(),visPedDTO.getTipoDocumentoCliente(),detallesPedidoConsolidadosProcesar);
								}
								
								//llamada al m\u00E9todo que determina los totales por detalle
								for(DetallePedidoDTO detallePedidoDTORecorrido : detallesPedidoConsolidadosProcesar){
									
									if(detallePedidoDTORecorrido.getId().getCodigoPedido().equals(visPedDTO.getId().getCodigoPedido())){
										CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTORecorrido, request, false,false);
										
										if(detallePedidoDTORecorrido.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
												detallePedidoDTORecorrido.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
											//if(vistaPedidoDTO!=null && vistaPedidoDTO.getId().getCodigoPedido().equals(visPedDTO.getId().getCodigoPedido())){
											Collection<ArticuloRelacionDTO>artRelCopyCol=null;
											if(CollectionUtils.isNotEmpty(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol())){
												artRelCopyCol= new ArrayList<ArticuloRelacionDTO>(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol().size());
												for(ArticuloRelacionDTO artRelDTO:detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol()){
													ArticuloRelacionDTO artRelCopyDTO= new ArticuloRelacionDTO();
													artRelCopyDTO.getId().setCodigoArticulo(artRelDTO.getId().getCodigoArticulo());
													artRelCopyDTO.getId().setCodigoArticuloRelacionado(artRelDTO.getId().getCodigoArticuloRelacionado());
													artRelCopyDTO.getId().setCodigoCompania(artRelDTO.getId().getCodigoCompania());
													artRelCopyDTO.setNpCantidadTotalConsolidada(artRelDTO.getNpCantidadTotalConsolidada());
													artRelCopyDTO.setNpValorTotalConsolidado(artRelDTO.getNpValorTotalConsolidado());
													artRelCopyCol.add(artRelCopyDTO);
												}
											}
											
											if(detallePedidoDTORecorrido.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
													detallePedidoDTORecorrido.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
												
												//Si el canasto de cotizaciones por validaciones cambio su estado EstadoCanCotVacio 1 a 0 cuando abre el pedido se vuelve a verificar y asignar correctamente este valor.
												String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
												if(detallePedidoDTORecorrido.getArticuloDTO().getCodigoArticuloOriginal()!= null && detallePedidoDTORecorrido.getArticuloDTO().getCodigoArticuloOriginal().equals(codigoCanCotVacio)){
													detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
												}else if(detallePedidoDTORecorrido.getArticuloDTO().getCodigoArticuloOriginal()!= null && detallePedidoDTORecorrido.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
													detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
												}	
											
												CotizacionReservacionUtil.cargarRecetaArticulo(request, detallePedidoDTORecorrido, visPedDTO.getId().getSecuencialEstadoPedido(),Boolean.TRUE,detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol(), false);
											}
											if(CollectionUtils.isNotEmpty(artRelCopyCol)){
												//Collection<ArticuloRelacionDTO>artRelCopyCol= new ArrayList<ArticuloRelacionDTO>(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol().size());
												for(ArticuloRelacionDTO artRelDTO:artRelCopyCol){
													ArticuloRelacionDTO artRelDTOCloneOrg=(ArticuloRelacionDTO)CollectionUtils.find(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol(),new BeanPropertyValueEqualsPredicate("id.codigoArticuloRelacionado",artRelDTO.getId().getCodigoArticuloRelacionado()));
													if(artRelDTOCloneOrg!=null){
														artRelDTOCloneOrg.setNpCantidadTotalConsolidada(artRelDTO.getNpCantidadTotalConsolidada());
														artRelDTOCloneOrg.setNpValorTotalConsolidado(artRelDTO.getNpValorTotalConsolidado());
													}
												}
											}
											//}
											//}
										}
									}
									else{
										if(detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
											//recorre cada item de la canasta
											for(ArticuloRelacionDTO recetaArticuloDTO : (Collection<ArticuloRelacionDTO>)(detallePedidoDTORecorrido.getArticuloDTO().getArticuloRelacionCol())){
												recetaArticuloDTO.setCantidadPrevioEstadoDescuento(0L);
												recetaArticuloDTO.setValorPrevioEstadoDescuento(0D);
												
												//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
												recetaArticuloDTO.setNpValorPrevioEstadoDescuento(0D);
											}
										}else{
											//se asignan los campos que sirven para el c\u00E1lculo de los descuentos
											detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setCantidadPrevioEstadoDescuento(0L);
											detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setValorPrevioEstadoDescuento(0D);
											
											//se limpia para que aplique bien el descuento caja/mayorista en pedidos consolidados
											detallePedidoDTORecorrido.getEstadoDetallePedidoDTO().setNpValorPrevioEstadoDescuento(0D);
										}
									}
								}
								
	//									Collection colDescuentoEstadoPedidoConsolidado = SessionManagerSISPE.getServicioClienteServicio().transProcesarDescuentos(detallesPedidoConsolidadosProcesar,llaveDescuentoCol,descuentoVariableCol);
								Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoConsolidado = CotizacionReservacionUtil.procesarDescuentosPorTipo(request, detallesPedidoConsolidadosProcesar, llaveDescuentoCol);
								
								//si es pedido consolidado se aplica los valores del descuento variable a las autorizaciones
								if(esPedidoConsolidado(request)){
									AutorizacionesUtil.aplicarEstadoDescuentoVariableAutorizacionesPedido(request, detallesPedidoConsolidadosProcesar);
									
									//se asigna la configuracion de las autorizaciones a los detalles
	//										asignarAutorizacionesDetallesConsolidados(request, detallesPedidoConsolidadosProcesar);
								}
								
								//eliminar detalles que no pertenecen al pedido
								List<DetallePedidoDTO> detallesEliminar= new ArrayList<DetallePedidoDTO>();
								for(DetallePedidoDTO detPed:detallesPedidoConsolidadosProcesar){
									if(!visPedDTO.getId().getCodigoPedido().equals(detPed.getId().getCodigoPedido())){
										detallesEliminar.add(detPed);
									}
								}
								detallesPedidoConsolidadosProcesar.removeAll(detallesEliminar);
								calcularValoresFinalesPedidoConsolidado(request, visPedDTO,detallesPedidoConsolidadosProcesar,colDescuentoEstadoPedidoConsolidado,Boolean.FALSE);
								
								//se asigna el descuento correcto y calculado al pedido principal
								VistaPedidoDTO vistaPedidoDTOAux = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
								if(vistaPedidoDTOAux!=null && vistaPedidoDTOAux.getId().getCodigoPedido().equals(visPedDTO.getId().getCodigoPedido())){
									session.setAttribute(CotizarReservarAction.COL_DESCUENTOS,colDescuentoEstadoPedidoConsolidado);
								}
								
								//verificar si el pedido tiene descuento de navidad empresaria, calcular el numero de bonos
								CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad(colDescuentoEstadoPedidoConsolidado,  request,detallesPedidoConsolidadosProcesar);
								
								/*guardar numero de bonos
								 *@autor osaransig*/
								
								Object numBonosObject = request.getSession().getAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
								if (numBonosObject != null && ((Integer) numBonosObject) > 0 ) {
									visPedDTO.setNumBonNavEmp((Integer) numBonosObject);
								}else{
									visPedDTO.setNumBonNavEmp(null);
								}
							}
						}
					
	//				//calcular el descuento de la pantalla principal
					Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoDTO = (Collection<DescuentoEstadoPedidoDTO>)session.getAttribute(CotizarReservarAction.COL_DESCUENTOS);
					if(colDescuentoEstadoPedidoDTO!= null){
						//se obtiene la suma de los descuentos
						double totalDescuento = 0;
						double totalPorcentaje = 0;
						for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : colDescuentoEstadoPedidoDTO){
							
							LogSISPE.getLog().info("DSCTO EST PEDIDO # {}",descuentoEstadoPedidoDTO.getValorDescuento().doubleValue());
							totalDescuento = totalDescuento + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
							totalPorcentaje = totalPorcentaje + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
						}
						LogSISPE.getLog().info("DESCUENTO TOTAL: {}",totalDescuento);
						LogSISPE.getLog().info("TOTAL %: {}",totalPorcentaje);
						//se guarda en sesi\u00F3n el porcentaje total de los descuentos
						session.setAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO,Double.valueOf(totalPorcentaje));
						//se guarda en sesi\u00F3n el total de descuentos que aplican y no aplican IVA
						session.setAttribute(CotizarReservarAction.DESCUENTO_TOTAL, Double.valueOf(totalDescuento));
					}
				}
				}
				catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					//errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
				}
				
			//se elimina de sesion el parametro
			session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		}
	
		
		/**
		 * @param request
		 * @param session
		 * @param detallePedido
		 * @param detallePedidoConsolidado
		 * @throws SISPEException
		 * @throws Exception
		 */
		public static void eliminarDescuentosConsolidados(HttpServletRequest request, HttpSession session, Collection<VistaPedidoDTO> pedidosConsolidadosEliminados,
				Collection<DetallePedidoDTO> detallePedidoConsolidado) throws SISPEException,	Exception {
			
			//se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
	//				int numeroDecimales = new Integer(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.numeroDecimales")).intValue();
			String codigoTipoDescuentoVariable = "";
			//se obtiene el c\u00F3digo del tipo de descuento variable
			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
			if(parametroDTO.getValorParametro()!=null){
				codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
			}
			Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoConsolidado =new ArrayList<DescuentoEstadoPedidoDTO>();
			if(detallePedidoConsolidado!=null && !detallePedidoConsolidado.isEmpty()){
				
				CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request); 
				
				for(VistaPedidoDTO visPedDTO: pedidosConsolidadosEliminados){
					
					//se eliminan las variables para que procese correctamente los descuentos por caja y mayorista
					session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
					session.setAttribute( CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA , "NO");
					
					LogSISPE.getLog().info("PEDIDO ELIMINADO: {}",visPedDTO.getId().getCodigoPedido());
					Collection<DetallePedidoDTO> colDetPed= new ArrayList<DetallePedidoDTO>();
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO=new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoDTO.getId().setCodigoCompania(visPedDTO.getId().getCodigoCompania());
					descuentoEstadoPedidoDTO.getId().setCodigoPedido(visPedDTO.getId().getCodigoPedido());
					descuentoEstadoPedidoDTO.getId().setCodigoAreaTrabajo(visPedDTO.getId().getCodigoAreaTrabajo());
					descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
					descuentoEstadoPedidoDTO.getDescuentoDTO().setTipoDescuentoDTO(new TipoDescuentoDTO());
					Collection<DescuentoEstadoPedidoDTO> desEstPedCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoAntesConsolidar(descuentoEstadoPedidoDTO);
					//armar las llaves para calcular los nuevos decuentos
					Collection<DescuentoDTO> descuentoVariableActualCol = new ArrayList<DescuentoDTO>();
					Collection<String> llavesDescuentosAplicados = new ArrayList<String>();
					
					Collection<DescuentoEstadoPedidoDTO> descuentosVariables = new ArrayList<DescuentoEstadoPedidoDTO>();
					
					if(CollectionUtils.isNotEmpty(desEstPedCol)){
						for(DescuentoEstadoPedidoDTO desEstPedDTO: desEstPedCol){
							
							LogSISPE.getLog().info("[codigoTipoDescuento]: {}",desEstPedDTO.getDescuentoDTO().getCodigoTipoDescuento());
							//se verifica el c\u00F3digo del descuento variable
							if(desEstPedDTO.getDescuentoDTO()!=null && codigoTipoDescuentoVariable.equals(desEstPedDTO.getDescuentoDTO().getCodigoTipoDescuento())){
								//actualizar el registro del descuento con el valor del porcentaje de descuento en el estado actual
								//esta operaci\u00F3n es necesaria porque el proceso que calcula los descuentos se basa en el porcentaje asignado al descuento
								desEstPedDTO.getDescuentoDTO().setPorcentajeDescuento(desEstPedDTO.getPorcentajeDescuento());
								descuentoVariableActualCol.add(desEstPedDTO.getDescuentoDTO());
								descuentosVariables.add(desEstPedDTO);
							}
							
							//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
							if(desEstPedDTO.getLlaveDescuento()!= null){
								llavesDescuentosAplicados.add(desEstPedDTO.getLlaveDescuento());
							}
							else{
								if(desEstPedDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
									session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
									llavesDescuentosAplicados.add((String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS));
								}
								else if(desEstPedDTO.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
									session.removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
									llavesDescuentosAplicados.add((String) session.getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA));
								}
							}
							LogSISPE.getLog().info("LLAVE: {}",desEstPedDTO.getLlaveDescuento());
						}
						session.setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES, descuentosVariables);
					}
	
					for (DetallePedidoDTO detallePedidoConsolidadoDTO  : detallePedidoConsolidado){
						
						if(visPedDTO.getId().getCodigoPedido().equals(detallePedidoConsolidadoDTO.getId().getCodigoPedido())){		
									colDetPed.add(detallePedidoConsolidadoDTO);
						}
					}
					if(llavesDescuentosAplicados.isEmpty()){
						llavesDescuentosAplicados=null;
					}
					if(descuentoVariableActualCol.isEmpty()){
						descuentoVariableActualCol=null;
					}
					//cuando los descuentos son de gerente comercial
					if(descuentoEstadoPedidoDTO.getLlaveDescuento() != null && descuentoEstadoPedidoDTO.getLlaveDescuento().contains(SEPARADOR_TOKEN.concat(CODIGO_GERENTE_COMERCIAL))){
						descuentoEstadoPedidoDTO.getId().setCodigoReferenciaDescuentoVariable(descuentoEstadoPedidoDTO.getLlaveDescuento().split(SEPARADOR_TOKEN)[3]);
					}else {
						if(descuentoEstadoPedidoDTO.getLlaveDescuento() != null && !descuentoEstadoPedidoDTO.getLlaveDescuento().equals("")){
							descuentoEstadoPedidoDTO.getId().setCodigoReferenciaDescuentoVariable(descuentoEstadoPedidoDTO.getLlaveDescuento().split(SEPARADOR_TOKEN)[1]);
						}else{ 
							descuentoEstadoPedidoDTO.getId().setCodigoReferenciaDescuentoVariable(descuentoEstadoPedidoDTO.getId().getCodigoDescuento());
						}
					}
					
					String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
					request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llavesDescuentosAplicados);
					//procesar detalles para cargar los descuentos de cajas o mayorista
					for (DetallePedidoDTO detPedDTO:colDetPed){
						//lamada al m\u00E9todo que determina los totales por detalle      
						CotizacionReservacionUtil.calcularValoresDetalle(detPedDTO, request, false,false);
						
						if(detPedDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
								detPedDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
							
							//Si el canasto de cotizaciones por validaciones cambio su estado EstadoCanCotVacio 1 a 0 cuando abre el pedido se vuelve a verificar y asignar correctamente este valor.
							String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
							if(detPedDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && detPedDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codigoCanCotVacio)){
								detPedDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
							}else if(detPedDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && detPedDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
								detPedDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
							}				
							//se llama al m\u00E9todo que carga la receta de cada art\u00EDculo que es un canasto
							CotizacionReservacionUtil.cargarRecetaArticulo(request, detPedDTO, visPedDTO.getId().getSecuencialEstadoPedido(), Boolean.FALSE, null, false);
						}
					}
					
					colDescuentoEstadoPedidoConsolidado = CotizacionReservacionUtil.procesarDescuentosPorTipo(request, colDetPed, llavesDescuentosAplicados);
					calcularValoresFinalesPedidoConsolidado(request,visPedDTO, colDetPed,colDescuentoEstadoPedidoConsolidado,Boolean.TRUE);
					
					//verificar si el pedido tiene descuento de navidad empresaria, calcular el numero de bonos
					CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad(colDescuentoEstadoPedidoConsolidado,  request,colDetPed);
					
					/*guardar numero de bonos
					 *@autor osaransig*/
					
					Object numBonosObject = request.getSession().getAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
					if (numBonosObject != null && ((Integer) numBonosObject) > 0 ) {
						visPedDTO.setNumBonNavEmp((Integer) numBonosObject);
					}else{
						visPedDTO.setNumBonNavEmp(null);
					}
				}
			}
			
			//se elimina de sesion el parametro
			session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		}
		
		
		  /**
		   * Determina el valor total de los descuentos por caja 
		   * 
		   * @param request												- La petici\u00F3n HTTP
		   * @param colDetallePedidoDTO						- Colecci\u00F3n del detalle del pedido
		   */
		  public static ActionMessage calcularValoresFinalesPedidoConsolidado(HttpServletRequest request, VistaPedidoDTO pedidosConsolidados,Collection <DetallePedidoDTO> colDetallePedidoDTO,
				  Collection<DescuentoEstadoPedidoDTO> colDesEstPedCon,Boolean esEliminarConsolidados) throws Exception{
		  	HttpSession session = request.getSession();
		  	String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		  	String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		  	Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoConsolidado = colDesEstPedCon;
		  	
		  	boolean errorDescuentoCaja = false;
		  	//se inicializan los subtotales
		  	double subTotalPedidoAplicaIVA = 0;
		  	double subTotalPedidoNoAplicaIVA= 0;
		  	
		  	double descuentoTotalCajas = 0;
		  	double descuentoTotalMayorista = 0;
		  	double valorTotalDescuentoCajas = 0;
		  	double valorTotalDescuentoMayorista = 0;
			
		  	ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
			String codCanCat = parametroDTO.getValorParametro();
			
			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionParaNuevasRecetas", request);
			String codClaRecetasNuevas = parametroDTO.getValorParametro();
			
			//parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoCanastoCotizacionesVacio", request);
			String codCanCotVacios = SessionManagerSISPE.getCodigoCanastoVacio(request);
			
			String calcularCanastosPreciosTemp=null;
			if(pedidosConsolidados!=null){
				calcularCanastosPreciosTemp=pedidosConsolidados.getObsmigperTemp();
			}
	
			//se desactivan los descuentos por caja y mayorista de acuerdo al pedido
	//				session.setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS, "NO");
	//				session.setAttribute( CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA , "NO");
			CotizacionReservacionUtil.verificarSiAplicaPreciosCajaMayorista(request, session);
			
		  	//se recorre la colecci\u00F3n del detalle
		  	int indice = 0;
		  	for(DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO){
		  	  	LogSISPE.getLog().info("**calcularValoresFinalesPedidoConsolidado**");
		  	  	
		  		//se asignan los nuevos valores para el POS
		  	  CotizacionReservacionUtil.ajustarValoresPOS(detallePedidoDTO, accionActual, request,null,indice,codClaRecetasNuevas,codCanCotVacios,codCanCat,calcularCanastosPreciosTemp);
				
				if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
					//--- se verifica si existe un art\u00EDculo con descuento por precio de caja ----
			  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja()!= null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja().doubleValue() > 0){
			  			descuentoTotalCajas = descuentoTotalCajas + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCaja().doubleValue();
			  			valorTotalDescuentoCajas = valorTotalDescuentoCajas + detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
			  		}
				}
		  		
				if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
			  		//--- se verifica si existe un art\u00EDculo con descuento de mayorista ----
			  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista()!= null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista().doubleValue() > 0){
			  			descuentoTotalMayorista = descuentoTotalMayorista + detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayorista().doubleValue();
			  			valorTotalDescuentoMayorista = valorTotalDescuentoMayorista + detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
			  		}
				}
				
		  		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
		  		//se verifican los posibles descuentos por caja o mayorista en una receta
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta()!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta() > 0){
						descuentoTotalCajas += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta().doubleValue();
						valorTotalDescuentoCajas += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
					}
		  		}
				
		  		if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta()!=null && detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta() > 0){
						descuentoTotalMayorista += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta().doubleValue();
						valorTotalDescuentoMayorista += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorPrevioVenta().doubleValue();
					}
		  		}
		  		
		  		
		  		if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()!=null 
						&& !detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty() 
						&& (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codClaRecetasNuevas) || detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(codCanCat))
						&& detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!=null){
					//calcular el valor con iva y sin iva de los canastos para sacar el valor total
					for(ArticuloRelacionDTO recetaArticuloCanCotVac : (Collection<ArticuloRelacionDTO>)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
						if(recetaArticuloCanCotVac.getArticuloRelacion().getAplicaImpuestoVenta()){
							subTotalPedidoAplicaIVA=subTotalPedidoAplicaIVA+SICArticuloCalculo.getInstancia().calcularValorConImpuestos((recetaArticuloCanCotVac.getValorTotalEstado()-(recetaArticuloCanCotVac.getValorTotalEstadoDescuento()==null?0D:recetaArticuloCanCotVac.getValorTotalEstadoDescuento())), CotizacionReservacionUtil.crearMapParaRedondearValores(VALOR_PORCENTAJE_IVA*100), true);
						}else{
							subTotalPedidoNoAplicaIVA=subTotalPedidoNoAplicaIVA+(recetaArticuloCanCotVac.getValorTotalEstado()-(recetaArticuloCanCotVac.getValorTotalEstadoDescuento()==null?0D:recetaArticuloCanCotVac.getValorTotalEstadoDescuento()));
						}
						
					}
					
				}else{
					//se verifica si el art\u00EDculo tiene IVA
			  		if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
			  			subTotalPedidoAplicaIVA += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
			  		}else{
			  			subTotalPedidoNoAplicaIVA += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
			  		}
				}
		  		
		  		//si este valor es menor a cero significa que hay errores en los descuentos por caja porque el precio o la unidad de manejo es erronea
		  		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento().doubleValue() < 0){
		  			errorDescuentoCaja = true;
		  		}
		  		if(CotizacionReservacionUtil.esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
		  			indice++;
		  		}
		  	}
		  	buscarPedidoConsolidadoCalcularTotales(request, pedidosConsolidados,colDescuentoEstadoPedidoConsolidado, session,subTotalPedidoAplicaIVA, subTotalPedidoNoAplicaIVA, esEliminarConsolidados);
		  	if (colDescuentoEstadoPedidoConsolidado != null){
				Collection<DescuentoEstadoPedidoDTO> colEliminar = new ArrayList<DescuentoEstadoPedidoDTO>();
				for(DescuentoEstadoPedidoDTO descuentoEstadoPedido : colDescuentoEstadoPedidoConsolidado ){
					//se verifica si ya existe un descuento incluido por art\u00EDculo
					if(descuentoEstadoPedido.getEsAplicadoAutomaticamente()!=null && descuentoEstadoPedido.getEsAplicadoAutomaticamente().equals(estadoActivo)){
						colEliminar.add(descuentoEstadoPedido);
					}
				}
				colDescuentoEstadoPedidoConsolidado.removeAll(colEliminar);
				colEliminar = null;
			}
			
	
			//se llama al m\u00E9todo que crea el nuevo tipo de descuento
			colDescuentoEstadoPedidoConsolidado = CotizacionReservacionUtil.crearDescuentoIncluidoPorArticulo(descuentoTotalCajas, descuentoTotalMayorista, valorTotalDescuentoCajas, valorTotalDescuentoMayorista,
					colDescuentoEstadoPedidoConsolidado, request);
			
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null){
				//se crea el objeto para consultar el descuento que se va a agregar
		  		DescuentoDTO descuentoFiltro = new DescuentoDTO();
		  		descuentoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  		descuentoFiltro.setTipoDescuentoDTO(new TipoDescuentoDTO());
			//se crea el objeto final
				if(descuentoTotalCajas == 0){
					descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"));
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoCajas);
					descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoCajas);
					descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalCajas, NUMERO_DECIMALES));
					descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
			  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
			  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
			  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
			  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
			  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
			  		
			  		if(colDescuentoEstadoPedidoConsolidado == null){
			  			colDescuentoEstadoPedidoConsolidado = new ArrayList<DescuentoEstadoPedidoDTO>();
			  		}
			  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
			  		colDescuentoEstadoPedidoConsolidado.add(descuentoEstadoPedidoDTO);
				}
			}
			if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
				//se crea el objeto para consultar el descuento que se va a agregar
		  		DescuentoDTO descuentoFiltro = new DescuentoDTO();
		  		descuentoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  		descuentoFiltro.setTipoDescuentoDTO(new TipoDescuentoDTO());
				if(descuentoTotalMayorista == 0){
					descuentoFiltro.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"));
					DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
					descuentoEstadoPedidoDTO.setValorPrevioDescuento(valorTotalDescuentoMayorista);
					descuentoEstadoPedidoDTO.setValorMotivoDescuento(valorTotalDescuentoMayorista);
					descuentoEstadoPedidoDTO.setValorDescuento(Util.roundDoubleMath(descuentoTotalMayorista, NUMERO_DECIMALES));
					descuentoEstadoPedidoDTO.getId().setCodigoDescuento(descuentoFiltro.getId().getCodigoDescuento());
			  		descuentoEstadoPedidoDTO.setPorcentajeDescuento(0d);
			  		descuentoEstadoPedidoDTO.setRangoInicialDescuento(0.01);
			  		descuentoEstadoPedidoDTO.setRangoFinalDescuento(9999999999.99);
			  		descuentoEstadoPedidoDTO.setEsAplicadoAutomaticamente(estadoActivo);
			  		descuentoEstadoPedidoDTO.setDescuentoDTO((DescuentoDTO)SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(descuentoFiltro).iterator().next());
			  		
			  		if(colDescuentoEstadoPedidoConsolidado == null){
			  			colDescuentoEstadoPedidoConsolidado = new ArrayList<DescuentoEstadoPedidoDTO>();
			  		}
			  		//se agrega a la colecci\u00F3n de descuentos, el objeto que indica un descuento incluido por art\u00EDculo, este descuento no tiene una llave 
			  		colDescuentoEstadoPedidoConsolidado.add(descuentoEstadoPedidoDTO);
				}
			}
			
			
		  	if(errorDescuentoCaja)
		  		return new ActionMessage("errors.descuentoCajas");
		  	return null;
		  }
		  
		  /**
		   * @param request
		   * @param pedidoConsolidadoDTO
		   * @param colDescuentoEstadoPedidoConsolidado
		   * @param session
		   * @param subTotalPedidoAplicaIVA
		   * @param subTotalPedidoNoAplicaIVA
		   * @throws SISPEException
		   * @throws Exception
		   */
		  public static void buscarPedidoConsolidadoCalcularTotales(HttpServletRequest request,	VistaPedidoDTO pedidoConsolidadoDTO, Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoConsolidado,
		  		HttpSession session, double subTotalPedidoAplicaIVA, double subTotalPedidoNoAplicaIVA,Boolean esEliminarConsolidados) throws SISPEException, Exception {
			  
		  		Collection<PedidoDTO> pedidoColActualizado= new ArrayList<PedidoDTO>();
		  		PedidoDTO pedidoBuscar= new PedidoDTO();
		  		pedidoBuscar.getId().setCodigoCompania(pedidoConsolidadoDTO.getId().getCodigoCompania());
		  		pedidoBuscar.getId().setCodigoAreaTrabajo(pedidoConsolidadoDTO.getId().getCodigoAreaTrabajo());
		  		pedidoBuscar.getId().setCodigoPedido(pedidoConsolidadoDTO.getId().getCodigoPedido());
		  		LogSISPE.getLog().info("BUSCANDO POR PEDIDO # {}",pedidoConsolidadoDTO.getId().getCodigoPedido());
		  		Collection<PedidoDTO> pedidoActColDTO= SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoBuscar);
		  		LogSISPE.getLog().info("Pedido a actualizar {}",pedidoActColDTO.size());
		  		if(pedidoActColDTO!=null && pedidoActColDTO.size()==1){
		  			PedidoDTO pedActDTO = pedidoActColDTO.iterator().next();
		  			if(esEliminarConsolidados){
		  				pedActDTO.setDescuentosEstadosPedidos(colDescuentoEstadoPedidoConsolidado);
		  			}
		  			else{
		  				pedActDTO.setDescuentosEstadosPedidos(colDescuentoEstadoPedidoConsolidado);
		  			}
		  			//verifica forma pago en efectivo
		  			String codigoTipoDescuentoPagEfe = CotizacionReservacionUtil.obtenerCodigoTipoDescuentoMaxiNavidad(request);
		  			boolean tieneDescuentoTipoMaxNavEmp = false;
		  			String pagoEfectivo=(String)session.getAttribute(CHECK_PAGO_EFECTIVO);
		  			if(colDescuentoEstadoPedidoConsolidado != null && !colDescuentoEstadoPedidoConsolidado.isEmpty()){
		  				for(DescuentoEstadoPedidoDTO descuentoEsPedDTO : colDescuentoEstadoPedidoConsolidado){
		  					if(descuentoEsPedDTO.getDescuentoDTO() != null && descuentoEsPedDTO.getDescuentoDTO().getTipoDescuentoDTO() != null
		  							&& descuentoEsPedDTO.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoPagEfe)){
		  						tieneDescuentoTipoMaxNavEmp = true;
		  						break;
		  					}
		  				}
		  				if(tieneDescuentoTipoMaxNavEmp){
		  					pedidoConsolidadoDTO.setNpPagoEfectivo("ok");
		  				}else{ 
		  					if(pagoEfectivo!=null){
		  						pedidoConsolidadoDTO.setNpPagoEfectivo("ok");
		  					}else{
		  						pedidoConsolidadoDTO.setNpPagoEfectivo(null);
		  					}
		  				}
		  			}else{
		  				if(pagoEfectivo!=null){
		  					pedidoConsolidadoDTO.setNpPagoEfectivo("ok");
		  				}else{
		  					pedidoConsolidadoDTO.setNpPagoEfectivo(null);
		  				}
		  			}
		  			
		  			calcularTotalesPedidosConsolidado(request, subTotalPedidoAplicaIVA, 
		  					subTotalPedidoNoAplicaIVA, pedActDTO, colDescuentoEstadoPedidoConsolidado,pedidoConsolidadoDTO,esEliminarConsolidados);
		  			
		  			
		  			pedidoColActualizado.add(pedActDTO);
		  		}
		  		if(!esEliminarConsolidados){
		  			LogSISPE.getLog().info("AGREGAR A PEDIDOS CONSOLIDADOS");
		  			Collection<PedidoDTO> pedidoColConsolidado=(Collection<PedidoDTO>) session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS);
		  			if(pedidoColConsolidado==null){
		  				pedidoColConsolidado= new ArrayList<PedidoDTO>();
		  			}
		  			for(PedidoDTO pedidoActualizado:pedidoColActualizado){	
		  					for(PedidoDTO pedido:pedidoColConsolidado){	
		  						if(pedido.getId().getCodigoPedido().equals(pedidoActualizado.getId().getCodigoPedido())){
		  							pedidoColConsolidado.remove(pedido);
		  							break;
		  						}
		  					}
		  				}
		  				LogSISPE.getLog().info("TAM PEDIDO CONSOLIDADO  {}",pedidoColConsolidado.size());
		  				pedidoColConsolidado.addAll(pedidoColActualizado);
		  				session.setAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS,pedidoColConsolidado);
		  				LogSISPE.getLog().info("TAM PEDIDO CONSOLIDADO ACTUALIZADO {}",pedidoColConsolidado.size());
		  		}
		  		else{
		  			LogSISPE.getLog().info("AGREGAR A PEDIDOS CONSOLIDADOS ELIMINADOS");
		  			Collection<PedidoDTO> pedidoColConsolidadoEliminado=(Collection<PedidoDTO>) session.getAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS);
		  			if(pedidoColConsolidadoEliminado==null){
		  				pedidoColConsolidadoEliminado= new ArrayList<PedidoDTO>();
		  			}
		  			
//		  			Boolean existePedido=Boolean.FALSE;
		  				for(PedidoDTO pedidoActualizado:pedidoColActualizado){
//		  					existePedido=Boolean.FALSE;
		  					for(PedidoDTO pedido:pedidoColConsolidadoEliminado){	
		  						if(pedido.getId().getCodigoPedido().equals(pedidoActualizado.getId().getCodigoPedido())){
		  							pedidoColConsolidadoEliminado.remove(pedido);
		  							break;
		  						}
		  					}
		  				}
		  			LogSISPE.getLog().info("TAM PEDIDO CONSOLIDADO  {}",pedidoColConsolidadoEliminado.size());
		  			pedidoColConsolidadoEliminado.addAll(pedidoColActualizado);
		  			session.setAttribute(CotizarReservarAction.PEDIDOS_CONSOLIDADOS_ELIMINADOS,pedidoColConsolidadoEliminado);
		  			LogSISPE.getLog().info("TAM PEDIDO CONSOLIDADO ACTUALIZADO ELIMINADO{}",pedidoColConsolidadoEliminado.size());
		  		}
		  }
		  
		  
		/**
		 * Determina el precio que se debe tomar en cuenta para realizar el c\u00E1lculo del total del detalle para el 
		 * art\u00EDculo que se va a agregar al detalle, (Este proceso es valido para los articulos que son consolidados)
		 * 
		 * @param  formulario				El formulario donde se mostrar\u00E1n los datos
		 * @param  request				La petici\u00F3n que actualmente se est\u00E1 procesando
		 * @return detallePedidoDTO		El detalle del pedido que fu\u00E9 creado
		 */
		public static List<DetallePedidoDTO> construirDetallesPedidoDesdeVistaConsolidados(CotizarReservarForm formulario, HttpServletRequest request,Collection<VistaPedidoDTO> visPedColConsAux,Boolean verificarSIC,
				Boolean eliminarConsolidacion, ActionMessages errors)throws Exception{
			
			HttpSession session = request.getSession();
			//se obtienen las claves que indican un estado activo y un estado inactivo
			String estadoInactivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_INACTIVO);
			String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			
			////se sube a sesion el parametro para que durante todo el proceso se consulte solo una vez
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
			Double costoEntregaPedido = 0D;
			
			//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
			String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
			session.setAttribute(CotizarReservarAction.CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);
			session.setAttribute(CotizarReservarAction.CAMBIO_PESO,"ok");
			//obtener la coleccion de pedidos consolidados
			//colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
			List<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
			List<DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
//					List<DetallePedidoDTO> detallePedido1 = new ArrayList <DetallePedidoDTO>();
			Collection<String> codigosArticulos = new ArrayList <String>();
			ArticuloDTO articuloDTO = null;
			
			//se obtienen los codigos del parametro para autorizar descuento variable o stock desde linea comercial-funcionario-proceso
	  		Long codigoAutorizarDescVar = AutorizacionesUtil.obtenerCodigoProcesoAutorizarDescVar(request);
	  		
			for(VistaPedidoDTO vistaPedidoDTO:visPedColConsAux){
				
				//se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
				LogSISPE.getLog().info("codigoLocal: {}",vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				LogSISPE.getLog().info("codigoPedido: {}",vistaPedidoDTO.getId().getCodigoPedido());
				LogSISPE.getLog().info("codigoEstado: {}",vistaPedidoDTO.getId().getCodigoEstado());
				LogSISPE.getLog().info("secuencialEstadoPedido: {}",vistaPedidoDTO.getId().getSecuencialEstadoPedido());
				articuloDTO = new ArticuloDTO();
				//articuloDTO.setArticuloComercialDTO(new ArticuloComercialDTO());
				//mvega
				articuloDTO.setArticuloUnidadManejoCol(new ArrayList<ArticuloUnidadManejoDTO>());
				articuloDTO.getArticuloUnidadManejoCol().add(new ArticuloUnidadManejoDTO());
				
				VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
				consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
				consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
				consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());			
				consultaVistaDetallePedidoDTO.setArticuloDTO(articuloDTO);
				consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
				detalleVistaPedido = (List<VistaDetallePedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
				EstadoPedidoUtil.mostrarDetallesPedidoPorEstado(vistaPedidoDTO, detalleVistaPedido);
				
				LogSISPE.getLog().info("detalle vista pedido: {}",detalleVistaPedido.size());
				String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
				//se crea la colecci\u00F3n de art\u00EDculos para realizar la consulta de los stocks y alcances
				ArrayList <ArticuloDTO> articulos = new ArrayList <ArticuloDTO>();
				//contador para los precios alterados
				int contadorPreciosAlterados = 0;
				
				//variable para sumar cuantos articulos han sido entregados
				long contadorEntrega=0;
				//variable para sumar cuantos articulos han sido despachados
				long contadorDespacho=0; 
				
				//verificamos si el pedido tiene costo flete
				if (vistaPedidoDTO.getValorCostoEntregaPedido()!=null){
					costoEntregaPedido += vistaPedidoDTO.getValorCostoEntregaPedido();
				}
				
				//SE RECUPERAN LAS LLAVES DE LOS DESCUENTOS				
				WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO, request, eliminarConsolidacion);
				
				//coleccion de detalles de cada vistaPedidoDTO
				Collection<DetallePedidoDTO> detallesPedidosActuales = new ArrayList<DetallePedidoDTO>();
				
				//se itera la vistaDetallePedido para crear un DetallePedidoDTO
				for (int i=0;i<detalleVistaPedido.size();i++){
					
					contadorEntrega=0;
					contadorDespacho=0;
					boolean precioAlterado = false; //variable para el control de cambios en los precios
					VistaDetallePedidoDTO vistaDetallePedidoDTO = detalleVistaPedido.get(i);
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
					detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
					detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoEstado(vistaDetallePedidoDTO.getId().getCodigoEstado());
					detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
					detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
					//este campo se asigna para las entregas
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadParcialEstado(0L);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setReservarBodegaSIC(estadoInactivo);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(0L);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstadoReservado(0D);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEspecialReservado(estadoInactivo);
					
					//aplica precio caja/mayorista
					detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioCaja(vistaDetallePedidoDTO.getAplicaPrecioCaja());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaPrecioMayorista(vistaDetallePedidoDTO.getAplicaPrecioMayorista());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
		
					//cambios para que no se pierdan los descuentos de pedidos consolidados cuando registra pesos finales
					//para cuando va a registrar pesos finales
					if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null &&
							request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
					//solo si el art\u00EDculo es de peso variable
			          if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_PAVO)){
			          	if(vistaDetallePedidoDTO.getPesoRegistradoLocal() == null)
			          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoArticuloEstado());
			          	else{
			          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
			          		detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());
			          	}
			          }
					}else{
			      	//se inicializa el peso aproximado
						if(vistaDetallePedidoDTO.getPesoRegistradoLocal()!=null && vistaDetallePedidoDTO.getPesoRegistradoLocal().doubleValue()!=0){
							detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoRegistradoLocal());
							detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());
						}else{
							detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
							detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(null);
						}
					}
			          
					//estos campos pueden variar al final de este m\u00E9todo, en el caso que se deban aplicar descuentos 
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoDescuento(vistaDetallePedidoDTO.getValorTotalEstadoDescuento());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(vistaDetallePedidoDTO.getValorTotalEstadoNeto());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(vistaDetallePedidoDTO.getValorTotalEstadoNetoIVA());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(vistaDetallePedidoDTO.getValorPrevioVenta());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(vistaDetallePedidoDTO.getValorFinalEstadoDescuento());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(vistaDetallePedidoDTO.getValorUnitarioPOS());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(vistaDetallePedidoDTO.getEstadoCanCotVacio());
					detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo() ? 
									vistaDetallePedidoDTO.getArticuloDTO().getCantidadMayoreo():0);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(vistaDetallePedidoDTO.getAplicaIVA());
					
					
					//Busca si la clasificacion del articulo del pedido coincide con las clasificaciones que administra el comprador del descuento variable
					FuncionarioDTO funcionarioAutorizador = AutorizacionesUtil.obtenerFuncionarioAutorizadorPorClasificacion(AutorizacionesUtil.obtenerCodigoClasificacion(detallePedidoDTO),
							AutorizacionesUtil.obtenerTipoMarca(detallePedidoDTO), request, codigoAutorizarDescVar, new ActionMessages());
					
					if(funcionarioAutorizador != null && StringUtils.isNotEmpty(funcionarioAutorizador.getUsuarioFuncionario())){
						detallePedidoDTO.setNpIdAutorizador(funcionarioAutorizador.getUsuarioFuncionario());
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
											detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1]);
								}
							}
						}
						LogSISPE.getLog().info("Tiene {} autorizacion(es) el articulo {}", autorizacionesCol.size(),vistaDetallePedidoDTO.getId().getCodigoArticulo()+" del pedido "+vistaDetallePedidoDTO.getId().getCodigoPedido());
						detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(autorizacionesCol);
						
						if(session.getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS) != null ){
							//no se muestran las autorizaciones que no fueron generadas desde la consolidacion
							AutorizacionesUtil.eliminarAutorizacionesNoSonConsolidadas(request, detallePedidoDTO); 
						}
					}

					//se llama a la funci\u00F3n que realiza el control y asignaci\u00F3n de precios
					precioAlterado = CotizacionReservacionUtil.controlPrecios(vistaDetallePedidoDTO, detallePedidoDTO.getEstadoDetallePedidoDTO(), request);
					
					//se validan los datos de los articulos
					int cantidadErrores = errors.size();
					UtilesSISPE.validarArticuloDetallePedido(detallePedidoDTO, errors);
					if(cantidadErrores != errors.size()){
						throw new SISPEException("Error al obtener el art\u00EDculo, existen problemas con la informaci\u00F3n registrada");
					}
					
					//llamada al m\u00E9todo que determina los totales por detalle      
					CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, false,false);
					
					LogSISPE.getLog().info("----contador entrega---- {}", contadorEntrega);
					LogSISPE.getLog().info("----contador despacho---- {}", contadorDespacho);
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-contadorEntrega));
					detallePedidoDTO.setNpContadorDespacho(Long.valueOf(contadorDespacho));
					detallePedidoDTO.setNpContadorEntrega(Long.valueOf(contadorEntrega));
					//se asignan las entregas por cada detalle
					detallePedidoDTO.setEntregaDetallePedidoCol(vistaDetallePedidoDTO.getEntregaDetallePedidoCol());
		
					detallePedidoDTO.setNpCodigoClasificacion(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion());
					detallePedidoDTO.setNpCodigoClasificacionArticulo(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion()
							+ caracterToken + vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
					detallePedidoDTO.setNpCodigoTipoDescuento(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento());
					detallePedidoDTO.setNpCodigoTipoDescuentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpCodigoTipoDescuento()
							+ caracterToken	+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo() + caracterToken + i);
					//campos para registrar el pedido por departamento
					detallePedidoDTO.setNpDepartamento(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento());
					detallePedidoDTO.setNpDepartamentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento()
							+ caracterToken	+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
		
					//se verifica si el art\u00EDculo es un canasto
//							if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta")) || 
//									vistaDetallePedidoDTO.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"))){
					if(vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
							vistaDetallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
						//se llama al m\u00E9todo que carga la receta de cada art\u00EDculo que es un canasto
						CotizacionReservacionUtil.cargarRecetaArticulo(request, detallePedidoDTO, vistaPedidoDTO.getId().getSecuencialEstadoPedido(),verificarSIC,null, false);
							if(detallePedidoDTO.getNpRecetaActualEsDiferenteAHistorial()){
								precioAlterado = true;
							}
					}
					
					//llamada al m\u00E9todo que construye la consulta de los art\u00EDculos
					WebSISPEUtil.construirConsultaArticulos(request,detallePedidoDTO.getArticuloDTO(), estadoInactivo, estadoInactivo, accionActual);
					//se almacenan los detalles y los art\u00EDculos
					detallePedido.add(detallePedidoDTO);
					detallesPedidosActuales.add(detallePedidoDTO.clone());
//							ConsolidarAction.agregarDetallePedidoConsolidado(request, detallePedido, detallePedidoDTO);
					
//							detallePedido1.add((DetallePedidoDTO)SerializationUtils.clone(detallePedidoDTO));
					articulos.add(detallePedidoDTO.getArticuloDTO());
					//se almacenan los codigos de art\u00EDculos del detalle
					codigosArticulos.add(vistaDetallePedidoDTO.getId().getCodigoArticulo());

					//si se altero el precio de un art\u00EDculo
					if(precioAlterado){
						contadorPreciosAlterados++;
					}
					LogSISPE.getLog().info("estadoArticuloSIC *: {}, estadoArticuloSICReceta *: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());
				}
				
				String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
				LogSISPE.getLog().info("accion actual: {}", accion);
				
				//si esta pagado totalmente no se debe realizar ning\u00FAn recalculo adicional
				if(contadorPreciosAlterados > 0 && !vistaPedidoDTO.getCodigoEstadoPagado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))){
					
					if(!accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))){
						//reprocesarDescuentos = true;
						session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS, "CAMBIO_PRECIO");
						session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS,"SI"); 
						//se verifica si hubo cambio en los precios
						String actualizarPrecios=((String)session.getAttribute("ec.com.smx.sic.sispe.confirmarActualizarPrecios"));
						LogSISPE.getLog().info("ACTUALIZAR PRECIOS: {}",actualizarPrecios);
						if(actualizarPrecios==null && request.getParameter("linkConsolidadoGeneral") == null){
							if(session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS) != null && !((String)session.getAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS)).equals("ok")){
								session.removeAttribute(SessionManagerSISPE.POPUP);
								session.setAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS,"ok");
								instanciarVentanaModificacionPreciosConsolidados(request);
							}
						}
					}else{
						LogSISPE.getLog().info("cuando es pedido consolidado con cambio de precios en confirmacion de pesos finales no se muestra el popUp de cambio de precios");
						session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS_CONSOLIDADOS);
						session.removeAttribute(CotizacionReservacionUtil.PRECIOS_ALTERADOS); 
					}
				}
				
				//se setean los detalles de cada pedido en el vistaPedidoActual
				LogSISPE.getLog().info(" pedido: "+vistaPedidoDTO.getId().getCodigoPedido()+" tamano detalles: "+detallesPedidosActuales.size());
				vistaPedidoDTO.setDetallesPedidosSeleccionados(detallesPedidosActuales);
				vistaPedidoDTO.setVistaDetallesPedidos(detalleVistaPedido);
			}
			
			if (costoEntregaPedido >= 0D){
				session.setAttribute(CotizacionReservacionUtil.COSTO_FLETE_ENTREGA_PEDIDO_CONSOLIDADO, costoEntregaPedido);
			}
			
			//se elimina de sesion el parametro
			session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
			
			LogSISPE.getLog().info("Total de detalles de pedido a agregar al pedido general para calcular nuevamente todo {}",detallePedido);
			return detallePedido;
		}
		
			
		  /**
		   * 
		   * @param request
		   * @param cotizarReservarForm
		   * @param subTotalAplicaIVA
		   * @param subTotalNoAplicaIVA
		   * @throws Exception
		   */
			public static void calcularTotalesPedidosConsolidado(HttpServletRequest request, double subTotalAplicaIVA, 
		  		double subTotalNoAplicaIVA, PedidoDTO pedidoConsolidadoDTO,Collection<DescuentoEstadoPedidoDTO> colDescuentoEstadoPedidoConsolidado,
		  		VistaPedidoDTO vistaPedidoConsolidadoDTO,Boolean esEliminarConsolidados)throws Exception{
			String estadoActivoAux = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
			String estadoInactivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
		  	
		  	HttpSession session = request.getSession();
		  	String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		  	//se formatean los totales del pedido antes de almacenarlos
			//Double descuentoFormateado =0D;// Util.roundDoubleMath(formulario.getDescuentoTotal(),NUMERO_DECIMALES);
			Double subTotalFormateado =0D;// Util.roundDoubleMath(formulario.getSubTotal(),NUMERO_DECIMALES);
			Double subTotalAplicaIVAFormateado =0D;// Util.roundDoubleMath(formulario.getSubTotalAplicaIVA(),NUMERO_DECIMALES);
			Double subTotalNoAplicaIVAFormateado = 0D;//Util.roundDoubleMath(formulario.getSubTotalNoAplicaIVA(),NUMERO_DECIMALES);
			Double ivaTotalFormateado = 0D;//Util.roundDoubleMath(formulario.getIvaTotal(),NUMERO_DECIMALES);
			Double totalPedidoFormateado = 0D;//Util.roundDoubleMath(formulario.getTotal(),NUMERO_DECIMALES);
		  	
		  	//se realizan algunos c\u00E1lculos y se asigna el resulatado a las variables del formulario
			subTotalFormateado=Double.valueOf(subTotalAplicaIVA + subTotalNoAplicaIVA);
			subTotalNoAplicaIVAFormateado=subTotalNoAplicaIVA;
		  	if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
		  		//CALCULOS NORMALES
			  	//se calcula el valor sin IVA del subTotalAplicaIVA
			  	double subTotalAplicaIVASinIVA = subTotalAplicaIVA / (1 + Double.valueOf(SessionManagerSISPE.getValorIVA(request)).doubleValue());
			  	subTotalAplicaIVASinIVA = Util.roundDoubleMath(Double.valueOf(subTotalAplicaIVASinIVA), NUMERO_DECIMALES);
			  	subTotalAplicaIVAFormateado=Double.valueOf(subTotalAplicaIVASinIVA);
			  	double valorIVA = subTotalAplicaIVA - subTotalAplicaIVASinIVA;
			  	ivaTotalFormateado=valorIVA;
			  	totalPedidoFormateado=subTotalFormateado;
		  	}else{
		  		//CALCULOS SIN IVA
		  		subTotalNoAplicaIVAFormateado=(subTotalNoAplicaIVA + subTotalAplicaIVA); //se guarda en este campo porque los c\u00E1lculos se hicieron sin iva
		  		subTotalAplicaIVAFormateado=0D;
		  		ivaTotalFormateado=0D;
		  		totalPedidoFormateado=subTotalFormateado;
		  	}
		  	
//						//solo cuando es una reservaci\u00F3n se va a cumplir la condici\u00F3n
//						Collection costoEntregasDTOCol = (Collection)session.getAttribute(EntregaLocalCalendarioAction.COSTOENTREGA);
//						//esta variable la obtengo de la clase EntregaLocalCalendarioAction, indica si la forma de realizar las entregas
//						//a domicilio es por sector o no
//						String entregaDomicilioPorSector = (String)session.getAttribute("ec.com.smx.sic.sispe.entregaDirLocal");
//						if(costoEntregasDTOCol!=null && entregaDomicilioPorSector!=null){
//							double valorTotalEntregas = 0;
//							for(Iterator it = costoEntregasDTOCol.iterator(); it.hasNext(); ){
//								CostoEntregasDTO costoEntregasDTO = (CostoEntregasDTO)it.next();
//								costoEntregasDTO.setValor(WebSISPEUtil.costoEntrega(totalPedidoFormateado, costoEntregasDTO.getPorcentajeCostoFlete()));
//								valorTotalEntregas = valorTotalEntregas + costoEntregasDTO.getValor();
//							}
//							session.setAttribute(EntregaLocalCalendarioAction.VALORTOTALENTREGA, Double.valueOf(valorTotalEntregas));
//						}
				//se obtiene la suma de los decuentos
				Double totalDescuento=0D;
				Double totalPorcentaje=0D;
				double porcentajeSubTotalDescuento=0D;
				EstadoPedidoDTO estadoPedidoDTO =null;
				for(DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO : colDescuentoEstadoPedidoConsolidado){
						
						totalDescuento = totalDescuento + descuentoEstadoPedidoDTO.getValorDescuento().doubleValue();
						totalPorcentaje = totalPorcentaje + descuentoEstadoPedidoDTO.getPorcentajeDescuento().doubleValue();
				}
				//se asignan los totales por estado
				estadoPedidoDTO = new EstadoPedidoDTO();
				estadoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				estadoPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCodigoLocalObjetivo(request));
				estadoPedidoDTO.setDescuentoTotalPedido(Util.roundDoubleMath(Double.valueOf(totalDescuento), NUMERO_DECIMALES)); //aqui guarda  los pedidos
				estadoPedidoDTO.setPorcentajeTotalDescuento(Util.roundDoubleMath(Double.valueOf(totalPorcentaje), NUMERO_DECIMALES));
				porcentajeSubTotalDescuento= estadoPedidoDTO.getDescuentoTotalPedido().doubleValue()*100 / subTotalFormateado.doubleValue();
				estadoPedidoDTO.setPorcentajeSubTotalDescuento(Util.roundDoubleMath(Double.valueOf(Double.valueOf(porcentajeSubTotalDescuento)), NUMERO_DECIMALES));
				estadoPedidoDTO.setSubTotalPedido(Util.roundDoubleMath(Double.valueOf(subTotalFormateado), NUMERO_DECIMALES));
				estadoPedidoDTO.setSubTotalAplicaIVA(Util.roundDoubleMath(Double.valueOf(subTotalAplicaIVAFormateado), NUMERO_DECIMALES));
				estadoPedidoDTO.setSubTotalNoAplicaIVA(Util.roundDoubleMath(Double.valueOf(subTotalNoAplicaIVAFormateado), NUMERO_DECIMALES));
				estadoPedidoDTO.setIvaPedido(Util.roundDoubleMath(Double.valueOf(ivaTotalFormateado), NUMERO_DECIMALES));
				estadoPedidoDTO.setTotalPedido(Util.roundDoubleMath(Double.valueOf(totalPedidoFormateado), NUMERO_DECIMALES));
				String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
				estadoPedidoDTO.setUserId(userId);
				//verifica forma de pago
				if(vistaPedidoConsolidadoDTO.getNpPagoEfectivo() != null){
					estadoPedidoDTO.setPagoEfectivo(estadoActivoAux);
				}else{
					estadoPedidoDTO.setPagoEfectivo(estadoInactivo);
				}
				
				//se asigna el estado de calculos con precios de afiliado
				if(session.getAttribute(CALCULO_PRECIOS_AFILIADO)!=null){
					estadoPedidoDTO.setEstadoPreciosAfiliado(estadoActivo);
				}else{
					estadoPedidoDTO.setEstadoPreciosAfiliado(estadoInactivo);
				}
				
				//se asigna el estado de calculos con iva o sin iva
				estadoPedidoDTO.setEstadoCalculosIVA((String)session.getAttribute(CALCULO_PRECIOS_CON_IVA));
				if(estadoPedidoDTO.getEstadoCalculosIVA() == null){
					estadoPedidoDTO.setEstadoCalculosIVA(estadoActivo);
				}
				LogSISPE.getLog().info("Total descuento {}",Util.roundDoubleMath(Double.valueOf(totalDescuento), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total totalPorcentaje {}",Util.roundDoubleMath(Double.valueOf(totalPorcentaje), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total porcentajeSubTotalDescuento {}",Util.roundDoubleMath(Double.valueOf(porcentajeSubTotalDescuento), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total subTotalFormateado {}",Util.roundDoubleMath(Double.valueOf(subTotalFormateado), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total subTotalAplicaIVAFormateado {}",Util.roundDoubleMath(Double.valueOf(subTotalAplicaIVAFormateado), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total subTotalNoAplicaIVAFormateado {}",Util.roundDoubleMath(Double.valueOf(subTotalNoAplicaIVAFormateado), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total ivaTotalFormateado {}",Util.roundDoubleMath(Double.valueOf(ivaTotalFormateado), NUMERO_DECIMALES));
				LogSISPE.getLog().info("Total totalPedidoFormateado {}",Util.roundDoubleMath(Double.valueOf(totalPedidoFormateado), NUMERO_DECIMALES));
				
				
				pedidoConsolidadoDTO.setEstadoPedidoDTO(estadoPedidoDTO);
		  }
			  
		/**
		 * 
		 * @param request
		 * @param errors
		 * @param session
		 * @param accion
		 * @param estadoActivo
		 * @param estadoInactivo
		 * @throws Exception
		 */
		public static void actualizarDetallePedidosConsolidados(HttpServletRequest request, String estadoActivo, String estadoInactivo,
				String rucEmpresa,String opTipoDocumento,Collection<DetallePedidoDTO> detallePedido) throws Exception{
			
			LogSISPE.getLog().info("Actualizar detalle precios consolidados");
			
			HttpSession session = request.getSession();
			
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitarPrecioMayoristaFormatoNegocioClasificacion", request);
			session.setAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO,parametroDTO.getValorParametro());
			
			boolean calculosPreciosAfiliado = true;
			boolean intercambiarPrecios = false;
			String tipoArticuloPavo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo");
			final String TIPO_ARTICULO_OTRO_PESO_VARIABLE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.otroPesoVariable");
			if(request.getSession().getAttribute(CALCULO_PRECIOS_AFILIADO)==null){
				calculosPreciosAfiliado = false;
			}
			
			String []vectorCantidad= new String[detallePedido.size()];
			String []vectorPesoPavos= new String[detallePedido.size()];
			String []vectorPrecio= new String[detallePedido.size()];
			String []vectorPrecioNoAfi= new String[detallePedido.size()];
			String []vectorPeso= new String[detallePedido.size()];
			String []vectorPesoActual= new String[detallePedido.size()];
			
			int pos=0;
			int posPesos=0;
			for (DetallePedidoDTO detPedDTO : detallePedido)
			{
				
				vectorCantidad[pos]=String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
				if(detPedDTO.getArticuloDTO().getTipoCalculoPrecio().equals(tipoArticuloPavo)){
					//se inicializa el peso total aproximado
					//double pesoTotalAproximado = detPedDTO.getArticuloDTO().getPesoAproximado().doubleValue() * detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					double pesoTotalAproximado = detPedDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta()  * detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					detPedDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(pesoTotalAproximado));
					vectorPesoPavos[posPesos]=String.valueOf(pesoTotalAproximado);
					vectorPesoActual[posPesos]=String.valueOf(pesoTotalAproximado);
					posPesos++;
				}
				if(detPedDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
					//double pesoTotalAproximado = detPedDTO.getArticuloDTO().getPesoAproximado().doubleValue() * detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					double pesoTotalAproximado = detPedDTO.getArticuloDTO().getArticuloComercialDTO().getPesoAproximadoVenta() * detPedDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					vectorPeso[pos]=String.valueOf(pesoTotalAproximado);
				}
					vectorPrecio[pos]=String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado());
					vectorPrecioNoAfi[pos]=String.valueOf(detPedDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado());
//							detPedDTO.setNpCantidadTotalConsolidada(0L);
//							detPedDTO.setNpValorTotalConsolidado(0D);
				pos++;
			}
			
			intercambiarPrecios = true;
//						this.checkCalculosPreciosMejorados = estadoActivo;
			
			//se verifica la empresa para calculos con iva o sin iva
			CotizacionReservacionUtil.verificarEmpresaExentaIVA(request, rucEmpresa, opTipoDocumento);
			
			LogSISPE.getLog().info("se actualiza el detalle");
			//cuando se a\u00F1adieron art\u00EDculos por la b\u00FAsqueda no se itera el detalle para actualizarlo
			if(session.getAttribute(BuscarArticuloAction.ART_AGREGADOS_BUSQUEDA_PED) == null)
			{
				//se obtiene el detalle del pedido de sesi\u00F3n
				//Collection detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);

				Double porcentajePrecioAfiliado = (Double)session.getAttribute(PORCENTAJE_CALCULO_PRECIOS_AFILIADO);
				//si no ocurrio alg\u00FAn error y el detalle no est\u00E1 vacio
				if(detallePedido!=null && !detallePedido.isEmpty() 
						&& vectorCantidad!=null && (vectorPrecio!=null || vectorPrecioNoAfi!=null))
				{
					LogSISPE.getLog().info("TAMANO DETALLE: {}",detallePedido.size());
					int indiceDetalle = 0;
					//StringBuffer articulosObsoletos = null;
					//UtilPopUp popUp = null;
					//esta colecci\u00F3n contiene los \u00EDndices de las cantidades modificadas con un valor menor al original
//							ArrayList indicesModificados = (ArrayList)session.getAttribute(CotizarReservarAction.COL_INDICES_CANTIDADES_MODIFICADAS);
//							int contadorIndicesModificados = 0;
					//se obiene la constante que representa la acci\u00F3n confirmar reservaci\u00F3n
					//String accionConfirmarReservacion = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion");
					//se obtiene la clave que indica una modificaci\u00F3n de precios en el detalle principal
					String inputsActivosModificacionPrecios = (String)session.getAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS);

					//se itera la colecci\u00F3n de los detalles del pedido
					for (DetallePedidoDTO detallePedidoDTO : detallePedido)
					{
												
						try
						{
//										//se verifica la acci\u00F3n sobre la cual se est\u00E1 actualizando el detalle del pedido
//										if(accion!=null && accion.equals(accionConfirmarReservacion)){
//											//solo para los art\u00EDculos de peso variable
//											if(detallePedidoDTO.getArticuloDTO().getTipoArticuloCalculoPrecio().equals(tipoArticuloPavo)){
//												//si el usuario ingresa un peso menor que cero se hace positivo
//												if(Double.parseDouble(vectorPeso[indiceDetalle])<0){
//													double peso = Double.parseDouble(vectorPeso[indiceDetalle]);
//													peso = (-1)*peso;
//													vectorPeso[indiceDetalle] = Double.toString(peso);
//												}else if(Double.parseDouble(vectorPeso[indiceDetalle])==0 || Double.parseDouble(vectorPeso[indiceDetalle])>999999.99){
//													vectorPeso[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().toString();
//												}
//												
//												//se actualiza el peso en el objeto del estado
//												detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(Double.valueOf(vectorPeso[indiceDetalle]));
//												//se recalcula el valor de la cantidad
//												//detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(CotizacionReservacionUtil.recalcularCantidadPorModificacionPesos(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal(), detallePedidoDTO.getArticuloDTO().getPesoAproximado()));
//												//detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadAjustadaModificacionPeso(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
//												//lamada a la funci\u00F3n que calcula el total del detalle
//												CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, true);
//											}
//										}else{
								//CONTROL DE LA CANTIDAD INGRESADA
//										LogSISPE.getLog().info("vectorCantidad: {}",vectorCantidad);
								//se crea esto en caso de que se ingresen decimales
								Double cantidadD = Double.valueOf(vectorCantidad[indiceDetalle]);
								long cantidad = cantidadD.longValue();
								vectorCantidad[indiceDetalle] = Long.toString(cantidad);

								//si el usuario ingresa un n\u00FAmero menor que cero se hace positivo
								if(cantidad < 0){
									long numPositivo = cantidad;
									numPositivo = (-1)*numPositivo;
									vectorCantidad[indiceDetalle] = Long.toString(numPositivo);
								}else if(cantidad == 0)
									vectorCantidad[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().toString();

								//CONTROL DEL PESO INGRESADO PARA LOS ARTICULOS QUE NO SON PAVOS
								if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
									if(Double.parseDouble(vectorPeso[indiceDetalle])<0){
										double peso = Double.parseDouble(vectorPeso[indiceDetalle]);
										peso = (-1)*peso;
										vectorPeso[indiceDetalle] = Double.toString(peso);
									}else if(Double.parseDouble(vectorPeso[indiceDetalle])==0 || Double.parseDouble(vectorPeso[indiceDetalle])>999999.99){
										vectorPeso[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().toString();
									}else{
										//se calcula la cantidad en base al peso
										Double cantidadGenerada = Math.ceil(Double.parseDouble(vectorPeso[indiceDetalle]));
										vectorCantidad[indiceDetalle] = String.valueOf(cantidadGenerada.longValue());
									}
								}
								
								//CONTROL DEL PRECIO INGRESADO
								//solo si existe la posibilidad de cambio de precios
								if(inputsActivosModificacionPrecios != null){
									if(calculosPreciosAfiliado){
										//si el usuario ingresa un precio menor que cero se hace positivo
										if(Double.parseDouble(vectorPrecio[indiceDetalle])<0){
											double numPositivo = Double.parseDouble(vectorPrecio[indiceDetalle]);
											numPositivo = (-1)*numPositivo;
											vectorPrecio[indiceDetalle] = Double.toString(numPositivo);
										}else if(Double.parseDouble(vectorPrecio[indiceDetalle])==0 || Double.parseDouble(vectorPrecio[indiceDetalle])>99999.99){
											if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
												//se verifica si el art\u00EDculo aplica IVA
												vectorPrecio[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().toString();
											else
												vectorPrecio[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().toString();
										}
									}else{
										//si el usuario ingresa un precio menor que cero se hace positivo
										if(Double.parseDouble(vectorPrecioNoAfi[indiceDetalle])<0){
											double numPositivo = Double.parseDouble(vectorPrecioNoAfi[indiceDetalle]);
											numPositivo = (-1)*numPositivo;
											vectorPrecioNoAfi[indiceDetalle] = Double.toString(numPositivo);
										}else if(Double.parseDouble(vectorPrecioNoAfi[indiceDetalle])==0 || Double.parseDouble(vectorPrecioNoAfi[indiceDetalle])>99999.99){
											if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
												//se verifica si el art\u00EDculo aplica IVA
												vectorPrecioNoAfi[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVANoAfiliado().toString();
											else
												vectorPrecioNoAfi[indiceDetalle] = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().toString();
										}
									}
								}

								//se obtiene la colecci\u00F3n de entregas
//										LogSISPE.getLog().info("INDICES CANTIDADES MODIFICADAS: {}",indicesModificados);

//											//se actualiz\u00F3 el detalle luego dar clic en el bot\u00F3n que registra el pedido
//											if(indicesModificados!=null){
//												//si el contador de indices es menor al tama\u00F1o de la colecci\u00F3n de indices modificados
//												if(contadorIndicesModificados < indicesModificados.size()){
//													String indice = indicesModificados.get(contadorIndicesModificados).toString();
//													if(indiceDetalle == Integer.parseInt(indice)){
//														LogSISPE.getLog().info("eliminar entregas por modificacion cuando indicesModificados is null");
//														eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, errors, request, true);
//														contadorIndicesModificados++;
//													}
//												}
//											}else{
//												//se actualiz\u00F3 el detalle inmediatamente luego de modificar las cantidades
//												if(Long.parseLong(vectorCantidad[indiceDetalle]) < detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()){
//													LogSISPE.getLog().info("eliminar entregas por modificacion cuando indicesModificados <> null");
//													eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, errors, request, true);
//												}
//											}
								
								//Control de asignaciones en los precios modificados
								controlCambiosPrecioConsolidados(detallePedidoDTO, indiceDetalle, inputsActivosModificacionPrecios, 
										calculosPreciosAfiliado, porcentajePrecioAfiliado, request,vectorPrecio,vectorPrecioNoAfi);

								//se almacena en la colecci\u00F3n de detalles las nuevas cantidades ingresadas
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(Long.valueOf(vectorCantidad[indiceDetalle]));	
								
								LogSISPE.getLog().info("es canasto para nuevas cotizaciones: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio());
								
								//este atributo se utiliza en la secci\u00F3n de entregas
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(Long.valueOf(vectorCantidad[indiceDetalle]).longValue() - detallePedidoDTO.getNpContadorEntrega().longValue());
								
								
								//esta condici\u00F3n es para el caso de pesos variables que no son pavos
								if(detallePedidoDTO.getArticuloDTO().getTipoCalculoPrecio().equals(TIPO_ARTICULO_OTRO_PESO_VARIABLE)){
									//se almacena el peso ingresado
									detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(Double.valueOf(vectorPeso[indiceDetalle]));
								}

								//se recalcula el valor total del canasto en algunos casos
								CotizacionReservacionUtil.recalcularPrecioReceta(detallePedidoDTO, request);
								//llamada a la funci\u00F3n que calcula el total del detalle
								CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, intercambiarPrecios,true);

								//control del stock para el detalle
								if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()!=null){
									long stockArticulo = detallePedidoDTO.getArticuloDTO().getNpStockArticulo().longValue();
									if(Long.parseLong(vectorCantidad[indiceDetalle])<=stockArticulo)
										detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoActivo);
									else
										detallePedidoDTO.getArticuloDTO().setNpEstadoStockArticulo(estadoInactivo);
								}
//										}
							
						}catch(Exception e){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
						}

						indiceDetalle++;
					}
				}
			}
			
			//se elimina de sesion el parametro
			session.removeAttribute(VALIDA_MAYORISTA_ESTABLECIMIENTO_CLASIFICACIO);
		}
					
	/**
	 * 
	 * @param detallePedidoDTO
	 * @param precioUnitarioEstado
	 * @param precioUnitarioIVAEstado
	 * @param indiceVector
	 * @param estadoModificacionPrecios
	 * @param calculosPreciosAfiliado
	 * @param porcentajePrecioAfiliado
	 * @param request
	 * @return
	 */
	private static boolean controlCambiosPrecioConsolidados(DetallePedidoDTO detallePedidoDTO, int indiceVector,String estadoModificacionPrecios, boolean calculosPreciosAfiliado,
			Double porcentajePrecioAfiliado, HttpServletRequest request,String []vectorPrecio,String []vectorPrecioNoAfi) throws Exception{
		
		//se obtiene el detalle del pedido
		EstadoDetallePedidoDTO estadoDetallePedidoDTO = detallePedidoDTO.getEstadoDetallePedidoDTO();
		
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		
		boolean detalleModificado = false;
		//solo si existe la posibilidad de cambio de precios
		if(estadoModificacionPrecios != null){
			Double precioVector = null;
			Double precioUnitarioEstadoRound = null;
			//control de cambios en el campo del valor unitario
			if(calculosPreciosAfiliado){
				//precios de afiliado
				precioVector = Util.roundDoubleMath(Double.valueOf(vectorPrecio[indiceVector]),NUMERO_DECIMALES);
				precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioIVAEstado(), NUMERO_DECIMALES);
				if(!SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
					precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioEstado(), NUMERO_DECIMALES);
			}else{
				//precios de no afiliado
				precioVector = Util.roundDoubleMath(Double.valueOf(vectorPrecioNoAfi[indiceVector]),NUMERO_DECIMALES);
				precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioIVANoAfiliado(), NUMERO_DECIMALES);
				if(!SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo))
					precioUnitarioEstadoRound = Util.roundDoubleMath(estadoDetallePedidoDTO.getValorUnitarioNoAfiliado(), NUMERO_DECIMALES);
			}

			Double precioVectorAfiliado = null;
			Double precioVectorNoAfiliado = null;
			
			LogSISPE.getLog().info("precio vector: {}, precio actual: {}" , precioVector,precioUnitarioEstadoRound);
			//se comparan los precios
			if(precioVector.doubleValue() > 0 && precioUnitarioEstadoRound.doubleValue()!= precioVector.doubleValue()){
				
				//se verifica si los c\u00E1lculos son con precio de afiliado
				if(calculosPreciosAfiliado){
					precioVectorAfiliado = precioVector;
					precioVectorNoAfiliado = UtilesSISPE.aumentarPorcentajeAPrecio(precioVector, porcentajePrecioAfiliado);
					vectorPrecioNoAfi[indiceVector] = precioVectorNoAfiliado.toString();
				}else{
					precioVectorNoAfiliado = precioVector;
					precioVectorAfiliado = UtilesSISPE.disminuirPorcentajeAPrecio(precioVector, porcentajePrecioAfiliado);
					vectorPrecio[indiceVector] = precioVectorAfiliado.toString();
				}
				
				if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
					LogSISPE.getLog().info("valorUnitario: {}, valorUnitarioNoAf: {}",precioVectorAfiliado ,precioVectorNoAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioIVAEstado(precioVectorAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(precioVectorNoAfiliado);
					//se verifica si el art\u00EDculo aplica IVA
					if(estadoDetallePedidoDTO.getAplicaIVA().equals(estadoActivo)){
						//se recalcula el precio sin IVA (AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioSinIVA = precioVectorAfiliado.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioEstado(Util.roundDoubleMath(valorUnitarioSinIVA,NUMERO_DECIMALES));
						
						//se recalcula el precio sin IVA (NO AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioNoAfiSinIVA = precioVectorNoAfiliado.doubleValue() / (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(Util.roundDoubleMath(Double.valueOf(valorUnitarioNoAfiSinIVA),NUMERO_DECIMALES));
					}else{
						estadoDetallePedidoDTO.setValorUnitarioEstado(precioVectorAfiliado);
						estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(precioVectorNoAfiliado);
					}
				}else{
					estadoDetallePedidoDTO.setValorUnitarioEstado(precioVectorAfiliado);
					estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(precioVectorNoAfiliado);
					
					//se verifica si el art\u00EDculo aplica IVA
					if(estadoDetallePedidoDTO.getAplicaIVA().equals(estadoActivo)){
						//se recalcula el precio con IVA (AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioConIVA = precioVectorAfiliado.doubleValue() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioIVAEstado(Util.roundDoubleMath(valorUnitarioConIVA,NUMERO_DECIMALES));
						
						//se recalcula el precio con IVA (NO AFILIADO) y se lo asigna al estado del detalle
						double valorUnitarioNoAfiConIVA = precioVectorNoAfiliado.doubleValue() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
						estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(Util.roundDoubleMath(Double.valueOf(valorUnitarioNoAfiConIVA),NUMERO_DECIMALES));
					}else{
						estadoDetallePedidoDTO.setValorUnitarioIVAEstado(precioVectorAfiliado);
						estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(precioVectorNoAfiliado);
					}
				}
				
				detalleModificado=true;
				//solo si los precios unitarios fueron modificados se recalcula el precio de caja y el rrecio de mayorista
				UtilesSISPE.recalcularPrecioCaja(detallePedidoDTO);
				UtilesSISPE.recalcularPrecioMayorista(detallePedidoDTO, WebSISPEUtil.obtenerPorcentajeDiferenciaPrecioNormalYMayorista(request));
				LogSISPE.getLog().info("VALOR UNITARIO MODIFICADO EN LA FILA: {}",indiceVector);
			}
		}
		
		return detalleModificado;
	}
		
	/**
	 * Asigna las autorizaciones del pedido consolidado a los detalles de los pedidos
	 * @param request
	 * @param detallePedidoActualCol
	 */
	public static void asignarAutorizacionesDetallesConsolidados(HttpServletRequest request, Collection<DetallePedidoDTO> detallePedidoActualCol){
		try{
			//solo para pedidos consolidados
			if(esPedidoConsolidado(request)){
				LogSISPE.getLog().info("ingresa a asignar las autorizaciones a los detalles consolidados");
				//se obtiene de sesion los detalles con autorizaciones
				Collection<DetallePedidoDTO> detallePedidoSessionCol = (ArrayList<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLES_CONSOLIDADOS_ACTUALES);
				if(CollectionUtils.isNotEmpty(detallePedidoActualCol) && CollectionUtils.isNotEmpty(detallePedidoSessionCol)){
					//se recorren los detalles 
					for(DetallePedidoDTO detalleActual : detallePedidoActualCol){
						
						Boolean tieneAutorizacion = Boolean.FALSE;
						//se recorren los estadosDetalles con autorizaciones
						for(DetallePedidoDTO detalleConAutorizacion : detallePedidoSessionCol){
							//se comparan los IDs
							if(detalleActual.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo().equals(detalleConAutorizacion.getId().getCodigoAreaTrabajo())
								&& detalleActual.getId().getCodigoArticulo().equals(detalleConAutorizacion.getId().getCodigoArticulo())
								&& detalleActual.getId().getCodigoCompania().equals(detalleConAutorizacion.getId().getCodigoCompania())
//									&& detalleActual.getId().getCodigoPedido().equals(detalleConAutorizacion.getId().getCodigoPedido())
								&& CollectionUtils.isNotEmpty(detalleConAutorizacion.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
									detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(detalleConAutorizacion.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol());
									LogSISPE.getLog().info("autorizaciones agregadas: "+detalleConAutorizacion.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().size()+" al articulo: "+detalleActual.getArticuloDTO().getDescripcionArticulo());
									tieneAutorizacion = Boolean.TRUE;
							}
						}
						//se eliminan las autorizaciones
						if(!tieneAutorizacion && !AutorizacionesUtil.verificarArticuloPorTipoAutorizacion(detalleActual, ConstantesGenerales.TIPO_AUTORIZACION_STOCK)){
							detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
							LogSISPE.getLog().info("se quitaron las autorizaciones al articulo: "+detalleActual.getId().getCodigoArticulo()+ " - "+detalleActual.getArticuloDTO().getDescripcionArticulo()+" del pedido : "+detalleActual.getId().getCodigoPedido());
						}
					}
				}
			
				//quiere decir que no existen autorizaciones actuales ni autorizaciones por activar, hay que eliminar las autorizaciones
				else if(CollectionUtils.isNotEmpty(detallePedidoActualCol) &&  request.getSession().getAttribute(AutorizacionesUtil.AUTORIZACIONES_ACTIVAR_COL) == null){
					LogSISPE.getLog().info("va a eliminar las autorizaciones de los detalles de un pedido consolidado");
					//se recorren los detalles 
					for(DetallePedidoDTO detalleActual : detallePedidoActualCol){
						//se recorren los detalles con autorizaciones
						if(CollectionUtils.isNotEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesEliminar = new ArrayList<DetalleEstadoPedidoAutorizacionDTO>();
							
							//se recorren las autorizaciones del detalle
							for(DetalleEstadoPedidoAutorizacionDTO autorizacion : detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
								if(autorizacion.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null
										&& autorizacion.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
									autorizacionesEliminar.add(autorizacion);
								}
							}
							//se eliminan las autorizaciones de descuento variable
							detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol().removeAll(autorizacionesEliminar);
							if(CollectionUtils.isEmpty(detalleActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
								detalleActual.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(null);
								LogSISPE.getLog().info("se quitaron las autorizaciones al articulo: "+detalleActual.getId().getCodigoArticulo()+ " - "+detalleActual.getArticuloDTO().getDescripcionArticulo()+" del pedido : "+detalleActual.getId().getCodigoPedido());
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al obtener los detalles consolidados con autorizaciones");
		}
	}
		
		
	/**
	 * 
	 * @param request
	 * @throws Exception
	 */
	public static void instanciarVentanaModificacionPreciosConsolidados(HttpServletRequest request)throws Exception{
		HttpSession session = request.getSession();
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Precios y recetas modificados");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('crearCotizacion.do', ['pregunta','div_DetalleDescuento','div_pagina','mensajes'], {parameters: 'confirmarActualizarPrecios=ok&intercambioPrecios=ok&preciosActuales=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta','div_DetalleDescuento','div_pagina','mensajes'], {parameters: 'cancelarActualizarPrecios=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/consolidarPedido/confirmarCambioPreciosConsolidados.jsp");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setAncho(60D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	
	/**
	 * popUp para indicar que el canasto que se esta creando es un especial y se va a verificar si el canasto existe en el SIC
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author bgudino
	 */
	public static void instanciarVentanaCambiarCanastosOtrosPedidosConsolidados(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar el popUp instanciarVentanaCambiarCanastosOtrosPedidosConsolidados");
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Informaci\u00F3n");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'cambiarCanastoConsolidacion=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta','div_pagina','mensajes'], {parameters: 'conservarCanastoConsolidacion=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("/servicioCliente/cotizarRecotizarReservar/popUpConservarCanastoConsolidacion.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	///////////////METODOS PARA VALIDAR LAS ENTREGAS CONSOLIDADAS COMO UN PEDIDO NORMAL////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Calcula el total de canastos que tienen todos los pedidos consolidados
	 * @param request
	 * @param codClaRecetasNuevas
	 * @return
	 * @throws Exception 
	 */
	public static void obtenerCantidadCanastosEspecialesConsolidados(HttpServletRequest request, String codClaRecetasNuevas, String codigoBarrasCanastoParcial, 
			Integer cantidadMinima, Double valorMinimo) throws Exception{
		
		
		//se verifica que todos 
		HttpSession session = request.getSession();
		boolean validacionTotal = StringUtils.isEmpty(codigoBarrasCanastoParcial) ? true : false;
		
		Map<String, Map<String, Object>> mapCanastoCantidades = new HashMap<String, Map<String, Object>>();
		String keyCantidadCanastos = "cantidadCanastos";
		String keyValorCanastos = "valorCanastos";
		boolean cumpleCondicion = Boolean.FALSE;	
		
		//primero se valida si se trata de un pedido consolidado
		if(esPedidoConsolidado(request)){
			
			cumpleCondicion = Boolean.TRUE;
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoConsolidado){
					
				
					if(CotizacionReservacionUtil.esCanasto(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), request)){
						
						Integer cantidadCanastos = 0;
						Double valorCanastos = 0D;
						String keyCodigoCanasto = detallePedidoDTO.getArticuloDTO().getCodigoBarrasActivo().getId().getCodigoBarras();
						
						Map<String, Object> mapTemporal = new HashMap<String, Object>();
						
						if(mapCanastoCantidades.containsKey(keyCodigoCanasto)){
							mapTemporal = mapCanastoCantidades.get(keyCodigoCanasto);
							cantidadCanastos = (Integer) mapTemporal.get(keyCantidadCanastos);
							valorCanastos = (Double) mapTemporal.get(keyValorCanastos);
						}
						cantidadCanastos += detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().intValue();
						valorCanastos += detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
						
						mapTemporal.put(keyCantidadCanastos, cantidadCanastos);
						mapTemporal.put(keyValorCanastos, valorCanastos);
						mapCanastoCantidades.put(keyCodigoCanasto, mapTemporal);
					}
				}
			}
			
			if(validacionTotal){
				
				LogSISPE.getLog().info("se valida por entrega total");
				
				//se validan que todos los items del map cumplan la condicion de cantidad minima de canastos o valor
				if(!mapCanastoCantidades.isEmpty()){
					
					Integer cantidadCanastos = 0;
					Double valorCanastos = 0D;
					
					for(Entry<String, Map<String, Object>> mapActual : mapCanastoCantidades.entrySet()){
					
						cantidadCanastos = (Integer)mapActual.getValue().get(keyCantidadCanastos);
						valorCanastos = (Double)mapActual.getValue().get(keyValorCanastos);
						
						LogSISPE.getLog().info("canasto actual:{} cantidadCanastos:{} valorVenta:{}",mapActual.getKey(), cantidadCanastos, valorCanastos);
						
						if(cantidadCanastos < cantidadMinima && valorCanastos < valorMinimo){
							LogSISPE.getLog().info("No cumple la condicion de cantidad o valor");
							cumpleCondicion = Boolean.FALSE;
							break;
						}
					}
				}
			}else{
				LogSISPE.getLog().info("se valida por entrega parcial");
				Long cantidadCanastos = 0L;
				Double valorCanastos = 0D;
				
				for(Entry<String, Map<String, Object>> mapActual : mapCanastoCantidades.entrySet()){
				
					cantidadCanastos = (Long)mapActual.getValue().get(keyCantidadCanastos);
					valorCanastos = (Double)mapActual.getValue().get(keyValorCanastos);
					
					if(mapActual.getKey().equals(codigoBarrasCanastoParcial)){
						
						LogSISPE.getLog().info("canasto validar:{} cantidadCanastos:{} valorVenta:{}",mapActual.getKey(), cantidadCanastos, valorCanastos);
						
						if(cantidadCanastos < cantidadMinima && valorCanastos < valorMinimo){
							LogSISPE.getLog().info("No cumple la condicion de cantidad o valor");
							cumpleCondicion = Boolean.FALSE;
							break;
						}
					}
				}
			}
		}
		LogSISPE.getLog().info("Los pedidos consolidados cumplen con la condicion para que el CD sea el responsable de elaborar los canastos {}",cumpleCondicion);
	}
	
	
	/**
	 * Obtiene el total de canastos que tienen todos los pedidos consolidados
	 * @param request
	 * @param codClaRecetasNuevas
	 * @return
	 * @throws Exception 
	 */
	public static int obtenerCantidadCanastosConsolidados(HttpServletRequest request, String codigoPedido) throws Exception{
		
		//se verifica que todos 
		HttpSession session = request.getSession();
		int cantidadCanastos = 0;
		
		//primero se valida si se trata de un pedido consolidado
		if(esPedidoConsolidado(request)){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoConsolidado){

					//se verifica que no sea el pedido actual
					if(!detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido) 
							&& CotizacionReservacionUtil.esCanasto(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion(), request)){
						cantidadCanastos += obtenerCantidadCanastosPorDetalleCD(detallePedidoDTO);
					}
				}
			}
		}
		LogSISPE.getLog().info(" En los otros pedidos consolidados existen: {} canastoss ",cantidadCanastos);
		return cantidadCanastos;
	}
	
	/**
	 * Obtiene el valor de los detalles en su equivalencia en canstos que tienen todos los pedidos consolidados
	 * @param request
	 * @param codClaRecetasNuevas
	 * @return
	 * @throws Exception 
	 */
	public static int obtenerEquivalenteEnCanastosConsolidados(HttpServletRequest request, String codigoPedido) throws Exception{
		
		//se verifica que todos 
		HttpSession session = request.getSession();
		int cantidadCanastos = 0;
		
		//primero se valida si se trata de un pedido consolidado
		if(esPedidoConsolidado(request)){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO:detallePedidoConsolidado){

					//se verifica que no sea el pedido actual
					if(!detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
						cantidadCanastos += UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue(), detallePedidoDTO.getArticuloDTO());
					}
				}
			}
		}
		LogSISPE.getLog().info(" En los otros pedidos consolidados existen: {} canastoss ",cantidadCanastos);
		return cantidadCanastos;
	}
	
	
	/**
	 * Obtiene el total de venta de los pedidos consolidados
	 * @param request
	 * @param configuracionTotalEntregas
	 * @return
	 * @throws Exception
	 */
	public static double obtenerTotalVentaConsolidados(HttpServletRequest request, String codigoPedido) throws Exception{
		
		//se verifica que todos 
		HttpSession session = request.getSession();
		double valorTotalVenta = 0;
		
		//primero se valida si se trata de un pedido consolidado
		if(esPedidoConsolidado(request)){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoConsolidado){
					
					//se verifica que no sea el pedido actual
					if(!detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
						valorTotalVenta += obtenerVentaPorDetalleCD(detallePedidoDTO);
					}
				}
			}
		}
		LogSISPE.getLog().info(" En los otros pedidos consolidados el total venta es: {}",valorTotalVenta);
		return valorTotalVenta;
	}
	
	
	/**
	 * Obtiene el numero de canastos configurados al CD
	 * @param detallePedidoDTO
	 * @return
	 */
	private static int obtenerCantidadCanastosPorDetalleCD(DetallePedidoDTO detallePedidoDTO){
		
		int cantidadCanastos = 0;
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			
			for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
				
				if(elaboraCanastoCD(entregaDetallePedidoDTO)){
					cantidadCanastos += entregaDetallePedidoDTO.getCantidadEntrega().intValue();
				}
			}
			
		}else{
			LogSISPE.getLog().info("el detalle {} no tiene entregas configuradas",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
			cantidadCanastos = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().intValue();
		}
		return cantidadCanastos;
	}

	
	/**
	 * Retorna el total de venta de las entregas que se solicitaron al CD
	 * @param detallePedidoDTO
	 * @return totalVenta de entregas configuradas al CD
	 * @throws SISPEException 
	 */
	private static double obtenerVentaPorDetalleCD(DetallePedidoDTO detallePedidoDTO) throws SISPEException{
		
		double valorVentaDetalle = 0;
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
			for(EntregaDetallePedidoDTO entregaDetallePedidoDTO : detallePedidoDTO.getEntregaDetallePedidoCol()){
				
				if(elaboraCanastoCD(entregaDetallePedidoDTO)){
					
					valorVentaDetalle = UtilesSISPE.valorEntregaArticuloBultos(detallePedidoDTO);
					
//					int bultosTotalEntrega = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().intValue();
//					//se obtiene los bultos a los que equivale la cantidad parcial articulos que se van a reservar.
//					int bultosParcialEntrega = entregaDetallePedidoDTO.getCantidadEntrega().intValue();
//					//Double valorUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta()/bultosTotalEntrega;
//					valorVentaDetalle += bultosParcialEntrega * detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta()/bultosTotalEntrega;
				}
			}
			
		}else{
			LogSISPE.getLog().info("el detalle {} no tiene entregas configuradas",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
			valorVentaDetalle = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorTotalVenta().doubleValue();
		}
		return valorVentaDetalle;
	}
	
	
	/**
	 * Verifica si la entregaDetallePedidoDTO es solicitada al CD el stock
	 * @param entregaDetallePedidoDTO
	 * @return true si se solicita al CD, false caso contrario
	 */
	private static boolean elaboraCanastoCD(EntregaDetallePedidoDTO entregaDetallePedidoDTO){
		
		boolean elaboraCanastoCD = Boolean.FALSE;
		if(entregaDetallePedidoDTO != null){
			
			if(entregaDetallePedidoDTO.getEntregaPedidoDTO() != null && entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp() != null 
					&& entregaDetallePedidoDTO.getEntregaPedidoDTO().getElaCanEsp().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
				
				elaboraCanastoCD = Boolean.TRUE;
			}
		}
		return elaboraCanastoCD;
	}
	
	
	/**
	 * Obtiene solo los detalles que la bodega se hara responsable de elaborar los canastos o los que aun no tienen
	 * configuradas entregas
	 * @param request
	 * @param codigoPedido
	 * @return
	 * @throws Exception
	 */
	public static Collection<DetallePedidoDTO> obtenerDetallePedidoElaboraCD(HttpServletRequest request, String codigoPedido) throws Exception{
		
		//se verifica que todos 
		HttpSession session = request.getSession();
		Collection<DetallePedidoDTO> detallePedidoDTOCol = new ArrayList<DetallePedidoDTO>();
		
		//primero se valida si se trata de un pedido consolidado
		if(esPedidoConsolidado(request)){
			
			List<DetallePedidoDTO> detallePedidoConsolidado = (ArrayList<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidado)){
				
				for(DetallePedidoDTO detallePedidoDTO : detallePedidoConsolidado){

					//se verifica que no sea el pedido actual
					if(!detallePedidoDTO.getId().getCodigoPedido().equals(codigoPedido)){
						
						if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
							
							Integer cantidadParcialCanastos = obtenerCantidadCanastosPorDetalleCD(detallePedidoDTO);
//							double cantidadVenta = obtenerVentaPorDetalleCD(detallePedidoDTO);
							
							//se verifica si la cantidad es distinta a la actual
							if(cantidadParcialCanastos != detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().intValue()){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(cantidadParcialCanastos.longValue());
//								detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(cantidadVenta);
							}
						}else{
							LogSISPE.getLog().info("el detalle {} no tiene entregas configuradas",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
						}
						detallePedidoDTOCol.add(detallePedidoDTO);
					}
				}
			}
		}
		return detallePedidoDTOCol;
	}
	
	
	/**
	 * Verifica si el pedido actual es un pedido consolidado
	 * @param request
	 * @return true si el pedido es consolidado, false caso contrario
	 */
	public static boolean esPedidoConsolidado(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null && (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
			
//			LogSISPE.getLog().info("El pedido actual es un pedido consolidado");
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
}