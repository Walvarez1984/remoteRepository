/*
 * DescuentosForm.java 
 * Creado el 27/06/2006
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
 * Esta clase permite el gestionar los datos de los Descuentos dentro de la aplicaci\u00F3n.
 * </p>
 * @author 	dlopez
 * @version 1.0
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class DescuentosForm extends ActionForm
{
    /**
     * Campos del formulario de Descuentos
     * <ul>
     * <li><code>estadoDescuento:</code> Estado del descuento
     * <li><code>codigoClasificacionDescuento:</code> C\u00F3digo de clasificaci\u00F3n del descuento
     * <li><code>codigoMotivoDescuento:</code> C\u00F3digo del motivo de decuento
     * <li><code>rangoInicialDescuento:</code> Rango inicial del descuento
     * <li><code>rangoFinalDescuento:</code> Rango final del descuento
     * <li><code>porcentajeDescuento:</code> Porcentaje del descuento <strong>en enteros (%) </strong>
     * </ul>
     * 
     */
    private String estadoDescuento;
    // private String codigoTipoDescuento;
    private String codigoMotivoDescuento;
    private String rangoInicialDescuento;
    private String rangoFinalDescuento;
    private String porcentajeDescuento;
    
    /**
     * Botones utlizados para guardar los cambios ingresados
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al formulario de Descuentos.</li>
     * <li><code>String botonActualizar:</code> Actulizara los registros de las Descuentos</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonActualizar;
    private String botonCancelar;
    
    /**
     * @return Devuelve estadoDescuento.
     */
    public String getEstadoDescuento()
    {
        return estadoDescuento;
    }
    /**
     * @param estadoDescuento El estadoDescuento a establecer.
     */
    public void setEstadoDescuento(String estadoDescuento)
    {
        this.estadoDescuento = estadoDescuento;
    }
    /**
     * Realiza la validaci\u00F3n de las p\u00E1ginas <code>nuevoDescuento.jsp</code> y <code>actualizarDescuento.jsp</code>.
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
            if(ayuda.equals("actualizarDescuento")) {
                this.botonActualizar = ayuda;
            } 
            else if(ayuda.equals("registrarDescuento")) {
                this.botonRegistrarNuevo = ayuda;
            }
        }
        
        try
        {
            //cuando se da click el boton Registrar---------------------------------------------------------------------
            if (this.botonRegistrarNuevo != null || this.botonActualizar != null) 
            {
                if(this.botonActualizar != null){
                    validar.validateMandatory(errors,"estadoDescuento",this.estadoDescuento,"errors.requerido","Estado del descuento");
                }
                /*validar.validateMandatory(errors, "codigoTipoDescuento", this.codigoTipoDescuento, "errors.requerido",
                 "Tipo Descuento");*/
                validar.validateMandatory(errors, "codigoMotivoDescuento", this.codigoMotivoDescuento, "errors.requerido",
                "Motivo Descuento");
                validar.validateDouble(errors, "rangoInicialDescuento", this.rangoInicialDescuento, true, 0.01, 9999999999.99,
                        "errors.formato.double", "Rango Inicial");
                validar.validateDouble(errors, "rangoFinalDescuento", this.rangoFinalDescuento, true, 0.01, 9999999999.99,
                        "errors.formato.double", "Rango Final");
                validar.validateDouble(errors, "porcentajeDescuento", this.porcentajeDescuento, true, 0, 100,
                        "errors.formato.double", "Porcentaje");
                if(errors.isEmpty()){
                    if(Double.parseDouble(this.rangoInicialDescuento) >= Double.parseDouble(this.rangoFinalDescuento)){
                        //se muestra un mensaje porque el rango inicial es mayor que el final
                        errors.add("rangos",new ActionMessage("errors.rango.valores","El Rango inicial","al rango final"));
                    }
                }
            }
            //comprueba el indice de la lista---------------------------------------------------------------------------
            else if (request.getParameter("indice") != null) {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
                ArrayList descuentos = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tipoDescuento.descuentos");
                //convierte el indice String a int
                int indice = Integer.parseInt(request.getParameter("indice"));
                //compara el indice con el # de registros
                if (indice >= descuentos.size())
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
     * <code>nuevoDescuento.jsp</code> y <code>actualizarDescuento.jsp</code>.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) 
    {
        rangoInicialDescuento = null;
        rangoFinalDescuento = null;
        porcentajeDescuento = null;
        codigoMotivoDescuento = null;
        botonRegistrarNuevo = null;
        botonCancelar = null;
        botonActualizar = null;
    }
    
    /**
     * @return Devuelve codigoMotivoDescuento.
     */
    public String getCodigoMotivoDescuento()
    {
        return codigoMotivoDescuento;
    }
    /**
     * @param codigoMotivoDescuento El codigoMotivoDescuento a establecer.
     */
    public void setCodigoMotivoDescuento(String codigoMotivoDescuento)
    {
        this.codigoMotivoDescuento = codigoMotivoDescuento;
    }
    /**
     * @return Devuelve porcentajeDescuento.
     */
    public String getPorcentajeDescuento()
    {
        return porcentajeDescuento;
    }
    /**
     * @param porcentajeDescuento El porcentajeDescuento a establecer.
     */
    public void setPorcentajeDescuento(String porcentajeDescuento)
    {
        this.porcentajeDescuento = porcentajeDescuento;
    }
    /**
     * @return Devuelve rangoFinalDescuento.
     */
    public String getRangoFinalDescuento()
    {
        return rangoFinalDescuento;
    }
    /**
     * @param rangoFinalDescuento El rangoFinalDescuento a establecer.
     */
    public void setRangoFinalDescuento(String rangoFinalDescuento)
    {
        this.rangoFinalDescuento = rangoFinalDescuento;
    }
    /**
     * @return Devuelve rangoInicialDescuento.
     */
    public String getRangoInicialDescuento()
    {
        return rangoInicialDescuento;
    }
    /**
     * @param rangoInicialDescuento El rangoInicialDescuento a establecer.
     */
    public void setRangoInicialDescuento(String rangoInicialDescuento)
    {
        this.rangoInicialDescuento = rangoInicialDescuento;
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
