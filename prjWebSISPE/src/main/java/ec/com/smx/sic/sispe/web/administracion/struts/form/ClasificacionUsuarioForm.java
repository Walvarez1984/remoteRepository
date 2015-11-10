/*
 * ClasificacionUsuarioForm.java
 * Creado el 19/09/2007
 * 
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.cliente.mdl.dto.ClasificacionUsuarioDTO;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Permite controlar las validaciones sobre las pantallas que permiten la creaci\u00F3n o actualizaci\u00F3n de las 
 * configuraciones de acceso de los usuarios a las clasificaciones.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since		JSDK 1.5	 	
 */

@SuppressWarnings({"serial","unchecked"})
public class ClasificacionUsuarioForm extends AdministracionListadoForm
{
	//campos del formulario
	private String idUsuario;
	private String nombreUsuario;
	private String nombreCompletoUsuario;
	private String clasificacion;
	private String [] checksSeleccion;
	
	//botones principales
	private String nuevaClasificacionUsuario;
	private String guardarClasificacionUsuario;
	
  /**
   * Realiza la validaci\u00F3n en las p\u00E1ginas <code>nuevaClasificacionUsuario.jsp 
   * y actulizarClasificacionUsuario.jsp</code>
   * 
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		El request que estamos procesando
   * @return errors		Los errores recogidos durante la ejecuci\u00F3n
   */
  public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
  {
  	ActionErrors errors = new ActionErrors();
    PropertyValidator validar = new PropertyValidatorImpl();
    HttpSession session = request.getSession();
    
  	//se verifica el valor del par\u00E1metro
  	String peticion = request.getParameter("ayuda");
  	if(peticion!=null && !peticion.equals("")){
  		if(peticion.equals("guardarA") || peticion.equals("guardarN"))
  			this.guardarClasificacionUsuario = peticion;
  	}
  	try{
	  	//------------ si se desea guardar la configuraci\u00F3n de la clasificaci\u00F3n -------------
	  	if(this.guardarClasificacionUsuario != null){
	  		//se realiza la validaci\u00F3n cuando se guarda una nueva configuraci\u00F3n
	      //validaci\u00F3n del campo del is del usuario
	      validar.validateMandatory(errors,"idUsuario",this.idUsuario,"errors.requerido","Usuario");
	      if(errors.isEmpty()){
		      //se obtiene de sesi\u00F3n la colecci\u00F3n de clasificaciones por usuario
		      Collection <ClasificacionUsuarioDTO> clasificacionesPorUsuario = (Collection)session.getAttribute("ec.com.smx.sic.sispe.clasificacionesPorUsuario");
		      if(clasificacionesPorUsuario == null || clasificacionesPorUsuario.isEmpty()){
		      	LogSISPE.getLog().info("coleccion clasificaciones vac\u00EDa");
		      	errors.add("sinClasificaciones",new ActionMessage("errors.sinItems", "la configuraci\u00F3n de permisos	", "una Clasificaci\u00F3n"));
		      }else{
		      	LogSISPE.getLog().info("coleccion clasificaciones no vac\u00EDa");
		      	boolean hayClasificaciones = false;
		      	//se verifica si todas las clasificaciones est\u00E1n desactivadas
		      	for(ClasificacionUsuarioDTO clasificacionUsuarioDTO : clasificacionesPorUsuario){
							if(clasificacionUsuarioDTO.getEstadoClasificacionUsuario()!=null){
								hayClasificaciones = true;
								break;
							}
						}
			      //si no hay clasificaciones se muestra el mensaje
			      if(!hayClasificaciones){
			      	errors.add("sinClasificaciones",new ActionMessage("errors.sinItems", "la configuraci\u00F3n de permisos	", "una Clasificaci\u00F3n"));
			      }
		      }
		      
		      if(this.guardarClasificacionUsuario.equals("guardarN") && errors.isEmpty()){
		      	LogSISPE.getLog().info("se debe validar el c\u00F3digo de clasificaci\u00F3n");
			      //se toma la colecci\u00F3n del resumen de clasificaciones para verificar que el nuevo usuario no
			      //est\u00E9 en el resumen
			      Collection <ClasificacionUsuarioDTO> resumenUsuarios = (Collection)session.getAttribute("ec.com.smx.sic.sispe.colResumenUsuarioClasificacionDTO");
			      if(resumenUsuarios!=null){
			      	for(ClasificacionUsuarioDTO clasificacionUsuarioDTO : resumenUsuarios){
			      		if(clasificacionUsuarioDTO.getId().getUserId().equals(this.idUsuario)){
			      			errors.add("idUsuario",new ActionMessage("errors.UsuarioExistente"));
			      			break;
			      		}
			      	}
			      }
		      }
	      }
	  	}
	  	//------------ si se desea agregar una clasificaci\u00F3n -------------------
	  	if(request.getParameter("agregarClasificacion") != null){
	      //valida el campo usuario sea requerido
	      validar.validateMandatory(errors,"clasificacion",this.clasificacion,"errors.requerido","Clasificaci\u00F3n");
	  	}
	  	//------------ si se desea eliminar o activar/desactivar una clasificaci\u00F3n -------------------
	  	if(request.getParameter("eliminarClasificacion")!= null 
	  			|| request.getParameter("activarDesactivarClasificacion")!= null){
	  		//se valida que existan detalles selecciones
	  		if(this.checksSeleccion == null)
	  			errors.add("noSeleccionados", new ActionMessage("errors.seleccion.requerido","una Clasificaci\u00F3n"));
	  	}
  	}catch(Exception ex){
  		LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
  	}
  	
  	return errors;
  }
  
  /**
   * Resetea los controles del formulario en las p\u00E1ginas <code>nuevaClasificacionUsuario.jsp y 
   * actulizarClasificacionUsuario.jsp</code>.
   * 
   * @param mapping 	El mapeo utilizado para seleccionar esta instancia
   * @param request 	La petici&oacue; que estamos procesando
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
  	this.idUsuario = null;
  	this.nombreUsuario = null;
  	this.nombreCompletoUsuario = null;
  	this.clasificacion = null;
  	this.checksSeleccion = null;
  	
  	this.nuevaClasificacionUsuario = null;
  	this.guardarClasificacionUsuario = null;
  }

	/**
	 * @return el idUsuario
	 */
	public String getIdUsuario() {
		return idUsuario;
	}

	/**
	 * @param idUsuario el idUsuario a establecer
	 */
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * @return el nombreCompletoUsuario
	 */
	public String getNombreCompletoUsuario() {
		return nombreCompletoUsuario;
	}

	/**
	 * @param nombreCompletoUsuario el nombreCompletoUsuario a establecer
	 */
	public void setNombreCompletoUsuario(String nombreCompletoUsuario) {
		this.nombreCompletoUsuario = nombreCompletoUsuario;
	}

	/**
	 * @return el nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * @param nombreUsuario el nombreUsuario a establecer
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * @return el clasificacion
	 */
	public String getClasificacion() {
		return clasificacion;
	}

	/**
	 * @param clasificacion el clasificacion a establecer
	 */
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	/**
	 * @return el guardarClasificacionUsuario
	 */
	public String getGuardarClasificacionUsuario() {
		return guardarClasificacionUsuario;
	}

	/**
	 * @param guardarClasificacionUsuario el guardarClasificacionUsuario a establecer
	 */
	public void setGuardarClasificacionUsuario(String guardarClasificacionUsuario) {
		this.guardarClasificacionUsuario = guardarClasificacionUsuario;
	}

	/**
	 * @return el nuevaClasificacionUsuario
	 */
	public String getNuevaClasificacionUsuario() {
		return nuevaClasificacionUsuario;
	}

	/**
	 * @param nuevaClasificacionUsuario el nuevaClasificacionUsuario a establecer
	 */
	public void setNuevaClasificacionUsuario(String nuevaClasificacionUsuario) {
		this.nuevaClasificacionUsuario = nuevaClasificacionUsuario;
	}

	/**
	 * @return el checksSeleccion
	 */
	public String[] getChecksSeleccion() {
		return checksSeleccion;
	}

	/**
	 * @param checksSeleccion el checksSeleccion a establecer
	 */
	public void setChecksSeleccion(String[] checksSeleccion) {
		this.checksSeleccion = checksSeleccion;
	}
}