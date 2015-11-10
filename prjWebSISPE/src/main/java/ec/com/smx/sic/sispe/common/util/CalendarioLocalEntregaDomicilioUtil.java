package ec.com.smx.sic.sispe.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.corporativo.admparamgeneral.dto.id.LocalID;
import ec.com.smx.corpv2.common.util.CorporativoConstantes;
import ec.com.smx.corpv2.dto.PersonaDTO;
import ec.com.smx.framework.common.util.ConverterUtil;
import ec.com.smx.framework.common.validator.Validator;
import ec.com.smx.framework.common.validator.ValidatorImpl;
import ec.com.smx.sic.mercancias.commons.factory.MercanciasFactory;
import ec.com.smx.sic.mercancias.commons.util.MercanciasConstantes;
import ec.com.smx.sic.mercancias.dto.InventarioMercanciaLocalDTO;
import ec.com.smx.sic.mercancias.est.CalendarioMesEST;
import ec.com.smx.sic.mercancias.est.ResumenDiaEST;
import ec.com.smx.sic.sispe.common.constantes.GlobalsStatics;
import ec.com.smx.sic.sispe.commons.factory.SISPEFactory;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.commons.util.MessagesAplicacionSISPE;
import ec.com.smx.sic.sispe.dto.CalendarioDiaLocalDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.action.EntregaLocalCalendarioAction;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.CotizarReservarForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

//Funciones Calendario SICMER
public class CalendarioLocalEntregaDomicilioUtil {
	
	/**
	 * Clase ofrece: 
	 * aesanchez
	 * 15:23:23
	 * version 0.1
	 *  	
	 */

	// variables de sesion para las horas desde hasta que se muestran en los combos del calendario SICMER	
	public final static String HORAS_DIA_DESDE = GlobalsStatics.PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".horasDiaDesde");
	public final static String HORAS_DIA_HASTA = GlobalsStatics.PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".horasDiaHasta");
	// mensaje cuando se configuran entregas fuera de un horario permitido de 20 a 24
	public final static String MENSAJE_HORAS_INVALIDAS = GlobalsStatics.PREFIJO_VARIABLE_SESION.concat("calendarioEntregasSicmer").concat(".horasInvalidas");
	private static Calendar c = new GregorianCalendar();
	private static Calendar fecha=Calendar.getInstance();
	
	public static void accionesCalendario(HttpServletRequest request, ActionMessages errors,ActionMessages infos, CotizarReservarForm formulario) throws Exception{
		
		Validator validator=new ValidatorImpl();
		
		//tomamos la fecha minima de entrega 
		fecha.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(formulario.getBuscaFecha()));
		LogSISPE.getLog().info("Fecha para el calendario BuscaFecha: "+formulario.getBuscaFecha());
		HttpSession session = request.getSession();
		//parametro para abrir 
		if(request.getParameter("accioncalendario").equals("abrir")){
			mostrarCalendario(session, formulario,fecha, request, errors);
		}else if(request.getParameter("accioncalendario").equals("mesanterior")){
			mesAnterior(request,errors);
		}else if(request.getParameter("accioncalendario").equals("mesposterior")){
			mesPosterior(request,errors);			
		}//Cuando se selecciona un dia del calendario
		else if(request.getParameter("accioncalendario").substring(0, 6).equals("verDia")){
			//se coloca el dia seleccionado
			verDia(request);
			//se carga los combos de rango de horas
			colocarRangoHorasNp(session,formulario);
		}else if(request.getParameter("accioncalendario").equals("cancelar")){
			LogSISPE.getLog().info("cerrar popup calendario SICMER");
			//se quita de session el calendario
			session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			session.removeAttribute(GlobalsStatics.POPUP_CALENDARIO);
			//si se cancela cuando no hay nada en rango de horas
			if(session.getAttribute(MENSAJE_HORAS_INVALIDAS)!=null){
				session.removeAttribute(MENSAJE_HORAS_INVALIDAS);
				if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
					formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
					formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
					formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
					formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
					formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
					formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
				}
			}
				formulario.setNpHoraDesde(null);
				formulario.setNpHoraHasta(null);
				formulario.setHoras(null);	
				formulario.setMinutos(null);
				formulario.setHorasMinutos(null);
		}//Cuando se acepta la fecha del calendario
		else if(request.getParameter("accioncalendario").equals("colocarfecha")){
			LogSISPE.getLog().info("Colocar fecha y cerrar calendario sicmer");
			
			ResumenDiaEST diaActual = (ResumenDiaEST)session.getAttribute(GlobalsStatics.RESUMEN_DIA_EST);
			formulario.setFechaEntregaCliente(ConverterUtil.parseDateToString(diaActual.getFecha().getTime()));
			LogSISPE.getLog().info("Fecha Entrega Cliente a colocar: "+diaActual.getFecha().getTime());	
			//se coloca el rango de horas, la hora en session y se quita de session el calendario
			LogSISPE.getLog().info("Rango de Horas : "+formulario.getNpHoraDesde()+" - "+formulario.getNpHoraHasta());
			formulario.setHoraDesde(formulario.getNpHoraDesde());
			formulario.setHoraHasta(formulario.getNpHoraHasta());
			String[] horaSeleccionada =  formulario.getHoraDesde().concat(":00").split(":");
			session.setAttribute(EntregaLocalCalendarioAction.HORA_SELECCIONADA,horaSeleccionada);
			session.removeAttribute(GlobalsStatics.POPUP_CALENDARIO);
			//si se acepta cuando no hay nada en rango de horas 
			if(session.getAttribute(MENSAJE_HORAS_INVALIDAS)!=null){
				session.removeAttribute(MENSAJE_HORAS_INVALIDAS);
				if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
					formulario.setDireccion(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getDireccion());
					formulario.setCodigoVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getCodigoVendedorSicmer());
					formulario.setNombreVendedorSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNombreVendedorSicmer());
					formulario.setNumeroDocumentoSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getNumeroDocumentoSicmer());
					formulario.setSeleccionCiudad(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getSeleccionCiudad());
					formulario.setQuienRecibeSicmer(((CotizarReservarForm)session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)).getQuienRecibeSicmer());
				}
			}
			//cargo los datos del formulario en session
			CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
			session.setAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER,formularioSicmer);

			//cuando se acepta y el stock es de cd hay calendario de bodega de por medio
			if(session.getAttribute(EntregaLocalCalendarioAction.SELECCIONARCALENDARIO)!=null){
				LogSISPE.getLog().info("va a setear fecha despacho calendario bodega");
				LocalID localID=new LocalID();
				if(session.getAttribute(EntregaLocalCalendarioAction.OPCIONCANASTOSESPECIALES).equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
					localID.setCodigoLocal(MessagesAplicacionSISPE.getInteger("ec.com.smx.sic.sispe.parametro.codigoCDCanastos"));
				}else{
					localID.setCodigoLocal(SessionManagerSISPE.getCurrentLocal(request));
				}
				LogSISPE.getLog().info("Codigo Local: "+localID.getCodigoLocal());
				localID.setCodigoCompania(SessionManagerSISPE.getCurrentCompanys(request).getId().getCodigoCompania());
				//si hay algun dia seleccionado anteriormente se deselecciona
				if(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)!=null){
					Object[] calendarioDiaLocalDTOObj=(Object[])session.getAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCALCOL);
					int diaSeleccionado= Integer.parseInt(String.valueOf(session.getAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO)));
					//Recupero el dia seleccionado anteriormente
					CalendarioDiaLocalDTO calendarioDiaLocalDTO1=(CalendarioDiaLocalDTO)calendarioDiaLocalDTOObj[diaSeleccionado];
					calendarioDiaLocalDTO1.setNpEsSeleccionado(false);
				}
				//Cargo el calendario	
				EntregaLocalCalendarioAction.obtenerLocal(request, localID, errors, formulario);
				
				session.removeAttribute(EntregaLocalCalendarioAction.DIASELECCIONADO);
				session.removeAttribute(EntregaLocalCalendarioAction.CALENDARIODIALOCAL);
				session.removeAttribute(EntregaLocalCalendarioAction.DIRECCION);
				session.setAttribute(EntregaLocalCalendarioAction.FECHAENTREGACLIENTE,formulario.getFechaEntregaCliente());
			}
		}else if(request.getParameter("accioncalendario").equals("programarfechas")){
			if(formulario.getOpElaCanEsp().equals(MessagesWebSISPE.getString("ec.com.smx.sic.sispe.entidadResponsable.bodega"))){
				colocarRangoHoras(session,formulario);
			}else{
				colocarRangoHorasNp(session,formulario);	
			}
		}else if(request.getParameter("accioncalendario").equals("consultarVendedor")){
			//se busca el vendedor, verifica si la cedula es valida
			LogSISPE.getLog().info("Busqueda vendedor en entrega a domicilio desde local");
			LogSISPE.getLog().info("Numero Documento Ingresado: "+formulario.getNumeroDocumentoSicmer());
			if(formulario.getNumeroDocumentoSicmer()!=null && validator.validateCedula( formulario.getNumeroDocumentoSicmer())){
				PersonaDTO personaDTO = new PersonaDTO();
				personaDTO.setNumeroDocumento(formulario.getNumeroDocumentoSicmer().trim());
				personaDTO.setEstadoPersona(CorporativoConstantes.ESTADO_ACTIVO);
				personaDTO = SISPEFactory.getDataService().findUnique(personaDTO);
				//si encuentra la persona se cargan los datos de nombre y codigo vendedor
				if(personaDTO!=null){
					LogSISPE.getLog().info("Codigo Persona Encontrado: "+personaDTO.getId().getCodigoPersona().toString());
					LogSISPE.getLog().info("Nombre Persona Encontrado: "+personaDTO.getNombreCompleto());
					formulario.setCodigoVendedorSicmer(personaDTO.getId().getCodigoPersona().toString());
					formulario.setNombreVendedorSicmer(personaDTO.getNombreCompleto());
					
					infos.add("", new ActionMessage("mensaje.VendedorSicmer",personaDTO.getNombreCompleto()));
				}else{
					LogSISPE.getLog().info("No se encontro persona");
					infos.add("DocumentoVendedor", new ActionMessage("mensaje.docVendedorSicmer"));
					formulario.setCodigoVendedorSicmer(null);
					formulario.setNombreVendedorSicmer(null);
				}
				//si no es valida la cedula o el campo esta vacio
			}else{
				LogSISPE.getLog().info("Cedula Invalida");
				errors.add("DocumentoVendedor", new ActionMessage("error.docVendedorSicmer.noValida"));
				formulario.setCodigoVendedorSicmer(null);
				formulario.setNombreVendedorSicmer(null);
			}
			
			if(session.getAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER)!=null){
				CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
				session.setAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER,formularioSicmer);
			}
		}
	}
	
	/**Carga el calendario sicmer
	 * @param session
	 * @param formulario
	 * @param fecha
	 * @param request
	 * @param errors
	 * @throws Exception
	 */
	public static void mostrarCalendario(HttpSession session, CotizarReservarForm formulario,Calendar fecha, HttpServletRequest request, ActionMessages errors) throws Exception {
		LogSISPE.getLog().info("Se va a abrir el calendario SICMER");
		LogSISPE.getLog().info("Setea variables de Sesion");
		session.setAttribute(GlobalsStatics.POPUP_CALENDARIO,"OK");
		session.setAttribute(GlobalsStatics.FECHA_ACTUAL, new Timestamp((ConverterUtil.getCurrentTruncDate()).getTime()));
		LogSISPE.getLog().info("Sube a sesion el formulario");
		CotizarReservarForm formularioSicmer = SerializationUtils.clone(formulario);
		session.setAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER,formularioSicmer);
		formulario.setNpHoraDesde(formulario.getHoraDesde());
		formulario.setNpHoraHasta(formulario.getHoraHasta());
		CalendarioMesEST calendario = null;
		Calendar diaProgramado = new GregorianCalendar();
		if(StringUtils.isNotEmpty(formulario.getFechaEntregaCliente())){//Cargar calendario de acuerdo a la fecha
			calendario = cargarDatos(new SimpleDateFormat("yyyy-MM-dd").parse(formulario.getFechaEntregaCliente()), request, errors);
			//se coloca el dia seleccionado, si hay alguna ya colocada en el formulario se setea en el dia programado
			LogSISPE.getLog().info("Fecha Entrega Cliente: "+formulario.getFechaEntregaCliente());
			diaProgramado.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(formulario.getFechaEntregaCliente()));
		}else{
			//Cargar calendario de acuerdo a la fecha
			calendario = cargarDatos(fecha.getTime(), request, errors);
			//se coloca el dia seleccionado, si hay alguna ya colocada en el formulario se setea en el dia programado
			LogSISPE.getLog().info("Fecha Entrega Cliente: "+formulario.getFechaEntregaCliente());
			diaProgramado.setTime(fecha.getTime());			
		}
		
		LogSISPE.getLog().info("Fecha Programada: "+diaProgramado.getTime());
		
		diaProgramado.set(Calendar.HOUR_OF_DAY, 0);diaProgramado.set(Calendar.MINUTE, 0);diaProgramado.set(Calendar.SECOND, 0);diaProgramado.set(Calendar.MILLISECOND, 0);
		for(ResumenDiaEST resumenDiaEST:calendario.getResumenDiaEST()){
			if(diaProgramado.get(Calendar.DAY_OF_MONTH) == resumenDiaEST.getFecha().get(Calendar.DAY_OF_MONTH) && diaProgramado.get(Calendar.MONTH) == resumenDiaEST.getFecha().get(Calendar.MONTH) && diaProgramado.get(Calendar.YEAR) == resumenDiaEST.getFecha().get(Calendar.YEAR)){
				resumenDiaEST.setBotonEstado(MercanciasConstantes.BOTON_SELECCIONADO);
				session.setAttribute(GlobalsStatics.RESUMEN_DIA_EST, resumenDiaEST);				
				break;
			}
		}
		//se carga los combos de rango de horas
		colocarRangoHorasNp(session,formulario);
				
	}

	/**Carga los datos del calendario con la fecha dada
	 * @param time
	 * @param request
	 * @param errors
	 * @return
	 */
	public static CalendarioMesEST cargarDatos(Date time, HttpServletRequest request, ActionMessages errors) {
		//Cargar calendario de fecha actual
		try{
			
			InventarioMercanciaLocalDTO inventarioMercanciaLocalDTO = new InventarioMercanciaLocalDTO();
			inventarioMercanciaLocalDTO.setCodigoLocal(SessionManagerSISPE.getCodigoLocalObjetivo(request));
			LogSISPE.getLog().info("Se llama al servicio de obtener calendario");
			LogSISPE.getLog().info("Codigo local: "+SessionManagerSISPE.getCodigoLocalObjetivo(request));
			LogSISPE.getLog().info("Fecha: "+time.getTime());
			CalendarioMesEST calendario = MercanciasFactory.getInventarioService().obtenerCalendarioConveniosServicioTecnico(time, inventarioMercanciaLocalDTO);//MercanciasFactory.getInventarioService().obtenerCalendarioConveniosServicioTecnico(time, inventarioMercanciaLocalDTO);
			
			//se invalidan en el calendario todos los dias anteriores a hoy
			if(calendario != null){
				if(CollectionUtils.isNotEmpty(calendario.getResumenDiaEST())){
					for(ResumenDiaEST resumenActual : calendario.getResumenDiaEST()){
						if(resumenActual.getFecha().before(fecha)){
							resumenActual.setBotonEstado(MercanciasConstantes.BOTON_OTRO_MES);
							resumenActual.setEstiloDia(MercanciasConstantes.DIA_OTRO_MES);							
						}
					}
				}
			}
			
			request.getSession().setAttribute(GlobalsStatics.CALENDARIO_MES_EST, calendario);
			
			return calendario;
		}catch (Exception e) {
			LogSISPE.getLog().error("Error en cargar cargarDatos Calendario SICMER{}", e);
			errors.add("errorCalendario", new ActionMessage("error.carga.calendario"));
		}	
			return null;
	}
	
	/**
	 * Obtiene la informacion del mes anterior
	 * @param request
	 */
	public static void mesAnterior(HttpServletRequest request, ActionMessages errors) {
		LogSISPE.getLog().info("Mes anterior calendario SICMER");
		CalendarioMesEST calendarioMesEST = (CalendarioMesEST)request.getSession().getAttribute(GlobalsStatics.CALENDARIO_MES_EST);			    
	    c.set(Calendar.YEAR,calendarioMesEST.getAnio());
	    c.set(Calendar.MONTH,calendarioMesEST.getMesNumber()-1);
	    c.set(Calendar.DATE,1);
	    c.set(Calendar.HOUR_OF_DAY,0);
	    c.set(Calendar.MINUTE,0);
	    c.set(Calendar.SECOND,0);
	    c.set(Calendar.MILLISECOND,0);
	    LogSISPE.getLog().info("Fecha a colocar:"+c.getTime());
	    //si se quiere regresar a un mes anterior a la fecha actual no lo deja
	    if((c.get(Calendar.MONTH)<(fecha.get(Calendar.MONTH))) && (c.get(Calendar.YEAR)<=(fecha.get(Calendar.YEAR)))){
	    	errors.add("noPuedeRegresar", new ActionMessage("warning.no.puede.regresar.mes"));
	    }else{
	    	cargarDatos(c.getTime(), request, errors);
    	}
	}
	
	/**
	 * Obtiene la informacion del mes posterior
	 * @param request
	 */
	public static void mesPosterior(HttpServletRequest request, ActionMessages errors) {
		LogSISPE.getLog().info("Mes posterior calendario SICMER");
		CalendarioMesEST calendarioMesEST = (CalendarioMesEST)request.getSession().getAttribute(GlobalsStatics.CALENDARIO_MES_EST);
	    c.set(Calendar.YEAR,calendarioMesEST.getAnio());
	    c.set(Calendar.MONTH,calendarioMesEST.getMesNumber()+1);
	    c.set(Calendar.DATE,1);
	    c.set(Calendar.HOUR_OF_DAY,0);
	    c.set(Calendar.MINUTE,0);
	    c.set(Calendar.SECOND,0);
	    c.set(Calendar.MILLISECOND,0);
	    LogSISPE.getLog().info("Fecha a colocar:"+c.getTime());
	    cargarDatos(c.getTime(), request, errors);	
	}
	
	/**Muestra los convenios y servicios tecnicos del dia seleccionado
	 * @param request
	 */
	public static void verDia(HttpServletRequest request){
		LogSISPE.getLog().info("Selecciona un dia en calendario sicmer");
		int indice = Integer.parseInt(request.getParameter("accioncalendario").substring(7));				
		CalendarioMesEST calendario = (CalendarioMesEST)request.getSession().getAttribute(GlobalsStatics.CALENDARIO_MES_EST);
		ArrayList<ResumenDiaEST> resumenDiaESTCol = new ArrayList<ResumenDiaEST>(calendario.getResumenDiaEST());
		ResumenDiaEST resumenDia = resumenDiaESTCol.get(indice);
		LogSISPE.getLog().info("Ver Calendario: ", indice, resumenDia.getFechaS());
		ResumenDiaEST diaAnterior = (ResumenDiaEST)request.getSession().getAttribute(GlobalsStatics.RESUMEN_DIA_EST);
		diaAnterior.setBotonEstado(MercanciasConstantes.BOTON_NORMAL);
		resumenDia.setBotonEstado(MercanciasConstantes.BOTON_SELECCIONADO);
		request.getSession().setAttribute(GlobalsStatics.RESUMEN_DIA_EST, resumenDia);
	}
	
	/**Carga horas de los combos de horas desde hasta Calendario SICMER
	 * @param ini
	 * @param fin
	 * @return
	 */
	public static Collection<String> cargarHorasDia(Integer ini, Integer fin) {
		LogSISPE.getLog().info("Colocando Rango de horas");
		LogSISPE.getLog().info("Inicio: "+ini);
		LogSISPE.getLog().info("Fin: "+fin);
		Collection<String> horas = new ArrayList<String>();;
		String hora = "";
		for(Integer i=ini;i<fin;i++){	    
			if(i<10)
				hora = "0" + i.toString();
			else
				hora = i.toString();
			//A\u00F1adiendo nueva hora
			horas.add(hora+":00");
		}
		return horas;
	}
	
	
	/**Carga las horas para elegir en la fecha desde y hasta del calendario SICMER
	 * @param session
	 * @param formulario
	 */
	public static void colocarRangoHorasNp(HttpSession session,CotizarReservarForm formulario){
		LogSISPE.getLog().info("Colocando Rango de horas en calendario SICMER");
		ResumenDiaEST resumenDiaEST = (ResumenDiaEST)session.getAttribute(GlobalsStatics.RESUMEN_DIA_EST);
		Calendar now = Calendar.getInstance();
		LogSISPE.getLog().info("Fecha actual en sesion: "+resumenDiaEST.getFechaS());
		LogSISPE.getLog().info("Dia del mes actual: "+now.get(Calendar.DAY_OF_MONTH));
		LogSISPE.getLog().info("Hora actual: "+now.get(Calendar.HOUR_OF_DAY));
		LogSISPE.getLog().info("HoraDesde Calendario SICMER: "+formulario.getNpHoraDesde());
		
		//si el dia es hoy
		if ((resumenDiaEST!=null) && (resumenDiaEST.getFechaS().equals(Integer.toString(now.get(Calendar.DAY_OF_MONTH))))){
			//validacion de rango de horas permitidas
			if(now.get(Calendar.HOUR_OF_DAY)<20 && now.get(Calendar.HOUR_OF_DAY)>5){
				session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(now.get(Calendar.HOUR_OF_DAY)+1,21));
				if(StringUtils.isEmpty(formulario.getNpHoraDesde()) || Integer.parseInt(formulario.getNpHoraDesde().substring(0,2)) < (now.get(Calendar.HOUR_OF_DAY)+1)){
					session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(now.get(Calendar.HOUR_OF_DAY)+2,22));
				}else{
					session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(Integer.parseInt(formulario.getNpHoraDesde().substring(0,2))+1,22));
				}
			//si es madrugada se cargan las horas del dia a partir de las 7
			}else if(now.get(Calendar.HOUR_OF_DAY)<6 && now.get(Calendar.HOUR_OF_DAY)>=0){
				session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(7,21));
				session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(8,22));
			//si se esta configurando entre las 20:00 hasta las 24:00 no se cargan las horas
			}else{
				formulario.setNpHoraDesde(null);
				formulario.setNpHoraHasta(null);
				session.setAttribute(HORAS_DIA_DESDE, null);
				session.setAttribute(HORAS_DIA_HASTA,null);
				session.setAttribute(MENSAJE_HORAS_INVALIDAS,"ok");
			}
		//si no es hoy y ya se ha elegido un rango de horas
		}else if(StringUtils.isNotEmpty(formulario.getNpHoraDesde())){
			session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(7,21));
			session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(Integer.parseInt(formulario.getNpHoraDesde().substring(0,2))+1,22));
		//si no es hoy y no se ha elegido las horas
		}else{
			session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(7,21));
			session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(8,22));
		}
	}
	
	/**Carga las horas para elegir en la fecha desde y hasta del calendario SICMER
	 * @param session
	 * @param formulario
	 */
	public static void colocarRangoHoras(HttpSession session,CotizarReservarForm formulario){
		if(StringUtils.isNotEmpty(formulario.getHoraDesde())){
			session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(7,21));
			session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(Integer.parseInt(formulario.getHoraDesde().substring(0,2))+1,22));
		//si no es hoy y no se ha elegido las horas
		}else{
			session.setAttribute(HORAS_DIA_DESDE, cargarHorasDia(7,21));
			session.setAttribute(HORAS_DIA_HASTA, cargarHorasDia(8,22));
		}
	}
	
	
	/**limpia variables de session usadas para entregas a domicilio desde local
	 * @param session
	 * @param formulario
	 */
	public static void eliminarVariablesCalendarioSICMER(HttpSession session,CotizarReservarForm formulario){
		LogSISPE.getLog().info("Limpiando variables de sesion SICMER");
		session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
		session.removeAttribute(GlobalsStatics.POPUP_CALENDARIO);
		session.removeAttribute(HORAS_DIA_HASTA);
		session.removeAttribute(HORAS_DIA_DESDE);
		session.removeAttribute(GlobalsStatics.FECHA_ACTUAL);
		session.removeAttribute(GlobalsStatics.RESUMEN_DIA_EST);
		session.removeAttribute(GlobalsStatics.CALENDARIO_MES_EST);
		session.removeAttribute(MENSAJE_HORAS_INVALIDAS);
		session.removeAttribute(EntregaLocalCalendarioAction.FORMULARIO_SICMER);
		session.removeAttribute(EntregaLocalCalendarioAction.BOOLEAN_CIUDAD_SOLO_LECTURA);
		formulario.setCodigoVendedorSicmer(null);
	}
}