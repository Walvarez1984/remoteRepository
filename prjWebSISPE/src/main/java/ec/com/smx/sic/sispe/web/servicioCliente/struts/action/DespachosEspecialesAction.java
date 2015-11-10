/*
 * Clase DespachosAction.java 
 * Creado 13/06/06
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.smx.corporativo.admparamgeneral.dto.LocalDTO;
import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.ContactoPersonaLocalizacionRelacionadoDTO;
import ec.com.smx.corpv2.dto.DatoContactoPersonaLocalizacionDTO;
import ec.com.smx.corpv2.dto.EmpresaDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.DespachosEntregasForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author 	walvarez
 * @version 8.0
 */
public class DespachosEspecialesAction extends BaseAction{
	
	private static final String OBJ_BUSQUEDA = "ec.com.smx.sic.sispe.pedidos.busqueda";
	private static final String INDICE_PAGINA = "ec.com.smx.sic.sispe.indice.pagina";
	private static final String INDICE_START = "ec.com.smx.sic.sispe.indice.start";
	private static final String RESERVAS_POR_DESPACHAR = "ec.com.smx.sic.sispe.reservasPorDespachar";
	private static final String RPT_DESPACHADO = "ec.com.smx.sic.sispe.reporte.despachado";
	public static final  String SEPARADOR_COMA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
	public static final String CONSULTAR_TODOS_DESPACHOS_PENDIENTES = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("consultar.todos.pendientes.despacho");
	//coleccion de vista pedidos de las reservas pendiente por despachar especial
	public static final String IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL = SessionManagerSISPE.PREFIJO_VARIABLE_SESION.concat("imprimir.reservas");
	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro
   * componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige
   * el control(si la respuesta se ha completado <code>null</code>)
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacute;n que estamos procesando
   * @param response		La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    //objetos para los mensajes
    ActionMessages success = new ActionMessages();
    ActionMessages infos = new ActionMessages();
    ActionMessages errors = new ActionMessages();
    //esta variable permite que se muestre o no una ventana con los datos que van a imprimirse para el 
    //reporte en la pesta\u00F1a de despachos
    //[1: se muestra, 0: no se muestra]  
    request.getSession().setAttribute("ec.com.smx.sic.sispe.Despacho.imprimir","0");
    
    
    HttpSession session = request.getSession();
    DespachosEntregasForm formulario = (DespachosEntregasForm) form;
    //se obtienen los estados activos e inactivos de sesi\u00F3n
    String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
	//se obtiene el bean que contiene los campos genericos
	BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
//	boolean generarXSL = false;
	String ayuda= request.getParameter(Globals.AYUDA);
//	if(ayuda!=null && !ayuda.equals("")){
//		if(ayuda.equals("xls"))
//			generarXSL = true;
//	}
	LogSISPE.getLog().info("ayuda: {}",ayuda);	
    String salida = "desplegar";
    try
    {
      /*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
      if(request.getParameter("range")!=null || request.getParameter("start")!=null) {
        LogSISPE.getLog().info("ENTRO A LA PAGINACION");
        String tab = (String)session.getAttribute("ec.com.smx.sic.sispe.seccionPagina");
        Collection datos = new ArrayList();
        if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos")))
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
        else{
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
          beanSession.setInicioPaginacion(request.getParameter("start"));
        }
        
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
			
			asignarDiasDespacho(colVistaPedidoDTO);
			
			if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"))){
	    		//se guarda el listado de pedidos despachados
	    		session.setAttribute("ec.com.smx.sic.sispe.pedidosDespachados",colVistaPedidoDTO);
	    		//subo a sesi\u00F3n el objeto con los par\u00E1metros de b\u00FAsqueda
	    		session.setAttribute(OBJ_BUSQUEDA, consultaPedidoDTO);
	    	
	    	}else{
	    		//se guarda el listado de pedidos despachados
	    		session.setAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar",colVistaPedidoDTO);
	    	}
			
			//se asignan las variables de paginaci\u00F3n
			formulario.setStart(start);
			formulario.setRange(request.getParameter("range"));
			formulario.setSize(String.valueOf(tamano));
			formulario.setDatos(colVistaPedidoDTO);
			
		    //se guarda en sesi\u00F3n el indice de la paginaci\u00F3n
		    session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
		}
				
//        if(datos!=null && !datos.isEmpty()){
//          int size =  session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS) != null ? Integer.parseInt(session.getAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS).toString()) : 0;
//          int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
//          int start = Integer.parseInt(request.getParameter("start"));
//          
//          int indice_pagina= (Integer)session.getAttribute(INDICE_PAGINA);
//          session.setAttribute(INDICE_START,start);
//          //permite seleccionar varios pedidos en varias paginas
//          selecPedidosPaginados(session, formulario, range, start, indice_pagina);
//          
//          formulario.setStart(String.valueOf(start));
//          formulario.setRange(String.valueOf(range));
//          formulario.setSize(String.valueOf(size));
//          
//          session.setAttribute(INDICE_PAGINA,start/range);
//          Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
//          formulario.setDatos(datosSub);
//          //session.setAttribute(COL_DATOS_REPORTE, datosSub);
//      
//        }
      }
      /*-------------------- si se escogi\u00F3 el link para ver el detalle de  alg\u00FAn pedido ---------------------*/
      else if (request.getParameter("indicePedido")!= null)
      {
        int indice=0;
        try{
          indice = Integer.parseInt(request.getParameter("indicePedido"));
          //obtengo de la sesion la colecci\u00F3n de los pedidos
          ArrayList pedido = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
          VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedido.get(indice);
          //se recuperan los checkboxs escogidos anteriormente y se los asigna al formulario
          if(vistaPedidoDTO.getNpEstadoVistaPedido()!=null){
            formulario.setOpSeleccionAlgunos(vistaPedidoDTO.getNpSeleccionados());
          	if(vistaPedidoDTO.getNpEstadoVistaPedido().equals(estadoActivo)){
          		formulario.setOpSeleccionTodos(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
          	}
          }
          
          //se almacena el indice del pedido seleccionado
          beanSession.setIndiceRegistro(request.getParameter("indicePedido"));
         
          Collection detallePedido = vistaPedidoDTO.getVistaDetallesPedidos();
          if(detallePedido==null){
          	//creaci\u00F3n del DTO de consulta
          	VistaDetallePedidoDTO consultaDetallePedidoDTO = new VistaDetallePedidoDTO();
          	//asignaci\u00F3n de los par\u00E1metros de consulta
          	consultaDetallePedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
          	consultaDetallePedidoDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
          	consultaDetallePedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
          	consultaDetallePedidoDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
          	consultaDetallePedidoDTO.getId().setSecuencialEstadoPedido(vistaPedidoDTO.getId().getSecuencialEstadoPedido());
          	consultaDetallePedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
          	if(beanSession.getDescripcionArticulo() != null){
          		consultaDetallePedidoDTO.setDescripcionArticulo(beanSession.getDescripcionArticulo());
          	}else if(beanSession.getCodigoClasificacion() != null){
          		consultaDetallePedidoDTO.setCodigoClasificacion(beanSession.getCodigoClasificacion());
          	}else if(beanSession.getCodigoArticulo() != null){
          		consultaDetallePedidoDTO.getId().setCodigoArticulo(beanSession.getCodigoArticulo());
          	}
          	consultaDetallePedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
          	consultaDetallePedidoDTO.setArticuloDTO(new ArticuloDTO());
          	//setear valor TRUE para traer los detalles del canasto especial
          	consultaDetallePedidoDTO.setNpObtenerDetalleCanastaEspecial(Boolean.TRUE);
          	//busqueda de los detalles
          	detallePedido = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaDetallePedido(consultaDetallePedidoDTO);
          }
          vistaPedidoDTO.setVistaDetallesPedidos(detallePedido);
          //se almacena el objeto vistaPedidoDTO en la sesi\u00F3n.
          session.setAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado",vistaPedidoDTO);
          session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
        }catch(NumberFormatException ex){
          //si el formato del indice es incorrecto
          errors.add("formatoIndice",new ActionMessage("errors.indiceDetalle.formato"));
        }catch(IndexOutOfBoundsException ex){
          //si el indice no se puede referenciar
          errors.add("numeroRegistro",new ActionMessage("errors.indiceDetalle.fueraDeRango"));
        }
        
        salida = "desplegar";
      }
      /*-- Control de Tabs--*/
      else if (beanSession.getPaginaTab()!= null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {
      	/*------------------------- si se escogi\u00F3 el tab de las reservas sin despachar -------------------------------*/
      	if (beanSession.getPaginaTab().esTabSeleccionado(0)) {    
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		LogSISPE.getLog().info("reservas por despachar");
      		formulario.setDatos(null);
      		formulario.setOpSeleccionTodos(null);
      		formulario.setOpSeleccionAlgunos(null);

      		//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos por despachar
      		Collection pedidosPorDespachar = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
      		if(pedidosPorDespachar!=null && !pedidosPorDespachar.isEmpty()){
      			LogSISPE.getLog().info("ENTRO A LA PAGINACION");
      			int size= pedidosPorDespachar.size();
      			int start= 0;
      			int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
      			formulario.setStart(String.valueOf(start));
      			formulario.setRange(String.valueOf(range));
      			formulario.setSize(String.valueOf(size));

      			Collection datosSub = Util.obtenerSubCollection(pedidosPorDespachar,start, start + range > size ? size : start+range);
      			formulario.setDatos(datosSub);
      		}

      		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
      	}
      	//----- si se escogi\u00F3 el tab de las reservas despachadas ----------
      	else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
      		session.removeAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL);
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
      		LogSISPE.getLog().info("reservas despachadas");
      		formulario.setDatos(null);
      		formulario.setOpSeleccionTodos(null);
      		formulario.setOpSeleccionAlgunos(null);

      		//colecci\u00F3n que almacenar\u00E1 la lista de pedidos
      		Collection pedidosDespachados= (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		//verifica la existencia de pedidos en estado producido
      		if (pedidosDespachados != null && !pedidosDespachados.isEmpty()) 
      		{
      			LogSISPE.getLog().info("ENTRO A LA PAGINACION");
      			int size= pedidosDespachados.size();
      			int start= 0;
      			if(beanSession.getInicioPaginacion()!=null)
      				start = Integer.parseInt(beanSession.getInicioPaginacion());
      			int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
      			formulario.setStart(String.valueOf(start));
      			formulario.setRange(String.valueOf(range));
      			formulario.setSize(String.valueOf(size));

      			Collection datosSub = Util.obtenerSubCollection(pedidosDespachados,start, start + range > size ? size : start+range);
      			formulario.setDatos(datosSub);

      		}
      		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"));
      	}
      }
      /*---------------------------- si presiona el bot\u00F3n buscar -----------------------------------*/
      else if(request.getParameter("buscar")!=null)
      {
        //Los despachos especiales siempre por defecto la entidad responsable es la BODEGA BOD
  		String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
  		formulario.setOpEntidadResponsable(entidadBodega);
    	//se llama al m\u00E9todo que realiza la b\u00FAsqueda
    	//----- si se escogi\u00F3 el tab de las reservas por despachar ----------
        if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
        	session.removeAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL);
        	this.buscar(formulario, session, request, beanSession,Boolean.FALSE, infos, errors);
        }//----- si se escogi\u00F3 el tab de las reservas despachadas ----------
      	else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
      		session.removeAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL);
      		this.buscar(formulario, session, request, beanSession,Boolean.TRUE, infos, errors);
      	}
      }
      /*---------------------------- si presiona el bot\u00F3n actualizar -----------------------------------*/
      else if(request.getParameter("actualizarDetalle")!=null)
      {
    	//se llama al m\u00E9todo que realiza la b\u00FAsqueda
    	//----- si se escogi\u00F3 el tab de las reservas por despachar ----------
        if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
        	//se consultan todos los pedidos pendientes
        	request.setAttribute(CONSULTAR_TODOS_DESPACHOS_PENDIENTES, "ok");
        	String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
    		formulario.setOpEntidadResponsable(entidadBodega);
        	this.buscar(formulario, session, request, beanSession,Boolean.FALSE, infos, errors);
        }//----- si se escogi\u00F3 el tab de las reservas despachadas ----------
      	else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
      		this.buscar(formulario, session, request, beanSession,Boolean.TRUE, infos, errors);
      	}
      } else if(formulario.getBotonImprimir2()!=null){
    	  LogSISPE.getLog().info("entrar a aceptarReporte");
    	  //tomo los pedidos de la b\u00FAsqueda
			 Collection<VistaPedidoDTO> pedidosDespachados = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
			 //si hay resultados
			 if(pedidosDespachados!=null && !pedidosDespachados.isEmpty()){
				 VistaReporteGeneralDTO vistaReporteGeneralDTO = new VistaReporteGeneralDTO();
				 	//tomo de sesi\u00F3n el objeto con los par\u00E1metros de b\u00FAsqueda
				 	VistaPedidoDTO vistaPedidoDTO=(VistaPedidoDTO)session.getAttribute(OBJ_BUSQUEDA);
				 	//codigo de la compania
					vistaReporteGeneralDTO.getId().setCodigoCompania(vistaPedidoDTO.getId().getCodigoCompania());
					//codigo del local actual
					vistaReporteGeneralDTO.getId().setCodigoAreaTrabajo(vistaPedidoDTO.getId().getCodigoAreaTrabajo());
					//codigo estado
					vistaReporteGeneralDTO.getId().setCodigoEstado(vistaPedidoDTO.getId().getCodigoEstado());
					
					//filtros de art\u00EDculos
					if(vistaPedidoDTO.getArticuloDTO() != null){
						LogSISPE.getLog().info("codigo articulo: {}",vistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
						vistaReporteGeneralDTO.getId().setCodigoArticulo(vistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
						vistaReporteGeneralDTO.setCodigoClasificacion(vistaPedidoDTO.getArticuloDTO().getCodigoClasificacion());
						vistaReporteGeneralDTO.setDescripcionArticulo(vistaPedidoDTO.getArticuloDTO().getDescripcionArticulo());
					}
					vistaReporteGeneralDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
					vistaReporteGeneralDTO.setCedulaContacto(vistaPedidoDTO.getCedulaContacto());
					vistaReporteGeneralDTO.setNombreContacto(vistaPedidoDTO.getNombreContacto());
					vistaReporteGeneralDTO.setRucEmpresa(vistaPedidoDTO.getRucEmpresa());
					vistaReporteGeneralDTO.setNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
					vistaReporteGeneralDTO.setFechaDespachoDesde(vistaPedidoDTO.getNpPrimeraFechaDespachoInicial());
					vistaReporteGeneralDTO.setFechaDespachoHasta(vistaPedidoDTO.getNpPrimeraFechaDespachoFinal());
					vistaReporteGeneralDTO.setNpCantidadDespacho(1L);
					//solo obtener articulos del tipo canastos especiales
					vistaReporteGeneralDTO.setCodigoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.codigoClasificacionCanastasElaboradas"));
					
					StringBuilder codigosPedidos = new StringBuilder("");
					int cont=1;
					LogSISPE.getLog().info("tamano colecci\u00F3n: {}",pedidosDespachados.size());
					for(VistaPedidoDTO vistaPedidoDTO2 : pedidosDespachados){
						if(cont < pedidosDespachados.size()){
							codigosPedidos.append(vistaPedidoDTO2.getId().getCodigoPedido()).append(",");
						}else{
							codigosPedidos.append(vistaPedidoDTO2.getId().getCodigoPedido());
						}
						cont++;
					}
					LogSISPE.getLog().info("codigos resultantes: {}",codigosPedidos);
					//c\u00F3digos de los pedidos
					vistaReporteGeneralDTO.getId().setCodigoPedido(codigosPedidos.toString());
					vistaReporteGeneralDTO.setEstadoActual(true);	
					//tomo el tipo de reporte que quiero generar
					vistaReporteGeneralDTO.setTipoReporte(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.reportes.tipoReporte.despachoBodegaGeneral"));
					//llamo al m\u00E9todo que devuelve la colecci\u00F3n
					ArrayList<VistaReporteGeneralDTO> vistaReporteGeneralDTOcol = (ArrayList<VistaReporteGeneralDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaReporteGeneral(vistaReporteGeneralDTO);

					//se recorre todos los datos encontrados para ir buscando y asignado los datos del contacto principal de la persona o empresa
					VistaReporteGeneralDTO auxVistaReporteGeneral1;
					ArrayList<VistaReporteGeneralDTO> auxDetalles;
					if(!vistaReporteGeneralDTOcol.isEmpty()){						
						for(int i=0; i<vistaReporteGeneralDTOcol.size(); i++){
							auxVistaReporteGeneral1=vistaReporteGeneralDTOcol.get(i);
							auxDetalles=(ArrayList<VistaReporteGeneralDTO>)auxVistaReporteGeneral1.getDetallesReporte();
							
							for(int j=0; j<auxDetalles.size(); j++){
								cargarDatosPersonaEmpresa(request, auxDetalles.get(j));		
							}
						}
					}
					
					//subo a sesi\u00F3n la colecci\u00F3n
					session.setAttribute(RPT_DESPACHADO, vistaReporteGeneralDTOcol);
					//esta variable permite que se muestre o no una ventana con los datos que van a imprimirse para el reporte
				    //[1: se muestra, 0: no se muestra]
				    session.setAttribute("ec.com.smx.sic.sispe.Despacho.imprimir","1");
			 	}
      }
      else if(request.getParameter("mostrarVentana2")!=null){
        salida = "rptDespacho";
      }
      /*-------------------------------------- si se escogi\u00F3 el boton Despachar -----------------------------*/
      else if (formulario.getBotonDespachar() != null){
        ArrayList pedidosPorDespachar = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
        
        //verificamos si existen pedidos para despachar y si existe por lo menos uno para despachar 
        int size = (CollectionUtils.isNotEmpty(pedidosPorDespachar)) ? pedidosPorDespachar.size() : 0;
        int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        int start = session.getAttribute(INDICE_START)!=null?(Integer)session.getAttribute(INDICE_START):0;
          
        int indicePagina= session.getAttribute(INDICE_START)!=null?(Integer)session.getAttribute(INDICE_START):0;  
        selecPedidosPaginados(session,formulario,range,start,indicePagina);

        HashMap<Integer, String[]> reservasSelec= (HashMap<Integer, String[]>)session.getAttribute(RESERVAS_POR_DESPACHAR);
        
        if(reservasSelec!=null && !reservasSelec.isEmpty()){
          try{
        	//obtiene par\u00E1metro Activacion de verificaci\u00F3n de mails.
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.validacionEnvioMailsPosiblesFuncionariosResponsables", request);
			Integer parametroActivacion = Integer.parseInt(parametroDTO.getValorParametro());
			boolean mostrarEnvioMail = false;
			String accionMail = "despachos";
			LogSISPE.getLog().info("Valor Parametro de Activacion->{}",parametroActivacion);
			//obtener indices para tomar las reservas a despachar
			//HashMap<Integer, String[]> indicesPedidos=(HashMap<Integer, String[]>)reservasSelec.values();
			Collection<VistaPedidoDTO> visPedColSel= new ArrayList<VistaPedidoDTO>();
        	//se iteran los pedidos para obtener los descuentos de cada uno
			for(Integer key : reservasSelec.keySet()){
				if(pedidosPorDespachar!=null && !pedidosPorDespachar.isEmpty()){
			          start = key*range;
			          Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(pedidosPorDespachar,start, start + range > size ? size : start+range);
			          //obtener pedidos seleccionados
			          for(String clave:reservasSelec.get(key)){
			        	  VistaPedidoDTO visPedDTO=(VistaPedidoDTO)CollectionUtils.get(datosSub,Integer.valueOf(clave));
			        	  visPedDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			        	  visPedColSel.add(visPedDTO);
			          }
			    }
            }
            
			LogSISPE.getLog().info("pedidos seleccionados: "+visPedColSel.size());
            SISPEFactory.obtenerServicioSispe().transProcesarDespachoReservas(visPedColSel);
			
            //SessionManagerSISPE.getServicioClienteServicio().transProcesarDespacho(pedidosPorDespachar,indicesPedidos);
            
            //SE MUESTRA EL MENSAJE DE EXITO
            success.add("exitoRegistro",new ActionMessage("message.exito.registro","El despacho"));
            
            //por defecto se consultan todos los pedidos pendientes
      		request.setAttribute(CONSULTAR_TODOS_DESPACHOS_PENDIENTES, "ok");
      		String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
    		formulario.setOpEntidadResponsable(entidadBodega);
      		
          	//se llama al m\u00E9todo que realiza la b\u00FAsqueda
          	this.buscar(formulario, session, request, beanSession,Boolean.FALSE, null, null);
            
            //se elimina la lista de pedidos despachados de sesi\u00F3n
            session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
            
          }catch(SISPEException ex){
            //el servicio Obtener datos ha fallado
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
            errors.add("despacho", new ActionMessage("errors.llamadaServicio.registrarDatos","el Despacho"));
            errors.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
          }

        }else{
          errors.add("indicesVacios",new ActionMessage("errors.seleccion.pedidos.despacho"));
        }

        salida = "desplegar";
      } 
      //IOnofre. Si escoge imprimir el reporte de reservas por despachar
      else if(request.getParameter("botonImprimiReservasPorDespachar") != null){
    	  session.removeAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL);
    	  LogSISPE.getLog().info("Ingresa a la opcion de imprimir reporte de reservas por despachar");

    	  Collection<VistaPedidoDTO> vistaPedidoDTOCol = (Collection<VistaPedidoDTO>) session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");

    	  List<VistaPedidoDTO> reservasImprimirCol = (List<VistaPedidoDTO>) session.getAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL);

    	  if(CollectionUtils.isEmpty(reservasImprimirCol)){
    		  reservasImprimirCol = new ArrayList<VistaPedidoDTO>();
    	  }

    	  //verificamos si existen pedidos para despachar y si existe por lo menos uno para despachar 
    	  int size = (CollectionUtils.isNotEmpty(vistaPedidoDTOCol)) ? vistaPedidoDTOCol.size() : 0;
    	  int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
    	  int start = session.getAttribute(INDICE_START)!=null?(Integer)session.getAttribute(INDICE_START):0;

    	  int indicePagina= session.getAttribute(INDICE_START)!=null?(Integer)session.getAttribute(INDICE_START):0;  
    	  selecPedidosPaginados(session,formulario,range,start,indicePagina);

    	  HashMap<Integer, String[]> reservasSelec= (HashMap<Integer, String[]>)session.getAttribute(RESERVAS_POR_DESPACHAR);
    	  if(reservasSelec!=null && !reservasSelec.isEmpty()){         	    			
    		  //se iteran los pedidos para obtener los pedidos escogidos
    		  for(Integer key : reservasSelec.keySet()){
    			  if(vistaPedidoDTOCol!=null && !vistaPedidoDTOCol.isEmpty()){
    				  start = key*range;
    				  Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(vistaPedidoDTOCol,start, start + range > size ? size : start+range);
    				  //obtener pedidos seleccionados
    				  for(String clave:reservasSelec.get(key)){
    					  VistaPedidoDTO visPedDTO=(VistaPedidoDTO)CollectionUtils.get(datosSub,Integer.valueOf(clave));
    					  visPedDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());

    					  //se consultan los detalles del pedido
    					  visPedDTO.setVistaDetallesPedidosReporte(null);
    					  visPedDTO.setValorTotalBrutoSinIva(null);
    					  visPedDTO.setVistaDetallesPedidos(null);
    					  visPedDTO.setNpLocalDTO(new LocalDTO());
    					  visPedDTO.setNpNombreUsuario("");
    					  EstadoPedidoUtil.obtenerDetallesPedido(visPedDTO, request);
    					  //Ionofre
    					  if(visPedDTO != null && visPedDTO.getVistaDetallesPedidosReporte() != null){
    						  //se cargan los detalles de las recetas en caso de que no se hayan consultado todav\u00EDa
    						  for(Iterator<VistaDetallePedidoDTO> it = visPedDTO.getVistaDetallesPedidosReporte().iterator(); it.hasNext();){
    							  VistaDetallePedidoDTO vistaDetallePedidoDTO = it.next();
    							  //si es una receta
    							  if(vistaDetallePedidoDTO.getCodigoClasificacion()!=null && 
    									  vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canasta")) || 
    									  vistaDetallePedidoDTO.getCodigoClasificacion().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial"))){

    								  //se carga la receta del art\u00EDculo
    								  Collection recetaArticulo = EstadoPedidoUtil.obtenerDetalleReceta(vistaDetallePedidoDTO);
    								  vistaDetallePedidoDTO.getArticuloDTO().setArticuloRelacionCol(recetaArticulo);
    							  }
    						  }
    					  }
    					  reservasImprimirCol.add(visPedDTO);
    				  }
    			  }
    		  }
    		  LogSISPE.getLog().info("pedidos seleccionados: "+reservasImprimirCol.size());
    		  session.setAttribute(IMPRIMIR_RESERVAS_POR_DESPACHAR_ESPECIAL, reservasImprimirCol);        
    	  }
    	  else{    		  
    		  errors.add("indicesVacios", new ActionMessage("errors.seleccion.reservas.por.despachar"));
    	  }
      }
      
      /*---------------------------------Muestra los pedidos producidos por omisi\u00F3n---------------------*/
      else 
      {
      	//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
      	SessionManagerSISPE.removeVarSession(request);

      	//se verifica que la entidad responsable no sea un local
      	String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
      	if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal))
      		//se obtienen los locales por ciudad
      		SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);

      	//se blanquea la colecci\u00F3n del formulario que contiene la paginaci\u00F3n
      	formulario.setDatos(null);
      	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
      	//Obtengo valor por defecto entidad Responsable.
		String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
		formulario.setOpEntidadResponsable(entidadBodega);
      	
      	//variable de sesi\u00F3n que almacenar\u00E1 los indices de los pedidos que tienen alguna entrega asignada
      	session.setAttribute("ec.com.smx.sic.sispe.despacho.indicesPedidos",new ArrayList());
      	session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
      	session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Despachos especiales");
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagadoLiquidado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"));
      	session.setAttribute("ec.com.smx.sic.sispe.validacion.pagoTotal.despacho", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"));
      	
      	//se guarda la acci\u00F3n en la sesi\u00F3n
      	session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despachoEspecial"));
      	formulario.setEtiquetaFechas("Fecha de despacho");

      	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de despacho");

      	//Iniciar Tabs
      	PaginaTab tabsDespachos = new PaginaTab("despachoEspecial", "desplegar", 0, 455, request);
      	Tab tabPedidosPorDespachar = new Tab("Reservas por despachar", "despachoEspecial", "/servicioCliente/despachoEspecial/reservasPorDespachar.jsp", true);
      	Tab tabPedidosDespachados = new Tab("Reservas despachadas", "despachoEspecial", "/servicioCliente/despachoEspecial/reservasDespachados.jsp", false);
      	tabsDespachos.addTab(tabPedidosPorDespachar);
      	tabsDespachos.addTab(tabPedidosDespachados);

      	beanSession.setPaginaTab(tabsDespachos);
      	
      	//por defecto se consultan todos los pedidos pendientes
  		request.setAttribute(CONSULTAR_TODOS_DESPACHOS_PENDIENTES, "ok");
  		this.buscar(formulario, session, request, beanSession,Boolean.FALSE, infos, errors);
      	
      	salida = "desplegar";
      }
    }catch(Exception ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    
    //se almacenan los mensajes
    saveMessages(request, success);
    saveInfos(request, infos);
    saveErrors(request, errors);
//    
	//se guarda el beanSession
	SessionManagerSISPE.setBeanSession(beanSession, request);
//	if(errors.size()>0){
//		this.buscar(formulario, session, request, beanSession, infos, errors);
//	}
    return mapping.findForward(salida);
  }


@SuppressWarnings("unchecked")
private void selecPedidosPaginados(HttpSession session, DespachosEntregasForm formulario, int range, int start, int indPag) {
	  HashMap<Integer, String[]> reservasSelec= (HashMap<Integer, String[]>)session.getAttribute(RESERVAS_POR_DESPACHAR);
	  int indicePagina = indPag; 
	  
	  if(reservasSelec==null){
		  reservasSelec= new HashMap<Integer, String[]>();
		  if(formulario.getOpEscogerProdBuscados()!=null){
			  reservasSelec.put(indicePagina,(String[])SerializationUtils.clone(formulario.getOpEscogerProdBuscados()));
	      	  session.setAttribute(RESERVAS_POR_DESPACHAR, reservasSelec);
		  }
	      formulario.setOpEscogerProdBuscados(null);
	  }else{
		  if(reservasSelec.containsKey(indicePagina)){
			  if(formulario.getOpEscogerProdBuscados()==null){
				  reservasSelec.remove(indicePagina);
			  }else{
				  reservasSelec.put(indicePagina,(String[])SerializationUtils.clone(formulario.getOpEscogerProdBuscados()));
			  }
			  indicePagina = start/range;
			  formulario.setOpEscogerProdBuscados(reservasSelec.get(indicePagina));
		  }else{
			  if(formulario.getOpEscogerProdBuscados()!=null){
				  reservasSelec.put(indicePagina, (String[])SerializationUtils.clone(formulario.getOpEscogerProdBuscados()));
				  session.setAttribute(RESERVAS_POR_DESPACHAR, reservasSelec);
			  }
	          indicePagina = start/range;
	          if(reservasSelec.containsKey(indicePagina)){
	        	  if(formulario.getOpEscogerProdBuscados()==null){
					  reservasSelec.remove(indicePagina);
				  }else{
					  formulario.setOpEscogerProdBuscados(reservasSelec.get(indicePagina));
				  }
	    	  }else{
	    		  formulario.setOpEscogerProdBuscados(null);
	    	  }
		  }
	  }
}
 
  
  /**
   * Ejecuta la b\u00FAsqueda en el formulario
   * 
   * @param formulario
   * @param session
   * @param request
   * @param beanSession
   * @param infos
   * @param errors
   * @throws Exception
   */
  private void buscar(DespachosEntregasForm formulario, HttpSession session, HttpServletRequest request, BeanSession beanSession,Boolean obtenerPedidosCanastosEspecialesDespachados,
  		ActionMessages infos, ActionMessages errors) throws Exception{
    //se obtiene la lista de art\u00EDculos de sesi\u00F3n
    Collection<VistaPedidoDTO> pedidosEnDespachos = new ArrayList<VistaPedidoDTO>();
	
    VistaPedidoDTO consultaVistaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
    
    //se traen los detalles del pedido para saber cuantos canastos especiales existen
    consultaVistaPedidoDTO.setVistaDetallesPedidos(new ArrayList<VistaPedidoDTO>());

    //se verifica cual es la secci\u00F3n que est\u00E1 cargada
    String tab = (String)session.getAttribute("ec.com.smx.sic.sispe.seccionPagina");
    if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"))){
    	consultaVistaPedidoDTO.getId().setCodigoEstado(((String)session.getAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado")).concat(ConstantesGenerales.CARACTER_SEPARACION).
    			concat(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PRODUCIDO).concat(ConstantesGenerales.CARACTER_SEPARACION).
    			concat(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO).concat(ConstantesGenerales.CARACTER_SEPARACION));
    	consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
    	consultaVistaPedidoDTO.setEtapaEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.finalizado"));
    	consultaVistaPedidoDTO.setVistaDetallesPedidos(null);
    }
    //se asigna el usuario para el filtro
    consultaVistaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
    consultaVistaPedidoDTO.setNpObtenerPedidosCanastosEspeciales(Boolean.TRUE);
    consultaVistaPedidoDTO.setNpObtenerPedidosCanastosEspecialesDespachados(obtenerPedidosCanastosEspecialesDespachados);
    consultaVistaPedidoDTO.getId().setCodigoEstado(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_RESERVADO.concat(ConstantesGenerales.CARACTER_SEPARACION).concat(
			ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PRODUCIDO).concat(ConstantesGenerales.CARACTER_SEPARACION).concat(
					ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ENPRODUCCION));
    
    consultaVistaPedidoDTO.setCodigoEstadoPagado(ConstantesGenerales.CODIGO_ESTADO_PAGADO_PARCIAL.concat(ConstantesGenerales.CARACTER_SEPARACION).
    concat(ConstantesGenerales.CODIGO_ESTADO_PAGADO_TOTAL).concat(ConstantesGenerales.CARACTER_SEPARACION).
    concat(ConstantesGenerales.CODIGO_ESTADO_PAGADO_LIQUIDADO).concat(ConstantesGenerales.CARACTER_SEPARACION));
    
    String [][] camposOrden = new String [][]{{"primeraFechaDespacho",ConstantesGenerales.ORDEN_ASCENDENTE}};
    consultaVistaPedidoDTO.setNpCamposOrden(camposOrden);
    
	String estadoEtapaFinalizado = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.finalizado");
	String estadoEtapaEnProceso = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.enProceso");
	
	// Validar si solo desea los pedidos en estado (DESPACHADO-EN PROCESO) y (PRODUCIDO-FINALIDADO)
	// o solo los (DESPACHADOS-FINALIDAZO)
	// Esto es para la subconsulta de la pantalla principal de reportes de despachos
	if ((consultaVistaPedidoDTO.getId() != null) &&
			(consultaVistaPedidoDTO.getId().getCodigoEstado() != null) &&
			(consultaVistaPedidoDTO.getEstadoActual() != null) &&
			(consultaVistaPedidoDTO.getEtapaEstadoActual() != null) &&
			(consultaVistaPedidoDTO.getId().getCodigoEstado().equals(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO)) &&
			(consultaVistaPedidoDTO.getEstadoActual().equals(ConstantesGenerales.ESTADO_SI)) &&
			(consultaVistaPedidoDTO.getEtapaEstadoActual().equals(estadoEtapaFinalizado))){
		LogSISPE.getLog().info("(DESPACHADOS-FINALIDAZO)");
		consultaVistaPedidoDTO.getId().setCodigoEstado(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO);
		consultaVistaPedidoDTO.setEstadoActual(ConstantesGenerales.ESTADO_SI);
		consultaVistaPedidoDTO.setEtapaEstadoActual(estadoEtapaFinalizado);
		
	}else{
		LogSISPE.getLog().info("(DESPACHADO-EN PROCESO) y (PRODUCIDO)");
		// Primero debo obtener los pedidos en estado despachado, que est\u00E9n en estadoActual=SI y en la etapaEstadoActual=EN PROCESO
		consultaVistaPedidoDTO.getId().setCodigoEstado(ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO+SEPARADOR_COMA
				+consultaVistaPedidoDTO.getId().getCodigoEstado()+SEPARADOR_COMA+ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ENTREGADO);
		consultaVistaPedidoDTO.setEstadoActual(ConstantesGenerales.ESTADO_SI);
		consultaVistaPedidoDTO.setEtapaEstadoActual(estadoEtapaEnProceso+SEPARADOR_COMA+estadoEtapaFinalizado);
	}
	
    //se guardan las variables de b\u00FAsqueda para utilizarlas posteriormente
    beanSession.setCodigoLocal(consultaVistaPedidoDTO.getId().getCodigoAreaTrabajo());//se carga valor de codigo area de trabajo en codigo local
    beanSession.setCodigoPedido(consultaVistaPedidoDTO.getId().getCodigoPedido());
    beanSession.setCodigoReservacion(consultaVistaPedidoDTO.getLlaveContratoPOS());
    beanSession.setCedulaContacto(consultaVistaPedidoDTO.getNumeroDocumentoPersona());
    beanSession.setNombreContacto(consultaVistaPedidoDTO.getNombrePersona());
    beanSession.setNombreEmpresa(consultaVistaPedidoDTO.getNombreEmpresa());
    beanSession.setRucEmpresa(consultaVistaPedidoDTO.getRucEmpresa());
    beanSession.setOpEntidadResonsable(consultaVistaPedidoDTO.getEntidadResponsable());
    if(consultaVistaPedidoDTO.getArticuloDTO()!=null){
    	beanSession.setCodigoArticulo(consultaVistaPedidoDTO.getArticuloDTO().getId().getCodigoArticulo());
    	beanSession.setDescripcionArticulo(consultaVistaPedidoDTO.getArticuloDTO().getDescripcionArticulo());
    	beanSession.setCodigoClasificacion(consultaVistaPedidoDTO.getArticuloDTO().getCodigoClasificacion());
    }else{
    	beanSession.setCodigoArticulo(null);
    	beanSession.setDescripcionArticulo(null);
    	beanSession.setCodigoClasificacion(null);
    }
   
    //se quitan las fechas para que consulten todos los pedidos pendientes por despachar
    if(request.getAttribute(CONSULTAR_TODOS_DESPACHOS_PENDIENTES) != null){
    	
    	//a la fecha actual se suman los dias parametrizados como necesarios para producir canastos especiales
    	ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.diazARestarAFechaDespachoAvisoProduccion", request);
    	
    	if(parametroDTO != null && parametroDTO.getValorParametro() != null){
    	
    		int diasElaborarCanastos = Integer.parseInt(parametroDTO.getValorParametro());
    		Calendar calendar = Calendar.getInstance();
    		calendar.add(Calendar.DAY_OF_YEAR, diasElaborarCanastos);
        	consultaVistaPedidoDTO.setNpPrimeraFechaDespachoInicial(null);
        	Timestamp fechaFinal = new Timestamp(WebSISPEUtil.construirFechaCompleta(calendar.getTime(), 0, 0, 23, 59, 59, 999));
        	LogSISPE.getLog().info("se buscaran pedidos con fecha final despacho: "+fechaFinal);
        	consultaVistaPedidoDTO.setNpPrimeraFechaDespachoFinal(fechaFinal);
    	}
    }
    
  	//se guarda las fechas inicial y final de entrega para los pedidos
  	beanSession.setFechaInicial(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoInicial());
  	beanSession.setFechaFinal(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoFinal());
  	
    //se blanquea la colecci\u00F3n de datos antes de actualizar la lista
    formulario.setDatos(null);
    
    try{
    	
    	//metodo que trae el total de registros
    	Integer total = SessionManagerSISPE.getServicioClienteServicio().transObtenerTotalVistaPedido(consultaVistaPedidoDTO);
    	session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, total);
    	LogSISPE.getLog().info("total de registros: {}",total);
		
		//se asignan los par\u00E1metros para la paginaci\u00F3n en la base de datos
    	int start = 0;
    	int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
    	consultaVistaPedidoDTO.setNpFirstResult(start);
    	consultaVistaPedidoDTO.setNpMaxResult(range);
    	consultaVistaPedidoDTO.setNpObtenerHistoricoModReservas(Boolean.FALSE);
    	
    	
    	
    	//obtiene solamente los registros en el rango establecido
//    	consultaPedidoDTO.setVistaDetallesPedidos(new ArrayList<VistaDetallePedidoDTO>());
    	if(total != null && total > 0){
    		pedidosEnDespachos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
    	}else{
    		pedidosEnDespachos = new ArrayList<VistaPedidoDTO>();
    	}
    	
    	//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
//    	pedidosEnDespachos = (Collection<VistaPedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaDespachar(consultaVistaPedidoDTO);
    	asignarDiasDespacho(pedidosEnDespachos);
    	
    	if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"))){
    		//se guarda el listado de pedidos despachados
    		session.setAttribute("ec.com.smx.sic.sispe.pedidosDespachados",pedidosEnDespachos);
    		//subo a sesi\u00F3n el objeto con los par\u00E1metros de b\u00FAsqueda
    		session.setAttribute(OBJ_BUSQUEDA, consultaVistaPedidoDTO);
    	
    	}else{
    		//se guarda el listado de pedidos despachados
    		session.setAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar",pedidosEnDespachos);
    	}
    	
    	//se realiza la paginaci\u00F3n si la colecci\u00F3n tiene elementos
    	if(pedidosEnDespachos!=null && !pedidosEnDespachos.isEmpty()){
    		
	       LogSISPE.getLog().info("ENTRO A LA PAGINACION");
          
           formulario.setStart(String.valueOf(start));
           formulario.setRange(String.valueOf(range));
           formulario.setSize(String.valueOf(total));
           
           formulario.setDatos(pedidosEnDespachos);
           beanSession.setInicioPaginacion(String.valueOf(start));
           session.setAttribute(INDICE_PAGINA,start/range);
           session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaVistaPedidoDTO);
               
//    		LogSISPE.getLog().info("SE REALIZA LA PAGINACION");
//    		int size= pedidosEnDespachos.size();
//    		int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
//    		int start= 0;
//    		session.setAttribute(INDICE_PAGINA,start/range);
//    		formulario.setStart(String.valueOf(start));
//    		formulario.setRange(String.valueOf(range));
//    		formulario.setSize(String.valueOf(size));
//
//    		Collection datosSub = Util.obtenerSubCollection(pedidosEnDespachos,start, start + range > size ? size : start+range);
//    		formulario.setDatos(datosSub);
//    		//session.setAttribute(COL_DATOS_REPORTE, datosSub);
//    		beanSession.setInicioPaginacion(String.valueOf(start));

    	}else if(infos != null){
    		if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion")))
    			infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Reservas despachadas"));
    		else
    			infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Reservas por despachar"));
    	}
    	
    	//si la b\u00FAsqueda se realiza cuando esta el control en la secci\u00F3n del detalle
    	if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"))){
    		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
    	}
    	
    	//se inicializa la colecci\u00F3n de indices
    	session.setAttribute("ec.com.smx.sic.sispe.despacho.indicesPedidos", new ArrayList());
    	//se blanquean los checks
    	formulario.setOpSeleccionAlgunos(null);
    	
    }catch(SISPEException ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      if(errors != null)
      	errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
    }
  }
  
  
  /**
   * CALCULAR EL NUMERO DE D\u00EDAS PARA REALIZAR EL DESPACHO 
   * @param pedidosEnDespachos
   * @throws Exception
   */
  public static void asignarDiasDespacho(Collection<VistaPedidoDTO> pedidosEnDespachos) throws Exception{
	  
	  if(CollectionUtils.isNotEmpty(pedidosEnDespachos)){
		  
		  String clasificacionCanastaEspecial = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.clasificacionTipoArticulo.canastaEspecial");
		  
		  for(VistaPedidoDTO visPedDTO:pedidosEnDespachos){
			  
			  Date fechaDespacho = visPedDTO.getPrimeraFechaDespacho();
			  Date fechaActual = new Date();
			  
			  if(fechaDespacho != null){
				  Long fechaDiferencia = ConverterUtil.returnDateDiff(ConverterUtil.parseDateToString(fechaActual), ConverterUtil.parseDateToString(fechaDespacho));							
				  visPedDTO.setNpDiasParaDespachoCanastosEspeciales(fechaDiferencia.toString());
				  
				  int cantidadCanastos = 0;
				  //se suma el numero de castos especiales que tiene el pedido
				  if(CollectionUtils.isNotEmpty(visPedDTO.getVistaDetallesPedidos())){
					  
					  for(VistaDetallePedidoDTO vistaDetallePedidoDTO : (Collection<VistaDetallePedidoDTO>) visPedDTO.getVistaDetallesPedidos()){
						  if(vistaDetallePedidoDTO.getCodigoClasificacion() != null 
								  && vistaDetallePedidoDTO.getCodigoClasificacion().equals(clasificacionCanastaEspecial)){
							  cantidadCanastos += vistaDetallePedidoDTO.getCantidadEstado().intValue();
						  }
					  }
					  visPedDTO.setNpCantidadCanastosEspeciales(cantidadCanastos);
				  }
			  }
		  }
	  }
  }
  
  
  /**
   * 
   * @param request
   * @param vistaReporteGeneralDTO
   */
  public static void cargarDatosPersonaEmpresa(HttpServletRequest request, VistaReporteGeneralDTO vistaReporteGeneralDTO) {
	  try{
			//para el caso de personas
			if (vistaReporteGeneralDTO.getTipoDocumentoContacto() != null && !vistaReporteGeneralDTO.getTipoDocumentoContacto().equals("NA") ) {
				
				//se inicializan las variables de la empresa
				vistaReporteGeneralDTO.setRucEmpresa("NA");
				vistaReporteGeneralDTO.setNombreEmpresa(null);
				vistaReporteGeneralDTO.setTelefonoEmpresa(null);
				
				//se inicializan las variables del contacto
				vistaReporteGeneralDTO.setNombreContacto(null);
				vistaReporteGeneralDTO.setCedulaContacto(null);
				vistaReporteGeneralDTO.setTelefonoContacto(null);
							
				//consultar el contacto principal de la persona
				DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
				contactoPersonaLocalizacionDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
				contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
				contactoPersonaLocalizacionDTO.setPersonaContactoDTO(new PersonaDTO());
				contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				contactoPersonaLocalizacionDTO.setCriteriaSearch(new CriteriaSearch());
				
				ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
				contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
				contactoRelacionadoDTO.setCodigoPersona(Long.valueOf(Long.valueOf(vistaReporteGeneralDTO.getCodigoPersona())));
				contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
				
				Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactoPersonaLocalizacionCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);
				
				DatoContactoPersonaLocalizacionDTO contactoTipoPersona=null;
				if(CollectionUtils.isNotEmpty(contactoPersonaLocalizacionCol)){
					contactoRelacionadoDTO = (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactoPersonaLocalizacionCol, 0);
					contactoTipoPersona = contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO();
				}							
				
				if(contactoTipoPersona!=null){
					if(contactoTipoPersona.getPersonaContactoDTO()!=null ){
						//cargar la informacion del contacto
						vistaReporteGeneralDTO.setNombreContacto(contactoTipoPersona.getPersonaContactoDTO().getNombreCompleto());
						vistaReporteGeneralDTO.setCedulaContacto(contactoTipoPersona.getPersonaContactoDTO().getNumeroDocumento());					
						
						String telefonoContacto="TD: ";
						if(contactoTipoPersona.getNumeroTelefonicoPrincipal()!=null){
							telefonoContacto+=contactoTipoPersona.getNumeroTelefonicoPrincipal();										
						} else telefonoContacto+=" SN ";	
						if(contactoTipoPersona.getNumeroTelefonicoTrabajo()!=null){
							telefonoContacto+=" - TT: "+contactoTipoPersona.getNumeroTelefonicoTrabajo();
						}else telefonoContacto+=" - TT: SN ";	
						if(contactoTipoPersona.getNumeroTelefonicoCelular()!=null){
							telefonoContacto+=" - TC: "+contactoTipoPersona.getNumeroTelefonicoCelular();
						}
						else telefonoContacto+=" - TC: SN ";									
						vistaReporteGeneralDTO.setTelefonoContacto(telefonoContacto);	
					}
				}
			}
			else if (vistaReporteGeneralDTO.getRucEmpresa() != null && !vistaReporteGeneralDTO.getRucEmpresa().equals("NA")) {
				
				//se inicializan las variables del contacto
				vistaReporteGeneralDTO.setNombreContacto(null);
				vistaReporteGeneralDTO.setCedulaContacto(null);
				vistaReporteGeneralDTO.setTelefonoContacto(null);
				
				vistaReporteGeneralDTO.setNumeroDocumentoPersona(null);
				vistaReporteGeneralDTO.setNombrePersona(null);			
							
				//se cargan los datos del contacto principal de la empresa
				//Buscar si existe la empresa 			
				EmpresaDTO empresaDto = new EmpresaDTO();
				empresaDto.setNumeroRuc(vistaReporteGeneralDTO.getRucEmpresa());
				EmpresaDTO empresaEncontrada =  SISPEFactory.getDataService().findUnique(empresaDto);		
				
				if(empresaEncontrada !=null){		
					//Buscar la localizacion
					LocalizacionDTO localizacionDto = new LocalizacionDTO();
					localizacionDto.setCodigoEmpresa(empresaEncontrada.getId().getCodigoEmpresa());
					localizacionDto.setTipoLocalizacion(CorporativoConstantes.TIPOLOCALIZACION_MATRIZ);
					LocalizacionDTO localizacionEncontrada = SISPEFactory.getDataService().findUnique(localizacionDto);
					
					if(localizacionEncontrada !=null){
						
						//consultar el contacto principal de la empresa
						DatoContactoPersonaLocalizacionDTO contactoPersonaLocalizacionDTO= new DatoContactoPersonaLocalizacionDTO();
						contactoPersonaLocalizacionDTO.getId().setCodigoCompania(empresaEncontrada.getId().getCodigoCompania());
						contactoPersonaLocalizacionDTO.setCodigoSistema(SystemProvenance.SISPE.name());
						contactoPersonaLocalizacionDTO.setPersonaDTO(new PersonaDTO());
						contactoPersonaLocalizacionDTO.setCodigoTipoContacto(ConstantesGenerales.CODIGO_TIPO_CONTACTO_PRINCIPAL_PEDIDOS_ESPECIALES);
						contactoPersonaLocalizacionDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
	
						ContactoPersonaLocalizacionRelacionadoDTO contactoRelacionadoDTO = new ContactoPersonaLocalizacionRelacionadoDTO();
						contactoRelacionadoDTO.setCodigoLocalizacion(localizacionEncontrada.getId().getCodigoLocalizacion());
						contactoRelacionadoDTO.setDatoContactoPersonaLocalizacionDTO(contactoPersonaLocalizacionDTO);
						contactoRelacionadoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
						
						Collection<ContactoPersonaLocalizacionRelacionadoDTO> contactosRelacionadosCol = SISPEFactory.getDataService().findObjects(contactoRelacionadoDTO);					
						
						if(CollectionUtils.isNotEmpty(contactosRelacionadosCol)){
							contactoRelacionadoDTO =  (ContactoPersonaLocalizacionRelacionadoDTO)CollectionUtils.get(contactosRelacionadosCol, 0);
							localizacionEncontrada.setContactoPrincipalDTO((contactoRelacionadoDTO.getDatoContactoPersonaLocalizacionDTO()));
						}			
						
						if(localizacionEncontrada.getContactoPrincipalDTO()!=null){
							if(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO()!=null){
								//cargar la informacion del contacto							
								vistaReporteGeneralDTO.setCedulaContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNumeroDocumento());
								vistaReporteGeneralDTO.setNombreContacto(localizacionEncontrada.getContactoPrincipalDTO().getPersonaContactoDTO().getNombreCompleto());
								
								String telefonoContacto="TD: ";
								if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal()!=null){
									telefonoContacto+=localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoPrincipal();										
								} else telefonoContacto+=" SN ";									
								if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo()!=null){
									telefonoContacto+=" - TT: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoTrabajo();										
								} else telefonoContacto+=" - TT: SN ";
								if(localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular()!=null){
									telefonoContacto+=" - TC: "+localizacionEncontrada.getContactoPrincipalDTO().getNumeroTelefonicoCelular();
								}
								else telefonoContacto+=" - TC: SN ";									
								vistaReporteGeneralDTO.setTelefonoContacto(telefonoContacto);							
							}
						}				
					}
				}			
			}
		  }
		  catch (Exception e) {
			  LogSISPE.getLog().info("Error al cargar datos del contacto en "+vistaReporteGeneralDTO.getClass());
		}
	}
}