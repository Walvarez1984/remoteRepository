<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!-- ventana llamada desde calendarizacion.jsp -->
<logic:notEmpty name="ec.com.smx.calendarizacion.paletaColores" scope="request">
	<bean:define id="visible" value="visible"/>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.calendarizacion.paletaColores" scope="request">
	<bean:define id="visible" value="hidden"/>
	<script language="javascript">
		ocultarModal();
	</script>
</logic:empty>
<div id="colores1" class="popup" style="visibility:${visible};"> 
	<div id="center" style="position:absolute;top:250px;left:320px;">
		<table border="0" width="100px" align="left" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
			<tr>
				<td background="images/barralogin.gif" height="10px" align="right">
					<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion_encabezado">
						<tr>
							<td align="right">
								<div id="botonWin">
									<a href="#" class="linkBlanco8" onclick="requestAjax('adminPlantillas.do?cerrarPaleta=ok', ['colores'], {popWait:false});ocultarModal();">X</a>
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
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="100%" align="left">
									<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioColorDTOCol">
										<bean:size id="tamano" name="ec.com.smx.calendarizacion.calendarioColorDTOCol"/>
										<bean:define id="fila" value="${0}"/>
										<tr><td height="3px"></td></tr>
										<c:forEach begin="0" end="3" step="1" var="filas1">
											<tr>
												<c:forEach begin="0" end="3" step="1" var="columnas1">
													<c:if test="${filas1*4+columnas1 < tamano}">
														<bean:define id="calendarioColorDTO" name="calendarizacionForm" property="colores[${filas1*4+columnas1}]"/>
														<td width="2%">
															<bean:define id="colorPlantilla" name="calendarioColorDTO" property="id.codigoColorPrincipal"/>
															<bean:define id="nombreColorPrincipal" name="calendarioColorDTO" property="nombreColorPrincipal"/>
															<table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
																<tr>
																	<td>
																		<html:link styleClass="paleta" title="${nombreColorPrincipal}" href="#" onclick="requestAjax('adminPlantillas.do', ['colores','color','colorSeleccionado'], {parameters: 'indicecolor=${filas1*4+columnas1}',popWait:false});ocultarModal();">
																		</html:link>
																	</td>
																</tr>
															</table>
														</td>
													</c:if>	
												</c:forEach>
											</tr>
											<tr><td height="3px"></td></tr>
										</c:forEach>
									</logic:notEmpty>	
								</table>
							</td>
						</tr>													
					</table>
				</td>
			</tr>
		</table>
	</div>
</div>