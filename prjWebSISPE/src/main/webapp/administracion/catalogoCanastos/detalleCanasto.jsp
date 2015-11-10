<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime(); %>

<html>
    <bean:define id="articuloDTO" name="ec.com.smx.sic.sispe.articuloDTO"></bean:define>
    <title>Lista de Art&iacute;culos</title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="GENERATOR" content="IBM Software Development Platform">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="max-age" content="0"> 
    <meta http-equiv="Expires" content="0">
    <link href="css/textos.css" rel="stylesheet" type="text/css">
    <link href="css/componentes.css" rel="stylesheet" type="text/css">
    <base target="_self">
    <body>
        <table border="0" width="100%" align="center" cellpadding="1" cellspacing="1">
            <html:form action="catalogoCanastos" method="post">
                <tr><td height="5px"></td></tr>
                <%-- tabla de detalles --%>
                <tr>
                    <td valign="top" align="center" height="200">
                        <img src="obtenerImagen.do?id=<%=id%>" width="300" height="250" border="0"/>
                    </td>
                    <%-- tabla de detalles --%>
                    <td valign="top">
                        <table border="0" class="textoAzul10" cellspacing="1" cellpadding="0">
                            <tr>
                                <td height="20" valign="top" align="right" class="textoNegro13" >
                                    <b><bean:write name="articuloDTO" property="descripcionArticulo"/></b>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top" align="right" class="textoRojo11" height="8">
                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articuloDTO.articulo.precioBaseImp}"/>
                                </td>
                            </tr>
                            <tr  valign="top" align="right" class="textoRojo9" height="8">
                                <td>
                                    Incluye IVA.
                                </td>
                            </tr>
                            <tr><td height="8px"></td></tr>
                            <logic:notEmpty name="articuloDTO" property="recetaArticulos">
                                <tr>
                                    <td>
                                        <div style="width:310px;height:330px;overflow:auto;">
                                            <table width="100%" cellpadding="1" cellspacing="0" class="textoAzul10">
                                                <logic:iterate id="recetaArticuloDTO" name="articuloDTO" property="recetaArticulos">
                                                    <tr>
                                                        <td>
                                                            <li><bean:write name="recetaArticuloDTO" property="articuloRelacion.descripcionArticulo"/>,&nbsp;<bean:write name="recetaArticuloDTO" property="articuloRelacion.articuloMedidaDTO.referenciaMedida"/></li>
                                                        </td>
                                                    </tr>
                                                </logic:iterate>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                        </table>
                    </td>
                </tr>
            </html:form>
        </table>
    </body>
</html>