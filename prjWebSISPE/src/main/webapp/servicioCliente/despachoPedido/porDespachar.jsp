<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="tabPedido"><bean:message key="ec.com.smx.sic.sispe.tab.pedidos"/></bean:define>
<bean:define id="tabDetPedido"><bean:message key="ec.com.smx.sic.sispe.tab.detallePedido"/></bean:define>
<bean:define id="opTodos"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<bean:define name="sispe.estado.inactivo" id="estadoInactivo"/>
<bean:define name="ec.com.smx.sic.sispe.pedido.estadoDespachado" id="estadoDespachado"/>
<bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
<%--<bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>--%>

<bean:define id="estadoPagoTotal" name="ec.com.smx.sic.sispe.pedido.estadoPagado"/>
<bean:define id="estadoValidacionPago" name="ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"/>

<% String sessionId = session.getId(); %>
<bean:define id="idSesion" value="<%=sessionId%>"/>

<div id="resultadosBusqueda">
    <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="99%" align="center" bgcolor="white">
        <logic:equal name="ec.com.smx.sic.sispe.seccionPagina" value="${tabPedido}">
            <%--Titulo de los datos--%>
            <tr>
                <td class="fila_titulo" colspan="6">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                        <tr>
                            <td width="3%">
                                &nbsp;<img src="images/pedidos24.gif" border="0"/>
                            </td>
                            <td width="85%" align="left">
                                <b>&nbsp;Lista de pedidos:</b>
                            </td>
                            <td width="10%" align="left">
                                <div id="botonD">	
                                    <html:link styleClass="despachoD" href="#" onclick="requestAjax('despachoPedido.do',['mensajes','resultadosBusqueda','pregunta'],{parameters: 'botonDespachar=ok'});">Despachar</html:link>
                                </div>												
                            </td>
                            <td width="2%"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </logic:equal>
        <logic:notEqual name="ec.com.smx.sic.sispe.seccionPagina" value="${tabPedido}">
            <%--Titulo de los datos--%>
            <tr>
                <td class="fila_titulo" colspan="6">
                    <table cellpadding="1" cellspacing="0" border="0" width="100%" align="center">
                        <tr>
                            <td width="3%">
                                &nbsp;<img src="images/detalle_pedidos24.gif" border="0"/>
                            </td>
                            <td width="20%" align="left">
                                <b>Detalle del pedido:</b>
                            </td>
                            <td width="40%" align="center" class="textoRojo10">&nbsp;</td>
                            <td align="rigth" width="10%">
                                <logic:equal name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                    <div id="botonD">	
                                        <html:link styleClass="aceptarD" href="#" title="Tomar en cuenta art&iacute;culos seleccionados y volver al listado de pedidos" onclick="requestAjax('despachoPedido.do',['resultadosBusqueda','pregunta'],{parameters: 'botonAceptarSeleccion=ok'});">Aceptar</html:link>
                                    </div>
                                </logic:equal>
                                <logic:notEqual name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                    <logic:equal name="estadoValidacionPago" value="${estadoActivo}">
                                        <div id="botonDesactivadoD">	
                                            <html:link styleClass="aceptarDesactivadoD" href="#" title="No puede entregar art&iacute;culos de este pedido hasta que el pedido no sea facturado">Aceptar</html:link>
                                        </div>
                                    </logic:equal>
                                    <logic:notEqual name="estadoValidacionPago" value="${estadoActivo}">
                                        <div id="botonD">	
                                            <html:link styleClass="aceptarD" href="#" title="Tomar en cuenta art&iacute;culos seleccionados y volver al listado de pedidos" onclick="requestAjax('despachoPedido.do',['resultadosBusqueda','pregunta'],{parameters: 'botonAceptarSeleccion=ok'});">Aceptar</html:link>
                                        </div>
                                    </logic:notEqual>
                                </logic:notEqual>
                            </td>
                            <td align="right" width="10%">
                                <div id="botonD">
                                    <html:link styleClass="atrasD" href="#" title="Cancelar la selecci&oacute;n de art&iacute;culos y volver al listado de pedidos" onclick="requestAjax('despachoPedido.do',['resultadosBusqueda','mensajes','pregunta','seccion_detalle'],{parameters: 'botonCancelarSeleccion=ok'});">Volver</html:link>
                                </div>						
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </logic:notEqual>
        <tr><td colspan="6" height="8"></td></tr>
        <tr>
            <td>
                <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                    <logic:equal name="ec.com.smx.sic.sispe.seccionPagina" value="${tabPedido}">
                        <logic:notEmpty name="despachosEntregasForm" property="datos">
                            <tr>
                                <td align="right" colspan="6">
                                    <smx:paginacion start="${despachosEntregasForm.start}" range="${despachosEntregasForm.range}" results="${despachosEntregasForm.size}" styleClass="textoNegro10" campos="false" url="despachoPedido.do"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                        <tr class="tituloTablas"  align="left">
                                            <td class="columna_contenido fila_contenido" width="3%" align="center">&nbsp;</td>
                                            <td class="columna_contenido fila_contenido" width="3%" align="center">No</td>
                                            <td class="columna_contenido fila_contenido" width="15%" align="center">No pedido</td>
                                            <td class="columna_contenido fila_contenido" width="8%" align="center">No res.</td>
                                            <td class="columna_contenido fila_contenido" width="39%" align="center">Contacto - Empresa</td>
                                            <td class="columna_contenido fila_contenido" width="10%" align="center">% abonado</td>
                                            <td class="columna_contenido fila_contenido" width="15%" align="center" title="Primera fecha de despacho al local">Primera fecha despacho</td>
                                            <td class="columna_contenido fila_contenido" width="7%" align="center">&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div style="width:100%;height:375px;overflow:auto">
                                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                            <logic:iterate name="despachosEntregasForm" property="datos" id="vistaPedidoDTO" indexId="indicePedido">
                                                <%--------- control del estilo para el color de las filas --------------%>
                                                <bean:define id="indiceGlobal" value="${indicePedido + despachosEntregasForm.start}"/>                                      
                                                <bean:define id="residuo" value="${indicePedido % 2}"/>
                                                <logic:equal name="residuo" value="0">
                                                    <bean:define id="clase" value="blanco10"/>
                                                </logic:equal>
                                                <logic:notEqual name="residuo" value="0">
                                                    <bean:define id="clase" value="grisClaro10"/>
                                                </logic:notEqual>
                                                <bean:define id="fila" value="${indiceGlobal + 1}"/>
                                                <%--- control de iconos cuando el pedido esta en estado despachado ----%>
                                                <logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoDespachado}">
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <bean:define id="clase" value="verdeClaro10"/>
                                                        <bean:define id="icono" value="./images/exito_16.gif"/>
                                                        <bean:define id="titulo" value="Todos los art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>
                                                    <logic:notEqual name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <bean:define id="icono" value="./images/advertencia_16.gif"/>
                                                        <bean:define id="clase" value="amarilloClaro10"/>
                                                        <bean:define id="titulo" value="Solo algunos art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:notEqual>
                                                </logic:equal>
                                                <%--- control de iconos cuando el pedido no est&aacute; en estado despachado ----%>
                                                <logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoDespachado}">		
                                                    <logic:empty name="vistaPedidoDTO" property="npEstadoVistaPedido">
                                                        <bean:define id="clase" value="rojoClaro10"/>
                                                        <bean:define id="icono" value="./images/error_small.gif"/>
                                                        <bean:define id="titulo" value="Ning&uacute;n art&iacute;culo de este pedido se ha seleccionado para el despacho"/>
                                                    </logic:empty>
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoInactivo}">
                                                        <bean:define id="icono" value="./images/advertencia_16.gif"/>
                                                        <bean:define id="clase" value="amarilloClaro10"/>
                                                        <bean:define id="titulo" value="Solo algunos art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <bean:define id="clase" value="verdeClaro10"/>
                                                        <bean:define id="icono" value="./images/exito_16.gif"/>
                                                        <bean:define id="titulo" value="Todos los art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>										
                                                </logic:notEqual>
                                                <%--------------------------------------------------------------------%>
                                                <tr class="${clase}" title="${titulo}">
                                                    <td class="columna_contenido fila_contenido" width="3%" align="center"><img src="${icono}" border="0"></td>
                                                    <td class="columna_contenido fila_contenido" width="3%" align="center">${fila}</td>
                                                    <td class="columna_contenido fila_contenido" width="15%" align="center">
                                                        <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indiceGlobal" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                        <%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indiceGlobal}','WESTPED_${idSesion}')"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>--%>
                                                    </td>
                                                    <td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
                                                    <td class="columna_contenido fila_contenido" width="39%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                    <td class="columna_contenido fila_contenido" width="10%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.porcentajeAbonoPedido}"/>&nbsp;%</td>
                                                    <td class="columna_contenido fila_contenido" width="15%" align="center" title="Primera fecha de despacho al local"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                    <td class="columna_contenido fila_contenido columna_contenido_der" width="7%" align="center">
                                                        <html:link href="#" onclick="requestAjax('despachoPedido.do',['mensajes','resultadosBusqueda','pregunta'],{parameters: 'indicePedido=${indiceGlobal}'});">detalle</html:link>	
                                                    </td>
                                                </tr>
                                            </logic:iterate>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td height="4" colspan="6"></td>
                            </tr>
                        </logic:notEmpty>
                        <logic:empty name="despachosEntregasForm" property="datos">
                            <tr><td>Seleccione una opci&oacute;n de b&uacute;squeda</td></tr>
                        </logic:empty>
                    </logic:equal>	
                    <logic:equal name="ec.com.smx.sic.sispe.seccionPagina" value="${tabDetPedido}">
                        <tr>
                            <td align="left">
                                <table border="0" cellspacing="0" class="textoAzul11">
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedidoSeleccionado">
                                        <tr>
                                            <td align="right"><b>No pedido:</b> </td>
                                            <td align="left" class="textoNegro11"><b><bean:write name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="id.codigoPedido"/></b></td>	
                                        </tr>
                                        <tr>
                                            <td align="right">Contacto-Empresa: </td>
                                            <td align="left" class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="contactoEmpresa"/></td>
                                        </tr>
                                        <tr><td height="5px"></td></tr>
                                        <logic:notEqual name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoTotal}">
                                            <logic:equal name="estadoValidacionPago" value="${estadoActivo}">
                                                <tr><td height="15px" valign="middle" colspan="2" class="textoRojo11"><b>Este pedido no se ha cancelado en su totalidad ning&uacute;n art&iacute;culo puede ser despachado</b></td></tr>
                                            </logic:equal>
                                        </logic:notEqual>
                                    </logic:notEmpty>	
                                </table>
                            </td>	
                        </tr>
                        <tr align="left"><td height="10px"></td></tr>
                        <tr>
                            <td>	
                                <logic:notEmpty name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="vistaDetallesPedidos">
                                    <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                        <tr>
                                            <td>
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <tr class="tituloTablas"  align="left">
                                                        <td class="columna_contenido" width="3%" align="center" rowspan="2">No</td>
                                                        <td class="columna_contenido" width="15%" align="center" rowspan="2">C&Oacute;DIGO BARRAS</td>
                                                        <td class="columna_contenido" width="22%" align="center" rowspan="2">ART&Iacute;CULO</td>
                                                        <td class="columna_contenido" width="9%" align="center" rowspan="2">CANT. RESERVADA</td>
                                                        <%--<td class="columna_contenido fila_contenido" width="24%" align="center" colspan="3">PESOS KG.</td>--%>
                                                        <td class="columna_contenido" width="27%" align="center" title="Fecha despacho de la bodega | cantidad a lugar">DESPACHOS</td>
                                                    </tr>
                                                    <tr class="tituloTablas">
                                                        <%--
                                                        <td class="columna_contenido" width="8%" align="center" title="Reservado">RES.</td>
                                                        <td class="columna_contenido" width="8%" align="center" title="Despachado">DESP.</td>
                                                        <td class="columna_contenido" width="8%" align="center" title="Registrar">REG.</td>
                                                        --%>
                                                        <td class="columna_contenido" align="center" width="27%" title="Fecha despacho de la bodega | cantidad a lugar">
                                                            <table cellpadding="0" cellspacing="0" width="100%">
                                                                <tr>
                                                                    <td align="center" width="13%"><html:checkbox title="Seleccionar todos" property="opSeleccionTodos" value="${opTodos}" onclick="activarDesactivarTodo(this, despachosEntregasForm.opSeleccionAlgunos);"/></td>
                                                                    <td align="center" width="87%">F. DESPACHO | CANT. LUGAR</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:320px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                                    <table cellpadding="1" cellspacing="0" width="100%" border="0">
                                                        <c:set var="indiceDetalle" value="0"/>
                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="vistaDetallesPedidos" id="detallePedido" indexId="numDetalle"> 
                                                            <logic:equal name="detallePedido" property="reservarBodegaSIC" value="${estadoActivo}">
                                                                <%--------- control del estilo para el color de las filas --------------%>
                                                                <bean:define id="residuo" value="${indiceDetalle % 2}"/>
                                                                <bean:define id="fila" value="${indiceDetalle + 1}"/>
                                                                <logic:equal name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="blanco10"/>
                                                                </logic:equal>
                                                                <logic:notEqual name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                                </logic:notEqual>
                                                                <%--------------------------------------------------------------------%>
                                                                <c:set var="pesoVariable" value=""/>
                                                                <logic:equal name="detallePedido" property="tipoArticuloCalculoPrecio" value="${tipoArticuloPavo}">
                                                                    <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                </logic:equal>
                                                                <%--<logic:equal name="detallePedido" property="tipoArticuloCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                    <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                </logic:equal>--%>
                                                                <tr class="${colorBack}"> 
                                                                    <td width="3%" align="center" class="columna_contenido fila_contenido">${fila}</td>
                                                                    <td align="center" width="15%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
                                                                    <td align="center" width="22%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="descripcionArticulo"/></td>
                                                                    <!-- en esta fila se aument� el hidden vectorPesoArticulo porque se coment� la parte de abajo ya que los pesos solo 
                                                                        se deben ingresar para los pavos y estos ya no se despachan desde esta pantalla -->
                                                                    <td align="center" width="9%" class="columna_contenido fila_contenido">
                                                                        <bean:write name="detallePedido" property="cantidadReservarSIC"/>
                                                                        <html:hidden property="vectorPesoArticulo" value="0"/>
                                                                    </td>
                                                                    <%--
                                                                    <td align="center" width="8%" class="columna_contenido fila_contenido textoAzul10" title="Peso Reservado">
                                                                        <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                            <bean:write name="detallePedido" property="pesoArticuloEstadoReservado"/>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="pesoVariable" value="${estadoActivo}">
                                                                            <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                                        </logic:notEqual>                                                                        
                                                                    </td>
                                                                    <td align="center" width="8%" class="columna_contenido fila_contenido textoAzul10" title="Peso Despachado">
                                                                        <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                            <logic:notEmpty name="detallePedido" property="pesoRegistradoBodega">
                                                                                <bean:write name="detallePedido" property="pesoRegistradoBodega"/>
                                                                            </logic:notEmpty>
                                                                            <logic:empty name="detallePedido" property="pesoRegistradoBodega">0.0</logic:empty>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="pesoVariable" value="${estadoActivo}">
                                                                            <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                                        </logic:notEqual>
                                                                    </td>
                                                                    <td align="center" width="8%" class="columna_contenido fila_contenido textoAzul10" title="Registrar Peso">
                                                                        <logic:equal name="pesoVariable" value="${estadoActivo}">
                                                                            <html:text property="vectorPesoArticulo" styleClass="textNormal" value="${detallePedido.npPesoDespacho}" maxlength="10" size="6"/>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="pesoVariable" value="${estadoActivo}">
                                                                            <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                                            <html:hidden property="vectorPesoArticulo" value="0"/>
                                                                        </logic:notEqual>
                                                                    </td>
                                                                    --%>
                                                                    <td align="center" width="27%" class="columna_contenido fila_contenido columna_contenido_der">
                                                                        <%------------------------ si la colecci&oacute;n de entregas no est&aacute; vacia -----------------------%>
                                                                        <table cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion">
                                                                            <logic:notEmpty name="detallePedido" property="entregaDetallePedidoCol">
                                                                                <c:set var="indiceEntrega" value="0"/>
                                                                                <logic:iterate name="detallePedido" property="entregaDetallePedidoCol" id="entrega" indexId="numEntrega">
                                                                                    <logic:greaterThan name="entrega" property="cantidadDespacho" value="${0}">
                                                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                                                        <bean:define id="residuoE" value="${indiceEntrega % 2}"/>
                                                                                        <c:set value="" var="titulo"/>
                                                                                        <logic:equal name="residuoE" value="0">
                                                                                            <bean:define id="claseE" value="blanco10"/>
                                                                                        </logic:equal>	
                                                                                        <logic:notEqual name="residuoE" value="0">
                                                                                            <bean:define id="claseE" value="celesteClaro10"/>
                                                                                        </logic:notEqual>
                                                                                        <logic:notEmpty name="entrega" property="fechaRegistroDespacho">
                                                                                            <bean:define id="fechaD">
                                                                                                <bean:write name="entrega" property="fechaRegistroDespacho" formatKey="formatos.fecha"/>
                                                                                            </bean:define>
                                                                                            <bean:define id="claseE" value="verdeClaro10"/>
                                                                                            <c:set var="titulo" value="Este art&iacute;culo ya fu&eacute; despachado el ${fechaD}"/>
                                                                                        </logic:notEmpty>
                                                                                        <%--------------------------------------------------------------------%>
                                                                                        <tr class="${claseE}" title="${titulo}">
                                                                                            <td align="left" class="columna_contenido fila_contenido" width="7%">
                                                                                                <bean:define value="${numDetalle}-${numEntrega}" id="indiceDespacho"/>
                                                                                                <logic:empty name="entrega" property="fechaRegistroDespacho"> 
                                                                                                    <html:multibox title="Fecha despacho al cliente | cantidad a despachar" property="opSeleccionAlgunos" value="${indiceDespacho}"/>&nbsp;
                                                                                                </logic:empty>	
                                                                                                <logic:notEmpty name="entrega" property="fechaRegistroDespacho">
                                                                                                    <img src="images/exito_16.gif">
                                                                                                </logic:notEmpty>
                                                                                            </td>
                                                                                            <td class="fila_contenido" width="83%" align="left">
                                                                                                <b><label class="textoAzul10"><bean:write name="entrega" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/></label>|&nbsp;<bean:write name="entrega" property="cantidadDespacho"/></b>&nbsp;a&nbsp;<label class="textoRojo10"><bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/></label>
                                                                                            </td>
																							<logic:empty name="entrega" property="fechaRegistroDespacho">
																								<bean:define id="tipoEntregaLocal"><bean:message key="ec.com.smx.sic.sispe.entrega.local1"/></bean:define>
																								<bean:define id="contextoOtroLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.otroLocal"/></bean:define>
																								<bean:define id="contextoAMiLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.miLocal"/></bean:define>
																								<c:if test="${entrega.entregaPedidoDTO.tipoEntrega == tipoEntregaLocal && (entrega.entregaPedidoDTO.codigoContextoEntrega == contextoOtroLocal || entrega.entregaPedidoDTO.codigoContextoEntrega == contextoAMiLocal)}">
																									<td class="fila_contenido" align="left" width="10%"><a href="#" onclick="requestAjax('despachoPedido.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'subirArchivoFoto=${detallePedido.id.codigoPedido}&&codigoLocal=${detallePedido.id.codigoAreaTrabajo}&&codigoLocalEntrega=${entrega.entregaPedidoDTO.codigoAreaTrabajoEntrega}&&codigoArticulo=${detallePedido.id.codigoArticulo}&&codigoEstado=${entrega.entregaPedidoDTO.codigoEstado}&&secuencialEstadoPedido=${entrega.entregaPedidoDTO.secuencialEstadoPedido}&&secuencialEntrega=${entrega.codigoEntregaPedido}&&codigoDetalleEntregaPedido=${entrega.id.codigoDetalleEntregaPedido}',evalScripts: true, popWait:false});"><img src="./images/subirFoto.gif" border="0" alt="adjuntar archivo foto"></a></td>
																								</c:if>
																								<c:if test="${entrega.entregaPedidoDTO.tipoEntrega != tipoEntregaLocal}">
																									<td class="fila_contenido" align="center" width="10%">-</td>
																								</c:if>
																							</logic:empty>
																							<logic:notEmpty name="entrega" property="fechaRegistroDespacho">
																								<td class="fila_contenido" align="center" width="10%">-</td>
																							</logic:notEmpty>
                                                                                        </tr>
                                                                                        <c:set var="indiceEntrega" value="${indiceEntrega + 1}"/>
                                                                                    </logic:greaterThan>
                                                                                </logic:iterate>
                                                                            </logic:notEmpty>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <c:set var="indiceDetalle" value="${indiceDetalle + 1}"/>                                                                
                                                            </logic:equal>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>	
                                </logic:notEmpty>
                            </td>
                        </tr>	
                    </logic:equal>
                </table>
            </td>
        </tr>
    </table>
</div>