<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<table border="0" cellspacing="0" cellpadding="0" width="100%"  align="right">
	<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
	<tr>
		<td align="left" valign="top" width="100%" id="seccionDiferidos">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
				<tr>
					<td  width="3%" align="left"><img src="images/detalle_diferidos.gif" border="0"></img></td>
					<td height="35px" valign="middle">DETALLE DE ART&Iacute;CULOS QUE APLICAN DIFERIDOS (SIN INTERESES)</td>
				</tr>
			</table>
		</td>
	</tr>
	<logic:notEmpty name="ec.com.smx.sic.sispe.reporteDiferidos">
		<tr>
			<td>
				<c:set var="totalDiferidos" value="${0}"/>
				<c:set var="valorCero" value="0.00"/>
				<bean:size name="ec.com.smx.sic.sispe.diferidoCuotas" id="tamanoCuotas"/>
				<c:set var="valorColspan" value="${tamanoCuotas + 8}"/>
				<table width="100%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion" align="center">
					<tr>
						<td bgcolor="#ffffff">
							<table border="0" cellspacing="0" cellpadding="0" width="100%">
								<tr class="tituloTablas" height="15px">
									<td align="center" width="3%">No</td>
									<td align="center" width="12%">C&oacute;digo barras</td>
									<td align="center" width="30%">Art&iacute;culo</td>
									<td align="center" width="8%">Precio Comercio</td>
									<td align="center" width="8%">Precio Uni.No/Afi</td>
									<td align="center" width="8%">Precio Uni.Afi</td>
									<td align="center" width="8%">Cant.&nbsp;</td>
									<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO" indexId="indiceCuota">
										<td align="center" width="5%"><bean:write name="cuotaDTO" property="numeroCuotas"/>MESES&nbsp;</td>
									</logic:iterate>
									<td align="center" width="8%" valign="top">SUB-TOTAL</td>
								</tr>
								<tr>
									<td colspan="${valorColspan}">
										<div id="detalleDif" style="width:100%;height:220px;overflow:auto;border-bottom:1px solid #cccccc">
											<table border="0" cellspacing="0" cellpadding="0" width="100%">
												<c:set var="subTotal" value="${0}"/>
												<logic:iterate name="ec.com.smx.sic.sispe.reporteDiferidos" id="extructuraDiferidos" indexId="indiceDif">
													<bean:define id="fila" value="${indiceDif + 1}"/>
													<bean:define id="residuo" value="${indiceDif % 2}"/>
													<logic:equal name="residuo" value="0">
														<bean:define id="colorBack" value="blanco10"/>
													</logic:equal>
													<logic:notEqual name="residuo" value="0">
														<bean:define id="colorBack" value="grisClaro10"/>
													</logic:notEqual>
													<tr  class="${colorBack}" valign="middle" height="30px">
														<td  class="columna_contenido fila_contenido" width="3%" align="center"><bean:write name="fila"/></td>
														<td  class="columna_contenido fila_contenido" width="11%" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
														<td  class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/>,&nbsp;														
														<logic:empty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
														&nbsp;,
														</logic:empty>
														<logic:notEmpty name="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo">
														<bean:write name="extructuraDiferidos" property="detallePedidoDTO.articuloDTO.articuloComercialDTO.marcaComercialArticulo"/>,
														</logic:notEmpty>
														</td>
														<td  class="columna_contenido fila_contenido" width="8%" align="right">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.PVP}"/></td>
														<td  class="columna_contenido fila_contenido" width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseNoAfiImp}"/></td>
														<td  class="columna_contenido fila_contenido" width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseImp}"/></td>
														<td  class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="extructuraDiferidos" property="detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado"/></td>
														<logic:iterate name="extructuraDiferidos"  property="colDiferidos" id="duplex" indexId="indiceCuota1">
															<c:if test="${duplex.secondObject != valorCero}">
																<td class="columna_contenido fila_contenido" align="right" width="5%"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${duplex.secondObject}"/></td>
															</c:if>
															<c:if test="${duplex.secondObject == valorCero}">
																<td  class="columna_contenido fila_contenido" align="center" width="5%">-</td>
															</c:if>
														</logic:iterate>
														<c:set var="subTotal" value="${extructuraDiferidos.detallePedidoDTO.estadoDetallePedidoDTO.cantidadEstado * extructuraDiferidos.detallePedidoDTO.articuloDTO.precioBaseImp}"/>
														<c:set var="totalDiferidos" value="${totalDiferidos + subTotal}"/>
														<td class="columna_contenido fila_contenido" width="8%" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${subTotal}"/></td>
													</tr>						
												</logic:iterate>
											</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td bgcolor="#ffffff" height="10px"></td></tr>
					<tr>
						<td  colspan="14"  bgcolor="#ffffff">
							<table  border="0" cellspacing="0" cellpadding="0" align="right">
								<tr>
									<td class="textoRojo10" align="right">TOTAL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									<td  align="right">
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDiferidos}"/>
									</td>
								</tr>
								<tr><td colspan="2" align="right">-----------------------</td></tr>
								<logic:iterate name="ec.com.smx.sic.sispe.diferidoCuotas"  id="cuotaDTO1" indexId="indiceCuota">
									<tr>
									 <td class="textoAzul10" align="right"><bean:write name="cuotaDTO1" property="numeroCuotas"/>&nbsp;MESES:&nbsp;&nbsp;&nbsp;&nbsp;</td>
									 <c:if test="${totalDiferidos >= cuotaDTO1.valorMinimo}">
										<c:set var="totalMonto" value="${(totalDiferidos/cuotaDTO1.numeroCuotas)*cuotaDTO1.valorInteres}"/>
										<td  align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalMonto}"/></td>
									 </c:if>
									 <c:if test="${totalDiferidos < cuotaDTO1.valorMinimo}">
										<td  align="right">-</td>
									 </c:if>
									</tr>
								</logic:iterate>
								<tr><td height="10px"></td></tr>
							</table>
						</td>
					</tr>	
				</table>
			</td>
		</tr>
	</logic:notEmpty>
	<tr><td height="5px"></td></tr>
	<tr>
		<td align="center">
			<div id="botonD">
				<html:link styleClass="cancelarD" href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'cancelarDiferido=ok', evalScripts:true});ocultarModal();">Cancelar</html:link>
			</div>
		</td>
	</tr>
</table>