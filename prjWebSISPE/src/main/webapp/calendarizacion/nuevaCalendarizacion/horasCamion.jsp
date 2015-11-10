<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>


<div id="detalleHorasCamion">
<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
	<tr>
		<td class="fila_titulo" colspan="7" height="29px">
			<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
				<tr>
					<td width="40" align="right"><img src="images/Kardex24.gif" border="0"></td>
					<td width="63">&nbsp;Camiones:</td>
					<td width="514">
						<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO">
							<bean:define id="calendarioDiaLocalDTO" name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO"/>
							<bean:write name="calendarioDiaLocalDTO" property="id.fechaCalendarioDia" formatKey="formato.fecha.letras"/>
						</logic:notEmpty>
					</td>
					<td width="357" align="right">
						<div id="botonD">
							<html:link styleClass="agregarD" href="#" onclick="requestAjax('kardex.do', ['preguntaCamion','mensajes'], {parameters: 'abrirPopupNuevoCamion=ok',popWait:true, evalScripts:true});">Agregar</html:link>
						</div>
					</td>
					<td width="1"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<logic:empty name="ec.com.smx.sic.sispe.horascamionresp">
				<table class="textoNegro11" cellpadding="0" cellspacing="0"
					align="left" width="100%">
						<tr>
							<td width="100%" height="400px"> 
								<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0" >
									<tr>						
										<td align="center" class="textoNegro11">
											No existe camiones para la fecha : 
											<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO">
												<bean:define id="calendarioDiaLocalDTO" name="ec.com.smx.calendarizacion.calendarioDiaLocalDTO"/>
												<bean:write name="calendarioDiaLocalDTO" property="id.fechaCalendarioDia" formatKey="formato.fecha.letras"/>
											</logic:notEmpty>										
										</td>
									</tr>
								</table>
			
							</td>
						</tr>
				</table>
			</logic:empty>
		</td>
	</tr>
	<tr>
		<td id="preguntaCamion">									
			<logic:notEmpty name="ec.com.smx.calendarizacion.mostrarpopupkardexcamion">
				<script language="javascript">
					$j(document).ready(function(){
						mostrarModal();						
					});
				</script>
				<tiles:insert page="/calendarizacion/nuevaCalendarizacion/nuevaKardexCamion.jsp"/>
				
			</logic:notEmpty>															
		</td>
	</tr>
	<tr><td colspan="7" height="7"></td></tr>
	<tr>
		<td align="center" id="detalles" colspan="7">
			<div id="divHorasCamion" style="width:100%;height:345px;overflow-y:auto;overflow-x:hidden;">
				<table class="textoNegro11" cellpadding="0" cellspacing="0"	align="left" width="100%">
					<tr>
						<td height="3px"></td>
					</tr>
					<tr>
						<td>
							<logic:notEmpty name="ec.com.smx.sic.sispe.horascamionresp">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<div id="horasCamion">
												<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
													<tr><td height="6px"></td></tr>
													<tr>
														<td valign="top">
															<table border="0" cellspacing="0" cellpadding="0" width="98%" align="center" style="border-top:#cccccc 1px solid" >
																<tr>
																	<td colspan="9">
																		<div id="listadoHorasCamion" >
																			<table border="0" cellspacing="0" cellpadding="0" width="100%">
																				<bean:define id="contadorCheckTransitoHC" value="0"></bean:define>
																				<logic:iterate name="ec.com.smx.sic.sispe.horascamionresp" id="horaCam" indexId="numDetalleHC">
																					<bean:define id="contadorSeleccionHC" value="${0}"/>
																					<bean:define id="numFilaHC" value="${numDetalleHC + 1}"/>
																					<%-- control del estilo para el color de las filas --%>
																					<bean:define id="residuoHC" value="${numDetalleHC % 2}"/>
																					<c:set var="claseHC" value="blanco10"/>
																					<c:set var="colorBackHC" value="#ffffff"/>
																					<logic:notEqual name="residuoHC" value="0">
																						<c:set var="claseHC" value="grisClaro10"/>
																						<c:set var="colorBackHC" value="#EBEBEB"/>
																					</logic:notEqual>
																					<tr>
																						<td>
																							<table id="table_${numDetalleHC}" cellspacing="0" cellpadding="0" width="100%" style="border-width: 1px;border: 1px solid; border-color: #7de44a;">
																								<tr id="fila_en_${numDetalleHC}">
																									<td width="4%" align="center" height="25"  class="${claseHC} textoNegro10 columna_contenido_blanco">
																										<div id="desplegar${numDetalleHC}">
																											<logic:notEmpty name="horaCam" property="npDetalleCalCamHorLoc">
																												<a title="Ver Detalle de Horas Cami&oacute;n" href="#" onClick="plegarA(${numDetalleHC});">
																													<html:img src="images/plegar.gif" border="0"/>
																												</a>
																											</logic:notEmpty>
																										</div>
																										<div id="plegar${numDetalleHC}" class="displayNo">
																											<a title="Ver Detalle de Horas Cami&oacute;n" href="#" onClick="desplegarA(${numDetalleHC});">
																												<html:img src="images/desplegar.gif" border="0"/>
																											</a>
																										</div>
																									</td>
																									<td class="${claseHC} textoNegro10 columna_contenido_blanco" align="center" width="5%"><bean:write name="numFilaHC"/></td>															
																									<td class="${claseHC} textoNegro10 columna_contenido_blanco" align="left" width="40%" >
																										<bean:define id="opcionesHorasDiaOBJ" name="kardexForm" property="horasDia[${horaCam.npPosicion}]" />
																										Hora:&nbsp;&nbsp;<bean:write name="opcionesHorasDiaOBJ" property="horas" formatKey="formatos.hora"/>
																									</td>
																								</tr>
																							</table>
																						</td>
																					</tr>
																					<tr>
																						<td>
																							<div id="marco${numDetalleHC}">
																								<logic:notEmpty name="horaCam" property="npDetalleCalCamHorLoc">
																									<table border="0" width="100%" cellpadding="0" cellspacing="0">
																										<tr>
																											<td width="4%"></td>
																											<td align="left">
																												<table border="0" width="99%" cellspacing="0" cellpadding="0">
																													<tr>
																														<td>
																															<table class="tabla_informacion" border="0" width="100%" cellspacing="0" cellpadding="0">
																																<tr>
																																	<td width="5%" align="right" class="tituloTablas columna_contenido"></td>																									
																																	<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido" >TRANSPORTE</td>
																																	<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido">CAPACIDAD</td>
																																	<td width="20%" align="center" class="subtituloTablasEntregaCliente columna_contenido" >CANTIDAD</td>
																																	<td class="columna_contenido subtituloTablasEntregaCliente" align="center" width="20%">TOTAL</td>																																		
																																</tr>
																															</table>
																														</td>
																													</tr>
																													<tr>
																														<td width="100%" align="left" colspan="6">
																															<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="left">
																																<logic:iterate name="horaCam" property="npDetalleCalCamHorLoc" id="detalle" indexId="numHorasCamion">
																																	<bean:define id="residuoEHC" value="${numHorasCamion % 2}"/>
																																	<c:set var="indiceHorasCamion" value="${indiceHorasCamion + 1}"/>
																																	<logic:equal name="residuoEHC" value="0">
																																		<bean:define id="colorBackHC" value="grisClaro10"/>
																																	</logic:equal>
																																	<logic:notEqual name="residuoEHC" value="0">
																																		<bean:define id="colorBackHC" value="blanco10"/>
																																	</logic:notEqual>
																																	
																																	<tr class="${colorBackHC}">
																																		<td width="5%" align="center" class="textoNegro9">
																													
																																		</td>
																																		
																																		<td width="20%" align="left" class="textoNegro9">
																																		<bean:write name="detalle" property="npNombreTransporte"/>
																																		</td>																											
																																		<td width="20%" align="center" class="textoNegro9">
																																			 <bean:write name="detalle" property="npCantidadBultos"/>
																																		</td>
																																		<td width="20%" align="center" class="textoNegro9" >
																																			<bean:write name="detalle" property="npNumerocamiones"/>	
																																		</td>
																																		<td width="20%" align="center" class="textoNegro9">
																																			<c:set var="cn" value="${detalle.npNumerocamiones}"/>
																																			<c:set var="ca" value="${detalle.npCantidadBultos}"/>
																																			<c:set var="mul" value="${cn*ca}" />
																																			<bean:write name="mul" formatKey="formatos.enteros"/>
																																		</td>
																																																																				
																																	</tr>
																																</logic:iterate>
																															</table>
																														</td>
																													</tr>
																												</table>
																											</td>																								
																										</tr>
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
															</table>
														</td>
													</tr>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</logic:notEmpty>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr><td colspan="7" height="7"></td></tr>
</table>
</div>