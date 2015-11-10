<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>	
	<div id="reportePedidoTxt">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
			<meta http-equiv="Content-Style-Type" content="text/css">
			<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
			<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
			<meta HTTP-EQUIV="max-age" CONTENT="0">
			<meta HTTP-EQUIV="Expires" CONTENT="0">
			<%--link href="../../css/textos.css" rel="stylesheet" type="text/css"--%>
			<title>Reporte</title>
			<style type="text/css">
				body {
					font-family: Verdana, Arial, Helvetica;
					font-size: 11px;
				}
				.fila_cabecera{
					border-bottom-width: 1px;
					border-bottom-style: solid;
					
					border-top-width: 1px;
					border-top-style: solid;
					
					border-left-width: 1px;
					border-left-style: solid;
				}
				.fila_cabecera_final{
					border-width: 1px;
					border-style: solid;
				}
				
				.fila_detalle{
					border-bottom-width: 1px;
					border-bottom-style: solid;
					
					border-left-width: 1px;
					border-left-style: solid;
				}
				.fila_detalle_final{
					border-bottom-width: 1px;
					border-bottom-style: solid;
					
					border-left-width: 1px;
					border-left-style: solid;
					
					border-right-width: 1px;
					border-right-style: solid;
				}
				.borde_inferior{
					border-bottom-width: 1px;
					border-bottom-style: solid;
				}
				.saltoDePagina{
					PAGE-BREAK-AFTER: always
				}
				.textoNegro10{
					font-family: Verdana, Arial, Helvetica;
					font-size: 10px;
					color: #000000;
				}
			</style>
		</head>
		
		<body>
			<%-- lista de definiciones para las acciones --%>
			<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
			<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
			<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
			<bean:define id="codigoEstablecimientoAki"><bean:message key="ec.com.smx.sic.sispe.codigoTipoEstablecimiento.aki"/></bean:define>
			<logic:notEmpty name="sispe.valor.iva"><bean:define id="valorIVA" name="sispe.valor.iva"/></logic:notEmpty>
			<logic:notEmpty name="sispe.estado.activo"><bean:define id="estadoActivo" name="sispe.estado.activo"/></logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
				<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
				<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
			</logic:notEmpty>
			
			<bean:define id="clasificacionCanastoEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
			<bean:define id="clasificacionCanastosCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo">
				<bean:define id="codigoEstablecimiento" name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
				<bean:define id="numeroBonos" name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.monto.minimoCompra">
				<bean:define id="montoMinimoCompra" name="ec.com.smx.sic.sispe.pedido.monto.minimoCompra"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.valor.bono.maxinavidad">
				<bean:define id="valorBono" name="ec.com.smx.sic.sispe.pedido.valor.bono.maxinavidad"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.valor.calculado.maxinavidad">
				<bean:define id="valorCalculado" name="ec.com.smx.sic.sispe.pedido.valor.calculado.maxinavidad"/>
			</logic:notEmpty>
			<logic:notEmpty name="sispe.codigo.limite.validez.reserva">
				<bean:define id="horasValidezReserva" name="sispe.codigo.limite.validez.reserva"/>
			</logic:notEmpty>
			
			<bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
			<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
			<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
			<bean:define id="afiliado" name="ec.com.smx.sic.sispe.pedido.preciosAfiliado"/>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
				<tr>
					<td valign="top">
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<!-- OANDINO: Presentación de imagen de código de barras -->
							<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
								<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
									<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS">
										<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.pedidoDTO"></bean:define>
										<tr>
											<td align="left"></td>
											<td colspan="2" align="right">
												<img src="${pageContext.request.contextPath}/CodigoBarras?data=${vistaPedidoDTO.npLlaveContratoPOS}&height=50&width=1"/>
											</td>
										</tr>
									</logic:notEmpty>
								</logic:empty>
							</logic:equal>
							<tr>
								<td align="left"></td>
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
									<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
										<td align="right">
											No de reservaci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS"/>
										</td>
									</logic:empty>
								</logic:equal>
								<td align="left">&nbsp;&nbsp;</td>
							</tr>
							<!-- --------------------------------------------------- -->
							<tr>
								<td align="left">
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&nbsp;-&nbsp;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&nbsp;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
								</td>
								<td align="right">Ruc:&nbsp;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
							</tr>
							<tr><td height="10px" colspan="2"></td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center" valign="top">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td align="left">
									<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
										<bean:message key="ec.com.smx.sic.sispe.titulo.cotizacion"/>
										&nbsp;(V&aacute;lida por&nbsp;<bean:write name="ec.com.smx.sic.sispe.cotizacion.diasValidez"/>&nbsp;d&iacute;as)
									</logic:notEqual>
									<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
										<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
											<bean:message key="ec.com.smx.sic.sispe.titulo.reservacion"/>
										</logic:empty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
											<bean:message key="ec.com.smx.sic.sispe.titulo.cotizacion"/>
											&nbsp;(V&aacute;lida por&nbsp;<bean:write name="ec.com.smx.sic.sispe.cotizacion.diasValidez"/>&nbsp;d&iacute;as)
										</logic:notEmpty>
									</logic:equal>
								</td>
							</tr>
							<tr><td><hr/></td></tr>
							<tr>
								<td width="100%">
									<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
										<tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
												<td align="left">
													No. de pedido:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>
												</td>
												<%--<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
													<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
														<td align="left">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No de Reservaci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="llaveContratoPOS"/>
														</td>
													</logic:empty>
												</logic:equal>--%>
											</logic:notEmpty>
										</tr>
										<tr><td height="5px"></td></tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="100%">
										<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
										<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
											<tiles:put name="tformName" value="cotizarRecotizarReservarForm"/>
											<tiles:put name="textoNegro" value="textoNegro11"/>
											<tiles:put name="textoAzul" value="textoNegro11"/>
										</tiles:insert>
									</table>
								</td>
							</tr>
							<tr><td height="8px"></td></tr>
							<tr>
								<td colspan="2" align="right">
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td align="left" width="20%">
												Lugar y fecha:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
											</td>
											<td align="left" width="40%">
												Elaborado en:&nbsp;<bean:write name="cotizarRecotizarReservarForm" property="localResponsable"/>
											</td>
										</tr>
										<tr><td height="7px"></td></tr>
										<tr>
											<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left" width="20%">
													Tel&eacute;fono local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
												</td>
												<td align="left" width="40%">
													Administrador local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
												</td>
											</logic:empty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left" width="20%">
													Tel&eacute;fono local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
												</td>
												<td align="left" width="40%">
													Administrador local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
												</td>
											</logic:notEmpty>
										</tr>
										<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
											<tr><td height="7px"></td></tr>
											<tr>
												<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
													<tr>
														<td class="titulo" align="left">
															La mercader&iacute;a ser&aacute; reservada por un plazo de 36 horas y los precios se mantendran por 5 d&iacute;as, si no existe un abono inicial en ${horasValidezReserva}  horas la reserva ser&aacute; ANULADA.
														</td>
													</tr>
												</table>
											</tr>
										</logic:equal>
									</table>
								</td>
							</tr>
							<tr><td height="10px"></td></tr>
						</table>
					</td>
				</tr>
				<tr><td width="100%"><hr/></td></tr>
				<tr>
					<td>
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td colspan="8">
									Detalle del pedido (I: valor aplicado el IVA)
								</td>
							</tr>
							<tr><td height="6px" colspan="8"></td></tr>
							<tr>
								<td colspan="8">
									<c:set var="totalPrevio" value="0"/>
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr align="left">
											<td align="left" width="20px" valign="top" class="fila_cabecera">No</td>
											<td align="left" valign="top" class="fila_cabecera">C&oacute;digo barras</td>
											<td align="left" valign="top" class="fila_cabecera">Art&iacute;culo</td>
											<td align="right" valign="top" class="fila_cabecera">Cant.</td>
											<td align="right" valign="top" class="fila_cabecera">Peso KG.</td>
											<td align="right" valign="top" class="fila_cabecera">V.unit.</td>
											<td align="right" valign="top" class="fila_cabecera">V.unit.IVA</td>
											<td align="right" valign="top" class="fila_cabecera">
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>Tot. bruto</td>
													</tr>
													<tr>
														<td>Inc. IVA</td>
													</tr>
												</table>
											</td>
											<%--<td align="right" width="6%" valign="top">V.Unit. Neto</td>--%>
											<td align="right" valign="top" class="fila_cabecera_final">
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td>Tot. neto</td>
													</tr>
													<tr>
														<td>Inc. IVA</td>
													</tr>
												</table>
											</td>
										</tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
											<bean:define id="estilo_detalle" value="fila_detalle"/>
											<bean:define id="estilo_detalle_final" value="fila_detalle_final"/>
											<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numPedido">
												<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
												<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
													<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
												</logic:equal>
												<bean:define id="fila" value="${numPedido + 1}"/>
												<c:set var="pesoVariable" value=""/>
												<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
												</logic:equal>
												<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
												</logic:equal>
												<logic:notEqual name="numPedido" value="0">
													<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
														<logic:notEmpty name="detalle" property="entregas">
															<bean:define id="estilo_detalle" value="fila_cabecera"/>
															<bean:define id="estilo_detalle_final" value="fila_cabecera_final"/>
														</logic:notEmpty>
													</logic:notEmpty>
												</logic:notEqual>
												<tr valign="top">
													<td align="left" class="${estilo_detalle}"><bean:write name="fila"/></td>
													<td align="left" class="${estilo_detalle}"><bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
													<c:if test="${detalle.estadoDetallePedidoDTO.cantidadReservarSIC > detalle.articuloDTO.npStockArticulo}">
													   <td align="left" class="${estilo_detalle}">
														<table width="100%" border="0" cellpadding="0" cellspacing="0">
															<tr>
																<td align="right">*</td>
																<td align="left"><bean:write name="detalle" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="detalle" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
															</tr>
														</table>
													  </td>
													</c:if>
													<c:if test="${detalle.estadoDetallePedidoDTO.cantidadReservarSIC <= detalle.articuloDTO.npStockArticulo}">
													  <td align="left" class="${estilo_detalle}">
														<table width="100%" border="0">
															<tr>
																<td align="left"><bean:write name="detalle" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="detalle" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
															</tr>
														</table>
													  </td>
													</c:if>
													<td align="right" class="${estilo_detalle}">
														<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
														</logic:notEqual>
														<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">&nbsp;</logic:equal>
													</td>
													<td align="right" class="${estilo_detalle}">
														<logic:equal name="pesoVariable" value="${estadoActivo}">
															<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
														</logic:equal>
														<logic:notEqual name="pesoVariable" value="${estadoActivo}">&nbsp;</logic:notEqual>
													</td>
													<td align="right" class="${estilo_detalle}">
														<table cellpadding="0" cellspacing="0" width="100%">
															<tr>															
																<td align="left" >
																	<logic:equal name="detalle" property="articuloDTO.codigoClasificacion" value="1606">
																		<b>T</b>
																	</logic:equal>
																</td>															
																<td align="right" >
																	<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																		<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																		</logic:equal>
																		<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																		</logic:notEqual>
																	</logic:notEmpty>
																	<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																		<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																		</logic:equal>
																		<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																		</logic:notEqual>
																	</logic:empty>
																</td>
															</tr>
														</table>
													</td>												
													<td align="right" class="${estilo_detalle}">
														<table cellpadding="0" cellspacing="0" width="100%">
															<tr>		
																<td align="left" >
																	<logic:equal name="detalle" property="articuloDTO.codigoClasificacion" value="1606">
																		<b>T</b>
																	</logic:equal>
																</td>																														
																<td align="right" >
																	<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																		<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																		</logic:equal>
																		<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																		</logic:notEqual>
																	</logic:notEmpty>
																	<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
																		<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																		</logic:equal>
																		<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																		</logic:notEqual>
																	</logic:empty>
																</td>
															</tr>
														</table>
													</td>
													<td align="right" class="${estilo_detalle}">
														<table border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td>&nbsp;&nbsp;</td>
																<td align="right" valign="top">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
																</td>
																<td align="right" valign="top">																
																	<c:set var="aplicaImpuesto" value="${detalle.articuloDTO.aplicaImpuestoVenta}"/>							
																	<c:if test="${aplicaImpuesto}">
																		I
																	</c:if>
																	<c:if test="${!aplicaImpuesto}">
																		&nbsp;
																	</c:if>
																</td>
																<td>&nbsp;</td>
															</tr>
														</table>
													</td>
													<%--<td align="right">
														<bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
													</td>--%>
													<td align="right" class="${estilo_detalle_final}">
														<table border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td>&nbsp;&nbsp;</td>
																<td align="right" valign="top">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
																</td>
																<td>&nbsp;</td>
															</tr>
														</table>
													</td>	
												</tr>
												<%-- secci&oacute;n que muestra las entregas --%>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
													<logic:notEmpty name="detalle" property="entregaDetallePedidoCol">
														<tr>
															<td colspan="2"></td>
															<td align="right" colspan="4">
																<%-- se muestra la colecci&oacute;n de entregas del articulo --%>
																<table border="0" cellspacing="0" cellpadding="0" width="100%" align="right">
																	<tr>
																		<td align="center" valign="top" class="fila_detalle">Cant. ent.</td>
																		<td align="center" valign="top" class="fila_detalle">Fecha entrega</td>
																		<td align="left" valign="top" class="fila_detalle_final">Lugar entrega</td>
																	</tr>
																	<logic:iterate name="detalle" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO">
																	<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO">
																		<bean:define id="entregaPedidoDTO" property="entregaPedidoDTO" name="entregaDetallePedidoDTO"></bean:define>
																	</logic:notEmpty>
																		<tr>
																			<td align="center" class="fila_detalle"><bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/></td>
																			<td align="center" class="fila_detalle">&nbsp;<bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>&nbsp;</td>
																			<td align="left" class="fila_detalle_final">
																   <table border="0" width="100%" cellpadding="1" cellspacing="0">
																	  <tr>
																	  	<td>
																		   <logic:notEmpty name="entregaPedidoDTO" property="divisionGeoPoliticaDTO">
																		  	 <bean:write name="entregaPedidoDTO" property="divisionGeoPoliticaDTO.nombreDivGeoPol"/>
																		  	  - 
																		   </logic:notEmpty>
																		   <!-- Mostrar Convenio vinculado a la entrega a domicilio desde local -->
																		   <logic:notEmpty name="entregaPedidoDTO" property="entregaPedidoConvenioCol">
																		  		 E.DOM 
																				   <logic:iterate name="entregaPedidoDTO" property="entregaPedidoConvenioCol" id="entregaPedidoConvenio">
																				 		<bean:write name="entregaPedidoConvenio" property="secuencialConvenio"/>
																				   </logic:iterate>
																			   
																			    - 
																		   </logic:notEmpty>
																		   <bean:write name="entregaPedidoDTO" property="direccionEntrega"/>
																		   <logic:equal name="entregaPedidoDTO" property="codigoBodega" value="97">
																				<table border="0" cellpadding="0" cellspacing="0">
																				  <tr>
																				   <td title="Bodega de Tránsito">-(Tráns)</td>
																				  </tr>
																				</table> 
																		   </logic:equal>
																	   </td>
																	  </tr>
																	</table>	
																  
																</td>
																		</tr>
																	</logic:iterate>
																</table>
															</td>
															<td colspan="2"></td>
														</tr>
														<tr><td height="5px" colspan="8"></td></tr>
													</logic:notEmpty>
												</logic:notEmpty>
											</logic:iterate>
										</logic:notEmpty>
										<%--tr>
											<td colspan="7" align="right"><bean:write name="subTotalBrutoNoAplicaIva" formatKey="formatos.numeros"/></td>
											<td colspan="3">&nbsp;</td>
										</tr--%>
										<tr>
										<td colspan="6"></td>
										<td colspan="2" class="textoNegro10" align="right">TOTAL A PAGAR:&nbsp; </td>
										<td align="right" class="textoNegro10 fila_detalle_final">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotal}"/>
										</td>
										</tr>
										<tr><td colspan="8">(-) El descuento se aplica sobre el precio del art&iacute;culo sin IVA</td></tr>
										<tr><td height="5px"></td></tr>
										<tr>
											<td colspan="3">
												<c:set var="totalDescuento" value="0"/>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
													<%-- se muestra el detalle de los descuentos --%>
													<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
														<%--c:set var="valorTotalAplica" value="0"/--%>
														<table border="0" cellspacing="0" width="100%" cellpadding="0">
															<tr align="left">
																<td colspan="4">Detalle de los descuentos sin IVA</td>
															</tr>
															<tr><td height="6px"></td></tr>
															<tr>
																<td align="left">
																	<table border="0" cellspacing="0" width="100%" cellpadding="1">
																		<tr align="left">
																			<td align="center" width="40%">DESCRIPCI&Oacute;N&nbsp;</td>
																			<td align="center" width="15%">V.TOTAL&nbsp;</td>
																			<td align="center" width="15%">PORCENTAJE&nbsp;</td>
																			<td align="center" width="15%">DESCUENTO&nbsp;</td>
																		</tr>
																		<tr><td colspan="4" height="5px" class="borde_inferior"></td></tr>
																		<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
																			<tr>
																				<td align="left" width="40%"><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/>&nbsp;</td>
																				<td align="right" width="15%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorPrevioDescuento}"/>&nbsp;</td>
																				<td align="right" width="15%">
																					<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																					</logic:greaterThan>
																					<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>&nbsp;
																				</td>
																				<td align="right" width="15%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
																			</tr>
																			<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
																			<%--c:set var="valorTotalAplica" value="${valorTotalAplica + descuento.valorPrevioDescuento}"/--%>
																		</logic:iterate>
																		<tr><td height="5px" colspan="4" class="borde_inferior"></td></tr>
																		<tr>
																			<td align="right">T. DESCUENTO:</td>
																			<%--td align="right"><bean:write name="valorTotalAplica" formatKey="formatos.numeros"/>&nbsp;</td--%>
																			<%--td align="right" colspan="2"><bean:write name="ec.com.smx.sic.sispe.pedido.descuento.porcentajeTotal" formatKey="formatos.numeros"/>%&nbsp;</td--%>
																			<td align="right" colspan="3"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/>&nbsp;</td>
																		</tr>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
																			<bean:define id="numeroBonos" name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir"/>
																			<tr><td height="5px" colspan="4" class=""></td></tr>
																			<tr><td colspan="4" align="center" class="fila_cabecera fila_detalle_final">NRO DE BONOS A RECIBIR:&nbsp;<bean:write name="numeroBonos" formatKey="formatos.enteros"/></td></tr>
																		</logic:notEmpty>
																	</table>
																</td>
															</tr>
														</table>
													</logic:notEmpty>
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
													<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
														<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
															<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
														</logic:iterate>
													</logic:notEmpty>
												</logic:empty>
											</td>
											<td colspan="6" align="right">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td align="center" colspan="2">RESUMEN TRIBUTARIO</td>
													</tr>
													<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<tr>
														<td align="right">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalBrutoNoAplicaIva}"/>
														</td>
													</tr>
													<tr>
														<td align="right">(-)DESCUENTO:&nbsp;</td>
														<td align="right">		
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.totalDescuentoIva}"/>											
														</td>
													</tr>
													<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<tr><td colspan="2" align="right" height="5px"></td></tr>
													<tr valign="bottom">
														<td align="right">SUBTOTAL NETO:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNetoBruto}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td align="right">TARIFA 0%:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
														</td>
													</tr>
													<tr valign="bottom"><td colspan="2" align="right" height="5px"></td></tr>
													<tr valign="bottom">
														<td align="right">COSTO FLETE:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
														</td>
													</tr>
													<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<tr valign="bottom">
														<td align="right">TOTAL:&nbsp;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/>
														</td>
													</tr>
													<tr><td height="6px"></td></tr>
												</table>
											</td>
											<td>&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionCanastos">
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.existenCanastos">
						<tr><td height="20px"></td></tr>
						<tr><td><hr/></td></tr>
						<tr><td>Detalle de los canastos</td></tr>
						<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detallePedidoDTO" indexId="posPedido">
							<c:if test="${detallePedidoDTO.articuloDTO.codigoClasificacion == clasificacionCanastoEspecial|| detallePedidoDTO.articuloDTO.codigoClasificacion == clasificacionCanastosCatalogo}">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="0">
											<tr><td height="10px" colspan="2"></td></tr>
											<tr>
												<td align="right">Descripci&oacute;n:&nbsp;</td>
												<td align="left"><bean:write name="detallePedidoDTO" property="articuloDTO.descripcionArticulo"/></td>
											</tr>
											<tr>
												<logic:notEqual name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
													<td align="right">Precio:&nbsp;</td>
													<td align="left"><bean:write name="detallePedidoDTO" property="estadoDetallePedidoDTO.valorUnitarioIVAEstado"/></td>
												</logic:notEqual>											
												<logic:equal name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
													<td align="right">Precio tot. sin IVA.:&nbsp;</td>
													<td align="left"><bean:write name="detallePedidoDTO" property="estadoDetallePedidoDTO.valorUnitarioEstado"/></td>
													<td align="right">Precio total:&nbsp;</td>
													<td align="left"><bean:write name="detallePedidoDTO" property="estadoDetallePedidoDTO.valorUnitarioIVAEstado"/></td>
												</logic:equal>
												<td align="right" width="60px">&nbsp;</td>
												<td align="right">Cantidad:&nbsp;</td>
												<td align="left"><bean:write name="detallePedidoDTO" property="estadoDetallePedidoDTO.cantidadEstado"/></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="10px"></td></tr>
								<logic:notEmpty name="detallePedidoDTO" property="articuloDTO">
									<tr>
										<td>
											<table cellpadding="1" cellspacing="0" width="100%">
												<tr>
													<td align="left" class="fila_cabecera">No</td>
													<td align="left" class="fila_cabecera">C&Oacute;DIGO BARRAS</td>
													<td align="left" class="fila_cabecera">ART&Iacute;CULO</td>
													<logic:notEqual name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
														<td align="right" class="fila_cabecera_final">CANT.</td>
													</logic:notEqual>
													<logic:equal name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
														<td align="right" class="fila_cabecera">CANT.</td>
														<td align="right" class="fila_cabecera">V.UNIT.</td>
														<td align="right" class="fila_cabecera">V.UNIT.IVA</td>
														<td align="right" class="fila_cabecera">T.NETO.INC.IVA</td>													
														<td align="right" class="fila_cabecera_final">IVA</td>
													</logic:equal>
												</tr>
												<tr><td colspan="7" height="5px"></td></tr>
												<logic:iterate name="detallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
													<c:set var="unidadManejo" value="${recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
													<logic:equal name="recetaArticuloDTO" property="articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
													  <c:set var="unidadManejo" value="${recetaArticuloDTO.articulo.unidadManejo}"/>
													</logic:equal>
													<tr>
														<bean:define id="fila" value="${registroReceta + 1}"/>
														<td align="left"><bean:write name="fila"/></td>
														<td align="left"><bean:write name="recetaArticuloDTO" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
														<td align="left"><bean:write name="recetaArticuloDTO" property="articuloRelacion.descripcionArticulo"/>,&nbsp;
														<bean:write name="recetaArticuloDTO" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;
														${unidadManejo}</td>
														<td align="right"><bean:write name="recetaArticuloDTO" property="cantidad"/></td>
														<logic:equal name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
															<td align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.articuloRelacion.precioBase}"/>
															</td>
															<td align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.articuloRelacion.precioBaseImp}"/>
															</td>
															<td align="right">
																<c:if test="${!recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
																<c:if test="${recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${(recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento)  * 1.12}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
															</td>
															<td align="right">
																<c:if test="${!recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">&nbsp;</c:if>
																<c:if test="${recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">I</c:if>
															</td>	
														</logic:equal>
													</tr>
												</logic:iterate>
												<logic:equal name="detallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
												<tr>	
													<td colspan="3" />
													<td align="right">TOTAL:</td >
													<td align="right">												
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:notEqual>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:notEqual>
														</logic:empty>											
													</td>
													<td align="right" >
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:notEqual>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:notEqual>
														</logic:empty>
													</td>
													<td align="right" >
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
													</td>
													<td align="right">&nbsp;</td> 
												</tr>
												</logic:equal>
											</table>
										</td>
									</tr>
								</logic:notEmpty>
								<logic:empty name="detallePedidoDTO" property="articuloDTO">
									<tr><td>Canasto vacio</td></tr>
								</logic:empty>
								<tr><td><hr/></td></tr>
							</c:if>
						</logic:iterate>
					</logic:notEmpty>
				</logic:notEmpty>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDiferidos">
					<logic:notEmpty name="ec.com.smx.sic.sispe.reporteDiferidos">
						<tr><td height="20px"></td></tr>
						<tr><td align="left">Detalle de art&iacute;culos que aplican diferidos (Sin intereses)</td></tr>
						<tr><td><hr/></td></tr>
						<tr>
							<td>
								<c:set var="totalDiferidos" value="${0}"/>
								<c:set var="valorCero" value="0.00"/>
								<table width="100%" border="0" cellpadding="1" cellspacing="0">
									<tr>
										<td align="center" width="10px" valign="top">No</td>
										<td align="center" width="55px" valign="top">C&oacute;digo barras</td>
										<td align="center" width="150px" valign="top">Art&iacute;culo</td>													
										<td align="center" width="35px"valign="top">Precio uni. no/afi</td>
										<td align="center" width="35px"valign="top">Precio uni. afi</td>
										<td align="center" width="25px"valign="top">Cant.&nbsp;</td>
										<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO" indexId="indiceCuota">
											<td align="center" width="5px" valign="top"><bean:write name="cuotaDTO" property="numeroCuotas"/>MESES&nbsp;</td>
										</logic:iterate>
										<td align="center" width="25px"valign="top">TOTAL</td>
									</tr>
									<tr><td colspan="9" height="5px"></td></tr>
									<c:set var="subTotal" value="${0}"/>
									<logic:iterate name="ec.com.smx.sic.sispe.reporteDiferidos" id="extructuraDiferidos" indexId="indiceDif">
										<bean:define id="fila" value="${indiceDif + 1}"/>
										<tr valign="middle">
											<td width="10px" align="left"><bean:write name="fila"/></td>
											<td width="55px" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
											<td width="150px" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;(										
											<logic:empty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												&nbsp;,
											</logic:empty>
											<logic:notEmpty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												<bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
											</logic:notEmpty>
											)</td>
											<td width="35px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseNoAfiImp}"/></td>
											<td width="35px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseImp}"/></td>
											<td width="25px" align="center"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado"/></td>
											<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
												<c:if test="${duplex.secondObject != valorCero}">
													<td align="center" width="15px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
												</c:if>
												<c:if test="${duplex.secondObject == valorCero}">
													<td align="center" width="15px">-</td>
												</c:if>
											</logic:iterate>
											<c:set var="subTotal" value="${extructuraDiferidos.detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado * extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseImp}"/>
											<c:set var="totalDiferidos" value="${totalDiferidos + subTotal}"/>
											<td width="25px" align="center"><bean:write name="subTotal"/></td>
										</tr>		
									</logic:iterate>
								</table>
							</td>
						</tr>
						<tr><td height="10px"></td></tr>
						<tr>
							<td colspan="14">
								<table border="0" cellspacing="0" cellpadding="0" align="right">
									<tr>
										<td align="left">TOTAL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										<td align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDiferidos}"/>
										</td>
									</tr>
									<tr><td colspan="2" align="right">-------------------------</td></tr>
									<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
										<tr>
										 <td align="right"><bean:write name="cuotaDTO1" property="numeroCuotas"/>&nbsp;MESES:&nbsp;&nbsp;&nbsp;&nbsp;</td>
										 <c:if test="${totalDiferidos >= cuotaDTO1.valorMinimo}">
											<c:set var="totalMonto" value="${(totalDiferidos/cuotaDTO1.numeroCuotas)*cuotaDTO1.valorInteres}"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalMonto}"/>
										 </c:if>
										 <c:if test="${totalDiferidos < cuotaDTO1.valorMinimo}">
											<td align="right">-</td>
										 </c:if>
										</tr>
									</logic:iterate>
								</table>
							</td>
						</tr>
						<tr><td><hr/></td></tr>
					</logic:notEmpty>
				</logic:notEmpty>
				<%-- mensaje final del reporte (depende del tipo de establecimiento) --%>
				<tr><td height="20px"></td></tr>
				
				<%-- valido que el pedido este en estado reservado para poder desplegar la nota --%>
				<%-- <logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}"> --%>
				 <tr><td align="left">NOTAS:<br/></td></tr>
				 <tr>
				 	<td align="left">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
						 	<tr>
							 	<td width="25px" valign="top">1.-</td>
							 	<td>Los art&iacute;culos que estan marcados con un (*) no tienen suficiente stock.</td>
							 </tr>
						 </table>
					</td>
				 </tr>	
				 <tr>
				 	<td align="left">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
						 	<tr>
							 	<td width="25px" valign="top">2.-</td>
							 	<td>Le recordamos que el archivo del beneficiario debe tener la siguiente informaci&oacute;n:<br/>C&eacute;dula, nombre, tel&eacute;fono, local o direcci&oacute;n de domicilio, fecha de entrega.<br/></td>
							 </tr>
						 </table>
					</td>
				 </tr>	
				 <tr><td height="20px"></td></tr>
				<%-- </logic:equal> --%>
				<tr><td align="left">OBSERVACIONES:<br/></td></tr>
				<tr><td height="5px"></td></tr>
				<tr>
					<td>
						<table cellpadding="1" cellspacing="0">
							<tiles:insert page="/reportes/observacionesPedido.jsp">
								<tiles:put name="codEst" value="${codigoEstablecimiento}"/>
								<tiles:put name="codEstAki" value="${codigoEstablecimientoAki}"/>
								<tiles:put name="estadoAfilacion" value="${afiliado}"/>
							</tiles:insert>
						</table>
					</td>
				</tr>
				<tr><td height="30px">&nbsp;</td></tr>
				<tr><td align="left">Firma y sello: _________________________________						Elaborado por: <bean:write name="vistaEntidadResponsableDTO" property="nombreCompletoUsuario"/></td></tr>
				
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">				
					<div class="saltoDePagina">			
					</div>
					<tr>
						<td>
							<table cellpadding="1" cellspacing="0" width="100%">
								<tiles:insert page="/reportes/comprobanteBonoReserva.jsp">
									<tiles:put name="nBonos" value="${numeroBonos}"/>
									<tiles:put name="mCompra" value="${montoMinimoCompra}"/>
									<tiles:put name="mBono" value="${valorBono}"/>
									<tiles:put name="vCalculado" value="${valorCalculado}"/>
								</tiles:insert>
							</table>
						</td>
					</tr>
				</logic:notEmpty>
			</table>
		</body>
	</div>
</html>