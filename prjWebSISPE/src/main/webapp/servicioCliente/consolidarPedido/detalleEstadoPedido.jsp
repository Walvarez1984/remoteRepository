<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime(); %>

<tiles:insert page="/include/topSinMenu.jsp"/>

<html:form action="detalleEstadoPedido" method="post" enctype="multipart/form-data">
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<input type="hidden" name="ayuda" value="">
		<tr>
			<td align="center">
				<div id="informacionOrdenCompra">
					<logic:notEmpty name="vistaDetallePedidoDTO">
						<div id="popupInfoOrdenCompras" class="popup" style="visibility:visible;top:100px">
							<div id="center" class="popupcontenido">
								<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion" width="500px">
									<tr>
										<td background="images/barralogin.gif" height="22px">
											<table cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td class="textoBlanco11">
														<b>&nbsp;&nbsp;Datos de la Orden de Compra</b>
													</td>
													<td align="right">
														<div id="botonWin">
															<a href="#" class="linkBlanco8" onClick="hide(['popupInfoOrdenCompras']);">X</a>
														</div>
													</td>
													<td width="3px"></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td bgcolor="#F4F5EB" align="center">
											<table class="tabla_informacion textoNegro11" border="0" width="100%">
												<logic:notEmpty name="vistaDetallePedidoDTO">
													<tr>
														<td align="left" class="textoAzul11" width="28%">C&oacute;digo barras:</td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarras"/></td>
													</tr>
													<tr>
														<td align="left" class="textoAzul11">Descripci&oacute;n Art&iacute;culo:</td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
													</tr>
													<tr>
														<td align="left" class="textoAzul11">No Orden de Compra:</td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra"/></td>
													</tr>
													<tr>
														<td align="left" class="textoAzul11">Observaci&oacute;n:</td>
														<td align="left"><bean:write name="vistaDetallePedidoDTO" property="observacionAutorizacionOrdenCompra"/></td>
													</tr>
												</logic:notEmpty>
												<tr>
													<td colspan="2" align="center">
														<table cellpadding="0" cellspacing="0" width="50%">
															<td align="center">
																<div id="botonD">
																	<html:link href="#" styleClass="aceptarD" onclick="hide(['popupInfoOrdenCompras']);">Aceptar</html:link>
																</div>
															</td>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</logic:notEmpty>
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
									<logic:notEmpty name="ec.com.smx.sic.sispe.flagDiferidos">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="diferidoA" onclick="requestAjax('detalleEstadoPedido.do',['pregunta','seccion_detalle','seccionDiferidos'],{parameters: 'verDiferido=ok', evalScripts: true});">Diferidos</html:link>
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
											<html:link href="#" styleClass="pdfA" onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'confirmarPDF=ok', evalScripts: true});">Crear PDF</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link href="#" onclick="requestAjax('detalleEstadoPedido.do',['pregunta'],{parameters: 'confirmarImpresionTexto=ok', evalScripts: true});" styleClass="imprimirA" >Imprimir</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<%--<logic:notEqual name="accionActual" value="${accionEstadoPedido}">
													<html:link href="#" onclick="window.close()" styleClass="cancelarA">Cancelar</html:link>
												</logic:notEqual>
												<logic:equal name="accionActual" value="${accionEstadoPedido}">--%>
											<html:link action="detalleEstadoPedido.do?atras=ok" styleClass="atrasA">Atras</html:link>
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
			<td align="center" valign="top">
				<tiles:insert page="/servicioCliente/estadoPedido/detalleComun.jsp"/>
			</td>
			<td>
				<logic:notEmpty name="ec.com.smx.sic.sispe.funcionImprimir">
					<script language="JavaScript">window.print();</script>
				</logic:notEmpty>
			</td>
		</tr>
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>