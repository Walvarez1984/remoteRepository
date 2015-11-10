/*
 * ListaPedidosEspecialesAction.java
 * Creado el 04/04/2008 9:13:41
 *   	
 */
package ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action;

import static ec.com.smx.sic.sispe.commons.util.ConstantesGenerales.CARACTER_SEPARACION;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;
/**
 * @author nperalta
 *
 */
@SuppressWarnings("unchecked")
public class ListaPedidosEspecialesAction extends BaseAction{
	private final String ORDEN_ASCENDENTE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	private static final String COL_SUB_PAGINA = "ec.com.smx.sic.sispe.pedidos.subPagina";

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)	throws Exception{

		ActionMessages exitos = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionErrors errors = new ActionErrors();
		
		HttpSession session= request.getSession();
		ListadoPedidosForm formulario = (ListadoPedidosForm)form;

		String forward="desplegar";
		try {
			
		if(request.getParameter("buscar")!=null) {

			LogSISPE.getLog().info("ENTRA A BOTON BUSCAR");
			//colecci\u00F3n que almacena los pedidos buscados
			Collection colVistaPedidoDTO = new ArrayList();
			//DTO que contiene los campos utilizados para la busqueda
			
			// BORRAR ------------------------
			LogSISPE.getLog().info("Llama a m\u00E9todo x2");
			// -------------------------------
			
			VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
			
			// Modificar pedidos pavos
			if (request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) != null) {
				consultaPedidoDTO.setCodigoTipoPedido(ConstantesGenerales.TIPO_PEDIDO_ESP_PAVOS);
				
			// Modificar pedido canasto de catalogo	
			} else if (request.getSession().getAttribute(PedidoCanastoCatalogoAction.PEDIDO_ESPECIAL_CANASTO_CATALOGO) != null) {
				consultaPedidoDTO.setCodigoTipoPedido(ConstantesGenerales.TIPO_PEDIDO_ESP_CANASTA_CATALOGO);
				
			} else { // Modificar pedidos especiales
				
				//Filtrar tipos de pedido que no pertenecen a esta clasificacion
				StringBuilder noInCodigoTipoPedido = new StringBuilder();
				noInCodigoTipoPedido.append(ConstantesGenerales.TIPO_PEDIDO_NORMAL).append(CARACTER_SEPARACION);
				noInCodigoTipoPedido.append(ConstantesGenerales.TIPO_PEDIDO_ESP_PAVOS).append(CARACTER_SEPARACION);
				noInCodigoTipoPedido.append(ConstantesGenerales.TIPO_PEDIDO_ESP_CANASTA_CATALOGO);
				
				consultaPedidoDTO.setNpCodigoTipoPedidoDif(noInCodigoTipoPedido.toString());
			}
			
				
			consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pedidoEspecial.solicitado"));
			//se buscar por estado actual [SI]
			consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
			//se asignan los campos de ordenamiento
			String [][] camposOrden = new String [][]{{"codigoPedido",ORDEN_ASCENDENTE},{"fechaInicialEstado",ORDEN_ASCENDENTE}};
			consultaPedidoDTO.setNpCamposOrden(camposOrden);
			try
			{
				//se obtiene la colecci\u00F3n de Pedidos buscados y se la almacena en sesi\u00F3n
				colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
				if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
					//se muestra un mensaje indicando que la lista est\u00E1 vacia
					infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos Especiales pendientes de Confirmaci\u00F3n"));
					session.removeAttribute(COL_SUB_PAGINA);
				}else{
					
					for(VistaPedidoDTO vistaPedidoDTO:(Collection<VistaPedidoDTO>)colVistaPedidoDTO){
						Collection<VistaDetallePedidoDTO> detallePedido = vistaPedidoDTO.getVistaDetallesPedidosReporte();
						if(detallePedido==null){
							LogSISPE.getLog().info("trae el detalle del pedido");
							//creaci\u00F3n del objeto VistaDetallePedidoDTO para la consulta
							VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
							consultaVistaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
							//LogSISPE.getLog().info("CODIGO DEL LOCAL: "+ vistaPedidoDTO.getId().getCodigoLocal());

							consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
							//consultaVistaDetallePedidoDTO.setCodigoTipoArticulo(vistaPedidoDTO.getCodigoTipoPedido());
							consultaVistaDetallePedidoDTO.getObservacionAutorizacionOrdenCompra();

							//asignaci\u00F3n de los par\u00E1metros de consulta
							consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
							consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
							consultaVistaDetallePedidoDTO.getEstadoDetallePedido();
							consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
							consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
							consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
							//busqueda de los detalles
							detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
							vistaPedidoDTO.setVistaDetallesPedidosReporte(detallePedido);

						}
					}

					LogSISPE.getLog().info("ENTRO A LA PAGINACION");
					Collection datos= colVistaPedidoDTO;
					session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, datos);
					formulario.setSize(String.valueOf(datos.size()));
					int size= datos.size();
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
					int start= 0;
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));
					Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
					session.setAttribute(COL_SUB_PAGINA,datosSub);
				}
			}
			catch(SISPEException ex){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				//si ocurre un error al llamar al m\u00E9todo del servicio
				errors.add("Pedidos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos de Carnes"));
				
			}

			forward="desplegar";
		}
		else if(request.getParameter("range")!=null || request.getParameter("start")!=null)
		{
			LogSISPE.getLog().info("ENTRO A LA PAGINACION");
			Collection datos= (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
			if(datos!=null){
				formulario.setSize(String.valueOf(datos.size()));
				int size= datos.size();
				int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
				int start= Integer.parseInt(request.getParameter("start"));
				formulario.setStart(String.valueOf(start));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(size));

				Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
				session.setAttribute(COL_SUB_PAGINA,datosSub);
			}
		}

		else{
			Collection<VistaPedidoDTO> colVistaPedidoDTO=new ArrayList<VistaPedidoDTO>();
			if(session.getAttribute(CrearPedidoAction.REGRESA_BUSQUEDA)!=null){
				colVistaPedidoDTO= (ArrayList<VistaPedidoDTO>)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
				LogSISPE.getLog().info("Entra a if**");
			}

			//se eliminan las variables de sesi\u00F3n que comiencen con "ec.com"
			SessionManagerSISPE.removeVarSession(request);
			//VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
			session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "B\u00FAsqueda de pedidos especiales de carnes");
			session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaConfirmarPedidoEspecial"));

			//se inicializan los radios de b\u00FAsqueda
			
			// IMPORTANTE: Se comenta campo opcionCampoBusqueda para habilitar consulta m\u00FAltiple
			// ----------------------------------------------------------------------------------------------------------------
			//formulario.setOpcionCampoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
			// ----------------------------------------------------------------------------------------------------------------
			
			formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			formulario.setEtiquetaFechas("Fecha de Pedido");
			session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de pedido");
			
			//se verifica si la entidad responsable es un local
			String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
			if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
				formulario.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal().toString());
			else
				//se obtienen los locales por ciudad
				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

			if(colVistaPedidoDTO!=null){
				session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
				LogSISPE.getLog().info("entra a if!!");

				formulario.setSize(String.valueOf(colVistaPedidoDTO.size()));
				int size= colVistaPedidoDTO.size();
				int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
				int start= 0;
				formulario.setStart(String.valueOf(start));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(size));

				Collection datosSub = Util.obtenerSubCollection(colVistaPedidoDTO,start, start + range > size ? size : start+range);
				session.setAttribute(COL_SUB_PAGINA,datosSub);
			}
		}
		}
		catch(Exception ex)
		{
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			forward = "errorGlobal";
		}
//		se guardan los mensajes
		saveErrors(request, errors);
		saveInfos(request, infos);
		saveMessages(request, exitos);

		return mapping.findForward(forward);
			}




}
