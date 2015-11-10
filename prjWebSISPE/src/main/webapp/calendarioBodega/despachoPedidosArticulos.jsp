<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vDia" name="vtDia"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vTipo" name="vtTipo"  classname="java.lang.String" ignore="true"/>
<bean:define id="tipoCanastaModificada"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasModificadas"/></bean:define>
<bean:define id="canastoCatalogo"><bean:message key="ec.com.smx.sic.sispe.codigoClasificacionCanastasCatalogo"/></bean:define>

<bean:define id="codigoTipArtDesp"><bean:message key="ec.com.smx.sic.sispe.tipoarticulo.despensa"/></bean:define>
<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.canasta">
		<bean:define id="tipoCanasto" name="ec.com.smx.sic.sispe.tipoArticulo.canasta"/>
	</logic:notEmpty>
	<logic:notEmpty name="ec.com.smx.sic.sispe.tipoArticulo.despensa">
		<bean:define id="tipoDespensa" name="ec.com.smx.sic.sispe.tipoArticulo.despensa"/>
	</logic:notEmpty>

<table border="0" class="textoNegro11" width="96%" align="center" cellspacing="0" cellpadding="0" style="position:absolute;top:2%;left:2%;">
    <logic:notEmpty name="ec.com.smx.sic.sispe.articulosDespachoLocal.${vDia}.${vTipo}">
        <%--<tr>
        	<td colspan="2">
        		<table border="0" cellspacing="0" cellpadding="0" width="100%">
        			<tr>
        				<td class="textoNegro10">
        					<b>INFO:&nbsp;</b>
        					<img src="./images/canasto_lleno.gif" border="0" title="tiene Canastas">&nbsp;Canasta&nbsp;-&nbsp;
        					<img src="./images/despensa_llena.gif" border="0" title="tiene Despensas">&nbsp;<label class="textoNaranja10">Despensa</label>
						</td>
        			</tr>
        		</table>
        	</td>
        </tr>
        <tr>
        	<td height="3px"></td>
        </tr>--%>
        <tr>
            <td colspan="2">
                <table border="0" cellspacing="0" cellpadding="1" width="100%">
                   	<%--<tr class="tituloTablas"  align="left">--%>
                    <tr class="tituloTablasB"  align="left">
                        <td class="columna_contenido" width="5%" align="center">&nbsp;</td>
                        <td class="columna_contenido" width="5%" align="center">No</td>
                        <td class="columna_contenido" width="60%" align="center">LOCAL</td>
                        <td class="columna_contenido" width="15%" align="center">DES</td>
                        <td class="columna_contenido" width="15%" align="center">ART</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">
	            <table border="0" cellspacing="0" cellpadding="2" width="100%">
	                <logic:iterate name="ec.com.smx.sic.sispe.articulosDespachoLocal.${vDia}.${vTipo}" id="vistaArticuloDTO" indexId="indiceArticulo">
	                    <bean:size id="sizeEntregas" name="vistaArticuloDTO" property="colVistaArticuloDTO"/>
	                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
	                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
	                    <%--------- control del estilo para el color de las filas --------------%>
	                    <bean:define id="residuo" value="${indiceArticulo % 2}"/>
	                    <%--<c:set var="clase" value="blanco10"/>--%>
	                    <c:set var="clase" value="blanco9B"/>
	                    <logic:notEqual name="residuo" value="0">
	                      <%--<c:set var="clase" value="grisClaro10"/>--%>  
	                      <c:set var="clase" value="grisClaro9B"/>
	                    </logic:notEqual>
	                    <%--------------------------------------------------------------------%>
	                    <tr class="${clase}">
	                        <td class="columna_contenido fila_contenido" width="5%" align="center">
	                            <c:set var="despliegueA1" value="block"/>
	                            <c:set var="despliegueA2" value="none"/>
	                            <logic:notEmpty name="indiceNivel1">
	                                <logic:equal name="indiceNivel1" value="${indiceTotal}">
	                                    <c:set var="despliegueA1" value="none"/>
	                                    <c:set var="despliegueA2" value="block"/>
	                                </logic:equal>
	                            </logic:notEmpty>
	                            
	                            <div style="display:${despliegueA1}" id="desplegar_despacho_${indiceArticulo}_${vDia}_${vTipo}">
	                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_despacho_${indiceArticulo}_${vDia}_${vTipo}']);show(['plegar_despacho_${indiceArticulo}_${vDia}_${vTipo}','listado_despacho_${indiceArticulo}_${vDia}_${vTipo}']);"></a>
	                            </div>
	                            <div style="display:${despliegueA2}" id="plegar_despacho_${indiceArticulo}_${vDia}_${vTipo}">
	                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_despacho_${indiceArticulo}_${vDia}_${vTipo}','listado_despacho_${indiceArticulo}_${vDia}_${vTipo}']);show(['desplegar_despacho_${indiceArticulo}_${vDia}_${vTipo}']);"></a>
	                            </div>
	                        </td>
	                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila"/></td>                       
	                        <td class="columna_contenido fila_contenido" width="60%" align="left"><bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/></td>
	                        <td class="columna_contenido fila_contenido" width="15%" align="right"><b><bean:write name="vistaArticuloDTO"  property="npNumeroEntregas"/><%--bean:write name="sizeEntregas"/--%></b></td>
	                        <td class="columna_contenido fila_contenido columna_contenido_der" width="15%" align="right"><b><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></b></td>
	                    </tr>
	                    <tr>
	                        <td colspan="5" id="listado_despacho_${indiceArticulo}_${vDia}_${vTipo}" align="center" style="display:${despliegueA2}">
	                            <!-- se muestra el detalle de los locales -->
	                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
	                                <%--<tr class="tituloTablasCeleste">--%>
	                                <tr class="tituloTablasCelesteB">
	                                    <td class="columna_contenido" width="5%" align="center">&nbsp;</td>
				                        <td class="columna_contenido" width="5%" align="center">No</td>
				                        <td class="columna_contenido" width="15%" align="center">No PEDIDO</td>
				                        <td class="columna_contenido" width="10%" align="center">No RES</td>
				                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
					                        <td class="columna_contenido" width="35%" align="center">CONTACTO EMPRESA</td>
					                        <td class="columna_contenido" width="10%" align="center">DESPACHADO</td>
					                        <td class="columna_contenido" width="10%" align="center">DESPACHO</td>
					                        <td class="columna_contenido" width="10%" align="center">PENDIENTE</td>
				                        </logic:equal>
				                         <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
					                        <td class="columna_contenido" width="45%" align="center">CONTACTO EMPRESA</td>
					                        <td class="columna_contenido" width="10%" align="center">DESPACHO</td>
					                        <td class="columna_contenido" width="10%" align="center">TOTAL</td> 
				                        </logic:equal>
				                        <td class="columna_contenido" width="5%" align="center">ART</td>
	                                </tr>
	                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
	                                    <%--------- control del estilo para el color de las filas --------------%>
	                                    <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
	                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
	                                    <logic:equal name="residuo2" value="0">
	                                        <%--<bean:define id="clase2" value="blanco10"/>--%>
	                                        <bean:define id="clase2" value="blanco9B"/>
	                                    </logic:equal>
	                                    <logic:notEqual name="residuo2" value="0">
	                                         <%--<bean:define id="clase2" value="amarilloClaro10"/>--%>
	                                        <bean:define id="clase2" value="amarilloClaro9B"/>
	                                    </logic:notEqual>
	                                    <%--------------------------------------------------------------------%>
	                                    <tr class="${clase2}">
	                                        <td class="columna_contenido fila_contenido" width="5%" align="center">
	                                            <c:set var="despliegueL1" value="block"/>
	                                            <c:set var="despliegueL2" value="none"/>
	                                            <logic:notEmpty name="indiceNivel2">
	                                                <logic:equal name="indiceNivel1" value="${indiceTotal}">
	                                                    <logic:equal name="indiceNivel2" value="${indiceArticulo2}">
	                                                        <c:set var="despliegueL1" value="none"/>
	                                                        <c:set var="despliegueL2" value="block"/>
	                                                    </logic:equal>
	                                                </logic:equal>
	                                            </logic:notEmpty>
	                                                        
	                                            <div style="display:${despliegueL1}" id="desplegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}">
	                                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}']);show(['plegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}','listado_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}']);"></a>
	                                            </div>
	                                            <div style="display:${despliegueL2}" id="plegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}">
	                                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}','listado_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}']);show(['desplegar_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}']);"></a>
	                                            </div>
	                                        </td>
	                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila2"/></td>
	                                        <td class="columna_contenido fila_contenido" width="15%" align="center">
	                                        	<%--<html:link title="detalle del pedido" onclick="mostrarPopUpDetalle('${vistaArticuloDTO2.id.codigoPedido}');" href="#"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></html:link>--%>
	                                        	<html:link title="Detalle del pedido" onclick="mostrarPopUpDetalle('${vistaArticuloDTO2.id.codigoPedido}');" href="#"><label class="textoRojo9"><bean:write name="vistaArticuloDTO2" property="id.codigoPedido"/></label></html:link>
	                                        	<logic:equal name="vistaArticuloDTO2" property="npTieneCanasta" value="true"><img src="./images/canasto_lleno.gif" border="0" title="Tiene canastas"></logic:equal>
	                                        	<logic:equal name="vistaArticuloDTO2" property="npTieneDespensa" value="true"><img src="./images/despensa_llena.gif" border="0" title="Tiene despensas"></logic:equal>
	                                        </td>
	                                        <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO2" property="llaveContratoPOS"/></td>
	                                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="0">
						                         <logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
						                    		<td class="columna_contenido fila_contenido" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
						                    	</logic:empty>
						                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
						                    		<td class="columna_contenido fila_contenido" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
						                    	</logic:notEmpty>
						                        <td class="columna_contenido fila_contenido" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroEntregados"/></b></td>
	                                        	<td class="columna_contenido fila_contenido" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
	                                       		<td class="columna_contenido fila_contenido" width="10%" align="right"><b>${vistaArticuloDTO2.numeroTotalEntregas - vistaArticuloDTO2.numeroEntregados}/<bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
					                        </logic:equal>
					                        <logic:equal name="ec.com.smx.sic.sispe.banderaBodegaTransito" value="1">
	                                        	  	<logic:empty name="vistaArticuloDTO2" property="nombreEmpresa">
						                    		<td class="columna_contenido fila_contenido" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoContacto}</td>
						                    	</logic:empty>
						                    	<logic:notEmpty name="vistaArticuloDTO2" property="nombreEmpresa">
						                    		<td class="columna_contenido fila_contenido" width="35%" align="left">${vistaArticuloDTO2.contactoEmpresa} - ${vistaArticuloDTO2.telefonoEmpresa}</td>
						                    	</logic:notEmpty>
	                                        	<td class="columna_contenido fila_contenido" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="npNumeroEntregas"/></b></td>
	                                       		<td class="columna_contenido fila_contenido" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="numeroTotalEntregas"/></b></td>
					                        </logic:equal>
	                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="5%" align="right"><b><bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/></b></td>
	                                    </tr>
	                                    <tr>
	                                        <td colspan="9" id="listado_despacho_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}" align="center" style="display:${despliegueL2}">
	                                            <!-- se muestra el detalle de los locales -->
	                                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
	                                                <%--<tr class="tituloTablasCeleste">--%>
	                                                <tr class="tituloTablasCelesteB">
	                                                    <td class="columna_contenido" width="5%" align="center">No</td>
	                                                    <td class="columna_contenido" width="15%" align="center">C&Oacute;DIGO BARRAS</td>
	                                                    <td class="columna_contenido" width="20%" align="center">ART&Iacute;CULO</td>
	                                                    <td class="columna_contenido" width="20%" align="center">LUGAR ENTREGA</td>
	                                                    <td class="columna_contenido" width="15%" align="center">FECHA DESPACHO</td>
	                                                    <td class="columna_contenido" width="15%" align="center">FECHA ENTREGA</td>
	                                                    <td class="columna_contenido" width="10%" align="center">ART</td>
	                                                </tr>
	                                                <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
	                                                    <%--------- control del estilo para el color de las filas --------------%>
	                                                    <bean:define id="residuo3" value="${indiceArticulo3 % 2}"/>
	                                                    <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
	                                                    <%--<bean:define id="claseTexto3" value="textoNegro10"/>--%>
	                                                    <bean:define id="claseTexto3" value="textoNegro9B"/>
	                                                    <logic:equal name="residuo3" value="0">
	                                                        <%--<bean:define id="clase3" value="blanco10"/>--%>
	                                                        <bean:define id="clase3" value="blanco9B"/>
	                                                    </logic:equal>
	                                                    <logic:notEqual name="residuo3" value="0">
	                                                        <%--<bean:define id="clase3" value="amarilloClaro10"/>--%>
	                                                        <bean:define id="clase3" value="amarilloClaro9B"/>
	                                                    </logic:notEqual>
	                                                    
	                                                    <bean:define id="codigoSubClasificacion" name="vistaArticuloDTO3" property="codigoSubClasificacion"/>
	                                                     <c:if test="${fn:contains(tipoDespensa, codigoSubClasificacion)}">
														  	<bean:define id="claseTexto3" value="textoNaranja9B"/>
														</c:if>
	                                                    
	                                                    
	                                                   <%--wa <logic:equal name="vistaArticuloDTO3" property="codigoTipoArticulo" value="${codigoTipArtDesp}">--%>
	                                                    	<%--<bean:define id="claseTexto3" value="textoNaranja10"/>--%>
	                                                    <%--wa	<bean:define id="claseTexto3" value="textoNaranja9B"/>
	                                                    </logic:equal>
	                                                    --%>
	                                                    <tr class="${clase3}">
	                                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><label class="${claseTexto3}"><bean:write name="numFila3"/></label></td>
	                                                        <c:if test="${vistaArticuloDTO3.codigoClasificacion == canastoCatalogo || vistaArticuloDTO3.codigoClasificacion == tipoCanastaModificada}">
	                                                        	<td class="columna_contenido fila_contenido" width="15%" align="center">
		                                                        	<a href="#" class="${claseTexto3}" onclick="mostrarDetalleCanastaDirecto('${vistaArticuloDTO3.id.codigoArticulo}', '${vistaArticuloDTO3.id.secuencialEstadoPedido}','${vistaArticuloDTO3.id.codigoPedido}','${vistaArticuloDTO3.codigoArticuloOriginal}');"><label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="codigoBarras"/></label></a>
		                                                        </td>
	                                                        </c:if>
	                                                        <c:if test="${vistaArticuloDTO3.codigoClasificacion != canastoCatalogo && vistaArticuloDTO3.codigoClasificacion != tipoCanastaModificada}">
	                                                        	<td class="columna_contenido fila_contenido" width="15%" align="center">
		                                                        	<label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="codigoBarras"/></label>
		                                                        </td>
	                                                        </c:if>
	                                                        <td class="columna_contenido fila_contenido" width="20%" align="left"><label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="descripcionArticulo"/></label></td>
	                                                        <td class="columna_contenido fila_contenido" width="20%" align="left"><label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></label></td>
	                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/></label></td>
	                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><label class="${claseTexto3}"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></label></td>
	                                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></b></td>
	                                                    </tr>
	                                                </logic:iterate>
	                                            </table>
	                                        </td>
	                                    </tr>
	                                </logic:iterate>
	                            </table>
	                        </td>
	                    </tr>
	                </logic:iterate>
	            </table>
            </td>
        </tr>
        <tr><td height="5px" colspan="2"></td></tr>
    </logic:notEmpty>
    <logic:empty name="ec.com.smx.sic.sispe.articulosDespachoLocal.${vDia}.${vTipo}">
    	<tr>
    		<td width="80%" height="40px" align="center" style="vertical-align: middle;">
    			<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
    				<tr>
    					<td width="100%" height="100%" class="tabla_informacion amarillo1">
    						NO TIENE DESPAHO A LOCALES PENDIENTE
    					</td>
    				</tr>
    			</table>
    		</td>
    	</tr>
    </logic:empty>
</table>
