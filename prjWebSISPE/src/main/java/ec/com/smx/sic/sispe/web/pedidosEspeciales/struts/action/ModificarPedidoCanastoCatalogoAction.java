package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author osaransig
 * Nov 29, 2013
 */
public class ModificarPedidoCanastoCatalogoAction extends BaseAction {

	
	@SuppressWarnings("unused")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}", peticion);
		
		//Consulto la accion actual
		String accion = (String) request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);

		String forward = "modificarPedidoEspecial";
		
		try {
			request.getSession().setAttribute(PedidoCanastoCatalogoAction.PEDIDO_ESPECIAL_CANASTO_CATALOGO, "ok");
			MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), request.getSession());
			
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n", e);
			forward = "errorGlobal";
		}
		
		return mapping.findForward(forward);
	}
	
	
}
