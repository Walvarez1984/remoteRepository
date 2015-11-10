/**
 * 
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase muestra un listado de los pedidos que deben ser confirmados como reservaci\u00F3n, es decir pedidos 
 * que ya est\u00E1n producidos pero puden variar sus costos debido a art\u00EDculos de peso variable.
 * </p>
 * @author 	fmunoz
 * @version 2.0
 * @since		JSDK 1.5.0
 */
public class ListaConfirmarReservacionAction extends ListadoPedidosAction
{
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request 				La petici&oacute;n que estamos procesando
   * @param response				La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)throws Exception{
		//se guarda la acci\u00F3n actual
		request.getSession().setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"));

		//se ejecuta la acci\u00F3n padre
		return super.execute(mapping, form, request, response);
  }
  
}

