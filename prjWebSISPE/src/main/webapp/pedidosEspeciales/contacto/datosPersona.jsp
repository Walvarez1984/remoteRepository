<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<bean:define id="cedula"><bean:message key="ec.com.smx.sic.sispe.documento.cedula"/></bean:define>
<bean:define id="ruc"><bean:message key="ec.com.smx.sic.sispe.documento.ruc"/></bean:define>
<bean:define id="pasaporte"><bean:message key="ec.com.smx.sic.sispe.documento.pasaporte"/></bean:define>

<div id="datosCliente">
	<table border="0" cellpadding="0" cellpadding="0" align="left" width="100%">
		<tr>
			<td width="100%">
				<table border="0" cellspacing="0" cellpadding="0" align="left" class="textoAzul11">
					<tr>
						<td align="left">Tipo documento: *&nbsp;</td>
						<td align="left" class="textoNegro11">
						
							<html:select property="comboTipoDocumento" styleClass="combos">
								<html:option value="${cedula}">C&eacute;dula</html:option>
								<html:option value="${pasaporte}">Pasaporte</html:option>
								<html:option value="${ruc}">RUC</html:option>
							</html:select>														
							
						</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left">N&uacute;mero documento: *&nbsp;</td>
						<td>
							<smx:text property="cedulaContacto" styleClass="textObligatorio" styleError="campoError" size="17" 
								maxlength="18" onchange="documentoCambiado();" 
								onkeyup="requestAjaxEnter('crearPedidoEspecial.do',['divTabs','contextoPedido','datosCliente', 'div_datosPersona', 'mensajes'],{parameters: 'consultarCliente=ok', evalScripts: true});limpiarTextoCambio();"/>
						</td>
						<td>
							<a href="#" onclick="requestAjax('crearPedidoEspecial.do', ['divTabs','contextoPedido','datosCliente','div_datosPersona','mensajes'] , {parameters: 'consultarCliente=ok', evalScripts: true});limpiarTextoCambio();"><img alt="Buscar" src="./images/buscar.gif" border="0"></a>
						</td>
					</tr>
					<tr>
						<logic:notEmpty property="nombreCompleto">
							<td align="left" colspan="6">Nombre persona:&nbsp; <span style="color:black;"><bean:write property="nombreCompleto" /></span></td>
						</logic:notEmpty>
						<logic:notEmpty property="nombreEmpresa">
								<td align="left" colspan="6">Raz&oacute;n social:&nbsp; <span style="color:black;"><bean:write property="nombreEmpresa" /></span></td>
						</logic:notEmpty>
					</tr>
				</table>					
			</td>			
		</tr>
	</table>	
</div>

