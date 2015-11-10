/*
 * Creado el 13/02/2007
 * 
 * Autor mbraganza
 *
 */
package ec.com.smx.sic.sispe.web.controlesusuario.tab;

/**
 * @author mbraganza
 *
 */
public class Tab {
    
    private String titulo;
    private String nombreActionIr;
    private String nombrePaginaMostrar;
    private int orden;
    private boolean seleccionado;
    private boolean activado;
    
    public Tab(PaginaTab paginaTab, String titulo, String nombreActionIr, String nombrePaginaMostrar, boolean seleccionado) {
        this.titulo = titulo;
        this.nombreActionIr = nombreActionIr;
        this.nombrePaginaMostrar = nombrePaginaMostrar;
        this.seleccionado = seleccionado;
        this.activado = true;
        paginaTab.addTab(this);
    }
    
    public Tab(PaginaTab paginaTab, String titulo, String nombreActionIr, String nombrePaginaMostrar, boolean seleccionado, boolean activado) {
      this.titulo = titulo;
      this.nombreActionIr = nombreActionIr;
      this.nombrePaginaMostrar = nombrePaginaMostrar;
      this.seleccionado = seleccionado;
      this.activado = activado;
      paginaTab.addTab(this);
    }
    
    public Tab(String titulo, String nombreActionIr, String nombrePaginaMostrar, boolean seleccionado) {
        this.titulo = titulo;
        this.nombreActionIr = nombreActionIr;
        this.nombrePaginaMostrar = nombrePaginaMostrar;
        this.seleccionado = seleccionado;
        this.activado = true;
    }
    
    
    public Tab(String titulo, String nombreActionIr, String nombrePaginaMostrar, boolean seleccionado, boolean activado) {
      this.titulo = titulo;
      this.nombreActionIr = nombreActionIr;
      this.nombrePaginaMostrar = nombrePaginaMostrar;
      this.seleccionado = seleccionado;
      this.activado = activado;
  }
    
    public int getOrden() {
        return this.orden;
    }
    
    
    public void setOrden(int orden) {
        this.orden = orden;
    }
    
    
    /**
     * @return Devuelve nombreAction.
     */
    public String getNombreActionIr() {
        return nombreActionIr;
    }
    /**
     * @param nombreAction El nombreAction a establecer.
     */
    public void setNombreActionIr(String nombreActionIr) {
        this.nombreActionIr = nombreActionIr;
    }
    
    /**
     * @return Devuelve nombrePaginaMostrar.
     */
    public String getNombrePaginaMostrar() {
        return nombrePaginaMostrar;
    }
    /**
     * @param nombrePaginaMostrar El nombrePaginaMostrar a establecer.
     */
    public void setNombrePaginaMostrar(String nombrePaginaMostrar) {
        this.nombrePaginaMostrar = nombrePaginaMostrar;
    }
    /**
     * @return Devuelve titulo.
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * @param titulo El titulo a establecer.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * @return Devuelve seleccionado.
     */
    public boolean isSeleccionado() {
        return seleccionado;
    }
    /**
     * @param seleccionado El seleccionado a establecer.
     */
    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

		/**
		 * @return el activado
		 */
		public boolean isActivado() {
			return activado;
		}

		/**
		 * @param activado el activado a establecer
		 */
		public void setActivado(boolean activado) {
			this.activado = activado;
		}
}
