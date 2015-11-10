/*
 * OrdenDiasSemanaDTO
 * Creado el 10/09/2012
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;
import java.util.Collection;

import ec.com.smx.sic.sispe.dto.CalendarioHoraCamionLocalDTO;

/**
 *  Clase que permite reconocer el orden de dias de la semana
 *  @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class HorasDTO implements Serializable{
	private String horas;
	private String seleccion;
	
	private Collection<CalendarioHoraCamionLocalDTO> npDetalleCalCamHorLoc;
	private Integer npPosicion;
	
	/**
	 * @return Devuelve horas.
	 */
	public String getHoras() {
		return horas;
	}
	/**
	 * @param horas Horas a establecer.
	 */
	public void setHoras(String horas) {
		this.horas = horas;
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
	public Collection<CalendarioHoraCamionLocalDTO> getNpDetalleCalCamHorLoc() {
		return npDetalleCalCamHorLoc;
	}
	public void setNpDetalleCalCamHorLoc(
			Collection<CalendarioHoraCamionLocalDTO> npDetalleCalCamHorLoc) {
		this.npDetalleCalCamHorLoc = npDetalleCalCamHorLoc;
	}
	public Integer getNpPosicion() {
		return npPosicion;
	}
	public void setNpPosicion(Integer npPosicion) {
		this.npPosicion = npPosicion;
	}
}

