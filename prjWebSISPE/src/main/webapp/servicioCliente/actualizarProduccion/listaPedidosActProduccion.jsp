<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<% String sessionId = session.getId(); %>
<bean:define id="idSesion" value="<%=sessionId%>"/>
<html:hidden property="codigoLocal"/>
<%--<html:hidden property="campoBusqueda"/>--%>
<html:hidden property="etiquetaFechas"/>
<html:hidden property="fechaInicial"/>
<html:hidden property="fechaFinal"/>
<%--<html:hidden property="opcionCampoBusqueda"/>--%>
<html:hidden property="opcionFecha"/>
<html:hidden property="opDespachoPendiente"/>
<html:hidden property="numeroMeses"/>

<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="99%" align="center" bgcolor="white">
    <%--Titulo de los datos--%>
    <tr>
        <td class="fila_titulo" colspan="6">
            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                <tr>
                    <td><img src="images/detalle_pedidos24.gif" border="0"/></td>
                    <td height="23px" width="100%">&nbsp;Lista de Pedidios</td>
                    <td align="left">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td colspan="6" height="8px"></td></tr>
    <tr>
        <td>
            <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                <logic:notEmpty name="controlProduccionForm" property="datos">
                	<tr>
	                    <td colspan="7">
	                        <table cellpadding="0" cellspacing="0" width="100%">
	                            <tr>
	                                <td align="left" class="textoRojo10" id="ordenarPor">
	                                    <logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
	                                        <bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
	                                        <b>Ordenado Por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
	                                    </logic:notEmpty>
	                                </td>
	                                <td align="right">
	                                    <smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" campos="false" url="actualizarProduccion.do"/>
	                                </td>
	                            </tr>
	                        </table>
	                    </td>
                    </tr>
                    <tr>
                        <td colspan="7">
                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                <tr class="tituloTablas"  align="left">
                                    <td rowspan="2" class="columna_contenido" width="3%" align="center"><input type="checkbox" onclick="activarDesactivarTodo(this,controlProduccionForm.opSeleccionPedidos);"></td>
                                    <td rowspan="2" class="columna_contenido" width="4%" align="center">No</td>
                                    <td rowspan="2" class="columna_contenido" width="13%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de pedido" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=0'});">No PEDIDO</a></td>
                                    <td rowspan="2" class="columna_contenido" width="7%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de reservaci&oacute;n" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=1'});">No RES.</a></td>
                                    <td rowspan="2" class="columna_contenido" width="30%" align="center"><a class="linkBlanco9" title="Ordenar por nombre del cliente" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=2'});">CONTACTO EMPRESA</a></td>
                                    <td colspan="3" class="fila_contenido columna_contenido" width="22%" align="center">PRODUCCION</td>
                                    <td rowspan="2" class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" title="Ordenar por porcentaje producido" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=6'});">% PRODUCIDO</a></td>
                                    <%--<td rowspan="2" class="columna_contenido" width="12%" align="center">ESTADO</td>--%>
                                    <td rowspan="2" class="columna_contenido" width="13%" align="center"><a class="linkBlanco9" title="Ordenar por fecha de despacho" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=7'});">PRIMERA FECHA DESPACHO</a></td>
                                </tr>
                                <tr class="tituloTablas"  align="left">
                                    <td class="columna_contenido" width="7%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad total reservada" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=3'});">TOTAL</a></td>
                                    <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad producida" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=4'});">PRODUCIDA</a></td>
                                    <td class="columna_contenido" width="7%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad pendiente" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=5'});">PENDIENTE</a></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="div_listado" style="width:100%;height:375px;overflow:auto">
                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                    <logic:iterate name="controlProduccionForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
                                        <bean:define id="indiceGlobal" value="${indicePedido + controlProduccionForm.start}"/>
                                        <bean:define id="numFila" value="${indiceGlobal + 1}"/>
                                        <%--------- control del estilo para el color de las filas --------------%>
                                        <bean:define id="residuo" value="${indicePedido % 2}"/>
                                        <%--------- control del estilo para el color de las filas --------------%>
                                        <logic:equal name="residuo" value="0">
                                            <bean:define id="clase" value="blanco10"/>
                                        </logic:equal>	
                                        <logic:notEqual name="residuo" value="0">
                                            <bean:define id="clase" value="grisClaro10"/>
                                        </logic:notEqual>
                                        <%--------------------------------------------------------------------%>
                                        <tr class="${clase}">
                                            <td class="fila_contenido columna_contenido" width="3%" align="center">
                                                <logic:notEmpty name="ec.com.smx.sic.sispe.afirmacion">
                                                    <bean:define name="ec.com.smx.sic.sispe.afirmacion" id="afirmacion"/>
                                                </logic:notEmpty>
                                                <html:multibox name="controlProduccionForm" property="opSeleccionPedidos">
                                                    <bean:write name="indiceGlobal"/>
                                                </html:multibox>
                                            </td>
                                            <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
                                            <td class="columna_contenido fila_contenido" width="13%" align="center">
                                                <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indiceGlobal" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                <%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indiceGlobal}','WESTPED_${idSesion}')"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link></td>--%>
                                            <td class="columna_contenido fila_contenido" width="7%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
                                            <td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                            <td class="columna_contenido fila_contenido" width="7%" align="center"><bean:write name="vistaPedidoDTO" property="cantidadReservadaEstado"/></td>
                                            <td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="cantidadParcialEstado"/></td>	    
                                            <td class="columna_contenido fila_contenido" width="7%" align="center"><bean:write name="vistaPedidoDTO" property="diferenciaCantidadEstado"/></td>
                                            <td class="columna_contenido fila_contenido" width="9%" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.porcentajeProducido}"/>%</td>
                                            <%--<td class="columna_contenido fila_contenido" width="12%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>--%>
                                            <td class="columna_contenido fila_contenido columna_contenido_der" width="13%" align="center" title="Primera fecha de despacho al local"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>					    
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
</table>