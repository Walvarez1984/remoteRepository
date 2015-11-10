<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:insert page="/include/top.jsp"/>
<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%">
<html:form action="lineaProduccion" method="post" enctype="multipart/form-data">
	<html:hidden property="ayuda" value=""/>
	<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
		<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
	</logic:notEmpty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
		<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
	</logic:notEmpty>
    <bean:define id="articuloEspecial"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
	<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>
	<tr>
		<td>
			<div id="pregunta">
				<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
					<jsp:include page="../confirmacion/confirmacion.jsp"/>
					<script language="javascript">mostrarModal();</script>
				</logic:notEmpty>
				<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
					<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
						<tiles:put name="vtformAction" value="lineaProduccion"/>
					</tiles:insert>
					<script language="javascript">mostrarModal();</script>
				</logic:notEmpty>
			</div>
		</td>
	</tr>
	<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
	<tr>
		<td align="left" valign="top" width="100%">
			<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
				<tr>
					<td width="3%" align="center"><img src="./images/lineaProduccion.gif" border="0"></img></td>
					<td height="35" valign="middle">Administraci&oacute;n de L&iacute;nea de Producci&oacute;n</td>
					<td align="right" valign="top">
						<table border="0" cellpadding="0" cellspacing="1">
							<tr>
								<logic:notEmpty name="ec.com.smx.sic.sispe.usuarioAccesoLineaPro">
									<td>
										<div id="botonA">
											<html:link styleClass="nuevoA" href="#" onclick="realizarEnvio('nuevaLinPro');" title="nueva línea producción">Nuevo</html:link>
										</div>
									</td>
								</logic:notEmpty>	
								<td>
									<bean:define id="exit" value="exit"/>
									<div id="botonA">
										<html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%--Cuerpo1--%>
	<logic:notEmpty name="ec.com.smx.sic.sispe.usuarioAccesoLineaPro">
	 <tr>
		<td align="center" valign="top">
			<table border="0" class="textoNegro12" width="99%" align="center" cellspacing="0" cellpadding="0">
				<tr><td height="7px" colspan="3"></td></tr>
				<tr>
					<%--Barra Izquierda--%>
					<td class="datos fila_contenido columna_contenido" id="izquierda" align="center" width="25%">
						<div style="width:100%;height:490px;">
							<tiles:insert page="/calendarioBodega/lineaProduccion/seccionBusqueda.jsp"/>
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
					<%-- Datos --%>
					<td class="datos fila_contenido columna_contenido_der" width="100%" id="derecha">
						<div style="width:100%;height:490px;">
							<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
								<%--Titulo de los datos--%>
								<tr>
									<td class="fila_titulo">
										<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
											<tr>
												<td><img src="./images/detalle_pedidos24.gif" border="0"/></td>
												<td height="23" width="100%">&nbsp;
													Lista L&iacute;nea Producci&oacute;n
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<%--Datos de pedidos--%>								
								<logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteCalArtDTOColPaginado">
								<tr>
									<td>
										<div id="resultadosBusqueda">
											<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
												<logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteCalArtDTOColPaginado">	
													<tr>
														<td>
															<table cellpadding="0" cellspacing="0" width="100%">
																<tr>
																	<td align="right" id="seg_paginacion">
																		<smx:paginacion start="${lineaProduccionForm.start}" range="${lineaProduccionForm.range}" results="${lineaProduccionForm.size}" styleClass="textoNegro11" url="lineaProduccion.do"  requestAjax="'seg_paginacion','div_listado'"/>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec">
													 <tr>
														<td>
															<table border="0" cellspacing="0" width="100%" height="17px" cellpadding="1">
																<tr class="tituloTablas">
																	<td class="columna_contenido" align="center" width="4%">&nbsp;</td>
																	<td class="columna_contenido" width="4%" align="center">No</td>
																	<td class="columna_contenido" width="17%" align="center">C&Oacute;DIGO BARRAS</td>
																	<td class="columna_contenido" width="25%" align="center">DESCRIPCI&Oacute;N</td>
																	<td class="columna_contenido" width="15%" align="center">TOTAL INGRESOS</td>
																	<td class="columna_contenido" width="15%" align="center">TOTAL EGRESOS</td>
																	<td class="columna_contenido" width="10%" align="center">SALDO</td>
																	<td class="columna_contenido" width="10%" align="center">%AVANCE</td>
																</tr>
															</table>
														</td>
													 </tr>
													</logic:notEmpty>
													<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt">
													 <tr>
														<td>
															<table border="0" cellspacing="0" width="100%" height="17px" cellpadding="1">
																<tr class="tituloTablas">
																	<td class="columna_contenido" align="center" width="4%">&nbsp;</td>
																	<td class="columna_contenido" width="6%" align="center">No</td>
																	<td class="columna_contenido" width="90%" align="center">FECHA DE LÍNEA PRODUCCIÓN</td>
																</tr>
															</table>
														</td>
													 </tr>
													</logic:notEmpty>
													<tr>
														<td>
															<div id="div_listado" style="width:100%;height:420px;overflow:auto">
																<table border="0" cellspacing="0" width="100%" cellpadding="1">
																	<logic:iterate name="ec.com.smx.sic.sispe.vistaReporteCalArtDTOColPaginado"  id="calendarioArticuloDTO" indexId="indiceCalArt">
																		<bean:define id="fila" value="${indiceCalArt + 1}"/>
																		<%-- control del estilo para el color de las filas --%>
																		<bean:define id="residuo" value="${fila % 2}"/>
																		<logic:equal name="residuo" value="0">
																			<bean:define id="colorBack" value="blanco10"/>
																		</logic:equal>
																		<logic:notEqual name="residuo" value="0">
																			<bean:define id="colorBack" value="grisClaro10"/>
																		</logic:notEqual>
																		
																			<bean:define id="codigoSubClasificacion" name="calendarioArticuloDTO" property="codigoSubClasificacion"/>
								                                            <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
																			  	<bean:define id="colorTexto1" value="textoNaranja10"/>
																			  	<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																				<bean:define id="descripcion" value="detalle de la despensa"/>
																			</c:if>
																			<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion)}">
																			  	<bean:define id="colorTexto1" value="textoNegro10"/>
																			  	<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
																				<bean:define id="descripcion" value="detalle del canasto"/>
																			</c:if>
																		
																		<%--<c:if test="${calendarioArticuloDTO.codigoTipoArticulo == tipoDespensa}">
																	   	  <bean:define id="colorTexto1" value="textoNaranja10"/>
																	    </c:if>
																	    <c:if test="${calendarioArticuloDTO.codigoTipoArticulo == tipoCanasto}">
																	   	  <bean:define id="colorTexto1" value="textoNegro10"/>
																	    </c:if> --%>
																		<tr class="${colorBack}">
																			<td class="columna_contenido fila_contenido" width="4%" align="center">
																				<c:set var="despliegueA1" value="block"/>
		                            											<c:set var="despliegueA2" value="none"/>
		                            											<div style="display:${despliegueA1}" id="desplegar_produccion_${indiceCalArt}">
														                           <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceCalArt}']);show(['plegar_produccion_${indiceCalArt}','listado_produccion_${indiceCalArt}']);"></a>
														                        </div>
														                        <div style="display:${despliegueA2}" id="plegar_produccion_${indiceCalArt}">
														                           <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceCalArt}','listado_produccion_${indiceCalArt}']);show(['desplegar_produccion_${indiceCalArt}']);"></a>
														                        </div>   
			                                                                </td>
			                                                               <logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec">																			 
																			 <c:set var="totalIngresos" value="${0}"/>
																			 <c:set var="totalEgresos" value="${0}"/>
																			 <logic:iterate name="calendarioArticuloDTO" property="colCalendarioArticulo" id="calendarioArticuloCalculo" indexId="indiceCalArtO">
																			 	 <c:set var="totalIngresos" value="${totalIngresos + calendarioArticuloCalculo.npTotalIngresos}"/>
																			 	 <c:set var="totalEgresos" value="${totalEgresos + calendarioArticuloCalculo.npTotalEgresos}"/>
																			 </logic:iterate>
																			 <c:set var="totalSaldo" value="${totalIngresos - totalEgresos}"/>
																			 <c:set var="valor1" value="${0}"/>
																			 
																			 <c:if test="${totalSaldo < valor1}">
																		      <bean:define id="colorNivel1" value="textoRojoNuevo"/>
																		      <c:set var="saldoNivel1" value="${(totalIngresos - totalEgresos)*-1}"/>
																		     </c:if>
																		     <c:if test="${totalSaldo > valor1}">
																		   	  <bean:define id="colorNivel1" value="textoAzul10"/>
																		   	  <c:set var="saldoNivel1" value="${(totalIngresos - totalEgresos)}"/>
																		     </c:if>
																		     <c:if test="${totalSaldo == valor1}">
																		   	  <bean:define id="colorNivel1" value="textoNegro10"/>
																		   	  <c:set var="saldoNivel1" value="${(totalIngresos - totalEgresos)}"/>
																		     </c:if>
																			 <c:set var="porcentajeAvance" value="${(totalIngresos*100)/totalEgresos}"/>
																			 <td class="columna_contenido fila_contenido" width="4%" align="center"><label class="${colorTexto1}"><bean:write name="fila"/></label></td>
																			 <td class="columna_contenido fila_contenido" width="17%" align="center">
                                                        			        	<a href="#" onclick="requestAjax('lineaProduccion.do', ['mensajes','pregunta'], {parameters: 'indiceDetalle=${calendarioArticuloDTO.id.codigoArticulo}',evalScripts: true});"><label class="${colorTexto1}"><bean:write name="calendarioArticuloDTO" property="codigoBarras"/></label></a>
                                                        			         </td>
																			 <td class="columna_contenido fila_contenido" width="25%" align="left">																			 	
																				<table border="0" width="100%" cellpadding="1">
																				  <tr>
																					 <td>
																						<%--<logic:equal name="calendarioArticuloDTO" property="codigoTipoArticulo" value="${tipoDespensa}">
																							<bean:define id="pathImagenArticulo" value="images/despensa_llena.gif"/>
																							<bean:define id="descripcion" value="detalle de la despensa"/>
																						</logic:equal>
																						<logic:equal name="calendarioArticuloDTO" property="codigoTipoArticulo" value="${tipoCanasto}">
																							<bean:define id="pathImagenArticulo" value="images/canasto_lleno.gif"/>
																							<bean:define id="descripcion" value="detalle del canasto"/>
																						</logic:equal> --%>
																						<label class="${colorTexto1}"><bean:write name="calendarioArticuloDTO" property="descripcionArticulo"/></label>
																					 </td>
																					 <td align="right">
																						<img title="${descripcion}" src="${pathImagenArticulo}" border="0"/>
																					 </td>
																				  </tr>
																				</table>	
																			 </td>	
																		     <td class="columna_contenido fila_contenido" width="15%" align="center"><label class="${colorTexto1}"><b><bean:write name="totalIngresos"/></b></label></td>	
																		   	 <td class="columna_contenido fila_contenido" width="15%" align="center"><label class="${colorTexto1}"><b><bean:write name="totalEgresos"/></b></label></td>	
	     																   	 <td class="columna_contenido fila_contenido" width="10%" align="center"><label class="${colorNivel1}"><b><bean:write name="saldoNivel1"/></b></label></td>	
																		     <td class="columna_contenido fila_contenido" width="10%" align="center"><label class="${colorTexto1}"><b><bean:write name="porcentajeAvance" formatKey="formatos.numeros.decimales"/>%</b></label></td>
																		   </logic:notEmpty>
																		   <logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt">
																			<td class="columna_contenido fila_contenido" width="6%" align="center"><label class="textoNegro10"><bean:write name="fila"/></label></td>
																			<td class="columna_contenido fila_contenido" width="90%" align="center"><label class="textoNegro10"><bean:write name="calendarioArticuloDTO" property="fechaCalendario" formatKey="formatos.fecha"/></label></td>
																		   </logic:notEmpty>
																		</tr>
																		<tr>
																			<td colspan="10" id="listado_produccion_${indiceCalArt}" align="center" style="display:${despliegueA2}">
																				<logic:notEmpty name="calendarioArticuloDTO" property="colCalendarioArticulo">
																					<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																					    <logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec">
																						 <tr class="tituloTablasCeleste">
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%">&nbsp;</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">FECHA</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">TOTAL INGRESOS</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">TOTAL EGRESOS</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">SALDO</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" width="4%" align="center">&nbsp;</td>
																						 </tr>
																						</logic:notEmpty>
																						<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt">
																						 <tr class="tituloTablasCeleste">
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center" width="4%">&nbsp;</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">C&Oacute;DIGO BARRAS</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">DESCRIPCI&Oacute;N</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">TOTAL INGRESOS</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">TOTAL EGRESOS</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" align="center">SALDO</td>
																							<td class="fila_contenido_negro columna_contenido_der_negro" width="4%" align="center">&nbsp;</td>
																						 </tr>
																						</logic:notEmpty>
																						<logic:iterate name="calendarioArticuloDTO" property="colCalendarioArticulo" id="calendarioArticuloDTO2" indexId="indiceCalArt1">
																							<%-- control del estilo para el color de las filas --%>
																							<bean:define id="fila1" value="${indiceCalArt1 + 1}"/>
																							<bean:define id="residuoR" value="${indiceCalArt1 % 2}"/>
																							<logic:equal name="residuoR" value="0">
																								<bean:define id="colorBack2" value="blanco10"/>
																							</logic:equal>
																							<logic:notEqual name="residuoR" value="0">
																								<bean:define id="colorBack2" value="amarilloClaro10"/>
																							</logic:notEqual>
																							
																								<bean:define id="codigoSubClasificacion1" name="calendarioArticuloDTO2" property="codigoSubClasificacion"/>
													                                            <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion1)}">
																								  	<bean:define id="colorTexto" value="textoNaranja10"/>
																								  	<bean:define id="pathImagenArticulo1" value="images/despensa_llena.gif"/>
																									<bean:define id="descripcion1" value="detalle de la despensa"/>
																								</c:if>
																								<c:if test="${fn:contains(tipoCanasto, codigoSubClasificacion1)}">
																								  	<bean:define id="colorTexto" value="textoNegro10"/>
																								  	<bean:define id="pathImagenArticulo1" value="images/canasto_lleno.gif"/>
																									<bean:define id="descripcion1" value="detalle del canasto"/>
																								</c:if>
																							
																							<%--<c:if test="${calendarioArticuloDTO2.codigoTipoArticulo == tipoDespensa}">
																						   	  <bean:define id="colorTexto" value="textoNaranja10"/>
																						    </c:if>
																						    <c:if test="${calendarioArticuloDTO2.codigoTipoArticulo == tipoCanasto}">
																						   	  <bean:define id="colorTexto" value="textoNegro10"/>
																						    </c:if>  --%>
																							<tr class="${colorBack2}">
																								<td class="columna_contenido fila_contenido" width="4%" align="center">
																									<c:set var="despliegueA3" value="block"/>
							                            											<c:set var="despliegueA4" value="none"/>
							                            											<div style="display:${despliegueA3}" id="desplegar_produccion_${indiceCalArt}_${indiceCalArt1}">
																			                           <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceCalArt}_${indiceCalArt1}']);show(['plegar_produccion_${indiceCalArt}_${indiceCalArt1}','listado_produccion_${indiceCalArt}_${indiceCalArt1}']);"></a>
																			                        </div>
																			                        <div style="display:${despliegueA4}" id="plegar_produccion_${indiceCalArt}_${indiceCalArt1}">
																			                           <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceCalArt}_${indiceCalArt1}','listado_produccion_${indiceCalArt}_${indiceCalArt1}']);show(['desplegar_produccion_${indiceCalArt}_${indiceCalArt1}']);"></a>
																			                        </div>   
								                                                                </td>
																							    <logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionArtFec">	
																								 <td align="center" class="columna_contenido fila_contenido"><label class="textoNegro10"><bean:write name="fila1"/></label></td>
																								 <td align="center" class="columna_contenido fila_contenido"><label class="textoNegro10"><bean:write name="calendarioArticuloDTO2" property="fechaCalendario" formatKey="formatos.fecha"/></label></td>
																								 <td align="right" class="columna_contenido fila_contenido"><label class="textoNegro10"><bean:write name="calendarioArticuloDTO2" property="npTotalIngresos"/></label></td>
																								 <td align="right" class="columna_contenido fila_contenido"><label class="textoNegro10"><bean:write name="calendarioArticuloDTO2" property="npTotalEgresos"/></label></td>
																								</logic:notEmpty>
																								<logic:notEmpty name="ec.com.smx.sic.sispe.tipoAgrupacionFecArt">	
																								 <td align="center" class="columna_contenido fila_contenido"><label class="${colorTexto}"><bean:write name="fila1"/></label></td>
																								 <td class="columna_contenido fila_contenido" align="center">
					                                                        			        	<a href="#" onclick="requestAjax('lineaProduccion.do', ['mensajes','pregunta'], {parameters: 'indiceDetalle=${calendarioArticuloDTO2.id.codigoArticulo}',evalScripts: true});"><label class="${colorTexto}"><bean:write name="calendarioArticuloDTO2" property="codigoBarras"/></label></a>
					                                                        			         </td>
																								 <td class="columna_contenido fila_contenido"  align="left">																			 	
																									<table border="0" width="100%" cellpadding="1">
																									  <tr>
																										 <td>
																											<%--<logic:equal name="calendarioArticuloDTO2" property="codigoTipoArticulo" value="${tipoDespensa}">
																												<bean:define id="pathImagenArticulo1" value="images/despensa_llena.gif"/>
																												<bean:define id="descripcion1" value="detalle de la despensa"/>
																											</logic:equal>
																											<logic:equal name="calendarioArticuloDTO2" property="codigoTipoArticulo" value="${tipoCanasto}">
																												<bean:define id="pathImagenArticulo1" value="images/canasto_lleno.gif"/>
																												<bean:define id="descripcion1" value="detalle del canasto"/>
																											</logic:equal> --%>
																											<label class="${colorTexto}"><bean:write name="calendarioArticuloDTO2" property="descripcionArticulo"/></label>
																										 </td>
																										 <td align="right">
																											<img title="${descripcion1}" src="${pathImagenArticulo1}" border="0"/>
																										 </td>
																									  </tr>
																									</table>	
																								 </td>
																								 <td align="right" class="columna_contenido fila_contenido"><label class="${colorTexto}"><bean:write name="calendarioArticuloDTO2" property="npTotalIngresos"/></label></td>
																								 <td align="right" class="columna_contenido fila_contenido"><label class="${colorTexto}"><bean:write name="calendarioArticuloDTO2" property="npTotalEgresos"/></label></td>
																								</logic:notEmpty>
																								<c:set var="saldo" value="${calendarioArticuloDTO2.npTotalIngresos - calendarioArticuloDTO2.npTotalEgresos}"/>
																								<c:set var="valor" value="${0}"/>
																							    <c:if test="${saldo < valor}">
																							   	 <bean:define id="colorNivel2" value="textoRojoNuevo"/>
																							   	 <c:set var="saldoNivel2" value="${(calendarioArticuloDTO2.npTotalIngresos - calendarioArticuloDTO2.npTotalEgresos)*-1}"/>
																							    </c:if>
																							    <c:if test="${saldo >= valor}">
																							   	 <bean:define id="colorNivel2" value="textoAzul10"/>
																							   	 <c:set var="saldoNivel2" value="${(calendarioArticuloDTO2.npTotalIngresos - calendarioArticuloDTO2.npTotalEgresos)}"/>
																							    </c:if>
																							    <c:if test="${saldo == valor}">
																							   	 <bean:define id="colorNivel2" value="textoNegro10"/>
																							   	 <c:set var="saldoNivel2" value="${(calendarioArticuloDTO2.npTotalIngresos - calendarioArticuloDTO2.npTotalEgresos)}"/>
																							    </c:if>
																								<td class="columna_contenido fila_contenido" align="center">
																									<label class="${colorNivel2}"><b><bean:write name="saldoNivel2"/></b></label>
																								</td> 
																								<td class="columna_contenido fila_contenido" width="4%" align="center">
					                                                        			        	<a href="#" onclick="requestAjax('lineaProduccion.do', ['div_pagina','mensajes'], {parameters: 'editarLinPro=${indiceCalArt1}&indice=${indiceCalArt}',evalScripts: true});"><img src="./images/editar16.gif" border="0" alt="editar línea producción"></a>
					                                                        			        </td>
																							</tr>
																							<tr>
																								<td colspan="10" id="listado_produccion_${indiceCalArt}_${indiceCalArt1}" align="center" style="display:${despliegueA4}">
																									<logic:notEmpty name="calendarioArticuloDTO" property="colCalendarioArticulo">
																										<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
																											<tr class="tituloTablasCeleste">
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Motivo Mov.</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Cantidad</td>
																												<td class="fila_contenido_negro columna_contenido_der_negro" align="center">Observación</td>
																											</tr>
																											<logic:iterate name="calendarioArticuloDTO2" property="colCalendarioArticulo" id="calendarioArticuloDTO3" indexId="indiceCalArt2">
																												<%-- control del estilo para el color de las filas --%>
																												<bean:define id="fila2" value="${indiceCalArt2 + 1}"/>
																												<bean:define id="residuoR" value="${indiceCalArt2 % 2}"/>
																												<logic:equal name="residuoR" value="0">
																													<bean:define id="colorBack3" value="blanco10"/>
																												</logic:equal>
																												<logic:notEqual name="residuoR" value="0">
																													<bean:define id="colorBack3" value="amarilloClaro10"/>
																												</logic:notEqual>
																												<tr class="${colorBack3} textoNegro10">
																													<td align="center" class="columna_contenido fila_contenido"><bean:write name="fila2"/></td>
																													<td align="center" class="columna_contenido fila_contenido"><bean:write name="calendarioArticuloDTO3" property="descripcionMotMov"/></td>
																													<td align="right" class="columna_contenido fila_contenido"><bean:write name="calendarioArticuloDTO3" property="cantidad"/></td>
																													<td align="right" class="columna_contenido fila_contenido">&nbsp;<bean:write name="calendarioArticuloDTO3" property="observacion"/></td>
																												</tr>
																											</logic:iterate>
																										</table>
																									</logic:notEmpty>
																								</td>
																							</tr>
																						</logic:iterate>
																					</table>
																				</logic:notEmpty>
																			</td>
																		</tr>
																	</logic:iterate>
																</table>
															</div>
														</td>
													</tr>
													<tr>
														<td  height="5px"></td>
													</tr>
											    </logic:notEmpty>
											    <logic:empty name="ec.com.smx.sic.sispe.vistaReporteCalArtDTOColPaginado">
													<tr>
														<td colspan="8" class="textoNegro11">Seleccione un criterio de b&uacute;squeda</td>
													</tr>
												</logic:empty>					
											</table>
										</div>
									</td>
								  </tr>
								</logic:notEmpty>
								<%--Fin datos de pedidos--%>
							</table>
						</div>
					</td>
					<%--Fin Datos--%>
					<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
				</tr>
			</table>
		</td>
	 </tr>
	</logic:notEmpty>
	<logic:empty name="ec.com.smx.sic.sispe.usuarioAccesoLineaPro">
		<tr>
			<td width="100%" height="18px"></td>
		</tr>
		<tr>
			<td width="100%" height="53px" class="tabla_informacion amarillo1" align="center">
				<b>NO TIENE LOS PERMISOS NECESARIOS PARA ACCEDER A ESTA OPCIÓN</b>
			</td>
		</tr>
    </logic:empty>	
</html:form>
</table>
<tiles:insert page="/include/bottom.jsp"/>