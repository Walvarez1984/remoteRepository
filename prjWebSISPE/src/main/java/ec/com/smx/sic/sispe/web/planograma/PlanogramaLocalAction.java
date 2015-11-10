package ec.com.smx.sic.sispe.web.planograma;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corporativo.commons.factory.CorporativoFactory;
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

public class PlanogramaLocalAction extends BaseAction{
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
	 * Accion de Administracion General de Locales 
	 * 
	 * @param mapping 			mapeo del archivo struts-config.xml
	 * @param form 			formulario utilizado en la acci&oacute;n
	 * @param request 			petici&oacute;n
	 * @param response 			respuesta
	 * @exception Exception		excepci&oacute;n
	 * @return forward 			a direccionar
	 */	

	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception {
		LogSISPE.getLog().info("::Entrando a Planograma");

		//Declaraciones e inicializaciones
		HttpSession session     = request.getSession();
		ActionMessages errors   = new ActionMessages();	 // almacena los mensajes de error
		ActionMessages infos    = new ActionMessages();	 // almacena los mensajes de alerta  	  	
		ActionMessages messages = new ActionMessages();  // almacena los mensajes        	
		String forward = null;	  	       	  	      	           	

		try{
			VistaEntidadResponsableDTO vistaEntidadResponsable = SessionManagerSISPE.getCurrentEntidadResponsable(request);
			
			String codigoLocal = vistaEntidadResponsable.getCodigoLocal().toString();
			if(codigoLocal!=null){
				LocalID localID = new LocalID();
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				localID.setCodigoLocal(new Integer(codigoLocal));
				LocalDTO local = CorporativoFactory.getParametrosService().findLocalById(localID);
				
				//Recuperar el usuario de sesi\u00F3n
				UserDto userDTO = SessionManagerSISPE.getDefault().getLoggedUser(request);
				//Obtengo el id de la compania
				Integer codigoCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
				//Obtengo el id del sistema origen en este caso SISPE
				String idSistemaOrigen = MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID");
				//Obtengo el sistema Origen
				SystemDto systemOrigenDTO = FrameworkFactory.getMultiCompanyService().getSystemById(idSistemaOrigen);
				LogSISPE.getLog().info("sistema Origen: {}", systemOrigenDTO.getSystemId());
				//Obtengo el id del sistema destino en este caso B2BI
				String idSistemaDestino = MessagesWebSISPE.getString("security.SYSTEM_ID_DESTINO_PLANOGRAMA");
				//Obtengo el sistema Origen
				SystemDto systemDestinoDTO = FrameworkFactory.getMultiCompanyService().getSystemById(idSistemaDestino);
				LogSISPE.getLog().info("sistema Origen: {}", systemDestinoDTO.getSystemId());
				//Obtengo el token para la conexi\u00F3n
				String token = FrameworkFactory.getSecurityService().getTokenForUser(userDTO.getUserId(), codigoCompania, systemOrigenDTO.getSystemId());
				//Si existen compradores para el usuario actual le envio a la pantalla de envio de mails
				if(local!=null){
					//Obtengo la Conexion a otro Sistema
					ConexionSistemaExterno conexion= FrameworkFactory.getConexionService().transConstruirConexion(codigoCompania.intValue(), idSistemaOrigen, idSistemaDestino, token, local.getId().getCodigoLocal().toString(), null, null, MessagesWebSISPE.getString("conexion.url.retornar"));
																						   //transConstruirConexion(String codigoCompania, String codigoSistemaOrigen, String codigoSistemaDestino,  String token, String codigoUnico, String nombreUnico, String descripcionUnico, String urlRetornar)
					//CORPMSGSystem.print("URL: " + conexion.getUrlConexion());
					//Subir a sesi\u00F3n la Conexion B2BI				    
					session.setAttribute(CONEXION, conexion);
					forward = FORWARD_SUCCESS;
				}
				//Si no existen compradores para el usuario actual se queda en el panel principal
				else{
					errors.add("WorkFlow", new ActionMessage("info.usuario.sin.compradores",userDTO.getUserCompleteName()));
					forward = FORWARD_MAIN;
				}
			}
		}catch( Exception e ){	
			// en caso de error, se carga el error general y se envia a la pagina de error estabecida por la accion general:
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}

		return  toForward( forward, mapping, request, errors, infos, messages );
	}

	/**
	 * Permite redireccionar la accion al forward establecido
	 * 
	 * @param forward  .- forward al cual se direcciona la accion
	 * @param mapping  .- mapping de la accion
	 * @param request  .- request de la llamada de la accion
	 * @param errors   .- mensajes de errores
	 * @param infos    .- mensajes de alerta
	 * @param messages .- mensajes adicionales del proceso
	 * @return
	 */
	private final ActionForward toForward( String forward, ActionMapping mapping, HttpServletRequest request, ActionMessages errors, ActionMessages infos, ActionMessages messages ){
		LogSISPE.getLog().info("saliendo por: {}", forward);
		saveErrors(request,errors);
		saveMessages(request,messages);
		saveInfos(request, infos);
		return mapping.findForward(forward);
	}     
}
