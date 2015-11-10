<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<html:form action="calendarizacion" method="post">	
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
										<bean:define id="exit" value="exit"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
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
				<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="98%" align="center">
					<%--Paleta de colores--%>
					<tr>
						<td id="colores" colspan="3" align="center">
							<tiles:insert page="/calendarizacion/paleta.jsp"/>
						</td>
					</tr>
					<%--Mensajes de confirmacion--%>
					<tr>
						<td colspan="3" id="confirmar">
							<tiles:insert page="/calendarizacion/mensajes.jsp"/>
						</td>
					</tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos" width="260px">
							<table cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<tiles:insert page="/calendarizacion/plantillas.jsp"/>
									</td>
								</tr>
								<tr>
									<td height="8px">
									</td>
								</tr>
								<tr>
									<td height="29px">
										<table width="260px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
											<tr>
												<td class="fila_titulo" height="29px">
													<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
														<tr>
															<td width="11%" align="right"><img src="./images/datos_informacion24.gif" border="0"></td>
															<td width="89%">&nbsp;Informaci&oacute;n</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr><td height="6px"></td></tr>
											<tr>
												<td>
													<table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
													   <!-- IMPORTANTE: Se presenta CPE en lugar de CN y CA -->
													   <%-- %> <tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CN:
															</td>
															<td class="textoNegro11">
																Capacidad Normal
															</td>
														</tr> --%>
														<%-- %><tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CA:
															</td>
															<td class="textoNegro11">
																Capacidad Adicional
															</td>
														</tr> --%>
													   	<tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CPE:
															</td>
															<td class="textoNegro11">
																Capacidad Pedidos Especiales
															</td>
														</tr>
														<tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CR:
															</td>
															<td class="textoNegro11">
																Cantidad Reservada
															</td>
														</tr>
														<tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CA:
															</td>
															<td class="textoNegro11">
																Cantidad Acumulada
															</td>
														</tr>
														<tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CD:
															</td>
															<td class="textoNegro11">
																Capacidad Disponible
															</td>
														</tr>
														<%--<tr>
															<td class="textoAzul11" width="40px">
																&nbsp;CE:
															</td>
															<td class="textoNegro11">
																Capacidad Excedida
															</td>
														</tr>--%>
														<tr>
															<td class="fila_contenido" colspan="2">&nbsp;</td>
														</tr>
														<tr>
															<td class="textoAzul11" colspan="2">
																<%-- %>&nbsp;CD=CN + CA - CR - CS --%>
																&nbsp;CD=CPE - CR - CA
															</td>
														</tr>
														<tr>
															<td class="textoAzul11" colspan="2">
																&nbsp;Unidad: Cantidad en Bultos
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr><td height="6px"></td></tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
						<%--Fin Barra Izquierda--%>
						<%-- Separador --%>
						<TD class="datos" width="10px">&nbsp;</TD>
						<%-- Calendario --%>
						<TD class="datos">
							<div id="actualizaCalendario">
								<tiles:insert page="/calendarizacion/calendario.jsp"/>
							</div>
						</TD>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>