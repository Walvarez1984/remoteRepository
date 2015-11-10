<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dTD">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:useAttribute id="indiceMail" name="pIndiceMail" classname="java.lang.String" ignore="true"/>
<html>
    <head>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
    </head>
    <body>
        <logic:notEmpty name="ec.com.smx.sic.sispe.despachoPedEsp.colLocalesMail">
            <table border="0" cellspacing="0" cellpadding="0" width="100%" style="font-size:11px;font-family: Verdana, Arial, Helvetica;color:#000000">
                <tr>
                    <td align="left" style="color:#FF0000;">
                        <b>CORPORACIÓN FAVORITA C.A.</b>
                    </td>
                </tr>
                <tr><td height="10px"></td></tr>
                <tr><td>Estimado(a) Usuario. Este es un mensaje de confirmación de despacho desde el CD, respecto a la mercadería solicitada para los pedidos especiales, a continuaci&oacute;n el detalle:</td></tr>
                <tr><td height="10px"></td></tr>
                <logic:iterate name="ec.com.smx.sic.sispe.despachoPedEsp.colLocalesMail" id="vistaArticuloDTO" indexId="indice">
                    <logic:equal name="indice" value="${indiceMail}">
                        <tr>
                            <td align="center" valign="top">
                                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                    <tr>
                                        <td width="100%">
                                            <table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
                                                <tr>
                                                    <td align="left">
                                                        <b>Or&iacute;gen:</b>&nbsp;<bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/>
                                                    </td>
                                                </tr>
                                                <tr><td height="5px"></td></tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr><td height="10px"></td></tr>
                                    <tr><td align="left"><b>Detalle de los art&iacute;culos</b></td></tr>
                                    <tr>
                                        <td>
                                            <table border="0" cellpadding="1" cellspacing="0" width="100%" style="border-width: 1px;border-style: solid;border-color: #cccccc;font-size: 10px;">
                                                <tr style="background-color:#FF0F0F;color: #FFFFFF;font-style: normal;line-height: normal;font-family: Verdana, Arial, Helvetica;font-weight: bold;">
                                                    <td align="center">No</td>
                                                    <td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Destino</td>
                                                    <td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">C&oacute;digo barras</td>
                                                    <td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Descripci&oacute;n</td>
                                                    <td align="center" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">Peso Kg.</td>
                                                </tr>
                                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indice2">
                                                    <tr>
                                                        <td align="center">${indice2 + 1}</td>
                                                        <td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">
                                                            <bean:write name="vistaArticuloDTO2" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO2" property="nombreLocalReferencia"/>
                                                        </td>
                                                        <td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">
                                                            <bean:write name="vistaArticuloDTO2" property="codigoBarras"/>
                                                        </td>
                                                        <td align="left" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">
                                                            <bean:write name="vistaArticuloDTO2" property="descripcionArticulo"/>
                                                        </td>
                                                        <td align="right" style="border-left-width: 1px;border-left-style: solid;border-left-color: #CCCCCC;">
                                                            <fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaArticuloDTO2.npPesoIngresado}"/>
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </logic:equal>
                </logic:iterate>
            </table>
        </logic:notEmpty>
    </body>
</html>
