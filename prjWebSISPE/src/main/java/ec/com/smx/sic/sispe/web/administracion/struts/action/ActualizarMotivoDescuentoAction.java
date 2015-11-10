/*
 * Clase ActualizarMotivoDescuentoAction.java 
 * Creado el 23/06/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.List;

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
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.MotivoDescuentoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de Actualizaci\u00F3n de
 * Motivos de Descuento y generar la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class ActualizarMotivoDescuentoAction extends BaseAction
{

  /**
   * <p>
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
   * </p>
   * Este m\u00E9todo permite Actualizar un Motivo de Descuento.
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacute;n que estamos procesando
   * @param response		La respuesta HTTP que se genera
   * @return ActionForward	Los seguimiento de salida de las acciones
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    ActionMessages messages = new ActionMessages();

    HttpSession session = request.getSession();
    MotivoDescuentoForm formulario = (MotivoDescuentoForm) form;
    String salida = "desplegar";

    try
    {
      //verifica si se escogi\u00F3 un registro
      if (request.getParameter("indice") != null) {
        //colecci\u00F3n que almacena los registros de los Par\u00E1metros generales
        ArrayList motivoDescuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
        int indice = Integer.parseInt(request.getParameter("indice"));
        
        //se guarda en sesi\u00F3n el indice de la colecci\u00F3n
        session.setAttribute("ec.com.smx.sic.sispe.indice", Integer.toString(indice));
        //DTO para motivo descuentos
        MotivoDescuentoDTO motivoDescuentoDTO = (MotivoDescuentoDTO) motivoDescuentos.get(indice);
        //mantiene la opci\u00F3n escogida en el combo box
        formulario.setEstadoMotivoDescuento(motivoDescuentoDTO.getEstadoMotivoDescuento());
        //se almacena en sesi\u00F3n el registro escogido
        session.setAttribute("ec.com.smx.sic.sispe.registroEscogido", motivoDescuentoDTO);
        
      }
      //Acci\u00F3n de actualizaci\u00F3n-------------------------------------------------------------------------------------
      else if (formulario.getBotonActualizar() != null) 
      {
        //se obtiene la colecci\u00F3n motivo de descuentos
        List<MotivoDescuentoDTO> motivoDescuentos = (List<MotivoDescuentoDTO>) session.getAttribute("ec.com.smx.sic.sispe.tabla");
        try {
          //se obtiene el indice de la colecci\u00F3n
          int indice = Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.indice").toString());
          //DTO de motivo descuento
          MotivoDescuentoDTO motivoDescuentoDTO = motivoDescuentos.get(indice);
          //establecimiento de datos a actualizar
          //motivoDescuentoDTO.setDescripcionMotivoDescuento(formulario.getDescripcionMotivoDescuento());
          motivoDescuentoDTO.setEstadoMotivoDescuento(formulario.getEstadoMotivoDescuento());
          //se actualiza la colecci\u00F3n
          motivoDescuentos.set(indice, motivoDescuentoDTO);
          motivoDescuentoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());//campos de auditoria
          
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio().transActualizarMotivoDescuento(motivoDescuentoDTO);
          formulario.setBotonActualizar(null);
          //establece la lista de registros en sesi\u00F3n
          session.setAttribute("ec.com.smx.sic.sispe.tabla", motivoDescuentos);
          //se guarda emensaje de exito 
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.actualizacion","El Motivo de descuento");
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida = "listado";
        }
        catch (NumberFormatException e) {
          //formato den{umero no v\u00E1lido
          messages.add("errorIndice", new ActionMessage("errors.indiceDetalle.formato"));
          saveErrors(request, messages);
        }
        catch (SISPEException e) {
          //si falla el m\u00E9todo de obtenci\u00F3n de datos
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
          messages.add("tipoDescuentos", new ActionMessage("errors.llamadaServicio.registrarDatos", "el Motivo de Descuento"));
          saveErrors(request, messages);
        }
        catch (Exception e) {
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
        }
      }
      //cancelaci\u00F3n de la acci\u00F3n------------------------------------------------------------------------------------
      else if (formulario.getBotonCancelar() != null || request.getParameter("volver")!=null){
        salida = "listado";
      }
    }
    catch(Exception ex){
      //excepcion desconocida
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    
    return mapping.findForward(salida);
  }
}
