<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<c:set var="alt_div_ent" value="245"/>
<logic:notEmpty name="ec.com.smx.sic.sispe.entidadBodega">
    <c:set var="alt_div_ent" value="225"/>
</logic:notEmpty>

<html:hidden name="cotizarRecotizarReservarForm" property="checkCalculosPreciosMejorados"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" class="tabla_informacion">
    <tr class="fila_titulo">
        <td align="left" class="fila_contenido textoNegro11">
            <b>&nbsp;Detalle De Entregas:</b>
        </td>
        <td height="27px" class="fila_contenido" align="right">
            <table cellspacing="0" cellpadding="0" align="right">
                <tr>
                    <td>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
                            <div id="botonD">
                                <input type="hidden" name="registrarFecha" value="">
                                <html:link styleClass="entregarD" href="#" onclick="cotizarRecotizarReservarForm.registrarFecha.value='OK';cotizarRecotizarReservarForm.submit();popWait();">Entregas</html:link>
                            </div>
                        </logic:notEmpty>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td height="5px" colspan="2"></td></tr>
    <tr>
        <td colspan="2">
            <logic:notEmpty name="ec.com.smx.sic.sispe.detallePedido">
                <table cellspacing="0" cellpadding="0" width="98%">
                    <tr>
                        <td>
                            <table cellspacing="0" cellpadding="0" width="100%">
                                <tr class="tituloTablas">
                                    <td class="columna_contenido" align="center" width="3%" rowspan="2">No</td>
                                    <td class="columna_contenido" width="2%" rowspan="2">&nbsp;</td>
                                    <td class="columna_contenido" align="center" width="27%" rowspan="2">DESCRIPCI&Oacute;N ART&Iacute;CULO</td>
                                    <td class="columna_contenido" align="center" width="8%" rowspan="2">CANT. TOTAL.</td>
                                    <td class="columna_contenido" align="center" width="8%" rowspan="2">BULTOS</td>
                                    <td class="columna_contenido" align="center" width="50%" title="fecha despacho a bodega | fecha entrega al cliente | unidades (bultos) a entregar">ENTREGAS</td>
                                </tr>
                                <tr class="tituloTablas"><td class="columna_contenido" align="center" width="50%" title="fecha despacho a bodega | fecha entrega al cliente | unidades (bultos) a entregar">FECHA DES. | FECHA ENT. | UNIDADES (BULTOS) A DIRECCION</td></tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div style="width:100%;height:${alt_div_ent}px;overflow:auto">
                                <table cellspacing="0" cellpadding="0" width="100%">
                                    <logic:iterate name="ec.com.smx.sic.sispe.detallePedido" id="detalle" indexId="numDetalle">
                                        <bean:define id="numFila" value="${numDetalle + 1}"/>
                                        <%--------- control del estilo para el color de las filas --------------%>
                                        <bean:define id="residuo" value="${numDetalle % 2}"/>
                                        <c:set var="clase" value="blanco10"/>
                                        <c:set var="title_fila" value=""/>
                                        <logic:notEqual name="residuo" value="0">
                                            <c:set var="clase" value="grisClaro10"/>
                                        </logic:notEqual>
                                        
                                        <%-- estilo para los art&iacute;culos especiales ---%>
                                        <logic:notEmpty name="detalle" property="articuloDTO.npEstadoEspecial">
                                            <c:set var="title_fila" value="art&iacute;culo especial"/>
                                            <c:set var="clase" value="verdeClaro10"/>
                                        </logic:notEmpty>
                                        <tr id="fila_en_${numDetalle}" class="${clase}">
                                            <td class="columna_contenido fila_contenido" align="center" width="3%"><bean:write name="numFila"/></td>
                                            <bean:define name="detalle" property="articuloDTO" id ="articulo"/>
                                            <td class="columna_contenido fila_contenido" width="2%">
                                                <logic:greaterThan name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
                                                    <img src="images/error_small.gif" title="este art&iacute;culo NO se ha entregado completamente"/>
                                                </logic:greaterThan>
                                                <logic:equal name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${detalle.npContadorEntrega}">
                                                    <img src="images/exito_16.gif" title="este art&iacute;culo se ha entregado completamente"/>
                                                </logic:equal>
                                            </td>
                                            <td class="columna_contenido fila_contenido" align="left" width="27%">
                                            	<logic:notEmpty name="articulo" property="npNuevoCodigoClasificacion">
													<img title="canasto modificado" src="images/estrella.gif" border="0">
												</logic:notEmpty>
                                            	<bean:write name="articulo" property="descripcionArticulo"/>
                                            </td>
                                            <td class="columna_contenido fila_contenido" align="center" width="8%"><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></td>
                                            <td class="columna_contenido fila_contenido" align="center" width="8%"><bean:write name="detalle" property="estadoDetallePedidoDTO.npCantidadBultos"/></td>
                                            
                                            <%---------------------- si la colecci&oacute;n de direcciones no est&aacute; vacia -------------------------%>
                                            <td align="center" class="columna_contenido fila_contenido columna_contenido_der" width="50%">
                                                <logic:notEmpty name="detalle" property="entregas">
                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tabla_informacion">
                                                        <logic:iterate name="detalle" property="entregas" id="entrega" indexId="numEntrega">
                                                            <bean:define id="residuoE" value="${numEntrega % 2}"/>
                                                            <logic:equal name="residuoE" value="0">
                                                                <bean:define id="claseE" value="blanco10"/>
                                                            </logic:equal>	
                                                            <logic:notEqual name="residuoE" value="0">
                                                                <bean:define id="claseE" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <tr class="${claseE}">
                                                                <td align="left">
                                                                    <table cellspacing="0" width="100%">
                                                                        <tr>
                                                                            <td width="96%" align="left" class="fila_contenido"><b><label class="textoAzul10"><bean:write name="entrega" property="fechaDespachoBodega" formatKey="formatos.fecha"/></label>|<label class="textoVerde10"><bean:write name="entrega" property="fechaEntregaCliente" formatKey="formatos.fecha"/></label>|<bean:write name="entrega" property="cantidadEntrega"/>&nbsp;(<bean:write name="entrega" property="npCantidadBultos"/>)</b>&nbsp;a&nbsp;<label class="textoRojo10"><bean:write name="entrega" property="direccionEntrega"/></label></td>
                                                                        </tr>
                                                                    </table>     
                                                                </td>
                                                            </tr>
                                                        </logic:iterate>
                                                        <tr>
                                                            <c:set var="colorTexto" value="textoAzul11"/>
                                                            <bean:define name="detalle" property="npContadorEntrega" id="contadorEntrega"/>	
                                                            <logic:equal name="detalle" property="estadoDetallePedidoDTO.cantidadEstado" value="${contadorEntrega}">
                                                                <c:set var="colorTexto" value="textoVerde11"/>
                                                            </logic:equal>
                                                            <td align="center" class="${colorTexto}"><b><bean:write name="detalle" property="npContadorEntrega"/></b>&nbsp;entregados de &nbsp;<b><bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/></b></td>
                                                        </tr>	
                                                    </table>
                                                </logic:notEmpty>
                                                <%---------------------- si la colecci&oacute;n de direcciones est&aacute; vacia -------------------------%>
                                                <logic:empty name="detalle" property="entregas">
                                                    <table cellpadding="0" cellspacing="0" border="0">
                                                        <tr>
                                                            <td><img src="images/error_small.gif"/></td>
                                                            <td class="textoRojo10">&nbsp;Sin direcci&oacute;n de entrega</td>
                                                        </tr>
                                                    </table>
                                                </logic:empty>
                                            </td>
                                        </tr>
                                    </logic:iterate>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </logic:notEmpty>
            <logic:empty name="ec.com.smx.sic.sispe.detallePedido">
                <bean:define id="alturaDetalleVacio" value="${alt_div_ent + 40}"/>
                <div style="width:100%;height:${alturaDetalleVacio}px;overflow:auto">
                    <table cellpadding="0" cellspacing="0" border="0" width="100%" class="textoNegro11">
                        <tr>
                            <td width="3%" align="center"><img src="images/info_16.gif"></td>
                            <td align="left"><bean:message key="errors.detalle.requerido"/></td>
                        </tr>
                    </table>
                </div>
            </logic:empty>
        </td>
    </tr>
</table>