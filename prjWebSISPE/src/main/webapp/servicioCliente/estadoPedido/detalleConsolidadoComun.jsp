<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
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
<%------ se obtiene la acción actual ---------%>
<%--<bean:define id="accionActual" name="ec.com.smx.sic.sispe.accion"/>
	<bean:define id="accionEstadoPedido"><bean:message key="ec.com.smx.sic.sispe.accion.estadoPedido"/></bean:define>--%>

<bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<%-- se definen los c&oacute;digos de las posibles entidades responsables --%>
<bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
	<tr>
		<td align="center">
			<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
				<tr class="fila_titulo">
					<td colspan="4" class="textoAzul11 fila_contenido" align="left" height="20px">&nbsp;Listado de empresas consolidadas</td>
				</tr>
				<tr>
					<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
						<table border="0" cellspacing="0" width="100%" align="left">
							<tr>
								<td colspan="2">
									<table width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedioGeneral">
												<td>
													<div id="div_empresasConsolidadas" style="width:100%;height:55px;overflow-x:hidden;overflow-y:auto;">
														<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" class=tabla_informacion_negro>
															<tr class="tituloTablasCeleste">
																<td class="fila_contenido_negro columna_contenido_der_negro" width="20%" align="left">&nbsp;Código pedido</td>
																<td align="left" width="80%" class="fila_contenido_negro columna_contenido_der_negro">Información empresa</td>
															</tr>
															<logic:iterate name="ec.com.smx.sic.sispe.empresas.vistaPedidoCol" id="vistaPedidoDTO" indexId="numeroRegistro">
															<tr>
																<td align="left"  class="textoNegro11">
																	<bean:write name="vistaPedidoDTO" property="id.codigoPedido"/>
																</td>
																<td align="left"  class="textoNegro11">
																	<bean:write name="vistaPedidoDTO" property="contactoEmpresa"/>
																</td>
															</tr>
															</logic:iterate>
														</table>
													</div>
												</td>
											</logic:notEmpty>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</TABLE>
		</td>
	</tr>
	<tr height="10"><td></td></tr>
	<tr>
		<td colspan="2" align="center">
			<div id="detallePedido">
				<table border="0" width="98%" cellpadding="0" cellspacing="0">
					<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
						<tr>
							<td>
								<c:set var="anchoFilaIngreso" value="31%"/>
								<table cellspacing="0" cellpadding="0" width="98%" align="left">
									<tr class="tituloTablas" height="20px">
										<td width="2%" align="center" class="columna_contenido">&nbsp;</td>
										<td width="3%" align="center" class="columna_contenido">No</td>
										<td width="13%" align="center" class="columna_contenido">C&oacute;digo barras</td>
										<td width="26%" align="center" class="columna_contenido">Art&iacute;culo</td>
										<td width="6%" align="center" class="columna_contenido">Cant.</td>
										<td width="6%" align="center"  class="columna_contenido">Stock</td>
										<td width="6%" align="center" class="columna_contenido">Peso Kg</td>
										<td width="6%" align="center" class="columna_contenido">V.Unit.</td>
										<td width="6%" align="center" class="columna_contenido">V.Unit.Iva</td>
										<%--	<table cellpadding="0" cellspacing="0" border="0">
												<tr>
													<td>
														<logic:notEmpty name="ec.com.smx.sic.sispe.establecimientoHabilitadoCambioPrecios">
															<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																<html:link styleClass="linkBlanco9" href="#" onclick="requestAjax('crearCotizacion.do',['seccion_detalle'],{parameters: 'actualizarPreciosUnitarios=ok'})">V.Unit</html:link>
															</logic:equal>
															<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">V.Unit.</logic:notEqual>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.establecimientoHabilitadoCambioPrecios">V.Unit.</logic:empty>
													</td>
												</tr>
											</table>
										</td>--%>
										<td width="7%" align="center" class="columna_contenido">Tot. Iva</td>
										<td width="2%" align="center" class="columna_contenido">Iva</td>
										<td width="5%" align="center" class="columna_contenido" title="descuento total: por caja + descuento aplicado">Dscto.</td>
										<%--<td width="5%" align="center" class="columna_contenido" title="valor unitario incluido el descuento">V.Unit. Neto</td>--%>
										<td width="7%" align="center" class="columna_contenido columna_contenido_der" title="valor total incluido el descuento">Tot. Neto</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<div id="div_listado" style="width:100%;height:250px;overflow-x:hidden;overflow-y:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
									<table cellspacing="0" cellpadding="0" width="98%" align="left">
										<logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numeroRegistro">
											<bean:define id="residuo" value="${numeroRegistro % 2}"/>
											<c:set var="titulo_fila" value=""/>
											<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejo}"/>
											<logic:equal name="detalle" property="articuloDTO.npHabilitarPrecioCaja" value="${estadoActivo}">
												<c:set var="unidadManejo" value="${detalle.articuloDTO.unidadManejoCaja}"/>
											</logic:equal>
											<c:set var="clase" value="blanco10"/>
											<logic:notEqual name="residuo" value="0">
												<c:set var="clase" value="grisClaro10"/>
											</logic:notEqual>
											<%-- control de estilos para indicar el status completo del artículo en el SIC --%>
											<c:choose>
												<%-- DE BAJA EN EL SIC --%>
												<c:when test="${detalle.articuloDTO.npEstadoArticuloSIC == articuloDeBaja || detalle.articuloDTO.npEstadoArticuloSICReceta == articuloDeBaja}">
													<c:set var="clase" value="verdeClaro10"/>
												</c:when>
												<c:otherwise>
													<%-- OBSOLETO EN EL SIC --%>
													<c:choose>
														<c:when test="${detalle.articuloDTO.claseArticulo != null && detalle.articuloDTO.claseArticulo == articuloObsoleto}">
															<c:set var="clase" value="amarilloClaro10"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<%-- SIN ALCANCE --%>
																<c:when test="${detalle.articuloDTO.npAlcance == estadoInactivo || detalle.articuloDTO.npAlcanceReceta == estadoInactivo}">
																	<c:set var="clase" value="rojoClaro10"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<%-- PROBLEMAS DE STOCK --%>
																		<c:when test="${detalle.articuloDTO.npEstadoStockArticulo == estadoInactivo || detalle.articuloDTO.npEstadoStockArticuloReceta == estadoInactivo}">
																			<c:set var="clase" value="naranjaClaro10"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<%-- INACTIVO EN EL SIC --%>
																				<c:when test="${detalle.articuloDTO.npEstadoArticuloSIC == estadoInactivo || detalle.articuloDTO.npEstadoArticuloSICReceta == estadoInactivo}">
																					<c:set var="clase" value="celesteClaro10"/>
																				</c:when>
																			</c:choose>
																		</c:otherwise>
																	</c:choose>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>											
											<bean:define id="fila" value="${numeroRegistro + 1}"/>
											<c:set var="pesoVariable" value=""/>
											<c:set var="imagen" value=""/>
											<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
												<c:set var="pesoVariable" value="${estadoActivo}"/>
												<c:set var="imagen" value="pavo.gif"/>
											</logic:equal>
											<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
												<c:set var="pesoVariable" value="${estadoActivo}"/>
												<c:set var="imagen" value="balanza.gif"/>
											</logic:equal>
											<%-- Definici&oacute;n de la propiedad articuloDTO --%>
											<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
											<tr>
												<td width="2%" class="fila_contenido columna_contenido" align="center">
													<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">
														<div id="plegar${numeroRegistro}" class="displayNo">
															<a title="Ver Detalle de Entregas" href="#" onClick="ocultar(${numeroRegistro},'marco','plegar','desplegar');">
																<html:img src="images/plegar.gif" border="0"/>
															</a>
														</div>
														<div id="desplegar${numeroRegistro}">
															<a title="Ver Detalle de Entregas" href="#" onClick="mostrar(${numeroRegistro},'marco','plegar','desplegar');">
																<html:img src="images/desplegar.gif" border="0"/>
															</a>
														</div>
													</logic:notEmpty>
													<logic:empty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">&nbsp;</logic:empty>
												</td>
												<td width="3%" class="columna_contenido fila_contenido" align="center"><bean:write name="fila"/></td>
												<td width="13%" class="columna_contenido fila_contenido" align="center" title="${titulo_fila}"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>
												<td width="26%" class="columna_contenido fila_contenido" align="left">
													<table cellpadding="0" cellspacing="0" width="100%" class="textoNegro9">
														<tr>
															<%--<c:if test="${articulo.codigoTipoArticulo == tipoCanasto || articulo.codigoTipoArticulo == tipoDespensa}"> --%>
															<c:if test="${articulo.codigoClasificacion == articuloEspecial|| articulo.codigoClasificacion == canastoCatalogo}">
																<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
																<td valign="middle" align="left">
																	<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
																		<img title="canasto modificado" src="images/estrella.gif" border="0">
																	</logic:notEmpty>
																	
																	<bean:define id="codigoSubClasificacion" name="articulo" property="codigoSubClasificacion"/>
	                                                                    			
						                                            <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																	  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																		<bean:define id="descripcion" value="detalle de la despensa"/>
																	</c:if>
																	<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
																	  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
						                                                <bean:define id="descripcion" value="detalle del canasto"/>
																	</c:if>
																	
																	<%--<logic:equal name="articulo" property="codigoTipoArticulo" value="${tipoDespensa}">
																		<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																		<bean:define id="descripcion" value="detalle de la despensa"/>
																	</logic:equal>
																	<logic:equal name="articulo" property="codigoTipoArticulo" value="${tipoCanasto}">
																		<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
																		<bean:define id="descripcion" value="detalle del canasto"/>
																	</logic:equal> --%>
																	<bean:write name="articulo" property="descripcionArticulo"/>,&nbsp;<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}
																</td>
																<td align="right">
																	<img title="${descripcion}" src="${pathImagenArticulo}" border="0"/>
																</td>
															</c:if>
															<%--<c:if test="${articulo.codigoTipoArticulo != tipoCanasto && articulo.codigoTipoArticulo != tipoDespensa}"> --%>
															<c:if test="${articulo.codigoClasificacion != articuloEspecial && articulo.codigoClasificacion != canastoCatalogo}">
																<td align="left"><bean:write name="articulo" property="descripcionArticulo"/>,&nbsp;<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																<logic:equal name="pesoVariable" value="${estadoActivo}">
																	<td align="right">
																		<img title="peso variable" src="images/${imagen}" border="0">
																	</td>
																</logic:equal>
															</c:if>
															<td width="2px">&nbsp;</td>
														</tr>
													</table>
												</td>
												<td width="6%" class="columna_contenido fila_contenido" align="center">
													<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
														<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																<html:text property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" styleClass="textNormal" size="6" maxlength="5" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});return validarInputNumeric(event);"/>
															</logic:equal>
															<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" write="true"/>
															</logic:notEqual>
														</logic:notEqual>
														<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
															<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
														</logic:equal>
													</logic:empty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">													
														<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
																<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
															</logic:equal>
															<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}" write="true"/>
															</logic:notEqual>
														</logic:notEqual>
														<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
															<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
															<html:hidden property="vectorCantidad" value="${detalle.estadoDetallePedidoDTO.cantidadEstado}"/>
														</logic:equal>
													</logic:notEmpty>
												</td>
												<td width="6%" class="columna_contenido fila_contenido" align="center">
													<logic:notEmpty name="detalle" property="articuloDTO.npStockArticulo">
														<bean:write name="detalle" property="articuloDTO.npStockArticulo"/>
													</logic:notEmpty>
													<logic:empty name="detalle" property="articuloDTO.npStockArticulo">&nbsp;</logic:empty>
												</td>
												<td width="6%" class="columna_contenido fila_contenido" align="right">
												<c:set var="habilitarCambioPesos" value=""/>
												<bean:define id="codigoClasificacion" name="detalle" property="articuloDTO.codigoClasificacion"/>
												<logic:iterate name="ec.com.smx.sic.sispe.parametro.clasificacionesArticulos.cambioPesosAux" id="codigo"> 
													<c:if test="${codigo == codigoClasificacion}"> 
													  <c:set var="habilitarCambioPesos" value="${estadoActivo}"/>
													</c:if>	
												</logic:iterate>
												<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">												
													<logic:equal name="pesoVariable" value="${estadoActivo}">
														<logic:equal name="habilitarCambioPesos" value="${estadoActivo}">
															<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																<center><html:text property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="6" maxlength="5" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'}); return validarInputNumeric(event);"/></center>
																<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
															</logic:notEqual>
														</logic:equal>
														<logic:notEqual name="habilitarCambioPesos" value="${estadoActivo}">
															<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																	<center><html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="9" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>
																</logic:equal>
																<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																	<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
																</logic:notEqual>
															</logic:equal>
														</logic:notEqual>
													</logic:equal>
													<logic:notEqual name="pesoVariable" value="${estadoActivo}">
														<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
														<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
													</logic:notEqual>
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">												
													<logic:equal name="pesoVariable" value="${estadoActivo}">
														<logic:equal name="habilitarCambioPesos" value="${estadoActivo}">														
															<logic:notEqual name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																<center>
																	<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
																	<html:hidden property="vectorPesoActual" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
																</center>
																<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
															</logic:notEqual>
														</logic:equal>
														<logic:notEqual name="habilitarCambioPesos" value="${estadoActivo}">														
															<logic:equal name="detalle" property="articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
																<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																	<center>
																		<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
																	</center>
																	<!--<center><html:text property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" styleClass="textNormal" size="7" maxlength="9" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>-->
																</logic:equal>
																<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																	<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}" write="true"/>
																</logic:notEqual>
															</logic:equal>
														</logic:notEqual>
													</logic:equal>
													<logic:notEqual name="pesoVariable" value="${estadoActivo}">
														<center><label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label></center>
														<html:hidden property="vectorPeso" value="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado}"/>
													</logic:notEqual>
												</logic:notEmpty>
												</td>
												<td width="6%" class="columna_contenido fila_contenido" align="right">
													<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:empty>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:empty>
														</logic:empty>
													</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:empty>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:empty>
														</logic:empty>
													</logic:notEqual>
												</td>
												
												
												<td width="6%" class="columna_contenido fila_contenido" align="right">
													<logic:equal name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
															</logic:empty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<center><html:text property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}" styleClass="textNormal" size="7" maxlength="7" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>
															</logic:notEmpty>
															<html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
																<html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}"/>
															</logic:empty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<center><html:text property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVANoAfiliado}" styleClass="textNormal" size="7" maxlength="7" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>
															</logic:notEmpty>
															<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
														</logic:empty>
													</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.precios.iva" value="${estadoActivo}">
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
																<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
															</logic:empty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<center><html:text property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>
															</logic:notEmpty>
															<html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.pedido.preciosAfiliado">
															<logic:empty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
																<html:hidden property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}"/>
															</logic:empty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.actualizar.preciosUnitarios">
																<center><html:text property="vectorPrecioNoAfi" value="${detalle.estadoDetallePedidoDTO.valorUnitarioNoAfiliado}" styleClass="textNormal" size="10" maxlength="8" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});"/></center>
															</logic:notEmpty>
															<html:hidden property="vectorPrecio" value="${detalle.estadoDetallePedidoDTO.valorUnitarioEstado}"/>
														</logic:empty>
													</logic:notEqual>
												</td>
												
												<td width="7%" class="fila_contenido columna_contenido" align="right">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorPrevioVenta}"/>
												</td>
												<td class="columna_contenido fila_contenido" width="2%" align="center">
													
													<c:set var="aplicaImpuesto" value="${articulo.aplicaImpuestoVenta}"/>							
													<c:if test="${aplicaImpuesto}">
														<img src="./images/tick.png" border="0">
													</c:if>
													<c:if test="${!aplicaImpuesto}">
														<img src="./images/x.png" border="0">
													</c:if>	
													
												</td>
												<td width="5%" class="fila_contenido columna_contenido" align="center">&nbsp;
													<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0">D</logic:greaterThan>
													<logic:lessThan name="detalle" property="estadoDetallePedidoDTO.valorFinalEstadoDescuento" value="0"><label class="textoRojo10">E</label></logic:lessThan>
												</td>
												<%--<td width="5%" class="fila_contenido columna_contenido" align="right">
													<bean:write name="detalle" property="estadoDetallePedidoDTO.valorUnitarioPOS" formatKey="formatos.numeros"/>
												</td>--%>
												<td width="7%" class="fila_contenido columna_contenido columna_contenido_der" align="right">
													<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorTotalVenta}"/>
												</td>
												<%-- prueba de datos
												<td width="7%" class="fila_contenido columna_contenido columna_contenido_der" align="right">
													<bean:write name="detalle" property="estadoDetallePedidoDTO.valorTotalEstado" formatKey="formatos.numeros"/> (<bean:write name="detalle" property="estadoDetallePedidoDTO.valorTotalEstadoIVA" formatKey="formatos.numeros"/>)
												</td>
												--%>
											</tr>
											<logic:notEmpty name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades">
												<tr>
													<td></td>
													<td colspan="11">
														<div id="marco${numeroRegistro}" class="displayNo">
															<table border="0" class="tabla_informacion_negro" width="50%" align="left" cellspacing="0" cellpadding="0">
																<tr>
																	<td align="center">
																		<table border="0" cellspacing="0" cellpadding="1" width="100%">
																			<tr class="tituloTablasCeleste"  align="left">
																				<td class="fila_contenido_negro" width="100%" align="center">DESCRIPCI&Oacute;N</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td>
																		<table border="0" cellspacing="0" cellpadding="1" width="100%">
																			<logic:iterate name="detalle" property="estadoDetallePedidoDTO.npDetalleUnidades" id="detalleUnidades" indexId="indiceDetalleCajas">
																				<bean:define id="fila" value="${indiceDetalleCajas + 1}"/>
																				<%-- control del estilo para el color de las filas --%>
																				<bean:define id="residuo" value="${indiceDetalleCajas % 2}"/>
																				<logic:equal name="residuo" value="0">
																					<bean:define id="clase" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuo" value="0">
																					<bean:define id="clase" value="grisClaro10"/>
																				</logic:notEqual>
																				<bean:define id="contador" value="${contador+1}" toScope="session"/>
																				<tr class="${clase}" id="fila_${indiceDetalleCajas}">
																					<td class="columna_contenido fila_contenido" width="100%" align="left"><bean:write name="detalleUnidades"/></td>
																				</tr>
																			</logic:iterate>
																		</table>
																	</td>
																</tr>
															</table>
														</div>
													</td>
												</tr>
											</logic:notEmpty>
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
	<tr height="10"><td></td></tr>
	<tr>
		<!-- secci&oacute;n de los totales -->
		<td colspan="2" align="center">
			<div id="pie_pedido">
				<table cellpadding="0" width="98%" cellspacing="0" border="0" class="tabla_informacion">
					<tr><td height="3px"></td></tr>
					<tr>	
						<td valign="top" width="41%">
							<%-- se muestra el detalle de los descuentos --%>
							<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/detalleDescuentos.jsp"/>
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
									<tr><td colspan="2" align="right" height="5px"></td></tr>
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
</table>