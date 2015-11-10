<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
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
		<logic:notEmpty name="ec.com.smx.sic.sispe.articuloDTO">
			<tr>
				<td width="100%">
					<table border="0" align="center" cellspacing="0" class="textoNegro11 tabla_informacion" width="98%">
						<tr>
							<td class="textoAzul11" align="left" width="20%"><b>C&oacute;digo barras:</b></td>
							<td align="left">
								<bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="codigoBarrasActivo.id.codigoBarras"/>
							</td>
						</tr>
						<tr>
							<td class="textoAzul11" align="left"><b>Descripci&oacute;n:</b></td>
							<td align="left">
								<bean:write name="ec.com.smx.sic.sispe.articuloDTO" property="descripcionArticulo"/>
							</td>
						</tr>
						<tr>
							<td class="textoAzul11" align="left"><b>Precio Total:</b></td>
							<td align="left">
								<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.articuloDTO.precioBaseImp}"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
			<tr>
				<td>
					<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="2" cellspacing="0">
							<tr align="left">
								<td width="5%" class="tituloTablas" align="center">&nbsp;</td>
								<td width="15%" class="tituloTablas columna_contenido" align="center">C&Oacute;DIGO BARRAS</td>
								<td width="45%" class="tituloTablas columna_contenido" align="center">ART&Iacute;CULO</td>
								<td width="10%" class="tituloTablas columna_contenido" align="center">CANTIDAD</td>
								<td width="10%" class="tituloTablas columna_contenido" align="center">V.UNIT.</td>
								<td width="7%" class="tituloTablas columna_contenido" align="center">IVA</td>
								<td width="4%" class="tituloTablas columna_contenido" align="center">V.TOTAL</td>
							</tr>
					</table>
				</td>
			</tr>
		</logic:notEmpty>
		<logic:notEmpty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">	 
			<tr>
			  <td>
			  <logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
				 <div id="detallePedido" style="width:100%;height:380px;overflow:auto;border-bottom:1px solid #cccccc">		
			  </logic:empty>
					<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
							<logic:iterate name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta" id="receta" indexId="indiceRegistro">
								<bean:define id="residuo" value="${indiceRegistro % 2}"/>
								<%--<logic:equal name="receta" property="articuloDTO.npImplemento" value="${estadoActivo}">
									<bean:define id="colorBack" value="violetaClaro10"/>
								</logic:equal>--%>
								<%--<logic:notEqual name="receta" property="articuloDTO.npImplemento" value="${estadoActivo}">--%>
									<logic:equal name="residuo" value="0">
										<bean:define id="colorBack" value="blanco10"/>
									</logic:equal>
									<logic:notEqual name="residuo" value="0">
										<bean:define id="colorBack" value="grisClaro10"/>
									</logic:notEqual>
								<%--</logic:notEqual>--%>
								 <tr class="${colorBack} textoNegro11">
									<bean:define id="fila" value="${indiceRegistro + 1}"/>			   
									<td width="5%" align="left" class="fila_contenido"><bean:write name="fila"/></td>
									<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
									<td width="45%" align="left" class="columna_contenido fila_contenido"><bean:write name="receta" property="articuloRelacion.descripcionArticulo"/></td>
									<td width="10%" align="center" class="columna_contenido fila_contenido"><bean:write name="receta" property="cantidad"/></td>
									<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.articuloRelacion.precioBaseImp}"/></td>
									<td width="7%" align="center" class="columna_contenido fila_contenido">									
										<c:set var="aplicaImpuesto" value="${receta.articuloRelacion.aplicaImpuestoVenta}"/>
										<c:if test="${aplicaImpuesto}">
											<img src="./images/tick.png" border="0">
										</c:if>
										<c:if test="${!aplicaImpuesto}">
											<img src="./images/x.png" border="0">
										</c:if>	
									</td>
									<td width="6%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioTotalIMP}"/></td>
								 </tr>	
							</logic:iterate>
					 </table>
				   <logic:empty name="ec.com.smx.sic.sispe.calendarioBodega.sel">
				    </div>
				   </logic:empty>
				</td>
			</tr>
		</logic:notEmpty>	
		<logic:empty name="ec.com.smx.sic.sispe.estadoDetallePedido.detalleCanasta">
			<tr>
				<td width="72%" height="40px" align="center" style="vertical-align: middle;">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td width="72%" height="100%" class="tabla_informacion amarillo1">
								La receta no posee Items
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</logic:empty>
	    <tr>
			<td align="right" colspan="9">
				<table border="0" cellspacing="3" align="center" class="textoNegro11">
					<tr>
						<td>
						 <div id="botonD">
							<html:link styleClass="cancelarD" href="#" onclick="requestAjax('lineaProduccion.do',['mensajes','pregunta'],{parameters: 'cancelarNuevo=ok',evalScripts: true});ocultarModal();">Cancelar</html:link>
						 </div>
						</td>
					</tr>
				</table>
			</td>
		</tr>	   
</table>