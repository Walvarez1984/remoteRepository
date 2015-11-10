/*
 * Clase ParametrosAction.java
 * Creado el 12/05/2006
 *
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

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ParametrosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Esta clase permite las peticiones sobre los par\u00E1metros generales, ya se para 
 * la creaci\u00F3n de uno nuevo o la actualizaci\u00F3n.
 * 
 * @author 	fmunoz
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2 
 */
public class ParametrosAction extends BaseAction
{
    /**
     * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
     * respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa 
     * crear)
     * 
     * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo 
     * se redirige el control.
     * 
     * @param mapping			El mapeo utilizado para seleccionar esta instancia
     * @param request			El request que estamos procesando
     * @param form			Esl formulario (si lo hay) asociado a la acci\u00F3n
     * @param response		La respuesta HTTP que se crea
     * @return ActionForward	Describe donde y como se redirige el control
     * @throws Exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
    throws Exception
    {
        //ParametrosForm formulario = (ParametrosForm)form;
        HttpSession session = request.getSession();
        ParametrosForm formulario = (ParametrosForm) form;
        String salida = "listado";
        
        try
        {
            
            if (request.getParameter("indice") != null) {
                //colecci\u00F3n que almacena los registros de los Par\u00E1metros generales
                ArrayList parametrosGenerales = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
                int indice = Integer.parseInt(request.getParameter("indice"));
                
                //se guarda en sesi\u00F3n el indice de la colecci\u00F3n
                session.setAttribute("ec.com.smx.sic.sispe.indice",Integer.toString(indice));
                
                ParametroDTO parametroDTO = (ParametroDTO)parametrosGenerales.get(indice);
                formulario.setDescripcionParametro(parametroDTO.getDescripcionParametro());
                formulario.setValorParametro(parametroDTO.getValorParametro());
            }
            
            else {
                //se eliminan las variables de sesi\u00F3n que comienzen con "ec"
                SessionManagerSISPE.removeVarSession(request);
                
                //colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
                Collection parametrosGenerales = new ArrayList();
                ParametroDTO consultaparametroDTO = new ParametroDTO(Boolean.TRUE);
                consultaparametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
                //llamada al m\u00E9todo del servicio para obtener la lista de registros
                session.setAttribute("ec.com.smx.sic.sispe.accion",
                        MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.parametro"));
                consultaparametroDTO.setGrupoSistema(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
                consultaparametroDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
                parametrosGenerales = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaparametroDTO);
                session.setAttribute("ec.com.smx.sic.sispe.tabla",parametrosGenerales);
                //	se guarda la lista de mensajes de error
                if(parametrosGenerales==null || parametrosGenerales.isEmpty()){
                    ControlMensajes controlMensajes = new ControlMensajes();
                    controlMensajes.setMessages(session,"message.listaVacia","Par\u00E1metros definidos");
                    session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","info");
                }
                session.setAttribute("WebSISPE.tituloVentana","Par\u00E1metros Generales");
                session.setAttribute("ec.com.smx.sic.sispe.imagenFormularioAdministracion", "parametros48.gif");
                session.setAttribute("ec.com.smx.sic.sispe.paginaAdministracion", "parametros.jsp");
                
            }
            
            
            
        }catch(SISPEException ex) {
            //si falla la llamada al m\u00E9todo del servicio
            ControlMensajes controlMensajes = new ControlMensajes();
            controlMensajes.setMessages(session,"errors.llamadaServicio.obtenerDatos","Par\u00E1metros Generales");
            session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","error");
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
        }
        catch(Exception ex){
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
            salida="errorGlobal";
        }
        
        return mapping.findForward(salida);
    }
}
