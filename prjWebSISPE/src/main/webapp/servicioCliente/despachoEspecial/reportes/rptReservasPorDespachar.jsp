<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
	<div id="rptReservasPorDespacharTxt">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
			<meta http-equiv="Content-Style-Type" content="text/css">
			<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
			<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
			<meta HTTP-EQUIV="max-age" CONTENT="0">
			<meta HTTP-EQUIV="Expires" CONTENT="0">		
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
				.bordes_columna{									
					border-left-width: 1px;
					border-left-style: solid;
				}				
				.bordes_columna_final{						
					border-left-width: 1px;
					border-left-style: solid;
					
					border-right-width: 1px;
					border-right-style: solid;
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
			</style>
		</head>
		<body>
			<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
			<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
			<bean:define name="sispe.estado.activo" id="estadoActivo"/>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoReservado">
				<bean:define name="ec.com.smx.sic.sispe.pedido.estadoReservado" id="estadoReservado"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial">
				<bean:define name="ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial" id="tipoPedidoEmpresarial"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
				<bean:define name="ec.com.smx.sic.sispe.tipoArticulo.canasto" id="tipoCanasto"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
				<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo">
				<bean:define id="clasificacionCanastosCatalogo" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas">
				<bean:define id="clasificacionCanastoEspecial" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/>
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
			<c:set var="existenCanastos" value=""/>
			<bean:define id="codigoEstablecimientoAki"><bean:message key="ec.com.smx.sic.sispe.codigoTipoEstablecimiento.aki"/></bean:define>
			<%--- se definen los c&oacute;digos de las posibles entidades responsables --------%>
			<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
			<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>					
						
			<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
			<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
			
			<logic:iterate name="ec.com.smx.sic.sispe.imprimir.reservas" id="impReservas">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
				<tr><td width="100%"><hr/></td></tr>
				<tr>
					<td valign="top">
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<tr>
								<td align="right" width="50%">
									<b>No. de pedido:&nbsp;</b><bean:write name="impReservas" property="id.codigoPedido"/>
								</td>
								<td align="left" width="50%">
									<b>&nbsp;&nbsp;No de reservaci&oacute;n:&nbsp;</b><bean:write name="impReservas" property="llaveContratoPOS"/>
								</td>								
							</tr>							
							<tr><td height="10px" colspan="2"></td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center" valign="top">
						<table border="0" width="100%" align="center" cellpadding="0" cellspacing="0">														
							<tr>
								<td>
									<table border="0" width="100%" cellpadding="0" cellspacing="0">
										<tr><td height="5px" colspan="2"></td></tr>
										<tr>											
											<td align="left" width="50%">
												Elaborado en:&nbsp;<bean:write name="impReservas" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="impReservas" property="nombreLocal"/>
											</td>
										</tr>
										<tr><td height="5px" colspan="2"></td></tr>
										<tr>
											<td align="left" width="50%">
												Primera fecha de despacho:&nbsp;<bean:write name="impReservas" property="primeraFechaDespacho" formatKey="formatos.fecha"/>
											</td>
											<td align="left" width="50%">
												Primera fecha de entrega:&nbsp;<bean:write name="impReservas" property="primeraFechaEntrega" formatKey="formatos.fechahora"/>
											</td>
										</tr>																			
									</table>
								</td>
							</tr>
							<tr><td height="5px"></td></tr>
						</table>
					</td>
				</tr>		
				<tr>
					<td align="center" valign="top">
						
					</td>
				</tr>				
				<logic:notEmpty name="impReservas" property="vistaDetallesPedidosReporte">
					<tr>
						<td>
							<c:set var="totalPrevio" value="0"/>
							<table width="100%" border="0" cellpadding="1" cellspacing="0">
								<tr>
									<td colspan="8">
										Detalle del pedido (I: valor aplicado el IVA)
									</td>
								</tr>
								<tr><td height="6px" colspan="8"></td></tr>
								<tr align="left">
									<td align="left" width="20px" valign="top" class="fila_cabecera">No</td>
									<td align="left" valign="top" class="fila_cabecera">C&oacute;digo barras</td>
									<td align="left" valign="top" class="fila_cabecera">Art&iacute;culo</td>
									<td align="right" valign="top" class="fila_cabecera">Cant.</td>
									<td align="right" valign="top" class="fila_cabecera">Peso Kg.</td>
									<td align="right" valign="top" class="fila_cabecera">V.unit</td>
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
								<bean:define id="estilo_detalle" value="fila_detalle"/>
								<bean:define id="estilo_detalle_final" value="fila_detalle_final"/>
								<logic:iterate name="impReservas" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
								
									<c:set var="totalPrevio" value="${totalPrevio + vistaDetallePedidoDTO.valorPrevioVenta}"/>
									
									<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
									<c:set var="pesoVariable" value=""/>
									<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
										<c:set var="pesoVariable" value="${estadoActivo}"/>
									</logic:equal>
									<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
										<c:set var="pesoVariable" value="${estadoActivo}"/>
									</logic:equal>																		
									
									<bean:define id="codigoSubClasificacion" name="vistaDetallePedidoDTO" property="codigoSubClasificacion"/>
																						
									<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
										<c:set var="existenCanastos" value="${estadoActivo}"/>
									</c:if>
									 <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
										<c:set var="existenCanastos" value="${estadoActivo}"/>
									</c:if>
										
									<logic:notEqual name="indiceDetalle" value="0">
										<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
											<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
												<bean:define id="estilo_detalle" value="fila_cabecera"/>
												<bean:define id="estilo_detalle_final" value="fila_cabecera_final"/>
											</logic:notEmpty>
										</logic:notEmpty>
									</logic:notEqual>										
									<tr>
										<td align="left" width="20px" valign="top" class="${estilo_detalle}"><bean:write name="numRegistro"/></td>
										<td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
										<c:if test="${vistaDetallePedidoDTO.cantidadReservarSIC > vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
										   <td align="left" valign="top" class="${estilo_detalle}">*<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
										</c:if>
										<c:if test="${vistaDetallePedidoDTO.articuloDTO.npStockArticulo == null || vistaDetallePedidoDTO.cantidadReservarSIC <= vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
										   <td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
										</c:if>
										
										<td align="right" valign="top" class="${estilo_detalle}">
											<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
												<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
											</logic:notEqual>
											<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">&nbsp;</logic:equal>
										</td>
										<td align="right" valign="top" class="${estilo_detalle}">
											<logic:equal name="pesoVariable" value="${estadoActivo}">
												<logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>												
												</logic:empty>
												<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoLocal}"/>
												</logic:notEmpty>
											</logic:equal>
											<logic:notEqual name="pesoVariable" value="${estadoActivo}">&nbsp;</logic:notEqual>		
										</td>
										<td align="right" class="${estilo_detalle}" valign="top">
											<table cellpadding="0" cellspacing="0" width="100%">
												<tr>															
													<td align="left">
														<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
															<b>T</b>
														</logic:equal>
													</td>															
													<td align="right">
														<logic:equal name="impReservas" property="estadoPreciosAfiliado" value="${estadoActivo}">										
															<logic:equal name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:equal>
															<logic:notEqual name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:notEqual>
														</logic:equal>
														<logic:notEqual name="impReservas" property="estadoPreciosAfiliado" value="${estadoActivo}">										
															<logic:equal name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:equal>
															<logic:notEqual name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:notEqual>
														</logic:notEqual>
													</td>
												</tr>
											</table>
										</td>		
										<td align="right" class="${estilo_detalle}" valign="top">
											<table cellpadding="0" cellspacing="0" width="100%">
												<tr>		
													<td align="left" >
														<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
															<b>T</b>
														</logic:equal>
													</td>																														
													<td align="right" >
														<logic:equal name="impReservas" property="estadoPreciosAfiliado" value="${estadoActivo}">										
															<logic:equal name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/>
															</logic:equal>
															<logic:notEqual name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:notEqual>
														</logic:equal>
														<logic:notEqual name="impReservas" property="estadoPreciosAfiliado" value="${estadoActivo}">										
															<logic:equal name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
															</logic:equal>
															<logic:notEqual name="impReservas" property="estadoCalculosIVA" value="${estadoActivo}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:notEqual>
														</logic:notEqual>
													</td>
												</tr>
											</table>
										</td>		
										<td align="right" valign="top" class="${estilo_detalle}">
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td>&nbsp;&nbsp;</td>
													<td align="right" valign="top">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>&nbsp;
													</td>
													<td align="right" valign="top">
														<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">I</logic:equal>
														<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">&nbsp;</logic:notEqual>
													</td>
													<td>&nbsp;</td>
												</tr>
											</table>
										</td>										
										<td align="right" valign="top" class="${estilo_detalle_final}">
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td>&nbsp;&nbsp;</td>
													<td align="right" valign="top">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
													</td>
													<td>&nbsp;</td>
												</tr>
											</table>
										</td>
									</tr>									
								</logic:iterate>
								<tr>
									<td height="5px" colspan="6"></td>
									<td colspan="2" class="textoNegro10" align="right">TOTAL A PAGAR:&nbsp; </td>
									<td align="right" class="textoNegro10 fila_detalle_final">
										<bean:define id="totalVenta" name="impReservas" property="totalPedido"/>									
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalVenta}"/>									
									</td>								
								</tr>								
								<tr><td height="5px"></td></tr>																
							</table>
						</td>
					</tr>
					<tr><td height="10px"></td></tr>
					<%-- control de la impresi&oacute;n de canastos --%>					
						<logic:equal name="existenCanastos" value="${estadoActivo}">
							<tr><td height="10px"></td></tr>							
							<tr><td align="left">Detalle de los canastos</td></tr>
							<logic:iterate name="impReservas" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">								
								<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial|| vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
									<tr>									
										<td>
											<table cellpadding="1" cellspacing="0" align="left">
												<tr><td height="10px" colspan="2"></td></tr>
												<tr>
													<td align="right">Descripci&oacute;n:&nbsp;</td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
													<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
														<td align="right">&nbsp;&nbsp;Precio:&nbsp;</td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
													</logic:notEqual>																																			
													<td align="right">&nbsp;&nbsp;Cantidad:&nbsp;</td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/></td>
												</tr>												
											</table>
										</td>
									</tr>
									<tr><td height="10px"></td></tr>
									<logic:notEmpty name="vistaDetallePedidoDTO" property="articuloDTO">
										<tr>
											<td>
												<table cellpadding="1" cellspacing="0" width="100%">
													<tr>
														<td align="left" class="fila_cabecera">No</td>
														<td align="left" class="fila_cabecera">C&Oacute;DIGO BARRAS</td>
														<td align="left" class="fila_cabecera">ART&Iacute;CULO</td>
														
															<td align="right" class="fila_cabecera_final">CANT.</td>
																												
													</tr>
													
													<logic:iterate name="vistaDetallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
														<tr>															
															<bean:define id="fila" value="${registroReceta + 1}"/>
															<td align="left" class="bordes_columna"><bean:write name="fila"/></td>
															<td align="left" class="bordes_columna"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
															<td align="left" class="bordes_columna"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>															
															<td align="right" class="bordes_columna_final"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>															
														</tr>
													</logic:iterate>
													<tr> 
														<td class="bordes_columna borde_inferior">&nbsp;</td>
														<td class="bordes_columna borde_inferior">&nbsp;</td>
														<td class="bordes_columna borde_inferior">&nbsp;</td>
														<td class="bordes_columna_final borde_inferior">&nbsp;</td>
													</tr>
												</table>
											</td>
										</tr>
									</logic:notEmpty>									
								</c:if>
							</logic:iterate>
						</logic:equal>					
				</logic:notEmpty>				
				<tr><td height="10px"></td></tr>
				<tr><td width="100%"><hr/></td></tr>				
			</table>
			</logic:iterate>
		</body>
	</div>
</html>