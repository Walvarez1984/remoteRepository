/*
 * Clase AdministracionListadoAction.java
 * Creado el 22/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.AdministracionListadoForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite la administracion del listado de todas los formularios de mantenimiento. 
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */
public class AdministracionListadoAction extends BaseAction
{
 /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) 
   * (o lo redirige a otro componente web que podr\u00EDa crear). Devuelve una instancia <code>ActionForward</code> 
   * que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param request			El request que estamos procesando
   * @param form			La instancia ActionForm que estamos utilizando (si la hay)
   * @param response		La respuesta HTTP que se crea
   * @return ActionForward 	Describe donde y como se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
  {
    ActionMessages messages = new ActionMessages();
    HttpSession session = request.getSession();
    AdministracionListadoForm formulario = (AdministracionListadoForm)form;
    String salida = "listado";

    try
    {
      /*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
      if(request.getParameter("range")!=null || request.getParameter("start")!=null)
      {
        LogSISPE.getLog().info("ENTRO A LA PAGINACION");
        Collection datos= (Collection)session.getAttribute("ec.com.smx.sic.sispe.tabla");
        int start = request.getParameter("start") != null ? Integer.parseInt(request.getParameter("start")) : 0;
        
        //se llama a la funci\u00F3n que realiza la paginaci\u00F3n
        realizarPaginacion(formulario, datos, start, Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango")));
      }
      /*-------------------------- cuando se desea crear un nuevo registro --------------------------------*/
      else if(formulario.getBotonNuevo()!=null){
        //se obtiene de sesi\u00F3n la acci\u00F3n que se est\u00E1 realizando
        String accion = (String)session.getAttribute("ec.com.smx.sic.sispe.accion");
        LogSISPE.getLog().info("LA ACCION ES (AL): "+accion);
        //si la accion escogida fu\u00E9 par\u00E1metros
        /*if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.parametro"))) {
          salida = "nuevoParametro";
        }*/
        if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.temporada"))) {
          salida = "nuevaTemporada";
        } else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.autorizacion"))) {
          salida = "nuevaAutorizacion";
        }else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.descuento"))) {
          salida = "nuevoDescuento";
        }else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.tipoDescuento"))) {
          salida = "nuevoTipoDescuento";
        }else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.motivoDescuento"))) {
          salida = "nuevoMotivoDescuentos";
        }else if (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.especial"))) {
        	salida = "nuevoEspecial";
        }
        formulario.setBotonNuevo(null);
      }
      /*-------------------------- cuando se da clic en el bot\u00F3n atras --------------------------------*/
      else if(request.getParameter("volver")!=null){
        salida="tipoDescuentos";
      }
      /*-------------------------- cuando se da clic en el bot\u00F3n salir --------------------------------*/
      else if(formulario.getBotonSalir()!=null)
      {
        String accion = (String)session.getAttribute("ec.com.smx.sic.sispe.accion");
        if(accion!=null && accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.descuento"))){
          session.setAttribute("ec.com.smx.sic.sispe.accion",
              MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.tipoDescuento"));
          salida="tipoDescuentos";
        }else{
          //se eliminan las variables de sesi\u00F3n que comienzen con "ec"
          SessionManagerSISPE.removeVarSession(request);
          salida = "menuPrincipal";
        }
      }
      /*---------- se ejecuta la primera vez que entra a la acci\u00F3n -------------*/
      else
      {
        LogSISPE.getLog().info("EN ADMINISTRACION LISTADO");
        LogSISPE.getLog().info("POR EL ELSE");
        String accion = (String)session.getAttribute("ec.com.smx.sic.sispe.accion");
        Collection datos = null;
        if(accion!=null && accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.descuento"))){
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos");
        }else
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.tabla");
        
        //se llama a la funci\u00F3n que realiza la paginaci\u00F3n
        realizarPaginacion(formulario, datos, 0, Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango")));
        
        //se obtiene un mensaje de exito de la sesi\u00F3n
        ControlMensajes controlMensajes = new ControlMensajes();
        messages = controlMensajes.getMessages(messages,session);
        if(!messages.isEmpty()){
          //se controla el tipo de mensaje que se va a mostrar
          String tipoMensaje = (String)session.getAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
          if(tipoMensaje!=null && tipoMensaje.equals("error"))
            saveErrors(request,messages);
          else if(tipoMensaje!=null && tipoMensaje.equals("info"))
            saveInfos(request,messages);
          else
            saveMessages(request,messages);
        }
        session.removeAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
      }
    }
    catch(Exception ex){
      //excepcion desconocida
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    return mapping.findForward(salida);
  }
  
  /**
   * Realiza la paginaci\u00F3n del listado principal
   * @param formulario
   * @param datos
   * @param start
   * @param range
   */
  public void realizarPaginacion(ActionForm actionForm, Collection datos, int start, int range)
  {
  	if(actionForm instanceof AdministracionListadoForm){
  		AdministracionListadoForm formulario = (AdministracionListadoForm)actionForm;
	  	//se verifica si existen datos
	  	if(datos!=null && !datos.isEmpty()){
		    formulario.setSize(String.valueOf(datos.size()));
		    int size = datos.size();
		    formulario.setStart(String.valueOf(start));
		    formulario.setRange(String.valueOf(range));
		    formulario.setSize(String.valueOf(size));
		    Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
		    formulario.setDatos(datosSub);
	  	}
  	}
  }
}
