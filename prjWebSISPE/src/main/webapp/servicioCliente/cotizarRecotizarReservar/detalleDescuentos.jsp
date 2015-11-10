<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos">
	<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" class="tabla_informacion" height="100%">
		<tr><td align="left" class="fila_titulo"><b>&nbsp;&nbsp;Descuentos</b></td></tr>
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" width="99%" align="left">
					<tr class="tituloTablas">
						<td align="center" class="columna_contenido" width="58%">DESCRIPCION</td>
						<%--<td align="center" class="columna_contenido" width="20%">V.TOTAL</td>--%>
						<td align="center" class="columna_contenido" width="8%">%</td>
						<td align="center" class="columna_contenido" width="14%">DESC.</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="fila_contenido">
				<div style="width:100%;height:50px;overflow:auto" id="div_DetalleDescuento">
					<table cellpadding="1" cellspacing="0" align="left" width="99%" class="tabla_informacion">
						<c:set var="totalDescuento" value="0"/>
						<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuento" indexId="numDescuento">
							<%-- control del estilo para el color de las filas --%>
							<bean:define id="residuo" value="${numDescuento % 2}"/>
							<logic:equal name="residuo" value="0">
								<bean:define id="colorBack" value="blanco10"/>
							</logic:equal>
							<logic:notEqual name="residuo" value="0">
								<bean:define id="colorBack" value="grisClaro10"/>
							</logic:notEqual>
							<tr class="${colorBack}">
								<td width="58%" align="left" class="columna_contenido fila_contenido"><bean:write name="descuento" property="descuentoDTO.tipoDescuentoDTO.descripcionTipoDescuento"/></td>
								<%--<td width="20%" align="right" class="columna_contenido fila_contenido"><bean:write name="descuento" property="valorPrevioDescuento" formatKey="formatos.numeros"/></td>--%>
								<td width="8%" align="right" class="columna_contenido fila_contenido">
									<logic:greaterThan name="descuento" property="porcentajeDescuento" value="0">
										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.porcentajeDescuento}"/>%
									</logic:greaterThan>
									<logic:equal name="descuento" property="porcentajeDescuento" value="0">---</logic:equal>
								</td>
								<td width="14%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuento.valorDescuento}"/></td>
							</tr>
							<c:set var="totalDescuento" value="${totalDescuento + descuento.valorDescuento}"/>
						</logic:iterate>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<table align="left" cellpadding="0" cellspacing="0" width="96%">
					<tr>
						<td align="right" class="textoRojo10" width="68%"><b>TOTAL:</b></td>
						<%--<td align="right" class="textoNegro10" width="20%"><b><bean:write name="ec.com.smx.sic.sispe.pedido.descuento.porcentajeTotal" formatKey="formatos.numeros"/>%</b></td>
						<td align="right" class="textoNegro10" width="20%"><b><bean:write name="cotizarRecotizarReservarForm" property="descuentoTotal" formatKey="formatos.numeros"/></b></td>--%>
						<td align="right" class="textoNegro10" width="20%"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalDescuento}"/></b></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</logic:notEmpty>