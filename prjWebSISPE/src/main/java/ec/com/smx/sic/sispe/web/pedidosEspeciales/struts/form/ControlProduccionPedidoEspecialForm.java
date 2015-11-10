/*
 * ControlProduccionPedidoEspecialForm.java
 * Creado el 14/04/2008 11:35:47
 *   	
 */
package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @author nperalta
 *
 */

@SuppressWarnings("serial")
public class ControlProduccionPedidoEspecialForm extends ListadoPedidosForm {
	/**
	 * Clase ofrece: 
	 * nperalta
	 * 11:35:47
	 * version 0.1
	 *  	
	 */
	/* Campos del formulario de control de producci\u00F3n de pedido especial: carnes
	 * private String checkSeleccionarTodo: selecciona todos los checks del formulario
	 * private String checksSeleccionar[]: selecciona solo un check
	 * 
	 * 
	 * **/
	private String checkSeleccionarTodo;
	private String checksSeleccionar[];
	private String peso;
	private String opTipoAgrupacion;

	PropertyValidator validador = new PropertyValidatorImpl();

	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		LogSISPE.getLog().info("AYUDAAAAAA: "+request.getParameter(Globals.AYUDA));
	
		/*
		if(request.getParameter("botonProducir")!=null){
			if(this.checksSeleccionar==null || this.checksSeleccionar.length==0){
				errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un pedido"));
			}

		}*/
		return errors;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		//llamada al reset de la clase padre
		super.reset(mapping, request);
		this.checkSeleccionarTodo=null;
		this.checksSeleccionar=null;
		this.peso=null;
	}
	/**
	 * @return el checkSeleccionarTodo
	 */
	public String getCheckSeleccionarTodo() {
		return checkSeleccionarTodo;
	}
	/**
	 * @param checkSeleccionarTodo el checkSeleccionarTodo a establecer
	 */
	public void setCheckSeleccionarTodo(String checkSeleccionarTodo) {
		this.checkSeleccionarTodo = checkSeleccionarTodo;
	}
	/**
	 * @return el checksSeleccionar
	 */
	public String[] getChecksSeleccionar() {
		return checksSeleccionar;
	}
	/**
	 * @param checksSeleccionar el checksSeleccionar a establecer
	 */
	public void setChecksSeleccionar(String[] checksSeleccionar) {
		this.checksSeleccionar = checksSeleccionar;
	}


	/**
	 * @return el peso
	 */
	public String getPeso() {
		return peso;
	}


	/**
	 * @param peso el peso a establecer
	 */
	public void setPeso(String peso) {
		this.peso = peso;
	}

	/**
	 * @return el opTipoAgrupacion
	 */
	public String getOpTipoAgrupacion() {
		return opTipoAgrupacion;
	}

	/**
	 * @param opTipoAgrupacion el opTipoAgrupacion a establecer
	 */
	public void setOpTipoAgrupacion(String opTipoAgrupacion) {
		this.opTipoAgrupacion = opTipoAgrupacion;
	}


}
