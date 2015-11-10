<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
<div id="rptEstadoPedidoEspTxt">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
        <link href="../../css/textos.css" rel="stylesheet" type="text/css">
        <title>Reporte</title>
    </head>
    <body>
        <table border="0" class="textoNegro11" width="98%" align="center" cellpadding="0" cellspacing="0" class="textoNegro10">
            <%----- secci&oacute;n del resumen del pedido -----%>
            <tr><td height="5px"></td></tr>
            <tr>
                <td colspan="2" width="100%">
                    <%------ para que el reporte se despliegue en un nueva ventana ------%>
                    <table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td align="left">
                                <logic:equal name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="npEstadoPedido" value="ESO">
                                    SOLICITUD
                                </logic:equal>
                                <logic:equal name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="npEstadoPedido" value="ECO">
                                    CONFIRMACION
                                </logic:equal>
                            </td>
                            <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.pedidoDTO">                                                        	                            	
                                <td align="right">
                                    <label>N&uacute;mero de pedido:&nbsp;</label><bean:write name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="id.codigoPedido"/>&nbsp;&nbsp;&nbsp;
                                </td>                                
                            </logic:notEmpty>
                        </tr>
                        <tr><td height="5px"></td></tr>
                    </table>
                </td>
            </tr>
			<tr>
				<table border="0" width="100%" cellspacing="0" cellpadding="1" align="left" class="textoNegro10" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 11px;text-align:justify">											
					<!--Todo el contenido de la cabecera se paso a la pagina cabeceraContactoFormulario.jsp -->
					<tiles:insert page="/contacto/cabeceraContactoFormulario.jsp">	
						<tiles:put name="tformName" value="crearPedidoEspecialForm"/>
						<tiles:put name="textoNegro" value="textoNegro11"/>
						<tiles:put name="textoAzul" value="textoNegro11"/>
					</tiles:insert>																																										
				</table>										
			</tr>
            <tr>
                <td colspan="2" align="right">
                    <table border="0" width="100%" cellpadding="0" cellspacing="0" class="textoNegro10">
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td align="left" width="40%">
                                Elaborado en:&nbsp;<bean:write name="crearPedidoEspecialForm" property="localResponsable"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="left" width="40%">
                                <logic:equal name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="npEstadoPedido" value="ESO">
                                    Estado:&nbsp;solicitado
                                </logic:equal>
                                <logic:equal name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="npEstadoPedido" value="ECO">
                                    Estado:&nbsp;confirmado
                                </logic:equal>
                            </td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                    </table>
                </td>
            </tr>
            <tr><td height="5px"></td></tr>
            <tr>
                <td colspan="2">
                    <table border="0" width="100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td height="20px" align="left" colspan="2">Lista de los art&iacute;culos del pedido</td>
                        </tr>
                        <tr><td colspan="2"></td></tr>
                        <tr>
                            <td colspan="2">
                                <table border="0" align="left" cellspacing="0" cellpadding="1" width="100%">
                                    <tr class="textoNegro11">
                                        <td align="left" width="5%">No</td>
                                        <td width="12%" align="left">C&Oacute;DIGO BARRAS</td>
                                        <td align="left" width="28%">ART&Iacute;CULO</td>
                                        <td align="right" width="7%">CANTIDAD</td>
                                        <td align="right" width="7%">PESO</td>
                                        <td width="10%" align="right">V. UNIT.</td>
                                        <td width="1%"></td>
                                        <td width="31%" align="left">OBSERVACI&Oacute;N</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr><td colspan="2"></td></tr>
                        <tr>
                            <td colspan="2" class="textoNegro10">
                                <div id="detallePedido" style="width:100%;height:250px;overflow:auto;">
                                    <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
                                            <logic:iterate name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol" id="detalle" indexId="indice">
                                                <bean:define id="numFila" value="${indice + 1}"/>
                                                <%--------- control del estilo para el color de las filas --------------%>
                                                <bean:define id="residuo" value="${indice % 2}"/>
                                                <logic:equal name="residuo" value="0">
                                                    <bean:define id="colorBack" value="blanco10"/>
                                                </logic:equal>
                                                <logic:notEqual name="residuo" value="0">
                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                </logic:notEqual>
                                                <%--------------------------------------------------------------------%>
                                                <tr class="${colorBack}">
                                                    <td width="5%" align="left">&nbsp;<bean:write name="numFila"/></td>
                                                    <td width="12%" align="left">&nbsp;<bean:write name="detalle" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
                                                    <td width="28%" align="left"><bean:write name="detalle" property="articuloDTO.descripcionArticulo"/></td>
                                                    <td width="7%" align="right">&nbsp;
                                                        <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado == 0.00}">
                                                            <bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
                                                        </c:if>	
                                                        <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado > 0.00}">
                                                            NA
                                                        </c:if>
                                                    </td>
                                                    <td width="7%" align="right">&nbsp;
                                                        <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado == 0.00}">
                                                            NA
                                                        </c:if>	
                                                        <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado > 0.00}">
                                                            <bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
                                                        </c:if>
                                                    </td>
                                                    <td width="10%" align="right">&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                                                    <td width="1%"></td>
                                                    <td width="31%" align="left">&nbsp;<bean:write name="detalle" property="estadoDetallePedidoDTO.observacionArticulo"/>
                                                </tr>
                                            </logic:iterate>
                                        </logic:notEmpty>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</div>
</html>