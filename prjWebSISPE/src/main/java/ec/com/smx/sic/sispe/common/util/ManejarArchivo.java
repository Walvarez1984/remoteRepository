/*
 * CrearArchivo.java
 * Creado el 28/03/2008 14:54:52
 *   	
 */
package ec.com.smx.sic.sispe.common.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;

import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoDTO;
import ec.com.smx.sic.sispe.dto.ArchivoPedidoID;
import ec.com.smx.sic.sispe.dto.EntregaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.ListadoPedidosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * @author nperalta
 *
 */
public class ManejarArchivo {
	
	public static final String RESUMEN_ENTREGAS = "ec.com.smx.sic.sispe.resumen.entregas";
	
	
	public static int copy(InputStream input, OutputStream output) {

		int caracter = 0;
		try{
			caracter = input.read();
		
			int finArchivo = -1;
			while( caracter != finArchivo ) { 
				output.write( caracter );
				caracter = input.read();
			}
			input.close();
			output.close();
			LogSISPE.getLog().info("el archivo fue creado");
		} catch (IOException e1) {
			// TODO Bloque catch generado autom\u00E1ticamente
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e1);
		}
		return caracter;
	}
	
	public void mostrarPopupArchivoCroquis(HttpServletRequest request, HttpSession session, String indicePedido, String accionEstado) throws Exception{
		
		LogSISPE.getLog().info("se va a mostrar el popUp adjuntar archivocroquis de la entrega: "+ indicePedido);
		
		if(request.getParameter("indiceEntregaCroquis") != null && session.getAttribute(RESUMEN_ENTREGAS) != null){
			List<EntregaPedidoDTO> entregas = (List<EntregaPedidoDTO>) session.getAttribute(RESUMEN_ENTREGAS);
			int indice = Integer.valueOf(request.getParameter("indiceEntregaCroquis"));
			EntregaPedidoDTO entregaSeleccionadaDTO = entregas.get(indice);
    	  	LogSISPE.getLog().info("Entra a mostrar la ventana para subir el archivo del croquis en el detalle de entregas");
			instanciarVentanaSubirArchivoCroquis(request, accionEstado);
			listarArchivosCroquis(request, entregaSeleccionadaDTO);
			session.setAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis", entregaSeleccionadaDTO);
		}
		else{
			LogSISPE.getLog().info("Entra a mostrar la ventana para subir el archivo del croquis en el calendario entrega bodega");
			instanciarVentanaSubirArchivoCroquis(request, accionEstado);
		}
	}
	
	public void adjuntarArchivoCroquis(HttpServletRequest request, HttpSession session, ActionForm formulario, String accionEstado) throws Exception{

		 LogSISPE.getLog().info("se carga el archivo seleccionado");
		 LogSISPE.getLog().info("Boton Aceptar---------");
		 
		 ManejarArchivo manejarArchivo = new ManejarArchivo();
		 
		 //Obtenemos de sesion la entrega seleccionada
		 EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
		 String codigoLocal = entregaPedidoDTO.getCodigoAreaTrabajoEstadoPedido().toString();
		 String pedido = entregaPedidoDTO.getCodigoPedido();
		
		 boolean verificarArchivo = true;
		 LogSISPE.getLog().info("CodPedido->{}",pedido);
		 LogSISPE.getLog().info("CodLocal->{}",codigoLocal);
		 //se arma el archivo pedido
		 ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
		 ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		 archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		 archivoPedidoDTO.setId(archivoPedidoID);
		 archivoPedidoDTO.setCodigoLocal(Integer.parseInt(codigoLocal));
		 archivoPedidoDTO.setCodigoPedido(pedido);
		 
		 String fileName ="";
		 
		 if (formulario instanceof CotizarReservarForm) {
			 LogSISPE.getLog().info("CotizarReservarForm");
			 CotizarReservarForm formularioIns = (CotizarReservarForm) formulario;	
			 archivoPedidoDTO.setNombreArchivo(formularioIns.getArchivo().getFileName());
			 archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formularioIns.getArchivo().getFileSize()));
			 archivoPedidoDTO.setArchivo(formularioIns.getArchivo().getFileData());
			 fileName = formularioIns.getArchivo().getFileName();
		 } else if (formulario instanceof ListadoPedidosForm) {
			 LogSISPE.getLog().info("ListadoPedidosForm");
			 ListadoPedidosForm formularioIns = (ListadoPedidosForm) formulario;
			 archivoPedidoDTO.setNombreArchivo(formularioIns.getArchivo().getFileName());
			 archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formularioIns.getArchivo().getFileSize()));
			 archivoPedidoDTO.setArchivo(formularioIns.getArchivo().getFileData());
			 fileName = formularioIns.getArchivo().getFileName();
		 }
		 
		 archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.CroquisEntrega"));
		 archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		 archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		 //verifica si el nuevo archivo no tiene el mismo nombre.
		 verificarArchivo = CotizacionReservacionUtil.validarArchivo(request, fileName);
		 
		 //se validan archivos repetidos o mas de un archivo
		 if(verificarArchivo && entregaPedidoDTO.getArchivoEntregaPedidoDTO() == null){
		 	LogSISPE.getLog().info("Archivo Diferente");
		  	//se guarda el archivo pedido de la entrega seleccionada
		 	manejarArchivo.aceptarArchivoCroquis(request,archivoPedidoDTO);
		 	entregaPedidoDTO.setArchivoEntregaPedidoDTO(archivoPedidoDTO);
		 	entregaPedidoDTO.setSecuencialArchivoPedido(archivoPedidoDTO.getId().getSecuencialArchivoPedido());
		 	SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entregaPedidoDTO);
		 } else {
		   	if(verificarArchivo == false){
		   		LogSISPE.getLog().info("Archivo repetido");
			   	String mensaje = "El archivo ya existe, \u00BFdesea reemplazarlo?";
			   	manejarArchivo.instanciarVentanaAuxiliarArchivoCroquis(request, accionEstado, mensaje);
		   	}else{
		   		LogSISPE.getLog().info("Intenta ingresar mas de un archivo");
		   		String mensaje = "Solo se puede adjuntar un archivo por cada entrega, \u00BFdesea reemplazar el archivo?";
		   		manejarArchivo.instanciarVentanaAuxiliarArchivoCroquis(request, accionEstado, mensaje);
		   	}
			
		 }	
	}
	
	public void eliminarArchivoCroquis(HttpServletRequest request, HttpSession session) throws Exception{
		ManejarArchivo manejarArchivo = new ManejarArchivo();
		//Obtenemos de sesion la entrega seleccionada
		EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
		if(entregaPedidoDTO != null && entregaPedidoDTO.getSecuencialArchivoPedido() != null){
			
			eliminarReferenciaArchivoEntregaPedido(request, entregaPedidoDTO);
			 
			Long secuencialArchivo = Long.valueOf(entregaPedidoDTO.getSecuencialArchivoPedido());
			String pedido = entregaPedidoDTO.getCodigoPedido();
			//se cancela el archivo del beneficiario 
			LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",secuencialArchivo);
			LogSISPE.getLog().info("Boton Eliminar de la lista de archivos",pedido);
			CotizacionReservacionUtil.eliminarArchivoBeneficiario(request, secuencialArchivo, pedido);
			Collection<ArchivoPedidoDTO> colFiles = (Collection<ArchivoPedidoDTO>) session.getAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE);
			colFiles.removeAll(colFiles);
			
			entregaPedidoDTO.setSecuencialArchivoPedido(null);
			entregaPedidoDTO.setArchivoEntregaPedidoDTO(null);
		}
	}
	
	public void remplazarArchivoCroquisAntIngresado(HttpServletRequest request, HttpSession session, ActionForm formulario) throws Exception{
		LogSISPE.getLog().info("elimina el archivo existente, carga el nuevo y se cierra el popUp de remplazo de archivo croquis");
		//elimina el archivo existente
		eliminarArchivoCroquis(request, session);
		//carga el nuevo archivo
		LogSISPE.getLog().info("se carga el archivo seleccionado");
		LogSISPE.getLog().info("Boton Aceptar---------");
		 
		//Obtenemos de sesion la entrega seleccionada
		EntregaPedidoDTO entregaPedidoDTO = (EntregaPedidoDTO)session.getAttribute("ec.com.smx.sic.sispe.entregaSelccionadaCroquis");
		String codigoLocal = entregaPedidoDTO.getCodigoAreaTrabajoEstadoPedido().toString();
		String pedido = entregaPedidoDTO.getCodigoPedido();
		
		LogSISPE.getLog().info("CodPedido->{}",pedido);
		LogSISPE.getLog().info("CodLocal->{}",codigoLocal);
		//se arma el archivo pedido
		ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
		ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		archivoPedidoID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		archivoPedidoDTO.setId(archivoPedidoID);
		archivoPedidoDTO.setCodigoLocal(Integer.parseInt(codigoLocal));
		archivoPedidoDTO.setCodigoPedido(pedido);
		
		 if (formulario instanceof CotizarReservarForm) {
			 LogSISPE.getLog().info("CotizarReservarForm");
			 CotizarReservarForm formularioIns = (CotizarReservarForm) formulario;	
			 archivoPedidoDTO.setNombreArchivo(formularioIns.getArchivo().getFileName());
			 archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formularioIns.getArchivo().getFileSize()));
			 archivoPedidoDTO.setArchivo(formularioIns.getArchivo().getFileData());
		 } else if (formulario instanceof ListadoPedidosForm) {
			 LogSISPE.getLog().info("ListadoPedidosForm");
			 ListadoPedidosForm formularioIns = (ListadoPedidosForm) formulario;
			 archivoPedidoDTO.setNombreArchivo(formularioIns.getArchivo().getFileName());
			 archivoPedidoDTO.setTamanioArchivo(Long.valueOf(formularioIns.getArchivo().getFileSize()));
			 archivoPedidoDTO.setArchivo(formularioIns.getArchivo().getFileData());
		 }
		
		archivoPedidoDTO.setTipoArchivo(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipo.archivo.CroquisEntrega"));
		archivoPedidoDTO.setUserId(SessionManagerSISPE.getDefault().getLoggedUser(request).getUserId());
		archivoPedidoDTO.setEstado(CorporativoConstantes.ESTADO_ACTIVO);
		 
		//se guarda el archivo pedido de la entrega seleccionada
		aceptarArchivoCroquis(request,archivoPedidoDTO);
		entregaPedidoDTO.setArchivoEntregaPedidoDTO(archivoPedidoDTO);
		entregaPedidoDTO.setSecuencialArchivoPedido(archivoPedidoDTO.getId().getSecuencialArchivoPedido());
		SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entregaPedidoDTO);
		
		session.removeAttribute(SessionManagerSISPE.POPUPAUX);
	}
	
	/**
	 * Configura el popUp de carga de archivo croquis
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author IOnofre
	 */
	private static void instanciarVentanaSubirArchivoCroquis(HttpServletRequest request,String accion)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro en instanciarVentanaSubirArchivoCroquis");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Cargar archivo Croquis");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'cancelarArchCroquis=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarArchCroquis=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setContenidoVentana("servicioCliente/confirmarReservacion/mostrarVentanaArchivoBene.jsp");
		popUp.setAccionEnvio(accion);
		popUp.setAncho(50D);
		popUp.setTope(40D);
		session.setAttribute(SessionManagerSISPE.POPUP, popUp);
		popUp = null;
	}
	
	/**
	 * Lista los archivos cargados de croquis
	 * @param request
	 * @param entregaPedidoDTO
	 * @throws Exception
	 * @author IOnofre
	 */
	private static void listarArchivosCroquis(HttpServletRequest request, EntregaPedidoDTO entregaPedidoDTO)throws Exception{
		LogSISPE.getLog().info("Entro listar los archivos de croquis");
		HttpSession session = request.getSession();		
		Collection<ArchivoPedidoDTO> colFiles = new ArrayList<ArchivoPedidoDTO>();
		if(entregaPedidoDTO.getArchivoEntregaPedidoDTO() != null){
			colFiles.add(entregaPedidoDTO.getArchivoEntregaPedidoDTO());
		}
	    
	    //se sube a sesion el total de los tama\u00F1os de los archivos subidos actualmente
		session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
	}
	
	/**
	 * Acepta el archivo de croquis
	 * @param request
	 * @param archivoArmado
	 * @throws Exception
	 * @author Ionofre
	 */
	public void aceptarArchivoCroquis(HttpServletRequest request,ArchivoPedidoDTO archivoArmado)throws Exception{
		LogSISPE.getLog().info("Entro a aceptarArchivoCroquis");
		HttpSession session = request.getSession();
		//ArchivoPedidoID archivoPedidoID = new ArchivoPedidoID();
	    ArchivoPedidoDTO archivoPedidoDTO = new ArchivoPedidoDTO();
		//seteo el codigo del pedido
		archivoPedidoDTO.setCodigoPedido(archivoArmado.getCodigoPedido());
		archivoPedidoDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
		try{			    
		    //entra al servicio de guardar en la base el archivo beneiciario
		    SessionManagerSISPE.getServicioClienteServicio().transCrearArchivoBenficiario(archivoArmado);
		    //Entra al servicio para obtener lo archivos del pedidobeneficiario.
		    Collection<ArchivoPedidoDTO> colFiles = new ArrayList<ArchivoPedidoDTO>();
		    colFiles.add(archivoArmado);
			//se sube a sesion los archivo filtrados por codigoPedido
			session.setAttribute(SessionManagerSISPE.COL_ARCHIVO_BENE, colFiles);
			LogSISPE.getLog().info("size files ->"+colFiles.size());
		} catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			throw e;
		}	
	}
	
	/**
	 * PopUp de pregunta, si deseea sobreescribir el archivo existente
	 * @param request
	 * @param accion
	 * @throws Exception
	 * @author IOnofre
	 */
	public void instanciarVentanaAuxiliarArchivoCroquis(HttpServletRequest request,String accion, String mensaje)throws Exception{
		HttpSession session = request.getSession();
		LogSISPE.getLog().info("Entro a instanciar la ventana de confirmacion sobre escribir archivo croquis");
		LogSISPE.getLog().info("Valor de la accion->{}", accion);
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("Archivo Croquis");
		popUp.setEtiquetaBotonOK("Si");
		popUp.setEtiquetaBotonCANCEL("No");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setValorOK("requestAjax('"+accion+"', ['pregunta','archivosCargados'], {parameters: 'reemplazarArchivo=ok', evalScripts:true});ocultarModal();");
		popUp.setValorCANCEL("requestAjax('"+accion+"', ['pregunta'], {parameters: 'cancelarReemplazo=ok', popWait:false, evalScripts:true});ocultarModal();");
		popUp.setAccionEnvioCerrar(popUp.getValorCANCEL());
		popUp.setPreguntaVentana(mensaje);
		session.setAttribute(SessionManagerSISPE.POPUPAUX, popUp);
		popUp = null;
	}
	
	/**
	 * Obtiene datos de entregaPedidoDTO y actualiza de la base el secuencialArchivoEntregaPedidoDTO.
	 * @param request
	 * @param entregaPedidoDTO
	 * @throws SISPEException
	 * @throws Exception
	 * @author IOnofre
	 */
	public void eliminarReferenciaArchivoEntregaPedido(HttpServletRequest request, EntregaPedidoDTO entregaPedidoDTO) throws SISPEException, Exception{
		EntregaPedidoDTO entregaPedidoConsultaDTO = new EntregaPedidoDTO();
		entregaPedidoConsultaDTO.setSecuencialArchivoPedido(entregaPedidoDTO.getSecuencialArchivoPedido());
		entregaPedidoConsultaDTO.getId().setCodigoCompania(entregaPedidoDTO.getId().getCodigoCompania());
		Collection<EntregaPedidoDTO> entregasPedidoCol = SessionManagerSISPE.getServicioClienteServicio().findObtenerEntregaPedidoDTO(entregaPedidoConsultaDTO);
		 
		if(CollectionUtils.isNotEmpty(entregasPedidoCol)){
			LogSISPE.getLog().info("se eliminara la referencia de {} entregasPedidoDTO",entregasPedidoCol.size());
			for(EntregaPedidoDTO entPedDTO : entregasPedidoCol){
				entPedDTO.setSecuencialArchivoPedido(null);
				entPedDTO.setArchivoEntregaPedidoDTO(null);
				SessionManagerSISPE.getServicioClienteServicio().transActualizarSecuencialArchivoEntregaPedidoDTO(entPedDTO); 
			}
		}
	}
	
}