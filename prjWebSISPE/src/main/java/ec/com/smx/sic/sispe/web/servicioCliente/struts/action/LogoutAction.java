/*
 * LogoutAction.java
 * Creado el 06/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase termina la sesi\u00F3n de un usuario y envia el control a la acci\u00F3n login.do
 * </p>
 * @author 	bmontesdeoca
 * @version	1.0
 * @since 	JSDK 1.4.2
 */
public class LogoutAction extends BaseAction
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
  public ActionForward execute(ActionMapping mapping,ActionForm form, 
    HttpServletRequest request,HttpServletResponse response) 
  	throws Exception 
  {
    //Declaraci\u00F3n de variables.
    String forward="salir";
    try{
    	SessionManagerSISPE.removeAllVarSession(request);
    	SessionManagerSISPE.getDefault().LogoutUser(request);      
    }catch(Exception ex){
      request.getSession().invalidate();
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    }
    
    return mapping.findForward(forward);
  }
}
