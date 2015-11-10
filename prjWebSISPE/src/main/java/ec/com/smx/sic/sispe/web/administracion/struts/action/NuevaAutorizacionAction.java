/*
 * Clase NuevaAutorizacionAction.java 
 * Creado el 12/05/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;


/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de una Nueva Autorizaci\u00F3n y
 * generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
public class NuevaAutorizacionAction extends BaseAction
{
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control.
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Registrar una Nueva Autorizaci\u00F3n</li>
   * </ul>
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form 			El formulario(si lo hay) asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request			El request que estamos procesando
   * @param response		La respuesta HTTP que se crea
   * @return ActionForward	Describe donde y como se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

  	/*
    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    AutorizacionesForm formulario = (AutorizacionesForm) form;
    String salida = "desplegar";

    try
    {
      //-------------------------- cuando se presiona sobre el bot\u00F3n nuevo ------------------------
      if (formulario.getBotonRegistrarNuevo() != null) 
      {
        //se obtiene el id del usuario logeado
        String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
        String userName = SessionManagerSISPE.getCurrentEntidadResponsable(request).getNombreCompletoUsuario();
        
        //se asignan las fechas y horas actuales del sistema
        Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
        
        //colecci\u00F3n para almacenar la lista de autorizaciones
        Collection autorizaciones = new ArrayList();
        //comprueba si la lista no es nula
        if (session.getAttribute("ec.com.smx.sic.sispe.tabla") != null)
          //se obtiene la colecci\u00F3n de autorizaci\u00F3n
          autorizaciones = (Collection) session.getAttribute("ec.com.smx.sic.sispe.tabla");
        
        //creaci\u00F3n del DTO para crear el nuevo registro
        AutorizacionDTO autorizacionDTO = new AutorizacionDTO();
        
        try 
        {
          //se establecen los datos en los campos
          if(session.getAttribute("ec.com.smx.sic.sispe.entidadLocal")!=null){
            autorizacionDTO.getId().setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal()); 
          }else{
            LogSISPE.debug("formulario.getLocal(): "+formulario.getLocal());
            autorizacionDTO.getId().setCodigoLocal(new Integer(formulario.getLocal())); 
          }
          autorizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          autorizacionDTO.setCodigoTipoAutorizacion(formulario.getTipoAutorizacion());
          autorizacionDTO.setEstadoAutorizacion(MessagesAplicacionSISPE
              .getString("ec.com.smx.sic.sispe.estado.activo"));
          autorizacionDTO.setUsuarioCreacionAutorizacion(userId);
          autorizacionDTO.setFechaCreacionAutorizacion(fechaCreacion);
          autorizacionDTO.setObservacionAutorizacion("CREADA POR: "+ userName + ", MOTIVOS: "+formulario.getObservacionAutorizacion());
          autorizacionDTO.setTipoAutorizacionDTO(new TipoAutorizacionDTO());
          
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio(request).transCrearAutorizacion(autorizacionDTO);
          
          //se crea el objeto para actualizar el listado de autorizaciones
          AutorizacionID consultaAutorizacionID = new AutorizacionID();
          consultaAutorizacionID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          AutorizacionDTO consultaAutorizacionDTO = new AutorizacionDTO();
          consultaAutorizacionDTO.setTipoAutorizacionDTO(new TipoAutorizacionDTO());
          //se obtiene la entidad que est\u00E1 logeada en el sistema
          String entidadResponsable = SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable();
          if(entidadResponsable!=null && entidadResponsable.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local"))){
            consultaAutorizacionID.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal());
          }
          consultaAutorizacionDTO.setId(consultaAutorizacionID);
          //llamada al m\u00E9todo del servicio para obtener la lista de autorizaciones
          autorizaciones = SessionManagerSISPE.getServicioClienteServicio(request)
          	.transObtenerAutorizacion(consultaAutorizacionDTO);
          
          //guarda la colecci\u00F3n en sesi\u00F3n
          session.setAttribute("ec.com.smx.sic.sispe.tabla", autorizaciones);
          
          //se guarda el mensaje de exito en la sesi\u00F3n
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.registroAutorizacion",autorizacionDTO.getId().getCodigoAutorizacion());
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida = "listado";
        }
        catch (SISPEException e) {
          messages.add("autorizaciones",new ActionMessage("errors.llamadaServicio.registrarDatos", "la Autorizaci\u00F3n"));
          saveErrors(request, messages);
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
        }
      }
      //------------------------ cuando se presiona sobre el bot\u00F3n cancelar -----------------------------------
      else if (formulario.getBotonCancelar()!= null || request.getParameter("volver")!=null) 
      {
        salida = "listado";
      }
    }
    catch(Exception ex){
      //excepcion desconocida
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    return mapping.findForward(salida);
    */
  	
  	return null;
  }
}