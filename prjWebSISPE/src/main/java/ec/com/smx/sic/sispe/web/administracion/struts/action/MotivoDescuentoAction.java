/*
 * Clase MotivoDescuentoAction.java
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

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase permite la manipulaci\u00F3n de datos de Motivo de Descuentos y generar 
 * la correcta navegaci\u00F3n sobre la aplicaci\u00F3n
 * 
 * @author 	dlopez
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2 
 */
public class MotivoDescuentoAction extends BaseAction
{
	/**
     * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
     * respuesta HTTP (response) (o lo
     * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia 
     * <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
     * Este m\u00E9todo permite:
     * <ul>
     * <li>Mostrar el listado de Motivo de Descuentos </li>
     * <li>Acceso a la Creaci\u00F3n de un Nuevo Motivo de Descuento</li>
     * <li>Acceso a la actualizaci\u00F3n de un Motivo de Desceunto</li>
     * </ul>
     * 
     * @param mapping			El mapeo utilizado para seleccionar esta instancia
     * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
     *          				campos
     * @param request			El request que estamos procesando
     * @param response		La respuesta HTTP que se crea
     * @return ActionForward	Describe donde y como se redirige el control
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
            HttpServletResponse response)
    throws Exception 
    {
        //MotivoDescuentoForm formulario = (MotivoDescuentoForm) form;
        HttpSession session = request.getSession();
        
        String salida = "listado";
        try
        {
            //se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
            SessionManagerSISPE.removeVarSession(request);
            //colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
            Collection motivodescuentos = new ArrayList();
            LogSISPE.getLog().info("Cargando datos en tabla: Motivo DESCUENTOS");
            MotivoDescuentoDTO consultaMotivoDescuentoDTO = new MotivoDescuentoDTO(Boolean.TRUE);
            consultaMotivoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
            //se establece la acci\u00F3n a motivo descuento
            session.setAttribute("ec.com.smx.sic.sispe.accion",
                    MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.motivoDescuento"));
            
            session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "motivoDescuento48.gif");
            session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "motivoDescuento.jsp");
            
            //llamada al m\u00E9todo del servicio para obtener la lista de registros
            motivodescuentos = SessionManagerSISPE.getServicioClienteServicio().transObtenerMotivoDescuento(consultaMotivoDescuentoDTO);
            
            session.setAttribute("ec.com.smx.sic.sispe.tabla", motivodescuentos);
            session.setAttribute("ec.com.smx.sic.sispe.activo",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
            session.setAttribute("ec.com.smx.sic.sispe.inactivo",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
            session.setAttribute("WebSISPE.tituloVentana","Motivo de descuentos");
            
            if (motivodescuentos == null || motivodescuentos.isEmpty()) {
                //se guarda la lista de mensajes en sesi\u00F3n
                ControlMensajes controlMensajes = new ControlMensajes();
                controlMensajes.setMessages(session,"message.listaVacia","Motivo de Descuentos");
                session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
            }
            
        }catch (SISPEException e) {
            //si falla el m\u00E9todo de obtenci\u00F3n de datos
        	LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
            ControlMensajes controlMensajes = new ControlMensajes();
            controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","Motivo de Descuentos");
            session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","error");
        }
        catch(Exception ex){
            //excepcion desconocida
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
            salida="errorGlobal";
        }
        
        return mapping.findForward(salida);
    }
}
