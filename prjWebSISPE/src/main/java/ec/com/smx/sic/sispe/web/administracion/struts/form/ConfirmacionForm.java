/*
 * Clase ConfirmacionForm.java
 * Creado el 12/05/2006
 *
 */
package ec.com.smx.sic.sispe.web.administracion.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * <p>
 * Esta clase contiene controles del formulario de confirmaci\u00F3n. Tambi\u00E9n se resetea el formulario cada vez 
 * que se realiza una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class ConfirmacionForm extends ActionForm
{
  /**
   * Botones principales para confirmar una determinada acci\u00F3n
   * <ul>
   * <li>String botonSI: Bot\u00F3n que confirma una acci\u00F3n.</li>
   * <li>String botonNO: Bot\u00F3n que cancela una acci\u00F3n.</li>
   * </ul>
   */
  private String botonSI;
  private String botonNO;
  

  /**
   * Resetea los controles del formulario de la p\u00E1gina <code>preguntaSiNo.jsp</code>, en cada petici\u00F3n.
   * @param mapping 	El mapeo utilizado para seleccionar esta instancia
   * @param request 	La petici&oacue; que estamos procesando
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
    this.botonSI=null;
    this.botonNO=null;
  }
  
  /**
   * @return Devuelve botonNO.
   */
  public String getBotonNO()
  {
    return botonNO;
  }
  /**
   * @param botonNO El botonNO a establecer.
   */
  public void setBotonNO(String botonNO)
  {
    this.botonNO = botonNO;
  }
  /**
   * @return Devuelve botonSI.
   */
  public String getBotonSI()
  {
    return botonSI;
  }
  /**
   * @param botonSI El botonSI a establecer.
   */
  public void setBotonSI(String botonSI)
  {
    this.botonSI = botonSI;
  }
}
