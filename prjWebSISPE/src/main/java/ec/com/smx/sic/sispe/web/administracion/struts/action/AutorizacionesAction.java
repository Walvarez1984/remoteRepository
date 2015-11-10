/*
 * Clase AutorizacionesAction.java 
 * Creado el 12/05/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.corporativo.admparamgeneral.dto.bo.ConexionCorpBO;
import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.corporativo.commons.util.ParametroConexionAutorizacionesEnum;
import ec.com.smx.corporativo.commons.util.ParametroConexionCorpEnum;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.multicompany.dto.SystemDto;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.util.ComponentesUtil;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Permite la manipulaci\u00F3n de datos de Autorizaciones y generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @author 	cbarahona
 * @version 1.0
 * @since	JSDK 1.4.2 
 */
public class AutorizacionesAction extends BaseAction {
	public static final String SESS_VAR_CONEXIONBO = "system.conexionBO";
	

  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Mostrar el listado de Autorizaciones</li>
   * <li>Acceso a la Creaci\u00F3n de una Nueva Autorizaci\u00F3n</li>
   * <li>Acceso a la Actualizaci\u00F3n de una Autorizaci\u00F3n</li>
   * </ul>
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request			El request que estamos procesando
   * @param response 		La respuesta HTTP que se crea
   * @throws Exception
   * @return ActionForward	Describe donde y como se redirige el control
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
      {
	  	//HttpSession session = request.getSession();
	    //Recuperar el usuario de sesi\u00F3n
	    UserDto userDTO = SessionManagerSISPE.getDefault().getLoggedUser(request);
	    Integer idCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
	    //Obtengo el sistema
	    SystemDto systemDTO = FrameworkFactory.getMultiCompanyService().getSystemById( CorporativoConstantes.SYSTEMID_CORPORATIVO);
	    String token = FrameworkFactory.getSecurityService().getTokenForUser(userDTO.getUserId(), idCompania, 
			     systemDTO.getSystemId());
	    String sistemaSISPE = CorporativoConstantes.SYSTEMID_SISPE;
	   
	    
	    //String codigoAreaTrabajo = null;
	    String grupoAutorizacion = null;
	    
	    //se obtienen los c\u00F3digos de los formatos de negocio activos
	    VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = new VistaEstablecimientoCiudadLocalDTO();
	    vistaEstablecimientoCiudadLocalDTO.getId().setCodigoCompania(idCompania);
	    String codigosEstablecimientosHabilitados = SessionManagerSISPE.getServicioClienteServicio().transObtenerCodigosEstablecimientosHabilitados(vistaEstablecimientoCiudadLocalDTO);   
	    String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
	    String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");

	    
	    VistaEntidadResponsableDTO vistaEntidadResponsableDTO = SessionManagerSISPE.getCurrentEntidadResponsable(request);
	    //si encuentra una entidad responsable
	    if(vistaEntidadResponsableDTO != null){
	    	LogSISPE.getLog().info("***Autorizaciones, Tiene entidad responsable");
	    	//si la entidad es un local
		    if(vistaEntidadResponsableDTO.getTipoEntidadResponsable().equals(entidadLocal)){
		    	//codigoAreaTrabajo = String.valueOf(vistaEntidadResponsableDTO.getId().getCodigoAreaTrabajo());
		    	grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.local");
		    	
		    }else if(vistaEntidadResponsableDTO.getTipoEntidadResponsable().equals(entidadBodega)){
		    	grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.bodega");
		    }
	    }
	    //si no tiene una entidad responsable, valida por Rol de usuario
	    else{
	    	LogSISPE.getLog().info("***Autorizaciones, No tiene entidad responsable, valida por rol de usuario");
	    	ParametroID parametroID = new ParametroID();
	    	ParametroDTO parametroDTO = null;
	    	UserDto usuario = null;
	    	//obtengo parametro con el rol de gerente comercial
	    	parametroID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    	parametroID.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoRolGerenteComercial"));
	    	//Obtiene usuario loggeado
	    	usuario = SessionManagerSISPE.getDefault().getLoggedUser(request);
	    	if(usuario != null){
	    		Collection<String> roles = new ArrayList<String>();
	    		parametroDTO = SISPEFactory.obtenerServicioSispe().transObtenerParametroPorID(parametroID);
	    		roles.add(parametroDTO.getValorParametro());
	    		//si el rol del usuario loggeado es de un gerente comercial
	    		if(FrameworkFactory.getSecurityService().transValidateUserInRoles(usuario.getUserId(), roles)){
	    			LogSISPE.getLog().info("***Autorizaciones para gerente comercial");
    				grupoAutorizacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.grupoAutorizacion.gerenciaComercial");
	    		}
	    	}
	    }
	    
	    //Conexion BO
	    ConexionCorpBO conexionCorpBO = SessionManagerSISPE.getCorpAutorizacionesServicio().transConstruirConexionBO(idCompania.intValue(), sistemaSISPE, token);
	    //Parametro de url de destino			  
		conexionCorpBO.agregarParametro(ParametroConexionCorpEnum.URL_DESTINO.getCodigoParametro(),MessagesWebSISPE.getString("url.relativo.corporativo.autorizaciones"));
		//Parametro de url de origen			  
		String urlRetorno = ComponentesUtil.getInstancia().obtenerUrlAbsolutaContexto(request).concat("/").concat(MessagesWebSISPE.getString("url.relativo.sispe.principal"));
		conexionCorpBO.agregarParametro(ParametroConexionCorpEnum.URL_RETORNO.getCodigoParametro(),urlRetorno);
		
	    conexionCorpBO.agregarParametro(ParametroConexionAutorizacionesEnum.CODIGO_ESTABLECIMIENTOSHABILITADOS.getCodigoParametro(), codigosEstablecimientosHabilitados);
	    //conexionCorpBO.agregarParametro(ParametroConexionAutorizacionesEnum.CODIGO_AREA_TRABAJO.getCodigoParametro(), codigoAreaTrabajo);
	    conexionCorpBO.agregarParametro(ParametroConexionAutorizacionesEnum.CODIGO_GRUPOAUTORIZACION.getCodigoParametro(), grupoAutorizacion);

	    LogSISPE.getLog().info("URL: " + conexionCorpBO.getUrlConexion());
 	    //enviar al sistema Corporativo
	    response.sendRedirect(conexionCorpBO.getUrlConexion());
	    
	    return null;
  	
  }
}