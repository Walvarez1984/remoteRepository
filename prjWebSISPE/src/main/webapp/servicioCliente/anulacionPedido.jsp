<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<bean:define id="tipoPedidoNormal" name="ec.com.smx.sic.sispe.tipoPedido.normal"/>
<bean:define id="opNumPedido"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroPedido"/></bean:define>
<bean:define id="opNumCedula"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroCedula"/></bean:define>
<bean:define id="opNombreContacto"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreContacto"/></bean:define>
<bean:define id="opRangoFechas"><bean:message key="ec.com.smx.sic.sispe.opcion.fechas"/></bean:define>
<bean:define id="opTodaTemporada"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define id="estadoActivo"><bean:write name="sispe.estado.activo"/></bean:define>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<% String sessionId = session.getId(); %>
<bean:define id="idSesion" value="<%=sessionId%>"/>

<html:form action="anulacionPedido" method="post">
	<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <%--<html:form action="anulacionPedido" method="post" focus="campoBusqueda">--%>
        
    
        <html:hidden property="ayuda" value=""/>
        <tr>
            <td>
                <div id="divPopUp">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
                        <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
                        <script language="javascript">mostrarModal();</script>
                    </logic:notEmpty>
                </div>
            </td>
        </tr>
        <!--T&iacute;tulos, botones: atr&aacute;s - regresar--> 
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    
                    <tr>
                        <td  width="3%" align="center"><img src="images/cancelarPedido.gif" border="0"></img></td>
                        <td height="35" valign="middle">Anulaci&oacute;n de pedidos</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" class="textos" width="99%" align="center">
                    <tr><td height="3px" colspan="2"></td></tr>
                    <tr>
                        <!--Barra Izquierda-->
                        <td class="datos" width="25%" >
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
                                <!-- B&uacute;squeda-->
                                <tr>
                                    <td colspan="2">
                                        <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td width="80%" class="datos">
                            <div id="resultadosBusqueda">
                                <table cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion">
                                    <tr align="left">
                                        <td>
                                            <table cellpadding="0" cellspacing="0" width="100%" align="center" class="fila_titulo">
                                                <tr>
                                                    <td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                                                    <td height="23" width="100%">&nbsp;Detalle de pedidos</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <logic:notEmpty name="anulacionesForm" property="datos">
                                                <table width="98%" cellspacing="0" cellpadding="0" align="center">
                                                    <tr>
                                                        <td align="right">
                                                            <smx:paginacion start="${anulacionesForm.start}" range="${anulacionesForm.range}" results="${anulacionesForm.size}" campos="false" styleClass="textos" url="anulacionPedido.do" requestAjax="'mensajes','resultadosBusqueda'"/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table cellspacing="0" cellpadding="1" width="100%" align="center" class="tabla_informacion">
                                                                <tr class="tituloTablas"  align="left">
                                                                    <td width="8%" align="center" class="columna_contenido">Acci&oacute;n</td>
                                                                    <td width="4%" align="center" class="columna_contenido">No</td>
                                                                    <td width="15%" align="center" class="columna_contenido">No Pedido</td>
                                                                    <td width="10%" align="center" class="columna_contenido">No Res</td>
                                                                    <td width="30%" align="center" class="columna_contenido">Cliente - Empresa</td>
                                                                    <td width="12%" align="center" class="columna_contenido">Fecha Estado</td>
                                                                    <td width="12%" align="center" class="columna_contenido">Estado</td>
                                                                    <td width="10%" align="center" class="columna_contenido">Valor Total</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <div id="lista_pedidos" style="width:100%;height:430px;overflow:auto">
                                                                <logic:notEmpty name="ec.com.smx.sic.sispe.tipoPedido.especial">
                                                                    <bean:define id="pedidoEspecial" name="ec.com.smx.sic.sispe.tipoPedido.especial"/>
                                                                </logic:notEmpty>   
                                                                <table cellspacing="0" cellpadding="2" width="100%" align="center" class="tabla_informacion">
                                                                    <logic:iterate name="anulacionesForm" property="datos" id="vistaPedidoDTO" indexId="indiceRegistro">
                                                                        <bean:define id="indice" value="${indiceRegistro + anulacionesForm.start}"/>
                                                                        <bean:define id="fila" value="${indice + 1}"/>
                                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                                        <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                                                                        <logic:equal name="residuo" value="0">
                                                                            <bean:define id="clase" value="blanco10"/>
                                                                        </logic:equal>	
                                                                        <logic:notEqual name="residuo" value="0">
                                                                            <bean:define id="clase" value="grisClaro10"/>
                                                                        </logic:notEqual>
                                                                        <logic:equal name="ec.com.smx.sic.sispe.anulacion.numPedido" value="${indice}">
                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.anular">
                                                                                <bean:define id="clase" value="celesteClaro10"/>
                                                                            </logic:notEmpty>
                                                                        </logic:equal>
                                                                        <%--------------------------------------------------------------------%>
                                                                        <tr class="${clase}">
                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.estado.anulado">
                                                                                <bean:define name="ec.com.smx.sic.sispe.estado.anulado" id="anulado"/>
                                                                            </logic:notEmpty>
                                                                            <td width="8%" align="center" class="columna_contenido fila_contenido">
                                                                                <logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="${anulado}" >									
                                                                                    <html:link href="#" onclick="requestAjax('anulacionPedido.do',['mensajes','seccion_autorizacion','lista_pedidos','divPopUp'],{parameters: 'indice=${indice}', popWait: false, evalScripts: true});">Anular</html:link>
                                                                                </logic:notEqual>
                                                                                <logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="${anulado}" >								
                                                                                    <b>Ninguna</b>
                                                                                </logic:equal>
                                                                            </td>
                                                                            <td width="4%" align="center" class="columna_contenido fila_contenido"><bean:write name="fila"/></td>
                                                                            <logic:notEqual name="vistaPedidoDTO" property="codigoTipoPedido" value="${tipoPedidoNormal}">
                                                                                <td width="15%" align="center" class="columna_contenido fila_contenido">
                                                                                    <html:link title="Detalle del pedido" onclick="popWait();" action="detalleEstadoPedidoEspecial.do?detallePedido=${indice}"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                                </td>
                                                                            </logic:notEqual>
                                                                            <logic:equal name="vistaPedidoDTO" property="codigoTipoPedido" value="${tipoPedidoNormal}">
                                                                                <td width="15%" align="center" class="columna_contenido fila_contenido">
                                                                                    <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                                </td>
                                                                            </logic:equal>
                                                                            <td width="10%" align="center" class="columna_contenido fila_contenido">
                                                                                <logic:notEmpty name="vistaPedidoDTO" property="llaveContratoPOS">
                                                                                    <bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/>
                                                                                </logic:notEmpty>
                                                                                <logic:empty name="vistaPedidoDTO" property="llaveContratoPOS">&nbsp;</logic:empty>
                                                                            </td>
                                                                            <td width="30%" align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                                            <td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
                                                                            <td width="12%" align="center" class="columna_contenido fila_contenido"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
                                                                            <td width="10%" align="right" class="columna_contenido fila_contenido"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
                                                                        </tr>
                                                                    </logic:iterate>
                                                                </table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </logic:notEmpty>
                                            <logic:empty name="anulacionesForm" property="datos">
                                                <table cellpadding="0" cellspacing="0" width="100%" align="left">
                                                    <tr><td>Seleccione un criterio de b&uacute;squeda.</td></tr>
                                                </table>
                                            </logic:empty>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>	
                    </tr>	
                </table>
            </td>
        </tr>
	</table>
</html:form>		
<tiles:insert page="/include/bottom.jsp"/>