package ec.com.smx.sic.sispe.web.jsf2.persona.empresa.controller;

import static ec.com.smx.sic.sispe.common.util.ContactoUtil.TIPO_DOCUMENTO;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.jsf.commons.beans.controller.CommonController;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.jsf2.empresa.controller.EmpresaController;
import ec.com.smx.sic.sispe.web.jsf2.persona.controller.PersonaController;
import ec.com.smx.sic.sispe.web.jsf2.persona.empresa.dataManager.BusquedaDataManager;


/**
 * @author osaransig
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "busquedaController")
public class BusquedaController extends CommonController {
	
	@ManagedProperty(value = "#{personaController}")
	private PersonaController personaController;
	
	@ManagedProperty(value = "#{empresaController}")
	private EmpresaController empresaController;
	
	@ManagedProperty(value = "#{personaEmpresaController}")
	private PersonaEmpresaController personaEmpresaController;
	
	@ManagedProperty(value = "#{busquedaDataManager}")
	private BusquedaDataManager busquedaDataManager;
	
	/**
	 * dependiendo del tipo de documento de la persona/empresa inicializa el manager correspondiente
	 */
	public void buscar() {
		String tipoDoc = getParmSession(TIPO_DOCUMENTO);
		LogSISPE.getLog().info("Tipo Documento de sesion: "+tipoDoc);
		if (tipoDoc == null) {
			personaEmpresaController.initialize();
		} else if (tipoDoc.equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA) || tipoDoc.equals(CorporativoConstantes.TIPO_DOCUMENTO_PASAPORTE)) {
			personaController.initialize();
		} else if (tipoDoc.equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) || tipoDoc.equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL)) {
			empresaController.initialize();
		} else {
			personaEmpresaController.initialize();
		}
	}
	
	public String getParmSession(String parametro) {
		String respuesta = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session.getAttribute(parametro) != null) {
			respuesta = (String) session.getAttribute(parametro); 
		}
		return respuesta;
	}


	public PersonaController getPersonaController() {
		return personaController;
	}


	public void setPersonaController(PersonaController personaController) {
		this.personaController = personaController;
	}


	public EmpresaController getEmpresaController() {
		return empresaController;
	}


	public void setEmpresaController(EmpresaController empresaController) {
		this.empresaController = empresaController;
	}


	public PersonaEmpresaController getPersonaEmpresaController() {
		return personaEmpresaController;
	}


	public void setPersonaEmpresaController(
			PersonaEmpresaController personaEmpresaController) {
		this.personaEmpresaController = personaEmpresaController;
	}


	@Override
	public void initialize() {
		LogSISPE.getLog().info("Initialize");
		
	}


	@Override
	public CommonDataManager getDataManager() {
		// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
		return getBusquedaDataManager();
	}


	public BusquedaDataManager getBusquedaDataManager() {
		return busquedaDataManager;
	}


	public void setBusquedaDataManager(BusquedaDataManager busquedaDataManager) {
		this.busquedaDataManager = busquedaDataManager;
	}
}
