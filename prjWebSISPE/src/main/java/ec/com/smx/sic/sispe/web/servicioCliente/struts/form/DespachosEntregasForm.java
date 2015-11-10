/*
 * Clase DespachosEntregasForm.java
 * Creado el 13/06/2006
 *
 */

package ec.com.smx.sic.sispe.web.servicioCliente.struts.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.common.util.Util;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.despachoEntrega.OrdenDespachoEntregaRDTO;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * <p>
 * Contiene los campos y los demas controles del formulario de Despachos y de Entregas de Pedidos.Tambi\u00E9n 
 * se resetea el formulario cada vez que se realiza una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @version	1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings({"serial","unchecked"})
public class DespachosEntregasForm extends ListadoPedidosForm
{
  /*
   * Controles principales para el formulario de Despachos y Entregas de pedidos:
   * 
   * String opSeleccionAlgunos: Arreglo de String que permite almacenar las entregas o despachos seleccionados en 
   * cada detalle.
   * String vectorPesoArticulo: Arreglo de String que permite modificar el peso de los art\u00EDculos, por detalle.
   * String opSeleccionTodos: CheckBox que permite seleccionar todos los art\u00EDculos.
   * String botonDespachar: Permite despachar los pedidos.
   * String botonEntregar: Permite entregar los pedidos.
   * String botonAceptarSeleccion: Permite aceptar la selecci\u00F3n de los art\u00EDculos a despachar o entregar.
   * String botonCancelarSeleccion: Permite cancelar la selecci\u00F3n de los art\u00EDculos a despachar o entregar.
   * String tabPedidos: Muestra la secci\u00F3n de los pedidos para despachar.
   * String tabImpresion: Muestra la secci\u00F3n para la impresi\u00F3n de las ordenes de despacho.
   */
  private String [] opSeleccionAlgunos;
  private Double [] vectorPesoArticulo;
  private String opSeleccionTodos;
  private String botonDespachar;
  private String botonEntregar;
  private String botonAceptarSeleccion;
  private String botonCancelarSeleccion;
  private String botonArchivoBeneficiario;
  private String tabPedidos;
  private String tabImpresion;
  private String botonImprimir;
  private String botonImprimir2;
  
  //variables para seleccionar las reservas anteriores
  private String opEscogerTodos;
  private String [] opEscogerProdBuscados;
  
  //IOnofre. Arreglo de String que permite almacenar los pedidos seleccionados para la impresion.
  private String [] opEscogerImprimir;
  
  public String[] getOpEscogerImprimir() {
	return opEscogerImprimir;
  }

  public void setOpEscogerImprimir(String[] opEscogerImprimir) {
	this.opEscogerImprimir = opEscogerImprimir;
  }

/**
   * Ejecuta la validacion en la p\u00E1gina JSP <code>despachoPedido.jsp</code>
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		La petici\u00F3n relizada desde el browser.
   * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
   * <code>null</code>
   * @throws Exception	Lanzada en caso de que falle la llamada a la capa de servicio.
   */
  public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
  {
  	ActionErrors errors = new ActionErrors();
 	
    //esta variable permite que se muestre o no una ventana con los datos que van a imprimirse
    //[1: se muestra, 0: no se muestra]
    request.getSession().setAttribute("ec.com.smx.sic.sispe.despachoEntrega.imprimir","0");
    
  	try{
  		LogSISPE.getLog().info("buscar: {}" , request.getParameter("buscar"));
  		errors = super.validate(mapping, request);
  		LogSISPE.getLog().info("errores: {}",errors.size());
  		
  	}catch(Exception ex){
  	  LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
  	}
  	return errors;
  }
  
  /**
   * Resetea los controles del formulario de las p\u00E1ginas <code>despachoPedido.jsp</code> y 
   * <code>entregaPedido.jsp</code>, en cada petici\u00F3n.
   * @param mapping		El mapeo utilizado para seleccionar esta instancia
   * @param request		La petici\u00F3n que se est\u00E1 procesando
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
	  //llamada al reset de la clase padre
	  super.reset(mapping, request);
		this.botonDespachar=null;
		this.botonEntregar=null;
		this.opSeleccionAlgunos=null;
		this.vectorPesoArticulo = null;
		this.opSeleccionTodos=null;
		this.botonAceptarSeleccion=null;
		this.botonCancelarSeleccion=null;
		this.botonImprimir=null;
		this.tabPedidos = null;
		this.tabImpresion = null;
		this.botonImprimir2=null;
		this.botonArchivoBeneficiario=null;
		
		this.opEscogerProdBuscados=null;
		this.opEscogerTodos=null;
  }
  
  /**
   * Construye el reporte de los despachos y las entregas, almacendolo en una variable de sesi\u00F3n
   * 
   * @param pedidos							- Collecci\u00F3n de pedidos despachados o entregados
   * @param inicioPaginacion		- Variable que contiene el indice de inicio de paginaci\u00F3n actual 
   * @param request							- Objeto que maneja la petici\u00F3n HTTP
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void construirReporteDespachoEntrega(Collection pedidos, String inicioPaginacion, 
  		HttpServletRequest request)throws Exception
  {
  	HttpSession session = request.getSession();
  	String accion = (String)session.getAttribute("ec.com.smx.sic.sispe.accion");
  	
    //se inicializan las variables 
    int start=0;
    int size= pedidos.size();
    int range= Integer.parseInt(MessagesWebSISPE.getString("parametro.paginacion.rango"));
    if(inicioPaginacion!=null)
      start = Integer.parseInt(inicioPaginacion);
    
    //se obtiene la subcolecci\u00F3n es decir la p\u00E1gina
    Collection<VistaPedidoDTO> pedidosPorPagina = Util.obtenerSubCollection(pedidos,start, start + range > size ? size : start+range);
    
    //colecci\u00F3n que contendr\u00E1 las colecciones de arreglos para el reporte.
    Collection datosReporte = new ArrayList();
    //colecci\u00F3n que almacenar\u00E1 los objetos archivoDTO para el reporte.
    Collection reportes = new ArrayList();
    //se crea la colecci\u00F3n que almacenar\u00E1 los pedidos seleccionados
    Collection <VistaPedidoDTO> pedidosSeleccionados = new ArrayList <VistaPedidoDTO>();
    
    //se obtienen los indices de los pedidos seleccionados
    //String [] indiceSeleccionados = this.getOpSeleccionAlgunos();
    LogSISPE.getLog().info("ITERANDO LA COLECCION DE PEDIDOS");
    //barrido de indices de pedidos escogidos
    for(VistaPedidoDTO vistaPedidoDTO: pedidosPorPagina){
    	vistaPedidoDTO.setNpCodigoUsuarioAFiltrar(SessionManagerSISPE.getCurrentEntidadResponsable(request).getId().getIdUsuario());
    	pedidosSeleccionados.add(vistaPedidoDTO);
    }

    //se determina el m\u00E9todo apropiado
    if(accion!=null && accion.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.accion.despacho"))){
    	datosReporte = SessionManagerSISPE.getServicioClienteServicio()
    		.transObtenerPedidosParaImprimirDespachos(pedidosSeleccionados, SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request));
    }else{
      datosReporte = SessionManagerSISPE.getServicioClienteServicio()
    		.transObtenerPedidosParaImprimirEntregas(pedidosSeleccionados, SessionManagerSISPE.getCurrentEntidadResponsable(request),
    				SessionManagerSISPE.getColeccionVistaEstablecimientoCiudadLocalDTO(request));
    }
    
    LogSISPE.getLog().info("pedidosPorPagina: {}",pedidosPorPagina.size());
    LogSISPE.getLog().info("datosReporte: {}",datosReporte);
    LogSISPE.getLog().info("size datosReporte: {}",datosReporte.size());
    //generar reporte en una p\u00E1gina JSP con los datos de pedidos seleccionados
    for(Iterator it=datosReporte.iterator();it.hasNext();){
      OrdenDespachoEntregaRDTO ordenDespachoEntregaRDTO = WebSISPEUtil.construirReporteDespachoEntrega((Collection)it.next());
      if(ordenDespachoEntregaRDTO!=null)
        reportes.add(ordenDespachoEntregaRDTO);
    }
    //se almacena la colecci\u00F3n en sesi\u00F3n
    session.setAttribute("ec.com.smx.sic.sispe.DespachosEntregas.reporte",reportes);
    
    //esta variable permite que se muestre una ventana con los datos que van a imprimirse
    //[1: se muestra, 0: no se muestra]
    session.setAttribute("ec.com.smx.sic.sispe.despachoEntrega.imprimir","1");
    request.getSession().setAttribute("ec.com.smx.sic.sispe.Despacho.imprimir","0");
  }
  
  /**
   * @return Devuelve opSeleccionAlgunos.
   */
  public String[] getOpSeleccionAlgunos()
  {
    return opSeleccionAlgunos;
  }
  /**
   * @param opSeleccionAlgunos El opSeleccionAlgunos a establecer.
   */
  public void setOpSeleccionAlgunos(String[] opSeleccionAlgunos)
  {
    this.opSeleccionAlgunos = opSeleccionAlgunos;
  }

	/**
	 * @return el vectorPesoArticulo
	 */
	public Double[] getVectorPesoArticulo() {
		return vectorPesoArticulo;
	}

	/**
	 * @param vectorPesoArticulo el vectorPesoArticulo a establecer
	 */
	public void setVectorPesoArticulo(Double[] vectorPesoArticulo) {
		this.vectorPesoArticulo = vectorPesoArticulo;
	}

	/**
   * @return Devuelve opSeleccionTodos.
   */
  public String getOpSeleccionTodos()
  {
    return opSeleccionTodos;
  }
  /**
   * @param opSeleccionTodos El opSeleccionTodos a establecer.
   */
  public void setOpSeleccionTodos(String opSeleccionTodos)
  {
    this.opSeleccionTodos = opSeleccionTodos;
  }

 
  /**
   * @return Devuelve botonDespachar.
   */
  public String getBotonDespachar()
  {
    return botonDespachar;
  }
  
  /**
   * @param botonDespachar El botonDespachar a establecer.
   */
  public void setBotonDespachar(String botonDespachar)
  {
    this.botonDespachar = botonDespachar;
  }
  
  /**
   * @return Devuelve botonEntregar.
   */
  public String getBotonEntregar()
  {
    return botonEntregar;
  }
  /**
   * @param botonEntregar El botonEntregar a establecer.
   */
  public void setBotonEntregar(String botonEntregar)
  {
    this.botonEntregar = botonEntregar;
  }
  /**
   * @return Devuelve botonAceptarSeleccion.
   */
  public String getBotonAceptarSeleccion()
  {
    return botonAceptarSeleccion;
  }
  /**
   * @param botonAceptarSeleccion El botonAceptarSeleccion a establecer.
   */
  public void setBotonAceptarSeleccion(String botonAceptarSeleccion)
  {
    this.botonAceptarSeleccion = botonAceptarSeleccion;
  }
  /**
   * @return Devuelve botonCancelarSeleccion.
   */
  public String getBotonCancelarSeleccion()
  {
    return botonCancelarSeleccion;
  }
  /**
   * @param botonCancelarSeleccion El botonCancelarSeleccion a establecer.
   */
  public void setBotonCancelarSeleccion(String botonCancelarSeleccion)
  {
    this.botonCancelarSeleccion = botonCancelarSeleccion;
  }

  /**
   * @return Devuelve tabImpresion.
   */
  public String getTabImpresion()
  {
    return tabImpresion;
  }
  /**
   * @param tabImpresion El tabImpresion a establecer.
   */
  public void setTabImpresion(String tabImpresion)
  {
    this.tabImpresion = tabImpresion;
  }
  /**
   * @return Devuelve tabPedidos.
   */
  public String getTabPedidos()
  {
    return tabPedidos;
  }
  /**
   * @param tabPedidos El tabPedidos a establecer.
   */
  public void setTabPedidos(String tabPedidos)
  {
    this.tabPedidos = tabPedidos;
  }

  /**
   * @return Devuelve botonImprimir.
   */
  public String getBotonImprimir() {

    return botonImprimir;
  }
  /**
   * @param botonImprimir El botonImprimir a establecer.
   */
  public void setBotonImprimir(String botonImprimir) {

    this.botonImprimir = botonImprimir;
  }

  public String getBotonImprimir2() {
		return botonImprimir2;
  }
	
  public void setBotonImprimir2(String botonImprimir2) {
		this.botonImprimir2 = botonImprimir2;
  }

/**
 * @return el botonArchivoBeneficiario
 */
public String getBotonArchivoBeneficiario() {
	return botonArchivoBeneficiario;
}

/**
 * @param botonArchivoBeneficiario el botonArchivoBeneficiario a establecer
 */
public void setBotonArchivoBeneficiario(String botonArchivoBeneficiario) {
	this.botonArchivoBeneficiario = botonArchivoBeneficiario;
}

public String getOpEscogerTodos() {
	return opEscogerTodos;
}

public void setOpEscogerTodos(String opEscogerTodos) {
	this.opEscogerTodos = opEscogerTodos;
}

public String[] getOpEscogerProdBuscados() {
	return opEscogerProdBuscados;
}

public void setOpEscogerProdBuscados(String[] opEscogerProdBuscados) {
	this.opEscogerProdBuscados = opEscogerProdBuscados;
}
  
	


}
