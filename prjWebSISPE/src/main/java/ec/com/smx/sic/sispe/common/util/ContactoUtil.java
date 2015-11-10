package ec.com.smx.sic.sispe.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.CatalogoValorDTO;
import ec.com.smx.corpv2.dto.ContactoPersonaLocalizacionRelacionadoDTO;
import ec.com.smx.corpv2.dto.DatoContactoPersonaLocalizacionDTO;
import ec.com.smx.corpv2.dto.DivisionGeoPoliticaDTO;
import ec.com.smx.corpv2.dto.EmpresaDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.corpv2.dto.id.LocalizacionID;
import ec.com.smx.corpv2.jsf.commons.session.controller.CorporativoSessionControllerBase;
import ec.com.smx.corpv2.jsf.commons.session.datamanager.CorporativoSessionDataManagerBase;
import ec.com.smx.corpv2.web.util.CorpCommonWebConstantes;
import ec.com.smx.framework.common.enumeration.TipoEmpresaEnum;
import ec.com.smx.framework.common.validator.Validator;
import ec.com.smx.framework.common.validator.ValidatorImpl;
import ec.com.smx.framework.exception.FrameworkException;
import ec.com.smx.framework.jsf.common.parser.SystemConnectionSerializer;
import ec.com.smx.framework.jsf.common.parser.URLSystemConnection;
import ec.com.smx.framework.jsf.commons.session.datamanager.SessionDataManagerBase;
import ec.com.smx.framework.jsf.commons.util.FacesUtil;
import ec.com.smx.frameworkv2.multicompany.dto.UserCompanySystemDto;
import ec.com.smx.frameworkv2.security.dto.UserDto;
import ec.com.smx.sic.cliente.common.convenio.TipoDocumentoEmpresaEnum;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.ClientePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.CrearPedidoForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase permite realizar la integraci\u00F3n con el sistema corporativo, especificamente con las pantallas de contactos 
 * @author wlopez
 * @author walvarez
 * @author aesanchez
 *
 */
public class ContactoUtil {

	// Variables de sesion para controlar las personas o empresas de pedidos y pedidos especiales
	public static final String PERSONA = "ec.com.smx.sic.sispe.persona";
	public static final String BTNCREARPERSONA = "ec.com.smx.sic.sispe.btnCrearPersona";
	public static final String BTNCREAREMPRESA = "ec.com.smx.sic.sispe.btnCrearEmpresa";
	public static final String BTNAGREGARLOCALIZACION = "ec.com.smx.sic.sispe.btnAgregarLocalizacion";
	public static final String LOCALIZACION = "ec.com.smx.sic.sispe.localizacion";
	public static final String LOC_GUARDADA = "ec.com.smx.sic.sispe.loc.persit";
	public static final String REPORTE = "ec.com.smx.sic.sispe.reporte";
	public static final String URL_REDIRECT_CONTACTOS = "system.corp.contactos";
	public static final String URL_REDIRECT_VIZUALIZAR_CONTACTOS = "system.corp.visualizar.contactos";
	public static final String ERROR_BUSQUEDA = "system.corp.error";
	public static final String URL_OPCIONES = "ec.com.smx.sic.sispe.url.opciones";
	public static final String ACCION = "ec.com.smx.sic.sispe.accion.execute"; //accion de struct que ejecuta la peticion del componente del corp
	
	//Constantes
	public static final String NUMERO_DOCUMENTO_STRUTS = "numeroDocumentoStruts";
	public static final String TIPO_DOCUMENTO = "tipoDocumentoStruts";
	public static final String HEIGHT_DIV_DATOSGENERALES = "heightDivDatosGenerales";
	public static final String SCROLL_HEIGHT_COMRELATEDCONTACTS = "scrollHeightComRelatedContacts";
	public static final String FLAG_BUSCAR_PERSONA_EMPRESA = "flagBuscarPersonaEmpresa";
	public static final String PERSONA_EMPRESA_SELECCIONADA = "personaEmpresaEncontrada";
	
	/**
	 * Localizacion seleccionada desde el componente de empresa
	 */
	public static final String LOCALIZACION_SELEC_COM_EMP = "ec.com.smx.sic.localizacionSelecComEmp";
	
	//variable para indicar si se va mostrar el resumen del pedido luego de crear un pedido
	public static final String TAB_RESUMEN_PEDIDO = "ec.com.smx.sic.sispe.resumen.pedido";
	
	//se almacena el codigoClientePedido cuando se hace un recotizacion, reserva, modReseva...
	public static final String COD_CLIENTE_PEDIDO = "ec.com.smx.sic.sispe.codigo.clientePedido";
	public static final String RUC_PERSONA = "ec.com.smx.sic.ruc.Persona";
	public static boolean rucPersona= false;
	/*
	 * **************************************************** PEDIDOS ************************************************
	 */
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsContactoPedido(HttpServletRequest request, ActionForm formulario) {
		LogSISPE.getLog().info("Construir tabs contactos");
		PaginaTab tabsCotizaciones = null;
		try {
			tabsCotizaciones = new PaginaTab("crearCotizacion", "deplegar", 1, 410, request);
			String accionActual=(String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			if(accionActual != null && accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
				//en pedidos consolidados se reduce el tamanio del tab
				tabsCotizaciones = new PaginaTab("crearCotizacion", "deplegar", 1, 400, request);
			}
			Tab tabPersona=new Tab("Persona", "crearCotizacion", "/contacto/persona.jsp", false);
			CotizarReservarForm cotizarReservarForm=null;
			if(formulario instanceof CotizarReservarForm){
				cotizarReservarForm =(CotizarReservarForm) formulario;
				if (null != cotizarReservarForm.getTipoDocumento() ) {
					if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || 
							cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
							|| (cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)){
						tabPersona = new Tab("Persona", "crearCotizacion", "/contacto/persona.jsp", false);
					}
					else if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)||cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
						tabPersona = new Tab("Empresa", "crearCotizacion", "/contacto/persona.jsp", false);	
					}
				}
			}
			else {
				tabPersona = new Tab("Persona", "crearCotizacion", "/contacto/persona.jsp", false);
			}
			Tab tabPedidos = new Tab("Detalle del pedido","crearCotizacion","/servicioCliente/cotizarRecotizarReservar/detallePedido.jsp",true);
			tabsCotizaciones.addTab(tabPersona);
			tabsCotizaciones.addTab(tabPedidos);		
			
			//si es pedido consolidado se oculta el boton de editar persona o empresa 
			if(request.getSession().getAttribute("ec.com.smx.sic.sispe.accion.consolidar") != null){
				request.getSession().setAttribute(REPORTE, "ok");
				request.getSession().setAttribute(TAB_RESUMEN_PEDIDO, "ok");
				request.getSession().setAttribute(ContactoUtil.ACCION, "crearCotizacion.do");
				request.getSession().setAttribute(ContactoUtil.TIPO_DOCUMENTO,cotizarReservarForm.getTipoDocumento());
			}

		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsCotizaciones;
	}

	/**
	 * Este m\u00E9todo selecciona el tab de Pedidos
	 * @author Wladimir L\u00F3pez
	 * @param beanSession
	 */
	public static void cambiarTabPedidos(BeanSession beanSession) {
		try{
			beanSession.getPaginaTab().getTab(0).setSeleccionado(false);
			beanSession.getPaginaTab().getTab(1).setSeleccionado(true);
			beanSession.getPaginaTab().setTabSeleccionado(1);
			beanSession.getPaginaTab().setTituloTabSeleccionado(beanSession.getPaginaTab().getTab(1).getTitulo());
		}
		catch (Exception e) {
			LogSISPE.getLog().info("No se pudo realizar el cambio al tab de pedidos");
		}
	}

	/**
	 * Este m\u00E9todo selecciona el tab Persona
	 * @author Wladimir L\u00F3pez
	 * @param beanSession bean de sesion para el tab de personas y pedidos
	 * @param nombreTab nombre del primer tab
	 */
	public static void cambiarTabPersonas(BeanSession beanSession,String nombreTab) {
		try{
			beanSession.getPaginaTab().getTab(0).setTitulo(nombreTab);
			beanSession.getPaginaTab().getTab(0).setSeleccionado(true);
			beanSession.getPaginaTab().getTab(1).setSeleccionado(false);
			beanSession.getPaginaTab().setTabSeleccionado(0);
			beanSession.getPaginaTab().setTituloTabSeleccionado(nombreTab);
		}
		catch (Exception e) {
			LogSISPE.getLog().info("No se pudo realizar el cambio al tab de Personas");
		}
	
	}

	/**
	 * Este m\u00E9todo permite obtener los datos de una persona o una empresa
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @param formulario
	 * @param errors
	 * @param beanSession
	 * @throws Exception
	 */
	public static boolean obtenerDatosPersonaEmpresa(HttpServletRequest request, HttpServletResponse response, ActionForm formulario, ActionMessages infos, ActionMessages warnings,ActionMessages errors) throws Exception {
		boolean retorno =false;
		LogSISPE.getLog().info("Se ingresa al m\u00E9todo obtener datos de Personas o de empresas ...");

		if (formulario instanceof CotizarReservarForm) {
			CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
			retorno = cargarDatosPedidos2(request, response, formularioPed.getTipoDocumento(), formularioPed.getNumeroDocumento(), formulario, infos, warnings,errors);

		} else if (formulario instanceof CrearPedidoForm) {
			LogSISPE.getLog().info("Pedidos Especiales");
			CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
			retorno = cargarDatosPedidos2(request, response, formularioPedEsp.getTipoDocumento(),formularioPedEsp.getNumeroDocumento(), formulario, infos, warnings,errors);
		}
		
		return retorno;
	}

	/** Verifica si existe, valida el numero documento, lo busca y coloca en sesion la persona/empresa o la busqueda
	 * @param request
	 * @param response
	 * @param tipoDocumento
	 * @param valorDocumento
	 * @param formulario
	 * @param infos
	 * @param warnings
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	private static boolean cargarDatosPedidos2(HttpServletRequest request, HttpServletResponse response, String tipoDocumento,String valorDocumento, ActionForm formulario, ActionMessages infos, ActionMessages warnings,ActionMessages errors) throws Exception {
		
		HttpSession session= request.getSession();
		
		try {
			Object personaEmpresaEncontrada = validarExisteNumeroDocumento(valorDocumento, errors, formulario, request, response);
			
			if (personaEmpresaEncontrada == null) {
				buscarPersonaEmpresa(formulario, request, session, response, errors);
				return false;
			}
			
			//se va a mostrar el componente de busqueda cuando se encuentre persona y empresa
			//con el mismo valor tipo documento
			else if (personaEmpresaEncontrada instanceof List) {
				buscarPersonaEmpresa(formulario, request, session, response, errors);
				return false;
			
			} else if (personaEmpresaEncontrada instanceof PersonaDTO) {
				
				if(validarDatosPersona(personaEmpresaEncontrada,errors,warnings)){
					cargarComponentePersona(request, response, formulario, (PersonaDTO) personaEmpresaEncontrada);
				}else{
					return false;
				}
				
			} else if (personaEmpresaEncontrada instanceof EmpresaDTO) {
				if(validarDatosEmpresa(personaEmpresaEncontrada,errors,warnings)){
					cargarComponenteEmpresa(request, response, formulario, (EmpresaDTO) personaEmpresaEncontrada);
					//validamos si ya se asigno una localizacion desde el corporativo
					if (session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP) == null) {
						LogSISPE.getLog().info("No existe localizacion seleccionada en sesion");
							//verificamos si existe una localizacion guardada generalmente cuando carga un pedido desde busqueda
						if(!setearLocalizacionGuardada(request, response, formulario, infos, warnings, errors)){
							
							//busca todas las localizaciones de la empresa
							Collection<LocalizacionDTO> localizacionesAsignadasList = null;
							LocalizacionDTO localizacionBuscar = new LocalizacionDTO();
							localizacionBuscar.setTipoLocalizacionDTO(new CatalogoValorDTO());
							localizacionBuscar.setEstadoLocalizacionDTO(new CatalogoValorDTO());
							localizacionBuscar.setPaisDTO(new DivisionGeoPoliticaDTO());
							localizacionBuscar.setCiudadDTO(new DivisionGeoPoliticaDTO());
							localizacionBuscar.setUsuarioRegistroDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
							localizacionBuscar.setUsuarioModificacionDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
							localizacionBuscar.setCodigoEmpresa(((EmpresaDTO) personaEmpresaEncontrada).getId().getCodigoEmpresa());
							
							localizacionesAsignadasList = SISPEFactory.getDataService().findObjects(localizacionBuscar);
							
							//selecciona por defecto la matriz
						if (CollectionUtils.isNotEmpty(localizacionesAsignadasList)) {
							LogSISPE.getLog().info("Localizaciones Encontradas: "+localizacionesAsignadasList.size());
							for(LocalizacionDTO localizacionDTO: localizacionesAsignadasList){
								if(localizacionDTO.getTipoLocalizacion().equals(CorporativoConstantes.TIPOLOCALIZACION_MATRIZ)){
									setearLocalizacionSeleccionada(request, response, formulario, infos, warnings, errors,
										localizacionDTO);
								}
							}
						}
					}	
							
					}else {
						 
						//fijando localizacion seleccionada si ya existia en sesion
						setearLocalizacionSeleccionada(request, response, formulario, infos, warnings, errors,
								(LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP));
					}
				}else{
					return false;
				}
				
			}
			
		} catch (SISPEException e) {
			LogSISPE.getLog().info("{}", e);
		}
		
		return true;
		
	}
	
	
	private static boolean validarDatosEmpresa(Object personaEmpresaEncontrada, ActionMessages errors, ActionMessages warnings) {
		EmpresaDTO empresa=(EmpresaDTO) personaEmpresaEncontrada;
		if(empresa.getValorTipoDocumento()==null || empresa.getValorTipoDocumento().trim().isEmpty()){
			errors.add("empresaSinTipoDoc", new ActionMessage("mensaje.contactos.informacion.personEmpresa","empresa","tipo documento"));
			return false;
		}else if(empresa.getRazonSocialEmpresa()==null || empresa.getRazonSocialEmpresa().trim().isEmpty()){
			warnings.add("empresaSinNombre", new ActionMessage("mensaje.contactos.informacion.personEmpresa","empresa","raz\u00F3n social"));
			return true;
		}
		return true;
	}

	private static boolean validarDatosPersona(Object personaEmpresaEncontrada, ActionMessages errors, ActionMessages warnings) {
		PersonaDTO persona=(PersonaDTO) personaEmpresaEncontrada;
		if(persona.getTipoDocumento()==null || persona.getTipoDocumento().trim().isEmpty()){
			errors.add("personaSinTipoDoc", new ActionMessage("mensaje.contactos.informacion.personEmpresa","persona","tipo documento"));
			return false;
		}else if(persona.getNombreCompleto()==null || persona.getNombreCompleto().trim().isEmpty()){
			warnings.add("personaSinNombre", new ActionMessage("mensaje.contactos.informacion.personEmpresa","persona","nombre completo"));
			return true;
		}
		return true;
	}
	
	private static boolean validarDatosLocalizacion(Object personaEmpresaEncontrada, ActionMessages errors, ActionMessages warnings) {
		EmpresaDTO empresa=((LocalizacionDTO) personaEmpresaEncontrada).getEmpresaDTO();
		if(empresa.getValorTipoDocumento()==null || empresa.getValorTipoDocumento().trim().isEmpty()){
			errors.add("empresaSinTipoDoc", new ActionMessage("mensaje.contactos.informacion.personEmpresa","empresa","tipo documento"));
			return false;
		}else if(empresa.getRazonSocialEmpresa()==null || empresa.getRazonSocialEmpresa().trim().isEmpty()){
			warnings.add("empresaSinNombre", new ActionMessage("mensaje.contactos.informacion.personEmpresa","empresa","raz\u00F3n social"));
			return true;
		}
		return true;
	}

	/**
	 * Fijar valores de la localizacion seleccionada en caso de una localizacion seleccionada desde el componente de 
	 * busqueda o que ya se a guardado
	 * @param request
	 * @param response
	 * @param formulario
	 * @param infos
	 * @param warnings
	 * @param errors
	 * @param beanSession
	 * @throws SISPEException
	 * @author osaransig
	 */
	public static void setearLocalizacionSeleccionada(HttpServletRequest request, HttpServletResponse response, 
			ActionForm formulario, ActionMessages infos, ActionMessages warnings,ActionMessages errors, LocalizacionDTO localizacionDTO) throws SISPEException {
		
		HttpSession session = request.getSession();
		
		LogSISPE.getLog().info("Seteando Localizacion en sesion: "+localizacionDTO.getId().getCodigoLocalizacion());
		session.setAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP, localizacionDTO);
		
		//si ya se selecciono la localizacion se asignan los valores en el formulario y lo sube a sesion
		if(!asignarDatosLocalizacion(localizacionDTO, formulario)){
//			infos.add("localizacion",new ActionMessage("info.contactos.localizacion.seleccionada", localizacionDTO.getDescripcionLocalizacion()));
//		}else{
			warnings.add("localizacionNoValida", new ActionMessage("error.contactos.localizacion.sinContacto",localizacionDTO.getDescripcionLocalizacion()));
		}
	}
	
	/**
	 * Setear valores en el formulario de la persona encontrada
	 * @param request
	 * @param formulario
	 * @param beanSession
	 * @param personaEncontrada
	 * @author osaransig
	 */
	public static void setearValoresPersonaEncontradaEnFormulario(HttpServletRequest request, ActionForm formulario, PersonaDTO personaEncontrada) {
		LogSISPE.getLog().info("seteamos valores de la persona encontrada en el formulario");
		if (formulario instanceof CotizarReservarForm) {
			CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
			if (request.getSession().getAttribute(RUC_PERSONA)!=null){
				formularioPed.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				formularioPed.setTipoDocumentoPersona(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				if(personaEncontrada.getNumeroRuc()!=null){
					formularioPed.setNumeroDocumento(personaEncontrada.getNumeroRuc());
					formularioPed.setNumeroDocumentoPersona(personaEncontrada.getNumeroRuc());
				}else{
					formularioPed.setNumeroDocumento(personaEncontrada.getNumeroDocumento()+"001");
					formularioPed.setNumeroDocumentoPersona(personaEncontrada.getNumeroDocumento()+"001");
				}
				if(StringUtils.isNotEmpty(personaEncontrada.getNombreComercial())){
					formularioPed.setNombrePersona(personaEncontrada.getNombreComercial());
				}else{
					formularioPed.setNombrePersona(personaEncontrada.getNombreCompleto());
				}
			}else{
				formularioPed.setTipoDocumento(personaEncontrada.getTipoDocumento());	
				formularioPed.setTipoDocumentoPersona(personaEncontrada.getTipoDocumento());
				formularioPed.setNumeroDocumento(personaEncontrada.getNumeroDocumento());
				formularioPed.setNumeroDocumentoPersona(personaEncontrada.getNumeroDocumento());
				formularioPed.setNombrePersona(personaEncontrada.getNombreCompleto());
			}

			formularioPed.setNombreEmpresa(null);
			formularioPed.setRucEmpresa(null);
			formularioPed.setNumeroDocumentoContacto(null);
			
			formularioPed.setEmailPersona(personaEncontrada.getEmailPersona());	
			formularioPed.setTelefonoPersona(getTelefonoPersona(personaEncontrada));
//			formularioPed.setEmailEnviarCotizacion(personaEncontrada.getEmailPersona());
			

		}  else if (formulario instanceof CrearPedidoForm) {
			
			CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
			//Contruyo el tab de persona
			if (request.getSession().getAttribute(RUC_PERSONA)!=null){
				formularioPedEsp.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				formularioPedEsp.setTipoDocumentoPersona(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
				if(personaEncontrada.getNumeroRuc()!=null){
					formularioPedEsp.setNumeroDocumento(personaEncontrada.getNumeroRuc());
					formularioPedEsp.setNumeroDocumentoPersona(personaEncontrada.getNumeroRuc());
				}else{
					formularioPedEsp.setNumeroDocumento(personaEncontrada.getNumeroDocumento()+"001");
					formularioPedEsp.setNumeroDocumentoPersona(personaEncontrada.getNumeroDocumento()+"001");
				}
				if(StringUtils.isNotEmpty(personaEncontrada.getNombreComercial())){
					formularioPedEsp.setNombrePersona(personaEncontrada.getNombreComercial());
				}else{
					formularioPedEsp.setNombrePersona(personaEncontrada.getNombreCompleto());
				}
			}else{
				formularioPedEsp.setTipoDocumento(personaEncontrada.getTipoDocumento());	
				formularioPedEsp.setTipoDocumentoPersona(personaEncontrada.getTipoDocumento());
				formularioPedEsp.setNumeroDocumento(personaEncontrada.getNumeroDocumento());
				formularioPedEsp.setNumeroDocumentoPersona(personaEncontrada.getNumeroDocumento());
				formularioPedEsp.setNombrePersona(personaEncontrada.getNombreCompleto());
			}
			
			formularioPedEsp.setNombreEmpresa(null);
			formularioPedEsp.setRucEmpresa(null);
			formularioPedEsp.setNumeroDocumentoContacto(null);
			formularioPedEsp.setEmailPersona(personaEncontrada.getEmailPersona());																	
			formularioPedEsp.setTelefonoPersona(getTelefonoPersona(personaEncontrada));

		}
	}
	
	
	/**
	 * Setear valores de empresa encontrada en el formulario correspondiente
	 * @param request
	 * @param formulario
	 * @param beanSession
	 * @param personaEncontrada
	 * @author osaransig
	 */
	public static void setearValoresEmpresaEncontradaEnFormulario(HttpServletRequest request, ActionForm formulario, EmpresaDTO empresaEncontrada) {
		LogSISPE.getLog().info("seteamos valores de la empresa encontrada en el formulario");
		if (formulario instanceof CotizarReservarForm) {
			
			CotizarReservarForm cotizarReservarForm = (CotizarReservarForm) formulario;
			
			cotizarReservarForm.setTipoDocumento(empresaEncontrada.getValorTipoDocumento());

			CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
			formularioPed.setNumeroDocumentoContacto(null);
			formularioPed.setTipoDocumentoContacto(null);
			formularioPed.setNombreContacto(null);
			formularioPed.setTelefonoContacto(null);
			formularioPed.setEmailContacto(null);
			formularioPed.setNumeroDocumentoPersona(null);
			formularioPed.setTipoDocumentoPersona(null);
			formularioPed.setNombrePersona(null);
			formularioPed.setTelefonoPersona(null);
			formularioPed.setEmailPersona(null);
//			formularioPed.setEmailEnviarCotizacion(null);

			formularioPed.setNombreEmpresa(empresaEncontrada.getRazonSocialEmpresa());
			formularioPed.setRucEmpresa(empresaEncontrada.getNumeroRuc());
			formularioPed.setNumeroDocumento(empresaEncontrada.getNumeroRuc());


		} else if (formulario instanceof CrearPedidoForm) {
			
			CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
			
			formularioPedEsp.setTipoDocumento(empresaEncontrada.getValorTipoDocumento());
			formularioPedEsp.setNumeroDocumentoContacto(null);
			formularioPedEsp.setTipoDocumentoContacto(null);
			formularioPedEsp.setNombreContacto(null);
			formularioPedEsp.setTelefonoContacto(null);
			formularioPedEsp.setEmailContacto(null);
			formularioPedEsp.setNumeroDocumentoPersona(null);
			formularioPedEsp.setTipoDocumentoPersona(null);
			formularioPedEsp.setNombrePersona(null);
			formularioPedEsp.setTelefonoPersona(null);
			formularioPedEsp.setEmailPersona(null);
//			formularioPedEsp.setEmailEnviarCotizacion(null);
			
			formularioPedEsp.setNombreEmpresa(empresaEncontrada.getRazonSocialEmpresa());
			formularioPedEsp.setRucEmpresa(empresaEncontrada.getNumeroRuc());
			formularioPedEsp.setNumeroDocumento(empresaEncontrada.getNumeroRuc());
					
		}
		
	}
	
	/**
	 * @param personaEncontrada
	 * @return
	 * @author osaransig
	 */
	private static String getTelefonoPersona(PersonaDTO personaEncontrada) {
		StringBuilder telefonoPersona = new StringBuilder("TD: ");
		
		if (personaEncontrada.getTelefonoDomicilio() != null) {
			telefonoPersona.append(personaEncontrada.getTelefonoDomicilio());	
			
		} else {
			telefonoPersona.append(" SN ");	
		}
		
		if (personaEncontrada.getTelefonoTrabajo() != null) {
			telefonoPersona.append(" - TT: "+personaEncontrada.getTelefonoTrabajo());
			
		} else {
			telefonoPersona.append(" - TT: SN ");
		}
		
		if (personaEncontrada.getTelefonoCelular() != null) {
			telefonoPersona.append(" - TC: ").append(personaEncontrada.getTelefonoCelular());
		} else {
			telefonoPersona.append(" - TC: SN ");
		}
		
		return telefonoPersona.toString();
	}
	
	/**
	 * Metodo para validar si existe el numero de documento ingresado
	 * @return
	 * @author osaransig
	 * @param formulario 
	 * @param request 
	 * @param response 
	 * @param salida 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "finally" })
	public static Object validarExisteNumeroDocumento(String numeroDocumento,ActionMessages errors, ActionForm formulario, HttpServletRequest request, HttpServletResponse response) throws SISPEException {
		LogSISPE.getLog().info("Se ingresa al m\u00E9todo que valida el numero documento y lo busca.");
		Object result = null;
		Validator validador=new ValidatorImpl();
		PersonaDTO personaDTO = null;
		EmpresaDTO empresaDTO = null;
		LogSISPE.getLog().info("Numero Documento : "+ numeroDocumento);
		try{
			if(StringUtils.isNotEmpty(numeroDocumento)){
				if(validador.validateRUC(numeroDocumento)){
					if(!validador.validateTipoRUC(numeroDocumento).equals(TipoEmpresaEnum.NATURAL)){
						//Buscar si existe la empresa
						empresaDTO = getEmpresaDTOTemplate();
						empresaDTO.setNumeroRuc(numeroDocumento.trim());
						empresaDTO.setEstadoEmpresa(CorporativoConstantes.ESTADO_ACTIVO);
						empresaDTO = SISPEFactory.getDataService().findUnique(empresaDTO);
					}else{
						//Buscar si existe la persona de ese ruc natural
						personaDTO = getPersonaDTOTemplate();
						personaDTO.setNumeroDocumento(numeroDocumento.substring(0, numeroDocumento.length()-3));
						personaDTO.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
						personaDTO = SISPEFactory.getDataService().findUnique(personaDTO);
						if(personaDTO!=null && personaDTO.getNumeroRuc()!=null){
							request.getSession().setAttribute(RUC_PERSONA,numeroDocumento);	
						}else{
							request.getSession().setAttribute(RUC_PERSONA,"ok");
						}
					}
						
				}else if(validador.validateCedula(numeroDocumento)){
					//Buscar si existe la persona
					personaDTO = getPersonaDTOTemplate();
					personaDTO.setNumeroDocumento(numeroDocumento.trim());
					personaDTO.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
					personaDTO = SISPEFactory.getDataService().findUnique(personaDTO);
				}else{
					//Buscar si existe la persona
					personaDTO = getPersonaDTOTemplate();
					personaDTO.setNumeroDocumento(numeroDocumento.trim());
					personaDTO.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
					personaDTO = SISPEFactory.getDataService().findUnique(personaDTO);
					
					//Buscar si existe la empresa
					empresaDTO = getEmpresaDTOTemplate();
					empresaDTO.setNumeroRuc(numeroDocumento.trim());
					empresaDTO.setEstadoEmpresa(CorporativoConstantes.ESTADO_ACTIVO);
					empresaDTO = SISPEFactory.getDataService().findUnique(empresaDTO);
					
				}
					
				if (personaDTO != null && empresaDTO != null) {
					
					result = new ArrayList<Object>();
					((List) result).add(personaDTO);
					((List) result).add(empresaDTO);
					
				} else if (personaDTO != null && empresaDTO == null) {
					result = personaDTO;
					LogSISPE.getLog().info("Persona Encontrada: "+ personaDTO.getNombreCompleto());
					
				} else if (personaDTO == null && empresaDTO != null) {
					result = empresaDTO;
					LogSISPE.getLog().info("Empresa Encontrada: "+ empresaDTO.getNombreComercialEmpresa());
				}
			}else{
				result=null;
			}
		}catch(Exception e){
			errors.add("",new ActionMessage("error.contactos.busqueda.emper"));
			
		}finally{
			return result;
		}
	}
	
	/**Instancia el objeto personadto
	 * @return personaDTO
	 */
	public static PersonaDTO getPersonaDTOTemplate() {
		PersonaDTO personaDTO = new PersonaDTO();
		personaDTO.setEstadoCivilDTO(new CatalogoValorDTO());
//		personaDTO.setEstadoPersonaDTO(new CatalogoValorDTO());
		personaDTO.setGeneroPersonaDTO(new CatalogoValorDTO());
//		personaDTO.setOrigenDatoDTO(new CatalogoValorDTO());
		personaDTO.setTipoDocumentoDTO(new CatalogoValorDTO());
		personaDTO.setTipoEmpleadoDTO(new CatalogoValorDTO());
		personaDTO.setPaisNacimientoDTO(new DivisionGeoPoliticaDTO());
		personaDTO.setCiudadNacimientoDTO(new DivisionGeoPoliticaDTO());
//		personaDTO.setCiudadResidenciaDTO(new DivisionGeoPoliticaDTO());
//		personaDTO.setPaisResidenciaDTO(new DivisionGeoPoliticaDTO());
		personaDTO.getCiudadNacimientoDTO().setDivisionGeoPoliticaPadreDTO(new DivisionGeoPoliticaDTO());
		personaDTO.setActividadDTO(new CatalogoValorDTO());
//		personaDTO.setSectorPersonaDTO(new DivisionGeoPoliticaDTO());
//		personaDTO.setConyugueDTO(new PersonaDTO());
//		personaDTO.setFotoPersonaDTO(new FotoPersonaDTO());
		personaDTO.setUsuarioRegistroDTO(new UserDto());
		personaDTO.setUsuarioModificacionDTO(new UserDto());	
		return personaDTO;
	}
	
	/**Instancia el objeto empresadto
	 * @return empresaDTO
	 */
	public static EmpresaDTO getEmpresaDTOTemplate() {
		EmpresaDTO empresaDTO=new EmpresaDTO();
		empresaDTO.setUsuarioRegistroDTO(new UserDto());
		empresaDTO.setUsuarioModificacionDTO(new UserDto());
		empresaDTO.setTipoDocumentoDTO(new CatalogoValorDTO());
		empresaDTO.setTipoEmpresaDTO(new CatalogoValorDTO());
		empresaDTO.setEstadoEmpresaDTO(new CatalogoValorDTO());
		empresaDTO.setOrigenDatoDTO(new CatalogoValorDTO());
		empresaDTO.setLocalizaciones(new ArrayList<LocalizacionDTO>());
		empresaDTO.getLocalizaciones().add(new LocalizacionDTO());
		return empresaDTO;
	}

	/**
	 * @param formulario
	 */
	public static void limpiarVariablesFormulario(ActionForm formulario) {
		LogSISPE.getLog().info("limpiando datos de contacto del formulario");
		if (formulario instanceof CotizarReservarForm) {
			CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
			formularioPed.setNombreEmpresa(null);
			formularioPed.setRucEmpresa(null);
			//cargar la informacion del contacto
			formularioPed.setNombreContacto(null);
			formularioPed.setTipoDocumentoContacto(null);
			formularioPed.setNumeroDocumentoContacto(null);
			formularioPed.setTelefonoContacto(null);	
			formularioPed.setNombrePersona(null);
			formularioPed.setTipoDocumentoPersona(null);
			formularioPed.setNumeroDocumentoPersona(null);
			formularioPed.setTelefonoPersona(null);
			formularioPed.setTipoDocumento(null);

		} else if (formulario instanceof CrearPedidoForm) {
			LogSISPE.getLog().info("Pedidos Especiales");
			CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
			formularioPedEsp.setNombreEmpresa(null);
			formularioPedEsp.setRucEmpresa(null);
			//cargar la informacion del contacto
			formularioPedEsp.setNombreContacto(null);
			formularioPedEsp.setTipoDocumentoContacto(null);
			formularioPedEsp.setNumeroDocumentoContacto(null);
			formularioPedEsp.setTelefonoContacto(null);	
			formularioPedEsp.setNombrePersona(null);
			formularioPedEsp.setTipoDocumentoPersona(null);
			formularioPedEsp.setNumeroDocumentoPersona(null);
			formularioPedEsp.setTelefonoPersona(null);	
			formularioPedEsp.setTipoDocumento(null);
		}
	}

	/**
	 * Este m\u00E9todo permite cargar datos en base a la persona o empresa del objeto VistaPedidoDTO
	 * @param request
	 * @param vistaPedidoDTO
	 * @param errors
	 */
	public static void cargarDatosPersonaEmpresa(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO) {
		LogSISPE.getLog().info("Cargar datos persona empresa desde vistaPedido");
		//para el caso de personas
		if (vistaPedidoDTO.getTipoDocumentoCliente()!= null && !vistaPedidoDTO.getTipoDocumentoCliente().equals("RUC")) {
			// Se carga en session la persona
			if(request.getParameter("pedidosAnteriores") == null && request.getParameter("accionesPedAnt") == null && request.getParameter("redactarMail")==null){
				request.getSession().setAttribute(PERSONA, vistaPedidoDTO.getNpPersona());
				request.getSession().setAttribute(LOCALIZACION, null);
			}
			
			//se inicializan las variables de la empresa
			vistaPedidoDTO.setRucEmpresa("NA");
			vistaPedidoDTO.setNombreEmpresa(null);
			vistaPedidoDTO.setTelefonoEmpresa(null);
			
			//se inicializan las variables del contacto
			vistaPedidoDTO.setNombreContacto("SIN CONTACTO");
			vistaPedidoDTO.setCedulaContacto("SN");
			vistaPedidoDTO.setTelefonoContacto("SN");
			vistaPedidoDTO.setEmailContacto("SN");
			vistaPedidoDTO.setContactoCliente("SIN CONTACTO");
			
			//consultar el contacto principal de la persona
			DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();		
			contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
			contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
			contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
			contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			contactoPersonaLocalizacionDTO.setCriteriaSearch(new CriteriaSearch());			
			
			ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
			contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
			contactoRelacionadoDTO.setCodigoPersona(Long.valueOf(Long.valueOf(vistaPedidoDTO.getCodigoPersona())));
			contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
			
			Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactoPersonaLocalizacionCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);
			
			DatoContactoPersonaLocalizacionDTO contactoTipoPersona=null;
			if(CollectionUtils.isNotEmpty(contactoPersonaLocalizacionCol)){
				contactoRelacionadoDTO = (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactoPersonaLocalizacionCol, 0);
				contactoTipoPersona = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
			}			
			
			if(contactoTipoPersona!=null){
				LogSISPE.getLog().info("Contacto de persona encontrada: "+contactoTipoPersona.getPersonaContactoDTO().getNumeroDocumento()+" - "+contactoTipoPersona.getPersonaContactoDTO().getNombreCompleto());
				
				if(contactoTipoPersona.getPersonaContactoDTO()!=null ){
					//cargar la informacion del contacto
					vistaPedidoDTO.setNombreContacto(contactoTipoPersona.getPersonaContactoDTO().getNombreCompleto());
					vistaPedidoDTO.setCedulaContacto(contactoTipoPersona.getPersonaContactoDTO().getNumeroDocumento());
					if(contactoTipoPersona.getEmailPrincipal()!=null){
						vistaPedidoDTO.setEmailContacto(contactoTipoPersona.getEmailPrincipal());
					} else if(contactoTipoPersona.getEmailTrabajo()!=null){
						vistaPedidoDTO.setEmailContacto(contactoTipoPersona.getEmailTrabajo());
					}					
					
					String telefonoContacto="TD: ";
					if(contactoTipoPersona.getNumeroTelefonicoPrincipal()!=null){
						telefonoContacto+=contactoTipoPersona.getNumeroTelefonicoPrincipal();										
					} else telefonoContacto+=" SN ";	
					if(contactoTipoPersona.getNumeroTelefonicoTrabajo()!=null){
						telefonoContacto+=" - TT: "+contactoTipoPersona.getNumeroTelefonicoTrabajo();
					}else telefonoContacto+=" - TT: SN ";	
					if(contactoTipoPersona.getNumeroTelefonicoCelular()!=null){
						telefonoContacto+=" - TC: "+contactoTipoPersona.getNumeroTelefonicoCelular();
					}
					else telefonoContacto+=" - TC: SN ";									
					vistaPedidoDTO.setTelefonoContacto(telefonoContacto);			
					vistaPedidoDTO.setContactoCliente(contactoTipoPersona.getPersonaContactoDTO().getTipoDocumento()+": "+vistaPedidoDTO.getCedulaContacto()
							+" - NC: "+vistaPedidoDTO.getNombreContacto()+" - "+vistaPedidoDTO.getTelefonoContacto());
					}
			}
			if(vistaPedidoDTO.getContactoEmpresa().startsWith(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
				vistaPedidoDTO.setTipoDocumentoPersona(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
//				if (vistaPedidoDTO.getNumeroDocumentoPersona().length()==10){
//					vistaPedidoDTO.setNumeroDocumentoPersona(vistaPedidoDTO.getNumeroDocumentoPersona()+"001");
//				}
			}else{
				vistaPedidoDTO.setTipoDocumentoPersona(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
			}
		} else if (vistaPedidoDTO.getRucEmpresa() != null && !vistaPedidoDTO.getRucEmpresa().equals("NA")) {
			
			//se inicializan las variables del contacto
			vistaPedidoDTO.setNombreContacto("SIN CONTACTO");
			vistaPedidoDTO.setCedulaContacto("SN");
			vistaPedidoDTO.setTelefonoContacto("SN");
			vistaPedidoDTO.setEmailContacto("SN");
			vistaPedidoDTO.setContactoCliente("SIN CONTACTO");
			
			vistaPedidoDTO.setNumeroDocumentoPersona(null);
			vistaPedidoDTO.setNombrePersona(null);			
						
			//se cargan los datos del contacto principal de la empresa
			//Buscar si existe la empresa 			
			EmpresaDTO empresaDto = new EmpresaDTO();
			empresaDto.setNumeroRuc(vistaPedidoDTO.getRucEmpresa());
			empresaDto.setEstadoEmpresa(CorporativoConstantes.ESTADO_ACTIVO);
			EmpresaDTO empresaEncontrada =  SISPEFactory.getDataService().findUnique(empresaDto);		
			
			//	 Se carga en session la empresa
			if(request.getParameter("pedidosAnteriores") == null && request.getParameter("accionesPedAnt") == null && request.getParameter("redactarMail")==null){
				request.getSession().setAttribute(PERSONA, null);
				request.getSession().setAttribute(LOCALIZACION, empresaEncontrada);
			}
			
			if(empresaEncontrada !=null){
				LogSISPE.getLog().info("Localizacion de empresa encontrada: "+vistaPedidoDTO.getCodigoLocalizacion());
				//Buscar la localizacion
				LocalizacionDTO localizacionDto = new LocalizacionDTO();
				localizacionDto.getId().setCodigoLocalizacion(Long.valueOf(vistaPedidoDTO.getCodigoLocalizacion()));
				localizacionDto.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
				LocalizacionDTO localizacionEncontrada = SISPEFactory.getDataService().findUnique(localizacionDto);
				
				if(localizacionEncontrada !=null){
										
					//consultar el contacto principal de la empresa
					DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
					contactoPersonaLocalizacionDTO.getId().setCodigoCompania(empresaEncontrada.getId().getCodigoCompania());
					contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
					contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
					contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
					contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);

					ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
					contactoRelacionadoDTO.setCodigoLocalizacion(localizacionEncontrada.getId().getCodigoLocalizacion());
					contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
					contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
					
					Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactosRelacionadosCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);					
					
					if(CollectionUtils.isNotEmpty(contactosRelacionadosCol)){
						contactoRelacionadoDTO =  (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactosRelacionadosCol, 0);
						localizacionEncontrada.setContactoPrincipalDTO((contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO()));
					}								
					
					if(localizacionEncontrada.getContactoPrincipalDTO()!=null){
						if(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO()!=null){
							LogSISPE.getLog().info("Contacto de empresa encontrada: "+localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNumeroDocumento()+" - "+localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNombreCompleto());
							//cargar la informacion del contacto							
							vistaPedidoDTO.setCedulaContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNumeroDocumento());
							vistaPedidoDTO.setNombreContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNombreCompleto());
							if(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal()!=null){
								vistaPedidoDTO.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal());
							} else if(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo()!=null){
								vistaPedidoDTO.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo());
							}
							
							String telefonoContacto="TD: ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal()!=null){
								telefonoContacto+=localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal();										
							} else telefonoContacto+=" SN ";									
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo()!=null){
								telefonoContacto+=" - TT: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo();										
							} else telefonoContacto+=" - TT: SN ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular()!=null){
								telefonoContacto+=" - TC: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular();
							}
							else telefonoContacto+=" - TC: SN ";									
							vistaPedidoDTO.setTelefonoContacto(telefonoContacto);
							vistaPedidoDTO.setContactoCliente(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getTipoDocumento()+": "+vistaPedidoDTO.getCedulaContacto()
									+" - NC: "+vistaPedidoDTO.getNombreContacto()+" - "+vistaPedidoDTO.getTelefonoContacto());
						}
					}				
				}
			}
			
		} else {//borrado de variables de sesion cuando se cargan contactos de pedidos anteriores
			if(request.getParameter("pedidosAnteriores") == null && request.getParameter("accionesPedAnt") == null){
				request.getSession().setAttribute(PERSONA, null);
				request.getSession().setAttribute(LOCALIZACION, null);
			}
		}
	}

	/**
	 * Este m\u00E9todo permite verificar si existe o una registro de cliente pedido en SCSPETCLIPED ya sea una persona o una empresa
	 * @author wlopez
	 * @param opcion
	 * @param request
	 * @return
	 */
	public static ClientePedidoDTO consultarClientePedido(String opcion, HttpServletRequest request) {
		
		LogSISPE.getLog().info("metodo consultar Cliente pedido");
		ClientePedidoDTO clientePedidoDtoEncontrado = null;
		
		ClientePedidoDTO clientePedidoDto = new ClientePedidoDTO();
		clientePedidoDto.setEstadoClientePedido(CorporativoConstantes.ESTADO_ACTIVO);
		LogSISPE.getLog().info("Tipo Documento Persona: "+opcion);
		if (opcion.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || opcion.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE )|| (opcion.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)) {
			// Obtener Persona de session
			PersonaDTO personaDto = (PersonaDTO) request.getSession().getAttribute(PERSONA);
			if(personaDto!=null){
				clientePedidoDto.setCodigoPersona(personaDto.getId().getCodigoPersona());
				clientePedidoDtoEncontrado = SISPEFactory.getDataService().findUnique(clientePedidoDto);
				if(clientePedidoDtoEncontrado == null){
					clientePedidoDtoEncontrado = new ClientePedidoDTO();
					clientePedidoDtoEncontrado.setCodigoPersona(personaDto.getId().getCodigoPersona());
				}else{
					LogSISPE.getLog().info("Codigo Cliente pedido: "+clientePedidoDtoEncontrado.getId().getCodigoClientePedido());
				}
			}
			
			
		} else if (opcion.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || opcion.equalsIgnoreCase(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
			// Obtener localizacion de session
			LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO)request.getSession().getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP);
			//busca el cliente pedido
			if (localizacionSeleccionada != null) {
				clientePedidoDto.setCodigoLocalizacion(localizacionSeleccionada.getId().getCodigoLocalizacion());
				clientePedidoDtoEncontrado = SISPEFactory.getDataService().findUnique(clientePedidoDto);
				if(clientePedidoDtoEncontrado==null){
					clientePedidoDtoEncontrado=new ClientePedidoDTO();
					clientePedidoDtoEncontrado.setCodigoLocalizacion(localizacionSeleccionada.getId().getCodigoLocalizacion());
				}
				else{
					LogSISPE.getLog().info("Codigo Cliente pedido: "+clientePedidoDtoEncontrado.getId().getCodigoClientePedido());
				}
			}
			
		}
		return clientePedidoDtoEncontrado;
	}

	/*
	 * **************************************************** PEDIDOS ESPECIALES ***********************************************
	 */
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos especiales
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsContactoPedidoEspecial(HttpServletRequest request, ActionForm formulario) {
		LogSISPE.getLog().info("Construir tabs contacto pedido especial");
		PaginaTab tabsCotizaciones = null;
		try {
			tabsCotizaciones = new PaginaTab("crearPedidoEspecial", "deplegar", 1, 360, request);
			Tab tabPersona=new Tab("Persona", "crearPedidoEspecial", "/contacto/persona.jsp", false);
			
			if(formulario instanceof CrearPedidoForm){
				CrearPedidoForm formularioPedEsp =(CrearPedidoForm) formulario;
				if (null != formularioPedEsp.getTipoDocumento() ) {
					if(formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || 
							formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
							|| (formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)){
						tabPersona = new Tab("Persona", "crearPedidoEspecial", "/contacto/persona.jsp", false);
					}
					else if(formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)||formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
						tabPersona = new Tab("Empresa", "crearPedidoEspecial", "/contacto/persona.jsp", false);	
					}
				}
			}
			else {
				tabPersona = new Tab("Persona", "crearPedidoEspecial", "/contacto/persona.jsp", false);
			}
			Tab tabPedidos = new Tab("Detalle del pedido especial","crearPedidoEspecial","/pedidosEspeciales/creacion/detallePedidoEspecial.jsp",true);
			tabsCotizaciones.addTab(tabPersona);
			tabsCotizaciones.addTab(tabPedidos);		

		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsCotizaciones;
	
	}

	/*
	 * **************************************************** RESUMEN PEDIDOS***********************************************
	 */
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos 
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsResumenPedido(HttpServletRequest request, ActionForm formulario) {
		LogSISPE.getLog().info("Construir tabs contacto resumen pedido");
		PaginaTab tabsCotizaciones = null;
		Map<String, Object> parametrosParaCorp;

		try {
			parametrosParaCorp=new HashMap<String, Object>();
			parametrosParaCorp.put("heightDivDatosGenerales", "170");
			parametrosParaCorp.put("columnSpacesComDatConPer", "385");
			parametrosParaCorp.put("labelSpaceComDatConPer", "125");
			parametrosParaCorp.put("scrollWidthComRelatedContacts", "500");
			parametrosParaCorp.put("scrollHeightComRelatedContacts", "120");			
			
			tabsCotizaciones = new PaginaTab("crearCotizacion", "deplegar", 1,300, request);
			Tab tabPersona=null;
			Tab tabDetalleEntregas=null;
			if(formulario instanceof CotizarReservarForm){
				CotizarReservarForm cotizarReservarForm =(CotizarReservarForm) formulario;
				if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || 
						cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
						|| (cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)){
					tabPersona = new Tab("Persona", "crearCotizacion", "/contacto/persona.jsp", false);
					
				}
				else if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)||cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
					tabPersona = new Tab("Empresa", "crearCotizacion", "/contacto/persona.jsp", false);	
					//camios wc
					request.getSession().setAttribute(TAB_RESUMEN_PEDIDO, "ok");
					
				}
				if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL)!=null && request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).toString().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))){
					tabDetalleEntregas = new Tab("Detalle entregas", "crearCotizacion", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
				}
			}
			else {
				tabPersona = new Tab("Persona", "crearCotizacion", "/contacto/persona.jsp", false);
			}
			Tab tabArticulos = new Tab("Detalle del pedido","crearCotizacion","/servicioCliente/cotizarRecotizarReservar/detalleRegistroPedido.jsp",true);
			tabsCotizaciones.addTab(tabPersona);
			tabsCotizaciones.addTab(tabArticulos);
			if(tabDetalleEntregas!=null){
				tabsCotizaciones.addTab(tabDetalleEntregas);
			}
			
			request.getSession().setAttribute(REPORTE, "ok");
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsCotizaciones;
	}

	/*
	 * **************************************************** RESUMEN PEDIDOS ESPECIALES***********************************************
	 */
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsResumenPedidoEspecial(HttpServletRequest request, ActionForm formulario) {
		LogSISPE.getLog().info("Construir tabs contacto resumen pedido especial");
		// Objetos para construir los tabs
				PaginaTab tabResumenPedidoEspecial = null;
				Map<String, Object> parametrosParaCorp;
				try {
					parametrosParaCorp=new HashMap<String, Object>();
					parametrosParaCorp.put("heightDivDatosGenerales", "160");
					parametrosParaCorp.put("columnSpacesComDatConPer", "370");
					parametrosParaCorp.put("labelSpaceComDatConPer", "100");
					parametrosParaCorp.put("scrollWidthComRelatedContacts", "500");
					parametrosParaCorp.put("scrollHeightComRelatedContacts", "120");	
					
					tabResumenPedidoEspecial = new PaginaTab("crearPedidoEspecial", "deplegar", 1, 280, request);
					Tab tabPersona=new Tab("Persona", "crearPedidoEspecial", "/contacto/persona.jsp", false);;
					if(formulario instanceof CrearPedidoForm){
						CrearPedidoForm formularioPedEsp =(CrearPedidoForm) formulario;
						if(formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || 
								formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
								|| (formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)){
							tabPersona = new Tab("Persona", "crearPedidoEspecial", "/contacto/persona.jsp", false);
							
						}
						else if(formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)||formularioPedEsp.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
							
							tabPersona = new Tab("Empresa", "crearPedidoEspecial", "/contacto/persona.jsp", false);	
							
							//camios wc
							request.getSession().setAttribute(TAB_RESUMEN_PEDIDO, "ok");
							
						}
					}
					Tab tabPedidos = new Tab("Detalle del pedido especial","crearPedidoEspecial","/pedidosEspeciales/creacion/detalleResumenPedido.jsp",true);					
					tabResumenPedidoEspecial.addTab(tabPersona);
					tabResumenPedidoEspecial.addTab(tabPedidos);
					request.getSession().setAttribute(REPORTE, "ok");
				} catch (Exception e) {
					LogSISPE.getLog().error("Error al generar los tabs de Resumen Pedido Especial", e);					
				}
				return tabResumenPedidoEspecial;
	}
	
	/*
	 * **************************************************** INTEGRACION ***********************************************
	 */
	
	
	/*
	 * Parametros para modificar estilos en el corporativo
	 * 
	 * 
	 * heightDivDatosGenerales 			alto del tab DatosGenerales 
	 * columnSpacesComDatConPer			separacion de las columnas en el tab DatosGenerales 		
	 * labelSpaceComDatConPer			pading entre las etiquetas y el contenido tab DatosGenerales
	 * scrollWidthComRelatedContacts    ancho del tab Contactos
	 * scrollHeightComRelatedContacts	alto del tab Contactos
	 * */
	/**
	 * Permite la integraci\u00F3n con el m\u00F3dulo de contacos del Corporativo
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @param session
	 * @return redirect
	 * @throws Exception 
	 */
	public static String integrarCorporativoJSF(HttpServletRequest request, HttpSession session, String evento, ActionForm formulario, UtilPopUp popUp, Map<String, Object> parametrosParaCorporativo ) throws Exception {
		LogSISPE.getLog().info("-- ENTRA EN EL PROCESO DE INTEGRACION CON JSF -- accion: "+evento);	
		
		// se obtiene el valor de la clase de contacto
		String claseContactoSispe="";
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClaseContacto", request);
		claseContactoSispe = parametroDTO.getValorParametro();			
		 		
		// se obtiene el valor de la clase de contacto
		String tipoContactoPrincipalSispe="";
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.cantidadContactosPrincipales", request);		
		
		tipoContactoPrincipalSispe = parametroDTO.getValorParametro();
		tipoContactoPrincipalSispe=tipoContactoPrincipalSispe.replace(",", ";");
		tipoContactoPrincipalSispe=tipoContactoPrincipalSispe.replace("-", ",");
				
		String forward = "desplegar";
		UserDto userDTO = null;
	    Integer idCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
		UserCompanySystemDto userCompanySystem = new UserCompanySystemDto();
		userCompanySystem.getId().setUserId(userDTO.getUserId());
		userCompanySystem.getId().setCompanyId(idCompania);
		userCompanySystem.getId().setSystemId(SystemProvenance.CORPV2.toString());
		
		URLSystemConnection urlSystemConnection = new URLSystemConnection();
		urlSystemConnection.setScriptParameters(new HashMap<String, Object>());
		urlSystemConnection.getScriptParameters().put("evalScripts", Boolean.TRUE);
		
		urlSystemConnection.setViewParameters(parametrosParaCorporativo);
		urlSystemConnection.getViewParameters().put("claseContacto", claseContactoSispe);
		//parametro que define el numero maximo de contactos
		urlSystemConnection.getViewParameters().put("maximumContactsByType", tipoContactoPrincipalSispe);
		//codigo del sistema enviado al corporativo usado para crear contactos  
		urlSystemConnection.getViewParameters().put("codSistema", (SystemProvenance.SISPE).toString());
		
		urlSystemConnection.getViewParameters().put("showActionButtons", "false");
		

		if( evento.endsWith("Persona")){
			try {
				if (formulario instanceof CotizarReservarForm) {
					CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
					urlSystemConnection.setFormActionURL("crearCotizacion.do");
					String cerrarPopUp="parent.requestAjax('"+urlSystemConnection.getFormActionURL()+"', ['divTabs','mensajes','pregunta','div_datosCliente'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait: false, evalScripts: true});parent.ocultarModal();";
					urlSystemConnection.setFormName("cotizarRecotizarReservarForm");
					urlSystemConnection.getViewParameters().put("accion",evento);
					urlSystemConnection.getViewParameters().put("numeroDocumento",formularioPed.getNumeroDocumento());
					urlSystemConnection.getViewParameters().put("tipoDocumento",formularioPed.getTipoDocumento());
					urlSystemConnection.getViewParameters().put("cerrarPopUp", cerrarPopUp);
					urlSystemConnection.getViewParameters().put("SYSID",SystemProvenance.SISPE.name());
				} else if (formulario instanceof CrearPedidoForm) {
					CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
					urlSystemConnection.setFormActionURL("crearPedidoEspecial.do");
					String cerrarPopUp="parent.requestAjax('"+urlSystemConnection.getFormActionURL()+"', ['divTabs','mensajes','pregunta','div_datosCliente'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait: false, evalScripts: true});parent.ocultarModal();";
					urlSystemConnection.setFormName("crearPedidoEspecialForm");		
					urlSystemConnection.getViewParameters().put("accion",evento);
					urlSystemConnection.getViewParameters().put("numeroDocumento", formularioPedEsp.getNumeroDocumento());
					urlSystemConnection.getViewParameters().put("tipoDocumento",formularioPedEsp.getTipoDocumento());
					urlSystemConnection.getViewParameters().put("cerrarPopUp", cerrarPopUp);
					urlSystemConnection.getViewParameters().put("SYSID",SystemProvenance.SISPE.name());
				}
				String urlOpcionSystema="";
				urlSystemConnection.setViewManagedBeanExpression("#{adminPersonaController.responderSistemaExterno()}");
				if(evento.equalsIgnoreCase("registrarPersona")){
					urlOpcionSystema = "/modulos/persona/detalles/nuevaPersonaServicio.jsf";
					session.setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Persona");										
					
				}else if(evento.equalsIgnoreCase("editarPersona")){
					urlOpcionSystema = "/modulos/persona/detalles/editarPersonaServicio.jsf";
					session.setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Persona");
					
				}else if(evento.equalsIgnoreCase("visualizarPersona")){
					urlOpcionSystema = "/modulos/persona/detalles/visualizarPersonaServicio.jsf";										
				}else if(evento.equalsIgnoreCase("buscarPersona")){
					urlOpcionSystema = "/modulos/contactos/buscarPersonaEmpresaServicio.jsf";
					session.setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Busqueda persona empresa");
					urlSystemConnection.setViewManagedBeanExpression("#{adminPersonaEmpresaController.responderSistemaExterno()}");
				}
				
				String urlparaSispe = SystemConnectionSerializer.getInstancia().generateURLSystemConnection(request,userCompanySystem, urlOpcionSystema, urlSystemConnection);
				
				//asigar a la variable de visulizacion
				if(evento.equalsIgnoreCase("visualizarPersona")){
					request.getSession().setAttribute(URL_REDIRECT_VIZUALIZAR_CONTACTOS, urlparaSispe);
				}else{
					//asignar url para mostrar en el PopUp cuando editamos o registramos una nueva persona o empresa
					request.getSession().setAttribute(URL_REDIRECT_CONTACTOS, urlparaSispe);
				}
								
				LogSISPE.getLog().info(SystemConnectionSerializer.getInstancia().decodeString(urlparaSispe));
				LogSISPE.getLog().info(urlparaSispe);
			} catch (Exception e) {
				LogSISPE.getLog().info("ERROR EN REDICCION A JSF:{} ", e);
			}
		}		
		

		else if(evento.endsWith("Empresa") ){					
			try {
				String urlOpcionSystema ="";
				if (formulario instanceof CotizarReservarForm) {
					CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
					urlSystemConnection.setFormActionURL("crearCotizacion.do");
					String cerrarPopUp="parent.requestAjax('"+urlSystemConnection.getFormActionURL()+"', ['mensajes','pregunta','div_datosCliente','divTabs'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait:false, evalScripts:true});parent.ocultarModal();";
					urlSystemConnection.getViewParameters().put("accion",evento);
					urlSystemConnection.setFormName("cotizarRecotizarReservarForm");
					urlSystemConnection.getViewParameters().put(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.parametro.numeroruc"),formularioPed.getNumeroDocumento());
					urlSystemConnection.getViewParameters().put(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.parametro.tipodocumento"),formularioPed.getTipoDocumento());
					urlSystemConnection.getViewParameters().put("cerrarPopUp",cerrarPopUp);
					urlSystemConnection.getViewParameters().put("SYSID",SystemProvenance.SISPE.name());
				} else if (formulario instanceof CrearPedidoForm) {
					CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
					urlSystemConnection.setFormActionURL("crearPedidoEspecial.do");
					String cerrarPopUp="parent.requestAjax('"+urlSystemConnection.getFormActionURL()+"', ['mensajes','pregunta','div_datosCliente','divTabs'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait:false, evalScripts:true});parent.ocultarModal();";
					urlSystemConnection.setFormName("crearPedidoEspecialForm");			
					urlSystemConnection.getViewParameters().put("accion",evento);
					urlSystemConnection.getViewParameters().put(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.parametro.numeroruc"), formularioPedEsp.getNumeroDocumento());
					urlSystemConnection.getViewParameters().put(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.parametro.tipodocumento"),formularioPedEsp.getTipoDocumento());
					urlSystemConnection.getViewParameters().put("cerrarPopUp",cerrarPopUp);
					urlSystemConnection.getViewParameters().put("SYSID",SystemProvenance.SISPE.name());
				}				
				urlSystemConnection.setViewManagedBeanExpression("#{adminEmpresaController.responderSistemaExterno()}");
				if(evento.equalsIgnoreCase("visualizarEmpresa")){
					urlOpcionSystema = "/modulos/empresa/detalles/visualizarEmpresaServicio.jsf";
	
				}else if(evento.equalsIgnoreCase("registrarEmpresa")){
					
					urlOpcionSystema = "/modulos/empresa/detalles/nuevaEmpresaServicio.jsf";
					request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Empresa");
					
				}else if(evento.equalsIgnoreCase("editarEmpresa")){
					
					urlOpcionSystema = "/modulos/empresa/detalles/editarEmpresaServicio.jsf";	
					request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Empresa");					
				}
				else if(evento.equalsIgnoreCase("buscarPersona")){
					urlOpcionSystema = "/modulos/contactos/buscarPersonaEmpresaServicio.jsf";	
					session.setAttribute(SessionManagerSISPE.POPUP, popUp);
					popUp.setTituloVentana("Empresa");	
					urlSystemConnection.setViewManagedBeanExpression("#{adminPersonaEmpresaController.responderSistemaExterno()}");
				}
				//Expresion que se ejecutara en el Corporativo V2
				
				
				String urlparaSispe = SystemConnectionSerializer.getInstancia().generateURLSystemConnection(request,userCompanySystem, urlOpcionSystema,  urlSystemConnection);
				
				//asigar a la variable de visulizacion
				if(evento.equalsIgnoreCase("visualizarEmpresa")){
					request.getSession().setAttribute(URL_REDIRECT_VIZUALIZAR_CONTACTOS, urlparaSispe);
				}
				else{
					//asignar url para mostrar en el PopUp cuando editamos o registramos una nueva persona o empresa
					request.getSession().setAttribute(URL_REDIRECT_CONTACTOS, urlparaSispe);
				}
				
//				request.getSession().setAttribute(URL_REDIRECT_CONTACTOS, urlparaSispe);
				
				LogSISPE.getLog().info(SystemConnectionSerializer.getInstancia().decodeString(urlparaSispe));
				LogSISPE.getLog().info(urlparaSispe);
			} catch (Exception e) {
				LogSISPE.getLog().info("ERROR EN REDICCION A JSF:{} ", e);
			}
		}
		return forward;
	}
	
	/**
	 * Este m\u00E9todo permite mostrar el popup para la integracion con JSF
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("finally")
	public static String mostrarPopUpCorporativo(HttpServletRequest request, HttpSession session, String accion, ActionForm formulario,ActionMessages errors) throws Exception{
		try{
			Map<String, Object> parametrosParaCorp=new HashMap<String, Object>();
			LogSISPE.getLog().info("SE VA A MOSTRAR EL POPUP PERSONA CONTACTOS");						
			
			UtilPopUp popUp = new UtilPopUp();
			popUp.setTope(-120D);
			popUp.setAncho(60D);	
			popUp.setContenidoVentana("contacto/popupCorporativo.jsp");
			popUp.setTituloVentana("Contactos");
			
			if (formulario instanceof CotizarReservarForm) {			
				popUp.setAccionEnvioCerrar("requestAjax('crearCotizacion.do', ['mensajes', 'pregunta','div_datosCliente','divTabs'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait:false, evalScripts:true});ocultarModal();");
			} else if (formulario instanceof CrearPedidoForm) {			
				popUp.setAccionEnvioCerrar("requestAjax('crearPedidoEspecial.do', ['div_pagina','mensajes', 'pregunta','div_datosCliente','divTabs'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait:false, evalScripts:true});ocultarModal();");
			}
			
			if(accion.equals("registrarPersona")){
				parametrosParaCorp.put("heightDivDatosGenerales","400");
				parametrosParaCorp.put("columnSpacesComDatConPer","385");
				parametrosParaCorp.put("labelSpaceComDatConPer","125");
				parametrosParaCorp.put("scrollWidthComRelatedContacts","480");
				parametrosParaCorp.put("scrollHeightComRelatedContacts","205");
				popUp.setTituloVentana("Persona");
			} 
			else if(accion.equals("editarPersona")){
				parametrosParaCorp.put("heightDivDatosGenerales","400");
				parametrosParaCorp.put("columnSpacesComDatConPer","385");
				parametrosParaCorp.put("labelSpaceComDatConPer","125");
				parametrosParaCorp.put("scrollWidthComRelatedContacts","480");
				parametrosParaCorp.put("scrollHeightComRelatedContacts","240");
				popUp.setTituloVentana("Persona");
			}
			else if(accion.equals("visualizarPersona") ){
				parametrosParaCorp=asignarParametrosCorpVisualizarPersona();
				popUp.setTituloVentana("Busqueda persona empresa");
			}
			else if(accion.equals("registrarEmpresa")){
				parametrosParaCorp.put("heightDivDatosGenerales","340");
				parametrosParaCorp.put("columnSpacesComDatConPer","450");
				parametrosParaCorp.put("labelSpaceComDatConPer","140");
				parametrosParaCorp.put("scrollWidthComRelatedContacts","480");
				parametrosParaCorp.put("scrollHeightComRelatedContacts","200");
				parametrosParaCorp.put("validarCodigoJDE","false");
				popUp.setTituloVentana("Empresa");
			}
			else if( accion.equals("editarEmpresa")){
				parametrosParaCorp.put("heightDivDatosGenerales","400");
				parametrosParaCorp.put("columnSpacesComDatConPer","450");
				parametrosParaCorp.put("labelSpaceComDatConPer","140");
				parametrosParaCorp.put("scrollWidthComRelatedContacts","480");
				parametrosParaCorp.put("scrollHeightComRelatedContacts","240");
				parametrosParaCorp.put("validarCodigoJDE","false");
				popUp.setTituloVentana("Empresa");
			}
			else if(accion.equals("visualizarEmpresa") ){
				parametrosParaCorp=asignarParametrosCorpVisualizarEmpresa();
				popUp.setTituloVentana("Empresa");
			}
			else if(accion.equals("buscarPersona")){
				parametrosParaCorp.put("widthDataTable", "655");
			}
			
			session.setAttribute(SessionManagerSISPE.POPUP, popUp);
						
			LogSISPE.getLog().info("Sale por: {}","desplegar");
			
			
		} catch (Exception e) {
			//excepcion desconocida
			LogSISPE.getLog().error("Error al mostrar los contactos del corporativo",e);
			errors.add("",new ActionMessage("errors.gerneral",e.getMessage()));
		}finally{
			return "desplegar";
		}

	}
	
	/**
	 * Este m\u00E9todo crea los tab de Contactos y Pedidos
	 * @author bgudino
	 * @param request
	 * @return Tabs con los datos del contacto(persona o Empresa) y el tab con el detalle de los pesos
	 */
	public static PaginaTab construirTabsContactoPesosFinales(HttpServletRequest request, ActionForm formulario) {
		LogSISPE.getLog().info("Construir tabs contacto resumen pesos finales");
		PaginaTab tabsContactoPesosFinales = null;
		try {
			tabsContactoPesosFinales = new PaginaTab("confirmacionReservacion", "deplegar", 1, 355, request);
			Tab persona=null;
			if(formulario instanceof CotizarReservarForm){
				CotizarReservarForm cotizarReservarForm =(CotizarReservarForm) formulario;
				if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || 
						cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)
						|| (cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) && request.getSession().getAttribute(RUC_PERSONA)!=null)){
					persona = new Tab("Persona", "confirmacionReservacion", "/contacto/persona.jsp", false);
				}
				else if(cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || 
						cotizarReservarForm.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)){
					persona = new Tab("Empresa", "confirmacionReservacion", "/contacto/persona.jsp", false);	
					
					//camios wc
					request.getSession().setAttribute(TAB_RESUMEN_PEDIDO, "ok");
					request.getSession().setAttribute(ContactoUtil.TIPO_DOCUMENTO,cotizarReservarForm.getTipoDocumento());
				}
			}
			else {
				persona = new Tab("Persona", "confirmacionReservacion", "/contacto/persona.jsp", false);
			}
			Tab tabPedidos = new Tab("Detalle del pedido","confirmacionReservacion","/servicioCliente/confirmarReservacion/detallePedidoPesosFinales.jsp",true);
			tabsContactoPesosFinales.addTab(persona);
			tabsContactoPesosFinales.addTab(tabPedidos);
			
			request.getSession().setAttribute(REPORTE, "ok");
			request.getSession().setAttribute(ContactoUtil.ACCION, "confirmarReservacion.do");

		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsContactoPesosFinales;
	}
	
	/**
	 * Este m\u00E9todo selecciona el tab de Contacto o DetalledelPEdido de acuerto a la posicionTab
	 * tab(0) Datos del contacto
	 * tab(1) datos del pedido
	 * @author bgudino
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabContactoPedidos(BeanSession beanSession, int posicionTab) {
		try{
			if(posicionTab < beanSession.getPaginaTab().getCantidadTabs()){
				//se deseleccionan todos los tabs
				for(int i=0; i<beanSession.getPaginaTab().getCantidadTabs(); i++){
					beanSession.getPaginaTab().getTab(i).setSeleccionado(false);
				}
				beanSession.getPaginaTab().getTab(posicionTab).setSeleccionado(true);
				beanSession.getPaginaTab().setTabSeleccionado(posicionTab);
				beanSession.getPaginaTab().setTituloTabSeleccionado(beanSession.getPaginaTab().getTab(posicionTab).getTitulo());
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al cambiar de tab", e);
		}
	}
	
	
	/**
	 * Asigna los tama\u00F1os de los componentes para el corporativo (VisualizarPersona)
	 * @return
	 */
	public static Map<String, Object> asignarParametrosCorpVisualizarPersona(){
		Map<String, Object> parametrosParaCorp=new HashMap<String, Object>();
		try{		
			//se asignas los tama\u00F1os del componente del corporativo
			parametrosParaCorp.put("heightDivDatosGenerales", "240");
			parametrosParaCorp.put("columnSpacesComDatConPer", "385");
			parametrosParaCorp.put("labelSpaceComDatConPer", "125");
			parametrosParaCorp.put("scrollWidthComRelatedContacts", "300");			
			parametrosParaCorp.put("scrollHeightComRelatedContacts", "180");
		}
		catch (Exception e) {
			LogSISPE.getLog().info("No se pudo asignar los parametros para el corporativo");
		}
		return parametrosParaCorp;		
	}
	
	/**
	 * Asigna los tama\u00F1os de los componentes para el corporativo (VisualizarEmpresa)
	 * @return
	 */
	public static Map<String, Object> asignarParametrosCorpVisualizarEmpresa(){
		Map<String, Object> parametrosParaCorp=new HashMap<String, Object>();
		try{			
			//se asignas los tama\u00F1os del componente del corporativo
			parametrosParaCorp.put("heightDivDatosGenerales", "230");
			parametrosParaCorp.put("columnSpacesComDatConPer", "385");
			parametrosParaCorp.put("labelSpaceComDatConPer", "125");			
			parametrosParaCorp.put("scrollWidthComRelatedContacts", "350");
			parametrosParaCorp.put("scrollHeightComRelatedContacts", "175");
		}
		catch (Exception e) {
			LogSISPE.getLog().info("No se pudo asignar los parametros para el corporativo");
		}
		return parametrosParaCorp;		
	}

	
	/**Carga el token de corporativo
	 * @param request
	 * @throws SISPEException
	 */
	public static void getTokenCorp(HttpServletRequest request) throws SISPEException {
		
		
		try {
				LogSISPE.getLog().info("Obteniendo token de corporativo");
				
				String token = ec.com.smx.framework.factory.FrameworkFactory.getSecurityService().getTokenForUser(
							SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId(),
							SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania(),
							SystemProvenance.CORPV2.toString());
				
				cargarSesssionDataManagerBase(token, request);
				
		} catch (FrameworkException e) {
			LogSISPE.getLog().error("{}", e);
			throw new SISPEException("Error al obtener token corporativo V2");
			
		} catch (Exception e) {
			LogSISPE.getLog().error("{}", e);
			throw new SISPEException("Error al obtener token corporativo V2");
		}
		
	}

	
	/**Selea la persona o empresa seleccionada desde el componente de busqueda
	 * @param request
	 * @param response
	 * @param formulario
	 * @param beanSession
	 * @param infos
	 * @param errors
	 * @param warnings
	 * @throws SISPEException
	 */
	public static boolean setearValoresPersonaEmpresaCompBusqueda(HttpServletRequest request, HttpServletResponse response, ActionForm formulario, BeanSession beanSession, ActionMessages infos, ActionMessages errors,ActionMessages warnings) throws SISPEException {
		LogSISPE.getLog().info("Cargando datos persona empresa desde busqueda corporativo");
		HttpSession session = request.getSession();
		Object object = request.getSession().getAttribute(PERSONA_EMPRESA_SELECCIONADA);
		if (object instanceof PersonaDTO) {
			if(validarDatosPersona(object, errors, warnings)){
				LogSISPE.getLog().info("Persona: "+((PersonaDTO) object).getNumeroDocumento()+" - "+((PersonaDTO) object).getNombreCompleto());
				cargarComponentePersona(request, response, formulario, (PersonaDTO) object);
				if (formulario instanceof CotizarReservarForm) {
					CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
					session.setAttribute(NUMERO_DOCUMENTO_STRUTS, formularioPed.getNumeroDocumento());
				}  else if (formulario instanceof CrearPedidoForm) {
					CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
					session.setAttribute(NUMERO_DOCUMENTO_STRUTS, formularioPedEsp.getNumeroDocumento());
				}
			}else{
				return false;
			}
			
		} else if (object instanceof LocalizacionDTO) {
			if(validarDatosLocalizacion(object, errors, warnings)){
				LogSISPE.getLog().info("Empresa: "+((LocalizacionDTO) object).getEmpresaDTO().getNumeroRuc()+" - "+((LocalizacionDTO) object).getEmpresaDTO().getNombreComercialEmpresa());
				session.setAttribute(NUMERO_DOCUMENTO_STRUTS, ((LocalizacionDTO) object).getEmpresaDTO().getNumeroRuc());
				LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO) object;
				
				if (localizacionSeleccionada.getEmpresaDTO() != null) {
					cargarComponenteEmpresa(request,response, formulario, localizacionSeleccionada.getEmpresaDTO());
					if (formulario instanceof CotizarReservarForm) {
						CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
						session.setAttribute(NUMERO_DOCUMENTO_STRUTS, formularioPed.getNumeroDocumento());
					}  else if (formulario instanceof CrearPedidoForm) {
						CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
						session.setAttribute(NUMERO_DOCUMENTO_STRUTS, formularioPedEsp.getNumeroDocumento());
					}
				}
				
				setearLocalizacionSeleccionada(request, response, formulario, infos, warnings, errors, localizacionSeleccionada);
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**Sube a sesion la persona y setea valores en el formulario
	 * @param request
	 * @param response
	 * @param formulario
	 * @param personaDTO
	 * @throws SISPEException
	 */
	public static void cargarComponentePersona(HttpServletRequest request, HttpServletResponse response, ActionForm formulario, PersonaDTO personaDTO) throws SISPEException {
		HttpSession session= request.getSession();
		LogSISPE.getLog().info("Seteando en sesion la persona");
		session.setAttribute(PERSONA, personaDTO);
		session.setAttribute(TIPO_DOCUMENTO, personaDTO.getTipoDocumento());
		session.removeAttribute(LOCALIZACION);
		session.setAttribute(URL_OPCIONES, "jsf/contacto/opcionesPersona.jsf");
		session.setAttribute(URL_REDIRECT_CONTACTOS, "jsf/contacto/adminContacto.jsf");
		setearValoresPersonaEncontradaEnFormulario(request, formulario, personaDTO);
	}
	
	
	/**Sube a sesion la empresa y setea valores en el formulario
	 * @param request
	 * @param response
	 * @param formulario
	 * @param empresaDTO
	 * @throws SISPEException
	 */
	public static void cargarComponenteEmpresa(HttpServletRequest request, HttpServletResponse response, ActionForm formulario, 
			EmpresaDTO empresaDTO) throws SISPEException {
		LogSISPE.getLog().info("Seteando en sesion la empresa");
		HttpSession session= request.getSession();
		
		session.removeAttribute(PERSONA);
		session.setAttribute(LOCALIZACION, empresaDTO);
		session.setAttribute(TIPO_DOCUMENTO, empresaDTO.getValorTipoDocumento());
		session.setAttribute(URL_OPCIONES, "jsf/contacto/opcionesEmpresa.jsf");
		session.setAttribute(URL_REDIRECT_CONTACTOS, "jsf/contacto/adminEmpresa.jsf");
		setearValoresEmpresaEncontradaEnFormulario(request, formulario, empresaDTO);
	}
	
	
	/**Setea datos para el componente de busqueda
	 * @param request
	 * @throws SISPEException
	 */
	public static void cargarComponenteBusqueda(HttpServletRequest request) throws SISPEException {
		HttpSession session= request.getSession();
		session.setAttribute(URL_OPCIONES, "jsf/contacto/opcionesBusqueda.jsf");
		session.setAttribute(URL_REDIRECT_CONTACTOS, "jsf/contacto/adminBusqueda.jsf");
		session.setAttribute(FLAG_BUSCAR_PERSONA_EMPRESA, "true");
	}
	
	/**
	 * Asigna los datos de la localizacion seleccionada al formulario
	 * @param localizacionEncontrada
	 * @param formulario
	 */
	public static Boolean asignarDatosLocalizacion(LocalizacionDTO localizacionEncontrada, ActionForm formulario){
		LogSISPE.getLog().info("Asignar datos de la localizacion");
		
		Boolean contactoAsignado = Boolean.FALSE;
		
		//consultar el contacto principal de la empresa
		DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
		contactoPersonaLocalizacionDTO.getId().setCodigoCompania(localizacionEncontrada.getId().getCodigoCompania());
		contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
		contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
		contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
		contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		
		ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
		contactoRelacionadoDTO.setCodigoLocalizacion(localizacionEncontrada.getId().getCodigoLocalizacion());
		contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
		contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		
		Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactosRelacionadosCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);
		
		if(CollectionUtils.isNotEmpty(contactosRelacionadosCol)){
			contactoRelacionadoDTO =  (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactosRelacionadosCol, 0);
			localizacionEncontrada.setContactoPrincipalDTO((contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO()));
		
			if (formulario instanceof CotizarReservarForm) {
				
				CotizarReservarForm formularioPed = (CotizarReservarForm) formulario;
				
				if(localizacionEncontrada.getContactoPrincipalDTO()!=null){
					
					if(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO()!=null){
						//cargar la informacion del contacto
						formularioPed.setNombreContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNombreCompleto());
						formularioPed.setTipoDocumentoContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getTipoDocumento());
						formularioPed.setNumeroDocumentoContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNumeroDocumento());
						
						if(localizacionEncontrada.getContactoPrincipalDTO()!= null ){
							String telefonoContacto="TD: ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal()!=null){
								telefonoContacto+=localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal();
							} else telefonoContacto+=" SN ";	
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo()!=null){
								telefonoContacto+=" - TT: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo();
							} else telefonoContacto+=" - TT: SN ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular()!=null){
								telefonoContacto+=" - TC: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular();
							}
							else telefonoContacto+=" - TC: SN ";
							formularioPed.setTelefonoContacto(telefonoContacto);
	
							//Se asigna el email del contacto al formulario
							if(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal()!=null){
								formularioPed.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal());
							}
							else if(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo()!=null){
								formularioPed.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo());
							}	
//							if(formularioPed.getEmailContacto()!=null){
//								formularioPed.setEmailEnviarCotizacion(formularioPed.getEmailContacto());
//							}
						}
					}
					contactoAsignado = Boolean.TRUE;
				}
			}else if (formulario instanceof CrearPedidoForm) {
				CrearPedidoForm formularioPedEsp = (CrearPedidoForm) formulario;
				if(localizacionEncontrada.getContactoPrincipalDTO()!=null){
					if(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO()!=null){
						//cargar la informacion del contacto
						formularioPedEsp.setNombreContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNombreCompleto());
						formularioPedEsp.setTipoDocumentoContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getTipoDocumento());
						formularioPedEsp.setNumeroDocumentoContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNumeroDocumento());
						if(localizacionEncontrada.getContactoPrincipalDTO()!= null ){
							String telefonoContacto="TD: ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal()!=null){
								telefonoContacto+=localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal();
							} else 	telefonoContacto+=" SN ";	
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo()!=null){
								telefonoContacto+=" - TT: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo();
							} else telefonoContacto+=" - TT: SN ";
							if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular()!=null){
								telefonoContacto+=" - TC: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular();
							} else 	telefonoContacto+=" - TC: SN ";
							formularioPedEsp.setTelefonoContacto(telefonoContacto);
							//Se asigna el email del contacto al formulario
							if(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal()!=null){
								formularioPedEsp.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailPrincipal());
							}
							else if(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo()!=null){
								formularioPedEsp.setEmailContacto(localizacionEncontrada.getContactoPrincipalDTO().getEmailTrabajo());
							}
						}else {
							formularioPedEsp.setTelefonoContacto("TD: SN - TT: SN - TC: SN");
						}
						contactoAsignado = Boolean.TRUE;
					}
				}
			}
		}
		
		return contactoAsignado;
	}
	
	public static void cargarComponenteLocalizaciones(HttpServletRequest request) throws SISPEException {
		
		HttpSession session= request.getSession();
		session.setAttribute(URL_REDIRECT_CONTACTOS, "jsf/contacto/adminLocalizacion.jsf");
	}
	
	public static void borrarAtributosSessionCorp(HttpServletRequest request) {
		LogSISPE.getLog().info("Borrar atributos de sesion secundarios de contactos");
		HttpSession session= request.getSession();
		session.removeAttribute(TAB_RESUMEN_PEDIDO);
		session.removeAttribute(TIPO_DOCUMENTO);
		session.removeAttribute(PERSONA_EMPRESA_SELECCIONADA);
		session.removeAttribute(FLAG_BUSCAR_PERSONA_EMPRESA);
		session.removeAttribute(NUMERO_DOCUMENTO_STRUTS);
	}
	/**
	 * Ejecutar accion que se encuentra en un controlador
	 * @param request
	 * @param response
	 * @param metodo
	 * @throws SISPEException
	 * @author osaransig
	 */
	public static void ejecutarAccionControlador(HttpServletRequest request, HttpServletResponse response, String metodo) throws SISPEException {
		try{
		FacesContext fc = FacesUtil.getDefaultInstance().getFacesContext(request, response); 
		fc.getApplication().evaluateExpressionGet(fc, metodo, Object.class);
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al ejecutar la accion {} del controlador ",metodo,e);
		}
	}
	
	
	/**
	 * Carga los datos de la persona/empresa en el componente de contactos
	 * @param formulario
	 * @param request
	 * @param response
	 * @param accion
	 * @param infos
	 * @param errors
	 * @param warnings
	 * @param errores
	 * @throws Exception
	 */
	public static void mostrarDatosVisualizarPersonaEmpresa(ActionForm form, HttpServletRequest request, HttpServletResponse response, ActionMessages infos, ActionMessages errors, ActionMessages warnings, ActionErrors errores) throws Exception{
		
		LogSISPE.getLog().info("mostrando datos de visualizacion de persona/empresa");
		HttpSession session = request.getSession();
		//se obtienen las claves que indican un estado activo y un estado inactivo
		if(form instanceof CotizarReservarForm){
			CotizarReservarForm formulario=(CotizarReservarForm) form;
			if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())){
				//se cargan los datos del cliente
				ContactoUtil.borrarAtributosSessionCorp(request);
				ContactoUtil.obtenerDatosPersonaEmpresa(request, response, formulario, infos,warnings,errors);
				
				session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
				session.setAttribute(ContactoUtil.ACCION, "crearCotizacion.do");
			}
		}else if(form instanceof CrearPedidoForm){
			CrearPedidoForm formulario=(CrearPedidoForm) form;
			if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())){
				//se cargan los datos del cliente
				ContactoUtil.borrarAtributosSessionCorp(request);
				ContactoUtil.obtenerDatosPersonaEmpresa(request, response, formulario, infos,warnings,errors);
				
				session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
				session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");
			}
		}
	}
	
	/**Busqueda de cliente al dar enter en el cuadro de texto de numero documento
	 * @param form
	 * @param request
	 * @param session
	 * @param errores
	 * @param errors
	 * @param accion
	 * @param estadoActivo
	 * @param estadoInactivo
	 * @param response
	 * @param infos
	 * @param warnings
	 * @param beanSession
	 * @throws Exception
	 */
	public static String consultarCliente(ActionForm form,HttpServletRequest request, HttpSession session, ActionErrors errores,ActionMessages errors, String accion, String estadoActivo, String estadoInactivo,
			HttpServletResponse response, ActionMessages infos, ActionMessages warnings, BeanSession beanSession,String salidaParam) throws Exception{
		String salida = salidaParam;
		//se cargan los datos del cliente
		try{
			boolean existePersonaEmpresa=false;
			ContactoUtil.borrarAtributosSessionCorp(request);
			session.removeAttribute(ContactoUtil.PERSONA);
			session.removeAttribute(ContactoUtil.LOCALIZACION);
			session.removeAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP);
			session.removeAttribute(ContactoUtil.LOC_GUARDADA);
			session.removeAttribute(ContactoUtil.RUC_PERSONA);
			if (form instanceof CotizarReservarForm) {
				CotizarReservarForm formulario = (CotizarReservarForm) form;
				existePersonaEmpresa=obtenerDatosPersonaEmpresa(request, response, formulario, infos,warnings,errors);
				if(existePersonaEmpresa){
					session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
					session.setAttribute(ContactoUtil.ACCION, "crearCotizacion.do");
					//solo para el caso de empresa
					if(formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
						//se actualiza el detalle para verificar si es una empresa que no paga IVA
						Collection detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
						formulario.actualizarDetalleForm(request, errores, accion, estadoActivo, estadoInactivo,detallePedido);						
						CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
					}
					
					PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request,formulario);
					beanSession.setPaginaTab(tabsCotizaciones);
				}
	
			} else if (form instanceof CrearPedidoForm) {
				CrearPedidoForm formulario = (CrearPedidoForm) form;
				session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
				session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");
				existePersonaEmpresa=obtenerDatosPersonaEmpresa(request, response, formulario, infos,warnings,errors);
				if(existePersonaEmpresa){
					PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedidoEspecial(request,formulario);
					beanSession.setPaginaTab(tabsCotizaciones);
				}
			}
			if(existePersonaEmpresa){
				//ejecutar el metodo para inicializar el controlador adecuado
				ContactoUtil.ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
			
//				if(session.getAttribute(PERSONA)!=null && ((PersonaDTO)session.getAttribute(PERSONA)).getNumeroRuc()==null && session.getAttribute(RUC_PERSONA)!=null){
				if(session.getAttribute(RUC_PERSONA)!=null && session.getAttribute(RUC_PERSONA).equals("ok")){
					setRucPersona(true);
					if (form instanceof CotizarReservarForm) {
						CotizarReservarForm formularioPed = (CotizarReservarForm) form;
						((PersonaDTO)session.getAttribute(PERSONA)).setNumeroRuc(formularioPed.getNumeroDocumento());
						((PersonaDTO)session.getAttribute(PERSONA)).setNombreComercial(formularioPed.getNombrePersona());
					}  else if (form instanceof CrearPedidoForm) {
						CrearPedidoForm formularioPedEsp = (CrearPedidoForm) form;
						((PersonaDTO)session.getAttribute(PERSONA)).setNumeroRuc(formularioPedEsp.getNumeroDocumento());
						((PersonaDTO)session.getAttribute(PERSONA)).setNombreComercial(formularioPedEsp.getNombrePersona());
					}
					salida=editarPersona(form, request, request.getSession(), response, errors);
				}
			}
			
		}catch (Exception e) {
			LogSISPE.getLog().error("Error al consultar el Cliente", e);
			errors.add("errorConsultarCliente",new ActionMessage("errors.gerneral",e.getMessage()));
		}
		return salida;
	}
	
	
	/**Coloca la persona o empresa escogida en la busqueda
	 * @param form
	 * @param request
	 * @param session
	 * @param errors
	 * @param response
	 * @param infos
	 * @param warnings
	 * @param beanSession
	 * @throws Exception
	 */
	public static String  perEmpBusqueda(ActionForm form,HttpServletRequest request, HttpSession session, ActionMessages errors, HttpServletResponse response, ActionMessages infos,ActionMessages warnings, BeanSession beanSession,String salidaParam) throws Exception{
		String salida=salidaParam;
		try{
			LogSISPE.getLog().info("Persona localizacion seleccionada desde componente de busqueda");
			session.removeAttribute(ContactoUtil.PERSONA);
			session.removeAttribute(ContactoUtil.LOCALIZACION);
			session.removeAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS);
			session.removeAttribute(SessionManagerSISPE.POPUP);
			session.removeAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP);
			if(setearValoresPersonaEmpresaCompBusqueda(request, response, form, beanSession, infos, errors,warnings)){
				//ejecutar el metodo para inicializar el controlador adecuado
				if(form instanceof CotizarReservarForm){
					PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedido(request,form);
					beanSession.setPaginaTab(tabsCotizaciones);
				} else{
					PaginaTab tabsCotizaciones = ContactoUtil.construirTabsContactoPedidoEspecial(request,form);
					beanSession.setPaginaTab(tabsCotizaciones);
				}
				ejecutarAccionControlador(request, response, "#{busquedaController.buscar()}");
//				if(session.getAttribute(PERSONA)!=null && ((PersonaDTO)session.getAttribute(PERSONA)).getNumeroRuc()==null && session.getAttribute(RUC_PERSONA)!=null){
				if(session.getAttribute(RUC_PERSONA)!=null && session.getAttribute(RUC_PERSONA).equals("ok")){
					setRucPersona(true);
					if (form instanceof CotizarReservarForm) {
						CotizarReservarForm formularioPed = (CotizarReservarForm) form;
						((PersonaDTO)session.getAttribute(PERSONA)).setNumeroRuc(formularioPed.getNumeroDocumento());
						((PersonaDTO)session.getAttribute(PERSONA)).setNombreComercial(formularioPed.getNombrePersona());
					}  else if (form instanceof CrearPedidoForm) {
						CrearPedidoForm formularioPedEsp = (CrearPedidoForm) form;
						((PersonaDTO)session.getAttribute(PERSONA)).setNumeroRuc(formularioPedEsp.getNumeroDocumento());
						((PersonaDTO)session.getAttribute(PERSONA)).setNombreComercial(formularioPedEsp.getNombrePersona());
					}
					salida=editarPersona(form, request, request.getSession(), response, errors);
				}
			}
		} catch (Exception e) {
			//excepcion desconocida
			LogSISPE.getLog().error("Error al seleccionar una persona/empresa desde la busqueda de contactos",e);
			errors.add("",new ActionMessage("errors.gerneral",e.getMessage()));
		}
		return salida;
	}
	
	/**Carga la pantalla de busqueda de contactos
	 * @param form
	 * @param request
	 * @param session
	 * @param response
	 * @param errors
	 * @throws Exception
	 */
	public static void buscarPersonaEmpresa(ActionForm form,HttpServletRequest request, HttpSession session,HttpServletResponse response,ActionMessages errors) throws Exception{
		try {
			LogSISPE.getLog().info("Va a abrir pop up de busqueda corporativo");
			session.removeAttribute(ContactoUtil.RUC_PERSONA);
			String accionCorporativo =  "buscarPersona";
			
			Boolean eliminarTipoDocumento = Boolean.TRUE;
			Validator validador = new ValidatorImpl();
			
			if (form instanceof CotizarReservarForm) {
				CotizarReservarForm formulario = (CotizarReservarForm) form;
				LogSISPE.getLog().info("Se procede a llamar a m\u00E9todo buscarPersona  o empresa: " + formulario.getNumeroDocumento());								
				session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
				session.setAttribute(ContactoUtil.ACCION, "crearCotizacion.do");
				
				//si el numero ingresado es un RUC no natural, no se elimina de sesion el tipoDocumento
				//para que el componente corporativo busque primero la empresa
				if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())
						&& validador.validateRUC(formulario.getNumeroDocumento()) 
						&& !validador.validateTipoRUC(formulario.getNumeroDocumento()).equals(TipoEmpresaEnum.NATURAL)){
					
					session.setAttribute(ContactoUtil.TIPO_DOCUMENTO, TipoDocumentoEmpresaEnum.RUC.toString());
					eliminarTipoDocumento = Boolean.FALSE;
				}
				
			} else if (form instanceof CrearPedidoForm) {
				CrearPedidoForm formulario = (CrearPedidoForm) form;
				LogSISPE.getLog().info("Se procede a llamar a m\u00E9todo buscarPersona  o empresa: " + formulario.getNumeroDocumento());	
				LogSISPE.getLog().info("Pedidos Especiales");
				session.setAttribute(ContactoUtil.NUMERO_DOCUMENTO_STRUTS, formulario.getNumeroDocumento());//cambios oscar
				session.setAttribute(ContactoUtil.ACCION, "crearPedidoEspecial.do");
				
				//si el numero ingresado es un RUC no natural, no se elimina de sesion el tipoDocumento
				//para que el componente corporativo busque primero la empresa
				if(StringUtils.isNotEmpty(formulario.getNumeroDocumento())
						&& validador.validateRUC(formulario.getNumeroDocumento()) 
						&& !validador.validateTipoRUC(formulario.getNumeroDocumento()).equals(TipoEmpresaEnum.NATURAL)){
					
					session.setAttribute(ContactoUtil.TIPO_DOCUMENTO, TipoDocumentoEmpresaEnum.RUC.toString());
					eliminarTipoDocumento = Boolean.FALSE;
				}
			}
			
			if(eliminarTipoDocumento){
				session.removeAttribute(ContactoUtil.TIPO_DOCUMENTO);
			}
			ContactoUtil.cargarComponenteBusqueda(request);//cambio oscar
			ContactoUtil.ejecutarAccionControlador(request, response, "#{personaEmpresaController.cargarBuscar()}");
			ContactoUtil.mostrarPopUpCorporativo(request, session, accionCorporativo, form,errors);
			
		} catch (Exception e) {
			//excepcion desconocida
			LogSISPE.getLog().error("Error al abrir la busqueda de contactos",e);
			errors.add("",new ActionMessage("errors.gerneral",e.getMessage()));
		}

	}
	
	
	/**Editar persona desde pesta\u00F1as de pedido
	 * @param form
	 * @param request
	 * @param session
	 * @param response
	 * @param salida
	 * @param errors
	 * @throws Exception
	 */
	public static String editarPersona(ActionForm form,HttpServletRequest request, HttpSession session,HttpServletResponse response,ActionMessages errors) throws Exception{
		LogSISPE.getLog().info("MUESTRA EL POPUP PARA EDITAR PERSONA del SISTEMA CORPORATIVO");
    	ContactoUtil.ejecutarAccionControlador(request, response, "#{personaController.editarPersona()}");
    	session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminContacto.jsf");//cambios oscar
		return ContactoUtil.mostrarPopUpCorporativo(request, session, "editarPersona", form,errors);
	}
	
	
	/**Editar empresa desde pesta\u00F1as del pedido
	 * @param response
	 * @param request
	 * @param session
	 * @param salida
	 * @param form
	 * @param errors
	 * @throws Exception
	 */
	public static String editarEmpresa(HttpServletResponse response, HttpServletRequest request, HttpSession session, String salidaParam, ActionForm form,ActionMessages errors) throws Exception{
		String salida=salidaParam;
		LogSISPE.getLog().info("MUESTRA EL POPUP PARA EDITAR EMPRESA del SISTEMA CORPORATIVO");
		ContactoUtil.ejecutarAccionControlador(request, response, "#{empresaController.editarEmpresa()}");
		session.setAttribute(ContactoUtil.URL_REDIRECT_CONTACTOS, "jsf/contacto/adminEmpresa.jsf");
		salida = ContactoUtil.mostrarPopUpCorporativo(request, session, "editarEmpresa", form,errors);
		return salida;
	}
	
	
	/**Quita de sesion los manager y otras variables relacionadas
	 * @param request
	 */
	public static void eliminarDataManagersSessionCorp(HttpServletRequest request){
		LogSISPE.getLog().info("Limpia variables de sesion de contactos");
		request.getSession().removeAttribute("personaDataManager");
		request.getSession().removeAttribute("empresaDataManager");
		request.getSession().removeAttribute("personaEmpresaDataManager");
		request.getSession().removeAttribute(ERROR_BUSQUEDA);
		request.getSession().removeAttribute(LOC_GUARDADA);
		request.getSession().removeAttribute(LOCALIZACION_SELEC_COM_EMP);
		request.getSession().removeAttribute(URL_REDIRECT_CONTACTOS);
		request.getSession().removeAttribute(URL_REDIRECT_VIZUALIZAR_CONTACTOS);
		request.getSession().removeAttribute(REPORTE);
		request.getSession().removeAttribute(PERSONA);
		request.getSession().removeAttribute(LOCALIZACION);
		borrarAtributosSessionCorp(request);
	}
	
	
	/**Carga los datos del manager en sesion
	 * @param token
	 * @param request
	 */
	public static void cargarSesssionDataManagerBase(String token, HttpServletRequest request) {
		
		CorporativoSessionControllerBase controllerBase = new CorporativoSessionControllerBase();
		controllerBase.setCorporativoSessionDataManagerBase(new CorporativoSessionDataManagerBase());
		controllerBase.loginUsingToken(token, "", false);
		
		
		SessionDataManagerBase sessionDataManagerBase = controllerBase.getCorporativoSessionDataManagerBase();
		request.getSession().setAttribute("sessionDataManagerBase", sessionDataManagerBase);
	}
	
	
	/**Si existe una localizacion guardada la setea
	 * @param request
	 * @param response
	 * @param formulario
	 * @param infos
	 * @param warnings
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public static boolean setearLocalizacionGuardada(HttpServletRequest request, HttpServletResponse response, 
			ActionForm formulario, ActionMessages infos, ActionMessages warnings	,ActionMessages errors) throws Exception{
		//si no esta vacia la variable de sesion que contiene el codigo localizacion 
		if (request.getSession().getAttribute(LOC_GUARDADA) != null) {
			LogSISPE.getLog().info("Existe localizacion guardada: "+(String) request.getSession().getAttribute(LOC_GUARDADA));
			LocalizacionDTO localizacion = new LocalizacionDTO();
			localizacion.setId(new LocalizacionID());
			localizacion.getId().setCodigoLocalizacion(Long.valueOf((String) request.getSession().getAttribute(LOC_GUARDADA)));
			localizacion.setTipoLocalizacionDTO(new CatalogoValorDTO());
			localizacion.getTipoLocalizacionDTO().getId().setCodigoCatalogoValor(localizacion.getTipoLocalizacion());
			localizacion.setEstadoLocalizacionDTO(new CatalogoValorDTO());
			localizacion.getEstadoLocalizacionDTO().getId().setCodigoCatalogoValor(localizacion.getEstadoLocalizacion());
			localizacion.setUsuarioRegistroDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
			localizacion.setUsuarioModificacionDTO(new ec.com.smx.frameworkv2.security.dto.UserDto());
			localizacion = SISPEFactory.getDataService().findUnique(localizacion);
			
			if (localizacion != null) {
				setearLocalizacionSeleccionada(request, response, formulario, infos, warnings, errors,
						localizacion);
			}
			request.getSession().removeAttribute(LOC_GUARDADA);
			return true;
		}else{
			return false;
		}
	}
	
	public static void limpiarContacto(ActionForm form,
			HttpSession session) {
		if (form instanceof CotizarReservarForm) {
			CotizarReservarForm formulario= (CotizarReservarForm) form;
			formulario.setTipoDocumento("");
			formulario.setNombrePersona("");
			formulario.setNombreEmpresa("");
		} else if (form instanceof CrearPedidoForm) {
			CrearPedidoForm formulario= (CrearPedidoForm) form;
			formulario.setTipoDocumento("");
			formulario.setNombrePersona("");
			formulario.setNombreEmpresa("");
		}
		
		session.removeAttribute(ContactoUtil.PERSONA);
		session.removeAttribute(ContactoUtil.LOCALIZACION);
	}

	public static boolean isRucPersona() {
		return rucPersona;
	}

	public static void setRucPersona(boolean rucPersona) {
		ContactoUtil.rucPersona = rucPersona;
	}

}




