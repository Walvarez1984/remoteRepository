package ec.com.smx.sic.sispe.web.jsf2.persona.empresa.controller;

import static ec.com.smx.sic.sispe.common.util.ContactoUtil.FLAG_BUSCAR_PERSONA_EMPRESA;
import static ec.com.smx.sic.sispe.common.util.ContactoUtil.NUMERO_DOCUMENTO_STRUTS;
import static ec.com.smx.sic.sispe.common.util.ContactoUtil.PERSONA_EMPRESA_SELECCIONADA;
import static ec.com.smx.sic.sispe.common.util.ContactoUtil.TIPO_DOCUMENTO;
import static ec.com.smx.sic.sispe.commons.util.LogSISPE.getLog;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.dto.EmpresaDTO;
import ec.com.smx.corpv2.web.util.common.jsf.BusquedaPersonaEmpresaComponent;
import ec.com.smx.corpv2.web.util.common.jsf.EmpresaComponent;
import ec.com.smx.corpv2.web.util.common.jsf.Person;
import ec.com.smx.corpv2.web.util.common.resources.CorpCommonWebResources;
import ec.com.smx.framework.jsf.commons.beans.controller.CommonController;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.util.FacesUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.web.jsf2.persona.empresa.dataManager.PersonaEmpresaDataManager;

/**
 * @author osaransig
 * 2013-03-08
 *
 */
@SuppressWarnings("serial")
@ManagedBean(name = "personaEmpresaController")
@ViewScoped
public class PersonaEmpresaController extends CommonController implements Serializable {

    @ManagedProperty(value = "#{personaEmpresaDataManager}")
    private PersonaEmpresaDataManager personaEmpresaDataManager;

  
    @Override
    public void initialize() {
    	getLog().info("ENTRANDO A CONTROLADOR DE PERSONAS EMPRESAS");
    	
    	if (personaEmpresaDataManager.getSessionDataManagerBase().getUserDto() != null) {
    		if(personaEmpresaDataManager.getBusquedaComponente()==null){
	    	personaEmpresaDataManager.setBusquedaComponente(
	    			new BusquedaPersonaEmpresaComponent(personaEmpresaDataManager.getSessionDataManagerBase().getUserDto().getUserId(), 
	    			personaEmpresaDataManager.getSessionDataManagerBase().getCompanyDto().getCompanyId(), 
	    			SystemProvenance.SISPE));
    		}
	    	cargarBuscar();
    	}
    	
    }

    public void cargarBuscar() {
    	String numDocumento = getParmSession(NUMERO_DOCUMENTO_STRUTS);
    	getLog().info("Numero documento: {}", numDocumento);
    	
    	String flagBuscarPerEmp = getParmSession(FLAG_BUSCAR_PERSONA_EMPRESA);
    	boolean buscarPerEmp = (flagBuscarPerEmp != null && flagBuscarPerEmp.equals("true")) ? true : false;
    	
    	if (numDocumento != null) {
    		personaEmpresaDataManager.getBusquedaComponente().inicialize(numDocumento, buscarPerEmp, getParmSession(TIPO_DOCUMENTO));
    	}
    	//cuando se abre la busqueda sin numero documento
    	else{ 
    		personaEmpresaDataManager.getBusquedaComponente().setEmpresaComponent(new EmpresaComponent(personaEmpresaDataManager.getSessionDataManagerBase().getUserDto().getUserId(), 
	    			personaEmpresaDataManager.getSessionDataManagerBase().getCompanyDto().getCompanyId(), 
	    			SystemProvenance.SISPE));
    		personaEmpresaDataManager.getBusquedaComponente().setPersonComponent(new Person(personaEmpresaDataManager.getSessionDataManagerBase().getCompanyDto().getCompanyId(), SystemProvenance.SISPE, personaEmpresaDataManager.getSessionDataManagerBase().getUserDto().getUserId()));

    		personaEmpresaDataManager.getBusquedaComponente().inicializarObjetos();
    		personaEmpresaDataManager.getBusquedaComponente().setMostrarLocalizacionesEmpresa(false);
    	
    	}
//    	personaEmpresaDataManager.getBusquedaComponente().getPersonaFilterVO().getBaseDTO().setNumeroDocumento(getParmSession(NUMERO_DOCUMENTO_STRUTS));
//    	personaEmpresaDataManager.setBusquedaComponente(
//    			new BusquedaPersonaEmpresaComponent(personaEmpresaDataManager.getSessionDataManagerBase().getUserDto().getUserId(), 
//    			personaEmpresaDataManager.getSessionDataManagerBase().getCompaniaDTO().getId().getCodigoCompania(), 
//    			SystemProvenance.SISPE, getParmSession(NUMERO_DOCUMENTO_STRUTS), buscarPerEmp));
//    	getLog().info("Nombre persona a mostrar en pantalla 1: " + personaEmpresaDataManager.getBusquedaComponente().getLazyPersona().iterator().next().getNombreCompleto());
//    	FacesUtils.restaurarVistaJSF();
//    	getLog().info("Nombre persona a mostrar en pantalla 2: " + personaEmpresaDataManager.getBusquedaComponente().getLazyPersona().iterator().next().getNombreCompleto());
    }
    
    
    @Override
    public CommonDataManager getDataManager() {
    	return getPersonaEmpresaDataManager();
    }

    
    public void seleccionarPersona() {
    	getLog().info("Seleccion persona desde busqueda de contactos");
    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    	session.setAttribute(PERSONA_EMPRESA_SELECCIONADA, personaEmpresaDataManager.getBusquedaComponente().getPersonaVO().getBaseDTO());
    	session.setAttribute(TIPO_DOCUMENTO, personaEmpresaDataManager.getBusquedaComponente().getPersonaVO().getBaseDTO().getTipoDocumento());
    	if(session.getAttribute(ContactoUtil.RUC_PERSONA)!=null &&  personaEmpresaDataManager.getBusquedaComponente().getPersonaVO().getBaseDTO().getNumeroRuc()!=null 
    			&& ((personaEmpresaDataManager.getBusquedaComponente().getPersonaFilterVO().getBaseDTO().getNumeroDocumento()!=null&&personaEmpresaDataManager.getBusquedaComponente().getPersonaFilterVO().getBaseDTO().getNumeroDocumento().length()>10 )
    					|| personaEmpresaDataManager.getBusquedaComponente().getPersonaFilterVO().getBaseDTO().getNombreComercial()!=null)){
    		session.setAttribute(NUMERO_DOCUMENTO_STRUTS, personaEmpresaDataManager.getBusquedaComponente().getPersonaVO().getBaseDTO().getNumeroRuc());    		
    	}else{
    		session.setAttribute(NUMERO_DOCUMENTO_STRUTS, personaEmpresaDataManager.getBusquedaComponente().getPersonaVO().getBaseDTO().getNumeroDocumento());
    	}
    	
    	setearFuncionSeleccionPersonaEmpresa();
    }

    public void seleccionarEmpresaLocalizacion() {
    	getLog().info("Seleccion empresa desde busqueda de contactos");
    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    	
//    	personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada().
//    		setEmpresaDTO(personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getEmpresaVO().getBaseDTO());
    	
    	personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getLocalizacion().
    		setLocalizacionComBusqueda(personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada());
    	
    	//fijando localizacion para que se utilice en el componente de empresas
    	session.setAttribute(ContactoUtil.LOCALIZACION_SELEC_COM_EMP, personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada());
    	
    	session.setAttribute(PERSONA_EMPRESA_SELECCIONADA, personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada());
    	session.setAttribute(TIPO_DOCUMENTO, personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada().getEmpresaDTO().getValorTipoDocumento());
    	session.setAttribute(NUMERO_DOCUMENTO_STRUTS, personaEmpresaDataManager.getBusquedaComponente().getLocalizacionSeleccionada().getEmpresaDTO().getNumeroRuc());
    	setearFuncionSeleccionPersonaEmpresa();
    }
    
    public String getParmSession(String parametro) {
		String respuesta = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session.getAttribute(parametro) != null) {
			respuesta = (String) session.getAttribute(parametro); 
		}
		return respuesta;
	}
    
	public PersonaEmpresaDataManager getPersonaEmpresaDataManager() {
		return personaEmpresaDataManager;
	}

	public void setPersonaEmpresaDataManager(
			PersonaEmpresaDataManager personaEmpresaDataManager) {
		this.personaEmpresaDataManager = personaEmpresaDataManager;
	}

  
	public void setearFuncionSeleccionPersonaEmpresa() {
		
		String accionExecute = getParmSession(ContactoUtil.ACCION);
		StringBuilder builder = new StringBuilder();
		
		if (accionExecute.equals("crearCotizacion.do")) {
		
			builder.append("parent.requestAjax('").append(accionExecute);
			builder.append("', ['divTabs','mensajes','pregunta','div_datosCliente'], {parameters: 'personaEmpresaDesdeComBusqueda=ok', popWait: true, evalScripts: true});parent.ocultarModal();");
			
						
		} else if (accionExecute.equals("crearPedidoEspecial.do")) {
			
			builder.append("parent.requestAjax('").append(accionExecute);
			builder.append("', ['mensajes','pregunta','div_datosCliente','divTabs'], {parameters: 'personaEmpresaDesdeComBusqueda=ok', popWait: true, evalScripts: true});parent.ocultarModal();");
			
		}
		getLog().info("Seteando funcion: "+builder.toString());
		personaEmpresaDataManager.getBusquedaComponente().setFunction(builder.toString());
	}
	
	public void editarLocalizacion() {
		
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getLocalizacion()
			.editarLocalizacionContactoPersonal();
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().cargaContactos();
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent()
			.getDatosContactoPersonal().editarInformacionContactoPersonal();
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent()
			.cargarContactosRelacionados();
		if(personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getRelatedContacts().getLstContactosRelacionados().size()==0){
			personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().setTabSeleccionado(5);
			personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().setNombreTabSel("tab5");
			Boolean[] tabsEnabled = {Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE};
			personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().setTabEnabled(tabsEnabled);
		}
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getEmpresaVO().
			setEvento(CorpCommonWebResources.getString("evento.visualizar"));
		
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getLocalizacion()
			.getLocalizacionVO().setEvento(CorpCommonWebResources.getString("evento.actualizar"));
		
		EmpresaDTO empresaDTO  = (EmpresaDTO) CollectionUtils.find(personaEmpresaDataManager.getBusquedaComponente().getEmpresaCol(), 
				new BeanPropertyValueEqualsPredicate("id.codigoEmpresa", personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getLocalizacion().getLocalizacionVO().getBaseDTO().getEmpresaDTO().getId().getCodigoEmpresa())); 
		personaEmpresaDataManager.getBusquedaComponente().getEmpresaComponent().getEmpresaVO().
			setBaseDTO(empresaDTO);
		FacesUtil.restaurarVistaJSF();
	}

}