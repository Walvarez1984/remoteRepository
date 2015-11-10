<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="pathSave" name="ec.com.smx.sic.sispe.reporte.directorioSalida"/>

<kreport:pdfout saveToServer="${pathSave}">
	<html page-orientation="H">
		<head>
			<meta http-equiv="Content-Style-Type" content="text/css"/>
			<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
			<meta HTTP-EQUIV="max-age" CONTENT="0"/>
			<meta HTTP-EQUIV="Expires" CONTENT="0"/>
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
			<logic:notEmpty name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo">
				<bean:define id="clasificacionCanastosCatalogo" name="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/>
			</logic:notEmpty>
			
			<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
			<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo">
				<bean:define id="codigoEstablecimiento" name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo"/>
			</logic:notEmpty>
			<bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
			<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
			<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
				<tr>
					<td valign="top" font-size="7pt">
						<table border="0" cellspacing="0" cellpadding="1" width="100%">
							<tr>
								<td align="left">
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&#32;-&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&#32;
									<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
								</td>
								<td align="right">Ruc:&#32;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
							</tr>
							<tr><td height="10px" colspan="2"></td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center" valign="top" font-size="7pt">
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td align="left">
									<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
										<bean:message key="ec.com.smx.sic.sispe.titulo.cotizacion"/>
										&#32;(V&aacute;lida por&#32;<bean:write name="ec.com.smx.sic.sispe.cotizacion.diasValidez"/>&#32;d&iacute;as)
									</logic:notEqual>
									<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
										<bean:message key="ec.com.smx.sic.sispe.titulo.reservacion"/>
										<%--(Abono Inicial:&#32;<bean:write name="cotizarRecotizarReservarForm" property="valorAbono" formatKey="formatos.numeros"/>)--%>
									</logic:equal>
								</td>
							</tr>
							<tr><td><hr/></td></tr>
							<tr>
								<td width="100%">
									<table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
										<tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
												<td align="left">
													No. de pedido:&#32;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>
												</td>
												<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
													<td align="left">
														No. de reservaci&oacute;n:&#32;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="llaveContratoPOS"/>
													</td>
												</logic:equal>
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">
												<td>&#32;</td>
											</logic:empty>
										</tr>
										<tr><td height="5px"></td></tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="100%">
										<tr>
											<td>
												<table border="0" width="100%" cellspacing="0" cellpadding="1" align="left">
													<tr>
														<td width="60px" align="right"></td>
														<td align="left"></td>
													</tr>
													<tr>
														<td colspan="2" align="left">Datos del contacto:</td>
													</tr>
													<tr><td height="3px" colspan="2"></td></tr>
													<tr>
														<td align="right">CI/Pasaporte:</td>
														<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="cedulaContacto"/></td>
													</tr>
													<tr>
														<td align="right">Nombre:</td>
														<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="nombreContacto"/></td>
													</tr>
													<tr>
														<td align="right">Tel&eacute;fono:</td>
														<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="telefonoContacto"/></td>
													</tr>
													<tr>
														<td align="right">Email:</td>
														<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="emailContacto"/></td>
													</tr>
												</table>
											</td>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.empresarial">
												<td valign="top">
													<table border="0" width="100%" cellspacing="0">
														<tr>
															<td width="45px" align="right"></td>
															<td align="left"></td>
														</tr>
														<tr align="center">
															<td colspan="2" align="left">Datos de la empresa:</td>
														</tr>
														<tr><td height="3px" colspan="2"></td></tr>
														<tr>
															<td width="10%" align="right">Ruc:</td>
															<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="rucEmpresa"/></td>
														</tr>
														<tr>
															<td width="10%" align="right">Empresa:</td>
															<td align="left"><bean:write name="cotizarRecotizarReservarForm" property="nombreEmpresa"/></td>
														</tr>
													</table>
												</td>
											</logic:notEmpty>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td height="7px"></td></tr>
							<tr>
								<td align="right">
									<table border="0" width="100%" cellpadding="1" cellspacing="0">
										<tr>
											<td align="left">
												Lugar y fecha:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&#32;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
											</td>
											<td align="left">
												Elaborado para:&#32;<bean:write name="cotizarRecotizarReservarForm" property="localResponsable"/>
											</td>
										</tr>
										<tr>
											<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left">
													Tel&eacute;fono local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
												</td>
												<td align="left">
													Administrador local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
												</td>
											</logic:empty>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
												<td align="left">
													Tel&eacute;fono local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
												</td>
												<td align="left">
													Administrador local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
												</td>
											</logic:notEmpty>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td><hr/></td></tr>
							<tr><td font-size="7pt">Detalle de los art&iacute;culos</td></tr>
							<tr><td><hr/></td></tr>
							<tr>
								<td>
									<table border="0" cellpadding="1" cellspacing="0" width="100%">
										<tr>
											<td align="left" width="20px">No</td>
											<td align="left">C&oacute;digo</td>
											<td align="left" width="150px">Art&iacute;culo</td>
											<td align="right" width="40px">Cant.</td>
											<td align="right">Peso KG.</td>
											<td align="right">V. unit.</td>
											<td align="right">Tot. IVA</td>
											<td align="right" width="25px">IVA</td>
											<td align="right">Dscto.</td>
											<%--<td align="right">V.Unit. Neto</td>--%>
											<td align="right">Tot. neto</td>
										</tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
											<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numPedido">
												<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
												<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
													<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
												</logic:equal>
												<bean:define id="fila" value="${numPedido + 1}"/>
												<c:set var="pesoVariable" value=""/>
												<logic:equal name="detalle" property="articuloDTO.tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
												</logic:equal>
												<logic:equal name="detalle" property="articuloDTO.tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
												</logic:equal>
												<tr valign="top">
													<td align="left" width="20px"><bean:write name="fila"/></td>
													<td align="left"><bean:write name="detalle" property="articuloDTO.codigoBarras"/></td>
													<td align="left"><bean:write name="detalle" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="detalle" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
													<td align="right">
														<logic:notEqual name="detalle" property="articuloDTO.tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
														</logic:notEqual>
														<logic:equal name="detalle" property="articuloDTO.tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">&nbsp;</logic:equal>
													</td>
													<td align="right">
														<logic:equal name="pesoVariable" value="${estadoActivo}">
															<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
														</logic:equal>
														<logic:notEqual name="pesoVariable" value="${estadoActivo}">&nbsp;</logic:notEqual>
													</td>
													<td align="right">
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
													<td align="right">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
													</td>
													<td align="right">
														<logic:equal name="detalle" property="articuloDTO.aplicaIVA" value="${estadoActivo}">I</logic:equal>
														<logic:notEqual name="detalle" property="articuloDTO.aplicaIVA" value="${estadoActivo}">&#32;</logic:notEqual>
													</td>
													<td class="fila_contenido columna_contenido" align="center">&#32;
														<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
													</td>
													<%--<td align="right">
														<bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
													</td>--%>
													<td align="right">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
													</td>
												</tr>
												<%---------------- secci&oacute;n que muestra las entregas ------------%>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionEntregas">
													<logic:notEmpty name="detalle" property="entregaDetallePedidoCol">
														<tr><td height="5px" colspan="10"></td></tr>
														<tr>
															<td colspan="10" font-size="7pt">
																<%------------------se muestra la colecci&oacute;n de entregas del articulo -----------------------%>
																<table border="0" cellspacing="0" cellpadding="0">
																	<tr>
																		<td align="center" valign="top" width="50px">Cant. despacho</td>
																		<td align="center" valign="top" width="50px">Cant. entrega</td>
																		<td align="center" valign="top" width="60px">Fecha despacho</td>
																		<td align="center" valign="top" width="80px">Fecha entrega</td>
																		<td align="left" valign="top" width="400px">Lugar entrega</td>
																	</tr>
																	<logic:iterate name="detalle" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
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
														<tr><td height="5px" colspan="10"></td></tr>
													</logic:notEmpty>
												</logic:notEmpty>
											</logic:iterate>
										</logic:notEmpty>
										<tr><td height="40px" align="right">.&#32;</td></tr>
										<tr>
											<td colspan="10" align="right">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr valign="bottom">
														<td colspan="4" align="right">SUBTOTAL:&#32;</td>
														<td align="right" width="50px">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotal}"/>
														</td>
													</tr>
													<tr valign="bottom"><td colspan="5" align="right">--------------------------------</td></tr>
													<tr valign="bottom">
														<td colspan="4" align="right">TARIFA 0%:&#32;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td colspan="4" align="right">TARIFA&#32;<bean:message key="porcentaje.iva"/>%:&#32;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td colspan="4" align="right"><bean:message key="porcentaje.iva"/>%&#32;IVA:&#32;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
														</td>
													</tr>
													<tr valign="bottom">
														<td colspan="4" align="right">COSTO FLETE&#32;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
														</td>
													</tr>
													<tr valign="bottom"><td colspan="5" align="right">--------------------------------</td></tr>
													<tr valign="bottom">
														<td colspan="4" align="right">TOTAL:&#32;</td>
														<td align="right">
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/>
														</td>
													</tr>
												</table>
											</td>
											<td>&#32;</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDescuentos">
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
						<tr><td height="20px" align="right">.&#32;</td></tr>
						<tr><td><hr/></td></tr>
						<tr>
							<td height="10px" font-size="7pt">Detalle de los descuentos</td>
						</tr>
						<tr><td><hr/></td></tr>
						<tr>
							<td font-size="7pt">
								<table border="0" cellpadding="1" cellspacing="0" width="70%" align="right">
									<tr>
										<td align="left">DESCRIPCION</td>
										<%--<td align="right">V.TOTAL</td>--%>
										<td align="right">PORCENTAJE</td>
										<td align="right">DESCUENTO</td>
									</tr>
									<c:set var="totalDescuento" value="0"/>
									<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
										<tr>
											<td align="left"><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></td>
											<%--<td align="right"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/></td>--%>
											<td align="right">
												<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>
												</logic:greaterThan>
												<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
											</td>
											<td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
										</tr>
										<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
									</logic:iterate>
									<tr><td height="20px" colspan="4"></td></tr>
									<tr>
										<td align="right" colspan="3">TOTAL:</td>
										<%-- <td align="right"><bean:write name="ec.com.smx.sic.sispe.pedido.descuento.porcentajeTotal" formatKey="formatos.numeros"/>%</td>--%>
										<td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></td>
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
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.existenCanastos">
						<tr><td height="20px" align="right">.&#32;</td></tr>
						<tr><td><hr/></td></tr>
						<tr><td font-size="7pt">Detalle de los canastos</td></tr>
						<tr><td><hr/></td></tr>
						<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detallePedidoDTO">
							
							<%--<c:if test="${detallePedidoDTO.articuloDTO.codigoTipoArticulo == tipoCanasto || detallePedidoDTO.articuloDTO.codigoTipoArticulo == tipoDespensa}"> --%>
							<c:if test="${detallePedidoDTO.articuloDTO.codigoClasificacion == articuloEspecial|| detallePedidoDTO.articuloDTO.codigoClasificacion == canastoCatalogo}">
								<tr>
									<td font-size="7pt">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<td align="right" width="50px">Descripci&oacute;n:&#32;</td>
												<td align="left"><bean:write name="detallePedidoDTO" property="articuloDTO.descripcionArticulo"/></td>
											</tr>
											<tr>
												<td align="right">Precio:&#32;</td>
												<td align="left"><bean:write name="detallePedidoDTO" property="estadoDetallePedidoDTO.valorUnitarioIVAEstado"/></td>
											</tr>
											<tr><td height="10px" colspan="2" width="100%"></td></tr>
										</table>
									</td>
								</tr>
								<logic:notEmpty name="detallePedidoDTO" property="articuloDTO.recetaArticulos">
									<tr>
										<td font-size="7pt">
											<table cellpadding="1" cellspacing="0" width="100%">
												<tr>
													<td align="left" width="30px">No</td>
													<td align="left" width="120px">CODIGO</td>
													<td align="left" width="200px">ARTICULO</td>
													<td align="right">CANT.</td>
												</tr>
												<logic:iterate name="detallePedidoDTO" property="articuloDTO.recetaArticulos" id="recetaArticuloDTO" indexId="registroReceta">
													<c:set var="unidadManejo" value="${recetaArticuloDTO.articuloDTO.unidadManejo}"/>
													<logic:equal name="recetaArticuloDTO" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
													  <c:set var="unidadManejo" value="${recetaArticuloDTO.articuloDTO.unidadManejoCaja}"/>
													</logic:equal>
													<tr>
														<bean:define id="fila" value="${registroReceta + 1}"/>
														<td align="left" width="30px"><bean:write name="fila"/></td> 
														<td align="left" width="120px"><bean:write name="recetaArticuloDTO" property="articuloDTO.codigoBarras"/></td>
														<td align="left" width="200px"><bean:write name="recetaArticuloDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
														
														<td align="right"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>
													</tr>
												</logic:iterate>
											</table>
										</td>
									</tr>
								</logic:notEmpty>
								<logic:empty name="detallePedidoDTO" property="articuloDTO.recetaArticulos">
									<tr><td font-size="7pt">Canasto vacio</td></tr>
								</logic:empty>
								<tr><td><hr/></td></tr>
							</c:if>
						</logic:iterate>
					</logic:notEmpty>
				</logic:notEmpty>
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.impresionDiferidos">
				    <logic:notEmpty name="ec.com.smx.sic.sispe.reporteDiferidos">
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
										<td align="center" width="70px">C&oacute;digo</td>
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
									<logic:iterate name="ec.com.smx.sic.sispe.reporteDiferidos" id="extructuraDiferidos" indexId="indiceDif">
										<bean:define id="fila" value="${indiceDif + 1}"/>
										<tr  valign="middle">
											<td width="20px" align="left"><bean:write name="fila"/></td>
											<td width="70px" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.id.codigoArticulo"/></td>
											<td width="170px" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;(											
											<logic:empty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												&nbsp;,
											</logic:empty>
											<logic:notEmpty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
												<bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
											</logic:notEmpty>											
											)</td>
											<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioArticuloIVAProveedor}"/></td>
											<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioArticuloIVANoAfiliado}"/></td>
											<td width="55px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioArticuloIVA}"/></td>
											<td width="45px" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado}"/></td>
											<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
												<c:if test="${duplex.secondObject != valorCero}">
													<td align="center" width="65px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
												</c:if>
												<c:if test="${duplex.secondObject == valorCero}">
													<td align="center" width="65px">-</td>
												</c:if>
											</logic:iterate>
											<c:set var="subTotal" value="${extructuraDiferidos.detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado * extructuraDiferidos.detallePedidoDTO.articuloDTO.precioArticuloIVA}"/>
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
				<%-- mensaje final del reporte (depende del tipo de establecimiento) --%>
				<tr><td height="40px" align="right">.&#32;</td></tr>
				<tr><td font-size="7pt" align="left">NOTAS:<br/></td></tr>
				<tr>
					<td font-size="7pt">
						<table cellpadding="1" cellspacing="0">					
								<tr>
									<td width="20px">1.- </td>
									<td>Los art&iacute;culos que estan marcados con un (*) de color rojo no tienen suficiente stock.</td>
								</tr>
								<tr>
									<td>2.- </td>
									<td>Le recordamos que el archivo del beneficiario debe tener la siguiente informaci&oacute;n:(C&eacute;dula, nombre, tel&eacute;fono, local o direcci&oacute;n de domicilio, fecha de entrega).</td>
								</tr>
						</table>
					</td>
				</tr>
				<tr><td align="left" font-size="8pt">OBSERVACIONES:<br/></td></tr>
				<tr>
					<td font-size="8pt">
						<table cellpadding="1" cellspacing="0">
							<%-- mensaje para los establecimientos que no son AKI --%>
							<logic:notEqual name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
								<tr>
									<td>1.- </td>
									<td>Para obtener el precio de afiliado, debe presentar la tarjeta de afiliaci&oacute;n (aplican restricciones).</td>
								</tr>
								<tr>
									<td>2.- </td>
									<td>Los precios anotados son de afiliado 
									<bean:define id="preciosAfiliado" name="ec.com.smx.sic.sispe.pedido.preciosAfiliado"></bean:define>
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
									<td>13.-</td>
									<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
								</tr>
							</logic:notEqual>
							<%-- mensaje para los establecimientos AKI --%>
							<logic:equal name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
								<tr>
									<td>1.- </td>
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
				<tr><td align="left" font-size="8pt">Firma y sello: _________________________________&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;Elaborado por: <bean:write name="vistaEntidadResponsableDTO" property="nombreCompletoUsuario"/></td></tr>
			</table>
		</body>
	</html>
</kreport:pdfout>