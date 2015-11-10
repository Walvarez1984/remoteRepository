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

<div id="nuevoKardex" class="popup" style="top:-50px;">
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
			                                <b>Creaci&oacute;n</b> 
			                            </td>
			                            <td align="right">											
                                            <html:link title="Cerrar" href="#" onclick="requestAjax('kardex.do', ['preguntaCamion','div_pagina','mensaje_popUp12'], {parameters: 'ocultarPopupCamion=ok', popWait:false, evalScripts:true});ocultarModal();">
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
											<div id="mensaje_popUp123" style="width:420px;position:relative;" >
												<tiles:insert  page="/include/mensajes.jsp" />
											</div>
			            					<div id="cuerpo_popUp" style="height:110px; overflow-y:auto;overflow-x:hidden;">
			            																														
												<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
													<tr>
														<td align="left" colspan="2">
															
																<table border="0" cellspacing="3" align="left" width="100%" style="height:100px;">
																	<tr>
																		<td class="textoAzul11" >
																			Hora:				
																		</td>
																		<td >
																			<html:select property="horaSeleccionada" styleClass="textObligatorio" style="width:110px;">
																				<html:option value=""></html:option>
																				<html:options collection="ec.com.smx.calendarizacion.horasdiacol" labelProperty="horas" property="seleccion"/>
																			</html:select>
																		</td>																	
																	</tr>
																	<tr>
																		<td class="textoAzul11" >
																			Camiones:																		
																		</td>
																		<td>
																			<smx:select property="transporteSeleccionado" styleClass="comboObligatorio" styleError="campoError" style="width:300px;">
																				<html:option value=""></html:option>
																				<logic:notEmpty name="ec.com.smx.calendarizacion.transportedtocol">
																					<logic:iterate name="ec.com.smx.calendarizacion.transportedtocol" id="transporteDTO" indexId="indiceTransporte">
																						<html:option value="${transporteDTO.id.codigoTransporte}" styleClass="comboDescripcionCiudad">${transporteDTO.nombreTransporte} - (${transporteDTO.cantidadBultos} Bultos)</html:option>																
																					</logic:iterate>
																				</logic:notEmpty>
																			</smx:select>
																		</td>
																	</tr>
																	<tr>
																		<td class="textoAzul11">
																			N&uacute;m camiones:
																		</td>
																		<td>
																			<smx:text property="numCamiones" size="18" style="width:110px;" styleClass="textObligatorio" styleError="campoError" maxlength="3" onkeypress="return validarInputNumeric(event);"/>
																		</td>
																	</tr>																
															  </table>
														  
														</td>
													</tr>
												</table>
												</div>												
											
										</td>
									</tr>
									<tr><td height="5px"></td></tr>  											
                             		<tr>
										<td align="center">				
											<table cellpadding="0" cellspacing="0" border="0">
												<tr>                                                    
													<logic:notEmpty name="ec.com.smx.calendarizacion.calendariobod">
														<td><div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('kardex.do', ['preguntaCamion','horasCamion','mensaje_popUp123','mensajes','detalleHorasCamion','tblCantidades'], {parameters: 'botonAgregarHorasCamion=ok',popWait:true, evalScripts:true});ocultarModal();">Aplicar</html:link></div></td>
													</logic:notEmpty>													
													<td>&nbsp;</td>                                                    
													<td><div id="botonD"><html:link href="#" styleClass="cancelarD" onclick="requestAjax('kardex.do', ['preguntaCamion','div_pagina','mensaje_popUp123'], {parameters: 'ocultarPopupCamion=ok', popWait:false, evalScripts:true});ocultarModal();">Cancelar</html:link></div></td>                                                    
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