/*
 * CatalogoCanastosForm.java
 * Creado el 03/03/2008 15:45:27
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
/**
 * @author nperalta
 *
 */
@SuppressWarnings("serial")
public class CatalogoCanastosForm extends ActionForm{
	/**
	 * Clase ofrece: 
	 * nperalta
	 * 15:45:27
	 * version 0.1
	 *  	
	 */
	private String emailContacto;
	private String opEscogerTodos;
	private String [] opEscogerSeleccionado;
	
	
	PropertyValidator validador = new PropertyValidatorImpl();
	
		
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errors = new ActionErrors();
		LogSISPE.getLog().info("boton AceptarEnvio: " + request.getParameter("aceptarEnvioMail"));
		LogSISPE.getLog().info("boton cerrarVentanaMail: " + request.getParameter("cerrarVentanaMail"));
		
		if(request.getParameter("aceptarEnvioMail")!=null)
		{
			
			validador.validateFormato(errors,"emailContacto",this.emailContacto,true,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
			if(!errors.isEmpty()){
				//Variable para controlar que no se dibuje nuevamente el frame que bloquea la pantalla
				request.setAttribute("ec.com.smx.sic.sispe.ventanaActiva", "ok");
			}
		}
	
		LogSISPE.getLog().info("opEscogerSeleccionado: " + this.opEscogerSeleccionado);
		if(request.getParameter("ingresoMailContacto")!=null){
			if(this.opEscogerSeleccionado!=null)
				LogSISPE.getLog().info("opEscogerSeleccionado tama\u00F1o: " + this.opEscogerSeleccionado.length);
			if(this.opEscogerSeleccionado==null || this.opEscogerSeleccionado.length==0){
				LogSISPE.getLog().info("entra al error");
		        errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un canasto"));
		      }
		    }
		
		LogSISPE.getLog().info("*****errores"+ errors.size());
		return errors;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request)
	  {
		this.emailContacto=null;
		this.opEscogerSeleccionado=null;
		this.opEscogerTodos=null;
	  }

	/**
	 * @return el emailContacto
	 */
	public String getEmailContacto() {
		return emailContacto;
	}


	/**
	 * @param emailContacto el emailContacto a establecer
	 */
	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}


	/**
	 * @return el opEscogerSeleccionado
	 */
	public String[] getOpEscogerSeleccionado() {
		return opEscogerSeleccionado;
	}


	/**
	 * @param opEscogerSeleccionado el opEscogerSeleccionado a establecer
	 */
	public void setOpEscogerSeleccionado(String[] opEscogerSeleccionado) {
		this.opEscogerSeleccionado = opEscogerSeleccionado;
	}


	/**
	 * @return el opEscogerTodos
	 */
	public String getOpEscogerTodos() {
		return opEscogerTodos;
	}


	/**
	 * @param opEscogerTodos el opEscogerTodos a establecer
	 */
	public void setOpEscogerTodos(String opEscogerTodos) {
		this.opEscogerTodos = opEscogerTodos;
	}


	}
