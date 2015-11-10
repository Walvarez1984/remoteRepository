<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../../include/top.jsp"/>
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro11">
    <tr>
        <td class="titulosAccion" height="35px">
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="3%" align="center"><img src="././images/pregunta.gif" border="0"></img></td>
                    <td align="left">&nbsp;&nbsp;Confirmaci&oacute;n</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <html:form action="confirmacion" method="post">
            <td align="center" valign="middle" height="300px">
                <table border="0" width="40%" class="tabla_informacion" cellspacing="0" bgcolor="#F4F5EB">
                    <tr>
                        <td background="images/barralogin.gif" height="22px" class="textoBlanco11" align="left"><b>&nbsp;&nbsp;Confirmaci&oacute;n</b></td>
                    </tr>
                    <tr>
                        <td>
                            <table border="0" height="50px">
                                <tr>
                                    <td valign="top"><img src="././images/pregunta24.gif" border="0"></td>
                                    <td align="left" valign="top">
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.confirmacion.mensaje">
                                            <html:messages id="mensaje" name="ec.com.smx.sic.sispe.confirmacion.mensaje">
                                                <bean:write name="mensaje"/><br>
                                            </html:messages>
                                        </logic:notEmpty>
                                    </td>
                                </tr>
                            </table>	
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table border="0" cellpadding="0" cellspacing="0" width="70%" height="40px">
                                <tr>
                                    <td align="center">
                                        <html:submit property="botonSI" value="  Si   " styleClass="botonLogin"/>
                                    </td>
                                    <td align="center">
                                        <html:submit property="botonNO" value="  No  " styleClass="botonLogin"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </html:form>
    </tr>
</TABLE>
<tiles:insert page="../../include/bottom.jsp"/>