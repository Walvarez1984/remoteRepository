/*
 * ClienteUtil.java
 * Creado el 16/06/2009 16:02:02
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.PERSONADTO_COL;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;

import ec.com.kruger.utilitario.dao.commons.dto.SearchResultDTO;
import ec.com.smx.corporativo.commons.util.CorporativoConstantes;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.ClientePedidoDTO;
import ec.com.smx.sic.sispe.web.pedidosEspeciales.struts.form.CrearPedidoForm;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings("unchecked")
public class ClienteUtil {
//	private static final String FILTRO_DOCUMENTO = "filtroDocumento";
//	private static final String FILTRO_NOMBRE1 = "filtroNombre1";
//	private static final String FILTRO_NOMBRE2 = "filtroNombre2";
//	private static final String FILTRO_APELLIDO1 = "filtroApellido1";
//	private static final String FILTRO_APELLIDO2 = "filtroApellido2";
//	private static final String FILTRO_RUC_EMPRESA = "filtroRucEmpresa";
//	private static final String FILTRO_NOMBRE_EMPRESA = "filtroNombreEmpresa";

	public static final String RADIO_SEL_PER = "radioSeleccionPersona";

	public static final String VAR_START_PAG = "valor.startPag";
	public static final String VAR_RANGE_PAG = "valor.rangePag";
	public static final String VAR_SIZE_PAG = "valor.sizePag";
	private static final String PLANTILLA_BUSQUEDA_CLIENTE = "ec.com.smx.sic.sispe.plantillaBusqueda.cliente";

//	/**
//	 * Obtiene los datos del cliente
//	 * 
//	 * @param request
//	 * @param documentoCliente
//	 * @param formulario
//	 * @throws Exception
//	 */
//	public static void obtenerDatosCliente(HttpServletRequest request, String documentoCliente, ActionForm formulario, ActionMessages errors)throws Exception{
//
//		LogSISPE.getLog().info("entra a consultar el cliente");
//		PedidoDTO consultaCliente = new PedidoDTO();
//		consultaCliente.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		consultaCliente.setCedulaContacto(documentoCliente.trim());
//		PedidoDTO pedidoDTO = SISPEFactory.obtenerServicioSispe().transObtenerCliente(consultaCliente);
//
//		if(pedidoDTO != null){
//			String valorNA = MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.noAplica");
//			//se verifica la instancia del formulario
//			if(formulario instanceof CotizarReservarForm){
//				CotizarReservarForm cotizarReservarForm = (CotizarReservarForm)formulario;
//
//				//se llenan los campos restantes del cliente
//				cotizarReservarForm.setNombreContacto(pedidoDTO.getNombreContacto());
//				cotizarReservarForm.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
//				cotizarReservarForm.setTipoDocumentoContacto(pedidoDTO.getTipoDocumento());
//				//para el contexto del pedido EMPRESARIAL o INDIVIDUAL
//				cotizarReservarForm.setOpTipoDocumento(pedidoDTO.getContextoPedido());
//
//				//datos de la empresa
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					cotizarReservarForm.setRucEmpresa(pedidoDTO.getRucEmpresa());
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					cotizarReservarForm.setNombreEmpresa(pedidoDTO.getNombreEmpresa());
//
//			}else if(formulario instanceof CrearPedidoForm){
//				CrearPedidoForm crearPedidoForm = (CrearPedidoForm)formulario;
//
//				//se llenan los campos restantes del cliente
//				crearPedidoForm.setNombreContacto(pedidoDTO.getNombreContacto());
//				crearPedidoForm.setTelefonoContacto(pedidoDTO.getTelefonoContacto());
//				crearPedidoForm.setOpTipoDocumentoContacto(pedidoDTO.getTipoDocumento());
//				//para el contexto del pedido EMPRESARIAL o INDIVIDUAL
//				crearPedidoForm.setOpTipoDocumento(pedidoDTO.getContextoPedido());
//
//				//datos de la empresa
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					crearPedidoForm.setRucEmpresa(pedidoDTO.getRucEmpresa());
//				if(!pedidoDTO.getRucEmpresa().equals(valorNA))
//					crearPedidoForm.setNombreEmpresa(pedidoDTO.getNombreEmpresa());
//			}
//		}else{
//			errors.add("cedulaContacto",new ActionMessage("errors.persona.noencontrada"));			
//		}
//
//		consultaCliente = null;
//	}


//	/**
//	 * Obtiene los datos de las personas en base a algunos filtros
//	 * 
//	 * @param request
//	 */
//	public static void buscarPersona(HttpServletRequest request)throws Exception{
//
//		HttpSession session = request.getSession();
//
//		LogSISPE.getLog().info("Se ingresa al m\u00E9todo buscarPersona...");
//
//		//se obtienen los par\u00E1metros de b\u00FAsqueda
//		ClientePedidoDTO clientePedidoFiltro = new ClientePedidoDTO();
//		clientePedidoFiltro.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
//		clientePedidoFiltro.setPersonaDTO(new PersonaDTO());
//		//clientePedidoFiltro.setEmpresaDTO(new EmpresaDTO());
//		clientePedidoFiltro.setLocalizacionDTO(new LocalizacionDTO());
//		clientePedidoFiltro.getLocalizacionDTO().setEmpresaDTO(new EmpresaDTO());
//		
//		boolean existenFiltros = false;
//
//		if(request.getParameter(FILTRO_DOCUMENTO) != null && !request.getParameter(FILTRO_DOCUMENTO).equals("")){
//			clientePedidoFiltro.getPersonaDTO().setNumeroDocumento(request.getParameter(FILTRO_DOCUMENTO));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_NOMBRE1) != null && !request.getParameter(FILTRO_NOMBRE1).equals("")){
//			clientePedidoFiltro.getPersonaDTO().setPrimerNombre(request.getParameter(FILTRO_NOMBRE1));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_NOMBRE2) != null && !request.getParameter(FILTRO_NOMBRE2).equals("")){
//			clientePedidoFiltro.getPersonaDTO().setSegundoNombre(request.getParameter(FILTRO_NOMBRE2));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_APELLIDO1) != null && !request.getParameter(FILTRO_APELLIDO1).equals("")){
//			clientePedidoFiltro.getPersonaDTO().setPrimerApellido(request.getParameter(FILTRO_APELLIDO1));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_APELLIDO2) != null && !request.getParameter(FILTRO_APELLIDO2).equals("")){
//			clientePedidoFiltro.getPersonaDTO().setSegundoApellido(request.getParameter(FILTRO_APELLIDO2));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_RUC_EMPRESA) != null && !request.getParameter(FILTRO_RUC_EMPRESA).equals("")){
//			//clientePedidoFiltro.getEmpresaDTO().setNumeroRuc(request.getParameter(FILTRO_RUC_EMPRESA));
//			clientePedidoFiltro.getLocalizacionDTO().getEmpresaDTO().setNumeroRuc(request.getParameter(FILTRO_RUC_EMPRESA));
//			existenFiltros = true;
//		}
//		if(request.getParameter(FILTRO_NOMBRE_EMPRESA) != null && !request.getParameter(FILTRO_NOMBRE_EMPRESA).equals("")){
//			//se asigna el filtro del nombre de la empresa en la direcci\u00F3n de trabajo
//			//clientePedidoFiltro.getEmpresaDTO().setNombreComercialEmpresa(request.getParameter(FILTRO_NOMBRE_EMPRESA));
//			clientePedidoFiltro.getLocalizacionDTO().getEmpresaDTO().setNombreComercialEmpresa(request.getParameter(FILTRO_NOMBRE_EMPRESA));
//			existenFiltros = true;
//		}
//		
//		
//		//Se realiza la b\u00FAsqueda
//		if(existenFiltros){
//			session.setAttribute(PLANTILLA_BUSQUEDA_CLIENTE, clientePedidoFiltro);
//			//  Se setean los par\u00E1metros correspondientes a procesos de paginaci\u00F3n
//			// Valor por defecto para par\u00E1metro de INICIO y RANGO en la primera b\u00FAsqueda para los valores de AreasTrabajoForm desde BaseForm
//			// - start
//			// - range
//			// - size  	
//
//			int start = 0;
//			int range = (Integer.valueOf(MessagesWebSISPE.getString("parametro.paginacion.rango")));
//
//			// Establecer par\u00E1metros de paginaci\u00F3n para el objeto que se env\u00EDa al m\u00E9todo de paginaci\u00F3n				
//			clientePedidoFiltro.getPersonaDTO().setFirstResult(0);
//			clientePedidoFiltro.getPersonaDTO().setMaxResults(Integer.valueOf(MessagesWebSISPE.getString("parametro.paginacion.rango")));
//
//			// Se realiza la b\u00FAsqueda en base a m\u00E9todos de paginaci\u00F3n en base
//			SearchResultDTO<ClientePedidoDTO> resultado = null;//SessionManagerSISPE.getServicioClienteServicio().transObtenerClientesPaginado(clientePedidoFiltro);
//			
////			if(resultado != null){
////				// Se almacena en session el resultado de la b\u00FAsqueda paginada en base
////				request.getSession().setAttribute(PERSONADTO_COL, resultado.getResults());
////	
////				session.setAttribute(WebSISPEUtil.START_PAG, String.valueOf(start));
////				session.setAttribute(WebSISPEUtil.RANGE_PAG, String.valueOf(range));
////				session.setAttribute(WebSISPEUtil.SIZE_PAG, String.valueOf(resultado.getCountResults()));
////			}else{
//				session.removeAttribute(WebSISPEUtil.START_PAG);
//				session.removeAttribute(WebSISPEUtil.RANGE_PAG);
//				session.removeAttribute(WebSISPEUtil.SIZE_PAG);	
//
//				// Anulaci\u00F3n de variable de listado de resutados
//				session.removeAttribute(PERSONADTO_COL);
////			}
//		}else{
//			session.removeAttribute(WebSISPEUtil.START_PAG);
//			session.removeAttribute(WebSISPEUtil.RANGE_PAG);
//			session.removeAttribute(WebSISPEUtil.SIZE_PAG);	
//
//			// Anulaci\u00F3n de variable de listado de resutados
//			session.removeAttribute(PERSONADTO_COL);
//		}
//		clientePedidoFiltro = null;
//	}


	/**
	 * Permite navegar el resultado de datos de las personas en base a algunos filtros
	 * 
	 * @param request, start
	 */
	public static void paginarListadoPersonas(HttpServletRequest request, String start){
		LogSISPE.getLog().info("Se procede a paginar el listado de personas...");
		HttpSession session = request.getSession();	

		// Se obtiene la plantilla de b\u00FAsqueda inicial
		LogSISPE.getLog().info("Start: {}",start);
		ClientePedidoDTO clientePedidoDTO = (ClientePedidoDTO)session.getAttribute(PLANTILLA_BUSQUEDA_CLIENTE);
		clientePedidoDTO.getPersonaDTO().setCountAgain(false);
		clientePedidoDTO.getPersonaDTO().setFirstResult(Integer.valueOf(start));
		try{
			//Se realiza la b\u00FAsqueda en base a m\u00E9todos de paginaci\u00F3n en base
			SearchResultDTO<ClientePedidoDTO> resultado = null;//SessionManagerSISPE.getServicioClienteServicio().transObtenerClientesPaginado(clientePedidoDTO);
			
			request.getSession().setAttribute(PERSONADTO_COL, resultado.getResults());	

			session.setAttribute(WebSISPEUtil.START_PAG, start);
			LogSISPE.getLog().info("--Start: {}",session.getAttribute(WebSISPEUtil.START_PAG));
			LogSISPE.getLog().info("--Range: {}",session.getAttribute(WebSISPEUtil.RANGE_PAG));
			LogSISPE.getLog().info("--Size: {}",session.getAttribute(WebSISPEUtil.SIZE_PAG));

		}catch (Exception e) {
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
	}		

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static void asignarPersonaSeleccionada(HttpServletRequest request, ActionForm formulario){
		//se obtiene la persona seleccionada
		List<ClientePedidoDTO> listClientes = (List<ClientePedidoDTO>)request.getSession().getAttribute(PERSONADTO_COL);
		
		if(request.getParameter(RADIO_SEL_PER)!=null){
			ClientePedidoDTO clientePedidoDTO = listClientes.get(Integer.parseInt(request.getParameter(RADIO_SEL_PER)));

			//se verifica la instancia del formulario
			if(formulario instanceof CotizarReservarForm){
				CotizarReservarForm cotizarReservarForm = (CotizarReservarForm)formulario;

				//se llenan los campos restantes del cliente
				cotizarReservarForm.setNumeroDocumento(clientePedidoDTO.getPersonaDTO().getNumeroDocumento());
				cotizarReservarForm.setNombrePersona(clientePedidoDTO.getPersonaDTO().getNombreCompleto());
				if(clientePedidoDTO.getPersonaDTO().getTelefonoDomicilio() != null)
					cotizarReservarForm.setTelefonoPersona(clientePedidoDTO.getPersonaDTO().getTelefonoDomicilio());
				else if(clientePedidoDTO.getPersonaDTO().getTelefonoCelular() != null)
					cotizarReservarForm.setTelefonoPersona(clientePedidoDTO.getPersonaDTO().getTelefonoCelular());

				//se verifica el tipo de documento
				if(!clientePedidoDTO.getPersonaDTO().getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
					cotizarReservarForm.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.pasaporte"));
				}else{
					cotizarReservarForm.setTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"));
				}

				//if(clientePedidoDTO.getEmpresaDTO()!= null){
				if(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO()!= null){	
					//para el contexto del pedido INDIVIDUAL
					cotizarReservarForm.setOpTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
	
					//cotizarReservarForm.setRucEmpresa(clientePedidoDTO.getEmpresaDTO().getNumeroRuc());
					cotizarReservarForm.setRucEmpresa(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO().getNumeroRuc());
					//cotizarReservarForm.setNombreEmpresa(clientePedidoDTO.getEmpresaDTO().getNombreComercialEmpresa());
					cotizarReservarForm.setNombreEmpresa(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO().getNombreComercialEmpresa());
				}else{
					//para el contexto del pedido INDIVIDUAL
					cotizarReservarForm.setOpTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
	
					cotizarReservarForm.setRucEmpresa(null);
					cotizarReservarForm.setNombreEmpresa(null);
				}
			}else if(formulario instanceof CrearPedidoForm){
				CrearPedidoForm pedidoEspecialForm = (CrearPedidoForm)formulario;

				//se llenan los campos restantes del cliente
				pedidoEspecialForm.setNumeroDocumento(clientePedidoDTO.getPersonaDTO().getNumeroDocumento());
				pedidoEspecialForm.setNombreContacto(clientePedidoDTO.getPersonaDTO().getNombreCompleto());
				if(clientePedidoDTO.getPersonaDTO().getTelefonoDomicilio() != null)
					pedidoEspecialForm.setTelefonoContacto(clientePedidoDTO.getPersonaDTO().getTelefonoDomicilio());
				else if(clientePedidoDTO.getPersonaDTO().getTelefonoCelular() != null)
					pedidoEspecialForm.setTelefonoContacto(clientePedidoDTO.getPersonaDTO().getTelefonoCelular());

				//se verifica el tipo de documento
				if(!clientePedidoDTO.getPersonaDTO().getTipoDocumento().equals(CorporativoConstantes.TIPO_DOCUMENTO_CEDULA)){
					pedidoEspecialForm.setOpTipoDocumentoContacto(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.pasaporte"));
				}else{
					pedidoEspecialForm.setOpTipoDocumentoContacto(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipoDocumento.cedula"));
				}

				//if(clientePedidoDTO.getEmpresaDTO()!= null){
				if(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO()!= null){	
					//para el contexto del pedido INDIVIDUAL
					pedidoEspecialForm.setOpTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.empresarial"));
	
					//pedidoEspecialForm.setRucEmpresa(clientePedidoDTO.getEmpresaDTO().getNumeroRuc());
					pedidoEspecialForm.setRucEmpresa(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO().getNumeroRuc());
					//pedidoEspecialForm.setNombreEmpresa(clientePedidoDTO.getEmpresaDTO().getNombreComercialEmpresa());
					pedidoEspecialForm.setNombreEmpresa(clientePedidoDTO.getLocalizacionDTO().getEmpresaDTO().getNombreComercialEmpresa());
				}else{
					//para el contexto del pedido INDIVIDUAL
					pedidoEspecialForm.setOpTipoDocumento(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.pedido.tipo.individual"));
	
					pedidoEspecialForm.setRucEmpresa(null);
					pedidoEspecialForm.setNombreEmpresa(null);
				}
			}

			//se elimina la ventana y la colecci\u00F3n de personas
			request.getSession().removeAttribute(SessionManagerSISPE.POPUP);
			request.getSession().removeAttribute(PERSONADTO_COL);
		}
	}

	/*
	 * M\u00E9todo utilizado para poder cargar el pop - up de b\u00FAsqueda de personas
	 * */
	public static UtilPopUp crearPopUpBusquedaClientes(){
		UtilPopUp popUp = new UtilPopUp();
		popUp.setTituloVentana("B\u00FAsqueda de clientes");
		popUp.setFormaBotones(UtilPopUp.OK_CANCEL);
		popUp.setContenidoVentana("servicioCliente/cotizarRecotizarReservar/busquedaPersonas.jsp");
		popUp.setFormaEnvioDatos(UtilPopUp.PERSONALIZADO);
		popUp.setTope(0d);
		popUp.setAncho(85d);

		return popUp;
	}
}