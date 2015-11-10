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
                                                    <td height="23" width="100%">&nbsp;Detalles por proveedor</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr><td height="5px"></td></tr>
                                    <tr>
                                        <td>
                                            <div id="resultadosBusqueda">
                                                <table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.coleccion.perecibles">
                                                        <tr>
                                                            <td colspan="7" align="right">
                                                                <smx:paginacion start="${vformName.start}" range="${vformName.range}" results="${vformName.size}" styleClass="textoNegro11" url="recotizarReservar.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
                                                            </td>
                                                        </tr> 
                                                        <tr>
                                                            <td colspan="7">
                                                                <table border="0" cellspacing="0" width="100%" class="tabla_informacion">	
                                                                    <tr class="tituloTablas">
                                                                        <td class="columna_contenido" width="2%" align="center">
																			<div id="XdesplegarC00" style="display:none" >
																				<a title="Mostrar todos los articulos" href="javascript:mostrarOcultarTodos('block','marcoC');mostrarOcultarTodos('none','XdesplegarC');mostrarOcultarTodos('block','XplegarC');">
																					<html:img src="images/desplegar.gif" border="0"/>
																				</a>
																			</div>
																			<div id="XplegarC00">
																				<a title="Ocultar todos los articulos"  href="javascript:mostrarOcultarTodos('none','marcoC');mostrarOcultarTodos('none','XplegarC');mostrarOcultarTodos('block','XdesplegarC');">
																					<html:img src="images/plegar.gif" border="0"/>
																				</a>
																			</div>
																		</td>
																		<td class="columna_contenido" width="4%" align="center">No</td>
																		<td class="columna_contenido" width="40%" align="left">&nbsp;&nbsp;&nbsp;&nbsp;Proveedor</td>
																		
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                                                    <table border="0" cellspacing="0" width="100%" class="tabla_informacion">
                                                                        <logic:iterate name="ec.com.smx.sic.sispe.coleccion.perecibles" id="pereciblesDTO" indexId="numeroRegistro">
                                                                          	<bean:define id="indiceGlobal" value="${numeroRegistro}"/>
																			<bean:define id="fila" value="${indiceGlobal + 1}"/>
																			<%-- control del estilo para el color de las filas --%>
																			<bean:define id="residuo" value="${indicePedido % 2}"/>
																			<logic:equal name="residuo" value="0">
																				<bean:define id="colorBack" value="blanco10"/>
																			</logic:equal>
																			<logic:notEqual name="residuo" value="0">
																				<bean:define id="colorBack" value="grisClaro10"/>
																			</logic:notEqual>
																			
																			<c:set var="displayC1" value="none"/>
					                                                         <c:set var="displayC2" value="block"/>
                                                                            <tr> 
                                                                                <td id="td_perecibles_${numeroRegistro}">
                                                                                	<table border="0" cellpadding="0" cellspacing="0" width="100%">
																						<tr class="${colorBack}">
																							<td class="columna_contenido fila_contenido" width="2%" align="center">
																								<div id="XdesplegarC${numeroRegistro}" style="display:${displayC1}">
																									<a title="Mostrar articulos" href="javascript:hide(['XdesplegarC${numeroRegistro}']);show(['XplegarC${numeroRegistro}','marcoC${numeroRegistro}']);">
																										<html:img src="images/desplegar.gif" border="0"/>
																									</a>
																								</div>
																								<div id="XplegarC${numeroRegistro}" style="display:${displayC2}">
																									<a title="Ocultar articulos" href="javascript:show(['XdesplegarC${numeroRegistro}']);hide(['XplegarC${numeroRegistro}','marcoC${numeroRegistro}']);">
																										<html:img src="images/plegar.gif" border="0"/>
																									</a>
																								</div>
																							</td>
																							<td class="columna_contenido fila_contenido" width="4%" align="center"><b><bean:write name="fila"/></b></td>
																							<td width="40%" class="columna_contenido fila_contenido" align="left">
																								<b><bean:write name="pereciblesDTO" property="proveedor.nombreProveedor"/></b>
																							</td>
																							
																						</tr>
																					</table>
                                                                                </td>
                                                                            </tr>
                                                                                
                                                                                
                                                                            <tr>
																				<td align="center">
																					<div id="marcoC${numeroRegistro}" style="display:${displayC2};margin-left:3%;">
																					
																						
																						<bean:define name="pereciblesDTO" property="size" id="sizeCol" />
																					<!--	Size: <c:out value="${sizeCol}"/> -->
																						<logic:notEmpty name="pereciblesDTO" property="articulosPereciblesCol">
																						
																							
																							<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																								<tr class="tituloTablasCeleste">
																									<td width="9%" class="columna_contenido" align="center">C&oacute;digo barras</td>
																									<td width="35%" class="columna_contenido" align="center">Descripci&oacute;n</td>
																									<td width="8%" class="columna_contenido" align="center">Stock N.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock V.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock D. L.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock V. R.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock V. D.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock L. R.</td>
																									<td width="8%" class="columna_contenido" align="center">Stock L. D.</td>
																								</tr>
																							
																						
																							<c:set var="estiloArticulos" value=""/>
																							<c:if test="${sizeCol>13}" >
																								<c:set var="estiloArticulos" value="height:200px;overflow-y:auto;overflow-x:hidden;"/>
																							</c:if>
																							
																							<div style="${estiloArticulos}" >
																							
																							<!--	<tr class="tituloTablasCeleste">
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">C&oacute;digo Barras</td>
																									<td width="40%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Descripci&oacute;n</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Stock N.</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Stock V.</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Stock D. L.</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Stock V. R.</td>
																									<td width="10%" class="fila_contenido_negro columna_contenido_der_negro" align="center">Stock L. R.</td>
																								</tr> -->
																								<logic:iterate name="pereciblesDTO" property="articulosPereciblesCol" id="articuloPer" indexId="indiceArticulo">
																									<%-- control del estilo para el color de las filas --%>
																									<bean:define id="residuoR" value="${indiceArticulo % 2}"/>
																									<logic:equal name="residuoR" value="0">
																										<bean:define id="colorBack2" value="blanco10"/>
																									</logic:equal>
																									<logic:notEqual name="residuoR" value="0">
																										<bean:define id="colorBack2" value="amarilloClaro10"/>
																									</logic:notEqual>
																									<tr class="${colorBack2} textoNegro10">
																										<td  width="9%" class="columna_contenido fila_contenido" width="8%" align="center">
																											<bean:write name="articuloPer" property="articulo.codigoBarrasActivo.id.codigoBarras"/>
																										</td>
																										<td width="35%" align="left" class="columna_contenido fila_contenido">
																											<html:link  onclick="requestAjax('enviarOCPerecibles.do',['div_pagina','mensajes'],{parameters:'obtenerDetalleArticulo=ok&indiceProveedor=${numeroRegistro}&indiceArticulo=${indiceArticulo}'});"
																												 title="Mostrar detalle de movimientos"  href="#">
																												<bean:write name="articuloPer" property="articulo.descripcionArticulo"/>
																											</html:link>
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockNegociado">
																												&nbsp;<bean:write name="articuloPer" property="stockNegociado" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockNegociado">
																												&nbsp;0
																											</logic:empty>
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockDisponibleReservas">
																												&nbsp;<bean:write name="articuloPer" property="stockDisponibleReservas" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockDisponibleReservas">
																												&nbsp;0
																											</logic:empty>																											
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockDisponibleLocales">
																												&nbsp;<bean:write name="articuloPer" property="stockDisponibleLocales" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockDisponibleLocales">
																												&nbsp;0
																											</logic:empty>																											
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockReservadoReservas">
																												&nbsp;<bean:write name="articuloPer" property="stockReservadoReservas" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockReservadoReservas">
																												&nbsp;0
																											</logic:empty>	
																																																						
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockDespachadoReservas">
																												&nbsp;<bean:write name="articuloPer" property="stockDespachadoReservas" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockDespachadoReservas">
																												&nbsp;0
																											</logic:empty>	
																																																						
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockReservadoLocales">
																												&nbsp;<bean:write name="articuloPer" property="stockReservadoLocales" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockReservadoLocales">
																												&nbsp;0
																											</logic:empty>	
																																																		
																										</td>
																										<td width="8%" align="center" class="columna_contenido fila_contenido">
																											<logic:notEmpty name="articuloPer" property="stockDespachadoLocales">
																												&nbsp;<bean:write name="articuloPer" property="stockDespachadoLocales" formatKey="formatos.enteros"/>
																											</logic:notEmpty>
																											<logic:empty name="articuloPer" property="stockDespachadoLocales">
																												&nbsp;0
																											</logic:empty>	
																																																		
																										</td>
																									</tr>
																								</logic:iterate>
																							
																							</div>
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
