/*
 * Clase ActualizarPasswordForm.java
 * Creado el 09/11/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Contiene los campos y los demas controles del formulario para cambiar la clave del usuario.
 * Tambi\u00E9n se resetean los formulario cada vez que se realiza una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @author	mbraganza
 * @version	1.1
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class ActualizarPasswordForm extends ActionForm
{
	//controles del formulario
	private String passwordActual;
	private String nuevoPassword;
	private String confirmarPassword;
	private String botonGuardar;
	private String botonCancelar;

	/**
	 * Realiza la validaci\u00F3n de la p\u00E1gina <code>cambiarClave.jsp</code>.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		El request que estamos procesando
	 * @return errors		Los errores recogidos durante la ejecuci\u00F3n
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();

		String ayuda = request.getParameter(Globals.AYUDA);
		if(ayuda != null && !ayuda.equals("")) {
			if(ayuda.equals("cambiarClave")) {
				this.botonGuardar = ayuda;
			}
		}

		try
		{
			//cuando se da click el boton Registrar Nuevo Registro
			if(this.botonGuardar != null){
				//valida el campo del c\u00F3digo del Par\u00E1metro
				validar.validateMandatory(errors,"passwordActual",this.passwordActual,"errors.requerido","Clave anterior");
				validar.validateMandatory(errors,"nuevoPassword",this.nuevoPassword,"errors.requerido","Clave nueva");
				validar.validateMandatory(errors,"confirmarPassword",this.confirmarPassword,"errors.requerido","Confirmar clave");
			}
		}
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>cambiarClave.jsp</code>, en cada petici\u00F3n.
	 * @param mapping 	El mapeo utilizado para seleccionar esta instancia
	 * @param request 	La petici&oacue; que estamos procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		this.passwordActual=null;
		this.nuevoPassword=null;
		this.confirmarPassword=null;
		this.botonGuardar=null;
	}

	/**
	 * @return Devuelve passwordActual.
	 */
	public String getPasswordActual()
	{
		return passwordActual;
	}
	/**
	 * @param passwordActual El passwordActual a establecer.
	 */
	public void setPasswordActual(String passwordActual)
	{
		this.passwordActual = passwordActual;
	}
	/**
	 * @return Devuelve nuevoPassword.
	 */
	public String getNuevoPassword()
	{
		return nuevoPassword;
	}
	/**
	 * @param nuevoPassword El nuevoPassword a establecer.
	 */
	public void setNuevoPassword(String nuevoPassword)
	{
		this.nuevoPassword = nuevoPassword;
	}
	/**
	 * @return Devuelve confirmarPassword.
	 */
	public String getConfirmarPassword()
	{
		return confirmarPassword;
	}
	/**
	 * @param confirmarPassword El confirmarPassword a establecer.
	 */
	public void setConfirmarPassword(String confirmarPassword)
	{
		this.confirmarPassword = confirmarPassword;
	}
	/**
	 * @return Devuelve botonGuardar.
	 */
	public String getBotonGuardar()
	{
		return botonGuardar;
	}
	/**
	 * @param botonGuardar El botonGuardar a establecer.
	 */
	public void setBotonGuardar(String botonGuardar)
	{
		this.botonGuardar = botonGuardar;
	}

	/**
	 * @return Devuelve botonCancelar.
	 */
	public String getBotonCancelar()
	{
		return botonCancelar;
	}
	/**
	 * @param botonCancelar El botonCancelar a establecer.
	 */
	public void setBotonCancelar(String botonCancelar)
	{
		this.botonCancelar = botonCancelar;
	}
}
