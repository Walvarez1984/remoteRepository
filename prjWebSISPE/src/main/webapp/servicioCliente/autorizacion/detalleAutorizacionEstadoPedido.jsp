<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
	<div id="detallePedido" style="width:100%;height:100%;overflow:auto;border-bottom:1px solid #cccccc">
		<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="98%">
			<tr>
			   <td>	
					<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr align="left" class="fila_titulo">
								<td colspan="2" class="textoNegro11" height="20px"><b>Detalle del pedido:</b></td>
							</tr>
							<tr>
								<td colspan="2" bgcolor="#F4F5EB">
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr class="tituloTablas" height="15px">
											<td class="columna_contenido" align="center">Aut.</td>
											<td class="columna_contenido" align="center">No</td>
											<td class="columna_contenido" align="center">C&oacute;digo barras</td>
											<td class="columna_contenido" align="center">Art&iacute;culo</td>
											<td class="columna_contenido" align="center" title="cantidad total a entregar">Cantidad</td>
											<td class="columna_contenido" align="center" >Estado autorizaci&oacute;n</td>
											<td class="columna_contenido" align="center" >% Solicitado</td>
											<td class="columna_contenido" align="center" >% Aprobado</td>
											<td class="columna_contenido" align="center" >D&iacute;as trans.</td>
											<td class="columna_contenido" align="center" >Gestionado por</td>
										</tr>	
										<bean:define id="numRegistro" value="0"/>								
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<logic:notEmpty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">											
												<c:set var="autorizacion" value="0"/>
												<c:set var="nombreDepartamento" value=""/>       
											 <logic:iterate name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutDTO" indexId="numeroAut">
											 <!--SOLO PARA EL CASO DE AUTORIZACIONES DE DESCUENTO VARIABLE -->
											<c:if test="${codigoDescuentoVariable == detalleAutDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	
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
														 <logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO">												             	
															<logic:notEmpty name="detalleAutDTO" property="estadoPedidoAutorizacionDTO.npNombreDepartamento">
																<c:set var="nombreDepartamento" value="-${detalleAutDTO.estadoPedidoAutorizacionDTO.npNombreDepartamento}- "/>
															</logic:notEmpty>																																			
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
														 </logic:notEmpty>
														   
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
													 </td>
													<td align="center" class="columna_contenido fila_contenido"><bean:write name="numRegistro"/></td>											
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
																			<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
																		</logic:empty>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																			<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
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
																			<img src="./images/${imgSrc}.gif" border="0">
																		</logic:empty>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																			<img src="./images/${imgSrc}.gif" border="0">
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
													<td class="columna_contenido fila_contenido" align="center" >
														&nbsp;<bean:write name="vistaDetallePedidoDTO" property="npEstadoAutorizacion"/>
													</td>
													<td class="columna_contenido fila_contenido" align="center">
														&nbsp;<bean:write name="vistaDetallePedidoDTO" property="npPorcentajeDctoSolicitado"/>
													</td>
													<td class="columna_contenido fila_contenido" align="center" >
														&nbsp;<bean:write name="vistaDetallePedidoDTO" property="npPorcentajeDctoAprobado"/>
													</td>
													<td class="columna_contenido fila_contenido" align="center" >
														&nbsp;<bean:write name="vistaDetallePedidoDTO" property="npFechaProcAutorizacion"/>
													</td>	
													<!-- username y nombre completo del autorizador -->
													<c:set var="nombreAutorizador" value="" />
													<c:set var="usuarioAutorizador" value="" />
													<logic:notEmpty name="vistaDetallePedidoDTO" property="npUsuarioAutorizadorDTO">
														<c:set var="usuarioAutorizador" value="${vistaDetallePedidoDTO.npUsuarioAutorizadorDTO.userName}" />													
														<c:set var="nombreAutorizador" value="${vistaDetallePedidoDTO.npUsuarioAutorizadorDTO.userCompleteName}" />
													</logic:notEmpty>
													<td class="columna_contenido fila_contenido" align="center" title ="${nombreAutorizador}">
														&nbsp;${usuarioAutorizador}																											
													</td>																												
												</tr>
											</c:if>
											</logic:iterate>
											</logic:notEmpty>
										</logic:iterate>
									</table>
								</td>
							</tr>
						</logic:notEmpty>
					</table>
				</td>
			</tr>
		</table>
	</div>
</logic:notEmpty>