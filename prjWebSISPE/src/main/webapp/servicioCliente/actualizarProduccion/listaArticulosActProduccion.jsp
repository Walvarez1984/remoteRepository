<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<bean:define id="esCanasto" name="ec.com.smx.sic.sispe.canasto"/>
<logic:notEmpty name="sispe.estado.activo">
	<bean:define id="estadoActivo"><bean:write name="sispe.estado.activo"/></bean:define>
</logic:notEmpty>
<bean:define id="claCanCat" name="ec.com.smx.sic.sispe.clasificacion.canastosCatalogo"/>
<table border="0" class="textoNegro11" width="99%" align="center" cellpadding="0" cellspacing="0">
	<!-- OANDINO: Sección de ventana emergente para intercambio de artículos -->
	<tr>
        <td colspan="3">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                <tr>
                    <td align="center">
                        <div id="popUpIntercambio">
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
		<%--Barra Izquierda--%>
		<td class="datos" width="26%">
			<table width="100%" border="0" cellpadding="1" cellspacing="0" bgcolor="white">
				<%-- B&uacute;squeda--%>
				<tr>
					<td colspan="2">
						<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
					</td>
				</tr>
			</table>
		</td>
		<%--Fin Barra Izquierda--%>
		<%-- Separador --%>
		<TD class="datos" width="2px">&nbsp;</TD>
		<%--Datos--%>
		<TD class="datos" width="82%">
			<div id="resultadosBusqueda">
				<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
					<%--Titulo de los datos--%>
					<tr>
						<td class="fila_titulo" colspan="7" height="26px">
							<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
								<tr>
									<td width="3%">
										&nbsp;<img src="images/articulos24.gif" border="0"/>
									</td>
									<td width="85%" align="left">
										<b>&nbsp;Lista de art&iacute;culos</b>
									</td>
									<logic:notEmpty name="controlProduccionForm" property="datos">
										<td width="10%" align="left">
											<div id="botonD">
												<html:link styleClass="actualizarD" href="#" onclick="requestAjax('actualizarProduccion.do',['mensajes','resultadosBusqueda'],{parameters: 'botonActProduccion=ok',evalScripts:true});">Actualizar</html:link>
											</div>
										</td>
									</logic:notEmpty>
									<td width="2%"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td height="4px" bgcolor="#F4F5EB"></td></tr>
					<tr>
						<td bgcolor="#F4F5EB">
							<table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
								<logic:notEmpty name="controlProduccionForm" property="datos">
									<tr>
										<td>
											<table cellpadding="0" cellspacing="0" width="100%">
												<tr>
													<td align="left" class="textoRojo10" id="ordenarPor">
														<logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
															<bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
															<b>Ordenado por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
														</logic:notEmpty>
													</td>
													<td align="right">
														<smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" campos="false" url="actualizarProduccion.do"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0" cellspacing="0" cellpadding="1" width="100%">
												<tr class="tituloTablas"  align="left">
													<td class="columna_contenido" width="2%" align="center"></td>
													<td class="columna_contenido" width="4%" align="center">No</td>
													<td class="columna_contenido" width="14%" align="center"><a class="linkBlanco9" title="ordenar por c&oacute;digo barras" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=0'});">C&oacute;digo barras</a></td>
													<td class="columna_contenido" width="14%" align="center">Observación</td>
													<td class="columna_contenido" width="19%" align="center"><a class="linkBlanco9" title="ordenar por descripci&oacute;n del art&iacute;culo" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=1'});">Art&iacute;culo</a></td>
													<td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="ordenar por descripci&oacute;n del art&iacute;culo" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=6'});">Und. manejo</a></td>
													<td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="ordenar por cantidad pendiente" href="#" onclick="requestAjax('actualizarProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=4'});">Pendiente</a></td>
													<td class="columna_contenido" width="8%" align="center">Cant. producir</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
												<table border="0" cellspacing="0" cellpadding="1" width="100%">
													<bean:define id="contador" value="${0}" toScope="session"/>
													<logic:iterate name="controlProduccionForm" property="datos" id="vistaArticuloDTO" indexId="indiceArticulo">
														<bean:define id="indiceGlobal" value="${indiceArticulo + controlProduccionForm.start}"/>
														<bean:define id="numFila" value="${indiceGlobal + 1}"/>
														<%-- control del estilo para el color de las filas --%>
														<bean:define id="residuo" value="${indiceArticulo % 2}"/>
														<logic:equal name="residuo" value="0">
															<bean:define id="clase" value="blanco10"/>
														</logic:equal>
														<logic:notEqual name="residuo" value="0">
															<bean:define id="clase" value="grisClaro10"/>
														</logic:notEqual>
														<logic:notEmpty name="vistaArticuloDTO" property="npErrorCantidadAProducir">
															<bean:define id="clase" value="rojoObsuro10"/>
														</logic:notEmpty>
														<bean:define id="contador" value="${contador+1}" toScope="session"/>
														<tr class="${clase}" id="fila_${indiceArticulo}">
															<td class="columna_contenido fila_contenido" width="3%" align="center">
																<c:set var="desplegarItemsReceta" value=""/>
																<logic:notEmpty name="vistaArticuloDTO" property="npDesplegarItemsReceta">
																	<c:set var="desplegarItemsReceta" value="${estadoActivo}"/>
																</logic:notEmpty>
																<logic:notEmpty name="vistaArticuloDTO" property="articulos">
																	<c:set var="desplegarItemsReceta" value="${estadoActivo}"/>
																</logic:notEmpty>
																<logic:notEmpty name="ec.com.smx.sic.sise.articuloSel">
																	<logic:notEqual name="ec.com.smx.sic.sise.articuloSel" value="${indiceArticulo}">
																		<div id="desplegar${contador}">
																			<logic:equal name="desplegarItemsReceta" value="${estadoActivo}">
																				<a title="Ver detalle del art&iacute;culo" href="#" onClick="desplegar(${contador});">
																					<html:img src="images/desplegar.gif" border="0"/>
																				</a>
																			</logic:equal>
																			<logic:notEqual name="desplegarItemsReceta" value="${estadoActivo}">&nbsp;</logic:notEqual>
																		</div>
																		<div id="plegar${contador}" class="displayNo">
																			<a title="Ver detalle del art&iacute;culo" href="#" onClick="plegar(${contador});">
																				<html:img src="images/plegar.gif" border="0"/>
																			</a>
																		</div>
																	</logic:notEqual>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sise.articuloSel">
																	 <!-- OANDINO: Verificar que se muestre el signo respectivo al tener al mantener desplegado un árbol -->
																	 <c:set var="despliegueA1" value="displaySi"/>
																	<c:set var="despliegueA2" value="displayNo"/>
																	<logic:notEmpty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
																		<bean:define id="idArticuloProd" name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio"></bean:define>
																		<logic:equal name="idArticuloProd" value="${indiceArticulo}">
																			 <c:set var="despliegueA1" value="displayNo"/>
																			 <c:set var="despliegueA2" value="displaySi"/>
																		</logic:equal>
																	 </logic:notEmpty>
																	<div id="desplegar${contador}" class="${despliegueA1}">
																		<logic:equal name="desplegarItemsReceta" value="${estadoActivo}">
																			<a title="Ver detalle del art&iacute;culo" href="#" onClick="desplegar(${contador});">
																				<html:img src="images/desplegar.gif" border="0"/>
																			</a>
																		</logic:equal>
																		<logic:notEqual name="desplegarItemsReceta" value="${estadoActivo}">
																			&nbsp;
																		</logic:notEqual>
																	</div>
																	<div id="plegar${contador}" class="${despliegueA2}">
																		<a title="Ver detalle del art&iacute;culo" href="#" onClick="plegar(${contador});">
																			<html:img src="images/plegar.gif" border="0"/>
																		</a>
																	</div>
																</logic:empty>
																<logic:notEmpty name="ec.com.smx.sic.sise.articuloSel">
																	<logic:equal name="ec.com.smx.sic.sise.articuloSel" value="${indiceArticulo}">
																		<div id="plegar${contador}">
																			<logic:equal name="desplegarItemsReceta" value="${estadoActivo}">
																				<a title="Ver detalle de art&iacute;culo" href="#" onClick="plegar(${contador});">
																					<html:img src="images/plegar.gif" border="0"/>
																				</a>
																			</logic:equal>
																			<logic:notEqual name="desplegarItemsReceta" value="${estadoActivo}">&nbsp;</logic:notEqual>
																		</div>
																		<div id="desplegar${contador}" class="displayNo">
																			<a title="Ver detalle de art&iacute;culo" href="#" onClick="desplegar(${contador});">
																				<html:img src="images/desplegar.gif" border="0"/>
																			</a>
																		</div>
																	</logic:equal>
																</logic:notEmpty>
															</td>
															<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
															<td class="columna_contenido fila_contenido" width="14%" align="left">
															<!-- OANDINO: Validación de vínculo para intercambio de artículos -->
																<logic:equal name="vistaArticuloDTO" property="articuloReceta" value="${esCanasto}">
																	<bean:write name="vistaArticuloDTO" property="codigoBarras"/>
																</logic:equal>
																<logic:notEqual name="vistaArticuloDTO" property="articuloReceta" value="${esCanasto}">
																	<html:link title="Elegir art&iacute;culo para intercambio" href="#" onclick="requestAjax('actualizarProduccion.do', ['mensajes','popUpIntercambio'], {parameters: 'mostrarPopUpIntercambio=${indiceGlobal}'});mostrarModal();">																	
																		<bean:write name="vistaArticuloDTO" property="codigoBarras"/>
																	</html:link>
																</logic:notEqual>
															</td>
															<!-- OANDINO: Campo de texto para ingreso de observación en SCSPETESTDETPED -->
															<td class="columna_contenido fila_contenido" width="8%" align="right">
																<html:textarea property="observacionArticulo" value="" styleClass="textNormal"/>
															</td>
															<td class="columna_contenido fila_contenido" width="19%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
															<td class="columna_contenido fila_contenido" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="unidadManejo"/>&nbsp;</td>
															<td class="columna_contenido fila_contenido textoAzul10" width="8%" align="right"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
															<td class="columna_contenido fila_contenido columna_contenido_der" width="8%" align="center">
																<logic:equal name="vistaArticuloDTO" property="articuloReceta" value="${esCanasto}">
																	<logic:notEqual name="vistaArticuloDTO" property="codigoClasificacion" value="${claCanCat}">
																		<html:text property="cantidadProducida" value="${vistaArticuloDTO.npCantidadParcialEstado}" styleClass="textNormal" size="7" maxlength="5"/>
																	</logic:notEqual>
																	<logic:equal name="vistaArticuloDTO" property="codigoClasificacion" value="${claCanCat}">
																		${vistaArticuloDTO.diferenciaCantidadEstado}
																		<input type="hidden" name="cantidadProducida" value="${vistaArticuloDTO.diferenciaCantidadEstado}">
																	</logic:equal>
																</logic:equal>
																<logic:notEqual name="vistaArticuloDTO" property="articuloReceta" value="${esCanasto}">
																	<logic:empty name="vistaArticuloDTO" property="articulos">
																		<html:text property="cantidadProducida" value="${vistaArticuloDTO.npCantidadParcialEstado}" styleClass="textNormal" size="7" maxlength="5"/>
																	</logic:empty>
																	<logic:notEmpty name="vistaArticuloDTO" property="articulos">
																		<input type="hidden" name="cantidadProducida" value="${vistaArticuloDTO.npCantidadParcialEstado}"/>
																	</logic:notEmpty>
																</logic:notEqual>
															</td>
														</tr>
														<tr>
															<td colspan="8">
																<logic:notEmpty name="ec.com.smx.sic.sise.articuloSel">
																	<logic:equal name="ec.com.smx.sic.sise.articuloSel" value="${indiceArticulo}">
																		<bean:define id="mostrarS" value="block"/>
																	</logic:equal>
																</logic:notEmpty>
																<logic:notEmpty name="ec.com.smx.sic.sise.articuloSel">
																	<logic:notEqual name="ec.com.smx.sic.sise.articuloSel" value="${indiceArticulo}">
																		<bean:define id="mostrarS" value="none"/>
																	</logic:notEqual>
																</logic:notEmpty>
																<logic:empty name="ec.com.smx.sic.sise.articuloSel">
																	<bean:define id="mostrarS" value="none"/>
																	<!-- OANDINO: Mantener desplegado el detalle de un determinado registro -->
																	<logic:notEmpty name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio">
																		<bean:define id="idArticuloProd" name="ec.com.smx.sic.sispe.tipo.objeto.Intercambio"></bean:define>
																		<logic:equal name="idArticuloProd" value="${indiceArticulo}">
																			<bean:define id="mostrarS" value="block"/>
																		</logic:equal>
																	</logic:notEmpty>
																</logic:empty>
																
																<div id="marco${contador}" style="display:${mostrarS};">
																	<logic:notEmpty name="vistaArticuloDTO" property="articulos">
																		<table border="0" class="tabla_informacion_negro" width="98%" align="center" cellspacing="0" cellpadding="0">
																			<tr>
																				<td colspan="6">
																					<table border="0" cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion_negro">
																						<tr class="tituloTablas"  align="left">
																							<td class="tituloTablasCeleste" width="4%" align="center"></td>
																							<td class="tituloTablasCeleste" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
																							<td class="tituloTablasCeleste" width="63%" align="center">ART&Iacute;CULO</td>
																							<td class="tituloTablasCeleste" width="18%" align="center">CANT. PRODUCIR</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<table border="0" cellspacing="0" cellpadding="1" width="100%">
																						<logic:iterate name="vistaArticuloDTO" property="articulos" id="articuloDTO" indexId="indiceDetalleArticulo">
																							<bean:define id="contador" value="${contador+1}" toScope="session"/>
																							<bean:define id="fila" value="${indiceDetalleArticulo}"/>
																							<%-- control del estilo para el color de las filas --%>
																							<bean:define id="residuo" value="${indiceDetalleArticulo % 2}"/>
																							<logic:equal name="residuo" value="0">
																								<bean:define id="clase" value="blanco10"/>
																							</logic:equal>
																							<logic:notEqual name="residuo" value="0">
																								<bean:define id="clase" value="naranjaClaro10"/>
																							</logic:notEqual>
																							<tr class="${clase}" id="fila_${indiceDetalleArticulo}">
																								<td width="4%" align="center" height="25"  class="columna_contenido fila_contenido">
																								</td>
																								<td class="columna_contenido fila_contenido" width="15%" align="left"><bean:write name="articuloDTO" property="codigoBarrasActivo.id.codigoBarras"/></td>
																								<td class="columna_contenido fila_contenido" width="63%" align="left"><bean:write name="articuloDTO" property="descripcionArticulo"/></td>
																								<td class="columna_contenido fila_contenido columna_contenido_der" width="18%" align="center">
																									<html:text property="cantidadProducida" value="${articuloDTO.npCantidadAProducir}" styleClass="textNormal" size="7" maxlength="5"/>
																								</td>
																							</tr>
																						</logic:iterate>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</logic:notEmpty>
																	<!-- Esta sección es la que se debe mostrar abierta luego de ingresar al vínculo de un articulo de tipo RecetaDTO -->
																	<div id="subtabla${indiceArticulo}">
																		<logic:notEmpty name="vistaArticuloDTO" property="npDesplegarItemsReceta">
																			<table border="0" class="tabla_informacion_negro" width="98%" align="center" cellspacing="0" cellpadding="0">
																				<tr>
																					<td colspan="6">
																						<table border="0" cellspacing="0" cellpadding="1" width="100%" class="tabla_informacion_negro">
																							<tr class="tituloTablas"  align="left">
																								<td class="tituloTablasCeleste" width="4%" align="center"></td>
																								<td class="tituloTablasCeleste" width="15%" align="center"><a class="" title="ordenar por c&oacute;digo barras" href="#" onclick="requestAjax('actualizarProduccion.do',['div_listado'],{parameters: 'indiceOrdenarA1=0&articuloF=${indiceArticulo}'});">C&Oacute;DIGO BARRAS</a></td>
																								<td class="tituloTablasCeleste" width="50%" align="center"><a class="" title="ordenar por nombre del art&iacute;culo" href="#" onclick="requestAjax('actualizarProduccion.do',['div_listado'],{parameters: 'indiceOrdenarA1=1&articuloF=${indiceArticulo}'});">ART&Iacute;CULO</a></td>
																								<td class="tituloTablasCeleste" width="13%" align="center"><a class="" title="ordenar por cantidad" href="#" onclick="requestAjax('actualizarProduccion.do',['div_listado'],{parameters: 'indiceOrdenarA1=2&articuloF=${indiceArticulo}'});">CANTIDAD</a></td>
																								<td class="tituloTablasCeleste" width="18%" align="center">CANT. MAX. PROD.</td>
																							</tr>
																						</table>
																					</td>
																				</tr>
																				<tr>
																					<td>
																						<table border="0" cellspacing="0" cellpadding="1" width="100%">
																							<logic:iterate name="vistaArticuloDTO" property="recetaArticulos" id="recetaArticuloDTO" indexId="indiceDetalleArticulo">
																								<bean:define id="contador" value="${contador+1}" toScope="session"/>
																								<bean:define id="fila" value="${indiceDetalleArticulo}"/>
																								<%-- control del estilo para el color de las filas --%>
																								<bean:define id="residuo" value="${indiceDetalleArticulo % 2}"/>
																								<logic:equal name="residuo" value="0">
																									<bean:define id="clase" value="blanco10"/>
																								</logic:equal>
																								<logic:notEqual name="residuo" value="0">
																									<bean:define id="clase" value="naranjaClaro10"/>
																								</logic:notEqual>
																								<logic:notEmpty name="recetaArticuloDTO" property="npErrorCantidadAProducir">
																									<bean:define id="clase" value="rojoObsuro10"/>
																								</logic:notEmpty>
																								<tr class="${clase}" id="fila_${indiceDetalleArticulo}">
																									<td width="4%" align="center" height="25"  class="columna_contenido fila_contenido">
																										<div id="desplegar${contador}">
																											<logic:notEmpty name="recetaArticuloDTO" property="npArticulos">
																												<a title="Ver detalle de art&iacute;culos" href="#" onClick="desplegar(${contador});">
																													<html:img src="images/desplegar.gif" border="0"/>
																												</a>
																											</logic:notEmpty>
																											<logic:empty name="recetaArticuloDTO" property="npArticulos">&nbsp;</logic:empty>
																										</div>
																										<div id="plegar${contador}" class="displayNo">
																											<a title="Ver detalle de art&iacute;culos" href="#" onClick="plegar(${contador});">
																												<html:img src="images/plegar.gif" border="0"/>
																											</a>
																										</div>
																									</td>
																									<!-- OANDINO: Se muestra vínculo en cada registro del detalle de las recetas -->
																									<td class="columna_contenido fila_contenido" width="15%" align="left">
																										<html:link title="Elegir artículo para intercambio" href="#" onclick="requestAjax('actualizarProduccion.do', ['mensajes','popUpIntercambio','div_x'], {parameters: 'mostrarPopUpIntercambio=${indiceArticulo}-${indiceDetalleArticulo}'});">
																											<bean:write name="recetaArticuloDTO" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/>
																										</html:link>
																										
																									</td>
																									<td class="columna_contenido fila_contenido" width="50%" align="left"><bean:write name="recetaArticuloDTO" property="articuloRelacion.descripcionArticulo"/></td>
																									<td class="columna_contenido fila_contenido" width="13%" align="left"><bean:write name="recetaArticuloDTO" property="cantidad"/></td>
																									<td class="columna_contenido fila_contenido columna_contenido_der" width="18%" align="center">
																										<bean:write name="recetaArticuloDTO" property="npCantidadMaximaProducir"/>
																									<%-- 	<logic:notEqual name="vistaArticuloDTO" property="articuloReceta" value="${esCanasto}"> --%>
																											<logic:notEmpty name="recetaArticuloDTO" property="articulo">																												
																												<input type="hidden" name="cantidadProducida" value="${recetaArticuloDTO.npCantidadMaximaProducir}"/>
																											</logic:notEmpty>
																									<%--	</logic:notEqual> --%>
																									</td>
																								</tr>
																								<tr>
																									<td colspan="8">
																										<div id="marco${contador}" class="displayNo">
																											<logic:notEmpty name="recetaArticuloDTO" property="npArticulos">
																												<bean:define id="articulos" name="recetaArticuloDTO" property="npArticulos" toScope="session"/>
																												<jsp:include page="listaArticulos.jsp"/>
																											</logic:notEmpty>
																										</div>
																									</td>
																								</tr>
																							</logic:iterate>
																						</table>
																					</td>
																				</tr>
																			</table>
																		</logic:notEmpty>
																	</div>
																</div>
															</td>
														</tr>
													</logic:iterate>
												</table>
											</div>
										</td>
									</tr>
								</logic:notEmpty>
								<logic:empty name="controlProduccionForm" property="datos">
									<tr>
										<td colspan="7">Seleccione un criterio de b&uacute;squeda</td>
									</tr>
								</logic:empty>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</TD>
		<%--Fin Datos--%>
	</tr>
</table>
<%--<script language="JavaScript" type="text/javascript">
	Field.activate('fechaInicial');
</script>--%>