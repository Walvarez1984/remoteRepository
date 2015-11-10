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
<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
<bean:define id="idTipoAbonoDescuentosPOS"><bean:message key="ec.com.smx.sic.sispe.tipoAbono.descuentosEspeciales"/></bean:define>
<bean:define id="estadoAnulado"><bean:message key="ec.com.smx.sic.sispe.estadoAnulado"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoCotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoCotizado" id="estadoCotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoRecotizado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoRecotizado" id="estadoRecotizado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoReservado">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoReservado" id="estadoReservado"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.estadoProduccion">
	<bean:define name="ec.com.smx.sic.sispe.pedido.estadoProduccion" id="estadoProduccion"/>
</logic:notEmpty>
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
<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<%-- se definen los c&oacute;digos de las posibles entidades responsables --%>
<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
<bean:define id="codigoAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock"/></bean:define>

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
	<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
		<tr><td height="5px"></td></tr>
		<tr align="left">
		   <td>	
			<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
				<div id="detallePedido" style="width:100%;height:210px;overflow:auto;border-bottom:1px solid #cccccc">
			</logic:empty>	
					<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr>
								<td colspan="2" bgcolor="#F4F5EB">
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr class="tituloTablas" height="15px">
											<td class="columna_contenido" align="center" title="AUTORIZACION">Aut</td>
											<td class="columna_contenido" align="center">&nbsp;</td>
											<td class="columna_contenido" align="center">No</td>
											<td class="columna_contenido" align="center">C&oacute;digo barras</td>
											<td class="columna_contenido" align="center">Art&iacute;culo</td>
											<td class="columna_contenido" align="center" title="cantidad total a entregar">Cant.</td>
											<td class="columna_contenido" align="center" title="cantidad reservada en bodega">Cant. res.</td>
											<td class="columna_contenido" align="center" >Peso KG.</td>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
												<c:set value="${columnas + 1}" var="columnas"/>
												<td class="columna_contenido" align="center" title="cantidad producida hasta el momento">Cant. prod.</td>
											</logic:equal>
											<td class="columna_contenido" align="center" >V. unit.</td>
											<td class="columna_contenido" align="center" >V. unit. IVA</td>
											<td class="columna_contenido" align="center" >Tot. IVA</td>
											<td class="columna_contenido" align="center" >IVA</td>
											<td class="columna_contenido" align="center" >Dscto.</td>
											<%--<td class="columna_contenido" align="center" >V.Unit Neto</td>--%>
											<td class="columna_contenido" align="center" >Tot. neto</td>
										</tr>									
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
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
												<bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
												<c:set var="pesoVariable" value=""/>
												<c:set var="imagen" value=""/>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                          	<c:set var="imagen" value="balanza.gif"/>
                                                    </logic:equal>
                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                                          	<c:set var="imagen" value="pavo.gif"/>
                                                    </logic:notEqual>
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
												            <img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Pendiente">
												         </c:if>
												         <c:if test="${autorizacion == 2}">								            	
												            <img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Solicitada">								            	
												         </c:if>
												         <c:if test="${autorizacion == 3}">
												         	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Aprobada">
												         </c:if>
												         <c:if test="${autorizacion == 4}">
										            		<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Rechazada">
												         </c:if>
												         <c:if test="${autorizacion == 5}">
										            		<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Utilizada en otro momento">
												         </c:if>
												         <c:if test="${autorizacion == 6}">
										            		<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de descuento variable ${nombreDepartamento}Caducada">
												         </c:if>
												     </logic:notEmpty>
												     <logic:empty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
												     	&nbsp;
												     </logic:empty>
												 </td>
												 
												 
												<td align="left" class="columna_contenido fila_contenido">
													<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
														<div id="desplegar${indiceDetalle}" >
															<a title="ver entregas" href="#" onClick="mostrar(${indiceDetalle},'marco','plegar','desplegar');">
																<html:img src="images/desplegar.gif" border="0"/>
															</a>
														</div>
														<div id="plegar${indiceDetalle}" class="displayNo">
															<a title="ocultar entregas" href="#" onClick="ocultar(${indiceDetalle},'marco','plegar','desplegar');">
																<html:img src="images/plegar.gif" border="0"/>
															</a>
														</div>
													</logic:notEmpty>
													<logic:empty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">&nbsp;</logic:empty>
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
															<c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo==articuloObsoleto}">
																<td>
																	<html:link href="#"  onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'popUpObsoleto=ok', evalScripts:true})">
		                                                            	<img src="images/obsoleto.png" border="0"  >
		                                                            </html:link>															
																</td>
															</c:if>
															<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}"> --%>
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
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<html:link title="detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><img src="./images/${imgSrc}.gif" border="0"></html:link>
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<a href="#" title="detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><img src="./images/${imgSrc}.gif" border="0"></a>
																	</logic:notEmpty>
																	
																	<%--<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoDespensa}"><bean:define id="imgSrc" value="despensa_llena"/></logic:equal> 
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<html:link title="detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><img src="./images/${imgSrc}.gif" border="0"></html:link>
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<a href="#" title="detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><img src="./images/${imgSrc}.gif" border="0"></a>
																	</logic:notEmpty>--%>
																</td>
															</c:if>
															<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo != tipoCanasto && vistaDetallePedidoDTO.codigoTipoArticulo != tipoDespensa}"> --%>
															<c:if test="${vistaDetallePedidoDTO.codigoClasificacion != articuloEspecial && vistaDetallePedidoDTO.codigoClasificacion != canastoCatalogo}">
																<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																<logic:equal name="pesoVariable" value="${estadoActivo}">
																	<td align="right">
																	<logic:equal name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
				                                                       	<img title="peso variable/unidad manejo" src="./images/${imagen}" border="0">
				                                                    </logic:equal>
				                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
				                                                       	<img title="peso variable" src="./images/${imagen}" border="0">
				                                                    </logic:notEqual>
																
																</td>
																</logic:equal>
															</c:if>
															<td width="2px">&nbsp;</td>
														</tr>
													</table>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="cantidad total a entregar">
													<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">														
														<c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo==articuloObsoleto}">
	                                                        <html:link href="#" title="Stock obsoleto" style="text-decoration:none; cursor: default;">
	                                                        	<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
	                                                        </html:link>
                                                        </c:if>  
                                                        <c:if test="${vistaDetallePedidoDTO.articuloDTO.claseArticulo!=articuloObsoleto}">
                                                        	<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
                                                        </c:if>  
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="cantidad reservada en bodega">
				                                	<table>
				                                    	<tr>
															<td width="50%">
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
				                                            <td width="50%">
							                                	<logic:notEmpty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">
							                                      	<c:set var="autorizacionStock" value="0"/>     
							                                      	<c:set var="estadoAutStockDetalle" value="0"/>                                                                  
							                                        <logic:iterate name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutStockDTO" indexId="numeroAut">                                                                            
							                                         	<logic:notEmpty name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO">
								                                     		<c:if test="${codigoAutorizacionStock == detalleAutStockDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">
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
							                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de stock aprobada">
							                                        </c:if>
							                                        <c:if test="${autorizacionStock == 2}">
							                                        	<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de stock rechazada">
							                                        </c:if>
																	<c:if test="${autorizacionStock == 3}">
							                                        	<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock solicitada">			                 
							                                        </c:if>
																	<c:if test="${autorizacionStock == 4}">
																		<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock gestionada">
																	</c:if>																		
							                                   	</logic:notEmpty>
							                                    <logic:empty name="vistaDetallePedidoDTO" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
							                                           &nbsp;
							                                   	</logic:empty>
				                                        	</td>
				                                    	</tr>
				                             		</table>
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
												<%--
												<td class="columna_contenido fila_contenido" align="right">
													<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
														<logic:greaterThan name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" value="0">
															(<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.unidadManejo"/>)
															<bean:write name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" formatKey="formatos.numeros"/>
														</logic:greaterThan>
														<logic:lessEqual name="vistaDetallePedidoDTO" property="valorCajaIVAEstado" value="0">
															<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
														</logic:lessEqual>
													</logic:equal>
													<logic:notEqual name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
														<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
													</logic:notEqual>
												</td>
												--%>
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
													<logic:greaterThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
													<logic:lessThan name="vistaDetallePedidoDTO" property="valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
												</td>
												<%--<td align="right" class="columna_contenido fila_contenido">
													<bean:write name="vistaDetallePedidoDTO" property="valorUnitarioPOS" formatKey="formatos.numeros"/>
												</td>--%>
												<td align="right" class="columna_contenido fila_contenido">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
												</td>
											</tr>
											<%-- MUESTRA EL DETALLE DE LAS ENTREGAS --%>
											<logic:notEmpty name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol">
												<tr>
													<td colspan="14" class="columna_contenido" align="center">
														<bean:define name="vistaDetallePedidoDTO" id="configuracionDTO"/>
														<table cellpadding="1" cellspacing="0" width="100%">
															<tr>
																<td>
																	<div id="marco${indiceDetalle}" class="displayNo">
																		<table width="95%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion_negro ">
																			<tr class="tituloTablasCeleste">
																				<td class="columna_contenido_der_negro fila_contenido_negro" align="center" width="2%" rowspan="2">No.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="5%" rowspan="2">Cant. entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="15%" rowspan="2">F. entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" colspan="2" title="datos de producci&oacute;n">Producci&oacute;n</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="15%" rowspan="2">F. despacho</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="18%" rowspan="2">Destino entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" rowspan="2">Contexto entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="17%" rowspan="2">Tipo entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="15%" rowspan="2">Stock entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="10%" rowspan="2">Res. ped.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="10%" rowspan="2">Res. prod.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="10%" rowspan="2">Res. des.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="10%" rowspan="2">Res. ent.</td>
																			</tr>
																			<tr class="tituloTablasCeleste">
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="total solicitado">Tot.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="total pendiente">Pend.</td>
																			</tr>
																			<logic:iterate name="vistaDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
																				<%-- control del estilo para el color de las filas --%>
																				<bean:define id="residuoE" value="${numEntrega % 2}"/>
																				<logic:equal name="residuoE" value="0">
																					<bean:define id="colorBack" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuoE" value="0">
																					<bean:define id="colorBack" value="amarilloClaro10"/>
																				</logic:notEqual>
																				<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO">
																					<bean:define id="entregaPedidoDTO" property="entregaPedidoDTO" name="entregaDetallePedidoDTO"></bean:define>
																				</logic:notEmpty>																				
																																				
																				<tr class="${colorBack}">
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="2%">
																						${numEntrega + 1}
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%">
																						<bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/>
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="15%">
																						<bean:write name="entregaPedidoDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="total solicitado">
																						<bean:write name="entregaDetallePedidoDTO" property="cantidadDespacho"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="total pendiente">
																						${entregaDetallePedidoDTO.cantidadDespacho - entregaDetallePedidoDTO.cantidadParcialDespacho}
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="15%">
																						<bean:write name="entregaPedidoDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="18%">
																					   <table border="0" width="100%" cellpadding="1" cellspacing="0">
																						  <tr>
																						   <td>
																							   <logic:notEmpty name="entregaPedidoDTO" property="divisionGeoPoliticaDTO">
																							  	 <bean:write name="entregaPedidoDTO" property="divisionGeoPoliticaDTO.nombreDivGeoPol"/>
																							  	  - 
																							   </logic:notEmpty>
																							   <!-- Mostrar Convenio vinculado a la entrega a domicilio desde local -->
																							   <logic:notEmpty name="entregaPedidoDTO" property="entregaPedidoConvenioCol">
																							  		<b> E.DOM 
																									   <logic:iterate name="entregaPedidoDTO" property="entregaPedidoConvenioCol" id="entregaPedidoConvenio">
																									 		<bean:write name="entregaPedidoConvenio" property="secuencialConvenio"/>
																									   </logic:iterate>
																								   </b>
																								    - 
																							   </logic:notEmpty>
																							   <bean:write name="entregaPedidoDTO" property="direccionEntrega"/>
																						   <td>
																						  </tr>
																						</table>	
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="8%" >
																						<logic:notEmpty name="entregaPedidoDTO" property="configuracionContextoDTO">
																							<bean:write name="entregaPedidoDTO" property="configuracionContextoDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="configuracionContextoDTO">&nbsp;</logic:empty>
																						<logic:equal name="entregaPedidoDTO" property="codigoBodega" value="97">
																							<table border="0" width="100%" cellpadding="1" cellspacing="0">
																							  <tr>
																							   <td title="Bodega de tránsito">(Tráns)</td>
																							  </tr>
																							 </table> 
																						</logic:equal>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="17%">
																						<logic:notEmpty name="entregaPedidoDTO" property="configuracionAlcanceDTO">
																							<bean:write name="entregaPedidoDTO" property="configuracionAlcanceDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="configuracionAlcanceDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="configuracionStockDTO">
																							<bean:write name="entregaPedidoDTO" property="configuracionStockDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="configuracionStockDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="configuracionResPedDTO">
																							<bean:write name="entregaPedidoDTO" property="configuracionResPedDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="configuracionStockDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="npResProduccion">
																							<bean:write name="entregaPedidoDTO" property="npResProduccion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="npResProduccion">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="npResDespacho">
																							<bean:write name="entregaPedidoDTO" property="npResDespacho"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="npResDespacho">&nbsp;</logic:empty>
																					</td>
																					<%--<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="bodegaDTO">
																							<bean:write name="entregaPedidoDTO" property="bodegaDTO.descripcionBodega"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="bodegaDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="bodegaDTO">
																							<bean:write name="entregaPedidoDTO" property="bodegaDTO.descripcionBodega"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="bodegaDTO">&nbsp;</logic:empty>
																					</td>--%>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="10%">
																						<logic:notEmpty name="entregaPedidoDTO" property="configuracionResEntDTO">
																							<bean:write name="entregaPedidoDTO" property="configuracionResEntDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaPedidoDTO" property="configuracionResEntDTO">&nbsp;</logic:empty>
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
										</logic:iterate>
									</table>
								</td>
							</tr>
						</logic:notEmpty>
					</table>
				<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
				 </div>
				</logic:empty>	
			</td>
		</tr>
		<logic:empty name="ec.com.smx.sic.sispe.NoMostrarTotales">
			<tr>
				<td colspan="14">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td>
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos">
									<table cellpadding="0" cellspacing="0" width="100%">
										<tr> 										
											<td align="left" class="textoAzul11" colspan="2"><b>&nbsp;&nbsp;Detalle de los descuentos</b></td>												
										</tr>
										<tr>
											<td width="5px"></td>
											<td align="left">
												<table border="0" cellspacing="0" cellpadding="0" class="tabla_informacion" width="70%">
													<tr class="tituloTablas" align="left">
														<td align="center" width="70%">DESCRIPCI&Oacute;N</td>
														<%--<td class="columna_contenido" align="center">V.TOTAL</td>--%>
														<td class="columna_contenido" align="center" width="8%">%</td>
														<td class="columna_contenido" align="center" width="17%">DESCUENTO</td>
														<td width="5%" class="blanco">&nbsp;</td>
													</tr>
													<c:set var="totalDescuento" value="0"/>	
													<tr>
														<td colspan="4">
															<div style="width:100%;height:65px;overflow-x:hidden;overflow-y:auto;" id="div_D">
															<table border="0" cellspacing="0" cellpadding="0" width="100%">
																<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="descuentosEstadosPedidos" id="descuento" indexId="numDescuento">														
																	<%-- control del estilo para el color de las filas --%>
																	<bean:define id="residuo" value="${numDescuento % 2}"/>
																	<logic:equal name="residuo" value="0">
																		<bean:define id="colorBack" value="blanco10"/>
																	</logic:equal>
																	<logic:notEqual name="residuo" value="0">
																		<bean:define id="colorBack" value="grisClaro10"/>
																	</logic:notEqual>
																		<tr class="${colorBack} textoNegro10">														
																			<td align="center" width="70%" class="columna_contenido fila_contenido"><b><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></b></td>
																			<%--<td align="right" class="columna_contenido fila_contenido"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/></td>--%>
																			<td align="right" width="8%" class="columna_contenido fila_contenido">
																				<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																				</logic:greaterThan>
																				<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
																			</td>
																			<td align="right" width="17%" class="columna_contenido fila_contenido columna_contenido_der"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
																			<td width="5%" class="blanco">&nbsp;</td>	
																		</tr>
																	<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
																</logic:iterate>															
															</table>
															</div>
														</td>
													</tr>
													<tr>
														<td class="textoRojo10 fila_contenido_sup" align="right" colspan="2"><b>TOTAL:</b></td>
														<%--<td class="textoAzul10 columna_contenido" align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%</b></td>
														<td class="textoAzul10 columna_contenido " align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descuentoTotalPedido" formatKey="formatos.numeros"/></b></td>--%>
														<td class="textoAzul10 columna_contenido fila_contenido_sup columna_contenido_der " align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td colspan="2" height="5px"></td></tr>
									</table>
								</logic:notEmpty>
							</td>

							<!-- nueva columna descuentos POS -->
							<bean:define id="idVistaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="id"></bean:define>
							<c:if test="${idVistaPedido.codigoEstado != estadoAnulado }">
							<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="abonosPedidos">
							<logic:iterate id="abono" name="ec.com.smx.sic.sispe.vistaPedido" property="abonosPedidos">
								<c:if test="${idTipoAbonoDescuentosPOS == abono.codigoTipoAbono }">
									<c:set var="totalDescuentoPos" value="${totalDescuentoPos + abono.valorAbono}" />
									<c:set var="sizeCol" value="${sizeCol + 1}"></c:set>
								</c:if>
							</logic:iterate>
							</logic:notEmpty>
							<c:if test="${sizeCol > 0 }">
								<td>
									<table cellpadding="0" cellspacing="0" width="100%">
										<tr>
											<td class="textoAzul11" colspan="2"><b>Detalle descuentos POS</b></td>
										</tr>
										<tr>
											<td width="5px"></td>
											<td>
												<table border="0" cellspacing="0" cellpadding="2" class="tabla_informacion">
													<tr class="tituloTablas" align="left">
														<td align="center">DESCRIPCI&Oacute;N</td>
														<td class="columna_contenido" align="center">DESCUENTO</td>
													</tr>
													<tr class="${colorBack} textoNegro10">
														<td align="center" class="columna_contenido fila_contenido">
															<b>DESCUENTOS POS</b>
														</td>
														<td align="right" class="columna_contenido fila_contenido">
															<c:if test="${totalDescuentoPos < 0}">
																<c:set var="positivoTotalDescuentoPos" value="${totalDescuentoPos * (-1) }" />
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${positivoTotalDescuentoPos}"/>
															</c:if>
															<c:if test="${totalDescuentoPos > 0}">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuentoPos}"/>
															</c:if>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</c:if>
							</c:if>
							<!-- fin nueva columna descuentos POS -->

							<td bordercolor="#ffffff">
								<table border="0" align="right" cellspacing="0" cellpadding="0">
									<tr height="14">
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
									<!--   <tr><td colspan="2" align="right">------------------------</td></tr> -->
									<tr height="14">
										<td class="textoRojo11" align="right">SUBTOTAL NETO:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<bean:define id="subTotalNetoBruto" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNetoBruto"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNetoBruto}"/>
										</td>
									</tr>
									<tr height="14">
										<td class="textoAzul10" align="right">TARIFA 0%:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<bean:define id="subTotalNoAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalNoAplicaIVA"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalNoAplicaIVA}"/>
										</td>
									</tr>
									<tr height="14">
										<td class="textoAzul10" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<bean:define id="subTotalAplicaIVA" name="ec.com.smx.sic.sispe.vistaPedido" property="subTotalAplicaIVA"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotalAplicaIVA}"/>
										</td>
									</tr>
									<tr height="14">
										<td class="textoAzul10" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<bean:define id="ivaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="ivaPedido"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ivaPedido}"/>
										</td>
									</tr>
									<tr>
										<td class="textoAzul10" align="right">COSTO FLETE:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<bean:define id="valorCostoEntregaPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="valorCostoEntregaPedido"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorCostoEntregaPedido}"/>
										</td>
									</tr>
									<tr><td colspan="2" align="right">------------------------</td></tr>
									<tr height="14">
										<td class="textoRojo11" align="right"><b>TOTAL:&nbsp;</b></td>
										<td class="textoNegro11" align="right">
											<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.vistaPedido" property="totalPedido"/>
											<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/></b>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</logic:empty>	  
	</table>
</logic:notEmpty>