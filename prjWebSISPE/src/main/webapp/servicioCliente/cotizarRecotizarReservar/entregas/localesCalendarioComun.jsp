<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="distintoMes"><bean:message key="color.distintoMes"/></bean:define>
<bean:define id="seleccionado"><bean:message key="color.seleccionado1"/></bean:define>
<bean:define id="coloresDia"><bean:message key="color.mes"/></bean:define>
<bean:define id="colorIncompleto"><bean:message key="color.colorAnulado"/></bean:define>
<bean:define id="colorIngresos"><bean:message key="color.colorIngreso"/></bean:define>
<bean:define id="colorActivos"><bean:message key="color.diasActivos"/></bean:define>
<bean:define id="autorizado"><bean:message key="color.autorizacion"/></bean:define>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="lugarEntrega"><bean:message key="ec.com.smx.sic.sispe.lugarEntrega"/></bean:define>
<bean:define id="tipoEntrega"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega"/></bean:define>
<bean:define id="stock"><bean:message key="ec.com.smx.sic.sispe.stock"/></bean:define>
<bean:define id="responsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="responsableBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
<bean:define id="tipoEntregaTotal"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega.total"/></bean:define>
<bean:define id="tipoEntregaParcial"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega.parcial"/></bean:define>
<bean:define id="entregaLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.miLocal"/></bean:define>
<bean:define id="entregaOtroLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.otroLocal"/></bean:define>
<bean:define id="entregaDireccion"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.domicilio"/></bean:define>
<bean:define id="stockLocal"><bean:message key="ec.com.smx.sic.sispe.stock.local"/></bean:define>
<bean:define id="stockParcialBodega"><bean:message key="ec.com.smx.sic.sispe.stock.parcialBodega"/></bean:define>
<bean:define id="stockTotalBodega"><bean:message key="ec.com.smx.sic.sispe.stock.totalBodega"/></bean:define>
<bean:define id="ciudadGuayaquil"><bean:message key="ec.com.smx.sic.sispe.ciudadesDomicilio.guayaquil"/></bean:define><bean:define id="establecimientosPerecibles" name="ec.com.smx.sic.sispe.establecimientosCol"/>


<logic:notEmpty name="ec.com.smx.sic.sispe.lugarEntregaDomicilio">
	<bean:define id="lugarEntregaDir" name="ec.com.smx.sic.sispe.lugarEntregaDomicilio"/>
</logic:notEmpty>

<c:set var="altoDivLocCal" value="455px"/>
<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
	<tr>
		<td class="fila_titulo" height="30px">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
				<tr>
					<td width="9%" align="right"><img src="images/entregar24.gif" border="0"></td>
					<td width="91%">&nbsp;Configuraci&oacute;n de la Entrega:</td>
					<td width="2px">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" width="99%" align="center" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<div style="height:${altoDivLocCal};overflow:auto;" id="seccionConfiguracion">
							<table border="0" cellpadding="0" cellspacing="0" width="95%">
								<tr>
									<td>
										<div id="buscar">
											<div style="visibility:visible;" id="opcionesBusqueda">
												<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
													<tr>
														<td>
															<table border="0" class="tabla_informacion" width="99%" align="center" cellspacing="0" cellpadding="0">
																<logic:notEmpty name="ec.com.smx.sic.sispe.seleccionarLocal">
																	<tr>
																		<td colspan="6">
																			<div id="localEntrega">
																				<table cellpadding="0" cellspacing="0">
																					<tr>
																						<td>&nbsp;</td>
																						<td class="textoAzul10">Loc Entrega:</td>
																						<td align="center">
																							<smx:text property="local" styleClass="combos" styleError="campoError" size="3" maxlength="5" onkeyup="cambiarSeleccionEntregas(this, document.getElementById('listaLocales'));" />
																						</td>
																						<td colspan="3">
																							<table border="0" cellpadding="0" cellspacing="0">
																								<tr>
																									<td align="right">
																										<smx:select property="listaLocales" styleClass="comboObligatorio" styleError="campoError" onchange="cambiarTextoEntregas(document.getElementById('local'),this);">
																											<html:option value="">Seleccione</html:option>
																											<logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
																												<logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
																													<logic:equal name="vistaEstablecimientoCiudadLocalDTO" property="id.codigoCiudad" value="${ciudadGuayaquil}">
																														<html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
																														<logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
																															<logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
																															<logic:iterate id="establecimientos" name="ec.com.smx.sic.sispe.establecimientosCol" indexId="indiceEstablecimientos">   
																																<c:if test="${vistaLocalDTO.codigoEstablecimiento !=establecimientosPerecibles[indiceEstablecimientos]}">
																																	<html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
																																</c:if>
																															</logic:iterate>	
																															</logic:iterate>
																														</logic:notEmpty>
																													</logic:equal>
																												</logic:iterate>
																											</logic:notEmpty>
																										</smx:select>
																									</td>
																								</tr>
																							</table>
																						</td>
																					</tr>
																				</table>
																			</div>
																		</td>
																	</tr>
																	<tr><td colspan="5" height="4px"></td></tr>
																</logic:notEmpty>
															</table>
														</td>
													</tr>
												</table>
											</div>
										</div>
									</td>
								</tr>
									<tr><td height="5px"></td></tr>
									<tr>
										<td>
											<div id="calendario">
												<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
													<script language="JavaScript" type="text/javascript">
														document.getElementById("seccionConfiguracion").scrollTop=document.getElementById("seccionConfiguracion").scrollHeight;
													</script>
												</logic:notEmpty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.seleccionarCalendario">
													<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
														<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
															<tr>
																<td class="fila_titulo" height="25px">
																	<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
																		<tr>
																			<td width="33">&nbsp;&nbsp;<img src="images/calendario24.gif" border="0"></td>
																			<td width="190">&nbsp;Fecha de recepci&oacute;n
																				<logic:notEmpty name="calendarioBodegaForm" property="idLocalOSector">
																					&nbsp<bean:write name="calendarioBodegaForm" property="idLocalOSector"/>
																				</logic:notEmpty>
																			</td>
																			<td width="29" align="right">
																				<html:link title="Mes Anterior" href="#" onclick="requestAjax('calendarioBodega.do', ['cambioMes','calendario','mensajesEntregas','warning','direccionEntregas'], {parameters: 'mesAnterior=ok&actualizar=ok',popWait:true,evalScripts:true});">
																					<html:img src = "images/atrasAzul.gif" border="0"/>
																				</html:link>
																			</td>
																			<td width="75" id="cambioMes" align="center">
																				<bean:write name="ec.com.smx.calendarizacion.mesBusqueda" formatKey="formato.mes.letras"/>
																			</td>
																			<td width="23">
																				<html:link title="Mes Siguiente" href="#" onclick="requestAjax('calendarioBodega.do', ['cambioMes','calendario','mensajesEntregas','warning','direccionEntregas'], {parameters: 'mesSiguiente=ok&actualizar=ok',popWait:true,evalScripts:true});">
																					<html:img src = "images/adelanteAzul.gif" border="0"/>
																				</html:link>
																			</td>
																			<td width="2px"></td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td align="center" colspan="6">
																	<table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
																		<tr>
																			<td colspan="7">
																				<table border="0" cellspacing="0" width="100%" class="tabla_informacion">
																					<tr align="left">
																						<logic:iterate name="ec.com.smx.sic.sispe.calendarizacion.ordenDias" id="ordenDias">
																							<td width="48px" class="tituloTablas columna_contenido" align="center">
																								<bean:write name="ordenDias"/>
																							</td>
																						</logic:iterate>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="7">
																				<table border="0" cellspacing="0" cellpadding="0" width="100%" align="left">
																					<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
																						<bean:define id="maximo" name="ec.com.smx.calendarizacion.numeroSemanas"/>
																						<bean:define id="fila" value="${0}"/>
																						<c:forEach begin="0" end="${maximo-1}" step="1" var="filas1">
																							<tr>
																								<c:forEach begin="0" end="6" step="1" var="columnas1">
																									<logic:notEmpty name="calendarioBodegaForm" property="calendarioDiaLocal[${filas1*7+columnas1}]">
																										<bean:define id="calendarioDiaLocalDTO" name="calendarioBodegaForm" property="calendarioDiaLocal[${filas1*7+columnas1}]"/>
																										<td class="contenido" valign="top">
																											<div id="calendarioDia${filas1*7+columnas1}">
																												<logic:equal name="calendarioDiaLocalDTO" property="npEsSeleccionado" value="false">
																													<bean:define id="seleccion" value=""/>
																													<bean:define id="colorSeleccionado" value="FFFFFF"/>
																													<bean:define id="colorDia" value="${coloresDia}"/>
																												</logic:equal>
																												<logic:equal name="calendarioDiaLocalDTO" property="npTieneAutorizacion" value="true">
																													<bean:define id="colorSeleccionado" value="${autorizado}"/><!-- color Autorizacion -->
																												</logic:equal>
																												<logic:equal name="calendarioDiaLocalDTO" property="npEsSeleccionado" value="true">
																													<bean:define id="seleccion" value="current"/>
																													<bean:define id="colorDia" value="${seleccionado}"/><!-- color celeste -->
																													<bean:define id="colorSeleccionado" value="${seleccionado}"/><!-- color celeste -->
																												</logic:equal>
																												<logic:equal name="calendarioDiaLocalDTO" property="npEsDistintoMes" value="true">
																													<bean:define id="colorDia" value="${distintoMes}"/>
																												</logic:equal>
																												<logic:equal name="calendarioDiaLocalDTO" property="npEstaEnRangoDespacho" value="true">
																													<logic:equal name="calendarioDiaLocalDTO" property="npPuedeSeleccionar" value="true">
																														<bean:define id="colorDia" value="${colorActivos}"/>
																													</logic:equal>
																												</logic:equal>
																												<bean:define id="marcoDia" value="marcoDiasLocal"/>
																												<table bgcolor="#${colorSeleccionado}" border="0" class="${diaActual}">
																													<tr>
																														<td align="right" bgcolor="#${colorDia}">
																															<bean:define id="di" name="calendarioDiaLocalDTO" property="npDiaMes"/>
																															<div id="${marcoDia}">
																																<logic:equal name="calendarioDiaLocalDTO" property="npPuedeSeleccionar" value="true">
																																	<a id="${seleccion}" href="#" onClick="requestAjax('calendarioBodega.do', ['calendario','fDespacho','mensajesEntregas'], {parameters: 'seleccionCal=${filas1*7+columnas1}&actualizar=ok',popWait:true,evalScripts:true});">
																																		<span><b>${di}</b></span>
																																	</a>
																																</logic:equal>
																																<logic:notEqual name="calendarioDiaLocalDTO" property="npPuedeSeleccionar" value="true">
																																	<span class="diaSinSeleccion"><b>${di}</b></span>
																																</logic:notEqual>
																															</div>
																														</td>
																													</tr>
																													<tr>
																														<td valign="top">
																															<table cellpadding="1" cellspacing="0" border="0" width="98%">
																																<tr>
																																	<td class="textoNegro9" align="left" title="CANTIDAD DISPONIBLE">
																																		D:<bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/>
																																	</td>
																																</tr>
																																<logic:notEmpty name="calendarioDiaLocalDTO" property="detalleCantidadCalendario">
																																	<logic:iterate id="cantidadDia" name="calendarioDiaLocalDTO" property="detalleCantidadCalendario">
																																		<tr>
																																			<td class="textoNegro9" align="left" title="${cantidadDia.nombreCompletoCantidad}">
																																				${cantidadDia.nombreCortoCantidad}:${cantidadDia.cantidad}
																																			</td>
																																		</tr>
																																	</logic:iterate>
																																</logic:notEmpty>
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
																</td>
															</tr>
															<tr><td colspan="6" height="6px"></td></tr>
														</table>
													</logic:notEmpty>
												</logic:notEmpty>
											</div>
										</td>
									</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="10px"></td></tr>
</table>