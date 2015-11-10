<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- tabDespacho es llamdado desde listaControlProduccionPedido.jsp -->
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>

<div>
    <table class="tabla_informacion textoNegro12" border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
        <%--Titulo de los datos--%>
        <tr>
            <td class="fila_titulo" colspan="6" align="right">
                <table cellpadding="1" cellspacing="0" border="0" width="100%">
                    <logic:empty name="ec.com.smx.sic.sispe.info.cerrarDia">
                        <tr>
                            <td width="3%"><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                            <td height="23" class="textoNegro10" align="left">&nbsp;Pedidos especiales pendientes de despacho</td>
                            <td height="23" align="right">
                                <div id="botonDsg">
                                    <html:link styleClass="despachoDsg" href="#" title="Habilitar despacho" onclick="requestAjax('controlProduccionPedEsp.do',['resultadosBusqueda','mensajes','pregunta'],{parameters: 'cerrarPendiente=ok', evalScripts: true});">Habilitar despacho</html:link>
                                </div>
                            </td>
                        </tr>
                    </logic:empty>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.info.cerrarDia">
                        <tr>
                            <td width="3%" align="center"><img src="images/info_16.gif"></td>
                            <td height="23" class="textoNegro10" align="left"><b>Esta opción no está habilitada porque el día no se ha cerrado.</b></td>
                        </tr>
                    </logic:notEmpty>
                </table>
            </td>
        </tr>
        
        <logic:empty name="ec.com.smx.sic.sispe.info.cerrarDia">
            <tr><td height="5px"></td></tr>
            <tr>
                <td align="center">
                    <div id="resultadosBusqueda" style="width:100%;height:440px;overflow-y:hidden;overflow-x:hidden;">
                        <table border="0" class="textoNegro11" width="99%" cellspacing="0" cellpadding="0">
                            <logic:notEmpty name="ec.com.smx.sic.sispe.despachoPedEsp.pedidosPendientes">
                                <tr>
                                    <td>
                                        <table border="0" cellspacing="0" class="tabla_informacion" align="center" width="100%" cellpadding="1">	
                                            <tr class="tituloTablas">
                                                <td width="3%" align="center">
                                                    <html:checkbox title="Seleccionar todos los pedidos" property="checkSeleccionarTodo" value="t" onclick="activarDesactivarTodo(this,controlProduccionPedidoEspecialForm.checksSeleccionar);"></html:checkbox>
                                                </td>
                                                <td class="columna_contenido" width="3%" align="center">No</td>
                                                <td class="columna_contenido" width="15%"align="center">Local</td>
                                                <td class="columna_contenido" width="13%"align="center">No pedido</td>
                                                <td class="columna_contenido" width="25%" align="center">Contacto - Empresa</td>
                                                <td class="columna_contenido" width="9%"align="center">Fecha despacho</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="div_listado" style="width:100%;height:385px;overflow-y:auto;overflow-x:auto;">
                                            <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="1">
                                                <logic:iterate name="ec.com.smx.sic.sispe.despachoPedEsp.pedidosPendientes" id="vistaPedidoDTO" indexId="numeroRegistro">
                                                    <%--------- control del estilo para el color de las filas -----------%>
                                                    <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                    <bean:define id="fila" value="${numeroRegistro + 1}"/>
                                                    <logic:equal name="residuo" value="0">
                                                        <bean:define id="colorBack" value="blanco10"/>
                                                    </logic:equal>
                                                    <logic:notEqual name="residuo" value="0">
                                                        <bean:define id="colorBack" value="grisClaro10"/>
                                                    </logic:notEqual>
                                                    
                                                    <logic:notEmpty name="vistaPedidoDTO" property="npEstadoError">
                                                        <bean:define id="colorBack" value="rojoObsuro10"/>
                                                    </logic:notEmpty> 
                                                    <%------------------------------------------------------------------%>
                                                    <tr class="${colorBack}"> 
                                                        <td class="columna_contenido fila_contenido_sup" align="center" width="3%">
                                                            <html:multibox property="checksSeleccionar"  value="${numeroRegistro}" title="${numeroRegistro+1}"/>
                                                        </td>
                                                        <td class="columna_contenido fila_contenido_sup" align="center" width="3%">${fila}</td>
                                                        <td class="columna_contenido fila_contenido_sup" align="left" width="15%" ><bean:write name="vistaPedidoDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaPedidoDTO" property="nombreLocal"/></td>
                                                        <td class="columna_contenido fila_contenido_sup" align="center" width="13%" >
                                                            <html:link title="Detalle del pedido" onclick="popWait();" action="detalleEstadoPedidoEspecial.do?detallePedido=${numeroRegistro}"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                        </td>
                                                        <td class="columna_contenido fila_contenido_sup" width="25%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>	
                                                        <td class="columna_contenido fila_contenido_sup " width="9%" align="center"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                    </tr>
                                                </logic:iterate> 
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </logic:notEmpty>
                            <logic:empty name="ec.com.smx.sic.sispe.pedidos">
                                <tr>
                                    <td class="textoNegro11" align="left">
                                        Seleccione un criterio de b&uacute;squeda
                                    </td>
                                </tr>
                            </logic:empty>
                        </table>
                    </div>
                </td>
            </tr>
        </logic:empty>
    </table>
</div>