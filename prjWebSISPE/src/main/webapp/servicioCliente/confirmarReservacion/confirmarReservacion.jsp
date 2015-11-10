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
<html:form action="confirmacionReservacion" method="post">
<TABLE border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
		<html:hidden property="ayuda" value=""/>
		<html:hidden property="checkCalculosPreciosAfiliado"/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../../confirmacion/confirmacion.jsp"/>
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
						<td width="3%" align="center"><img src="./images/confirmarReservar.gif" border="0"></img></td>
						<td align="left">&nbsp;&nbsp;<bean:write name="WebSISPE.tituloVentana"/></td>
						<td align="right">
							<table cellspacing="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('regConfirmacionReservacion');" title="guardar confirmaci&oacute;n">Guardar</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link styleClass="cancelarA" href="#" onclick="requestAjax('confirmacionReservacion.do',['pregunta'],{parameters: 'volverBuscar=ok'});mostrarModal();" title="volver a la p&aacute;gina de b&uacute;squeda">Cancelar</html:link>
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
												<td class="fila_titulo" height="20px">
													<table cellpadding="0" cellspacing="0" align="left" width="100%">
														<tr>
														<td align="left">&nbsp;Encabezado</td>
														<td align="center" class="textoRojo11">No pedido:&nbsp;<label class="textoAzul11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/></label>, No reserva:&nbsp;<label class="textoAzul11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="llaveContratoPOS"/></label> </td>
														<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
															<td align="right" class="textoRojo11" id="local" width="20%"><b>Local:</b>&nbsp;
																<label class="textoAzul11"><b><html:hidden name="cotizarRecotizarReservarForm" property="localResponsable" write="true"/></b>&nbsp;</label>
															</td>
														</logic:notEmpty>
													   </tr>
													</table>
												</td>
											</tr>
											<tr>
												<td>
													<table cellpadding="0" cellspacing="0" class="tabla_informacion" width="100%">
														<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
														<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
															<tiles:put name="tformName" value="cotizarRecotizarReservarForm"/>
															<tiles:put name="textoNegro" value="textoNegro11"/>
															<tiles:put name="textoAzul" value="textoAzul11"/>
														</tiles:insert>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<tr>
									<!--  TOODO EL CONTENIDO DEL DETALLE DEL PEDIDO DE LOS PESOS FINALES SE PASO A LA PAGINA 
										detallePedidoPesosFinales.jsp -->
									<td valign="top" height="50%">
										<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
									</td>	
								</tr>								
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
</TABLE>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>