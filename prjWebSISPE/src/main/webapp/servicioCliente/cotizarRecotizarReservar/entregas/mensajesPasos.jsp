<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<bean:define id="paso1"><bean:message key="ayuda.paso1"/></bean:define>
<bean:define id="paso2"><bean:message key="ayuda.paso2"/></bean:define>
<bean:define id="paso3"><bean:message key="ayuda.paso3"/></bean:define>
<bean:define id="paso4"><bean:message key="ayuda.paso4"/></bean:define>

<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
	<tr>
		<td style="background:url(images/warning_esq_sup_izq.gif)" height="5" width="5"></td>
		<td style="background:url(images/warning_sup.gif)"></td>
		<td style="background:url(images/warning_esq_sup_der.gif)" height="5" width="5"></td>
	</tr>
	<%--<tr>
		<td style="background:url(images/warning_izq.gif)" height="5" width="5"></td>
		<td bgcolor="#FFF8CE">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
				<tr>
					<td width="26" align="center"><img src="./images/advertencia.gif"/></td>
					<td class="textoNegro13" align="left">&nbsp;Advertencia:<br></td>
				</tr>
			</table>
		</td>
		<td style="background:url(images/warning_der.gif)" height="5" width="5"></td>
	</tr>--%>
	<!-- Mensajes -->
	<tr>
		<td style="background:url(images/warning_izq.gif)" height="5" width="5"></td>
		<td bgcolor="#FFF8CE">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" class="textoNegro11">
				<tr>
					<td width="30px">
						<logic:equal name="ec.com.smx.sic.paso" value="${paso1}">
							<img src="images/1.gif"/>
						</logic:equal>
						<logic:equal name="ec.com.smx.sic.paso" value="${paso2}">
							<img src="images/2.gif"/>
						</logic:equal>
						<logic:equal name="ec.com.smx.sic.paso" value="${paso3}">
							<img src="images/3.gif"/>
						</logic:equal>
						<logic:equal name="ec.com.smx.sic.paso" value="${paso4}">
							<img src="images/4.gif"/>
						</logic:equal>
					</td>
					<td align="left">
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td align="left">
									<logic:notEmpty name="ec.com.smx.sic.mensajes">
									  	<bean:write name="ec.com.smx.sic.mensajes"/>
								  </logic:notEmpty><br>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
			</table>
		</td>
		<td style="background:url(images/warning_der.gif)" height="5" width="5"></td>
	</tr>
	<tr>
		<td style="background:url(images/warning_esq_inf_izq.gif)" height="5" width="5"></td>
		<td style="background:url(images/warning_inf.gif)"></td>
		<td style="background:url(images/warning_esq_inf_der.gif)" height="5" width="5"></td>
	</tr>
</table>