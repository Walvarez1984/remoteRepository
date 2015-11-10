/*
 * Clase PeguntaSiNoAction.java
 * Creado el 12/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ConfirmacionForm;

/**
 * <p>
 * Esta clase maneja las peticiones para aceptar(SI) o cancelar(NO) una determinada acci\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
public	class ConfirmacionAction extends BaseAction
{
	/**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control.
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param request			El request que estamos procesando
   * @param form			La instancia <code>ActionForm</code> que estamos utilizando (si la hay).
   * @param response		La respuesta HTTP que creamos
   * @return ActionForward	Describe donde y como se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
  {
    
    HttpSession session = request.getSession();   
    //creaci\u00F3n del formulario para acceder a sus campos
    ConfirmacionForm formulario = (ConfirmacionForm)form;
  	String salida = "confirmacion";
	
  	try
  	{
  	  if(formulario.getBotonSI()!=null)
  	  {
  	    if(session.getAttribute("ec.com.smx.sic.sispe.accionEscojida")!=null)
  	      //asigna la salida a la acci\u00F3n correspondiente
  	      salida = (String)session.getAttribute("ec.com.smx.sic.sispe.accionEscojida");
  	    else{  
  	      //si la llamada fue hecha desde la reservaci\u00F3n
  	      if(((String)session.getAttribute("ec.com.smx.sic.sispe.accion"))
  	          .equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")))
  	        salida="registrar";
  	    }
  	    LogSISPE.getLog().info("el usuario escogio SI");
  	    request.setAttribute("ec.com.smx.sic.sispe.respuestaPregunta.si","ok");
  	    session.removeAttribute("ec.com.smx.sic.sispe.accionEscojida");
  	  }
  	  else if(formulario.getBotonNO()!=null)
  	  {
  	    salida = "cotizarRecotizarReservar";
  	    session.removeAttribute("ec.com.smx.sic.sispe.accionEscojida");
  	    LogSISPE.getLog().info("el usuario escogio NO");
  	  }
  	}
  	catch(Exception ex){
  	  //EXCEPCION DESCONOCIDA
  	  LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
  	}
    return mapping.findForward(salida);
  }

}
