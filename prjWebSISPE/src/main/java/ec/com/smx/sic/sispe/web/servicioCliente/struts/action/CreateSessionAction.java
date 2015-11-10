/*
 * CreateSessionAction.java
 * 02/02/2007
 * Supemaxi
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.CompaniaDTO;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.multicompany.dto.SystemDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author  mbraganza, fmunoz
 * @version 1.0
 * @since	JSDK 1.4.2
 */
public class CreateSessionAction extends BaseAction {
  
	/**
	 * 
	 */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, 
      HttpServletResponse response) throws Exception{
    
	  
    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    boolean redireccion = session.getAttribute("loginDirecto") != null;
    LogSISPE.getLog().info("loginDirecto en SISPE resultado: {}" , redireccion);
    
    String forward = !redireccion ? "principal" : "menuPrincipal";
    CompaniaDTO companiaDTO = null;
    
    LogSISPE.getLog().info("Duracion de la sesion SISPE: {}" , request.getSession().getMaxInactiveInterval());
    try
    {
    	//se obtiene el valor del par\u00E1metro que indica si est\u00E1 activado el debug de la aplicaci\u00F3n
    	//ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.estadoEscrituraMensajesLog", request);
    	//LogSISPE.setBanderaHabilitarDebug(parametroDTO.getValorParametro());
    	
    	//se cargan los datos del sistema
      SystemDto systemDTO = FrameworkFactory.getMultiCompanyService().getSystemById(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
      SessionManagerSISPE.getDefault().setCurrentSystem(request,systemDTO);
      LogSISPE.getLog().info("SYSTEM DESCRIPTION : {}" , SessionManagerSISPE.getDefault().getCurrentSystem(request).getSystemDescription());
      LogSISPE.getLog().info("SYSTEM ID: {}" , SessionManagerSISPE.getDefault().getCurrentSystem(request).getSystemId());

      //se cargan los datos de la compa\u00F1ia
      companiaDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findCompaniaById(new Integer(MessagesWebSISPE.getString("security.CURRENT_COMPANY_ID")));
      SessionManagerSISPE.setCurrentCompanys(request,companiaDTO);
      LogSISPE.getLog().info("Codigo del Compan\u00EDa: {}" , companiaDTO.getId().getCodigoCompania());

      //se guarda en sesi\u00F3n el c\u00F3digo de la excepci\u00F3n que indica que una transacci\u00F3n debe ser registrada
      //nuevamente
      String codExceptionRegistrarNuevamente = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigo.error.sincronizacionBaseDatos");
      SessionManagerSISPE.setCodExceptionRegistrarNuevamente(request,codExceptionRegistrarNuevamente);
      LogSISPE.getLog().info("codigo.error.sincronizacionBaseDatos: {}",SessionManagerSISPE.getCodExceptionRegistrarNuevamente(request));
      
      //se sube a sesi\u00F3n el estado activo e inactivo que viene desde la aplicaci\u00F3n
      SessionManagerSISPE.setEstadoActivo(request);
      SessionManagerSISPE.setEstadoInactivo(request);
      //se sube a sesi\u00F3n la variable que contiene el porcentaje del iva
      SessionManagerSISPE.setValorIVA(request);

      //se obtiene la VistaEntidadResponsableDTO y se la guarda en sesi\u00F3n
      VistaEntidadResponsableDTO vistaEntidadResponsableDTO = new VistaEntidadResponsableDTO();
      vistaEntidadResponsableDTO.getId().setCodigoCompania(companiaDTO.getId().getCodigoCompania());
      vistaEntidadResponsableDTO.getId().setIdUsuario(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
      try{
    	  vistaEntidadResponsableDTO = SISPEFactory.obtenerServicioSispe().transObtenerVistaEntidadResponsableDTO(vistaEntidadResponsableDTO);
    	  LogSISPE.getLog().info("TIPO ESTABLECIMIENTO: {}",vistaEntidadResponsableDTO.getCodigoEstablecimiento());
          LogSISPE.getLog().info("CIUDAD: {}",vistaEntidadResponsableDTO.getNombreCiudad());
          //subo la entidad responsable
          SessionManagerSISPE.setCurrentEntidadResponsable(request,vistaEntidadResponsableDTO);
      }catch (Exception e) {
    	  LogSISPE.getLog().info("***********SIN ENTIDAD RESPONSABLE*****************");
    	 LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
      }  
      
      LogSISPE.getLog().info("ID de sesi\u00F3n en SISPE (Prueba): {}" , session.getId());
      LogSISPE.getLog().info("Prueba de sesi\u00F3n en SISPE: {}" , session.getAttribute("prueba1"));
    }
    catch(Exception ex){ //salida = login
      //messages.add("name", new ActionMessage("errors.login.claveInvalida"));
      forward = "usuarioNoAutorizado";
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    }

    saveErrors(request, messages);
    return mapping.findForward(forward);
    
  }
  
}
