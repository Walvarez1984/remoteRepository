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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<logic:notEmpty name="sispe.valor.iva">
	<bean:define id="valorIVA" name="sispe.valor.iva"/>
</logic:notEmpty>
<table border="0" width="100%" class="textos" align="left">
	<tr><td height="8"></td></tr>
	<html:hidden property="ayuda" value=""/>
	<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO">
		<tr>
			<td width="50%">
				<table border="0" align="center" cellspacing="0" class="textoNegro11 tabla_informacion" width="98%">
					<tr>
						<td class="textoAzul11" align="left" width="11%"><b>C&oacute;digo barras:</b></td>
						<td align="left">
							<bean:write name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO" property="codigoBarras"/>
						</td>
						<logic:notEmpty name="ec.com.smx.sic.sispe.canastaDTO">
							<td class="textoAzul11" align="left" width="17%"><b>C&oacute;digo barras original:</b></td>
							<td align="left">
								<bean:write name="ec.com.smx.sic.sispe.canastaDTO" property="codigoBarrasActivo.id.codigoBarras"/>
							</td>
						</logic:notEmpty>
					</tr>
					<tr>
						<td class="textoAzul11" align="left"><b>Descripci&oacute;n:</b></td>
						<td align="left">
							<bean:write name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO" property="descripcionArticulo"/>
						</td>
						<logic:notEmpty name="ec.com.smx.sic.sispe.canastaDTO">
							<td class="textoAzul11" align="left"><b>Descripci&oacute;n:</b></td>
							<td align="left">
								<bean:write name="ec.com.smx.sic.sispe.canastaDTO" property="descripcionArticulo"/>
							</td>
						</logic:notEmpty>
					</tr>
					<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO" property="valorUnitarioIVAEstado">
						<tr>
							<td class="textoAzul11" align="left"><b>Precio Total:</b></td>
							<bean:define id="valorUnitarioIVAEstado" name="ec.com.smx.sic.sispe.estadoDetallePedido.vistaDetallePedidoDTO" property="valorUnitarioIVAEstado"></bean:define>
							<td align="left">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorUnitarioIVAEstado}"/>
							</td>
							<logic:notEmpty name="ec.com.smx.sic.sispe.canastaDTO">
								<td class="textoAzul11" align="left"><b>Precio Total:</b></td>
								<bean:define id="precioBase" name="ec.com.smx.sic.sispe.canastaDTO" property="precioBase"></bean:define>
								<td align="left">
									<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${precioBase}"/>
								</td>
							</logic:notEmpty>
						</tr>
					</logic:notEmpty>
				</table>
			</td>
		</tr>
	</logic:notEmpty>
	<logic:empty name="ec.com.smx.sic.sispe.canastaDTO">
		<tr>
		<td colspan="2">		
			<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
				<tr align="left">
					<td colspan="4" class="textoAzul11" align="left">Items de la Canasta Estado Actual</td>	
				</tr>
				<tr align="left">
					<td width="5%" class="tituloTablas" align="center">&nbsp;</td>
					<td width="15%" class="tituloTablas columna_contenido" align="center">C&Oacute;DIGO BARRAS</td>
					<td width="41%" class="tituloTablas columna_contenido" align="center">ART&Iacute;CULO</td>
					<td width="12%" class="tituloTablas columna_contenido" align="center">CANTIDAD</td>
					<td width="10%" class="tituloTablas columna_contenido" align="center">V.UNIT.</td>
					<td width="4%" class="tituloTablas columna_contenido" align="center">IVA</td>
					<td width="11%" class="tituloTablas columna_contenido" align="center">V.TOTAL</td>
				</tr>	
				<tr>
					<td colspan="7">
						<table width="100%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
							<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanastaLP">
								<logic:iterate name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanastaLP" id="receta" indexId="indiceRegistro">
									<bean:define id ="unidadManejo" value="${receta.articuloRelacion.unidadManejo}"/>
									<logic:equal name="receta" property="articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
										<bean:define id ="unidadManejo" value="${receta.articuloRelacion.unidadManejoCaja}"/>
									</logic:equal>								
									<bean:define id="residuo" value="${indiceRegistro % 2}"/>
									<logic:equal name="residuo" value="0">
										<bean:define id="colorBack" value="blanco10"/>
									</logic:equal>
									<logic:notEqual name="residuo" value="0">
										<bean:define id="colorBack" value="grisClaro10"/>
									</logic:notEqual>
									<tr class="${colorBack} textoNegro11">
										<bean:define id="fila" value="${indiceRegistro + 1}"/>
										<td width="5%" align="left" class="fila_contenido"><bean:write name="fila"/></td>
										<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
										<td width="41%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="receta" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
										<td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="receta" property="cantidad"/></td>
										<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/></td>
										<td width="4%" align="center" class="columna_contenido fila_contenido">
											<c:set var="aplicaImpuesto" value="${receta.articuloRelacion.aplicaImpuestoVenta}"/>
											<c:if test="${aplicaImpuesto}">
												<img src="./images/tick.png" border="0">
											</c:if>
											<c:if test="${!aplicaImpuesto}">
												<img src="./images/x.png" border="0">
											</c:if>											
										</td>
										<td width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/></td>
									</tr>
								</logic:iterate>	
							</logic:notEmpty>
							<logic:empty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanastaLP">
								<tr>
									<td width="80%" height="40px" align="center" style="vertical-align: middle;">
										<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
											<tr>
												<td width="100%" height="100%" class="tabla_informacion amarillo1">
													La receta no posee Items
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</logic:empty>	
						</table>
					</td>
				</tr>
			</table>
		</td>
	  </tr>
	</logic:empty>			
	<logic:notEmpty name="ec.com.smx.sic.sispe.canastaDTO">
	  <tr>
        <td colspan="2" valign="top">
            <tiles:insert page="/controlesUsuario/controlTab.jsp"/>
        </td>
	  </tr>
	</logic:notEmpty>	
</table>