/*
 * Clase DetalleCanastaAction.java
 * Creado el 04/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CHECK_PAGO_EFECTIVO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.common.articulo.TipoCatalogoArticulo;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.CaracteristicaDinamicaDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.sispe.common.util.AutorizacionesUtil;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.BuscarArticuloUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.DescuentosUtil;
import ec.com.smx.sic.sispe.common.util.EntregaLocalCalendarioUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaRecetaArticuloDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite el manejo de los art\u00EDculos incluidos en una canasta.
 * Maneja un conjunto de tags que muestran diversas secciones del formulario:
 * <ol>
 * 	<li>DETALLE DE LA CANASTA: Muestra el detalle de los art\u00EDculos incluidos en la canasta y el monto total.
 * 		En esta secci\u00F3n tambi\u00E9n se puede actualizar el listado de art\u00EDculos si se realiza alg\u00FAn cambio en 
 * 		las cantidades.</li>
 * 	<li>BUSQUEDA DE ARTICULOS: Muestra una secci\u00F3n donde se puede realizar la b\u00FAsqueda de art\u00EDculos, 
 * 		ya sea por c\u00F3digo de clasificaci\u00F3n, por descripci\u00F3n de la clasificaci\u00F3n o por nombre del art\u00EDculo.</li>
 * </ol>
 * </p>
 * @author 	fmunoz
 * @version	2.0
 * @since		JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class DetalleCanastaAction extends BaseAction
{
	public final static String CANT_MAX_ITEM_RECETA = "ec.com.smx.sic.sispe.parametro.cantidadMaximaPorItemReceta";
	public final static String RECETA_SELECCIONADA = "ec.com.smx.sic.sispe.receta.detallePedidoDTO";
	public final static String DETALLE_SIN_ACTUALIZAR = "ec.com.smx.sic.sispe.canasta.detalleSinActualizar";

	public static final String PORC_MODIFICACION_RECETA = "ec.com.smx.sic.sispe.parametro.porcentajeModificacionItemsCanasto";
	public static final String COL_CODARTICULOS_DETALLE_RECETA = "ec.com.smx.sic.sispe.detalleCanasta.codigosArticulos";
	public static final String ITEMS_A_ELIMINAR_DE_ELIMINADOS = "ec.com.smx.sic.sispe.receta.itemsAEliminarDeEliminados";

	//public static final String COL_DETALLE_RECETA = "ec.com.smx.sic.sispe.pedido.detalleCanasta";
	public static final String TOTAL_RECETA = "ec.com.smx.sic.sispe.pedido.totalCanasta";
	private static final String ITEMS_A_ELIMINAR_DE_AGREGADOS = "ec.com.smx.sic.sispe.receta.itemsAEliminarDeAgregados";
	private static final String DETALLE_PEDIDODTO_ORIGINAL = "ec.com.smx.sic.sispe.receta.detallePedidoDTOOriginal";
	private static final String COL_AGREGADOS_RECETA_ORIGINAL = "ec.com.smx.sic.sispe.receta.itemsAgregadosRecetaOriginal";
	private static final String COL_ELIMINADOS_RECETA_ORIGINAL = "ec.com.smx.sic.sispe.receta.itemsEliminadosRecetaOriginal";
	//private static final String ARTICULODTO_ORIGINAL = "ec.com.smx.sic.sispe.receta.articuloDTOOriginal";
	private static final String COL_RECETA_ART_ORIGINAL = "ec.com.smx.sic.sispe.receta.recetaArticuloDTOColOriginal";
	public static final String COL_CANT_ORIGINALES_RECETA = "ec.com.smx.sic.sispe.pedido.canasta.cantidadesOriginales";
	public static final String TOTAL_RECETA_A_SUMAR = "ec.com.smx.sic.sispe.articulosAnadidos.totalCanasta";
	public static final String CODIGOS_CLASIFICACIONES_IMPLEMENTOS = "ec.com.smx.sic.sispe.clasificacionesImplementos";
	
	public static final String TOTAL_RECETA_SIN_IVA = "ec.com.smx.sic.sispe.pedido.totalCanastaSinIva";
	public static final String CANASTA_EXISTENTE = "ec.com.smx.sic.sispe.pedido.canastaExistente";
	public static final String VERIFICAR_CANASTAESPECIAL = "ec.com.smx.sic.sispe.pedido.verificar.canastaespecial";
	public static final String VISTADETALLE_CANASTAESPECIAL = "ec.com.smx.sic.sispe.pedido.vista.canastaespecial";
	public static final String ARTICULO_CANASTAESPECIAL = "ec.com.smx.sic.sispe.pedido.articulo.canastaespecial";
	
	//boolean que indica si la canasta actual es de catalogo o no
	public static final String ES_CANASTO_DE_CATALOGO = "ec.com.smx.sic.sispe.es.canasto.catalogo";
	
	public static final String CAMBIOS_CANASTO= "ec.com.smx.sic.sispe.es.cambios.canasto";
	
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
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages warnings = new ActionMessages();
		ActionMessages errors = new ActionMessages();

		CotizarReservarForm formulario= (CotizarReservarForm)form;
		HttpSession session = request.getSession();
		String salida="detalleCanasta";

		//se obtienen las claves que indican un estado activo y un estado inactivo
		String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		String estadoInactivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_INACTIVO);
		
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//se obtiene el c\u00F3digo de la receta vacia
		//String codigoRecetaVacia = SessionManagerSISPE.getCodigoCanastoVacio(request);
		try
		{
			/*---- cuando se desea ver el detalle de una canasta ----*/
			if(request.getParameter("indiceCanasta")!=null)
			{
				//colecci\u00F3n sobre la que se realizar\u00E1n todas las manipulaciones de la receta.
				Collection detalleCanasta = null;
				//colecci\u00F3n que almacenar\u00E1 los c\u00F3digos de los art\u00EDculos incluidos en la canasta
				Collection <String> codigosArticulos = new ArrayList <String>();
				//colecci\u00F3n que almacenar\u00E1 las cantidades iniciales del canasto
				Collection <Long> cantidadesOriginales = new ArrayList <Long>();

				//se inicializan algunos campos
				formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
				formulario.setCheckSeleccionarTodo(null);
				formulario.setChecksSeleccionar(null);
				formulario.setTextoBusqueda(null);
				formulario.setCodigoArticulo(null);
				formulario.setCantidadArticulo(null);
				
				int indice=0;
				try
				{
					//se obtiene el indice del detalle de la cotizaci\u00F3n seleccionado
					indice = Integer.parseInt(request.getParameter("indiceCanasta"));
					
					
					//se obtiene de sesi\u00F3n la colecci\u00F3n del detalle principal
					ArrayList detalle = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					//se obtiene el objeto detalle
					DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)detalle.get(indice);
					//se guarda en sesi\u00F3n el indice del art\u00EDculo seleccionado
					beanSession.setIndiceRegistro(String.valueOf(indice));
					//se obtiene la cantidad del canasto del detalle principal
					long cantidadCanasto = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					LogSISPE.getLog().info("cantidadCanasto: {}",cantidadCanasto);
					
					//se sube a sesion la bandera que indica si el canasto actual de catalogo
					if(detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))
							&& detallePedidoDTO.getArticuloDTO().getNpNuevoCodigoClasificacion() == null){
						session.setAttribute(ES_CANASTO_DE_CATALOGO, true);
					}else{
						session.removeAttribute(ES_CANASTO_DE_CATALOGO);
					}
					
					detalleCanasta = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
					if(detalleCanasta == null){
						detalleCanasta = new ArrayList<ArticuloRelacionDTO>();
						detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(detalleCanasta);
					}
					
					if(detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol().isEmpty()){
						if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoInactivo))
							infos.add("listaVacia",new ActionMessage("message.listaVacia","Art\u00EDculos en esta canasta"));
					}else{
						LogSISPE.getLog().info("se obtubo desde la sesi\u00F3n");
						LogSISPE.getLog().info("numero de items: {}",detalleCanasta.size());
						
						//se recorre la la colecci\u00F3n del detalle de la cotizaci\u00F3n
						for(Iterator<ArticuloRelacionDTO>it = detalleCanasta.iterator(); it.hasNext();){
							ArticuloRelacionDTO recetaArticulo = it.next();
							//se a\u00F1aden tambi\u00E9n los c\u00F3digos de los art\u00EDculos
							codigosArticulos.add(recetaArticulo.getArticuloRelacion().getId().getCodigoArticulo());
							cantidadesOriginales.add(Long.parseLong(recetaArticulo.getCantidad().toString()));
							//control de stock para el detalle
							if(recetaArticulo.getArticuloRelacion().getNpStockArticulo()!=null
									&& recetaArticulo.getArticuloRelacion().getNpImplemento().equals(estadoInactivo)){
								long cantidadItem = recetaArticulo.getCantidad().longValue();
								long stockArticulo = recetaArticulo.getArticuloRelacion().getNpStockArticulo().longValue();
								long totalCantidad = cantidadItem * cantidadCanasto;
								if(totalCantidad<=stockArticulo)
									recetaArticulo.getArticuloRelacion().setNpEstadoStockArticulo(estadoActivo);
								else
									recetaArticulo.getArticuloRelacion().setNpEstadoStockArticulo(estadoInactivo);
							}
						}
					}
					
					//guardo en sesion los subtotales que me van a servir para las actualizaciones posteriores.
					session.setAttribute(TOTAL_RECETA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado());
					session.setAttribute(TOTAL_RECETA_SIN_IVA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado());
					//se inicializa el precio total del canasto, para una posterior comparaci\u00F3n
					beanSession.setPrecioCanastoInicial(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado());

					//se inicializa el detalle de la canasta y los c\u00F3digos.
					//session.setAttribute(COL_DETALLE_RECETA, detalleCanasta);
					session.setAttribute(COL_CANT_ORIGINALES_RECETA, cantidadesOriginales);
					session.setAttribute(COL_CODARTICULOS_DETALLE_RECETA, codigosArticulos);
					//se elimina la variable que controla la b\u00FAsqueda en pedido principal
					session.removeAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL);
					//se inicializa la opci\u00F3n de b\u00FAsqueda
					formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));

					//se inicializan las colecciones de los items agregados y eliminados
					if(detallePedidoDTO.getItemsAgregadosReceta()==null)
						detallePedidoDTO.setItemsAgregadosReceta(new ArrayList<String>()); //solo la primera vez se crea
					if(detallePedidoDTO.getItemsEliminadosReceta()==null)
						detallePedidoDTO.setItemsEliminadosReceta(new ArrayList<String>()); //solo la primera vez se crea

					LogSISPE.getLog().info("items agregados: {} items eliminados: {}",detallePedidoDTO.getItemsAgregadosReceta().size(),detallePedidoDTO.getItemsEliminadosReceta().size());

					//se verifica el par\u00E1metro para el m\u00E1ximo de modificaciones
					String porcentajeModificacionItemsCanasto = (String)session.getAttribute(PORC_MODIFICACION_RECETA);
					if(porcentajeModificacionItemsCanasto==null){
						//se obtiene el par\u00E1metro para calcular el m\u00E1ximo de modificaciones
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion(PORC_MODIFICACION_RECETA, request);
						if(parametroDTO!=null){
							porcentajeModificacionItemsCanasto = parametroDTO.getValorParametro();
							session.setAttribute(PORC_MODIFICACION_RECETA, porcentajeModificacionItemsCanasto);
						}
					}

					//se verifica el par\u00E1metro para la cantidad m\u00E1xima por item
					String cantidadMaximaPorItem = (String)session.getAttribute(CANT_MAX_ITEM_RECETA);
					if(cantidadMaximaPorItem==null){
						//se obtiene el par\u00E1metro para calcular el m\u00E1ximo de modificaciones
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion(CANT_MAX_ITEM_RECETA, request);
						if(parametroDTO!=null){
							cantidadMaximaPorItem = parametroDTO.getValorParametro();
							session.setAttribute(CANT_MAX_ITEM_RECETA, cantidadMaximaPorItem);
						}
					}

					//se verifica el valor para el m\u00E1ximo de modificaciones, solo se realiza la primera vez
					if(detallePedidoDTO.getNpCanMaxModIteRec()==null){
						double maximoModificaciones = Math.ceil(detalleCanasta.size() * (Double.parseDouble(porcentajeModificacionItemsCanasto)/100));
						//se guarda la cantidad maxima de modificaciones
						detallePedidoDTO.setNpCanMaxModIteRec(Double.valueOf(maximoModificaciones).intValue());
					}
					LogSISPE.getLog().info("maximo numero de items a modificar: {}",detallePedidoDTO.getNpCanMaxModIteRec());

					//se realiza una clonaci\u00F3n de los objetos principales
					session.setAttribute(DETALLE_PEDIDODTO_ORIGINAL, detallePedidoDTO.clone());
					session.setAttribute(COL_AGREGADOS_RECETA_ORIGINAL, detallePedidoDTO.getItemsAgregadosReceta().clone());
					session.setAttribute(COL_ELIMINADOS_RECETA_ORIGINAL, detallePedidoDTO.getItemsEliminadosReceta().clone());
					//session.setAttribute(ARTICULODTO_ORIGINAL, detallePedidoDTO.getArticuloDTO().clone());
					
					Collection <ArticuloRelacionDTO> recetaOriginal = new ArrayList<ArticuloRelacionDTO>();
					//se realiza la clonaci\u00F3n de cada item
					for(Iterator <ArticuloRelacionDTO> it = detalleCanasta.iterator(); it.hasNext(); ){
						ArticuloRelacionDTO recetaArticuloDTO = it.next();
						recetaOriginal.add(recetaArticuloDTO);
					}
					session.setAttribute(COL_RECETA_ART_ORIGINAL, recetaOriginal);

					//se guarda la receta original cuando se ingres\u00F3 por primera vez al detalle
					if(detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal()==null){
						LogSISPE.getLog().info("LA PROPIEDAD RECETA ORIGINAL ESTA VACIA");
						detallePedidoDTO.getArticuloDTO().setRecetaArticulosOriginal(recetaOriginal);
						LogSISPE.getLog().info("Receta original asignada, tama\u00F1o: {}",detallePedidoDTO.getArticuloDTO().getRecetaArticulosOriginal().size());
					}

					//se guarda en sesi\u00F3n el objeto DetallePedidoDTO
					session.setAttribute(RECETA_SELECCIONADA, detallePedidoDTO);
					
					//se obtiene el par\u00E1metro que indica las clasificaciones de los implementos
					String clasificacionesImplementos = (String)session.getAttribute(CODIGOS_CLASIFICACIONES_IMPLEMENTOS);
					if(clasificacionesImplementos == null){
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigosClasificacionesImplementos", request);
						session.setAttribute(CODIGOS_CLASIFICACIONES_IMPLEMENTOS, parametroDTO.getValorParametro());
					}
					//CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
				}
				catch(NumberFormatException ex){
					errors.add("formatoIndice",new ActionMessage("errors.indiceDetalle.formato"));
					salida="cotizacion";
				}
				catch(IndexOutOfBoundsException ex){
					//si el indice del detalle de la cotizaci\u00F3n no se puede referenciar
					errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
					salida="cotizacion";
				}
				catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
					errors.add("agregarArticuloSIC", new ActionMessage("errors.agregarArticuloSIC"));
				}
			}
			/*----------------- cuando se desea ver el detalle de los art\u00EDculos de una canasta -----------------------*/
			else if(request.getParameter("actualizarCanasto")!=null)
			{
				LogSISPE.getLog().info("POR ACTUALIZAR");
				if(request.getAttribute("cantidadExedida")!=null){
					warnings.add("cantidadesExcedidas", new ActionMessage("warning.cantidadExcedida.itemReceta", session.getAttribute(CANT_MAX_ITEM_RECETA)));
				}
				//se obtiene la articulo receta que se est\u00E1 validando
				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA);
				
				//se actualiza el total del canasto
				this.actualizarTotalCanastaPorAdicionBusqueda(request);
				
				//llamada al m\u00E9todo que controla las diferencias del canasto actual en relaci\u00F3n al original
				if(CotizacionReservacionUtil.verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request)){
					detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
					session.setAttribute(CAMBIOS_CANASTO, "ok");
				}else{
					//control para que la canasta de cotizaciones no valide modificacion del 30% de la receta
					String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
					if(detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo()!= null && detallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo().equals(codigoCanCotVacio)){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
					}else if(detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().
							equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
					}else{
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoInactivo);
					}
				}
				
				calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
				
				formulario.setCheckSeleccionarTodo(null);
				formulario.setChecksSeleccionar(null);
			}
			/*----------------- cuando se desea a\u00F1adir un art\u00EDculo a la canasta por su c\u00F3digo ----------------------*/
			else if(request.getParameter("agregarArticulo")!=null)
			{
				Boolean recetaIgual = false;
				//se obtiene el c\u00F3digo ingresado del art\u00EDculo
				String codigoArticulo = formulario.getCodigoArticulo().trim();
				try{
					//se eliminan los ceros al inicio del c\u00F3digo
					codigoArticulo = Long.valueOf(formulario.getCodigoArticulo().trim()).toString();
				}catch (NumberFormatException e) {}
				
				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(RECETA_SELECCIONADA);
				String [] codigosArticulos = new String[1];
				codigosArticulos[0] = codigoArticulo;
				//primero se verifica si en verdad se puede agregar el art\u00EDculo
				if(validarAdicion(request, codigosArticulos, detallePedidoDTO)){
					//obtengo la colecci\u00F3n de detalles de la sesion
					//Collection detalleCanasta = (Collection)session.getAttribute(COL_DETALLE_RECETA);
					Collection detalleCanasta = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
					//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos de la canasta
					Collection codigosArticulosCanasta = (Collection)session.getAttribute(COL_CODARTICULOS_DETALLE_RECETA);
					//se obtienen las cantidades originales almacenadas en sesi\u00F3n
					Collection cantidadesOriginales = (Collection)session.getAttribute(COL_CANT_ORIGINALES_RECETA);
					//se obtiene de sesi\u00F3n la colecci\u00F3n del detalle principal
					ArrayList detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);

					long cantidadCanasto = ((DetallePedidoDTO)detallePedido.get(Integer.parseInt(beanSession.getIndiceRegistro())))
						.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
					LogSISPE.getLog().info("cantidad: {}",cantidadCanasto);

					//se obtiene los datos de la receta
					ArticuloDTO canasto = detallePedidoDTO.getArticuloDTO();
					String codigoArticuloPadre = canasto.getId().getCodigoArticulo();

					//llamada al m\u00E9todo que construye la consulta
					ArticuloDTO consultaArticuloDTO = new ArticuloDTO();
//					consultaArticuloDTO.getId().setCodigoArticulo(codigoArticulo);					
					consultaArticuloDTO.setNpCodigoBarras(codigoArticulo);
					//condici\u00F3n para que solo se obtengan items
//					consultaArticuloDTO.setCodigoTipoArticulo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item").concat(",")
//							.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.relacionado")));
					
					consultaArticuloDTO.addCriteriaSearchParameter("codigoClasificacion", ComparatorTypeEnum.NOT_IN_COMPARATOR, new String[]{MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"),MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial")});
					//condici\u00F3n para que no se obtengan perecibles
					//consultaArticuloDTO.setEstadoPerecibleReceta(estadoInactivo);
					WebSISPEUtil.construirConsultaArticulos(request, consultaArticuloDTO, estadoActivo, codigoArticuloPadre, accion);
					consultaArticuloDTO.setNpSecuencialEstadoPedido(null);
					try 
					{
						//llamada al m\u00E9todo de la capa de servicio que devuelve la colecci\u00F3n de art\u00EDculos
						ArticuloDTO articuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(consultaArticuloDTO);
						if(articuloDTO!=null){
							codigoArticulo = articuloDTO.getId().getCodigoArticulo();
						//verificar si el articulo es obsoleto
						if(!articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
							
							//Verificar que no sea un perecible y que no se pueda agregar en la receta
							CaracteristicaDinamicaDTO caracteristicaDinamicaDTO = new CaracteristicaDinamicaDTO();
							caracteristicaDinamicaDTO.setCodigoTipoCaracteristica(TipoCatalogoArticulo.CARACTERISTICA_NOPERMITIDOENRECETA);
							caracteristicaDinamicaDTO.setCodigoClasificacion(articuloDTO.getCodigoClasificacion());
							
							CaracteristicaDinamicaDTO carDinResDTO= SISPEFactory.getDataService().findUnique(caracteristicaDinamicaDTO);
							
							Boolean estadoPerecibleReceta=Boolean.FALSE;
							if(carDinResDTO != null){
								estadoPerecibleReceta=Boolean.TRUE;
							}
							
							if(!estadoPerecibleReceta)//articuloDTO.getEstadoPerecibleReceta().equals(estadoInactivo)) //solo si se obtubo un art\u00EDculo
							{
								//SE CONFIRMA PRIMERO SI EL ARTICULO YA ESTA EN EL LISTADO
								if(codigosArticulosCanasta.contains(codigoArticulo)){
									//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
									errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetidoCanasta",articuloDTO.getDescripcionArticulo()));
									LogSISPE.getLog().info("error: ya esta en la lista");
								}else if(articuloDTO.getNpEstadoArticuloSIC()!=null && 
										articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
									//cuando el art\u00EDculo esta de baja en el SIC
									errors.add("articuloDeBaja",new ActionMessage("errors.agregarArticuloDeBaja", codigoArticulo));
								}else{
									//solo si el art\u00EDculo no es implemento
									if(articuloDTO.getNpImplemento().equals(estadoInactivo)){
										//se calcula la cantidad
										long cantidadTotal = Long.parseLong(formulario.getCantidadArticulo()) * cantidadCanasto; 
										if(articuloDTO.getNpStockArticulo()!=null
												&& articuloDTO.getNpStockArticulo().longValue() < cantidadTotal){
											//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
											articuloDTO.setNpEstadoStockArticulo(estadoInactivo);
											request.setAttribute("ec.com.smx.sic.sispe.articulo.problemasStock","ok");
											LogSISPE.getLog().info("problemas de stock");
										}else if(articuloDTO.getNpAlcance()!=null && articuloDTO.getNpAlcance().equals(estadoInactivo)){
											request.setAttribute("ec.com.smx.sic.sispe.articulo.problemasAlcance","ok");
										}
										//se guarda el art\u00EDculo en el request
										request.setAttribute("ec.com.smx.sic.sispe.articuloDTO",articuloDTO);
									}
	
									//llamada al m\u00E9todo que genera el objeto RecetaArticuloDTO
									ArticuloRelacionDTO recetaArticuloDTO = CotizacionReservacionUtil.construirNuevaReceta(articuloDTO,new Long(formulario.getCantidadArticulo().trim()),request);
									//se a\u00F1ade el detalle a la colecci\u00F3n del canasto
									detalleCanasta.add(recetaArticuloDTO);
									LogSISPE.getLog().info("SE ANADIO A LA LISTA");
									session.setAttribute(CAMBIOS_CANASTO, "ok");
									//se actualiza la lista de codigos de art\u00EDculos del canasto
									codigosArticulosCanasta.add(codigoArticulo);
									//se actualiza la lista de cantidades originales
									cantidadesOriginales.add(new Long(formulario.getCantidadArticulo().trim()));
	
									//---------------- se actualizan los totales del pedido ---------------------
									LogSISPE.getLog().info("ACTUALIZANDO TOTAL DE LA CANASTA");
									
									double nuevoTotal = 0;
									//se obtiene el total de la canasta y se le suma el total del item agregado
									Double totalCanasto = (Double)session.getAttribute(TOTAL_RECETA);
									if(totalCanasto!=null)
										nuevoTotal = totalCanasto.doubleValue();
									
									//nuevoTotal = nuevoTotal + recetaArticuloDTO.getPrecioTotalIVA().doubleValue();
									nuevoTotal = nuevoTotal + recetaArticuloDTO.getArticulo().getPrecioBaseImp().doubleValue();
									
									//llamada al m\u00E9todo que controla las diferencias del canasto actual en relaci\u00F3n al original
									if(CotizacionReservacionUtil.verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request)){
										detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
									}else{
										detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoInactivo);
										nuevoTotal=detallePedidoDTO.getArticuloDTO().getPrecioBaseImp();
										recetaIgual = true;
									}
									
									//se actualiza el total de la canasta en sesi\u00F3n
									session.setAttribute(TOTAL_RECETA, Double.valueOf(nuevoTotal));
									
									formulario.setCodigoArticulo(null);
									formulario.setCantidadArticulo(null);
	
									//colecci\u00F3n de items eliminados de la colecci\u00F3n "detallePedidoDTO.getItemsEliminadosReceta()"
									Collection <String> itemsAEliminarDeEliminados = (Collection <String>)session.getAttribute(ITEMS_A_ELIMINAR_DE_ELIMINADOS);
									if(itemsAEliminarDeEliminados!=null && !itemsAEliminarDeEliminados.contains(codigoArticulo))
										detallePedidoDTO.getItemsAgregadosReceta().add(codigoArticulo);
								}
								
								session.setAttribute(COL_CODARTICULOS_DETALLE_RECETA,codigosArticulosCanasta);
								//session.setAttribute(COL_DETALLE_RECETA,detalleCanasta);
							}else{
								infos.add("articulosVacio",new ActionMessage("message.codigoBarras.invalido.receta"));
							}
						}else{
							infos.add("articulosObsoletos",new ActionMessage("message.codigoBarras.invalido.articulo.obsoleto"));
						}
					}else{
							infos.add("articulosObsoletos",new ActionMessage("message.codigoBarras.invalido"));
						}
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
						errors.add("agregarArticuloSIC", new ActionMessage("errors.agregarArticuloSIC"));
					}
					
				}else{
					//se verifica el par\u00E1metro para el m\u00E1ximo de modificaciones
					String porcentajeModificacionItemsCanasto = (String)session.getAttribute(PORC_MODIFICACION_RECETA);
					//se genera un mensaje de error
					errors.add("itemNoAgregado", new ActionMessage("info.canasto.modificacionArticulos", 
							Double.parseDouble(porcentajeModificacionItemsCanasto) + "%", detallePedidoDTO.getNpCanMaxModIteRec()));
				}
								
				if(recetaIgual){
					session.setAttribute(TOTAL_RECETA_SIN_IVA, Double.valueOf(detallePedidoDTO.getArticuloDTO().getPrecioBase()));
					
					if(CotizacionReservacionUtil.verificarCanastoExistente(detallePedidoDTO, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))
							|| CotizacionReservacionUtil.verificarCanastoExistente(detallePedidoDTO, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						//si el canasto es igual al canasto de catalogo inicial se debe validar como tal.
						detallePedidoDTO.getArticuloDTO().setCodigoArticuloOriginal(null);
						infos.add("canastoExiste",new ActionMessage("errors.canastaExiste",session.getAttribute(DetalleCanastaAction.CANASTA_EXISTENTE)));
					}
				}else{
					calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
				}
			}
			/*----------------------- cuando se desea eliminar items del canasto --------------------------*/
			else if(request.getParameter("eliminarArticulos")!=null)
			{
				//se obtienen los indices de los art\u00EDculos a eliminar
				String [] indicesRegistros = formulario.getChecksSeleccionar();
				LogSISPE.getLog().info("por eliminar detalle canasta");

				if(indicesRegistros!=null){
					//obtengo los atributos de los totales del formulario
					Double totalCanasto=(Double)session.getAttribute(TOTAL_RECETA);
					//se obtiene el objeto DetallePedidoDTO
					DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(RECETA_SELECCIONADA);
					//obtengo la colecci\u00F3n de detalles de la sesion
					//ArrayList detalleCanasta = (ArrayList)session.getAttribute(COL_DETALLE_RECETA);
					List detalleCanasta = (List)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
					//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos de la canasta
					List codigosArticulosCanasta = (List)session.getAttribute(COL_CODARTICULOS_DETALLE_RECETA);
					//se obtiene la colecci\u00F3n de las cantidades originales del canasto
					List cantidadesOriginales = (List)session.getAttribute(COL_CANT_ORIGINALES_RECETA);
					
					//se inicializa el nuevo total del pedido
					double nuevoTotalCanasto = totalCanasto.doubleValue();

					int indiceRegistro=0;
					LogSISPE.getLog().info("# registros: {}",indicesRegistros.length);

					//se realiza la validaci\u00F3n de la eliminaci\u00F3n de los items
					if(validarEliminacion(request, indicesRegistros, detallePedidoDTO)){
						detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(estadoActivo);
						//se recorre el arreglo de indices
						for(int i=0;i<indicesRegistros.length;i++){
	
							indiceRegistro = Integer.parseInt(indicesRegistros[i]);
							LogSISPE.getLog().info("indice registro: {}",indiceRegistro);
							//Obtengo el detalle que voy a eliminar
							ArticuloRelacionDTO recetaArticuloDTO = (ArticuloRelacionDTO)detalleCanasta.get(indiceRegistro);
							//nuevoTotalCanasto = nuevoTotalCanasto - recetaArticuloDTO.getPrecioTotalIVA().doubleValue();
							nuevoTotalCanasto = nuevoTotalCanasto - recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp().doubleValue();

							Collection <String> itemsAEliminarDeAgregados = (Collection <String>)session.getAttribute(ITEMS_A_ELIMINAR_DE_AGREGADOS);
							//solo si el c\u00F3digo del art\u00EDculo no est\u00E1 en esta colecci\u00F3n se agrega a la colecci\u00F3n de eliminados
							if((itemsAEliminarDeAgregados!=null && !itemsAEliminarDeAgregados.contains(recetaArticuloDTO.getArticuloRelacion().getId().getCodigoArticulo()))
									|| (itemsAEliminarDeAgregados == null 
										&& !detallePedidoDTO.getItemsEliminadosReceta().contains(recetaArticuloDTO.getId().getCodigoArticuloRelacionado()))){
								//se agrega el c\u00F3digo del art\u00EDculo a la colecci\u00F3n de eliminados
//								detallePedidoDTO.getItemsEliminadosReceta().add(recetaArticuloDTO.getArticuloRelacion().getId().getCodigoArticulo());
								detallePedidoDTO.getItemsEliminadosReceta().add(recetaArticuloDTO.getId().getCodigoArticuloRelacionado());
							}
						}

						//se elimina el primer registro
						indiceRegistro = Integer.parseInt(indicesRegistros[0]);
						detalleCanasta.remove(indiceRegistro);
						codigosArticulosCanasta.remove(indiceRegistro);
						cantidadesOriginales.remove(indiceRegistro);
						int contEliminados=1;
						//se eliminan los siguientes registros
						for(int i=1;i<indicesRegistros.length;i++){
							indiceRegistro = Integer.parseInt(indicesRegistros[i]);
							indiceRegistro = indiceRegistro - contEliminados;
							detalleCanasta.remove(indiceRegistro);
							codigosArticulosCanasta.remove(indiceRegistro);
							cantidadesOriginales.remove(indiceRegistro);
							//se eliminan los registros seleccionados
							contEliminados++;
						}
						//se actualiza el total del canasto en el formulario
						if(nuevoTotalCanasto<0 || detalleCanasta.isEmpty()){
							session.setAttribute(TOTAL_RECETA, 0.01d);
						}else{
							session.setAttribute(TOTAL_RECETA, Double.valueOf(nuevoTotalCanasto));
						}							

						formulario.setCheckSeleccionarTodo(null);
						formulario.setChecksSeleccionar(null);
						session.setAttribute(CAMBIOS_CANASTO, "ok");
					}else{
						//se verifica el par\u00E1metro para el m\u00E1ximo de modificaciones
						String porcentajeModificacionItemsCanasto = (String)session.getAttribute(PORC_MODIFICACION_RECETA);
						//se genera un mensaje de error
						errors.add("itemsNoEliminados", new ActionMessage("info.canasto.modificacionArticulos", 
								Double.parseDouble(porcentajeModificacionItemsCanasto) + "%", detallePedidoDTO.getNpCanMaxModIteRec()));
					}
					
					calcularTotalRecetaPorPreciosEspeciales(request, detallePedidoDTO, Boolean.FALSE);
				}
			}
			/*--------------- cuando se desea cambiar el precio unitario de los art\u00EDculos del canasto ---------------*/
			else if(request.getParameter("actualizarPreciosUnitarios")!=null){
				request.setAttribute(CotizarReservarAction.ACTIVAR_INPUTS_CAMBIO_PRECIOS,"ok");
			}
			/*--------------- cuando se desea almacenar los cambios en el detalle de una canasta ---------------------*/
			else if(formulario.getBotonRegistrarCanasta()!=null || ((String)session.getAttribute(VERIFICAR_CANASTAESPECIAL)!=null && request.getParameter("siVerificarStock")!=null)){
				try{
						LogSISPE.getLog().info("Presiono el boton Guardar Canasta");
						
						if(session.getAttribute(CAMBIOS_CANASTO)!=null){
							String peticion = request.getParameter(Globals.AYUDA);		
							
							//se obtienen los totales
							double subTotalNoAplicaIVA = ((Double)session.getAttribute(CotizarReservarAction.SUB_TOTAL_NOAPLICA_IVA)).doubleValue();
							double subTotalAplicaIVA = ((Double)session.getAttribute(CotizarReservarAction.SUB_TOTAL_APLICA_IVA)).doubleValue();
							Boolean seRegistro = false;
							//se obtiene el detalle del pedido principal
							Collection<DetallePedidoDTO> detallePedido = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
							DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(RECETA_SELECCIONADA);				
							
							if(peticion.equals("siRegCanastoIgual")){
								int indiceTipoEspecial = 0;
								ArrayList tipoEspeciales = (ArrayList)session.getAttribute(CotizarReservarAction.COL_TIPO_ESPECIALES);
								EspecialDTO especialDTO = (EspecialDTO)tipoEspeciales.get(indiceTipoEspecial);
								Collection<ArticuloDTO> articulosEspeciales = especialDTO.getArticulos();					
								
								DetallePedidoDTO nuevoDetallePedidoDTO = new DetallePedidoDTO();
								for(ArticuloDTO articuloDTO : articulosEspeciales){
									if(articuloDTO.getDescripcionArticulo().equals(session.getAttribute(DetalleCanastaAction.CANASTA_EXISTENTE))){								
										nuevoDetallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado(), detallePedido.size(), articuloDTO, request,errors);							
										if(nuevoDetallePedidoDTO!=null){	
											detallePedido.remove(detallePedidoDTO);
											detallePedido.add(nuevoDetallePedidoDTO);
											
										}else{
											errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo con codigo de barras: "+articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()+", es probable que existan problemas con la informaci\u00F3n registrada"));
										}
									}
								}
								detallePedidoDTO.getArticuloDTO().setCodigoArticuloOriginal(null);
							}
							
							if(detallePedidoDTO != null){
								//se obtiene el precio total del canasto
								Double precioCanastoIVA = Util.roundDoubleMath((Double)session.getAttribute(TOTAL_RECETA), NUMERO_DECIMALES);
								Double precioCanastoSinIVA = Util.roundDoubleMath((Double)session.getAttribute(TOTAL_RECETA_SIN_IVA), NUMERO_DECIMALES); //se inicializa el precio total sin iva
								
//								if(detallePedidoDTO.getEstadoDetallePedidoDTO().getAplicaIVA().equals(estadoActivo)){
//									//se calcula el valor total del canasto sin iva
//									precioCanastoSinIVA = precioCanastoIVA	/ (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
//									precioCanastoSinIVA = Util.roundDoubleMath(precioCanastoSinIVA, NUMERO_DECIMALES);
//								}
								
								//se formatea el valor unitario del estado
								detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(Util.roundDoubleMath(detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado(),NUMERO_DECIMALES));
				
								if(precioCanastoIVA.doubleValue() != detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().doubleValue()){
									//se asignan los valores unitarios al estado del detalle
									detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(precioCanastoSinIVA);
									detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(precioCanastoIVA);
									
									//se asignan los valores unitarios para no afiliados
									detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(precioCanastoSinIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request)));
									detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(precioCanastoIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request)));
									
									//llamada al m\u00E9todo que recalcula el precio de caja
									UtilesSISPE.recalcularPrecioCaja(detallePedidoDTO);
									//llamada al m\u00E9todo que recalcula el precio de mayorista
									UtilesSISPE.recalcularPrecioMayorista(detallePedidoDTO, WebSISPEUtil.obtenerPorcentajeDiferenciaPrecioNormalYMayorista(request));
									
									//llamada al m\u00E9todo que recalcula los totales del detalle del pedido principal
									CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoDTO, request, false,false);
								}					
								
								//validar si cambio los precios del canasto para verificar la receta
								Boolean cambioPrecioCanasto=Boolean.FALSE;
								Double precioCanasto = Util.roundDoubleMath((Double)session.getAttribute(TOTAL_RECETA),NUMERO_DECIMALES);
								LogSISPE.getLog().info("precioCanastoActual: {}, precioCanastoInicial: {}",precioCanasto,beanSession.getPrecioCanastoInicial());
								//se realiza la comparaci\u00F3n de precios
								if(precioCanasto!=null && beanSession.getPrecioCanastoInicial()!=null &&
										precioCanasto.doubleValue() != beanSession.getPrecioCanastoInicial().doubleValue()){
									cambioPrecioCanasto=Boolean.TRUE;
								}
								
								
								if(CotizacionReservacionUtil.verificarCanastoExistente(detallePedidoDTO, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && !peticion.equals("siRegCanastoIgual")
										//&& detallePedidoDTO.getArticuloDTO().getDescripcionArticulo().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.canastoCotizacion"))
										&& request.getParameter("cambiarCanastoConsolidacion")== null && request.getParameter("conservarCanastoConsolidacion") == null){						
									WebSISPEUtil.asignarVariablesPreguntaConfirmacion(null, 
											"\u00BFEl canasto que desea ingresar ya existe como canasto de cat\u00E1logo "+ session.getAttribute(DetalleCanastaAction.CANASTA_EXISTENTE) +". Desea agregar este art\u00EDculo?", "siRegCanastoIgual", "noRegCanasto", request);						
								} //para verificar si es canasto especial
								else if(CotizacionReservacionUtil.verificarCanastoExistente(detallePedidoDTO, request,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial")) 
										&& !peticion.equals("siRegCanastoIgual")
										&& ((String)session.getAttribute(VERIFICAR_CANASTAESPECIAL))==null
										&& cambioPrecioCanasto && request.getParameter("cambiarCanastoConsolidacion")== null && request.getParameter("conservarCanastoConsolidacion") == null){
									
									VistaRecetaArticuloDTO vistaRecetaArticulo =(VistaRecetaArticuloDTO)session.getAttribute(VISTADETALLE_CANASTAESPECIAL);
									LogSISPE.getLog().info("Codigo de Articulo " + vistaRecetaArticulo.getId().getCodigoRecetaArticulo());
									LogSISPE.getLog().info("Ya existe la receta con la clave: " + vistaRecetaArticulo.getClaveRecetaArticulo() + ", descripcion: " + vistaRecetaArticulo.getDescripcionArticulo());
									
									ArticuloDTO articuloConsulta = new ArticuloDTO();
									articuloConsulta.getId().setCodigoCompania(detallePedidoDTO.getArticuloDTO().getId().getCodigoCompania());
									articuloConsulta.getId().setCodigoArticulo(vistaRecetaArticulo.getId().getCodigoRecetaArticulo());
									
									Collection<ArticuloDTO>articuloDTOs = SISPEFactory.obtenerServicioSispe().transObtenerArticuloQBE(articuloConsulta);
									
									if(articuloDTOs.size()==1){
										ArticuloDTO articuloDTOAux = articuloDTOs.iterator().next();
										session.setAttribute(ARTICULO_CANASTAESPECIAL,articuloDTOAux);
									}else {
										throw new SISPEException("No se puede registrar el canasto en el SISPE, existe m\u00E1s de un art\u00EDculo con el c\u00F3digo de barras: {}",  articuloDTOs.iterator().next().getCodigoBarrasActivo().getId().getCodigoBarras());
									}
									
									//cuando el pedido ha sido modificado desde otra sesion
						  			CotizacionReservacionUtil.instanciarVentanaVerificarCanastoEspecial(request, "detalleCanasta.do");						
									//se vuelve a crear la variable de b\u00FAsqueda para el pedido principal
									session.setAttribute(VERIFICAR_CANASTAESPECIAL,"ok");
									formulario.setBotonRegistrarCanasta("verificarCanastoEspecial");
								}else{
									Boolean guardarReceta=Boolean.TRUE;
									if(((String)session.getAttribute(VERIFICAR_CANASTAESPECIAL))!=null){
												ArticuloDTO articuloDTOAux = (ArticuloDTO)session.getAttribute(ARTICULO_CANASTAESPECIAL);
												ArticuloDTO artConsulta= new ArticuloDTO();
												artConsulta.setNpCodigoBarras(articuloDTOAux.getCodigoBarrasActivo().getId().getCodigoBarras());
												WebSISPEUtil.construirConsultaArticulos(request, artConsulta, estadoInactivo, estadoInactivo, accion);
												try{
													SISPEFactory.obtenerServicioSispe().transObtenerArticuloCodigoBarras(artConsulta);
												}catch(SISPEException ex){
													 guardarReceta=Boolean.FALSE;
													LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
													errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
													errors.add("errorSIC", new ActionMessage("errors.agregarArticuloSIC"));
												}
										formulario.setBotonRegistrarCanasta("acepCanasto");	
										session.removeAttribute(VERIFICAR_CANASTAESPECIAL);	
										session.removeAttribute(SessionManagerSISPE.POPUP);
									}
									
									//se valida si es un pedido consolidado y existen los mismos canastos en los pedidos consolidados
									if(formulario.getBotonRegistrarCanasta() != null && formulario.getBotonRegistrarCanasta().equals("acepCanasto") 
											&& existeCanastoEspecialEnPedidosConsolidados(request, detallePedidoDTO)){
										LogSISPE.getLog().info("si existe el canasto en los pedidos consolidados");
										//se muestra el popUp para aplicar el cambio o no en los otros pedidos consolidados
							  			ConsolidarAction.instanciarVentanaCambiarCanastosOtrosPedidosConsolidados(request, "detalleCanasta.do");
							  			guardarReceta = false;
							  			
									}
									 
									if(request.getParameter("cambiarCanastoConsolidacion") != null){
										LogSISPE.getLog().info("se cambiaran todos los canastos especiales");
										session.removeAttribute(SessionManagerSISPE.POPUP);
										asignarBanderaCambiarCanastoConsolidado(request, detallePedidoDTO, true);
										
									}else if(request.getParameter("conservarCanastoConsolidacion") != null){
										LogSISPE.getLog().info("solo se cambiara el canasto actual");
										session.removeAttribute(SessionManagerSISPE.POPUP);
										asignarBanderaCambiarCanastoConsolidado(request, detallePedidoDTO, false);
									}
									
									if(EntregaLocalCalendarioUtil.existenEntregasDomicilioCDEntregadas(detallePedidoDTO)){
										
										boolean mostrarPopUp = true;
										
										if(request.getParameter("siEliminarDetalle") != null){
											mostrarPopUp = false;
											session.removeAttribute(SessionManagerSISPE.POPUP);
										}
										else if(request.getParameter("cancelarEliminarDetalleDespachado") != null){
											LogSISPE.getLog().info("El usuario presiono cancelar creacion de canasto especial");
											session.removeAttribute(SessionManagerSISPE.POPUP);
											guardarReceta = false; 
											mostrarPopUp = false;
										}
										
										if(mostrarPopUp && session.getAttribute(SessionManagerSISPE.POPUP) == null){
											CotizacionReservacionUtil.instanciarPopUpEliminarDetalleEntregasDespachadas(request, "detalleCanasta.do");
											guardarReceta = false;
										}
									}
									
									
									if(guardarReceta){
										
										//si los items eliminados tienen autorizacion de stock, se inactiva la autorizacion
										AutorizacionesUtil.inactivarAutorizacionesStockItemsEliminados(detallePedidoDTO);
										//se eliminan las entregas si un canasto de catalogo se hizo especial o viceversa
										eliminarEntregasCreacionCanasto(request, detallePedidoDTO, accion, warnings, formulario);
										
										session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedido);
										seRegistro=true;
										
										//se actualiza los detalles consolidados
										ConsolidarAction.actualizarDetalleModificadoEnConsolidados(request, detallePedidoDTO);
										
										//se verifica si debe aplicar automaticamente el descuento MARCA PROPIA
										formulario.setOpDescuentos(DescuentosUtil.iniciarDescuentoAutomaticoMarcaPropia(request, Boolean.TRUE));
										//se actualizan los descuentos
										formulario.actualizarDescuentos(request,warnings);
				
										CotizacionReservacionUtil.verificarProblemasEnRecetaPorDatosSIC(detallePedidoDTO, request);
										
										//se vuelve a crear la variable de b\u00FAsqueda para el pedido principal
										session.setAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL,"ok");
				
										//llamada al m\u00E9todo que controla las diferencias del canasto actual en relaci\u00F3n al original
										CotizacionReservacionUtil.verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request);
										//InicializoParametros
										CrearReservacionAction.inicializarParametros(request, formulario);
										//llamada al m\u00E9todo que calcula los valores finales del pedido (detalles y totales)
										CotizacionReservacionUtil.calcularValoresFinalesPedido(request, detallePedido, formulario);
										
										LogSISPE.getLog().info("SUBTOTAL APLICA IVA: {}",subTotalAplicaIVA);
										LogSISPE.getLog().info("SUBTOTAL NO APLICA IVA: {}",subTotalNoAplicaIVA);
						
										//se inicializa la opci\u00F3n de b\u00FAsqueda
										formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
										formulario.setTextoBusqueda(null);
										formulario.setCodigoArticulo(null);
										formulario.setCantidadArticulo(null);
						
										formulario.setCheckSeleccionarTodo(null);
										formulario.setChecksSeleccionar(null);
																
										//se eliminan las variables de sesi\u00F3n que ya no se van a necesitar
										limpiarVariablesSesion(session);
										
										//check nuevamente si tiene o no pago en efectivo
										if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
												session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
											formulario.setCheckPagoEfectivo(estadoActivo);
										}
										
										//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
										CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
										try{
											CotizacionReservacionUtil.actualizarDetalleAction(request, infos, errors, warnings, formulario, estadoActivo, estadoInactivo,Boolean.TRUE);
										}catch(SISPEException ex){
											LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
											errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
											errors.add("errorSIC", new ActionMessage("errors.agregarArticuloSIC"));
										}
									}
								}					
							}
							if(seRegistro){
								LogSISPE.getLog().info("se registr\u00F3");
								session.removeAttribute(CAMBIOS_CANASTO);
								salida="cotizacion";
							}else{
								LogSISPE.getLog().info("no se registr\u00F3");
								salida="detalleCanasta";
							}
					}else{
						salida="cotizacion";
					}
				}catch(Exception ex){
					LogSISPE.getLog().info("error de aplicacion",ex);
					errors.add("Exception",new ActionMessage("errors.gerneral",ex.getMessage()));
					salida="detalleCanasta";
				}
			}//cerrar ventana de verifiacion de canastos especiales
			else if(request.getParameter("cancelarVerificarCanastosEspeciales")!=null){
				LogSISPE.getLog().info("cancelarVerificarCanastosEspeciales PopUp");
				formulario.setBotonRegistrarCanasta("acepCanasto");	
				session.removeAttribute(VERIFICAR_CANASTAESPECIAL);	
				session.removeAttribute(SessionManagerSISPE.POPUP);
			}
			//------------------ cuando se da click en el bot\u00F3n atras -----------------------
			else if(formulario.getBotonSalirCanasta()!=null)
			{
				//se obtiene la canasta inicialmente consultada
				//ArrayList canastaInicial = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.pedido.canastaInicial");
				//se obtiene el precio total del canasto y se lo formatea antes de la comparaci\u00F3n
				Double precioCanasto = Util.roundDoubleMath((Double)session.getAttribute(TOTAL_RECETA),NUMERO_DECIMALES);
				LogSISPE.getLog().info("precioCanastoActual: {}, precioCanastoInicial: {}",precioCanasto,beanSession.getPrecioCanastoInicial());
				//se realiza la comparaci\u00F3n de precios
				if(precioCanasto!=null && beanSession.getPrecioCanastoInicial()!=null &&
						precioCanasto.doubleValue() != beanSession.getPrecioCanastoInicial().doubleValue() && 
						!formulario.getBotonSalirCanasta().equals("noRegCanasto")){
					//significa que el canasto fu\u00E9 modificado y que se debe mostrar la pregunta de confirmaci\u00F3n
					//llamada al m\u00E9todo que realiza el registro de las variables para la pregunta de confirmaci\u00F3n
					WebSISPEUtil.asignarVariablesPreguntaConfirmacion("message.confirmacion.salirCanastoSinGuardar", 
							"\u00BFDesea guardar los cambios realizados?", "siRegCanasto", "noRegCanasto", request);
					//se actualiza el precio incicial para la pr\u00F3xima comparaci\u00F3n
					return mapping.findForward(salida);
				}

				LogSISPE.getLog().info("INDICE: {}",beanSession.getIndiceRegistro());
				//se obtiene el detalle del pedido principal
				ArrayList detallePedido = (ArrayList)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
				//se obtiene el detallePedidoOriginal
				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(DETALLE_PEDIDODTO_ORIGINAL);
				if(detallePedidoDTO != null){
					detallePedidoDTO.setItemsAgregadosReceta((ArrayList<String>)session.getAttribute(COL_AGREGADOS_RECETA_ORIGINAL));
					detallePedidoDTO.setItemsEliminadosReceta((ArrayList<String>)session.getAttribute(COL_ELIMINADOS_RECETA_ORIGINAL));
					//detallePedidoDTO.setArticuloDTO((ArticuloDTO)session.getAttribute(ARTICULODTO_ORIGINAL));
					//se obtienen las recetas originales
					Collection recetasOriginales = (Collection)session.getAttribute(COL_RECETA_ART_ORIGINAL);
					detallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetasOriginales);
					//se reasigna el objeto detallePedidoDTO al detalle principal del pedido
					detallePedido.set(Integer.parseInt(beanSession.getIndiceRegistro()), detallePedidoDTO);
				}
				//se inicializan algunos campos del formulario
				formulario.setCheckSeleccionarTodo(null);
				formulario.setChecksSeleccionar(null);
				formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
				formulario.setTextoBusqueda(null);
				formulario.setCodigoArticulo(null);
				formulario.setCantidadArticulo(null);

				//se vuelve a crear la variable de b\u00FAsqueda para el pedido principal
				session.setAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL,"ok");
				//se eliminan las variables que ya no se necesitar\u00E1n
				limpiarVariablesSesion(session);
				
				//check nuevamente si tiene o no pago en efectivo
				if(session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP) != null && ((String)session.getAttribute(CotizarReservarAction.DES_MAX_NAV_EMP)).equals("ok") || 
						session.getAttribute(CHECK_PAGO_EFECTIVO) != null){
					formulario.setCheckPagoEfectivo(estadoActivo);
				}
				CrearReservacionAction.inicializarParametros(request, formulario);
				//verifica si existen descuentos seleccionados y establece las propiedades correspondientes en el formulario
				CotizacionReservacionUtil.establecerDescuentosFormulario(request, formulario);
				session.removeAttribute(CAMBIOS_CANASTO);
				salida="cotizacion";
			}else{
				salida = "detalleCanasta";
			}
			
			//IOnofre. Cierra el PopUp de buscar articulos
			if(request.getParameter("buscador") !=null){
				LogSISPE.getLog().info("Cierra el PopUp de busqueda de articulos");
				session.removeAttribute(BuscarArticuloUtil.POPUP_BUSQUEDA_ARTICULOS);
				session.removeAttribute("ec.com.smx.sic.sispe.catalogoArticulos.articulos");
			}
			
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		
		//se guardan los mensajes 
		saveMessages(request, success);
		saveInfos(request, infos);
		saveWarnings(request, warnings);
		saveErrors(request, errors);

		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);

		return mapping.findForward(salida);
	}

	/**
	 * Actualiza el total de la canasta cuando se a\u00F1adieron art&iacute;culos por la b&uacute;squeda.
	 * @param session			La sesi&oacute;n actual
	 * @param formulario	El formulario asociado a la acci&oacute;n
	 */
	private void actualizarTotalCanastaPorAdicionBusqueda(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("por verificarTotalCanasta");
		Double totalCanasta = (Double)session.getAttribute(TOTAL_RECETA);
		double nuevoTotalCanasto = 0;
		if(totalCanasta!=null)
			nuevoTotalCanasto = totalCanasta.doubleValue();

		//se obtiene el valor que se va sumar al total de la canasta
		Double totalASumar = (Double)session.getAttribute(TOTAL_RECETA_A_SUMAR);
		LogSISPE.getLog().info("TOTAL A SUMAR: {}",totalASumar);
		if(totalASumar!=null)
			nuevoTotalCanasto = nuevoTotalCanasto + totalASumar.doubleValue();

		session.setAttribute(TOTAL_RECETA, Double.valueOf(nuevoTotalCanasto));
		session.removeAttribute(TOTAL_RECETA_A_SUMAR);
	}

	/**
	 * Realiza la validaci\u00F3n de la adici\u00F3n de art\u00EDculos.
	 * @param  request											- La petici\u00F3n HTTP
	 * @param  codigosArticulos						 	- Los c\u00F3digos de los art\u00EDculos a agregar
	 * @param  detallePedidoDTO							- El objeto <code>DetallePedidoDTO</code>
	 * @return [true: se puede agregar, false: no se puede agregar]
	 */
	public static boolean validarAdicion(HttpServletRequest request, String [] codigosArticulos, 
			DetallePedidoDTO detallePedidoDTO)throws Exception{
		
		HttpSession session = request.getSession();
		String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		
		session.removeAttribute(ITEMS_A_ELIMINAR_DE_ELIMINADOS);
		
		String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);//358801
		
		if(!detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)
				|| (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))
						&& (detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal() != null 
					&& !detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codigoCanCotVacio)))){
			//primero se verifica que la cantidad de articulos a agregar sea menor o igual al n\u00FAmero m\u00E1ximo de
			//modificaciones
			Collection <String> itemsAEliminarDeEliminados = new ArrayList <String>();
			//se itera el arreglo de c\u00F3digos de art\u00EDculos
			for(int i=0; i<codigosArticulos.length; i++){
				//se verifica si el art\u00EDculo est\u00E1 en la lista de eliminados
				if(detallePedidoDTO.getItemsEliminadosReceta().contains(codigosArticulos[i])){
					itemsAEliminarDeEliminados.add(codigosArticulos[i]);
					LogSISPE.getLog().info("codigo a eliminar, de eliminados: {}",codigosArticulos[i]);
				}
			}

			//se calcula el valor de la cantidad posible de adiciones
			int cantidadPosibleAdiciones = (detallePedidoDTO.getNpCanMaxModIteRec().intValue() 
					+ itemsAEliminarDeEliminados.size()) - (detallePedidoDTO.getItemsAgregadosReceta().size() + detallePedidoDTO.getItemsEliminadosReceta().size());
			LogSISPE.getLog().info("cantidadPosibleAdiciones: {}",cantidadPosibleAdiciones);
			//si esta cantidad calculada es menor a la cantidad posible de eliminaciones
			if(codigosArticulos.length <= cantidadPosibleAdiciones){
				for(Iterator <String> it = itemsAEliminarDeEliminados.iterator(); it.hasNext();){
					String codigoArticulo = it.next();
					detallePedidoDTO.getItemsEliminadosReceta().remove(codigoArticulo);
				}
				//se guarda la colecci\u00F3n de c\u00F3digos de art\u00EDculos que fueron eliminados
				session.setAttribute(ITEMS_A_ELIMINAR_DE_ELIMINADOS, itemsAEliminarDeEliminados);
				return true;
			}
			return false;
		}

		return true;
	}

	/**
	 * Realiza la validaci\u00F3n de la eliminaci\u00F3n de art\u00EDculos
	 * @param  request											- La petici\u00F3n HTTP
	 * @param  seleccionados								- Arreglo que contiene los indices de los items a eliminar
	 * @param  detallePedidoDTO							- El objeto <code>DetallePedidoDTO</code>
	 * @return [true: se puede eliminar, false: no se puede eliminar]
	 */
	public static boolean validarEliminacion(HttpServletRequest request, String [] seleccionados, DetallePedidoDTO detallePedidoDTO)throws Exception{
		
		HttpSession session = request.getSession();
		String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
		
		//se elimina la colecci\u00F3n donde se guardan los items eliminados
		session.removeAttribute(ITEMS_A_ELIMINAR_DE_AGREGADOS);
		String codigoCanCotVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);//358801
		
		if(!detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo) 
				|| (detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))
						&& (detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal() != null 
					&& !detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal().equals(codigoCanCotVacio)))){
			
			Collection <String> itemsAEliminarDeAgregados = new ArrayList <String>();
			//obtengo la colecci\u00F3n de detalles de la sesion
			//ArrayList detalleCanasta = (ArrayList)session.getAttribute(COL_DETALLE_RECETA);
			List detalleCanasta = (List)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
			int indiceReceta = 0;
			for(int i = 0; i<seleccionados.length; i++){
				indiceReceta = Integer.parseInt(seleccionados[i]);
				ArticuloRelacionDTO recetaArticuloDTO = (ArticuloRelacionDTO)detalleCanasta.get(indiceReceta);
				//se verifica si el art\u00EDculo est\u00E1 en la lista de agregados
				if(detallePedidoDTO.getItemsAgregadosReceta().contains(recetaArticuloDTO.getId().getCodigoArticulo())){
					itemsAEliminarDeAgregados.add(recetaArticuloDTO.getId().getCodigoArticulo());
					LogSISPE.getLog().info("codigo a eliminar, en agregados: {}",recetaArticuloDTO.getId().getCodigoArticulo());
				}
			}

			//se calcula el valor de la cantidad posible de eliminaciones
			int cantidadPosibleEliminaciones = (detallePedidoDTO.getNpCanMaxModIteRec().intValue() 
					+ itemsAEliminarDeAgregados.size()) - (detallePedidoDTO.getItemsAgregadosReceta().size() + detallePedidoDTO.getItemsEliminadosReceta().size());
			LogSISPE.getLog().info("cantidadPosibleEliminaciones: {}",cantidadPosibleEliminaciones);
			//si esta cantidad calculada es menor a la cantidad posible de eliminaciones
			if(seleccionados.length <= cantidadPosibleEliminaciones){
				for(Iterator <String> it = itemsAEliminarDeAgregados.iterator(); it.hasNext();){
					String codigoArticulo = it.next();
					detallePedidoDTO.getItemsAgregadosReceta().remove(codigoArticulo);
				}
				//se guarda la colecci\u00F3n de c\u00F3digos de art\u00EDculos que fueron eliminados
				session.setAttribute(ITEMS_A_ELIMINAR_DE_AGREGADOS, itemsAEliminarDeAgregados);

				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Realizar el calculo del total de la receta tomando en cuenta si los items manejan precios de caja o mayorista
	 * @param request
	 * @param detallePedidoDTO	
	 * @param formulario
	 * @return
	 * @throws Exception
	 */
	public static double calcularTotalRecetaPorPreciosEspeciales(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, Boolean esRecetaActual)throws Exception
	{
		//Obtener vistaPedido para saber si se calcula el pedido con en nuevo metodo o el anterior
		VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
		
		//Obtengo el codigo del establecimiento actual
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		//solo si es un canasto de cotizaciones
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getEstadoCanCotVacio().equals(estadoActivo)){
			//Obtengo la coleccion de articulos del detalle
			Collection detalleCanasta = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
			boolean esActivoPrecioMayorista = false;
			double totalRecetaNormal = 0.0; //Variable que indica el total del valor de la receta tomando en cuenta solamente precios unitarios
			double totalRecetaIVANormal = 0.0; //Variable que indica el total del valor de la receta tomando en cuenta solamente precios unitarios
			double totalItemRecetaNormal = 0.0; //Variable que indica el total del valor por cada item de la receta
			double totalItemRecetaEspecial = 0.0; //Variable que indica el total del valor especial por cada item de la receta

			double totalValorUnitario = 0.0;
			double totalValorUnitarioIVA = 0.0;
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoCajaReceta(null);
			detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoMayoristaReceta(null);
			
			//Recorro todos los articulos de la receta para verificar cuales tienen precio de caja
			for (Iterator<ArticuloRelacionDTO> iter = detalleCanasta.iterator(); iter.hasNext();){
				ArticuloRelacionDTO recetaArticuloDTO = iter.next();
				esActivoPrecioMayorista = false;
				
				Long cantidadTotal = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * recetaArticuloDTO.getCantidad().longValue();
//				Double precioUnitarioIVACalculo = recetaArticuloDTO.getPrecioUnitarioIVA();
//				Double precioUnitarioCalculo = recetaArticuloDTO.getPrecioUnitario();
//				Double precioCajaCalculo = recetaArticuloDTO.getPrecioCaja();
				
				Double precioUnitarioIVACalculo = recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp();
				Double precioUnitarioCalculo = recetaArticuloDTO.getArticuloRelacion().getPrecioBase();
				Double precioCajaCalculo = recetaArticuloDTO.getArticuloRelacion().getPrecioCaja();
				
				totalRecetaNormal += cantidadTotal.longValue() * precioUnitarioCalculo.doubleValue();
				totalRecetaIVANormal += cantidadTotal.longValue() * precioUnitarioIVACalculo.doubleValue();
				totalItemRecetaNormal = cantidadTotal.longValue() * precioUnitarioCalculo.doubleValue();
				
				totalValorUnitario = totalValorUnitario + (precioUnitarioCalculo*recetaArticuloDTO.getCantidad().longValue());
				totalValorUnitarioIVA = totalValorUnitarioIVA + (precioUnitarioIVACalculo*recetaArticuloDTO.getCantidad().longValue());
				if(CotizacionReservacionUtil.esLocalActivoParaPrecioMayorista(SessionManagerSISPE.getCurrentLocal(request),recetaArticuloDTO.getArticuloRelacion(), request)){
					if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA))==null){
						if(recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioMayoreo()
								&& cantidadTotal >= recetaArticuloDTO.getArticuloRelacion().getCantidadMayoreo()){
							//precioUnitarioCalculo = recetaArticuloDTO.getPrecioMayorista();
							precioUnitarioCalculo = recetaArticuloDTO.getArticuloRelacion().getPrecioMayorista();
							precioCajaCalculo = 0d;
							esActivoPrecioMayorista = true;
						}
					}
				}
				//se verifica si el art\u00EDculo tiene habilitado el precio por caja
				//if(!esActivoPrecioMayorista && recetaArticuloDTO.getArticulo().getNpHabilitarPrecioCaja().equals(estadoActivo) && recetaArticuloDTO.getPrecioCaja() > 0){
				if(((String)request.getSession().getAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS))==null && !esActivoPrecioMayorista && recetaArticuloDTO.getArticuloRelacion().getHabilitadoPrecioCaja() && recetaArticuloDTO.getArticuloRelacion().getPrecioCaja() > 0){
					//se obtiene el cociente entero de la divisi\u00F3n entre la cantidad y la unidad de manejo
					//long cociente = cantidadTotal / recetaArticuloDTO.getArticulo().getUnidadManejoPrecioCaja().longValue();
					long cociente = cantidadTotal / recetaArticuloDTO.getArticuloRelacion().getUnidadManejoPrecioCaja().longValue();
					//se calcula el residuo de la divisi\u00F3n
					long residuo = cantidadTotal - (recetaArticuloDTO.getArticuloRelacion().getUnidadManejoPrecioCaja().longValue() * cociente);
					LogSISPE.getLog().info("unidad de manejo: {}" , recetaArticuloDTO.getArticuloRelacion().getUnidadManejoPrecioCaja());
					LogSISPE.getLog().info("precio de caja: {}" , precioCajaCalculo);
					LogSISPE.getLog().info("valor unitario: {}" , precioUnitarioCalculo);
					double totalCaja= cociente * precioCajaCalculo.doubleValue();
					double totalUnidades = residuo * precioUnitarioCalculo.doubleValue();
					totalItemRecetaEspecial = totalCaja + totalUnidades;
					detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoCajaReceta(
							(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta() != null ? detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoCajaReceta() : 0.0)
							+ Util.roundDoubleMath(totalItemRecetaNormal - totalItemRecetaEspecial, NUMERO_DECIMALES)
					);
				}else{
					LogSISPE.getLog().info("1: {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado());
					LogSISPE.getLog().info("2: {}" , recetaArticuloDTO.getCantidad());
					LogSISPE.getLog().info("3: {}" , precioUnitarioCalculo);
					if(esActivoPrecioMayorista){
						totalItemRecetaEspecial = cantidadTotal.longValue() * precioUnitarioCalculo.doubleValue();
						detallePedidoDTO.getEstadoDetallePedidoDTO().setNpValorDescuentoMayoristaReceta(
								(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta() != null ? detallePedidoDTO.getEstadoDetallePedidoDTO().getNpValorDescuentoMayoristaReceta() : 0.0)
								+ Util.roundDoubleMath(totalItemRecetaNormal - totalItemRecetaEspecial, NUMERO_DECIMALES)
						);
					}
				}
				LogSISPE.getLog().info("total: {}" , totalItemRecetaEspecial);
				
				//si es una canasta vacia
				CotizacionReservacionUtil.calcularValoresDetalleCanastaEspecial(detallePedidoDTO.getEstadoDetallePedidoDTO(), recetaArticuloDTO, request, false);
			}
			
			/*double valorUnitarioIVAEspecial = totalRecetaEspecial / detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
			valorUnitarioIVAEspecial = Util.roundDoubleMath(valorUnitarioIVAEspecial, NUMERO_DECIMALES);*/
			//parametro precios Temp
			String calcularCanastosPreciosTemp=null;
			if(vistaPedidoDTO!=null){
				calcularCanastosPreciosTemp=vistaPedidoDTO.getObsmigperTemp();
			}
			double valorUnitarioNormal =0;
			double valorUnitarioIVANormal =0;
			//TODO: SE CAMBIA LA FORMA DE CALCULAR EL PRECIO DEL CANASTO ESPECIAL, SOLICITUD OW EN REUNION P:\SISPE\Documentacion\Version 8\ActasReunion\2013-05-08 ACTA ANALISIS SISPE CANASTOS ESPECIALES - ARTICULOS GRANEL - MR EN ESTADO DESPACHADO.doc
//			if(calcularCanastosPreciosTemp==null){
//				valorUnitarioNormal = totalRecetaNormal / detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
//				valorUnitarioIVANormal =Util.roundDoubleMath(valorUnitarioNormal * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);
//				valorUnitarioIVANormal = Util.roundDoubleMath(valorUnitarioIVANormal, NUMERO_DECIMALES);
//			}else{
				valorUnitarioIVANormal = totalRecetaIVANormal / detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue();
				valorUnitarioIVANormal = Util.roundDoubleMath(valorUnitarioIVANormal, NUMERO_DECIMALES);
//			}
				if(CotizacionReservacionUtil.verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request)){
					//request.getSession().setAttribute(TOTAL_RECETA, Double.valueOf(valorUnitarioEspecial));
					request.getSession().setAttribute(TOTAL_RECETA, Double.valueOf(valorUnitarioIVANormal));			
					request.getSession().setAttribute(TOTAL_RECETA_SIN_IVA, Util.roundDoubleMath(Double.valueOf(totalValorUnitario), NUMERO_DECIMALES));
					
					//se verifica si el 
					LogSISPE.getLog().info("valor unitario receta: {}" , valorUnitarioIVANormal);
				}else{
					if(detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()!= null && detallePedidoDTO.getArticuloDTO().getCodigoClasificacion().
							equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
						request.getSession().setAttribute(TOTAL_RECETA, Double.valueOf(valorUnitarioIVANormal));			
						request.getSession().setAttribute(TOTAL_RECETA_SIN_IVA, Util.roundDoubleMath(Double.valueOf(totalValorUnitario), NUMERO_DECIMALES));
						
					}else{
						request.getSession().setAttribute(TOTAL_RECETA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado());
						request.getSession().setAttribute(TOTAL_RECETA_SIN_IVA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado());	
						
					}
					
					//para controlar el cambio de precios en receta
					if(!esRecetaActual){
						valorUnitarioIVANormal = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado();
					}
					LogSISPE.getLog().info("valor unitario receta: {}" , valorUnitarioIVANormal);
				}	
			//return valorUnitarioEspecial;
			return valorUnitarioIVANormal;
		}else{
			if(CotizacionReservacionUtil.verificarDiferenciasConRecetaOriginal(detallePedidoDTO, request)){
				Collection<ArticuloRelacionDTO> detalleCanasta = detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();	
				double totalValorUnitario = 0.0;
				double totalValorUnitarioIVA = 0.0;
				
				for (ArticuloRelacionDTO recetaArticuloDTO: detalleCanasta){
					totalValorUnitario = totalValorUnitario + (recetaArticuloDTO.getArticuloRelacion().getPrecioBase()*recetaArticuloDTO.getCantidad().longValue());
					totalValorUnitarioIVA = totalValorUnitarioIVA + (recetaArticuloDTO.getArticuloRelacion().getPrecioBaseImp()*recetaArticuloDTO.getCantidad().longValue());
				}	
				request.getSession().setAttribute(TOTAL_RECETA, Util.roundDoubleMath(Double.valueOf(totalValorUnitarioIVA), NUMERO_DECIMALES));
				request.getSession().setAttribute(TOTAL_RECETA_SIN_IVA, Util.roundDoubleMath(Double.valueOf(totalValorUnitario), NUMERO_DECIMALES));
			}else{
				request.getSession().setAttribute(TOTAL_RECETA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado());
				request.getSession().setAttribute(TOTAL_RECETA_SIN_IVA, detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado());	
			}
			
		}
		
		return 0;
	}

	/**
	 * 
	 * @param session
	 */
	private void limpiarVariablesSesion(HttpSession session){
		//se eliminan las variables de sesi\u00F3n que ya no se van a necesitar
		session.removeAttribute(TOTAL_RECETA);
		session.removeAttribute(RECETA_SELECCIONADA);
		session.removeAttribute(COL_CODARTICULOS_DETALLE_RECETA);
		session.removeAttribute(COL_CANT_ORIGINALES_RECETA);
		//session.removeAttribute(COL_DETALLE_RECETA);
		session.removeAttribute(DETALLE_PEDIDODTO_ORIGINAL);
		session.removeAttribute(COL_AGREGADOS_RECETA_ORIGINAL);
		session.removeAttribute(COL_ELIMINADOS_RECETA_ORIGINAL);
		//session.removeAttribute(ARTICULODTO_ORIGINAL);
		session.removeAttribute(COL_RECETA_ART_ORIGINAL);
		session.removeAttribute(DETALLE_SIN_ACTUALIZAR);
	}
	
	
	/**
	 * Verifica si existe un canasto igual en los otros pedidos consolidados en estado COT, REC, CCA
	 * @param request
	 * @param detallePedidoDTO
	 * @return true si el canasto existe en los otros pedidos consolidados, false caso contrario
	 */
	private Boolean existeCanastoEspecialEnPedidosConsolidados(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO){
		
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Verificando si el canasto existe en los otros pedidos consolidados");
		//se verifica si se trata de un pedido consolidado
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
				&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
			
			//se obtienen los pedidos consolidados
			//se obtiene de sesion el pedido consolidado
			Collection<DetallePedidoDTO> detallePedidoConsolidadoCol = (ArrayList<DetallePedidoDTO>)
					session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidadoCol)){
				
				//se recorren los detalles consolidados
				for(DetallePedidoDTO detallePedidoConsolidadoDTO : detallePedidoConsolidadoCol){
					
					//se verifica si es el mismo detalle
					if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())
							&& !detallePedidoDTO.getId().getCodigoPedido().equals(detallePedidoConsolidadoDTO.getId().getCodigoPedido())
							&& (detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZADO)
									|| detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RECOTIZADO)
									|| detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZACION_CADUCADA))){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Se setea una bandera para saber si hay que cambiar todos los canastos consolidados o solo el que se esta modificando actualmente
	 * @param request
	 * @param detallePedidoDTO
	 * @param cambiarCanastosConsolidados
	 * @throws Exception
	 */
	private void asignarBanderaCambiarCanastoConsolidado(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, boolean cambiarCanastosConsolidados) throws Exception{
		
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Verificando si el canasto existe en los otros pedidos consolidados");
		//se verifica si se trata de un pedido consolidado
		if(session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO) != null 
				&& (Boolean)session.getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
			
			//se obtienen los pedidos consolidados
			//se obtiene de sesion el pedido consolidado
			Collection<DetallePedidoDTO> detallePedidoConsolidadoCol = (ArrayList<DetallePedidoDTO>)
					session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			if(CollectionUtils.isNotEmpty(detallePedidoConsolidadoCol)){
				
				//se recorren los detalles consolidados
				for(DetallePedidoDTO detallePedidoConsolidadoDTO : detallePedidoConsolidadoCol){
					
					//se verifica si es el mismo detalle
					if(detallePedidoDTO.getId().getCodigoArticulo().equals(detallePedidoConsolidadoDTO.getId().getCodigoArticulo())
							&& !detallePedidoDTO.getId().getCodigoPedido().equals(detallePedidoConsolidadoDTO.getId().getCodigoPedido())
							&& (detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZADO)
									|| detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RECOTIZADO)
									|| detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_COTIZACION_CADUCADA))){
						
						detallePedidoConsolidadoDTO.setNpCambiarACanastoConsolidado(cambiarCanastosConsolidados);
						LogSISPE.getLog().info(""+detallePedidoConsolidadoDTO.getArticuloDTO().getDescripcionArticulo()+" - "+detallePedidoConsolidadoDTO.getId().getCodigoPedido());
					
						if(cambiarCanastosConsolidados){
							//se realiza el cambio de receta en los demas pedidos consolidados
							detallePedidoConsolidadoDTO.getArticuloDTO().setArticuloRelacionCol(new ArrayList<ArticuloRelacionDTO>());
							for(ArticuloRelacionDTO articuloRelacionDTO : detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol()){
								detallePedidoConsolidadoDTO.getArticuloDTO().getArticuloRelacionCol().add(SerializationUtils.clone(articuloRelacionDTO));
							}
							LogSISPE.getLog().info("cambiando el detalle de la receta del pedido "+detallePedidoConsolidadoDTO.getId().getCodigoPedido()+" articulo :"
									+detallePedidoConsolidadoDTO.getId().getCodigoArticulo()+":"+detallePedidoConsolidadoDTO.getArticuloDTO().getDescripcionArticulo());
							
							//se obtiene el precio total del canasto
							Double precioCanastoIVA = Util.roundDoubleMath((Double)session.getAttribute(TOTAL_RECETA), NUMERO_DECIMALES);
							Double precioCanastoSinIVA = precioCanastoIVA; //se inicializa el precio total sin iva
							String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
							
							if(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getAplicaIVA().equals(estadoActivo)){
								//se calcula el valor total del canasto sin iva
								precioCanastoSinIVA = precioCanastoIVA	/ (1 + Double.parseDouble(SessionManagerSISPE.getValorIVA(request)));
								precioCanastoSinIVA = Util.roundDoubleMath(precioCanastoSinIVA, NUMERO_DECIMALES);
							}
							
							//se formatea el valor unitario del estado
							detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(Util.roundDoubleMath(detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado(),NUMERO_DECIMALES));
			
							if(precioCanastoIVA.doubleValue() != detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().doubleValue()){
								//se asignan los valores unitarios al estado del detalle
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(precioCanastoSinIVA);
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(precioCanastoIVA);
								
								//se asignan los valores unitarios para no afiliados
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(precioCanastoSinIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request)));
								detallePedidoConsolidadoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(precioCanastoIVA, WebSISPEUtil.obtenerPorcentajeCalculoPreciosAfiliado(request)));
								
								//llamada al m\u00E9todo que recalcula el precio de caja
								UtilesSISPE.recalcularPrecioCaja(detallePedidoConsolidadoDTO);
								//llamada al m\u00E9todo que recalcula el precio de mayorista
								UtilesSISPE.recalcularPrecioMayorista(detallePedidoDTO, WebSISPEUtil.obtenerPorcentajeDiferenciaPrecioNormalYMayorista(request));
								
								//llamada al m\u00E9todo que recalcula los totales del detalle del pedido principal
								CotizacionReservacionUtil.calcularValoresDetalle(detallePedidoConsolidadoDTO, request, false,false);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Si se modifica un canasto de catalogo convirtiendolo en canasto especial, se eliminan las entregas para que se configuren como canasto especial o viceversa
	 * @param request
	 * @param detallePedidoDTO
	 * @param accion
	 * @param warnings
	 * @param formulario
	 * @throws Exception
	 */
	private static void eliminarEntregasCreacionCanasto(HttpServletRequest request, DetallePedidoDTO detallePedidoDTO, 
			String accion, ActionMessages warnings, CotizarReservarForm formulario) throws Exception{
		LogSISPE.getLog().info("Si el canasto modificado era de catalogo, se eliminan las entregas para que se configure nuevamente como canasto especial o viceversa");
		
		if(CollectionUtils.isNotEmpty(detallePedidoDTO.getEntregaDetallePedidoCol())){
				
			if((request.getSession().getAttribute(ES_CANASTO_DE_CATALOGO) != null && (Boolean)request.getSession().getAttribute(ES_CANASTO_DE_CATALOGO)) 
						|| detallePedidoDTO.getArticuloDTO().getCodigoArticuloOriginal()==null){
			
				//se valida la accion
				if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")) 
						|| accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))
						|| accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))){
					
					//modifica los datos de las entregas del detalle a eliminar
					formulario.eliminarEntregasPorModificacionDetallePrincipal(detallePedidoDTO, new ActionErrors(), request, false, false);
					warnings.add("entregasEliminadas", new ActionMessage("warnings.entregas.canastos.eliminados"));
				}
			}
		}
		
	}
}