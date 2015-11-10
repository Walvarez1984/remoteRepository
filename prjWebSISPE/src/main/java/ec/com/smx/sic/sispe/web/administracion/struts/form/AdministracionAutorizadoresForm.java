/*
 * Creado el 17/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
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
 * @author mnaranjo
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
@SuppressWarnings("serial")
public class AdministracionAutorizadoresForm extends ActionForm {
	//private String estadoAutorizacion;
	private String nombreUsuario;
	private String idUsuarioAutorizado;
	private String local;
	private String estadoAutorizador;
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		String ayuda = request.getParameter(Globals.AYUDA);

		try {
			//  cuando se da click el boton Nuevo
			// Registro----------------------------------------------------------------
			if (ayuda!=null && ayuda.equals("guardarAdminAutorizadores")) {
				
				validar.validateMandatory(errors, "nombreUsuario",this.nombreUsuario, "errors.requerido",	"Nombre");
				validar.validateMandatory(errors, "local",this.local, "errors.requerido",	"Local");
			}
			// cuando el usuario quiere agregar un nuevo usuario a la lista de autorizados
			else if(ayuda!=null && ayuda.equals("agregarUsuario")){
				if(idUsuarioAutorizado != null && idUsuarioAutorizado.equals("sinValor")){
					validar.validateMandatory(errors, "idUsuarioAutorizado",this.idUsuarioAutorizado, "errors.requerido",	"Usuario");
				}
			}

		} catch (Exception ex) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
		this.local = null;
		this.nombreUsuario = null;
		this.idUsuarioAutorizado = null;
	}
	
	/**
	 * @return Devuelve nombreCompletoUsuario.
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	/**
	 * @param nombreCompletoUsuario El nombreCompletoUsuario a establecer.
	 */
	public void setNombreUsuario(String nombreCompletoUsuario) {
		this.nombreUsuario = nombreCompletoUsuario;
	}
	/**
	 * @return Devuelve local.
	 */
	public String getLocal() {
		return local;
	}
	/**
	 * @param local El local a establecer.
	 */
	public void setLocal(String local) {
		this.local = local;
	}
	/**
	 * @return Devuelve idUsuarioAutorizado.
	 */
	public String getIdUsuarioAutorizado() {
		return idUsuarioAutorizado;
	}
	/**
	 * @param idUsuarioAutorizado El idUsuarioAutorizado a establecer.
	 */
	public void setIdUsuarioAutorizado(String idUsuarioAutorizado) {
		this.idUsuarioAutorizado = idUsuarioAutorizado;
	}
	/**
	 * @return Devuelve estadoAutorizador.
	 */
	public String getEstadoAutorizador() {
		return estadoAutorizador;
	}
	/**
	 * @param estadoAutorizador El estadoAutorizador a establecer.
	 */
	public void setEstadoAutorizador(String estadoAutorizador) {
		this.estadoAutorizador = estadoAutorizador;
	}
}
