/*
 * CantidadTransporteCapacidad.java
 * Creado el 22/09/2012 09:56:32
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import ec.com.smx.sic.cliente.mdl.dto.TransporteDTO;

/**
 * @author bgudino
 *
 */
public class CantidadTransporteCapacidad {

	private Integer capacidadCamion;
	private Integer cantidadDisponible;
	private Integer cantidadUtilizada;
	private TransporteDTO transporteDTO; 
	private Long secuencialHoraCamion;
	
	public CantidadTransporteCapacidad(){
		this.capacidadCamion = 0;
		this.cantidadDisponible = 0;
		this.transporteDTO = null;
		this.secuencialHoraCamion = 0L;
		this.cantidadUtilizada = 0;
	}
	
	
	public CantidadTransporteCapacidad(CantidadTransporteCapacidad obj){
		this.capacidadCamion = obj.getCapacidadCamion();
		this.cantidadDisponible = obj.getCantidadDisponible();
		this.transporteDTO = obj.getTransporteDTO();
		this.secuencialHoraCamion = obj.getSecuencialHoraCamion();
	}
	public CantidadTransporteCapacidad(Integer cantidadDisponible, TransporteDTO transporte, Long secuencialPadre){
		this.cantidadDisponible = cantidadDisponible;
		this.transporteDTO = transporte;
		this.capacidadCamion = transporteDTO.getCantidadBultos();
		this.secuencialHoraCamion = secuencialPadre;

	}


	/**
	 * @return el capacidadCamiones
	 */
	public Integer getCapacidadCamion() {
		return capacidadCamion;
	}

	/**
	 * @param capacidadCamiones el capacidadCamiones a establecer
	 */
	public void setCapacidadCamion(Integer capacidadCamion) {
		this.capacidadCamion = capacidadCamion;
	}

	
	/**
	 * @return el cantidadDisponible
	 */
	public Integer getCantidadDisponible() {
		return cantidadDisponible;
	}

	/**
	 * @param cantidadDisponible el cantidadDisponible a establecer
	 */
	public void setCantidadDisponible(Integer cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	/**
	 * @return el transporteDTO
	 */
	public TransporteDTO getTransporteDTO() {
		return transporteDTO;
	}

	/**
	 * @param transporteDTO el transporteDTO a establecer
	 */
	public void setTransporteDTO(TransporteDTO transporteDTO) {
		this.transporteDTO = transporteDTO;
	}

	/**
	 * @return el secuencialHoraCamion
	 */
	public Long getSecuencialHoraCamion() {
		return secuencialHoraCamion;
	}

	/**
	 * @param secuencialHoraCamion el secuencialHoraCamion a establecer
	 */
	public void setSecuencialHoraCamion(Long secuencialHoraCamion) {
		this.secuencialHoraCamion = secuencialHoraCamion;
	}

	/**
	 * @return el cantidadUtilizada
	 */
	public Integer getCantidadUtilizada() {
		return cantidadUtilizada;
	}

	/**
	 * @param cantidadUtilizada el cantidadUtilizada a establecer
	 */
	public void setCantidadUtilizada(Integer cantidadUtilizada) {
		this.cantidadUtilizada = cantidadUtilizada;
	}
	
	

}
