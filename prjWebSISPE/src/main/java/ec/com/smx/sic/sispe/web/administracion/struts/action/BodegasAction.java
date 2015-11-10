
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import ec.com.smx.framework.web.action.BaseAction;

/**
 * <p>
 * Esta clase permite listar todas las bodegas que existen en
 * la base de datos y permite acceder a opciones para editar y
 * crear una bodega.  
 * </p>
 * @author 	cbarahona 
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class BodegasAction extends BaseAction {
//	/**
//	 * Permite listar todas las bodegas existentes con las siguientes
//	 * opciones:
//	 * <ul>
//	 * <li>Listar bodegas existentes</li>	
//	 * <li>Preparar la p\u00E1gina para la edici\u00F3n de una bodega</li>
//	 * <li>Preparar la p\u00E1gina para creaci\u00F3n de una bodega</li>
//	 * </ul>
//	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
//	 * @param form			    El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
//	 * @param request			El request que estamos procesando
//	 * @param response		    La respuesta HTTP que se crea
//	 * @return ActionForward	Describe donde y como se redirige el control
//	 * @throws Exception        error generado por struts
//	 */
//	
//	public ActionForward execute(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//
//		ActionMessages messages = new ActionMessages();
//		HttpSession session = request.getSession();
//		String salida="";
//		BodegaForm formulario = (BodegaForm)form;
//
//		try{
//			//el usuario quiere crear una bodega preguntamos cual es su bodega padre
//			if(request.getParameter("codigoBodegaPadre")!= null){
//				String codigoBodegaPadre = request.getParameter("codigoBodegaPadre");
//				if(codigoBodegaPadre.equals("-1")){
//					formulario.setCodigoBodegaPadre(null);
//				}
//				else{
//					formulario.setCodigoBodegaPadre(codigoBodegaPadre);
//				}
//				salida = "nuevaBodega";				
//			}		
//			//el usuario quiere actualizar una bodega actual
//			else if(request.getParameter("codigoBodegaEditar") != null){
//
//				ArrayList bodegas = new ArrayList(
//						(Collection)session.getAttribute("ec.com.smx.sic.sispe.administracion.bodegas")
//				);
//
//				String codigoBodegaEditar = request.getParameter("codigoBodegaEditar");			
//				BodegaDTO bodegaDTO = null;
//
//				//buscamos el c\u00F3digo de la bodega de acuerdo a un c\u00F3digo espec\u00EDfico
//				for(Iterator it = bodegas.iterator();it.hasNext();){
//					bodegaDTO = (BodegaDTO)it.next();
//					if(bodegaDTO.getId().getCodigoBodega().equals(codigoBodegaEditar)){						
//						break;
//					}
//				}			
//
//				if(bodegaDTO != null){
//					//subir al formulario los datos de la bodega a actualizar	
//					formulario.setCodigoBodega(codigoBodegaEditar);
//					formulario.setCodigoBodegaPadre(bodegaDTO.getCodigoBodegaPadre());
//					formulario.setDescripcionBodega(bodegaDTO.getDescripcionBodega());			
//					formulario.setEstadoBodega(bodegaDTO.getEstadoBodega());				
//					formulario.setUsuarioCreacion(bodegaDTO.getUsuarioCreacion());
//					formulario.setFechaCreacion(bodegaDTO.getFechaCreacion().toString());
//					session.setAttribute(SessionManagerSISPE.BODEGA_DTO, bodegaDTO);
//					LogSISPE.getLog().info("formulario cargado");			
//				}
//				salida = "actualizarBodega";
//
//			}
//			//el usuario quiere ver las bodegas que est\u00E1n contenidas en una bodega actual
//			else if(request.getParameter("codigoBodega") != null){
//
//				String codigoBodega = request.getParameter("codigoBodega");
//				ArrayList pathBodegas = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.pathBodegas");
//				ArrayList bodegas = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.bodegas");				
//
//				//lista de las bodegas hijas de acuerdo al c\u00F3digo del padre enviado como par\u00E1metro
//				Collection bodegasHija= new ArrayList();
//				BodegaDTO bodegaHijaDTO;	
//				BodegaDTO bodegaDTO = null;
//
//				//c\u00F3digo de la bodega padre anteriormente mostrada
//				String codigoBodegaAnterior = null;			
//
//
//				//buscamos el c\u00F3digo de la bodega anterior al c\u00F3digo de la bodega padre actual
//				for(Iterator it = bodegas.iterator();it.hasNext();){
//					bodegaHijaDTO = (BodegaDTO)it.next();
//					if(bodegaHijaDTO.getId().getCodigoBodega().equals(codigoBodega)){
//						bodegaDTO = bodegaHijaDTO;
//						codigoBodegaAnterior = bodegaHijaDTO.getCodigoBodegaPadre();
//						break;
//					}
//				}	
//
//				//	si ya no hay bodegas anteriores
//				if(codigoBodegaAnterior == null){
//					codigoBodegaAnterior = "-1";					
//				}
//				//nuevo path a agregar
//				String nuevoPath = " <a href=\"bodegas.do?codigoBodega=" +
//				bodegaDTO.getId().getCodigoBodega() + "\" title =\"Ver bodegas subordinadas de la bodega "+
//				bodegaDTO.getId().getCodigoBodega()+"\">" + "(" + 
//				bodegaDTO.getId().getCodigoBodega() +
//				") " + bodegaDTO.getDescripcionBodega() +
//				"/</a>";
//
//				boolean existePath = false;
//
//				//verificamos si el path ya esta contenido en la lista de paths
//				for(int i = 0; i < pathBodegas.size();i++){
//					String path = (String)pathBodegas.get(i);
//					if(path.equals(nuevoPath)){
//						for(int j = i + 1; j < pathBodegas.size() ; j++ ){
//							pathBodegas.remove(j);
//						}
//						existePath = true;
//						break;
//					}
//				}
//				//si no existe el path lo agregamos
//				if(!existePath){
//					pathBodegas.add(nuevoPath);
//				}
//
//				//llenamos las bodegas hijas que existen a partir de una bodega padre
//				for(Iterator it = bodegas.iterator();it.hasNext();){
//					bodegaHijaDTO = (BodegaDTO)it.next();						
//					if(bodegaHijaDTO.getCodigoBodegaPadre()!= null && 
//							bodegaHijaDTO.getCodigoBodegaPadre().equals(codigoBodega)){
//						bodegasHija.add(bodegaHijaDTO);
//					}
//				}	
//
//
//				if(bodegasHija.size() == 0){
//					messages.add("noHayBodegas",
//							new ActionMessage("ec.com.smx.sic.sispe.administracion.bodegas.noExistenBodegas",codigoBodega));
//					saveInfos(request, messages);
//				}
//
//				//variable que me indica que se ha creado o modificado una bodega
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada", new Boolean(false));
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaAnterior",codigoBodegaAnterior);
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaPadre",codigoBodega);
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegasPadre",bodegasHija);
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.pathBodegas",pathBodegas);
//
//				salida="listarBodegas";
//			}
//			//el usuario quiere ver la lista de bodegas de un nivel superior
//			else if(request.getParameter("codigoBodegaAtras") != null){
//
//				ArrayList bodegas = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.bodegas");
//				ArrayList pathBodegas = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.administracion.pathBodegas");
//
//				String codigoBodegaPadre = request.getParameter("codigoBodegaAtras");
//				String codigoBodegaAnterior = null;
//				//lista de las bodegas hijas de acuerdo al c\u00F3digo del padre enviado como par\u00E1metro
//				Collection bodegasHija= new ArrayList();
//				BodegaDTO bodegaHijaDTO;
//
//				if(codigoBodegaPadre.equals("-1")){					
//					// llenamos las bodegas que no tienen codigo de bodega padre
//					for(Iterator it = bodegas.iterator();it.hasNext();){
//						bodegaHijaDTO = (BodegaDTO)it.next();						
//						if(bodegaHijaDTO.getCodigoBodegaPadre()== null ){
//							bodegasHija.add(bodegaHijaDTO);
//						}
//					}
//					codigoBodegaAnterior = codigoBodegaPadre;
//
//					pathBodegas.removeAll(pathBodegas);
//					pathBodegas = new ArrayList();
//					pathBodegas.add("<a href=\"bodegas.do\" title=\"Ver las bodegas principales\">Bodegas:/</a>");
//				}
//				//caso contrario las bodegas si tienen padre
//				else{	
//					//buscamos el c\u00F3digo de la bodega anterior al c\u00F3digo de la bodega padre actual
//					for(Iterator it = bodegas.iterator();it.hasNext();){
//						bodegaHijaDTO = (BodegaDTO)it.next();
//						if(bodegaHijaDTO.getId().getCodigoBodega().equals(codigoBodegaPadre)){
//							codigoBodegaAnterior = bodegaHijaDTO.getCodigoBodegaPadre();
//							break;
//						}
//					}
//
//					//si ya no hay bodegas anteriores
//					if(codigoBodegaAnterior == null){
//						codigoBodegaAnterior = "-1";
//					}
//
//					//llenamos las bodegas hijas que existen a partir de una bodega padre
//					for(Iterator it = bodegas.iterator();it.hasNext();){
//						bodegaHijaDTO = (BodegaDTO)it.next();						
//						if(bodegaHijaDTO.getCodigoBodegaPadre()!= null && 
//								bodegaHijaDTO.getCodigoBodegaPadre().equals(codigoBodegaPadre)){
//							bodegasHija.add(bodegaHijaDTO);
//						}
//					}					
//					pathBodegas.remove(pathBodegas.size() - 1);
//				}
//
//				//	variable que me indica que se ha creado o modificado una bodega
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada", new Boolean(false));
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaAnterior",codigoBodegaAnterior);
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaPadre",codigoBodegaPadre);
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegasPadre",bodegasHija);				
//				session.setAttribute("ec.com.smx.sic.sispe.administracion.pathBodegas",pathBodegas);
//				salida = "listarBodegas";
//
//			}			
//			//por defecto se listar\u00E1n todas las bodegas			
//			else{
//				Boolean creadoModificado = null;
//				//	se obtiene un mensaje de exito de la sesi\u00F3n
//				ControlMensajes controlMensajes = new ControlMensajes();
//				messages = controlMensajes.getMessages(messages,session);
//				LogSISPE.getLog().info("--------------------------------");
//				LogSISPE.getLog().info(""+messages);
//				LogSISPE.getLog().info("--------------------------------");
//				if(!messages.isEmpty()){
//					//se controla el tipo de mensaje que se va a mostrar
//					String tipoMensaje = (String)session.getAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje");
//					LogSISPE.getLog().info("--------------------------------");
//					LogSISPE.getLog().info(""+tipoMensaje);
//					LogSISPE.getLog().info("--------------------------------");
//					if(tipoMensaje!=null && tipoMensaje.equals("error"))
//						saveErrors(request,messages);
//					else if(tipoMensaje!=null && tipoMensaje.equals("info"))
//						saveInfos(request,messages);
//					else
//						saveMessages(request,messages);
//				}
//
//				//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com.smx"
//				if(session.getAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada") == null){
//					SessionManagerSISPE.removeVarSession(request);
//					creadoModificado = new Boolean(false);
//				}
//				else{
//					creadoModificado = (Boolean)session.getAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada");
//				}
//
//				//si se ha hecho una actualizaci\u00F3n o una creaci\u00F3n de una bodega listamos 
//				if(creadoModificado.booleanValue()){
//					Collection bodegas = null;				
//					String codigoBodegaPadre = (String)session.getAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaPadre");
//
//					BodegaDTO consultaBodegaDTO = new BodegaDTO(Boolean.TRUE);
//
//					//establecer el c\u00F3digo de la compania en la bodega
//					consultaBodegaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//
//					bodegas = SessionManagerSISPE.getServicioClienteServicio().transObtenerBodega(consultaBodegaDTO);
//					//lista de las bodegas hijas de acuerdo al c\u00F3digo del padre enviado como par\u00E1metro
//					Collection bodegasHija= new ArrayList();
//					BodegaDTO bodegaHijaDTO;
//
//					//buscamos las bodegas que no tienen padre en caso de que no exista ning\u00FAn c\u00F3digo padre
//					if(codigoBodegaPadre.equals("-1")){
//						for(Iterator it = bodegas.iterator();it.hasNext();){
//							bodegaHijaDTO = (BodegaDTO)it.next();
//							if(bodegaHijaDTO.getCodigoBodegaPadre() == null){
//								bodegasHija.add(bodegaHijaDTO);
//							}
//						} 
//					}
//					//buscamos las bodegas que tienen un padre si existe alg\u00FAn c\u00F3digo padre
//					else{
//						for(Iterator it = bodegas.iterator();it.hasNext();){
//							bodegaHijaDTO = (BodegaDTO)it.next();
//							if(bodegaHijaDTO.getCodigoBodegaPadre() != null && 
//									bodegaHijaDTO.getCodigoBodegaPadre().equals(codigoBodegaPadre)){							
//								bodegasHija.add(bodegaHijaDTO);							
//							}
//						}	
//					}
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegas",bodegas);
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegasPadre",bodegasHija);
//				}
//				else{
//					//colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
//					Collection bodegas= new ArrayList();
//
//					//Colecci\u00F3n solo de las bodegas que no tienen una bodega padre
//					Collection bodegasPadre= new ArrayList();
//					BodegaDTO consultaBodegaDTO = new BodegaDTO(Boolean.TRUE);
//
//					//establecer el c\u00F3digo de la compania en la bodega
//					consultaBodegaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//
//					bodegas = SessionManagerSISPE.getServicioClienteServicio().transObtenerBodega(consultaBodegaDTO);
//					BodegaDTO bodegaDTO;
//
//					for(Iterator it = bodegas.iterator();it.hasNext();){
//						bodegaDTO = (BodegaDTO)it.next();
//						if(bodegaDTO.getCodigoBodegaPadre() == null){
//							bodegasPadre.add(bodegaDTO);
//						}
//					}            	            
//
//					//c\u00F3digo de la bodega padre de la lista mostrada
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaPadre","-1");
//					//lista de todas las bodegas
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegas",bodegas);
//					//lista de las bodegas que se van a mostrar
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegasPadre",bodegasPadre);
//					//c\u00F3digo de la bodega de la lista mostrada anteriormente, inicialmente con valor -1
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoBodegaAnterior","-1");
//
//					Collection pathBodegas = new ArrayList();
//					pathBodegas.add("<a href=\"bodegas.do\" title=\"Ver las bodegas principales\">Bodegas:/</a>");
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.pathBodegas",pathBodegas);
//
//					//variable que me indica que se ha creado o modificado una bodega
//					session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada", creadoModificado);
//				}
//				salida = "listarBodegas";            
//
//			}
//		}
//		catch(Exception ex){
//			//excepcion desconocida
//			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
//			salida="errorGlobal";
//		}
//
//		return mapping.findForward(salida);
//	}
}
