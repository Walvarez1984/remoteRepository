<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="estadoAnulado"><bean:message key="ec.com.smx.sic.sispe.estadoAnulado"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoCotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoCotizado" id="estadoCotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoRecotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoRecotizado" id="estadoRecotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoReservado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoReservado" id="estadoReservado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoProduccion">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoProduccion" id="estadoProduccion"/>
</logic:notEmpty>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define name="sispe.estado.activo" id="estadoActivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
	<bean:define name="ec.com.smx.sic.sispe.tipoArticulo.canasto" id="tipoCanasto"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>	
<%------ se obtiene la acción actual ---------%>
<%--<bean:define id="accionActual" name="ec.com.smx.sic.sispe.accion"/>
	<bean:define id="accionEstadoPedido"><bean:message key="ec.com.smx.sic.sispe.accion.estadoPedido"/></bean:define>--%>

<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<%-- se definen los c&oacute;digos de las posibles entidades responsables --%>
<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
	<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
		<tr>
			<td align="center">
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
					<tr class="fila_titulo">
						<td colspan="4" class="textoNegro11 fila_contenido" align="left" height="20px"><b>Datos de la cabecera:</b></td>
					</tr>
					<tr>
						<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
							<table border="0" cellspacing="0" width="100%" align="left">
								<tr>
									<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<td></td>
												<td class="textoNegro11" width="30%" align="left">
													<label class="textoAzul11">No pedido:&nbsp;</label>
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoConsolidado">
													<b><label class="textoRojo11">
														(CONSOLIDADO)
													</label></b>
													</logic:notEmpty>
												</td>
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
													<td align="left" class="textoNegro11" width="20%">
														<label class="textoAzul11">No reservaci&oacute;n:&nbsp;</label>
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
													</td>
												</logic:notEmpty>
												<td align="left" class="textoNegro11" width="20%">
													<label class="textoAzul11">Entidad responsable:&nbsp;</label>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
														<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
													</logic:equal>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
														<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
													</logic:equal>
												</td>
												<td align="left" class="textoNegro11" width="15%">
													<label class="textoAzul11">Precios afiliado:&nbsp;</label>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">Si</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">No</logic:notEqual>
												</td>
												<td align="left" class="textoNegro11" width="45%">
													<label class="textoAzul11">Pago efectivo:&nbsp;</label>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">Si</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">No</logic:notEqual>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado">
									<tr>
										<td class="textoAzul11" width="12%" align="right">Pedido relacionado:</td>
										<td align="left" class="textoNegro11" width="80%">
											<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado"/>
										</td>
									</tr>
								</logic:notEmpty>
								
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
									<tr>
										<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<td class="textoAzul11" align="right" width="12%">Empresa:</td>
												<td class="textoNegro11" align="left" width="65%">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
												</td>
												<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="fechaFinalEstado">
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
														<td class="textoAzul11" align="right" width="15%">Nro bonos a recibir: </td>
														<td class="textoNegro11" align="left" width="5%">
																<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/>																			
														</td>
													</logic:notEmpty>
												</logic:empty>
												<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
													<td class="textoAzul11" align="left" width="20%">&nbsp;</td>
												</logic:empty>
												</td>
											</tr>
										</table>
									</tr>																			
								</logic:notEmpty>
								
								<!-- PARA EL CASO DE DATOS DE PERSONA -->
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
									<tr>
										<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<td class="textoAzul11" align="right" width="10%">Cliente:</td>
												<td class="textoNegro11" align="left" width="50%">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
												</td>
												<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="fechaFinalEstado">
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
														<td class="textoAzul11" align="right" width="15%">Nro bonos a recibir: </td>
														<td class="textoNegro11" align="left" width="5%">
																<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/>																			
														</td>
													</logic:notEmpty>
												</logic:empty>
												<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
													<td class="textoAzul11" align="left" width="20%">&nbsp;</td>
												</logic:empty>
											</tr>
										</table>
										</td>										
									</tr>
								</logic:notEmpty>	
								
								<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->														
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto">	
										<tr>
											<td class="textoAzul11" align="right" width="12%">Contacto:</td>																					
											<td class="textoNegro11" align="left" width="80%">		
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoCliente"/>&nbsp;																													
											</td>	
										</tr>										
								</logic:notEmpty>	
																							
								<tr>
								<td class="textoAzul11" align="right">Elaborado en:</td>
								<td>
									<table align="left" cellpadding="1" cellspacing="0" width="82%">
										<tr>
									
									<td class="textoNegro11" align="left">
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
									</td>
								
									<td class="textoAzul11" align="right">Fecha de estado:</td>
									<td class="textoNegro11" align="left">
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
									</td>
									</tr>
									</table>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<bean:define id="codigoEstadoReserva"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>
												<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
													<td class="textoAzul11" align="right" width="13%">Estado:</td>
													<td class="textoRojo11" align="left" width="18%">
														<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
													</td>
													<td class="textoAzul11" align="right" width="5%">Etapa:</td>
													<td class="textoNegro11" align="left">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="etapaEstadoActual"/>
													</td>
												</logic:notEqual>
												<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
													<td class="textoAzul11" align="right" width="13%">Estado:</td>
													<td class="textoRojo11" align="left" width="18%">
														<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
													</td>
													<td class="textoAzul11" align="right" width="5%">Etapa:</td>
													<td class="textoNegro11" align="left">
														<bean:define id="pedidoReservadoPCO"><bean:message key="estado.pedidoReservado.pendienteConfirmar"/></bean:define>
														<bean:define id="pedidoReservadoCON"><bean:message key="estado.pedidoReservado.confirmado"/></bean:define>
														<bean:define id="pedidoReservadoEXP"><bean:message key="estado.pedidoReservado.expirado"/></bean:define>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoPCO}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.PCO"/></bean:define>
														</logic:equal>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoCON}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.CON"/></bean:define>
														</logic:equal>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoEXP}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.EXP"/></bean:define>
														</logic:equal>
														<bean:write name="descripcionEstadoReserva"/>
													</td>
												</logic:equal>
											</tr>
										</table>
									</td>
								</tr>
								<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
								<c:choose>
									<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
										<tr>
											<td class="textoAzul11" align="right">Valor abono inicial:</td>
											<td>
												<table cellpadding="0" cellspacing="0" align="left" class="textoNegro11">
													<tr>
														<td>
															<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="valorAbonoInicialManual">
																<bean:define id="valorAbonoInicialManual" name="ec.com.smx.sic.sispe.vistaPedido" property="valorAbonoInicialManual"></bean:define>
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorAbonoInicialManual}"/>
															</logic:notEmpty>
															<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="valorAbonoInicialManual">
																0.00
															</logic:empty>
														</td>
														<td align="left">
															<label class="textoAzul11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valor abonado:</label>&nbsp;
															<bean:define id="abonoPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="abonoPedido"></bean:define>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abonoPedido}"/>
														</td>
														<!--nueva columna valor pagado -->
														<td align="left">
															<label class="textoAzul11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valor total pagado:</label>&nbsp;
															 <c:if test="${vistaPedidoDTO.id.codigoEstado == estadoAnulado}">
															 0.00
															 </c:if>
															 <c:if test="${vistaPedidoDTO.id.codigoEstado != estadoAnulado }">
															 <logic:notEmpty name="vistaPedidoDTO" property="abonosPedidos">
															 	<logic:iterate id = "abono" name="vistaPedidoDTO" property="abonosPedidos">
															 		 <c:set var="totalAbonos" value="${totalAbonos + abono.valorAbono}"/>
															 	</logic:iterate>
															 	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalAbonos}"/>
															 </logic:notEmpty>
															 <logic:empty name="vistaPedidoDTO" property="abonosPedidos">
															 	0.00
															 </logic:empty>
															 </c:if>	
														</td>
														<!--fin nueva columna valor pagado -->
														<td align="left">
															<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(Enviado al CD)</label>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(No enviado al CD)</label>
															</logic:notEqual>
														</td>
														<smx:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstadoPagado" valueKey="codigoEstadoDevolucionAbonos">
															<td align="left">
																<label class="textoAzul11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estado pago:</label>&nbsp;
																
																<smx:equal name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																	<bean:message key="label.codigoEstadoPSP"/>
																</smx:equal>
																<smx:equal name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
																	<bean:message key="label.codigoEstadoPPA"/>
																</smx:equal>
																<smx:equal name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
																	<bean:message key="label.codigoEstadoPTO"/>
																</smx:equal>
																<smx:equal name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																	<bean:message key="label.codigoEstadoLQD"/>
																</smx:equal>
																
															</td>
														</smx:notEqual>
													</tr>
												</table>
											</td>
										</tr>
									</c:when>
								</c:choose>
							</table>
						</td>
					</tr>
				</TABLE>
			</td>
		</tr>
		<!-- para cuando el pedido tiene autorizaciones de descuento variable o stock -->
<%-- 		<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="tieneAutorizacion"> --%>
			<tr>
				<td >
					<div style="width:98%">
						<!--  Todo el detalle del pedido, fué trasladado a 
								/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp
						 -->
						<tiles:insert page="/controlesUsuario/controlTab.jsp"/>		
					</div>
				</td>
			</tr>	
<%-- 		</logic:notEmpty> --%>
		<!-- para cuando el pedido no tiene autorizaciones de descuento variable o stock-->
<%-- 		<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="tieneAutorizacion"> --%>
<!-- 			<tr height="10px"><td></td></tr> -->
<!-- 			<tr> -->
<!-- 				<td > -->
<!-- 				<div style="width:98%"> -->
<%-- 					<tiles:insert page="/servicioCliente/estadoPedido/detalleEstadoPedidoComun.jsp"/> --%>
<!-- 				</div> -->
<!-- 				</td> -->
<!-- 			</tr>	 -->
<%-- 		</logic:empty>	 --%>
	</table>
</logic:notEmpty>