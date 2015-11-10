package ec.com.smx.sic.sispe.web.workListBPM.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

public class WorkListBPMAction extends BaseAction  {
	
	/**
	 * Accion para redireccionar a la pantalla de workListBPM
	 * 
	 * @param mapping 			mapeo del archivo struts-config.xml
	 * @param form 			    formulario utilizado en la acci&oacute;n
	 * @param request 			petici&oacute;n
	 * @param response 			respuesta
	 * @exception Exception		excepci&oacute;n
	 * @return forward 			a direccionar
	 */	

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("Entrando a WorkListAction");

		//Declaraciones e inicializaciones
		//HttpSession session     = request.getSession();
       	
		String forward = null;	  	       	  	      	           	

		try{   	        	   
			
			LogSISPE.getLog().info("Ingreso a cargar WorkListBPM" );
			forward = "desplegar";

		}catch( Exception e ){	
			// en caso de error, se carga el error general y se envia a la pagina de error estabecida por la accion general:
			LogSISPE.getLog().error("Error metodo execute{}",e);
			forward = "error";
		}

		return  toForward( forward, mapping, request);
	}

	/**
	 * Permite redireccionar la accion al forward establecido
	 * 
	 * @param forward  .- forward al cual se direcciona la acci\u00F3n
	 * @param mapping  .- mapping de la accion
	 * @param request  .- request de la llamada de la accion
	 * @return
	 */
	private final ActionForward toForward( String forward, ActionMapping mapping, HttpServletRequest request){
		LogSISPE.getLog().info("saliendo por: {}",forward);
		return mapping.findForward(forward);
	}     
}
