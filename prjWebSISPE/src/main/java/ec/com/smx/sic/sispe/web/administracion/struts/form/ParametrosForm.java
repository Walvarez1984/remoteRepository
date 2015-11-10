/*
 * Clase ParametrosForm.java
 * Creado el 12/05/2006
 *
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
 * Contiene los campos y los demas controles de los formularios para el mantenimiento de Par\u00E1metros aqu\u00ED se realizan las 
 * validaciones de los datos ingresados por el usuario. Tambi\u00E9n se resetean los formulario cada vez que se realiza 
 * una petici\u00F3n.
 * </p>
 * @author 	fmunoz
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class ParametrosForm extends ActionForm
{
    /**
     * Campos del formulario de Par\u00E1metros Generales
     * <ul> 
     * <li><code>String codigoParametro:</code> Campo donde se ingresar\u00E1 el c\u00F3digo del Par\u00E1metro.</li>
     * <li><code>String descripcionParametro:</code> Campo donde se ingresar\u00E1 la descripci\u00F3n del Par\u00E1metro.</li>
     * <li><code>String valorParametro:</code> Campo donde se ingresar\u00E1 el valor del Par\u00E1metro.</li>
     * <ul>
     */
    private String descripcionParametro;
    private String valorParametro;
    
    /**
     * Botones utlizados para guardar los cambios ingresados
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al formulario de Par\u00E1metros Generales.</li>
     * <li><code>String botonActualizar:</code> Actulizara los registros de los Par\u00E1metros</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonActualizar;
    private String botonCancelar;
    
    /**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevoParametro.jsp</code> y 
     * <code>actualizarParametro.jsp</code>.
     * @param mapping		El mapeo utilizado para seleccionar esta instancia
     * @param request		El request que estamos procesando
     * @return errors		Los errores recogidos durante la ejecuci\u00F3n
     */
    public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        PropertyValidator validar = new PropertyValidatorImpl();
        HttpSession session = request.getSession();
        
        String ayuda = request.getParameter(Globals.AYUDA);
        if(ayuda != null && !ayuda.equals("")) {
            if(ayuda.equals("registrarParametro")) {
                this.botonActualizar = ayuda;
            }
        }
        
        
        try
        {
            //cuando se da click el boton Registrar Nuevo Registro
            if(this.botonRegistrarNuevo != null || this.botonActualizar != null)
            {
                //valida el campo del c\u00F3digo del Par\u00E1metro
                validar.validateMandatory(errors,"descripcionParametro",this.descripcionParametro,"errors.requerido","Descripci\u00F3n");
                validar.validateMandatory(errors,"valorParametro",this.valorParametro,"errors.requerido","Valor");
            }
            else if(request.getParameter("indice")!=null)
            {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
                ArrayList parametros = (ArrayList)session.getAttribute("ec.com.smx.sic.sispe.tabla");
                
                int indice = Integer.parseInt(request.getParameter("indice"));
                if(indice >= parametros.size())
                    //el indice del registro referenciado no esta en la colecci\u00F3n
                    errors.add("indice",new ActionMessage("errors.registro.fueraDeRango"));
            }
        }
        catch(Exception ex){
            LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
        }
        return errors;
    }
    
    /**
     * Resetea los controles del formulario de las p\u00E1ginas
     * <code>nuevoParametro.jsp</code> y <code>actualizarParametro.jsp</code>.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        this.descripcionParametro=null;
        this.valorParametro=null;
        
        this.botonRegistrarNuevo=null;
        this.botonActualizar=null;
        this.botonCancelar=null;
    }
    
    /**
     * @return Devuelve descripcionParametro.
     */
    public String getDescripcionParametro()
    {
        return descripcionParametro;
    }
    /**
     * @param descripcionParametro El descripcionParametro a establecer.
     */
    public void setDescripcionParametro(String descripcionParametro)
    {
        this.descripcionParametro = descripcionParametro;
    }
    /**
     * @return Devuelve valorParametro.
     */
    public String getValorParametro()
    {
        return valorParametro;
    }
    /**
     * @param valorParametro El valorParametro a establecer.
     */
    public void setValorParametro(String valorParametro)
    {
        this.valorParametro = valorParametro;
    }
    
    /**
     * @return Devuelve botonActualizar.
     */
    public String getBotonActualizar()
    {
        return botonActualizar;
    }
    /**
     * @param botonActualizar El botonActualizar a establecer.
     */
    public void setBotonActualizar(String botonActualizar)
    {
        this.botonActualizar = botonActualizar;
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
}
