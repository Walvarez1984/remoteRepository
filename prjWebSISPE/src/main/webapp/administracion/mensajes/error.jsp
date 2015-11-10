<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro12" align="center">
    <tr>
        <td class="titulosAccion" height="35px">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="3%" align="center"><img src="././images/error_48.gif" border="0"></td>
                    <td align="left">ERROR - SISPE</td>
                    <td align="right">
                        <div id="botonA">
                            <a class="atrasA" href="#" onclick="history.go(-2); return false;">Atras</a>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="10px"></td></tr>
    <tr>
        <td align="center">
            <b>No se pudo procesar la petici&oacute;n, vuelva a intentarlo o dirijase al men&uacute; principal y escoja la opci&oacute;n que desea realizar.</b>
        </td>
    </tr>
</table>
<tiles:insert page="/include/bottom.jsp"/>