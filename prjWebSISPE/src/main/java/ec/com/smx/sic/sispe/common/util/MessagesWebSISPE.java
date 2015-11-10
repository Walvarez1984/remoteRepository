/*
 * Creado el 23/03/2006
 *
 */
package ec.com.smx.sic.sispe.common.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Esta clase contiene los m\u00E9todos que permiten la obtenci\u00F3n del valor de una clave del archivo de propiedades.
 * @author 	bmontesdeoca
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
public class MessagesWebSISPE 
{
	private static final String BUNDLE_NAME = "ec.com.smx.sic.sispe.web.resources.ApplicationResources";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


	/**
	 * Permite la obtenci\u00F3n del valor de la clave del archivo de propiedades ingresada
	 * @param  key							- Clave del archivo de propiedades que se desea obtener
	 * @return								- Valor de la clave ingresada
	 * @throws MissingResourceException
	 */
	public static String getString(String key) throws MissingResourceException{
		return RESOURCE_BUNDLE.getString(key);
	}

}
