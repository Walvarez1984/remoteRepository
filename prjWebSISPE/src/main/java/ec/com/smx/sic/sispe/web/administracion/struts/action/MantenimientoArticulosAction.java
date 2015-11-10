/*
 * MantenimientoArticulosAction.java
 * Creado el 21/11/2007 11:22:17
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.MantenimientoArticulosForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Permite actualizar los datos de un conjunto de art\u00EDculos mediante una b\u00FAsqueda con diferentes filtros.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5	 	 
 */
@SuppressWarnings("unchecked")
public class MantenimientoArticulosAction extends BaseAction
{
	//nombres de las variables de sesi\u00F3n para el manejo de art\u00EDculos
	public final static String LISTA_ARTICULOS = "ec.com.smx.sic.sispe.mantenimiento.listaArticulos";  
	public final static String ARTICULO_SELECCIONADO = "ec.com.smx.sic.sispe.articuloSeleccionado";
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * Este m\u00E9todo permite:
   * <ul>
   * <li>Manejar un listado de art\u00EDculos luego de una b\u00FAsqueda</li>
   * <li>Acceder a los datos de un determinado art\u00EDculo, para su actualizaci\u00F3n</li>
   * <li>Guardar los nuevos datos de un conjkunto de art\u00EDculos</li>
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
  	//objetos para los mensajes generados
  	ActionMessages success = new ActionMessages();
  	ActionMessages infos = new ActionMessages();
  	ActionMessages errors = new ActionMessages();
  	
    HttpSession session = request.getSession();
    MantenimientoArticulosForm formulario = (MantenimientoArticulosForm)form;
    
    String salida = "mantenimientoArticulos";
    
  	//se verifica el valor de la petici\u00F3n
  	String peticion = request.getParameter("ayuda");
  	if(peticion == null){
  		peticion = "";
  	}
  	
  	try{
	  	//-------------- cuando se desea actualizar los datos de un pedido ---------------
	  	if(request.getParameter("indiceArticulo") != null){
	  		//se declara el indice seleccionado
	  		int indice = Integer.parseInt(request.getParameter("indiceArticulo"));
	  		//se obtiene la colecci\u00F3n de art\u00EDculos
	  		ArrayList<ArticuloDTO> articulos = (ArrayList<ArticuloDTO>)session.getAttribute(LISTA_ARTICULOS);
	  		ArticuloDTO articuloDTO = articulos.get(indice);
	  		
	  		//se inicializan los datos del formulario
	  		formulario.setDescripcionArticulo(articuloDTO.getDescripcionArticulo());
	  		//formulario.setPrecioArticulo(articuloDTO.getPrecioArticulo().toString());
	  		formulario.setPrecioArticulo(articuloDTO.getPrecioBase().toString());
	  		formulario.setPrecioCajaArticulo(articuloDTO.getPrecioCaja().toString());
	  		//formulario.setPesoAproximado(articuloDTO.getPesoAproximado().toString());
	  		formulario.setPesoAproximado(articuloDTO.getArticuloComercialDTO().getPesoAproximadoVenta().toString());
	  		formulario.setUnidadManejo(articuloDTO.getUnidadManejo().toString());
	  		formulario.setUnidadManejoCaja(articuloDTO.getUnidadManejo().toString());
	  		formulario.setEstadoArticulo(articuloDTO.getEstadoArticulo());
	  		formulario.setTipoArticuloCalculoPrecio(articuloDTO.getTipoCalculoPrecio());
	  		
	  		//se guarda en sesi\u00F3n el art\u00EDculo seleccionado
	  		session.setAttribute(ARTICULO_SELECCIONADO, articuloDTO);
	  		
	  		//se guardan en sesi\u00F3n los tipos de art\u00EDculo para los c\u00E1lculos
	  		if(session.getAttribute("ec.com.smx.sic.sispe.articulo.normal") == null){
	  			//c\u00F3digos de los tipos de art\u00EDculos para realizar los calculos
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.normal", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.normal"));
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.pavo", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo"));
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.otroPesoVariable", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.otroPesoVariable"));
	  			
	  			//descripci\u00F3n de los tipos de art\u00EDculos para realizar los calculos
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.normal.descripcion", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.normal.descripcion"));
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.pavo.descripcion", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.pavo.descripcion"));
	  			session.setAttribute("ec.com.smx.sic.sispe.articulo.otroPesoVariable.descripcion", 
	  					MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.articulo.otroPesoVariable.descripcion"));
	  		}
	  		
	  		salida = "actualizarArticulo";
	  	}
	  	//------------- cuando se desea agregar un art\u00EDculo por su c\u00F3digo ------------
	  	else if(request.getParameter("agregarArticulo") != null){
	      //se obtiene la colecci\u00F3n de art\u00EDculos
	      Collection <ArticuloDTO> listaArticulos = (Collection <ArticuloDTO>)session.getAttribute(LISTA_ARTICULOS);
	      if(listaArticulos == null)
	      	listaArticulos = new ArrayList<ArticuloDTO>();
	      
	      //se verifica que el c\u00F3digo de art\u00EDculo a ingresar no est\u00E9 en el listado
	      for(ArticuloDTO articuloDTO : listaArticulos) {
					if(formulario.getCodigoArticulo().trim().equals(articuloDTO.getId().getCodigoArticulo())){
						errors.add("codigoArticulo", new ActionMessage("errors.itemRepetido", "El art\u00EDculo"));
						break;
					}
				}
	      
	      //solo si no hubo errores anteriormente
	      if(errors.isEmpty()){
	      	//se construye el objeto para la condulta
		      ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
		      consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		      consultaArticuloDTO.getId().setCodigoArticulo(formulario.getCodigoArticulo().trim());
		      
		      //llamada al m\u00E9todo de la capa de servicio que devuelve la colecci\u00F3n de art\u00EDculos
		      Collection <ArticuloDTO> articulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
		      if(!articulos.isEmpty()){
		      	Iterator <ArticuloDTO> it = articulos.iterator();
		      	ArticuloDTO articuloDTO = it.next();
		      
		      	//se agrega el art\u00EDculo a la colecci\u00F3n
		      	listaArticulos.add(articuloDTO);
		      	session.setAttribute(LISTA_ARTICULOS, listaArticulos);
		      }else{
		      	infos.add("articulosInvalido",new ActionMessage("message.codigoBarras.invalido"));
		      }
	      }
	  	}
	  	//------------- cuando se agregan art\u00EDculos por la b\u00FAsqueda ------------
	  	else if(request.getParameter("agregarArticulos") != null){
	  		
	  		//se obtiene la colecci\u00F3n de art\u00EDculos agregados por la b\u00FAsqueda
	  		Collection<ArticuloDTO> articulosBuscados = (Collection <ArticuloDTO>)session.getAttribute(BuscarArticuloAction.ARTICULOS_AGREGADOS_MANTENIMIENTO);
	  		if(articulosBuscados!=null){
	  			LogSISPE.getLog().info("longitud agregados: "+articulosBuscados.size());
	  			//se obtiene la colecci\u00F3n de art\u00EDculos
		      Collection <ArticuloDTO> listaArticulos = (Collection <ArticuloDTO>)session.getAttribute(LISTA_ARTICULOS);
		      if(listaArticulos == null)
		      	listaArticulos = new ArrayList<ArticuloDTO>();
		      
		      //se crea la colecci\u00F3n para agregar los nuevos art\u00EDculos
		      Collection <ArticuloDTO> articulosParaAgregar = new ArrayList<ArticuloDTO>();
		      //se itera la colecci\u00F3n de art\u00EDculos buscados
		      for(ArticuloDTO articuloBuscado : articulosBuscados){
		      	boolean seEncontroArticulo = false;
		      	if(!listaArticulos.isEmpty()){
				      //se verifica que el c\u00F3digo de art\u00EDculo buscado no este en el listado principal
				      for(ArticuloDTO articuloDTO : listaArticulos) {
								if(articuloBuscado.getId().getCodigoArticulo().equals(articuloDTO.getId().getCodigoArticulo())){
									seEncontroArticulo = true;
									break;
								}
							}
				      
				      if(!seEncontroArticulo)
				      	articulosParaAgregar.add(articuloBuscado);
				      	
		      	}else{
		      		articulosParaAgregar.add(articuloBuscado);
		      	}
		      }
		      
		      //se agregan todos los art\u00EDculos
		      listaArticulos.addAll(articulosParaAgregar);
		      session.setAttribute(LISTA_ARTICULOS, listaArticulos);
		      
		      //se elimina la colecci\u00F3n de articulos buscados
		      session.removeAttribute(BuscarArticuloAction.ARTICULOS_AGREGADOS_MANTENIMIENTO);
	  		}
	  	}
	  	//-------------- caso por omisi\u00F3n ---------------
	  	else{
	  		//primero se eliminan las posibles variables de sesi\u00F3n
	  		SessionManagerSISPE.removeVarSession(request);
	  		
	  		//se inicializa algunos datos del formulario
	  		formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
	  		
	  		//se crea una variable para determinar como se debe manejar la adici\u00F3n de art\u00EDculos por la b\u00FAsqueda
	  		session.setAttribute("ec.com.smx.sic.sispe.busqueda.porMantenimiento", "ok");
	  	}
	  	
	  	//se guardan los mensajes generados
	  	saveMessages(request, success);
	  	saveInfos(request, infos);
	  	saveErrors(request, errors);
	  	
  	}catch(Exception ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
  	}
  	
  	//salida hacia la p\u00E1gina adecuada
  	return mapping.findForward(salida);
  }
}
