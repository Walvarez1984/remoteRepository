<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:insert page="/include/top.jsp"/>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<logic:empty name="sispe.estado.activo">
	<bean:define id="estadoActivo" value="1"/>
	<bean:define id="estadoInactivo" value="0"/>
</logic:empty>
<bean:define id="preciosActualizables"><bean:message key="ec.com.smx.sic.sispe.receta.actualizar.precio"/></bean:define>
<bean:define id="opCodigoClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroClasificacion"/></bean:define>
<bean:define id="opNombreClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreClasificacion"/></bean:define>
<bean:define id="opNombreArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreArticulo"/></bean:define>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<bean:define id="articuloDeBaja"><bean:message key="ec.com.smx.sic.sispe.estadoArticuloSIC.deBaja"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.clasificacionesImplementos">
	<bean:define id="clasificacionesImplementos" name="ec.com.smx.sic.sispe.clasificacionesImplementos"/>
</logic:notEmpty>
<c:set var="modificarDetallePedido" value="${estadoActivo}"/>
<!-- Se verifica si la acción es modificación de reserva -->
<logic:notEmpty name="ec.com.smx.sic.sispe.modificarReserva.pagadoTotalmente">
	<%-- esta condición sirve para controlar que cuando el pedido se haya pagado totalmente no se puedan modificar cantidades o artículos del detalle --%>
	<c:set var="modificarDetallePedido" value="0"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.modificarReserva.pagadoLiquidado">
	<%-- esta condición sirve para controlar que cuando el pedido se haya pagado totalmente no se puedan modificar cantidades o artículos del detalle --%>
	<c:set var="modificarDetallePedido" value="0"/>
</logic:notEmpty>
<html:form action="detalleCanasta" method="post">
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<tiles:insert page="/confirmacion/confirmacion.jsp"/>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="detalleCanasta"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<tr>
			<td class="titulosAccion" height="35px">
				<html:hidden property="ayuda" value=""/>
				<html:hidden property="numeroDocumento"/>
				<html:hidden property="nombreContacto"/>
				<html:hidden property="telefonoContacto"/>
				<html:hidden property="rucEmpresa"/>
				<html:hidden property="nombreEmpresa"/>
				<html:hidden property="fechaEntrega"/>
				<html:hidden property="indiceLocalResponsable"/>
				<html:hidden property="checkCalculosPreciosMejorados"/>
				<html:hidden property="checkCalculosPreciosAfiliado"/>
				<html:hidden property="opValidarPedido"/>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td  width="3%" align="center"><img src="images/detalle_canasto.gif" border="0"></img></td>
						<td align="left">&nbsp;&nbsp;Detalle de:&nbsp;<bean:write name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.descripcionArticulo"/></td>
						<td align="right">
							<table cellspacing="0">
								<tr>
								<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
									<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
										<td>
											<div id="botonA">
												<%--<html:link href="#" styleClass="guardarA" onclick="realizarEnvio('acepCanasto');">Guardar</html:link>--%>
												<html:link styleClass="guardarA" href="#" onclick="requestAjax('detalleCanasta.do',['pregunta','div_pagina','mensajes'],{parameters: 'ayuda=acepCanasto', evalScripts:true});">Guardar</html:link>
											</div>
										</td>
									</logic:equal>
								</logic:empty>
									<td>
										<div id="botonA">
											<html:link styleClass="cancelarA" href="#" onclick="realizarEnvio('cancCanasto');">Cancelar</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center" valign="top">
				<table border="0" width="100%" cellpadding="0" cellspacing="0" align="left">
					<tr><td colspan="3" height="5px"></td></tr>
					<tr>
						<td colspan="3">
							<div id="cabezeraCanasto">
								<table border="0" align="left" cellpadding="0" class="textoNegro11">
									<tr>
										<td class="textoAzul11" align="right" >C&oacute;digo barras:&nbsp;</td>
										<td align="left"><bean:write name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
										<td colspan="3">&nbsp;</td>
									</tr>
									<tr>
										<td class="textoAzul11" align="right">Descripci&oacute;n:&nbsp;</td>
										<td align="left"><bean:write name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.descripcionArticulo"/></td>
										<td colspan="1">&nbsp;</td>
										<td class="textoAzul11" align="right">Cantidad solicitada:&nbsp;</td>
										<td align="left"><bean:write name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="estadoDetallePedidoDTO.cantidadEstado"/></td>										
									</tr>
									<tr>							
										<td class="textoAzul11" align="right"><b>Precio tot. sin IVA.:</b>&nbsp;</td>
										<td align="left"><b>
											<bean:define id="totalCanastaSinIva" name="ec.com.smx.sic.sispe.pedido.totalCanastaSinIva"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalCanastaSinIva}"/></b></td>
										<td colspan="1" width="30px">&nbsp;</td>										
										<td class="textoAzul11" align="right"><b>Precio total:</b>&nbsp;</td>
										<td align="left"><b>
											<bean:define id="totalCanasta" name="ec.com.smx.sic.sispe.pedido.totalCanasta"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalCanasta}"/></b></td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr><td colspan="3" height="5px"></td></tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos" width="10%">
						<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
							<table width="100%" border="0" class="tabla_informacion panelContent" cellpadding="0" cellspacing="0">
								<%-- B&uacute;squeda--%>
								<tr>
									<td class="fila_titulo" colspan="2">
										<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
											<tr>
												<td width="18%"><img src="images/buscar24.gif" border="0"/></td>
												<td width="85%" class="textoNegro11">B&uacute;squeda</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<c:set var="actualizarDetalleClick" value="requestAjax('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok', evalScripts: true});"/>
										<c:set var="actualizarDetalleEnter" value="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok', evalScripts: true});"/>
										<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
											<c:set var="actualizarDetalleClick" value=""/>
											<c:set var="actualizarDetalleEnter" value=""/>
										</logic:notEqual>
										<table border="0" cellspacing="0" align="left" width="100%">
											<tr>
												<td align="left" class="textoAzul9"><html:radio property="opTipoBusqueda" value="${opCodigoClasificacion}" onclick="document.forms[0].textoBusqueda.focus()">Cod. clasificaci&oacute;n</html:radio></td>
											</tr>
											<tr>
												<td align="left" class="textoAzul9"><html:radio property="opTipoBusqueda" value="${opNombreClasificacion}" onclick="document.forms[0].textoBusqueda.focus()">Desc. clasificaci&oacute;n</html:radio></td>
											</tr>
											<tr>
												<td align="left" class="textoAzul9"><html:radio property="opTipoBusqueda" value="${opNombreArticulo}" onclick="document.forms[0].textoBusqueda.focus()">Desc. art&iacute;culo</html:radio></td>
											</tr>
											<tr>
												<td align="left">
													<html:text property="textoBusqueda" size="26" styleClass="textNormal" onkeyup="requestAjaxEnter('crearCotizacion.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});"/>
													<!--<html:text property="textoBusqueda" size="26" styleClass="textNormal" onkeypress="resultadosBusquedaENTER('buscarArticulo.do','WIN_RBUS',['opTipoBusqueda','textoBusqueda'],'buscarArtPed');${actualizarDetalleEnter}"/> -->																																
																														  
													<!-- para el correcto funcionamiento del ENTER -->
													<input type="text" name="textoOculto" style="display:none">
												</td>
											</tr>
											<tr><td height="5px"></td></tr>
											<tr>
												<td align="left"><html:link href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'buscarPorEstructura=ok',popWait:true});">Estructura comercial</html:link></td>
												<!--<td align="left"><html:link href="#" onclick="dialogoWeb('catalogoArticulos.do','WIN_RBUS','dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');${actualizarDetalleClick}">Estructura comercial XX</html:link></td>-->
											</tr>
										</table>
									</td>
								</tr>
								<%--Bot&oacute;n Buscar--%>
								<tr><td height="7px"></td></tr>
								<tr>
									<td align="right" id="botonD"><html:link href="#" styleClass="buscarD" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});">Buscar</html:link></td>
									<!--<td align="right" id="botonD"><html:link href="#" styleClass="buscarD" onclick="resultadosBusqueda('buscarArticulo.do','WIN_RBUS',['opTipoBusqueda','textoBusqueda'],'buscarArtPed');${actualizarDetalleClick}">Buscar</html:link></td> -->
								</tr>
								<tr><td height="5px"></td></tr>
								<%--Fin B&uacute;squeda--%>
							</table>
							</logic:empty>
							<table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td height="5px"></td></tr></table>
							<table width="100%" border="0" class="tabla_informacion panelContent" cellpadding="0" cellspacing="0">
								<tr>
									<td class="fila_titulo" colspan="2">
										<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
											<tr>
												<td width="18%"><img src="images/datos_informacion24.gif" border="0"/></td>
												<td width="85%" class="textoNegro11">Informaci&oacute;n</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td align="left" height="25px" width="100%">
										<table border="0" align="left">
											<tr>
												<td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="violetaClaro10" width="100%" height="12px"></td></tr></table></td>
												<td align="left" class="textoAzul10">Implementos (Los puede encontrar en las clasificaciones ${clasificacionesImplementos})</td>
											</tr>
											<tr>
												<td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="naranjaClaro10" width="100%" height="12px"></td></tr></table></td>
												<td align="left" class="textoAzul10">Sin stock<br>(El valor que se compara con el stock para cada &iacute;tem es su cantidad multiplicada por la cantidad solicitada del canasto.)</td>
											</tr>
											<tr>
												<td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
												<td align="left" class="textoAzul10">Sin alcance</td>
											</tr>
											<tr>
												<td width="18%" valign="top" align="right"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="celesteClaro10" width="100%" height="12px"></td></tr></table></td>
												<td align="left" class="textoAzul10">Inactivo en el SIC</td>
											</tr>
											<tr>
												<td width="18%" valign="top" align="right"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
												<td align="left" class="textoAzul10">De baja en el SIC</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
						<%--Fin Barra Izquierda--%>
						<td width="4px"></td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tabla_informacion">
								<tr>
									<td class="fila_titulo" height="25px">
										<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
											<tr>
												<td align="left" class="textoNegro11">
													<b>&nbsp;Detalle del canasto:</b>
												</td>
												<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
													<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
														<td>
															<table cellspacing="0" cellpadding="1" align="right">
																<tr>
																	<td>
																		<div id="botonD">
																			<html:link styleClass="agregarD" href="#" onclick="requestAjax('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'agregarArticulo=ok', evalScripts: true});cotizarRecotizarReservarForm.codigoArticulo.focus();">Agregar</html:link>
																		</div>
																	</td>
																	<td>
																		<div id="botonD">
																			<html:link styleClass="actualizarD" href="#" onclick="requestAjax('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok'});">Actualizar</html:link>
																		</div>
																	</td>
																	<td>
																		<div id="botonD">
																			<html:link styleClass="eliminarD" href="#" onclick="requestAjax('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'eliminarArticulos=ok'});">Eliminar</html:link>
																		</div>
																	</td>
																</tr>
															</table>
														</td>
													</logic:equal>
												</logic:empty>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<tr>
									<td>
										<div id="detalleCanasto">
											<table width="100%" border="0" cellpadding="0" cellspacing="0">
												<tr>
													<td>
														<table cellpadding="0" cellspacing="0" width="98%" align="left" class="tabla_informacion">
															<tr class="tituloTablas">
																<td align="center" class="columna_contenido" width="3%"><html:checkbox property="checkSeleccionarTodo" value="t" onclick="activarDesactivarTodo(this, cotizarRecotizarReservarForm.checksSeleccionar);"/></td>
																<td align="center" class="columna_contenido" width="3%">No</td>
																<td align="center" class="columna_contenido" width="14%">C&oacute;digo barras</td>
																<td align="center" class="columna_contenido" width="29%">Art&iacute;culo</td>
																<td align="center" class="columna_contenido" width="6%" >Cant.</td>
																<td align="center" class="columna_contenido" width="7%">Stock</td>
																<%--<td align="center" class="columna_contenido" width="6%">PESO KG.</td>--%>
																<td align="center" class="columna_contenido" width="7%">V. unit.</td>
																<td align="center" class="columna_contenido" width="7%">V. unit.IVA
																	<logic:equal name="preciosActualizables" value="${estadoActivo}">
																		<html:link href="#" onclick="requestAjax('detalleCanasta.do',['detalleCanasto'],{parameters: 'actualizarPreciosUnitarios=ok'});"><img src="images/icon_refresh.gif" border="0" alt="modificar precios"></html:link>
																	</logic:equal>
																</td>
																<td align="center" class="columna_contenido" width="7%">V. caja</td>
																<td align="center" class="columna_contenido" width="7%">V. mayorista</td>
																<td class="columna_contenido" width="3%" align="center">IVA</td>
																<td align="center" class="columna_contenido" width="7%">V. total</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td>
														<div id="div_listado" style="width:100%;height:360px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
															<table cellpadding="0" cellspacing="0" width="98%" align="left" class="tabla_informacion">
																<logic:notEmpty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.articuloRelacionCol">
																	<logic:iterate name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.articuloRelacionCol" id="receta" indexId="indiceRegistro">
																		<!-- control del estilo para el color de las filas -->
																		<bean:define id="residuo" value="${indiceRegistro % 2}"/>
																		<c:set var="clase" value="blanco10"/>
																		<logic:notEqual name="residuo" value="0">
																			<c:set var="clase" value="grisClaro10"/>
																		</logic:notEqual>
																		<c:set var="unidadManejo" value="${receta.articuloRelacion.unidadManejo}"/>
																		<logic:equal name="receta" property="articuloRelacion.habilitadoPrecioCaja" value="${true}">
																			<c:set var="unidadManejo" value="${receta.articuloRelacion.unidadManejoPrecioCaja}"/>
																		</logic:equal>
																		<%-- control de estilos para indicar el status completo del artículo en el SIC --%>
																		<c:choose>
																			<%-- DE BAJA EN EL SIC --%>
																			<c:when test="${receta.articuloRelacion.npEstadoArticuloSIC == articuloDeBaja}">
																				<c:set var="clase" value="verdeClaro10"/>
																				<c:set var="title_fila" value="art&iacute;culo dado de baja en el SIC"/>
																			</c:when>
																			<c:otherwise>
																				<c:choose>
																					<%-- SIN ALCANCE --%>
																					<c:when test="${receta.articuloRelacion.npAlcance == estadoInactivo}">
																						<c:set var="clase" value="rojoClaro10"/>
																						<c:set var="title_fila" value="art&iacute;culo sin ALCANCE"/>
																					</c:when>
																					<c:otherwise>
																						<c:choose>
																							<%-- PROBLEMAS DE STOCK --%>
																							<c:when test="${receta.articuloRelacion.npEstadoStockArticulo == estadoInactivo}">
																								<c:set var="clase" value="naranjaClaro10"/>
																								<c:set var="title_fila" value="art&iacute;culo sin STOCK"/>
																							</c:when>
																							<c:otherwise>
																								<c:choose>
																									<%-- INACTIVO EN EL SIC --%>
																									<c:when test="${receta.articuloRelacion.npEstadoArticuloSIC == estadoInactivo}">
																										<c:set var="clase" value="celesteClaro10"/>
																										<c:set var="title_fila" value="art&iacute;culo inactivo en el SIC"/>
																									</c:when>
																								</c:choose>
																							</c:otherwise>
																						</c:choose>
																					</c:otherwise>
																				</c:choose>
																			</c:otherwise>
																		</c:choose>
																		<%-- control del color para los implementos --%>
																		<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																			<c:set var="clase" value="violetaClaro10"/>
																		</logic:equal>
																		<bean:define id="fila" value="${indiceRegistro + 1}"/>
																		<c:set var="pesoVariable" value=""/>
																		<logic:equal name="receta" property="articuloRelacion.tipoCalculoPrecio" value="${tipoArticuloPavo}">
																			<c:set var="pesoVariable" value="${estadoActivo}"/>
																		</logic:equal>
																		<logic:equal name="receta" property="articuloRelacion.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																			<c:set var="pesoVariable" value="${estadoActivo}"/>
																		</logic:equal>
																		<tr class="${clase}">
																			<td align="center" class="fila_contenido columna_contenido" width="3%">
																				<logic:notEqual name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																					<html:multibox property="checksSeleccionar" value="${indiceRegistro}"/>
																				</logic:notEqual>
																				<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																					<!-- condiciones que se deben tomar en cuenta para un implemento -->
																					<logic:equal name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="estadoDetallePedidoDTO.estadoCanCotVacio" value="${estadoActivo}">
																						<!-- si es un canasto de cotizaciones, se puede eliminar -->
																						<html:multibox property="checksSeleccionar" value="${indiceRegistro}"/>
																					</logic:equal>
																					<logic:notEqual name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="estadoDetallePedidoDTO.estadoCanCotVacio" value="${estadoActivo}">
																						<!-- si no es un canasto de cotizaciones, solo se puede eliminar si se agrego el implemento -->
																						<logic:notEmpty name="receta" property="npNuevo">
																							<html:multibox property="checksSeleccionar" value="${indiceRegistro}"/>
																						</logic:notEmpty>
																						<logic:empty name="receta" property="npNuevo">&nbsp;</logic:empty>
																					</logic:notEqual>
																				</logic:equal>
																			</td>
																			<td align="center" class="fila_contenido columna_contenido" width="3%"><bean:write name="fila"/></td>
																			<td align="center" class="fila_contenido columna_contenido" width="14%"><bean:write name="receta" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
																			<td align="left" class="fila_contenido columna_contenido" width="29%">
																				<table cellpadding="0" cellspacing="0" width="100%">
																					<tr>
																						<td><bean:write name="receta" property="articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="receta" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																						<logic:equal name="pesoVariable" value="${estadoActivo}">
																							<td align="right"><img title="peso variable" src="images/balanza.gif" border="0"></td>
																						</logic:equal>
																					</tr>
																				</table>
																			</td>
																			<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
																				<td align="center" class="fila_contenido columna_contenido" width="6%">
																					<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																						<html:text property="vectorcantidadCanasta" value="${receta.cantidad}" styleClass="textNormal" size="5" maxlength="3" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok'});"/>
																					</logic:equal>
																					<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																						<html:hidden property="vectorcantidadCanasta" value="${receta.cantidadArticulo}" write="true"/>
																					</logic:notEqual>
																				</td>
																			</logic:empty>
																			<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
																				<td align="center" class="fila_contenido columna_contenido" width="6%">
																					<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																						<bean:write name="receta" property="cantidad"/>
																						<html:hidden property="vectorcantidadCanasta" value="${receta.cantidad}"/>
																					</logic:equal>
																					<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																						<html:hidden property="vectorcantidadCanasta" value="${receta.cantidad}" write="true"/>
																					</logic:notEqual>
																				</td>
																			</logic:notEmpty>
																			<!--COLUMNA STOCK -->
																			<td align="center" class="fila_contenido columna_contenido" width="7%">
																				<logic:notEmpty name="receta" property="articuloRelacion.npStockArticulo">
																					<bean:write name="receta" property="articuloRelacion.npStockArticulo"/>
																				</logic:notEmpty>
																				
																				<!-- ICONO ESTADO DE AUTORIZACION DE STOCK -->
																				<logic:notEmpty name="receta" property="articuloRelacion.npEstadoAutorizacion">		
																				<c:set var="autorizacionStock" value="0"/>
																					<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.pendiente">
										                                         		<c:set var="autorizacionStock" value="1"/>
										                                            </smx:equal>                                                                            
										                                           	<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.solicitada">	
										                                            	<c:set var="autorizacionStock" value="2"/>
										                                            </smx:equal>
										                                           	<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.aprobada">	
										                                          		<c:set var="autorizacionStock" value="3"/>
										                                			</smx:equal>
										                                     		<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.rechazada">	
										                                              	<c:set var="autorizacionStock" value="4"/>
										                                  			</smx:equal>
										                               				<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.utilizada">	
										                                            	<c:set var="autorizacionStock" value="5"/>
										                                    		</smx:equal>
										                                			<smx:equal name="receta" property="articuloRelacion.npEstadoAutorizacion" valueKey="ec.com.smx.sic.sispe.estado.autorizacion.caducada">	
										                                   				<c:set var="autorizacionStock" value="6"/>
										                                          	</smx:equal>
										                                    	    <c:if test="${autorizacionStock == 0}">
											                                        	&nbsp;
											                                        </c:if>                                                                        
											                                        <c:if test="${autorizacionStock == 1}">
											                                        	<img src="images/autPendiente.png" border="0" title="Autorizaci&oacute;n de Stock Pendiente">
											                                        </c:if>
											                                        <c:if test="${autorizacionStock == 2}">			                                        	
											                                         	<img src="images/autSolicitada.png" border="0" title="Autorizaci&oacute;n de Stock Solicitada">			                                        	
											                                        </c:if>
											                                        <c:if test="${autorizacionStock == 3}">
											                                        	<img src="images/autAprobada.png" border="0" title="Autorizaci&oacute;n de Stock Aprobada">			                 
											                                        </c:if>
											                                        <c:if test="${autorizacionStock == 4}">
											                                        	<img src="images/autRechazada.png" border="0" title="Autorizaci&oacute;n de Stock Rechazada">
											                                        </c:if>
											                                        <c:if test="${autorizacionStock == 5}">
											                                        	<img src="images/autUtilizada.png" border="0" title="Autorizaci&oacute;n de Stock Utilizada en otro momento">
											                                        </c:if>
											                                        <c:if test="${autorizacionStock == 6}">
											                                         	<img src="images/autCaducada.png" border="0" title="Autorizaci&oacute;n de Stock Caducada">
											                                       	</c:if>																					
																				</logic:notEmpty>
																				<logic:empty name="receta" property="articuloRelacion.npStockArticulo">&nbsp;</logic:empty>
																			</td>
																			<%--<td class="columna_contenido fila_contenido" align="center" width="6%">
																				<logic:equal name="pesoVariable" value="${estadoActivo}">
																					<bean:define id="cantidad" name ="receta" property="cantidadArticulo"/>
																					<bean:define id="peso" name ="receta" property="articuloRelacion.pesoAproximado"/>
																					<bean:define id="pesoTotal" value="${cantidad * peso}"/>
																					<bean:write name="pesoTotal" formatKey="formatos.numeros"/>
																				</logic:equal>
																				<logic:notEqual name="pesoVariable" value="${estadoActivo}">
																					<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
																				</logic:notEqual>
																			</td>--%>
																			<td class="fila_contenido columna_contenido" width="7%" align="right">
																			
																				<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBase}"/>
																					<html:hidden property="vectorPrecioItem" value="${receta.articuloRelacion.precioBase}"/>
																				</logic:empty>
																				<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																					<logic:notEqual name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																						<html:text property="vectorPrecioItem" value="${receta.articuloRelacion.precioBase}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok'});"/>
																					</logic:notEqual>
																					<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBase}"/>
																						<html:hidden property="vectorPrecioItem" value="${receta.precioBase}"/>
																					</logic:equal>
																				</logic:notEmpty>
																			</td>
																			<td class="fila_contenido columna_contenido" width="7%" align="right">
																			
																				<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																					<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/>
																					<html:hidden property="vectorPrecioItem" value="${receta.articuloRelacion.precioBaseImp}"/>
																				</logic:empty>
																				<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitario">
																					<logic:notEqual name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																						<html:text property="vectorPrecioItem" value="${receta.articuloRelacion.precioBaseImp}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'actualizarCanasto=ok'});"/>
																					</logic:notEqual>
																					<logic:equal name="receta" property="articuloRelacion.npImplemento" value="${estadoActivo}">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/>
																						<html:hidden property="vectorPrecioItem" value="${receta.precioBaseImp}"/>
																					</logic:equal>
																				</logic:notEmpty>
																			</td>
																			
																			<logic:notEmpty name="ec.com.smx.sic.sispe.local.activo.precioMayorista">
																				<td class="fila_contenido columna_contenido textoAzul10" width="7%" align="center">
																					<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																				</td>
																				<c:if test="${receta.articuloRelacion.npHabilitarPrecioMayorista == estadoActivo && receta.articuloRelacion.precioMayorista > 0}">
																					<td class="fila_contenido columna_contenido" width="7%" align="right">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioMayoristaImp}"/>
																					</td>
																				</c:if>
																				<c:if test="${receta.articuloRelacion.npHabilitarPrecioMayorista != estadoActivo}">
																					<td class="fila_contenido columna_contenido textoAzul10" width="7%" align="center">
																						<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																					</td>
																				</c:if>
																			</logic:notEmpty>
																			<logic:empty name="ec.com.smx.sic.sispe.local.activo.precioMayorista">
																				
																				<c:if test="${receta.articuloRelacion.habilitadoPrecioCaja == true && receta.articuloRelacion.precioCaja > 0}">
																					<td class="fila_contenido columna_contenido" width="7%" align="right">
																						<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioCajaImp}"/>
																					</td>
																				</c:if>
																				<c:if test="${receta.articuloRelacion.habilitadoPrecioCaja == false}">
																					<td class="fila_contenido columna_contenido textoAzul10" width="7%" align="center">
																						<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																					</td>
																				</c:if>
																				<td class="fila_contenido columna_contenido textoAzul10" width="7%" align="center">
																					<bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
																				</td>
																			</logic:empty>
																			
																			<td align="center" class="fila_contenido columna_contenido" width="3%">
																				
																				<c:if test="${receta.articuloRelacion.aplicaImpuestoVenta }">
																					<img src="images/tick.png" border="0">
																				</c:if>
																				<c:if test="${!receta.articuloRelacion.aplicaImpuestoVenta }">
																					<img src="images/x.png" border="0">
																				</c:if>
																				
																			</td>
																			<td align="right" class="fila_contenido columna_contenido" width="7%">
																				<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioTotalIMP}"/>
																			</td>
																		</tr>
																	</logic:iterate>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO" property="articuloDTO.articuloRelacionCol">
																	<tr>
																		<td width="3%" align="center"><img src="images/info_16.gif"></td>
																		<td align="left" class="textoNegro11">Ingrese los art&iacute;culos para el pedido</td>
																	</tr>
																</logic:empty>
															</table>
														</div>
													</td>
												</tr>
												<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
												<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
													<tr>
														<td class="columna_contenido celesteFosforescente">
															<c:set var="descripcionArticulo" value=""/>
															<c:set var="stockArticulo" value=""/>
															<c:set var="descripcionProblema" value=""/>
															<logic:notEmpty name="ec.com.smx.sic.sispe.articulo.problemasStock">
																<c:set var="descripcionArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/></c:set>
																<c:set var="stockArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="npStockArticulo"/></c:set>
																<c:set var="descripcionProblema" value="Problema de stock"/>
															</logic:notEmpty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.articulo.problemasAlcance">
																<c:set var="descripcionArticulo"><bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/></c:set>
																<c:set var="descripcionProblema" value="Problema de alcance"/>
															</logic:notEmpty>
															<table border="0" cellpadding="0" cellspacing="0" width="98%" align="left">
																<tr>
																	<td align="center" width="3%"><img src="images/flechader.gif" border="0"></td>
																	<td align="center" width="3%">&nbsp;</td>
																	<td align="center" width="30%">C&oacute;digo barras:&nbsp;<html:text property="codigoArticulo" size="17" styleClass="textNormal" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'agregarArticulo=ok', evalScripts: true});"/></td>
																	<td align="center" width="17%">Cantidad:&nbsp;<smx:text property="cantidadArticulo" size="5" maxlength="3" styleClass="textNormal" styleError="campoError" value="1" onkeypress="requestAjaxEnter('detalleCanasta.do',['mensajes','cabezeraCanasto','detalleCanasto'],{parameters: 'agregarArticulo=ok', evalScripts: true});"/></td>
																	<td align="center" width="25%" class="textoRojo10">&nbsp;
																		<logic:notEmpty name="descripcionArticulo">
																			<bean:write name="descripcionArticulo"/>
																		</logic:notEmpty>
																		<logic:empty name="descripcionArticulo">&nbsp;</logic:empty>
																	</td>
																	<td width="6%">&nbsp;</td>
																	<td colspan="3" align="left">
																		<logic:notEmpty name="descripcionProblema">
																			<table cellpadding="0" cellspacing="0">
																				<tr>
																					<td><img src="images/advertencia_16.gif" border="0">&nbsp;</td>
																					<td>
																						<bean:write name="descripcionProblema"/>&nbsp;
																						<logic:notEmpty name="stockArticulo">
																							(Stock: <bean:write name="stockArticulo"/>)
																						</logic:notEmpty>
																					</td>
																				</tr>
																			</table>
																		</logic:notEmpty>
																		<logic:empty name="descripcionProblema">&nbsp;</logic:empty>
																	</td>
																</tr>
															</table>
															<script language="javascript">
																document.getElementById('div_listado').scrollTop=document.getElementById('div_listado').scrollHeight;Field.activate('codigoArticulo');
															</script>
														</td>
													</tr>
												</logic:equal>
												</logic:empty>
											</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
</TABLE>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>