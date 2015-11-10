/*
 * Clase ArticuloRDTO.java
 * Creado el 15/08/2006
 *
 */
package ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega;

import java.io.Serializable;

/**
 * @author fmunoz
 * @version %i%
 */
@SuppressWarnings("serial")
public class ArticuloRDTO implements Serializable
{
  private String codigoArticulo;
  private String descripcionArticulo;
  private String cantidadArticulo;
  private String codigoBarras;
  
  /**
   * @return Devuelve cantidadArticulo.
   */
  public String getCantidadArticulo()
  {
    return cantidadArticulo;
  }
  /**
   * @param cantidadArticulo El cantidadArticulo a establecer.
   */
  public void setCantidadArticulo(String cantidadArticulo)
  {
    this.cantidadArticulo = cantidadArticulo;
  }
  /**
   * @return Devuelve codigoArticulo.
   */
  public String getCodigoArticulo()
  {
    return codigoArticulo;
  }
  /**
   * @param codigoArticulo El codigoArticulo a establecer.
   */
  public void setCodigoArticulo(String codigoArticulo)
  {
    this.codigoArticulo = codigoArticulo;
  }
  /**
   * @return Devuelve descripcionArticulo.
   */
  public String getDescripcionArticulo()
  {
    return descripcionArticulo;
  }
  /**
   * @param descripcionArticulo El descripcionArticulo a establecer.
   */
  public void setDescripcionArticulo(String descripcionArticulo)
  {
    this.descripcionArticulo = descripcionArticulo;
  }
/**
 * @return el codigoBarras
 */
public String getCodigoBarras() {
	return codigoBarras;
}
/**
 * @param codigoBarras el codigoBarras a establecer
 */
public void setCodigoBarras(String codigoBarras) {
	this.codigoBarras = codigoBarras;
}
  
  
}
