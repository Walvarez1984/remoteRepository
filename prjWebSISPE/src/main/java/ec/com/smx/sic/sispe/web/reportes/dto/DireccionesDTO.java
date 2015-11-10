/* Creado el 01/08/2007
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class DireccionesDTO implements Cloneable,Serializable{
private String codigoDireccion;
private String descripcion;
private Timestamp fechaEntrega;
private int numeroDireccion;
private String codigoSector;
private String distanciaDireccion;
private String horas;
private String minutos;

//valor del costo de la entregas asociadas a esta direccion
private Double valorFlete;



public DireccionesDTO clone(){
	DireccionesDTO clon = null;
		try{
			clon = (DireccionesDTO) super.clone();
		} catch (CloneNotSupportedException e){
			LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
		}
		return clon;
	}

/**
 * @return el codigoDireccion
 */
public String getCodigoDireccion() {
	return this.codigoDireccion;
}
/**
 * @param codigoDireccion el codigoDireccion a establecer
 */
public void setCodigoDireccion(String codigoDireccion) {
	this.codigoDireccion = codigoDireccion;
}
/**
 * @return el descripcion
 */
public String getDescripcion() {
	return this.descripcion;
}
/**
 * @param descripcion el descripcion a establecer
 */
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
/**
 * @return el fechaEntrega
 */
public Timestamp getFechaEntrega() {
	return this.fechaEntrega;
}
/**
 * @param fechaEntrega el fechaEntrega a establecer
 */
public void setFechaEntrega(Timestamp fechaEntrega) {
	this.fechaEntrega = fechaEntrega;
}
/**
 * @return el numeroDireccion
 */
public int getNumeroDireccion() {
	return this.numeroDireccion;
}
/**
 * @param numeroDireccion el numeroDireccion a establecer
 */
public void setNumeroDireccion(int numeroDireccion) {
	this.numeroDireccion = numeroDireccion;
}
/**
 * @return el codigoSector
 */
public String getCodigoSector() {
	return this.codigoSector;
}
/**
 * @param codigoSector el codigoSector a establecer
 */
public void setCodigoSector(String codigoSector) {
	this.codigoSector = codigoSector;
}

/**
 * @return el horas
 */
public String getHoras() {
	return horas;
}

/**
 * @param horas el horas a establecer
 */
public void setHoras(String horas) {
	this.horas = horas;
}

/**
 * @return el minutos
 */
public String getMinutos() {
	return minutos;
}

/**
 * @param minutos el minutos a establecer
 */
public void setMinutos(String minutos) {
	this.minutos = minutos;
}

public Double getValorFlete() {
	return valorFlete;
}

public void setValorFlete(Double valorFlete) {
	this.valorFlete = valorFlete;
}

/**
 * @return el distanciaDireccion
 */
public String getDistanciaDireccion() {
	return this.distanciaDireccion;
}

/**
 * @param distanciaDireccion el distanciaDireccion a establecer
 */
public void setDistanciaDireccion(String distanciaDireccion) {
	this.distanciaDireccion = distanciaDireccion;
}

}
