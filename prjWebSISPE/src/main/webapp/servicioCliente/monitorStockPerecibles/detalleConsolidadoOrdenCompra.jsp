<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

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
                                                    <td height="23" width="100%">&nbsp;Detalles por pedido</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr><td height="5px"></td></tr>
                                    <tr>
                                        <td>
                                            <div id="resultadosBusqueda">
                                                <table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.coleccion.consolidado.pedido">
														
                                                        <tr>
                                                            <td colspan="7" align="right">
                                                                <smx:paginacion start="${vformName.start}" range="${vformName.range}" results="${vformName.size}" styleClass="textoNegro11" url="recotizarReservar.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
                                                            </td>
                                                        </tr> 
                                                        <tr>
                                                            <td>
                                                                <table border="0" cellspacing="0" width="100%" class="tabla_informacion">	
                                                                    <tr class="tituloTablas">
                                                                        <td class="columna_contenido" width="3%" align="center">
																		
																			<div id="XdesplegarC00" style="display:none" >
																				<a title="Mostrar todos los locales" href="javascript:mostrarOcultarTodos('block','marcoC');mostrarOcultarTodos('none','XdesplegarC');mostrarOcultarTodos('block','XplegarC');">
																					<html:img src="images/desplegar.gif" border="0"/>
																				</a>
																			</div>
																			<div id="XplegarC00">
																				<a title="Ocultar todos los locales"  href="javascript:mostrarOcultarTodos('none','marcoC');mostrarOcultarTodos('none','XplegarC');mostrarOcultarTodos('block','XdesplegarC');">
																					<html:img src="images/plegar.gif" border="0"/>
																				</a>
																			</div>
																		</td>
																		<td class="columna_contenido" width="4%" align="center">No</td>
																		<td class="columna_contenido" width="10%" align="center">C&oacute;digo barras</td>
																		<td class="columna_contenido" width="20%" align="center">Descripci&oacute;n</td>
																		<td class="columna_contenido" width="6%" align="center">Tamaño</td>
																		<td class="columna_contenido" width="10%" align="center">Fecha Despacho</td>
																		<td class="columna_contenido" width="12%" align="center">Consolidado</td>
																		
																		
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                                                    <table border="0" cellspacing="0" width="100%" class="tabla_informacion">
                                                                        <logic:iterate name="ec.com.smx.sic.sispe.coleccion.consolidado.pedido" id="articulo" indexId="numeroRegistro">
                                                                          	
																			<bean:define id="indiceGlobal" value="${numeroRegistro}"/>
																			<bean:define id="fila" value="${indiceGlobal + 1}"/>
																			<%-- control del estilo para el color de las filas --%>
																			<bean:define id="residuo" value="${indicePedido % 2}"/>
																			<c:set var="clase" value="blanco10"/>
																			<logic:notEqual name="residuo" value="0">
																				<c:set var="clase" value="grisClaro10"/>
																			</logic:notEqual>
																			
																			
																			<c:set var="displayC1" value="none"/>
					                                                         <c:set var="displayC2" value="block"/>
                                                                            <tr class="blanco10"> 
                                                                                <td id="td_perecibles_${numeroRegistro}">
                                                                                	<table border="0" cellpadding="0" cellspacing="0" width="100%">
																						<tr>
																							<td class="columna_contenido fila_contenido" width="3%" align="center">
																								<div id="XdesplegarC${numeroRegistro}" style="display:${displayC1}">
																									<a title="Mostrar locales" href="javascript:hide(['XdesplegarC${numeroRegistro}']);show(['XplegarC${numeroRegistro}','marcoC${numeroRegistro}']);">
																										<html:img src="images/desplegar.gif" border="0"/>
																									</a>
																								</div>
																								<div id="XplegarC${numeroRegistro}" style="display:${displayC2}">
																									<a title="Ocultar locales" href="javascript:show(['XdesplegarC${numeroRegistro}']);hide(['XplegarC${numeroRegistro}','marcoC${numeroRegistro}']);">
																										<html:img src="images/plegar.gif" border="0"/>
																									</a>
																								</div>
																							</td>
																							<td class="columna_contenido fila_contenido" width="4%" align="center"><b><bean:write name="fila"/></b></td>
																							<td width="10%" class="columna_contenido fila_contenido" align="center">
																								<b><bean:write name="articulo" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/></b>
																							</td>
																							<td width="20%" class="columna_contenido fila_contenido" align="left">
																								&nbsp;&nbsp;<b><bean:write name="articulo" property="articuloDTO.descripcionArticulo"/></b>
																							</td>
																							
																							<td width="6%" class="columna_contenido fila_contenido" align="center">
																								<logic:empty name="articulo" property="articuloDTO.articuloMedidaDTO.referenciaMedida">
																									<b>ND</b>
																								</logic:empty>
																								
																								<logic:notEmpty name="articulo" property="articuloDTO.articuloMedidaDTO.referenciaMedida">
																									<b><bean:write name="articulo" property="articuloDTO.articuloMedidaDTO.referenciaMedida"/> </b>
																								</logic:notEmpty>
																							</td>
																							
																							<td width="10%" class="columna_contenido fila_contenido" align="center">
																								<b><bean:write name="articulo" property="fechaDespachoBodega" formatKey="formatos.fecha" /></b>
																							</td>
																							
																							<td width="12%" class="columna_contenido fila_contenido" align="right" style="padding-right:9%;">
																								<b><bean:write name="articulo" property="cantidadConsolidado"/></b>
																							</td>
																							
																						</tr>
																					</table>
                                                                                </td>
                                                                            </tr>
                                                                                
                                                                                
                                                                            <tr>
																				<td align="right">
																					<div id="marcoC${numeroRegistro}" width="96%" style="display:${displayC2};padding-right:30px;">
																						<logic:notEmpty name="articulo" property="pedidoLocalesCol">
																							<table width="96%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																								<tr class="tituloTablasCeleste">
																									<td width="20%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Local</td>
																									<td width="30%" class="fila_contenido_negro columna_contenido_der_negro" align="center">No Pedido</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">No Reserva</td>
																									<td width="13%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Cantidad</td>
																									
																								</tr>
																								<logic:iterate name="articulo" property="pedidoLocalesCol" id="pedido" indexId="indicePedido">
																									<%-- control del estilo para el color de las filas --%>
																									<bean:define id="residuoR" value="${indicePedido % 2}"/>
																									<logic:equal name="residuoR" value="0">
																										<bean:define id="colorBack2" value="blanco10"/>
																									</logic:equal>
																									<logic:notEqual name="residuoR" value="0">
																										<bean:define id="colorBack2" value="amarilloClaro10"/>
																									</logic:notEqual>
																									<tr class="${colorBack2} textoNegro10">
																										<td class="columna_contenido fila_contenido" width="20%" align="left">																										
																											<bean:write name="pedido" property="pedidoDTO.areaTrabajoDTO.id.codigoAreaTrabajo"/> - 																												
																											<bean:write name="pedido" property="pedidoDTO.areaTrabajoDTO.nombreAreaTrabajo"/>																												
																										</td>
																										<td width="30%" align="center" class="columna_contenido fila_contenido">
																											<bean:define id="codigoPedidoBuscar" name="pedido" property="pedidoDTO.id.codigoPedido" />
																											<html:link  onclick="requestAjax('enviarOCPerecibles.do',['div_pagina','mensajes'],{parameters:'mostrarDetallePedido=ok&indiceArticulo=${numeroRegistro}&indicePedido=${indicePedido}'});"
																												 title="Mostrar detalle pedido"  href="#">
																												<bean:write name="pedido" property="pedidoDTO.id.codigoPedido"/>
																											</html:link>
																											
																										</td>
																										<td width="10%" align="center" class="columna_contenido fila_contenido">
																											<logic:empty name="pedido" property="numeroReserva">
																												NA
																											</logic:empty>
																											
																											<logic:notEmpty name="pedido" property="numeroReserva">
																												<bean:write name="pedido" property="numeroReserva"/>
																											</logic:notEmpty>
																										</td>
																										<td width="13%" align="right" class="columna_contenido fila_contenido" style="padding-right:6%">
																											<logic:notEmpty name="pedido" property="cantidad">																											
																													&nbsp;<bean:write name="pedido" property="cantidad" formatKey="formatos.enteros"/>																											
																											</logic:notEmpty>
																											<logic:empty name="pedido" property="cantidad">
																												&nbsp;0
																											</logic:empty>																											
																										</td>
																										
																									</tr>
																								</logic:iterate>
																							</table>
																						</logic:notEmpty>
																					</div>
																				</td>
																			</tr> 
                                                                        </logic:iterate>        
                                                                    </table>          
                                                                </div> 
															</td>	
                                                        </tr>
                                                        <tr>
															<td  height="5px"></td>
														</tr>
                                                    </logic:notEmpty>
                                                    <logic:empty name="ec.com.smx.sic.sispe.coleccion.perecibles">
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
