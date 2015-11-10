<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
        <link href="../../css/textos.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <TABLE  class="textoNegro10" border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td>
                    <table  class="textoNegro12" border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr>
                            <td><bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&nbsp;-</td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td>PEDIDOS ESPECIALES</td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td>DETALLE DE LOS PEDIDOS AGRUPADOS POR ART&Iacute;CULO</td>
                        </tr>
                        <tr><td height="10px"></td></tr>
                    </table>
                </td>
            </tr>
            <tr><td height="15px"></td></tr>
            <tr valign="top">
                <td>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.articulo">
                        <table class="textoNegro10" border="0" cellspacing="0" align="center" width="100%" cellpadding="1"> 
                            <tr>
                                <td>C&oacute;digo barras</td>
                                <td>Descripci&oacute;n</td>
                                <td align="right">Cantidad total</td>
                                <td align="right">Peso total</td>
                            </tr>
                            <logic:iterate name="ec.com.smx.sic.sispe.reporte.articulo" id="vistaReporteGeneralDTO1" >
                                <logic:notEmpty name="vistaReporteGeneralDTO1" property="detallesReporte">
                                    <tr valign="top" class="textoNegro12">
                                        <td>
                                            <bean:write  name="vistaReporteGeneralDTO1" property="codigoBarras"/> 
                                        </td>
                                        <td>
                                            <bean:write  name="vistaReporteGeneralDTO1" property="descripcionArticulo"/>
                                        </td>
                                        <td align="right">
                                            <bean:write  name="vistaReporteGeneralDTO1" property="cantidadTotal"/> 
                                        </td>
                                        <td align="right">
                                            <bean:write  name="vistaReporteGeneralDTO1" property="pesoTotal"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center" colspan="4">
                                            <table cellpadding="1" border="0" width="80%">
                                                <tr>
                                                    <td align="left" width="30%">Local</td>
                                                    <td align="right" width="10%">Cantidad total</td>
                                                    <td align="right">Peso total</td>
                                                </tr>
                                                <logic:iterate name="vistaReporteGeneralDTO1" property="detallesReporte" id="vistaReporteGeneralDTO2">
                                                    <logic:notEmpty name="vistaReporteGeneralDTO2" property="detallesReporte">
                                                        <tr>
                                                            <td align="left" width="30%">
                                                                <bean:write  name="vistaReporteGeneralDTO2" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;
                                                                <bean:write  name="vistaReporteGeneralDTO2" property="nombreLocal"/>
                                                            </td>
                                                            <td align="right" width="10%">
                                                                <bean:write  name="vistaReporteGeneralDTO2" property="cantidadTotal"/>
                                                            </td>
                                                            <td align="right" width="10%">
                                                                <bean:write  name="vistaReporteGeneralDTO2" property="pesoTotal"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="center" colspan="3">
                                                                <table cellpadding="0" cellspacing="0" border="0" width="90%">
                                                                    <tr>
                                                                        <td width="16%" align="left">Pedido</td>
                                                                        <td width="35%" align="left">Cliente</td>
                                                                        <td width="6%" align="right">Cant.</td>
                                                                        <td width="7%" align="right">Peso&nbsp;&nbsp;&nbsp;</td>
                                                                        <td width="40%" align="left">Observación</td>
                                                                        
                                                                    </tr>
                                                                    <logic:iterate name="vistaReporteGeneralDTO2" property="detallesReporte" id="vistaReporteGeneralDTO3">
                                                                        <tr>
                                                                            <td align="left">
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="id.codigoPedido"/>
                                                                            </td>
                                                                            <td align="left">
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="cedulaContacto"/>&nbsp;-&nbsp;<bean:write  name="vistaReporteGeneralDTO3" property="nombreContacto"/>
                                                                            </td>
                                                                            <td align="right">
                                                                                &nbsp; <bean:write  name="vistaReporteGeneralDTO3" property="cantidadEstado"/>
                                                                            </td>
                                                                            <td align="right">
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="pesoArticuloEstado"/>&nbsp;&nbsp;&nbsp;
                                                                            </td>
                                                                            <td align="left">
                                                                                &nbsp;<bean:write  name="vistaReporteGeneralDTO3" property="observacionArticulo"/>
                                                                            </td>
                                                                        </tr>
                                                                    </logic:iterate>
                                                                    <tr><td height="25px"></td></tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </logic:notEmpty>
                                                </logic:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                    <%--<tr><td><p style="page-break-after: always;"></p></td></tr>--%>
                                </logic:notEmpty>
                            </logic:iterate>
                        </table>
                    </logic:notEmpty>
                </td>
            </tr>
        </TABLE>
    </body>
</html>