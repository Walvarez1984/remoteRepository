/*
 * CatalogoCanastosAction.java
 * Creado el 03/03/2008 14:20:18
 *   	
 */
package ec.com.smx.sic.sispe.web.administracion.struts.action;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.mensajeria.commons.resources.MensajeriaMessages;
import ec.com.smx.mensajeria.dto.EventoDTO;
import ec.com.smx.mensajeria.dto.id.EventoID;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloRelacionDTO;
import ec.com.smx.sic.cliente.mdl.dto.ParametroDTO;
import ec.com.smx.sic.sispe.common.util.ManejarArchivo;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.ArticuloDatoImagenDTO;
import ec.com.smx.sic.sispe.web.administracion.struts.form.CatalogoCanastosForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * @author nperalta
 *
 */
@SuppressWarnings("unchecked")
public class CatalogoCanastosAction extends BaseAction
{
	public static final String COL_ARTICULOS = "ec.com.smx.sic.sispe.canastos";
	static final String COL_ARTICULOS_SELECCIONADOS = "ec.com.smx.sic.sispe.canastos.seleccionados";
	static final String ARTICULO_DTO = "ec.com.smx.sic.sispe.articuloDTO";
	static final String CODIGO_CANASTO_VACIO = "ec.com.smx.sic.sispe.codigoCanastoVacio"; 
	static final String DIRECTORIO_IMAGEN = "ec.com.smx.sic.sispe.directorio.imagenes.canastos";
	static final String SEPARADOR_DIRECTORIO = "ec.com.smx.sic.sispe.separador.directorios";


	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo
	 * redirige a otro componente web que podr\u00EDa crear) Devuelve una instancia <code>ActionForward</code> que
	 * describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form					El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          						campos
	 * @param request					El request que estamos procesando
	 * @param response 				La respuesta HTTP que se crea
	 * @throws Exception
	 * @return ActionForward	Describe donde y como se redirige el control
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{

		ActionMessages exitos = new ActionMessages();

		//se obtiene el estado activo del sistema
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		CatalogoCanastosForm formulario= (CatalogoCanastosForm)form;
		//se obtiene la sesi\u00F3n  
		HttpSession session = request.getSession();
		String forward = "catalogoCanastos";

		//Si ya fue enviado el mail elimino la variable de sesion
		if(request.getParameter("aceptarEnvioMail")==null){
			session.removeAttribute("ec.com.smx.sic.sispe.envioMail");
		}

		if(request.getParameter("detalleCanasto")!=null){
			//toma el \u00EDndice del canasto seleccionado
			int indiceArticulo = Integer.parseInt(request.getParameter("detalleCanasto"));

			//Array que guarda los articulos del canasto
			List<ArticuloDatoImagenDTO> articuloDatoImagenDTOcol = (List<ArticuloDatoImagenDTO>)session.getAttribute(COL_ARTICULOS);
			//---Aqu\u00ED se guardan los articulos del canasto especificado
			ArticuloDatoImagenDTO canasto = articuloDatoImagenDTOcol.get(indiceArticulo);
			LogSISPE.getLog().info("**NOMBRE**: " + canasto.getDescripcionArticulo());

			//se verifica si la colecci\u00F3n de la receta est\u00E1 vac\u00EDa
			if(canasto.getRecetaArticulos()==null){
				//se crea la consulta para traer los items del canasto
				ArticuloRelacionDTO consultaRecetaArticuloDTO = new ArticuloRelacionDTO();
				consultaRecetaArticuloDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request)
						.getId().getCodigoCompania());
				consultaRecetaArticuloDTO.getId().setCodigoArticulo(canasto.getId().getCodigoArticulo());
				consultaRecetaArticuloDTO.setEstado(estadoActivo);
				consultaRecetaArticuloDTO.setArticulo(new ArticuloDTO());
				consultaRecetaArticuloDTO.getArticulo().setNpCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));

				//Obtener datos de la colecci\u00F3n de recetas
				Collection recetaArticulo = SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticulo(consultaRecetaArticuloDTO, true);
				canasto.setRecetaArticulos(recetaArticulo);
			}
			//subir articulos de la canasta seleccionada a sesion.
			session.setAttribute(ARTICULO_DTO, canasto);
			forward="detalleCanasto";
		}
		else if(request.getParameter("ingresoMailContacto")!=null){
			LogSISPE.getLog().info("entra a abrir el mail");

			//carga en sesi\u00F3n la ventana para ingresar el email.
			session.setAttribute("ec.com.smx.sic.sispe.envioMailVentana","ok");
		}
		//si se hace clic en el boton Aceptar
		else if(request.getParameter("aceptarEnvioMail")!=null)
		{
			//Array que guarda los articulos del especial
			List<ArticuloDatoImagenDTO> colArticuloDatoImagenDTO =(List<ArticuloDatoImagenDTO>)session.getAttribute(COL_ARTICULOS);
			//Array que guardar\u00E1 los art\u00EDculos seleccionados
			Collection<ArticuloDatoImagenDTO> articulos = new ArrayList<ArticuloDatoImagenDTO>();

			//obtiene los checkbox seleccionados
			String[] seleccionados = formulario.getOpEscogerSeleccionado();
			//si se han seleccionado canastos
			if(seleccionados!=null){

				LogSISPE.getLog().info("opEscogerSeleccionado: " + seleccionados.length);
				Integer codigoCompania = SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania();
				String rutasImagenes = "";
				//se iteran los art\u00EDculos seleccionados
				for(int i=0;i<seleccionados.length;i++)	{
					byte[] bfile = null;
					ArticuloDatoImagenDTO articuloDTO = colArticuloDatoImagenDTO.get((new Integer(seleccionados[i])).intValue());

					//almacenar los bytes de la imagen del articulo
					bfile= articuloDTO.getDatoImagen();
					if(bfile!=null)
					{
						LogSISPE.getLog().info("DATO IMAGEN***: "+ bfile);
						//obtener el path donde se guardar\u00E1n las im\u00E1genes de los articulos seleccionados
						String filePath = getServlet().getServletContext().getRealPath(MessagesAplicacionSISPE.getString(DIRECTORIO_IMAGEN)).concat(MessagesAplicacionSISPE.getString(SEPARADOR_DIRECTORIO));
						LogSISPE.getLog().info("PATH**: "+ filePath);
						//El codigo del articulo ser\u00E1 el nombre de la imagen, para que pueda ser llamada desde la jsp
						String nombreImagen= articuloDTO.getDescripcionArticulo();

						InputStream inputStream = new ByteArrayInputStream(bfile);
						//carga de path, nombre de la imagen y su extensi\u00F3n
						OutputStream outputStream = new FileOutputStream(filePath.concat(nombreImagen).concat(".jpeg"));
						rutasImagenes = rutasImagenes.concat(filePath.concat(nombreImagen).concat(".jpeg")).concat(",");
						
						//se realiza la copia
						ManejarArchivo.copy(inputStream, outputStream);
						LogSISPE.getLog().info("el archivo fue copiado");
					}

					//si no est\u00E1n cargados los items del canasto, se realiza la carga
					if(articuloDTO.getRecetaArticulos()==null){
						//se crea la consulta para traer los items del canasto
						ArticuloRelacionDTO consultaRecetaArticuloDTO = new ArticuloRelacionDTO();
						consultaRecetaArticuloDTO.getId().setCodigoCompania(codigoCompania);
						consultaRecetaArticuloDTO.getId().setCodigoArticulo(articuloDTO.getId().getCodigoArticulo());
						consultaRecetaArticuloDTO.setEstado(estadoActivo);
						consultaRecetaArticuloDTO.setArticulo(new ArticuloDTO());
						consultaRecetaArticuloDTO.getArticulo().setNpCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));
						//Obtener datos de la colecci\u00F3n de recetas
						Collection recetaArticulo = SessionManagerSISPE.getServicioClienteServicio().transObtenerRecetaArticulo(consultaRecetaArticuloDTO, false);
						articuloDTO.setRecetaArticulos(recetaArticulo);
					}
					//se a\u00F1ade el art\u00EDculo a la lista de seleccionados
					articulos.add(articuloDTO);					
				}
				if(StringUtils.isNotEmpty(rutasImagenes)){
					//se elimina el \u00FAltimo separador
					rutasImagenes = rutasImagenes.substring(0, rutasImagenes.length()-1);
					request.setAttribute("imagenesCatalogoArticulos", rutasImagenes);
				}
				else{
					request.setAttribute("imagenesCatalogoArticulos", "");
				}
				
				
				LogSISPE.getLog().info("art seleccionados: " + articulos.size());

				//se sube a sesi\u00F3n la colecci\u00F3n de art\u00EDculos seleccionados
				session.setAttribute(COL_ARTICULOS_SELECCIONADOS, articulos);
			}

			EventoID eventoID=new EventoID();
			eventoID.setCodigoEvento(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.envioCatalogo"));
			eventoID.setSystemId(MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID"));
			eventoID.setCompanyId(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			EventoDTO eventoDTO = SessionManagerSISPE.getMensajeria().transObtenerEventoPorID(eventoID);

			MailMensajeEST mailMensajeEST=new MailMensajeEST();
			mailMensajeEST.setDe(MessagesWebSISPE.getString("mail.cuenta.sispe"));
			LogSISPE.getLog().info("dir******* " + request.getParameter("emailContacto"));

			mailMensajeEST.setAsunto(eventoDTO.getAsuntoEvento());
			//MailMensajeEST.setMensaje(descripcionEvento);
			mailMensajeEST.setEventoDTO(eventoDTO);
			mailMensajeEST.setPara((request.getParameter("emailContacto")).split(","));
			//estos datos son tomados del archivo properties de la aplicaci\u00F3n de mensajer\u00EDa
			mailMensajeEST.setHost(MensajeriaMessages.getString("mail.serverHost"));
			mailMensajeEST.setPuerto(MensajeriaMessages.getString("mail.puerto"));
			mailMensajeEST.setFormatoHTML(true);
			session.setAttribute("ec.com.smx.sic.sispe.envioMail", mailMensajeEST);//variable de sesion donde se cargan los datos del mail para enviarlo desde la jsp
			LogSISPE.getLog().info("***MAIL ENVIADO***");
			//session.removeAttribute("ec.com.smx.sic.sispe.envioMailVentana");
			exitos.add("envioMail",new ActionMessage("message.mail.send.exito"));

			String contactos = "";
			//se guardan los contactos como un string separado por comas
			for(int i=0; i<mailMensajeEST.getPara().length;i++){
				contactos = contactos.concat(mailMensajeEST.getPara()[i]).concat(",");
			}
			//se elimina el \u00FAltimo separador
			contactos = contactos.substring(0, contactos.length() - 1);
			request.setAttribute("contactosEnvioMail", contactos);
			
			//Variable para controlar que no se dibuje nuevamente el frame que bloquea la pantalla
			request.setAttribute("ec.com.smx.sic.sispe.ventanaActiva", "ok");
		}
		else if(request.getParameter("cerrarVentanaMail")!=null)
		{
			session.removeAttribute("ec.com.smx.sic.sispe.envioMailVentana");
		}
		//---------------- acci\u00F3n por defecto -------------------
		else
		{
			//--- eliminar las variables de sesi\u00F3n ---
			SessionManagerSISPE.removeVarSession(request);

			String codigoClasificacionCanCatalogo = "";
			//se obtiene el par\u00E1metro que contiene la clasificaci\u00F3n de canastos de cat\u00E1logo
			ParametroDTO consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoClasificacionCanastasCatalogo", request);
			if(consultaParametroDTO.getValorParametro()!=null){
				codigoClasificacionCanCatalogo = consultaParametroDTO.getValorParametro();
			}

			String codigoCanastoVacio = SessionManagerSISPE.getCodigoCanastoVacio(request);
//			//se obtiene el par\u00E1metro que contiene el c\u00F3digo del canasto vac\u00EDo
//			consultaParametroDTO = WebSISPEUtil.obtenerParametroAplicacion("ec.com.smx.sic.sispe.parametro.codigoCanastoCotizacionesVacio", request);
//			if(consultaParametroDTO.getValorParametro()!=null){
//				codigoCanastoVacio = consultaParametroDTO.getValorParametro();
//			}

			//----------- se cargan los art\u00EDculos en la clasificaci\u00F3n de canastos de cat\u00E1logo 
			//creaci\u00F3n del DTO para el art\u00EDculo
			ArticuloDatoImagenDTO articuloImagenDTO= new ArticuloDatoImagenDTO();
			articuloImagenDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			articuloImagenDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
			articuloImagenDTO.setCodigoClasificacion(codigoClasificacionCanCatalogo);
			articuloImagenDTO.setEstadoArticulo(estadoActivo);
			Collection<ArticuloDatoImagenDTO> colArticuloImagenDTO = SessionManagerSISPE.getServicioClienteServicio().transObtenerArticulosDatoImagen(articuloImagenDTO);
			//se guarda la colecci\u00F3n en sesi\u00F3n
			session.setAttribute(COL_ARTICULOS, colArticuloImagenDTO);
			//se sube a a sesi\u00F3n el c\u00F3digo del canasto vacio
			session.setAttribute(CODIGO_CANASTO_VACIO, codigoCanastoVacio);
		}

		saveMessages(request, exitos);

		return mapping.findForward(forward);

	}
}