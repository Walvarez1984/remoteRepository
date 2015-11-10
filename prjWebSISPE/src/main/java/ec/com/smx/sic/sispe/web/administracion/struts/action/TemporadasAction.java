/*
 * TemporadasAction.java 
 * Creado el 08/06/2006
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.TemporadaDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.TemporadasForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase permite la manipulaci\u00F3n de datos del y generar la correcta navegaci\u00F3n 
 * sobre la aplicaci\u00F3n.
 * 
 * @author 	dlopez
 * @author 	fmunoz
 * @author	mbraganza
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
public class TemporadasAction extends BaseAction 
{
	/**
     * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
     * respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa 
     * crear) Devuelve una instancia <code>ActionForward</code> que
     * describe d\u00F3nde y c\u00F3mo se redirige el control.
     * Este m\u00E9todo permite:
     * <ul>
     * <li>Mostrar el listado de Temporadas </li>
     * <li>Desactivar una Temporada</li>
     * <li>Acceso a la creaci\u00F3n de Nuevas Temporadas</li>
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
            HttpServletResponse response) throws Exception {
        
        ActionMessages messages = new ActionMessages();
        TemporadasForm formulario = (TemporadasForm) form;
        HttpSession session = request.getSession();
        
        String salida = "listado";
        
        try
        {
            //verificaci\u00F3n del indice del registro de temporada a desactivar----------------------------------------------
            if (request.getParameter("indice") != null) 
            {
                try {
                    LogSISPE.getLog().info("INGRESO A DESACTIVAR TEMPORADA");
                    
                    //se tranforma el indice de String a int
                    int indice = Integer.parseInt(request.getParameter("indice"));
                    //obtiene la lista de temporadas de la sesi\u00F3n
                    ArrayList temporadas = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
                    //se obtiene la temporada que se va a desactivar
                    TemporadaDTO temporadaDTO = (TemporadaDTO) temporadas.get(indice);
                    //llamada al m\u00E9todo del servicio para desactivar la temporada
                    SessionManagerSISPE.getServicioClienteServicio().transDesactivarTemporada(temporadaDTO);
                    
                    //llamada al m\u00E9todo para obtener las temporadas actualizadas
                    TemporadaDTO consultaTemporadaDTO = new TemporadaDTO();
                    consultaTemporadaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
                    //llamada al m\u00E9todo del servicio para obtener la lista de registros
                    temporadas = (ArrayList) SessionManagerSISPE.getServicioClienteServicio().transObtenerTemporada(
                            consultaTemporadaDTO);
                    //establece la lista en la variable de sesi\u00F3n
                    session.setAttribute("ec.com.smx.sic.sispe.tabla", temporadas);
                    LogSISPE.getLog().info("SE DESACTIVO LA TEMPORADA");
                    
                    //se guarda el mensaje de exito en la sesi\u00F3n
                    ControlMensajes controlMensajes = new ControlMensajes();
                    controlMensajes.setMessages(session,"message.exito.desactivacion","La temporada");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
                    
                    //captura la excepci\u00F3n si no se pudo hacer la conversi\u00F3n de n\u00FAmeros
                }
                catch (NumberFormatException ex) {
                    messages.add("errorIndice", new ActionMessage("errors.indiceDetalle.formato"));
                    saveErrors(request, messages);
                }
                catch (SISPEException e) {
                    //si falla la llamada al m\u00E9todo del servicio
                    LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
                    ControlMensajes controlMensajes = new ControlMensajes();
                    controlMensajes.setMessages(session,"errors.llamadaServicio.registrarDatos","la desactivaci\u00F3n");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","error");
                }
                LogSISPE.getLog().info("Link-Desactivar");
                //session.setAttribute("ec.com.smx.sic.sispe.desactivarAnular","ok");
                
            }
            
            //Acci\u00F3n cancelar---------------------------------------------------------------------------------------------
            else if (formulario.getBotonCancelar() != null) {
                LogSISPE.getLog().info("SE CANCELO");
            }
            //acci\u00F3n por defecto------------------------------------------------------------------------------------------
            else {
                //se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
                SessionManagerSISPE.removeVarSession(request);
                //colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
                Collection temporadasGenerales = new ArrayList();
                
                try {
                    TemporadaDTO consultaTemporadaDTO = new TemporadaDTO();
                    consultaTemporadaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
                    //establece la variable de sesi\u00F3n para mostrar el listado de temporadas
                    session.setAttribute("ec.com.smx.sic.sispe.accion",
                            MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.temporada"));
                    
                    //llamada al m\u00E9todo del servicio para obtener la lista de registros
                    temporadasGenerales = SessionManagerSISPE.getServicioClienteServicio().transObtenerTemporada(
                            consultaTemporadaDTO);
                    session.setAttribute("ec.com.smx.sic.sispe.tabla", temporadasGenerales);
                    
                    //establece variables de sesi\u00F3n para la presentaci\u00F3n de estados
                    SessionManagerSISPE.getEstadoActivo(request);
                    session.setAttribute("WebSISPE.tituloVentana", "Temporadas");
                    session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "temporada48.gif");
                    session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "temporadas.jsp");
                    
                    //guarda los mensajes de error si la lista de temporadas esta vac\u00EDa
                    if (temporadasGenerales == null || temporadasGenerales.isEmpty()) {
                        //se guarda el mensaje de informaci\u00F3n en la sesi\u00F3n
                        ControlMensajes controlMensajes = new ControlMensajes();
                        controlMensajes.setMessages(session,"message.listaVacia","Temporadas");
                        session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
                    }
                }
                catch (SISPEException e) {
                    //se guarda el mensaje de error en la sesi\u00F3n
                    ControlMensajes controlMensajes = new ControlMensajes();
                    controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","Temporadas");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","error");
                    LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
                }
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