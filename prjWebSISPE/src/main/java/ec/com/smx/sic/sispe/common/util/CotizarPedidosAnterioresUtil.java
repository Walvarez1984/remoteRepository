package ec.com.smx.sic.sispe.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.BuscarArticuloAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CatalogoArticulosAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.ListadoPedidosAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

public class CotizarPedidosAnterioresUtil {
	/**
	 * Clase ofrece: 
	 * @author aesanchez
	 * 16:54:08
	 * version 0.1
	 * @throws Exception 
	 *  	
	 */
	//Ejecuta las acciones de pedidos anteriores 
	public static void accionesPedAnt(HttpServletRequest request, CotizarReservarForm formulario, ActionMessages errors, ActionMessages exitos,ActionMessages warnings,ActionMessages infos ) throws Exception{
		try {
			
			HttpSession session=request.getSession();
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			//Obtengo la accion actual
			String accion=(String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
			//se obtiene el bean que contiene los campos genericos
			BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
	
			//despliega la busqueda de pedidos anteriores
			if(request.getParameter("accionesPedAnt").equals("abrir")){
				LogSISPE.getLog().info("abrir pop up pedidos anteriores");
				session.setAttribute(CotizarReservarForm.PEDIDOS_ANTERIORES, "ok");
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pedidos.anteriores"));
				LogSISPE.getLog().info("Accion Actual: "+session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL));
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
				if(formulario.getNumeroDocumento()!=null && formulario.getTipoDocumento()!=null && 
						(formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC) 
						|| formulario.getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_INTERNACIONAL))){
					formulario.setRucEmpresaTxt(formulario.getNumeroDocumento());
					formulario.setRucEmpresa(formulario.getNumeroDocumento());
				}	
				else{
					formulario.setDocumentoPersonalTxt(formulario.getNumeroDocumento())	;
					formulario.setNumeroDocumentoPersona(formulario.getNumeroDocumento())	;	
				}
				formulario.setNumeroDocumentoContacto(formulario.getNumeroDocumento())	;	
				
			}
			//cierra la busqueda de pedidos anteriores
			else if(request.getParameter("accionesPedAnt").equals("cerrar")){
				LogSISPE.getLog().info("cerrar popup busqueda pedidos anteriores");
				session.removeAttribute(CotizarReservarForm.PEDIDOS_ANTERIORES);
				session.removeAttribute(ListadoPedidosAction.COL_SUB_PAGINA);
				formulario.setFechaInicial(null);
				formulario.setFechaFinal(null);
				session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
				
			}
			//cierra el listado de articulos de un pedido anterior
			else if(request.getParameter("accionesPedAnt").equals("cerrarpopup")){
				
				LogSISPE.getLog().info("cerrar popup listado articulos del popup busqueda pedidos anteriores");
				session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
				formulario.setOpEscogerProdBuscados(null);
				formulario.setOpEscogerTodos(null);
				session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO);
				
			}
			//busqueda de los pedidos anteriores
			else if(request.getParameter("accionesPedAnt").equals("buscarPedAnt")){
				LogSISPE.getLog().info("Ejecucion busqueda Pedidos Anteriores");
				//solo pedidos actuales
				formulario.setOpEstadoActivo(estadoActivo);
				String codigoInicialIds = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID");
				String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
			 
			//colecci\u00F3n que almacena los pedidos buscados	
		        Collection colVistaPedidoDTO = null;
		        VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
		      
				session.setAttribute(SessionManagerSISPE.ESTADO_ACTUAL, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		           //si no se asign\u00F3 un estado en particular se consulta por los siguientes
		        if(consultaPedidoDTO.getId().getCodigoEstado().equals(codigoInicialIds)){
		        	consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")
			        	.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
			            .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")));
		        }
		        LogSISPE.getLog().info("Estados de Pedido a buscar: "+consultaPedidoDTO.getId().getCodigoEstado());
		          //se asignan los campos de ordenamiento
		    	  consultaPedidoDTO.setNpCamposOrden(new String [][]{{"fechaInicialEstado",ListadoPedidosAction.ORDEN_ASCENDENTE}});
		          colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
		          if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
		            	//se muestra un mensaje indicando que la lista est\u00E1 vacia
		            	infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos Anteriores"));
		            	
		        	  session.removeAttribute(ListadoPedidosAction.COL_SUB_PAGINA);
		            }
		          else{
		              LogSISPE.getLog().info("ENTRO A LA PAGINACION");
		              formulario.setSize(String.valueOf(colVistaPedidoDTO.size()));
		              int size= colVistaPedidoDTO.size();
		              int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
		              int start= 0;
		              formulario.setStart(String.valueOf(start));
		              formulario.setRange(String.valueOf(range));
		              formulario.setSize(String.valueOf(size));
		              
		              Collection datosSub = Util.obtenerSubCollection(colVistaPedidoDTO,start, start + range > size ? size : start+range);
		             // ListadoPedidosAction.verificarAutorizacionesPedido(datosSub);
		              session.setAttribute(ListadoPedidosAction.COL_SUB_PAGINA,datosSub);
		             
		            }
		          session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
			}
			
			//busqueda de los articulos del pedido anteriores seleccionado
			else if(request.getParameter("accionesPedAnt").startsWith("buscarArtPedAnt")){
				LogSISPE.getLog().info("Busqueda de detalle de pedido seleccionado");
				Integer indice = null;
				//if (request.getParameter("indice") != null) {
				indice= Integer.valueOf(request.getParameter("accionesPedAnt").substring(16));
				//}
				LogSISPE.getLog().info("Indice Pedido: "+indice);
				VistaPedidoDTO consultaVistaPedidoDTO = null;
				
				//obtener de la sesion la colecci\u00F3n de los pedidos en general
				List pedidos = (List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
									
				if(pedidos!=null && !pedidos.isEmpty()){
					
					consultaVistaPedidoDTO = new VistaPedidoDTO();
					consultaVistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);									
					consultaVistaPedidoDTO.setNpPersona((PersonaDTO)request.getSession().getAttribute(ContactoUtil.PERSONA));				
					EstadoPedidoUtil.obtenerDetallesPedido(consultaVistaPedidoDTO, request);
				}
					
				//aqui se almacena la lista de articulos que se van a buscar
				Collection<ArticuloDTO> articulos = new ArrayList<ArticuloDTO>();
			
				formulario.setDatos(null);
				//solo si no se generaron mensajes
				if(errors.isEmpty()){
					
					int tamano=0;
					//se llama al m\u00E9todo que construye la consulta de art\u00EDculos
					Collection<VistaDetallePedidoDTO> vistas=consultaVistaPedidoDTO.getVistaDetallesPedidosReporte();
					
					if(consultaVistaPedidoDTO != null){
						
						if(CollectionUtils.isNotEmpty(consultaVistaPedidoDTO.getVistaDetallesPedidosReporte())){
							for(VistaDetallePedidoDTO vista : vistas){
								ArticuloDTO art=new ArticuloDTO();
								art=vista.getArticuloDTO();
								art.setNpCantidadTotalRegistros((int)(long)vista.getCantidadEstado());
								WebSISPEUtil.construirConsultaArticulos(request, art, estadoInactivo, estadoInactivo, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pedidos.anteriores"));
								art.setArticuloRelacionCol(new ArrayList<ArticuloRelacionDTO>());
								try{
									ArrayList <ArticuloDTO> articulosTemp = new ArrayList <ArticuloDTO>();
									articulosTemp.add(art);
									//se llama al m\u00E9todo del servicio que obtiene el stock y alcance de art\u00EDculos seleccionados
									SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulosTemp);
									if(art.getNpStockArticulo()!=null && art.getNpStockArticulo().longValue() < vista.getCantidadEstado().longValue()){
										LogSISPE.getLog().info("stock articulo CD{}",art.getNpStockArticulo().longValue());
										LogSISPE.getLog().info("stock articulo {}",vista.getCantidadEstado().longValue());
										art.setNpEstadoStockArticulo(estadoInactivo);					
										
										//stock obsoleto
										if(vista.getEstadoDetallePedidoDTO().getStoLocArtObs() != null){
											if(vista.getEstadoDetallePedidoDTO().getStoLocArtObs()!=0 && art.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto"))){
												art.setNpStockArticulo(vista.getEstadoDetallePedidoDTO().getStoLocArtObs().longValue());				
											}
										}
										
									}else{
										art.setNpEstadoStockArticulo(estadoActivo);
									}
									articulos.add(art);
									tamano+=1;
								}catch(Exception ex){
									art.setNpEstadoArticuloSIC(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"));
									articulos.add(art);
								}
							}
						}	
					}
					
					//guardo en sesion el tama\u00F1o
					request.getSession().setAttribute(BuscarArticuloAction.TAMANOCONSULTAARTICULOS, new Integer(tamano));
					
					if(articulos== null || articulos.isEmpty()){
						//se muestra el mensaje que indica que la lista esta vacia
						infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Art\u00EDculos"));
					}else{
						formulario.setDatos(articulos);	
					}
					//la busqueda se almacena en una variable de sesi\u00F3n
					session.setAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA,articulos);
				}else{
						session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
					}
				//se elimina la variable que indica que hubo una b\u00FAsqueda por cat\u00E1logo
				session.removeAttribute(BuscarArticuloAction.BUSQUEDA_POR_CATALOGO);
				
			}
	
			// procedimiento para agregar los articulos escogidos del pedido anterior a la nueva cotizacion
			else if(request.getParameter("accionesPedAnt").startsWith("agregarArticulos")){
				LogSISPE.getLog().info("Se va a agregar los articulos seleccionados al pedido");
					//se obtiene de sesi\u00F3n el detalle de la cotizaci\u00F3n
					List<DetallePedidoDTO> detallePedido = (List)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
					//se obtiene la colecci\u00F3n con los c\u00F3digos de los art\u00EDculos del pedido
					List codigosArticulosPedido = (List)session.getAttribute(CotizarReservarAction.COL_CODIGOS_ARTICULOS);
					VistaPedidoDTO vistaPedidoActualDTO =(VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.VISTA_PEDIDO);
									
					//se obtiene los articulos desplegados en el formulario de resultado de la b\u00FAsqueda
					List articulos = (List)formulario.getDatos();
	
	//				String codigoTipoArticuloRecetasCanasta = (String)session.getAttribute(BuscarArticuloAction.COD_TIPO_ARTICULO_CANASTA);
	//				String codigoTipoArticuloRecetasDespensa = (String)session.getAttribute(BuscarArticuloAction.COD_TIPO_ARTICULO_DESPENSA);
	//				if(codigoTipoArticuloRecetasCanasta == null){
	//					//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos
	//					codigoTipoArticuloRecetasCanasta = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
	//					session.setAttribute(BuscarArticuloAction.COD_TIPO_ARTICULO_CANASTA, codigoTipoArticuloRecetasCanasta);
	//				}
	//				if(codigoTipoArticuloRecetasDespensa == null){
	//					//se crea una variable que almacena el c\u00F3gigo del tipo de art\u00EDculo [01] obtenido de un archivo de recursos
	//					codigoTipoArticuloRecetasDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
	//					session.setAttribute(BuscarArticuloAction.COD_TIPO_ARTICULO_DESPENSA, codigoTipoArticuloRecetasDespensa);
	//				}
					
					//variables para el control del mensaje a mostrar
					boolean preciosActualizados =false;
					
					try{
						//se itera el arreglo de String OpEscogerProdBuscados     
						String seleccionados [] = formulario.getOpEscogerProdBuscados();
						if(seleccionados!=null){
							String codigosArticulosAgregados = "";
							String detalleArticulosRepetidos = "";
							int cuentaRepetidos=0;
							int cuentaObsoletos=0;
							int cuentaAgregados=0;
							//Variable que se usa en la jsp (detallePedido) para la validacion de cambio de pesos de los articulos habilitados 
							String[] clasificaciones = CotizacionReservacionUtil.obtenerClasificacionesParaCambioPesos(request).split(",");
							session.setAttribute(CotizarReservarAction.CLASIFICACIONES_AUX_CAMBIO_PESOS, clasificaciones);
							
							//se itera la colecci\u00F3n de indices
							ArrayList <ArticuloDTO> articulosSeleccionados = new ArrayList <ArticuloDTO>();
							for(int i=0;i<seleccionados.length;i++){
								ArticuloDTO articuloDTO = (ArticuloDTO)articulos.get(Integer.parseInt(seleccionados[i]));
								
								List<VistaDetallePedidoDTO> vistaArticulo=(List)vistaPedidoActualDTO.getVistaDetallesPedidosReporte();
							
								try{
									double precioVista = vistaArticulo.get(Integer.parseInt(seleccionados[i])).getValorUnitarioEstado();
									if (!(precioVista==articuloDTO.getPrecioBase())){
										preciosActualizados=true;
									}
									
									ArticuloDTO consultaArticuloDTO = new ArticuloDTO();					
									consultaArticuloDTO.setNpCodigoBarras(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras());
									WebSISPEUtil.construirConsultaArticulos(request, consultaArticuloDTO, estadoInactivo, estadoInactivo, accion);
									//llamada al m\u00E9todo de la capa de servicio que devuelve el art\u00EDculo solicitado
									ArticuloDTO articuloEncontradoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloCodigoBarras(consultaArticuloDTO);
									
									if(articuloEncontradoDTO != null){
										articuloEncontradoDTO.setNpCantidadTotalRegistros(articuloDTO.getNpCantidadTotalRegistros());
										articulosSeleccionados.add(articuloEncontradoDTO);
									}else{
										infos.add("articulosVacio",new ActionMessage("message.codigo.barras.sin.temporada",consultaArticuloDTO.getNpCodigoBarras()));
									}
								}catch(Exception ex){
									UtilesSISPE.validarArticulo(articuloDTO, errors, false, null);
									LogSISPE.getLog().error("Error al obtener art\u00EDculo, el art\u00EDculo est\u00E1 de baja",ex);
								}
							}
								
	
							//se itera la colecci\u00F3n de art\u00EDculos seleccionados
							for (ArticuloDTO articuloDTO : articulosSeleccionados){
								if(!codigosArticulosPedido.contains(articuloDTO.getId().getCodigoArticulo())
	//									|| articuloDTO.getCodigoTipoArticulo().equals(codigoTipoArticuloRecetasCanasta)
	//									|| articuloDTO.getCodigoTipoArticulo().equals(codigoTipoArticuloRecetasDespensa)){
										|| articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) 
										|| articuloDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
									//se verifica el estado del art\u00EDculo
									if(articuloDTO.getNpEstadoArticuloSIC()==null ||
											!articuloDTO.getNpEstadoArticuloSIC().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"))){
										
										//Iniciar validacion de descuentos por articulo
										CotizacionReservacionUtil.iniciarDescuentoPavos(request,articuloDTO);
										//llamada al m\u00E9todo que construye el detalle del pedido
										int cantidad=articuloDTO.getNpCantidadTotalRegistros();
										articuloDTO.setNpCantidadTotalRegistros(null);
										DetallePedidoDTO detallePedidoDTO = CotizacionReservacionUtil.construirNuevoDetallePedido((long)cantidad, detallePedido.size(), articuloDTO, request,errors);
										if(detallePedidoDTO != null && !CotizacionReservacionUtil.canastoConRecetaSinTemporadaActiva(detallePedidoDTO.getArticuloDTO(), infos)){
											detallePedido.add(detallePedidoDTO);
											LogSISPE.getLog().info("se agreg\u00F3 un registro al detallePedido");
											codigosArticulosAgregados=codigosArticulosAgregados.concat(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()).concat(", ");
											cuentaAgregados++;
											codigosArticulosPedido.add(articuloDTO.getId().getCodigoArticulo());
										}else if(infos.isEmpty()){
											errors.add("errorContruirArticulo", new ActionMessage("errors.gerneral","Error al obtener el art\u00EDculo con codigo de barras: "+articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()+", es probable que existan problemas con la informaci\u00F3n registrada"));
										}
									}
									//si el articulo se encuentra como obsoleto en el SIC
									if(articuloDTO.getClaseArticulo() != null && articuloDTO.getClaseArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claseArticulo.obsoleto")) 
											&& ( articuloDTO.getNpStockArticulo()==null || articuloDTO.getNpStockArticulo() < 1)){
										//articulosObsoletos=articulosObsoletos.concat(articuloDTO.getId().getCodigoArticulo()).concat(", ");
										cuentaObsoletos++;
									}
								}else{
									
									detalleArticulosRepetidos=detalleArticulosRepetidos.concat(articuloDTO.getCodigoBarrasActivo().getId().getCodigoBarras()).concat(", ");
									cuentaRepetidos++;
									}	
							}
							
							LogSISPE.getLog().info("Codigos articulos Agregados: "+codigosArticulosAgregados);
							LogSISPE.getLog().info("Cantidad Articulos Agregados: "+cuentaAgregados);
							LogSISPE.getLog().info("Cantidad Articulos Obsoletos: "+cuentaObsoletos);
							LogSISPE.getLog().info("Cantidad Articulos Repetidos: "+cuentaRepetidos);
							
							if(cuentaObsoletos==0){
								
								if(cuentaAgregados==1){
									exitos.add("agregarArticulo",new ActionMessage("message.exito.itemAgregado","El art\u00EDculo "+codigosArticulosAgregados.substring(0, codigosArticulosAgregados.length()-2)));
								}
								else if(cuentaAgregados>1){
									
									if (preciosActualizados){
										exitos.add("agregarArticulos",new ActionMessage("message.exito.articulosAgregados.detalle",cuentaAgregados,"y los precios han sido actualizados"));
									}
									else{
										exitos.add("agregarArticulos",new ActionMessage("message.exito.articulosAgregados.detalle",cuentaAgregados,""));
									}
								}
								
								if(cuentaRepetidos>1){
										errors.add("ArticulosRepetidos",new ActionMessage("errors.detalleArticulosRepetidos",detalleArticulosRepetidos.subSequence(0,detalleArticulosRepetidos.length()-2 )));
								}
								else if(!detalleArticulosRepetidos.equals("")){
										errors.add("ArticuloRepetido",new ActionMessage("errors.detalleRepetido",detalleArticulosRepetidos.subSequence(0,detalleArticulosRepetidos.length()-2 )));
								}
							}
						
							session.removeAttribute(ListadoPedidosAction.COL_SUB_PAGINA);
							session.removeAttribute(CotizarReservarForm.PEDIDOS_ANTERIORES);
							session.removeAttribute(CatalogoArticulosAction.ARTICULOS_AGREGADOS_BUSQUEDA);
							session.removeAttribute(SessionManagerSISPE.VISTA_PEDIDO);
							formulario.setFechaInicial(null);
							formulario.setFechaFinal(null);
						}
						else{
							
							errors.add("ningunSeleccionado", new ActionMessage("errors.seleccion.requerido", "un art\u00EDculo"));
						}
					}
					catch(SISPEException ex){
						errors.add("conexion_sic",new ActionMessage("errors.SISPEException",ex.getMessage()));
					}
					//Cambiar al tab de pedidos
					cambiarAlTabPedido(session, request, beanSession);
					session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion"));
		    }
		} catch (Exception e) {
			//excepcion desconocida
			LogSISPE.getLog().error("Error de cotizar pedidos anteriores",e);
			errors.add("",new ActionMessage("error.gerneral",e.getMessage()));
		}
	}
	
	  
	  public static void cambiarAlTabPedido(HttpSession session, HttpServletRequest request, BeanSession beanSession ){			
			//LogSISPE.getLog().info("el tab selecciondo es: {}, {} ",beanSession.getPaginaTab().getTabSeleccionado(),beanSession.getPaginaTab().getTituloTabSeleccionado());
			 if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().esTabSeleccionado(0)) {
				 LogSISPE.getLog().info("Se elige el tab de PEDIDOS");
				 ContactoUtil.cambiarTabContactoPedidos(beanSession, 1);			
			 }
		}
	  
}
