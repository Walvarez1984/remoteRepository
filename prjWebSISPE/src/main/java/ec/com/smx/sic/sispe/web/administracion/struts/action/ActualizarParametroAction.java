/*
 * Clase ActualizarParametroAction.java
 * Creado el 12/05/2006
 *
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
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ParametrosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * <p>
 * Esta clase permite actualizar los par&aacute;metros generales de la aplicaci&oacute;n.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
@SuppressWarnings("unchecked")
public class ActualizarParametroAction extends BaseAction
{
  
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) 
   * o lo redirige a otro componente web que podr\u00EDa crear. Devuelve una instancia <code>ActionForward</code>
   * que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping				El mapeo utilizado para seleccionar esta instancia
   * @param request				El request que estamos procesando
   * @param form				El formulario (si lo hay) asociado a la acci\u00F3n que estamos utilizando	
   * @param response			La respuesta HTTP que se crea
   * @return ActionForward		Describe donde y como se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)throws Exception{
  	
    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    ParametrosForm formulario = (ParametrosForm)form;
    String salida = "desplegar";
    
    try
    {
      if(request.getParameter("indice")!=null)
      {
        //colecci\u00F3n que almacena los registros de los Par\u00E1metros generales
        ArrayList parametrosGenerales = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
        int indice = Integer.parseInt(request.getParameter("indice"));
        
        //se guarda en sesi\u00F3n el indice de la colecci\u00F3n
        session.setAttribute("ec.com.smx.sic.sispe.indice",Integer.toString(indice));
        
        ParametroDTO parametroDTO = (ParametroDTO)parametrosGenerales.get(indice);
        formulario.setDescripcionParametro(parametroDTO.getDescripcionParametro());
        formulario.setValorParametro(parametroDTO.getValorParametro());
      }
      else if(formulario.getBotonActualizar()!=null)
      {
        //se obtiene la colecci\u00F3n de parametros 
        List<ParametroDTO> parametros = (List<ParametroDTO>)session.getAttribute("ec.com.smx.sic.sispe.tabla");
        
        //se obtiene el indice de la colecci\u00F3n
        String indice = (String)session.getAttribute("ec.com.smx.sic.sispe.indice");
        
        ParametroDTO parametroDTO = parametros.get(Integer.parseInt(indice));
        parametroDTO.setDescripcionParametro(formulario.getDescripcionParametro());
        parametroDTO.setValorParametro(formulario.getValorParametro());
//        parametroDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
        //se actualiza la colecci\u00F3n
        parametros.set(Integer.parseInt(indice),parametroDTO);
        try{
          //llamada al m\u00E9todo del servicio para almacenar el nuevo registro
          SessionManagerSISPE.getServicioClienteServicio().transActualizarParametro(parametroDTO);  
          session.setAttribute("ec.com.smx.sic.sispe.tabla",parametros);
          ControlMensajes controlMensajes = new ControlMensajes();
          controlMensajes.setMessages(session,"message.exito.actualizacion","El Par\u00E1metro");
          session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");
          salida = "listado";
        }catch(SISPEException ex){
          //si falla el m\u00E9todo de actualizaci\u00F3n de datos
          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          messages.add("actualizarParametro", new ActionMessage("errors.llamadaServicio.registrarDatos","el Par\u00E1metro"));
          saveErrors(request, messages);
        }
      }
      else if(formulario.getBotonCancelar()!=null || request.getParameter("volver")!=null)
      {
        salida="listado";
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
