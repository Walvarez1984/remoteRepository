/*
 * Clase NuevaTemporadaAction.java 
 * Creado el 05/06/2006
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

import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TemporadaDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.TemporadasForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos de una Nueva Temporada y generar 
 * la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class NuevaTemporadaAction extends BaseAction 
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control.
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Registrar una Nueva Temporada </li>
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
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws Exception
      {

    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    TemporadasForm formulario = (TemporadasForm) form;
    
    String salida = "desplegar";
    //Clase utilizada para convertir una fecha de formato String a Date.
    SqlTimestampConverter convertidor = new SqlTimestampConverter(new String[] {"formatos.fecha"});

    try
    {
      /*------------------------------------- acci\u00F3n nuevo regisatro--------------------------------------- */
      if (formulario.getBotonRegistrarNuevo() != null) {
        //se obtiene el id del usuario logeado
        String userId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
        //colecci\u00F3n para almacenar las tempordas
        Collection temporadas = null;
        try {
          //se asignan las fechas y horas actuales del sistema
          Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
          //se transforma la fecha de inicial de temporada
          Timestamp fechaInicialTemporada = (Timestamp) convertidor.convert(Timestamp.class, formulario.getFechaInicialTemporada());
          //se transforma la fecha de final de temporada
          Timestamp fechaFinalTemporada = (Timestamp) convertidor.convert(Timestamp.class, formulario.getFechaFinalTemporada());
          LogSISPE.getLog().info("fecha final: "+fechaFinalTemporada);
          //creaci\u00F3n del DTO para crear el nuevo registro
          TemporadaDTO temporadaDTO = new TemporadaDTO();
          //establece los campos para el nuevo registro
          temporadaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          LogSISPE.getLog().info("CODIGO COMPANIA: "+temporadaDTO.getId().getCodigoCompania());
          temporadaDTO.setDescripcionTemporada(formulario.getDescripcionTemporada());
//          temporadaDTO.setFechaCreacionTemporada(fechaCreacion);
          temporadaDTO.setFechaInicialTemporada(fechaInicialTemporada);
          temporadaDTO.setFechaFinalTemporada(fechaFinalTemporada);
          //se toma el estado del archivo de recursos de la aplicaci\u00F3n
          temporadaDTO.setEstadoTemporada(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
          temporadaDTO.setUsuarioRegistro(userId);
          temporadaDTO.setFechaRegistro(fechaCreacion);
          
          if(formulario.getIncluirArtTemAnt().equals("si") ){
        	  temporadaDTO.addDynamicProperty("migrarArtTem",Boolean.TRUE);
          }else{
        	  temporadaDTO.addDynamicProperty("migrarArtTem",Boolean.FALSE);
          }
        	
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio().transRegistrarTemporada(temporadaDTO);
          
          //llamada al m\u00E9todo para obtener las temporadas actualizadas
          TemporadaDTO consultaTemporadaDTO = new TemporadaDTO();
          consultaTemporadaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
          //llamada al m\u00E9todo del servicio para obtener la lista de registros
          temporadas = (ArrayList) SessionManagerSISPE.getServicioClienteServicio().transObtenerTemporada(consultaTemporadaDTO);
          //establece la lista en la variable de sesi\u00F3n
          session.setAttribute("ec.com.smx.sic.sispe.tabla", temporadas);
          LogSISPE.getLog().info("tama\u00F1o de la coleccion: "+temporadas.size());
          
          //se guarda el mensaje de exito en la sesi\u00F3n
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.registro","La Nueva Temporada");
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida = "listado";
          
        }catch (SISPEException e) 
        {
          //la llamada al servicio falla
          LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
          messages.add("Temporada", new ActionMessage("errors.llamadaServicio.registrarDatos", "la Temporada"));
          saveErrors(request, messages);
        }
        /*------------------------- si se presion\u00F3 sobre cancelar --------------------------------*/
      }else if (formulario.getBotonCancelar() != null || request.getParameter("volver")!=null)
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
  }
}