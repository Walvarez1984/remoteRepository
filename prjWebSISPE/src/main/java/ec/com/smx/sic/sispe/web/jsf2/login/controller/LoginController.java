package ec.com.smx.sic.sispe.web.jsf2.login.controller;

import javax.faces.component.html.HtmlForm;

import ec.com.smx.corpv2.common.util.CorporativoLogger;
import ec.com.smx.corpv2.jsf.commons.session.controller.CorporativoSessionControllerBase;


public class LoginController extends CorporativoSessionControllerBase implements CorporativoLogger {

	private HtmlForm form;
	
	private boolean mostrarTabMenu;
	private String errorMenuMessage;
	
//	 @PostConstruct
//     public void postConstruct() {
//          inicializar();
//     }

	public HtmlForm getForm() {
		inicializar();
		return form;
	}
	
	

	public void setForm(HtmlForm form) {
		this.form = form;
	}

	public boolean isMostrarTabMenu() {
		return mostrarTabMenu;
	}

	public void setMostrarTabMenu(boolean mostrarTabMenu) {
		this.mostrarTabMenu = mostrarTabMenu;
	}

	public String getErrorMenuMessage() {
		return errorMenuMessage;
	}

	public void setErrorMenuMessage(String errorMenuMessage) {
		this.errorMenuMessage = errorMenuMessage;
	}
	
}