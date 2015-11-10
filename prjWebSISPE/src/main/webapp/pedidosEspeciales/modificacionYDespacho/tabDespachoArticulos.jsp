<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/mensajeria.tld" prefix="mensajeria"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<!-- tabDespacho es llamdado desde listaControlProduccionPedido.jsp -->
<tiles:useAttribute id="vtformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define name="sispe.estado.inactivo" id="estadoInactivo"/>

<div>
    <table class="tabla_informacion textoNegro12" border="0" cellpadding="0" cellspacing="0" width="99%" align="center">
        <%--Titulo de los datos--%>
        <tr>
            <td class="fila_titulo" colspan="6" align="right">
                <table cellpadding="1" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td width="3%"><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                        <td height="23" class="textoNegro10" align="left">&nbsp;Art&iacute;culos listos para despachar</td>
                        <td width="10%">
                            <div id="botonDsg">
                                <html:link styleClass="despachoPrevioDsg" href="#" title="Despacho Anticipado" onclick="requestAjax('controlProduccionPedEsp.do',['mensajes','mailDespachoPrevio'],{parameters: 'desPreArt=ok', evalScripts: true});">Pre - Despachar</html:link>
                            </div>
                        </td>
                        <td align="right" width="10%">
                            <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                <div id="botonD">
                                    <html:link styleClass="despachoD" href="#" title="Realizar Despacho" onclick="requestAjax('controlProduccionPedEsp.do',['resultadosBusqueda','mensajes','pregunta'],{parameters: 'botonDespacharArticulos=ok', evalScripts: true});">Despachar</html:link>
                                </div>
                            </logic:notEmpty>
                            <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                <div id="botonDesactivadoD">
                                    <html:link styleClass="despachoDlock" href="#" title="Despacho desactivado, primero cierre el d&iacute;a">Despachar</html:link>
                                </div>
                            </logic:empty>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="5px"></td></tr>
        <tr>
            <td align="center">
                <div id="resultadosBusqueda" style="width:100%;height:440px;overflow-y:hidden;overflow-x:hidden;">
                    <table border="0" class="textoNegro11" width="99%" cellspacing="0" cellpadding="0">
                        <logic:notEmpty name="ec.com.smx.sic.sispe.despachoPedEsp.paginaArticulos">
                            <tr>
                                <td align="right" id="pag">
                                    <smx:paginacion start="${controlProduccionPedidoEspecialForm.start}" range="${controlProduccionPedidoEspecialForm.range}" results="${controlProduccionPedidoEspecialForm.size}" styleClass="textoNegro11" url="controlProduccionPedEsp.do" campos="false" requestAjax="'pag','div_listado'"/>
                                </td> 
                            </tr>
                            <tr>
                                <td>
                                    <table border="0" cellspacing="0" class="tabla_informacion" align="center" width="100%" cellpadding="1">	
                                        <tr class="tituloTablas">
                                            <%--
                                            <td class="columna_contenido" width="3%" align="center">
                                                <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                    <html:checkbox title="Seleccionar todos los pedidos" property="checkSeleccionarTodo" value="t" onclick="activarDesactivarTodo(this,controlProduccionPedidoEspecialForm.checksSeleccionar);"></html:checkbox>
                                                </logic:notEmpty>
                                                <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                    &nbsp;
                                                </logic:empty>
                                            </td>
                                            --%>
                                            <td class="columna_contenido" width="3%">&nbsp;</td>
                                            <td class="columna_contenido" width="3%" align="center">No</td>
                                            <td class="columna_contenido" width="15%"align="center">Local</td>
                                            <td class="columna_contenido" width="15%" align="center">Local entrega</td>
                                            <td class="columna_contenido" width="10%"align="center">Codigo</td>
                                            <td class="columna_contenido" width="25%"align="center">Descripci&oacute;n</td>
                                            <td class="columna_contenido" width="8%" align="center">Cantidad</td>
                                            <td class="columna_contenido" width="8%"align="center">Peso</td>
                                            <td class="columna_contenido" width="9%"align="center">&nbsp;</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="div_listado" style="width:100%;height:375px;overflow-y:auto;overflow-x:auto;">
                                        <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="1">
                                            <logic:iterate name="ec.com.smx.sic.sispe.despachoPedEsp.paginaArticulos" id="vistaArticuloDTO" indexId="indiceArticulo">
                                                <%--------- control del estilo para el color de las filas -----------%>
                                                <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                <bean:define id="fila" value="${indiceArticulo + controlProduccionPedidoEspecialForm.start + 1}"/>
                                                <logic:equal name="residuo" value="0">
                                                    <bean:define id="colorBack" value="blanco10"/>
                                                </logic:equal>
                                                <logic:notEqual name="residuo" value="0">
                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                </logic:notEqual>
                                                <logic:notEqual name="vistaArticuloDTO" property="id.codigoAreaTrabajo" value="${vistaArticuloDTO.codigoLocalReferencia}">
                                                    <bean:define id="colorBack" value="verdeClaro10"/>
                                                </logic:notEqual>
                                                
                                                <%------------------------------------------------------------------%>
                                                <tr class="${colorBack}">
                                                    <%--
                                                    <td class="columna_contenido fila_contenido_sup" align="center" width="3%">
                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                            <html:multibox property="checksSeleccionar"  value="${indiceArticulo}"/>
                                                        </logic:notEmpty>
                                                        <logic:empty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                            &nbsp;
                                                        </logic:empty>
                                                    </td>
                                                    --%>
                                                    <td class="columna_contenido fila_contenido_sup" width="3%">
                                                        <c:set var="despliegueA1" value="block"/>
                                                        <c:set var="despliegueA2" value="none"/>
                                                        <logic:notEmpty name="indiceNivel1">
                                                            <logic:equal name="indiceNivel1" value="${indiceArticulo}">
                                                                <c:set var="despliegueA1" value="none"/>
                                                                <c:set var="despliegueA2" value="block"/>
                                                            </logic:equal>
                                                        </logic:notEmpty>
                                                        
                                                        <logic:notEmpty name="vistaArticuloDTO" property="colVistaArticuloDTO">
                                                            <div style="display:${despliegueA1}" id="desplegar_${indiceArticulo}">
                                                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}']);show(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);"></a>
                                                            </div>
                                                            <div style="display:${despliegueA2}" id="plegar_${indiceArticulo}">
                                                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);show(['desplegar_${indiceArticulo}']);"></a>
                                                            </div>
                                                        </logic:notEmpty>
                                                    </td>
                                                    <td class="columna_contenido fila_contenido_sup" align="center" width="3%">${fila}</td>
                                                    <td class="columna_contenido fila_contenido_sup" align="left" width="15%"><bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/></td>
                                                    <td class="columna_contenido fila_contenido_sup" align="left" width="15%"><bean:write name="vistaArticuloDTO" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalReferencia"/></td>
                                                    <td class="columna_contenido fila_contenido_sup" align="center" width="10%"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                                                    <td class="columna_contenido fila_contenido_sup" width="25%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>	
                                                    <td class="columna_contenido fila_contenido_sup " width="8%" align="center"><bean:write name="vistaArticuloDTO" property="cantidadEstado"/></td>
                                                    <td class="columna_contenido fila_contenido_sup " width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaArticuloDTO.pesoArticuloEstado}"/></td>
                                                    <td class="columna_contenido fila_contenido_sup " width="9%" align="left">
                                                        <input type="text" name="peso${indiceArticulo}" value="${vistaArticuloDTO.npPesoIngresado}" class="textNormal" maxlength="10" size="7">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="9">
                                                        <div id="listado_${indiceArticulo}" style="display:${despliegueA2}">
                                                            <logic:notEmpty name="vistaArticuloDTO" property="colVistaArticuloDTO">
                                                                <table border="0" class="tabla_informacion_negro" width="95%" align="center" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td colspan="6">
                                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion_negro">
                                                                                <tr class="tituloTablas" align="left">
                                                                                    <td class="tituloTablasCeleste" width="5%" align="center">No</td>
                                                                                    <td class="tituloTablasCeleste" width="15%" align="center">No pedido</td>
                                                                                    <td class="tituloTablasCeleste" width="9%" align="center">Cantidad</td>
                                                                                    <td class="tituloTablasCeleste" width="9%" align="center">Peso</td>
                                                                                    <td class="tituloTablasCeleste" width="25%" align="center">Observaci&oacute;n</td>
                                                                                    <td class="tituloTablasCeleste" width="10%" align="center">Fecha despacho</td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
                                                                                    <%--------- control del estilo para el color de las filas --------------%>
                                                                                    <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
                                                                                    <logic:equal name="residuo2" value="0">
                                                                                        <bean:define id="clase2" value="blanco10"/>
                                                                                    </logic:equal>
                                                                                    <logic:notEqual name="residuo2" value="0">
                                                                                        <bean:define id="clase2" value="amarilloClaro10"/>
                                                                                    </logic:notEqual>
                                                                                    
                                                                                    <%--
                                                                                <c:set var="titulo" value=""/>
                                                                                <logic:notEmpty name="ec.com.smx.sic.sispe.habilitaDespacho">
                                                                                    <logic:equal name="vistaArticuloDTO2" property="npEstadoCierreDiaPedidosEspeciales" value="${estadoInactivo}">
                                                                                        <bean:define id="clase2" value="naranjaClaro10"/>
                                                                                        <c:set var="titulo" value="este pedido no se toma en cuenta para el despacho"/>
                                                                                    </logic:equal>
                                                                                </logic:notEmpty>
                                                                                --%>
                                                                                
                                                                                    <tr class="${clase2}" id="fila_${indiceArticulo2}"> 
                                                                                        <td width="5%" align="center" class="columna_contenido fila_contenido">${indiceArticulo2 + 1}</td>
                                                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><html:link title="Ver detalle del pedido" onclick="realizarEnvio('IPVA-${indiceArticulo}-${indiceArticulo2}');" href="#"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link></td>
                                                                                        <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaArticuloDTO2" property="cantidadEstado"/></td>
                                                                                        <td class="columna_contenido fila_contenido" width="9%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaArticuloDTO2.pesoArticuloEstado}"/></td>
                                                                                        <td class="columna_contenido fila_contenido" width="25%" align="left"><bean:write name="vistaArticuloDTO2" property="observacionArticulo"/></td>
                                                                                        <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO2" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
                                                                                    </tr>
                                                                                </logic:iterate>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </logic:notEmpty>
                                                        </div>
                                                    </td>
                                                </tr>
                                                <tr><td height="2px"></td></tr>
                                            </logic:iterate> 
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </logic:notEmpty>
                        <logic:empty name="ec.com.smx.sic.sispe.despachoPedEsp.paginaArticulos">
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
        <tr>
            <td>
                <div id="mailDespachoPrevio">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.colMailMensajeEST">
                        <logic:iterate name="ec.com.smx.sic.sispe.colMailMensajeEST" id="mailMensajeEST" indexId="indiceMail">
                            <mensajeria:enviarMail asunto="${mailMensajeEST.asunto}" de="${mailMensajeEST.de}" para="${mailMensajeEST.para[0]}" host="${mailMensajeEST.host}" puerto="${mailMensajeEST.puerto}"
                            codigoCompania = "${valoresMail.eventoDTO.id.companyId}" codigoSistema = "${valoresMail.eventoDTO.id.systemId}" codigoEvento = "${valoresMail.eventoDTO.id.codigoEvento}">
                                <tiles:insert page="/pedidosEspeciales/modificacionYDespacho/mailDespachoPrevio.jsp" flush="false">
                                    <tiles:put name="pIndiceMail" value="${indiceMail}"/>
                                </tiles:insert>
                            </mensajeria:enviarMail>
                        </logic:iterate>
                    </logic:notEmpty>
                </div>
            </td>
        </tr>
    </table>
</div>