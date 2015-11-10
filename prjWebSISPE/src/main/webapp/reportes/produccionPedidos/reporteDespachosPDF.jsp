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
<c:set var="total" value="0"/>
<bean:define id="totalVentas" value="0"/>

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
            <table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
                <tr>
                    <td font-size="7pt">
                        <table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
                            <tr>
                                <td colspan="8" font-size="11pt">
                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
                                </td>
                            </tr>
                            
                            
                            <tr>
                                <td colspan="8" font-size="10pt">Reporte de Despachos</td>
                            </tr>
                            
                            
                            <%--Datos de Despachos--%>
                            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">
                                <tr><td height="15" colspan="8"><hr/></td></tr>
                                
                                <tr>
                                    <td align="center">Local</td>
                                    <td align="center" colspan="2">N&uacute;mero pedido</td>
                                    <td align="center">N&uacute;mero reserva</td>
                                    <td align="center" colspan="2">Persona responsable</td>
                                    <td align="center" colspan="2">Estado</td>
                                </tr>	
                                <tr><td height="15" colspan="8"><hr/></td></tr>
                                
                                <c:set var="localAnterior" value=""/>
                                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaReporteGeneralDTO" indexId="nVista">
                                    <%--
                                        <c:if test="${localAnterior != vistaReporteGeneralDTO.id.codigoLocal}">
                                            <tr>
                                                <td colspan="5">
                                                    <table border="0" cellpadding="2" cellspacing="0" width="100%" class="tabla_informacion">
                                                        <tr class="amarilloClaro11">
                                                            <td width="55%">
                                                                <html:link href="#" onclick="requestAjax('reporteDespachos.do', ['resultadosBusqueda','mensajes'], {parameters: 'filtroLocal=${vistaReporteGeneralDTO.id.codigoLocal}',popWait:true});">
                                                                    <bean:write name="vistaReporteGeneralDTO" property="id.codigoLocal"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/>
                                                                </html:link>
                                                            </td>
                                                            <td width="25%">
                                                                <b>Producci&oacute;n por local:</b>
                                                            </td>
                                                            <td width="20%">
                                                                <b><bean:write name="vistaReporteGeneralDTO" property="totalPorProducirPorLocal" formatKey="formatos.numeros"/></b> de
                                                                <b><bean:write name="vistaReporteGeneralDTO" property="totalProducidoPorLocal"/></b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <c:set var="localAnterior" value="${vistaReporteGeneralDTO.id.codigoLocal}"/>	
                                        </c:if>
                                        --%>
                                    
                                    <tr>
                                        <td align="left">
                                            <bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/>
                                        </td>
                                        <td colspan="2" align="center">
                                            <bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
                                        </td>
                                        <td align="center">
                                            <bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
                                        </td>
                                        <td colspan="2" align="left">
                                            <b>CI:</b> <bean:write name="vistaReporteGeneralDTO" property="cedulaContacto"/> -
                                            <b>NC:</b> <bean:write name="vistaReporteGeneralDTO" property="nombreContacto"/> -
                                            <b>TC:</b> <bean:write name="vistaReporteGeneralDTO" property="telefonoContacto"/> -
                                            <b>NE:</b> <bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/>
                                        </td>
                                        <td colspan="2" align="left">
                                            <bean:write name="vistaReporteGeneralDTO" property="descripcionEstado"/>
                                        </td>
                                    </tr>
                                    <tr><td height="15" colspan="8"><hr/></td></tr>
                                    <tr>
                                        <td align="center">Art&iacute;culo</td>
                                        <td align="center">Descripci&oacute;n</td>
                                        <td align="center">Producido</td>
                                        <td align="center">% Producido</td>
                                        <td align="center">Cantidad Despacho</td>
                                        <td align="center">Destino Despacho</td>
                                        <td align="center">Fecha Despacho</td>
                                        <td align="center">Fecha Entrega</td>
                                    </tr>
                                    <tr><td height="15" colspan="8"><hr/></td></tr>
                                    <logic:iterate name="vistaReporteGeneralDTO" property="detallesReporte" id="detalleVistaReporte" indexId="nVistaDetalle">
                                        <tr>
                                            <td align="center">&#32;<bean:write name="detalleVistaReporte" property="id.codigoArticulo"/></td>
                                            <td><bean:write name="detalleVistaReporte" property="descripcionArticulo"/></td>
                                            <td align="center">
                                                <bean:write name="detalleVistaReporte" property="totalProducidoPorArticulo" formatKey="formatos.enteros"/>/<bean:write name="detalleVistaReporte" property="cantidadDespacho"/>
                                            </td>
                                            <td align="center">
                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalleVistaReporte.porcentajeProducido}"/>%
                                            </td>
                                            <td align="center"><bean:write name="detalleVistaReporte" property="cantidadDespacho"/></td>
                                            <td><bean:write name="detalleVistaReporte" property="direccionEntrega"/></td>    
                                            <td align="center"><bean:write name="detalleVistaReporte" property="fechaDespachoBodega" formatKey="formatos.fechahora"/></td>
                                            <td align="center"><bean:write name="detalleVistaReporte" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
                                        </tr>
                                    </logic:iterate> 
                                    <tr>
                                        <td colspan="5">
                                            &#32;
                                        </td>
                                        <td colspan="2" font-size="8pt">
                                            <b>Producci&oacute;n por pedido:</b>
                                        </td>
                                        <td font-size="8pt">
                                            <b><bean:write name="vistaReporteGeneralDTO" property="totalProducidoPorPedido" formatKey="formatos.enteros"/></b> de 
                                            <b><bean:write name="vistaReporteGeneralDTO" property="totalPorProducirPorPedido" formatKey="formatos.enteros"/></b>
                                        </td>
                                    </tr>
                                    <tr><td colspan="8"><hr/></td></tr>
                                    
                                </logic:iterate>
                                
                                
                                
                            </logic:notEmpty>
                        </table>
                    </td>
                </tr>
            </table>
        </body>
    </html>
</kreport:pdfout>