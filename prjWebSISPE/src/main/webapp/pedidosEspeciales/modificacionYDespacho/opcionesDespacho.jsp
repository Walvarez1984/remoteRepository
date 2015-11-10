<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <tr>
        <td class="textoNegro11" align="center">	
            <table cellpadding="0" cellspacing="0" border="0" width="50%">
                <tr>
                    <td align="left">
                        <html:radio  property="opTipoAgrupacion" value="lp">Local y Pedido</html:radio>
                    </td>
                </tr>
                 <tr>
                    <td align="left">
                        <html:radio property="opTipoAgrupacion" value="ar">Artículo</html:radio>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>