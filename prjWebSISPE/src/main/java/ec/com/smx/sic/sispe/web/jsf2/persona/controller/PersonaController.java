package ec.com.smx.sic.sispe.web.jsf2.persona.controller;

import static ec.com.smx.corpv2.web.util.common.resources.CorpCommonWebResources.getString;
import static ec.com.smx.sic.sispe.common.util.ContactoUtil.HEIGHT_DIV_DATOSGENERALES;
import static ec.com.smx.sic.sispe.common.util.ContactoUtil.SCROLL_HEIGHT_COMRELATEDCONTACTS;
import static ec.com.smx.sic.sispe.commons.util.LogSISPE.getLog;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.corpv2.web.util.common.jsf.Person;
import ec.com.smx.corpv2.web.util.common.resources.CorpCommonWebResources;
import ec.com.smx.framework.jsf.commons.beans.controller.CommonController;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.util.FacesUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.web.jsf2.persona.dataManager.PersonaDataManager;

/**
 * @author osaransig
 *	2013-02-21
 *	
 */
@SuppressWarnings("serial")
@ManagedBean(name = "personaController")
@ViewScoped
public class PersonaController extends CommonController implements Serializable {
	
	@ManagedProperty(value = "#{personaDataManager}")
	private PersonaDataManager personaDataManager;
	
	@Override
	public void initialize() {
		/**
		 * inicializando componente de personas
		 */
		if (personaDataManager.getSessionDataManagerBase().getCompanyDto() != null) {
			//inicializa el componente persona si no ha sido inicializado antes
			if (personaDataManager.getPersonComponent()==null) {
				personaDataManager.setPersonComponent(new Person(personaDataManager.getSessionDataManagerBase().getCompanyDto().getCompanyId(), 
						SystemProvenance.SISPE, personaDataManager.getSessionDataManagerBase().getUserDto().getUserId()));
			}
			
			if (personaDataManager.getPersonComponent().getPersonaVO().getEvento() == null ||
					!personaDataManager.getPersonComponent().getPersonaVO().getEvento().equals(getString("evento.actualizar"))) {
				visualizarPersona();
			}
			
		}
	}
	
	//Carga los datos de sesion en el componente y establece las funciones
	public void visualizarPersona() {
		
		getLog().info("Visualizar persona");
  		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		if (session.getAttribute(ContactoUtil.PERSONA) != null) {
			//se cargan los datos de contacto si la persona es diferente a la actualmente seleccionada, se coloca en null los datoscontacto personal para que los busque
//			if(personaDataManager.getPersonComponent().getDatosContactoPersonal()!=null && !(personaDataManager.getPersonComponent().getDatosContactoPersonal().getDatoContactoPersonaLocalizacionDTO().getPersonaDTO().getId().getCodigoPersona().equals(
//					((PersonaDTO)session.getAttribute(ContactoUtil.PERSONA)).getId().getCodigoPersona()))){
				personaDataManager.getPersonComponent().setDatosContactoPersonal(null);
//			}
			//carga el dto que esta en sesion en el componente
			personaDataManager.getPersonComponent().getPersonaVO().setBaseDTO((PersonaDTO)session.getAttribute(ContactoUtil.PERSONA));
			personaDataManager.getPersonComponent().cargarContactos();
			personaDataManager.getPersonComponent().setHeightDivDatosGenerales(getParmSession(HEIGHT_DIV_DATOSGENERALES));
			personaDataManager.getPersonComponent().setScrollHeightComRelatedContacts(getParmSession(SCROLL_HEIGHT_COMRELATEDCONTACTS));
		}
		
		//se colocan los eventos si es resumen o visualizacion
		String resumen = getParmSession(ContactoUtil.REPORTE);
		if (resumen != null) {
			cambiarValorEventoAResumen();
		}else{
			cambiarValorEventoAVisualizar();
		}
		
		personaDataManager.getPersonComponent().setEsRucPersona(false);
		personaDataManager.getPersonComponent().setReqNomCom(false);
		
		FacesUtil.restaurarVistaJSF();
	}
	
	public void editarPersona() {
		
//		personaDataManager.getPersonComponent().getPersonaVO().setBaseDTO((PersonaDTO) session.getAttribute(ContactoUtil.PERSONA));
		if(ContactoUtil.rucPersona){
			personaDataManager.getPersonComponent().setEsRucPersona(Boolean.TRUE);
		}
		
		personaDataManager.getPersonComponent().editarPersona(null, null, null);
		
		String accionExecute = getParmSession(ContactoUtil.ACCION);
		StringBuilder builder = new StringBuilder();
		if(accionExecute!=null){
			builder.append("parent.requestAjax('").append(accionExecute);
			builder.append("', ['divTabs','mensajes','pregunta','div_datosCliente'], {parameters: 'cerrarPopUpCorporativo=cerrarPersonaEmpresa', popWait: false, evalScripts: true});parent.ocultarModal();");
		}
		
		personaDataManager.getPersonComponent().setFunction(builder.toString());
		FacesUtil.restaurarVistaJSF();
	}
	
	public String getParmSession(String parametro) {
		String respuesta = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session.getAttribute(parametro) != null) {
			respuesta = (String) session.getAttribute(parametro); 
		}
		return respuesta;
	}
	
	public void guardarNuevaPersona() {
		personaDataManager.getPersonComponent().guardarPersonaNueva();
		personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
		
	}
	
	public void guardarEdicionPersona() {
		personaDataManager.getPersonComponent().guardarPersonaEditar();
		FacesMessage.Severity severity = FacesContext.getCurrentInstance().getMaximumSeverity();
    	if (severity==null || !severity.equals(FacesMessage.SEVERITY_ERROR)) {
			personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
			session.setAttribute(ContactoUtil.PERSONA, personaDataManager.getPersonComponent().getPersonaVO().getBaseDTO());
			if(session.getAttribute(ContactoUtil.RUC_PERSONA)!=null && session.getAttribute(ContactoUtil.RUC_PERSONA).toString().equals("ok") && personaDataManager.getPersonComponent().getPersonaVO().getBaseDTO().getNumeroRuc()!=null){
				session.setAttribute(ContactoUtil.RUC_PERSONA,personaDataManager.getPersonComponent().getPersonaVO().getBaseDTO().getNumeroRuc());
			
			}
			personaDataManager.getPersonComponent().setEsRucPersona(Boolean.FALSE);
			personaDataManager.getPersonComponent().setReqNomCom(false);
			//se setea la variable en el request para mostrar los mensajes en sispe
			String accionBotonCancelar = personaDataManager.getPersonComponent().getFunction();
			accionBotonCancelar = accionBotonCancelar.replace("cerrarPersonaEmpresa", "modificar");
			personaDataManager.getPersonComponent().setFunction(accionBotonCancelar);
    	}
	}
	
	public void cancelar() {
		personaDataManager.getPersonComponent().cancelar();
		personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
		personaDataManager.getPersonComponent().setEsRucPersona(Boolean.FALSE);
		personaDataManager.getPersonComponent().setReqNomCom(false);
		//se setea la variable en el request para mostrar los mensajes en sispe
		String accionBotonCancelar = personaDataManager.getPersonComponent().getFunction();
		accionBotonCancelar = accionBotonCancelar.replace("cerrarPersonaEmpresa", "cancelarModificacion");
		personaDataManager.getPersonComponent().setFunction(accionBotonCancelar);
	}
	
	public PersonaDataManager getPersonaDataManager() {
		return personaDataManager;
	}

	public void setPersonaDataManager(PersonaDataManager personaDataManager) {
		this.personaDataManager = personaDataManager;
	}

//	public HtmlForm getForm() {
//
//		if (getPersonaDataManager() != null) {
//			if (!(getPersonaDataManager().getIsInitialized())) {
//
//				initialize();
//				getPersonaDataManager().setIsInitialized(Boolean.TRUE);
//			}
//
//		}
//		return form;
//	}
//
//	public void setForm(HtmlForm form) {
//		this.form = form;
//	}

	@Override
	public CommonDataManager getDataManager() {
		return getPersonaDataManager();
	}

	/**
	 * Cambia el evento a resumen (R) para mostrar el componente en el resumen del pedido en SISPE
	 */
	public void cambiarValorEventoAResumen() {
		getLog().info("Cambio de evento a resumen");
		String accionExecute = getParmSession(ContactoUtil.ACCION);
		if (!accionExecute.equals("confirmarReservacion.do")) {
			personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.resumen"));
		}else{
			personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
		}
	}
	
	/**
	 * Cambia el evento a visualizar para mostrar el componente en las pesta\u00F1as de cotizacion
	 */
	public void cambiarValorEventoAVisualizar() {
		getLog().info("Cambio de evento a visualizar");
			personaDataManager.getPersonComponent().getPersonaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
	}

}
