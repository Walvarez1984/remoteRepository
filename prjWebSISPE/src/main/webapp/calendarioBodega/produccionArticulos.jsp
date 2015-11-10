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
        <%--logic:equal name="vDia" value="diaActual">
	        <tr>
	        	<td colspan="2">
	        		<table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
	        			<tr>
	        				<td width="30px"><b>INFO:&nbsp;</b></td>
	        				<td class="rojoClaro10B" width="20px">&nbsp;</td>
							<td>&nbsp;Producci&oacute;n Pendiente</td>
	        			</tr>
	        		</table>
	        	</td>
	        </tr>
	        <tr>
	        	<td height="3px"></td>
	        </tr>
        </logic:equal--%>
        <logic:equal name="vClasificacion" value="catalogo">
	        <tr>
	            <td colspan="2">
	                <table border="0" cellspacing="0" cellpadding="1" width="100%">
	                    <tr class="tituloTablasB"  align="left">
	                        <td class="columna_contenido" width="5%" align="center">&nbsp;</td>
	                        <td class="columna_contenido" width="5%" align="center">No</td>
	                        <td class="columna_contenido" width="20%" align="center">C&Oacute;DIGO BARRAS</td>
	                        <td class="columna_contenido" width="60%" align="center">ART&Iacute;CULO</td>
	                        <td class="columna_contenido fila_contenido" width="10%" align="center">ART</td>
	                    </tr>
	                </table>
	            </td>
	        </tr>
	        <tr>
	            <td colspan="2">
		            <table border="0" cellspacing="0" cellpadding="2" width="100%">
		                <logic:iterate name="ec.com.smx.sic.sispe.articulosProduccion.${vDia}.${vTipo}.${vClasificacion}" id="vistaArticuloDTO" indexId="indiceArticulo">
		                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
		                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
		                    <%--------- control del estilo para el color de las filas --------------%>
		                    <bean:define id="residuo" value="${indiceArticulo % 2}"/>
		                    <c:set var="clase" value="blanco9B"/>
		                    <logic:notEqual name="residuo" value="0">
		                        <c:set var="clase" value="grisClaro9B"/>
		                    </logic:notEqual>
		                    <logic:equal name="vistaArticuloDTO" property="esPendiente" value="1">
                            	<bean:define id="clase" value="rojoClaro10B"/>
                            </logic:equal>
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
		                            
		                            <div style="display:${despliegueA1}" id="desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}">
		                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);show(['plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                            </div>
		                            <div style="display:${despliegueA2}" id="plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}">
		                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);show(['desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                            </div>
		                        </td>
		                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila"/></td>
		                        <td class="columna_contenido fila_contenido" width="20%" align="center"><a href="#" onclick="mostrarDetalleCanastaDirecto('${vistaArticuloDTO.id.codigoArticulo}',null,null,'${vistaArticuloDTO.codigoArticuloOriginal}');"><label class="textoNegro9"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></label></a></td>
		                        <td class="columna_contenido fila_contenido" width="60%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
		                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
		                    </tr>
		                    <tr>
		                        <td colspan="6" id="listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}" align="center" style="display:${despliegueA2}">
		                            <!-- se muestra el detalle de los locales -->
		                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
		                                <tr class="tituloTablasCelesteB">
		                                    <td class="columna_contenido" width="9%" align="center">&nbsp;</td>
		                                    <td class="columna_contenido" width="9%" align="center">No</td>
		                                    <td class="columna_contenido" width="82%" align="center">LOCAL</td>
		                                    <td class="columna_contenido" width="14%" align="center">ART</td>
		                                </tr>
		                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
		                                    <%--------- control del estilo para el color de las filas --------------%>
		                                    <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
		                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
		                                    <logic:equal name="residuo2" value="0">
		                                        <bean:define id="clase2" value="blanco9B"/>
		                                    </logic:equal>
		                                    <logic:notEqual name="residuo2" value="0">
		                                        <bean:define id="clase2" value="amarilloClaro9B"/>
		                                    </logic:notEqual>
		                                    <logic:equal name="vistaArticuloDTO2" property="esPendiente" value="1">
                                            	<bean:define id="clase2" value="rojoClaro10B"/>
                                            </logic:equal>
		                                    <%--------------------------------------------------------------------%>
		                                    <tr class="${clase2}">
		                                        <td class="columna_contenido fila_contenido" width="9%" align="center">
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
		                                                        
		                                            <div style="display:${despliegueL1}" id="desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}">
		                                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);show(['plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                                            </div>
		                                            <div style="display:${despliegueL2}" id="plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}">
		                                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);show(['desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                                            </div>
		                                        </td>
		                                        <td class="columna_contenido fila_contenido" width="9%" align="center"><bean:write name="numFila2"/></td>
		                                        <td class="columna_contenido fila_contenido" width="82%" align="left"><bean:write name="vistaArticuloDTO2" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO2" property="nombreLocalOrigen"/></td>
		                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="14%" align="right"><b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b></td>
		                                    </tr>
		                                    <tr>
		                                        <td colspan="4" id="listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}" align="center" style="display:${despliegueL2}">
		                                            <!-- se muestra el detalle de los locales -->
		                                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
		                                                <tr class="tituloTablasCelesteB">
		                                                    <td class="columna_contenido" width="5%" align="center">No</td>
		                                                    <td class="columna_contenido" width="15%" align="center">No PEDIDO</td>
		                                                    <td class="columna_contenido" width="10%" align="center">No RES</td>
		                                                    <td class="columna_contenido" width="30%" align="center">LUGAR ENTREGA</td>
		                                                    <td class="columna_contenido" width="15%" align="center">FECHA DESPACHO</td>
		                                                    <td class="columna_contenido" width="15%" align="center">FECHA ENTREGA</td>
		                                                    <td class="columna_contenido" width="10%" align="center">ART</td>
		                                                </tr>
		                                                <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
		                                                    <%--------- control del estilo para el color de las filas --------------%>
		                                                    <bean:define id="residuo3" value="${indiceArticulo3 % 2}"/>
		                                                    <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
		                                                    <logic:equal name="residuo3" value="0">
		                                                        <bean:define id="clase3" value="blanco9B"/>
		                                                    </logic:equal>
		                                                    <logic:notEqual name="residuo3" value="0">
		                                                        <bean:define id="clase3" value="amarilloClaro9B"/>
		                                                    </logic:notEqual>
		                                                    <logic:equal name="vistaArticuloDTO3" property="esPendiente" value="1">
		                                                    	<bean:define id="clase3" value="rojoClaro10B"/>
		                                                    </logic:equal>
		                                                    <%--------------------------------------------------------------------%>
		                                                    <tr class="${clase3}">
		                                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila3"/></td>
		                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><html:link title="Detalle del pedido" onclick="mostrarPopUpDetalle('${vistaArticuloDTO3.id.codigoPedido}');" href="#"><label class="textoNegro9"><bean:write name="vistaArticuloDTO3" property="id.codigoPedido"/></label></html:link></td>
		                                                        <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO3" property="llaveContratoPOS"/></td>
		                                                        <td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
		                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
		                                                        <td class="columna_contenido fila_contenido" width="15%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
		                                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="diferenciaCantidadEstado"/></b></td>
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
	    </logic:equal>
	    <logic:equal name="vClasificacion" value="especial">
	    	<tr>
	            <td colspan="2">
	                <table border="0" cellspacing="0" cellpadding="1" width="100%">
	                    <tr class="tituloTablasB"  align="left">
	                        <td class="columna_contenido" width="5%" align="center">&nbsp;</td>
	                        <td class="columna_contenido" width="5%" align="center">No</td>
	                        <td class="columna_contenido" width="17%" align="center">No PEDIDO</td>
	                        <td class="columna_contenido" width="8%" align="center">No RES</td>
	                        <td class="columna_contenido" width="55%" align="center">CONTACTO EMPRESA</td>
	                        <td class="columna_contenido fila_contenido" width="10%" align="center">ART</td>
	                    </tr>
	                </table>
	            </td>
	        </tr>
	        <tr>
	            <td colspan="2">
		            <table border="0" cellspacing="0" cellpadding="2" width="100%">
		                <logic:iterate name="ec.com.smx.sic.sispe.articulosProduccion.${vDia}.${vTipo}.${vClasificacion}" id="vistaArticuloDTO" indexId="indiceArticulo">
		                    <bean:define id="indiceTotal" value="${indiceArticulo + calendarioBodegaForm.start}"/>
		                    <bean:define id="numFila" value="${indiceTotal + 1}"/>
		                    <%--------- control del estilo para el color de las filas --------------%>
		                    <bean:define id="residuo" value="${indiceArticulo % 2}"/>
		                    <c:set var="clase" value="blanco9B"/>
		                    <logic:notEqual name="residuo" value="0">
		                        <c:set var="clase" value="grisClaro9B"/>
		                    </logic:notEqual>
		                    <logic:equal name="vistaArticuloDTO" property="esPendiente" value="1">
                            	<bean:define id="clase" value="rojoClaro10B"/>
                            </logic:equal>
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
		                            
		                            <div style="display:${despliegueA1}" id="desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}">
		                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);show(['plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                            </div>
		                            <div style="display:${despliegueA2}" id="plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}">
		                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);show(['desplegar_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                            </div>
		                        </td>
		                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila"/></td>
		                        <td class="columna_contenido fila_contenido" width="17%" align="center"><html:link title="Detalle del pedido" onclick="mostrarPopUpDetalle('${vistaArticuloDTO.id.codigoPedido}');" href="#"><label class="textoNegro9"><bean:write name="vistaArticuloDTO" property="id.codigoPedido"/></label></html:link></td>
		                        <td class="columna_contenido fila_contenido" width="8%" align="center"><bean:write name="vistaArticuloDTO" property="llaveContratoPOS"/></td>
		                        <td class="columna_contenido fila_contenido" width="55%" align="left">CI:${vistaArticuloDTO.cedulaContacto} - NC:${vistaArticuloDTO.nombreContacto} - TC:${vistaArticuloDTO.telefonoContacto} - NE:${vistaArticuloDTO.nombreEmpresa}</td>
		                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
		                    </tr>
		                    <tr>
		                        <td colspan="6" id="listado_produccion_${indiceArticulo}_${vDia}_${vTipo}_${vClasificacion}" align="center" style="display:${despliegueA2}">
		                            <!-- se muestra el detalle de los locales -->
		                            <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
		                                <tr class="tituloTablasCelesteB">
		                                    <td class="columna_contenido" width="5%" align="center">&nbsp;</td>
		                                    <td class="columna_contenido" width="5%" align="center">No</td>
		                                    <td class="columna_contenido" width="30%" align="center">C&Oacute;DIGO</td>
		                                    <td class="columna_contenido" width="50%" align="center">ART&Iacute;CULO</td>
		                                    <td class="columna_contenido" width="10%" align="center">ART</td>
		                                </tr>
		                                <logic:iterate name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO2" indexId="indiceArticulo2">
		                                    <%--------- control del estilo para el color de las filas --------------%>
		                                    <bean:define id="residuo2" value="${indiceArticulo2 % 2}"/>
		                                    <bean:define id="numFila2" value="${indiceArticulo2 + 1}"/>
		                                    <logic:equal name="residuo2" value="0">
		                                        <bean:define id="clase2" value="blanco9B"/>
		                                    </logic:equal>
		                                    <logic:notEqual name="residuo2" value="0">
		                                        <bean:define id="clase2" value="amarilloClaro9B"/>
		                                    </logic:notEqual>
		                                    <logic:equal name="vistaArticuloDTO2" property="esPendiente" value="1">
                                            	<bean:define id="clase2" value="rojoClaro10B"/>
                                            </logic:equal>
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
		                                                        
		                                            <div style="display:${despliegueL1}" id="desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}">
		                                                <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);show(['plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                                            </div>
		                                            <div style="display:${despliegueL2}" id="plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}">
		                                                <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}','listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);show(['desplegar_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}']);"></a>
		                                            </div>
		                                        </td>
		                                        <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila2"/></td>
		                                        <td class="columna_contenido fila_contenido" width="30%" align="center"><a href="#" onclick="mostrarDetalleCanastaDirecto('${vistaArticuloDTO2.id.codigoArticulo}', '${vistaArticuloDTO2.id.secuencialEstadoPedido}','${vistaArticuloDTO2.id.codigoPedido}','${vistaArticuloDTO2.codigoArticuloOriginal}');"><label class="textoNegro9"><bean:write name="vistaArticuloDTO2" property="codigoBarras"/></label></a></td>
		                                        <td class="columna_contenido fila_contenido" width="50%" align="left"><bean:write name="vistaArticuloDTO2" property="descripcionArticulo"/></td>
		                                        <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center"><b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b></td>
		                                    </tr>
		                                    <tr>
					                            <td colspan="5" id="listado_produccion_${indiceArticulo}_${indiceArticulo2}_${vDia}_${vTipo}_${vClasificacion}" align="center" style="display:${despliegueL2}">
					                                <!-- se muestra el detalle de los locales -->
					                                <table cellpadding="1" cellspacing="0" width="96%" class="tabla_informacion_negro">
					                                    <tr class="tituloTablasCelesteB">
					                                        <td class="columna_contenido" width="5%" align="center">No</td>
					                                        <td class="columna_contenido" width="45%" align="center">LUGAR ENTREGA</td>
					                                        <td class="columna_contenido" width="20%" align="center">FECHA DESPACHO</td>
					                                        <td class="columna_contenido" width="20%" align="center">FECHA ENTREGA</td>
					                                        <td class="columna_contenido" width="10%" align="center">ART</td>
					                                    </tr>
					                                    <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3" indexId="indiceArticulo3">
					                                        <%--------- control del estilo para el color de las filas --------------%>
					                                        <bean:define id="residuo3" value="${indiceArticulo3 % 2}"/>
					                                        <bean:define id="numFila3" value="${indiceArticulo3 + 1}"/>
					                                        <logic:equal name="residuo3" value="0">
					                                            <bean:define id="clase3" value="blanco9B"/>
					                                        </logic:equal>
					                                        <logic:notEqual name="residuo3" value="0">
					                                            <bean:define id="clase3" value="amarilloClaro9B"/>
					                                        </logic:notEqual>
					                                        <logic:equal name="vistaArticuloDTO3" property="esPendiente" value="1">
					                                        	<bean:define id="clase3" value="rojoClaro10B"/>
					                                        </logic:equal>
					                                        <%--------------------------------------------------------------------%>
					                                        <tr class="${clase3}">
					                                            <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila3"/></td>
					                                            <td class="columna_contenido fila_contenido" width="45%" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
					                                            <td class="columna_contenido fila_contenido" width="20%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
					                                            <td class="columna_contenido fila_contenido" width="20%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
					                                            <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="diferenciaCantidadEstado"/></b></td>
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
	    </logic:equal>
        <tr><td height="5px" colspan="2"></td></tr>
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
