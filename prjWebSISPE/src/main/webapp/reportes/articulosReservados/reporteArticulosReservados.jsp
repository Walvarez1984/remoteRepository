<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:insert page="/include/top.jsp"/>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<html:form action="reporteArticulosReservados" method="post">
		<input type="hidden" name="ayuda" value="">
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<!-- Sección de pop-up de agrupaciójn de reportes de entregas -->
		<tr>
		<td style="position:relative;">
		<tiles:insert page="/include/mensajes.jsp"/>
		</td>
			<td>
				<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
					<tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
					<script language="javascript">mostrarModal();</script>
				</logic:notEmpty>
			</td>
		</tr>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="./images/reporteArticulosReservados.gif" border="0"></img></td>
						<td height="35" valign="middle">Reporte de Artículos Reservados</td>
						<td align="right" valign="top">
							<table border="0">
								<tr>
									<td>
										<div id="botonA">
									   		<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
									   	</div>
								  	</td>
									<td>
										<bean:define id="exit" value="exit"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<%--Cuerpo--%>
		<tr>
			<td align="center" valign="top">
				<table border="0" class="textoNegro12" width="99%" align="center" cellspacing="0" cellpadding="0">
					<tr><td height="7px" colspan="3"></td></tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos fila_contenido columna_contenido" id="izquierda" align="center" width="25%">
							<div style="width:100%;height:490px;">
								<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
							</div>
						</td>
						<td id="divisor" height="${alto_divs}">
							<span style="display:block" id="img_ocultar">
								<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
							</span>
							<span style="display:none" id="img_mostrar">
								<a href="#"><img src="./images/spliter_der.png" border="0"></a>
							</span>
						</td>
						<td class="datos fila_contenido columna_contenido_der" width="100%" id="derecha">
							<div style="width:100%;height:490px;">
								<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									<%--Titulo de los datos--%>
									<tr>
										<td class="fila_titulo">
											<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
												<tr>
													<td><img src="./images/entregar24.gif" border="0"/></td>
													<td height="23" width="100%">&nbsp;
														Lista de resultados
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<div id="resultadosBusqueda">
												 <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
													 <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">
															<tr>
																<td colspan="6">
																	<table cellpadding="0" cellspacing="0" width="100%">
																		<tr>
																			<td align="right" id="pag">
																				<smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro10" url="reporteArticulosReservados.do" requestAjax="'mensajes','pag','div_listado'"/>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<table border="0" cellspacing="0" cellpadding="1" width="100%">
																		<tr class="tituloTablas">
																			<td class="columna_contenido" width="4%" align="center">No.</td>
																			<td class="columna_contenido" width="15%" align="center">C&oacute;digo barras</td>
																			<td class="columna_contenido" width="25%" align="center">Descripci&oacute;n art&iacute;culo</td>
																			<td class="columna_contenido" width="26%" align="center">
																				<table border="0" cellspacing="0" cellpadding="1" width="100%">
																					<tr>
																						<td colspan="3">Cant. Reservada</td>
																					</tr>
																					<tr>
																						<td class="fila_contenido_dif">
																							<table border="0" cellspacing="0" cellpadding="1" width="100%">
																								<tr>
																									<td colspan="2" width="66%">Solicitada a CD</td>
																								</tr>
																								<tr>
																									<td class="fila_contenido_dif" width="33%">Enviada</td>
																									<td class="columna_contenido fila_contenido_dif" width="33%">No Enviada</td>
																								</tr>
																							</table>
																						</td>
																						<td class="columna_contenido fila_contenido_dif" align="center" width="33%">No solicitada a CD</td>
																					</tr>
																				</table>
																			</td>
																			<td class="columna_contenido" width="10%" align="center">Producido</td>
																			<td class="columna_contenido" width="10%" align="center">Despachado</td>
																			<td class="columna_contenido" width="10%" align="center">Stock</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
																		<table border="0" cellspacing="0" cellpadding="1" width="100%">
																			<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaArticuloDTO" indexId="indiceArticulo">
																				<bean:define id="indiceGlobal" value="${indiceArticulo + listadoPedidosForm.start}"/>
																				<bean:define id="numFila" value="${indiceGlobal + 1}"/>
																				<bean:define id="residuo" value="${indiceArticulo % 2}"/>
																				<logic:equal name="residuo" value="0">
																					<bean:define id="clase" value="blanco10"/>
																				</logic:equal>
																				<logic:notEqual name="residuo" value="0">
																					<bean:define id="clase" value="grisClaro10"/>
																				</logic:notEqual>
																				<tr class="${clase}">
																					<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
																					<td class="columna_contenido fila_contenido" width="15%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
																					<td class="columna_contenido fila_contenido" width="25%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
																					<td class="columna_contenido fila_contenido" width="26%" align="center">
																						<table cellspacing="0" cellpadding="0" width="100%">
																							<tr>
																								<td align="center" width="33%">
																									<bean:write name="vistaArticuloDTO" property="cantidadEnviado"/>
																								</td>
																								<td class="columna_contenido" align="center" width="33%">
																									<bean:write name="vistaArticuloDTO" property="cantidadNoEnviado"/>
																								</td>
																								<td class="columna_contenido" align="center" width="33%">
																									${vistaArticuloDTO.cantidadEntregarTotal - vistaArticuloDTO.cantidadDespacharTotal}
																								</td>
																							</tr>
																						</table>
																					</td>
																					<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO" property="cantidadParcialProTotal"/>/<bean:write name="vistaArticuloDTO" property="cantidadEnviado"/></td>
																					<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO" property="cantidadParcialDesTotal"/>/<bean:write name="vistaArticuloDTO" property="cantidadEnviado"/></td>
																					<td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center">
																						<logic:notEmpty name="vistaArticuloDTO" property="npStockArticulo">
																							<bean:write name="vistaArticuloDTO" property="npStockArticulo"/>
																						</logic:notEmpty>
																						<logic:empty name="vistaArticuloDTO" property="npStockArticulo">&nbsp;</logic:empty>
																					</td>
																				</tr>
																			</logic:iterate>
																		</table>
																	</div>
																</td>
															</tr>
													</logic:notEmpty>
													<logic:empty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">Seleccione un criterio de b&uacute;squeda</logic:empty>
						   						</table>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</td>
						<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
					</tr>
				</table>
			</td>
		</tr>
	</html:form>
</TABLE>
<tiles:insert page="/include/bottom.jsp"/>