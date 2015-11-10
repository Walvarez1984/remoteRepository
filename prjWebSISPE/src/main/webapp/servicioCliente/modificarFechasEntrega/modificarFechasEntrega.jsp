<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>
<%-- lista de definiciones para las acciones --%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="opEmpresarial"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.empresarial"/></bean:define>
<c:set var="cedulaCto" value="SN"></c:set>
<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="cedulaContacto"  >
<c:set var="cedulaCto" ><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="cedulaContacto" ></bean:write></c:set>
</logic:notEmpty>

<html:form action="modificarFechasEntrega">
	<TABLE border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
		<html:hidden property="ayuda" value=""/>
		<tr>
			<td class="titulosAccion" height="35px">
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="3%" align="center"><img src="images/temporada48.gif" border="0"></img></td>
						<td align="left">Modificaci&oacute;n de Fechas de Entrega</td>
						<td align="right">
							<table cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('guardar');" title="guardar modificaciones">Guardar</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link styleClass="cancelarA" href="#" onclick="realizarEnvio('cancelar');" title="volver a la p&aacute;gina de b&uacute;squeda">Cancelar</html:link>
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
			<td align="center" valign="top">
				<table border="0" class="textoNegro11" align="center" width="98%" cellpadding="0" cellspacing="0">
					<tr><td height="5px"></td></tr>
					<tr>
						<td valign="top" width="100%">
							<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
								<tr>
									<td colspan="3">
										<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
											<tr>
												<td class="fila_titulo" height="20px" >
													<table cellpadding="0" cellspacing="0" align="left" width="100%">
														<tr>						
															<td align="right" class="textoRojo11">No Pedido:&nbsp;<label class="textoAzul10"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/></label></td>
															<td align="right" class="textoRojo11">No Reserva:&nbsp;<label class="textoAzul10"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="llaveContratoPOS"/></label></td>
															<td align="right" class="textoRojo11" id="local" width="20%"><b>Local:</b>&nbsp;
																<label class="textoAzul10"><b><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombreLocal"/></b>&nbsp;</label>
															</td>
													   </tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>	
								<tr>																		
									<!-- DATOS DE LA EMPRESA -->		
									<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombreEmpresa">
										<td valign="top" width="50%">						
											<table border="0" cellspacing="0" class="textoNegro11" align="left">
												<tr>
													<td colspan="2" align="left" class="textoAzul11"><b>Datos de la Empresa:</b></td>
												</tr>
												<tr><td height="6"></td></tr>
												<tr>
													<td class="textoAzul11" align="right">RUC:&nbsp;</td>												
													<td align="left">																																	
														<html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="rucEmpresa" write="true"/>
													</td>
												</tr>
												<tr>															
													<td class="textoAzul11" align="right">Raz&oacute;n social:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombreEmpresa" write="true"/></td>
												</tr>	
												<tr>															
													<td class="textoAzul11" align="right">Tel&eacute;fono:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="telefonoEmpresa" write="true"/></td>
												</tr>												
											</table>										
										</td>
									</logic:notEmpty>	
									
									<!-- DATOS DE LA PERSONA -->		
									<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombrePersona">
										<td valign="top" width="50%">						
											<table border="0" cellspacing="0" class="textoNegro11" align="left">
												<tr>
													<td colspan="2" class="textoAzul11" align="left"><b>Datos del cliente:</b></td>
												</tr>
												<tr><td height="6"></td></tr>
												<tr>
													<td class="textoAzul11" align="right">CI/Pasaporte:&nbsp;</td>												
													<td align="left">																																	
														<html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="numeroDocumentoPersona" write="true"/>
													</td>
												</tr>
												<tr>															
													<td class="textoAzul11" align="right">Nombre:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombrePersona" write="true"/></td>
												</tr>	
												<tr>															
													<td class="textoAzul11" align="right">Tel&eacute;fono:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="telefonoPersona" write="true"/></td>
												</tr>	
												<tr>															
													<td class="textoAzul11" align="right">Email:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="emailPersona" write="true"/></td>
												</tr>											
											</table>										
										</td>
									</logic:notEmpty>
									
									<c:if test="${cedulaCto == 'SN'}">
										<td valign="top" width="50%">						
											<table border="0" cellspacing="0" class="textoNegro11" align="left">
												<tr>
													<td colspan="2" class="titulo" align="left"><b>Datos del contacto:</b></td>
												</tr>
												<tr><td height="6"></td></tr>
												<tr>
													<td><b><label class="textoRojo11">SIN CONTACTO</label></b></td>																		
												</tr>												
											</table>										
										</td>
									</c:if>
									
									<!-- DATOS DEL CONTACTO -->		
									<c:if test="${cedulaCto != 'SN'}">
										<td valign="top" width="50%">						
											<table border="0" cellspacing="0" class="textoNegro11" align="left">
												<tr>
													<td colspan="2" class="textoAzul11" align="left"><b>Datos del contacto:</b></td>
												</tr>
												<tr><td height="6"></td></tr>
												<tr>
													<td class="textoAzul11" align="right">CI/Pasaporte:&nbsp;</td>												
													<td align="left">																																	
														<html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="cedulaContacto" write="true"/>
													</td>
												</tr>
												<tr>															
													<td class="textoAzul11" align="right">Nombre contacto:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="nombreContacto" write="true"/></td>
												</tr>	
												<tr>															
													<td class="textoAzul11" align="right">Tel&eacute;fono contacto:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="telefonoContacto" write="true"/></td>
												</tr>		
												<tr>															
													<td class="textoAzul11" align="right">Email contacto:&nbsp;</td>
													<td align="left"><html:hidden name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="emailContacto" write="true"/></td>
												</tr>											
											</table>										
										</td>
									</c:if>	
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="5px"></td></tr>
					<tr>
						<td>
							<table class="textoNegro11 tabla_informacion" cellpadding="0" cellspacing="0" align="left" width="100%">
								<tr><td class="fila_titulo" height="20px" align="left">Detalle de las Entregas</td></tr>
								<tr><td height="5px"></td></tr>
								<tr>
									<td>										
										<table width="95%" cellspacing="0">
											<tr><td align="left">Las fechas est&aacute;n en formato a&ntilde;o-mes-dia (aaaa-mm-dd)</td></tr>	
											<tr>											
												<table border="0" cellspacing="0" cellpadding="0" width="98%" align="center" style="border-top:#cccccc 1px solid" >									
													<tr>
														<td colspan="9" >
															<div id="listado_articulos" style="height:325px;overflow-x:hidden;overflow-y:auto;">
																<table border="0" cellspacing="0" cellpadding="0" width="100%">																	
																	<bean:define id="contadorCheckTransito" value="0"></bean:define>
																	<logic:iterate name="ec.com.smx.sic.sispe.entregasResp" id="entrega" indexId="numDetalle">
																		<bean:define id="contadorSeleccion" value="${0}"/>
																		<bean:define id="numFila" value="${numDetalle + 1}"/>
																		<bean:define id="residuo" value="${numDetalle % 2}"/>
																		<c:set var="clase" value="blanco10"/>
																		<c:set var="colorBack" value="#ffffff"/>
																		<logic:notEqual name="residuo" value="0">
																			<c:set var="clase" value="grisClaro10"/>
																			<c:set var="colorBack" value="#EBEBEB"/>
																		</logic:notEqual>
																		<tr>
																			<td>
																				<table id="table_${numDetalle}" cellspacing="0" cellpadding="0" width="100%" style="border-width: 1px;border: 1px solid; border-color: #7de44a;">
																					<tr id="fila_en_${numDetalle}" >
																						<td width="2%" align="center" height="25"  class="${clase} textoNegro10 columna_contenido_blanco">
																							<div id="desplegar${numDetalle}">
																								<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
																									<a title="Ver Detalle de Entregas" href="#" onClick="plegarA(${numDetalle});">
																										<html:img src="images/plegar.gif" border="0"/>
																									</a>
																								</logic:notEmpty>
																							</div>
																							<div id="plegar${numDetalle}" class="displayNo">
																								<a title="Ver Detalle de Entregas" href="#" onClick="desplegarA(${numDetalle});">
																									<html:img src="images/desplegar.gif" border="0"/>
																								</a>
																							</div>
																						</td>
																						<td class="${clase} textoNegro10 columna_contenido_blanco" align="center" width="3%"><bean:write name="numFila"/></td>
																						
																						<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="40%">
																							Direcci&oacute;n entrega:&nbsp;&nbsp;
																							<bean:define id="direccionEntrega">
																								<bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/>
																							</bean:define>
																							<logic:notEmpty name="entrega" property="entregaPedidoDTO.codigoLocalSector">
																								<input type="text" name="txtde_${numDetalle}" value="${direccionEntrega}" size="50" class="textNormal"/>
																							</logic:notEmpty>
																							<logic:empty name="entrega" property="entregaPedidoDTO.codigoLocalSector">
																								<bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/>
																								<input type="hidden" name="txtde_${numDetalle}" value="${direccionEntrega}">
																							</logic:empty>
																						</td>
																						<bean:define id="fechaEntrega">
																							<bean:write name="entrega" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fecha"/>
																						</bean:define>
																						<bean:define id="fechaDespacho">
																							<bean:write name="entrega" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/>
																						</bean:define>
																						<logic:equal name="entrega" property="cantidadDespacho" value="0">
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="15%">NA</td>
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="10%">NA</td>
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="15%">Fecha entrega:&nbsp;&nbsp;<bean:write name="entrega" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fecha"/></td>
																						</logic:equal>
																						<logic:notEqual name="entrega" property="cantidadDespacho" value="0">
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="15%">Fecha Despacho:&nbsp;&nbsp;<input type="text" name="txtfd_${numDetalle}" value="${fechaDespacho}" size="12" maxlength="10" class="textNormal"/></td>
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="10%">Can. Despacho:&nbsp;&nbsp;<bean:write name="entrega" property="cantidadDespacho"/></td>
																							<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="15%">Fecha entrega:&nbsp;&nbsp;<input type="text" name="txtfe_${numDetalle}" value="${fechaEntrega}" size="12" maxlength="10" class="textNormal"/></td>
																						</logic:notEqual>
																						<td class="${clase} textoNegro10 columna_contenido_blanco" align="left" width="15%">Can. Entrega:&nbsp;&nbsp;<bean:write name="entrega" property="cantidadEntrega"/></td>
																					</tr>
																					<tr>
																						<td colspan="9">
																							<div id="marco${numDetalle}">
																								<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
																									<table border="0" width="100%" cellpadding="0" cellspacing="0">
																										<tr>
																											<td width="5%"></td>
																											<td align="left">
																												<table border="0" width="99%" cellspacing="0" cellpadding="0">
																													<tr>
																														<td>
																															<table class="tabla_informacion" border="0" width="100%" cellspacing="0" cellpadding="0">
																																<tr>
																																	<td width="5%" align="center" class="tituloTablas columna_contenido">No</td>
																																	<td width="20%" align="center" class="tituloTablas columna_contenido" >C&Oacute;DIGO BARRAS</td>
																																	<td width="65%" align="left" class="subtituloTablasEntregaCliente columna_contenido" >DESCRIPCI&Oacute;N ART&Iacute;CULO</td>
																																	<td width="10%" align="center" class="subtituloTablasEntregaCliente columna_contenido">CANTIDAD</td>	
																																</tr>
																															</table>
																														</td>
																													</tr>
																													<tr>																														
																														<td width="100%" align="left" colspan="6">																								
																															<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="left">
																																<logic:iterate name="entrega" property="entregaPedidoDTO.npDetallePedido" id="detalle" indexId="numEntrega">
																																	<bean:define id="fila" value="${numEntrega + 1}"/>
																																	<bean:define id="residuoE" value="${numEntrega % 2}"/>
																																	<c:set var="indiceEntrega" value="${indiceEntrega + 1}"/>
																																	<logic:equal name="residuoE" value="0">
																																		<bean:define id="colorBack" value="grisClaro10"/>
																																	</logic:equal>
																																	<logic:notEqual name="residuoE" value="0">
																																		<bean:define id="colorBack" value="blanco10"/>
																																	</logic:notEqual>
																																	<bean:define name="detalle" property="articuloDTO" id ="articulo"/>
																																	<bean:define name="detalle" property="estadoDetallePedidoDTO" id ="estadoDetallePedidoDTO"/>
																																	<tr class="${colorBack}">
																																		<td width="5%" align="center" class="textoNegro10 ">${fila}</td>										
																																		<td width="20%" align="left" class="textoNegro9">
																																			<bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/>
																																		</td>
																																		<td width="65%" align="left" class="textoNegro9" title="C&oacute;digo barras: ${articulo.codigoBarrasActivo.id.codigoBarras}">
																																			<bean:write name="articulo" property="descripcionArticulo"/>
																																		</td>																											
																																		<td width="10%" align="center" class="textoNegro9">
																																			<bean:write name="estadoDetallePedidoDTO" property="cantidadEstado"/>
																																		</td>																										
																																	</tr>
																																</logic:iterate>
																															</table>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																									</table>
																								</logic:notEmpty>
																							</div>	
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<logic:notEmpty name="entrega" property="entregaPedidoDTO.npDetallePedido">
																			<tr><td height="5px" colspan="6"></td></tr>
																		</logic:notEmpty>
																	</logic:iterate>
																</table>
															</div>
														</td>
													</tr>
												</table>
											</tr>
										</table>
									</td>
								</tr>
								<%--<tr>
									<td>
										<table width="95%" cellspacing="0">
											<tr><td align="left">Las fechas est&aacute;n en formato a&ntilde;o-mes-dia (aaaa-mm-dd)</td></tr>
											<tr>
												<td>
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr class="tituloTablas" height="15px">
															<td width="3%" align="center" class="columna_contenido">&nbsp;</td>
															<td width="4%" align="center" class="columna_contenido">No</td>
															<td width="20%" align="center" class="columna_contenido">C&Oacute;DIGO BARRAS</td>
															<td width="65%" align="center" class="columna_contenido">DESCRIPCI&Oacute;N ART&Iacute;CULO</td>
															<td width="10%" align="center" class="columna_contenido">CANTIDAD</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<div id="detalleEntregas" style="height:370px;overflow:auto">
														<table cellpadding="1" cellspacing="0" width="100%">
															<logic:iterate name="ec.com.smx.sic.sispe.estadoDetallePedidoCol" id="estadoDetallePedidoDTO" indexId="indice1">
																<bean:define id="fila" value="${indice1 + 1}"/>
																<bean:define id="residuo" value="${indice1 % 2}"/>
																<bean:define id="clase" value="blanco10"/>
																<logic:notEqual name="residuo" value="0">
																	<bean:define id="clase" value="grisClaro10"/>
																</logic:notEqual>
																<tr class="${clase}">
																	<td width="3%" align="center" class="columna_contenido fila_contenido fila_contenido_sup">
																		<div style="display:none" id="desplegar_${indice1}">
																			<a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indice1}']);show(['plegar_${indice1}','listado_${indice1}']);"></a>
																		</div>
																		<div style="display:block" id="plegar_${indice1}">
																			<a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indice1}','listado_${indice1}']);show(['desplegar_${indice1}']);"></a>
																		</div>
																	</td>
																	<td width="4%" align="center" class="columna_contenido fila_contenido fila_contenido_sup">${fila}</td>
																	<td width="20%" align="center" class="columna_contenido fila_contenido fila_contenido_sup"><bean:write name="estadoDetallePedidoDTO" property="detallePedidoDTO.articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
																	<td width="65%" align="center" class="columna_contenido fila_contenido fila_contenido_sup"><bean:write name="estadoDetallePedidoDTO" property="detallePedidoDTO.articuloDTO.descripcionArticulo"/></td>
																	<td width="10%" align="center" class="columna_contenido columna_contenido_der fila_contenido fila_contenido_sup"><bean:write name="estadoDetallePedidoDTO" property="cantidadEstado"/></td>
																</tr>
																<tr>
																	<td colspan="5">
																		<div id="listado_${indice1}">
																			<table cellpadding="1" cellspacing="0" width="90%">
																				<tr class="tituloTablasCeleste">
																					<td class="columna_contenido" width="50%" align="center">Direcci&oacute;n Entrega</td>
																					<td class="columna_contenido" width="15%" align="center">Fecha Despacho</td>
																					<td class="columna_contenido" width="10%" align="center">Can. Despacho</td>
																					<td class="columna_contenido" width="15%" align="center">Fecha Entrega</td>
																					<td class="columna_contenido" width="10%" align="center">Can. Entrega</td>
																				</tr>
																				<logic:iterate name="estadoDetallePedidoDTO" property="entregaDetallePedidoCol" id="entregaDetallePedidoDTO" indexId="indice2">
																					<bean:define id="residuo2" value="${indice2 % 2}"/>
																					<logic:equal name="residuo2" value="0">
																						<bean:define id="clase2" value="blanco10"/>
																					</logic:equal>
																					<logic:notEqual name="residuo2" value="0">
																						<bean:define id="clase2" value="amarilloClaro10"/>
																					</logic:notEqual>
																					<logic:notEmpty name="entregaDetallePedidoDTO" property="npDescripcionArticulo">
																						<%-- error en el registro --%
																						<bean:define id="clase2" value="rojoClaro10"/>
																					</logic:notEmpty>
																					
																					<tr class="${clase2}">
																						<td width="50%" align="center" class="columna_contenido fila_contenido">
																						<bean:define id="direccionEntrega"><bean:write name="entregaDetallePedidoDTO" property="entregaPedidoDTO.direccionEntrega"/></bean:define>
																						<logic:notEmpty name="entregaDetallePedidoDTO" property="entregaPedidoDTO.codigoLocalSector">
																							<input type="text" name="txtde_${indice1}_${indice2}" value="${direccionEntrega}" size="50" class="textNormal"/>
																						</logic:notEmpty>
																						<logic:empty name="entregaDetallePedidoDTO" property="entregaPedidoDTO.codigoLocalSector">
																							<bean:write name="entregaDetallePedidoDTO" property="entregaPedidoDTO.direccionEntrega"/>
																							<input type="hidden" name="txtde_${indice1}_${indice2}" value="${direccionEntrega}">
																						</logic:empty>
																						</td>
																						<bean:define id="fechaEntrega"><bean:write name="entregaDetallePedidoDTO" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fecha"/></bean:define>
																						<bean:define id="fechaDespacho"><bean:write name="entregaDetallePedidoDTO" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/></bean:define>
																						<logic:equal name="entregaDetallePedidoDTO" property="cantidadDespacho" value="0">
																							<td width="15%" align="center" class="columna_contenido fila_contenido">NA</td>
																							<td width="10%" align="center" class="columna_contenido fila_contenido">NA</td>
																							<td width="10%" align="center" class="columna_contenido fila_contenido"><bean:write name="entregaDetallePedidoDTO" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fecha"/></td>
																						</logic:equal>
																						<logic:notEqual name="entregaDetallePedidoDTO" property="cantidadDespacho" value="0">
																							<td width="15%" align="center" class="columna_contenido fila_contenido"><input type="text" name="txtfd_${indice1}_${indice2}" value="${fechaDespacho}" size="12" maxlength="10" class="textNormal"/></td>
																							<td width="10%" align="center" class="columna_contenido fila_contenido"><bean:write name="entregaDetallePedidoDTO" property="cantidadDespacho"/></td>
																							<td width="10%" align="center" class="columna_contenido fila_contenido"><input type="text" name="txtfe_${indice1}_${indice2}" value="${fechaEntrega}" size="12" maxlength="10" class="textNormal"/></td>
																						</logic:notEqual>
																						<td width="15%" align="center" class="columna_contenido columna_contenido_der fila_contenido"><bean:write name="entregaDetallePedidoDTO" property="cantidadEntrega"/></td>
																					</tr>
																				</logic:iterate>
																				<tr><td height="3px"></td></tr>
																			</table>
																		</div>
																	</td>
																</tr>
															</logic:iterate>
														</table>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>--%>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>