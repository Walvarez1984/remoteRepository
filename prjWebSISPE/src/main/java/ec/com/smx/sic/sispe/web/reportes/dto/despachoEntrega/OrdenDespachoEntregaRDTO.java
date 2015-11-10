/*
 * Clase OrdenDespachoEntregaDTO.java
 * Creado el 15/08/2006
 *
 */
package ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author 	fmunoz
 * @version 2.0
 */
@SuppressWarnings("serial")
public class OrdenDespachoEntregaRDTO implements Serializable
{
	private String descripcionLocal;
  private Collection <PedidoRDTO> pedidos;
  
	/**
	 * @return el descripcionLocal
	 */
	public String getDescripcionLocal() {
		return descripcionLocal;
	}
	/**
	 * @param descripcionLocal el descripcionLocal a establecer
	 */
	public void setDescripcionLocal(String descripcionLocal) {
		this.descripcionLocal = descripcionLocal;
	}
	/**
	 * @return el pedidos
	 */
	public Collection<PedidoRDTO> getPedidos() {
		return pedidos;
	}
	/**
	 * @param pedidos el pedidos a establecer
	 */
	public void setPedidos(Collection<PedidoRDTO> pedidos) {
		this.pedidos = pedidos;
	}

}
