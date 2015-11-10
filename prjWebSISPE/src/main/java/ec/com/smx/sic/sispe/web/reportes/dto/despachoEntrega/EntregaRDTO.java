/*
 * Clase EntregaRDTO.java
 * Creado el 15/08/2006
 *
 */
package ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author fmunoz
 * @version %i%
 */
@SuppressWarnings("serial")
public class EntregaRDTO implements Serializable
{
  private String lugarEntrega;
  private Collection <EntregaFechaRDTO> entregasFecha;
  
  //datos del pedido
  private String [] datosPedido;
  
  /**
   * @return Devuelve lugarEntrega.
   */
  public String getLugarEntrega()
  {
    return lugarEntrega;
  }
  /**
   * @param lugarEntrega El lugarEntrega a establecer.
   */
  public void setLugarEntrega(String lugarEntrega)
  {
    this.lugarEntrega = lugarEntrega;
  }
  
	/**
	 * @return el datosPedido
	 */
	public String[] getDatosPedido() {
		return datosPedido;
	}
	/**
	 * @param datosPedido el datosPedido a establecer
	 */
	public void setDatosPedido(String[] datosPedido) {
		this.datosPedido = datosPedido;
	}
	/**
	 * @return el entregasFecha
	 */
	public Collection<EntregaFechaRDTO> getEntregasFecha() {
		return entregasFecha;
	}
	/**
	 * @param entregasFecha el entregasFecha a establecer
	 */
	public void setEntregasFecha(Collection<EntregaFechaRDTO> entregasFecha) {
		this.entregasFecha = entregasFecha;
	}
	
}
