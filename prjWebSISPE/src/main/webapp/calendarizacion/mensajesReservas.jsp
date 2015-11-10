<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde ingresoKardex.jsp -->
<logic:notEmpty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
	<bean:define id="visible" value="visible"/>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
	<bean:define id="visible" value="hidden"/>
</logic:empty>	
<div id="popup" class="popup" style="visibility:${visible};"> 
	<div id="center" class="popupcontenido">
		<table border="0" width="300px" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
			<tr>
				<td background="images/barralogin.gif" height="22px">
					<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion_encabezado">
						<tr>
							<td align="right">
								<div id="botonWin">
									<a href="#" class="linkBlanco8" onclick="requestAjax('kardex.do?cerrarVentana=ok', ['mensajes','confirmar'], {popWait:false});">X</a>
								</div>
							</td>
						</tr>	
					</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#F4F5EB">
					<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
						<tr>
						   <td class="textoNegro11" align="center">
								<table border="0" width="99%" >
									<tr>
										<td height="5"><img src="images/pregunta.gif"/></td>
										<%--<tr><td height="5"><%@ include file="/include/mensajes.jsp"%></td></tr>--%>
										<td align="center"  colspan="2">
											<logic:notEmpty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
												<bean:write name="ec.com.smx.calendarizacion.eliminarPlantilla"/>
											</logic:notEmpty>	
										</td>
									</tr>
									<tr><td height="5" colspan="3"></td></tr>
									<tr>
										<td></td>
										<td align="center" height="20px" class="textoNegro12">
											<html:button property="botonSi" value="  Si  " styleClass="botonLogin" onclick="window.showModalDialog('condicionReserva.do?condicionSi=ok','Reservas','scroll:yes;dialogHeight:370px;dialogWidth:800px;help:no');kardexForm.submit();"/>												
										</td>
										<td align="center" height="20px" class="textoNegro12">
											<html:button property="botonNo" value="  No  " styleClass="botonLogin" onclick="window.showModalDialog('condicionReserva.do?condicionNo=ok','Reservas','scroll:yes;dialogHeight:370px;dialogWidth:800px;help:no');kardexForm.submit();"/>												
										</td>
									</tr>
									<tr><td height="5"></td></tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>