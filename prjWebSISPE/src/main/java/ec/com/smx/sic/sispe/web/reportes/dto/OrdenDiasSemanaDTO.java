/*
 * OrdenDiasSemanaDTO
 * Creado el 20/04/2007
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;

/**
 *  Clase que permite reconocer el orden de dias de la semana
 *  @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class OrdenDiasSemanaDTO implements Serializable{
	private String nombreDia;
	private String seleccion;
	
	
	/**
	 * @return Devuelve nombreDia.
	 */
	public String getNombreDia() {
		return nombreDia;
	}
	/**
	 * @param nombreDia El nombreDia a establecer.
	 */
	public void setNombreDia(String nombreDia) {
		this.nombreDia = nombreDia;
	}

	/**
	 * @return Devuelve seleccion.
	 */
	public String getSeleccion() {
		return seleccion;
	}
	/**
	 * @param seleccion El seleccion a establecer.
	 */
	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
	}
}
