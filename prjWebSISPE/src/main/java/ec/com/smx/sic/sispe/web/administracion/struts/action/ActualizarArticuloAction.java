/*
 * ActualizarArticuloAction.java
 * Creado el 21/11/2007 11:22:46
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.web.administracion.struts.form.MantenimientoArticulosForm;

/**
 * <p>
 * Controla la actualizaci\u00F3n de los datos de un art\u00EDculo
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5	 	
 */
public class ActualizarArticuloAction extends BaseAction
{
  
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Control de cambios en los datos del art\u00EDculo</li>
   * <li>Guardar los nuevos datos del art\u00EDculo</li>
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
  	ActionMessages errors = new ActionMessages();
  	ActionMessages success = new ActionMessages();
  	
    HttpSession session = request.getSession();
    MantenimientoArticulosForm formulario = (MantenimientoArticulosForm)form;
    
    String salida = "actualizarArticulo";
    
  	//se verifica el valor de la petici\u00F3n
  	String peticion = request.getParameter("ayuda");
  	if(peticion == null){
  		peticion = "";
  	}
  	
  	//------------- cuando se guarda la actualizaci\u00F3n de los datos del art\u00EDculo -----------
  	if(peticion.equals("actualizarArticulo")){
  		//se actualizan los datos del art\u00EDculo guardao en sesi\u00F3n
  		ArticuloDTO articuloDTO = (ArticuloDTO)session.getAttribute(MantenimientoArticulosAction.ARTICULO_SELECCIONADO);
  		articuloDTO.setDescripcionArticulo(formulario.getDescripcionArticulo());
//  		articuloDTO.setPrecioArticulo(Double.parseDouble(formulario.getPrecioArticulo()));
//  		articuloDTO.setPrecioCaja(Double.parseDouble(formulario.getPrecioCajaArticulo()));
  		//se verifica si el art\u00EDculo tiene IVA
//  		if(articuloDTO.getAplicaIVA().equals(SessionManagerSISPE.getEstadoActivo(request))){
//  			//se calcula el precio unitario con IVA
//  			double precioIVA = articuloDTO.getPrecioArticulo() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//  			articuloDTO.setPrecioArticuloIVA(Util.roundDoubleMath(new Double(precioIVA), NUMERO_DECIMALES));
//  			
//  			//se calcula el precio de caja con IVA
//  			double precioCajaIVA = articuloDTO.getPrecioCaja() * (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//  			articuloDTO.setPrecioCajaIVA(Util.roundDoubleMath(new Double(precioCajaIVA), NUMERO_DECIMALES));
//  		}else{
//  			articuloDTO.setPrecioArticuloIVA(articuloDTO.getPrecioArticulo());
//  			articuloDTO.setPrecioCajaIVA(articuloDTO.getPrecioCaja());
//  		}
//  		articuloDTO.setPesoAproximado(Double.parseDouble(formulario.getPesoAproximado()));
//  		articuloDTO.setUnidadManejo(Long.parseLong(formulario.getUnidadManejo()));
//  		articuloDTO.setUnidadManejoCaja(Long.parseLong(formulario.getUnidadManejoCaja()));
//  		articuloDTO.setEstadoArticulo(formulario.getEstadoArticulo());
//  		articuloDTO.setTipoArticuloCalculoPrecio(formulario.getTipoArticuloCalculoPrecio());
  		
//    	try{
//    		//lamada al m\u00E9todo que registra el art\u00EDculo
//    		//SessionManagerSISPE.getServicioClienteServicio().transActualizarArticulo(articuloDTO);
//    		success.add("exitoActualizacion", new ActionMessage("message.exito.actualizacion", "El art\u00EDculo"));
//    		salida = "listadoArticulos";
//    	}catch(SISPEException ex){
//    		errors.add("errorActualizacion", new ActionMessage("errors.llamadaServicio.registrarDatos", "la actualizaci\u00F3n de art\u00EDculos"));
//    	}
  	}
  	//------ cuando se cancela la actualizaci\u00F3n de los datos del art\u00EDculo -----------
  	else if(request.getParameter("accionCancelada") != null){
  		salida = "listadoArticulos";
  	}
  	
  	//se guardan los mensajes generados
  	saveErrors(request, errors);
  	saveMessages(request, success);
  	
  	return mapping.findForward(salida);
  }
}
