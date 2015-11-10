<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vMes" name="vtMes"  classname="java.lang.String" ignore="true"/>

<div style="position:relative;height:100%;width:100%;">
	<div id="div_mesCalendario_${vMes}" style="position:absolute;left:1%;top:5px;width:98%;height:40px;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
			<tr>
				<td height="40px" width="35%">
					<div style="width:100%;height:100%;overflow-y:auto;overflow-x:hidden;border-bottom-style:solid;border-bottom-width:1px;border-bottom-color:#A0A0A0;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
							<tr>
								<td align="center" height="10px" class="contenedorTitulo">
									INFORMACION
								</td>
							</tr>
							<tr>
								<td align="left" class="contenedorCuerpo">
									<table border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td width="3px">&nbsp;</td>
											<td class="mesActualColor" width="20px">&nbsp;</td>
											<td>&nbsp;Mes seleccionado</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td class="mesDistintoColor" width="20px">&nbsp;</td>
											<td>&nbsp;Mes distinto</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td class="diaActualColor" width="20px">&nbsp;</td>
											<td>&nbsp;D&iacute;a actual</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td class="diaSeleccionadoColor" width="20px">&nbsp;</td>
											<td>&nbsp;D&iacute;a seleccionado</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">E.DOM.</td>
											<td>&nbsp;Entrega a domicilio</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">D.LOC.</td>
											<td>&nbsp;Despacho a local</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">CE</td>
											<td>&nbsp;Producci&oacute;n canasta especial</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">DE</td>
											<td>&nbsp;Producci&oacute;n despensa especial</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">LC</td>
											<td>&nbsp;L&iacute;nea de producci&oacute;n canasta</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px">LD</td>
											<td>&nbsp;L&iacute;nea de producci&oacute;n despensa</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px" class="cantidadDiaPendienteP"><b>100</b></td>
											<td>&nbsp;Cantidad pendiente</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="20px" class="cantidadDiaAdicionalP"><b>100</b></td>
											<td>&nbsp;Cantidad adicional</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
											<td width="3px"><img src="./images/despensa_llena.gif"></td>
											<td class="textoNaranja10">Despensa</td>
										</tr>
										<tr>
											<td width="3px">&nbsp;</td>
										    <td width="3px"><img src="./images/canasto_lleno.gif" border="0"></td>
											<td class="textoNegro10">Canasta</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
				</td>
				<td width="1%"></td>
				<td height="100%" width="64%" class="cabeceraCalendario">
					<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
						<tr>
							<td width="10%" align="left">
								&nbsp;<logic:equal name="vMes" value="sel"><a href="#" class="textoCabeceraCalendario" title="Recorrer un mes hacia atr&aacute;s" onclick="requestAjax('calendarioBodega.do', ['div_mesCalendario_sel','div_diasCalendario_sel','div_mesCalendario_pos','div_diasCalendario_pos'], {parameters: 'mesAtras=ok', evalScripts: true});">&nbsp;&laquo;</a></logic:equal>
							</td>
							<td width="80%" align="center">
								<logic:notEmpty name="ec.com.smx.sic.sispe.mesSeleccionadoNombre.${vMes}">
									<bean:write name="ec.com.smx.sic.sispe.mesSeleccionadoNombre.${vMes}"/>								
								</logic:notEmpty>&nbsp;<logic:notEmpty name="ec.com.smx.sic.sispe.anioCalendario.${vMes}">
									<bean:write name="ec.com.smx.sic.sispe.anioCalendario.${vMes}"/>	
								</logic:notEmpty>
							</td>
							<td width="10%" align="right">
								&nbsp;<logic:equal name="vMes" value="sel"><a href="#" class="textoCabeceraCalendario" title="Recorrer un mes hacia adelante" onclick="requestAjax('calendarioBodega.do', ['div_mesCalendario_sel','div_diasCalendario_sel','div_mesCalendario_pos','div_diasCalendario_pos'], {parameters: 'mesAdelante=ok', evalScripts: true});">&raquo;&nbsp;</a></logic:equal>
							</td>
						</tr>				
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_semanaCalendario_${vMes}" style="position:absolute;left:1%;top:47px;width:98%;height:20px;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" style="font-size:14px;">
			<tr>
				<td width="14%" align="center"><b>LUNES</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>MARTES</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>MIERCOLES</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>JUEVES</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>VIERNES</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>SABADO</b></td>
				<td width="0.33%"></td>
				<td width="14%" align="center"><b>DOMINGO</b></td>
			</tr>
		</table>
	</div>
	<div id="div_diasCalendario_${vMes}" style="position:absolute;left:1%;top:69px;width:98%;height:100%;">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
			<logic:notEmpty name="ec.com.smx.sic.sispe.calendarioBodega.${vMes}">
				<%--bean:define id="pendiente" value="true"/--%>
				<logic:iterate name="ec.com.smx.sic.sispe.calendarioBodega.${vMes}" id="CalendarioDiaLocalDTO" indexId="contadorDias">
					<bean:define id="mod" value="${contadorDias % 7}" />
					<bean:define id="altoDia" value="19"/>
					<bean:size id="sizeCol" name="ec.com.smx.sic.sispe.calendarioBodega.${vMes}"/>
					<logic:notEqual name="sizeCol" value="35">
						<bean:define id="altoDia" value="15.66"/>
						<logic:equal name="sizeCol" value="28">
							<bean:define id="altoDia" value="24"/>
						</logic:equal>
					</logic:notEqual>
					<bean:define id="estiloDiaCalendario" value="mesActual"/>
					<logic:equal name="CalendarioDiaLocalDTO" property="npEsDistintoMes" value="true">
						<bean:define id="estiloDiaCalendario" value="mesDistinto"/>
					</logic:equal>
					<logic:equal name="CalendarioDiaLocalDTO" property="npEsFechaActual" value="true">
						<bean:define id="estiloDiaCalendario" value="diaActual"/>
						<%--bean:define id="pendiente" value="false"/--%>
					</logic:equal>
					<logic:equal name="CalendarioDiaLocalDTO" property="npEsSeleccionado" value="true">
						<bean:define id="estiloDiaCalendario" value="diaSeleccionado"/>
					</logic:equal>
					<logic:equal name="mod" value="0">
						<logic:notEqual name="contadorDias" value="0">
							</tr>
							<tr><td colspan="7" height="1%"></td></tr>
						</logic:notEqual>
						<tr>
					</logic:equal>
					<logic:notEqual name="mod" value="0">
						<td width="0.33%">&nbsp;</td>
					</logic:notEqual>
						
					<td width="14%" height="${altoDia}%" align="center" style="vertical-align: top;" class="diaCalendario">
						<div id="${estiloDiaCalendario}" align="top" style="width:100%; height:100%;">
							<a href="#" style="height:100%;width:100%;vertical-align:top;" onclick="onClickSeleccionarDia('${vMes},${contadorDias}', ${CalendarioDiaLocalDTO.npEsFechaActual}, ${CalendarioDiaLocalDTO.npEsSeleccionado});">
								<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" align="left" style="vertical-align: top;">
									<tr>
										<td align="right" width="100%" class="numeroDia" valign="top">
											${CalendarioDiaLocalDTO.npDiaMes}
										</td>
									</tr>
									
									<tr>
										<td valign="top">
											<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" style="vertical-align:top;">
												<logic:notEmpty name="CalendarioDiaLocalDTO" property="detalleCantidadCalendario">
													<tr>
														<td width="100%">
															<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
																<tr>
													<logic:iterate id="detalleDia" name="CalendarioDiaLocalDTO" property="detalleCantidadCalendario" indexId="numDetCanCal">
														<%--Seccion despacho/entrega--%>
														<logic:empty name="detalleDia" property="detalle">
																	<td>
																		<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
																			<tr>
																				<td align="center" title="${detalleDia.nombreCompletoCantidad}" class="textoCantidad">
																					<b>${detalleDia.nombreCortoCantidad}</b>
																				</td>
																			</tr>
																			<tr>
																				<td align="center" class="cantidadDia">
																					<bean:define id="estiloCantidadDia" value="cantidadDiaDE"/>
																					<%--logic:equal name="pendiente" value="true"--%>
																						<logic:greaterThan name="detalleDia" property="cantidad" value="0">
																							<bean:define id="estiloCantidadDia" value="cantidadDiaPendienteDE"/>
																						</logic:greaterThan>
																					<%--/logic:equal--%>
																					<b class="${estiloCantidadDia}">${detalleDia.cantidad}</b>
																				</td>
																			</tr>
																		</table>
																	</td>
														</logic:empty>
														<%--Seccion cierre de des/pro e incio seccion produccion--%>
														<logic:equal name="numDetCanCal" value="2">
																</tr>
															</table>
														</td>
													</tr>
													<tr><td height="3px"><hr width="90%" align="center" style="border-style:dashed;border-width:1px;border-color:#000000;"></td></tr>
													<tr>
														<td width="100%">
															<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
														</logic:equal>
														<%--Seccion produccion--%>
														<logic:notEmpty name="detalleDia" property="detalle">
															<tr>
																<td width="100%">
																	<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
																		<tr>
															<logic:iterate id="detalleCantidad" name="detalleDia" property="detalle">
																			<td align="center" title="${detalleCantidad.nombreCompletoCantidad}">
																				<bean:define id="estiloCantidadDia" value="cantidadDiaP"/>
																				<logic:notEmpty name="detalleCantidad" property="cantidad">
																					<bean:define id="cantidadImpresion" name="detalleCantidad" property="cantidad"/>
																				</logic:notEmpty>
																				
																				<logic:greaterThan name="cantidadImpresion" value="0">
																					<logic:equal name="detalleCantidad" property="nombreCortoCantidad" value="LC">
																						<bean:define id="estiloCantidadDia" value="cantidadDiaAdicionalP"/>
																					</logic:equal>
																					<logic:notEqual name="detalleCantidad" property="nombreCortoCantidad" value="LC">
																						<logic:equal name="detalleCantidad" property="nombreCortoCantidad" value="LD">
																							<bean:define id="estiloCantidadDia" value="cantidadDiaAdicionalP"/>
																						</logic:equal>
																						<logic:notEqual name="detalleCantidad" property="nombreCortoCantidad" value="LD">
																							<bean:define id="estiloCantidadDia" value="cantidadDiaPendienteP"/>
																						</logic:notEqual>
																					</logic:notEqual>
																				</logic:greaterThan>
																				
																				<logic:lessThan name="cantidadImpresion" value="0">
																					<bean:define id="cantidadImpresion" value="${cantidadImpresion * (-1)}"/>
																					<bean:define id="estiloCantidadDia" value="cantidadDiaPendienteP"/>
																				</logic:lessThan>
																				<%--logic:equal name="pendiente" value="true">
																					<logic:notEqual name="detalleCantidad" property="cantidad" value="0">
																						<bean:define id="estiloCantidadDia" value="cantidadDiaPendienteP"/>
																					</logic:notEqual>
																				</logic:equal--%>
																				<b class="textoCantidad">${detalleCantidad.nombreCortoCantidad}:</b><b class="${estiloCantidadDia}">${cantidadImpresion}</b>
																			</td>
															</logic:iterate>
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
										</td>
									</tr>
									<%--logic:notEmpty name="CalendarioDiaLocalDTO" property="detalleCantidadCalendario">
										<logic:iterate id="detalleDia" name="CalendarioDiaLocalDTO" property="detalleCantidadCalendario">
											<tr>
												<td width="100%">
													<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
														<tr>
															<td align="left" colspan="2" title="${detalleDia.nombreCompletoCantidad}">
																<b>&nbsp;${detalleDia.nombreCortoCantidad}:</b>
															</td>
														</tr>
														<logic:notEmpty name="detalleDia" property="detalle">
															<logic:iterate id="detalleCantidad" name="detalleDia" property="detalle" indexId="indiceCantidades">
																<bean:define id="modCan" value="${indiceCantidades % 2}"/>
																<bean:define id="estiloCantidadDia" value="cantidadDia"/>
																<logic:equal name="pendiente" value="true">
																	<logic:greaterThan name="detalleCantidad" property="cantidad" value="0">
																		<bean:define id="estiloCantidadDia" value="cantidadDiaPendiente"/>
																	</logic:greaterThan>
																</logic:equal>
																<logic:equal name="modCan" value="0">
																	<logic:notEqual name="indiceCantidades" value="0">
																		</tr>
																	</logic:notEqual>
																	<tr>
																</logic:equal>
																<td align="left" title="${detalleCantidad.nombreCompletoCantidad}">
																	&nbsp;&nbsp;<b>${detalleCantidad.nombreCortoCantidad}:</b>&nbsp;<b class="${estiloCantidadDia}">${detalleCantidad.cantidad}</b>
																</td>
															</logic:iterate>
															</tr>
														</logic:notEmpty>
													</table>
												</td>
											</tr>
										</logic:iterate>
									</logic:notEmpty--%>
								</table>
							</a>
						</div>
					</td>
				</logic:iterate>
				</tr>
			</logic:notEmpty>
		</table>
	</div>
</div>
