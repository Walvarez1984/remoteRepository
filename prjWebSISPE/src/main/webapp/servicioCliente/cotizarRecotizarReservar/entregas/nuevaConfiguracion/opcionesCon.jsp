<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>

<bean:define id="lugarEntrega"><bean:message key="ec.com.smx.sic.sispe.lugarEntrega"/></bean:define>
<bean:define id="tipoEntrega"><bean:message key="ec.com.smx.sic.sispe.tipoEntrega"/></bean:define>
<bean:define id="stock"><bean:message key="ec.com.smx.sic.sispe.stock"/></bean:define>
<bean:define id="estadoActivo"><bean:message key="ec.com.smx.sic.sispe.estado.activo"/></bean:define>
<bean:define id="estadoInactivo"><bean:message key="ec.com.smx.sic.sispe.estado.inactivo"/></bean:define>


<logic:notEmpty name="ec.com.smx.sic.sispe.bloquearopcionentregadomicilio">
	<c:set var="bloqueoEntregaDomicilio" value="${estadoActivo}"/>
</logic:notEmpty>
<logic:empty name="ec.com.smx.sic.sispe.bloquearopcionentregadomicilio">
	<c:set var="bloqueoEntregaDomicilio" value="${estadoInactivo}"/>
</logic:empty>

<logic:notEmpty name="ec.com.smx.sic.sispe.bloqueoEstablecimientosSICMER">
	<c:set var="bloqueoSICMER" value="${estadoActivo}"/>
</logic:notEmpty>
<logic:empty name="ec.com.smx.sic.sispe.bloqueoEstablecimientosSICMER">
	<c:set var="bloqueoSICMER" value="${estadoInactivo}"/>
</logic:empty>

<logic:notEmpty name="ec.com.smx.sic.sispe.cantidad.canastos.entrega.bodega">
	<bean:define id="cantidadCanasto" name="ec.com.smx.sic.sispe.cantidad.canastos.entrega.bodega"/>
</logic:notEmpty>
<logic:empty name="ec.com.smx.sic.sispe.cantidad.canastos.entrega.bodega">
	<bean:define id="cantidadCanasto" value="0"/>
</logic:empty>
<%-- Cargo cada una de las configuraciones para las entregas --%>
<logic:notEmpty name="ec.com.smx.sic.sispe.entregas.configuracionDTOCol">
	<logic:iterate name="ec.com.smx.sic.sispe.entregas.configuracionDTOCol" id="configuracionDTO">
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${lugarEntrega}">
			<bean:define id="configLugarEntrega" name="configuracionDTO"/>
		</logic:equal>
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${tipoEntrega}">
			<bean:define id="configTipoEntrega" name="configuracionDTO"/>
		</logic:equal>
		<logic:equal name="configuracionDTO" property="id.codigoConfiguracion" value="${stock}">
			<bean:define id="configStock" name="configuracionDTO"/>
		</logic:equal>
	</logic:iterate>
</logic:notEmpty>
 
<div id="div_configuracionEntregasOpciones" style="width:100%;height:465px;overflow-y:auto;overflow-x:hidden;">
	<table class="fondoBlanco textoNegro11" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
		<tr>
			<td id="mensaje2">
				<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/entregas/mensajesPasos.jsp"/>
			</td>
		</tr>
	</table>
	<table class="fondoBlanco textoNegro11" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">	
		<c:set var="indiceLetra" value="A"/>	
		<logic:notEmpty name="ec.com.smx.sic.sispe.mostraropcioncanastosespeciales">
			<tr>
				<td width="6px" class="textoRojo12" style="	font-size: 16px;"><b>&nbsp;&nbsp;${indiceLetra})</b></td>
				<td align="left" valign="bottom">&nbsp;&nbsp;<b>Seleccione el responsable de la elaboraci&oacute;n de los canastos especiales</b></td>
				<c:set var="indiceLetra" value="B"/>	
			</tr>
			<tr>
				<td height="29px" colspan="2">
					<div id="canastosEspeciales">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
							<tr>
								<td>
									<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
										<tr><td height="6px"></td></tr>
										<tr>
												
												<td valign="top" class="columna_contenido fila_contenido columna_contenido_der">
													<table border="0" width="96%" align="center" cellspacing="0" cellpadding="0">
														<tr class="tituloTablasResponsableEleboracionCanastos" height="15px">
															<td class="columna_contenido" align="center">
																
																<span title="Canastos especiales">Especiales</span>
															</td>
														</tr>													
														<tr>
															<td class="textoNegro10">
																<html:radio property="opElaCanEsp" value="BOD" onclick="habilitarOpcionesEntregasRes(cotizarRecotizarReservarForm.opElaCanEsp,cotizarRecotizarReservarForm.opLugarEntrega, cotizarRecotizarReservarForm.opStock,${bloqueoEntregaDomicilio},${bloqueoSICMER})">
																	<span title="Canastos especiales">Elabora los canastos especiales la bodega de canastos. <b>(Cantidad m&iacute;nima para entregas a domicilio desde el CD: ${cantidadCanasto})</b></span>
																</html:radio>
															</td>
														</tr>	
														<tr>
															<td class="textoNegro10">
																<html:radio property="opElaCanEsp" value="LOC" onclick="habilitarOpcionesEntregasRes(cotizarRecotizarReservarForm.opElaCanEsp,cotizarRecotizarReservarForm.opLugarEntrega, cotizarRecotizarReservarForm.opStock,${bloqueoEntregaDomicilio},${bloqueoSICMER})">
																	<span title="Canastos especiales">Elabora los canastos especiales en su local.</span>
																</html:radio>
															</td>
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
		</logic:notEmpty>
		<tr>
			<td width="6px" class="textoRojo12" style="font-size: 16px;"><b>&nbsp;&nbsp;${indiceLetra})</b></td>
			<td align="left" valign="bottom">&nbsp;&nbsp;<b>¿La forma de entrega ser&aacute;?</b></td>
			
			<c:if test="${indiceLetra == 'B'}">
				<c:set var="indiceLetra" value="C"/>
			</c:if>	
			<c:if test="${indiceLetra == 'A'}">
				<c:set var="indiceLetra" value="B"/>
			</c:if>
		</tr>
		<tr>
			<td height="29px" colspan="2">
				<div id="tipoEntrega">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
						<tr>
							<td>
								<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
									<tr><td height="6px"></td></tr>
									<tr>
										<logic:notEmpty name="configTipoEntrega">
											<td valign="top" class="columna_contenido fila_contenido columna_contenido_der">
												<table border="0" width="96%" align="center" cellspacing="0" cellpadding="0">
													<tr class="tituloTablasEntregaCliente" height="15px">
														<td class="columna_contenido" align="center">
															<bean:define id="descripcionConfiguracion" name="configLugarEntrega" property="descripcionConfiguracion"/>
															<span title="${descripcionConfiguracion}"><bean:write name="configTipoEntrega" property="nombreConfiguracion"/></span>
														</td>
													</tr>
													<logic:iterate name="configTipoEntrega" id="configuracionDTO" property="configuraciones">
														<bean:define id="descripcionConfiguracion" name="configuracionDTO" property="descripcionConfiguracion"/>
														<bean:define id="valor" name="configuracionDTO" property="id.codigoConfiguracion"/>
														<tr>
															<td class="textoNegro10">
																<html:radio property="opTipoEntrega" value="${valor}">
																	<span title="${descripcionConfiguracion}"><bean:write name="configuracionDTO" property="nombreConfiguracion"/></span>
																</html:radio>
															</td>
														</tr>
													</logic:iterate>
												</table>
											</td>
										</logic:notEmpty>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		
		<tr>
			<td width="6px"  class="textoRojo12" style="font-size: 16px;"><b>&nbsp;&nbsp;${indiceLetra})</b></td>
			<td align="left" valign="bottom">&nbsp;&nbsp;<b>¿De d&oacute;nde se tomar&aacute; la mercader&iacute;a?.</b></td>
			<c:if test="${indiceLetra == 'C'}">
				<c:set var="indiceLetra" value="D"/>
			</c:if>	
			<c:if test="${indiceLetra == 'B'}">
				<c:set var="indiceLetra" value="C"/>
			</c:if>	
		</tr>
		<tr>
			<td height="29px" colspan="2">
				<div id="pedirCD">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
						<tr>
							<td>
								<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
									<tr><td height="6px"></td></tr>
									<tr>
										<logic:notEmpty name="configStock">
											<tr>
												<td colspan="3">
													<table border="0" width="96%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
														<tr class="tituloTablasPedidoCD" height="15px">
															<td class="columna_contenido" align="center">
																<bean:define id="descripcionConfiguracion" name="configLugarEntrega" property="descripcionConfiguracion"/>
																<span title="${descripcionConfiguracion}"><bean:write name="configStock" property="nombreConfiguracion"/></span>
															</td>
														</tr>
														<logic:iterate name="configStock" id="configuracionDTO" property="configuraciones">
															<bean:define id="descripcionConfiguracion" name="configuracionDTO" property="descripcionConfiguracion"/>
															<bean:define id="valor" name="configuracionDTO" property="id.codigoConfiguracion"/>
															<tr>
																<td class="textoNegro10">
																	<html:radio property="opStock" value="${valor}" onclick="habilitarOpcionesEntregas(cotizarRecotizarReservarForm.opElaCanEsp,cotizarRecotizarReservarForm.opStock,cotizarRecotizarReservarForm.opLugarEntrega,${bloqueoSICMER})">
																		<span title="${descripcionConfiguracion}"><bean:write name="configuracionDTO" property="nombreConfiguracion"/></span>
																	</html:radio>
																</td>
															</tr>
														</logic:iterate>
													</table>
												</td>
											</tr>
										</logic:notEmpty>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		
		<tr>		
			<td width="6px" class="textoRojo12" style="	font-size: 16px;"><b>&nbsp;&nbsp;${indiceLetra})</b></td>
			<td align="left" valign="bottom">&nbsp;&nbsp;<b>¿En d&oacute;nde  retirar&aacute; la mercader&iacute;a el cliente?</b></td>	
		</tr>
		<tr>
			<td height="29px" colspan="2">
				<div id="lugarEntrega">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
						<tr>
							<td>
								<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
									<tr><td height="6px"></td></tr>
									<tr>
										<logic:notEmpty name="configLugarEntrega">
											<td valign="top" width="30%" class="columna_contenido fila_contenido">
												<table border="0" width="96%" align="center" cellspacing="0" cellpadding="0">
													<tr class="tituloTablas" height="15px">
														<td class="columna_contenido" align="center">
															<bean:define id="descripcionConfiguracion" name="configLugarEntrega" property="descripcionConfiguracion"/>
															<span title="${descripcionConfiguracion}"><bean:write name="configLugarEntrega" property="nombreConfiguracion"/></span>
														</td>
													</tr>
													<logic:iterate name="configLugarEntrega" id="configuracionDTO" property="configuraciones">
														<bean:define id="descripcionConfiguracion" name="configLugarEntrega" property="descripcionConfiguracion"/>
														<bean:define id="valor" name="configuracionDTO" property="id.codigoConfiguracion"/>
														<tr>
															<td class="textoNegro10">
																<logic:notEmpty name="ec.com.smx.sic.sispe.mostraropcioncanastosespeciales">
																	<html:radio property="opLugarEntrega" value="${valor}" >
																		<span title="${descripcionConfiguracion}"><bean:write name="configuracionDTO" property="nombreConfiguracion"/></span>
																	</html:radio>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sispe.mostraropcioncanastosespeciales">
																	<html:radio property="opLugarEntrega" value="${valor}" >
																		<span title="${descripcionConfiguracion}"><bean:write name="configuracionDTO" property="nombreConfiguracion"/></span>
																	</html:radio>

																</logic:empty>
															</td>
														</tr>
													</logic:iterate>
												</table>
											</td>
										</logic:notEmpty>
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
<%-- Mantiene la configuracion seleccionada en las entregas sirve para que no se me activen nuevamente los radios --%>
<script language="JavaScript" type="text/javascript">	
	mantenerOpcionesEntregas(cotizarRecotizarReservarForm.opStock,cotizarRecotizarReservarForm.opLugarEntrega,${bloqueoSICMER});	
</script>
<logic:notEmpty name="ec.com.smx.sic.sispe.mostraropcioncanastosespeciales">
	<script language="JavaScript" type="text/javascript">	
		mantenerOpcionesEntregasRes(cotizarRecotizarReservarForm.opElaCanEsp,cotizarRecotizarReservarForm.opStock,cotizarRecotizarReservarForm.opLugarEntrega,${bloqueoEntregaDomicilio},${bloqueoSICMER});
	</script>
	
</logic:notEmpty>
<script languaje="JavaScript" type="text/javascript">

	function habilitarOpcEntregas(opLugarEntrega,radiosStock, bandera){			
						
		var pos9;
		var pos11;
		var otroLocal = false;
		//se verifica si esta selecion en lugar de entrega "otro local"
		alert('AQUI - habilitarOpcEntregas');
		if(opLugarEntrega != null){
			for(k=0;k<opLugarEntrega.length;k++){
				if(opLugarEntrega[k].value == '5'){
					otroLocal = true;
					break;
				}
			}
		}
		
		for(k=0;k<radiosStock.length;k++){
			if(radiosStock[k].value == '9'){
				pos9 = k;
				radiosStock[k].disabled=false;
				radiosStock[k].checked=false;
			}
			else if(radiosStock[k].value == '11' ){
				pos11 = k;
				if(otroLocal == false){
					radiosStock[k].disabled=false;
					radiosStock[k].checked=false;
				}
			}
		}
		
		if(bandera == 1){
			radiosStock[pos9].disabled=true;
			radiosStock[pos11].checked=true;
		}				
	}
	
</script>