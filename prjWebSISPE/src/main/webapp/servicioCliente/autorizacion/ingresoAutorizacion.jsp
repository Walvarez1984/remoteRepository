<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<bean:define id="modReservacion"><bean:message key="ec.com.smx.sic.sispe.accion.modificarReservacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>

<bean:define id="accion" name="ec.com.smx.sic.sispe.accion"/>

<c:if test="${accion == reservacion || accion == modReservacion || accion == cotizacion || accion == recotizacion}">
	<bean:define id="accion" value="crearCotizacion.do"/>
	<bean:define id="seccionesActualizar" value="mensajes"/>
</c:if>
<logic:notEmpty name="ec.com.smx.sic.sispe.busqueda.porPedidosEspeciales">
	<bean:define id="accion" value="crearPedidoEspecial.do"/>
	<bean:define id="seccionesActualizar" value="mensajes','pregunta"/>
</logic:notEmpty>

<table border="0" cellspacing="0" cellpadding="1" width="100%">
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr><td height="8px" class="2"></td></tr>
				<tr>
					<td>
						<table align="center" width="90%" cellpadding="0" cellspacing="0" class="tabla_informacion">
							<tr>
								<td colspan="2" class="fila_titulo textoNegro10" width="85%" align="left">
									<html:radio property="opAutorizacion" value="${estadoInactivo}" onclick="document.getElementById('numeroAutorizacion').focus();"/>M&eacute;todo 1
								</td>
							</tr>
							<tr><td align="left" class="textoAzul10">No de autorizaci&oacute;n:</td></tr>
							<tr>
								<td align="left">
									<html:text property="numeroAutorizacion" styleClass="combos" size="20" maxlength="100" onfocus="opAutorizacion[0].checked=true" onkeypress="requestAjaxEnter('${accion}',['${seccionesActualizar}'], {parameters: 'aplicarAutorizacion=ok'});return validarInputNumeric(event);"
									onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
								</td>
							</tr>
						</table>
					</td>
					<td width="2px"></td>
				</tr>
				<tr><td height="8px"></td></tr>
				<tr>
					<td>
						<table align="center" width="90%" cellpadding="0" cellspacing="0" class="tabla_informacion">
							<tr>
								<td colspan="2" align="left" class="fila_titulo textoNegro10"><html:radio property="opAutorizacion" value="${estadoActivo}" onclick="document.getElementById('loginAutorizacion').focus();"/>M&eacute;todo 2</td>
							</tr>
							<tr>
								<td align="left" class="textoAzul10">Usuario:</td>
							</tr>
							<tr>
								<td align="left">
									<html:text property="loginAutorizacion" styleClass="combos" size="20" onfocus="opAutorizacion[1].checked=true" onkeypress="requestAjaxEnter('${accion}',['${seccionesActualizar}'],{parameters: 'aplicarAutorizacion=ok'})"/>
								</td>
							</tr>
							<tr>
								<td align="left" class="textoAzul10">Contrase&ntilde;a:</td>
							</tr>
							<tr>
								<td align="left">
									<html:password property="passwordAutorizacion" styleClass="combos" size="20" onfocus="opAutorizacion[1].checked=true" onkeypress="requestAjaxEnter('${accion}',['${seccionesActualizar}'],{parameters: 'aplicarAutorizacion=ok'})"/>
								</td>
							</tr>
						</table>
					</td>
					<td width="4px"></td>
				</tr>
				<tr><td height="8px" colspan="2"></td></tr>
				<tr>
					<td>
						<table align="center" width="90%" cellpadding="0" cellspacing="0">
							<tr>
								<td align="left" class="textoAzul10">Observaci&oacute;n:</td>
							</tr>
							<tr>
								<td align="left">
									<html:textarea property="observacionAutorizacion" styleClass="combos" cols="25" rows="2"/>
								</td>
							</tr>
						</table>
					</td>
					<td width="2px"></td>
				</tr>
				<tr><td height="5px"></td></tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.descripcionUsoAutorizacion">
					<tr>
						<td colspan="2" align="left" class="textoNegro9"><b>Nota:&nbsp;</b><bean:write name="ec.com.smx.sic.sispe.descripcionUsoAutorizacion"/></td>
					</tr>
					<tr><td height="5px"></td></tr>
				</logic:notEmpty>
				<%--Bot&oacute;n Aceptar--%>
				<tr>
					<td align="center">
						<div id="botonD">
							<html:link styleClass="aceptarD" href="#" onclick="requestAjax('${accion}',['${seccionesActualizar}','divTabs'],{parameters: 'aplicarAutorizacion=ok'});">Aceptar</html:link>
						</div>
					</td>
					<td width="2px"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
