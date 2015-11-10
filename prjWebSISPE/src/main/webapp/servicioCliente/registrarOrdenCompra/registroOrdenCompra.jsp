<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<tiles:insert page="/include/top.jsp"/>
<%-- lista de definiciones para las acciones --%>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
	<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
</logic:notEmpty>
<bean:define id="tipoPedidoEmpresarial">
	<bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.empresarial"/>
</bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<c:set var="uno" value="${1}"/>

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>

<html:form action="registrarOrdenCompra" method="post">	
	<TABLE border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
		<html:hidden property="ayuda" value=""/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../../confirmacion/confirmacion.jsp"/>
					</logic:notEmpty>
					<logic:present name="ec.com.sxm.sic.sispe.regresarSinConfirmacion">
						<script type="text/javascript">realizarEnvio('siVolverBusqueda');</script>
					</logic:present>
				</div>
			</td>
		</tr>
		<tr>
			<td class="titulosAccion" height="35px">
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="3%" align="center"><img src="./images/autorizacionOrdenCompra.gif" border="0"></img></td>
						<td align="left">&nbsp;&nbsp;Registro de Autorizaci&oacute;n Orden de Compra</td>
						<td align="right">
							<table cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('registrarOrdenesCompra');" title="guardar &oacute;rdenes de compra">Guardar</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link styleClass="cancelarA" href="#" onclick="requestAjax('registrarOrdenCompra.do',['pregunta'],{parameters: 'volverBuscar=ok', evalScripts:true});" title="volver a la p&aacute;gina de b&uacute;squeda">Cancelar</html:link>
										</div>
									</td>
									<td>
										<bean:define id="exit" value="ok"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
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
				<table border="0" class="textoNegro11" align="center" width="98%" cellpadding="0" cellspacing="0">
					<tr><td height="5px"></td></tr>
					<tr>
						<td valign="top" width="100%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
								<tr>
									<td colspan="3">
										<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
											<tr>
												<td class="fila_titulo" height="20px">
													<table cellpadding="0" cellspacing="0" align="left" width="100%">
														<tr>
															<td align="left">&nbsp;Encabezado (Datos del cliente)</td>
															<td align="right" class="textoRojo11">No Pedido:&nbsp;<label class="textoAzul11"><bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="id.codigoPedido"/></label></td>
															<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
																<td align="right" class="textoRojo11" id="local" width="20%"><b>Local:</b>&nbsp;
																	<label class="textoAzul11"><b>
																			<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="id.codigoAreaTrabajo"/>
																	</b>&nbsp;</label>
																</td>
															</logic:notEmpty>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%">
														<tr>
															<td>
																<table border="0" cellspacing="0" cellpadding="0" align="left" width="45%" class="textoAzul11" >
																	<tr>
																		<td align="right" width="5%">CI/Pasaporte:&nbsp;</td>
																		<td align="left" width="13%" class="textoNegro11">
																			<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="cedulaContacto"/>
																			<%-- esta campo oculto es para el correcto funcionamiento de ajax --%>
																			<span style="display:none"><input type="text" name="prueba"></span>
																		</td>
																	</tr>
																	<tr>
																		<td align="right">Nombre:&nbsp;</td>
																		<td align="left" class="textoNegro11">
																			<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="nombreContacto"/>
																		</td>
																	</tr>
																	<tr>
																		<td align="right">Tel&eacute;fono:&nbsp;</td>
																		<td align="left" class="textoNegro11">
																			<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="telefonoContacto"/>
																		</td>
																	</tr>
																</table>
															</td>
															<td valign="top">
																<logic:equal name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="contextoPedido" value="${tipoPedidoEmpresarial}">
																	<table border="0" width="55%" cellpadding="0" cellspacing="0" align="left">
																		<tr>
																			<td class="textoAzul11" align="right">Ruc:&nbsp;</td>
																			<td align="left" class="textoNegro11">
																				<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="rucEmpresa"/>
																			</td>
																		</tr>
																		<tr>
																			<td class="textoAzul11" align="right">Empresa:&nbsp;</td>
																			<td align="left" class="textoNegro11">
																				<bean:write name="ec.com.smx.sic.sispe.ordenCompra.vistaPedidoOrdenCompra" property="nombreEmpresa"/>
																			</td>
																		</tr>
																	</table>
																</logic:equal>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<tr>
									<td>
										<div id="seccion_detalle">
											<table class="textoNegro12" cellpadding="0" cellspacing="0" align="left" width="100%" border="0">
												<tr>
													<td>
														<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion">
															<tr class="fila_titulo">
																<td align="left" class="fila_contenido">
																	<b class="textoNegro11">&nbsp;Art&iacute;culos</b>
																</td>
																<td height="27px" class="fila_contenido" align="right">
																	<div id="botonD">
																		<html:link title="Copiar Num. Orden y Observaci&oacute;n" styleClass="copiarD" href="#" onclick="requestAjax('registrarOrdenCompra.do', ['mensajes','idCheckTodos','div_listado'], {parameters: 'copiarOrdenPedido=ok',popWait:true});">Copiar</html:link>
																	</div>
																</td>
																<td width="2px"></td>
															</tr>
															<tr><td height="10"></td></tr>
															<tr>
																<td colspan="2">
																	<div id="detallePedido">
																		<table border="0" width="100%" cellpadding="0" cellspacing="0">
																			<logic:notEmpty name="ec.com.smx.sic.sispe.ordenCompra.detallesPedido">
																				<tr>
																					<td>
																						<table cellspacing="0" cellpadding="3" width="98%" align="center">
																							<tr class="tituloTablas">
																								<td class="tituloTablas" align="center" width="3%" id="idCheckTodos">
																									<html:checkbox title="seleccionar todos" property="todoPedidos" onclick="activarDesactivarTodo(this,registrarOrdenCompraForm.checkSeleccion)"/>
																								</td>
																								<td width="3%" align="center" class="columna_contenido">No</td>
																								<td width="11%" align="center" class="columna_contenido">C&Oacute;DIGO BARRAS</td>
																								<td width="18%" align="center" class="columna_contenido">ART&Iacute;CULO</td>
																								<td width="7%" align="center" class="columna_contenido">CANTIDAD</td>
																								<td width="7%" align="center" class="columna_contenido">PESO KG</td>
																								<td width="6%" align="center" class="columna_contenido">VALOR UNITARIO</td>
																								<td width="3%" align="center" class="columna_contenido">IVA</td>
																								<td width="7%" align="center" class="columna_contenido">VALOR TOTAL</td>
																								<td width="15%" align="center" class="columna_contenido">N&Uacute;MERO ORDEN COMPRA</td>
																								<td width="18%" align="center" class="columna_contenido columna_contenido_der">OBSERVACI&Oacute;N</td>
																							</tr>
																							<tr class="celesteFosforescente">
																								<td width="67%" colspan="9" class="columna_contenido fila_contenido">&nbsp;</td>
																								<td width="15%" class="columna_contenido fila_contenido" align="center">
																									<smx:text property="numeroOrdenPedido" value="${detalle.numeroAutorizacionOrdenCompra}" styleClass="textNormal" size="20" maxlength="20"/>
																								</td>
																								<td width="18%" class="columna_contenido fila_contenido columna_contenido_der" align="center">
																									<smx:textarea property="observacionPedido" styleClass="textNormal" value="${detalle.observacionAutorizacionOrdenCompra}" cols="25" rows="2"/>
																								</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						<div id="div_listado" style="width:100%;height:330px;overflow:auto;">
																							<table cellspacing="0" cellpadding="0" width="98%">
																								<%-------- comienza la iteracion del detalle del pedido --------%>
																								<logic:iterate name="ec.com.smx.sic.sispe.ordenCompra.detallesPedido" id="detalle" indexId="numeroRegistro">
																									<%----- control del estilo para el color de las filas ------%>
																									<bean:define id="residuo" value="${numeroRegistro % 2}"/>
																									<c:set var="clase" value="blanco10"/>
																									<logic:notEqual name="residuo" value="0">
																										<c:set var="clase" value="grisClaro10"/>
																									</logic:notEqual>
																									<bean:define id="fila" value="${numeroRegistro + 1}"/>
																									<c:set var="pesoVariable" value=""/>
																									<logic:equal name="detalle" property="detallePedidoDTO.articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
																										<c:set var="pesoVariable" value="${estadoActivo}"/>
																									</logic:equal>
																									<tr class="${clase}">
																										<td width="3%" align="center" class="columna_contenido fila_contenido">
																											<html:multibox property="checkSeleccion" value="${numeroRegistro}"></html:multibox>
																										</td>
																										<td width="3%" class="columna_contenido fila_contenido" align="center"><bean:write name="fila"/></td>
																										<%-----Definici&oacute;n de la propiedad articuloDTO ------%>
																										<td width="11%" class="columna_contenido fila_contenido" align="center"><bean:write name="detalle" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
																										<td width="18%" class="columna_contenido fila_contenido" align="left">
																											<table cellpadding="0" cellspacing="0" width="100%">
																												<tr>
																													<td align="left"><bean:write name="detalle" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/></td>
																													<%--<c:if test="${detalle.detallePedidoDTO.articuloDTO.codigoTipoArticulo == tipoCanasto || detalle.detallePedidoDTO.articuloDTO.codigoTipoArticulo == tipoDespensa}"> --%>
																													<c:if test="${detalle.detallePedidoDTO.articuloDTO.codigoClasificacion == articuloEspecial|| detalle.detallePedidoDTO.articuloDTO.codigoClasificacion == canastoCatalogo}">
																															<bean:define id="codigoSubClasificacion" name="detalle" property="detallePedidoDTO.articuloDTO.codigoSubClasificacion"/>
																				                                            <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																															  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																																<bean:define id="descripcion" value="detalle de la despensa"/>
																															</c:if>
																															<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
																															  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
																				                                                <bean:define id="descripcion" value="detalle del canasto"/>
																															</c:if>
																														<td align="right"><img title="${descripcion}" src="${pathImagenArticulo}" border="0"/></td>
																													</c:if>
																													<%--<c:if test="${detalle.detallePedidoDTO.articuloDTO.codigoTipoArticulo != tipoCanasto && detalle.detallePedidoDTO.articuloDTO.codigoTipoArticulo != tipoDespensa}"> --%>
																													<c:if test="${detalle.detallePedidoDTO.articuloDTO.codigoClasificacion != articuloEspecial && detalle.detallePedidoDTO.articuloDTO.codigoClasificacion != canastoCatalogo}">
																														<logic:equal name="pesoVariable" value="${estadoActivo}">
																															<td align="right"><img title="peso variable" src="images/pavo.gif" border="0"></td>
																														</logic:equal>
																													</c:if>
																													<td width="2px">&nbsp;</td>
																												</tr>
																											</table>
																										</td>
																										<td width="7%" class="columna_contenido fila_contenido" align="center">
																											<logic:notEmpty name="detalle" property="cantidadConOrdenCompra">
																												${detalle.cantidadReservarSIC - detalle.cantidadConOrdenCompra}
																											</logic:notEmpty>
																											<logic:empty name="detalle" property="cantidadConOrdenCompra">
																												<bean:write name="detalle" property="cantidadReservarSIC"/>
																											</logic:empty>
																										</td>
																										<td width="7%" class="columna_contenido fila_contenido" align="right">
																											<logic:equal name="pesoVariable" value="${estadoActivo}">
																												<logic:notEmpty name="detalle" property="cantidadConOrdenCompra">
																													<%-- regla de tres --%>
																													<c:set var="pesoArticulo" value="${((detalle.cantidadReservarSIC - detalle.cantidadConOrdenCompra) * detalle.pesoArticuloEstadoReservado)/detalle.cantidadReservarSIC}"/>
																													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${pesoArticulo}"/>
																												</logic:notEmpty>
																												<logic:empty name="detalle" property="cantidadConOrdenCompra">
																													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.pesoArticuloEstadoReservado}"/>
																												</logic:empty>
																											</logic:equal>
																											<logic:notEqual name="pesoVariable" value="${estadoActivo}">
																												<center class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></center>
																											</logic:notEqual>
																										</td>
																										<td width="6%" class="columna_contenido fila_contenido" align="center">
																											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.valorUnitarioEstado}"/>
																										</td>
																										<td class="columna_contenido fila_contenido" width="3%" align="center">
																											<logic:equal name="detalle" property="detallePedidoDTO.articuloDTO.aplicaImpuestoVenta" value="${estadoActivo}">
																												<img src="images/tick.png" border="0">
																											</logic:equal>
																											<logic:notEqual name="detalle" property="detallePedidoDTO.articuloDTO.aplicaImpuestoVenta" value="${estadoActivo}">
																												<img src="images/x.png" border="0">
																											</logic:notEqual>
																										</td>
																										<td width="7%" class="fila_contenido columna_contenido" align="right">
																											<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.valorTotalEstadoIVA}"/>
																											</logic:equal>
																											<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
																												<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.valorTotalEstado}"/>
																											</logic:notEqual>
																										</td>
																										<logic:equal name="detalle" property="npNumeroAutorizacionOrdenCompraInCorrecto" value="true">
																											<bean:define id="estiloCuadroTexto" value="campoError"/>
																										</logic:equal>
																										<logic:equal name="detalle" property="npNumeroAutorizacionOrdenCompraInCorrecto" value="false">
																											<bean:define id="estiloCuadroTexto" value="textNormal"/>
																										</logic:equal>
																										<c:set var="indiceTexto" value="${numeroRegistro - 1}"/>
																										<td width="15%" class="columna_contenido fila_contenido" align="center">
																											<smx:text property="numerosAutorizaciones" value="${detalle.numeroAutorizacionOrdenCompra}" styleClass="${estiloCuadroTexto}" size="20" maxlength="20"/>
																										</td>
																										<td width="18%" class="columna_contenido fila_contenido columna_contenido_der" align="center">
																											<smx:textarea property="observacionesNumerosAutorizaciones" value="${detalle.observacionAutorizacionOrdenCompra}" styleClass="${estiloCuadroTexto}" cols="25" rows="2"/>
																										</td>
																									</tr>
																								</logic:iterate>
																							</table>
																						</div>
																					</td>
																				</tr>
																			</logic:notEmpty>
																		</table>
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr><td height="2px"></td></tr>
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