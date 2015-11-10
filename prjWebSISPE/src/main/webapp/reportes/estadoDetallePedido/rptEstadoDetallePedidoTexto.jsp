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
<div id="rptEstadoPedidoTxt">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta http-equiv="Content-Style-Type" content="text/css">
		<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
		<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
		<meta HTTP-EQUIV="max-age" CONTENT="0">
		<meta HTTP-EQUIV="Expires" CONTENT="0">
		<%--link href="../../css/textos.css" rel="stylesheet" type="text/css"--%>
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
		<!-- Estado precio afiliado -->
		<bean:define id="afiliado" name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado"/>
		
		<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
		<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
		
		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
			<tr>
				<td valign="top">
					<table border="0" cellspacing="0" cellpadding="1" width="100%">
						<!-- OANDINO: Presentación de imagen de código de barras -->
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
							<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"></bean:define>
							<tr>
								<td align="left"></td>
								<td colspan="2" align="right">
									<img src="${pageContext.request.contextPath}/CodigoBarras?data=${vistaPedidoDTO.llaveContratoPOS}&height=50&width=1"/>
								</td>
							</tr>
						</logic:notEmpty>
						<tr>
							<td align="left"></td>
							<td align="right">
								No de reservaci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
							</td>
							<td align="left">&nbsp;&nbsp;</td>
						</tr>
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
					<table border="0" width="100%" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="left">Entidad responsable:&nbsp;<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
												<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
											</logic:equal>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
												<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
											</logic:equal>
										</td>
									</tr>
									<tr><td height="5px"></td></tr>
									<tr>
										<td align="left">
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td align="left" width="50%">
														No. de pedido:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
													</td>
													<td align="left" width="50%">
														Estado:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="10px"></td></tr>
						<tr>
							<td>
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr><td height="5px" colspan="2"></td></tr>
									<tr>
										<td align="left" width="50%">
											Fecha de elaboraci&oacute;n:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fecha"/>
										</td>
										<td align="left" width="50%">
											Elaborado en:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
										</td>
									</tr>
									<tr><td height="5px" colspan="2"></td></tr>
									<tr>
										<td align="left" width="50%">
											Primera fecha de despacho:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/>
										</td>
										<td align="left" width="50%">
											Primera fecha de entrega:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fechahora"/>
										</td>
									</tr>
									<tr><td height="5px" colspan="2"></td></tr>
									<tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
											<td align="left" width="50%">
												Tel&eacute;fono local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.telefonoLocal"/>
											</td>
											<td align="left" width="50%">
												Administrador local:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.nombreAdministrador"/>
											</td>
										</logic:notEmpty>
										<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
											<td align="left" width="50%">
												Tel&eacute;fono local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
											</td>
											<td align="left" width="50%">
												Administrador local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
											</td>
										</logic:empty>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="5px"></td></tr>
					</table>
				</td>
			</tr>
			<tr><td width="100%"><hr/></td></tr>
			<tr>
				<td align="center" valign="top">
					<table border="0" width="100%" align="center" cellpadding="0" cellspacing="0">						
						<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
					<tiles:insert page="/contacto/cabeceraContactoVistaPedido.jsp"/>	
						<%--<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
						<c:choose>
							<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
								<tr>
									<td colspan="2">
										<table cellpadding="0" cellspacing="0" align="left" width="100%">
											<tr><td align="right" colspan="2" width="5px"></td></tr>
											<tr>
												<td align="left" width="15%">
													Valor Abonado:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="abonoPedido" formatKey="formatos.numeros"/>
												</td>
												<td align="left">
													Saldo:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="saldoAbonoPedido" formatKey="formatos.numeros"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:when>
						</c:choose>--%>
					</table>
				</td>
			</tr>
			<tr><td width="100%"><hr/></td></tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
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
								<%--td align="right" valign="top" class="fila_cabecera">Iva</td--%>
								<%--td align="right" valign="top" class="fila_cabecera">Dscto.</td--%>
								<%--<td align="right" valign="top">V.Unit Neto</td>--%>
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
								<tr>
									<td align="left" width="20px" valign="top" class="${estilo_detalle}"><bean:write name="numRegistro"/></td>
									<td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
									<c:if test="${vistaDetallePedidoDTO.cantidadReservarSIC > vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
									   <td align="left" valign="top" class="${estilo_detalle}">*<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
									</c:if>
									<c:if test="${vistaDetallePedidoDTO.articuloDTO.npStockArticulo == null || vistaDetallePedidoDTO.cantidadReservarSIC <= vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
									   <td align="left" valign="top" class="${estilo_detalle}"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
									</c:if>
									<%-- <td align="left" valign="top"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoInternoProveedor"/>,&nbsp;${unidadManejo}</td> --%>
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
												<td align="left" >
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
													<b>T</b>
													</logic:equal>
												</td>															
												<td align="right" >
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
									<td align="right" class="${estilo_detalle}" valign="top">
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr>		
												<td align="left" >
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
														<b>T</b>
													</logic:equal>
												</td>																														
												<td align="right" >
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
									<%--<td align="right" valign="top">
										<bean:write name="vistaDetallePedidoDTO" property="valorUnitarioPOS" formatKey="formatos.numeros"/>
									</td>--%>
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
								<%-- secci&oacute;n que muestra las entregas --%>
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
									<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
										<tr>
											<td colspan="2"></td>
											<td align="right" colspan="4">
												<%-- se muestra la colecci&oacute;n de entregas del articulo --%>
												<table border="0" cellspacing="0" cellpadding="0" width="100%" align="left">
													<tr>
														<td align="center" valign="top" class="fila_detalle">Cant. ent.</td>
														<td align="center" valign="top" class="fila_detalle">Fecha entrega</td>
														<td align="left" valign="top" class="fila_detalle_final">Lugar entrega</td>
													</tr>
													<logic:iterate name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
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
																			   <td title="Bodega de tránsito">-(Tráns)</td>
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
										<tr><td height="5px" colspan=8"></td></tr>
									</logic:notEmpty>
								</logic:notEmpty>
							</logic:iterate>
							<tr>
								<td height="5px" colspan="6"></td>
								<td colspan="2" class="textoNegro10" align="right">TOTAL A PAGAR:&nbsp; </td>
									<td align="right" class="textoNegro10 fila_detalle_final">
									<bean:define id="totalVenta" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>									
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalVenta}"/>									
									</td>
							
							</tr>
							<tr><td colspan="8">(-) El descuento se aplica sobre el precio del art&iacute;culo sin IVA</td></tr>
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
														<table border="0" cellspacing="0" width="80%" cellpadding="1">
															<tr align="left">
																<td align="center" width="40%">DESCRIPCI&Oacute;N&nbsp;</td>
																<td align="center" width="15%">V.TOTAL&nbsp;</td>
																<td align="center" width="15%">PORCENTAJE&nbsp;</td>
																<td align="center" width="15%">DESCUENTO&nbsp;</td>
															</tr>
															<tr><td colspan="4" height="5px" class="borde_inferior"></td></tr>
															<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">
																<tr>
																	<td align="left" width="40%"><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/>&nbsp;</td>
																	<td align="right" width="15%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorPrevioDescuento}"/>&nbsp;</td>
																	<td align="right" width="15%">
																		<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																		</logic:greaterThan>
																		<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>&nbsp;
																	</td>
																	<td align="right" width="15%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/>&nbsp;</td>
																	<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
																	<%--c:set var="valorTotalAplica" value="${valorTotalAplica + descuento.valorPrevioDescuento}"/--%>
																</tr>
															</logic:iterate>
															<tr><td height="5px" colspan="4" class="borde_inferior"></td></tr>
															<tr>
																<td align="right">T. DESCUENTO:</td>
																<%--td align="right"><bean:write name="valorTotalAplica" formatKey="formatos.numeros"/>&nbsp;</td--%>
																<%--td align="right" colspan="2"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%&nbsp;</td--%>
																<td colspan="3" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/>&nbsp;</td>
															</tr>
															<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																<tr><td height="5px" colspan="4" class=""></td></tr>
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
											<td align="right">SUBTOTAL NETO:&nbsp;</td>
											<td align="right">
												<bean:define id="subTotalNetoBruto" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNetoBruto"/>
												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNetoBruto}"/>
											</td>
										</tr>										
										<tr>
											<td align="right">TARIFA 0%:&nbsp;</td>
											<td align="right">
												<bean:define id="subTotalNoAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNoAplicaIVA"/>
												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNoAplicaIVA}"/>
											</td>
										</tr>
										<tr>
											<td align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
											<td align="right">
												<bean:define id="subTotalAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalAplicaIVA"/>
												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalAplicaIVA}"/>
											</td>
										</tr>
										<tr>
											<td align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
											<td align="right">
												<bean:define id="ivaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="ivaPedido"/>
												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ivaPedido}"/>
											</td>
										</tr>
										<tr><td colspan="2" align="right" height="5px"></td></tr>
										<tr>
											<td align="right">COSTO FLETE:&nbsp;</td>
											<td align="right">
												<bean:define id="valorCostoEntregaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="valorCostoEntregaPedido"/>
												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCostoEntregaPedido}"/>
											</td>
										</tr>
										<tr><td colspan="2" align="right" height="5px" class="borde_inferior"></td></tr>
										<tr>
											<td align="right">TOTAL:&nbsp;</td>
											<td align="right">
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
						<tr><td align="left">Detalle de los canastos</td></tr>
						<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">
							<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
							<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial|| vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="0" align="left">
											<tr><td height="10px" colspan="2"></td></tr>
											<tr>
												<td align="right">Descripci&oacute;n:&nbsp;</td>
												<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
											</tr>
											<tr>
												<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
													<td align="right">Precio:&nbsp;</td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
												</logic:notEqual>												
												<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
													<td align="right">Precio tot. sin IVA.:&nbsp;</td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioEstado"/></td>
													<td align="right">Precio total:&nbsp;</td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
												</logic:equal>
												<td align="right" width="60px">&nbsp;</td>
												<td align="right">Cantidad:&nbsp;</td>
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
													<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
														<td align="right" class="fila_cabecera_final">CANT.</td>
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">													
														<td align="right" class="fila_cabecera">CANT.</td>
														<td align="right" class="fila_cabecera">V.UNIT.</td>
														<td align="right" class="fila_cabecera">V.UNIT.IVA</td>
														<td align="right" class="fila_cabecera">T.NETO.INC.IVA</td>														
														<td align="right" class="fila_cabecera_final">IVA</td>
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
														<td align="left"><bean:write name="fila"/></td>
														<td align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
														<td align="left"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
														<td align="right"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>
														<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
															<td align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitario}"/>
															</td>
															<td align="right">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaArticuloDTO.precioUnitarioIVA}"/>
															</td>
															<td align="right">
																<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
																<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">
																	<c:set var="totalValorIva" value="${(recetaArticuloDTO.valorTotalEstado - recetaArticuloDTO.valorTotalEstadoDescuento)  * 1.12}"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalValorIva}"/>
																</c:if>
															</td>
															<td align="right">
																<c:if test="${!recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">&nbsp;</c:if>
																<c:if test="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}">I</c:if>
															</td>	
														</logic:equal>
													</tr>
												</logic:iterate>
												<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
												<tr>
													<td colspan="3" />
													<td align="right">TOTAL:</td >
													<td align="right">		
														<c:if test="${afiliado != null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
														</c:if>
														<c:if test="${afiliado == null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</c:if>
													</td>
													<td align="right" >
														<c:if test="${afiliado != null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
														</c:if>
														<c:if test="${afiliado == null}">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
														</c:if>
													</td>
													<td align="right" >
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
													</td>
													<td align="right">&nbsp;</td> 
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
										<td align="center" width="35px"valign="top">Precio uni.no/afi</td>
										<td align="center" width="35px"valign="top">Precio uni.afi</td>
										<td align="center" width="25px"valign="top">Cant.&nbsp;</td>
										<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO" indexId="indiceCuota">
											<td align="center" width="5px" valign="top"><bean:write name="cuotaDTO" property="numeroCuotas"/>MESES&nbsp;</td>
										</logic:iterate>
										<td align="center" width="25px"valign="top">TOTAL</td>
									</tr>
									<tr><td colspan="9" height="5px"></td></tr>
									<c:set var="subTotal" value="${0}"/>
									<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesDiferidos" id="extructuraDiferidos" indexId="indiceDif">
										<bean:define id="fila" value="${indiceDif + 1}"/>
										<tr  valign="middle">
											<td width="10px" align="left"><bean:write name="fila"/></td>
											<td width="55px" align="left"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
											<td width="150px" align="left"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;(
											<logic:empty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												&nbsp;,
											</logic:empty>
											<logic:notEmpty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												<bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
											</logic:notEmpty>
											)</td>
											<td width="35px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseNoAfiImp}"/></td>
											<td width="35px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/></td>
											<td width="25px" align="center"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.cantidadEstado"/></td>
											<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
												<c:if test="${duplex.secondObject != valorCero}">
													<td align="center" width="15px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
												</c:if>
												<c:if test="${duplex.secondObject == valorCero}">
													<td align="center" width="15px">-</td>
												</c:if>
											</logic:iterate>
											<c:set var="subTotal" value="${extructuraDiferidos.vistaDetallePedidoDTO.cantidadEstado * extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/>
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
										<td  align="right">TOTAL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										<td  align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDiferidos}"/>
										</td>
									</tr>
									<tr><td colspan="2" align="right">-----------------------</td></tr>
									<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
										<tr>
										 <td align="right"><bean:write name="cuotaDTO1" property="numeroCuotas"/>&nbsp;MESES:&nbsp;&nbsp;&nbsp;&nbsp;</td>
										 <c:if test="${totalDiferidos >= cuotaDTO1.valorMinimo}">
											<c:set var="totalMonto" value="${(totalDiferidos/cuotaDTO1.numeroCuotas)*cuotaDTO1.valorInteres}"/>
											<td  align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalMonto}"/></td>
										 </c:if>
										 <c:if test="${totalDiferidos < cuotaDTO1.valorMinimo}">
											<td  align="right">-</td>
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
			
			<tr><td height="20px"></td></tr>
			<logic:notEqual name="sispe.vistaEntidadResponsableDTO" property="codigoLocal" value="99">
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
				<tr><td align="left">OBSERVACIONES:<br/></td></tr>
				<tr><td height="5px"></td></tr>
				<tr>
					<td>
						<table cellpadding="1" cellspacing="0">
							<tiles:insert page="/reportes/observacionesPedido.jsp">
								<tiles:put name="codEst"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstablecimiento"/></tiles:put>
								<tiles:put name="codEstAki" value="${codigoEstablecimientoAki}"/>
								<tiles:put name="estadoAfilacion" value="${afiliado}"/>
							</tiles:insert>
						</table>
					</td>
				</tr>
				<tr><td height="30px">&nbsp;</td></tr>
				<tr><td align="left">Firma y sello: _________________________________						Elaborado por: <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npNombreUsuario"/></td></tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
					<tr class="saltoDePagina"><td>&nbsp;</td></tr>
					<tr>
						<td>
							
								<table cellpadding="1" cellspacing="0" width="100%">
									<tiles:insert page="/reportes/comprobanteBonoEstado.jsp">
										<tiles:put name="nBonos" value="${numeroBonos}"/>
										<tiles:put name="mCompra" value="${montoMinimoCompra}"/>
										<tiles:put name="mBono" value="${valorBono}"/>
										<tiles:put name="vCalculado" value="${valorCalculado}"/>
									</tiles:insert>
								</table>
							
						</td>
					</tr>
				</logic:notEmpty>
			</logic:notEqual>
		</table>
	</body>
</div>
</html>