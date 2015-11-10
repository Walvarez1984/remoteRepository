<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
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
<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
	<tr>
	 <td height="10px"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		   <div style="width:98%;height:267px;overflow:auto;border-bottom:0px solid #cccccc">
			<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
				<tr align="left">
					<td colspan="4" class="textoAzul11" align="left">Items de la Canasta Original</td>	
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
										<bean:define id ="unidadManejo" value="${receta.articuloRelacion.unidadManejo}"/>
										<c:if test="${receta.articuloRelacion.habilitadoPrecioCaja}">
											<bean:define id ="unidadManejo" value="${receta.articuloRelacion.unidadManejo}"/>
										</c:if>
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
										<td width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${receta.precioTotalIMP}"/></td>
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
	<tr>
	 <td height="10px"></td>
	</tr>
	<tr>
	  <td align="center" colspan="2">  
	    <div style="width:98%;height:67px;overflow:auto;border-bottom:0px solid #cccccc">
			<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
					<tr align="left">
						<td colspan="4" class="textoAzul11" align="left">Items Eliminados de la Canasta Original</td>	
					</tr>
					<tr align="left">
						<td width="5%" class="tituloTablasRojo1" align="center">&nbsp;</td>
						<td width="15%" class="tituloTablasRojo1 columna_contenido" align="center">C&Oacute;DIGO BARRAS</td>
						<td width="41%" class="tituloTablasRojo1 columna_contenido" align="center">ART&Iacute;CULO</td>
						<td width="12%" class="tituloTablasRojo1 columna_contenido" align="center">CANTIDAD</td>
						<td width="10%" class="tituloTablasRojo1 columna_contenido" align="center">V.UNIT.</td>
						<td width="4%" class="tituloTablasRojo1 columna_contenido" align="center">IVA</td>
						<td width="11%" class="tituloTablasRojo1 columna_contenido" align="center">V.TOTAL</td>
					</tr>
					<tr>
						<td colspan="7">
							<table width="100%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
							  <logic:notEmpty name="ec.com.smx.sic.sispe.receta.itemEliminadosRecetaOriginal">
								<logic:iterate name="ec.com.smx.sic.sispe.receta.itemEliminadosRecetaOriginal" id="recetaE" indexId="indiceRegistroE">
									<bean:define id="residuo" value="${indiceRegistroE % 2}"/>
									<%--<logic:equal name="recetaE" property="articuloDTO.npImplemento" value="${estadoActivo}">
										<bean:define id="colorBack" value="violetaClaro10"/>
									</logic:equal>--%>
									<%--<logic:notEqual name="recetaE" property="articuloDTO.npImplemento" value="${estadoActivo}">--%>
										<logic:equal name="residuo" value="0">
											<bean:define id="colorBack" value="blanco10"/>
										</logic:equal>
										<logic:notEqual name="residuo" value="0">
											<bean:define id="colorBack" value="grisClaro10"/>
										</logic:notEqual>
									<%--</logic:notEqual>--%>
									<tr class="${colorBack} textoNegro11">
										<bean:define id="fila" value="${indiceRegistroE + 1}"/>
										<td width="5%" align="left" class="fila_contenido"><bean:write name="fila"/></td>
										<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaE" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
										<td width="41%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaE" property="articuloRelacion.descripcionArticulo"/></td>
										<td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="recetaE" property="cantidad"/></td>
										<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaE.articuloRelacion.precioBaseImp}"/></td>
										<td width="4%" align="center" class="columna_contenido fila_contenido">
											<c:set var="aplicaImpuestoE" value="${recetaE.articuloRelacion.aplicaImpuestoVenta}"/>							
											<c:if test="${aplicaImpuestoE}">
												<img src="./images/tick.png" border="0">
											</c:if>
											<c:if test="${!aplicaImpuestoE}">
												<img src="./images/x.png" border="0">
											</c:if>
										</td>
										<td  width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaE.precioTotalIMP}"/></td>
									</tr>
								</logic:iterate>
							 </logic:notEmpty>
							 <logic:empty name="ec.com.smx.sic.sispe.receta.itemEliminadosRecetaOriginal">
								<tr>
									<td width="100%" height="20px" align="center" style="vertical-align: middle;">
										<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
											<tr>
												<td width="100%"  class="tabla_informacion amarillo1">
													No tiene Items Eliminados
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
    <tr>
	 <td height="10px"></td>
	</tr>
	<tr>
	  <td align="center" colspan="2">  
	    <div style="width:98%;height:67px;overflow:auto;border-bottom:0px solid #cccccc">
			<table width="98%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
					<tr align="left">
						<td colspan="4" class="textoAzul11" align="left">Items Agregados//Modificados de la Canasta Original</td>	
					</tr>
					<tr align="left">
						<td width="5%" class="tituloTablasAzul" align="center">&nbsp;</td>
						<td width="15%" class="tituloTablasAzul columna_contenido" align="center">C&Oacute;DIGO BARRAS</td>
						<td width="41%" class="tituloTablasAzul columna_contenido" align="center">ART&Iacute;CULO</td>
						<td width="12%" class="tituloTablasAzul columna_contenido" align="center">CANTIDAD</td>
						<td width="10%" class="tituloTablasAzul columna_contenido" align="center">V.UNIT.</td>
						<td width="4%" class="tituloTablasAzul columna_contenido" align="center">IVA</td>
						<td width="11%" class="tituloTablasAzul columna_contenido" align="center">V.TOTAL</td>
					</tr>
					<tr>
						<td colspan="7">
							<table width="100%" border="0" align="center" class="tabla_informacion" cellpadding="0" cellspacing="0">
								<logic:notEmpty name="ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal">
									<logic:iterate name="ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal" id="recetaA" indexId="indiceRegistroA">
										<bean:define id="residuo" value="${indiceRegistroA % 2}"/>
										<logic:equal name="residuo" value="0">
											<bean:define id="colorBack" value="blanco10"/>
										</logic:equal>
										<logic:notEqual name="residuo" value="0">
											<bean:define id="colorBack" value="grisClaro10"/>
										</logic:notEqual>
										<tr class="${colorBack} textoNegro11">
											<bean:define id="fila" value="${indiceRegistroA + 1}"/>
											<td width="5%" align="left" class="fila_contenido"><bean:write name="fila"/></td>
											<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaA" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
											<td width="41%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaA" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/></td>
											<td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="recetaA" property="cantidadArticulo"/></td>
											<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaA.precioUnitarioIVA}"/></td>
											<td width="4%" align="center" class="columna_contenido fila_contenido">												
												<c:set var="aplicaImpuestoA" value="${recetaA.recetaArticuloDTO.articuloRelacion.aplicaImpuestoVenta}"/>							
												<c:if test="${aplicaImpuestoA}">
													<img src="./images/tick.png" border="0">
												</c:if>
												<c:if test="${!aplicaImpuestoA}">
													<img src="./images/x.png" border="0">
												</c:if>												
											</td>
											<td width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaA.precioTotalIVA}"/></td>
										</tr>
									</logic:iterate>	
								</logic:notEmpty>
								<logic:notEmpty name="ec.com.smx.sic.sispe.receta.itemModificadosRecetaOriginal">
									<logic:iterate name="ec.com.smx.sic.sispe.receta.itemModificadosRecetaOriginal" id="recetaM" indexId="indiceRegistroM">
										<bean:define id="residuo" value="${(indiceRegistroA + indiceRegistroM)% 2}"/>
											<logic:equal name="residuo" value="0">
												<bean:define id="colorBack" value="grisClaro10"/>
											</logic:equal>
											<logic:notEqual name="residuo" value="0">
												<bean:define id="colorBack" value="blanco10 "/>
											</logic:notEqual>
											 <tr class="${colorBack} textoNegro11">
												<logic:notEmpty name="ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal">
													<bean:define id="fila1" value="${indiceRegistroA + indiceRegistroM + 2}"/>
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal">
													<bean:define id="fila1" value="${indiceRegistroA + indiceRegistroM + 1}"/>
												</logic:empty>
												<td width="5%" align="left" class="fila_contenido"><bean:write name="fila1"/></td>
												<td width="15%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaM" property="recetaArticuloDTO.articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
												<td width="41%" align="left" class="columna_contenido fila_contenido"><bean:write name="recetaM" property="recetaArticuloDTO.articuloRelacion.descripcionArticulo"/></td>
												<td width="12%" align="center" class="columna_contenido fila_contenido"><label class="textoRojoE11"><B><bean:write name="recetaM" property="cantidadArticulo"/></B></label></td>
												<td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaM.precioUnitarioIVA}"/></td>
												<td width="4%" align="center" class="columna_contenido fila_contenido">
													<logic:equal name="recetaM" property="articuloDTO.aplicaImpuestoVenta" value="${estadoActivo}">
														<html:img page="/images/tick.png" border="0"/>
													</logic:equal>
													<logic:notEqual name="recetaM" property="articuloDTO.aplicaImpuestoVenta" value="${estadoActivo}">
														<html:img page="/images/x.png" border="0"/>
													</logic:notEqual>
												</td>
												<td width="11%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${recetaM.precioTotalIVA}"/></td>
											</tr>
									</logic:iterate>	
								</logic:notEmpty>
								<logic:empty name="ec.com.smx.sic.sispe.receta.itemAgregadosRecetaOriginal">
								  <logic:empty name="ec.com.smx.sic.sispe.receta.itemModificadosRecetaOriginal">	
									<tr>
										<td width="1000%" height="20px" align="center" style="vertical-align: middle;">
											<table border="0" cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td width="100%" height="100%" class="tabla_informacion amarillo1">
														No tiene Items Agregados/Modificados
													</td>
												</tr>
											</table>
										</td>
								    </tr>
								  </logic:empty>
							   </logic:empty>	
							</table>			
						</td>
				  </tr>
			 </table>
		 </div>
	   </td>
	 </tr>
	 <tr>
	  <td height="10px"></td>
	 </tr>	
</table>