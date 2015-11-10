<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tr>
        <td class="textoNegro11" align="center">
            <table border="0" width="90%">
                <logic:notEmpty name="ec.com.smx.sic.sispe.autorizacion.metodo2">
                    <tr>
                        <td>
                            <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
                                <tr>
                                    <td colspan="2" class="fila_titulo textoNegro10" width="85%" align="left">
                                        <input type="radio" id="opRadioAutorizacion" name="opAutorizacion" value="${estadoInactivo}" onclick="document.getElementById('numeroAutorizacion').focus();">M&eacute;todo 1  
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" class="textoAzul11">N&uacute;mero de autorizaci&oacute;n</td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <!--<input type="text" name="numeroAutorizacion" class="textNormal" size="20" onfocus="opAutorizacion[0].checked=true;">-->
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
	                                        <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress">
		                                    	<bean:define id="eventoKeyPress" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress"></bean:define>
		                                    	<input type="text" name="numeroAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,0);" onkeypress="${eventoKeyPress};return validarInputNumeric(event);" 
		                                    	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true">
		                                    </logic:notEmpty>		                                    
		                                    <logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress">
		                                    	<input type="text" name="numeroAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,0);" onkeypress="return validarInputNumeric(event);" 
		                                    	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true">
		                                    </logic:empty> 
										</logic:notEmpty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">					
											<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress">
		                                    	<bean:define id="eventoKeyPress" name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress"></bean:define>
		                                    	<input type="text" name="numeroAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,0);" onkeypress="${eventoKeyPress}">
		                                    </logic:notEmpty>	                                    
		                                    <logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress">
		                                    	<input type="text" name="numeroAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,0);" onkeypress="return validarInputNumeric(event);" 
		                                    	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true">
		                                    </logic:empty>
	                                    </logic:notEmpty>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </logic:notEmpty>
                <logic:empty name="ec.com.smx.sic.sispe.autorizacion.metodo2">
                    <input type="hidden" name="opAutorizacion" value="${estadoInactivo}">
                    <tr>
                        <td align="left" class="textoAzul11">N&uacute;mero de autorizaci&oacute;n</td>
                    </tr>
                    <tr>
                        <td align="left">
                            <input type="text" name="numeroAutorizacion" class="textNormal" size="20">
                        </td>
                    </tr>
                </logic:empty> 
                <logic:notEmpty name="ec.com.smx.sic.sispe.autorizacion.metodo2">
                    <tr><td height="5px"></td></tr>
                    <tr>
                        <td>
                            <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
                                <tr>
                                    <td colspan="2" align="left" class="fila_titulo textoNegro10"><input type="radio" id="opRadioAutorizacion" name="opAutorizacion" value="${estadoActivo}" onclick="document.getElementById('loginAutorizacion').focus();"/>M&eacute;todo 2</td>
                                </tr>
                                <tr>
                                    <td align="left" class="textoAzul10">Usuario:</td>
                                </tr>
                                <tr>
                                    <td align="left">
                                        <input type="text" name="loginAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,1);"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" class="textoAzul10">Contrase&ntilde;a:</td>	
                                </tr>
                                <tr>
                                    <td align="left">
                                        <!--<input type="password" name="passwordAutorizacion" class="textNormal" size="20" onfocus="opAutorizacion[1].checked=true"/>-->
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
	                                        <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress">
		                                    	<bean:define id="eventoKeyPress" name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress"></bean:define>
		                                    	<input type="password" name="passwordAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,1);" onkeypress="${eventoKeyPress}">
		                                    </logic:notEmpty>		                                    
		                                    <logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacion" property="valorKeyPress">
		                                    	<input type="password" name="passwordAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,1);">
		                                    </logic:empty> 
										</logic:notEmpty>
										<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">					
											<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress">
		                                    	<bean:define id="eventoKeyPress" name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress"></bean:define>
		                                    	<input type="password" name="passwordAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,1);" onkeypress="${eventoKeyPress}">
		                                    </logic:notEmpty>	                                    
		                                    <logic:empty name="ec.com.smx.sic.sispe.popUpConfirmacionAux" property="valorKeyPress">
		                                    	<input type="password" name="passwordAutorizacion" class="textNormal" size="20" onkeyup="activar(opRadioAutorizacion,1);">
		                                    </logic:empty>
	                                    </logic:notEmpty>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </logic:notEmpty>
                <tr><td height="10px"></td></tr>
                <tr>
                    <td align="left" class="textoAzul11">Observaci&oacute;n</td>
                </tr>
                <tr>
                    <td align="left">
                        <textarea name="observacionAutorizacion" cols="50" rows="4" class="textNormal"></textarea>
                    </td>
                </tr>
                <tr><td height="5"></td></tr>
            </table>
        </td>
    </tr>
</table>
<script language="javascript">document.getElementById('opAutorizacion').checked=true;</script>