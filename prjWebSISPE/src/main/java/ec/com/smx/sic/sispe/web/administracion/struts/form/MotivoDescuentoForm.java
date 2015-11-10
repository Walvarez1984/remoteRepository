/*
 * MotivoDescuentoForm.java 
 * Creado el 22/06/2006
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
 * Esta clase permite el gestionar los datos de Motivo de descuentos dentro de la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class MotivoDescuentoForm extends ActionForm
{
    
    /**
     * Campos del formulario de Motivo de descuentos
     * <ul>
     * <li><code>String estadoMotivoDescuento:</code> Registra el estado del motivo de descuento
     * <li><code>String descripcionMotivoDescuento:</code> Registra la descripci\u00F3n del motivo de descuento
     * </ul>
     */
    
    private String estadoMotivoDescuento;
    private String descripcionMotivoDescuento;
    
    /**
     * Botones utlizados para guardar los cambios ingresados
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al listado Tipo de descuentos.</li>
     * <li><code>String botonActualizar:</code> Actualizar\u00E1 el registro seleccionado de tipo de descuento</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonCancelar;
    private String botonActualizar;
    
    /**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevoMotivoDescuento.jsp</code> y 
     * <code>actualizarMotivoDescuento.jsp</code>.
     * @param mapping		El mapeo utilizado para seleccionar esta instancia
     * @param request		El request que estamos procesando
     * @return errors		Los errores recogidos durante la ejecuci\u00F3n
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        PropertyValidator validar = new PropertyValidatorImpl();
        HttpSession session = request.getSession();
        
        //SE OBTIENE EL VALOR DEL BOTON QUE SE PRESION\u00F3
        /*String ayuda= request.getParameter(Globals.AYUDA);
         if(ayuda!=null)
         {
         LogSISPE.debug("SE PRESIONO UN BOTON");
         if(ayuda.equals("RN"))
         this.botonRegistrarNuevo=ayuda;
         if(ayuda.equals("AC"))
         this.botonActualizar=ayuda;
         }*/
        
        String ayuda = request.getParameter(Globals.AYUDA);
        if(ayuda != null && !ayuda.equals("")) {
            if(ayuda.equals("actualizarMotivoDescuento")) {
                this.botonActualizar = ayuda;
            } 
            else if(ayuda.equals("registrarMotivoDescuento")) {
                this.botonRegistrarNuevo = ayuda;
            }
        }
        
        
        try
        {
            //  cuando se da click el boton Nuevo Registro----------------------------------------------------------------
            if (this.botonRegistrarNuevo != null) {
                validar.validateMandatory(errors, "descripcionTipoDescuento", this.descripcionMotivoDescuento,
                        "errors.requerido", "Descripci\u00F3n");
            }
            else if (this.botonActualizar != null) {
                validar.validateMandatory(errors, "estadoMotivoDescuento", this.estadoMotivoDescuento, "errors.requerido",
                "Estado Motivo Descuento");
            }
            //si se escogi\u00F3 un registro-----------------------------------------------------------------------------------
            else if (request.getParameter("indice") != null) {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
                ArrayList motivoDescuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
                
                int indice = Integer.parseInt(request.getParameter("indice"));
                if (indice >= motivoDescuentos.size())
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
     * Resetea los controles del formulario de las p\u00E1ginas
     * <code>nuevoMotivoDescuento.jsp</code> y <code>actualizarMotivoDescuento.jsp</code>.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
        botonRegistrarNuevo = null;
        botonCancelar = null;
        botonActualizar = null;
        descripcionMotivoDescuento = null;
        estadoMotivoDescuento = null;
    }
    
    /**
     * @return Devuelve descripcionMotivoDescuento.
     */
    public String getDescripcionMotivoDescuento()
    {
        return descripcionMotivoDescuento;
    }
    /**
     * @param descripcionMotivoDescuento El descripcionMotivoDescuento a establecer.
     */
    public void setDescripcionMotivoDescuento(String descripcionMotivoDescuento)
    {
        this.descripcionMotivoDescuento = descripcionMotivoDescuento;
    }
    /**
     * @return Devuelve estadoMotivoDescuento.
     */
    public String getEstadoMotivoDescuento()
    {
        return estadoMotivoDescuento;
    }
    /**
     * @param estadoMotivoDescuento El estadoMotivoDescuento a establecer.
     */
    public void setEstadoMotivoDescuento(String estadoMotivoDescuento)
    {
        this.estadoMotivoDescuento = estadoMotivoDescuento;
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
