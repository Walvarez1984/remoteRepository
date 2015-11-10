/*
 * MenuPrincipalAction.java
 * Creado el 23/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.common.logging.LogFramework;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase env\u00EDa el control a la p\u00E1gina <code>menuPrincipal.jsp</code> donde se muestra el men\u00FA principal
 * de la aplicaci\u00F3n.
 * </p>
 * @author 	bmontesdeoca, fmunoz, mbraganza
 * @version 2.0
 * @since	JSDK 1.4.2
 */
public class MenuPrincipalAction extends BaseAction 
{
	LogFramework log = new LogFramework(MenuPrincipalAction.class, true);

	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
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
			HttpServletRequest request, HttpServletResponse response)throws Exception{

		HttpSession session = request.getSession();
		String forward = "menu";
		
		/*------------------------------- se verifica si el usuario tiene sesi\u00F3n ----------------------------------*/
		
		//Verificaci\u00F3n de sesi\u00F3n por token
		if (SessionManagerSISPE.getDefault().verifySessionByToken(request, response)) {
			//log.println("Prueba de sesi\u00F3n en SISPE: " + session.getAttribute("prueba1"));
			session.setAttribute("loginDirecto", Boolean.TRUE);
			forward = "createSession";
		} 
		//Verificaci\u00F3n de sesi\u00F3n por usuario y password
		else if(!SessionManagerSISPE.getDefault().verifySession(request)){
			forward = "login";
		}
		else if(request.getParameter("salir")!=null){
			//se eliminan las posibles variables y formularios de sesi\u00F3n
			SessionManagerSISPE.removeVarSession(request);
			SessionManagerSISPE.removeFormSession(request);
			ContactoUtil.eliminarDataManagersSessionCorp(request);
		}
		else{
			//se eliminan las posibles variables y formularios de sesi\u00F3n
			SessionManagerSISPE.removeVarSession(request);
			SessionManagerSISPE.removeFormSession(request);
			ContactoUtil.eliminarDataManagersSessionCorp(request);
		}

		request.getSession().removeAttribute("sispe.pedido.pavos");
		//se activa el men\u00FA principal
		MenuUtils.activarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
		//log.println("MENU SISPE:"+session.getAttribute("menus"));
		try{
			log.println("Verificar sesion corp:");
			ContactoUtil.getTokenCorp(request);
		}catch (Exception ex){
			log.println("Si verificar sesion corp es nula o ya se perdi\u00F3: "+ex);
			forward = "logout";
		}
		log.println("SALIDA MENU SISPE:"+forward);
		return mapping.findForward(forward);
	}

}