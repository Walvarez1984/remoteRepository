<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<tiles:insert page="/include/top.jsp"/>
<%---------------------- lista de definiciones para las acciones -----------------------------%>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="codigoEstadoPagadoTotalmente" name="ec.com.smx.sic.sispe.codigoEstadoPagadoTotalmente"/>
<bean:define id="codigoEstadoPagadoLiquidado" name="ec.com.smx.sic.sispe.codigoEstadoPagadoLiquidado"/>
<bean:define id="codigoEstadoPagadoSinPago" name="ec.com.smx.sic.sispe.codigoEstadoPagadoSinPago"/>
<bean:define id="codigoEstadoPagadoParcialmente" name="ec.com.smx.sic.sispe.codigoEstadoPagadoParcialmente"/>

<html:form action="registrarAbonoPedido" method="post">
	<TABLE border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
        <html:hidden property="ayuda" value=""/>
        <tr>
            <td>
                <div id="pregunta">
                	<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
                    	<jsp:include page="../../confirmacion/confirmacion.jsp"/>
                    </logic:notEmpty>
                    <logic:present name="ec.com.sxm.sic.sispe.regresarSinConfirmacion">
                        <script type="text/javascript">
                			realizarEnvio('siVolverBusqueda');
                        </script>
                    </logic:present>
                </div>
            </td>
        </tr>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="images/registroPago48.gif" border="0"></img></td>
                        <td align="left">&nbsp;&nbsp;Registro de Pago</td>						
                        <td align="right">
                            <table cellspacing="0">
                                <tr>
                                	<td>
                                        <logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
                                            <div id="botonA">
                                                <html:link styleClass="actualizarA" href="#" onclick="requestAjax('registrarAbonoPedido.do',['pregunta','mensajes','div_pagina'],{parameters: 'actualizarAbonoPedido=ok', evalScripts:true});" title="actualizar abonos">Actualizar</html:link>
                                            </div>
                                        </logic:empty>
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
                                        	<div id="botonA">
                                                <html:link styleClass="actualizarA" href="#" onclick="requestAjax('reenviarModificarAbonos.do',['pregunta','mensajes','div_pagina'],{parameters: 'actualizarAbonoPedido=ok', evalScripts:true});" title="actualizar abonos">Actualizar</html:link>
                                            </div>
                                        </logic:notEmpty>
                                    </td>
                                    <td>
										<bean:define id="codigoEstadoPagadoA" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado"/>
										<logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
											<c:choose>
												<c:when test='${codigoEstadoPagadoA==codigoEstadoPagadoSinPago}'>
													<div id="botonA">
														<html:link styleClass="guardarA" href="#" onclick="requestAjax('registrarAbonoPedido.do',['pregunta','mensajes','div_pagina'],{parameters: 'registrarAbono=ok', evalScripts:true});" title="guardar abono">Guardar</html:link>
														<!--<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('registrarAbonoPedido');" title="guardar abono">Guardar</html:link>-->
													</div> 
												</c:when>
												<c:when test='${codigoEstadoPagadoA==codigoEstadoPagadoParcialmente}'>
													<div id="botonA">
														<html:link styleClass="guardarA" href="#" onclick="requestAjax('registrarAbonoPedido.do',['pregunta','mensajes','div_pagina'],{parameters: 'registrarAbono=ok', evalScripts:true});" title="guardar abono">Guardar</html:link>
														<!--<html:link styleClass="guardarA" href="#" onclick="realizarEnvio('registrarAbonoPedido');" title="guardar abono">Guardar</html:link>-->
													</div> 
												</c:when>
											</c:choose>
										</logic:empty>
                                    </td>
                                    <td>
                                        <logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
	                                        <div id="botonA">
	                                            <html:link styleClass="cancelarA" href="#" onclick="requestAjax('registrarAbonoPedido.do',['pregunta'],{parameters: 'volverBuscar=ok', evalScripts:true});" title="volver a la p&aacute;gina de b&uacute;squeda">Cancelar</html:link>
	                                        </div>
                                        </logic:empty>
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
                                        	<div id="botonA">
	                                            <html:link styleClass="cancelarA" href="#" onclick="requestAjax('reenviarModificarAbonos.do',['div_pagina'],{parameters: 'volverBuscar=ok', evalScripts:true});" title="volver a la p&aacute;gina de b&uacute;squeda">Cancelar</html:link>
	                                        </div>
                                        </logic:notEmpty>
                                        
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="ok"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
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
                <table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
                    <tr><td height="7px" colspan="3"></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td class="datos" width="20%">
                            <table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="fila_titulo" colspan="2">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td width="18%"><img src="images/datos_informacion24.gif" border="0"/></td>
                                                <td width="82%" class="textoNegro11">Informaci&oacute;n</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                <td>
                                    <table border="0" align="left">
                                       
                                       
                                        <tr>
                                            <td class="textoAzul11" width="18%">Abono Inicial:</td>
                                            <td class="textoNegro11">
                                                <%--<bean:define name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialSistema" id="valorAbonoInicialSistema"/>--%>
                                                <%--<logic:lessEqual name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialManual" value="${valorAbonoInicialSistema}">--%>
                                                    <%--<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialManual" formatKey="formatos.numeros"/>--%>
                                                <%--</logic:lessEqual>--%>
                                                <%--<logic:greaterThan name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialManual" value="${valorAbonoInicialSistema}">--%>
                                                    <%--<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialSistema" formatKey="formatos.numeros"/>--%>
                                                <%--</logic:greaterThan>--%>
												<bean:define id="abonoInicial" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="valorAbonoInicialManual"></bean:define>
                                                <fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abonoInicial}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11">Pago:</td>
                                            <td class="textoNegro11">                                                											
												<smx:equal name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado" valueKey="codigoEstadoPagoSinPago">
													<bean:message key="label.codigoEstadoPSP"/>
												</smx:equal>
												<smx:equal name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoParcial">
													<bean:message key="label.codigoEstadoPPA"/>
												</smx:equal>
												<smx:equal name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoTotal">
													<bean:message key="label.codigoEstadoPTO"/>
												</smx:equal>
												<smx:equal name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
													<bean:message key="label.codigoEstadoLQD"/>
												</smx:equal>
												
                                            </td>
                                        </tr>
                                        
                                        <tr>
                                            <td class="textoAzul11">Total:</td>
                                            <td class="textoNegro11" align="right">
											<bean:define id="totalPedido" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="totalPedido"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalPedido}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11">Abonado:</td>
                                            <td class="textoNegro11" align="right">
											<bean:define id="abonoPedido" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="abonoPedido"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abonoPedido}"/></td>
                                        </tr>
                                        <tr>
                                            <td class="textoAzul11">Saldo:</td>
                                            <td class="textoNegro11" align="right">
											<bean:define id="saldoAbonoPedido" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="saldoAbonoPedido"></bean:define>
											<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${saldoAbonoPedido}"/></td>
                                        </tr>
                                    </table>
                                </td>
                                
                            </table>
                        </td>
                        <%--Fin Barra Izquierda--%>
    
                        <%--Separador--%>
                        <td class="datos" width="1%">&nbsp;</td>
                        <%--Fin Separador--%>
    
                        <%--Datos--%>
                        <td class="datos" width="79%">
                            <table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
                                <tr>
                                    <td>
                                        <table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
                                            <tr>
                                                <td class="fila_titulo" height="20px">
                                                    <table cellpadding="0" cellspacing="0" align="left" width="100%" border="0">
                                                        <tr>
                                                            <td align="left" width="3%"><img src="images/detalle_pedidos24.gif" border="0"></td>
                                                            <td align="left">Pedido</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table border="0" cellpadding="0" cellpadding="0" align="center" width="100%">
                                                        <tr>
                                                            <td>
                                                                <table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" class="textoAzul11" >
                                                                    <tr>
                                                                        <td align="left" width="16%">Pedido:</td>
                                                                        <td width="30%" class="textoRojo11" align="left" style="font-weight:bold;">
                                                                            <bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="id.codigoPedido"/>
                                                                        </td>
																		
																		<td width="12%">Reserva:</td>
																		<td width="30%" class="textoRojo11" style="font-weight:bold;"><bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="llaveContratoPOS"/></td>
																	
                                                                    </tr>
																	<tr>
                                                                        <td height="5px" />
																	</tr>
                                                                    <tr>
                                                                        <td align="left">Elaborado en:</td>
                                                                        <td class="textoNegro11" align="left">
                                                                            <bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="nombreLocal"/>
                                                                        </td>
                                                                    </tr>
																	<tr>
                                                                        <td height="5px" />
																	</tr>
                                                                    <tr>
                                                                        <td>Fecha de Elaboraci&oacute;n:</td>
                                                                        <td class="textoNegro11" align="left">
                                                                            <bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
                                                                        </td>
                                                                    </tr>
																	<tr>
                                                                        <td height="5px" />
																	</tr>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="nombreEmpresa">
																		<tr>
																			<td class="textoAzul11" align="left" width="12%">Empresa:</td>
																			<td class="textoNegro11" align="left" width="30%" colspan="3">
																					<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="contactoEmpresa"/>																			
																			</td>
																		</tr>
																		<tr>	
																			<td height="5px" />
																		</tr>																		
																	</logic:notEmpty>
								
																	<!-- PARA EL CASO DE DATOS DE PERSONA -->
																	<logic:notEmpty name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="nombrePersona">
																		<tr>
																			<td class="textoAzul11" align="left" width="12%">Cliente:</td>
																			<td class="textoNegro11" align="left" width="30%" colspan="3">
																					<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="contactoEmpresa"/>																			
																			</td>
																		</tr>
																		<tr>
																			<td height="5px" />
																		</tr>
																	</logic:notEmpty>	
																	
																	<!-- PARA EL CASO DE DATOS DEL CONTACTO PRINCIPAL DE LA PERSONA O EMPRESA -->														
																	<logic:notEmpty name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="nombreContacto">	
																			<tr>
																				<td class="textoAzul11" align="left" width="12%">Contacto:</td>																					
																				<td class="textoNegro11" align="left" width="30%" colspan="3">		
																						<bean:write name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="contactoCliente"/>&nbsp;																													
																				</td>	
																			</tr>	
																			<tr>
                                                                        <td height="5px" />
																			</tr>																			
																	</logic:notEmpty>									
                                                                </table>
                                                            </td>
															<td>																			
																<logic:notEmpty name="ec.com.smx.sic.sispe.registro.abono.informacion">
																	<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" >															
																		<tr>
																			<logic:equal name="ec.com.smx.sic.sispe.registro.abono.informacion" value="advertencia">
																				<td><img src="images/advertencia_48.gif" border="0"></img></td>
																				<td>El pago realizado no es el correcto reenv&iacute;e el abono</td>																			
																			</logic:equal>
																			<logic:equal name="ec.com.smx.sic.sispe.registro.abono.informacion" value="credito">
																				<td><img src="images/calificar.gif" border="0"></img></td>
																				<td>El valor del abono es superior al valor total del pedido, verifique la NOTA DE CREDITO</td>																																								
																			</logic:equal>	
																			<smx:equal name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="codigoEstadoPagado" valueKey="codigoEstadoPagadoLiquidado">
																				<td><img src="images/aprobarFactura.fw.png" border="0"></img></td>
																				<td>El pedido ya se encuentra facturado</td>
																			</smx:equal>																																																								
																		</tr>															
																	</table>
																</logic:notEmpty>
															</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr><td height="10"></td></tr>
                                
                                
                            </table>
							
                        </td>
                    </tr>
                </table>
		
				<table width="98%">
							
								<tr>
                                    <td>
                                        <table class="tabla_informacion" cellpadding="0" cellspacing="0" align="left" width="100%">
                                            <tr>
                                                <td class="fila_titulo">
                                                    <table cellpadding="0" cellspacing="0" align="left" width="100%" border="0">
                                                        <tr>
                                                            <td align="left" width="3%"><img src="images/infoAbonosPedidos.gif" border="0"></td>
                                                            <td align="left">Abonos</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table border="0" class="textoNegro11" width="99%" align="center" cellspacing="0" cellpadding="0">
                                                        <bean:size id="cantidadAbonos" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="abonosPedidos"/>
                                                        <logic:greaterThan name="cantidadAbonos" value="0">
                                                            <c:set var="totalAbonos" value="${0}"/>
                                                            <tr><td height="10"></td></tr>
                                                            <tr>
                                                                <td>
                                                                    <table border="0" cellpadding="2" cellspacing="" width="100%" class="tabla_informacion" align="left">
                                                                       <tr class="tituloTablas" align="center">
                                                                            <td width="12%">Fecha</td>
                                                                            <td class="columna_contenido" width="18%">Forma de Pago</td>
                                                                            <td class="columna_contenido" width="18%">Tipo</td>
                                                                            <td class="columna_contenido" width="9%">Cajero</td>
                                                                            <td class="columna_contenido" width="8%">Caja</td>
                                                                            <td class="columna_contenido" width="12%">Transacci&oacute;n</td>
                                                                            <td class="columna_contenido" width="12%" >Nº Factura</td>
																			<td class="columna_contenido" width="10%" >Valor</td>
                                                                        </tr> 
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <div id="div_listado" style="width:100%;height:150px;overflow-y:auto">
                                                                        <table border="0" cellspacing="0" width="98%" class="tabla_informacion" cellpadding="2" align="left"> 
                                                                            <logic:iterate id = "abono" name="ec.com.smx.sic.sispe.VistaPedidoAbonos" property="abonosPedidos" indexId="nAbono">
                                                                                <bean:define id="residuo" value="${nAbono % 2}"/>
                                                                                <logic:equal name="residuo" value="0">
                                                                                    <bean:define id="colorBack" value="blanco10"/>
                                                                                </logic:equal>
                                                                                <logic:notEqual name="residuo" value="0">
                                                                                    <bean:define id="colorBack" value="grisClaro10"/>
                                                                                </logic:notEqual>
                                                                                <tr class="${colorBack}">
                                                                                     <td class="fila_contenido" width="12%" align="center"><bean:write name="abono" property="fechaAbono" formatKey="formatos.fechahorasegundo"/></td>
                                                                                    <td class="columna_contenido fila_contenido" width="18%" align="center"><bean:write name="abono" property="formaPagoDTO.descripcionFormaPago"/></td>
                                                                                    <td class="columna_contenido fila_contenido" width="18%" align="center"><bean:write name="abono" property="tipoAbonoDTO.descripcionTipoAbono"/></td>
                                                                                    <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="abono" property="codigoCajeroAbono"/></td>
                                                                                    <td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="abono" property="codigoCajaAbono"/></td>
                                                                                    <td class="columna_contenido fila_contenido" width="12%" align="center"><bean:write name="abono" property="numeroTransaccionAbono"/></td>
                                                                                    <td id="nfactura${nAbono}"class="columna_contenido fila_contenido" width="12%" align="center">
																						&nbsp;
																						<script language="JavaScript" type="text/javascript">
																							var nf="${abono.observacionAbono}";
																							var id="nfactura"+"${nAbono}";
																							nf=nf.split("|");
																							if(nf.length>1){
																								document.getElementById(id).innerHTML=nf[0];
																							}else{
																								document.getElementById(id).innerHTML="-";
																							}
																						</script>
																					</td>
																					<td class="columna_contenido fila_contenido" width="10%" align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${abono.valorAbono}"/></td>
                                                                                </tr>
                                                                                <c:set var="totalAbonos" value="${totalAbonos + abono.valorAbono}"/>
                                                                                
                                                                            </logic:iterate>
                                                                            <tr>
                                                                                <td class="celesteClaro10" colspan="7" align="right"><b>Total: </b></td>
                                                                                <td class="celesteClaro10" align="right"><b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${totalAbonos}"/></b></td>
                                                                            </tr>
                                                                        </table>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </logic:greaterThan>
                                                        <logic:equal name="cantidadAbonos" value="0">
                                                            <tr>
                                                                <td class="textoAzul11" width="98%">
                                                                    <em>No existen abonos</em>
                                                                </td>
                                                            </tr>
                                                        </logic:equal>
                                                    </table>
                                                </td>
                                            </tr>
                                            
                                        </table>
                                    </td>
                                </tr>
                                <logic:empty name="ec.com.smx.sic.sispe.reenviarModificarAbonos">
									<c:choose>
										<c:when test='${codigoEstadoPagadoA==codigoEstadoPagadoSinPago}'>
											<tr><td height="10"></td></tr>                                    
		                                    <tr>
		                                        <td>
		                                            <table border="0" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
		                                                <tr>
		                                                    <td class="fila_titulo" height="20px">
		                                                        <table cellpadding="0" cellspacing="0" align="left" width="100%" border="0">
		                                                            <tr>
		                                                                <td align="left" width="3%"><img src="images/abono.gif" border="0"></td>
		                                                                <td align="left">Nuevo Abono</td>
		                                                            </tr>
		                                                        </table>
		                                                    </td>
		                                                </tr>
		                                                <tr>
		                                                    <td> 
		                                                        <table border="0" cellpadding="1" cellspacing="0" align="left" width="100%" class="textoAzul11">
		                                                            <tr align="left">
		                                                                <td align="left" width="15%">
		                                                                    N&uacute;mero Factura*:  
		                                                                </td>
																		<td colspan="3">
																			<table border="0" cellpadding="1" cellspacing="0" align="left" width="100%">
																				<tr>
																					<td align="center" width="6%">
																						<smx:text property="puntoEmisionFactura" styleClass="textObligatorio" styleError="campoError" size="3" maxlength="3" />
																					</td>
																					<td align="left" width="2%">-</td>
																					<td align="left" >
																						<smx:text property="numeroFactura" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="9" />	
																					</td>
																					<td align="left">ej: 123-123456789</td>
																				</tr>
																			</table>
																		</td>
		                                                          	 </tr>
		                                                            <tr align="left">
		                                                                <td align="left" width="15%" class="fila_contenido">
		                                                                    Valor abono*:  
		                                                                </td>
		                                                                <td align="left" width="15%" class="fila_contenido">
		                                                                    <smx:text property="valorAbono" styleClass="textObligatorio" styleError="campoError"/>
		                                                                </td>
		                                                                <td colspan="5" class="fila_contenido">
		                                                                    <table border="0" width="100%">
		                                                                        <tr>
		                                                                            <td align="left" width="18%">
		                                                                                Forma de Pago*:  
		                                                                            </td>
		                                                                            <td align="left" colspan="5">
		                                                                                <smx:select property="comboFormasPago" styleClass="comboObligatorio" styleError="campoError">
		                                                                                    <html:option value="">Seleccione</html:option>
		                                                                                    <html:options collection="ec.com.smx.sic.sispe.formasPagoAbonos" labelProperty="descripcionFormaPago" property="id.codigoFormaPago"/>
		                                                                                </smx:select>
		                                                                            </td>
		                                                                        </tr>
		                                                                    </table>
		                                                                </td>
		                                                            </tr>
		                                                          <tr height="2px">
																	</tr>
		                                                            <tr align="left">
		                                                                <td>Caja*: </td>
		                                                                <td><smx:text property="codigoCaja" styleClass="textObligatorio" styleError="campoError"/></td>
		                                                                <td width="10%">Cajero*: </td>
		                                                                <td width="20%"><smx:text property="codigoCajero" styleClass="textObligatorio" styleError="campoError"/></td>
		                                                                <td width="20%">Transacci&oacute;n N&uacute;mero*:</td>
		                                                                <td><smx:text property="numeroTransaccionAbono" styleClass="textObligatorio" styleError="campoError"/></td>
		                                                            </tr>
		                                                            <tr align="left">
		                                                                <td valign="top">Observaci&oacute;n*: </td>
		                                                                <td colspan="5">
		                                                                    
		                                                                    <smx:textarea property="observacionAbono" styleClass="textObligatorio" styleError="campoError" cols="132" rows="3"/>
		                                                                    
		                                                                </td>
		                                                            </tr>
		                                                        </table>
		                                                    </td>
		                                                </tr>
		                                            </table>
		                                        </td>
		                                    </tr> 
										</c:when>
										<c:when test='${codigoEstadoPagadoA==codigoEstadoPagadoParcialmente}'>
											<tr><td height="10"></td></tr>                                    
											<tr>
												<td>
													<table border="0" width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
														<tr>
															<td class="fila_titulo" height="20px">
																<table cellpadding="0" cellspacing="0" align="left" width="100%" border="0">
																	<tr>
																		<td align="left" width="3%"><img src="images/abono.gif" border="0"></td>
																		<td align="left">Nuevo Abono</td>
																	</tr>
																</table>
															</td>
														</tr>
														<tr>
															<td>
																<table border="0" cellpadding="1" cellspacing="0" align="left" width="100%" class="textoAzul11">
																	 <tr align="left">
		                                                                <td align="left" width="15%">
		                                                                    N&uacute;mero Factura*:  
		                                                                </td>
																		<td colspan="3">
																			<table border="0" cellpadding="1" cellspacing="0" align="left" width="100%">
																				<tr>
																					<td align="center" width="6%">
																						<smx:text property="puntoEmisionFactura" styleClass="textObligatorio" styleError="campoError" size="3" maxlength="3" />
																					</td>
																					<td align="left" width="2%">-</td>
																					<td align="left" >
																						<smx:text property="numeroFactura" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="9" />	
																					</td>
																					<td align="left">ej: 123-123456789</td>
																				</tr>
																			</table>
																		</td>
		                                                          	 </tr>
																	<tr align="left">
																		<td align="left" width="15%" class="fila_contenido">
																			Valor abono*:  
																		</td>
																		<td align="left" width="15%" class="fila_contenido">
																			<smx:text property="valorAbono" styleClass="textObligatorio" styleError="campoError"/>
																		</td>
																		<td colspan="5" class="fila_contenido">
																			<table border="0" width="100%">
																				<tr>
																					<td align="left" width="18%">
																						Forma de Pago*:  
																					</td>
																					<td align="left" colspan="5">
																						<smx:select property="comboFormasPago" styleClass="comboObligatorio" styleError="campoError">
																							<html:option value="">Seleccione</html:option>
																							<html:options collection="ec.com.smx.sic.sispe.formasPagoAbonos" labelProperty="descripcionFormaPago" property="id.codigoFormaPago"/>
																						</smx:select>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr height="2px">
																	</tr>
																	<tr align="left">
																		<td>Caja*: </td>
																		<td><smx:text property="codigoCaja" styleClass="textObligatorio" styleError="campoError"/></td>
																		<td width="10%">Cajero*: </td>
																		<td width="20%"><smx:text property="codigoCajero" styleClass="textObligatorio" styleError="campoError"/></td>
																		<td width="20%">Transacci&oacute;n N&uacute;mero*:</td>
																		<td><smx:text property="numeroTransaccionAbono" styleClass="textObligatorio" styleError="campoError"/></td>
																	</tr>
																	<tr align="left">
																		<td valign="top">Observaci&oacute;n*: </td>
																		<td colspan="5">
																			
																			<smx:textarea property="observacionAbono" styleClass="textObligatorio" styleError="campoError" cols="132" rows="3"/>
																			
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
													</table>
												</td>
											</tr> 
										</c:when>
									</c:choose>
								</logic:empty>
							
							</table>
            </td>
        </tr>
	</table>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>