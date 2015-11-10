<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@page contentType="application/pdf" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>
<fmt:setLocale value="en_US"/>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>
<bean:define id="pathSave" name="ec.com.smx.sic.sispe.reporte.directorioSalida"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.estado.despachadoEspecial">
    <bean:define id="estadoDespachado" name="ec.com.smx.sic.sispe.estado.despachadoEspecial"></bean:define>
</logic:notEmpty>
<kreport:pdfout saveToServer="${pathSave}">
    <html page-orientation="V">
        <head>
            <meta http-equiv="Content-Style-Type" content="text/css"/>
            <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
            <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
            <meta HTTP-EQUIV="max-age" CONTENT="0"/>
            <meta HTTP-EQUIV="Expires" CONTENT="0"/>
        </head>
        <body>
            <logic:notEmpty name="sispe.estado.activo"><bean:define id="estadoActivo" name="sispe.estado.activo"/></logic:notEmpty>
            <logic:notEmpty name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo">
                <bean:define id="codigoEstablecimiento" name="ec.com.smx.sic.sispe.codigoTipoEstablecimientoObjetivo"/>
            </logic:notEmpty>
            <bean:define name="sispe.vistaEntidadResponsableDTO" id="vistaEntidadResponsableDTO"/>
            <bean:define id="entidadLocal"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local"/></bean:define>
            <bean:define id="entidadBodega"><bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega"/></bean:define>
            
            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte">
                <table align="left" border="0" cellspacing="0" cellpadding="0" width="100%">
                    <tr>
                        <td valign="top" font-size="7pt" >
                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                <tr>
                                    <td align="left" >
                                        <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&#32;-&#32;
                                        <bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&#32;
                                        <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
                                    </td>
                                    <td align="right">Ruc:&#32;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
                                </tr>
                                
                            </table>
                        </td>
                    </tr>	
                    <tr height="100"><td></td></tr>
                    <tr>
                        <td  align="left" valign="top" font-size="7pt"  >
                            <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                <tr>
                                    <td>
                                        &nbsp;
                                        &nbsp;
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        <!-- INICIO CABECERA --> 
                                        <table border="0" cellspacing="0" width="100%" align="left" >
                                            <tr>
                                                <td width="95px" align="left" >Entidad responsable:
                                                </td>
                                                <td align="left" width="80px">
                                                    <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadLocal}">
                                                        <bean:message key="ec.com.smx.sic.sispe.entidadResponsable.local.descripcion"/>
                                                    </logic:equal>
                                                    <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="entidadResponsable" value="${entidadBodega}">
                                                        <bean:message key="ec.com.smx.sic.sispe.entidadResponsable.bodega.descripcion"/>
                                                    </logic:equal>
                                                </td>
                                                <td  align="right" width="75px">Estado:
                                                </td>
                                                <td align="left"> 
                                                    &nbsp;<bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="descripcionEstado"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  align="right"></td>
                                                <td align="left"></td>
                                            </tr>
                                            <tr>
                                                <td align="left">No. pedido:
                                                </td>
                                                <td align="left">
                                                    <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoPedido"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="left">DATOS DEL CLIENTE</td>
                                            </tr>                                        
                                            <tr>
                                                <td>
                                                    <table cellpadding="0" cellspacing="0" border="0">
                                                        <tr>
                                                            <td align="right">CI/Pasaporte:&#32;</td>
                                                            <td align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="cedulaContacto"/></td>  
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Nombre:&#32;</td>
                                                            <td align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreContacto"/></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Tel&eacute;fono:&#32;</td>
                                                            <td align="left"><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="telefonoContacto"/></td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  align="left">Fecha de elaboraci&oacute;n:
                                                </td>
                                                <td  align="left">
                                                    <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="fechaInicialEstado" formatKey="formatos.fechahora"/>
                                                </td>
                                                <td align="left" >Elaborado en:
                                                </td>
                                                <td align="left"> 
                                                    <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoAreaTrabajo"/><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="nombreLocal"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="left">Fecha de despacho:</td>
                                                <td><bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaDespacho" formatKey="formatos.fecha"/>
                                                </td>
                                                <td align="left">Fecha de entrega:&#32;</td>
                                                <td>
                                                    <bean:write name="ec.com.smx.sic.sispe.vistaPedido" property="primeraFechaEntrega" formatKey="formatos.fechahora"/>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- FIN CABECERA --> 
                                    </td>
                                </tr>
                                
                                <!-- INICIO DETALLE -->  
                            </table>
                        </td>
                    </tr>
                    <tr height="50px"><td></td></tr>
                    <tr>
                        <td align="center" font-size="7pt" >
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
                                <tr><td><hr/></td></tr>       
                                <tr align="left" >
                                    <td font-size="10pt" colspan="2" height="15px">DETALLE DEL PEDIDO:</td>
                                </tr>
                                <tr><td><hr/></td></tr>       
                                <tr>
                                    <td>
                                        <logic:notEqual name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoDespachado}">
                                            <c:set var="columnas" value="7"/>
                                        </logic:notEqual>
                                        <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoDespachado}">      
                                            <c:set var="columnas" value="11"/>
                                        </logic:equal>
                                        <table width="100%" border="0" cellpadding="1" cellspacing="0">	
                                            <tr>
                                                <td> 
                                                    <table width="100%" border="0" cellpadding="1" cellspacing="0">
                                                        <tr>
                                                            <td width="18px" align="left">No</td>
                                                            <td width="27px" align="left" >C&oacute;digo barras</td>
                                                            <td width="120px" align="center" >Art&iacute;culo</td>
                                                            <td width="100px" align="left">Observaci&oacute;n</td>
                                                            <td width="20px" align="right" title="cantidad total a entregar">Cant.</td>
                                                            <td width="35px" align="center" >Peso KG.</td>
                                                            <td width="23px" align="right">V. unit.</td>
                                                            <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoDespachado}">
                                                                <td align="right" width="30px">Tot. IVA</td>
                                                                <td align="center" width="15px">IVA</td>
                                                                <td  align="right" width="25px">V. unit. neto</td>
                                                                <td  align="right" width="35px">Tot. neto</td>
                                                            </logic:equal>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <c:set var="condicion" value="0.00"/>
                                                <td colspan="${columnas}">
                                                    
                                                    <table  width="100%" border="0" cellpadding="1" cellspacing="0">	
                                                        <logic:iterate name="ec.com.smx.sic.sispe.vistaPedido" property="vistaDetallesPedidosReporte" id="vistaDetallePedidoDTO" indexId="indiceDetalle">
                                                            <%--------- control del estilo para el color de las filas ---------------%>
                                                            <bean:define id="residuo" value="${indiceDetalle % 2}"/>
                                                            <logic:equal name="residuo" value="0">
                                                                <bean:define id="colorBack" value="blanco10"/>
                                                            </logic:equal>	
                                                            <logic:notEqual name="residuo" value="0">
                                                                <bean:define id="colorBack" value="grisClaro10"/>
                                                            </logic:notEqual>
                                                            <%---------------------------------------------------------------------%>
                                                            <bean:define id="numRegistro" value="${indiceDetalle + 1}"/>
                                                            <tr>
                                                                <td width="18px" align="left" ><bean:write name="numRegistro"/></td>
                                                                <td width="27px" align="left">
                                                                    <logic:notEmpty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
                                                                        <bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
                                                                    </logic:notEmpty>
                                                                    <logic:empty name="vistaDetallePedidoDTO" property="numeroAutorizacionOrdenCompra">
                                                                        <bean:write name="vistaDetallePedidoDTO" property="codigoBarras"/>
                                                                    </logic:empty> 
                                                                </td>
                                                                <td width="120px" align="left"><bean:write  name="vistaDetallePedidoDTO" property="descripcionArticulo"/></td>
                                                                <td width="100px" align="left" ><bean:write name="vistaDetallePedidoDTO" property ="observacionArticulo"/></td>
                                                                <td width="20px" align="right"  title="cantidad total a entregar">
                                                                    <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.cantidadEstado}"/>
                                                                    </logic:equal>
                                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                        <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                                    </logic:notEqual>
                                                                </td>
                                                                <td width="35px" align="right">
                                                                    <logic:notEqual name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.pesoArticuloEstado}"/>
                                                                    </logic:notEqual>
                                                                    <logic:equal name="vistaDetallePedidoDTO" property="pesoArticuloEstado" value="${condicion}">
                                                                        <bean:message key="ec.com.smx.sic.sispe.valor.noAplica"/>
                                                                    </logic:equal>
                                                                </td>
                                                                <td width="23px" align="right" ><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/></td>
                                                                <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoDespachado}">
                                                                	<td  align="right" width="30px"><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioIVAEstado}"/>
                                                                    </td>
                                                                    <td align="center" width="15px">
                                                                        <logic:equal name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                            <html:img page="/images/tick.png" border="0"/>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="vistaDetallePedidoDTO" property="aplicaIVA" value="${estadoActivo}">
                                                                            X
                                                                        </logic:notEqual>
                                                                    </td>
                                                                    <td align="right" width="25px">
                                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorUnitarioPOS}"/>
                                                                    </td>
                                                                    <td align="right" width="35px">
                                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${vistaDetallePedidoDTO.valorTotalVenta}"/>
                                                                    </td>
                                                                </logic:equal>
                                                            </tr>	
                                                        </logic:iterate>
                                                        <tr><td height="15px"></td></tr>
                                                        <logic:equal name="ec.com.smx.sic.sispe.vistaPedido" property="id.codigoEstado" value="${estadoDespachado}">
                                                            <tr align="right">
                                                                <td align="right" colspan="11">
                                                                    <table border="0" cellpadding="0" cellspacing="0" align="right">
                                                                        <tr height="14">
                                                                            <td align="right">SUBTOTAL:&nbsp;</td>
                                                                            <td align="right" width="60px">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalPedido}"/>
                                                                            </td>	
                                                                        </tr>
                                                                        <tr><td colspan="2" align="right">------------------------</td></tr>
                                                                        <tr height="14">
                                                                            <td  align="right">TARIFA 0%:&nbsp;</td>
                                                                            <td align="right">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalNoAplicaIVA}"/>
                                                                            </td>
                                                                        </tr>
                                                                        <tr height="14">
                                                                            <td  align="right">TARIFA&nbsp;<bean:message key="porcentaje.iva"/>%:&nbsp;</td>
                                                                            <td align="right">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.subTotalAplicaIVA}"/>
                                                                            </td>
                                                                        </tr>
                                                                        <tr height="14">
                                                                            <td  align="right"><bean:message key="porcentaje.iva"/>%&nbsp;IVA:&nbsp;</td>
                                                                            <td  align="right">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.ivaPedido}"/>
                                                                            </td>
                                                                        </tr>
                                                                        <tr><td colspan="2" align="right">------------------------</td></tr>
                                                                        <tr height="14">
                                                                            <td  align="right">TOTAL:&nbsp;</td>
                                                                            <td  align="right">
                                                                            	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${ec.com.smx.sic.sispe.vistaPedido.totalPedido}"/>
                                                                            </td>
                                                                        </tr>	                	                    	
                                                                    </table>
                                                                </td>
                                                            </tr>	
                                                        </logic:equal>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </logic:notEmpty>
        </body>
    </html>
</kreport:pdfout>