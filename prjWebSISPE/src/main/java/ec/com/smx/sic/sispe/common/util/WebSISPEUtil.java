/*
 * Clase WebSISPEUtil.java
 * Creado el 21/07/2006
 *
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_ESTABLECIMIENTO_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CODIGO_LOCAL_REFERENCIA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.NUMERO_DECIMALES;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PORCENTAJE_CALCULO_PRECIOS_AFILIADO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PORCENTAJE_DIFERENCIA_PRECIOS_NORMAL_MAYORISTA;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PREFIJO_VARIABLE_SESION;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;

import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.common.util.ColeccionesUtil;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.ManejoFechas;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.common.util.dto.Duplex;
import ec.com.smx.framework.factory.FrameworkFactory;
import ec.com.smx.framework.security.dto.UserDto;
import ec.com.smx.framework.web.util.MenuUtils;
import ec.com.smx.mensajeria.commons.resources.MensajeriaMessages;
import ec.com.smx.mensajeria.dto.EventoDTO;
import ec.com.smx.mensajeria.dto.id.EventoID;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.common.articulo.SICArticuloConstantes;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloPrecioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionUsuarioDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.DescuentoDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialClasificacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.EspecialDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoDescuentoDTO;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.ExtructuraDiferidos;
import ec.com.smx.sic.sispe.dto.CuotaDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.DescuentoEstadoPedidoID;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaCalendarioArticuloDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaEntidadResponsableDTO;
import ec.com.smx.sic.sispe.dto.VistaEstablecimientoCiudadLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaLocalDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.calendarioBodega.struts.form.LineaProduccionForm;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action.CrearPedidoAction;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.action.PedidoPerecibleAction;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.CrearPedidoForm;
import ec.com.smx.sic.sispe.web.reportes.dto.OrdenDiasSemanaDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.ArticuloRDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.BeneficiarioRDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.EntregaFechaRDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.EntregaRDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.OrdenDespachoEntregaRDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.PedidoRDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * Contiene m&eacute;todos gen&eacute;ricos que son utilizados por algunas clases en el proyecto Web.
 * @author	fmunoz
 * @version 2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class WebSISPEUtil
{
	public static final String ACCION_CONSOLIDAR = "ec.com.smx.sic.sispe.accion.consolidar";
	//public static final String PRECIOS_ALTERADOS = "ec.com.smx.sic.sispe.reservacion.preciosMejorados";
	public static final String COD_TIPO_PEDIDO_ESP_USER = "ec.com.smx.sic.sispe.pedidoEspecial.tipoPedidoUsuario";
	public static final int CANTIDAD_MESES_ANIO = 12;
	public static final String REPORTE_DIFERIDOS = "ec.com.smx.sic.sispe.reporteDiferidos";
	public static final String FLAG_DIFERIDOS = "ec.com.smx.sic.sispe.flagDiferidos";
	
	public static final String START_PAG = PREFIJO_VARIABLE_SESION.concat("startPag");
	public static final String RANGE_PAG = PREFIJO_VARIABLE_SESION.concat("rangePag");
	public static final String SIZE_PAG = PREFIJO_VARIABLE_SESION.concat("sizePag");
	private static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken"); //-
	private static final String CODIGO_TIPO_DESCUENTO = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.llaveDescuento.codigoTipoDescuento"); //CTD
	
	
	/**
	 * Asigna las propiedades necesarias para realizar la consulta de art\u00EDculos.
	 * @param request										- La petici&oacute;n relizada desde el browser
	 * @param articuloDTO								- Una instancia de <code>ArticuloDTO</code> creada previamente
	 * @param npDetalle									- Indica si el art\u00EDculo a consultar es o no item de la receta especificada en el par\u00E1metro <code>npCodigoArticuloPadre</code>
	 * @param npCodigoArticuloPadre			- Indica el c\u00F3digo del art\u00EDculo que identifica la receta que contiene los items
	 */
	public static void construirConsultaArticulos(HttpServletRequest request,ArticuloDTO articuloDTO, String npDetalle, String npCodigoArticuloPadre, String accion){
		try{
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			//creaci\u00F3n del DTO para el art\u00EDculo
			articuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			articuloDTO.setEstadoArticulo(estadoActivo);
			if(!SessionManagerSISPE.getCurrentLocal(request).equals(new Integer(0))){
				articuloDTO.setNpCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));
			}
			else{
				
				articuloDTO.setNpCodigoLocal(SessionManagerSISPE.getCodigoLocalObjetivo(request));
				
				if(articuloDTO.getNpCodigoLocal() == null){
					
					//se obtiene de sesion del vistaDetallePedido
					if(request.getSession().getAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO") != null){
						
						VistaDetallePedidoDTO vistaDetallePedidoDTO  = (VistaDetallePedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO");
						
						if(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo() != null && vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo() > 0){
							
							LogSISPE.getLog().info("Se asigna el codigo local del vistaDetallePedido: {}",vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
							articuloDTO.setNpCodigoLocal(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
						}
					}
				}
			}
			
			articuloDTO.setNpDetalle(npDetalle);
			articuloDTO.setNpCodigoArticuloPadre(npCodigoArticuloPadre);
			articuloDTO.setNpActivarInterfazSIC(estadoActivo);
			
			if (request.getSession().getAttribute("sispe.pedido.pavos") != null) {
				articuloDTO.setNpPedidoPavos("stockDisponiblesLocales");
			}
			
			//se consulta articulos solo en estado CODIFICADO
			articuloDTO.setCodigoEstado(SICArticuloConstantes.ESTADOARTICULO_CODIFICADO);
			
			//se verifica la acci\u00F3n para indicar que debe o no dar alcance temporal
			if(accion!=null && (accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")))){
				
				//solo se setea el alcance temporal cuando presiona el boton actualizar desde reservacion y el articulo no tenga alcance
				if(request.getParameter("actualizarDetalle") != null && articuloDTO.getNpAlcance() != null 
						&& articuloDTO.getNpAlcance().equals(SessionManagerSISPE.getEstadoInactivo(request))){
					articuloDTO.setNpAlcance(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.claveAlcanceTemporal"));
				}
				
				if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion"))
						&& request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO)!=null){					
					//esta condici\u00F3n se realiza para verificar cual debe ser el valor correcto del stock para un art\u00EDculo
					articuloDTO.setNpSecuencialEstadoPedido(((VistaPedidoDTO)request.getSession().getAttribute(SessionManagerSISPE.VISTA_PEDIDO)).getId().getSecuencialEstadoPedido());
				}
			}

			LogSISPE.getLog().info("codigoCompania: "+articuloDTO.getId().getCodigoCompania() +				
					//", codigoArticulo: "+articuloDTO.consultaArticuloDTO.setNpCodigoBarras( +
					", codigoBarras: "+articuloDTO.getNpCodigoBarras() +
					", estado: "+articuloDTO.getEstadoArticulo() +
					", npCodigoLocal: "+articuloDTO.getNpCodigoLocal() +
					", npDetalle: "+articuloDTO.getNpDetalle() +
					", npCodigoArticuloPadre: "+articuloDTO.getNpCodigoArticuloPadre() +
					", npAlcance: "+articuloDTO.getNpAlcance() +
					", npSecuencialEstadoPedido: "+articuloDTO.getNpSecuencialEstadoPedido());
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
	}

	/**
	 * Obtiene un objeto <code>VistaLocalDTO</code>
	 * @param 	indiceLocal		- Contiene el indice del local seleccionado, este indice es compuesto:
	 * 													el indice de la ciudad y el indice del local
	 * @request request				- La petici\u00F3n HTTP
	 * @return 								- Objeto <code>VistaLocalDTO</code> con los datos
	 * @throws 	Exception
	 */
	public static VistaLocalDTO obtenerDatosLocal(String indiceLocal, HttpServletRequest request)throws Exception
	{
		VistaLocalDTO vistaLocalDTO = null;
		try{
			String token = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.caracterToken");
			if(indiceLocal != null){
				String [] indiceCompuesto = indiceLocal.split(token);
				//indiceCompuesto[0] : indice ciudad
				//indiceCompuesto[1] : indice local
				LogSISPE.getLog().info("indice ciudad: {}, indice local: {}",indiceCompuesto[0],indiceCompuesto[1]);
				//se obtiene la colecci\u00F3n de los locales que est\u00E1 en sesi\u00F3n
				ArrayList<VistaEstablecimientoCiudadLocalDTO> colVistaEstablecimientoCiudadLocalDTO =	
					(ArrayList<VistaEstablecimientoCiudadLocalDTO>)request.getSession().getAttribute(SessionManagerSISPE.COLECCION_LOCALES);

				//se obtiene el objeto VistaEstablecimientoCiudadLocalDTO
				VistaEstablecimientoCiudadLocalDTO vistaEstablecimientoCiudadLocalDTO = colVistaEstablecimientoCiudadLocalDTO.get(Integer.parseInt(indiceCompuesto[0]));

				//se obtiene el objeto VistaLocalDTO
				ArrayList<VistaLocalDTO> colVistaLocalDTO = (ArrayList<VistaLocalDTO>)vistaEstablecimientoCiudadLocalDTO.getVistaLocales();
				vistaLocalDTO = colVistaLocalDTO.get(Integer.parseInt(indiceCompuesto[1]));
			}
		}catch(Exception ex){
			throw ex;
		}
		return vistaLocalDTO;
	}

	/**
	 * Construye la <code>OrdenDespachoEntregaRDTO</code> que sera utilizado para mostrar el reporte.
	 * @param datosReporte		- Contiene la colecci\u00F3n que contiene los datos del reporte por local
	 * @return 								- El objeto <code>OrdenDespachoEntregaRDTO</code> que contiene el reporte a 
	 * 													ser mostrado
	 */
	public static OrdenDespachoEntregaRDTO construirReporteDespachoEntrega(Collection coleccionDatosReportePorLocal)throws Exception
	{
		LogSISPE.getLog().info("******* generando Orden ********");
		if(coleccionDatosReportePorLocal!=null)
		{
			OrdenDespachoEntregaRDTO ordenDespachoEntregaRDTO = new OrdenDespachoEntregaRDTO();
			try{
				LogSISPE.getLog().info("tama\u00F1o de la colecci\u00F3n de reportes: {}",coleccionDatosReportePorLocal.size());
				Collection <PedidoRDTO> pedidos = new ArrayList <PedidoRDTO>();
				for(Iterator <Collection> it = coleccionDatosReportePorLocal.iterator(); it.hasNext();){
					PedidoRDTO pedidoRDTO = obtenerPedido(it.next());
					if(pedidoRDTO!=null)
						pedidos.add(pedidoRDTO);
				}
				//se obtiene la primera entrega
				PedidoRDTO pedidoRDTO = (PedidoRDTO)pedidos.toArray()[0];
				String [] datos = pedidoRDTO.getDatosLocal();
				//se asignan los campos correspondientes
				ordenDespachoEntregaRDTO.setDescripcionLocal(datos[1]);
				ordenDespachoEntregaRDTO.setPedidos(pedidos);

				return ordenDespachoEntregaRDTO;
			}catch(Exception ex){
				throw ex;
			}
		}
		return null;
	}
	/**
	 * Construye el objeto <code>PedidoRDTO</code> con los datos del pedido.
	 * 
	 * @param datosReportePorPedido		- Colecci\u00F3n de los datos del reporte por pedido
	 * @return												- Objeto <code>PedidoRDTO</code> generado
	 * @throws Exception
	 */
	private static PedidoRDTO obtenerPedido(Collection datosReportePorPedido)throws Exception
	{
		if(datosReportePorPedido!=null){
			PedidoRDTO pedidoRDTO = new PedidoRDTO();
			Collection <EntregaRDTO> entregas = new ArrayList <EntregaRDTO>();

			try{
				for(Iterator <Collection> it = datosReportePorPedido.iterator(); it.hasNext();){
					EntregaRDTO entregaRDTO = obtenerEntrega(it.next());
					if(entregaRDTO!=null)
						entregas.add(entregaRDTO);
				}

				//se obtiene la primera entrega
				EntregaRDTO entregaRDTO = (EntregaRDTO)entregas.toArray()[0];
				String [] datos = entregaRDTO.getDatosPedido();

				//se asignan los campos correspondientes
				pedidoRDTO.setNumeroPedido(datos[3]);
				pedidoRDTO.setCedulaContacto(datos[4]);
				pedidoRDTO.setNombreContacto(datos[5]);
				pedidoRDTO.setTelefonoContacto(datos[6]);
				pedidoRDTO.setNombreEmpresa(datos[7]);
				pedidoRDTO.setRucEmpresa(datos[8]);
				pedidoRDTO.setEntregas(entregas);
				pedidoRDTO.setDatosLocal(datos);

				return pedidoRDTO;
			}catch(Exception ex){
				throw ex;
			}
		}
		return null;
	}

	/**
	 * Construye la EntregaRDTO que ser\u00E1 mostrada en el reporte.
	 * @param datosReporte	Colecci\u00F3n de arreglos de string con los datos del reporte
	 * @return entregaDTO		La estructura que contiene el subreporte a ser mostrado
	 */
	private static EntregaRDTO obtenerEntrega(Collection datosReportePorEntrega)throws Exception
	{
		if(datosReportePorEntrega!=null){
			try
			{
				String [] primerRegistro = (String [])datosReportePorEntrega.toArray()[0];
				EntregaRDTO entregaRDTO = new EntregaRDTO();
				entregaRDTO.setLugarEntrega(primerRegistro[9]);
				entregaRDTO.setDatosPedido(primerRegistro);

				//se crea la colecci\u00F3n que almacenar\u00E1 los objetos EntregaFechaRDTO
				Collection <EntregaFechaRDTO> entregasFecha = new ArrayList <EntregaFechaRDTO>(); 
				//se inicializa el valor de la fecha
				String fechaDespachoEntrega = primerRegistro[10];

				//se crea la colecci\u00F3n que almacenar\u00E1 los objetos BeneficiarioRDTO
				Collection <BeneficiarioRDTO> beneficiarios = new ArrayList <BeneficiarioRDTO>();
				//se inicializa con el valor del primer n\u00FAmero de c\u00E9dula
				String cedulaBeneficiario = primerRegistro[14];  
				String nombreBeneficiario = primerRegistro[15];

				//colecci\u00F3n que guardar\u00E1 los registros de cada detalle de art\u00EDculos.
				Collection <ArticuloRDTO> articulos = new ArrayList <ArticuloRDTO>();
				for(Iterator <String []> it = datosReportePorEntrega.iterator();it.hasNext();)
				{
					String [] registroActual = it.next();
					ArticuloRDTO articuloRDTO = new ArticuloRDTO();
					LogSISPE.getLog().info("CEDULA: {}",registroActual[14]);
					LogSISPE.getLog().info("CODIGO ARTICULO: {}",registroActual[11]);
					LogSISPE.getLog().info("CODIGO BARRAS: {}",registroActual[17]);
					LogSISPE.getLog().info("DESCRIPCION: {}",registroActual[12]);
					LogSISPE.getLog().info("CANTIDAD: {}",registroActual[13]);
					LogSISPE.getLog().info("------------------------------");
					articuloRDTO.setCodigoArticulo(registroActual[11]);
					articuloRDTO.setDescripcionArticulo(registroActual[12]);
					articuloRDTO.setCantidadArticulo(registroActual[13]);
					articuloRDTO.setCodigoBarras(registroActual[17]);

					//se comparan las fechas 
					if(!fechaDespachoEntrega.equals(registroActual[10])){
						//se agrega el objeto a la colecci\u00F3n de beneficiarios
						beneficiarios.add(construirBeneficiario(articulos, cedulaBeneficiario, nombreBeneficiario));
						//se actualizan los valores
						cedulaBeneficiario = registroActual[14];
						nombreBeneficiario = registroActual[15];
						//se crea una nueva colecci\u00F3n de art\u00EDculos para el pr\u00F3ximo beneficiario
						articulos = new ArrayList <ArticuloRDTO>();

						//se crea el objeto Entrega por fecha y se agrega el objeto a la colecci\u00F3n
						entregasFecha.add(construirEntregaFecha(beneficiarios, fechaDespachoEntrega));
						//se actualiza la nueva fecha
						fechaDespachoEntrega = registroActual[10];
						//se crea una nueva colecci\u00F3n de beneficiarios para la proxima entrega por fecha
						beneficiarios = new ArrayList <BeneficiarioRDTO>();
					}//se comparan las c\u00E9dulas de los beneficiarios
					else if(!cedulaBeneficiario.equals(registroActual[14])){           	
						beneficiarios.add(construirBeneficiario(articulos, cedulaBeneficiario, nombreBeneficiario));
						//se actualizan los valores
						cedulaBeneficiario = registroActual[14];
						nombreBeneficiario = registroActual[15];

						//se crea una nueva colecci\u00F3n de art\u00EDculos para el pr\u00F3ximo beneficiario
						articulos = new ArrayList <ArticuloRDTO>();
					}
					articulos.add(articuloRDTO);
				}
				//creaci\u00F3n del \u00FAltimo beneficiario
				beneficiarios.add(construirBeneficiario(articulos, cedulaBeneficiario, nombreBeneficiario));
				//creaci\u00F3n de la \u00FAltima entrega por fecha y se agrega el objeto a la colecci\u00F3n
				entregasFecha.add(construirEntregaFecha(beneficiarios, fechaDespachoEntrega));
				//se agrega la entrega por fecha a la colecci\u00F3n de entregas generales
				entregaRDTO.setEntregasFecha(entregasFecha);

				return entregaRDTO;
				
			}catch(Exception ex){
				throw ex;
			}
		}
		return null;
	}

	/**
	 * Construye el objeto <code>BeneficiarioRDTO</code>
	 * @param articulos						- Colecci\u00F3n de los art\u00EDculos que le corresponden al beneficiario
	 * @param cedulaBeneficiario	- C\u00E9dula del beneficiario
	 * @param nombreBeneficiario	- Nombre del beneficiario
	 * @return										- El objeto <code>BeneficiarioRDTO</code> creado
	 */
	private static BeneficiarioRDTO construirBeneficiario(Collection <ArticuloRDTO> articulos, String cedulaBeneficiario,
			String nombreBeneficiario)
	{
		LogSISPE.getLog().info("CREACION DEL BENEFICIARIO");
		LogSISPE.getLog().info("CON: {} ARTICULOS",articulos.size());
		//se crea el objeto BeneficiarioRDTO
		BeneficiarioRDTO beneficiarioRDTO = new BeneficiarioRDTO();
		beneficiarioRDTO.setCedulaBeneficiario(cedulaBeneficiario);
		beneficiarioRDTO.setNombreBeneficiario(nombreBeneficiario);
		beneficiarioRDTO.setArticulos(articulos);

		return beneficiarioRDTO;
	}

	/**
	 * Construye el objeto <code>EntregaFechaRDTO</code>
	 * @param beneficiarios					- Colecci\u00F3n de los beneficiarios que le corresponden a la Entrega por Fecha
	 * @param fechaDespachoEntrega	- La fecha de despacho a bodega/entrega al cliente  
	 * @return											- El objeto <code>EntregaFechaRDTO</code> creado
	 */
	private static EntregaFechaRDTO construirEntregaFecha(Collection <BeneficiarioRDTO> beneficiarios, 
			String fechaDespachoEntrega)
	{
		LogSISPE.getLog().info("CREACION DE LA FECHA ENTREGA");
		LogSISPE.getLog().info("CON: {} BENEFICIARIOS",beneficiarios.size());
		//se crea el objeto EntregaFechaRDTO
		EntregaFechaRDTO entregaFechaRDTO = new EntregaFechaRDTO();
		entregaFechaRDTO.setFechaDespachoEntrega(fechaDespachoEntrega);
		entregaFechaRDTO.setBeneficiarios(beneficiarios);

		return entregaFechaRDTO;
	}

	/**
	 * Formatea un timestamp para que no se muestre con los segundos
	 * @param fecha			Fecha que se desea formatear
	 * @return fechaString	Fecha formateada como un <code>String</code>	
	 */
	public static String formatearTimestamp(Timestamp fecha)
	{
		if(fecha!=null){
			String fechaString = fecha.toString();
			int indice = fechaString.lastIndexOf(":");
			if(indice >0)
				return fechaString.substring(0,indice);
			return fechaString;
		}
		return null;
	}


	/**
	 * Adjunta la fecha actual al nombre de un determinado archivo
	 * @param		nombreCortoArchivo
	 * @param 	extension
	 * @return 	nombreCompletoArchivo
	 * @throws 	Exception
	 */
	public static String generarNombreArchivo(String nombreCortoArchivo, String extension) throws Exception
	{
		try{
			//se obtiene la fecha actual y se la formatea
			Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
			String fechaFormateada = simpleDateFormat.format(fechaActual);

			String nombreCompletoArchivo = nombreCortoArchivo+"_"+fechaFormateada+"."+extension;
			//se retorna el nombre completo
			return nombreCompletoArchivo;
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			throw ex;
		}
	}

	/**
	 * Obtiene el path de la aplicaci\u00F3n en el servidor.
	 * @param 	request					La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return	pathAplicacion	El contexto de la aplicaci\u00F3n
	 * @throws Exception
	 */
	public static String obtenerContextoAplicacion(HttpServletRequest request) throws Exception
	{
		LogSISPE.getLog().info("se obtiene el contexto de la aplicaci\u00F3n");
		try{
			ServletContext servletContext = request.getSession().getServletContext();
			String contexto = servletContext.getRealPath(request.getContextPath());
			//deacuerdo al separador de directorios 
			int indice = contexto.lastIndexOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.separador.directorios"));
			String pathAplicacion = contexto.substring(0,indice);
			return pathAplicacion;
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			throw ex;
		}
	}

	/**
	 * Obtiene la fecha m\u00EDnima de entrega
	 * @param 	request							La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return	fechaMinimaEntrega	La fecha m\u00EDnima de entrega
	 * @throws 	Exception
	 */
	public static String obtenerFechaMinimaEntregaResponsableCD(HttpServletRequest request) throws Exception
	{
		LogSISPE.getLog().info("se obtiene la fecha m\u00EDnima de entrega");
		try{
			ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.fechaEntrega", request);
			//se crea un formato para la fecha
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MessagesWebSISPE.getString("formatos.fecha"));
			if(parametroDTO.getValorParametro()!=null){
				//se llama a la funci\u00F3n que suma los d\u00EDas que devolvi\u00F3 la consulta a la fecha actual
				Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
				Timestamp fechaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, Long.parseLong(parametroDTO.getValorParametro()));
				//se formatea la fecha de entrega
				return simpleDateFormat.format(fechaEntrega);
			}
			//se retorna la fecha actual
			return simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			throw ex;
		}
	}
	
	/**
	 * Obtiene la fecha m\u00EDnima de entrega
	 * @param 	request							La petici\u00F3n que actualmente se est\u00E1 procesando
	 * @return	fechaMinimaEntrega	La fecha m\u00EDnima de entrega
	 * @throws 	Exception
	 */
	public static Timestamp obtenerFechaMinimaEntregaTimeStamp(HttpServletRequest request) throws Exception
	{
		LogSISPE.getLog().info("se obtiene la fecha m\u00EDnima de entrega");
		try{
			ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.fechaEntrega", request);
			//se crea un formato para la fecha
			if(parametroDTO.getValorParametro()!=null){
				//se llama a la funci\u00F3n que suma los d\u00EDas que devolvi\u00F3 la consulta a la fecha actual
				Timestamp fechaActual = new Timestamp(System.currentTimeMillis());
				Timestamp fechaEntrega = ManejoFechas.sumarDiasTimestamp(fechaActual, Long.parseLong(parametroDTO.getValorParametro()));
				//se formatea la fecha de entrega
				return fechaEntrega;
			}
			//se retorna la fecha actual
			return new Timestamp(System.currentTimeMillis());
		}catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			throw ex;
		}
	}


	/**
	 * Construye el orden de dias en el que va a salir en el calendario
	 * 
	 * @param dias	Dias de la Semana
	 * @return ordenDiasSemanaDTOCol	coleccion de dias de la semana
	 */	
	public static Collection<OrdenDiasSemanaDTO> ordenDiasSemana(String [] dias)
	{
		Collection <OrdenDiasSemanaDTO> ordenDiasSemanaDTOCol=new ArrayList <OrdenDiasSemanaDTO>();
		for(int i=0;i<dias.length;i++){
			OrdenDiasSemanaDTO ordenDiasSemanaDTO=new OrdenDiasSemanaDTO();
			ordenDiasSemanaDTO.setNombreDia(dias[i]);
			ordenDiasSemanaDTO.setSeleccion("");
			ordenDiasSemanaDTOCol.add(ordenDiasSemanaDTO);
		}
		return(ordenDiasSemanaDTOCol);
	}
	
	/**
	 * Calcula el costo de una entrega segun el sector
	 * 
	 * @param totalReserva	Valor total de la reserva
	 * @param porcentaje  	Porcentaje que representa el costo por sector
	 * @return costo	Costo de la entrega por costo
	 */	
	public static double costoEntrega(Double totalReserva, Double porcentaje)
	{
		double valorReserva=totalReserva.doubleValue();
		double porcentajeCosto=porcentaje.doubleValue();
		double costo=(valorReserva * porcentajeCosto)/100;
		return(costo);
	}

	/**
	 * Calcula el costo de una entrega segun la distancia entre el local y la direccion
	 * @param distancia	Valor de la distancia en km
	 * @param request
	 * @param totalEntrega	Valor total de la entrega
	 * @return costo	Costo de la entrega por costo
	 * @throws Exception 
	 */	
	public static double costoEntregaDistancia(Double distancia, HttpServletRequest request, Double totalParcialEntrega, Boolean pedirArticulosLocal) throws Exception
	{
		double costo = 0d;
		
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.costoFleteKilometro", request);
		ParametroDTO parametroDistanciaDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.distancia.minimaEntregaDomicioLocal", request);
		ParametroDTO parametroMontoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioLocal", request);
		ParametroDTO parametroRecargoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.entrega.recargoEntregaDomicilio", request);
		
		if(parametroDTO.getValorParametro()!=null && parametroDistanciaDTO.getValorParametro()!=null 
				&& parametroMontoDTO.getValorParametro()!=null && parametroRecargoDTO.getValorParametro()!=null){
			
			try{
				
				double costoPorKilometro = (Double.valueOf(parametroDTO.getValorParametro())).doubleValue();
				double valorDistancia=distancia.doubleValue();
				
				LogSISPE.getLog().info("costo por kilometro: {}", costoPorKilometro);
				LogSISPE.getLog().info("distancia: " + distancia);
				
				if(pedirArticulosLocal){//
					
//					Para entregas a domicilio desde el local si el valor es mayor o igual a $250 y es 3 KM a la redonda, es gratis.
//					Para entregas a domicilio desde el local si el valor es mayor o igual a $250 y es mayor a 3 KM, se debe de sumar un extra por Kil\u00F3metro.
					if(totalParcialEntrega>= Double.valueOf(parametroMontoDTO.getValorParametro())){
						if(valorDistancia<=Double.valueOf(parametroDistanciaDTO.getValorParametro())){
							costo = 0;
						}else{
							costo = valorDistancia * costoPorKilometro;
						}
					}else{
//						Para entregas a domicilio desde el local si el valor es menor $250, es $25 m\u00E1s el extra por Kil\u00F3metro.
//						Para entregas a domicilio desde el local si el valor es menor $250 y la distancia es igual a 3KM, se debe cobrar  $25 m\u00E1s el extra por Kil\u00F3metro.
						costo = (valorDistancia * costoPorKilometro) + (Double.valueOf(parametroRecargoDTO.getValorParametro())).doubleValue();
					}
				// cuado se solicita desde el CD el calculo del flete se mantiene
				}else{
					costo = valorDistancia * costoPorKilometro;
				}
				
			}catch(NumberFormatException ex){
				LogSISPE.getLog().info("error en el formato del par\u00E1metro");
				return 0;
			}
		}else {
			costo = 0;
		}
		
		LogSISPE.getLog().info("costo entrega {}", costo);
		
		return costo;
	}
	
	/**
	 * Calcula la distancia de una entrega segun el costo existente en la entrega
	 * 
	 * @param totalReserva	Valor total de la reserva
	 * @param porcentaje  	Porcentaje que representa el costo por sector
	 * @return costo	Costo de la entrega por costo
	 * @throws Exception 
	 */	
	public static double distanciaEntregaCosto(Double costo, HttpServletRequest request, Boolean esEntregaDomStockLocal) throws Exception
	{
		double valorCosto=costo.doubleValue();
		
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.costoFleteKilometro", request);
//		ParametroDTO parametroDistanciaDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.distancia.minimaEntregaDomicioLocal", request);
//		ParametroDTO parametroMontoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.monto.minimoEntregaDomicioLocal", request);
//		ParametroDTO parametroRecargoDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.entrega.recargoEntregaDomicilio", request);
		
		if(parametroDTO.getValorParametro()!=null){
			try{
				double costoPorKilometro = (Double.valueOf(parametroDTO.getValorParametro())).doubleValue();
				LogSISPE.getLog().info("costo por kilometro: {}", costoPorKilometro);
				LogSISPE.getLog().info("costo: {}", valorCosto);
				
				if(esEntregaDomStockLocal){
					double distancia=valorCosto/costoPorKilometro;
					return(distancia);//ver como hacer
				}else {
					double distancia=valorCosto/costoPorKilometro;
					return(distancia);
				}
				
			}catch(NumberFormatException ex){
				LogSISPE.getLog().info("error en el formato del par\u00E1metro");
				return 0;
			}
		}
		return 0;
	}
	/**
	 * Construye una consulta Linea Produccion para las b\u00FAsquedas(sobre la CalendarioArticulo)
	 * 
	 * @param  request						Petici\u00F3n manejada actualmente
	 * @param  form								Formulario de donde se obtienen los campos de b\u00FAsqueda
	 * @return CalendarioArticuloDTO			Objeto que contiene los campos que utiliza luego el m\u00E9todo que realiza la
	 * 														b\u00FAsqueda
	 * @throws Exception
	 */
	public static VistaCalendarioArticuloDTO construirConsultaLineaProduccion(HttpServletRequest request, ActionForm form)throws Exception{
		LogSISPE.getLog().info("Se procede a ingresar a construirConsultaLineaProduccion...");
		//rango de fechas para las b\u00FAsquedas
		Date fechaInicial = null;
		Date fechaFinal = null;
		
		//se crea el objeto que sirve para consulta
		VistaCalendarioArticuloDTO consultaCalArtDTO = new VistaCalendarioArticuloDTO();
		consultaCalArtDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//se verifica la instancia del formulario
		LineaProduccionForm formulario = (LineaProduccionForm)form;
		//JMENA: Setear valor para tipo verificar --------------
		if(formulario.getCodigoClasificacionTxt()!= null && !formulario.getCodigoClasificacionTxt().trim().equals("")){
			consultaCalArtDTO.setCodigoClasificacion(formulario.getCodigoClasificacionTxt());
			LogSISPE.getLog().info("C\u00F3digo clasificaci\u00F3n: {}",consultaCalArtDTO.getCodigoClasificacion());
		}
		
		if(formulario.getCodigoArticuloTxt()!= null && !formulario.getCodigoArticuloTxt().trim().equals("")){
//			consultaCalArtDTO.getId().setCodigoArticulo(formulario.getCodigoArticuloTxt().trim());
//			LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaCalArtDTO.getId().getCodigoArticulo());
			consultaCalArtDTO.setCodigoBarras(formulario.getCodigoArticuloTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo barras: {}",consultaCalArtDTO.getCodigoBarras());
		}
		
		if(formulario.getDescripcionArticuloTxt()!= null && !formulario.getDescripcionArticuloTxt().trim().equals("")){
			consultaCalArtDTO.setDescripcionArticulo(formulario.getDescripcionArticuloTxt().trim());
			LogSISPE.getLog().info("Descripci\u00F3n art\u00EDculo: {}",consultaCalArtDTO.getDescripcionArticulo());
		}
		/*
		if(formulario.getTipoMovCombo() != null && !formulario.getTipoMovCombo().trim().equals("S")){
			consultaCalArtDTO.setTipoMovimiento(formulario.getTipoMovCombo().trim());
			LogSISPE.getLog().info("Tipo Motivo Movimiento: {}",consultaCalArtDTO.getTipoMovimiento());
		}*/
		//se verifica los rangos de fechas a ser tomados en cuenta
		LogSISPE.getLog().info("Opci\u00F3n fecha: {}",formulario.getOpcionFecha());
		
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n Rango");
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = ConverterUtil.parseStringToDate(formulario.getFechaInicial());
					
					LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = ConverterUtil.parseStringToDate(formulario.getFechaFinal());
					LogSISPE.getLog().info("Fecha final: {}",fechaFinal);
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			//si ambas fechas permanecen nulas se busca por todo
			if(fechaInicial == null && fechaFinal == null){
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			}
		  } 
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Hoy'");
			consultaCalArtDTO.setFechaCalendario(new Date());
		  }
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Desde'");
			LogSISPE.getLog().info("N\u00FAmero de meses: {}",formulario.getNumeroMeses());
			
			int meses = 4;
			try{
				//se convierte a entero el n\u00FAmero de meses
				meses = Integer.parseInt(formulario.getNumeroMeses());
			}catch(NumberFormatException e) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			fechaInicial = new Date(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
			LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
		  }
		if(formulario.getFechaCalendario()!=null && !formulario.getFechaCalendario().equals("")){
			LogSISPE.getLog().info("Busqueda Por fecha exacta del calendario");
			consultaCalArtDTO.setFechaCalendario(ConverterUtil.parseStringToDate(formulario.getFechaCalendario()));
		  }
		if(fechaInicial!=null)
			consultaCalArtDTO.setNpFechaInicio(fechaInicial);
		if(fechaFinal!=null)
			consultaCalArtDTO.setNpFechaFin(fechaFinal);	
		
		return consultaCalArtDTO;
		
	}
		

	/**
	 * Construye una consulta gen\u00E9rica para las b\u00FAsquedas de pedidos (sobre la VISTAPEDIDO)
	 * 
	 * @param  request						Petici\u00F3n manejada actualmente
	 * @param  form								Formulario de donde se obtienen los campos de b\u00FAsqueda
	 * @return VistaPedidoDTO			Objeto que contiene los campos que utiliza luego el m\u00E9todo que realiza la
	 * 														b\u00FAsqueda
	 * @throws Exception
	 */
	public static VistaPedidoDTO construirConsultaGeneralPedidos(HttpServletRequest request, ActionForm form)throws Exception{
		LogSISPE.getLog().info("Se procede a ingresar a construirConsultaGeneralPedidos...");
						
		//se obtiene la acci\u00F3n de la sesi\u00F3n
		String accion = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		boolean limitarPorFechas = true;
		
		LogSISPE.getLog().info("Acci\u00F3n: {}",accion);
		
		//se crea el objeto que sirve para consulta
		VistaPedidoDTO consultaVistaPedidoDTO = new VistaPedidoDTO();
		consultaVistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//se verifica la instancia del formulario
		if(form instanceof ListadoPedidosForm){
			return inicializarFormListaPedido(request, accion, estadoActivo,limitarPorFechas, consultaVistaPedidoDTO, (ListadoPedidosForm)form);
		}
		return inicializarFormCotizarReservar(request, accion, estadoActivo,limitarPorFechas, consultaVistaPedidoDTO, (CotizarReservarForm)form);
	}

	/**
	 * @param request
	 * @param accion
	 * @param estadoActivo
	 * @param limitarPorFechas
	 * @param consultaVistaPedidoDTO
	 * @param formulario
	 * @return
	 */
	private static VistaPedidoDTO inicializarFormCotizarReservar(
			HttpServletRequest request, String accion, String estadoActivo,
			boolean limPorFech, VistaPedidoDTO consultaVistaPedidoDTO,CotizarReservarForm formulario) {
		
		boolean limitarPorFechas = limPorFech;
		//OANDINO: Setear valor para tipo verificar si el registro es enviado o no al CD --------------
		LogSISPE.getLog().info("Enviado a CD: {}",formulario.getComboEnviadoCD());
		if(formulario.getComboEnviadoCD()!= null && !formulario.getComboEnviadoCD().equals("")){
			consultaVistaPedidoDTO.setReservarBodegaSIC(formulario.getComboEnviadoCD());
		}
		// --------------------------------------------------------------------------------------------
			
		if(formulario.getNumeroPedidoTxt()!= null && !formulario.getNumeroPedidoTxt().trim().equals("")){
				//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
				consultaVistaPedidoDTO.getId().setCodigoPedido(formulario.getNumeroPedidoTxt().trim());
				LogSISPE.getLog().info("N\u00FAmero pedido: {}",consultaVistaPedidoDTO.getId().getCodigoPedido());
				limitarPorFechas = false;
		}
		
		if(formulario.getNumeroReservaTxt()!= null && !formulario.getNumeroReservaTxt().trim().equals("")){
			consultaVistaPedidoDTO.setLlaveContratoPOS(formulario.getNumeroReservaTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero reserva: {}",consultaVistaPedidoDTO.getLlaveContratoPOS());
			limitarPorFechas = false;
		}
		
		if(formulario.getNumeroConsolidadoTxt()!= null && !formulario.getNumeroConsolidadoTxt().trim().equals("")){
			consultaVistaPedidoDTO.setCodigoConsolidado(formulario.getNumeroConsolidadoTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero consolidado: {}",consultaVistaPedidoDTO.getCodigoConsolidado());
			limitarPorFechas = false;
		}
		
		if(formulario.getCodigoClasificacionTxt()!= null && !formulario.getCodigoClasificacionTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO());
			consultaVistaPedidoDTO.getArticuloDTO().setCodigoClasificacion(formulario.getCodigoClasificacionTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo clasificaci\u00F3n: {}",consultaVistaPedidoDTO.getArticuloDTO().getCodigoClasificacion());
		}
		
		if(formulario.getCodigoArticuloTxt()!= null && !formulario.getCodigoArticuloTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO());
			consultaVistaPedidoDTO.getArticuloDTO().getId().setCodigoArticulo(formulario.getCodigoArticuloTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaVistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
		}
		
		if(formulario.getDescripcionArticuloTxt()!= null && !formulario.getDescripcionArticuloTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO());
			consultaVistaPedidoDTO.getArticuloDTO().setDescripcionArticulo(formulario.getDescripcionArticuloTxt().trim());
			LogSISPE.getLog().info("Descripci\u00F3n art\u00EDculo: {}",consultaVistaPedidoDTO.getArticuloDTO().getDescripcionArticulo());
		}
		
		if(formulario.getDocumentoPersonalTxt()!= null && !formulario.getDocumentoPersonalTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNumeroDocumentoPersona(formulario.getDocumentoPersonalTxt().trim());
			LogSISPE.getLog().info("Documento personal: {}",consultaVistaPedidoDTO.getNumeroDocumentoPersona());
		}
		
		if(formulario.getNombreContactoTxt()!= null && !formulario.getNombreContactoTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNombrePersona(formulario.getNombreContactoTxt().trim());
			LogSISPE.getLog().info("Nombre contacto: {}",consultaVistaPedidoDTO.getNombreContacto());
		}
		
		if(formulario.getRucEmpresaTxt()!= null && !formulario.getRucEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setRucEmpresa(formulario.getRucEmpresaTxt().trim());
			LogSISPE.getLog().info("Ruc: {}",consultaVistaPedidoDTO.getRucEmpresa());
		}
		
		if(formulario.getNombreEmpresaTxt()!= null && !formulario.getNombreEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNombreEmpresa(formulario.getNombreEmpresaTxt().trim());
			LogSISPE.getLog().info("Nombre empresa: {}",consultaVistaPedidoDTO.getNombreEmpresa());
		}

		//se verifica si la consulta se debe filtrar solo los pedidos en el estado actual
	    if(formulario.getOpEstadoActivo()!= null && formulario.getOpEstadoActivo().equals(estadoActivo)){
	    	consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
	    }
	    //busqueda por el estado del pedido
	    if (formulario.getEstadoPedido() != null && !formulario.getEstadoPedido().equals("")){
			consultaVistaPedidoDTO.getId().setCodigoEstado(formulario.getEstadoPedido());
			//por omisi\u00F3n debe ser el estado actual
			//consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		}
		
		// OANDINO: B\u00FAsqueda por estados de pago del pedido en el caso de la ventana Estado del Pedido ---------
		LogSISPE.getLog().info("Estado Pago Pedido: {}",formulario.getComboEstadoPagoPedido());
		if(formulario.getComboEstadoPagoPedido() != null &&!formulario.getComboEstadoPagoPedido().equals("")){
			consultaVistaPedidoDTO.setCodigoEstadoPagado(formulario.getComboEstadoPagoPedido());
		}
		
		//B\u00FAsqueda por estados de pago del pedido
		if (formulario.getEstadoPagoPedido() != null && !formulario.getEstadoPagoPedido().equals("")){
			consultaVistaPedidoDTO.setCodigoEstadoPagado(formulario.getEstadoPagoPedido());
		}

		//busqueda de los pedidos que por lo menos tengan una orden de compra
		if(formulario.getOpPedidoOrdenCompra()!=null && !formulario.getOpPedidoOrdenCompra().equals("")){
			consultaVistaPedidoDTO.setNpOrdenCompra(formulario.getOpPedidoOrdenCompra()); //estadoActivo o estadoInactivo
		}
		
		//busqueda por tipo de pedido
		if(StringUtils.isNotEmpty(formulario.getComboTipoPedido())){
			consultaVistaPedidoDTO.setCodigoTipoPedido(formulario.getComboTipoPedido());
		}
		
		//rango de fechas para las b\u00FAsquedas
		Timestamp fechaInicial = null;
		Timestamp fechaFinal = null;
		//se verifica los rangos de fechas a ser tomados en cuenta
		
		LogSISPE.getLog().info("Opci\u00F3n fecha: {}",formulario.getOpcionFecha());
		
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n Rango");
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaInicial(), 0, 0, 0, 0, 0, 0));
					LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaFinal(), 0, 0, 23, 59, 59, 999));
					LogSISPE.getLog().info("Fecha final: {}",fechaFinal);
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			//si ambas fechas permanecen nulas se busca por todo
			if(fechaInicial == null && fechaFinal == null){
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			}
		}
		
		//JACC: Si la busqueda es por fecha actual
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Hoy'");
			//se llenan las fechas
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
			fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 23, 59, 59, 999));
		}
		
		//cuando se busca por la opci\u00F3n todos
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Desde'");
			LogSISPE.getLog().info("N\u00FAmero de meses: {}",formulario.getNumeroMeses());
			
			int meses = 4;
			try{
				if(formulario.getNumeroMeses()!=null && !formulario.getNumeroMeses().equals("") ){
					//se convierte a entero el n\u00FAmero de meses
					meses = Integer.parseInt(formulario.getNumeroMeses());
				}
				else{
					LogSISPE.getLog().info("Se toma el parametro por default 4 meses");
				}
			}catch(NumberFormatException e) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
		}

		//se asignan las fechas de acuerdo a la acci\u00F3n
		if(accion!=null && (fechaInicial!=null || fechaFinal!=null) && limitarPorFechas){
			if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaControlProduccionPedEsp"))){
				LogSISPE.getLog().info("primera fecha despacho");
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
				
			}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega"))){
				consultaVistaPedidoDTO.setNpPrimeraFechaEntregaInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpPrimeraFechaEntregaFinal(fechaFinal);
				
			}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"))){
				if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaDespacho"))){
					LogSISPE.getLog().info("Se ha elegido fecha despacho");
					consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
				}
				else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEntrega"))){
					LogSISPE.getLog().info("Se ha elegido fecha entrega");
					consultaVistaPedidoDTO.setNpPrimeraFechaEntregaInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpPrimeraFechaEntregaFinal(fechaFinal);
				}
				else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEstadoPedido"))){
					LogSISPE.getLog().info("Se ha elegido fecha estado");
					consultaVistaPedidoDTO.setNpFechaEstadoInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpFechaEstadoFinal(fechaFinal);
				}
			}else{
				consultaVistaPedidoDTO.setNpFechaEstadoInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpFechaEstadoFinal(fechaFinal);
			}
		}
		
		//se verifica si el usuario es de un local
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
		}else{
			//asignaci\u00F3n del local
			if(formulario.getCodigoLocal()!=null && !formulario.getCodigoLocal().equals("")){
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(new Integer(formulario.getCodigoLocal()));
			}
			//asignaci\u00F3n de la entidad responsable
			if(formulario.getOpEntidadResponsable()!=null && !formulario.getOpEntidadResponsable().equals("")){
				LogSISPE.getLog().info("Entro a realizar el filtro Entidad Responsable");
				consultaVistaPedidoDTO.setEntidadResponsable(formulario.getOpEntidadResponsable()); //BOD o LOC
			}	
		}

		return consultaVistaPedidoDTO;
	}
	
	/**
	 * @param request
	 * @param accion
	 * @param estadoActivo
	 * @param limitarPorFechas
	 * @param consultaVistaPedidoDTO
	 * @param formulario
	 * @return
	 */
	private static VistaPedidoDTO inicializarFormListaPedido(
			HttpServletRequest request, String accion, String estadoActivo,
			boolean limPorFech, VistaPedidoDTO consultaVistaPedidoDTO,ListadoPedidosForm formulario) {
		
		boolean limitarPorFechas = limPorFech;
		//OANDINO: Setear valor para tipo verificar si el registro es enviado o no al CD --------------
		LogSISPE.getLog().info("Enviado a CD: {}",formulario.getComboEnviadoCD());
		if(formulario.getComboEnviadoCD()!= null && !formulario.getComboEnviadoCD().equals("")){
			consultaVistaPedidoDTO.setReservarBodegaSIC(formulario.getComboEnviadoCD());
		}
		// --------------------------------------------------------------------------------------------
			
		if(formulario.getNumeroPedidoTxt()!= null && !formulario.getNumeroPedidoTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.getId().setCodigoPedido(formulario.getNumeroPedidoTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero pedido: {}",consultaVistaPedidoDTO.getId().getCodigoPedido());
			limitarPorFechas = false;
		}
		
		if(formulario.getNumeroReservaTxt()!= null && !formulario.getNumeroReservaTxt().trim().equals("")){
			consultaVistaPedidoDTO.setLlaveContratoPOS(formulario.getNumeroReservaTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero reserva: {}",consultaVistaPedidoDTO.getLlaveContratoPOS());
			limitarPorFechas = false;
		}
		
		if(formulario.getNumeroConsolidadoTxt()!= null && !formulario.getNumeroConsolidadoTxt().trim().equals("")){
			consultaVistaPedidoDTO.setCodigoConsolidado(formulario.getNumeroConsolidadoTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero consolidado: {}",consultaVistaPedidoDTO.getCodigoConsolidado());
			limitarPorFechas = false;
		}
		
		if(formulario.getCodigoClasificacionTxt()!= null && !formulario.getCodigoClasificacionTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO(Boolean.TRUE));
			consultaVistaPedidoDTO.getArticuloDTO().setCodigoClasificacion(formulario.getCodigoClasificacionTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo clasificaci\u00F3n: {}",consultaVistaPedidoDTO.getArticuloDTO().getCodigoClasificacion());
		}
		
		if(formulario.getCodigoArticuloTxt()!= null && !formulario.getCodigoArticuloTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO(Boolean.TRUE));
			//consultaVistaPedidoDTO.getArticuloDTO().getId().setCodigoArticulo(formulario.getCodigoArticuloTxt().trim());
			consultaVistaPedidoDTO.getArticuloDTO().setNpCodigoBarras(formulario.getCodigoArticuloTxt().trim());
			//LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaVistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
			LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaVistaPedidoDTO.getArticuloDTO().getNpCodigoBarras());
		}
		
		if(formulario.getDescripcionArticuloTxt()!= null && !formulario.getDescripcionArticuloTxt().trim().equals("")){
			consultaVistaPedidoDTO.setArticuloDTO(new ArticuloDTO(Boolean.TRUE));
			consultaVistaPedidoDTO.getArticuloDTO().setDescripcionArticulo(formulario.getDescripcionArticuloTxt().trim());
			LogSISPE.getLog().info("Descripci\u00F3n art\u00EDculo: {}",consultaVistaPedidoDTO.getArticuloDTO().getDescripcionArticulo());
		}
		
		if(formulario.getDocumentoPersonalTxt()!= null && !formulario.getDocumentoPersonalTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNumeroDocumentoPersona(formulario.getDocumentoPersonalTxt().trim());
			LogSISPE.getLog().info("Documento personal: {}",consultaVistaPedidoDTO.getNumeroDocumentoPersona());
		}
		
		if(formulario.getNombreContactoTxt()!= null && !formulario.getNombreContactoTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNombrePersona(formulario.getNombreContactoTxt().trim());
			LogSISPE.getLog().info("Nombre contacto: {}",consultaVistaPedidoDTO.getNombrePersona());
		}
		
		if(formulario.getRucEmpresaTxt()!= null && !formulario.getRucEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setRucEmpresa(formulario.getRucEmpresaTxt().trim());
			LogSISPE.getLog().info("Ruc: {}",consultaVistaPedidoDTO.getRucEmpresa());
		}
		
		if(formulario.getNombreEmpresaTxt()!= null && !formulario.getNombreEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaPedidoDTO.setNombreEmpresa(formulario.getNombreEmpresaTxt().trim());
			LogSISPE.getLog().info("Nombre empresa: {}",consultaVistaPedidoDTO.getNombreEmpresa());
		}

		//se verifica si la consulta se debe filtrar solo los pedidos en el estado actual
	    if(formulario.getOpEstadoActivo()!= null && formulario.getOpEstadoActivo().equals(estadoActivo)){
	    	consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
	    }
	    //busqueda por el estado del pedido
	    if (formulario.getEstadoPedido() != null && !formulario.getEstadoPedido().equals("")){
			consultaVistaPedidoDTO.getId().setCodigoEstado(formulario.getEstadoPedido());
			//por omisi\u00F3n debe ser el estado actual
			//consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
		}
		
		// OANDINO: B\u00FAsqueda por estados de pago del pedido en el caso de la ventana Estado del Pedido ---------
		LogSISPE.getLog().info("Estado Pago Pedido: {}",formulario.getComboEstadoPagoPedido());
		if(formulario.getComboEstadoPagoPedido() != null &&!formulario.getComboEstadoPagoPedido().equals("")){
			consultaVistaPedidoDTO.setCodigoEstadoPagado(formulario.getComboEstadoPagoPedido());
		}
		
		//B\u00FAsqueda por estados de pago del pedido
		if (formulario.getEstadoPagoPedido() != null && !formulario.getEstadoPagoPedido().equals("")){
			consultaVistaPedidoDTO.setCodigoEstadoPagado(formulario.getEstadoPagoPedido());
		}

		//busqueda de los pedidos que por lo menos tengan una orden de compra
		if(formulario.getOpPedidoOrdenCompra()!=null && !formulario.getOpPedidoOrdenCompra().equals("")){
			consultaVistaPedidoDTO.setNpOrdenCompra(formulario.getOpPedidoOrdenCompra()); //estadoActivo o estadoInactivo
		}
		
		//busqueda por tipo de pedido
		if(StringUtils.isNotEmpty(formulario.getComboTipoPedido())){
			consultaVistaPedidoDTO.setCodigoTipoPedido(formulario.getComboTipoPedido());
		}
		
		//rango de fechas para las b\u00FAsquedas
		Timestamp fechaInicial = null;
		Timestamp fechaFinal = null;
		//se verifica los rangos de fechas a ser tomados en cuenta
		
		LogSISPE.getLog().info("Opci\u00F3n fecha: {}",formulario.getOpcionFecha());
		
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n Rango");
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaInicial(), 0, 0, 0, 0, 0, 0));
					LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaFinal(), 0, 0, 23, 59, 59, 999));
					LogSISPE.getLog().info("Fecha final: {}",fechaFinal);
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			//si ambas fechas permanecen nulas se busca por todo
			if(fechaInicial == null && fechaFinal == null){
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			}
		}
		
		//JACC: Si la busqueda es por fecha actual
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Hoy'");
			//se llenan las fechas
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
			fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 23, 59, 59, 999));
		}
		
		//cuando se busca por la opci\u00F3n todos
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Desde'");
			LogSISPE.getLog().info("N\u00FAmero de meses: {}",formulario.getNumeroMeses());
			
			int meses = 4;
			try{
				//se convierte a entero el n\u00FAmero de meses
				meses = Integer.parseInt(formulario.getNumeroMeses());
			}catch(NumberFormatException e) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
		}

		//se asignan las fechas de acuerdo a la acci\u00F3n
		if(accion!=null && (fechaInicial!=null || fechaFinal!=null) && limitarPorFechas){
			if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.pendienteProduccion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.actualizarProduccion")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho")) ||
					accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.listaControlProduccionPedEsp"))){
				LogSISPE.getLog().info("primera fecha despacho");
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
				
			}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega"))){
				consultaVistaPedidoDTO.setNpPrimeraFechaEntregaInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpPrimeraFechaEntregaFinal(fechaFinal);
				
			}else  if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despachoEspecial"))){
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
				
			}else if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"))){
				if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaDespacho"))){
					LogSISPE.getLog().info("Se ha elegido fecha despacho");
					consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
				}
				else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEntrega"))){
					LogSISPE.getLog().info("Se ha elegido fecha entrega");
					consultaVistaPedidoDTO.setNpPrimeraFechaEntregaInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpPrimeraFechaEntregaFinal(fechaFinal);
				}
				else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEstadoPedido"))){
					LogSISPE.getLog().info("Se ha elegido fecha estado");
					consultaVistaPedidoDTO.setNpFechaEstadoInicial(fechaInicial);
					consultaVistaPedidoDTO.setNpFechaEstadoFinal(fechaFinal);
				}
			}else{
				consultaVistaPedidoDTO.setNpFechaEstadoInicial(fechaInicial);
				consultaVistaPedidoDTO.setNpFechaEstadoFinal(fechaFinal);
			}
		}
		
		//se verifica si el usuario es de un local
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
		}else{
			//asignaci\u00F3n del local
			if(formulario.getCodigoLocal()!=null && !formulario.getCodigoLocal().equals("")){
				consultaVistaPedidoDTO.getId().setCodigoAreaTrabajo(new Integer(formulario.getCodigoLocal()));
			}
			//asignaci\u00F3n de la entidad responsable
			if(formulario.getOpEntidadResponsable()!=null && !formulario.getOpEntidadResponsable().equals("")){
				LogSISPE.getLog().info("Entro a realizar el filtro Entidad Responsable");
				consultaVistaPedidoDTO.setEntidadResponsable(formulario.getOpEntidadResponsable()); //BOD o LOC
			}	
		}

		return consultaVistaPedidoDTO;
	}

	/**
	 * Construye una consulta gen\u00E9rica para las b\u00FAsquedas de art\u00EDculos (sobre la VISTAARTICULO)
	 * 
	 * @param  request						Petici\u00F3n manejada actualmente
	 * @param  form								Formulario de donde se obtienen los campos de b\u00FAsqueda
	 * @return VistaPedidoDTO			Objeto que contiene los campos que utiliza luego el m\u00E9todo que realiza la
	 * 														b\u00FAsqueda
	 * @throws Exception
	 */
	public static VistaArticuloDTO construirConsultaVistaArticulos(HttpServletRequest request, ActionForm form)throws Exception{
		LogSISPE.getLog().info("Se procede a ingresar a construirConsultaVistaArticulos...");
		
		String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		LogSISPE.getLog().info("va a consultar en vistaArticuloDTO");
		//se crea el objeto que sirve para consulta
		VistaArticuloDTO consultaVistaArticuloDTO = new VistaArticuloDTO();
		consultaVistaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		//se verifica la instancia del formulario
		ListadoPedidosForm formulario = (ListadoPedidosForm)form;
		boolean limitarPorFechas = true;
		
		// IMPORTANTE: Modificaci\u00F3n de m\u00E9todo para habilitar b\u00FAsquedas m\u00FAltiples
		// ------------------------------------------------------------------------------------------------------------------
		//si se busca por n\u00FAmero de pedido
		/*if(formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.getId().setCodigoPedido(formulario.getCampoBusqueda().trim());
			limitarPorFechas = false;
		}*/
		
		if(formulario.getNumeroPedidoTxt()!= null && !formulario.getNumeroPedidoTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.getId().setCodigoPedido(formulario.getNumeroPedidoTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero pedido: {}",consultaVistaArticuloDTO.getId().getCodigoPedido());
			limitarPorFechas = false;
		}
		
		//si se busca por n\u00FAmero de reservacion
		/*if(formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroReserva"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo de la reservaci\u00F3n que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCodigoReservacion(formulario.getCampoBusqueda().trim());
			limitarPorFechas = false;
		}*/
		
		if(formulario.getNumeroReservaTxt()!= null && !formulario.getNumeroReservaTxt().trim().equals("")){
			//se asigna al DTO el codigo de la reservaci\u00F3n que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCodigoReservacion(formulario.getNumeroReservaTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero reserva: {}",consultaVistaArticuloDTO.getCodigoReservacion());
			limitarPorFechas = false;
		}
		
		//si se busca por n\u00FAmero de c\u00E9dula
		/*else if(formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroCedula"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCedulaContacto(formulario.getCampoBusqueda());
		}*/
		
		if(formulario.getDocumentoPersonalTxt()!= null && !formulario.getDocumentoPersonalTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCedulaContacto(formulario.getDocumentoPersonalTxt().trim());
			LogSISPE.getLog().info("Documento personal: {}",consultaVistaArticuloDTO.getCedulaContacto());
		}
		
		//si se busca por nombre del contacto
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreContacto"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setNombreContacto(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion nombreContacto");
		}*/
		
		if(formulario.getNombreContactoTxt()!= null && !formulario.getNombreContactoTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setNombreContacto(formulario.getNombreContactoTxt().trim());
			LogSISPE.getLog().info("Nombre contacto: {}",consultaVistaArticuloDTO.getNombreContacto());			
		}
		
		//si se busca por ruc de de la empresa
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.rucEmpresa"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setRucEmpresa(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion rucEmpresa desde vista articulo");
		}*/
		
		if(formulario.getRucEmpresaTxt()!= null && !formulario.getRucEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setRucEmpresa(formulario.getRucEmpresaTxt().trim());
			LogSISPE.getLog().info("Ruc: {}",consultaVistaArticuloDTO.getRucEmpresa());
		}
		
		//si se busca por nombre de la empresa
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreEmpresa"))
				&& !formulario.getCampoBusqueda().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setNombreEmpresa(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion nombreEmpresa desde vista articulo");
		}*/
		
		if(formulario.getNombreEmpresaTxt()!= null && !formulario.getNombreEmpresaTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setNombreEmpresa(formulario.getNombreEmpresaTxt().trim());
			LogSISPE.getLog().info("Nombre empresa: {}",consultaVistaArticuloDTO.getNombreEmpresa());
		}
		
		
		//si se busca por codigo de articulo
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.codigoArticulo"))
				&& !formulario.getCampoBusqueda().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.getId().setCodigoArticulo(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion codigoArticulo desde vista articulo");
		}*/
		
		if(formulario.getCodigoArticuloTxt()!= null && !formulario.getCodigoArticuloTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			//consultaVistaArticuloDTO.getId().setCodigoArticulo(formulario.getCodigoArticuloTxt().trim());
			//LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaVistaArticuloDTO.getId().getCodigoArticulo());
			consultaVistaArticuloDTO.setCodigoBarras(formulario.getCodigoArticuloTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",consultaVistaArticuloDTO.getCodigoBarras());
		}

		//si se busca por nombre de articulo
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.nombreArticulo"))
				&& !formulario.getCampoBusqueda().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setDescripcionArticulo(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion nombreArticulo desde vista articulo");
		}*/
		
		if(formulario.getDescripcionArticuloTxt()!= null && !formulario.getDescripcionArticuloTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setDescripcionArticulo(formulario.getDescripcionArticuloTxt().trim());
			LogSISPE.getLog().info("Nombre art\u00EDculo: {}",consultaVistaArticuloDTO.getDescripcionArticulo());
		}
		
		//si se busca por codigo de clasificacion
		/*else if (formulario.getOpcionCampoBusqueda().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"))
				&& !formulario.getCampoBusqueda().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCodigoClasificacion(formulario.getCampoBusqueda());
			LogSISPE.getLog().info("opcion codigoClasificacion desde vista articulo");
		}*/	

		if(formulario.getCodigoClasificacionTxt()!= null && !formulario.getCodigoClasificacionTxt().trim().equals("")){
			//se asigna al DTO el codigo del pedido que se ingreso como par\u00E1metro de busqueda
			consultaVistaArticuloDTO.setCodigoClasificacion(formulario.getCodigoClasificacionTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero clasificaci\u00F3n: {}",consultaVistaArticuloDTO.getCodigoClasificacion());
		}
		//asignaci\u00F3n de la entidad responsable
		if(formulario.getOpEntidadResponsable()!=null && !formulario.getOpEntidadResponsable().equals("")){
			LogSISPE.getLog().info("Entro a realizar el filtro Entidad Responsable");
			consultaVistaArticuloDTO.setEntidadResponsable(formulario.getOpEntidadResponsable()); //BOD o LOC
		}
		
		// ------------------------------------------------------------------------------------------------------------------
		
		//rango de fechas para las b\u00FAsquedas
		Timestamp fechaInicial = null;
		Timestamp fechaFinal = null;
		//se verifica los rangos de fechas a ser tomados en cuenta
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaInicial(), 0, 0, 0, 0, 0, 0));
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaFinal(), 0, 0, 23, 59, 59, 999));
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			//si ambas fechas permanecen nulas se busca por todo
			if(fechaInicial == null && fechaFinal == null){
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			}
		}
		
		//JACC: Si la busqueda es por fecha actual
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
			//se llenan las fechas
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
			fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 23, 59, 59, 999));
			//cuando se busca por la opci\u00F3n todos
		}
		
		//se busca por la opci\u00F3n todos
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
			//se convierte a entero el n\u00FAmero de meses
			int meses = Integer.parseInt(formulario.getNumeroMeses());
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
		}
		
		//se verifica la acci\u00F3n actual para asignar los tipos de fecha para la b\u00FAsqueda
		//se asignan las fechas de acuerdo a la acci\u00F3n
		if(accionActual!=null && (fechaInicial!=null || fechaFinal!=null) && limitarPorFechas){
			if(accionActual.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.registrarOrdenCompra"))){
//				if(limitarPorFechas){
					consultaVistaArticuloDTO.setNpFechaEstadoInicial(fechaInicial);
					consultaVistaArticuloDTO.setNpFechaEstadoFinal(fechaFinal);
//				}
			}else{
				//en el caso de b\u00FAsquedas por fechas de despacho o entrega siempre deben ser incluidas
				consultaVistaArticuloDTO.setNpPrimeraFechaDespachoInicialTimestamp(fechaInicial);
				consultaVistaArticuloDTO.setNpPrimeraFechaDespachoFinalTimestamp(fechaFinal);
			}
		}
		
		//se verifica si la entidad responsable es un local
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
			consultaVistaArticuloDTO.getId().setCodigoAreaTrabajo(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoAreaTrabajo());
		}else{
			if(formulario.getCodigoLocal()!=null && !formulario.getCodigoLocal().equals(""))
				consultaVistaArticuloDTO.getId().setCodigoAreaTrabajo(new Integer(formulario.getCodigoLocal()));
		}

		//se asigna el ordenamiento
		consultaVistaArticuloDTO.setNpOrderBy("descripcionArticulo");

		return consultaVistaArticuloDTO;
	}


	/**
	 * Obtiene el detalle de los descuentos en un estado espec\u00EDfico
	 * @param vistaPedidoDTO
	 * @param request
	 * @throws Exception
	 */
	public static void obtenerDescuentosEstadoPedido(VistaPedidoDTO vistaPedidoDTO, HttpServletRequest request,Boolean eliminarConsolidacion)throws Exception
	{
		//se verifica si el detalle ya tiene asignado descuentos 
		//Collection descuentosEstadosPedidos = vistaPedidoDTO.getDescuentosEstadosPedidos();
		Collection<DescuentoEstadoPedidoDTO> descuentosEstadosPedidos = null;
		if(descuentosEstadosPedidos == null){
			if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO ) != null && (Boolean)request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO )){
				Collection<VistaPedidoDTO> colVistaPedidoDTO= (Collection<VistaPedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.VISTA_PEDIDOS_CONSOLIDADOS);
				if(CollectionUtils.isNotEmpty(colVistaPedidoDTO)){
					 descuentosEstadosPedidos = new ArrayList<DescuentoEstadoPedidoDTO>();
					for(VistaPedidoDTO vistaPedidoActual : colVistaPedidoDTO){
						if(CollectionUtils.isNotEmpty(vistaPedidoActual.getDescuentosEstadosPedidos())){
							descuentosEstadosPedidos.addAll(vistaPedidoActual.getDescuentosEstadosPedidos());
						}
					}
					if( CollectionUtils.isEmpty((Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.RESPALDO_DESCUENTOS_CONSOLIDADOS))){
						request.getSession().setAttribute(CotizarReservarAction.RESPALDO_DESCUENTOS_CONSOLIDADOS, descuentosEstadosPedidos);
					}
				}
			}
			//creaci\u00F3n del objeto DescuentoEstadoPedidoDTO para la consulta de los estados
			DescuentoEstadoPedidoDTO descuentoEstadoPedidoDTO = new DescuentoEstadoPedidoDTO();
			descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
			descuentoEstadoPedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
			descuentoEstadoPedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
			descuentoEstadoPedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			descuentoEstadoPedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
			LogSISPE.getLog().info("<<<<<<<<<<<<<<Codigo de Pedido>>>>>>{}",vistaPedidoDTO.getId().getCodigoPedido());			
			descuentoEstadoPedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
			if(!eliminarConsolidacion){
				LogSISPE.getLog().info("trae los descuentos actuales");
				
				descuentosEstadosPedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoEstadoPedido(descuentoEstadoPedidoDTO);
				request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_APLICADOS,descuentosEstadosPedidos);
			}else{
				LogSISPE.getLog().info("trae los descuentos anteriores");
				descuentoEstadoPedidoDTO.setDescuentoDTO(new DescuentoDTO());
				descuentoEstadoPedidoDTO.getDescuentoDTO().setTipoDescuentoDTO(new TipoDescuentoDTO());
				descuentosEstadosPedidos=SessionManagerSISPE.getServicioClienteServicio().transObtenerDescuentoAntesConsolidar(descuentoEstadoPedidoDTO);
				request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_APLICADOS,descuentosEstadosPedidos);
			}

			
			//se buscan los descuentos variables
			Collection<DescuentoEstadoPedidoDTO> descuentosVariablesCol = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
			if(CollectionUtils.isEmpty(descuentosVariablesCol) && request.getSession().getAttribute(CotizarReservarAction.ELIMINAR_DESCUENTOS_CONSOLIDADOS) == null){
				 descuentosVariablesCol = new ArrayList<DescuentoEstadoPedidoDTO>();
				if(CollectionUtils.isNotEmpty(descuentosEstadosPedidos)){
					String codigoTipoDescuentoVariable = "";
					ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoVariable", request);
					if(parametroDTO.getValorParametro()!=null){
						codigoTipoDescuentoVariable = parametroDTO.getValorParametro();
					}
					//se verifica si es un descuento de tipo variable
					for(DescuentoEstadoPedidoDTO descuentoAct : (Collection<DescuentoEstadoPedidoDTO>) descuentosEstadosPedidos){
						if(descuentoAct.getDescuentoDTO().getTipoDescuentoDTO().getId().getCodigoTipoDescuento().equals(codigoTipoDescuentoVariable)){
							descuentosVariablesCol.add(descuentoAct);
						}
					}
				}
				
				//se valida que suba a sesion solo los descuentos variables seleccionados
				Collection<DescuentoEstadoPedidoDTO> descuentosActuales = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
				Collection<DescuentoEstadoPedidoDTO> descuentosVariablesActuales = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES);
				if(CollectionUtils.isEmpty(descuentosVariablesActuales)){
					descuentosVariablesActuales = new ArrayList<DescuentoEstadoPedidoDTO>();
				}
				
				if(CollectionUtils.isNotEmpty(descuentosActuales) && CollectionUtils.isNotEmpty(descuentosVariablesCol)){
					//se verifica que el descuento variable este aplicado actualmente // 273-CTD2-CMDCAN,  274-CTD17-CMDMON   //212-CTD3-CMDMON-COM01
					for(DescuentoEstadoPedidoDTO dsctoActual : descuentosActuales){
						for(DescuentoEstadoPedidoDTO dsctoVariable : descuentosVariablesCol){
							String llaveDsctoActual = dsctoActual.getLlaveDescuento();
							if(llaveDsctoActual != null && !llaveDsctoActual.equals("")){
								String llaveDsctoVariable = dsctoVariable.getLlaveDescuento();
								if(llaveDsctoVariable != null && !llaveDsctoVariable.equals("")){
									llaveDsctoActual = llaveDsctoActual.replace(llaveDsctoActual.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
									llaveDsctoVariable = llaveDsctoVariable.replace(llaveDsctoVariable.split(SEPARADOR_TOKEN)[0]+SEPARADOR_TOKEN+CODIGO_TIPO_DESCUENTO, CODIGO_TIPO_DESCUENTO);
									if(llaveDsctoVariable.contains(llaveDsctoActual) || llaveDsctoActual.contains(llaveDsctoVariable)){
										descuentosVariablesActuales.add(dsctoVariable);
										break;
									}
									
								}
							}
						}
					}
					//sube a sesion los descuentos variables
					request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES,descuentosVariablesActuales);
				}
				else if(CollectionUtils.isNotEmpty(descuentosVariablesCol)){
					//sube a sesion los descuentos variables
					request.getSession().setAttribute(CotizarReservarAction.COL_DESCUENTOS_VARIABLES,descuentosVariablesCol);
				}
			}
	
			vistaPedidoDTO.setDescuentosEstadosPedidos(descuentosEstadosPedidos);
			//verificar si los descuentos son de cajas o Mayorista para aplicarse, caso contrario no deben aplicarse
			if(!CollectionUtils.isEmpty(descuentosEstadosPedidos)){
				Collection<String> llavesProcesada=(Collection<String>)request.getSession().getAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS);
				
				//Si no existen llaves pero si descuentos, se toman las llaves de los descuentos
				//a esta opcion ingresa cuando la consolidacion viene por el lado de recotizacion
				
				//validacion para el descuento navidad empresarial credito
				String codigoTipoDesc = "";
				if (descuentosEstadosPedidos != null && descuentosEstadosPedidos.size() == 1) {
					if (descuentosEstadosPedidos.toArray(new DescuentoEstadoPedidoDTO[0])[0].getLlaveDescuento() != null) {
						codigoTipoDesc = descuentosEstadosPedidos.toArray(new DescuentoEstadoPedidoDTO[0])[0].getLlaveDescuento().split(SEPARADOR_TOKEN)[1].substring(3);
					}
				}
				
				if(request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL) != null 
						&& (request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion"))
						|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion"))
						|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion"))
						|| request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion")))) {
					if(CollectionUtils.isEmpty(llavesProcesada) || (CollectionUtils.isNotEmpty(llavesProcesada) && descuentosEstadosPedidos.size() != llavesProcesada.size())
							||(codigoTipoDesc.equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoDescuento.navEmp.credito")))){
						
						CotizacionReservacionUtil.obtenerCodigoTipoDescuentoPorCajasMayorista(request);
						String llaveDescuentoPorCajas = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
						String llaveDescuentoMayorista = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
						if(llavesProcesada == null)
							llavesProcesada = new ArrayList<String>();
						
						for(DescuentoEstadoPedidoDTO descuentoEstadoPedido : descuentosEstadosPedidos){
							//se verifica la llave porque es posible que existan descuentos incluidos por art\u00EDculo y estos no tienen una llave
							if(descuentoEstadoPedido.getLlaveDescuento()!= null){
								llavesProcesada.add(descuentoEstadoPedido.getLlaveDescuento());
							}
							else{
								if(descuentoEstadoPedido.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
									llavesProcesada.add(llaveDescuentoPorCajas);
								}
								else if(descuentoEstadoPedido.getId().getCodigoDescuento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
									llavesProcesada.add(llaveDescuentoMayorista);
								}
							}
						}
					}
					llavesProcesada = CotizacionReservacionUtil.eliminarLlavesRepetidas(llavesProcesada);
				}

				if(!CollectionUtils.isEmpty(llavesProcesada)){
					Collection<DescuentoEstadoPedidoDTO> colResp = CollectionUtils.select(descuentosEstadosPedidos, new Predicate() {
						
						public boolean evaluate(Object arg0) {
							DescuentoEstadoPedidoDTO desEstPed = (DescuentoEstadoPedidoDTO) arg0;
							DescuentoEstadoPedidoID idDesEstPed = desEstPed.getId();
							
							return idDesEstPed.getCodigoReferenciaDescuentoVariable().equals("CAJ");
						}
					});
					if(!CollectionUtils.isEmpty(colResp)){
						CotizacionReservacionUtil.iniciarDescuentoPorCajas(request,null);
					}else if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
						request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
						final String llaveDes = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORCAJAS);
						llavesProcesada=CollectionUtils.select(llavesProcesada, new Predicate() {
						public boolean evaluate(Object arg0) {
								String llaveEncontar=(String)arg0;
									return llaveEncontar!=null?!llaveEncontar.equals(llaveDes):false;
								}
						});
	
					}
					Collection<DescuentoEstadoPedidoDTO> colRespMay = CollectionUtils.select(descuentosEstadosPedidos, new Predicate() {
						
						public boolean evaluate(Object arg0) {
							DescuentoEstadoPedidoDTO desEstPed = (DescuentoEstadoPedidoDTO) arg0;
							DescuentoEstadoPedidoID idDesEstPed = desEstPed.getId();
							
							return idDesEstPed.getCodigoReferenciaDescuentoVariable().equals("MAY");
						}
					});
					if(!CollectionUtils.isEmpty(colRespMay)){
						CotizacionReservacionUtil.iniciarDescuentoPorMayorista(request);
					}else if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
						request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
						final String llaveDes = (String) request.getSession().getAttribute(CotizacionReservacionUtil.LLAVE_DESCUENTOS_PORMAYORISTA);
						llavesProcesada=CollectionUtils.select(llavesProcesada, new Predicate() {
						public boolean evaluate(Object arg0) {
								String llaveEncontar=(String)arg0;
									return llaveEncontar!=null?!llaveEncontar.equals(llaveDes):false;
								}
						});
	
					}
					request.getSession().setAttribute(CotizarReservarAction.COL_DESC_SELECCIONADOS,llavesProcesada);
					//inicializar los descuentos de cajas y mayoristas
					if(!CollectionUtils.isEmpty(llavesProcesada)){
						String [] descSelec= new String[llavesProcesada.size()];
						int pos=0;
						for(String llave: llavesProcesada){
							descSelec[pos]=llave;
							pos++;
						}
						LogSISPE.getLog().info("descuentos seleccionados "+descSelec.toString());
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS,descSelec);
					}else{
						request.getSession().setAttribute(CotizacionReservacionUtil.DESCUENTOS_SELECCIONADOS,new String[0]);
					}
				} else {//se deshabilitan las llaves de descuentos de cajas y mayorista
					if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
						request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
					}
					if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
						request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
					}
				}
			} 
			//bgudino si el pedido es consolidado y no tiene descuentos se deshabilita los precios de caja y mayorista para los nuevos detalles que ingresen al pedido
			else if(request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO ) != null 
					&& (Boolean)request.getSession().getAttribute(CotizarReservarAction.ES_PEDIDO_CONSOLIDADO)){
				if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porMayorista"))){
					request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORMAYORISTA,"NO");
				}
				if (!existeTipoDescuentoPedidoConsolidacion(request, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoDescuento.porCaja"))){
					request.getSession().setAttribute(CotizacionReservacionUtil.APLICA_DESCUENTOS_PORCAJAS,"NO");
				}
			}
			//Verifica el numero de bonos-Maxi solo si es reservacion - modificacionReserva
			if(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")) ||
					vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion")) ||
					vistaPedidoDTO.getId().getCodigoEstado().equals( MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")) ||
					vistaPedidoDTO.getId().getCodigoEstado().equals( MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))||
					vistaPedidoDTO.getId().getCodigoEstado().equals( MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))){
				//berifica numero/bonos-maxi-navidad
				CotizacionReservacionUtil.verificaCantidadBonosMaxiNavidad(descuentosEstadosPedidos, request,null);
			}else{
				//inicializo las variables a utilizar en la impresion del resumen del pedido para comprobante(BONO-MAXI-NAVIDAD)
				request.getSession().removeAttribute(CotizarReservarAction.NUMERO_BONOS_RECIBIR_COMPROBANTE_MAXI);
				request.getSession().removeAttribute(CotizarReservarAction.MONTO_MINIMO_COMPRA_COMPROBANTE_MAXI);
				request.getSession().removeAttribute(CotizarReservarAction.VALOR_BONO_COMPROBANTE_MAXI);
				request.getSession().removeAttribute(CotizarReservarAction.MONTO_CALCULADO_COMPROBANTE_MAXI);
			
			}
		}
		
	}
	/**
	 * Obtiene el los meses en que el articulo enviado puede ser diferido
	 * @param detallePedidoDTO
	 * @param request
	 * @throws Exception
	 */
	public static List<Duplex<CuotaDTO, Double>> obtenerListaDiferidos(VistaDetallePedidoDTO detallePedidoDTO, HttpServletRequest request)throws Exception
	{
		LogSISPE.getLog().info("Medoto que calcula los diferidos de un articulo");
		List<Duplex<CuotaDTO, Double>> colDiferidos = null;
		Duplex<CuotaDTO, Double> diferidoEXT = null;
		Double valorCuota = 0D;
		Double sinInteres = 1D;
		
		//obtengo de session la cuotas
		Collection<CuotaDTO> colCuotas =(ArrayList<CuotaDTO>)request.getSession().getAttribute(EstadoPedidoUtil.DIFERIDO_CUOTAS);
		//Verifico el listado de cuotas activas 
		if(colCuotas != null && colCuotas.size() > 0){
			LogSISPE.getLog().info("Tama\u00F1o de cuotas-->{}",colCuotas.size());
			colDiferidos =  new ArrayList<Duplex<CuotaDTO, Double>>();
			for(CuotaDTO cuota : colCuotas){
				//Validacion para cuotas sin intereses
				if(cuota.getValorInteres().equals(sinInteres)){
					//Verifico los rangos en los que cae le precio del articulo y a\u00F1ado a una collection de diferidos
					//if((detallePedidoDTO.getArticuloDTO().getPrecioArticuloIVA()* detallePedidoDTO.getCantidadEstado()) >= cuota.getValorMinimo()){
					if((detallePedidoDTO.getArticuloDTO().getPrecioBaseImp()* detallePedidoDTO.getCantidadEstado()) >= cuota.getValorMinimo()){
						diferidoEXT = new Duplex<CuotaDTO, Double>();
						diferidoEXT.setFirstObject(cuota);
						//Calculo del valor de la cuota
						//valorCuota = ((detallePedidoDTO.getArticuloDTO().getPrecioArticuloIVA()*detallePedidoDTO.getCantidadEstado())/cuota.getNumeroCuotas()) * cuota.getValorInteres();
						valorCuota = ((detallePedidoDTO.getArticuloDTO().getPrecioBaseImp()*detallePedidoDTO.getCantidadEstado())/cuota.getNumeroCuotas()) * cuota.getValorInteres();
						LogSISPE.getLog().info("Valor de la cuotas-->{}",valorCuota);
						diferidoEXT.setSecondObject(valorCuota);
						colDiferidos.add(diferidoEXT);
					}else{
						diferidoEXT = new Duplex<CuotaDTO, Double>();
						diferidoEXT.setFirstObject(cuota);
						LogSISPE.getLog().info("--Cuota que no aplica--{}",cuota.getNombre());
						diferidoEXT.setSecondObject(0D);
						colDiferidos.add(diferidoEXT);
					}
				}
			}
		}
		return colDiferidos;
	}
	/**
	 * Obtiene el los meses en que el articulo enviado puede ser diferido
	 * @param detallePedidoDTO
	 * @param request
	 * @throws Exception
	 */
	public static List<Duplex<CuotaDTO, Double>> obtenerListaDiferidos1(DetallePedidoDTO detallePedidoDTO, HttpServletRequest request)throws Exception
	{
		LogSISPE.getLog().info("Medoto que calcula los diferidos de un articulo");
		List<Duplex<CuotaDTO, Double>> colDiferidos = null;
		Duplex<CuotaDTO, Double> diferidoEXT = null;
		Double valorCuota = 0D;
		Double sinInteres = 1D;
		//obtengo de session la cuotas
		Collection<CuotaDTO> colCuotas =(ArrayList<CuotaDTO>)request.getSession().getAttribute(EstadoPedidoUtil.DIFERIDO_CUOTAS);
		//Verifico el listado de cuotas activas 
		if(colCuotas != null && colCuotas.size() > 0){
			LogSISPE.getLog().info("Tama\u00F1o de cuotas-->{}",colCuotas.size());
			colDiferidos =  new ArrayList<Duplex<CuotaDTO, Double>>();
			for(CuotaDTO cuota : colCuotas){
				//Validacion para cuotas sin intereses
				if(cuota.getValorInteres().equals(sinInteres)){
					//Verifico los rangos en los que cae le precio del articulo y a\u00F1ado a una collection de diferidos
					if((detallePedidoDTO.getArticuloDTO().getPrecioBaseImp()* detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado()) >= cuota.getValorMinimo()){
						diferidoEXT = new Duplex<CuotaDTO, Double>();
						diferidoEXT.setFirstObject(cuota);
						//Calculo del valor de la cuota
						valorCuota = ((detallePedidoDTO.getArticuloDTO().getPrecioBaseImp()*detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado())/cuota.getNumeroCuotas()) * cuota.getValorInteres();
						LogSISPE.getLog().info("Valor de la cuotas-->{}",valorCuota);
						diferidoEXT.setSecondObject(valorCuota);
						colDiferidos.add(diferidoEXT);
					}else{
						diferidoEXT = new Duplex<CuotaDTO, Double>();
						diferidoEXT.setFirstObject(cuota);
						LogSISPE.getLog().info("--Cuota que no aplica--{}",cuota.getNombre());
						diferidoEXT.setSecondObject(0D);
						colDiferidos.add(diferidoEXT);
					}
				}
			}
		}
		return colDiferidos;
	}
	

	/**
	 * Calcula el valor del IVA de un determinado valor tomando en cuenta los descuentos.
	 * 
	 * @param valorAplicaIVA	- Valor sobre el cual se calcula el IVA
	 * @param valorIVA				- El valor del porcentaje del IVA
	 * @param request					- La petici\u00F3n HTTP
	 * @return								- El valor calculado con el IVA incluido
	 * @throws Exception
	 */
	public static Double obtenerValorIVA(Double valorAplicaIVA, Double valorIVA, HttpServletRequest request)throws Exception{
		Double porcentajeTotalDescuento = (Double)request.getSession().getAttribute(CotizarReservarAction.PORCENTAJE_TOT_DESCUENTO);
		double valorCalculadoIVA = valorAplicaIVA * (1 - porcentajeTotalDescuento/100) * valorIVA;
		return Util.roundDoubleMath(Double.valueOf(valorCalculadoIVA),NUMERO_DECIMALES);
	}

	/**
	 * Asigna las variables necesarias para realizar la pregunta de confirmaci\u00F3n.
	 * 
	 * @param keyConfirmacion						- Clave de un archivo properties que contiene el mensaje a ser mostrado.
	 * @param preguntaConfirmacion			- Contenido de la pregunta de confirmaci\u00F3n.
	 * @param valorSI										- El valor para la respuesta SI
	 * @param valorNO										- El valor para la respuesta NO
	 * @param request										- La petici\u00F3n HTTP
	 * @return													- El valor calculado con el IVA incluido
	 * @throws Exception
	 */
	public static void asignarVariablesPreguntaConfirmacion(String keyConfirmacion, String preguntaConfirmacion,
			String valorSI, String valorNO, HttpServletRequest request){
		//se asignan las variables de sesi\u00F3n necesarias para la pregunta
		request.setAttribute("ec.com.smx.sic.sispe.pedido.mensajeConfirmacion",keyConfirmacion);
		request.setAttribute("ec.com.smx.sic.sispe.pedido.preguntaConfirmacion",preguntaConfirmacion);
		request.setAttribute("ec.com.smx.sic.sispe.pedido.botonSI",valorSI);
		request.setAttribute("ec.com.smx.sic.sispe.pedido.botonNO",valorNO);
		request.setAttribute("ec.com.smx.sic.sispe.mostrarConfirmacion","ok");
	}
	
	/**
	 * Asigna los valores a las variables de sesi\u00F3n para la impresi\u00F3n del pedido
	 * @param request
	 * @param estadoActivo
	 */
	public static void inicializarParametrosImpresion(HttpServletRequest request, String estadoActivo, boolean esImpresion)throws Exception{
		request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.impresionEntregas");
		request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.impresionDescuentos");
		request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.impresionCanastos");
		request.getSession().removeAttribute("ec.com.smx.sic.sispe.pedido.impresionDiferidos");
		//variables para el control de las secciones del reporte que van a generar
		if(request.getParameter("opEntregas")!=null && request.getParameter("opEntregas").equals(estadoActivo))
			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.impresionEntregas", "ok");
		if(request.getParameter("opDescuentos")!=null && request.getParameter("opDescuentos").equals(estadoActivo))
			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.impresionDescuentos", "ok");
		if(request.getParameter("opCanastos")!=null && request.getParameter("opCanastos").equals(estadoActivo)){
			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.impresionCanastos", "ok");
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			//subClasificacion para articulos tipo canastos 
			String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
			request.getSession().setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasto", valorTipoArticuloCanastas);
			//subClasificacion para articulos tipo despensas 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
	  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
	  		request.getSession().setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa", valorTipoArticuloDespensas);
			//se obtiene la acci\u00F3n actual para saber desde donde se est\u00E1 llamando al m\u00E9todo
//			String accionActual = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
//			if(!MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.cotizacion").equals(accionActual)
//					&& !MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizacion").equals(accionActual)
//					&& !MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reservacion").equals(accionActual)){
				//si se desea mostrar el detalle de las recetas
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				if(vistaPedidoDTO != null && vistaPedidoDTO.getVistaDetallesPedidosReporte() != null){
					//se cargan los detalles de las recetas en caso de que no se hayan consultado todav\u00EDa
//					String tipoArticuloRecetaCanasta = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta");
//					String tipoArticuloRecetaDespensa = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa");
					for(Iterator<VistaDetallePedidoDTO> it = vistaPedidoDTO.getVistaDetallesPedidosReporte().iterator(); it.hasNext();){
						VistaDetallePedidoDTO vistaDetallePedidoDTO = it.next();
						//si es una receta
//		  			if(vistaDetallePedidoDTO.getCodigoTipoArticulo()!=null && 
//		  					(vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaCanasta) || vistaDetallePedidoDTO.getCodigoTipoArticulo().equals(tipoArticuloRecetaDespensa))){
//		  			
			  			if(vistaDetallePedidoDTO.getCodigoClasificacion()!=null && 
			  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
			  					vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
			  				
			  				if(vistaDetallePedidoDTO.getArticuloDTO()== null)
			  					vistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
			  				
			  				if( !vistaDetallePedidoDTO.getArticuloDTO().getTieneArticuloRelacionado()){
				  				//se carga la receta del art\u00EDculo
				  				Collection recetaArticulo = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
				  				vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticulo);
			  				}
			  			}
					}
//				}
			}
		}
		//Validacion Articulos diferidos
		if(request.getParameter("opDiferidos")!=null && request.getParameter("opDiferidos").equals(estadoActivo)){
			request.getSession().setAttribute("ec.com.smx.sic.sispe.pedido.impresionDiferidos", "ok");
			//Verifico de donde proviene el reporte diferidos
			if(request.getSession().getAttribute(EstadoPedidoUtil.ACCION_ORIGEN) != null && 
					request.getSession().getAttribute(EstadoPedidoUtil.ACCION_ORIGEN).equals("ESTADO_PED")){
				LogSISPE.getLog().info("---Impresion por el estado pedido");
				buscarDiferidos(request);
			}else if(request.getSession().getAttribute(EstadoPedidoUtil.ACCION_ORIGEN) != null && 
					request.getSession().getAttribute(EstadoPedidoUtil.ACCION_ORIGEN).equals("COT_PED")){
		  		LogSISPE.getLog().info("---Impresion por resumen de cotizacion - recotizacion");
		  		buscarDiferidos1(request);
			}
		}	
		if(esImpresion)
		 //variable para llamar a la funci\u00F3n estandar que realiza la impresi\u00F3n 
	 	 request.setAttribute("ec.com.smx.sic.sispe.funcionImprimir", "ok");
	}

	/**
	 * Obtiene el objeto <code>ParamteroDTO</code>
	 * @param  claveCodigoParamtero	- La clave del c\u00F3digo del par\u00E1metro que se desea obtener esta clave est\u00E1 en el archivo properties de la aplicaci\u00F3n
	 * @return objeto <code>ParamteroDTO</code>
	 */
	public static ParametroDTO obtenerParametroAplicacion(String claveCodigoParametro, HttpServletRequest request)throws Exception
	{
		//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
		ParametroDTO consultaParametroDTO = new ParametroDTO(Boolean.TRUE);
		consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaParametroDTO.getId().setCodigoParametro(MessagesAplicacionSISPE.getString(claveCodigoParametro));
		Collection<ParametroDTO> parametros = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
		if(!parametros.isEmpty()){
			//se obtiene el primer elemento de la colecci\u00F3n
			Iterator <ParametroDTO> it = parametros.iterator();
			consultaParametroDTO = it.next();
		}else{
			LogSISPE.getLog().info("No se encontr\u00F3 el par\u00E1metro de clave: {}", claveCodigoParametro);
		}
		return consultaParametroDTO;
	}

	/**
	 * Obtiene el objeto <code>ParamteroDTO</code>
	 * @param  clavesCodigoParamtero	- La clave del c\u00F3digo del par\u00E1metro que se desea obtener
	 * 																	esta clave est\u00E1 en el archivo properties de la aplicaci\u00F3n
	 * @return objeto <code>ParamteroDTO</code>
	 */
	public static Collection<ParametroDTO> obtenerParametrosAplicacion(String [] codigosParametros, HttpServletRequest request)throws Exception
	{
		Collection<ParametroDTO> colParametroDTO = null;
		String codigos = "";
		if(codigosParametros!=null){
			for(int i=0;i<codigosParametros.length;i++){
				codigos = codigos.concat(codigosParametros[i]).concat(",");
			}
			codigos = codigos.substring(0, codigos.length() - 1);
			
			//se obtiene el par\u00E1metro que me indica la fecha m\u00EDnima de entrega
			ParametroDTO consultaParametroDTO = new ParametroDTO(Boolean.TRUE);
			consultaParametroDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			consultaParametroDTO.getId().setCodigoParametro(codigos);
			
			colParametroDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametro(consultaParametroDTO);
		}

		return colParametroDTO;
	}
	
	/**
	 * Verifica si en el pedido existen articulo que aplican diferidos
	 * @return true = hay articulos que aplican false = no hay articulos que aplican
	 */
	public static void verificarDiferidosPedido(HttpServletRequest request)throws Exception
	{
		LogSISPE.getLog().info("Entro al metodo verificarDiferidos--");
		boolean flag = false;
		String valor;
  		String codigoClasificionPadre;
  		
  		//se obtiene los codigo de clasificaciones de departamentos que aplican diferidos 
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentosDiferidos", request);
		String[] codigoDepartamentos = parametroDTO.getValorParametro().split(",");
		
		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
  		
		if(vistaPedidoDTO.getVistaDetallesPedidosReporte() != null && !vistaPedidoDTO.getVistaDetallesPedidosReporte().isEmpty()){
			for(VistaDetallePedidoDTO vista : (Collection<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
	  			valor = vista.getCodigoClasificacion();
	  			codigoClasificionPadre = valor.substring(0, 2);
	  			//recorre todos los dtos que aplican diferidos
	  			for(int i=0; i<codigoDepartamentos.length; i++){
	  				if(codigoClasificionPadre.equals(codigoDepartamentos[i])
//	  						 && vista.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item"))){
	  					&& !vista.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
	  					   !vista.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){	
	  					flag = true;
	  					break;
	  				}
	  			}
	  			if(flag){
	  				break;
	  			}
	  		}
			if(flag){
				request.getSession().setAttribute(FLAG_DIFERIDOS, "OK");
			}else{
				request.getSession().removeAttribute(FLAG_DIFERIDOS);
			}
		}
	}
	
	/**
	 * Verifica si en el pedido existen articulo que aplican diferidos
	 * @return true = hay articulos que aplican false = no hay articulos que aplican
	 */
	public static void verificarDiferidosCotizarRes(HttpServletRequest request)throws Exception
	{
		LogSISPE.getLog().info("Entro al metodo verificarDiferidos--");
		boolean flag = false;
		String valor;
  		String codigoClasificionPadre;
  		
  		//se obtiene los codigo de clasificaciones de departamentos que aplican diferidos 
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentosDiferidos", request);
		String[] codigoDepartamentos = parametroDTO.getValorParametro().split(",");
		
		//se obtiene el detalle del Pedido
		Collection <DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		if(colDetallePedidoDTO != null && !colDetallePedidoDTO.isEmpty()){
			for(DetallePedidoDTO detalle : colDetallePedidoDTO){
				flag = false;
				valor = detalle.getArticuloDTO().getCodigoClasificacion();
	  			codigoClasificionPadre = valor.substring(0, 2);
	  			LogSISPE.getLog().info("codigoClasificacionPadre--{}",codigoClasificionPadre);
	  			
	  			//recorre todos los dtos que aplican diferidos
	  			for(int i=0; i<codigoDepartamentos.length; i++){
	  				if(codigoClasificionPadre.equals(codigoDepartamentos[i])
//	  						 && detalle.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item"))){
	  						&& !detalle.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
		  					   !detalle.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
	  					LogSISPE.getLog().info("Aplica diferido el articulo--{}",detalle.getArticuloDTO().getDescripcionArticulo());
	  					flag = true;
	  					break;
	  				}
	  			}
	  			if(flag){
	  				break;
	  			}
			}
			if(flag){
				request.getSession().setAttribute(FLAG_DIFERIDOS, "OK");
			}else{
				request.getSession().removeAttribute(FLAG_DIFERIDOS);
			}
		}
	}

	/**
	 * Ordena los datos de una colecci\u00F3n que representa una tabla en una p\u00E1gina jsp, por un determinado campo.
	 * 
	 * @param request
	 * @param indiceOrdenar
	 * @param nivel
	 * @param colOrdenar
	 * @return
	 * @throws Exception
	 */
	public static Collection ordenarColeccionDatos(HttpServletRequest request, int indiceOrdenar, 
			Collection colOrdenar, String nivel)throws Exception{

		HttpSession session = request.getSession();
		//colecci\u00F3n que almacenar\u00E1 los datos ordenados
		Collection colOrdenada = new ArrayList(); 
		String datosTablaOrdenar = SessionManagerSISPE.DATOS_TABLA_ORDENAR;
		String datosColumnaOrdenada = SessionManagerSISPE.DATOS_COLUMNA_ORDENADA;

		//se concatena el nivel al nombre de la variable, en el caso de que se trate de ordenar una tabla anidada
		//en otra
		if(nivel != null){
			datosTablaOrdenar = datosTablaOrdenar + nivel;
			datosColumnaOrdenada = datosColumnaOrdenada + nivel;
			LogSISPE.getLog().info("nivel no es nulo");
		}

		//se obtiene la colecci\u00F3n de datos de la tabla
		String [][] datosTabla = (String [][])session.getAttribute(datosTablaOrdenar);
		if(datosTabla!=null){
			LogSISPE.getLog().info("va a ordenar");
			//se obtiene los datos de la columna
			String columnaOrdenamiento [] = datosTabla[indiceOrdenar];
			//variable para determinar el ordenamiento
			int tipoOrden = ColeccionesUtil.ORDEN_ASC;
			String descripcionOrden = "Ascendente";

			//se verifica la columna que indica el tipo de orden
			if(columnaOrdenamiento[2]!=null)
				if(columnaOrdenamiento[2].equals(Integer.toString(ColeccionesUtil.ORDEN_ASC))){
					tipoOrden = ColeccionesUtil.ORDEN_DESC;
					descripcionOrden = "Descendente";
				}

			//se llama a la funci\u00F3n que realiza el ordenamiento
			colOrdenada = ColeccionesUtil.sort(colOrdenar, tipoOrden, columnaOrdenamiento[0]);

			//se incializan los datos de la columna de ordenamiento
			for(int i=0; i<datosTabla.length;i++){
				datosTabla[i][2] = null;
			}

			//se asigna el ordenamiento actual en la columna adecuada
			datosTabla[indiceOrdenar][2] = Integer.toString(tipoOrden);

			//se actualiza la variable de sesi\u00F3n
			session.setAttribute(datosTablaOrdenar, datosTabla);

			//se actualiza la variable que indica la columna ordenada
			session.setAttribute(datosColumnaOrdenada, new String [] {datosTabla[indiceOrdenar][1], descripcionOrden});
		}

		return colOrdenada;
	}

	/**
	 * 
	 * @param fecha					- Fecha base
	 * @param mes						-	Cantidad de meses a sumar a la fecha
	 * @param dia						-	Cantidad de d\u00EDas a sumar a la fecha
	 * @param hora					-	Cantidad de horas a sumar a la fecha
	 * @param minuto				-	Cantidad de minutos a sumar a la fecha
	 * @param segundo				-	Cantidad de segundos a sumar a la fecha
	 * @param milisegundo		-	Cantidad de milisegundos a sumar a la fecha
	 * @return
	 */
	public static long construirFechaCompleta(String fecha, int mes, int dia, int hora, int minuto, int segundo, int milisegundo){
		GregorianCalendar hoy = new GregorianCalendar();

		if (fecha != null){
			hoy.setTime(ConverterUtil.parseStringToDate(fecha));
		}
		hoy.set(Calendar.HOUR_OF_DAY, hora);
		hoy.set(Calendar.MINUTE, minuto);
		hoy.set(Calendar.SECOND, segundo);
		hoy.set(Calendar.MILLISECOND, milisegundo);
		
		hoy.add(Calendar.DAY_OF_MONTH, dia);
		hoy.add(Calendar.MONTH, mes);
		return hoy.getTimeInMillis();
	}

	/**
	 * 
	 * @param fecha					- Fecha base
	 * @param mes						-	Cantidad de meses a sumar a la fecha
	 * @param dia						-	Cantidad de d\u00EDas a sumar a la fecha
	 * @param hora					-	Cantidad de horas a sumar a la fecha
	 * @param minuto				-	Cantidad de minutos a sumar a la fecha
	 * @param segundo				-	Cantidad de segundos a sumar a la fecha
	 * @param milisegundo		-	Cantidad de milisegundos a sumar a la fecha
	 * @return
	 */
	public static long construirFechaCompleta(Date fecha, int mes, int dia, int hora, int minuto, int segundo, int milisegundo){
		GregorianCalendar hoy = new GregorianCalendar();

		if (fecha != null){
			hoy.setTime(fecha);
		}
		hoy.set(Calendar.HOUR_OF_DAY, hora);
		hoy.set(Calendar.MINUTE, minuto);
		hoy.set(Calendar.SECOND, segundo);
		hoy.set(Calendar.MILLISECOND, milisegundo);
		
		hoy.add(Calendar.DAY_OF_MONTH, dia);
		hoy.add(Calendar.MONTH, mes);
		return hoy.getTimeInMillis();
	}
	
	/**
	 * Carga los datos de un pedido especial para realizar la confirmacion del pedido 
	 * 
	 * @param  formulario			El formulario donde se mostrar\u00E1n los datos
	 * @param  request				La petici\u00F3n que actualmente se est\u00E1 procesando
	 */
	public static void construirDetallesPedidoEspecialDesdeVista(CrearPedidoForm formulario, HttpServletRequest request)throws Exception
	{
		HttpSession session = request.getSession();
		//se obtienen las claves que indican un estado inactivo
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);

		String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		
		//se obtiene la colecci\u00F3n de los pedidos 
		ArrayList pedidos = (ArrayList)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
		//colecci\u00F3n que almacenar\u00E1 el detalle del pedido seleccionado
		ArrayList detalleVistaPedido = new ArrayList();
		ArrayList <DetallePedidoDTO> detallePedido = new ArrayList <DetallePedidoDTO>();
		Collection <String> codigosArticulos = new ArrayList <String>();

		int indice = Integer.parseInt(request.getParameter("indice"));
		//creaci\u00F3n del DTO para almacenar el pedido selecionado
		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedidos.get(indice);
		
		//se realiza la consulta del especial que corresponde al tipo de pedido
		EspecialDTO especialDTO = new EspecialDTO(Boolean.TRUE);
		especialDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		especialDTO.setCodigoTipoPedido(vistaPedidoDTO.getCodigoTipoPedido());
		especialDTO.setEspecialesClasificaciones(new ArrayList());
		
		Collection<EspecialDTO> especiales = SessionManagerSISPE.getServicioClienteServicio().transObtenerEspecial(especialDTO);
		session.setAttribute(CrearPedidoAction.COL_TIPO_ESPECIAL, especiales);
		//se verifican las clasificaciones del especial en la posici\u00F3n cero ya que la colecci\u00F3n contiene un solo 
		//elemento
		WebSISPEUtil.obtenerClasificacionEspecial(request, (List<EspecialDTO>)especiales, 0);

		//se obtiene la colecci\u00F3n de locales para escoger el local de entrega
		SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
		
		//se guarda en sesi\u00F3n el pedido seleccionado
		session.setAttribute(SessionManagerSISPE.VISTA_PEDIDO, vistaPedidoDTO);
		//se guarda el c\u00F3digo del local
		session.setAttribute(CODIGO_LOCAL_REFERENCIA, vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		//se guarda el c\u00F3digo de establecimiento
		session.setAttribute(CODIGO_ESTABLECIMIENTO_REFERENCIA, vistaPedidoDTO.getCodigoEstablecimiento());

		//se verifica si el usuario logeado es de la bodega
		String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
		if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){      
			session.setAttribute(CrearPedidoAction.ES_ENTIDAD_BODEGA,"ok");

			//se cargan los datos del local
			LocalID localID = new LocalID();
			localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			localID.setCodigoLocal(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
			LocalDTO localDTO = SessionManagerSISPE.getCorpAdmParametrosGeneralesService(request).findLocalById(localID);

			if(localDTO!=null){
				//se guarda el telefono y el nombre del administrador del local
				session.setAttribute(CrearPedidoAction.TELEFONO_LOCAL, localDTO.getTelefonoLocal());
				session.setAttribute(CrearPedidoAction.ADMINISTRADOR_LOCAL, localDTO.getNombreAdministrador());
			}
		}
		session.setAttribute(CrearPedidoAction.LOCAL_ANTERIOR, "");

		//se consulta el detalle del pedido seleccionado y se lo almacena en sesi\u00F3n
		VistaDetallePedidoDTO consultaVistaDetallePedidoDTO = new VistaDetallePedidoDTO();
		consultaVistaDetallePedidoDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
		consultaVistaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		consultaVistaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
		consultaVistaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
		consultaVistaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
		consultaVistaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
		detalleVistaPedido = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaVistaDetallePedidoDTO);

		String caracterToken = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
		//se crea la colecci\u00F3n de art\u00EDculos para realizar la consulta de los stocks y alcances
		Collection<ArticuloDTO> articulos = new ArrayList <ArticuloDTO>();

		boolean especialPavos = request.getSession().getAttribute(PedidoPerecibleAction.PEDIDO_ESPECIAL_PERECIBLE) == null ? false : true;
		
		//se itera la vistaDetallePedido para crear un DetallePedidoDTO
		for (int i=0;i<detalleVistaPedido.size();i++)
		{
			VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)detalleVistaPedido.get(i);
			DetallePedidoDTO detallePedidoDTO = new DetallePedidoDTO();
			
			detallePedidoDTO.setCodigoBarras(vistaDetallePedidoDTO.getCodigoBarras());
			
			//se crea el DetallePedidoDTO para poder almacenarlo en la reservaci\u00F3n
			detallePedidoDTO.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			detallePedidoDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			detallePedidoDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			detallePedidoDTO.getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			detallePedidoDTO.setArticuloDTO(vistaDetallePedidoDTO.getArticuloDTO());

			//creaci\u00F3n del estado del detalle del pedido
			EstadoDetallePedidoDTO estadoDetallePedidoDTO = new EstadoDetallePedidoDTO();
			estadoDetallePedidoDTO.getId().setCodigoCompania(vistaDetallePedidoDTO.getId().getCodigoCompania());
			estadoDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaDetallePedidoDTO.getId().getCodigoAreaTrabajo());
			estadoDetallePedidoDTO.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
			estadoDetallePedidoDTO.getId().setCodigoEstado(vistaDetallePedidoDTO.getId().getCodigoEstado());
			estadoDetallePedidoDTO.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
			estadoDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
			estadoDetallePedidoDTO.setCantidadEstado(vistaDetallePedidoDTO.getCantidadEstado());
			estadoDetallePedidoDTO.setCantidadParcialEstado(0l);
			estadoDetallePedidoDTO.setReservarBodegaSIC(estadoInactivo);
			estadoDetallePedidoDTO.setCantidadReservarSIC(0l);
			estadoDetallePedidoDTO.setPesoArticuloEstadoReservado(0d);
			estadoDetallePedidoDTO.setEspecialReservado(estadoInactivo);
			LogSISPE.getLog().info("******observacion***** {}" , vistaDetallePedidoDTO.getObservacionArticulo());
			estadoDetallePedidoDTO.setObservacionArticulo(vistaDetallePedidoDTO.getObservacionArticulo());
			estadoDetallePedidoDTO.setValorCajaIVAEstado(vistaDetallePedidoDTO.getValorCajaIVAEstado());
			estadoDetallePedidoDTO.setEstadoCanCotVacio(estadoInactivo);
			//estadoDetallePedidoDTO.setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getArticuloDTO().getCantidadMinimaMayoreo());
			estadoDetallePedidoDTO.setCantidadMinimaMayoreoEstado(vistaDetallePedidoDTO.getArticuloDTO().getCantidadMayoreo());
			
			//se inicializa el peso aproximado
			estadoDetallePedidoDTO.setPesoArticuloEstado(vistaDetallePedidoDTO.getPesoArticuloEstado());
			estadoDetallePedidoDTO.setAplicaIVA(vistaDetallePedidoDTO.getAplicaIVA());

			estadoDetallePedidoDTO.setCantidadPrevioEstadoDescuento(0l);
			estadoDetallePedidoDTO.setValorPrevioEstadoDescuento(0d);
			//precios de afiliado
			estadoDetallePedidoDTO.setValorUnitarioEstado(vistaDetallePedidoDTO.getValorUnitarioEstado());
			estadoDetallePedidoDTO.setValorUnitarioIVAEstado(vistaDetallePedidoDTO.getValorUnitarioIVAEstado());
			estadoDetallePedidoDTO.setValorCajaEstado(vistaDetallePedidoDTO.getValorCajaEstado());
			estadoDetallePedidoDTO.setValorCajaIVAEstado(vistaDetallePedidoDTO.getValorCajaIVAEstado());
			estadoDetallePedidoDTO.setValorMayorista(vistaDetallePedidoDTO.getValorMayoristaEstado());
			estadoDetallePedidoDTO.setValorMayoristaIVA(vistaDetallePedidoDTO.getValorMayoristaIVAEstado());
			//precios de no afiliado
			estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(vistaDetallePedidoDTO.getValorUnitarioNoAfiliado());
			estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(vistaDetallePedidoDTO.getValorUnitarioIVANoAfiliado());
			estadoDetallePedidoDTO.setValorCajaNoAfiliado(vistaDetallePedidoDTO.getValorCajaNoAfiliado());
			estadoDetallePedidoDTO.setValorCajaIVANoAfiliado(vistaDetallePedidoDTO.getValorCajaIVANoAfiliado());
			estadoDetallePedidoDTO.setValorMayoristaNoAfiliado(vistaDetallePedidoDTO.getValorMayoristaNoAfiliado());
			estadoDetallePedidoDTO.setValorMayoristaIVANoAfiliado(vistaDetallePedidoDTO.getValorMayoristaIVANoAfiliado());
			
			//respaldo de precios originales
			estadoDetallePedidoDTO.setValorUnitarioEstadoOriginal(vistaDetallePedidoDTO.getValorUnitarioEstado());
			estadoDetallePedidoDTO.setValorUnitarioIVAEstadoOriginal(vistaDetallePedidoDTO.getValorUnitarioIVAEstado());
			estadoDetallePedidoDTO.setValorCajaEstadoOriginal(vistaDetallePedidoDTO.getValorCajaEstado());
			estadoDetallePedidoDTO.setValorCajaIVAEstadoOriginal(vistaDetallePedidoDTO.getValorCajaIVAEstado());
			
			estadoDetallePedidoDTO.setValorTotalEstado(0d);
			estadoDetallePedidoDTO.setValorTotalEstadoIVA(0d);
			estadoDetallePedidoDTO.setValorTotalEstadoDescuento(0d);
			estadoDetallePedidoDTO.setValorTotalEstadoNeto(0d);
			estadoDetallePedidoDTO.setValorTotalEstadoNetoIVA(0d);
			estadoDetallePedidoDTO.setValorTotalVenta(0d);
			
			if (especialPavos) {
				estadoDetallePedidoDTO.setNpCantidadAnterior(vistaDetallePedidoDTO.getCantidadEstado());
			}
			
			//se actualiza el estado del detalle en detallePedidoDTO
			detallePedidoDTO.setEstadoDetallePedidoDTO(estadoDetallePedidoDTO);
			detallePedidoDTO.setNpContadorEntrega(0l);
			//campos para registrar el pedido por departamento
			detallePedidoDTO.setNpDepartamento(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento());
			detallePedidoDTO.setNpDepartamentoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getNpDepartamento()
					+ caracterToken
					+ vistaDetallePedidoDTO.getArticuloDTO().getId().getCodigoArticulo());


			//llamada al m\u00E9todo que construye la consulta de los art\u00EDculos
			WebSISPEUtil.construirConsultaArticulos(request,detallePedidoDTO.getArticuloDTO(),estadoInactivo, 
					estadoInactivo, accionActual);
			//se almacenan los detalles y los art\u00EDculos
			detallePedido.add(detallePedidoDTO);
			articulos.add(detallePedidoDTO.getArticuloDTO());
			//se almacenan los codigos de art\u00EDculos del detalle
			codigosArticulos.add(vistaDetallePedidoDTO.getId().getCodigoArticulo());

		}

		//llamada al m\u00E9todo que obtiene el alcance y stock de los art\u00EDculos
		SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosCodigosBarras(articulos);

		/*------------------- se asignan los atributos necesarios al formulario --------------------*/
//		formulario.setNumeroDocumento(vistaPedidoDTO.getCedulaContacto());
//		formulario.setNombreContacto(vistaPedidoDTO.getNombreContacto());
//		formulario.setTelefonoContacto(vistaPedidoDTO.getTelefonoContacto());
		LogSISPE.getLog().info("** TIPO DE PEDIDO **: {}",vistaPedidoDTO.getContextoPedido());
		
		//se asigna el contexto del pedido al formulario
		if(vistaPedidoDTO.getContextoPedido().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"))){
//			formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.empresarial"));
//			formulario.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
//			formulario.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());	
			formulario.setNumeroDocumento(vistaPedidoDTO.getRucEmpresa());
//			formulario.setTelefonoEmpresa(vistaPedidoDTO.getTelefonoEmpresa());
//			formulario.setNombreContacto(null);
//			formulario.setNumeroDocumentoContacto(null);
//			formulario.setNombrePersona(null);
//			formulario.setNumeroDocumentoPersona(null);
//			formulario.setEmailEnviarCotizacion(null);
//
//			new ContactoUtil();
//			EmpresaDTO empresaDto = ContactoUtil.getEmpresaDTOTemplate();
//			empresaDto.setNumeroRuc(vistaPedidoDTO.getRucEmpresa());
//			empresaDto.setValorTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
//			empresaDto.setEstadoEmpresa(CorporativoConstantes.ESTADO_ACTIVO);
//			EmpresaDTO empresaEncontrada =  SISPEFactory.getDataService().findUnique(empresaDto);
//			
//			//Indicamos a la variable de formulario que la empresa fue encontrada
//			request.getSession().setAttribute(ContactoUtil.LOCALIZACION, empresaEncontrada);
			request.getSession().setAttribute(ContactoUtil.LOC_GUARDADA, vistaPedidoDTO.getCodigoLocalizacion());
//			request.getSession().removeAttribute(ContactoUtil.PERSONA);
//						
//			//consultar el contacto principal de la empresa
//			DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
//			contactoPersonaLocalizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
//			contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
//			contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
//			contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
////			contactoPersonaLocalizacionDTO.setPersonaContactoDTO(new PersonaDTO());
//
//			ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
//			contactoRelacionadoDTO.setCodigoLocalizacion(Long.valueOf(vistaPedidoDTO.getCodigoLocalizacion()));
//			contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
//			contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//			
//			Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactosRelacionadosCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);					
//			DatoContactoPersonaLocalizacionDTO contactoTipoLocalizacion=new DatoContactoPersonaLocalizacionDTO();
//			if(CollectionUtils.isNotEmpty(contactosRelacionadosCol)){
//				contactoRelacionadoDTO =  (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactosRelacionadosCol, 0);
//				contactoTipoLocalizacion = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
//			}					
//			
//			if(contactoTipoLocalizacion!=null){
//				if(contactoTipoLocalizacion.getPersonaContactoDTO()!=null){
//					//cargar la informacion del contacto
//					formulario.setNombreContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getNombreCompleto());
//					formulario.setTipoDocumentoContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getTipoDocumento());
//					formulario.setNumeroDocumentoContacto(contactoTipoLocalizacion.getPersonaContactoDTO().getNumeroDocumento());
//					if(contactoTipoLocalizacion.getEmailPrincipal()!=null){
//						formulario.setEmailContacto(contactoTipoLocalizacion.getEmailPrincipal());
//					} else if(contactoTipoLocalizacion.getEmailTrabajo()!=null){
//						formulario.setEmailContacto(contactoTipoLocalizacion.getEmailTrabajo());
//					}
//					if(formulario.getEmailContacto()!= null){
//						formulario.setEmailEnviarCotizacion(formulario.getEmailContacto());
//					}
//					
//					String telefonoContacto="TD: ";
//					if(contactoTipoLocalizacion.getNumeroTelefonicoPrincipal()!=null){
//						telefonoContacto+=contactoTipoLocalizacion.getNumeroTelefonicoPrincipal();										
//					} else telefonoContacto+=" SN ";	
//					if(contactoTipoLocalizacion.getNumeroTelefonicoTrabajo()!=null){
//						telefonoContacto+=" - TT: "+contactoTipoLocalizacion.getNumeroTelefonicoTrabajo();
//					}else telefonoContacto+=" - TT: SN ";
//					if(contactoTipoLocalizacion.getNumeroTelefonicoCelular()!=null){
//						telefonoContacto+=" - TC: "+contactoTipoLocalizacion.getNumeroTelefonicoCelular();
//					}
//					else telefonoContacto+=" - TC: SN ";									
//					formulario.setTelefonoContacto(telefonoContacto);
//					
////					formulario.setNombreContacto(formulario.getTipoDocumentoContacto()+": "+formulario.getNumeroDocumentoContacto()
////							+" - NC: "+formulario.getNombreContacto()+" - "+formulario.getTelefonoContacto());
//				}
//			}
		
		//para el caso de personas	
		}
		else{
//			formulario.setOpTipoDocumento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opTipoDocumento.personal"));
//			formulario.setTelefonoPersona(vistaPedidoDTO.getTelefonoPersona());
			if(vistaPedidoDTO.getContactoEmpresa().startsWith(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC)){
				formulario.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
				session.setAttribute(ContactoUtil.RUC_PERSONA, vistaPedidoDTO.getNumeroDocumentoPersona());
				formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
			}else{
				formulario.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
			}
//			formulario.setNombrePersona(vistaPedidoDTO.getNombrePersona());
//			formulario.setTelefonoPersona(vistaPedidoDTO.getTelefonoPersona());
//			formulario.setTipoDocumentoPersona(vistaPedidoDTO.getTipoDocumentoCliente());
//			formulario.setNumeroDocumentoPersona(vistaPedidoDTO.getNumeroDocumentoPersona());
//			formulario.setEmailPersona(vistaPedidoDTO.getEmailPersona());
//			formulario.setEmailEnviarCotizacion(vistaPedidoDTO.getEmailPersona());
//			formulario.setRucEmpresa(null);
//			formulario.setNombreEmpresa(null);
//			formulario.setTipoDocumentoContacto(null);
//			formulario.setNumeroDocumentoContacto(null);
//			formulario.setNombreContacto("SIN CONTACTO");
//			formulario.setTelefonoContacto("SN");
//			
//			new ContactoUtil();
//			PersonaDTO personaDto = ContactoUtil.getPersonaDTOTemplate();
//			personaDto.setNumeroDocumento(vistaPedidoDTO.getNumeroDocumentoPersona());
//			personaDto.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoPersona());
//			personaDto.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
//			PersonaDTO personaEncontrada=  SISPEFactory.getDataService().findUnique(personaDto);
//			
//			//Indicamos a la variable de formulario que la persona fue encontrada
//			request.getSession().setAttribute(ContactoUtil.PERSONA, personaEncontrada);
//			request.getSession().removeAttribute(ContactoUtil.LOCALIZACION);
//			request.getSession().removeAttribute(ContactoUtil.LOC_GUARDADA);
//			
//			//consultar el contacto principal de la persona
//			DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
//			contactoPersonaLocalizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//			contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
//			contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
//			contactoPersonaLocalizacionDTO.setPersonaContactoDTO(new PersonaDTO());
//			contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//			contactoPersonaLocalizacionDTO.setCriteriaSearch(new CriteriaSearch());
//			contactoPersonaLocalizacionDTO.getCriteriaSearch().addCriteriaSearchParameter(new CriteriaSearchParameter<Timestamp>("fechaFin", ComparatorTypeEnum.IS_NULL));
//			
//			ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
//			contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
//			contactoRelacionadoDTO.setCodigoPersona(Long.valueOf(Long.valueOf(vistaPedidoDTO.getCodigoPersona())));
//			contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
//			
//			Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactoPersonaLocalizacionCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);
//			
//			DatoContactoPersonaLocalizacionDTO contactoTipoPersona=null;
//			if(CollectionUtils.isNotEmpty(contactoPersonaLocalizacionCol)){
//				contactoRelacionadoDTO = (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactoPersonaLocalizacionCol, 0);
//				contactoTipoPersona = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
//			}
//			
//			if(contactoTipoPersona!=null){
//				if(contactoTipoPersona.getPersonaContactoDTO()!=null){
//					//cargar la informacion del contacto
//					formulario.setNombreContacto(contactoTipoPersona.getPersonaContactoDTO().getNombreCompleto());
//					formulario.setTipoDocumentoContacto(contactoTipoPersona.getPersonaContactoDTO().getTipoDocumento());
//					formulario.setNumeroDocumentoContacto(contactoTipoPersona.getPersonaContactoDTO().getNumeroDocumento());
//					if(contactoTipoPersona.getEmailPrincipal()!=null){
//						formulario.setEmailContacto(contactoTipoPersona.getEmailPrincipal());
//					} else if(contactoTipoPersona.getEmailTrabajo()!=null){
//						formulario.setEmailContacto(contactoTipoPersona.getEmailTrabajo());
//					}	
//					if(formulario.getEmailContacto()!=null){
//						formulario.setEmailEnviarCotizacion(formulario.getEmailContacto());
//					}
//					
//					String telefonoContacto="TD: ";
//					if(contactoTipoPersona.getNumeroTelefonicoPrincipal()!=null){
//						telefonoContacto+=contactoTipoPersona.getNumeroTelefonicoPrincipal();										
//					} else telefonoContacto+=" SN ";	
//					if(contactoTipoPersona.getNumeroTelefonicoTrabajo()!=null){
//						telefonoContacto+=" - TT: "+contactoTipoPersona.getNumeroTelefonicoTrabajo();
//					}else telefonoContacto+=" - TT: SN ";
//					if(contactoTipoPersona.getNumeroTelefonicoCelular()!=null){
//						telefonoContacto+=" - TC: "+contactoTipoPersona.getNumeroTelefonicoCelular();
//					}
//					else telefonoContacto+=" - TC: SN ";									
//					formulario.setTelefonoContacto(telefonoContacto);		
////					formulario.setNombreContacto(formulario.getTipoDocumentoContacto()+": "+formulario.getNumeroDocumentoContacto()
////							+" - NC: "+formulario.getNombreContacto()+" - "+formulario.getTelefonoContacto());
//				}
//			}		
		}
		
		formulario.setTipoDocumento(vistaPedidoDTO.getTipoDocumentoCliente());
		if(vistaPedidoDTO.getNumBonNavEmp()!=null){
			formulario.setNumBonNavEmp(vistaPedidoDTO.getNumBonNavEmp().toString());
		}

//		//se asigna el tipo de documento del cliente al formulario
//		//Permite colocar en sessi\u00F3n las variables de PERSONA o LOCALIZACION
//		if(vistaPedidoDTO.getNpPersona() !=null){
//			request.getSession().setAttribute(ContactoUtil.PERSONA, vistaPedidoDTO.getNpPersona());			
//			formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA);
//			formulario.setNumeroDocumento(vistaPedidoDTO.getNpPersona().getNumeroDocumento());
//		}else if(vistaPedidoDTO.getNpEmpresa() !=null){
//			request.getSession().setAttribute(ContactoUtil.LOCALIZACION, vistaPedidoDTO.getNpEmpresa());
//			formulario.setTipoDocumento(CorporativoConstantes.TIPO_DOCUMENTO_EMPRESA_RUC);
//			formulario.setNumeroDocumento(vistaPedidoDTO.getNpEmpresa().getNumeroRuc());
//		} 

		formulario.setOpTipoBusqueda(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion"));
		formulario.setOpAutorizacion(estadoInactivo);
		formulario.setOpTipoPedido(vistaPedidoDTO.getCodigoTipoPedido());
		
		//se asigna el local responsable, adicionalmente se agrega 
		formulario.setLocalResponsable(vistaPedidoDTO.getId().getCodigoAreaTrabajo()
				+ " " + caracterToken + " " + vistaPedidoDTO.getNombreLocal());
		LogSISPE.getLog().info("LOCAL RESPONSABLE OBTENIDO: {}",formulario.getLocalResponsable());
		
		formulario.setLocalDespacho(vistaPedidoDTO.getCodigoLocalEntrega().toString());

		session.setAttribute(CrearPedidoAction.DETALLE_PEDIDO,detallePedido);
		session.setAttribute(CrearPedidoAction.COL_CODIGOS_ARTICULOS,codigosArticulos);
		//se desactiva el men\u00FA de opciones
		MenuUtils.desactivarMenu(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_MENU"), session);
	}

  
  /**
   * Verifica si el rol del usuario junto con el tipo de pedido se encuentra configurado, 
   * en la tabla de par\u00E1metros generales para que pueda acceder a los pedidos
   * 
   * @param vistaEntidadResponsableDTO	- Objeto que contiene los datos del usuario
   * @param request											- La petici\u00F3n HTTP
   */
  public static String verificarUsuarioPedidoEspecial(VistaEntidadResponsableDTO vistaEntidadResponsableDTO,
  		HttpServletRequest request)throws Exception{
  	
  	String token = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
  	
  	//se obtiene el valor del par\u00E1metro en donde buscar\u00E1 el rol
  	ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.secuenciaRolUsuarioEspecialTipoPedido", request);

  	if(parametroDTO.getValorParametro()!=null){
  		
  		//se separan las secuencias y se borran los espacios en blanco
  		String [] secuenciaRolTipPed = parametroDTO.getValorParametro().replaceAll(" ", "").split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
  		String codigoTipoPedido = "";
  		String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"); 
  		
  		//se iteran los roles del usuario
  		for(String rolUsuario : vistaEntidadResponsableDTO.getRolesUsuario()){
  			for(int i=0;i<secuenciaRolTipPed.length;i++){
  				//se verifica si existe el rol en cada secuencia que es de la forma ROL-TIPOPEDIDO
  				String rolId = secuenciaRolTipPed[i].split(token)[0];
  				if(rolId.equals(rolUsuario)){
  					String codigoTipoPedidoActual = secuenciaRolTipPed[i].split(token)[1];
  					codigoTipoPedido = codigoTipoPedido.concat(codigoTipoPedidoActual).concat(caraterSeparacion);
  				}
  			}
  		}
			if(!codigoTipoPedido.equals("")){
				//se elimina el \u00FAltimo separador
				codigoTipoPedido = codigoTipoPedido.substring(0, codigoTipoPedido.length() - 1);
				request.getSession().setAttribute(COD_TIPO_PEDIDO_ESP_USER, codigoTipoPedido);
				return codigoTipoPedido;
			}
  	}
  	return null;
  }
  
  /**
   * Obtiene la colecci\u00F3n de usuarios que pertenecen al rol configurado en la tabla de par\u00E1metros, en base al c\u00F3digo
   * del tipo de pedido
   * 
   * @param codigoTipoPedido	- El c\u00F3digo del tipo de pedido
   * @param request						- La petici\u00F3n HTTP
   */
  public static Collection<UserDto> obtenerUsuariosTipoPedido(String codigoTipoPedido, HttpServletRequest request)throws Exception{
  	
  	String token = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");
  	
  	//se obtiene el valor del par\u00E1metro en donde buscar\u00E1 el rol
  	ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.secuenciaRolUsuarioEspecialTipoPedido", request);

  	String rolEncontrado = null;
  	if(parametroDTO.getValorParametro()!=null){
  		//se separan las secuencias y se borran los espacios en blanco
  		String [] secuenciaRolTipPed = parametroDTO.getValorParametro().replaceAll(" ", "").split(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion"));
  		for(int i=0;i<secuenciaRolTipPed.length;i++){
  			//se verifica si existe el tipo de pedido, cada secuencia es de la forma ROL-TIPO_PEDIDO
  			String tipoPedidoActual = secuenciaRolTipPed[i].split(token)[1];
  			if(tipoPedidoActual.equals(codigoTipoPedido)){
  				rolEncontrado = secuenciaRolTipPed[i].split(token)[0];
					LogSISPE.getLog().info("rol encontrado: {}",rolEncontrado);
  				break;
  			}
  		}
  	}
  	
  	Collection<UserDto> usuarios = null;
  	//si el rol fue encontrado se obtienen los usuarios
  	if(rolEncontrado != null){
  		usuarios = FrameworkFactory.getSecurityService().getUsersByRole(rolEncontrado);
  	}
  	
  	return usuarios;
  } 
  
	/**
	 * Obtencion de los codigos de clasificaci\u00F3n para b\u00FAsqueda de pedidos especiales
	 * 
	 * @param request					Petici\u00F3n que se est\u00E1 procesando
	 * @param especialDTOcol	lista de especiales
	 * @param indiceEsp				indice de la colecci\u00F3n de especiales de la que se quiere tomar
	 * 												la clasificaci\u00F3n
	 * @throws Exception
	 */
	public static void obtenerClasificacionEspecial(HttpServletRequest request, 
			List<EspecialDTO> especialDTOcol, int indiceEsp) throws Exception{

		HttpSession session=request.getSession();
		//tomo de sesion la colecci\u00F3n de pedidos especiales
		Collection<EspecialClasificacionDTO> espClasificaciones = especialDTOcol.get(indiceEsp).getEspecialesClasificaciones();
		StringBuilder codigosClasificaciones = new StringBuilder("");
		int cont = 1;
		for(EspecialClasificacionDTO especialClasificacionDTO : espClasificaciones){
			if(cont < espClasificaciones.size()){
				codigosClasificaciones.append(especialClasificacionDTO.getId().getCodigoClasificacion()).append(",");
			}else{
				codigosClasificaciones.append(especialClasificacionDTO.getId().getCodigoClasificacion());
			}
			cont++;
		}
		LogSISPE.getLog().info("clasificaciones resultantes: {}",codigosClasificaciones);
		session.setAttribute(CrearPedidoAction.LISTA_CLASIFICACIONES, codigosClasificaciones.toString());
		session.setAttribute(CrearPedidoAction.INDICE_ESPECIAL, indiceEsp);
		LogSISPE.getLog().info("indideEsp: {}",indiceEsp);
	}
	
	/**
	 *
	 * @param fechaCalendarioInicial
	 * @param fechaCalendarioFinal
	 * @return
	 * @throws Exception
	 */
	public static int calcularDiasEntreFechas(GregorianCalendar fechaCalendarioInicial, GregorianCalendar fechaCalendarioFinal) throws Exception{
		int diasEntreFechas = 0;
		int mesFechaInicial = 0;
		int mesFechaFinal = 0;
		GregorianCalendar fechaCalendarioInicialTemp = null;

		diasEntreFechas = fechaCalendarioFinal.get(Calendar.DAY_OF_MONTH) - fechaCalendarioInicial.get(Calendar.DAY_OF_MONTH) - 1;

		if (diasEntreFechas < 0
				|| fechaCalendarioFinal.get(Calendar.MONTH) > fechaCalendarioInicial.get(Calendar.MONTH)
				|| fechaCalendarioFinal.get(Calendar.YEAR) > fechaCalendarioInicial.get(Calendar.YEAR)) {
			mesFechaInicial = fechaCalendarioInicial.get(Calendar.MONTH);
			mesFechaFinal = (fechaCalendarioFinal.get(Calendar.YEAR) - fechaCalendarioInicial.get(Calendar.YEAR)) * CANTIDAD_MESES_ANIO + fechaCalendarioFinal.get(Calendar.MONTH);
			fechaCalendarioInicialTemp = new GregorianCalendar();
			fechaCalendarioInicialTemp.setTime(fechaCalendarioInicial.getTime());
			for (int meses = mesFechaInicial; meses < mesFechaFinal; meses++) {
				diasEntreFechas += fechaCalendarioInicialTemp.getActualMaximum(Calendar.DAY_OF_MONTH);
				fechaCalendarioInicialTemp.add(Calendar.MONTH, 1);
			}
		}
		return diasEntreFechas;
	}
	
	/**
	 * Calcula el precio de no afiliado y lo asigna en los campos correspondientes del estado
	 * @param request
	 * @param estadoDetallePedidoDTO
	 */
	public static void calcularPreciosNoAfiliados(HttpServletRequest request, EstadoDetallePedidoDTO estadoDetallePedidoDTO,ArticuloDTO articuloDTO)throws Exception{
		//Double valorPorcentaje = obtenerPorcentajeCalculoPreciosAfiliado(request);
		
		//se asignan los precios de no afiliado
//		estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorUnitarioEstado(), valorPorcentaje));
//		estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorUnitarioIVAEstado(), valorPorcentaje));
//		estadoDetallePedidoDTO.setValorCajaNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorCajaEstado(), valorPorcentaje));
//		estadoDetallePedidoDTO.setValorCajaIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorCajaIVAEstado(), valorPorcentaje));
//		estadoDetallePedidoDTO.setValorMayoristaNoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorMayorista(), valorPorcentaje));
//		estadoDetallePedidoDTO.setValorMayoristaIVANoAfiliado(UtilesSISPE.aumentarPorcentajeAPrecio(estadoDetallePedidoDTO.getValorMayoristaIVA(), valorPorcentaje));
		
		//se asignan los precios de no afiliado
		estadoDetallePedidoDTO.setValorUnitarioNoAfiliado(articuloDTO.getPrecioBaseNoAfi());
		estadoDetallePedidoDTO.setValorUnitarioIVANoAfiliado(articuloDTO.getPrecioBaseNoAfiImp());
		if(articuloDTO.getHabilitadoPrecioCaja()){
			estadoDetallePedidoDTO.setValorCajaNoAfiliado(articuloDTO.getPrecioCajaNoAfi());
			estadoDetallePedidoDTO.setValorCajaIVANoAfiliado(articuloDTO.getPrecioCajaNoAfiImp());
		}
		else{
			estadoDetallePedidoDTO.setValorCajaNoAfiliado(0D);
			estadoDetallePedidoDTO.setValorCajaIVANoAfiliado(0D);
		}
		if(articuloDTO.getHabilitadoPrecioMayoreo()){
			estadoDetallePedidoDTO.setValorMayoristaNoAfiliado(articuloDTO.getPrecioMayoristaNoAfi());
			estadoDetallePedidoDTO.setValorMayoristaIVANoAfiliado(articuloDTO.getPrecioMayoristaNoAfiImp());
		}else {
			estadoDetallePedidoDTO.setValorMayoristaNoAfiliado(0D);
			estadoDetallePedidoDTO.setValorMayoristaIVANoAfiliado(0D);
		}		
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Double obtenerPorcentajeCalculoPreciosAfiliado(HttpServletRequest request)throws Exception {
		
		//se obtiene el par\u00E1metro para el c\u00E1lculo
		Double valorPorcentaje = (Double)request.getSession().getAttribute(PORCENTAJE_CALCULO_PRECIOS_AFILIADO);
		if(valorPorcentaje == null){
			ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentajeCalculoPreciosAfiliado", request);
			if(parametroDTO.getValorParametro() != null){
				double porcentaje = Double.parseDouble(parametroDTO.getValorParametro());
				valorPorcentaje = Double.valueOf(porcentaje/100);
				request.getSession().setAttribute(PORCENTAJE_CALCULO_PRECIOS_AFILIADO, valorPorcentaje);
			}
		}
		
		return valorPorcentaje;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Double obtenerPorcentajeDiferenciaPrecioNormalYMayorista(HttpServletRequest request)throws Exception {
		
		//se obtiene el par\u00E1metro para el c\u00E1lculo
		Double valorPorcentaje = (Double)request.getSession().getAttribute(PORCENTAJE_DIFERENCIA_PRECIOS_NORMAL_MAYORISTA);
		if(valorPorcentaje == null){
			ParametroDTO parametroDTO = obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.porcentajeDiferenciaPrecioNormalMayorista", request);
			if(parametroDTO.getValorParametro() != null){
				double porcentaje = Double.parseDouble(parametroDTO.getValorParametro());
				valorPorcentaje = Double.valueOf(porcentaje/100);
				request.getSession().setAttribute(PORCENTAJE_DIFERENCIA_PRECIOS_NORMAL_MAYORISTA, valorPorcentaje);
			}
		}
		
		return valorPorcentaje;
	}
	
	/**
	 * Genera la colecci\u00F3n de entidades responsables sin usuarios duplicados, por razones del rol
	 * @param 	colvistaEntidadResponsableDTO		- Colecci\u00F3n de entidades original
	 * @return
	 */
	public static Collection<VistaEntidadResponsableDTO> agruparEntidadesResponsablesPorUsuario(Collection<VistaEntidadResponsableDTO> colvistaEntidadResponsableDTO){
		
		Collection<VistaEntidadResponsableDTO> colvistaEntidadResponsableDTOFinal = null;
		if(colvistaEntidadResponsableDTO!= null && !colvistaEntidadResponsableDTO.isEmpty()){
			String userIdReferencia = null;
			colvistaEntidadResponsableDTOFinal = new ArrayList<VistaEntidadResponsableDTO>();
			
			//se eliminan los posibles duplicados en los usuarios por motivos del rol
			for(VistaEntidadResponsableDTO vistaEntidadResponsableDTO : colvistaEntidadResponsableDTO){
				if(!vistaEntidadResponsableDTO.getId().getIdUsuario().equals(userIdReferencia)){
					colvistaEntidadResponsableDTOFinal.add(vistaEntidadResponsableDTO);
					userIdReferencia = vistaEntidadResponsableDTO.getId().getIdUsuario();
				}
			}
		}
		return colvistaEntidadResponsableDTOFinal;
	}
	
	/**
	 * 
	 * @param resultados					- Colecci\u00F3n que va a ser paginada
	 * @param coleccionPaginada 	- Nombre de la variable de sesi\u00F3n para la colecci\u00F3n resultado de la paginaci\u00F3n
	 * @param request
	 * @param sufijoVarSesion			- Esta variable se usa para introducir sufijos a las variables de sesi\u00F3n que se usan en la paginaci\u00F3n, esto puede ser \u00FAtil cuando
	 * 															se tenga m\u00E1s de una tabla paginada en una pantalla, estos sufijos pueden ser n\u00FAmeros, si se desea que el m\u00E9todo funcione sin sufijos
	 * 															se puede enviar como par\u00E1metro la cadena vacia ""
	 * @param rangoPaginacion
	 * @param inicioPaginacion	 	- Valor para indicar un inicio de paginaci\u00F3n espec\u00EDfico
	 */
	public Collection paginarResultados(Collection resultados, HttpServletRequest request, String sufijoVarSesion, String rangoPaginacion, String inicioPaginacion){

		HttpSession session = request.getSession();
		
		int size = resultados.size();
		int range = Integer.parseInt(rangoPaginacion);
		int start = 0;
		
		if(inicioPaginacion != null){
			start = Integer.parseInt(inicioPaginacion);
		}else if(request.getParameter("start".concat(sufijoVarSesion)) != null){
			start = Integer.parseInt(request.getParameter("start".concat(sufijoVarSesion)));
		}

		session.setAttribute(START_PAG.concat(sufijoVarSesion), String.valueOf(start));
		session.setAttribute(RANGE_PAG.concat(sufijoVarSesion), String.valueOf(range));
		session.setAttribute(SIZE_PAG.concat(sufijoVarSesion), String.valueOf(size));	

		Collection paginatedResults = null;

		if(resultados.size() > 0){
			paginatedResults = ec.com.smx.framework.common.util.Util.obtenerSubCollection(resultados, start, start + range > size ? size : start+range);
			LogSISPE.getLog().info("La pagina resultante tiene {}",paginatedResults.size());
		}else
			LogSISPE.getLog().info("No se encontraron elementos para paginar");	

		return paginatedResults;
	}
	
	/**
	 * IMPORTANTE:
	 * M\u00E9todo que permite construir una plantilla de b\u00FAsqueda para reportes de entregas por articulo/pedido
	 * 
	 * @param request, form
	 * @return VistaReporteGeneralDTO
	 * 
	 */
	public static VistaReporteGeneralDTO construirConsultaReporteGeneral(HttpServletRequest request, ActionForm form, VistaReporteGeneralDTO vistaReporteGeneralBusqueda) throws Exception{
		LogSISPE.getLog().info("Se procede a ingresar a construirConsultaReporteGeneral...");
				
		// Variables para fecha de entrega
		Timestamp fechaInicial = null;
		Timestamp fechaFinal = null;
		
		//OANDINO: Se obtiene el tipo de proceso ----------------------------------------------------
		String accion = (String)request.getSession().getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		//-------------------------------------------------------------------------------------------
		
		// Instancia de formulario
		ListadoPedidosForm formulario = (ListadoPedidosForm)form;
		
		// Se setean valores comunes
		vistaReporteGeneralBusqueda.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		
		// Se setean valores de acuerdo a datos ingresados desde el formulario
		if(formulario.getNumeroPedidoTxt()!= null && !formulario.getNumeroPedidoTxt().trim().equals("")){				
			vistaReporteGeneralBusqueda.getId().setCodigoPedido(formulario.getNumeroPedidoTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero pedido: {}",vistaReporteGeneralBusqueda.getId().getCodigoPedido());				
		}
		
		if(formulario.getNumeroReservaTxt()!= null && !formulario.getNumeroReservaTxt().trim().equals("")){
			vistaReporteGeneralBusqueda.setLlaveContratoPOS(formulario.getNumeroReservaTxt().trim());
			LogSISPE.getLog().info("N\u00FAmero reserva: {}",vistaReporteGeneralBusqueda.getLlaveContratoPOS());				
		}
		
		if(formulario.getCodigoClasificacionTxt()!= null && !formulario.getCodigoClasificacionTxt().trim().equals("")){
			vistaReporteGeneralBusqueda.setCodigoClasificacion(formulario.getCodigoClasificacionTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo clasificaci\u00F3n: {}",vistaReporteGeneralBusqueda.getCodigoClasificacion());
		}
		
		if(formulario.getCodigoArticuloTxt()!= null && !formulario.getCodigoArticuloTxt().trim().equals("")){				
//			vistaReporteGeneralBusqueda.getId().setCodigoArticulo(formulario.getCodigoArticuloTxt().trim());
//			LogSISPE.getLog().info("C\u00F3digo art\u00EDculo: {}",vistaReporteGeneralBusqueda.getId().getCodigoArticulo());
			vistaReporteGeneralBusqueda.setCodigoBarras(formulario.getCodigoArticuloTxt().trim());
			LogSISPE.getLog().info("C\u00F3digo barras art\u00EDculo: {}",vistaReporteGeneralBusqueda.getCodigoBarras());
		}
		
		if(formulario.getDescripcionArticuloTxt()!= null && !formulario.getDescripcionArticuloTxt().trim().equals("")){				
			vistaReporteGeneralBusqueda.setDescripcionArticulo(formulario.getDescripcionArticuloTxt().trim());
			LogSISPE.getLog().info("Descripci\u00F3n art\u00EDculo: {}",vistaReporteGeneralBusqueda.getDescripcionArticulo());
		}
		
		if(formulario.getDocumentoPersonalTxt()!= null && !formulario.getDocumentoPersonalTxt().trim().equals("")){				
			vistaReporteGeneralBusqueda.setCedulaContacto(formulario.getDocumentoPersonalTxt().trim());
			LogSISPE.getLog().info("Documento personal: {}",vistaReporteGeneralBusqueda.getCedulaContacto());
		}
		
		if(formulario.getNombreContactoTxt()!= null && !formulario.getNombreContactoTxt().trim().equals("")){
			vistaReporteGeneralBusqueda.setNombreContacto(formulario.getNombreContactoTxt().trim());
			LogSISPE.getLog().info("Nombre contacto: {}",vistaReporteGeneralBusqueda.getNombreContacto());
		}
		
		if(formulario.getRucEmpresaTxt()!= null && !formulario.getRucEmpresaTxt().trim().equals("")){
			vistaReporteGeneralBusqueda.setRucEmpresa(formulario.getRucEmpresaTxt().trim());
			LogSISPE.getLog().info("Ruc: {}",vistaReporteGeneralBusqueda.getRucEmpresa());
		}
		
		if(formulario.getNombreEmpresaTxt()!= null && !formulario.getNombreEmpresaTxt().trim().equals("")){
			vistaReporteGeneralBusqueda.setNombreEmpresa(formulario.getNombreEmpresaTxt().trim());
			LogSISPE.getLog().info("Nombre empresa: {}",vistaReporteGeneralBusqueda.getNombreEmpresa());
		}
		
		//Se verifica los rangos de fechas a ser tomados en cuenta			
		LogSISPE.getLog().info("Opci\u00F3n fecha: {}",formulario.getOpcionFecha());
		
		if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Rango'");
			try{
				if(formulario.getFechaInicial()!=null && !formulario.getFechaInicial().equals("")){
					//se asigna la fecha inicial
					fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaInicial(), 0, 0, 0, 0, 0, 0));						
				}
				if(formulario.getFechaFinal()!=null && !formulario.getFechaFinal().equals("")){
					//se asigna la fecha final
					fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(formulario.getFechaFinal(), 0, 0, 23, 59, 59, 999));						
				}
			}catch(Exception e){
				//en caso de que existan problemas en la conversi\u00F3n de string a timeestamp
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			
			//si ambas fechas permanecen nulas se busca por todo
			if(fechaInicial == null && fechaFinal == null){
				formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			}
		}
		else if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Hoy'");
			//se llenan las fechas
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 0, 0, 0, 0));
			fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), 0, 0, 23, 59, 59, 999));									
		}
		//cuando se busca por la opci\u00F3n todos
		else if(formulario.getOpcionFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"))){
			LogSISPE.getLog().info("Se ha elegido la opci\u00F3n 'Desde'");
			LogSISPE.getLog().info("N\u00FAmero de meses: {}",formulario.getNumeroMeses());
			
			int meses = 4;
			try{
				//se convierte a entero el n\u00FAmero de meses
				meses = Integer.parseInt(formulario.getNumeroMeses());
			}catch(NumberFormatException e) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			fechaInicial = new Timestamp(WebSISPEUtil.construirFechaCompleta(new Date(), (-1)*meses, 0, 0, 0, 0, 0));
		}
		
		LogSISPE.getLog().info("Fecha inicial: {}",fechaInicial);
		LogSISPE.getLog().info("Fecha final: {}",fechaFinal);
				
		//OANDINO: Se verifica el tipo de fecha seg\u00FAn la acci\u00F3n ------------------------------------------------------------------------
		if(accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.reporteArticulosReservados"))){
			LogSISPE.getLog().info("Se ha ingresado en base a la acci\u00F3n del reporte de art\u00EDculos reservados");
			if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaDespacho"))){
				LogSISPE.getLog().info("Se ha elegido fecha despacho");
				vistaReporteGeneralBusqueda.setFechaDespachoDesde(fechaInicial);
				vistaReporteGeneralBusqueda.setFechaDespachoHasta(fechaFinal);
			}
			else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEntrega"))){
				LogSISPE.getLog().info("Se ha elegido fecha entrega");
				vistaReporteGeneralBusqueda.setFechaEntregaDesde(fechaInicial);
				vistaReporteGeneralBusqueda.setFechaEntregaHasta(fechaFinal);
			}
			else if(formulario.getComboTipoFecha().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEstadoPedido"))){
				LogSISPE.getLog().info("Se ha elegido fecha estado");
				vistaReporteGeneralBusqueda.setFechaInicialEstadoDesde(fechaInicial);
				vistaReporteGeneralBusqueda.setFechaInicialEstadoHasta(fechaFinal);
			}				
		}
		//------------------------------------------------------------------------------------------------------------------------------
		else{
			LogSISPE.getLog().info("Se ha ingresado en base a la acci\u00F3n por defecto");
			vistaReporteGeneralBusqueda.setFechaEntregaDesde(fechaInicial);
			vistaReporteGeneralBusqueda.setFechaEntregaHasta(fechaFinal);
		}
		
		// Se devuelve plantilla de b\u00FAsqueda
		return vistaReporteGeneralBusqueda;
	}
	
	/**
	 * Este m\u00E9todo me devuelve una coleccion de nombres de parametros que fueron enviados en el request y que empiezan con un prefijo
	 * @param request
	 * @param prefijo
	 * @return
	 */
	public static Collection<String> obtenerParametrosRequestPrefijo(HttpServletRequest request, String prefijo){
		Enumeration<String> parametrosRequest = request.getParameterNames();
		Collection<String> camposSeleccionadas = new ArrayList<String>();
		while(parametrosRequest.hasMoreElements()){
			String parametroActual = parametrosRequest.nextElement();
			if(parametroActual.startsWith(prefijo)){
				//se inserta siempre en la posici\u00F3n cero porque en la lista de par\u00E1metros vienen primero los \u00FAltimos
				camposSeleccionadas.add(parametroActual);
			}
		}
		
		return camposSeleccionadas;
	}
	
	/**
	 * @param request
	 * @param idEvento
	 * @return
	 */
	public static MailMensajeEST construirEstructuraMail(HttpServletRequest request, String idEvento)throws Exception{
		
		EventoDTO eventoDTO = null;
		String mailUsuario = SessionManagerSISPE.getCurrentEntidadResponsable(request).getEmailUser();
		if(idEvento != null){
			EventoID eventoID=new EventoID();
			eventoID.setCodigoEvento(idEvento);
			eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
			eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);
		}
		//se construye la estructura para el enviar el email
		MailMensajeEST mailMensajeEST = new MailMensajeEST();
		//mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuenta.sispe"));
		  mailMensajeEST.setDe(mailUsuario);
		  mailMensajeEST.setReemplazarRemitente(Boolean.FALSE);
		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoEstablecimiento().equals(CorporativoConstantes.CODIGO_ESTABLECIMIENTO_JUGUETON)){
			mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuentaJugueton.sispe"));
		}
		
		//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
		mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
		mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
		mailMensajeEST.setEventoDTO(eventoDTO);
		
		return mailMensajeEST;
	}
	
	public static boolean verificarUsuarioClasificacion(HttpServletRequest request, boolean verificaCanastas)throws Exception{
		String codigosClasificaciones = null;
		
		//obtengo el usuario logeado
		VistaEntidadResponsableDTO usuarioLogeado = SessionManagerSISPE.getCurrentEntidadResponsable(request);
		String idUsuario = usuarioLogeado.getId().getIdUsuario();
		
		//se realiza la consulta para obtener las clasificaciones a las que tienen acceso los usuarios
		ClasificacionUsuarioDTO consultaClasificacionUsuarioDTO = new ClasificacionUsuarioDTO(Boolean.TRUE);
		consultaClasificacionUsuarioDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		consultaClasificacionUsuarioDTO.getId().setUserId(idUsuario);
		if(verificaCanastas){
			//se obtiene los codigo de clasificaciones (1606/1602)para validar los usuarios correspondientes. 
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionValidaResponsablePedido", request);
			codigosClasificaciones = parametroDTO.getValorParametro();
			consultaClasificacionUsuarioDTO.getId().setCodigoClasificacion(codigosClasificaciones);
		}
		consultaClasificacionUsuarioDTO.setEstadoClasificacionUsuario(SessionManagerSISPE.getEstadoActivo(request));
		
		Collection<ClasificacionUsuarioDTO> colClasificacionUsuarioDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacionesUsuario(consultaClasificacionUsuarioDTO);
		if(colClasificacionUsuarioDTO!=null && !colClasificacionUsuarioDTO.isEmpty()){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static void instanciarPopUpDiferidos(HttpServletRequest request, String accion)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Diferidos");
		//popUp.setEtiquetaBotonOK("Si");
		popUp.setTope(10d);
		popUp.setAncho(85D);
		popUp.setEtiquetaBotonCANCEL("Cancelar");
		//popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//popUp.setPreguntaVentana("Desea adjuntar un archivo del beneficiario al pedido?");
		if(accion.equals("estadoPedido")){			
			popUp.setValorCANCEL("requestAjax('detalleEstadoPedido.do', ['pregunta'], {parameters: 'cancelarDiferido=ok',popWait:false});ocultarModal();");
			popUp.setContenidoVentana("servicioCliente/estadoPedido/diferidoEstadoPedido.jsp");
		}else{
			popUp.setValorCANCEL("requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarDiferido=ok',popWait:false});ocultarModal();");
			popUp.setContenidoVentana("servicioCliente/estadoPedido/diferidoResumenPedido.jsp");
		}
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	public static void buscarDiferidos(HttpServletRequest request) throws Exception{
		//obtengo la collection de cuotas Activas
		CuotaDTO cuotaDTO = new CuotaDTO();
		cuotaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
		cuotaDTO.setEstado("ACT");
  		Collection<CuotaDTO> colCuotas = SessionManagerSISPE.getServicioClienteServicio().transObtenerCuotas(cuotaDTO);
  		request.getSession().setAttribute(EstadoPedidoUtil.DIFERIDO_CUOTAS, colCuotas);
  		
  		//se obtiene los codigo de clasificaciones de departamentos que aplican diferidos 
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentosDiferidos", request);
		String[] codigoDepartamentos = parametroDTO.getValorParametro().split(",");
		boolean flag = false;
		
		LogSISPE.getLog().info("---Impresion por el estado pedido");
		// --INICIO REPORTE DIFERIDOS--(VISTA_PEDIDO)
  		String valor;
  		String codigoClasificionPadre;
  		Collection<ExtructuraDiferidos> colArtDif = new ArrayList<ExtructuraDiferidos>();
  		ExtructuraDiferidos estructuraDTO = null;
  		
  		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
  		for(VistaDetallePedidoDTO vista : (Collection<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidosReporte()){
  			LogSISPE.getLog().info("1er For");
  			flag = false;
  			valor = vista.getCodigoClasificacion();
  			codigoClasificionPadre = valor.substring(0, 2);
  			//recorre todos los dtos que aplican diferidos
  			for(int i=0; i<codigoDepartamentos.length; i++){
  				if(codigoClasificionPadre.equals(codigoDepartamentos[i])
//  						 && vista.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item"))){
  					&& !vista.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
					   !vista.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
  					LogSISPE.getLog().info("Aplica diferido el articulo--{}",vista.getArticuloDTO().getDescripcionArticulo());
  					flag = true;
  					break;
  				}
  			}
  			//bandera que me indica que el articulo aplica diferidos
  			if(flag){
  				if(WebSISPEUtil.obtenerListaDiferidos(vista,request) != null && WebSISPEUtil.obtenerListaDiferidos(vista,request).size()>0){
  					estructuraDTO = new ExtructuraDiferidos();
	  				//Armo la Extructura diferidos
	  				estructuraDTO.setVistaDetallePedidoDTO(vista);
	  				estructuraDTO.setColDiferidos(WebSISPEUtil.obtenerListaDiferidos(vista,request));
	  				colArtDif.add(estructuraDTO);
  				}
  			}
  		}
  		vistaPedidoDTO.setVistaDetallesDiferidos(colArtDif);
  		request.getSession().setAttribute(EstadoPedidoUtil.VISTA_PEDIDO,vistaPedidoDTO);
  		// --FIN REPORTE DIFERIDOS--
	}
	public static void buscarDiferidos1(HttpServletRequest request) throws Exception{
		//obtengo la collection de cuotas Activas
		CuotaDTO cuotaDTO = new CuotaDTO();
		cuotaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
		cuotaDTO.setEstado("ACT");
  		Collection<CuotaDTO> colCuotas = SessionManagerSISPE.getServicioClienteServicio().transObtenerCuotas(cuotaDTO);
  		request.getSession().setAttribute(EstadoPedidoUtil.DIFERIDO_CUOTAS, colCuotas);
  		
  		//se obtiene los codigo de clasificaciones de departamentos que aplican diferidos 
		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoDepartamentosDiferidos", request);
		String[] codigoDepartamentos = parametroDTO.getValorParametro().split(",");
		boolean flag = false;
		LogSISPE.getLog().info("---Impresion por COTIZAICON - RECO - RES");
		// --INICIO REPORTE DIFERIDOS por un resumen de una COTIZACION - RECOTIZACION--
		String valor;
  		String codigoClasificionPadre;
  		Collection<ExtructuraDiferidos> colArtDif = new ArrayList<ExtructuraDiferidos>();
  		ExtructuraDiferidos estructuraDTO = null;
  		//se obtiene el detalle del Pedido
  		Collection <DetallePedidoDTO> colDetallePedidoDTO = (Collection<DetallePedidoDTO>)request.getSession().getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		
		for(DetallePedidoDTO detalle : colDetallePedidoDTO){
			
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getPrecioBaseImp()--{} ",  detalle.getArticuloDTO().getPrecioBaseImp());
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getEstaCargadaRelacionImpuestos()--{} ",  detalle.getArticuloDTO().getEstaCargadaRelacionImpuestos());
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getTieneArticuloPrecio()--{} ",  detalle.getArticuloDTO().getTieneArticuloPrecio());
			
			if(detalle.getArticuloDTO().getTieneArticuloPrecio()){
				for (ArticuloPrecioDTO articuloPrecioDTO : detalle.getArticuloDTO().getArticuloPrecioCol()){
					LogSISPE.getLog().info(" articuloPrecioDTO.getTieneArticulo()--{} ",  articuloPrecioDTO.getTieneArticulo());
					if(!articuloPrecioDTO.getTieneArticulo()){
						articuloPrecioDTO.setArticulo(detalle.getArticuloDTO());
					}
//					LogSISPE.getLog().info(" articuloPrecioDTO.getValorActualCalculado()--{} ",  articuloPrecioDTO.getValorActualCalculado());
				}
			}
			
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getPrecioBaseImp()--{} ",  detalle.getArticuloDTO().getPrecioBaseImp());
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getEstaCargadaRelacionImpuestos()--{} ",  detalle.getArticuloDTO().getEstaCargadaRelacionImpuestos());
//			LogSISPE.getLog().info(" detalle.getArticuloDTO().getTieneArticuloPrecio()--{} ",  detalle.getArticuloDTO().getTieneArticuloPrecio());
			
			flag = false;
			valor = detalle.getArticuloDTO().getCodigoClasificacion();
  			codigoClasificionPadre = valor.substring(0, 2);
  			LogSISPE.getLog().info("codigoClasificacionPadre--{}",codigoClasificionPadre);
  			
  			//recorre todos los dtos que aplican diferidos
  			for(int i=0; i<codigoDepartamentos.length; i++){
  				if(codigoClasificionPadre.equals(codigoDepartamentos[i])
//  						 && detalle.getArticuloDTO().getCodigoTipoArticulo().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.item"))){
  					&& !detalle.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) && 
					   !detalle.getArticuloDTO().getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){
  					LogSISPE.getLog().info("Aplica diferido el articulo--{}",detalle.getArticuloDTO().getDescripcionArticulo());
  					flag = true;
  					break;
  				}
  			}
  			//Filtro por departamento(0060)
  			if(flag){
  				if(WebSISPEUtil.obtenerListaDiferidos1(detalle,request) != null && WebSISPEUtil.obtenerListaDiferidos1(detalle,request).size()>0){
  					estructuraDTO = new ExtructuraDiferidos();
	  			    //Armo la Extructura diferidos
	  				estructuraDTO.setDetallePedidoDTO(detalle);
	  				estructuraDTO.setColDiferidos(WebSISPEUtil.obtenerListaDiferidos1(detalle,request));
	  				colArtDif.add(estructuraDTO);
  				}
  			}else{
					LogSISPE.getLog().info("No aplica diferido el articulo--{}",detalle.getArticuloDTO().getDescripcionArticulo());
			}
  		}
		request.getSession().setAttribute(REPORTE_DIFERIDOS, colArtDif);
		// --FIN REPORTE DIFERIDOS--
	}
	
	/**
	 * verifica si el pedido tiene problemas de stock en (pavos,pollos,canastas)
	 * @param request
	 * @param colDetallePedido
	 */
	public static boolean verificarProblemasStock(HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedido)throws Exception{
		boolean flag = false;
		for(DetallePedidoDTO detalle : colDetallePedido){
			if(detalle.isNpSinStockPavPolCan()){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @param request
	 * @param colDetallePedido
	 * @return true si los detalles tienen problemas de stock y false caso contrario
	 * @throws Exception
	 */
	public static Boolean tieneProblemasDeStockPorClasificacion(HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedido ) throws Exception{
		//se recupera de session el pedido
		LogSISPE.getLog().info("validando si los detalles del pedido tienen problemas de stock");
		PedidoDTO pedidoDTO = (PedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
		
		Boolean existeAutorizacionStockPendiente = (Boolean) request.getSession().getAttribute(AutorizacionesUtil.TIENE_AUTORIZACIONES_PENDIENTES_STOCK);
		for(DetallePedidoDTO detalle : colDetallePedido){
			LogSISPE.getLog().info("articulo {} - {}", detalle.getId().getCodigoArticulo(), detalle.getArticuloDTO().getDescripcionArticulo());
			Boolean verificarStock = Boolean.TRUE;
			
			//valida si aumentaron los pavos sin stock o si se agregaron pavos sin stock
			if(CotizacionReservacionUtil.esPavo(detalle.getArticuloDTO().getCodigoClasificacion(), request) && CollectionUtils.isNotEmpty(pedidoDTO.getNpDiferenciaDetallePedidoPOS())){
				//se recorren los detalles modificados
				for(DetallePedidoDTO detalleModificado : pedidoDTO.getNpDiferenciaDetallePedidoPOS()){
					//se comparan los articulos
					if(detalle.getId().getCodigoArticulo().equals(detalleModificado.getId().getCodigoArticulo())){
						//se comparan las cantidades
						if(detalle.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() <= detalleModificado.getEstadoDetallePedidoDTO().getCantidadEstado()){
							//si el detalle es el mismo no se valida el stock
							verificarStock = Boolean.FALSE;
							break;
						}
					}
				}
			}
			//si no tiene stock pero tiene autorizaciones aprobadas retorna true
			if((verificarStock && detalle.isNpSinStockPavPolCan() && !AutorizacionesUtil.verificarDetalleAutorizadoStock(detalle, request)
					&& AutorizacionesUtil.solicitarAutorizacionStockPorFechas(request, detalle.getArticuloDTO().getCodigoClasificacion()))
					|| (existeAutorizacionStockPendiente != null && existeAutorizacionStockPendiente)){
					return Boolean.TRUE;
			}
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * @param request
	 * @param colDetallePedido
	 * @return true si los detalles tienen problemas de stock y false caso contrario
	 * @throws Exception
	 */
	public static Boolean tieneProblemasDeStockPorClasificacionAnterior(HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedido ) throws Exception{
		
		Boolean cargarStockNuevaVersion = Boolean.TRUE;
		if(cargarStockNuevaVersion){
			return tieneProblemasDeStockPorClasificacion(request, colDetallePedido);
		}

		Boolean canastosSinStock = Boolean.FALSE;
		Boolean pavosSinStock = Boolean.FALSE;
		
		//se recupera de session el pedido
		PedidoDTO pedidoDTO = (PedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.pedidoReservar");
		
		Boolean tieneAutorizacionCanastos = (Boolean) request.getSession().getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_CANASTOS);
		Boolean tieneAutorizacionPavos  =   (Boolean) request.getSession().getAttribute(AutorizacionesUtil.TIENE_AUTORIZACION_STOCK_PAVOS);
		for(DetallePedidoDTO detalle : colDetallePedido){
			LogSISPE.getLog().info("articulo {} - {}", detalle.getId().getCodigoArticulo(), detalle.getArticuloDTO().getDescripcionArticulo());
			Boolean verificarStock = Boolean.TRUE;
			
			//valida si aumentaron los pavos sin stock o si se agregaron pavos sin stock
			if(CollectionUtils.isNotEmpty(pedidoDTO.getNpDiferenciaDetallePedidoPOS())){
				//se recorren los detalles modificados
				for(DetallePedidoDTO detalleModificado : pedidoDTO.getNpDiferenciaDetallePedidoPOS()){
					//se comparan los articulos
					if(detalle.getId().getCodigoArticulo().equals(detalleModificado.getId().getCodigoArticulo())){
						//se comparan las cantidades
						if(detalle.getEstadoDetallePedidoDTO().getCantidadEstado().longValue() <= detalleModificado.getEstadoDetallePedidoDTO().getCantidadEstado()){
							//si el detalle es el mismo no se valida el stock
							verificarStock = Boolean.FALSE;
							break;
						}
					}
				}
			}
			
			if(verificarStock && detalle.isNpSinStockPavPolCan()){
				if(CotizacionReservacionUtil.esCanasto(detalle.getArticuloDTO().getCodigoClasificacion(), request)){
					canastosSinStock = Boolean.TRUE;
					LogSISPE.getLog().info("canasto con problemas de stock");
				}
				else if(CotizacionReservacionUtil.esPavo(detalle.getArticuloDTO().getCodigoClasificacion(), request)){
					pavosSinStock = Boolean.TRUE;
					LogSISPE.getLog().info("pavo con problemas de stock");
				}
			}
		}
		if(canastosSinStock && (tieneAutorizacionCanastos == null || !tieneAutorizacionCanastos )){
			return Boolean.TRUE;
		}
		
		if(pavosSinStock && (tieneAutorizacionPavos == null || !tieneAutorizacionPavos )){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * order los estados el pedido
	 * @param request
	 * @param colEstados
	 */
	public static Collection<EstadoSICDTO> ordenarPorCodigosEstados(Collection<EstadoSICDTO> colEstados)throws Exception{
		//Estados Ordenados por codigo.
		String [] colEstAux  = {MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"),
							   MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"),
							   MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"),
							   MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"),
							   MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado")};
		
		Collection<EstadoSICDTO> colEstadosOrdenados = null;
		if(colEstados != null && !colEstados.isEmpty()){
			colEstadosOrdenados = new ArrayList<EstadoSICDTO>();
			for (int i=0; i<colEstados.size(); i++){
				for(EstadoSICDTO estadoPedido : colEstados){
					if(colEstAux[i].equals(estadoPedido.getId().getCodigoEstado())){
						colEstadosOrdenados.add(estadoPedido);
					}
				}
			}
		}
		return colEstadosOrdenados;
	}
	
	/**
	 * verifica si el pedido tiene problemas de stock en (pavos,pollos,canastas)
	 * @param request
	 * @param colDetallePedido
	 */
	public static void desactivarProblemasStock(HttpServletRequest request, Collection<DetallePedidoDTO> colDetallePedido)throws Exception{
		for(DetallePedidoDTO detalle : colDetallePedido){
			if(detalle.isNpSinStockPavPolCan()){
				detalle.setNpSinStockPavPolCan(false);
			}
		}
	}
	
	/*
	 * **************************************************** RESUMEN PEDIDOS***********************************************
	 */
	/**
	 * Este m\u00E9todo crea los tab de configuracion entrega pedido
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsConfiguracionEntregas(HttpServletRequest request, ActionForm formulario) {
		PaginaTab tabsCotizaciones = null;
		Map<String, Object> parametrosParaCorp;

		try {
			parametrosParaCorp=new HashMap<String, Object>();
			parametrosParaCorp.put("heightDivDatosGenerales", "170");
			parametrosParaCorp.put("columnSpacesComDatConPer", "385");
			parametrosParaCorp.put("labelSpaceComDatConPer", "125");
			parametrosParaCorp.put("scrollWidthComRelatedContacts", "500");
			parametrosParaCorp.put("scrollHeightComRelatedContacts", "120");			
			
			tabsCotizaciones = new PaginaTab("entregaLocalCalendario", "deplegar", 1,480, request);
			Tab resConEntOp1 = new Tab("Configuraci\u00F3n-entregas", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracionV2/resumenConEnt.jsp", true);
			Tab resConEntOp2 = new Tab("Responsables-entregas", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/resumenConEntAgr.jsp", false);
			
			tabsCotizaciones.addTab(resConEntOp1);
			tabsCotizaciones.addTab(resConEntOp2);
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsCotizaciones;
	}
	
	/**
	 * Este m\u00E9todo crea los tab de configuracion entrega pedido
	 * @author Wladimir L\u00F3pez
	 * @param request
	 * @return
	 */
	public static PaginaTab construirTabsPopUpConfEnt(HttpServletRequest request, ActionForm formulario) {
		PaginaTab tabsCotizaciones = null;
		Map<String, Object> parametrosParaCorp;

		try {
			parametrosParaCorp=new HashMap<String, Object>();
			parametrosParaCorp.put("heightDivDatosGenerales", "170");
			parametrosParaCorp.put("columnSpacesComDatConPer", "385");
			parametrosParaCorp.put("labelSpaceComDatConPer", "125");
			parametrosParaCorp.put("scrollWidthComRelatedContacts", "500");
			parametrosParaCorp.put("scrollHeightComRelatedContacts", "120");			
			
			Integer tabSeleccionado=(Integer)request.getSession().getAttribute(EntregaLocalCalendarioAction.TABSELECCIONADONAVEGACION);
						
			
			if(tabSeleccionado == null){
				tabSeleccionado = 0;
			}
			
			request.setAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR, "ec.com.smx.sic.controlesusuario.tabPopUp");
			request.setAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR, "rTabPopUp");
			tabsCotizaciones = new PaginaTab("entregaLocalCalendario", "deplegar", tabSeleccionado,350, request);
			String tabHabilitados=null;
			
			if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
				tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA);
			}
			else{
				if((String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA)!=null){
					tabHabilitados=(String)request.getSession().getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADRESERVA);
				}
			}
			
			
			
			if(tabHabilitados==null){
				Tab opcionesConfiguracion = new Tab("Configurar", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/opcionesCon.jsp", true,false);
				Tab calendarioConfiguracion = new Tab("Calendario", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/calendarioCon.jsp", false,false);
				
				tabsCotizaciones.addTab(opcionesConfiguracion);
				tabsCotizaciones.addTab(calendarioConfiguracion);
			}
			else{
				Tab opcionesConfiguracion = new Tab("Configurar", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/opcionesCon.jsp", false,false);
				Tab entregarParcialesConfiguracion = new Tab("Configurar cantidades parciales", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/entregasParcialesCon.jsp", true,false);
				Tab calendarioConfiguracion = new Tab("Calendario", "entregaLocalCalendario", "/servicioCliente/cotizarRecotizarReservar/entregas/nuevaConfiguracion/calendarioCon.jsp", false,false);
				
				tabsCotizaciones.addTab(opcionesConfiguracion);
				tabsCotizaciones.addTab(entregarParcialesConfiguracion);
				tabsCotizaciones.addTab(calendarioConfiguracion);
			}
			
		} catch (Exception e) {
			LogSISPE.getLog().error("Error al generar los tabs", e);
		}

		return tabsCotizaciones;
	}
	
	/**
	 * Este m\u00E9todo selecciona el tab de Contacto o DetalledelPEdido de acuerto a la posicionTab
	 * tab(0) Datos del contacto
	 * tab(1) datos del pedido
	 * @author bgudino
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabConfiguracionesEntregas(BeanSession beanSession, int posicionTab) {
		try{
			if(posicionTab == 0 || posicionTab ==1){
				beanSession.getPaginaTab().getTab(0).setSeleccionado(false);
				beanSession.getPaginaTab().getTab(1).setSeleccionado(false);
		
				beanSession.getPaginaTab().getTab(posicionTab).setSeleccionado(true);
				beanSession.getPaginaTab().setTabSeleccionado(posicionTab);
				beanSession.getPaginaTab().setTituloTabSeleccionado(beanSession.getPaginaTab().getTab(posicionTab).getTitulo());
			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al cambiar de tab", e);
		}
	}
	
	/**
	 * Este m\u00E9todo selecciona el tab de Contacto o DetalledelPEdido de acuerto a la posicionTab
	 * tab(0) Datos del contacto
	 * tab(1) datos del pedido
	 * @author bgudino
	 * @param beanSession, posicionTab
	 */
	public static void cambiarTabPopUpConfiguracionesEntregas(BeanSession beanSession, int posicionTab) {
		try{
			for(int i = 0; i<beanSession.getPaginaTabPopUp().getCantidadTabs(); i++){
				beanSession.getPaginaTabPopUp().getTab(i).setSeleccionado(Boolean.FALSE);
			}
//			if(posicionTab == 0 || posicionTab ==1){
//				beanSession.getPaginaTabPopUp().getTab(0).setSeleccionado(false);
//				beanSession.getPaginaTabPopUp().getTab(1).setSeleccionado(false);
		
				beanSession.getPaginaTabPopUp().getTab(posicionTab).setSeleccionado(true);
				beanSession.getPaginaTabPopUp().setTabSeleccionado(posicionTab);
				if(beanSession.getPaginaTab().getTab(posicionTab) !=null){
					beanSession.getPaginaTabPopUp().setTituloTabSeleccionado(beanSession.getPaginaTab().getTab(posicionTab).getTitulo());
				}
				
//			}
		}
		catch (Exception e) {
			LogSISPE.getLog().error("Error al cambiar de tab", e);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param tipoDescuento
	 * @return
	 */
	public static Boolean existeTipoDescuentoPedidoConsolidacion(HttpServletRequest request, String codigoDescuento){
		LogSISPE.getLog().info("se verifica si existe el codigoDescuento: {}",codigoDescuento);
//		Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
//		if(CollectionUtils.isEmpty(descuentos)){
//			descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.RESPALDO_DESCUENTOS_CONSOLIDADOS);
//		}
		Collection<DescuentoEstadoPedidoDTO> descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.RESPALDO_DESCUENTOS_CONSOLIDADOS);
		if(CollectionUtils.isEmpty(descuentos)){
			descuentos = (Collection<DescuentoEstadoPedidoDTO>) request.getSession().getAttribute(CotizarReservarAction.COL_DESCUENTOS);
		}
			
		if(CollectionUtils.isNotEmpty(descuentos)){
			for(DescuentoEstadoPedidoDTO descuentoEstadoPedido : descuentos){
				if(descuentoEstadoPedido.getId().getCodigoDescuento().equals(codigoDescuento)){
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static void instanciarPopUpImpresionConvenios(HttpServletRequest request,Boolean btnCerrar)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Impresi\u00F3n convenios");
		//popUp.setEtiquetaBotonOK("Si");
		popUp.setTope(80d);
		popUp.setAncho(35d);
//		popUp.setEtiquetaBotonCANCEL("Cancelar");
		//popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
//		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		//popUp.setPreguntaVentana("Desea adjuntar un archivo del beneficiario al pedido?");
		if(btnCerrar){
			popUp.setValorCANCEL("requestAjax('detalleEstadoPedido.do', ['pregunta'], {parameters: 'cancelarDiferido=ok',popWait:false});ocultarModal();");
			popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());	
		}else{
			popUp.setNoMostrarBotonCerrar("ok");
		}
		popUp.setContenidoVentana("servicioCliente/estadoPedido/popUpImpresionConvenios.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
}
