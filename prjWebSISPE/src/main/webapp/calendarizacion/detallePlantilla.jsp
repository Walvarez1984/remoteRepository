<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana llamada desde plantillas.jsp -->
<bean:define id="estDefecto" name="ec.com.smx.calendarizacion.porDefecto"/>
<bean:define id="estAnuladoPlantilla" name="ec.com.smx.calendarizacion.estadoAnuladoPlantilla"/>

<logic:notEmpty name="ec.com.smx.calendarizacion.detallePlantilla">
	<bean:define id="opPlantilla" name="ec.com.smx.calendarizacion.detallePlantilla"/>
    <table width="251px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
        <%-- informacion de la plantilla--%>
        <tr>
            <td class="fila_titulo" colspan="4" height="29px">
                <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                    <tr>
                        <td width="50%">&nbsp;
							Detalle Plantilla
						</td>
						<td align="right" width="49%" valign="bottom">
							<c:if test="${opPlantilla == 'ver'}">
								<logic:empty name="ec.com.smx.calendarizacion.eliminarPlantilla">
									<div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('calendarizacion.do',['mensajes','actualizaCalendario','plantillas'],{parameters: 'aplicarCalendario=ok'})">Aplicar</html:link></div>				
								</logic:empty> 
							</c:if>
							<c:if test="${opPlantilla == 'nuevo'}">
								<div id="botonD"><html:link href="#" styleClass="guardarD" onclick="requestAjax('calendarizacion.do',['mensajes','plantillas','listadoPlantilla','actualizaCalendario'],{parameters: 'guardarPlantilla=ok',popWait:true})">Guardar</html:link></div>				
							</c:if>
						</td>	
						<td width="2px"></td>		
	                </tr>
                </table>
            </td>
        </tr>
		<c:if test="${opPlantilla == 'ver'}">
        	<bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO"/>	
	        <tr height="168px">
	            <td align="left" colspan="2">
	                <table border="0" cellspacing="0" align="left" width="100%">
						<tr>
	                        <td class="textoAzul11" width="40%" valign="top">
								&nbsp;Nombre:                        
							</td>
	                        <td class="textoNegro11">
						        <bean:write name="calendarioPlantillaLocalDTO" property="nombrePlantilla"/>	
	                        </td>	
						</tr> 
						<c:if test="${calendarioPlantillaLocalDTO.estadoPlantillaLocal == '2'}">
							<tr>
		                        <td class="textoAzul11">
									&nbsp;Por Defecto:
								</td>
								<td>	
						        	<smx:checkbox property="estadoPlantilla" disabled="true" valueKey="ec.com.smx.sic.sispe.calendarizacion.plantilla.defecto"/>		
		                        </td>	
							</tr>
						</c:if>				    	                                    								
						<tr>
	                        <td class="textoAzul11">
								&nbsp;Color:
	                        </td>
	                        <td class="textoNegro11">
						        <bean:define id="colorPlantilla" name="calendarioPlantillaLocalDTO" property="codigoColorPrincipal"/>
						        <bean:define id="nombreColorPrincipal" name="ec.com.smx.calendarizacion.calendarioColorDTO" property="nombreColorPrincipal"/>
						        <table bgcolor="${colorPlantilla}" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
									<tr>
										<td class="paleta" title="${nombreColorPrincipal}">
										</td>
									</tr>
								</table>
                             </td>	
						</tr>
	                    <tr>
	                        <td class="textoNegro11" colspan="2"><b>
								&nbsp;Configuraci&oacute;n:
	                        </b></td>
						</tr>
						<tr>	
	                        <td colspan="2">
								<table border="0" class="textos" width="100%" align="center" cellspacing="0" cellpadding="0">
									<tr>
										<td width="100%">
											<table border="0" width="98%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion">
												<tr>
													<!-- IMPORTANTE: Se modifica la cabecera de la sección de configuración de plantillas -->
													<%-- %><td width="34%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
													<td width="33%" align="center" class="tituloTablas columna_contenido"><B>CN</B></td>																	
													<td width="33%" align="center" class="tituloTablas columna_contenido"><B>CA</B></td>--%>
													
													<td width="52%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
													<td width="48%" align="center" class="tituloTablas columna_contenido"><B>CPE</B></td>																														
												</tr>	
											</table>
										</td>
									</tr>
									<tr>
										<td  width="100%" align="center">		
											<div id="div_listado" style="width:98%;height:70px;overflow:auto">						
												<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
													<logic:notEmpty name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal">
														<bean:define id="calendarioDetallePlantillaLocalDTOCol" name="calendarioPlantillaLocalDTO" property="calendarioDetallesPlantillaLocal"/>	
														<logic:iterate name="calendarioDetallePlantillaLocalDTOCol" id="calendarioDetallePlantillaLocalDTO" indexId="numeroDetalle">
															<bean:define id="numFila" value="${numeroDetalle % 2}"/>
															<logic:equal name="numFila" value="0">	
																<bean:define id="color" value="grisClaro"/>
															</logic:equal>
															<logic:equal name="numFila" value="1">	
																<bean:define id="color" value="blanco"/>
															</logic:equal>
															<logic:notEqual name="calendarioDetallePlantillaLocalDTO" property="npEstadoDetalle" value="${estAnuladoPlantilla}">
																<bean:define id="opcionesDiasOBJ" name="calendarizacionForm" property="dias[${calendarioDetallePlantillaLocalDTO.numeroDia}]"/>
																<tr class="${color}">
																	<td width="52%" class="textoNegro9" align="left">&nbsp;
																		<bean:write name="opcionesDiasOBJ" property="nombreDia"/>
																	</td>
																	<!-- IMPORTANTE: Se muestra la suma de la cantidad normal y la cantidad adicional en CPE -->	
																	<%-- %><td width="48%" align="left" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadNormal" formatKey="formatos.enteros"/>&nbsp;</td>																																																
																	<td width="33%" align="right" class="textoNegro10">&nbsp;<bean:write name="calendarioDetallePlantillaLocalDTO" property="capacidadAdicional" formatKey="formatos.enteros"/>&nbsp;</td>--%>
																																																			
																	<td width="48%" align="left" class="textoNegro10">
																		
																		<c:set var="cn" value="${calendarioDetallePlantillaLocalDTO.capacidadNormal}"/>
																		<c:set var="ca" value="${calendarioDetallePlantillaLocalDTO.capacidadAdicional}"/>
																		<c:set var="sum" value="${cn+ca}" />
																																												
																		<bean:write name="sum" formatKey="formatos.enteros"/>
																						
																	</td>
																</tr>
															</logic:notEqual>
														</logic:iterate>
													</logic:notEmpty>
												</table>
											</div>		
										</td>
									</tr>
								</table>	    	                                    	
	                        </td>	
						</tr>
		            </table>
	            </td>
	        </tr>
		</c:if>	
		<c:if test="${opPlantilla == 'nuevo'}">
	        <tr height="168px">
	            <td align="left" colspan="2">
	                <table border="0" cellspacing="3" align="left" width="100%">
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
											<html:link styleClass="paleta" title="${nombreColorPrincipal}" href="#" onclick="requestAjax('calendarizacion.do', ['colores','mensajes'], {parameters: 'seleccionarColor=OK',popWait:true});mostrarModal();">
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
														<!-- IMPORTANTE: Se modifica cabecera de configuración de plantilla -->
														<%-- %><td width="38%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
														<td width="26%" align="center" class="tituloTablas columna_contenido"><B>CN</B></td>																	
														<td width="26%" align="center" class="tituloTablas columna_contenido"><B>CA</B></td>
														<td width="10%" align="right" class="tituloTablas columna_contenido" colspan="2"></td>--%>
														
														<td width="51%" align="center" class="tituloTablas columna_contenido"><B>D&iacute;a</B></td>
														<td width="39%" align="center" class="tituloTablas columna_contenido"><B>CPE</B></td>																															
														<td width="10%" align="right" class="tituloTablas columna_contenido" colspan="2"></td>
													</tr>	
												</table>
											</td>
										</tr>
										<tr>
											<td  width="100%" id="listado">
												<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
													<tr>
														<td width="51%" class="textoNegro9" align="center" height="25px">
															<html:select property="diaSeleccionado" styleClass="textObligatorio">
																<html:option value=""></html:option>
																<html:options collection="ec.com.smx.calendarizacion.opcionesDiasCol" labelProperty="nombreDia" property="seleccion"/>
															</html:select>
														</td>
														<td width="39%" align="center" class="textoNegro9 columna_contenido">
															<smx:text property="cn" size="10" styleClass="textObligatorio" styleError="campoError"/>
														</td>				
																										
														<!-- IMPORTANTE: Se comenta el campo de cantidad adicional -->																																
														<%-- %><td width="26%" align="center" class="textoNegro9 columna_contenido">
															<smx:text property="ca" size="5" styleClass="textObligatorio" styleError="campoError" onkeypress="requestAjaxEnter('calendarizacion.do', ['listado','mensajes','confirmar'], {parameters: 'agregarConfiguracion=OK',popWait:true});"/>
														</td>--%>
														
														<td width="10%" align="center" colspan="2" class="columna_contenido">
															<html:link title="Agregar Configuraci&oacute;n" href="#" onclick="requestAjax('calendarizacion.do', ['listado','mensajes','confirmar'], {parameters: 'agregarConfiguracion=OK',popWait:false});"><img src="./images/exito_16.gif" border="0"></html:link>
														</td>
													</tr>
												</table>
												<div id="divlistado" style="width:100%;height:50px;overflow:auto">						
													<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
														<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO">
															<bean:define id="calendarioPlantillaLocalDTO" name="ec.com.smx.calendarizacion.calendarioPlantillaLocalDTO"/>	
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
																			<html:link title="Eliminar" href="#" onclick="requestAjax('calendarizacion.do', ['confirmar','plantillas','mensajes'], {parameters: 'eliminarConfiguracionLink=${numeroDetalle}',popWait:true});">
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
											</td>
										</tr>
									</table>	    	                                    	
		                        </td>	
							</tr>
						</logic:empty>	
	              </table>
	            </td>
	        </tr>
		</c:if>	               
    </table>
</logic:notEmpty>