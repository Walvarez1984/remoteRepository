<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:insert page="/include/top.jsp"/>

<html:form action="reporteEntregas" method="post">
	<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<%--<html:form action="estadoPedido" method="post" focus="campoBusqueda">--%>
		<input type="hidden" name="ayuda" value="">
		<%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
		<!-- Sección de pop-up de agrupaciójn de reportes de entregas --> 
		<tr>
	        <td>
	            <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
	                <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
	                <script language="javascript">mostrarModal();</script>	                
	            </logic:notEmpty>	            
	        </td>
    	</tr>
    	<bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
		<tr>
			<td align="left" valign="top" width="100%">
				<table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
					<tr>
						<td width="3%" align="center"><img src="./images/entregaPedido.gif" border="0"></img></td>
						<td height="35" valign="middle">Reporte de entregas pendientes</td>
						<td align="right" valign="top">
							<table border="0">
								<tr>
									<td>									
										<div id="botonA">
	                                   		<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
	                                   		<%--<html:link href="#" styleClass="excelA" onclick="requestAjax('reporteEntregas.do', ['mensajes'], {parameters: 'reporteExcel=ok',popWait:true});">Crear XLS</html:link>--%>
	                                   	</div>
	                              	</td>
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
		<%--Cuerpo--%>
		<tr>
			<td align="center" valign="top">
				<table border="0" class="textoNegro12" width="99%" align="center" cellspacing="0" cellpadding="0">
					<tr><td height="7px" colspan="3"></td></tr>
					<tr>
						<%--Barra Izquierda--%>
						<td class="datos fila_contenido columna_contenido" id="izquierda" align="center" width="25%">
							<div style="width:100%;height:490px;">
								<tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
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
						<td class="datos fila_contenido columna_contenido_der" width="100%" id="derecha">
							<div style="width:100%;height:490px;">
								<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
									<%--Titulo de los datos--%>
									<tr>
										<td class="fila_titulo">
											<table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
												<tr>
													<td><img src="./images/entregar24.gif" border="0"/></td>
													<td height="23" width="100%">&nbsp;
													Lista de resultados	
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<div id="resultadosBusqueda">
												 <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
						                                <logic:notEmpty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol">                                                                                          	
							                                    <tr>
							                                        <td colspan="6">
							                                            <table cellpadding="0" cellspacing="0" width="100%">
							                                                <tr>							                                                    
							                                                    <td align="right" id="pag">
							                                                        <smx:paginacion start="${listadoPedidosForm.start}" range="${listadoPedidosForm.range}" results="${listadoPedidosForm.size}" styleClass="textoNegro10" url="reporteEntregas.do" requestAjax="'mensajes','pag','div_listado'"/>
							                                                    </td>
							                                                </tr>
							                                            </table>
							                                        </td>
							                                    </tr>					                                   
						                                    	<tr>
						                                            <td>
						                                                <table border="0" cellspacing="0" cellpadding="1" width="100%">
						                                                    <tr class="tituloTablas"  align="left">
						                                                        <td class="columna_contenido" width="4%" align="center"></td>
						                                                        <td class="columna_contenido" width="4%" align="center">No.</td>
						                                                        <td class="columna_contenido" width="20%" align="center">C&oacute;digo barras</td>
						                                                        <td class="columna_contenido" width="52%" align="center">Ref. art.</td>
						                                                        <td class="columna_contenido" width="10%" align="center">Cantidad</td>
						                                                        <td class="columna_contenido" width="10%" align="center">Bultos</td>
						                                                    </tr>
						                                                </table>
						                                            </td>
						                                        </tr>
						                                        <tr>
						                                            <td>
						                                                <div id="div_listado" style="width:100%;height:370px;overflow:auto;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#cccccc">
						                                                    <table border="0" cellspacing="0" cellpadding="1" width="100%">
						                                                    	<!-- Inicio Iteración nivel 1 ---------------------------------------------------------------------------------------- -->                                                                   
						                                                        <logic:iterate name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol" id="vistaArticuloDTO" indexId="indiceArticulo">
						                                                            <bean:define id="indiceGlobal" value="${indiceArticulo + listadoPedidosForm.start}"/>
						                                                            <bean:define id="numFila" value="${indiceGlobal + 1}"/>
						                                                            
						                                                            <bean:define id="residuo" value="${indiceArticulo % 2}"/>
						                                                            <logic:equal name="residuo" value="0">
						                                                                <bean:define id="clase" value="blanco10"/>
						                                                            </logic:equal>
						                                                            <logic:notEqual name="residuo" value="0">
						                                                                <bean:define id="clase" value="grisClaro10"/>
						                                                            </logic:notEqual>
						                                                            <tr class="${clase}"> 
						                                                                <td class="columna_contenido fila_contenido" width="4%" align="center">
						                                                                    <c:set var="despliegueA1" value="block"/>
						                                                                    <c:set var="despliegueA2" value="none"/>
						                                                                    
						                                                                   <!--  Se contrala que se mantenga activo el índice elegido previo a la visualización de un detalle de pedido -->
						                                                                   <logic:notEmpty name="ec.com.smx.sic.sispe.primerIndiceVRG">
						                                                                   		<bean:define id="indiceActivoVRG" name="ec.com.smx.sic.sispe.primerIndiceVRG"></bean:define>
						                                                                   		<logic:equal name="indiceActivoVRG" value="${indiceArticulo}">
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
						                                                                <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>        
						                                                              	<td class="columna_contenido fila_contenido" width="52%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>  
						                                                              	<td class="columna_contenido fila_contenido" width="10%" align="left"><bean:write name="vistaArticuloDTO" property="cantidadDespacharTotal"/></td> 
						                                                              	<td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="left"><bean:write name="vistaArticuloDTO" property="cantidadBultos"/></td>
						                                                            </tr>
						                                                            <tr>
						                                                                <td colspan="10" id="listado_${indiceArticulo}" align="center" style="display:${despliegueA2}">
						                                                                    <!-- se muestra el detalle de los locales -->
						                                                                    <table cellpadding="1" cellspacing="0" width="90%" class="tabla_informacion_negro">
						                                                                        <tr class="tituloTablasCeleste">
						                                                                           	<td class="columna_contenido" width="4%" align="center"></td>
						                                                                           	<td class="columna_contenido" width="4%" align="center">No.</td>
						                                                        					<td class="columna_contenido" width="20%" align="center">Código ped.</td>
						                                                        					<td class="columna_contenido" width="23%" align="center">No. res</td>
						                                                        					<td class="columna_contenido" width="20%" align="center">Estado</td>
						                                                        					<td class="columna_contenido" width="20%" align="center">Cantidad</td>
						                                                        					<td class="columna_contenido" width="5%" align="center"></td>
						                                                        					<%-- %><td class="columna_contenido" width="24%" align="center">Bultos</td>  --%>
						                                                                        </tr>
						                                                                        <logic:notEmpty name="vistaArticuloDTO" property="detallesReporte">
						                                                                            <!-- Inicio Iteración nivel 2 ---------------------------------------------------------------------------------------- -->                                                                   
							                                                                        <logic:iterate name="vistaArticuloDTO" property="detallesReporte" id="vistaArticuloDTO2" indexId="indiceArticulo2">
							                                                                          
							                                                                            <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
							                                                                            <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
							                                                                            <logic:equal name="residuo2" value="0">
							                                                                                <bean:define id="clase2" value="blanco10"/>
							                                                                            </logic:equal>
							                                                                            <logic:notEqual name="residuo2" value="0">
							                                                                                <bean:define id="clase2" value="amarilloClaro10"/>
							                                                                            </logic:notEqual>
							                                                                                                                                                                                                                                  
							                                                                            <tr class="${clase2}">
							                                                                               <td class="columna_contenido fila_contenido" width="4%" align="center">
							                                                                                    <c:set var="despliegueL1" value="block"/>
						                                                                    				    <c:set var="despliegueL2" value="none"/>
						                                                                    
											                                                                    <!--  Se contrala que se mantenga activo el índice elegido previo a la visualización de un detalle de pedido -->
											                                                                    <logic:notEmpty name="ec.com.smx.sic.sispe.primerIndiceVRG">
											                                                                   		<bean:define id="indiceActivoVRG" name="ec.com.smx.sic.sispe.primerIndiceVRG"></bean:define>
											                                                                   		<logic:equal name="indiceActivoVRG" value="${indiceArticulo2}">
											                                                                   		    <c:set var="despliegueL1" value="none"/>
											                                                                            <c:set var="despliegueL2" value="block"/>
											                                                                        </logic:equal>
											                                                                    </logic:notEmpty>
												                                                                   
								                                                                                <div style="display:${despliegueL1}" id="desplegar_${indiceArticulo}_${indiceArticulo2}">
							                                                                        				<a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}_${indiceArticulo2}']);show(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);"></a>
							                                                                    				</div>
							                                                                    				<div style="display:${despliegueL2}" id="plegar_${indiceArticulo}_${indiceArticulo2}">
							                                                                        				<a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);show(['desplegar_${indiceArticulo}_${indiceArticulo2}']);"></a>
							                                                                    				</div>
						                                                                    			   </td>						                                                                   
							                                                                               <td class="columna_contenido fila_contenido" width="4%" align="center"><bean:write name="numFila2"/></td>
							                                                                			   <td class="columna_contenido fila_contenido" width="20%" align="left">
							                                                                			   		<bean:define id="indices" value="${indiceArticulo}_${indiceArticulo2}"></bean:define>
							                                                                			   		<html:link title="Ver Detalles" action="reporteEntregas" paramId="indiceVRG" paramName="indices"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link>												
							                                                                			   </td>
							                                                              	  			   <td class="columna_contenido fila_contenido" width="23%" align="left"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>  
							                                                              	  			   <td class="columna_contenido fila_contenido" width="20%" align="left">							                                                              	  			   								                                                              	  			   
							                                                              	  			   		<bean:write name="vistaArticuloDTO2" property="descripcionEstado"/>
							                                                              	  			   </td>  
							                                                              				   <td class="columna_contenido fila_contenido" width="20%" align="left"><bean:write name="vistaArticuloDTO2" property="cantidadDespacharTotal"/></td> 
							                                                              			       <td class="columna_contenido fila_contenido" width="5%" align="center">
						                                                              			        	 <%--bean:define id="bodega" value="ec.com.smx.sic.sispe.entidadResponsable.bodega"></bean:define--%>
						                                                              			        	 <bean:define id="estado" value="${vistaArticuloDTO2.descripcionEstado}"/>
						                                                              			        	 <bean:define id="valor" value="${vistaArticuloDTO.entidadResponsable}"/>
						                                                              			        	 <logic:notEmpty name="vistaArticuloDTO2" property="fechaRegistroDespacho">
							                                                              			        	 <logic:equal name="estado"  value="Despachado">
							                                                              			        		<c:if test="${valor =='BOD'}"> 
							                                                              			        		 <a href="#" onclick="requestAjax('entregaPedido.do', ['div_pagina','mensajes'], {parameters: 'accionLink=${vistaArticuloDTO2.id.codigoPedido}',evalScripts: true});"><img src="./images/entregar16.gif" border="0" alt="Ir a entrega pedido"></a>
							                                                              			        	    </c:if>
							                                                              			        	    <c:if test="${valor =='LOC'}"> 
							                                                              			        		 <a href="#" onclick="requestAjax('entregaPedido.do', ['div_pagina','mensajes'], {parameters: 'accionLink=${vistaArticuloDTO2.id.codigoPedido}',evalScripts: true});"><img src="./images/entregar16.gif" border="0" alt="Ir a entrega pedido"></a>
							                                                              			        	    </c:if>
							                                                              			        	 </logic:equal>
							                                                              			         </logic:notEmpty>
						                                                              			        	 <logic:notEqual name="estado" value="Despachado">-</logic:notEqual> 
							                                                              			       </td> 
							                                                              			       <%-- %><td class="columna_contenido fila_contenido" width="24%" align="left"><bean:write name="vistaArticuloDTO2" property="cantidadBultos"/></td>  --%>                                                                                                                                                                                                                                           
							                                                                            </tr>
							                                                                            <tr>
							                                                                            	<td colspan="10" id="listado_${indiceArticulo}_${indiceArticulo2}" align="center" style="display:${despliegueL2}">
											                                                                    <!-- se muestra el detalle de los locales -->
											                                                                    <table cellpadding="1" cellspacing="0" width="90%" class="tabla_informacion_negro">
											                                                                        <tr class="tituloTablasCeleste">									                                                                           
											                                                        					<td class="columna_contenido" width="33%" align="center">Dir. entrega</td>
											                                                        					<td class="columna_contenido" width="33%" align="center">Fecha entrega</td>
											                                                        					<td class="columna_contenido" width="34%" align="center">Cantidad entrega</td>
											                                                                        </tr>
											                                                                        <logic:notEmpty name="vistaArticuloDTO2" property="detallesReporte">
											                                                                            <!-- Inicio Iteración nivel 3 ---------------------------------------------------------------------------------------- -->                                                                   
												                                                                        <logic:iterate name="vistaArticuloDTO2" property="detallesReporte" id="vistaArticuloDTO3" indexId="indiceArticulo3">
												                                                                          
												                                                                            <bean:define id="residuo3" value="${indiceArticulo3 % 2}"/>
												                                                                            <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
												                                                                            <logic:equal name="residuo3" value="0">
												                                                                                <bean:define id="clase3" value="blanco10"/>
												                                                                            </logic:equal>
												                                                                            <logic:notEqual name="residuo3" value="0">
												                                                                                <bean:define id="clase3" value="amarilloClaro10"/>
												                                                                            </logic:notEqual>
												                                                                                                                                                                                                                                  
												                                                                            <tr class="${clase3}">												                                                                               												                                                                                 												                                                                            											                                                                    												                                                                               												                                                                			  
												                                                              	  			   <td class="columna_contenido fila_contenido" width="33%" align="left"><bean:write name="vistaArticuloDTO3" property="direccionEntrega"/></td>  
												                                                              				   <td class="columna_contenido fila_contenido" width="33%" align="left"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td> 
												                                                              			       <td class="columna_contenido fila_contenido" width="34%" align="left"><bean:write name="vistaArticuloDTO3" property="cantidadEntrega"/></td>
												                                                                            </tr>												                                                                            												                                                                            
												                                                                        </logic:iterate>
												                                                                        <!-- Fin Iteración nivel 3 ---------------------------------------------------------------------------------------- -->                                                                   
											                                                                        </logic:notEmpty>
											                                                                    </table>
						                                                                					</td>
							                                                                            </tr>							                                                                            
							                                                                        </logic:iterate>
							                                                                        <!-- Fin Iteración nivel 2 ---------------------------------------------------------------------------------------- -->                                                                   
						                                                                        </logic:notEmpty>
						                                                                    </table>
						                                                                </td>
						                                                            </tr>
						                                                        </logic:iterate>
						                                                        <!-- Fin Iteración nivel 1 ---------------------------------------------------------------------------------------- -->                                                                   
						                                                    </table>
						                                                </div>
						                                            </td>
						                                        </tr>                                   
						                                 </logic:notEmpty>   
						                                 <logic:empty name="ec.com.smx.sic.sispe.vistaReporteGeneralDTOCol"> 
                                 							No hay datos
                                 						 </logic:empty>                                                       
                           				    	</table>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</td>	
						<script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
					</tr>
				</table>
			</td>
		</tr>
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>