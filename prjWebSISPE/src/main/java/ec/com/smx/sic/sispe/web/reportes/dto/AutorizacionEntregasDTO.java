/* Creado el 06/08/2007
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import ec.com.smx.autorizaciones.dto.AutorizacionDTO;

/**
 * @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class AutorizacionEntregasDTO implements Serializable{
private Long codigoAutorizacion;
private String codigoLocal;
private String numeroAutorizacion;
private Timestamp fechaAutorizacion;
private AutorizacionDTO autorizacionDTO;
/**
 * @return el codigoAutorizacion
 */
public Long getCodigoAutorizacion() {
	return this.codigoAutorizacion;
}
/**
 * @param codigoAutorizacion el codigoAutorizacion a establecer
 */
public void setCodigoAutorizacion(Long codigoAutorizacion) {
	this.codigoAutorizacion = codigoAutorizacion;
}
/**
 * @return el codigoLocal
 */
public String getCodigoLocal() {
	return this.codigoLocal;
}
/**
 * @param codigoLocal el codigoLocal a establecer
 */
public void setCodigoLocal(String codigoLocal) {
	this.codigoLocal = codigoLocal;
}
/**
 * @return el fechaAutorizacion
 */
public Timestamp getFechaAutorizacion() {
	return this.fechaAutorizacion;
}
/**
 * @param fechaAutorizacion el fechaAutorizacion a establecer
 */
public void setFechaAutorizacion(Timestamp fechaAutorizacion) {
	this.fechaAutorizacion = fechaAutorizacion;
}
/**
 * @return el numeroAutorizacion
 */
public String getNumeroAutorizacion() {
	return this.numeroAutorizacion;
}
/**
 * @param numeroAutorizacion el numeroAutorizacion a establecer
 */
public void setNumeroAutorizacion(String numeroAutorizacion) {
	this.numeroAutorizacion = numeroAutorizacion;
}
/**
 * @return el autorizacionDTO
 */
public AutorizacionDTO getAutorizacionDTO() {
	return this.autorizacionDTO;
}
/**
 * @param autorizacionDTO el autorizacionDTO a establecer
 */
public void setAutorizacionDTO(AutorizacionDTO autorizacionDTO) {
	this.autorizacionDTO = autorizacionDTO;
}



}
