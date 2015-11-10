/*
 * Clase NuevoDescuentoAction.java 
 * Creado el 27/06/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.sql.Timestamp;
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.DescuentosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos del Nuevo descuento y generar la
 * correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */

public class NuevoDescuentoAction extends BaseAction
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control.
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Registrar un Nuevo Descuento</li>
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
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception 
      {

    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    DescuentosForm formulario = (DescuentosForm) form;
    //se obtienen los valores de los estados activo e inactivo
    String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
	
    String salida = "desplegar";
    try
    {
      //cuando se presiona sobre el bot\u00F3n Regitrar nuevo descuento------------------------------------------------
      if (formulario.getBotonRegistrarNuevo() != null) 
      {
        //SE OBTIENE EL TIPOD DE DESCUENTO
        TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)session.getAttribute("ec.com.smx.sic.sispe.TipoDescuentoDTO");  
        //creaci\u00F3n del DTO para crear el nuevo registro
        DescuentoDTO descuentoDTO = new DescuentoDTO(Boolean.TRUE);
        Collection descuentos = new ArrayList();
        try 
        {
          //comprueba si la lista no es nula
          if (session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos") != null)
            //se obtiene la colecci\u00F3n de descuentos
            descuentos = (Collection) session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos");
          
          //se obtiene el id del usuario logeado
          String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
          //se asignan las fechas y horas actuales del sistema
          Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
          //se establecen los datos en los campos
          descuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          descuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
          descuentoDTO.setCodigoMotivoDescuento(formulario.getCodigoMotivoDescuento());
          descuentoDTO.setEstadoDescuento(estadoActivo);
          descuentoDTO.setFechaCreacionDescuento(fechaCreacion);
          descuentoDTO.setPorcentajeDescuento(Double.valueOf(formulario.getPorcentajeDescuento()));
          descuentoDTO.setRangoInicialDescuento(Double.valueOf(formulario.getRangoInicialDescuento()));
          descuentoDTO.setRangoFinalDescuento(Double.valueOf(formulario.getRangoFinalDescuento()));
          descuentoDTO.setUsuarioCreacionDescuento(userId);
          
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio().transRegistrarDescuento(descuentoDTO);
          LogSISPE.getLog().info("DESCUENTO REGISTRADO");
          LogSISPE.getLog().info("CONSULTANDO DESCUENTOS NUEVOS----------");
          
          //consulta la lista de datos agregados
          DescuentoDTO consultaDescuentoDTO = new DescuentoDTO(Boolean.TRUE);
          consultaDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          consultaDescuentoDTO.setMotivoDescuentoDTO(new MotivoDescuentoDTO());
          consultaDescuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
          consultaDescuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
          
          //guarda los datos en la colecci\u00F3n
          descuentos = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(consultaDescuentoDTO);
          //guarda la colecci\u00F3n en sesi\u00F3n
          session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos", descuentos);
          
          //se guarda el mensaje de exito en la sesi\u00F3n
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.registro","El Descuento");
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida = "listado";
        }
        catch (SISPEException e) {
          //cuando no se ha seleccionado un pedido
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
          messages.add("Descuento", new ActionMessage("errors.llamadaServicio.registrarDatos", "el Descuento"));
          messages.add("exceptionSISPE", new ActionMessage("errors.SISPEException", e.getMessage()));
          saveErrors(request, messages);
          //descuentoDTO.getId().setCodigoDescuento(null);
        }
        
      }
      
      //cuando se presiona sobre el bot\u00F3n cancelar----------------------------------------------------------------
      else if (formulario.getBotonCancelar()!= null || request.getParameter("volver")!=null)
      {
        //SE OBTIENE EL TIPOD DE DESCUENTO
        TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)session.getAttribute("ec.com.smx.sic.sispe.TipoDescuentoDTO");  
        //consulta la lista de datos agregados
        DescuentoDTO consultaDescuentoDTO = new DescuentoDTO();
        consultaDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
        consultaDescuentoDTO.setMotivoDescuentoDTO(new MotivoDescuentoDTO());
        consultaDescuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
        consultaDescuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
        
        //guarda los datos en la colecci\u00F3n
        Collection descuentos = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(consultaDescuentoDTO);
        //guarda la colecci\u00F3n en sesi\u00F3n
        session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos", descuentos);
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
