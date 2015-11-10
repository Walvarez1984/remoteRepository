/*
 * Clase DespachosAction.java 
 * Creado 13/06/06
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.SUBIR_ARCHIVO_FOTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.kruger.utilitario.dao.commons.hibernate.CriteriaSearch;
import ec.com.smx.corpv2.common.enums.SystemProvenance;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.ContactoPersonaLocalizacionRelacionadoDTO;
import ec.com.smx.corpv2.dto.DatoContactoPersonaLocalizacionDTO;
import ec.com.smx.corpv2.dto.EmpresaDTO;
import ec.com.smx.corpv2.dto.LocalizacionDTO;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.UtilPopUp;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.ArchivoEntregaDTO;
import ec.com.smx.sic.sispe.dto.DefArchivoEntregaDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaReporteGeneralDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.DespachosEntregasForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite realizar el despacho parcial o total de un pedido, mediante la selecci\u00F3n de los lugares 
 * de entrega donde ser\u00E1n enviados los art\u00EDculos.</br>
 * Se manejan los siguientes iconos para indicar al usuario el estado del despacho:
 * <ol>
 * 	<li><img src="images/error_small.gif"/>&nbsp;Ning\u00FAn art\u00EDculo del pedido se ha despachado</li>
 * 	<li><img src="images/advertencia_16.gif"/>&nbsp;Parte del pedido se ha despachado</li>
 *  <li><img src="images/exito_16.gif"/>&nbsp;Todo el pedido est\u00E1 listo para despacharse</li>
 * </ol>
 * </p>
 * @author 	fmunoz
 * @version 2.0
 * @since	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class DespachosAction extends BaseAction{
  private static final String OBJ_BUSQUEDA = "ec.com.smx.sic.sispe.pedidos.busqueda";
  private static final String RPT_DESPACHADO = "ec.com.smx.sic.sispe.reporte.despachado";
  private static final String COL_DATOS_REPORTE ="ec.com.sic.sispe.datos.reporte";
  public static final String PLANTILLA_DEFARCHIVOENTREGA ="ec.com.sic.sispe.plantilla.defArchivoEntrega";
  public static final String IMPRIMIR_ETIQUETAS = "ec.com.sic.sispe.imprimir.etiquetas";
  public static final String NUMERO_ENTREGAS = "ec.com.sic.sispe.numero.entregas";
  public static final String ENTREGA_ELIMINADA_ACTUAL = "ec.com.sic.sispe.entrega.eliminada.actual";
  //private String estadoDespachado =  MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado");
  //private String estadoProducido =  MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido");
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
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  throws Exception {
    //objetos para los mensajes
    ActionMessages success = new ActionMessages();
    ActionMessages infos = new ActionMessages();
    ActionMessages errors = new ActionMessages();
    String accion= "despachoPedido.do";
    //  esta variable permite que se muestre o no una ventana con los datos que van a imprimirse para el 
    //reporte en la pesta\u00F1a de despachos
    //[1: se muestra, 0: no se muestra]  
    request.getSession().setAttribute("ec.com.smx.sic.sispe.Despacho.imprimir","0");
    HttpSession session = request.getSession();
    DespachosEntregasForm formulario = (DespachosEntregasForm) form;
    //se obtienen los estados activos e inactivos de sesi\u00F3n
    String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
    String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
	//se obtiene el bean que contiene los campos genericos
	BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
	boolean generarXSL = false;
	String ayuda= request.getParameter(Globals.AYUDA);
	if(ayuda!=null && !ayuda.equals("")){
		if(ayuda.equals("xls"))
			generarXSL = true;
	}
	LogSISPE.getLog().info("ayuda: {}",ayuda);	
    String salida = "desplegar";
    try
    {
      /*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
      if(request.getParameter("range")!=null || request.getParameter("start")!=null)
      {
        LogSISPE.getLog().info("ENTRO A LA PAGINACION");
        String tab = (String)session.getAttribute("ec.com.smx.sic.sispe.seccionPagina");
        Collection datos = new ArrayList();
        if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos")))
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
        else{
          datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
          beanSession.setInicioPaginacion(request.getParameter("start"));
        }
        if(datos!=null && !datos.isEmpty()){
          int size= datos.size();
          int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
          int start = Integer.parseInt(request.getParameter("start"));
          formulario.setStart(String.valueOf(start));
          formulario.setRange(String.valueOf(range));
          formulario.setSize(String.valueOf(size));
          
          Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
          formulario.setDatos(datosSub);
          
          session.setAttribute(COL_DATOS_REPORTE, datosSub);
      
        }
      }
      //accion el botton aceptar pop up
      else if(request.getParameter("aceptaArchivoFoto") != null){
    	  LogSISPE.getLog().info("Presion\u00F3 el boton aceptar del popUp");
    	  List<DefArchivoEntregaDTO> colArchivos = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
    	  //Inicializo la variable de session total
  		  if(request.getSession().getAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO)==null){
  			 request.getSession().setAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO, new ArrayList<DefArchivoEntregaDTO>());
  		  }
    	  //obtengo el total de archivos Adjuntos
  		  List<DefArchivoEntregaDTO> colTotalFiles = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
  		  //Definicion Archivo Aux para eliminar el ultimo archivo
  		  DefArchivoEntregaDTO defArchAux = (DefArchivoEntregaDTO)session.getAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX);
  		  //Collection que se almacena los archivos duplicados
  		  Collection<DefArchivoEntregaDTO> colDuplicada = new ArrayList<DefArchivoEntregaDTO>();
  		  if(colArchivos != null && !colArchivos.isEmpty()){
    		//Encuentro el codigo de Pedido 
    		DefArchivoEntregaDTO defArchivo = (DefArchivoEntregaDTO)CollectionUtils.get(colArchivos, 0);
    		if(colTotalFiles != null && !colTotalFiles.isEmpty()){
    			//remueve si encuentra archivos subidos de ese pedido
    			for(DefArchivoEntregaDTO totalDefArc : colTotalFiles){
    				//Verifico si existe ya una collection de ese pedido
    				if(totalDefArc.getSecuencialEntrega().equals(defArchivo.getSecuencialEntrega())){
    					colDuplicada.add(totalDefArc);
    				}
        		}
    			//Elimino la collection duplicada
    			if(colDuplicada != null && !colDuplicada.isEmpty()){
    				colTotalFiles.removeAll(colDuplicada);
    			}
    		}
    		//Agrego la nueva collection al total de archivos
			colTotalFiles.addAll(colArchivos);
    		session.setAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO, colTotalFiles);
          }else if(defArchAux != null){
    		if(colTotalFiles != null && !colTotalFiles.isEmpty()){
    			//remueve si encuentra archivos subidos de ese pedido
    			for(DefArchivoEntregaDTO totalDefArc : colTotalFiles){
    				//Verifico si existe ya una collection de ese pedido
    				if(totalDefArc.getSecuencialEntrega().equals(defArchAux.getSecuencialEntrega())){
    					colDuplicada.add(totalDefArc);
    				}
        		}
    			//Elimino la collection duplicada
    			if(colDuplicada != null && !colDuplicada.isEmpty()){
    				colTotalFiles.removeAll(colDuplicada);
    			}
    		}
    		session.setAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO, colTotalFiles);
          }
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
      }
      
      //accion el botton cancelar pop up
      else if(request.getParameter("cancelaArchivoFoto") != null){
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
      }
      //accion del boton adjuntar foto
      else if(request.getParameter(SUBIR_ARCHIVO_FOTO) != null){
			//almacena el codigo del pedido que se envia desde la jsp estadopedido
    	  	String pedido = (String)request.getParameter(SUBIR_ARCHIVO_FOTO);
    	  	Integer codigoLocal = Integer.parseInt(request.getParameter("codigoLocal"));
    	  	Integer codigoLocalEntrega = Integer.parseInt(request.getParameter("codigoLocalEntrega"));
    	  	String codigoArticulo = (String)request.getParameter("codigoArticulo");
			String codigoEstado = (String)request.getParameter("codigoEstado");
			String secuencialEstadoPedido = (String)request.getParameter("secuencialEstadoPedido");
			String secuencialEntrega = (String)request.getParameter("secuencialEntrega");
			Long codigoDetalleEntregaPedido = Long.parseLong(request.getParameter("codigoDetalleEntregaPedido"));
			//limpio variable de session
    	  	session.removeAttribute(PLANTILLA_DEFARCHIVOENTREGA);
    	  	session.removeAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX);
    	  	//se construye la plantilla para la busqueda defArchivoEntrega
			DefArchivoEntregaDTO defPlantillaDTO = new DefArchivoEntregaDTO();
			defPlantillaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			defPlantillaDTO.getId().setCodigoArticulo(codigoArticulo);			
			defPlantillaDTO.getId().setCodigoAreaTrabajo(codigoLocal);
			defPlantillaDTO.getId().setCodigoPedido(pedido);
			defPlantillaDTO.setCodigoLocalEntrega(codigoLocalEntrega);
			defPlantillaDTO.setCodigoEstado(codigoEstado);
			defPlantillaDTO.setSecuencialEstadoPedido(secuencialEstadoPedido);
			defPlantillaDTO.setSecuencialEntrega(secuencialEntrega);
			defPlantillaDTO.setCodigoDetalleEntregaPedido(codigoDetalleEntregaPedido);
			session.setAttribute(PLANTILLA_DEFARCHIVOENTREGA, defPlantillaDTO);
			//Realiza el metodo de busqueda
			CotizacionReservacionUtil.busquedaArchivoFoto(request, defPlantillaDTO);
			//se construye la ventana de informacion de archivos fotos
			CotizacionReservacionUtil.instanciarVentanaSubirArchivoFoto(request, accion);
			
      }
      else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("adjuntarArchivoFoto")) {
    	  //Obtengo de session la plantilla
		  DefArchivoEntregaDTO plantillaDefArcEnt = (DefArchivoEntregaDTO)session.getAttribute(PLANTILLA_DEFARCHIVOENTREGA);
		  session.removeAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX);
		 //seteo propiedades archivo foto
		  DefArchivoEntregaDTO defArchivoDTo = new DefArchivoEntregaDTO();
		  defArchivoDTo.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		  defArchivoDTo.getId().setCodigoArticulo(plantillaDefArcEnt.getId().getCodigoArticulo());			
		  defArchivoDTo.getId().setCodigoAreaTrabajo(plantillaDefArcEnt.getId().getCodigoAreaTrabajo());
		  defArchivoDTo.setCodigoLocalEntrega(plantillaDefArcEnt.getCodigoLocalEntrega());
		  defArchivoDTo.getId().setCodigoPedido(plantillaDefArcEnt.getId().getCodigoPedido());
		  defArchivoDTo.setSecuencialEstadoPedido(plantillaDefArcEnt.getSecuencialEstadoPedido());
		  defArchivoDTo.setSecuencialEntrega(plantillaDefArcEnt.getSecuencialEntrega());
		  defArchivoDTo.setCodigoEstado(plantillaDefArcEnt.getCodigoEstado());
		  defArchivoDTo.setNombreArchivo(formulario.getArchivo().getFileName());
		  defArchivoDTo.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.FotoDespacho"));
		  defArchivoDTo.setTamanioArchivo(BigDecimal.valueOf(formulario.getArchivo().getFileSize()));
		  defArchivoDTo.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		  defArchivoDTo.setCodigoDetalleEntregaPedido(plantillaDefArcEnt.getCodigoDetalleEntregaPedido());
		  //defArchivoDTo.setRegisterDate(new Timestamp(System.currentTimeMillis()));
		  //Creo el archivo
		  ArchivoEntregaDTO archivoDTO = new ArchivoEntregaDTO();
		  archivoDTO.setDatosArchivo(formulario.getArchivo().getFileData());
		  defArchivoDTo.setArchivoEntregaDTO(archivoDTO);
		  defArchivoDTo.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		  
		  //verifica si el nuevo archivo no tiene el mismo nombre.
		  if(!CotizacionReservacionUtil.validarArchivoFoto(request, formulario.getArchivo().getFileName())){
	    	LogSISPE.getLog().info("Pasa validacion foto");
	    	//se acepta el archivo del beneficiario
			CotizacionReservacionUtil.agregarSessionArchivoFoto(request,defArchivoDTo);
		  }else{
	    	LogSISPE.getLog().info("Archivo Repetido");
	    	CotizacionReservacionUtil.instanciarVentanaAuxiliarArchivoFoto(request,accion);
		  }
	  }
      //accion del icono eliminar archivo del popUp mostrarArchivoBeneficiario
	  else if(request.getParameter("eliminarArchivoFoto") != null){
		  Integer indiceArchivo = Integer.parseInt(request.getParameter("eliminarArchivoFoto"));
		  CotizacionReservacionUtil.eliminarArchivoFoto(request,indiceArchivo);				
	  }
	  else if(request.getParameter("verArchivoFoto") != null){
			LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
			CotizacionReservacionUtil.verArchivoFoto(request, response);
			salida = null;
	  }
	  else if(request.getParameter("reemplazarArchivo") != null){
			LogSISPE.getLog().info("Acepto el reeplazo del archivo");	
			//creo el nuevo archivo.
			CotizacionReservacionUtil.reemplazarArchivoFoto(request,formulario);
			//finalmente oculta el popUpAuxiliar.
			CotizacionReservacionUtil.cancelarReeplazoArchivo(request,"despachoPedido.do");	
	  }
      /*-------- cuando se cancela el reemplazo del archivo existente----------*/
	  else if(request.getParameter("cancelarReemplazo") != null){
			//se cancela el archivo del beneficiario 
			CotizacionReservacionUtil.cancelarReeplazoArchivo(request, "despachoPedido.do");
	  }
      /*-------------------- si se escogi\u00F3 el link para ver el detalle de  alg\u00FAn pedido ---------------------*/
      else if (request.getParameter("indicePedido")!= null){
        int indice=0;
        ArrayList<VistaPedidoDTO> pedido = null;
        session.removeAttribute(SessionManagerSISPE.DEF_ARCHVO_AUX);
        session.removeAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
        session.removeAttribute(SessionManagerSISPE.COL_ARCHIVO_FOTO);
        session.removeAttribute(PLANTILLA_DEFARCHIVOENTREGA);
        try{
          indice = Integer.parseInt(request.getParameter("indicePedido"));
          if(session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar") != null){
        	  //obtengo de la sesion la colecci\u00F3n de los pedidos por despachar
              pedido = (ArrayList<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
          }
          if(session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados") != null){
        	  //obtengo de la sesion la colecci\u00F3n de los pedidos por despachar
        	  pedido = (ArrayList<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
          }
          
          VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedido.get(indice);
          //se recuperan los checkboxs escogidos anteriormente y se los asigna al formulario
          if(session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar") != null || session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados") != null){
	          if(vistaPedidoDTO.getNpEstadoVistaPedido()!=null){
	            formulario.setOpSeleccionAlgunos(vistaPedidoDTO.getNpSeleccionados());
	          	if(vistaPedidoDTO.getNpEstadoVistaPedido().equals(estadoActivo))
	          		formulario.setOpSeleccionTodos(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
	          }
          }
          //se almacena el indice del pedido seleccionado
          beanSession.setIndiceRegistro(request.getParameter("indicePedido"));
         
          Collection detallePedido = vistaPedidoDTO.getVistaDetallesPedidos();
          session.removeAttribute(ENTREGA_ELIMINADA_ACTUAL);
          eliminaEntregasImprimirReporte(request, vistaPedidoDTO);
          
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
          
        }catch(SISPEException ex){
        	LogSISPE.getLog().info("Error de aplicacion "+ex);
        	errors.add("Exception",new ActionMessage("errors.SISPEException",ex.getMessage()));
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
      	/*------------------------- si se escogi\u00F3 el tag de los pedidos en producci\u00F3n -------------------------------*/
      	if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
      		/*---------- si se escogi\u00F3 el tab de pedidos por despachar ----------------*/
      		//else if(formulario.getTabPedidos()!=null)
      		session.removeAttribute("ec.com.sic.sispe.imprimir.etiquetas");
      		session.removeAttribute("ec.com.smx.sic.sispe.imprimirEtiquetas");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		LogSISPE.getLog().info("pedidos por despachar");
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
      	//----- si se escogi\u00F3 el tab de pedidos despachados ----------
      	else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
      		session.removeAttribute("ec.com.sic.sispe.imprimir.etiquetas");
      		session.removeAttribute("ec.com.smx.sic.sispe.imprimirEtiquetas");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
      		LogSISPE.getLog().info("pedidos despachados");
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
      	//----------------IOnofre, se escogi\u00F3 el tab de imprmir etiquetas-------------
      	else if(beanSession.getPaginaTab().esTabSeleccionado(2)){
      		LogSISPE.getLog().info("impresion de etiquetas de pedidos despachados y desde domicilio CD");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
      		session.removeAttribute(IMPRIMIR_ETIQUETAS);
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado");
      		LogSISPE.getLog().info("pedidos despachados para impresion");
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
      		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.imprimirEtiquetas"));
      	}
      }
      /*---------------------------- si presiona el bot\u00F3n buscar -----------------------------------*/
      else if(request.getParameter("buscar")!=null){    	      	  
      	
      	  if(beanSession.getPaginaTab().esTabSeleccionado(2)){
      		LogSISPE.getLog().info("impresion de etiquetas de pedidos despachados y desde domicilio CD");
      		session.removeAttribute(IMPRIMIR_ETIQUETAS);
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado");
      		session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
      		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.imprimirEtiquetas"));
      		consultarPedidosDespachadosCD(formulario, request, beanSession, infos, errors);
      	}else{      		
      		//se llama al m\u00E9todo que realiza la b\u00FAsqueda
        	this.buscar(formulario, session, request, beanSession, infos, errors);
      	}      	  
      	
      }
      
      /*IOnofre.---------------------- si se escogi\u00F3 el bot\u00F3n Imprimir etiquetas ---------------------------------*/
      else if(request.getParameter("botonImprimir")!=null){
    	  
    	  List<EntregaPedidoDTO> entregasEtiquetasImprimirCol = (List<EntregaPedidoDTO>) session.getAttribute(IMPRIMIR_ETIQUETAS);
    	  if(CollectionUtils.isNotEmpty(entregasEtiquetasImprimirCol)){
	    	  LogSISPE.getLog().info("entra a imprimir las entregas seleccionadas");
	    	  session.setAttribute("ec.com.smx.sic.sispe.imprimirEtiquetas", "ok");
	    	  salida="imprimirEtiquetas";
    	  }else{
    		  session.removeAttribute("ec.com.smx.sic.sispe.imprimirEtiquetas");
    		  errors.add("indicesVacios", new ActionMessage("errors.seleccion.etiquetas.entregas.imprimir"));
    	  }
    	  
      }
      else if(formulario.getBotonImprimir2()!=null){
    	  LogSISPE.getLog().info("entrar a aceptarReporte");
    	  //tomo los pedidos de la b\u00FAsqueda
			 Collection pedidosDespachados = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
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
					//itero la colecci\u00F3n de pedidos paginados
					List<VistaPedidoDTO> datosPedidos = (List<VistaPedidoDTO>)session.getAttribute(COL_DATOS_REPORTE);
					StringBuilder codigosPedidos = new StringBuilder("");
					int cont=1;
					LogSISPE.getLog().info("tamano colecci\u00F3n: {}",datosPedidos.size());
					for(VistaPedidoDTO vistaPedidoDTO2 : datosPedidos){
						if(cont < datosPedidos.size()){
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
      /*--------------- realiza el forward a la p\u00E1gina adecuada -------------------*/
      else if(request.getParameter("mostrarVentana")!=null){
        salida = "rptDespachoEntrega";
      }
      else if(request.getParameter("mostrarVentana2")!=null){
        salida = "rptDespacho";
      }
      /*-------- CONFIRMACION de la generaci\u00F3n del documento como XSL -------*/
	  else if(generarXSL){
	    	LogSISPE.getLog().info("confirmar impresion XSL");
	      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("Despacho de Pedidos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
	      	salida= "reporteDespachoXLS";
	  }
      /*------------------------------ si se acept\u00F3 los despachos seleccionados ---------------------------------*/
      else if(formulario.getBotonAceptarSeleccion()!=null)
      {
        //se obtiene el indice del pedido seleccionado
        String indiceSeleccionado = beanSession.getIndiceRegistro();
        //se obtiene de sesi\u00F3n la colecci\u00F3n de indices guardados hasta el momento de los pedidos
        Collection indicesPedidos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.despacho.indicesPedidos");
        //se obtiene el pedido que se seleccion\u00F3
        VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado");
        
        //se crea la colecci\u00F3n de indices de los detalles del pedido
        Collection indicesDetallePedido = new ArrayList();
        
        //se obtiene el detalle de los pedidos
        ArrayList<VistaDetallePedidoDTO> detallePedido = (ArrayList<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidos();
        
        /*-------------------------------------------------------------------------------------------------------*/
        /*----- Se calcula la cantidad de entregas que existen por art\u00EDculo que todav\u00EDa no se han entregado ------*/
        int detallesCompletados = 0; //variable que almacena la cantidad de detalles que ya est\u00E1n
        //se obtiene el vector de los pesos ingresados
        Double [] pesosDespachados = formulario.getVectorPesoArticulo();
        int indiceVectorPesos = 0;  //indice para el vector de pesos
        //completamente despachados 
        //se obtiene el estado del pedido seleccionado
        for(VistaDetallePedidoDTO vistaDetallePedidoDTO : detallePedido){

          //solo si se reserv\u00F3 el art\u00EDculo asociado al detalle 
          if(vistaDetallePedidoDTO.getReservarBodegaSIC().equals(estadoActivo)){
          	try{
          		//el valor es correcto
          		vistaDetallePedidoDTO.setNpPesoDespacho(pesosDespachados[indiceVectorPesos]);
	          	if(pesosDespachados[indiceVectorPesos]<0){
	          		//valor menor a cero
	          		pesosDespachados[indiceVectorPesos] = pesosDespachados[indiceVectorPesos] * (-1);
	          		vistaDetallePedidoDTO.setNpPesoDespacho(pesosDespachados[indiceVectorPesos]);
	            	LogSISPE.getLog().info("el peso fue menor a cero");
	          	}
          	}catch(NumberFormatException ex){
          		//no es un n\u00FAmero
          		vistaDetallePedidoDTO.setNpPesoDespacho(0D);
          	}
          	
          	LogSISPE.getLog().info("el peso asignado es: {}",vistaDetallePedidoDTO.getNpPesoDespacho());
          	ArticuloDTO articuloDTO = new ArticuloDTO();          	
          	articuloDTO = vistaDetallePedidoDTO.getArticuloDTO();
          	articuloDTO.setTipoCalculoPrecioFiltro(vistaDetallePedidoDTO.getTipoArticuloCalculoPrecio());
          	vistaDetallePedidoDTO.setArticuloDTO(articuloDTO);
          	indiceVectorPesos++;
          }
          
          LogSISPE.getLog().info("ENTRO A BLANQUEAR");
          vistaDetallePedidoDTO.setIndicesEntregas(null);
          ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)vistaDetallePedidoDTO.getEntregaDetallePedidoCol();
          int sizeEntregas = entregas.size();
          int entregasCompletadas = 0; //variable que almacena la cantidad de entregas por art\u00EDculo que tienen
          //una fecha de despacho
        	  for(EntregaDetallePedidoDTO entDetPedDTO:entregas){
	        	 if(entDetPedDTO.getFechaRegistroDespacho()!=null)
	              entregasCompletadas++;
	            else
	              break;
        	  }
          if(entregasCompletadas==sizeEntregas)
            detallesCompletados++;
        }
        //session.setAttribute("ec.com.smx.sic.sispe.detallePedido",detallePedido);
        
        int sizeDetallePedido = detallePedido.size() - detallesCompletados;
        /*--------------------------------------------------------------------------------------------------*/
        
        String indiceAnteriorDetalle = ""; //almacena el indice anterior de cada detalle encontrado
        int sizeEntregas = 0; //almacena la sumatoria total de entregas encontradas en los detalleSeleccionados
        
        /*---------------------------- comienza el proceso -------------------------------*/
        String [] escogidos = formulario.getOpSeleccionAlgunos();
        if(escogidos!=null)
        {	
        	
        	Collection<DefArchivoEntregaDTO> colFiles = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
        	
        	//se itera el arreglo de los indices escogidos
        	for(int i=0;i<escogidos.length;i++){
        		String [] indices = escogidos[i].split("-");
        		LogSISPE.getLog().info(i+" INDICE ARTICULO: {}, INDICE ENTREGA: {}",indices[0],indices[1]);
        		LogSISPE.getLog().info("----------------------------------");
        		//se obtiene el indice del art\u00EDculo que contiene las entregas que se seleccionaron
        		//este indice se encuentra en el primer arreglo de String: indices[0]
        		int indiceDetalleSel = Integer.parseInt(indices[0]);
        		int indiceEntregaSel = Integer.parseInt(indices[1]);
        		VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)detallePedido.get(indiceDetalleSel);
        		
        		//Proceso de comparacion de los archivos cargados con los despachos seleccionados
        		EntregaDetallePedidoDTO entregasAuxDTO =(EntregaDetallePedidoDTO) CollectionUtils.get(vistaDetallePedidoDTO.getEntregaDetallePedidoCol(), indiceEntregaSel);
        		
        		if(colFiles != null && !colFiles.isEmpty()){
        			//En esta collection se almacena los archivo que tienen el check de despachos
        			Collection<DefArchivoEntregaDTO> colFilesActual = new ArrayList<DefArchivoEntregaDTO>();
        			for(DefArchivoEntregaDTO defArchivo : colFiles){
        				if(defArchivo.getSecuencialEntrega().equals(entregasAuxDTO.getEntregaPedidoDTO().getId().getCodigoEntregaPedido())){
        					colFilesActual.add(defArchivo);
        				}
            		}
        			//A\u00F1ado a la collection total pedido archivo foto
        			if(colFilesActual != null && !colFilesActual.isEmpty()){
        				//obtengo el total de archivos Adjuntos
            	  		Collection<DefArchivoEntregaDTO> colPedidoTotalFiles = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.PEDIDO_TOTAL_COL_ARCHIVO_FOTO);
            	  		//Coleccion a eliminar de la general
            	  		Collection<DefArchivoEntregaDTO> colEliminar = new ArrayList<DefArchivoEntregaDTO>();
            	  		if(colPedidoTotalFiles != null && !colPedidoTotalFiles.isEmpty()){
            	  			Set<String> colEliminadosRep = new HashSet<String>();
        	  				for(DefArchivoEntregaDTO def1 : colFilesActual){
        	  					colEliminadosRep.add(def1.getSecuencialEntrega());
        	  				}
            	  			//verifico si los que viene ya no estan en la ultima collection
            	  			for(String secuencialEntrega: colEliminadosRep){
            	  				for(DefArchivoEntregaDTO defArcEntDTOAUX : colPedidoTotalFiles){
                	  				if(defArcEntDTOAUX.getSecuencialEntrega().equals(secuencialEntrega)){
                	  					colEliminar.add(defArcEntDTOAUX);
                	  				}	
                	  			}
            	  			}
            	  			if(colEliminar != null && !colEliminar.isEmpty()){
            	  				colPedidoTotalFiles.removeAll(colEliminar);
            	  			}
            	  			colPedidoTotalFiles.addAll(colFilesActual);
            	  		}else{
            	  			colPedidoTotalFiles = new ArrayList<DefArchivoEntregaDTO>();
            	  			colPedidoTotalFiles.addAll(colFilesActual);
            	  		}
        				//A\u00F1ado a la lista general de pedido de archivos cargados
                		session.setAttribute(SessionManagerSISPE.PEDIDO_TOTAL_COL_ARCHIVO_FOTO, colPedidoTotalFiles);
        			}
            	}
        		//Fin proceso
        		
        		//si el indice no consta en la lista se a\u00F1ade y se actualiza el tama\u00F1o total de las entregas
        		if(!indicesDetallePedido.contains(indices[0])){
        			indicesDetallePedido.add(indices[0]);
        			LogSISPE.getLog().info("SE GUARDO EN INDICES DEL DETALLE: {}",indices[0]);
        			vistaPedidoDTO.setIndicesVistaDetallesPedidos(indicesDetallePedido);
        		}
        		//se suman los tama\u00F1os de las entregas encontradas
        		if(!indiceAnteriorDetalle.equals(indices[0])){
        			indiceAnteriorDetalle=indices[0];
        			Collection<EntregaDetallePedidoDTO> entregas = vistaDetallePedidoDTO.getEntregaDetallePedidoCol();
        			int despachados = 0;
        			//se itera la colecci\u00F3n de entregas del detalle actual para determinar cuantas 
        			//tienen una fecha de despacho
        				for(EntregaDetallePedidoDTO entDetPedDTO:entregas){
	        				if(entDetPedDTO.getFechaRegistroDespacho()!=null){
	        					despachados++; //se suman las entregas con fecha de despacho
	        				}
        				}
        			sizeEntregas = sizeEntregas + entregas.size() - despachados;
        		}
        		/*----------------- seccion entregas ------------------------*/
        		//creaci\u00F3n de la colecci\u00F3n de indices de entregas
        		Collection indicesEntrega = null;
        		if(vistaDetallePedidoDTO.getIndicesEntregas()!=null)
        			indicesEntrega = vistaDetallePedidoDTO.getIndicesEntregas();
        		else
        			indicesEntrega=new ArrayList();

        		if(!indicesEntrega.contains(indices[1])){
        			indicesEntrega.add(indices[1]);
        			LogSISPE.getLog().info("*** SE GUARDO EN INDICES DE LA ENTREGA: {}",indices[1]);
        			vistaDetallePedidoDTO.setIndicesEntregas(indicesEntrega);
        		}
        		/*-----------------------------------------------------------*/
        	}

        	//se comparan los tama\u00F1os de las colecciones para determinar si fueron seleccionados 
        	//todos los beneficiarios
        	LogSISPE.getLog().info("sizeDetallePedido: {}",sizeDetallePedido);
        	//LogSISPE.getLog().info("indicesDetallePedido.size(): "+indicesDetallePedido.size());
        	LogSISPE.getLog().info("--------------------------------------");
        	if(indicesDetallePedido.size() == sizeDetallePedido){
        		LogSISPE.getLog().info("sizeEntregas: {}",sizeEntregas);
        		LogSISPE.getLog().info("escogidos.length: {}",escogidos.length);
        		//si la cantidad de escogidos es igual a la cantidad de beneficiarios encontrados
        		if(escogidos.length == sizeEntregas){
        			//se despach\u00F3 todo el pedido
        			vistaPedidoDTO.setNpEstadoVistaPedido(estadoActivo); 
        			LogSISPE.getLog().info("SE SELECCIONO TODO");
        		}
        		else
        			// se despach\u00F3 parte del pedido
        			vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); 
        	}else
        		// se despach\u00F3 parte del pedido
        		vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); 

        	//si el indice seleccionado no esta en la colecci\u00F3n se a\u00F1ade
        	if(!indicesPedidos.contains(indiceSeleccionado))
        		indicesPedidos.add(indiceSeleccionado);

        	//finalmente se almacena el arreglo de String que contiene los valores que se chequearon en el 
        	//multiBoxs de beneficiarios, para recuperarlos cuando se quiera editar la entrega de un pedido.
        	vistaPedidoDTO.setNpSeleccionados(escogidos);

        	/*------ prueba de indices ----------*/
        	LogSISPE.getLog().info("INDICES DETALLE");
        	for(Iterator it = indicesDetallePedido.iterator();it.hasNext();){
        		LogSISPE.getLog().info("INDICE: {}",it.next());
        	}
        	LogSISPE.getLog().info("---------------");
        	for(int i=0;i<detallePedido.size();i++){
        		VistaDetallePedidoDTO v = (VistaDetallePedidoDTO)detallePedido.get(i);
        		Collection e = v.getIndicesEntregas();
        		LogSISPE.getLog().info("DETALLE {}",i);
        		if(e!=null)
        			for (Iterator it = e.iterator();it.hasNext();)
        				LogSISPE.getLog().info("INDICE ENTREGA: {}",it.next());
        		else
        			LogSISPE.getLog().info("COLECCION NULA");
        	}
        	/*-----------------------------------*/
        }
        else //no se escogi\u00F3 ninguna entrega
        {
        	vistaPedidoDTO.setNpSeleccionados(null);
        	vistaPedidoDTO.setNpEstadoVistaPedido(null);
        	boolean seElimino = indicesPedidos.remove(indiceSeleccionado);
        	LogSISPE.getLog().info("\u00BFSE ELIMINO EL INDICE? {}",seElimino);

        	/*------ prueba de indices ----------*/
        	LogSISPE.getLog().info("PRUEBA DE INDICES DETALLE");
        	for(Iterator it = indicesDetallePedido.iterator();it.hasNext();){
        		LogSISPE.getLog().info("INDICE: {}",it.next());
        	}

        	for(int i=0;i<detallePedido.size();i++){
        		VistaDetallePedidoDTO v = (VistaDetallePedidoDTO)detallePedido.get(i);
        		Collection e = v.getIndicesEntregas();
        		LogSISPE.getLog().info("DETALLE {}",i);
        		if(e!=null)
        			for (Iterator it = e.iterator();it.hasNext();)
        				LogSISPE.getLog().info("INDICE ENTREGA: {}",it.next());
        		else
        			LogSISPE.getLog().info("COLECCION NULA");
        	}
        	/*-----------------------------------*/
        }
        vistaPedidoDTO.setVistaDetallesPedidos(detallePedido);
        session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
        
      }
      /*-------------------- si se escogi\u00F3 el link para ver el detalle de  alg\u00FAn pedido ---------------------*/
      else if(formulario.getBotonCancelarSeleccion()!=null){ 
  		//obtengo el total de archivos Adjuntos
  		Collection<DefArchivoEntregaDTO> colTotalFiles = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
  		if(colTotalFiles != null && !colTotalFiles.isEmpty()){
  			LogSISPE.getLog().info("size files ->"+colTotalFiles.size());
  			CotizacionReservacionUtil.instanciarVentanaAdvertenciaEliminacion(request, accion);
  		}else{
  			session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos")); 
  		}
      }
      else if(request.getParameter("aceptaCancelacionFotos") != null){
    	  Collection<DefArchivoEntregaDTO> colTotalFiles = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
    	  CotizacionReservacionUtil.verificarArchivoFotosEliminar(colTotalFiles);
    	  session.removeAttribute(SessionManagerSISPE.TOTAL_COL_ARCHIVO_FOTO);
    	  session.removeAttribute(SessionManagerSISPE.POPUP);
    	  session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
      }
      /*-------------------------------------- si se escogi\u00F3 el boton Despachar -----------------------------*/
      else if (formulario.getBotonDespachar() != null || request.getParameter("enviarNotificacion") != null || request.getParameter("cancelarNotificacion") != null) 
      {
        ArrayList<VistaPedidoDTO> pedidosPorDespachar = (ArrayList<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorDespachar");
        Collection<String> indicesPedidos = (Collection<String>)session.getAttribute("ec.com.smx.sic.sispe.despacho.indicesPedidos");
        
        if(indicesPedidos!=null && !indicesPedidos.isEmpty())
        {
          try{
        	  
        	  Boolean enviarNotificacionLocal = null;
        	  if(request.getParameter("enviarNotificacion") != null){
        		  enviarNotificacionLocal = true;
        		  request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
        	  }if(request.getParameter("cancelarNotificacion") != null){
        		  enviarNotificacionLocal = false;
        		  request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
        	  }
        	  
        	  if(enviarNotificacionLocal == null){
        		  //se instancia el popUp
        		  instanciarPopUpEnvioNotificacionEntregaLocal(request);
        		  return mapping.findForward(salida);
        	  }
    	    
        	//obtiene par\u00E1metro Activacion de verificaci\u00F3n de mails.
			ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.validacionEnvioMailsPosiblesFuncionariosResponsables", request);
			Integer parametroActivacion = Integer.parseInt(parametroDTO.getValorParametro());
			boolean mostrarEnvioMail = false;
			String accionMail = "despachos";
			LogSISPE.getLog().info("Valor Parametro de Activacion->{}",parametroActivacion);
        	//se iteran los pedidos para obtener los descuentos de cada uno
			
			for(String indiceActual : indicesPedidos){
            	
				int indice = Integer.parseInt(indiceActual);
            	///////////////////////INICIO Proceso que verifica el envio de mails cuando hay entregas a domicilio y el Responsable es Bodega
				if(parametroActivacion == Integer.parseInt(estadoActivo)){
					//obtengo el codigo de pedido despachado de la vistaDTO
					VistaPedidoDTO vistaDTO = (VistaPedidoDTO)pedidosPorDespachar.get(indice);
					vistaDTO.setNpEnviarNotificacionDespacho(enviarNotificacionLocal);
					mostrarEnvioMail =  SessionManagerSISPE.getServicioClienteServicio().transEnvioMailResponsableDomicilioBodega(vistaDTO.getId().getCodigoPedido(),accionMail);
					//verifico si se envio el mail o no
					LogSISPE.getLog().info("Valor del Boolean mostrarEnvioMail->{}",mostrarEnvioMail);	
				}else{
					LogSISPE.getLog().info("Valor del Parametro EnvioMailsFuncionariosEntregaDomicilio Desactivado");
				}
				/////////////////////////////////////////////FIN////////////////////////////////////////////////////////////////////////////
            	
            	//se llama a la funci\u00F3n que carga los descuentos del pedido
				//esto es necesario para el correcto registro de los descuentos en caso de que existan
				WebSISPEUtil.obtenerDescuentosEstadoPedido((VistaPedidoDTO)pedidosPorDespachar.get(indice),request,Boolean.FALSE);
            }
            
           // SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId();
            
            SessionManagerSISPE.getServicioClienteServicio().transProcesarDespacho(pedidosPorDespachar,indicesPedidos);
            
            //Envio de mail a los locales informandoles que se ha subido fotos por despacho pedido
            Collection<DefArchivoEntregaDTO> colArchivos = (ArrayList<DefArchivoEntregaDTO>)session.getAttribute(SessionManagerSISPE.PEDIDO_TOTAL_COL_ARCHIVO_FOTO);
            if(colArchivos != null && !colArchivos.isEmpty()){
            	boolean envioMails = SessionManagerSISPE.getServicioClienteServicio().transEnvioMailInfolLocalArchivo(colArchivos);
            	if(envioMails){
            		LogSISPE.getLog().info("---Se enviaron los mails a los locales de archivos fotos--");
            	}else{
            		LogSISPE.getLog().info("---No se enviaron los mails a los locales de archivos fotos--");
            	}
            }

            //SE MUESTRA EL MENSAJE DE EXITO
            success.add("exitoRegistro",new ActionMessage("message.exito.registro","El despacho"));

          	//se llama al m\u00E9todo que realiza la b\u00FAsqueda
          	this.buscar(formulario, session, request, beanSession, null, null);
            
            //se elimina la lista de pedidos despachados de sesi\u00F3n
            session.removeAttribute("ec.com.smx.sic.sispe.pedidosDespachados");
            
          }catch(SISPEException ex){
            //el servicio Obtener datos ha fallado
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
            errors.add("despacho", new ActionMessage("errors.llamadaServicio.registrarDatos","el Despacho"));
            errors.add("errorSISPE", new ActionMessage("errors.SISPEException",ex.getMessage()));
          }

        }else{
          errors.add("indicesVacios",new ActionMessage("errors.seleccion.pedidos","el despacho"));
        }

        salida = "desplegar";
      }
      
      /*IOnofre.-------- si se acept\u00F3 los despachos seleccionados para impresion ---------------------------------*/
      else if(request.getParameter("botonAceptar")!=null){

    		//se obtiene el indice del pedido seleccionado
    		String indiceSeleccionado = beanSession.getIndiceRegistro();
    		//se obtiene de sesi\u00F3n la colecci\u00F3n de indices guardados hasta el momento de los pedidos
    		Collection indicesPedidos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.despacho.indicesPedidos");
    		//se obtiene el pedido que se seleccion\u00F3
    		VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado");
    		
    		List<EntregaPedidoDTO> entregasImprimirCol = (List<EntregaPedidoDTO>) session.getAttribute(IMPRIMIR_ETIQUETAS);
    	   
    		if(CollectionUtils.isEmpty(entregasImprimirCol)){
    			entregasImprimirCol = new ArrayList<EntregaPedidoDTO>();
    		}
    		
    		//se crea la colecci\u00F3n de indices de los detalles del pedido
    		Collection indicesDetallePedido = new ArrayList();
    		
    		//se obtiene el detalle de los pedidos
    		ArrayList<VistaDetallePedidoDTO> detallePedido = (ArrayList<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidos();
    		
    		String indiceAnteriorDetalle = ""; //almacena el indice anterior de cada detalle encontrado
    		int sizeEntregas = 0; //almacena la sumatoria total de entregas encontradas en los detalleSeleccionados
    		String [] escogidos = formulario.getOpSeleccionAlgunos();
    		if(escogidos!=null){	
    		Date date = new Date();	
    			//se itera el arreglo de los indices escogidos
    			for(int i=0;i<escogidos.length;i++){
    				String [] indices = escogidos[i].split("-");
    				LogSISPE.getLog().info(i+" INDICE ARTICULO: {}, INDICE ENTREGA: {}",indices[0],indices[1]);
    				//se obtiene el indice del art\u00EDculo que contiene las entregas que se seleccionaron
    				//este indice se encuentra en el primer arreglo de String: indices[0]
    				int indiceDetalleSel = Integer.parseInt(indices[0]);
    				int indiceEntregaSel = Integer.parseInt(indices[1]);
    				VistaDetallePedidoDTO vistaDetallePedidoDTO = (VistaDetallePedidoDTO)detallePedido.get(indiceDetalleSel);
    				
    				if(CollectionUtils.isNotEmpty(vistaDetallePedidoDTO.getEntregaDetallePedidoCol())){
    					List<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)vistaDetallePedidoDTO.getEntregaDetallePedidoCol();
    					EntregaDetallePedidoDTO entregaDetallePedidoDTO = entregas.get(indiceEntregaSel);
    					
    					EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO) SerializationUtils.clone(entregaDetallePedidoDTO.getEntregaPedidoDTO());
    					entregaDetallePedidoDTO.setCodigoArticulo(vistaDetallePedidoDTO.getArticuloDTO().getDescripcionArticulo());
    					LogSISPE.getLog().info("descripcion articulo "+entregaDetallePedidoDTO.getCodigoArticulo());
    					
    					Boolean entregaExistente = buscarEntregas(entregaPedidoDTO, entregasImprimirCol);
    					Boolean entregaDetalleExistente = buscarDetalleEntregas(entregaDetallePedidoDTO, entregasImprimirCol);
    					int pos = buscarPosicionEntregas(entregaPedidoDTO, entregasImprimirCol);
    					
    					if(entregaExistente && pos >= 0){
    						entregaPedidoDTO = entregasImprimirCol.get(pos);
    					}
    					
    					PedidoDTO pedidoDTO = new PedidoDTO();
    					//setear campos del pedidoDTO
    					
    					if(vistaPedidoDTO.getNombreEmpresa() != null){
    						pedidoDTO.setNpNombreEmpresa(vistaPedidoDTO.getNombreEmpresa());
    						pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombreContacto());
    						pedidoDTO.setNpTelefonoContacto(vistaPedidoDTO.getTelefonoContacto() != null ? vistaPedidoDTO.getTelefonoContacto() : vistaPedidoDTO.getTelefonoEmpresa());
    					}
    					if(vistaPedidoDTO.getNombrePersona() != null){
    						pedidoDTO.setNpNombreContacto(vistaPedidoDTO.getNombrePersona());
    						pedidoDTO.setNpTelefonoContacto(vistaPedidoDTO.getTelefonoPersona());
    					}   			
    					
//    	        			pedidoDTO.setNpTelefonoContacto(vistaPedidoDTO.getTelefonoContacto());
    					pedidoDTO.getId().setCodigoPedido(entregaDetallePedidoDTO.getCodigoPedido());
    					
    					EstadoPedidoDTO estadoPedidoDTO = new EstadoPedidoDTO();
    					//setear datos estadoPedidoDTO
    					estadoPedidoDTO.setLlaveContratoPOS(vistaPedidoDTO.getLlaveContratoPOS());
    					estadoPedidoDTO.setPedidoDTO(pedidoDTO);
    					
    					entregaPedidoDTO.setFechaRegistro(date);
    					
    					if(!entregaExistente && !entregaDetalleExistente){
    						entregaPedidoDTO.setEntregaDetallePedidoCol(new ArrayList<EntregaDetallePedidoDTO>());
    					}
    					
    					if(!entregaDetalleExistente){
    						entregaPedidoDTO.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
    					}else{
//    	        				entregaPedidoDTO.getEntregaDetallePedidoCol().add(entregaDetallePedidoDTO);
    						int posDetalle = buscarPosicionDetalleEntregas(entregaDetallePedidoDTO, entregasImprimirCol);
    						if(posDetalle >= 0){
    							List<EntregaDetallePedidoDTO> entregasDetalle = new ArrayList<EntregaDetallePedidoDTO>(entregaPedidoDTO.getEntregaDetallePedidoCol());
    							entregasDetalle.set(posDetalle, entregaDetallePedidoDTO);
    							entregaPedidoDTO.setEntregaDetallePedidoCol(entregasDetalle);
    						}
    					}
    					entregaPedidoDTO.setEstadoPedidoDTO(estadoPedidoDTO);
    					
    					if(!entregaExistente){
    						entregasImprimirCol.add(entregaPedidoDTO);
    					}else{
    						entregasImprimirCol.set(pos, entregaPedidoDTO);
    					}
    				}
    				//si el indice no consta en la lista se a\u00F1ade y se actualiza el tama\u00F1o total de las entregas
    				if(!indicesDetallePedido.contains(indices[0])){
    					indicesDetallePedido.add(indices[0]);
    					LogSISPE.getLog().info("SE GUARDO EN INDICES DEL DETALLE: {}",indices[0]);
    					vistaPedidoDTO.setIndicesVistaDetallesPedidos(indicesDetallePedido);
    				}
    				//se suman los tama\u00F1os de las entregas encontradas
    				if(!indiceAnteriorDetalle.equals(indices[0])){
    					indiceAnteriorDetalle=indices[0];
    					Collection<EntregaDetallePedidoDTO> entregas = vistaDetallePedidoDTO.getEntregaDetallePedidoCol();
    					int despachados = 0;
    					//se itera la colecci\u00F3n de entregas del detalle actual para determinar cuantas 
    					//tienen una fecha de despacho
    						for(EntregaDetallePedidoDTO entDetPedDTO:entregas){
    							if(entDetPedDTO.getFechaRegistroDespacho()!=null){
    								despachados++; //se suman las entregas con fecha de despacho
    							}
    						}


    					sizeEntregas = sizeEntregas + entregas.size() - despachados;
    				}
    				/*----------------- seccion entregas ------------------------*/
    				//creaci\u00F3n de la colecci\u00F3n de indices de entregas
    				Collection indicesEntrega = null;
    				if(vistaDetallePedidoDTO.getIndicesEntregas()!=null)
    					indicesEntrega = vistaDetallePedidoDTO.getIndicesEntregas();
    				else
    					indicesEntrega=new ArrayList();

    				if(!indicesEntrega.contains(indices[1])){
    					indicesEntrega.add(indices[1]);
    					LogSISPE.getLog().info("*** SE GUARDO EN INDICES DE LA ENTREGA: {}",indices[1]);
    					vistaDetallePedidoDTO.setIndicesEntregas(indicesEntrega);
    				}
    			}
    			session.setAttribute(IMPRIMIR_ETIQUETAS, entregasImprimirCol);
    			session.setAttribute(NUMERO_ENTREGAS, entregasImprimirCol.size());
    			
    			//se comparan los tama\u00F1os de las colecciones para determinar si fueron seleccionados 
            	//todos los beneficiarios
            	LogSISPE.getLog().info("sizeDetallePedido: {}",entregasImprimirCol.size());
            	//LogSISPE.getLog().info("indicesDetallePedido.size(): "+indicesDetallePedido.size());
            	LogSISPE.getLog().info("--------------------------------------");
            	if(indicesDetallePedido.size() == entregasImprimirCol.size()){
            		LogSISPE.getLog().info("sizeEntregas: {}",sizeEntregas);
            		LogSISPE.getLog().info("escogidos.length: {}",escogidos.length);
            		//si la cantidad de escogidos es igual a la cantidad de beneficiarios encontrados
            		if(escogidos.length == sizeEntregas){
            			//se despach\u00F3 todo el pedido
            			vistaPedidoDTO.setNpEstadoVistaPedido(estadoActivo); 
            			LogSISPE.getLog().info("SE SELECCIONO TODO");
            		}
            		else
            			// se despach\u00F3 parte del pedido
            			vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); 
            	}else
            		// se despach\u00F3 parte del pedido
            		vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); 

    			
    			//si el indice seleccionado no esta en la colecci\u00F3n se a\u00F1ade
    			if(!indicesPedidos.contains(indiceSeleccionado))
    				indicesPedidos.add(indiceSeleccionado);

    			//finalmente se almacena el arreglo de String que contiene los valores que se chequearon en el 
    			//multiBoxs de beneficiarios, para recuperarlos cuando se quiera editar la entrega de un pedido.
    			vistaPedidoDTO.setNpSeleccionados(escogidos);

    			/*------ prueba de indices ----------*/
    			LogSISPE.getLog().info("INDICES DETALLE");
    			for(Iterator it = indicesDetallePedido.iterator();it.hasNext();){
    				LogSISPE.getLog().info("INDICE: {}",it.next());
    			}
    			LogSISPE.getLog().info("---------------");
    			for(int i=0;i<detallePedido.size();i++){
    				VistaDetallePedidoDTO v = (VistaDetallePedidoDTO)detallePedido.get(i);
    				Collection e = v.getIndicesEntregas();
    				LogSISPE.getLog().info("DETALLE {}",i);
    				if(e!=null)
    					for (Iterator it = e.iterator();it.hasNext();)
    						LogSISPE.getLog().info("INDICE ENTREGA: {}",it.next());
    				else
    					LogSISPE.getLog().info("COLECCION NULA");
    			}
    			
    		}
    		
    		else //no se escogi\u00F3 ninguna entrega
    		{
    			vistaPedidoDTO.setNpSeleccionados(null);
    			vistaPedidoDTO.setNpEstadoVistaPedido(null); 
    			session.removeAttribute(NUMERO_ENTREGAS);
    			boolean seElimino = indicesPedidos.remove(indiceSeleccionado);
    			LogSISPE.getLog().info("\u00BFSE ELIMINO EL INDICE? {}",seElimino);
    			
    			/*------ prueba de indices ----------*/
    			LogSISPE.getLog().info("PRUEBA DE INDICES DETALLE");
    			for(Iterator it = indicesDetallePedido.iterator();it.hasNext();){
    				LogSISPE.getLog().info("INDICE: {}",it.next());
    			}

    			for(int i=0;i<detallePedido.size();i++){
    				VistaDetallePedidoDTO v = (VistaDetallePedidoDTO)detallePedido.get(i);
    				Collection e = v.getIndicesEntregas();
    				LogSISPE.getLog().info("DETALLE {}",i);
    				if(e!=null)
    					for (Iterator it = e.iterator();it.hasNext();)
    						LogSISPE.getLog().info("INDICE ENTREGA: {}",it.next());
    				else
    					LogSISPE.getLog().info("COLECCION NULA");
    			}      	
    		}
    		vistaPedidoDTO.setVistaDetallesPedidos(detallePedido);
    		session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.imprimirEtiquetas"));       
    	  
      }
      else if(request.getParameter("botonVolverBusquedaEtiquetas") != null){  
    	  LogSISPE.getLog().info("retorna a la pantalla de busqueda de reservas para la impresion de etiquetas");
    	  List <EntregaPedidoDTO> entregaEliminadaActualCol = (List<EntregaPedidoDTO>) session.getAttribute(ENTREGA_ELIMINADA_ACTUAL);
    	  if(entregaEliminadaActualCol != null){
	    	  List<EntregaPedidoDTO> entregasImprimirCol = (List<EntregaPedidoDTO>) session.getAttribute(IMPRIMIR_ETIQUETAS);
	    	  entregasImprimirCol.addAll(entregaEliminadaActualCol);
    	  }
    	  session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.imprimirEtiquetas"));
      }
      
      /*---------------------------------Muestra los pedidos producidos por omisi\u00F3n---------------------*/
      else{
    	  
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
      	session.setAttribute(SessionManagerSISPE.TITULO_VENTANA, "Despacho de pedidos");
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagadoLiquidado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"));
      	session.setAttribute("ec.com.smx.sic.sispe.validacion.pagoTotal.despacho", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoEntregado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"));
      	session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoReservado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"));
      	
      	//se guarda la acci\u00F3n en la sesi\u00F3n
      	session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho"));
      	formulario.setEtiquetaFechas("Fecha de Despacho");

      	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de despacho");

      	//Iniciar Tabs
      	PaginaTab tabsDespachos = new PaginaTab("despachoPedido", "desplegar", 0, 455, request);
      	Tab tabPedidosPorDespachar = new Tab("Por despachar", "despachoPedido", "/servicioCliente/despachoPedido/porDespachar.jsp", true);
      	Tab tabPedidosDespachados = new Tab("Despachados", "despachoPedido", "/servicioCliente/despachoPedido/despachados.jsp", false);
      	//IOnofre. Nuevo tab para la impresion de etiquetas
      	Tab tabImprimirEtiquetas = new Tab("Imprimir etiquetas", "despachoPedido", "/servicioCliente/despachoPedido/imprimirEtiquetas.jsp", false);
      	tabsDespachos.addTab(tabPedidosPorDespachar);
      	tabsDespachos.addTab(tabPedidosDespachados);
      	tabsDespachos.addTab(tabImprimirEtiquetas);
      	
      	beanSession.setPaginaTab(tabsDespachos);
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
    
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);
	if(errors.size()>0){
		
		if(beanSession.getPaginaTab().esTabSeleccionado(2)){
      		LogSISPE.getLog().info("existe algun error al tratar de imprimir el reporte");
      	}else{
      		this.buscar(formulario, session, request, beanSession, infos, errors);
      	}
	}
    return mapping.findForward(salida);
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
  private void buscar(DespachosEntregasForm formulario, HttpSession session, HttpServletRequest request, BeanSession beanSession,
  		ActionMessages infos, ActionMessages errors) throws Exception{
    //se obtiene la lista de art\u00EDculos de sesi\u00F3n
    Collection pedidosEnDespachos = new ArrayList();
    
    //BORRAR ------------------------
	LogSISPE.getLog().info("Llama a m\u00E9todo x5");
	// -------------------------------
	
    VistaPedidoDTO consultaVistaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);


    //se asigna el usuario para el filtro
    consultaVistaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
    
    consultaVistaPedidoDTO.getId().setCodigoEstado((ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PRODUCIDO).concat(ConstantesGenerales.CARACTER_SEPARACION)
    	.concat((String)session.getAttribute("ec.com.smx.sic.sispe.pedido.estadoReservado")));
    
    //se verifica cual es la secci\u00F3n que est\u00E1 cargada
    String tab = (String)session.getAttribute("ec.com.smx.sic.sispe.seccionPagina");
    if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"))){
    	//consultaVistaPedidoDTO.getId().setCodigoEstado((String)session.getAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado"));
    	
    	consultaVistaPedidoDTO.getId().setCodigoEstado(((String)session.getAttribute("ec.com.smx.sic.sispe.pedido.estadoDespachado")).concat(ConstantesGenerales.CARACTER_SEPARACION)
    			.concat((String)session.getAttribute("ec.com.smx.sic.sispe.pedido.estadoEntregado")));
    	
    	consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));
    	consultaVistaPedidoDTO.setEtapaEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.etapa.finalizado"));
    }

    //se guardan las variables de b\u00FAsqueda para utilizarlas posteriormente
    beanSession.setCodigoLocal(consultaVistaPedidoDTO.getId().getCodigoAreaTrabajo());//se carga valor de codigo area de trabajo en codigo local
    beanSession.setCodigoPedido(consultaVistaPedidoDTO.getId().getCodigoPedido());
    beanSession.setCodigoReservacion(consultaVistaPedidoDTO.getLlaveContratoPOS());
//  beanSession.setCedulaContacto(consultaVistaPedidoDTO.getCedulaContacto());
//  beanSession.setNombreContacto(consultaVistaPedidoDTO.getNombreContacto());
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
    
  	//se guarda las fechas inicial y final de entrega para los pedidos
  	beanSession.setFechaInicial(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoInicial());
  	beanSession.setFechaFinal(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoFinal());
  	
    //se blanquea la colecci\u00F3n de datos antes de actualizar la lista
    formulario.setDatos(null);
    
    try{
    	//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
    	pedidosEnDespachos = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosParaDespachar(consultaVistaPedidoDTO);
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
    		LogSISPE.getLog().info("SE REALIZA LA PAGINACION");
    		int size= pedidosEnDespachos.size();
    		int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
    		int start= 0;
    		formulario.setStart(String.valueOf(start));
    		formulario.setRange(String.valueOf(range));
    		formulario.setSize(String.valueOf(size));

    		Collection datosSub = Util.obtenerSubCollection(pedidosEnDespachos,start, start + range > size ? size : start+range);
    		formulario.setDatos(datosSub);
    		session.setAttribute(COL_DATOS_REPORTE, datosSub);
    		beanSession.setInicioPaginacion(String.valueOf(start));

    	}else if(infos != null){
    		if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion")))
    			infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos Despachados"));
    		else
    			infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos por Despachar"));
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
   * 
   * @param request
   * @param vistaReporteGeneralDTO
   */
  @SuppressWarnings("deprecation")
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
	  
  public static void instanciarPopUpEnvioNotificacionEntregaLocal(HttpServletRequest request){

		//se crea la ventana para preguntar si se va enviar notoficacion del despacho al local
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Notificaci\u00F3n al local");
		popUp.setPreguntaVentana("\u00BFDesea enviar un mail al(los) local(es), informando que ya se realiz\u00F3 el despacho?");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setValorOK("requestAjax('despachoPedido.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'enviarNotificacion=ok'});");
		popUp.setValorCANCEL("requestAjax('despachoPedido.do', ['pregunta','div_pagina','mensajes','div_datosCliente'], {parameters:'cancelarNotificacion=ok'});");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
//		popUp.setContenidoVentana("/servicioCliente/cotizarRecotizarReservar/popUpPedidoNoReservado.jsp");
		popUp.setAncho(Double.valueOf(50));
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		
		//se guarda la ventana
		request.getSession().setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	
	}
  
  	public static void consultarPedidosDespachadosCD(DespachosEntregasForm formulario, HttpServletRequest request, BeanSession beanSession,
	  		ActionMessages infos, ActionMessages errors) throws Exception{
	    //se obtiene la lista de art\u00EDculos de sesi\u00F3n
	    Collection<VistaPedidoDTO> pedidosEnDespachos = new ArrayList<VistaPedidoDTO>();
	    
	    HttpSession session = request.getSession();	
	    VistaPedidoDTO consultaVistaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
	    String caraterSeparacion = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterSeparacion");
	    
    	consultaVistaPedidoDTO.getId().setCodigoEstado((String)ConstantesGenerales.CODIGO_ESTADO_PEDIDO_DESPACHADO
    			.concat(caraterSeparacion+ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ENTREGADO));
    	consultaVistaPedidoDTO.setEstadoActual(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.afirmacion"));

	    //se asigna el usuario para el filtro
	    consultaVistaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
	    consultaVistaPedidoDTO.setNpObtenerEntregasPedidos(false);
	    consultaVistaPedidoDTO.setNpObtenerReservasDomicio(true);
	    consultaVistaPedidoDTO.setNpCiudadObtenerReservas(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entrega.ciudadesDomicilio"));

	    //se guardan las variables de b\u00FAsqueda para utilizarlas posteriormente
	    beanSession.setCodigoLocal(consultaVistaPedidoDTO.getId().getCodigoAreaTrabajo());//se carga valor de codigo area de trabajo en codigo local
	    beanSession.setCodigoPedido(consultaVistaPedidoDTO.getId().getCodigoPedido());
	    beanSession.setCodigoReservacion(consultaVistaPedidoDTO.getLlaveContratoPOS());
	//  beanSession.setCedulaContacto(consultaVistaPedidoDTO.getCedulaContacto());
	//  beanSession.setNombreContacto(consultaVistaPedidoDTO.getNombreContacto());
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
	    
	  	//se guarda las fechas inicial y final de entrega para los pedidos
	  	beanSession.setFechaInicial(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoInicial());
	  	beanSession.setFechaFinal(consultaVistaPedidoDTO.getNpPrimeraFechaDespachoFinal());
	  	
	    //se blanquea la colecci\u00F3n de datos antes de actualizar la lista
	    formulario.setDatos(null);
	    
	    try{
	    	//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
	    	pedidosEnDespachos = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaVistaPedidoDTO);
	    	    	
	    	//se realiza la paginaci\u00F3n si la colecci\u00F3n tiene elementos
	    	if(pedidosEnDespachos!=null && !pedidosEnDespachos.isEmpty()){
	    		LogSISPE.getLog().info("SE REALIZA LA PAGINACION");
	    		//se guarda el listado de pedidos despachados
	    		session.setAttribute("ec.com.smx.sic.sispe.pedidosDespachados",pedidosEnDespachos);
	    		int size= pedidosEnDespachos.size();
	    		int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
	    		int start= 0;
	    		formulario.setStart(String.valueOf(start));
	    		formulario.setRange(String.valueOf(range));
	    		formulario.setSize(String.valueOf(size));

	    		Collection<VistaPedidoDTO> datosSub = Util.obtenerSubCollection(pedidosEnDespachos,start, start + range > size ? size : start+range);
	    		formulario.setDatos(datosSub);
	    		session.setAttribute(COL_DATOS_REPORTE, datosSub);
	    		beanSession.setInicioPaginacion(String.valueOf(start));

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
  	 * Busca en una coleccion si hay entregas existentes
  	 * @param entregaPedidoDTO
  	 * @param entregasImprimirCol
  	 * @return 
  	 * @author ionofre
  	 */
  	public static boolean buscarEntregas(EntregaPedidoDTO entregaPedidoDTO, List<EntregaPedidoDTO> entregasImprimirCol){
  		
  		if(CollectionUtils.isNotEmpty(entregasImprimirCol)){
  			
  			for(EntregaPedidoDTO entPedDTO : entregasImprimirCol){
  	  			if(entregaPedidoDTO.getId().getCodigoEntregaPedido().longValue() == entPedDTO.getId().getCodigoEntregaPedido().longValue()){
  	  				return true;
  	  			}
  	  		}
  		}
  		return false;
  	}
  	
  	public static int buscarPosicionEntregas(EntregaPedidoDTO entregaPedidoDTO, List<EntregaPedidoDTO> entregasImprimirCol){
  		
  		int pos = 0;
  		if(CollectionUtils.isNotEmpty(entregasImprimirCol)){
  			
  			for(EntregaPedidoDTO entPedDTO : entregasImprimirCol){
  	  			if(entregaPedidoDTO.getId().getCodigoEntregaPedido().longValue() == entPedDTO.getId().getCodigoEntregaPedido().longValue()){
  	  				return pos;
  	  			}
  	  			pos++;
  	  		}
  		}
  		return -1;
  	}
  	
  	/**
  	 * Busca en una coleccion si hay detalles de entregas existentes
  	 * @param entregaDetallePedidoDTO
  	 * @param entregasImprimirCol
  	 * @return
  	 * @author ionofre
  	 */
  	public static boolean buscarDetalleEntregas(EntregaDetallePedidoDTO entregaDetallePedidoDTO, List<EntregaPedidoDTO> entregasImprimirCol){
  		
  		if(CollectionUtils.isNotEmpty(entregasImprimirCol)){
  			
  			for(EntregaPedidoDTO entPedDTO : entregasImprimirCol){
  				if(CollectionUtils.isNotEmpty(entPedDTO.getEntregaDetallePedidoCol())){
  					for(EntregaDetallePedidoDTO entDetPedDTO : entPedDTO.getEntregaDetallePedidoCol()){
  						if(entDetPedDTO.getId().getCodigoDetalleEntregaPedido().longValue() == entregaDetallePedidoDTO.getId().getCodigoDetalleEntregaPedido().longValue()){
  		  	  				return true;
  		  	  			}
  					}
  				}
  	  		}
  		}
  		return false;
  	}
  	
  	/**
  	 * 
  	 * @param entregaPedidoDTO
  	 * @param entregasImprimirCol
  	 * @return
  	 * @author eonofre
  	 */
  	public static void eliminaEntregasImprimirReporte(HttpServletRequest request, VistaPedidoDTO vistaPedidoDTO){
  		
  		HttpSession session = request.getSession();
  		List<EntregaPedidoDTO> entregasImprimirCol = (List<EntregaPedidoDTO>) session.getAttribute(IMPRIMIR_ETIQUETAS);
  		List<EntregaPedidoDTO> entregasImprimirColAux = new ArrayList<EntregaPedidoDTO>();
  		if(CollectionUtils.isNotEmpty(entregasImprimirCol)){
  			
  			for(EntregaPedidoDTO entPedDTO : entregasImprimirCol){
  	  			if(vistaPedidoDTO.getId().getCodigoPedido().equals(entPedDTO.getCodigoPedido())){
  	  				entregasImprimirColAux.add(entPedDTO);
  	  			}
  	  		}
  			entregasImprimirCol.removeAll(entregasImprimirColAux);
			session.setAttribute(ENTREGA_ELIMINADA_ACTUAL, entregasImprimirColAux);

  		}
  	}
  	
  	public static int buscarPosicionDetalleEntregas(EntregaDetallePedidoDTO entregaDetallePedidoDTO, List<EntregaPedidoDTO> entregasImprimirCol){
  		
  		if(CollectionUtils.isNotEmpty(entregasImprimirCol)){
  			
  			for(EntregaPedidoDTO entPedDTO : entregasImprimirCol){
  				int pos = 0;
  				if(CollectionUtils.isNotEmpty(entPedDTO.getEntregaDetallePedidoCol())){
  					for(EntregaDetallePedidoDTO entDetPedDTO : entPedDTO.getEntregaDetallePedidoCol()){
  						if(entDetPedDTO.getId().getCodigoDetalleEntregaPedido().longValue() == entregaDetallePedidoDTO.getId().getCodigoDetalleEntregaPedido().longValue()){
  		  	  				return pos;
  		  	  			}
  						pos++;
  					}
  				}
  	  		}
  		}
  		return -1;
  	}
}