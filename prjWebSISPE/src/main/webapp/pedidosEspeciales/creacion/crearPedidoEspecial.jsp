<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- Pantalla principal para el registro del pedido especial -->
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<bean:define id="opPersonal"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.personal"/></bean:define>
<bean:define id="opEmpresarial"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.empresarial"/></bean:define>
<bean:define id="opConfirmar"><bean:message key="ec.com.smx.sic.sispe.opConfirmar.pedido"/></bean:define>
<bean:define id="opCodigoClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroClasificacion"/></bean:define>
<bean:define id="opNombreClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreClasificacion"/></bean:define>
<bean:define id="opNombreArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreArticulo"/></bean:define>
<bean:define id="tipoDocumentoCedula"><bean:message key="ec.com.smx.sic.sispe.documento.cedula"/></bean:define>
<bean:define id="tipoDocumentoPasaporte"><bean:message key="ec.com.smx.sic.sispe.documento.pasaporte"/></bean:define>
<bean:define id="token"><bean:message key="ec.com.smx.sic.sispe.caracterToken"/></bean:define>

<%------- se verifica si la entidad responsable es una bodega ---------%>
<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.entidadBodega">
	<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.primeraVez.bodega">
		<c:set var="visibilidadContenido" value="hidden"/>
	</logic:notEmpty>
</logic:notEmpty>

<div id="div_pagina">
	<table border="0" cellspacing="0" cellpadding="0" align="center" width="99%">
		<tr>
			<td colspan="2">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					<tr>
						<td align="center">
							<div id="div_confirmarPedido">
								<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
									<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
										<tiles:put name="vtformName" beanName="vformName"/>
										<tiles:put name="vtformAction" beanName="vtformAction"/>
									</tiles:insert>
									<script language="javascript">mostrarModal();</script>
								</logic:notEmpty>
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
					<tr>
						<td align="center">
							<div id="div_tipoPedido">
								<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
									<tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
									<script language="javascript">mostrarModal();</script>
								</logic:notEmpty>
							</div>
						</td>
					</tr>

				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<!-- Sección de inserción de ventana emergente de búsqueda de personas, en donde se envía el nombre de la clase action asociada -->
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="/confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							  <tiles:put name="vtformAction" value="crearPedidoEspecial"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.accion.buscar.articulos">
						<tiles:insert page="/servicioCliente/estructuraComercial/listaArticulos.jsp">		
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<jsp:include page="/confirmacion/confirmacionCerrarFormulario.jsp"/>
			</td>
		</tr>
		
		<tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.entidadBodega">
				<td width="50%">
					<table border="0" cellpadding="0" cellspacing="0" >
						<tr>
							<td  align="left" class="textoAzul11" id="local" colspan="3">&nbsp;&nbsp;<b>Local:</b>&nbsp;
								<logic:empty name="ec.com.smx.sic.sispe.pedido.incioEntidadBodega">										
									<smx:select property="indiceLocalResponsable" styleClass="comboObligatorio" styleError="campoError" onchange="requestAjax('crearPedidoEspecial.do',['mensajes','div_pagina','localDespacho'],{parameters: 'cambiarLocal=ok', evalScripts: true});">
										<html:option value="">Seleccione</html:option>
										<logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
											<logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
												<html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
												<logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
													<logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
														<html:option value="${indiceCiudad}${token}${indiceLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
													</logic:iterate>
												</logic:notEmpty>
											</logic:iterate>
										</logic:notEmpty>
									</smx:select>
								</logic:empty>
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.incioEntidadBodega">
									<label class="textoNegro11"><b><html:hidden name="${vformName}" property="localResponsable" write="true"/></b></label>
								</logic:notEmpty>
							</td>
						</tr>
					</table>
				</td>
			</logic:notEmpty>
			<td align="left">
				<div style="visibility:${visibilidadContenido}">
					<table width="100%">
						<tr>
							<td align="left" class="textoAzul11" id="localDespacho">&nbsp;&nbsp;Local de despacho:&nbsp;
								<smx:select property="localDespacho" styleClass="comboObligatorio" styleError="campoError">
									<html:option value="">Seleccione</html:option>
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
							<logic:notEmpty name="ec.com.smx.sic.sispe.info.cerrarDia">
								<td align="right"   >
									<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
										<tr>
											<td width="3%" align="center"><img src="images/advertencia_16.gif"></td>
											<td align="left" class="textoRojo10">Ya se realizó el cierre del día, se asignará la siguiente fecha de despacho disponible.</td>
										</tr>
									</table>
								</td>
							</logic:notEmpty>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr><td height="2px"/></tr>
		<tr>
			<td colspan="2">
				<div style="visibility:${visibilidadContenido}">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td valign="top">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
												<tr>
													<td  class="fila_titulo" height="25px" colspan="3">
														<table cellpadding="0" cellspacing="0" border="0" width="100%">
															<tr>
																<td width="30px" align="right"><img src="./images/especiales.gif" border="0"></td>
																<td width="100%" class="textoNegro11">&nbsp;Tipo de pedido</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr><td height="5px"></td></tr>
												<logic:notEmpty name="ec.com.smx.sic.sispe.pedidos.especiales">
													<tr>
														<td>
															<table cellspacing="0" cellpadding="0" align="left" width="100%">
																<logic:iterate name="ec.com.smx.sic.sispe.pedidos.especiales" id="especialDTO" indexId="indicePedido">
																	<tr>
																		<td align="left" class="textoAzul11">
																			<logic:empty name="ec.com.smx.sic.sispe.ingresoDesdeBusqueda">
																				<html:radio property="opTipoPedido" value="${especialDTO.tipoPedidoDTO.id.codigoTipoPedido}" onclick="requestAjax('crearPedidoEspecial.do',['resultadosBusqueda','mensajes','div_tipoPedido','divTabs','div_pagina'],{parameters:'indicePedido=${indicePedido}',evalScripts: true});"><b><bean:write name="especialDTO" property="descripcionEspecial"/></b></html:radio>
																			</logic:empty>
																			<logic:notEmpty name="ec.com.smx.sic.sispe.ingresoDesdeBusqueda">
																				<li><b><bean:write name="especialDTO" property="descripcionEspecial"/></b></li>
																			</logic:notEmpty>
																		</td>
																	</tr>
																</logic:iterate>
															</table>
														</td>
													</tr>
												</logic:notEmpty>
												<tr><td height="5px"></td></tr>
											</table>
										</td>
									</tr>
									<tr><td height="10px"></td></tr>
									<tr>
										<td>
											<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
												<tr>
													<td class="fila_titulo" height="25px" colspan="2">
														<table cellpadding="0" cellspacing="0" border="0" width="100%">
															<tr>
																<td width="30px" align="right"><img src="./images/buscar24.gif" border="0"></td>
																<td width="166" class="textoNegro11">&nbsp;B&uacute;squeda</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr><td height="6px"></td></tr>
												<tr>
													<td>
														<table cellspacing="0" cellpadding="0" align="left" width="100%">
															<tr>
																<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[0]);${vformName}.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opCodigoClasificacion}" onclick="${vformName}.textoBusqueda.focus();">Cod. clasificaci&oacute;n</html:radio></td>
															</tr>
															<tr>
																<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[1]);${vformName}.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opNombreClasificacion}" onclick="document.forms[0].textoBusqueda.focus()">Desc. clasificaci&oacute;n</html:radio></td>
															</tr>
															<tr>
																<td align="left" class="textoAzul9" onclick="chequear(${vformName}.opTipoBusqueda[2]);${vformName}.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opNombreArticulo}" onclick="document.forms[0].textoBusqueda.focus()">Desc. art&iacute;culo</html:radio></td>
															</tr>
															<tr>
																<td align="left"><html:text property="textoBusqueda" size="24" styleClass="textNormal" onkeypress="requestAjaxEnter('crearPedidoEspecial.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});"/></td>
																				 
															</tr>
														</table>
													</td>
													<td width="4px">&nbsp;</td>
												</tr>
												<tr><td height="10px" colspan="2"></td></tr>
												<tr>
													<td align="right"><div id="botonD"><html:link href="#" styleClass="buscarD" onclick="requestAjax('crearPedidoEspecial.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});">Buscar</html:link></div></td>
													<td width="2px"></td>
												</tr>
												<tr><td height="5px" colspan="2"></td></tr>
											</table>
										</td>
									</tr>
									<tr><td height="10px"></td></tr>
									<tr>
										<td>
											<table width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion">
												<tr class="fila_titulo" height="25px">
													<td>
														<table cellpadding="0" cellspacing="0" border="0" width="100%">
															<tr>
																<td width="20%"><img src="./images/datos_informacion24.gif" border="0"></td>
																<td align="left" class="textoNegro11">Informaci&oacute;n</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr><td height="6px"></td></tr>
												<tr>
													<td>
														<table cellspacing="0" cellpadding="1" align="left" width="100%">
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">&nbsp;Sin alcance</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoObsuro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">&nbsp;Error de datos</td>
															</tr>
															<tr></tr>
															
															 <logic:notEmpty name="sispe.pedido.pavos">
																<tr>
																	<td class="textoAzul11" colspan="2" width="100%">
																		<table cellspacing="0" cellpadding="1" align="left" width="100%">
																			<tr>
																				<td align="left" class="textoNegro10" width="50%" valign="top">
																					<b>Stock D. L.:</b>																					
																				</td>
																				<td align="left" class="textoAzul10">Stock disponible locales</td>
																			</tr>
																		</table>	
																	</td>
																</tr>
															 </logic:notEmpty>
														</table>
													</td>
												</tr>
												<tr><td height="5px" colspan="2"></td></tr>
											</table>
										</td>
									</tr>
								</table>
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.autorizacion.existe">
									<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
										<tr>
											<td class="fila_titulo" height="25px" colspan="2">
												<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
													<tr>
														<td width="30px" align="right"><img src="./images/autorizacion.gif" border="0"></td>
														<td width="166" class="textoNegro12">&nbsp;Autorizaci&oacute;n</td>
														<td width="5"></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td height="6px"></td></tr>
										<tr>
											<td>
												<tiles:insert page="/servicioCliente/autorizacion/ingresoAutorizacion.jsp"/>
											</td>
										</tr>
									</table>
								</logic:notEmpty>
								<table>
									<tr><td height="4px"/></tr>
								</table>
								<%--
								<table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
									<tr>
										<td class="fila_titulo" height="25px" colspan="3">
											<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
												<tr>
													<td width="9%" align="right"><img src="./images/datos_informacion24.gif" border="0"></td>
													<td width="91%">&nbsp;Informaci&oacute;n</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="6px" colspan="3"></td></tr>
									<tr>
										<td width="2px"></td>
										<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
										<td align="left" class="textoAzul10">Sin Alcance</td>
									</tr>
								</table>
								--%>
							</td>
							<td width="4px"></td>
							<td valign="top" width="84%" id="derecha">
								<div style="border-bottom:1px solid #cccccc">
									<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
										<tr>
											<td>
												<table border="0" width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
													<tr>
														<td class="fila_titulo" id="contextoPedido">
															<table width="100%" cellpadding="0" cellspacing="0" border="0">
																<tr class="fila_titulo">
																	<!--
																		Se omite esta parte
																		@author Wladimir López
																		 
																	<td class="textoAzul11" width="5%" align="left">&nbsp;Tipo:</td>
																	<td align="left" width="10%">
																		<html:radio property="opTipoDocumento" value="${opPersonal}" onclick="hide(['datosEmpresa']);">Personal</html:radio>
																	</td>
																	<td align="left" width="5%">
																		<html:radio property="opTipoDocumento" value="${opEmpresarial}" onclick="show(['datosEmpresa']);">Empresarial</html:radio>
																	</td>
																	-->
																	<td class="textoAzul11" width="17%" align="right">&nbsp;Confirmar:</td>
																	<td align="left" width="4%">
																		<logic:empty name="ec.com.smx.sic.sispe.pedido.sinConfirmacion">
																			<html:checkbox property="opConfirmarPedido" title="active esta opci&oacute;n para confirmar el pedido" onclick="requestAjax('crearPedidoEspecial.do',['mensajes','botones','seccionConfirmar'],{parameters: 'confirmaPedido=ok'});" value="ok"/><label class="textoNegro10"></label>
																		</logic:empty>
																	</td>
																	<td width="55%">
																		<div id="seccionConfirmar">
																			<c:set var="desplegarF" value="none"/>
																			<logic:equal name="${vformName}" property="opConfirmarPedido" value="ok">
																				<c:set var="desplegarF" value="block"/>
																			</logic:equal>
																			<div id="fechas" style="display:${desplegarF}">
																				<table border="0" width="100%" cellpadding="0" cellspacing="0">
																					<tr>
																						<td width="25%" align="right" class="textoNegro10" >Fecha despacho:*&nbsp;&nbsp;</td>
																						<td width="14%" align="left" class="textoAzul10">
																							<%--<smx:text name="${vformName}" property="fechaDespacho" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="13"/>--%>
																							<html:hidden name="${vformName}" property="fechaDespacho" write="true"/>
																						</td>
																						<td align="left" width="6%">
																							&nbsp;
																							<%--<smx:calendario property="fechaDespacho" key="formatos.fecha"/>--%>
																						</td>
																						<td width="30%" align="right" class="textoNegro10">Fecha entrega cliente:*&nbsp;&nbsp;</td>
																						<td width="14%" align="left">
																							<smx:text name="${vformName}" property="fechaEntrega" styleClass="textObligatorio" onkeypress="return validarInputFecha(event);" styleError="campoError" size="12" maxlength="13"/>
																						</td>
																						<td align="left" width="6%">
																							<smx:calendario property="fechaEntrega" key="formatos.fecha"/>
																						</td>
																					</tr>
																				</table>
																			</div>
																		</div>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													
														<td>
															<!-- 	
																@author Wladimir López
																Todo el DIV de datosCliente, fué trasladado a 
																/pedidosEspeciales/contacto/datosPersona.jsp
												 			-->
															<tiles:insert page="/servicioCliente/contacto/datosPersona.jsp">
																<tiles:put name="vtformName" value="crearPedidoEspecialForm"/>
															</tiles:insert>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										
										<tr>
											<td valign="top">
												<div id="tabPedidoEspecial">
													<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
												</div>
											</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>
</div>
