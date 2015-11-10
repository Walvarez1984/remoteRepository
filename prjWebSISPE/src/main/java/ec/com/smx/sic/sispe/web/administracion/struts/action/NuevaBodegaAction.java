/*
 * Creado el 16/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import ec.com.smx.framework.web.action.BaseAction;


/**
 * <p>
 * Clase para la creaci\u00F3n de una nueva bodega
 * </p>
 * @author cbarahona
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
public class NuevaBodegaAction extends BaseAction {
//	/**
//	 * Permite listar todas las bodegas existentes con las siguientes
//	 * opciones:
//	 * <ul>
//	 * <li>Listar bodegas existentes</li>	
//	 * <li>Preparar la p\u00E1gina para la edici\u00F3n de una bodega</li>
//	 * <li>Preparar la p\u00E1gina para creaci\u00F3n de una bodega</li>
//	 * </ul>
//	 * @param mapping			El mapeo utilizado para seleccionar esta instancia
//     * @param form			    El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
//     * @param request			El request que estamos procesando
//     * @param response		    La respuesta HTTP que se crea
//     * @return ActionForward	Describe donde y como se redirige el control
//     * @throws Exception        error generado por struts
//	 */
//	public ActionForward execute(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//	ActionMessages messages = new ActionMessages();
//	HttpSession session = request.getSession();
//	BodegaForm formulario = (BodegaForm) form;
//
//	String salida = "listarBodegas";
//	String ayuda = request.getParameter(Globals.AYUDA);
//	
//
//	try {
//		//el usuario procede a guardar la bodega editada
//		 if (ayuda.equals("guardarBodega")) {
//		    LogSISPE.getLog().info("<<<<<<<<<<<<<<<<<<<<<<<Entro en la Accion NuevoBodega.do <<GuardarBodega>>>>>>>>>>>>>");
//			BodegaDTO bodegaDTO = new BodegaDTO(Boolean.TRUE);
//			bodegaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			//establece los campos para el nuevo registro
//			bodegaDTO.setDescripcionBodega(formulario.getDescripcionBodega());
//			bodegaDTO.setEstadoBodega(formulario.getEstadoBodega());
//			bodegaDTO.setUsuarioCreacion("FRM0");
//			bodegaDTO.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
//			
//			if(formulario.getCodigoBodegaPadre() != null && !formulario.getCodigoBodegaPadre().equals("")){
//				LogSISPE.getLog().info("Entro a crear una Nueva SubBodega de ->{}",formulario.getCodigoBodegaPadre());
//				bodegaDTO.setCodigoBodegaPadre(formulario.getCodigoBodegaPadre());
//			}else{
//				LogSISPE.getLog().info("Entro a crear una Nueva Bodega Padre");	
//			}
//			//llamada al m\u00E9todo del servicio para almacenar el nuevo registro
//			SessionManagerSISPE.getServicioClienteServicio().transRegistrarBodega(bodegaDTO);
//			//se agrega a la colecci\u00F3n la lista de registrso obtenidos
//			LogSISPE.getLog().info("-----------------guardando la nueva bodega----------------");
//			//variable que me indica que se ha creado una nueva bodega
//			session.setAttribute("ec.com.smx.sic.sispe.administracion.bodegaCreadaModificada", new Boolean(true));
//			//se guarda el mensaje de exito
//			ControlMensajes controlMensajes = new ControlMensajes();
//	        controlMensajes.setMessages(session,"message.exito.actualizacion","La Bodega");
//	        session.setAttribute("ec.com.smx.sic.sispe.mantenimiento.mensaje","exito");//mensajes de guardado correctamente	        
//	        
//			salida = "listarBodegas";			
//		}
//	} catch (Exception ex) {
//		LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
//		messages.add("Bodega",
//				new ActionMessage("errors.llamadaServicio.registrarDatos",
//						"la bodega"));
//		saveErrors(request, messages);
//	}
//	return mapping.findForward(salida);
//	}
}
