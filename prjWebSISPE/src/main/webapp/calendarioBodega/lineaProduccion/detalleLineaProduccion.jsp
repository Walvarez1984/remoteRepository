<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime(); %>
<logic:notEmpty name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO">
  <bean:define id="calendarioArticuloDTO" name="ec.com.smx.sic.sispe.lineaProduccion.calendarioArticuloDTO"/>
</logic:notEmpty>
<tiles:insert page="/include/topSinMenu.jsp"/>

<html:form action="lineaProduccion" method="post">	
	<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
		<input type="hidden" name="ayuda" value="">
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="lineaProduccion"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<tr>
			<td align="left" valign="top" width="100%" align="center" id="botones">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="images/detalleLineaProduccion.gif" border="0"></img></td>
						<td height="35" valign="middle">Detalle de la Producci&oacute;n seleccionada</td>
						<td align="right">
							<table border="0">
								<tr>
								 <%-- <logic:notEmpty name="calendarioArticuloDTO">
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
										</div>
									</td>									
									<td>
										<div id="botonA">
											<html:link href="#" onclick="requestAjax('lineaProduccion.do',['pregunta'],{parameters: 'confirmarImpresionTexto=ok', evalScripts: true});" styleClass="imprimirA" >Imprimir</html:link>
										</div>
									</td>
							     </logic:notEmpty>--%>	
									<td>
										<div id="botonA">
											<html:link action="lineaProduccion.do?atras=ok" styleClass="atrasA">Atr&aacute;s</html:link>
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
			<td align="center" valign="top" id="detalleComun">
				<tiles:insert page="/calendarioBodega/lineaProduccion/detalleComun.jsp"/>
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