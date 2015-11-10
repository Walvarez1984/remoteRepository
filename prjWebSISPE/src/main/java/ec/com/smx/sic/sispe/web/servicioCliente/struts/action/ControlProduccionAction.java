/*
 * Clase ControlProduccionAction.java
 * Creado el 26/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ControlProduccionForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Permite controlar los pedidos y art\u00EDculos que est\u00E1n con producci\u00F3n pendiente.<br>
 * Muestra un lista con los pedidos que tienen una producci\u00F3n pendiente, es decir pedidos con estado
 * RESERVADO o EN PRODUCCION y sus porcentajes de producci\u00F3n, los pedidos RESERVADOS pueden ser enviados 
 * a producci\u00F3n. Tambi\u00E9n muestra una lista de todos los art\u00EDculos que est\u00E1n en producci\u00F3n junto con las cantidades 
 * totales a producir y el porcentaje parcial producido hasta el momento.
 * </p>
 * @author 	fmunoz
 * @author	mbraganza 
 * @version	2.0
 * @since  	JSDK 1.4.2
 */
public class ControlProduccionAction extends BaseAction
{
	//se obtienen los estados de b\u00FAsqueda
	private String estadoReservado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado");
	private String estadoEnProduccion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion");

	private final String COLECCION_ARTICULOS = "ec.com.smx.sic.sispe.articulos";
	private final String INICIO_PAGINACION_ARTICULOS = "ec.com.smx.sic.sispe.paginacionArticulos.inicio";

	//nombre para las variables de respaldo que contienen los datos de ordenamiento para pedidos y art\u00EDculos
	private final String DATOS_TABLA_ORDENAR_PEDIDOS = "ec.com.smx.sic.sispe.ordenamiento.datosTablaPedidos";
	private final String DATOS_COLUMNA_ORDENADA_PEDIDOS = "ec.com.smx.sic.sispe.ordenamiento.datosColumnaPedido";

	private final String DATOS_TABLA_ORDENAR_ARTICULOS = "ec.com.smx.sic.sispe.ordenamiento.datosTablaArticulos";
	private final String DATOS_COLUMNA_ORDENADA_ARTICULOS = "ec.com.smx.sic.sispe.ordenamiento.datosColumnaArticulo";
	private final String VISTA_ARTICULO_REPORTE="ec.com.sic.sispe.ec.com.smx.sic.sispe.vistaArticuloReporte";
	private static final String ACEPTAR_REPORTE = "aceptarReporte";
	private final String COLECCION_ARTICULOS_REPORTE = "ec.com.smx.sic.sispe.articulosReporte";
	private final String DIAS_RESTAR_FECHA_MINIMA_DESPACHO = "ec.com.smx.sic.sispe.diasRestarFechaMinimaDespacho";
	
	//variables con las opciones de tipo de reporte
	private final String REPORTE_1="ec.com.smx.sic.sispe.articulo.rptFechaArtLocal";
	private final String REPORTE_2="ec.com.smx.sic.sispe.articulo.rptArtFechaLocal"; 
	private final String REPORTE_3="ec.com.smx.sic.sispe.articulo.rptLocalFechaArt";
	private final String REPORTE_4="ec.com.smx.sic.sispe.articulo.rptLocalArtFecha";	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente 
	 * respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa 
	 * crear).
	 * 
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo 
	 * se redirige el control
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();

		HttpSession session = request.getSession();

		//se obtiene el valor de la petici\u00F3n
		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("peticion: {}",peticion);

		String salida = "controlProduccion";
		ControlProduccionForm formulario = (ControlProduccionForm)form;
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		
		String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		try
		{
			/*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
			if(request.getParameter("range")!=null || request.getParameter("start")!=null)
			{
				LogSISPE.getLog().info("ENTRAR A LA PAGINACION");
				Collection datos = new ArrayList();
				if(beanSession.getPaginaTab()!=null && beanSession.getPaginaTab().esTabSeleccionado(0)){
					datos = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
					beanSession.setInicioPaginacion(request.getParameter("start"));
				}else if(beanSession.getPaginaTab()!=null && beanSession.getPaginaTab().esTabSeleccionado(1)){
					datos = (Collection)session.getAttribute(COLECCION_ARTICULOS);
					session.setAttribute(INICIO_PAGINACION_ARTICULOS, request.getParameter("start"));
				}

				if(datos!=null){
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(datos, Integer.parseInt(request.getParameter("start")),datos.size(),false);
					formulario.setDatos(datosPagina);
				}
				if(beanSession.getOpEstadoActivo()!= null &&  beanSession.getOpEstadoActivo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"))){
					formulario.setOpEstadoActivo(estadoActivo);
				}
				if(beanSession.getEstadoPedido() != null && !beanSession.getEstadoPedido().equals("")){
					formulario.setEstadoPedido(beanSession.getEstadoPedido());
				}	
			}
			//---------- Control de Tabs --------------
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
				try {

					//Pedidos
					if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
						Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
						if(colVistaPedidoDTO!=null && !colVistaPedidoDTO.isEmpty())
						{
							LogSISPE.getLog().info("ENTRO A LA PAGINACION");
							//llamada al m\u00E9todo que realiza la paginaci\u00F3n
							Collection datosPagina = formulario.paginarDatos(colVistaPedidoDTO, 0,colVistaPedidoDTO.size(),false);
							formulario.setDatos(datosPagina);

							//se incializa el indice de inicio de paginaci\u00F3n
							beanSession.setInicioPaginacion("0");

							//se recuperan los datos del ordenamiento para los pedidos
							if(session.getAttribute(DATOS_TABLA_ORDENAR_PEDIDOS)!=null){
								session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, 
										session.getAttribute(DATOS_TABLA_ORDENAR_PEDIDOS));
								session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA,
										session.getAttribute(DATOS_COLUMNA_ORDENADA_PEDIDOS));
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
						if(beanSession.getOpEntidadResonsable() != null){
							LogSISPE.getLog().info("VALOR DE SESSION");
							formulario.setOpEntidadResponsable(beanSession.getOpEntidadResonsable());
						}else{
							LogSISPE.getLog().info("OPCION DEFAUL");
							//Obtengo valor por defecto entidad Responsable.
							String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
							formulario.setOpEntidadResponsable(entidadBodega);
						}
						
						//IMPORTANTE: Se comentan campos opcionCampoBusqueda y campoBusqueda para habilitar consulta m\u00FAltiple
						// -------------------------------------------------------------------------------
						//formulario.setOpcionCampoBusqueda(beanSession.getOpBusquedaCampo());
						//formulario.setCampoBusqueda(beanSession.getCampoBusqueda());
						// -------------------------------------------------------------------------------
						
						
						formulario.setEtiquetaFechas("Fecha de Despacho");
						//-------------------------------------------------------------
					}

					//Art\u00EDculos
					else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {

						//se respaldan la opci\u00F3nes de b\u00FAsqueda
						beanSession.setOpBusquedaFecha(formulario.getOpcionFecha());
						
						// IMPORTANTE: Se comentan campos opcionCampoBusqueda y campoBusqueda para habilitar consulta m\u00FAltiple
						// -------------------------------------------------------------------------------
						//beanSession.setOpBusquedaCampo(formulario.getOpcionCampoBusqueda());
						//beanSession.setCampoBusqueda(formulario.getCampoBusqueda());
						// -------------------------------------------------------------------------------

						//colecci\u00F3n que almacenar\u00E1 la lista de pedidos
						Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
						//solo si hay pedidos con producci\u00F3n pendiente y no hay art\u00EDculos
						if(colVistaPedidoDTO!=null && !colVistaPedidoDTO.isEmpty())
						{
							//colecci\u00F3n que almacenar\u00E1 la lista de art\u00EDculos
							Collection colArticuloDTO = (Collection)session.getAttribute(COLECCION_ARTICULOS);
							//LogSISPE.getLog().info("codigoEstado: "+beanSession.getCodigoEstado() + ", codigoEstadoAnterior: "+beanSession.getCodigoEstadoAnterior());

							//significa que el estado de b\u00FAsqueda ha cambiado
							if(colArticuloDTO == null){

								//se obtiene el c\u00F3digo de inicializaci\u00F3n para los ids de los DTOs
								String codigoInicialIds = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID");

								//se realiza la comparaci\u00F3n correspondiente, para asignar el valor correcto al estado de b\u00FAsqueda
								if(beanSession.getCodigoEstado().equals(codigoInicialIds)){
									beanSession.setCodigoEstado(estadoReservado.concat(",").concat(estadoEnProduccion));
								}

								try{
									//creacion de la vista para la consulta de art\u00EDculos que est\u00E1n en producci\u00F3n
									VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
									consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
									consultaVistaArticuloDTO.getId().setCodigoAreaTrabajo(beanSession.getCodigoLocal());
									consultaVistaArticuloDTO.getId().setCodigoPedido(beanSession.getCodigoPedido());
									consultaVistaArticuloDTO.getId().setCodigoEstado(beanSession.getCodigoEstado());
									consultaVistaArticuloDTO.setArticuloCompletado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.negacion"));
									consultaVistaArticuloDTO.setCodigoReservacion(beanSession.getCodigoReservacion());
									consultaVistaArticuloDTO.setCedulaContacto(beanSession.getCedulaContacto());
									consultaVistaArticuloDTO.setNombreContacto(beanSession.getNombreContacto());
									consultaVistaArticuloDTO.setNombreEmpresa(beanSession.getNombreEmpresa());
									if (beanSession.getCodigoArticulo()!=null)
										consultaVistaArticuloDTO.getId().setCodigoArticulo(beanSession.getCodigoArticulo());
									consultaVistaArticuloDTO.setDescripcionArticulo(beanSession.getDescripcionArticulo());
									consultaVistaArticuloDTO.setCodigoClasificacion(beanSession.getCodigoClasificacion());
									consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(beanSession.getFechaInicial());
									consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(beanSession.getFechaFinal());
									consultaVistaArticuloDTO.setRucEmpresa(beanSession.getRucEmpresa());
									consultaVistaArticuloDTO.setNpTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloLocal"));
									//consultaVistaArticuloDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());

									//llamada al m\u00E9todo de la capa de servicio
									colArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosParaActualizarProduccion(consultaVistaArticuloDTO);
									session.setAttribute(COLECCION_ARTICULOS,colArticuloDTO);

									//se incializa los datos de ordenamiento para los art\u00EDculos
									this.inicializarDatosOrdenamientoArticulos(request);

									//subo a sesi\u00F3n el objeto con los par\u00E1metros de consulta para el reporte
									session.setAttribute(VISTA_ARTICULO_REPORTE, consultaVistaArticuloDTO);


								}catch(SISPEException ex){
									LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
									//cuando no se ha seleccionado un pedido para producci\u00F3n
									errors.add("Articulos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Articulos en Producci\u00F3n"));
								}
								//se actualiza el estado anterior
								//beanSession.setCodigoEstadoAnterior(beanSession.getCodigoEstado());
							}

							//se realiza la paginaci\u00F3n
							//se blanquean los datos de la lista
							formulario.setDatos(null);
							if(colArticuloDTO==null || colArticuloDTO.isEmpty()){
								infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Art\u00EDculos pendientes de producci\u00F3n","Pedidos"));
							}else{
								LogSISPE.getLog().info("ENTRO A LA PAGINACION");
								//llamada al m\u00E9todo que realiza la paginaci\u00F3n
								Collection datosPagina = formulario.paginarDatos(colArticuloDTO, 0,colArticuloDTO.size(),false);
								formulario.setDatos(datosPagina);

								//se elimina la variable que indica el inicio de paginaci\u00F3n en los art\u00EDculos
								session.removeAttribute(INICIO_PAGINACION_ARTICULOS);

								//se recuperan los datos del ordenamiento para los pedidos
								if(session.getAttribute(DATOS_TABLA_ORDENAR_ARTICULOS)!=null){
									session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, 
											session.getAttribute(DATOS_TABLA_ORDENAR_ARTICULOS));
									session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA,
											session.getAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS));
								}
							}
						}else{
							infos.add("listaVacia",new ActionMessage("message.listaVacia.controlProducion","Art\u00EDculos pendientes de producci\u00F3n","Pedidos"));
						}
					}

					salida = beanSession.getPaginaTab().getForwardPrincipal();
				}catch (Exception e) {
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}
			}
			//------------------ cuando se desea ver el detalle de un pedido ---------------
			else if(peticion!=null && peticion.startsWith("IPVA")){
				session.setAttribute(SessionManagerSISPE.INDICE_PEDIDO_VISTA_ARTICULO, peticion);
				salida = "detallePedido";
			}
			/*---------------------------- si presiona el bot\u00F3n buscar -----------------------------------*/
			else if(request.getParameter("buscar")!=null)
			{
				//se obtiene la lista de art\u00EDculos de sesi\u00F3n
				Collection<VistaPedidoDTO> pedidosPorProducir = new ArrayList<VistaPedidoDTO>();
				//creacion de la vista del Control de Producci\u00F3n
				
				//BORRAR ------------------------
				LogSISPE.getLog().info("Llama a m\u00E9todo x4");
				// -------------------------------
				
				VistaPedidoDTO consultaControlProduccionDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);

				Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
				
				//se obtienen los estados que tengan una producci\u00F3n pendiente: produccionPendiente [SI]
				String afirmacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion");
				consultaControlProduccionDTO.setProduccionPendiente(afirmacion);
				//consultaControlProduccionDTO.setEstadoActual(afirmacion);
				consultaControlProduccionDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());

				//se guardan los campos de b\u00FAsqueda para utilizarlos posteriormente
				beanSession.setCodigoLocal(consultaControlProduccionDTO.getId().getCodigoAreaTrabajo());
				beanSession.setCodigoPedido(consultaControlProduccionDTO.getId().getCodigoPedido());
				beanSession.setOpEntidadResonsable(consultaControlProduccionDTO.getEntidadResponsable());
				beanSession.setCodigoEstado(consultaControlProduccionDTO.getId().getCodigoEstado());
				beanSession.setCodigoReservacion(consultaControlProduccionDTO.getLlaveContratoPOS());
//				beanSession.setCedulaContacto(consultaControlProduccionDTO.getCedulaContacto());
//				beanSession.setNombreContacto(consultaControlProduccionDTO.getNombreContacto());
				beanSession.setCedulaContacto(consultaControlProduccionDTO.getNumeroDocumentoPersona());
				beanSession.setNombreContacto(consultaControlProduccionDTO.getNombrePersona());
				beanSession.setNombreEmpresa(consultaControlProduccionDTO.getNombreEmpresa());
				beanSession.setRucEmpresa(consultaControlProduccionDTO.getRucEmpresa());
				beanSession.setOpEstadoActivo(consultaControlProduccionDTO.getEstadoActual());
				beanSession.setEstadoPedido(consultaControlProduccionDTO.getId().getCodigoEstado());
				if(consultaControlProduccionDTO.getArticuloDTO()!=null){
					LogSISPE.getLog().info("consulta por articulo");
					beanSession.setCodigoArticulo(consultaControlProduccionDTO.getArticuloDTO().getId().getCodigoArticulo());
					beanSession.setDescripcionArticulo(consultaControlProduccionDTO.getArticuloDTO().getDescripcionArticulo());
					beanSession.setCodigoClasificacion(consultaControlProduccionDTO.getArticuloDTO().getCodigoClasificacion());
				}else{
					beanSession.setCodigoArticulo(null);
					beanSession.setDescripcionArticulo(null);
					beanSession.setCodigoClasificacion(null);
				}
				//se guarda las fechas inicial y final de entrega
				beanSession.setFechaInicial(consultaControlProduccionDTO.getNpPrimeraFechaDespachoInicial());
				beanSession.setFechaFinal(consultaControlProduccionDTO.getNpPrimeraFechaDespachoFinal());

				//se blanquea la colecci\u00F3n de datos antes de actualizar la lista
				formulario.setDatos(null);

				try{
					//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
					pedidosPorProducir = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaControlProduccion(consultaControlProduccionDTO);
					session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,pedidosPorProducir);

					//se realiza la paginaci\u00F3n si la colecci\u00F3n tiene elementos
					if(pedidosPorProducir!=null && !pedidosPorProducir.isEmpty()){
						LogSISPE.getLog().info("SE REALIZA LA PAGINACION");
						//llamada al m\u00E9todo que realiza la paginaci\u00F3n
						Collection datosPagina = formulario.paginarDatos(pedidosPorProducir, 0,pedidosPorProducir.size(),false);
						formulario.setDatos(datosPagina);

						beanSession.setInicioPaginacion(String.valueOf("0"));
						//llamada a la funci\u00F3n que inicializa los datos de ordenamiento
						this.inicializarDatosOrdenamientoPedidos(request);

						//SE VERIFICA SI LOS PEDIDOS DEBEN MOSTRARSE CON ALERTA
						Integer numeroDias = (Integer)session.getAttribute(DIAS_RESTAR_FECHA_MINIMA_DESPACHO);
						if(numeroDias != null){
							//se iteran los pedidos para asignar la propiedad "pedidoPorEntregar"
							for(VistaPedidoDTO vistaPedidoDTO : pedidosPorProducir){
								if(vistaPedidoDTO.getPrimeraFechaDespacho() != null){
									Timestamp fechaReferencia = ManejoFechas.sumarDiasTimestamp(vistaPedidoDTO.getPrimeraFechaDespacho(), (-1)*numeroDias.longValue());
									if(fechaActual.getTime() >= fechaReferencia.getTime()){
										vistaPedidoDTO.setPedidoPorEntregar(estadoActivo);
									}
								}
							}
						}
						
					}else{
						infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos con producci\u00F3n pendiente"));
					}
				}catch(SISPEException ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
				}

				//se elimina la lista de art\u00EDculos despues de cada b\u00FAsqueda
				session.removeAttribute(COLECCION_ARTICULOS);
				//beanSession.setCodigoEstadoAnterior(null);
				formulario.setOpEscogerProducir(null);
			}
			//------------------- ordenar la colecci\u00F3n de pedidos ---------------------
			else if(request.getParameter("indiceOrdenarP")!=null){

				LogSISPE.getLog().info("Ordenar la tabla de pedidos");
				int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenarP"));

				Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				if(colVistaPedidoDTO != null){
					//se llama a la funci\u00F3n que realiza el ordenamiento
					colVistaPedidoDTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, colVistaPedidoDTO, null);
	
					//se incializa el indice de la paginaci\u00F3n actual para los pedidos
					int indicePaginacion = 0;
					if(beanSession.getInicioPaginacion()!=null)
						indicePaginacion = Integer.parseInt(beanSession.getInicioPaginacion());
	
					LogSISPE.getLog().info("indice paginacion: {}",indicePaginacion);
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(colVistaPedidoDTO, indicePaginacion, colVistaPedidoDTO.size(),false);
					formulario.setDatos(datosPagina);
	
					//se realiza el respaldo de los datos de ordenamiento
					session.setAttribute(DATOS_TABLA_ORDENAR_PEDIDOS, session.getAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR));
					session.setAttribute(DATOS_COLUMNA_ORDENADA_PEDIDOS, session.getAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA));
				}
				//se actualiza la colecci\u00F3n en sesi\u00F3n
				session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
			}
			//------------------- ordenar la colecci\u00F3n de art\u00EDculos ---------------------
			else if(request.getParameter("indiceOrdenarA")!=null){

				LogSISPE.getLog().info("Ordenar la tabla de art\u00EDculos");
				int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenarA"));

				Collection colVistaArticuloTO = (Collection)session.getAttribute(COLECCION_ARTICULOS);
				if(colVistaArticuloTO != null){
					//se llama a la funci\u00F3n que realiza el ordenamiento
					colVistaArticuloTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, colVistaArticuloTO, null);

					//se incializa el indice de la paginaci\u00F3n actual para los art\u00EDculos
					int indicePaginacion = 0;
					if(session.getAttribute(INICIO_PAGINACION_ARTICULOS)!=null)
						indicePaginacion = Integer.parseInt((String)session.getAttribute(INICIO_PAGINACION_ARTICULOS));
	
					LogSISPE.getLog().info("indice paginacion: {}",indicePaginacion);
					//llamada al m\u00E9todo que realiza la paginaci\u00F3n
					Collection datosPagina = formulario.paginarDatos(colVistaArticuloTO, indicePaginacion,colVistaArticuloTO.size(),false);
					formulario.setDatos(datosPagina);
	
					//se realiza el respaldo de los datos de ordenamiento
					session.setAttribute(DATOS_TABLA_ORDENAR_ARTICULOS, session.getAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR));
					session.setAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS, session.getAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA));
				}
				
				//se actualiza la colecci\u00F3n en sesi\u00F3n
				session.setAttribute(COLECCION_ARTICULOS, colVistaArticuloTO);
			}
			/*-------------------------------------- si se escogi\u00F3 el boton Producir ------------------------------------*/
			else if(formulario.getBotonProducir()!=null)
			{
				//se obtiene de sesi\u00F3n los pedidos en el control de producci\u00F3n
				ArrayList colVistaPedidoDTO = (ArrayList)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);

				//colecci\u00F3n donde se almacenan los pedidos seleccionados
				Collection <VistaPedidoDTO> seleccionados = new ArrayList <VistaPedidoDTO>();
				//se obtiene el vector de chekBox
				String [] checks = formulario.getOpEscogerProducir();
				if(checks!=null && checks.length >0){
					//se obtiene el is del usuario activo en el sistema
					String idUsuario = SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario();
					//se recorren los checks
					for(int i=0;i<checks.length;i++){
						VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)colVistaPedidoDTO.get(Integer.parseInt(checks[i]));
						vistaPedidoDTO.setUserId(idUsuario);
						//se llama a la funci\u00F3n que carga los descuentos del pedido
						//esto es necesario para el correcto registro de los descuentos en caso de que existan
						WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO,request,Boolean.FALSE);
						//se a\u00F1aden los pedidos a la colecci\u00F3n
						seleccionados.add(vistaPedidoDTO);
					}

					try
					{
						//se registra el pedido en Producci\u00F3n
						SessionManagerSISPE.getServicioClienteServicio().transRegistrarEnProduccion(seleccionados);
						//se genera el mensaje de exito
						success.add("exitoProduccion",new ActionMessage("message.exito.enviarProduccion"));
					}
					catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("errorRegistrar",new ActionMessage("errors.llamadaServicio.registrarDatos","la Producci\u00F3n"));
						errors.add("exceptionSISPE",new ActionMessage("errors.SISPEException",ex.getMessage()));
					}

					//---------------- Se actualiza la lista de los pedidos ---------------------
					//creacion de la vista del Control de Producci\u00F3n
					VistaPedidoDTO consultaControlProduccion = new VistaPedidoDTO();
					consultaControlProduccion.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					consultaControlProduccion.getId().setCodigoAreaTrabajo(beanSession.getCodigoLocal());
					consultaControlProduccion.getId().setCodigoPedido(beanSession.getCodigoPedido());
					consultaControlProduccion.getId().setCodigoEstado(beanSession.getCodigoEstado());
					
					//se obtienen los estados que tengan una producci\u00F3n pendiente: produccionPendiente [SI]
					String afirmacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion");
					consultaControlProduccion.setProduccionPendiente(afirmacion);
					consultaControlProduccion.setEstadoActual(afirmacion);
					//consultaControlProduccion.setEtapaEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.finalizado"));
					consultaControlProduccion.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
					//campos de b\u00FAsqueda
					consultaControlProduccion.setLlaveContratoPOS(beanSession.getCodigoReservacion());
					consultaControlProduccion.setCedulaContacto(beanSession.getCedulaContacto());
					consultaControlProduccion.setNombreContacto(beanSession.getNombreContacto());
					consultaControlProduccion.setNombreEmpresa(beanSession.getNombreEmpresa());
					consultaControlProduccion.setRucEmpresa(beanSession.getRucEmpresa());
					consultaControlProduccion.setArticuloDTO(new ArticuloDTO());
					//entidad responsable
					if(beanSession.getOpEntidadResonsable()!=null){
						consultaControlProduccion.setEntidadResponsable(beanSession.getOpEntidadResonsable());
					}
					//c\u00F3digo del art\u00EDculo
					if(beanSession.getCodigoArticulo()!=null){
						consultaControlProduccion.getArticuloDTO().getId().setCodigoArticulo(beanSession.getCodigoArticulo());
					}
					//descripci\u00F3n del art\u00EDculo
					if(beanSession.getDescripcionArticulo()!=null){
						consultaControlProduccion.getArticuloDTO().setDescripcionArticulo(beanSession.getDescripcionArticulo());
					}
					//c\u00F3digo de la clasificaci\u00F3n
					if(beanSession.getCodigoClasificacion()!=null){
						consultaControlProduccion.getArticuloDTO().setDescripcionArticulo(beanSession.getDescripcionArticulo());
					}

					consultaControlProduccion.setNpPrimeraFechaDespachoInicial(beanSession.getFechaInicial());
					consultaControlProduccion.setNpPrimeraFechaDespachoFinal(beanSession.getFechaFinal());

					//se obtienen los estados que tengan una producci\u00F3n pendiente: produccionPendiente [SI]
					consultaControlProduccion.setProduccionPendiente(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

					//llamada al m\u00E9todo del servicio
					colVistaPedidoDTO = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaControlProduccion(consultaControlProduccion);
					//se limpia la colecci\u00F3n de datos antes de verificar la paginaci\u00F3n
					formulario.setDatos(null);
					if(colVistaPedidoDTO!=null && !colVistaPedidoDTO.isEmpty()){
						LogSISPE.getLog().info("ENTRO A LA PAGINACION");
						//llamada al m\u00E9todo que realiza la paginaci\u00F3n
						Collection datosPagina = formulario.paginarDatos(colVistaPedidoDTO, Integer.parseInt(beanSession.getInicioPaginacion()),colVistaPedidoDTO.size(),false);
						formulario.setDatos(datosPagina);
						//llamada a la funci\u00F3n que inicializa los datos de ordenamiento
						this.inicializarDatosOrdenamientoPedidos(request);
					}
					//se guarda en sesi\u00F3n la nueva colecci\u00F3n de pedidos
					session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
					//se eliminan la colecci\u00F3n de sesi\u00F3n donde estaban los art\u00EDculos
					session.removeAttribute(COLECCION_ARTICULOS);
					//beanSession.setCodigoEstadoAnterior(null);
					formulario.setOpEscogerProducir(null);
				}else{
					//mensaje informativo
					infos.add("PedidosSeleccionados",new ActionMessage("message.seleccionPedidos.Produccion"));
				}
			}
			else if(request.getParameter("crearReporte")!=null)
			{
				LogSISPE.getLog().info("entra a crear reporte");
				//se obtiene la colecci\u00F3n de art\u00EDculos y se verifica si tiene datos
				VistaArticuloDTO  vistaArticuloDTO = (VistaArticuloDTO)session.getAttribute(VISTA_ARTICULO_REPORTE);
				if(vistaArticuloDTO == null){
					errors.add("errorReporteProduccion",new ActionMessage("errors.exportarDatos.sinDatos", "xls"));
				}
				else
				{	
					LogSISPE.getLog().info("entra a crear reporte");
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
					formulario.setOpTipoAgrupacion((String)session.getAttribute(REPORTE_1));
					//se guarda
					request.setAttribute(SessionManagerSISPE.POPUP, popUp);

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
					salida="rptVistaArticulo";
				}
				LogSISPE.getLog().info("peticion: {}",peticion);
			}

			//caso por omisi\u00F3n--------------------------------------------------
			else
			{
				//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
				SessionManagerSISPE.removeVarSession(request);
				//se blanquea la colecci\u00F3n del formulario que contiene la paginaci\u00F3n
				formulario.setDatos(null);
				formulario.setEtiquetaFechas("Fecha de Despacho");
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
				//Obtengo valor por defecto entidad Responsable.
				String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
				formulario.setOpEntidadResponsable(entidadBodega);
				formulario.setOpEstadoActivo(estadoActivo);

				session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de despacho");

				//se obtiene el par\u00E1metro que indica cuantos d\u00EDas se deben restar a la fecha de despacho
				//para alertar que un pedido debe ser producido
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diazARestarAFechaDespachoAvisoProduccion", request);
				if(parametroDTO.getValorParametro()!=null){
					session.setAttribute(DIAS_RESTAR_FECHA_MINIMA_DESPACHO, Integer.parseInt(parametroDTO.getValorParametro()));
				}
				
				//se verifica si la entidad responsable es un local
				String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
				if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
					//se obtienen los locales por ciudad
					SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

				//se realiza la consulta de estados
				EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO();
				consultaEstadoDTO.getId().setCodigoEstado(estadoReservado+","+estadoEnProduccion);
				consultaEstadoDTO.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.estadoNormal"));
				//Obtener datos de la colecci\u00F3n de estados, en la base de datos
				Collection estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
				//guardar en sesi\u00F3n esta colecci\u00F3n
				session.setAttribute(SessionManagerSISPE.COL_ESTADOS, estados);

				//Iniciar Tabs
				PaginaTab tabsControlProduccion = new PaginaTab("controlProduccion", "controlProduccion", 0, 470, request);
				Tab tabPedidos = new Tab("Pedidos", "controlProduccion", "/servicioCliente/controlProduccion/listaPedidosConProduccion.jsp", true);
				Tab tabArticulos = new Tab("Art\u00EDculos", "controlProduccion", "/servicioCliente/controlProduccion/listaArticulosConProduccion.jsp", false);
				//se agregan los tabs
				tabsControlProduccion.addTab(tabPedidos);
				tabsControlProduccion.addTab(tabArticulos);

				beanSession.setPaginaTab(tabsControlProduccion);

				//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
				session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Control de producci\u00F3n");
				//se obtiene el estado para los pedidos reservado, en producci\u00F3n y anulado
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoReservado",estadoReservado);
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoEnProduccion",estadoEnProduccion);
				//session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoAnulado",estadoAnulado);

				//se guarda la acci\u00F3n en la sesi\u00F3n
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, 
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion"));

				//subo a sesi\u00F3n las variables de los tipos de reporte
				session.setAttribute(REPORTE_1,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.fechaArticuloLocal"));
				session.setAttribute(REPORTE_2,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.articuloFechaLocal"));
				session.setAttribute(REPORTE_3,
						MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.LocalFechaArticulo"));
				session.setAttribute(REPORTE_4,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.vistaArticulo.tipoReporte.LocalArticuloFecha"));
			}
			
		}catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}

		//se guarda la nueva configuraci\u00F3n
		SessionManagerSISPE.setBeanSession(beanSession, request);

		//se guardan los mensajes generados
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);

		//finaliza con
		return mapping.findForward(salida);
	}

	/**
	 * Inicializa los datos para el ordenamiento de la colecci\u00F3n de pedidos
	 * @param request
	 */
	private void inicializarDatosOrdenamientoPedidos(HttpServletRequest request){

		HttpSession session = request.getSession();

		//se incializan los datos de la tabla
		String[][] datosTabla = {
				{"id.codigoPedido", "No de pedido", null},
				{"llaveContratoPOS", "No de reservaci\u00F3n", null},
//				{"nombreContacto", "Nombre del Cliente", null},
				{"contactoEmpresa", "Nombre del cliente", null},
				{"cantidadReservadaEstado", "Cantidad reservada", null},
				{"cantidadParcialEstado", "Cantidad producida", null},
				{"diferenciaCantidadEstado", "Cantidad pendiente", null},
				{"porcentajeProducido", "Porcentaje producido", null},
				{"descripcionEstado", "Estado", null},
				{"primeraFechaDespacho", "Fecha de despacho", null}
		};
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
	private void inicializarDatosOrdenamientoArticulos(HttpServletRequest request){

		HttpSession session = request.getSession();

		//se incializan los datos de la tabla
		String[][] datosTabla = {
				{"id.codigoArticulo", "C\u00F3digo barras", null},
				{"descripcionArticulo", "Descripci\u00F3n art\u00EDculo", null},
				{"cantidadReservadaEstado", "Cantidad reservada", null},
				{"cantidadParcialEstado", "Cantidad producida", null},
				{"diferenciaCantidadEstado", "Cantidad pendiente", null},
				{"porcentajeProducido", "Porcentaje producido", null},
				{"unidadManejo", "Unidad de manejo", null},
				{"fechaDespachoBodega", "Fecha de despacho", null}
		};
		//se guardan los datos en sesi\u00F3n
		session.setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
		//se inicializa los datos de la culumna ordenada 
		session.setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {"C\u00F3digo barras", "Ascendente"});

		//se inicializan las variables de respaldo para los art\u00EDculos
		session.setAttribute(DATOS_TABLA_ORDENAR_ARTICULOS, datosTabla);
		session.setAttribute(DATOS_COLUMNA_ORDENADA_ARTICULOS, new String [] {"C\u00F3digo barras", "Ascendente"});
	}

}