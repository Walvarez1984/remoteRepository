<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="estadoReservado" name="ec.com.smx.sic.sispe.pedido.estadoReservado"/>
<bean:define id="estadoEnProduccion" name="ec.com.smx.sic.sispe.pedido.estadoEnProduccion"/>
<%--<bean:define id="estadoAnulado" name="ec.com.smx.sic.sispe.pedido.estadoAnulado"/>--%>

<table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="99%" align="center" bgcolor="white">
    
    <%--Titulo de los datos--%>
    <tr>
        <td class="fila_titulo">
            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                <tr>
                    <td><img src="images/articulos24.gif" border="0"/></td>
                    <td height="23" width="100%">&nbsp;
                        Detalle general de los Art&iacute;culos
                    </td>
                    <td align="right">
                        <div id="botonD">
                            <html:link styleClass="excelD" href="#" title="Generar reporte de control de producci&oacute;n de art&iacute;culos" onclick="requestAjax('controlProduccion.do',['resultadosBusqueda','mensajes','pregunta' ],{parameters: 'crearReporte=ok', evalScripts: true});">Reporte</html:link>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="8px" bgcolor="#F4F5EB"></td></tr>
    <tr>
        <td bgcolor="#F4F5EB">
            <table border="0" class="textoNegro11" width="98%" align="center" cellspacing="0" cellpadding="0">
                <logic:notEmpty name="controlProduccionForm" property="datos">
                    <tr>
                        <td colspan="2">
                            <table cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="left" class="textoRojo10" id="ordenarPor">
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.ordenamiento.datosColumna">
                                            <bean:define id="datosColumnaOrdenada" name="ec.com.smx.sic.sispe.ordenamiento.datosColumna"/>
                                            <b>Ordenado Por:</b>&nbsp;<label class="textoAzul10">${datosColumnaOrdenada[0]}&nbsp;(Orden:&nbsp;${datosColumnaOrdenada[1]})</label>
                                        </logic:notEmpty>
                                    </td>
                                    <td align="right" id="seg_pag">
                                        <smx:paginacion start="${controlProduccionForm.start}" range="${controlProduccionForm.range}" results="${controlProduccionForm.size}" styleClass="textoNegro10" campos="false" url="controlProduccion.do" requestAjax="'seg_pag','div_listado'"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                <tr class="tituloTablas"  align="left">
                                    <td class="columna_contenido" width="3%" align="center">&nbsp;</td>
                                    <td class="columna_contenido" width="4%" align="center">No</td>
                                    <td class="columna_contenido" width="12%" align="center"><a class="linkBlanco9" title="Ordenar por c&oacute;digo barras" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=0'});">C&oacute;digo barras</a></td>
                                    <td class="columna_contenido" width="30%" align="center"><a class="linkBlanco9" title="Ordenar por descripci&oacute;n del art&iacute;culo" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=1'});">Art&iacute;culo</a></td>
                                    <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por unidad de manejo" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=6'});">Und. Manejo</a></td>
                                    <td class="columna_contenido fila_contenido" width="8%" align="center"><a class="linkBlanco9" title="Ordenar por total pendiente" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=4'});">Pendiente</a></td>
                                    <%--<td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="ordenar por porcentaje producido" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=6'});">% Producido</a></td>
                                    <td class="columna_contenido" width="10%" align="center"><a class="linkBlanco9" title="ordenar por porcentaje producido" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=7'});">Fecha de Despacho</a></td>--%>
                                </tr>
                                <%--
                                <tr class="tituloTablas"  align="left">
                                    <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="ordenar por total reservado" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=2'});">Total</a></td>
                                    <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="ordenar por total producido" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=3'});">Producido</a></td>
                                    <td class="columna_contenido" width="8%" align="center"><a class="linkBlanco9" title="ordenar por total pendiente" href="#" onclick="requestAjax('controlProduccion.do',['ordenarPor','div_listado'],{parameters: 'indiceOrdenarA=4'});">Pendiente</a></td>
                                </tr>
                                --%>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div id="div_listado" style="width:100%;height:380px;overflow:auto">
                                <table border="0" cellspacing="0" cellpadding="2" width="100%">
                                    <logic:iterate name="controlProduccionForm" property="datos" id="vistaArticuloDTO" indexId="indiceArticulo">
                                        <bean:define id="indiceTotal" value="${indiceArticulo + controlProduccionForm.start}"/>
                                        <bean:define id="numFila" value="${indiceTotal + 1}"/>
                                        <%--------- control del estilo para el color de las filas --------------%>
                                        <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                        <c:set var="clase" value="blanco10"/>
                                        <logic:notEqual name="residuo" value="0">
                                            <c:set var="clase" value="grisClaro10"/>
                                        </logic:notEqual>
                                        <%--------------------------------------------------------------------%>
                                        <tr class="${clase}">
                                            <td class="columna_contenido fila_contenido" width="3%" align="center">
                                                <c:set var="despliegueA1" value="block"/>
                                                <c:set var="despliegueA2" value="none"/>
                                                <logic:notEmpty name="indiceNivel1">
                                                    <logic:equal name="indiceNivel1" value="${indiceTotal}">
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
                                            <td class="columna_contenido fila_contenido" width="4%" align="left"><bean:write name="numFila"/></td>
                                            <td class="columna_contenido fila_contenido" width="12%" align="left"><bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                                            <td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                                            <td class="columna_contenido fila_contenido" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="unidadManejo"/></td>
                                            <%--<td class="columna_contenido fila_contenido" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></td>   					    
                                            <td class="columna_contenido fila_contenido" width="8%" align="right"><bean:write name="vistaArticuloDTO" property="cantidadParcialEstado"/></td>--%>
                                            <td class="columna_contenido fila_contenido textoAzul10 columna_contenido_der" width="8%" align="center"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
                                            <%--<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO" property="porcentajeProducido" formatKey="formatos.numeros"/>%</td>
                                            <td class="columna_contenido fila_contenido columna_contenido_der" width="10%" align="center"><bean:write name="vistaArticuloDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>--%>
                                        </tr>
                                        <tr>
                                            <td colspan="6" id="listado_${indiceArticulo}" align="center" style="display:${despliegueA2}">
                                                <!-- se muestra el detalle de los locales -->
                                                <table cellpadding="1" cellspacing="0" width="90%" class="tabla_informacion_negro">
                                                    <tr class="tituloTablasCeleste">
                                                        <td class="columna_contenido" width="3%" align="center">&nbsp;</td>
                                                        <td class="columna_contenido" width="5%" align="center">No</td>
                                                        <td class="columna_contenido" width="30%" align="center">Local</td>
                                                        <td class="columna_contenido" width="10%" align="center">Pendiente</td>
                                                        <!--<td class="columna_contenido" width="10%" align="center">% Producido</td>-->
                                                    </tr>
                                                    <!--
                                                    <tr class="tituloTablasCeleste">
                                                        <td class="columna_contenido" width="10%" align="center">Total</td>
                                                        <td class="columna_contenido" width="10%" align="center">Producido</td>
                                                        <td class="columna_contenido" width="10%" align="center">Pendiente</td>
                                                    </tr>
                                                    -->
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
                                                        <%--------------------------------------------------------------------%>
                                                        <tr class="${clase2}">
                                                            <td class="columna_contenido fila_contenido" width="3%" align="center">
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
                                                                            
                                                                <div style="display:${despliegueL1}" id="desplegar_${indiceArticulo}_${indiceArticulo2}">
                                                                    <a href="#"><img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indiceArticulo}_${indiceArticulo2}']);show(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);"></a>
                                                                </div>
                                                                <div style="display:${despliegueL2}" id="plegar_${indiceArticulo}_${indiceArticulo2}">
                                                                    <a href="#"><img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indiceArticulo}_${indiceArticulo2}','listado_${indiceArticulo}_${indiceArticulo2}']);show(['desplegar_${indiceArticulo}_${indiceArticulo2}']);"></a>
                                                                </div>
                                                            </td>
                                                            <td class="columna_contenido fila_contenido" width="5%" align="center"><bean:write name="numFila2"/></td>
                                                            <td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO2" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO2" property="nombreLocalOrigen"/></td>
                                                            <%--<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO2" property="cantidadReservadaEstado"/></td>
                                                            <td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO2" property="cantidadParcialEstado"/></td>--%>
                                                            <td class="columna_contenido fila_contenido textoAzul10" width="10%" align="right"><b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b></td>
                                                            <%--<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO2" property="porcentajeProducido" formatKey="formatos.numeros"/>%</td>--%>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="4" id="listado_${indiceArticulo}_${indiceArticulo2}" align="center" style="display:${despliegueL2}">
                                                                <!-- se muestra el detalle de los locales -->
                                                                <table cellpadding="1" cellspacing="0" width="90%" class="tabla_informacion_negro">
                                                                    <tr class="tituloTablasCeleste">
                                                                        <td class="columna_contenido" width="4%" align="center">No</td>
                                                                        <td class="columna_contenido" width="15%" align="center">No Pedido</td>
                                                                        <td class="columna_contenido" width="10%" align="center">No Reserva</td>
                                                                        <td class="columna_contenido" width="10%" align="center">Lugar Entrega</td>
                                                                        <td class="columna_contenido" width="10%" align="center">Fecha Despacho</td>
                                                                        <td class="columna_contenido" width="10%" align="center">Fecha Entrega</td>
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
                                                                            <td class="columna_contenido fila_contenido" width="10%" align="left"><html:link title="Detalle del pedido" onclick="realizarEnvio('IPVA-${indiceTotal}-${indiceArticulo2}-${indiceArticulo3}');" href="#"><bean:write name="vistaArticuloDTO3" property="id.codigoPedido"/></html:link></td>
                                                                            <td class="columna_contenido fila_contenido" width="10%" align="left"><bean:write name="vistaArticuloDTO3" property="llaveContratoPOS"/></td>
                                                                            <%--<td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO3" property="cantidadReservadaEstado"/></td>
                                                                            <td class="columna_contenido fila_contenido" width="10%" align="right"><bean:write name="vistaArticuloDTO3" property="cantidadParcialEstado"/></td>--%>
                                                                            <td class="columna_contenido fila_contenido" width="30%" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
                                                                            <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>
                                                                            <td class="columna_contenido fila_contenido" width="10%" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fechahora"/></td>
                                                                            <td class="columna_contenido fila_contenido textoAzul10" width="10%" align="right"><b><bean:write name="vistaArticuloDTO3" property="diferenciaCantidadEstado"/></b></td>
                                                                            <%--<td class="columna_contenido fila_contenido" width="11%" align="right"><bean:write name="vistaArticuloDTO3" property="porcentajeProducido" formatKey="formatos.numeros"/>%</td>--%>
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
                            </div>
                        </td>
                    </tr>
                    <tr><td height="5px" colspan="2"></td></tr>
                </logic:notEmpty>
            </table>
        </td>
    </tr>
</table>