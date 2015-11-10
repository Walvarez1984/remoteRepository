<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>

<div>
<table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
                    <tr><td height="5px"></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td class="datos" width="25%" id="izquierda">
                            <div style="height:500px;">
                                <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                            </div>
                        </td>
						<td id="divisor" height="480px">
								<span style="display:block" id="img_ocultar">
									<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
								</span>
								<span style="display:none" id="img_mostrar">
									<a href="#"><img src="./images/spliter_der.png" border="0"></a>
								</span>
							</td>
                        <%--Datos--%>
                        <td class="datos" width="80%" id="derecha">
                            <div style="height:500px;">
                                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                    <%--Titulo de los datos--%>
                                    <tr>
                                        <td class="fila_titulo" colspan="7">
                                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                                <tr>
                                                    <td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
                                                    <td height="23" width="100%">&nbsp;Detalles por fecha</td>
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
                                                                <smx:paginacion start="${vformName.start}" range="${vformName.range}" results="${vformName.size}" styleClass="textoNegro11" url="recotizarReservar.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
                                                            </td>
                                                        </tr> 
                                                        <tr>
                                                            <td colspan="7">
                                                                <table border="0" cellspacing="0" width="100%" class="tabla_informacion">	
                                                                    <tr class="tituloTablas">
                                                                        <td width="4%" align="center">No</td>
                                                                        <td class="columna_contenido" width="16%" align="center">No PEDIDO</td>
                                                                        <td class="columna_contenido" width="20%" align="center">CONTACTO-EMPRESA</td>
                                                                        <td class="columna_contenido" width="11%" align="center">FECHA ESTADO</td>
                                                                        <%--<td class="columna_contenido" width="11%" align="center">FECHA ENTREGA</td>--%>
                                                                        <td class="columna_contenido" width="13%" align="center">ESTADO</td>
                                                                        <td class="columna_contenido" width="9%" align="center">V. TOTAL</td>
                                                                        <td class="columna_contenido" width="20%" align="center">ACCION</td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                                                    <table border="0" cellspacing="0" width="100%" class="tabla_informacion">
                                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
                                                                            <bean:define id="indice" value="${numeroRegistro + vformName.start}"/>
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
                                                                                <td class="columna_contenido fila_contenido" align="center" width="16%" >
                                                                                <logic:notEmpty name="vistaPedidoDTO" property="codigoConsolidado">
                                                                                    <html:img src="images/consolidar.gif" border="0" alt="Pedido consolidado"/>
                                                                                </logic:notEmpty>
                                                                                    <html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
                                                                                    <%--<html:link href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indice}','WESTPED_${idSesion}')"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>--%>
                                                                                </td>
                                                                                <td class="columna_contenido fila_contenido" width="20%" align="center"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>	
                                                                                <td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
                                                                                <%--<td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="fechaMinimaEntrega" formatKey="formatos.fechahora"/></td>--%>
                                                                                <td class="columna_contenido fila_contenido" width="13%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
                                                                                <td class="columna_contenido fila_contenido" width="9%" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
                                                                                <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
																					<td class="columna_contenido fila_contenido" width="20%" align="center">
	                                                                                    <html:link action="recotizar" paramId="indice" paramName="indice" onclick="popWait();">Consolidar</html:link>
	                                                                                </td>
																				</logic:notEmpty>
																				<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
																					 <td class="columna_contenido fila_contenido" width="20%" align="center">
	                                                                                    <html:link action="recotizar" paramId="indice" paramName="indice" onclick="popWait();">Recotizar</html:link>&nbsp;/&nbsp;<html:link action="reservar" paramId="indice" paramName="indice" onclick="popWait();">Reservar</html:link>
	                                                                                </td>
																				</logic:empty>
                                                                               
                                                                            </tr>
                                                                        </logic:iterate>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </logic:notEmpty>
                                                    <logic:empty name="ec.com.smx.sic.sispe.pedidos">
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
                            </div>
                        </td>
                        <script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
                        <%--Fin Datos--%>
                    </tr>
                    <%--Fin P&aacute;gina--%>
                </table>
</div>
