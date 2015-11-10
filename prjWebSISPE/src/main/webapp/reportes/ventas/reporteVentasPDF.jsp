<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<c:set var="codigoPedido" value=""/>
<c:set var="cantidadRegistros" value="${0}"/>
<bean:define id="totalVentas" value="${0}"/>

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
                            <%--Cabecera--%>
                            <tr>
                                <td colspan="6" font-size="11pt">
                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6" font-size="10pt">Reporte de Ventas</td>
                            </tr>
                            <%--Local--%>
                            <tr>
                                <td colspan="6">
                                    <logic:present name="ec.com.smx.sic.sispe.nombreLocalResponsable" scope="request">
                                        <span>Local Responsable:</span>&#32;
                                        <span><bean:write name="ec.com.smx.sic.sispe.nombreLocalResponsableVentas" scope="request"/></span>
                                    </logic:present>
                                </td>
                            </tr>
                            <tr><td height="15" colspan="6"><hr/></td></tr>
                            
                            <%--Datos de Ventas--%>
                            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado">
                                <c:set var="codigoPedido" value=""/>
                                <c:set var="cantidadRegistros" value="${0}"/>
                                <bean:define id="totalVentas" value="${0}"/>
                                <tr><td height="6"></td></tr>
                                <tr>
                                    <td align="center">Local</td>
                                    <td align="center">N&uacute;mero pedido</td>
                                    <td align="center">N&uacute;mero reserva</td>
                                    <td align="center">Persona responsable</td>
                                    <td align="center">Pago del pedido</td>
                                    <td align="center">Fecha del pedido</td>
                                </tr>
                                <tr><td height="15" colspan="6"><hr/></td></tr>
                                
                                <c:set var="pedidoAnterior" value=""/>
                                <c:set var="total" value="${0}"/>	
                                <c:set var="cambiaTotal" value="${0}"/>
                                <c:set var="primeraIteracion" value="${0}"/>
                                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOColPaginado" id="pedidoActual" indexId="indice">
                                    <c:if test="${cambiaTotal == 0}">
                                        <c:set var="valorTotal" value="${pedidoActual.totalPedido}"/>	
                                        <c:set var="cambiaTotal" value="${1}"/>
                                    </c:if>
                                    
                                    <c:if test="${pedidoAnterior != pedidoActual.id.codigoPedido}">
                                        <c:set var="valorActual" value="${pedidoActual.totalPedido}"/>
                                        <c:if test="${primeraIteracion != 0}">
                                            <tr>
                                                <td colspan="6">
                                                    <table>
                                                        <tr>
                                                            <td colspan="4">&#32;</td>
                                                            <td>Total Ganancia Otros Locales:</td>
                                                            <td><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${total}"/></b></td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="4">&#32;</td>
                                                            <td>Total Venta Local:</td>
                                                            <td>
                                                                <c:set var="totVentaLocal" value="${valorTotal-total}"/>
                                                                <b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totVentaLocal}"/>/b>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="4">&#32;</td>
                                                            <td>Total Ventas Pedido:</td>	
                                                            <td><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorTotal}"/></b></td>
                                                        </tr>
                                                        <c:set var="valorTotal" value="${valorActual}"/>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr><td height="5"></td></tr>
                                            <c:set var="total" value="${0}"/>
                                        </c:if>
                                        
                                        
                                        <tr>
                                            <td align="center"><bean:write name="pedidoActual" property="id.codigoLocal"/> - <bean:write name="pedidoActual" property="nombreLocal"/></td>	
                                            <td align="center">
                                                <bean:write name="pedidoActual" property="id.codigoPedido"/>
                                            </td>
                                            <td align="center">
                                                <bean:write name="pedidoActual" property="llaveContratoPOS"/>
                                            </td>
                                            <td align="left">
                                                <b>CI:</b> <bean:write name="pedidoActual" property="cedulaContacto"/> -
                                                <b>NC:</b> <bean:write name="pedidoActual" property="nombreContacto"/> -
                                                <b>TC:</b> <bean:write name="pedidoActual" property="telefonoContacto"/> -
                                                <b>NE:</b> <bean:write name="pedidoActual" property="nombreEmpresa"/>
                                            </td>
                                            <td align="left">
                                                <smx:equal name="pedidoActual" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
                                                    <bean:message key="label.codigoEstadoPSP"/>
                                                </smx:equal>
                                                <smx:equal name="pedidoActual" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
                                                    <bean:message key="label.codigoEstadoPPA"/>
                                                </smx:equal>
                                                <smx:equal name="pedidoActual" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
                                                    <bean:message key="label.codigoEstadoPTO"/>
                                                </smx:equal>
                                            </td>
                                            <td align="center"><bean:write name="pedidoActual" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
                                            
                                        </tr>
                                        <tr><td colspan="6">&#32;</td></tr>
                                        <tr><td colspan="6"><hr/></td></tr>
                                        <tr>
                                            <td align="center">Local Entrega / Direcci&oacute;n Entrega</td>
                                            <td align="center">Art&iacute;culo</td>
                                            <td align="center">Cantidad</td>
                                            <td align="center">Fecha Despacho</td>
                                            <td align="center">Fecha Entrega</td>
                                            <td align="center">Ganancia Otro Local</td>
                                        </tr>
                                        <tr><td colspan="6"><hr/></td></tr>
                                        <c:set var="primeraIteracion" value="${1}"/>
                                        <c:set var="pedidoAnterior" value="${pedidoActual.id.codigoPedido}"/>
                                        <%--<c:set var="codigoPedido" value="${pedidoActual.id.codigoPedido}"/>--%>
                                        <c:set var="totalVentas" value="${totalVentas + pedidoActual.totalPedido}"/>
                                        <c:set var="cantidadRegistros" value="${cantidadRegistros + 1}"/>	
                                    </c:if>
                                    
                                    <tr>
                                        <td>
                                            <bean:write name="pedidoActual" property="direccionEntrega"/>
                                        </td>
                                        <td align="right">
                                            <bean:write name="pedidoActual" property="descripcionArticulo"/>
                                        </td>
                                        <td align="right">
                                            <bean:write name="pedidoActual" property="cantidadEntrega"/>
                                        </td>
                                        <td align="center">
                                            <bean:write name="pedidoActual" property="fechaDespachoBodega" formatKey="formatos.fecha"/>
                                        </td>
                                        <td align="center">
                                            <bean:write name="pedidoActual" property="fechaEntregaCliente" formatKey="formatos.fecha"/>
                                        </td>	
                                        <td align="right">
                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${pedidoActual.gananciaVentasLocalEntrega}"/>
                                        </td>	
                                    </tr>
                                    <c:set var="total" value="${total + pedidoActual.gananciaVentasLocalEntrega}"/>
                                </logic:iterate>
                                <tr><td colspan="6"><hr/></td></tr>
                                <tr>
                                    <td colspan="6">
                                        <table>
                                            <tr>
                                                <td colspan="4">&#32;</td>
                                                <td>Total Ganancia Otros Locales:</td>
                                                <td><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${total}"/></b></td>
                                            </tr>
                                            <tr>
                                                <td colspan="4">&#32;</td>
                                                <td>Total Venta Local:</td>
                                                <td>
                                                    <c:set var="totVentaLocal" value="${valorTotal-total}"/>
                                                    <b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totVentaLocal}"/></b>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="4">&#32;</td>
                                                <td>Total Ventas Pedido:</td>	
                                                <td><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${valorTotal}"/></b></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                
                                <c:set var="cambiaTotal" value="${0}"/>
                                <%--
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <td colspan="4">&#32;</td>
                                    <td><b>TOTAL VENTAS:</b></td>
                                    <td><b><bean:write name="totalVentas" formatKey="formatos.numeros"/></b></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    --%>
                                <tr><td height="6"></td></tr>
                            </logic:notEmpty>
                        </table>
                    </td>
                </tr>
            </table>
            
            <%--Fin datos de ventas--%>
        </body>
    </html>
</kreport:pdfout>