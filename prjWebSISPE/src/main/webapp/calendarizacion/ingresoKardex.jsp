<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%--<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>--%>

<tiles:insert page="/include/topJQ.jsp"/>
<logic:empty name="ec.com.smx.sic.sispe.mensajes">

<div id="mensajes" style="font-size:0px;position:relative;">
	<tiles:insert page="/include/mensajes.jsp"/>
</div>
</logic:empty>

<html:form action="kardex" method="post">	
	<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	
		<html:hidden property="ayuda" value=""/>
		<%--T&iacute;tulos, boton: salir --%>
		<tr>
			<td align="left" valign="top" width="100%" colspan="3">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td  width="3%" align="center"><img src="images/calendarizacion.gif" border="0"></img></td>
						<td height="35" valign="middle">Calendarizaci&oacute;n</td>
						<td align="right" valign="top">
							<table border="0">
								<tr>
									<td>
										<bean:define id="regresar" value="regresar"/>
										<div id="botonA">
											<html:link action="calendarizacion" styleClass="calendarizacionA" paramId="regresar" paramName="regresar" title="volver a la calendarizaci&oacute;n">Calendario</html:link>
										</div>
									</td>
									<td>
										<bean:define id="exit" value="ok"/>
										<div id="botonA">
											<html:link action="menuPrincipal" styleClass="inicioA" paramId="salir" paramName="exit" title="ir al men&uacute; principal">Inicio</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="7" colspan="3"></td></tr>
		<tr>
			<td>
				<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="99%" align="center">
					<%--Mensajes de confirmacion--%>
					<tr>
						<td colspan="3" id="confirmar">
							<tiles:insert page="/calendarizacion/mensajesReservas.jsp"/>
						</td>
					</tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos" width="200" id="configuracion">
							<tiles:insert page="/calendarizacion/configuracionPlantilla.jsp"/>
						</td>
						<TD class="datos" width="1">&nbsp;</TD>
						<%-- Calendario --%>
						<TD width="800" class="datos">
							<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
						</TD>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>