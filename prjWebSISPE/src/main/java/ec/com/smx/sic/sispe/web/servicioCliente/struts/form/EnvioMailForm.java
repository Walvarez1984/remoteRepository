/*
 * Clase ListadoPedidosForm.java
 * Creado el 27/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>Formulario de b\u00FAsqueda gen\u00E9rico usado para cualquier tipo de consulta</p>
 * @author 	fmunoz
 * @version	2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class EnvioMailForm extends ActionForm
{
	private String textoMail;
	private String ccMail;
	private String asuntoMail;
	private String emailEnviarCotizacion;
	
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>redactarMail.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		
		try
		{
			if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("siEnviarEmail")){
				LogSISPE.getLog().info("request redactarMail");
				validar.validateMandatory(errors,"emailContacto",this.emailEnviarCotizacion,"errors.requerido","Para");
				if(errors.isEmpty()){
					validar.validateFormato(errors,"emailContacto",this.emailEnviarCotizacion,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				if(this.ccMail!=null && !this.ccMail.trim().isEmpty()){
					validar.validateFormato(errors,"emailContactoConCopia",this.ccMail,false,"^[A-Za-z0-9_]+@[A-Za-z0-9_]|[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]","errors.formato.email");
				}
				validar.validateMandatory(errors,"asuntoMail",this.asuntoMail,"errors.requerido","Asunto");
				validar.validateMandatory(errors,"textoMail",this.textoMail,"errors.requerido","Contenido");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.textoMail", this.textoMail!=null?this.textoMail:"");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.asuntoMail", this.asuntoMail!=null?this.asuntoMail:"");
				request.getSession().setAttribute("ec.com.smx.sic.sispe.paraMail", this.emailEnviarCotizacion!=null?this.emailEnviarCotizacion:"");
				if (errors.size()>0){
					request.getSession().setAttribute(SessionManagerSISPE.MENSAJES_SISPE, "ok");
				}
			}
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>estadoPedido.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n que se est\u00E1 procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{	
			
//			this.ccMail=null;
//			this.emailEnviarCotizacion=null;
//			this.asuntoMail=null;
//			this.textoMail=null;
	}
	
	public String getTextoMail() {
		return textoMail;
	}

	public void setTextoMail(String textoMail) {
		this.textoMail = textoMail;
	}

	public String getCcMail() {
		return ccMail;
	}

	public void setCcMail(String ccMail) {
		this.ccMail = ccMail;
	}

	public String getAsuntoMail() {
		return asuntoMail;
	}

	public void setAsuntoMail(String asuntoMail) {
		this.asuntoMail = asuntoMail;
	}

	public String getEmailEnviarCotizacion() {
		return emailEnviarCotizacion;
	}

	public void setEmailEnviarCotizacion(String emailEnviarCotizacion) {
		this.emailEnviarCotizacion = emailEnviarCotizacion;
	}
}
