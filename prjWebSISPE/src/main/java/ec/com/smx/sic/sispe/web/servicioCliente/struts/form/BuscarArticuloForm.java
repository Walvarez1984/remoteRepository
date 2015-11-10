/*
 * Clase BuscarArticuloForm.java
 * Creado el 20/04/2006
 *
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;


/**
 * <p>
 * Contiene los campos y los demas controles del formulario de b\u00FAsqueda de art\u00EDculos. Tambi\u00E9n se resetea 
 * el formulario cada vez que se realiza una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version 1.0
 * @since 	JSDK 1.4.2
 */ 
@SuppressWarnings("serial")
public class BuscarArticuloForm extends ActionForm
{
  /**
   * Campos para realizar la busqueda:
   * <ul>
   * 	<li><code>String codigoClasificacion: </code>Campo donde se ingresa el n\u00FAmero de clasificaci\u00F3n</li>
   * 	<li><code>String nombreClasificacion: </code>Campo donde se ingresa el nombre de la clasificaci\u00F3n</li>
   * 	<li><code>String nombreArticulo: </code>Campo donde se ingresa el nombre del art\u00EDculo</li>
   * 	<li><code>String opcionBusqueda: </code>Permite escoger solo una de las opciones de busqueda</li>
   * 	<li><code>String botonBuscarProd: </code>Bot\u00F3n que permite realizar la b\u00FAsqueda</li>
   * </ul>
   */
	private String [] checksClasificaciones;
  private String codigoClasificacion;
  private String nombreClasificacion;
  private String nombreArticulo;
  private String opcionBusqueda;
  private String botonBuscarProd;
  
  /**
   * Campos mostrados en el formulario de la lista de art\u00EDculos buscados:
   * <ul>
   * 	<li><code>String opEscogerTodos: </code>Permite escoger todos los art\u00EDculos que se van a a\u00F1adir al detalle 
   * 		del pedido</li>
   * 	<li><code>String [] opEscogerProdBuscados: </code>Arreglo de Checkboxs que permite escoger un subconjunto de art\u00EDculos que
   * 		se van a a\u00F1adir al detalle del pedido</li>
   * 	<li><code>String botonAnadirArt: </code>Bot\u00F3n que permite a\u00F1adir los art\u00EDculos seleccionados</li>
   * 	<li><code>String botonCancelarArt: </code>Bot\u00F3n que cancela la acci\u00F3n</li>
   * 	<li><code>String [] precioEspecial: </code>Arreglo de campos para actualizar el precio especial</li>
   * 	<li><code>String botonActualizarArt: </code>Bot\u00F3n que permite actualizar el precio especial de un art\u00EDculo, este 
   * 		bot\u00F3n es mostrado solo cuando se hace el mantenimento de los art\u00EDculos</li>
   * </ul>
   */
  private String opEscogerTodos;
  private String [] opEscogerProdBuscados;
  private String botonAnadirArt;
  private String botonCancelarArt;
  private String [] precioEspecial;
  private String botonActualizarArt;
  
  /**
   * Par\u00E1metros de paginaci\u00F3n
   * <ul>
   * <li><code>Collection datos: </code>Colecci\u00F3n que almacenar\u00E1 los datos de la tabla consultada.</li>
   * <li><code>String start: </code>Indice para el inicio del paginador</li>
   * <li><code>String range: </code>Valor que indica el n\u00FAmero de registros que se presentar\u00E1n en cada paginaci\u00F3n</li>
   * <li><code>String size: </code>Valor que indica el n\u00FAmero total de registros.</li>
   * </ul>
   */
  private Collection datos;
  private String start;
  private String range;
  private String size;
  
  /**
   * Ejecuta la validacion en la p\u00E1gina JSP <code>listaArticulos.jsp</code>
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		La petici\u00F3n relizada desde el browser.
   * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
   * <code>null</code>
   */
  public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
  {	
    ActionErrors errors = new ActionErrors();
    /*------------------------ cuando se da clic en el boton A\u00F1adir art\u00EDculos ------------------------*/ 
    if(this.botonAnadirArt!=null){
      if(this.opEscogerProdBuscados==null && this.opEscogerTodos==null){
        errors.add("ningunoSeleccionado",new ActionMessage("errors.seleccion.requerido","un Art\u00EDculo"));
      }
    }
    return errors;
  }
  
  /**
   * Resetea los controles del formulario de la p\u00E1gina <code>buscarArticulo.jsp</code>, en cada petici\u00F3n.
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		La petici\u00F3n que se est\u00E1 procesando
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
    LogSISPE.getLog().info("POR RESETEAR");
    this.opcionBusqueda=MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.numeroClasificacion");
    this.checksClasificaciones = null;
    this.codigoClasificacion=null;
    this.nombreClasificacion=null;
    this.nombreArticulo=null;
    this.botonBuscarProd=null;
    
    //campos que se muestran cuando se da clic en botonBuscarProd.
    this.opEscogerProdBuscados=null;
    this.opEscogerTodos=null;
    this.botonAnadirArt=null;
    this.botonCancelarArt=null;
    this.precioEspecial=null;
    this.botonActualizarArt=null;
  }
  
  /**
	 * @return el checksClasificaciones
	 */
	public String[] getChecksClasificaciones() {
		return checksClasificaciones;
	}

	/**
	 * @param checksClasificaciones el checksClasificaciones a establecer
	 */
	public void setChecksClasificaciones(String[] checksClasificaciones) {
		this.checksClasificaciones = checksClasificaciones;
	}

	/**
   * @return Devuelve codigoClasificacion.
   */
  public String getCodigoClasificacion()
  {
    return codigoClasificacion;
  }
  /**
   * @param codigoClasificacion El codigoClasificacion a establecer.
   */
  public void setCodigoClasificacion(String codigoClasificacion)
  {
    this.codigoClasificacion = codigoClasificacion;
  }
  /**
   * @return Devuelve nombreClasificacion.
   */
  public String getNombreClasificacion()
  {
    return nombreClasificacion;
  }
  /**
   * @param nombreClasificacion El nombreClasificacion a establecer.
   */
  public void setNombreClasificacion(String nombreClasificacion)
  {
    this.nombreClasificacion = nombreClasificacion;
  }

  /**
   * @return Devuelve nombreArticulo.
   */
  public String getNombreArticulo()
  {
    return nombreArticulo;
  }
  /**
   * @param nombreArticulo El nombreArticulo a establecer.
   */
  public void setNombreArticulo(String nombreArticulo)
  {
    this.nombreArticulo = nombreArticulo;
  }
  /**
   * @return Devuelve opcionBusqueda.
   */
  public String getOpcionBusqueda()
  {
    return opcionBusqueda;
  }
  /**
   * @param opcionBusqueda El opcionBusqueda a establecer.
   */
  public void setOpcionBusqueda(String opcionBusqueda)
  {
    this.opcionBusqueda = opcionBusqueda;
  }
  /**
   * @return Devuelve botonBuscarProd.
   */
  public String getBotonBuscarProd()
  {
    return botonBuscarProd;
  }
  /**
   * @param botonBuscarProd El botonBuscarProd a establecer.
   */
  public void setBotonBuscarProd(String botonBuscarProd)
  {
    this.botonBuscarProd = botonBuscarProd;
  }
  /**
   * @return Devuelve opEscogerTodos.
   */
  public String getOpEscogerTodos()
  {
    return opEscogerTodos;
  }
  /**
   * @param opEscogerTodos El opEscogerTodos a establecer.
   */
  public void setOpEscogerTodos(String opEscogerTodos)
  {
    this.opEscogerTodos = opEscogerTodos;
  }
  /**
   * @return Devuelve opEscogerProdBuscados.
   */
  public String[] getOpEscogerProdBuscados()
  {
    return opEscogerProdBuscados;
  }
  /**
   * @param opEscogerProdBuscados El opEscogerProdBuscados a establecer.
   */
  public void setOpEscogerProdBuscados(String[] opEscogerProdBuscados)
  {
    this.opEscogerProdBuscados = opEscogerProdBuscados;
  }

  /**
   * @return Devuelve botonAnadirArt.
   */
  public String getBotonAnadirArt()
  {
    return botonAnadirArt;
  }
  /**
   * @param botonAnadirArt El botonAnadirArt a establecer.
   */
  public void setBotonAnadirArt(String botonAnadirArt)
  {
    this.botonAnadirArt = botonAnadirArt;
  }
  /**
   * @return Devuelve botonCancelarArt.
   */
  public String getBotonCancelarArt()
  {
    return botonCancelarArt;
  }
  /**
   * @param botonCancelarArt El botonCancelarArt a establecer.
   */
  public void setBotonCancelarArt(String botonCancelarArt)
  {
    this.botonCancelarArt = botonCancelarArt;
  }
  
  /**
   * @return Devuelve precioEspecial.
   */
  public String[] getPrecioEspecial()
  {
    return precioEspecial;
  }
  /**
   * @param precioEspecial El precioEspecial a establecer.
   */
  public void setPrecioEspecial(String[] precioEspecial)
  {
    this.precioEspecial = precioEspecial;
  }
  /**
   * @return Devuelve botonActualizarArt.
   */
  public String getBotonActualizarArt()
  {
    return botonActualizarArt;
  }
  /**
   * @param botonActualizarArt El botonActualizarArt a establecer.
   */
  public void setBotonActualizarArt(String botonActualizarArt)
  {
    this.botonActualizarArt = botonActualizarArt;
  }
  /**
   * @return Devuelve datos.
   */
  public Collection getDatos()
  {
    return datos;
  }
  /**
   * @param datos El datos a establecer.
   */
  public void setDatos(Collection datos)
  {
    this.datos = datos;
  }
  /**
   * @return Devuelve range.
   */
  public String getRange()
  {
    return range;
  }
  /**
   * @param range El range a establecer.
   */
  public void setRange(String range)
  {
    this.range = range;
  }
  /**
   * @return Devuelve size.
   */
  public String getSize()
  {
    return size;
  }
  /**
   * @param size El size a establecer.
   */
  public void setSize(String size)
  {
    this.size = size;
  }
  /**
   * @return Devuelve start.
   */
  public String getStart()
  {
    return start;
  }
  /**
   * @param start El start a establecer.
   */
  public void setStart(String start)
  {
    this.start = start;
  }
}
