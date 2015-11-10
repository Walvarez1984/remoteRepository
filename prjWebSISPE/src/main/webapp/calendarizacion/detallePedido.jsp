<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta name="GENERATOR" content="IBM Software Development Platform">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <link href="css/textos.css" rel="stylesheet" type="text/css">
        <link href="css/componentes.css" rel="stylesheet" type="text/css">
        <link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
        <%----- solo cuando se imprime el estado del detalle del pedido -------%>
        <link rel=alternate media=print href="reportes/estadoDetallePedido/rptEstadoDetallePedidoTexto.jsp?id=<%=(new java.util.Date()).getTime()%>">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
        <title>
            Detalle del pedido No:
            <logic:notEmpty name="ec.com.smx.sic.sispe.estadoPedidoDTO">
                <bean:write name="ec.com.smx.sic.sispe.estadoPedidoDTO" property="id.codigoPedido"/>
            </logic:notEmpty>
        </title>
    </head>
    
    <body>
    	<bean:define id="tipoArticuloPiezaPesoUnidadManejo"><bean:message key="ec.com.smx.sic.sispe.articulo.tipoControlCosto"/></bean:define>
		<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasto">
			<bean:define id="tipoArticuloCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasto"/>
		</logic:notEmpty>
		<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
			<bean:define id="tipoArticuloDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
		</logic:notEmpty>

        <bean:define id="estadoActivo" name="sispe.estado.activo"/>
        <bean:define id="tipoArticuloPavo"><bean:message key="ec.com.smx.sic.sispe.articulo.pavo"/></bean:define>
        <bean:define id="tipoArticuloOtrosPesoVariable"><bean:message key="ec.com.smx.sic.sispe.articulo.otroPesoVariable"/></bean:define>
        <bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
		<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
        
        <TABLE border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
            <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
            <tr>
                <td align="left" valign="top" width="100%">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                        <tr>
                            <td  width="3%" align="center"><img src="images/detalle_pedidos.gif" border="0"></img></td>
                            <td height="35" valign="middle">Detalle del pedido seleccionado</td>
                            <td align="right">
                                <table border="0">
                                    <tr>
                                        <%--<td>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.estadoPedidoDTO">
                                                <div id="botonA">	
                                                    <html:link href="#" onclick="window.print()" styleClass="imprimirA" >Imprimir</html:link>
                                                </div>
                                            </logic:notEmpty>
                                        </td>--%>
                                        <td>
                                            <div id="botonA">	
                                                <html:link href="#" onclick="window.close()" styleClass="cancelarA" >Cancelar</html:link>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <tiles:insert page="/include/mensajes.jsp"/>
                </td>
            </tr>
            <tr><td height="5px"></td></tr>
            <tr>
                <td align="center"  valign="top">
                    <logic:notEmpty name="ec.com.smx.sic.sispe.estadoPedidoDTO">
                        <form action="detalleEstadoPedido">
                            <table border="0" cellpadding="0" cellspacing="0" class="textoNegro11" width="100%">
		                    	<tr>
									<td colspan="2" width="100%" class="textoAzul11" bgcolor="#F4F5EB">
										<table border="0" cellspacing="0" width="100%" align="left">
											<tr>
												<td colspan="2">
													<table cellpadding="1" cellspacing="0" width="100%">
														<tr>
															<td></td>
															<td class="textoNegro11" width="30%" align="left">
																<label class="textoAzul11">No Pedido:&nbsp;</label>
																<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
																<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoConsolidado">
																<b><label class="textoRojo11">
																	(CONSOLIDADO)
																</label></b>
																</logic:notEmpty>
															</td>
															<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS">
																<td align="left" class="textoNegro11" width="20%">
																	<label class="textoAzul11">No Reservaci&oacute;n:&nbsp;</label>
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="llaveContratoPOS"/>
																</td>
															</logic:notEmpty>
															<td align="left" class="textoNegro11" width="20%">
																<label class="textoAzul11">Entidad Responsable:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
																	<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
																</logic:equal>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
																	<bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
																</logic:equal>
															</td>
															<td align="left" class="textoNegro11" width="15%">
																<label class="textoAzul11">Precios afiliado:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">Si</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPreciosAfiliado" value="${estadoActivo}">No</logic:notEqual>
															</td>
															<td align="left" class="textoNegro11" width="45%">
																<label class="textoAzul11">Pago efectivo:&nbsp;</label>
																<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">Si</logic:equal>
																<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="pagoEfectivo" value="${estadoActivo}">No</logic:notEqual>
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado">
												<tr>
													<td class="textoAzul11" width="18%" align="right">Pedido Relacionado:</td>
													<td align="left" class="textoNegro11" width="80%">
														<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="codigoPedidoRelacionado"/>
													</td>
												</tr>
											</logic:notEmpty>
											
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreEmpresa">
												<tr>
													<td class="textoAzul11" align="right" width="12%">Empresa:</td>
													<td class="textoNegro11" align="left" width="80%">
															<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
													</td>
												</tr>																				
											</logic:notEmpty>
											
											<!-- PARA EL CASO DE DATOS DE PERSONA -->
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombrePersona">
												<tr>
													<td class="textoAzul11" align="right" width="12%">Cliente:</td>
													<td class="textoNegro11" align="left" width="80%">
															<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoEmpresa"/>																			
													</td>
												</tr>
											</logic:notEmpty>	
											
											
												<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->					
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto">
													<tr>
														<td class="textoAzul11" align="right" width="12%">Contacto:</td>																																		
														<td class="textoNegro11" align="left" width="80%">
															<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="contactoCliente"/>																			
														</td>																									
													</tr>
												</logic:notEmpty>
																		
											<tr>
												<td class="textoAzul11" align="right">Elaborado en:</td>
												<td class="textoNegro11" align="left">
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
												</td>
											</tr>
											<tr>
												<td class="textoAzul11" align="right">Fecha de Estado:</td>
												<td class="textoNegro11" align="left">
													<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<table cellpadding="0" cellspacing="0" >
														<tr>
															<bean:define id="codigoEstadoReserva"><bean:message key="ec.com.smx.sic.sispe.estado.reservado"/></bean:define>
															<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
																<td class="textoAzul11" align="right" width="13%">Estado:</td>
																<td class="textoRojo11" align="left" width="18%">
																	<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
																</td>
																<td class="textoAzul11" align="right" width="5%">Etapa:</td>
																<td class="textoNegro11" align="left">
																	<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="etapaEstadoActual"/>
																</td>
															</logic:notEqual>
															<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${codigoEstadoReserva}">
																<td class="textoAzul11" align="right" width="13%">Estado:</td>
																<td class="textoRojo11" align="left" width="18%">
																	<b><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/></b>
																</td>
																<td class="textoAzul11" align="right" width="5%">Etapa:</td>
																<td class="textoNegro11" align="left">
																	<bean:define id="pedidoReservadoPCO"><bean:message key="estado.pedidoReservado.pendienteConfirmar"/></bean:define>
																	<bean:define id="pedidoReservadoCON"><bean:message key="estado.pedidoReservado.confirmado"/></bean:define>
																	<bean:define id="pedidoReservadoEXP"><bean:message key="estado.pedidoReservado.expirado"/></bean:define>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoPCO}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.PCO"/></bean:define>
																	</logic:equal>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoCON}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.CON"/></bean:define>
																	</logic:equal>
																	<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="estadoPedidoReserva" value="${pedidoReservadoEXP}">
																		<bean:define id="descripcionEstadoReserva"><bean:message key="label.pedidoReservado.EXP"/></bean:define>
																	</logic:equal>
																	<bean:write name="descripcionEstadoReserva"/>
																</td>
															</logic:equal>
														</tr>
													</table>
												</td>
											</tr>
											<bean:define id="vistaPedidoDTO" name="ec.com.smx.sic.sispe.vistaPedido"/>
											<c:choose>
												<c:when test="${vistaPedidoDTO.id.codigoEstado != estadoCotizado && vistaPedidoDTO.id.codigoEstado != estadoRecotizado}">
													<tr>
														<td class="textoAzul11" align="right">Valor Abono Inicial:</td>
														<td>
															<table cellpadding="0" cellspacing="0" align="left" class="textoNegro11">
																<tr>
																	<td>
																		<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.valorAbonoInicialManual}"/>
																	</td>
																	<td align="left">
																		<label class="textoAzul11">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Valor Abonado:</label>&nbsp;<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.abonoPedido}"/>
																	</td>
																	<td align="left">
																		<logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																			<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(Enviado al CD)</label>
																		</logic:equal>
																		<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="reservarBodegaSIC" value="${estadoActivo}">
																			<label class="textoRojo11">&nbsp;&nbsp;&nbsp;&nbsp;(No Enviado al CD)</label>
																		</logic:notEqual>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</c:when>
											</c:choose>
										</table>
									</td>
								</tr>
                            <%--
                                <tr>
                                    <td>	
                                        <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="tabla_informacion">
                                            <tr class="fila_titulo">
                                                <td colspan="4" class="textoNegro11 fila_contenido" align="left"><b>Datos de la cabecera:</b></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" width="100%" class="textoAzul11">	
                                                    <table border="0" cellspacing="0" width="100%" align="left">
                                                        <tr>
                                                            <td class="textoAzul11" width="17%" align="right">No Pedido:</td>
                                                            <td align="left" class="textoNegro11">
                                                                <bean:write name="ec.com.smx.sic.sispe.estadoPedidoDTO" property="id.codigoPedido"/>	
                                                            </td>  
                                                        </tr>
                                                    																		
                                                        <tr>
                                                            <td class="textoAzul11" align="right">Fecha de Creaci&oacute;n:</td>
                                                            <td class="textoNegro11" align="left">
                                                                <bean:write name="ec.com.smx.sic.sispe.estadoPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </TABLE>
                                    </td>
                                </tr>
                                --%>
                                <tr height="10"><td colspan="6"></td></tr>
                                <tr>
                                    <td>
                                        <table width="98%" border="0"  cellpadding="0" cellspacing="0" class="tabla_informacion">
											<logic:empty name="ec.com.smx.sic.sispe.entregaDTOCol">
												<tr>
													<td>
														<table class="textoNegro11" cellpadding="0" cellspacing="0"	align="left" width="100%">
															<tr>
																<td width="100%" height="400px"> 
																	<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" >
																		<tr>						
																			<td align="center" class="textoNegro11">
																			<bean:define id="areaTrabajoPedido" name="ec.com.smx.sic.sispe.codigo.area.trabajo"/>
																			<bean:define id="nombreAreaTrabajoPedido" name="ec.com.smx.sic.sispe.nombre.area.trabajo"/>
																				No existen art&iacute;culos para el area de trabajo
																				${areaTrabajoPedido} - ${nombreAreaTrabajoPedido}
																			</td>
																		</tr>
																	</table>
												
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</logic:empty>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.entregaDTOCol">
                                                <tr align="left" class="fila_titulo">
                                                    <td colspan="6" class="textoNegro11"><b>Detalle del Pedido:</b></td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <table width="100%" border="0" cellpadding="1" cellspacing="0">	
                                                            <tr class="tituloTablas">
                                                                <td width="6%" class="columna_contenido" align="center">No</td>
                                                                <td width="15%" class="columna_contenido" align="center" colspan="2">C&Oacute;DIGO BARRAS</td>
                                                                <td width="23%" class="columna_contenido" align="center">ART&Iacute;CULO</td>
                                                                <td width="8%" class="columna_contenido" align="center">CANTIDAD (UNIDAD)</td>
                                                                <td width="8%" class="columna_contenido" align="center">CANTIDAD (BULTOS)</td>
                                                                <td width="10%" class="columna_contenido" align="center">FECHA DESPACHO</td>
                                                                <td width="10%" class="columna_contenido" align="center">FECHA ENTREGA</td>
                                                                <td width="20%" class="columna_contenido" align="center">DIRECCI&Oacute;N ENTREGA</td>
                                                            </tr>
                                                            <logic:iterate name="ec.com.smx.sic.sispe.entregaDTOCol" id="entregaDTO" indexId="indiceDetalle">
                                                                <%--------- control del estilo para el color de las filas -------------%>
                                                                <bean:define id="residuo" value="${indiceDetalle % 2}"/>
                                                                <logic:equal name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="blanco10"/>
                                                                </logic:equal>	
                                                                <logic:notEqual name="residuo" value="0">
                                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                                </logic:notEqual>
                                                                <c:set var="pesoVariable" value=""/>
                                                                <logic:equal name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.tipoCalculoPrecio" value="${tipoArticuloPavo}">
                                                                    <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                </logic:equal>
                                                                <logic:equal name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.tipoCalculoPrecio" value="${tipoArticuloOtrosPesoVariable}">
                                                                    <c:set var="pesoVariable" value="${estadoActivo}"/>
                                                                </logic:equal>
                                                                <%-------------------------------------------------------------------%>
                                                                <tr class="${colorBack}">
                                                                    <bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
                                                                    <td align="center"><bean:write name="numRegistro"/></td>
                                                                    <%--<logic:equal name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoTipoArticulo" value="${tipoArticuloCanasto}"> --%>
																   <c:if test="${entregaDTO.estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoClasificacion == articuloEspecial || entregaDTO.estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoClasificacion == canastoCatalogo}">
                                                                        <td class="columna_contenido" align="left">                                                                        
                                                                            <html:link title="detalle del canasto" action="estadoDetalleCanasto" paramId="indiceEntregaKardex" paramName="indiceDetalle">
                                                                                <bean:write name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.id.codigoArticulo"/>
                                                                            </html:link>
                                                                        </td>
                                                                        <td align="right">
                                                                        
                                                                        	<bean:define id="codigoSubClasificacion" name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoSubClasificacion"/>
								                                            <c:if test="${fn:contains(tipoArticuloDespensa, codigoSubClasificacion)}">
																			  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																				<bean:define id="descripcion" value="detalle de la despensa"/>
																			</c:if>
																			<c:if test="${fn:contains(tipoArticuloCanasto, codigoSubClasificacion)}">
																			  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
								                                                <bean:define id="descripcion" value="detalle del canasto"/>
																			</c:if>
                                                                        
                                                                            <html:link title="detalle del canasto" action="estadoDetalleCanasto" paramId="indiceEntregaKardex" paramName="indiceDetalle">
                                                                                <img title="${descripcion}" src="${pathImagenArticulo}" border="0"/>
                                                                            </html:link>
                                                                        </td>
                                                                    <%--</logic:equal> --%>
                                                                    </c:if>
                                                                    <%--<logic:notEqual name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoTipoArticulo" value="${tipoArticuloCanasto}">
                                                                        <logic:equal name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoTipoArticulo" value="${tipoArticuloDespensa}">
	                                                                        <td class="columna_contenido" align="left">
	                                                                            <html:link title="detalle del canasto" action="estadoDetalleCanasto" paramId="indiceEntregaKardex" paramName="indiceDetalle">
	                                                                                <bean:write name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.id.codigoArticulo"/>
	                                                                            </html:link>
	                                                                        </td>
	                                                                        <td align="right">
	                                                                            <html:link title="detalle del canasto" action="estadoDetalleCanasto" paramId="indiceEntregaKardex" paramName="indiceDetalle">
	                                                                                <img src="./images/canasto_lleno.gif" border="0">
	                                                                            </html:link>
	                                                                        </td>
	                                                                    </logic:equal>
	                                                                    <logic:notEqual name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoTipoArticulo" value="${tipoArticuloDespensa}">
	                                                                        <td class="columna_contenido" align="left">
	                                                                            <bean:write name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.id.codigoArticulo"/>
	                                                                        </td>
	                                                                        <td>
	                                                                            <logic:equal name="pesoVariable" value="${estadoActivo}">
	                                                                                <img title="peso variable" src="./images/balanza.gif" border="0">
	                                                                            </logic:equal>
	                                                                            <logic:notEqual name="pesoVariable" value="${estadoActivo}">&nbsp;</logic:notEqual>
	                                                                        </td>
	                                                                	</logic:notEqual>
                                                                    </logic:notEqual>--%>
                                                                    <c:if test="${entregaDTO.estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoClasificacion != articuloEspecial && entregaDTO.estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.codigoClasificacion != canastoCatalogo}">
																		 <td class="columna_contenido" align="left">
	                                                                            <bean:write name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.id.codigoArticulo"/>
	                                                                        </td>
                                                                    	<td align="right">
																			<logic:equal name="pesoVariable" value="${estadoActivo}">
																				<logic:equal name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
									                                             	<img title="peso variable/unidad Manejo" src="images/balanza.gif" border="0">
									                                            </logic:equal>
									                                            <logic:notEqual name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.articuloComercialDTO.valorTipoControlCosto" value="${tipoArticuloPiezaPesoUnidadManejo}">
									                                                <img title="peso variable" src="images/pavo.gif" border="0">
									                                            </logic:notEqual> 
									                                        </logic:equal> 
																		</td>
                                                                    </c:if>
                                                                    
                                                                    <td class="columna_contenido" align="left">
                                                                        -<bean:write name="entregaDTO" property="estadoDetallePedidoDTO.detallePedidoDTO.articuloDTO.descripcionArticulo"/>
                                                                    </td>
                                                                    <td class="columna_contenido" align="right"><bean:write name="entregaDTO" property="cantidadDespacho"/></td>
                                                                    <td class="columna_contenido" align="right"><bean:write name="entregaDTO" property="npCantidadBultosDespachoCalculados"/></td>
                                                                    <td class="columna_contenido" align="center"><bean:write name="entregaDTO" property="entregaPedidoDTO.fechaDespachoBodega" formatKey="formatos.fecha"/></td>
                                                                    <td class="columna_contenido" align="center"><bean:write name="entregaDTO" property="entregaPedidoDTO.fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
                                                                    <td class="columna_contenido" align="center"><bean:write name="entregaDTO" property="entregaPedidoDTO.direccionEntrega"/></td>
                                                                </tr>
                                                            </logic:iterate>
                                                        </table>             
                                                    </td>
                                                </tr>
                                            </logic:notEmpty>
                                        </table>
                                    </td>
                                </tr>
                            </table>	
                        </form>
                    </logic:notEmpty>
                </td>
            </tr>
        </TABLE>
    </BODY>
</HTML>