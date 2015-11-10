<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>

<bean:define id="fechaDespacho"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaDespacho"/></bean:define>
<bean:define id="fechaEntrega"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEntrega"/></bean:define>
<bean:define id="fechaEstado"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEstadoPedido"/></bean:define>
<bean:define id="opNumPedido"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroPedido"/></bean:define>
<bean:define id="opNumReserva"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroReserva"/></bean:define>
<bean:define id="opClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroClasificacion"/></bean:define>
<bean:define id="opCodArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.codigoArticulo"/></bean:define>
<bean:define id="opArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreArticulo"/></bean:define>
<bean:define id="opNumCedula"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroCedula"/></bean:define>
<bean:define id="opNombreContacto"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreContacto"/></bean:define>
<bean:define id="opNombreEmpresa"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreEmpresa"/></bean:define>
<bean:define id="opRucEmpresa"><bean:message key="ec.com.smx.sic.sispe.opcion.rucEmpresa"/></bean:define>
<bean:define id="opRangoFechas"><bean:message key="ec.com.smx.sic.sispe.opcion.fechas"/></bean:define>
<bean:define id="opHoy"><bean:message key="ec.com.smx.sic.sispe.opcion.hoy"/></bean:define>
<bean:define id="opTodaTemporada"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define id="entidadResponsableLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>

<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:define id="tipoEntidadResponsable"><bean:write name="sispe.vistaEntidadResponsableDTO" property="tipoEntidadResponsable"/></bean:define>
</logic:notEmpty>

<bean:define id="tipoAccion" name="ec.com.smx.sic.sispe.accion"/>
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
					<td>
						<div style="width:100%;height:${altoDivLocCal};overflow-y:auto;overflow-x:hidden;">
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr><td colspan="2" height="3px"></td></tr>
								
								<logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
									<logic:empty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
										<tr><td class="textoAzul11" width="15%" colspan="2">&nbsp;Local responsable:</td></tr>
										<tr>
											<td colspan="2">
												<table width="100%" align="center" border="0">
													<tr>
														<td align="left">
															<smx:select property="codigoLocal" styleClass="comboFijo220px" styleError="campoError">
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
										<tr><td colspan="2" height="2px"></td></tr>
									</logic:empty>
								</logic:notEqual>
								
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${accionEstadoPedido}">
									<tr>
										<td colspan="2" class="fila_contenido fila_contenido_sup">
											<table cellpadding="1" cellspacing="0">
												<tr><td colspan="2"></td></tr>
												<tr>
													<td class="textoAzul11" width="15%" colspan="2">
														&nbsp;Envio CD:&nbsp;<html:select property="comboEnviadoCD" styleClass="combos" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});">
															<html:option value="">Todos</html:option>
															<html:option value="${estadoActivo}">SI</html:option>
															<html:option value="${estadoInactivo}">NO</html:option>
														</html:select>
													</td>
												</tr>
												<tr>
													<td class="textoAzul11" width="15%" colspan="2">
														&nbsp;Tipo pedido:&nbsp;<html:select property="comboTipoPedido" styleClass="combos" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});">
															<html:option value="">Todos</html:option>
															<html:options collection="ec.com.smx.sic.sispe.tiposPedido" labelProperty="descripcionTipoPedido" property="id.codigoTipoPedido"/>
														</html:select>
													</td>
												</tr>
												<tr height="4px"></tr>
												<logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
													<tr>
														<td class="textoAzul11" width="15%" colspan="2">
															&nbsp;Responsabilidad:&nbsp;<html:select property="opEntidadResponsable" styleClass="combos">
																<html:option value="">Todos</html:option>
																<html:option value="BOD">BODEGA</html:option>
																<html:option value="LOC">LOCAL</html:option>
															</html:select>
														</td>
													</tr>
													<tr>
														<td class="textoAzul11" width="15%" colspan="2">
															&nbsp;Ordenes de compra:&nbsp;<html:select property="opPedidoOrdenCompra" styleClass="combos">
																<html:option value="">Todos</html:option>
																<html:option value="${estadoActivo}">Con</html:option>
																<html:option value="${estadoInactivo}">Sin</html:option>
															</html:select>
														</td>
													</tr>
												</logic:notEqual>
											</table>
										</td>
									</tr>
								</logic:equal>
											
								<%-- Se valida que la entidad responsable sea != local y la accion == reporteEntregas para presenta checkbox --%>
								<smx:equal name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.reporteEntregas">
									<tr>
										<td colspan="2" class="fila_contenido fila_contenido_sup">
											<table cellpadding="1" cellspacing="0">
												<logic:notEqual name="tipoEntidadResponsable" value="${entidadResponsableLocal}">
													<tr>
														<td class="textoAzul11" width="15%" colspan="2">
															<html:checkbox property="opEntidadResponsable" value="${estadoActivo}" onclick="if(this.checked==true) document.forms[0].codigoLocal.disabled=true; else document.forms[0].codigoLocal.disabled=false">Responsabilidad bodega</html:checkbox>
														</td>
													</tr>
												</logic:notEqual>
											</table>
										</td>
									</tr>
								</smx:equal>
								<tr><td height="5px"></td></tr>
								<!-- Sección de búsqueda por radio buttons desactivada -->
								<!-- Pedidos -->
								<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${accionEnvioOrdenCompraPerecibles}">
									<tr>
										<td colspan="2" class="textoNegro11"><b>&nbsp;Pedido:</b></td>
									</tr>
									<script language="JavaScript">
										function ocultarSeccionFechas(){
											if(document.getElementById("numPed").value!=""||
											document.getElementById("numRes").value!=""){
												plegar("Busqueda");
											}else{
												desplegar("Busqueda");	
											}
												
										}
									</script>
									<tr>
										<td class="textoAzul11" width="50%">&nbsp;N&uacute;mero pedido</td>
										<td align="left" >
											<html:text property="numeroPedidoTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"
											onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
										</td>
									</tr>
									<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
										<logic:notEqual name="${mostrarReserva}" value="mostrarReserva">
											<tr>
												<td class="textoAzul11" width="50%">&nbsp;N&uacute;mero reserva</td>
												<td align="left" >
													<html:text property="numeroReservaTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"
													onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
												</td>
											</tr>
											<!-- tr>
												<td class="textoAzul11" width="50%">&nbsp;N&uacute;mero Consolidado</td>
												<td align="left" >
													<html:text property="numeroConsolidadoTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
												</td>
											</tr-->
										</logic:notEqual>
									</logic:empty>
								</logic:notEqual>
								<tr><td height="5px"></td></tr>
								<%-- Articulos --%>
								<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${accionConsolidarPedido}">
									<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${accionEntrega}">
										<tr>
											<td colspan="2" class="textoNegro11"><b>&nbsp;Art&iacute;culo:</b></td>
										</tr>
										<tr>
											<td class="textoAzul11" width="50%">&nbsp;C&oacute;digo clasificaci&oacute;n</td>
											<td align="left" >
												<html:text property="codigoClasificacionTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"
												onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
											</td>
										</tr>
										<tr>
											<td class="textoAzul11" width="50%">&nbsp;C&oacute;digo barras</td>
											<td align="left" >
												<html:text property="codigoArticuloTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"
												onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
											</td>
										</tr>
										<tr>
											<td class="textoAzul11" width="50%">&nbsp;Descripci&oacute;n art&iacute;culo</td>
											<td align="left" >
												<html:text property="descripcionArticuloTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
											</td>
										</tr>
										<tr><td height="5px"></td></tr>
									</logic:notEqual>
								</logic:notEqual>
								<%-- Clientes --%>
								<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${accionEnvioOrdenCompraPerecibles}">
								<tr>
									<td colspan="2" class="textoNegro11"><b>&nbsp;Cliente:</b></td>
								</tr>
								<tr>
									<td class="textoAzul11">&nbsp;CI/Pas/RUC Nat.</td>
									<td align="left" >
										<html:text property="documentoPersonalTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
									</td>
								</tr>
								<%--Nombre del contacto--%>
								<tr>
									<td class="textoAzul11">&nbsp;Nombre contacto</td>
									<td align="left" >
										<html:text property="nombreContactoTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
									</td>
								</tr>
								<tr><td height="5px"></td></tr>
								<%-- Empresa--%>
								<tr>
									<td colspan="2" class="textoNegro11"><b>&nbsp;Empresa:</b></td>
								</tr>
								<tr>
									<td class="textoAzul11">&nbsp;Ruc empresa</td>
									<td align="left" >
										<html:text property="rucEmpresaTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
									</td>
								</tr>
								<%--Nombre del contacto--%>
								<tr>
									<td class="textoAzul11">&nbsp;Nombre empresa</td>
									<td align="left" >
										<html:text property="nombreEmpresaTxt" styleClass="textNormal" size="18" maxlength="100" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'})"/>
									</td>
								</tr>
								</logic:notEqual>
								<%--Estado del pedido--%>
								<tr><td colspan="2" class="fila_contenido">&nbsp;</td></tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.estados">
									<tr>										
									   <td id="seccionCombo" class="textoAzul11" colspan="2">
									     <table border="0" align="left">
										    <%-- <logic:equal name="ec.com.smx.sic.sispe.accion" value="${accionConsolidarPedido}">
												    <tr>
													   <td>
													     <html:checkbox property="opBuscarConsolidados" value="${estadoActivo}">Consolidados</html:checkbox>
													   </td>
													</tr>
											</logic:equal>--%>
											<logic:empty name="ec.com.smx.sic.sispe.estado.actual">
											    <tr>
												   <td>
												     <html:checkbox property="opEstadoActivo" value="${estadoActivo}">En estado actual</html:checkbox>
												   </td>
												</tr>
											</logic:empty>
											<logic:empty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
												<tr>
												   <td align="left" class="textoAzul11">
													 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estado normal
												   </td>
												</tr>											
												<tr>
												   <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													 <html:select property="estadoPedido" styleClass="combos">
														<html:option value="">Todos</html:option>
														<html:options collection="ec.com.smx.sic.sispe.estados" labelProperty="descripcionEstado" property="id.codigoEstado"/>
													 </html:select>
												   </td>
												</tr>
											</logic:empty>			
										 </table>									     
									   </td>
									</tr>									
								</logic:notEmpty>
								<logic:notEmpty name="ec.com.smx.sic.sispe.estadosPago">
									<tr><td colspan="2" class="fila_contenido">&nbsp;</td></tr>
									<tr><td align="left" height="15px" class="textoAzul11" colspan="2">&nbsp;Estado pago</td></tr>
									<tr>
										<td colspan="2">
											&nbsp;<html:select property="estadoPagoPedido" styleClass="combos" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});">
												<html:option value="">Todos</html:option>
												<html:options collection="ec.com.smx.sic.sispe.estadosPago" labelProperty="descripcionEstado" property="id.codigoEstado"/>
											</html:select>
										</td>
									</tr>
								</logic:notEmpty>
								<!--Fecha de entrega-->
								<smx:equal name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.enviarDespacho">
									<tr>
										<td class="textoAzul11" width="15%" colspan="2">
											<html:checkbox property="opDespachoPendiente" value="${estadoActivo}"><b>&nbsp;Despacho pendiente</b></html:checkbox>
										</td>
									</tr>
								</smx:equal>
								<smx:equal name="ec.com.smx.sic.sispe.accion" value="${accionArticulosReservados}">
									<tr>
									   <td class="textoAzul11" colspan="2">
									     <table border="0" align="left">
										    <tr>	
												<td class="fila_contenido" width="15%" colspan="2">
													<html:checkbox property="opStockArticuloReservado" value="${estadoActivo}"><b>&nbsp;Sin suficiente stock</b></html:checkbox>
												</td>
											</tr>
										</table>
									  </td>
									</tr>
								</smx:equal>
								<tr><td height="7px"></td></tr>
								<tr>
									<td colspan="2" class="textoNegro11">
										<logic:notEmpty name="ec.com.smx.sic.sispe.tipoFechaBusqueda">
											<smx:notEqual name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.reporteEntregas">
												<logic:empty name="ec.com.smx.sic.sispe.accion.seccion.busqueda.reportes">
													&nbsp;<b><bean:write name="ec.com.smx.sic.sispe.tipoFechaBusqueda"/></b>
												</logic:empty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.accion.seccion.busqueda.reportes">
				                                   	<tr><td colspan="2"></td></tr>
													<tr><td align="left" height="15px" class="textoAzul11" colspan="2">&nbsp;Tipo de fecha</td></tr>
													<tr></tr>
													<tr>
														<td colspan="2">
															&nbsp;<html:select property="comboTipoFecha" styleClass="combos" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});">
																<html:option value="${fechaEstado}">Fecha estado</html:option>
																<html:option value="${fechaDespacho}">Fecha despacho</html:option>
																<html:option value="${fechaEntrega}">Fecha entrega</html:option>
															</html:select>
															<a href="#" onclick="limpiar();">
																<img alt="Limpiar campos de pedido y habilitar sección fechas" src="./images/limpiar.gif" border="0" height="18px">
															</a>
														</td>
													</tr>
												</logic:notEmpty>
											</smx:notEqual>
											<smx:equal name="ec.com.smx.sic.sispe.accion" valueKey="ec.com.smx.sic.sispe.accion.reporteEntregas">
												&nbsp;<b>Fecha de entrega</b>
												<a href="#" onclick="limpiar();">
													<img alt="Limpiar campos de pedido y habilitar sección fechas" src="./images/limpiar.gif" border="0" height="18px">
												</a>
											</smx:equal>
										</logic:notEmpty>
										<script language="JavaScript">
											function limpiar(){
												document.getElementById("numPed").value="";
												document.getElementById("numRes").value="";
												desplegar("Busqueda");	
											}
										</script>
									</td>
								</tr>
								<logic:equal name="ec.com.smx.sic.sispe.accion" value="${accionEnvioOrdenCompraPerecibles}">
									<tr>
										<td class="textoAzul11" width="15%" colspan="2">
											<logic:empty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
												<b>&nbsp;Fecha despacho</b>
											</logic:empty>
										</td>
									</tr>
								</logic:equal>
								<tr><td height="5px"></td></tr>
								<tr>
									<td colspan="2">
										<logic:empty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
											<table border="0" width="100%" cellspacing="0" cellpadding="0">
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
																				<html:text property="fechaInicial" styleClass="textNormal" size="12" maxlength="10" onfocus="document.forms[0].opcionFecha[0].checked=true;" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});return validarInputFecha(event);"/>
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
																				<html:text property="fechaFinal" styleClass="textNormal" size="12" maxlength="10" onfocus="document.forms[0].opcionFecha[0].checked=true;" onkeypress="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});return validarInputFecha(event);"/>
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
														Desde&nbsp;<html:text property="numeroMeses" styleClass="textNormal" size="3" maxlength="3" onfocus="document.forms[0].opcionFecha[2].checked=true;" onkeypress="return validarInputNumeric(event);" onkeyup="requestAjaxEnter('crearCotizacion.do',['mensajesBusquedaPopup','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});"
														onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>&nbsp;meses atr&aacute;s
													</td>
													

												</tr>
											</table>
										</logic:empty>
									</td>
									
									
								</tr>
							
								<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
									<tr>
										<td colspan="2" align="left">
											<table width="260px" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
												<tr>
													<td class="fila_titulo" height="29px">
														<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
															<tr>
																<td width="11%" align="right"><img src="./images/datos_informacion24.gif" border="0"></td>
																<td width="89%">&nbsp;Informaci&oacute;n</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr><td height="6px"></td></tr>
												<tr>
													<td>
														<table border="0" class="textos" width="98%" align="center" cellspacing="0" cellpadding="0">
														
														  
														   	<tr>
																<td class="textoAzul11" width="80px">
																	&nbsp;Stock N:
																</td>
																<td class="textoNegro11">
																	Stock negociado
																</td>
															</tr>
															<tr>
																<td class="textoAzul11" width="80px">
																	&nbsp;Stock V:
																</td>
																<td class="textoNegro11">
																	Stock virtual
																</td>
															</tr>
															<tr>
																<td class="textoAzul11" width="80px">
																	&nbsp;Stock D. L:
																</td>
																<td class="textoNegro11">
																	Stock disponible locales
																</td>
															</tr>
															<tr>
																<td class="textoAzul11" width="80px">
																	&nbsp;Stock V. R:
																</td>
																<td class="textoNegro11">
																	Stock virtual reservado
																</td>
															</tr>
															
															<tr>
																<td class="textoAzul11" width="80px">
																	&nbsp;Stock L. R:
																</td>
																<td class="textoNegro11">
																	Stock locales reservado
																</td>
															</tr>
														
														</table>
													</td>
												</tr>
												<tr><td height="6px"></td></tr>
											</table>
											<!--
											<div style="padding-left:10px;">
											<table width="96%" align="left" cellpadding="0" style="border-width:1px;border-style:solid;border-color:#cccccc;">
												<tr>
													<td width="31%" class="textoNegro10"><b>&nbsp;Stock N: </b></td>
													<td align="left" class="textoAzul10">Stock negociado</td>
												</tr>
												<tr>
													<td width="31%" class="textoNegro10"><b>&nbsp;Stock D. R: </b></td>
													<td align="left" class="textoAzul10">Stock disponible reservas</td>
												</tr>
												<tr>
													<td width="31%" class="textoNegro10"><b>&nbsp;Stock D. L:</b></td>
													<td align="left" class="textoAzul10">Stock disponible locales</td>
												</tr>
												<tr>
													<td width="31%" class="textoNegro10" ><b>&nbsp;Stock R. R: </b></td>
													<td align="left" class="textoAzul10">Stock reservado reservas</td>
												</tr>
												<tr>
													<td width="31%" class="textoNegro10"><b>&nbsp;Stock R. L:</b></td>
													<td align="left" class="textoAzul10">Stock reservas locas</td>
												</tr>
										
											</table>
										    </div>-->
										</td>
									</tr>
								</logic:notEmpty>
								
								<!--Fin B&uacute;squeda-->
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	
	<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${accionPedidoEspecial}">
	 <tr><td colspan="2" height="5px"></td></tr>
	</logic:notEqual>
	<!--Bot&oacute;n Buscar-->
	<tr>
		<td colspan="2">
			<table cellpadding="1" cellspacing="0" width="100%">
				<tr>
					<td align="center">
						<div id="botonD">
							<html:link styleClass="buscarD" href="#" onclick="requestAjax('crearCotizacion.do',['pregunta','seccion_autorizacion','resultadosBusqueda'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=buscarPedAnt'});">Buscar</html:link>
						</div>
					</td>
					<td width="3px"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr><td height="4" colspan="2"></td></tr>
</table>