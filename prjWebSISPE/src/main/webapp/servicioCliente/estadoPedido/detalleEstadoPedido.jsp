<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime(); %>

<tiles:insert page="/include/topSinMenu.jsp"/>
<bean:define id="anulado"><bean:message key="ec.com.smx.sic.sispe.estado.anulado"/></bean:define>
<bean:define id="cotCaducada"><bean:message key="ec.com.smx.sic.sispe.estado.cotizacionCaducada"/></bean:define>
<bean:define id="devolucion"><bean:message key="ec.com.smx.sic.sispe.estado.devolucion"/></bean:define>

<html:form action="detalleEstadoPedido" method="post" enctype="multipart/form-data">
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<input type="hidden" name="ayuda" value="">
		<tr>
			<td align="center">
				<div id="informacionOrdenCompra">
					<logic:empty name="ec.com.smx.sic.sispe.pedidoResumenConsolidado">
					<logic:notEmpty name="vistaDetallePedidoDTO">
						<div id="popupInfoOrdenCompras" class="popup" style="visibility:visible;top:100px;">
							<div id="center" class="popupcontenido">
								<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion" width="500px">
									<tr>
										<td background="images/barralogin.gif" height="22px">
											<table cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td class="textoBlanco11">
														<b>&nbsp;&nbsp;Datos de la orden de compra</b>
													</td>
													<td align="right">
														<div id="botonWin">
															<a href="#" class="linkBlanco8" onClick="hide(['popupInfoOrdenCompras']);ocultarModal();"> <img src="./images/close.gif" border="0" style="padding-top:3px;"/></a>
														</div>
													</td>
													<td width="3px"></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#F4F5EB" align="top">
											<table class="tabla_informacion textoNegro11" border="0" width="100%">
												<tr>
													<td>
														<div id="cuerpo_popUp">
															<table border="0" cellspacing="0" cellpadding="0" width="100%">
																 <tr>
																	<td bgcolor="#ffffff" align="left" >
																		<div style="overflow:auto;border:1px solid #cccccc;padding:4px;">
																			<table width="100%"> 
																				<logic:notEmpty name="vistaDetallePedidoDTO">
																					<tr>
																						<td align="left" class="textoAzul11" width="28%">C&oacute;digo barras:</td>
																						<td align="left"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
																					</tr>
																					<tr>
																						<td align="left" class="textoAzul11">Descripci&oacute;n art&iacute;culo:</td>
																						<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
																					</tr>
																					<tr>
																						<td align="left" class="textoAzul11">No orden de compra:</td>
																						<td align="left"><bean:write name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra"/></td>
																					</tr>
																					<tr>
																						<td align="left" class="textoAzul11">Observaci&oacute;n:</td>
																						<td align="left"><bean:write name="vistaDetallePedidoDTO" property="observacionAutorizacionOrdenCompra"/></td>
																					</tr>
																				</logic:notEmpty>
																				
																			</table>
																		</div>
																	</td>
																</tr>
																
																<tr>
																	<td height="2px"></td>
																</tr>
																
																<tr>
																	<td colspan="2" align="center">
																		<table cellpadding="0" cellspacing="0" width="50%">
																			<td align="center">
																				<div id="botonD">
																					<html:link href="#" styleClass="aceptarD" onclick="hide(['popupInfoOrdenCompras']);ocultarModal();">Aceptar</html:link>
																				</div>
																			</td>
																		</table>
																	</td>
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
						</div>
					</logic:notEmpty>
					</logic:empty>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<tiles:insert page="/confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="detalleEstadoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">
						<tiles:insert page="/confirmacion/popUpConfirmacionAux.jsp">
							<tiles:put name="vtformAction" value="estadoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					
					<!-- PopUp con autorizaciones que tienen mensajes -->
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones">
						<tiles:insert page="/servicioCliente/autorizacion/popUpColaAutorizaciones.jsp">
							<tiles:put name="vtformAction" value="detalleEstadoPedido"/>                            
						</tiles:insert>
					</logic:notEmpty>
					<!-- PopUp de Redactar mail-->
					<logic:notEmpty name="ec.com.smx.sic.sispe.redactarMail">
						<tiles:insert page="/servicioCliente/redactarMail.jsp">
							<tiles:put name="vtformAction" value="detalleEstadoPedido"/>
							<tiles:put name="vtformName" value="listadoPedidosForm"/>
						</tiles:insert>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<tr>
			<td align="left" valign="top" width="100%" align="center">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="images/detalle_pedidos.gif" border="0"></img></td>
						<td height="35" valign="middle">Detalle del pedido seleccionado</td>
						<td align="right">
							<table border="0">
								<tr>
									<logic:empty name="ec.com.smx.sic.sispe.pedidoResumenConsolidado">
									<bean:define id="vistaPedido" name="ec.com.smx.sic.sispe.vistaPedido"/>
									<bean:define id="rolActual" name="sispe.vistaEntidadResponsableDTO"/>
										<logic:notEmpty name="ec.com.smx.sic.sispe.convenioEntregaCol">
											<td >
												<div id="botonA">
													<html:link href="#" styleClass="imprimirDiaHoyA" title="Imprime los convenios creados con SICMER" onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'imprimirConvenios=ok', evalScripts: true});">Imp. conv.</html:link>
												</div>
											</td>
										</logic:notEmpty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.rolesMail">
		           							<bean:define id="rolesMail" name="ec.com.smx.sic.sispe.rolesMail"/>
		           							<c:if test="${vistaPedido.id.codigoEstado!=anulado && vistaPedido.id.codigoEstado!=cotCaducada && vistaPedido.id.codigoEstado!=devolucion && fn:contains(rolesMail,rolActual.id.idRol)}">
												<td>
													<div id="botonA">
														<html:link href="#" styleClass="enviar_mailA" title="Envía un mail con el pedido al cliente" onclick="requestAjax('detalleEstadoPedido.do',['div_pagina','pregunta'],{parameters: 'redactarMail=ok', evalScripts: true});">Env&iacute;o mail</html:link>
													</div>
												</td>
											</c:if>
										</logic:notEmpty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.flagDiferidos">
											<td>
												<div id="botonA">
													<html:link href="#" styleClass="diferidoA" onclick="requestAjax('detalleEstadoPedido.do',['pregunta','seccion_detalle','seccionDiferidos'],{parameters: 'verDiferido=ok', evalScripts: true});">Diferidos</html:link>
												</div>
											</td>
										</logic:notEmpty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.mensajes.autorizaciones">
											<td>
												<div id="botonA">
													<html:link href="#" styleClass="mensajesAutorizaciones" onclick="requestAjax('detalleEstadoPedido.do',['div_pagina','pregunta'],{parameters: 'mensajesAutorizaciones=ok', evalScripts: true});">Mensajes</html:link>
												</div>
											</td>
										</logic:notEmpty>
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
											</div>
										</td>
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="pdfA" onclick="requestAjax('detalleEstadoPedido.do',['pregunta','mensajes'],{parameters: 'confirmarPDF=ok', evalScripts: true});">Crear PDF</html:link>
											</div>
										</td>
										<td>
											<div id="botonA">
												<html:link href="#" onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'confirmarImpresionTexto=ok', evalScripts: true});" styleClass="imprimirA" >Imprimir</html:link>
											</div>
										</td>
									</logic:empty>
									<td>
										<div id="botonA">
											<%--<logic:notEqual name="accionActual" value="${accionEstadoPedido}">
													<html:link href="#" onclick="window.close()" styleClass="cancelarA">Cancelar</html:link>
												</logic:notEqual>
												<logic:equal name="accionActual" value="${accionEstadoPedido}">--%>
												<logic:empty name="ec.com.smx.sic.sispe.monitor.perecibles.atras">
													<html:link action="detalleEstadoPedido.do?atras=ok" styleClass="atrasA">Atras</html:link>	
												</logic:empty>
												
												<logic:notEmpty name="ec.com.smx.sic.sispe.monitor.perecibles.atras">
													<html:link action="enviarOCPerecibles.do?atrasMonitorPerecibles=ok" styleClass="atrasA">Atras</html:link>
												</logic:notEmpty>
											
											<%--</logic:equal>--%>
										</div>
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
			<logic:empty name="ec.com.smx.sic.sispe.pedidoResumenConsolidado">
				<td align="center" valign="top">
					<tiles:insert page="/servicioCliente/estadoPedido/detalleComun.jsp"/>
				</td>
			</logic:empty>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoResumenConsolidado">
				<td align="center" valign="top">
					<tiles:insert page="/servicioCliente/estadoPedido/detalleConsolidadoComun.jsp"/>
				</td>
			</logic:notEmpty>
			<td>
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="ok">
					<!--<script language="JavaScript">window.print();</script>-->
					<script language="JavaScript">imprimeSeleccion('rptEstadoPedidoTxt');</script>
				</logic:equal>
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="okLaser">	
					<script language="JavaScript">imprimeSeleccion('imprConvenioLaser');</script>
				</logic:equal>
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="okMatriz">	
					<script language="JavaScript">imprimeSeleccion('imprConvenioMatriz');</script>
				</logic:equal>
			</td>
		</tr>
	</TABLE>
</html:form>
<logic:notEmpty name="ec.com.smx.sic.sispe.envioMail">
	<bean:define id="valoresMail" name="ec.com.smx.sic.sispe.envioMail"/>
	<logic:empty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:empty>
	<logic:notEmpty name="valoresMail" property="cc">
		<mensajeria:enviarMail asunto="${valoresMail.asunto}" de="${valoresMail.de}" para="${valoresMail.para[0]}" host="${valoresMail.host}" puerto="${valoresMail.puerto}" 
		codigoCompania="${valoresMail.eventoDTO.id.companyId}" codigoSistema="${valoresMail.eventoDTO.id.systemId}" cc="${valoresMail.cc[0]}"
		codigoEvento="${valoresMail.eventoDTO.id.codigoEvento}" reemplazarRemitente="${valoresMail.reemplazarRemitente}">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro10">
				<tr>
					<td>
						<bean:write name="ec.com.smx.sic.sispe.textoMail" filter="false"/>
					</td>
				</tr>
			</table>
			<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/envioCotizacionMail.jsp" flush="false"/>
		</mensajeria:enviarMail>
	</logic:notEmpty>
</logic:notEmpty>
<tiles:insert page="/include/bottom.jsp"/>