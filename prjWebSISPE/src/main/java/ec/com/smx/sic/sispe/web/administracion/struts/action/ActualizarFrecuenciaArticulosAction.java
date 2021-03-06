/*
 * Creado el 10/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.dao.DataIntegrityViolationException;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.FrecuenciaArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.FrecuenciaDTO;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.web.administracion.struts.form.FrecuenciaArticulosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Clase para actualizar las frecuencias de art\u00EDculos
 * </p>
 * 
 * @author cbarahona
 * @version 1.0
 * @since JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class ActualizarFrecuenciaArticulosAction extends BaseAction {
	/**
	 * Esta acci\u00F3n permite hacer actualizaciones de una frecuencia espec\u00EDfica y
	 * agregar nuevos art\u00EDculos a la lista de frecuencia art\u00EDculo, tiene las
	 * siguientes opciones:
	 * <ul>
	 * <li>Actualizar una frecuencia</li>
	 * <li>Agregar un art\u00EDculo a la frecuencia</li>
	 * <li>Cancelar una actualizaci\u00F3n</li>
	 * </ul>
	 * 
	 * @param mapping
	 *            El mapeo utilizado para seleccionar esta instancia
	 * @param form
	 *            El formulario asociado a esta acci\u00F3n de donde se toman y
	 *            establecen valores de campos
	 * @param request
	 *            El request que estamos procesando
	 * @param response
	 *            La respuesta HTTP que se crea
	 * @return ActionForward Describe donde y como se redirige el control
	 * @throws Exception
	 *             error generado por struts
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		String salida = "frecuenciaArticulos";
		String ayuda = request.getParameter(Globals.AYUDA);
		FrecuenciaArticulosForm formulario = (FrecuenciaArticulosForm) form;

		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		try {
			//	el usuario quiere activar o desactivar un art\u00EDculo de la lista
			if (ayuda != null && (ayuda.startsWith("desactivar") || ayuda.startsWith("activar"))) {
				//obtener el \u00EDndice de desactivaci\u00F3n a partir de una subcadena de la ayuda
				int indice = -1;
				
				//si el  usuario quiere desactivar
				if(ayuda.startsWith("desactivar")){
					indice = Integer.parseInt(
									ayuda.substring("desactivar".length())
								);
				}
				//el usuario quiere activar el art\u00EDculo
				else{
					indice = Integer.parseInt(
							ayuda.substring("activar".length())
						);
				}

				List frecuenciaArticulos = new ArrayList((Collection)session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos"));
				//Obtenemos la relaci\u00F3n frecuencia art\u00EDculo de la lista de
				// art\u00EDculos
				FrecuenciaArticuloDTO frecuenciaArticuloDTO = (FrecuenciaArticuloDTO) frecuenciaArticulos.get(indice);

				//preguntamos si el estado del art\u00EDculo es activo
				if (frecuenciaArticuloDTO.getEstadoFrecuenciaArticulo().equals(
						estadoActivo)) {
					frecuenciaArticuloDTO
							.setEstadoFrecuenciaArticulo(estadoInactivo);
				} else {
					frecuenciaArticuloDTO
							.setEstadoFrecuenciaArticulo(estadoActivo);
				}

				//cambiamos en la lista de frecuencia articulos y subimos a la
				// sesi\u00F3n
				frecuenciaArticulos.set(indice, frecuenciaArticuloDTO);
				session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos", frecuenciaArticulos);
				salida = "actualizarFrecuenciaArticulos";
			}
			//el usuario quiere eliminar un art\u00EDculo de la frecuencia
			else if (request.getParameter("indiceEliminacion")!=null ) {

				ArrayList frecuenciaArticulos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos");
		        ArrayList codigoArticulos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos");
		        try{
		        	int indice = Integer.parseInt(request.getParameter("indiceEliminacion"));
					//se obtiene el EspecialClasificacionDTO
					FrecuenciaArticuloDTO frecuenciaArticuloDTO = (FrecuenciaArticuloDTO) frecuenciaArticulos.get(indice);
					if(frecuenciaArticuloDTO.getId().getCodigoFrecuencia()!=null){
						frecuenciaArticuloDTO.setEstadoFrecuenciaArticulo(null);
					}
					else{
						codigoArticulos.remove(indice);  
						frecuenciaArticulos.remove(indice);						
					}					
					session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos",codigoArticulos);
					session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos",frecuenciaArticulos);
		        }
		        catch(IndexOutOfBoundsException ex){
		          //si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
		          messages.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
		          saveErrors(request,messages);
		        }
				salida = "actualizarFrecuenciaArticulos";
			}

			//el usuario quiere guardar la frecuencia actual
			else if (ayuda != null && ayuda.equals("guardarFrecuencia")) {
				ArrayList frecuencias = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuencias");
				
					//establezco las propiedades de la frecuencia
					FrecuenciaDTO frecuencia = (FrecuenciaDTO) frecuencias.get(formulario.getIndiceFrecuencia());
					frecuencia.setEstadoFrecuencia(formulario.getEstadoFrecuencia());
					frecuencia.setDescripcionFrecuencia(formulario.getDescripcionFrecuencia());
					//frecuencia.setFrecuenciasArticulos((Collection) session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos"));
					Collection<FrecuenciaArticuloDTO> frecuenciaArticulos = new HashSet<FrecuenciaArticuloDTO>((Collection<FrecuenciaArticuloDTO>)
							session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos"));
					frecuencia.setFrecuenciasArticulos(frecuenciaArticulos);
					frecuencia.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
				try{
					//llamada al m\u00E9todo del servicio para almacenar el nuevo
					// registro
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarFrecuencia(frecuencia);
	
					//mensajes de guardado correctamente
					ControlMensajes controlMensajes = new ControlMensajes();
			        controlMensajes.setMessages(session,"message.exito.actualizacion","La Frecuencia");
			        session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");	
					salida = "frecuenciaArticulos";	
				}catch(DataIntegrityViolationException ex){
					LogSISPE.getLog().error("Error al eliminar articulo de frecuencia articulo por ser clave foranea en otra tabla",ex);					
					//si un articulo que se intenta eliminar es clave foranea en otra tabla
					messages.add("claveForaneaArticulo",new ActionMessage("errors.claveForaneaArticulo"));
					saveErrors(request,messages);
					salida = "actualizarFrecuenciaArticulos";
				}
			}

			//el usuario quiere agregar un nuevo articulo a la frecuencia
			else if (ayuda != null && ayuda.equals("agregarArticulo")) {
				LogSISPE.getLog().info("----------------------------------------");
				LogSISPE.getLog().info("Agregar Articulo");
				LogSISPE.getLog().info("----------------------------------------");
				Collection codigoArticulos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos");			
				//codigo de art\u00EDculos temporal
				List codArticulosTmp = new ArrayList(codigoArticulos);
				
				//obtenemos el c\u00F3digo del art\u00EDculo a agregar del formulario
				String codigoArticulo = formulario.getCodigoArticulo();
				codigoArticulo = codigoArticulo.trim();
				//buscamos el indice del c\u00F3digo de art\u00EDculo dentro de la lista de c\u00F3digos		
				int indice = codArticulosTmp.indexOf(codigoArticulo);
				LogSISPE.getLog().info("----------------------------------------");
				LogSISPE.getLog().info("   "+indice+"   ");
				LogSISPE.getLog().info("----------------------------------------");
				
				//si el art\u00EDculo no esta en la lista de articulos de las frecuencias
				if(indice < 0)
				{
					//se construye el objeto articuloDTO
					ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
					consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					//consultaArticuloDTO.getId().setCodigoArticulo(formulario.getCodigoArticulo());
					consultaArticuloDTO.setNpCodigoBarras(formulario.getCodigoArticulo());
					consultaArticuloDTO.setEstadoArticulo(estadoActivo);
					consultaArticuloDTO.setNpCodigoLocal(new Integer(0));
					consultaArticuloDTO.setNpDetalle(SessionManagerSISPE.getEstadoInactivo(request));
					consultaArticuloDTO.setNpActivarInterfazSIC(estadoInactivo);
					consultaArticuloDTO.setNpCodigoArticuloPadre(estadoInactivo);
					
					//obtener art\u00EDculo de la base de datos
					ArticuloDTO articuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(consultaArticuloDTO);
					
					if(articuloDTO!=null){
						//se construye el objeto de los especialesClasificacionDTO
						FrecuenciaArticuloDTO frecuenciaArticuloDTO = new FrecuenciaArticuloDTO();
						frecuenciaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						frecuenciaArticuloDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
						frecuenciaArticuloDTO.setEstadoFrecuenciaArticulo(estadoActivo);
						frecuenciaArticuloDTO.setArticuloDTO(articuloDTO);
						Collection frecuenciaArticulos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos");
						
						//agregar la nueva frecuencia art\u00EDculo a la colecci\u00F3n de la frecuencia
						frecuenciaArticulos.add(frecuenciaArticuloDTO);
						codigoArticulos.add(formulario.getCodigoArticulo());
						session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos",frecuenciaArticulos);
						session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos",codigoArticulos);
					}
					// si el art\u00EDculo no existe en la base de datos
					else{
						messages.add("articuloNoEncontrado",new ActionMessage("message.codigo.invalido","un art\u00EDculo"));
						saveInfos(request,messages);	            
					}
				}else{    
					ArrayList frecuenciaArticulos = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos");	
						int indiceCodigo = codArticulosTmp.indexOf(formulario.getCodigoArticulo());
						FrecuenciaArticuloDTO frecuenciaArticuloDTO = (FrecuenciaArticuloDTO)frecuenciaArticulos.get(indiceCodigo);
						if(frecuenciaArticuloDTO.getEstadoFrecuenciaArticulo()==null){
							frecuenciaArticuloDTO.setEstadoFrecuenciaArticulo(estadoActivo);
						}						
						else{
							//si el c\u00F3digo del art\u00EDculo ya se encuentra en la colecci\u00F3n de frecuencia art\u00EDculos
							messages.add("articuloExistente",new ActionMessage("errors.codigoRepetido.nuevaLista",formulario.getCodigoArticulo(),String.valueOf(indice+1), "listado"));
							saveErrors(request,messages);
						}				          
				}			
				
				salida = "actualizarFrecuenciaArticulos";
			}
		}catch (Exception ex) {
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida = "errorGlobal";
		}
		return mapping.findForward(salida);
	}
}
