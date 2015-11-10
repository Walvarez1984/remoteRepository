
package ec.com.smx.sic.sispe.web.reportes.struts.action;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.reportes.struts.form.ReporteGeneralForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author mbraganza
 *
 */
public class ReporteGeneralAction extends BaseAction {
	
	private final String REPORTE_1="ec.com.smx.sic.sispe.entrega.tipoReporte.conPed";
	private final String REPORTE_2="ec.com.smx.sic.sispe.entrega.tipoReporte.pedArt";
	private static final String ACEPTAR_REPORTE = "aceptarReporte";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuilder salida = null;
		ReporteGeneralForm formulario = (ReporteGeneralForm) form;
		ActionMessages errores = null;
		HttpSession sesion = request.getSession();
		String reporteEstado = request.getParameter(Globals.AYUDA);
		//Subo a sesi\u00F3n las variables de los tipos de reporte
		sesion.setAttribute(REPORTE_1,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.ventasConPed"));
		sesion.setAttribute(REPORTE_2,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.ventasPedArt"));
		
		LogSISPE.getLog().info("BotonExportarReporteGeneral: {}", formulario.getBotonExportarReporte());
		if (formulario.getBotonExportarReporte() != null){
			String nombreReporte = null;
			String tipoReporte = null;
			LocalID localID = null;
			LocalDTO localDTO = null;
			Collection vistaReporteGeneralDTOCol = (Collection) sesion.getAttribute("ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado");
			
			if (vistaReporteGeneralDTOCol != null && vistaReporteGeneralDTOCol.isEmpty() == false){
				try{
					tipoReporte = (String) sesion.getAttribute(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.nombreVariableTipo"));
					
					
					if (tipoReporte.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas"))){
						//nombreReporte = "ventas";
							LogSISPE.getLog().info("Entra opcion reporte ventas instanciarPopUp");
							instanciarPopUpOpcionesAgrupacion(request);
							formulario.setOpTipoAgrupacion((String)sesion.getAttribute(REPORTE_1));
							salida = new StringBuilder("desplegar");
							
					} else if (tipoReporte.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.despachos"))){
						nombreReporte = "despachos";
					} 
					if (sesion.getAttribute("ec.com.smx.sic.sispe.codigoLocalResponsable") != null){
						localID = new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal((Integer) sesion.getAttribute("ec.com.smx.sic.sispe.codigoLocalResponsableVentas"));
						localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);
						if (localDTO != null){
							request.setAttribute("ec.com.smx.sic.sispe.nombreLocalResponsable", localDTO.getNombreLocal());	
						}
					}
					
					if (!tipoReporte.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas"))) {
						request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(nombreReporte, formulario.getBotonExportarReporte()));
						salida = new StringBuilder().append(nombreReporte).append(formulario.getBotonExportarReporte());
					}					
				}catch(Exception e){
					errores = new ActionMessages();
					errores.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.general", formulario.getBotonExportarReporte()));					
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}
			} else{
				errores = new ActionMessages();
				errores.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.sinDatos", formulario.getBotonExportarReporte()));
				salida = new StringBuilder("desplegar");
			}
				
		}else if (reporteEstado != null && reporteEstado.equals(ACEPTAR_REPORTE)){
			LogSISPE.getLog().info("Presiono el boton aceptar reporte del popUpConfirmacion");
			//Obtengo datos de plantilla de b\u00FAsqueda
			VistaReporteGeneralDTO plantillaBusquedaOriginal = new VistaReporteGeneralDTO(); 
			plantillaBusquedaOriginal = (VistaReporteGeneralDTO)sesion.getAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA_GENERAL1);
			
			if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.Todos"))){
				LogSISPE.getLog().info("opcion todos");
				//excluye los siguientes estados
				plantillaBusquedaOriginal.setNpNoCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado").concat(",")
						.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.devolucion")));
				//parametro que me indica que debe realizar la relacion con entregas y sin entregas.
				plantillaBusquedaOriginal.setNpReporteVentasTodos("3");
			} else if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.cotizadosReservados"))){
				LogSISPE.getLog().info("opcion cotizadosReservados");
				//filtra solo por estos estados
				plantillaBusquedaOriginal.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"));
				//parametro que me indica que opcion del combo es.
				plantillaBusquedaOriginal.setNpReporteVentasTodos("1");
				
			} else if(formulario.getCmbOpcionEstadoPedido().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.cotizadosSinReserva"))){
				LogSISPE.getLog().info("opcion cotizadosSinReserva");
				//filtra solo por estos estados
				plantillaBusquedaOriginal.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado").concat(",")
						.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado")).concat(",")
						.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
				//no realiza join con las entregas
				plantillaBusquedaOriginal.setNpNoObtenerEntregas("ok");
				plantillaBusquedaOriginal.setNpReporteVentasTodos("2");
			}
			
			//Resumen de campos seteados
			LogSISPE.getLog().info("No obtener entregas->{}",plantillaBusquedaOriginal.getNpNoObtenerEntregas());
			LogSISPE.getLog().info("Codigo de estado->{}",plantillaBusquedaOriginal.getId().getCodigoEstado());
			LogSISPE.getLog().info("Tipo reporte->{}",formulario.getOpTipoAgrupacion());
			LogSISPE.getLog().info("Estados {}",plantillaBusquedaOriginal.getNpNoCodigoEstado());
			LogSISPE.getLog().info("Reporte venta todos {}",plantillaBusquedaOriginal.getNpReporteVentasTodos());
			
			//Seteo el nuevo tipo de agrupaci\u00F3n
			plantillaBusquedaOriginal.setTipoReporte(formulario.getOpTipoAgrupacion());
			//Colecci\u00F3n creada para guardar datos de consulta con nueva agrupaci\u00F3n
			Collection vistaReporteGeneralDTOCol = null;
			
			//Se llama al m\u00E9todo de obtenci\u00F3n de datos
			vistaReporteGeneralDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(plantillaBusquedaOriginal);
			//Se procede a cargar reportes
			if(vistaReporteGeneralDTOCol!=null){
				sesion.removeAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS_EXCEL);
				sesion.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS_EXCEL, vistaReporteGeneralDTOCol);
				//Elimino variables de control de reporte en excel
				sesion.removeAttribute("ec.com.sic.sispe.reporte.reporteVenta1");
				sesion.removeAttribute("ec.com.sic.sispe.reporte.reporteVenta2");
				
				//sesion.setAttribute(ReporteGeneralForm.COL_PEDIDOS_COMPLETOS, vistaReporteGeneralDTOCol);
				request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("ventas", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
				
				//Subo a sesi\u00F3n la variable correspondiente a la opci\u00F3n escogida
				if(formulario.getOpTipoAgrupacion().equals(sesion.getAttribute(REPORTE_1))){
					//Agrupaci\u00F3n: articulo/pedido
					LogSISPE.getLog().info("escogio el reporte contacto/pedido");
					sesion.setAttribute("ec.com.sic.sispe.reporte.reporteVenta1", "ok");
					sesion.removeAttribute(SessionManagerSISPE.POPUP);
					//salida = new StringBuilder("ventasxls");
				}
				else if(formulario.getOpTipoAgrupacion().equals(sesion.getAttribute(REPORTE_2))){
					//Agrupaci\u00F3n: pedido/articulo
					LogSISPE.getLog().info("escogio el reporte pedido/articulo");
					sesion.setAttribute("ec.com.sic.sispe.reporte.reporteVenta2", "ok");
					sesion.removeAttribute(SessionManagerSISPE.POPUP);
				}										
			}
			//if(!formulario.getOpTipoAgrupacion().equals(sesion.getAttribute(REPORTE_1))){
				//Se direcciona a reporteVentasExcelAux.jsp
				salida = new StringBuilder("reporteVentas1XLS");	
			//}
			
		}
		saveErrors(request, errores);
		LogSISPE.getLog().info("salidaReporteGeneral: {}", salida.toString());
	
		return mapping.findForward(salida.toString());
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpOpcionesAgrupacion(HttpServletRequest request)throws Exception{
		LogSISPE.getLog().info("Entra a crear popUp");
		//Se crea la ventana popUp
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Opciones de agrupaci\u00F3n");
		popUp.setMensajeVentana("Escoja una de las siguientes opciones para crear el reporte: ");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setValorOK("realizarEnvioSinProcesando2('"+ACEPTAR_REPORTE+"');hide(['popupConfirmar']);ocultarModal();");
		popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
		popUp.setEtiquetaBotonOK("Aceptar");
		popUp.setEtiquetaBotonCANCEL("Cancelar");
		popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
		popUp.setContenidoVentana("/reportes/ventas/opcionesReporteVentas.jsp");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//Se guarda en session la imagen del pop-up
		request.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
}
