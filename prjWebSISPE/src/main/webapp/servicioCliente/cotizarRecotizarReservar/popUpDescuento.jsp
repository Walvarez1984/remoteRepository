<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="estadoActivo" name="estadoActivo" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="token" name="token" classname="java.lang.String" ignore="true"/>

<script language="javascript">mostrarModal();</script>
<c:set var="mostrarNuevoDescuento" value="${0}"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.check.pagoEfectivo">
	 <c:set var="mostrarNuevoDescuento" value="${1}"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo">
	<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPagoEfectivo" id="tipoDescuentoMaxiNavidad"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDes.navEmp.credito">
	<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDes.navEmp.credito" id="tipoDescuentoMaxiNavidadCredito"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja">
	<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorCaja" id="tipoDescuentoPorCaja"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista">
	<bean:define name="ec.com.smx.sic.sispe.parametro.codigoTipoDescuentoPorMayorista" id="tipoDescuentoPorMayorista"/>
</logic:notEmpty>

<c:set var="pedidoConsolidado" value="false"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.es.pedido.consolidado">
	<c:set var="pedidoConsolidado" value="true"/>
</logic:notEmpty>

<%-- <bean:define name="ec.com.smx.sic.sispe.parametro.autorizacion.adminLocal" id="autorizarAdminLocal"/> --%>
<%-- <c:set var="restarPosicionPorcentajeVarDcto" value="1"/> --%>
<%-- <c:if test="${autorizarAdminLocal == 'SI' }"> --%>
<%-- 	<c:set var="restarPosicionPorcentajeVarDcto" value="1" /> --%>
<%-- </c:if> --%>
<%-- <c:if test="${autorizarAdminLocal == 'NO' }"> --%>
<%-- 	<c:set var="restarPosicionPorcentajeVarDcto" value="0"/> --%>
<%-- </c:if> --%>

<!-- Clasificar los tipos descuentos -->
<bean:define name="ec.com.smx.sic.sispe.tipoDescuentoExcluyente" id="excluyentes"/>
<bean:define name="ec.com.smx.sic.sispe.tipoDescuentoOtros" id="otros"/>
<bean:define name="ec.com.smx.sic.sispe.tipoDescuentoVariables" id="variables"/>
<logic:notEmpty name="excluyentes">
	<c:set var="contadorExcluyente" value="0"/>
</logic:notEmpty>
<logic:notEmpty name="otros">
	<c:set var="contadorOtros" value="0"/>
</logic:notEmpty>
<logic:notEmpty name="variables">
	<c:set var="contadorVariables" value="0"/>
</logic:notEmpty>

<div id="descuentos" class="popup" style="top:-140px;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="70%">
		<tr>
			<td valign="top" align="center">
				<div id="center" class="popupcontenido">
					<%-- Contenido de la ventana de confirmacion --%>					
			        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" >
			        
			        	<!-- FILA CON TITULO DEL POPUP -->
			            <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Descuentos disponibles</b>
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','pregunta','divTabs'], {parameters: 'ayuda=cancelarDescuento', evalScripts: true});ocultarModal();">
                                                <img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                            </html:link>&nbsp;                                            
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            
			            <!-- FILA CON TODO EL CONTENIDO DEL POPUP -->
						<tr>
							<td bgcolor="#F4F5EB" valign="top">	                            
			                    <table class="tabla_informacion textoNegro11" border="0" width="100%" cellpadding="0" cellspacing="5">
									<!-- 1 fila SECCION MENSAJES -->
									<tr>
										<td>
											<div id="mensaje_popUp" style="font-size:0px;position:relative;">
												<tiles:insert  page="/include/mensajes.jsp" /> 
											</div> 
										</td>
									</tr>
									
									<!-- TODO EL CONTENIDO DE LOS DESCUENTOS -->
	                                <tr>
								   		<td valign="top">
											<div id="cuerpo_popUp" style="height:510px; width:750px; overflow-x:hidden;overflow-y:hidden;">								   	                                     
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.tipoDescuento">
                                                <table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tr>
													
														<!-- 1 columna SECCION INFORMATICA DE LOS DESCUENTOS -->
														<td bgcolor="#ffffff" align="left">
                                                            <div style="height:500px;overflow-x:hidden;overflow-y:auto;border:1px solid #cccccc;padding:4px;">
                                                                <table cellpadding="0" cellspacing="0" border="0" class="textoNegro9">
                                                                   <tr>
                                                                        <td class="textoRojo10" align="left" colspan="4">
                                                                        	<table width="100%" cellpadding="0" cellspacing="0">
                                                                            	<tr>
                                                                                	<td width="5%"><html:img src="images/info_16.gif" border="0"/></td>
                                                                                    <td align="left">&nbsp;<b>RANGOS POR TIPO DE DESCUENTO</b></td>
                                                                                </tr>
                                                                            </table>
                                                                     	</td>
                                                                    </tr>
                                                                    <tr><td height="5px" colspan="4"></td></tr>
																	
																	<!-- pago en efectico desactivado -->
                                                                    <c:if test="${mostrarNuevoDescuento == 0}">
                                                                        <logic:iterate name="ec.com.smx.sic.sispe.pedido.tipoDescuento.mostrar.rangos" id="tipoDescuentoDTO">
                                                                            <c:if test="${tipoDescuentoDTO.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">			
                                                                                <tr><td align="left" colspan="4"><b><bean:write name="tipoDescuentoDTO" property="descripcionTipoDescuento"/></b></td></tr>
                                                                                <logic:notEmpty name="tipoDescuentoDTO" property="detalleDescuentos">
                                                                                    <logic:iterate name="tipoDescuentoDTO" property="detalleDescuentos" id="descuentoDTO">
                                                                                        <logic:equal name="descuentoDTO" property="estadoDescuento" value="${estadoActivo}">
                                                                                            <tr>                                                                                                
                                                                                                <td align="left">&nbsp;&nbsp;&nbsp;<bean:write name="descuentoDTO" property="motivoDescuentoDTO.descripcionMotivoDescuento"/>&nbsp;&nbsp;</td>
                                                                                                <td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.rangoInicialDescuento}"/>
                                                                                                <td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.rangoFinalDescuento}"/>&nbsp;&nbsp;</td>
                                                                                                <td align="right" class="textoAzul9">
                                                                                                    <logic:empty name="tipoDescuentoDTO" property="npTipoDescuentoVariable">
                                                                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.porcentajeDescuento}"/>%
                                                                                                    </logic:empty>
                                                                                                    <logic:notEmpty name="tipoDescuentoDTO" property="npTipoDescuentoVariable">...%</logic:notEmpty>
                                                                                                </td>
                                                                                            </tr>
                                                                                        </logic:equal>
                                                                                    </logic:iterate>
                                                                                    <tr><td height="3px" colspan="4"></td></tr>
                                                                                </logic:notEmpty>
                                                                            </c:if>
                                                                        </logic:iterate>
                                                                    </c:if>
                                                                    <c:if test="${mostrarNuevoDescuento == 1}">																	
                                                                         <logic:iterate name="ec.com.smx.sic.sispe.pedido.tipoDescuento.mostrar.rangos" id="tipoDescuentoDTO">
																		<c:if test="${tipoDescuentoDTO.id.codigoTipoDescuento != tipoDescuentoMaxiNavidadCredito}">
                                                                            <tr><td align="left" colspan="4"><b><bean:write name="tipoDescuentoDTO" property="descripcionTipoDescuento"/></b></td></tr>
																				<logic:notEmpty name="tipoDescuentoDTO" property="detalleDescuentos">
																					<logic:iterate name="tipoDescuentoDTO" property="detalleDescuentos" id="descuentoDTO">
																						<logic:equal name="descuentoDTO" property="estadoDescuento" value="${estadoActivo}">
																							<tr>
																								<td align="left">&nbsp;&nbsp;&nbsp;<bean:write name="descuentoDTO" property="motivoDescuentoDTO.descripcionMotivoDescuento"/>&nbsp;&nbsp;</td>
																								<td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.rangoInicialDescuento}"/>&nbsp;&nbsp;a&nbsp;&nbsp;</td>
																								<td align="right"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.rangoFinalDescuento}"/>&nbsp;&nbsp;</td>
																								<td align="right" class="textoAzul9">
																									<logic:empty name="tipoDescuentoDTO" property="npTipoDescuentoVariable">
																										<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${descuentoDTO.porcentajeDescuento}"/>%
																									</logic:empty>
																									<logic:notEmpty name="tipoDescuentoDTO" property="npTipoDescuentoVariable">...%</logic:notEmpty>
																								</td>
																							</tr>
																						</logic:equal>
																					</logic:iterate>
																					<tr><td height="3px" colspan="4"></td></tr>
																				</logic:notEmpty>
																			</c:if>
                                                                        </logic:iterate>
                                                                    </c:if>
                                                                </table>
                                                            </div>
                                                        </td>
														<!-- fin 1 columna-->
														
														<!-- 2 columna CONFIGURACION DE DESCUENTOS -->
														<td> 
															<div style="background-color:#FFFFFF; border:1px solid #cccccc;padding:4px;">
                                                                <table border="0" class="textoNegro11" width="100%" cellspacing="0" cellpadding="0">
                                                                    <tr>
																		<td>	
                                                                            <div style="height:500px;overflow-x:hidden;overflow-y:auto;">
                                                                                <table width="100%" cellpadding="0" cellspacing="0"  bgcolor="#ffffff" class="tabla_datos">
                                                                                    <logic:iterate name="ec.com.smx.sic.sispe.pedido.tipoDescuento" id="tipoDescuento" indexId="indiceTipo">
                                                                                        <bean:define id="numFila" value="${indiceTipo % 2}"/>                                                     
                                                                                        <smx:equal name="numFila" value="0">	
                                                                                            <bean:define id="color" value="blanco"/>
                                                                                        </smx:equal>
                                                                                        <smx:equal name="numFila" value="1">	
                                                                                            <bean:define id="color" value="grisClaro"/>
                                                                                        </smx:equal>    
                                                                                        <bean:define id="contador" value="-1"/>
                                                                                        <bean:define name="tipoDescuento" property="id.codigoTipoDescuento" id="codTipoDesc"/>
                                                                                        <bean:define name="tipoDescuento" property="npVisible" id="npMostrarMotivo"/>
																						<bean:define name="tipoDescuento" property="npExcluyente" id="clasificacionTipoDes"/>
                                                                                        <tr>
                                                                                            <td>
																								<!-- DESCUENTOS EXCLUYENTES SIN PAGO EFECTIVO -->
																								<c:if test="${clasificacionTipoDes == excluyentes}">
																								<c:if test="${mostrarNuevoDescuento == 0}">
                                                                                                    <table border="0" cellpadding="2" cellspacing="0" width="100%">
																										<c:if test="${contadorExcluyente == 0}">
																											<bean:define id="contadorExcluyente" value="${contadorExcluyente + 1}"/>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0">
																													<tr>
																														<td class="textoRojo10" align="left" width="200px">
																															<b>DESCUENTOS EXCLUYENTES</b>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0"  bgcolor="#ffffff" class="tabla_informacion_encabezado">
																													<tr class="tituloTablas">
																														<td class="tituloTablasCel" width="200px" align="center">TIPO DESCUENTO</td>
																														<td align="center" width="18%" class="tituloTablasCel">SELECCIONE</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										</c:if>
																										<tr>																										
                                                                                                            <td class="${color} fila_contenido" align="left" nowrap="nowrap" width="267px">
                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
																													<tr>
                                                                                                                        <td><bean:write name="tipoDescuento" property="descripcionTipoDescuento"/></td>                                                                                                            
                                                                                                                    </tr>    
                                                                                                                </table>
                                                                                                            </td>																											
                                                                                                            <logic:iterate name="ec.com.smx.sic.sispe.pedido.motivoDescuento" id="motivoDescuento" indexId="indiceMotivo">
                                                                                                                <bean:define id="contador" value="${contador + 1}"/>
                                                                                                                <bean:define name="motivoDescuento" property="id.codigoMotivoDescuento" id="codMotDesc"/>
																												
																												<!-- LLAVE DE LOS DESCUENTOS EXCLUYENTES -->
                                                                                                                <bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}" id="codigo"/>                                                                                                                
																												
																												<c:if test="${npMostrarMotivo == codMotDesc && clasificacionTipoDes == excluyentes}">
	                                                                                                                <logic:empty name="tipoDescuento" property="npTipoDescuentoVariable">
																														<td class="${color} columna_contenido fila_contenido" width="18%" align="center">&nbsp;
	                                                                                                                    	<c:if test="${tipoDescuento.id.codigoTipoDescuento == tipoDescuentoMaxiNavidad}">
	                                                                                                                            <html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" style="font-size:0px;">
	                                                                                                                                <bean:write name="codigo"/>
	                                                                                                                            </html:multibox>
	                                                                                                                        </c:if>
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorMayorista and codMotDesc=='MON')}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" style="font-size:0px;">
																																		<bean:write name="codigo"/>
																																	</html:multibox>
	                                                                                                                        </c:if>
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorCaja and codMotDesc=='MON')}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" style="font-size:0px;">
																																		<bean:write name="codigo"/>
																																	</html:multibox>
	                                                                                                                        </c:if>
																															<c:if test="${tipoDescuento.id.codigoTipoDescuento == tipoDescuentoMaxiNavidadCredito}">
																																<c:if test="${pedidoConsolidado=='true'}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" style="font-size:0px;">
																																		<bean:write name="codigo"/>
																																	</html:multibox>
																																</c:if>
																																<c:if test="${pedidoConsolidado=='false'}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" style="font-size:0px;">
																																		<bean:write name="codigo"/>
																																	</html:multibox>
																																</c:if>
																															</c:if>
	                                                                                                                        <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">
																																 <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidadCredito}">
																																	<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorMayorista or codMotDesc!='MON')}">
																																		<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorCaja or codMotDesc!='MON')}">																																 
																																			<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" style="font-size:0px;">
																																				<bean:write name="codigo"/>
																																			</html:multibox>
																																		</c:if>
																																	</c:if>
																																</c:if>
	                                                                                                                        </c:if>
																														</td>
	                                                                                                                </logic:empty>
                                                                                                                </c:if>
                                                                                                            </logic:iterate>
                                                                                                        </tr>
                                                                                                    </table>    
                                                                                                </c:if>
																								<!--fin 1 c:if -->
																								
																								<!-- 2 c:if DESCUENTOS EXCLUYENTES CON PAGO EFECTIVO -->
                                                                                                <c:if test="${mostrarNuevoDescuento == 1}">
                                                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                                                        <c:if test="${contadorExcluyente == 0}">
																											<bean:define id="contadorExcluyente" value="${contadorExcluyente + 1}"/>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0">
																													<tr>
																														<td class="textoRojo10" align="left" width="267px" >
																															<b>DESCUENTOS EXCLUYENTES</b>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0"  bgcolor="#ffffff" class="tabla_informacion_encabezado">
																													<tr class="tituloTablas">
																														<td class="tituloTablasCel" width="200px" align="center">TIPO DESCUENTO</td>
																														<td align="center" width="18%" class="tituloTablasCel">SELECCIONE</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										</c:if>
																										<tr>  
																											<td class="${color} fila_contenido" align="left" nowrap="nowrap" width="267px">                 
																												<table border="0" cellpadding="0" cellspacing="0" width="100%">
																													<tr>
                                                                                                                        <td><bean:write name="tipoDescuento" property="descripcionTipoDescuento"/></td>                                                                                                            
                                                                                                                    </tr>    
                                                                                                                </table>
																											</td>
																											<logic:iterate name="ec.com.smx.sic.sispe.pedido.motivoDescuento" id="motivoDescuento" indexId="indiceMotivo">
																												<bean:define id="contador" value="${contador + 1}"/>                                                                                                            
																												<bean:define name="motivoDescuento" property="id.codigoMotivoDescuento" id="codMotDesc"/>
																												<bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}" id="codigo"/>
																												<c:if test="${npMostrarMotivo == codMotDesc && clasificacionTipoDes == excluyentes}"> <!-- Cambios oscar -->
																													<logic:empty name="tipoDescuento" property="npTipoDescuentoVariable">																														
																														<td class="${color} columna_contenido fila_contenido" width="18%" align="center">
																															<c:if test="${tipoDescuento.id.codigoTipoDescuento == tipoDescuentoMaxiNavidadCredito}">
																																<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																	<bean:write name="codigo"/>
																																</html:multibox>
																															</c:if>
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorMayorista and codMotDesc=='MON')}">
																																<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																	<bean:write name="codigo"/>
																																</html:multibox>
																															</c:if>
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorCaja and codMotDesc=='MON')}">
																																<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																	<bean:write name="codigo"/>
																																</html:multibox>
																															</c:if>
																															<c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidadCredito}">
																																<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorMayorista or codMotDesc!='MON')}">
																																	<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorCaja or codMotDesc!='MON')}">
																																		<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" >
																																			<bean:write name="codigo"/>
																																		</html:multibox>
																																	</c:if>
																																</c:if>
																															</c:if>
																														</td>
																													</logic:empty>
																												</c:if>
																											</logic:iterate>
                                                                                                        </tr>
                                                                                                    </table>
                                                                                                </c:if>
																								<!-- fin 2 c:if -->																								
																								</c:if>
																								<!-- FIN EXCLUYENTES -->
																								
																								<!-- OTROS -->
																								<c:if test="${clasificacionTipoDes == otros}">																								
                                                                                                    <table border="0" cellpadding="2" cellspacing="0" width="100%">
																										<c:if test="${contadorOtros == 0}">
																											<bean:define id="contadorOtros" value="${contadorOtros + 1}"/>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0">
																													<tr>
																														<td class="textoRojo10" align="left" width="267px" >
																															<b>OTROS DESCUENTOS</b>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>																										
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0"  bgcolor="#ffffff" class="tabla_informacion_encabezado">
																													<tr class="tituloTablas">
																														<td class="tituloTablasCel" width="200px" align="center">TIPO DESCUENTO</td>
																														<td align="center" width="18%" class="tituloTablasCel">SELECCIONE</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										</c:if>
																										<tr>                                                                                                           
                                                                                                            <td class="${color} fila_contenido" align="left" nowrap="nowrap" width="267px">
                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
																													<tr>
                                                                                                                        <td><bean:write name="tipoDescuento" property="descripcionTipoDescuento"/></td>                                                                                                            
                                                                                                                    </tr>    
                                                                                                                </table>
                                                                                                            </td>																											
                                                                                                            <logic:iterate name="ec.com.smx.sic.sispe.pedido.motivoDescuento" id="motivoDescuento" indexId="indiceMotivo">
                                                                                                                <bean:define id="contador" value="${contador + 1}"/>
                                                                                                                <bean:define name="motivoDescuento" property="id.codigoMotivoDescuento" id="codMotDesc"/>
                                                                                                                <bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}" id="codigo"/>
																												<c:if test="${npMostrarMotivo == codMotDesc && clasificacionTipoDes == otros}">
	                                                                                                                <logic:empty name="tipoDescuento" property="npTipoDescuentoVariable">
																														<td class="${color} columna_contenido fila_contenido" width="18%" align="center">&nbsp;
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorMayorista and codMotDesc=='MON')}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																		<bean:write name="codigo"/>
																																	</html:multibox>
	                                                                                                                        </c:if>
																															<c:if test="${(tipoDescuento.id.codigoTipoDescuento == tipoDescuentoPorCaja and codMotDesc=='MON')}">
																																	<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																		<bean:write name="codigo"/>
																																	</html:multibox>
	                                                                                                                        </c:if>		
	                                                                                                                        <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">
																																 <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidadCredito}">
																																	<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorMayorista or codMotDesc!='MON')}">
																																		<c:if test="${(tipoDescuento.id.codigoTipoDescuento != tipoDescuentoPorCaja or codMotDesc!='MON')}">																																 
																																			<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" >
																																				<bean:write name="codigo"/>
																																			</html:multibox>
																																		</c:if>
																																	</c:if>
																																</c:if>
	                                                                                                                        </c:if>
																														</td>
	                                                                                                                </logic:empty>
                                                                                                                </c:if>
                                                                                                            </logic:iterate>
                                                                                                        </tr>
                                                                                                    </table>                                                                                      																																											
																								</c:if>
																								<!-- FIN OTROS -->
																								
																								<!-- VARIABLES -->
																								<c:if test="${clasificacionTipoDes == variables}">																							
                                                                                                    <table border="0" cellpadding="2" cellspacing="0" width="100%">
																										<c:if test="${contadorVariables == 0}">
																											<bean:define id="contadorVariables" value="${contadorVariables + 1}"/>
																										<tr>
																											<td colspan="2">
																												<table width="100%" cellpadding="0" cellspacing="0">
																													<tr>
																														<td class="textoRojo10" align="left" width="267px" >
																															<b>DESCUENTOS VARIABLES</b>
																														</td>
																													</tr>
																												</table>
																											</td>
																										</tr>
																										</c:if>
																										<tr>                                                                                                           
                                                                                                            <td class="${color} fila_contenido" align="left" nowrap="nowrap" width="200px">
                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
																													<tr>
                                                                                                                        <logic:notEmpty name="tipoDescuento" property="npTipoDescuentoVariable">
                                                                                                                        <td width="8%">
                                                                                                                            <div class="contenedor tituloTodos"> 
                                                                                                                                <table cellpadding="0" cellspacing="0" border="0">
                                                                                                                                    <tr>
                                                                                                                                        <td class="arbol" style="padding-top:1px;">
                                                                                                                                            <html:img src="images/plegar.gif" border="0" style="cursor:pointer;"/>
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                </table>
                                                                                                                             </div>  
                                                                                                                        </td>                                                                                                       
                                                                                                                        </logic:notEmpty>
                                                                                                                        <td><bean:write name="tipoDescuento" property="descripcionTipoDescuento"/></td>                                                                                                            
                                                                                                                    </tr>    
                                                                                                                </table>
                                                                                                            </td>
                                                                                                        </tr>
                                                                                                    </table>                                                                                                   
																								<!--fin 1 c:if -->
																							
																								<!-- logic: notEmpty -->
                                                                                                <logic:notEmpty name="tipoDescuento" property="npTipoDescuentoVariable">
                                                                                                    <div class="compradores" align="left">                                                                                                        	
                                                                                                        <table border="0" width="100%" cellspacing="0" cellpadding="0"  height="20px">
                                                                                                            <tr>
                                                                                                                <td width="4%" align="center" class="fila_contenido" >&nbsp;</td>
                                                                                                                <td>
                                                                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                                                                    	<tr>
                                                                                                                            <td width="49%" align="center" class="tituloTablasCel" height="16px">
                                                                                                                                <B>AUTORIZADOR</B>                                            
                                                                                                                            </td>
                                                                                                                             <td width="25%" align="center" class="tituloTablasCel">
                                                                                                                                <B>% MARCA PROPIA</B>
                                                                                                                            </td>
																															<td width="25%" align="center" class="tituloTablasCel">
                                                                                                                                <B>% PROVEEDOR</B>
                                                                                                                            </td>
                                                                                                                        </tr>
                                                                                                                        <tr>
																															<c:if test="${autorizarAdminLocal == 'SI' }">
																																<td colspan="10" class="violeta10 columna_contenido fila_contenido">
																																	&nbsp;Descuento variable <b>menor o igual al <bean:write name="ec.com.smx.sic.sispe.pedido.valorMaxDescuento"/>%</b>    
																																</td>
																															</c:if>
																														</tr>
																														<tr>
																															<c:if test="${autorizarAdminLocal == 'SI' }">
																																<td align="left" class="${color} fila_contenido columna_contenido" style="padding-left:2px;">
																																	ADMINISTRADOR DEL LOCAL
																																</td>
																																<logic:iterate name="ec.com.smx.sic.sispe.pedido.motivoDescuento" id="motivoDescuento" indexId="indiceMotivo">
																																	<bean:define id="contador" value="${contador + 1}"/>
																																	<bean:define name="motivoDescuento" property="id.codigoMotivoDescuento" id="codMotDesc"/>
																																	<bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}${token}ADM${token}INX${0}" id="codigo"/>																																	
																																	
																																	<c:if test="${npMostrarMotivo == codMotDesc}"><!-- Cambios oscar -->
																																<td class="${color} columna_contenido fila_contenido" align="center">
																																			<c:if test="${tipoDescuento.id.codigoTipoDescuento == tipoDescuentoMaxiNavidad}">
																																				<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="true" value="" >
																																					<bean:write name="codigo"/>
																																				</html:multibox>
																																			</c:if>
																																			<c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">
																																				<html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" >
																																					<bean:write name="codigo"/>
																																				</html:multibox>
																																			</c:if>
																																</td>
																																	</c:if>
																																</logic:iterate>
																																<td align="center" class="${color} fila_contenido columna_contenido" style="padding-left:2px;" >
																																	<html:text name="cotizarRecotizarReservarForm" property="porcentajeVarDescuento" value="${cotizarRecotizarReservarForm.porcentajeVarDescuento[0]}" size="4" styleClass="textNormal" maxlength="5" style="text-align:right;" onkeypress="return validarInputNumericDecimal(event);"/>%&nbsp;                                                                                                                                        
																																</td>
																															</c:if>
																														</tr>
                                                                                                                        <tr>
                                                                                                                        	<td colspan="10" class="violeta10 columna_contenido fila_contenido">
                                                                                                                        		&nbsp;Descuentos variables <b>entre el 0.01 y el <bean:write name="ec.com.smx.sic.sispe.pedido.valorMaxDescuento"/>%</b>
                                                                                                                            </td>
                                                                                                                        </tr>
																														<logic:notEmpty name="ec.com.smx.sic.sispe.departamentos.pedido">
                                                                                                                            <logic:iterate id="clasificacionDTO" name="ec.com.smx.sic.sispe.departamentos.pedido" indexId="numRegistro">                                                                                            
                                                                                                                                <bean:define id="numFila" value="${numRegistro % 2}"/>                                                     
                                                                                                                                <smx:equal name="numFila" value="0">	
                                                                                                                                    <bean:define id="color" value="blanco"/>
                                                                                                                                </smx:equal>
                                                                                                                                <smx:equal name="numFila"  value="1">	
                                                                                                                                    <bean:define id="color" value="grisClaro"/>
                                                                                                                                </smx:equal>
																																<tr>
																																	<td align="left" class="${color} fila_contenido columna_contenido" style="padding-left:2px;">
																																				<bean:write name="clasificacionDTO" property="descripcionClasificacion" />
																																	</td>
                                                                                                                                    <logic:iterate name="ec.com.smx.sic.sispe.pedido.motivoDescuento" id="motivoDescuento" indexId="indiceMotivo">
                                                                                                                                        <bean:define id="contador" value="${contador + 1}"/>
                                                                                                                                        <bean:define name="motivoDescuento" property="id.codigoMotivoDescuento" id="codMotDesc"/>
                                                                                                                                        <bean:define name="clasificacionDTO" property="id.codigoClasificacion" id="codClasificacion"/>
                                                                                                                                        <bean:define name="clasificacionDTO" property="npIdAutorizador" id="idAutorizador"/>
                                                                                                                                        
                                                                                                                                        <c:if test="${npMostrarMotivo == codMotDesc}"> <!-- Cambios oscar -->
																																		
																																			<td class="${color} columna_contenido fila_contenido" align="center">	          
																																				<bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}${token}COM${codClasificacion}:${idAutorizador.split(',')[0]}${token}INX${numRegistro}0" id="codigoMP"/>
																																																																						
	                                                                                                                                            <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">
	                                                                                                                                                <html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="${!clasificacionDTO.npActivarDescMp}">
	                                                                                                                                                    <bean:write name="codigoMP"/>
	                                                                                                                                                </html:multibox>
	                                                                                                                                            </c:if>
																																				<c:set var="estiloCheckMarcaPropia" value="textNormal"/>
																																				<c:if test="${!clasificacionDTO.npActivarValorDescMp}">
																																					<c:set var="estiloCheckMarcaPropia" value="textObligatorioOpacity"/>
																																				</c:if>																																				
	                                                                                                                                            <html:text name="cotizarRecotizarReservarForm" property="porcentajeVarDescuento[${numRegistro}][0]" value="${cotizarRecotizarReservarForm.porcentajeVarDescuento[numRegistro][0]}" 
                                                                                                                                        			size="4" disabled="${!clasificacionDTO.npActivarValorDescMp}"  styleClass="${estiloCheckMarcaPropia}" maxlength="5" style="text-align:right;" onkeypress="return validarInputNumericDecimal(event);" />%&nbsp;
																																			</td>
																																			
																																			<td class="${color} columna_contenido fila_contenido" align="center">	          
																																			<bean:define value="${indiceTipo}${token}CTD${codTipoDesc}${token}CMD${codMotDesc}${token}COM${codClasificacion}:${fn:length(idAutorizador.split(','))>1?idAutorizador.split(',')[1]:idAutorizador.split(',')[0]}${token}INX${numRegistro}1" id="codigoProv"/>
                                                                                                                                        		
																																				<c:set var="estiloCheckProveedor" value="textNormal"/>
																																				<c:if test="${!clasificacionDTO.npActivarDescProv}">
																																					<c:set var="estiloCheckProveedor" value="textObligatorioOpacity"/>
																																				</c:if>																															
	                                                                                                                                            <c:if test="${tipoDescuento.id.codigoTipoDescuento != tipoDescuentoMaxiNavidad}">
	                                                                                                                                                <html:multibox name="cotizarRecotizarReservarForm" property="opDescuentos" disabled="${!clasificacionDTO.npActivarDescProv}" >
	                                                                                                                                                    <bean:write name="codigoProv"/>
	                                                                                                                                                </html:multibox>
	                                                                                                                                            </c:if>
	                                                                                                                                            <html:text name="cotizarRecotizarReservarForm" property="porcentajeVarDescuento[${numRegistro}][1]" value="${cotizarRecotizarReservarForm.porcentajeVarDescuento[numRegistro][1]}" 
                                                                                                                                        			size="4" disabled="${!clasificacionDTO.npActivarValorDescProv}"  styleClass="${estiloCheckProveedor}" maxlength="5" style="text-align:right;" onkeypress="return validarInputNumericDecimal(event);" />%&nbsp;
																																			</td>
																																			
                                                                                                                                        </c:if>
                                                                                                                                    </logic:iterate>
<%-- 																																	<td align="center" class="${color} fila_contenido columna_contenido" style="padding-left:2px;" > --%>
<%--                                                                                                                                         <html:text name="cotizarRecotizarReservarForm" property="porcentajeVarDescuento" value="${cotizarRecotizarReservarForm.porcentajeVarDescuento[numRegistro+restarPosicionPorcentajeVarDcto]}"  --%>
<%--                                                                                                                                         size="4" disabled="${activarDescuento}"  styleClass="textNormal" maxlength="5" style="text-align:right;" onkeypress="return validarInputNumericDecimal(event);" />%&nbsp;                                                                                                                                                                                                                                                                             --%>
<!-- 																																	</td> -->
																																</tr>    
																															</logic:iterate>
                                                                                                                        </logic:notEmpty>    
                                                                                                                        <logic:empty name="ec.com.smx.sic.sispe.departamentos.pedido">
                                                                                                                        <tr>
                                                                                                                            <td colspan="10">
                                                                                                                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                                                                                    <tr>
                                                                                                                                        <td align="center" height="50px" class="tabla_informacion amarillo1" width="340px">
                                                                                                                                            No se han encontrado departamentos para los art&iacute;culos del pedido
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                </table>
                                                                                                                            </td>
                                                                                                                        </tr>        
                                                                                                                    	</logic:empty>   
                                                                                                                    </table>
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </table>
                                                                                                    </div>
                                                                                                </logic:notEmpty>
																								<!-- fin logic: notEmpty -->
																								</c:if>
																								<!-- FIN VARIABLES-->
																							</td>
                                                                                        </tr>                
                                                                                    </logic:iterate>                                                                                
                                                                                </table>
                                                                            </div>
                                                                        </td>	
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                        </td>
														<!-- fin 2 columna POPUP-->
													</tr>
                                                </table>
												</logic:notEmpty>
											</div>
										</td>
			                        </tr>
									<!-- fin 1 fila -->
									<!-- 2 fila -->
                         			<tr>
										<td align="left" class="textoAzul9">
											<html:checkbox property="opSinDescuentos" title="sin descuentos" value="sd"/>&nbsp;Eliminar todos los descuentos al pedido
										</td>
									</tr>
									<!-- fin 2 fila -->
									<!-- 3 fila -->

									<!-- fin 3 fila -->
									<!-- 4 fila -->
                             		<tr>
										<td align="center">				
											<table cellpadding="0" cellspacing="0" border="0">
												<tr>                                                    
													<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
														<td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('crearCotizacion.do',['mensaje_popup','seccion_detalle','pregunta','divTabs','div_datosCliente','mensajes'],{parameters: 'aplicarDescuento=ok', evalScripts: true});ocultarModal();">Aplicar</html:link></div></td>
													</logic:equal>
													<td>&nbsp;</td>                                                    
													<td><div id="botonD"><html:link href="#" styleClass="cancelarD" onclick="requestAjax('crearCotizacion.do',['mensaje_popup','seccion_detalle','pregunta','divTabs','mensajes'], {parameters: 'ayuda=cancelarDescuento', evalScripts: true});ocultarModal();">Cancelar</html:link></div></td>                                                    
												</tr>
											</table>									
										</td>
									</tr>
									<!-- fin 4 fila -->
			                    </table>
			                </td>
			            </tr>	
			        </table>
				</div>
			</td>
		</tr>		        	
	</table>
</div>

<script type="text/javascript">
	$j(document).ready(	
		function () {
			var toggleContent = function(e) {
				var targetContent = $j('div.compradores');				
				if (targetContent.css('display') == 'none') {
					targetContent.slideDown(300);
					$j(this).find('td.arbol').html('<html:img src="images/plegar.gif" border="0" style="cursor:pointer;"/>');
				} else {
					targetContent.slideUp(300);
					$j(this).find('td.arbol').html('<html:img src="images/desplegar.gif" border="0" style="cursor:pointer;"/>');
				}		
			}
			$j('div.contenedor').bind('click', toggleContent);															
		}
		
	);
</script>