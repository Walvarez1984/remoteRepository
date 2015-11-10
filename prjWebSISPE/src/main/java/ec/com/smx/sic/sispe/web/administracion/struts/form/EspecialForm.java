/*
 * EspcialForm.java
 * Creado el 19/03/2007
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Esta clase permite el gestionar los datos de los especiales dentro de la aplicaci\u00F3n.
 * </p>
 * @author 	jacalderon
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class EspecialForm extends ActionForm{

	/**
	 * Campos del formulario de Tipo de especiales
	 * <ul>
	 * <li><code>String codigoClasificacionEspecial:</code> C&oacute;digo de la clasificaci&oacute;n a a\u00F1adir 
	 * <li><code>String estadoEspecial:</code> Registra el estado del especial
	 * <li><code>String descripcionEspecial:</code> Registra la descripci\u00F3n del especial
	 * </ul>
	 */
	private String codigoClasificacionEspecial;
	private String estadoEspecial;
	private String opPublicar;
	private String descripcionEspecial;
	private String tipoPedido;
	
	/**
	 * Botones utlizados para guardar los cambios ingresados
	 * <ul>
	 * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
	 * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n.</li>
	 * <li><code>String botonActualizar:</code> Actualiza</li>
	 * <ul>
	 */
	private String botonRegistrarNuevo;
	private String botonAgregarClasificacion;
	private String botonCancelar;
	private String botonActualizar;

	/**
	 * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevoTipoDescuento.jsp</code> y 
	 * <code>actualizarTipoDescuento.jsp</code>.
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		El request que estamos procesando
	 * @return errors		Los errores recogidos durante la ejecuci\u00F3n
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errors = new ActionErrors();
		PropertyValidator validar = new PropertyValidatorImpl();
		HttpSession session = request.getSession();

		String ayuda = request.getParameter(Globals.AYUDA);
		if(ayuda != null && !ayuda.equals("")) {
			if(ayuda.equals("actualizarEspecial")) {
				this.botonActualizar = ayuda;
			}else if (ayuda.equals("registrarEspecial")) {
				this.botonRegistrarNuevo = ayuda;
			}
		}

		try
		{
			//cuando se va a guardar un especial ------------------------------
			if (this.botonRegistrarNuevo!=null || this.botonActualizar != null){
				validar.validateMandatory(errors, "estadoEspecial", this.estadoEspecial, "errors.requerido",
					"Estado");
				/*if(!errors.isEmpty()){
					this.estadoEspecial="";
				}*/
				validar.validateMandatory(errors, "descripcionEspecial", this.descripcionEspecial,
						"errors.requerido", "Descripci\u00F3n");
				validar.validateMandatory(errors, "tipoPedido", this.tipoPedido,
						"errors.requerido", "Tipo de Pedido");
				
			}
			/*----------------------- cuando se da clic en el bot\u00F3n agregar clasificacion ----------------------*/
			else if(this.botonAgregarClasificacion!=null){
				validar.validateMandatory(errors,"codigoClasificacion",this.codigoClasificacionEspecial,"errors.requerido","C\u00F3digo de Clasificaci\u00F3n");
			}
			//cuando se ha escogido un registro --------------------------------------
			else if (request.getParameter("indice") != null){
				//se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
				ArrayList tipoDescuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
				//obtiene el indice del registro escogido
				int indice = Integer.parseInt(request.getParameter("indice"));
				if (indice >= tipoDescuentos.size())
					//el indice del registro referenciado no esta en la colecci\u00F3n
					errors.add("indice", new ActionMessage("errors.registro.fueraDeRango"));
			}
		}
		catch(Exception ex){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
		}
		return errors;
	}

	/**
	 * Resetea los controles del formulario de las p\u00E1ginas
	 * <code>nuevoEspecial.jsp</code> y <code>actualizarEspecial.jsp</code>, en cada petici\u00F3n.
	 * @param mapping 	El mapeo utilizado para seleccionar esta instancia
	 * @param request 	La petici&oacue; que estamos procesando
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) 
	{
		this.codigoClasificacionEspecial=null;
		this.botonRegistrarNuevo = null;
		this.botonCancelar = null;
		this.botonActualizar = null;
		this.descripcionEspecial = null;
		this.estadoEspecial = null;
		this.opPublicar = null;
		this.botonAgregarClasificacion=null;
		this.tipoPedido = null;
	}

	/**
	 * @return Devuelve botonActualizar.
	 */
	public String getBotonActualizar() {
		return botonActualizar;
	}
	/**
	 * @param botonActualizar El botonActualizar a establecer.
	 */
	public void setBotonActualizar(String botonActualizar) {
		this.botonActualizar = botonActualizar;
	}
	/**
	 * @return Devuelve botonAgregarClasificacion.
	 */
	public String getBotonAgregarClasificacion() {
		return botonAgregarClasificacion;
	}
	/**
	 * @param botonAgregarClasificacion El botonAgregarClasificacion a establecer.
	 */
	public void setBotonAgregarClasificacion(String botonAgregarClasificacion) {
		this.botonAgregarClasificacion = botonAgregarClasificacion;
	}
	/**
	 * @return Devuelve botonCancelar.
	 */
	public String getBotonCancelar() {
		return botonCancelar;
	}
	/**
	 * @param botonCancelar El botonCancelar a establecer.
	 */
	public void setBotonCancelar(String botonCancelar) {
		this.botonCancelar = botonCancelar;
	}
	/**
	 * @return Devuelve botonRegistrarNuevo.
	 */
	public String getBotonRegistrarNuevo() {
		return botonRegistrarNuevo;
	}
	/**
	 * @param botonRegistrarNuevo El botonRegistrarNuevo a establecer.
	 */
	public void setBotonRegistrarNuevo(String botonRegistrarNuevo) {
		this.botonRegistrarNuevo = botonRegistrarNuevo;
	}
	/**
	 * @return Devuelve codigoClasificacionEspecial.
	 */
	public String getCodigoClasificacionEspecial() {
		return codigoClasificacionEspecial;
	}
	/**
	 * @param codigoClasificacionEspecial El codigoClasificacionEspecial a establecer.
	 */
	public void setCodigoClasificacionEspecial(
			String codigoClasificacionEspecial) {
		this.codigoClasificacionEspecial = codigoClasificacionEspecial;
	}
	/**
	 * @return Devuelve descripcionEspecial.
	 */
	public String getDescripcionEspecial() {
		return descripcionEspecial;
	}
	/**
	 * @param descripcionEspecial El descripcionEspecial a establecer.
	 */
	public void setDescripcionEspecial(String descripcionEspecial) {
		this.descripcionEspecial = descripcionEspecial;
	}
	/**
	 * @return Devuelve estadoEspecial.
	 */
	public String getEstadoEspecial() {
		return estadoEspecial;
	}
	/**
	 * @param estadoEspecial El estadoEspecial a establecer.
	 */
	public void setEstadoEspecial(String estadoEspecial) {
		this.estadoEspecial = estadoEspecial;
	}

	/**
	 * @return el opPublicar
	 */
	public String getOpPublicar() {
		return opPublicar;
	}

	/**
	 * @param opPublicar el opPublicar a establecer
	 */
	public void setOpPublicar(String opPublicar) {
		this.opPublicar = opPublicar;
	}

	/**
	 * @return el tipoPedido
	 */
	public String getTipoPedido() {
		return tipoPedido;
	}

	/**
	 * @param tipoPedido el tipoPedido a establecer
	 */
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}
	
}
