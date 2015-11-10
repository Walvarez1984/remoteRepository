<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%String sessionId = session.getId();%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="idSesion" value="<%=sessionId%>"/>

<c:set var="totalProducido" value="${0}"/>
<c:set var="totalPorProducir" value="${0}"/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion">
	<logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado">
		<%--Paginacion--%>
		<tr>
			<td class="fila_titulo" colspan="3">
				<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
					<tr>
						<td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
						<td height="23" width="100%">&nbsp;Detalle</td>
						<td width="75%" align="right" id="seg_paginacion">
							<smx:paginacion start="${reporteGeneralForm.start}" range="${reporteGeneralForm.range}" results="${reporteGeneralForm.size}" campos="false" styleClass="textoNegro10" url="reporteDespachos.do" requestAjax="'seg_paginacion','div_listado'"/>
						</td>
						<td width="5%">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="6"></td></tr>
		<tr>
			<td>
				<table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="5">
							<table border="0" width="100%" cellspacing="0" cellpadding="3" align="center">
								<tr class="tituloTablas">
									<td width="20%" align="center" class="columna_contenido">Local</td>
									<td width="15%" align="center" class="columna_contenido">N&uacute;mero pedido</td>
									<td width="8%" align="center" class="columna_contenido">N&uacute;mero reserva</td>
									<td width="31%" align="center" class="columna_contenido">Datos cliente</td>
									<td width="26%" align="left" class="columna_contenido">Estado</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<div id="div_listado" style="width:100%;height:385px;overflow-x:hidden;overflow-y:auto;">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<c:set var="localAnterior" value=""/>
									<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado" id="vistaReporteGeneralDTO" indexId="nVista">
										<c:set var="totalProducido" value="${totalProducido + vistaReporteGeneralDTO.totalProducido}"/>
										<c:set var="totalPorProducir" value="${totalPorProducir + vistaReporteGeneralDTO.totalPorProducir}"/>
										<tr>
											<td colspan="5">
												<table border="0" cellpadding="1" cellspacing="0" width="100%" class="tabla_informacion" align="center">
													<tr class="grisClaro10">
														<td width="20%" align="center">
															<bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/>
														</td>
														<td width="15%">
															<%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoLocal}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}','WESTPED_${idSesion}')">
																<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/
															</html:link>--%>
															<%--html:link title="detalle del pedido" href="#" onclick="requestAjax('detalleEstadoPedido.do',['mensajes','div_pagina'],{parameters: 'codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoAreaTrabajo}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}'});">
																<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
															</html:link--%>
															<html:link action="detalleEstadoPedido.do?codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoAreaTrabajo}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}&llaveContratoPOS=${vistaReporteGeneralDTO.llaveContratoPOS}&secuencialEstadoPedido=${vistaReporteGeneralDTO.id.secuencialEstadoPedido}" title="Detalle del pedido">
																<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
															</html:link>
														</td>
														<td width="8%">
															<bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
														</td>
														<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto != 'RUC'}">
															<td width="31%">
																<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
																<bean:write name="vistaReporteGeneralDTO" property="numeroDocumentoPersona"/> -
																<bean:write name="vistaReporteGeneralDTO" property="nombrePersona"/> - 
																<bean:write name="vistaReporteGeneralDTO" property="telefonoPersona"/> 
															</td>
														</c:if>
														<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto == 'RUC'}">
															<td width="31%">
																<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
																<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/> -
																<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/> - 	
																<bean:write name="vistaReporteGeneralDTO" property="telefonoEmpresa"/>															
															</td>
														</c:if>
											
														<td width="26%" align="left">
															<bean:write name="vistaReporteGeneralDTO" property="descripcionEstado"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td colspan="5">
												<table width="96%" cellpadding="2" cellspacing="0" border="0" align="center">
													<tr class="tituloTablasCeleste">
														<td width="14%" align="center" class="columna_contenido fila_contenido">C&oacute;digo barras</td>
														<td width="19%" align="center" class="columna_contenido fila_contenido">Descripci&oacute;n</td>
														<td width="9%" align="center" class="columna_contenido fila_contenido">Completado</td>
														<td width="10%" align="center" class="columna_contenido fila_contenido">% Completado</td>
														<td width="18%" align="center" class="columna_contenido fila_contenido">Destino despacho</td>
														<td width="11%" align="center" class="columna_contenido fila_contenido">Fecha despacho</td>
														<td width="12%" align="center" class="columna_contenido fila_contenido columna_contenido_der">Fecha entrega</td>
													</tr>
													<logic:iterate name="vistaReporteGeneralDTO" property="detallesReporte" id="detalleVistaReporte" indexId="nVistaDetalle">
														<bean:define id="residuoDetalle" value="${nVistaDetalle % 2}"/>
														<logic:equal name="residuoDetalle" value="0">
															<bean:define id="colorBackDetalle" value="blanco10"/>
														</logic:equal>
														<logic:notEqual name="residuoDetalle" value="0">
															<bean:define id="colorBackDetalle" value="blanco10"/>
														</logic:notEqual>
														<tr class = "${colorBackDetalle}">
															<td class="columna_contenido fila_contenido" align="center"><bean:write name="detalleVistaReporte" property="codigoBarras"/></td>
															<td class="columna_contenido fila_contenido"><bean:write name="detalleVistaReporte" property="descripcionArticulo"/></td>
															<td class="columna_contenido fila_contenido" align="right">
																<bean:write name="detalleVistaReporte" property="totalProducidoPorArticulo" formatKey="formatos.enteros"/>/<bean:write name="detalleVistaReporte" property="cantidadDespacho"/>
															</td>
															<td class="columna_contenido fila_contenido">
																<table border="0" cellpadding="0" cellspacing="0" width="90%" align="center">
																	<c:choose>
																		<c:when test="${detalleVistaReporte.porcentajeProducido <= 30}">
																			<c:set var="colorProducido" value="red"/>
																			<c:set var="colorLetraProducido" value="red"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${detalleVistaReporte.porcentajeProducido > 30 && detalleVistaReporte.porcentajeProducido < 100}">
																					<c:set var="colorProducido" value="yellow"/>
																					<c:set var="colorLetraProducido" value="black"/>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${detalleVistaReporte.porcentajeProducido == 100}">
																							<c:set var="colorProducido" value="green"/>
																							<c:set var="colorLetraProducido" value="black"/>
																						</c:when>
																					</c:choose>
																				</c:otherwise>
																			</c:choose>
																		</c:otherwise>
																	</c:choose>
																	<tr>
																		<td align="center" style="color:${colorLetraProducido}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalleVistaReporte.porcentajeProducido}"/>%
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<table border="0" width="100%" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
																				<tr>
																					<td>
																						<c:choose>
																							<c:when test="${detalleVistaReporte.porcentajeProducido > 0}">
																								<table border="0" width="${detalleVistaReporte.porcentajeProducido}%" bgcolor="${colorProducido}">
																									<tr><td></td></tr>
																								</table>
																							</c:when>
																							<c:otherwise>
																								<table border="0" width="100%" bgcolor="white">
																									<tr><td></td></tr>
																								</table>
																							</c:otherwise>
																						</c:choose>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
															</td>
															<td class="columna_contenido fila_contenido"><bean:write name="detalleVistaReporte" property="direccionEntrega"/></td>
															<td class="columna_contenido fila_contenido" align="center"><bean:write name="detalleVistaReporte" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
															<td class="columna_contenido fila_contenido columna_contenido_der" align="center"><bean:write name="detalleVistaReporte" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
														</tr>
													</logic:iterate>
												</table>
											</td>
										</tr>
										<tr>
											<td colspan="5">
												<table width="96%" cellpadding="2" cellspacing="0" border="0" align="center">
													<tr>
														<td width="60%">
															&nbsp;
														</td>
														<td width="25%" class="verdeClaro10 fila_contenido columna_contenido">
															<b>Completado: </b>
														</td>
														<td width="15%" class="verdeClaro10 fila_contenido columna_contenido_der" align="right">
															<b><bean:write name="vistaReporteGeneralDTO" property="totalProducidoPorPedido" formatKey="formatos.enteros"/></b> de
															<b><bean:write name="vistaReporteGeneralDTO" property="totalPorProducirPorPedido" formatKey="formatos.enteros"/></b>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td height="12"></td></tr>
									</logic:iterate>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="textoNegro11 columna_contenido fila_contenido fila_contenido_sup celesteClaro10" width="80%" align="right">
							<b>Total producción:</b>
						</td>
						<td class="fila_contenido fila_contenido_sup celesteClaro10" width="18%" align="right">
							<table width="100%" border="0">
								<tr>
									<td>&nbsp;</td>
									<td align="right">
										<b><bean:write name="totalProducido" formatKey="formatos.enteros"/></b>
									</td>
									<td>/</td>
									<td align="right">
										<b><bean:write name="totalPorProducir" formatKey="formatos.enteros"/></b>
									</td>
								</tr>
							</table>
						</td>
						<td class="fila_contenido fila_contenido_sup columna_contenido_der celesteClaro10" width="2%" align="left">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="6"></td></tr>
	</logic:notEmpty>
</table>