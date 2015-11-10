package ec.com.smx.sic.sispe.web.jsf2.persona.dataManager;

import java.io.IOException;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ec.com.smx.corpv2.common.util.CorporativoLogger;
import ec.com.smx.corpv2.dto.FotoPersonaDTO;
import ec.com.smx.corpv2.web.util.common.jsf.Person;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.session.datamanager.SessionDataManagerBase;
import ec.com.smx.framework.jsf.messages.MessagesController;

/**
 * @author osaransig
 * 2013-02-21
 */
@SuppressWarnings("serial")
@ManagedBean(name = "personaDataManager")
@SessionScoped
public class PersonaDataManager extends CommonDataManager {
	
	@ManagedProperty(value = "#{sessionDataManagerBase}")
	private SessionDataManagerBase sessionDataManagerBase;
	
	private Person personComponent;

	@Override
	public String getIdDataManager() {
		return "personaDataManager";
	}
	
	
	public Person getPersonComponent() {
		return personComponent;
	}

	public void setPersonComponent(Person personComponent) {
		this.personComponent = personComponent;
	}

	public SessionDataManagerBase getSessionDataManagerBase() {
		return sessionDataManagerBase;
	}

	public void setSessionDataManagerBase(
			SessionDataManagerBase sessionDataManagerBase) {
		this.sessionDataManagerBase = sessionDataManagerBase;
	}

	public void paint(OutputStream stream, Object data) throws IOException {
		try{
		if(data != null){
			FotoPersonaDTO fotoPersona = getPersonComponent().getPersonaVO().getBaseDTO().getFotoPersonaDTO();
			if(fotoPersona.getDatoImagen() !=null){
				stream.write(fotoPersona.getDatoImagen());
			}
		}		
		}catch (Exception e) {
			CorporativoLogger.LOG.error("Error al tratar de dibujar la foto de la persona",e);
			MessagesController.addError(null, "Error al dibujar la im&aacutegen");
		}
		
    }


}
