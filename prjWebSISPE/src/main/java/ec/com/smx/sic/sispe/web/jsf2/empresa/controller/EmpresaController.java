package ec.com.smx.sic.sispe.web.jsf2.empresa.controller;

import static ec.com.smx.corpv2.web.util.common.resources.CorpCommonWebResources.getString;
import static ec.com.smx.sic.sispe.commons.util.LogSISPE.getLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpSession;

import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.dto.EmpresaDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.web.util.CorpCommonWebConstantes;
import ec.com.smx.corpv2.web.util.common.jsf.EmpresaComponent;
import ec.com.smx.corpv2.web.util.common.resources.CorpCommonWebResources;
import ec.com.smx.framework.jsf.commons.beans.controller.CommonController;
import ec.com.smx.framework.jsf.commons.beans.datamanager.CommonDataManager;
import ec.com.smx.framework.jsf.commons.util.FacesUtil;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.web.jsf2.empresa.dataManager.EmpresaDataManager;

@SuppressWarnings("serial")
@ManagedBean(name = "empresaController")
@RequestScoped
public class EmpresaController extends CommonController implements Serializable{
	
	@ManagedProperty(value = "#{empresaDataManager}")
	private EmpresaDataManager empresaDataManager;
	
	private HtmlForm formVisualizar;
	
	@Override
	public CommonDataManager getDataManager() {
		return getEmpresaDataManager();
	}

	@Override
	public void initialize() {
		//inicializa el componente empresa si no ha sido inicializado antes
		if(empresaDataManager.getEmpresaComponent()==null){
		empresaDataManager.setEmpresaComponent(new EmpresaComponent(empresaDataManager.getSessionDataManagerBase().getUserDto().getUserId(),
				empresaDataManager.getSessionDataManagerBase().getCompanyDto().getCompanyId(), 
				SystemProvenance.SISPE));
		}
		if (empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().getEvento() == null ||
				!empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().getEvento().equals(getString("evento.actualizar"))) {
			visualizarEmpresa();
		}
	}
	
	//Carga los datos de sesion en el componente y establece las funciones
	public void visualizarEmpresa() {
		
		getLog().info("Visualizar empresa");
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		
		if (session.getAttribute(ContactoUtil.LOCALIZACION) != null) {
			//carga el dto que esta en sesion en el componente
			empresaDataManager.getEmpresaComponent().getEmpresaVO().setBaseDTO((EmpresaDTO)session.getAttribute(ContactoUtil.LOCALIZACION));
			//se guarda en el componente de localizacion el dto que esta en sesion
			empresaDataManager.getEmpresaComponent().getLocalizacion().setLocalizacionComBusqueda((LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP));
			//se carga los datos de contacto de la localizacion
			empresaDataManager.getEmpresaComponent().datosLocalizaciones();
		}
		
		//cuando la accion es pesos finales se ejecuta como crearcotizacion
		String accionExecute = getParmSession(ContactoUtil.ACCION);
		if (accionExecute.equals("confirmarReservacion.do")) {
			accionExecute = "crearCotizacion.do";
		}
		getLog().info("Accion: "+accionExecute);
		//funcion para seleccionar una localizacion y setearla al formulario de struts
		StringBuilder builder = new StringBuilder();
		builder.append("parent.requestAjax('").append(accionExecute);
		builder.append("', ['mensajes'], {parameters: 'localizacionCorporativo=ok', popWait: false, evalScripts: true});");
		empresaDataManager.getEmpresaComponent().setFunction(builder.toString());
		
		//funcion para visualizar una localizacion desde el sispe.
		StringBuilder builderFunction = new StringBuilder();
		builderFunction.append("parent.requestAjax('").append(accionExecute);
		builderFunction.append("', ['pregunta','popUpCorporativo'], {parameters: 'visualizarLocalizacion=ok', popWait: false, evalScripts: true});parent.ocultarModal();");
		empresaDataManager.getEmpresaComponent().setFunctionVisualizarLocalizacion(builderFunction.toString());
		
		//funcion para cerrar el popUp del corporativo desde el sispe.
		StringBuilder builderFunctionCerrar = new StringBuilder();
		builderFunctionCerrar.append("parent.requestAjax('").append(accionExecute);
		builderFunctionCerrar.append("', ['pregunta','div_pagina'], {parameters: 'cerrarPopUpCorporativo=ok', popWait: false, evalScripts: true});parent.ocultarModal();");
		empresaDataManager.getEmpresaComponent().setFuntionCerrarPopUpLocalizacion(builderFunctionCerrar.toString());
		
		//se colocan los eventos si es resumen o visualizacion con sus medidas
		String resumen = getParmSession(ContactoUtil.TAB_RESUMEN_PEDIDO);
		if (resumen == null) {
			if (accionExecute.equals("crearCotizacion.do")) {
				empresaDataManager.getEmpresaComponent().getLocalizacion().setHeightLocal(180);
				empresaDataManager.getEmpresaComponent().setHeightempresa(190);
			} else if (accionExecute.equals("crearPedidoEspecial.do")) {
				empresaDataManager.getEmpresaComponent().getLocalizacion().setHeightLocal(140);
				empresaDataManager.getEmpresaComponent().setHeightempresa(149);
			}
			empresaDataManager.getEmpresaComponent().getLocalizacion().setEnableCheck(true);
			cambiarValorEventoAVisualizar();
		}else{
			empresaDataManager.getEmpresaComponent().getLocalizacion().setEnableCheck(Boolean.FALSE);
			Collection<LocalizacionDTO> locasig = new ArrayList<LocalizacionDTO>();
			locasig.add((LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP));
			empresaDataManager.getEmpresaComponent().getLocalizacion().setLocasig(locasig);
			cambiarValorEventoAResumen();
		}
		empresaDataManager.getEmpresaComponent().setContactoLocalizacion(Boolean.FALSE);
	}
	
	public void editarEmpresa() {
		getLog().info("Editar empresa");
		empresaDataManager.getEmpresaComponent().preEditarEmpresa();
	}
	
	public void editarLocalizacion() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().setBaseDTO((LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP));
		empresaDataManager.getEmpresaComponent().editarLocalizacion();
		empresaDataManager.getEmpresaComponent().setContactoLocalizacion(Boolean.TRUE);
	}
	
	public String getParmSession(String parametro) {
		String respuesta = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session.getAttribute(parametro) != null) {
			respuesta = (String) session.getAttribute(parametro); 
		}
		
		return respuesta;
	}
	
	public void actualizar() {
		getLog().info("Actualizar empresa componente");
		FacesUtil.restaurarVistaJSF();
	}
	
	public void salirLocalizacion() {
		//seteamos el componente en modo de visualizacion
		empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().setEvento(CorpCommonWebResources.getString("evento.mostrar"));
		empresaDataManager.getEmpresaComponent().setNombreTabSel("tab2");
		empresaDataManager.getEmpresaComponent().setContactoLocalizacion(Boolean.FALSE);
	}
	
	public void guardarLocalizacionEmpresa() {
		empresaDataManager.getEmpresaComponent().guardarLocalizacionComEmpresa();
		empresaDataManager.getEmpresaComponent().setContactoLocalizacion(Boolean.FALSE);
	}
	
	/**
	 * Seleccionar localizacion del componente de localizaciones y guardarlo en una variable de session para que lo use el SISPE
	 */
	public void seleccionarLocalizacion(AjaxBehaviorEvent e){
		
		getLog().info("seleccionarLocalizacion");
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.removeAttribute("localizacionSelect");
		LocalizacionDTO localizacionParam = (LocalizacionDTO)e.getComponent().getAttributes().get("localizacionSelect"); 

		//se quita el seleccionado de cualquier localizacion diferente
		if(localizacionParam.isSelected()){
			
			session.setAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP, localizacionParam);
			for(LocalizacionDTO locDTO : empresaDataManager.getEmpresaComponent().getLocalizacion().getLocasig()){
				if(locDTO.getId().getCodigoLocalizacion().intValue()!=localizacionParam.getId().getCodigoLocalizacion().intValue()){
					locDTO.setSelected(Boolean.FALSE);
				}
			}
		}
	}
	
	public EmpresaDataManager getEmpresaDataManager() {
		return empresaDataManager;
	}

	public void setEmpresaDataManager(EmpresaDataManager empresaDataManager) {
		this.empresaDataManager = empresaDataManager;
	}
	
	
	public HtmlForm getFormVisualizar() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		String resumenPedido = session.getAttribute(ContactoUtil.TAB_RESUMEN_PEDIDO) == null ? 
				null : ((String) session.getAttribute(ContactoUtil.TAB_RESUMEN_PEDIDO));
		
		if (resumenPedido != null) {
			if (empresaDataManager != null && empresaDataManager.getEmpresaComponent() !=null &&
					empresaDataManager.getEmpresaComponent().getLocalizacion() != null) {
				
				empresaDataManager.getEmpresaComponent().getLocalizacion().setEnableCheck(false);
				
				//cargar localizacion seleccionada
				LocalizacionDTO localizacionSeleccionada = (LocalizacionDTO) session.getAttribute(CorpCommonWebConstantes.LOCALIZACION_SELEC_COM_EMP);
				if (localizacionSeleccionada != null) {
					Collection<LocalizacionDTO> colLoc = new ArrayList<LocalizacionDTO>(1);
					colLoc.add(localizacionSeleccionada);
					empresaDataManager.getEmpresaComponent().getLocalizacion().setLocasig(colLoc);
				}
			}
		}
		FacesUtil.restaurarVistaJSF();
		return formVisualizar;
	}


	public void setFormVisualizar(HtmlForm formVisualizar) {
		this.formVisualizar = formVisualizar;
	}
	
	/**
	 * Cambia el evento a resumen (R) para mostrar el componente en el resumen del pedido en SISPE
	 */
	public void cambiarValorEventoAResumen() {
		getLog().info("Cambio de evento a resumen");
		empresaDataManager.getEmpresaComponent().getEmpresaVO().setEvento(CorpCommonWebResources.getString("evento.resumen"));
		empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().setEvento(CorpCommonWebResources.getString("evento.mostrar"));
		String accionExecute = getParmSession(ContactoUtil.ACCION);
		if (accionExecute.equals("crearCotizacion.do")) {
			empresaDataManager.getEmpresaComponent().getLocalizacion().setHeightLocal(108);
			empresaDataManager.getEmpresaComponent().setHeightempresa(116);
		} else if (accionExecute.equals("crearPedidoEspecial.do")) {
			empresaDataManager.getEmpresaComponent().getLocalizacion().setHeightLocal(88);
			empresaDataManager.getEmpresaComponent().setHeightempresa(96);
		}  else if (accionExecute.equals("confirmarReservacion.do")) {
			empresaDataManager.getEmpresaComponent().getLocalizacion().setHeightLocal(158);
			empresaDataManager.getEmpresaComponent().setHeightempresa(166);
		}
	}
	
	/**
	 * Cambia el evento a visualizar para mostrar el componente en las pesta\u00F1as de cotizacion tanto empresa como localizacion
	 */
	public void cambiarValorEventoAVisualizar() {
		getLog().info("Cambio de evento a visualizar");
		empresaDataManager.getEmpresaComponent().getEmpresaVO().setEvento(CorpCommonWebResources.getString("evento.visualizar"));
		empresaDataManager.getEmpresaComponent().getLocalizacion().getLocalizacionVO().setEvento(CorpCommonWebResources.getString("evento.mostrar"));
	}

}
