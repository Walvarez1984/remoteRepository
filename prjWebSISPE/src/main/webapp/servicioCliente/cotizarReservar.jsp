<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>
<%-- lista de definiciones para las acciones --%>
<bean:define id="cotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.cotizacion"/></bean:define>
<bean:define id="recotizacion"><bean:message key="ec.com.smx.sic.sispe.accion.recotizacion"/></bean:define>
<bean:define id="reservacion"><bean:message key="ec.com.smx.sic.sispe.accion.reservacion"/></bean:define>
<bean:define id="modificarReservacion"><bean:message key="ec.com.smx.sic.sispe.accion.modificarReservacion"/></bean:define>
<bean:define id="opPersonal"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.personal"/></bean:define>
<bean:define id="opEmpresarial"><bean:message key="ec.com.smx.sic.sispe.opTipoDocumento.empresarial"/></bean:define>
<bean:define id="opCodigoClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroClasificacion"/></bean:define>
<bean:define id="opNombreClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreClasificacion"/></bean:define>
<bean:define id="opNombreArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreArticulo"/></bean:define>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<bean:define id="token"><bean:message key="ec.com.smx.sic.sispe.caracterToken"/></bean:define>
<bean:define id="tipoDocumentoCedula"><bean:message key="ec.com.smx.sic.sispe.documento.cedula"/></bean:define>
<bean:define id="tipoDocumentoPasaporte"><bean:message key="ec.com.smx.sic.sispe.documento.pasaporte"/></bean:define>

<c:set var="imagen" value="cotizacion.gif"/>
<c:set var="estilo_btnCotRes" value="reservacionA"/>
<c:set var="texto_btnCotRes" value="Reservar"/>
<c:set var="title_btnCotRes" value="Pasar a la reservaci&oacute;n"/>
<c:set var="title_btnCotAnt" value="Cotizar pedidos anteriores"/>
<c:set var="estilo_btnCotAnt" value="pedAntA"/>
<c:set var="texto_btnCotAnt" value="Copiar cot"/>
<c:set var="mostrarComboLocales" value=""/>
<c:set var="visibilidadContenido" value="visible"/>
<c:set var="alto_divs" value="500px"/>
<c:set var="mostrarInicio" value=""/>
<c:set var="onClicCancelar" value="show(['popupCerrarForm']);mostrarModal();"/>
<c:set var="tituloCancelar" value="ir al men&uacute; principal"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.recotizacion">
	<c:set var="mostrarInicio" value="${estadoActivo}"/>
	<c:set var="onClicCancelar" value="requestAjax('crearCotizacion.do',['pregunta'],{parameters: 'volverBuscar=ok'});mostrarModal();"/>
	<c:set var="tituloCancelar" value="volver a la pantalla de b&uacute;squeda"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.reservacion">
	<c:set var="mostrarInicio" value="${estadoActivo}"/>
	<c:set var="onClicCancelar" value="requestAjax('crearCotizacion.do',['pregunta'],{parameters: 'volverBuscar=ok'});mostrarModal();"/>
	<c:set var="tituloCancelar" value="volver a la pantalla de b&uacute;squeda"/>
</logic:notEmpty>
<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
	<c:set var="imagen" value="reservacion.gif"/>
	<c:set var="estilo_btnCotRes" value="cotizacionA"/>
	<c:set var="texto_btnCotRes" value="Cotizar"/>
	<c:set var="title_btnCotRes" value="Pasar a la cotizaci&oacute;n"/>
</logic:equal>

<c:set var="modificarDetallePedido" value="${estadoActivo}" scope="session"/>
<%-- Se verifica si la reservación a modificar está abonada totalmente --%>
<logic:notEmpty name="ec.com.smx.sic.sispe.modificarReserva.pagadoTotalmente">
	<%-- esta condición sirve para controlar que cuando el pedido se haya pagado totalmente no se puedan modificar cantidades o artículos del detalle --%>
	<c:set var="modificarDetallePedido" value="0" scope="session"/>
</logic:notEmpty>
<%-- se verifica si la entidad responsable es una bodega --%>
<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
	<c:set var="alto_divs" value="480px"/>
	<c:set var="mostrarComboLocales" value="${estadoActivo}"/>
	<logic:notEmpty name="ec.com.smx.sic.sispe.recotizacion">
		<c:set var="mostrarComboLocales" value=""/>
	</logic:notEmpty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.reservacion">
		<c:set var="mostrarComboLocales" value=""/>
	</logic:notEmpty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.cotizar.primeraVez.bodega">
		<c:set var="visibilidadContenido" value="hidden"/>
	</logic:notEmpty>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.popup.despacho">
	<c:set var="mostrarDespacho" value="1"/>
</logic:notEmpty>
<html:form action="crearCotizacion" method="post">
	<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
			<html:hidden property="ayuda" value=""/>
			<tr>
				<td>
					<div id="pregunta">
						<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
							<jsp:include page="../confirmacion/confirmacion.jsp"/>
							<script language="javascript">mostrarModal();</script>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
							<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
								<tiles:put name="vtformAction" value="crearCotizacion"/>
							</tiles:insert>
							<script language="javascript">mostrarModal();</script>
						</logic:notEmpty>
	                    <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupDescuento">
							<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/popUpDescuento.jsp">
								<tiles:put name="vtformAction" value="crearCotizacion"/>
	                            <tiles:put name="estadoActivo" value="${estadoActivo}"/>
	                            <tiles:put name="token" value="${token}"/>
							</tiles:insert>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupDescuentoVariable">
							<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/popUpAutDescVar.jsp">
								<tiles:put name="vtformAction" value="crearCotizacion"/>                            
							</tiles:insert>
						</logic:notEmpty>
						<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.popupAutorizaciones">
							<tiles:insert page="/servicioCliente/autorizacion/popUpColaAutorizaciones.jsp">
								<tiles:put name="vtformAction" value="crearCotizacion"/>                            
							</tiles:insert>
						</logic:notEmpty>
						
						<logic:notEmpty name="ec.com.smx.sic.sispe.accion.pedidos.anteriores">
							<tiles:insert page="/servicioCliente/cotizarRecotizarReservar/listaPedidosAnteriores.jsp">		
							</tiles:insert>
							<script language="javascript">mostrarModal();</script>
						</logic:notEmpty>
						
						<logic:notEmpty name="ec.com.smx.sic.sispe.accion.buscar.articulos">
							<tiles:insert page="/servicioCliente/estructuraComercial/listaArticulos.jsp">		
							</tiles:insert>
							<script language="javascript">mostrarModal();</script>
						</logic:notEmpty>
						
						<logic:notEmpty name="ec.com.smx.sic.sispe.accion.buscar.articulos.estructura.comercial">
							<tiles:insert page="/servicioCliente/estructuraComercial/catalogoArticulos.jsp">		
							</tiles:insert>
							<script language="javascript">mostrarModal();</script>
						</logic:notEmpty>
						
						<logic:empty name="ec.com.smx.sic.sispe.accion.buscar.articulos">
							<logic:empty name="ec.com.smx.sic.sispe.accion.buscar.articulos.estructura.comercial">
								<logic:notEmpty name="ec.com.smx.sic.sispe.catalogoArticulos.articulos">
									<tiles:insert page="/servicioCliente/estructuraComercial/listaArticulosPedAnt.jsp">	
									</tiles:insert>
									<script language="javascript">mostrarModalZ('frameModal2',119);</script>
								</logic:notEmpty>
							</logic:empty>	
						</logic:empty>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<jsp:include page="../confirmacion/confirmacionCerrarFormulario.jsp"/>
				</td>
			</tr>
			<tr>
				<td class="titulosAccion" height="35px">
					<table border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td width="3%" align="center"><img src="./images/${imagen}" border="0"></img></td>
							<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
									<td align="left">
										&nbsp;&nbsp;<bean:write name="WebSISPE.tituloVentana"/>&nbsp;
										<logic:equal name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
											(No pedido:&nbsp;<label class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/></label>,
											 No reserva:&nbsp;<label class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="llaveContratoPOS"/></label>)
										</logic:equal>
										<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
											<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
												(No:&nbsp;<label class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.pedidoDTO" property="id.codigoPedido"/></label>)
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
												<logic:equal name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="npEsNuevaReserva" value="${true}">
													(Pedido nuevo)
												</logic:equal>
												<logic:notEqual name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="npEsNuevaReserva" value="${true}">
													(No:&nbsp;<label class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/></label>)
												</logic:notEqual>
												</logic:notEmpty>
											</logic:empty>
										</logic:notEqual>
									</td>
							</logic:empty>
							<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
								<logic:notEmpty name="ec.com.smx.sic.sispe.pedioGeneral">
									<td align="left">
											&nbsp;&nbsp;Formulario de consolidación&nbsp;
												(No:&nbsp;<label class="textoNegro11">Pedido consolidado</label>)
									</td>
								</logic:notEmpty>
								<logic:empty name="ec.com.smx.sic.sispe.pedioGeneral">
									<td align="left">
										&nbsp;&nbsp;Formulario de consolidación&nbsp;
										<%--<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">--%>
											<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
												(No:&nbsp;<label class="textoNegro11"><bean:write name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="id.codigoPedido"/></label>)
											</logic:notEmpty>
										<%--</logic:empty>--%>
									</td>
								</logic:empty>	
							</logic:notEmpty>
							<%--<logic:equal name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
								<logic:equal name="mostrarDespacho" value="1">
									<td align="left" width="15%">														
										<img src="images/despachado.png" width="35px" height="35px" border="0" title="Pedido despachado">
									</td>
								</logic:equal>
							</logic:equal>--%>
							<td align="right">
								 <logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
							 		<logic:empty name="ec.com.smx.sic.sispe.vistaPedidoDTOActual">
										<table cellspacing="0">
											<tr>
											<td>
												<div id="botonA">
													<!-- <html:link styleClass="guardarA" href="#" onclick="realizarEnvio('guardarConsolidacion');" title="Guardar consolidaci&oacute;n">Guardar</html:link> -->
													<html:link href="#" styleClass="guardarA" onclick="requestAjax('crearCotizacion.do', ['pregunta','mensajes','div_pagina'], {parameters: 'ayuda=guardarConsolidacion', evalScripts:true});ocultarModal();" title="Guardar consolidaci&oacute;n">Guardar</html:link>
												</div>
											</td>
											<td>
												<div id="botonA">
														<html:link href="#" styleClass="cancelarA" onclick="requestAjax('crearCotizacion.do', ['pregunta','mensajes'], {parameters: 'ayuda=regConsolidacion', evalScripts:true});ocultarModal();" title="${tituloCancelar}">Cancelar</html:link>
												</div>
											</td>
												<%-- <logic:equal name="mostrarInicio" value="${estadoActivo}">
													<td class="columna_contenido_der" align="center">&nbsp;</td>
													<td align="center">&nbsp;</td>
													<td>
														<div id="botonA">
															<html:link styleClass="inicioA" href="#" onclick="show(['popupCerrarForm']);mostrarModal();" title="ir al men&uacute; principal">Inicio</html:link>
														</div>
													</td>
												</logic:equal>--%>
											</tr>
										</table>
									</logic:empty>
									<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTOActual">
										<table cellspacing="0">
											<tr>
												<td>
													<div id="botonA">
														<html:link href="#" styleClass="cancelarA" onclick="${onClicCancelar}" title="${tituloCancelar}">Cancelar</html:link>
													</div>
												</td>
												<%--<logic:equal name="mostrarInicio" value="${estadoActivo}">
													<td class="columna_contenido_der" align="center">&nbsp;</td>
													<td align="center">&nbsp;</td>
													<td>
														<div id="botonA">
															<html:link styleClass="inicioA" href="#" onclick="show(['popupCerrarForm']);mostrarModal();" title="ir al men&uacute; principal">Inicio</html:link>
														</div>
													</td>
												</logic:equal>--%>
											</tr>
										</table>
									</logic:notEmpty>
								</logic:notEmpty>
								<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
									<table cellspacing="0">
												<tr>											
												<logic:equal name="ec.com.smx.sic.sispe.accion" value="${cotizacion}">
												<td>	
												
													<table cellspacing="0">	
														<tr>
															<td>
																<div id="botonA">
																	<html:link styleClass="${estilo_btnCotAnt}" title="${title_btnCotAnt}" href="#" onclick="requestAjax('crearCotizacion.do',['pregunta'],{parameters: 'pedidosAnteriores=ok&accionesPedAnt=abrir',evalScripts:true,popWait:true});">${texto_btnCotAnt}</html:link>
																</div>	
															</td>
														</tr>
													</table>
												</td>
												</logic:equal>
												<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
													<td>
														<div id="botonA">
															<html:link styleClass="${estilo_btnCotRes}" title="${title_btnCotRes}" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'cambiarContexto=ok',evalScripts:true});">${texto_btnCotRes}</html:link>
														</div>
													</td>
												</logic:notEqual>
												<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
													<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
														<td class="columna_contenido_der" align="center">&nbsp;</td>
														<td align="center">&nbsp;</td>
														<td>
															<div id="botonA">
																<html:link styleClass="guardarA"  href="#"onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina'], {parameters: 'ayuda=regCotizacion', evalScripts:true});ocultarModal();" title="Guardar cotizaci&oacute;n">Guardar</html:link>
																<%--html:link styleClass="guardarA" href="#" onclick="realizarEnvio('regCotizacion');" title="guardar cotizaci&oacute;n">Guardar</html:link--%>
															</div>
														</td>
													</logic:notEqual>
													<logic:equal name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
														<td>
															<div id="botonA">
																<html:link styleClass="confirmarGuardarA"  href="#"onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina'], {parameters: 'ayuda=regReservacion', evalScripts:true});ocultarModal();" title="Guardar reservaci&oacute;n enviando datos al POS y al centro de distribuci&oacute;n">Confirmar</html:link>
																<%--html:link styleClass="confirmarGuardarA" href="#" onclick="realizarEnvio('regReservacion');" title="guardar reservaci&oacute;n enviando datos al POS y al Centro de Distribuci&oacute;n">Confirmar</html:link--%>
															</div>
														</td>
													</logic:equal>
												</logic:notEqual>
												<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
													<td>
														<div id="botonA">
															<html:link styleClass="guardarA" href="#"onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina'], {parameters: 'ayuda=regReservaTemp', evalScripts:true});ocultarModal();" title="Guardar reservaci&oacute;n TEMPORAL sin enviar datos al POS y al centro de distribuci&oacute;n">Guardar</html:link>
															<%--html:link styleClass="guardarA" href="#" onclick="realizarEnvio('regReservaTemp');" title="guardar reservaci&oacute;n TEMPORAL sin enviar datos al POS y al Centro de Distribuci&oacute;n">Guardar</html:link--%>
														</div>
													</td>
													<td class="columna_contenido_der" align="center">&nbsp;</td>
													<td align="center">&nbsp;</td>
													<td>
														<div id="botonA">
															<%-- <html:link styleClass="confirmarGuardarA" href="#"onclick="requestAjax('crearCotizacion.do', ['pregunta','mensajes','div_pagina'], {parameters: 'ayuda=regReservacion', evalScripts:true});ocultarModal();" title="guardar reservaci&oacute;n enviando datos al POS y a Bodega">Confirmar</html:link> --%>
															<html:link styleClass="confirmarGuardarA" href="#"onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina'], {parameters: 'ayuda=regReservacion', evalScripts:true});ocultarModal();" title="Guardar reservaci&oacute;n enviando datos al POS y a bodega">Confirmar</html:link>
															<%--html:link styleClass="confirmarGuardarA" href="#" onclick="realizarEnvio('regReservacion');" title="guardar reservaci&oacute;n enviando datos al POS y a Bodega">Confirmar</html:link--%>
														</div>
													</td>
												</logic:equal>
												<td>
													<div id="botonA">
														<html:link href="#" styleClass="cancelarA" onclick="${onClicCancelar}" title="${tituloCancelar}">Cancelar</html:link>
													</div>
												</td>
												<logic:equal name="mostrarInicio" value="${estadoActivo}">
													<td class="columna_contenido_der" align="center">&nbsp;</td>
													<td align="center">&nbsp;</td>
													<td>
														<div id="botonA">
															<html:link styleClass="inicioA" href="#" onclick="show(['popupCerrarForm']);mostrarModal();" title="Ir al men&uacute; principal">Inicio</html:link>
														</div>
													</td>
												</logic:equal>
											</tr>
										</table>
								</logic:empty>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
				<tr><td height="0px"></td></tr>
				<tr>
					<td align="left" class="textoAzul11" id="local">&nbsp;&nbsp;<b>Local:</b>&nbsp;
						<logic:equal name="mostrarComboLocales" value="${estadoActivo}">
							<smx:select property="indiceLocalResponsable" styleClass="comboObligatorio" styleError="campoError" onchange="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'cambiarLocal=ok', evalScripts: true});">
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
						</logic:equal>
						<logic:notEqual name="mostrarComboLocales" value="${estadoActivo}">
							<label class="textoNegro11"><b><html:hidden name="cotizarRecotizarReservarForm" property="localResponsable" write="true"/></b></label>
						</logic:notEqual>
					</td>
				</tr>
			</logic:notEmpty>
			<tr>
				<td align="center" valign="top">
					<div style="visibility:${visibilidadContenido}">
						<table border="0" class="textoNegro11" align="left" width="100%" cellpadding="0" cellspacing="0">
							<tr><td height="4px"></td></tr>
							<tr>
								<td width="16%" valign="top" id="izquierda">
									<div style="width:100%;height:${alto_divs};overflow:auto;border-bottom:1px solid #cccccc">
										<table align="left" cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td>
													<div style="height:20px"><table width="100%" cellpadding="0" cellspacing="0" class="tabla_informacion"><tr class="fila_titulo"><td width="20%"><img src="./images/datos_informacion24.gif" border="0"></td><td align="left">Informaci&oacute;n</td></tr></table></div>
													<div style="height:60px;overflow:auto;border-width:1px;border-style:solid;border-color:#cccccc">
														<table align="left" cellpadding="0">
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="violetaClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">Implementos</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="naranjaClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">Sin stock</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">Sin alcance</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">De baja en el SIC</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="celesteClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">Inactivo en el SIC</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="amarilloClaro10" width="100%" height="12px"></td></tr></table></td>
																<td align="left" class="textoAzul10">Obsoleto en el SIC</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><img src="./images/pavo.gif" border="0"></td>
																<td align="left" class="textoAzul10">Art&iacute;culo pavo</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><img src="./images/balanza.gif" border="0"></td>
																<td align="left" class="textoAzul10">Art&iacute;culo de peso variable</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><img src="./images/canasto_lleno.gif" border="0"></td>
																<td align="left" class="textoAzul10">Art&iacute;culo receta</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><img src="./images/despensa_llena.gif" border="0"></td>
																<td align="left" class="textoAzul10">Art&iacute;culo despensa</td>
															</tr>
															<tr>
																<td width="20%" valign="top"><img src="./images/estrella.gif" border="0"></td>
																<td align="left" class="textoAzul10">Receta modificada</td>
															</tr>
														</table>
													</div>
												</td>
											</tr>
											<tr><td height="5px"></td></tr>
											<tr>
												<td>
													<div id="accordion1" style="width:100%">
															<logic:notEmpty name="cotizarRecotizarReservarForm" property="datosConsolidados">
																<div>
																		<div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><html:img src="images/consolidar.gif" border="0"/></td><td align="left">Consolidados</tr></table></div>
																		<div class="panelContent">
																			<div id="div_listado" style="width:100%;height:200px;overflow:auto">
																							<table border="0" cellspacing="0" width="98%" cellpadding="1">
																									<tr>
																										<td width="20%" class="columna_contenido fila_contenido" align="left">
																												<a href="javascript:;" class="linkAzul" title="Click aqui para ver el pedido" 
																													onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina','descuentos'],{parameters:'consolidadoGeneral=ok',evalScripts:true});popWait('div_wait');">
																													Pedido consolidado
																												</a>
																										</td>
																									</tr>
																									<tr>
																										<td colspan="10" align="center">
																												<logic:notEmpty name="cotizarRecotizarReservarForm" property="datosConsolidadosTotal">
																													<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTOActual">
																														<bean:define id="idPedidoActual" name="ec.com.smx.sic.sispe.vistaPedidoDTOActual" property="id.codigoPedido"/>
																													</logic:notEmpty>
																													<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																														<tr class="tituloTablasCeleste">
																															<td class="fila_contenido_negro columna_contenido_der_negro" align="center">&nbsp;</td>
																															<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No pedido</td>
																															<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Estado</td>
																														</tr>
																														<logic:iterate name="cotizarRecotizarReservarForm" property="datosConsolidadosTotal" id="vistaPedidoDTO2" indexId="indicePedido2">
																														<%-- control del estilo para el color de las filas --%>
																															<bean:define id="residuoR" value="${indicePedido2 % 2}"/>
																															<logic:equal name="residuoR" value="0">
																																<bean:define id="colorBack2" value="blanco10"/>
																															</logic:equal>
																															<logic:notEqual name="residuoR" value="0">
																																<bean:define id="colorBack2" value="amarilloClaro10"/>
																															</logic:notEqual>
																															<tr class="${colorBack2} textoNegro10">
																																<td class="columna_contenido fila_contenido" width="3%" align="center">
																																	<c:if test="${idPedidoActual== vistaPedidoDTO2.id.codigoPedido}">
																																  		<html:img src="images/consolidar.gif" border="0" alt="Pedido consolidado"/>
																																  	</c:if>
																																</td>
																																<td align="center" class="columna_contenido fila_contenido">
																																<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTOActual">
																																  <c:if test="${idPedidoActual!= vistaPedidoDTO2.id.codigoPedido}">
																																  	<bean:define id="soloLectura" value="SI"/>
																																  </c:if>
																																  <c:if test="${idPedidoActual== vistaPedidoDTO2.id.codigoPedido}">
																																  	<bean:define id="soloLectura" value="NO"/>
																																  </c:if>
																																</logic:notEmpty>
																																<logic:empty name="ec.com.smx.sic.sispe.vistaPedidoDTOActual">
																																	<bean:define id="soloLectura" value="SI"/>
																																</logic:empty>
																																<a href="javascript:;" class="linkAzul" title="Click aqui para ver el pedido" 
																																onclick="requestAjax('crearCotizacion.do', ['mensajes','div_pagina','seccion_detalle','descuentos'],{parameters:'indice=ok&codigoPedido=${vistaPedidoDTO2.id.codigoPedido}&codigoLocal=${vistaPedidoDTO2.id.codigoAreaTrabajo}&entidadResponsable=${vistaPedidoDTO2.entidadResponsable}&soloLectura=${soloLectura}',evalScripts:true});popWait('div_wait');">
																																<bean:write name="vistaPedidoDTO2" property="id.codigoPedido"/>
																																</a>
																																</td>
																																<td align="center" class="columna_contenido fila_contenido">&nbsp;<bean:write name="vistaPedidoDTO2" property="descripcionEstado"/></td>
																															</tr>
																														</logic:iterate>
																													</table>
																												</logic:notEmpty>
																										</td>
																									</tr>
																							</table>
																						</div>
																		</div>
																</div>
														</logic:notEmpty>
														<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">	
														<div>
															<div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><img src="./images/especiales.gif" border="0"></td><td align="left">Catálogo</tr></table></div>
															<div class="panelContent" style="width:98%">
																<logic:notEmpty name="ec.com.smx.sic.sispe.pedido.tipoEspeciales">
																	<table border="0" align="left" cellspacing="0" cellpadding="0" width="100%">
																		<tr>
																			<td>
																				<table border="0" align="left" class="textoAzul9" cellspacing="0" cellpadding="0" width="100%">
																					<bean:size name="ec.com.smx.sic.sispe.pedido.tipoEspeciales" id="tamanoEspeciales"/>
																					<logic:iterate name="ec.com.smx.sic.sispe.pedido.tipoEspeciales" id="especialDTO" indexId="indiceTipoEspecial">
																						<tr>
																							<td align="left" width="10%"><html:radio property="opTipoEspeciales" value="${indiceTipoEspecial}" onclick="showExclusivo(${indiceTipoEspecial}, 'seccionEspeciales_',${tamanoEspeciales});desactivarTodo(cotizarRecotizarReservarForm.opEspecialesSeleccionados);"/></td>
																							<td align="left"><b>${especialDTO.descripcionEspecial}</b></td>
																						</tr>
																					</logic:iterate>
																				</table>
																			</td>
																		</tr>
																		<tr><td height="3px"></td></tr>
																		<tr>
																			<td>
																				<div id="especiales">
																					<table border="0" class="textoNegro9 tabla_informacion" cellspacing="0" cellpadding="0" width="100%">
																						<c:set var="indiceGlobal" value="0"/>
																						<logic:iterate name="ec.com.smx.sic.sispe.pedido.tipoEspeciales" id="especialDTO" indexId="indiceTipoEspecial">
																							<c:set var="visibilidad" value="none"/>
																							<logic:equal name="indiceTipoEspecial" value="${cotizarRecotizarReservarForm.opTipoEspeciales}">
																								<c:set var="visibilidad" value="block"/>
																							</logic:equal>
																							<tr>
																								<td id="seccionEspeciales_${indiceTipoEspecial}" style="display:${visibilidad}">
																									<table cellpadding="0" cellspacing="0">
																										<logic:iterate name="especialDTO" property="articulos" id="articuloDTO" indexId="indiceArticulo">
																											<bean:define id="residuo" value="${indiceArticulo % 2}"/>
																											<c:set var="clase" value="blanco9"/>
																											<c:set var="colorBack" value="#ffffff"/>
																											<logic:notEqual name="residuo" value="0">
																												<c:set var="clase" value="grisClaro9"/>
																												<c:set var="colorBack" value="#EBEBEB"/>
																											</logic:notEqual>
																											<tr class="${clase}" id="filaE_${indiceGlobal}">
																												<td title="C&oacute;digo barras:&nbsp;${articuloDTO.codigoBarrasActivo.id.codigoBarras}">
																													<table cellpadding="0" cellspacing="0">
																														<tr>
																															<td align="left" valign="top" width="3%">
																																<html:multibox property="opEspecialesSeleccionados" value="${indiceTipoEspecial}-${indiceArticulo}-${indiceGlobal}"/>
																															</td>
																															<td colspan="2" align="left" onclick="chequear2(cotizarRecotizarReservarForm.opEspecialesSeleccionados, ${indiceGlobal});">
																																<b><bean:write name="articuloDTO" property="descripcionArticulo"/>,&nbsp;<bean:write name="articuloDTO" property="articuloMedidaDTO.referenciaMedida"/></b>
																															</td>
																														</tr>
																														<tr>
																															<td class="fila_contenido">&nbsp;</td>
																															<td class="fila_contenido" align="left" width="30%">
																																<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																																	<html:text property="vectorCantidadEspecial" value="1" styleClass="textNormal" size="5" maxlength="4" onkeypress="return validarInputNumeric(event);" onkeyup="activar(cotizarRecotizarReservarForm.opEspecialesSeleccionados, ${indiceGlobal});requestAjaxEnter('crearCotizacion.do',['mensajes','pregunta','especiales','seccion_detalle','divTabs','div_datosCliente'],{parameters: 'agregarArticulo=ok&articuloNuevoEspecial=ok', evalScripts: true});"
																																	onkeydown="return checkKeysCombination(event,'ctrl,86') ? setPasteTime(this, 20, true, true, '99999999999999999999', '0'):true"/>
																																</logic:equal>
																																<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																																	<html:text property="vectorCantidadEspecial" value="1" styleClass="textNormal" size="5" maxlength="4" onkeypress="return validarInputNumeric(event);"/>
																																</logic:notEqual>
																															</td>
																															<td width="60%" class="fila_contenido" onclick="chequear2(cotizarRecotizarReservarForm.opEspecialesSeleccionados, ${indiceGlobal});">&nbsp;</td>
																														</tr>
																													</table>
																												</td>
																											</tr>
																											<c:set var="indiceGlobal" value="${indiceGlobal + 1}"/>
																										</logic:iterate>
																									</table>
																								</td>
																							</tr>
																						</logic:iterate>
																					</table>
																				</div>
																			</td>
																		</tr>
																	</table>
																</logic:notEmpty>
															</div>
														</div>
														</logic:empty>
														<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
														<div>
															<div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><img src="./images/buscar24.gif" border="0"></td><td align="left">B&uacute;squeda</tr></table></div>
															<div class="panelContent">
																<table border="0" cellpadding="0" align="left">
																	<tr>
																		<td>
																			<c:set var="actualizarDetalleClick" value="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','divTabs'],{parameters: 'actualizarDetalle=ok', evalScripts: true});"/>
																			<c:set var="actualizarDetalleEnter" value="requestAjaxEnter('crearCotizacion.do',['mensajes','seccion_detalle','divTabs'],{parameters: 'actualizarDetalle=ok', evalScripts: true});"/>
																			<logic:notEqual name="modificarDetallePedido" value="${estadoActivo}">
																				<c:set var="actualizarDetalleClick" value=""/>
																				<c:set var="actualizarDetalleEnter" value=""/>
																			</logic:notEqual>
																			<table cellspacing="0" cellpadding="0" align="left" width="100%">
																				<tr>
																					<td align="left" class="textoAzul9" onclick="chequear(cotizarRecotizarReservarForm.opTipoBusqueda[0]);cotizarRecotizarReservarForm.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opCodigoClasificacion}" onclick="cotizarRecotizarReservarForm.textoBusqueda.focus();">Cod. clasificaci&oacute;n</html:radio></td>
																				</tr>
																				<tr>
																					<td align="left" class="textoAzul9" onclick="chequear(cotizarRecotizarReservarForm.opTipoBusqueda[1]);cotizarRecotizarReservarForm.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opNombreClasificacion}" onclick="document.forms[0].textoBusqueda.focus()">Desc. clasificaci&oacute;n</html:radio></td>
																				</tr>
																				<tr>
																					<td align="left" class="textoAzul9" onclick="chequear(cotizarRecotizarReservarForm.opTipoBusqueda[2]);cotizarRecotizarReservarForm.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="${opNombreArticulo}" onclick="document.forms[0].textoBusqueda.focus()">Desc. art&iacute;culo</html:radio></td>
																				</tr>
																				<tr>
																					<td align="left" class="textoAzul9" onclick="chequear(cotizarRecotizarReservarForm.opTipoBusqueda[3]);cotizarRecotizarReservarForm.textoBusqueda.focus();"><html:radio property="opTipoBusqueda" value="ma" onclick="document.forms[0].textoBusqueda.focus()">Marca</html:radio></td>
																				</tr>
																				<tr>
																					<td align="left"><html:text property="textoBusqueda" size="24" styleClass="textNormal" onkeyup="requestAjaxEnter('crearCotizacion.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});"/></td>
<%-- 																					<td align="left"><html:text property="textoBusqueda" size="24" styleClass="textNormal" onkeyup="resultadosBusquedaENTER('buscarArticulo.do','WIN_RBUS',['opTipoBusqueda','textoBusqueda'], 'buscarArtPed');${actualizarDetalleEnter}"/></td> --%>
																				</tr>
																				<tr><td height="5px"></td></tr>
																				<tr>
																					<td align="left"><html:link href="#" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'buscarPorEstructura=ok',popWait:true});">Estructura comercial</html:link>
<%-- 																				<td align="left"><html:link href="#" onclick="dialogoWeb('catalogoArticulos.do','WIN_RBUS','dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');${actualizarDetalleClick}">Estructura comercial</html:link> --%>
																				</tr>
																			</table>
																		</td>
																	</tr>
																	<tr><td height="10px"></td></tr>
																	<tr>
																	
																		<td align="left"><div id="botonD"><html:link href="#" styleClass="buscarD" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'buscarArtPed=ok&buscador=ok',popWait:true});">Buscar</html:link></div></td>
<%-- 																		<td align="left"><div id="botonD"><html:link href="#" styleClass="buscarD" onclick="resultadosBusqueda('buscarArticulo.do', 'WIN_RBUS', ['opTipoBusqueda','textoBusqueda'], 'buscarArtPed');${actualizarDetalleClick}">Buscar</html:link></div></td> --%>
																	</tr>
																</table>
															</div>
														</div>
														</logic:empty>
														<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
															<logic:equal name="ec.com.smx.sic.sispe.pedido.mostrarAutorizacion" value="${estadoActivo}">
																<div>
																	<div class="panelHeader"><table width="100%" cellpadding="0" cellspacing="0"><tr><td width="20%"><img src="./images/autorizacion.gif" border="0"></td><td align="left">Autorizaci&oacute;n</tr></table></div>
																	<div class="panelContent">
																		<tiles:insert page="/servicioCliente/autorizacion/ingresoAutorizacion.jsp"/>
																	</div>
																</div>
															</logic:equal>
														</logic:empty>
													</div>
												</td>
											</tr>
										</table>
										<script language="JavaScript" type="text/javascript">acordeon('accordion1',300);</script>
									</div>
								</td>
								<td id="divisor" height="${alto_divs}">
									<span style="display:block" id="img_ocultar">
										<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
									</span>
									<span style="display:none" id="img_mostrar">
										<a href="#"><img src="./images/spliter_der.png" border="0"></a>
									</span>
								</td>
								<td valign="top" width="84%" id="derecha">
									<div style="height:${alto_divs};border-bottom:1px solid #cccccc">
										<table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
											<tr>
												<td colspan="3">
													<table width="100%" cellspacing="0" cellpadding="0" class="tabla_informacion">
														<tr>
															<td class="fila_contenido" id="contextoPedido">
															<logic:empty name="ec.com.smx.sic.sispe.accion.consolidar">
																<table width="100%" cellpadding="0" cellspacing="0">
																	<tr class="fila_titulo">
																		
																		<!--
																			<td class="textoAzul11" width="5%" align="left">&nbsp;Tipo:</td>
																			EDITANDO RADIOS DE BUSQUEDA DE PERSONA 
																			@author Wladimir López
																		<td align="left" width="10%">
																			<html:radio property="opTipoDocumento" value="${opPersonal}" onclick="hide(['datosEmpresa']);show(['datosConsolidacion']);requestAjax('crearCotizacion.do',['mensajes','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});">Personal</html:radio>
																		</td>
																		<td align="left" width="15%">
																			<html:radio property="opTipoDocumento" value="${opEmpresarial}" onclick="show(['datosEmpresa']);hide(['datosConsolidacion']);requestAjax('crearCotizacion.do',['mensajes','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});">Empresarial</html:radio>
																		</td>
																		
																		 -->
																		<td class="textoRojo11" align="center">
																			<bean:define id="esConsolidado" value="false"/>
																			<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO" property="codigoConsolidado">
																					<bean:define id="esConsolidado" value="true"/>
																				</logic:notEmpty>
																			</logic:notEmpty>
																			<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
																					<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO" property="codigoConsolidado">
																						<bean:define id="esConsolidado" value="true"/>
																					</logic:notEmpty>
																				</logic:notEmpty>
																			</logic:empty>
																			
																			<c:set var="estadoDeshabilitado" value="true"/>
																			<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																				<c:set var="estadoDeshabilitado" value="false"/>
																			</logic:equal>
																			<logic:equal name="esConsolidado" value="true">
																				<c:set var="estadoDeshabilitado" value="true"/>
																			</logic:equal>
																			
																			<%--<logic:equal name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
																				<html:checkbox title="Si selecciona esta opci&oacute;n se crear&aacute; una entrega autom&aacute;tica con la siguiente configuraci&oacute;n: Se entregar&aacute; en: MI LOCAL, Tipo de entrega: TOTAL, La mercader&iacute;a se tomar&aacute; de:  CENTRO DE DISTRIBUCI&Oacute;N" property="checkReservarStockEntrega" value="${estadoActivo}" disabled="${estadoDeshabilitado}" onclick="requestAjax('crearCotizacion.do',['mensajes'],{parameters: 'reservarStockEntrega=ok'});">Pre Entrega autom&aacute;tica</html:checkbox>&nbsp;&nbsp;
																			</logic:equal>--%>
																			
																			<logic:empty name="ec.com.smx.sic.sispe.establecimientoHabilitadoPreciosAfiliado">
																				<html:checkbox property="checkCalculosPreciosAfiliado" value="${estadoActivo}" disabled="true" onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});">Precios afiliado</html:checkbox>&nbsp;&nbsp;
																			</logic:empty>
																			<logic:notEmpty name="ec.com.smx.sic.sispe.establecimientoHabilitadoPreciosAfiliado">
																				<input type="checkbox" disabled="disabled" checked="checked">Precios afiliado</input>&nbsp;&nbsp;
																			</logic:notEmpty>
																			
																			<html:checkbox property="checkPagoEfectivo" value="${estadoActivo}" disabled="${estadoDeshabilitado}" onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','descuentos','div_datosCliente'],{parameters: 'pagoEfectivo=ok'});">Pago efectivo</html:checkbox>
																		</td>
																		
																		<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${reservacion}">
																			<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${modificarReservacion}">
																				<html:hidden property="opValidarPedido" value="${estadoActivo}"/>
																				<%--<td class="textoRojo11"><html:checkbox property="opValidarPedido" value="${estadoActivo}">Registrar sin validar Art&iacute;culo en el SIC</html:checkbox></td>--%>
																			</logic:notEqual>
																		</logic:notEqual>
																		<html:hidden property="fechaEntrega"/>
																	</tr>
																</table>
															</logic:empty>
															<logic:notEmpty name="ec.com.smx.sic.sispe.accion.consolidar">
																<table width="100%" cellpadding="0" cellspacing="0">
																	<tr class="fila_titulo">
																		<logic:empty name="ec.com.smx.sic.sispe.pedioGeneral">
																			<td class="textoAzul11" width="60%" align="left">&nbsp;Datos del contacto</td>
																		</logic:empty>
																		<logic:notEmpty name="ec.com.smx.sic.sispe.pedioGeneral">
																			<td class="textoAzul11" width="60%" align="left">&nbsp;Listado de empresas consolidadas</td>
																		</logic:notEmpty>
																		<td class="textoAzul11" width="40%" align="left">
																			<logic:notEmpty name="ec.com.smx.sic.sispe.pedidoDTO">
																				<bean:define id="pedidoValidacion" name="ec.com.smx.sic.sispe.pedidoDTO"/>
																			</logic:notEmpty>
																			<logic:empty name="ec.com.smx.sic.sispe.pedidoDTO">
																				<logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedidoDTO">
																					<bean:define id="pedidoValidacion" name="ec.com.smx.sic.sispe.vistaPedidoDTO"/>
																				</logic:notEmpty>
																			</logic:empty>
																			<c:set var="estadoDeshabilitado" value="true"/>
																			<logic:equal name="modificarDetallePedido" value="${estadoActivo}">
																				<c:set var="estadoDeshabilitado" value="false"/>
																			</logic:equal>
																			<logic:notEmpty name="idPedidoActual">
																		  		<c:set var="estadoDeshabilitado" value="true"/>
																			</logic:notEmpty>
																			
																			<logic:empty name="ec.com.smx.sic.sispe.establecimientoHabilitadoPreciosAfiliado">
																				<html:checkbox property="checkCalculosPreciosAfiliado" value="${estadoActivo}" disabled="true" onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle'],{parameters: 'actualizarDetalle=ok'});">Precios afiliado</html:checkbox>&nbsp;&nbsp;
																			</logic:empty>
																			<logic:notEmpty name="ec.com.smx.sic.sispe.establecimientoHabilitadoPreciosAfiliado">
																				<input type="checkbox" disabled="disabled" checked="checked">Precios afiliado</input>&nbsp;&nbsp;
																			</logic:notEmpty>
																			<%--<html:checkbox property="checkPagoEfectivo" value="${estadoActivo}" disabled="true">Pago Efectivo</html:checkbox>--%>
																			<html:checkbox property="checkPagoEfectivo" value="${estadoActivo}"  disabled="${estadoDeshabilitado}"  onclick="requestAjax('crearCotizacion.do',['mensajes','seccion_detalle','descuentos','div_datosCliente'],{parameters: 'pagoEfectivo=ok'});">Pago efectivo</html:checkbox>
																		</td>
																	</tr>
																</table>
															</logic:notEmpty>
															</td>
														</tr>
														<tr>
														<logic:empty name="ec.com.smx.sic.sispe.pedioGeneral">
															<td>
																<!-- 	
																	@author Wladimir López
																	Todo el DIV de datosCliente, fué trasladado a 
																	/servicioCliente/contacto/datosPersona.jsp
													 			-->
																<tiles:insert page="/servicioCliente/contacto/datosPersona.jsp">
																		<tiles:put name="vtformName" value="cotizarRecotizarReservarForm"/>
																</tiles:insert>
															</td>
														</logic:empty>
														<logic:notEmpty name="ec.com.smx.sic.sispe.pedioGeneral">
															<td>
															<div id="div_empresasConsolidadas" style="width:100%;height:55px;overflow-x:auto;overflow-y:auto;">
																<table border="0" cellspacing="0" cellpadding="0" align="left" width="100%" class=tabla_informacion_negro>
																	<tr class="tituloTablasCeleste">
																		<td class="fila_contenido_negro columna_contenido_der_negro" width="20%" align="left">&nbsp;Código pedido</td>
																		<td align="left" width="80%" class="fila_contenido_negro columna_contenido_der_negro">Información empresa</td>
																	</tr>
																	<logic:iterate name="ec.com.smx.sic.sispe.empresas.vistaPedidoCol" id="vistaPedidoDTO" indexId="numeroRegistro">
																	<tr>
																		<td align="left"  class="textoNegro11">
																			<bean:write name="vistaPedidoDTO" property="id.codigoPedido"/>
																		</td>
																		<td align="left"  class="textoNegro11">
																			<bean:write name="vistaPedidoDTO" property="contactoEmpresa"/>
																		</td>
																	</tr>
																	</logic:iterate>
																</table>
															</div>
															</td>
														</logic:notEmpty>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td valign="top">
													<!-- @author Wladimir López	
															Todo el DIV de detalles, fué trasladado a 
																/servicioCliente/cotizarRecotizarReservar/detallePedido.jsp
														 -->
														<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
												</td>
											</tr>
										</table>
									</div>
								</td>
								<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
							</tr>
						</table>
					</div>
				</td>
			</tr>
	</table>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>