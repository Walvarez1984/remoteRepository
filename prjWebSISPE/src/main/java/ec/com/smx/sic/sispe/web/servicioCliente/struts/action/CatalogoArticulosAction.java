/*
 * Clase CatalogoArticulosAction.java
 * Creado el 20/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.sic.cliente.mdl.dto.ArticuloDTO;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionDTO;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.BuscarArticuloForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Esta clase muestra un cat\u00E1logo de los art\u00EDculos agrupados por distintos niveles de clasificaci\u00F3n. El primer
 * nivel es DIVISIONES, est\u00E1s pueden contener uno o varios DEPARTAMENTOS y finalmente una o varias CLASIFICACIONES.</br>
 * Las CLASIFICACIONES contienen art\u00EDculos los cuales son desplegados cuando una es escogida.
 * </p>
 * @author 	fmunoz
 * @version	2.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("unchecked")
public class CatalogoArticulosAction extends BaseAction
{
	//se obtienen las descripciones de los diferentes niveles
	private static final String TITULO_DIVISION = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDivision");
	private static final String TITULO_DEPARTAMENTO = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDepartamento");
	private static final String TITULO_CLASIFICACION = MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloClasificacion");
	
	public static final String ARTICULOS_AGREGADOS_BUSQUEDA = "ec.com.smx.sic.sispe.catalogoArticulos.articulos";
	public static final String DIVISION_CATALOGO = "ec.com.smx.sic.sispe.catalogoArticulos.division";
	public static final String CATALOGO_ACTUAL = "ec.com.smx.sic.sispe.catalogoArticulos.catalogo";
	public static final String CATALOGO_ANTERIOR = "ec.com.smx.sic.sispe.catalogoArticulos.catalogoAnterior";
	
	private static final String COL_DIVISIONES = "ec.com.smx.sic.sispe.catalogoArticulos.divisiones";
	private static final String NIVEL_CATALOGO = "ec.com.smx.sic.sispe.catalogoArticulos.nivel";
	private static final String RUTA_ESTRUCTURA_COMERCIAL = "ec.com.smx.sic.sispe.catalogoArticulos.estructura";
	private static final String RUTA_EST_COMERCIAL_ANTERIOR = "ec.com.smx.sic.sispe.catalogoArticulos.estructuraAnterior";
	
	public static final String CLASIFICACIONES_AGREGADAS = "ec.com.smx.sic.sispe.clasificacionesAgregadas";
	
	static final String TAMANOCONSULTAARTICULOS = "ec.com.smx.sic.sispe.tamanoConsultaArticulos";//Sesion que sirve para guardar el tama\u00F1o de la coleccion resustante de la consulta de articulos
	static final String ARTICULOCONSULTA = "ec.com.smx.sic.sispe.articuloConsulta";//Sesion que sirve para guardar el articulo con los parametros de busqueda

	
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control(si la respuesta se ha completado <code>null</code>)
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

		ActionMessages success = new ActionMessages();
		ActionMessages infos = new ActionMessages();
		ActionMessages errors = new ActionMessages();
		
		BuscarArticuloForm formulario= (BuscarArticuloForm)form;

		HttpSession session= request.getSession();
		//variable que almacena 
		String salida="catalogo";

		String estadoInactivo = SessionManagerSISPE.getEstadoInactivo(request);
		
		try
		{
			/*------------------------ cuando se selecciona una descripcion del cat\u00E1logo --------------------------------*/
			if(request.getParameter("numeroRegistro")!=null)
			{
				int numeroRegistro=0;
				Collection buscados = new ArrayList();
				Collection articulos = new ArrayList();

				try
				{
					numeroRegistro = Integer.parseInt(request.getParameter("numeroRegistro"));
					String nivelActual = request.getParameter("descripcionNivel");

					/*------------------------------- cuando estamos en el nivel DIVISION ------------------------------------*/
					if(nivelActual.equals(TITULO_DIVISION))
					{
						LogSISPE.getLog().info("OBTENER DEPARTAMENTOS");
						//se obtiene la lista que almacena las divisiones
						//ArrayList division = (ArrayList)session.getAttribute(COL_DIVISIONES);
						ArrayList divisiones = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
						ClasificacionDTO divisionDTO  = (ClasificacionDTO)divisiones.get(numeroRegistro);
						//session.setAttribute(BuscarArticuloAction.DIVISION_CATALOGO, divisionDTO);

						session.setAttribute(NIVEL_CATALOGO,TITULO_DEPARTAMENTO);
						session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,request.getParameter("clasificacion")+" "
								+divisionDTO.getId().getCodigoClasificacion()+"-"+divisionDTO.getDescripcionClasificacion()+"/");

						//proceso para obtener la clasificaci\u00F3n de departamentos que se almacena en la colecci\u00F3n buscados
						ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
						clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
						clasificacionConsultaDTO.setCodigoClasificacionPadre(divisionDTO.getId().getCodigoClasificacion());
						clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.departamento"));
						clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
								.getString("ec.com.smx.sic.sispe.estado.activo"));
						clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
						
						buscados = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);
						if(buscados==null || buscados.isEmpty()){
							//se muestra un mensaje indicando que la lista est\u00E1 vacia
							infos.add("listaVacia",new ActionMessage("message.listaVacia","Departamentos dentro de la divisi\u00F3n "+divisionDTO.getDescripcionClasificacion()));
						}
						//variable de sesion que almacena la clasificaci\u00F3n obtenida
						session.setAttribute(CATALOGO_ACTUAL,buscados);
						//session.setAttribute("ec.com.smx.sic.sispe.catalogoArticulos.departamentos",buscados);

						salida="catalogo";
					}
					/*------------------------------ cuando estamos en el nivel DEPARTAMENTO ----------------------------------*/
					else if(nivelActual.equals(TITULO_DEPARTAMENTO))
					{
						LogSISPE.getLog().info("OBTENER CLASIFICACIONES");
						//lista que almacena los departamentos de la ultima division escogida. 
						ArrayList departamentos = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
						session.setAttribute(CATALOGO_ANTERIOR,departamentos);

						//se obtiene el departamento que selecciono el usuario y se la almacena en una variable de sesi\u00F3n
						ClasificacionDTO departamentoDTO = (ClasificacionDTO)departamentos.get(numeroRegistro);

						session.setAttribute(NIVEL_CATALOGO, TITULO_CLASIFICACION);
						session.setAttribute(RUTA_EST_COMERCIAL_ANTERIOR,request.getParameter("clasificacion"));
						session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,request.getParameter("clasificacion")+" "
								+departamentoDTO.getId().getCodigoClasificacion()+"-"+departamentoDTO.getDescripcionClasificacion()+"/");

						//proceso para sacar la lista de clases que se almacenar\u00E1 en la colecci\u00F3n buscados
						ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
						clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
						clasificacionConsultaDTO.setCodigoClasificacionPadre(departamentoDTO.getId().getCodigoClasificacion());
						clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.clase"));
						clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
								.getString("ec.com.smx.sic.sispe.estado.activo"));
						clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
						
						buscados = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);

						if(buscados==null || buscados.isEmpty()){
							//se muestra un mensaje indicando que la lista est\u00E1 vacia
							infos.add("listaVacia",new ActionMessage("message.listaVacia","Clases dentro del Departamento "+departamentoDTO.getDescripcionClasificacion()));
						}

						//variable de sesion que almacena la clasificaci\u00F3n obtenida
						session.setAttribute(CATALOGO_ACTUAL,buscados);
						salida="catalogo";

					}
					/*-------------------- cuando estamos en el nivel CLASIFICACION --------------------*/
					else if(nivelActual.equals(TITULO_CLASIFICACION))
					{
						LogSISPE.getLog().info("OBTENER ARTICULOS");

						ArrayList clasificaciones = (ArrayList)session.getAttribute(CATALOGO_ACTUAL);
						//session.setAttribute(CATALOGO_ANTERIOR,clasificaciones);
						ClasificacionDTO clasificacionDTO = (ClasificacionDTO)clasificaciones.get(numeroRegistro);
						int start= 0;
						int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
						//objeto DTO que servir\u00E1 para obtener los articulos de la clase escogida
						ArticuloDTO articuloDTO = new ArticuloDTO();
						articuloDTO.setCodigoClasificacion(clasificacionDTO.getId().getCodigoClasificacion());
						try{
							articulos = BuscarArticuloAction.construirConsultaArticulos(request, articuloDTO, estadoInactivo, start, range);
							formulario.setDatos(null);
							if(articulos==null || articulos.isEmpty()){
								//se muestra un mensaje indicando que la lista est\u00E1 vacia
								infos.add("listaVacia",new ActionMessage("message.listaVacia","Art\u00EDculos dentro de la Clase "+clasificacionDTO.getDescripcionClasificacion()));
							}else{
								LogSISPE.getLog().info("ENTRO A LA PAGINACION");
								int size= ((Integer)session.getAttribute(TAMANOCONSULTAARTICULOS)).intValue();
								formulario.setStart(String.valueOf(start));
								formulario.setRange(String.valueOf(range));
								formulario.setSize(String.valueOf(size));
								formulario.setDatos(articulos);
								//Guardo el articulo con los parametros de la consulta para la busqueda en base
								session.setAttribute(ARTICULOCONSULTA, articuloDTO);
							}
						}catch(SISPEException ex){
							LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
							errors.add("Articulos",new ActionMessage("errors.llamadaServicio.obtenerDatos","Art\u00EDculos"));
						}
						//variable de sesi\u00F3n que almacenar\u00E1 la lista de articulos. 
						session.setAttribute(ARTICULOS_AGREGADOS_BUSQUEDA, articulos);
						session.setAttribute(BuscarArticuloAction.BUSQUEDA_POR_CATALOGO,"ok");
						salida="listaArticulos";
					}
				}catch(NumberFormatException e)
				{
					errors.add("formatoIndice",new ActionMessage("errors.indiceDetalle.formato"));
				}
			}
			//------------ cuando se hace clic en el bot\u00F3n Atras -----------
			else if(request.getParameter("atras")!=null){
				if(request.getParameter("nivel")!=null){
					Collection listadoAnterior = (Collection)session.getAttribute(CATALOGO_ANTERIOR);
					session.setAttribute(CATALOGO_ACTUAL,listadoAnterior);
					if(request.getParameter("nivel").equals(TITULO_DEPARTAMENTO)){
						session.setAttribute(NIVEL_CATALOGO, TITULO_DIVISION);
						session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,"Estructura comercial: ");
					}
					if(request.getParameter("nivel").equals(TITULO_CLASIFICACION)){
						session.setAttribute(NIVEL_CATALOGO, TITULO_DEPARTAMENTO);
						String estructuraAnterior = (String)session.getAttribute(RUTA_EST_COMERCIAL_ANTERIOR);
						session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,estructuraAnterior);
						Collection divisiones = (Collection)session.getAttribute(COL_DIVISIONES);
						session.setAttribute(CATALOGO_ANTERIOR,divisiones);
					}
				}
			}
			//cuando se agregan las clasificaciones desde la pantalla de permisos
			else if(request.getParameter("agregarClasificacion")!=null){
				if(formulario.getChecksClasificaciones()!=null){
					ArrayList <ClasificacionDTO> clasificaciones = (ArrayList <ClasificacionDTO>)session.getAttribute(CATALOGO_ACTUAL);
					Collection <ClasificacionDTO> clasificacionesAgregadas = (Collection <ClasificacionDTO>)session.getAttribute(CLASIFICACIONES_AGREGADAS);
					if(clasificacionesAgregadas == null)
						clasificacionesAgregadas = new ArrayList<ClasificacionDTO>();
					
					int indice = 0; //indice de las clasificaciones
					for(int i=0; i<formulario.getChecksClasificaciones().length;i++){
						indice = Integer.parseInt(formulario.getChecksClasificaciones()[i]); 
						ClasificacionDTO clasificacionDTO = clasificaciones.get(indice);
						clasificacionesAgregadas.add(clasificacionDTO);
					}
					session.setAttribute(CLASIFICACIONES_AGREGADAS, clasificacionesAgregadas);
					//se genera un mensaje de exito
					success.add("clasificacionesAgregadas", new ActionMessage("message.exito.clasificacionesAgregadas"));
					formulario.setChecksClasificaciones(null);
				}else
					errors.add("noSeleccionados", new ActionMessage("errors.seleccion.requerido","una Clasificaci\u00F3n"));
			}
			
			//------------- acci\u00F3n por omisi\u00F3n -------------
			else
			{
				LogSISPE.getLog().info("POR OMISION");
				//creaci\u00F3n de la coleccion para el manejo de las divisiones.
				Collection division = new ArrayList();
				ClasificacionDTO clasificacionConsultaDTO = new ClasificacionDTO(Boolean.TRUE);
				try
				{
					//Obtener datos de la colecci\u00F3n de clasificacion
					//buscar los datos del primer nivel
					clasificacionConsultaDTO.getId().setCodigoCompania(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getCodigoCompania());
					clasificacionConsultaDTO.setCodigoTipoClasificacion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.tipoClasificacion.division"));
					clasificacionConsultaDTO.setEstadoClasificacion(MessagesAplicacionSISPE
							.getString("ec.com.smx.sic.sispe.estado.activo"));
					clasificacionConsultaDTO.setNpOrderBy("id.codigoClasificacion");
					
					division = SessionManagerSISPE.getServicioClienteServicio().transObtenerClasificacion(clasificacionConsultaDTO);
					if(division==null || division.isEmpty()){
						//se muestra un mensaje indicando que la lista est\u00E1 vacia
						infos.add("listaVacia",new ActionMessage("message.listaVacia","Divisiones de art\u00EDculos"));
					}
					session.setAttribute(COL_DIVISIONES,division);
					session.setAttribute(CATALOGO_ANTERIOR,division);
					session.setAttribute(CATALOGO_ACTUAL,division);
					session.setAttribute(NIVEL_CATALOGO,
							MessagesWebSISPE.getString("ec.com.smx.sic.sispe.catalogo.tituloDivision"));
					session.setAttribute(RUTA_ESTRUCTURA_COMERCIAL,"Estructura comercial: ");
				}
				catch(SISPEException ex)
				{
					//se muestran los mensajes de error correspondientes
					errors.add("errorObtener",new ActionMessage("errors.llamadaServicio.obtenerDatos","Clasificaci\u00F3n de art\u00EDculos"));
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
				}   
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		//se guardan los mensajes generados
		saveMessages(request, success);
		saveInfos(request, infos);
		saveErrors(request, errors);
		
		return mapping.findForward(salida);
	}
}
