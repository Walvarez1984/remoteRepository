<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="include/top.jsp"/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro12" align="center">
    <tr>
        <td class="titulosAccion" height="35px">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="3%" align="center"><img src="./images/error_48.gif" border="0"></td>
                    <td align="left">P&aacute;gina restringida</td>
                    <td align="right">
                        <div id="botonA">
                            <a class="cancelarA" href="menuPrincipal.do">Salir</a>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="10px"></td></tr>
    <tr>
        <td align="center">
            <b>La p&aacute;gina solicitada esta restringida. Consulte con el administrador del Sistema.</b>
        </td>
    </tr>
</table>

<tiles:insert page="include/bottom.jsp"/>