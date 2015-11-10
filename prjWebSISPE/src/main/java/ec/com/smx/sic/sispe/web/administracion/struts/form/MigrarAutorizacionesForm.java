/*
 * MigrarAutorizacionesForm.java
 * Creado el 01/10/2013 11:10:18
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * @author bgudino
 *
 */
public class MigrarAutorizacionesForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoPedido;
	private String codigoPedidoCorregir;

	/**
	 * Clase ofrece: 
	 * bgudino
	 * 11:10:18
	 * version 0.1
	 *  	
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request){
		LogSISPE.getLog().info("validando la migracion de autorizaciones en el form");
		ActionErrors errors = new ActionErrors();
//		PropertyValidator validar = new PropertyValidatorImpl();
	   	
		String ayuda = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("valor ayuda "+ayuda);
		return errors;
	}

	/**
	 * @return el codigoPedido
	 */
	public String getCodigoPedido() {
		return codigoPedido;
	}

	/**
	 * @param codigoPedido el codigoPedido a establecer
	 */
	public void setCodigoPedido(String codigoPedido) {
		this.codigoPedido = codigoPedido;
	}

	/**
	 * @return el codigoPedidoCorregir
	 */
	public String getCodigoPedidoCorregir() {
		return codigoPedidoCorregir;
	}

	/**
	 * @param codigoPedidoCorregir el codigoPedidoCorregir a establecer
	 */
	public void setCodigoPedidoCorregir(String codigoPedidoCorregir) {
		this.codigoPedidoCorregir = codigoPedidoCorregir;
	}
	
	
	
}
