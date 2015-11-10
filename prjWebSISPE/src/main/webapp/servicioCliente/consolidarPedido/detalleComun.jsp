<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
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

<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
	<table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
		<tr>
			<td align="center">
				<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
					<tr class="fila_titulo">
						<td colspan="4" class="textoNegro11 fila_contenido" align="left" height="20px"><b>Datos de la cabecera:</b></td>
					</tr>
					<tr>
						<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
							<table border="0" cellspacing="0" width="100%" align="left">
								<tr>
									<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<td></td>
												<td class="textoNegro11" width="20%" align="left">
													<label class="textoAzul11">No Pedido:&nbsp;</label>
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
												</td>
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
													<td align="left" class="textoNegro11" width="20%">
														<label class="textoAzul11">No Reservaci&oacute;n:&nbsp;</label>
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
													</td>
												</logic:notEmpty>
												<td align="left" class="textoNegro11" width="20%">
													<label class="textoAzul11">Entidad Responsable:&nbsp;</label>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
														<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
													</logic:equal>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
														<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
													</logic:equal>
												</td>
												<td align="left" class="textoNegro11" width="60%">
													<label class="textoAzul11">Precios afiliado:&nbsp;</label>
													<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">Si</logic:equal>
													<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">No</logic:notEqual>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado">
									<tr>
										<td class="textoAzul11" width="12%" align="right">Pedido relacionado:</td>
										<td align="left" class="textoNegro11" width="80%">
											<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado"/>
										</td>
									</tr>
								</logic:notEmpty>
								<tr>
									<td class="textoAzul11" align="right" width="12%">Contacto Empresa:</td>
									<td class="textoNegro11" align="left" width="80%">
										<b>CI:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto"/>&nbsp;-&nbsp;<b>NC:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto"/>&nbsp;-&nbsp;<b>TC:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoContacto"/>&nbsp;-&nbsp;<b>RUC:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="rucEmpresa"/>&nbsp;-&nbsp;<b>NE:</b>&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa"/>
									</td>
								</tr>
								<tr>
									<td class="textoAzul11" align="right">Elaborado en:</td>
									<td class="textoNegro11" align="left">
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
									</td>
								</tr>
								<tr>
									<td class="textoAzul11" align="right">Fecha de Estado:</td>
									<td class="textoNegro11" align="left">
										<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<table cellpadding="1" cellspacing="0" width="100%">
											<tr>
												<bean:define id="codigoEstadoReserva"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>
												<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
													<td class="textoAzul11" align="right" width="13%">Estado:</td>
													<td class="textoRojo11" align="left" width="18%">
														<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
													</td>
													<td class="textoAzul11" align="right" width="5%">Etapa:</td>
													<td class="textoNegro11" align="left">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="etapaEstadoActual"/>
													</td>
												</logic:notEqual>
												<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
													<td class="textoAzul11" align="right" width="13%">Estado:</td>
													<td class="textoRojo11" align="left" width="18%">
														<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
													</td>
													<td class="textoAzul11" align="right" width="5%">Etapa:</td>
													<td class="textoNegro11" align="left">
														<bean:define id="pedidoReservadoPCO"><bean:message key="estado.pedidoReservado.pendienteConfirmar"/></bean:define>
														<bean:define id="pedidoReservadoCON"><bean:message key="estado.pedidoReservado.confirmado"/></bean:define>
														<bean:define id="pedidoReservadoEXP"><bean:message key="estado.pedidoReservado.expirado"/></bean:define>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoPCO}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.PCO"/></bean:define>
														</logic:equal>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoCON}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.CON"/></bean:define>
														</logic:equal>
														<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoEXP}">
															<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.EXP"/></bean:define>
														</logic:equal>
														<bean:write name="descripcionEstadoReserva"/>
													</td>
												</logic:equal>
											</tr>
										</table>
									</td>
								</tr>
								<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
								<c:choose>
									<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
										<tr>
											<td class="textoAzul11" align="right">Valor abono inicial:</td>
											<td>
												<table cellpadding="0" cellspacing="0" align="left" class="textoNegro11">
													<tr>
														<td>
															<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.valorAbonoInicialManual}"/>
														</td>
														<td align="left">
															<label class="textoAzul11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valor Abonado:</label>&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.abonoPedido}"/>
														</td>
														<td align="left">
															<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(Enviado al CD)</label>
															</logic:equal>
															<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(No Enviado al CD)</label>
															</logic:notEqual>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</c:when>
								</c:choose>
							</table>
						</td>
					</tr>
				</TABLE>
			</td>
		</tr>
		<tr height="10"><td></td></tr>
		<tr>
		   <td>	
			<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
				<div id="detallePedido" style="width:100%;height:220px;overflow:auto;border-bottom:1px solid #cccccc">
			</logic:empty>	
					<table width="98%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
						<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
							<tr align="left" class="fila_titulo">
								<td colspan="2" class="textoNegro11" height="20px"><b>Detalle del pedido:</b></td>
							</tr>
							<tr>
								<td colspan="2" bgcolor="#F4F5EB">
									<table width="100%" border="0" cellpadding="1" cellspacing="0">
										<tr class="tituloTablas" height="15px">
											<td class="columna_contenido" align="center">&nbsp;</td>
											<td class="columna_contenido" align="center">No</td>
											<td class="columna_contenido" align="center">C&oacute;digo</td>
											<td class="columna_contenido" align="center">Art&iacute;culo</td>
											<td class="columna_contenido" align="center" title="Cantidad total a entregar">Cant.</td>
											<td class="columna_contenido" align="center" title="Cantidad reservada en bodega">Cant. Res.</td>
											<td class="columna_contenido" align="center" >Peso Kg.</td>
											<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoProduccion}">
												<c:set value="${columnas + 1}" var="columnas"/>
												<td class="columna_contenido" align="center" title="Cantidad producida hasta el momento">Cant. Prod.</td>
											</logic:equal>
											<td class="columna_contenido" align="center" >V.Unit.</td>
											<td class="columna_contenido" align="center" >Tot. Iva</td>
											<td class="columna_contenido" align="center" >Iva</td>
											<td class="columna_contenido" align="center" >Dscto.</td>
											<%--<td class="columna_contenido" align="center" >V.Unit Neto</td>--%>
											<td class="columna_contenido" align="center" >Tot. Neto</td>
										</tr>									
										<logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
											<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejo}"/>
											<logic:equal name="vistaDetallePedidoDTO" property="habilitarPrecioCaja" value="${estadoActivo}">
												<c:set var="unidadManejo" value="${vistaDetallePedidoDTO.articuloDTO.unidadManejoCaja}"/>
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
													<c:set var="imagen" value="pavo.gif"/>
												</logic:equal>
												<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
													<c:set var="pesoVariable" value="${estadoActivo}"/>
													<c:set var="imagen" value="balanza.gif"/>
												</logic:equal>
												<%-- secci&oacute;n que muestra las entregas y beneficiarios --%>
												<td class="fila_contenido">
													<logic:notEmpty name="vistaDetallePedidoDTO" property="entregas">
														<div id="desplegar${indiceDetalle}" >
															<a title="Ver entregas" href="#" onClick="mostrar(${indiceDetalle},'marco','plegar','desplegar');">
																<html:img src="images/desplegar.gif" border="0"/>
															</a>
														</div>
														<div id="plegar${indiceDetalle}" class="displayNo">
															<a title="Ocultar entregas" href="#" onClick="ocultar(${indiceDetalle},'marco','plegar','desplegar');">
																<html:img src="images/plegar.gif" border="0"/>
															</a>
														</div>
													</logic:notEmpty>
													<logic:empty name="vistaDetallePedidoDTO" property="entregas">&nbsp;</logic:empty>
												</td>
												<td align="left" class="columna_contenido fila_contenido"><bean:write name="numRegistro"/></td>
												<td class="columna_contenido fila_contenido" align="left">
													<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<html:link href="#" onclick="requestAjax('detalleEstadoPedido.do',['informacionOrdenCompra'],{parameters: 'indiceInfoOrdenCompra=${indiceDetalle}'});" title="Detalle de la orden de compra"><bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/></html:link>
														</logic:notEmpty>
														<logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
															<bean:write name="vistaDetallePedidoDTO" property="id.codigoArticulo"/>
														</logic:empty>
													</logic:empty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
														<bean:write name="vistaDetallePedidoDTO" property="id.codigoArticulo"/>
													</logic:notEmpty>
												</td>
												<td class="columna_contenido fila_contenido">
													<table border="0" width="100%" cellpadding="1" cellspacing="0">
														<tr>
															<%--<c:if test="${vistaDetallePedidoDTO.codigoTipoArticulo == tipoCanasto || vistaDetallePedidoDTO.codigoTipoArticulo == tipoDespensa}">--%>
															<c:if test="${vistaDetallePedidoDTO.codigoClasificacion == articuloEspecial || vistaDetallePedidoDTO.codigoClasificacion == canastoCatalogo}">
																<td align="left">
																	<%-- secci&oacute;n para obtener el c&oacute;digo de las canastas y crear el link --%>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<html:link title="Detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</html:link>
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<a href="#" title="Detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</a>
																	</logic:notEmpty>
																</td>
																<td valign="middle" align="right">
																	<bean:define id="imgSrc" value="canasto_lleno"/>
																	<bean:define id="codigoSubClasificacion" name="vistaDetallePedidoDTO" property="codigoSubClasificacion"/>
																	<c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																		<bean:define id="imgSrc" value="despensa_llena"/>
																	</c:if>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<html:link title="Detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><img src="./images/${imgSrc}.gif" border="0"></html:link>
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<a href="#" title="Detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><img src="./images/${imgSrc}.gif" border="0"></a>
																	</logic:notEmpty>
																	
																	<%--<logic:equal name="vistaDetallePedidoDTO" property="codigoTipoArticulo" value="${tipoDespensa}"><bean:define id="imgSrc" value="despensa_llena"/></logic:equal>
																	<logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<html:link title="detalle de la receta" action="estadoDetalleCanasto" paramId="indiceDetallePedido" paramName="indiceDetalle"><img src="./images/${imgSrc}.gif" border="0"></html:link>
																	</logic:empty>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
																		<a href="#" title="detalle de la receta" onclick="mostrarDetalleCanasta('${indiceDetalle}');"><img src="./images/${imgSrc}.gif" border="0"></a>
																	</logic:notEmpty> --%>
																</td>
															</c:if>
															<c:if test="${vistaDetallePedidoDTO.codigoClasificacion != articuloEspecial && vistaDetallePedidoDTO.codigoClasificacion != canastoCatalogo}">
																<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="vistaDetallePedidoDTO" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
																<logic:equal name="pesoVariable" value="${estadoActivo}">
																	<td align="right"><img title="Peso variable" src="./images/${imagen}" border="0"></td>
																</logic:equal>
															</c:if>
															<td width="2px">&nbsp;</td>
														</tr>
													</table>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="Cantidad total a entregar">
													<logic:notEqual name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<bean:write name="vistaDetallePedidoDTO" property="cantidadEstado"/>
													</logic:notEqual>
													<logic:equal name="vistaDetallePedidoDTO" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
														<label class="textoAzul10"><bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/></label>
													</logic:equal>
												</td>
												<td class="columna_contenido fila_contenido" align="center" title="Cantidad reservada en bodega">
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
													<td class="columna_contenido fila_contenido" align="center" title="Cantidad producida hasta el momento">
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
											<logic:notEmpty name="vistaDetallePedidoDTO" property="entregas">
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
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="5%" rowspan="2">Cant. Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="11%" rowspan="2">F. Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" colspan="2" title="Datos de producci&oacute;n">Producci&oacute;n</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" rowspan="2">F. Despacho</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="18%" rowspan="2">Destino Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%" rowspan="2">Contexto Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="17%" rowspan="2">Tipo Entrega</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="15%" rowspan="2">Stock Entrega</td>
																			</tr>
																			<tr class="tituloTablasCeleste">
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="Total solicitado">Tot.</td>
																				<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%" title="Total pendiente">Pend.</td>
																			</tr>
																			<logic:iterate name="vistaDetallePedidoDTO" property="entregas" id="entregaDTO" indexId="numEntrega">
																				<%-- control del estilo para el color de las filas --%>
																				<bean:define id="residuoE" value="${numEntrega % 2}"/>
																				<logic:equal name="residuoE" value="0">
																					<bean:define id="colorBack" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuoE" value="0">
																					<bean:define id="colorBack" value="amarilloClaro10"/>
																				</logic:notEqual>
																				<tr class="${colorBack}">
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="2%">
																						${numEntrega + 1}
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%">
																						<bean:write name="entregaDTO" property="cantidadEntrega"/>
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="11%">
																						<bean:write name="entregaDTO" property="fechaEntregaCliente" formatKey="formatos.fechahora"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="Total solicitado">
																						<bean:write name="entregaDTO" property="cantidadDespacho"/>
																					</td>
																					<td align="right" class="columna_contenido fila_contenido textoNegro10" width="5%" title="Total pendiente">
																						${entregaDTO.cantidadDespacho - entregaDTO.cantidadParcialDespacho}
																					</td>
																					<td align="center" class="columna_contenido fila_contenido textoNegro10" width="8%">
																						<bean:write name="entregaDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="18%">
																					   <table border="0" width="100%" cellpadding="1" cellspacing="0">
																						  <tr>
																						   <td>
																							   <logic:notEmpty name="entregaDTO" property="divisionDTO">
																							  	 <bean:write name="entregaDTO" property="divisionDTO.nombreDivGeoPol"/>-
																							   </logic:notEmpty>
																							   <bean:write name="entregaDTO" property="direccionEntrega"/>
																						   <td>
																						  </tr>
																						</table>	
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="8%" >
																						<logic:notEmpty name="entregaDTO" property="configuracionContextoDTO">
																							<bean:write name="entregaDTO" property="configuracionContextoDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionContextoDTO">&nbsp;</logic:empty>
																						<logic:equal name="entregaDTO" property="codigoLocalTransito" value="97">
																							<table border="0" width="100%" cellpadding="1" cellspacing="0">
																							  <tr>
																							   <td title="Bodega de tránsito">(Tráns)</td>
																							  </tr>
																							 </table> 
																						</logic:equal>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="17%">
																						<logic:notEmpty name="entregaDTO" property="configuracionAlcanceDTO">
																							<bean:write name="entregaDTO" property="configuracionAlcanceDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionAlcanceDTO">&nbsp;</logic:empty>
																					</td>
																					<td align="left"class="columna_contenido fila_contenido textoNegro10" width="15%">
																						<logic:notEmpty name="entregaDTO" property="configuracionStockDTO">
																							<bean:write name="entregaDTO" property="configuracionStockDTO.nombreConfiguracion"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDTO" property="configuracionStockDTO">&nbsp;</logic:empty>
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
		<tr height="5"><td></td></tr>
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
												<table border="0" cellspacing="0" cellpadding="2" class="tabla_informacion">
													<tr class="tituloTablas" align="left">
														<td align="center">DESCRIPCI&Oacute;N</td>
														<%--<td class="columna_contenido" align="center">V.TOTAL</td>--%>
														<td class="columna_contenido" align="center">%</td>
														<td class="columna_contenido" align="center">DESCUENTO</td>
													</tr>
													<c:set var="totalDescuento" value="0"/>
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
															<td align="center" class="columna_contenido fila_contenido"><b><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></b></td>
															<%--<td align="right" class="columna_contenido fila_contenido"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/></td>--%>
															<td align="right" class="columna_contenido fila_contenido">
																<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
																	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
																</logic:greaterThan>
																<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
															</td>
															<td align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
														</tr>
														<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
													</logic:iterate>
													<tr>
														<td class="textoRojo10" align="right" colspan="2"><b>TOTAL:</b></td>
														<%--<td class="textoAzul10 columna_contenido" align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="porcentajeTotalDescuento" formatKey="formatos.numeros"/>%</b></td>
														<td class="textoAzul10 columna_contenido" align="right"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descuentoTotalPedido" formatKey="formatos.numeros"/></b></td>--%>
														<td class="textoAzul10 columna_contenido" align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td colspan="2" height="5px"></td></tr>
									</table>
								</logic:notEmpty>
							</td>
							<td bordercolor="#ffffff">
								<table border="0" align="right" cellspacing="0" cellpadding="0">
									<tr height="14">
										<td class="textoAzul10" align="right">SUBTOTAL:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalPedido}"/>
										</td>
									</tr>
									<tr><td colspan="2" align="right">------------------------</td></tr>
									<tr height="14">
										<td class="textoAzul10" align="right">TARIFA 0%:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalNoAplicaIVA}"/>
										</td>
									</tr>
									<tr height="14">
										<td class="textoAzul10" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalAplicaIVA}"/>
										</td>
									</tr>
									<tr height="14">
										<td class="textoAzul10" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.ivaPedido}"/>
										</td>
									</tr>
									<tr>
										<td class="textoAzul10" align="right">COSTO FLETE:&nbsp;</td>
										<td class="textoNegro10" align="right">
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.valorCostoEntregaPedido}"/>
										</td>
									</tr>
									<tr><td colspan="2" align="right">------------------------</td></tr>
									<tr height="14">
										<td class="textoRojo11" align="right"><b>TOTAL:&nbsp;</b></td>
										<td class="textoNegro11" align="right">
											<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.totalPedido}"/></b>
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