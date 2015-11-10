<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define name="sispe.estado.activo" id="estadoActivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
	<bean:define name="ec.com.smx.sic.sispe.tipoArticulo.canasto" id="tipoCanasto"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>	

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
	
	<logic:empty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
		<table border="0" cellpadding="0" cellspacing="0" class="textoRojo12" width="100%">
			<tr><td height="250px" align="center"><b>EL PEDIDO NO TIENE DESCUENTOS</b></td></tr>
		</table>		
	</logic:empty>
	
	<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
	<div id="articulosPorDescuetos" style="width:100%;height:100%;overflow:auto;border-bottom:1px solid #cccccc">
		<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="98%">
			
			<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuentos" indexId="indiceDescuentos">
			<tr ><td height="10px"> </td> </tr>
				<tr>
					<td class="columna_contenido fila_contenido textoRojo10"> 
						
						</b><bean:write name="descuentos" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></b>	
						
						<c:set var="codigoDescuento" value="${descuentos.descuentoDTO.codigoTipoDescuento}-${descuentos.id.codigoReferenciaDescuentoVariable}"/>
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
							<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
								<tr align="left">
								   <td>					
										<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
												<tr>
													<td colspan="2" bgcolor="#F4F5EB">
														<table width="100%" border="0" cellpadding="1" cellspacing="0">
															<tr class="tituloTablas" height="15px">
																<td class="columna_contenido" align="center" title="AUTORIZACION">Aut</td>
																<td class="columna_contenido" align="center">No</td>
																<td class="columna_contenido" align="center">C&oacute;digo barras</td>
																<td class="columna_contenido" align="center">Art&iacute;culo</td>
																<td class="columna_contenido" align="center" title="cantidad total a entregar">Cant.</td>
																<td class="columna_contenido" align="center" title="cantidad reservada en bodega">Cant. Res.</td>
																<td class="columna_contenido" align="center" >Peso Kg.</td>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
																	<c:set value="${columnas + 1}" var="columnas"/>
																	<td class="columna_contenido" align="center" title="cantidad producida hasta el momento">Cant. Prod.</td>
																</logic:equal>
																<td class="columna_contenido" align="center" >V.Unit.</td>
																<td class="columna_contenido" align="center" >Tot. Iva</td>
																<td class="columna_contenido" align="center" >Iva</td>
																<td class="columna_contenido" align="center" >Dscto.</td>
																<td class="columna_contenido" align="center" >Tot. Neto</td>
															</tr>		
															<bean:define id="numRegistro" value="0"/>																
															
															<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
																<logic:greaterThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0">
																	<logic:notEmpty  name="vistaDetallePedidoDTO"  property="npCodigoReferencialDescuento">																
																		<c:set var="numeroLlaves" value="${vistaDetallePedidoDTO.npCodigoReferencialDescuento.size()}"/>
																		<logic:iterate name="vistaDetallePedidoDTO"  property="npCodigoReferencialDescuento" id="llaveActual">
																			<logic:equal name="llaveActual" value="${codigoDescuento}">
																			
																				<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
																				<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
																					<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
																				</logic:equal>
																				<bean:define id="residuo" value="${indiceDetalle % 2}"/>
																				<logic:equal name="residuo" value="0">
																					<bean:define id="colorBack" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuo" value="0">
																					<bean:define id="colorBack" value="grisClaro10"/>
																				</logic:notEqual>
																				<tr class="${colorBack}">
																					<bean:define id="numRegistro" value="${numRegistro + 1}"/>
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
																					<!-- COLUMNA DE AUTORIZACION DE DESCUENTO VARIABLE -->                                                                
																					 <td width="3%" class="columna_contenido fila_contenido" align="center" valign="middle">
																					 	<logic:notEmpty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol"> 
																					 		<c:set var="autorizacion" value="0"/>
																					 		<c:set var="nombreDepartamento" value=""/>                                                                       
																					         <logic:iterate name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutDTO" indexId="numeroAut">                                                                            
																					             <logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO">
																					             	
																					             	<logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.npNombreDepartamento">
																					             		<c:set var="nombreDepartamento" value="-${detalleAutDTO.estadoPedidoAutorizacionDTO.npNombreDepartamento}- "/>
																					             	</logic:notEmpty>
																					       		<c:if test="${codigoDescuentoVariable == detalleAutDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	                                                                                
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.pendiente">
																					                      <c:set var="autorizacion" value="1"/>
																					                  </smx:equal>                                                                            
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
																					                      <c:set var="autorizacion" value="2"/>
																					                  </smx:equal>
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
																					                      <c:set var="autorizacion" value="3"/>
																					                  </smx:equal>
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
																					                      <c:set var="autorizacion" value="4"/>
																					                  </smx:equal>
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.utilizada">	
																					                      <c:set var="autorizacion" value="5"/>
																					                  </smx:equal>
																					                  <smx:equal name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.caducada">	
																					                      <c:set var="autorizacion" value="6"/>
																					                  </smx:equal>
																					              </c:if>
																					             </logic:notEmpty>
																					         </logic:iterate>
																					         <c:if test="${autorizacion == 0}">
																					            &nbsp;
																					         </c:if>                                                                        
																					         <c:if test="${autorizacion == 1}">
																					            <img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Pendiente">
																					         </c:if>
																					         <c:if test="${autorizacion == 2}">								            	
																					            <img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Solicitada">								            	
																					         </c:if>
																					         <c:if test="${autorizacion == 3}">
																					         	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Aprobada">
																					         </c:if>
																					         <c:if test="${autorizacion == 4}">
																			            		<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Rechazada">
																					         </c:if>
																					         <c:if test="${autorizacion == 5}">
																			            		<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Utilizada en otro momento">
																					         </c:if>
																					         <c:if test="${autorizacion == 6}">
																			            		<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de Descuento Variable ${nombreDepartamento}Caducada">
																					         </c:if>
																					     </logic:notEmpty>
																					     <logic:empty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
																					     	&nbsp;
																					     </logic:empty>
																					 </td>
																					<td align="left" class="columna_contenido fila_contenido"><bean:write name="numRegistro"/></td>											
																					<td class="columna_contenido fila_contenido" align="left">
																						<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																							<logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
																								<html:link href="#" onclick="requestAjax('detalleEstadoPedido.do',['informacionOrdenCompra'],{parameters: 'indiceInfoOrdenCompra=${indiceDetalle}'});mostrarModal();" title="detalle de la orden de compra"><bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/></html:link>
																							</logic:notEmpty>
																							<logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
																								<bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
																							</logic:empty>
																						</logic:empty>
																						<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																							<bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
																						</logic:notEmpty>
																					</td>
																					<td class="columna_contenido fila_contenido">
																						<table border="0" width="100%" cellpadding="1" cellspacing="0">
																							<tr>
																								<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial || vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
																									<td align="left">
																										<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
																										<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																											<html:link title="detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</html:link>
																										</logic:empty>
																										<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																											<a href="#" title="detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</a>
																										</logic:notEmpty>
																									</td>
																									<td valign="middle" align="right">
																										<bean:define id="codigoSubClasificacion" name="vistaDetallePedidoDTO" property="codigoSubClasificacion"/>
																										<bean:define id="imgSrc" value="canasto_lleno"/>
																										<c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																											<bean:define id="imgSrc" value="despensa_llena"/>
																										</c:if>
																										<%--<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoDespensa}"><bean:define id="imgSrc" value="despensa_llena"/></logic:equal>--%>
																										
																										<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																											<html:link title="detalle del canasto" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><img src="./images/${imgSrc}.gif" border="0"></html:link>
																										</logic:empty>
																										<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																											<a href="#" title="detalle de la despensa" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><img src="./images/${imgSrc}.gif" border="0"></a>
																										</logic:notEmpty>
																									</td>
																								</c:if>
																								<c:if test="${vistaDetallePedidoDTO.codigoClasificacion != articuloEspecial && vistaDetallePedidoDTO.codigoClasificacion != canastoCatalogo}">
																									<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																									<logic:equal name="pesoVariable" value="${estadoActivo}">
																										<td align="right"><img title="peso variable" src="./images/${imagen}" border="0"></td>
																									</logic:equal>
																								</c:if>
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
																					<td class="columna_contenido fila_contenido" align="right">
																						<logic:equal name="pesoVariable" value="${estadoActivo}">
																							<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
																								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoLocal}"/>
																							</logic:notEmpty>
																							<logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoLocal">
																								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
																							</logic:empty>
																						</logic:equal>
																						<logic:notEqual name="pesoVariable" value="${estadoActivo}">
																							<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
																						</logic:notEqual>
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
																					<td class="columna_contenido fila_contenido" align="right">
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
																					<td align="right" class="columna_contenido fila_contenido">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorPrevioVenta}"/>
																					</td>
																					<td align="center" class="columna_contenido fila_contenido">
																						<logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
																							<html:img page="/images/tick.png" border="0"/>
																						</logic:equal>
																						<logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
																							<html:img page="/images/x.png" border="0"/>
																						</logic:notEqual>
																					</td>
																					<td align="center" class="columna_contenido fila_contenido">&nbsp;
																						<c:if test="${numeroLlaves > 1}">	
																							<!-- PARA EL CASO DE DETALLES CON AUTORIZACIONES-->				
																							<c:if test="${descuentos.descuentoDTO.codigoTipoDescuento == '3'}">
																								<bean:write name="vistaDetallePedidoDTO" property="valorTotalEstadoDescuento"/>
																							</c:if>	
																							<!-- PARA EL CASO DE DETALLES CON DESCUENTOS DE CAJA O MAYORISTAS-->																								
																							<c:if test="${descuentos.id.codigoReferenciaDescuentoVariable == 'CAJ' || descuentos.id.codigoReferenciaDescuentoVariable == 'MAY'}">
																								${vistaDetallePedidoDTO.valorFinalEstadoDescuento - vistaDetallePedidoDTO.valorTotalEstadoDescuento}
																							</c:if>																																													
																						</c:if>
																						<c:if test="${numeroLlaves == 1}">
																							<bean:write name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" />
																						</c:if>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
																					</td>
																				</tr>											
																			</logic:equal>
																		</logic:iterate>
																	</logic:notEmpty >
																</logic:greaterThan>
															</logic:iterate>
														</table>
													</td>
												</tr>
											</logic:notEmpty>
										</table>				 
									</td>
								</tr>
							</table>
						</logic:notEmpty>
					</td>
				</tr>
			</logic:iterate>			
		</table>
		</div>
	</logic:notEmpty>
</logic:notEmpty>