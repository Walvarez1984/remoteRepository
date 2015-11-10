package ec.com.smx.sic.sispe.web.jsf2.empresa.dataManager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ec.com.smx.corpv2.web.util.common.jsf.EmpresaComponent;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.session.datamanager.SessionDataManagerBase;

@SuppressWarnings("serial")
@ManagedBean(name = "empresaDataManager")
@SessionScoped
public class EmpresaDataManager extends CommonDataManager {
	
	@ManagedProperty(value = "#{sessionDataManagerBase}")
	private SessionDataManagerBase sessionDataManagerBase;
	
	private EmpresaComponent empresaComponent;

	@Override
	public String getIdDataManager() {
		// TODO Ap\u00E9ndice de m\u00E9todo generado autom\u00E1ticamente
		return "empresaDataManager";
	}

	public SessionDataManagerBase getSessionDataManagerBase() {
		return sessionDataManagerBase;
	}

	public void setSessionDataManagerBase(
			SessionDataManagerBase sessionDataManagerBase) {
		this.sessionDataManagerBase = sessionDataManagerBase;
	}

	public EmpresaComponent getEmpresaComponent() {
		return empresaComponent;
	}

	public void setEmpresaComponent(EmpresaComponent empresaComponent) {
		this.empresaComponent = empresaComponent;
	}
	
	

}
