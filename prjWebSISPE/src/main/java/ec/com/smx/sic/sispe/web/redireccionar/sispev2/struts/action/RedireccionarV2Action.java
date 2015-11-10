package ec.com.smx.sic.sispe.web.redireccionar.sispev2.struts.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.constantes.ConstantesGlobales;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.RedireccionarJSF;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * 
 * @author cpiedra
 *
 */
public class RedireccionarV2Action extends BaseAction {
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
			HttpServletResponse response) throws Exception 
			{
		LogSISPE.getLog().info(" Ingresa redireccionar");	
		ActionMessages infos = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		String forward = ConstantesGlobales.FORWARD_REDIRECT;
		String servidor=obtenerServidor(request, session);
		//Cosnultandop el sistema sispev2

		//Parametros para obtener el url de la pagina a redireccionar
		String claseContactoSispe="";
        ParametroDTO parametroDTO = new ParametroDTO(Boolean.TRUE);
        parametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
        parametroDTO.getId().setCodigoParametro(MessagesWebSISPE.getString("codigo.parametro.url"));
        Collection parametrosDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(parametroDTO);
       
        if(!parametrosDTO.isEmpty()){
            parametroDTO=(ParametroDTO) parametrosDTO.iterator().next();
            //se obtiene el url de la pagina a redireccionar
            claseContactoSispe = parametroDTO.getValorParametro();
        }
        //valida que el parametro no sea nulo
		if(request.getParameter(ConstantesGlobales.OPCION)!=null ){
			LogSISPE.getLog().info("calendarioHoras", request.getParameter("calendario"));
			//Metodo para formar el url con los parametros que se necesitamn
			RedireccionarJSF.obtenerInstancia().formarUrlRetorno
					(claseContactoSispe.concat("?").concat(ConstantesGlobales.PAGINA_REDIRECCIONAR)
								.concat("=").concat(request.getParameter(ConstantesGlobales.OPCION)), request, session);
		}else{
		LogSISPE.getLog().info("No se encontro ningun parametro para redireccionar {}", request.getParameter("calendario"));
		RedireccionarJSF.obtenerInstancia().formarUrlRetorno(servidor+ConstantesGlobales.URL_REDIRECCION.concat("?"), request, session);
		}
		LogSISPE.getLog().info("-- Sale por: {}",forward);	
		saveInfos(request, infos);
		saveErrors(request, errors);
		return mapping.findForward(forward);

	}	
	
	/**
	 * Metodo para obtener el nomebre del servidor que se encuentra en ese momnento //http://localhost:8080
	 * @param request
	 * @param session
	 * @return String
	 * @throws Exception
	 */
	public String obtenerServidor(HttpServletRequest request, HttpSession session) throws Exception {
		String urlorigen = request.getRequestURL().toString();
		LogSISPE.getLog().info("url Retorno:{}",urlorigen);
		String puertoServidor = (urlorigen.substring(0, urlorigen.indexOf(MessagesWebSISPE.getString("conexion.servidor"))));
		LogSISPE.getLog().info("servido: {} ",puertoServidor );
		return puertoServidor;
	}
	
	
}
