/*
 * Creado el 13/02/2007
 * 
 * Autor mbraganza
 *
 */
package ec.com.smx.sic.sispe.web.controlesusuario.tab;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import ec.com.smx.sic.sispe.commons.util.ConstantesGenerales;

/**
 * @author mbraganza
 *
 */
public class PaginaTab {
    
    public static final String PARAMETRO_SESSION = "ec.com.smx.sic.controlesusuario.tab";
    public static final String PARAMETRO_REQUEST = "rTab";
    
    private String parametroSession;
    private String parametroRequest;
    private int altoTab;
    private int cantidadTabs;
    private int tabSeleccionado;
    private String forwardPrincipal;
    private String tituloTabSeleccionado;
    private String nombreActionPadre;
    private ArrayList tabs;
    
    
    public PaginaTab (String nombreActionPadre, String forwardPrincipal, int tabSeleccionado, int altoTab, HttpServletRequest request) {
        this.nombreActionPadre = nombreActionPadre;
        this.tabSeleccionado = tabSeleccionado;
        this.tituloTabSeleccionado = "";
        this.forwardPrincipal = forwardPrincipal;
        this.altoTab = altoTab;
        this.tabs = new ArrayList();
        this.cantidadTabs = 0;
        
        
        if (request.getAttribute(ConstantesGenerales.PARAMETRO_SESSION_VAR) == null) {
        	this.parametroSession = "ec.com.smx.sic.controlesusuario.tab";
        } else {
        	this.parametroSession = (String) request.getAttribute("parametroSessionVar");
        }
        
        
        if (request.getAttribute(ConstantesGenerales.PARAMETRO_REQUEST_VAR) == null) {
        	 this.parametroRequest = "rTab";
        } else {
        	 this.parametroRequest = (String) request.getAttribute("parametroRequestVar");
        }
        
        
//        this.parametroRequest = PARAMETRO_REQUEST;
//        this.parametroSession = PARAMETRO_SESSION;
        request.getSession().setAttribute(this.parametroSession, this);
    }
    
    public PaginaTab(int cantidadTabs, String nombreActionPadre) {
        this.cantidadTabs = cantidadTabs;
        this.nombreActionPadre = nombreActionPadre;
        this.tabs = new ArrayList(this.cantidadTabs);
    }
    
    @SuppressWarnings("unchecked")
    public PaginaTab(String nombreActionPadre, ArrayList tabs) {
        this.nombreActionPadre = nombreActionPadre;
        this.tabs = new ArrayList(tabs);
    }
    
    /*public void iniciarTabs(HttpServletRequest request) {
        request.getSession().setAttribute(this.parametroSesion, this);
    }*/
    
    
    private void cambiarTabSeleccionado(HttpServletRequest request) {
        if (request.getParameter(this.parametroRequest) != null) {
          this.cambiarTabSeleccionado(Integer.parseInt(request.getParameter(this.parametroRequest)));
  		}
    }
    
    public void cambiarTabSeleccionado(int tabSeleccionado) {
        if (this.tabSeleccionado != tabSeleccionado) {//Comprobaci\u00F3n para cambio manual
            this.getTab(this.tabSeleccionado).setSeleccionado(false);
    		   this.setTabSeleccionado(tabSeleccionado);
    		   this.getTab(tabSeleccionado).setSeleccionado(true);
    		   this.tituloTabSeleccionado = this.getTab(tabSeleccionado).getTitulo();
        }
    }
    
    private int getIndiceTabByTituloTab(String tituloTab) {
        for(Iterator i = this.tabs.iterator(); i.hasNext();) {
            Tab tab = (Tab) i.next();
            if (tab.getTitulo().equals(tituloTab)) {
                return tab.getOrden();
            }
        }
        
        return -1;
    }
    
    public void cambiarTabSeleccionado(String tituloTabSeleccionado) {
        if (!this.tituloTabSeleccionado.equals(tituloTabSeleccionado)) { //Comprobaci\u00F3n para cambio manual
            int tabSeleccionado = this.getIndiceTabByTituloTab(tituloTabSeleccionado);
            if (tabSeleccionado != -1) {
                this.cambiarTabSeleccionado(tabSeleccionado);
            }
        }
    }
    
    public void iniciarTabs(HttpServletRequest request) {
        /*if (request.getSession().getAttribute(this.PARAMETRO_SESSION) != null) {
            return true;
        }*/
        //this.iniciarTabs(request);
        request.getSession().setAttribute(PARAMETRO_SESSION, this);
        //return false;
    }
    
    public boolean comprobarSeleccionTab(HttpServletRequest request) {
        if (request.getParameter(this.parametroRequest) != null) {
            this.cambiarTabSeleccionado(request);
            return true;
        }
        return false;
    }
    
    public boolean esTabSeleccionado(int tabSeleccionado) {
        if (this.tabSeleccionado == tabSeleccionado) {
            return true;
        }
        return false;
    }
    
    public boolean esTabSeleccionado(String tituloTabSeleccionado) {
        
        if (this.tituloTabSeleccionado.equals(tituloTabSeleccionado)) {
            return true;
        }
        
        return false;
    }
    
    
    public String getTituloTabSeleccionado() {
        return this.tituloTabSeleccionado;
    }
    
    public void setTituloTabSeleccionado(String tituloTabSeleccionado) {
        this.tituloTabSeleccionado = tituloTabSeleccionado;
    }
    /**
     * @return Devuelve tabSeleccionado.
     */
    public int getTabSeleccionado() {
        return tabSeleccionado;
    }
    /**
     * @param tabSeleccionado El tabSeleccionado a establecer.
     */
    public void setTabSeleccionado(int tabSeleccionado) {
        this.tabSeleccionado = tabSeleccionado;
    }
    /**
     * @return Devuelve cantidadTabs.
     */
    public int getCantidadTabs() {
        return cantidadTabs;
    }
    /**
     * @param cantidadTabs El cantidadTabs a establecer.
     */
    public void setCantidadTabs(int cantidadTabs) {
        this.cantidadTabs = cantidadTabs;
    }
    /**
     * @return Devuelve nombreActionPadre.
     */
    public String getNombreActionPadre() {
        return nombreActionPadre;
    }
    /**
     * @param nombreActionPadre El nombreActionPadre a establecer.
     */
    public void setNombreActionPadre(String nombreActionPadre) {
        this.nombreActionPadre = nombreActionPadre;
    }
    /**
     * @return Devuelve tabs.
     */
    public ArrayList getTabs() {
        return tabs;
    }
    /**
     * @param tabs El tabs a establecer.
     */
    @SuppressWarnings("unchecked")
	public void setTabs(ArrayList tabs) {
        this.tabs = new ArrayList(tabs);
    }
    
    public Tab getTab(int indice) {
        if (indice >= 0 && indice < this.tabs.size()) {
            return (Tab) this.tabs.get(indice);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public void addTab(Tab tab) {
        tab.setOrden(cantidadTabs);
        this.tabs.add(tab);
        this.cantidadTabs++;
    }
    
    
    
    public String getForwardPrincipal() {
        return this.forwardPrincipal;
    }
    
    public void setForwardPrincipal(String forwardPrincipal) {
        this.forwardPrincipal = forwardPrincipal;
    }
    
    
    public int getAltoTab() {
        return this.altoTab;
    }
    public void setAltoTab(int altoTab) {
        this.altoTab = altoTab;
    }
    
    
    public String getParametroRequest() {
        return this.parametroRequest;
    }
    public String getParametroSession() {
        return this.parametroSession;
    }
}
