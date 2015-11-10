<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="opNumPedido"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroPedido"/></bean:define>
<bean:define id="opNumReserva"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroReserva"/></bean:define>
<bean:define id="opNumCedula"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroCedula"/></bean:define>
<bean:define id="opNombreContacto"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreContacto"/></bean:define>
<bean:define id="opRucEmpresa"><bean:message key="ec.com.smx.sic.sispe.opcion.rucEmpresa"/></bean:define>
<bean:define id="opNombreEmpresa"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreEmpresa"/></bean:define>
<bean:define id="opCodigoArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.codigoArticulo"/></bean:define>
<bean:define id="opRangoFechas"><bean:message key="ec.com.smx.sic.sispe.opcion.fechas"/></bean:define>
<bean:define id="opTodaTemporada"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define id="opFechaHoy"><bean:message key="ec.com.smx.sic.sispe.opcion.hoy"/></bean:define>
<bean:define id="entidadResponsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
<bean:define id="fechaDespacho"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaDespacho"/></bean:define>
<bean:define id="fechaEntrega"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEntrega"/></bean:define>
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:define id="tipoEntidadResponsable"><bean:write name="sispe.vistaEntidadResponsableDTO" property="tipoEntidadResponsable"/></bean:define>
</logic:notEmpty>

<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas">
    <bean:define id="tituloReporte" value="Reporte de ventas"/>
    <bean:define id="imagenReporte" value="images/reporteVentas48.gif"/>
    <bean:define id="accionReporte" value="reporteVentas.do"/>
    <bean:define id="paginaReporte" value="/reportes/ventas/reporteVentas.jsp"/>
</smx:equal>

<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.despachos">
    <bean:define id="tituloReporte" value="Reporte de despachos"/>
    <bean:define id="imagenReporte" value="images/reporteDespachosReservacion.gif"/>
    <bean:define id="accionReporte" value="reporteDespachos.do"/>
    <bean:define id="paginaReporte" value="/reportes/produccionPedidos/reporteDespachoReservacion.jsp"/>
</smx:equal>

<tiles:insert page="/include/top.jsp"/>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<html:form action="reporteGeneral" method="post" focus="txtValorBusqueda">
		<input type="hidden" name="ayuda" value="">
		<!-- Sección de pop-up de agrupaciójn de reportes de ventas --> 
		<tr>
	        <td>
	            <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
	                <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
	                <script language="javascript">mostrarModal();</script>	                
	            </logic:notEmpty>	            
	        </td>
    	</tr>
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="${imagenReporte}" border="0"></img></td>
						<td height="35" valign="middle"><bean:write name="tituloReporte"/></td>
						<td align="right" valign="top">
							<table border="0">
								<tr>
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
										</div>
									</td>
									<!--
									<td>
										<div id="botonA">
											<html:link href="#" styleClass="pdfA" onclick="enviarFormulario('pdf', 0, false);">Crear PDF</html:link>
										</div>
									</td>
									-->
									<td>
										<bean:define id="exit" value="exit"/>
										<div id="botonA">
											<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="6" colspan="3"></td></tr>
		<%--Cuerpo--%>
		<tr>
			<td align="center" valign="top">
				<table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos" width="19%">
							<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
								<%-- B&uacute;squeda--%>
								<tr>
									<td class="fila_titulo" colspan="3">
										<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
											<tr>
												<td width="15%"><img src="./images/buscar24.gif" border="0"/></td>
												<td width="85%">B&uacute;squeda</td>
											</tr>
										</table>
									</td>
								</tr>
								<%-- Locales --%>
								<logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
									<tr>
										<td colspan="2">
											<table width="100%" align="center" border="0">
												<tr>
													<td class="textoAzul11">Local:</td>
												</tr>
												<tr>
													<td align="left" id="localResponsable">
														<smx:select property="cmbCodigoLocal" styleClass="comboFijo220px" styleError="campoError">
															<html:option value="">Todos</html:option>
															<logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
																<logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
																	<html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
																	<logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
																		<logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
																			<html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
																		</logic:iterate>
																	</logic:notEmpty>
																</logic:iterate>
															</logic:notEmpty>
														</smx:select>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td colspan="3" height="1px" class="fila_contenido">&nbsp;</td></tr>
								</logic:notEqual>
								<%--Fin locales--%>
								<%--N&uacute;mero de pedido--%>
								<tr>
									<td width="2%">
										<html:radio property="opcionValorBusqueda" value="${opNumPedido}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
									</td>
									<td class="textoAzul11" width="18%" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[0]);reporteGeneralForm.txtValorBusqueda.focus();">N&uacute;mero pedido</td>
								</tr>
								<tr>
									<td width="2%">
										<html:radio property="opcionValorBusqueda" value="${opNumReserva}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
									</td>
									<td class="textoAzul11" width="18%" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[1]);reporteGeneralForm.txtValorBusqueda.focus();">N&uacute;mero reserva</td>
								</tr>
								<%--N&uacute;mero de c&eacute;dula del cliente--%>
								<tr>
									<td>
										<html:radio property="opcionValorBusqueda" value="${opNumCedula}" onclick="reporteGeneralForm.txtValorBusqueda.focus();"/>
									</td>
									<td class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[2]);reporteGeneralForm.txtValorBusqueda.focus();">CI/Pas/RUC Nat.</td>
								</tr>
								<%--Nombre del contacto--%>
								<tr>
									<td>
										<html:radio property="opcionValorBusqueda" value="${opNombreContacto}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
									</td>
									<td class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[3]);reporteGeneralForm.txtValorBusqueda.focus();">Nombre cliente</td>
								</tr>
								<%--Rucempresa--%>
								<tr>
									<td>
										<html:radio property="opcionValorBusqueda" value="${opRucEmpresa}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
									</td>
									<td class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[4]);reporteGeneralForm.txtValorBusqueda.focus();">Ruc empresa</td>
								</tr>
								
								<%--Nombre empresa--%>
								<tr>
									<td>
										<html:radio property="opcionValorBusqueda" value="${opNombreEmpresa}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
									</td>
									<td class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[5]);reporteGeneralForm.txtValorBusqueda.focus();">Nombre empresa</td>
								</tr>
								<%--Codigo del articulo--%>
								<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.despachos">
									<tr>
										<td>
											<html:radio property="opcionValorBusqueda" value="${opCodigoArticulo}" onclick="reporteGeneralForm.txtValorBusqueda.focus()"/>
										</td>
										<td class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionValorBusqueda[6]);reporteGeneralForm.txtValorBusqueda.focus();">C&oacute;digo barras</td>
									</tr>
								</smx:equal>
								<tr>
									<td>&nbsp;</td>
									<td>
										<html:text property="txtValorBusqueda" styleClass="textNormal" size="27" maxlength="100" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'})"/>
									</td>
								</tr>
								<tr><td colspan="3" height="1px" class="fila_contenido">&nbsp;</td></tr>
								<%--Estado del pago--%>
								<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas">
									<tr>
										<td class="textoAzul11" height="17" colspan="2">&nbsp;Estado del pago:</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<html:select property="cmbCodigoEstadoPagado" styleClass="combos" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});">
												<html:option value="">Todos</html:option>
												<logic:notEmpty name="ec.com.smx.sic.estadosPago">
													<html:options collection="ec.com.smx.sic.estadosPago" labelProperty="descripcionEstado" property="id.codigoEstado"/>
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr><td colspan="3" height="1px" class="fila_contenido">&nbsp;</td></tr>
								</smx:equal>
								<%--Opciones Pedido(cotizados-reservados, pedidos cotizados sin reservar)--%>
								<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas">
									<tr>
										<td class="textoAzul11" height="17" colspan="2">&nbsp;Estado del pedido:</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<html:select property="cmbOpcionEstadoPedido" styleClass="combos" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});">
												<html:option value="3">Todos</html:option>
												<html:option value="1">cotizados - reservados</html:option>
												<html:option value="2">cotizados - sin reservar</html:option>
											</html:select>
										</td>
									</tr>
									<tr><td colspan="3" height="1px" class="fila_contenido">&nbsp;</td></tr>
								</smx:equal>
								<%--Fecha en Ventas--%>
								<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.ventas">
									<tr>
										<td colspan="2" class="textoNegro11">&nbsp;Fecha del pedido:</td>
										<html:hidden property="cmbTipoFecha" write="false" value="ok"/>
									</tr>
									<tr><td colspan="3" height="5px"></td></tr>
									<tr>
										<td width="2%"><html:radio property="opcionFechaBusqueda" value="${opRangoFechas}" onclick="reporteGeneralForm.txtFechaInicial.focus()"/></td>
										<td align="left" width="15%" class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionFechaBusqueda[0]);reporteGeneralForm.txtFechaInicial.focus()">Entre (aaaa-mm-dd):</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td>
											<table border="0" width="100%" cellspacing="0" cellpadding="0">
												<tr>
													<td class="textoNegro11" align="right" width="10%">Inicial:</td>
													<td align="left">
														<table border="0" cellspacing="0">
															<tr>
																<td class="textoAzul11">
																	<smx:text property="txtFechaInicial" styleClass="textNormal" size="12" maxlength="10" onfocus="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});return validarInputFecha(event);" styleError="campoError"/>
																</td>
																<td onclick="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;">
																	<smx:calendario property="txtFechaInicial" key="formatos.fecha"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td class="textoNegro11" align="right">Final:&nbsp;</td>
													<td>
														<table border="0" cellspacing="0">
															<tr>
																<td class="textoAzul11">
																	<smx:text property="txtFechaFinal" styleClass="textNormal" size="12" maxlength="10" onfocus="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});return validarInputFecha(event);" styleError="campoError"/>
																</td>
																<td onclick="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;">
																	<smx:calendario property="txtFechaFinal" key="formatos.fecha"/>
																</td>
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="5px"></td></tr>
									<tr>
										<td><html:radio property="opcionFechaBusqueda" value="${opFechaHoy}"/></td>
										<td align="left" class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionFechaBusqueda[1])">Hoy</td>
									</tr>
									<tr><td height="5px"></td></tr>
									<tr>
										<td><html:radio property="opcionFechaBusqueda" value="${opTodaTemporada}"/></td>
										<td align="left" class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionFechaBusqueda[2])">Desde&nbsp;<html:text property="numeroMeses" styleClass="textNormal" size="3" maxlength="3" onfocus="chequear(reporteGeneralForm.opcionFechaBusqueda[2]);" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'})"/>&nbsp;meses atr&aacute;s</td>
									</tr>
									<tr><td height="5px"></td></tr>
								</smx:equal>
								<%--Fecha en Despachos--%>
								<smx:equal name="ec.com.smx.sic.sispe.reporteGeneral.tipo" valueKey="ec.com.smx.sic.sispe.reporteGeneral.tipo.despachos">
									<tr>
										<td colspan="2" class="textoAzul11">
											&nbsp;Fecha:
											<div id="campoOculto" style="display:none">
												<input type="text"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td class="combos">
											<%--html:select property="cmbTipoFecha" styleClass="combos" onchange="requestAjax('${accionReporte}', ['filtrosFechas'], {popWait:false});"--%>
											<html:select property="cmbTipoFecha" styleClass="combos" onchange="if (this.value == ''){hide(['filtrosFechas']);} else {show(['filtrosFechas']); if (document.forms('reporteGeneralForm').opcionFechaBusqueda[1].checked == false)chequear(document.forms('reporteGeneralForm').opcionFechaBusqueda[0]); else document.forms('reporteGeneralForm').txtFechaInicial.focus();}">
												<html:option value="">Seleccione</html:option>
												<html:option value="${fechaDespacho}">Despacho</html:option>
												<html:option value="${fechaEntrega}">Entrega</html:option>
											</html:select>
										</td>
									</tr>
									<tr>
										<td colspan="2" id="filtrosFechas" style="display:none;">
											<%--logic:present name="ec.com.smx.sic.sispe.verFiltrosFechas"--%>
											<table border="0">
												<tr><td height="5px"></td></tr>
												<tr>
													<td width="2%"><html:radio property="opcionFechaBusqueda" value="${opRangoFechas}" onclick="document.forms('reporteGeneralForm').txtFechaInicial.focus()"/></td>
													<td align="left" width="15%" class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionFechaBusqueda[0]);document.forms('reporteGeneralForm').txtFechaInicial.focus()">Entre (aaaa-mm-dd):</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
													<td>
														<table border="0" width="100%" cellspacing="0" cellpadding="0">
															<tr>
																<td class="textoNegro11" align="right" width="10%">Inicial:</td>
																<td align="left">
																	<table border="0" cellspacing="0">
																		<tr>
																			<td class="textoAzul11">
																				<smx:text property="txtFechaInicial" styleClass="textNormal" size="12" maxlength="10" onfocus="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});" styleError="campoError"/>
																			</td>
																			<td onclick="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;">
																				<smx:calendario property="txtFechaInicial" key="formatos.fecha"/>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td class="textoNegro11" align="right">Final:&nbsp;</td>
																<td>
																	<table border="0" cellspacing="0">
																		<tr>
																			<td class="textoAzul11">
																				<smx:text property="txtFechaFinal" styleClass="textNormal" size="12" maxlength="10" onfocus="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'});" styleError="campoError"/>
																			</td>
																			<td onclick="reporteGeneralForm.opcionFechaBusqueda[0].checked=true;">
																				<smx:calendario property="txtFechaFinal" key="formatos.fecha"/>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td><html:radio property="opcionFechaBusqueda" value="${opTodaTemporada}"/></td>
													<td align="left" class="textoAzul11" onclick="chequear(reporteGeneralForm.opcionFechaBusqueda[1])">Desde&nbsp;<html:text property="numeroMeses" styleClass="textNormal" size="3" maxlength="3" onfocus="chequear(reporteGeneralForm.opcionFechaBusqueda[1]);" onkeypress="requestAjaxEnter('${accionReporte}', ['mensajes','resultadosBusqueda'], {parameters: 'botonBuscar=ok'})"/>&nbsp;meses atr&aacute;s</td>
												</tr>
											</table>
											<%--/logic:present--%>
										</td>
									</tr>
									<tr><td colspan="3" height="1px" class="fila_contenido">&nbsp;</td></tr>
									<td>
										<html:checkbox property="filtrarPorUsuario"/>
									</td>
									<td class="textoAzul11">Filtrar por usuario</td>
									<tr><td colspan="3">&nbsp;</td></tr>
								</smx:equal>
								<%--Bot&oacute;n Buscar--%>
								<tr>
									<td>&nbsp;</td>
									<td align="right">
										<div id="botonD">
											<html:link styleClass="buscarD" href="#" onclick="requestAjax('${accionReporte}',['mensajes','resultadosBusqueda'],{parameters: 'botonBuscar=ok'});">Buscar</html:link>
										</div>
									</td>
									<td></td>
								</tr>
								<tr><td height="4" colspan="2"></td></tr>
								<%--Fin B&uacute;squeda--%>
							</table>
						</td>
						<%--Fin Barra Izquierda--%>

						<%-- Separador --%>
						<TD class="datos" width="1%">&nbsp;</TD>

						<%-- Datos --%>
						<TD class="datos" width="80%">
							<div id="resultadosBusqueda">
								<tiles:insert page="${paginaReporte}"/>
							</div>
						</TD>
					</tr>
					<%--Fin P&aacute;gina--%>
				</table>
			</td>
		</tr>
	</html:form>
</TABLE>
<tiles:insert page="/include/bottom.jsp"/>