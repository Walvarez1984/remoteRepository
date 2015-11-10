<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>

<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <table cellpadding="0" cellspacing="0" class="titulosAccion" width="100%">
                <td width="3%" align="center"><img src="./images/SISPE.gif" border="0"></td>
                <td valign="middle">Inicio</td>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center">
            <%-- panel --%>
            <tiles:insert page="/include/mainPanel.jsp"/>
        </td>
    </tr>
</table>
<tiles:insert page="../include/bottom.jsp"/>