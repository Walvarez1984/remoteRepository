<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde calendarizacion.jsp -->
<bean:define id="estActivo" name="ec.com.smx.calendarizacion.estadoActivo"/>
<bean:define id="estReservado" name="ec.com.smx.calendarizacion.reservado"/>
<bean:define id="noProgramado"><bean:message key="color.noProgramado"/></bean:define>
<bean:define id="distintoMes"><bean:message key="color.distintoMes"/></bean:define>
<bean:define id="seleccionado"><bean:message key="color.seleccionado"/></bean:define>
<div id="calendarioDiv">
<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
	<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
		<tr>
			<td class="fila_titulo" colspan="7" height="26px">
				<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
					<tr>
						<td height="23" width="20%">&nbsp;
							Calendario - Local:
						</td>
						<td width="55%">
							<logic:notEmpty name="ec.com.smx.sic.sispe.local">
								<bean:write name="ec.com.smx.sic.sispe.local" property="codigoLocal"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.local" property="nombreAreaTrabajo"/>
							</logic:notEmpty>
						</td>
						<td width="7%" align="right">
							<html:link title="Mes Anterior" href="#" onclick="requestAjax('calendarizacion.do', ['cambioMes','calendario','mensajes','detallesPlantillaCalendario'], {parameters: 'mesAnterior=ok',popWait:true});">
								<html:img src = "./images/atrasAzul.gif" border="0"/>
							</html:link>
						</td>
						<td width="13%" id="cambioMes" align="center">
							<bean:write name="ec.com.smx.calendarizacion.mesBusqueda" formatKey="formato.mes.letras"/>
						</td>
						<td width="5%">
							<html:link title="Mes Siguiente" href="#" onclick="requestAjax('calendarizacion.do', ['cambioMes','calendario','mensajes','detallesPlantillaCalendario'], {parameters: 'mesSiguiente=ok',popWait:true});">
								<html:img src = "./images/adelanteAzul.gif" border="0"/>
							</html:link>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td colspan="3" height="7"></td></tr>
		<tr>
			<td align="center">
				<div id="calendario">
					<table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="3">
								<table border="0" cellspacing="0" width="100%">
									<tr align="left">
										<td width="2%">
											<bean:define id="seleccionarTodos" name="ec.com.smx.calendarizacion.seleccionarTodos"/>
											<div id="seleccionMes">
												<a id="${seleccionarTodos}" title="Todo el mes" href="#" onclick="requestAjax('calendarizacion.do', ['calendario','mensajes'], {parameters: 'mes=0',popWait:false});"></a>
											</div>
										</td>
										<logic:iterate name="ec.com.smx.calendarizacion.ordenDiasSemanaDTOCol" id="ordenDiasSemanaDTO" indexId="indiceDia">
											<td width="14%">
												<bean:define name="ordenDiasSemanaDTO" property="seleccion" id="diaSeleccion"/>
												<bean:define name="ordenDiasSemanaDTO" property="nombreDia" id="nombreDia"/>
												<div id="tituloColumnas">
													<a id="${diaSeleccion}" href="#" onclick="requestAjax('calendarizacion.do', ['calendario','mensajes'], {parameters: 'diaSel=${indiceDia}',popWait:false});">${nombreDia}</a>
												</div>
											</td>
										</logic:iterate>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="100%" align="left">
									<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
										<bean:define id="maximo" name="ec.com.smx.calendarizacion.numeroSemanas"/>
										<bean:define id="fila" value="${0}"/>
										<c:forEach begin="0" end="${maximo-1}" step="1" var="filas1">
											<tr>
												<c:forEach begin="0" end="6" step="1" var="columnas1">
													<logic:notEmpty name="calendarizacionForm" property="calendarioDiaLocal[${filas1*7+columnas1}]">
														<bean:define id="calendarioDiaLocalDTO" name="calendarizacionForm" property="calendarioDiaLocal[${filas1*7+columnas1}]"/>
														<bean:define id="columna" value="${(columnas1 + 1) %7}"/>
														<logic:equal name="columna" value="${1}">
															<bean:define id="fila" value="${(fila + 1)}"/>
															<td width="2%" height="30px">
																<bean:define id="selfila" name="calendarizacionForm" property="filaSeleccionada[${fila-1}]"/>
																<div id="tituloFilas">
																	<a id="${selfila}" href="#" onclick="requestAjax('calendarizacion.do', ['calendario','mensajes'], {parameters: 'filaSel=${filas1*7}',popWait:false});"></a>
																</div>
															</td>
														</logic:equal>
														<td class="contenido">
															<div id="calendarioDia${filas1*7+columnas1}">
																<bean:define id="colorPrincipal" name="calendarioDiaLocalDTO" property="codigoColorPrincipal"/>
																<logic:notEmpty name="calendarioDiaLocalDTO" property="estadoCalendarioDia">
																	<logic:equal name="calendarioDiaLocalDTO" property="estadoCalendarioDia" value="${estActivo}">
																		<bean:define id="colorSecundario" value="${noProgramado}"/>
																		<bean:define id="diaActual" value=""/>
																	</logic:equal>
																	<logic:equal name="calendarioDiaLocalDTO" property="estadoCalendarioDia" value="${estReservado}">
																		<bean:define id="colorSecundario" name="calendarioDiaLocalDTO" property="codigoColorSecundario"/>
																		<bean:define id="diaActual" value=""/>
																	</logic:equal>
																	<logic:equal name="calendarioDiaLocalDTO" property="npEsDistintoMes" value="true">
																		<logic:notEqual name="calendarioDiaLocalDTO" property="estadoCalendarioDia" value="${estReservado}">
																			<bean:define id="colorSecundario" value="${distintoMes}"/>
																		</logic:notEqual>
																	</logic:equal>
																</logic:notEmpty>
																<logic:equal name="calendarioDiaLocalDTO" property="npEsFechaActual" value="true">
																	<bean:define id="diaActual" value="tabla_informacion_rojo"/>
																</logic:equal>
																<logic:equal name="calendarioDiaLocalDTO" property="npEsSeleccionado" value="true">
																	<bean:define id="seleccion" value="current"/>
																	<bean:define id="colorSecundario" value="${seleccionado}"/><!-- color celeste -->
																</logic:equal>
																<logic:equal name="calendarioDiaLocalDTO" property="npTienePlantillaPorDefecto" value="true">
																	<bean:define id="marcoDia" value="marcoDiasDefecto"/>
																</logic:equal>
																<logic:notEqual name="calendarioDiaLocalDTO" property="npTienePlantillaPorDefecto" value="true">
																	<bean:define id="marcoDia" value="marcoDias"/>
																</logic:notEqual>
																<logic:equal name="calendarioDiaLocalDTO" property="npEsSeleccionado" value="false">
																	<bean:define id="seleccion" value=""/>
																</logic:equal>
																<table bgcolor="#${colorSecundario}" border="0" class="${diaActual}">
																	<tr>
																		<td bgcolor="#${colorPrincipal}" align="right">
																			<bean:define id="di" name="calendarioDiaLocalDTO" property="npDiaMes"/>
																			<div id="${marcoDia}">
																				<a id="${seleccion}" href="#" onclick="requestAjax('calendarizacion.do', ['calendarioDia${filas1*7+columnas1}','mensajes'], {parameters: 'seleccionCal=${filas1*7+columnas1}',popWait:false});">
																					<c:if test="${calendarioDiaLocalDTO.cantidadExcedida>0}">
																						<span><img src = "images/error12.gif" border="0" align="left"/></span>
																						<!-- IMPORTANTE: No se presenta cantidad excedida -->
																						<%--<span class="textoAzul9" style="font-weight:bold">CE:</span>
																						<span class="textoNegro9"><bean:write name="calendarioDiaLocalDTO" property="cantidadExcedida" formatKey="formatos.enteros"/></span>&nbsp;--%>
																					</c:if>
																					<span><b>${di}</b></span>
																				</a>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<table cellpadding="1" cellspacing="0" border="0" width="98%">
																				<!-- IMPORTANTE: Se presenta CPE en lugar de CN y CA -->
																				<%--<tr>
																					<td class="textoAzul9" width="40%"><b>
																						CN:</b>
																					</td>
																					<td class="textoNegro9" width="31%" align="right">
																						<bean:write name="calendarioDiaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/>
																					</td>
																					<td width="29%"></td>
																				</tr>
																				<tr>
																					<td class="textoAzul9"><b>
																						CA:</b>
																					</td>
																					<td class="textoNegro9" align="right">
																						<bean:write name="calendarioDiaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/>
																					</td>
																					<td></td>
																				</tr>--%>
																				<tr>
																					<td class="textoAzul9" width="40%"><b>
																						CPE:</b>
																					</td>
																					<td class="textoNegro9" width="31%" align="right">
																						<c:set var="cn" value="${calendarioDiaLocalDTO.capacidadNormal}"/>
																						<c:set var="ca" value="${calendarioDiaLocalDTO.capacidadAdicional}"/>
																						<c:set var="sum" value="${cn+ca}" />
																						<bean:write name="sum" formatKey="formatos.enteros"/>
																					</td>
																					<td width="29%"></td>
																				</tr>
																				<tr>
																					<td class="textoAzul9"><b>
																						CR:</b>
																					</td>
																					<td class="textoNegro9" align="right">
																						<bean:write name="calendarioDiaLocalDTO" property="cantidadReservada" formatKey="formatos.enteros" />
																					</td>
																					<td></td>
																				</tr>
																				<tr>
																					<td class="textoAzul9"><b>
																						CA:</b>
																					</td>
																					<td class="textoNegro9" align="right">
																						<bean:write name="calendarioDiaLocalDTO" property="cantidadAcumulada" formatKey="formatos.enteros"/>
																					</td>
																					<td></td>
																				</tr>
																				<tr>
																					<td class="textoAzul9"><b>
																						CD:</b>
																					</td>
																					<td class="textoNegro9" align="right">
																						<!-- IMPORTANTE: Presentar cantidad excedente en lugar de cantidad disponible -->
																						<%--<bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/>--%>
																						<c:if test="${calendarioDiaLocalDTO.cantidadExcedida>0}">
																					 		-<bean:write name="calendarioDiaLocalDTO" property="cantidadExcedida" formatKey="formatos.enteros"/>
																					 	</c:if>
																					 	<c:if test="${calendarioDiaLocalDTO.cantidadExcedida==0}">
																					 	 	<bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/>
																					 	</c:if>
																					</td>
																					<td align="right">
																						<html:link title="Kardex" action="kardex?kardexSel=${filas1*7+columnas1}">
																							<img src = "images/Kardex16.gif" border="0" align="bottom"/>
																						</html:link>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
															</div>
														</td>
													</logic:notEmpty>
												</c:forEach>
											</tr>
										</c:forEach>
									</logic:notEmpty>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr><td colspan="3" height="7"></td></tr>
	</table>
</logic:notEmpty>
</div>