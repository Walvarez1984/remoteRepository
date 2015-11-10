/*
 * TipoDescuentoAction.java 
 * Creado el 21/06/2006
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la manipulaci\u00F3n de datos del Tipo de descuento y generar la
 * correcta navegaci\u00F3n sobre la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @author	fmunoz
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2 
 */
public class TipoDescuentoAction extends BaseAction {
	/**
     * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
     * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
     * describe d\u00F3nde y c\u00F3mo se redirige el control.
     * Este m\u00E9todo permite:
     * <ul>
     * <li>Mostrar los registros de Tipos de descuentos </li>
     * <li>Acceso a la creaci\u00F3n de Nuevos Tipos de descuento</li>
     * <li>Acceso a la actualizaci\u00F3n de los Tipos de descuentos</li>
     * </ul>
     * 
     * @param mapping			El mapeo utilizado para seleccionar esta instancia
     * @param form			El formulario (si lo hay) asociado a esta acci\u00F3n de donde se toman y establecen valores de
     *          campos
     * @param request			El request que estamos procesando
     * @param response		La respuesta HTTP que se crea
     * @return ActionForward	Describe donde y como se redirige el control
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
            {
        //TipoDescuentoForm formulario = (TipoDescuentoForm) form;
        HttpSession session = request.getSession();
        String salida = "listado";
        
        try
        {
            //se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
            SessionManagerSISPE.removeVarSession(request);
            //colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
            Collection tipoDescuentos = new ArrayList();
            
            LogSISPE.getLog().info("Cargando datos en tabla: DESCUENTOS");
            TipoDescuentoDTO consultaTipoDescuentoDTO = new TipoDescuentoDTO(Boolean.TRUE);
            consultaTipoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
            consultaTipoDescuentoDTO.setTiposDescuentosClasificaciones(new ArrayList());
            //llamada al m\u00E9todo del servicio para obtener la lista de registros
            session.setAttribute("ec.com.smx.sic.sispe.accion",
                    MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.tipoDescuento"));
            tipoDescuentos = SessionManagerSISPE.getServicioClienteServicio()
            	.transObtenerTipoDescuento(consultaTipoDescuentoDTO);
            session.setAttribute("ec.com.smx.sic.sispe.tabla", tipoDescuentos);
            
            session.setAttribute("WebSISPE.tituloVentana", "Tipos de Descuentos");
            session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "tiposDescuentos48.gif");
            session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "tipoDescuento.jsp");
            
            if (tipoDescuentos == null || tipoDescuentos.isEmpty()) {
                //se guarda la lista de mensajes en sesi\u00F3n
                ControlMensajes controlMensajes = new ControlMensajes();
                controlMensajes.setMessages(session,"message.listaVacia","Tipo de Descuentos");
                session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
            }
            
        }
        catch (SISPEException e) {
            //excepci\u00F3n de llamada a m\u00E9todo
            LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
            ControlMensajes controlMensajes = new ControlMensajes();
            controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","el Tipo de Descuento");
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
