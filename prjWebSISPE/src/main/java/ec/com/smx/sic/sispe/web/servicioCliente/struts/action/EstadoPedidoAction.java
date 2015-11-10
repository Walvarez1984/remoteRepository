/*
 * Clase EstadoPedidoAction.java
 * Creado el 07/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.ACEPTAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CANCELAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.COL_TIPOS_PEDIDO;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.SUBIR_ARCH_BENE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import ec.com.kruger.utilitario.dao.commons.enumeration.ComparatorTypeEnum;
import ec.com.smx.autorizaciones.dto.AutorizacionDTO;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.cliente.mdl.dto.sispe.TipoPedidoDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.ContactoUtil;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoID;
import ec.com.smx.sic.sispe.dto.DetalleEstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.DetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoAutorizacionDTO;
import ec.com.smx.sic.sispe.dto.EstadoPedidoDTO;
import ec.com.smx.sic.sispe.dto.EstadoSICDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p> 
 * Esta clase permite realizar la b\u00FAsqueda de pedidos por diferentes par\u00E1metros de b\u00FAsqueda:
 * <ul>
 * <li>N\u00FAMERO DE PEDIDO: Se listan todos los pedidos con ese c\u00F3digo en sus diferentes estados.</li>
 * <li>C\u00E9DULA DEL CONTACTO: Se listan todos los pedidos con esa c\u00E9dula del contacto en sus diferentes estados.</li>
 * <li>NOMBRE DEL CONTACTO: Se listan todos los pedidos con ese nombre del contacto en sus diferentes estados.</li>
 * <li>FECHA DE CREACI\u00F3N, INICIAL Y FINAL: Se listan todos los pedidos que tengan una fecha de creaci\u00F3n entre 
 * el rango inicial y final</li>
 * <li>ESTADO ACTUAL DEL PEDIDO: Se listan todos los pedidos que tengan como estado actual el escogido.</li>
 * </ul>
 * </p>
 * @author 	fmunoz, mbraganza
 * @version	2.0
 * @since	JSDK 1.4.2
 */
public class EstadoPedidoAction extends BaseAction
{
  private final String ORDEN_ASC = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.consultas.orden.ascendente");
  public static final String INDICE_PEDIDO_SELECCIONADO = "indicePedidoPrincipal";
  public static final String CODIGO_PEDIDO = "codigoPedido";
  public static final String FLAG = "archivoPedido";
  public static final String CODIGO_LOCAL = "codigoLocal";
  public static final String ACTIVO = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.activo");
  public static final String INACTIVO = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.inactivo");
  public static final  String SEPARADOR_TOKEN = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.caraterToken");	
  /**
   * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) 
   * (o lo redirige a otro componente web que podr\u00EDa crear)
   * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
   * (si la respuesta se ha completado <code>null</code>)
   * 
   * @param mapping			El mapeo utilizado para seleccionar esta instancia
   * @param form			El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
   *          				campos
   * @param request 		La petici&oacute;n que estamos procesando
   * @param response		La respuesta HTTP que se genera
   * @return ActionForward	Acci&oacuten o p&acute;gina jsp donde se redirige el control 
   * @throws Exception
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)throws Exception{
  	
    ActionMessages infos = new ActionMessages();
    ActionMessages errors = new ActionMessages();
    
    HttpSession session = request.getSession();
    ListadoPedidosForm formulario = (ListadoPedidosForm)form;
    BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
    //se la referencia del formulario CotizarReservarForm
	//CotizarReservarForm formularioCotizar = (CotizarReservarForm)form;
    String salida = "desplegar";
    String accionEstado= "estadoPedido.do";
    session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
    session.removeAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL.concat("XLS"));
    try
    {
      /*-------------------------- cuando se da clic en los campos de paginaci\u00F3n --------------------------------*/
      if(request.getParameter("range")!=null || request.getParameter("start")!=null)
      {
      	LogSISPE.getLog().info("ENTRO A LA PAGINACION link");
      	
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
					
					ListadoPedidosAction.verificarAutorizacionesPedido(colVistaPedidoDTO);
					//verifica si tiene aut. de dscto. variable o stock
					verificarAutorizaciones(colVistaPedidoDTO);
					
					//se asignan las variables de paginaci\u00F3n
					formulario.setStart(start);
					formulario.setRange(request.getParameter("range"));
					formulario.setSize(String.valueOf(tamano));
					formulario.setDatos(colVistaPedidoDTO);
					
          //se guarda la colecci\u00F3n general
          session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
	        //se guarda en sesi\u00F3n el indice de la paginaci\u00F3n
	        session.setAttribute(ListadoPedidosForm.INDICE_PAGINACION, start);
				}
      }
      /*--------------------------------- cuando se busca un pedido --------------------------------------*/
      else if(request.getParameter("buscar") != null)
      {
    	LogSISPE.getLog().info("Buscando pedidos");
    	  //colecci\u00F3n que almacena los pedidos buscados
        Collection <VistaPedidoDTO> colVistaPedidoDTO = new ArrayList <VistaPedidoDTO>();
                       	        	       
        //DTO que contiene los pedidos a buscar
        VistaPedidoDTO consultaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
        //se verifica si existe un tipo de pedido especial para filtrar la b\u00FAsqueda
        //if(session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER)!=null){
        //	consultaPedidoDTO.setCodigoTipoPedido((String)session.getAttribute(WebSISPEUtil.COD_TIPO_PEDIDO_ESP_USER));
        //}
        
        //se asignan los campos de ordenamiento
  			String [][] camposOrden = new String [][]{{"codigoAreaTrabajo",ORDEN_ASC},{"codigoPedido",ORDEN_ASC},{"fechaInicialEstado", ORDEN_ASC}};
  			consultaPedidoDTO.setNpCamposOrden(camposOrden);
  			//se incializa la colecci\u00F3n
  			formulario.setDatos(null);
  			
        try
        {
        	
        	//se obtiene el rol de los usuarios de compras
    		ParametroDTO parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.rolUsuariosCompras", request);
    		if(parametroDTO!=null && parametroDTO.getValorParametro()!=null){
	    		String rolCompras = parametroDTO.getValorParametro();
	    		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol().equals(rolCompras)){//VistaEntidadResponsableDTO
	    			//estados del articulo
	    			if(consultaPedidoDTO.getId().getCodigoEstado().equals("-1")){
		    			consultaPedidoDTO.getId().setCodigoEstado(
		    					new StringBuilder()
		    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
		    					.append(",")
		    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
		    					.append(",")
		    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
		    					.append(",")
		    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
		    					.toString()
		    			);
	    			}
	
	    			//Filtrar los pedidos por el usuario logueado actualmente
	    			consultaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
	    		}else{
	    			parametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoRolAdministradorBodega", request);
	        		String rolBodegaCanastos = parametroDTO.getValorParametro();
	        		//valida si es bodega de canastos
	        		if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol().equals(rolBodegaCanastos)){//VistaEntidadResponsableDTO
	        			//estados del articulo
		    			if(consultaPedidoDTO.getId().getCodigoEstado().equals("-1")){
			    			consultaPedidoDTO.getId().setCodigoEstado(
			    					new StringBuilder()
			    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
			    					.append(",")
			    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
			    					.append(",")
			    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
			    					.append(",")
			    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
			    					.append(",")
			    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))
			    					.toString()
			    			);
		    			}
		    			//Filtrar los pedidos por el usuario logueado actualmente
		    			//consultaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		    			//consultaPedidoDTO.setNpObtenerReservasDomicio(Boolean.TRUE);
		    			//consultaPedidoDTO.setNpCiudadObtenerReservas("ECU.UIO','ECU.GYE");
	        		}else{
	        			//SISPEADMBODTRA
	        			if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdRol().equals("SISPEADMBODTRA")){//VistaEntidadResponsableDTO
		        			//estados del articulo
			    			if(consultaPedidoDTO.getId().getCodigoEstado().equals("-1")){
				    			consultaPedidoDTO.getId().setCodigoEstado(
				    					new StringBuilder()
				    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))
				    					.append(",")
				    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.enProduccion"))
				    					.append(",")
				    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.producido"))
				    					.append(",")
				    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.despachado"))
				    					.append(",")
				    					.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"))
				    					.toString()
				    			);
			    			}
			    			//Filtrar los pedidos por el usuario logueado actualmente
			    			//consultaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
			    			consultaPedidoDTO.setNpObtenerReservasDomicio(Boolean.TRUE);
			    			consultaPedidoDTO.setNpCiudadObtenerReservas("ECU.GYE");
		        		}
	        		}
	    		}
    		}
    		
    		
        	
        	//metodo que trae el total de registros
        	Integer total = SessionManagerSISPE.getServicioClienteServicio().transObtenerTotalVistaPedido(consultaPedidoDTO);
        	session.setAttribute(SessionManagerSISPE.CANTIDAD_TOTAL_PEDIDOS, total);
        	LogSISPE.getLog().info("total de registros: {}",total);
        	
        	//se asignan los par\u00E1metros para la paginaci\u00F3n en la base de datos
        	int start = 0;
        	int range = Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
        	consultaPedidoDTO.setNpFirstResult(start);
        	consultaPedidoDTO.setNpMaxResult(range);
        	consultaPedidoDTO.setNpObtenerHistoricoModReservas(Boolean.TRUE);
        	
        	//obtiene solamente los registros en el rango establecido
//        	consultaPedidoDTO.setVistaDetallesPedidos(new ArrayList<VistaDetallePedidoDTO>());
        	colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
        	
          if(colVistaPedidoDTO==null || colVistaPedidoDTO.isEmpty()){
            infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos"));
          }else{
        	 
        	  for(VistaPedidoDTO vistaPedido:colVistaPedidoDTO){
        		  if(vistaPedido.getId().getCodigoEstado().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.estado.reservado"))){
        			 EstadoPedidoDTO estPed=new EstadoPedidoDTO();
        			 estPed.getId().setCodigoCompania(vistaPedido.getId().getCodigoCompania());
        			 estPed.getId().setCodigoPedido(vistaPedido.getId().getCodigoPedido());
        			 estPed.getId().setCodigoEstado(vistaPedido.getId().getCodigoEstado());
        			 estPed.getId().setSecuencialEstadoPedido(null);
        			 estPed.getId().setCodigoAreaTrabajo(null);
        			 Collection<EstadoPedidoDTO> colEstPed =SISPEFactory.getDataService().findObjects(estPed);
        			 Long cuenta=(long) colEstPed.size();
        			 if (cuenta>1|| vistaPedido.getCodigoPedidoRelacionado()!=null||(cuenta==1 && colEstPed.iterator().next().getFechaModificacion()!=null)){
        				 vistaPedido.setNpEsNuevaReserva(false);
        			 }else{
        				 vistaPedido.setNpEsNuevaReserva(true);
        			 }
        		  }
        	  }
        	  
//        	verificarAutorizaciones(colVistaPedidoDTO);
        	ListadoPedidosAction.verificarAutorizacionesPedido(colVistaPedidoDTO);
              
            LogSISPE.getLog().info("ENTRO A LA PAGINACION");
            formulario.setDatos(colVistaPedidoDTO);
            formulario.setStart(String.valueOf(start));
            formulario.setRange(String.valueOf(range));
            formulario.setSize(String.valueOf(total));
            
            //se guarda la colecci\u00F3n general
            session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL,colVistaPedidoDTO);
            //se guarda los parametros de la busqueda para la vistaPedido
            session.setAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO, consultaPedidoDTO);
            //se elimina la variable del indice de paginaci\u00F3n
            session.removeAttribute(ListadoPedidosForm.INDICE_PAGINACION);
            session.removeAttribute(DetalleEstadoPedidoAction.AUXILIAR_ATRAS_DETESTPED);
            
            //llamada a la funci\u00F3n que inicializa los datos de ordenamiento
            this.inicializarDatosOrdenamiento(request);
          }
        }catch(SISPEException ex){
          LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
          errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Pedidos"));
        }
        salida = "desplegar";
      }
      //accion del boton editar de la pantalla estado pedido
      else if(request.getParameter(ACEPTAR_ARCH_BENE) != null){
			//almacena el codigo del pedido que se envia desde la jsp estadopedido
    	  	String pedido = (String)request.getParameter(ACEPTAR_ARCH_BENE);
    	  	String codigoLocal = (String)request.getParameter("codigoLocal");
    	  	//Verifica si tiene el campo archivo beneficiario actico(1) o inactivo(0)
    	  	String campoArchivoBeneficiario = (String)request.getParameter("campoArchivo");   	  
    	  	session.setAttribute(FLAG, campoArchivoBeneficiario);
    	  	session.setAttribute(CODIGO_PEDIDO, pedido);
    	  	session.setAttribute(CODIGO_LOCAL, codigoLocal);
    	  	
    	  	LogSISPE.getLog().info("Entra a mostra la ventana para subir el archivo del beneficiario");
			//se construye la ventana de informacion de archivos del beneficiario y sus opciones(subir,eliminar)
			CotizacionReservacionUtil.instanciarVentanaSubirArchivoBeneficiario(request, accionEstado);
			CotizacionReservacionUtil.listarArchivos(request, pedido);
			salida = "desplegar";
      }
      /*-------------------- cuando se desea subir un archivo de configuracion del beneficiario ----------------------*/
	  else if(request.getParameter("ayuda")!=null && request.getParameter("ayuda").equals("adjuntarArchivo")) {				
		 
		  LogSISPE.getLog().info("Boton Aceptar---------");
		  String codPedido = (String)session.getAttribute(CODIGO_PEDIDO);
		  String codLocal = (String)(session.getAttribute(CODIGO_LOCAL));
		  String campoArchivoBeneficiario = (String)(session.getAttribute(FLAG));
		  
		  //si el campo del pedido archivoBenficiario esta inactivo lo actualiza en activo.
	  	  	if(campoArchivoBeneficiario != null && campoArchivoBeneficiario.equals(INACTIVO)) {
	  	  		LogSISPE.getLog().info("Este pedido no tiene el campo archivoBeneficiario(Activo)");
	  	  		PedidoDTO pedidoDTO = new PedidoDTO();
					PedidoDTO pedidoActualizadoDTO = new PedidoDTO();
					//seteo el codigo de pedido y codigoCompania
					pedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					pedidoDTO.getId().setCodigoPedido(codPedido);
					//obtengo el pedido actual
					ArrayList<PedidoDTO> pedidoActual = (ArrayList<PedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
					pedidoActualizadoDTO = pedidoActual.get(0);
					//seteo el valor en el campo archivoBaneficiario
					pedidoActualizadoDTO.setArchivoBeneficiario(ACTIVO);
					SessionManagerSISPE.getServicioClienteServicio().transActualizarPedido(pedidoActualizadoDTO);
	  	  	}
		  
		  boolean verificarArchivo = true;
		  LogSISPE.getLog().info("CodPedido->{}",codPedido);
		  LogSISPE.getLog().info("CodLocal->{}",codLocal);
		  	ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
		    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
			archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		    archivoPedidoDTO.setId(archivoPedidoID);
		    archivoPedidoDTO.setCodigoLocal(Integer.parseInt(codLocal));
		    archivoPedidoDTO.setCodigoPedido(codPedido);
		    archivoPedidoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
		    archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.Beneficiario"));
		    archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));
		    archivoPedidoDTO.setArchivo(formulario.getArchivo().getFileData());
		    archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		    archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		    session.removeAttribute(SessionManagerSISPE.ARCHIVO_DTO);
		    //verifica si el nuevo archivo no tiene el mismo nombre.
		    verificarArchivo = CotizacionReservacionUtil.validarArchivo(request, formulario.getArchivo().getFileName());
		    if(verificarArchivo){
		    	LogSISPE.getLog().info("Archivo Diferente");
		    	//se acepta el archivo del beneficiario
				CotizacionReservacionUtil.aceptarArchivoBeneficiario(request,archivoPedidoDTO);
		    } else {
		    	LogSISPE.getLog().info("Archivo Repetido");
		    	CotizacionReservacionUtil.instanciarVentanaAuxiliarArchivo(request, accionEstado);
		    }
		    	
			salida = "desplegar";
	  }
      //accion del boton reemplazar archivo existente.
	  else if(request.getParameter("reemplazarArchivo") != null){
			LogSISPE.getLog().info("Acepto el reeplazo del archivo");
			ArchivoPedidoDTO archivoDTO =(ArchivoPedidoDTO)session.getAttribute(SessionManagerSISPE.ARCHIVO_DTO);
			LogSISPE.getLog().info("Compania->{}",archivoDTO.getId().getCodigoCompania());
			LogSISPE.getLog().info("secuencial->{}",archivoDTO.getId().getSecuencialArchivoPedido());
			LogSISPE.getLog().info("local->{}",archivoDTO.getCodigoLocal());
			LogSISPE.getLog().info("codpedido->{}",archivoDTO.getCodigoPedido());
			LogSISPE.getLog().info("tama\u00F1o->{}",archivoDTO.getTamanioArchivo());
			LogSISPE.getLog().info("tipo->{}",archivoDTO.getTipoArchivo());
			LogSISPE.getLog().info("nombe->{}",archivoDTO.getNombreArchivo());
			//LogSISPE.getLog().info("bytes->{}",archivoDTO.getArchivo());
			
			LogSISPE.getLog().info("Nombre_Nuevo->{}",formulario.getArchivo().getFileName());
			//LogSISPE.getLog().info("Bytes Nuevo->{}",formulario.getArchivo().getFileData());
			LogSISPE.getLog().info("Tama\u00F1o Nuevo->{}",formulario.getArchivo().getFileSize());
			
			//setea el tama\u00F1o, bytes, nombre.
			archivoDTO.setNombreArchivo(formulario.getArchivo().getFileName());
			archivoDTO.setArchivo(formulario.getArchivo().getFileData());
			archivoDTO.setTamanioArchivo(Long.valueOf(formulario.getArchivo().getFileSize()));	
			//creo el nuevo archivo.
			CotizacionReservacionUtil.confirmarReemplazo(request, archivoDTO);
			//finalmente oculta el popUpAuxiliar.
			CotizacionReservacionUtil.cancelarReeplazoArchivo(request, accionEstado);
			salida = "desplegar";	
	  }
      /*-------- cuando se cancela el reemplazo del archivo existente----------*/
	  else if(request.getParameter("cancelarReemplazo") != null){
			//se cancela el archivo del beneficiario 
			CotizacionReservacionUtil.cancelarReeplazoArchivo(request, accionEstado);
			salida = "desplegar";
	  }
      //accion del icono eliminar archivo del popUp mostrarArchivoBeneficiario
	  else if(request.getParameter("eliminarArchivo") != null){
		  //almacena el codigo del pedido que se envia desde la jsp estadopedido
  	  		String pedido = (String)session.getAttribute(CODIGO_PEDIDO);
		  	Long secuencialArchivo = Long.valueOf(request.getParameter("eliminarArchivo"));
			//se cancela el archivo del beneficiario 
			LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",secuencialArchivo);
			LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",pedido);
			//session.removeAttribute(SessionManagerSISPE.POPUP);
			CotizacionReservacionUtil.eliminarArchivoBeneficiario(request, secuencialArchivo, pedido);
			salida = "desplegar";	
	  }
	  else if(request.getParameter("verArchivo") != null){
			LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
			CotizacionReservacionUtil.verArchivo(request, response);
	  }
      /*-------- cuando se cancela el la subida de un archivo de configuracion del beneficiario----------*/
	  else if(request.getParameter(CANCELAR_ARCH_BENE) != null){
			//se cancela el archivo del beneficiario 
			CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionEstado);
			salida = "desplegar";
	  }
	 /*-------------------- cuando se desea subir un archivo de configuracion del beneficiario ----------------------*/
	  else if(request.getParameter(SUBIR_ARCH_BENE) != null){
			//se cancela el archivo del beneficiario 
			LogSISPE.getLog().info("Se confirmo la subida del Archivo");
			//remueve la variable de session
			CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionEstado);
			salida = "desplegar";
	  }
      //obtener los pedidos relacionados
      else if(request.getParameter("pedidoRelacionado") != null){
      	VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)((List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL)).get(Integer.parseInt(request.getParameter("pedidoRelacionado")));
      	if(vistaPedidoDTO.getVistaPedidoDTOCol() == null)
      		vistaPedidoDTO.setVistaPedidoDTOCol(SessionManagerSISPE.getServicioClienteServicio().transObtenerHistoricoModificacionesReserva(vistaPedidoDTO,null));
      }
      //obtener el detalle de alg\u00FAn pedido relacionado
      else if(request.getParameter("indiceRelacionado")!=null){
      	VistaPedidoDTO pedidoPrincipal = (VistaPedidoDTO)((List)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL)).get(Integer.parseInt(request.getParameter("indicePrincipal")));
      	VistaPedidoDTO pedidoRelacionado = ((List<VistaPedidoDTO>)pedidoPrincipal.getVistaPedidoDTOCol()).get(Integer.parseInt(request.getParameter("indiceRelacionado")));
      	EstadoPedidoUtil.obtenerDetallesPedido(pedidoRelacionado, request);
      	ContactoUtil.cargarDatosPersonaEmpresa(request, pedidoRelacionado);
      	DetalleEstadoPedidoAction.obtenerEntregas(session,pedidoRelacionado.getVistaDetallesPedidosReporte());
		DetalleEstadoPedidoAction.obtenerRolesEnvioMail(request);
		
		PaginaTab tabDetallePedido= new PaginaTab("detalleEstadoPedido", "deplegar", 0,335, request);
		Tab tabDetallePedidoComun = new Tab("Detalle del pedido", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp", true);
		tabDetallePedido.addTab(tabDetallePedidoComun);	
		if(CollectionUtils.isNotEmpty((Collection<EntregaPedidoDTO>)session.getAttribute(DetalleEstadoPedidoAction.RESUMEN_ENTREGAS))){
			Tab tabDetalleEntregas = new Tab("Detalle entregas", "detalleEstadoPedido", "/servicioCliente/estadoPedido/detalleEntregas.jsp", false);
			tabDetallePedido.addTab(tabDetalleEntregas);	
		}
		beanSession.setPaginaTab(tabDetallePedido);
      	session.setAttribute(INDICE_PEDIDO_SELECCIONADO, request.getParameter("indicePrincipal"));
      	salida = "detallePedido";
      }
      //esta es otra forma de manejar la ventana emergente del detalle
			//crea una variable request para mostrar posteriormente una ventana con del detalle del pedido
      else if(request.getParameter("indicePedido")!=null){
				//se crea la variable
				request.setAttribute("indicePedidoSeleccionado", request.getParameter("indicePedido"));
      }
      //------------------- ordenar la colecci\u00F3n de datos ---------------------
      else if(request.getParameter("indiceOrdenar")!=null){
      	LogSISPE.getLog().info("Ordenar la tabla de datos");
      	int indiceColumna = Integer.parseInt(request.getParameter("indiceOrdenar"));
      	Collection colVistaPedidoDTO = (Collection)session.getAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL);
      	
      	if(colVistaPedidoDTO != null){
      		//se llama a la funci\u00F3n que realiza el ordenamiento
      		colVistaPedidoDTO = WebSISPEUtil.ordenarColeccionDatos(request, indiceColumna, colVistaPedidoDTO,null);
	      	formulario.setDatos(colVistaPedidoDTO);
      	}
      	//se actualiza la colecci\u00F3n en sesi\u00F3n
      	session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL, colVistaPedidoDTO);
      }
      //-----------------------generar reporte excel------------------------------
      else if(request.getParameter("ayuda") != null && request.getParameter("ayuda").equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel"))){
    	  LogSISPE.getLog().info("Se procede a presentar el reporte en formato de hoja de c\u00E1lculo...");
    		  
    	  //colecci\u00F3n que almacena los pedidos buscados
    	  Collection <VistaPedidoDTO> colVistaPedidoDTO = new ArrayList <VistaPedidoDTO>();
    	 
    	  //verificar si la variable de sesion no es nula
    	  if(session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO)!=null){
	    	  //DTO que contiene los pedidos a buscar
	    	  VistaPedidoDTO consultaPedidoDTO = ((VistaPedidoDTO)session.getAttribute(SessionManagerSISPE.CONSULTA_VISTAPEDIDO)).clone();
	    	  consultaPedidoDTO.setNpFirstResult(null);
	    	  consultaPedidoDTO.setNpMaxResult(null);
	
	    	  try{
	    		  //obtiene solamente los registros en el rango establecido
	    		  colVistaPedidoDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerVistaPedido(consultaPedidoDTO);
	    		  
	    		  //recorre la colecci\u00F3n obteniedo los pedidos relacionados
	    		  for(VistaPedidoDTO vistaPedidoDTO : colVistaPedidoDTO){
	    			  if(vistaPedidoDTO.getId().getCodigoEstado().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.anulado"))){
	    				  vistaPedidoDTO.setVistaPedidoDTOCol(SessionManagerSISPE.getServicioClienteServicio().transObtenerHistoricoModificacionesReserva(vistaPedidoDTO,null));
	    			  }
	    		  }
	
	    		  //se guarda la colecci\u00F3n general
	    		  session.setAttribute(SessionManagerSISPE.COLECCION_PEDIDOS_GENERAL.concat("XLS"), colVistaPedidoDTO);
	  
	    		  request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("estadoPedido", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));							
	    		  salida = "reporteEstadoPedidoXLS";
	  
	    	  }catch(SISPEException ex){
	    		  salida = "desplegar";
	    		  LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
	    		  errors.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.sinDatos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipo.excel.reportesExcel")));
	    	  }
    	  }else{
    		  salida = "desplegar";
    		  LogSISPE.getLog().info("No existen datos a exportar");
    		  infos.add("errorReporteGeneral", new ActionMessage("errors.exportarDatos.sinDatos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tipo.excel.reportesExcel")));
    	  }
      }
      //------------------- caso por omisi\u00F3n ---------------------
      else{
      	LogSISPE.getLog().info("Ingreso a caso por omisi\u00F3n x...");
      	//se eliminan las variables de sesi\u00F3n que comienzen con "ec.com"
      	String accionConsolidar=(String)session.getAttribute(SessionManagerSISPE.ACCION_ACTUAL);
      	LogSISPE.getLog().info("Accion estado PedidoAction {}",accionConsolidar);
      	//si la accionConsolidar viene con valor de consolidaci\u00F3n no debe de borrar ninguna variable de sesion
      	if(accionConsolidar==null){
      		SessionManagerSISPE.removeVarSession(request);
      	}else if(!accionConsolidar.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
      		SessionManagerSISPE.removeVarSession(request);
      		
      	}

      	//se consultan los estados de pago
      	EstadoSICDTO estadoSICDTO = new EstadoSICDTO();
      	estadoSICDTO.getId().setCodigoEstado(new StringBuilder()
	      	.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"))
	  		.append(",")
      		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"))
      		.append(",")
      		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.parcialmente"))
      		.append(",")
      		.append(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago")).toString());
      	session.setAttribute("ec.com.smx.sic.sispe.estadosPago", SessionManagerSISPE.getServicioClienteServicio().transObtenerEstado(estadoSICDTO));

      	//se consultan los tipos de pedido
      	TipoPedidoDTO tipoPedidoFiltro = new TipoPedidoDTO(Boolean.TRUE);
      	tipoPedidoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
      	session.setAttribute(COL_TIPOS_PEDIDO, SessionManagerSISPE.getServicioClienteServicio().transObtenerTipoPedido(tipoPedidoFiltro));
      	
      	String estadoActivo = (String)session.getAttribute(SessionManagerSISPE.ESTADO_ACTIVO);
      	String entidadLocal = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.local");
      	String entidadBodega = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega");
      	
      //se realiza la consulta de estados
      	EstadoSICDTO consultaEstadoDTO = new EstadoSICDTO(Boolean.TRUE);
      	consultaEstadoDTO.setContextoEstado(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoEstado.estadoNormal"));
      	consultaEstadoDTO.addCriteriaSearchParameter("id.codigoEstado", ComparatorTypeEnum.NOT_IN_COMPARATOR, new String[]{ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PENDIENTE, ConstantesGenerales.CODIGO_ESTADO_PEDIDO_ENVIADO_ORDEN_COMPRA, ConstantesGenerales.CODIGO_ESTADO_PEDIDO_PAGADO});
      	//Obtener datos de la colecci\u00F3n de estados, en la base de datos
      	Collection<EstadoSICDTO> estados = SISPEFactory.getDataService().findObjects(consultaEstadoDTO);

      	//se verifica si la entidad responsable es un local
      	if(!SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadLocal)){
      		//se obtienen los locales por ciudad
      		SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request);
      		
  				//primero se verifica si el rol del usuario logeado est\u00E1 configurado en los par\u00E1metros
  				//WebSISPEUtil.verificarUsuarioPedidoEspecial(SessionManagerSISPE.getCurrentEntidadResponsable(request), request);
       	}

      	//ASIGNACION DE LAS VARIABLES DE SESION NECESARIAS
      	session.setAttribute(SessionManagerSISPE.TITULO_VENTANA,"Estado de los pedidos");
      	//La primera vez se muestra el cuadro de texto de n\u00FAmero de pedido
      	session.setAttribute("ec.com.smx.sic.sispe.opcionBusqueda",
      			MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroPedido"));
      	//si viene de la accion consolidar no debe de hacer nada, debe quedarse con los valores anteriores de sesion
      	if(accionConsolidar==null){
      		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
	      			MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"));
      	}else if(!accionConsolidar.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.consolidarPedido"))){
      		session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL,
	      			MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.estadoPedido"));
      	}
      	
      	session.setAttribute(SessionManagerSISPE.ACCION_SECCION_BUSQUEDA, "ok");
      	
      	//variable para controlar el filtro por entidad responsable
      	session.setAttribute("ec.com.smx.sic.sispe.busqueda.opEntidadResponsable", "ok");
      	//guardar en sesi\u00F3n esta colecci\u00F3n
      	session.setAttribute(SessionManagerSISPE.COL_ESTADOS,estados);

      	//se inicializan los atributos del formulario
      	formulario.setDatos(null);
      	formulario.setEtiquetaFechas("Fecha de Estado");
      	
      	formulario.setOpcionFecha(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.hoy"));
      	if(SessionManagerSISPE.getCurrentEntidadResponsable(request).getTipoEntidadResponsable().equals(entidadBodega)){
      		formulario.setOpEntidadResponsable(entidadBodega);
      	}
      	
      	formulario.setOpEstadoActivo(estadoActivo);
      	
      	//se sube a sesi\u00F3n el c\u00F3digo para el tipo de pedido normal
      	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.normal",
      			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.normal"));
      	
      	session.setAttribute("ec.com.smx.sic.sispe.tipoPedido.devolucion",
      			MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoPedido.devolucion"));
      	
      	session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de estado");
      	//Variable de sesion para que no presente el combo de estados
      	session.setAttribute(SessionManagerSISPE.ESTADO_CHECK,"ok");
      }
      
    }
    catch(Exception ex){
      LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
      salida="errorGlobal";
    }
    
    //se guardan los mensajes generados
    saveInfos(request, infos);
    saveErrors(request, errors);
    
    LogSISPE.getLog().info("Se procede a trav\u00E9s de: {}",salida);
    
    return mapping.findForward(salida);
  }
  
  /**
   * Inicializa los datos para el ordenamiento
   * @param request
   */
  private void inicializarDatosOrdenamiento(HttpServletRequest request){
  	
    //se incializan los datos de la tabla
    String[][] datosTabla = {
      {"id.codigoPedido", "C\u00F3digo del pedido", null},
      {"llaveContratoPOS", "Llave del contrato", null},
      {"fechaInicialEstado", "Fecha de pedido", null},
//      {"nombreContacto", "Nombre del Cliente", null},
      {"contactoEmpresa", "Nombre del cliente", null},
      {"totalPedido", "Valor total", null},
      {"abonoPedido", "Valor abonado", null},
      {"descripcionEstado", "Estado", null},
//      {"etapaEstadoActual", "Etapa del Estado", null},
      {"estadoActual", "Estado actual", null}
    };
    //se guardan los datos en sesi\u00F3n
    request.getSession().setAttribute(SessionManagerSISPE.DATOS_TABLA_ORDENAR, datosTabla);
    //se inicializa los datos de la culumna ordenada 
    request.getSession().setAttribute(SessionManagerSISPE.DATOS_COLUMNA_ORDENADA, new String [] {"C\u00F3digo del Pedido", "Ascendente"});
  }
  
  /**
   * Verifica si el pedido tiene autorizaciones de descuento variable o de stock
   * @param colVistaPedidoDTO
   */
  public void verificarAutorizaciones(Collection<VistaPedidoDTO> colVistaPedidoDTO){
	  for(VistaPedidoDTO vistaPedidoDTO : colVistaPedidoDTO){
		  
          //se itera la vistaDetallePedido para crear un DetallePedidoDTO
		  if(CollectionUtils.isNotEmpty(vistaPedidoDTO.getVistaDetallesPedidosReporte())){
        		Collection<VistaDetallePedidoDTO> colVistaDetallePedidoDTO = vistaPedidoDTO.getVistaDetallesPedidosReporte();    
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
		  			
		  			//verifica si tiene autorizaciones de stock
		  			if(StringUtils.isNotEmpty(vistaPedidoDTO.getTieneAutorizacionStock())){
		  				if(!vistaPedidoDTO.getTieneAutorizacionStock().equals("SI")){
		  					if(Integer.parseInt(vistaPedidoDTO.getTieneAutorizacionStock()) > 0){
		  						vistaPedidoDTO.setTieneAutorizacionStock("SI");
		  					}else{
		  						vistaPedidoDTO.setTieneAutorizacionStock(null);
		  					}
		  				}
		  			}
		  		}
		  	}
	  	}     
  	}
  
  
}
