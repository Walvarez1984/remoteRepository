/*
 * Clase AutorizacionesForm.java 
 * Creado el 12/05/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.form;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * <p>
 * Esta clase permite el gestionar los datos de las Autorizaciones dentro de la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class AutorizacionesForm extends ActionForm
{
    
    /**
     * Campos del formulario de Autorizaciones
     * <ul>
     * <li><code>String tipoAutorizacion:</code> Campo donde se ingresar\u00E1 el Tipo de Autorizaci\u00F3n.</li>
     * <li><code>String observacionAutorizacion:</code> Campo donde se ingresar\u00E1 la Observaci\u00F3n de la Autorizaci\u00F3n.</li>
     * <li><code>String local:</code> Campo donde se ingresar\u00E1 el local donde se genera la autorizaci\u00F3n.</li>
     * <ul>
     */
    private String tipoAutorizacion;
    private String observacionAutorizacion;
    private String local;
    
    /**
     * Botones utlizados para guardar los cambios ingresados
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al formulario de Autorizaciones.</li>
     * <li><code>String botonDesactivar:</code> Desactiva la autorizaci\u00F3n seleccionada</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonCancelar;
    private String botonDesactivar;
    
    /**
     * Realiza la validaci\u00F3n de la p\u00E1gina <code>nuevaAutorizacion.jsp</code>.
     * @param mapping		El mapeo utilizado para seleccionar esta instancia
     * @param request		El request que estamos procesando
     * @return errors		Los errores recogidos durante la ejecuci\u00F3n
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
        PropertyValidator validar = new PropertyValidatorImpl();
        HttpSession session = request.getSession();
        
        String ayuda = request.getParameter(Globals.AYUDA);
        if(ayuda != null && !ayuda.equals("")) {
            if(ayuda.equals("registrarAutorizacion")) {
                this.botonRegistrarNuevo = ayuda;
            } 
        }
        
        try
        {
            //	cuando se da click el boton Nuevo Registro----------------------------------------------------------------
            if (this.botonRegistrarNuevo != null) {
                //valida los campos de tipo de autorizaci\u00F3n y observaci\u00F3n.
                if(session.getAttribute("ec.com.smx.sic.sispe.entidadLocal")==null){
                  if(this.local.equals("") || this.local.equals("ciudad"))
                    errors.add("local",new ActionMessage("errors.requerido","Local Responsable"));
                }
                validar.validateMandatory(errors, "tipoAutorizacion", this.tipoAutorizacion, "errors.requerido","Tipo autorizaci\u00F3n");
                validar.validateMandatory(errors, "observacionAutorizacion", this.observacionAutorizacion,"errors.requerido", "Observaci\u00F3n");
            }
            
            //Valida la desactivaci\u00F3n de una autorizaci\u00F3n-----------------------------------------------------------------
            else if (this.botonDesactivar != null) {
                //valida que la observaci\u00F3n sea obligatoria
                validar.validateMandatory(errors, "observacionRequerida", this.observacionAutorizacion,
                "errors.observacionDesactivacion.requerido");
            }
            //Valida la selecci\u00F3n de un registro--------------------------------------------------------------------------
            else if (request.getParameter("indice") != null) {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del par\u00E1metros
                ArrayList parametros = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
                //transforma el indice String a int
                int indice = Integer.parseInt(request.getParameter("indice"));
                //comprueba si el indice es correcto dentro del rango
                if (indice >= parametros.size())
                    //el indice del registro referenciado no esta en la colecci\u00F3n
                    errors.add("indice", new ActionMessage("errors.registro.fueraDeRango"));
            }
        }
        catch(Exception ex){
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
        }
        
        return errors;
    }
    
    /**
     * Resetea los controles del formulario de la p\u00E1gina <code>nuevaAutorizacion.jsp</code>, en cada petici\u00F3n.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
        this.tipoAutorizacion = null;
        this.observacionAutorizacion = null;
        this.botonRegistrarNuevo = null;
        this.botonDesactivar = null;
        this.botonCancelar = null;
        this.local=null;
    }
    
    /**
     * @return Devuelve local.
     */
    public String getLocal()
    {
        return local;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(String local)
    {
        this.local = local;
    }
    /**
     * @return Devuelve observacionAutorizacion.
     */
    public String getObservacionAutorizacion()
    {
        return observacionAutorizacion;
    }
    /**
     * @param observacionAutorizacion El observacionAutorizacion a establecer.
     */
    public void setObservacionAutorizacion(String observacionAutorizacion)
    {
        this.observacionAutorizacion = observacionAutorizacion;
    }
    /**
     * @return Devuelve tipoAutorizacion.
     */
    public String getTipoAutorizacion()
    {
        return tipoAutorizacion;
    }
    /**
     * @param tipoAutorizacion El tipoAutorizacion a establecer.
     */
    public void setTipoAutorizacion(String tipoAutorizacion)
    {
        this.tipoAutorizacion = tipoAutorizacion;
    }
    /**
     * @return Devuelve botonRegistrarNuevo.
     */
    public String getBotonRegistrarNuevo()
    {
        return botonRegistrarNuevo;
    }
    /**
     * @param botonRegistrarNuevo El botonRegistrarNuevo a establecer.
     */
    public void setBotonRegistrarNuevo(String botonRegistrarNuevo)
    {
        this.botonRegistrarNuevo = botonRegistrarNuevo;
    }
    /**
     * @return Devuelve botonCancelar.
     */
    public String getBotonCancelar()
    {
        return botonCancelar;
    }
    /**
     * @param botonCancelar El botonCancelar a establecer.
     */
    public void setBotonCancelar(String botonCancelar)
    {
        this.botonCancelar = botonCancelar;
    }
    /**
     * @return Devuelve botonDesactivar.
     */
    public String getBotonDesactivar()
    {
        return botonDesactivar;
    }
    /**
     * @param botonDesactivar El botonDesactivar a establecer.
     */
    public void setBotonDesactivar(String botonDesactivar)
    {
        this.botonDesactivar = botonDesactivar;
    }
}
