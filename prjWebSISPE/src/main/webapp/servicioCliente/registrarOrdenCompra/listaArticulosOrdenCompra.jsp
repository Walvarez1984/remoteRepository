<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<tr>
		<%--Datos--%>
		<td align="center" valign="top">
			<table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
				<tr><td height="7px" colspan="3"></td></tr>
				<%--Titulo de los datos--%>
				<tr>
					<TD class="datos" width="80%">
						<div id="resultadosBusqueda">
							<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
								<tr>
									<td class="fila_titulo" colspan="7">
										<table cellpadding="1" cellspacing="0" border="0" width="100%" align="center">
											<tr>
												<td width="3%">&nbsp;<img src="images/articulos24.gif" border="0"/></td>
												<td width="67%" align="left">
													<b>&nbsp;Lista de art&iacute;culos</b>
												</td>
												<logic:notEmpty name="ec.com.smx.sic.sispe.ordenesCompra.paginaArticulos">
													<td height="12%" align="right">
														<div id="botonD">
															<html:link title="Copiar num. orden y observaci&oacute;n" styleClass="copiarD" href="#" onclick="requestAjax('registrarOrdenCompra.do', ['mensajes','idCheckTodos','div_listado'], {parameters: 'copiarOrdenArticulos=ok'});">Copiar</html:link>
														</div>
													</td>
													<td height="12%" align="right">
														<div id="botonD">
															<html:link styleClass="guardarD" href="#" onclick="requestAjax('registrarOrdenCompra.do', ['mensajes','resultadosBusqueda'], {parameters: 'guardarOrdenCompraArt=ok',popWait:true});">Guardar</html:link>
														</div>
													</td>
												</logic:notEmpty>
												<td width="1%"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<tr>
									<td>
										<table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
											<logic:notEmpty name="ec.com.smx.sic.sispe.ordenesCompra.paginaArticulos">
												<tr>
													<td>
														<table cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td align="right" id="seg_pag">
																	<smx:paginacion start="${registrarOrdenCompraForm.start}" range="${registrarOrdenCompraForm.range}" results="${registrarOrdenCompraForm.size}" styleClass="textoNegro10" url="registrarOrdenCompra.do" requestAjax="'seg_pag','div_listado'"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td>
														<table border="0" cellspacing="0" cellpadding="1" width="100%">
															<tr class="tituloTablas"  align="left">
																<td class="columna_contenido" width="4%" align="center" id="idCheckTodos"><input type="checkbox" name="checkTodos" onclick="activarDesactivarTodo(this,registrarOrdenCompraForm.checkSeleccion);"></td>
																<td class="columna_contenido" width="4%" align="center">&nbsp;</td>
																<td class="columna_contenido" width="4%" align="center">No.</td>
																<td class="columna_contenido" width="15%" align="center">C&oacute;digo barras</td>
																<td class="columna_contenido" width="30%" align="center">Art&iacute;culo</td>
																<td class="columna_contenido" width="10%" align="center">Cantidad</td>
																<td class="columna_contenido" width="16%" align="center">No. orden</td>
																<td class="columna_contenido" width="20%" align="center">Observaci&oacute;n</td>
															</tr>
															<tr class="celesteFosforescente">
																<td width="63%" colspan="6" class="columna_contenido fila_contenido">&nbsp;</td>
																<td width="16%" class="columna_contenido fila_contenido" align="center">
																	<smx:text property="numeroOrdenArticulo" value="" styleClass="textNormal" size="20" maxlength="20"/>
																</td>
																<td width="21%" class="columna_contenido fila_contenido columna_contenido_der" align="center">
																	<smx:textarea property="observacionOrdenArticulo" styleClass="textNormal" value="" cols="25" rows="2"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td>
														<div id="div_listado" style="width:100%;height:355px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
															<table border="0" cellspacing="0" cellpadding="1" width="100%">
																<logic:iterate name="ec.com.smx.sic.sispe.ordenesCompra.paginaArticulos" id="vistaArticuloDTO" indexId="indiceArticulo">
																	<%-- control del estilo para el color de las filas --%>
																	<bean:define id="residuo" value="${indiceArticulo % 2}"/>
																	<bean:define id="indiceGlobal" value="${indiceArticulo + registrarOrdenCompraForm.start}"/>
																	<logic:equal name="residuo" value="0">
																		<bean:define id="clase" value="blanco10"/>
																	</logic:equal>
																	<logic:notEqual name="residuo" value="0">
																		<bean:define id="clase" value="grisClaro10"/>
																	</logic:notEqual>
																	<tr class="${clase}" id="fila_${indiceArticulo}">
																		<td class="columna_contenido fila_contenido" width="4%" align="center">
																			<html:multibox property="checkSeleccion" value="${indiceArticulo}"/>
																		</td>
																		<td class="columna_contenido fila_contenido" width="4%" align="center">
																			<logic:notEmpty name="vistaArticuloDTO" property="colVistaArticuloDTO">
																				<div style="display:block" id="desplegar_${indiceArticulo}">
																					<a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}']);show(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);"></a>
																				</div>
																				<div style="display:none" id="plegar_${indiceArticulo}">
																					<a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);show(['desplegar_${indiceArticulo}']);"></a>
																				</div>
																			</logic:notEmpty>
																		</td>
																		<td class="columna_contenido fila_contenido" width="4%" align="center">${indiceGlobal + 1}</td>
																		<td class="columna_contenido fila_contenido" width="15%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
																		<td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
																		<td class="columna_contenido fila_contenido" width="10%" align="right">
																			<logic:notEmpty name="vistaArticuloDTO" property="cantidadConOrdenCompra">
																				${vistaArticuloDTO.cantidadReservadaEstado - vistaArticuloDTO.cantidadConOrdenCompra}
																			</logic:notEmpty>
																			<logic:empty name="vistaArticuloDTO" property="cantidadConOrdenCompra">
																				<bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/>
																			</logic:empty>
																		</td>
																		<td class="columna_contenido fila_contenido" width="16%" align="center">
																			<smx:text property="numerosOrdenesArticulo" value="${vistaArticuloDTO.npNumeroOrdenCompra}" styleClass="textNormal" size="20" maxlength="20"/>
																		</td>
																		<td class="columna_contenido fila_contenido columna_contenido_der" width="20%" align="center">
																			<smx:textarea property="observacionOrdenesArticulo" value="${vistaArticuloDTO.npObservacionOrdenCompra}" styleClass="textNormal" cols="25" rows="2"/>
																		</td>
																	</tr>
																	<tr>
																		<td colspan="8">
																			<div id="listado_${indiceArticulo}" style="display:none">
																				<logic:notEmpty name="vistaArticuloDTO" property="colVistaArticuloDTO">
																					<table border="0" class="tabla_informacion_negro" width="70%" align="center" cellspacing="0" cellpadding="0">
																						<tr>
																							<td colspan="6">
																								<table border="0" cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion_negro">
																									<tr class="tituloTablas" align="left">
																										<td class="tituloTablasCeleste" width="5%" align="center">No</td>
																										<td class="tituloTablasCeleste" width="20%" align="center">No pedido</td>
																										<td class="tituloTablasCeleste" width="20%" align="center">No reserva</td>
																										<td class="tituloTablasCeleste" width="20%" align="center">Cantidad</td>
																										<td class="tituloTablasCeleste" width="15%" align="center">Fecha entrega</td>
																									</tr>
																								</table>
																							</td>
																						</tr>
																						<tr>
																							<td>
																								<table border="0" cellspacing="0" cellpadding="1" width="100%">
																									<logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
																										<%-- control del estilo para el color de las filas --%>
																										<bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
																										<logic:equal name="residuo2" value="0">
																											<bean:define id="clase2" value="blanco10"/>
																										</logic:equal>
																										<logic:notEqual name="residuo2" value="0">
																											<bean:define id="clase2" value="amarilloClaro10"/>
																										</logic:notEqual>
																										<tr class="${clase2}" id="fila_${indiceArticulo2}">
																											<td width="5%" align="center" class="columna_contenido fila_contenido">${indiceArticulo2 + 1}</td>
																											<td class="columna_contenido fila_contenido" width="20%" align="left"><html:link title="Ver detalle del pedido" onclick="realizarEnvio('IPVA-${indiceArticulo}-${indiceArticulo2}');" href="#"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link></td>
																											<td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>
																											<td class="columna_contenido fila_contenido" width="20%" align="right">
																												<logic:notEmpty name="vistaArticuloDTO2" property="cantidadConOrdenCompra">
																													${vistaArticuloDTO2.cantidadReservadaEstado - vistaArticuloDTO2.cantidadConOrdenCompra}
																												</logic:notEmpty>
																												<logic:empty name="vistaArticuloDTO2" property="cantidadConOrdenCompra">
																													<bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/>
																												</logic:empty>
																											</td>
																											<td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
																										</tr>
																									</logic:iterate>
																								</table>
																							</td>
																						</tr>
																					</table>
																				</logic:notEmpty>
																			</div>
																		</td>
																	</tr>
																</logic:iterate>
															</table>
														</div>
													</td>
												</tr>
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.ordenesCompra.paginaArticulos">
												<tr>
													<td colspan="5">Seleccione un criterio de b&uacute;squeda</td>
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
		</TD>
	</tr>
</table>