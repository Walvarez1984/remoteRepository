<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<logic:notEmpty name="ec.com.smx.sic.sispe.entregaArticuloLocal">
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:define id="tipoEntidadResponsable"><bean:write name="sispe.vistaEntidadResponsableDTO" property="tipoEntidadResponsable"/></bean:define>
</logic:notEmpty>
<bean:define id="entidadResponsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>

<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	
	<tr>
		<td>
			<div id="div_pagina">
			</div>
				<div id="mensajes">
					<tiles:insert page="/include/mensajes.jsp"/>
				</div>
			
		</td>
	</tr>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="images/entregaPedido.gif" border="0"></img></td>
						<td height="35" valign="middle">Entregas</td>
						<td align="right" valign="top">
							<input type="hidden" name="botonGuardarCambios" value="">
							<input type="hidden" name="botonCerrarEntregas" value="">
							<table border="0" cellpadding="1" cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="guardarA" href="#" onclick="guardarConfigEntregas();">Guardar</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr><td height="5px"></td></tr>
		<tr>
			<td align="center">
			<div id="entregas">
				<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="98%">
					<%--Mensajes de confirmacion--%>
					<tr>
						<%--Barra Izquierda--%>
						<td valign="top" width="40%" align="left">
							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td>
										<div id="localesCalendario">
											<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/localesCalendarioComun.jsp"/>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<%--Fin Barra Izquierda--%>
						<td width="1%"  align="left">&nbsp;</td>
						<%-- Calendario --%>
						<td valign="top" width="59%"  align="left">
							<div id="reservacion">
								<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/reservacionComun.jsp"/>
							</div>
						</td>
					</tr>
				</table>
			</div>
			</td>
		</tr>
	
</table>
</logic:notEmpty>