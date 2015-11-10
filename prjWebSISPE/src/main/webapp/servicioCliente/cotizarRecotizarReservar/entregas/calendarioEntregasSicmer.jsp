<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="distintoMes"><bean:message key="color.distintoMes"/></bean:define>
<bean:define id="seleccionado"><bean:message key="color.seleccionado1"/></bean:define>
<bean:define id="coloresDia"><bean:message key="color.mes"/></bean:define>
<bean:define id="colorIncompleto"><bean:message key="color.colorAnulado"/></bean:define>
<bean:define id="colorIngresos"><bean:message key="color.colorIngreso"/></bean:define>
<bean:define id="colorActivos"><bean:message key="color.diasActivos"/></bean:define>
<bean:define id="autorizado"><bean:message key="color.autorizacion"/></bean:define>
<bean:define id="responsableBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
<bean:define id="entregaDomicilioLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.domicilio.local"/></bean:define>
<logic:iterate name="ec.com.smx.sic.sispe.detallePedidoAux" id="detalle" indexId="numDetalle">
	<html:hidden name="cotizarRecotizarReservarForm" property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}"/>
</logic:iterate>

<div id="div_configuracionEntregasCalendario" style="width:100%;height:465px;overflow-y:auto;overflow-x:hidden;">
<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
	<tr>
		<td>
			<table border="0" width="99%" align="center" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<div id="seccionConfiguracion">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr><td height="5px"></td></tr>
								<tr>
									<td>
										<div id="opcionesBusqueda">
											<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
												<%-- Cabecera de mensajes --%>
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
															<%-- Fila de Ciudad y Sector de entrega --%>
															<tr>
																	<td >&nbsp;</td>
																	<td class="textoAzul11" >
																		Ciudad de entrega:
																	</td>
																	<td >&nbsp;</td>
																	<td>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.vistaEstablecimientoCiudadLocalDTOCol">
																		<%-- variable que coloca si el combo se modifica --%>
																		<bean:define id="localsicmer" name="ec.com.smx.sic.sispe.entregas.sicmer.ciudad.sololectura.boolean"/>
																			<smx:select  disabled="${localsicmer}" property="seleccionCiudad" styleClass="comboFijoOpacity220px" styleError="campoError"  onchange="requestAjax('entregaLocalCalendario.do', ['opcionesBusqueda','restaurarPosicion'], {parameters: 'seleccionCiudadComboSICMER=OK',popWait:true,evalScripts:true});" >
																				<html:option value="" styleClass="textObligatorio">SELECCIONE</html:option>
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
																	<!-- ZONA CIUDAD-->
																	<td >&nbsp;</td>
																	<td class="textoAzul11" >
																		Sector de entrega:
																	</td>
																	
																	<td colspan="2">
																		<logic:notEmpty name="ec.com.smx.sic.sispe.ciudad.sector.entrega">
																			<smx:select property="selecionCiudadZonaEntrega" styleClass="comboFijo220px" styleError="campoError"  onchange="requestAjax('entregaLocalCalendario.do', ['opcionesBusqueda','restaurarPosicion'], {parameters: 'seleccionCiudadZonaComboSICMER=OK',popWait:true,evalScripts:true});">
																				<html:option value="" styleClass="textObligatorio">SELECCIONE</html:option>
																				<logic:iterate name="ec.com.smx.sic.sispe.ciudad.sector.entrega" id="divisionGeopCiudad" indexId="indiceCiudadZona">
																					<html:option value="${divisionGeopCiudad.id.codigoDivGeoPol}">${divisionGeopCiudad.descripcionDivGeoPol}</html:option>																						
																				</logic:iterate>
																			</smx:select>
																			<html:hidden name="cotizarRecotizarReservarForm" property="selecionCiudadZonaEntrega"/>
																		</logic:notEmpty>
																		
																		<logic:empty name="ec.com.smx.sic.sispe.ciudad.sector.entrega">																				
																			<smx:select property="selecionCiudadZonaEntrega" styleClass="comboFijo220px" styleError="campoError"  >
																				<html:option value="" styleClass="textObligatorio">SELECCIONE</html:option>
																			</smx:select>
																			<html:hidden name="cotizarRecotizarReservarForm" property="selecionCiudadZonaEntrega"/>
																		</logic:empty>
																	</td>																		
																</tr>
																<tr><td height="2px"></td></tr>
																<%-- Fila de Direccion  --%>
																<tr>
																	<td >&nbsp;</td>
																	<td  class="textoAzul11">Direcci&oacute;n: </td>
																	<td ></td>
																	<td colspan="4" align="left" class="textoNegro10" >																			
																		<smx:textarea name="cotizarRecotizarReservarForm" property="direccion" styleClass="textObligatorio" styleError="campoError" style="text-transform:uppercase;" cols="110" rows="2" onblur="validarMayuscula(this,'null');"/>
																	</td>
																	
																</tr>
																<tr><td height="2px"></td></tr>
																<%-- Documento de vendedor y boton de busqueda  --%>
																<tr>
																	<td >&nbsp;</td>
																	<td class="textoAzul11" colspan="2">CI. vendedor:</td>
																	<td>
																		<smx:text property="numeroDocumentoSicmer" styleClass="textObligatorio" styleError="campoError" size="40" onkeypress="return validarInputNumeric(event);"
																			onkeyup="requestAjaxEnter('entregaLocalCalendario.do',['opcionesBusqueda','restaurarPosicion','mensajesPopUp'],{parameters:'calendariosicmer=ok&accioncalendario=consultarVendedor',evalScripts:true});"/>		
																	</td>
																	<td>
																		<a href="#" onclick="requestAjax('entregaLocalCalendario.do',['opcionesBusqueda','restaurarPosicion','mensajesPopUp'],{parameters:'calendariosicmer=ok&accioncalendario=consultarVendedor',evalScripts:true});">
																			<img alt="Buscar" src="./images/buscar.gif" border="0">
																		</a>
																	</td>
																	
																	<td class="textoAzul11" >Nombre vendedor:</td>
																	<td class="textoNegro10">
																		<bean:write name="cotizarRecotizarReservarForm" property="nombreVendedorSicmer" />
																		<html:hidden name="cotizarRecotizarReservarForm" property="nombreVendedorSicmer"/>
																	</td>
																	<td >&nbsp;</td>
																</tr>
																
																<tr><td height="2px"></td></tr>
																<%-- Fila de fecha minima de entrega --%>		
															<tr>
																<td >&nbsp;</td>
																<td height="25" class="textoAzul11" colspan="2" >Fecha m&iacute;nima de entrega:</td>
																	<td class="textoNegro10"  >
																		<c:if test="${cotizarRecotizarReservarForm.opElaCanEsp==responsableBodega}">
																			<smx:text property="buscaFecha" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="10" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['popupConfirmar','entregas','mensajesPopUp'], {parameters: 'entregas=ok',popWait:true,evalScripts:true});"/>
																		</c:if>
																		<c:if test="${cotizarRecotizarReservarForm.opElaCanEsp!=responsableBodega}">
																			<bean:write name="cotizarRecotizarReservarForm" property="buscaFecha" formatKey="formatos.fecha"/>
																			<html:hidden name="cotizarRecotizarReservarForm" property="buscaFecha"/>
																		</c:if>
																		<c:if test="${cotizarRecotizarReservarForm.opElaCanEsp==responsableBodega}">
																			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																			<smx:calendario property="buscaFecha" key="formatos.fecha"/>
																		</c:if>
																	</td>
																	<td ></td>
																	<td class="textoAzul11" >Qui&eacute;n recibir&aacute;?: </td>
																	<td><smx:text property="quienRecibeSicmer" styleClass="textObligatorio" styleError="campoError" size="40" /></td>
																	<td >&nbsp;</td>
															</tr>
															<tr><td height="2px"></td></tr>
															<%-- Fila de Fecha de entrega cliente--%>
															<tr>
																<td>&nbsp;</td>
																<td class="textoAzul11" colspan="2">Fecha de entrega al cliente: </td>
																
																	<td class="textoNegro11" id="fechaEntregas">
																		<logic:empty name="cotizarRecotizarReservarForm" property="fechaEntregaCliente">
																			<b>Seleccione día cal.</b>
																		</logic:empty>
																		<logic:notEmpty name="cotizarRecotizarReservarForm" property="fechaEntregaCliente">
																			<bean:write name="cotizarRecotizarReservarForm" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
																			<html:hidden name="cotizarRecotizarReservarForm" property="fechaEntregaCliente"/>
																		</logic:notEmpty>
																		<%-- Desplieque de calendario SICMER--%>
																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																		<c:if test="${cotizarRecotizarReservarForm.opElaCanEsp!=responsableBodega}">
																			<a href="#" title="Despliega el calendario para seleccionar la fecha de entrega al cliente" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta','calendariopopup','opcionesBusqueda','restaurarPosicion','mensajesPopUp'], {parameters: 'calendariosicmer=ok&accioncalendario=abrir',popWait:true, evalScripts: true});ocultarModal();"><img src="images/calendar.gif" border="0"/></a>
																		</c:if>
																	</td>
																	
																	<td>&nbsp;</td>
																<%-- Fecha Desde y Hasta--%>
																<td colspan="2">
																<table  cellspacing="0" cellpadding="0">
																	<tr>
																		<td class="textoAzul11"  >
																			          Entre las:&nbsp;&nbsp;
																		</td>			
                                                                        <td>
                                                                   	 		<c:if test="${cotizarRecotizarReservarForm.opElaCanEsp==responsableBodega}">
	                                                                            <smx:select property="horaDesde" styleClass="comboObligatorio"  styleError="campoError" onchange="requestAjax('entregaLocalCalendario.do', ['opcionesBusqueda'], {parameters: 'calendariosicmer=ok&accioncalendario=programarfechas',popWait:true,evalScripts:true});">
	                                                                                <logic:notEmpty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaDesde">
	                                                                                     <html:options name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaDesde"/>
	                                                                                 </logic:notEmpty>
	                                                                            </smx:select>
                                                                          	</c:if>
																		    <c:if test="${cotizarRecotizarReservarForm.opElaCanEsp!=responsableBodega}">
	                                                                       		<logic:empty name="cotizarRecotizarReservarForm" property="horaDesde">
																					<b>-</b>
																				</logic:empty>
																				<logic:notEmpty name="cotizarRecotizarReservarForm" property="horaDesde">
																					<bean:write name="cotizarRecotizarReservarForm" property="horaDesde" />
																					<html:hidden name="cotizarRecotizarReservarForm" property="horaDesde"/>
																				</logic:notEmpty>
																			</c:if>
                                                                        </td>
																		<td class="textoAzul11">  &nbsp;&nbsp;horas y las: &nbsp;&nbsp; </td>
																	    <td>
																		    <c:if test="${cotizarRecotizarReservarForm.opElaCanEsp==responsableBodega}">
	                                                                            <smx:select property="horaHasta" styleClass="comboObligatorio"  styleError="campoError">
	                                                                               <logic:notEmpty name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaHasta">
	                                                                                     <html:options name="ec.com.smx.sic.sispe.calendarioEntregasSicmer.horasDiaHasta"/>
	                                                                               </logic:notEmpty>
	                                                                            </smx:select>
	                                                                            <html:hidden name="cotizarRecotizarReservarForm" property="horaHasta"/>
																		    </c:if>
																		    <c:if test="${cotizarRecotizarReservarForm.opElaCanEsp!=responsableBodega}">
																		   		<logic:empty name="cotizarRecotizarReservarForm" property="horaHasta">
																					<b>-</b>
																				</logic:empty>
																				<logic:notEmpty name="cotizarRecotizarReservarForm" property="horaHasta">
																					<bean:write name="cotizarRecotizarReservarForm" property="horaHasta" />
																					<html:hidden name="cotizarRecotizarReservarForm" property="horaHasta"/>
																				</logic:notEmpty>
																			</c:if>
                                                                        </td>
																		<td class="textoAzul11">  &nbsp;&nbsp;horas.</td>
																		
																	</tr>
                                                                </table>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			<table width="100%">
				<tr>
					<td>
					<%-- Desplieque de calendario de bodega cuando el stock es de cd--%>
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
																																	<a id="${seleccion}" href="#" onClick="requestAjax('entregaLocalCalendario.do', ['popupConfirmar','calendario','direccion12','mensajesPopUp','buscar','restaurarPosicion','pregunta'], {parameters: 'seleccionCal=${filas1*7+columnas1}',popWait:true,evalScripts:true});ocultarModal();">																																																					
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
			</table>
		</td>
	</tr>
	<tr><td height="10px"></td></tr>
</table>
</div>
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
