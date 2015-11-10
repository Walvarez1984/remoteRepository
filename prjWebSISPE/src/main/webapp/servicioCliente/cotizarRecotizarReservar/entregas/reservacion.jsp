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
<bean:define id="stockParcialBodega"><bean:message key="ec.com.smx.sic.sispe.stock.parcialBodega"/></bean:define>
<bean:define id="tipoEntregaParcial"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega.parcial"/></bean:define>
<bean:define id="entregaDireccion"><bean:message key="ec.com.smx.sic.sispe.entrega.direccion"/></bean:define>
<bean:define id="indiceEntrega" value="${0}"/>
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
									<!-- Descomentar este codigo si se desea actualizar las entregas -->
									<%--td align="right" width="105px">
										<div id="botonD">
											<html:link title="Actualiza los cambios que se realicen en las entregas" styleClass="actualizarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['mensajes','calendario','reserva'], {parameters: 'botonActualizarEntrega=ok',popWait:true});">Act. Entrega</html:link>
										</div>
									</td--%>
									<td align="right" width="105px">
										<div id="botonD">
											<html:link title="Elimina las entregas que se encuentren seleccionadas" styleClass="eliminarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['mensajes','localEntrega','calendario','reserva','direccionEntregas','fechaEntregas','pregunta'], {parameters: 'botonEliminarEntrega=ok',popWait:true, evalScripts:true});">Eliminar Ent.</html:link>
										</div>
									</td>
									<td align="right" width="105px">
										<div id="botonD">
											<html:link title="Agrega entregas al detalle del pedido por artículo" styleClass="agregarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['mensajes','reserva','datos','costoEntrega','calendario','fechaEntregas','direccionEntregas','buscar'], {parameters: 'botonAceptarEntrega=ok',popWait:true, evalScripts:true});">Aceptar Ent.</html:link>
										</div>
									</td>
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
										<table cellpadding="0" cellspacing="0">
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
												<td width="110" class="textoAzul11" align="left">Nombre Contacto:&nbsp;</td>
													<td align="left">
														<bean:write name="cotizarRecotizarReservarForm" property="nombreContacto"/>
												</logic:empty>
												
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0">
											<td width="32"></td>
											<logic:notEmpty name="cotizarRecotizarReservarForm" property="fechaDespacho">
												<td width="112" class="textoAzul11">Fecha Recepci&oacute;n:</td>
												<td width="150">
													<bean:write name="cotizarRecotizarReservarForm" property="fechaDespacho"/>
												</td>
											</logic:notEmpty>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="6px"></td></tr>
					<tr>
						<td valign="top">
							<%--<div id="reserva">--%>
							<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedidoAux">
								<table border="0" cellspacing="0" cellpadding="0" class="tabla_informacion" width="98%" align="center">
									<tr align="left" height="18px">
										<td class="tituloTablas" align="right" width="4%"></td>
										<td class="tituloTablas" align="center" width="5%">
											<logic:notEmpty name="ec.com.smx.sic.sispe.existenEntregas">
												<html:checkbox title="Eliminar todas las entregas" property="todo" onclick="activarDesactivarTodo(this,cotizarRecotizarReservarForm.seleccionEntregas)"/>
											</logic:notEmpty>
										</td>
										<td class="columna_contenido tituloTablas" align="center" width="39%">DESCRIPCI&Oactute;N ART&Iacute;CULO</td>
										<td class="columna_contenido tituloTablas" align="center" width="11%">CANT. TOTAL.</td>
										<td class="columna_contenido tituloTablasEntregaCliente" align="center" width="19%" colspan="2">ENT. CLIENTE</td>
										<td class="columna_contenido tituloTablasPedidoCD" width="25%" colspan="2">&nbsp;&nbsp;&nbsp;PEDIR AL CD</td>
									</tr>
									<tr>
										<td colspan="9">
											<div id="listado_articulos" style="width:100%;height:${altoDivDetArt}px;overflow:auto;">
												<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<bean:define id="contadorCheckTransito" value="0"></bean:define>
													<logic:iterate name="ec.com.smx.sic.sispe.detallePedidoAux" id="detalle" indexId="numDetalle">
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
														<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
														<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
															<c:set var="clase" value="naranjaA10"/>
														</logic:greaterThan>
														<logic:equal name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
															<c:set var="clase" value="verdeClaro10"/>
														</logic:equal>
														<tr id="fila_en_${numDetalle}">
															<td width="4%" align="center" height="25"  class="${clase} textoNegro10 columna_contenido_blanco">
																<div id="desplegar${numDetalle}">
																	<logic:notEmpty name="detalle" property="entregas">
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
															<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
															<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="39%" title="C&oacute;digo barras: ${articulo.codigoBarrasActivo.id.codigoBarras}">
																<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
																	<img title="canasto modificado" src="images/estrella.gif" border="0">
																 </logic:notEmpty>
																<bean:write name="articulo" property="descripcionArticulo"/>
															</td>
															<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="11%" id="cantidad_en_${numDetalle}"><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></td>
															<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="8%">
																<logic:notEmpty name="ec.com.smx.sic.sispe.habilitarCantidadEntrega">
																	<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
																		<smx:text name="cotizarRecotizarReservarForm" property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}" styleClass="combos" size="3" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['mensajes','reserva','datos','costoEntrega','sectores','calendario','fechaEntregas','direccionEntregas'], {parameters: 'botonAceptarEntrega=ok',popWait:true, evalScripts:true});"/>
																	</logic:greaterThan>
																	<logic:equal name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
																		<html:hidden property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}"/>
																	</logic:equal>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sispe.habilitarCantidadEntrega">
																	<html:hidden write="true" property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}"/>
																</logic:empty>
															</td>
															<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="12%" id="contador_en_${numDetalle}">
																<b>
																<bean:write name="detalle" property="npContadorEntrega"/></b>/<b><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>&nbsp;</b>
															</td>
															<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="7%">
																<logic:notEmpty name="ec.com.smx.sic.sispe.habilitarCantidadReserva">
																	<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
																		<smx:text name="cotizarRecotizarReservarForm" property="cantidadPedidaBodega" value="${detalle.estadoDetallePedidoDTO.cantidadReservarSIC}" styleClass="combos" size="3" onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['mensajes','reserva','datos','costoEntrega','sectores','calendario','fechaEntregas','direccionEntregas'], {parameters: 'botonAceptarEntrega=ok',popWait:true, evalScripts:true});"/>
																	</logic:greaterThan>
																	<logic:equal name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
																		<html:hidden property="cantidadPedidaBodega" value="0"/>
																	</logic:equal>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sispe.habilitarCantidadReserva">
																	<html:hidden write="true" property="cantidadPedidaBodega" value="${detalle.estadoDetallePedidoDTO.cantidadReservarSIC}"/>
																</logic:empty>
															</td>
															<td class="${clase} textoNegro10 columna_contenido_blanco" width="14%" id="contador_en_${numDetalle}">
																&nbsp;&nbsp;&nbsp;<b><bean:write name="detalle" property="npContadorDespacho"/></b>
																/<b><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></b>
															</td>
														</tr>
														<tr>
															<td colspan="9">
																<div id="marco${numDetalle}">
																	<logic:notEmpty name="detalle" property="entregas">
																		<table border="0" width="100%" cellpadding="0" cellspacing="0">
																			<tr>
																				<td width="4%"></td>
																				<td align="left">
																					<table border="0" width="99%" cellspacing="0" cellpadding="0">
																						<tr>
																							<td>
																								<table class="tabla_informacion" border="0" width="100%" cellspacing="0" cellpadding="0">
																									<tr>
																										<td width="3%" align="right" class="tituloTablas columna_contenido"></td>
																										<td width="5%" align="right" class="tituloTablas columna_contenido"></td>
																										<td width="21%" align="center" class="subtituloTablasEntregaCliente columna_contenido" title="Fecha de Entrega">FECHA ENT.</td>
																										<td width="30%" align="center" class="subtituloTablasEntregaCliente columna_contenido">DIRECCI&Oacute;N&nbsp;</td>
																										<td class="columna_contenido subtituloTablasEntregaCliente" align="center" width="11%">ENTREGAR</td>
																										<td width="14%" align="center" class="subtituloTablasPedidoCD columna_contenido" title="Fecha de Despacho">FECHA DES.</td>
																										<td class="columna_contenido subtituloTablasPedidoCD" width="10%" align="center">PEDIR CD</td>
																										<td width="8%" align="center" class="subtituloTablasTrans columna_contenido">TRAN.</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																						<tr>
																							<%-- si la colecci&oacute;n de direcciones no est&aacute; vacia --%>
																							<td width="100%" align="left" colspan="6">
																								<%--<div id="div_listadoO" style="width:100%;height:100px;overflow-x:hidden;overflow-y:auto;">--%>
																								<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="left">
																									<logic:iterate name="detalle" property="entregas" id="entrega" indexId="numEntrega">
																										<bean:define id="residuoE" value="${numEntrega % 2}"/>
																										<c:set var="indiceEntrega" value="${indiceEntrega + 1}"/>
																										<logic:equal name="residuoE" value="0">
																											<bean:define id="colorBack" value="grisClaro10"/>
																										</logic:equal>
																										<logic:notEqual name="residuoE" value="0">
																											<bean:define id="colorBack" value="blanco10"/>
																										</logic:notEqual>
																										<tr class="${colorBack}">
																											<td width="3%" align="center" class="textoNegro9">
																												<html:link href="javascript:requestAjax('entregaLocalCalendario.do',['mensajes','div_pagina'],{parameters: 'verResponsable=${indiceEntrega}'});mostrarModal();" title="Ver responsable"><img title="Ver responsable" src="./images/resp.gif" border="0"></html:link>
																											</td>
																											<td width="5%" align="center" class="textoNegro9">
																												<html:multibox property="seleccionEntregas" value="${numEntrega}-${numDetalle}"></html:multibox>
																											</td>
																											<td width="21%" align="center" class="textoNegro9">
																												<bean:write name="entrega" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
																											</td>
																											<td width="30%" align="left" class="textoNegro9">&nbsp;<bean:write name="entrega" property="direccionEntrega"/></td>
																											<td width="11%" align="center" class="textoNegro9">
																												<!-- Descomentar este codigo si se desea actualizar las entregas -->
																												<%--logic:equal name="entrega" property="codigoAlcanceEntrega" value="${tipoEntregaParcial}">
																													<smx:text name="cotizarRecotizarReservarForm" property="cantidadEntregas" value="${entrega.npCantidadEntrega}" styleClass="combos" size="4"  onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['mensajes','calendario','reserva'], {parameters: 'botonActualizarEntrega=ok',popWait:true});"/>
																												</logic:equal>
																												<logic:notEqual name="entrega" property="codigoAlcanceEntrega" value="${tipoEntregaParcial}">
																													<bean:write name="entrega" property="cantidadEntrega"/>
																													<html:hidden property="cantidadEntregas" value="${entrega.cantidadEntrega}"/>
																												</logic:notEqual--%>
																												<!-- Quitar este codigo si se desea actualizar las entregas -->
																												<bean:write name="entrega" property="cantidadEntrega"/>
																											</td>
																											<td width="14%" align="center" class="textoNegro9">
																												<logic:notEmpty name="entrega" property="calendarioDiaLocalDTO">
																													<c:if test="${entrega.npCantidadBultos>0}">
																														<bean:write name="entrega" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
																													</c:if>
																												</logic:notEmpty>
																											</td>
																											<c:set var="bultos" value=""/>
																											<logic:notEmpty name="entrega" property="calendarioDiaLocalDTO">
																												<c:if test="${entrega.npCantidadBultos>0}">
																													<c:set var="bultos"><bean:write name="entrega" property="npCantidadBultos"/></c:set>
																												</c:if>
																											</logic:notEmpty>
																											<td width="10%" align="center" class="textoNegro9" title="Bultos: ${bultos}">
																												<!-- Descomentar este codigo si se desea actualizar las entregas -->
																												<%--logic:equal name="entrega" property="codigoObtenerStock" value="${stockParcialBodega}">
																													<smx:text name="cotizarRecotizarReservarForm" property="cantidadDespachos" value="${entrega.npCantidadDespacho}" styleClass="combos" size="4"  onkeypress="requestAjaxEnter('entregaLocalCalendario.do', ['mensajes','calendario','reserva'], {parameters: 'botonActualizarEntrega=ok',popWait:true});"/>
																												</logic:equal>
																												<logic:notEqual name="entrega" property="codigoObtenerStock" value="${stockParcialBodega}">
																													<c:if test="${entrega.npCantidadBultos>0}">
																														<bean:write name="entrega" property="cantidadDespacho"/>
																														<html:hidden property="cantidadDespachos" value="${entrega.cantidadDespacho}"/>
																													</c:if>
																													<c:if test="${entrega.npCantidadBultos==0}">
																														<html:hidden property="cantidadDespachos" value="${entrega.cantidadDespacho}"/>
																													</c:if>
																												</logic:notEqual--%>
																												<!-- Quitar este codigo si se desea actualizar las entregas -->
																												<c:if test="${entrega.npCantidadBultos > 0}">
																													<bean:write name="entrega" property="cantidadDespacho"/>
																												</c:if>
																											</td>
																											<!-- OANDINO: Sección para CHECKBOX de tránsito -->
																											<td width="8%" align="center" class="textoNegro9" title="Establecer bodega de tr&aacute;nsito">
																												<c:if test="${entrega.npValidarCheckTransito == 1}">
																													<c:if test="${entrega.npPasoObligatorioBodegaTransito != null && entrega.npPasoObligatorioBodegaTransito == 1}">
																														<html:multibox property="checkTransitoArray" title="Tr&aacute;nsito Obligatorio" value="${entrega.npValorCodigoTransito}-${numDetalle}-${numEntrega}" onclick="return(false);"></html:multibox>
																														<script type="text/javascript">
																															//alert('paso');
																															if(cotizarRecotizarReservarForm.checkTransitoArray.length){
																																cotizarRecotizarReservarForm.checkTransitoArray[${contadorCheckTransito}].checked = true;
																															}else{
																																cotizarRecotizarReservarForm.checkTransitoArray.checked = true;
																															}
																														</script>
																													</c:if>
																													<c:if test="${entrega.npPasoObligatorioBodegaTransito == null}">
																														<html:multibox property="checkTransitoArray" value="${entrega.npValorCodigoTransito}-${numDetalle}-${numEntrega}"></html:multibox>
																													</c:if>
																													<bean:define id="contadorCheckTransito" value="${contadorCheckTransito + 1}"></bean:define>
																												</c:if>
																											</td>
																											<%--
																											<td width="8%" align="center" class="textoNegro9">
																												<logic:notEmpty name="entrega" property="calendarioDiaLocalDTO">
																													<c:if test="${entrega.npCantidadBultos>0}">
																														<bean:write name="entrega" property="npCantidadBultos"/>
																													</c:if>
																												</logic:notEmpty>
																											</td>
																											--%>
																										</tr>
																									</logic:iterate>
																								</table>
																								<%--</div>--%>
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
														<logic:notEmpty name="detalle" property="entregas">
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
	<logic:empty name="ec.com.smx.sic.sispe.calendarizacion.locales">
		<tr><td height="6px"></td></tr>
		<tr>
			<td align="center">
				<table border="0" cellpadding="0" cellspacing="0" width="98%">
					<tr>
						<td width="98%">
							<div id="direccionEntregas">
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
									<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
										<tr>
											<td class="fila_titulo" height="29px">
												<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
													<tr>
														<td width="5%" align="right"><img src="images/direcciones.gif" border="0"></td>
														<td width="95%" align="left">&nbsp;Direcciones de Entrega:</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td height="6px"></td></tr>
										<tr>
											<td>
												<table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
													<tr>
														<td width="100%">
															<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion" height="19px">
																<tr>
																	<td width="5%" align="center" class="tituloTablas columna_contenido"></td>
																	<td width="95%" align="center" class="tituloTablas columna_contenido"><B>DIRECCION</B></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td align="left">
															<div style="width:100%;height:65px;overflow-y:auto;overflow-x:hidden">
																<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" class="tabla_informacion">
																	<logic:iterate name="ec.com.smx.sic.sispe.pedido.direccionesAux" id="direccionesDTO" indexId="indiceDireccionEntregas">
																		<bean:define id="numFila" value="${indiceDireccionEntregas % 2}"/>
																		<logic:equal name="numFila" value="0">
																			<bean:define id="color" value="grisClaro"/>
																		</logic:equal>
																		<logic:equal name="numFila" value="1">
																			<bean:define id="color" value="blanco"/>
																		</logic:equal>
																		<tr class="${color}">
																			<td width="5%" align="center" class="textoNegro9">
																				<html:multibox property="direcciones" value="${indiceDireccionEntregas}" onclick="requestAjax('entregaLocalCalendario.do',['datos','mensajes','direccionEntregas'],{parameters: 'seleccionaDir=${indiceDireccionEntregas}',evalScripts:true});"/>
																				<html:hidden name="cotizarRecotizarReservarForm" property="direcciones"/>
																			</td>
																			<td width="95%" class="textoNegro9">
																				&nbsp;<bean:write name="direccionesDTO" property="descripcion"/>
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
										<tr><td height="6px"></td></tr>
									</table>
								</logic:notEmpty>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</logic:empty>
</table>