package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.EstadoDetallePedidoDTO;


/**
 * 
 * @author mbraganza
 *
 */
@SuppressWarnings("serial")
public class RegistrarOrdenCompraForm extends ListadoPedidosForm {
	
	private String botonSi;
	private String botonGuardarOrdenesCompra;
	private String[] numerosAutorizaciones;
	private String[] observacionesNumerosAutorizaciones;
	private String[] checkSeleccion;
	private String todoPedidos;
	private String observacionPedido;
	private String numeroOrdenPedido;
	private String[] numerosOrdenesArticulo;
	private String[] observacionOrdenesArticulo;
	private String numeroOrdenArticulo;
	private String observacionOrdenArticulo;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.botonSi = null;
		this.botonGuardarOrdenesCompra = null;
		this.numerosAutorizaciones = null;
		this.observacionesNumerosAutorizaciones = null;
		this.checkSeleccion=null;
	}
	
	/**
	 * Ejecuta la validacion en la p\u00E1gina JSP <code>cotizarReservar.jsp</code>
	 * @param mapping		El mapeo utilizado para seleccionar esta instancia
	 * @param request		La petici\u00F3n relizada desde el browser.
	 * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	 * <code>null</code>
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errors = null;
		String ayuda = request.getParameter(Globals.AYUDA);
		if (ayuda != null && ayuda.equals("") == false){
			if (ayuda.equals("siVolverBusqueda")) {
				this.botonSi = ayuda;
			} else if (ayuda.equals("registrarOrdenesCompra")) {
				this.botonGuardarOrdenesCompra = ayuda;
			}
		}
		
		return errors;
	}
	
	/**
	 * 
	 * @param errors
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public int validarNumOrden(ActionMessages errors,HttpServletRequest request) throws Exception
	{
		ActionErrors errorsTemp = null;
		PropertyValidator validar = null;
		
		ArrayList estadosDetallesPedido = null;
		boolean[] autorizacionesIncorrectas = null;
		 		
		
		if (this.botonGuardarOrdenesCompra != null){
			validar = new PropertyValidatorImpl();
			autorizacionesIncorrectas = new boolean[this.numerosAutorizaciones.length];
			
			LogSISPE.getLog().info("Valores arreglo boolean");
			for (boolean a : autorizacionesIncorrectas){
				LogSISPE.getLog().info("a1: {}", a);
			}
			
			for (int i = 0; i < this.numerosAutorizaciones.length; i++){
				if (this.numerosAutorizaciones[i].equals("") && this.observacionesNumerosAutorizaciones[i].equals("") == false){
					errors.add("numerosAutorizaciones", new ActionMessage("errors.numeroAutorizacionOrdenCompraObligatorio"));
					autorizacionesIncorrectas[i] = true;
				} else if (this.numerosAutorizaciones[i].equals("") == false && this.observacionesNumerosAutorizaciones[i].equals("")){
					errors.add("observacionesNumerosAutorizaciones", new ActionMessage("errors.observacionAutorizacionOrdenCompraObligatorio"));
					autorizacionesIncorrectas[i] = true;
				} else {
					errorsTemp = new ActionErrors();
					validar.validateFormato(errorsTemp, "numerosAutorizaciones", this.numerosAutorizaciones[i], false, "^(\\d)*$", "errors.formato.numeroAutorizacionOrdenCompra", "errors.requerido", "N\u00FAmero de autorizaci\u00F3n de orden de compra");
					validar.validateFormato(errorsTemp, "observacionesNumerosAutorizaciones", this.observacionesNumerosAutorizaciones[i], false, "^(.){1,500}$", "errors.formato.observacionNumeroAutorizacionOrdenCompra", "errors.requerido", "Observaci&oacute;n del abono");
					if (errorsTemp.size() > 0){
						autorizacionesIncorrectas[i] = true;
						for (Iterator j = errorsTemp.get(); j.hasNext();){
							//LogSISPE.getLog().info("ClaseErrorsRegistroOrdenCompra: " + j.next().getClass());
							errors.add("observacionesNumerosAutorizaciones", (ActionMessage) j.next());
						}
					}
				}	
			}
			
			if (errors.size() > 0){
				if (request.getSession().getAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido") != null){
					estadosDetallesPedido = (ArrayList) request.getSession().getAttribute("ec.com.smx.sic.sispe.ordenCompra.detallesPedido");
					for (int i = 0; i < this.getNumerosAutorizaciones().length; i++){
						if (this.getNumerosAutorizaciones()[i].equals("") == false){
							EstadoDetallePedidoDTO estadoDetallePedidoActual = (EstadoDetallePedidoDTO) estadosDetallesPedido.get(i);
							
							estadoDetallePedidoActual.setNumeroAutorizacionOrdenCompra(this.getNumerosAutorizaciones()[i]);
							estadoDetallePedidoActual.setObservacionAutorizacionOrdenCompra(this.getObservacionesNumerosAutorizaciones()[i]);
							estadoDetallePedidoActual.setNpNumeroAutorizacionOrdenCompraInCorrecto(autorizacionesIncorrectas[i]);
						}
					}
				}
			}
		}
		
		LogSISPE.getLog().info("numero de errores:{}" , errors.size());
		return errors.size();
	}

	
	/**
	 * @return el botonSI
	 */
	public String getBotonSi() {
		return botonSi;
	}

	/**
	 * @param botonSI el botonSI a establecer
	 */
	public void setBotonSi(String botonSI) {
		this.botonSi = botonSI;
	}




	/**
	 * @return el numerosAutorizaciones
	 */
	public String[] getNumerosAutorizaciones() {
		return numerosAutorizaciones;
	}
	/**
	 * @param numerosAutorizaciones el numerosAutorizaciones a establecer
	 */
	public void setNumerosAutorizaciones(String[] numerosAutorizaciones) {
		this.numerosAutorizaciones = numerosAutorizaciones;
	}
	/**
	 * @return el observacionesNumerosAutorizaciones
	 */
	public String[] getObservacionesNumerosAutorizaciones() {
		return observacionesNumerosAutorizaciones;
	}
	/**
	 * @param observacionesNumerosAutorizaciones el observacionesNumerosAutorizaciones a establecer
	 */
	public void setObservacionesNumerosAutorizaciones(
			String[] observacionesNumerosAutorizaciones) {
		this.observacionesNumerosAutorizaciones = observacionesNumerosAutorizaciones;
	}


	/**
	 * @return el botonGuardarOrdenesCompra
	 */
	public String getBotonGuardarOrdenesCompra() {
		return botonGuardarOrdenesCompra;
	}


	/**
	 * @param botonGuardarOrdenesCompra el botonGuardarOrdenesCompra a establecer
	 */
	public void setBotonGuardarOrdenesCompra(String botonGuardarOrdenesCompra) {
		this.botonGuardarOrdenesCompra = botonGuardarOrdenesCompra;
	}




	public String getNumeroOrdenPedido() {
		return numeroOrdenPedido;
	}


	public void setNumeroOrdenPedido(String numeroOrdenPedido) {
		this.numeroOrdenPedido = numeroOrdenPedido;
	}


	public String getObservacionPedido() {
		return observacionPedido;
	}


	public void setObservacionPedido(String observacionPedido) {
		this.observacionPedido = observacionPedido;
	}


	public String getTodoPedidos() {
		return todoPedidos;
	}


	public void setTodoPedidos(String todoPedidos) {
		this.todoPedidos = todoPedidos;
	}


	public String getNumeroOrdenArticulo() {
		return numeroOrdenArticulo;
	}


	public void setNumeroOrdenArticulo(String numeroOrdenArticulo) {
		this.numeroOrdenArticulo = numeroOrdenArticulo;
	}


	public String[] getNumerosOrdenesArticulo() {
		return numerosOrdenesArticulo;
	}


	public void setNumerosOrdenesArticulo(String[] numerosOrdenesArticulo) {
		this.numerosOrdenesArticulo = numerosOrdenesArticulo;
	}


	public String getObservacionOrdenArticulo() {
		return observacionOrdenArticulo;
	}


	public void setObservacionOrdenArticulo(String observacionOrdenArticulo) {
		this.observacionOrdenArticulo = observacionOrdenArticulo;
	}


	public String[] getObservacionOrdenesArticulo() {
		return observacionOrdenesArticulo;
	}


	public void setObservacionOrdenesArticulo(String[] observacionOrdenesArticulo) {
		this.observacionOrdenesArticulo = observacionOrdenesArticulo;
	}

	/**
	 * @return el checkSeleccion
	 */
	public String[] getCheckSeleccion() {
		return checkSeleccion;
	}

	/**
	 * @param checkSeleccion el checkSeleccion a establecer
	 */
	public void setCheckSeleccion(String[] checkSeleccion) {
		this.checkSeleccion = checkSeleccion;
	}

}
