<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde ingresoKardex.jsp -->
<bean:define id="estReservado" name="ec.com.smx.calendarizacion.estadoMovimiento.reservado"/>
<bean:define id="movIngreso" name="ec.com.smx.calendarizacion.ingreso"/>
<bean:define id="movEgreso" name="ec.com.smx.calendarizacion.egreso"/>
<bean:define id="movAnulado" name="ec.com.smx.calendarizacion.anulado"/>
<bean:define id="movAicn" name="ec.com.smx.calendarizacion.ajusteIngresoCapacidadNormal"/>
<bean:define id="movAecn" name="ec.com.smx.calendarizacion.ajusteEgresoCapacidadNormal"/>
<bean:define id="movAica" name="ec.com.smx.calendarizacion.ajusteIngresoCapacidadAdicional"/>
<bean:define id="movAeca" name="ec.com.smx.calendarizacion.ajusteEgresoCapacidadAdicional"/>
<bean:define id="colorReservado"><bean:message key="color.colorReservado"/></bean:define>
<bean:define id="colorIngresos"><bean:message key="color.colorIngreso"/></bean:define>
<bean:define id="colorEgresos"><bean:message key="color.colorEgreso"/></bean:define>
<bean:define id="colorAnulado"><bean:message key="color.colorAnulado"/></bean:define>
<bean:define id="colorAjuste"><bean:message key="color.colorAjustes"/></bean:define>

<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
	<tr>
		<td class="fila_titulo" colspan="7" height="29px">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
				<tr>
					<td width="40" align="right"><img src="images/Kardex24.gif" border="0"></td>
					<td width="63">&nbsp;Kardex:</td>
					<td width="514">
						<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO">
							<bean:define id="calendarioDiaLocalDTO" name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO"/>
							<bean:write name="calendarioDiaLocalDTO" property="id.fechaCalendarioDia" formatKey="formato.fecha.letras"/>
						</logic:notEmpty>
					</td>
					<td width="357" align="right">
						<logic:notEmpty name="ec.com.smx.kardex.local">
							<div id="botonD">
								<html:link styleClass="agregarD" href="#" onclick="requestAjax('kardex.do', ['divlistado1','mensajes','confirmar','configuracion','listado'], {parameters: 'botonAgregarKardex=ok',popWait:true});">Agregar</html:link>
							</div>
						</logic:notEmpty>
					</td>
					<td width="1"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td colspan="7" height="7"></td></tr>
	<tr>
		<td align="center" id="detalles" colspan="7">
			<table border="0" class="textos" width="100%" align="center" cellspacing="0" cellpadding="0">
				<tr>
					<td width="100%">
					   <table class="tabla_informacion" border="0" cellpadding="5px" cellspacing="0" width="100%">
							<tr>
								<td width="6%" align="center" class="tituloTablas columna_contenido"><B>COD.</B></td>
								<td width="6%" align="center" class="tituloTablas columna_contenido"><B>REF.</B></td>
								<td width="6%" align="center" class="tituloTablas columna_contenido"><B>P. FECHA ENTREGA</B></td>
								<td width="8%" align="center" class="tituloTablas columna_contenido"><B>TIPO MOV.</B></td>
								<td width="8%" align="center" class="tituloTablas columna_contenido"><B>MOTIVO MOVIMIENTO</B></td>
								<td width="11%" align="center" class="tituloTablas columna_contenido"><B>CONCEPTO</B></td>
								<td width="8%" colspan="2" align="center" class="tituloTablas columna_contenido"><B>CANTIDAD</B></td>
								<td width="11%" colspan="2" align="center" class="tituloTablas columna_contenido"><B># PED.</B></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="100%" id="listado">
						
							<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
								<tr>
									<td width="15%" class="textoNegro9" align="center">
										<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioTipoMovimientoDTOCol">
											Tipo:&nbsp;<smx:select property="tipoMovimiento" styleClass="textObligatorio" styleError="campoError" onchange="requestAjax('kardex.do', ['mensajes','motivos'], {parameters: 'tMovimiento=ok'});">
												<html:option value="">Seleccione</html:option>
												<html:options collection="ec.com.smx.calendarizacion.calendarioTipoMovimientoDTOCol" labelProperty="descripcionTipoMovimiento" property="id.codigoTipoMovimiento"/>
											</smx:select>
										</logic:notEmpty>
									</td>
									<td width="45%" class="textoNegro9" align="center" id="motivos">
										<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol">
											Motivo:&nbsp;<html:select property="motivo" styleClass="textObligatorio">
												<html:option value="">Seleccione</html:option>
												<html:options collection="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol" labelProperty="descripcionMotivoMovimiento" property="id.codigoMotivoMovimiento"/>
											</html:select>
										</logic:notEmpty>
									</td>
									<td width="25%" align="center" class="textoNegro9">
										Concepto:&nbsp;<smx:textarea rows="2" cols="20" property="concepto" styleClass="textObligatorio" styleError="campoError"/>
									</td>
									<td width="15%" align="center" class="textoNegro9">
										Cantidad:&nbsp;<smx:text property="cantidad" size="5" styleClass="textObligatorio" styleError="campoError" onkeypress="requestAjaxEnter('kardex.do', ['divlistado1','mensajes','confirmar','configuracion','listado'], {parameters: 'botonAgregarKardex=ok',popWait:true});"/>
									</td>
								</tr>
							</table>
						
					</td>
				</tr>
				<tr>
					<td>
						 <div id="divlistado1" style="width:100%;height:385px;overflow:auto">
							<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
								<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol">
									<smx:iterator name="ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTOCol" id="calendarioDetalleDiaLocalDTO"/>
								</logic:notEmpty>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td colspan="7" height="7"></td></tr>
</table>