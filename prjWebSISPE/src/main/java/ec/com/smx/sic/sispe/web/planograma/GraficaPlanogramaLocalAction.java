package ec.com.smx.sic.sispe.web.planograma;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.integracion.dto.ConexionSistemaExterno;
import ec.com.smx.framework.multicompany.dto.SystemDto;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.util.ComponentesUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

public class GraficaPlanogramaLocalAction extends BaseAction {
	/**
	 * Variables estaticas de forward
	 */
	private final static String FORWARD_SUCCESS = ComponentesUtil.FORWARD_SUCCES;
	private final static String FORWARD_MAIN = ComponentesUtil.FORWARD_MAIN;	
	
	/**
	 * Variables de session
	 */
	private final static String CONEXION = "ec.com.smx.sispe.conexion";
	
	/**
	 * Acci&oacute;n para redireccionar a la gr&aacute;fica del Planograma del Local 
	 * 
	 * @param mapping 			mapeo del archivo struts-config.xml
	 * @param form 				formulario utilizado en la acci&oacute;n
	 * @param request 			petici&oacute;n
	 * @param response 			respuesta
	 * @exception Exception		excepci&oacute;n
	 * @return forward 			a direccionar
	 */	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("::Entrando a Grafica Planograma Local");
		
		//Declaraciones e inicializaciones
		String forward = null;
		HttpSession session     = request.getSession();
		ActionMessages errors   = new ActionMessages();
		
		try {
			//Vista para obtener el local por el usuario loggeado
			VistaEntidadResponsableDTO vistaEntidadResponsableDTO = SessionManagerSISPE.getCurrentEntidadResponsable(request);
			if(vistaEntidadResponsableDTO.getCodigoLocal() != null){
				//codigo del local
				Integer codigoLocal = vistaEntidadResponsableDTO.getCodigoLocal();
				//codigo de la compania
				Integer codigoCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
				//usuario loggeado
				UserDto usuario = SessionManagerSISPE.getDefault().getLoggedUser(request);
				//sistema origen SISPE
				String idSistemaOrigen = MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID");
				SystemDto sistemaOrigen = FrameworkFactory.getMultiCompanyService().getSystemById(idSistemaOrigen);
				//sistema destino B2B
				String idSistemaDestino = MessagesWebSISPE.getString("security.SYSTEM_ID_DESTINO_GRAFICA_PLANOGRAMA");
				//SystemDto sistemaDestino = FrameworkFactory.getMultiCompanyService().getSystemById(idSistemaDestino);
				LogSISPE.getLog().info("Sistema Origen: {} ; Sistema Destino: {}",idSistemaOrigen,idSistemaDestino);
				//token para la conexion
				String token = FrameworkFactory.getSecurityService().getTokenForUser(usuario.getUserId(), codigoCompania, sistemaOrigen.getSystemId());
				//Obtener la url de retorno
				String urlRetorno=obtenerURLRetorno(request, session);
				//Conexion a sistema externo
				ConexionSistemaExterno conexionSisExt = FrameworkFactory.getConexionService().transConstruirConexion(codigoCompania, idSistemaOrigen, idSistemaDestino, token, Integer.toString(codigoLocal), null, null, urlRetorno);
				//ConexionSistemaExterno conexionSisExt = FrameworkFactory.getConexionService().transConstruirConexion(Integer.toString(codigoCompania), idSistemaOrigen, idSistemaDestino, token, Integer.toString(codigoLocal), null, null, MessagesWebSISPE.getString("conexion.url.retornar"));
				//Subir a session la conexion
				session.setAttribute(CONEXION, conexionSisExt);
				forward = FORWARD_SUCCESS;
			}else{
				errors.add("graficaSinLocal", new ActionMessage("errors.graficaPlanogramaLocal.usuarioSinLocal"));
				forward = FORWARD_MAIN;
			}
		} catch (Exception e) {
			//en caso de error, se carga el error general y se envia a la pagina de error estabecida por la accion general
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		
		saveErrors(request,errors);
		LogSISPE.getLog().info("saliendo por: {}",forward);
		return mapping.findForward(forward);
	}
	
	private String obtenerURLRetorno(HttpServletRequest request, HttpSession session) throws  Exception {
        String urlorigen = request.getRequestURL().toString();
        LogSISPE.getLog().info("getRequestURL: " + urlorigen);
        String urlRetorno=null;
        String urlRetornoCompleta = (urlorigen.substring(0, urlorigen.lastIndexOf("/")));
        urlRetorno = urlRetornoCompleta + MessagesWebSISPE.getString("conexion.accion.retornar");
        LogSISPE.getLog().info("UrlRetornoContexto: " + urlRetorno);
        return urlRetorno;
	}
}
