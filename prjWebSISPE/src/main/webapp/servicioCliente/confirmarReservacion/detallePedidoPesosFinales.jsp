<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<%-- lista de definiciones para las acciones --%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="opEmpresarial"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.empresarial"/></bean:define>

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
	<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedidoDTO"></bean:define>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.saldoAbono">
	<bean:define id="saldoAbono" name="ec.com.smx.sic.sispe.pedido.saldoAbono"></bean:define>
</logic:notEmpty>

<div id="detallePedido">
	<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
			<tr>
				<td align="center" valign="top">
					<table border="0" class="textoNegro11" align="center" width="98%" cellpadding="0" cellspacing="0">
						<tr><td height="5px"></td></tr>
						<tr>
							<td valign="top" width="100%">
								<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
			
									<tr><td height="5px"></td></tr>
									<tr>
										<td>
											<div id="seccion_detalle">
												<table class="textoNegro12" cellpadding="0" cellspacing="0" align="left" width="100%">
													<tr>
														<td> <!-- el contenido se encuentra en detalleConfirmarReservacion.jsp  -->
															<bean:define id="subPagina" name="ec.com.smx.sic.sispe.pedido.subPagina"/>
															<tiles:insert page="${subPagina}"/>
														</td>
													</tr>
													<tr><td height="2px"></td></tr>
													<tr>
														<%-- secci&oacute;n de los totales --%>
														<td>
															<div id="pie_pedido">
																<table cellpadding="0" width="100%" cellspacing="0" border="0" align="left" class="tabla_informacion">
																	<tr><td height="3px"></td></tr>
																	<tr>
																		<td width="30%" valign="top">
																			<table width="100%" cellspacing="0" cellpadding="0" align="left" class="textoNegro11">
																				<tr><td height="5px"></td></tr>
																				<%--<tr>
																						<td align="right" class="textoAzul11">Porc. Abonado:&nbsp;</td>
																						<td align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="porcentajeAbonoPedido" formatKey="formatos.numeros"/>%</td>
																					</tr>--%>
																				<tr>
																					<td align="right" class="textoAzul11" width="40%">&nbsp;Valor Abonado:&nbsp;</td>																						
																					<td align="left">																					
																						<c:if test="${vistaPedidoDTO.abonoPedido != null}">
																							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.abonoPedido}"/>
																						</c:if>	
																						<c:if test="${vistaPedidoDTO.abonoPedido == null}">
																							<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="0.00"/>
																						</c:if>																						
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoRojo11"><b>Saldo:&nbsp;</b></td>
																					<c:set var="clase" value="textoNegro11"/>
																					<logic:lessThan name="saldoAbono" value="0">
																						<c:set var="clase" value="textoNaranja11"/>
																					</logic:lessThan>
																					<td align="left" class="${clase}">
																						<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${saldoAbono}"/></b>
																					</td>
																				</tr>
																			</table>
																		</td>
																		<td valign="top" width="45%">
																			<%-- se muestra el detalle de los descuentos --%>
																			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/detalleDescuentos.jsp"/>
																		</td>
																		<td valign="top" width="35%">
																			<table border="0" cellspacing="0" align="right" cellpadding="0">
																				<tr>
																					<td align="right" class="textoAzul10">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<html:hidden name="cotizarRecotizarReservarForm" property="subTotalBrutoNoAplicaIva"/>
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalBrutoNoAplicaIva}"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoAzul10">(-)DESCUENTO:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.totalDescuentoIva}"/>
																					</td>
																				</tr>
																				<tr><td colspan="2" align="right" height="5px"></td></tr>
																				<tr>
																					<td align="right" class="textoRojo10">SUBTOTAL NETO:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNetoBruto}"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoAzul11">TARIFA 0%:&nbsp;</td>
																					<td align="right" class="textoNegro11">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
																						<html:hidden name="cotizarRecotizarReservarForm" property="subTotalNoAplicaIVA"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoAzul10">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
																						<html:hidden name="cotizarRecotizarReservarForm" property="subTotalAplicaIVA"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoAzul10"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
																						<html:hidden name="cotizarRecotizarReservarForm" property="ivaTotal"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoAzul10">COSTO FLETE:&nbsp;</td>
																					<td align="right" class="textoNegro10">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
																						<html:hidden name="cotizarRecotizarReservarForm" property="costoFlete"/>
																					</td>
																				</tr>
																				<tr>
																					<td align="right" class="textoRojo10"><b>TOTAL:&nbsp;</b></td>
																					<td align="right" class="textoNegro10">
																						<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/></b>
																						<html:hidden name="cotizarRecotizarReservarForm" property="total"/>
																					</td>
																				</tr>
																			</table>
																		</td>
																		<td width="2%">&nbsp;</td>
																	</tr>
																	<tr><td height="3px"></td></tr>
																</table>
															</div>
														</td>
													</tr>
													<tr><td height="2px"></td></tr>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
	</table>
</div>