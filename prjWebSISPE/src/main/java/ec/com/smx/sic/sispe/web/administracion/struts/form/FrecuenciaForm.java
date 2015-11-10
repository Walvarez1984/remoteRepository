/*
 * Creado el 09/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import org.apache.struts.action.ActionForm;

/**
 * @author cbarahona
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
@SuppressWarnings("serial")
public class FrecuenciaForm extends ActionForm {
	private String codigoCompania;
	private String codigoFrecuencia;
	private String descripcionFrecuencia;
	private String estadoFrecuencia;
	/**
	 * @return Devuelve codigoCompania.
	 */
	public String getCodigoCompania() {
		return codigoCompania;
	}
	/**
	 * @param codigoCompania El codigoCompania a establecer.
	 */
	public void setCodigoCompania(String codigoCompania) {
		this.codigoCompania = codigoCompania;
	}
	/**
	 * @return Devuelve codigoFrecuencia.
	 */
	public String getCodigoFrecuencia() {
		return codigoFrecuencia;
	}
	/**
	 * @param codigoFrecuencia El codigoFrecuencia a establecer.
	 */
	public void setCodigoFrecuencia(String codigoFrecuencia) {
		this.codigoFrecuencia = codigoFrecuencia;
	}
	/**
	 * @return Devuelve descripcionFrecuencia.
	 */
	public String getDescripcionFrecuencia() {
		return descripcionFrecuencia;
	}
	/**
	 * @param descripcionFrecuencia El descripcionFrecuencia a establecer.
	 */
	public void setDescripcionFrecuencia(String descripcionFrecuencia) {
		this.descripcionFrecuencia = descripcionFrecuencia;
	}
	/**
	 * @return Devuelve estadoFrecuencia.
	 */
	public String getEstadoFrecuencia() {
		return estadoFrecuencia;
	}
	/**
	 * @param estadoFrecuencia El estadoFrecuencia a establecer.
	 */
	public void setEstadoFrecuencia(String estadoFrecuencia) {
		this.estadoFrecuencia = estadoFrecuencia;
	}
}
