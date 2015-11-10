<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:insert page="/include/top.jsp"/>

<%-- lista de definiciones para las acciones --%>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<bean:define id="anulado"><bean:message key="ec.com.smx.sic.sispe.estado.anulado"/></bean:define>
<bean:define id="cotCaducada"><bean:message key="ec.com.smx.sic.sispe.estado.cotizacionCaducada"/></bean:define>
<bean:define id="devolucion"><bean:message key="ec.com.smx.sic.sispe.estado.devolucion"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
	<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
	<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
</logic:notEmpty>

<c:set var="imagen" value="cotizacion.gif"/>
<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
</logic:equal>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
<div  id="resumen_pedido" >
<html:form action="crearCotizacion" method="post" enctype="multipart/form-data">
	<table border="0" cellspacing="0" cellpadding="0" width="100%">
	
		<input type="hidden" name="ayuda" value="">
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="/confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="crearCotizacion"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">
						<tiles:insert page="/confirmacion/popUpConfirmacionAux.jsp">
							<tiles:put name="vtformAction" value="estadoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones">
						<script language="javascript">ocultarModal();</script>
					</logic:notEmpty>
					<!-- PopUp de Redactar mail-->
					<logic:notEmpty name="ec.com.smx.sic.sispe.redactarMail">
						<tiles:insert page="/servicioCliente/redactarMail.jsp">
							<tiles:put name="vtformAction" value="envioMail"/>
							<tiles:put name="vtformName" value="envioMailForm"/>
						</tiles:insert>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<tr>
			<td class="titulosAccion" height="35px">
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td  width="3%" align="center"><img src="images/${imagen}" border="0"></img></td>
						<td align="left">&nbsp;&nbsp;<bean:write name="WebSISPE.tituloVentana"/></td>
						<td align="right">
							<table cellspacing="0">
								<tr>
									<%--
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="pdfA" onclick="requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'confirmarPDF=ok'});">Crear PDF</html:link>
										</div>
									</td>
									--%>
									<bean:define id="vistaPedido" name="ec.com.smx.sic.sispe.vistaPedido"/>
            						<bean:define id="rolesMail" name="ec.com.smx.sic.sispe.rolesMail"/>
									<bean:define id="rolActual" name="sispe.vistaEntidadResponsableDTO"/>
									<c:if test="${vistaPedido.id.codigoEstado!=anulado && vistaPedido.id.codigoEstado!=cotCaducada && vistaPedido.id.codigoEstado!=devolucion && fn:contains(rolesMail,rolActual.id.idRol)}">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="enviar_mailA" title="Envía un mail con el pedido al cliente" onclick="requestAjax('crearCotizacion.do',['div_pagina','pregunta'],{parameters: 'redactarMail=ok', evalScripts: true});">Env&iacute;o mail</html:link>
											</div>
										</td>
									</c:if>
									<logic:notEmpty name="ec.com.smx.sic.sispe.flagDiferidos">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="diferidoA" onclick="requestAjax('crearCotizacion.do',['pregunta','seccion_detalle','seccionDiferidos'],{parameters: 'verDiferido=ok', evalScripts: true});">Diferidos</html:link>
											</div>
										</td>
									</logic:notEmpty>
									<!--  
									<logic:notEmpty name="ec.com.smx.sic.sispe.responsables.entregas">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="responsableA" onclick="enviarFormulario('siGenerarPDFRes', 0, false);">Crear PDF</html:link>
											</div>
										</td>
									</logic:notEmpty>
									-->																	
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
										</div>
									</td>
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="imprimirA" onclick="requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'confirmarImpresionTexto=ok',  evalScripts: true});">Imprimir</html:link>
										</div>
									</td>
									<td>
										<bean:define id="exit" value="ok"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" title="volver al men&uacute; principal">Inicio</html:link>
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
				<table border="0" class="textoNegro11" width="98%" align="center" cellpadding="0" cellspacing="0">
					<%-- secci&oacute;n del resumen del pedido --%>
					<tr><td height="5px"></td></tr>
					<tr>
						<td colspan="2" width="100%">
							<%-- para que el reporte se despliegue en un nueva ventana --%>
							<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11 tabla_informacion">
								<tr><td height="5px"></td></tr>
								<tr>
									<!--<logic:notEmpty name="ec.com.smx.sic.sispe.reservacion.responsable">
										<td align="left" width="50%">
											<label class="textoRojo11"><b>Entidad responsable:</b></label>&nbsp;
											<bean:write name="ec.com.smx.sic.sispe.reservacion.responsable"/>
										</td>
									</logic:notEmpty>-->
									<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
										<td align="left">
											<b><label class="textoRojo11">N&uacute;mero de pedido:&nbsp;</label></b><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/>&nbsp;&nbsp;&nbsp;
										</td>
										<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
											<logic:empty name="ec.com.smx.sic.sispe.pedido.reservacionTemporal">
												<td align="left">
													<b><label class="textoRojo11">N&uacute;mero reservaci&oacute;n:&nbsp;</label></b><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOS"/>&nbsp;&nbsp;&nbsp;
												</td>
											</logic:empty>
										</logic:equal>
									</logic:notEmpty>
								</tr>
								<tr><td height="5px"></td></tr>
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
					<tr>
						<td colspan="2" align="right">
							<table border="0" width="100%" class="textoNegro11 tabla_informacion" cellpadding="0" cellspacing="0">
								<tr><td height="5px"></td></tr>
								<tr>
									<td align="left" width="50%">
										<b>Elaborado en:&nbsp;</b><html:hidden name="cotizarRecotizarReservarForm" property="localResponsable" write="true"/>
									</td>
									<%--logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSAnterior">
										<td align="right">
											<label class="textoRojo10">Reservaci&oacute;n anterior:&nbsp;</label><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSAnterior"/>&nbsp;&nbsp;
										</td>
										<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion">
											<td align="right">
												<label class="textoRojo10">Devoluci&oacute;n&nbsp;</label>(N&uacute;mero:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion"/>, Valor:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npValorDevolucionPOS" formatKey="formatos.numeros"/>)&nbsp;&nbsp;
											</td>
										</logic:notEmpty>
									</logic:notEmpty--%>
									<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion">
										<td align="right">
											<label class="textoRojo10">Devoluci&oacute;n&nbsp;</label>(N&uacute;mero:&nbsp;<bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="npLlaveContratoPOSDevolucion"/>, Valor:&nbsp;
											<bean:define id="npValorDevolucionPOS" name="ec.com.smx.sic.sispe.pedidoDTO" property="npValorDevolucionPOS"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${npValorDevolucionPOS}"/>)&nbsp;&nbsp;
										</td>
									</logic:notEmpty>									
								</tr>
								<tr><td height="5px"></td></tr>
							</table>
						</td>
					</tr>
					
					<tr><td height="2px"></td></tr>
					<tr>
						<td colspan="2">
							<!-- 
								@author Wladimir López	
								 Todo el table de detalles, fué trasladado a 
									/servicioCliente/cotizarRecotizarReservar/detalleRegistroPedido.jsp
							 -->
							<tiles:insert page="/controlesUsuario/controlTab.jsp"/>							
						</td>
					</tr>
				</table>
			</td>
		</tr>                 
		<tr>
			<td align="center">
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="ok">
					<!--  <script language="JavaScript">window.print();</script> -->
					<script language="JavaScript">imprimeSeleccion('reportePedidoTxt');</script>
				</logic:equal>
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="okLaser">	
					<script language="JavaScript">imprimeSeleccion('imprConvenioLaser');</script>
				</logic:equal>
				<logic:equal name="ec.com.smx.sic.sispe.funcionImprimir" value="okMatriz">	
					<script language="JavaScript">imprimeSeleccion('imprConvenioMatriz');</script>
				</logic:equal>
			</td>
		</tr>
	</table>
</html:form>
</div>
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