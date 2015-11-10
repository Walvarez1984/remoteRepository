<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:insert page="/include/top.jsp"/>
<bean:define id="tipoPedidoNormal" name="ec.com.smx.sic.sispe.tipoPedido.normal"/>
<bean:define id="tipoPedidoDevolucion" name="ec.com.smx.sic.sispe.tipoPedido.devolucion"/>
<bean:define id="activoA"><bean:message key="ec.com.smx.sic.sispe.estado.activo"/></bean:define>
<bean:define id="inactivoA"><bean:message key="ec.com.smx.sic.sispe.estado.inactivo"/></bean:define>
<bean:define id="anulado"><bean:message key="ec.com.smx.sic.sispe.estado.anulado1"/></bean:define>
<bean:define id="cotizado"><bean:message key="ec.com.smx.sic.sispe.estado.cotizado1"/></bean:define>
<bean:define id="recotizado"><bean:message key="ec.com.smx.sic.sispe.estado.recotizado1"/></bean:define>
<bean:define id="cotCaduca"><bean:message key="ec.com.smx.sic.sispe.estado.coticaduca1"/></bean:define>
<bean:define id="opTodos"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<c:set var="verDetalleNormal" value="0"/>

<html:form action="crearCotizacion" method="post" enctype="multipart/form-data">
<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<%--<html:form action="estadoPedido" method="post" focus="campoBusqueda">--%>	
		<html:hidden property="ayuda" value=""/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../../confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones">
						<tiles:insert page="/servicioCliente/autorizacion/popUpColaAutorizaciones.jsp">
							<tiles:put name="vtformAction" value="crearCotizacion"/>                            
						</tiles:insert>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="../../confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="crearCotizacion"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">
						<tiles:insert page="/confirmacion/popUpConfirmacionAux.jsp">
							<tiles:put name="vtformAction" value="consolidacion"/>
						</tiles:insert>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.banderaModal">
						<script language="javascript">ocultarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="./images/busqueda_pedido.gif" border="0"></img></td>
						<td height="35" valign="middle">Consolidación de pedidos</td>
						<td align="right" valign="top">
							<table border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td>
										<div id="botonA">										
											<html:link href="#" styleClass="consolidarA" onclick="requestAjax('crearCotizacion.do',['mensajes','div_pagina','pregunta'],{parameters: 'guardarConsolidacion=ok &posicionScroll='+document.getElementById('div_listado').scrollTop , evalScripts:true});">Consolidar</html:link>									
										</div>
									</td>
									<td>
										<logic:empty name="ec.com.smx.sic.sispe.activar.atras">
											<bean:define id="exit" value="exit"/>
	                                        <div id="botonA">	
	                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
	                                        </div>
                                        </logic:empty>
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.activar.atras">
	                                        <bean:define id="salir" value="salir"/>
		                                    <div id="botonA">
												<html:link action="crearCotizacion" paramId="ayuda" paramName="salir" styleClass="atrasA">Atras</html:link>
											</div>
										</logic:notEmpty>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<%--Cuerpo--%>
		<tr>
			<td align="center" valign="top">
				<table border="0" class="textoNegro12" width="99%" align="center" cellspacing="0" cellpadding="0">
					<tr><td height="7px" colspan="3"></td></tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos fila_contenido columna_contenido" id="izquierda" align="center" width="25%">
							<div style="width:100%;height:490px;">
								<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
							</div>
						</td>
						<td width="1px">&nbsp;</td>
						<td id="divisor" height="${alto_divs}">
							<span style="display:block" id="img_ocultar">
								<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
							</span>
							<span style="display:none" id="img_mostrar">
								<a href="#"><img src="./images/spliter_der.png" border="0"></a>
							</span>
						</td>
						<td width="1px">&nbsp;</td>
						<bean:define id="seccionActual" value=""/>
						<bean:define id="seccionPedidosConsolidados" value=""/>
						<logic:notEmpty name="cotizarRecotizarReservarForm" property="datosConsolidados">
							<logic:iterate name="cotizarRecotizarReservarForm" property="datosConsolidados" id="vistaPedidoDTO" indexId="indicePedido">
								<c:if test="${indicePedido!=0}">
									<bean:define id="seccionActual" value="${seccionActual},'td_consolidado_${indicePedido}'"/>
									<bean:define id="seccionPedidosConsolidados" value="${seccionPedidosConsolidados},'marcoC${indicePedido}'"/>
								</c:if>
								<c:if test="${indicePedido==0}">
									<bean:define id="seccionActual" value="'td_consolidado_${indicePedido}'"/>
									<bean:define id="seccionPedidosConsolidados" value="'marcoC${indicePedido}'"/>
								</c:if>
							</logic:iterate>
						</logic:notEmpty>
						<%-- Datos --%>
						<td class="datos columna_contenido_der" width="100%" id="derecha">
							<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
								<tr>
									<td>
										<div id="cabeceraConsolidados" style="height:250px;">
											<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
												<%--Titulo de los datos--%>
												<tr>
													<td class="fila_titulo">
													<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
														<tr>
															<td ><img src="./images/detalle_pedidos24.gif" border="0"/></td>
															<td height="28" width="100%">&nbsp;
																Pedidos consolidados
															</td>
															<td >
																<div id="botonD">
																	<html:link styleClass="eliminarD" href="#" title="Eliminar pedidos (Desconsolidar)" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes',${seccionPedidosConsolidados},'resultadosBusqueda','pregunta'],{parameters: 'eliminarPedidosConsolidados=ok', evalScripts: true});">Eliminar</html:link>
																</div>
															</td>
															<td>&nbsp;</td>
														</tr>
													</table>
													</td>
												</tr>
												<tr>
													<td>
														<div id="resultadosPedidosConsolidados">
															<table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
																<logic:notEmpty name="cotizarRecotizarReservarForm" property="datosConsolidados">
																	<tr>
																		<td>
																			<table border="0" cellspacing="0" width="97%" cellpadding="1">
																				<tr>
																					<td></td>
																				</tr>
																				<tr class="tituloTablas">
																					<td class="columna_contenido" width="6%" align="center">&nbsp;</td>
																					<td class="columna_contenido" width="4%" align="center">No</td>
																					<td class="columna_contenido" width="30%" align="left"></td>
																					<td class="columna_contenido" width="47%" align="left">&nbsp;</td>
																					<td class="columna_contenido" width="10%" align="left">Consolidar</td>	
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<div id="div_listado" style="width:100%;height:200px;overflow:auto">
																				<table border="0" cellspacing="0" width="97%" cellpadding="0">
																					<logic:iterate name="cotizarRecotizarReservarForm" property="datosConsolidados" id="vistaPedidoDTO" indexId="indicePedido">
																						<bean:define id="indiceGlobal" value="${indicePedido}"/>
																						<bean:define id="fila" value="${indiceGlobal + 1}"/>
																						<%-- control del estilo para el color de las filas --%>
																						<bean:define id="residuo" value="${indicePedido % 2}"/>
																						<logic:equal name="residuo" value="0">
																							<bean:define id="colorBack" value="blanco10"/>
																						</logic:equal>
																						<logic:notEqual name="residuo" value="0">
																							<bean:define id="colorBack" value="grisClaro10"/>
																						</logic:notEqual>
																						<c:if test="${vistaPedidoDTO.codigoTipoPedido == tipoPedidoNormal || vistaPedidoDTO.codigoTipoPedido == tipoPedidoDevolucion}">
																							<c:set var="verDetalleNormal" value="1"/>
																						</c:if>
																						<c:set var="displayC1" value="none"/>
					                                                                    <c:set var="displayC2" value="block"/>
																						<tr>
																							<td id="td_consolidado_${indicePedido}">
																								<c:if test="${cotizarRecotizarReservarForm.pedidosValidados[indicePedido]=='1'}">
																									<bean:define id="colorBack" value="rojoClaroA10"/>
																								</c:if>
																								<table border="0" cellpadding="0" cellspacing="0" width="100%">
																									<tr class="${colorBack}">
																										<td class="columna_contenido fila_contenido" width="6%" align="center">
																											<logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="CSD">
																												<div id="desplegarC${indicePedido}" style="display:${displayC1}">
																													<a title="Mostrar pedidos consolidados" href="javascript:hide(['desplegarC${indicePedido}']);show(['plegarC${indicePedido}','marcoC${indicePedido}']);">
																														<html:img src="images/desplegar.gif" border="0"/>
																													</a>
																												</div>
																												<div id="plegarC${indicePedido}" style="display:${displayC2}">
																													<a href="javascript:show(['desplegarC${indicePedido}']);hide(['plegarC${indicePedido}','marcoC${indicePedido}']);">
																														<html:img src="images/plegar.gif" border="0"/>
																													</a>
																												</div>
																											</logic:equal>
																											<logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="ANU">&nbsp;</logic:notEqual>
																										</td>
																										<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="fila"/></td>
																										<td width="30%" class="columna_contenido fila_contenido" align="left">
																											<%--<logic:equal name="verDetalleNormal" value="0" >
																												Pedido consolidado
																											</logic:equal>
																											<logic:equal name="verDetalleNormal" value="1">
																												Pedido consolidado
																											</logic:equal> --%>
																											<%--<html:link title="Click aqui para ver el pedido consolidado" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'linkConsolidadoGeneral=${indicePedido}',evalScripts:true});">Pedido consolidado</html:link>--%>
																											<html:link title="Click aqui para ver el pedido consolidado" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','div_pagina','pie_pedido'],{parameters: 'linkConsolidadoGeneral=${indicePedido}&posicionScroll='+document.getElementById('div_listado').scrollTop,evalScripts:true});">Pedido consolidado</html:link>
																										</td>
																										<td width="47%" class="columna_contenido fila_contenido" >
																											&nbsp;
																										</td>
																										<td width="10%" class="columna_contenido fila_contenido columna_contenido_der">
																											<div id="seccionImagenes_${indicePedido}">
																												<center>
																												<c:if test="${cotizarRecotizarReservarForm.pedidosValidados[indicePedido]=='0'}">
																														<a href="javascript:;" class="linkAzul" title="Click aqui para consolidar su pedido" 
																															onclick="requestAjax('crearCotizacion.do', ['mensajes',${seccionActual}],{parameters:'validarPedido=${vistaPedidoDTO.id.codigoPedido}',evalScripts:true});popWait('div_wait');">
																																																																												
																															<img border="0" src="<%=request.getContextPath()%>/images/aceptarDes.gif"  />
																														</a>
																												</c:if>
																												<c:if test="${cotizarRecotizarReservarForm.pedidosValidados[indicePedido]=='1'}">
																														<a href="javascript:;" class="linkAzul" title="Click aqui para anular la consolidación"
																														onclick="requestAjax('crearCotizacion.do', [${seccionActual}],{parameters:'validarPedido=${vistaPedidoDTO.id.codigoPedido}',evalScripts:true} ,'${vformName}');popWait('div_wait');">
																														<img border="0" src="<%=request.getContextPath()%>/images/aceptar16.gif"   />
																														</a>
																					  							</c:if>	
																												</center>
																											</div>
																										</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																						<tr>
																							<td align="center">
																								<div id="marcoC${indicePedido}" style="display:${displayC2}">
																									<logic:notEmpty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
																										<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																											<tr class="tituloTablasCeleste">
																												<td width="3%" class="fila_contenido_negro columna_contenido_der_negro" align="center">&nbsp;</td>
																												<td width="11%" class="fila_contenido_negro columna_contenido_der_negro" align="center">No pedido</td>
																												<%--<td width="4%" class="fila_contenido_negro columna_contenido_der_negro" align="center">No Res.</td>--%>
																												<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Fecha estado</td>
																												<td width="17%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Contacto empresa</td>
																												<td width="5%" class="fila_contenido_negro columna_contenido_der_negro" align="center">V. total</td>
																												<td width="5%" class="fila_contenido_negro columna_contenido_der_negro" align="center">V. abono</td>
																												<td width="5%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Estado</td>
																											</tr>
																											<logic:iterate name="vistaPedidoDTO" property="vistaPedidoDTOCol" id="vistaPedidoDTO2" indexId="indicePedido2">
																												<%-- control del estilo para el color de las filas --%>
																												<bean:define id="residuoR" value="${indicePedido2 % 2}"/>
																												<logic:equal name="residuoR" value="0">
																													<bean:define id="colorBack2" value="blanco10"/>
																												</logic:equal>
																												<logic:notEqual name="residuoR" value="0">
																													<bean:define id="colorBack2" value="amarilloClaro10"/>
																												</logic:notEqual>
																												<tr class="${colorBack2} textoNegro10">
																													<td class="columna_contenido fila_contenido" width="3%" align="center">
																														<html:multibox property="opSeleccionPedidosConsolidados" value="${indicePedido2}-${vistaPedidoDTO2.id.codigoPedido}"/>
																													</td>
																													<td align="center" class="columna_contenido fila_contenido">
																														<%--<bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/>--%>
																														<html:link title="Click aqui para ver el pedido" href="#" onclick="requestAjax('detalleEstadoPedido.do',['mensajes','div_pagina','pie_pedido'],{parameters: 'indice=${indicePedido}&listaConsolidados=${indicePedido2}&posicionScroll='+document.getElementById('div_listado').scrollTop,evalScripts:true});"><bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/></html:link>
																													</td>
																													<%--<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="vistaPedidoDTO2" property="llaveContratoPOS"/></td>--%>
																													<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="vistaPedidoDTO2" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
																													<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="vistaPedidoDTO2" property="contactoEmpresa"/></td>
																													<td align="right" class="columna_contenido fila_contenido">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.totalPedido}"/></td>
																													<td align="right" class="columna_contenido fila_contenido">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.abonoPedido}"/></td>
																													<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="vistaPedidoDTO2" property="descripcionEstado"/></td>
																												</tr>
																											</logic:iterate>
																										</table>
																									</logic:notEmpty>
																								</div>
																							</td>
																						</tr>
																					</logic:iterate>
																				</table>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td  height="5px"></td>
																	</tr>
																</logic:notEmpty>
																<logic:empty name="cotizarRecotizarReservarForm" property="datosConsolidados">
																	<tr>
																		<div id="div_listado" >																	
																			<td colspan="8" class="textoNegro11">No existen pedidos asignados</td>
																		</div>
																	</tr>
																</logic:empty>
															</table>
														</div>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
							</table>
							<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" height="7px">
								<tr>
									<td></td>
								</tr>
							</table>
							<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
								<tr>
									<td>
										<div style="width:100%;height:230px;">
											<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
												<%--Titulo de los datos--%>
												<tr>
													<td class="fila_titulo">
													<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
														<tr>
															<td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
															<td height="28" width="100%">&nbsp;
																Lista de pedidos por consolidar
															</td>
															<td>
																<div id="botonD">
																	<html:link styleClass="agregarD" href="#" title="Agregar pedidos (Consolidar)" onclick="javascript:requestAjax('crearCotizacion.do',['mensajes','resultadosPedidosConsolidados','resultadosBusqueda','div_pagina'],{parameters: 'agregarPedidosConsolidados=ok&posicionScroll='+document.getElementById('div_listado').scrollTop, evalScripts: true});">Agregar</html:link>
																</div>
															</td>
															<td>&nbsp;</td>
														</tr>
													</table>
													</td>
												</tr>
												<tr>
													<td>
														<div id="resultadosBusqueda">
															<table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
																<logic:notEmpty name="cotizarRecotizarReservarForm" property="datos">
																	<tr>
																		<td>
																			<table cellpadding="0" cellspacing="0" width="100%">
																				<tr>
																					<td align="right">
																						<smx:paginacion start="${cotizarRecotizarReservarForm.start}" range="${cotizarRecotizarReservarForm.range}" results="${cotizarRecotizarReservarForm.size}" styleClass="textoNegro11" url="crearCotizacion.do?pedidos=ok"  requestAjax="'mensajes','resultadosBusqueda'"/>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<table border="0" cellspacing="0" width="97%" cellpadding="1">
																				<tr class="tituloTablas">
																					<td class="columna_contenido" width="3%" align="center">&nbsp;</td>
																					<td class="columna_contenido" width="3%" align="center"><html:checkbox title="Seleccionar todos" property="opSeleccionTodos" value="${opTodos}" onclick="activarDesactivarTodo(this, cotizarRecotizarReservarForm.opSeleccionPedidosConsolidar);"/></td>
																					<td class="columna_contenido" width="4%" align="center">No</td>
																					<td class="columna_contenido" width="16%" align="center">No PEDIDO</td>
																					<%--<td class="columna_contenido" width="7%" align="center">No RES.</td>--%>
																					<td class="columna_contenido" width="14%" align="center">FECHA ESTADO</td>
																					<td class="columna_contenido" width="19%" align="center">CONTACTO EMPRESA</td>
																					<td class="columna_contenido" width="8%" align="center">VALOR TOTAL</td>
																					<td class="columna_contenido" width="8%" align="center">VALOR ABONADO</td>
																					<td class="columna_contenido" width="8%" align="center">ESTADO</td>
																					<td class="columna_contenido" width="6%" align="center">ACTUAL</td>																		
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr>
																		<td>
																			<div id="div_listadoPedidosConsolidar" style="width:100%;height:160px;overflow:auto">
																				<table border="0" cellspacing="0" width="97%" cellpadding="1">
																					<logic:iterate name="cotizarRecotizarReservarForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
																						<bean:define id="indiceGlobal" value="${indicePedido + cotizarRecotizarReservarForm.start}"/>
																						<bean:define id="fila" value="${indiceGlobal + 1}"/>
																						<%-- control del estilo para el color de las filas --%>
																						<bean:define id="residuo" value="${indicePedido % 2}"/>
																						<logic:equal name="residuo" value="0">
																							<bean:define id="colorBack" value="blanco10"/>
																						</logic:equal>
																						<logic:notEqual name="residuo" value="0">
																							<bean:define id="colorBack" value="grisClaro10"/>
																						</logic:notEqual>
																						<c:if test="${vistaPedidoDTO.codigoTipoPedido == tipoPedidoNormal || vistaPedidoDTO.codigoTipoPedido == tipoPedidoDevolucion}">
																							<c:set var="verDetalleNormal" value="1"/>
																						</c:if>
																						<c:set var="display1" value="block"/>
					                                                                    <c:set var="display2" value="none"/>
					                                                                    <logic:notEmpty name="indicePedidoPrincipal">
					                                                                        <logic:equal name="indicePedidoPrincipal" value="${indicePedido}">
					                                                                            <c:set var="display1" value="none"/>
					                                                                            <c:set var="display2" value="block"/>
					                                                                        </logic:equal>
					                                                                    </logic:notEmpty>
																						<tr class="${colorBack}">
																						<td class="columna_contenido fila_contenido" width="3%" align="center">
																							<logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="CSD">
																										<div id="desplegar${indicePedido}" style="display:${display1}">
																											<a title="Mostrar pedidos consolidados" href="javascript:hide(['desplegar${indicePedido}']);show(['plegar${indicePedido}','marco${indicePedido}']);">
																												<html:img src="images/desplegar.gif" border="0"/>
																											</a>
																										</div>
																										<div id="plegar${indicePedido}" style="display:${display2}">
																											<a href="javascript:show(['desplegar${indicePedido}']);hide(['plegar${indicePedido}','marco${indicePedido}']);">
																												<html:img src="images/plegar.gif" border="0"/>
																											</a>
																										</div>
																							</logic:equal>
																							<logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="ANU">&nbsp;</logic:notEqual>
																						</td>
																							<td class="columna_contenido fila_contenido" width="3%" align="center">
																								<html:multibox property="opSeleccionPedidosConsolidar" value="${indicePedido}-${vistaPedidoDTO.id.codigoPedido}"/>
																							</td>
																							<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="fila"/></td>
																							<td width="16%" class="fila_contenido columna_contenido" align="center">
																								<logic:equal name="verDetalleNormal" value="0" >
																									<html:link title="Detalle del pedido" onclick="popWait();" action="detalleEstadoPedidoEspecial.do?detallePedido=${indicePedido}"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
																								</logic:equal>
																								<logic:equal name="verDetalleNormal" value="1">
																									<html:link title="Click aqui para ver el pedido" action="detalleEstadoPedido?consolidacion=ok" paramId="indice" paramName="indicePedido" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
																								</logic:equal>
																							</td>
																							<%--<td class="columna_contenido fila_contenido" width="7%" align="center">
																								<logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
																									<bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/>
																								</logic:notEmpty>
																								<logic:empty name="llaveContratoPOS">&nbsp;</logic:empty>
																							</td>--%>
																							<td class="columna_contenido fila_contenido" width="14%" align="center">&nbsp;<bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
																							<td class="columna_contenido fila_contenido" width="19%" align="left">&nbsp;<bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
																							<td class="columna_contenido fila_contenido" width="8%" align="right">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
																							<td class="columna_contenido fila_contenido" width="8%" align="right">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.abonoPedido}"/></td>
																							<td class="columna_contenido fila_contenido" width="8%" align="center">&nbsp;<bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
																							<td class="columna_contenido fila_contenido columna_contenido_der" width="6%" align="center">&nbsp;<bean:write name="vistaPedidoDTO" property="estadoActual"/></td>
																						</tr>
																						<tr>
																							<td colspan="10" align="center">
																								<div id="marco${indicePedido}" style="display:${display2}">
																									<logic:notEmpty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
																										<b align="center" class="textoAzul10">Pedidos a consolidar</b>
																										<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																											<tr class="tituloTablasCeleste">
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">&nbsp;</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No pedido</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No reserva</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Fecha estado</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">V. total</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">V. abono</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Estado</td>
																											</tr>
																											<logic:iterate name="vistaPedidoDTO" property="vistaPedidoDTOCol" id="vistaPedidoDTO2" indexId="indicePedido2">
																												<%-- control del estilo para el color de las filas --%>
																												<bean:define id="residuoR" value="${indicePedido2 % 2}"/>
																												<logic:equal name="residuoR" value="0">
																													<bean:define id="colorBack2" value="blanco10"/>
																												</logic:equal>
																												<logic:notEqual name="residuoR" value="0">
																													<bean:define id="colorBack2" value="amarilloClaro10"/>
																												</logic:notEqual>
																												<tr class="${colorBack2} textoNegro10">
																													<td class="columna_contenido fila_contenido" width="3%" align="center">
																														<html:multibox property="opSeleccionPedidosConsolidar" value="${indicePedido}-${vistaPedidoDTO.id.codigoPedido}"/>
																													</td>
																													<td align="center" class="columna_contenido fila_contenido">
																														<html:link title="Detalle del pedido" action="estadoPedido.do?indicePrincipal=${indicePedido}&indiceRelacionado=${indicePedido2}"><bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/></html:link>
																													</td>
																													<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="llaveContratoPOS"/></td>
																													<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
																													<td align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.totalPedido}"/></td>
																													<td align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.abonoPedido}"/></td>
																													<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="descripcionEstado"/></td>
																												</tr>
																											</logic:iterate>
																										</table>
																									</logic:notEmpty>
																								</div>
																							</td>
																						</tr>
																					</logic:iterate>
																				</table>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td  height="5px"></td>
																	</tr>
																</logic:notEmpty>
																<logic:empty name="cotizarRecotizarReservarForm" property="datos">
																	<tr>
																		<td colspan="8" class="textoNegro11">Seleccione un criterio de b&uacute;squeda</td>
																	</tr>
																</logic:empty>
															</table>
														</div>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
						<%--Fin Datos--%>
						<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
					</tr>
				</table>
			</td>
		</tr>	
		
		<logic:notEmpty name="ec.com.smx.sic.sispe.posicion.div"> 
  		<bean:define id="valPosicion" name="ec.com.smx.sic.sispe.posicion.div"/> 
		<html:hidden property="posicion" value="${valPosicion}"/> 
		   <script language="JavaScript" type="text/javascript"> 
		     
		    document.getElementById('div_listado').scrollTop=document.getElementById('posicion').value; 
		     
		   </script> 
		  </logic:notEmpty> 
		  <logic:empty name="ec.com.smx.sic.sispe.posicion.div"> 
		   <html:hidden property="posicion" value=""/> 
		  </logic:empty>
		
	
</TABLE>


<tiles:insert page="/include/bottom.jsp"/>
<div id="restaurarPosicion">
<logic:notEmpty name="ec.com.smx.sic.sispe.posicion.div"> 
  <bean:define id="valPosicion" name="ec.com.smx.sic.sispe.posicion.div"/> 
  <html:hidden property="posicion" value="${valPosicion}"/> 
   <script language="JavaScript" type="text/javascript">
    		document.getElementById('div_listado').scrollTop=document.getElementById('posicion').value; 
   </script> 
  </logic:notEmpty> 
  <logic:empty name="ec.com.smx.sic.sispe.posicion.div"> 
   <html:hidden property="posicion" value=""/> 
  </logic:empty> 
</div>
</html:form>