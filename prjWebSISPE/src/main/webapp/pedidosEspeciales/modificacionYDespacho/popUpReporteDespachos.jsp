<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<div id="popUpReporteDespachos">

	<table align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="15%" colspan="2">
				<b>&nbsp;Fecha despacho:</b>
			</td>
		</tr>
		<tr><td height="5px"></td></tr>
		<tr>
			<td>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td width="33%">&nbsp;</td>
						<td align="center" width="10%">Inicial:</td>
						<td>
							<table align="left" border="0" cellspacing="0">
								<tr>
									<td>	
										<html:text property="fechaInicialRepDespachos" styleClass="textNormal" size="12" maxlength="10"/>
									</td>
									<td>
										<smx:calendario property="fechaInicialRepDespachos" key="formatos.fecha"/>
									</td>
									<td width="2px"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="33%">&nbsp;</td>
						<td align="center">Final:&nbsp;</td>
						<td>
							<table align="left" border="0" cellspacing="0">
								<tr>
									<td >
										<html:text property="fechaFinalRepDespachos" styleClass="textNormal" size="12" maxlength="10"/>
									</td>
									<td>
										<smx:calendario property="fechaFinalRepDespachos" key="formatos.fecha"/>
									</td>
									<td width="2px"></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>

	</table>
</div>