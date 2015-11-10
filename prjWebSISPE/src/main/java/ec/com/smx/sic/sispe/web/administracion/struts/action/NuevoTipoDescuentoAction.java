/*
 * Clase NuevoTipoDescuentoAction.java 
 * Creado el 22/06/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.TipoDescuentoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de un Nuevo Tipo de Descuento y
 * generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class NuevoTipoDescuentoAction extends BaseAction 
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control.
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Registrar un Nuevo Tipo de Descuento</li>
   * </ul>
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario (si lo hay) asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request			El request que estamos procesando
   * @param response		La respuesta HTTP que se crea
   * @return ActionForward	Describe donde y como se redirige el control
   * @throws Exception
   */
  public static final String CODIGOCALSIFICACION= "ec.com.smx.sic.sispe.nuevoTipoDescuento.clasificacion";

  public ActionForward execute(ActionMapping mapping, ActionForm form, 
      HttpServletRequest request,HttpServletResponse response) 
  throws Exception 
  {

    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    TipoDescuentoForm formulario = (TipoDescuentoForm) form;
    //se obtienen los valores de los estados activo e inactivo
    String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
    String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
    String salida = "desplegar";

    try
    {
      //cuando se presiona sobre el bot\u00F3n nuevo-------------------------------------------------------------------
      if (formulario.getBotonRegistrarNuevo() != null) 
      {
        //colecci\u00F3n para almacenar la lista de tipo de descuentos
        Collection tiposDescuento = null;
        
        try {
          //comprueba si la lista no es nula
          if (session.getAttribute("ec.com.smx.sic.sispe.tabla") != null)
            //se obtiene la colecci\u00F3n de autorizaci\u00F3n
            tiposDescuento = (Collection) session.getAttribute("ec.com.smx.sic.sispe.tabla");
          else
            tiposDescuento = new ArrayList();
          
          //creaci\u00F3n del DTO para crear el nuevo registro
          TipoDescuentoDTO tipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
          tipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          //se establecen los datos en los campos
          tipoDescuentoDTO.setEstadoTipoDescuento(estadoActivo);
          tipoDescuentoDTO.setDescripcionTipoDescuento(formulario.getDescripcionTipoDescuento());
          tipoDescuentoDTO.setTiposDescuentosClasificaciones((Collection)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones"));
          tipoDescuentoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio().transRegistrarTipoDescuento(tipoDescuentoDTO);
          
          //se actualiza la lista de tipos de descuentos desde la base de datos
          LogSISPE.getLog().info("Actualizando los tipos de descuentos");
          TipoDescuentoDTO consultaTipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
          consultaTipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          consultaTipoDescuentoDTO.setTiposDescuentosClasificaciones(new ArrayList());
          //llamada a la capa de servicio
          tiposDescuento = SessionManagerSISPE.getServicioClienteServicio()
        	.transObtenerTipoDescuento(consultaTipoDescuentoDTO);
          session.setAttribute("ec.com.smx.sic.sispe.tabla", tiposDescuento);
          
          //se guarda el mensaje de exito en la sesi\u00F3n
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.registro","El Tipo de descuento");
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida="listado";
        }
        catch (SISPEException ex) {
          //cuando existe un error en el registro
          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          messages.add("tipoDescuento", new ActionMessage("errors.llamadaServicio.registrarDatos", "el Tipo Descuento"));
          messages.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
          saveErrors(request, messages);
        }
      }
      /*-------------------------- cuando se desea agragar una clasificaci\u00F3n ----------------------------*/
      else if(formulario.getBotonAgregarClasificacion()!=null)
      {
        Collection codigosClasificaciones = (Collection)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");       
        session.setAttribute(CODIGOCALSIFICACION, formulario.getCodigoClasificacionDescuento());
        if(!codigosClasificaciones.contains(formulario.getCodigoClasificacionDescuento()))
        {
          //se construye el objeto clasificacionDTO
          ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
          clasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          clasificacionDTO.getId().setCodigoClasificacion(formulario.getCodigoClasificacionDescuento());
          clasificacionDTO.setEstadoClasificacion(estadoActivo);
          ArrayList clasificaciones = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionDTO);
          if(clasificaciones!=null && !clasificaciones.isEmpty()){
            LogSISPE.getLog().info("tama\u00F1o de las clasificaciones: "+clasificaciones.size());
            clasificacionDTO = (ClasificacionDTO)clasificaciones.get(0);
            //se construye el objeto de las tipoDescuentoClasificacionDTO
            TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = new TipoDescuentoClasificacionDTO();
            tipoDescuentoClasificacionDTO.getId().setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
            tipoDescuentoClasificacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
            tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoActivo);
            tipoDescuentoClasificacionDTO.setClasificacionDTO(clasificacionDTO);
            Collection tiposDescuentosClasificaciones = (Collection)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones");
            //primero se verifica si la clasificaci\u00F3n que desea agregar se encuentra en otro especial
            TipoDescuentoClasificacionDTO tipoDescuentoClasificacionConsultado = SessionManagerSISPE.getServicioClienteServicio().transValidarTipoDescuentoClasificacion(tipoDescuentoClasificacionDTO);
            if(tipoDescuentoClasificacionConsultado != null){
              //si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
              messages.add("clasificacionRepetida",new ActionMessage("errors.anadir.clasificacion.tipoDescuento",
                  formulario.getCodigoClasificacionDescuento(),clasificacionDTO.getDescripcionClasificacion(),
                  tipoDescuentoClasificacionConsultado.getId().getCodigoTipoDescuento(), tipoDescuentoClasificacionConsultado.getClasificacionDTO().getDescripcionClasificacion()));
              saveErrors(request,messages);
            }
            else{
            	//campos de auditoria
            	tipoDescuentoClasificacionDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
            	tipoDescuentoClasificacionDTO.setUsuarioRegistro(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
            	
                tiposDescuentosClasificaciones.add(tipoDescuentoClasificacionDTO);
                codigosClasificaciones.add(formulario.getCodigoClasificacionDescuento());
            }
          }else{
            //si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
            messages.add("clasificacionNoEncontrada",new ActionMessage("message.codigo.invalido","una clasificaci\u00F3n"));
            saveInfos(request,messages);
          }
        }else{
          //si el c\u00F3digo de la clasificaci\u00F3n ya se encuentra en la colecci\u00F3n
          messages.add("clasificacion",new ActionMessage("errors.clasificacionRepetida",formulario.getCodigoClasificacionDescuento()));
          saveErrors(request,messages);
        }
      }
      /*------------------------- si se desactiva o activa una clasificaci\u00F3n --------------------------------*/
      else if(request.getParameter("indiceDesactivacionActivacion")!=null)
      {
        ArrayList clasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones");
        formulario.setCodigoClasificacionDescuento((String)session.getAttribute(CODIGOCALSIFICACION));
        try{
          //se obtiene el TipoDescuentoClasificacionDTO
          TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO)
          	clasificaciones.get(Integer.parseInt(request.getParameter("indiceDesactivacionActivacion")));
          if(tipoDescuentoClasificacionDTO.getEstadoTipoDescuentoClasificacion().equals(estadoActivo))
            tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoInactivo);
          else
            tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(estadoActivo);
        }
        catch(IndexOutOfBoundsException ex){
          //si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
          messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
          saveErrors(request,messages);
          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
        }
        //se recupera el dato de sesi\u00F3n
        //formulario.setDescripcionTipoDescuento((String)session
          //  .getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcion"));
      }
      /*------------------------- si se elimina una clasificaci\u00F3n --------------------------------*/
      else if(request.getParameter("indiceEliminacion")!=null)
      {
        ArrayList clasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones");
        ArrayList codigosClasificaciones = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.codigosClasificaciones");
        formulario.setCodigoClasificacionDescuento((String)session.getAttribute(CODIGOCALSIFICACION));
        try{
          int indice = Integer.parseInt(request.getParameter("indiceEliminacion"));
          //se obtiene el TipoDescuentoClasificacionDTO
          TipoDescuentoClasificacionDTO tipoDescuentoClasificacionDTO = (TipoDescuentoClasificacionDTO) clasificaciones.get(indice);
          tipoDescuentoClasificacionDTO.setEstadoTipoDescuentoClasificacion(null);
          clasificaciones.remove(indice);
          codigosClasificaciones.remove(indice);  
	      session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",codigosClasificaciones);
		  session.setAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones",clasificaciones);
        }
        catch(IndexOutOfBoundsException ex){
          //si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
          messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
          saveErrors(request,messages);
        }
      }
      //cuando se presiona sobre el bot\u00F3n cancelar----------------------------------------------------------------
      else if (formulario.getBotonCancelar()!=null || request.getParameter("volver")!=null){
        //se actualiza la lista de tipos de descuentos desde la base de datos
        LogSISPE.getLog().info("Actualizando los tipos de descuentos");
        TipoDescuentoDTO consultaTipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
        consultaTipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
        consultaTipoDescuentoDTO.setTiposDescuentosClasificaciones(new ArrayList());
        //llamada a la capa de servicio
        Collection tiposDescuento = SessionManagerSISPE.getServicioClienteServicio()
      		.transObtenerTipoDescuento(consultaTipoDescuentoDTO);
        session.setAttribute("ec.com.smx.sic.sispe.tabla", tiposDescuento);
        salida = "listado";
      }
      else{
        //crea la colecci\u00F3n que contendr\u00E1 las clasificaciones
        session.setAttribute("ec.com.smx.sic.sispe.tiposDescuentosClasificaciones",new ArrayList());
        session.setAttribute("ec.com.smx.sic.sispe.codigosClasificaciones",new ArrayList());
      }
    }
    catch(Exception ex){
      //esxepcion desconocida
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    return mapping.findForward(salida);
  }
}