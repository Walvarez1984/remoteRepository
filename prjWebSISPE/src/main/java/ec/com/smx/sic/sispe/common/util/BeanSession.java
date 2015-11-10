/*
 * BeanSesion.java
 * Creado el 28/09/2007 9:52:56
 *   	
 */
package ec.com.smx.sic.sispe.common.util;

import java.io.Serializable;
import java.sql.Timestamp;

import ec.com.smx.sic.sispe.web.controlesusuario.tab.PaginaTab;

/**
 * @author 	fmunoz
 * @since		JSDK 1.5
 */
@SuppressWarnings("serial")
public class BeanSession implements Serializable
{
	//VARIBALES UTILIZADAS EN LOS FORMULARIOS DE B\u00FASQUEDA
  private Integer codigoLocal = null;
  private String codigoPedido = null;
  private String codigoReservacion = null;
  private String codigoArticulo = null;
  private String codigoClasificacion = null;
  private String descripcionArticulo = null;
  private String cedulaContacto = null;
  private String nombreContacto = null;
  private String nombreEmpresa = null;
  private String rucEmpresa=null;
  private String codigoEstado = null;
  private String codigoEstadoAnterior = null;
  private Timestamp fechaInicial = null;
  private Timestamp fechaFinal = null;
  private String opBusquedaFecha = null;
  private String opBusquedaCampo = null;
  private String opEstadoActivo = null;
  private String estadoPedido = null;
  private String campoBusqueda = null;
  private String estadoActual=null;
  private String usuarioLogeado=null;
  private String reservarBodegaSIC=null;
  private String[][] camposOrden=null;
  private String ordenCompra=null;
  private String codigoUsuarioAFiltrar=null;
  private String tipoReporte=null;
  private String noObtenerEntregas=null;
  private String numeroOrdenCompra=null;
  
  private PaginaTab paginaTab = null;
  private PaginaTab paginaTabPopUp = null;
  private String inicioPaginacion = null;
  private String indiceRegistro = null;
  private String opEntidadResonsable = null;
  
  //VARIABLES PARA EL MANEJO DEL CANASTO
  private Double precioCanastoInicial = new Double(0);
  
	/**
	 * @return el campoBusqueda
	 */
	public String getCampoBusqueda() {
		return campoBusqueda;
	}
	/**
	 * @param campoBusqueda el campoBusqueda a establecer
	 */
	public void setCampoBusqueda(String campoBusqueda) {
		this.campoBusqueda = campoBusqueda;
	}
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
	 * @return el codigoEstado
	 */
	public String getCodigoEstado() {
		return codigoEstado;
	}
	/**
	 * @param codigoEstado el codigoEstado a establecer
	 */
	public void setCodigoEstado(String codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	/**
	 * @return el codigoEstadoAnterior
	 */
	public String getCodigoEstadoAnterior() {
		return codigoEstadoAnterior;
	}
	/**
	 * @param codigoEstadoAnterior el codigoEstadoAnterior a establecer
	 */
	public void setCodigoEstadoAnterior(String codigoEstadoAnterior) {
		this.codigoEstadoAnterior = codigoEstadoAnterior;
	}
	/**
	 * @return el codigoLocal
	 */
	public Integer getCodigoLocal() {
		return codigoLocal;
	}
	/**
	 * @param codigoLocal el codigoLocal a establecer
	 */
	public void setCodigoLocal(Integer codigoLocal) {
		this.codigoLocal = codigoLocal;
	}
	/**
	 * @return el codigoPedido
	 */
	public String getCodigoPedido() {
		return codigoPedido;
	}
	/**
	 * @param codigoPedido el codigoPedido a establecer
	 */
	public void setCodigoPedido(String codigoPedido) {
		this.codigoPedido = codigoPedido;
	}
	
	/**
	 * @return el codigoReservacion
	 */
	public String getCodigoReservacion() {
		return codigoReservacion;
	}
	/**
	 * @param codigoReservacion el codigoReservacion a establecer
	 */
	public void setCodigoReservacion(String codigoReservacion) {
		this.codigoReservacion = codigoReservacion;
	}
	
	/**
	 * @return el codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo el codigoArticulo a establecer
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return el fechaFinal
	 */
	public Timestamp getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal el fechaFinal a establecer
	 */
	public void setFechaFinal(Timestamp fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return el fechaInicial
	 */
	public Timestamp getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial el fechaInicial a establecer
	 */
	public void setFechaInicial(Timestamp fechaInicial) {
		this.fechaInicial = fechaInicial;
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
	 * @return el opBusquedaCampo
	 */
	public String getOpBusquedaCampo() {
		return opBusquedaCampo;
	}
	/**
	 * @param opBusquedaCampo el opBusquedaCampo a establecer
	 */
	public void setOpBusquedaCampo(String opBusquedaCampo) {
		this.opBusquedaCampo = opBusquedaCampo;
	}
	/**
	 * @return el opBusquedaFecha
	 */
	public String getOpBusquedaFecha() {
		return opBusquedaFecha;
	}
	/**
	 * @param opBusquedaFecha el opBusquedaFecha a establecer
	 */
	public void setOpBusquedaFecha(String opBusquedaFecha) {
		this.opBusquedaFecha = opBusquedaFecha;
	}
	/**
	 * @return el paginaTab
	 */
	public PaginaTab getPaginaTab() {
		return paginaTab;
	}
	/**
	 * @param paginaTab el paginaTab a establecer
	 */
	public void setPaginaTab(PaginaTab paginaTab) {
		this.paginaTab = paginaTab;
	}
	/**
	 * @return el inicioPaginacion
	 */
	public String getInicioPaginacion() {
		return inicioPaginacion;
	}
	/**
	 * @param inicioPaginacion el inicioPaginacion a establecer
	 */
	public void setInicioPaginacion(String inicioPaginacion) {
		this.inicioPaginacion = inicioPaginacion;
	}

	/**
	 * @return el indiceRegistro
	 */
	public String getIndiceRegistro() {
		return indiceRegistro;
	}
	/**
	 * @param indiceRegistro el indiceRegistro a establecer
	 */
	public void setIndiceRegistro(String indiceRegistro) {
		this.indiceRegistro = indiceRegistro;
	}

	/**
	 * @return el precioCanastoInicial
	 */
	public Double getPrecioCanastoInicial() {
		return precioCanastoInicial;
	}
	/**
	 * @param precioCanastoInicial el precioCanastoInicial a establecer
	 */
	public void setPrecioCanastoInicial(Double precioCanastoInicial) {
		this.precioCanastoInicial = precioCanastoInicial;
	}
	/**
	 * @return el codigoClasificacion
	 */
	public String getCodigoClasificacion() {
		return codigoClasificacion;
	}
	/**
	 * @param codigoClasificacion el codigoClasificacion a establecer
	 */
	public void setCodigoClasificacion(String codigoClasificacion) {
		this.codigoClasificacion = codigoClasificacion;
	}
	/**
	 * @return el descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	/**
	 * @param descripcionArticulo el descripcionArticulo a establecer
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	public String getRucEmpresa() {
		return rucEmpresa;
	}
	public void setRucEmpresa(String rucEmpresa) {
		this.rucEmpresa = rucEmpresa;
	}
	public String getEstadoActual() {
		return estadoActual;
	}
	public void setEstadoActual(String estadoActual) {
		this.estadoActual = estadoActual;
	}
	public String getUsuarioLogeado() {
		return usuarioLogeado;
	}
	public void setUsuarioLogeado(String usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}
	public String getReservarBodegaSIC() {
		return reservarBodegaSIC;
	}
	public void setReservarBodegaSIC(String reservarBodegaSIC) {
		this.reservarBodegaSIC = reservarBodegaSIC;
	}
	public String[][] getCamposOrden() {
		return camposOrden;
	}
	public void setCamposOrden(String[][] camposOrden) {
		this.camposOrden = camposOrden;
	}
	public String getOrdenCompra() {
		return ordenCompra;
	}
	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}
	public String getCodigoUsuarioAFiltrar() {
		return codigoUsuarioAFiltrar;
	}
	public void setCodigoUsuarioAFiltrar(String codigoUsuarioAFiltrar) {
		this.codigoUsuarioAFiltrar = codigoUsuarioAFiltrar;
	}
	public String getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	public String getNoObtenerEntregas() {
		return noObtenerEntregas;
	}
	public void setNoObtenerEntregas(String noObtenerEntregas) {
		this.noObtenerEntregas = noObtenerEntregas;
	}
	public String getNumeroOrdenCompra() {
		return numeroOrdenCompra;
	}
	public void setNumeroOrdenCompra(String numeroOrdenCompra) {
		this.numeroOrdenCompra = numeroOrdenCompra;
	}
	public String getOpEntidadResonsable() {
		return opEntidadResonsable;
	}
	public void setOpEntidadResonsable(String opEntidadResonsable) {
		this.opEntidadResonsable = opEntidadResonsable;
	}
	public String getEstadoPedido() {
		return estadoPedido;
	}
	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	public String getOpEstadoActivo() {
		return opEstadoActivo;
	}
	public void setOpEstadoActivo(String opEstadoActivo) {
		this.opEstadoActivo = opEstadoActivo;
	}
	public PaginaTab getPaginaTabPopUp() {
		return paginaTabPopUp;
	}
	public void setPaginaTabPopUp(PaginaTab paginaTabPopUp) {
		this.paginaTabPopUp = paginaTabPopUp;
	}
	
}