package ec.com.smx.sic.sispe.web.calendarioBodega.struts.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.id.ParametroID;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioArticuloDTO;
import ec.com.smx.sic.sispe.dto.CalendarioMotivoMovimientoDTO;
import ec.com.smx.sic.sispe.dto.VistaCalendarioArticuloDTO;
import ec.com.smx.sic.sispe.web.calendarioBodega.struts.form.LineaProduccionForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.CotizarReservarAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * @author jmena
 *
 */
public class LineaProduccionAction extends BaseAction{
	
	private final String COL_ARTICULOS = "ec.com.smx.sic.sispe.lineaProduccion.colArticulos";
	private final String CAL_ARTICULO_DTO = "ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO";
	private final String CAL_ARTICULO_DTO_HIJO = "ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloHijoDTO";
	
	private final String INDICE_SELECCION_PADRE = "indiceSeleccionPadre";
	private final String INDICE_SELECCION_HIJO = "indiceSeleccionHijo";
	private final String TIPO_AGRUPACION_ART_FEC = "ec.com.smx.sic.sispe.tipoAgrupacionArtFec";
	private final String TIPO_AGRUPACION_FEC_ART = "ec.com.smx.sic.sispe.tipoAgrupacionFecArt";
	private final String ARTICULO_DTO = "ec.com.smx.sic.sispe.articuloDTO";
	private final String DETALLE_CANASTA = "ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta";
	private final String ACCESO_USUARIO_LINEA_PRO = "ec.com.smx.sic.sispe.usuarioAccesoLineaPro";
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
   * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
   * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
   
   * @param mapping					El mapeo utilizado para seleccionar esta instancia
   * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          							campos
   * @param request					El request que estamos procesando
   * @param response 				La respuesta HTTP que se crea
   * @throws Exception
   * @return ActionForward	Describe donde y como se redirige el control*/
	
	@SuppressWarnings("unchecked") 
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		      HttpServletResponse response) throws Exception{
		 /************************Declaracion de variables**********************************/
		PropertyValidator validar = new PropertyValidatorImpl();  	
		ActionMessages errors = new ActionMessages();
		ActionErrors error = new ActionErrors();
		ActionMessages infos = new ActionMessages();
		  	
		    HttpSession session = request.getSession(); 
		    String forward = "desplegar";
		    LineaProduccionForm formulario = (LineaProduccionForm)form;
		    String ayuda = request.getParameter(Globals.AYUDA);
		    //se obtienen las claves que indican un estado activo y un estado inactivo
			String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
			String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
			//se guarda en sesi\u00F3n el c\u00F3digo del tipo de art\u00EDculo CANASTO
//			session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.canasta",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.canasta"));
//			session.setAttribute("ec.com.smx.sic.sispe.tipoArticulo.despensa",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
			
			//subClasificacion para articulos tipo canastos 
	  		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.canastos", request);
			String valorTipoArticuloCanastas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_CANASTA, valorTipoArticuloCanastas);
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_CANASTA, valorTipoArticuloCanastas);
			
			//subClasificacion para articulos tipo despensas 
	  		parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.tipoArticulo.despensas", request);
	  		String valorTipoArticuloDespensas= parametroDTO.getValorParametro();
			//session.setAttribute(CotizarReservarAction.TIPO_ART_RECETA_DESPENSA, MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoarticulo.despensa"));
			session.setAttribute(CotizarReservarAction.SUBCLASIFICACIONES_TIPO_ARTICULO_DESPENSA, valorTipoArticuloDespensas);
			
		    /*--------------------------------- cuando se busca Linea Produccion --------------------------------------*/
		    if(request.getParameter("buscar") != null){
				LogSISPE.getLog().info("Presiono el boton Buscar");
				//remuevo las variables session para el tipo de presentacion del reporte.
				session.removeAttribute(TIPO_AGRUPACION_ART_FEC);
				session.removeAttribute(TIPO_AGRUPACION_FEC_ART);
				session.removeAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA);
				Collection colCalendarioArticuloDTO = null;
		        //DTO que contiene los campos seteados
		        VistaCalendarioArticuloDTO consultaCalArtDTO = WebSISPEUtil.construirConsultaLineaProduccion(request, formulario);
		        
		        if(formulario.getCmbOpcionAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"))){
		        	LogSISPE.getLog().info("Opcion de Agrupacion articulo - fecha");
		        	consultaCalArtDTO.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"));
		        	session.setAttribute(TIPO_AGRUPACION_ART_FEC,"ok");
		        }else if(formulario.getCmbOpcionAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"))){
		        	LogSISPE.getLog().info("Opcion de Agrupacion fecha - articulo");
		        	consultaCalArtDTO.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"));
		        	session.setAttribute(TIPO_AGRUPACION_FEC_ART,"ok");
		        }
		        //Se guarda en session la plantilla de b\u00FAsqueda
				session.setAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA, consultaCalArtDTO);
		        
		        try{
		        	//metodo que obtiene la coleccion de CalendarioArticuloDTO
		        	colCalendarioArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(consultaCalArtDTO);
		        	//se realiza la paginaci\u00F3n
					LineaProduccionForm.realizarPaginacion(request, formulario, colCalendarioArticuloDTO, "parametro.paginacion.rango");
					session.setAttribute(LineaProduccionForm.COL_CAL_ART_COMPLETOS, colCalendarioArticuloDTO);		 
		        }catch(SISPEException ex){
		        	 LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		             errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","L\u00EDnea Producci\u00F3n"));
		        }
				forward = "desplegar";
		      }
		    /*----------------------Cuando presiona el boton nuevo-----------------------------*/
		    else if(ayuda!=null && ayuda.equals("nuevaLinPro")){
		    	LogSISPE.getLog().info("Presiono el boton Nuevo");
		    	session.removeAttribute(CAL_ARTICULO_DTO);
		    	session.removeAttribute(CAL_ARTICULO_DTO_HIJO);
		    	session.removeAttribute(INDICE_SELECCION_PADRE);
		    	session.removeAttribute(INDICE_SELECCION_HIJO);
		    	formulario.setFechaCalendario(null);
		      forward = "detalleRegistro";
			}
		    /*----------------------agregar un nuevo registro Linea de Produccion-----------------------*/
		    else if(request.getParameter("agregarLinPro") != null){
		    	LogSISPE.getLog().info("Presiono el boton Agregar Linea Produccion");
		    	instanciarPopUpNuevoLineaProduccion(request);
		    	forward = "detalleRegistro";
		    }
		    //paginacion
			else if (request.getParameter("start") != null && request.getParameter("range")!=null){
				try{
					
					Collection datos = (Collection) session.getAttribute(LineaProduccionForm.COL_CAL_ART_COMPLETOS);
					LogSISPE.getLog().error("---Tama\u00F1o de la coleccion--{}",datos.size());
					LineaProduccionForm.realizarPaginacion(request, formulario, datos, "parametro.paginacion.rango");
				}catch(Exception e){
					errors.add("errorReporteCalArt", new ActionMessage("errors.busqueda", ""));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
				}
			}
		    /*----------------------Graba el nuevo registro Linea de Produccion-----------------------*/
		    else if(request.getParameter("aceptarNuevoLinPro") != null){
		    	LogSISPE.getLog().info("Presiono el boton Aceptar del PopUp");
				if(!StringUtils.isEmpty(formulario.getCantidad()) || formulario.getCantidad().equals("")){
					validar.validateLong(error,"cantidad",formulario.getCantidad(),true,1,99999,"errors.formato.long","cantidad");
				}
				if(formulario.getTipoMotMov().equals("seleccione")){	
					error.add(formulario.getTipoMotMov(), new ActionMessage("errors.requerido", "tipo movimiento"));
				}
				if(error.size() ==  0){
					try{	
			    		LogSISPE.getLog().info("::::Iniciando registrar Linea de Produccion.");
			    		CalendarioArticuloDTO calArtDTO = new CalendarioArticuloDTO();
			    		//obtengo de session el objeto Padre
		    		    VistaCalendarioArticuloDTO calArtActualDTOP = (VistaCalendarioArticuloDTO)session.getAttribute(CAL_ARTICULO_DTO);
		    		    //obtengo de session el objeto Hijo
		    		    VistaCalendarioArticuloDTO calArtActualDTOH = (VistaCalendarioArticuloDTO)session.getAttribute(CAL_ARTICULO_DTO_HIJO);
		    		    //LLenar datos de CalendarioArticuloDTO
			    		calArtDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			    		
			    		if(session.getAttribute(TIPO_AGRUPACION_ART_FEC)!= null){
					    	  calArtDTO.setFechaCalendario(calArtActualDTOH.getFechaCalendario());  
					    	  calArtDTO.getId().setCodigoArticulo(calArtActualDTOP.getId().getCodigoArticulo());
					    }else if (session.getAttribute(TIPO_AGRUPACION_FEC_ART)!= null){
					    	  calArtDTO.setFechaCalendario(calArtActualDTOP.getFechaCalendario());
					    	  calArtDTO.getId().setCodigoArticulo(calArtActualDTOH.getId().getCodigoArticulo());
					     }
			    		//obtengo valores del formulario formulario
			    		  Long cant = Long.valueOf(formulario.getCantidad());
			    		  calArtDTO.setCantidad(cant);
			    		  calArtDTO.setTipoMovimiento(formulario.getTipoMotMov());
			    		  calArtDTO.setObservacion(formulario.getObservacion());
			    		  calArtDTO.setUserId(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
			    	    //Metodo que graba la Linea de Produccion
			    		  SessionManagerSISPE.getServicioClienteServicio().transRegistrarCalendarioArticulo(calArtDTO);
					    //funcion que me actualiza la pantalla
			    		  actualizarVentana(request, formulario);  
			    		  session.removeAttribute(SessionManagerSISPE.POPUP);
			    		}catch(Exception ex){
			    		  LogSISPE.getLog().info("Error al ejecutar registrarCalendarioArticulo:"); 
			    		}
		    	}	
		    	forward = "detalleRegistro";
		    }
		    /*----------------------eliminar un registro Linea de Produccion-----------------------*/
		    else if(request.getParameter("eliminarLinPro") != null){
		    	LogSISPE.getLog().info("Presiono el boton Eliminar Linea Produccion");
    		    VistaCalendarioArticuloDTO calArtActualDTOH = (VistaCalendarioArticuloDTO)session.getAttribute(CAL_ARTICULO_DTO_HIJO);
    		    //Creo el objeto CalendarioArticuloDTO
    		    List<CalendarioArticuloDTO> colCalendario = new ArrayList<CalendarioArticuloDTO>();
    		    CalendarioArticuloDTO seleccionado = new CalendarioArticuloDTO(); 
    		    
    		    List<VistaCalendarioArticuloDTO> detalle = (ArrayList<VistaCalendarioArticuloDTO>)calArtActualDTOH.getColCalendarioArticulo();
    		    if(formulario.getChecksSeleccionar() != null && detalle.size()>0 && calArtActualDTOH != null){
    		    	for(String sec : formulario.getChecksSeleccionar() ){
        		    	LogSISPE.getLog().info("Indice de seleccion n->{}", sec);
        		    	LogSISPE.getLog().info("Objeto a eliminar{}",detalle.get(Integer.valueOf(sec)).getId().getSecuencialCalendario());
        		    	seleccionado.getId().setSecuencialCalendario(detalle.get(Integer.valueOf(sec)).getId().getSecuencialCalendario());
        		    	//seleccionados.add(sec);
        		    	colCalendario.add(seleccionado);
        		    	seleccionado = new CalendarioArticuloDTO();
        		    }
    		    	//Metodo que elimina los registros de  Linea de Produccion seleccionados
		    		SessionManagerSISPE.getServicioClienteServicio().transEliminarCalendarioArticulo(colCalendario);
		    		formulario.setCheckTodo(null);
		    		formulario.setChecksSeleccionar(null);
		    		//funcion que me actualiza la pantalla
		    		actualizarVentana(request, formulario);	
    		    }else{
    		    	 LogSISPE.getLog().info("No ha seleccionado ningun articulo");
    		    }
		    	forward = "detalleRegistro";
		    }
		    /*----------------------Cuando se selecciona editar del Listado-----------------------------*/
		    else if(request.getParameter("editarLinPro") != null){
		    	//Inicializo los indices padre e hijo
		    	Integer indiceHijo = Integer.parseInt(request.getParameter("editarLinPro"));
		    	Integer indicePadre = Integer.parseInt(request.getParameter("indice"));
		    	
		    	LogSISPE.getLog().info("Presiono el boton Editar");
		    	LogSISPE.getLog().info("Valor del IndicePadre:{}",indicePadre.toString());
		    	LogSISPE.getLog().info("Valor del IndiceHijo:{}",indiceHijo.toString());
		    	
		    	session.setAttribute(INDICE_SELECCION_PADRE,request.getParameter("indice"));
		    	session.setAttribute(INDICE_SELECCION_HIJO,request.getParameter("editarLinPro"));
		    	if(session.getAttribute(LineaProduccionForm.COL_CAL_ART_PAGINADOS) != null){
		    		List<VistaCalendarioArticuloDTO> vista = new ArrayList<VistaCalendarioArticuloDTO>();
		    		//vista = (ArrayList<VistaCalendarioArticuloDTO>)formulario.getDatos();
		    		vista = (ArrayList<VistaCalendarioArticuloDTO>)session.getAttribute(LineaProduccionForm.COL_CAL_ART_PAGINADOS);
	    			//obtengo la referencia del objeto Padre seleccionado
	    			vista.get(indicePadre);
	    			session.setAttribute(CAL_ARTICULO_DTO, vista.get(indicePadre));
	    			//obtengo la referencia del objeto hijo seleccionado
	    			List<VistaCalendarioArticuloDTO> detalle = (ArrayList<VistaCalendarioArticuloDTO>)vista.get(indicePadre).getColCalendarioArticulo();
	    			session.setAttribute(CAL_ARTICULO_DTO_HIJO, detalle.get(indiceHijo));	
		    	}
		    	forward = "detalleRegistro";
			}
		    /*---------------------Accion Atras del detalle linea Produccion----------------------*/
		    else if(request.getParameter("atras")!=null){
		    	LogSISPE.getLog().info("Presiono el boton Atras");
		    	session.removeAttribute(CAL_ARTICULO_DTO);
		    	session.removeAttribute(CAL_ARTICULO_DTO_HIJO);
		    	Collection<VistaCalendarioArticuloDTO> colCalendarioArticuloDTO = null;
		    	VistaCalendarioArticuloDTO vista = (VistaCalendarioArticuloDTO)session.getAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA);
		    	if(vista != null){
			        //Acutalizo la seccion de busqueda
		    		if(vista.getId().getCodigoArticulo() != null && !vista.getId().getCodigoArticulo().equals("-1"))
		    		formulario.setCodigoArticuloTxt(vista.getId().getCodigoArticulo());
			        if(vista.getDescripcionArticulo() != null)
			    		formulario.setDescripcionArticuloTxt(vista.getDescripcionArticulo());
			        if(vista.getCodigoClasificacion() != null)
			    		formulario.setCodigoClasificacionTxt(vista.getCodigoClasificacion());
			        if(vista.getNpFechaInicio() != null && vista.getNpFechaFin() != null){
			        	formulario.setFechaInicial(ConverterUtil.parseDateToString(vista.getNpFechaInicio()));
			        	formulario.setFechaFinal(ConverterUtil.parseDateToString(vista.getNpFechaFin()));
			        	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechas"));
			        }	
			        if(vista.getNpTipoReporteAgrupacion() != null && vista.getNpTipoReporteAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2")))
			        	formulario.setCmbOpcionAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"));
			        if(vista.getNpTipoReporteAgrupacion() != null && vista.getNpTipoReporteAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1")))
			        	formulario.setCmbOpcionAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"));
			        if(vista.getNpFechaInicio() == null && vista.getNpFechaFin() == null && vista.getFechaCalendario() != null)
			        	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
			        if(vista.getNpFechaInicio() != null && vista.getNpFechaFin() == null)
			        	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
			        //
		    		try{
			        	//metodo que obtiene la coleccion de CalendarioArticuloDTO
			        	colCalendarioArticuloDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(vista);
			        	//se realiza la paginaci\u00F3n
						LineaProduccionForm.realizarPaginacion(request, formulario, colCalendarioArticuloDTO, "parametro.paginacion.rango");
						session.setAttribute(LineaProduccionForm.COL_CAL_ART_COMPLETOS, colCalendarioArticuloDTO);
			        }catch(SISPEException ex){
			        	 LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			             errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","L\u00EDnea Producci\u00F3n"));
			        }
		    	}
		    	forward = "desplegar";	
		    }
		    /*----------------------Mostrar Solo Detalle de Canasta---------------------------------------*/
			else if(request.getParameter("indiceDetalle") != null){
				//en pantalla se muestra el codigoBarras pero se pasa como parametro en la funcion ajax el codigoArticulo
				LogSISPE.getLog().info("Entro a Mostrar solo detalle de canasta");
				LogSISPE.getLog().info("Valor del Codigo Art\u00EDculo Canasto/Despensa->{}",request.getParameter("indiceDetalle"));
				try{
					//Obtengo la receta original validado desde el sic
					Collection <ArticuloRelacionDTO> colRecetaO = EstadoPedidoUtil.obtenerReceta(request, request.getParameter("indiceDetalle"));
					consultarArticulos(request, estadoActivo, request.getParameter("indiceDetalle"));
					//SE GUARDA LA RECETA
					request.setAttribute(DETALLE_CANASTA, colRecetaO);
					instanciarPopUpReceta(request);
				}catch(SISPEException e){
					error.add("No existe ese Art\u00EDculo en el sic",new ActionMessage("errors.articulos.ArticuloNoExistente"));
				}
				
				forward = "desplegar";
			}
		    /*----------------------Cuando se cancela del PopUp Nuevo Linea Produccion-----------------------------*/
		    else if(request.getParameter("cancelarNuevo") != null){
		    	LogSISPE.getLog().info("Presiono el boton cancelar del PopUp Nuevo");
				session.removeAttribute(SessionManagerSISPE.POPUP);
		    	forward = "desplegar";
			}
		    /*----------------------Cuando se busca un articulo en esa fecha-----------------------------*/
		    else if(request.getParameter("aceptarBusqueda") != null){		    	
		    	LogSISPE.getLog().info("Busca linea de produccion de ese articulo en esa fecha");
		    	try{
			    	if(StringUtils.isNotEmpty(formulario.getFechaCalendario()) && !formulario.getArticuloCombo().equals("vacio")){
			            //coleccion de lineas de Produccion
			    		List<VistaCalendarioArticuloDTO> colCalendarioArticuloDTO = new ArrayList<VistaCalendarioArticuloDTO>();
			    		Date fechaInicial = ConverterUtil.parseStringToDate(formulario.getFechaCalendario());
			    		//campos seteados en el objeto
			    		VistaCalendarioArticuloDTO consultaCalArtDTO = new VistaCalendarioArticuloDTO();
			    		consultaCalArtDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			    		//consultaCalArtDTO.getId().setCodigoArticulo(formulario.getArticuloCombo());
			    		consultaCalArtDTO.setCodigoBarras(formulario.getArticuloCombo());
			    		consultaCalArtDTO.setFechaCalendario(fechaInicial);
			    		
			    		if(formulario.getCmbOpcionAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"))){
				        	LogSISPE.getLog().info("Opcion de Agrupacion articulo - fecha");
				        	consultaCalArtDTO.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"));
				        	session.setAttribute(TIPO_AGRUPACION_ART_FEC,"ok");
				        }else if(formulario.getCmbOpcionAgrupacion().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"))){
				        	LogSISPE.getLog().info("Opcion de Agrupacion fecha - articulo");
				        	consultaCalArtDTO.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"));
				        	session.setAttribute(TIPO_AGRUPACION_FEC_ART,"ok");
				        }
			        	//metodo que obtiene la coleccion de CalendarioArticuloDTO
			        	colCalendarioArticuloDTO = (ArrayList<VistaCalendarioArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(consultaCalArtDTO);
			        	if(colCalendarioArticuloDTO != null && !colCalendarioArticuloDTO.isEmpty()){
			        		LogSISPE.getLog().info("Tama\u00F1o de la coleccion{}",colCalendarioArticuloDTO.size());
			        		//obtengo la referencia del objeto Padre seleccionado
			        		colCalendarioArticuloDTO.get(0);
			    			session.setAttribute(CAL_ARTICULO_DTO, colCalendarioArticuloDTO.get(0));
			    			//obtengo la referencia del objeto hijo seleccionado
			    			List<VistaCalendarioArticuloDTO> detalle = (ArrayList<VistaCalendarioArticuloDTO>)colCalendarioArticuloDTO.get(0).getColCalendarioArticulo();
			    			session.setAttribute(CAL_ARTICULO_DTO_HIJO, detalle.get(0));
			        			
			        	}else{
			        		LogSISPE.getLog().info("Entro a registra un nuevo articulo en esa fecha");
			        		//obtengo el articulo seleccionado del combo
			        		ArticuloDTO consultaArticuloDTO = new ArticuloDTO(Boolean.TRUE);
						    consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
						    consultaArticuloDTO.setNpCodigoBarras(formulario.getArticuloCombo());
						    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
						    //Obtengo el objeto articulo
						    List<ArticuloDTO> articulos = (ArrayList<ArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
						    if(articulos != null && !articulos.isEmpty()){
						    	LogSISPE.getLog().info("Tama\u00F1o de la coleccion:{}",articulos.size());
						    	//seteo los valores en la vista
						    	consultaCalArtDTO.getId().setCodigoArticulo(articulos.get(0).getId().getCodigoArticulo());
						    	//consultaCalArtDTO.setCodigoTipoArticulo(articulos.get(0).getCodigoTipoArticulo());
						    	consultaCalArtDTO.setDescripcionArticulo(articulos.get(0).getDescripcionArticulo());
						    	consultaCalArtDTO.setNpTotalEgresos(0L);
						    	consultaCalArtDTO.setNpTotalIngresos(0L);
						    	consultaCalArtDTO.setColCalendarioArticulo(null);
						    	//consultaCalArtDTO.setDescripcionTipArt(calArtActualDTOP.getDescripcionTipArt());
						    	session.setAttribute(CAL_ARTICULO_DTO, consultaCalArtDTO);
						    	session.setAttribute(CAL_ARTICULO_DTO_HIJO, consultaCalArtDTO);
						    }	
			        	}	
			    	} else { 
			    		LogSISPE.getLog().info("No ha seleccionado los parametros de busqueda");
			    		errors.add("busqueda",new ActionMessage("errors.parametros.busqueda"));
			    	}
			    	}catch(SISPEException e){
						errors.add("busqueda",new ActionMessage("errors.cargar.busqueda",e.getStackTrace()));
					} 	
		    	forward = "detalleRegistro";
			}
		    /*----------------------Si entra a la p\u00E1gina por primera vez-----------------------------*/
		    else{
		    	LogSISPE.getLog().info("Entra por primera vez");
		    	boolean verificar;
				session.removeAttribute(CAL_ARTICULO_DTO);
				session.removeAttribute(INDICE_SELECCION_PADRE);
				session.removeAttribute(INDICE_SELECCION_HIJO);
				session.removeAttribute(SessionManagerSISPE.PLANTILLA_BUSQUEDA);
				session.removeAttribute(ACCESO_USUARIO_LINEA_PRO);
				verificar = WebSISPEUtil.verificarUsuarioClasificacion(request, true);
				if(verificar){
					session.setAttribute(ACCESO_USUARIO_LINEA_PRO, "ok");					
					LogSISPE.getLog().info("Usuario Logeado tiene Permisos de las clasificaciones 1606/1602");
					try{
						//cargo la lista de motivo movimientos
						CalendarioMotivoMovimientoDTO calendarioMotivoMovimientoDTO=new CalendarioMotivoMovimientoDTO();
						calendarioMotivoMovimientoDTO.setTipoCalendario(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoCalendario.bodega"));
						calendarioMotivoMovimientoDTO.setEstadoMotivoMovimiento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo"));
						Collection calendarioMotivoMovimientoDTOCol = SessionManagerSISPE.getServicioClienteServicio().transObtenerCalendarioMotivosMovimiento(calendarioMotivoMovimientoDTO);
						if(calendarioMotivoMovimientoDTOCol != null && !calendarioMotivoMovimientoDTOCol.isEmpty()){
							LogSISPE.getLog().info("Nro de Motivos:{}",calendarioMotivoMovimientoDTOCol.size());
							session.setAttribute("ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol",calendarioMotivoMovimientoDTOCol);	
						} else
							LogSISPE.getLog().info("No obtubo motivos de movimientos");	
					}catch(SISPEException e){
						errors.add("tipoMovimiento",new ActionMessage("errors.cargar.tipoMovimiento",e.getStackTrace()));
					}
					consultarArticulos(request,estadoActivo,null);
				}else{
					LogSISPE.getLog().info("Usuario Logeado no tiene Permisos de las clasificaciones");
				}
				
			}
		    saveErrors(request, error);
		    saveErrors(request, errors);
		    return mapping.findForward(forward);    
	 }
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void actualizarVentana(HttpServletRequest request, LineaProduccionForm formulario)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		List<VistaCalendarioArticuloDTO> colCalendarioArticuloDTO = null;
		
		//obtengo de session el objeto Padre
	    VistaCalendarioArticuloDTO calArtActualDTOP = (VistaCalendarioArticuloDTO)session.getAttribute(CAL_ARTICULO_DTO);
	    //obtengo de session el objeto Hijo
	    VistaCalendarioArticuloDTO calArtActualDTOH = (VistaCalendarioArticuloDTO)session.getAttribute(CAL_ARTICULO_DTO_HIJO);
	    //Plantilla nueva
	    VistaCalendarioArticuloDTO plantilla = new VistaCalendarioArticuloDTO();
	    plantilla.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
	    plantilla.getId().setCodigoArticulo(calArtActualDTOH.getId().getCodigoArticulo());
	   
	    LogSISPE.getLog().info("::::::::::::Valor del Codigo Articulo del Padre a actualizar:::{}",calArtActualDTOP.getId().getCodigoArticulo());
	    LogSISPE.getLog().info("::::::::::::Valor del Codigo Articulo del hijo  a actualizar:::{}",calArtActualDTOH.getId().getCodigoArticulo());
	    
	    if(session.getAttribute(TIPO_AGRUPACION_ART_FEC)!= null){
	    	plantilla.setFechaCalendario(calArtActualDTOH.getFechaCalendario());
	    	plantilla.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"));
	    }else{
	    	plantilla.setFechaCalendario(calArtActualDTOP.getFechaCalendario());
	    	plantilla.setNpTipoReporteAgrupacion(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"));
	    }
	    LogSISPE.getLog().info("::::::::::::Entro a Actualizar la Pantalla");
	    //metodo que obtiene la coleccion de CalendarioArticuloDTO
    	colCalendarioArticuloDTO = (ArrayList<VistaCalendarioArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaCalendarioArticulo(plantilla);
    	if(colCalendarioArticuloDTO != null && !colCalendarioArticuloDTO.isEmpty()){
			//obtengo la referencia del objeto Padre seleccionado
			session.setAttribute(CAL_ARTICULO_DTO, colCalendarioArticuloDTO.get(0));
			//obtengo la referencia del objeto hijo seleccionado
			List<VistaCalendarioArticuloDTO> detalle = (ArrayList<VistaCalendarioArticuloDTO>)colCalendarioArticuloDTO.get(0).getColCalendarioArticulo();
			session.setAttribute(CAL_ARTICULO_DTO_HIJO, detalle.get(0));	
    	}else{
    		LogSISPE.getLog().info("Entro a registra un nuevo articulo en esa fecha");
		    VistaCalendarioArticuloDTO nuevoCalArt = new VistaCalendarioArticuloDTO();
    		//seteo los valores en la nueva vista
		    nuevoCalArt.getId().setCodigoCompania(calArtActualDTOH.getId().getCodigoCompania());
		    nuevoCalArt.getId().setCodigoArticulo(calArtActualDTOH.getId().getCodigoArticulo());
		    nuevoCalArt.setFechaCalendario(calArtActualDTOP.getFechaCalendario());
		    //nuevoCalArt.setCodigoTipoArticulo(calArtActualDTOH.getCodigoTipoArticulo());
		    nuevoCalArt.setDescripcionArticulo(calArtActualDTOH.getDescripcionArticulo());
		    nuevoCalArt.setDescripcionTipArt(calArtActualDTOP.getDescripcionTipArt());
		    nuevoCalArt.setNpTotalEgresos(0L);
		    nuevoCalArt.setNpTotalIngresos(0L);
		    nuevoCalArt.setColCalendarioArticulo(null);
		    session.setAttribute(CAL_ARTICULO_DTO, nuevoCalArt);
		    session.setAttribute(CAL_ARTICULO_DTO_HIJO, nuevoCalArt);
    		
    	}
    		
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpNuevoLineaProduccion(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("L\u00EDnea producci\u00F3n");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('lineaProduccion.do', ['pregunta','seccionDetalle','seccionTotales','zonaMensajes'], {parameters: 'aceptarNuevoLinPro=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('lineaProduccion.do', ['pregunta'], {parameters: 'cancelarNuevo=ok', popWait:false});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setAncho(58d);
		popUp.setContenidoVentana("calendarioBodega/lineaProduccion/ingresoLineaProduccion.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;	
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private void instanciarPopUpReceta(HttpServletRequest request)throws Exception{
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye la informaci\u00F3n de la ventana
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Detalle del canasto");
		//popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorCANCEL("requestAjax('lineaProduccion.do', ['pregunta','mensajes'], {parameters: 'cancelarNuevo=ok', evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setTope(-45d);
		popUp.setAncho(70d);
		popUp.setContenidoVentana("calendarioBodega/lineaProduccion/detalleCanasta.jsp");
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;	
	}
	
	/**
	 * Consulta el articulo que se muestra en el detalle de la receta.
	 * @param request
	 * @param estadoActivo
	 * @param codigoArticulo 
	 * @throws Exception
	 */
	private void consultarArticulos(HttpServletRequest request, String estadoActivo, String codigoArticulo)throws Exception{
		//cargo el combo de articulo
		//Se obtiene el parametro de Canastas (1602)
    	ParametroID parametro = new ParametroID();
		//Obtengo codigo de el catalago de Canastas desde la tabla Parametro
		parametro.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		parametro.setCodigoParametro(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo"));
		ParametroDTO parametroDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerParametroPorID(parametro);
		LogSISPE.getLog().info("Valor del Parametro{}",parametroDTO.getValorParametro());
		//se obtiene la sesi\u00F3n
		HttpSession session = request.getSession();
		//se construye el objeto para la consulta
		ArticuloDTO consultaArticuloDTO = new ArticuloDTO(Boolean.TRUE);
		if(codigoArticulo == null){
			LogSISPE.getLog().info("Entro a cargar el Combo");
			consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
    	    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
    	    consultaArticuloDTO.setCodigoClasificacion(parametroDTO.getValorParametro());
    	    consultaArticuloDTO.setNpCodigoTipoArticulo("ok");
    	}else{
    		LogSISPE.getLog().info("Entro a consultar ese articulo");
    		consultaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
    	    consultaArticuloDTO.setEstadoArticulo(estadoActivo);
    	    consultaArticuloDTO.getId().setCodigoArticulo(codigoArticulo);
    	}	
		//llamada al m\u00E9todo de la capa de servicio que devuelve la colecci\u00F3n de art\u00EDculos
	    List <ArticuloDTO> articulos = (ArrayList<ArticuloDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerArticuloQBE(consultaArticuloDTO);
	    if(articulos != null && !articulos.isEmpty() && codigoArticulo == null){
	    	LogSISPE.getLog().info("Tama\u00F1o de la coleccion:{}",articulos.size());
	    	session.setAttribute(COL_ARTICULOS, articulos);
	    }
	    if(articulos != null && !articulos.isEmpty() && codigoArticulo != null){
	    	ArticuloDTO art = (ArticuloDTO)articulos.get(0);
	    	session.setAttribute(ARTICULO_DTO, art);
	    }
	    	
	}
	
}
