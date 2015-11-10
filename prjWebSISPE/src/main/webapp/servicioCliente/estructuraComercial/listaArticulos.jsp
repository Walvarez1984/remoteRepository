<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
		
<div id="popUpBusquedaArticulos" class="popup" style="top:60px;" >
	<%-- lista de definiciones para las acciones --%>
	<bean:define id="crePedEsp"><bean:message key="ec.com.smx.sic.sispe.accion.crearPedidoEspecial"/></bean:define>
	
	<logic:notEmpty name="sispe.estado.activo">
		<bean:define name="sispe.estado.activo" id="estadoActivo"/>
	</logic:notEmpty>
	<logic:notEmpty name="sispe.estado.inactivo">
		<bean:define name="sispe.estado.inactivo" id="estadoInactivo"/>
	</logic:notEmpty>
	<bean:define id="estadoObsoleto"><bean:message key="ec.com.smx.sic.sispe.claseArticulo.obsoleto"/></bean:define>
		<TABLE border="0" cellspacing="0" cellpadding="2" width="80%" align="center" class="tabla_informacion" bgcolor="#F1F3F5">
			<tr>
				  <td background="images/barralogin.gif" align="center">
					  <table cellpadding="0" cellspacing="0" width="100%" border="0">			
						  <tr>
							  <td class="textoBlanco11" align="left">
								&nbsp;<b>B&uacute;squeda de art&iacute;culos</b>
							  </td>
							  <!-- Boton Cerrar -->
							  <td align="right">
								<logic:notEmpty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
									<html:link title="Cerrar" href="#" onclick="javascript:requestAjax('crearPedidoEspecial.do',['pregunta','mensajes','detallePedido','divTabs','tabPedidoEspecial'],{parameters: 'actualizarDetalle=ok', evalScripts: true});">																						   
										<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
									</html:link>&nbsp;
								</logic:notEmpty>
								<logic:notEmpty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO">
									<html:link title="Cerrar" href="#" onclick="javascript:requestAjax('detalleCanasta.do',['pregunta','detalleCanasto','cabezeraCanasto'],{parameters: 'actualizarCanasto=ok&buscador=ok',evalScripts:true});">
										<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
									</html:link>&nbsp;
								</logic:notEmpty>
								<logic:empty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO">
									<logic:empty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
										<html:link title="Cerrar" href="#" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&buscador=ok',evalScripts:true});">
											<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
										</html:link>&nbsp;
									</logic:empty>
								</logic:empty>
							  </td>		                           
						  </tr>
					  </table>
				  </td>
			 </tr>
			 <tr>
			 	<td>
					 <div id="mensajesPopUpArticulos" style="font-size:1px;position:relative;">
						<jsp:include page="/include/mensajes.jsp"/>	
					</div>
				</td>
			 </tr>
			<tr>
				<td height="35px">
					<table class="titulosAccion" border="0" width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td  width="3%" align="center"><img src="images/lista.gif" border="0"></img></td>
							<td align="left">&nbsp;&nbsp;Lista de art&iacute;culos</td>
							<td align="right">
								<table cellspacing="0">
									<tr>
										<logic:notEmpty name="ec.com.smx.sic.sispe.busqueda.porCatalogo">
											<td>  
												<div id="botonA">
													<a class="atrasA" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','pregunta'],{parameters: 'atras=ok&buscador=ok'});">Atras</a>
												</div>
											</td>
										</logic:notEmpty>
										
										<c:set var="parametroAgregar" value="AP"/>
										<logic:notEmpty name="ec.com.smx.sic.sispe.busqueda.porMantenimiento">
											<c:set var="parametroAgregar" value="AM"/>
										</logic:notEmpty>
										 <logic:notEmpty name="ec.com.smx.sic.sispe.busqueda.porPedidosEspeciales">
											<c:set var="parametroAgregar" value="APE"/>
										</logic:notEmpty>
										<!-- esta condicion se aplica cunado se desea modificar una reserva que está pagada completamente -->
										<logic:empty name="ec.com.smx.sic.sispe.modificarReserva.pagadoTotalmente">
											<td>
												<logic:notEmpty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
													<div id="botonA">													
														<a class="aceptarA" href="#" onclick="requestAjax('crearPedidoEspecial.do',['pregunta','mensajesPopUpArticulos'], {parameters: 'agregarArticulos=${parametroAgregar}&buscador=ok'});">Agregar</a>
													</div>
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
													<div id="botonA">													
														<a class="aceptarA" href="#" onclick="requestAjax('crearCotizacion.do',['pregunta','mensajesPopUpArticulos'], {parameters: 'agregarArticulos=${parametroAgregar}&buscador=ok'});">Agregar</a>
													</div>
												</logic:empty>
											</td>
										</logic:empty>
										<td>
											<bean:define id="exit" value="ok"/>
											<div id="botonA">
												<logic:notEmpty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
													<a href="#" class="cancelarA" onclick="javascript:requestAjax('crearPedidoEspecial.do',['pregunta','mensajes','detallePedido','divTabs','tabPedidoEspecial'],{parameters: 'actualizarDetalle=ok', evalScripts: true});">Cerrar</a>																						   								
												</logic:notEmpty>
												<logic:notEmpty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO">
													<a href="#" class="cancelarA" onclick="javascript:requestAjax('detalleCanasta.do',['pregunta','detalleCanasto','cabezeraCanasto'],{parameters: 'actualizarCanasto=ok&buscador=ok',evalScripts:true});">Cerrar</a>													
												</logic:notEmpty>
												<logic:empty name="ec.com.smx.sic.sispe.receta.detallePedidoDTO">
													<logic:empty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
														<a href="#" class="cancelarA" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&buscador=ok',evalScripts: true});">Cerrar</a>
													</logic:empty>
												</logic:empty>
												<!--<a href="#" class="cancelarA" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','seccion_detalle'],{parameters: 'actualizarDetalle=ok&buscador=ok',evalScripts: true});">Cerrar</a> -->
											</div>
										</td>
									</tr>
								</table>		
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center" valign="top">
					<table border=0 cellspacing="0" cellpadding="0" width="100%">
						<logic:empty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
							<logic:notEmpty name="cotizarRecotizarReservarForm" property="datos">
								<tr><td height="3px"></td></tr>
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td align="left">
													<table cellpadding="0" cellspacing="2" class="tabla_informacion textoAzul10" align="left">
														<tr>
															<td bgcolor="#ffd6c1" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Sin alcance</td>
															<td>&nbsp;</td>
															<td bgcolor="#ffedd2" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Sin stock</td>
															<td>&nbsp;</td>
															<td bgcolor="#ffffca" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Obsoletos</td>
														</tr>
													</table>
													
												</td>
												<td align="right">
													<smx:paginacion start="${cotizarRecotizarReservarForm.start}" range="${cotizarRecotizarReservarForm.range}" results="${cotizarRecotizarReservarForm.size}" campos="false" styleClass="textoNegro11" url="crearCotizacion.do?buscador=ok" requestAjax="'mensajes','popUpBusquedaArticulos'"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="3px"></td></tr>
								<tr>	
									<td>
										<div style="width:100%;height:455px;overflow:auto;border:1px solid #cccccc">
											<table align="left" cellspacing="0" cellpadding="0" width="98%">
												<tr><td height="5px"></td></tr>
												<tr class="tituloTablas"  align="left">
													<td width="3%" class="columna_contenido">
														<html:checkbox name="cotizarRecotizarReservarForm" title="a&ntilde;adir todos los productos" property="opEscogerTodos" value="t" onclick="activarDesactivarTodo(this,cotizarRecotizarReservarForm.opEscogerProdBuscados);"></html:checkbox>
													</td>
													<td align="center" class="columna_contenido">No</td>
													<td align="center" class="columna_contenido">CLASIFICACI&Oacute;N</td>
													<td align="center" class="columna_contenido">C&Oacute;DIGO BARRAS</td>
													<td align="center" class="columna_contenido">DESCRIPCI&Oacute;N</td>
													<td align="center" class="columna_contenido">TAMAÑO</td>
													<td align="center" class="columna_contenido">MARCA</td>
													<td align="center" class="columna_contenido">PRECIO</td>
													<td align="center" class="columna_contenido">IVA</td>
													<td align="center" class="columna_contenido columna_contenido_der">PRECIO+IVA</td>
												</tr>
												<logic:iterate name="cotizarRecotizarReservarForm" property="datos" id="articulo" indexId="numArticulo">
													<%--------- control del estilo para el color de las filas --------------%>
													<bean:define id="indiceGlobal" value="${cotizarRecotizarReservarForm.start + numArticulo}"/>
													<bean:define id="residuo" value="${numArticulo % 2}"/>
													<bean:define id="numFila" value="${indiceGlobal + 1}"/>
													<logic:equal name="residuo" value="0">
														<bean:define id="clase" value="blanco10"/>
													</logic:equal>
													<logic:notEqual name="residuo" value="0">
														<bean:define id="clase" value="grisClaro10"/>
													</logic:notEqual>
													<%----- control del estilo para el stock y alcance -----%>
													<%---- control del estilo para el alcance -----%>
													<c:choose>
														<c:when test="${articulo.npAlcance == estadoInactivo}">
															<c:set var="clase" value="rojoClaro10"/>
															<c:set var="title_fila" value="art&iacute;culo sin ALCANCE"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<%--------- control del estilo para el stock -------%>
																<c:when test="${articulo.npEstadoStockArticulo == estadoInactivo}">
																	<!-- el stock no se muestra cuando se agregan artículos desde la pantalla de carnes -->
																	<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${crePedEsp}">
																		<c:set var="clase" value="naranjaClaro10"/>
																		<c:set var="title_fila" value="art&iacute;culo sin STOCK"/>
																	</logic:notEqual>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<%-- articulo obsoleto --%>
																		<c:when test="${articulo.claseArticulo != null && articulo.claseArticulo == estadoObsoleto}">
																			<c:set var="clase" value="amarilloClaro10"/>
																			<c:set var="title_fila" value="art&iacute;culo OBSOLETO"/>
																		</c:when>
																	</c:choose>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
													<tr class="${clase}"> 
														<bean:define name="articulo" property="id" id ="id"/>
														<td class="columna_contenido fila_contenido" align="center">
															<html:multibox name="cotizarRecotizarReservarForm" property="opEscogerProdBuscados">
																<bean:write name="numArticulo"/>
															</html:multibox>
														</td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="numFila"/></td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="articulo" property="codigoClasificacion"/></td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>
														<td align="left" class="columna_contenido fila_contenido">
															<bean:write name="articulo" property="descripcionArticulo"/>
															<!-- el stock no se muestra cuando se agregan artículos desde la pantalla de carnes -->
															<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${crePedEsp}">
																<logic:notEmpty name="articulo" property="npStockArticulo">
																	<label class="textoAzul10">&nbsp;(Stock: <bean:write name="articulo" property="npStockArticulo"/>)</label>
																</logic:notEmpty>
															</logic:notEqual>
														</td>
														<td align="right" class="columna_contenido fila_contenido"><logic:present name="articulo" property="articuloMedidaDTO.referenciaMedida">
															<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>
														</logic:present>
														<logic:notPresent name="articulo" property="articuloMedidaDTO.referenciaMedida">
															&nbsp;
														</logic:notPresent>
														</td>
														<td align="right" class="columna_contenido fila_contenido">
														<logic:present name="articulo" property="articuloComercialDTO.marcaComercialArticulo">
															<bean:write name="articulo" property="articuloComercialDTO.marcaComercialArticulo.nombre"/>
														</logic:present>
														<logic:notPresent name="articulo" property="articuloComercialDTO.marcaComercialArticulo">
															&nbsp;
														</logic:notPresent>
														</td>
														<td align="right" class="columna_contenido fila_contenido">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articulo.precioBase}"/></td>
														<td align="center" class="columna_contenido fila_contenido">
															
															
															<c:set var="aplicaImpuesto" value="${articulo.aplicaImpuestoVenta}"/>							
															<c:if test="${aplicaImpuesto}">
																<img src="./images/tick.png" border="0">
															</c:if>
															<c:if test="${!aplicaImpuesto}">
																<img src="./images/x.png" border="0">
															</c:if>
															
														</td>
														<td align="right" class="columna_contenido fila_contenido columna_contenido_der"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articulo.precioBaseImp}"/></td>
													</tr>
												</logic:iterate>
											</table>
										</div>
									</td>
								</tr>
								<tr><td height="10px"></td></tr>
							</logic:notEmpty>
							<logic:empty name="cotizarRecotizarReservarForm" property="datos">
								<table class="fondoBlanco textoNegro11" align="left" cellspacing="0" cellpadding="0" width="100%" height="400px">
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
								</table>
							</logic:empty>
						</logic:empty>
						
						
						<!-- PARA BUSQUEDA DE PEDIDOS ESPECIALES -->
						<logic:notEmpty name="ec.com.smx.sic.sispe.busquedaPedEspeciales">
							<logic:notEmpty name="crearPedidoEspecialForm" property="datos">
								<tr><td height="3px"></td></tr>
								<tr>
									<td>
										<table cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td align="left">
													<table cellpadding="0" cellspacing="2" class="tabla_informacion textoAzul10" align="left">
														<tr>
															<td bgcolor="#ffd6c1" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Sin alcance</td>
															<td>&nbsp;</td>
															<td bgcolor="#ffedd2" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Sin stock</td>
															<td>&nbsp;</td>
															<td bgcolor="#ffffca" width="15%" style="border:1px solid #cccccc">&nbsp;</td>
															<td>Obsoletos</td>
														</tr>
													</table>
													
												</td>
												<td align="right">
													<smx:paginacion start="${crearPedidoEspecialForm.start}" range="${crearPedidoEspecialForm.range}" results="${crearPedidoEspecialForm.size}" campos="false" styleClass="textoNegro11" url="crearPedidoEspecial.do?buscador=ok" requestAjax="'mensajes','popUpBusquedaArticulos'"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="3px"></td></tr>
								<tr>	
									<td>
										<div style="width:100%;height:455px;overflow:auto;border:1px solid #cccccc">
											<table align="left" cellspacing="0" cellpadding="0" width="98%">
												<tr><td height="5px"></td></tr>
												<tr class="tituloTablas"  align="left">
													<td width="3%" class="columna_contenido">
														<html:checkbox name="crearPedidoEspecialForm" title="a&ntilde;adir todos los productos" property="opEscogerTodos" value="t" onclick="activarDesactivarTodo(this,crearPedidoEspecialForm.opEscogerProdBuscados);"></html:checkbox>
													</td>
													<td align="center" class="columna_contenido">No</td>
													<td align="center" class="columna_contenido">CLASIFICACI&Oacute;N</td>
													<td align="center" class="columna_contenido">C&Oacute;DIGO BARRAS</td>
													<td align="center" class="columna_contenido">DESCRIPCI&Oacute;N</td>
													<td align="center" class="columna_contenido">TAMAÑO</td>
													<td align="center" class="columna_contenido">MARCA</td>
													<td align="center" class="columna_contenido">PRECIO</td>
													<td align="center" class="columna_contenido">IVA</td>
													<td align="center" class="columna_contenido columna_contenido_der">PRECIO+IVA</td>
												</tr>
												<logic:iterate name="crearPedidoEspecialForm" property="datos" id="articulo" indexId="numArticulo">
													<%--------- control del estilo para el color de las filas --------------%>
													<bean:define id="indiceGlobal" value="${crearPedidoEspecialForm.start + numArticulo}"/>
													<bean:define id="residuo" value="${numArticulo % 2}"/>
													<bean:define id="numFila" value="${indiceGlobal + 1}"/>
													<logic:equal name="residuo" value="0">
														<bean:define id="clase" value="blanco10"/>
													</logic:equal>
													<logic:notEqual name="residuo" value="0">
														<bean:define id="clase" value="grisClaro10"/>
													</logic:notEqual>
													<%----- control del estilo para el stock y alcance -----%>
													<%---- control del estilo para el alcance -----%>
													<c:choose>
														<c:when test="${articulo.npAlcance == estadoInactivo}">
															<c:set var="clase" value="rojoClaro10"/>
															<c:set var="title_fila" value="art&iacute;culo sin ALCANCE"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<%--------- control del estilo para el stock -------%>
																<c:when test="${articulo.npEstadoStockArticulo == estadoInactivo}">
																	<!-- el stock no se muestra cuando se agregan artículos desde la pantalla de carnes -->
																	<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${crePedEsp}">
																		<c:set var="clase" value="naranjaClaro10"/>
																		<c:set var="title_fila" value="art&iacute;culo sin STOCK"/>
																	</logic:notEqual>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<%-- articulo obsoleto --%>
																		<c:when test="${articulo.claseArticulo != null && articulo.claseArticulo == estadoObsoleto}">
																			<c:set var="clase" value="amarilloClaro10"/>
																			<c:set var="title_fila" value="art&iacute;culo OBSOLETO"/>
																		</c:when>
																	</c:choose>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
													<tr class="${clase}"> 
														<bean:define name="articulo" property="id" id ="id"/>
														<td class="columna_contenido fila_contenido" align="center">
															<html:multibox name="crearPedidoEspecialForm" property="opEscogerProdBuscados">
																<bean:write name="numArticulo"/>
															</html:multibox>
														</td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="numFila"/></td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="articulo" property="codigoClasificacion"/></td>
														<td align="center" class="columna_contenido fila_contenido"><bean:write name="articulo" property="codigoBarrasActivo.id.codigoBarras"/></td>
														<td align="left" class="columna_contenido fila_contenido">
															<bean:write name="articulo" property="descripcionArticulo"/>
															<!-- el stock no se muestra cuando se agregan artículos desde la pantalla de carnes -->
															<logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${crePedEsp}">
																<logic:notEmpty name="articulo" property="npStockArticulo">
																	<label class="textoAzul10">&nbsp;(Stock: <bean:write name="articulo" property="npStockArticulo"/>)</label>
																</logic:notEmpty>
															</logic:notEqual>
														</td>
														<td align="right" class="columna_contenido fila_contenido"><logic:present name="articulo" property="articuloMedidaDTO.referenciaMedida">
															<bean:write name="articulo" property="articuloMedidaDTO.referenciaMedida"/>
														</logic:present>
														<logic:notPresent name="articulo" property="articuloMedidaDTO.referenciaMedida">
															&nbsp;
														</logic:notPresent>
														</td>
														<td align="right" class="columna_contenido fila_contenido">
														<logic:present name="articulo" property="articuloComercialDTO.marcaComercialArticulo">
															<bean:write name="articulo" property="articuloComercialDTO.marcaComercialArticulo.nombre"/>
														</logic:present>
														<logic:notPresent name="articulo" property="articuloComercialDTO.marcaComercialArticulo">
															&nbsp;
														</logic:notPresent>
														</td>
														<td align="right" class="columna_contenido fila_contenido">
														<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articulo.precioBase}"/></td>
														<td align="center" class="columna_contenido fila_contenido">
															
															
															<c:set var="aplicaImpuesto" value="${articulo.aplicaImpuestoVenta}"/>							
															<c:if test="${aplicaImpuesto}">
																<img src="./images/tick.png" border="0">
															</c:if>
															<c:if test="${!aplicaImpuesto}">
																<img src="./images/x.png" border="0">
															</c:if>
															
														</td>
														<td align="right" class="columna_contenido fila_contenido columna_contenido_der"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articulo.precioBaseImp}"/></td>
													</tr>
												</logic:iterate>
											</table>
										</div>
									</td>
								</tr>
								<tr><td height="10px"></td></tr>
							</logic:notEmpty>
							<logic:empty name="crearPedidoEspecialForm" property="datos">
								<table class="fondoBlanco textoNegro11" align="left" cellspacing="0" cellpadding="0" width="100%" height="400px">
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
								</table>
							</logic:empty>
						</logic:notEmpty>
					</table>
				</td>
			</tr>
		</TABLE>
</div>