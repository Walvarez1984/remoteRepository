/*
 * ListaPedidosAction.java
 * Creado el 04/07/2008 9:31:11
 *   	
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.ControlMensajes;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * 
 * @author fmunoz
 */
public class ListadoPedidosAction extends BaseAction
{
	public static final String ORDEN_ASCENDENTE = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
	public static final String COL_SUB_PAGINA = "ec.com.smx.sic.sispe.pedidos.subPagina";
	static final String VOLVER_A_BUSQUEDA = "ec.com.smx.sic.sispe.mostrar.volverABusqueda";
	private static final String ACTIVAR_REENVIO_RESERVACION = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("reenvioReservacion");
	
	//posibles acciones que accedan a esta acci\u00F3n gen\u00E9rica
	private String accionRecotizarReservar = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.recotizarReservar");
	private String accionModificarReserva = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarReservacion");
	private String accionConfirmarPesosFinales = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.confirmarReservacion");
	private String accionModificarFechasEntrega = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.modificarFechasEntrega");
	public static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");	
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   * 
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de campos
   * @param request 				La petici&oacute;n que estamos procesando
   * @param response				La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception 
  {
  	ActionMessages success = new ActionMessages();
    ActionMessages infos = new ActionMessages();
    ActionMessages errors = new ActionMessages();
    
    HttpSession session= request.getSession();
    ListadoPedidosForm formulario = (ListadoPedidosForm)form;
    String salida = "desplegar";
    
    String accionActual = (String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
    String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
    String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
    String accionConsolidado=(String) session.getAttribute(WebSISPEUtil.ACCION_CONSOLIDAR);
    session.removeAttribute("ec.com.smx.sic.sispe.estado.actual");
    String codigoInicialIds = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID");
    try
    {
      /*--------------- cuando se da clic en los campos de paginaci\u00F3n -----------------------*/
      if(request.getParameter("range")!=null || request.getParameter("start")!=null)
      {
    	  
        LogSISPE.getLog().info("ENTRO A LA PAGINACION");
//        Collection datos= (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
//        if(datos!=null){
//          formulario.setSize(String.valueOf(datos.size()));
//          int size= datos.size();
//          int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
//          int start= Integer.parseInt(request.getParameter("start"));
//          formulario.setStart(String.valueOf(start));
//          formulario.setRange(String.valueOf(range));
//          formulario.setSize(String.valueOf(size));
//          
//          Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
//          verificarAutorizacionesPedido(datosSub);
//          session.setAttribute(COL_SUB_PAGINA,datosSub);
//        }
        
	        //se obtiene el tamano de la coleccion total de articulos
			int tamano = ((Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS)).intValue();
			LogSISPE.getLog().info("tama\u00F1o: {}" , tamano);
			String start = request.getParameter("start");
	
			if(tamano > 0){
				
				//Recupero la vista pedido con los parametros de la busqueda
				VistaPedidoDTO consultaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO);
				//Cambio el inicio de la busqueda
				consultaPedidoDTO.setNpFirstResult(Integer.valueOf(start));
				Collection<VistaPedidoDTO> colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
				verificarAutorizacionesPedido(colVistaPedidoDTO);
				
				//se asignan las variables de paginaci\u00F3n
				formulario.setStart(start);
				formulario.setRange(request.getParameter("range"));
				formulario.setSize(String.valueOf(tamano));
				formulario.setDatos(colVistaPedidoDTO);
				session.setAttribute(COL_SUB_PAGINA,colVistaPedidoDTO);
						
		        //se guarda la colecci\u00F3n general
		        session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
			    //se guarda en sesi\u00F3n el indice de la paginaci\u00F3n
		        session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
		  }
      }
      /*-------------------------------------- cuando se escogi\u00F3 Buscar -------------------------------------------*/
      else if(request.getParameter("buscar")!=null)
      {
        //colecci\u00F3n que almacena los pedidos buscados
        Collection<VistaPedidoDTO> colVistaPedidoDTO = null;
        VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
        formulario.setDatos(null);
        //se buscar por estado actual [SI]
        //consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

        try
        {
        	
        	int start = 0;
        	int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        	int total = 0;
        	
          if(accionActual!=null && accionActual.equals(accionRecotizarReservar)){
        		session.setAttribute(SessionManagerSISPE.ESTADO_ACTUAL, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
            //si no se asign\u00F3 un estado en particular se consulta por los tres
            if(consultaPedidoDTO.getId().getCodigoEstado().equals(codigoInicialIds)){
            	consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")
            		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
                .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
            }
            //se asignan los campos de ordenamiento
      		consultaPedidoDTO.setNpCamposOrden(new String [][]{{"fechaInicialEstado",ORDEN_ASCENDENTE}});
      		consultaPedidoDTO.setVistaDetallesPedidos(new ArrayList<VistaDetallePedidoDTO>());
      		
      		total = asignarPaginacionConsulta(session, consultaPedidoDTO, start, range);
      		
            colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
            
            //verifica si tiene autorizaciones de stok o descuento variable
//            verificarAutorizaciones(colVistaPedidoDTO);
          }else if(accionActual!=null && accionActual.equals(accionModificarReserva)){
            
        	//si no se asign\u00F3 un estado en particular se consulta por los tres
            if(consultaPedidoDTO.getId().getCodigoEstado().equals(codigoInicialIds)){
            	//consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
            		//.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
                //.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")));
            	String codigosEstadosPermitidos = CotizacionReservacionUtil.obtenerCodigosEstadoPedidosModificacionReserva(request);
            	consultaPedidoDTO.getId().setCodigoEstado(codigosEstadosPermitidos!=null?codigosEstadosPermitidos:codigoInicialIds);
            }
            //se asignan los campos de ordenamiento
            consultaPedidoDTO.setNpCamposOrden(new String [][]{{"codigoPedido",ORDEN_ASCENDENTE},{"fechaInicialEstado",ORDEN_ASCENDENTE}});
            //si el usuario actual no tiene permisos para realizar el reenv\u00EDo de reservaci\u00F3n no se obtienen los pedidos que est\u00E1n pagados totalmente
            if(session.getAttribute(ACTIVAR_REENVIO_RESERVACION) == null){
            	consultaPedidoDTO.setCodigoEstadoPagado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente")
                		.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente"))
	            		.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago"))
	            		.concat(",").concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado")));
            }
            //siempre buscar por el estado actual
            consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
            session.setAttribute("ec.com.smx.sic.sispe.estado.actual",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
            consultaPedidoDTO.setVistaDetallesPedidos(new ArrayList<VistaDetallePedidoDTO>());
            
            total = asignarPaginacionConsulta(session, consultaPedidoDTO, start, range);
            
            colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
            
            //verifica si tiene autorizaciones de stok o descuento variable
            verificarAutorizaciones(colVistaPedidoDTO);
          }else if(accionActual!=null && accionActual.equals(accionConfirmarPesosFinales)){
          	//se asignan los campos de ordenamiento
          	consultaPedidoDTO.setNpCamposOrden(new String [][]{{"primeraFechaEntrega",ORDEN_ASCENDENTE}});
          	
          	
        	if(consultaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoInicializaAtributosID"))){
        		consultaPedidoDTO.getId().setCodigoEstado(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RESERVADO + "," + ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ENPRODUCCION + "," +
						ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PRODUCIDO + "," + ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO + "," + ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RESERVA_CONFIRMADA);
			}
        	consultaPedidoDTO.setConfirmarReserva(ConstantesGenerales.ESTADO_ACTIVO);
        	
          	total = asignarPaginacionConsulta(session, consultaPedidoDTO, start, range);
          	
            //se obtiene la colecci\u00F3n de Pedidos buscados y se la almacena en sesi\u00F3n
//          	colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaConfirmarPesos(consultaPedidoDTO);
          	colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
          	
          }else if(accionActual != null && accionActual.equals(accionModificarFechasEntrega)){
          	//si no se asign\u00F3 un estado en particular se consulta por los tres
            if(consultaPedidoDTO.getId().getCodigoEstado().equals(codigoInicialIds)){
            	consultaPedidoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
            		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
                .concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")));
            }
            //para que obtenga solo los pedidos que hayan soliciado mercader\u00EDa al CD
            consultaPedidoDTO.setCantidadReservadaEstado(1l);
            consultaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
            
            total = asignarPaginacionConsulta(session, consultaPedidoDTO, start, range);
            
            colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
          }
          
          if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
          	//se muestra un mensaje indicando que la lista est\u00E1 vacia
          	if(accionActual!=null && accionActual.equals(accionRecotizarReservar)){
          		infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos pendientes para Reservaci\u00F3n"));
          	}else if(accionActual!=null && accionActual.equals(accionModificarReserva)){
          		infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Reservaciones con posibilidad de cambio"));
          	}else if(accionActual!=null && accionActual.equals(accionConfirmarPesosFinales)){
          		infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos para Confirmaci\u00F3n de Pesos"));
          	}else if(accionActual!=null && accionActual.equals(accionModificarFechasEntrega)){
          		infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos para Modificar Fechas de entrega"));
          	}
          	
            session.removeAttribute(COL_SUB_PAGINA);
          }else{
//            LogSISPE.getLog().info("ENTRO A LA PAGINACION");
//            formulario.setSize(String.valueOf(colVistaPedidoDTO.size()));
//            int size= colVistaPedidoDTO.size();
//            int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
//            int start= 0;
//            formulario.setStart(String.valueOf(start));
//            formulario.setRange(String.valueOf(range));
//            formulario.setSize(String.valueOf(size));
//            
//            Collection datosSub = Util.obtenerSubCollection(colVistaPedidoDTO,start, start + range > size ? size : start+range);
//            verificarAutorizacionesPedido(datosSub);
//            session.setAttribute(COL_SUB_PAGINA,datosSub);
        	  
        	  session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
        	  
              LogSISPE.getLog().info("ENTRO A LA PAGINACION");
              formulario.setDatos(colVistaPedidoDTO);
              formulario.setStart(String.valueOf(start));
              formulario.setRange(String.valueOf(range));
              formulario.setSize(String.valueOf(total));
              verificarAutorizacionesPedido(colVistaPedidoDTO);
              session.setAttribute(COL_SUB_PAGINA,colVistaPedidoDTO);
          }
        }
        catch(SISPEException ex){
          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          //si ocurre un error al llamar al m\u00E9todo del servicio
          errors.add("Pedidos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos"));
        }
        
        //se guarda colecci\u00F3n de los pedidos
        session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);

      }else{
        Collection pedidosBuscados = null;
        if(session.getAttribute(VOLVER_A_BUSQUEDA)!=null){
        	//se guarda una variable la lista de los pedidos buscados anteriormente
        	pedidosBuscados = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
        	LogSISPE.getLog().info("pedidos buscados: {}",pedidosBuscados);
        }

      	//se verifica si existen mensajes de exito en la sesi\u00F3n
      	ControlMensajes controlMensajes = new ControlMensajes();
      	controlMensajes.getMessages(success,session);
      	controlMensajes = null;
      	
      	//se obtiene de sesion datos de paginacion para que no se pierdan en el siguiente metodo
        //se obtiene el tamano de la coleccion total de articulos
		int tamano = session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null 
				? (Integer)session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) : 0;
		String start = (String) session.getAttribute(ListadoPedidosForm.INDICE_PAGINACION);
		VistaPedidoDTO consultaPedidoDTO = (VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO);

        //se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
        SessionManagerSISPE.removeVarSession(request);
        
        if(accionConsolidado!=null && !accionActual.equals(accionRecotizarReservar)){
        	session.setAttribute(WebSISPEUtil.ACCION_CONSOLIDAR,accionConsolidado);
        }
        //se sube nuevamente la variable porque en la senetencia anterior se eliminan todas las variables de sesi\u00F3n
        session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, accionActual);
        formulario.setOpEstadoActivo(formulario.getEstadoActual()?ConstantesGenerales.ESTADO_ACTIVO:ConstantesGenerales.ESTADO_INACTIVO);
        String tituloVentana = "";
        //se realiza la consulta de estados
        EstadoSICDTO consultaEstadoDTO = null;
        
        //se verifican las acciones para cargar los estados
      	if(accionActual!=null && accionActual.equals(accionRecotizarReservar)){
      		consultaEstadoDTO = new EstadoSICDTO();
	        consultaEstadoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizado")
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.recotizado"))
	        		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.cotizacionCaducada")));
	        tituloVentana = "B\u00FAsqueda de Cotizaciones y Recotizaciones";
      	}else if(accionActual!=null && accionActual.equals(accionModificarReserva)){
      		consultaEstadoDTO = new EstadoSICDTO();
      		String codigosEstadosPermitidos = CotizacionReservacionUtil.obtenerCodigosEstadoPedidosModificacionReserva(request);
      		consultaEstadoDTO.getId().setCodigoEstado(codigosEstadosPermitidos!=null?codigosEstadosPermitidos:codigoInicialIds);
	        //consultaEstadoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
	        //		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
	        //		.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido")));
      		tituloVentana = "B\u00FAsqueda para modificaciones de reserva";
      		session.setAttribute("ec.com.smx.sic.sispe.estado.pagado.sinPago", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago"));
      		session.setAttribute("ec.com.smx.sic.sispe.estado.pagado.totalmente", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
      		
      		//se obtiene el par\u00E1metro que contiene el id del usuario administrador
      		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoUsuarioAdministrador", request);
//      		if(parametroDTO.getValorParametro()!=null 
//      				&& parametroDTO.getValorParametro().equals(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario())){
//      			//si el usuario logeado es el administrador tiene la opci\u00F3n de reenviar la reservaci\u00F3n al SIC
//      			session.setAttribute(ACTIVAR_REENVIO_RESERVACION, "ok");
//      		}
      		
      		//se verifica si el parametro para habilitar el ReenvioReservaCD esta activo
      		ParametroDTO parametroAuxDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.habilitar.ReenvioReservaCD", request);
	  		
      		if (parametroAuxDTO.getValorParametro()!=null 
	  			&& parametroAuxDTO.getValorParametro().equals(ConstantesGenerales.ESTADO_ACTIVO) 
	  				&& parametroDTO.getValorParametro()!=null 
	  	      			&& parametroDTO.getValorParametro().equals(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario())){
	  			//si el usuario logeado es el administrador tiene la opci\u00F3n de reenviar la reservaci\u00F3n al SIC
	  			session.setAttribute(ACTIVAR_REENVIO_RESERVACION, "ok");
	  		}
      		
      		//siempre buscar por el estado actual
            session.setAttribute("ec.com.smx.sic.sispe.estado.actual",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
            
      	}else if(accionActual!=null && accionActual.equals(accionConfirmarPesosFinales)){
      		consultaEstadoDTO = new EstadoSICDTO();
      		consultaEstadoDTO.getId().setCodigoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado")
      				.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
      				.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
      				.concat(caraterSeparacion).concat(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado")));
      		tituloVentana = "B\u00FAsqueda Para Registrar Pesos Finales";
      		
      	}else if(accionActual!=null && accionActual.equals(accionModificarFechasEntrega)){
      		tituloVentana = "B\u00FAsqueda Para Modificar Fechas de Entrega";
      	}
        
      	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de pedido");
        //VARIABLE DE SESION QUE CONTROLA LOS TITULOS DE LAS VENTANAS
        session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, tituloVentana);
        
        if(consultaEstadoDTO != null){
        	//Obtener datos de la colecci\u00F3n de estados, en la base de datos
        	Collection estados = SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(consultaEstadoDTO);
        	if(accionActual!=null && accionActual.equals(accionModificarReserva)){
        		Collection<EstadoSICDTO> colEstOrdenados = WebSISPEUtil.ordenarPorCodigosEstados(estados);
            	//guardar en sesi\u00F3n esta colecci\u00F3n
            	session.setAttribute(SessionManagerSISPE.COL_ESTADOS, colEstOrdenados!=null&&!colEstOrdenados.isEmpty()?colEstOrdenados:estados);
        	}else{
        		//guardar en sesi\u00F3n esta colecci\u00F3n
            	session.setAttribute(SessionManagerSISPE.COL_ESTADOS,estados);
        	}
        }
        
        formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));

        //se verifica si la entidad responsable es un local
        String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
        if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
        	formulario.setCodigoLocal(SessionManagerSISPE.getCurrentEntidadResponsable(request).getCodigoLocal().toString());
        else
        	//se obtienen los locales por ciudad
        	SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

        if(pedidosBuscados!=null){
        	//se restablece el valor de la lista de pedidos si es el caso 
//        	session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,pedidosBuscados);
//        	Collection datos = pedidosBuscados;
//        	formulario.setSize(String.valueOf(datos.size()));
//        	int size= datos.size();
//        	int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
//        	int start= 0;
//        	formulario.setStart(String.valueOf(start));
//        	formulario.setRange(String.valueOf(range));
//        	formulario.setSize(String.valueOf(size));
//
//        	Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
//        	session.setAttribute(COL_SUB_PAGINA,datosSub);
        	
        	if(tamano > 0){
    			
    			//se asignan las variables de paginaci\u00F3n
    			formulario.setStart(start);
    			formulario.setRange(MessagesWebSISPE.getString("parametro.paginacion.rango"));
    			formulario.setSize(String.valueOf(tamano));
    			formulario.setDatos(pedidosBuscados);
    			session.setAttribute(COL_SUB_PAGINA,pedidosBuscados);
    					
    	        //se guarda la colecci\u00F3n general
    	        session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,pedidosBuscados);
    		    //se guarda en sesi\u00F3n el indice de la paginaci\u00F3n
    	        session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, tamano);
    	        session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
    			session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
    		}
        }
      }
    }catch(Exception ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    
    //se guardan los mensajes
    saveMessages(request, success);
    saveInfos(request, infos);
    saveErrors(request, errors);
    
    return mapping.findForward(salida);
  }
  
  public static void verificarAutorizacionesPedido ( Collection<VistaPedidoDTO> vistaPedidoCol){
	  
    if(CollectionUtils.isNotEmpty(vistaPedidoCol)){
    	//para las autorizaciones de descuento variable
		  for(VistaPedidoDTO vistaActual : vistaPedidoCol){
			  if(StringUtils.isNotEmpty(vistaActual.getTieneAutorizacionDctVar())){
				  if(!vistaActual.getTieneAutorizacionDctVar().equals(ConstantesGenerales.ESTADO_SI)){
					  if(Integer.parseInt(vistaActual.getTieneAutorizacionDctVar()) > 0){
						  vistaActual.setTieneAutorizacionDctVar(ConstantesGenerales.ESTADO_SI);
					  }else{
						  vistaActual.setTieneAutorizacionDctVar(null);
					  }
				  }
			  }
			  //para las autorizaciones de stock
			  if(StringUtils.isNotEmpty(vistaActual.getTieneAutorizacionStock())){
				  if(!vistaActual.getTieneAutorizacionStock().equals(ConstantesGenerales.ESTADO_SI)){
					  if(Integer.parseInt(vistaActual.getTieneAutorizacionStock()) > 0){
						  vistaActual.setTieneAutorizacionStock(ConstantesGenerales.ESTADO_SI);
					  }else{
						  vistaActual.setTieneAutorizacionStock(null);
					  }
				  }
			  }
		  }
	 }
  }
  
  /**
   * Verifica si el pedido tiene autorizaciones de descuento variable o de stock
   * @param colVistaPedidoDTO
   */
  public void verificarAutorizaciones(Collection<VistaPedidoDTO> colVistaPedidoDTO){
		//verifica si tien autorizaciones de stok o descuento variable
		  for(VistaPedidoDTO vistaPedidoDTO : (Collection<VistaPedidoDTO>)colVistaPedidoDTO){
	          Collection<VistaDetallePedidoDTO> colVistaDetallePedidoDTO = vistaPedidoDTO.getVistaDetallesPedidosReporte();              		
	          //se itera la vistaDetallePedido para crear un DetallePedidoDTO
	  		for (VistaDetallePedidoDTO vistaDetallePedidoDTO : (ArrayList<VistaDetallePedidoDTO>)colVistaDetallePedidoDTO){            			
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
	  			
	  			//Se consulta si tiene autorizaciones            			
	  			DetalleEstadoPedidoAutorizacionDTO detalleAutorizaciones = new DetalleEstadoPedidoAutorizacionDTO();
	  			EstadoPedidoAutorizacionDTO estadoPedidoAutorizacionDTO = new EstadoPedidoAutorizacionDTO();
	  			estadoPedidoAutorizacionDTO.setAutorizacionDTO(new AutorizacionDTO());
	  			detalleAutorizaciones.setEstadoPedidoAutorizacionDTO(estadoPedidoAutorizacionDTO);
	  			detalleAutorizaciones.getId().setCodigoCompania(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoCompania());
	  			detalleAutorizaciones.getId().setCodigoAreaTrabajo(detallePedidoDTO.getEstadoDetallePedidoDTO().getId().getCodigoAreaTrabajo());
	  			detalleAutorizaciones.getId().setCodigoPedido(vistaDetallePedidoDTO.getId().getCodigoPedido());
	  			detalleAutorizaciones.getId().setSecuencialEstadoPedido(vistaDetallePedidoDTO.getId().getSecuencialEstadoPedido());
	  			detalleAutorizaciones.getId().setCodigoArticulo(vistaDetallePedidoDTO.getId().getCodigoArticulo());
	  			detalleAutorizaciones.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
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
	  						}
	  					}
	  				}
	  				LogSISPE.getLog().info("Tiene {} autorizacion(es) el articulo {}", autorizacionesCol.size(),vistaDetallePedidoDTO.getId().getCodigoArticulo());
	  				detallePedidoDTO.getEstadoDetallePedidoDTO().setDetalleEstadoPedidoAutorizacionCol(autorizacionesCol);
	  			}
	  			vistaDetallePedidoDTO.setEstadoDetallePedidoDTO(detallePedidoDTO.getEstadoDetallePedidoDTO());
	  			//verifica si tiene autorizaciones de stock
	  			Boolean autStock = false;
	  			if(vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol() != null){
						if(autStock){
							break;
						}
						for(DetalleEstadoPedidoAutorizacionDTO detalleEstadoPedidoAutorizacionDTO : vistaDetallePedidoDTO.getEstadoDetallePedidoDTO().getDetalleEstadoPedidoAutorizacionCol()){
							if(detalleEstadoPedidoAutorizacionDTO.getEstadoPedidoAutorizacionDTO().getNpTipoAutorizacion().equals(Long.valueOf(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock")))){
								vistaPedidoDTO.setTieneAutorizacionStock("SI");
	      					autStock = true;
	      					break;
							}
						}
					}
	  			//verifica si tiene autorizaciones de descuento variable
	  			if(StringUtils.isNotEmpty(vistaPedidoDTO.getTieneAutorizacionDctVar())){
					  if(!vistaPedidoDTO.getTieneAutorizacionDctVar().equals("SI")){
						  if(Integer.parseInt(vistaPedidoDTO.getTieneAutorizacionDctVar()) > 0){
							vistaPedidoDTO.setTieneAutorizacionDctVar("SI");
						  }else{
							vistaPedidoDTO.setTieneAutorizacionDctVar(null);
						  }
					  }
	  			}
	  		}	
		  }     
	  }
  
  	public static Integer asignarPaginacionConsulta(HttpSession session, VistaPedidoDTO consultaPedidoDTO, int start, int range) throws SISPEException, Exception{
  		
  	  	//metodo que trae el total de registros
  		Integer total = SessionManagerSISPE.getServicioClienteServicio().transObtenerTotalVistaPedido(consultaPedidoDTO);
    	session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, total);
    	LogSISPE.getLog().info("total de registros: {}",total);
    	
    	//se asignan los par\u00E1metros para la paginaci\u00F3n en la base de datos
    	consultaPedidoDTO.setNpFirstResult(start);
    	consultaPedidoDTO.setNpMaxResult(range);
    	consultaPedidoDTO.setNpObtenerHistoricoModReservas(Boolean.FALSE);
    	
    	return total;
  	}
  
}
