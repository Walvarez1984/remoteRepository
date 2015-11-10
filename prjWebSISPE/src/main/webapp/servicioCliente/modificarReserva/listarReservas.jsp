<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<bean:define id="estadoSinPago" name="ec.com.smx.sic.sispe.estado.pagado.sinPago"/>
<bean:define id="estadoPagadoTotalmente" name="ec.com.smx.sic.sispe.estado.pagado.totalmente"/>

<c:set var="anchoColumnaAccion" value="10%"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.reenvioReservacion"> 
    <c:set var="anchoColumnaAccion" value="16%"/>
</logic:notEmpty>
<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <tr>
        <td>
            <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
                <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
                <script language="javascript">mostrarModal();</script>
            </logic:notEmpty>
        </td>
    </tr>
    <!--Contenido de la p&aacute;gina-->
    <tr>
        <td align="center" valign="top">
            <table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
                <tr><td height="7px" colspan="3"></td></tr>
                <tr>
                    <!--Barra Izquierda-->
                    <td class="datos" width="25%">
                        <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                    </td>
                    <TD class="datos" width="1%">&nbsp;</TD> 
                    <!--Datos-->
                    <TD class="datos" width="80%">
                        <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                            <!--Titulo de los datos-->
                            <tr>
                                <td class="fila_titulo" colspan="7">
                                    <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                        <tr>
                                            <td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
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
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                                <tr>
                                                    <td colspan="7" align="right">
                                                        <smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro11" url="listaModificarReserva.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
                                                    </td>
                                                </tr> 
                                                <tr>
                                                    <td colspan="7">
                                                        <table border="0" cellspacing="0" width="100%" class="tabla_informacion" cellpadding="1">	
                                                            <tr class="tituloTablas">
                                                                <td width="3%" align="center">No</td>
                                                                <td class="columna_contenido" width="3%" align="center" title="Pedido con autorizaci&oacute;n.">Aut.</td>
                                                                <td class="columna_contenido" width="13%" align="center">No pedido</td>
                                                                <td class="columna_contenido" width="5%" align="center">No res.</td>
                                                                <td class="columna_contenido" width="20%" align="center">Contacto - Empresa</td>
                                                                <%--<td class="columna_contenido" width="10%" align="center">Estado</td>--%>
                                                                <td class="columna_contenido" width="9%" align="center">Fecha pedido</td>
                                                                <td class="columna_contenido" width="8%" align="center">V. total</td>
                                                                <td class="columna_contenido" width="10%" align="center">Estado pago</td>
                                                                <td class="columna_contenido" width="${anchoColumnaAccion}" align="center">Acci&oacute;n</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div id="div_listado" style="width:100%;height:420px;overflow:auto">
                                                            <table border="0" cellspacing="0" cellpadding="2" width="100%" class="tabla_informacion">
                                                                <logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
                                                                    <!-- <bean:define id="indice" value="${numeroRegistro + listadoPedidosForm.start}"/> -->
                                                                    <bean:define id="indice" value="${numeroRegistro}"/>
                                                                    <bean:define id="fila" value="${indice + 1}"/>
                                                                    <%--------- control del estilo para el color de las filas --------------%>
                                                                    <bean:define id="residuo" value="${numeroRegistro % 2}"/>
                                                                    <logic:equal name="residuo" value="0">
                                                                        <bean:define id="colorBack" value="blanco10"/>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="residuo" value="0">
                                                                        <bean:define id="colorBack" value="grisClaro10"/>
                                                                    </logic:notEqual>
                                                                    
                                                                    <c:set var="tituloFila" value="Estado: ${vistaPedidoDTO.descripcionEstado}"/>
                                                                    <logic:equal name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoPagadoTotalmente}">
                                                                        <c:set var="tituloFila" value="Estado: ${vistaPedidoDTO.descripcionEstado}, Pagado Totalmente"/>
                                                                    </logic:equal>
                                                                    <%--------------------------------------------------------------------%>
                                                                    <tr class="${colorBack}"> 
                                                                        <td class="fila_contenido" align="center" width="3%">${fila + listadoPedidosForm.start}</td>
                                                                        <logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
                                                                        	<logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionStock">
                                                                        		<td class="columna_contenido fila_contenido" align="center" width="3%" title="Pedido con autorizaci&oacute;n descuento variable y stock.">
																			  		<html:img src="images/autorizacion16.png" border="0"/>
																			 	</td>
                                                                        	</logic:notEmpty>
                                                                        	<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionStock">
                                                                        		<td class="columna_contenido fila_contenido" align="center" width="3%" title="Pedido con autorizaci&oacute;n descuento variable.">
																			  		<html:img src="images/autorizacion16.png" border="0"/>
																			 	</td>
                                                                        	</logic:empty>
																		</logic:notEmpty>
																		<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionDctVar">
																			<logic:notEmpty name="vistaPedidoDTO" property="tieneAutorizacionStock">
                                                                        		<td class="columna_contenido fila_contenido" align="center" width="3%" title="Pedido con autorizaci&oacute;n de stock.">
																			  		<html:img src="images/autorizacion16.png" border="0"/>
																			 	</td>
                                                                        	</logic:notEmpty>
																			<logic:empty name="vistaPedidoDTO" property="tieneAutorizacionStock">
                                                                        		<td class="columna_contenido fila_contenido" align="center" width="3%">&nbsp;</td>
                                                                        	</logic:empty>
																		</logic:empty>
                                                                        <td class="columna_contenido fila_contenido" align="center" width="13%">
                                                                        	<logic:notEmpty name="vistaPedidoDTO" property="codigoConsolidado">
                                                                                <html:img src="images/consolidar.gif" border="0" alt="Pedido consolidado"/>
                                                                            </logic:notEmpty>
                                                                            <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                       	</td>
                                                                        <td class="columna_contenido fila_contenido" width="5%" align="center" title="${tituloFila}"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/>&nbsp;</td>	
                                                                        <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
                                                                        <%--<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>--%>
                                                                        <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
                                                                        <td class="columna_contenido fila_contenido" width="8%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
                                                                        <td class="columna_contenido fila_contenido" width="10%" align="left">&nbsp;
																			<smx:equal name="vistaPedidoDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																				<bean:message key="label.codigoEstadoPSP"/>
																			</smx:equal>
																			<smx:equal name="vistaPedidoDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
																				<bean:message key="label.codigoEstadoPPA"/>
																			</smx:equal>
																			<smx:equal name="vistaPedidoDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
																				<bean:message key="label.codigoEstadoPTO"/>
																			</smx:equal>
																			<smx:equal name="vistaPedidoDTO" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																				<bean:message key="label.codigoEstadoLQD"/>
																			</smx:equal>
																		</td>
                                                                        <td class="columna_contenido fila_contenido" width="${anchoColumnaAccion}" align="left">
                                                                            <logic:notEmpty name="ec.com.smx.sic.sispe.reenvioReservacion">
                                                                                <%-->logic:notEqual name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoPagadoTotalmente}"--%>
                                                                                    <html:link action="listaModificarReserva" paramId="indiceModificar" paramName="indice" title="Modificar la reservaci&oacute;n" onclick="popWait();">Modificar</html:link>
                                                                                <%--/logic:notEqual--%>
                                                                                <%-- logic:equal name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoPagadoTotalmente}">
                                                                                    <label class="textoGris10"><b>Modificar</b></label>
                                                                                </logic:equal--%>
                                                                                <%--logic:notEqual name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoSinPago}"--%>
                                                                                    &nbsp;/&nbsp;<html:link action="listaModificarReserva" paramId="indiceReenviar" paramName="indice" title="Reenviar la reservación al SIC">Reenviar CD</html:link>
                                                                                <%--/logic:notEqual>
                                                                                <logic:equal name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoSinPago}">
                                                                                    &nbsp;/&nbsp;<label class="textoGris10"><b>Reenviar CD</b></label>
                                                                                </logic:equal--%>
                                                                            </logic:notEmpty>
                                                                            <logic:empty name="ec.com.smx.sic.sispe.reenvioReservacion">
                                                                                <%-- logic:notEqual name="vistaPedidoDTO" property="codigoEstadoPagado" value="${estadoPagadoTotalmente}"--%>
                                                                                    <html:link action="listaModificarReserva" paramId="indiceModificar" paramName="indice" title="Modificar la reservaci&oacute;n" onclick="popWait();">Modificar</html:link>
                                                                                <%--/logic:notEqual--%>                                                                                
                                                                            </logic:empty>
                                                                        </td>
                                                                    </tr>
                                                                </logic:iterate>
                                                            </table>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </logic:notEmpty>
                                            <logic:empty name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                                <tr>
                                                    <td colspan="7">
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
</table>
