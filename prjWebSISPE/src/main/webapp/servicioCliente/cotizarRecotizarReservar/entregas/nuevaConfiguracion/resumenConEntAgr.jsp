<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<c:set var="formulario" value="cotizarRecotizarReservarForm" />
<c:set var="accion" value="crearCotizacion.do"/>

<!-- insertada desde entregaArticuloLocal.jsp -->
<logic:empty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
	<c:set var="altoDivDetArt" value="400"/>
</logic:empty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
	<c:set var="altoDivDetArt" value="260"/>
</logic:notEmpty>
<bean:define id="stockParcialBodega"><bean:message key="ec.com.smx.sic.sispe.stock.parcialBodega"/></bean:define>
<bean:define id="tipoEntregaParcial"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega.parcial"/></bean:define>
<bean:define id="entregaDireccion"><bean:message key="ec.com.smx.sic.sispe.entrega.direccion"/></bean:define>
<bean:define id="indiceEntrega" value="${0}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" >	
	<tr>
		<td class="fila_titulo" height="29px">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
				<tr>
					<td width="30px" align="right"><img src="images/pedidos24.gif" border="0"></td>
					<td>&nbsp;Detalle de responsables</td>
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
						<div style="border-bottom-style: solid; border-bottom-color: #ebebeb;">
							<table cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="32"></td>
									<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
										<td width="112" class="textoAzul11" align="left">No Pedido:&nbsp;</td>
										<td width="120" align="left">
											<bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/>
										</td>
									</logic:notEmpty>										
									<logic:notEmpty name="cotizarRecotizarReservarForm" property="rucEmpresa" >
										<td width="110" class="textoAzul11" align="left">Raz&oacute;n Social:&nbsp;</td>
										<td align="left">
											<bean:write name="cotizarRecotizarReservarForm" property="nombreEmpresa"/>
										</td>
									</logic:notEmpty>
									<logic:empty name="cotizarRecotizarReservarForm" property="rucEmpresa" >
									<td width="110" class="textoAzul11" align="left">Nombre Cliente:&nbsp;</td>
										<td align="left">
											<bean:write name="cotizarRecotizarReservarForm" property="nombrePersona"/>
									</logic:empty>
									
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td width="32"></td>
								<logic:notEmpty name="cotizarRecotizarReservarForm" property="fechaDespacho">
									<td width="112" class="textoAzul11">Fecha Recepci&oacute;n:</td>
									<td width="150">
										<bean:write name="cotizarRecotizarReservarForm" property="fechaDespacho"/>
									</td>
								</logic:notEmpty>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<logic:empty name="ec.com.smx.sic.sispe.entregasResp">
				<table class="textoNegro11" cellpadding="0" cellspacing="0"
					align="left" width="100%">
						<tr>
							<td width="100%" height="400px"> 
								<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" >
									<tr>						
										<td align="center" class="textoNegro11">
											No existe responsables para el pedido											
										</td>
									</tr>
								</table>
			
							</td>
						</tr>
				</table>
			</logic:empty>
		</td>
	</tr>
</table>
<div id="div_configuracionEntregasAgr" style="width:100%;height:400px;overflow-y:auto;overflow-x:hidden;">
	<table class="textoNegro11" cellpadding="0" cellspacing="0"	align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
		<tr>
			<td>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.entregasResp">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<div id="reserva">
				<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
					
					
					
					<tr><td height="6px"></td></tr>
					<tr>
						<td valign="top">
							<%--<div id="reserva">--%>
							<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedidoAux">
								<table border="0" cellspacing="0" cellpadding="0" width="98%" align="center" style="border-top:#cccccc 1px solid" >
									
									<tr>
										<td colspan="9" >
											<div id="listado_articulos" >
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													
													<bean:define id="contadorCheckTransito" value="0"></bean:define>
													<logic:iterate name="ec.com.smx.sic.sispe.entregasResp" id="entrega" indexId="numDetalle">
														<bean:define id="contadorSeleccion" value="${0}"/>
														<bean:define id="numFila" value="${numDetalle + 1}"/>
														<%-- control del estilo para el color de las filas --%>
														<bean:define id="residuo" value="${numDetalle % 2}"/>
														<c:set var="clase" value="blanco10"/>
														<c:set var="colorBack" value="#ffffff"/>
														<logic:notEqual name="residuo" value="0">
															<c:set var="clase" value="grisClaro10"/>
															<c:set var="colorBack" value="#EBEBEB"/>
														</logic:notEqual>
														<tr>
															<td>
																<table id="table_${numDetalle}" cellspacing="0" cellpadding="0" width="100%" style="border-width: 1px;border: 1px solid; border-color: #7de44a;">
																	<tr id="fila_en_${numDetalle}" >
																		<td width="4%" align="center" height="25"  class="${clase} textoNegro10 columna_contenido_blanco">
																			<div id="desplegar${numDetalle}">
																				<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
																					<a title="Ver Detalle de Entregas" href="#" onClick="plegarA(${numDetalle});">
																						<html:img src="images/plegar.gif" border="0"/>
																					</a>
																				</logic:notEmpty>
																			</div>
																			<div id="plegar${numDetalle}" class="displayNo">
																				<a title="Ver Detalle de Entregas" href="#" onClick="desplegarA(${numDetalle});">
																					<html:img src="images/desplegar.gif" border="0"/>
																				</a>
																			</div>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="center" width="5%"><bean:write name="numFila"/></td>															
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="40%" >
																		Fecha entrega:&nbsp;&nbsp;<bean:write name="entrega" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="51%" id="cantidad_en_${numDetalle}">&nbsp;
																		Direcci&oacute;n entrega:&nbsp;&nbsp;<bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/></td>
			
																	</tr>
																	<tr>
																		<td colspan="9">
																			
																			
																			<div id="marco${numDetalle}">
																				<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
																					<table border="0" width="100%" cellpadding="0" cellspacing="0">
																						<tr>
																							<td width="4%"></td>
																							<td align="left">
																								<table border="0" width="99%" cellspacing="0" cellpadding="0">
																									<tr>
																										<td>
																											<table class="tabla_informacion" border="0" width="100%" cellspacing="0" cellpadding="0">
																												<tr>
																													<td width="13%" align="center" class="tituloTablas columna_contenido" >COD. BARRAS</td>
																													<td width="12%" align="center" class="subtituloTablasEntregaCliente columna_contenido" >ART&Iacute;CULO</td>
																													<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido">RESPONSABLE PEDIDO</td>
																													<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido" >REPONSABLE PRODUCCI&Oacute;N</td>
																													<td class="columna_contenido subtituloTablasEntregaCliente" align="center" width="20%">RESPONSABLE DESPACHO</td>
																													<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido" >REPONSABLE ENTREGA</td>
																																																							
																												</tr>
																											</table>
																										</td>
																									</tr>
																									<tr>
																										<%-- si la colecci&oacute;n de direcciones no est&aacute; vacia --%>
																										<td width="100%" align="left" colspan="6">																								
																											<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="left">
																												<logic:iterate name="entrega" property="entregaPedidoDTO.npDetallePedido" id="detalle" indexId="numEntrega">
																													<bean:define id="residuoE" value="${numEntrega % 2}"/>
																													<c:set var="indiceEntrega" value="${indiceEntrega + 1}"/>
																													<logic:equal name="residuoE" value="0">
																														<bean:define id="colorBack" value="grisClaro10"/>
																													</logic:equal>
																													<logic:notEqual name="residuoE" value="0">
																														<bean:define id="colorBack" value="blanco10"/>
																													</logic:notEqual>
																													<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
																													<tr class="${colorBack}">
																																								
																														<td width="13%" align="left" class="textoNegro9">
																														<bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/>
																														</td>
																														<td width="12%" align="left" class="textoNegro9" title="C&oacute;digo barras: ${articulo.codigoBarrasActivo.id.codigoBarras}">
																														<bean:write name="articulo" property="descripcionArticulo"/>
																														</td>																											
																														<td width="20%" align="center" class="textoNegro9">
																															 <bean:write name="detalle" property="npReponsable.responsablePedido"/>
																														</td>
																														<td width="20%" align="center" class="textoNegro9" >
																															<bean:write name="detalle" property="npReponsable.responsableProduccion"/>	
																														</td>
																														<td width="20%" align="center" class="textoNegro9">
																															 <bean:write name="detalle" property="npReponsable.responsableDespacho"/>
																														</td>
																														
																														<td width="20%" align="center" class="textoNegro9" >
																															<bean:write name="detalle" property="npReponsable.responsableEntrega"/>	
																														</td>
																																																									
																													</tr>
																												</logic:iterate>
																											</table>
																										</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																					</table>
																				</logic:notEmpty>
																			</div>	
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
															<tr><td height="5px" colspan="6"></td></tr>
														</logic:notEmpty>
													</logic:iterate>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</logic:notEmpty>
							<%--</div>--%>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
				</table>
			</div>
		</td>
	</tr>
</table>
			
</logic:notEmpty>
			</td>
		</tr>
	</table>
</div>
