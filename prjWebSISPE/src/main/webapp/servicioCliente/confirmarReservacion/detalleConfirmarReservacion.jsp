<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<%--<bean:define id="ranModCan" name="ec.com.smx.sic.sispe.registroPesosFinales.rangoModificacionCantidad"/>--%>
<div id="seccion_detalle_pedido">
	<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion" >
		<tr class="fila_titulo">
			<td height="27px" class="fila_contenido">
				<table cellspacing="0" cellpadding="0" align="right">
					<tr>
						<td>
							<div id="botonD">
								<html:link styleClass="actualizarD" href="#" title="Actualizar detalle del pedido" onclick="requestAjax('confirmacionReservacion.do',['mensajes','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});">Actualizar</html:link>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<%--<tr><td height="3px" class="textoRojo10" colspan="2" align="left">Las cantidades ingresadas no ser&aacute;n tomadas en cuenta para los c&aacute;lculos simplemente se recalculan cuando se ingresa el peso total, estas pueden ser modificadas en un rango de m&aacute;s o menos <b>${ranModCan}</b> en relaci&oacute;n a la cantidad total.</td></tr>--%>
		<tr><td height="5px" colspan="2"></td></tr>
		<tr>
			<td colspan="2">
				<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
					<div id="detallePedido">
						<table border="0" width="100%" cellpadding="1" cellspacing="0">
							<tr>
								<td>
									<table cellspacing="0" cellpadding="0" width="98%"  align="left">
										<tr class="tituloTablas">
											<td width="2%" align="center" class="columna_contenido" rowspan="2">No</td>
											<td width="12%" align="center" class="columna_contenido" rowspan="2">C&oacute;digo barras</td>
											<td width="25%" align="center" class="columna_contenido" rowspan="2">Art&iacute;culo</td>
											<td width="6%" align="center" class="columna_contenido fila_contenido" rowspan="2">Cant.</td>
											<td width="21%" align="center" class="columna_contenido fila_contenido" colspan="2">Peso Kg.</td>
											<td width="5%" align="center" class="columna_contenido" rowspan="2">V.Unit</td>
											<td width="5%" align="center" class="columna_contenido" rowspan="2">V.Unit.Iva</td>
											<td width="7%" align="center" class="columna_contenido" rowspan="2">Tot. Iva</td>
											<td width="2%" align="center" class="columna_contenido" rowspan="2">Iva</td>
											<td width="5%" align="center" class="columna_contenido" rowspan="2">Dscto.</td>
											<%--<td width="5%" align="center" class="columna_contenido" rowspan="2">V.Unit Neto</td>--%>
											<td width="7%" align="center" class="columna_contenido columna_contenido_der" rowspan="2">Tot. Neto</td>
										</tr>
										<tr class="tituloTablas">
											<%--<td width="6%" align="center" class="columna_contenido">Total</td>
											<td width="6%" align="center" class="columna_contenido">Confirmar</td>--%>
											<td width="7%" align="center" class="columna_contenido">Total</td>
											<td width="7%" align="center" class="columna_contenido">Confirmar</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<div id="div_listado" style="width:100%;height:175px;overflow:auto">
										<table cellspacing="0" cellpadding="1" width="98%" align="left" border="0">
											<%-- comienza la iteracion del detalle del pedido --%>
											<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numeroRegistro">
												<%-- control del estilo para el color de las filas --%>
												<bean:define id="residuo" value="${numeroRegistro % 2}"/>
												<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
												<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
													<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
												</logic:equal>
												<c:set var="clase" value="blanco10"/>
												<c:set var="claseTextoCantidad" value="textNormal"/>
												<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.npEstadoInformativo">
													<c:set var="claseTextoCantidad" value="textError"/>
												</logic:notEmpty>
												<logic:notEqual name="residuo" value="0">
													<c:set var="clase" value="grisClaro10"/>
												</logic:notEqual>
												<bean:define id="fila" value="${numeroRegistro + 1}"/>
												<c:set var="articuloPavo" value=""/>
												<c:set var="articuloOtroPesoVariable" value=""/>
												<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="articuloPavo" value="${estadoActivo}"/>
												</logic:equal>
												<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="articuloOtroPesoVariable" value="${estadoActivo}"/>
												</logic:equal>
												<tr class="${clase}">
													<td width="2%" class="columna_contenido fila_contenido" align="center"><bean:write name="fila"/><html:hidden property="checksSeleccionar" value="${numeroRegistro}"/></td>
													<%-- Definici&oacute;n de la propiedad articuloDTO --%>
													<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
													<td width="12%" class="columna_contenido fila_contenido" align="center"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>
													<td width="25%" class="columna_contenido fila_contenido" align="left">
														<table cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td align="left"><bean:write name="articulo" property="descripcionArticulo"/>,&nbsp;<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
							
																<%--<c:if test="${articulo.codigoTipoArticulo != tipoCanasto && articulo.codigoTipoArticulo != tipoDespensa}">--%>
																<c:if test="${articulo.codigoClasificacion == articuloEspecial || articulo.codigoClasificacion == canastoCatalogo}">
																	
																	<bean:define id="codigoSubClasificacion" name="articulo" property="codigoSubClasificacion"/>
	                                                        		<c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																	  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
	                                                                    <bean:define id="descripcion" value="Detalle de la despensa"/>
																	</c:if>
																	<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
																	  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
	                                                                    <bean:define id="descripcion" value="Detalle del canasto"/>
																	</c:if>
																	<td align="right"><img title="${descripcion}" src="${pathImagenArticulo}" border="0"/></td>
																</c:if>
																<c:if test="${articulo.codigoClasificacion != articuloEspecial && articulo.codigoClasificacion != canastoCatalogo}">
																	<logic:equal name="articuloPavo" value="${estadoActivo}">
																		<td align="right">
																			<logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
			                                                                	<c:set var="imagen" value="balanza.gif"/>
																				<img title="Peso variable/Unidad manejo" src="images/balanza.gif" border="0">
			                                                                </logic:equal>
			                                                                <logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
			                                                                	<c:set var="imagen" value="pavo.gif"/>
																				<img title="Peso variable" src="images/pavo.gif" border="0">
			                                                                </logic:notEqual>	
																		</td>
																	</logic:equal>
																	<logic:equal name="articuloOtroPesoVariable" value="${estadoActivo}">
																		<td align="right"><img title="Peso variable" src="images/balanza.gif" border="0"></td>
																	</logic:equal>
																</c:if>
																<td width="2px">&nbsp;</td>
															</tr>
														</table>
													</td> 
													<td width="6%" class="columna_contenido fila_contenido" align="center">
														<logic:notEqual name="articuloOtroPesoVariable" value="${estadoActivo}">
															<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" write="true"/>
														</logic:notEqual>
														<logic:equal name="articuloOtroPesoVariable" value="${estadoActivo}">
															<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
															<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
														</logic:equal>
													</td>
													<%--<td width="6%" class="columna_contenido fila_contenido" align="center">
														<logic:notEqual name="articuloOtroPesoVariable" value="${estadoActivo}">
															<logic:equal name="articuloPavo" value="${estadoActivo}">
																<html:text property="vectorCanAjuModPesos" value="${detalle.estadoDetallePedidoDTO.cantidadAjustadaModificacionPeso}" styleClass="${claseTextoCantidad}" size="5" maxlength="4"/>
															</logic:equal>
															<logic:notEqual name="articuloPavo" value="${estadoActivo}">
																<html:hidden property="vectorCanAjuModPesos" value="${detalle.estadoDetallePedidoDTO.cantidadAjustadaModificacionPeso}" write="true"/>
															</logic:notEqual>
														</logic:notEqual>
														<logic:equal name="articuloOtroPesoVariable" value="${estadoActivo}">
															<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
															<html:hidden property="vectorCanAjuModPesos" value="${detalle.estadoDetallePedidoDTO.cantidadAjustadaModificacionPeso}"/>
														</logic:equal>
													</td>--%>
													<td width="7%" class="columna_contenido fila_contenido" align="right">
														<logic:equal name="articuloPavo" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
														</logic:equal>
														<logic:equal name="articuloOtroPesoVariable" value="${estadoActivo}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
														</logic:equal>
														<logic:notEqual name="articuloPavo" value="${estadoActivo}">
															<logic:notEqual name="articuloOtroPesoVariable" value="${estadoActivo}">
																<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
															</logic:notEqual>
														</logic:notEqual>
													</td>
													<td width="7%" class="columna_contenido fila_contenido" align="center">
														<logic:equal name="articuloPavo" value="${estadoActivo}">
															<logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
																<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoRegistradoLocal}" write="true"/>
															</logic:equal>
															<logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
																<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.pesoRegistradoLocal"> 																     
																	<html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoRegistradoLocal}" styleClass="textNormal" size="9" maxlength="9" onkeypress="requestAjaxEnter('confirmacionReservacion.do',['mensajes','seccion_detalle','detallePedido','numBonos'],{parameters: 'actualizarDetalle=ok'}); return validarInputNumericDecimal(event);"
																	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, false, '99999999999999999999', '2'):true"/>
																</logic:notEmpty>
																<logic:empty name="detalle" property="estadoDetallePedidoDTO.pesoRegistradoLocal">
																	<html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="9" maxlength="9" onkeypress="requestAjaxEnter('confirmacionReservacion.do',['mensajes','seccion_detalle','detallePedido','numBonos'],{parameters: 'actualizarDetalle=ok'}); return validarInputNumericDecimal(event);"
																	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, false, '99999999999999999999', '2'):true"/>
																</logic:empty>
															</logic:notEqual>	
														</logic:equal>
														<logic:notEqual name="articuloPavo" value="${estadoActivo}">
															<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
															<html:hidden property="vectorPeso" value="0"/>
														</logic:notEqual>
													</td>
													<!--PRECIO UNITARIO SIN IVA -->
													<td width="5%" class="columna_contenido fila_contenido" align="right">
														<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
															<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																</logic:empty>                                                                            
															</logic:notEmpty>
															<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																</logic:empty>
															</logic:empty>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
															<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																</logic:empty>
															</logic:notEmpty>
															<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																</logic:empty>
															</logic:empty>
														</logic:notEqual>
													</td>
													<!--PRECIO UNITARIO CON IVA -->
													<td width="5%" class="columna_contenido fila_contenido" align="right">
														<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
															<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
															</logic:notEmpty>
															<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
															</logic:empty>
														</logic:equal>
														<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
															<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:notEmpty>
															<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:empty>
														</logic:notEqual>
													</td>
													<td align="right" class="columna_contenido fila_contenido" width="7%">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
													</td>
													<td class="columna_contenido fila_contenido" width="2%" align="center">
														
														<c:set var="aplicaImpuesto" value="${articulo.aplicaImpuestoVenta}"/>							
														<c:if test="${aplicaImpuesto}">
															<img src="./images/tick.png" border="0">
														</c:if>
														<c:if test="${!aplicaImpuesto}">
															<img src="./images/x.png" border="0">
														</c:if>														
														
													</td>
													<td align="center" class="columna_contenido fila_contenido" width="5%">&nbsp;
														<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
														<logic:lessThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
													</td>
													<%--<td align="right" class="columna_contenido fila_contenido" width="5%">
														<bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
													</td>--%>
													<td align="right" class="columna_contenido fila_contenido columna_contenido_der" width="7%">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
													</td>
												</tr>
											</logic:iterate>
											<%-- termina la iteracion del detalle del pedido --%>
										</table>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</logic:notEmpty>
			</td>
		</tr>
	</table>
</div>