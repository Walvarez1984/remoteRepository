/**
 * 
 */
package ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author fmunoz
 *
 */
@SuppressWarnings("serial")
public class PedidoRDTO implements Serializable
{
  private String numeroPedido;
  private String cedulaContacto;
  private String nombreContacto;
  private String telefonoContacto;
  private String rucEmpresa;
  private String nombreEmpresa;
  private Collection <EntregaRDTO> entregas;
  
  //datos del local
  private String [] datosLocal;
  
	/**
	 * @return el cedulaContacto
	 */
	public String getCedulaContacto() {
		return cedulaContacto;
	}
	/**
	 * @param cedulaContacto el cedulaContacto a establecer
	 */
	public void setCedulaContacto(String cedulaContacto) {
		this.cedulaContacto = cedulaContacto;
	}
	/**
	 * @return el nombreContacto
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}
	/**
	 * @param nombreContacto el nombreContacto a establecer
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	/**
	 * @return el nombreEmpresa
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	/**
	 * @param nombreEmpresa el nombreEmpresa a establecer
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	/**
	 * @return el numeroPedido
	 */
	public String getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido el numeroPedido a establecer
	 */
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/**
	 * @return el rucEmpresa
	 */
	public String getRucEmpresa() {
		return rucEmpresa;
	}
	/**
	 * @param rucEmpresa el rucEmpresa a establecer
	 */
	public void setRucEmpresa(String rucEmpresa) {
		this.rucEmpresa = rucEmpresa;
	}
	/**
	 * @return el telefonoContacto
	 */
	public String getTelefonoContacto() {
		return telefonoContacto;
	}
	/**
	 * @param telefonoContacto el telefonoContacto a establecer
	 */
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}
	
	/**
	 * @return el entregas
	 */
	public Collection<EntregaRDTO> getEntregas() {
		return entregas;
	}
	/**
	 * @param entregas el entregas a establecer
	 */
	public void setEntregas(Collection<EntregaRDTO> entregas) {
		this.entregas = entregas;
	}
	/**
	 * @return el datosLocal
	 */
	public String[] getDatosLocal() {
		return datosLocal;
	}
	/**
	 * @param datosLocal el datosLocal a establecer
	 */
	public void setDatosLocal(String[] datosLocal) {
		this.datosLocal = datosLocal;
	}

  
}
