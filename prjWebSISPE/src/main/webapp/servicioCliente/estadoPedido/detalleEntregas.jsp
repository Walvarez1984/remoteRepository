<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<bean:define id="stockParcialBodega"><bean:message key="ec.com.smx.sic.sispe.stock.parcialBodega"/></bean:define>
<bean:define id="tipoEntregaParcial"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega.parcial"/></bean:define>
<bean:define id="entregaDireccion"><bean:message key="ec.com.smx.sic.sispe.entrega.direccion"/></bean:define>
<bean:define id="contextoEntregaDomicilioCD"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.domicilio"/></bean:define>

<bean:define id="indiceEntrega" value="${0}"/>
<div id="div_configuracionEntregasAgr" style="width:100%;height:310px;overflow-y:auto;overflow-x:hidden;">
	<table class="textoNegro11" cellpadding="0" cellspacing="0"	align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
		<tr>
			<td>
			
			<logic:notEmpty name="ec.com.smx.sic.sispe.resumen.entregas">
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<div id="reserva">
				<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
					<tr><td height="6px"></td></tr>
					<tr>
						<td valign="top">
								<table border="0" cellspacing="0" cellpadding="0" width="98%" align="center" style="border-top:#cccccc 1px solid" >
									<tr>
										<td colspan="9" >
											<div id="listado_articulos" >
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													
													<bean:define id="contadorCheckTransito" value="0"></bean:define>
													<logic:iterate name="ec.com.smx.sic.sispe.resumen.entregas" id="entrega" indexId="numDetalle">
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
																		<td width="3%" align="center" height="25"  class="${clase} textoNegro10 columna_contenido_blanco">
																			<div id="desplegar${numDetalle}" class="displayNo">
																				<logic:notEmpty name="entrega" property="npDetallePedido">
																					<a title="Ver detalle de entregas" href="#" onClick="plegarA(${numDetalle});">
																						<html:img src="images/plegar.gif" border="0"/>
																					</a>
																				</logic:notEmpty>
																			</div>
																			<div id="plegar${numDetalle}" >
																				<a title="Ver detalle de entregas" href="#" onClick="desplegarA(${numDetalle});">
																					<html:img src="images/desplegar.gif" border="0"/>
																				</a>
																			</div>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="center" width="3%"><bean:write name="numFila"/></td>															
																		<td class="${clase} textoNegro10 columna_contenido_blanco"  align="left" width="12%" >
																			<table width="100%" cellpadding="0" cellspacing="0">
																				<tr>
																					<td style="font-weight: bold;">Fecha entrega:</td>
																				</tr>
																				<tr>
																					<td><bean:write name="entrega" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
																				</tr>
																			</table>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="11%" style="border-left-width: 1px;border-left-style: solid;border-left-color: #000000; border-right-width: 1px;border-right-style: solid;border-right-color: #000000;">
																			<table width="100%" cellpadding="0" cellspacing="0">
																				<tr >
																					<td style="font-weight: bold;">Fecha despacho:</td>
																				</tr>
																				<tr>
																					<td><bean:write name="entrega" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
																				</tr>
																			</table>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="31%" id="cantidad_en_${numDetalle}" style="border-right-width: 1px;border-right-style: solid;border-right-color: #000000;">
																			<table width="100%" cellpadding="0" cellspacing="0">
																				<tr>
																					<td style="font-weight: bold;"> Direcci&oacute;n entrega:</td>
																				</tr>
																				<tr>
																					<td>
																						
																					   <logic:notEmpty name="entrega" property="entregaPedidoConvenioCol">
																					  		<b> E.DOM 
																							   <logic:iterate name="entrega" property="entregaPedidoConvenioCol" id="entregaPedidoConvenio">
																							 		<bean:write name="entregaPedidoConvenio" property="secuencialConvenio"/>
																							   </logic:iterate>
																						   </b>  
																						    - 
																					   </logic:notEmpty>
																						   
																					   <bean:write name="entrega" property="direccionEntrega"/>
																			   		</td>
																				</tr>
																			</table>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="30%" style="border-right-width: 1px;border-right-style: solid;border-right-color: #000000;">
																			<table width="100%" cellpadding="0" cellspacing="0" >
																				<tr>
																					<td style="font-weight: bold;">Stock entrega:</td>
																				</tr>
																				<tr>
																					<td><bean:write name="entrega" property="configuracionStockDTO.nombreConfiguracion" /></td>
																				</tr>
																			</table>
																		</td>
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="30%" style="border-right-width: 1px;border-right-style: solid;border-right-color: #000000;" >
																		<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
       																	<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
																			<table width="100%" cellpadding="0" cellspacing="0" >
																				<tr>
																					<td style="font-weight: bold;">Res. elabora canasto:</td>
																				</tr>
																				<tr>
																					<td>
																						<c:if test="${entrega.elaCanEsp==entidadLocal}">
																							LOCAL
																						</c:if>
																						<c:if test="${entrega.elaCanEsp==entidadBodega}">
																							BODEGA
																						</c:if>
																					</td>
																				</tr>
																			</table>
																		</td>
<!-- 																	IOnofre. Se agrega la carga de archivo croquis solo si la entrega es a domicilio. -->																	
																		<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="30%" >																			
																			<table width="100%" cellpadding="0" cellspacing="0" >
																				<tr>
																					<td style="font-weight: bold;">Archivo croquis:</td>
																				</tr>
																				<c:if test="${entrega.codigoContextoEntrega == contextoEntregaDomicilioCD}">
																				<tr>
																					<td>
																						<a href="#" onclick="requestAjax('detalleEstadoPedido.do', ['pregunta'], {parameters: 'mostrarPopUpArchCroquis=ok&&indiceEntregaCroquis=${numDetalle}',popWait:true}); mostrarModal();"><img src="./images/adjuntar2.gif" border="0" alt="Adjuntar archivo de croquis"></a>
																					</td>
																				</tr>
																				</c:if>
																			</table>																			
																		</td>
																	</tr>
																	<tr>
																		<td colspan="9">
																			<div id="marco${numDetalle}" class="displayNo">
																				<logic:notEmpty name="entrega" property="npDetallePedido">
																					<table border="0" width="100%" cellpadding="0" cellspacing="0">
																						<tr>
																							<td width="4%"></td>
																							<td align="left">
																								<table border="0" width="99%" cellspacing="0" cellpadding="0" class="tabla_informacion_negro">
<!-- 																									<tr> -->
<!-- 																										<td> -->
<!-- 																											<table class="tabla_informacion" border="0" width="100%" cellspacing="0" cellpadding="0"> -->
																												<tr class="tituloTablasCeleste">
																													<td width="11%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Cod. barras</td>
																													<td width="15%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Art&iacute;culo</td>
																													<td width="5%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Cantidad entrega</td>
																													<td width="5%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Prod. total</td>
																													<td width="5%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Prod. pen.</td>
																													<td width="14%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Contexto entrega</td>
																													<td width="13%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Tipo entrega</td>
																													<td width="8%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Res. pedido</td>
																													<td width="8%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Res. prod.</td>
																													<td width="8%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Res. des.</td>
																													<td width="8%" align="center" class="columna_contenido_der_negro fila_contenido_negro" >Res. ent.</td>
																												</tr>
<!-- 																											</table> -->
<!-- 																										</td> -->
<!-- 																									</tr> -->
<!-- 																									<tr> -->
																										<%-- si la colecci&oacute;n de direcciones no est&aacute; vacia --%>
<!-- 																										<td width="100%" align="left" colspan="6">																								 -->
<!-- 																											<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="95%" align="left"> -->
 																												<logic:iterate name="entrega" property="npDetallePedido" id="detalle" indexId="numEntrega">
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
																														<td width="11%" align="left" class="columna_contenido fila_contenido textoNegro10">
																															<bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/>
																														</td>
																														<td width="15%" align="left" class="columna_contenido fila_contenido textoNegro10" title="C&oacute;digo barras: ${articulo.codigoBarrasActivo.id.codigoBarras}">
																															<bean:write name="articulo" property="descripcionArticulo"/>
																														</td>
																														<td width="5%" align="center" class="columna_contenido fila_contenido textoNegro10">
																															 <bean:write name="detalle" property="npContadorEntrega"/>
																														</td>
																														<td width="5%" align="center" class="columna_contenido fila_contenido textoNegro10">
																															 <bean:write name="detalle" property="npContadorDespacho"/>
																														</td>
																														<td width="5%" align="center" class="columna_contenido fila_contenido textoNegro10">
																															 ${detalle.npContadorDespacho - detalle.npCantidadReservaInicial}
																														</td>
																														<c:if test="${numEntrega==0}">
																															<td rowspan="${entrega.npDetallePedido.size()}" width="14%" align="center" valign="center" class="columna_contenido fila_contenido textoNegro10">
																																 <logic:notEmpty name="entrega" property="configuracionContextoDTO">
																																	<bean:write name="entrega" property="configuracionContextoDTO.nombreConfiguracion"/>
																																</logic:notEmpty>
																																<logic:empty name="entrega" property="configuracionContextoDTO">&nbsp;</logic:empty>
																																<logic:equal name="entrega" property="codigoBodega" value="97">
																																	<table border="0" width="100%" cellpadding="1" cellspacing="0">
																																	  <tr>
																																	   <td title="Bodega de Tránsito">(Tráns)</td>
																																	  </tr>
																																	 </table> 
																																</logic:equal>
																															</td>
																															<td rowspan="${entrega.npDetallePedido.size()}" width="13%" align="center" valign="center" class="columna_contenido fila_contenido textoNegro10">
																																<logic:notEmpty name="entrega" property="configuracionAlcanceDTO">
																																	<bean:write name="entrega" property="configuracionAlcanceDTO.nombreConfiguracion"/>
																																</logic:notEmpty>
																																<logic:empty name="entrega" property="configuracionAlcanceDTO">&nbsp;</logic:empty> 
																															</td>
																																																																																	
																															<td rowspan="${entrega.npDetallePedido.size()}" width="8%" align="center" class="columna_contenido fila_contenido textoNegro10">
																																 <bean:write name="detalle" property="npReponsable.responsablePedido"/>
																															</td>
																														</c:if>	
																														<td width="8%" align="center" class="columna_contenido fila_contenido textoNegro10" >
																															<bean:write name="detalle" property="npReponsable.responsableProduccion"/>	
																														</td>
																														<td width="8%" align="center" class="columna_contenido fila_contenido textoNegro10">
																															 <bean:write name="detalle" property="npReponsable.responsableDespacho"/>
																														</td>
																														<c:if test="${numEntrega==0}">
																															<td rowspan="${entrega.npDetallePedido.size()}" width="8%" align="center" class="columna_contenido fila_contenido textoNegro10" >
																																<bean:write name="detalle" property="npReponsable.responsableEntrega"/>	
																															</td>
																														</c:if>	
																													</tr>
																												</logic:iterate>
<!-- 																											</table> -->
<!-- 																										</td> -->
<!-- 																									</tr> -->
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
														<logic:notEmpty name="entrega" property="npDetallePedido">
															<tr><td height="5px" colspan="6"></td></tr>
														</logic:notEmpty>
													</logic:iterate>
												</table>
											</div>
										</td>
									</tr>
								</table>
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
