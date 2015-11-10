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
<bean:define id="estadoPagadoLiquidado"><bean:message key="codigoEstadoPagadoLiquidado"/></bean:define>
<bean:define id="devolucion"><bean:message key="ec.com.smx.sic.sispe.estado.devolucion1"/></bean:define>
<bean:define id="reservado"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<%--<html:form action="estadoPedido" method="post" focus="campoBusqueda">--%>
	<html:form action="estadoPedido" method="post" enctype="multipart/form-data">
		<html:hidden property="ayuda" value=""/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../confirmacion/confirmacion.jsp"/>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="estadoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">
						<tiles:insert page="/confirmacion/popUpConfirmacionAux.jsp">
							<tiles:put name="vtformAction" value="estadoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
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
						<td height="35" valign="middle">Estado de pedidos</td>
						<td width="10%">
							<table align="left" cellpadding="0">
								<tr>
									<td valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="celesteClaro10" width="100%" height="12px"></td></tr></table></td>
								</tr>
								<tr>
									<td align="left" class="textoAzul9">Reservas modificadas</td>
								</tr>
							</table>
						</td>		
						<td align="right" valign="top">
							<table border="0" cellpadding="0" cellspacing="1">
								<tr>
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
										</div>
									</td>
									<td>
										<bean:define id="exit" value="exit"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
										</div>
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
						<td id="divisor" height="${alto_divs}">
							<span style="display:block" id="img_ocultar">
								<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
							</span>
							<span style="display:none" id="img_mostrar">
								<a href="#"><img src="./images/spliter_der.png" border="0"></a>
							</span>
						</td>
						<%-- Datos --%>
						<td class="datos fila_contenido columna_contenido_der" width="100%" id="derecha">
							<div style="width:100%;height:490px;">
								<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									<%--Titulo de los datos--%>
									<tr>
										<td class="fila_titulo">
											<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
												<tr>
													<td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
													<td height="23" width="100%">&nbsp;
														Lista de pedidos
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<%--Datos de pedidos--%>
									<tr>
										<td>
											<div id="resultadosBusqueda">
												<table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
													<logic:notEmpty name="listadoPedidosForm" property="datos">
														<tr>
															<td>
																<table cellpadding="0" cellspacing="0" width="100%">
																	<tr>
																		<td align="left" class="textoRojo10" id="ordenarPor" height="20px">
																			<logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
																				<bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
																				<b>Ordenado Por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
																			</logic:notEmpty>
																		</td>
																		<td align="right">
																			<smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro11" url="estadoPedido.do"  requestAjax="'mensajes','resultadosBusqueda'"/>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td>
																<table border="0" cellspacing="0" width="98%" cellpadding="1">
																	<tr class="tituloTablas">
																		<td class="columna_contenido" width="3%" align="center">&nbsp;</td>
																		<td class="columna_contenido" width="3%" align="center">&nbsp;</td>
																		<td class="columna_contenido" width="3%" align="center">No</td>
																		<td class="columna_contenido" width="4%" align="center" title="Pedido con autorizaci&oacute;n.">AUT.</td>
																		<td class="columna_contenido" width="14%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de pedido" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=0'});">No PEDIDO</a></td>
																		<td class="columna_contenido" width="7%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de reserva" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=1'});">No RES.</td>
																		<td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="Ordenar por fecha de estado" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=2'});">FECHA ESTADO</td>
																		<td class="columna_contenido" width="19%" align="center"><a class="linkBlanco9" title="Ordenar por contacto" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=3'});">CONTACTO EMPRESA</td>
																		<td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="Ordenar por valor total del pedido" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=4'});">VALOR TOTAL</td>
																		<td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="Ordenar por el valor abonado al pedido" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=5'});">VALOR ABONADO</td>
																		<td class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" title="Ordenar por estado del pedido" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=6'});">ESTADO</td>
																		<td class="columna_contenido" width="6%" align="center"><a class="linkBlanco9" title="Ordenar por ultimo estado del pedido" href="#" onclick="requestAjax('estadoPedido.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenar=7'});">ACTUAL</td>
																		<td class="columna_contenido" width="3%" align="center">&nbsp;</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td>
																<div id="div_listado" style="width:100%;height:420px;overflow:auto">
																	<logic:notEmpty name="ec.com.smx.sic.sispe.tipoPedido.especial">
																		<bean:define id="pedidoEspecial" name="ec.com.smx.sic.sispe.tipoPedido.especial"/>
																	</logic:notEmpty>
																	<table border="0" cellspacing="0" width="98%" cellpadding="1">
																		<logic:iterate name="listadoPedidosForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
																			<bean:define id="indiceGlobal" value="${indicePedido + listadoPedidosForm.start}"/>
																			<bean:define id="fila" value="${indiceGlobal + 1}"/>
																			<%-- control del estilo para el color de las filas --%>
																			<c:if test="${vistaPedidoDTO.npEsNuevaReserva || vistaPedidoDTO.id.codigoEstado!=reservado}">
																				<bean:define id="residuo" value="${indicePedido % 2}"/>
																				<logic:equal name="residuo" value="0">
																					<bean:define id="colorBack" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuo" value="0">
																					<bean:define id="colorBack" value="grisClaro10"/>
																				</logic:notEqual>
																			</c:if>
																			<c:if test="${!vistaPedidoDTO.npEsNuevaReserva && vistaPedidoDTO.id.codigoEstado==reservado}">
																				<bean:define id="colorBack" value="celesteClaro10"/>
																			</c:if>
																			<c:set var="verDetalleNormal" value="0"/>
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
																					<bean:define name="vistaPedidoDTO" property="totalPedido" id="totalPedidoBean"/>
																					<bean:define name="vistaPedidoDTO" property="abonoPedido" id="abonoPedidoBean"/>
																					<bean:define name="vistaPedidoDTO" property="id.codigoEstado" id="estadoBean"/>
																					<logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
																					<c:if test="${totalPedidoBean - abonoPedidoBean < 0}">
																						<c:choose>
																							<c:when test='${vistaPedidoDTO.codigoEstadoPagado == estadoPagadoLiquidado}'>
																								<img src="images/aprobarSolicitud.gif" border="0" title="Pedido con Nota de Cr&eacute;dito">
																							</c:when>
																							<c:when test='${vistaPedidoDTO.codigoEstadoPagado != estadoPagadoLiquidado}'>
																								<img src="images/valeCaja.gif" border="0" title="Pedido con Vale de Caja">
																							</c:when>
																						</c:choose>
																					</c:if>
																					<c:if test="${totalPedidoBean - abonoPedidoBean > 0 || totalPedidoBean - abonoPedidoBean == 0}">
																					&nbsp;
																					</c:if>
																					</logic:notEmpty>
																					<logic:empty name="vistaPedidoDTO" property="llaveContratoPOS">
																					&nbsp;
																					</logic:empty>
																				</td>
																				<td class="columna_contenido fila_contenido" width="3%" align="center">
																					<bean:define name="vistaPedidoDTO" property="totalPedido" id="totalPedidoBean"/>
																					<bean:define name="vistaPedidoDTO" property="abonoPedido" id="abonoPedidoBean"/>
																					<bean:define name="vistaPedidoDTO" property="id.codigoEstado" id="estadoBean"/>
																					<logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
																					<logic:notEmpty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
																						<c:if test="${vistaPedidoDTO.estadoActual == 'SI'}">
																						<div id="desplegar${indicePedido}" style="display:${display1}">
																							<a title="Mostrar histórico de cambios en la reservación" href="javascript:requestAjax('estadoPedido.do',['marco${indicePedido}'],{parameters: 'pedidoRelacionado=${indicePedido}'});hide(['desplegar${indicePedido}']);show(['plegar${indicePedido}','marco${indicePedido}']);">
																								<html:img src="images/desplegar.gif" border="0"/>
																							</a>
																						</div>
																						<div id="plegar${indicePedido}" style="display:${display2}">
																							<a href="javascript:show(['desplegar${indicePedido}']);hide(['plegar${indicePedido}','marco${indicePedido}']);">
																								<html:img src="images/plegar.gif" border="0"/>
																							</a>
																						</div>
																						</c:if>
																						<c:if test="${vistaPedidoDTO.estadoActual == 'NO'}">
																						&nbsp;
																						</c:if>
																					</logic:notEmpty>
																					<logic:empty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
																						&nbsp;
																					</logic:empty>
																					</logic:notEmpty>
																					<logic:empty name="vistaPedidoDTO" property="llaveContratoPOS">
																					&nbsp;
																					</logic:empty>
																				</td>
																				<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="fila"/></td>
																				<logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
		                                                                        	<logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionStock">
		                                                                        		<td class="columna_contenido fila_contenido" align="center" width="4%" title="Pedido con autorizaci&oacute;n descuento variable y stock.">
																					  		<html:img src="images/autorizacion16.png" border="0"/>
																					 	</td>
		                                                                        	</logic:notEmpty>
		                                                                        	<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionStock">
		                                                                        		<td class="columna_contenido fila_contenido" align="center" width="4%" title="Pedido con autorizaci&oacute;n descuento variable.">
																					  		<html:img src="images/autorizacion16.png" border="0"/>
																					 	</td>
		                                                                        	</logic:empty>
																				</logic:notEmpty>
																				<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
																					<logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionStock">
		                                                                        		<td class="columna_contenido fila_contenido" align="center" width="4%" title="Pedido con autorizaci&oacute;n de stock.">
																					  		<html:img src="images/autorizacion16.png" border="0"/>
																					 	</td>
		                                                                        	</logic:notEmpty>
																					<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionStock">
		                                                                        		<td class="columna_contenido fila_contenido" align="center" width="4%">&nbsp;</td>
		                                                                        	</logic:empty>
																				</logic:empty>																				
																				<%-- <logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
                                                                                	<td class="columna_contenido fila_contenido" align="center" width="4%" title="Pedido con autorizaci&oacute;n.">
																			  			<html:img src="images/autorizacion16.png" border="0"/>
																			 		</td>
                                                                                </logic:notEmpty>
                                                                                <logic:empty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
                                                                                	<td class="columna_contenido fila_contenido" align="center" width="4%">&nbsp;</td>
                                                                                </logic:empty>--%>
																				<td width="14%" class="fila_contenido columna_contenido" align="center">
																					<logic:notEmpty name="vistaPedidoDTO" property="codigoConsolidado">
                                                                                    	<html:img src="images/consolidar.gif" border="0" alt="Pedido consolidado"/>
                                                                                	</logic:notEmpty>
																					<logic:equal name="verDetalleNormal" value="0" >
																						<html:link title="Detalle del pedido" onclick="popWait();" action="detalleEstadoPedidoEspecial.do?detallePedido=${indicePedido}"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
																					</logic:equal>
																					<logic:equal name="verDetalleNormal" value="1">
																						<html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indicePedido" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
																					</logic:equal>
																				</td>
																				<td class="columna_contenido fila_contenido" width="7%" align="center">
																					<logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
																						<bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/>
																					</logic:notEmpty>
																					<logic:empty name="llaveContratoPOS">&nbsp;</logic:empty>
																				</td>
																				<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
																				<td class="columna_contenido fila_contenido" width="19%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
																				<td class="columna_contenido fila_contenido" width="10%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
																				<td class="columna_contenido fila_contenido" width="10%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.abonoPedido}"/></td>
																				<td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
																				<td class="columna_contenido fila_contenido" width="6%" align="center"><bean:write name="vistaPedidoDTO" property="estadoActual"/></td>
																				<td class="columna_contenido fila_contenido columna_contenido_der" width="3%" align="center">
																					<bean:define id="archivo" value="${vistaPedidoDTO.archivoBeneficiario}"/>
																					<logic:notEmpty name="vistaPedidoDTO" property="archivoBeneficiario">
																						<c:if test="${vistaPedidoDTO.codigoTipoPedido == tipoPedidoNormal && vistaPedidoDTO.archivoBeneficiario == activoA && vistaPedidoDTO.descripcionEstado != anulado && vistaPedidoDTO.descripcionEstado != cotizado  && vistaPedidoDTO.descripcionEstado != recotizado && vistaPedidoDTO.descripcionEstado != cotCaduca && vistaPedidoDTO.estadoActual == 'SI'}">
																							<a href="#" onclick="requestAjax('estadoPedido.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'aceptarArchBene=${vistaPedidoDTO.id.codigoPedido}&&codigoLocal=${vistaPedidoDTO.id.codigoAreaTrabajo}',evalScripts: true});"><img src="./images/adjuntar.gif" border="0" alt="adjuntar archivo beneficiario"></a>
																						</c:if>
																						<c:if test="${vistaPedidoDTO.codigoTipoPedido == tipoPedidoNormal && vistaPedidoDTO.archivoBeneficiario == inactivoA && vistaPedidoDTO.descripcionEstado != anulado && vistaPedidoDTO.descripcionEstado != cotizado  && vistaPedidoDTO.descripcionEstado != recotizado && vistaPedidoDTO.descripcionEstado != cotCaduca && vistaPedidoDTO.estadoActual == 'SI'}">
																							<a href="#" onclick="requestAjax('estadoPedido.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'aceptarArchBene=${vistaPedidoDTO.id.codigoPedido}&&codigoLocal=${vistaPedidoDTO.id.codigoAreaTrabajo}&&campoArchivo=${vistaPedidoDTO.archivoBeneficiario}',evalScripts: true});"><img src="./images/adjuntar2.gif" border="0" alt="adjuntar archivo beneficiario"></a>
																						</c:if>
																						<c:if test="${vistaPedidoDTO.descripcionEstado == anulado ||  vistaPedidoDTO.descripcionEstado == cotizado || vistaPedidoDTO.descripcionEstado == recotizado || vistaPedidoDTO.descripcionEstado == cotCaduca || vistaPedidoDTO.codigoTipoPedido != tipoPedidoNormal || vistaPedidoDTO.estadoActual == 'NO' }">-</c:if>
																					</logic:notEmpty>
																				</td>
																			</tr>
																			<tr>
																				<td colspan="10" align="center">
																					<div id="marco${indicePedido}" style="display:${display2}">
																						<logic:notEmpty name="vistaPedidoDTO" property="vistaPedidoDTOCol">
																							<b align="center" class="textoAzul10">Historial de modificaciones de la reserva ${vistaPedidoDTO.llaveContratoPOS}</b>
																							<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																								<tr class="tituloTablasCeleste">
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No Pedido</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No Reserva</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Fecha Estado</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">V. Total</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">V. Abono</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Estado</td>
																									<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Tip. Doc.</td>
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
																										<td align="center" class="columna_contenido fila_contenido">
																											<html:link title="Detalle del pedido" action="estadoPedido.do?indicePrincipal=${indicePedido}&indiceRelacionado=${indicePedido2}"><bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/></html:link>
																										</td>
																										<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="llaveContratoPOS"/></td>
																										<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="fechaInicialEstado" formatKey="formatos.fechahora"/></td>
																										<td align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.totalPedido}"/></td>
																										<td align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO2.abonoPedido}"/></td>
																										<td align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO2" property="descripcionEstado"/></td>
																										<c:if test="${vistaPedidoDTO2.descripcionEstado == devolucion && vistaPedidoDTO.codigoEstadoPagado == estadoPagadoLiquidado }">
																										<td align="center" class="columna_contenido fila_contenido">Nota de Crédito</td>
																										</c:if>
																										<c:if test="${vistaPedidoDTO2.descripcionEstado == devolucion && vistaPedidoDTO.codigoEstadoPagado != estadoPagadoLiquidado }">
																										<td align="center" class="columna_contenido fila_contenido">Vale de Caja</td>
																										</c:if>
																										<c:if test="${vistaPedidoDTO2.descripcionEstado != devolucion }">
																										<td align="center" class="columna_contenido fila_contenido"> - </td>
																										</c:if>
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
													<logic:empty name="listadoPedidosForm" property="datos">
														<tr>
															<td colspan="8" class="textoNegro11">Seleccione un criterio de b&uacute;squeda</td>
														</tr>
													</logic:empty>
												</table>
											</div>
										</td>
									</tr>
									<%--Fin datos de pedidos--%>
								</table>
							</div>
						</td>
						<%--Fin Datos--%>
						<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
					</tr>
				</table>
			</td>
		</tr>
	</html:form>
</TABLE>
<tiles:insert page="/include/bottom.jsp"/>