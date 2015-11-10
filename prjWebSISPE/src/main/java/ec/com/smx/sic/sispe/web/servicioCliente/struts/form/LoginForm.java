/*
 * LoginForm.java
 * Creado el 23/03/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import org.apache.struts.action.ActionForm;

/**
 * <p>
 * Contiene los campos para el ingreso del login y password en el formulario de ingreso al sistema.
 * </p>
 * @author 	bmontesdeoca
 * @version 1.0
 * @since	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class LoginForm extends ActionForm
{
  /**
   * Campos donde se ingresan los datos del usuario que desea ingresar al sistema.
   * <ul>
   * <li>String login: Login del usuario</li>
   * <li>String password: Clave del usuario</li>
   * </ul>
   */
  private String login;
  private String password;

  /**
   * @return Devuelve login.
   */
  public String getLogin()
  {
    return login;
  }
  /**
   * @param login El login a establecer.
   */
  public void setLogin(String login)
  {
    this.login = login;
  }
  /**
   * @return Devuelve password.
   */
  public String getPassword()
  {
    return password;
  }
  /**
   * @param password El password a establecer.
   */
  public void setPassword(String password)
  {
    this.password = password;
  }
}
