/* Creado el 27/03/2008
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.BeanSession;
//import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action.CrearPedidoAction;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action.PedidoPerecibleAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.BuscarArticuloForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * * <p>
 * Contiene los campos y los demas controles del formulario del Pedidos aqu\u00ED 
 * se realizan las validaciones de los datos ingresados por el usuario. Tambi\u00E9n se resetea el formulario cada vez que se realiza 
 * una petici\u00F3n.
 * @author jacalderon
 * @author Wladimir L\u00F3pez
 * @version 3.0
 * @since 	JSDK 1.5
 *
 */

@SuppressWarnings({"serial","unchecked"})
public class CrearPedidoForm extends BuscarArticuloForm{
	/*
	 * Campos principales donde se almacenan los datos del contacto y de la empresa:
	 * 
	 * String identificacionCedula: Campo donde se ingresa el n\u00FAmero de c\u00E9dula o pasaporte, este debe ser v\u00E1lido
	 * String nombreContacto: Campo donde se ingresa el nombre del contacto
	 * String telefonoContacto: Campo donde se ingresa el tel\u00E9fono del contacto
	 * String emailContacto: Campo donde se ingresa el correo electr\u00F3nico del contacto
	 * String rucEmpresa: Campo donde se ingresa el ruc de la empresa a la que pertenece el contacto
	 * String nombreEmpresa: Campo donde se ingresa el nombre de la empresa a la que pertenece el contacto 
	 * String opTipoDocumentoContacto: Opcion que indica si el tipo de documento es cedula o pasaporte
	 * String opTipoDocumento: Opcion que indica si los datos igresados seran para contacto y empresa
	 */

	//private String cedulaContacto;
	//private String identificacionContacto;
	
	
	//generico, almacena el RUC, CED, PASS, es la caja de texto
		private String numeroDocumento;
		private String tipoDocumento;
		
		//es el dato del contacto de la empresa o persona
		private String numeroDocumentoContacto;
		private String nombreContacto;
		private String telefonoContacto;
		private String tipoDocumentoContacto;
		private String emailContacto;
		
		//para los datos de la persona
		private String numeroDocumentoPersona;
		private String nombrePersona;
		private String telefonoPersona;
		private String tipoDocumentoPersona;
		private String emailPersona;
		
		//datos de la empresa
		private String rucEmpresa;
		private String nombreEmpresa;
		private String telefonoEmpresa;
		
		private String opTipoDocumentoContacto;
		private String opTipoDocumento;
	

	/*
	 * Campos para el detalle del pedido
	 * 
	 * String cantidadArticulo: Campo donde se ingresa la cantidad de articulos a a\u00F1adir
	 * String pesoArticulo: Campo donde se ingresa
	 * String articulosSeleccionados: Opcion Multiple que permite seleccionar varios articulos del detalle
	 * String checkActualizarStockAlcance: opcion que indica si se desea chequear el stock 
	 * String checkSeleccionarTodo: Opcion que indica que se van a seleccionar o no todos los articulos del detalle
	 * String checksSeleccionar: Opcion que indica que se va a chequear o no un articulo
	 * String codigoArticulo: Campo donde se ingresa el codigo del articulo que se va a agregar al detalle
	 * String cantidadArticuloI: Campo donde se ingresa la cantidad del articulo que se va a agregar al detalle
	 */

	private String cantidadArticulo[];
	private String pesoArticulo[];
	private String observacion[];
	private String checkActualizarStockAlcance;
	private String checkSeleccionarTodo;
	private String checksSeleccionar[];
	private String codigoArticulo;
	private String cantidadArticuloI;


	/*
	 * Campos para la Busqueda de articulos:
	 * 
	 * String opTipoBusqueda: opcion que indica el criterio de la busqueda
	 * String textoBusqueda: Campo donde se ingresa el concepto de la busqueda
	 */

	private String opTipoBusqueda;
	private String textoBusqueda;

	/*
	 *Opci\u00F3n de tipo de art\u00EDculo especial
	 *
	 *String opTipoPedido: indica el tipo de articulo escogido 
	 * */
	private String opTipoPedido;

	/*
	 * Controles que se muestran en la secci\u00F3n de Autorizaci\u00F3n:
	 * 
	 * String opAutorizacion: Opci\u00F3n que permite escoger el tipo de autorizaci\u00F3n que se va aplicar
	 * String numeroAutorizacion: Campo donde se ingresa el n\u00FAmero de Autorizaci\u00F3n
	 * String loginAutorizacion: Campo donde se ingresa el login del usuario
	 * String passwordAutorizacion: Campo donde se ingresa el password del usuario
	 * String observacionAutorizacion: Campo donde se ingresa la observaci\u00F3n por el uso de la autorizaci\u00F3n
	 */
	private String opAutorizacion;
	private String numeroAutorizacion;
	private String loginAutorizacion;
	private String passwordAutorizacion;
	private String observacionAutorizacion;

	/*String indiceLocalResponsable: Listado de los locales existentes
	 * String localResponsable: Campo que contiene lam descripci\u00F3n del local seleccionado.
	 */
	private String indiceLocalResponsable;
	private String localResponsable;
	//private String indiceLocalDespacho;
	private String localDespacho;

	/*String opConfirmarPedido: Opcion que indica si el pedido va a ser confirmado
	 * String fechaDespacho: Campo donde se ingresa la fecha de despacho
	 * String fechaEntrega: Campo donde se ingresa la fecha de entrega
	 */

	private String opConfirmarPedido;
	private String fechaEntrega;
	private String fechaDespacho;
	private String opConfirmarPedidoPopUp;
	private String fechaEntregaPopUp;
	private String fechaDespachoPopUp;	
	
	private String numBonNavEmp;
	
	private String textoMail;
	private String ccMail;
	private String asuntoMail;
	private String emailEnviarCotizacion;

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>cotizarReservar.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		LogSISPE.getLog().info("por resetear");

//		this.cedulaContacto=null;
//		this.identificacionContacto=null;
//		this.numeroDocumento=null;
//		this.nombreContacto=null;
//		this.telefonoContacto=null;
//		this.emailContacto=null;
//		this.rucEmpresa=null;
//		this.nombreEmpresa=null;
		this.opTipoDocumentoContacto=CorporativoConstantes.TIPO_DOCUMENTO_CEDULA;
		this.opTipoDocumento=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal");

		this.opTipoBusqueda=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion");
		this.textoBusqueda=null;

		this.cantidadArticulo=new String[0];
		this.pesoArticulo=new String[0];
		this.observacion=new String[0];
		this.checkActualizarStockAlcance=null;
		this.checkSeleccionarTodo=null;
		this.checksSeleccionar=new String[0];
		this.codigoArticulo=null;
		this.cantidadArticuloI=null;

		this.numeroAutorizacion=null;
		this.loginAutorizacion=null;
		this.passwordAutorizacion=null;
		this.observacionAutorizacion=null;

		this.indiceLocalResponsable=null;
		//this.indiceLocalDespacho=null;

		this.opConfirmarPedido=null;
		this.opConfirmarPedidoPopUp=null;
		
		this.ccMail=null;
		this.emailEnviarCotizacion=null;
		this.asuntoMail=null;
		this.textoMail=null;
	
	}
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>crearPedidoEspecial.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		HttpSession session=request.getSession();
		String peticion = request.getParameter(Globals.AYUDA);
		try
		{
			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			 
			//se obtienen las claves que indican un estado activo y un estado inactivo
			//..String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			//se obtiene el detalle del pedido de sesi\u00F3n
			Collection<DetallePedidoDTO> colDetallePedidoDTO = (ArrayList<DetallePedidoDTO>)session.getAttribute(CrearPedidoAction.DETALLE_PEDIDO);
			
			if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(1)) {
				if(colDetallePedidoDTO != null && !colDetallePedidoDTO.isEmpty()){
					int indiceDetalle = 0;
					//solo si no agregaron art\u00EDculos por la b\u00FAsqueda
					for (DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO){
						try{
							detallePedidoDTO.setNpEstadoError(null);
							long cantidad = 0;
							double peso = 0;
	
							LogSISPE.getLog().info("observacion: {}" , this.observacion[indiceDetalle]);
//							if(this.observacion[indiceDetalle]!=null && !this.observacion[indiceDetalle].equals("")){
								detallePedidoDTO.getEstadoDetallePedidoDTO().setObservacionArticulo(this.observacion[indiceDetalle]);
//							}
	
							//se valida la cantidad
							if(this.cantidadArticulo[indiceDetalle]==null || this.cantidadArticulo[indiceDetalle].equals("")){
								LogSISPE.getLog().info("cantidad vacia");
								this.cantidadArticulo[indiceDetalle]="0";
								
							}else{
								//se verifica el formato de la cantidad
								try{
									cantidad = Long.parseLong(this.cantidadArticulo[indiceDetalle]);
								}catch(NumberFormatException ex){
									cantidad = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
								}
							}
							
							if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null) {
								//se valida el peso
								if(this.pesoArticulo[indiceDetalle]==null || this.pesoArticulo[indiceDetalle].equals("")){
									LogSISPE.getLog().info("peso vacio");
									this.pesoArticulo[indiceDetalle]="0.00";
								}else{
									//se verifica el formato del peso
									try{
										peso = Double.parseDouble(this.pesoArticulo[indiceDetalle]);
									}catch(NumberFormatException ex){
										peso = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado();
									}
								}
							}
	
							detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(cantidad);
							detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(peso);
	
							indiceDetalle++;
						}catch(ArrayIndexOutOfBoundsException ex){
							//solo se atrapa la excepci\u00F3n
							//LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						}
					}
				}
			}
			//ActionMessages messages = new ActionMessages();
			PropertyValidator validator = new PropertyValidatorImpl();
			LogSISPE.getLog().info("***ayuda: ***" + request.getParameter("ayuda"));

			/*----------------- cuando se presiona el boton Actualizar ----------------------*/
			/*LogSISPE.getLog().info("actualizarDetalle: " + request.getParameter("actualizarDetalle"));
			if(request.getParameter("actualizarDetalle")!=null){

				LogSISPE.getLog().info("entra al formulario");

				//si no ocurrio alg\u00FAn error y el detalle no est\u00E1 vacio
				if(colDetallePedidoDTO!=null && !colDetallePedidoDTO.isEmpty()
						&& this.cantidadArticulo.length>0 && this.pesoArticulo.length>0){
					LogSISPE.getLog().info("TAMANO DETALLE: "+colDetallePedidoDTO.size());
					//valida el detalle
					this.validaDetalle(colDetallePedidoDTO, errors, false);
				}
			}*/
			/*------------------------ cuando se da clic en el bot\u00F3n de Aplicar Autorizaci\u00F3n ------------------*/
			if(request.getParameter("aplicarAutorizacion")!=null)
			{
				LogSISPE.getLog().info("opAutorizacion: "+this.opAutorizacion);
				if(this.opAutorizacion.equals(estadoInactivo))
					validator.validateMandatory(errors,"numeroAutorizacion",this.numeroAutorizacion,"errors.requerido","N\u00FAmero de autorizaci\u00F3n");
				else{
					validator.validateMandatory(errors,"loginAutorizacion",this.loginAutorizacion,"errors.requerido","Usuario");
					validator.validateMandatory(errors,"passwordAutorizacion",this.passwordAutorizacion,"errors.requerido","Contrase\u00F1a");
				}
			}
			/*------------------------ cuando se da clic en el bot\u00F3n de Aplicar Autorizaci\u00F3n ------------------*/
			else if(request.getParameter("agregarArticulo")!=null)
			{
				try{
					double cantidad=(new Double(this.cantidadArticuloI)).doubleValue();
					if(cantidad<0){
						cantidad=(-1)*cantidad;
						this.cantidadArticuloI=(new Double(cantidad)).toString();
					}
				}catch(NumberFormatException e){
					LogSISPE.getLog().info("cantidad errada");
					errors.add("cantidadEstado",new ActionMessage("error.validacion.cantidadOpeso.invalido","que desea a\u00F1adir al detalle"));
				}
			}
//			/*-------- cuando se desea enviar por mail un pedido ------------*/
//			else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("enviarMail"))
//			{
//				LogSISPE.getLog().info("va a validar campo de envio de mail");
//				validator.validateMandatory(errors,"emailContacto",this.emailEnviarCotizacion,"errors.requerido","Correo Electr\u00F3nico");
//				if(!errors.isEmpty())
//					//se guarda el error generado
//					request.setAttribute(CrearPedidoAction.ERRORES_ENVIO_MAIL,new ActionMessage("errors.requerido","Correo Electr\u00F3nico"));
//				else{
//					validator.validateFormato(errors,"emailContacto",this.emailEnviarCotizacion,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
//					if(!errors.isEmpty())
//						//se guarda el error generado
//						request.setAttribute(CrearPedidoAction.ERRORES_ENVIO_MAIL,new ActionMessage("errors.formato.email"));
//				}
//				//esta condici\u00F3n es requerida para para que el forward se bien direccionado
//				//se limpia la colecci\u00F3n de errores, para que no tome el forward normal
//				errors.clear();
//			}
			/*
			else if(request.getParameter("cambiarLocalDespacho")!=null){
				LogSISPE.getLog().info("entro a cambiar local despacho");
				if(this.getLocalDespacho().equals("")||this.getLocalDespacho().equals("ciudad")){
					//condici\u00F3n para manejar el error
					errors.add("localDespacho",new ActionMessage("errors.cambio.local"));
				}
			}
			 */
			/*-------------------------Grabar pedido------------------------------------------*/
			else if(request.getParameter("grabarPedido")!=null||peticion!=null && peticion.equals(CrearPedidoAction.REG_PEDIDO)){
				LogSISPE.getLog().info("entra a grabar pedido2");
				if(session.getAttribute(CotizarReservarAction.ES_ENTIDAD_BODEGA)!=null){
					//solo se debe validar este campo si se est\u00E1 creando un pedido desde cero
					if(session.getAttribute(CrearPedidoAction.INGRESO_DESDE_BUSQUEDA) == null){
						//valida que el campor localResponsable sea requerido
						validator.validateMandatory(errors,"indiceLocalResponsable",this.indiceLocalResponsable,"errors.requerido","Local");
					}
				}
				if(this.getLocalDespacho().equals("")||this.getLocalDespacho().equals("ciudad")){
					//condici\u00F3n para manejar el error
					errors.add("localDespacho",new ActionMessage("errors.requerido", "Local Despacho"));
				}
				
				//valida fechas si seleccion\u00F3 confirmar desde el formulario
				if(this.opConfirmarPedido!=null){
					LogSISPE.getLog().info("estado op: {}",this.opConfirmarPedido);
					//se valida el formato de las fechas primero
					validator.validateFecha(errors, "fechaDespacho", this.fechaDespacho, true, MessagesWebSISPE.getString("formatos.fecha"), "errors.formato.fecha","errors.requerido","Fecha de Despacho");
					validator.validateFecha(errors,"fechaEntrega",this.fechaEntrega,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha de Entrega");
					
					//si no hay errores anteriores
					if(errors.isEmpty()){
						SqlTimestampConverter convertidorFechas = new SqlTimestampConverter(new String[]{"formatos.fecha"});
						//se obtiene la fecha m\u00EDnima de despacho
						Timestamp fechaMinimaDespacho = (Timestamp)session.getAttribute(CrearPedidoAction.FECHA_MINIMA_DESPACHO);
						Timestamp fechaMinimaEntrega = (Timestamp)session.getAttribute(CrearPedidoAction.FECHA_MINIMA_ENTREGA);
						Timestamp fechaDespachoIngresada = (Timestamp)convertidorFechas.convert(Timestamp.class, this.fechaDespacho);
						Timestamp fechaEntregaIngresada = (Timestamp)convertidorFechas.convert(Timestamp.class, this.fechaEntrega);
						
						LogSISPE.getLog().info("va a validar diferencia de fechas");
						//LA FECHA DE DESPACHO INGRESADA DEBE SER MAYOR A LA FECHA MINIMA DE DESPACHO 
						//QUE ENV\u00EDA EL SIC
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
						//si la fecha de despacho ingresada es menor a la m\u00EDnima
						if(fechaDespachoIngresada.getTime() < fechaMinimaDespacho.getTime()){
							errors.add("fechaDespacho", new ActionMessage("errors.fechaIngresada.menorMinima","Fecha de despacho", simpleDateFormat.format(fechaMinimaDespacho)));
						}else	if(fechaEntregaIngresada.getTime() < fechaMinimaEntrega.getTime()){
							//la fecha de entrega ingresada debe ser mayor a la fecha m\u00EDnima de entrega
							errors.add("fechaEntrega", new ActionMessage("errors.fechaIngresada.menorMinima","Fecha de entrega", simpleDateFormat.format(fechaMinimaEntrega)));
						}else if(fechaDespachoIngresada.getTime() >= fechaEntregaIngresada.getTime()){
							//la fecha de despacho ingresada debe ser menor a la fecha de entrega
							errors.add("fechaDespacho", new ActionMessage("errors.fechaDespacho.mayorAFechaEntrega"));
						}
					}
				}
				
				//valida fechas si seleccion\u00F3 confirmar desde el popUP
				if(this.opConfirmarPedidoPopUp!=null){
					LogSISPE.getLog().info("estado opPopUp: {}",this.opConfirmarPedidoPopUp);
					//se valida el formato de las fechas primero
					this.fechaDespacho=this.fechaDespachoPopUp;
					this.fechaEntrega=this.fechaEntregaPopUp;
					
					validator.validateFecha(errors, "fechaDespacho", this.fechaDespachoPopUp, true, MessagesWebSISPE.getString("formatos.fecha"), "errors.formato.fecha","errors.requerido","Fecha de Despacho");
					validator.validateFecha(errors,"fechaEntrega",this.fechaEntregaPopUp,true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha de Entrega");
					LogSISPE.getLog().info("fecha despacho pop: {}",this.fechaDespachoPopUp);
					LogSISPE.getLog().info("fecha entrega pop: {}",this.fechaEntregaPopUp);
					if(!errors.isEmpty()){
						session.setAttribute(CrearPedidoAction.CONFIRMAR_PEDIDO, "ok");
						this.opConfirmarPedido="ok";
						request.getSession().setAttribute(SessionManagerSISPE.POPUP, null);
					}
					
					if(errors.isEmpty()){
					SqlTimestampConverter convertidorFechas = new SqlTimestampConverter(new String[]{"formatos.fecha"});
					//se obtiene la fecha m\u00EDnima de despacho
					Timestamp fechaMinimaDespacho = (Timestamp)session.getAttribute(CrearPedidoAction.FECHA_MINIMA_DESPACHO);
					Timestamp fechaMinimaEntrega = (Timestamp)session.getAttribute(CrearPedidoAction.FECHA_MINIMA_ENTREGA);

					Timestamp fechaDespachoIngresada = (Timestamp)convertidorFechas.convert(Timestamp.class, this.fechaDespachoPopUp);
					Timestamp fechaEntregaIngresada = (Timestamp)convertidorFechas.convert(Timestamp.class, this.fechaEntregaPopUp);

					//si no hay errores anteriores
				
						//LA FECHA DE DESPACHO INGRESADA DEBE SER MAYOR A LA FECHA MINIMA DE DESPACHO 
						//QUE ENV\u00EDA EL SIC
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
						//si la fecha de despacho ingresada es menor a la m\u00EDnima
						if(fechaDespachoIngresada.getTime() < fechaMinimaDespacho.getTime()){
							errors.add("fechaDespacho", new ActionMessage("errors.fechaIngresada.menorMinima","Fecha de despacho", simpleDateFormat.format(fechaMinimaDespacho)));
							this.opConfirmarPedido="ok";
							session.setAttribute(CrearPedidoAction.CONFIRMAR_PEDIDO, "ok");

						}else	if(fechaEntregaIngresada.getTime() < fechaMinimaEntrega.getTime()){
							//la fecha de entrega ingresada debe ser mayor a la fecha m\u00EDnima de entrega
							errors.add("fechaEntrega", new ActionMessage("errors.fechaIngresada.menorMinima","Fecha de entrega", simpleDateFormat.format(fechaMinimaEntrega)));
							this.opConfirmarPedido="ok";
							session.setAttribute(CrearPedidoAction.CONFIRMAR_PEDIDO, "ok");
							session.removeAttribute(SessionManagerSISPE.POPUP);
							
						}else if(fechaDespachoIngresada.getTime() >= fechaEntregaIngresada.getTime()){
							//la fecha de despacho ingresada debe ser menor a la fecha de entrega
							errors.add("fechaDespacho", new ActionMessage("errors.fechaDespacho.mayorAFechaEntrega"));
							this.opConfirmarPedido="ok";
							session.setAttribute(CrearPedidoAction.CONFIRMAR_PEDIDO, "ok");

						}
					}
				}
				if(errors.isEmpty()){
					//----------------------- validaci\u00F3n de los datos de la cabecera---------------------
					//se valida que el tipo de documento sea correcto y sea obligatorio
					validarTipoDocumento(validator, errors, request);
					String  valorComboTipoDoc = getTipoDocumento();
					if((valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE))
							&& this.nombrePersona==null){
						request.getSession().removeAttribute(ContactoUtil.PERSONA);
						request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
						errors.add("nombreContacto",new ActionMessage("message.integracion.corp.buscar.cliente"));
					}
	//				else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
					else if((valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) && request.getSession().getAttribute(ContactoUtil.RUC_PERSONA)==null){
						if(this.nombreEmpresa==null){
							request.getSession().removeAttribute(ContactoUtil.PERSONA);
							request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
							errors.add("nombreEmpresa",new ActionMessage("message.integracion.corp.buscar.cliente"));
						}
						else{
							this.tipoDocumentoContacto=null;
							if(session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP)==null){
								errors.add("localizacionNoAsignada", new ActionMessage("error.contactos.localizacion.noAsignada"));
							}else{
								ContactoUtil.asignarDatosLocalizacion((LocalizacionDTO)session.getAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP), this);
							}
						}
					}
			
					
					//se valida que existan art\u00EDculos en el detalle
					if(colDetallePedidoDTO==null || colDetallePedidoDTO.isEmpty()){
						//si el detalle se encuentra vacio.
						errors.add("detalle",new ActionMessage("errors.detalle.requerido"));
						this.cambiarAlTabPedido(session, request);
					}else{
						//valida el detalle
						if(peticion.equals(CrearPedidoAction.REG_PEDIDO)){
							request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
						}
						this.validaDetalle(colDetallePedidoDTO, errors,true, request,session);
					}
				}
			}
			/*----------------- VALIDA EL TIPO DE DOCUMENTO SELECCIONADO EN EL COMBO BOX----------------------*/
			/*----------------- CUANDO SE INGRESA EL TIPO DE DOCUMENTO ----------------------*/
			else if (request.getParameter("consultarCliente")!=null) {
				LogSISPE.getLog().info("consultarCliente");
			}else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("siEnviarEmail")){
				LogSISPE.getLog().info("request redactarMail");
				validator.validateMandatory(errors,"emailContacto",this.emailEnviarCotizacion,"errors.requerido","Para");
				if(errors.isEmpty()){
					validator.validateFormato(errors,"emailContacto",this.emailEnviarCotizacion,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				if(this.ccMail!=null){
					validator.validateFormato(errors,"emailContactoConCopia",this.ccMail,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				validator.validateMandatory(errors,"asuntoMail",this.asuntoMail,"errors.requerido","Asunto");
				validator.validateMandatory(errors,"textoMail",this.textoMail,"errors.requerido","Contenido");
				if (errors.size()>0){
					request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				}
			}
		}
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Realiza la validaci\u00F3n de los datos del detalle del pedido
	 * 
	 * @param  detallePedido
	 * @param  errors
	 * @param session 
	 * @param  validarObservacion
	 * @throws Exception
	 */
	private void validaDetalle(Collection<DetallePedidoDTO> colDetallePedidoDTO, ActionErrors errors, 
			boolean validarValoresCampos, HttpServletRequest request, HttpSession session)throws Exception{

		int indiceDetalle = 0;
		boolean hayArtSinObs = false;
		boolean hayArtConAmbosValores = false;
		boolean hayArtSinValores = false;
		boolean hayArticulosSinAlcance = false;
		int cantidadMayorStock = 0;
		
		String estadoInactivo = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
		//se itera la colecci\u00F3n de los detalles del pedido
		for (DetallePedidoDTO detallePedidoDTO : colDetallePedidoDTO)
		{
			detallePedidoDTO.setNpEstadoError(null);

			long cantidad = 0;
			double peso = 0;

//			LogSISPE.getLog().info("cantidadIngresada: {}", this.cantidadArticulo[indiceDetalle]);
//			LogSISPE.getLog().info("pesoIngresado: {}", this.pesoArticulo[indiceDetalle]);

			UtilPopUp popup =  (UtilPopUp) request.getSession().getAttribute(SessionManagerSISPE.POPUP);
			if (popup == null) {
				//se valida el valor de la cantidad
//				if(this.cantidadArticulo[indiceDetalle]==null || this.cantidadArticulo[indiceDetalle].equals("")){
//					LogSISPE.getLog().info("cantidad vacia");
//					this.cantidadArticulo[indiceDetalle]="0";
//				}else{
//					//se verifica el formato de la cantidad
//					try{
//						cantidad = Long.parseLong(this.cantidadArticulo[indiceDetalle]);
//					}catch(NumberFormatException ex){
						cantidad = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado();
//					}
//				}
				if (request.getSession().getAttribute("sispe.pedido.pavos") != null) {
					//se valida si la cantida ingresada es mayor al stock
					if(detallePedidoDTO.getArticuloDTO().getNpStockArticulo()<Long.valueOf(cantidad)){
						cantidadMayorStock++;
					}
				}
	
				//valida si la accion no viene de pedidos especiales pavos
				if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null) {
					//se valida el valor del peso
//					if(this.pesoArticulo[indiceDetalle]==null || this.pesoArticulo[indiceDetalle].equals("")){
//						LogSISPE.getLog().info("peso vacio");
//						this.pesoArticulo[indiceDetalle]="0.00";
//					}else{
//						//se verifica el formato del peso
//						try{
//							peso = Double.parseDouble(this.pesoArticulo[indiceDetalle]);
//						}catch(NumberFormatException ex){
							peso = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado();
//						}
//					}
				}
			}
			//si los valores son negativos se hacen positivos
			if(cantidad < 0)
				cantidad=(-1)*cantidad;
			if(peso < 0)
				peso=(-1)*peso;

			if(validarValoresCampos){
				//la cantidad y el peso son excluyentes
				if(cantidad > 0 && peso > 0){
					//errors.add("valoresExcluyentes", new ActionMessage("errors.valoresExcluyentes.cantidadPeso"));
					hayArtConAmbosValores = true;
					detallePedidoDTO.setNpEstadoError("ok");
				}else if(cantidad == 0 && peso == 0){
					//errors.add("valoresRequeridos", new ActionMessage("errors.valoresObligatorios.cantidadPeso"));
					hayArtSinValores = true;
					detallePedidoDTO.setNpEstadoError("ok");
				}

				//LogSISPE.getLog().info("observacion: {}", this.observacion[indiceDetalle]);
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getObservacionArticulo()==null || detallePedidoDTO.getEstadoDetallePedidoDTO().getObservacionArticulo().equals("")){
					detallePedidoDTO.setNpEstadoError("ok");
					hayArtSinObs = true;
				}
			}

			detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(cantidad);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(peso);
//			if(this.observacion.length>0){
//				detallePedidoDTO.getEstadoDetallePedidoDTO().setObservacionArticulo(this.observacion[indiceDetalle]);
//			}
			

			indiceDetalle++;
			
			if(detallePedidoDTO.getArticuloDTO().getNpAlcance()!= null && detallePedidoDTO.getArticuloDTO().getNpAlcance().equals(estadoInactivo)){
				hayArticulosSinAlcance = true;
			}
		}
		
		//valida si la accion no viene de pedidos especiales pavos en cuyo caso la observacion no es obligatoria
		if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) != null) {
			if(hayArtSinObs) {
				//se genera un mensaje para la observaci\u00F3n
				errors.add("observacion",new ActionMessage("error.validacion.observacion.requerido"));
				this.cambiarAlTabPedido(session, request);
			}	
		}

		if(hayArtConAmbosValores){
			errors.add("valoresExcluyentes", new ActionMessage("errors.valoresExcluyentes.cantidadPeso"));
			this.cambiarAlTabPedido(session, request);
		}

		if (hayArtSinValores) {
			
			if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null) {
				errors.add("valoresRequeridos", new ActionMessage("errors.valoresObligatorios.cantidadPeso"));
				
			} else {
				errors.add("valoresRequeridos", new ActionMessage("errors.valoresObligatorios.cantidad"));
			}
			this.cambiarAlTabPedido(session, request);
		}
		
		if(cantidadMayorStock>0){
			errors.add("cantidadMayorStock", new ActionMessage("errors.articulos.cantidadMayorStock"));
			this.cambiarAlTabPedido(session, request);
		}

		if(errors.isEmpty() && hayArticulosSinAlcance){
			errors.add("alcances", new ActionMessage("errors.articulos.sinAlcance"));
			this.cambiarAlTabPedido(session, request);
		}
	}

	/**
	 * @return el emailContacto
	 */
	public String getEmailContacto() {
		return this.emailContacto;
	}
	/**
	 * @param emailContacto el emailContacto a establecer
	 */
	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}
//	/**
//	 * @return el identificacionContacto
//	 */
//	public String getIdentificacionContacto() {
//		return this.identificacionContacto;
//	}
//	/**
//	 * @param identificacionContacto el identificacionContacto a establecer
//	 */
//	public void setIdentificacionContacto(String identificacionContacto) {
//		this.identificacionContacto = identificacionContacto;
//	}
	/**
	 * @return el nombreContacto
	 */
	public String getNombreContacto() {
		return this.nombreContacto;
	}
	/**
	 * @param nombreContacto el nombreContacto a establecer
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	/**
	 * @return el nombreEmpresa
	 */
	public String getNombreEmpresa() {
		return this.nombreEmpresa;
	}
	/**
	 * @param nombreEmpresa el nombreEmpresa a establecer
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	/**
	 * @return el opTipoDocumento
	 */
	public String getOpTipoDocumento() {
		return opTipoDocumento;
	}
	/**
	 * @param opTipoDocumento el opTipoDocumento a establecer
	 */
	public void setOpTipoDocumento(String opTipoDocumento) {
		this.opTipoDocumento = opTipoDocumento;
	}
	/**
	 * @return el opTipoDocumentoContacto
	 */
	public String getOpTipoDocumentoContacto() {
		return this.opTipoDocumentoContacto;
	}
	/**
	 * @param opTipoDocumentoContacto el opTipoDocumentoContacto a establecer
	 */
	public void setOpTipoDocumentoContacto(String opTipoDocumentoContacto) {
		this.opTipoDocumentoContacto = opTipoDocumentoContacto;
	}
	/**
	 * @return el rucEmpresa
	 */
	public String getRucEmpresa() {
		return this.rucEmpresa;
	}
	/**
	 * @param rucEmpresa el rucEmpresa a establecer
	 */
	public void setRucEmpresa(String rucEmpresa) {
		this.rucEmpresa = rucEmpresa;
	}
	/**
	 * @return el telefonoContacto
	 */
	public String getTelefonoContacto() {
		return this.telefonoContacto;
	}
	/**
	 * @param telefonoContacto el telefonoContacto a establecer
	 */
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}


	/**
	 * @return el opTipoBusqueda
	 */
	public String getOpTipoBusqueda() {
		return this.opTipoBusqueda;
	}


	/**
	 * @param opTipoBusqueda el opTipoBusqueda a establecer
	 */
	public void setOpTipoBusqueda(String opTipoBusqueda) {
		this.opTipoBusqueda = opTipoBusqueda;
	}


	/**
	 * @return el textoBusqueda
	 */
	public String getTextoBusqueda() {
		return this.textoBusqueda;
	}


	/**
	 * @param textoBusqueda el textoBusqueda a establecer
	 */
	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}


	/**
	 * @return el checkActualizarStockAlcance
	 */
	public String getCheckActualizarStockAlcance() {
		return this.checkActualizarStockAlcance;
	}


	/**
	 * @param checkActualizarStockAlcance el checkActualizarStockAlcance a establecer
	 */
	public void setCheckActualizarStockAlcance(String checkActualizarStockAlcance) {
		this.checkActualizarStockAlcance = checkActualizarStockAlcance;
	}


	/**
	 * @return el checkSeleccionarTodo
	 */
	public String getCheckSeleccionarTodo() {
		return this.checkSeleccionarTodo;
	}


	/**
	 * @param checkSeleccionarTodo el checkSeleccionarTodo a establecer
	 */
	public void setCheckSeleccionarTodo(String checkSeleccionarTodo) {
		this.checkSeleccionarTodo = checkSeleccionarTodo;
	}


	/**
	 * @return el checksSeleccionar
	 */
	public String[] getChecksSeleccionar() {
		return this.checksSeleccionar;
	}


	/**
	 * @param checksSeleccionar el checksSeleccionar a establecer
	 */
	public void setChecksSeleccionar(String[] checksSeleccionar) {
		this.checksSeleccionar = checksSeleccionar;
	}


	/**
	 * @return el observacion
	 */
	public String[] getObservacion() {
		return this.observacion;
	}
	/**
	 * @param observacion el observacion a establecer
	 */
	public void setObservacion(String[] observacion) {
		this.observacion = observacion;
	}
	/**
	 * @return el cantidadArticulo
	 */
	public String[] getCantidadArticulo() {
		return this.cantidadArticulo;
	}


	/**
	 * @param cantidadArticulo el cantidadArticulo a establecer
	 */
	public void setCantidadArticulo(String[] cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}


	/**
	 * @return el pesoArticulo
	 */
	public String[] getPesoArticulo() {
		return this.pesoArticulo;
	}


	/**
	 * @param pesoArticulo el pesoArticulo a establecer
	 */
	public void setPesoArticulo(String[] pesoArticulo) {
		this.pesoArticulo = pesoArticulo;
	}


	/**
	 * @return el cantidadArticuloI
	 */
	public String getCantidadArticuloI() {
		return this.cantidadArticuloI;
	}


	/**
	 * @param cantidadArticuloI el cantidadArticuloI a establecer
	 */
	public void setCantidadArticuloI(String cantidadArticuloI) {
		this.cantidadArticuloI = cantidadArticuloI;
	}


	/**
	 * @return el codigoArticulo
	 */
	public String getCodigoArticulo() {
		return this.codigoArticulo;
	}


	/**
	 * @param codigoArticulo el codigoArticulo a establecer
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return el loginAutorizacion
	 */
	public String getLoginAutorizacion() {
		return this.loginAutorizacion;
	}
	/**
	 * @param loginAutorizacion el loginAutorizacion a establecer
	 */
	public void setLoginAutorizacion(String loginAutorizacion) {
		this.loginAutorizacion = loginAutorizacion;
	}
	/**
	 * @return el numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return this.numeroAutorizacion;
	}
	/**
	 * @param numeroAutorizacion el numeroAutorizacion a establecer
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	/**
	 * @return el observacionAutorizacion
	 */
	public String getObservacionAutorizacion() {
		return this.observacionAutorizacion;
	}
	/**
	 * @param observacionAutorizacion el observacionAutorizacion a establecer
	 */
	public void setObservacionAutorizacion(String observacionAutorizacion) {
		this.observacionAutorizacion = observacionAutorizacion;
	}
	/**
	 * @return el opAutorizacion
	 */
	public String getOpAutorizacion() {
		return this.opAutorizacion;
	}
	/**
	 * @param opAutorizacion el opAutorizacion a establecer
	 */
	public void setOpAutorizacion(String opAutorizacion) {
		this.opAutorizacion = opAutorizacion;
	}
	/**
	 * @return el passwordAutorizacion
	 */
	public String getPasswordAutorizacion() {
		return this.passwordAutorizacion;
	}
	/**
	 * @param passwordAutorizacion el passwordAutorizacion a establecer
	 */
	public void setPasswordAutorizacion(String passwordAutorizacion) {
		this.passwordAutorizacion = passwordAutorizacion;
	}
	/**
	 * @return el indiceLocalResponsable
	 */
	public String getIndiceLocalResponsable() {
		return this.indiceLocalResponsable;
	}
	/**
	 * @param indiceLocalResponsable el indiceLocalResponsable a establecer
	 */
	public void setIndiceLocalResponsable(String indiceLocalResponsable) {
		this.indiceLocalResponsable = indiceLocalResponsable;
	}
	/**
	 * @return el opConfirmarPedido
	 */
	public String getOpConfirmarPedido() {
		return this.opConfirmarPedido;
	}
	/**
	 * @param opConfirmarPedido el opConfirmarPedido a establecer
	 */
	public void setOpConfirmarPedido(String opConfirmarPedido) {
		this.opConfirmarPedido = opConfirmarPedido;
	}
	/**
	 * @return el fechaEntrega
	 */
	public String getFechaEntrega() {
		return this.fechaEntrega;
	}
	/**
	 * @param fechaEntrega el fechaEntrega a establecer
	 */
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	/**
	 * @return el localResponsable
	 */
	public String getLocalResponsable() {
		return this.localResponsable;
	}
	/**
	 * @param localResponsable el localResponsable a establecer
	 */
	public void setLocalResponsable(String localResponsable) {
		this.localResponsable = localResponsable;
	}
	/**
	 * @return el fechaDespacho
	 */
	public String getFechaDespacho() {
		return fechaDespacho;
	}
	/**
	 * @param fechaDespacho el fechaDespacho a establecer
	 */
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	/**
	 * @return el opTipoPedido
	 */
	public String getOpTipoPedido() {
		return opTipoPedido;
	}
	/**
	 * @param opTipoPedido el opTipoPedido a establecer
	 */
	public void setOpTipoPedido(String opTipoPedido) {
		this.opTipoPedido = opTipoPedido;
	}
	public String getOpConfirmarPedidoPopUp() {
		return opConfirmarPedidoPopUp;
	}
	public void setOpConfirmarPedidoPopUp(String opConfirmarPedidoPopUp) {
		this.opConfirmarPedidoPopUp = opConfirmarPedidoPopUp;
	}
	public String getFechaDespachoPopUp() {
		return fechaDespachoPopUp;
	}
	public void setFechaDespachoPopUp(String fechaDespachoPopUp) {
		this.fechaDespachoPopUp = fechaDespachoPopUp;
	}
	public String getFechaEntregaPopUp() {
		return fechaEntregaPopUp;
	}
	
	public void setFechaEntregaPopUp(String fechaEntregaPopUp) {
		this.fechaEntregaPopUp = fechaEntregaPopUp;
	}

	public String getLocalDespacho() {
		return localDespacho;
	}
	public void setLocalDespacho(String localDespacho) {
		this.localDespacho = localDespacho;
	}
//	/**
//	 * @return el comboTipoDocumento
//	 */
//	public String getComboTipoDocumento() {
//		return comboTipoDocumento;
//	}
//	/**
//	 * @param comboTipoDocumento el comboTipoDocumento a establecer
//	 */
//	public void setComboTipoDocumento(String comboTipoDocumento) {
//		this.comboTipoDocumento = comboTipoDocumento;
//	}
//	/**
//	 * @return el cedulaContacto
//	 */
//	public String getCedulaContacto() {
//		return cedulaContacto;
//	}
//	/**
//	 * @param cedulaContacto el cedulaContacto a establecer
//	 */
//	public void setCedulaContacto(String cedulaContacto) {
//		this.cedulaContacto = cedulaContacto;
//	}
//	
	/**
	 * Este m\u00E9todo permite validar el tipo de documento elegido
	 * @author Wladimir Lopez 
	 * @param validator
	 * @param errors
	 */
	private void validarTipoDocumento(PropertyValidator validator, ActionErrors errors, HttpServletRequest request) {
		LogSISPE.getLog().info("INGRESA A LA VALIDACION DEL TIPO DE DOCUMENTO");
		String  valorComboTipoDoc = getTipoDocumento(); //getComboTipoDocumento();
		validator.validateMandatory(errors,"numeroDocumento",this.numeroDocumento.trim(),"errors.requerido","N\u00FAmero Documento");
		if(StringUtils.isEmpty(tipoDocumento) || StringUtils.isEmpty(nombrePersona) && StringUtils.isEmpty(nombreEmpresa) ){
			errors.add("numeroDocumento",new ActionMessage("errors.cotizarreservar.buscarcliente"));
		}
		if (errors.isEmpty()){
			//se obtiene el bean que contiene los campos genericos
//			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
			
//			if(getNumeroDocumento().matches("\\d+")==true){
////				if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
//				if(getNumeroDocumento().trim().length()==10){
//					if(!validator.validateCedula(this.numeroDocumento.trim())){
//						errors.add("cedulaContacto",new ActionMessage("errors.cedula.incorrecto"));
//						request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//						request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//						ContactoUtil.reiniciarBotonesSession(4, request);
//						ContactoUtil.limpiarVariablesFormulario(this);
//						//ContactoUtil.cambiarTabPersonas(beanSession,  MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//						beanSession.getPaginaTab().getTab(0).setTitulo(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.persona"));
//					}					
//				}
////				else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
//				else if(getNumeroDocumento().trim().length()==13){
//					if(!validator.validateRUC(this.numeroDocumento.trim())){
//						errors.add("cedulaContacto",new ActionMessage("errors.ruc.incorrecto"));
//						request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//						request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//						ContactoUtil.reiniciarBotonesSession(4, request);
//						ContactoUtil.limpiarVariablesFormulario(this);
//						//ContactoUtil.cambiarTabPersonas(beanSession,  MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.empresa"));
//						beanSession.getPaginaTab().getTab(0).setTitulo(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.empresa"));
//					}
//				}
//			}
			
			LogSISPE.getLog().info("VALIDACION DE BUSQUEDA DE PERSONA");
			//String  valorComboTipoDoc = getTipoDocumento();
			if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
				if(!validator.validateCedula(this.numeroDocumento.trim())){
					errors.add("cedulaContacto",new ActionMessage("errors.cedula.incorrecto"));
					request.getSession().removeAttribute(ContactoUtil.PERSONA);
					request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
					ContactoUtil.limpiarVariablesFormulario(this);
				}					
			}
//			else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)){
//				if(!validator.validateCedula(this.numeroDocumento.trim())){
//					errors.add("cedulaContacto",new ActionMessage("errors.pasaporte.incorrecto"));
//					request.getSession().setAttribute(ContactoUtil.PERSONA, null);
//					request.getSession().setAttribute(ContactoUtil.LOCALIZACION, null);
//					ContactoUtil.reiniciarBotonesSession(4, request);
//				}
//			}
			else if(valorComboTipoDoc.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
				if(!validator.validateRUC(this.numeroDocumento.trim())){
					errors.add("cedulaContacto",new ActionMessage("errors.ruc.incorrecto"));
					request.getSession().removeAttribute(ContactoUtil.PERSONA);
					request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
					ContactoUtil.limpiarVariablesFormulario(this);
				}
			}	
		}
		else{
			request.getSession().removeAttribute(ContactoUtil.PERSONA);
			request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
			ContactoUtil.limpiarVariablesFormulario(this);
		}
		
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getNumeroDocumentoContacto() {
		return numeroDocumentoContacto;
	}
	public void setNumeroDocumentoContacto(String numeroDocumentoContacto) {
		this.numeroDocumentoContacto = numeroDocumentoContacto;
	}
	public String getTipoDocumentoContacto() {
		return tipoDocumentoContacto;
	}
	public void setTipoDocumentoContacto(String tipoDocumentoContacto) {
		this.tipoDocumentoContacto = tipoDocumentoContacto;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	/**
	 * @return el numeroDocumentoPersona
	 */
	public String getNumeroDocumentoPersona() {
		return numeroDocumentoPersona;
	}
	/**
	 * @param numeroDocumentoPersona el numeroDocumentoPersona a establecer
	 */
	public void setNumeroDocumentoPersona(String numeroDocumentoPersona) {
		this.numeroDocumentoPersona = numeroDocumentoPersona;
	}
	/**
	 * @return el nombrePersona
	 */
	public String getNombrePersona() {
		return nombrePersona;
	}
	/**
	 * @param nombrePersona el nombrePersona a establecer
	 */
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	/**
	 * @return el telefonoPersona
	 */
	public String getTelefonoPersona() {
		return telefonoPersona;
	}
	/**
	 * @param telefonoPersona el telefonoPersona a establecer
	 */
	public void setTelefonoPersona(String telefonoPersona) {
		this.telefonoPersona = telefonoPersona;
	}
	/**
	 * @return el tipoDocumentoPersona
	 */
	public String getTipoDocumentoPersona() {
		return tipoDocumentoPersona;
	}
	/**
	 * @param tipoDocumentoPersona el tipoDocumentoPersona a establecer
	 */
	public void setTipoDocumentoPersona(String tipoDocumentoPersona) {
		this.tipoDocumentoPersona = tipoDocumentoPersona;
	}
	/**
	 * @return el emailPersona
	 */
	public String getEmailPersona() {
		return emailPersona;
	}
	/**
	 * @param emailPersona el emailPersona a establecer
	 */
	public void setEmailPersona(String emailPersona) {
		this.emailPersona = emailPersona;
	}
	/**
	 * @return el telefonoEmpresa
	 */
	public String getTelefonoEmpresa() {
		return telefonoEmpresa;
	}
	/**
	 * @param telefonoEmpresa el telefonoEmpresa a establecer
	 */
	public void setTelefonoEmpresa(String telefonoEmpresa) {
		this.telefonoEmpresa = telefonoEmpresa;
	}
	public String getNumBonNavEmp() {
		return numBonNavEmp;
	}
	public void setNumBonNavEmp(String numBonNavEmp) {
		this.numBonNavEmp = numBonNavEmp;
	}
	public String getTextoMail() {
		return textoMail;
	}
	public void setTextoMail(String textoMail) {
		this.textoMail = textoMail;
	}
	public String getCcMail() {
		return ccMail;
	}
	public void setCcMail(String ccMail) {
		this.ccMail = ccMail;
	}
	public String getAsuntoMail() {
		return asuntoMail;
	}
	public void setAsuntoMail(String asuntoMail) {
		this.asuntoMail = asuntoMail;
	}
	public String getEmailEnviarCotizacion() {
		return emailEnviarCotizacion;
	}
	public void setEmailEnviarCotizacion(String emailEnviarCotizacion) {
		this.emailEnviarCotizacion = emailEnviarCotizacion;
	}
	
	/**
	 * Este metodo cambia el foco del tab de personas al tab de pedidos
	 * @param session
	 * @param request
	 */
	public void cambiarAlTabPedido(HttpSession session, HttpServletRequest request){		
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
			 LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
			 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);
			 SessionManagerSISPE.setBeanSession(beanSession, request);
		 }
	}
}
