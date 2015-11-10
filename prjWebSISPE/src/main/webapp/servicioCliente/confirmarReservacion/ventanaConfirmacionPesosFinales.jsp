<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.saldoAbono">
	<bean:define id="saldoAbono" name="ec.com.smx.sic.sispe.pedido.saldoAbono"></bean:define>
</logic:notEmpty>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellpadding="0" cellspacing="5">
    <tr>
        <td align="left">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                	<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
	                <td align="left">
	                 	Est&aacute; seguro(a) que los <b>PESOS</b> ingresados son los correctos.?
	                </td>
                </tr>
                <tr>
                <td valign="top" width="3%">&nbsp;&nbsp;</td>
                	<logic:lessThan name="ec.com.smx.sic.sispe.pedido.saldoAbono" value="${0}">
	                	<td align="left" class="textoRojo11">
	                		<b>Se Generar&aacute; una devoluci&oacute;n por el valor de: $<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.pedido.saldoAbono}"/></b>
		                </td>
	                </logic:lessThan>
	                <logic:greaterThan name="ec.com.smx.sic.sispe.pedido.saldoAbono" value="${0}">
	                	<td align="left" class="textoRojo11">
	                		<b>Existe un saldo a cancelar por el valor de: $<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${saldoAbono}"/>
		                </td>
	                </logic:greaterThan>
                </tr>
            </table>
        </td>
    </tr>                                 
</table>