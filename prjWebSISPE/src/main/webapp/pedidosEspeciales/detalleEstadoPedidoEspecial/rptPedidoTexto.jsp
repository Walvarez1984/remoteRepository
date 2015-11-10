<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<bean:define id="estadoDespachado" name="ec.com.smx.sic.sispe.estado.despachadoEspecial"></bean:define>
<bean:define id="estadoSolicitado" name="ec.com.smx.sic.sispe.estado.solicitadoEspecial"></bean:define>
<bean:define id="estadoDespachoPrevio" name="ec.com.smx.sic.sispe.estado.despachoPrevio"></bean:define>
<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>

<c:set var="mostrarCamposDespacho" value="0"/>
<c:if test="${vistaPedidoDTO.id.codigoEstado == estadoDespachado || vistaPedidoDTO.id.codigoEstado == estadoDespachoPrevio}">
    <c:set var="mostrarCamposDespacho" value="1"/>
</c:if>

<html>
<div id="rptDetalleEstadoPedidoEspecialTxt">
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
        <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido">
                <bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
                <bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
                <bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
                <tr>
                    <td>
                        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="textoNegro10">
                            <html:form action="detalleEstadoPedidoEspecial" method="post">
                                <tr>
                                    <td valign="top" font-size="7pt" >
                                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                            <tr>
                                                <td align="left" >
                                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&nbsp;-&nbsp;
                                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&nbsp;
                                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
                                                </td>
                                                <td align="right">Ruc:&nbsp;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td>&nbsp;</td></tr>
                                <tr>
                                    <td colspan="4" width="100%" >
                                        
                                        <!-- INICIO CABECERA --> 
                                        <table border="0" cellspacing="0" width="100%" align="left" class="textoNegro10" >
                                            <tr>
                                                <td align="left" width="10%">No. pedido:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/></td>
                                            </tr>
                                            <tr><td height="10px"></td></tr>
                                   			  <!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
											<tiles:insert page="/contacto/cabeceraContactoVistaPedido.jsp"/>
                                            <tr><td height="10px"></td></tr>
                                            <tr>
                                                <td align="left">Estado:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table cellpadding="0" cellspacing="0" width="100%">
                                                        <tr>                                                      
                                                            <td align="left">
                                                                Elaborado en:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
                                                            </td>
                                                            <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoSolicitado}">
                                                                <td align="left">
                                                                    Entrega en:&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoLocalEntrega"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocalEntrega"/>
                                                                </td>
                                                            </logic:notEqual>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoSolicitado}">
                                                <tr>
                                                    <td>
                                                        <table cellpadding="0" cellspacing="0" width="100%">
                                                            <tr>
															    <td align="left" width="12%">Fecha de pedido:</td>
																<td  align="left">
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
																</td>
															
                                                                <td align="left" width="12%">Fecha despacho:</td>
                                                                <td align="left" width="15%"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                                <td align="left" width="15%">Fecha entrega cliente:</td>
                                                                <td align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fechahora"/></td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </logic:notEqual>
                                        </table>
                                        <!-- FIN CABECERA -->
                                    </td>
                                </tr>        
                            </html:form>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="textoNegro10">
                            <tr><td height="10px"></td></tr>
                            <tr align="left">
                                <td>DETALLE DEL PEDIDO:</td>
                            </tr>
                            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
                                <tr>
                                    <td>
                                        <c:set var="columnas" value="7"/>
                                        <logic:equal name="mostrarCamposDespacho" value="1">
                                            <c:set var="columnas" value="12"/>
                                        </logic:equal>
                                        <table width="100%" border="0" cellpadding="2" cellspacing="0" class="textoNegro10">	
                                            <tr>
                                                <td align="left" width="3%">No</td>
                                                <td align="left" width="12%">C&oacute;digo barras</td>
                                                <td align="left" width="20%">Art&iacute;culo</td>
                                                <td align="right" width="5%">Cant.</td>
                                                <td align="right" width="6%">Peso KG.</td>
                                                <td align="center" width="20%">Observaci&oacute;n</td>
                                                <td align="right" width="6%">V. unit.</td>
												<td align="right" width="6%">V. unit. IVA</td>
                                                <logic:equal name="mostrarCamposDespacho" value="1">
                                                    <td align="right" width="6%">Peso reg.</td>
                                                    <td align="right" width="8%">Tot. IVA</td>
                                                    <td align="right" width="2%">IVA</td>
                                                    <td align="right" width="6%">V. unit. neto</td>
                                                    <td align="right" width="8%">Tot. neto</td>
                                                </logic:equal>
                                            </tr>
                                            <c:set var="condicion" value="0.00"/>
                                            <logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
                                                <bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
                                                <tr>
                                                    <td align="left" width="3%" valign="top"><bean:write name="numRegistro"/></td>
                                                    <td align="left" width="12%" valign="top">
                                                        <bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
                                                    </td>
                                                    <td align="left" width="20%" valign="top"><bean:write  name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
                                                    <td align="right" width="5%" valign="top">
                                                        <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.cantidadEstado}"/>
                                                        </logic:equal>
                                                        <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                            <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                        </logic:notEqual>
                                                    </td>
                                                    <td align="right" width="6%" valign="top">
                                                        <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
                                                        </logic:notEqual>
                                                        <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                            <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                        </logic:equal>
                                                    </td>
                                                    <td align="left" width="20%" valign="top"><bean:write name="vistaDetallePedidoDTO" property="observacionArticulo"/></td>
                                                    <td align="right" width="6%" valign="top"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioEstado}"/></td>
                                                    <td align="right" width="6%" valign="top"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                                                    <logic:equal name="mostrarCamposDespacho" value="1">
                                                        <td align="right" width="6%" valign="top">
                                                        	<logic:notEmpty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">
                                                        		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoRegistradoBodega}"/>
                                                            </logic:notEmpty>
                                                            <logic:empty name="vistaDetallePedidoDTO" property="pesoRegistradoBodega">---</logic:empty>
                                                        </td>
                                                        <td align="right" width="8%" valign="top"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalEstadoIVA}"/></td>
                                                        <td align="right" width="2%" valign="top">
                                                            <logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                I
                                                            </logic:equal>
                                                            <logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">&nbsp;</logic:notEqual>
                                                        </td>
                                                        <td align="right" width="6%" valign="top">
                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioPOS}"/>
                                                        </td>
                                                        <td align="right" width="8%" valign="top">
                                                        	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
                                                        </td>
                                                    </logic:equal>
                                                </tr>	
                                            </logic:iterate>
                                            <tr><td height="15px"></td></tr>
                                            <logic:equal name="mostrarCamposDespacho" value="1">
                                                <tr align="right">
                                                    <td align="right" colspan="${columnas}">
                                                        <table border="0" cellpadding="0" cellspacing="0" align="right">
                                                            <tr height="14">
                                                                <td class="textoNegro10" align="right">SUBTOTAL (Aproximado):&nbsp;</td>
                                                                <td class="textoNegro10" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalPedido}"/>
                                                                </td>	
                                                            </tr>
                                                            <tr><td colspan="2" align="right">------------------------</td></tr>
                                                            <tr height="14">
                                                                <td class="textoNegro10" align="right">TARIFA 0%:&nbsp;</td>
                                                                <td class="textoNegro10" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalNoAplicaIVA}"/>
                                                                </td>
                                                            </tr>
                                                            <tr height="14">
                                                                <td class="textoNegro10" align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
                                                                <td class="textoNegro10" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalAplicaIVA}"/>
                                                                </td>
                                                            </tr>
                                                            <tr height="14">
                                                                <td class="textoNegro10" align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
                                                                <td class="textoNegro10" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.ivaPedido}"/>
                                                                </td>
                                                            </tr>
                                                            <tr><td colspan="2" align="right">------------------------</td></tr>
                                                            <tr height="14">
                                                                <td class="textoNegro10" align="right">TOTAL (Aproximado):&nbsp;</td>
                                                                <td class="textoNegro11" align="right">
                                                                	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.totalPedido}"/>
                                                                </td>
                                                            </tr>	                	                    	
                                                        </table>
                                                    </td>
                                                </tr>	
                                            </logic:equal>
                                        </table>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                        </table>
                    </td>
                </tr>
            </logic:notEmpty>
        </TABLE>
    </body>
</div>
</html>