/*
 * OrdenDiasSemanaDTO
 * Creado el 10/09/2012
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;

import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalCamionDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalHoraDTO;

/**
 *  Clase que permite reconocer el orden de dias de la semana
 *  @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class CalendarioDetPlaLocHorCamDTO implements Serializable{
	
	private CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO;
	private CalendarioDetallePlantillaLocalHoraDTO calendarioDetallePlantillaLocalHoraDTO;
	private CalendarioDetallePlantillaLocalCamionDTO calendarioDetallePlantillaLocalCamionDTO;
	
	public CalendarioDetallePlantillaLocalDTO getCalendarioDetallePlantillaLocalDTO() {
		return calendarioDetallePlantillaLocalDTO;
	}
	public void setCalendarioDetallePlantillaLocalDTO(
			CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO) {
		this.calendarioDetallePlantillaLocalDTO = calendarioDetallePlantillaLocalDTO;
	}
	public CalendarioDetallePlantillaLocalHoraDTO getCalendarioDetallePlantillaLocalHoraDTO() {
		return calendarioDetallePlantillaLocalHoraDTO;
	}
	public void setCalendarioDetallePlantillaLocalHoraDTO(
			CalendarioDetallePlantillaLocalHoraDTO calendarioDetallePlantillaLocalHoraDTO) {
		this.calendarioDetallePlantillaLocalHoraDTO = calendarioDetallePlantillaLocalHoraDTO;
	}
	public CalendarioDetallePlantillaLocalCamionDTO getCalendarioDetallePlantillaLocalCamionDTO() {
		return calendarioDetallePlantillaLocalCamionDTO;
	}
	public void setCalendarioDetallePlantillaLocalCamionDTO(
			CalendarioDetallePlantillaLocalCamionDTO calendarioDetallePlantillaLocalCamionDTO) {
		this.calendarioDetallePlantillaLocalCamionDTO = calendarioDetallePlantillaLocalCamionDTO;
	}
	
	
	
}

