<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<div id="popupImpresionConvenios">

	<table align="center" width="100%" cellpadding="0" cellspacing="0"
		class="fondoblanco">
		<tr>
			<td>
				<table align="center" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top" width="3%"><img src="images/info.gif"
							border="0"></td>
						<td>&nbsp;</td>
						<td align="left">Elija el tipo de impresora en la cual se
							imprimirán los convenios con SICMER.</td>
					</tr>

				</table>
			</td>
		</tr>
	</table>
	<table align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<div id="botonA">
					<html:link href="#" styleClass="laserA"
						onclick="enviarFormulario('imprimirConvenioL');">Laser</html:link>
				</div>
			</td>
			<td width="40px">&nbsp;</td>
			<td>
				<div id="botonA">
					<html:link href="#" styleClass="matricialA"
						onclick="enviarFormulario('imprimirConvenioM');">Matricial</html:link>
				</div>
			</td>
		</tr>
	</table>
</div>