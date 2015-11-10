/*
 * Creado el 09/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
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
import org.apache.struts.action.ActionMessages;

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
 * Esta clase para la administraci\u00F3n de la frecuencia de art\u00EDculos
 * </p>
 * @author 	cbarahona
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class FrecuenciaArticulosAction extends BaseAction {
	/**
	 * Permite listar todas las frecuencias de art\u00EDculos existentes con las siguientes
	 * opciones:
	 * <ul>
	 * <li>Listar frecuencias existentes</li>	
	 * <li>Preparar la p\u00E1gina para la edici\u00F3n</li>
	 * <li>Preparar la p\u00E1gina para creaci\u00F3n</li>
	 * </ul>
	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
     * @param form			    El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
     * @param request			El request que estamos procesando
     * @param response		    La respuesta HTTP que se crea
     * @return ActionForward	Describe donde y como se redirige el control
     * @throws Exception        error generado por struts
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();
		String salida="frecuenciaArticulos";
		String ayuda = request.getParameter(Globals.AYUDA);
		String cancelar = request.getParameter("cancel");
		FrecuenciaArticulosForm formulario = (FrecuenciaArticulosForm)form;
		
		try{
			//el usuario quiere salir de la administraci\u00F3n de frecuencias
			if(cancelar != null && cancelar.equals("cancel")){
				salida = "frecuenciaArticulos";
			}
			
			//el usuario quiere crear una frecuencia
			else if(ayuda!=null && ayuda.equals("nuevaFrecuenciaArticulos")){
				salida = "nuevaFrecuenciaArticulos";
				Collection codigoArticulos = new ArrayList();
				Collection frecuenciaArticulos = new ArrayList();
				//las colecciones de frecuencias de art\u00EDculos son vac\u00EDas
				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos",codigoArticulos);
				session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos",frecuenciaArticulos);
			}		
			//el usuario quiere actualizar una frecuencia
			else if(request.getParameter("indiceRegistro")!=null){			
				
				//obtener el indice de la frecuencia a ser actualizada
				ArrayList frecuencias = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.administracion.frecuencias");
				int fila = Integer.parseInt(request.getParameter("indiceRegistro"));			
				
				FrecuenciaDTO frecuenciaDTO = (FrecuenciaDTO) frecuencias.get(fila);
				
				//subir al formulario los datos de la frecuencia a actualizar	
				formulario.setIndiceFrecuencia(fila);
				formulario.setDescripcionFrecuencia(frecuenciaDTO.getDescripcionFrecuencia());			
				formulario.setEstadoFrecuencia(frecuenciaDTO.getEstadoFrecuencia());
				
				LogSISPE.getLog().info("formulario cargado");
				//preparar las frecuencias de los art\u00EDculos para cargar los art\u00EDculos 
				FrecuenciaArticuloDTO frecuenciaArticuloDTO = new FrecuenciaArticuloDTO(Boolean.TRUE);
				frecuenciaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				frecuenciaArticuloDTO.getId().setCodigoFrecuencia(frecuenciaDTO.getId().getCodigoFrecuencia());
				frecuenciaArticuloDTO.setArticuloDTO(new ArticuloDTO());
				LogSISPE.getLog().info("frecuencia art\u00EDculo cargado");				
				
				//cargar los art\u00EDculos
				Collection frecuenciaArticulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerFrecuenciaArticulo(frecuenciaArticuloDTO);				
				frecuenciaDTO.setFrecuenciasArticulos(frecuenciaArticulos);			
				
				//llenar las claves de los articulos
				Collection codigoArticulos = new ArrayList();
				Iterator it=frecuenciaArticulos.iterator();
				while(it.hasNext()){
					FrecuenciaArticuloDTO farticuloDTO = (FrecuenciaArticuloDTO)it.next();
					codigoArticulos.add(farticuloDTO.getArticuloDTO().getId().getCodigoArticulo());
					LogSISPE.getLog().info("Tiene bitacoraArticulos: " + farticuloDTO.getArticuloDTO().getTieneArtBitCodBar().toString());
				}			
				
				//las claves de los articulos y los articulos en sesion
				session.setAttribute("ec.com.smx.sic.sispe.administracion.codigoArticulos",codigoArticulos);
				session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuenciaArticulos",frecuenciaArticulos);				
				
				salida = "actualizarFrecuenciaArticulos";
				
			}		
			else{
				
				//	se obtiene un mensaje de exito de la sesi\u00F3n
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
	            //se eliminan las variables de sesi\u00F3n que comienzen con "ec.com.smx"
	            SessionManagerSISPE.removeVarSession(request);
	            LogSISPE.getLog().info("primera vez");
				
	            //colecci\u00F3n que almacenar\u00E1 la lista obtenida del servicio.
	            Collection frecuencias= new ArrayList();
	            FrecuenciaDTO consultaFrecuenciaDTO = new FrecuenciaDTO(Boolean.TRUE);
	            consultaFrecuenciaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	            LogSISPE.getLog().info("set compania listo");
	            //falta llamada al m\u00E9todo de la capa de servicio
	            frecuencias = SessionManagerSISPE.getServicioClienteServicio().transObtenerFrecuencia(consultaFrecuenciaDTO);
	            LogSISPE.getLog().info("coleccion de frecuencias cargadas");
	            
	            session.setAttribute("ec.com.smx.sic.sispe.administracion.frecuencias",frecuencias);
	            
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
