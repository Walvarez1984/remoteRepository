<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dTD">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<html>
	<head>
		<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="max-age" CONTENT="0">
		<meta HTTP-EQUIV="Expires" CONTENT="0">
		<style type="text/css">
			.textoRojoC13{
				font-family: Verdana, Arial, Helvetica;
				font-size: 10px;
				color: #E30207;
			}
			.fila_cabecera{
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #CCCCCC;
				
				border-top-width: 1px;
				border-top-style: solid;
				border-top-color: #CCCCCC;
				
				border-left-width: 1px;
				border-left-style: solid;
				border-left-color: #CCCCCC;
			}
			.fila_cabecera_final{
				border-width: 1px;
				border-style: solid;
				border-color: #CCCCCC;
			}
			
			.fila_detalle{
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #CCCCCC;
				
				border-left-width: 1px;
				border-left-style: solid;
				border-left-color: #CCCCCC;
			}
			.fila_detalle_final{
				border-bottom-width: 1px;
				border-bottom-style: solid;
				border-bottom-color: #CCCCCC;
				
				border-left-width: 1px;
				border-left-style: solid;
				border-left-color: #CCCCCC;
				
				border-right-width: 1px;
				border-right-style: solid;
				border-right-color: #CCCCCC;
			}
    	</style>
	</head>
	<body>
		<%-- lista de definiciones para las acciones --%>
		<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
		<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
		<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
		<bean:define id="estadoPedido"><bean:message key="ec.com.smx.sic.sispe.accion.estadoPedido"/></bean:define>
		<bean:define id="codigoEstablecimientoAki"><bean:message key="ec.com.smx.sic.sispe.codigoTipoEstablecimiento.aki"/></bean:define>
		<logic:notEmpty name="sispe.valor.iva"><bean:define id="valorIVA" name="sispe.valor.iva"/></logic:notEmpty>
		<logic:notEmpty name="sispe.estado.activo"><bean:define id="estadoActivo" name="sispe.estado.activo"/></logic:notEmpty>
		<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
		<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
		<bean:define id="accionPedidoEspecial"><bean:message key="ec.com.smx.sic.sispe.accion.crearPedidoEspecial"/></bean:define>	
		
		<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
			<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasto"/>
		</logic:notEmpty>
		<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
			<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
		</logic:notEmpty>		
		<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo">
			<bean:define id="clasificacionCanastosCatalogo" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/>
		</logic:notEmpty>
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
		<bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
		<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
		<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
		<bean:define id="afiliado" name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado"/>
		<bean:define id="accionActual" name="ec.com.smx.sic.sispe.accion"/>		

		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;text-align:justify">
			<tr><td height="10px"></td></tr>
			<tr>
				<td align="left" style="color:#FF0000;">
					<b>CORPORACIÓN FAVORITA C.A.</b>
				</td>
			</tr>
			<tr><td height="10px"></td></tr>
			<tr><td height="10px"></td></tr>
			<tr>
				<td valign="top" font-size="7pt">
					<table border="0" cellspacing="0" cellpadding="1" width="100%">
						<tr>
							<td align="left" style="font-size:9;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${cotizacion}">
									<bean:message key="ec.com.smx.sic.sispe.titulo.cotizacion"/>
									&nbsp;(V&aacute;lida por&nbsp;<bean:write name="ec.com.smx.sic.sispe.cotizacion.diasValidez"/>&nbsp;d&iacute;as)
								</logic:equal>
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
						<%--<tr>
							<td height="10px" colspan="2" align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 10px;color: #000000;font-weight: bold;">
								Ruc:&nbsp;<bean:message key="security.CURRENT_COMPANY_RUC"/>
							</td>
						</tr>--%>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center" valign="top" font-size="7pt">
					<table border="0" cellspacing="0" cellpadding="0" width="100%">
						<tr>
							<td width="100%">
								<table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
									<tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
											<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;color: #000000;">
												<b>No de pedido:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
											</td>
<%-- 											<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}"> --%>
<%-- 												<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal"> --%>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
												<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
													<b>No de reservaci&oacute;n:&nbsp;</b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
												</td>
											</logic:notEmpty>
<%-- 												</logic:empty> --%>
<%-- 											</logic:equal> --%>
										</logic:notEmpty>
									</tr>
									<tr><td height="5px"></td></tr>
								</table>
							</td>
						</tr>
						<tr><td><hr/></td></tr>
						<tr>
							<td>
								<table cellpadding="0" cellspacing="0" width="100%">
									<tr>
										<table border="0" width="100%" cellspacing="0" cellpadding="1" align="left" class="textoNegro10" style="font-family: Verdana, Arial, Helvetica, sans-serif;text-align:justify">											
											<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
											<tiles:insert page="/contacto/cabeceraContactoVistaPedidoMail.jsp"/>	
										</table>										
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="7px"></td></tr>
						<tr>
							<td colspan="2" align="right">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
											<b>Elaborado por:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/> - <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
										</td>
									
										<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
											<b>Lugar y fecha:</b>&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
										</td>
									</tr>
									<tr><td height="7px"></td></tr>
									<tr>
										<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
											<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
												<b>Administrador local:</b>&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
											</td>
											<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
												<b>Tel&eacute;fono local:</b>&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
											</td>
										</logic:empty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
											<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
												<b>Administrador local:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
											</td>
											<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
												<b>Tel&eacute;fono local:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
											</td>
										</logic:notEmpty>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td><hr/></td></tr>
						<tr><td height="10px"></td></tr>
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr>
								<td>
									<c:set var="totalPrevio" value="0"/>
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr>
											<td colspan="8" class="textoNegro10">
												Detalle del pedido (I: valor aplicado el IVA)
											</td>
										</tr>
										<tr><td height="6px" colspan="8"></td></tr>
										<tr align="left" style="background-color:#FF0F0F;color: #FFFFFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
											<td align="left" width="20px" valign="top" class="fila_cabecera">No</td>
											<td align="left" valign="top" class="fila_cabecera">C&oacute;digo barras</td>
											<td align="left" valign="top" class="fila_cabecera">Art&iacute;culo</td>
											<td align="right" valign="top" class="fila_cabecera">Cant.</td>
											<td align="right" valign="top" class="fila_cabecera">Peso KG.</td>
											<td align="right" valign="top" class="fila_cabecera">V.unit</td>
											<td align="right" valign="top" class="fila_cabecera">V.unit.IVA</td>
											<td align="right" valign="top" class="fila_cabecera">
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td  style="color: #FFFFFF;font-size: 9px;font-style: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">Tot. bruto</td>
													</tr>
													<tr>
														<td style="color: #FFFFFF;font-size: 9px;font-style: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">Inc. IVA</td>
													</tr>
												</table>
											</td>
											<%--td align="right" valign="top" class="fila_cabecera">Iva</td--%>
											<%--td align="right" valign="top" class="fila_cabecera">Dscto.</td--%>
											<%--<td align="right" valign="top">V.Unit Neto</td>--%>
											<td align="right" valign="top" class="fila_cabecera_final">
												<table border="0" cellpadding="0" cellspacing="0" width="100%">
													<tr>
														<td  style="color: #FFFFFF;font-size: 9px;font-style: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">Tot. neto</td>
													</tr>
													<tr>
														<td style="color: #FFFFFF;font-size: 9px;font-style: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">Inc. IVA</td>
													</tr>
												</table>
											</td>
										</tr>
										<bean:define id="estilo_detalle" value="fila_detalle"/>
										<bean:define id="estilo_detalle_final" value="fila_detalle_final"/>
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<c:set var="totalPrevio" value="${totalPrevio + vistaDetallePedidoDTO.valorPrevioVenta}"/>
											<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
												<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											</logic:equal>
											<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
											<c:set var="pesoVariable" value=""/>
											<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
												<c:set var="pesoVariable" value="${estadoActivo}"/>
											</logic:equal>
											<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
												<c:set var="pesoVariable" value="${estadoActivo}"/>
											</logic:equal>
											
											<%--<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoCanasto}">
												<c:set var="existenCanastos" value="${estadoActivo}"/>
											</logic:equal>
											<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoDespensa}">
												<c:set var="existenCanastos" value="${estadoActivo}"/>
											</logic:equal>--%>
											
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
											<tr valign="top" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
												<td align="left" width="20px" valign="top" class="${estilo_detalle}"><bean:write name="numRegistro"/></td>
												<td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
												<c:if test="${vistaDetallePedidoDTO.cantidadReservarSIC > vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
													<td align="left" class="${estilo_detalle}">
														<table width="100%" border="0" class="textoRojoC13">
															<tr>
																<td align="left" class="textoRojoC13" >*</td>
																<td align="left" class="textoRojoC13" ><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
															</tr>
														</table>
													</td>
												</c:if>
												<c:if test="${vistaDetallePedidoDTO.articuloDTO.npStockArticulo == null || vistaDetallePedidoDTO.cantidadReservarSIC <= vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
												   <td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
												</c:if>
												<%-- <td align="left" valign="top"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoInternoProveedor"/>,&nbsp;${unidadManejo}</td> --%>
												<td align="right" valign="top" class="${estilo_detalle}">
													<!-- PARA EL CASO DE PEDIDOS NORMALES -->																								
													<c:if test="${accionActual != accionPedidoEspecial && accionActual != estadoPedido}">													
														<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
														</logic:notEqual>
														<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">&nbsp;</logic:equal>
													</c:if>
													
													<!-- PARA EL CASO DE PEDIDOS ESPECIALES -->
													<c:if test="${accionActual == accionPedidoEspecial || accionActual == estadoPedido}">													
														<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
													</c:if>
												</td>
												<td align="right" valign="top" class="${estilo_detalle}" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
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
												<td align="right" class="${estilo_detalle}" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;" valign="top">
													<table cellpadding="0" cellspacing="0" width="100%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<tr>															
															<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																<b>T</b>
																</logic:equal>
															</td>															
															<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">										
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
																	</logic:equal>
																	<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
																	</logic:notEqual>
																</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">										
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																	</logic:equal>
																	<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																	</logic:notEqual>
																</logic:notEqual>
															</td>
														</tr>
													</table>
												</td>		
												<td align="right" class="${estilo_detalle}" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;" valign="top">
													<table cellpadding="0" cellspacing="0" width="100%" class="textoNegro9">
														<tr>		
															<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																	<b>T</b>
																</logic:equal>
															</td>																														
															<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">										
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																	</logic:equal>
																	<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/>
																	</logic:notEqual>
																</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">										
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																	</logic:equal>
																	<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoCalculosIVA" value="${estadoActivo}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																	</logic:notEqual>
																</logic:notEqual>
															</td>
														</tr>
													</table>
												</td>		
												<td align="right" valign="top" class="${estilo_detalle}" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
													<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<tr>
															<td>&nbsp;&nbsp;</td>
															<td align="right" valign="top" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>&nbsp;
															</td>
															<td align="right" valign="top" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">I</logic:equal>
																<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">&nbsp;</logic:notEqual>
															</td>
															<td>&nbsp;</td>
														</tr>
													</table>
												</td>
												<%--<td align="right" valign="top">
													<bean:write name="vistaDetallePedidoDTO" property="valorUnitarioPOS" formatKey="formatos.numeros"/>
												</td>--%>
												<td align="right" valign="top" class="${estilo_detalle_final}" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
													<table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<tr>
															<td>&nbsp;&nbsp;</td>
															<td align="right" valign="top" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
															</td>
															<td>&nbsp;</td>
														</tr>
													</table>
												</td>
											</tr>
											<%-- secci&oacute;n que muestra las entregas --%>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
												<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
													<tr>
														<td colspan="2"></td>
														<td align="right" colspan="4">
															<%-- se muestra la colecci&oacute;n de entregas del articulo --%>
															<table border="0" cellspacing="0" cellpadding="0" width="100%" align="left" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
																<tr>
																	<td align="center" valign="top" style="background-color:#DDEEFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana;font-weight: bolder;border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Cant. ent.</td>
																	<td align="center" valign="top" style="background-color:#DDEEFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana;font-weight: bolder;border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Fecha entrega</td>
																	<td align="left" valign="top" style="background-color:#DDEEFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana;font-weight: bolder;border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Lugar entrega</td>
																</tr>
																<logic:iterate name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
																<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO">
																	<bean:define id="entregaPedidoDTO" property="entregaPedidoDTO" name="entregaDetallePedidoDTO"></bean:define>
																</logic:notEmpty>
																	<tr>
																		<td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/></td>
																		<td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">&nbsp;<bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>&nbsp;</td>
																		<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																		   <logic:notEmpty name="entregaPedidoDTO" property="divisionGeoPoliticaDTO">
																		  	 <bean:write name="entregaPedidoDTO" property="divisionGeoPoliticaDTO.nombreDivGeoPol"/>-
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
																</logic:iterate>
															</table>
														</td>
														<td colspan="2"></td>
													</tr>
													<tr><td height="5px" colspan=8"></td></tr>
												</logic:notEmpty>
											</logic:notEmpty>
										</logic:iterate>
										<tr>
											<td height="5px" colspan="6"></td>
											<td colspan="2" class="textoNegro10" align="right">TOTAL A PAGAR:&nbsp; </td>
												<td align="right" class="textoNegro10 fila_detalle_final">
													<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/>
												</td>
										
										</tr>
										<tr><td colspan="8" class="textoNegro10">(-) El descuento se aplica sobre el precio del art&iacute;culo sin IVA</td></tr>
										<tr><td height="5px"></td></tr>
										<%--tr><td colspan="7" align="right"><bean:write name="totalPrevio" formatKey="formatos.numeros"/></td></tr--%>
										<tr>
											<td align="left" colspan="3">
												<c:set var="totalDescuento" value="0"/>
												<%-- se muestra el detalle de los descuentos --%>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
														<%--c:set var="valorTotalAplica" value="0"/--%>
														<table border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td align="left" class="textoNegro11">Detalle de los descuentos sin IVA</td>
															</tr>
															<tr><td height="6px"></td></tr>
															<tr>
																<td align="left">
																	<table border="0" cellspacing="0" width="80%" cellpadding="1" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
																		<tr align="left" style="background-color:#FF0F0F;color: #FFFFFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
																			<td align="center" width="40%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">DESCRIPCI&Oacute;N&nbsp;</td>
<!-- 																			<td align="center" width="15%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">V.TOTAL&nbsp;</td> -->
																			<td align="center" width="15%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">PORCENTAJE&nbsp;</td>
																			<td align="center" width="15%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">DESCUENTO&nbsp;</td>
																		</tr>
																		<tr><td colspan="3" height="5px" class="borde_inferior"></td></tr>
																		<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">
																			<tr>
																				<td align="left" width="40%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																				<bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/>&nbsp;</td>
<%-- 																				<td align="right" width="15%"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/>&nbsp;</td> --%>
																				<td align="right" width="15%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																					<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																					</logic:greaterThan>
																					<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>&nbsp;
																				</td>
																				<td align="right" width="15%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/>&nbsp;</td>
																				<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
																				<%--c:set var="valorTotalAplica" value="${valorTotalAplica + descuento.valorPrevioDescuento}"/--%>
																			</tr>
																		</logic:iterate>
																		<tr><td height="5px" colspan="3" class="borde_inferior"></td></tr>
																		<tr style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																			<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">T. DESCUENTO:</td>
																			<%--td align="right"><bean:write name="valorTotalAplica" formatKey="formatos.numeros"/>&nbsp;</td--%>
																			<%--td align="right" colspan="2"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%&nbsp;</td--%>
																			<td colspan="3" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/>&nbsp;</td>
																		</tr>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																			<tr><td height="5px" colspan="3" class=""></td></tr>
																			<tr><td colspan="4" align="center" class="fila_cabecera fila_detalle_final">NRO DE BONOS A RECIBIR:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/></td></tr>
																		</logic:notEmpty>
																	</table>
																</td>
															</tr>
														</table>
													</logic:notEmpty>
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
														<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento">
															<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
														</logic:iterate>
													</logic:notEmpty>
												</logic:empty>
											</td>
											<td align="right" colspan="6">
												<table border="0" align="right" cellspacing="0" cellpadding="0">
													<tr height="14">
													<td class="textoAzul10" colspan="2" align="center">RESUMEN TRIBUTARIO</td>
													<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<td class="textoAzul10" align="right">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
													<td class="textoNegro10" align="right">
														<bean:define id="valorTotalBrutoSinIva" name="ec.com.smx.sic.sispe.vistaPedido" property="valorTotalBrutoSinIva"/>
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorTotalBrutoSinIva}"/>
													</td>
												</tr>
												<tr>
													<td align="right" class="textoAzul10">(-)DESCUENTO:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="totalDescuentoIva" name="ec.com.smx.sic.sispe.vistaPedido" property="totalDescuentoIva"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuentoIva}"/>
														</td>
												</tr>
												<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<tr>
														<td align="right" class="textoAzul10">SUBTOTAL NETO:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="subTotalNetoBruto" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNetoBruto"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNetoBruto}"/>
														</td>
													</tr>										
													<tr>
														<td align="right" class="textoAzul10">TARIFA 0%:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="subTotalNoAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNoAplicaIVA"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNoAplicaIVA}"/>
														</td>
													</tr>
													<tr>
														<td align="right" class="textoAzul10">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="subTotalAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalAplicaIVA"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalAplicaIVA}"/>
														</td>
													</tr>
													<tr>
														<td align="right" class="textoAzul10"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="ivaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="ivaPedido"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ivaPedido}"/>
														</td>
													</tr>
													<tr><td colspan="2" align="right" height="5px"></td></tr>
													<tr>
														<td align="right" class="textoAzul10">COSTO FLETE:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="valorCostoEntregaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="valorCostoEntregaPedido"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCostoEntregaPedido}"/>
														</td>
													</tr>
													<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
													<tr>
														<td align="right" class="textoAzul10">TOTAL:&nbsp;</td>
														<td align="right" class="textoNegro10">
															<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/>
														</td>
													</tr>
												</table>
											</td>
											<td>&nbsp;</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td height="20px"></td></tr>
							<%-- control de la impresi&oacute;n de canastos --%>
							<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionCanastos">
							<logic:equal name="existenCanastos" value="${estadoActivo}">
									<tr><td height="20px"></td></tr>
									<tr><td><hr/></td></tr>
									<tr class="textoNegro10"><td align="left">Detalle de los canastos</td></tr>
									<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">
										<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
										<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial|| vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
											<tr>
												<td>
													<table cellpadding="1" cellspacing="0" align="left" class="textoNegro9">
														<tr><td height="10px" colspan="2"></td></tr>
														<tr>
															<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">Descripci&oacute;n:&nbsp;</td>
															<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
														</tr>
														<tr>
															<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">Precio:&nbsp;</td>
																<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
															</logic:notEqual>												
															<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">Precio tot. sin IVA.:&nbsp;</td>
																<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioEstado"/></td>
																<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">Precio total:&nbsp;</td>
																<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
															</logic:equal>
															<td align="right" width="60px">&nbsp;</td>
															<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">Cantidad:&nbsp;</td>
															<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/></td>
														</tr>
													</table>
												</td>
											</tr>
											<tr><td height="10px"></td></tr>
											<logic:notEmpty name="vistaDetallePedidoDTO" property="articuloDTO">
												<tr>
													<td>
														<table cellpadding="1" cellspacing="0" width="100%" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
															<tr style="background-color:#FF0F0F;color: #FFFFFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
																<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">No</td>
																<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">C&Oacute;DIGO BARRAS</td>
																<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">ART&Iacute;CULO</td>
																<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">CANT.</td>
																</logic:notEqual>
																<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">													
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">CANT.</td>
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">V.UNIT.</td>
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">V.UNIT.IVA</td>
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">T.NETO.INC.IVA</td>														
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">IVA</td>
																</logic:equal>
															</tr>
															<tr><td colspan="7" height="5px"></td></tr>
															<logic:iterate name="vistaDetallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
																<tr>
																	<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
																	<logic:equal name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
																		<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
																	</logic:equal>
																	<bean:define id="fila" value="${registroReceta + 1}"/>
																	<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="fila"/></td>
																	<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
																	<td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																	<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>
																	<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																		<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitario}"/>
																		</td>
																		<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitarioIVA}"/>
																		</td>
																		<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																			<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																				<c:set var="totalValorIva" value="${recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento}"/>
																				<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																			</c:if>
																			<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																				<c:set var="totalValorIva" value="${(recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento)  * 1.12}"/>
																				<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																			</c:if>
																		</td>
																		<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																			<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">&nbsp;</c:if>
																			<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">I</c:if>
																		</td>	
																	</logic:equal>
																</tr>
															</logic:iterate>
															<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
															<tr>
																<td colspan="3" />
																<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">TOTAL:</td >
																<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">		
																	<c:if test="${afiliado != null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																	</c:if>
																	<c:if test="${afiliado == null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																	</c:if>
																</td>
																<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																	<c:if test="${afiliado != null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																	</c:if>
																	<c:if test="${afiliado == null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																	</c:if>
																</td>
																<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
																</td>
																<td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">&nbsp;</td> 
															</tr>
															</logic:equal>
														</table>
													</td>
												</tr>
											</logic:notEmpty>
											<tr><td><hr/></td></tr>
										</c:if>
									</logic:iterate>
								</logic:equal>
							</logic:notEmpty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDiferidos">
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesDiferidos">
									<tr><td height="20px"></td></tr>
									<tr><td align="left" class="textoNegro10">Detalle de art&iacute;culos que aplican diferidos (Sin intereses)</td></tr>
									<tr><td><hr/></td></tr>
									<tr>
										<td>
											<c:set var="totalDiferidos" value="${0}"/>
											<c:set var="valorCero" value="0.00"/>
											<table width="100%" border="0" cellpadding="1" cellspacing="0" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
												<tr style="background-color:#FF0F0F;color: #FFFFFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
													<td align="center" width="10px" valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">No</td>
													<td align="center" width="55px" valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">C&oacute;digo barras</td>
													<td align="center" width="150px" valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Art&iacute;culo</td>													
													<td align="center" width="35px"valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Precio uni.no/afi</td>
													<td align="center" width="35px"valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Precio uni.afi</td>
													<td align="center" width="25px"valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Cant.&nbsp;</td>
													<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO" indexId="indiceCuota">
														<td align="center" width="5px" valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;"><bean:write name="cuotaDTO" property="numeroCuotas"/>MESES&nbsp;</td>
													</logic:iterate>
													<td align="center" width="25px"valign="top" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">TOTAL</td>
												</tr>
												<tr><td colspan="9" height="5px"></td></tr>
												<c:set var="subTotal" value="${0}"/>
												<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesDiferidos" id="extructuraDiferidos" indexId="indiceDif">
													<bean:define id="fila" value="${indiceDif + 1}"/>
													<tr  valign="middle">
														<td width="10px" align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="fila"/></td>
														<td width="55px" align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
														<td width="150px" align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;(
														<logic:empty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
															&nbsp;,
														</logic:empty>
														<logic:notEmpty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
															<bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
														</logic:notEmpty>
														)</td>
														<td width="35px" align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseNoAfiImp}"/></td>
														<td width="35px" align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/></td>
														<td width="25px" align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.cantidadEstado"/></td>
														<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
															<c:if test="${duplex.secondObject != valorCero}">
																<td align="center" width="15px" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
															</c:if>
															<c:if test="${duplex.secondObject == valorCero}">
																<td align="center" width="15px" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">-</td>
															</c:if>
														</logic:iterate>
														<c:set var="subTotal" value="${extructuraDiferidos.vistaDetallePedidoDTO.cantidadEstado * extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/>
														<c:set var="totalDiferidos" value="${totalDiferidos + subTotal}"/>
														<td width="25px" align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="subTotal"/></td>
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
													<td  align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">TOTAL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
													<td  align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDiferidos}"/>
													</td>
												</tr>
												<tr><td colspan="2" align="right">-----------------------</td></tr>
												<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
													<tr>
													 <td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><bean:write name="cuotaDTO1" property="numeroCuotas"/>&nbsp;MESES:&nbsp;&nbsp;&nbsp;&nbsp;</td>
													 <c:if test="${totalDiferidos >= cuotaDTO1.valorMinimo}">
														<c:set var="totalMonto" value="${(totalDiferidos/cuotaDTO1.numeroCuotas)*cuotaDTO1.valorInteres}"/>
														<td  align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalMonto}"/></td>
													 </c:if>
													 <c:if test="${totalDiferidos < cuotaDTO1.valorMinimo}">
														<td  align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;font-family: Verdana, Arial, Helvetica;font-size: 9px;">-</td>
													 </c:if>
													</tr>
												</logic:iterate>
											</table>
										</td>
									</tr>
									<tr><td><hr/></td></tr>
								</logic:notEmpty>
							</logic:notEmpty>
						</logic:notEmpty>
			<tr><td height="20px">&nbsp;</td></tr>
			<tr><td align="left">
			<table class="textoNegro10">
					<tr>
						<td>
							<b>NOTA:</b><br/>
						</td>
					</tr>
				</table>
			</td></tr>
			<tr><td align="left">
			<table style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
					<tr>
						<td>
							Los art&iacute;culos que estan marcados con un (*) de color rojo, tienen insuficiencia en su stock.<br/>
						</td>
					</tr>
				</table>
			
			</td></tr>
			<tr><td height="20px"></td></tr>
			<tr><td align="left" >
				<table class="textoNegro10">
					<tr>
						<td>
							<b>OBSERVACIONES:</b><br/>
						</td>
					</tr>
				</table>
			</td></tr>
			<tr>
				<td>
					<table cellpadding="1" cellspacing="0" style="font-family: Verdana,Arial,Helvetica;font-size: 9px;">
						<tiles:insert page="/reportes/observacionesPedido.jsp">
							<tiles:put name="codEst" value="${codigoEstablecimiento}"/>
							<tiles:put name="codEstAki" value="${codigoEstablecimientoAki}"/>
							<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
								<bean:define id="afiliado" name="ec.com.smx.sic.sispe.pedido.preciosAfiliado"></bean:define>
								<tiles:put name="estadoAfilacion" value="${afiliado}"/>
							</logic:notEmpty>
						</tiles:insert>
					</table>
				</td>
			</tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
				<tr><td height="20px">&nbsp;</td></tr>
				<tr>
					<td>
						<table cellpadding="1" cellspacing="0" width="100%">
							<tiles:insert page="/reportes/comprobanteBonoMail.jsp">
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
</html>