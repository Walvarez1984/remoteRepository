/*
 * CalendarioBodegaForm.java
 * Creado el 21/07/2010 16:27:02
 *   	
 */
package ec.com.smx.sic.sispe.web.calendarioBodega.struts.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.commons.util.UtilesSISPE;
import ec.com.smx.sic.sispe.commons.util.dto.CantidadCalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioConfiguracionDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.DireccionesDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * @author jvillacis
 *
 */
@SuppressWarnings("serial")
public class CalendarioBodegaForm extends ActionForm {

/**
	 * Par\u00E1metros de paginaci\u00F3n
	 * <ul>
	 * <li>Collection datos: Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
	 * <li>String start: Indice para el inicio del paginador</li>
	 * <li>String range: Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada paginaci\u00F3n</li>
	 * <li>String size: Valor que indica el n\u00FAmero total de registros.</li>
	 * </ul>
	 */
	private Collection datos;
	private String start;
	private String range;
	private String size;
	private String opLugarEntrega;
	private String opTipoEntrega;
	private String opStock;
	private String localResponsable;
	private String[] checkTransitoArray;
	private String opLocalResponsable;
	private String fechaEntregaCliente;
	private String listaLocales;
	private Object[] calendarioDiaLocal;
	private String[] cantidadEstados;
	private String[] cantidadPedidaBodega;
	private String  nombreContacto;
	private String fechaDespacho;
	private String local;
	private String idLocalOSector;
	public static final String CALENDARIODIALOCAL= "ec.com.smx.calendarizacion.calendarioDiaLocalDTO"; //CD del dia seleccionado en el calendario
	
	
	/**
	 * 
	 * @param datos
	 * @param formulario
	 * @param inicio
	 * @return
	 */
	public Collection paginarDatos(Collection datos, int inicio, int size,boolean paginacionBase) throws Exception{
		LogSISPE.getLog().info("PAGINACION");
        int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        int start = inicio;
        
        this.start = String.valueOf(start);
        this.range = String.valueOf(range);
        this.size = String.valueOf(size);
        
        LogSISPE.getLog().info("start: {}" , this.start);
        LogSISPE.getLog().info("range: {}" , this.range);
        LogSISPE.getLog().info("size: {}" , this.size);
        if(paginacionBase)
        	return datos;
        
        return Util.obtenerSubCollection(datos, start, start + range > size ? size : start + range);
	}
  
	/**
	 * @author jacalderon
	 * @param request
	 */
	public void mantenerValoresEntregas(HttpServletRequest request){
		HttpSession session=request.getSession();
		int indexDetalles=0;//indice para las cantidades de los detalles
		//Obtengo el detalle del pedido
		Collection detallePedidoDTOCol=(Collection)session.getAttribute(EntregaLocalCalendarioAction.DETALLELPEDIDOAUX);
		if(detallePedidoDTOCol != null){
			for(Iterator numeroDetalle=detallePedidoDTOCol.iterator();numeroDetalle.hasNext();){
				DetallePedidoDTO detallePedidoDTO = (DetallePedidoDTO)numeroDetalle.next();
				LogSISPE.getLog().info("indice de cantidades: {}" , indexDetalles);
				long cantEntregada = detallePedidoDTO.getNpContadorEntrega();//Numero de articulos entregados
				LogSISPE.getLog().info("*********npCantidadEstado******* {}" , detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
				/*******************Verifico cantidades en los detalles del pedido*********************/
				//Si se va a entregar parcialmente
				if(session.getAttribute(EntregaLocalCalendarioAction.HABILITARCANTIDADENTREGA)!=null){
					LogSISPE.getLog().info("****cantidad ingresada******{}" , this.cantidadEstados[indexDetalles]);
					if(this.cantidadEstados[indexDetalles]!=null && !this.cantidadEstados[indexDetalles].equals("")){
						LogSISPE.getLog().info("***********el articulo {} fue seleccionado*****************",indexDetalles);
						LogSISPE.getLog().info("cantidadEstado[{}]: {}",indexDetalles,this.cantidadEstados[indexDetalles]);
						long cantidadMaximaDetalle=detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadEstado().longValue()-cantEntregada;
						LogSISPE.getLog().info("cantidadMaximaDetalle:{}" , cantidadMaximaDetalle);
						//Valida el formato de la cantidad ingresada
						try{
							detallePedidoDTO.getEstadoDetallePedidoDTO().setNpCantidadEstado(new Long(this.cantidadEstados[indexDetalles]));
						}catch(NumberFormatException e){
							LogSISPE.getLog().info("cantidad errada para la cantidad de entrega");
						}
					}
				}
				//Si se va a pedir a bodega
				if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 
						&& this.opStock != null 
						&& (this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.parcialBodega"))
								|| this.opStock.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))){
					if(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado().longValue()>0 && this.cantidadPedidaBodega[indexDetalles]!=null && !this.cantidadPedidaBodega[indexDetalles].equals("")){
						/******************Valido las cantidades ingresadas en el pedido a bodega**************/
						//Valida el formato de la cantidad ingresada
						try{
							//Si la entrega es parcial y se va a entregar todo desde el CD
							if(session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA)!=null && session.getAttribute(EntregaLocalCalendarioAction.TIPOENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoEntrega.parcial")) && session.getAttribute(EntregaLocalCalendarioAction.STOCKENTREGA).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.stock.totalBodega")))
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(detallePedidoDTO.getEstadoDetallePedidoDTO().getNpCantidadEstado());
							else
								detallePedidoDTO.getEstadoDetallePedidoDTO().setCantidadReservarSIC(new Long(this.cantidadPedidaBodega[indexDetalles]));
						}catch(NumberFormatException e){
							LogSISPE.getLog().info("cantidad errada para la cantidad de reserva");
						}
					}
				}
				indexDetalles++;
			}
		}
	}
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>cotizarReservar.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		//ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();

		String accion = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
		LogSISPE.getLog().info("por validar - calendarioBodega {}",accion);

		String peticion = request.getParameter(Globals.AYUDA);
		LogSISPE.getLog().info("Valor de la peticion de Cotizar-->"+peticion);
		
		if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null){
			LogSISPE.getLog().info("inicial calendario local");	
		 	this.setCalendarioDiaLocal(((Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)));
		}

		return errors;
	}


	/**
	 * Resetea los controles del formulario de la p\u00E1gina <code>cotizarReservar.jsp</code>, en cada petici\u00F3n.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		LogSISPE.getLog().info("por resetear");
		this.opTipoEntrega=null;
		this.opLugarEntrega=null;
		this.opStock=null;
		this.checkTransitoArray=null;
		this.localResponsable=null;
		this.fechaEntregaCliente=null;
		this.opLocalResponsable=null;
		this.listaLocales=null;
		this.calendarioDiaLocal=null;
		this.nombreContacto=null;
		this.fechaDespacho=null;
		this.local=null;
		this.idLocalOSector=null;
	}

	/**
	 * Permite validar los campos requeridos para las cantidades.
	 * @author walvarez
	 * @param errors
	 * @param request
	 * 
	 * @return Devuelve un int.
	 */
	@SuppressWarnings("unchecked")
	public int validarCantidades(ActionMessages errors,HttpServletRequest request){
		PropertyValidator validar= new PropertyValidatorImpl();
		HttpSession session = request.getSession();
		DireccionesDTO direccionesDTO = null;
		int numeroBultos=0;//variable para convertir la cantidad ingresada a bultos
		Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
		CalendarioDiaLocalDTO calendarioDiaLocalDTO=(CalendarioDiaLocalDTO)session.getAttribute(CALENDARIODIALOCAL);
		LogSISPE.getLog().info("local del calendario: {}" , calendarioDiaLocalDTO.getId().getCodigoLocal());
		CalendarioConfiguracionDiaLocalDTO calendarioConfiguracionDiaLocalDTO=new CalendarioConfiguracionDiaLocalDTO();
		if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1)!=null){
			//obtengo la configuracion del calendario
			calendarioConfiguracionDiaLocalDTO=(CalendarioConfiguracionDiaLocalDTO)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1);
		}
		try{
		//Obtengo la configuracion del dia seleccionado
		CalendarioDiaLocalDTO calendarioDiaLocalActual=SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioDiaLocalPorID(calendarioDiaLocalDTO.getId(),calendarioConfiguracionDiaLocalDTO);
		//Obtengo el detalle del pedido
		Collection detallePedidoDTOCol=(Collection)session.getAttribute(CotizarReservarAction.DETALLE_PEDIDO);
		LogSISPE.getLog().info("detallePedidoDTOCol: {}",detallePedidoDTOCol.size());
		DetallePedidoDTO detallePedidoDTO= (DetallePedidoDTO)detallePedidoDTOCol.iterator().next();
		LogSISPE.getLog().info("detallePedidoDTO: {}",detallePedidoDTO);
		if(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue()>0){
			LogSISPE.getLog().info("Ingreso a calcular los detalles: {}");
			//calculo el numero de bultos
			try {
				numeroBultos = UtilesSISPE.calcularCantidadBultos(detallePedidoDTO.getEstadoDetallePedidoDTO().getCantidadReservarSIC().longValue(), detallePedidoDTO.getArticuloDTO());
				
				//cantidades informativas
				cantidadesInformativas = new ArrayList<CantidadCalendarioDiaLocalDTO>();
				//cantidad para frio
				CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
				cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
				cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
				cantidadesInformativas.add(cantidadFrio);
				//cantidad para seco
				CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
				cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
				cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
				cantidadesInformativas.add(cantidadSeco);
				
			} catch (SISPEException e) {
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				LogSISPE.getLog().info("error al calcular el numero de bultos detalles");
			}
		}
		
		LogSISPE.getLog().info("numero de bultos:{}" , numeroBultos);
		LogSISPE.getLog().info("CD: {}" , calendarioDiaLocalActual.getCantidadDisponible());
		if((new Long(ConverterUtil.fromDoubleToInteger(calendarioDiaLocalActual.getCantidadDisponible()))).longValue()<numeroBultos){
			//Si no existe la capacidad verifica si tiene autorizacion
			GregorianCalendar horaDespacho=new GregorianCalendar();
			horaDespacho.setTime(calendarioDiaLocalActual.getId().getFechaCalendarioDia());
			if(!calendarioDiaLocalActual.getId().getCodigoLocal().toString().equals((SessionManagerSISPE.getCurrentLocal(request)).toString())){
				calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
							calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),true, cantidadesInformativas, calendarioDiaLocalActual);
					//Obtengo los dias que debo modificar su capacidad acumulada
					Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
					for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
						//cantidades informativas
						Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
						//cantidad para frio
						CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
						cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
						cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
						cantidadesInformativasAux.add(cantidadFrio);
						//cantidad para seco
						CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
						cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
						cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
						cantidadesInformativasAux.add(cantidadSeco);
					
						calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
								calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),true, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
					}
					session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
			}
			else{
				calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
						calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),false, cantidadesInformativas, calendarioDiaLocalActual);
				//Obtengo los dias que debo modificar su capacidad acumulada
				Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
				for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
					//cantidades informativas
					Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
					//cantidad para frio
					CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
					cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
					cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
					cantidadesInformativasAux.add(cantidadFrio);
					//cantidad para seco
					CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
					cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
					cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
					cantidadesInformativasAux.add(cantidadSeco);
					
					calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
							calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),false, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
				}
				session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
			}
		}
		else{
			calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalActual.getId().getCodigoCompania(), calendarioDiaLocalActual.getId().getCodigoLocal(), calendarioDiaLocalActual.getId().getFechaCalendarioDia(),calendarioDiaLocalActual.getCapacidadNormal(), 
					calendarioDiaLocalActual.getCapacidadAdicional(), Double.valueOf(numeroBultos), Double.valueOf(0),false, cantidadesInformativas, calendarioDiaLocalActual);
			//Obtengo los dias que debo modificar su capacidad acumulada
			Collection<CalendarioDiaLocalDTO> calendarioDiaLocalDTOCol=(ArrayList<CalendarioDiaLocalDTO>)session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOLAUX);
			for(CalendarioDiaLocalDTO calendarioDiaLocalDTOAux:calendarioDiaLocalDTOCol){
				//cantidades informativas
				Collection<CantidadCalendarioDiaLocalDTO> cantidadesInformativasAux = new ArrayList<CantidadCalendarioDiaLocalDTO>();
				//cantidad para frio
				CantidadCalendarioDiaLocalDTO cantidadFrio = new CantidadCalendarioDiaLocalDTO();
				cantidadFrio.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.frio"));
				cantidadFrio.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadFrio.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
				cantidadesInformativasAux.add(cantidadFrio);
				//cantidad para seco
				CantidadCalendarioDiaLocalDTO cantidadSeco = new CantidadCalendarioDiaLocalDTO();
				cantidadSeco.setTipoCantidad(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoBodega.seco"));
				cantidadSeco.setCantidad((detallePedidoDTO.getArticuloDTO().getNpTipoBodega() != null && detallePedidoDTO.getArticuloDTO().getNpTipoBodega().equals(cantidadSeco.getTipoCantidad())) ? new Long(numeroBultos) : 0L );
				cantidadesInformativasAux.add(cantidadSeco);
				
				calendarioConfiguracionDiaLocalDTO.setConfiguracionDiaLocal(calendarioDiaLocalDTOAux.getId().getCodigoCompania(), calendarioDiaLocalDTOAux.getId().getCodigoLocal(), calendarioDiaLocalDTOAux.getId().getFechaCalendarioDia(),calendarioDiaLocalDTOAux.getCapacidadNormal(), 
						calendarioDiaLocalDTOAux.getCapacidadAdicional(), Double.valueOf(0), Double.valueOf(numeroBultos),false, cantidadesInformativasAux, calendarioDiaLocalDTOAux);
			}
			session.setAttribute(EntregaLocalCalendarioAction.CALENDARIOCONFIGURACIONDIALOCALAUX1, calendarioConfiguracionDiaLocalDTO);
		}
		}
		catch(Exception e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			return -1;
		}
		return 0;
	}
	public String getOpLugarEntrega() {
		return opLugarEntrega;
	}


	public void setOpLugarEntrega(String opLugarEntrega) {
		this.opLugarEntrega = opLugarEntrega;
	}


	public String getOpTipoEntrega() {
		return opTipoEntrega;
	}


	public void setOpTipoEntrega(String opTipoEntrega) {
		this.opTipoEntrega = opTipoEntrega;
	}


	public String getOpStock() {
		return opStock;
	}


	public void setOpStock(String opStock) {
		this.opStock = opStock;
	}


	public String getLocalResponsable() {
		return localResponsable;
	}


	public void setLocalResponsable(String localResponsable) {
		this.localResponsable = localResponsable;
	}


	public String[] getCheckTransitoArray() {
		return checkTransitoArray;
	}


	public void setCheckTransitoArray(String[] checkTransitoArray) {
		this.checkTransitoArray = checkTransitoArray;
	}


	public String getOpLocalResponsable() {
		return opLocalResponsable;
	}


	public void setOpLocalResponsable(String opLocalResponsable) {
		this.opLocalResponsable = opLocalResponsable;
	}


	public String getFechaEntregaCliente() {
		return fechaEntregaCliente;
	}


	public void setFechaEntregaCliente(String fechaEntregaCliente) {
		this.fechaEntregaCliente = fechaEntregaCliente;
	}


	public Collection getDatos() {
		return datos;
	}


	public void setDatos(Collection datos) {
		this.datos = datos;
	}


	public String getStart() {
		return start;
	}


	public void setStart(String start) {
		this.start = start;
	}


	public String getRange() {
		return range;
	}


	public void setRange(String range) {
		this.range = range;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getListaLocales() {
		return listaLocales;
	}


	public void setListaLocales(String listaLocales) {
		this.listaLocales = listaLocales;
	}


	public Object[] getCalendarioDiaLocal() {
		return calendarioDiaLocal;
	}


	public void setCalendarioDiaLocal(Object[] calendarioDiaLocal) {
		this.calendarioDiaLocal = calendarioDiaLocal;
	}

	public String[] getCantidadEstados() {
		return cantidadEstados;
	}

	public void setCantidadEstados(String[] cantidadEstados) {
		this.cantidadEstados = cantidadEstados;
	}

	public String[] getCantidadPedidaBodega() {
		return cantidadPedidaBodega;
	}

	public void setCantidadPedidaBodega(String[] cantidadPedidaBodega) {
		this.cantidadPedidaBodega = cantidadPedidaBodega;
	}

	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	public String getFechaDespacho() {
		return fechaDespacho;
	}

	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getIdLocalOSector() {
		return idLocalOSector;
	}

	public void setIdLocalOSector(String idLocalOSector) {
		this.idLocalOSector = idLocalOSector;
	}
}
