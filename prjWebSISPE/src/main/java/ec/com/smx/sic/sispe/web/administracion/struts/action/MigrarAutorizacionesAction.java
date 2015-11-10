/*
 * MigrarAutorizacionesAction.java
 * Creado el 01/10/2013 09:37:06
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.MigrarAutorizacionesForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author bgudino
 *
 */
public class MigrarAutorizacionesAction extends BaseAction {
	/**
	 * Clase ofrece: 
	 * bgudino
	 * 09:37:06
	 * version 0.1
	 *  	
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		    //Declaraciones Iniciales
		    ActionMessages errors = new ActionMessages();
		    ActionMessages exitos = new ActionMessages(); 
		    HttpSession session = request.getSession();
		    
		    MigrarAutorizacionesForm formulario = (MigrarAutorizacionesForm) form; 
		    
		    session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
		    
		    String salida = "desplegar";
		    try{
		    	if(request.getParameter("migrarAutorizaciones") != null){
		    		String codigoPedido = null;
		    		if(StringUtils.isNotEmpty(formulario.getCodigoPedido())){
		    			codigoPedido =  formulario.getCodigoPedido().trim();
		    			LogSISPE.getLog().info("codigo pedido a migrar: "+codigoPedido);
		    		}
		    		 
		        	LogSISPE.getLog().info("se procede a migrar las autorizaciones de descuento variable a linea comercial");
		        	SessionManagerSISPE.getServicioClienteServicio().transMigrarAutorizacionesLineaComercial(codigoPedido);
		        	exitos.add("migradoCorrectamente", new ActionMessage("errors.gerneral", "Las autorizaciones de descuento variable se han migrado correctamente") );
		        	formulario.setCodigoPedido(null);
		        }else if(request.getParameter("corregirAutorizaciones") != null){
		        	String codigoPedido = null;
		    		if(StringUtils.isNotEmpty(formulario.getCodigoPedidoCorregir())){
		    			codigoPedido =  formulario.getCodigoPedidoCorregir().trim();
		    			LogSISPE.getLog().info("codigo pedido a corregir las llaves de descuento: "+codigoPedido);
		    		}
		        	LogSISPE.getLog().info("corrigiendo las llaves de las autorizaciones migradas");
		        	SessionManagerSISPE.getServicioClienteServicio().transCorregirMigracionAutorizacionesLineaComercial(codigoPedido);
		        	exitos.add("migradoCorrectamente", new ActionMessage("errors.gerneral", "Las llaves de descuento variable se han corregido correctamente") );
		        	formulario.setCodigoPedidoCorregir(null);
		        }

		    }catch(Exception ex){
		      salida = "desplegar";
		      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		      errors.add("noMigrado", new ActionMessage("errors.gerneral", "Las autorizaciones de descuento variable NO se han migrado, "+ex.getMessage()) );
		    }
		    return forward(salida, mapping, request, errors, exitos);
//		    return mapping.findForward(salida);
		  }
	
	private final ActionForward forward(String forward, ActionMapping mapping, HttpServletRequest request, ActionMessages errors, 
			ActionMessages exitos) {
		LogSISPE.getLog().info("saliendo por:" + forward);
				
		saveErrors(request, errors);
		saveMessages(request, exitos);
		return mapping.findForward(forward);
	}
}
