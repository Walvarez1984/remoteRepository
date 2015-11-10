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

<tiles:insert page="/include/top.jsp"/>

 <logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
  	<bean:define id="vformulario" value="reenviarModificarAbonos"/>
  	<bean:define id="vaccion" value="reenviarModificarAbonos.do"/>
 </logic:notEmpty>
  <logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
  	<bean:define id="vformulario"  value="registrarAbonoPedido"/>
  	<bean:define id="vaccion" value="registrarAbonoPedido.do"/>
 </logic:empty>

<html:form action="${vformulario}" method="post">
	<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <%--<html:form action="registrarAbonoPedido" method="post" focus="campoBusqueda">--%>
        <html:hidden property="ayuda" value=""/>
        <tr>
            <td>
                <div id="pregunta">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
                        <jsp:include page="../../confirmacion/confirmacion.jsp"/>
                    </logic:notEmpty>
                </div>
            </td>
        </tr>
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/busqueda_pedido.gif" border="0"></img></td>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
                        	<td height="35" valign="middle">Ver abonos</td>
                        </logic:notEmpty>
                        <logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
                        	<td height="35" valign="middle">Pedidos</td>
                        </logic:empty>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%--Contenido de la p&aacute;gina--%>
        <tr>
            <td align="center" valign="top">
                <table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
                    <tr><td height="7px" colspan="3"></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td class="datos" width="25%">
                            <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                        </td>
                        <TD class="datos" width="1%">&nbsp;</TD>
                        <%--Datos--%>
                        <TD class="datos" width="80%">
                            <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                <%--Titulo de los datos--%>
                                <tr>
                                    <td class="fila_titulo" colspan="7">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td><img src="images/detalle_pedidos24.gif" border="0"/></td>
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
                                                <logic:present name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                                    <tr>
                                                        <td colspan="5" align="right">
                                                            <smx:paginacion start="${registrarAbonoPedidoForm.start}" range="${registrarAbonoPedidoForm.range}" results="${registrarAbonoPedidoForm.size}" styleClass="textoNegro11" url="${vaccion}" campos="false"/>
                                                        </td>
                                                    </tr> 
                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
														<tr>
															<td colspan="5">															
																<table border="0" cellspacing="0" width="98%" class="tabla_informacion">	
																	<tr class="tituloTablas">
																		<td class="columna_contenido" width="4%" align="center">&nbsp;</td>
																		<td class="columna_contenido" width="4%" align="center">No</td>
																		<td class="columna_contenido" width="14%" align="center">N&Uacute;MERO PEDIDO</td>
																		<td class="columna_contenido" width="8%" align="center">N&Uacute;MERO RESERVA</td>
																		<td class="columna_contenido" width="23%" align="center">CONTACTO - EMPRESA</td>
																		<td class="columna_contenido" width="12%" align="center">VALOR TOTAL</td>                                                                    
																		<td class="columna_contenido" width="15%" align="center">ESTADO</td>                                                                    
																		<td class="columna_contenido" width="10%" align="center">ESTADO PAGO</td>
																		<td class="columna_contenido" width="15%" align="center">ACCI&Oacute;N</td>
																	</tr>
																</table>															
															</td>
														</tr>
													</logic:notEmpty>
													<logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
														<tr>
															<td colspan="5">															
																<table border="0" cellspacing="0" width="98%" class="tabla_informacion">	
																	<tr class="tituloTablas">
																		<td class="columna_contenido" width="4%" align="center">&nbsp;</td>
																		<td class="columna_contenido" width="4%" align="center">No</td>
																		<td class="columna_contenido" width="14%" align="center">N&Uacute;MERO PEDIDO</td>
																		<td class="columna_contenido" width="8%" align="center">N&Uacute;MERO RESERVA</td>
																		<td class="columna_contenido" width="28%" align="center">CONTACTO - EMPRESA</td>
																		<td class="columna_contenido" width="12%" align="center">VALOR TOTAL</td>																		
																		<td class="columna_contenido" width="15%" align="center">ESTADO PAGO</td>
																		<td class="columna_contenido" width="15%" align="center">ACCI&Oacute;N</td>
																	</tr>
																</table>															
															</td>
														</tr>
													</logic:empty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
														<tr>
															<td>
																<div id="div_listado" style="width:100%;height:400px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">                                                            
																	<table border="0" cellspacing="0" width="98%" class="tabla_informacion" cellpadding="1" cellspacing="0">
																		<logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="pedido" indexId="numeroRegistro">
																			<bean:define id="indice" value="${numeroRegistro + registrarAbonoPedidoForm.start}"/>
																			<%--------- control del estilo para el color de las filas --------------%>
																			<bean:define id="residuo" value="${numeroRegistro % 2}"/>
																			<bean:define id="fila" value="${numeroRegistro + 1}"/>
																			<logic:equal name="residuo" value="0">
																				<bean:define id="colorBack" value="blanco10"/>
																			</logic:equal>
																			<logic:notEqual name="residuo" value="0">
																				<bean:define id="colorBack" value="grisClaro10"/>
																			</logic:notEqual>
																			<%--------------------------------------------------------------------%>
																			<tr class="${colorBack}">
																				<td class="columna_contenido fila_contenido" align="center" width="4%">																			
																					<bean:define id="total" value="${pedido.totalPedido-pedido.abonoPedido}"/>																			
																					<bean:define id="codigoEstado" value="${pedido.codigoEstadoPagado}"/>																				
																					<bean:define id="codigoEstadoPedido" value="${pedido.id.codigoEstado}"/>																			
																					 <c:choose>
																						<c:when test='${codigoEstadoPedido == "ANU" && codigoEstado == "LQD"}'>
																							<img src="images/calificar16x16.gif" border="0" title="Pedido anulado por nota de cr&eacute;dito">
																						</c:when>
																						<c:when test='${(total < 0.0)&&(codigoEstado == "PTO" ||codigoEstado == "LQD")}'>
																							<img src="images/aprobarSolicitud.gif" border="0" title="Pedido con Nota de Cr&eacute;dito">
																						</c:when>
																						<c:when test='${(total > 0.0)&&(codigoEstado == "PTO"||codigoEstado == "LQD")}'>
																							<img src="images/advertencia_16.gif" border="0" title="El pago realizado no es el correcto reenv&iacute;e el abono">
																						</c:when>
																						<c:when test='${total == 0.0 && codigoEstado == "PTO"}'>
																							<img src="images/exito_16.gif" border="0" title="Pedido sin problema en los abonos">
																						</c:when>
																						<c:when test='${total == 0.0 && codigoEstado == "LQD"}'>
																							<img src="images/aprobarFactura.gif" border="0" title="El pedido ya se encuentra facturado">
																						</c:when>																						
																						<c:otherwise>
																							<img src="images/info_16.gif" border="0" title="El Pedido no se encuentra pagado totalmente">
																						</c:otherwise>
																					</c:choose>
																				</td>
																				<td class="columna_contenido fila_contenido" align="center" width="4%">${fila}</td>
																				<%--<td class="columna_contenido fila_contenido" align="center" width="14%"><html:link href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indice}','ESTDETPED')"><bean:write name="pedido" property="id.codigoPedido"/></html:link></td>--%>
																				<td class="columna_contenido fila_contenido" align="center" width="14%"><html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="pedido" property="id.codigoPedido"/></html:link></td>
																				<td class="columna_contenido fila_contenido" align="center" width="8%">&nbsp;<bean:write name="pedido" property="llaveContratoPOS"/></td>
																				<td class="columna_contenido fila_contenido" width="23%" align="left"><bean:write name="pedido" property="contactoEmpresa"/></td>
																				<td class="columna_contenido fila_contenido" width="12%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${pedido.totalPedido}"/></td>
																				<td class="columna_contenido fila_contenido" width="15%" align="left">&nbsp;
																					${pedido.descripcionEstado}
																				</td>                                                                    		
																				<td class="columna_contenido fila_contenido" width="10%" align="left">&nbsp;
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						<bean:message key="label.codigoEstadoPSP"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
																						<bean:message key="label.codigoEstadoPPA"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
																						<bean:message key="label.codigoEstadoPTO"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																						<bean:message key="label.codigoEstadoLQD"/>
																					</smx:equal>
																				</td>
																				<td class="columna_contenido fila_contenido" width="10%" align="center">
																					<logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						 Pedido sin Abonos
																					</smx:equal>
																					<smx:notEqual name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						 <html:link action="reenviarModificarAbonos" paramId="indice" paramName="numeroRegistro" title="Ver los abonos del pedido" onclick="popWait();">Ver abonos</html:link>
																					</smx:notEqual>
																					</logic:notEmpty>
																					<%-- Cuando se va ha registrar un abono --%>
																					<logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
																						<html:link action="registrarAbonoPedido" paramId="indice" paramName="numeroRegistro" title="Registrar abono" onclick="popWait();">Abonar</html:link>
																					</logic:empty>
																				</td>
																			</tr>
																		</logic:iterate>
																	</table>
																</div>
															</td>
														</tr>
													</logic:notEmpty>
													<logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
														<tr>
															<td>
																<div id="div_listado" style="width:100%;height:400px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">                                                            
																	<table border="0" cellspacing="0" width="98%" class="tabla_informacion" cellpadding="1" cellspacing="0">
																		<logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="pedido" indexId="numeroRegistro">
																			<bean:define id="indice" value="${numeroRegistro + registrarAbonoPedidoForm.start}"/>
																			<%--------- control del estilo para el color de las filas --------------%>
																			<bean:define id="residuo" value="${numeroRegistro % 2}"/>
																			<bean:define id="fila" value="${numeroRegistro + 1}"/>
																			<logic:equal name="residuo" value="0">
																				<bean:define id="colorBack" value="blanco10"/>
																			</logic:equal>
																			<logic:notEqual name="residuo" value="0">
																				<bean:define id="colorBack" value="grisClaro10"/>
																			</logic:notEqual>
																			<%--------------------------------------------------------------------%>
																			<tr class="${colorBack}">
																				<td class="columna_contenido fila_contenido" align="center" width="4%">																			
																					<bean:define id="total" value="${pedido.totalPedido-pedido.abonoPedido}"/>																			
																					<bean:define id="codigoEstado" value="${pedido.codigoEstadoPagado}"/>																				
																					 <c:choose>
																						<c:when test='${(total < 0.0)&&(codigoEstado == "PTO" ||codigoEstado == "LQD")}'>
																							<img src="images/aprobarSolicitud.gif" border="0" title="Pedido con nota de cr&eacute;dito">
																						</c:when>
																						<c:when test='${(total > 0.0)&&(codigoEstado == "PTO"||codigoEstado == "LQD")}'>
																							<img src="images/advertencia_16.gif" border="0" title="El pago realizado no es el correcto reenv&iacute;e el abono">
																						</c:when>
																						<c:when test='${total == 0.0 && codigoEstado == "PTO"}'>
																							<img src="images/exito_16.gif" border="0" title="Pedido sin problema en los abonos">
																						</c:when>
																						<c:when test='${total == 0.0 && codigoEstado == "LQD"}'>																						
																							<img src="images/aprobarFactura.gif" border="0" title="El pedido ya se encuentra facturado">
																						</c:when>
																						<c:otherwise>
																							<img src="images/info_16.gif" border="0" title="El Pedido no se encuentra pagado totalmente">
																						</c:otherwise>
																					</c:choose>
																				</td>
																				<td class="columna_contenido fila_contenido" align="center" width="4%">${fila}</td>
																				<%--<td class="columna_contenido fila_contenido" align="center" width="14%"><html:link href="#" onclick="mypopup('detalleEstadoPedido.do?indice=${indice}','ESTDETPED')"><bean:write name="pedido" property="id.codigoPedido"/></html:link></td>--%>
																				<td class="columna_contenido fila_contenido" align="center" width="14%"><html:link title="Detalle del pedido" action="detalleEstadoPedido" paramId="indice" paramName="indice" onclick="popWait();"><bean:write name="pedido" property="id.codigoPedido"/></html:link></td>
																				<td class="columna_contenido fila_contenido" align="center" width="8%">&nbsp;<bean:write name="pedido" property="llaveContratoPOS"/></td>
																				<td class="columna_contenido fila_contenido" width="28%" align="left"><bean:write name="pedido" property="contactoEmpresa"/></td>
																				<td class="columna_contenido fila_contenido" width="12%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${pedido.totalPedido}"/></td>
																				<td class="columna_contenido fila_contenido" width="15%" align="left">&nbsp;
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						<bean:message key="label.codigoEstadoPSP"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
																						<bean:message key="label.codigoEstadoPPA"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
																						<bean:message key="label.codigoEstadoPTO"/>
																					</smx:equal>
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																						<bean:message key="label.codigoEstadoLQD"/>
																					</smx:equal>
																				</td>
																				<td class="columna_contenido fila_contenido" width="15%" align="center">
																					<logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
																					<smx:equal name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						 Pedido sin Abonos
																					</smx:equal>
																					<smx:notEqual name="pedido" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
																						 <html:link action="reenviarModificarAbonos" paramId="indice" paramName="numeroRegistro" title="Ver los abonos del pedido" onclick="popWait();">Ver abonos</html:link>
																					</smx:notEqual>
																					</logic:notEmpty>
																					<%-- Cuando se va ha registrar un abono --%>
																					<logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
																						<html:link action="registrarAbonoPedido" paramId="indice" paramName="numeroRegistro" title="Registrar Abono" onclick="popWait();">Abonar</html:link>
																					</logic:empty>
																				</td>
																			</tr>
																		</logic:iterate>
																	</table>
																</div>
															</td>
														</tr>
													</logic:empty>
                                                </logic:present>
                                                <logic:notPresent name="ec.com.smx.sic.sispe.pedidos.subPagina">
                                                    <tr>
                                                        <td colspan="5">
                                                            Seleccione un criterio de b&uacute;squeda
                                                        </td>
                                                    </tr>
                                                </logic:notPresent>
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
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>