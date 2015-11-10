<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<c:set var="formulario" value="cotizarRecotizarReservarForm" />
<c:set var="accion" value="crearCotizacion.do"/>
<bean:define id="articuloObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>

<logic:empty name="ec.com.smx.sic.sispe.pedido.direccionesAux">
	<c:set var="altoDivDetArt" value="400"/>
</logic:empty>
<bean:define id="indiceEntrega" value="${0}"/>

<div id="div_configuracionEntregasParciales" style="width:100%;height:465px;overflow-y:auto;overflow-x:hidden;" >
	<table class="fondoBlanco textoNegro11" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		<tr>
			<td id="mensaje2">
				<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajesPasos.jsp"/>
			</td>
		</tr>
	</table>
	<table class="textoNegro11" cellpadding="0" cellspacing="0"
		align="left" width="100%">
		<tr>
			<td height="3px"></td>
		</tr>
				
		
		
		
		<tr><td height="6px"></td></tr>
		<tr>
			<td valign="top">
							
				<logic:notEmpty name="ec.com.smx.sic.sispe.detallePedidoAux">
					<table border="0" cellspacing="0" cellpadding="0" class="tabla_informacion" width="98%" align="center">
						<tr align="left" height="18px">
							<td class="tituloTablas" align="right" width="4%"></td>
							<td class="tituloTablas" align="center" width="5%">
								
							</td>
							<td class="columna_contenido tituloTablas" align="center" width="39%">DESCRIPCI&Oacute;N ART&Iacute;CULO</td>
							<td class="columna_contenido tituloTablas" align="center" width="11%">CANT. TOTAL.</td>
							<td class="columna_contenido tituloTablasEntregaCliente" align="left" width="19%" colspan="2">
							ENT. CLIENTE
							<smx:text name="cotizarRecotizarReservarForm"  property="valorReplica"  styleClass="combos" size="3" value="" maxlength="5" onkeypress="return validarInputNumeric(event);" onkeydown="requestAjaxEnter('entregaLocalCalendario.do', ['listado_articulos','mensajesPopUp'], {parameters: 'replicarValores=ok',popWait:true,evalScripts:true});" />
							</td>
							<td class="columna_contenido tituloTablasPedidoCD" width="25%" colspan="2">&nbsp;&nbsp;&nbsp;PEDIR AL CD</td>
						</tr>
						<tr>
							<td colspan="9">
								<div id="listado_articulos" >
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
													
												</td>
												<td class="${clase} textoNegro10 columna_contenido_blanco" align="center" width="5%"><bean:write name="numFila"/></td>
												<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
												<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="39%" title="C&oacute;digo barras: ${articulo.codigoBarrasActivo.id.codigoBarras}">
													<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
														<img title="canasto modificado" src="images/estrella.gif" border="0">
												   </logic:notEmpty>
												   <c:if test="${detalle.articuloDTO.claseArticulo==articuloObsoleto}">
															<img src="images/obsoleto.png" border="0"  title="Este artículo es Obsoleto. Podrá reservar solo si tiene stock en su local o en otro local. Es su responsabilidad obtener este producto. ">
													</c:if>
													<bean:write name="articulo" property="descripcionArticulo"/>
												</td>
												<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="11%" id="cantidad_en_${numDetalle}"><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></td>
												<td class="${clase} textoNegro10 columna_contenido_blanco" align="right" width="8%">
													<logic:notEmpty name="ec.com.smx.sic.sispe.habilitarCantidadEntrega">
														<logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
															<logic:equal name="detalle" property="npHabilitarEntregaObsoletos" value="${true}">
																<smx:text name="cotizarRecotizarReservarForm" property="cantidadEstados" value="${detalle.estadoDetallePedidoDTO.npCantidadEstado}" styleClass="combos" size="3" maxlength="5" onkeypress="return validarInputNumeric(event);"/>
															</logic:equal>
															<logic:equal name="detalle" property="npHabilitarEntregaObsoletos" value="${false}">
																<html:hidden property="cantidadEstados" write="true" value="0"/>
															</logic:equal>
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
															<smx:text name="cotizarRecotizarReservarForm" property="cantidadPedidaBodega" value="${detalle.estadoDetallePedidoDTO.cantidadReservarSIC}" styleClass="combos" size="3" maxlength="5" onkeypress="return validarInputNumeric(event);"/>
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
													
												</td>
											</tr>											
										</logic:iterate>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</logic:notEmpty>
			</td>
		</tr>
		<tr><td height="6px"></td></tr>	
	</table>
</div>