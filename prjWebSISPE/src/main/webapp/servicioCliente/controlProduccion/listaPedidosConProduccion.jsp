<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<logic:notEmpty name="sispe.estado.activo">
    <bean:define id="estadoActivo"><bean:write name="sispe.estado.activo"/></bean:define>
</logic:notEmpty>

<table border="0" class="textoNegro11" width="99%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <!--Barra Izquierda-->
        <td class="datos" width="25%">
            
                <table cellpadding="0" cellspacing="0" width="100%">
                    <tr>
                        <td>
                            <!----- se inserta la secci&oacute;n de b&uacute;squeda ------>
                            <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                        </td>
                    </tr>
                    <tr><td height="6px"></td></tr>
                    <tr>
                        <td>
                            <!--Separaci&oacute;n-->
                            <table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                                <!-- Informaci&oacute;n-->
                                <tr>
                                    <td class="fila_titulo" colspan="2">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td width="15%"><img src="images/datos_informacion24.gif" border="0"/></td>
                                                <td width="85%">Informaci&oacute;n</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" colspan="2">
                                        <table border="0" cellspacing="3" align="left">
                                            <tr>
                                                <td title="Pedidos que ya deben ser puestos en producci&oacute;n debido a la cercan&iacute;a de la fecha de despacho">
                                                    <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0">
                                                        <tr>
                                                            <td class="naranjaClaro10" height="5" width="20">&nbsp;</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td align="left" title="Pedidos que ya deben ser puestos en producci&oacute;n debido a la cercan&iacute;a de la fecha de despacho">Pr&oacute;ximos a entregarse</td>			
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            
        </td>
        <!--Fin Barra Izquierda-->
        <!-- Separador -->
        <TD class="datos" width="1%">&nbsp;</TD>
        <!--Datos-->
        <TD class="datos" width="82%" bgcolor="white">
            <div id="resultadosBusqueda" style="height:455px;border:1px solid #cccccc">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                    <%--Titulo de los datos--%>
                    <tr>
                        <td class="fila_titulo" colspan="7" height="26px">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td><img src="images/detalle_pedidos24.gif" border="0"/></td>
                                    <td height="23" width="100%">&nbsp;
                                        Lista de Pedidos
                                    </td>
                                    <td align="left">
                                        <logic:notEmpty name="controlProduccionForm" property="datos">
                                        	<div id="botonD">	
                                            	<html:link styleClass="producirD" href="#" title="Empezar producci&oacute;n" onclick="requestAjax('controlProduccion.do',['resultadosBusqueda','mensajes'],{parameters: 'botonProducir=ok'});">Producir</html:link>
                                            </div>
                                        </logic:notEmpty>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="7" height="8"></td></tr>
                    <tr>
                        <td>
                            <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                                <logic:notEmpty name="controlProduccionForm" property="datos">
                                    <tr>
                                        <td>
                                            <table cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td align="left" class="textoRojo10" id="ordenarPor">
                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
                                                            <bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
                                                            <b>Ordenado Por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
                                                        </logic:notEmpty>
                                                    </td>
                                                    <td align="right">
                                                        <smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" campos="false" url="controlProduccion.do"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="7">
                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                <bean:define name="ec.com.smx.sic.sispe.pedido.estadoReservado" id="estadoReservado"/>
                                                <bean:define id="estadoReservadoConfirmado"><bean:message key="estado.pedidoReservado.confirmado"/></bean:define>
                                                <tr class="tituloTablas"  align="left">
                                                    <td rowspan="2" class="columna_contenido" width="3%" align="center"><input type="checkbox" onclick="activarDesactivarTodo(this,controlProduccionForm.opEscogerProducir);"></td>
                                                    <td rowspan="2" class="columna_contenido" width="3%" align="center">No</td>
                                                    <td rowspan="2" class="columna_contenido" width="15%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de pedido" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=0'});">No Pedido</a></td>
                                                    <td rowspan="2" class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo de reservaci&oacute;n" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=1'});">No Res.</a></td>
                                                    <td rowspan="2" class="columna_contenido" width="22%" align="center"><a class="linkBlanco9" href="#" title="Ordenar por nombre del cliente" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=2'});">Contacto Empresa</a></td>
                                                    <td colspan="3" class="fila_contenido columna_contenido" width="28%" align="center">PRODUCCION</td>
                                                    <td rowspan="2" class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" href="#" title="Ordenar por porcentaje producido" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=6'});">% Producido</a></td>
                                                    <!--<td rowspan="2" class="columna_contenido" width="12%" align="center"><a class="linkBlanco9" href="#" title="ordenar por estado" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=7'});">ESTADO</a></td>-->
                                                    <td rowspan="2" class="columna_contenido" width="11%" align="center" title="Primera fecha de despacho al local"><a class="linkBlanco9" href="#" title="Ordenar por fecha de despacho" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=8'});">Primera Fecha Despacho</a></td>
                                                </tr>
                                                <tr class="tituloTablas"  align="left">
                                                    <td class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad reservada" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=3'});">Total</a></td>
                                                    <td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad producida" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=4'});">Producida</a></td>
                                                    <td class="columna_contenido" width="9%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad pendiente" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarP=5'});">Pendiente</a></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div id="div_listado" style="width:100%;height:370px;overflow:auto;">
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <logic:iterate name="controlProduccionForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
                                                        <bean:define id="indice" value="${indicePedido + controlProduccionForm.start}"/>
                                                        <bean:define id="numFila" value="${indice + 1}"/>
                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                        <bean:define id="residuo" value="${indicePedido % 2}"/>
                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                        <logic:equal name="vistaPedidoDTO" property="pedidoPorEntregar" value="${estadoActivo}">
                                                            <bean:define id="clase" value="naranjaClaro10"/>
                                                            <bean:define id="color" value="#ffedd2"/>
                                                        </logic:equal>
                                                        <logic:notEqual name="vistaPedidoDTO" property="pedidoPorEntregar" value="${estadoActivo}">
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="clase" value="blanco10"/>
                                                                <bean:define id="color" value="#ffffff"/>
                                                                <bean:define id="titulo" value=""/>
                                                            </logic:equal>	
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="clase" value="grisClaro10"/>
                                                                <bean:define id="color" value="#EBEBEB"/>
                                                                <bean:define id="titulo" value="Pedidos que ya deben ser producidos debido a la cercan&iacute;a de la fecha de despacho"/>
                                                            </logic:notEqual>
                                                        </logic:notEqual>
                                                        <%--------------------------------------------------------------------%>
                                                        <tr class="${clase}" title="${titulo}">
                                                            <td class="fila_contenido columna_contenido" width="3%">
                                                                <logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoReservado}">                                                                    
                                                                    <logic:equal name="vistaPedidoDTO" property="estadoPedidoReserva" value="${estadoReservadoConfirmado}">
	                                                                    <html:multibox name="controlProduccionForm" property="opEscogerProducir">
	                                                                        <bean:write name="indice"/>
	                                                                    </html:multibox>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="vistaPedidoDTO" property="estadoPedidoReserva" value="${estadoReservadoConfirmado}">
                                                                    	&nbsp;
                                                                    </logic:notEqual>
                                                                    <%--html:multibox name="controlProduccionForm" property="opEscogerProducir">
                                                                        <bean:write name="indice"/>
                                                                    </html:multibox--%>
                                                                </logic:equal>
                                                                <logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoReservado}">
                                                                    &nbsp;
                                                                </logic:notEqual>
                                                            </td>
                                                            <td class="columna_contenido fila_contenido" width="3%" align="center"><bean:write name="numFila"/></td>
                                                            <td class="columna_contenido fila_contenido" width="15%" align="center">
                                                                <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                <%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indice}','WESTPED_${idSesion}')"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link></td>--%>
                                                            <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
                                                            <td class="columna_contenido fila_contenido" width="22%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                            <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaPedidoDTO" property="cantidadReservadaEstado"/></td>
                                                            <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaPedidoDTO" property="cantidadParcialEstado"/></td> 					    
                                                            <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaPedidoDTO" property="diferenciaCantidadEstado"/></td>
                                                            <td class="columna_contenido fila_contenido" width="9%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.porcentajeProducido}"/>%</td>
                                                            <%--<td class="columna_contenido fila_contenido" width="12%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>--%>
                                                            <td class="columna_contenido fila_contenido columna_contenido_der" width="11%" align="center" title="Primera fecha de despacho al local"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>						    
                                                        </tr>
                                                    </logic:iterate>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                </logic:notEmpty>
                                <logic:empty name="controlProduccionForm" property="datos">
                                    <tr>
                                        <td colspan="7">
                                            Seleccione un criterio de b&uacute;squeda
                                        </td>
                                    </tr>
                                </logic:empty>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </TD>
        <%--Fin Datos--%>
    </tr>
</table>
<%--<script language="JavaScript" type="text/javascript">
    Field.activate('fechaInicial');
</script>--%>