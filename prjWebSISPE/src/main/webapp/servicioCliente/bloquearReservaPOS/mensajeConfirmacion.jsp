<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<table class="textoNegro11"  border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
	<tr>
		<td colspan="2">
			<div id="mensajesBloquearPOS" style="font-size:0px;position:relative;">
				<logic:empty name="ec.com.smx.sic.sispe.mensajes">
					<tiles:insert page="/include/mensajes.jsp"/>
				</logic:empty>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top" width="3%"><img src="images/info48.gif" border="0">&nbsp;&nbsp;</td> 
					<td align="left">
						<table>
							<tr>
								<td>
									Para desbloquear la reserva debe tener en n&uacute;mero de confirmaci&oacute;n generado en el POS.
								</td>
							</tr>
							
							<tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.secuencial.transaccion">
									<bean:define id="secTransaccion" name="ec.com.smx.sic.sispe.secuencial.transaccion" ></bean:define>
									<td align="left">
										N&uacute;mero de confirmaci&oacute;n esperado:  <label class="textoRojo10">${secTransaccion} </label>
									</td>
									
								</logic:notEmpty>
							</tr>
														
						</table>
					</td>
					
				</tr>
			
			</table>
			
			<table align="center" >
				<tr>
					<td align="center" class="textoAzul11">N&uacute;mero de Confirmaci&oacute;n</td>
					<td align="center">
						<input type="text" name="numeroConfirmacion" class="textNormal" size="30">
					</td>
				</tr>
				
				<tr><td></td></tr>
			</table>
		</td>
	</tr>
</table>
