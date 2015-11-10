/*
 * Clase BeneficiarioRDTO.java
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
public class BeneficiarioRDTO implements Serializable
{
  private String cedulaBeneficiario;
  private String nombreBeneficiario;
  private Collection <ArticuloRDTO> articulos;
  
  
  /**
   * @return Devuelve cedulaBeneficiario.
   */
  public String getCedulaBeneficiario()
  {
    return cedulaBeneficiario;
  }
  /**
   * @param cedulaBeneficiario El cedulaBeneficiario a establecer.
   */
  public void setCedulaBeneficiario(String cedulaBeneficiario)
  {
    this.cedulaBeneficiario = cedulaBeneficiario;
  }
  /**
   * @return Devuelve nombreBeneficiario.
   */
  public String getNombreBeneficiario()
  {
    return nombreBeneficiario;
  }
  /**
   * @param nombreBeneficiario El nombreBeneficiario a establecer.
   */
  public void setNombreBeneficiario(String nombreBeneficiario)
  {
    this.nombreBeneficiario = nombreBeneficiario;
  }
  
	/**
	 * @return el articulos
	 */
	public Collection<ArticuloRDTO> getArticulos() {
		return articulos;
	}
	/**
	 * @param articulos el articulos a establecer
	 */
	public void setArticulos(Collection<ArticuloRDTO> articulos) {
		this.articulos = articulos;
	}

}
