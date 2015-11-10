<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% String sessionId = session.getId(); %>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="idSesion" value="<%=sessionId%>"/>	
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<logic:notEmpty name="sispe.valor.iva">
	<bean:define id="valorIVA" name="sispe.valor.iva"/>
</logic:notEmpty>
<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="99%" align="center" bgcolor="white">
	<tr>
		<td colspan="2">
		<div style="width:98%;height:470px;overflow:auto;border-bottom:0px solid #cccccc">
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
							<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
								<logic:iterate name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta" id="receta" indexId="indiceRegistro">
									<bean:define id="residuo" value="${indiceRegistro % 2}"/>
										<logic:equal name="residuo" value="0">
											<bean:define id="colorBack" value="blanco10"/>
										</logic:equal>
										<logic:notEqual name="residuo" value="0">
											<bean:define id="colorBack" value="grisClaro10"/>
										</logic:notEqual>
										<bean:define id ="unidadManejo" value="${receta.recetaArticuloDTO.articuloRelacion.unidadManejo}"/>
										<logic:equal name="receta" property="recetaArticuloDTO.articuloRelacion.npHabilitarPrecioCaja" value="${estadoActivo}">
											<bean:define id ="unidadManejo" value="${receta.recetaArticuloDTO.articuloRelacion.unidadManejoCaja}"/>
										</logic:equal>
									<tr class="${colorBack} textoNegro11">
										<bean:define id="fila" value="${indiceRegistro + 1}"/>
										<td width="5%" align="left" class="fila_contenido"><bean:write name="fila"/></td>
										<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloDTO.codigoBarras"/></td>
										<td width="41%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloDTO.descripcionArticulo"/>,&nbsp;<bean:write name="receta" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/>,&nbsp;${unidadManejo}</td>
										<td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="receta" property="cantidadArticulo"/></td>
										<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioUnitarioIVA}"/></td>
										<td width="4%" align="center" class="columna_contenido fila_contenido">
											<logic:equal name="receta" property="articuloDTO.aplicaIVA" value="${estadoActivo}">
												<html:img page="/images/tick.png" border="0"/>
											</logic:equal>
											<logic:notEqual name="receta" property="articuloDTO.aplicaIVA" value="${estadoActivo}">
												<html:img page="/images/x.png" border="0"/>
											</logic:notEqual>
										</td>
										<td width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioTotalIVA}"/></td>
									</tr>
								</logic:iterate>	
							</logic:notEmpty>
							<logic:empty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
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
			</div>
		</td>
	</tr>
</table>