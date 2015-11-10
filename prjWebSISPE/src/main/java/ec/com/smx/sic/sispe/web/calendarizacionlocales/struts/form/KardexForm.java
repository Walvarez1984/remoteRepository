 /*
 * Creado el 17/05/2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c\u00F3digo - Plantillas de c\u00F3digo
 */
package ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioDetalleDiaLocalDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.HorasDTO;

/**
 * @author jacalderon
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class KardexForm extends ActionForm{
	
	//IMPORTANTE: Se comentan variables de fecha en ingreso de kardex 
	// ---------------------------------------------------------------
	//private String horas;
	//private String minutos;
	//private String segundos;
	// ---------------------------------------------------------------
	private String tipoMovimiento;
	private String motivo;
	private String concepto;
	private String cantidad;
	
	private String[] cantidadAlmacenamientoNueva;
	private String[] cantidadAlmacenamientoHijoNueva;
	private String[] conceptoMovimiento;
	private Collection secuenciales;
	
	private Object[] horasDia;
	private Collection transporte ;
	private String horaSeleccionada;
	private String transporteSeleccionado;
	private String numCamiones;
	
	public static final String HORASDIAS  = "ec.com.smx.calendarizacion.horasdias";
	
	/**
	   * Resetea los controles del formulario de la p\u00E1gina <code>filtrosDespachoReservacion.jsp</code>, en cada petici\u00F3n.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   */
	  public void reset(ActionMapping mapping, HttpServletRequest request) 
	  {
		// IMPORTANTE: Se comentan variables de fecha en ingreso de kardex 
		// ---------------------------------------------------------------
	    //this.horas=null;
	    //this.minutos=null;
	    //this.segundos=null;
		// ---------------------------------------------------------------
	    this.tipoMovimiento=null;
	    this.motivo=null;
	    this.concepto=null;
	    this.cantidad=null;
    	this.cantidadAlmacenamientoNueva = new String[0];	
    	this.cantidadAlmacenamientoHijoNueva = new String[0];	
	    this.conceptoMovimiento=new String[0];
	    this.secuenciales=new ArrayList();
	  }
	  
	  /**
	   * Ejecuta la validacion en la p\u00E1gina JSP <code>kardex.jsp</code>
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia	
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	   * <code>null</code>
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    ActionErrors errors = new ActionErrors();
	    PropertyValidator validar = new PropertyValidatorImpl();
	    HttpSession session = request.getSession();
	    	    
	    if (request.getParameter("botonAgregarKardex") != null) {
	    	
	    	//IMPORTANTE: Se comentan validaciones para fecha 
	    	// ----------------------------------------------------------------------------------------
	    	/*validar.validateLong(errors,"horas",this.horas,true,0,0,"errors.horas.requerido");
        	if(errors.size()==0){
            	if(new Integer(this.horas).intValue()>24){
            		errors.add("horasError",new ActionMessage("errors.horas.requerido"));
            	}
        	}
        	
        	validar.validateLong(errors,"minutos",this.minutos,true,0,0,"errors.minutos.requerido");
        	if(errors.size()==0){
            	if(new Integer(this.minutos).intValue()>60)
            		errors.add("minutoError",new ActionMessage("errors.minutos.requerido"));
        	}

        	validar.validateLong(errors,"segundos",this.segundos,true,0,0,"errors.segundos.requerido");
        	if(errors.size()==0){
            	if(new Integer(this.segundos).intValue()>60)
            		errors.add("segundoError",new ActionMessage("errors.segundos.requerido"));
        	}*/
	    	// ----------------------------------------------------------------------------------------

	    	validar.validateMandatory(errors, "tipoMovimiento", this.tipoMovimiento,"errors.tipoMovimiento.requerido");
	    	validar.validateMandatory(errors, "motivo", this.motivo,"errors.motivo.requerido");
	    	validar.validateMandatory(errors, "concepto", this.concepto,"errors.concepto.requerido");
	    	validar.validateLong(errors, "cantidad", this.cantidad, true, 1L, Long.MAX_VALUE, "error.validacion.cantidad.invalido");
	    }
	    else if(request.getParameter("editarCantidad")!=null){
	    	LogSISPE.getLog().info("ayuda: {}" , request.getParameter("ayuda"));
	    	LogSISPE.getLog().info("entro a validar editarCantidad: {}", request.getParameter("editarCantidad"));
	    	LogSISPE.getLog().info("tama\u00F1o arreglo cantidad: {}", this.cantidadAlmacenamientoNueva.length);
	    	//this.cantidadAlmacenamientoNueva=
	    	ArrayList calendarioDetalleDiaLocalDTOCol=(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
			/*int index=new Integer(request.getParameter("editarCantidad")).intValue();
			LogSISPE.getLog().info("cantidad[" + index + "]: " + this.cantidadAlmacenamientoNueva[index]);*/
	    	recursividad(calendarioDetalleDiaLocalDTOCol,request.getParameter("editarCantidad"),session,secuenciales);
	    	int indice=0;
	    	int index=0;
	    	LogSISPE.getLog().info("secuenciales tama\u00F1o: {}" , this.secuenciales.size());
	    	for(Iterator i=secuenciales.iterator();i.hasNext();){
	    		Object secuencia=(i.next());
	    		LogSISPE.getLog().info("secuencia: {}" , secuencia);
	    		if(secuencia!=null)
	    			index=indice;
    			indice++;
	    	}
	    	LogSISPE.getLog().info("index: {}",index);
			validar.validateDouble(errors,"cantidadAlmacenamiento",this.cantidadAlmacenamientoNueva[index],true,0,0,"error.validacion.cantidad.invalido");
				
		    	LogSISPE.getLog().info("secuenciales tama\u00F1o1: {}" , this.secuenciales.size());
				CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO");
				LogSISPE.getLog().info("devuelve calendario: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
				//CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO = (CalendarioDetalleDiaLocalDTO)calendarioDetalleDiaLocalDTOCol.get(new Integer(request.getParameter("editarCantidad")).intValue());
				try{
					session.setAttribute("ec.com.smx.calendarizacion.cantidadReservada",this.cantidadAlmacenamientoNueva[index]);
					calendarioDetalleDiaLocalDTO.setNpCantidadAlmacenamiento(new Double(this.cantidadAlmacenamientoNueva[index]));
				}catch(NumberFormatException e){
					calendarioDetalleDiaLocalDTO.setCantidadAlmacenamiento(calendarioDetalleDiaLocalDTO.getNpCantidadAlmacenamiento());
				}
	    }
	    else if(request.getParameter("editarCantidad")!=null && request.getParameter("editarCantidadHijo")!=null){
	    	LogSISPE.getLog().info("entro a validar editarCantidad");
	    	ArrayList calendarioDetalleDiaLocalDTOCol=(ArrayList)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol");
			int index=new Integer(request.getParameter("editarCantidadHijo")).intValue();
			LogSISPE.getLog().info("cantidad[" + index + "]: " + this.cantidadAlmacenamientoHijoNueva[index]);
			validar.validateDouble(errors,"cantidadAlmacenamiento",this.cantidadAlmacenamientoHijoNueva[index],true,0,0,"error.validacion.cantidad.invalido");
				LogSISPE.getLog().info("error es igual a 0");
				CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO = (CalendarioDetalleDiaLocalDTO)calendarioDetalleDiaLocalDTOCol.get(new Integer(request.getParameter("editarCantidad")).intValue());
				try{
					session.setAttribute("ec.com.smx.calendarizacion.cantidadReservada",this.cantidadAlmacenamientoNueva[index]);
					calendarioDetalleDiaLocalDTO.setNpCantidadAlmacenamiento(new Double(this.cantidadAlmacenamientoNueva[index]));
				}catch(NumberFormatException e){
					calendarioDetalleDiaLocalDTO.setCantidadAlmacenamiento(calendarioDetalleDiaLocalDTO.getNpCantidadAlmacenamiento());
				}
	    }
	    LogSISPE.getLog().info("errores: ", errors.size());
	    return(errors);
	 }
	  /**
		 * Funcion recursiva para ganerar el arbol
		 */
		public static void recursividad(Collection calendarioDetalleDiaLocalDTOCol,String secuencial,HttpSession session,Collection secuenciales)
		  {
			LogSISPE.getLog().info("secuen: {}", secuencial);
			
			//CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO1=new CalendarioDetalleDiaLocalDTO();
			  	for(Iterator col=calendarioDetalleDiaLocalDTOCol.iterator();col.hasNext();){
			  		CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)col.next();
			  		LogSISPE.getLog().info("calendarioDetalleDiaLocalDTO.id: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
			  		if(calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia().toString().equals(secuencial)){
			  			LogSISPE.getLog().info("sec: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
			  			session.setAttribute("ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO", calendarioDetalleDiaLocalDTO);
			  			secuenciales.add(calendarioDetalleDiaLocalDTO);
			  			break;
			  		}
			  		
			  		if(calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion()!=null && calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion().size()>0){
			  			LogSISPE.getLog().info("itera: {}" , calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
			  			secuenciales.add(null);
			  			recursividad(calendarioDetalleDiaLocalDTO.getMovimientosPorAnulacion(),secuencial,session,secuenciales);
			  		}
			  		else{
			  			secuenciales.add(null);
			  			LogSISPE.getLog().info("itera padre: {}", calendarioDetalleDiaLocalDTO.getId().getSecuencialDetalleCalendarioDia());
			  		}
			  	}
			  	//LogSISPE.getLog().info("secuencial: " + calendarioDetalleDiaLocalDTO1.getId().getSecuencialDetalleCalendarioDia());
		  }
	  
	  /**
		 * Permite validar si los campos requeridos para las condiciones de reservas.
		 * @param errors
	 	 * @param request
		 * 
		 * @return Devuelve un int.
		 */
		public int validarCampos(ActionErrors errors,HttpServletRequest request){
			PropertyValidator validar= new PropertyValidatorImpl();
			HttpSession session = request.getSession();
			if(request.getParameter("grabarCondiciones")!=null){ 

				Collection calendarioDetalleDiaLocalDTOCol=(Collection)session.getAttribute("ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1");
				LogSISPE.getLog().info("coleccionNueva: {}" , calendarioDetalleDiaLocalDTOCol.size());
				int index=0;
				
				for(Iterator i = calendarioDetalleDiaLocalDTOCol.iterator(); i.hasNext();){
					validar.validateMandatory(errors, "conceptoMovimiento", this.conceptoMovimiento[index],"errors.conceptos.requerido",new Integer(index + 1));
					CalendarioDetalleDiaLocalDTO calendarioDetalleDiaLocalDTO=(CalendarioDetalleDiaLocalDTO)i.next();
					calendarioDetalleDiaLocalDTO.setConceptoMovimiento(this.conceptoMovimiento[index]);
					index++;
				}
			}	
			LogSISPE.getLog().info("errores: {}", errors.size());
			return errors.size();
		}

		public Collection opcionesHoras(HttpSession session)
		{
			
			int horanInicio = MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.hora.inicio");
			int horaFin = MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.hora.fin");;
			
			Calendar calIni = Calendar.getInstance();
			calIni.set(Calendar.HOUR_OF_DAY,horanInicio);		
			Calendar calFin = Calendar.getInstance();
			calFin.set(Calendar.HOUR_OF_DAY,horaFin);
			
			Date horaI = null;
			Date horaF = null;
			String horaInicial = null;
			String horaFinal = null;
			
			
			Collection<HorasDTO> opcionesHorasCol=new ArrayList<HorasDTO>();
			HorasDTO horasDiaDTO=new HorasDTO();
			int cont=0;
			for(int i=calIni.get(Calendar.HOUR_OF_DAY); i<=calFin.get(Calendar.HOUR_OF_DAY);i++){			
				
				horasDiaDTO=new HorasDTO();				
				
				calIni.set(Calendar.MINUTE, 0);
				horaI =calIni.getTime();
				horaInicial = new SimpleDateFormat("HH:mm").format(horaI);
				
				
				calIni.set(Calendar.MINUTE,59);
				horaF=calIni.getTime();
				horaFinal=new SimpleDateFormat("HH:mm").format(horaF);
				
				horasDiaDTO.setHoras(horaInicial+" - "+horaFinal);
				horasDiaDTO.setSeleccion(new Integer(cont)+","+horaInicial);
				
				LogSISPE.getLog().info(horaInicial+" - "+horaFinal);
				
				opcionesHorasCol.add(horasDiaDTO);
				calIni.add(Calendar.HOUR_OF_DAY, 1);
				cont++;
				
			}
			
			//utilizo un object[] 
			Object[] horasDiaDTOObj=opcionesHorasCol.toArray();
			session.setAttribute(HORASDIAS,horasDiaDTOObj);
			
			return(opcionesHorasCol);
		}
		
	/**
	 * @return Devuelve cantidad.
	 */
	public String getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad El cantidad a establecer.
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Devuelve concepto.
	 */
	public String getConcepto() {
		return concepto;
	}
	/**
	 * @param concepto El concepto a establecer.
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	
	
	// IMPORTANTE: Se comentan m\u00E9todos GET y SET para properties de fecha
	// ------------------------------------------------------------------
	/**
	 * @return Devuelve horas.
	 */
	/*public String getHoras() {
		return horas;
	}*/
	/**
	 * @param horas El horas a establecer.
	 */
	/*public void setHoras(String horas) {
		this.horas = horas;
	}*/
	/**
	 * @return Devuelve minutos.
	 */
	/*public String getMinutos() {
		return minutos;
	}*/
	/**
	 * @param minutos El minutos a establecer.
	 */
	/*public void setMinutos(String minutos) {
		this.minutos = minutos;
	}*/
	/**
	 * @return Devuelve segundos.
	 */
	/*public String getSegundos() {
		return segundos;
	}*/
	/**
	 * @param segundos El segundos a establecer.
	 */
	/*public void setSegundos(String segundos) {
		this.segundos = segundos;
	}*/
	// ------------------------------------------------------------------
	
	/**
	 * @return Devuelve motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo El motivo a establecer.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	/**
	 * @return Devuelve tipoMovimiento.
	 */
	public String getTipoMovimiento() {
		return tipoMovimiento;
	}
	/**
	 * @param tipoMovimiento El tipoMovimiento a establecer.
	 */
	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	
	
	
	/**
	 * @return Devuelve cantidadAlmacenamientoNueva.
	 */
	public String[] getCantidadAlmacenamientoNueva() {
		return cantidadAlmacenamientoNueva;
	}
	/**
	 * @param cantidadAlmacenamientoNueva El cantidadAlmacenamientoNueva a establecer.
	 */
	public void setCantidadAlmacenamientoNueva(
			String[] cantidadAlmacenamientoNueva) {
		this.cantidadAlmacenamientoNueva = cantidadAlmacenamientoNueva;
	}
	/**
	 * @return Devuelve conceptoMovimiento.
	 */
	public String[] getConceptoMovimiento() {
		return conceptoMovimiento;
	}
	/**
	 * @param conceptoMovimiento El conceptoMovimiento a establecer.
	 */
	public void setConceptoMovimiento(String[] conceptoMovimiento) {
		this.conceptoMovimiento = conceptoMovimiento;
	}

	public Object[] getHorasDia() {
		return horasDia;
	}

	public void setHorasDia(Object[] horasDia) {
		this.horasDia = horasDia;
	}

	public Collection getTransporte() {
		return transporte;
	}

	public void setTransporte(Collection transporte) {
		this.transporte = transporte;
	}

	public String getHoraSeleccionada() {
		return horaSeleccionada;
	}

	public void setHoraSeleccionada(String horaSeleccionada) {
		this.horaSeleccionada = horaSeleccionada;
	}

	public String getTransporteSeleccionado() {
		return transporteSeleccionado;
	}

	public void setTransporteSeleccionado(String transporteSeleccionado) {
		this.transporteSeleccionado = transporteSeleccionado;
	}

	public String getNumCamiones() {
		return numCamiones;
	}

	public void setNumCamiones(String numCamiones) {
		this.numCamiones = numCamiones;
	}
}
