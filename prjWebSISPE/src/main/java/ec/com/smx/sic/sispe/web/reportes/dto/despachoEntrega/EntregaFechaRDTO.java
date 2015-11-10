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
public class EntregaFechaRDTO implements Serializable
{
	private String fechaDespachoEntrega;
	private Collection <BeneficiarioRDTO> beneficiarios;
	
	/**
	 * @return el beneficiarios
	 */
	public Collection<BeneficiarioRDTO> getBeneficiarios() {
		return beneficiarios;
	}
	/**
	 * @param beneficiarios el beneficiarios a establecer
	 */
	public void setBeneficiarios(Collection<BeneficiarioRDTO> beneficiarios) {
		this.beneficiarios = beneficiarios;
	}
	/**
	 * @return el fechaDespachoEntrega
	 */
	public String getFechaDespachoEntrega() {
		return fechaDespachoEntrega;
	}
	/**
	 * @param fechaDespachoEntrega el fechaDespachoEntrega a establecer
	 */
	public void setFechaDespachoEntrega(String fechaDespachoEntrega) {
		this.fechaDespachoEntrega = fechaDespachoEntrega;
	}
	
	
}
