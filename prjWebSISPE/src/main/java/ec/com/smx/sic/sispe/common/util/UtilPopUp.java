/**
 * 
 */
package ec.com.smx.sic.sispe.common.util;

import java.io.Serializable;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings("serial")
public class UtilPopUp implements Serializable
{
	//constantes para los botones
	public static final int OK = 1;
	public static final int OK_CANCEL = 2;
	
	//constantes para la forma de env\u00EDo de datos
	public static final int SUBMIT = 1;
	public static final int PERSONALIZADO = 2;
	
	//atributos
	private String tituloVentana;
	private String mensajeVentana;
	private String mensajeErrorVentana;
	private String contenidoVentana;
	private String preguntaVentana;
	private String valorOK;
	private String valorCANCEL;
	private Integer formaBotones;
	private Integer formaEnvioDatos;
	private String accionEnvio;
	private String accionEnvioCerrar;
	private String etiquetaBotonOK;
	private String etiquetaBotonCANCEL;
	private Double ancho;
	private Double tope;
	private String valorKeyPress;
	private String estiloOK;
	private String estiloCANCEL;
	private String noMostrarBotonCerrar;
	
	//constructor
	public UtilPopUp(){
		this.tituloVentana = null;
		this.mensajeVentana = null;
		this.preguntaVentana = null;
		this.valorCANCEL = null;
		this.valorOK = null;
		this.formaBotones = null;
		this.formaEnvioDatos = Integer.valueOf(SUBMIT);
		this.accionEnvio = null;
		this.ancho = 40D;
		this.mensajeErrorVentana = null;
		this.tope = null;
		this.valorKeyPress = null;
		this.noMostrarBotonCerrar=null;
	}


	/**
	 * @return el formaBotones
	 */
	public Integer getFormaBotones() {
		return formaBotones;
	}


	/**
	 * @param formaBotones el formaBotones a establecer
	 */
	public void setFormaBotones(Integer formaBotones) {
		this.formaBotones = formaBotones;
	}


	/**
	 * @return el mensajeVentana
	 */
	public String getMensajeVentana() {
		return mensajeVentana;
	}

	/**
	 * @param mensajeVentana el mensajeVentana a establecer
	 */
	public void setMensajeVentana(String mensajeVentana) {
		this.mensajeVentana = mensajeVentana;
	}

	/**
	 * @return el preguntaVentana
	 */
	public String getPreguntaVentana() {
		return preguntaVentana;
	}

	/**
	 * @param preguntaVentana el preguntaVentana a establecer
	 */
	public void setPreguntaVentana(String preguntaVentana) {
		this.preguntaVentana = preguntaVentana;
	}

	/**
	 * @return el tituloVentana
	 */
	public String getTituloVentana() {
		return tituloVentana;
	}

	/**
	 * @param tituloVentana el tituloVentana a establecer
	 */
	public void setTituloVentana(String tituloVentana) {
		this.tituloVentana = tituloVentana;
	}

	/**
	 * @return el valorCANCEL
	 */
	public String getValorCANCEL() {
		return valorCANCEL;
	}

	/**
	 * @param valorCANCEL el valorCANCEL a establecer
	 */
	public void setValorCANCEL(String valorCANCEL) {
		this.valorCANCEL = valorCANCEL;
	}

	/**
	 * @return el valorOK
	 */
	public String getValorOK() {
		return valorOK;
	}

	/**
	 * @param valorOK el valorOK a establecer
	 */
	public void setValorOK(String valorOK) {
		this.valorOK = valorOK;
	}


	/**
	 * @return el formaEnvioDatos
	 */
	public Integer getFormaEnvioDatos() {
		return formaEnvioDatos;
	}


	/**
	 * @param formaEnvioDatos el formaEnvioDatos a establecer
	 */
	public void setFormaEnvioDatos(Integer formaEnvioDatos) {
		this.formaEnvioDatos = formaEnvioDatos;
	}


	/**
	 * @return el accionEnvio
	 */
	public String getAccionEnvio() {
		return accionEnvio;
	}


	/**
	 * @param accionEnvio el accionEnvio a establecer
	 */
	public void setAccionEnvio(String accionEnvio) {
		this.accionEnvio = accionEnvio;
	}


	/**
	 * @return el contenidoVentana
	 */
	public String getContenidoVentana() {
		return contenidoVentana;
	}


	/**
	 * @param contenidoVentana el contenidoVentana a establecer
	 */
	public void setContenidoVentana(String contenidoVentana) {
		this.contenidoVentana = contenidoVentana;
	}


	/**
	 * @return el ancho
	 */
	public Double getAncho() {
		return ancho;
	}


	/**
	 * @param ancho el ancho a establecer
	 */
	public void setAncho(Double ancho) {
		this.ancho = ancho;
	}


	/**
	 * @return el accionEnvioCerrar
	 */
	public String getAccionEnvioCerrar() {
		return accionEnvioCerrar;
	}


	/**
	 * @param accionEnvioCerrar el accionEnvioCerrar a establecer
	 */
	public void setAccionEnvioCerrar(String accionEnvioCerrar) {
		this.accionEnvioCerrar = accionEnvioCerrar;
	}


	/**
	 * @return el etiquetaBotonCANCEL
	 */
	public String getEtiquetaBotonCANCEL() {
		return this.etiquetaBotonCANCEL;
	}


	/**
	 * @param etiquetaBotonCANCEL el etiquetaBotonCANCEL a establecer
	 */
	public void setEtiquetaBotonCANCEL(String etiquetaBotonCANCEL) {
		this.etiquetaBotonCANCEL = etiquetaBotonCANCEL;
	}


	/**
	 * @return el etiquetaBotonOK
	 */
	public String getEtiquetaBotonOK() {
		return this.etiquetaBotonOK;
	}


	/**
	 * @param etiquetaBotonOK el etiquetaBotonOK a establecer
	 */
	public void setEtiquetaBotonOK(String etiquetaBotonOK) {
		this.etiquetaBotonOK = etiquetaBotonOK;
	}


	/**
	 * @return el mensajeErrorVentana
	 */
	public String getMensajeErrorVentana() {
		return mensajeErrorVentana;
	}


	/**
	 * @param mensajeErrorVentana el mensajeErrorVentana a establecer
	 */
	public void setMensajeErrorVentana(String mensajeErrorVentana) {
		this.mensajeErrorVentana = mensajeErrorVentana;
	}


	/**
	 * @return el tope
	 */
	public Double getTope() {
		return tope;
	}


	/**
	 * @param tope el tope a establecer
	 */
	public void setTope(Double tope) {
		this.tope = tope;
	}


	/**
	 * @return el valorKeyPress
	 */
	public String getValorKeyPress() {
		return valorKeyPress;
	}


	/**
	 * @param valorKeyPress el valorKeyPress a establecer
	 */
	public void setValorKeyPress(String valorKeyPress) {
		this.valorKeyPress = valorKeyPress;
	}


	/**
	 * @return el estiloOK
	 */
	public String getEstiloOK() {
		return estiloOK;
	}


	/**
	 * @param estiloOK el estiloOK a establecer
	 */
	public void setEstiloOK(String estiloOK) {
		this.estiloOK = estiloOK;
	}


	/**
	 * @return el estiloCANCEL
	 */
	public String getEstiloCANCEL() {
		return estiloCANCEL;
	}


	/**
	 * @param estiloCANCEL el estiloCANCEL a establecer
	 */
	public void setEstiloCANCEL(String estiloCANCEL) {
		this.estiloCANCEL = estiloCANCEL;
	}


	public String getNoMostrarBotonCerrar() {
		return noMostrarBotonCerrar;
	}


	public void setNoMostrarBotonCerrar(String mostrarBotonCerrar) {
		this.noMostrarBotonCerrar = mostrarBotonCerrar;
	}

	}