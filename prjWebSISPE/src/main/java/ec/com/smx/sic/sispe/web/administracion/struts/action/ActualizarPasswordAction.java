/*
 * Clase ActualizarPasswordAction.java
 * Creado el 09/11/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ActualizarPasswordForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Permite realizar la actualizaci\u00F3n de la contrase\u00F1a para los usuarios del sistema.
 * 
 * @author 	fmunoz
 * @version 1.0
 * @since	JSDK 1.4.2	 	
 */
public class ActualizarPasswordAction extends BaseAction
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) 
   * o lo redirige a otro componente web que podr\u00EDa crear. Devuelve una instancia <code>ActionForward</code>
   * que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping				El mapeo utilizado para seleccionar esta instancia
   * @param request				El request que estamos procesando
   * @param form				El formulario (si lo hay) asociado a la acci\u00F3n que estamos utilizando	
   * @param response			La respuesta HTTP que se crea
   * @return ActionForward		Describe donde y como se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
  throws Exception
  {
    //Declaraciones Iniciales
    ActionMessages errors= new ActionMessages(); 
    ActionMessages messages= new ActionMessages();
    ActualizarPasswordForm formulario =(ActualizarPasswordForm)form;
    HttpSession session = request.getSession();
    
    String salida = "desplegar";
    try{
      if(formulario.getBotonGuardar()!=null)
      {
      	//se obtienen las expresiones que servir\u00E1n como pol\u00EDtica de validaci\u00F3n para el nuevo usuario
      	String expresion1 = FrameworkFactory.getSecurityService().getPolicyValue("fwk_formato_username");
      	String expresion2 = FrameworkFactory.getSecurityService().getPolicyValue("fwk_formato_password");
        LogSISPE.getLog().info("se verifica el usuario");
        LogSISPE.getLog().info("expresi\u00F3n regular para el login: "+expresion1);
        LogSISPE.getLog().info("expresi\u00F3n regular para el password: "+expresion2);
        UserDto user= SessionManagerSISPE.getDefault().getLoggedUser(request);
        if(user.getUserPassword().equals(formulario.getPasswordActual())){
        	//se verifica la igualdad en la confirmaci\u00F3n del password
          if(formulario.getNuevoPassword().equals(formulario.getConfirmarPassword())){
          	//se verifica la validez del password, es decir que cumpla el formato de la expresi\u00F3n regular
          	if(formulario.getNuevoPassword().matches(expresion2)){
          		user.setUserPassword(formulario.getNuevoPassword());
          		FrameworkFactory.getSecurityService().updateUser(user, expresion1, expresion2);
          		LogSISPE.getLog().info("mostrar popUp cambio correcto de contrasenia");
    			instanciarPopUpConfirmacion(request);
          		//messages.add("actualizaLoginEmpresa", new ActionMessage("messages.actualizacionPassword.exito",user.getUserName()));
          	}else{
          		//se lanza un mensaje de error de clave inv\u00E1lida
          		String minimoCaracteres = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.password.minimoCaracteres");
          		String minimoMayusculas = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.password.minimoMayusculas");
          		String minimoNumeros = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.password.minimoNumeros");
          		errors.add("claveInvalida", new ActionMessage("errors.actualizarPassword.noValida",formulario.getNuevoPassword(),
          				minimoCaracteres, minimoMayusculas, minimoNumeros));
          	}
          }else{
            errors.add("actualizaLoginEmpresa", new ActionMessage("errors.actualizarPassword.confirmacion"));
          }
        }else{
        	errors.add("actualizaLoginEmpresa", new ActionMessage("errors.usuario.password"));
        }
        //se guardan los mensajesz
        saveErrors(request,errors);
        saveMessages(request,messages);
      }
      else if(formulario.getBotonCancelar()!=null){
        //se eliminan las variables de sesi\u00F3n del sistema
        SessionManagerSISPE.removeVarSession(request);	
        salida="menuPrincipal";
      }
      else{
        int mostrarPregunta = CotizacionReservacionUtil.verificarPregunta(request,"cambiarClave");
        if(mostrarPregunta == 1){
          messages.add("confirmacion",new ActionMessage("message.confirmarCambioAccion",session.getAttribute("WebSISPE.tituloVentana")));
          request.setAttribute("ec.com.smx.sic.sispe.confirmacion.mensaje",messages);
          salida = "confirmacion";
        }
      }
      if(request.getParameter("cambioContrasenia") != null){
			
			LogSISPE.getLog().info("Cerrando el popUp de cambio de contrasenia");
			request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
			salida = "menuPrincipal";
      }
     
    }catch(Exception ex){
      salida = "errorGlobal";
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    }
    return mapping.findForward(salida);
  }
  
  /**
	 * PopUp de confirmacion de cambio de contrasenia
	 * @param request
	 */
	public static void instanciarPopUpConfirmacion(HttpServletRequest request){
		
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Confirmaci\u00F3n");
		popUp.setMensajeVentana(MessagesWebSISPE.getString("messages.actualizacionPassword.exito"));
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setValorOK("requestAjax('cambiarClave.do',['mensajes','div_pagina'],{parameters: 'cambioContrasenia=ok',evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorOK());
		popUp.setAncho(Double.valueOf(30));
		popUp.setFormaBotones(UtilPopUp.OK);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		
		//se guarda la ventana
		request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
}