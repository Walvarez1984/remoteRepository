<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<c:set var="localAnterior" value=""/>

<html>
    <head>
        <meta http-equiv="Content-Style-Type" content="text/css"/>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="max-age" CONTENT="0"/>
        <meta HTTP-EQUIV="Expires" CONTENT="0"/>
        
        <style type="text/css">
            .nombreCompania{
            font-size: 20px;
            font-family: Verdana, Arial, Helvetica;
            color: #FF0033;
            font-weight: bolder
            }
            .borde{
            border: thin solid #999999;
            }
            
            .titulo{
            font-size: 15px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            font-weight: bolder
            }
            
            .tituloTablas{
            background-color:#333333;
            color: #FFFFFF;
            font-size: 9px;
            font-family: Verdana, Arial, Helvetica;
            font-weight: bold;
            }
            
            .textoNegro10{
            font-size: 10px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            }
            
            .columna_contenido
            {
            border-left-width: 1px;
            border-left-style: solid;
            border-left-color: #CCCCCC;	
            }
            
            .textoRojo10{
            font-family: Verdana, Arial, Helvetica;
            font-size: 10px;
            color: #990000;
            }
            
            .textoAzul9N{
            font-family: Verdana, Arial, Helvetica;
            font-size: 9px;
            color: #0000aa;
            font-weight:bold;
            }
            
            .textoNegro9{
            font-family: Verdana, Arial, Helvetica;
            font-size: 9px;
            color: #000000;
            }
            
            .tituloTablasCeleste{
            background-color:#DDEEFF;
            color: #000000;
            font-size: 9px;
            font-style: normal;
            line-height: normal;
            font-family: Verdana;
            font-weight: bolder;
            }
            
            
            .verdeClaro10 {
            background-color: #dcf3d1;
            font-family: Verdana;
            font-size: 10px;
            color: #000000;
            }
            
            .blanco10 {	
            background-color: #FFFFFF;
            font-family: Verdana;
            font-size: 10px;
            color: #000000;
            }
            
            .grisClaro {
            background-color: #CCCCCC;
            font-family: Verdana;
            font-size: 11px;
            color: #000000;
            }
            
            .tabla_informacion
            {
            border-width: 1px;
            border-style: solid;
            border-color: #cccccc;
            }
            
            .fila_contenido
            {
            border-bottom-width: 1px;
            border-bottom-style: solid;
            border-bottom-color: #cccccc;
            }
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" align="center">
            <tr>
                <td class="nombreCompania" colspan="3">
                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
                </td>
            </tr>
            <tr>
                <td class="titulo">Reporte de Despachos</td>
            </tr>
            <%--Datos de Despachos--%>
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">
                <tr><td height="6"></td></tr>
                <tr class="tituloTablas">
                    <td align="center" class="borde">Local</td>
                    <td align="center" class="borde" colspan="2">N&uacute;mero pedido</td>
                    <td align="center" class="borde">N&uacute;mero reserva</td>
                    <td align="center" class="borde" colspan="2">Persona responsable</td>
                    <td align="center" class="borde" colspan="2">Estado</td>
                </tr>	
                <c:set var="localAnterior" value=""/>
                <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaReporteGeneralDTO" indexId="nVista">
                     <tr class="grisClaro" height="30" valign="top">
                        <td align="center">
                            <bean:write name="vistaReporteGeneralDTO" property="id.codigoAreaTrabajo"/> - <bean:write name="vistaReporteGeneralDTO" property="nombreLocal"/>
                        </td>
                        <td colspan="2">
                            &nbsp;<bean:write name="vistaReporteGeneralDTO" property="id.codigoPedido"/>
                        </td>
                        <td>
                            &nbsp;<bean:write name="vistaReporteGeneralDTO" property="llaveContratoPOS"/>
                        </td>
	               		<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto != 'RUC'}">
							<td colspan="2">
								<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
								<bean:write name="vistaReporteGeneralDTO" property="numeroDocumentoPersona"/> -
								<bean:write name="vistaReporteGeneralDTO" property="nombrePersona"/> - 
								<bean:write name="vistaReporteGeneralDTO" property="telefonoPersona"/> 
							</td>
						</c:if>
						<c:if test="${vistaReporteGeneralDTO.tipoDocumentoContacto == 'RUC'}">
							<td colspan="2">
								<bean:write name="vistaReporteGeneralDTO" property="tipoDocumentoContacto"/>: 
								<bean:write name="vistaReporteGeneralDTO" property="rucEmpresa"/> -
								<bean:write name="vistaReporteGeneralDTO" property="nombreEmpresa"/> - 	
								<bean:write name="vistaReporteGeneralDTO" property="telefonoEmpresa"/>															
							</td>
						</c:if>
						
                        <td align="center" colspan="2">
                            <bean:write name="vistaReporteGeneralDTO" property="descripcionEstado"/>
                        </td>
                    </tr>
                    <tr class="tituloTablasCeleste">
                        <td align="center" class="borde">C&oacute;digo barras</td>
                        <td align="center" class="borde">Descripci&oacute;n</td>
                        <td align="center" class="borde">Completado</td>
                        <td align="center" class="borde">% Completado</td>
                        <td align="center" class="borde">Cantidad Despacho</td>
                        <td align="center" class="borde">Destino Despacho</td>
                        <td align="center" class="borde">Fecha Despacho</td>
                        <td align="center" class="borde">Fecha Entrega</td>
                    </tr>
                    <logic:iterate name="vistaReporteGeneralDTO" property="detallesReporte" id="detalleVistaReporte" indexId="nVistaDetalle">
                        <bean:define id="residuoDetalle" value="${nVistaDetalle % 2}"/>
                        <logic:equal name="residuoDetalle" value="0">
                            <bean:define id="colorBackDetalle" value="blanco10"/>
                        </logic:equal>
                        <logic:notEqual name="residuoDetalle" value="0">
                            <bean:define id="colorBackDetalle" value="blanco10"/>
                        </logic:notEqual>
                        <tr class = "${colorBackDetalle}">
                            <td class="borde" align="center">&nbsp;<bean:write name="detalleVistaReporte" property="codigoBarras"/></td>
                            <td class="borde"><bean:write name="detalleVistaReporte" property="descripcionArticulo"/></td>
                            <td class="borde" align="right">
                               &nbsp;<bean:write name="detalleVistaReporte" property="totalProducidoPorArticulo" formatKey="formatos.enteros"/>/<bean:write name="detalleVistaReporte" property="cantidadDespacho"/>
                            </td>
                            <c:choose>
                                    <c:when test="${detalleVistaReporte.porcentajeProducido <= 30}">
                                        <c:set var="colorLetraProducido" value="red"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${detalleVistaReporte.porcentajeProducido > 30 && detalleVistaReporte.porcentajeProducido < 100}">
                                                <c:set var="colorLetraProducido" value="orange"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${detalleVistaReporte.porcentajeProducido == 100}">
                                                        <c:set var="colorLetraProducido" value="green"/>
                                                    </c:when>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            <td align="center" style="color:${colorLetraProducido};border: thin solid #999999;">
                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalleVistaReporte.porcentajeProducido}"/>%
                            </td>
                            <td class="borde" align="right"><bean:write name="detalleVistaReporte" property="cantidadDespacho"/></td>
                            <td class="borde"><bean:write name="detalleVistaReporte" property="direccionEntrega"/></td>    
                            <td class="borde" align="center"><bean:write name="detalleVistaReporte" property="fechaDespachoBodega" formatKey="formatos.fechahora"/></td>
                            <td class="borde" align="center"><bean:write name="detalleVistaReporte" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
                        </tr>
                    </logic:iterate>
                    <tr>
                        <td colspan="8">
                            <table>
                                <tr>
                                    <td colspan="5">&nbsp;</td>
                                    <td class="verdeClaro10">&nbsp;</td>
                                    <td class="verdeClaro10">
                                        <b>Completado: </b>
                                    </td>
                                    <td class="verdeClaro10">
                                        <b><bean:write name="vistaReporteGeneralDTO" property="totalProducidoPorPedido" formatKey="formatos.enteros"/></b> de 
                                        <b><bean:write name="vistaReporteGeneralDTO" property="totalPorProducirPorPedido" formatKey="formatos.enteros"/></b>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td height="12"></td></tr>
                </logic:iterate>
                <tr><td height="6"></td></tr>
            </logic:notEmpty>
        </table>
    </body>
</html>