<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:insert page="/include/top.jsp"/>
<smx:equal name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.modificarFechasEntrega">
	<bean:define id="titulo" value="B&uacute;squeda de pedidos para modificar fechas de entrega"/>
	<bean:define id="accion" value="listaModificarFechasEntrega"/>
</smx:equal>

<html:form action="${accion}" method="post">
	<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td  width="3%" align="center"><img src="images/busqueda_pedido.gif" border="0"></img></td>
						<td height="35" valign="middle">${titulo}</td>
						<td align="right" valign="top">
							<table border="0">
								<tr>
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
		<%--Contenido de la p&aacute;gina principal--%>
		<tr>
			<td align="center" valign="top">
				<table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
					<tr><td height="5px"></td></tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos" width="25%" id="izquierda">
							<div style="height:500px;">
								<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
							</div>
						</td>
						<td width="1%" id="divisor">&nbsp;
						</td>
						<%--Datos--%>
						<td class="datos" width="80%" id="derecha">
							<div style="height:500px;">
								<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									<%--Titulo de los datos--%>
									<tr>
										<td class="fila_titulo" colspan="7">
											<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
												<tr>
													<td><img src="images/detalle_pedidos24.gif" border="0"/></td>
													<td height="23" width="100%">&nbsp;Detalle de pedidos</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="5px"></td></tr>
									<tr>
										<td>
											<div id="resultadosBusqueda">
												<table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
													<logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.subPagina">
														<tr>
															<td colspan="7" align="right">
																<smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro11" url="${accion}.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
															</td>
														</tr>
														<tr>
															<td colspan="7">
																<table border="0" cellspacing="0" width="100%" class="tabla_informacion">
																	<tr class="tituloTablas">
																		<td width="4%" align="center">No</td>
																		<td class="columna_contenido" width="14%" align="center">No PEDIDO</td>
																		<td class="columna_contenido" width="10%" align="center">No RES.</td>
																		<td class="columna_contenido" width="20%" align="center">CONTACTO-EMPRESA</td>
																		<td class="columna_contenido" width="11%" align="center">FECHA ESTADO</td>
																		<td class="columna_contenido" width="13%" align="center">ESTADO</td>
																		<td class="columna_contenido" width="9%" align="center">V. TOTAL</td>
																		<td class="columna_contenido" width="20%" align="center">ACCI&Oacute;N</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td>
																<div id="div_listado" style="width:100%;height:440px;overflow:auto">
																	<table border="0" cellspacing="0" width="100%" class="tabla_informacion">
																		<logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
																			<bean:define id="indice" value="${numeroRegistro}"/>
																			<%-- control del estilo para el color de las filas --%>
																			<bean:define id="residuo" value="${numeroRegistro % 2}"/>
																			<bean:define id="fila" value="${indice + 1}"/>
																			<logic:equal name="residuo" value="0">
																				<bean:define id="colorBack" value="blanco10"/>
																			</logic:equal>
																			<logic:notEqual name="residuo" value="0">
																				<bean:define id="colorBack" value="grisClaro10"/>
																			</logic:notEqual>
																			<tr class="${colorBack}">
																				<td class="fila_contenido" align="center" width="4%">${fila + listadoPedidosForm.start}</td>
																				<td class="columna_contenido fila_contenido" align="center" width="14%" >
																					<html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
																				</td>
																				<td class="columna_contenido fila_contenido" width="10%" align="center">&nbsp;<bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
																				<td class="columna_contenido fila_contenido" width="20%" align="center"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
																				<td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
																				<td class="columna_contenido fila_contenido" width="13%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
																				<td class="columna_contenido fila_contenido" width="9%" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
																				<td class="columna_contenido fila_contenido" width="20%" align="center">
																					<smx:equal name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.modificarFechasEntrega">
																						<html:link action="modificarFechasEntrega" paramId="indice" paramName="indice" onclick="popWait();">Modificar Fechas</html:link>
																					</smx:equal>
																				</td>
																			</tr>
																		</logic:iterate>
																	</table>
																</div>
															</td>
														</tr>
													</logic:notEmpty>
													<logic:empty name="ec.com.smx.sic.sispe.pedidos">
														<tr>
															<td colspan="7">
																Seleccione un criterio de b&uacute;squeda
															</td>
														</tr>
													</logic:empty>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</td>
						<%--Fin Datos--%>
					</tr>
					<%--Fin P&aacute;gina--%>
				</table>
			</td>
		</tr>
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>