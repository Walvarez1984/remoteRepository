package ec.com.smx.sic.sispe.web.jsf2.persona.empresa.dataManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.session.datamanager.SessionDataManagerBase;

/**
 * @author osaransig
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "busquedaDataManager")
public class BusquedaDataManager extends CommonDataManager {

	
	@ManagedProperty(value="#{sessionDataManagerBase}")
	private SessionDataManagerBase sessionDataManagerBase;

	
	@Override
	public String getIdDataManager() {
		return "busquedaDataManager";
	}


	public SessionDataManagerBase getSessionDataManagerBase() {
		return sessionDataManagerBase;
	}


	public void setSessionDataManagerBase(
			SessionDataManagerBase sessionDataManagerBase) {
		this.sessionDataManagerBase = sessionDataManagerBase;
	}
	
	
}
