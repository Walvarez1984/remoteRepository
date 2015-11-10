<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<bean:define id="tabPedido"><bean:message key="ec.com.smx.sic.sispe.tab.imprimirEtiquetas"/></bean:define>
<bean:define id="tabDetPedido"><bean:message key="ec.com.smx.sic.sispe.tab.detallePedido"/></bean:define>
<bean:define id="opTodos"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<bean:define id="estadoPagoTotal" name="ec.com.smx.sic.sispe.pedido.estadoPagado"/>
<bean:define id="estadoValidacionPago" name="ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"/>

<tiles:insert page="/include/metas.jsp"/>

<% String sessionId = session.getId(); %>
<bean:define id="idSesion" value="<%=sessionId%>"/>
<% long ide = (new java.util.Date()).getTime(); %>

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
                                <b>&nbsp;Listado de pedidos con despachos a domicilio desde el CD:</b>
                            </td>
                            <td width="10%" align="left">
                                <div id="botonD">	
                                    <html:link styleClass="despachoD" href="#" onclick="requestAjax('despachoPedido.do',['pregunta','mensajes','resultadosBusqueda'],{parameters: 'botonImprimir=ok',evalScripts: true});">Imprimir</html:link>
                                </div>												
                            </td>
                            <td width="2%"></td>
                        </tr>
                        <tr>
							<td align="center">
								<logic:equal name="ec.com.smx.sic.sispe.imprimirEtiquetas" value="ok">	
									<script language="JavaScript">imprimirReporteEntregas('reportes/despachosEntregas/rptEtiquetaEntregaCD.jsp?id=<%=ide%>');</script>
								</logic:equal>
							</td>
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
                            <td width="85%" align="left">
                                <b>Detalle de entregas del pedido seleccionado:</b>
                            </td>
                            <td width="40%" align="center" class="textoRojo10">&nbsp;</td>
                            <td align="rigth" width="10%">                                
								<div id="botonD">	
									<html:link styleClass="aceptarD" href="#" title="Seleccionar entregas a imprimir" onclick="requestAjax('despachoPedido.do',['resultadosBusqueda','pregunta','mensajes'],{parameters: 'botonAceptar=ok'});">Aceptar</html:link>
								</div>                               
                            </td>
                            <td align="right" width="10%">
                                <div id="botonD">
                                    <html:link styleClass="atrasD" href="#" title="Cancelar la selecci&oacute;n de entregas y volver al listado de pedidos" onclick="requestAjax('despachoPedido.do',['resultadosBusqueda','mensajes','pregunta','seccion_detalle'],{parameters: 'botonVolverBusquedaEtiquetas=ok'});">Volver</html:link>
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
                                            <td class="columna_contenido fila_contenido" width="3%" align="center">No</td>
                                            <td class="columna_contenido fila_contenido" width="15%" align="center">No pedido</td>
                                            <td class="columna_contenido fila_contenido" width="8%" align="center">No res.</td>
                                            <td class="columna_contenido fila_contenido" width="39%" align="center">Contacto - Empresa</td>                                            
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
                                                <bean:define id="indice" value="${indicePedido + despachosEntregasForm.start}"/>
                                                <bean:define id="numFila" value="${indice + 1}"/>                                                                                          
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
												
												<%--------------------------------------------------------------------%>
												<tr class="${clase}" title="${titulo}">
													<td class="columna_contenido fila_contenido" width="3%" align="center">${fila}</td>
													<td class="columna_contenido fila_contenido" width="15%" align="center">
														<html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indiceGlobal" onclick="popWait();"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>                                                      
													</td>
													<td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>
													<td class="columna_contenido fila_contenido" width="39%" align="left"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>
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
                                                        <td class="columna_contenido" width="27%" align="center" title="Fecha despacho de la bodega | cantidad a lugar">DESPACHOS</td>
                                                    </tr>
                                                    <tr class="tituloTablas">                                                        
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
                                                                
                                                                <tr class="${colorBack}"> 
                                                                    <td width="3%" align="center" class="columna_contenido fila_contenido">${fila}</td>
                                                                    <td align="center" width="15%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></td>
                                                                    <td align="center" width="22%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="descripcionArticulo"/></td>
                                                                   
                                                                    <td align="center" width="9%" class="columna_contenido fila_contenido"><bean:write name="detallePedido" property="cantidadReservarSIC"/></td>
                                                                    
                                                                    <td align="center" width="27%" class="columna_contenido fila_contenido columna_contenido_der">
                                                                        <%------------------------ si la colecci&oacute;n de entregas no est&aacute; vacia -----------------------%>
                                                                        <table cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion">
                                                                            <logic:notEmpty name="detallePedido" property="entregaDetallePedidoCol">
                                                                                <c:set var="indiceEntrega" value="0"/>
                                                                                <logic:iterate name="detallePedido" property="entregaDetallePedidoCol" id="entrega" indexId="numEntrega">
                                                                                    <logic:greaterThan name="entrega" property="cantidadDespacho" value="${0}">
                                                                                        <tr class="${claseE}" title="${titulo}">
																							<logic:notEmpty name="entrega" property="fechaRegistroDespacho">
																								<td align="left" class="columna_contenido fila_contenido" width="7%">
																									<bean:define value="${numDetalle}-${numEntrega}" id="indiceDespacho"/>																								                                                                                                
																									<html:multibox title="Fecha despacho al cliente | cantidad despachada" property="opSeleccionAlgunos" value="${indiceDespacho}"/>&nbsp;                                                                                                                                                                                              
																								</td>																							
																								<td class="fila_contenido" width="83%" align="left">
																									<b><label class="textoAzul10"><bean:write name="entrega" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/></label>|&nbsp;<bean:write name="entrega" property="cantidadDespacho"/></b>&nbsp;a&nbsp;<label class="textoRojo10"><bean:write name="entrega" property="entregaPedidoDTO.direccionEntrega"/></label>
																								</td>
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