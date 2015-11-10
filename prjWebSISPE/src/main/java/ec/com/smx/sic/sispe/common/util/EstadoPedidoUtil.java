/*
 * EstadoPedidoUtil.java
 * Creado el 21/07/2009 9:53:05
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.kruger.utilitario.dao.commons.exception.DAOException;
import ec.com.smx.autorizaciones.constants.AutorizacionesConstants;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.autorizaciones.dto.AutorizacionValorComponenteDTO;
import ec.com.smx.autorizaciones.integracion.dto.Autorizacion;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.common.util.transformer.GetInvokerTransformer;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.frameworkv2.util.dto.DynamicComponentDto;
import ec.com.smx.frameworkv2.util.dto.DynamicComponentValueDto;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloBitacoraCodigoBarrasDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloComercialDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloImpuestoDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloMedidaDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloProveedorDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloUnidadManejoDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloUnidadManejoUsoDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.MarcaArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoClasificacionDTO;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.AbonoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoConvenioDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetalleRecetaDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoBloqueoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoBloqueoID;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.FormaPagoDTO;
import ec.com.smx.sic.sispe.dto.TipoAbonoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;
import ec.com.smx.sic.mercancias.commons.exception.MercanciasException;
import ec.com.smx.sic.mercancias.commons.factory.MercanciasFactory;
import ec.com.smx.sic.mercancias.dto.ConvenioEntregaDomicilioDTO;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings("unchecked")
public class EstadoPedidoUtil
{
	public static final String VISTA_PEDIDO = "ec.com.smx.sic.sispe.vistaPedido";
	public static final String DIFERIDO_CUOTAS = "ec.com.smx.sic.sispe.diferidoCuotas";
	public static final String ACCION_ORIGEN = "ec.com.smx.sic.sispe.accionOrigenDif";

	public static final String ENCABEZADO_0RIGINAL = "ec.com.smx.sic.sispe.canastaDTO";
	public static final String CANASTO_0RIGINAL = "ec.com.smx.sic.sispe.canastoOriginal";
	public static final String CANASTO_ACTUAL = "ec.com.smx.sic.sispe.canastoActual";
	public static final String COL_ITEMS_ELIMINADOS = "ec.com.smx.sic.sispe.receta.itemEliminadosRecetaOriginal";
	public static final String COL_ITEMS_AGREGADOS = "ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal";
	public static final String COL_ITEMS_MODIFICADOS = "ec.com.smx.sic.sispe.receta.itemModificadosRecetaOriginal";
	
	public static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");	
	private static final String ESTADO_ACTIVO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
	
	
	
	/**
	 * Obtienenn el detalle del pedido consultado.
	 * 
	 * @param vistaPedidoDTO			El objeto vistaPedidoDTO que contiene todos los datos del pedido
	 * @param request							La petici\u00F3n manejada actualmente
	 * @throws Exception
	 */
	public static void obtenerDetallesPedido(VistaPedidoDTO vistaPedidoDTO, HttpServletRequest request) throws Exception
	{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se verifica la entidad logeada en el sistema
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			//se obtiene los datos adicionales del local
			if(vistaPedidoDTO.getNpLocalDTO() == null){
				LocalID localID = new LocalID();
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				localID.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
				LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);
				//se guarda el local
				vistaPedidoDTO.setNpLocalDTO(localDTO);
			}
		}

		//se obtiene el nombre del usuario que registr\u00F3 el pedido
		if(vistaPedidoDTO.getNpNombreUsuario() == null){
			UserDto userDto = FrameworkFactory.getSecurityService().getUserById(vistaPedidoDTO.getUserId());
			if(userDto != null){
				vistaPedidoDTO.setNpNombreUsuario(userDto.getUserCompleteName());
			}
		}

		//cuando se trata de pedidos anteriores no se cargan los descuentos
		if(request.getParameter("pedidosAnteriores") == null && request.getParameter("accionesPedAnt") == null
				&& request.getParameter("botonImprimiReservasPorDespachar") == null){
			//se llama a la funci\u00F3n que carga los descuentos del pedido
			WebSISPEUtil.obtenerDescuentosEstadoPedido(vistaPedidoDTO,request,Boolean.FALSE);
		}

		//se elimina la variable que controla el detalle de las entregas
		session.removeAttribute("ec.com.smx.sic.sispe.detalleEstadoPedido.entregas");

		LogSISPE.getLog().info("vistaPedidoDTO.getId().getCodigoEstado(): {}",vistaPedidoDTO.getId().getCodigoEstado());

		//se verifica si el detalle est\u00E1 nulo
		Collection<VistaDetallePedidoDTO> colVistaDetallePedidoDTO = vistaPedidoDTO.getVistaDetallesPedidosReporte();
		if(colVistaDetallePedidoDTO==null){
			LogSISPE.getLog().info("trae el detalle del pedido");
			//creaci\u00F3n del objeto VistaDetallePedidoDTO para la consulta
			VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
			consultaVistaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
			//LogSISPE.getLog().info("CODIGO DEL LOCAL: "+ vistaPedidoDTO.getId().getCodigoLocal());
			consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			//asignaci\u00F3n de los par\u00E1metros de consulta
			consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			//consultaVistaDetallePedidoDTO.setCodigoBarras(vistaPedidoDTO.getId().getCodigoPedido());
			consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			consultaVistaDetallePedidoDTO.getArticuloDTO().setArticuloComercialDTO(new ArticuloComercialDTO());

			//join marca articulo
			consultaVistaDetallePedidoDTO.getArticuloDTO().getArticuloComercialDTO().setMarcaComercialArticulo(new MarcaArticuloDTO());

			//condici\u00F3n adicional para los pedidos que no est\u00E1n COTIZADOS
			if(!vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado"))
					&& 	request.getParameter("botonImprimiReservasPorDespachar") == null){
				LogSISPE.getLog().info("existen entregas ");
				consultaVistaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
			}

			//busqueda de los detalles
			colVistaDetallePedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);
			vistaPedidoDTO.setVistaDetallesPedidosReporte(colVistaDetallePedidoDTO);  
			//  		VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)colVistaDetallePedidoDTO.iterator().next();
			//  		Collection<ArticuloBitacoraCodigoBarrasDTO> aux = vistaDetallePedidoDTO.getArticuloDTO().getArtBitCodBarCol();
			//  		aux.size();

			List<VistaDetallePedidoDTO> detalleVistaPedido = new ArrayList<VistaDetallePedidoDTO>();
			detalleVistaPedido = (ArrayList<VistaDetallePedidoDTO>)colVistaDetallePedidoDTO;

			//se itera la vistaDetallePedido para crear un DetallePedidoDTO
			for (int i=0;i<detalleVistaPedido.size();i++){

				VistaDetallePedidoDTO vistaDetallePedidoDTO = detalleVistaPedido.get(i);
				DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
				//se crea el DetallePedidoDTO para poder almacenarlo en la reservaci\u00F3n
				detallePedidoDTO.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
				detallePedidoDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
				detallePedidoDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
				detallePedidoDTO.getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
				detallePedidoDTO.setArticuloDTO(vistaDetallePedidoDTO.getArticuloDTO());
				detallePedidoDTO.setEstadoDetallePedidoDTO(new EstadoDetallePedidoDTO());


				//creaci\u00F3n del estado del detalle del pedido
				detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
				detallePedidoDTO.getEstadoDetallePedidoDTO().getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoDescuento(vistaDetallePedidoDTO.getValorTotalEstadoDescuento());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNeto(vistaDetallePedidoDTO.getValorTotalEstadoNeto());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalEstadoNetoIVA(vistaDetallePedidoDTO.getValorTotalEstadoNetoIVA());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorPrevioVenta(vistaDetallePedidoDTO.getValorPrevioVenta());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorFinalEstadoDescuento(vistaDetallePedidoDTO.getValorFinalEstadoDescuento());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioPOS(vistaDetallePedidoDTO.getValorUnitarioPOS());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVAEstado(vistaDetallePedidoDTO.getValorUnitarioIVAEstado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioEstado(vistaDetallePedidoDTO.getValorUnitarioEstado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioIVANoAfiliado(vistaDetallePedidoDTO.getValorUnitarioIVANoAfiliado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorUnitarioNoAfiliado(vistaDetallePedidoDTO.getValorUnitarioNoAfiliado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorTotalVenta(vistaDetallePedidoDTO.getValorTotalVenta());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setEstadoCanCotVacio(vistaDetallePedidoDTO.getEstadoCanCotVacio());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setValorMayorista(vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo()?
						vistaDetallePedidoDTO.getValorMayoristaEstado():0);

				detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadMinimaMayoreoEstado(
						vistaDetallePedidoDTO.getArticuloDTO().getHabilitadoPrecioMayoreo() ? 
								vistaDetallePedidoDTO.getArticuloDTO().getCantidadMayoreo():0);
				detallePedidoDTO.getEstadoDetallePedidoDTO().setAplicaIVA(vistaDetallePedidoDTO.getAplicaIVA());

				detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
				detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(vistaDetallePedidoDTO.getPesoRegistradoLocal());

				//solo cuando es pedido consolidado y el valor es cero no debe de pintar en pantalla sino el peso registrado
				if(vistaDetallePedidoDTO.getPesoRegistradoLocal() == null || vistaDetallePedidoDTO.getPesoRegistradoLocal()==0){ 
					vistaDetallePedidoDTO.setPesoRegistradoLocal(null);
				}

				//en la opcion de impresion de reservas por despachar no es necesario consultar las autorizaciones
				if(request.getParameter("botonImprimiReservasPorDespachar") == null){

					//Se consulta si tiene autorizaciones
					DetalleEstadoPedidoAutorizacionDTO detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
					EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
					estadoPedidoAutorizacionDTO.setAutorizacionDTO(new ec.com.smx.autorizaciones.dto.AutorizacionDTO());

					detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
					detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
					detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo());
					detalleAutorizaciones.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
					detalleAutorizaciones.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
					detalleAutorizaciones.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
					detalleAutorizaciones.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
					Collection<DetalleEstadoPedidoAutorizacionDTO> autorizacionesCol = SISPEFactory.getDataService().findObjects(detalleAutorizaciones);
					if(CollectionUtils.isNotEmpty(autorizacionesCol)){
						for(DetalleEstadoPedidoAutorizacionDTO detalleAutorizacion : autorizacionesCol){
							if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO() != null && detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion() != null){

								detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpTipoAutorizacion(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion());
								//para el caso de descuento variable
								if(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().getCodigoTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){

									detalleAutorizacion.getEstadoPedidoAutorizacionDTO().setNpNombreDepartamento(
											detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[5].split("AUTORIZACION DESCUENTO VARIABLE")[1]);

									AutorizacionValorComponenteDTO autorizacionValorComponente = new AutorizacionValorComponenteDTO();
									DynamicComponentValueDto valorComponenteDto=new DynamicComponentValueDto();
									valorComponenteDto.setDynamicComponentDto(new DynamicComponentDto());
									autorizacionValorComponente.setDynamicComponentValueDTO(valorComponenteDto);
									autorizacionValorComponente.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
									autorizacionValorComponente.getId().setCodigoAutorizacion(detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());
									autorizacionValorComponente.getId().setCodigoSistema(CorporativoConstantes.CODIGO_SISTEMA_SISPE);

									List<AutorizacionValorComponenteDTO> valoresComponenteCol = (List<AutorizacionValorComponenteDTO>) SISPEFactory.getDataService().findObjects(autorizacionValorComponente);

									//se consultan los valores componentes
									if(CollectionUtils.isNotEmpty(valoresComponenteCol)){

										detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setAutorizacionValorComponenteDTO(valoresComponenteCol);
										//Create Value transient helper collection
										List<DynamicComponentValueDto> dynamicComponentValues =  new ArrayList<DynamicComponentValueDto>();
										for (AutorizacionValorComponenteDTO autorizacionValorComponenteDTO : valoresComponenteCol) {
											dynamicComponentValues.add(autorizacionValorComponenteDTO.getDynamicComponentValueDTO());			
										}
										detalleAutorizacion.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO().setValoresComponente(dynamicComponentValues);
									}
								}
							}
						}
						LogSISPE.getLog().info("Tiene {} autorizacion(es) el articulo {}", autorizacionesCol.size(),vistaDetallePedidoDTO.getId().getCodigoArticulo());
						detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(autorizacionesCol);
					}
				}

				vistaDetallePedidoDTO.setEstadoDetallePedidoDTO(detallePedidoDTO.getEstadoDetallePedidoDTO());

				LogSISPE.getLog().info("estadoArticuloSIC *: {}, estadoArticuloSICReceta *: {}",detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSIC(),detallePedidoDTO.getArticuloDTO().getNpEstadoArticuloSICReceta());
				//llaveDsctos=iniciarDescuentoPavos(request, detallePedidoDTO.getArticuloDTO());
				//calculo de precios Para resumen tributario
				obtenerSubTotalBruto(request, detallePedidoDTO,vistaPedidoDTO);
			}
		}

		Double valorTotalDescuento=0D;
		//setear nombre de autorizador en los descuentos
		if(vistaPedidoDTO.getDescuentosEstadosPedidos() != null && !vistaPedidoDTO.getDescuentosEstadosPedidos().isEmpty()){

			//se obtiene de session la coleccion de departamentos
			Collection<ClasificacionDTO> clasificacionDepartamentosPedidoCol = AutorizacionesUtil.iniciarClasificacionesDepartamentos(request);

			//se buscan los departamentos
			for(DescuentoEstadoPedidoDTO descuentoActual : (ArrayList<DescuentoEstadoPedidoDTO>)vistaPedidoDTO.getDescuentosEstadosPedidos()){

				if(descuentoActual.getLlaveDescuento() != null &&  descuentoActual.getLlaveDescuento().split(SEPARADOR_TOKEN).length > 3){
					DescuentoDTO descuento =   SerializationUtils.clone(descuentoActual.getDescuentoDTO());
					descuentoActual.setDescuentoDTO(null);
					String[] llaveDescuento = descuentoActual.getLlaveDescuento().split(SEPARADOR_TOKEN);
					if(llaveDescuento.length >= 4){

						String nombreDepartamento = "";
						String codigoClasificacionPadre = AutorizacionesUtil.obtenerCodigoClasificacionPadreDeLlaveDescuento(descuentoActual.getLlaveDescuento());
						//					String codigoAutorizador = AutorizacionesUtil.obtenerCodigoAutorizadorDeLlaveDescuento(descuentoActual.getLlaveDescuento());

						if(!codigoClasificacionPadre.equals("") && nombreDepartamento.equals("")){
							//							ClasificacionDTO departamento = AutorizacionesUtil.obtenerClasificacionDTOPorCodigo(clasificacionDepartamentosPedidoCol, 
							//									codigoClasificacionPadre, codigoAutorizador, request);
							ClasificacionDTO departamento = null;
							if(CollectionUtils.isNotEmpty(clasificacionDepartamentosPedidoCol) && StringUtils.isNotEmpty(codigoClasificacionPadre)){
								for(ClasificacionDTO clasificacionActual : clasificacionDepartamentosPedidoCol){
									if(clasificacionActual.getId().getCodigoClasificacion().equals(codigoClasificacionPadre)){
										departamento =  clasificacionActual;
										break;
									}
								}
							}
							if(departamento != null){
								nombreDepartamento = departamento.getDescripcionClasificacion().split(SEPARADOR_TOKEN)[0];
							}else{
								LogSISPE.getLog().info("No existe un departamento con la clasificacion: "+codigoClasificacionPadre);
							}
						}				
						descuento.getTipoDescuentoDTO().setDescripcionTipoDescuento(descuento.getTipoDescuentoDTO().getDescripcionTipoDescuento()+" "+nombreDepartamento);
						if(llaveDescuento[4].equals(ConstantesGenerales.ESTADO_INACTIVO)){
							descuento.getTipoDescuentoDTO().setDescripcionTipoDescuento(descuento.getTipoDescuentoDTO().getDescripcionTipoDescuento()+" "+MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.titulo.descuento.variable.marcaPropia"));
						}
						
						descuentoActual.setDescuentoDTO(descuento);

					}	
				}
				valorTotalDescuento+=descuentoActual.getValorDescuento();
			}
		}
		vistaPedidoDTO.setTotalDescuentoIva(valorTotalDescuento);
		vistaPedidoDTO.setSubTotalNetoBruto(Util.roundDoubleMath(vistaPedidoDTO.getValorTotalBrutoSinIva()-valorTotalDescuento, NUMERO_DECIMALES));
		//se sube a sesi\u00F3n algunos estados
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoCotizado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado"));
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoRecotizado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"));
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoReservado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"));
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoProduccion",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"));
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoReservaConfirmada",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservaConfirmada"));
		session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoEntregado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"));

		//se guarda en sesi\u00F3n el c\u00F3digo del tipo de art\u00EDculo CANASTO
		//		session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
		//		session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		//subClasificacion para articulos tipo canastos 
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
		String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
		session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto", valorTipoArticuloCanastas);

		//subClasificacion para articulos tipo despensas 
		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
		//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
		session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", valorTipoArticuloDespensas);

		//se sube a sesi\u00F3n el tipo de pedido para la impresi\u00F3n de los reportes
		session.setAttribute("ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
		session.setAttribute("ec.com.smx.sic.sispe.enVistaDetallePedidoDTO", "ok");

		//jparedes obtener los abonos del pedido
		Collection abonosPedido = null;
		AbonoPedidoDTO abonoBusqueda = new AbonoPedidoDTO();
		abonoBusqueda.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
		abonoBusqueda.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		abonoBusqueda.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
		abonoBusqueda.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
		abonoBusqueda.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
		abonoBusqueda.setFormaPagoDTO(new FormaPagoDTO());
		abonoBusqueda.setTipoAbonoDTO(new TipoAbonoDTO());
		abonosPedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerAbonoPedido(abonoBusqueda);
		vistaPedidoDTO.setAbonosPedidos(abonosPedido);
		//fin obtener obonos del pedido

		session.setAttribute(VISTA_PEDIDO, vistaPedidoDTO);
		session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO,vistaPedidoDTO);		
		ContactoUtil.cargarDatosPersonaEmpresa(request, vistaPedidoDTO);
	}

	/**
	 * @param request
	 * @param detallePedidoDTO
	 */
	private static void obtenerSubTotalBruto(HttpServletRequest request,
			DetallePedidoDTO detallePedidoDTO,VistaPedidoDTO vistaPedidoDTO) {
		double precioUnitarioIVA = 0;
		double precioUnitario = 0;
		//parametro precios Temp
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//parametro precios Temp
//		String calcularCanastosPreciosTemp=null;
//		if(vistaPedidoDTO!=null){
//			calcularCanastosPreciosTemp=vistaPedidoDTO.getObsmigperTemp();
//		}
		//se inicializan los precios
		if(SessionManagerSISPE.getEstadoPreciosIVA(request).equals(estadoActivo)){
				precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVAEstado().doubleValue();
				precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().doubleValue();
				if(vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoInactivo)){
					precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioIVANoAfiliado().doubleValue();
					precioUnitario = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().doubleValue();
				}
		}else{
			precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioEstado().doubleValue();
			if(vistaPedidoDTO.getEstadoPreciosAfiliado().equals(estadoInactivo)){
					precioUnitarioIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getValorUnitarioNoAfiliado().doubleValue();
				}
				precioUnitario = precioUnitarioIVA;
		}
		//--------- se ajusta cada uno de los valores del detalle por motivos de venta en el POS --------
		double valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitario;
//		double valorPrevioVentaIVA =0D;
		
//		valorPrevioVentaIVA= detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
//		if(calcularCanastosPreciosTemp==null){
//			if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//		  		valorPrevioVentaIVA=SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);//detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
//		  	}
//		}
		// Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);//detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() * precioUnitarioIVA;
		Double valorTotalPrevio=0D;
		Double valorTotalBrutoSinIva=0D;
		Double valorBrutoSinIva=0D;
			//se verifica si el art\u00EDculo es de peso variable
			if(CotizacionReservacionUtil.esArticuloPesoVariable(detallePedidoDTO.getArticuloDTO())){
				//se verifica la acci\u00F3n actual
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal() == null || detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal()==0){
//						valorPrevioVentaIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitarioIVA;
						detallePedidoDTO.getEstadoDetallePedidoDTO().setPesoRegistradoLocal(null);
						valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoArticuloEstado().doubleValue() * precioUnitario;
					}else{
//						valorPrevioVentaIVA = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitarioIVA;
						valorPrevioVenta = detallePedidoDTO.getEstadoDetallePedidoDTO().getPesoRegistradoLocal().doubleValue() * precioUnitario;
					}
					
//					if(calcularCanastosPreciosTemp==null){
//						if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//							valorPrevioVentaIVA =SICArticuloCalculo.getInstancia().calcularValorConImpuestos(valorPrevioVenta, (VALOR_PORCENTAJE_IVA*100));//Util.roundDoubleMath(valorPrevioVenta * (1 + VALOR_PORCENTAJE_IVA), NUMERO_DECIMALES);
//						}
//					}
				}
			
				//se asigna el valor previo venta			
				valorPrevioVenta = Util.roundDoubleMath(valorPrevioVenta, NUMERO_DECIMALES);
				valorTotalPrevio=vistaPedidoDTO.getValorTotalBrutoSinIva()==null?0D:vistaPedidoDTO.getValorTotalBrutoSinIva();
//				if(detallePedidoDTO.getArticuloDTO().getAplicaImpuestoVenta()){
//						valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVentaIVA / (1 + VALOR_PORCENTAJE_IVA),NUMERO_DECIMALES);
//						valorTotalBrutoSinIva=valorTotalPrevio+valorBrutoSinIva;
//				}
//				else{
						valorTotalBrutoSinIva=valorTotalPrevio+valorPrevioVenta;
						valorBrutoSinIva=Util.roundDoubleMath(valorPrevioVenta,NUMERO_DECIMALES);
//				}
			vistaPedidoDTO.setValorTotalBrutoSinIva((vistaPedidoDTO.getValorTotalBrutoSinIva()==null?0D:vistaPedidoDTO.getValorTotalBrutoSinIva())+valorBrutoSinIva);
			LogSISPE.getLog().info("total bruto Sin IVA: {}",valorTotalBrutoSinIva);
			LogSISPE.getLog().info("total bruto Sin IVA: {}",vistaPedidoDTO.getValorTotalBrutoSinIva());
	}
	
	/**
	 * Obtener el detalle del estado de una receta
	 * 
	 * @param vistaDetallePedidoDTO
	 * @return
	 * @throws Exception
	 */
	public static Collection<EstadoDetalleRecetaDTO> obtenerDetalleReceta(VistaDetallePedidoDTO vistaDetallePedidoDTO) throws Exception{
		LogSISPE.getLog().info("<<<<<<Entro a obtener Detalle Receta EstadoPedidoUtils>>>>>>>>>");
		
		
		EstadoDetalleRecetaDTO estadoDetalleRecetaConsulta = new EstadoDetalleRecetaDTO();
		estadoDetalleRecetaConsulta.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
		estadoDetalleRecetaConsulta.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());		
		estadoDetalleRecetaConsulta.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
		
		estadoDetalleRecetaConsulta.setRecetaArticuloDTO(new ArticuloRelacionDTO());
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().setArticuloRelacion(new ArticuloDTO());		
		
		//para impuestos
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().setArticuloImpuestoCol(new ArrayList<ArticuloImpuestoDTO>());
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().getArticuloImpuestoCol().add(new ArticuloImpuestoDTO());
		
		//para medidas
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().setArticuloMedidaDTO(new ArticuloMedidaDTO());
		
		//codigo de barras
		ArticuloBitacoraCodigoBarrasDTO artCodBarDTO= new ArticuloBitacoraCodigoBarrasDTO();
		artCodBarDTO.setEstadoArticuloBitacora(ESTADO_ACTIVO);
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().setArtBitCodBarCol(new ArrayList<ArticuloBitacoraCodigoBarrasDTO>());
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().getArtBitCodBarCol().add(artCodBarDTO);
		
		//Unidad de manejo
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().setArticuloUnidadManejoCol(new ArrayList<ArticuloUnidadManejoDTO>());
		ArticuloUnidadManejoDTO articuloUnidadManejoDTO = new ArticuloUnidadManejoDTO();
		articuloUnidadManejoDTO.setArticuloUnidadManejoUsoCol(new ArrayList<ArticuloUnidadManejoUsoDTO>());
		articuloUnidadManejoDTO.getArticuloUnidadManejoUsoCol().add(new ArticuloUnidadManejoUsoDTO());
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().getArticuloUnidadManejoCol().add(articuloUnidadManejoDTO);
		
		//articulo proveedor
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().setArticuloProveedorCol(new ArrayList<ArticuloProveedorDTO>());
		estadoDetalleRecetaConsulta.getRecetaArticuloDTO().getArticuloRelacion().getArticuloProveedorCol().add(new ArticuloProveedorDTO());
		
		return SessionManagerSISPE.getServicioClienteServicio().transObtenerEstadoDetalleReceta(estadoDetalleRecetaConsulta);
		
	}
	/**
	 * M\u00E9todo que obtiene el detalle de la canasta Original
	 * 
	 * @param codigoArticulo
	 * @return
	 * @throws Exception
	 */
	public static Collection <ArticuloRelacionDTO> obtenerReceta(HttpServletRequest request, String codigoArticulo)throws Exception {
		//se obtiene el estado activo e inactivo [1 y 0 respectivamente]
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//se crea la consulta para traer los items del canasto original
		ArticuloRelacionDTO consultaRecetaArticuloDTO = new ArticuloRelacionDTO(Boolean.TRUE);
		consultaRecetaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaRecetaArticuloDTO.getId().setCodigoArticulo(codigoArticulo);//en el gestor se debe pasar como codigo barras
		consultaRecetaArticuloDTO.setEstado(estadoActivo);
		//se crea el objeto articuloDTO para obtener la relaci\u00F3n
		ArticuloDTO articuloDTO = new ArticuloDTO(Boolean.TRUE);
		
		
		//se construye la consulta para traer el status del art\u00EDculo desde el SIC
		WebSISPEUtil.construirConsultaArticulos(request, articuloDTO, estadoActivo, codigoArticulo, accionActual);
		consultaRecetaArticuloDTO.setArticuloRelacion(articuloDTO);
		
		//Obtener datos de la colecci\u00F3n de recetas
		Collection <ArticuloRelacionDTO> recetaArticulo = SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticulo(consultaRecetaArticuloDTO, false);
		Collection <ArticuloRelacionDTO> recetaArticuloCol = new ArrayList<ArticuloRelacionDTO>();
		LogSISPE.getLog().info("Tama\u00F1o de la Receta: {}", recetaArticulo.size());
		for(ArticuloRelacionDTO receta : recetaArticulo){
			if(!receta.getArticuloRelacion().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || receta.getArticuloRelacion().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
				recetaArticuloCol.add(receta);
			}
			//LogSISPE.getLog().info("Nombre item: {} {}",receta.getArticuloRelacion().getId().getCodigoArticulo()+"---"+receta.getArticuloRelacion().getCodigoBarrasActivo().getId().getCodigoBarras()+"---"+receta.getArticuloRelacion().getDescripcionArticulo());
		}
		return recetaArticuloCol;	
	}
	
	public static void actualizarArticuloStockIntegracionSic(HttpServletRequest request, ActionMessages errors){ 
		try {
			//se obtiene la sesi\u00F3n
			HttpSession session = request.getSession();
			
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			//verificaci\u00F3n para la actualizaci\u00F3n del stock y alcance
			Collection<ArticuloDTO> articulos = new ArrayList<ArticuloDTO>();
			//articulo armado
			ArticuloDTO consultaArticuloDTO = null;
			//se obtiene de sesi\u00F3n la colecci\u00F3n con el estado del detalle del pedido
			VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO) session.getAttribute(VISTA_PEDIDO);
			List<VistaDetallePedidoDTO> colVistaDetallePedido = (ArrayList<VistaDetallePedidoDTO>) vistaPedidoDTO.getVistaDetallesPedidosReporte();
			for(VistaDetallePedidoDTO vistaDetallePedidoDTO : colVistaDetallePedido){
				consultaArticuloDTO = vistaDetallePedidoDTO.getArticuloDTO();
				LogSISPE.getLog().info("codigoArticulo:{}->",vistaDetallePedidoDTO.getId().getCodigoArticulo());
				LogSISPE.getLog().info("codigoPedido:{}->",vistaDetallePedidoDTO.getId().getCodigoPedido());
				consultaArticuloDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
				consultaArticuloDTO.setNpCodigoBarras(vistaDetallePedidoDTO.getCodigoBarras());
				WebSISPEUtil.construirConsultaArticulos(request, consultaArticuloDTO, estadoInactivo, estadoInactivo,null);
				//llamada al m\u00E9todo de la capa de servicio que devuelve el art\u00EDculo solicitado
				consultaArticuloDTO.setNpAlcance(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
				articulos.add(consultaArticuloDTO);
			}
			//llamada al m\u00E9todo que obtiene el stock y alcance de los art\u00EDculos
			SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);
	
			//almaceno en un array el campo del stock traido desde la integracion.
			LogSISPE.getLog().info("Entro a ver el stock, actualizado desde la integracion");
			Long [] npStock = new Long[colVistaDetallePedido.size()]; 
			Integer indice =0;
			//obtengo el valor del campo npStockArticulo
			for(ArticuloDTO actualizados : articulos){
				LogSISPE.getLog().info("codigoArticulo:{}->",actualizados.getId().getCodigoArticulo());
				LogSISPE.getLog().info("npStockArticulos:{}->",actualizados.getNpStockArticulo());
				npStock[indice]= actualizados.getNpStockArticulo();
				indice++;
			}
			indice=0;
			List<VistaDetallePedidoDTO> colActual = new ArrayList<VistaDetallePedidoDTO>(); 
			for(VistaDetallePedidoDTO vista : colVistaDetallePedido){
				//Actualizo el campo del stockArticulo 
				vista.getArticuloDTO().setNpStockArticulo(npStock[indice]);
				colActual.add(vista);
				indice++;
			}
			vistaPedidoDTO.setVistaDetallesPedidosReporte(colActual);
			session.setAttribute(VISTA_PEDIDO, vistaPedidoDTO);	
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("SISPEException",new ActionMessage("errors.SISPEException",ex.getMessage()));
		}
	}
	
	public static void instanciarTabsCanasta(HttpServletRequest request, String accion, String forward)throws Exception {
		LogSISPE.getLog().info("::::: Entro a Instanciar PaginaTab:::::");
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		//session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.canastaActual"));
		//Objetos para construir los tabs
		PaginaTab tabsCanastas = new  PaginaTab(accion, forward, 0, 478, request);
		Tab tabCanastoEstadoActual = new Tab("Estado Actual", accion, "/servicioCliente/estadoPedido/canastaEstadoActual.jsp", true);
		Tab tabCanastoEstadoOriginal = new Tab("Detalle Estado Modificado", accion, "/servicioCliente/estadoPedido/canastaEstadoDetalleModificado.jsp", false);
		tabsCanastas.addTab(tabCanastoEstadoActual);
		tabsCanastas.addTab(tabCanastoEstadoOriginal);
		beanSession.setPaginaTab(tabsCanastas);
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);
	}
	
	//Metodo que me devuelve los codigos de los items de la RecetaArticuloDTO
	public static Collection<Long> itemsCanasta (HttpServletRequest request, Collection<ArticuloRelacionDTO> canasto)throws Exception {
		final Collection<Long> colCodigos = new ArrayList<Long>();
		
		LogSISPE.getLog().info("Codigos de receta");
		for(ArticuloRelacionDTO item : canasto){
			//colCodigos.add(Long.parseLong(item.getId().getCodigoArticulo()));
			if(!item.getArticuloRelacion().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta"))||item.getArticuloRelacion().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
				colCodigos.add(Long.parseLong(item.getId().getCodigoArticuloRelacionado()));
			}			
		}
		return colCodigos;
	}
	
	//Metodo que me devuelve los codigos de los items de la EstadoDetalleRecetaDTO
	public static Collection<Long> itemsCanasta1 (HttpServletRequest request, Collection<EstadoDetalleRecetaDTO> canasto)throws Exception {
		final Collection<Long> colCodigos = new ArrayList<Long>();
		
		LogSISPE.getLog().info("Codigos de receta");
		for(EstadoDetalleRecetaDTO item : canasto){
			colCodigos.add(Long.parseLong(item.getId().getCodigoArticuloRelacionado()));		
		}
		return colCodigos;
	}
	//Funcion que sube a session los items eliminados y Agregados
	public static void comparacionItemsCanasta(HttpServletRequest request, final Collection<Long> canastoOriginal, final Collection<Long> canastoModificado,  Collection<ArticuloRelacionDTO> colCanastoOriginal, Collection<EstadoDetalleRecetaDTO> colCanastoModifi)throws Exception {
		HttpSession session = request.getSession();
		Collection<Long> eliminados = CollectionUtils.subtract(canastoOriginal, canastoModificado);
		LogSISPE.getLog().info("Items Eliminados de la Original");
		//Lista de codigos de articulos eliminados
     	String [] codArticulosEliminados = new String[eliminados.size()];
     	Integer indice=0;
		for(Iterator it1= eliminados.iterator();it1.hasNext();){
			codArticulosEliminados [indice] = String.valueOf(it1.next());
			LogSISPE.getLog().info("Item:->{}",codArticulosEliminados [indice]);
			indice++;
		}
		if(seleccionArticulosEliminados(colCanastoOriginal, codArticulosEliminados) != null){
			session.setAttribute(COL_ITEMS_ELIMINADOS,seleccionArticulosEliminados(colCanastoOriginal, codArticulosEliminados));
		}
		Collection<Long> agregados = CollectionUtils.subtract(canastoModificado, canastoOriginal);
		//Lista de codigos de articulos agregados
     	String [] codArticulosAgregados = new String[agregados.size()];
		indice = 0;
     	LogSISPE.getLog().info("Items Agregados de la Original");
		for(Iterator it= agregados.iterator();it.hasNext();){
			codArticulosAgregados [indice] = String.valueOf(it.next());
			LogSISPE.getLog().info("Item:->{}",codArticulosAgregados [indice]);
			indice++;
		}
		if(seleccionArticulosAgregados(colCanastoModifi, codArticulosAgregados) != null){
			session.setAttribute(COL_ITEMS_AGREGADOS,seleccionArticulosAgregados(colCanastoModifi, codArticulosAgregados));
		}
		
	}
	//Me de vuelve del canasto Original los Items eliminados en una Collection<RecetaArticuloDTO>
	public static Collection<ArticuloRelacionDTO> seleccionArticulosEliminados(Collection<ArticuloRelacionDTO> listaOriginal, final String[] codigoArticulos) throws Exception {
		Predicate predicate = new Predicate() {			
		public boolean evaluate(Object arg0) {
			ArticuloRelacionDTO item = (ArticuloRelacionDTO)arg0;		
			return (ArrayUtils.contains(codigoArticulos, item.getArticuloRelacion().getId().getCodigoArticulo()));
		}
	  };
	  Collection<ArticuloRelacionDTO> listaResultante = new ArrayList<ArticuloRelacionDTO>();
	  CollectionUtils.select(listaOriginal, predicate, listaResultante);
	  return listaResultante;
	}
	//Me de vuelve del canasto Original los Items Agregados en una Collection<EstadoDetalleRecetaDTO>
	public static Collection<EstadoDetalleRecetaDTO> seleccionArticulosAgregados(Collection<EstadoDetalleRecetaDTO> listaOriginal, final String[] codigoArticulos) throws Exception {
		Predicate predicate = new Predicate() {			
		public boolean evaluate(Object arg0) {
			EstadoDetalleRecetaDTO item = (EstadoDetalleRecetaDTO)arg0;		
			return (ArrayUtils.contains(codigoArticulos, item.getRecetaArticuloDTO().getArticuloRelacion().getId().getCodigoArticulo()));
		}
	  };
	  Collection<EstadoDetalleRecetaDTO> listaResultante = new ArrayList<EstadoDetalleRecetaDTO>();
	  CollectionUtils.select(listaOriginal, predicate, listaResultante);
	  return listaResultante;
	}
	//Funcion que sube a session los items eliminados y Agregados
	public static void comparacionCantidadesItemsCanasta(HttpServletRequest request, Collection<ArticuloRelacionDTO> colCanastoOriginal, Collection<EstadoDetalleRecetaDTO> colCanastoModifi)throws Exception {
		LogSISPE.getLog().info("<<<M\u00E9todo que compara las cantidades>>>>");
		HttpSession session = request.getSession();
		Collection<EstadoDetalleRecetaDTO> itemsModificados = new ArrayList<EstadoDetalleRecetaDTO>();
		//Comparo cantidades modificadas del Canasto Original con la modificada.
		for(ArticuloRelacionDTO rOriginal: colCanastoOriginal){
			for(EstadoDetalleRecetaDTO rModificada : colCanastoModifi){
				if(rOriginal.getArticuloRelacion().getId().getCodigoArticulo().equals(rModificada.getId().getCodigoArticulo())){
					if(!rOriginal.getCantidad().equals(rModificada.getCantidadArticulo())){
						itemsModificados.add(rModificada);
					}
				}
			}
		}
		if(itemsModificados != null && !itemsModificados.isEmpty()){
			LogSISPE.getLog().info("Tama\u00F1o de la Colecci\u00F3n Modificada->{}",itemsModificados.size());
			session.setAttribute(COL_ITEMS_MODIFICADOS,itemsModificados);
		}else
			LogSISPE.getLog().info("No Existe Cantidades Modificadas->{}");	
	}
	
	
	/**
	 * Carga los datos para las pesta\u00F1as de detalleAutorizacion, y descuentos por articulo 
	 * 
	 * @param vistaPedidoDTO			El objeto vistaPedidoDTO que contiene todos los datos del pedido
	 * @param request							La petici\u00F3n manejada actualmente
	 * @throws Exception
	 */
	public static void obtenerDetallesPedidoAutorizaciones(VistaPedidoDTO vistaPedido, HttpServletRequest request) throws Exception
	{
		LogSISPE.getLog().info("ingresa al metodo obtenerDetallesPedidoAutorizaciones para crear una lista los detalles que tiene autorizacion de descuento variable");
		try{
			//para validar si tiene aut. dscto. variable
			  if(StringUtils.isNotEmpty(vistaPedido.getTieneAutorizacionDctVar())){
				  if(!vistaPedido.getTieneAutorizacionDctVar().equals(ConstantesGenerales.ESTADO_SI)){
					  if(Integer.parseInt(vistaPedido.getTieneAutorizacionDctVar()) > 0){
						  vistaPedido.setTieneAutorizacionDctVar(ConstantesGenerales.ESTADO_SI);
					  }else{
						  vistaPedido.setTieneAutorizacionDctVar(null);
					  }
				  }
			  }
			//Si tiene autorizaciones ingresa al metodo
			if(vistaPedido.getTieneAutorizacionDctVar() != null &&  vistaPedido.getTieneAutorizacionDctVar().equals("SI")){
				
				ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.filtro.autorizacion", request);
				Long valorDescuentoAprobado = new Long(parametroDTO.getValorParametro().split(SEPARADOR_TOKEN)[0]); //23 descuento aprobado
				Long valorDescuentoSolicitado = new Long(parametroDTO.getValorParametro().split(SEPARADOR_TOKEN)[1]);//61 descuento solicitado
				//colleccion que guarda los detalles con autorizaciones de dcto variable
				Collection<VistaDetallePedidoDTO> vistaDetallePedidoCol =  vistaPedido.getVistaDetallesPedidosReporte();
				
				//se obtiene la coleccion de codigoTipoDescuento
				Collection<String> codigosTipoDescuentoCol = new ArrayList<String>();				
				Collection<DescuentoEstadoPedidoDTO> descuentosEstadosPedidoCol = vistaPedido.getDescuentosEstadosPedidos();
				if(CollectionUtils.isNotEmpty(descuentosEstadosPedidoCol)){
					
					CotizacionReservacionUtil.cargarConfiguracionDescuentos(request, ESTADO_ACTIVO);
					CotizacionReservacionUtil.generarOpDescuentos(request, descuentosEstadosPedidoCol);
					String codigoTipoDescuentoMarcaPropia = DescuentosUtil.obtenerCodigoTipoDescuentoMarcaPropia(request);
					boolean dsctMarcaPropia = false;
					
					for(DescuentoEstadoPedidoDTO descuentoActual : descuentosEstadosPedidoCol){
						
						codigosTipoDescuentoCol.add(descuentoActual.getDescuentoDTO().getCodigoTipoDescuento());
						if(descuentoActual.getLlaveDescuento() != null && descuentoActual.getLlaveDescuento().contains(codigoTipoDescuentoMarcaPropia)){
							dsctMarcaPropia = true;
						}
						
						if(descuentoActual.getId().getCodigoDescuento() != null){
							if(descuentoActual.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
								
								request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS);
								LogSISPE.getLog().info("descuento por cajas habilitado ");
								
							}else if(descuentoActual.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
								
								request.getSession().removeAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA);
								LogSISPE.getLog().info("descuento por mayorista habilitado ");
							}
						}
					}
				}
				
				//se buscan las clasificaciones para los descuentos
				Collection<TipoDescuentoClasificacionDTO> clasificacionesDescuentos = new ArrayList<TipoDescuentoClasificacionDTO>();
				if(CollectionUtils.isNotEmpty(codigosTipoDescuentoCol)){				
					for(String codigoTipoDescuentoActual : codigosTipoDescuentoCol){
						TipoDescuentoClasificacionDTO tipClasificacionDTO= new TipoDescuentoClasificacionDTO();
						tipClasificacionDTO.getId().setCodigoCompania(vistaPedido.getId().getCodigoCompania());										
						tipClasificacionDTO.getId().setCodigoTipoDescuento(codigoTipoDescuentoActual);
						clasificacionesDescuentos.addAll(SISPEFactory.getDataService().findObjects(tipClasificacionDTO));
					}
				}
				
												
				if(CollectionUtils.isNotEmpty(vistaDetallePedidoCol)){
					
					ArrayList<EstadoPedidoAutorizacionDTO> colaAutorizacionesConsulta = new ArrayList<EstadoPedidoAutorizacionDTO>();
					ArrayList<String> listaOpDescuentos = new ArrayList<String>();
					
					//se recorre la lista de vistaDetallePedidos
					for(VistaDetallePedidoDTO vistaActual : vistaDetallePedidoCol){
						
						LogSISPE.getLog().info(""+vistaActual.getArticuloDTO().getDescripcionArticulo()+" - "+vistaActual.getArticuloDTO().getNpCodigoTipoDescuento());	
						
						//quiere decir que si tiene descuento
						if(vistaActual.getEstadoDetallePedidoDTO().getValorFinalEstadoDescuento() > 0){
							
							if(CollectionUtils.isNotEmpty(clasificacionesDescuentos)){
								Boolean encontroDescuento = null;
								//se comparan las clasificaciones de descuentos
								for(TipoDescuentoClasificacionDTO clasificacionDctoActual : clasificacionesDescuentos){
									encontroDescuento = Boolean.FALSE;
									if(clasificacionDctoActual.getId().getCodigoClasificacion().equals(vistaActual.getArticuloDTO().getCodigoClasificacion())){
									
										for(DescuentoEstadoPedidoDTO descuentoActual : descuentosEstadosPedidoCol){
																						
											if(clasificacionDctoActual.getId().getCodigoTipoDescuento().equals(descuentoActual.getDescuentoDTO().getCodigoTipoDescuento())){
												if(vistaActual.getNpCodigoReferencialDescuento() == null)
													vistaActual.setNpCodigoReferencialDescuento(new HashSet<String>());
												vistaActual.getNpCodigoReferencialDescuento().add(clasificacionDctoActual.getId().getCodigoTipoDescuento()+SEPARADOR_TOKEN+descuentoActual.getId().getCodigoReferenciaDescuentoVariable());
												encontroDescuento = Boolean.TRUE;
												break;
											}
										}
									}
								}
								if(!encontroDescuento){
									validarDescuentosCajasMayorista(vistaActual, vistaPedido, request);
								}
							}
							//se verifica si es un articulo con descuento de caja o mayorista
							else{
								validarDescuentosCajasMayorista(vistaActual, vistaPedido, request);
							}
						}
						//se verifica el estado de las autorizaciones
						if(vistaActual != null  && CollectionUtils.isNotEmpty(vistaActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol())){
							
							for(DetalleEstadoPedidoAutorizacionDTO autorizacionActual : vistaActual.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
								
								//solo para el caso de descuentos variable
								if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion() != null &&
										autorizacionActual.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().longValue() == ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue()){
									
									LogSISPE.getLog().info("ProcessCode: {}",autorizacionActual.getEstadoPedidoAutorizacionDTO().getNumeroProceso());
									AutorizacionDTO autorizacionDTO = autorizacionActual.getEstadoPedidoAutorizacionDTO().getAutorizacionDTO();
									if(autorizacionActual.getEstadoPedidoAutorizacionDTO().getEstado().equals(ConstantesGenerales.ESTADO_AUT_APROBADA)){
										if(vistaActual.getNpCodigoReferencialDescuento() == null){
											vistaActual.setNpCodigoReferencialDescuento(new HashSet<String>());
										}
										vistaActual.getNpCodigoReferencialDescuento().add(autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[1].split("CTD")[1]+SEPARADOR_TOKEN+
											autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN)[3]);	
									}
									if(autorizacionDTO.getUsuarioModificacion() != null && (StringUtils.isNotEmpty(autorizacionDTO.getEstado()) 
											&& !autorizacionDTO.getEstado().equals(AutorizacionesConstants.getInstance().AUTORIZACION_ESTADO_PREGUNTA.toString()))){
										//si el estado es diferente a SOL (solicitado) se busca quien gestiono la autorizacion
										UserDto userDTO = new UserDto();
										userDTO.setUserId(autorizacionDTO.getUsuarioModificacion() != null ? autorizacionDTO.getUsuarioModificacion() : autorizacionDTO.getUsuarioAutorizador());
										
										UserDto usuarioDTO = SISPEFactory.getDataService().findUnique(userDTO);
										vistaActual.setNpUsuarioAutorizadorDTO(usuarioDTO);
									}
									
									if(autorizacionDTO != null){
										//Busca del componente el porcentaje solicitado y el aprobado
										vistaActual.setNpPorcentajeDctoAprobado("-");	
										vistaActual.setNpPorcentajeDctoSolicitado("-");
										if(CollectionUtils.isNotEmpty(autorizacionDTO.getValoresComponente())){
											List<DynamicComponentValueDto>  componentesAutorizacion = autorizacionDTO.getValoresComponente();
											for(DynamicComponentValueDto dynCom : componentesAutorizacion){
												if(dynCom.getDynamicComponentDto().getId().getDynamicComponentId().longValue() == valorDescuentoAprobado){
													vistaActual.setNpPorcentajeDctoAprobado(dynCom.getComponentValue());								
												}
												else if(dynCom.getDynamicComponentDto().getId().getDynamicComponentId().longValue() == valorDescuentoSolicitado){								
													vistaActual.setNpPorcentajeDctoSolicitado(dynCom.getComponentValue());
												}
											}
										}
									
										if(vistaActual.getNpPorcentajeDctoAprobado().isEmpty() || vistaActual.getNpPorcentajeDctoAprobado().equals("0.00")){
											vistaActual.setNpPorcentajeDctoAprobado("-");
										}
										if(vistaActual.getNpPorcentajeDctoSolicitado().isEmpty() ){
											vistaActual.setNpPorcentajeDctoSolicitado("-");
										}
										
										Date fechaSolicitud = autorizacionDTO.getFechaSolicitudAutorizacion();
										Date fechaAprobada = autorizacionDTO.getFechaAutorizacion();
										Long fechaDiferencia = null;
										if(fechaAprobada == null){
											fechaAprobada = new Date();
										}
										fechaDiferencia = 	ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaSolicitud), ConverterUtil.parseDateToString(fechaAprobada));							
										vistaActual.setNpEstadoAutorizacion(autorizacionDTO.getEstadoActualAutorizacion());							
										vistaActual.setNpFechaProcAutorizacion(fechaDiferencia.toString());
										
										//se muestra o no el boton de mensajes de autorizaciones
										if(StringUtils.isNotEmpty(autorizacionDTO.getEstado()) && (autorizacionDTO.getEstado().equals(AutorizacionesConstants.getInstance().AUTORIZACION_ESTADO_PREGUNTA.toString()) 
												|| autorizacionDTO.getEstado().equals(AutorizacionesConstants.getInstance().AUTORIZACION_ESTADO_RESPUESTA.toString()))){
											
											//se crea la cola de autorizaciones para mostrar los mensajes
											Autorizacion autorizacion = new Autorizacion();
											String [] titulo = autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial().split(SEPARADOR_TOKEN);
											if(titulo != null && titulo.length >=7 && titulo[5] !=null && titulo[titulo.length-1] !=null){
												autorizacion.setTituloAutorizacion(titulo[5] + " " + titulo[titulo.length-1] +  "%");
											}else{
												autorizacion.setTituloAutorizacion("AUTORIZACION DESCUENTO VARIABLE");// + detalle.getEstadoPedidoAutorizacionDTO().getValorReferencial() + "%)");
											}
											
											autorizacion.setCodigoAutorizacion( autorizacionActual.getEstadoPedidoAutorizacionDTO().getCodigoAutorizacion());
											
											//se agregan los usuarios que recibiran copia para que los mensajes les lleguen a todos
												DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
												detallePedidoDTO.setArticuloDTO(vistaActual.getArticuloDTO());
												Collection<String> idAutorizadorCopia = AutorizacionesUtil.obtenerIdFuncionarioRecibiraCopiaAutorizacion(request, detallePedidoDTO,
														ConstantesGenerales.getInstancia().TIP_AUT_DESCUENTO_VARIABLE.longValue());
												if(CollectionUtils.isNotEmpty(idAutorizadorCopia)){
													autorizacion.getUsuariosAutorizadores().addAll(idAutorizadorCopia);
													LogSISPE.getLog().info("se enviaran copia de autorizaciones a: "+idAutorizadorCopia);
												}
											
											autorizacionActual.getEstadoPedidoAutorizacionDTO().setNpAutorizacion(autorizacion);
											
											LogSISPE.getLog().info("valor referencial {}",autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial());
											//se agregan el valor referencial a la coleccion
											if(!listaOpDescuentos.contains(autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial())){
												listaOpDescuentos.add(autorizacionActual.getEstadoPedidoAutorizacionDTO().getValorReferencial());
												colaAutorizacionesConsulta.add(autorizacionActual.getEstadoPedidoAutorizacionDTO());
											}
										}
									}
								}
							}
						}					
					}
					//se sube a sesion la cola de autorizaciones para mostrar los mensajes en el estado del pedido
					if(CollectionUtils.isNotEmpty(colaAutorizacionesConsulta)){
						request.getSession().setAttribute("ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones","ok");
						request.getSession().setAttribute(AutorizacionesUtil.AUTORIZACIONES_CONSULTA, colaAutorizacionesConsulta);
					}
				}			
				
				//se sube a sesion el obj vistavistaPedido con detalles que tienen autorizaciones de dcto variable
				request.getSession().setAttribute(VISTA_PEDIDO, vistaPedido);
			}					
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al cargar los datos de las autorizaciones en los detalles", e);
			throw new Exception(e);
		}
	}
	
	
	
	private static void validarDescuentosCajasMayorista(VistaDetallePedidoDTO vistaActual, VistaPedidoDTO vistaPedido, HttpServletRequest request) throws MissingResourceException, DAOException, Exception{
		
		
		if(vistaActual.getArticuloDTO().getHabilitadoPrecioCaja()){
			
			if(CotizacionReservacionUtil.esArticuloActivoParaPrecioCaja(vistaPedido.getId().getCodigoAreaTrabajo(), vistaActual.getArticuloDTO().getPrecioCaja(), vistaActual.getArticuloDTO(), request)){
				
				if(vistaActual.getNpCodigoReferencialDescuento() == null){
					vistaActual.setNpCodigoReferencialDescuento(new HashSet<String>());
				}
				
				DescuentoDTO descuentoBusqueda = new DescuentoDTO();										
				descuentoBusqueda.getId().setCodigoCompania(vistaPedido.getId().getCodigoCompania());
				descuentoBusqueda.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"));
				DescuentoDTO descuentoDTO =  SISPEFactory.getDataService().findUnique(descuentoBusqueda);
				
				if(descuentoDTO != null){
					vistaActual.getNpCodigoReferencialDescuento().add(descuentoDTO.getCodigoTipoDescuento()+SEPARADOR_TOKEN+descuentoDTO.getId().getCodigoDescuento());
				}
			}
			
		}else if(vistaActual.getArticuloDTO().getHabilitadoPrecioMayoreo()){
			if(CotizacionReservacionUtil.esArticuloActivoParaPrecioMayorista(vistaPedido.getId().getCodigoAreaTrabajo(), vistaActual.getEstadoDetallePedidoDTO().getCantidadMinimaMayoreoEstado(), 
					vistaActual.getEstadoDetallePedidoDTO().getValorMayorista(), vistaActual.getEstadoDetallePedidoDTO().getCantidadEstado(), vistaActual.getArticuloDTO(), request)){
				
				if(vistaActual.getNpCodigoReferencialDescuento() == null){
					vistaActual.setNpCodigoReferencialDescuento(new HashSet<String>());
				}
				
				DescuentoDTO descuentoBusqueda = new DescuentoDTO();										
				descuentoBusqueda.getId().setCodigoCompania(vistaPedido.getId().getCodigoCompania());
				descuentoBusqueda.getId().setCodigoDescuento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"));
				DescuentoDTO descuentoDTO =  SISPEFactory.getDataService().findUnique(descuentoBusqueda);
				
				if(descuentoDTO != null){
					vistaActual.getNpCodigoReferencialDescuento().add(descuentoDTO.getCodigoTipoDescuento()+SEPARADOR_TOKEN+descuentoDTO.getId().getCodigoDescuento());
				}
			}									
		}
	}
	
	
	/**
	 * M&eacute;todo para validar si una reserva est&aacute; bloqueada desde el punto de venta
	 * @param codigoAreaTrabajo
	 * @param codigoCompania
	 * @param codigoEstado
	 * @param codigoPedido
	 * @param secuencialEstadoPedido
	 * @return	<code>TRUE</code> si la reserva est&aacute; bloqueada, <code>FALSE</code> si la reserva no est&aacute; bloqueada
	 * @throws SISPEException
	 * @author osaransig
	 */
	public static boolean reservaBloqueadaDesdePOS(
			Integer codigoAreaTrabajo, Integer codigoCompania,
			String codigoEstado, String codigoPedido,
			String secuencialEstadoPedido) throws SISPEException {

		boolean respuesta = Boolean.FALSE;
		
		EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();
		estadoPedidoDTO.getId().setCodigoAreaTrabajo(codigoAreaTrabajo);
		estadoPedidoDTO.getId().setCodigoCompania(codigoCompania);
		estadoPedidoDTO.getId().setCodigoEstado(codigoEstado);
		estadoPedidoDTO.getId().setCodigoPedido(codigoPedido);
		estadoPedidoDTO.getId().setSecuencialEstadoPedido(secuencialEstadoPedido);
		estadoPedidoDTO.addCriteriaSearchParameter("fechaFinalEstado", ComparatorTypeEnum.IS_NULL);
		estadoPedidoDTO.setEstado( MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.activo.corporativo"));
		
		EstadoPedidoBloqueoDTO estadoPedidoBloqueoDTO = new EstadoPedidoBloqueoDTO();
		estadoPedidoBloqueoDTO.setId(new EstadoPedidoBloqueoID());
		
		estadoPedidoDTO.setEstadoPedidoBloqueoCol(new ArrayList<EstadoPedidoBloqueoDTO>());
		estadoPedidoDTO.getEstadoPedidoBloqueoCol().add(estadoPedidoBloqueoDTO);
		
		EstadoPedidoDTO estadoPedidoFind = SISPEFactory.getDataService().findUnique(estadoPedidoDTO);
		
		if (estadoPedidoFind != null && CollectionUtils.isNotEmpty(estadoPedidoFind.getEstadoPedidoBloqueoCol())) {
			
			String habilitarCambiosPOS = estadoPedidoFind.getEstadoPedidoBloqueoCol().iterator().next().getHabilitarCambiosPos();
			
			respuesta = habilitarCambiosPOS == null ? Boolean.FALSE :  
					ConstantesGenerales.ESTADO_ACTIVO.equals(habilitarCambiosPOS);
		}
		
		
		return respuesta;
	}
	

	/** Verifica si Existe una orden de salida con  guia de remision o si hay articulos ya entregados en el pedido
	 * @param errors
	 * @param codigoPedido
	 * @param codigoCompania
	 * @return
	 * @throws MercanciasException
	 */
	public static boolean reservaBloqueadaDesdeSICMER(ActionMessages errors, String codigoPedido,Integer codigoCompania) throws MercanciasException {
		try {
			
			Boolean articulosEntregados=false;
			Collection<ConvenioEntregaDomicilioDTO> colConEntDomDTOR=new ArrayList<ConvenioEntregaDomicilioDTO>();
			EntregaPedidoDTO entPedDTO=new EntregaPedidoDTO();
			entPedDTO.setCodigoPedido(codigoPedido);
			entPedDTO.getId().setCodigoCompania(codigoCompania);
			entPedDTO.setCodigoContextoEntrega(Integer.parseInt(ConstantesGenerales.CONTEXTO_ENTREGA_DOMICILIO_MI_LOCAL));
			Collection<EntregaDetallePedidoDTO> colEntDetPedDTO=new ArrayList<EntregaDetallePedidoDTO>();
			colEntDetPedDTO.add(new EntregaDetallePedidoDTO());
			entPedDTO.setEntregaDetallePedidoCol(colEntDetPedDTO);
			Collection<EntregaPedidoDTO> colEntPedDTOR= SISPEFactory.getDataService().findObjects(entPedDTO);
			
			if(CollectionUtils.isNotEmpty(colEntPedDTOR)){
				Collection<String> codigosEntregasPedido  = CollectionUtils.collect(colEntPedDTOR, new GetInvokerTransformer("id.codigoEntregaPedido"));
				EntregaPedidoConvenioDTO entPedConDTO=new EntregaPedidoConvenioDTO();
				entPedConDTO.setConvenioEntregaDomicilioDTO(new ConvenioEntregaDomicilioDTO());
				entPedConDTO.addCriteriaSearchParameter("codigoEntregaPedido", ComparatorTypeEnum.IN_COMPARATOR, codigosEntregasPedido);
				Collection<EntregaPedidoConvenioDTO> colEntPedConDTO= SISPEFactory.getDataService().findObjects(entPedConDTO);
				Collection<ConvenioEntregaDomicilioDTO> colConEntDomDTO=new ArrayList<ConvenioEntregaDomicilioDTO>();
				if(CollectionUtils.isNotEmpty(colEntPedConDTO)){
					for(EntregaPedidoConvenioDTO entPedConDTOR:colEntPedConDTO){
						colConEntDomDTO.add(entPedConDTOR.getConvenioEntregaDomicilioDTO());
					}
				}
				
				colConEntDomDTOR=MercanciasFactory.getInventarioService().obtenerEstadoOrdenSalidaGuiaRemisionDTO(colConEntDomDTO);
				articulosEntregados=buscarArticulosEntregados(colEntPedDTOR);
			}
			//Si la colConEntDomDTOR tiene un valor, entonces existe una OS con guia de remision.
			if (CollectionUtils.isNotEmpty(colConEntDomDTOR)|| articulosEntregados){
				return true;
			}else{
				return false;
			}
		} catch (MercanciasException e) {
			LogSISPE.getLog().error("error al verificar estado de entregas de la reserva",e);
			errors.add("errorSISPE", new ActionMessage("error.bloqueo.sicmer"));
			return false;
		}
	}

	private static Boolean buscarArticulosEntregados(
			Collection<EntregaPedidoDTO> colEntPedDTOR) {
		Boolean existenArticulosEntregados=Boolean.FALSE;
		for(EntregaPedidoDTO entPedDTOR: colEntPedDTOR){
			for(EntregaDetallePedidoDTO entdetPedDTO: entPedDTOR.getEntregaDetallePedidoCol()){
				if(entdetPedDTO.getFechaRegistroEntregaCliente()!=null){
					existenArticulosEntregados=Boolean.TRUE;
					break;
				}
			}
			if(existenArticulosEntregados){
				return existenArticulosEntregados;
			}
		}
		return existenArticulosEntregados;
	}
	
	
	/**
	 * Solo para el proceso de consolidacion: Si existe el mismo canasto en mas de un pedido consolidado y si deseo modificar el canasto especial,
	 * en todos los pedidos consolidados debo actualizar el nuevo canasto especial. Pero al actualizar el nuevo articulo en los pedidos consolidados
	 * se crea un nuevo articulo y el anterior se queda inactivo, esto ocaciona que en la recotizacion o en el estado del pedido salgan todos los items 
	 * incluido los inactivos.
	 * 
	 * Si el pedido es el actual se muestran todos los detalles activos, caso contrario se muestran solo los detalles inactivos,
	 * se implementa este control porque en consolidacion pueden haber detalles activos e inactivos asociados a un mismo secuencialEstadoPedido
	 * porque cuando se modifica la receta de un canasto en un pedido y se aplica el cambio realizado en el canasto 
	 * a los demas canastos iguales de los otros pedidos consolidados se inactiva el detalle actual y se crea un nuevo detalle con el nuevo canasto.
	 * @param vistaPedidoDTO
	 * @param vistaDetallePedidoDTOCol
	 */
	public static void mostrarDetallesPedidoPorEstado(VistaPedidoDTO vistaPedidoDTO, Collection<?> vistaDetallePedidoDTOCol){
		
		if(vistaPedidoDTO != null && vistaPedidoDTO.getCodigoConsolidado() != null && CollectionUtils.isNotEmpty(vistaDetallePedidoDTOCol)){
			String estadoMostrar = "";
			
			if(vistaPedidoDTO.getFechaFinalEstado() == null){
				estadoMostrar = ConstantesGenerales.ESTADO_ACTIVO;
			}else{
				estadoMostrar = ConstantesGenerales.ESTADO_INACTIVO;
			}
			
			LogSISPE.getLog().info("filtrando los detalles en estado "+estadoMostrar);	
			Collection<VistaDetallePedidoDTO> detallesEliminar = new ArrayList<VistaDetallePedidoDTO>();
			//se recorren los detalles
			for(VistaDetallePedidoDTO vistaDetallePedidoDTO : (ArrayList<VistaDetallePedidoDTO>)vistaDetallePedidoDTOCol){
				if(!vistaDetallePedidoDTO.getEstadoDetallePedido().equals(estadoMostrar)){
					detallesEliminar.add(vistaDetallePedidoDTO);
					LogSISPE.getLog().info("no se mostrara el articulo "+vistaDetallePedidoDTO.getId().getCodigoArticulo());
				}
			}
			if(estadoMostrar.equals(ConstantesGenerales.ESTADO_INACTIVO)){
				if(detallesEliminar.size()!=vistaDetallePedidoDTOCol.size()){
					vistaDetallePedidoDTOCol.removeAll(detallesEliminar);
				}
			}else{
				vistaDetallePedidoDTOCol.removeAll(detallesEliminar);
			}
		}
	}

}
