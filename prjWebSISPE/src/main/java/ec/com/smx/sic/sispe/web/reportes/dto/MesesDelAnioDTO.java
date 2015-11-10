/*
 * MesesDelAniioDTO.java
 * Creado el 21/05/2007
 *
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;

/**
 * Clase que permite cargar los meses del a\u00F1o
 * @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class MesesDelAnioDTO implements Serializable{
	String codigoMes;
	String nombreMes;
	
	
	/**
	 * @return Devuelve codigoMes.
	 */
	public String getCodigoMes() {
		return codigoMes;
	}
	/**
	 * @param codigoMes El codigoMes a establecer.
	 */
	public void setCodigoMes(String codigoMes) {
		this.codigoMes = codigoMes;
	}
	/**
	 * @return Devuelve nombreMes.
	 */
	public String getNombreMes() {
		return nombreMes;
	}
	/**
	 * @param nombreMes El nombreMes a establecer.
	 */
	public void setNombreMes(String nombreMes) {
		this.nombreMes = nombreMes;
	}
}
