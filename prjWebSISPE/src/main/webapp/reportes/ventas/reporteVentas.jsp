<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% String sessionId = session.getId(); %>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="idSesion" value="<%=sessionId%>"/>

<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%">
	<%--Titulo de los datos--%>
	<tr>
		<td class="fila_titulo" colspan="8">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
				<tr>
					<td><img src="./images/detalleVentas24.gif" border="0"/></td>
					<td height="23" width="100%">&nbsp;Detalle de ventas</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="5"></td></tr>
	<tr>
		<td align="right" id="seg_paginacion">
			<smx:paginacion start="${reporteGeneralForm.start}" range="${reporteGeneralForm.range}" results="${reporteGeneralForm.size}" styleClass="textoNegro11" url="reporteVentas.do"  requestAjax="'seg_paginacion','div_listado'"/>
		</td>
	</tr>
	<%--Datos de pedidos--%>
	<tr>
		<td>
			<table border="0" width="98%" cellspacing="0" cellpadding="0">
				<logic:notEmpty name="ec.com.smx.sic.sispe.buscar">
					<logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado">
						<%-- se itera la colección completa para obtener el total de ventas --%>
						<c:set var="totalVentas" value="${0}"/>
						<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaReporteGeneralDTO">
							<c:set var="totalVentas" value="${totalVentas + vistaReporteGeneralDTO.totalPedido}"/>
						</logic:iterate>
						<tr>
							<td colspan="5">
								<table border="0" cellspacing="0" width="100%" cellpadding="3">
									<tr class="tituloTablas">
										<td class="columna_contenido" width="20%" align="center">Local</td>
										<td class="columna_contenido" width="13%" align="center">N&uacute;mero pedido</td>
										<td class="columna_contenido" width="8%" align="center">N&uacute;mero reserva</td>
										<td class="columna_contenido" width="28%" align="center">Datos del cliente</td>
										<td class="columna_contenido" width="16%" align="center">Estado del pago</td>
										<td class="columna_contenido" width="15%" align="center">Fecha del pedido</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="5">
								<div id="div_listado" style="width:100%;height:380px;overflow:auto">
									<table border="0" cellspacing="0" width="100%" cellpadding="1">
										<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado" id="vistaReporteGeneralDTO" indexId="indice">
											<tr>
												<td class="grisClaro10">
													<table border="0" width="100%" class="tabla_informacion">
														<tr>
															<td width="20%" align="center"><bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/></td>
															<td width="13%" class="columna_contenido" align="center">
																<%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoLocal}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}','WESTPED_${idSesion}')">
																	<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
																</html:link--%>
																<%--html:link title="detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/></html:link--%>
																<%--html:link title="detalle del pedido" href="#" onclick="requestAjax('detalleEstadoPedido.do',['mensajes','div_pagina'],{parameters: 'codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoAreaTrabajo}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}'});"><bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/></html:link--%>
																<html:link action="detalleEstadoPedido.do?codigoPedido=${vistaReporteGeneralDTO.id.codigoPedido}&codigoLocal=${vistaReporteGeneralDTO.id.codigoAreaTrabajo}&codigoEstado=${vistaReporteGeneralDTO.id.codigoEstado}&llaveContratoPOS=${vistaReporteGeneralDTO.llaveContratoPOS}&secuencialEstadoPedido=${vistaReporteGeneralDTO.id.secuencialEstadoPedido}" title="Detalle del pedido">
																	<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
																</html:link>
															</td>
															<td width="8%" class="columna_contenido" align="center">
																<logic:notEmpty name="vistaReporteGeneralDTO" property="llaveContratoPOS">
																   <bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
																</logic:notEmpty>
																<logic:empty name="vistaReporteGeneralDTO" property="llaveContratoPOS">-</logic:empty>
															</td>
															<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto != 'RUC' || (vistaReporteGeneralDTO.tipoDocumentoContacto=='RUC' && vistaReporteGeneralDTO.tipoDocumento=='R')}">
																<td width="28%" class="columna_contenido" align="left">
																	<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
																	<bean:write name="vistaReporteGeneralDTO" property="numeroDocumentoPersona"/> -
																	<bean:write name="vistaReporteGeneralDTO" property="nombrePersona"/> - 
																	<bean:write name="vistaReporteGeneralDTO" property="telefonoPersona"/> 
																</td>
															</c:if>
															<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto == 'RUC' && vistaReporteGeneralDTO.tipoDocumento!='R'}">
																<td  width="28%" class="columna_contenido" align="left">
																	<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
																	<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/> -
																	<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/> - 	
																	<bean:write name="vistaReporteGeneralDTO" property="telefonoEmpresa"/>															
																</td>
															</c:if>									
															<td width="16%" class="columna_contenido" align="left">
																<smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																	<bean:message key="label.codigoEstadoPSP"/>
																</smx:equal>
																<smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
																	<bean:message key="label.codigoEstadoPPA"/>
																</smx:equal>
																<smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
																	<bean:message key="label.codigoEstadoPTO"/>
																</smx:equal>
																<smx:equal name="vistaReporteGeneralDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																	<bean:message key="label.codigoEstadoLQD"/>
																</smx:equal>
															</td>
															<td class="columna_contenido" width="15%" align="center"><bean:write name="vistaReporteGeneralDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<c:set var="total" value="${0}"/>
													<table border="0" cellpadding="0" cellspacing="0" width="100%">
														<tr>
															<td>
																<table width="95%" cellpadding="2" cellspacing="0" align="center">
																	<tr class="tituloTablasCeleste">
																		<td width="30%" align="center" class="fila_contenido columna_contenido">Local entrega / Direcci&oacute;n entrega</td>
																		<td width="30%" align="left" class="fila_contenido columna_contenido">Art&iacute;culo</td>
																		<td width="8%" align="center" class="fila_contenido columna_contenido">Cantidad</td>
																		<td width="12%" align="center" class="fila_contenido columna_contenido">Fecha despacho</td>
																		<td width="12%" align="center" class="fila_contenido columna_contenido">Fecha entrega</td>
																		<td width="10%" align="center" class="fila_contenido columna_contenido columna_contenido_der">Ganancia otro local</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td>
																<table width="95%" align="center" cellpadding="2" cellspacing="0">
																	<logic:iterate name="vistaReporteGeneralDTO" property="detallesReporte" id="vistaReporteGeneralDTO2" indexId="indice2">
																		<tr>
																			<td width="30%" class="columna_contenido fila_contenido blanco10">
																				<logic:notEmpty name="vistaReporteGeneralDTO2" property="direccionEntrega">
																					<bean:write name="vistaReporteGeneralDTO2" property="direccionEntrega"/>
																				</logic:notEmpty>
																				<logic:empty name="vistaReporteGeneralDTO2" property="direccionEntrega">-</logic:empty>
																			</td>
																			<td width="30%" class="columna_contenido fila_contenido blanco10" align="left">
																				<bean:write name="vistaReporteGeneralDTO2" property="descripcionArticulo"/>
																			</td>
																			<td width="8%" class="columna_contenido fila_contenido blanco10" align="right">
																				<bean:define id="cantidad" value="${vistaReporteGeneralDTO2.cantidadEntrega}"/>
																				<c:if test="${cantidad == '0'}"> 
                                                       			        		   <bean:write name="vistaReporteGeneralDTO2" property="cantidadEstado"/>
                                                       			        	    </c:if>
                                                       			        	    <c:if test="${cantidad != '0'}"> 
                                                       			        		   <bean:write name="vistaReporteGeneralDTO2" property="cantidadEntrega"/>
                                                       			        	    </c:if>
																			</td>
																			<td width="12%" class="columna_contenido fila_contenido blanco10" align="center">
																				<logic:notEmpty name="vistaReporteGeneralDTO2" property="fechaDespachoBodega">
																					<bean:write name="vistaReporteGeneralDTO2" property="fechaDespachoBodega" formatKey="formatos.fecha"/>	
																				</logic:notEmpty>
																				<logic:empty name="vistaReporteGeneralDTO2" property="fechaDespachoBodega">-</logic:empty>
																			</td>
																			<td width="12%" class="columna_contenido fila_contenido blanco10" align="center">
																				<logic:notEmpty name="vistaReporteGeneralDTO2" property="fechaEntregaCliente">
																					<bean:write name="vistaReporteGeneralDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
																				</logic:notEmpty>
																				<logic:empty name="vistaReporteGeneralDTO2" property="fechaEntregaCliente">-</logic:empty>
																			</td>
																			<td width="10%" align="right" class="columna_contenido columna_contenido_der fila_contenido blanco10">
																				<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
																			</td>
																		</tr>
																		<c:set var="total" value="${total + vistaReporteGeneralDTO2.gananciaVentasLocalEntrega}"/>
																	</logic:iterate>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table width="95%" align="center" border="0">
														<tr>
															<td width="56%"></td>
															<td class="textoNegro11">Total ganancia otros locales:</td>
															<td class="textoNegro11">&nbsp;</td>
															<td align="right" class="textoNegro10"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${total}"/></b></td>
														</tr>
														<tr>
															<td width="56%"></td>
															<td class="textoNegro11 fila_contenido">Total venta local:</td>
															<td class="textoNegro11 fila_contenido">&nbsp;</td>
															<td align="right" class="textoNegro10 fila_contenido">
																<c:set var="totVentaLocal" value="${vistaReporteGeneralDTO.totalPedido - total}"/>
																<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totVentaLocal}"/></b>
															</td>
														</tr>
														<tr>
															<td></td>
															<td width="27%" class="textoNegro11 verdeClaro10">Total ventas pedido:</td>
															<td width="2%" id="accionAbonos_${indice}" align="right" class="verdeClaro10">
																<html:link href="#" onclick="requestAjax('reporteVentas.do',['mensajes', 'accionAbonos_${indice}','abonos_${indice}'],{parameters: 'accionAbonos=${accionAbonos}&nPedido=${indice}', evalScripts:true, popWait:true});">
																	<img src="./images/${accionAbonos}.gif" border="0" title="${accionAbonos} abonos">
																</html:link>
															</td>
															<td width="15%" align="right" class="verdeClaro10"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaReporteGeneralDTO.totalPedido}"/></b>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<%--Abonos--%>
												<td colspan="6" align="right">
													<div id="abonos_${indice}">
														<logic:equal name="accionAbonos" value="plegar">
															<bean:size id="nAbonos" name="ec.com.smx.sic.sispe.VistaReporteGeneralActual" property="abonosPedido"/>
															<logic:greaterThan name="nAbonos" value="0">
																<bean:define id="totalAbonos" value="${0}"/>
																<table border="0" cellpadding="1" cellspacing="0" width="100%">
																	<tr><td height="5"></td></tr>
																	<tr class="tituloTablasCeleste">
																		<td width="20%" class="blanco10"></td>
																		<td class="columna_contenido fila_contenido fila_contenido_sup" align="center" width="25%">Fecha de abono</td>
																		<td class="columna_contenido fila_contenido fila_contenido_sup" align="center" width="27%">Fecha de registro</td>
																		<td class="tabla_informacion" align="center" width="25%">Valor</td>
																		<td width="3%" class="blanco10"></td>
																	</tr>
																	<logic:iterate id="abono" name="ec.com.smx.sic.sispe.VistaReporteGeneralActual" property="abonosPedido" indexId="nAbono">
																		<bean:define id="residuoAbono" value="${nAbono % 2}"/>
																		<logic:equal name="residuoAbono" value="0">
																			<bean:define id="colorBackAbono" value="blanco10"/>
																		</logic:equal>
																		<logic:notEqual name="residuoAbono" value="0">
																			<bean:define id="colorBackAbono" value="naranjaClaro10"/>
																		</logic:notEqual>
																		<tr class="${colorBackAbono}">
																			<td class="blanco10">&nbsp;</td>
																			<td class="columna_contenido fila_contenido" align="center">&nbsp;<bean:write name="abono" property="fechaAbono" formatKey="formatos.fechahora"/></td>
																			<td class="columna_contenido fila_contenido" align="center">&nbsp;<bean:write name="abono" property="fechaRegistroAbono" formatKey="formatos.fechahora"/></td>
																			<td class="columna_contenido fila_contenido columna_contenido_der" align="right">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abono.valorAbono}"/></td>
																		</tr>
																		<c:set var="totalAbonos" value="${totalAbonos + abono.valorAbono}"/>
																	</logic:iterate>
																	<tr>
																		<td class="textoNegro11" colspan="3" align="right"><b>Total:</b></td>
																		<td class="columna_contenido fila_contenido columna_contenido_der amarilloClaro10" align="right">
																			<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalAbonos}"/></b>
																		</td>
																		<td width="3%" class="blanco10"></td>
																	</tr>
																	<tr><td height="8"></td></tr>
																</table>
															</logic:greaterThan>
															<logic:equal name="nAbonos" value="0">
																<table border="0" cellpadding="1" cellspacing="0" width="100%">
																	<tr><td height="5"></td></tr>
																	<tr>
																		<td class="textoAzul11" width="98%">
																			<em>No existen abonos</em>
																		</td>
																		<td width="2%"></td>
																	</tr>
																	<tr><td height="5"></td></tr>
																</table>
															</logic:equal>
															<div style="font-size:1px">&nbsp;
																<script language="javascript">desplegarSeccion('div#abonos_${indiceAbondos}', 'normal');</script>
															</div>
														</logic:equal>
														<logic:equal name="accionAbonos" value="desplegar">
															<div style="font-size:1px">&nbsp;
																<script language="javascript">plegarSeccion('div#abonos_${indiceAbondos}', 'fast');</script>
															</div>
														</logic:equal>
													</div>
												</td>
											</tr>
										</logic:iterate>
									</table>
								</div>
							</td>
						</tr>
						<tr>
							<td class="textoNegro11 columna_contenido fila_contenido fila_contenido_sup celesteClaro10" width="15%" align="right">
								<b>Cantidad ventas:</b>
							</td>
							<td class="textoNegro11 fila_contenido fila_contenido_sup celesteClaro10" width="8%" align="right">
								<bean:size id="cantidadPedidos" name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol"/>
								<b><bean:write name="cantidadPedidos"/></b>
							</td>
							<td class="textoNegro11 fila_contenido fila_contenido_sup celesteClaro10" width="62%" align="right">
								<b>Total ventas:</b>
							</td>
							<td class="fila_contenido fila_contenido_sup celesteClaro10" width="13%" align="right">
								<table width="100%" border="0">
									<tr>
										<td>&nbsp;</td>
										<td align="right">
											<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalVentas}"/></b>
										</td>
									</tr>
								</table>
							</td>
							<td class="fila_contenido fila_contenido_sup columna_contenido_der celesteClaro10" width="2%" align="left">&nbsp;</td>
						</tr>
						<tr><td height="10"></td></tr>
					</logic:notEmpty>
				</logic:notEmpty>
				<logic:empty name="ec.com.smx.sic.sispe.buscar">
					<tr>
						<td colspan="8">Seleccione un criterio de b&uacute;squeda</td>
					</tr>
				</logic:empty>
			</table>
		</td>
	</tr>
	<%--Fin datos de pedidos--%>
</table>