<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellpadding="0" cellspacing="5" class="fondoblanco">
    <tr>
		<td align="left" class="fondoBlanco">
			<table border="0" cellspacing="0" width="98%" cellpadding="0" >
				<tr>
					<td>
						&nbsp;&nbsp;<html:radio property="opDescuentoSeleccionadoConsolidado" value="">
							Procesar sin considerar estos descuentos
						</html:radio>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
        <td align="left" width="100%">
		<div id="div_descuentos" style="width:100%;height:280px;overflow-x:hidden;overflow-y:auto;">
			<table cellpadding="0" cellspacing="0" width="98%" border="0">
				<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.descuentos"> 
				<%--bean:define id="idPedido">
					<bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/>
				</bean:define--%>
				<tr>
                	<td align="center">
                		<table border="0" cellspacing="0" width="100%" cellpadding="1" class="tabla_informacion">
	                		<tr class="tituloTablas">
								<td  width="10%" align="center" class="columna_contenido">&nbsp;</td>
								<td  width="80%" align="left" class="columna_contenido">Descuento consolidado</td>
							</tr>
							<tr class="blanco10">
	                			<td class="columna_contenido fila_contenido"  width="10%" align="center">
									<html:radio property="opDescuentoSeleccionadoConsolidado" value="PO-DESCON"/>
								</td>
		                		<td class="columna_contenido fila_contenido" width="100%" align="left">
		                			Pedido consolidado
		                		</td>
	                		</tr>
							<tr  class="blanco10">
								<td colspan="2" align="center" class="columna_contenido fila_contenido">
									<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
										<tr class="tituloTablasCeleste">
											<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Tipo descuento</td>
											<td class="fila_contenido_negro columna_contenido_der_negro" align="center">% descuento</td>
										</tr>
										<logic:iterate name="ec.com.smx.sic.sispe.pedido.descuentos" id="descuentosEstados" indexId="indicePedido2">
											<%-- control del estilo para el color de las filas --%>
											<bean:define id="residuoR" value="${indicePedido2 % 2}"/>
											<logic:equal name="residuoR" value="0">
												<bean:define id="colorBack2" value="blanco10"/>
											</logic:equal>
											<logic:notEqual name="residuoR" value="0">
												<bean:define id="colorBack2" value="amarilloClaro10"/>
											</logic:notEqual>
											<tr class="${colorBack2} textoNegro10">
												<td align="center" class="columna_contenido fila_contenido">
													<bean:write name="descuentosEstados" property="npNombreDescuentoVariable"/>
												</td>
												<!-- <td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="descuentosEstados" property="porcentajeDescuento"/></td> -->
												<c:if test="${descuentosEstados.porcentajeDescuento != '0.0'}">
													<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="descuentosEstados" property="porcentajeDescuento"/></td>
												</c:if>
												<c:if test="${descuentosEstados.porcentajeDescuento == '0.0'}">
													<td align="center" class="columna_contenido fila_contenido">&nbsp;--&nbsp;</td>
												</c:if>	
											</tr>
										</logic:iterate>
									</table>
								</td>
							</tr>
						</table>
                	</td>
                	</tr>
                	<tr>
                		<td height="4px"></td>
                	</tr>
				</logic:notEmpty>
                	<tr>
	                <td align="center">
  						<table border="0" cellspacing="0" width="100%" cellpadding="1" class="tabla_informacion">
							<tr class="tituloTablas">
								<td width="10%" align="center" class="columna_contenido">&nbsp;</td>
								<td width="80%" align="left" class="columna_contenido">Código pedido a consolidar</td>
							</tr>
							<logic:iterate name="ec.com.smx.sic.sispe.vistaPedidosDescuentosConsolidados" id="vistaPedidoDTO" indexId="indicePedido">
							<logic:notEmpty name="vistaPedidoDTO" property="descuentosEstadosPedidos">
								<%-- control del estilo para el color de las filas --%>
								<bean:define id="residuo" value="${indicePedido % 2}"/>
								<logic:equal name="residuo" value="0">
									<bean:define id="colorBack" value="blanco10"/>
								</logic:equal>
								<logic:notEqual name="residuo" value="0">
									<bean:define id="colorBack" value="grisClaro10"/>
								</logic:notEqual>
								<tr class="${colorBack}">
									<td class="columna_contenido fila_contenido" width="10%" align="center"><html:radio property="opDescuentoSeleccionadoConsolidado" value="${indicePedido}-${vistaPedidoDTO.id.codigoPedido}"/></td>
									<td width="80%" class="columna_contenido fila_contenido" align="left">
										<bean:write name="vistaPedidoDTO" property="id.codigoPedido"/>
									</td>
								</tr>
								<tr class="${colorBack}">
									<td colspan="2" align="center" class="columna_contenido fila_contenido">
											<logic:notEmpty name="vistaPedidoDTO" property="descuentosEstadosPedidos">
												<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
													<tr class="tituloTablasCeleste">
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Tipo descuento</td>
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">% descuento</td>
													</tr>
													<logic:iterate name="vistaPedidoDTO" property="descuentosEstadosPedidos" id="descuentosEstados" indexId="indicePedido2">
														<%-- control del estilo para el color de las filas --%>
														<bean:define id="residuoR" value="${indicePedido2 % 2}"/>
														<logic:equal name="residuoR" value="0">
															<bean:define id="colorBack2" value="blanco10"/>
														</logic:equal>
														<logic:notEqual name="residuoR" value="0">
															<bean:define id="colorBack2" value="amarilloClaro10"/>
														</logic:notEqual>
														<tr class="${colorBack2} textoNegro10">
															<td align="center" class="columna_contenido fila_contenido">
																<bean:write name="descuentosEstados" property="npNombreDescuentoVariable"/>
															</td>
															<!-- <td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="descuentosEstados" property="porcentajeDescuento"/></td> -->
															<c:if test="${descuentosEstados.porcentajeDescuento != '0.0'}">
																<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="descuentosEstados" property="porcentajeDescuento"/></td>
															</c:if>
															<c:if test="${descuentosEstados.porcentajeDescuento == '0.0'}">
																<td align="center" class="columna_contenido fila_contenido">&nbsp;--&nbsp;</td>
															</c:if>
														</tr>
													</logic:iterate>
												</table>
											</logic:notEmpty>
									</td>
								</tr>
							</logic:notEmpty>
							</logic:iterate>
						</table>
	                </td>
                </tr>
            </table>
		</div>	
        </td>
    </tr>                                 
</table>	