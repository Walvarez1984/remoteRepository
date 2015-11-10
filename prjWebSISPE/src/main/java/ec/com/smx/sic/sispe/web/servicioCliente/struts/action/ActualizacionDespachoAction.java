/*
 * ActualizacionDespachoAction.java
 * Creado el 27/05/2008 14:53:10
 *   	
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * 	Esta clase permite actualizar las cantidades de los art\u00EDculos que se van a despachar, maneja dos tags 
 * 	que muestran distintas secciones del formulario.
 * 	<ol>
 * 		<li>ARTICULOS: En esta secci\u00F3n se procesan las cantidades ingresadas que actualizar\u00E1n el despacho.</li>
 * 		<li>PEDIDOS:   En est\u00E1 secci\u00F3n se escogen los pedidos que ser\u00E1n afectados por la acualizaci\u00F3n. Si no se
 * 				esoge alguno, se actualizar\u00E1n los pedidos en orden de fecha de despacho.</li>
 * 	</ol>
 * </p>
 * @author 	fmunoz
 * @version	3.0
 * @since 	JSDK 1.5
 */
public class ActualizacionDespachoAction extends ActualizacionProduccionAction
{
	//variable que almacena el modo de control de producci\u00F3n en base a la bodega
	public static final String MODO_ACTUALIZAR_DESPACHO = "ec.com.smx.sic.sispe.bodega.modoControlProduccion"; 
	public static final String LOCAL_CON_DIFERENTES_DESTINOS = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("despachoPerecibles.localConDiferentesDestinos");
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		//se sube a sesi\u00F3n la variable correspondiente
		session.setAttribute(MODO_ACTUALIZAR_DESPACHO, 
				MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.procesoBodega.modoActualizarDespacho"));

		//se llama al m\u00E9todo que ejecuta la l\u00F3gica principal
		return super.execute(mapping, form, request, response);
	}
}
