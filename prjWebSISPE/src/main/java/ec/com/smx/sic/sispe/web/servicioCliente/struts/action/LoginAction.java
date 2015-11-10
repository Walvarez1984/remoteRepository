/*
 * LoginAction.java
 * Creado el 23/03/2006
 * Supermaxi
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.exception.FrameworkException;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase controla el logeo del usuario antes de ingresar al sistema, realiza la validaci\u00F3n de los datos 
 * ingresados. Si los datos son correctos se env\u00EDa el control al Men\u00FA principal, caso contrario se muestra un 
 * mensaje de error indicando que los datos son incorrectos. 
 * 
 * @author 	bmontesdeoca, fmunoz, mbraganza, cbarahona
 * @version 2.0
 * @since 	JSDK 1.4.2
 */
public class LoginAction extends BaseAction
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control 
   * (si la respuesta no se ha completado retorna <code>null</code>)
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacute;n que estamos procesando
   * @param response		La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, 
      HttpServletResponse response) throws Exception{
    
    ActionMessages messages = new ActionMessages();
    String login = request.getParameter("login");
    String password = request.getParameter("password");
    
    String salida = "login";
    UserDto userDTO = null;
    //verificamos si el usuario esta logueado desde otro sistema
    if(SessionManagerSISPE.getDefault().verifySessionByToken(request, response)){
    	salida = "createSession";
    }
    else if(login != null && password != null){
      try{
      	userDTO = SessionManagerSISPE.getDefault().loginUser(login, password,null, request, response).getUser();
        if(userDTO == null){
          throw new FrameworkException("login falla");
        }
        salida = "createSession";
        
      }catch(Exception ex){
        LogSISPE.getLog().info("Error en login por password y usuario");
        messages.add("name", new ActionMessage("errors.login.claveInvalida"));
        LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      }
    }else
      messages.add("name", new ActionMessage("errors.login.claveInvalida"));
    
    saveErrors(request, messages);
    
    return (mapping.findForward(salida));
  }
  
  
}
