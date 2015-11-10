package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;

/**
 * 
 * @author mbraganza
 *
 */
@SuppressWarnings("serial")
public class RegistrarAbonoPedidoForm extends ListadoPedidosForm {
	
	private String botonGuardarAbono;
	private String botonSi;
	private String botonNo;
	
	private String codigoCaja;
	private String codigoCajero;
	private String comboFormasPago;
	private String comboTiposAbonos;
	private String observacionAbono;
	private String valorAbono;
	private String numeroTransaccionAbono;
	private String puntoEmisionFactura;
	private String numeroFactura;
	
	/**
	 * 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		this.botonNo = null;
		this.botonSi = null;
		this.botonGuardarAbono = null;
		this.codigoCaja = null;
		this.codigoCajero = null;
		this.comboFormasPago = null;
		this.comboTiposAbonos = null;
		this.observacionAbono = null;
		this.valorAbono = null;
		this.numeroTransaccionAbono = null;
		this.puntoEmisionFactura = null;
		this.numeroFactura = null;
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {		
		ActionErrors errors = null;
		PropertyValidator validar = null;
		String ayuda = request.getParameter(Globals.AYUDA);
		VistaPedidoDTO pedidoActual = null;
		double limiteValorAbono = 0D;
		double limiteInferiorAbono = 0D;
		double valorAbono = 0D;
		
		if (ayuda != null && ayuda.equals("") == false){
			if (ayuda.equals("siVolverBusqueda") || ayuda.equals("siReenviarPedido")) {
				this.botonSi = ayuda;
			} else if (ayuda.equals("registrarAbonoPedido")) {
				this.botonGuardarAbono = ayuda;
			}
		}
		
		if (this.botonGuardarAbono != null || request.getParameter("registrarAbono")!= null) {
			errors = new ActionErrors();
			validar = new PropertyValidatorImpl();
			
			if (request.getSession().getAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos") != null){
				pedidoActual = (VistaPedidoDTO) request.getSession().getAttribute("ec.com.smx.sic.sispe.VistaPedidoAbonos");
			}
			
			//Punto de emision Factura
			validar.validateFormato(errors, "puntoEmisionFactura", this.puntoEmisionFactura ,true, "\\d{3}", "errors.formato.puntoEmisionFactura", "errors.requerido", "Punto Emision de Factura" );
			
			//Numero Factura
			validar.validateFormato(errors, "numeroFactura", this.numeroFactura,true, "\\d{9}", "errors.formato.numeroFactura", "errors.requerido", "Numero de Factura" );
			
			//Valor del Abono
			validar.validateFormato(errors, "valorAbono", this.valorAbono, true, "^((\\d)*(\\.(\\d){1,2})?)$", "errors.formato.valorAbonoPedido", "errors.requerido", "Valor del abono");
			if (errors.size() == 0){
				if (pedidoActual != null && pedidoActual.getSaldoAbonoPedido() != null){
					limiteValorAbono = pedidoActual.getSaldoAbonoPedido();
					//correcci\u00F3n de los decimales del l\u00EDmite de abono
					DecimalFormat twoDigit = new DecimalFormat("0.00");
					limiteValorAbono = Double.valueOf(twoDigit.format(limiteValorAbono).replace(",", "."));
					
					if (MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.estado.pagado.sinPago").equals(pedidoActual.getCodigoEstadoPagado()) 
							&& pedidoActual.getValorAbonoInicialManual() != null){
						limiteInferiorAbono = pedidoActual.getValorAbonoInicialManual();
						/* TODO: incialmente se solicit\u00F3 asi
						if(pedidoActual.getValorAbonoInicialManual().doubleValue() <= pedidoActual.getValorAbonoInicialSistema().doubleValue())
							limiteInferiorAbono = pedidoActual.getValorAbonoInicialManual().doubleValue();
						else
							limiteInferiorAbono = pedidoActual.getValorAbonoInicialSistema().doubleValue();*/
					}
					LogSISPE.getLog().info("valorLimiteAbonoRegistroAbono:{} " , limiteValorAbono);
					//LogSISPE.getLog().info("valorLimiteAbonoRegistroAbono(format): " + limiteValorAbono);
					LogSISPE.getLog().info("valorLimiteInferiorAbonoRegistroAbono: {}" , limiteInferiorAbono);
				} 
				valorAbono = Double.parseDouble(this.valorAbono);
				if ((valorAbono == 0 || valorAbono < limiteInferiorAbono) || valorAbono > limiteValorAbono){
					errors.add("valorAbono", new ActionMessage("errors.rango.valorAbonoPedido", limiteInferiorAbono, limiteValorAbono));
				}
			}
			
			//Tipos de abonos
			//validar.validateMandatory(errors, "comboTiposAbonos", this.comboTiposAbonos, "errors.requerido", "Tipo de Abono");
			
			//Forma de pago
			validar.validateMandatory(errors, "comboFormasPago", this.comboFormasPago, "errors.requerido", "Forma de Pago");
			
			//C\u00F3digo de caja
			validar.validateFormato(errors, "codigoCaja", this.codigoCaja, true, "^(\\d{1,4})$", "errors.formato.codigoCaja", "errors.requerido", "C&oacute;digo de caja");
			
			//C\u00F3digo de cajero
			validar.validateFormato(errors, "codigoCajero", this.codigoCajero, true, "^(\\d{1,10})$", "errors.formato.codigoCajero", "errors.requerido", "C&oacutedigo de cajero");
			
			//N\u00FAmero de transacci\u00F3n
			validar.validateFormato(errors, "numeroTransaccionAbono", this.numeroTransaccionAbono, true, "^(\\d{1,4})$", "errors.formato.numeroTransaccionAbono", "errors.requerido", "N&uacute;mero de transacci&oacute;n");
			
			//Observaci\u00F3n
			validar.validateFormato(errors, "observacionAbono", this.observacionAbono, true, "^(.){1,500}$", "errors.formato.observacionAbono", "errors.requerido", "Observaci&oacute;n del abono");
			
		} else {
			errors = super.validate(mapping, request);
		}
		
		return errors;
	}



	/**
	 * @return el botonNo
	 */
	public String getBotonNo() {
		return botonNo;
	}

	/**
	 * @param botonNo el botonNo a establecer
	 */
	public void setBotonNo(String botonNo) {
		this.botonNo = botonNo;
	}

	/**
	 * @return el botonSi
	 */
	public String getBotonSi() {
		return botonSi;
	}

	/**
	 * @param botonSi el botonSi a establecer
	 */
	public void setBotonSi(String botonSi) {
		this.botonSi = botonSi;
	}


	/**
	 * @return el botonGuardarAbono
	 */
	public String getBotonGuardarAbono() {
		return botonGuardarAbono;
	}


	/**
	 * @param botonGuardarAbono el botonGuardarAbono a establecer
	 */
	public void setBotonGuardarAbono(String botonGuardarAbono) {
		this.botonGuardarAbono = botonGuardarAbono;
	}


	/**
	 * @return el codigoCaja
	 */
	public String getCodigoCaja() {
		return codigoCaja;
	}


	/**
	 * @param codigoCaja el codigoCaja a establecer
	 */
	public void setCodigoCaja(String codigoCaja) {
		this.codigoCaja = codigoCaja;
	}


	/**
	 * @return el codigoCajero
	 */
	public String getCodigoCajero() {
		return codigoCajero;
	}


	/**
	 * @param codigoCajero el codigoCajero a establecer
	 */
	public void setCodigoCajero(String codigoCajero) {
		this.codigoCajero = codigoCajero;
	}


	/**
	 * @return el comboFormasPago
	 */
	public String getComboFormasPago() {
		return comboFormasPago;
	}


	/**
	 * @param comboFormasPago el comboFormasPago a establecer
	 */
	public void setComboFormasPago(String comboFormasPago) {
		this.comboFormasPago = comboFormasPago;
	}


	/**
	 * @return el numeroTransaccionAbono
	 */
	public String getNumeroTransaccionAbono() {
		return numeroTransaccionAbono;
	}


	/**
	 * @param numeroTransaccionAbono el numeroTransaccionAbono a establecer
	 */
	public void setNumeroTransaccionAbono(String numeroTransaccionAbono) {
		this.numeroTransaccionAbono = numeroTransaccionAbono;
	}


	/**
	 * @return el observacionAbono
	 */
	public String getObservacionAbono() {
		return observacionAbono;
	}


	/**
	 * @param observacionAbono el observacionAbono a establecer
	 */
	public void setObservacionAbono(String observacionAbono) {
		this.observacionAbono = observacionAbono;
	}


	/**
	 * @return el valorAbono
	 */
	public String getValorAbono() {
		return valorAbono;
	}


	/**
	 * @param valorAbono el valorAbono a establecer
	 */
	public void setValorAbono(String valorAbono) {
		this.valorAbono = valorAbono;
	}


	/**
	 * @return el comboTiposAbonos
	 */
	public String getComboTiposAbonos() {
		return comboTiposAbonos;
	}


	/**
	 * @param comboTiposAbonos el comboTiposAbonos a establecer
	 */
	public void setComboTiposAbonos(String comboTiposAbonos) {
		this.comboTiposAbonos = comboTiposAbonos;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getPuntoEmisionFactura() {
		return puntoEmisionFactura;
	}

	public void setPuntoEmisionFactura(String puntoEmisionFactura) {
		this.puntoEmisionFactura = puntoEmisionFactura;
	}
	
	
	
}