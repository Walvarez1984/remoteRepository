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
<bean:define name="ec.com.smx.sic.sispe.pedido.estadoEntregado" id="estadoEntregado"/>
<bean:define id="estadoPagoTotal" name="ec.com.smx.sic.sispe.pedido.estadoPagado"/>
<bean:define id="estadoPagoLiquidado" name="ec.com.smx.sic.sispe.pedido.estadoPagadoLiquidado"/>
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
    <bean:define id="tipoEntidadResponsable"><bean:write name="sispe.vistaEntidadResponsableDTO" property="tipoEntidadResponsable"/></bean:define>
</logic:notEmpty>
<bean:define id="entidadResponsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>

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
                                <b>&nbsp;Lista de pedidos por entregar</b>
                            </td>
                            <td width="10%" align="left">
                                <div id="botonD">
                                    <html:link styleClass="entregarD" href="#" onclick="requestAjax('entregaPedido.do',['mensajes','resultadosBusqueda'],{parameters: 'botonEntregar=ok'});">&nbsp;Entregar</html:link>
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
                    <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                        <tr>
                            <td width="3%">
                                &nbsp;<img src="images/detalle_pedidos24.gif" border="0"/>
                            </td>
                            <td width="20%" align="left">
                                <b>Detalle del pedido</b>
                            </td>
                            <td width="40%" align="center" class="textoRojo10">&nbsp;</td>
                            <td width="10%" align="left">
                                <logic:equal name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                    <div id="botonD">	
                                        <html:link styleClass="aceptarD" href="#" title="Tomar en cuenta art&iacute;culos seleccionados y volver al listado de pedidos" onclick="requestAjax('entregaPedido.do',['resultadosBusqueda'],{parameters: 'botonAceptarSeleccion=ok'});">Aceptar</html:link>
                                    </div>
                                </logic:equal>
                                <logic:notEqual name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                    <div id="botonDesactivadoD">	
                                        <html:link styleClass="aceptarDesactivadoD" href="#" title="No puede entregar art&iacute;culos de este pedido hasta que el pedido no sea facturado">Aceptar</html:link>
                                    </div>	
                                </logic:notEqual>
                            </td>
                            <logic:notEmpty name="ec.com.sic.sispe.boton.benenficiario">
	                            <td width="10%" align="left">
								   <div id="botonD">
										<html:link styleClass="archivoBeneficiarioD" href="#" title="Descargar el archivo del beneficiario" onclick="requestAjax('entregaPedido.do', ['mensajes','pregunta','seccion_detalle','resultadosBusqueda'], {parameters: 'aceptarArchBene=ok',evalScripts: true});">Beneficiario</html:link>
								   </div>								
								</td>
							</logic:notEmpty>
                            <td width="10%" align="left">
                                <div id="botonD">
                                    <html:link styleClass="atrasD" href="#" title="Cancelar la selecci&oacute;n de art&iacute;culos y volver al listado de pedidos" onclick="requestAjax('entregaPedido.do',['resultadosBusqueda'],{parameters: 'botonCancelarSeleccion=ok'});">Volver</html:link>
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
                                    <smx:paginacion start="${despachosEntregasForm.start}" range="${despachosEntregasForm.range}" results="${despachosEntregasForm.size}" styleClass="textoNegro10" campos="false" url="entregaPedido.do"/>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="6">
                                    <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                        <tr class="tituloTablas"  align="left">
                                            <td class="columna_contenido fila_contenido" width="3%" align="center">&nbsp;</td>
                                            <td class="columna_contenido fila_contenido" width="4%" align="center">No</td>
                                            <td class="columna_contenido fila_contenido" width="13%" align="center">No pedido</td>
                                            <td class="columna_contenido fila_contenido" width="8%" align="center">No res.</td>
                                            <td class="columna_contenido fila_contenido" width="35%" align="center">Contacto - Empresa</td>
                                            <td class="columna_contenido fila_contenido" width="9%" align="center">Abonado</td>
                                            <td class="columna_contenido fila_contenido" width="9%" align="center">Total</td>
                                            <td class="columna_contenido fila_contenido" width="12%" align="center">Prim. fecha entrega</td>
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
                                                <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                <logic:equal name="residuo" value="0">
                                                    <c:set var="clase" value="blanco10"/>
                                                </logic:equal>
                                                <logic:notEqual name="residuo" value="0">
                                                    <c:set var="clase" value="grisClaro10"/>
                                                </logic:notEqual>
                                                <bean:define id="fila" value="${indiceGlobal + 1}"/>
                                                <%--- control de iconos cuando el pedido esta en estado entregado ----%>
                                                <logic:equal name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoEntregado}">
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <c:set var="clase" value="verdeClaro10"/>
                                                        <bean:define id="icono" value="./images/exito_16.gif"/>
                                                        <bean:define id="titulo" value="Todos los art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>
                                                    <logic:notEqual name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <c:set var="clase" value="amarilloClaro10"/>
                                                        <bean:define id="icono" value="./images/advertencia_16.gif"/>
                                                        <bean:define id="titulo" value="Solo algunos art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:notEqual>
                                                </logic:equal>
                                                <%--- control de iconos cuando el pedido no est&aacute; en estado entregado ----%>
                                                <logic:notEqual name="vistaPedidoDTO" property="id.codigoEstado" value="${estadoEntregado}">	
                                                    <logic:empty name="vistaPedidoDTO" property="npEstadoVistaPedido">
                                                        <c:set var="clase" value="rojoClaro10"/>
                                                        <bean:define id="icono" value="./images/error_small.gif"/>
                                                        <bean:define id="titulo" value="Ning&uacute;n art&iacute;culo de este pedido se ha seleccionado para el despacho"/>
                                                    </logic:empty>
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoInactivo}">
                                                        <bean:define id="icono" value="./images/advertencia_16.gif"/>
                                                        <c:set var="clase" value="amarilloClaro10"/>
                                                        <bean:define id="titulo" value="Solo algunos art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>
                                                    <logic:equal name="vistaPedidoDTO" property="npEstadoVistaPedido" value="${estadoActivo}">
                                                        <c:set var="clase" value="verdeClaro10"/>
                                                        <bean:define id="icono" value="./images/exito_16.gif"/>
                                                        <bean:define id="titulo" value="Todos los art&iacute;culos de este pedido se han seleccionado para el despacho"/>
                                                    </logic:equal>										
                                                </logic:notEqual>
                                                <logic:lessThan name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                                    <c:set var="clase" value="naranjaClaro10"/>
                                                    <bean:define id="icono" value="./images/error_small.gif"/>
                                                    <bean:define id="titulo" value="Este pedido no se ha cancelado en su totalidad"/>
                                                </logic:lessThan>
                                                <%--------------------------------------------------------------------%>
                                                <tr class="${clase}" title="${titulo}">
                                                    <td class="columna_contenido fila_contenido" width="3%" align="center"><img src="${icono}" border="0"></td>
                                                    <td class="columna_contenido fila_contenido" width="4%" align="center">${fila}</td>
                                                    <td class="columna_contenido fila_contenido" width="13%" align="center">
                                                        <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indiceGlobal" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                        <%--<html:link title="detalle del pedido" href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indiceGlobal}','WESTPED_${idSesion}')"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>--%>
                                                    </td>
                                                    <td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
                                                    <td class="columna_contenido fila_contenido" width="35%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                    <td class="columna_contenido fila_contenido" width="9%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.abonoPedido}"/></td>
                                                    <td class="columna_contenido fila_contenido" width="9%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
                                                    <td class="columna_contenido fila_contenido" width="12%" align="center" title="Primera fecha de entrega al cliente"><bean:write name="vistaPedidoDTO" property="primeraFechaEntrega" formatKey="formatos.fechahora"/></td>
                                                    <td class="columna_contenido fila_contenido columna_contenido_der" width="7%" align="center">
                                                        <html:link href="#" onclick="requestAjax('entregaPedido.do',['mensajes','resultadosBusqueda'],{parameters: 'indicePedido=${indiceGlobal}', evalScripts:true});">detalle</html:link>	
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
                                        <logic:notEqual name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="codigoEstadoPagado" value="${estadoPagoLiquidado}">
                                            <tr><td height="15px" valign="middle" colspan="2" class="textoRojo11"><b>Este pedido no se ha cancelado en su totalidad o no ha sido facturado, por lo tanto ning&uacute;n art&iacute;culo puede ser entregado</b></td></tr>
                                        </logic:notEqual>
                                    </logic:notEmpty>	
                                </table>
                            </td>	
                        </tr>
                        <tr>
                            <td>	
                                <logic:notEmpty name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="vistaDetallesPedidos">
                                    <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                        <tr>
                                            <td>
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <tr class="tituloTablas"  align="left">
                                                        <td class="columna_contenido" width="3%" align="center" rowspan="2">No</td>
                                                        <td class="columna_contenido" width="17%" align="center" rowspan="2">C&Oacute;DIGO BARRAS</td>
                                                        <td class="columna_contenido" width="35%" align="center" rowspan="2">ART&Iacute;CULO</td>
                                                        <td class="columna_contenido" width="8%" align="center" rowspan="2">CANT. TOT.</td>
                                                        <td class="columna_contenido" width="37%" align="center" title="Fecha entrega al cliente | Cantidad a entregar">ENTREGAS</td>
                                                    </tr>
                                                    <tr class="tituloTablas">
                                                        <td class="columna_contenido" align="center" width="33%" title="Fecha entrega al cliente | Cantidad a entregar">
                                                            <table cellpadding="0" cellspacing="0" width="100%">
                                                                <tr>
                                                                    <td align="center" width="8%"><html:checkbox title="Seleccionar todos" property="opSeleccionTodos" value="${opTodos}" onclick="activarDesactivarTodo(this, despachosEntregasForm.opSeleccionAlgunos);"/></td>
                                                                        <logic:equal name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
                                                                            <td align="center" width="92%">FECHA ENTREGA | CANTIDAD</td>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
                                                                            <td align="center" width="92%">FECHA ENTREGA | CANTIDAD - DOMICILIO</td>
                                                                        </logic:notEqual>                                                                        
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
                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="vistaDetallesPedidos" id="detallePedido" indexId="numDetalle"> 
                                                            <%--------- control del estilo para el color de las filas --------------%>
                                                            <bean:define id="residuo" value="${numDetalle % 2}"/>
                                                            <bean:define id="fila" value="${numDetalle + 1}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="claseD" value="blanco10"/>
                                                            </logic:equal>	
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="claseD" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%--------------------------------------------------------------------%>
                                                            <tr class="${claseD}" id="fila_${numDetalle}">
                                                                <td align="center" width="3%" class="columna_contenido fila_contenido">${fila}</td>
                                                                <td align="center" width="17%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="codigoBarras"/></td>
                                                                <td align="center" width="35%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="descripcionArticulo"/></td>
                                                                <td align="center" width="8%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="cantidadEstado"/></td>
                                                                <td align="center" width="37%" class="columna_contenido fila_contenido columna_contenido_der" title="Fecha entrega cliente | Cantidad a entregar">
                                                                    <%------------------se muestra la colecci&oacute;n de entregas del articulo -----------------------%>
                                                                    <table border="0" cellspacing="0" cellpadding="0" width="100%" class="tabla_informacion">
                                                                        <logic:iterate name="detallePedido" property="entregaDetallePedidoCol" id="entrega" indexId="numEntrega">
                                                                            <%--------- control del estilo para el color de las filas de las entregas --------------%>
                                                                            <bean:define id="residuo" value="${numEntrega % 2}"/>
                                                                            <bean:define id="fila" value="${numEntrega + 1}"/>
                                                                            <logic:equal name="residuo" value="0">
                                                                                <bean:define id="claseE" value="blanco10"/>
                                                                            </logic:equal>
                                                                            <logic:notEqual name="residuo" value="0">
                                                                                <bean:define id="claseE" value="amarilloClaro10"/>
                                                                            </logic:notEqual>
                                                                            <tr class="${claseE}" id="fila_entrega_${numDetalle}_${numEntrega}">
                                                                                <td align="center" class="columna_contenido fila_contenido" width="7%">
                                                                                    <%----------- se muestra la colecci&oacute;n de beneficiarios  
                                                                                    <logic:notEmpty name="entrega" property="beneficiarios">------------%>
                                                                                        <table border="0" cellpadding="0" cellspacing="0">
                                                                                            <%-----------<logic:iterate name="entrega" property="beneficiarios" id="beneficiario" indexId="numBeneficiario">------------%>
                                                                                                <tr>			      						
                                                                                                    <td class="textoNegro10" align="center">
                                                                                                        <bean:define value="${numDetalle}-${numEntrega}" id="indiceEntrega"/>
                                                                                                        <logic:notEmpty name="entrega" property="fechaRegistroEntregaCliente"> 
                                                                                                            <bean:define id="fechaE">
                                                                                                                <bean:write name="entrega" property="fechaRegistroEntregaCliente" formatKey="formatos.fechahora"/>
                                                                                                            </bean:define>
                                                                                                            <img title="Este art&iacute;culo ya fu&eacute; entregado el ${fechaE}" src="images/exito_16.gif">
                                                                                                            <script>cambiarEstilo('fila_entrega_${numDetalle}_${numEntrega}','#dcf3d1','normal');</script>
                                                                                                        </logic:notEmpty>
                                                                                                        <logic:empty name="entrega" property="fechaRegistroEntregaCliente"> 
                                                                                                            <html:multibox property="opSeleccionAlgunos" value="${indiceEntrega}"/>
                                                                                                        </logic:empty>
                                                                                                    </td>	
                                                                                                </tr>
                                                                                           <%-----------  </logic:iterate>------------%>
                                                                                        </table>
                                                                                    <%----------- </logic:notEmpty>------------%>
                                                                                </td>
                                                                                <td align="left" class="fila_contenido" width="93%">
                                                                                    <b>
                                                                                        <label class="textoAzul10">
                                                                                            <bean:write name="entrega" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fechahora"/>
                                                                                        </label>&nbsp;|&nbsp;<bean:write name="entrega" property="cantidadEntrega"/>
                                                                                    </b>
                                                                                    <logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
                                                                                        &nbsp;a&nbsp;<label class="textoRojo10"><bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/></label>
                                                                                    </logic:notEqual>
                                                                                </td>
                                                                                <bean:define id="tipoEntregaLocal"><bean:message key="ec.com.smx.sic.sispe.entrega.local1"/></bean:define>
																				<bean:define id="contextoOtroLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.otroLocal"/></bean:define>
																				<bean:define id="contextoAMiLocal"><bean:message key="ec.com.smx.sic.sispe.contextoEntrega.miLocal"/></bean:define>
																				<c:if test="${entrega.entregaPedidoDTO.tipoEntrega == tipoEntregaLocal && (entrega.entregaPedidoDTO.codigoContextoEntrega == contextoOtroLocal || entrega.entregaPedidoDTO.codigoContextoEntrega == contextoAMiLocal)}">
																					<td class="fila_contenido" align="left" width="10%"><a href="#" onclick="requestAjax('entregaPedido.do', ['mensajes','pregunta','seccion_detalle'], {parameters: 'linkArchivoFoto=${detallePedido.id.codigoPedido}&&codigoLocalEntrega=${entrega.entregaPedidoDTO.codigoAreaTrabajoEntrega}&&codigoArticulo=${detallePedido.id.codigoArticulo}',evalScripts: true, popWait:false});"><img src="./images/subirFoto.gif" border="0" alt="ver archivo foto"></a></td>
																				</c:if>
																				<c:if test="${entrega.entregaPedidoDTO.tipoEntrega != tipoEntregaLocal}">
																					<td class="fila_contenido" align="center" width="10%">-</td>
																				</c:if>
                                                                            </tr>
                                                                        </logic:iterate>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>	
                                </logic:notEmpty>
                                 <logic:empty name="ec.com.smx.sic.sispe.pedidoSeleccionado" property="vistaDetallesPedidos">
                                 <table class="tabla_informacion" border="0" cellspacing="0" cellpadding="0" width="100%" height ="380px">
                                        <tr>
                                            <td>
                                            	<center> No existen detalles del pedido a entregar.</center>
                                            </td>
                                        </tr>
                                 </logic:empty>
                            </td>
                        </tr>	
                    </logic:equal>
                </table>
            </td>
        </tr>
    </table>
</div>