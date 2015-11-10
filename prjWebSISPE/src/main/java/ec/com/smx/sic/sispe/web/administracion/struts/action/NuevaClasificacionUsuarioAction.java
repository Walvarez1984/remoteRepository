/*
 * NuevaClasificacionUsuarioAction.java
 * Creado el 19/09/2007
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

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.sispe.web.administracion.struts.form.ClasificacionUsuarioForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;

/**
 * <p>
 * Permite controlar la creaci\u00F3n de una nueva configuraci\u00F3n de Acceso a las clasificaciones 
 * para los diferentes usuarios.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5	 	
 */
@SuppressWarnings("unchecked")
public class NuevaClasificacionUsuarioAction extends BaseAction
{
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Controlar la asignaci\u00F3n de usuarios para restringir el acceso a una determinada clasificaci\u00F3n 
   * de art\u00EDculos</li>
   * </ul>
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request					El request que estamos procesando
   * @param response 				La respuesta HTTP que se crea
   * @throws Exception
   * @return ActionForward	Describe donde y como se redirige el control
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
  	//objetos para los mensajes
  	ActionMessages errors = new ActionMessages();
  	ActionMessages success = new ActionMessages();

  	HttpSession session = request.getSession();
  	ClasificacionUsuarioForm formulario = (ClasificacionUsuarioForm) form;
  	String salida = "nueva";
  	
  	//------------ cuando se desea guardar la actualizaci\u00F3n de la configuraci\u00F3n --------
  	if(formulario.getGuardarClasificacionUsuario()!=null){
  		//lamada al m\u00E9todo que guarda los datos del formulario
  		if(ClasificacionUsuarioAction.guardarNuevaConfiguracionUsuario(request, formulario.getIdUsuario(), 
  				errors, success)){
  			//solo si el registro fue exitoso
  	  	//redirecci\u00F3n a la acci\u00F3n principal
        salida = "clasificacionUsuario";
  		}
  	}
  	//------------ cuando se desea elminar un usuario de las lista --------------
  	else if(request.getParameter("eliminarClasificacion")!=null){
  		//se llama al m\u00E9todo que realiza la eliminaci\u00F3n
  		ClasificacionUsuarioAction.eliminarClasificacionUsuario(request, formulario.getChecksSeleccion(), "N");
  		formulario.setChecksSeleccion(null);
  	}
  	//------------ cuando se desea desactivar un usuario -------------
  	else if(request.getParameter("activarDesactivarClasificacion")!=null){
  		//se llama al m\u00E9todo que realiza la eliminaci\u00F3n
  		ClasificacionUsuarioAction.activarDesactivarClasificacionUsuario(request, formulario.getChecksSeleccion());
  		formulario.setChecksSeleccion(null);
  	}
  	//------------ cuando se desea agregar una clasificaci\u00F3n al listado -------------
  	else if(request.getParameter("agregarClasificacion")!=null){
  		//lamada al m\u00E9todo que realiza la adici\u00F3n unitaria
  		ClasificacionUsuarioAction.agregarClasificacionUsuario(request, formulario.getClasificacion(), errors);
  	}
  	//------------ cuando se desea agregar un listado de clasificaciones -------------
  	else if(request.getParameter("agregarClasificaciones")!=null){
  		Collection <ClasificacionDTO> clasificaciones = (Collection <ClasificacionDTO>)session.getAttribute(CatalogoArticulosAction.CLASIFICACIONES_AGREGADAS);
  		if(clasificaciones!=null){
  			//lamada al m\u00E9todo que realiza la adici\u00F3n de varias clasificaciones
  			ClasificacionUsuarioAction.agregarClasificacionesUsuario(request, clasificaciones);
  			session.removeAttribute(CatalogoArticulosAction.CLASIFICACIONES_AGREGADAS);
  		}
  	}
  	//se guardan los mensajes
  	saveMessages(request, success);
  	saveErrors(request, errors);
  	
  	//se redirecciona el control
  	return mapping.findForward(salida);
  }
}
