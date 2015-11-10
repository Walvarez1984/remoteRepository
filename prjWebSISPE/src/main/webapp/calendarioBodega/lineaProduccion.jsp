<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vDia" name="vtDia"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vTipo" name="vtTipo"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vClasificacion" name="vtClasificacion"  classname="java.lang.String" ignore="true"/>

<table border="0" class="textoNegro11" width="100%" align="center" cellspacing="0" cellpadding="0" style="width:100%;">
    <logic:notEmpty name="ec.com.smx.sic.sispe.articulosProduccion.${vDia}.${vTipo}.${vClasificacion}">
		<tr>
			<td>
				<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<table border="0" cellspacing="0" width="100%" height="17px" cellpadding="1">
								<tr class="tituloTablasB">
									<td class="columna_contenido" align="center" width="5%">&nbsp;</td>
									<td class="columna_contenido" width="5%" align="center">No</td>
									<td class="columna_contenido" width="20%" align="center">C&Oacute;DIGO BARRAS</td>
									<td class="columna_contenido" width="40%" align="center">DESCRIPCI&Oacute;N</td>
									<td class="columna_contenido" width="10%" align="center">TOTAL INGRESOS</td>
									<td class="columna_contenido" width="10%" align="center">TOTAL EGRESOS</td>
									<td class="columna_contenido" width="10%" align="center">SALDO</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table border="0" cellspacing="0" width="100%" cellpadding="1">
								<logic:iterate name="ec.com.smx.sic.sispe.articulosProduccion.${vDia}.${vTipo}.${vClasificacion}" id="calendarioArticuloDTO" indexId="indiceCalArt">
									<bean:define id="fila" value="${indiceCalArt + 1}"/>
									<%-- control del estilo para el color de las filas --%>
									<bean:define id="residuo" value="${fila % 2}"/>
									<logic:equal name="residuo" value="0">
										<bean:define id="colorBack" value="blanco9B"/>
									</logic:equal>
									<logic:notEqual name="residuo" value="0">
										<bean:define id="colorBack" value="grisClaro9B"/>
									</logic:notEqual>
									<logic:equal name="calendarioArticuloDTO" property="esPendiente" value="1">
                          				<bean:define id="colorBack" value="rojoClaro10B"/>
                                   	</logic:equal>
									<tr class="${colorBack}">
										<td class="columna_contenido fila_contenido" width="5%" align="center">
											<c:set var="despliegueA1" value="block"/>
  											<c:set var="despliegueA2" value="none"/>
   											<div style="display:${despliegueA1}" id="desplegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}">
					                           <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}']);show(['plegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}','listado_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}']);"></a>
					                        </div>
					                        <div style="display:${despliegueA2}" id="plegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}">
					                           <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}','listado_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}']);show(['desplegar_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}']);"></a>
					                        </div>   
                                       	</td>
										<td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="fila"/></td>
										<td class="columna_contenido fila_contenido" width="20%" align="center"><a href="#" onclick="mostrarDetalleCanastaDirecto('${calendarioArticuloDTO.id.codigoArticulo}', null,null);"><label class="textoNegro9"><bean:write name="calendarioArticuloDTO" property="id.codigoArticulo"/></label></a></td>
										<td class="columna_contenido fila_contenido" width="40%" align="center"><bean:write name="calendarioArticuloDTO" property="descripcionArticulo"/></td>
										<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="calendarioArticuloDTO" property="npTotalIngresos"/></td>
										<td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="calendarioArticuloDTO" property="npTotalEgresos"/></td>	
										<c:set var="saldo" value="${calendarioArticuloDTO.npTotalIngresos - calendarioArticuloDTO.npTotalEgresos}"/>
										<c:set var="valor" value="${0}"/>
										<%--<bean:define id="color" value="textoNegro11"/>--%>
										<bean:define id="color" value="textoNegro9B"/>
									    <c:if test="${saldo > valor}">
									   		<%--<bean:define id="color" value="textoCantidadAdicional"/>--%>
									   		<bean:define id="color" value="textoCantidadAdicionalV1"/>
									    </c:if>
									    <c:if test="${saldo < valor}">
									   		<%--<bean:define id="color" value="textoCantidadPendiente"/>--%>
									   		<bean:define id="color" value="textoCantidadPendienteV1"/>
									   		<bean:define id="saldo" value="${saldo * (-1)}"/>
									    </c:if>
										<td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center">
											<label class="${color}"><bean:write name="saldo"/></label>
										</td> 
									</tr>
									<tr>
										<td colspan="7" id="listado_produccion_${vDia}_${vTipo}_${vClasificacion}_${indiceCalArt}" align="center" style="display:${despliegueA2}">
											<logic:notEmpty name="calendarioArticuloDTO" property="colCalendarioArticulo">
												<table width="95%" border="0" cellpadding="1" cellspacing="0" class="tabla_informacion_negro">
													<tr class="tituloTablasCelesteB">
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">No</td>
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">MOTIVO MOV.</td>
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">CANTIDAD</td>
														<td class="fila_contenido_negro columna_contenido_der_negro" align="center">OBSERVACION</td>
													</tr>
													<logic:iterate name="calendarioArticuloDTO" property="colCalendarioArticulo" id="calendarioArticuloDTO2" indexId="indiceCalArt1">
														<%-- control del estilo para el color de las filas --%>
														<bean:define id="fila1" value="${indiceCalArt1 + 1}"/>
														<bean:define id="residuoR" value="${indiceCalArt1 % 2}"/>
														<logic:equal name="residuoR" value="0">
															<bean:define id="colorBack2" value="blanco9B"/>
														</logic:equal>
														<logic:notEqual name="residuoR" value="0">
															<bean:define id="colorBack2" value="amarilloClaro9B"/>
														</logic:notEqual>
													 	<logic:equal name="calendarioArticuloDTO2" property="esPendiente" value="1">
                                                   			<bean:define id="colorBack2" value="rojoClaro10B"/>
                                                    	</logic:equal>
														<tr class="${colorBack2}">
															<td align="center" class="columna_contenido fila_contenido"><bean:write name="fila1"/></td>
															<td align="center" class="columna_contenido fila_contenido">
																<bean:write name="calendarioArticuloDTO2" property="descripcionMotMov"/>
																<logic:equal name="calendarioArticuloDTO2" property="esPendiente" value="1">
																	&nbsp;<bean:write name="calendarioArticuloDTO2" property="fechaCalendario" formatKey="formatos.fecha"/>
																</logic:equal>
															</td>
															<td align="right" class="columna_contenido fila_contenido"><bean:write name="calendarioArticuloDTO2" property="cantidad"/></td>
															<td align="right" class="columna_contenido fila_contenido">&nbsp;<bean:write name="calendarioArticuloDTO2" property="observacion"/></td>
														</tr>
													</logic:iterate>
												</table>
											</logic:notEmpty>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</td>
					</tr>
					<tr>
						<td  height="5px"></td>
					</tr>
				</table>
			</td>
		</tr>
	</logic:notEmpty>
    <logic:empty name="ec.com.smx.sic.sispe.articulosProduccion.${vDia}.${vTipo}.${vClasificacion}">
    	<tr>
    		<td width="80%" height="40px" align="center" style="vertical-align: middle;">
    			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
    				<tr>
    					<td width="100%" height="100%" class="tabla_informacion amarillo1">
    						NO TIENE PRODUCCION PENDIENTE
    					</td>
    				</tr>
    			</table>
    		</td>
    	</tr>
    </logic:empty>
</table>