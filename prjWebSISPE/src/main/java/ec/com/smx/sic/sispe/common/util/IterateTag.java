/* Creado el 02/07/2007
 * TODO
 */
/**
 * 
 */
package ec.com.smx.sic.sispe.common.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;

import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleDiaLocalDTO;


/**
 * @author jacalderon
 *
 */

@SuppressWarnings("serial")
public class IterateTag  extends BodyTagSupport {
		
	/**
     * Iterator of the elements of this collection, while we are actually
     * running.
     */
    protected Iterator iterator = null;

    // ------------------------------------------------------------- Properties

    /**
     * The collection over which we will be iterating.
     */
    protected Object collection = null;

    /**
     * The name of the scripting variable to be exposed.
     */
    protected String id = null;

    /**
     * The name of the collection or owning bean.
     */
    protected String name = null;

   /**
     * The property name containing the collection.
     */
    protected String property = null;

    /**
     * The scope of the bean specified by the name property, if any.
     */
    protected String scope = null;

    /**
     * The Java class of each exposed element of the collection.
     */
    protected String type = null;

    public Object getCollection() {
        return (this.collection);
    }

    public void setCollection(Object collection) {
        this.collection = collection;
    }

    public String getId() {
        return (this.id);
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * <p>Return the zero-relative index of the current iteration through the
     * loop.  If you specify an <code>offset</code>, the first iteration
     * through the loop will have that value; otherwise, the first iteration
     * will return zero.</p>
     *
     * <p>This property is read-only, and gives nested custom tags access to
     * this information.  Therefore, it is <strong>only</strong> valid in
     * between calls to <code>doStartTag()</code> and <code>doEndTag()</code>.
     * </p>
     */
    
 
    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return (this.property);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return (this.scope);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return (this.type);
    }

    public void setType(String type) {
        this.type = type;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Construct an iterator for the specified collection, and begin looping
     * through the body once per element.
     *
     * @throws JspException if a JSP exception has occurred
     */
    @SuppressWarnings("deprecation")
	public int doStartTag() throws JspException {
        // Acquire the collection we are going to iterate over
    	Format formatoNumeroEntero = null;
        Object collection = this.collection;
        LogSISPE.getLog().info("collection: {}", collection);
         collection =
                TagUtils.getInstance().lookup(pageContext, name, property,
                    scope);
        if (collection == null) {
            JspException e = 
                new JspException("error iterate.collection");

            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }

        /**********************************************************************/
         
        /**********************************************************************/
        // Construct an iterator for this collection
        if (collection instanceof Collection) {
            iterator = ((Collection) collection).iterator();
        } else {
            JspException e =
                new JspException("error en iterate.iterator");

            TagUtils.getInstance().saveException(pageContext, e);
            throw e;
        }
        
        formatoNumeroEntero = NumberFormat.getCurrencyInstance();
        ((DecimalFormat) formatoNumeroEntero).applyLocalizedPattern(MessagesWebSISPE.getString("formatos.enteros"));
        
        recursividad((Collection) collection, pageContext, formatoNumeroEntero);
             
            return (EVAL_BODY_TAG);
      
    }

   
    public static void recursividad(Collection calendarioDetalleDiaLocalDTOCol, PageContext pageContext, Format formatoNumeroEntero)
	  {
    	// Se inicializa el contador de registro y el tama\u00F1o de la columna de contenido
    	int cont = 0;
    	int size = 16;
    	String var = "";
    	
    	for(Iterator col=calendarioDetalleDiaLocalDTOCol.iterator();col.hasNext();){
	  		CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)col.next();
	  		try {
	  			
	  			LogSISPE.getLog().info("Tipo: {}",calendarioDetalleDiaLocalDTO.getCodigoTipoMovimiento());
	  			LogSISPE.getLog().info("Motivo: {}",calendarioDetalleDiaLocalDTO.getCodigoMotivoMovimiento());
	  			LogSISPE.getLog().info("ID: {}",cont);
	  			
	  			/***************************************Presentacion del html************************************/
				String color=null;
				if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ingresado")))
					color=MessagesWebSISPE.getString("color.colorIngreso");
				else if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.egresado")))
					color=MessagesWebSISPE.getString("color.colorEgreso");
				else if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.reservado")))
					color=MessagesWebSISPE.getString("color.colorReservado");
				else if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.anulado")))
						color=MessagesWebSISPE.getString("color.colorAnulado");
				else if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.ajusteCapacidad")))
					color=MessagesWebSISPE.getString("color.colorAjustes");
				if(calendarioDetalleDiaLocalDTO.getSecuencialDetalleReferencia()!=null){
					pageContext.getOut().write("<tr><td width=\"6%\" align=\"center\">" + calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia() + "</td>");
					pageContext.getOut().write("<td width=\"6%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getSecuencialDetalleReferencia() + "</td>");
				}
				else{
					pageContext.getOut().write("<tr><td width=\"6%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia() + "</td>");
					pageContext.getOut().write("<td width=\"6%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">NA</td>");
				}
				
				//IMPORTANTE: Se muestra la fecha de entrega del pedido en vez de la hora 
				// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//pageContext.getOut().write("<td width=\"12%\" align=\"center\" class=\"columna_contenido\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getHoraDesde() + "</td>");
				
				if(calendarioDetalleDiaLocalDTO.getPedidoDTO()!=null){
					
					String varFechaOriginal = calendarioDetalleDiaLocalDTO.getPedidoDTO().getPrimeraFechaEntrega().toString();					
					String varFechaFinal = varFechaOriginal.substring(0,10);
					
					pageContext.getOut().write("<td width=\"7%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + varFechaFinal + "</td>");
				}
				else{
					pageContext.getOut().write("<td width=\"7%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">NA</td>");
				}
				// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				
				pageContext.getOut().write("<td width=\"8%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getCalendarioTipoMovimientoDTO().getDescripcionTipoMovimiento() + "</td>");
				pageContext.getOut().write("<td width=\"10%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getCalendarioMotivoMovimientoDTO().getDescripcionMotivoMovimiento() + "</td>");
				
				//IMPORTANTE: Se procura a\u00F1adir m\u00E9todo JS para truncar contenido de columna de concepto de movimiento
				// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				//pageContext.getOut().write("<td width=\"12%\" align=\"left\" class=\"columna_contenido fila_contenido_dif textoNegro9\" bgcolor=\""+color+"\">" + calendarioDetalleDiaLocalDTO.getConceptoMovimiento() + "</td>");
				pageContext.getOut().write("<td width=\"12%\" align=\"left\" class=\"columna_contenido fila_contenido_dif textoNegro9\" bgcolor=\""+color+"\">");				
				pageContext.getOut().write("<div class=\"descripcion"+cont+"\" id=\"nombre"+cont+"\">");							
				pageContext.getOut().write("</div>");				
				pageContext.getOut().write("<script type=\"text/javascript\">");
								
				// Validacion de tipo de movimiento
				if(calendarioDetalleDiaLocalDTO.getPedidoDTO()!= null 
						&& calendarioDetalleDiaLocalDTO.getCodigoTipoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.tipoMovimiento.codigo.ingreso")) 
						&& calendarioDetalleDiaLocalDTO.getCodigoMotivoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.motivoMovimiento.codigo.reserva")))
				{
					LogSISPE.getLog().info("Se procede a mostrar mensaje adicional...");
					
					
					String ci = calendarioDetalleDiaLocalDTO.getPedidoDTO().getNpCedulaContacto();	
					String nc = calendarioDetalleDiaLocalDTO.getPedidoDTO().getNpNombreContacto();	
					String tc = calendarioDetalleDiaLocalDTO.getPedidoDTO().getNpTelefonoContacto();	
					String ne = calendarioDetalleDiaLocalDTO.getPedidoDTO().getNpNombreEmpresa();	
					String rc = "NA";
					/*if(calendarioDetalleDiaLocalDTO.getPedidoDTO().getLlaveContratoPOS()!= null )						
						rc = calendarioDetalleDiaLocalDTO.getPedidoDTO().getLlaveContratoPOS();*/
					
					if(calendarioDetalleDiaLocalDTO.getPedidoDTO().getEstadoPedidoDTO().getLlaveContratoPOS()!= null )						
						rc = calendarioDetalleDiaLocalDTO.getPedidoDTO().getEstadoPedidoDTO().getLlaveContratoPOS();
					
					var = "CLIENTE: CI: "+ci+" NC: "+nc+" TC: "+tc+" NE: "+ne+" RESERVACION: "+rc;
																					
				}
				else{
					LogSISPE.getLog().info("Se procede a mostrar mensaje original...");
					var = calendarioDetalleDiaLocalDTO.getConceptoMovimiento();				
					var = var.replace("\n"," ");
				}
				
				LogSISPE.getLog().info("CONTENIDO DE VARIABLE: {}",var);
				
				//pageContext.getOut().write("truncar('"+var+"','descripcion','"+cont+"','"+size+"','Concepto')");				
				pageContext.getOut().write("</script>");				
				pageContext.getOut().write(" " + var + "</td>");
				// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				
				
				pageContext.getOut().write("<td width=\"4%\" align=\"right\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">" + formatoNumeroEntero.format(calendarioDetalleDiaLocalDTO.getCantidadAlmacenamiento()) + "</td>");
				pageContext.getOut().write("<td width=\"4%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">");
				if(calendarioDetalleDiaLocalDTO.getEstadoMovimiento().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoMovimiento.reservado"))){
					LogSISPE.getLog().info("es estado reservado");
					String cantidadAlmacenamiento = formatoNumeroEntero.format(calendarioDetalleDiaLocalDTO.getNpCantidadAlmacenamiento());
					pageContext.getOut().write("<input type=\"text\" name=\"cantidadAlmacenamientoNueva\" size=\"3\" class=\"textObligatorio\" value="+cantidadAlmacenamiento+" size=\"3\" onkeypress=\"requestAjaxEnter('kardex.do', ['divlistado1','mensajes','confirmar','configuracion'], {parameters: 'editarCantidad="+calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia()+"',popWait:true});\"/>");
					LogSISPE.getLog().info("editarCantidad: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
					//pageContext.getOut().write("<smx:text name=\"kardexForm\" property=\"cantidadAlmacenamientoNueva\" value="+cantidadAlmacenamiento+" styleClass=\"textObligatorio\" size=\"3\" onkeypress=\"requestAjaxEnter('kardex.do', ['divlistado1','mensajes','confirmar','configuracion'], {parameters: 'editarCantidad="+lengthCount+"',popWait:true});\"/>");
				}
				else{
					String cantidadAlmacenamiento = formatoNumeroEntero.format(calendarioDetalleDiaLocalDTO.getNpCantidadAlmacenamiento());
					pageContext.getOut().write("<input type=\"hidden\" name=\"cantidadAlmacenamientoNueva\" value="+cantidadAlmacenamiento+" size=\"3\" onkeypress=\"requestAjaxEnter('kardex.do', ['divlistado1','mensajes','confirmar','configuracion'], {parameters: 'editarCantidad="+calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia()+"',popWait:true});\"/>");
					LogSISPE.getLog().info("editarCantidad: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
				}
				pageContext.getOut().write("</td>");
				pageContext.getOut().write("<td width=\"12%\" align=\"center\" class=\"columna_contenido fila_contenido_dif\" bgcolor=\""+color+"\">");
				if(calendarioDetalleDiaLocalDTO.getNumeroPedido()!=null){
					String numPedido=calendarioDetalleDiaLocalDTO.getNumeroPedido();	
					String local=calendarioDetalleDiaLocalDTO.getId().getCodigoLocal().toString();
					pageContext.getOut().write("<a title=\"Detalle Pedido\" href=\"#\" onclick=\"mypopup('detalleEstadoPedido.do?verPedidoKardex=" + numPedido + "&local="+local+"','ESTDETPED')\">"+numPedido + "</a>");
				}
				pageContext.getOut().write("</td>");
				pageContext.getOut().write("</tr>");
			} catch (IOException e) {
				// TODO Bloque catch generado autom\u00E1ticamente
				LogSISPE.getLog().error("error de aplicaci\u00F3n",e);
			}
			/********************************************************************************************************/
			/*********************Si tiene hijos llama nuevamente a la funcion para navegar los hijos*******************/
	  		if(calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion()!=null && calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion().size()>0){
	   			recursividad(calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion(), pageContext, formatoNumeroEntero);
	  		}
	  		else{
	   			LogSISPE.getLog().info("itera padre: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
	  		}
	  			
	  		// Se incrementa la variable para asignar valor al siguiente registro
	  		cont++;
	  	}
	  }
    /**
     * Clean up after processing this enumeration.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
      
        return (EVAL_PAGE);
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();

        iterator = null;
        id = null;
        name = null;
        property = null;
        scope = null;
    }
}
