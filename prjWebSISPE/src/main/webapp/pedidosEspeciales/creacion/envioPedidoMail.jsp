<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dTD">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<html>
<head>
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
	<meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
	<meta HTTP-EQUIV="max-age" CONTENT="0">
	<meta HTTP-EQUIV="Expires" CONTENT="0">
</head>
<body>
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td align="center" valign="top">
							<table border="0" class="textoNegro11" width="100%" align="center" cellpadding="0" cellspacing="0">
							<%--------------------------- secci&oacute;n del resumen del pedido ---------------------%>
								<tr>
									<td height="5px"></td>
								</tr>
								<tr>
									<td valign="top" font-size="7pt">
										<table border="0" cellspacing="0" cellpadding="1" width="100%">
											<tr>
												<td align="left" style="font-size:10;font-family: Verdana, Arial, Helvetica;color:#FF0000;font-weight: bold;">
												CORPORACI&Oacute;N FAVORITA</td>
											</tr>
											<tr>
												<td height="10px" colspan="2" align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 10px;color: #000000;font-weight: bold;">
												Ruc:&nbsp;<bean:message key="security.CURRENT_COMPANY_RUC" /></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr align="center">
												<td align="right" width="50%">
													<table border="0" width="100%" cellspacing="0">
														<tr>
															<td align="left" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;color: #000000;" colspan="2">
															<b>No de Pedido:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="id.codigoPedido" /></td>
														</tr>
														
													</table>
												</td>
				
											</tr>
											
											<tr>
												<table border="0" width="100%" cellspacing="0" cellpadding="1" align="left" class="textoNegro10" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;text-align:justify">											
													<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
													<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
														<tiles:put name="tformName" value="crearPedidoEspecialForm"/>
														<tiles:put name="textoNegro" value="textoNegro10"/>
														<tiles:put name="textoAzul" value="textoAzul10"/>
													</tiles:insert>																																										
												</table>										
											</tr>
			
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="2" align="right">
										<table border="0" width="100%" class="textoNegro11 tabla_informacion" cellpadding="0" cellspacing="0">
											<tr>
												<td height="5px"></td>
											</tr>
											<tr>
												<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;"><b>Elaborado en:&nbsp;</b>
		 											<html:hidden name="crearPedidoEspecialForm" property="localResponsable" write="true" />
		 										</td>
			 									<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
													<b>Lugar y Fecha:</b>&nbsp;
													<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad" />,&nbsp;
													<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
												</td>
											</tr>
											<tr>
												<td height="7px"></td>
											</tr>
											<tr>
												<logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
													<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<b>Administrador Local:</b>&nbsp;
														<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador" />
													</td>
													<td align="left" width="50%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<b>Tel&eacute;fono Local:</b>&nbsp;
														<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal" />
													</td>
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
													<td align="left" width="40%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<b>Administrador Local:</b>&nbsp;
														<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal" />
													</td>
													<td align="left" width="20%" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
														<b>Tel&eacute;fono Local:</b>&nbsp;
														<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal" />
													</td>
												</logic:notEmpty>
											</tr>
											<tr>
												<td height="5px"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td height="5px"></td>
								</tr>
								<tr>
									<td colspan="2">
										<table border="0" width="99%" cellpadding="0" cellspacing="0">
											<tr>
												<td style="font-family: Verdana, Arial, Helvetica;font-size: 11px;" height="20px" align="left" colspan="2">
													<b>Lista de los art&iacute;culos del pedido</b>
												</td>
											</tr>
											<tr>
												<td>
													<table border="0" align="left" cellspacing="0" cellpadding="1" width="100%" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
														<tr style="background-color:#FF0F0F;color: #FFFFFF;font-size: 9px;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" align="center" width="5%">No</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" width="12%" align="center">C&Oacute;DIGO BARRAS</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" align="center" width="28%">ART&Iacute;CULO</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" align="center" width="7%">CANTIDAD</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" align="center" width="7%">PESO</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" width="10%" align="center">V. UNITARIO</td>
															<td style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;" width="31%" align="center">OBSERVACI&Oacute;N</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table border="0" width="100%" cellpadding="1" cellspacing="0">
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
															<logic:iterate name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol" id="detalle" indexId="indice">
																<bean:define id="numFila" value="${indice + 1}" />
																<%--------------------------------------------------------------------%>
																<tr style="font-family: Verdana, Arial, Helvetica;font-size: 9px;">
																	<td width="5%" align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="numFila" />
																	</td>
																	<td width="12%" align="center" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras" />
																	</td>
																	<td width="28%" align="left" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="detalle" property="articuloDTO.descripcionArticulo" />
																	</td>
																	<td width="7%" align="right" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" />
																	</td>
																	<td width="7%" align="right" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado" />
																	</td>
																	<td width="10%" align="right" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																	</td>
																	<td width="31%" align="left" style="border-bottom-width: 1px;border-bottom-style: solid;border-bottom-color: #cccccc;">&nbsp;
																		<bean:write name="detalle" property="estadoDetallePedidoDTO.observacionArticulo" /> 
																</tr>
															</logic:iterate>
														</logic:notEmpty>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<%-------- mensaje final del reporte (depende del tipo de establecimiento)----------------%>
		<tr>
			<td height="40px" align="right">&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table style="font-family: Verdana, Arial, Helvetica;font-size: 10px;" cellpadding="1" cellspacing="0">
					<tr>
			<td><b>OBSERVACIONES:</b><br/></td>
		</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td >
				<table style="font-family: Verdana, Arial, Helvetica;font-size: 9px;" cellpadding="1" cellspacing="0">
					<%-------- mensaje para los establecimientos que no son AKI ------------%>
					<logic:notEqual name="codigoEstablecimiento"
						value="${codigoEstablecimientoAki}">
						<tr>
							<td width="20px">1.-</td>
							<td>Para obtener el precio de Afiliado, debe presentar la
							Tarjeta de Afiliaci&oacute;n (aplican restricciones).</td>
						</tr>
						<tr>
							<td>2.-</td>
							  <td>Los precios anotados son de Afiliado 
							  <bean:define id="preciosAfiliado" name="ec.com.smx.sic.sispe.pedido.preciosAfiliado"></bean:define>
									<c:if test="${preciosAfiliado != null}">
										__X__ No Afiliado ___
									</c:if>
		
									<c:if test="${preciosAfiliado == null}">
										___ No Afiliado __X__
									</c:if>
							   </td>
						</tr>
						<tr>
							<td>3.-</td>
							<td>El pago podra ser: Efectivo, Tarjeta de Cr&eacute;dito,
							Cheque, etc.</td>
						</tr>
						<tr>
							<td valign="top">4.-</td>
							<td>Para el pago con cheque presentar la Tarjeta de
							Afiliaci&oacute;n que deber&aacute; ser del titular de la cuenta<br />
							corriente y el cheque a nombre de Supermaxi, Megamaxi o
							Corporaci&oacute;n Favorita C.A. (aplican restricciones).</td>
						</tr>
						<tr>
							<td>5.-</td>
							<td>En caso de efectuarse Retenci&oacute;n a la fuente
							deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita
							C.A.</td>
						</tr>
						<tr>
							<td valign="top">6.-</td>
							<td>Si se va a requerir la factura debe canjearse la Nota de
							Venta, acercandose a Servicios al Cliente y presentar<br />
							el RUC y la Raz&oacute;n Social.</td>
						</tr>
						<tr>
							<td>7.-</td>
							<td>La confirmaci&oacute;n del Cliente para surtir esta
							Proforma, debe ser con un m&iacute;nimo de 72 Horas de
							anticipaci&oacute;n.</td>
						</tr>
						<tr>
							<td>8.-</td>
							<td>Al confirmar debe ser cancelado el valor total de la
							Proforma.</td>
						</tr>
						<tr>
							<td>9.-</td>
							<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
						</tr>
						<tr>
							<td>10.-</td>
							<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
						</tr>
						<tr>
							<td>11.-</td>
							<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
						</tr>
					</logic:notEqual>
					<%-------- mensaje para los establecimientos AKI ------------%>
					<logic:equal name="codigoEstablecimiento"
						value="${codigoEstablecimientoAki}">
						<%-------- mensaje para los establecimientos AKI ------------%>
						<tr>
							<td width="20px">1.-</td>
							<td>El pago podra ser: Efectivo, Tarjeta de Cr&eacute;dito,
							Cheque, etc.</td>
						</tr>
						<tr>
							<td>2.-</td>
							<td>En caso de efectuarse Retenci&oacute;n a la fuente
							deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita
							C.A.</td>
						</tr>
						<tr>
							<td valign="top">3.-</td>
							<td>Si se va a requerir la factura debe canjearse la Nota de
							Venta, acercandose a Servicios al Cliente y presentar<br />
							el RUC y la Raz&oacute;n Social.</td>
						</tr>
						<tr>
							<td>4.-</td>
							<td>La confirmaci&oacute;n del Cliente para surtir esta
							Proforma, debe ser con un m&iacute;nimo de 72 Horas de
							anticipaci&oacute;n.</td>
						</tr>
						<tr>
							<td>5.-</td>
							<td>Al confirmar debe ser cancelado el valor total de la
							Proforma.</td>
						</tr>
						<tr>
							<td>6.-</td>
							<td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
						</tr>
						<tr>
							<td>7.-</td>
							<td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
						</tr>
						<tr>
							<td>8.-</td>
							<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
						</tr>
					</logic:equal>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
