<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<c:set var="localAnterior" value=""/>
<bean:define id="totalVentas" value="${0}"/>
<c:set var="total" value="${0}"/>

<kreport:pdfout>
    <html>
        <head>
            <meta http-equiv="Content-Style-Type" content="text/css"/>
            <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
            <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
            <meta HTTP-EQUIV="max-age" CONTENT="0"/>
            <meta HTTP-EQUIV="Expires" CONTENT="0"/>
        </head>
        
        <body>
            
            <table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
                <tr>
                    <td font-size="7pt" align="center">
                        REPORTE DE ARTICULOS CON PRODUCCION PENDIENTE
                    </td>    
                </tr>
                <tr>
                    <td font-size="7pt" align="right">&#32;.</td>    
                </tr>
                <%--Datos de Produccion--%>
                <logic:notEmpty name="ec.com.smx.sic.sispe.articulos">
                    <tr>
                        <td font-size="7pt">
                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                <tr align="left">
                                    <td rowspan="2" align="left" width="70px">CODIGO</td>
                                    <td rowspan="2" align="left" width="150px">ARTICULO</td>
                                    <td rowspan="2" align="left" width="50px">UND. MANEJO</td>
                                    <td colspan="3" align="center" width="150px">PRODUCCION</td>
                                    <td rowspan="2" align="right" width="50px">PRODUCIDO</td>
                                </tr>
                                <tr align="left">
                                    <td align="center" width="50px">TOTAL</td>
                                    <td align="center" width="50px">PRODUCIDO</td>
                                    <td align="center" width="50px">PENDIENTE</td>
                                </tr>
                                <bean:define id="contador" value="${0}" toScope="session"/>	
                                <logic:iterate name="ec.com.smx.sic.sispe.articulos" id="vistaArticuloDTO" indexId="indiceArticulo">
                                    <bean:define id="fila" value="${indiceArticulo + 1}"/>
                                    <bean:define id="contador" value="${contador+1}" toScope="session"/>
                                    <tr> 
                                        <td align="left" width="70px"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                                        <td align="left" width="150px"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                                        <td align="left" width="50px"><bean:write name="vistaArticuloDTO" property="unidadManejo"/></td>
                                        <td align="center" width="50px"><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></td>   					    
                                        <td align="center" width="50px"><bean:write name="vistaArticuloDTO" property="cantidadParcialEstado"/></td>
                                        <td align="center" width="50px"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
                                        <td align="right" width="50px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaArticuloDTO.porcentajeProducido}"/>&#32;%</td>
<%--                                         <td align="right" width="50px"><bean:write name="vistaArticuloDTO" property="porcentajeProducido" formatKey="formatos.numeros"/>&#32;%</td>  					       							     --%>
                                    </tr>
                                    <tr>
                                        <td colspan="7">
                                            <logic:notEmpty name="vistaArticuloDTO" property="articulos">
                                                <table border="1" width="100%" align="center" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td>
                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                <tr align="left">
                                                                    <td align="left" width="70px">CODIGO</td>
                                                                    <td align="left">ARTICULO</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                <logic:iterate name="vistaArticuloDTO" property="articulos" id="articuloDTO" indexId="indiceDetalleArticulo">
                                                                    <bean:define id="contador" value="${contador+1}" toScope="session"/>	
                                                                    <bean:define id="fila" value="${indiceDetalleArticulo}"/>
                                                                    <tr> 
                                                                        <td align="left" width="70px"><bean:write name="articuloDTO" property="codigoBarras"/></td>
                                                                        <td align="left"><bean:write name="articuloDTO" property="descripcionArticulo"/></td>
                                                                    </tr>
                                                                </logic:iterate>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </logic:notEmpty>
                                            <logic:notEmpty name="vistaArticuloDTO" property="npDesplegarItemsReceta">
                                                <table border="1" width="98%" align="center" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td>
                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                <tr class="tituloTablas"  align="left">
                                                                    <td align="left" width="70px">CODIGO</td>
                                                                    <td align="left" width="250px">ARTICULO</td>
                                                                    <td align="center" width="50px">CANTIDAD</td>
                                                                    <td align="center" width="70px">CANT. MAX. PROD.</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                <logic:iterate name="vistaArticuloDTO" property="recetaArticulos" id="recetaArticuloDTO" indexId="indiceDetalleArticulo">
                                                                    <bean:define id="contador" value="${contador+1}" toScope="session"/>	
                                                                    <bean:define id="fila" value="${indiceDetalleArticulo}"/>
                                                                    <logic:notEmpty name="recetaArticuloDTO" property="articulos">
                                                                        <tr> 
                                                                            <td align="left" width="70px"><bean:write name="recetaArticuloDTO" property="articuloDTO.codigoBarras"/></td>
                                                                            <td align="left" width="250px"><bean:write name="recetaArticuloDTO" property="articuloDTO.descripcionArticulo"/></td>
                                                                            <td align="center" width="50px"><bean:write name="recetaArticuloDTO" property="cantidadArticulo"/></td>																								
                                                                            <td align="center" width="70px">
                                                                                <bean:write name="recetaArticuloDTO" property="npCantidadMaximaProducir"/>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td colspan="4">
                                                                                <bean:define id="articulos" name="recetaArticuloDTO" property="articulos" toScope="session"/>
                                                                                <table border="1" width="95%" align="center" cellspacing="0" cellpadding="0">
                                                                                    <tr>
                                                                                        <td>
                                                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                                                <tr align="left">
                                                                                                    <td align="left" width="70px">CODIGO</td>
                                                                                                    <td align="left">ARTICULO</td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td>
                                                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                                                <logic:iterate name="articulos" id="articuloDTO" indexId="indiceDetalleArticulos">
                                                                                                    <bean:define id="fila" value="${indiceDetalleArticulo + 1}"/>
                                                                                                    <tr> 
                                                                                                        <td align="left" width="70px"><bean:write name="articuloDTO" property="codigoBarras"/></td>
                                                                                                        <td align="left"><bean:write name="articuloDTO" property="descripcionArticulo"/></td>
                                                                                                    </tr>
                                                                                                </logic:iterate>
                                                                                            </table>
                                                                                        </td>
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                    </logic:notEmpty>
                                                                </logic:iterate>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </logic:notEmpty>		
                                        </td>
                                    </tr>     	
                                </logic:iterate>
                            </table>
                        </td>
                    </tr>
                </logic:notEmpty>
            </table>
            <%--Fin datos de ventas--%>
        </body>
    </html>
</kreport:pdfout>