<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<logic:notEmpty name="sispe.estado.activo">
    <bean:define id="estadoActivo"><bean:write name="sispe.estado.activo"/></bean:define>
</logic:notEmpty>

<logic:notEmpty name="ec.com.smx.sic.sispe.despachoPerecibles.localConDiferentesDestinos">
    <bean:define id="localConDiferentesDestinos" name="ec.com.smx.sic.sispe.despachoPerecibles.localConDiferentesDestinos"/>
</logic:notEmpty>

<table border="0" class="textoNegro11" width="99%" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <!--Barra Izquierda-->
        <td class="datos" width="26%"> 
            <table width="100%" border="0" cellpadding="1" cellspacing="0" bgcolor="white">
                <%-- Busqueda--%>
                <tr>
                    <td colspan="2">
                        <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                    </td>
                </tr>
            </table>
        </td>
        <!--Fin Barra Izquierda-->
        
        <!-- Separador -->
        <TD class="datos" width="2px">&nbsp;</TD>
        
        <!--Datos-->
        <TD class="datos" width="82%">
            <div id="resultadosBusqueda">
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" bgcolor="white">
                    <!--Titulo de los datos-->
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
                                    <logic:notEqual name="sispe.vistaEntidadResponsableDTO" property="id.idRol" value="SISPEMONPED">
                                    	<logic:notEmpty name="controlProduccionForm" property="datos">
	                                        <logic:empty name="ec.com.smx.sic.sispe.consulta.despacho.totales">
	                                            <td width="10%" align="left">
	                                                <div id="botonD">
	                                                    <html:link styleClass="despachoD" href="#" onclick="requestAjax('actualizarDespacho.do',['mensajes','resultadosBusqueda'],{parameters: 'botonActProduccion=ok',evalScripts:true});">&nbsp;Despachar</html:link>
	                                                </div>
	                                            </td>
	                                        </logic:empty>
	                                    </logic:notEmpty>
                                    </logic:notEqual>
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
                                        <td colspan="6">
                                            <table cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td align="left" class="textoRojo10" id="ordenarPor">
                                                        <logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
                                                            <bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
                                                            <b>Ordenado por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
                                                        </logic:notEmpty>
                                                    </td>
                                                    <td align="right" id="pag">
                                                        <smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" url="actualizarDespacho.do" requestAjax="'mensajes','pag','div_listado'"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <logic:empty name="ec.com.smx.sic.sispe.consulta.despacho.totales">
                                        <tr>
                                            <td>
                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                    <tr class="tituloTablas"  align="left">
                                                        <td class="columna_contenido" width="3%" align="center"></td>
                                                        <td class="columna_contenido" width="4%" align="center">No</td>
                                                        <td class="columna_contenido" width="12%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo barras" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=0'});">C&oacute;digo barras</a></td>
                                                        <td class="columna_contenido" width="35%" align="center"><a class="linkBlanco9" title="Ordenar por descripci&oacute;n del art&iacute;culo" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=1'});">Art&iacute;culo</a></td>
                                                        <td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="Ordenar por fecha de despacho" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=6'});">Fecha despacho</a></td>
                                                        <td class="columna_contenido" width="8%" align="center">Stock</td>                                                    
                                                        <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad pendiente" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=4'});">Pendiente</a></td>
                                                        <logic:notEqual name="sispe.vistaEntidadResponsableDTO" property="id.idRol" value="SISPEMONPED">
                                                        	<td class="columna_contenido" width="8%" align="center">Cant. despachar</td>
                                                        </logic:notEqual>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                                    <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                                        <logic:iterate name="controlProduccionForm" property="datos" id="vistaArticuloDTO" indexId="indiceArticulo">
                                                            <bean:define id="indiceGlobal" value="${indiceArticulo + controlProduccionForm.start}"/>
                                                            <bean:define id="numFila" value="${indiceGlobal + 1}"/>
                                                            <%--------- control del estilo para el color de las filas --------------%>
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
                                                            <%--------------------------------------------------------------------%>
                                                            <tr class="${clase}"> 
                                                                <td class="columna_contenido fila_contenido" width="3%" align="center">
                                                                    <c:set var="despliegueA1" value="block"/>
                                                                    <c:set var="despliegueA2" value="none"/>
                                                                    <logic:notEmpty name="indiceNivel1">
                                                                        <logic:equal name="indiceNivel1" value="${indiceGlobal}">
                                                                            <c:set var="despliegueA1" value="none"/>
                                                                            <c:set var="despliegueA2" value="block"/>
                                                                        </logic:equal>
                                                                    </logic:notEmpty>
                                                                    
                                                                    <div style="display:${despliegueA1}" id="desplegar_${indiceArticulo}">
                                                                        <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}']);show(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);"></a>
                                                                    </div>
                                                                    <div style="display:${despliegueA2}" id="plegar_${indiceArticulo}">
                                                                        <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}','listado_${indiceArticulo}']);show(['desplegar_${indiceArticulo}']);"></a>
                                                                    </div>
                                                                </td>
                                                                <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
                                                                <td class="columna_contenido fila_contenido" width="12%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                                                                <td class="columna_contenido fila_contenido" width="35%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                                                               
                                                                <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
                                                                <td class="columna_contenido fila_contenido textoAzul10" width="8%" align="right"><b><bean:write name="vistaArticuloDTO" property="npStockArticulo"/></b></td>
                                                                <td class="columna_contenido fila_contenido textoAzul10" width="8%" align="right"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
                                                                <logic:notEqual name="sispe.vistaEntidadResponsableDTO" property="id.idRol" value="SISPEMONPED">
	                                                                <td class="columna_contenido fila_contenido columna_contenido_der" width="8%" align="center">
	                                                                    <html:text property="cantidadProducida" value="${vistaArticuloDTO.npCantidadParcialEstado}" styleClass="textNormal" size="7" maxlength="5"/>
	                                                                </td>
	                                                          	</logic:notEqual>
                                                            </tr>
                                                            <tr>
                                                                <td colspan="8" class="columna_contenido" align="center">
                                                                    <!-- se muestra el detalle de los locales -->
                                                                    <table cellpadding="1" cellspacing="0" width="100%">
																		<tr>
																			<td>
																				<div id="listado_${indiceArticulo}" style="display:${despliegueA2}">
																					<table width="95%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion_negro ">
																						<tr class="tituloTablasCeleste">
																							<td class="columna_contenido" width="3%" align="center">&nbsp;</td>
																							<td class="columna_contenido" width="4%" align="center">No</td>
																							<td class="columna_contenido" width="40%" align="center">Local</td>
																							<td class="columna_contenido" width="10%" align="center">Pendiente</td>
																						</tr>
																						<logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
																							<%--------- control del estilo para el color de las filas --------------%>
																							<bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
																							<bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
																							<logic:equal name="residuo2" value="0">
																								<bean:define id="clase2" value="blanco10"/>
																							</logic:equal>
																							<logic:notEqual name="residuo2" value="0">
																								<bean:define id="clase2" value="amarilloClaro10"/>
																							</logic:notEqual>
																							<logic:equal name="vistaArticuloDTO2" property="npComportamientoRegistro" value="${localConDiferentesDestinos}">
																								<bean:define id="clase2" value="celesteFosforescente"/>
																							</logic:equal>
																							
																							<%--------------------------------------------------------------------%>
																							<tr class="${clase2}">
																								<c:set var="despliegueL1" value="block"/>
																								<c:set var="despliegueL2" value="none"/>
																								<logic:notEmpty name="indiceNivel2">
																									<logic:equal name="indiceNivel1" value="${indiceGlobal}">
																										<logic:equal name="indiceNivel2" value="${indiceArticulo2}">
																											<c:set var="despliegueL1" value="none"/>
																											<c:set var="despliegueL2" value="block"/>
																										</logic:equal>
																									</logic:equal>
																								</logic:notEmpty>
																								
																								<td class="columna_contenido fila_contenido" width="3%" align="center">
																									<div style="display:${despliegueL1}" id="desplegar_${indiceArticulo}_${indiceArticulo2}">
																										<a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}_${indiceArticulo2}']);show(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);"></a>
																									</div>
																									<div style="display:${despliegueL2}" id="plegar_${indiceArticulo}_${indiceArticulo2}">
																										<a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);show(['desplegar_${indiceArticulo}_${indiceArticulo2}']);"></a>
																									</div>
																								</td>
																								<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila2"/></td>
																								<td class="columna_contenido fila_contenido" width="40%" align="left"><bean:write name="vistaArticuloDTO2" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO2" property="nombreLocalOrigen"/></td>																								
																								<td class="columna_contenido fila_contenido textoAzul10" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b></td>																								
																							</tr>
																							<tr>
																								<td colspan="7"  align="center" >
																									<!-- se muestra el detalle de los locales -->
																									<table cellpadding="1" cellspacing="0" width="100%">
																										<tr>
																											<td>
																												<div id="listado_${indiceArticulo}_${indiceArticulo2}" style="display:${despliegueL2}">
																													<table width="85%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion_negro ">
																														<tr class="tituloTablasCeleste">
																															<td class="columna_contenido" width="4%" align="center">No</td>
																															<td class="columna_contenido" width="15%" align="center">Pedido</td>
																															<td class="columna_contenido" width="15%" align="center">Reserva</td>
																															<td class="columna_contenido" width="30%" align="center">Lugar Entrega</td>
																															<td class="columna_contenido" width="15%" align="center">Fecha Entrega</td>
																															<td class="columna_contenido" width="10%" align="center">Pendiente</td>
																														</tr>
																														<logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
																															<%--------- control del estilo para el color de las filas --------------%>
																															<bean:define id="residuo3" value="${indiceArticulo3 % 2}"/>
																															<bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
																															<logic:equal name="residuo3" value="0">
																																<bean:define id="clase3" value="blanco10"/>
																															</logic:equal>
																															<logic:notEqual name="residuo3" value="0">
																																<bean:define id="clase3" value="amarilloClaro10"/>
																															</logic:notEqual>
																															<%--------------------------------------------------------------------%>
																															<tr class="${clase3}">
																																<td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila3"/></td>
																																<td class="columna_contenido fila_contenido" width="15%" align="left"><html:link title="Detalle del pedido" onclick="realizarEnvio('IPVA-${indiceGlobal}-${indiceArticulo2}-${indiceArticulo3}');" href="#"><bean:write name="vistaArticuloDTO3" property="id.codigoPedido"/></html:link></td>
																																<td class="columna_contenido fila_contenido" width="15%" align="left"><bean:write name="vistaArticuloDTO3" property="llaveContratoPOS"/></td>
																																<%--<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></td>
																															<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO3" property="cantidadParcialEstado"/></td>--%>
																																<td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
																																<td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
																																<%--<td class="columna_contenido fila_contenido" width="11%" align="right"><bean:write name="vistaArticuloDTO3" property="porcentajeProducido" formatKey="formatos.numeros"/>%</td>--%>
																																<td class="columna_contenido fila_contenido textoAzul10" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="diferenciaCantidadEstado"/></b></td>
																															</tr>
																														</logic:iterate>
																													</table>
																												</div>
																											</td>																										
																										</tr>
																									</table>
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
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:empty>
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.consulta.despacho.totales">
                                        <tr>
                                            <td>
                                                <table border="0" cellspacing="0" cellpadding="2" width="100%">
                                                    <tr class="tituloTablas"  align="left">
                                                        <td rowspan="2" class="columna_contenido" width="4%" align="center">No</td>
                                                        <td rowspan="2" class="columna_contenido" width="12%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo barras" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=0'});">C&oacute;digo barras</a></td>
                                                        <td rowspan="2" class="columna_contenido" width="35%" align="center"><a class="linkBlanco9" title="Ordenar por descripci&oacute;n del art&iacute;culo" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=1'});">Art&iacute;culo</a></td>
                                                        <td colspan="3" class="columna_contenido fila_contenido columna_contenido_der" width="23%" align="center">Totales</td>
                                                    </tr>
                                                    <tr class="tituloTablas"  align="left">
                                                        <td class="columna_contenido" width="7%" align="center"><a class="linkBlanco9" title="Ordenar por total reservado" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=2'});">Solicitado</a></td>
                                                        <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por centidad producida" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=3'});">Despachado</a></td>
                                                        <td class="columna_contenido columna_contenido_der" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por cantidad pendiente" href="#" onclick="requestAjax('actualizarDespacho.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=4'});">Pendiente</a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
                                                    <table border="0" cellspacing="0" cellpadding="2" width="100%">
                                                        <logic:iterate name="controlProduccionForm" property="datos" id="vistaArticuloDTO" indexId="indiceArticulo">
                                                            <bean:define id="indiceGlobal" value="${indiceArticulo + controlProduccionForm.start}"/>
                                                            <bean:define id="numFila" value="${indiceGlobal + 1}"/>
                                                            <%--------- control del estilo para el color de las filas --------------%>
                                                            <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="clase" value="blanco10"/>
                                                            </logic:equal>
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="clase" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%--------------------------------------------------------------------%>
                                                            <tr class="${clase}"> 
                                                                <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila"/></td>
                                                                <td class="columna_contenido fila_contenido" width="12%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                                                                <td class="columna_contenido fila_contenido" width="35%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                                                                <td class="columna_contenido fila_contenido" width="7%" align="right"><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></td>
                                                                <td class="columna_contenido fila_contenido" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="cantidadParcialEstado"/></td>
                                                                <td class="columna_contenido fila_contenido columna_contenido_der" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></td>
                                                            </tr>
                                                        </logic:iterate>
                                                    </table>
                                                </div>
                                            </td>
                                        </tr>
                                    </logic:notEmpty>
                                </logic:notEmpty>
                                <logic:empty name="controlProduccionForm" property="datos">
                                    <tr>
                                        <td>Seleccione un criterio de b&uacute;squeda</td>
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