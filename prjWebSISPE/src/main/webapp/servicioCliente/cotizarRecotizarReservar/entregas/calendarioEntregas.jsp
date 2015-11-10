<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
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

<logic:iterate name="ec.com.smx.sic.sispe.detallePedidoAux" id="detalle" indexId="numDetalle">
	<html:hidden name="cotizarRecotizarReservarForm" property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}"/>
</logic:iterate>

<logic:notEmpty name="ec.com.smx.sic.sispe.lugarEntregaDomicilio">
	<bean:define id="lugarEntregaDir" name="ec.com.smx.sic.sispe.lugarEntregaDomicilio"/>
</logic:notEmpty>


<%-- Cargo cada una de las configuraciones para las entregas --%>
<logic:notEmpty name="ec.com.smx.sic.sispe.entregas.configuracionDTOCol">
	<logic:iterate name="ec.com.smx.sic.sispe.entregas.configuracionDTOCol" id="configuracionDTO">
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${lugarEntrega}">
			<bean:define id="configLugarEntrega" name="configuracionDTO"/>
		</logic:equal>
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${tipoEntrega}">
			<bean:define id="configTipoEntrega" name="configuracionDTO"/>
		</logic:equal>
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${stock}">
			<bean:define id="configStock" name="configuracionDTO"/>
		</logic:equal>
	</logic:iterate>
</logic:notEmpty>

<c:set var="altoDivLocCal" value="455px"/>
<%--logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
	<c:set var="altoDivLocCal" value="450px"/>
</logic:notEmpty--%>



<div id="div_configuracionEntregasCalendario" style="width:100%;height:465px;overflow-y:auto;overflow-x:hidden;">
<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
	<tr>
		<td>
			<table border="0" width="99%" align="center" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<div id="seccionConfiguracion">
							<table border="0" cellpadding="0" cellspacing="0" width="95%">
								<tr><td height="5px"></td></tr>
								<tr>
									<td>
										<div id="buscar">
											<logic:notEmpty name="ec.com.smx.sic.sispe.existeLugarEntrega">
												<bean:define id="visible" value="visible"/>
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.existeLugarEntrega">
												<bean:define id="visible" value="hidden"/>
											</logic:empty>
											<div style="visibility:${visible};" id="opcionesBusqueda">
												<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
													<logic:empty name="siDireccion">
														<tr>
															<td id="mensaje2">
																<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajesPasos.jsp"/>
															</td>
														</tr>
														<tr><td height="6px"></td></tr>
													</logic:empty>
													<tr>
														<td>
															<table border="0" class="tabla_informacion" width="99%" align="center" cellspacing="0" cellpadding="0">
																<tr>
																	<td width="3">&nbsp;</td>
																	<td height="25" class="textoAzul10" colspan="2" width="30%">Fecha m&iacute;nima de entrega:</td>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.editaFechaMinima">
																		<td colspan="2">
																			<table>
																				<tr>
																					<td class="textoAzul10" width="22%" align="left">
																						<smx:text property="buscaFecha" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});return validarInputFecha(event);"/>
																					</td>
																					<td>
																						<smx:calendario property="buscaFecha" key="formatos.fecha"/>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</logic:notEmpty>
																	<logic:empty name="ec.com.smx.sic.sispe.editaFechaMinima">
																		<td class="textoNegro10" width="30%" colspan="2">
																			<bean:write name="cotizarRecotizarReservarForm" property="buscaFecha" formatKey="formatos.fecha"/>
																			<html:hidden name="cotizarRecotizarReservarForm" property="buscaFecha"/>
																		</td>
																	</logic:empty>
																	<td width="40%"></td>
																</tr>
																<tr>
																	<td>&nbsp;</td>
																	<td class="textoAzul10" colspan="2">Fecha de entrega al cliente:</td>
																	<logic:notEmpty name="ec.com.smx.sic.flag.calendario.bodega">
																		<td class="textoRojo10" id="fechaEntregas" colspan="2">
																			<logic:notEmpty name="cotizarRecotizarReservarForm" property="fechaEntregaCliente">
																				<%--<b><bean:write name="cotizarRecotizarReservarForm" property="fechaEntregaCliente" formatKey="formatos.fecha"/></b>--%>
																				<smx:text disabled="true" property="fechaEntregaCliente" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10"/>
																			</logic:notEmpty>
																			<logic:empty name="cotizarRecotizarReservarForm" property="fechaEntregaCliente">
																				<b>Seleccione día cal.</b>
																			</logic:empty>
																		</td>
																	</logic:notEmpty>
																	<logic:empty name="ec.com.smx.sic.flag.calendario.bodega">
																		<td colspan="2">
																			<table>
																				<tr>
																					<td class="textoAzul10" id="fechaEntregas" >
																						<smx:text property="fechaEntregaCliente" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10" 
																						onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});return validarInputFecha(event);"
																						onblur="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});"/>
																					</td>
																					<td>
																						<div>
																							<smx:calendario property="fechaEntregaCliente" key="formatos.fecha"/>
																						</div>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</logic:empty>
																	<td>
																		<table cellpadding="0" cellspacing="0" border="0" align="right">
																			<tr>
																				<%--
																				<td class="textoAzul10" align="right">Hora:&nbsp;</td>
																				<td width="45%">
																					<smx:text property="horas" size="1" styleClass="textObligatorio" styleError="campoError" maxlength="2" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});"/>:
																					<smx:text property="minutos" size="1" styleClass="textObligatorio" styleError="campoError" maxlength="2" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});"/>
																				</td>
																				<td class="textoNegro9">24horas HH:mm</td>
																				--%>																			
																				<td class="textoAzul10" align="right">Hora:&nbsp;</td>																			
																                <td width="45%">
																                	<smx:select property="horasMinutos" styleClass="comboObligatorio" styleError="campoError">
																							<html:option value="">Seleccione</html:option>										
																									<logic:notEmpty name="ec.com.smx.sic.sispe.horaCol" >
																										<logic:iterate name="ec.com.smx.sic.sispe.horaCol"  id="hora" indexId="indice">
																											<html:option value="${hora}"></html:option>
																										</logic:iterate>
																									</logic:notEmpty>
																					</smx:select>&nbsp;
																					<%--<smx:select property="minutos" styleClass="comboObligatorio" styleError="campoError">
																							<html:option value="">Seleccione</html:option>										
																									<logic:notEmpty name="ec.com.smx.sic.sispe.minutoCol" >
																										<logic:iterate name="ec.com.smx.sic.sispe.minutoCol" id="minuto" indexId="indice">
																											<html:option value="${minuto}"></html:option>
																										</logic:iterate>
																									</logic:notEmpty>
																					</smx:select>--%>	
																				</td>
																				<td class="textoNegro9">&nbsp;24horas HH:mm</td>		
																			</tr>
																		</table>
																	</td>
																</tr>
																<logic:notEmpty name="ec.com.smx.sic.sispe.seleccionarLocal">
																	<tr>
																		<td colspan="6">
																			<div id="localEntrega">
																				<table cellpadding="0" cellspacing="0">
																					<tr>
																						<td>&nbsp;</td>
																						<td class="textoAzul10">Loc Entrega:</td>
																						<td align="center">
																							<smx:text property="local" styleClass="combos" styleError="campoError" size="3" maxlength="5" onkeyup="cambiarSeleccionTexto(this, cotizarRecotizarReservarForm.listaLocales);" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});return validarInputNumeric(event);"/>
																						</td>
																						<td colspan="3">
																							<table border="0" cellpadding="0" cellspacing="0">
																								<tr>
																									<td align="right">
																										<smx:select property="listaLocales" styleClass="comboObligatorio" styleError="campoError" onchange="cambiarTextoLocal(cotizarRecotizarReservarForm.local,this);">
																											<html:option value="">Seleccione</html:option>
																											<logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
																												<logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
																													<html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
																													<logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
																														<logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
																															<html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
																														</logic:iterate>
																													</logic:notEmpty>
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
								<logic:notEmpty name="ec.com.smx.sic.sispe.existeLugarEntrega">
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
																			<logic:notEmpty name="cotizarRecotizarReservarForm" property="idLocalOSector">
																				<logic:equal name="cotizarRecotizarReservarForm" property="idLocalOSector" value="99">
																					<td width="190">&nbsp;Bodega canastos(<bean:write name="cotizarRecotizarReservarForm" property="idLocalOSector"/>)
																						<img align="top" title="Capacidad furgones" src="images/furgon.gif" border="0">
																					</td>
																				</logic:equal>
																				<logic:equal name="cotizarRecotizarReservarForm" property="idLocalOSector" value="97">
																					<td width="190">&nbsp;Bodega tránsito(<bean:write name="cotizarRecotizarReservarForm" property="idLocalOSector"/>)
																						<img align="top" title="Capacidad furgones" src="images/furgon.gif" border="0">
																					</td>
																				</logic:equal>
																				<c:if test="${cotizarRecotizarReservarForm.idLocalOSector != 99 && cotizarRecotizarReservarForm.idLocalOSector != 97}">
																					<td width="190">&nbsp;Fecha de recepci&oacute;n
																						<logic:notEmpty name="cotizarRecotizarReservarForm" property="idLocalOSector">
																							&nbsp<bean:write name="cotizarRecotizarReservarForm" property="idLocalOSector"/>
																						</logic:notEmpty>
																					</td>
																				</c:if>
																			</logic:notEmpty>
																			<td width="29" align="right">
																				<html:link title="Mes anterior" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','cambioMes','calendario','mensajesPopUp','warning','direccionEntregas'], {parameters: 'mesAnterior=ok',popWait:true,evalScripts:true});">
																					<html:img src = "images/atrasAzul.gif" border="0"/>
																				</html:link>
																			</td>
																			<td width="75" id="cambioMes" align="center">
																				<bean:write name="ec.com.smx.calendarizacion.mesBusqueda" formatKey="formato.mes.letras"/>
																			</td>
																			<td width="23">
																				<html:link title="Mes siguiente" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','cambioMes','calendario','mensajesPopUp'], {parameters: 'mesSiguiente=ok',popWait:true,evalScripts:true});">
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
																									<logic:notEmpty name="cotizarRecotizarReservarForm" property="calendarioDiaLocal[${filas1*7+columnas1}]">
																										<bean:define id="calendarioDiaLocalDTO" name="cotizarRecotizarReservarForm" property="calendarioDiaLocal[${filas1*7+columnas1}]"/>
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
																																	<a id="${seleccion}" href="#" onClick="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','calendario','direccion12','mensajesPopUp','buscar','restaurarPosicion'], {parameters: 'seleccionCal=${filas1*7+columnas1}',popWait:true,evalScripts:true});">																																																					
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
																																	<logic:equal name="calendarioDiaLocalDTO" property="id.codigoLocal" value="99">
																																		<td class="textoNegro9" align="left" title="CAPACIDAD DISPONIBLE">
																																			CF:<bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/>
																																		</td>
																																	</logic:equal>
																																	<logic:notEqual name="calendarioDiaLocalDTO" property="id.codigoLocal" value="99">
																																		<td class="textoNegro9" align="left" title="CANTIDAD DISPONIBLE">
																																			D:<bean:write name="calendarioDiaLocalDTO" property="cantidadDisponible" formatKey="formatos.enteros"/>
																																		</td>
																																	</logic:notEqual>
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
									<tr>
										<td id="direccion12">
											<%--SECCION PARA LLENAR LA DIRECCION--%>
											<div id="datos">
												<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTOCol">
													<script language="JavaScript" type="text/javascript">
														document.getElementById("seccionConfiguracion").scrollTop=document.getElementById("seccionConfiguracion").scrollHeight;
													</script>
												</logic:notEmpty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.habilitarDireccion">
													<table class="tabla_informacion" cellpadding="1" cellspacing="0" width="100%">
														<logic:notEmpty name="siDireccion">
															<tr><td height="6px"></td></tr>
															<tr>
																<td>
																	<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajesPasos.jsp"/>
																</td>
															</tr>
															<tr><td height="6px"></td></tr>
														</logic:notEmpty>
														<tr>
															<td>
																<table border="0" class="textos" width="100%" cellspacing="0" cellpadding="0">
																	<logic:notEmpty name="ec.com.smx.sic.sispe.comboSeleccionCiudad">
																		<tr>
																			<td width="8%">&nbsp;</td>
																			<td class="textoAzul11" width="33%">
																				Ciudad de entrega:
																			</td>
																			<td width="72%" colspan="2">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol">
																					<smx:select property="seleccionCiudad" styleClass="comboFijo220px" styleError="campoError"  onchange="requestAjax('entregaLocalCalendario.do', ['seleccionCiudad','mensajesPopUp','datos','pregunta1'], {parameters: 'seleccionCiudadCombo=OK',popWait:true,evalScripts:true});">
																						<html:option value="">SELECCIONE</html:option>
																						<logic:iterate name="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
																							<html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
																							<logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
																								<logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaEstablecimientoCiudadLocalDTO2" indexId="indiceCiudad2">
																									<logic:equal name="vistaEstablecimientoCiudadLocalDTO2" property="npCiudadRecomendada" value="${estadoActivo}">
																										<html:option value="${vistaEstablecimientoCiudadLocalDTO2.id.codigoCiudad}" styleClass="comboDescripcionCiudadRecomendada">&nbsp;&nbsp;&nbsp;${vistaEstablecimientoCiudadLocalDTO2.nombreCiudad}</html:option>
																									</logic:equal>
																									<logic:notEqual name="vistaEstablecimientoCiudadLocalDTO2" property="npCiudadRecomendada" value="${estadoActivo}">
																										<!-- OANDINO: Se concatena el nombre del ciudad conjuntamente con su respectivo código -->
																										<html:option value="OTR${vistaEstablecimientoCiudadLocalDTO2.nombreCiudad}/${vistaEstablecimientoCiudadLocalDTO2.id.codigoCiudad}">&nbsp;&nbsp;&nbsp;${vistaEstablecimientoCiudadLocalDTO2.nombreCiudad}</html:option>																																																																																																
																									</logic:notEqual>
																								</logic:iterate>
																							</logic:notEmpty>
																						</logic:iterate>
																					</smx:select>
																				</logic:notEmpty>
																				<html:hidden name="cotizarRecotizarReservarForm" property="seleccionCiudad"/>
																			</td>
																		</tr>
																	</logic:notEmpty>
																	<logic:notEmpty name="ec.com.smx.calendarizacion.diaSeleccionado">
																		<tr>
																			<td width="0%"></td>
																			<td width="28%" class="textoAzul11">Direcci&oacute;n:</td>
																			<td width="72%" colspan="2" align="left" class="textoNegro10">
																				<logic:empty name="cotizarRecotizarReservarForm" property="direcciones">
																					<smx:textarea name="cotizarRecotizarReservarForm" property="direccion" styleClass="textObligatorio" styleError="campoError" style="text-transform:uppercase;" cols="35" rows="2" onblur="validarMayuscula(this,'null');"/>
																				</logic:empty>
																				<logic:notEmpty name="cotizarRecotizarReservarForm" property="direcciones">
																					<html:hidden name="cotizarRecotizarReservarForm" property="direccion"  write="true"/>
																				</logic:notEmpty>
																			</td>
																		</tr>
																		
																		<tr>
																			<td width="0%" height="16px">&nbsp;</td>
																			<td class="textoAzul11" colspan="7">Distancia Aproximada desde ${lugarEntregaDir}: <b>Para ver el costo del flete, al ingresar la distancia presione ENTER</b></td>
																		</tr>
																		<tr>
																			<td>
																				<logic:empty name="cotizarRecotizarReservarForm" property="direcciones">
																					<html:radio property="unidadTiempo" value="K"/>
																				</logic:empty>
																			</td>
																			<logic:empty name="cotizarRecotizarReservarForm" property="direcciones">
																				<td class="textoAzul11">Km:</td>
																				<td align="left" class="textoNegro10">
																					 <smx:text name="cotizarRecotizarReservarForm" property="distancia" maxlength="4" styleClass="textObligatorio" styleError="campoError" onfocus="chequear(cotizarRecotizarReservarForm.unidadTiempo[0]);"
																					 onkeyup="requestAjaxEnter('entregaLocalCalendario.do', ['datos','mensajesPopUp'], {parameters: 'botonCalcularCostoEntregaDomicilio=ok',popWait:true});"/>
																				</td>
																			</logic:empty>
																			 <logic:notEmpty name="cotizarRecotizarReservarForm" property="direcciones">
																				<logic:notEmpty name="cotizarRecotizarReservarForm" property="distancia">
																					<td class="textoAzul11">Km:</td>
																					<td align="left" class="textoNegro10">
																						<html:hidden name="cotizarRecotizarReservarForm" property="distancia"  write="true"/>
																					</td>
																				</logic:notEmpty>
																			</logic:notEmpty>
																		</tr>
																		<logic:empty name="cotizarRecotizarReservarForm" property="direcciones">
																			<tr>
																				<td>
																					<html:radio property="unidadTiempo" value="H"/>
																				</td>
																				<td class="textoAzul11">Tiempo:</td>
																				<td align="left">
																					<table cellpadding="0" cellspacing="0" border="0" width="50%">
																						<tr>
																							<td width="50%" class="textoAzul11">
																								Horas
																							</td>
																							<td width="50%" class="textoAzul11">
																								Minutos
																							</td>
																						</tr>
																					</table>
																				</td>
																				<td align="left">
																					<table cellpadding="0" cellspacing="0" border="0" width="100%">
																						<tr>
																							<td width="50%">
																								<html:link href="#" onclick="requestAjax('entregaLocalCalendario.do', ['mensajes','div_pagina'], {parameters: 'botonCalcularCostoEntregaDomicilio=ok',popWait:true});">Ver costo flete:</html:link>
																							</td>
																							<td width="50%" class="textoAzul11">
																								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoEntregaDomicilio}"/>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td class="textoAzul11" colspan="2"></td>
																				<td align="left">
																					<table cellpadding="0" cellspacing="0" border="0" width="50%">
																						<tr>
																							<td width="50%">
																								<smx:text name="cotizarRecotizarReservarForm" property="distanciaH" size="3" maxlength="2" styleClass="textObligatorio" styleError="campoError" onfocus="chequear(cotizarRecotizarReservarForm.unidadTiempo[1]);"
																								onkeyup="requestAjaxEnter('entregaLocalCalendario.do', ['datos','mensajesPopUp'], {parameters: 'botonCalcularCostoEntregaDomicilio=ok',popWait:true});"/>
																							</td>
																							<td width="50%">
																								<smx:text name="cotizarRecotizarReservarForm" property="distanciaM" size="3" maxlength="2" styleClass="textObligatorio" styleError="campoError" onfocus="chequear(cotizarRecotizarReservarForm.unidadTiempo[1]);"
																								onkeyup="requestAjaxEnter('entregaLocalCalendario.do', ['datos','mensajesPopUp'], {parameters: 'botonCalcularCostoEntregaDomicilio=ok',popWait:true});"/>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</logic:empty>
																		 <logic:notEmpty name="cotizarRecotizarReservarForm" property="direcciones">
																			<logic:notEmpty name="cotizarRecotizarReservarForm" property="distanciaM">
																				<tr>
																					<td></td>
																					<td class="textoAzul11">Tiempo:</td>
																					<td align="left">
																						<table cellpadding="0" cellspacing="0" border="0" width="50%">
																							<tr>
																								<td width="50%" class="textoAzul11">
																									Horas
																								</td>
																								<td width="50%" class="textoAzul11">
																									Minutos
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td class="textoAzul11" colspan="2"></td>
																					<td align="left">
																						<table cellpadding="0" cellspacing="0" border="0" width="50%">
																							<tr>
																								<td width="50%">
																									<html:hidden name="cotizarRecotizarReservarForm" property="distanciaH"  write="true"/>
																								</td>
																								<td width="50%">
																									<html:hidden name="cotizarRecotizarReservarForm" property="distanciaM"  write="true"/>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																			</logic:notEmpty>
																		</logic:notEmpty>
																		<logic:notEmpty name="cotizarRecotizarReservarForm" property="direcciones">
																				<html:hidden name="cotizarRecotizarReservarForm" property="unidadTiempo"/>
																			</logic:notEmpty>
																		
																	</logic:notEmpty>
																</table>
															</td>
														</tr>
													</table>
												</logic:notEmpty>
											</div>
										</td>
									</tr>
								</logic:notEmpty>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="10px"></td></tr>
</table>
</div>
<div style="visibility:hidden"><input type="text" value=""/></div>
<%-- Mantiene la configuracion seleccionada en las entregas sirve para que no se me activen nuevamente los radios --%>
<div id="restaurarPosicion">
	<logic:notEmpty name="ec.com.smx.sic.sispe.posiciondivconfentregas">
		<script language="JavaScript" type="text/javascript">		
			document.getElementById("div_configuracionEntregasCalendario").scrollTop=document.getElementById("div_configuracionEntregasCalendario").scrollHeight;
		</script>
	</logic:notEmpty>
	<logic:empty name="ec.com.smx.sic.sispe.posiciondivconfentregas">
		<script language="JavaScript" type="text/javascript">		
			document.getElementById("div_configuracionEntregasCalendario").scrollTop=0;
		</script>
	</logic:empty>
</div>
