<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">
<html:html>
<head>
	<TITLE>RESERVAS</TITLE>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<tiles:insert page="/include/metas.jsp"/>
	<BASE target="_self">
</head>

<bean:define id="estReservado" name="ec.com.smx.calendarizacion.estadoMovimiento.reservado"/>
<bean:define id="movIngreso" name="ec.com.smx.calendarizacion.ingreso"/>
<bean:define id="movEgreso" name="ec.com.smx.calendarizacion.egreso"/>

<bean:define id="colorReservado"><bean:message key="color.colorReservado"/></bean:define>
<bean:define id="colorIngresos"><bean:message key="color.colorIngreso"/></bean:define>
<bean:define id="colorEgresos"><bean:message key="color.colorEgreso"/></bean:define>
<bean:define id="colorAnulado"><bean:message key="color.colorAnulado"/></bean:define>

<body bottommargin="0" leftmargin="0" rightmargin="0" topmargin="0" >
	<html:form action="condicionReserva" method="POST">
		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textos" >
			<tr><td id="mensaje">		
				<%@ include file="../include/mensajes.jsp"%>				
			</td></tr>
			<!--LINEA ROJA CAB-->
			<tr>
				<td class="barraTituloBG">&nbsp;CORPORACI&Oacute;N FAVORITA C.A.</td>
			</tr> 
			<!--CABECERA-->
			<tr><td height="5"><input type="hidden" name="ayuda" value=""></td></tr>
			<!--CUERPO-->
			<tr>  
				 <td height="70" valign="top" align="left">
					<table align="center" border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td class="titulosAccion" height="57px">
								<table border="0" width="100%" cellspacing="0" cellpadding="0">
								    <tr>
										<td  width="3%" align="center"><%--<img src="images/adjuntar_mail.gif" border="0"></img>--%></td>
										<td align="left">&nbsp;&nbsp;</td>
										<td align="right">
											<table cellspacing="0">
												<tr>
													<td>
														<logic:empty name="ec.com.smx.calendarizacion.kardexIngresado">
															<div id="botonA">	
																<html:link styleClass="aceptarA" href="#" onclick="requestAjax('condicionReserva.do', ['cerrarVentana','mensajes'], {parameters: 'grabarCondiciones=ok',popWait:true,evalScripts:true});">Aceptar</html:link>
															</div>
														</logic:empty>	
													</td>
													<td>
														<div id="botonA">	
															<html:link styleClass="cancelarA" href="#" onclick="window.close();">Cancelar</html:link>
														</div>
													</td>
													<td width="2%"></td>
												</tr>
											</table>		
										</td>
								    </tr>
								</table>
							</td>
						</tr>
						<tr><td><br></td></tr>
						<tr>
							<td>
								<table align="center" border="0" width="95%" cellpadding="0" cellspacing="0" class="tabla_informacion">
									<tr>
										<td width="100%">
											<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"  class="tabla_informacion" height="18px">
												<tr>
													<td width="20%" align="center" class="tituloTablas columna_contenido"><B>HORA</B></td>
													<td width="20%" align="center" class="tituloTablas columna_contenido"><B>TIPO MOV.</B></td>																	
													<td width="29%" align="center" class="tituloTablas columna_contenido"><B>MOTIVO MOV.</B></td>
													<td width="21%" align="center" class="tituloTablas columna_contenido"><B>CONCEPTO</B></td>											
													<td width="10%" align="center" class="tituloTablas columna_contenido"><B>CANTIDAD</B></td>											
												</tr>	
											</table>
										</td>
									</tr>
									<tr>
										<td width="100%" id="listado">
											<table class="tabla_informacion" border="0" cellpadding="1" cellspacing="1" width="100%" align="center">
												<bean:define id="calendarioDetalleDiaLocalDTO1" name="ec.com.smx.calendarizacion.calendarioDetalleDiaLocalDTO"/>
												<bean:define id="color" value="${colorAnulado}"/>
												<tr bgcolor="#${color}" height="18px">
													<td width="20%" class="textoNegro10" align="center">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO1" property="horaDesde"/>&nbsp;</td>
													<td width="20%" align="center" class="textoNegro10">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO1" property="calendarioTipoMovimientoDTO.descripcionTipoMovimiento"/>&nbsp;</td>																																																
													<td width="29%" align="center" class="textoNegro10">&nbsp;
												  <bean:write name="calendarioDetalleDiaLocalDTO1" property="calendarioMotivoMovimientoDTO.descripcionMotivoMovimiento"/>&nbsp;</td>
													<td width="21%" align="left" class="textoNegro10">&nbsp;
												  <bean:write name="calendarioDetalleDiaLocalDTO1" property="conceptoMovimiento"/>&nbsp;</td>
													<td width="10%" align="center" class="textoNegro10">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO1" property="cantidadAlmacenamiento"/>&nbsp;</td>	
												</tr>
												<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1">
													<logic:iterate name="ec.com.smx.calendarizacion.calendarioDetalleDialLocalDTOCol1" id="calendarioDetalleDiaLocalDTO" indexId="numeroDetalle">
														<logic:equal name="calendarioDetalleDiaLocalDTO" property="calendarioMotivoMovimientoDTO.codigoTipoMovimiento" value="${movIngreso}">
															<bean:define id="color" value="${colorIngresos}"/>
														</logic:equal>
														<logic:equal name="calendarioDetalleDiaLocalDTO" property="calendarioMotivoMovimientoDTO.codigoTipoMovimiento" value="${movEgreso}">
															<bean:define id="color" value="${colorEgresos}"/>
														</logic:equal>
														<logic:equal name="calendarioDetalleDiaLocalDTO" property="estadoMovimiento" value="${estReservado}">
															<bean:define id="color" value="${colorReservado}"/>
														</logic:equal>
														<tr bgcolor="#${color}" height="18px">
															<td width="20%" class="textoNegro10" align="center">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO" property="horaDesde"/>&nbsp;</td>
															<td width="20%" align="center" class="textoNegro10">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO" property="calendarioTipoMovimientoDTO.descripcionTipoMovimiento"/>&nbsp;</td>																																																
															<td width="29%" align="center" class="textoNegro10">&nbsp;
														  <bean:write name="calendarioDetalleDiaLocalDTO" property="calendarioMotivoMovimientoDTO.descripcionMotivoMovimiento"/>&nbsp;</td>
															<td width="21%" align="left" class="textoNegro10">
																<logic:empty name="ec.com.smx.calendarizacion.kardexIngresado">
																	&nbsp;<smx:textarea rows="2" cols="25" name="calendarioDetalleDiaLocalDTO" property="conceptoMovimiento" styleClass="textObligatorio" value="${calendarioDetalleDiaLocalDTO.conceptoMovimiento}"/>&nbsp;
																</logic:empty>
																<logic:notEmpty name="ec.com.smx.calendarizacion.kardexIngresado">
																	&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO" property="conceptoMovimiento"/>&nbsp;
																</logic:notEmpty>	
														  </td>
															<td width="10%" align="center" class="textoNegro10">&nbsp;<bean:write name="calendarioDetalleDiaLocalDTO" property="cantidadAlmacenamiento"/>&nbsp;</td>	
														</tr>
													</logic:iterate>
												</logic:notEmpty>
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
				<td id="cerrarVentana">
					<logic:notEmpty name="ec.com.smx.calendarizacion.cerrarVentana">
						<script language="javascript">
							window.close();
						</script>
					</logic:notEmpty>	
				</td>
			</tr>	
		</table>	
	</html:form>
</body>	
</html:html>