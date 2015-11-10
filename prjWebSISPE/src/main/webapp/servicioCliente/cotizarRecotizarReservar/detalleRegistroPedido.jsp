<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
<bean:define id="codigoAutorizacionStock"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.autorizacion.stock"/></bean:define>


<%-- lista de definiciones para las acciones --%>
<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>

<c:set var="imagen" value="cotizacion.gif"/>
<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
</logic:equal>

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<bean:define id="indiceEntrega" value="${0}"/>
							
<table border="0" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
	<tr>
		<td colspan="2">
			<table border="0" align="left" cellspacing="0" cellpadding="1" width="98%" class="tabla_informacion">
				<tr class="tituloTablas">
					<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
						<td class="columna_contenido" align="center" width="2%">&nbsp;</td>
					</logic:equal>
					<td width="3%" class="columna_contenido" align="center" rowspan="2" title="AUTORIZACION">Aut</td>
                    <td width="3%" class="columna_contenido" align="center" rowspan="2">No</td>
					<td width="9%" class="columna_contenido" align="center" rowspan="2">C&oacute;digo barras</td>
					<td width="23%" class="columna_contenido" align="center" rowspan="2">Art&iacute;culo</td>
					<td width="5%" align="center" class="columna_contenido" rowspan="2">Cant.</td>
					<td width="6%" align="center" class="columna_contenido" rowspan="2">Stock</td>
					<td width="6%" align="center" class="columna_contenido" rowspan="2">Peso KG.</td>
					<td width="5%" align="center" class="columna_contenido" rowspan="2">V. unit.</td>
					<td width="5%" align="center" class="columna_contenido" rowspan="2">V. unit. IVA</td>
					<td width="7%" align="center" class="columna_contenido" rowspan="2">Tot. IVA</td>
					<td width="2%" align="center" class="columna_contenido" rowspan="2">IVA</td>
					<td width="3%" align="center" class="columna_contenido" rowspan="2" title="descuento total: por caja + descuento aplicado">Desc</td>
					<%--<td width="5%" align="center" class="columna_contenido" rowspan="2">V.Unit. Neto.</td>--%>
					<td width="7%" align="center" class="columna_contenido" rowspan="2">Tot. neto</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<div id="detallePedido" style="width:100%;height:150px;overflow:auto;border-bottom:1px solid #cccccc">
				<table border="0" width="98%" cellpadding="1" cellspacing="0" align="left" class="tabla_informacion">
					<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
						<logic:iterate name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="vistaDetallesPedidosReporte" id="detalle" indexId="indice">
							<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
							<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
								<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
							</logic:equal>
							<bean:define id="numFila" value="${indice + 1}"/>
							<%-- control del estilo para el color de las filas --%>
							<bean:define id="residuo" value="${indice % 2}"/>
							<logic:equal name="residuo" value="0">
								<bean:define id="colorBack" value="blanco10"/>
							</logic:equal>
							<logic:notEqual name="residuo" value="0">
								<bean:define id="colorBack" value="grisClaro10"/>
							</logic:notEqual>
							<c:set var="pesoVariable" value=""/>
							<c:set var="imagen" value=""/>
							<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
								<c:set var="pesoVariable" value="${estadoActivo}"/>
								<logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                	<c:set var="imagen" value="balanza.gif"/>
                                </logic:equal>
                                <logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
                                	<c:set var="imagen" value="pavo.gif"/>
                                </logic:notEqual>
							</logic:equal>
							<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
								<c:set var="pesoVariable" value="${estadoActivo}"/>
								<c:set var="imagen" value="balanza.gif"/>
							</logic:equal>
							<tr class="${colorBack}">
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
									<td class="fila_contenido" width="2%" align="center">
										<logic:notEmpty name="detalle" property="entregaDetallePedidoCol">
											<div id="desplegar${indice}" >
												<a title="Ver entregas" href="#" onClick="mostrar(${indice},'marco','plegar','desplegar');">
													<html:img src="images/desplegar.gif" border="0"/>
												</a>
											</div>
											<div id="plegar${indice}" class="displayNo">
												<a title="Ver detalle del pedido..." href="#" onClick="ocultar(${indice},'marco','plegar','desplegar');">
													<html:img src="images/plegar.gif" border="0"/>
												</a>
											</div>
										</logic:notEmpty>
										<logic:empty name="detalle" property="entregaDetallePedidoCol">
											&nbsp;
										</logic:empty>
									</td>									
								</logic:equal>
								<!-- COLUMNA DE AUTORIZACION DE DESCUENTO VARIABLE -->                                                                
								 <td width="3%" class="columna_contenido fila_contenido" align="center" valign="middle">
								 	<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol"> 
								 		<c:set var="autorizacion" value="0"/>
								 		<c:set var="nombreDepartamento" value=""/>                                                                       
								         <logic:iterate name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutDTO" indexId="numeroAut">                                                                            
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
								     <logic:empty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
								     	&nbsp;
								     </logic:empty>
								 </td>
                                <td width="3%" align="center" class="fila_contenido columna_contenido"><bean:write name="numFila"/></td>
								<td width="9%" align="center" class="fila_contenido columna_contenido"><bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
								<td width="23%" align="left" class="fila_contenido columna_contenido">
									<table cellpadding="0" cellspacing="0" width="100%">
										<tr>
											<td valign="middle" align="left">											
												<c:if test="${detalle.articuloDTO.claseArticulo==articuloObsoleto}">
													<html:link href="#"  onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'popUpObsoleto=ok', evalScripts:true})">
		                                            	<img src="images/obsoleto.png" border="0"  >
		                                        	</html:link>
												</c:if>
												<logic:notEmpty name="detalle" property="articuloDTO.npNuevoCodigoClasificacion">
													<img title="canasto modificado" src="images/estrella.gif" border="0">
												</logic:notEmpty>
												
													<bean:define id="codigoSubClasificacion" name="detalle" property="articuloDTO.codigoSubClasificacion"/>
	                                                                    			
		                                            <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
													  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
														<bean:define id="descripcion" value="detalle de la despensa"/>
													</c:if>
													<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
													  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
		                                                <bean:define id="descripcion" value="detalle del canasto"/>
													</c:if>
												
												<%--<logic:equal name="detalle" property="articuloDTO.codigoTipoArticulo" value="${tipoDespensa}">
													<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
													<bean:define id="descripcion" value="detalle de la despensa"/>
												</logic:equal>
												<logic:equal name="detalle" property="articuloDTO.codigoTipoArticulo" value="${tipoCanasto}">
													<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
													<bean:define id="descripcion" value="detalle del canasto"/>
												</logic:equal> --%>
												<bean:write name="detalle" property="articuloDTO.descripcionArticulo"/>,&nbsp;${unidadManejo}
											</td>
											<%--<c:if test="${detalle.articuloDTO.codigoTipoArticulo == tipoCanasto || detalle.articuloDTO.codigoTipoArticulo == tipoDespensa}">--%>
											<c:if test="${detalle.articuloDTO.codigoClasificacion == articuloEspecial|| detalle.articuloDTO.codigoClasificacion == canastoCatalogo}">
												<td align="right">
													<img title="${descripcion}" src="${pathImagenArticulo}" border="0"/>
												</td>
											</c:if>
											<%--<c:if test="${detalle.articuloDTO.codigoTipoArticulo != tipoCanasto && detalle.articuloDTO.codigoTipoArticulo != tipoDespensa}"> --%>
											<c:if test="${detalle.articuloDTO.codigoClasificacion != articuloEspecial && detalle.articuloDTO.codigoClasificacion != canastoCatalogo}">
											<td align="right">
												<logic:equal name="pesoVariable" value="${estadoActivo}">
													<logic:equal name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
		                                             	<img title="peso variable/unidad Manejo" src="images/${imagen}" border="0">
		                                            </logic:equal>
		                                            <logic:notEqual name="detalle" property="articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
		                                                <img title="peso variable" src="images/${imagen}" border="0">
		                                            </logic:notEqual> 
		                                        </logic:equal> 
											</td>
											</c:if>
											<td width="2px">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="5%" align="center" class="fila_contenido columna_contenido">
									<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
										<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
									</logic:equal>
									<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
										<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
									</logic:notEqual>
								</td>
								<td width="6%" class="columna_contenido fila_contenido" align="center">
                                	<table>
                                    	<tr>
											<td width="50%">
													<logic:notEqual name="detalle" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<logic:notEmpty name="detalle" property="cantidadReservarSIC">
															<bean:write name="detalle" property="cantidadReservarSIC"/>
														</logic:notEmpty>
														<logic:empty name="detalle" property="cantidadReservarSIC">&nbsp;</logic:empty>
													</logic:notEqual>
													<logic:equal name="detalle" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
											</td>                                  
                                            <td width="50%">
			                                	<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol"> 
			                                      	<c:set var="autorizacionStock" value="0"/>
			                                      	<c:set var="estadoAutStockDetalle" value="0"/>
			                                      	                                                                  
			                                        <logic:iterate name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol" id="detalleAutStockDTO" indexId="numeroAut">                                                                            
			                                         	<logic:notEmpty name="detalleAutStockDTO" property="estadoPedidoAutorizacionDTO">
				                                        	<c:if test="${codigoAutorizacionStock == detalleAutStockDTO.estadoPedidoAutorizacionDTO.npTipoAutorizacion}">	                                                                                
					                                            <smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.pendiente">
					                                         		<c:set var="autorizacionStock" value="1"/>
					                                            </smx:equal>                                                                            
					                                           	<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
					                                            	<c:set var="autorizacionStock" value="2"/>
					                                            </smx:equal>
					                                           	<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
					                                          		<c:set var="autorizacionStock" value="3"/>
					                                			</smx:equal>
					                                     		<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
					                                              	<c:set var="autorizacionStock" value="4"/>
					                                  			</smx:equal>
					                               				<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.utilizada">	
					                                            	<c:set var="autorizacionStock" value="5"/>
					                                    		</smx:equal>
					                                			<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.caducada">	
					                                   				<c:set var="autorizacionStock" value="6"/>
					                                          	</smx:equal>
																<smx:equal name="detalleAutStockDTO" property="estadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.gestionada">	
																	<c:set var="autorizacionStock" value="7"/>
																</smx:equal>					                                          	
					                                          	<c:if test="${autorizacionStock != 3}">
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
			                                        	<img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de stock pendiente">
			                                        </c:if>
			                                        <c:if test="${autorizacionStock == 2}">			                                        	
			                                         	<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock solicitada">			                                        	
			                                        </c:if>
			                                        <c:if test="${autorizacionStock == 3}">
			                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de stock aprobada">			                 
			                                        </c:if>
			                                        <c:if test="${autorizacionStock == 4}">
			                                        	<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de stock rechazada">
			                                        </c:if>
			                                        <c:if test="${autorizacionStock == 5}">
			                                        	<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de stock utilizada en otro momento">
			                                        </c:if>
			                                        <c:if test="${autorizacionStock == 6}">
			                                         	<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de stock caducada">
			                                       	</c:if>
													<c:if test="${autorizacionStock == 7}">
														<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de stock gestionada">
													</c:if>			                                       	
			                                   	</logic:notEmpty>
			                                    <logic:empty name="detalle" property="estadoDetallePedidoDTO.detalleEstadoPedidoAutorizacionCol">                                                                    	
			                                           &nbsp;
			                                   	</logic:empty>
                                        	</td>
                                    	</tr>
                             		</table>
                                </td>
								<td width="6%"class="fila_contenido columna_contenido" align="right">
									<logic:equal name="pesoVariable" value="${estadoActivo}">
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
									</logic:equal>
									<logic:notEqual name="pesoVariable" value="${estadoActivo}">
										<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
									</logic:notEqual>
								</td>
								
								<td width="5%" align="right" class="fila_contenido columna_contenido">
									<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
										<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
										</logic:equal>
										<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
										</logic:notEqual>
									</logic:notEmpty>
									<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
										<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
										</logic:equal>
										<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
										</logic:notEqual>
									</logic:empty>
								</td>
								
								<td width="5%" align="right" class="fila_contenido columna_contenido">
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
								<td width="7%" class="fila_contenido columna_contenido" align="right">
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
								</td>
								<td class="columna_contenido fila_contenido" width="2%" align="center">								
									<c:set var="aplicaImpuesto" value="${detalle.articuloDTO.aplicaImpuestoVenta}"/>
									<logic:notEmpty name="detalle" property="articuloDTO.npNuevoCodigoClasificacion">
	                                <c:if test="${(detalle.npAplicaImpuestoCanastoEspecial)}">
	                                 	<img src="images/tick.png" border="0">
				                    </c:if>  
				                      	<c:if test="${(!detalle.npAplicaImpuestoCanastoEspecial)}">
	                                   	<img src="images/x.png" border="0">				                                                        	
				                       	</c:if> 	       
                                 	</logic:notEmpty>
                                   	<logic:empty name="detalle" property="articuloDTO.npNuevoCodigoClasificacion">
	                                   	<c:if test="${aplicaImpuesto}">
											<img src="images/tick.png" border="0">
										</c:if>
										<c:if test="${!aplicaImpuesto}">
											<img src="images/x.png" border="0">
										</c:if>
                                  	</logic:empty>	
									<%--<c:if test="${aplicaImpuesto}">
										<img src="images/tick.png" border="0">
										</c:if>
										<c:if test="${!aplicaImpuesto}">
										<img src="images/x.png" border="0">
									</c:if>--%>
								</td>
								<td width="3%" class="fila_contenido columna_contenido" align="center">&nbsp;
									<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
									<logic:lessThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
								</td>
								<%--<td width="5%" class="fila_contenido columna_contenido" align="right">
									<bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
								</td>--%>
								<td width="7%" class="fila_contenido columna_contenido columna_contenido_der" align="right">
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
								</td>
								
								<%-- sección de detalle del pedido --%>									
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
									<tr>
										<td colspan="14" class="columna_contenido" align="center">
											<bean:define name="detalle" id="configuracionDTO"/>
											<table cellpadding="1" cellspacing="0" width="100%">
												<tr>
													<td>
														<div id="marco${indice}" class="displayNo">
															<logic:notEmpty name="detalle"  property="entregaDetallePedidoCol">
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
																			<logic:iterate name="detalle" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="numEntrega">
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
																				<logic:notEmpty name="entregaPedidoDTO" property="npDetallePedido">
																						<bean:define id="entrega" property="npDetallePedido" name="entregaPedidoDTO"></bean:define>
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
																							   <logic:notEmpty name="entregaPedidoDTO" property="entregaPedidoConvenioCol">
																							  		<b> E.DOM 
																									   <logic:iterate name="entregaPedidoDTO" property="entregaPedidoConvenioCol" id="entregaPedidoConvenio">
																									 		<bean:write name="entregaPedidoConvenio" property="secuencialConvenio"/>
																									   </logic:iterate>
																								   </b>  
																								    - 
																							   </logic:notEmpty>
																							   
																							   <bean:write name="entregaPedidoDTO" property="direccionEntrega"/>
																						   </td>
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
															</logic:notEmpty>
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</logic:equal>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
				</table>
			</div>
		</td>
	</tr>
	<tr><td colspan="2" height="5px"></td></tr>
	<tr>
		<td colspan="2" align="right">
			<table border="0" width="70%" cellspacing="0" cellpadding="0">
				<tr>
					<%-- se muestra el detalle de los descuentos --%>
					<td align="left" valign="top" width="55%">
						<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/detalleDescuentos.jsp"/>
					</td>
					<td align="right" valign="top" width="33%">
						<table border="0" cellspacing="0" cellpadding="0">
							<tr>
								<tr>
									<td colspan="5" align="right" class="textoAzul11">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
									<td align="right">
										<html:hidden name="cotizarRecotizarReservarForm" property="subTotalBrutoNoAplicaIva"/>
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalBrutoNoAplicaIva}"/>
									</td>
								</tr>
								<tr>
									<td colspan="5" align="right" class="textoAzul11">(-)DESCUENTO:&nbsp;</td>
									<td align="right">		
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.totalDescuentoIva}"/>											
									</td>
								</tr>
								<tr><td colspan="6" align="right" height="5px">-------------------------</td></tr>
							</tr>
							<tr valign="bottom">
								<td colspan="5" align="right" class="textoRojo11">SUBTOTAL NETO:&nbsp;</td>
								<td align="right">
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNetoBruto}"/>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="right" class="textoAzul11">Tarifa 0%:&nbsp;</td>
								<td align="right" class="textoNegro11">
									<html:hidden name="cotizarRecotizarReservarForm" property="subTotalNoAplicaIVA"/>
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="right" class="textoAzul11">Tarifa&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
								<td align="right" class="textoNegro11">
									<html:hidden name="cotizarRecotizarReservarForm" property="subTotalAplicaIVA"/>
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="right" class="textoAzul11"><bean:message key="porcentaje.iva"/>%&nbsp;Iva:&nbsp;</td>
								<td align="right" class="textoNegro11">
									<html:hidden name="cotizarRecotizarReservarForm" property="ivaTotal"/>
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="right" class="textoAzul11">Costo flete:&nbsp;</td>
								<td align="right" class="textoNegro11">
									<html:hidden name="cotizarRecotizarReservarForm" property="costoFlete"/>
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
								</td>
							</tr>
							<tr>
								<td colspan="5" align="right" class="textoRojo11"><b>TOTAL :&nbsp;</b></td>
								<td align="right" class="textoNegro11">
									<html:hidden name="cotizarRecotizarReservarForm" property="total"/>
									<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/></b>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>			