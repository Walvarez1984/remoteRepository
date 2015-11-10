<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana insertada desde entregaArticuloLocal.jsp -->

<bean:define id="vformName" value="cotizarRecotizarReservarForm"/>
<bean:define id="vformAction" value="entregaLocalCalendario.do"/>
	
<div id="popupArchivoCroquis" class="popup"  style="top:100px;z-index:121;"> 	
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="50%">
		<tr>
			<td valign="top" align="center">
			    <div id="center" class="popupcontenido">
					<%-- Contenido de la ventana de confirmacion --%>					
			        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="62%">
			            <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Cargar archivo croquis</b>
			                            </td>
			                            <td align="right">										

											 <html:link styleClass="noDmin" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupArchivoCroquis'], {parameters: 'botonCancelarArchCroquis=ok', popWait:false, evalScripts:true});ocultarModalID('frameModal2');mostrarModal();">
												<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
											 </html:link>										                                                                                    
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            <tr>
			                <td bgcolor="#F4F5EB" valign="top">	                            
			                    <table class="tabla_informacion textoNegro11" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">			                       								
	                                <tr>								   		                                                 
                                        <td>
                                        	<table class="tabla_informacion fondoBlanco" border="0" width="500px"  align="center" cellpadding="0" cellspacing="5">                                            
												<tr>													
													<td align="left" valign="top" colspan="2">	
														<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
															<tr>
																 
																	<td align="left">
																		Ruta del archivo : 
																	</td> 
																	<td align="left">
																		<html:file size="50" property="archivo" styleClass="textObligatorio" onkeydown="return false;"  onchange="requestAjax('entregaLocalCalendario.do', ['pregunta'], {parameters: 'adjuntarArchivo=ok', popWait:false, evalScripts:true});"/>
																	</td>
																 
															</tr>
															<tr>
																<td height="10px"></td> 
															</tr>
															<tr>
																<td width="100%" colspan="2">
																	<table width="100%" cellpadding="0" cellspacing="0" border="0">
																		<tr>
																			<td>
																				<table border="0" width="100%" align="center" cellspacing="0" cellpadding="1" class="tabla_informacion_encabezado">
																					<tr>
																						<td width="40%" align="center" class="tituloTablas" height="20px">Archivos Adjuntos</td>
																						<td width="38%" align="center" class="tituloTablas" height="20px">Tamaño</td>
																						 <logic:empty name="ec.com.sic.sispe.boton.benenficiario"> 
																							 <td width="22%" align="left" class="tituloTablas" height="20px">Eliminar&nbsp;&nbsp;&nbsp;Descargar</td>
																						 </logic:empty>
																						 <logic:notEmpty name="ec.com.sic.sispe.boton.benenficiario">
																							<td width="22%" align="center" class="tituloTablas" height="20px">Descargar</td>
																						 </logic:notEmpty>	
																					</tr>	
																				</table>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<logic:notEmpty name="ec.com.smx.sic.sispe.archivo.beneficiario">
																					<div id="archivosCargados" style="width:100%;height:80px;overflow-y:auto;overflow-x:hidden;">
																						<table border="0" width="100%" align="center" cellspacing="0" cellpadding="1" class="tabla_informacion_encabezado">
																							<logic:iterate id="archivoDTO" name="ec.com.smx.sic.sispe.archivo.beneficiario"  indexId="indiceArchivo">
																								<bean:define id="numFilaO" value="${indiceArchivo % 2}"/>
																								<smx:equal name="numFilaO" valueKey="valor.numero.false">
																									<bean:define id="color" value="celeste"/>
																								</smx:equal>
																								<smx:equal name="numFilaO" valueKey="valor.numero.true">
																									<bean:define id="color" value="blanco"/>
																								</smx:equal>
																								<tr class="${color}">
																									<td width="40%" align="left" class="fila_contenido columna_contenido" height="14px">
																										<bean:write name="archivoDTO" property="nombreArchivo"/>
																									</td>
																									<td width="35%" align="center" class="fila_contenido columna_contenido" height="14px">
																										<c:set var="valorc" value="${archivoDTO.tamanioArchivo / 1024}"></c:set>
																											<bean:write name="valorc" formatKey="formatos.numeros.decimales" />
																									</td>
																									<td width="25%" align="center" class="fila_contenido columna_contenido columna_contenido_der" height="14px">
																										<table width="100%">
																											<tr>
																												<logic:empty name="ec.com.sic.sispe.boton.benenficiario"> 
																													<td width="50%">
																														<html:link href="#" onclick="requestAjax('${vformAction}.do', ['archivosCargados'],{parameters: 'eliminarArchivo=${archivoDTO.id.secuencialArchivoPedido}',evalScripts:true});popWait('div_wait');" title="Eliminar archivo">
																															<img src="./images/eliminarFoto.gif" border="0" title="Eliminar archivo" />
																														</html:link>
																													</td>
																												</logic:empty> 
																												<td width="50%">
																													<html:link title="Descargar archivo" 
																														paramId="indice" paramName="indiceArchivo" action="${vformAction}?verArchivo=${indiceArchivo}">
																														<img src="./images/load.gif" border="0" title="Descargar archivo" />
																													</html:link>
																												</td>
																											</tr>
																										</table>
																									</td>
																								</tr>
																							</logic:iterate>
																						</table>
																					</div>
																				</logic:notEmpty>					
																				<logic:empty name="ec.com.smx.sic.sispe.archivo.beneficiario">
																					<div id="archivosCargados" style="width:100%;height:80px;overflow-y:auto;overflow-x:hidden;">
																						<table border="0" cellpadding="0" cellspacing="0" width="100%">
																							<tr>
																								<td align="center" height="100px" class="tabla_informacion amarillo1">
																									No ha subido ningún Archivo.
																								</td>
																							</tr>
																						</table>
																					</div>
																				</logic:empty>
																			</td>
																		</tr>	
																	</table>
																</td>
															</tr>	
														</table>
													</td>
                                                </tr>                                                
                                            </table>                                            
                                        </td>
                                    </tr>
                                    <tr>
                                    	<td align="center">
                                            <table cellpadding="0" cellspacing="0" border="0">
                                                <tr>                                                                                                   
                                                    <td><div id="botonD">
													
													<html:link styleClass="aceptarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupArchivoCroquis'], {parameters: 'botonAceptarArchivoCroquis=ok', evalScripts:true});ocultarModalID('frameModal2');mostrarModal();">Aceptar</html:link>
													</div></td>
													<td>&nbsp;</td>
                                                    <td><div id="botonD">
													<html:link styleClass="cancelarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['popupArchivoCroquis'], {parameters: 'botonCancelarArchCroquis=ok', popWait:false, evalScripts:true});ocultarModalID('frameModal2');mostrarModal();">Cancelar</html:link></div></td>
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