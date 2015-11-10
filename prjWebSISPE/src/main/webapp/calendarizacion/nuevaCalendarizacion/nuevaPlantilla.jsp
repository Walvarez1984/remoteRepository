<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="estadoActivo" name="estadoActivo" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="token" name="token" classname="java.lang.String" ignore="true"/>

<!-- ventana llamada desde plantillas.jsp -->
<bean:define id="estDefecto" name="ec.com.smx.calendarizacion.porDefecto"/>
<bean:define id="estAnuladoPlantilla" name="ec.com.smx.calendarizacion.estadoAnuladoPlantilla"/>

<div id="nuevaPlantilla" class="popup" style="top:-50px;">
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="80%">		
		<tr>
			<td valign="top" align="center">
				<div id="center" class="popupcontenido">
					<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" >
						<tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Crear plantilla</b> 
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('calendarizacion.do', ['pregunta','div_pagina'], {parameters: 'ocultarVentana=ok', popWait:false, evalScripts:true});ocultarModal();">
                                                <img src="./images/close.gif" border="0" style="padding-top:3px;"/>
                                            </html:link>&nbsp;                                            
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            <tr>
			            	<td bgcolor="#F4F5EB" valign="top">
			            		<table class="tabla_informacion textoNegro11" border="0" width="100%" cellpadding="0" cellspacing="5">
			            			<tr>
			            				<td valign="top">
			            					<div id="cuerpo_popUp" style="height:375px;">
												<logic:empty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
			            						<div id="mensaje_popUp12" style="width:420px; position:relative;" >
													<tiles:insert  page="/include/mensajes.jsp" />
												</div>												
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.calendarizacion.modificaplantilla">
													<logic:notEmpty name="ec.com.smx.calendarizacion.verDetalles">
														<table width="100%"  border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
																<tr>
																	<td width="100%" height="300px" align="center"> 
																		
																		<table width="100%" style="border:0px" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
																			<tr>
																				<td class="textoAzul11" align="left">
																					Nombre:				
																				</td>
																				<td class="textoNegro11" align="left">
																					<smx:text property="nombrePlantilla" size="24" styleClass="textObligatorio" styleError="campoError"/>
																				</td>
																				<td width="150" align="left" class="textoNegro11" id="colorSeleccionado" align="left">
																					<bean:define id="calendarioColorDTO" name="ec.com.smx.calendarizacion.calendarioColorDTO"/>
																					<bean:define id="colorPlantilla" name="calendarioColorDTO" property="id.codigoColorPrincipal"/>
																					<bean:define id="nombreColorPrincipal" name="calendarioColorDTO" property="nombreColorPrincipal"/>
																					<table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
																						<tr>
																							<td>
																								<html:link styleClass="paleta" title="${nombreColorPrincipal}" href="#" onclick="requestAjax('calendarizacion.do', ['pregunta','mensaje_popUp12'], {parameters: 'seleccionarColor=OK',popWait:true,evalScripts:true});mostrarModal();">
																								</html:link>
																							</td>
																						</tr>
																					</table>
																			  </td>	
																			</tr>
																			<tr>
																				<td class="textoAzul11" align="left">
																					Por Defecto:																				
																				</td>	
																				<td class="textoNegro11" align="left" colspan="2">
																					<html:checkbox name="calendarizacionForm" property="estadoPlantilla" styleClass="textObligatorio"/>
																				</td
																			</tr>
																		</table>
																		
													
																	</td>
																</tr>
														</table>
														<div  style="width:420px;height:100%" >
															
														</div>
													</logic:notEmpty>
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.calendarizacion.verDetalles">
												<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
													<tr height="168px">
														<td id="pp" align="left" colspan="2">
															<table border="0" cellspacing="3" align="left" width="100%" style="height:375px;">
																<tr>
																	<td class="textoAzul11" valign="top">
																		Nombre:				
																	</td>
																	<td class="textoNegro11">
																		<smx:text property="nombrePlantilla" size="24" styleClass="textObligatorio" styleError="campoError"/>
																	</td>
																	<td width="150" align="left" class="textoNegro11" id="colorSeleccionado">
																		<bean:define id="calendarioColorDTO" name="ec.com.smx.calendarizacion.calendarioColorDTO"/>
																		<bean:define id="colorPlantilla" name="calendarioColorDTO" property="id.codigoColorPrincipal"/>
																		<bean:define id="nombreColorPrincipal" name="calendarioColorDTO" property="nombreColorPrincipal"/>
																		<table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
																			<tr>
																				<td>
																					<html:link styleClass="paleta" title="${nombreColorPrincipal}" href="#" onclick="requestAjax('calendarizacion.do', ['pregunta','mensaje_popUp12'], {parameters: 'seleccionarColor=OK',popWait:true,evalScripts:true});mostrarModal();">
																					</html:link>
																				</td>
																			</tr>
																		</table>
																  </td>	
																</tr>
																<tr>
																	<td class="textoAzul11" colspan="3">
																		Por Defecto:
																		<html:checkbox name="calendarizacionForm" property="estadoPlantilla" styleClass="textObligatorio"/>
																	</td>	
																</tr>
																<logic:empty name="ec.com.smx.calendarizacion.verDetalles">
																	<tr>
																		<td class="textoNegro11" colspan="2"><b>
																			Configuraci&oacute;n:
																		</b></td>
																	</tr>
																	<tr>	
																		<td colspan="3">
																			<table border="0" class="textos" width="97%" align="center" cellspacing="0" cellpadding="0">
																				<tr>
																					<td width="100%">
																						<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion" height="18px">
																							<tr>												
																								
																								<td width="21%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
																								<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioloc">
																									<td width="48%" align="center" class="tituloTablas columna_contenido"><B>CPE</B></td>
																								</logic:notEmpty>
																								<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
																									<td width="21%" align="center" class="tituloTablas columna_contenido"><B>Horas</B></td>
																									<td width="24%" align="center" class="tituloTablas columna_contenido"><B>Camiones</B></td>
																									<td width="18%" align="center" class="tituloTablas columna_contenido"><B>Num. Camiones</B></td>
																									<td width="10%" align="right" class="tituloTablas columna_contenido" colspan="2"></td>
																								</logic:notEmpty>
																							</tr>	
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td  width="100%" id="nuevoListado">
																						<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
																							<tr>
																								<td width="21%" class="textoNegro9" align="center" height="25px">
																									<html:select property="diaSeleccionado" styleClass="textObligatorio">
																										<html:option value=""></html:option>
																										<html:options collection="ec.com.smx.calendarizacion.opcionesDiasCol" labelProperty="nombreDia" property="seleccion"/>
																									</html:select>
																								</td>
																								<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
																								<td width="21%" align="center" class="textoNegro9 columna_contenido">
																									<html:select property="horaSeleccionada" styleClass="textObligatorio" style="width:85px;">
																										<html:option value=""></html:option>
																										<html:options collection="ec.com.smx.calendarizacion.opcionesHorasDiaCol" labelProperty="horas" property="seleccion"/>
																									</html:select>
																								</td>
																								<td width="24%" class="textoNegro9" align="center" height="25px">
																									<smx:select property="transporteSeleccionado" styleClass="comboObligatorio" styleError="campoError" style="width:95%;">
																										<html:option value=""></html:option>
																										<logic:notEmpty name="ec.com.smx.calendarizacion.transporteDTOCol">
																											<logic:iterate name="ec.com.smx.calendarizacion.transporteDTOCol" id="transporteDTO" indexId="indiceTransporte">
																												<html:option value="${transporteDTO.id.codigoTransporte}" styleClass="comboDescripcionCiudad">${transporteDTO.nombreTransporte}</html:option>																
																											</logic:iterate>
																										</logic:notEmpty>
																									</smx:select>
																								</td>
																								</logic:notEmpty>
																								<td width="18%" class="textoNegro9" align="center" height="25px">													
																									<smx:text property="cn" size="10" styleClass="textObligatorio" styleError="campoError"/>
																								</td>
																																				
																								
																								<td width="10%" align="center" colspan="2" class="columna_contenido">
																									<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
																										<html:link title="Agregar Configuraci&oacute;n" href="#" onclick="requestAjax('calendarizacion.do', ['nuevoListado','confirmar','mensaje_popUp12','mensaje_popUp122'], {parameters: 'agregarDetallesConfiguracion=OK',popWait:true,evalScripts:true});"><img src="./images/exito_16.gif" border="0"></html:link>
																									</logic:notEmpty>
																									<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioloc">
																										<html:link title="Agregar Configuraci&oacute;n" href="#" onclick="requestAjax('calendarizacion.do', ['nuevoListado','confirmar','mensaje_popUp12'], {parameters: 'agregarConfiguracion=OK',popWait:true,evalScripts:true});"><img src="./images/exito_16.gif" border="0"></html:link>
																									</logic:notEmpty>
																								</td>
																							</tr>
																						</table>
																						<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
																							<div id="divlistado" style="width:100%;height:250px;overflow:auto">						
																								<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
																									<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDetPlaLocHorCamDTOCol">
																											
																										<bean:define id="numeroFila" value="${0}"/>
																										<logic:iterate name="ec.com.smx.calendarizacion.calendarioDetPlaLocHorCamDTOCol" id="calendarioDetPlaLocHorComDTO" indexId="numeroDetalle">
																											<bean:define id="numFila" value="${numeroFila % 2}"/>
																											<logic:equal name="numFila" value="0">	
																												<bean:define id="color" value="grisClaro"/>
																											</logic:equal>
																											<logic:equal name="numFila" value="1">	
																												<bean:define id="color" value="blanco"/>
																											</logic:equal>
																											
																											<logic:notEqual name="calendarioDetPlaLocHorComDTO" property="calendarioDetallePlantillaLocalDTO.npEstadoDetalle" value="${estAnuladoPlantilla}">
																												<bean:define id="opcionesDiasOBJ" name="calendarizacionForm" property="dias[${calendarioDetPlaLocHorComDTO.calendarioDetallePlantillaLocalDTO.numeroDia}]"/>
																												<bean:define id="opcionesHorasDiaOBJ" name="calendarizacionForm" property="horasDia[${calendarioDetPlaLocHorComDTO.calendarioDetallePlantillaLocalHoraDTO.npPosicionHoras}]" />
																												
																												<tr class="${color}">
																													<!-- IMPORTANTE: Se modifica tabla de presentación de detalles de configuración -->
																													<td width="21%" class="textoNegro9" align="left">&nbsp;
																														<bean:write name="opcionesDiasOBJ" property="nombreDia"/>&nbsp;
																													</td>
																													<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
																													<td width="21%" class="textoNegro9" align="left">&nbsp;
																														<bean:write name="opcionesHorasDiaOBJ" property="horas" formatKey="formatos.hora"/>&nbsp;
																													</td>
																													<td width="21%" class="textoNegro9" align="left">&nbsp;
																														<bean:write name="calendarioDetPlaLocHorComDTO" property="calendarioDetallePlantillaLocalCamionDTO.npNombreTransporte"/>&nbsp;
																													</td>																																																					
																													
																													<td width="21%" align="left" class="textoNegro10">
																													
																														<bean:write name="calendarioDetPlaLocHorComDTO" property="calendarioDetallePlantillaLocalCamionDTO.cantidad" formatKey="formatos.enteros"/>
																														
																													</td>
																													</logic:notEmpty>
																													<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioloc">
																													<td width="48%" align="left" class="textoNegro10">
																														
																														<c:set var="cn" value="${calendarioDetallePlantillaLocalDTO.capacidadNormal}"/>
																														<c:set var="ca" value="${calendarioDetallePlantillaLocalDTO.capacidadAdicional}"/>
																														<c:set var="sum" value="${cn+ca}" />
																																																								
																														<bean:write name="sum" formatKey="formatos.enteros"/>
																																		
																													</td>
																													</logic:notEmpty>																																																
																													<td width="10%" align="right">		
																														<html:link title="Eliminar" href="#" onclick="requestAjax('calendarizacion.do', ['nuevoListado'], {parameters: 'eliminarDetallesConfiguracion=${numeroDetalle}',popWait:true});">
																															<html:img src = "./images/eliminar.gif" border="0"/>
																														</html:link>
																													</td>
																												</tr>
																												<bean:define id="numeroFila" value="${numeroFila + 1}"/>
																											</logic:notEqual>	
																										</logic:iterate>
																									</logic:notEmpty>
																								</table>
																							</div>
																						</logic:notEmpty>
																						<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioloc">
																							<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTONueva">
																								<bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTONueva"/>
																							</logic:notEmpty>
																							<div id="divlistado1" style="width:100%;height:250px;overflow:auto">						
																									<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center" >
																										<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTONueva">
																											<bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTONueva"/>	
																											<bean:define id="calendarioDetallePlantillaLocalDTOCol" name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal"/>	
																											<bean:define id="numeroFila" value="${0}"/>
																											<logic:iterate name="calendarioDetallePlantillaLocalDTOCol" id="calendarioDetallePlantillaLocalDTO" indexId="numeroDetalle">
																												<bean:define id="numFila" value="${numeroFila % 2}"/>
																												<logic:equal name="numFila" value="0">	
																													<bean:define id="color" value="grisClaro"/>
																												</logic:equal>
																												<logic:equal name="numFila" value="1">	
																													<bean:define id="color" value="blanco"/>
																												</logic:equal>
																												<logic:notEqual name="calendarioDetallePlantillaLocalDTO" property="npEstadoDetalle" value="${estAnuladoPlantilla}">
																													<bean:define id="opcionesDiasOBJ" name="calendarizacionForm" property="dias[${calendarioDetallePlantillaLocalDTO.numeroDia}]"/>
																													<tr class="${color}">
																														<!-- IMPORTANTE: Se modifica tabla de presentación de detalles de configuración -->
																														<td width="51%" class="textoNegro9" align="left">&nbsp;
																															<bean:write name="opcionesDiasOBJ" property="nombreDia"/>&nbsp;
																														</td>																																																						
																														<%-- %><td width="26%" align="right" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/>&nbsp;</td>																																																
																														<td width="26%" align="right" class="textoNegro9">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/>&nbsp;</td>--%>
																														<td width="39%" align="left" class="textoNegro10">
																														
																															<c:set var="cn" value="${calendarioDetallePlantillaLocalDTO.capacidadNormal}"/>
																															<c:set var="ca" value="${calendarioDetallePlantillaLocalDTO.capacidadAdicional}"/>
																															<c:set var="sum" value="${cn+ca}" />
																																																								
																															<bean:write name="sum" formatKey="formatos.enteros"/>
																															
																														</td>																																																
																														<td width="10%" align="right">		
																															<html:link title="Eliminar" href="#" onclick="requestAjax('calendarizacion.do', ['confirmar','plantillas','mensajes','divlistado1'], {parameters: 'eliminarConfiguracionLink=${numeroDetalle}',popWait:true});">
																																<html:img src = "./images/eliminar.gif" border="0"/>
																															</html:link>
																														</td>
																													</tr>
																													<bean:define id="numeroFila" value="${numeroFila + 1}"/>
																												</logic:notEqual>	
																											</logic:iterate>
																										</logic:notEmpty>
																									</table>
																								</div>	
																						</logic:notEmpty>	
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
												</logic:empty>
											</div>
										</td>
									</tr>
									<tr><td height="5px"></td></tr>  											
                             		<tr>
										<td align="center">				
											<table cellpadding="0" cellspacing="0" border="0">
												<tr>                                                    
													<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
														<td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('calendarizacion.do', ['pregunta','div_pagina','mensaje_popUp12','mensajes','listadoPlantilla','plantillas','calendarioDiv','divlistado'], {parameters: 'guardarDetalleConfiguracion=ok',popWait:true, evalScripts:true});ocultarModal();">Aplicar</html:link></div></td>
													</logic:notEmpty>
													<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioloc">
														<td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('calendarizacion.do', ['pregunta','div_pagina','mensaje_popUp12','mensajes','listadoPlantilla','plantillas','calendarioDiv','div_listado'], {parameters: 'guardarPlantilla=ok',popWait:true, evalScripts:true});ocultarModal();">Aplicar</html:link></div></td>
													</logic:notEmpty>
													<td>&nbsp;</td>                                                    
													<td><div id="botonD"><html:link href="#" styleClass="cancelarD" onclick="requestAjax('calendarizacion.do', ['pregunta','div_pagina','mensaje_popUp12'], {parameters: 'ocultarVentana=ok', popWait:false, evalScripts:true});ocultarModal();">Cancelar</html:link></div></td>                                                    
												</tr>
											</table>									
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</div>