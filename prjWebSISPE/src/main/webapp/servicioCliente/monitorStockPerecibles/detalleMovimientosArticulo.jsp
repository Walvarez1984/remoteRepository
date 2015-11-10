<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>
<html:form action="enviarOCPerecibles" method="post">
<TABLE border="0" cellspacing="0" cellpadding="0" align="center" width="100%">

		<html:hidden property="ayuda" value=""/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="crearCotizacion"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<tr>
			<td class="titulosAccion" height="35px">
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td  width="3%" align="center"><img src="./css/images/monitorPerecibles.gif" border="0"></img></td>
                        <td height="35" valign="middle">
                       		Monitor de stock/Perecibles
                        </td>
						<td align="right">
							<table cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="atrasA" action="enviarOCPerecibles" onclick="realizarEnvio('atrasStockPerecibles')" >Atras</html:link>
										</div>
									</td>
									<td>
									<td>
										<div id="botonA">
											<html:link  styleClass="actualizarA"  href="#"
												onclick="requestAjax('enviarOCPerecibles.do', ['pregunta','mensajes','idArtTemDet'], {parameters: 'actualizarDatosArticulo=ok', popWait:true, evalScripts:true});ocultarModal();">
                                                        Actualizar
                                             </html:link>
										</div>
									</td>
									<td>		
										<div id="botonA">
											<html:link  styleClass="nuevoA"  href="#"
												onclick="requestAjax('enviarOCPerecibles.do', ['pregunta','mensajes'], {parameters: 'registrarNuevoMovimiento=ok', popWait:false, evalScripts:true});ocultarModal();">
                                                        Nuevo
                                             </html:link>
										</div>
									</td>
									<td>
										<bean:define id="exit" value="ok"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="left" valign="top">
				<div id="idArtTemDet">
				<table  border="0" class="textoNegro11" align="center" width="98%" cellpadding="0" cellspacing="0">
				<bean:define id="articuloSeleccionado"  name="ec.com.smx.sic.sispe.articulo.perecible.seleccionado"/>	
					<tr><td height="5px"></td></tr>
					<tr>
						<td valign="top" width="100%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
								<tr>
									<td colspan="3">
										<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
											<tr>
												<td class="fila_titulo" height="20px">
													<table cellpadding="0" cellspacing="0" align="left" width="100%">
														<tr>
														<td align="left">&nbsp;Datos del art&iacute;culo: </td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table cellpadding="1" cellspacing="2" class="tabla_informacion" width="100%">
																										
														<tr>
															<td class="textoAzul11" align="left" width="15%">
																C&oacute;digo barras:
															</td>
															<td align="left" width="30%">
																${articuloSeleccionado.articulo.codigoBarrasActivo.id.codigoBarras}
															</td>
															<td width="55%">
																&nbsp;
															</td>
														</tr>
														
														<tr>
															<td class="textoAzul11" align="left">
																Descripci&oacute;n:
															</td>
															<td align="left">
																${articuloSeleccionado.articulo.descripcionArticulo}
															</td>
														</tr>
														
														<tr>
															<td class="textoAzul11" align="left">
																Proveedor:
															</td>
															<td align="left">
																${articuloSeleccionado.nombreProveedor}
															</td>
														</tr>
														
														
														
													</table>
												</td>
											</tr>
											
											<tr height="1px"><td></td></tr>
											
											<tr>
												<td>
													<table border="0" cellspacing="0" width="100%" class="tabla_informacion">
														<tr class="tituloTablas">
															<td width="14%" class="columna_contenido" align="center">Stock negociado.</td>
															<td width="14%" class="columna_contenido" align="center">Stock virtual.</td>
															<td width="14%" class="columna_contenido" align="center">Stock disponible locales.</td>
															<td width="14%" class="columna_contenido" align="center">Stock virtual reservado.</td>
															<td width="14%" class="columna_contenido" align="center">Stock virtual despachado.</td>
															<td width="14%" class="columna_contenido" align="center">Stock locales reservado.</td>
															<td width="14%" class="columna_contenido" align="center">Stock locales despachado.</td>
														</tr>
														
														<tr class="blanco10">
															
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockNegociado">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockNegociado" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockNegociado">
																	&nbsp;0
																</logic:empty>
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockDisponibleReservas">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockDisponibleReservas" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockDisponibleReservas">
																	&nbsp;0
																</logic:empty>																											
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockDisponibleLocales">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockDisponibleLocales" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockDisponibleLocales">
																	&nbsp;0
																</logic:empty>																											
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockReservadoReservas">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockReservadoReservas" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockReservadoReservas">
																	&nbsp;0
																</logic:empty>	
																																											
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockDespachadoReservas">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockDespachadoReservas" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockDespachadoReservas">
																	&nbsp;0
																</logic:empty>	
																																											
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockReservadoLocales">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockReservadoLocales" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockReservadoLocales">
																	&nbsp;0
																</logic:empty>	
																																							
															</td>
															<td align="center" class="columna_contenido">
																<logic:notEmpty name="articuloSeleccionado" property="stockDespachadoLocales">
																	&nbsp;<bean:write name="articuloSeleccionado" property="stockDespachadoLocales" formatKey="formatos.enteros"/>
																</logic:notEmpty>
																<logic:empty name="articuloSeleccionado" property="stockDespachadoLocales">
																	&nbsp;0
																</logic:empty>	
																																							
															</td>
																									
														</tr>
													
													</table>
												
												</td>
											
											</tr>
											
										</table>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<tr>
								
								
									
									
									<td valign="top" height="50%">
										<div id="resultadosBusqueda">
										<table id="detalleMovimientoKardex" width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
											<tr>
												<td class="fila_titulo" height="20px">
													<table cellpadding="0" cellspacing="0" align="left" width="100%">
														<tr>
														<td align="left">&nbsp;Movimientos: </td>
														</tr>
													</table>
												</td>
											</tr>																												
											 <tr>
												<td align="right" style="padding-right:20%;">													
														<smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro11" url="enviarOCPerecibles.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>														
												</td>
											</tr> 											
											<tr>
												<td>
													<table  border="0" cellspacing="0" width="85%" class="tabla_informacion">
														<tr class="tituloTablas">
															<td width="3%" class="columna_contenido" align="center">No</td>
															<td width="6%" class="columna_contenido" align="center">Fecha movimiento</td>
															<td width="13%" class="columna_contenido" align="center">Tipo movimiento</td>
															<td width="16%" class="columna_contenido" align="center">Motivo movimiento</td>
															<td width="8%" class="columna_contenido" align="center">Cantidad</td>
														<!--	<td width="10%" class="columna_contenido" align="center">&nbsp;</td> -->
														</tr>												
													</table>
												
												</td>
											
											</tr>
											
											<tr>
												<td>
													<div style="overflow-x:hidden;overflow-y:auto; height:282px; width:85%" >
														<table id="idArtTemKardex" border="0" cellspacing="0" width="100%" class="tabla_informacion">
															<logic:iterate name="ec.com.smx.sic.sispe.detalle.movimientos" id="movimientoKardex" indexId="numeroRegistro">
																<bean:define id="indiceGlobal" value="${numeroRegistro}"/>
																<bean:define id="fila" value="${indiceGlobal + listadoPedidosForm.start + 1}"/>
																<bean:define id="residuo" value="${numeroRegistro % 2}"/>
																<c:set var="clase" value="blanco10"/>
																<logic:notEqual name="residuo" value="0">
																	<c:set var="clase" value="grisClaro10"/>
																</logic:notEqual>
																
															<tr class="${clase}">
																<td width="3%" class="columna_contenido" align="center">
																	<bean:write name="fila"/>
																</td>
																<td width="6%" class="columna_contenido" align="center">
																	<bean:write name="movimientoKardex" property="fechaRegistro" formatKey="formatos.fechahora"/>
																</td>
																<td width="13%" class="columna_contenido" align="left" style="padding-left:3%;">
																	&nbsp;&nbsp;<bean:write name="movimientoKardex" property="articuloTemporadaDetalleDTO.tipoStockDTO.nombreStock"/>
																</td>
																<td width="16%" class="columna_contenido" align="left" style="padding-left:7%;">																																
																
																<c:if test="${movimientoKardex.codigoMotivoMovimiento!='ED' && movimientoKardex.codigoMotivoMovimiento!='EDL'}">
																<bean:write name="movimientoKardex" property="calendarioMotivoMovimientoDTO.codigoTipoMovimiento"/>
																</c:if>
																<c:if test="${movimientoKardex.codigoMotivoMovimiento=='ED' || movimientoKardex.codigoMotivoMovimiento=='EDL'}">
																	DES
																</c:if>
																	&nbsp;-&nbsp;									
																	<bean:write name="movimientoKardex" property="calendarioMotivoMovimientoDTO.descripcionMotivoMovimiento"/>
																																																																	
																</td>
																<td width="8%" class="columna_contenido" align="right">
																	<div style="margin-right:30px;">
																		<bean:write name="movimientoKardex" property="cantidad" formatKey="formatos.enteros"/>
																	</div>
																</td>
														<!--		<td width="10%" class="columna_contenido" align="center">
																	&nbsp;
																</td>-->
															</tr>
															
															</logic:iterate>
														
														</table>
													</div>
												</td>
											
											</tr>
											<tr><td>&nbsp;</td><!-- Quitar-->
											</tr>
										</table>
										</div>
									</td>
									
									
									
								</tr>								
							</table>
						</td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
</TABLE>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>