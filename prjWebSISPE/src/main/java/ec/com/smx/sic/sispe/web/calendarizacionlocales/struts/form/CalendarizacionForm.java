/*
 * Creado el 17/04/2007
 *
 * CalendarizacionForm.java
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.validator.PropertyValidator;
import ec.com.smx.framework.validator.PropertyValidatorImpl;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.commons.exception.SISPEException;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioColorDTO;
import ec.com.smx.sic.sispe.dto.CalendarioDetallePlantillaLocalDTO;
import ec.com.smx.sic.sispe.dto.CalendarioPlantillaLocalDTO;
import ec.com.smx.sic.sispe.web.calendarizacionlocales.struts.action.CalendarizacionAction;
import ec.com.smx.sic.sispe.web.reportes.dto.OrdenDiasSemanaDTO;
import ec.com.smx.sic.sispe.web.reportes.dto.HorasDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;


/**
 * <p>
 * Esta clase permite realizar la calendarizacion para los locales.
 * </p>
 * 
 * @author jacalderon
 * @version 1.0
 * @since 	JSDK 1.4.2 
 */

@SuppressWarnings("serial")
public class CalendarizacionForm extends ActionForm 
{
	/**
	   * Campos para realizar la b\u00FAsqueda por fechas para cargar el calendario
	   * <ul>
	   * <li>String buscaMes: Campo que donde se seleccionara el mes de busqueda</li>
	   * <li>String buscaAnio: Campo donde se ingresar\u00E1 el a\u00F1o de busqueda</li>
	   * <li>String buscaFecha: Campo donde se ingresar\u00E1 el c\u00F3digo de art\u00EDculo para la b\u00FAsqueda</li>
	   * <ul>
	   */
	
		private String buscaMes;
		private String buscaAnio;
		private String buscaFecha;
		private String botonBuscar;

	  /**
	   * Campos para confirmaci\u00F3n en la ventanas emergentes
	   * <ul>
	   * <li>String botonSi</li>
	   * <li>String botonNo</li>
	   * <ul>
	   */

		private String botonSi;
		private String botonNo;

		//selecciona todos los dias del calendario
		private String seleccionarTodos;
		//seleccion de una semana
		private String [] filaSeleccionada;
		//check para los criterios de busqueda entra fechas
		private String opcionBusqueda;
		
		
		//checks para seleccionar los locales desde la pantalla de administracion
		private String todo;
		private String[] seleccionados;
		private String ciudades;
		
		
	  /**
	   * Campos para confirmaci\u00F3n en la ventanas emergentes
	   * <ul>
	   * <li>String nombrePlantilla: Nombre de la plantilla</li>
 	   * <li>String coloPrincipalPlantilla: Color de la plantilla</li>
  	   * <li>boolean estadoPlantilla: Estado la la plantilla por defecto</li>
	   * <li>String diaDetalle: Campo donde se selecciona un dia de la semana</li>
  	   * <li>String capacidadNormal: Campo donde se registra la capacidad normal</li>
  	   * <li>String capacidadAdicional: Campo donde se registra la capacidad adicional</li>
	   * 
	   * <ul>
	   */

		//cabecera de la plantilla
		private String nombrePlantilla;
		private String coloPrincipalPlantilla;
		private boolean estadoPlantilla;
		
		//detalle de la plantilla
		private String diaSeleccionado;
		private String horaSeleccionada;
		private String transporteSeleccionado;
		private String cn;
		private String ca;
		
	 /**
	   * Campos para iterar los objetos que son object
	   * <ul>
	   * <li>String calendarioDiaLocal: Calendario</li>
 	   * <li>String colores: Colores</li>
  	   * <li>boolean dias: Dias de la senana</li>
	   * 
	   * <ul>
	   */
		private Object[] calendarioDiaLocal;
		private Object[] colores;
		private Object[] dias;
		//meses del a\u00F1o
		private Object[] horasDia;
		private Collection transporte ;
		private static final String[] MESES ={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiempre","Octubre","Noviembre","Diciembre"};
		
	 /**
	   * Campos utilizados para realizar ajustes de capacidad desde la administracion de plantillas
	   * <ul>
	   * <li>String ajusteCapacidadNormal: Caja de texto para ingresar CN</li>
 	   * <li>String ajusteCapacidadAdicional: Caja de texto para ingresar CA</li>
 	   * <li>String fechaInicial: Caja de texto para ingresar la fecha inicial</li>
 	   * <li>String fechaFinal: Caja de texto para ingresar la fecha final</li> 
 	   * <li>String todoA: Check que indica si se va o no a seleccionar todos locales</li>
 	   * <li>String seleccionadosA: Check de seleccion de los locales a los que se les va a aplicar ajuste</li>
 	   * <li>String ciudadesA: Compo de ciudades para consultar los locales</li>
 	   *    
	   * <ul>
	   */
		
		private String ajusteCapacidadNormal;
		private String ajusteCapacidadAdicional;
		private String conceptoMovimiento;
		private String fechaInicial;
		private String fechaFinal;
		private String todoA;
		private String[] seleccionadosA;
		private String ciudadesA;
		
		
	
	/**
	   * Resetea los controles del formulario de la p\u00E1gina <code>filtrosDespachoReservacion.jsp</code>, en cada petici\u00F3n.
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   */
	  public void reset(ActionMapping mapping, HttpServletRequest request) 
	  {
	    
	    this.buscaMes=null;
	    this.buscaAnio=null;
	    this.buscaFecha=null;
	    this.botonBuscar=null;
	    this.botonSi=null;
	    this.botonNo=null;
	    this.nombrePlantilla=null;
		this.coloPrincipalPlantilla=null;
	    this.seleccionarTodos="";
	    this.calendarioDiaLocal=new Object[35];
	    this.colores=new Object[]{};
	    this.dias=new Object[]{};
	    this.horasDia=new Object[]{};
	    this.cn=null;
	    this.ca=null;
	    this.diaSeleccionado=null;
	    this.horaSeleccionada=null;
	    this.estadoPlantilla=false;
	    this.filaSeleccionada=new String[]{"","","","","",""};
	    this.opcionBusqueda="fema";
	    this.todo=null;
	    this.seleccionados=null;
	    this.ciudades=null;
	    this.ajusteCapacidadNormal=null;
	    this.ajusteCapacidadAdicional=null;
	    this.fechaInicial=null;
	    this.fechaFinal=null;
	    this.todoA=null;
	    this.seleccionadosA=null;
	    this.ciudadesA=null;
	    this.conceptoMovimiento=null;		
		

	  }
	  /**
	   * Ejecuta la validacion en la p\u00E1gina JSP <code>filtrosDespachoReservacion.jsp</code>
	   * @param mapping		El mapeo utilizado para seleccionar esta instancia	
	   * @param request		La petici\u00F3n que se est\u00E1 procesando
	   * @return errors		Coleccion de errores producidos por la validaci\u00F3n, si no hubieron errores se retorna
	   * <code>null</code>
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    ActionErrors errors = new ActionErrors();
	    ActionMessages info = new ActionMessages();
	    PropertyValidator validar = new PropertyValidatorImpl();
	    HttpSession session = request.getSession();
	    
	    LogSISPE.getLog().info("boton buscar: {}" , request.getParameter("botonBuscarCalendar"));
	        
	    if (request.getParameter("agregarConfiguracion") != null) {
	    	validar.validateMandatory(errors, "diaSel", this.diaSeleccionado,"errors.diaSel.requerido");
	    	validar.validateLong(errors, "cn", this.cn, true, 1L, Long.MAX_VALUE, "error.validacion.cn.invalido");
	    	
	    	//IMPORTANTE: Se comenta la validaci\u00F3n para el campode Cantidad Adicional
	    	//validar.validateLong(errors, "ca", this.ca, true, 0L, Long.MAX_VALUE, "error.validacion.ca.invalido");
	    }
	    else if(request.getParameter("guardarPlantilla")!=null){
	    	validar.validateMandatory(errors, "nombrePlantilla", this.nombrePlantilla,"errors.nombrePlantilla.requerido");
	    	
	    	if(request.getParameter("adminPlantillas")!=null){
	    		if(session.getAttribute(CalendarizacionAction.CALENDARIOPLANTILLALOCALDTO)==null){
	    			errors.add("configuracionPlantilla",new ActionMessage("error.configuracionPlantilla.requerido"));
	    		}
	    	}else{
	    		if(session.getAttribute(CalendarizacionAction.CALENDARIOPLANTILLALOCALDTONUEVA)==null){
	    			errors.add("configuracionPlantilla",new ActionMessage("error.configuracionPlantilla.requerido"));
	    		}
	    	}
	    	
	    	
    	}
	    	
	    else if(request.getParameter("botonBuscarCalendar")!=null){
	    	LogSISPE.getLog().info("mes:  {}" , this.buscaMes);
		    LogSISPE.getLog().info("a\u00F1o: {}" , this.buscaAnio);
		    LogSISPE.getLog().info("fecha: {}" , this.buscaFecha);
	    	if((this.buscaAnio==null || this.buscaAnio.equals("")) && (this.buscaMes==null || this.buscaMes.equals("")) && (this.buscaFecha==null || this.buscaFecha.equals("")))
	    		errors.add("buscar",new ActionMessage("error.buscarFecha.requerido"));
	    	else if((this.buscaAnio!=null || !this.equals("")) && (this.buscaMes==null || this.buscaMes.equals("")) && (this.opcionBusqueda.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaMesAno")))){
	    		validar.validateFormato(errors,"buscaAnio",this.buscaAnio,false,"^\\d{4}","errors.formato.buscaAnio");
	    		Calendar cal = Calendar.getInstance();
	    		this.buscaMes=new Integer(cal.get(Calendar.MONTH)).toString();
	    	}
	    	else if ((this.buscaMes!=null || !this.equals("")) && (this.buscaAnio==null || this.buscaAnio.equals("")) && (this.opcionBusqueda.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaMesAno")))){
	    		Calendar cal = Calendar.getInstance();
	    		this.buscaAnio=new Integer(cal.get(Calendar.YEAR)).toString();
	    	}
	    	else if(this.buscaFecha!=null && !this.buscaFecha.equals("") && (this.opcionBusqueda.equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.opcion.fechaEspecifica"))))
	    		validar.validateFecha(errors,"buscaFecha",this.buscaFecha,false,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha");
	    	LogSISPE.getLog().info("mes:  {}", this.buscaMes);
		    LogSISPE.getLog().info("a\u00F1o: {}", this.buscaAnio);
		    LogSISPE.getLog().info("fecha: {}", this.buscaFecha);
	    }
	    /*******************************CARGA DE SESSIONES EXISTENTES****************************************/
	    if(session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol")!=null){
		 	this.colores=((Object[])session.getAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol"));
		 }
		 
		 this.dias=((Object[])session.getAttribute("ec.com.smx.calendarizacion.opcionesDiasOBJ"));
		 
		 this.horasDia=((Object[])session.getAttribute("ec.com.smx.calendarizacion.opcionesHorasDiaOBJ"));
		 		 		 
		// this.estadoPlantilla=(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.plantilla.defecto"));
		 
		 if(session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL)!=null)
		 	this.calendarioDiaLocal=((Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL));		
		 
	    LogSISPE.getLog().info("numero de errores: {}", errors.size());
	    
	    return(errors);
	    }
	  
	  /**
		 * Cargar lista de dias para la configuracion de la plantilla
		 * @param dias
		 * @param session
		 * @return
		 */
		
		public Collection opcionesDias(String[] dias,HttpSession session)
		{
			//La primera opcion en la lista va a  ser para todos los dias
			Collection<OrdenDiasSemanaDTO> opcionesDiasCol=new ArrayList<OrdenDiasSemanaDTO>();
			OrdenDiasSemanaDTO ordenDiasSemanaDTO=new OrdenDiasSemanaDTO();
			ordenDiasSemanaDTO.setNombreDia("Todos");
			ordenDiasSemanaDTO.setSeleccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"));
			opcionesDiasCol.add(ordenDiasSemanaDTO);
			
			//Cargo a la lista los dias en el orden predeterminado
			for(int dia=0;dia<dias.length;dia++){
				ordenDiasSemanaDTO=new OrdenDiasSemanaDTO();//DTO no persistente
				ordenDiasSemanaDTO.setNombreDia(dias[dia]);
				ordenDiasSemanaDTO.setSeleccion(new Integer(dia + 1).toString());
				opcionesDiasCol.add(ordenDiasSemanaDTO);
			}
			
			/* La \u00FAltima opcion en la lista es RESTO: todos los dias que no fueron seleccionados en la lista
			 * Para grabar una plantilla debe ser elegido RESTO obligatoriamente o la opcion de TODOS
			 */
			
			ordenDiasSemanaDTO=new OrdenDiasSemanaDTO();
			ordenDiasSemanaDTO.setNombreDia("Resto");
			ordenDiasSemanaDTO.setSeleccion(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.restoDeDias"));
			opcionesDiasCol.add(ordenDiasSemanaDTO);
			
			//utilizo un object[] para poder buscar el nombre del dia
			Object[] ordenDiasSemanaDTOObj=opcionesDiasCol.toArray();
			session.setAttribute("ec.com.smx.calendarizacion.opcionesDiasOBJ",ordenDiasSemanaDTOObj);
			
			return(opcionesDiasCol);
		}
		

		 /**
		 * Cargar lista de horas para la configuracion de la plantilla
		 * @param horas
		 * @param session
		 * @return
		 */
		
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
			session.setAttribute("ec.com.smx.calendarizacion.opcionesHorasDiaOBJ",horasDiaDTOObj);
			
			return(opcionesHorasCol);
		}
		
	  /**
		 * Busca si existe el dia seleccionado dentro de la coleccion
		 * @param calendarioPlantillaLocalDTO
		 * @param diaSeleccionado
		 * @param session
		 * @return
		 */
		
		public String opcionesDiasModificado(CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO,String diaSeleccionado,HttpSession session) 
		{
			/* Esta funci\u00F3n busca si ya existe una configuraci\u00F3n creada para el dia seleccionado:
			 * Si existe devuelve la ubicaci\u00F3n del registro para preguntar si desea modificarlo o no
			 */
			session.removeAttribute("ec.com.smx.calendarizacion.filaTodos");
			LogSISPE.getLog().info("entro a verificar si el dia existe: {}" , diaSeleccionado);
			String seleccionado=null; //ubicacion del registro en caso de que exista la configuracion
			int contador=0; //contador de filas para ubicar el registro
			LogSISPE.getLog().info("dia seleccionado: {}" , diaSeleccionado);
			for(Iterator numero=calendarioPlantillaLocalDTO.getCalendarioDetallesPlantillaLocal().iterator(); numero.hasNext();){
				CalendarioDetallePlantillaLocalDTO calendarioDetallePlantillaLocalDTO = (CalendarioDetallePlantillaLocalDTO)numero.next();
				LogSISPE.getLog().info("dia a comparar: {}" , calendarioDetallePlantillaLocalDTO.getNumeroDia());
				//pregunta si el dia seleccionado existe y tiene configuracion activa
				if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(diaSeleccionado)){
					LogSISPE.getLog().info("entro a if");
					seleccionado=new Integer(contador).toString();
				}
				//***************** Si el dia seleccionados fue TODOS los guarda en session para cambiarlo por RESTO en caso de elegir una configuracion para otro dis****************
				if(!calendarioDetallePlantillaLocalDTO.getNpEstadoDetalle().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.estadoDetallePlantilla.anulado")) && calendarioDetallePlantillaLocalDTO.getNumeroDia().toString().equals(MessagesAplicacionSISPE.getString("ec.com.smx.sic.sispe.calendarizacion.detallePlantilla.todosLosDias"))){
					LogSISPE.getLog().info("dia todos");
					session.setAttribute("ec.com.smx.calendarizacion.filaTodos",new Integer(contador));
				}
				contador++;
			}
			LogSISPE.getLog().info("fila: {}", seleccionado);
			return(seleccionado);
		}
		
	  /**
		 * Devuelve la paleta de colores
		 * @param session
		 * @param request
		 * @param calendarioPlantillaLocalDTO
		 * @param errors
		 * @throws Exception
		 */
		
		public void paleta(HttpSession session,CalendarioPlantillaLocalDTO calendarioPlantillaLocalDTO, ActionMessages errors) throws Exception
		{
			try{
				//carga de localID para buscar los colores
				LocalID localID=(LocalID)session.getAttribute("ec.com.smx.calendarizacion.localID");
				//carga la paleta de colores
				Collection calendarioColorDTOCol=new ArrayList();
				LogSISPE.getLog().info("va a cargar la paleta de colores");
				LogSISPE.getLog().info("calendarioPlantillaLocalDTO: {}" , calendarioPlantillaLocalDTO);
				//Metodo para obtener los colores disponibles 
				calendarioColorDTOCol=SessionManagerSISPE.getServicioClienteServicio().transObtenerColoresPorLocal(localID,calendarioPlantillaLocalDTO);
				
				//utilizo un object[] para poder iterar en la jsp
				Object[] calendarioColorDTOObj=calendarioColorDTOCol.toArray();
				session.setAttribute("ec.com.smx.calendarizacion.calendarioColorDTOCol",calendarioColorDTOObj);
				
				LogSISPE.getLog().info("numero de colores: {}", calendarioColorDTOObj.length);
				if (calendarioColorDTOObj.length==0)
					errors.add("sinColores",new ActionMessage("errors.sinColores"));
				else{
					//Guardo el primer color para desplegar un color siempre por defecto
					CalendarioColorDTO calendarioColorDTO1=(CalendarioColorDTO)calendarioColorDTOObj[0];
					session.setAttribute("ec.com.smx.calendarizacion.calendarioColorDTO",calendarioColorDTO1);
				}
			}catch(SISPEException e){
				errors.add("cargarPaleta",new ActionMessage("error.cargarPaleta",e.getStackTrace()));
			}
		}
		
		/**
		 * Valida los campos del ajuste 
		 * @param request
		 * @param errors
		 * @return
		 * @throws Exception
		 */
		//JACC
		public int validarIngresoAjustes(HttpServletRequest request, ActionErrors errors) throws Exception
		{
			PropertyValidator validador= new PropertyValidatorImpl();
			if((this.ajusteCapacidadAdicional==null || this.ajusteCapacidadAdicional.equals("")) && (this.ajusteCapacidadNormal==null || this.ajusteCapacidadNormal.equals(""))){
				errors.add("ajustes",new ActionMessage("error.ingreso.ajustes"));
			}
			if(this.ajusteCapacidadNormal!=null)
				validador.validateLong(errors, "ajusteCapacidadNormal", this.ajusteCapacidadNormal, false, 1L, Long.MAX_VALUE, "error.validacion.capacidadNormal.invalido");
			if(this.ajusteCapacidadAdicional!=null)
				validador.validateLong(errors, "ajusteCapacidadAdicional", this.ajusteCapacidadAdicional, false, 1L, Long.MAX_VALUE, "error.validacion.capacidadAdicional.invalido");

	    	validador.validateMandatory(errors, "conceptoMovimiento", this.conceptoMovimiento,"errors.concepto.requerido");
	    	
	    	validador.validateFecha(errors,"fechaInicial",this.getFechaInicial(),true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha inicial");
	    	validador.validateFecha(errors,"fechaFinal",this.getFechaFinal(),true,MessagesWebSISPE.getString("formatos.fecha"),"errors.formato.fecha","errors.requerido","Fecha final");
	    	
	    	if(this.seleccionadosA==null || this.seleccionadosA.length==0)
	    		errors.add("seleccionLocal",new ActionMessage("errors.seleccionar.local"));
	    	
	    	if(errors.size()==0){
	    		long resC=ConverterUtil.returnDateDiff(this.fechaInicial, this.fechaFinal);
	    		LogSISPE.getLog().info("******diferencia ****** {}", resC);
	    		if(resC==-1){
					errors.add("fechas",new ActionMessage("errors.fecha.menor.fechas"));
				}
	    	}
			return errors.size();
		}
	  
		/**
		 * @return Devuelve botonBuscar.
		 */
		public String getBotonBuscar() {
			return botonBuscar;
		}
		/**
		 * @param botonBuscar El botonBuscar a establecer.
		 */
		public void setBotonBuscar(String botonBuscar) {
			this.botonBuscar = botonBuscar;
		}
		/**
		 * @return Devuelve buscaAnio.
		 */
		public String getBuscaAnio() {
			return buscaAnio;
		}
		/**
		 * @param buscaAnio El buscaAnio a establecer.
		 */
		public void setBuscaAnio(String buscaAnio) {
			this.buscaAnio = buscaAnio;
		}
		/**
		 * @return Devuelve buscaFecha.
		 */
		public String getBuscaFecha() {
			return buscaFecha;
		}
		/**
		 * @param buscaFecha El buscaFecha a establecer.
		 */
		public void setBuscaFecha(String buscaFecha) {
			this.buscaFecha = buscaFecha;
		}
	/**
	 * @return Devuelve buscaMes.
	 */
	public String getBuscaMes() {
		return buscaMes;
	}
	/**
	 * @param buscaMes El buscaMes a establecer.
	 */
	public void setBuscaMes(String buscaMes) {
		this.buscaMes = buscaMes;
	}
	/**
	 * @return Devuelve meses.
	 */
	public String[] getMeses() {
		return MESES;
	}
	
		/**
		 * @return Devuelve filaSeleccionada.
		 */
		public String[] getFilaSeleccionada() {
			return filaSeleccionada;
		}
		/**
		 * @param filaSeleccionada El filaSeleccionada a establecer.
		 */
		public void setFilaSeleccionada(String[] filaSeleccionada) {
			this.filaSeleccionada = filaSeleccionada;
		}
		
		/**
		 * @return Devuelve seleccionarTodos.
		 */
		public String getSeleccionarTodos() {
			return seleccionarTodos;
		}
		/**
		 * @param seleccionarTodos El seleccionarTodos a establecer.
		 */
		public void setSeleccionarTodos(String seleccionarTodos) {
			this.seleccionarTodos = seleccionarTodos;
		}
		
		/**
		 * @return Devuelve botonNo.
		 */
		public String getBotonNo() {
			return botonNo;
		}
		/**
		 * @param botonNo El botonNo a establecer.
		 */
		public void setBotonNo(String botonNo) {
			this.botonNo = botonNo;
		}
		/**
		 * @return Devuelve botonSi.
		 */
		public String getBotonSi() {
			return botonSi;
		}
		/**
		 * @param botonSi El botonSi a establecer.
		 */
		public void setBotonSi(String botonSi) {
			this.botonSi = botonSi;
		}
		
		/**
		 * @return Devuelve coloPrincipalPlantilla.
		 */
		public String getColoPrincipalPlantilla() {
			return coloPrincipalPlantilla;
		}
		/**
		 * @param coloPrincipalPlantilla El coloPrincipalPlantilla a establecer.
		 */
		public void setColoPrincipalPlantilla(String coloPrincipalPlantilla) {
			this.coloPrincipalPlantilla = coloPrincipalPlantilla;
		}
		/**
		 * @return Devuelve nombrePlantilla.
		 */
		public String getNombrePlantilla() {
			return nombrePlantilla;
		}
		/**
		 * @param nombrePlantilla El nombrePlantilla a establecer.
		 */
		public void setNombrePlantilla(String nombrePlantilla) {
			this.nombrePlantilla = nombrePlantilla;
		}
		/**
		 * @return Devuelve calendarioDiaLocal.
		 */
		public Object[] getCalendarioDiaLocal() {
			return calendarioDiaLocal;
		}
		/**
		 * @param calendarioDiaLocal El calendarioDiaLocal a establecer.
		 */
		public void setCalendarioDiaLocal(Object[] calendarioDiaLocal) {
			this.calendarioDiaLocal = calendarioDiaLocal;
		}
		
		/**
		 * @return Devuelve colores.
		 */
		public Object[] getColores() {
			return colores;
		}
		/**
		 * @param colores El colores a establecer.
		 */
		public void setColores(Object[] colores) {
			this.colores = colores;
		}
		
		/**
		 * @return Devuelve ca.
		 */
		public String getCa() {
			return ca;
		}
		/**
		 * @param ca El ca a establecer.
		 */
		public void setCa(String ca) {
			this.ca = ca;
		}
		/**
		 * @return Devuelve cn.
		 */
		public String getCn() {
			return cn;
		}
		/**
		 * @param cn El cn a establecer.
		 */
		public void setCn(String cn) {
			this.cn = cn;
		}
		
		
		
		
		/**
		 * @return Devuelve diaSeleccionado.
		 */
		public String getDiaSeleccionado() {
			return diaSeleccionado;
		}
		/**
		 * @param diaSeleccionado El diaSeleccionado a establecer.
		 */
		public void setDiaSeleccionado(String diaSeleccionado) {
			this.diaSeleccionado = diaSeleccionado;
		}
		
		/**
		 * @return Devuelve estadoPlantilla.
		 */
		public boolean isEstadoPlantilla() {
			return estadoPlantilla;
		}
		/**
		 * @param estadoPlantilla El estadoPlantilla a establecer.
		 */
		public void setEstadoPlantilla(boolean estadoPlantilla) {
			this.estadoPlantilla = estadoPlantilla;
		}
		
		/**
		 * @return Devuelve dias.
		 */
		public Object[] getDias() {
			return dias;
		}
		/**
		 * @param dias El dias a establecer.
		 */
		public void setDias(Object[] dias) {
			this.dias = dias;
		}
		
		/**
		 * @return Devuelve opcionBusqueda.
		 */
		public String getOpcionBusqueda() {
			return opcionBusqueda;
		}
		/**
		 * @param opcionBusqueda El opcionBusqueda a establecer.
		 */
		public void setOpcionBusqueda(String opcionBusqueda) {
			this.opcionBusqueda = opcionBusqueda;
		}
		/**
		 * @return el seleccionados
		 */
		public String[] getSeleccionados() {
			return this.seleccionados;
		}
		/**
		 * @param seleccionados el seleccionados a establecer
		 */
		public void setSeleccionados(String[] seleccionados) {
			this.seleccionados = seleccionados;
		}
		/**
		 * @return el todo
		 */
		public String getTodo() {
			return this.todo;
		}
		/**
		 * @param todo el todo a establecer
		 */
		public void setTodo(String todo) {
			this.todo = todo;
		}
		/**
		 * @return el ciudades
		 */
		public String getCiudades() {
			return this.ciudades;
		}
		/**
		 * @param ciudades el ciudades a establecer
		 */
		public void setCiudades(String ciudades) {
			this.ciudades = ciudades;
		}
		public String getAjusteCapacidadAdicional() {
			return ajusteCapacidadAdicional;
		}
		public void setAjusteCapacidadAdicional(String ajusteCapacidadAdicional) {
			this.ajusteCapacidadAdicional = ajusteCapacidadAdicional;
		}
		public String getAjusteCapacidadNormal() {
			return ajusteCapacidadNormal;
		}
		public void setAjusteCapacidadNormal(String ajusteCapacidadNormal) {
			this.ajusteCapacidadNormal = ajusteCapacidadNormal;
		}
		public String getCiudadesA() {
			return ciudadesA;
		}
		public void setCiudadesA(String ciudadesA) {
			this.ciudadesA = ciudadesA;
		}
		public String getFechaFinal() {
			return fechaFinal;
		}
		public void setFechaFinal(String fechaFinal) {
			this.fechaFinal = fechaFinal;
		}
		public String getFechaInicial() {
			return fechaInicial;
		}
		public void setFechaInicial(String fechaInicial) {
			this.fechaInicial = fechaInicial;
		}
		public String[] getSeleccionadosA() {
			return seleccionadosA;
		}
		public void setSeleccionadosA(String[] seleccionadosA) {
			this.seleccionadosA = seleccionadosA;
		}
		public String getTodoA() {
			return todoA;
		}
		public void setTodoA(String todoA) {
			this.todoA = todoA;
		}
		public String getConceptoMovimiento() {
			return conceptoMovimiento;
		}
		public void setConceptoMovimiento(String conceptoMovimiento) {
			this.conceptoMovimiento = conceptoMovimiento;
		}
		public String getHoraSeleccionada() {
			return horaSeleccionada;
		}
		public void setHoraSeleccionada(String horaSeleccionada) {
			this.horaSeleccionada = horaSeleccionada;
		}
		public Object[] getHorasDia() {
			return horasDia;
		}
		public void setHorasDia(Object[] horasDia) {
			this.horasDia = horasDia;
		}
		
		public String getTransporteSeleccionado() {
			return transporteSeleccionado;
		}
		public void setTransporteSeleccionado(String transporteSeleccionado) {
			this.transporteSeleccionado = transporteSeleccionado;
		}
		public Collection getTransporte() {
			return transporte;
		}
		public void setTransporte(Collection transporte) {
			this.transporte = transporte;
		}
}
