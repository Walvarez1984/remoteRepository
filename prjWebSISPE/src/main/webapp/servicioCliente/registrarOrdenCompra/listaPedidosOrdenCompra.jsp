<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <%--Contenido de la p&aacute;gina--%>
    <tr>
        <td align="center" valign="top">
            <table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
                <tr><td height="7px" colspan="3"></td></tr>
                <tr>
                    <%--Datos--%> 
                    <TD class="datos" width="80%">
                        <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                            <%--Titulo de los datos--%>
                            <tr>
                                <td class="fila_titulo" colspan="7">
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                        <tr>
                                            <td><img src="images/detalle_pedidos24.gif" border="0"/></td>
                                            <td height="23" width="100%">&nbsp;Detalle de pedidos</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td height="5px"></td></tr>
                            <tr>
                                <td>
                                    <div id="resultadosBusqueda">
                                        <table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.pedidos">
                                                <tr>
                                                    <td colspan="5" align="right" id="seg_pag">
                                                        <smx:paginacion start="${registrarOrdenCompraForm.start}" range="${registrarOrdenCompraForm.range}" results="${registrarOrdenCompraForm.size}" styleClass="textoNegro11" url="registrarOrdenCompra.do" requestAjax="'seg_pag','div_listado'"/>
                                                    </td>
                                                </tr> 
                                                <tr>
                                                    <td colspan="5">
                                                        <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="2">	
                                                            <tr class="tituloTablas">
                                                                <td width="4%" align="center">No</td>
                                                                <td class="columna_contenido" width="15%" align="center">No Pedido</td>
                                                                <td class="columna_contenido" width="8%" align="center">No Reserva</td>
                                                                <td class="columna_contenido" width="16%" align="center">Local</td>
                                                                <td class="columna_contenido" width="29%" align="center">Cliente - Empresa</td>
                                                                <td class="columna_contenido" width="12%" align="center">Valor Total</td>
                                                                <td class="columna_contenido" width="16%" align="center">Acci&oacute;n</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div id="div_listado" style="width:100%;height:375px;overflow:auto">
                                                            <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="2">
                                                                <logic:iterate name="ec.com.smx.sic.sispe.pedidos" id="pedido" indexId="numeroRegistro">
                                                                    <bean:define id="indice" value="${numeroRegistro + registrarOrdenCompraForm.start}"/> 
                                                                    <%--------- control del estilo para el color de las filas --------------%>
                                                                    <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                                    <bean:define id="fila" value="${indice + 1}"/>
                                                                    <logic:equal name="residuo" value="0">
                                                                        <bean:define id="colorBack" value="blanco10"/>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="residuo" value="0">
                                                                        <bean:define id="colorBack" value="grisClaro10"/>
                                                                    </logic:notEqual>
                                                                    <%--------------------------------------------------------------------%>
                                                                    <tr class="${colorBack}"> 
                                                                        <td class="fila_contenido" align="center" width="4%">${fila}</td>
                                                                        <td class="columna_contenido fila_contenido" align="center" width="15%">
                                                                        	<html:link title="ver el detalle completo del pedido" action="detalleEstadoPedido" paramId="indice" paramName="numeroRegistro" onclick="popWait();"><bean:write name="pedido" property="id.codigoPedido"/></html:link>
                                                                        	<%--<html:link href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${numeroRegistro}','ESTDETPED')"><bean:write name="pedido" property="id.codigoPedido"/></html:link>--%>
                                                                       	</td>
                                                                        <td class="columna_contenido fila_contenido" align="center" width="8%"><bean:write name="pedido" property="llaveContratoPOS"/></td>
                                                                        <td class="columna_contenido fila_contenido" width="16%" align="left">
                                                                            <bean:write name="pedido" property="id.codigoAreaTrabajo"/> - <bean:write name="pedido" property="nombreLocal"/>
                                                                        </td>
                                                                        <td class="columna_contenido fila_contenido" width="29%" align="left"><bean:write name="pedido" property="contactoEmpresa"/></td>
                                                                        <td class="columna_contenido fila_contenido" width="12%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${pedido.totalPedido}"/></td>
                                                                        <td class="columna_contenido fila_contenido" width="16%" align="center">
                                                                            <table>
                                                                                <tr>
                                                                                    <td>
                                                                                        <html:link action="registrarOrdenCompra" paramId="indice" paramName="numeroRegistro" title="Registrar Ordenes de Compra" onclick="popWait();">Registrar Ordenes de Compra</html:link>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </logic:iterate>
                                                            </table>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </logic:notEmpty>
                                            <logic:empty name="ec.com.smx.sic.sispe.pedidos">
                                                <tr>
                                                    <td colspan="5">
                                                        Seleccione un criterio de b&uacute;squeda
                                                    </td>
                                                </tr>
                                            </logic:empty>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </TD>
                    <%--Fin Datos--%>
                </tr>
                <%--Fin P&aacute;gina--%>
            </table>
        </td>
    </tr>
</TABLE>
