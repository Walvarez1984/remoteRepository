<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- tabDespacho es llamdado desde listaControlProduccionPedido.jsp -->
<tiles:useAttribute id="vtformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>

<table class="tabla_informacion textoNegro12" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
    <%--Titulo de los datos--%>
    <tr valign="top">
        <td class="fila_titulo" valign="top" align="right">
            <table cellpadding="1" cellspacing="0" border="0">
                <tr>
                    <td width="3%"><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                    <td height="23" align="left" class="textoNegro10">Pedidos especiales listos para despachar</td>
                    <%--<td height="23" width="10%">
                        <div id="botonDsg">
                            <html:link styleClass="despachoPrevioDsg" href="#" title="Despacho Anticipado" onclick="requestAjax('controlProduccionPedEsp.do',['mensajes'],{parameters: 'despachoPrevio=ok', evalScripts: true});">Pre - Despachar</html:link>
                        </div>
                    </td>--%>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                        <td height="23" width="10%">
                            <div id="botonD">
                                <html:link styleClass="despachoD" href="#" title="Iniciar despacho" onclick="requestAjax('controlProduccionPedEsp.do',['resultadosBusqueda','mensajes','pregunta'],{parameters: 'botonDespacharPed=ok', evalScripts: true});">Despachar</html:link>
                            </div>
                        </td>
                    </logic:notEmpty>
                    <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">
                        <td height="23" width="10%">
                            <div id="botonDesactivadoD">
                                <html:link styleClass="despachoDlock" href="#" title="Despacho desactivado, primero cierre el d&iacute;a" >Despachar</html:link>
                            </div>
                        </td>
                    </logic:empty>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="5px" bgcolor="#F4F5EB"></td></tr>
    <tr>
        <td bgcolor="#F4F5EB" align="center">
            <div id="resultadosBusqueda">
                <table border="0" class="textoNegro11" width="99%" cellspacing="0" cellpadding="0">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.subPagina">
                        <tr>
                            <td colspan="8" align="right">
                                <smx:paginacion start="${controlProduccionPedidoEspecialForm.start}" range="${controlProduccionPedidoEspecialForm.range}" results="${controlProduccionPedidoEspecialForm.size}" styleClass="textoNegro11" url="controlProduccionPedEsp.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
                            </td>
                        </tr> 
                        <tr>
                            <td align="center">
                                <table cellpadding="0" cellspacing="0" width="100%">
                                    <tr>
                                        <td>
                                            <table border="0" cellspacing="0" class="tabla_informacion" width="100%" cellpadding="1">	
                                                <tr class="tituloTablas">
                                                    <td width="3%" align="center">
                                                        <html:checkbox title="Seleccionar todos los pedidos" property="checkSeleccionarTodo" value="t" onclick="activarDesactivarTodo(this,controlProduccionPedidoEspecialForm.checksSeleccionar);"></html:checkbox>
                                                    </td>
                                                    <td class="columna_contenido" width="2%" align="center"></td>
                                                    <td class="columna_contenido" width="3%" align="center">No</td>
                                                    <td class="columna_contenido" width="12%"align="center">Local Origen</td>
                                                    <td class="columna_contenido" width="12%"align="center">Local Despacho</td>
                                                    <td class="columna_contenido" width="12%"align="center">No Pedido</td>
                                                    <td class="columna_contenido" width="20%" align="center">Contacto - Empresa</td>
                                                    <td class="columna_contenido" width="8%"align="center">Fecha Despacho</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <div id="div_listado" style="width:100%;height:355px;overflow-y:auto;overflow-x:auto;">
                                                <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="1">
                                                    <logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
                                                        <bean:define id="idLocal" value="${vistaPedidoDTO.id.codigoAreaTrabajo}" />
                                                        <bean:define id="indice" value="${numeroRegistro + controlProduccionPedidoEspecialForm.start}"/>
                                                        <%--------- control del estilo para el color de las filas -----------%>
                                                        <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                        <bean:define id="fila" value="${indice + 1}"/>
                                                        <logic:equal name="residuo" value="0">
                                                            <bean:define id="colorBack" value="blanco10"/>
                                                        </logic:equal>
                                                        <logic:notEqual name="residuo" value="0">
                                                            <bean:define id="colorBack" value="grisClaro10"/>
                                                        </logic:notEqual>
                                                        <logic:notEqual name="idLocal" value="${vistaPedidoDTO.codigoLocalEntrega}">
                                                            <bean:define id="colorBack" value="verdeClaro10" />
                                                        </logic:notEqual>
                                                        <logic:equal name="vistaPedidoDTO" property="npEstadoError" value="1">
                                                            <bean:define id="colorBack" value="rojoObsuro10"/>
                                                        </logic:equal>
                                                        
                                                        <%-- se verifica la fecha de cierre para verificar si se puede dspachar o no --%>
                                                        <c:set var="titulo" value=""/>
                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                            <logic:empty name="vistaPedidoDTO" property="fechaCierrePedidoEspecial">
                                                                <bean:define id="colorBack" value="naranjaClaro10"/>
                                                                <c:set var="titulo" value="Este pedido no puede ser despachado, primero debe habilitarlo"/>
                                                            </logic:empty>
                                                        </logic:notEmpty>
                                                        
                                                        <%------------------------------------------------------------------%>
                                                        <tr class="${colorBack}"> 
                                                            <td class="columna_contenido fila_contenido_sup" align="center" width="3%">
                                                                <div id="habilitarCheck${numeroRegistro}">
                                                                    <%--<logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                                        <logic:notEmpty name="vistaPedidoDTO" property="fechaCierrePedidoEspecial">
                                                                            <logic:notEmpty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                                <html:multibox property="checksSeleccionar" value="${numeroRegistro}"/>
                                                                            </logic:notEmpty>
                                                                            <logic:empty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                                <html:multibox property="checksSeleccionar" value="${numeroRegistro}" disabled="true"/>
                                                                            </logic:empty>
                                                                        </logic:notEmpty>
                                                                        <logic:empty name="vistaPedidoDTO" property="fechaCierrePedidoEspecial">&nbsp;</logic:empty>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">--%>
                                                                        <logic:notEmpty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                            <html:multibox property="checksSeleccionar"  value="${numeroRegistro}"/>
                                                                        </logic:notEmpty>
                                                                        <logic:empty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                            <html:multibox property="checksSeleccionar"  value="${numeroRegistro}" disabled="true"/>
                                                                        </logic:empty>
                                                                    <%--</logic:empty>--%>
                                                                </div>
                                                            </td>
                                                            <td class="columna_contenido fila_contenido_sup" align="center" width="2%" align="right">
                                                                <%-- si el despacho esta habilitado --%>
                                                                <%--<logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                                    <logic:notEmpty name="vistaPedidoDTO" property="fechaCierrePedidoEspecial">
                                                                        <div id="desplegar${numeroRegistro}" >
                                                                            <a title="Ver Detalle del Pedido" href="#" onClick="requestAjax('controlProduccionPedEsp.do',['mensajes', 'habilitarCheck${numeroRegistro}', 'marco${numeroRegistro}'],{parameters: 'detalle=${indice}', evalScripts:true});mostrar(${numeroRegistro},'marco','plegar','desplegar');">
                                                                                <html:img src="images/desplegar.gif" border="0"/>
                                                                            </a>
                                                                        </div>
                                                                        <div id="plegar${numeroRegistro}" class="displayNo">
                                                                            <a title="Ver Detalle del Pedido" href="#" onClick="ocultar(${numeroRegistro},'marco','plegar','desplegar');">
                                                                                <html:img src="images/plegar.gif" border="0"/>
                                                                            </a>
                                                                        </div>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="vistaPedidoDTO" property="fechaCierrePedidoEspecial">&nbsp;</logic:empty>
                                                                </logic:notEmpty>--%>
                                                                <%-- si el despacho no esta habilitado --%>
                                                                <%--<logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">--%>
                                                                    <div id="desplegar${numeroRegistro}">
                                                                        <logic:empty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                            <a title="Ver detalle del pedido" href="#" onClick="requestAjax('controlProduccionPedEsp.do',['mensajes', 'habilitarCheck${numeroRegistro}', 'desplegar${numeroRegistro}', 'marco${numeroRegistro}'],{parameters: 'detalle=${indice}', evalScripts:true});mostrar(${numeroRegistro},'marco','plegar','desplegar');">
                                                                                <html:img src="images/desplegar.gif" border="0"/>
                                                                            </a>
                                                                        </logic:empty>
                                                                        <logic:notEmpty name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                            <a title="Ver detalle del pedido" href="#" onClick="mostrar(${numeroRegistro},'marco','plegar','desplegar');">
                                                                                <html:img src="images/desplegar.gif" border="0"/>
                                                                            </a>
                                                                        </logic:notEmpty>
                                                                    </div>
                                                                    <div id="plegar${numeroRegistro}" class="displayNo">
                                                                        <a title="Ver detalle del pedido" href="#" onClick="ocultar(${numeroRegistro},'marco','plegar','desplegar');">
                                                                            <html:img src="images/plegar.gif" border="0"/>
                                                                        </a>
                                                                    </div>
                                                                <%--</logic:empty>--%>
                                                            </td>
                                                            <td class="columna_contenido fila_contenido_sup" align="center" width="3%">${fila}</td>
                                                            <td class="columna_contenido fila_contenido_sup" align="left" width="12%" title="${titulo}"><bean:write name="vistaPedidoDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaPedidoDTO" property="nombreLocal"/></td>
                                                            <td class="columna_contenido fila_contenido_sup" align="left" width="12%"><bean:write name="vistaPedidoDTO" property="codigoLocalEntrega"/>&nbsp;-&nbsp;<bean:write name="vistaPedidoDTO" property="nombreLocalEntrega"/></td>
                                                            
                                                            <td class="columna_contenido fila_contenido_sup" align="center" width="12%" >
                                                                <html:link title="Detalle del pedido" onclick="realizarEnvio('dp${indice}');" href="#"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                            </td>
                                                            <td class="columna_contenido fila_contenido_sup" width="20%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>	
                                                            <td class="columna_contenido fila_contenido_sup" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="primeraFechaDespacho" formatKey="formatos.fecha"/></td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="8" class="columna_contenido fila_contenido_sup">
                                                                <table cellpadding="0" cellspacing="0" width="100%">
                                                                    <tr>
                                                                        <td id="detallePedido" align="center">
                                                                            <div id="marco${numeroRegistro}" class="displayNo">
                                                                                <logic:notEmpty  name="vistaPedidoDTO" property="vistaDetallesPedidos">
                                                                                    <table width="90%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
                                                                                        <tr class="tituloTablasCeleste">
                                                                                            <td class="columna_contenido_der_negro fila_contenido_negro" align="center" width="12%">C&oacute;digo Barras</td>
                                                                                            <td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="25%">Art&iacute;culo</td>
                                                                                            <td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="5%">Marca</td>
                                                                                            <td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%">Cant.</td>
                                                                                            <td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="8%">Peso</td>
                                                                                            <td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="28%">Observaci&oacute;n</td>
                                                                                            <td class="fila_contenido_negro " align="center" width="8%">Peso a Registrar</td>
                                                                                        </tr>
                                                                                        <logic:iterate name="vistaPedidoDTO" property="vistaDetallesPedidos" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
                                                                                            <%--------- control del estilo para el color de las filas ------------%>
                                                                                            <bean:define id="residuo" value="${indiceDetalle % 2}"/>
                                                                                            <logic:equal name="residuo" value="0">
                                                                                                <bean:define id="colorBack" value="blanco10"/>
                                                                                            </logic:equal>	
                                                                                            <logic:notEqual name="residuo" value="0">
                                                                                                <bean:define id="colorBack" value="amarilloClaro10"/>
                                                                                            </logic:notEqual>
                                                                                            <logic:notEmpty name="vistaDetallePedidoDTO" property="npEstadoError">
                                                                                                <bean:define id="colorBack" value="rojoObsuro10"/>
                                                                                            </logic:notEmpty>
                                                                                            <bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
                                                                                            <tr class="${colorBack}">
                                                                                                <td class="columna_contenido fila_contenido" align="left" width="12%">
                                                                                                     <bean:write name="vistaDetallePedidoDTO" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>
                                                                                                </td>
                                                                                                <td align="left" class="columna_contenido fila_contenido" width="25%">
                                                                                                    <bean:write  name="vistaDetallePedidoDTO" property="descripcionArticulo"/>
                                                                                                </td>
                                                                                                <td align="left" class="columna_contenido fila_contenido" width="5%">
                                                                                                    <logic:notEmpty name="vistaDetallePedidoDTO" property="marcaArticulo">
                                                                                                        <bean:write name="vistaDetallePedidoDTO" property="marcaArticulo"/>
                                                                                                    </logic:notEmpty>
                                                                                                    <logic:empty name="vistaDetallePedidoDTO" property="marcaArticulo">
                                                                                                        &nbsp;
                                                                                                    </logic:empty>
                                                                                                </td>
                                                                                                <td align="right" class="columna_contenido fila_contenido" width="8%">
                                                                                                    <bean:write  name="vistaDetallePedidoDTO" property="cantidadEstado"/>
                                                                                                </td>
                                                                                                <td align="right" class="columna_contenido fila_contenido" width="8%">
                                                                                                    <bean:write  name="vistaDetallePedidoDTO" property="pesoArticuloEstado"/>
                                                                                                </td>
                                                                                                <td align="left" class="columna_contenido fila_contenido" width="28%">
                                                                                                    <bean:write  name="vistaDetallePedidoDTO" property="observacionArticulo"/>
                                                                                                </td>
                                                                                                <td class="columna_contenido fila_contenido " align="center" width="8%">
                                                                                                    <div id="div_peso">
                                                                                                        <input  type="text" name="${numeroRegistro}-${indiceDetalle}" class="textNormal" value="${vistaDetallePedidoDTO.pesoRegistradoBodega}" maxlength="12" size="7">
                                                                                                    </div>
                                                                                                </td>
                                                                                            </tr>
                                                                                        </logic:iterate>
                                                                                    </table>
                                                                                </logic:notEmpty>
                                                                            </div>
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
                                </table>
                            </td>
                        </tr>
                    </logic:notEmpty>
                    <logic:empty name="ec.com.smx.sic.sispe.pedidos.subPagina">
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
</table>