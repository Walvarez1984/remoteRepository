/*
 * ActualizarDescuentoAction.java 
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

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.DescuentosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de Actualizaci\u00F3n de Descuentos y generar la correcta navegaci\u00F3n 
 * sobre la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
public class ActualizarDescuentoAction extends BaseAction
{
  /**
   * <p>
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>).
   * </p>
   * Este m\u00E9todo permite:
   * <ul>
   * 	<li>Registrar la Actualizaci\u00F3n de descuentos</li>
   * </ul>
   * 
   * @param mapping 		El mapeo utilizado para seleccionar esta instancia
   * @param form 			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacue; que estamos procesando
   * @param response 		La respuesta HTTP que se genera
   * @return ActionForward	Los seguimiento de salida de las acciones
   * @throws Exception
   */
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    DescuentosForm formulario = (DescuentosForm) form;
    String salida = "desplegar";

    try
    {
      //verifica si se escogi\u00F3 un registro para realizar una acci\u00F3n-------------------------------------------------
      if (request.getParameter("indice") != null) 
      {
        //establece la accion a Descuentos (des)
        session.setAttribute("ec.com.smx.sic.sispe.accion",
            MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.descuento"));
      
        //colecci\u00F3n que almacena los registros de los Par\u00E1metros generales
        ArrayList descuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos");
        LogSISPE.getLog().info("EL REGISTRO DE DESCUENTOS A ACTUALIZAR ESTA VACIO (Ant)?: " + descuentos.isEmpty());
        int indice = Integer.parseInt(request.getParameter("indice"));
        
        //se guarda en sesi\u00F3n el indice de la colecci\u00F3n
        session.setAttribute("ec.com.smx.sic.sispe.indice", Integer.toString(indice));
        //se obtiene el descuento seleccionado
        DescuentoDTO descuentoDTO = (DescuentoDTO) descuentos.get(indice);
        //se cargan los datos en el formulario
        formulario.setEstadoDescuento(descuentoDTO.getEstadoDescuento());
        //formulario.setCodigoTipoDescuento(descuentoDTO.getCodigoTipoDescuento());
        formulario.setCodigoMotivoDescuento(descuentoDTO.getCodigoMotivoDescuento());
        String rangoInicialFormateado = Util.roundDoubleMath(descuentoDTO.getRangoInicialDescuento(),2).toString();
        String rangoFinalFormateado = Util.roundDoubleMath(descuentoDTO.getRangoFinalDescuento(),2).toString();
        String porcentajeFormateado = Util.roundDoubleMath(descuentoDTO.getPorcentajeDescuento(),2).toString();
        //se actualiza el formulario
        formulario.setRangoInicialDescuento(rangoInicialFormateado);
        formulario.setRangoFinalDescuento(rangoFinalFormateado);
        formulario.setPorcentajeDescuento(porcentajeFormateado);
        
        //se almacena en sesi\u00F3n el registro escogido
        LogSISPE.getLog().info("EL REGISTRO DE DESCUENTOS A ACTUALIZAR ESTA VACIO (Desp)?: " + descuentos.isEmpty());
        salida = "desplegar";
      }
      
      //acci\u00F3n de actualizaci\u00F3n-------------------------------------------------------------------------------------
      else if (formulario.getBotonActualizar() != null) 
      {
        //se obtiene la colecci\u00F3n tipo de descuentos
        ArrayList descuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos");
        try 
        {
          //se obtiene el indice de la colecci\u00F3n
          int indice = Integer.parseInt(session.getAttribute("ec.com.smx.sic.sispe.indice").toString());
          //se obtiene el id del usuario logeado
          String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
          //se asignan las fechas y horas actuales del sistema
          Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
          DescuentoDTO descuentoDTO = (DescuentoDTO) descuentos.get(indice);
          
          //se llama al m\u00E9todo que verifica si existi\u00F3 alg\u00FAn cambio en el formulario
          validarDiferenciaDatos(descuentoDTO,formulario,request,messages);
          if(messages.isEmpty())
          {
            TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)session.getAttribute("ec.com.smx.sic.sispe.TipoDescuentoDTO");
            //establecimiento de los datos a actualizar
            descuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
            descuentoDTO.setCodigoMotivoDescuento(formulario.getCodigoMotivoDescuento());
            descuentoDTO.setEstadoDescuento(formulario.getEstadoDescuento());
            descuentoDTO.setFechaCreacionDescuento(fechaCreacion);
            descuentoDTO.setPorcentajeDescuento(Double.valueOf(formulario.getPorcentajeDescuento()));
            descuentoDTO.setRangoInicialDescuento(Double.valueOf(formulario.getRangoInicialDescuento()));
            descuentoDTO.setRangoFinalDescuento(Double.valueOf(formulario.getRangoFinalDescuento()));
            descuentoDTO.setUsuarioCreacionDescuento(userId);

            //llamada al m\u00E9todo del servicio para actualizar el registro
            SessionManagerSISPE.getServicioClienteServicio().transRegistrarDescuento(descuentoDTO);
            
            //Consulta la lista de registros actualizados
            DescuentoDTO consultaDescuentoDTO = new DescuentoDTO(Boolean.TRUE);
            consultaDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
            consultaDescuentoDTO.setMotivoDescuentoDTO(new MotivoDescuentoDTO());
            consultaDescuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
            consultaDescuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
            descuentos = (ArrayList) SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(consultaDescuentoDTO);
            
            formulario.setBotonActualizar(null);
            //establece el listado de registros
            session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos", descuentos);
            
            //se guarda emensaje de exito 
            ControlMensajes controlMensajes = new ControlMensajes();
            controlMensajes.setMessages(session,"message.exito.actualizacion","El Descuento");
            session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
            salida = "listado";
          }
        }
        //Captura de errores
        catch (NumberFormatException e) {
          messages.add("errorIndice", new ActionMessage("errors.indiceDetalle.formato"));
          saveErrors(request, messages);
        }
        catch (SISPEException e){
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
          messages.add("descuentos", new ActionMessage("errors.llamadaServicio.registrarDatos", "el Descuento"));
          messages.add("exceptionSISPE", new ActionMessage("errors.SISPEException", e.getMessage()));
          saveErrors(request, messages);
        }
      }
      // si se cancela la actualizaci\u00F3n-----------------------------------------------------------------------------
      else if (formulario.getBotonCancelar() != null || request.getParameter("volver")!=null) 
      {
        //SE OBTIENE EL TIPOD DE DESCUENTO
        TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)session.getAttribute("ec.com.smx.sic.sispe.TipoDescuentoDTO");
        //Consulta la lista de registros actualizados
        DescuentoDTO consultaDescuentoDTO = new DescuentoDTO(true);
        consultaDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
        consultaDescuentoDTO.setMotivoDescuentoDTO(new MotivoDescuentoDTO());
        consultaDescuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
        consultaDescuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
        
        Collection descuentos = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(consultaDescuentoDTO);
        //establece el listado de registros
        session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos", descuentos);
        salida = "listado";
      }
      
    }
    catch(Exception ex){
      //excepcion desconocida
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    
    return mapping.findForward(salida); //finaliza
  }
  
  /**
   * Comprueba si hay alguna diferencia en los datos ingresados
   * @param formulario		Formulario de descuentos
   * @param messages		Colecci&oacute;n de mensajes manejados en la acci&oacute;n
   */
  private void validarDiferenciaDatos(DescuentoDTO descuentoDTO,DescuentosForm formulario,
      HttpServletRequest request,ActionMessages messages){
    try
    {
      if(descuentoDTO!=null){
        LogSISPE.getLog().info("descuentoDTO.getCodigoTipoDescuento(): "+descuentoDTO.getCodigoTipoDescuento());
        LogSISPE.getLog().info("descuentoDTO.getMotivoDescuentoDTO(): "+descuentoDTO.getCodigoMotivoDescuento());
        LogSISPE.getLog().info("formulario.getCodigoMotivoDescuento(): "+formulario.getCodigoMotivoDescuento());
        LogSISPE.getLog().info("descuentoDTO.getRangoInicialDescuento(): "+descuentoDTO.getRangoInicialDescuento());
        LogSISPE.getLog().info("formulario.getRangoInicialDescuento(): "+formulario.getRangoInicialDescuento());
        LogSISPE.getLog().info("descuentoDTO.getRangoFinalDescuento(): "+descuentoDTO.getRangoFinalDescuento());
        LogSISPE.getLog().info("formulario.getRangoFinalDescuento(): "+formulario.getRangoFinalDescuento());
        LogSISPE.getLog().info("descuentoDTO.getPorcentajeDescuento(): "+descuentoDTO.getPorcentajeDescuento());
        LogSISPE.getLog().info("formulario.getPorcentajeDescuento(): "+formulario.getPorcentajeDescuento());
        if(descuentoDTO.getEstadoDescuento().equals(formulario.getEstadoDescuento()))
          if(descuentoDTO.getCodigoMotivoDescuento().equals(formulario.getCodigoMotivoDescuento()))
            if(descuentoDTO.getRangoInicialDescuento().doubleValue()==Double.parseDouble(formulario.getRangoInicialDescuento()))
            if(descuentoDTO.getRangoFinalDescuento().doubleValue()==Double.parseDouble(formulario.getRangoFinalDescuento()))
              if(descuentoDTO.getPorcentajeDescuento().doubleValue()==Double.parseDouble(formulario.getPorcentajeDescuento())){
                //se crea un mensaje informativo
                messages.add("sinCambios",new ActionMessage("message.descuento.sinModificar"));
                saveInfos(request,messages);
                LogSISPE.getLog().info("FUE IGUAL");
              }
      }
    }catch(Exception ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
    }
  }
}