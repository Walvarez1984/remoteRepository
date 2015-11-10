/*
 * TipoDescuentoForm.java 
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
 * Esta clase permite el gestionar los datos del tipo de descuento dentro de la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class TipoDescuentoForm extends ActionForm
{
    /**
     * Campos del formulario de Tipo de descuentos
     * <ul>
     * <li><code>String codigoClasificacionDescuento:</code> C&oacute;digo de la clasificaci&oacute;n a a\u00F1adir 
     * <li><code>String estadoTipoDescuento:</code> Registra el estado del tipo de descuento
     * <li><code>String descripcionTipoDescuento:</code> Registra la descripci\u00F3n del tipo de descuento
     * </ul>
     */
    private String codigoClasificacionDescuento;
    private String estadoTipoDescuento;
    private String descripcionTipoDescuento;
    
    /**
     * Botones utlizados para guardar los cambios ingresados
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al formulario de Autorizaciones.</li>
     * <li><code>String botonActualizar:</code> Desactiva la autorizaci\u00F3n seleccionada</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonAgregarClasificacion;
    private String botonCancelar;
    private String botonActualizar;
    
    /**
     * @return Devuelve codigoClasificacionDescuento.
     */
    public String getCodigoClasificacionDescuento()
    {
        return codigoClasificacionDescuento;
    }
    /**
     * @param codigoClasificacionDescuento El codigoClasificacionDescuento a establecer.
     */
    public void setCodigoClasificacionDescuento(
            String codigoClasificacionDescuento)
    {
        this.codigoClasificacionDescuento = codigoClasificacionDescuento;
    }
    /**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevoTipoDescuento.jsp</code> y 
     * <code>actualizarTipoDescuento.jsp</code>.
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
            if(ayuda.equals("actualizarTipoDescuento")) {
                this.botonActualizar = ayuda;
            } else if (ayuda.equals("registrarTipoDescuento")) {
                this.botonRegistrarNuevo = ayuda;
            }
        }
        
        try
        {
            //cuando se da click el boton Nuevo Registro----------------------------------------------------------------
            if (this.botonRegistrarNuevo != null) 
            {
                //valida los campos a registrar
                validar.validateMandatory(errors, "descripcionTipoDescuento", this.descripcionTipoDescuento,
                        "errors.requerido", "Descripci\u00F3n");
            }
            /*----------------------- cuando se da clic en el bot\u00F3n agregar clasificacion ----------------------*/
            else if(this.botonAgregarClasificacion!=null){
                validar.validateMandatory(errors,"codigoClasificacion",this.codigoClasificacionDescuento,"errors.requerido","C\u00F3digo de Clasificaci\u00F3n");
            }
            //cuando se va a actualizar un registro----------------------------------------------------------------------
            else if (this.botonActualizar != null) 
            {
                validar.validateMandatory(errors, "estadoTipoDescuento", this.estadoTipoDescuento, "errors.requerido",
                "Estado Tipo Descuento");
                if(errors.size()>0){
                    this.estadoTipoDescuento="";
                }
                validar.validateMandatory(errors, "descripcionTipoDescuento", this.descripcionTipoDescuento,
                        "errors.requerido", "Descripci\u00F3n");
            }
            //cuando se ha escogido un registro---------------------------------------------------------------------------
            else if (request.getParameter("indice") != null)
            {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
                ArrayList tipoDescuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
                //obtiene el indice del registro escogido
                int indice = Integer.parseInt(request.getParameter("indice"));
                if (indice >= tipoDescuentos.size())
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
     * <code>nuevoTipoDescuento.jsp</code> y <code>actualizarTipoDescuento.jsp</code>, en cada petici\u00F3n.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) 
    {
        if(request.getParameter("indiceEliminacion")!=null || request.getParameter("indiceDesactivacion")!=null){
            guardarCamposEnSession(request);
        }
        this.codigoClasificacionDescuento=null;
        botonRegistrarNuevo = null;
        botonCancelar = null;
        botonActualizar = null;
        descripcionTipoDescuento = null;
        estadoTipoDescuento = null;
        this.botonAgregarClasificacion=null;
    }
    
    /**
     * Guarda los campos del formulario en sesi\u00F3n.
     * @param request		La petici\u00F3n que se est\u00E1 procesando
     */
    private void guardarCamposEnSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        
        //actualizaci\u00F3n de los datos en sesi\u00F3n
        String estadoTipoDescuento = (String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.estado");
        if(this.estadoTipoDescuento!=null && !this.estadoTipoDescuento.equals(estadoTipoDescuento))
            session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.estado",this.estadoTipoDescuento);
        
        String descripcionTipoDescuento = (String)session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcion");
        if(this.descripcionTipoDescuento!=null && !this.descripcionTipoDescuento.equals(descripcionTipoDescuento))
            session.setAttribute("ec.com.smx.sic.sispe.tipoDescuento.descripcion",this.descripcionTipoDescuento);
    }
    
    /**
     * @return Devuelve descripcionTipoDescuento.
     */
    public String getDescripcionTipoDescuento()
    {
        return descripcionTipoDescuento;
    }
    /**
     * @param descripcionTipoDescuento El descripcionTipoDescuento a establecer.
     */
    public void setDescripcionTipoDescuento(String descripcionTipoDescuento)
    {
        this.descripcionTipoDescuento = descripcionTipoDescuento;
    }
    /**
     * @return Devuelve estadoTipoDescuento.
     */
    public String getEstadoTipoDescuento()
    {
        return estadoTipoDescuento;
    }
    /**
     * @param estadoTipoDescuento El estadoTipoDescuento a establecer.
     */
    public void setEstadoTipoDescuento(String estadoTipoDescuento)
    {
        this.estadoTipoDescuento = estadoTipoDescuento;
    }
    /**
     * @return Devuelve botonAgregarClasificacion.
     */
    public String getBotonAgregarClasificacion()
    {
        return botonAgregarClasificacion;
    }
    /**
     * @param botonAgregarClasificacion El botonAgregarClasificacion a establecer.
     */
    public void setBotonAgregarClasificacion(String botonAgregarClasificacion)
    {
        this.botonAgregarClasificacion = botonAgregarClasificacion;
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
