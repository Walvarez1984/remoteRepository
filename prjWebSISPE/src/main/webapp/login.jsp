<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="include/topLogin.jsp"/>

<tiles:insert page="include/mensajes.jsp"/>
<html:form action="/login.do" method="post" focus="login">
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr height="150"><td></td></tr>
        <tr>
            <td valign="top" align="center">
                <table width="350" bordercolor="FE7279"  border="1" cellpadding="0" cellspacing="0" >
                    <tr>
                        <td align="left" height="30" colspan= "2" background="images/barralogin.gif" class="textoBlanco12">&nbsp;<b>Ingreso al SISPE</b></td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="textoNegro11">
                                <tr>
                                    <td width="110"><img src="images/candado.jpg"/></td>
                                    <td bgcolor="F1F3F5" valign="top">
                                        <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                                <td colspan="3" height="10"></td>
                                            </tr>
                                            <tr>
                                                <td align="right"><b>Usuario:</b></td>
                                                <td align="right"><input type="text" name="login" size="17"></td>
                                                <td width="10"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" height="10"></td>
                                            </tr>
                                            <tr>
                                                <td align="right"><b>Contrase&ntilde;a:</b></td>
                                                <td align="right"><input type="password" name="password" size="17"></td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" height="10"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="right">
                                                    <input name="Submit" type="submit" class="botonLogin" value="Ingresar"/>
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" height="10"></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</html:form>
<tiles:insert page="include/bottomLogin.jsp"/>
