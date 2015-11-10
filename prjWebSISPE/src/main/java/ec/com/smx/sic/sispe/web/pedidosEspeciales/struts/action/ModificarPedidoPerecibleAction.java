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
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author osaransig
 *
 */
public class ModificarPedidoPerecibleAction extends BaseAction {

	public static final String 	MODIFICACION_PEDIDO_PAVOS = "sispe.pedido.pavos.modificacion";
	
	@SuppressWarnings("unused")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);
		
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		//Consulto la accion actual
		String accion = (String) request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);

		String forward = "modificarPedidoEspecial";
		
		try {
			request.getSession().setAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE, "ok");
			MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), request.getSession());
			
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n", e);
			forward = "errorGlobal";
		}
		
		return mapping.findForward(forward);
	}
	
	
}
