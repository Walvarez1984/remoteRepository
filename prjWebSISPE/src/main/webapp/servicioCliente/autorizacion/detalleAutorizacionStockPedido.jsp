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
<bean:define id="codigoAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock"/></bean:define>

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
											<td class="columna_contenido" align="center" >Cant. Res.</td>
											<td class="columna_contenido" align="center" >Estado autorizaci&oacute;n</td>
										</tr>	
										<bean:define id="numRegistro" value="0"/>								
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<logic:notEmpty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">
											<logic:iterate name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutStockDTO" indexId="numeroAut">                                                                            
							                    <c:set var="autorizacionStock" value="0"/>
							                    <logic:notEmpty name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO">
								               		<c:if test="${codigoAutorizacionStock == detalleAutStockDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	
								               			<c:set var="autorizacionStock" value="1"/>   
									            	</c:if>
								            	</logic:notEmpty>
								            </logic:iterate>												
											<c:if test="${autorizacionStock == 1}">
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
												<!-- COLUMNA DE AUTORIZACION DE STOCK-->                                                                
												<td width="3%" class="columna_contenido fila_contenido" align="center" valign="middle">
												 	<logic:notEmpty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">
							                          	<c:set var="autorizacionStock" value="0"/>
							                          	<c:set var="estadoAutStockDetalle" value="0"/>                                                                    
				                                        <logic:iterate name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutStockDTO" indexId="numeroAut">                                                                            
				                                         	<logic:notEmpty name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO">																				
					                                        	<c:if test="${codigoAutorizacionStock == detalleAutStockDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">								                                       
					                                        		<c:set var="estadoAutorizacionStock" value="${detalleAutStockDTO.estadoPedidoAutorizacionDTO.autorizacionDTO.estadoActualAutorizacion}"/>
																	<smx:equal name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
																		<c:set var="autorizacionStock" value="1"/>	
																	</smx:equal>
																	<smx:equal name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
																		<c:set var="autorizacionStock" value="2"/>																					
																	</smx:equal>
																	<smx:equal name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
																		<c:set var="autorizacionStock" value="3"/>																					
																	</smx:equal>
																	<smx:equal name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO.estado" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.gestionada">	
																		<c:set var="autorizacionStock" value="4"/>
																	</smx:equal>
																	<c:if test="${autorizacionStock != 1}">
                                                                      		<c:set var="estadoAutStockDetalle" value="${autorizacionStock}"/>
                                                                    </c:if> 
					                                     		</c:if>
				                                        	</logic:notEmpty>
				                                    	</logic:iterate>
				                                    	<c:if test="${estadoAutStockDetalle != 0}">
                                                       		<c:set var="autorizacionStock" value="${estadoAutStockDetalle}"/>
                                                          	</c:if>
				                                        <c:if test="${autorizacionStock == 0}">
				                                        	&nbsp;
				                                        </c:if>                                                                        
				                                        <c:if test="${autorizacionStock == 1}">
				                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de Stock Aprobada">
				                                        </c:if>																	
				                                        <c:if test="${autorizacionStock == 2}">
				                                        	<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de Stock Rechazada">
				                                        </c:if>		
														<c:if test="${autorizacionStock == 3}">
				                                        	<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de Stock Solicitada">			                 
				                                        </c:if>
														<c:if test="${autorizacionStock == 4}">
															<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock gestionada">
														</c:if>					                                        
				                                   	</logic:notEmpty>
							                        <logic:empty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
							                       		&nbsp;
							                     	</logic:empty>
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
												<td class="columna_contenido fila_contenido" align="center">
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
												<td class="columna_contenido fila_contenido" align="center" >
													&nbsp;${estadoAutorizacionStock}
												</td>																												
											</tr>
											</c:if>
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