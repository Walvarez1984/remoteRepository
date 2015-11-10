<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto">
<c:set var="cedulaCto"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto" /></c:set>
</logic:notEmpty>
<bean:define id="preciosAfiliado" name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado"></bean:define>
<kreport:pdfout>
	<html page-orientation="H">
		<head>
			<meta http-equiv="Content-Style-Type" content="text/css"/>
			<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="max-age" CONTENT="0"/>
			<meta HTTP-EQUIV="Expires" CONTENT="0"/>
			<style type="text/css">
			  .textoRojoC13{
		 	   font-family: Verdana, Arial, Helvetica;
			   font-size: 10px;
			   color: #E30207;
			  }
			  .saltoDePagina{
				PAGE-BREAK-AFTER: always;
			  }
    		</style>
		</head>
		<body>
			<bean:define id="estadoActivo" name="sispe.estado.activo"/>
			<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
			<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoCotizado">
				<bean:define name="ec.com.smx.sic.sispe.pedido.estadoCotizado" id="estadoCotizado"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoRecotizado">
				<bean:define name="ec.com.smx.sic.sispe.pedido.estadoRecotizado" id="estadoRecotizado"/>
			</logic:notEmpty>
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
			
			<bean:define id="clasificacionCanastoEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
			<bean:define id="clasificacionCanastosCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
				<bean:define id="numeroBonos" name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.monto.minimoCompra">
				<bean:define id="montoCompraMinima" name="ec.com.smx.sic.sispe.pedido.monto.minimoCompra"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.valor.bono.maxinavidad">
				<bean:define id="valorBono" name="ec.com.smx.sic.sispe.pedido.valor.bono.maxinavidad"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.valor.calculado.maxinavidad">
				<bean:define id="valorCalculado" name="ec.com.smx.sic.sispe.pedido.valor.calculado.maxinavidad"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo">
				<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo" id="tipoDescuentoMaxiNavidad"/>
			</logic:notEmpty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial">
				<bean:define name="ec.com.smx.sic.sispe.reporte.tipoPedidoEmpresarial" id="tipoPedidoEmpresarial"/>
			</logic:notEmpty>
			<bean:define id="codigoEstablecimientoAki"><bean:message key="ec.com.smx.sic.sispe.codigoTipoEstablecimiento.aki"/></bean:define>
			<c:set var="existenCanastos" value=""/>
			<%-- se definen los c&oacute;digos de las posibles entidades responsables --%>
			<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
			<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>

			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td valign="top" font-size="7pt" >
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<tr>
								<td align="left">
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&#32;-&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
								</td>
								<td align="right">Ruc:&nbsp;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
							</tr>
							<tr><td colspan="2" height="10px"></td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td font-size="7pt">
						<table cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="70px" align="left">Entidad responsable:</td>
								<td align="left">
									<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
										<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
									</logic:equal>
									<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
										<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
									</logic:equal>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table cellpadding="1" cellspacing="0" align="left">
										<tr>
											<td align="left" width="150px">
												No. de pedido:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
											</td>
											<td align="left" width="150px">
												No. de reservaci&oacute;n:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
											</td>
											<td align="left" width="150px">
												Estado:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="3" cellspacing="0" width="100%">
								<tr>
								  <!-- PARA EL CASO DE DATOS DE EMPRESA -->   
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
									<td valign="top" width="300" align="left" font-size="7pt" >
										<table border="0" width="100%" cellspacing="0">
											<tr>
												<td width="60">Datos de la empresa:</td>
												<td width="240">&nbsp;</td>
											</tr>
											<tr>
												<td>RUC:</td>
												<td>
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="rucEmpresa" />
												</td>
											</tr>
											<tr>
												<td>Raz&oacute;n social:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa" /></td>
											</tr>
											<tr>
												<td>Tel&eacute;fono:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoEmpresa" /></td>
											</tr>
										</table>
									</td>
								</logic:notEmpty>
								
								  <!-- PARA EL CASO DE DATOS DE PERSONA -->   
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
									<td valign="top" width="300" align="left" font-size="7pt" >
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<tr>
												<td width="60" >Datos del cliente:</td>
												<td width="240"> &nbsp;</td>
											</tr>
											<tr>
												<td >Documento:</td>
												<td>
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numeroDocumentoPersona" />
												</td>
											</tr>
											<tr>
												<td>Nombre:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona" /></td>
											</tr>
											<tr>
												<td>Tel&eacute;fono:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoPersona" /></td>
											</tr>
											<tr>
												<td>Email:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="emailPersona" /></td>
											</tr>
										</table>
									</td>
								</logic:notEmpty>
								
								<c:if test="${cedulaCto == 'SN'}">
									<td valign="top" width="300" align="left" font-size="7pt" >
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<tr>
												<td width="60">Datos del contacto:</td>
												<td width="240">&nbsp;</td>
											</tr>
											<tr>
												<td height="5px">SIN CONTACTO</td>
											</tr>
										</table>
									</td>
								</c:if>
								<c:if test="${cedulaCto != 'SN'}">
									<td valign="top" width="300" align="left" font-size="7pt" >
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<tr>
												<td width="60">Datos del contacto:</td>
												<td width="240">&nbsp;</td>
											</tr>
											<tr>
												<td>Documento:</td>
												<td>
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto" />
												</td>
											</tr>
											<tr>
												<td>Nombre:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto" /></td>
											</tr>
											<tr>
												<td>Tel&eacute;fono:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoContacto" /></td>
											</tr>
											<tr>
												<td>Email:</td>
												<td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="emailContacto" /></td>
											</tr>
										</table>
									</td>
								</c:if>					
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td font-size="7pt">
						<table width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td align="left" width="300px">
									Fecha de elaboraci&oacute;n:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fecha"/>
								</td>
								<td align="left">
									Elaborado en:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
								</td>
							</tr>
							<tr>
								<td align="left">
									Primera fecha de despacho:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/>
								</td>
								<td align="left">
									Primera fecha de entrega:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fechahora"/>
								</td>
							</tr>
							<tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
									<td align="left" width="20%">
										Tel&eacute;fono local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.telefonoLocal"/>
									</td>
									<td align="left" width="40%">
										Administrador local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.nombreAdministrador"/>
									</td>
								</logic:notEmpty>
								<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
									<td align="left" width="20%">
										Tel&eacute;fono local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
									</td>
									<td align="left" width="40%">
										Administrador local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
									</td>
								</logic:empty>
							</tr>
						</table>
					</td>
				</tr>
				<%--<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
				<c:choose>
					<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
						<tr>
							<td font-size="7pt">
								<table cellpadding="0" cellspacing="0" align="left">
									<tr><td align="right" colspan="2">.&#32;</td></tr>
									<tr>
										<td width="50px">
											Valor Abonado:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="abonoPedido" formatKey="formatos.numeros"/>&#32;&#32;&#32;&#32;&#32;&#32;&#32;
										</td>
										<td>
											Saldo:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="saldoAbonoPedido" formatKey="formatos.numeros"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:when>
				</c:choose>--%>
				<tr>
					<td>
						<table width="98%" border="0" cellpadding="0" cellspacing="0">
							<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
								<tr><td><hr/></td></tr>
								<tr><td font-size="7pt">Detalle de los art&iacute;culos</td></tr>
								<tr><td><hr/></td></tr>
								<tr>
									<td font-size="7pt">
										<c:set var="totalPrevio" value="0"/>
										<table width="100%" border="0" cellpadding="1" cellspacing="0">
											<tr>
												<td align="left" width="20px" valign="top">No</td>
												<td align="left">C&oacute;digo barras</td>
												<td align="left" width="150px">Art&iacute;culo</td>
												<td align="right" width="40px">Cant.</td>
												<td align="right">Peso Kg.</td>
												<td align="right">V.unit</td>
												<td align="right">V.unit.IVA</td>
												<td align="right">Tot. IVA</td>
												<td align="right">IVA</td>
												<td align="right">Dscto.</td>
												<%--<td align="right">V.Unit Neto</td>--%>
												<td align="right">Tot. neto</td>
											</tr>
											<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
												<c:set var="totalPrevio" value="${totalPrevio + vistaDetallePedidoDTO.valorPrevioVenta}"/>
												<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
												<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
													<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejoPrecioCaja}"/>
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
												</logic:equal> --%>
												
												<bean:define id="codigoSubClasificacion" name="vistaDetallePedidoDTO" property="codigoSubClasificacion"/>
												
                                                <c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
												  	<c:set var="existenCanastos" value="${estadoActivo}"/>
												</c:if>
												
												<tr>
													<td align="left" width="20px"><bean:write name="numRegistro"/></td>
													<td align="left"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
													<c:if test="${vistaDetallePedidoDTO.cantidadReservarSIC > vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
													   <td align="left" width="150px" style="color:#E30207">*<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
													</c:if>
													<c:if test="${vistaDetallePedidoDTO.cantidadReservarSIC <= vistaDetallePedidoDTO.articuloDTO.npStockArticulo}">
													   <td align="left" width="150px"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
													</c:if>
													<%-- <td align="left" width="150px"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoInternoProveedor"/>,&nbsp;${unidadManejo}</td> --%>
													<td align="right" width="40px">
														<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">&nbsp;</logic:equal>
														<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
														</logic:notEqual>
													</td>
													<td align="right">
														<logic:equal name="pesoVariable" value="${estadoActivo}">
															<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoLocal}"/>
															</logic:notEmpty>
															<logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
															</logic:empty>
														</logic:equal>
														<logic:notEqual name="pesoVariable" value="${estadoActivo}">&nbsp;</logic:notEqual>
													</td>
													<td align="right" class="${estilo_detalle}" valign="top">
														<table cellpadding="0" cellspacing="0" width="100%">
															<tr>															
																<td align="left" >
																	<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																		T
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
																		T
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
													<td align="right" valign="top">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>
													</td>
													<td align="right" valign="top">
														<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">I</logic:equal>
														<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">&nbsp;</logic:notEqual>
													</td>
													<td align="right" valign="top">
														<logic:greaterThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
													</td>
													<%--<td align="right" valign="top">
														<bean:write name="vistaDetallePedidoDTO" property="valorUnitarioPOS" formatKey="formatos.numeros"/>
													</td>--%>
													<td align="right" valign="top">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
													</td>
												</tr>
												<%-- secci&oacute;n que muestra las entregas --%>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
													<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
														<tr><td height="5px" colspan="8"></td></tr>
														<tr>
															<td></td>
															<td colspan="8">
																<%-- se muestra la colecci&oacute;n de entregas del articulo --%>
																<table border="0" cellspacing="0" cellpadding="0">
																	<tr>
																		<td align="center" valign="top" width="50px">Cant. despacho</td>
																		<td align="center" valign="top" width="50px">Cant. entrega</td>
																		<td align="center" valign="top" width="60px">Fecha despacho</td>
																		<td align="center" valign="top" width="80px">Fecha entrega</td>
																		<td align="left" valign="top" width="400px">Lugar entrega</td>
																	</tr>
																	<logic:iterate name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
																	<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO">
																		<bean:define id="entregaPedidoDTO" property="entregaPedidoDTO" name="entregaDetallePedidoDTO"></bean:define>
																	</logic:notEmpty>
																		<tr><td colspan="5"><hr/></td></tr>
																		<tr>
																			<td align="center"><bean:write name="entregaDetallePedidoDTO" property="cantidadDespacho"/></td>
																			<td align="center"><bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/></td>
																			<td align="center"><bean:write name="entregaPedidoDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
																			<td align="center"><bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
																			<td align="left">
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
														</tr>
														<tr><td height="5px" colspan="8"></td></tr>
													</logic:notEmpty>
												</logic:notEmpty>
											</logic:iterate>
											<logic:empty name="ec.com.smx.sic.sispe.NoMostrarTotales">
												<tr><td colspan="11" align="right">&#32;.</td></tr>
												<tr><td colspan="8" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPrevio}"/></td></tr>
												<tr>
													<td colspan="11">
														<table border="0" align="right" cellspacing="0" cellpadding="0">
															<tr>
																<td align="right">SUBTOTAL BRUTO SIN IVA:&#32;</td>
																<td align="right" width="50px">
																	<bean:define id="valorTotalBrutoSinIva" name="ec.com.smx.sic.sispe.vistaPedido" property="valorTotalBrutoSinIva"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorTotalBrutoSinIva}"/>
																</td>
															</tr>
															<tr>
																<td align="right">(-)DESCUENTO:&#32;</td>
																<td align="right" width="50px">
																	<bean:define id="totalDescuentoIva" name="ec.com.smx.sic.sispe.vistaPedido" property="totalDescuentoIva"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuentoIva}"/>
																</td>
															</tr>
															<tr>
																<td align="right">SUBTOTAL NETO:&#32;</td>
																<td align="right" width="50px">
																	<bean:define id="subTotalNetoBruto" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNetoBruto"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNetoBruto}"/>
																</td>
															</tr>
															<tr><td colspan="2" align="right">-----------------------------</td></tr>
															<tr>
																<td align="right">TARIFA 0%:&#32;</td>
																<td align="right">
																	<bean:define id="subTotalNoAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNoAplicaIVA"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNoAplicaIVA}"/>
																</td>
															</tr>
															<tr>
																<td align="right">TARIFA&#32;<bean:message key="porcentaje.iva"/>%:&#32;</td>
																<td align="right">
																	<bean:define id="subTotalAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalAplicaIVA"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalAplicaIVA}"/>
																</td>
															</tr>
															<tr>
																<td align="right"><bean:message key="porcentaje.iva"/>%&#32;IVA:&#32;</td>
																<td align="right">
																	<bean:define id="ivaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="ivaPedido"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ivaPedido}"/>
																</td>
															</tr>
															<tr><td colspan="2" height="5px"></td></tr>
															<tr>
																<td align="right">COSTO FLETE:&#32;</td>
																<td align="right">
																	<bean:define id="valorCostoEntregaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="valorCostoEntregaPedido"/>
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCostoEntregaPedido}"/>
																</td>
															</tr>
															<tr><td colspan="2" align="right">-----------------------------</td></tr>
															<tr>
																<td align="right"><b>TOTAL:&#32;</b></td>
																<td align="right">
																	<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
																	<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/></b>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</logic:empty>
										</table>
									</td>
								</tr>
								<%-- se muestra el detalle de los descuentos --%>
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
									<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
										<tr><td><hr/></td></tr>
										<tr><td align="left" font-size="7pt"><b>Detalle de los descuentos</b></td></tr>
										<tr><td><hr/></td></tr>
										<tr>
											<td font-size="7pt">
												<table border="0" cellspacing="0" cellpadding="1">
													<tr>
														<td align="left" width="150px">DESCRIPCI&Oacute;N</td>
														<%--<td align="right" width="100px">V.TOTAL</td>--%>
														<td align="right" width="80px">PORCENTAJE</td>
														<td align="right" width="80px">DESCUENTO</td>
													</tr>
													<c:set var="totalDescuento" value="0"/>
													<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">
														<tr>
															<td align="left"><b><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></b></td>
															<%--<td align="right"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/></td>--%>
															<td align="right">
																<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																</logic:greaterThan>
																<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
															</td>
															<td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
															<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
														</tr>
													</logic:iterate>
													<tr>
														<td align="right" colspan="2"><b>TOTAL:</b></td>
														<%-- <td align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%</b></td> --%>
														<td align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
													</tr>
													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp">
																<tr><td><hr/></td></tr>
																<tr><td height="5px" ></td></tr>
																<tr><td  align="center">NRO DE BONOS A RECIBIR:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="numBonNavEmp"/></td></tr>
																<tr><td><hr/></td></tr>
													</logic:notEmpty>
												</table>
											</td>
										</tr>
									</logic:notEmpty>
								</logic:notEmpty>
								
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionCanastos">
									<logic:equal name="existenCanastos" value="${estadoActivo}">
										<tr><td height="20px" align="right">.&#32;</td></tr>
										<tr><td><hr/></td></tr>
										<tr><td font-size="7pt">Detalle de los canastos</td></tr>
										<tr><td><hr/></td></tr>
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO">
											<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
											<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == clasificacionCanastoEspecial|| vistaDetallePedidoDTO.codigoClasificacion == clasificacionCanastosCatalogo}">
												<tr>
													<td font-size="7pt">
														<table cellpadding="1" cellspacing="0" width="100%">
															<tr>
																<td align="right" width="75px">Descripci&oacute;n:&#32;</td>
																<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
															</tr>
															<logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">																
																<tr>
																	<td align="right">Precio:&#32;</td>
																	<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
																</tr>
															</logic:notEqual>
															<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																<tr>
																	<td align="right">Precio tot. sin IVA.:&#32;</td>
																	<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioEstado"/></td>
																</tr>
																<tr>
																	<td align="right">Precio total:&#32;</td>
																	<td align="left"><bean:write name="vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"/></td>
																</tr>											
															</logic:equal>
															<tr>
																<td align="right">Cantidad:&nbsp;</td>
																<td align="left"><bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/></td>
															</tr>
															<tr><td height="10px" colspan="2" width="100%"></td></tr>
														</table>
													</td>
												</tr>
												<logic:notEmpty name="vistaDetallePedidoDTO" property="articuloDTO">
													<tr>
														<td font-size="7pt">
															<table cellpadding="1" cellspacing="0" width="100%">
																<%--<tr><td colspan="7"><hr/></td></tr>--%>
																<tr>
																	<td align="left" width="30px">No</td>
																	<td align="left" width="120px">C&Oacute;DIGO BARRAS</td>
																	<td align="left" width="200px">ART&Iacute;CULO</td>
																	<td align="right">CANT.</td>
																	<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.codigoClasificacion" value="1606">
																		<td align="right">V.UNIT.</td>
																		<td align="right">V.UNIT.IVA</td>
																		<td align="right">T.NETO.INC.IVA</td>
																		<td align="right" width="20px">IVA</td>
																	</logic:equal>
																</tr>
																<%--<tr><td colspan="7"><hr/></td></tr>--%>
																<logic:iterate name="vistaDetallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="recetaArticuloDTO" indexId="registroReceta">
																	<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
																	<logic:equal name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
																		<c:set var="unidadManejo" value="${recetaArticuloDTO.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
																	</logic:equal>
																	<tr>
																		<bean:define id="fila" value="${registroReceta + 1}"/>
																		<td align="left" width="30px"><bean:write name="fila"/></td>
																		<td align="left" width="120px"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
																		<td align="left" width="200px"><bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="recetaArticuloDTO.articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
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
																	<td colspan="3"/>
																	<td align="right">TOTAL:</td >
																	<td align="right">		
																	<c:if test="${preciosAfiliado != null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																	</c:if>
																	<c:if test="${preciosAfiliado == null}">
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																	</c:if>
																	</td>
																	<td align="right" >
																		<c:if test="${preciosAfiliado != null}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																		</c:if>
																		<c:if test="${preciosAfiliado == null}">
																			<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																		</c:if>
																	</td>
																	<td align="right" >
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.estadoDetallePedidoDTO.valorTotalVenta}"/>
																	</td>
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
										<tr><td height="20px" align="right">.&#32;</td></tr>
										<tr><td><hr/></td></tr>
										<tr><td align="left" font-size="7pt">DETALLE DE ART&Iacute;CULOS QUE APLICAN DIFERIDOS (SIN INTERESES)</td></tr>
										<tr><td><hr/></td></tr>
										<tr>
											<td font-size="7pt">
												<c:set var="totalDiferidos" value="${0}"/>
												<c:set var="valorCero" value="0.00"/>
												<table width="100%" border="0" cellpadding="1" cellspacing="0">
													<tr>
														<td align="center" width="20px">No</td>
														<td align="center" width="70px">C&oacute;digo barras</td>
														<td align="center" width="170px">Art&iacute;culo</td>
														<td align="center" width="55px">Precio comercio</td>	
														<td align="center" width="55px">Precio uni. no/afi</td>
														<td align="center" width="55px">Precio uni. afi</td>
														<td align="center" width="45px">Cant.&#32;</td>
														<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO" indexId="indiceCuota">
															<td align="center" width="65px" ><bean:write name="cuotaDTO" property="numeroCuotas"/>MESES&#32;</td>
														</logic:iterate>
														<td align="center" width="15px">TOTAL</td>
													</tr>
													<c:set var="subTotal" value="${0}"/>
													<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesDiferidos" id="extructuraDiferidos" indexId="indiceDif">
														<bean:define id="fila" value="${indiceDif + 1}"/>
														<tr  valign="middle">
															<td width="20px" align="left"><bean:write name="fila"/></td>
															<td width="70px" align="left"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
															<td width="170px" align="left"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;(
															<logic:empty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
															&nbsp;,
															</logic:empty>
															<logic:notEmpty name="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
															<bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
															</logic:notEmpty>
															)</td>
															<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.PVP}"/></td>
															<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseNoAfiImp}"/></td>
															<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/></td>
															<td width="45px" align="center"><bean:write name="extructuraDiferidos" property="vistaDetallePedidoDTO.cantidadEstado"/></td>
															<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
																<c:if test="${duplex.secondObject != valorCero}">
																	<td align="center" width="65px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
																</c:if>
																<c:if test="${duplex.secondObject == valorCero}">
																	<td align="center" width="65px">-</td>
																</c:if>
															</logic:iterate>
															<c:set var="subTotal" value="${extructuraDiferidos.vistaDetallePedidoDTO.cantidadEstado * extructuraDiferidos.vistaDetallePedidoDTO.articuloDTO.precioBaseImp}"/>
															<c:set var="totalDiferidos" value="${totalDiferidos + subTotal}"/>
															<td width="15px" align="center"><bean:write name="subTotal"/></td>
														</tr>		
													</logic:iterate>
												</table>
											</td>
										</tr>
										<tr>
											<td font-size="7pt">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td align="right">TOTAL:</td>
														<td align="right" width="50px">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDiferidos}"/>
														</td>
													</tr>
													<tr><td colspan="2" align="right">-----------------------------</td></tr>
													<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
														<tr>
														 <td align="right"><bean:write name="cuotaDTO1" property="numeroCuotas"/>&#32;MESES:</td>
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
								    </logic:notEmpty>
							    </logic:notEmpty>
							</logic:notEmpty>
						</table>
					</td>
				</tr>
				<%-- mensaje final del reporte (depende del tipo de establecimiento) --%>
				<tr><td height="40px" align="right">.&#32;</td></tr>
				<logic:notEqual name="sispe.vistaEntidadResponsableDTO" property="codigoLocal" value="99" >
					<tr><td font-size="7pt" align="left">NOTAS:<br/></td></tr>
					<tr>
						<td font-size="7pt">
							<table cellpadding="1" cellspacing="0">					
									<tr>
										<td width="20px">1.- </td>
										<td align="left"> Los art&iacute;culos que estan marcados con un <b>(*)</b> no tiene suficiente stock.</td>	
									</tr>
									<tr>
										<td>2.- </td>
										<td>Le recordamos que el archivo del beneficiario debe tener la siguiente informaci&oacute;n:(C&eacute;dula, nombre, tel&eacute;fono, local o direcci&oacute;n de domicilio, fecha de entrega).</td>
									</tr>
							</table>
						</td>
					</tr>
					<tr><td font-size="7pt" align="left">OBSERVACIONES:<br/></td></tr>
					<tr>
						<td font-size="7pt">
							<table cellpadding="1" cellspacing="0">
								<%-- mensaje para los establecimientos que no son AKI --%>
								<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
									<tr>
										<td width="20px">1.- </td>
										<td>Para obtener el precio de afiliado, debe presentar la tarjeta de afiliaci&oacute;n (aplican restricciones).</td>
									</tr>
									<tr>
										<td>2.- </td>
										  <td>Los precios anotados son de afiliado 
										<c:if test="${preciosAfiliado != null}">
											__X__ No afiliado ___</td>
										</c:if>
			
										<c:if test="${preciosAfiliado == null}">
											___ No afiliado __X__</td>
										</c:if>
									</tr>
									<tr>
										<td>3.- </td>
										<td>El pago podra ser: efectivo, tarjeta de cr&eacute;dito, cheque, etc.</td>
									</tr>
									<tr>
										<td valign="top">4.- </td>
										<td>
											Para el pago con cheque presentar la tarjeta de afiliaci&oacute;n que deber&aacute; ser del titular de la cuenta<br/>
											corriente y el cheque a nombre de Supermaxi, Megamaxi o Corporaci&oacute;n Favorita C.A. (aplican restricciones).
										</td>
									</tr>
									<tr>
										<td>5.- </td>
										<td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
									</tr>
									<tr>
										<td valign="top">6.- </td>
										<td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
											el RUC y la raz&oacute;n social.
										</td>
									</tr>
									<tr>
										<td>7.- </td>
										<td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
									</tr>
									<tr>
										<td>8.- </td>
										<td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
									</tr>
									<tr>
										<td>9.- </td>
										<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
									</tr>
									<tr>
										<td>10.- </td>
										<td>Al confirmar el valor total de la proforma puede variar debido a un posible costo de flete si existen entregas a domicilio.</td>
									</tr>
									<tr>
										<td>11.- </td>
										<td>Al confirmar el valor total de la proforma puede variar si existen art&iacute;culos de peso variable ya que estos se reservan con el peso medio y en el momento de ser pesados puede variar el costo.</td>
									</tr>
									<tr>
										<td>12.- </td>
										<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
									</tr>
									<tr>
										<td>13.- </td>
										<td>Los valores autorizados para los art&iacute;culos que aplican a diferidos son:</td>
									</tr>
									<logic:notEmpty name="ec.com.smx.sic.sispe.diferidoCuotas">
										<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
											<tr>
											  <td align="right" colspan="1">&#32;&#32;&#32;<bean:write name="cuotaDTO1" property="numeroCuotas"/></td>
											  <td align="left">&#32;&#32;MESES&#32;$<bean:write name="cuotaDTO1" property="valorMinimo"/></td>
											</tr>
										</logic:iterate>
									</logic:notEmpty>
									<tr>
										<td>14.-</td>
										<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
									</tr>
								</logic:notEqual>
								<%-------- mensaje para los establecimientos AKI ------------%>
								<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
									<%-------- mensaje para los establecimientos AKI ------------%>
									<tr>
										<td width="20px">1.- </td>
										<td>El pago podra ser: efectivo, tarjeta de cr&eacute;dito, cheque, etc.</td>
									</tr>
									<tr>
										<td>2.- </td>
										<td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
									</tr>
									<tr>
										<td valign="top">3.- </td>
										<td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
											el RUC y la raz&oacute;n social.
										</td>
									</tr>
									<tr>
										<td>4.- </td>
										<td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
									</tr>
									<tr>
										<td>5.- </td>
										<td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
									</tr>
									<tr>
										<td>6.- </td>
										<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
									</tr>
									<tr>
										<td>7.- </td>
											<td>Al confirmar, el valor total de la proforma puede variar debido a un posible costo de flete si existen entregas a domicilio.</td>
										</tr>
									<tr>
										<td>8.- </td>
										<td>Al confirmar, el valor total de la proforma puede variar si existen art&iacute;culos de peso variable ya que estos se reservan con el peso medio y en el momento de ser pesados puede variar el costo.</td>
									</tr>
									<tr>
										<td>9.- </td>
										<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
									</tr>
									<tr>
										<td>10.-</td>
										<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
									</tr>
								</logic:equal>
							</table>
						</td>
					</tr>
					<tr><td height="40px" align="right">.&#32;</td></tr>
					<tr><td align="left" font-size="7pt">Firma y Sello: _________________________________&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;Elaborado por: <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npNombreUsuario"/></td></tr>
					<tr><td height="40px" align="right">.&#32;</td></tr>
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.numeroBonosRecibir">
						<tr><td align="left" font-size="7pt">COMPROBANTE BONO NAVIDAD EMPRESARIAL</td></tr>
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
							<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">
									<bean:define id="idMaxi" name="descuento" property="descuentoDTO.tipoDescuentoDTO.id.codigoTipoDescuento"/>
									<c:if test="${idMaxi == tipoDescuentoMaxiNavidad}"> 
									   <bean:define id="vDes" name="descuento" property="valorDescuento"/>
									   <bean:define id="porcentaje" name="descuento" property="porcentajeDescuento"/>
									</c:if>
							</logic:iterate>
						</logic:notEmpty>
						<tr><td><hr/></td></tr>
						<tr><td height="40px" align="right">&#32;</td></tr>
						<tr>
							<td width="100%" valign="top" font-size="7pt">
								<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
									<bean:define id="idLlaveContratoPOS" name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"></bean:define>
									<tr>
										<td align="left">
											No de pedido:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>		
										</td>					
									</tr>
									<tr><td height="5px"></td></tr>
									<c:if test="${idLlaveContratoPOS != null}">
									<tr>
										<td align="left">
											No de reservaci&oacute;n:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
										</td>
									</tr>
									</c:if>
									<tr><td height="5px"></td></tr>
								</table>
							</td>
						</tr>
						<tr>
							<td valign="top" font-size="7pt">
								<table cellpadding="0" cellspacing="0" width="100%" align="left">
									<tr>
										<td align="left">
											<table border="0" width="100%" cellspacing="0" align="left">
												<tr>
													<td colspan="2" align="left">Datos del contacto:</td>
												</tr>
												<tr><td height="3px" colspan="2"></td></tr>
												<tr>
													<td width="2px" align="left">Documento:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto"/></td>
												</tr>
												<tr>
													<td width="2px" align="left">Nombre:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto"/></td>
												</tr>
												<tr>
													<td width="2px" align="left">Tel&eacute;fono:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoContacto"/></td>
												</tr>
											</table>
										</td>
										<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="contextoPedido" value="${tipoPedidoEmpresarial}">
											<td valign="top">
												<table border="0" width="100%" cellspacing="0">
													<tr align="center">
														<td colspan="2" align="left">Datos de la empresa:</td>
													</tr>
													<tr><td height="3px" colspan="2"></td></tr>
													<tr>
														<td width="10%" align="right">Ruc:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="rucEmpresa"/></td>
													</tr>
													<tr>
														<td align="right">Empresa:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa"/></td>
													</tr>
												</table>
											</td>
										</logic:equal>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="3px" colspan="2" align="right">.&#32;</td></tr>
						<tr>
							<td align="left" valign="top" font-size="7pt">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="left">
											Fecha de elaboración:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fecha"/>
										</td>
										<td align="left">
											Elaborado en:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&#32;-&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
										</td>
									</tr>
									<tr><td height="7px"></td></tr>
									<tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
											<td align="left">
												Teléfono local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.telefonoLocal"/>
											</td>
											<td align="left">
												Administrador local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO.nombreAdministrador"/>
											</td>
										</logic:notEmpty>
										<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="npLocalDTO">
											<td align="left">
												Teléfono local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
											</td>
											<td align="left">
												Administrador local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
											</td>
										</logic:empty>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td><hr/></td></tr>
						<tr><td height="40px" align="right">.&#32;</td></tr>
						<tr>
							<td colspan="2" align="right" font-size="7pt">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="left" colspan="2">
											En sus pagos de CONTADO Y CREDITO CORRIENTE por cada $<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${montoCompraMinima}"/> dólares de compra, reciba un bono de $<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorBono}"/> dólares.
										</td>
									</tr>
									<tr><td height="90px" align="right">.&#32;</td></tr>
									<tr><td align="left" colspan="2">-----------------------------------------</td></tr>
									<tr>
										<td align="left" width="700px">VALOR CALCULADO:.&#32;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCalculado}"/></td>
									</tr>
									<tr>
										<td align="left" width="70px">NAVIDAD EMPRESARIAL:.&#32;<bean:write name="porcentaje"/>%</td>
									</tr>
									<tr>
										<td align="left" width="70px">No BONOS A RECIBIR:.&#32;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${numeroBonos}"/></td>
									</tr>
									<tr><td align="left" colspan="2">-----------------------------------------</td></tr>
									<tr><td height="200px" align="right">.&#32;</td></tr>
									<tr>
										<td colspan="2" align="right">
											<table border="0" width="100%" cellpadding="0" cellspacing="0">
												<tr>
													<td align="left" colspan="2">INFORMACIÓN DE ENTREGA DE BONOS</td>
												</tr>
												<tr><td><hr/></td></tr>
												<tr><td height="180px">.&#32;</td></tr>
												<tr>
													<td align="left"># FACTURA:______________________________________________</td>
												</tr>
												<tr><td height="100px">.&#32;</td></tr>
												<tr>
													<td align="left">FORMA PAGO:______________________________________________</td>
												</tr>
												<tr><td height="100px">.&#32;</td></tr>
												<tr>
													<td align="left">FECHA ENTREGA:______________________________________________</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="280px">.&#32;</td></tr>
						<tr>
							<td align="center">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="center">____________________________&#32;&#32;</td>
										<td align="center">&#32;&#32;____________________________</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="center" valign="top" font-size="7pt">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="center">Firma del cliente&#32;&#32;</td>
										<td align="center">&#32;&#32;Firma persona responsable del local</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td height="180px">.&#32;</td></tr>
						<tr>
							<td align="center" valign="top" font-size="7pt">
								<table border="0" width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td align="center">NOMBRE:____________________________&#32;&#32;</td>
										<td align="center">&#32;&#32;NOMBRE:____________________________</td>
									</tr>
								</table>
							</td>
						</tr>
					</logic:notEmpty>
				</logic:notEqual>
			</table>
			<%--Fin datos de ventas--%>
		</body>
	</html>
</kreport:pdfout>