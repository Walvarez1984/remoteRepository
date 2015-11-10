/* Creado el 27/06/2007
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.web.reportes.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.EntregaDetallePedidoDTO;

/**
 *  Clase que permite manejar los costos de las entregas a domicilio
 *  @author jacalderon
 *
 */
@SuppressWarnings("serial")
public class CostoEntregasDTO implements Cloneable,Serializable{
	
	private String codigoSector;
	private Timestamp fechaEntrega;
	private String nombreSector;
	private Double valor;
	private int numeroEntregas;
	private Double porcentajeCostoFlete;
	private double distancia;
	private Double npValorParcial;
	
	//atributos para asociar el valor del flete a la direccion
	private String descripcion;
	
	private Collection<EntregaDetallePedidoDTO> entregaDetallePedidoCol;
	
	/**
	 * @return el distancia
	 */
	public double getDistancia() {
		return this.distancia;
	}

	/**
	 * @param distancia el distancia a establecer
	 */
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public CostoEntregasDTO clone(){
		CostoEntregasDTO clon = null;
			try{
				clon = (CostoEntregasDTO) super.clone();
			} catch (CloneNotSupportedException e){
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			return clon;
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
	 * @return el fechaEntrega
	 */
	public Timestamp getFechaEntrega() {
		return fechaEntrega;
	}

	/**
	 * @param fechaEntrega el fechaEntrega a establecer
	 */
	public void setFechaEntrega(Timestamp fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	/**
	 * @return el nombreSector
	 */
	public String getNombreSector() {
		return this.nombreSector;
	}
	/**
	 * @param nombreSector el nombreSector a establecer
	 */
	public void setNombreSector(String nombreSector) {
		this.nombreSector = nombreSector;
	}
	/**
	 * @return el valor
	 */
	public Double getValor() {
		return this.valor;
	}
	/**
	 * @param valor el valor a establecer
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}
	/**
	 * @return el numeroEntregas
	 */
	public int getNumeroEntregas() {
		return this.numeroEntregas;
	}
	/**
	 * @param numeroEntregas el numeroEntregas a establecer
	 */
	public void setNumeroEntregas(int numeroEntregas) {
		this.numeroEntregas = numeroEntregas;
	}
	/**
	 * @return el porcentajeCostoFlete
	 */
	public Double getPorcentajeCostoFlete() {
		return porcentajeCostoFlete;
	}
	/**
	 * @param porcentajeCostoFlete el porcentajeCostoFlete a establecer
	 */
	public void setPorcentajeCostoFlete(Double porcentajeCostoFlete) {
		this.porcentajeCostoFlete = porcentajeCostoFlete;
	}

	public Double getNpValorParcial() {
		return npValorParcial;
	}

	public void setNpValorParcial(Double npValorParcial) {
		this.npValorParcial = npValorParcial;
	}

	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Collection<EntregaDetallePedidoDTO> getEntregaDetallePedidoCol() {
		return entregaDetallePedidoCol;
	}

	public void setEntregaDetallePedidoCol(
			Collection<EntregaDetallePedidoDTO> entregaDetallePedidoCol) {
		this.entregaDetallePedidoCol = entregaDetallePedidoCol;
	}

	
}
