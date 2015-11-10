<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<div id="busquedapopup" class="popup" style="top:60px;" >
	<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="98%" align="center">
	     <tr>
	          <td background="images/barralogin.gif" align="center">
	              <table cellpadding="0" cellspacing="0" width="100%" border="0">			
	                  <tr>
	                      <td class="textoBlanco11" align="left">
	                      	&nbsp;<b>B&uacute;squeda pedidos anteriores</b>
	                      </td>
	                      <!-- Boton Cerrar -->
	                      <td align="right">
	                      	<html:link title="Cerrar" href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=cerrar', evalScripts: true});ocultarModal();" >
	                      		<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
	                      	</html:link>&nbsp;
	                      </td>		                           
	                  </tr>
	              </table>
	          </td>
	      </tr>    
          <tr>
          		<td bgcolor="#F4F5EB" valign="top">                                
               		<table class="tabla_informacion" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
                    	<tr>								   		                                                 
                        	<td>
                           		<table class="tabla_informacion fondoBlanco" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">
									<tr>
										<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
										   		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
										 	<html:hidden property="ayuda" value=""/>
												
									        <tr>
									            <td align="left" valign="top" width="100%">
									              	<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" >
									                    <tr>
									                    	<td>                                 	
																<div id="mensajesBusquedaPopup" style="font-size:1px;position:relative;">
																	<jsp:include page="/include/mensajes.jsp"/>	
																</div>
															</td>
									                    </tr>
									                </table>
									                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
									                    <tr>
									                        <td  width="3%" align="center"><img src="./images/busqueda_pedido.gif" border="0"></img></td>
									                        <td height="35" valign="middle">
									                        Pedidos anteriores
									                        </td>
									                       
									                    </tr>
									                </table>
									            </td>
									        </tr>
										    <%--Contenido de la p&aacute;gina--%>
									        <tr>
									            <td align="center" valign="top">
									                <table border="0" class="textoNegro12 fondoBlanco" width="100%" align="center" cellspacing="0" cellpadding="0">
									                    <tr><td height="5px"></td></tr>
									                    <tr>
									                        <%--Barra Izquierda--%>
									                        <td class="datos fondoBlanco" width="25%" id="izquierda" height="98%" >
									                            <div style="height:500;">
									                                <tiles:insert page="/servicioCliente/busqueda/seccionBusquedaPedAnt.jsp"/>
									                            </div>
									                        </td>
									                        <td width="1%" id="divisor">&nbsp;
									                        </td>
									                        <%--Datos--%>
									                        <td class="datos" width="100%" id="derecha">
									                            <div style="height:500px;">
									                                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									                                    <%--Titulo de los datos--%>
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
									                                                <table border="0" class="textoNegro11" width="100%" align="center" cellspacing="0" cellpadding="0">
									                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.subPagina">
									                                                        <tr>
									                                                            <td colspan="7" align="right">
									                                                                <smx:paginacion start="${cotizarRecotizarReservarForm.start}" range="${cotizarRecotizarReservarForm.range}" results="${cotizarRecotizarReservarForm.size}" styleClass="textoNegro11" url="crearCotizacion.do" campos="false" requestAjax="'mensajes','resultadosBusqueda'"/>
									                                                            </td>
									                                                        </tr> 
									                                                        <tr>
									                                                            <td colspan="7">
									                                                                <table border="0" cellspacing="0" width="97%" class="tabla_informacion">	
									                                                                    <tr class="tituloTablas">
									                                                                        <td width="4%" align="center">No</td>
									                                                                        <td class="columna_contenido" width="16%" align="center">No PEDIDO</td>
																											<td class="columna_contenido" width="10%" align="center">No RES</td>
									                                                                        <td class="columna_contenido" width="20%" align="center">CONTACTO-EMPRESA</td>
									                                                                        <td class="columna_contenido" width="11%" align="center">FECHA ESTADO</td>
									                                                                        <%--<td class="columna_contenido" width="11%" align="center">FECHA ENTREGA</td>--%>
									                                                                        <td class="columna_contenido" width="11%" align="center">ESTADO</td>
									                                                                        <td class="columna_contenido" width="9%" align="center">V. TOTAL</td>
									                                                                        
									                                                                    </tr>
									                                                                </table>
									                                                            </td>
									                                                        </tr>
									                                                        <tr>
									                                                            <td>
									                                                                <div id="div_listado" style="width:100%;height:440px;overflow:auto" height="98%">
									                                                                    <table border="0" cellspacing="0" width="97%" class="tabla_informacion" style="empty-cells: show;"	>
									                                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedidos.subPagina" id="vistaPedidoDTO" indexId="numeroRegistro">
									                                                                            <bean:define id="indice" value="${numeroRegistro + cotizarRecotizarReservarForm.start}"/>
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
									                                                                                <html:link title="Detalle del pedido" href="#" onclick="requestAjax('crearCotizacion.do', ['articulosPedAntpopUp','pregunta'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarArtPedAnt-${indice}', popWait:true, evalScripts: true});"><bean:write name="vistaPedidoDTO" property="id.codigoPedido"/></html:link>
									                                                                                </td>
									                                                                                <td class="columna_contenido fila_contenido" width="10%"  align="center">&nbsp;<bean:write name="vistaPedidoDTO" property="llaveContratoPOS"/></td>	
																													<td class="columna_contenido fila_contenido" width="20%" align="center"><bean:write name="vistaPedidoDTO" property="contactoEmpresa"/></td>	
									                                                                                <td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="fechaInicialEstado" formatKey="formatos.fecha"/></td>
									                                                                                <%--<td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="fechaMinimaEntrega" formatKey="formatos.fechahora"/></td>--%>
									                                                                                <td class="columna_contenido fila_contenido" width="11%" align="center"><bean:write name="vistaPedidoDTO" property="descripcionEstado"/></td>
									                                                                                <td class="columna_contenido fila_contenido" width="9%" align="center"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaPedidoDTO.totalPedido}"/></td>
									                                                                                
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
									                        <%--Fin Datos--%>
									                    </tr>
									                    <%--Fin P&aacute;gina--%>
									                </table>
									            </td>
									        </tr>
											<tr><td height="1px"></td></tr>
											<tr>
											   <td align="center">
													<div id="botonD">
														<html:link styleClass="cancelarD" href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'pedidosAnteriores=ok&accionesPedAnt=cerrar', evalScripts: true});ocultarModal();">Cancelar</html:link>
													</div>
												</td>
										  	</tr>
										</TABLE>
									</tr>	
								</table>	
							</td>
						</tr>
					</table>
				</td>							
			</tr>	
	</table>	
</div>