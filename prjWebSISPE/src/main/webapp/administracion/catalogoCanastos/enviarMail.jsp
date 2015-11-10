<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<html>
	<head></head>
	<body>
		<!-- contenido -->
		<table cellpadding="1" cellspacing="0" width="100%">
		<%--Cuerpo--%>
		<tr>
			<td>
				<div style="width:100%;height:500px;border:1px solid #cccccc;overflow:auto">
					<table border="0" cellpadding="1" cellspacing="0" width="100%" align="center">
						<tr>
							<c:set var="contador" value="0"/>
							<c:set var="fila_inicio" value=""/>
							<c:set var="fila_fin" value=""/>
							<logic:notEmpty name="ec.com.smx.sic.sispe.canastos.seleccionados">
								<logic:iterate id="articuloDTO" name="ec.com.smx.sic.sispe.canastos.seleccionados" indexId="indiceArticulo">
									<logic:notEqual name="contador" value="2">
										<c:set var="fila_inicio" value=""/>
										<c:set var="fila_fin" value=""/>
									</logic:notEqual>
									<logic:equal name="contador" value="2">
										<c:set var="contador" value="0"/>
										<c:set var="fila_inicio" value="<tr>"/>
										<c:set var="fila_fin" value="</tr>"/>
									</logic:equal>
									${fila_fin}
									${fila_inicio}
									<td height="200px">
										<%-- inicio tabla para cada canasta --%>
										<table border="1" width="100%" height="350px" align="center" style="border-width: 1px;border-style: solid;border-color: #cccccc;">
										<logic:notEmpty name="ec.com.smx.sic.sispe.pathAplicacion">
											<bean:define id="pathAplicacion" name="ec.com.smx.sic.sispe.pathAplicacion"></bean:define>
										</logic:notEmpty>
											<tr>
												<td valign="top" style="border-right-width: 1px;border-right-style: solid;border-right-color: #CCCCCC;">
													<table width="100%" cellpadding="0" cellspacing="0">
														<tr>
															<%--
															<td valign="top">
																<img id="idImagen" src=cid:rrtrtrtrtrt"${pathAplicacion}/administracion/catalogoCanastos/imagenes/${articuloDTO.id.codigoArticulo}.jpeg" width="130" height="110" border="0"/>
															</td>
															--%>
															<td>
																<%-- tabla de detalles --%>
																<table border="0" cellspacing="1" cellpadding="0" height="165" width="100%">
																	<tr>
																		<td height="20" valign="top" align="right" style="font-family: Verdana, Arial, Helvetica;font-size:13;color:#000000">
																			<b><bean:write name="articuloDTO" property="descripcionArticulo"/></b>
																		</td>
																	</tr>
																	<tr>
																		<td valign="top" align="right"  height="10" style="font-family: Verdana, Arial, Helvetica;font-size:12;color:#990000">
																			<b>$&nbsp;<bean:write name="articuloDTO" property="articulo.precioBaseImp"/></b>
																		</td>
																	</tr>
																	<tr>
																		<td align="right" style="font-family: Verdana, Arial, Helvetica;font-size: 9px;color: #990000;" height="20px">Incluido IVA.</td>
																	</tr>
																	<logic:notEmpty name="articuloDTO" property="recetaArticulos">
																		<tr>
																			<td>
																				<div style="height:250px;overflow:auto">
																					<table cellpadding="1" cellspacing="0" width="100%">
																						<ul>
																						<logic:iterate id="recetaArticuloDTO" name="articuloDTO" property="recetaArticulos">
																							<tr>
																								<td style="font-family: Verdana, Arial, Helvetica;font-size:9;color:#0000aa">
																									<li>
																										<bean:write name="recetaArticuloDTO" property="articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/>
																									</li>
																								</td>
																							</tr>
																						</logic:iterate>
																						</ul>
																					</table>
																				</div>
																			</td>
																		</tr>
																	</logic:notEmpty>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
										<%-- fin tabla para cada canasta --%>
									</td>
									<c:set var="contador" value="${contador + 1}"/>
								</logic:iterate>
							</logic:notEmpty>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</body>
</html>
