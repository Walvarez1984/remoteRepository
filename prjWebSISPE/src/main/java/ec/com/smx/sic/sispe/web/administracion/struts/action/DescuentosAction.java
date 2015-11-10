/*
 * Clase DescuentosAction,java
 * Creado el 27/06/2006
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
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.MotivoDescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase permite la manipulaci\u00F3n de datos de Descuentos y generar la correcta 
 * navegaci\u00F3n sobre la aplicaci\u00F3n
 * 
 * @author 	dlopez
 * @author 	fmunoz
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2 
 */
public class DescuentosAction extends BaseAction {
	/**
     * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
     * respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear) 
     * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo 
     * se redirige el control.
     * 
     * Este m\u00E9todo permite:
     * <ul>
     * <li>Mostrar el listado de Descuentos </li>
     * <li>Acceso a la Craci\u00F3n de un Nuevo Descuento</li>
     * <li>Acceso a la Actualizaci\u00F3n de un Descuento</li>
     * </ul>
     * @param mapping			El mapeo utilizado para seleccionar esta instancia.
     * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
     *          				campos
     * @param request			El request que estamos procesando.
     * @param response		El response al cual enviamos la informaci\u00F3n
     * @return ActionForward	Describe donde y como se redirige el control
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        
        //DescuentosForm formulario = (DescuentosForm) form;
        HttpSession session = request.getSession();
        
        String salida = "listado";
        
        try
        {
            //cuando se da clic en el link de los detalles de las clasificaciones, desde la pantalla tipos descuentos
            if(request.getParameter("indiceTipoDescuento")!=null)
            {
                LogSISPE.getLog().info("CARGANDO DATOS DE DESCUENTOS");
                //establece la accion a Descuentos (des)
                session.setAttribute("ec.com.smx.sic.sispe.accion",
                        MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.descuento"));
                
                session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "descuentos48.gif");
                session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "descuentos.jsp");
                
                //se obtienen los tipos de descuentos
                ArrayList tiposDescuentos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
                int indice = Integer.parseInt(request.getParameter("indiceTipoDescuento"));
                TipoDescuentoDTO tipoDescuentoDTO = (TipoDescuentoDTO)tiposDescuentos.get(indice);
                
                //DTO de descuentos y motivos de descuentos
                DescuentoDTO consultaDescuentoDTO = new DescuentoDTO(Boolean.TRUE);
                MotivoDescuentoDTO consultaMotivoDescuentoDTO = new MotivoDescuentoDTO(Boolean.TRUE);
                
                LogSISPE.getLog().info("tipoDescuentoDTO.getId().getCodigoTipoDescuento(): "+tipoDescuentoDTO.getId().getCodigoTipoDescuento());
                //establece los estados de cada uno a activos para la consulta
                consultaDescuentoDTO.setCodigoTipoDescuento(tipoDescuentoDTO.getId().getCodigoTipoDescuento());
                consultaDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
                consultaDescuentoDTO.setMotivoDescuentoDTO(new MotivoDescuentoDTO());
                consultaDescuentoDTO.setTipoDescuentoDTO(new TipoDescuentoDTO());
                
                consultaMotivoDescuentoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
                consultaMotivoDescuentoDTO.setEstadoMotivoDescuento(MessagesAplicacionSISPE
                        .getString("ec.com.smx.sic.sispe.estado.activo"));
                //Obtiene los motivos de descuentos activos
                Collection motivoDescuentos = SessionManagerSISPE.getServicioClienteServicio()
                	.transObtenerMotivoDescuento(consultaMotivoDescuentoDTO);
                
                Collection descuentos = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuento(consultaDescuentoDTO);
                LogSISPE.getLog().info("DESCUENTOS: "+descuentos);
                //establece las variables de sesi\u00F3n para almacenar las colecciones descuentos y motivos
                session.setAttribute("ec.com.smx.sic.sispe.motivoDescuento", motivoDescuentos);
                session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos", descuentos);
                session.setAttribute("ec.com.smx.sic.sispe.TipoDescuentoDTO",tipoDescuentoDTO);
                
                //establece las varibles de sesi\u00F3n para estados
                session.setAttribute("ec.com.smx.sic.sispe.estado.activo",
                        MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
                session.setAttribute("ec.com.smx.sic.sispe.estado.inactivo",
                        MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo"));
                
                session.setAttribute("WebSISPE.tituloVentana", tipoDescuentoDTO.getDescripcionTipoDescuento()+ ": Parametrizar Descuentos");
                
                //comprobaci\u00F3n de carga de listado para los combos
                //se guardan los mansajes informativos en sesi\u00F3n para que no se pierdan
                ControlMensajes controlMensajes = new ControlMensajes();
                if (descuentos == null || descuentos.isEmpty()) 
                {
                    controlMensajes.setMessages(session,"message.listaVacia","Descuentos");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
                }
                if (motivoDescuentos == null || motivoDescuentos.isEmpty()) 
                {
                    controlMensajes.setMessages(session,"message.listaVacia","Motivo Descuentos");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
                }
                LogSISPE.getLog().info("DATOS DE DESCUENTOS");
                
            }
        }
        catch (SISPEException e) 
        {
            LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
            //se guarda el mensaje de error en sesi\u00F3n
            ControlMensajes controlMensajes = new ControlMensajes();
            controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","Descuentos");
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