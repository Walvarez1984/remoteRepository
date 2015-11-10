<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>
<bean:define id="opHoy"><bean:message key="ec.com.smx.sic.sispe.opcion.hoy"/></bean:define>
<bean:define id="opTodaTemporada"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define id="opRangoFechas"><bean:message key="ec.com.smx.sic.sispe.opcion.fechas"/></bean:define>
<bean:define id="opcion1"><bean:message key="ec.com.smx.sic.sispe.tipoAgrupacionOpcion1"/></bean:define>
<bean:define id="opcion2"><bean:message key="ec.com.smx.sic.sispe.tipoAgrupacionOpcion2"/></bean:define>

<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
	<tr>
		<td>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="white">
				<tr>
					<td class="fila_titulo" colspan="2">
						<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
							<tr>
								<td width="15%"><img src="./images/buscar24.gif" border="0"/></td>
								<td width="85%">B&uacute;squeda </td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>   
					<td colspan="2" class="textoNegro11"><b>&nbsp;Art&iacute;culo:</b></td>
				</tr>
				<tr>
					<td class="textoAzul11" width="50%">&nbsp;C&oacute;digo barras</td>
					<td align="left" >
						<html:text property="codigoArticuloTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'})"
						onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
					</td>
				</tr>
				<tr>
					<td class="textoAzul11" width="50%">&nbsp;C&oacute;digo clasificaci&oacute;n</td>
					<td align="left" >
						<html:text property="codigoClasificacionTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'})"
						onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
					</td>
				</tr>
				<tr>
					<td class="textoAzul11" width="50%">&nbsp;Descripci&oacute;n art&iacute;culo</td>
					<td align="left" >
						<html:text property="descripcionArticuloTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'})"/>
					</td>
				</tr>
				<tr><td height="5px"></td></tr>
				<%-- 
				<logic:notEmpty name="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol">
				 <tr>
					<td colspan="2" class="textoNegro11" align="left"><b>Tipo/Mov.</b>
				   	 <smx:select property="tipoMovCombo" styleClass="comboFijo220px">
				      <html:option value="S">Todos</html:option>
				      <html:options collection="ec.com.smx.calendarizacion.calendarioMotivoMovimientoDTOCol" labelProperty="descripcionMotivoMovimiento" property="id.codigoMotivoMovimiento"/>
				   	 </smx:select>   
		      		</td>
		      	 </tr>
				</logic:notEmpty>
				<tr><td height="5px"></td></tr>--%>
				<tr>
					<td class="textoNegro11" height="17" colspan="2"><b>Tipos de búsqueda</b></td>
				</tr>
				<tr>
					<td align="left" colspan="2">
						<html:select property="cmbOpcionAgrupacion" styleClass="combos">
							<html:option value="${opcion2}">fecha - artículo</html:option>
							<html:option value="${opcion1}">artículo - fecha</html:option>
						</html:select>
					</td>
				</tr>
				<tr><td height="5px"></td></tr>
				<tr>
					<td colspan="2">
						<table border="0" width="100%" cellspacing="0" cellpadding="0">
							<tr>   
								<td colspan="2" class="textoNegro11">&nbsp;<b>Fecha:</b></td>
							</tr>
							<tr>
								<td width="2%"><html:radio property="opcionFecha" value="${opRangoFechas}"/></td>
								<td align="left" width="15%" class="textoAzul11" onclick="chequear(document.forms[0].opcionFecha[0]);document.forms[0].fechaInicial.focus();">Rango&nbsp;(aaaa-mm-dd):</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td>
									<table border="0" width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td class="textoNegro11" align="left" width="10%">Inicial:</td>
											<td align="left">
												<table border="0" cellspacing="0">
													<tr>
														<td class="textoAzul11">
															<html:text property="fechaInicial" styleClass="textNormal" size="12" maxlength="10" onfocus="document.forms[0].opcionFecha[0].checked=true;" onkeypress="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'});return validarInputFecha(event);"/>
														</td>
														<td onclick="document.forms[0].opcionFecha[0].checked=true;">
															<smx:calendario property="fechaInicial" key="formatos.fecha"/>
														</td>
														<td width="2px"></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="textoNegro11" align="left">Final:&nbsp;</td>
											<td>
												<table border="0" cellspacing="0">
													<tr>
														<td class="textoAzul11">
															<html:text property="fechaFinal" styleClass="textNormal" size="12" maxlength="10" onfocus="document.forms[0].opcionFecha[0].checked=true;" onkeypress="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'});return validarInputFecha(event);"/>
														</td>
														<td onclick="document.forms[0].opcionFecha[0].checked=true;">
															<smx:calendario property="fechaFinal" key="formatos.fecha"/>
														</td>
														<td width="2px"></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><html:radio property="opcionFecha" value="${opHoy}"/></td>
								<td align="left" class="textoAzul11" onclick="chequear(document.forms[0].opcionFecha[1]);">Hoy</td>
							</tr>
							<tr>
								<td><html:radio property="opcionFecha" value="${opTodaTemporada}"/></td>
								<td align="left" class="textoAzul11" onclick="chequear(document.forms[0].opcionFecha[2]);">
									Desde&nbsp;<html:text property="numeroMeses" styleClass="textNormal" size="3" maxlength="3" onfocus="document.forms[0].opcionFecha[2].checked=true;" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('lineaProduccion.do', ['mensajes','seccion_autorizacion','resultadosBusqueda'], {parameters: 'buscar=ok'});"
									onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>&nbsp;meses atr&aacute;s
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="15px" colspan="2"></td></tr>
	<!--Bot&oacute;n Buscar-->
	<tr>
		<td colspan="2">
			<table cellpadding="1" cellspacing="0" width="100%">
				<tr>
					<td align="center">
						<div id="botonD">
							<html:link styleClass="buscarD" href="#" onclick="requestAjax('lineaProduccion.do',['mensajes','resultadosBusqueda','div_pagina','pregunta'],{parameters: 'buscar=ok',evalScripts: true});">Buscar</html:link>
						</div>
					</td>
					<td width="3px"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="78px" colspan="2"></td></tr>
	<tr>
	 <td>
	  <div style="height:26px"><table width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion"><tr class="fila_titulo"><td width="20%"><img src="./images/datos_informacion24.gif" border="0"></td><td align="left">Informaci&oacute;n</td></tr></table></div>
	  <div style="height:80px;overflow:auto;border-width:0px;border-style:solid;border-color:#cccccc">
		<table align="left" cellpadding="0">
			<tr>
				<td width="20%" valign="top"><table width="100%"><tr><td width="20px" valign="top"><img src="./images/despensa_llena.gif" border="0"></td></tr></table></td>
				<td align="left" class="textoNaranja10">Despensa</td>
			</tr>
			<tr>
				<td width="20%" valign="top"><table width="100%"><tr><td width="20px" valign="top"><img src="./images/canasto_lleno.gif" border="0"></td></tr></table></td>
				<td align="left" class="textoNegro10">Canasta</td>
			</tr>
			<tr>
				<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojo10" width="100%" height="12px"></td></tr></table></td>
				<td align="left" class="textoRojoNuevo">Saldo negativo</td>
			</tr>
			<tr>
				<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="azul10" width="100%" height="12px"></td></tr></table></td>
				<td align="left" class="textoAzul10">Saldo positivo</td>
			</tr>
			<tr>
				<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="negro10" width="100%" height="12px"></td></tr></table></td>
				<td align="left" class="textoNegro10">Saldo completo</td>
			</tr>
		</table>
	  </div>
	 </td>
	</tr>
</table>