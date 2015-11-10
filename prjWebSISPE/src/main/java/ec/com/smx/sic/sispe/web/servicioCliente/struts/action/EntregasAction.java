/*
 * Clase EntregasAction.java Creado 13/06/06
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.ACEPTAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CANCELAR_ARCH_BENE;
import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.SUBIR_ARCH_BENE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.sispe.common.util.BeanSession;
import ec.com.smx.sic.sispe.common.util.CotizacionReservacionUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.DefArchivoEntregaDTO;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.PedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaDetallePedidoDTO;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;
import ec.com.smx.sic.sispe.web.controlesusuario.tab.Tab;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.DespachosEntregasForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase permite realizar la entrega parcial o total de un pedido, mediante la selecci\u00F3n de los beneficiarios 
 * que recibir\u00E1n los art\u00EDculos.</br>
 * Se manejan los siguientes iconos para indicar al usuario el estado de la entrega:
 * <ol>
 * 	<li><img src="images/error_small.gif"/>&nbsp;Ning\u00FAn art\u00EDculo del pedido se ha entregado</li>
 * 	<li><img src="images/advertencia_16.gif"/>&nbsp;Parte del pedido se ha entregado</li>
 *  <li><img src="images/exito_16.gif"/>&nbsp;Todo el pedido est\u00E1 listo para entregarse</li>
 * </ol>
 * </p>
 * @author 	fmunoz
 * @version 2.0
 * @since	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class EntregasAction extends BaseAction  
{
	private static final String OBJ_BUSQUEDA = "ec.com.smx.sic.sispe.pedidos.busqueda";
	private static final String COL_DATOS_REPORTE ="ec.com.sic.sispe.datos.reporte";
	private static final String MOSTRAR_OPCIONES_BASICAS ="ec.com.sic.sispe.mostrar.todasOpciones";
	
	//private static final String BOTON_BENEFICIARIO ="ec.com.sic.sispe.boton.benenficiario";
	public static final String CODIGO_PEDIDO = "codigoPedido";
	public static final String CODIGO_LOCAL = "codigoLocal";


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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		
		HttpSession session = request.getSession();
		DespachosEntregasForm formulario = (DespachosEntregasForm) form;

		//se obtienen los estados activos e inactivos de sesi\u00F3n
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		//se obtiene el bean que contiene los campos genericos
		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);

		//esta variable permite que se muestre o no una ventana con los datos que van a imprimirse para el 
		//reporte en la pesta\u00F1a de entregados
		//[1: se muestra, 0: no se muestra]  
		request.getSession().setAttribute("ec.com.smx.sic.sispe.Despacho.imprimir","0");
		String accionEntregas = "entregaPedido.do";
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
					datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar");
				else{
					datos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosEntregados");
					beanSession.setInicioPaginacion(request.getParameter("start"));
				}

				if(datos!=null && !datos.isEmpty()){
					int size= datos.size();
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
					int start= Integer.parseInt(request.getParameter("start"));
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection datosSub = Util.obtenerSubCollection(datos,start, start + range > size ? size : start+range);
					formulario.setDatos(datosSub);
					//subo a sesi\u00F3n los datos en caso de crear reporte
					session.setAttribute(COL_DATOS_REPORTE, datosSub);
				}
			}
			//------------- cuando presiona ver archivo foto ----------------------------
			else if(request.getParameter("linkArchivoFoto")!=null){
				//Datos enviados desde la pagina JSP(porEntregar)
	    	  	String codigoPedido = (String)request.getParameter("linkArchivoFoto");
	    	  	Integer codigoLocalEntrega = Integer.parseInt(request.getParameter("codigoLocalEntrega"));
	    	  	String codigoArticulo = (String)request.getParameter("codigoArticulo");
				//se construye la plantilla para la busqueda defArchivoEntrega
				DefArchivoEntregaDTO defPlantillaDTO = new DefArchivoEntregaDTO();
				defPlantillaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				defPlantillaDTO.getId().setCodigoArticulo(codigoArticulo);			
				defPlantillaDTO.setCodigoLocalEntrega(codigoLocalEntrega);
				defPlantillaDTO.getId().setCodigoPedido(codigoPedido);
				session.setAttribute(DespachosAction.PLANTILLA_DEFARCHIVOENTREGA, defPlantillaDTO);
				session.setAttribute(MOSTRAR_OPCIONES_BASICAS, "ok");
				//Realiza el metodo de busqueda
				CotizacionReservacionUtil.busquedaArchivoFoto(request, defPlantillaDTO);
				//se construye la ventana de informacion de archivos fotos
				CotizacionReservacionUtil.instanciarVentanaSubirArchivoFoto(request, accionEntregas);
			}
			//accion el botton cancelar pop up
		    else if(request.getParameter("cancelaArchivoFoto") != null){
		    	  session.removeAttribute(SessionManagerSISPE.POPUP);
		    }
		    else if(request.getParameter("verArchivoFoto") != null){
				LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
				CotizacionReservacionUtil.verArchivoFoto(request, response);
				salida = null;
		    }
			//accion el botton aceptar pop up
		    else if(request.getParameter("aceptaArchivoFoto") != null){
		    	 session.removeAttribute(SessionManagerSISPE.POPUP);
		    }
			//------------- cuando se desea realizar la b\u00FAsqueda ----------------------------
			else if(request.getParameter("buscar")!=null)
			{
				LogSISPE.getLog().info("---Entro por buscar de  EntregasAction---");
				buscarPedidos(session, request, form,beanSession);	
			}
			/*-------------------- si se escogi\u00F3 el link para ver el detalle de  alg\u00FAn pedido ---------------------*/
			else if (request.getParameter("indicePedido")!= null) 
			{
				int indice=0;
				try
				{
					indice = Integer.parseInt(request.getParameter("indicePedido"));
					formulario.setOpSeleccionTodos(null);
					formulario.setOpSeleccionAlgunos(null);

					//obtengo de la sesion la colecci\u00F3n de los pedidos
					ArrayList pedido = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar");
					VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)pedido.get(indice);

					if(vistaPedidoDTO.getNpEstadoVistaPedido()!=null){
						formulario.setOpSeleccionAlgunos(vistaPedidoDTO.getNpSeleccionados());
						if(vistaPedidoDTO.getNpEstadoVistaPedido().equals(estadoActivo))
							formulario.setOpSeleccionTodos(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.todos"));
					}

					if(vistaPedidoDTO.getVistaDetallesPedidos()==null){
						//se obtienen los detalles del pedidopor entregar y se los guarda en la colecci\u00F3n del 
						//objeto vistaPedidoDTO
						SessionManagerSISPE.getServicioClienteServicio().transObtenerDetallesPedidosEntregas(vistaPedidoDTO, 
								SessionManagerSISPE.getCurrentEntidadResponsable(request));
					}

					//se almacena el indice del pedido seleccionado
					beanSession.setIndiceRegistro(request.getParameter("indicePedido"));

					//se almacena el DTO obtenido en sesi\u00F3n.
					session.setAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado",vistaPedidoDTO);
					session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido"));
					//se salva el codigo del pedido seleccionado y el codigo del local
					session.setAttribute(CODIGO_PEDIDO, vistaPedidoDTO.getId().getCodigoPedido());
		    	  	session.setAttribute(CODIGO_LOCAL, vistaPedidoDTO.getId().getCodigoAreaTrabajo());
		    	  	
		    	  	//--J-Verificar si el pedido seleccionado tiene el codigo activo para subir el archivo.
		    	  	PedidoDTO pedidoDTO = new PedidoDTO();
					PedidoDTO pedidoActuallizadoDTO = new PedidoDTO();
					//seteo el codigo de pedido y codigoCompania
					pedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					pedidoDTO.getId().setCodigoPedido(vistaPedidoDTO.getId().getCodigoPedido());
					//obtengo el pedido actual
					ArrayList<PedidoDTO> pedidoActual = (ArrayList<PedidoDTO>)SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidoQBE(pedidoDTO);
					pedidoActuallizadoDTO = pedidoActual.get(0);
					
					if(pedidoActual != null && !pedidoActual.isEmpty()){
						if(pedidoActuallizadoDTO.getArchivoBeneficiario().equals(estadoActivo)) {
							LogSISPE.getLog().info("Si tiene este pedido activo el campo archivoBeneficiario");
							session.setAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO,"OK");
						} else {
							LogSISPE.getLog().info("No tiene este pedido el campo archivoBeneficiario");
							session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
						}
					}
					//--J--
					
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
			else if (beanSession.getPaginaTab() != null && beanSession.getPaginaTab().comprobarSeleccionTab(request)) {

				/*---------- si se escogi\u00F3 el tag de los pedidos en producci\u00F3n --------------*/
				if (beanSession.getPaginaTab().esTabSeleccionado(0)) {
					/*--------------- si se escogi\u00F3 el tab de pedidos por entregar ----------------*/
					//else if(formulario.getTabPedidos()!=null)
					LogSISPE.getLog().info("pedidos por entregar");
					formulario.setDatos(null);
					formulario.setOpSeleccionTodos(null);
					formulario.setOpSeleccionAlgunos(null);

					//se obtiene de sesi\u00F3n la colecci\u00F3n de pedidos por despachar
					Collection pedidosPorEntregar = (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar");
					if(pedidosPorEntregar!=null && !pedidosPorEntregar.isEmpty()){
						LogSISPE.getLog().info("ENTRO A LA PAGINACION");
						int size= pedidosPorEntregar.size();
						int start= 0;
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));

						Collection datosSub = Util.obtenerSubCollection(pedidosPorEntregar,start, start + range > size ? size : start+range);
						formulario.setDatos(datosSub);
					}

					session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
				}
				/*------------ si se escogi\u00F3 el tab de pedidos entregados --------------*/    
				else if (beanSession.getPaginaTab().esTabSeleccionado(1)) {
					LogSISPE.getLog().info("pedidos entregados");
					formulario.setDatos(null);
					formulario.setOpSeleccionTodos(null);
					formulario.setOpSeleccionAlgunos(null);

					//colecci\u00F3n que almacenar\u00E1 la lista de pedidos
					Collection pedidosEntregados= (Collection)session.getAttribute("ec.com.smx.sic.sispe.pedidosEntregados");
					//verifica la existencia de pedidos en estado producido
					if (pedidosEntregados != null && !pedidosEntregados.isEmpty()) 
					{
						LogSISPE.getLog().info("ENTRO A LA PAGINACION");
						int size= pedidosEntregados.size();
						int start= 0;
						if(beanSession.getInicioPaginacion()!=null)
							start = Integer.parseInt(beanSession.getInicioPaginacion());
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
						formulario.setStart(String.valueOf(start));
						formulario.setRange(String.valueOf(range));
						formulario.setSize(String.valueOf(size));

						Collection datosSub = Util.obtenerSubCollection(pedidosEntregados,start, start + range > size ? size : start+range);
						formulario.setDatos(datosSub);

					}
					session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"));
				}
			}
			  //accion del boton editar de la pantalla estado pedido
		      else if(request.getParameter(ACEPTAR_ARCH_BENE) != null){
		    	  	String pedido = (String)session.getAttribute(CODIGO_PEDIDO);
		    	  	LogSISPE.getLog().info("Entra a mostra la ventana para subir el archivo del beneficiario");
		    	  	LogSISPE.getLog().info("Valor del codigo del pedido->{}", pedido);
		    	  	LogSISPE.getLog().info("Valor de la accion->{}", accionEntregas);
		    	  	//se construye la ventana de informacion de archivos del beneficiario y sus opciones(subir,eliminar)
					CotizacionReservacionUtil.instanciarVentanaSubirArchivoBeneficiario(request, accionEntregas);
					CotizacionReservacionUtil.listarArchivos(request, pedido);
					salida = "desplegar";
		      }
			  else if(request.getParameter("verArchivo") != null){
					LogSISPE.getLog().info("Presiono el Boton ver archivo del icono del PopUp");
					CotizacionReservacionUtil.verArchivo(request, response);
			  }
		      /*-------- cuando se cancela el la subida de un archivo de configuracion del beneficiario----------*/
			  else if(request.getParameter(CANCELAR_ARCH_BENE) != null){
					//se cancela el archivo del beneficiario 
					CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionEntregas);
					//session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
					salida = "desplegar";
			  }
			 /*-------------------- cuando se desea subir un archivo de configuracion del beneficiario ----------------------*/
			  else if(request.getParameter(SUBIR_ARCH_BENE) != null){
					//se cancela el archivo del beneficiario 
					LogSISPE.getLog().info("Se confirmo la subida del Archivo");
					//remueve la variable de session
					//session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
					CotizacionReservacionUtil.cancelarArchivoBeneficiario(request, accionEntregas);
					salida = "desplegar";
			  }
			/*------------------------------ si se acept\u00F3 las entregas seleccionadas ---------------------------------*/
			else if(formulario.getBotonAceptarSeleccion()!=null)
			{
				//se obtiene el pedido que se seleccion\u00F3
				VistaPedidoDTO vistaPedidoDTO = (VistaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.pedidoSeleccionado");

				//se obtiene el indice del pedido seleccionado
				String indiceSeleccionado = beanSession.getIndiceRegistro();
				//se obtiene de sesi\u00F3n la colecci\u00F3n de indices guardados hasta el momento de los pedidos
				Collection indicesPedidos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos");

				//se crea la colecci\u00F3n de indices de los detalles del pedido
				Collection indicesDetallePedido = new ArrayList();

				//se obtiene el detalle de los pedidos
				ArrayList<VistaDetallePedidoDTO> detallePedido = (ArrayList<VistaDetallePedidoDTO>)vistaPedidoDTO.getVistaDetallesPedidos();

				/*-------------------------------------------------------------------------------------------------------*/
				/*----- Se calcula la cantidad de entregas que existen por art\u00EDculo que todav\u00EDa no se han entregado ------*/
				int detallesCompletados = 0; //variable que almacena la cantidad de art\u00EDculos que ya est\u00E1n 
				//TODO REVISION DE BENEFICIARIOS
				//completamente entregados 
				for(VistaDetallePedidoDTO vistaDetallePedidoDTO:detallePedido){
					vistaDetallePedidoDTO.setIndicesEntregas(null);
					ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)vistaDetallePedidoDTO.getEntregaDetallePedidoCol();
					int sizeEntregas = entregas.size();
					int entregasCompletadas = 0; //variable que almacena la cantidad de entregas que ya est\u00E1n 
					//completamente entregadas
					for(int i=0;i<entregas.size();i++){
						EntregaDetallePedidoDTO entregaDTO = (EntregaDetallePedidoDTO)entregas.get(i);
						//entregaDTO.setIndicesBeneficiarios(null);
						//ArrayList beneficiarios = (ArrayList)entregaDTO.getBeneficiarios();
						//int sizeBeneficiarios = beneficiarios.size();
						//int beneficiariosConEntrega = 0;  //variable que almacena la cantidad de beneficiarios por entrega 
						//con fecha de entrega	
						//for(int j=0;j<beneficiarios.size();j++){
							//BeneficiarioSICDTO beneficiarioDTO = (BeneficiarioSICDTO)beneficiarios.get(j);
							if(entregaDTO.getFechaRegistroEntregaCliente()!=null){
								entregasCompletadas++;								
							}
//							else
//								break;
//						//}
//						if(beneficiariosConEntrega==sizeBeneficiarios)
//							entregasCompletadas++;
					}
					if(entregasCompletadas==sizeEntregas)
						detallesCompletados++;
				}
				int sizeDetallePedido = detallePedido.size() - detallesCompletados;
				/*---------------------------------------------------------------------------------------------------*/

				String indiceAnteriorEntrega = ""; //String que almacena el indice de la entrega guardado anteriormente 
				String indiceAnteriorDetalle = ""; //String que almacena el indice del detalle del Pedido 
				//guardado anteriormente.
				int sizeEntregas = 0; //almacena la sumatoria total de entregas encontradas en los detalleSeleccionados
				int totalIndicesEntregas = 0; //almacena la sumatoria total de indices almacenados para las entregas.
				int sizeBeneficiarios = 0; //almacena la sumatoria total de beneficiarios encontrados 
				//en las entregas seleccionadas

				/*if(formulario.getOpSeleccionTodos()!=null){
          vistaPedidoDTO.setNpEstadoVistaPedido(estadoActivo);
          //si el indice seleccionado no esta en la colecci\u00F3n se a\u00F1ade
          if(!indicesPedidos.contains(indiceSeleccionado))
            indicesPedidos.add(indiceSeleccionado);
        }else
        {*/
				String [] escogidos = formulario.getOpSeleccionAlgunos();
				if(escogidos!=null){
					//se itera el arreglo de los indices escogidos
					for(int i=0;i<escogidos.length;i++){
						String [] indices = escogidos[i].split("-");
						LogSISPE.getLog().info(i+" INDICE ARTICULO: "+indices[0]+", INDICE ENTREGA: "+indices[1]);//+", INDICE BENEFICIARIO"+indices[2]);
						LogSISPE.getLog().info("----------------------------------");
						//se obtiene el indice del art\u00EDculo que contiene las entregas que se seleccionaron
						//este indice se encuentra en el primer arreglo de String: indices[0]
						int indiceDetalleSel = Integer.parseInt(indices[0]);
						VistaDetallePedidoDTO vistadetallePedidoDTO = (VistaDetallePedidoDTO)detallePedido.get(indiceDetalleSel);
						//si el indice no consta en la lista se a\u00F1ade y se actualiza el tama\u00F1o total de las entregas
						if(!indicesDetallePedido.contains(indices[0]))
						{
							indiceAnteriorEntrega="";
							indicesDetallePedido.add(indices[0]);
							LogSISPE.getLog().info("SE GUARDO EN INDICES DEL DETALLE {}",indices[0]);
							vistaPedidoDTO.setIndicesVistaDetallesPedidos(indicesDetallePedido);
						}
//						//TODO REVISION DE BENEFICIARIOS
//						//se suman los tama\u00F1os de las entregas encontradas
//						if(!indiceAnteriorDetalle.equals(indices[0])){
//							indiceAnteriorDetalle=indices[0];
//							Collection<EntregaDetallePedidoDTO> entregas = vistadetallePedidoDTO.getEntregaDetallePedidoCol();
//							int entregasCompletadas = 0;
//							//se itera la colecci\u00F3n de entregas del detalle actual para determinar cuantas 
//							//est\u00E1n completamente entregadas
//							int beneficiariosConEntrega = 0;
//							for(EntregaDetallePedidoDTO entregaDTO: entregas){
//								//Collection beneficiarios = entregaDTO.getBeneficiarios();
////								int beneficiariosConEntrega = 0;
////								for(Iterator ite = beneficiarios.iterator();ite.hasNext();){
////									BeneficiarioSICDTO beneficiarioDTO = (BeneficiarioSICDTO)ite.next();
//									if(entregaDTO.getFechaRegistroEntregaCliente()!=null)
//										beneficiariosConEntrega++; //se suman los beneficiarios con fecha de entrega
////								}
//								//si el tama\u00F1o de los beneficiarios de la entrega es igual a la cantidad de entregados
//								//entonces la entrega se ha completado
//								if(beneficiarios.size() == beneficiariosConEntrega)
//									entregasCompletadas ++;
//							}
//							//se actualiza el tama\u00F1o total de las entregas no completadas
//							sizeEntregas = sizeEntregas + entregas.size() - entregasCompletadas;
//						}

						/*----------------- seccion entregas ------------------------*/
						//se obtiene la colecci\u00F3n de las entregas de ese art\u00EDculo
						ArrayList<EntregaDetallePedidoDTO> entregas = (ArrayList<EntregaDetallePedidoDTO>)vistadetallePedidoDTO.getEntregaDetallePedidoCol();
						//se obtiene el indice de la entrega que contiene los beneficiarios de las entregas
						//este indice se encuentra en el segundo arreglo de String: indices[1]
						int indiceEntregaSel = Integer.parseInt(indices[1]);

						EntregaDetallePedidoDTO entregaDetallePedidoDTO = (EntregaDetallePedidoDTO)entregas.get(indiceEntregaSel);
						//creaci\u00F3n de la colecci\u00F3n de indices de entregas
						Collection indicesEntrega = null;
						if(vistadetallePedidoDTO.getIndicesEntregas()!=null)
							indicesEntrega = vistadetallePedidoDTO.getIndicesEntregas();
						else
							indicesEntrega = new ArrayList();

						//si el indice no consta en la lista se a\u00F1ade y se actualiza el tama\u00F1o total de los beneficiarios
						if(!indicesEntrega.contains(indices[1]))
						{
							indicesEntrega.add(indices[1]);
							LogSISPE.getLog().info("****SE GUARDO EN INDICES DE LA ENTREGA: {}",indices[1]);
							vistadetallePedidoDTO.setIndicesEntregas(indicesEntrega);
						}
						//TODO REVISION DE BENEFICIARIOS
						if(!indiceAnteriorEntrega.equals(indices[1])){
							totalIndicesEntregas ++;
							indiceAnteriorEntrega = indices[1];
							//Collection beneficiarios = entregaDTO.getBeneficiarios();
							int entregados = 0;
							//se itera la colecci\u00F3n de beneficiarios de la entrega actual para determinar cuantas
							//tienen una fecha de entrega
//							for(Iterator it = beneficiarios.iterator();it.hasNext();){
//								BeneficiarioSICDTO beneficiarioDTO = (BeneficiarioSICDTO)it.next();
								if(entregaDetallePedidoDTO.getFechaRegistroEntregaCliente()!=null){
									entregados++; //se suman las entregas 
								}
//							}
							//se actualiza el total de beneficiarios sin entrega
							sizeBeneficiarios = sizeBeneficiarios + entregas.size() - entregados;
						}
						/*-----------------------------------------------------------*/

						/*----------------- seccion beneficiarios ----------------------*/
//						//creaci\u00F3n de la colecci\u00F3n de indices de beneficiarios
//						Collection indicesBenefiario = null;
//						if(entregaDTO.getIndicesBeneficiarios()!=null)
//							indicesBenefiario = entregaDTO.getIndicesBeneficiarios();
//						else
//							indicesBenefiario = new ArrayList();
//						//si el indice no consta en la lista esta se actualiza
//						if(!indicesBenefiario.contains(indices[2])){
//							indicesBenefiario.add(indices[2]);
//							LogSISPE.getLog().info("********SE GUARDO EN INDICES DEl BENEFICIARIO: {}",indices[2]);
//							entregaDTO.setIndicesBeneficiarios(indicesBenefiario);
//						}
						/*---------------------------------------------------------------*/
					}
					

					//se comparan los tama\u00F1os de las colecciones para determinar si fueron seleccionados 
					//todos los beneficiarios
					LogSISPE.getLog().info("sizeDetallePedido: {}",sizeDetallePedido);
					LogSISPE.getLog().info("indicesDetallePedido.size(): {}",indicesDetallePedido.size());
					LogSISPE.getLog().info("------------------------------------");
					if(indicesDetallePedido.size() == sizeDetallePedido){
						LogSISPE.getLog().info("sizeEntregas: {}",sizeEntregas);
						LogSISPE.getLog().info("totalIndicesEntregas: {}",totalIndicesEntregas);
						LogSISPE.getLog().info("-------------------------------------");
//						if(sizeEntregas == totalIndicesEntregas){
						if(escogidos.length == totalIndicesEntregas){
							LogSISPE.getLog().info("escogidos.length: {}",escogidos.length);
							LogSISPE.getLog().info("sizeBeneficiarios: {}",sizeBeneficiarios);
							LogSISPE.getLog().info("-------------------------------------");
							//si la cantidad de escogidos es igual a la cantidad de beneficiarios encontrados
							if(escogidos.length == sizeBeneficiarios)
								vistaPedidoDTO.setNpEstadoVistaPedido(estadoActivo); //se entrego todo el pedido
							else
								vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); //se entreg\u00F3 parte del pedido
						}else
							vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); //se entreg\u00F3 parte del pedido
					}
					else
						vistaPedidoDTO.setNpEstadoVistaPedido(estadoInactivo); //se entreg\u00F3 parte del pedido

					//si el indice seleccionado no esta en la colecci\u00F3n se a\u00F1ade
					if(!indicesPedidos.contains(indiceSeleccionado)){
						indicesPedidos.add(indiceSeleccionado);
						//session.setAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos",indicesPedidos);
						LogSISPE.getLog().info("se agrego: {}",indiceSeleccionado);
					}
					//finalmente se almacena el arreglo de String que contiene los valores que se chequearon en el 
					//multiBoxs de beneficiarios, para recuperarlos cuando se quiera editar la entrega de un pedido.
					vistaPedidoDTO.setNpSeleccionados(escogidos);

					/*------------------- prueba de indices ----------------------*/
					/*LogSISPE.getLog().info("PRUEBA DE INDICES DETALLE");
             for(Iterator it = indicesDetallePedido.iterator();it.hasNext();){
            	 LogSISPE.getLog().info("INDICE: "+it.next());
             }

             for(int i=0;i<detallePedido.size();i++){
            	 VistaDetallePedidoDTO v = (VistaDetallePedidoDTO)detallePedido.get(i);
            	 ArrayList entregas = (ArrayList)v.getEntregas();
            	 Collection indicesE = v.getIndicesEntregas();
            	 LogSISPE.getLog().info("DETALLE "+i);
            	 if(indicesE!=null)
            		 for(Iterator it=indicesE.iterator();it.hasNext();)
            			 LogSISPE.getLog().info("  INDICE ENTREGA: "+it.next());
            	 else
            		 LogSISPE.getLog().info("  SIN INDICES_ENTREGA");

             if(entregas!=null)//se recorre las entregas para obtener los indices del beneficiario
            	 for (int j =0;j<entregas.size();j++){
            		 EntregaDTO entregaDTO = (EntregaDTO)entregas.get(j);
            		 Collection b = entregaDTO.getIndicesBeneficiarios();
            		 LogSISPE.getLog().info("-----------");
            		 LogSISPE.getLog().info("  ENTREGA: "+j);
            		 if(b!=null)
            			 for(Iterator it = b.iterator();it.hasNext();)
            				 LogSISPE.getLog().info("    INDICE BENEFICIARIO: "+it.next());
            		 else
            			 LogSISPE.getLog().info("    SIN INDICES_BENEFICIARIO");
            	 }
             }*/
					/*-------------------------------------------------------------*/
				}
				else //no se escogi\u00F3 ning\u00FAn beneficiario
				{
					vistaPedidoDTO.setNpEstadoVistaPedido(null);
					vistaPedidoDTO.setNpSeleccionados(null);
					boolean seElimino = indicesPedidos.remove(indiceSeleccionado);
					LogSISPE.getLog().info("\u00BFSE ELIMINO EL INDICE? {}",seElimino);
				}
				//}
				vistaPedidoDTO.setVistaDetallesPedidos(detallePedido);
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
				formulario.setOpSeleccionAlgunos(null);
				formulario.setOpSeleccionTodos(null);
				session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
			}
			/*------------------------------ si se escogi\u00F3 el bot\u00F3n Imprimir ---------------------------------*/
			else if(formulario.getBotonImprimir()!=null){
				//obtener la coleccion de pedidos
				Collection pedidosEntregados = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.pedidosEntregados");
				if(pedidosEntregados!=null && !pedidosEntregados.isEmpty()){
					//llamada al m\u00E9todo que realiza la construcci\u00F3n del reporte
					formulario.construirReporteDespachoEntrega(pedidosEntregados, beanSession.getInicioPaginacion(), request);
				}
			}
			//--------------- realiza el forward a la p\u00E1gina donde se muestra el reporte ------------------
			else if(request.getParameter("mostrarVentana")!=null){
				salida = "rptDespachoEntrega";
			}
			else if(request.getParameter("mostrarVentana2")!=null){
				salida = "rptDespacho";
			}
			/*-------- CONFIRMACION de la generaci\u00F3n del documento como XSL -------*/
		    else if(generarXSL){
		    	LogSISPE.getLog().info("confirmar impresion XSL");
		      	request.setAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo", WebSISPEUtil.generarNombreArchivo("Entrega de Pedidos", MessagesWebSISPE.getString("ec.com.smx.sic.sispe.extension.excel.reportesExcel")));
		      	salida= "reporteEntregaXLS";
		    }
			/*-------------------- si se escogi\u00F3 el link para ver el detalle de  alg\u00FAn pedido ---------------------*/
			else if(formulario.getBotonCancelarSeleccion()!=null){
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
				session.removeAttribute(SessionManagerSISPE.BOTON_BENEFICIARIO);
			}
			/*-------------------------------------- si se escogi\u00F3 el boton Entregar ----------------------------- */
			else if (formulario.getBotonEntregar()!= null){
				formulario.setDatos(null);
				ArrayList<VistaPedidoDTO> pedidosPorEntregar = (ArrayList<VistaPedidoDTO>)session.getAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar");
				Collection indicesPedidos = (Collection)session.getAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos");
				if(indicesPedidos!=null && !indicesPedidos.isEmpty())
				{
					try{
						//se iteran los pedidos para obtener los descuentos de cada uno
						for(Iterator it = indicesPedidos.iterator();it.hasNext();){
							int indice = Integer.parseInt((String)it.next());
							//se llama a la funci\u00F3n que carga los descuentos del pedido
							//esto es necesario para el correcto registro de los descuentos en caso de que existan
							WebSISPEUtil.obtenerDescuentosEstadoPedido((VistaPedidoDTO)pedidosPorEntregar.get(indice),request,Boolean.FALSE);
							LogSISPE.getLog().info("npEstadoVistaPedido: {}",((VistaPedidoDTO)pedidosPorEntregar.get(indice)).getNpEstadoVistaPedido());
						}

						SessionManagerSISPE.getServicioClienteServicio().transProcesarPedidosParaEntregar(pedidosPorEntregar,
								indicesPedidos);

						//SE MUESTRA EL MENSAJE DE EXITO
						success.add("exitoRegistro",new ActionMessage("message.exito.registro","La Entrega"));

					}catch(SISPEException ex){
						//el servicio Obtener datos ha fallado
						LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
						errors.add("entrega", new ActionMessage("errors.llamadaServicio.registrarDatos","la Entrega"));
					}

					//se actualiza la lista de las entregas
					VistaPedidoDTO vistaPedidoDTO = new VistaPedidoDTO();
					vistaPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
					vistaPedidoDTO.getId().setCodigoAreaTrabajo(beanSession.getCodigoLocal());
					vistaPedidoDTO.getId().setCodigoPedido(beanSession.getCodigoPedido());
					vistaPedidoDTO.setLlaveContratoPOS(beanSession.getCodigoReservacion());
					vistaPedidoDTO.setCedulaContacto(beanSession.getCedulaContacto());
					vistaPedidoDTO.setNombreContacto(beanSession.getNombreContacto());
					vistaPedidoDTO.setNombreEmpresa(beanSession.getNombreEmpresa());
					vistaPedidoDTO.setRucEmpresa(beanSession.getRucEmpresa());
					vistaPedidoDTO.setEntidadResponsable(beanSession.getOpEntidadResonsable());
					if(beanSession.getCodigoArticulo()!=null || beanSession.getDescripcionArticulo()!=null || beanSession.getCodigoClasificacion()!=null){
						vistaPedidoDTO.setArticuloDTO(new ArticuloDTO());
						vistaPedidoDTO.getArticuloDTO().getId().setCodigoArticulo(beanSession.getCodigoArticulo());
						vistaPedidoDTO.getArticuloDTO().setCodigoClasificacion(beanSession.getCodigoClasificacion());
						vistaPedidoDTO.getArticuloDTO().setDescripcionArticulo(beanSession.getDescripcionArticulo());
					}
					vistaPedidoDTO.setNpPrimeraFechaEntregaInicial(beanSession.getFechaInicial());
					vistaPedidoDTO.setNpPrimeraFechaEntregaFinal(beanSession.getFechaFinal());
					//para que la consulta traiga los pedidos por entregar
					vistaPedidoDTO.setNpEstadoPedidosEntregados(estadoInactivo);
					pedidosPorEntregar = (ArrayList)SessionManagerSISPE.getServicioClienteServicio().
					transObtenerPedidosEntregas(vistaPedidoDTO, SessionManagerSISPE.getCurrentEntidadResponsable(request));

					session.setAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar",pedidosPorEntregar);
					session.setAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos", new ArrayList());
					//se elimina la lista de pedidos entregados de sesi\u00F3n
					session.removeAttribute("ec.com.smx.sic.sispe.pedidosEntregados");
				}else{
					infos.add("indicesVacios",new ActionMessage("errors.seleccion.pedidos","la entrega"));
				}

				if(pedidosPorEntregar!=null && !pedidosPorEntregar.isEmpty()){
					LogSISPE.getLog().info("ENTRO A LA PAGINACION");
					int size= pedidosPorEntregar.size();
					int start= 0;
					int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
					formulario.setStart(String.valueOf(start));
					formulario.setRange(String.valueOf(range));
					formulario.setSize(String.valueOf(size));

					Collection datosSub = Util.obtenerSubCollection(pedidosPorEntregar,start, start + range > size ? size : start+range);
					formulario.setDatos(datosSub);
				}
				salida = "desplegar";
			}
			/*---------------------------------Muestra los pedidos producidos por defecto-------------------------*/
			else 
			{
				LogSISPE.getLog().info("Entro por Else EntregasAction");
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
				session.setAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos",new ArrayList());
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
				session.setAttribute("WebSISPE.tituloVentana", "Entrega de Pedidos");
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoEntregado",MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.entregado"));
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.totalmente"));
				session.setAttribute("ec.com.smx.sic.sispe.pedido.estadoPagadoLiquidado", MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.liquidado"));
				session.setAttribute(SessionManagerSISPE.ACCION_ACTUAL, 
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.entrega"));
				//Objetos para construir los tabs
				PaginaTab tabsEntregas = new PaginaTab("entregaPedido", "desplegar", 0, 455, request);
				Tab tabPedidosPorEntregar = new Tab("Por entregar", "entregaPedido", "/servicioCliente/entregaPedido/porEntregar.jsp", true);
				Tab tabPedidosEntregados = new Tab("Entregados", "entregaPedido", "/servicioCliente/entregaPedido/entregados.jsp", false);
				tabsEntregas.addTab(tabPedidosPorEntregar);
				tabsEntregas.addTab(tabPedidosEntregados);
				beanSession.setPaginaTab(tabsEntregas);
				formulario.setEtiquetaFechas("Fecha de Entrega");
				session.setAttribute(SessionManagerSISPE.TIPO_FECHA_BUSQUEDA, "Fecha de entrega");
				//--------------- realiza la accion del Link desde la pantalla de reportesEntrega  ------------------
				if(request.getParameter("accionLink")!=null){
					LogSISPE.getLog().info("---Entro por accionLink de  EntregasAction---{}", request.getParameter("accionLink"));
					formulario.setNumeroPedidoTxt(request.getParameter("accionLink"));
					//llama a la funcion buscarPedidos seteandole el codigo de pedido seleccionado de la jsp reportesEntregas
					buscarPedidos(session, request, form,beanSession);
				}
				salida = "desplegar";
			}
		}
		
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida="errorGlobal";
		}
		//se guarda el beanSession
		SessionManagerSISPE.setBeanSession(beanSession, request);
		
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);
		
		LogSISPE.getLog().info("salida: {}" , salida);
		return mapping.findForward(salida);		
	}
	//Funcion General para consulta de pedidos 
	private void buscarPedidos( HttpSession session, HttpServletRequest request, ActionForm form, BeanSession beanSession) throws Exception {
		
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		
		//Se obtiene la variable de formulario
		DespachosEntregasForm formulario = (DespachosEntregasForm) form;
		//se obtienen los estados activos e inactivos de sesi\u00F3n 
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		//se obtiene la lista de art\u00EDculos de sesi\u00F3n
		Collection pedidos = new ArrayList();
		//BORRAR ------------------------
		LogSISPE.getLog().info("Llama a m\u00E9todo x6");
		// -------------------------------
		
		LogSISPE.getLog().info("---Entro por la funcion buscarPedidos creada ---");
		VistaPedidoDTO consultaVistaPedidoDTO = WebSISPEUtil.construirConsultaGeneralPedidos(request, formulario);
		consultaVistaPedidoDTO.setNpEstadoPedidosEntregados(estadoInactivo);
		//se obtiene el tab actual para saber donde estamos
		String tab = (String)session.getAttribute("ec.com.smx.sic.sispe.seccionPagina");
		if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion"))){
			consultaVistaPedidoDTO.setNpEstadoPedidosEntregados(estadoActivo);
		}
		//se guardan las variables de b\u00FAsqueda para utilizarlas posteriormente
		beanSession.setCodigoLocal(consultaVistaPedidoDTO.getId().getCodigoAreaTrabajo());
		beanSession.setCodigoReservacion(consultaVistaPedidoDTO.getLlaveContratoPOS());
		beanSession.setCodigoPedido(consultaVistaPedidoDTO.getId().getCodigoPedido());
//	    beanSession.setCedulaContacto(consultaVistaPedidoDTO.getCedulaContacto());
//	    beanSession.setNombreContacto(consultaVistaPedidoDTO.getNombreContacto());
		beanSession.setCedulaContacto(consultaVistaPedidoDTO.getNumeroDocumentoPersona());
		beanSession.setNombreContacto(consultaVistaPedidoDTO.getNombrePersona());
		beanSession.setNombreEmpresa(consultaVistaPedidoDTO.getNombreEmpresa());
		beanSession.setRucEmpresa(consultaVistaPedidoDTO.getRucEmpresa());
		//se guarda las fechas inicial y final de entrega para los pedidos
		beanSession.setFechaInicial(consultaVistaPedidoDTO.getNpPrimeraFechaEntregaInicial());
		beanSession.setFechaFinal(consultaVistaPedidoDTO.getNpPrimeraFechaEntregaFinal());
		beanSession.setOpEntidadResonsable(consultaVistaPedidoDTO.getEntidadResponsable());

		//se blanquea la colecci\u00F3n de datos antes de actualizar la lista
		formulario.setDatos(null);

		try{
			//llamada al m\u00E9todo de servicio para obtener la colecci\u00F3n
			pedidos = SessionManagerSISPE.getServicioClienteServicio().transObtenerPedidosEntregas(consultaVistaPedidoDTO, SessionManagerSISPE.getCurrentEntidadResponsable(request));
			if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion")))
			{
				//se guarda el listado de pedidos despachados
				session.setAttribute("ec.com.smx.sic.sispe.pedidosEntregados",pedidos);
				session.setAttribute(OBJ_BUSQUEDA, consultaVistaPedidoDTO);
			}
			else
				//se guarda el listado de pedidos despachados
				session.setAttribute("ec.com.smx.sic.sispe.pedidosPorEntregar",pedidos);

			//se realiza la paginaci\u00F3n si la colecci\u00F3n tiene elementos
			if(pedidos!=null && !pedidos.isEmpty())
			{
				LogSISPE.getLog().info("SE REALIZA LA PAGINACION");
				int size= pedidos.size();
				int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
				int start= 0;
				formulario.setStart(String.valueOf(start));
				formulario.setRange(String.valueOf(range));
				formulario.setSize(String.valueOf(size));

				Collection datosSub = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);

				formulario.setDatos(datosSub);
				//subo a sesi\u00F3n los datos en caso de crear reporte
				session.setAttribute(COL_DATOS_REPORTE, datosSub);
				beanSession.setInicioPaginacion(String.valueOf(start));

			}else{
				if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.impresion")))
					infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos Entregados"));
				else
					infos.add("listaVacia",new ActionMessage("message.listaVaciaBusqueda","Pedidos pendientes de Entregar"));
			}
			//si la b\u00FAsqueda se realiza cuando esta el contol en la secci\u00F3n del detalle
			if(tab!=null && tab.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.detallePedido")))
				session.setAttribute("ec.com.smx.sic.sispe.seccionPagina",
						MessagesWebSISPE.getString("ec.com.smx.sic.sispe.tab.pedidos"));
			//se inicializa la colecci\u00F3n de indices
			session.setAttribute("ec.com.smx.sic.sispe.entrega.indicesPedidos", new ArrayList());
			//se blanquean los checks
			formulario.setOpSeleccionAlgunos(null);
		}catch(SISPEException ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			errors.add("errorBusqueda",new ActionMessage("errors.llamadaServicio.general"));
		}
		
	}

}