<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<table class="fondoBlanco textoNegro11" border="0" width="100%" cellpadding="0" cellspacing="5">
	<tr>
		<td align="left">
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="2" id="mensajesModReserva" style="position:relative;"><tiles:insert page="/include/mensajes.jsp" /></td>
				</tr>
				<tr>
					<td valign="top" width="3%"><img src="images/pregunta24.gif"
						border="0">&nbsp;&nbsp;</td>
					<td align="left">¿Qu&eacute; acci&oacute;n desea ejecutar?</td>
				</tr>
				<tr>
					<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<html:radio
							property="radioReserva" value="factura" /> <b>Factura total del pedido.</b>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Se creará una nueva factura con todos los cambios realizados).
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<html:radio
							property="radioReserva" value="nuevaReserva" /> <b>Factura del pedido solo con los cambios.</b>
					</td>
				</tr>
				<tr>
					<td align="left" colspan="2">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(Se creará una nueva factura únicamente con los artículos o cantidades aumentadas).
					</td>
				</tr>
				<%-- tr>
					<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<html:radio
							property="radioReserva" value="nuevaReservaConDesc" /> <b>Generar una nueva reserva y conservar los descuentos.</b>
					</td>
				</tr--%>
			</table>
		</td>
	</tr>
</table>