/* Creado el 27/03/2008
 * TODO
 */

package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_ESTABLECIMIENTO_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PERSONADTO_COL;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PREFIJO_VARIABLE_SESION;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.corpv2.web.util.CorpCommonWebConstantes;
import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.mensajeria.commons.util.WebMensajeriaUtil;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.SubClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.BuscarArticuloUtil;
import ec.com.smx.sic.sispe.common.util.ClienteUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CierreDiaPedidoEspecialDTO;
import ec.com.smx.sic.sispe.dto.ClientePedidoDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.integracion.sic.dto.IntGestionarPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.CrearPedidoForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleEstadoPedidoAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author  nperalta
 * @version 3.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class CrearPedidoAction extends BaseAction {
	/**
	 * <p>
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
	 * </p>
	 * 
	 * @param mapping 		El mapeo utilizado para seleccionar esta instancia
	 * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          				campos
	 * @param request 		La petici&oacue; que estamos procesando
	 * @param response 		La respuesta HTTP que se genera
	 * @return ActionForward	Los seguimiento de salida de las acciones
	 * @throws Exception
	 */

	//Variable que indica que la busqueda de articulos va a ser realizada desde pedidos especiales, se utiliza en listaArticulos.jsp para asignar el valor APE para la busqueda
	private static final String TIPOBUSQUEDA = "ec.com.smx.sic.sispe.busqueda.porPedidosEspeciales";
	//Coleccion donde se guarda el detalle del pedido
	public static final String DETALLE_PEDIDO = "ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol";
	//Coleccion que guarda la lista de articulos agregados en el detalle, se utiliza para comparar los articulos que se van a ir agregando con los ya existentes en el detalle
	public static final String COL_CODIGOS_ARTICULOS = "ec.com.smx.sic.sispe.pedido.codigosArticulos";
	//Guarda el articulo para controlar problemas de alcance y de stock
	private static final String ARTICULO = "ec.com.smx.sic.sispe.pedido.articuloDTO";
	//Variable que indica si se puede iniciar el ingreso de articulos al pedido: Si la entidad reponsable es el local
	//la variable es nula y se puede ubicar el cursor en la caja de texto del codigoArticulo, si es la bodega es no nula
	//y debe ser anulada al seleccionar un local para poder inicial el pedido
	private static final String PRIMERA_VEZ_BODEGA = "ec.com.smx.sic.sispe.pedido.primeraVez.bodega";
	//Si la sesion existe quiere decir que se puede acceder a las autorizaciones desde pantalla
	private static final String SECCION_AUTORIZACION = "ec.com.smx.sic.sispe.pedido.autorizacion.existe";
	//Sesion que guarda el local seleccionado
	public static final String LOCAL_ANTERIOR = "ec.com.smx.sic.sispe.pedido.localAnterior";
	//Sesion que indica que la entidad por donde se ingreso al pedido es la bodega
	public static final String ES_ENTIDAD_BODEGA = "ec.com.smx.sic.sispe.pedido.entidadBodega";
	//Sesiones que guardan el telefono y el administrador del local para cuando se hacen pedidos desde bodega
	public static final String TELEFONO_LOCAL = "ec.com.smx.sic.sispe.vistaLocalDTO.pedido.telefonoLocal";
	public static final String ADMINISTRADOR_LOCAL = "ec.com.smx.sic.sispe.vistaLocalDTO.pedido.administradorLocal";
	//Sesion que almacena la autorizacion
	public static final String AUTORIZACION = "ec.com.smx.sic.sispe.autorizacion";
	//Sesion que indica si se va a confirmar el pedido
	public static final String CONFIRMAR_PEDIDO = "ec.com.smx.sic.fechas.visible";
	//Sesion que indica si se va a mostrar el resumen del pedido
	public static final String RESUMEN_PEDIDO = "ec.com.smx.sic.resumen.visible";
	//Sesion que guarda la entidad responsable
	private static final String RESPONSABLE_RESERVACION = "ec.com.smx.sic.sispe.pedido.responsable";
	//Graba el pedido generado
	public static final String PEDIDO_GENERADO = "ec.com.smx.sic.sispe.pedido.pedidoDTO";
	//Guarda el directorio de salida del pdf
	private static final String DIRECTORIO_SALIDA_REPORTE = "ec.com.smx.sic.sispe.reporte.directorioSalida";
	//Indica si se va confirmar el pedido y este fue creado desde la bodega
	private static final String INCIO_ENTIDAD_BODEGA = "ec.com.smx.sic.sispe.pedido.incioEntidadBodega";
	//Sesion que indica si se va a confirmar el pedido desde la pantalla de busqueda entonces el check de confirmacion no debe salir
	//Si esta sesion es !=null tambien indica que el ingreso fue hecho desde la pantalla de busqueda
	//private static final String SIN_CONFIRMACION = "ec.com.smx.sic.sispe.pedido.sinConfirmacion";
	//Sirve para eliminar las variables de sesion al entrar a la accion de lista de pedidos especiales
	public static final String REGRESA_BUSQUEDA = "ec.com.smx.sic.sispe.pedido.regresaBusqueda";
	//si la variable existe quiere decir que ya genero el pdf y puede enviar el archivo adjunto en el mail
	//private static final String ARCHIVO_ADJUNTAR = "ec.com.smx.sic.sispe.mail.archivoParaAdjuntar";
	//almacena errores para enviar el mail
	public static final String ERRORES_ENVIO_MAIL = "ec.com.smx.sic.sispe.enviarMail.errors";
	//indica que se ingres\u00F3 al pedido desde una b\u00FAsqueda previa
	public static final String INGRESO_DESDE_BUSQUEDA = "ec.com.smx.sic.sispe.ingresoDesdeBusqueda";
	//guarda la fecha m\u00EDnima de despacho enviada por el SIC
	public static final String FECHA_MINIMA_DESPACHO = "ec.com.smx.sic.sispe.fechaMinimaDespacho";
	//guarda la fecha m\u00EDnima de entrega
	public static final String FECHA_MINIMA_ENTREGA = "ec.com.smx.sic.sispe.fechaMinimaEntrega";
	//guarda el objeto que contiene los datos devueltos por el SIC
	public static final String DATOS_FECHA_MINIMA_DESPACHO_DTO = "ec.com.smx.sic.sispe.datosfechaMinimaDespachoSIC";
	
	//guarda la descripci\u00F3n com\u00FAn usada el la b\u00FAsqueda de art\u00EDculos especiales de carnes
	public static final String DESC_COMUN_SUBCLA_FILTRO_CARNES = "ec.com.smx.sic.sispe.filtroComunBusquedaCarnes";
	//guarda la coleccion de especiales disponibles
	public static final String COL_TIPO_ESPECIAL="ec.com.smx.sic.sispe.pedidos.especiales";
	//codigos de clasificaciones para la b\u00FAsqueda
	public static final String LISTA_CLASIFICACIONES = "ec.com.smx.sic.sispe.pedidoEspecial.codClasificaciones";
	//cuando cambia el tipo de pedido
	private static final String CAMBIO_PEDIDO = "cambiarPedido";
	//indice del pedido seleccionado
	public static final String INDICE_ESPECIAL = "ec.com.smx.sic.sispe.pedidoEspecial.indice";
	//respaldo del indice de pedido seleccionado
	private static final String INDICE_ESP_RESPALDO="ec.com.smx.sic.sispe.pedidoEspecial.indiceRespaldo";
	//tipo de pedido especial actual
	private static final String TIPO_ESP_ACTUAL = "ec.com.smx.sic.sispe.pedidoEspecial.actual";
	//tipo de pedido especial anterior
	private static final String TIPO_ESP_ANTERIOR = "ec.com.smx.sic.sispe.pedidoEspecial.anterior"; 
	//bot\u00F3n 'no' del popup
	private static final String CANCELA_CAMBIO="cancelarCambio";
	public static final String CODIGO_ESP_CARNES = PREFIJO_VARIABLE_SESION.concat("codigoEspecialCarnes");
	//Variable que indica si se va a confirmar el pedido desde el popUp
	public static final String CONFIRMAR_PEDIDO_POPUP = "ec.com.smx.sic.fechas.popUp.visible";
	//cuando se acepta el registro de un pedido
	public static final String REG_PEDIDO = "regPedido";
	
	public static final String SOLICITAR_POPUP = "";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//Declaracion de variables
		HttpSession session=request.getSession();
		ActionErrors errores = new ActionErrors();
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages exitos = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		CrearPedidoForm formulario=(CrearPedidoForm)form;
		
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);
		String opTipoPedido= formulario.getOpTipoPedido();
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);	
		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		//Consulto la accion actual
		String accion=(String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);

		String forward="desplegar";

		try{
			//Si ya fue enviado el mail elimino la variable de sesion
			if(session.getAttribute("ec.com.smx.sic.sispe.envioMail")!=null){
				session.removeAttribute("ec.com.smx.sic.sispe.envioMail");
			}
			/***************************************************************************************************
			 *********************************** BODEGA ********************************************************
			 ***************************************************************************************************/
			/*----------------------- desde la bodega se selecciona un local --------------------------*/
			if (request.getParameter("cambiarTipoDocumentoContacto") != null) {				
				LogSISPE.getLog().info("Se ha cambiado el tipo de documento del contacto a: {}",formulario.getTipoDocumento());			
				//borrado de los campos del contacto
				formulario.setNombreContacto(null);
				formulario.setNumeroDocumentoContacto(null);
				formulario.setTelefonoContacto(null);
				formulario.setEmailContacto(null);
				formulario.setTipoDocumentoContacto(null);
				
				//borrado de los campos de la empresa
				formulario.setRucEmpresa(null);
				formulario.setNombreEmpresa(null);
				
				//borrado de los campos de la persona
				formulario.setNombrePersona(null);
				formulario.setNumeroDocumentoPersona(null);
				formulario.setTelefonoPersona(null);
				formulario.setEmailPersona(null);
				formulario.setTipoDocumentoPersona(null);
				
//				formulario.setEmailEnviarCotizacion(null);
				
				//borra de sesion los datos del contacto o empresa
				request.getSession().removeAttribute(ContactoUtil.PERSONA);
				request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
			}
			else if(request.getParameter("cambiarLocal")!=null)
			{
				LogSISPE.getLog().info("entro a seleccionar un local");
				if(formulario.getIndiceLocalResponsable().equals("") || formulario.getIndiceLocalResponsable().equals("ciudad")){
					//condici\u00F3n para manejar el error
					errors.add("localResponsable",new ActionMessage("errors.cambio.local"));
					LogSISPE.getLog().info("local anterior: {}",session.getAttribute(LOCAL_ANTERIOR));
					formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
				}else{
					LogSISPE.getLog().info("Se procede a ingresar por primera vez a la aplicaci\u00F3n...");
					//esta condici\u00F3n se cumple solamente cuando se ingresa por primera vez al pedido especial
					if(session.getAttribute(PRIMERA_VEZ_BODEGA)!=null){
						//se guarda toda la descripci\u00F3n
						session.setAttribute(LOCAL_ANTERIOR, formulario.getIndiceLocalResponsable());
						//se obtienen los datos del local
						VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
						//se obtiene el c\u00F3digo del local de la posici\u00F3n 0
						session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaLocalDTO.getId().getCodigoLocal());
						//se obtiene el c\u00F3digo del establecimiento de la posici\u00F3n 2
						session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaLocalDTO.getCodigoEstablecimiento());
						session.removeAttribute(PRIMERA_VEZ_BODEGA);
						
						formulario.setLocalDespacho(vistaLocalDTO.getId().getCodigoLocal().toString());
						
						//para controlar fechas de entrega y despacho dependiendo el cierre de d\u00EDa
						this.verificarCierreDiaYFechaMinimaDespacho(formulario, request, infos, errors, 
								vistaLocalDTO.getId().getCodigoLocal());
					}else{
						LogSISPE.getLog().info("pregunta si se desea cambiar el local");
						//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
						WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.cambiolocal", 
								"\u00BFDesea cambiar el local?", "siCambiarLocal", "noCambiarLocal", request);
					}
				}
			}
			
			/* cuando se consultan los datos del cliente al dar ENTER sobre la caja de texto del documento
			 * del cliente
			 */
			else if(request.getParameter("consultarCliente")!=null){
				LogSISPE.getLog().info("Se procede a consultar los datos del cliente, numDocumento: {}", formulario.getNumeroDocumento() );
				formulario.setTipoDocumento("");

				//se realiza la consulta de los datos del cliente
				if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())){
					forward = ContactoUtil.consultarCliente(formulario, request, session, errores, errors, accion, estadoActivo, estadoInactivo, response, infos, warnings, beanSession,forward);
				}else{
					ContactoUtil.borrarAtributosSessionCorp(request);
					session.removeAttribute(ContactoUtil.PERSONA);
					session.removeAttribute(ContactoUtil.LOCALIZACION);
					session.removeAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP);
					session.removeAttribute(ContactoUtil.LOC_GUARDADA);
					session.setAttribute(ContactoUtil.URL_OPCIONES, "jsf/contacto/opcionesBusqueda.jsf");
					session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminBusqueda.jsf");
					session.setAttribute(ContactoUtil.FLAG_BUSCAR_PERSONA_EMPRESA, "false");
					session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");
					ContactoUtil.mostrarPopUpCorporativo(request, session, "visualizarPersona", formulario,errors);
					ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
				}
			}
			/*
			 * Seleccionar persona-empresa del componente busqueda - cambios oscar
			 */
			else if(request.getParameter("personaEmpresaDesdeComBusqueda") != null) {
				
				forward = ContactoUtil.perEmpBusqueda(formulario, request, session, errors, response, infos,warnings, beanSession,forward);
			}
			/*-------------- cuando se cancela una confirmaci\u00F3n ---------------*/
			else if(request.getParameter("botonNo")!=null && request.getParameter("botonNo").equals("noCambiarLocal")){
				//se restablece el c\u00F3digo ingresado anteriormente, cuando se cancela el cambio de local desde bodega
				formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
			}
			/*------------ [NO ES DESDE UN LOCAL] cuando se acepta la confirmaci\u00F3n del cambio de local -------------*/
			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("siCambiarLocal")){
				//se respaldan las colecciones de datos que est\u00E1n en sesi\u00F3n antes de inicializar el formulario
				String indiceLocalResponsable = formulario.getIndiceLocalResponsable();

				//se inicializa el formulario
				this.inicializarPedido(formulario, request);
				//asigno el tipo de pedido seleccionado del local anterior
				formulario.setOpTipoPedido(opTipoPedido);
				formulario.setIndiceLocalResponsable(indiceLocalResponsable);
				//se guarda toda la descripci\u00F3n
				session.setAttribute(LOCAL_ANTERIOR, indiceLocalResponsable);
				//se obtienen los datos del local
				VistaLocalDTO vistaLocalDTO = WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
				//se obtiene el c\u00F3digo del local de la posici\u00F3n 0
				session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaLocalDTO.getId().getCodigoLocal());
				//se obtiene el c\u00F3digo del establecimiento de la posici\u00F3n 2
				session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaLocalDTO.getCodigoEstablecimiento());

				formulario.setLocalDespacho(vistaLocalDTO.getId().getCodigoLocal().toString());
				
				//se almacena el tel\u00E9fono del local responsable y el nombre del administrador
				//estos valores se usan cuando el pedido se realiza desde la bodega
				session.setAttribute(TELEFONO_LOCAL, vistaLocalDTO.getTelefonoLocal());
				session.setAttribute(ADMINISTRADOR_LOCAL, vistaLocalDTO.getAdministradorLocal());
				
				this.verificarCierreDiaYFechaMinimaDespacho(formulario, request, infos, errors, 
						vistaLocalDTO.getId().getCodigoLocal());
				
			}
			/***************************************************************************************************
			 ***************************************** PEDIDO **************************************************
			 ***************************************************************************************************/
			/*----------------------- check para confirmar pedido desde formulario--------------------------*/
			else if(request.getParameter("confirmaPedido")!=null){
				if(formulario.getOpConfirmarPedido()!=null){
					//Asigno una sesi\u00F3n para hacer visible las fechas
					session.setAttribute(CONFIRMAR_PEDIDO, "ok");
					LogSISPE.getLog().info("no muestra popUp");
				}else{
					//aparecer\u00E1 un popup de confirmaci\u00F3n cuando guarde el pedido 
					session.removeAttribute(CONFIRMAR_PEDIDO);
					LogSISPE.getLog().info("debe mostrar popUp");
				}
			}
			/*----------------------- check para confirmar pedido desde popUP--------------------------*/
			else if(request.getParameter("confirmaPedidopopUp")!=null){
				if(formulario.getOpConfirmarPedidoPopUp()!=null){
					//Asigno una sesi\u00F3n para hacer visible las fechas
					session.setAttribute(CONFIRMAR_PEDIDO_POPUP, "ok");
					LogSISPE.getLog().info("es diferente de null");

				}else{
					//aparecer\u00E1 un popup de confirmaci\u00F3n cuando guarde el pedido 
					session.removeAttribute(CONFIRMAR_PEDIDO_POPUP);
					session.removeAttribute(CONFIRMAR_PEDIDO);
					LogSISPE.getLog().info("es null");
				}
			}
			/*-----------------------Entra desde la pantalla de modificar pedido--------------*/
			else if(request.getParameter("indice")!=null)
			{
				//llamada al m\u00E9todo que construye la recotizaci\u00F3n en base a la vista del detalle
				WebSISPEUtil.construirDetallesPedidoEspecialDesdeVista(formulario, request);
				
				//se obtiene la colecci\u00F3n de los pedidos 
				ArrayList<VistaPedidoDTO> pedidos = (ArrayList<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				//creaci\u00F3n del DTO para almacenar el pedido selecionado
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(Integer.parseInt(request.getParameter("indice")));

				//llamada al m\u00E9todo que verifica el cierre de d\u00EDa
				this.verificarCierreDiaYFechaMinimaDespacho(formulario, request, infos, errors, 
						vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				
				//indico que la busqueda va a ser desde pedidos especiales
				session.setAttribute(TIPOBUSQUEDA, "ok");
				session.setAttribute(INGRESO_DESDE_BUSQUEDA, "ok");
				//Pregunto si se debe acceder a las autorizaciones
				if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.pedidoEspecial.estado").equals("0")){
					session.removeAttribute(SECCION_AUTORIZACION);
				}else{
					LogSISPE.getLog().info("asigna la variable");
					session.setAttribute(SECCION_AUTORIZACION, "ok");
				}
				
				//Indica que la accion actual es crearPedidoEspecial
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.crearPedidoEspecial"));	
				session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");

				//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Formulario de pedidos para carnes");

				//variable que sirve para saber que inicialmente se ingres\u00F3 el pedido desde la bodega
				//esta se utiliza en la jsp para mostrar el local responsable
				session.setAttribute(INCIO_ENTIDAD_BODEGA,"ok");
				
				ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());

				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedidoEspecial(request, formulario);
				beanSession.setPaginaTab(tabsCotizaciones);
				
				//ejecutar el metodo para inicializar el controlador adecuado
				ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
				
			}
			//----------------- cuando se desea volver a la secci\u00F3n de b\u00FAsqueda
			else if(request.getParameter("volverBuscar")!=null){
				LogSISPE.getLog().info("va a abrir ventana de confirmacion");
				//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
				WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.regresarABusqueda", 
						"\u00BFDesea volver a la pantalla de b\u00FAsqueda?", "siVolverBusqueda", null, request);
			}
			//--------- cuando se responde SI para volver a la pantalla de b\u00FAsqueda de cotizaciones y recotizaciones
			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("siVolverBusqueda")){
				LogSISPE.getLog().info("responde si volver a la busqueda");
				//se activa el men\u00FA principal
				MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
				session.setAttribute(REGRESA_BUSQUEDA, "ok");
				ContactoUtil.eliminarDataManagersSessionCorp(request);
				forward = "listaCotizadosRecotizados";
			}
			/*-------------- cuando se cancela una confirmaci\u00F3n ---------------*/
			else if(request.getParameter("botonNo")!=null && request.getParameter("botonNo").equals("noCambiarLocal")){
				LogSISPE.getLog().info("responde no volver a la busqueda");
				//se restablece el c\u00F3digo ingresado anteriormente, cuando se cancela el cambio de local desde bodega
				formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
			}

			/***************************************************************************************************
			 *********************************** DETALLE DEL PEDIDO ********************************************
			 ***************************************************************************************************/
			/*----------------------- si se desea agregar un art\u00EDculo de manera manual a la reserva --------------------------*/
			else if(request.getParameter("agregarArticulo")!=null)
			{
				//se obtiene de la sesion los datos del detalle y de la lista de articulos.
				ArrayList<DetallePedidoDTO> detallePedido = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos
				ArrayList<String> codigosArticulos = (ArrayList<String>)session.getAttribute(COL_CODIGOS_ARTICULOS);

				try{
					String codigoArticulo = formulario.getCodigoArticulo().trim();//devuelve el codigo sin espacios en blanco
					ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
					//consultaArticuloDTO.getId().setCodigoArticulo(codigoArticulo);
					consultaArticuloDTO.setNpCodigoBarras(codigoArticulo);

					//obtengo el indice del pedido escogido
					int indice = (Integer)session.getAttribute(INDICE_ESPECIAL);	
					LogSISPE.getLog().info("indiceBusquedaManual: {}",indice);
					//tomo de sesion la colecci\u00F3n de pedidos especiales
					List<EspecialDTO> especialDTOcol = (List<EspecialDTO>)session.getAttribute(COL_TIPO_ESPECIAL);
					EspecialDTO especialDTO = especialDTOcol.get(indice);
					consultaArticuloDTO.setNpCodigoEspecial(especialDTO.getId().getCodigoEspecial());
					//obtengo la clasificacion del pedido especial escogido
					/*WebSISPEUtil.obtenerClasificacionEspecial(request, especialDTOcol, indice);

					if(session.getAttribute(LISTA_CLASIFICACIONES)!= null){
						consultaArticuloDTO.setCodigoClasificacion((String)session.getAttribute(LISTA_CLASIFICACIONES));
					}*/
					
					//se obtiene el c\u00F3digo del par\u00E1metro para el c\u00F3digo de especiales
					String codigoEspecialCarnes = (String)session.getAttribute(CODIGO_ESP_CARNES);
					if(codigoEspecialCarnes == null){
						//parametro que indica cu\u00E1ntos d\u00EDas debe haber entre despacho y entrega
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialCarnes", request);
						codigoEspecialCarnes = parametroDTO.getValorParametro();
						session.setAttribute(CODIGO_ESP_CARNES, codigoEspecialCarnes);
					}

					LogSISPE.getLog().info("valor par\u00E1metro especial: {}",codigoEspecialCarnes);
					LogSISPE.getLog().info("codigo especial: {}", especialDTO.getId().getCodigoEspecial());
					//si busca un art\u00EDculo en carnes
					if(especialDTO.getId().getCodigoEspecial().equals(codigoEspecialCarnes))
					{
						ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
						clasificacionDTO.setCodigoClasificacionPadre(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDepartamentoCarnes"));
						//se crea el objeto subClasificaci\u00F3n para la b\u00FAsqueda
						SubClasificacionDTO subClasificacionDTO = new SubClasificacionDTO();
						subClasificacionDTO.setDescripcionSubClasificacion("");
						String descripcionComunSubCla = (String)session.getAttribute(DESC_COMUN_SUBCLA_FILTRO_CARNES);

						if(descripcionComunSubCla == null){
							//se obtiene el par\u00E1metro de la descripci\u00F3n com\u00FAn para las clasificaciones
							ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.descripcionFiltroSubclasificacionCarnes", request);
							if(parametroDTO.getValorParametro() != null){
								descripcionComunSubCla = parametroDTO.getValorParametro();
								session.setAttribute(DESC_COMUN_SUBCLA_FILTRO_CARNES, descripcionComunSubCla);
								subClasificacionDTO.setDescripcionSubClasificacion(descripcionComunSubCla);
							}
						}else{
							subClasificacionDTO.setDescripcionSubClasificacion(descripcionComunSubCla);
						}
						consultaArticuloDTO.setNpConsultarArticulosCarnes(estadoActivo);
						consultaArticuloDTO.setSubClasificacionDTO(subClasificacionDTO);
						consultaArticuloDTO.setClasificacionDTO(clasificacionDTO);
					}

					WebSISPEUtil.construirConsultaArticulos(request, consultaArticuloDTO, estadoInactivo, estadoInactivo,accion);

					if (request.getSession().getAttribute("sispe.pedido.pavos") != null) {
						consultaArticuloDTO.setNpEstadoStockArticulo("pedidoEspecialPavos");
					}
					
					//llamada al m\u00E9todo de la capa de servicio que devuelve un articulo de acuerdo a su codigo de barras
					ArticuloDTO articuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(consultaArticuloDTO);

					if(articuloDTO!=null) //solo si se obtuvo el art\u00EDculo
					{
						//SE CONFIRMA PRIMERO SI EL ARTICULO YA ESTA EN EL LISTADO
						if(codigosArticulos.contains(codigoArticulo))
						{
							//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
							errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetido",articuloDTO.getDescripcionArticulo()));
							LogSISPE.getLog().info("error: ya esta en la lista");
						}else{
							//Pregunta si el articulo no tiene alcance suficiente
							if(articuloDTO.getNpAlcance()!=null && articuloDTO.getNpAlcance().equals(estadoInactivo)){
								//se guarda el art\u00EDculo para el manejo de errores de stock o alcance
								request.setAttribute("ec.com.smx.sic.sispe.articuloDTO",articuloDTO);
							}
							//se guarda el art\u00EDculo para el manejo de errores de stock o alcance
							request.setAttribute(ARTICULO,articuloDTO);

							//llamada al m\u00E9todo que construye el detalle del pedido
							DetallePedidoDTO detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(new Long(formulario.getCantidadArticuloI().trim()),
									detallePedido.size(), articuloDTO, request,errors);
							if(detallePedidoDTO!=null){
								//se a\u00F1ade el detalle a la colecci\u00F3n
								detallePedido.add(detallePedidoDTO);
								LogSISPE.getLog().info("SE ANADIO A LA LISTA");
								//se actualiza la lista de codigos de art\u00EDculos
								codigosArticulos.add(codigoArticulo);
	
								//se blanquean los campos de ingreso
								formulario.setCodigoArticulo(null);
								formulario.setCantidadArticuloI(null);
							}else{
								errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo con codigo de barras: "+articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()+", es probable que existan problemas con la informaci\u00F3n registrada"));
							}
						}
					}else{
						infos.add("articulosVacio",new ActionMessage("message.codigoBarras.invalido"));
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					errors.add("agregarArticuloSIC", new ActionMessage("errors.agregarArticuloSIC"));
				}
			}
			/*-------------------- cuando se desea eliminar un registro del detalle del pedido ----------------------*/
			else if(request.getParameter("eliminarArticulos")!=null)
			{
				LogSISPE.getLog().info("por eliminar");
				//se obtienen los indices seleccionados
				String [] indicesRegistros = formulario.getChecksSeleccionar();
				//String todos = formulario.getCheckSeleccionarTodo();
				//obtengo la colecci\u00F3n de detalles de la sesion
				ArrayList<DetallePedidoDTO> detalle = (ArrayList<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
				//obtengo los codigos de los art\u00EDculos de sesi\u00F3n
				ArrayList<String> codigosArticulos = (ArrayList<String>)session.getAttribute(COL_CODIGOS_ARTICULOS);
				LogSISPE.getLog().info("detalles: {}", detalle.size());
				LogSISPE.getLog().info("articulos: {}" , codigosArticulos.size());
				if(indicesRegistros!=null && indicesRegistros.length > 0)
				{
					int indiceRegistro=0;
					LogSISPE.getLog().info("# registros: {}",indicesRegistros.length);
					//se recorre el arreglo de indices
					for(int i=0;i<indicesRegistros.length;i++){
						indiceRegistro = Integer.parseInt(indicesRegistros[i]);
						//Obtengo el detalle que voy a eliminar
						DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)detalle.get(indiceRegistro);
						LogSISPE.getLog().info("ARTICULO: {}",detallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
					}

					//se elimina el primer registro
					indiceRegistro = Integer.parseInt(indicesRegistros[0]);
					detalle.remove(indiceRegistro);
					codigosArticulos.remove(indiceRegistro);
					int contElimnados=1;
					//se eliminan los siguientes registros
					for(int i=1;i<indicesRegistros.length;i++){
						indiceRegistro = Integer.parseInt(indicesRegistros[i]);
						indiceRegistro = indiceRegistro - contElimnados;
						detalle.remove(indiceRegistro);
						codigosArticulos.remove(indiceRegistro);
						//se eliminan los registros seleccionados
						contElimnados++;
					}
					formulario.setCheckSeleccionarTodo(null);
					formulario.setChecksSeleccionar(null);
				}else{
					infos.add("no ha seleccionado art\u00EDculos",new ActionMessage("info.eliminarArticulosEspeciales"));
				}
					
			}
			/*------------------------------ cuando se actualiza la lista de detalles ----------------------------------*/
			else if(request.getParameter("actualizarDetalle")!=null)
			{
				LogSISPE.getLog().info("entra a actualizar el detalle");
				//si se agregaron art\u00EDculos por la b\u00FAsqueda no se realiza la consulta para el alcance
				if(session.getAttribute(BuscarArticuloAction.BUSQUEDA_PED_ESPECIALES) == null){
					//se obtiene de la sesion los datos del detalle y de la lista de articulos.
					Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);
					if(detallePedido!=null && !detallePedido.isEmpty()){
						//verificaci\u00F3n para la actualizaci\u00F3n del stock y alcance
						ArrayList <ArticuloDTO> articulos = new ArrayList <ArticuloDTO>();
						for(DetallePedidoDTO detallePedidoDTO: detallePedido){
							ArticuloDTO articuloDTO = detallePedidoDTO.getArticuloDTO();
							articulos.add(articuloDTO);
						}
						LogSISPE.getLog().info("actualizando alcance ...");
						
						try{
							//llamada al m\u00E9todo que obtiene alcance de los art\u00EDculos (el valor se actualiza por referencia)
							SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);
						}catch(SISPEException ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
						}
					}
				}else{
					session.removeAttribute(BuscarArticuloAction.BUSQUEDA_PED_ESPECIALES);
					session.removeAttribute("ec.com.smx.sic.sispe.accion.buscar.articulos");
					session.removeAttribute("ec.com.smx.sic.sispe.catalogoArticulos.articulos");
				}

				//se actualizan los datos del formulario
				formulario.setChecksSeleccionar(null);
				formulario.setCheckSeleccionarTodo(null);
			}
			/***************************************************************************************************
			 *********************************** AUTORIZACIONES ************************************************
			 ***************************************************************************************************/
			/*------------------------------------------Autorizaciones--------------------------------------------*/
			else if(request.getParameter("aplicarAutorizacion")!=null){
				LogSISPE.getLog().info("entra a generar la autorizacion");
				//TODO: Se verific\u00F3  para que se usa esta autorizacion y no hay uso en la actualidad
				errors.add("errorAutorizacion",new ActionMessage("errors.SISPEException","Error al aplicar la autorizaci\u00F3n, autorizaci\u00F3n no encontrada"));
//				//se llama al m\u00E9todo que comprueba si se puede aplicar una autorizaci\u00F3n
//				AutorizacionesUtil.verificarAutorizacion(request, response, messages, errors, accion, formulario.getOpAutorizacion(), 
//						formulario.getNumeroAutorizacion(), formulario.getObservacionAutorizacion(), 
//						formulario.getLoginAutorizacion(), formulario.getPasswordAutorizacion());
			}
			//-------------- popUp de confirmaci\u00F3n de fecha de despacho-------------------
			else if(request.getParameter("grabarPedido")!=null)	{
				LogSISPE.getLog().info("abre el popUP");
				//if(ContactoUtil.obtenerDatosPersonaEmpresa(request, formulario, errors, beanSession)){
					try{
						//inicializo los campos
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
						formulario.setFechaDespachoPopUp(simpleDateFormat.format(session.getAttribute(FECHA_MINIMA_DESPACHO)));
						formulario.setFechaEntregaPopUp(simpleDateFormat.format(session.getAttribute(FECHA_MINIMA_ENTREGA)));
						formulario.setOpConfirmarPedidoPopUp(null);
						//se crea la ventana popUp
						UtilPopUp popUp = new UtilPopUp();
						popUp.setTituloVentana("Confirmar fecha de despacho");
						popUp.setMensajeVentana("No ha sido confirmada la fecha de despacho del pedido. "+
						"Si desea confirmarla habilite la siguiente opci\u00F3n. Para guardar el pedido haga clic en Aceptar.");
						popUp.setContenidoVentana("/pedidosEspeciales/creacion/chkConfirmarFechas.jsp");
						popUp.setFormaBotones(UtilPopUp.OK);
						popUp.setValorOK(REG_PEDIDO);
						popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
						popUp.setEtiquetaBotonOK("Aceptar");
						
						LogSISPE.getLog().info("confirmado: {}" ,formulario.getOpConfirmarPedido());
						LogSISPE.getLog().info("confirmado pop up: {}" , formulario.getOpConfirmarPedidoPopUp());
						//se guarda en sesi\u00F3n
						request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
						request.getSession().setAttribute(SOLICITAR_POPUP, "ok");
						
//						PaginaTab tabsResumenPedidoEspecial = ContactoUtil.construirTabsResumenPedidoEspecial(request,formulario);
//						beanSession.setPaginaTab(tabsResumenPedidoEspecial);
						
					}
					catch (Exception e) {
						errors.add("errorFechas",new ActionMessage("errors.SISPEException", MessagesWebSISPE.getString("errors.obtenerFechaMinimaDespachoSIC")));
				}
//			}
//			else{
//					forward="desplegar";
//			}
		}
			/***************************************************************************************************
			 *********************************** GRABAR EL PEDIDO **********************************************
			 ***************************************************************************************************/
			//Va a grabar el pedido, se valida que la sesion sea nula porque al presionar F5 en la pantalla de resumen vuelve a grabar el pedido
			else if(peticion!=null && peticion.equals(REG_PEDIDO)&& session.getAttribute(RESUMEN_PEDIDO)==null){
				LogSISPE.getLog().info("entra a grabar el pedido");

				//se obtiene el detalle del Pedido
				Collection <DetallePedidoDTO> colDetallePedidoDTO = (Collection <DetallePedidoDTO>)session.getAttribute(DETALLE_PEDIDO);

				//se llama al m\u00E9todo que construye el PedidoDTO
				PedidoDTO pedidoDTO = crearPedidoDTO(formulario, request);
				LogSISPE.getLog().info("CODIGO COMPANIA: {}",pedidoDTO.getId().getCodigoCompania());
				try
				{
					PedidoDTO pedidoActualDTO=new PedidoDTO();
					//Si se va a confirmar un pedido ya existente
					if(session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO)!=null){
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
						pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());

					}
					//Si se va a confirmar el pedido desde el formulario			
					if(formulario.getOpConfirmarPedido()==null&&formulario.getOpConfirmarPedidoPopUp()==null){
						
						LogSISPE.getLog().info("graba pedido");
						request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
						if(session.getAttribute(SOLICITAR_POPUP).equals("ok")){
							pedidoDTO.setNpEstadoPedido(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ESPECIAL_SOLICITADO);
							request.getSession().setAttribute(SOLICITAR_POPUP, "");
						}else{
							pedidoDTO.setNpEstadoPedido(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ESPECIAL_CONFIRMADO);
						}
						//se registra el pedido en la base de datos y se retorna el n\u00FAmero de la Cotizaci\u00F3n
						pedidoActualDTO=SessionManagerSISPE.getServicioClienteServicio().transRegistrarSolicitudPedidoEspecial(pedidoDTO, colDetallePedidoDTO);
						session.removeAttribute(CONFIRMAR_PEDIDO);
					}
					//si se va a confirmar desde el popup
					else if(formulario.getOpConfirmarPedidoPopUp()!=null || formulario.getOpConfirmarPedido()!=null){
						LogSISPE.getLog().info("graba pedido con confirmacion");
						
						if((formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)==null){
							if(formulario.getTipoDocumentoContacto()==null){
								ContactoUtil.ejecutarAccionControlador(request, response, "#{empresaController.editarLocalizacion()}");
								session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminLocalizacion.jsf");
								ContactoUtil.mostrarPopUpCorporativo(request, session, "editarEmpresa", formulario,errors);
								return mapping.findForward(forward);
							}
						}
						
						//se registra la confirmacion del pedido en la base de datos y se retorna el n\u00FAmero de la Cotizaci\u00F3n
						pedidoDTO.setNpEstadoPedido(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ESPECIAL_CONFIRMADO);
						pedidoActualDTO=SessionManagerSISPE.getServicioClienteServicio().transRegistrarConfirmacionPedidoEspecial(pedidoDTO, colDetallePedidoDTO);						
					}

					session.setAttribute(PEDIDO_GENERADO, pedidoActualDTO);
					
					if(session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO)==null){
					//se crea el vistaPedido
					VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
						//se construye el objeto VistaPedidoDTO para realizar la consulta
						consultaVistaPedidoDTO = new VistaPedidoDTO();
						consultaVistaPedidoDTO.getId().setCodigoCompania(pedidoActualDTO.getId().getCodigoCompania());
						consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(pedidoActualDTO.getId().getCodigoAreaTrabajo());
						consultaVistaPedidoDTO.getId().setCodigoPedido(pedidoActualDTO.getId().getCodigoPedido());
						consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		
						//primero se obtienen los datos del pedido
						Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
						if(CollectionUtils.isNotEmpty(colVistaPedidoDTO)){
							VistaPedidoDTO vistaPedidoDTO= colVistaPedidoDTO.iterator().next();
							Collection detallePedido = vistaPedidoDTO.getVistaDetallesPedidosReporte();
							if(detallePedido==null){
								LogSISPE.getLog().info("trae el detalle del pedido");
								//creaci\u00F3n del objeto VistaDetallePedidoDTO para la consulta
								VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
								consultaVistaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
								//LogSISPE.getLog().info("CODIGO DEL LOCAL: "+ vistaPedidoDTO.getId().getCodigoLocal());

								consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
								//consultaVistaDetallePedidoDTO.setCodigoTipoArticulo(vistaPedidoDTO.getCodigoTipoPedido());
								consultaVistaDetallePedidoDTO.getObservacionAutorizacionOrdenCompra();

								//asignaci\u00F3n de los par\u00E1metros de consulta
								consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
								consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
								consultaVistaDetallePedidoDTO.getEstadoDetallePedido();
								consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
								consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
								//busqueda de los detalles
								detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
								vistaPedidoDTO.setVistaDetallesPedidosReporte(detallePedido);

							}
							
							session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
							session.setAttribute(EstadoPedidoUtil.VISTA_PEDIDO, vistaPedidoDTO);
						}
					}else{
						if(session.getAttribute(EstadoPedidoUtil.VISTA_PEDIDO)==null){
							session.setAttribute(EstadoPedidoUtil.VISTA_PEDIDO, session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO));
						}
					}
					session.setAttribute(RESPONSABLE_RESERVACION,	pedidoDTO.getNpResponsablePedido());
					//Asigna la variable para el cambio de botones para el resumen del pedido
					session.setAttribute(RESUMEN_PEDIDO,"ok");
					//mensaje de exito para el registro autom\u00E1tico
					messages.add("validezCotizacion",new ActionMessage("message.exito.registro", "El Pedido"));
					//pantalla de resumen      	
					forward="resumen";
					
					DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
					
					ContactoUtil.mostrarDatosVisualizarPersonaEmpresa(formulario, request, response, infos, errors, warnings, new ActionErrors());
					
					if(warnings!=null && warnings.size()==1 && warnings.get().next().toString().startsWith("error.contactos.localizacion.sinContacto")){
						warnings.clear();
					}
					//Se carga el tab para resumen de pedidos especiales
					PaginaTab tabsResumenPedidoEspecial = ContactoUtil.construirTabsResumenPedidoEspecial(request,formulario);
					beanSession.setPaginaTab(tabsResumenPedidoEspecial);
					
					//ejecutar el metodo para inicializar el controlador adecuado
					ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
					
				}catch(SISPEException e){
					
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
					errors.add("guardarConfirmacion",new ActionMessage("error.grabar.registro"));
					errors.add("errorFechas",new ActionMessage("errors.SISPEException", e.getMessage()));
					//ESTE SEGMENTO SE REALIZA EN CASO DE QUE EL D\u00EDA FUERA CERRADO MIENTRAS EL USUARIO REALIZABA EL PEDIDO
					//EN ESTE CASO SE DEBE ACTUALIZAR LA FECHA M\u00EDNIMAS DE DESPACHO Y ENTREGA

					//se realiza la consulta para determinar si ya se cerr\u00F3 el d\u00EDa
					CierreDiaPedidoEspecialDTO cierreDiaPedidoEspecialDTO = new CierreDiaPedidoEspecialDTO();

					cierreDiaPedidoEspecialDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerActualCierreDiaPedidoEspecial(formulario.getOpTipoPedido());
					//si ya se cerr\u00F3 el d\u00EDa desde la bodega
					if(cierreDiaPedidoEspecialDTO!=null){
						errors.add("errorFechas",new ActionMessage("errors.SISPEException", e.getMessage()));
						
						//se obtiene los datos del objeto que contiene los datos devueltos por el SIC
						IntGestionarPedidoDTO intGestionarPedidoDTO = (IntGestionarPedidoDTO)session.getAttribute(DATOS_FECHA_MINIMA_DESPACHO_DTO);

//						Timestamp nuevaFechaMinDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getSigFechaMinimaDespacho(), 0, 0, 0, 0, 0, 0));
//						Timestamp nuevaFechaMinEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getSigFechaMinimaDespacho(), 0, 1, 0, 0, 0, 0));

						Timestamp nuevaFechaMinDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getSigfechaMinDespacho(), 0, 0, 0, 0, 0, 0));
						Timestamp nuevaFechaMinEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getSigfechaMinDespacho(), 0, 1, 0, 0, 0, 0));
						
						//se actualizan las fechas en sesi\u00F3n
						session.setAttribute(FECHA_MINIMA_DESPACHO, nuevaFechaMinDespacho);
						session.setAttribute(FECHA_MINIMA_ENTREGA, nuevaFechaMinEntrega);
						
						//se formatean las fechas y se asignan a los campos del formulario
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
						formulario.setFechaDespacho(simpleDateFormat.format(nuevaFechaMinDespacho));
						formulario.setFechaDespachoPopUp(simpleDateFormat.format(nuevaFechaMinDespacho));
						formulario.setFechaEntrega(simpleDateFormat.format(nuevaFechaMinEntrega));
						formulario.setFechaEntregaPopUp(simpleDateFormat.format(nuevaFechaMinEntrega));
					}
				}
			}

			/***************************************************************************************************
			 *********************************** RESUMEN DEL PEDIDO **********************************************
			 ***************************************************************************************************/

			/*-------- cuando se desea imprimir el pedido como texto ------------*/
			else if(request.getParameter("confirmarImpresionTexto")!=null){
				LogSISPE.getLog().info("va a imprimir el pedido");
				//variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
				request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
				//pantalla de resumen      	
				forward="resumen";
			}
			/*-------- cuando se desea generar el pedido en formato PDF ------------*/
			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("confirmarPDF")){
				LogSISPE.getLog().info("va a generar PDF");
				//se crea el nombre del archivo
				String nombreArchivo = "pedidoEspecial";
				//se llama al m\u00E9todo que forma el nombre completo del archivo
				String nombreCompletoArchivo = WebSISPEUtil.generarNombreArchivo(nombreArchivo, "pdf");
				//se guarda el nombre del archivo
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", nombreCompletoArchivo);
				//llamada al m\u00E9todo que crea el directorio en el servidor, donde se almacena el archivo
				//que luego ser\u00E1 enviado por mail
				String directorioSalida = WebMensajeriaUtil.crearCarpeta("SISPE") + nombreCompletoArchivo;
				//se guarda el directorio de salida
				session.setAttribute(DIRECTORIO_SALIDA_REPORTE, directorioSalida);
				//session.setAttribute(ARCHIVO_ADJUNTAR,"ok");
				LogSISPE.getLog().info("ANTES DE GENERAR EL ARCHIVO PDF");

				//se llama a la funci\u00F3n que inicializa los par\u00E1metros de impresi\u00F3n
				WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, false);

				//salida a la p\u00E1gina del PDF	
				forward="reportePDF";
			}
//			//------------- cuando ya se ha formado el archivo PDF, se env\u00EDa el mail -------------------
//			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("enviarMail"))
//			{
//				//if(session.getAttribute(ARCHIVO_ADJUNTAR)!=null){
//				ActionMessage mensaje = (ActionMessage)request.getAttribute(ERRORES_ENVIO_MAIL);
//				if(mensaje!=null){
//					errors.add("errorMail",mensaje);
//				}else{
//					//envio automatico del mailas
//					LogSISPE.getLog().info("empieza el envio");
//					try{
//						//Parametros necesarios para buscar la plantilla, deben ser ingresados por cada sistema
//						EventoID eventoID=new EventoID();
//						eventoID.setCodigoEvento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.envioPedido"));
//						eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
//						eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//						EventoDTO eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);
//						MailMensajeEST mailMensajeEST=new MailMensajeEST();
//						mailMensajeEST.setDe(eventoDTO.getEmailRemitente());//MessagesWebSISPE.getString("mail.cuenta.sispe"));
//						mailMensajeEST.setPara(formulario.getEmailEnviarCotizacion().split(","));
//						String nombreContacto=null;
//						if(formulario.getNombreContacto()!=null){
//							nombreContacto=formulario.getNombreContacto();
//						}else{
//							nombreContacto=formulario.getNombrePersona();
//						}
//						String descripcionEvento = eventoDTO.getDescripcionEvento().replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.nombreContacto"), nombreContacto);
//						//se determina el tipo de pedido
//						String documento = "Pedido";
//						
//						descripcionEvento = descripcionEvento.replaceAll(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.parametro.nombreDocumento"), documento);
//						mailMensajeEST.setAsunto(eventoDTO.getAsuntoEvento());
//						mailMensajeEST.setMensaje(descripcionEvento);
//						mailMensajeEST.setEventoDTO(eventoDTO);
//						//mailMensajeEST.setArchvivosAduntosTag(directorioPedido);
//						//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
//						mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
//						mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
//						mailMensajeEST.setFormatoHTML(true);
//						session.setAttribute("ec.com.smx.sic.sispe.envioMail", mailMensajeEST);//variable de sesion donde se cargan los datos del mail para enviarlo desde la jsp
//						//metodo para enviar el mail
//						//SessionManagerSISPE.getMensajeria(request).transEnvioMail(mailMensajeEST, 
//						//		MensajeriaMessages.getString("ec.com.smx.mensajeria.DIRECTORIO_ARCHIVOS") + "SISPE", "pdf");
//						messages.add("envioMail",new ActionMessage("message.mail.send.exito"));
//					}catch(Exception ex){
//						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
//						errors.add("envioMail",new ActionMessage("errors.mail.send.error",ex.getMessage()));
//					}
//				}
//				/*}else{
//					infos.add("envioMail",new ActionMessage("info.envioMail.adjuntarArchivo"));
//				}*/
//				forward="resumen";
//			}
			//si quiere cambiar el tipo de pedido
			else if(request.getParameter("indicePedido")!=null)
			{
				if(!formulario.getOpTipoPedido().equals(session.getAttribute(TIPO_ESP_ACTUAL))){
					int indice= Integer.parseInt(request.getParameter("indicePedido"));
					LogSISPE.getLog().info("indice: "+indice);
					session.setAttribute(INDICE_ESPECIAL, indice);

					//se crea la ventana popUp
					UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Cambio de tipo de pedido");
					popUp.setMensajeVentana("Si cambia la opci\u00F3n de tipo de pedido los datos ingresados se perder\u00E1n.");
					popUp.setPreguntaVentana("\u00BFEst\u00E1 seguro de que desea cambiar el tipo de pedido?");
					popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
					popUp.setValorOK(CAMBIO_PEDIDO);
					popUp.setAccionEnvioCerrar("realizarEnvio('"+CANCELA_CAMBIO+"');");
					popUp.setEtiquetaBotonOK("S\u00ED");
					popUp.setEtiquetaBotonCANCEL("No");
					popUp.setValorCANCEL("realizarEnvio('"+CANCELA_CAMBIO+"');");
					//se guarda
					request.setAttribute(SessionManagerSISPE.POPUP, popUp);

				}
			}
			// IMPORTANTE : C\u00F3digo de acceso a ventana emergente para b\u00FAsqueda de personas
			// ---------------------------------------------------------------------------------------------------------
			//	--------- cuando se desea buscar una persona para agregarla al pedido---------
			else if(request.getParameter("verPopUpClientes") != null){
				LogSISPE.getLog().info("Se procede a mostrar ventana emergente en pedidos especiales");
				UtilPopUp popUp = ClienteUtil.crearPopUpBusquedaClientes();
				popUp.setValorOK("requestAjax('crearPedidoEspecial.do',['pregunta','contextoPedido','datosCliente'],{parameters: 'aceptarPersona=ok',evalScripts:true});ocultarModal();");
				popUp.setValorCANCEL("requestAjax('crearPedidoEspecial.do',['pregunta'],{parameters: 'cancelarPopUpClientes=ok'});ocultarModal();");
				popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
				
				session.setAttribute(SessionManagerSISPE.POPUP, popUp);
			}
			//------- busqueda de personas ---------
			else if(request.getParameter("buscarPersonaEmpresa") != null){
				if (StringUtils.isNotEmpty(formulario.getNumeroDocumento())) {
					ContactoUtil.buscarPersonaEmpresa(formulario, request, session, response,errors);
				}else{
					ContactoUtil.borrarAtributosSessionCorp(request);
					session.removeAttribute(ContactoUtil.PERSONA);
					session.removeAttribute(ContactoUtil.LOCALIZACION);
					session.removeAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP);
					session.removeAttribute(ContactoUtil.LOC_GUARDADA);
					session.setAttribute(ContactoUtil.URL_OPCIONES, "jsf/contacto/opcionesBusqueda.jsf");
					session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminBusqueda.jsf");
					session.setAttribute(ContactoUtil.FLAG_BUSCAR_PERSONA_EMPRESA, "false");
					session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");
					ContactoUtil.mostrarPopUpCorporativo(request, session, "visualizarPersona", formulario,errors);
					ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
				}
			}
			// -------cargar persona elegida---------
			else if(request.getParameter("aceptarPersona") != null){
				LogSISPE.getLog().info("Se procede a llamar a m\u00E9todo aceptarPersona en Pedidos...");
				ClienteUtil.asignarPersonaSeleccionada(request, formulario);
			}
			//------- busqueda de personas cancelada --------
			else if(request.getParameter("cancelarPopUpClientes")!=null){
				LogSISPE.getLog().info("Se cancela el proceso en CrearPedidoAction...");
				session.removeAttribute(SessionManagerSISPE.POPUP);
				session.removeAttribute(PERSONADTO_COL);
				
				//IMPORTANTE
				// Se anulan los valores de paginaci\u00F3n cada vez que se cancela el proceso de b\u00FAsqueda
				session.removeAttribute(ClienteUtil.VAR_START_PAG);
				session.removeAttribute(ClienteUtil.VAR_RANGE_PAG);
				session.removeAttribute(ClienteUtil.VAR_SIZE_PAG);
			}
			// Paginaci\u00F3n de listado principal
			else if(request.getParameter("range")!= null && request.getParameter("start")!= null){				
				LogSISPE.getLog().info("Se procede a paginar el listado original en Pedidos...");					
				ClienteUtil.paginarListadoPersonas(request, request.getParameter("start"));												
			}
			// --------------------------------------------------------------------------------------------------------------------
			//si no quiere cambiar de pedido
			else if(peticion!=null && peticion.equals(CANCELA_CAMBIO)){
				//asigno la variable anterior del radio button
				int indiceAnterior= ((Integer)session.getAttribute(INDICE_ESP_RESPALDO));
				formulario.setOpTipoPedido((String)(session.getAttribute(TIPO_ESP_ANTERIOR)));
				LogSISPE.getLog().info("anterior: {}",(String)(session.getAttribute(TIPO_ESP_ANTERIOR)));
				session.setAttribute(INDICE_ESPECIAL, indiceAnterior);
				LogSISPE.getLog().info("indice anterior: {}",indiceAnterior);

			}
			//si cambia de tipo de pedido
			else if(peticion != null && peticion.equals(CAMBIO_PEDIDO)){
				LogSISPE.getLog().info("entra a cambiar pedido");

				//tipo de pedido actual
				session.setAttribute(TIPO_ESP_ACTUAL, formulario.getOpTipoPedido());
				//obtengo el \u00EDndice del pedido
				int indice = (Integer)session.getAttribute(INDICE_ESPECIAL);
				LogSISPE.getLog().info("indiceAceptar: {}", indice);
				//tomo de sesion la colecci\u00F3n de pedidos especiales
				ArrayList<EspecialDTO> especialDTOcol = (ArrayList<EspecialDTO>)session.getAttribute(COL_TIPO_ESPECIAL);

				WebSISPEUtil.obtenerClasificacionEspecial(request, especialDTOcol, indice);

				this.verificarCierreDiaYFechaMinimaDespacho(formulario, request, infos, errors, 
						(Integer)session.getAttribute(CODIGO_LOCAL_REFERENCIA));
				
				//se inicializa el formulario
				this.inicializarPedido(formulario, request);
				formulario.setTextoBusqueda(null);
				formulario.setCodigoArticulo(null);
				formulario.setIndiceLocalResponsable((String)session.getAttribute(LOCAL_ANTERIOR));
				LogSISPE.getLog().info("local anterior: {}"+session.getAttribute(LOCAL_ANTERIOR));
				//asigno la nueva variable en caso de que quiera cambiar posteriormente de tipo de pedido
				session.setAttribute(TIPO_ESP_ANTERIOR, formulario.getOpTipoPedido());
				session.setAttribute(INDICE_ESP_RESPALDO, indice);
				//se borra de sesion los datos del contacto y/o de localizacion
				session.removeAttribute(ContactoUtil.PERSONA);
				session.removeAttribute(ContactoUtil.LOCALIZACION);
				session.removeAttribute(ContactoUtil.REPORTE);				
				//ContactoUtil.cambiarTabPersonas(beanSession, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
				beanSession.getPaginaTab().getTab(0).setTitulo(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
			}
								
			/**
			 * MUESTRA EL POPUP PARA EDITAR PERSONA
			 */
		    //else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("editarPersona")){
			 else if(request.getParameter("editarPersona")!=null && request.getParameter("editarPersona").equals("ok")){
				 forward = ContactoUtil.editarPersona(formulario, request, session, response, errors);
			}
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR PERSONA
			 */
		     else if(request.getParameter("registrarPersona")!=null && request.getParameter("registrarPersona").equals("ok")){
				LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR PERSONA del SISTEMA CORPORATIVO");
				forward = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarPersona", formulario,errors);
		    }
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR LOCALIZACION
			 */
		    else if(request.getParameter("registrarLocalizacion")!=null && request.getParameter("registrarLocalizacion").equals("ok")){
		    	LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR LOCALIZACION del SISTEMA CORPORATIVO");
		    	forward = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarLocalizacion", formulario,errors);
			}
			/**
			 * MUESTRA EL POPUP PARA EDITAR EMPRESA
			 */
		    else if(request.getParameter("editarEmpresa")!=null && request.getParameter("editarEmpresa").equals("ok")){
		    	forward = ContactoUtil.editarEmpresa(response, request, session, forward, formulario,errors);
			}
			/**
			 * MUESTRA EL POPUP PARA REGISTRAR EMPRESA
			 */
		    else if(request.getParameter("registrarEmpresa")!=null && request.getParameter("registrarEmpresa").equals("ok")){
		    	LogSISPE.getLog().info("MUESTRA EL POPUP PARA REGISTRAR EMPRESA del SISTEMA CORPORATIVO");
		    	forward = ContactoUtil.mostrarPopUpCorporativo(request, session, "registrarEmpresa", formulario,errors);
			}
			
			/**
			 * CIERRA EL POPUP PARA LA INTREGRACION CON JSF
			 */
		    else if(request.getParameter("cerrarPopUpCorporativo") != null){
		    	
				String valorCerrarPopUp = request.getParameter("cerrarPopUpCorporativo");
		    	LogSISPE.getLog().info("cerrarPopUpCorporativo: {}",valorCerrarPopUp);
				request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
				request.getSession().removeAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS);
				//mensajes al cerrar el popUp de la integracion con el corporativo
				if(!formulario.getNumeroDocumento().isEmpty()){
					if(valorCerrarPopUp.equals("guardar")){
						infos.add("", new ActionMessage("message.integracion.corp.creacion.cliente"));
						
					}else if(valorCerrarPopUp.equals("cancelarCreacion")){
						//cancelacion creacion persona 
						infos.add("", new ActionMessage("message.integracion.corp.cacelacion.creacion"));
						
					}else if(valorCerrarPopUp.equals("modificar")){
						//modificacion persona exitosa 
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.cliente"));
						if(ContactoUtil.isRucPersona()){
							ContactoUtil.setRucPersona(false);
						}
						if(request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null){
							if (request.getSession().getAttribute(ContactoUtil.RUC_PERSONA).equals("ok")){
								ContactoUtil.limpiarContacto(formulario, session);
							}else{
								ContactoUtil.setearValoresPersonaEncontradaEnFormulario(request, formulario, (PersonaDTO) request.getSession().getAttribute(ContactoUtil.PERSONA));
							}
						}
					}else if(valorCerrarPopUp.equals("cancelarModificacion")){
						//cancelacion modificacion persona
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.modificacion"));
						if(ContactoUtil.isRucPersona()){
							ContactoUtil.limpiarContacto(formulario, session);
							ContactoUtil.setRucPersona(false);
						}
						
					}else if(valorCerrarPopUp.equals("registrarEmpresa")){
						//Creacion empresa con exito
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Empresa",""));
						
					}else if(valorCerrarPopUp.equals("editarEmpresa")){
						//modificacion empresa exitosa 
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.empresa.localizacion","Empresa"));
						
					}else if(valorCerrarPopUp.equals("cancelarEdicionEmpresa")){
						//cancelar edicion de empresa 
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Empresa",""));
						
					}else if(valorCerrarPopUp.equals("registrarLocalizacion")){
						//la localizacion se creo exitosamente
						infos.add("", new ActionMessage("message.integracion.corp.creacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cancelarRegistroLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.creacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("modificarLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.modificacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cancelarModificarLocalizacion")){
						//se cancela el registro de localizacion
						infos.add("", new ActionMessage("message.integracion.corp.cancelacion.modificacion.empresa.localizacion","Localizaci\u00F3n"));
						
					}else if(valorCerrarPopUp.equals("cerrarPersonaEmpresa")){
						
						if(StringUtils.isNotEmpty(formulario.getTipoDocumento())){
							if (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
									|| (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null)) {
								if(ContactoUtil.isRucPersona()){
									ContactoUtil.limpiarContacto(formulario, session);
									ContactoUtil.setRucPersona(false);
								}
								ContactoUtil.ejecutarAccionControlador(request, response, "#{personaController.visualizarPersona()}");
							} else if (formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
								ContactoUtil.ejecutarAccionControlador(request, response, "#{empresaController.visualizarEmpresa()}");
							}
						}
					}
					
//					if(request.getParameter("cerrarPopUpCorporativo").equals("cancelarCreacion")){
//						ContactoUtil.mostrarPopUpCorporativo(request,session,"buscarPersona",formulario);
//					}
					/*else if(request.getParameter("cerrarPopUpCorporativo").equals("cerrarPersonaEmpresa")){
						Map<String, Object> parametrosParaCorp;
						parametrosParaCorp=ContactoUtil.asignarParametrosCorpVisualizarPersona();

						if(formulario instanceof CotizarReservarForm){
							parametrosParaCorp.put("heightDivDatosGenerales", "210");
							parametrosParaCorp.put("scrollHeightComRelatedContacts", "160");
						}
						ContactoUtil.obtenerDatosPersonaEmpresa(request, formulario, infos, warnings, errors, beanSession);
//						ContactoUtil.integrarCorporativoJSF(request, session, "visualizarPersona", formulario, null, parametrosParaCorp);
//						ContactoUtil.construirTabsContactoPedido(request, formulario);
					}*/
//					else{
//						ContactoUtil.obtenerDatosPersonaEmpresa(request, response, formulario, infos,warnings,errors, beanSession);
//					}
				}
				else{
					formulario.setTipoDocumento("");
					formulario.setNombrePersona("");
					formulario.setNombreEmpresa("");
					session.removeAttribute(ContactoUtil.PERSONA);
					session.removeAttribute(ContactoUtil.LOCALIZACION);
				}
//				else{
//					if(valorCerrarPopUp.equals("cancelarCreacion")){
//						ContactoUtil.mostrarPopUpCorporativo(request,session,"buscarPersona",formulario,errors);
//					}
//					if(valorCerrarPopUp.equals("cerrarPersonaEmpresa")){
//						Map<String, Object> parametrosParaCorp;
//						parametrosParaCorp=ContactoUtil.asignarParametrosCorpVisualizarPersona();
//						formulario.setTipoDocumento("");
//						ContactoUtil.integrarCorporativoJSF(request, session, "visualizarPersona", formulario, null, parametrosParaCorp);
//						ContactoUtil.construirTabsContactoPedido(request, formulario);
//					}
//					formulario.setNombrePersona("");
//					formulario.setNombreEmpresa("");
//					session.removeAttribute(ContactoUtil.PERSONA);
//					session.removeAttribute(ContactoUtil.LOCALIZACION);
//				}
				//se regresa siempre al tab del pedido
				ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
				
				//actualizar componente de empresas -- ultimos cambios
//				FacesUtil.getDefaultInstance().getFacesContext(request, response);
//				FacesUtil.getDefaultInstance().resetManagedBean("empresaController");

			}
			/**
			 * @author Wladimir L\u00F3pez
			 * Control para los tabs de persona y pedidos
			 */
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				
				String parametro= beanSession.getPaginaTab().getParametroRequest();
				if(!parametro.equals("rTabPopUp")){
					if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
						/*--------------- si se escogi\u00F3 el tab de personas ----------------*/
						LogSISPE.getLog().info("Se elige el tab de PERSONAS");
						//ContactoUtil.cambiarTabContactoPedidos(beanSession, 0);
						session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//						ContactoUtil.ejecutarAccionControlador(request, response, "#{personaEmpresaController.cargarBuscar()}");
						
					}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
						/*------------ si se escogi\u00F3 el tab de pedidos entregados --------------*/
						LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
						//ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
						session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
					}
				}
				else{
					if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
						/*--------------- Detalles del nuevo pedido ----------------*/
						cambiarTabsDiferenciasDetallePed(beanSession,0);
					}else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
						/*--------------- Cambios en el pedido ----------------*/
						LogSISPE.getLog().info("Cambios en el pedido");
						cambiarTabsDiferenciasDetallePed(beanSession,1);
					}
				}
			}
			//--Cambios oscar
			//datos de la localizacion desde el componente de contactos
			else if(request.getParameter("localizacionCorporativo")!=null){
				try{
					LogSISPE.getLog().info("Localizacion desde el corporativo");
					
	//				if(session.getAttribute("ec.com.smx.sic.sispe.contactos.localizacion")!=null){
					if(session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP)!=null){
	//					LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO)session.getAttribute("ec.com.smx.sic.sispe.contactos.localizacion");
						LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO)session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP);
						
						if(localizacionSeleccionada.getEstadoLocalizacion().equals(CorporativoConstantes.ESTADO_ACTIVO)){
						
							LogSISPE.getLog().info(localizacionSeleccionada.toString());
							
							if(ContactoUtil.asignarDatosLocalizacion(localizacionSeleccionada, formulario)){
								infos.add("localizacion",new ActionMessage("info.contactos.localizacion.seleccionada", localizacionSeleccionada.getDescripcionLocalizacion()));
							}else{
								warnings.add("localizacion",new ActionMessage("error.contactos.localizacion.sinContacto", localizacionSeleccionada.getDescripcionLocalizacion()));
							}
						
						}else {
							errors.add("localizacion",new ActionMessage("error.contactos.localizacion.inactivo", localizacionSeleccionada.getDescripcionLocalizacion()));
						}
						
					}else{
						infos.add("localizacion",new ActionMessage("error.contactos.localizacion.noAsignada"));
					}
				} catch (Exception e) {
					//excepcion desconocida
					LogSISPE.getLog().error("Error al abrir la busqueda de contactos",e);
					errors.add("",new ActionMessage("errors.gerneral",e.getMessage()));
				}
			}
			//cuando se desea ver la localizacion de una empresa del corporativo desde el sispe --cambios oscar
			else if(request.getParameter("visualizarLocalizacion")!=null){
				
				LogSISPE.getLog().info("Ver localizacion");
				
				ContactoUtil.cargarComponenteLocalizaciones(request);
				ContactoUtil.mostrarPopUpCorporativo(request, session, "verLocalizacion", formulario,errors);
				//validar si se selecciona la localizacion desde la pesta\u00F1a de resumen de la reservacion
//				if (session.getAttribute(ContactoUtil.TAB_RESUMEN_PEDIDO) != null) {
//					PaginaTab tabsResumen = ContactoUtil.construirTabsResumenPedidoEspecial(request,formulario);
//					beanSession.setPaginaTab(tabsResumen);
//					forward = "resumen";
//				}
			}
			//IOnofre. Saca el PopUp para buscar los articulos
			else if(request.getParameter("buscador")!=null){
				LogSISPE.getLog().info("Busqueda de Articulos");
				BuscarArticuloUtil.accionesBuscarArticulos(request, formulario, errors, exitos, warnings, infos);	
			}
			//****************************** PRIMERA ENTRADA AL PEDIDO *****************************//
			else if (session.getAttribute(RESUMEN_PEDIDO) == null) {
				
				//se eliminan las variables de sesi\u00F3n que comiencen con "ec.com.smx"
				SessionManagerSISPE.removeVarSession(request);
				this.inicializarPedido(formulario, request);
				formulario.setTipoDocumento("");
				//Verifico quien es la entidad responsable si el local o la bodega
				String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				LogSISPE.getLog().info("entidad: "+entidadLocal);
				//se realiza la consulta de los locales
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

				//tomo la clave de pedido especial
				TipoPedidoDTO tipoPedidoDTO = new TipoPedidoDTO();
				tipoPedidoDTO.setCaracteristicaTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.caracteristicaEspecial"));

				EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
				especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				especialDTO.setEstadoEspecial(estadoActivo);
				especialDTO.setTipoPedidoDTO(tipoPedidoDTO);
				especialDTO.setEspecialesClasificaciones(new ArrayList<EspecialClasificacionDTO>());
				especialDTO.setPublicado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
				
				//entra aqui cuando viene de la validacion de pedidos pavos
				if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) != null) {
//					especialDTO.getId().setCodigoEspecial(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigo.especial.pavos.especial"));
					especialDTO.getTipoPedidoDTO().getId().setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.especial.pavos"));
					
				}
				
				LogSISPE.getLog().info("tipo pedido: {}",especialDTO.getTipoPedidoDTO().getCaracteristicaTipoPedido());
				//Trae los objetos de la tabla de especiales [carnes, pollos]
				List<EspecialDTO> especialDTOcol =(List<EspecialDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerEspecial(especialDTO);
				
				if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null) {
					
					EspecialDTO especialKey= new EspecialDTO();
					TipoPedidoDTO tipoPedidoKey = new TipoPedidoDTO();
					tipoPedidoKey.getId().setCodigoTipoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.especial.pavos"));
					especialKey.setTipoPedidoDTO(tipoPedidoKey);
					
					Comparator<EspecialDTO> comparator = new Comparator<EspecialDTO>() {
	
						public int compare(EspecialDTO o1, EspecialDTO o2) {
							return o1.getTipoPedidoDTO().getId().getCodigoTipoPedido().compareTo(o2.getTipoPedidoDTO().getId().getCodigoTipoPedido());
						}
					};
					
					Collections.sort(especialDTOcol, comparator);
					
					int i = Collections.binarySearch(especialDTOcol, especialKey, comparator);
					
					if ( i >=  0) {
						especialDTOcol.remove(i);
					}
				
				}
				session.setAttribute(COL_TIPO_ESPECIAL, especialDTOcol);
				//radiobutton que ser\u00E1 seleccionado por default
				formulario.setOpTipoPedido(especialDTOcol.get(0).getTipoPedidoDTO().getId().getCodigoTipoPedido());

				//envio el indice cero (carnes) para obtener su clasificaci\u00F3n
				WebSISPEUtil.obtenerClasificacionEspecial(request, especialDTOcol, 0);

				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
					LogSISPE.getLog().info("entra a bodega");
					//Asigna valor a la variable porque todavia no se puede inciar el ingreso de articulos hasta no seleccionar un local
					session.setAttribute(PRIMERA_VEZ_BODEGA, "ok");
					//Indica que la entidad de ingreso es la bodega
					session.setAttribute(ES_ENTIDAD_BODEGA,"ok");
					
				}else{
					LogSISPE.getLog().info("entra a local");
					//se almacena el c\u00F3digo del local responsable
					session.setAttribute(CODIGO_LOCAL_REFERENCIA, SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
					//se inicializa el local de despacho
					formulario.setLocalDespacho(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal().toString());
					
					//para controlar fechas de entrega y despacho dependiendo el cierre de d\u00EDa
					this.verificarCierreDiaYFechaMinimaDespacho(formulario, request, infos, errors, 
							SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
				}
				
				//guardo el tipo de pedido escogido
				session.setAttribute(TIPO_ESP_ACTUAL, formulario.getOpTipoPedido());
				//esta variable solo cambia cuando da clic en el bot\u00F3n SI
				session.setAttribute(TIPO_ESP_ANTERIOR, formulario.getOpTipoPedido());
				session.setAttribute(INDICE_ESP_RESPALDO, 0);

				request.getSession().removeAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS);//cambios oscar
				//Se contruye los tabs de Contacto y Pedidos
//				ContactoUtil.getTokenCorp(request);//cambios oscar
				PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedidoEspecial(request, formulario);
				beanSession.setPaginaTab(tabsCotizaciones);
			//	ContactoUtil.reiniciarBotonesSession(4, request);
				
				forward = "desplegar";
			}else if(request.getParameter("redactarMail")!=null){
				LogSISPE.getLog().info("popUp de envio de correo");
				session.setAttribute("ec.com.smx.sic.sispe.redactarMail", "ok");		
				VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				session.setAttribute("ec.com.smx.sic.sispe.paraMail","");
				if(vistaPedido!=null){
					
					//setando por defecto cero en valores nulos para que pinte bien
					vistaPedido.setValorTotalBrutoSinIva(vistaPedido.getValorTotalBrutoSinIva() != null ? vistaPedido.getValorTotalBrutoSinIva() : 0D);
					vistaPedido.setTotalDescuentoIva(vistaPedido.getTotalDescuentoIva()!= null ? vistaPedido.getTotalDescuentoIva() : 0D );
					vistaPedido.setSubTotalNetoBruto(vistaPedido.getSubTotalNetoBruto() != null ? vistaPedido.getSubTotalNetoBruto() : 0D);
					vistaPedido.setSubTotalNoAplicaIVA(vistaPedido.getSubTotalNoAplicaIVA() != null ? vistaPedido.getSubTotalNoAplicaIVA() : 0D);
					vistaPedido.setSubTotalAplicaIVA(vistaPedido.getSubTotalAplicaIVA() != null ? vistaPedido.getSubTotalAplicaIVA() : 0D);
					vistaPedido.setIvaPedido(vistaPedido.getIvaPedido() != null ? vistaPedido.getIvaPedido() : 0D);
					vistaPedido.setValorCostoEntregaPedido(vistaPedido.getValorCostoEntregaPedido() != null ? vistaPedido.getValorCostoEntregaPedido() : 0D);
					vistaPedido.setTotalPedido(vistaPedido.getTotalPedido() != null ? vistaPedido.getTotalPedido() : 0D);
					
					ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedido);
					session.setAttribute("ec.com.smx.sic.sispe.textoMail", MessagesWebSISPE.getString("messages.mail.textoMail").replace("{0}", " "+(vistaPedido.getNombreEmpresa()!=null?vistaPedido.getNombreEmpresa():vistaPedido.getNombrePersona())));
					if(vistaPedido.getNombreEmpresa()!=null && vistaPedido.getCedulaContacto()!=null){
						if(vistaPedido.getEmailContacto()!=null && !vistaPedido.getEmailContacto().isEmpty()){
							session.setAttribute("ec.com.smx.sic.sispe.paraMail", vistaPedido.getEmailContacto());
						}
					}else{
						if(vistaPedido.getEmailPersona()!=null && !vistaPedido.getEmailPersona().isEmpty()){
							session.setAttribute("ec.com.smx.sic.sispe.paraMail",vistaPedido.getEmailPersona());
						}
					}
				}else{
					session.setAttribute("ec.com.smx.sic.sispe.textoMail", MessagesWebSISPE.getString("messages.mail.textoMail").replace("{0}", ""));
				}
				session.setAttribute("ec.com.smx.sic.sispe.asuntoMail",  "Pedido especial");
			}
			
			//Pregunto si se encuentra en la pantalla del resumen mando el forward a la misma en caso de que presionen F5
			if(session.getAttribute(RESUMEN_PEDIDO)!=null && !forward.equals("reportePDF")){
				forward="resumen";
			}
			
			
			//se guarda el beanSession
			SessionManagerSISPE.setBeanSession(beanSession, request);
			//se guardan todos los mensajes generados 
			saveMessages(request, exitos);
			saveInfos(request, infos);
			saveErrors(request, errors);
			saveWarnings(request, warnings);
			LogSISPE.getLog().info("CrearPedidoAction sale por1: {}", forward);

		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			forward = "errorGlobal";
		}
		
		return mapping.findForward(forward);
	}

	/**
	 * Inicializaci\u00F3n de los valores del pedido que no vienen de la aplicaci\u00F3n
	 * 
	 * @param formulario		Formulario manejado actualmente
	 * @param request			Petici\u00F3n que se est\u00E1 procesando
	 * @param accion			Acci\u00F3n que se est\u00E1 realizando
	 */
	private void inicializarPedido(CrearPedidoForm formulario, HttpServletRequest request)throws Exception{
		HttpSession session=request.getSession();
		//indico que la busqueda va a ser desde pedidos especiales
		session.setAttribute(TIPOBUSQUEDA, "ok");
		//Inicializa la coleccion de detalles y de codigo de articulos
		session.setAttribute(DETALLE_PEDIDO, new ArrayList<DetallePedidoDTO>());
		session.setAttribute(COL_CODIGOS_ARTICULOS, new ArrayList<String>());
		//Pregunto si se debe acceder a las autorizaciones
		LogSISPE.getLog().info("estado del pedido especial:{} ", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.pedidoEspecial.estado"));
		if(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.pedidoEspecial.estado").equals(SessionManagerSISPE.getEstadoInactivo(request))){
			session.removeAttribute(SECCION_AUTORIZACION);
		}else{
			LogSISPE.getLog().info("asigna la variable");
			session.setAttribute(SECCION_AUTORIZACION, "ok");
		}

		//Indica que la accion actual es crearPedidoEspecial
		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
				MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.crearPedidoEspecial"));

		/*------ se incializan los campos del formulario ---------*/
		formulario.setIndiceLocalResponsable("");
		formulario.setOpTipoDocumentoContacto(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
		formulario.setTipoDocumento(null);
		//se incializan los datos del cliente 
		formulario.setNumeroDocumento(null);
		formulario.setNombreContacto(null);
		formulario.setTelefonoContacto(null);
		formulario.setRucEmpresa(null);
		formulario.setNombreEmpresa(null);
		//se desactiva el men\u00FA de opciones
		MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
	}

	/**
	 * Crea al objeto PedidoDTO
	 * 
	 * @param  accion			String que indica la acci\u00F3n que se est\u00E1 ejecutando	
	 * @param  formulario		Formulario manejado actualmente
	 * @param  request			La petici\u00F3n manejada actualmente
	 * @return pedidoDTO			El objeto que identifica un <code>PedidoDTO</code>
	 */
	private PedidoDTO crearPedidoDTO(CrearPedidoForm formulario, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		PedidoDTO pedidoDTO = new PedidoDTO();
		EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();

		try
		{
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			
			//se obtiene el id del usuario logeado
			String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();

			//SI SE DESEA GUARDAR UNA COTIZACI\u00F3N 
			pedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			
			Integer codigoLocal = (Integer)session.getAttribute(CODIGO_LOCAL_REFERENCIA);
			//se obtiene el c\u00F3digo del local
			LogSISPE.getLog().info("CODIGO LOCAL: {}",codigoLocal);
			pedidoDTO.getId().setCodigoAreaTrabajo(codigoLocal);

			//se inicializa el valor para la confirmaci\u00F3n de una reserva y para especiales reservados
			pedidoDTO.setConfirmarReserva(estadoInactivo);
			pedidoDTO.setEspecialesReservados(estadoInactivo);

			//se verifica si el usuario logeado es de la bodega
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
				if(session.getAttribute(INGRESO_DESDE_BUSQUEDA)==null){
					//si el usuario es la bodega se debe tomar de este campo, no de la entidad responsable
					//se obtienen los datos del local
					VistaLocalDTO vistaLocalDTO= WebSISPEUtil.obtenerDatosLocal(formulario.getIndiceLocalResponsable(), request);
					//se concatenan los valores del c\u00F3digo y nombre del local
					LogSISPE.getLog().info("codigo local: {}" , vistaLocalDTO.getId().getCodigoLocal());
					LogSISPE.getLog().info("nombre del local: {}" , vistaLocalDTO.getNombreLocal());
					formulario.setLocalResponsable(vistaLocalDTO.getId().getCodigoLocal()+" - "+vistaLocalDTO.getNombreLocal());
				}
				pedidoDTO.setNpDescripcionLocalOrigen(formulario.getLocalResponsable());

			}else{
				pedidoDTO.setNpDescripcionLocalOrigen(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal() 
						+"-" +SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreAreaTrabajo());
				formulario.setLocalResponsable(pedidoDTO.getNpDescripcionLocalOrigen());
			}			
			
			/**
			 * @author wlopez
			 * Permite verificar si un cliente pedido existe ya sea por persona o empresa
			 */			
			//se obtiene la vistaPedidoDTO
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
			
			boolean modificarClientePedido=false;
			if(pedidoDTO.getCodigoClientePedido()==null){
				modificarClientePedido=true;
			}
			else{
				if(vistaPedidoDTO==null || !vistaPedidoDTO.getTipoDocumentoCliente().equals(formulario.getTipoDocumento())){
					modificarClientePedido=true;
				}
				else {
					//para el caso de personas
					if(vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)
							|| vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){												
						if(!vistaPedidoDTO.getNumeroDocumentoPersona().equals(formulario.getNumeroDocumento())){ 
								modificarClientePedido=true;
						}
					}
					//para el caso de empresas
					else if((vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || vistaPedidoDTO.getTipoDocumentoCliente().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL))
							&& !vistaPedidoDTO.getRucEmpresa().equals(formulario.getNumeroDocumento())){													
								modificarClientePedido=true;
							}
					}				
			}
			ClientePedidoDTO cliePed = null;

			// se verifica si el numero de documento del contacto ha cambiado
			if (modificarClientePedido == true) {
				cliePed = ContactoUtil.consultarClientePedido(formulario.getTipoDocumento(), request);

				if (cliePed!=null && cliePed.getId().getCodigoClientePedido() != null) {
					pedidoDTO.setCodigoClientePedido(cliePed.getId().getCodigoClientePedido());
					pedidoDTO.setClientePedidoDTO(cliePed);
				}else{
					pedidoDTO.setCodigoClientePedido(null);
					pedidoDTO.setClientePedidoDTO(cliePed);
				}
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumento());
			}
			
			if(formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) ||
					formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
					|| (formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null)){
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
				pedidoDTO.setNpCedulaContacto(formulario.getNumeroDocumentoPersona());
				pedidoDTO.setNpNombreContacto(formulario.getNombrePersona());
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumento());
				pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoPersona());
				if (request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)!=null){
					pedidoDTO.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.ruc.persona.natural"));
				}
			}
			else if(formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || formulario.getTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){																
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
				pedidoDTO.setNpCedulaContacto(formulario.getNumeroDocumentoContacto());
				pedidoDTO.setNpNombreContacto(formulario.getNombreContacto());
				pedidoDTO.setNpTipoDocumento(formulario.getTipoDocumentoContacto());
				pedidoDTO.setNpTelefonoContacto(formulario.getTelefonoContacto());
				//se setean los datos de la empresa
				pedidoDTO.setNpNombreEmpresa(formulario.getNombreEmpresa());
				pedidoDTO.setNpRucEmpresa(formulario.getRucEmpresa());
				pedidoDTO.setNpTelefonoEmpresa(formulario.getTelefonoEmpresa());
			}						
			//Se asignan los datos correspondientes al cliente
			/*pedidoDTO.setCedulaContacto(formulario.getIdentificacionContacto().trim());
			pedidoDTO.setNombreContacto(formulario.getNombreContacto());
			pedidoDTO.setTelefonoContacto(formulario.getTelefonoContacto());
			//se verifica el contexto del documento si es "empresarial" o "individual"
			if(formulario.getOpTipoDocumento()!=null 
					&& formulario.getOpTipoDocumento().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"))) {
				session.setAttribute("ec.com.smx.sic.sispe.pedido.empresarial","ok");
				pedidoDTO.setRucEmpresa(formulario.getRucEmpresa());
				pedidoDTO.setNombreEmpresa(formulario.getNombreEmpresa());
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
			}else{
				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
			}
			
			//se verifica el tipo de documento del cliente
			if(formulario.getOpTipoDocumentoContacto()!=null
					&& formulario.getOpTipoDocumentoContacto().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
				pedidoDTO.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"));
			}else{
				pedidoDTO.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.pasaporte"));
			}*/
			
			//Se asignan los datos correspondientes al cliente
			/**
			 * @author wlopez
			 * Permite verificar si un cliente pedido existe ya sea por persona o empresa
			 */

//			pedidoDTO.setClientePedidoDTO(cliePed);
			
//			if(formulario.getComboTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) ||
//					formulario.getComboTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
//				cliePed = ContactoUtil.consultarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA, request);
//				if(cliePed== null){
//					ContactoUtil.registarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA, request);
//					cliePed = ContactoUtil.consultarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA, request);
//				}
//				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
//			}else if(formulario.getComboTipoDocumento().equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
//				cliePed = ContactoUtil.consultarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC, request);
//				if(cliePed== null){
//					ContactoUtil.registarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC, request);
//					cliePed = ContactoUtil.consultarClientePedido(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC, request);
//				}
//				pedidoDTO.setContextoPedido(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
//			}
			
//			if(cliePed !=null){
//				pedidoDTO.setCodigoClientePedido(cliePed.getId().getCodigoClientePedido());
//				pedidoDTO.setClientePedidoDTO(cliePed);
//			}
			
			pedidoDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			
			//se asigna el tipo de pedido
			pedidoDTO.setCodigoTipoPedido(formulario.getOpTipoPedido());
			
			//se asigna el usuario responsable
			pedidoDTO.setUsuarioResponsable(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			
			//se asigna el sistema
			pedidoDTO.setSysId(SessionManagerSISPE.getDefault().getDefaultSystemId());
			
			LogSISPE.getLog().info("tipo de pedido: {}",pedidoDTO.getContextoPedido());

			//pedidoDTO.setDireccionEntrega(direccionEntrega);
			pedidoDTO.setNpCodigosArticulos((Collection<String>)session.getAttribute(COL_CODIGOS_ARTICULOS));
			//se crea el convertidor de fechas
			SqlTimestampConverter convertidor = new SqlTimestampConverter(new String[]{"formatos.fecha"});
			if(request.getSession().getAttribute(CONFIRMAR_PEDIDO)!=null){
				//fecha minima de entrega y despacho
				pedidoDTO.setPrimeraFechaEntrega((Timestamp)convertidor.convert(Timestamp.class,formulario.getFechaEntrega()));
				LogSISPE.getLog().info("fecha de entrega: {}" , formulario.getFechaEntrega());
				pedidoDTO.setPrimeraFechaDespacho((Timestamp)convertidor.convert(Timestamp.class, formulario.getFechaDespacho()));
				LogSISPE.getLog().info("fecha de despacho: {}",formulario.getFechaDespacho());
			}else{
				//fecha minima de entrega y despacho
				pedidoDTO.setPrimeraFechaEntrega((Timestamp)convertidor.convert(Timestamp.class,formulario.getFechaEntregaPopUp()));
				LogSISPE.getLog().info("fecha de entrega pop : {}", formulario.getFechaEntregaPopUp());
				pedidoDTO.setPrimeraFechaDespacho((Timestamp)convertidor.convert(Timestamp.class, formulario.getFechaDespachoPopUp() ));
				LogSISPE.getLog().info("fecha de despacho pop: {}",formulario.getFechaDespachoPopUp());
			}

			//se asigna la fecha m\u00EDnima de entrega
			pedidoDTO.setFechaMinimaEntrega((Timestamp)session.getAttribute(FECHA_MINIMA_ENTREGA));

			//se asignan los totales por estado
			estadoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			estadoPedidoDTO.getId().setCodigoAreaTrabajo(codigoLocal);
			estadoPedidoDTO.setDescuentoTotalPedido(0D);
			estadoPedidoDTO.setPorcentajeTotalDescuento(0D);
			estadoPedidoDTO.setPorcentajeSubTotalDescuento(0D);
			estadoPedidoDTO.setSubTotalPedido(0D);
			estadoPedidoDTO.setSubTotalAplicaIVA(0D);
			estadoPedidoDTO.setSubTotalNoAplicaIVA(0D);
			estadoPedidoDTO.setIvaPedido(0D);
			estadoPedidoDTO.setTotalPedido(0D);
			estadoPedidoDTO.setUserId(userId);
			estadoPedidoDTO.setEstadoValidacion(estadoInactivo);
			estadoPedidoDTO.setEstadoPreciosAfiliado(estadoActivo);
			estadoPedidoDTO.setEstadoCalculosIVA(estadoActivo);
			
			pedidoDTO.setCodigoLocalEntrega(Integer.valueOf(formulario.getLocalDespacho()));
			LogSISPE.getLog().info("codigo local entrega: {}", pedidoDTO.getCodigoLocalEntrega());
			
			pedidoDTO.setEstadoPedidoDTO(estadoPedidoDTO);
			if(session.getAttribute(AUTORIZACION)!=null){
				pedidoDTO.setNpAutorizacionesPedidoCol(new ArrayList<AutorizacionDTO>());
				pedidoDTO.getNpAutorizacionesPedidoCol().add((AutorizacionDTO)session.getAttribute(AUTORIZACION));
			}
			pedidoDTO.setClienteProcesadoCorporativo(estadoInactivo);
			
			//Guardo la fecha en que se guardo el pedido para el reporte PDF
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
			session.setAttribute("ec.com.smx.sic.sispe.pedido.fechaPedido",simpleDateFormat.format(new Date()));

			//validacion para pedidos especiales, npRegistroPedidoEspecial se utiliza para la validacion del del stock de perecibles 
			if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null) {
				pedidoDTO.setNpRegistroPedidoEspecial(Boolean.FALSE);
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return pedidoDTO;
	}

	/**
	 *  @param formulario		Formulario manejado actualmente
	 * @param request			petici\u00F3n que se est\u00E1 procesando
	 * @param infos				Mensajes
	 * @param parametroDTO		diferencia de d\u00EDas entre despacho y entrega
	 * 
	 */
	private void verificarCierreDiaYFechaMinimaDespacho(CrearPedidoForm formulario, HttpServletRequest request,
			ActionMessages infos, ActionMessages errors, Integer codigoLocal)throws Exception{

		LogSISPE.getLog().info("opFormulario: {}", formulario.getOpTipoPedido());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
		HttpSession session = request.getSession();

		//parametro que indica cu\u00E1ntos d\u00EDas debe haber entre despacho y entrega
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diasASumarALaFechaMinimaDespachoCarnes", request);
		LogSISPE.getLog().info("**dias a sumar: {}" , parametroDTO.getValorParametro());

		int diasASumar = 1;
		if(parametroDTO.getValorParametro() != null){
			diasASumar = Integer.parseInt(parametroDTO.getValorParametro());
		}
		
		//se obtienen los datos del Especial sobre el que se realiz\u00F3 el pedido
		Integer indiceEspecial = (Integer)session.getAttribute(INDICE_ESPECIAL);
		List<EspecialDTO> listEspecialDTO = (List<EspecialDTO>)session.getAttribute(COL_TIPO_ESPECIAL);
		EspecialDTO especialDTO = listEspecialDTO.get(indiceEspecial);
		
		//se realiza la consulta para obtener la fecha m\u00EDnima de despacho
		EspecialDTO consultaEspecialDTO = new EspecialDTO();
		consultaEspecialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaEspecialDTO.setNpCodigoLocal(codigoLocal);
		consultaEspecialDTO.setCodigoBodega(especialDTO.getCodigoBodega());
		
		try{
//			IntFechaMinimaDespachoPedidoEspecialDTO intFechaMinimaDespachoPedidoEspecialDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerFechaMinimaDespachoPedidoEspecialSIC(consultaEspecialDTO);
			IntGestionarPedidoDTO intGestionarPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerFechaMinimaDespachoPedidoEspecialSIC(consultaEspecialDTO);
			if(intGestionarPedidoDTO!=null){
				session.setAttribute(DATOS_FECHA_MINIMA_DESPACHO_DTO, intGestionarPedidoDTO);
				
				consultaEspecialDTO = null;
	
				//se verifica si el estado del d\u00EDa, es decir si fue cerrado o no
				CierreDiaPedidoEspecialDTO cierreDiaPedidoEspecialDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerActualCierreDiaPedidoEspecial(especialDTO.getCodigoTipoPedido());
				
				Timestamp fechaMinimaDespacho = null;
				Timestamp fechaMinimaEntrega = null;
				
				//si ya fue cerrado el dia
				if(cierreDiaPedidoEspecialDTO!=null){
					LogSISPE.getLog().info("d\u00EDa cerrado se toma la siguiente fecha de despacho disponible");
					//muestra el mensaje de que el dia ya fue cerrado
					infos.add("cerrarDia",new ActionMessage("info.especial.cerrarDia.fecha"));
					
//					fechaMinimaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getSigFechaMinimaDespacho(), 0, 0, 0, 0, 0, 0));
//					fechaMinimaEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getSigFechaMinimaDespacho(), 0, diasASumar, 0, 0, 0, 0));
					fechaMinimaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getSigfechaMinDespacho(), 0, 0, 0, 0, 0, 0));
					fechaMinimaEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getSigfechaMinDespacho(), 0, diasASumar, 0, 0, 0, 0));
					session.setAttribute("ec.com.smx.sic.sispe.info.cerrarDia", "ok");
				}else{
					LogSISPE.getLog().info("se toma la fecha m\u00EDnima de despacho disponible");
					
//					fechaMinimaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getFechaMinimaDespacho(), 0, 0, 0, 0, 0, 0));
//					fechaMinimaEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intFechaMinimaDespachoPedidoEspecialDTO.getFechaMinimaDespacho(), 0, diasASumar, 0, 0, 0, 0));
					fechaMinimaDespacho = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getFechaMinDespacho(), 0, 0, 0, 0, 0, 0));
					fechaMinimaEntrega = new Timestamp(WebSISPEUtil.construirFechaCompleta(intGestionarPedidoDTO.getFechaMinDespacho(), 0, diasASumar, 0, 0, 0, 0));
					session.removeAttribute("ec.com.smx.sic.sispe.info.cerrarDia");
				}
		
				//setear campos de fechas de despacho y entrega
				formulario.setFechaDespacho(simpleDateFormat.format(fechaMinimaDespacho));
				formulario.setFechaEntrega(simpleDateFormat.format(fechaMinimaEntrega));
				
				formulario.setFechaDespachoPopUp(simpleDateFormat.format(fechaMinimaDespacho));
				formulario.setFechaEntregaPopUp(simpleDateFormat.format(fechaMinimaEntrega));
		
				//se guarda la fecha m\u00EDniama de despacho en sesi\u00F3n para usarla en las validaciones futuras
				session.setAttribute(FECHA_MINIMA_DESPACHO, fechaMinimaDespacho);
				session.setAttribute(FECHA_MINIMA_ENTREGA, fechaMinimaEntrega);
			}else{
				LogSISPE.getLog().info("No existe comunicacion con el SIC (intFechaMinimaDespachoPedidoEspecialDTO) es nulo");
			}
		}catch(SISPEException ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("fechaMinimaDespacho", new ActionMessage("errors.obtenerFechaMinimaDespachoSIC"));
		}
	}
	
	/**
	 * Este m&eacute;todo selecciona el tab	
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabsDiferenciasDetallePed(BeanSession beanSession, int posicionTab) {
		if(posicionTab != -1){
			ArrayList<?> tabsDiferenciasDetallePed= beanSession.getPaginaTab().getTabs(); 
			for(int i=0;i<tabsDiferenciasDetallePed.size();i++){
				if(posicionTab==i){
					beanSession.getPaginaTab().getTab(i).setSeleccionado(true);
				}
				else{
					beanSession.getPaginaTab().getTab(i).setSeleccionado(false);
				}
			}
			
		}
	}
}
