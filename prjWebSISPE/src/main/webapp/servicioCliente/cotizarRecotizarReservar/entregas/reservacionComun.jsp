<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- insertada desde entregaArticuloLocal.jsp -->
<logic:empty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
	<c:set var="altoDivDetArt" value="400"/>
</logic:empty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
	<c:set var="altoDivDetArt" value="260"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigoArticulo">
	<bean:define id="codigoArt" name="ec.com.smx.sic.sispe.codigoArticulo"/>
</logic:notEmpty>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<div id="reserva">
				<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
					<tr>
						<td class="fila_titulo" height="29px">
							<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
								<tr>
									<td width="30px" align="right"><img src="images/pedidos24.gif" border="0"></td>
									<td>&nbsp;Detalle del pedido</td>
									<td width="3px"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
					<tr>
						<td>
							<table border="0" class="textos" width="100%" align="center" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0" border="0" width="100%">
											<tr>
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
													<td width="20%" class="textoAzul11" align="left">No Pedido:&nbsp;</td>
													<td width="80%" align="left">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
													</td>
												</logic:notEmpty>
											</tr>
											<tr>
												<td width="20%" class="textoAzul11" align="left">Nombre Contacto:&nbsp;</td>
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
													<td width="80%" align="left">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>
													</td>
												</logic:notEmpty>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
									<div id="fDespacho">
										<table cellpadding="0" cellspacing="0">
											<logic:notEmpty name="calendarioBodegaForm" property="fechaDespacho">
												<td width="20%" class="textoAzul11">Fecha Recepci&oacute;n:</td>
												<td width="80%">
													<bean:write name="calendarioBodegaForm" property="fechaDespacho"/>
												</td>
											</logic:notEmpty>
										</table>
									</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
					<tr>
						<td valign="top">
							<%--<div id="reserva">--%>
							<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr align="left" class="fila_titulo">
								<td colspan="2" class="textoNegro11" height="20px"><b>Detalle del Pedido:</b></td>
							</tr>
							<tr>
								<td colspan="2" bgcolor="#F4F5EB">
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr class="tituloTablas" height="15px">
											<td class="columna_contenido" align="center">&nbsp;</td>
											<td class="columna_contenido" align="center">No</td>
											<td class="columna_contenido" align="center">C&oacute;digo barras</td>
											<td class="columna_contenido" align="center">Art&iacute;culo</td>
											<td class="columna_contenido" align="center" title="cantidad total a entregar">Cant.</td>
											<td class="columna_contenido" align="center" title="cantidad reservada en bodega">Cant. Res.</td>
										</tr>									
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<logic:equal name="vistaDetallePedidoDTO" property="id.codigoArticulo" value="${codigoArt}">
											<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
												<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejoCaja}"/>
											</logic:equal>
											<bean:define id="residuo" value="${indiceDetalle % 2}"/>
											<logic:equal name="residuo" value="0">
												<bean:define id="colorBack" value="blanco10"/>
											</logic:equal>
											<logic:notEqual name="residuo" value="0">
												<bean:define id="colorBack" value="grisClaro10"/>
											</logic:notEqual>
											<tr class="${colorBack}">
												<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
												<c:set var="pesoVariable" value=""/>
												<c:set var="imagen" value=""/>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<c:set var="imagen" value="pavo.gif"/>
												</logic:equal>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<c:set var="imagen" value="balanza.gif"/>
												</logic:equal>
												<%-- secci&oacute;n que muestra las entregas y beneficiarios --%>
												<td class="fila_contenido">
													<logic:notEmpty name="vistaDetallePedidoDTO" property="entregas">
														<div id="desplegarE${indiceDetalle}" class="displayNo">
															<a title="ver entregas" href="#" onClick="mostrar(${indiceDetalle},'marcoE','plegarE','desplegarE');">
																<html:img src="images/desplegar.gif" border="0"/>
															</a>
														</div>
														<div id="plegarE${indiceDetalle}" >
															<a title="ocultar entregas" href="#" onClick="ocultar(${indiceDetalle},'marcoE','plegarE','desplegarE');">
																<html:img src="images/plegar.gif" border="0"/>
															</a>
														</div>
													</logic:notEmpty>
													<logic:empty name="vistaDetallePedidoDTO" property="entregas">&nbsp;</logic:empty>
												</td>
												<td align="left" class="columna_contenido fila_contenido"><bean:write name="numRegistro"/></td>
												<td class="columna_contenido fila_contenido" align="left">
													<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<html:link href="#" onclick="requestAjax('calendarioBodega.do',['informacionOrdenCompra'],{parameters: 'indiceInfoOrdenCompra=${indiceDetalle}'});" title="detalle de la orden de compra"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarras"/></html:link>
														</logic:notEmpty>
														<logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarras"/>
														</logic:empty>
													</logic:empty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarras"/>
													</logic:notEmpty>
												</td>
												<td class="columna_contenido fila_contenido">
													<table border="0" width="100%" cellpadding="1" cellspacing="0">
														<tr>
																<td align="left">
																	<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
																	<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
																</td>
															<td width="2px">&nbsp;</td>
														</tr>
													</table>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="cantidad total a entregar">
													<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="cantidad reservada en bodega">
													<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<logic:notEmpty name="vistaDetallePedidoDTO" property="cantidadReservarSIC">
															<bean:write name="vistaDetallePedidoDTO" property="cantidadReservarSIC"/>
														</logic:notEmpty>
														<logic:empty name="vistaDetallePedidoDTO" property="cantidadReservarSIC">&nbsp;</logic:empty>
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
												</td>
												<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
													<td class="columna_contenido fila_contenido" align="center" title="cantidad producida hasta el momento">
														<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<bean:write name="vistaDetallePedidoDTO" property="cantidadParcialEstado"/>
														</logic:notEqual>
														<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
														</logic:equal>
													</td>
												</logic:equal>
											</tr>
											<%-- MUESTRA EL DETALLE DE LAS ENTREGAS --%>
											<logic:notEmpty name="vistaDetallePedidoDTO" property="entregas">
												<tr>
													<td colspan="14" class="columna_contenido" align="center">
														<bean:define name="vistaDetallePedidoDTO" id="configuracionDTO"/>
														<table cellpadding="1" cellspacing="0" width="100%">
															<tr>
																<td>
																	<div id="marcoE${indiceDetalle}" >
																		<table width="95%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion_negro ">
																			<tr class="tituloTablasCeleste">
																				<td class="columna_contenido_der_negro fila_contenido_negro" align="center" width="2%" rowspan="2">No.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="5%" rowspan="2">Cant. Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="11%" rowspan="2">F. Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" colspan="2" title="datos de producci&oacute;n">Producci&oacute;n</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" rowspan="2">F. Despacho</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="18%" rowspan="2">Destino Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" rowspan="2">Contexto Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="17%" rowspan="2">Tipo Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="15%" rowspan="2">Stock Entrega</td>
																			</tr>
																			<tr class="tituloTablasCeleste">
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="total solicitado">Tot.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="total pendiente">Pend.</td>
																			</tr>
																			<logic:iterate name="vistaDetallePedidoDTO" property="entregas" id="entregaDTO" indexId="numEntrega">
																				<%-- control del estilo para el color de las filas --%>
																				<bean:define id="residuoE" value="${numEntrega % 2}"/>
																				<logic:equal name="residuoE" value="0">
																					<bean:define id="colorBack" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuoE" value="0">
																					<bean:define id="colorBack" value="amarilloClaro10"/>
																				</logic:notEqual>
																				<tr class="${colorBack}">
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="2%">
																						${numEntrega + 1}
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%">
																						<bean:write name="entregaDTO" property="cantidadEntrega"/>
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="11%">
																						<bean:write name="entregaDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="total solicitado">
																						<bean:write name="entregaDTO" property="cantidadDespacho"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="total pendiente">
																						${entregaDTO.cantidadDespacho - entregaDTO.cantidadParcialDespacho}
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="8%">
																						<bean:write name="entregaDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="18%">
																					   <table border="0" width="100%" cellpadding="1" cellspacing="0">
																						  <tr>
																						   <td>
																							   <logic:notEmpty name="entregaDTO" property="divisionDTO">
																							  	 <bean:write name="entregaDTO" property="divisionDTO.nombreDivGeoPol"/>-
																							   </logic:notEmpty>
																							   <bean:write name="entregaDTO" property="direccionEntrega"/>
																						   <td>
																						  </tr>
																						</table>	
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="8%" >
																						<logic:notEmpty name="entregaDTO" property="configuracionContextoDTO">
																							<bean:write name="entregaDTO" property="configuracionContextoDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionContextoDTO">&nbsp;</logic:empty>
																						<logic:equal name="entregaDTO" property="codigoLocalTransito" value="97">
																							<table border="0" width="100%" cellpadding="1" cellspacing="0">
																							  <tr>
																							   <td title="Bodega de Tránsito">(Tráns)</td>
																							  </tr>
																							 </table> 
																						</logic:equal>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="17%">
																						<logic:notEmpty name="entregaDTO" property="configuracionAlcanceDTO">
																							<bean:write name="entregaDTO" property="configuracionAlcanceDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionAlcanceDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="15%">
																						<logic:notEmpty name="entregaDTO" property="configuracionStockDTO">
																							<bean:write name="entregaDTO" property="configuracionStockDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionStockDTO">&nbsp;</logic:empty>
																					</td>
																				</tr>
																			</logic:iterate>
																		</table>
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</logic:notEmpty>
											</logic:equal>
										</logic:iterate>
									</table>
								</td>
							</tr>
						</logic:notEmpty>
					</table>
							<%--</div>--%>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
				</table>
			</div>
		</td>
	</tr>
</table>