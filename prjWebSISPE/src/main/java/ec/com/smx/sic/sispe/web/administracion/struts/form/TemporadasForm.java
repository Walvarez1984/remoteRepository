/*
 * TemporadasForm.java 
 * Creado el 08/06/2006
 */

package ec.com.smx.sic.sispe.web.administracion.struts.form;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import ec.com.smx.framework.common.util.converter.SqlTimestampConverter;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;

/**
 * Esta clase permite el gestionar los datos de las Temporadas dentro de la 
 * aplicaci\u00F3n.
 * 
 * @author 	dlopez
 * @author	mbraganza
 * @version 1.1
 * @since 	JSDK 1.4.2
 */
@SuppressWarnings("serial")
public class TemporadasForm extends ActionForm 
{
    
    /**
     * Campos del formulario Temporadas
     * <ul>
     * <li><code>String descripcionTemporada:</code> Campo donde se ingresar\u00E1 la descripci\u00F3n de la Temporada.</li>
     * <li><code>String fechaInicialTemporada:</code> Campo donde se almacena la fecha incial de la Temporada.</li>
     * <li><code>String fechaFinalTemporada:</code> Campo donde se almacena la fecha incial de la Temporada.</li>
     * <li><code>String incluirArtTemAnt:</code> Campo donde se almacena si se desea incluir los aryiculos d ela temporada anterior en la nueva Temporada.</li>
     * <ul>
     */
    private String descripcionTemporada;
    private String fechaInicialTemporada;
    private String fechaFinalTemporada;
    private String incluirArtTemAnt;
    
    /**
     * Botones del formulario Temporadas
     * <ul>
     * <li><code>String botonRegistrarNuevo:</code> Guardar\u00E1 los datos ingresados en la base de datos.</li>
     * <li><code>String botonCancelar:</code> Cancelar\u00E1 la acci\u00F3n enviandonos al formulario de Temporadas.</li>
     * <li><code>String botonActualizar:</code> Actulizara los registros de las Temporadas</li>
     * <ul>
     */
    private String botonRegistrarNuevo;
    private String botonCancelar;
    private String botonActualizar;
    
    /**
     * Realiza la validaci\u00F3n de la p\u00E1gina <code>nuevaTemporada.jsp</code>.
     * @param mapping		El mapeo utilizado para seleccionar esta instancia
     * @param request		El request que estamos procesando
     * @return errors		Los errores recogidos durante la ejecuci\u00F3n
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errors = new ActionErrors();
        PropertyValidator validar = new PropertyValidatorImpl();
        HttpSession session = request.getSession();
        
        String ayuda = request.getParameter(Globals.AYUDA);
        if(ayuda != null && !ayuda.equals("")) {
            if(ayuda.equals("nuevaTemporada")) {
                this.botonRegistrarNuevo = ayuda;
            }
        }
        
        try
        {
            //Acci\u00F3n nuevo registro---------------------------------------------------------------------------------------
            if (this.botonRegistrarNuevo != null || this.botonActualizar != null) 
            {
                //valida de campos
                validar.validateMandatory(errors, "descripcionTemporada", this.descripcionTemporada, "errors.requerido",
                "Descripci\u00F3n");
                validar.validateFecha(errors, "fechaInicialTemporada", this.fechaInicialTemporada, true, MessagesWebSISPE
                        .getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha Inicial");
                validar.validateFecha(errors, "fechaFinalTemporada", this.fechaFinalTemporada, true, MessagesWebSISPE
                        .getString("formatos.fecha"),"errors.formato.fecha","errors.requerido", "Fecha Final");
                
                //se valida el rango de las fechas
                if(errors.isEmpty()){
                  	//Clase utilizada para convertir una fecha de formato String a Date.
                  	SqlTimestampConverter convertidor = new SqlTimestampConverter(new String[]{"formatos.fechahora"});
                    Timestamp fechaInicial = (Timestamp)convertidor.convert(Timestamp.class,this.getFechaInicialTemporada());
                    Timestamp fechaFinal = (Timestamp)convertidor.convert(Timestamp.class,this.getFechaFinalTemporada());
                    //se valida que la fecha inicial no sea mayor a la final
                    if(fechaInicial.getTime() > fechaFinal.getTime())
                        errors.add("fechas",new ActionMessage("errors.rango.valores","La fecha Inicial","la fecha Final"));
                }
            }
            //comprobaci\u00F3n el indice de la lista--------------------------------------------------------------------------
            else if (request.getParameter("indice") != null) 
            {
                //se obtiene de sesi\u00F3n la colecci\u00F3n del detalle
                ArrayList temporadas = (ArrayList) session.getAttribute("ec.com.smx.sic.sispe.tabla");
                //convierte el indice String a int
                int indice = Integer.parseInt(request.getParameter("indice"));
                //compara el indice con el # de registros
                if (indice >= temporadas.size())
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
     * Resetea los controles del formulario de la p\u00E1gina <code>nuevaTemporada.jsp</code>, en cada petici\u00F3n.
     * @param mapping 	El mapeo utilizado para seleccionar esta instancia
     * @param request 	La petici&oacue; que estamos procesando
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        
        this.descripcionTemporada = null;
        this.fechaInicialTemporada = null;
        this.fechaFinalTemporada = null;
        this.botonRegistrarNuevo = null;
        this.botonActualizar = null;
        this.botonCancelar = null;
        this.incluirArtTemAnt = null;
        
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
     * @return Devuelve descripcionTemporada.
     */
    public String getDescripcionTemporada()
    {
        return descripcionTemporada;
    }
    /**
     * @param descripcionTemporada El descripcionTemporada a establecer.
     */
    public void setDescripcionTemporada(String descripcionTemporada)
    {
        this.descripcionTemporada = descripcionTemporada;
    }
    /**
     * @return Devuelve fechaFinalTemporada.
     */
    public String getFechaFinalTemporada()
    {
        return fechaFinalTemporada;
    }
    /**
     * @param fechaFinalTemporada El fechaFinalTemporada a establecer.
     */
    public void setFechaFinalTemporada(String fechaFinalTemporada)
    {
        this.fechaFinalTemporada = fechaFinalTemporada;
    }
    /**
     * @return Devuelve fechaInicialTemporada.
     */
    public String getFechaInicialTemporada()
    {
        return fechaInicialTemporada;
    }
    /**
     * @param fechaInicialTemporada El fechaInicialTemporada a establecer.
     */
    public void setFechaInicialTemporada(String fechaInicialTemporada)
    {
        this.fechaInicialTemporada = fechaInicialTemporada;
    }

	public String getIncluirArtTemAnt() {
		return incluirArtTemAnt;
	}

	public void setIncluirArtTemAnt(String incluirArtTemAnt) {
		this.incluirArtTemAnt = incluirArtTemAnt;
	}
    
}
