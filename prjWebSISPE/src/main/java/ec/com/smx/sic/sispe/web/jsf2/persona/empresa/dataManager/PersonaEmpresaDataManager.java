package ec.com.smx.sic.sispe.web.jsf2.persona.empresa.dataManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ec.com.smx.corpv2.web.util.common.jsf.BusquedaPersonaEmpresaComponent;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.session.datamanager.SessionDataManagerBase;

@SuppressWarnings("serial")
@ManagedBean(name = "personaEmpresaDataManager")
@SessionScoped
public class PersonaEmpresaDataManager extends CommonDataManager {

	@Override
	public String getIdDataManager() {
		return "personaEmpresaDataManager";
	}

	@ManagedProperty(value="#{sessionDataManagerBase}")
	private SessionDataManagerBase sessionDataManagerBase;

	private BusquedaPersonaEmpresaComponent busquedaComponente;

	public SessionDataManagerBase getSessionDataManagerBase() {
		return sessionDataManagerBase;
	}

	public void setSessionDataManagerBase(
			SessionDataManagerBase sessionDataManagerBase) {
		this.sessionDataManagerBase = sessionDataManagerBase;
	}

	public BusquedaPersonaEmpresaComponent getBusquedaComponente() {
		return busquedaComponente;
	}

	public void setBusquedaComponente(
			BusquedaPersonaEmpresaComponent busquedaComponente) {
		this.busquedaComponente = busquedaComponente;
	}
	




	
}
