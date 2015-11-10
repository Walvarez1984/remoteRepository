package ec.com.smx.sic.sispe.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.exception.SICException;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloComercialDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.MarcaArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.SubClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action.CrearPedidoAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ConsolidarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.DetalleCanastaAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.BuscarArticuloForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

public class BuscarArticuloUtil {
	
	public static final String TAMANOCONSULTAARTICULOS = "ec.com.smx.sic.sispe.tamanoConsultaArticulos";//Sesion que sirve para guardar el tama\u00F1o de la coleccion resustante de la consulta de articulos
	public static final String ARTICULOCONSULTA = "ec.com.smx.sic.sispe.articuloConsulta";//Sesion que sirve para guardar el articulo con los parametros de busqueda
	public static final String BUSQUEDA_PED_ESPECIALES = "ec.com.smx.sic.sispe.busquedaPedEspeciales";
	public static final String BUSQUEDA_POR_CATALOGO = "ec.com.smx.sic.sispe.busqueda.porCatalogo";
	public static final String ART_AGREGADOS_BUSQUEDA_PED = "ec.com.smx.sic.sispe.articulosAgregados.busquedaPed";
	
	public static final String POPUP_BUSQUEDA_ARTICULOS = "ec.com.smx.sic.sispe.accion.buscar.articulos"; //Variable de sesion
	
	public static final String POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL = "ec.com.smx.sic.sispe.accion.buscar.articulos.estructura.comercial"; //Variable de sesion
	
	private static final String TITULO_DIVISION = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDivision");
	public static final String CATALOGO_ACTUAL = "ec.com.smx.sic.sispe.catalogoArticulos.catalogo";
	private static final String TITULO_DEPARTAMENTO = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDepartamento");
	private static final String NIVEL_CATALOGO = "ec.com.smx.sic.sispe.catalogoArticulos.nivel";
	private static final String RUTA_ESTRUCTURA_COMERCIAL = "ec.com.smx.sic.sispe.catalogoArticulos.estructura";
	private static final String TITULO_CLASIFICACION = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloClasificacion");
	public static final String CATALOGO_ANTERIOR = "ec.com.smx.sic.sispe.catalogoArticulos.catalogoAnterior";
	private static final String RUTA_EST_COMERCIAL_ANTERIOR = "ec.com.smx.sic.sispe.catalogoArticulos.estructuraAnterior";
	public static final String ARTICULOS_AGREGADOS_BUSQUEDA = "ec.com.smx.sic.sispe.catalogoArticulos.articulos";
	private static final String COL_DIVISIONES = "ec.com.smx.sic.sispe.catalogoArticulos.divisiones";
	public static final String CLASIFICACIONES_AGREGADAS = "ec.com.smx.sic.sispe.clasificacionesAgregadas";

	
	/**
	 * Clase ofrece: 
	 * IOnofre
	 * 10:16:02
	 * version 0.1
	 *  	
	 */
	public static void accionesBuscarArticulos(HttpServletRequest request, BuscarArticuloForm formulario, ActionMessages errors, ActionMessages exitos,ActionMessages warnings,ActionMessages infos){
		
		HttpSession session= request.getSession();
		//Obtengo la accion actual
		String accion=(String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		
		try{
		/*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
		if(request.getParameter("range")!=null || request.getParameter("start")!=null)
		{
			LogSISPE.getLog().info("ENTRO A LA PAGINACION");
			LogSISPE.getLog().info("start: {}, range: {}",request.getParameter("start"),request.getParameter("range"));
			//reseteo los valores del check de articulos.
			LogSISPE.getLog().info("Reseteo los valores de los checks");
			formulario.setOpEscogerProdBuscados(null);
			formulario.setOpEscogerTodos(null);
			//se obtiene el tamano de la coleccion total de articulos
			int tamano=0;
			tamano = ((Integer)session.getAttribute(TAMANOCONSULTAARTICULOS)).intValue();
			if(tamano>0){
				int size= tamano;
				int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
				int start= Integer.parseInt(request.getParameter("start"));
				formulario.setStart(String.valueOf(start));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(size));
				//Recupero el articulo con los parametros de la busqueda
				ArticuloDTO articulo=(ArticuloDTO)session.getAttribute(ARTICULOCONSULTA);
				//Cambio el inicio de la busqueda
				//articulo.setNpFirstResult(new Integer(start));
				articulo.setFirstResult(new Integer(start));
				//articulo.setMaxResults(range);
				Collection articulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulo(articulo);
				formulario.setDatos(articulos);
			}
		}
		/*----------------- cuando se quiere buscar un art\u00EDculo en el pedido ---------------------------*/
		else if(request.getParameter("buscarArtPed")!=null){
			
			LogSISPE.getLog().info("ENTRO A LA BUSQUEDA DESDE LOS PEDIDOS");
			//variable usada para verificar si la clasificacion a buscar es correcta para el tipo de pedido
			boolean clasificacionCorrecta=false;
			
			ClasificacionDTO clasificacionDTO = new ClasificacionDTO();
			//aqui se almacena la lista de articulos que se van a buscar
			Collection articulos = new ArrayList();
			//se crea el objeto DTO para acceder a los articulos
			ArticuloDTO articuloDTO = new ArticuloDTO();
			articuloDTO.setCodigoEstado(SICArticuloConstantes.getInstancia().ESTADOARTICULO_CODIFICADO);
			ArticuloComercialDTO articuloComercialDTO = null;
			MarcaArticuloDTO marcaArticuloDTO = null;
			
			String accionCrearPedidoEspecial = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.crearPedidoEspecial");
			
			if(request.getParameter("textoBusqueda")==null || request.getParameter("textoBusqueda").equals("")){
				errors.add("busqueda",new ActionMessage("errors.requerido.busqueda"));
			}else{
			
				if(request.getParameter("opTipoBusqueda").equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion")))
				{
					//Si la accion es pedidos especiales se verifica la siguiente condici\u00F3n
					if(accion!=null && accion.equals(accionCrearPedidoEspecial)){
						//se consulta por c\u00F3digo de clasificaci\u00F3n
						if(session.getAttribute(CrearPedidoAction.LISTA_CLASIFICACIONES)!= null){
							String codigoClasificacionIngresado = request.getParameter("textoBusqueda");
							String clasificaciones =(String)session.getAttribute(CrearPedidoAction.LISTA_CLASIFICACIONES);
							String[] colClasificaciones= clasificaciones.split(",");
							LogSISPE.getLog().info("tama\u00F1o coleccion: {}",colClasificaciones.length);
							//se iteran las clasificaciones permitidas
							for(int i=0;i<colClasificaciones.length;i++){
								if(colClasificaciones[i].equals(codigoClasificacionIngresado)){
									LogSISPE.getLog().info("clasificacion[{}]: {}",i,colClasificaciones[i]);
									articuloDTO.setCodigoClasificacion(codigoClasificacionIngresado);
									clasificacionCorrecta = true;
									break;
								}
							}
						}
						
						if(!clasificacionCorrecta){
							//si la clasificaci\u00F3n es incorrecta se termina la ejecuci\u00F3n del m\u00E9todo y se genera un mensaje
							//de error
							errors.add("errorObtener",new ActionMessage("errors.listaVacia.codigoClasificacion"));
						}
					}
					articuloDTO.setCodigoClasificacion(request.getParameter("textoBusqueda"));
					
				}else if(request.getParameter("opTipoBusqueda").equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreClasificacion"))){
					//se consulta por nombre de clasificaci\u00F3n
					clasificacionDTO.setDescripcionClasificacion(request.getParameter("textoBusqueda"));
					clasificacionDTO.setEstadoClasificacion(ConstantesGenerales.ESTADO_ACTIVO);
					articuloDTO.setClasificacionDTO(clasificacionDTO);
				}else if(request.getParameter("opTipoBusqueda").equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreArticulo"))){
					//se consulta por descripci\u00F3n del art\u00EDculo
					articuloDTO.setDescripcionArticulo(request.getParameter("textoBusqueda"));
				}else if(request.getParameter("opTipoBusqueda").equals("ma")){
					//se consulta por marca del art\u00EDculo
					////articuloDTO.setMarcaArticulo(request.getParameter("textoBusqueda"));
					articuloComercialDTO = new ArticuloComercialDTO();
					marcaArticuloDTO = new MarcaArticuloDTO();						
					marcaArticuloDTO.setNombre(request.getParameter("textoBusqueda"));
					articuloComercialDTO.setMarcaComercialArticulo(marcaArticuloDTO);
					articuloDTO.setArticuloComercialDTO(articuloComercialDTO);
					
					
				}					
			}

			//Si la accion es pedidos especiales solo busca articulos especiales
			if(accion!=null && accion.equals(accionCrearPedidoEspecial)){
				
				//Asigno un valor a la sesion para restringir la busqueda
				session.setAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL, "ok");
				int indiceEspecial = ((Integer)session.getAttribute(CrearPedidoAction.INDICE_ESPECIAL));
				LogSISPE.getLog().info("indice Buscar: {}", indiceEspecial);
				//tomo de sesion la colecci\u00F3n de pedidos especiales
				ArrayList<EspecialDTO> especialDTOcol = (ArrayList<EspecialDTO>)session.getAttribute(CrearPedidoAction.COL_TIPO_ESPECIAL);
				EspecialDTO especialDTO = especialDTOcol.get(indiceEspecial);
				
				//se obtiene el c\u00F3digo del par\u00E1metro para el c\u00F3digo de especiales
				String codigoEspecialCarnes = (String)session.getAttribute(CrearPedidoAction.CODIGO_ESP_CARNES);
				if(codigoEspecialCarnes == null){
					//parametro que indica cu\u00E1ntos d\u00EDas debe haber entre despacho y entrega
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoEspecialCarnes", request);
					codigoEspecialCarnes = parametroDTO.getValorParametro();
					session.setAttribute(CrearPedidoAction.CODIGO_ESP_CARNES, codigoEspecialCarnes);
				}
				
				//si busca un art\u00EDculo en carnes
				LogSISPE.getLog().info("codigo especial pedido: {}", especialDTO.getId().getCodigoEspecial());
				LogSISPE.getLog().info("codigo especial carnes: {}", codigoEspecialCarnes);
				
				if(especialDTO.getId().getCodigoEspecial().equals(codigoEspecialCarnes))
				{
					LogSISPE.getLog().info("busca solo carnes");
					clasificacionDTO.setCodigoClasificacionPadre(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDepartamentoCarnes"));
					//se crea el objeto subClasificaci\u00F3n para la b\u00FAsqueda
					SubClasificacionDTO subClasificacionDTO = new SubClasificacionDTO();
					subClasificacionDTO.setDescripcionSubClasificacion("");
					String descripcionComunSubCla = (String)session.getAttribute(CrearPedidoAction.DESC_COMUN_SUBCLA_FILTRO_CARNES);

					if(descripcionComunSubCla == null){
						//se obtiene el par\u00E1metro de la descripci\u00F3n com\u00FAn para las clasificaciones
						ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.descripcionFiltroSubclasificacionCarnes", request);
						if(parametroDTO.getValorParametro() != null){
							descripcionComunSubCla = parametroDTO.getValorParametro();
							session.setAttribute(CrearPedidoAction.DESC_COMUN_SUBCLA_FILTRO_CARNES, descripcionComunSubCla);
							subClasificacionDTO.setDescripcionSubClasificacion(descripcionComunSubCla);
						}
					}else{
						subClasificacionDTO.setDescripcionSubClasificacion(descripcionComunSubCla);
					}
					articuloDTO.setNpConsultarArticulosCarnes(ConstantesGenerales.ESTADO_ACTIVO);
					articuloDTO.setSubClasificacionDTO(subClasificacionDTO);
				}
				articuloDTO.setClasificacionDTO(clasificacionDTO);
				articuloDTO.setNpCodigoEspecial(especialDTO.getId().getCodigoEspecial());
				session.setAttribute(BUSQUEDA_PED_ESPECIALES, "ok");
			}

			try{ 
				formulario.setDatos(null);
				//solo si no se generaron mensajes
				if(errors.isEmpty()){
					int start= 0;
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));

					//se llama al m\u00E9todo que construye la consulta de art\u00EDculos
					articulos = construirConsultaArticulos(request, articuloDTO,ConstantesGenerales.ESTADO_INACTIVO, start, range);
					
					if(articulos== null || articulos.isEmpty()){
						//se muestra el mensaje que indica que la lista esta vacia
						infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos"));
					}else{
						LogSISPE.getLog().info("ENTRO A LA PAGINACION");
						int size= ((Integer)session.getAttribute(TAMANOCONSULTAARTICULOS)).intValue();	
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));
						formulario.setDatos(articulos);
					}
					//la busqueda se almacena en una variable de sesi\u00F3n
					session.setAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA,articulos);
					//Guardo el articulo con los parametros de la consulta para la busqueda en base
					session.setAttribute(ARTICULOCONSULTA, articuloDTO);
				}else
					session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
			}catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Art\u00EDculos"));
				
				if(ex.getCause() != null && ex.getCause() instanceof SICException && ((SICException)ex.getCause()).getDescripcionError() != null){
					errors.add("errorSic",new ActionMessage("errors.gerneral",((SICException)ex.getCause()).getDescripcionError()));
				}
			}
			//Cambiar al tab de pedidos
			//ContactoUtil.cambiarTabPedidos(beanSession);
			//this.cambiarAlTabPedido(session, request, beanSession);
			
			//se elimina la variable que indica que hubo una b\u00FAsqueda por cat\u00E1logo
			session.removeAttribute(BUSQUEDA_POR_CATALOGO);
			//INSTANCIAR EL POPUP
			//instanciarVentanaBusquedaArticulos(request, accionEstado);
			session.setAttribute(POPUP_BUSQUEDA_ARTICULOS, "ok");
			session.setAttribute("ec.com.smx.sic.sispe.cotizarReservarForm", formulario);
			//salida = "listado";
		}

		/*---------------------- cuando se acepta para a\u00F1adir los productos seleccionados --------------------*/
		else if(request.getParameter("agregarArticulos")!=null && request.getParameter("agregarArticulos").equals("AP")){
			
			//PARA ESTA FORMA DE AGREGAR ARTICULOS NO HACE FALTA LLAMAR AL METODO QUE ACTUALIZA LOS DESCUENTOS PORQUE 
			//LUEGO SE LLAMA A ACTUALIZAR EL DETALLE DEL PEDIDO DE LA CLASE CrearCotizacionAction

			//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
			List detallePedido = (List)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
			
			//se recupera de sesion el pedido consolidado
			Collection<DetallePedidoDTO> detallePedidoConsolidado = (Collection<DetallePedidoDTO>)session.getAttribute(CotizarReservarAction.DETALLES_PEDIDOS_CONSOLIDADOS);
			
			//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos del pedido
			List codigosArticulosPedido = (List)session.getAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS);

			//se obtiene de sesi\u00F3n el detalle de la canasta
			//List detalleCanasta = (List)session.getAttribute(DetalleCanastaAction.COL_DETALLE_RECETA);
			//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos de la canasta
			//Collection codigosArticulosCanasta = (Collection)session.getAttribute(DetalleCanastaAction.COL_CODARTICULOS_DETALLE_RECETA);
			//se obtienen las cantidades originales almacenadas en sesi\u00F3n
			//Collection cantidadesOriginalesCanasto = (Collection)session.getAttribute(DetalleCanastaAction.COL_CANT_ORIGINALES_RECETA);

			List detalleCanasta = null;
			Collection codigosArticulosCanasta = null;
			Collection cantidadesOriginalesCanasto = null;
			
			//se obtiene los articulos desplegados en el formulario de resultado de la b\u00FAsqueda
			List articulos = (List)formulario.getDatos();
			//variable de sesi\u00F3n que me indica en que colecci\u00F3n debo almacenar los articulos que se han escogido
			//si no es nula significa que almaceno en la colecci\u00F3n del pedido
			String busquedaEnDetallePrincipal = (String)session.getAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL);

//			String codigoTipoArticuloRecetasCanasta = (String)session.getAttribute(COD_TIPO_ARTICULO_CANASTA);
//			String codigoTipoArticuloRecetasDespensa = (String)session.getAttribute(COD_TIPO_ARTICULO_DESPENSA);
//			if(codigoTipoArticuloRecetasCanasta == null){
//				//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos
//				codigoTipoArticuloRecetasCanasta = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//				session.setAttribute(COD_TIPO_ARTICULO_CANASTA, codigoTipoArticuloRecetasCanasta);
//			}
//			if(codigoTipoArticuloRecetasDespensa == null){
//				//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos
//				codigoTipoArticuloRecetasDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
//				session.setAttribute(COD_TIPO_ARTICULO_DESPENSA, codigoTipoArticuloRecetasDespensa);
//			}
			
			double totParcialCanasta = 0;
			if(session.getAttribute(DetalleCanastaAction.TOTAL_RECETA_A_SUMAR)!=null){
				totParcialCanasta = ((Double)session.getAttribute(DetalleCanastaAction.TOTAL_RECETA_A_SUMAR)).doubleValue();
			}
			//variables para el control del mensaje a mostrar
			int contadorArticulosProblemas = 0;
			int cantidadTotalArticulos = articulos.size();
			//bandera para determinar si se agregaron art\u00EDculos al detalle principal mediante la b\u00FAsqueda
			boolean articulosAgregados = false;
			try	{
				//se itera el arreglo de String OpEscogerProdBuscados     
				String seleccionados [] = formulario.getOpEscogerProdBuscados();
				if(seleccionados!=null){
					
					String articulosDeBaja = "";
					StringBuffer articulosObsoletos = null;
					
					//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
					String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
					session.setAttribute(CotizarReservarAction.CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);
					
					if(busquedaEnDetallePrincipal!=null) //cuando se busca el pedido principal
					{
						//se itera la colecci\u00F3n de indices
						ArrayList <ArticuloDTO> articulosSeleccionados = new ArrayList <ArticuloDTO>();
						ArrayList <ArticuloDTO> articulosNoValidos = new ArrayList <ArticuloDTO>();
						for(int i=0;i<seleccionados.length;i++){
							ArticuloDTO articuloDTO = (ArticuloDTO)articulos.get(Integer.parseInt(seleccionados[i]));
							if(articuloDTO.getTipoCalculoPrecio()!=null){
								//llamada al m\u00E9todo que asigna los campos necesarios para la consulta de art\u00EDculos
								WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, ConstantesGenerales.ESTADO_INACTIVO, ConstantesGenerales.ESTADO_INACTIVO, accion);
								articulosSeleccionados.add(articuloDTO);
							}else{
								articulosNoValidos.add(articuloDTO);
							}
						}
						//se modifica la cantidad total de art\u00EDculos seleccionados
						cantidadTotalArticulos = articulosSeleccionados.size();
						if(articulosNoValidos.size()>0){
							if(seleccionados.length>1){
								String codigosBarras="";
								for(ArticuloDTO articuloDTO :articulosNoValidos){
									codigosBarras=codigosBarras.concat(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()).concat(", ");
								}
								warnings.add("articuloNoValido", new ActionMessage("errors.articuloNoValido",codigosBarras.substring(0, codigosBarras.length()-2)));
							}else{
								errors.add("articuloNoValido", new ActionMessage("errors.articuloNoValido",articulosNoValidos.get(0).getCodigoBarrasActivo().getId().getCodigoBarras()));
							}
						}

						try{
							//se llama al m\u00E9todo del servicio que obtiene el stock y alcance de art\u00EDculos seleccionados
							SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosSeleccionados);
							
							//se itera la colecci\u00F3n de art\u00EDculos seleccionados
							for (ArticuloDTO articuloDTO : articulosSeleccionados) {
								if(!codigosArticulosPedido.contains(articuloDTO.getId().getCodigoArticulo())
//										|| articuloDTO.getCodigoTipoArticulo().equals(codigoTipoArticuloRecetasCanasta)
//										|| articuloDTO.getCodigoTipoArticulo().equals(codigoTipoArticuloRecetasDespensa)){
										|| articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) 
										|| articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
									//se verifica el estado del art\u00EDculo
									if(articuloDTO.getNpEstadoArticuloSIC()==null ||
											!articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
										
										//Iniciar validacion de descuentos por articulo
										CotizacionReservacionUtil.iniciarDescuentoPavos(request,articuloDTO);
										//llamada al m\u00E9todo que construye el detalle del pedido
										DetallePedidoDTO detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(1L, detallePedido.size(), articuloDTO, request,errors);
										if(detallePedidoDTO!=null && !CotizacionReservacionUtil.canastoConRecetaSinTemporadaActiva(detallePedidoDTO.getArticuloDTO(), errors)){
											detallePedido.add(detallePedidoDTO);
											
											//aniadir al pedido consolididao el detalle ingresado
											if(detallePedidoConsolidado != null){
												detallePedidoConsolidado.add(detallePedidoDTO);
											}
											
											LogSISPE.getLog().info("se agreg\u00F3 un registro al detallePedido");
											//se a\u00F1aden los c\u00F3digos de los art\u00EDculos a la colecci\u00F3n
											codigosArticulosPedido.add(articuloDTO.getId().getCodigoArticulo());
		
											articulosAgregados = true;
									}else if(errors.isEmpty()){
										errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo con codigo de barras: "+articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()+", es probable que existan problemas con la informaci\u00F3n registrada"));
									}
	
									}else{
										articulosDeBaja = articulosDeBaja.concat(articuloDTO.getId().getCodigoArticulo()).concat(", ");
										contadorArticulosProblemas++;
									}
									//si el articulo se encuentra como obsoleto en el SIC
									if(articuloDTO.getClaseArticulo() != null && articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) 
											&& articuloDTO.getNpStockArticulo() < 1){
										if(articulosObsoletos == null){
											articulosObsoletos = new StringBuffer();
										}
										articulosObsoletos.append(articuloDTO.getId().getCodigoArticulo()).append(", ");
									}
								}else{
									//se agregar el mensaje de detalle repetido
									errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetido",articuloDTO.getDescripcionArticulo()));
								}
							}
							
							//si encuentra art\u00EDculos obsoletos
							if(articulosObsoletos != null){
								errors.add("articulosObsoletos", new ActionMessage("errors.stock.articulo.obsoleto.cotizacion", articulosObsoletos.toString()));
							}
							
							//se agrega un mensaje de error
							if(!articulosDeBaja.equals("")){
								errors.add("articulosDeBaja", new ActionMessage("errors.agregarGrupoArticulosDeBaja", articulosDeBaja));
							}
							
						}catch(Exception ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
							errors.add("errorSIC", new ActionMessage("errors.agregarGrupoArticulosSIC"));
						}
					}
					else  //cuando se busca en el detalle del canasto
					{
						DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)session.getAttribute(DetalleCanastaAction.RECETA_SELECCIONADA);
						detalleCanasta = (List)detallePedidoDTO.getArticuloDTO().getArticuloRelacionCol();
						//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos de la canasta
						codigosArticulosCanasta = (Collection)session.getAttribute(DetalleCanastaAction.COL_CODARTICULOS_DETALLE_RECETA);
						//se obtienen las cantidades originales almacenadas en sesi\u00F3n
						cantidadesOriginalesCanasto = (Collection)session.getAttribute(DetalleCanastaAction.COL_CANT_ORIGINALES_RECETA);
						
						String [] codigosArticulos = new String [seleccionados.length];
						//se forma el arreglo de c\u00F3digos de art\u00EDculos a agregar
						for(int i=0;i<seleccionados.length;i++){
							ArticuloDTO articuloDTO = (ArticuloDTO)articulos.get(Integer.parseInt(seleccionados[i]));
							codigosArticulos[i] = articuloDTO.getId().getCodigoArticulo();
						}

						if(DetalleCanastaAction.validarAdicion(request, codigosArticulos, detallePedidoDTO)){
							ArticuloDTO canasto = detallePedidoDTO.getArticuloDTO();
							//se itera la colecci\u00F3n de indices
							ArrayList articulosSeleccionados = new ArrayList();
							for(int i=0;i<seleccionados.length;i++){
								ArticuloDTO articuloDTO = (ArticuloDTO)articulos.get(Integer.parseInt(seleccionados[i]));
								//llamada al m\u00E9todo que asigna los campos necesarios para la consulta de art\u00EDculos
								WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, ConstantesGenerales.ESTADO_ACTIVO, canasto.getId().getCodigoArticulo(), accion);
								articuloDTO.setNpSecuencialEstadoPedido(null);
								articulosSeleccionados.add(articuloDTO);
							}
							//se modifica la cantidad total de art\u00EDculos seleccionados
							cantidadTotalArticulos = articulosSeleccionados.size();

							try{
								//se llama al m\u00E9todo del servicio que obtiene el stock y alcance de art\u00EDculos seleccionados
								SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosSeleccionados);
								
								//se itera la colecci\u00F3n de articulos para los art\u00EDculos seleccionados
								for(int i=0;i<articulosSeleccionados.size();i++){
									ArticuloDTO articuloDTO = (ArticuloDTO)articulosSeleccionados.get(i);
									//se obtiene la cantidad solicitada del canasto
									//la comparaci\u00F3n se hace como sis se multiplicara [1 * cantidadCanasto]
									Long cantidadCanasto = ((DetallePedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.receta.detallePedidoDTO")).getEstadoDetallePedidoDTO().getCantidadEstado();
									if(articuloDTO.getNpStockArticulo()!=null && articuloDTO.getNpStockArticulo().longValue() < cantidadCanasto.longValue()){
										//cuando el art\u00EDculo ya est\u00E1 en la lista ocurre un error
										articuloDTO.setNpEstadoStockArticulo(ConstantesGenerales.ESTADO_INACTIVO);
									}
									
									//solo si el articulo se encuentra marcado se a\u00F1ade al detalle
									if(!codigosArticulosCanasta.contains(articuloDTO.getId().getCodigoArticulo())){
										
										//se verifica el estado del art\u00EDculo
										if(articuloDTO.getNpEstadoArticuloSIC()==null ||
												!articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
											//llamada al m\u00E9todo que genera el objeto RecetaArticuloDTO
											ArticuloRelacionDTO recetaArticuloDTO = CotizacionReservacionUtil.construirNuevaReceta(articuloDTO,new Long(1),request);												
											//totParcialCanasta = totParcialCanasta + recetaArticuloDTO.getPrecioTotalIVA().doubleValue();
											totParcialCanasta = totParcialCanasta + recetaArticuloDTO.getPrecioTotalIMP().doubleValue();
											
											detalleCanasta.add(recetaArticuloDTO);
											codigosArticulosCanasta.add(articuloDTO.getId().getCodigoArticulo());
											//se actualiza la lista de cantidades originales
											cantidadesOriginalesCanasto.add(new Long(1));
	
											//colecci\u00F3n de items eliminados de la colecci\u00F3n "detallePedidoDTO.getItemsEliminadosReceta()"
											Collection <String> itemsAEliminarDeEliminados = (Collection <String>)session.getAttribute(DetalleCanastaAction.ITEMS_A_ELIMINAR_DE_ELIMINADOS);
											if(itemsAEliminarDeEliminados!=null && !itemsAEliminarDeEliminados.contains(articuloDTO.getId().getCodigoArticulo()))
												detallePedidoDTO.getItemsAgregadosReceta().add(articuloDTO.getId().getCodigoArticulo());
											
										}else{
											articulosDeBaja = articulosDeBaja.concat(articuloDTO.getId().getCodigoArticulo()).concat(", ");
											contadorArticulosProblemas ++;
										}
										
										//si el articulo se encuentra como obsoleto en el SIC
										if(articuloDTO.getClaseArticulo() != null && articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) 
												&& articuloDTO.getNpStockArticulo() < 1){
											if(articulosObsoletos == null){
												articulosObsoletos = new StringBuffer();
											}
											articulosObsoletos.append(articuloDTO.getId().getCodigoArticulo()).append(", ");
										}
									}else{
										//se agregar el mensaje de detalle repetido
										errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetidoCanasta",articuloDTO.getDescripcionArticulo()));
									}
								}
//								si encuentra art\u00EDculos obsoletos
								if(articulosObsoletos != null){
									errors.add("articulosObsoletos", new ActionMessage("errors.stock.articulo.obsoleto.cotizacion", articulosObsoletos.toString()));
								}
								
								//se agrega un mensaje de error
								if(!articulosDeBaja.equals("")){
									errors.add("articulosDeBaja", new ActionMessage("errors.agregarGrupoArticulosDeBaja", articulosDeBaja));
								}
								
							}catch(SISPEException ex){
								LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
								errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
								errors.add("errorSIC", new ActionMessage("errors.agregarGrupoArticulosSIC"));
							}
							
						}else{
							//se verifica el par\u00E1metro para el m\u00E1ximo de modificaciones
							String porcentajeModificacionItemsCanasto = (String)session.getAttribute(DetalleCanastaAction.PORC_MODIFICACION_RECETA);
							//se genera un mensaje de error
							errors.add("itemNoAgregado", new ActionMessage("info.canasto.modificacionArticulos", 
									Double.parseDouble(porcentajeModificacionItemsCanasto) + "%", detallePedidoDTO.getNpCanMaxModIteRec()));
						}
					}

					//solo si se pudieron agregar art\u00EDculos (esta condici\u00F3n es para los canastos)
					if(errors.isEmpty()){
						if(articulosAgregados){
							session.setAttribute(ART_AGREGADOS_BUSQUEDA_PED, "ok");
							
							//se actualiza los detalles consolidados
							ConsolidarAction.crearDetalleConsolidadosNoRepetidos(request, detallePedidoConsolidado);
						}
						//se actualizan los totales en sesi\u00F3n
						//session.setAttribute(SUB_TOTAL_APLICAIVA_A_SUMAR, new Double(subTotParcialPedidoAplicaIVA));
						//session.setAttribute(SUB_TOTAL_NOAPLICAIVA_A_SUMAR, new Double(subTotParcialPedidoNoAplicaIVA));
						session.setAttribute(DetalleCanastaAction.TOTAL_RECETA_A_SUMAR, Double.valueOf(totParcialCanasta));

						//se actualizan las colecciones en sesi\u00F3n
						session.setAttribute(CotizarReservarAction.DETALLE_PEDIDO, detallePedido);
						session.setAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS, codigosArticulosPedido);
						//session.setAttribute(DetalleCanastaAction.COL_DETALLE_RECETA, detalleCanasta);
						session.setAttribute(DetalleCanastaAction.COL_CODARTICULOS_DETALLE_RECETA, codigosArticulosCanasta);
						session.setAttribute(DetalleCanastaAction.COL_CANT_ORIGINALES_RECETA, cantidadesOriginalesCanasto);

						//se crea el mensaje de acuerdo al contador
						if(contadorArticulosProblemas > 0){
							if(contadorArticulosProblemas == cantidadTotalArticulos){
								//mensaje de error, ning\u00FAn art\u00EDculo se agreg\u00F3
								errors.add("articulosDeBaja", new ActionMessage("errors.agregarGrupoArticulosDeBaja", articulosDeBaja));
							}else{
								//mensaje de advertencia, solo algunos art\u00EDculos se agregaron
								warnings.add("articulosDeBaja", new ActionMessage("errors.agregarGrupoArticulosDeBaja", articulosDeBaja));
							}
						}else{
							//mensaje de exito, todos los art\u00EDculos fueron agregados al pedido o a la receta
							exitos.add("agregarArticulos",new ActionMessage("message.exito.articulosAgregados"));
						}
						formulario.setOpEscogerProdBuscados(null);
						formulario.setOpEscogerTodos(null);
					}
				}else{
					errors.add("ningunSeleccionado", new ActionMessage("errors.seleccion.requerido", "un art\u00EDculo"));
				}
			}catch(SISPEException ex){
				errors.add("conexion_sic",new ActionMessage("errors.SISPEException",ex.getMessage()));
			}
			//Cambiar al tab de pedidos
			//ContactoUtil.cambiarTabPedidos(beanSession);
			cambiarAlTabPedido(session, request, beanSession);
		}
		else if(request.getParameter("agregarArticulos")!=null 
				&& request.getParameter("agregarArticulos").equals("APE")){
			
			LogSISPE.getLog().info("AGREGAR ARTICULOS PARA EL DETALLE DEL PEDIDO ESPECIAL");
			
			//bajamos de sesion el formulario con los datos
//			formulario = (CotizarReservarForm) session.getAttribute("ec.com.smx.sic.sispe.cotizarReservarForm");

			//se obtiene la colecci\u00F3n de art\u00EDculos agregados por la b\u00FAsqueda
			ArrayList<DetallePedidoDTO> detallePedidoDTOCol = (ArrayList<DetallePedidoDTO>)session.getAttribute(CrearPedidoAction.DETALLE_PEDIDO);
			//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos del pedido
			ArrayList codigosArticulosPedido = (ArrayList)session.getAttribute(CrearPedidoAction.COL_CODIGOS_ARTICULOS);
			if(detallePedidoDTOCol==null){
				detallePedidoDTOCol=new ArrayList<DetallePedidoDTO>();
			}
			//se obtiene de sesi\u00F3n los articulos desplegados en el formulario.
			ArrayList<ArticuloDTO> articulos = (ArrayList<ArticuloDTO>)formulario.getDatos();
			//se itera la colecci\u00F3n de indices
			ArrayList <ArticuloDTO> articulosSeleccionados = new ArrayList <ArticuloDTO>();
			String seleccionados [] = formulario.getOpEscogerProdBuscados();
			try{
				if(seleccionados!=null)
				{
					for(int i=0;i<seleccionados.length;i++){
						ArticuloDTO articuloDTO = (ArticuloDTO)articulos.get(Integer.parseInt(seleccionados[i]));
						//llamada al m\u00E9todo que asigna los campos necesarios para la consulta de art\u00EDculos
						WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, ConstantesGenerales.ESTADO_INACTIVO, ConstantesGenerales.ESTADO_INACTIVO, accion);
						articulosSeleccionados.add(articuloDTO);
					}

					//se llama al m\u00E9todo del servicio que obtiene el stock y alcance de art\u00EDculos seleccionados
					SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosSeleccionados);
					
					LogSISPE.getLog().info("va a agregar los articulos");
					//se itera la colecci\u00F3n de art\u00EDculos seleccionados
					for (ArticuloDTO articuloDTO : articulosSeleccionados) {
						if(!codigosArticulosPedido.contains(articuloDTO.getId().getCodigoArticulo())){
							//llamada al m\u00E9todo que construye el detalle del pedido
							LogSISPE.getLog().info("*****detalle:***** {}" , detallePedidoDTOCol.size());
							LogSISPE.getLog().info("*****articulo:***** {}", articuloDTO.getId().getCodigoArticulo());
							DetallePedidoDTO detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido(0L,detallePedidoDTOCol.size(),articuloDTO,request,errors);
							if(detallePedidoDTO!=null){
								detallePedidoDTOCol.add(detallePedidoDTO);
								LogSISPE.getLog().info("se agreg\u00F3 un registro al detallePedido");
								//se a\u00F1aden los c\u00F3digos de los art\u00EDculos a la colecci\u00F3n
								codigosArticulosPedido.add(articuloDTO.getId().getCodigoArticulo());
							}else{
								errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo con codigo de barras: "+articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()+", es probable que existan problemas con la informaci\u00F3n registrada"));
							}
						}
					}

					//mensaje de exito, todos los art\u00EDculos fueron agregados al pedido o a la receta
					exitos.add("agregarArticulos",new ActionMessage("message.exito.articulosAgregados"));

					formulario.setOpEscogerProdBuscados(null);
					formulario.setOpEscogerTodos(null);

				}else{
					errors.add("ningunSeleccionado", new ActionMessage("errors.seleccion.requerido", "un art\u00EDculo"));
				}
			}catch(SISPEException ex){
				errors.add("conexion_sic",new ActionMessage("errors.SISPEException",ex.getMessage()));
			}
			//Cambiar al tab de pedidos
			//ContactoUtil.cambiarTabPedidos(beanSession);
			cambiarAlTabPedido(session, request, beanSession);
		}

		/*-------------------------------------- cuando se cancela la busqueda ----------------------------------------*/
		else if(formulario.getBotonCancelarArt()!=null){
			//se eliminan de sesi\u00F3n las variables que no se necesitan
			session.removeAttribute(CatalogoArticulosAction.DIVISION_CATALOGO);
			session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
		}
		
		else if(request.getParameter("atras") != null){
			LogSISPE.getLog().info("Regresa del listado de los articulos al catalogo de productos");
			session.removeAttribute(POPUP_BUSQUEDA_ARTICULOS);
			session.setAttribute(POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL, "ok");
		}
		
		
		
		/*-------------------Metodos para la busqueda de articulos por estructura comercial----------------------------*/
		
		/*------------------------ cuando se selecciona una descripcion del cat\u00E1logo --------------------------------*/
		else if(request.getParameter("numeroRegistro")!=null)
		{
			int numeroRegistro=0;
			Collection buscados = new ArrayList();
			Collection articulos = new ArrayList();

			try
			{
				numeroRegistro = Integer.parseInt(request.getParameter("numeroRegistro"));
				String nivelActual = request.getParameter("descripcionNivel");

				/*------------------------------- cuando estamos en el nivel DIVISION ------------------------------------*/
				if(nivelActual.equals(TITULO_DIVISION))
				{
					LogSISPE.getLog().info("OBTENER DEPARTAMENTOS");
					//se obtiene la lista que almacena las divisiones
					//ArrayList division = (ArrayList)session.getAttribute(COL_DIVISIONES);
					ArrayList divisiones = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
					ClasificacionDTO divisionDTO  = (ClasificacionDTO)divisiones.get(numeroRegistro);
					//session.setAttribute(BuscarArticuloAction.DIVISION_CATALOGO, divisionDTO);

					session.setAttribute(NIVEL_CATALOGO,TITULO_DEPARTAMENTO);
					session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,request.getParameter("clasificacion")+" "
							+divisionDTO.getId().getCodigoClasificacion()+"-"+divisionDTO.getDescripcionClasificacion()+"/");

					//proceso para obtener la clasificaci\u00F3n de departamentos que se almacena en la colecci\u00F3n buscados
					ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
					clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
					clasificacionConsultaDTO.setCodigoClasificacionPadre(divisionDTO.getId().getCodigoClasificacion());
					clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.departamento"));
					clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
							.getString("ec.com.smx.sic.sispe.estado.activo"));
					clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
					
					buscados = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);
					if(buscados==null || buscados.isEmpty()){
						//se muestra un mensaje indicando que la lista est\u00E1 vacia
						infos.add("listaVacia",new ActionMessage("message.listaVacia","Departamentos dentro de la divisi\u00F3n "+divisionDTO.getDescripcionClasificacion()));
					}
					//variable de sesion que almacena la clasificaci\u00F3n obtenida
					session.setAttribute(CATALOGO_ACTUAL,buscados);
					//session.setAttribute("ec.com.smx.sic.sispe.catalogoArticulos.departamentos",buscados);
					
					session.setAttribute(POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL, "ok");

					//salida="catalogo";XXX
				}
				/*------------------------------ cuando estamos en el nivel DEPARTAMENTO ----------------------------------*/
				else if(nivelActual.equals(TITULO_DEPARTAMENTO))
				{
					LogSISPE.getLog().info("OBTENER CLASIFICACIONES");
					//lista que almacena los departamentos de la ultima division escogida. 
					ArrayList departamentos = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
					session.setAttribute(CATALOGO_ANTERIOR,departamentos);

					//se obtiene el departamento que selecciono el usuario y se la almacena en una variable de sesi\u00F3n
					ClasificacionDTO departamentoDTO = (ClasificacionDTO)departamentos.get(numeroRegistro);

					session.setAttribute(NIVEL_CATALOGO, TITULO_CLASIFICACION);
					session.setAttribute(RUTA_EST_COMERCIAL_ANTERIOR,request.getParameter("clasificacion"));
					session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,request.getParameter("clasificacion")+" "
							+departamentoDTO.getId().getCodigoClasificacion()+"-"+departamentoDTO.getDescripcionClasificacion()+"/");

					//proceso para sacar la lista de clases que se almacenar\u00E1 en la colecci\u00F3n buscados
					ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
					clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
					clasificacionConsultaDTO.setCodigoClasificacionPadre(departamentoDTO.getId().getCodigoClasificacion());
					clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.clase"));
					clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
							.getString("ec.com.smx.sic.sispe.estado.activo"));
					clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
					
					buscados = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);

					if(buscados==null || buscados.isEmpty()){
						//se muestra un mensaje indicando que la lista est\u00E1 vacia
						infos.add("listaVacia",new ActionMessage("message.listaVacia","Clases dentro del Departamento "+departamentoDTO.getDescripcionClasificacion()));
					}

					//variable de sesion que almacena la clasificaci\u00F3n obtenida
					session.setAttribute(CATALOGO_ACTUAL,buscados);
					
					session.setAttribute(POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL, "ok");
					//salida="catalogo";XXX

				}
				/*-------------------- cuando estamos en el nivel CLASIFICACION --------------------*/
				else if(nivelActual.equals(TITULO_CLASIFICACION))
				{
					LogSISPE.getLog().info("OBTENER ARTICULOS");

					ArrayList clasificaciones = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
					//session.setAttribute(CATALOGO_ANTERIOR,clasificaciones);
					ClasificacionDTO clasificacionDTO = (ClasificacionDTO)clasificaciones.get(numeroRegistro);
					int start= 0;
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
					//objeto DTO que servir\u00E1 para obtener los articulos de la clase escogida
					ArticuloDTO articuloDTO = new ArticuloDTO();
					articuloDTO.setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
					try{
						articulos = BuscarArticuloAction.construirConsultaArticulos(request, articuloDTO, ConstantesGenerales.ESTADO_INACTIVO, start, range);
						formulario.setDatos(null);
						if(articulos==null || articulos.isEmpty()){
							//se muestra un mensaje indicando que la lista est\u00E1 vacia
							infos.add("listaVacia",new ActionMessage("message.listaVacia","Art\u00EDculos dentro de la Clase "+clasificacionDTO.getDescripcionClasificacion()));
						}else{
							LogSISPE.getLog().info("ENTRO A LA PAGINACION");
							int size= ((Integer)session.getAttribute(TAMANOCONSULTAARTICULOS)).intValue();
							formulario.setStart(String.valueOf(start));
							formulario.setRange(String.valueOf(range));
							formulario.setSize(String.valueOf(size));
							formulario.setDatos(articulos);
							//Guardo el articulo con los parametros de la consulta para la busqueda en base
							session.setAttribute(ARTICULOCONSULTA, articuloDTO);
						}
					}catch(SISPEException ex){
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("Articulos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Art\u00EDculos"));
					}
					//variable de sesi\u00F3n que almacenar\u00E1 la lista de articulos. 
					session.setAttribute(ARTICULOS_AGREGADOS_BUSQUEDA, articulos);
					session.setAttribute(BuscarArticuloAction.BUSQUEDA_POR_CATALOGO,"ok");
					
					session.setAttribute(POPUP_BUSQUEDA_ARTICULOS, "ok");
					session.removeAttribute(POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL);
					//salida="listaArticulos";XXX
				}
			}catch(NumberFormatException e)
			{
				errors.add("formatoIndice",new ActionMessage("errors.indiceDetalle.formato"));
			}
		}
		//------------ cuando se hace clic en el bot\u00F3n Atras -----------
		else if(request.getParameter("atrasArticulos")!=null){
			if(request.getParameter("nivel")!=null){
				Collection listadoAnterior = (Collection)session.getAttribute(CATALOGO_ANTERIOR);
				session.setAttribute(CATALOGO_ACTUAL,listadoAnterior);
				if(request.getParameter("nivel").equals(TITULO_DEPARTAMENTO)){
					session.setAttribute(NIVEL_CATALOGO, TITULO_DIVISION);
					session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,"Estructura comercial: ");
				}
				if(request.getParameter("nivel").equals(TITULO_CLASIFICACION)){
					session.setAttribute(NIVEL_CATALOGO, TITULO_DEPARTAMENTO);
					String estructuraAnterior = (String)session.getAttribute(RUTA_EST_COMERCIAL_ANTERIOR);
					session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,estructuraAnterior);
					Collection divisiones = (Collection)session.getAttribute(COL_DIVISIONES);
					session.setAttribute(CATALOGO_ANTERIOR,divisiones);
				}
			}
		}
		//cuando se agregan las clasificaciones desde la pantalla de permisos
		else if(request.getParameter("agregarClasificacion")!=null){
			if(formulario.getChecksClasificaciones()!=null){
				ArrayList <ClasificacionDTO> clasificaciones = (ArrayList <ClasificacionDTO>)session.getAttribute(CATALOGO_ACTUAL);
				Collection <ClasificacionDTO> clasificacionesAgregadas = (Collection <ClasificacionDTO>)session.getAttribute(CLASIFICACIONES_AGREGADAS);
				if(clasificacionesAgregadas == null)
					clasificacionesAgregadas = new ArrayList<ClasificacionDTO>();
				
				int indice = 0; //indice de las clasificaciones
				for(int i=0; i<formulario.getChecksClasificaciones().length;i++){
					indice = Integer.parseInt(formulario.getChecksClasificaciones()[i]); 
					ClasificacionDTO clasificacionDTO = clasificaciones.get(indice);
					clasificacionesAgregadas.add(clasificacionDTO);
				}
				session.setAttribute(CLASIFICACIONES_AGREGADAS, clasificacionesAgregadas);
				//se genera un mensaje de exito
				exitos.add("clasificacionesAgregadas", new ActionMessage("message.exito.clasificacionesAgregadas"));
				formulario.setChecksClasificaciones(null);
			}else
				errors.add("noSeleccionados", new ActionMessage("errors.seleccion.requerido","una Clasificaci\u00F3n"));
		}
		
		//------------- acci\u00F3n por omisi\u00F3n -------------
		else
		{
			LogSISPE.getLog().info("POR OMISION");
			//creaci\u00F3n de la coleccion para el manejo de las divisiones.
			Collection division = new ArrayList();
			ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
			try
			{
				//Obtener datos de la colecci\u00F3n de clasificacion
				//buscar los datos del primer nivel
				clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
				clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.division"));
				clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
						.getString("ec.com.smx.sic.sispe.estado.activo"));
				clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
				
				division = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);
				if(division==null || division.isEmpty()){
					//se muestra un mensaje indicando que la lista est\u00E1 vacia
					infos.add("listaVacia",new ActionMessage("message.listaVacia","Divisiones de art\u00EDculos"));
				}
				session.setAttribute(COL_DIVISIONES,division);
				session.setAttribute(CATALOGO_ANTERIOR,division);
				session.setAttribute(CATALOGO_ACTUAL,division);
				session.setAttribute(NIVEL_CATALOGO,
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDivision"));
				session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,"Estructura comercial: ");
				session.setAttribute(POPUP_BUSQUEDA_ARTICULOS_ESTRUCTURA_COMERCIAL, "ok");
			}
			catch(SISPEException ex)
			{
				//se muestran los mensajes de error correspondientes
				errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Clasificaci\u00F3n de art\u00EDculos"));
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			}   
		}
		
		

	}
	catch(Exception ex){
		//excepcion desconocida
		LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
	}
	}
	
	/**
	 * Realiza la consulta de art\u00EDculos
	 * @param request					- La petici\u00F3n HTTP
	 * @param articuloDTO			- El objeto base de la consulta
	 * @param estadoInactivo	- String que representa un estado inactivo
	 * @return								-	Colecci\u00F3n resultado de la b\u00FAsqueda
	 */
	public static Collection construirConsultaArticulos(HttpServletRequest request, ArticuloDTO articuloDTO, 
			String estadoInactivo, Integer firstResult, Integer maxResults)throws Exception{
		
		HttpSession session = request.getSession();
		//variable de sesi\u00F3n que me indica en donde se est\u00E1 realizando la b\u00FAsqueda
		String busquedaEnDetallePrincipal = (String)session.getAttribute(CotizarReservarAction.BUSQUEDA_PRINCIPAL);
		String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//[!null: pedido principal, null: en el canasto]
		if(busquedaEnDetallePrincipal == null){
			//condici\u00F3n para que no se obtengan canastos
//			articuloDTO.addCriteriaSearchParameter("codigoTipoArticulo", ComparatorTypeEnum.NOT_IN_COMPARATOR,MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item").concat(",")
//					.concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.relacionado")));
			
			//condici\u00F3n para que no obtenga perecibles que no se deban agregar al canasto
			//articuloDTO.setEstadoPerecibleReceta(estadoInactivo);
			articuloDTO.setEstadoPerecibleRecetaFiltro(estadoInactivo);
		}

		LogSISPE.getLog().info("busquedaEnDetallePrincipal: {}",busquedaEnDetallePrincipal);
		//llamada al m\u00E9todo que construye la consulta
		WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, estadoInactivo, estadoInactivo, accionActual);
		//se asignan los par\u00E1metros para la paginaci\u00F3n en la base de datos
//		articuloDTO.setNpFirstResult(firstResult);
//		articuloDTO.setNpMaxResults(maxResults);
		
		articuloDTO.setFirstResult(firstResult);
		articuloDTO.setMaxResults(maxResults);
		
		//llamada al m\u00E9todo de la capa de servicio que devuelve la colecci\u00F3n de art\u00EDculos
		Collection articulos = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulo(articuloDTO);
		
		//llamada al m\u00E9todo que devuelve el tama\u00F1o de la coleccion total de art\u00EDculos
		int tamano=articuloDTO.getNpMaxResults();
		//guardo en sesion el tama\u00F1o
		request.getSession().setAttribute(TAMANOCONSULTAARTICULOS, new Integer(tamano));

		return articulos;
	}
	
	/**
	 * Este metodo cambia el foco del tab de personas al tab de pedidos
	 * @param session
	 * @param request
	 */
	public static void cambiarAlTabPedido(HttpSession session, HttpServletRequest request, BeanSession beanSession ){			
		//LogSISPE.getLog().info("el tab selecciondo es: {}, {} ",beanSession.getPaginaTab().getTabSeleccionado(),beanSession.getPaginaTab().getTituloTabSeleccionado());
		 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
			 LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
			 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);			
		 }
	}
}
