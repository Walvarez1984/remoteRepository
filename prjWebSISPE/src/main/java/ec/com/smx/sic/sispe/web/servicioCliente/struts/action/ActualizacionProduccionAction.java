/*
 * Clase ActualizacionProduccionAction.java
 * Creado el 10/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_CANASTOS_CATALOGO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ControlProduccionForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;
/**
 * <p>
 * Esta clase permite actualizar las cantidades de los art\u00EDculos que se van a producir, maneja dos tags 
 * que muestran distintas secciones del formulario.
 * <ol>
 * <li>ARTICULOS: En esta secci\u00F3n se procesan las cantidades ingresadas que actualizar\u00E1n la producci\u00F3n.</li>
 * <li>PEDIDOS:   En est\u00E1 secci\u00F3n se escogen los pedidos que ser\u00E1n afectados por la acualizaci\u00F3n. Si no se
 * 		esoge alguno, se actualizar\u00E1n los pedidos que tengan la fecha de entrega m\u00E1s pr\u00F3xima.</li>
 * </ol>
 * </p>
 * @author 	fmunoz
 * @version	3.0
 * @since 	JSDK 1.5
 */
@SuppressWarnings("unchecked")
public class ActualizacionProduccionAction extends BaseAction
{
	//nombre para las variables de respaldo que contienen los datos de ordenamiento para pedidos y art\u00EDculos
	private final String DATOS_TABLA_ORDENAR_PEDIDOS = "ec.com.smx.sic.sispe.ordenamiento.datosTablaPedidos";
	private final String DATOS_COLUMNA_ORDENADA_PEDIDOS = "ec.com.smx.sic.sispe.ordenamiento.datosColumnaPedido";

	private final String DATOS_TABLA_ORDENAR_ARTICULOS = "ec.com.smx.sic.sispe.ordenamiento.datosTablaArticulos";
	private final String DATOS_COLUMNA_ORDENADA_ARTICULOS = "ec.com.smx.sic.sispe.ordenamiento.datosColumnaArticulo";
	//constante para nombrar la colecci\u00F3n de art\u00EDculos
	public static final String COLECCION_ARTICULOS = "ec.com.smx.sic.sispe.articulos";
	//constante para nombrar la variable de inicio de paginaci\u00F3n para los pedidos
	private final String INICIO_PAGINACION_PEDIDOS = "ec.com.smx.sic.sispe.paginacionArticulos.inicio";

	private final String VECTOR_PEDIDOS_SELECCIONADOS = "ec.com.smx.sic.sispe.vectorPedidosSeleccionados";
	private final String VECTOR_ANTERIOR_PEDIDOS_SELECCIONADOS = "ec.com.smx.sic.sispe.vectorAnteriorPedidosSeleccionados";

	private final String VECTOR_CANTIDADES_PRODUCIR = "ec.com.smx.sic.sispe.cantidadesAProducir";
	//private final String CLASIFICACIONES_PERECIBLES = "ec.com.smx.sic.sispe.codigosClasificacionesPerecibles";

	private final String ARTICULO_SELECCIONADO="ec.com.smx.sic.sise.articuloSel";//indice que indica que recetas de que articulo esta siendo ordenado
	//variable que guarda el objeto con los criterios de b\u00FAsqueda para generar reportes
	private final String VISTA_ARTICULO_REPORTE="ec.com.smx.sic.sispe.vistaArticuloReporte";
	private static final String ACEPTAR_REPORTE = "aceptarReporte";
	private final String COLECCION_ARTICULOS_REPORTE = "ec.com.smx.sic.sispe.articulosReporte";
	//variables con las opciones de tipo de reporte
	private final String REPORTE_1="ec.com.smx.sic.sispe.articulo.rptFechaArtLocal";
	private final String REPORTE_2="ec.com.smx.sic.sispe.articulo.rptArtFechaLocal"; 
	private final String REPORTE_3="ec.com.smx.sic.sispe.articulo.rptLocalFechaArt";
	private final String REPORTE_4="ec.com.smx.sic.sispe.articulo.rptLocalArtFecha";

	private final String CONSULTA_DESPACHO_TOTAL = "ec.com.smx.sic.sispe.consulta.despacho.totales";
	//variable que controla la generaci\u00F3n de reportes dependiendo del tipo de b\u00FAsqueda
	
	//OANDINO: Variables de control para intercambio de art\u00EDculos
	//Variable que contiene el DTO tipo VistaArticuloDTO para mostrar en pop-up de intercambio
	private final String VAR_VISTA_ARTICULO_DTO_INTERCAMBIO = "ec.com.smx.sic.sispe.vistaArticuloDTO.Intercambio";
	//Variabel que identifica el tipo de proceso para agregar art\u00EDculos de intercambio
	private static final String ACEPTAR_INTERCAMBIO = "aceptarIntercambio";
	//Variable que identifica si el tipo de objeto tiene un grupo de recetas o no, adem\u00E1s permite mantener desplegado el detalle de un registro
	private static final String VAR_TIPO_OBJETO_INTERCAMBIO = "ec.com.smx.sic.sispe.tipo.objeto.Intercambio";
	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{

		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		//se obtiene el valor de la petici\u00F3n
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);

		ControlProduccionForm formulario = (ControlProduccionForm)form;
		HttpSession session = request.getSession();
		//se obtiene el bean que contiene los campos genericos en sesi\u00F3n
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		String salida = "actualizacion";

		//se obtiene la variable de sesi\u00F3n que contiene el modo de actualizaci\u00F3n de despacho
		String modoActualizarDespacho = (String)session.getAttribute(ActualizacionDespachoAction.MODO_ACTUALIZAR_DESPACHO);
		if(modoActualizarDespacho != null){
			salida = "actualizacionDespacho";
		}

		//se obtiene el c\u00F3digo de inicializaci\u00F3n para los ids de los DTOs
		String codigoInicialIds = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID");

		try
		{
			if(request.getParameter("indiceOrdenarA1")==null){
				//se elimina el indice del articulo que fue ordenado
				session.removeAttribute(ARTICULO_SELECCIONADO);
			}

			/*--------------- cuando se da clic en los campos de paginaci\u00F3n --------------------*/
			if(request.getParameter("range")!=null || request.getParameter("start")!=null)
			{
				LogSISPE.getLog().info("ENTRO A LA PAGINACION");
				Collection datos = null;
				if(beanSession.getPaginaTab()!=null && beanSession.getPaginaTab().esTabSeleccionado(1)){
					datos = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					LogSISPE.getLog().info("PEDIDOS");
					//se actualizan los checks de los pedidos
					formulario.setOpSeleccionPedidos((String [])session.getAttribute(VECTOR_PEDIDOS_SELECCIONADOS));
					session.setAttribute(INICIO_PAGINACION_PEDIDOS, request.getParameter("start"));
				}
				else if(beanSession.getPaginaTab()!=null && beanSession.getPaginaTab().esTabSeleccionado(0)){
					datos = (Collection)session.getAttribute(COLECCION_ARTICULOS);
					LogSISPE.getLog().info("ARTICULOS");
					beanSession.setInicioPaginacion(request.getParameter("start"));
				}

				if(datos!=null){
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(datos, Integer.parseInt(request.getParameter("start")),datos.size(),false);
					formulario.setDatos(datosPagina);
				}
			}
			
			//OANDINO: ------------------------------------------------------------------------------------------------------------------------
			//Se muestra pop-up de intercambio de art\u00EDculos
			else if(request.getParameter("mostrarPopUpIntercambio")!=null){
				LogSISPE.getLog().info("Se procede a cargar Pop-Up de intercambio de art\u00EDculos...");
				
				//Se resetean los campos de texto de ingreso de c\u00F3digo de art\u00EDculo
				formulario.setCodigoArtIntercambio(null);
				
				//Se obtiene el \u00EDndice del registro elegido
				LogSISPE.getLog().info("Registro art\u00EDculo: {}",request.getParameter("mostrarPopUpIntercambio"));		
								
				String[] indiceRegistro = request.getParameter("mostrarPopUpIntercambio").split("-");
				LogSISPE.getLog().info("Tama\u00F1o arreglo de n\u00FAmero de registro: {}",indiceRegistro.length);
								
				if(indiceRegistro.length == Integer.valueOf(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"))){
					//El tipo de registro al que se ha accedido es del tipo ArticuloDTO
					LogSISPE.getLog().info("Indice \u00FAnico...");
																	
					//Se obtiene el DTO respectivo al \u00EDndice obtenido anteriormente de la colecci\u00F3n total de resultados
					ArrayList<VistaArticuloDTO> articulosColeccion = (ArrayList<VistaArticuloDTO>)session.getAttribute(COLECCION_ARTICULOS);
					VistaArticuloDTO articuloColDTO = articulosColeccion.get(Integer.valueOf(indiceRegistro[0]));							
													
					//Cargo en session el DTO consultado
					if(articuloColDTO!=null){
						LogSISPE.getLog().info("C\u00F3digo barras: {}",articuloColDTO.getId().getCodigoArticulo());
						LogSISPE.getLog().info("Descripci\u00F3n: {}",articuloColDTO.getDescripcionArticulo());					
						session.setAttribute(VAR_VISTA_ARTICULO_DTO_INTERCAMBIO, articuloColDTO);
					}
					
					//Se anula valor de variable de control de tipo de \u00EDndice y despliegue de detalles
					session.removeAttribute(VAR_TIPO_OBJETO_INTERCAMBIO);											
				}
				else{								
					//El tipo de registro al que se ha accedido es del tipo RecetaArticuloDTO
					LogSISPE.getLog().info("Indice compuesto...");
									
					//Se obtiene el DTO respectivo al \u00EDndice obtenido anteriormente de la colecci\u00F3n total de resultados
					ArrayList<VistaArticuloDTO> articulosColeccion = (ArrayList<VistaArticuloDTO>)session.getAttribute(COLECCION_ARTICULOS);
					VistaArticuloDTO articuloColDTO = articulosColeccion.get(Integer.valueOf(indiceRegistro[0]));	
					
					ArrayList<ArticuloRelacionDTO> colRecetas = (ArrayList<ArticuloRelacionDTO>)articuloColDTO.getRecetaArticulos();
					ArticuloRelacionDTO recetaDTO = colRecetas.get(Integer.valueOf(indiceRegistro[1]));
					
					LogSISPE.getLog().info("C\u00F3digo de RecetaDTO: {}",recetaDTO.getId().getCodigoArticulo());																	
					session.setAttribute(VAR_VISTA_ARTICULO_DTO_INTERCAMBIO, recetaDTO);		
					
					//Se setea valor del registro padre en varible para control de tipo de \u00EDndice y para mantener desplegado el detalle
					session.setAttribute(VAR_TIPO_OBJETO_INTERCAMBIO, indiceRegistro[0]);
				}
				
				LogSISPE.getLog().info("Entra a crear popUp");				
				//Se crea la ventana popUp en base a sus propiedades
				UtilPopUp popUp = new UtilPopUp();
				popUp.setTituloVentana("Intercambio de art\u00EDculos");
				popUp.setMensajeVentana("Ingrese la informaci&oacute;n de los art&iacute;culos a intercambiar: ");
				popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
				popUp.setValorOK("requestAjax('actualizarProduccion.do',['popUpIntercambio','div_listado'],{parameters: '" + ACEPTAR_INTERCAMBIO + "=ok'});ocultarModal();");
				popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();requestAjax('actualizarProduccion.do',['mensajes'],{parameters: 'cancelarPopUpIntercambio=ok'});");
				popUp.setEtiquetaBotonOK("Aceptar");
				popUp.setEtiquetaBotonCANCEL("Cancelar");	
				popUp.setValorCANCEL(popUp.getAccionEnvioCerrar());
				popUp.setContenidoVentana("/servicioCliente/actualizarProduccion/intercambioArticulos.jsp");
				popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);	
				
				//Se guarda en session la imagen del pop-up
				session.setAttribute(SessionManagerSISPE.POPUP, popUp);		
				
			}
			
			//Se procede a validar c\u00F3digo para intercambio para cargar nuevo art\u00EDculo en colecci\u00F3n
			else if(request.getParameter(ACEPTAR_INTERCAMBIO) != null){
				LogSISPE.getLog().info("Se procede a verificar validez de c\u00F3digo de art\u00EDculo a intercambiar...");
				
				//Bandera de verificaci\u00F3n de registros duplicados
				boolean band = true;
												
				LogSISPE.getLog().info("C\u00F3digo ingresado: {}",formulario.getCodigoArtIntercambio().trim());
				
				//Se crea plantilla tipo ArticuloDTO para verificar la existencia del c\u00F3digo de art\u00EDculo ingresado
				ArticuloDTO artColDTO = new ArticuloDTO();				
				artColDTO.getId().setCodigoCompania(1);
				artColDTO.setNpCodigoBarras(formulario.getCodigoArtIntercambio().trim());
//				Collection<ArticuloDTO> colArticulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulo(artColDTO); 	
				ArticuloDTO articuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(artColDTO);
								
				//Si la b\u00FAsqueda devuelve resultados, entonces se carga el registro obtenido al listado en pantalla
				if(articuloDTO!=null){
					LogSISPE.getLog().info("El c\u00F3digo ingresado si corresponde a un art\u00EDculo existente");
									
					//for(Iterator it=colArticulos.iterator();it.hasNext();) {
						//Se obtiene el art\u00EDculo de la b\u00FAsqueda
						//ArticuloDTO articuloDTO = (ArticuloDTO)it.next();
						
						//Colecci\u00F3n que contendr\u00E1 la colecci\u00F3n de art\u00EDculos dependiendo del tipo de DTO 
						Collection<ArticuloDTO> articulosDTOCol = new ArrayList<ArticuloDTO>();
						
						//Se obtiene de session el registro elegido en primer lugar a trav\u00E9s del v\u00EDnculo para ventana emergente
						if(session.getAttribute(VAR_TIPO_OBJETO_INTERCAMBIO) == null){
							VistaArticuloDTO vistaArticulo = (VistaArticuloDTO)session.getAttribute(VAR_VISTA_ARTICULO_DTO_INTERCAMBIO);
							//Obtengo el listado de relaciones
							articulosDTOCol = vistaArticulo.getArticulos();
																					
							//Se a\u00F1ade nuevo registro verificando si no existe previamente
							if(articulosDTOCol!=null){
								//Se barre el listado del detalle de art\u00EDculos para comparar c\u00F3digos de art\u00EDculo
								for(ArticuloDTO dto:articulosDTOCol){
									if(dto.getId().getCodigoArticulo().equals(articuloDTO.getId().getCodigoArticulo()))									
										band = false;										
								}							
								if(band){
									//Se a\u00F1ade nuevo art\u00EDculo
									articulosDTOCol.add(articuloDTO);		
									//Se elimina variable de contenidos de pop-up
									session.removeAttribute(SessionManagerSISPE.POPUP);
								}
								else{
									//Si la b\u00FAsqueda no devuelve resultados se muestra mensaje de error en propiedades de pop-up
									UtilPopUp popUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
									popUp.setMensajeErrorVentana("El c\u00F3digo corresponde a un art\u00EDculo existente");
								}
							}
							else{
								//Se crea nueva colecci\u00F3n para a\u00F1adir art\u00EDculo
								Collection<ArticuloDTO> coleccionNueva = new ArrayList<ArticuloDTO>();
								coleccionNueva.add(articuloDTO);
								vistaArticulo.setArticulos(coleccionNueva);
								
								//Se elimina variable de contenidos de pop-up
								session.removeAttribute(SessionManagerSISPE.POPUP);
							}
							
							Collection<ArticuloDTO> fg = vistaArticulo.getArticulos();
							for(Iterator ite=fg.iterator(); ite.hasNext();)
							{
								ArticuloDTO art = (ArticuloDTO)ite.next();
								LogSISPE.getLog().info("Codigo de registro tipo ArticuloDTO: {}",art.getId().getCodigoArticulo());	
							}
						}
						else{						
							ArticuloRelacionDTO recetaArticulo = (ArticuloRelacionDTO)session.getAttribute(VAR_VISTA_ARTICULO_DTO_INTERCAMBIO);
							//Obtengo el listado de relaciones
							articulosDTOCol = recetaArticulo.getNpArticulos();
						
							//Se a\u00F1ade nuevo registro verificando si no existe previamente
							if(articulosDTOCol!=null){
								//Se barre el listado del detalle de art\u00EDculos para comparar c\u00F3digos de art\u00EDculo
								for(ArticuloDTO dto:articulosDTOCol){
									if(dto.getId().getCodigoArticulo().equals(articuloDTO.getId().getCodigoArticulo()))									
										band = false;										
								}							
								if(band){
									//Se a\u00F1ade nuevo art\u00EDculo
									articulosDTOCol.add(articuloDTO);		
									//Se elimina variable de contenidos de pop-up
									session.removeAttribute(SessionManagerSISPE.POPUP);
								}
								else{
									//Si la b\u00FAsqueda no devuelve resultados se muestra mensaje de error en propiedades de pop-up
									UtilPopUp popUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
									popUp.setMensajeErrorVentana("El c\u00F3digo corresponde a un art\u00EDculo existente");
								}
							}
							else{
								//Se crea nueva colecci\u00F3n para a\u00F1adir art\u00EDculo
								Collection<ArticuloDTO> coleccionNueva = new ArrayList<ArticuloDTO>();
								coleccionNueva.add(articuloDTO);
								recetaArticulo.setNpArticulos(coleccionNueva);
								
								//Se elimina variable de contenidos de pop-up
								session.removeAttribute(SessionManagerSISPE.POPUP);
							}
							
							Collection<ArticuloDTO> fg = recetaArticulo.getNpArticulos();
							for(Iterator ite=fg.iterator(); ite.hasNext();)
							{
								ArticuloDTO art = (ArticuloDTO)ite.next();
								LogSISPE.getLog().info("Codigo de registro tipo RecetaArticuloDTO: {}",art.getId().getCodigoArticulo());	
							}
						}
												
				//	}
																							
				}
				else{
					LogSISPE.getLog().info("El c\u00F3digo ingresado no corresponde a un art\u00EDculo existente");
					
					//Si la b\u00FAsqueda no devuelve resultados se muestra mensaje de error en propiedades de pop-up
					UtilPopUp popUp = (UtilPopUp)session.getAttribute(SessionManagerSISPE.POPUP);
					popUp.setMensajeErrorVentana("El c\u00F3digo ingresado no corresponde a un art\u00EDculo");															
				}
			
			}
			//Se procede a cancelar el proceso de intercambio de art\u00EDculos en producci\u00F3n
			else if(request.getParameter("cancelarPopUpIntercambio")!=null){
				LogSISPE.getLog().info("Cancelando pop-up de confirmaci\u00F3n...");
				
				//Se procede a remover la variable de session que mantiene el despliegue del pop-up de intercambio
				session.removeAttribute(SessionManagerSISPE.POPUP);
				//Se procede a remover la variable de session que mantiene desplegado el detalle de un registro
				session.removeAttribute(VAR_TIPO_OBJETO_INTERCAMBIO);
			}
						
			//---------------------------------------------------------------------------------------------------------------------------------
			
			/*---------------------------- cuando se presiona el bot\u00F3n buscar ---------------------------------*/
			else if(request.getParameter("buscar")!=null)
			{				
				
				//se obtiene la lista de art\u00EDculos de sesi\u00F3n
				Collection <VistaArticuloDTO> articulosEnProduccion = new ArrayList<VistaArticuloDTO>();
				Collection <VistaArticuloDTO> articulosEnProduccionAux = new ArrayList<VistaArticuloDTO>();
				//se llama a la funci\u00F3n que construye la consulta
				VistaArticuloDTO consultaVistaArticuloDTO = WebSISPEUtil.construirConsultaVistaArticulos(request, formulario);

				String estadoAfirmacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion");
				String estadoNegacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion");

				String codigoEstado = "";
				//se verifica el modo de actualizaci\u00F3n
				if(modoActualizarDespacho != null){
					codigoEstado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
						.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
						.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
						.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
						.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"));
					consultaVistaArticuloDTO.getId().setCodigoEstado(codigoEstado);

					if(formulario.getOpDespachoPendiente() !=null){
						//solo se obtienen los art\u00EDculos incompletos
						consultaVistaArticuloDTO.setArticuloCompletado(estadoNegacion);
						consultaVistaArticuloDTO.setObtenerStockArticulos(estadoAfirmacion);
					}else{
						LogSISPE.getLog().info("consulta de las cantidades totales");
						//se consultan los dem\u00E1s estados actuales para obtener los totales
						codigoEstado = codigoEstado.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"));
						consultaVistaArticuloDTO.getId().setCodigoEstado(codigoEstado);
						consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articulo"));
					}
				}else{
//					codigoEstado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");
					
					codigoEstado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
							.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"));
					//estas condiciones se ejecutan cuando es el proceso normal de producci\u00F3n
					consultaVistaArticuloDTO.getId().setCodigoEstado(codigoEstado);
					consultaVistaArticuloDTO.setArticulos(new ArrayList());
					consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articulo"));
					consultaVistaArticuloDTO.setArticuloCompletado(estadoNegacion);
				}

				consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
				consultaVistaArticuloDTO.setArticuloPermitido(estadoAfirmacion);

				//se guardan los campos de b\u00FAsqueda en la sesi\u00F3n
				beanSession.setCodigoLocal(consultaVistaArticuloDTO.getId().getCodigoAreaTrabajo());
				beanSession.setCodigoPedido(consultaVistaArticuloDTO.getId().getCodigoPedido());
				beanSession.setCodigoEstado(codigoEstado);
				beanSession.setCodigoReservacion(consultaVistaArticuloDTO.getCodigoReservacion());
				beanSession.setCedulaContacto(consultaVistaArticuloDTO.getCedulaContacto());
				beanSession.setNombreContacto(consultaVistaArticuloDTO.getNombreContacto());
				beanSession.setNombreEmpresa(consultaVistaArticuloDTO.getNombreEmpresa());
				beanSession.setRucEmpresa(consultaVistaArticuloDTO.getRucEmpresa());
				beanSession.setCodigoArticulo(consultaVistaArticuloDTO.getId().getCodigoArticulo());
				beanSession.setDescripcionArticulo(consultaVistaArticuloDTO.getDescripcionArticulo());
				beanSession.setCodigoClasificacion(consultaVistaArticuloDTO.getCodigoClasificacion());
				beanSession.setOpEntidadResonsable(consultaVistaArticuloDTO.getEntidadResponsable());
				

				//se almacenan en sesi\u00F3n la fechas de inicio y fin de entrega
				beanSession.setFechaInicial(consultaVistaArticuloDTO.getNpPrimeraFechaDespachoInicialTimestamp());
				beanSession.setFechaFinal(consultaVistaArticuloDTO.getNpPrimeraFechaDespachoFinalTimestamp());

				LogSISPE.getLog().info("ruc: " + consultaVistaArticuloDTO.getRucEmpresa()+ 
						",codArticulo: " + consultaVistaArticuloDTO.getId().getCodigoArticulo() +
						",codBarras: " + consultaVistaArticuloDTO.getCodigoBarras() +
						",desArticulo: " + consultaVistaArticuloDTO.getDescripcionArticulo()+
						",codClasificacion: " + consultaVistaArticuloDTO.getCodigoClasificacion());

				//se blanquea la colecci\u00F3n de datos antes de actualizar la lista
				formulario.setDatos(null);
				try{
					//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
					articulosEnProduccion = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosParaActualizarProduccion(consultaVistaArticuloDTO);
					
					//IOnofre. Los articulos de la clasificacion 1606 solo deben mostrarse en la actualizacion de produccion.					
					if(modoActualizarDespacho == null){					
						for(VistaArticuloDTO vistaArtDTO : articulosEnProduccion){
							//
							if(vistaArtDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasElaboradas"))){
								articulosEnProduccionAux.add(vistaArtDTO); 
							}
						}
					}else{
						articulosEnProduccionAux.addAll(articulosEnProduccion);
					}

					//session.setAttribute(COLECCION_ARTICULOS,articulosEnProduccion);
					session.setAttribute(COLECCION_ARTICULOS,articulosEnProduccionAux);
					//se realiza la paginaci\u00F3n si la colecci\u00F3n tiene elementos
					if(articulosEnProduccionAux!=null && !articulosEnProduccionAux.isEmpty()){
						//llamada al m\u00E9todo que realiza la paginaci\u00F3n
						Collection datosPagina = formulario.paginarDatos(articulosEnProduccionAux, 0,articulosEnProduccionAux.size(),false);
						formulario.setDatos(datosPagina);
						beanSession.setInicioPaginacion("0");

						//se inicializan los datos del ordenamiento para los art\u00EDculos
						this.inicializarDatosOrdenamientoArticulos(request, modoActualizarDespacho);

						//se completa la cantidad que se despachar
						if(modoActualizarDespacho != null){
							Long cantidadDespachar = 0L;
							for(Iterator<VistaArticuloDTO> it = articulosEnProduccionAux.iterator(); it.hasNext();){
								VistaArticuloDTO vistaArticuloDTO = it.next();
								if(vistaArticuloDTO.getNpStockArticulo().longValue() < vistaArticuloDTO.getDiferenciaCantidadEstado().longValue()){
									cantidadDespachar = vistaArticuloDTO.getNpStockArticulo();
								}else{
									cantidadDespachar = vistaArticuloDTO.getDiferenciaCantidadEstado();
								}
								vistaArticuloDTO.setNpCantidadParcialEstado(cantidadDespachar);
							}
						}
						//subo a sesi\u00F3n el objeto con los criterios de consulta
						session.setAttribute(VISTA_ARTICULO_REPORTE, consultaVistaArticuloDTO);
					}else{
						//se verifica el modo de actualizaci\u00F3n
						if(modoActualizarDespacho!=null){
							infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos pendientes de Despacho"));
						}else{
							infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos en Producci\u00F3n"));
						}
						session.removeAttribute(VISTA_ARTICULO_REPORTE);
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
					errors.add("errorSISPE",new ActionMessage("errors.SISPEException", ex.getMessage()));
				}

				//eliminar la colecci\u00F3n de los pedidos despu\u00E9s de cada b\u00FAsqueda
				session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				session.removeAttribute(VECTOR_PEDIDOS_SELECCIONADOS);
				session.removeAttribute(VECTOR_ANTERIOR_PEDIDOS_SELECCIONADOS);

				if(modoActualizarDespacho != null){
					//si la b\u00FAsqueda se realiz\u00F3 por despacho pendiente
					if(formulario.getOpDespachoPendiente()!=null){
						session.removeAttribute(CONSULTA_DESPACHO_TOTAL);
					}else{
						session.setAttribute(CONSULTA_DESPACHO_TOTAL, "ok");
					}
				}
			}
			//------------------ cuando se desea ver el detalle de un pedido ---------------
			else if(peticion!=null && peticion.startsWith(SessionManagerSISPE.PREFIJO_IPVA)){
				//se sube a sesi\u00F3n las cantidades ingresadas porque el m\u00E9todo siguiente la utiliza
				session.setAttribute(VECTOR_CANTIDADES_PRODUCIR, formulario.getCantidadProducida());
				//m\u00E9todo que reasigna las cantidades ingresadas
				this.reasignarCantidadesIngresadas(formulario, session);
				
				if(session.getAttribute("ec.com.smx.sic.controlesusuario.tab")!= null){
					session.setAttribute(DetalleEstadoPedidoAction.RESPALDO_TABS, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
					session.setAttribute(DetalleEstadoPedidoAction.RESPALDO_TABRESPALDO, session.getAttribute("ec.com.smx.sic.controlesusuario.tab"));
				}

				session.setAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO, peticion);
				//se elimina la variable de sesi\u00F3n de cantidades
				session.removeAttribute(VECTOR_CANTIDADES_PRODUCIR);
				salida = "detallePedido";
			}
			//------------------- ordenar la colecci\u00F3n de pedidos ---------------------
			else if(request.getParameter("indiceOrdenarP")!=null){

				LogSISPE.getLog().info("Ordenar la tabla de pedidos");
				int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenarP"));

				Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				if(colVistaPedidoDTO != null){
					//se llama a la funci\u00F3n que realiza el ordenamiento
					colVistaPedidoDTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, colVistaPedidoDTO,null);
	
					//se actualiza la colecci\u00F3n en sesi\u00F3n
					session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
					
					//se incializa el indice de la paginaci\u00F3n actual para los pedidos
					int inicioPaginacion = 0;
					if(session.getAttribute(INICIO_PAGINACION_PEDIDOS)!=null)
						inicioPaginacion = Integer.parseInt((String)session.getAttribute(INICIO_PAGINACION_PEDIDOS));
	
					LogSISPE.getLog().info("indice paginacion: {}",inicioPaginacion);
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(colVistaPedidoDTO, inicioPaginacion, colVistaPedidoDTO.size(),false);
					formulario.setDatos(datosPagina);
	
					//se realiza el respaldo de los datos de ordenamiento
					session.setAttribute(DATOS_TABLA_ORDENAR_PEDIDOS, session.getAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR));
					session.setAttribute(DATOS_COLUMNA_ORDENADA_PEDIDOS, session.getAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA));
				}

			}
			//------------------- ordenar la colecci\u00F3n de art\u00EDculos ---------------------
			else if(request.getParameter("indiceOrdenarA")!=null){

				LogSISPE.getLog().info("Ordenar la tabla de art\u00EDculos");
				int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenarA"));

				Collection colVistaArticuloTO = (Collection)session.getAttribute(COLECCION_ARTICULOS);
				if(colVistaArticuloTO != null){
					//se llama a la funci\u00F3n que realiza el ordenamiento
					colVistaArticuloTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, colVistaArticuloTO,null);
				
					//se actualiza la colecci\u00F3n en sesi\u00F3n
					session.setAttribute(COLECCION_ARTICULOS, colVistaArticuloTO);
	
					//se incializa el indice de la paginaci\u00F3n actual para los art\u00EDculos
					int inicioPaginacion = 0;
					if(beanSession.getInicioPaginacion()!=null)
						inicioPaginacion = Integer.parseInt(beanSession.getInicioPaginacion());
	
					LogSISPE.getLog().info("indice paginacion: {}"+inicioPaginacion);
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(colVistaArticuloTO, inicioPaginacion,colVistaArticuloTO.size(),false);
					formulario.setDatos(datosPagina);
	
					//se realiza el respaldo de los datos de ordenamiento
					session.setAttribute(DATOS_TABLA_ORDENAR_ARTICULOS, 
							session.getAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR));
					session.setAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS, 
							session.getAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA));
				}
			}
			//------------------- ordenar la colecci\u00F3n de art\u00EDculos relacionados de un canasto ---------------------
			else if(request.getParameter("indiceOrdenarA1")!=null){
				LogSISPE.getLog().info("Ordenar la tabla de art\u00EDculos de los canastos");
				int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenarA1"));
				ArrayList<VistaArticuloDTO> vistaArticuloDTOCol=(ArrayList<VistaArticuloDTO>)formulario.getDatos();
				VistaArticuloDTO vistaArticuloDTO = vistaArticuloDTOCol.get(Integer.parseInt(request.getParameter("articuloF")));
				LogSISPE.getLog().info("articulo seleccionado: {}", vistaArticuloDTO.getDescripcionArticulo());
				LogSISPE.getLog().info("items: {}", vistaArticuloDTO.getRecetaArticulos().size());
				LogSISPE.getLog().info("sin orden");
				for (Iterator iter = vistaArticuloDTO.getRecetaArticulos().iterator(); iter.hasNext();) {
					ArticuloRelacionDTO element = (ArticuloRelacionDTO) iter.next();
					LogSISPE.getLog().info("hijos: {}" , element.getArticuloRelacion().getDescripcionArticulo());
				}
				//se llama a la funci\u00F3n que realiza el ordenamiento
				Collection<ArticuloRelacionDTO> colVistaArticuloTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, vistaArticuloDTO.getRecetaArticulos(),"2");
				LogSISPE.getLog().info("con orden");
				for (Iterator iter = colVistaArticuloTO.iterator(); iter.hasNext();) {
					ArticuloRelacionDTO element = (ArticuloRelacionDTO) iter.next();
					LogSISPE.getLog().info("hijos: {}", element.getArticuloRelacion().getDescripcionArticulo());
				}
				LogSISPE.getLog().info("numero de detalles: {}", colVistaArticuloTO.size());
				vistaArticuloDTO.setRecetaArticulos(colVistaArticuloTO);
				session.setAttribute(ARTICULO_SELECCIONADO, new Integer(request.getParameter("articuloF")).intValue());
				LogSISPE.getLog().info("indice del articulo: {}" , request.getParameter("articuloF"));

				//se actualiza la colecci\u00F3n en sesi\u00F3n
				//session.setAttribute(COLECCION_ARTICULOS, colVistaArticuloTO);

				formulario.setDatos(vistaArticuloDTOCol);

			}
			//------------ si se escogi\u00F3 Actualizar producci\u00F3n o Enviar despacho -----------------
			else if(formulario.getBotonActProduccion() != null)
			{
				//se obtiene la colecci\u00F3n de art\u00EDculos a actualizar
				Collection <VistaArticuloDTO> listaArticulosProduccion = formulario.getDatos();
				
				//obtener pedidos a despachar
				String cadenaPedidos="";
				if(!CollectionUtils.isEmpty(listaArticulosProduccion)){
					for(VistaArticuloDTO lisArtPro:listaArticulosProduccion){
						if(!CollectionUtils.isEmpty(lisArtPro.getColVistaArticuloDTO())){
							for(VistaArticuloDTO lisArtEnt:lisArtPro.getColVistaArticuloDTO()){
								if(!CollectionUtils.isEmpty(lisArtEnt.getColVistaArticuloDTO())){
									for(VistaArticuloDTO lisArtPed:lisArtEnt.getColVistaArticuloDTO()){
											cadenaPedidos+=lisArtPed.getId().getCodigoPedido()+",";
									}
								}
								
							}
						}
					}
					
				}else{
					cadenaPedidos="-1";
				}
				beanSession.setCodigoPedido(cadenaPedidos);
				//se obtiene la lista de art\u00EDculos de sesi\u00F3n
				Collection articulosEnProduccion = (Collection)session.getAttribute(COLECCION_ARTICULOS);
				//esta variable se utiliza para indicar al m\u00E9todo que obtiene los pedidos
				//que obtenga la fecha m\u00EDnima de despacho solo de las entregas incompletas
				boolean obtFecMinDesEntPen = false;
				if(modoActualizarDespacho == null){
					//solo si se actualiza la producci\u00F3n
					obtFecMinDesEntPen = true;
				}

				//se forma la consulta para los pedidos que est\u00E1n en produccion
				List<VistaPedidoDTO> pedidosEnProduccion = (List<VistaPedidoDTO>)this.construirConsultaPedidos(request, beanSession, obtFecMinDesEntPen, false,Boolean.TRUE);
				
				//si se cambio el estado a EPR se vuelve a consultar con el nuevo estado
				if(cambiarEstadoEnProduccion(pedidosEnProduccion, request)){
					pedidosEnProduccion = (List<VistaPedidoDTO>)this.construirConsultaPedidos(request, beanSession, obtFecMinDesEntPen, false,Boolean.TRUE);
				}
				
				if(pedidosEnProduccion!=null && !pedidosEnProduccion.isEmpty()){
					String rolId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol();
					//se iteran los pedidos para asignar los filtros por usuario
					for(int i = 0; i<pedidosEnProduccion.size(); i++){
						VistaPedidoDTO vistaPedidoDTO = pedidosEnProduccion.get(i);
						//se asigna el filtro del usuario
						vistaPedidoDTO.setNpRolUsuarioAFiltrar(rolId);
						vistaPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
						LogSISPE.getLog().info("num pedido: {}", vistaPedidoDTO.getId().getCodigoPedido());
					}
				}

				try{
					
					LocalID localID = null;
					//se obtiene el c\u00F3digo de inicializaci\u00F3n para los ids de los DTOs
					if(beanSession.getCodigoLocal()!=null && beanSession.getCodigoLocal().intValue() != Integer.parseInt(codigoInicialIds)){
						localID = new LocalID();
						localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						localID.setCodigoLocal(beanSession.getCodigoLocal());
					}

					if(modoActualizarDespacho == null){
						//OANDINO: Asignar valor de observaci\u00F3n para SCSPETESTDETPED
						//Variable de indexaci\u00F3n
						int indice = 0;
						
						//Se recorre el listado total de registros y se asigna el respectivo valor para el campo observaci\u00F3n
						for(VistaArticuloDTO vistaArtDTO:listaArticulosProduccion){
							LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",vistaArtDTO.getId().getCodigoArticulo());
							LogSISPE.getLog().info("Observaci\u00F3n a asignar: {}",formulario.getObservacionArticulo()[indice]);
							
							//Se verifica que el contenido de observaci\u00F3n de art\u00EDculo sea diferente de nulo
							if(formulario.getObservacionArticulo()[indice]!=null && !formulario.getObservacionArticulo()[indice].equals(""))
								vistaArtDTO.setObservacionArticulo(formulario.getObservacionArticulo()[indice]);
							else
								vistaArtDTO.setObservacionArticulo(null);
							
							//Se incrementa el \u00EDndice
							indice++;
						}
					}
					
					SessionManagerSISPE.getServicioClienteServicio().transActualizarProduccion(localID, listaArticulosProduccion, pedidosEnProduccion, modoActualizarDespacho);
					
					//se realiza la llamada al m\u00E9todo que construye la consulta de art\u00EDculos
					articulosEnProduccion = this.construirConsultaArticulos(request, beanSession, modoActualizarDespacho);

					session.setAttribute(COLECCION_ARTICULOS,articulosEnProduccion);

					//se inicializan los datos del ordenamiento para los art\u00EDculos
					this.inicializarDatosOrdenamientoArticulos(request, modoActualizarDespacho);

					//eliminar la colecci\u00F3n de los pedidos
					session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);

					//se verifica el modo de actualizaci\u00F3n
					if(modoActualizarDespacho != null){
						success.add("exitoActualizacion",new ActionMessage("message.exito.actualizacionDespacho"));
					}else{
						success.add("exitoActualizacion",new ActionMessage("message.exito.actualizacionProduccion"));
					}

					formulario.setDatos(null);

					//se vuelve a paginar el formulario
					if(articulosEnProduccion!=null && !articulosEnProduccion.isEmpty()){
						LogSISPE.getLog().info("entr\u00F3 a la paginaci\u00F3n");
						int start = 0;
						if(Integer.parseInt(beanSession.getInicioPaginacion()) < articulosEnProduccion.size())
							start = Integer.parseInt(beanSession.getInicioPaginacion());

						//llamada al m\u00E9todo que realiza la paginaci\u00F3n
						Collection datosPagina = formulario.paginarDatos(articulosEnProduccion, start,articulosEnProduccion.size(),false);
						formulario.setDatos(datosPagina);
					}

					formulario.setOpSeleccionPedidos(null);
					session.removeAttribute(VECTOR_PEDIDOS_SELECCIONADOS);
					session.removeAttribute(VECTOR_ANTERIOR_PEDIDOS_SELECCIONADOS);
					
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					if(modoActualizarDespacho != null){
						errors.add("almacenarDatos",new ActionMessage("errors.llamadaServicio.registrarDatos","el Env\u00EDo de Despacho"));
					}else{
						errors.add("almacenarDatos",new ActionMessage("errors.llamadaServicio.registrarDatos","la Actualizaci\u00F3n de producci\u00F3n"));									
					}
					errors.add("exceptionSISPE",new ActionMessage("errors.SISPEException",ex.getMessage()));
				}
			}
			//------------------------ Control de Tabs -------------------------------
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)){								
				
				//---------- si se escogi\u00F3 el tab de los art\u00EDculos en producci\u00F3n ----------------
				if(beanSession.getPaginaTab().esTabSeleccionado(0))
				{
					LogSISPE.getLog().info("tab articulos");
					Collection articulosEnProduccion = (Collection)session.getAttribute(COLECCION_ARTICULOS);
					//se blanquea la colecci\u00F3n de datos
					formulario.setDatos(null);

					//se obtienen los pedidos seleccionados para saber cuantos fueron seleccionados anteriormente
					session.setAttribute(VECTOR_PEDIDOS_SELECCIONADOS, formulario.getOpSeleccionPedidos());

					//se obtiene el campo de b\u00FAsqueda de pedidos
					String [] indicesSeleccionadosAnterior = (String [])session.getAttribute(VECTOR_ANTERIOR_PEDIDOS_SELECCIONADOS);
					ArrayList <VistaPedidoDTO> colVistaPedidos = (ArrayList <VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					//variable para bandera para el control de la ejecuci\u00F3n de la consulta
					boolean consultarArticulosNuevamente = false;

					//condici\u00F3n que determina si se debe volver a realizar la consulta de art\u00EDculos
					if(formulario.getOpSeleccionPedidos()!=null && indicesSeleccionadosAnterior==null){
						//si las longitudes no coinciden se debe realizar una nueva b\u00FAsqueda
						if(colVistaPedidos!=null && (colVistaPedidos.size() != formulario.getOpSeleccionPedidos().length)){
							LogSISPE.getLog().info("*** 1 ***");
							consultarArticulosNuevamente= true;
						}
					}else if(formulario.getOpSeleccionPedidos()==null && indicesSeleccionadosAnterior!=null){
						if(colVistaPedidos!=null && (colVistaPedidos.size() != indicesSeleccionadosAnterior.length)){
							LogSISPE.getLog().info("*** 2 ***");
							//si uno de los campos no es nulo y el otro s\u00ED
							consultarArticulosNuevamente= true;
						}
					}else if(formulario.getOpSeleccionPedidos()!=null && indicesSeleccionadosAnterior!=null){
						//solo si los dos campos son diferentes de null
						if(formulario.getOpSeleccionPedidos().length == indicesSeleccionadosAnterior.length){
							LogSISPE.getLog().info("*** 3 ***");
							//se iteran los arreglos para comparar los indices
							for(int i=0; i<formulario.getOpSeleccionPedidos().length;i++){
								//si los c\u00F3digos son distintos
								if(!formulario.getOpSeleccionPedidos()[i].equals(indicesSeleccionadosAnterior[i])){
									consultarArticulosNuevamente= true;
									break;
								}
							}
						}else{
							consultarArticulosNuevamente= true;
						}
					}

					LogSISPE.getLog().info("realizarConsultaArticulos: {}",consultarArticulosNuevamente);

					if(consultarArticulosNuevamente){
						//se inicializan los c\u00F3digos
						String codigosPedidos = "";
						if(formulario.getOpSeleccionPedidos()!=null && colVistaPedidos!=null){
							//se iteran los \u00EDndices para obtener los c\u00F3digos de los pedidos
							for(int i=0; i<formulario.getOpSeleccionPedidos().length;i++){
								//se obtiene el objeto VistaPedidoDTO
								VistaPedidoDTO vistaPedidoDTO = colVistaPedidos.get(Integer.parseInt(formulario.getOpSeleccionPedidos()[i]));
								codigosPedidos = codigosPedidos + vistaPedidoDTO.getId().getCodigoPedido();
								//se verifica que no se lleg\u00F3 al \u00FAltimo registro
								if(i+1 < formulario.getOpSeleccionPedidos().length)
									codigosPedidos = codigosPedidos + ",";
							}
							beanSession.setCodigoPedido(codigosPedidos);
						}else{
							beanSession.setCodigoPedido(codigoInicialIds);
						}


						//se realiza la llamada al m\u00E9todo que construye la consulta de art\u00EDculos
						articulosEnProduccion = this.construirConsultaArticulos(request, beanSession, modoActualizarDespacho);
						session.setAttribute(COLECCION_ARTICULOS, articulosEnProduccion);
						session.setAttribute(VECTOR_ANTERIOR_PEDIDOS_SELECCIONADOS, formulario.getOpSeleccionPedidos());
						//se incializa la variable de inicio de paginaci\u00F3n
						beanSession.setInicioPaginacion("0");
						//se inicializan los datos del ordenamiento para los art\u00EDculos
						this.inicializarDatosOrdenamientoArticulos(request, modoActualizarDespacho);
						//JACC
						//se inicializan los datos del ordenamiento para los art\u00EDculos de los canastos
						this.inicializarDatosOrdenamientoArticulosRelacionados(request);
					}

					if(articulosEnProduccion!=null && !articulosEnProduccion.isEmpty()){
						//se realiza la paginaci\u00F3n
						int start = 0;
						if(Integer.parseInt(beanSession.getInicioPaginacion()) < articulosEnProduccion.size())
							start = Integer.parseInt(beanSession.getInicioPaginacion());

						//llamada al m\u00E9todo que realiza la paginaci\u00F3n
						Collection datosPagina = formulario.paginarDatos(articulosEnProduccion, start,articulosEnProduccion.size(),false);
						formulario.setDatos(datosPagina);

						//se inicializa la variable de inicio de paginaci\u00F3n de art\u00EDculos
						beanSession.setInicioPaginacion(String.valueOf(start));

						//se recuperan los datos del ordenamiento para los art\u00EDculos
						if(session.getAttribute(DATOS_TABLA_ORDENAR_ARTICULOS)!=null){
							session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, 
									session.getAttribute(DATOS_TABLA_ORDENAR_ARTICULOS));
							session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA,
									session.getAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS));
						}

						if(!consultarArticulosNuevamente){
							//se llama al m\u00E9todo que reasigna las cantidades ingresadas
							this.reasignarCantidadesIngresadas(formulario, session);
						}
					}

					//------ se asignan nuevamente los campos de b\u00FAsqueda con los valores de la \u00FAltima b\u00FAsqueda
					if(beanSession.getCodigoLocal()!=null)
						formulario.setCodigoLocal(beanSession.getCodigoLocal().toString());
					if(beanSession.getFechaInicial()!=null && beanSession.getFechaFinal()!=null 
							&& beanSession.getOpBusquedaFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
						//se formatean las fechas
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
						formulario.setFechaInicial(simpleDateFormat.format(beanSession.getFechaInicial()));
						formulario.setFechaFinal(simpleDateFormat.format(beanSession.getFechaFinal()));
					}
					//se asigna el valor del combo en la seccion de busqueda
					if(beanSession.getOpEntidadResonsable() != null){
						LogSISPE.getLog().info("VALOR DE SESSION");
						formulario.setOpEntidadResponsable(beanSession.getOpEntidadResonsable());
					}else{
						LogSISPE.getLog().info("OPCION DEFAUL");
						//Obtengo valor por defecto entidad Responsable.
						String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
						formulario.setOpEntidadResponsable(entidadBodega);
					}

					//formulario.setOpcionFecha(beanSession.getOpBusquedaFecha());
					//formulario.setOpcionCampoBusqueda(beanSession.getOpBusquedaCampo());
					//formulario.setCampoBusqueda(beanSession.getCampoBusqueda());
					//formulario.setEtiquetaFechas("Fecha de Despacho");
					//-------------------------------------------------------------

					session.removeAttribute(VECTOR_CANTIDADES_PRODUCIR);
				}
				//------------------- si se escogi\u00F3 el tab de los pedidos en producci\u00F3n -----------------
				else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
					LogSISPE.getLog().info("tab pedidos");

					//solo si la consulta se realiza para las cantidades pendientes
					if(session.getAttribute(CONSULTA_DESPACHO_TOTAL) == null){
						LogSISPE.getLog().info("consulta de pedidos pendientes");

						//se actualizan los pedidos que fueron chequeados
						formulario.setOpSeleccionPedidos((String [])session.getAttribute(VECTOR_PEDIDOS_SELECCIONADOS));
						//se respaldan las cantidades ingresadas en la secci\u00F3n de actualizaci\u00F3n de producci\u00F3n
						session.setAttribute(VECTOR_CANTIDADES_PRODUCIR, formulario.getCantidadProducida());
						//se respaldan la opci\u00F3nes de b\u00FAsqueda
						beanSession.setOpBusquedaFecha(formulario.getOpcionFecha());
						
						// IMPORTANTE: Comentarios sobre campos opcionCampoBusqueda y campoBusqueda para habilitar consulta m\u00FAltiple
						// -------------------------------------------------------------------------
						//beanSession.setOpBusquedaCampo(formulario.getOpcionCampoBusqueda());
						//beanSession.setCampoBusqueda(formulario.getCampoBusqueda());
						// -------------------------------------------------------------------------

						if(session.getAttribute(COLECCION_ARTICULOS)!=null && 
								!((Collection)session.getAttribute(COLECCION_ARTICULOS)).isEmpty())
						{
							try
							{
								//creaci\u00F3n de la coleci\u00F3n que almacenar\u00E1 los pedidos
								Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
								if(colVistaPedidoDTO == null){
									//se forma la consulta para los pedidos que est\u00E1n en produccion
									colVistaPedidoDTO = this.construirConsultaPedidos(request, beanSession, false, true,Boolean.FALSE);
									//se actualiza la variable de sesi\u00F3n
									session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
									//se incializan los datos para ordenamiento para los pedidos
									this.inicializarDatosOrdenamientoPedidos(request, modoActualizarDespacho); 
								}
								//se blanquean los datos de la colecci\u00F3n
								formulario.setDatos(null);
								if(colVistaPedidoDTO == null || colVistaPedidoDTO.isEmpty()){
									if(modoActualizarDespacho != null){
										infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Pedidos pendientes de Despacho","Art\u00EDculos"));
									}else{
										infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Pedidos en Producci\u00F3n","Art\u00EDculos"));									
									}

								}else{
									LogSISPE.getLog().info("ENTRO A LA PAGINACION");
									//llamada al m\u00E9todo que realiza la paginaci\u00F3n
									Collection datosPagina = formulario.paginarDatos(colVistaPedidoDTO, 0, colVistaPedidoDTO.size(),false);
									formulario.setDatos(datosPagina);

									//se elimina porque se inicia nuevamente en cero
									session.removeAttribute(INICIO_PAGINACION_PEDIDOS);

									//se recuperan los datos del ordenamiento para los pedidos
									if(session.getAttribute(DATOS_TABLA_ORDENAR_PEDIDOS)!=null){
										session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, 
												session.getAttribute(DATOS_TABLA_ORDENAR_PEDIDOS));
										session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA,
												session.getAttribute(DATOS_COLUMNA_ORDENADA_PEDIDOS));
									}
								}	
							}catch(SISPEException ex){
								LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
								if(modoActualizarDespacho != null){
									errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Art\u00EDculos pendientes de Despacho"));
								}else{
									errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Art\u00EDculos en producci\u00F3n"));
								}
							}
						}else{
							if(modoActualizarDespacho != null){
								infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Pedidos pendientes de Despacho","Art\u00EDculos"));
							}else{
								infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Pedidos en Producci\u00F3n","Art\u00EDculos"));									
							}
						}
					}else{
						formulario.setDatos(null);
					}
				}
			}
			//--------------------------- exportar a pdf ---------------------------
			else if (request.getParameter("ayuda") != null && request.getParameter("ayuda").equals("exportarPDF"))
			{
				LogSISPE.getLog().info("entra a exportar PDF");
				LogSISPE.getLog().info("ayuda: {}", request.getParameter("ayuda"));
				//se inicializa el tipo de archivo
				String tipoArchivo = "pdf";
				//se incializa el nombre del archivo
				final String NOMBRE_REPORTE = "produccion_articulos";

				//se obtiene la colecci\u00F3n de art\u00EDculos y se verifica si tiene datos
				Collection colVistaArticuloDTO = (Collection)session.getAttribute(COLECCION_ARTICULOS);
				if(colVistaArticuloDTO == null || colVistaArticuloDTO.isEmpty()){
					errors.add("errorReporteProduccion",new ActionMessage("errors.exportarDatos.sinDatos", tipoArchivo));
				}else{
					request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));			
					LogSISPE.getLog().info("exporta a: {}" , tipoArchivo);
					salida = "reporteProduccionPDF";
				}
			}
			//-------------------------- exportar a excel ---------------------------
			else if (request.getParameter("ayuda") != null && request.getParameter("ayuda").equals("exportarXLS"))
			{
				LogSISPE.getLog().info("entra a exportar XLS");
				LogSISPE.getLog().info("ayuda: ", request.getParameter("ayuda"));
				//se inicializa el tipo de archivo
				String tipoArchivo = "xls";
				//se incializa el nombre del archivo
				final String NOMBRE_REPORTE = "produccion_articulos";
				//se obtiene la colecci\u00F3n de art\u00EDculos y se verifica si tiene datos
				Collection colVistaArticuloDTO = (Collection)session.getAttribute(COLECCION_ARTICULOS);
				if(colVistaArticuloDTO == null || colVistaArticuloDTO.isEmpty()){
					errors.add("errorReporteProduccion",new ActionMessage("errors.exportarDatos.sinDatos", tipoArchivo));
				}else{
					request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));			
					LogSISPE.getLog().info("exporta a: {}", tipoArchivo);
					salida = "reporteProduccionXLS";
				}
			}     

			//------------pop up para reportes-------------------
			else if (peticion!= null && peticion.equals("xls"))
			{

				//se obtiene la colecci\u00F3n de art\u00EDculos y se verifica si tiene datos
				VistaArticuloDTO  vistaArticuloDTO = (VistaArticuloDTO)session.getAttribute(VISTA_ARTICULO_REPORTE);
				if(vistaArticuloDTO == null){
					errors.add("errorReporteProduccion",new ActionMessage("errors.exportarDatos.sinDatos", "xls"));
				}
				else if(session.getAttribute(CONSULTA_DESPACHO_TOTAL)==null)
				{	
					LogSISPE.getLog().info("entra a crear popUp");
					//se crea la ventana popUp
					UtilPopUp popUp = new UtilPopUp();
					popUp.setTituloVentana("Opciones de agrupaci\u00F3n");
					popUp.setMensajeVentana("Escoja una de las siguientes opciones para crear el reporte: ");
					popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
					popUp.setValorOK("realizarEnvioSinProcesando2('"+ACEPTAR_REPORTE+"');");
					popUp.setAccionEnvioCerrar("hide(['popupConfirmar']);ocultarModal();");
					popUp.setEtiquetaBotonOK("Aceptar");
					popUp.setEtiquetaBotonCANCEL("Cancelar");
					popUp.setValorCANCEL("hide(['popupConfirmar']);ocultarModal();");
					popUp.setContenidoVentana("/servicioCliente/actualizarDespacho/opcionesAgrupacion.jsp");
					popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
					
					//se guarda
					request.setAttribute(SessionManagerSISPE.POPUP, popUp);
				}
				else if(session.getAttribute(CONSULTA_DESPACHO_TOTAL)!=null){

					LogSISPE.getLog().info("genera reporte sin popUp");
					//se inicializa el tipo de archivo
					String tipoArchivo = "xls";
					//se inicializa el nombre del archivo
					final String NOMBRE_REPORTE = "despacho_totales";
					request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));

					LogSISPE.getLog().info("exporta a: {}", tipoArchivo);
					salida="xlsReporteTotales";

				}
			}
			//-------------------------- exportar a excel ---------------------------
			else if(peticion != null && peticion.equals(ACEPTAR_REPORTE)){
				LogSISPE.getLog().info("entra a aceptar reporte");
				//tomo de sesi\u00F3n el objeto de consulta
				VistaArticuloDTO vistaArticuloDTO= (VistaArticuloDTO)session.getAttribute(VISTA_ARTICULO_REPORTE);
				vistaArticuloDTO.setNpTipoReporte(formulario.getOpTipoAgrupacion());

				//llamo al m\u00E9todo que contruye la consulta.
				Collection<VistaArticuloDTO>  vistaArticuloDTOcol = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaArticulo(vistaArticuloDTO);
				if(vistaArticuloDTOcol!=null)
				{
					//elimino variables
					session.removeAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo1");
					session.removeAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo2");
					session.removeAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo3");
					session.removeAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo4");
					//subo a sesi\u00F3n la colecci\u00F3n obtenida
					session.setAttribute(COLECCION_ARTICULOS_REPORTE, vistaArticuloDTOcol);
					//	se inicializa el tipo de archivo
					String tipoArchivo = "xls";
					//se inicializa el nombre del archivo
					final String NOMBRE_REPORTE = "despacho_articulos";
					LogSISPE.getLog().info("exporta a: {}" , tipoArchivo);
					request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo(NOMBRE_REPORTE, tipoArchivo));

					//subo a sesi\u00F3n la variable correspondiente a la opci\u00F3n escogida
					if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_1))){
						//fecha despacho, art\u00EDculo, local
						LogSISPE.getLog().info("escogio 1");
						session.setAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo1", "ok");
					}
					else if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_2))){
						//Art\u00EDculo, fecha de despacho, local
						LogSISPE.getLog().info("escogio 2");
						session.setAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo2", "ok");
					}
					else if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_3))){
						//local, fecha de despacho, art\u00EDculo
						LogSISPE.getLog().info("escogio 3");
						session.setAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo3", "ok");
					}
					else if(formulario.getOpTipoAgrupacion().equals(session.getAttribute(REPORTE_4)))
					{
						//local,  art\u00EDculo, fecha de despacho
						LogSISPE.getLog().info("escogi\u00F3 4");
						session.setAttribute("ec.com.sic.sispe.reporte.rptdespachoArticulo4", "ok");
					}
					salida="rptDespachoArticulo";
				}
				LogSISPE.getLog().info("peticion!!!: {}",peticion);
			}

			//--------------------- acci\u00F3n por omisi\u00F3n ------------------------
			else
			{    
				LogSISPE.getLog().info("Se ingresa por primera vez a ActualizacionProduccionAction.java...");
				
				//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com.smx.sic"
				SessionManagerSISPE.removeVarSession(request);

				//variable de sesion para cambiar el metas
				session.setAttribute(MessagesWebSISPE.getString("parametro.importacion.JQUERY"),"ok");
				//se blanquea la colecci\u00F3n del formulario que contiene la paginaci\u00F3n
				formulario.setDatos(null);
				formulario.setEtiquetaFechas("Fecha de Despacho");
				//Obtengo valor por defecto entidad Responsable.
				String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
				formulario.setOpEntidadResponsable(entidadBodega);

				session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de despacho");
				
				PaginaTab nuevaPaginaTab = null;
				Tab tabArticulos = null;
				Tab tabPedidos = null;

				//se inicializa la opci\u00F3n de b\u00FAsqueda por hoy
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));

				if(modoActualizarDespacho != null){
					session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Enviar despacho");
					//se guarda la acci\u00F3n en la sesi\u00F3n
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, 
							MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.enviarDespacho"));

					//Iniciar Tabs
					nuevaPaginaTab = new PaginaTab("actualizarDespacho", "actualizacionDespacho", 0, 444, request);
					tabArticulos = new Tab("Art\u00EDculos", "actualizarDespacho", "/servicioCliente/actualizarDespacho/listaArticulosEnviarDespacho.jsp", true);
					tabPedidos = new Tab("Pedidos", "actualizarDespacho", "/servicioCliente/actualizarDespacho/listaPedidosEnviarDespacho.jsp", false);

					formulario.setOpDespachoPendiente(SessionManagerSISPE.getEstadoActivo(request));
					
					session.setAttribute(ActualizacionDespachoAction.LOCAL_CON_DIFERENTES_DESTINOS, 
							MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.localConDestinosDiferentes"));
				}else{
					session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Actualizaci\u00F3n de producci\u00F3n");
					//se guarda la acci\u00F3n en la sesi\u00F3n
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, 
							MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion"));
					//session para saber si son canastos
					session.setAttribute("ec.com.smx.sic.sispe.canasto", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
					//JACC
					//se inicializan los datos del ordenamiento para los art\u00EDculos de los canastos
					this.inicializarDatosOrdenamientoArticulosRelacionados(request);

					//Iniciar Tabs
					nuevaPaginaTab = new PaginaTab("actualizarProduccion", "actualizacion", 0, 444, request);
					tabArticulos = new Tab("Art\u00EDculos", "actualizarProduccion", "/servicioCliente/actualizarProduccion/listaArticulosActProduccion.jsp", true);
					tabPedidos = new Tab("Pedidos", "actualizarProduccion", "/servicioCliente/actualizarProduccion/listaPedidosActProduccion.jsp", false);
					
					//se obtiene la clasificaci\u00F3n de canastos de cat\u00E1logo
					ParametroID parametroID = new ParametroID();
					parametroID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					parametroID.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo"));
					ParametroDTO parametroDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametroID);
					session.setAttribute(CODIGO_CANASTOS_CATALOGO, parametroDTO.getValorParametro());
					
					parametroID = null;
				}

				//se verifica si la entidad responsable es un local
				String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
					//se obtienen los locales por ciudad
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
				
				//subo a sesi\u00F3n las variables de los tipos de reporte
				session.setAttribute(REPORTE_1,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.fechaArticuloLocal"));
				session.setAttribute(REPORTE_2,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloFechaLocal"));
				session.setAttribute(REPORTE_3,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.LocalFechaArticulo"));
				session.setAttribute(REPORTE_4,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.LocalArticuloFecha"));

				nuevaPaginaTab.addTab(tabArticulos);
				nuevaPaginaTab.addTab(tabPedidos);
				//se actualiza la propiedad del bean
				beanSession.setPaginaTab(nuevaPaginaTab);
				
				
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}

		//se guardan los mensajes
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);

		
		//se envia el control a la p\u00E1gina correspondiente, indicada en el archivo struts-config
		LogSISPE.getLog().info("Salida: {}",salida);
		return mapping.findForward(salida);
	}

	/**
	 * Construye la consulta para obtener los pedidos en producci\u00F3n, en base a la consulta realizada
	 * para los art\u00EDculos.
	 * 
	 * @param 	request									Petici\u00F3n actual
	 * @param 	obtFecMinDesNoCom				Indica si se debe obtener la fecha m\u00EDnima de despacho de las entregas
	 * 																	no completadas (cantidadDespacho > cantidadDespachoParcial)
	 * @param 	obtSumCanResYPar				Indica si se debe obtener las cantidades reservadas, y parciales
	 * @return	pedidosEnProduccio			Colecci\u00F3n de objetos de tipo VistaPedidoDTO
	 * @throws 	Exception
	 */
	private Collection construirConsultaPedidos(HttpServletRequest request, 
			BeanSession beanSession, boolean obtFecMinDesNoCom, boolean obtSumCanResYPar,Boolean consultarPedidos)throws Exception{

		String estadoAfirmacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion");
		LogSISPE.getLog().info("lista de pedidos: {}",beanSession.getCodigoPedido());
		LogSISPE.getLog().info("ENTRO A LA CONSULTA DE PEDIDOS");
		//Se crea la vista del Pedido y se le asigna el estado en Producci\u00F3n
		VistaPedidoDTO consultaPedidosParaActProd = new VistaPedidoDTO();
		consultaPedidosParaActProd.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaPedidosParaActProd.getId().setCodigoAreaTrabajo(beanSession.getCodigoLocal());
		consultaPedidosParaActProd.getId().setCodigoPedido(beanSession.getCodigoPedido());
		consultaPedidosParaActProd.getId().setCodigoEstado(beanSession.getCodigoEstado());
		consultaPedidosParaActProd.setEstadoActual(estadoAfirmacion);
		//consultaPedidosParaActProd.setEtapaEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.finalizado"));
		consultaPedidosParaActProd.setLlaveContratoPOS(beanSession.getCodigoReservacion());
		consultaPedidosParaActProd.setNumeroDocumentoPersona(beanSession.getCedulaContacto());
		consultaPedidosParaActProd.setNombrePersona(beanSession.getNombreContacto());
		consultaPedidosParaActProd.setNombreEmpresa(beanSession.getNombreEmpresa());
		consultaPedidosParaActProd.setRucEmpresa(beanSession.getRucEmpresa());
		consultaPedidosParaActProd.setNpPrimeraFechaDespachoInicial(beanSession.getFechaInicial());
		consultaPedidosParaActProd.setNpPrimeraFechaDespachoFinal(beanSession.getFechaFinal());
		consultaPedidosParaActProd.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
		consultaPedidosParaActProd.setProduccionPendiente(estadoAfirmacion);
		consultaPedidosParaActProd.setEntidadResponsable(beanSession.getOpEntidadResonsable());
		consultaPedidosParaActProd.setNpObtenerEntregasPedidos(consultarPedidos);
		
		//significa que la fecha minima de despacho obtenida para los pedidos
		//es en base a las entregas cuyos art\u00EDculos est\u00E1n incompletos
		if(obtFecMinDesNoCom){
			consultaPedidosParaActProd.setNpFecMinDesEntNoCom(estadoAfirmacion);
		}
		
		//Condici\u00F3n para obtener los totales por pedido de las cantidades reservadas, y parciales
		if(obtSumCanResYPar){
			consultaPedidosParaActProd.setNpEtapaProduccion(estadoAfirmacion);
		}
		
		//consulta por datos del art\u00EDculo
		if(beanSession.getCodigoClasificacion()!=null || beanSession.getCodigoArticulo()!=null || beanSession.getDescripcionArticulo()!=null){
			consultaPedidosParaActProd.setArticuloDTO(new ArticuloDTO());
			LogSISPE.getLog().info("****clasificacion******");
			consultaPedidosParaActProd.getArticuloDTO().setCodigoClasificacion(beanSession.getCodigoClasificacion());
			LogSISPE.getLog().info("****codArticulo******");
			consultaPedidosParaActProd.getArticuloDTO().getId().setCodigoArticulo(beanSession.getCodigoArticulo());
			LogSISPE.getLog().info("****desArticulo******");
			consultaPedidosParaActProd.getArticuloDTO().setDescripcionArticulo(beanSession.getDescripcionArticulo());
		}

		//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n de pedidos
		Collection pedidosEnProduccion = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaActualizarProduccion(consultaPedidosParaActProd);

		return pedidosEnProduccion;
	}

	/**
	 * Construye la consulta para obtener los art\u00EDculo en producci\u00F3n, en base a los pedidos seleccionados
	 * en la secci\u00F3n pedidos.
	 * 
	 * @param 	request									Petici\u00F3n actual
	 * @return	pedidosEnProduccio			Colecci\u00F3n de objetos de tipo VistaPedidoDTO
	 * @throws 	Exception
	 */
	private Collection construirConsultaArticulos(HttpServletRequest request, 
			BeanSession beanSession, String modoActualizarDespacho)throws Exception{

		HttpSession session = request.getSession();
		LogSISPE.getLog().info("ENTRO A LA CONSULTA DE ARTICULOS");
		String estadoAfirmacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion");
		String estadoNegacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion");

		//se crea el objeto para la consulta
		VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
		consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		if(modoActualizarDespacho != null){
			consultaVistaArticuloDTO.setObtenerStockArticulos(estadoAfirmacion);
		}else{
			//para traer los art\u00EDculos relacionados
			consultaVistaArticuloDTO.setArticulos(new ArrayList());
			consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articulo"));
		}
		consultaVistaArticuloDTO.getId().setCodigoAreaTrabajo(beanSession.getCodigoLocal());
		consultaVistaArticuloDTO.getId().setCodigoPedido(beanSession.getCodigoPedido());
		consultaVistaArticuloDTO.getId().setCodigoEstado(beanSession.getCodigoEstado());
		consultaVistaArticuloDTO.setCodigoReservacion(beanSession.getCodigoReservacion());
		consultaVistaArticuloDTO.setCedulaContacto(beanSession.getCedulaContacto());
		consultaVistaArticuloDTO.setNombreContacto(beanSession.getNombreContacto());
		consultaVistaArticuloDTO.setNombreEmpresa(beanSession.getNombreEmpresa());
		consultaVistaArticuloDTO.setRucEmpresa(beanSession.getRucEmpresa());
		//a\u00F1ado el tipo de entidad responsable
		consultaVistaArticuloDTO.setEntidadResponsable(beanSession.getOpEntidadResonsable());
		
		if(beanSession.getCodigoArticulo()!=null)
			consultaVistaArticuloDTO.getId().setCodigoArticulo(beanSession.getCodigoArticulo());
		consultaVistaArticuloDTO.setDescripcionArticulo(beanSession.getDescripcionArticulo());
		consultaVistaArticuloDTO.setCodigoClasificacion(beanSession.getCodigoClasificacion());

		consultaVistaArticuloDTO.setArticuloCompletado(estadoNegacion);
		//se asignan las fechas con las cuales se realiz\u00F3 la \u00FAltima b\u00FAsqueda, para actualizar a lista de art\u00EDculos
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(beanSession.getFechaInicial());
		consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(beanSession.getFechaFinal());

		consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
		consultaVistaArticuloDTO.setArticuloPermitido(estadoAfirmacion);

		//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
		Collection articulosEnProduccion = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosParaActualizarProduccion(consultaVistaArticuloDTO);

		//se completa la cantidad que se despachar
		if(modoActualizarDespacho != null){
			for(Iterator<VistaArticuloDTO> it = articulosEnProduccion.iterator(); it.hasNext();){
				VistaArticuloDTO vistaArticuloDTO = it.next();
				vistaArticuloDTO.setNpCantidadParcialEstado(vistaArticuloDTO.getDiferenciaCantidadEstado());
				Long cantidadDespachar = 0L;
				if(vistaArticuloDTO.getNpStockArticulo().longValue() < vistaArticuloDTO.getDiferenciaCantidadEstado().longValue()){
					cantidadDespachar = vistaArticuloDTO.getNpStockArticulo();
				}else{
					cantidadDespachar = vistaArticuloDTO.getDiferenciaCantidadEstado();
				}
				vistaArticuloDTO.setNpCantidadParcialEstado(cantidadDespachar);
								
			}
		}
		
		//subo a sesi\u00F3n el objeto para el reporte
		session.setAttribute(VISTA_ARTICULO_REPORTE, consultaVistaArticuloDTO);

		return articulosEnProduccion;
	}

	/**
	 * Reasigna la cantidades ingresadas en los art\u00EDculos hijos.
	 * 
	 * @param 	cantidadesAProducir			- Arreglo de cantidades a reasignar
	 * @param 	indiceCantidades				- Indice del arreglo de cantidades a reasignar
	 * @param 	articulosHijos					- Colecci\u00F3n de art\u00EDculos
	 * @return 	indice actualizado del arreglo de cantidades
	 */
	private int reasignarCantidadesHijos(String [] cantidadesAProducir, int indCan, 
			Collection<ArticuloDTO> articulosHijos)throws Exception{
		int indiceCantidades = indCan;
		indiceCantidades ++;
		//se itera la colecci\u00F3n de los art\u00EDculos hijos
		for(ArticuloDTO articuloDTO: articulosHijos){
			if(articuloDTO.getArticulos()!=null && !articuloDTO.getArticulos().isEmpty()){
				//se llama a la funci\u00F3n recursiva
				indiceCantidades = reasignarCantidadesHijos(cantidadesAProducir, indiceCantidades, articuloDTO.getArticulos());
			}else{
				try{
					LogSISPE.getLog().info("CANTIDAD A PRODUCIR (HIJO) ["+indiceCantidades+"]: "+cantidadesAProducir[indiceCantidades]);
					articuloDTO.setNpCantidadAProducir(Long.valueOf(cantidadesAProducir[indiceCantidades]));
					articuloDTO.setNpCantidadAProducirFija(articuloDTO.getNpCantidadAProducir());
				}catch(NumberFormatException ex){
					articuloDTO.setNpCantidadAProducir(new Long(0));
					articuloDTO.setNpCantidadAProducirFija(articuloDTO.getNpCantidadAProducir());
				}
				indiceCantidades++;  //se incrementa el indice
			}
		}
		return indiceCantidades;
	}

	/**
	 * Inicializa los datos para el ordenamiento de la colecci\u00F3n de pedidos
	 * @param request
	 */
	private void inicializarDatosOrdenamientoPedidos(HttpServletRequest request, String modoActualizarDespacho){

		HttpSession session = request.getSession();
		String [][] datosTabla = null;
		//se verifica el modo de actualizaci\u00F3n
		if(modoActualizarDespacho != null){
			//se incializan los datos de la tabla
			datosTabla = new String [][] {
					{"id.codigoPedido", "No de pedido", null},
					{"llaveContratoPOS", "No de reservaci\u00F3n", null},
//					{"nombreContacto", "Nombre del Cliente", null},
					{"contactoEmpresa", "Nombre del cliente", null},
					{"descripcionEstado", "Descripci\u00F3n estado", null},
					{"cantidadReservadaEstado", "Cantidad reservada", null},
					{"cantidadParcialEstado", "Cantidad despachada", null},
					{"diferenciaCantidadEstado", "Cantidad pendiente", null},
					{"porcentajeProducido", "Porcentaje despachado", null},
					{"primeraFechaDespacho", "Fecha de despacho", null}
			};
		}else{
			//se incializan los datos de la tabla
			datosTabla = new String [][] {
					{"id.codigoPedido", "No de pedido", null},
					{"llaveContratoPOS", "No de reservaci\u00F3n", null},
//					{"nombreContacto", "Nombre del Cliente", null},
					{"contactoEmpresa", "Nombre del cliente", null},
					{"cantidadReservadaEstado", "Cantidad reservada", null},
					{"cantidadParcialEstado", "Cantidad producida", null},
					{"diferenciaCantidadEstado", "Cantidad pendiente", null},
					{"porcentajeProducido", "Porcentaje producido", null},
					{"primeraFechaDespacho", "Fecha de despacho", null}
			};
		}

		//se guardan los datos en sesi\u00F3n
		session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
		//se inicializa los datos de la culumna ordenada 
		session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {"Fecha de Despacho", "Ascendente"});

		//se inicializan las variables de respaldo para los pedidos
		session.setAttribute(DATOS_TABLA_ORDENAR_PEDIDOS, datosTabla);
		session.setAttribute(DATOS_COLUMNA_ORDENADA_PEDIDOS, new String [] {"Fecha de Despacho", "Ascendente"});
	}

	/**
	 * Inicializa los datos para el ordenamiento de la colecci\u00F3n de art\u00EDculos
	 * @param request
	 */
	private void inicializarDatosOrdenamientoArticulos(HttpServletRequest request, String modoActualizardespacho){

		HttpSession session = request.getSession();

		String columnaOrdenada = "Fecha de Despacho";

		String [][] datosTabla = null;
		if(modoActualizardespacho != null){
			//se incializan los datos de la tabla
			datosTabla = new String [][]{
					{"id.codigoArticulo", "C\u00F3digo barras", null},
					{"descripcionArticulo", "Descripci\u00F3n art\u00EDculo", null},
					{"cantidadReservadaEstado", "Cantidad reservada", null},
					{"cantidadParcialEstado", "Cantidad despachada", null},
					{"diferenciaCantidadEstado", "Cantidad pendiente", null},
					{"porcentajeProducido", "Porcentaje despachado", null},
					{"fechaDespachoBodega", "Fecha de despacho", null}
			};

			//si la consulta es para las cantidades totales
			if(session.getAttribute(CONSULTA_DESPACHO_TOTAL)!=null){
				columnaOrdenada = "C\u00F3digo de barras";
			}
		}else{
			//se incializan los datos de la tabla
			datosTabla = new String [][]{
					{"id.codigoArticulo", "C\u00F3digo barras", null},
					{"descripcionArticulo", "Descripci\u00F3n art\u00EDculo", null},
					{"cantidadReservadaEstado", "Cantidad reservada", null},
					{"cantidadParcialEstado", "Cantidad producida", null},
					{"diferenciaCantidadEstado", "Cantidad pendiente", null},
					{"porcentajeProducido", "Porcentaje producido", null},
					{"unidadManejo", "Unidad de manejo", null},
					{"fechaDespachoBodega", "Fecha de despacho", null}
			};
			columnaOrdenada = "C\u00F3digo de barras";
		}

		//se guardan los datos en sesi\u00F3n
		session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
		//se inicializa los datos de la culumna ordenada 
		session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {columnaOrdenada, "Ascendente"});

		//se inicializan las variables de respaldo para los art\u00EDculos
		session.setAttribute(DATOS_TABLA_ORDENAR_ARTICULOS, datosTabla);
		session.setAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS, new String [] {columnaOrdenada, "Ascendente"});
	}

	/**
	 * Inicializa los datos para el ordenamiento de la colecci\u00F3n de art\u00EDculos cuando tenga articulos relacionados un canasto
	 * @param request
	 */
	private void inicializarDatosOrdenamientoArticulosRelacionados(HttpServletRequest request){

		HttpSession session = request.getSession();

		//se incializan los datos de la tabla
		String[][] datosTabla = {
				{"id.codigoArticulo", "C\u00F3digo barras", null},
				{"articuloDTO.descripcionArticulo", "Descripci\u00F3n art\u00EDculo", null},
				{"cantidadArticulo", "Cantidad reservada", null},
		};
		String sesionDatosTablaOrdenar=(SessionManagerSISPE.DATOS_TABLA_ORDENAR).toString()+"2";
		LogSISPE.getLog().info("sesion_datos_tabla_ordenar1: {}" , sesionDatosTablaOrdenar);

		//se guardan los datos en sesi\u00F3n
		session.setAttribute(sesionDatosTablaOrdenar, datosTabla);
		//se inicializa los datos de la culumna ordenada 
		//session.setAttribute((SessionManagerSISPE.DATOS_COLUMNA_ORDENADA).toString()+"2", new String [] {"Descripci\u00F3n Art\u00EDculo", "Ascendente"});
	}

	/**
	 * 
	 * @param formulario
	 * @param session
	 * @throws Exception
	 */
	private void reasignarCantidadesIngresadas(ControlProduccionForm formulario, HttpSession session)throws Exception{

		//se verifican las cantidades que inicialmente se ingresaron
		int indiceCantidades = 0;
		String [] cantidadesAProducir = (String [])session.getAttribute(VECTOR_CANTIDADES_PRODUCIR);
		if(cantidadesAProducir!=null){
			for(Iterator<VistaArticuloDTO> it = formulario.getDatos().iterator(); it.hasNext();){
				VistaArticuloDTO vistaArticuloDTO = it.next();
				LogSISPE.getLog().info("CANTIDAD["+indiceCantidades+"]: "+cantidadesAProducir[indiceCantidades]);
				if(vistaArticuloDTO.getArticulos()!=null && !vistaArticuloDTO.getArticulos().isEmpty()){
					//llamada a la funci\u00F3n recursiva que reasigna los valores ingresados en las cantidades a 
					//producir de los hijos de los art\u00EDculos gen\u00E9ricos
					indiceCantidades = reasignarCantidadesHijos(cantidadesAProducir,indiceCantidades,vistaArticuloDTO.getArticulos());
				}else if(vistaArticuloDTO.getNpDesplegarItemsReceta()!=null){
					try{
						//se vuelve a asignar la cantidad en la colecci\u00F3n principal                  
						vistaArticuloDTO.setNpCantidadParcialEstado(Long.valueOf(cantidadesAProducir[indiceCantidades]));
						vistaArticuloDTO.setNpCantidadParcialEstadoFija(vistaArticuloDTO.getNpCantidadParcialEstado());
					}catch(NumberFormatException ex){
						vistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
						vistaArticuloDTO.setNpCantidadParcialEstadoFija(vistaArticuloDTO.getNpCantidadParcialEstado());
					}
					indiceCantidades ++;	
					//llamada a la funci\u00F3n recursiva que reasigna los valores ingresados en las cantidades a 
					//producir de los hijos de los art\u00EDculos gen\u00E9ricos en una receta
					for(Iterator itReceta = vistaArticuloDTO.getRecetaArticulos().iterator(); itReceta.hasNext();){
						ArticuloRelacionDTO recetaArticuloDTO = (ArticuloRelacionDTO)itReceta.next();
						//esta condici\u00F3n es para se tomen en cuenta solo los art\u00EDculos de tipo general
						if(recetaArticuloDTO.getArticuloRelacion()!=null && !CollectionUtils.isEmpty(recetaArticuloDTO.getNpArticulos()))
							indiceCantidades = reasignarCantidadesHijos(cantidadesAProducir,indiceCantidades,recetaArticuloDTO.getNpArticulos());
					}
				}else{
					try{
						//se vuelve a asignar la cantidad en la colecci\u00F3n principal                  
						vistaArticuloDTO.setNpCantidadParcialEstado(Long.valueOf(cantidadesAProducir[indiceCantidades]));
						vistaArticuloDTO.setNpCantidadParcialEstadoFija(vistaArticuloDTO.getNpCantidadParcialEstado());
					}catch(NumberFormatException ex){
						vistaArticuloDTO.setNpCantidadParcialEstado(new Long(0));
						vistaArticuloDTO.setNpCantidadParcialEstadoFija(vistaArticuloDTO.getNpCantidadParcialEstado());
					}
					indiceCantidades ++;
				}
			}
		}else
			LogSISPE.getLog().info("cantidadesAProducir NULL");
	}
	
	
	/**
	 * Se actualiza el estado del pedido de REC a EPR
	 * @param pedidosEnProduccion
	 * @param request
	 * @return true si se cambio el estado, false caso contrario
	 * @throws Exception
	 */
	private boolean cambiarEstadoEnProduccion(List<VistaPedidoDTO> pedidosEnProduccion, HttpServletRequest request) throws Exception{
		
		boolean cambioEstado = false;
		
		if(CollectionUtils.isNotEmpty(pedidosEnProduccion)){
			
			List<VistaPedidoDTO> pedidosControlProduccion = new ArrayList<VistaPedidoDTO>();
			
			String rolId = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol();
			//se iteran los pedidos para asignar los filtros por usuario
			for(VistaPedidoDTO vistaPedidoDTO : pedidosEnProduccion){
				
				if(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))){
					
					//se asigna el filtro del usuario
					vistaPedidoDTO.setNpRolUsuarioAFiltrar(rolId);
					vistaPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
					
					//se llama a la funci\u00F3n que carga los descuentos del pedido
					//esto es necesario para el correcto registro de los descuentos en caso de que existan
					WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO,request,Boolean.FALSE);
					pedidosControlProduccion.add(vistaPedidoDTO);
				}
			}
			
			if(!pedidosControlProduccion.isEmpty()){
				try{
					//se registra el pedido en Producci\u00F3n
					SessionManagerSISPE.getServicioClienteServicio().transRegistrarEnProduccion(pedidosControlProduccion);
					cambioEstado = true;
				}catch (Exception e){
					LogSISPE.getLog().error("Error al cambiarEstadoEnProduccion. "+e);
					cambioEstado = false;
				}
			}
		}
		
		return cambioEstado;
	}
}
