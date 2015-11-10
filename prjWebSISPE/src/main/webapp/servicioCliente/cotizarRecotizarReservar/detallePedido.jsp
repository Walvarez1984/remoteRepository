<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>
<bean:define id="preciosActualizables"><bean:message key="ec.com.smx.sic.sispe.detallePedido.actualizar.precio"/></bean:define>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<bean:define id="modificarReservacion"><bean:message key="ec.com.smx.sic.sispe.accion.modificarReservacion"/></bean:define>
<bean:define id="articuloDeBaja"><bean:message key="ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"/></bean:define>
<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="codigoDescuentoVariable"><bean:message key="ec.com.smx.sic.sispe.tipoAutorizacion.codigo.descuentoVariable"/></bean:define>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<c:set var="imagen" value="cotizacion.gif"/>
<c:set var="estilo_btnCotRes" value="reservacionA"/>
<c:set var="texto_btnCotRes" value="Reservar"/>
<c:set var="title_btnCotRes" value="Pasar a la reservaci&oacute;n"/>
<c:set var="accion" value="crearCotizacion.do"/>
<c:set var="altura_div" value="215"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
	<c:set var="altura_div" value="218"/>
</logic:notEmpty>

<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
	<c:set var="estilo_btnCotRes" value="cotizacionA"/>
	<c:set var="texto_btnCotRes" value="Cotizar"/>
	<c:set var="title_btnCotRes" value="Pasar a la cotizaci&oacute;n"/>
</logic:equal>

<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>

<div id="seccion_detalle">
	<table class="textoNegro11"  cellpadding="0" cellspacing="0" align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
		<tr>
            <td colspan="6">
                <table border="0" align="left" cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion">
                    <tr class="fila_titulo">
                        <logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
                        <td align="left" class="fila_contenido textoNegro10">
                            <logic:equal name="modificarDetallePedido" value="${estadoActivo}">
                                &nbsp;<html:checkbox name="cotizarRecotizarReservarForm" property="checkActualizarStockAlcance" value="${estadoActivo}" title="Active esta opci&oacute;n para actualizar el stock y alcance mediante el bot&oacute;n ACTUALIZAR"/><bean:message key="label.actualizarEstadoSIC"/>
                                <logic:notEmpty name="ec.com.smx.sic.sispe.reservacion.preciosMejorados">
                                    &nbsp;<html:checkbox name="cotizarRecotizarReservarForm" property="checkCalculosPreciosMejorados" value="${estadoActivo}" title="Active esta opci&oacute;n para que los cálculos tomen los datos actuales" onclick="requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&intercambioPrecios=ok'});"/><bean:message key="label.calculosConDatosActuales"/>
                                </logic:notEmpty>
                                <logic:empty name="ec.com.smx.sic.sispe.reservacion.preciosMejorados">
                                    <html:hidden name="cotizarRecotizarReservarForm" property="checkCalculosPreciosMejorados"/>
                                </logic:empty>
                            </logic:equal>
                            <logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
                                &nbsp;Detalle del pedido
                            </logic:notEqual>
                        </td>
                        <td height="25px" class="fila_contenido">
                            <table cellspacing="0" cellpadding="1" align="right">
                                <tr>
                                    <logic:equal name="modificarDetallePedido" value="${estadoActivo}">                                   
                                        <td>
                                            <div id="botonD">
                                                <html:link styleClass="agregarD" href="#" title="Agregar art&iacute;culos al detalle" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle','div_datosCliente'],{parameters: 'agregarArticulo=ok', evalScripts: true});">Agregar</html:link>
                                            </div>
                                        </td>
                                        <logic:empty name="ec.com.smx.sic.sispe.ocultaSeccionDescuentos">
                                            <td>
                                                <div id="botonD">
                                                    <html:link styleClass="descuentoD" href="#" title="Agregar descuentos al pedido" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle'],{parameters: 'popupDescuento=ok', evalScripts: true});">Descuento</html:link>
                                                </div>
                                            </td>
                                        </logic:empty>    
                                        <td>
                                            <div id="botonD">
                                                <html:link styleClass="actualizarD" href="#" title="Actualizar detalle del pedido" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'actualizarDetalle=ok',evalScripts:true});">Actualizar</html:link>
                                            </div>
                                        </td>
                                        <td>
                                            <div id="botonD">
                                                <html:link styleClass="eliminarD" href="#" title="Eliminar art&iacute;culos seleccionados" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','mensajes','seccion_detalle','div_datosCliente'],{parameters: 'eliminarArticulos=ok'});">Eliminar</html:link>
                                            </div>
                                        </td>	
                                    </logic:equal>
                                    <logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">										
                                        <%-- <td>
                                            <div id="botonD">
                                                <html:link styleClass="eliminarD" href="#" title="subir un archivo de configuraci&oacute;n del beneficiario" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','pregunta'],{parameters: 'subirArchivo=ok'});">Beneficiario</html:link>
                                            </div>
                                        </td> --%>
                                        <td>
                                            <div id="botonD">
                                                <input type="hidden" name="registrarFecha" value="">
<%--                                                 <html:link styleClass="entregarD" href="#" title="Realizar entrega de art&iacute;culos" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta','seccion_detalle','div_datosCliente'],{parameters: 'registrarFecha=ok',evalScripts:true});">&nbsp;Entregas</html:link> --%>
                                                <html:link styleClass="entregarD" href="#" title="Realizar entrega de art&iacute;culos" onclick="cotizarRecotizarReservarForm.registrarFecha.value='OK';cotizarRecotizarReservarForm.submit();popWait();">&nbsp;Entregas</html:link>
                                            </div>
                                        </td>
										</logic:equal>                                    
                                    <logic:equal name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
                                        <td>
                                            <div id="botonD" style="border: 1px solid #000000">
                                                <input type="hidden" name="registrarFecha" value="">
                                                <html:link styleClass="entregarD" href="#" title="Realizar entrega de art&iacute;culos" onclick="cotizarRecotizarReservarForm.registrarFecha.value='OK';cotizarRecotizarReservarForm.submit();popWait();">&nbsp;Entregas</html:link>
                                            </div>
                                        </td>
                                    </logic:equal>
                                </tr>
                            </table>
                        </td>
                        </logic:empty>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
                            <td align="left" class="fila_contenido textoNegro10">
                            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="91%">
                                        Detalles del pedido consolidado
                                    </td>
                                   	<td height="25px">
                                        <div id="botonD">
	                                        <logic:notEmpty name="ec.com.smx.sic.sispe.ocultar.boton.descuentos.consolidacion">
	                                            <html:link styleClass="descuentoD" href="#" title="Agregar descuentos al pedido" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle'],{parameters: 'popupDescuento=ok', evalScripts: true});">Descuento</html:link>
	                                         </logic:notEmpty>
                                         </div>
                                   	</td>
                                </tr>
                            </table>	
                            </td>
                        </logic:notEmpty>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <jsp:include page="/servicioCliente/cotizarRecotizarReservar/detallePedidoArticulos.jsp"/>
                        </td>
                    </tr>
                </table>
            </td>
		</tr>
		<tr>
			<!-- secci&oacute;n de los totales -->
			<td colspan="6">
				<div id="pie_pedido">
					<table cellpadding="0" width="100%" cellspacing="3" border="0" align="left" class="tabla_informacion">
						<tr><td height="3px" colspan="3"></td></tr>
						<tr>
							<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
								 <c:set var="ancho" value="${28}"/>
							</logic:empty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
								 <c:set var="ancho" value="${5}"/>
							</logic:notEmpty>
							<td valign="middle" width="${ancho}%">
								<!--<bean:define id="accion" name="ec.com.smx.sic.sispe.accion"/>-->
								<logic:present name="ec.com.smx.sic.sispe.accion">
									<bean:define id="accion" name="ec.com.smx.sic.sispe.accion"/>
								</logic:present>
								<logic:notPresent name="ec.com.smx.sic.sispe.accion">
									no se encuentra esta variab
								</logic:notPresent>
								
								<c:set var="mostrarIngresoAbonos" value="0"/>
								<c:if test="${accion == reservacion}">
									<c:set var="mostrarIngresoAbonos" value="1"/>
								</c:if>
								<c:if test="${accion == modificarReservacion}">
									<logic:notEmpty name="ec.com.smx.sic.sispe.modificarReserva.sinPago">
										<c:set var="mostrarIngresoAbonos" value="1"/>
									</logic:notEmpty>
									<logic:empty name="ec.com.smx.sic.sispe.modificarReserva.sinPago">
										<c:set var="mostrarIngresoAbonos" value="2"/>
									</logic:empty>
								</c:if>
								<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
									<logic:equal name="mostrarIngresoAbonos" value="1">
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td>
													<table width="100%" cellspacing="0" cellpadding="0" align="left" class="textoNegro11">
														<tr>
															<td align="right" class="textoAzul11">Porc. min. abono:&nbsp;</td>
															<bean:define id="porcentajeAbono" name="ec.com.smx.sic.sispe.pedido.porcentajeAbono"/>
															<td align="left"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${porcentajeAbono}"/>%</b></td>
														</tr>
														<tr>												
															<td align="right" class="textoAzul11">Valor min. abono:&nbsp;</td>
															<bean:define id="valorAbono" name="ec.com.smx.sic.sispe.pedido.valorAbono"/>
															<td align="left"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorAbono}"/></b></td>
														</tr>
														<c:set var="editarAbonoCero" value="false"></c:set>
														<c:set var="estiloAbonoCero" value="textObligatorio"></c:set>
														<tr>																											
															<td align="right" class="textoAzul11">Abono cero:&nbsp;</td>
															<td>
																<logic:empty name="cotizarRecotizarReservarForm" property="valorAbono">
																	<input type="checkbox" property="checkAbonoCero" value="sinAbonoCero" onclick="javascript:requestAjax('crearCotizacion.do',['seccion_detalle','valAbono'],{parameters: 'abonoCero=ok', evalScripts: true});">																		
																</logic:empty>
																<logic:notEmpty name="cotizarRecotizarReservarForm" property="valorAbono">
																	<logic:equal name="cotizarRecotizarReservarForm" property="valorAbono" value="0">
																		<input type="checkbox" property="checkAbonoCero" value="sinAbonoCero" checked onclick="javascript:requestAjax('crearCotizacion.do',['seccion_detalle','valAbono'],{parameters: 'abonoCero=cancelar', evalScripts: true});">
																		<c:set var="editarAbonoCero" value="true"></c:set>
																		<c:set var="estiloAbonoCero" value="textObligatorioOpacity"></c:set>
																	</logic:equal>
																	<logic:notEqual name="cotizarRecotizarReservarForm" property="valorAbono" value="0">
																		<input type="checkbox" property="checkAbonoCero" value="sinAbonoCero" onclick="javascript:requestAjax('crearCotizacion.do',['seccion_detalle','valAbono'],{parameters: 'abonoCero=ok', evalScripts: true});">
																	</logic:notEqual>
																</logic:notEmpty>																
															</td>
														</tr>
														<tr>
															<td align="right" class="textoAzul11">Valor abono:*&nbsp;</td>								
															<td><smx:text property="valorAbono" disabled="${editarAbonoCero}" styleClass="${estiloAbonoCero}" styleError="campoError" size="16" maxlength="13" onkeypress="return validarInputNumericDecimal(event);"/></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</logic:equal>
									<logic:equal name="mostrarIngresoAbonos" value="2">
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td align="right" class="textoAzul11" valign="top">Valor abonado:&nbsp;</td>
												<bean:define id="abonoPedido" name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="abonoPedido"/>
												<td align="left" valign="top"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abonoPedido}"/>
											</tr>										
											<logic:notEmpty name="ec.com.smx.sic.sispe.devolucionAbono">
												<tr>
													<td align="right" class="textoAzul11" valign="top">Devoluci&oacute;n:&nbsp;</td>
													<td align="left" valign="top" class="textoRojo11">
														<bean:define id="devolucionAbono" name="ec.com.smx.sic.sispe.devolucionAbono"></bean:define>
														<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${devolucionAbono}"/></b>
													</td>
												</tr>
											</logic:notEmpty>
											<bean:define id="pagadoTotalmente" name="ec.com.smx.sic.sispe.estadoPagadoTotalmente"/>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="codigoEstadoPagado" value="${pagadoTotalmente}">
												<tr><td height="25px" align="right" class="textoAzul11" colspan="2"><b>PAGADO TOTALMENTE</b></td></tr>
											</logic:equal>
											<bean:define id="liquidado" name="ec.com.smx.sic.sispe.estadoLiquidado"/>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="codigoEstadoPagado" value="${liquidado}">
												<tr><td height="25px" align="right" class="textoAzul11" colspan="2"><b>RESERVA LIQUIDADA</b></td></tr>
											</logic:equal>
										</table>
									</logic:equal>
								</logic:empty>
							</td>
							<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
								<td align="left" width="18%" class="textoRojo11">
									<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentoMaxNavEmp">
										TODOS LOS PEDIDOS CONSOLIDADOS DEBEN DE SER PAGADOS EN EFECTIVO.
									</logic:notEmpty>
								</td>
							</logic:notEmpty>
							<td valign="top" width="48%">
								<%-- se muestra el detalle de los descuentos --%>
								<div style="overflow-y:auto; overflow-x:hidden;height:100px">
									<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/detalleDescuentos.jsp"/>
								</div>
							</td>
							<td valign="top" width="25%">
								<table border="0" cellspacing="0" align="right" cellpadding="0">
									<tr>
										<td align="right" class="textoAzul10">SUBTOTAL BRUTO SIN IVA:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<html:hidden name="cotizarRecotizarReservarForm" property="subTotalBrutoNoAplicaIva"/>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalBrutoNoAplicaIva}"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoAzul10">(-)DESCUENTO:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.totalDescuentoIva}"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoRojo10">SUBTOTAL NETO:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNetoBruto}"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoAzul11">TARIFA 0%:&nbsp;</td>
										<td align="right" class="textoNegro11">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalNoAplicaIVA}"/>
											<html:hidden name="cotizarRecotizarReservarForm" property="subTotalNoAplicaIVA"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoAzul10">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.subTotalAplicaIVA}"/>
											<html:hidden name="cotizarRecotizarReservarForm" property="subTotalAplicaIVA"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoAzul10"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.ivaTotal}"/>
											<html:hidden name="cotizarRecotizarReservarForm" property="ivaTotal"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoAzul10">COSTO FLETE:&nbsp;</td>
										<td align="right" class="textoNegro10">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.costoFlete}"/>
											<html:hidden name="cotizarRecotizarReservarForm" property="costoFlete"/>
										</td>
									</tr>
									<tr>
										<td align="right" class="textoRojo10"><b>TOTAL:&nbsp;</b></td>
										<td align="right" class="textoNegro10">
											<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${cotizarRecotizarReservarForm.total}"/></b>
											<html:hidden name="cotizarRecotizarReservarForm" property="total"/>
										</td>
									</tr>
								</table>
							</td>
							<td width="2%">&nbsp;</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				
					<script language="JavaScript" type="text/javascript">document.getElementById('div_listado').scrollTop=document.getElementById('div_listado').scrollHeight;</script>
								
			</td>
		</tr>
	</table>
</div>