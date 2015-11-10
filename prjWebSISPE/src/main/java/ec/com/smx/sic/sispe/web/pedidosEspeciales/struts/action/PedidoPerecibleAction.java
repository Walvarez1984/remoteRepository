package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.CrearPedidoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author osaransig
 *
 */
public class PedidoPerecibleAction extends BaseAction {

	public static final String PEDIDO_ESPECIAL_PERECIBLE = "sispe.pedido.pavos";
	
	@SuppressWarnings("unused")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		ActionMessages messages = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();

		CrearPedidoForm formulario = (CrearPedidoForm) form;
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);
		
		String opTipoPedido= formulario.getOpTipoPedido();
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);

		//Consulto la accion actual
		String accion = (String) request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);

		String forward = "pedidoEspecial";
		
		try {
			request.getSession().setAttribute(PEDIDO_ESPECIAL_PERECIBLE, "ok");
			
			
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n", e);
			forward = "errorGlobal";
		}
		
		return mapping.findForward(forward);
	}
	
	
}
