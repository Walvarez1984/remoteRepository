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

<kreport:pdfout saveToServer="${pathSave}">
    <html page-orientation="H">
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
            
            <table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro10">
                <tr>
                    <td valign="top" font-size="7pt">
                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                            <tr>
                                <td align="left">
                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>&#32;-&#32;
                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/>&#32;
                                    <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
                                </td>
                                <td align="right">Ruc:&#32;<bean:message key="security.CURRENT_COMPANY_RUC"/></td>
                            </tr>
                            <tr><td height="10px" colspan="2"></td></tr>
                        </table>
                    </td>
                </tr>	
                <tr>
                    <td align="center" valign="top" font-size="7pt">
                        <table border="0" cellspacing="0" cellpadding="1" width="100%">
                            <tr>
								<td>
									<table cellpadding="0" cellspacing="0" width="100%">
										<tr>
											<td align="left" width="60px">
												No de pedido:&#32;
											</td>
											<td align="left">
												<bean:write name="ec.com.smx.sic.sispe.pedido.pedidoDTO" property="id.codigoPedido"/>
											</td>
										</tr>
									</table>
								</td>
                            </tr>
                            <tr>
                                <td>
                                    <table cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <td>

                                                <table border="0" width="100%" cellspacing="0" cellpadding="1" align="left">		
                                                    <tr>
                                                        <td width="60px" align="right"></td>
                                                        <td align="left"></td>
                                                    </tr>												
                                                    <tr>
                                                        <td colspan="2" align="left">Datos del contacto:</td>
                                                    </tr>
                                                    <tr><td height="3px" colspan="2"></td></tr>
                                                    <tr>
                                                        <td align="right">CI/Pasaporte:</td>
                                                        <td align="left"><bean:write name="crearPedidoEspecialForm" property="identificacionContacto"/></td>  
                                                    </tr>  
                                                    <tr>   
                                                        <td align="right">Nombre:</td>
                                                        <td align="left"><bean:write name="crearPedidoEspecialForm" property="nombreContacto"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Tel&eacute;fono:</td>
                                                        <td align="left"><bean:write name="crearPedidoEspecialForm" property="telefonoContacto"/></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email:</td>
                                                        <td align="left"><bean:write name="crearPedidoEspecialForm" property="emailContacto"/></td>
                                                    </tr>                                                    
                                                </table>
                                            </td>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.empresarial">
                                                <td valign="top">
                                                    <table border="0" width="100%" cellspacing="0">
                                                        <tr>
                                                            <td width="45px" align="right"></td>
                                                            <td align="left"></td>
                                                        </tr>
                                                        <tr align="center">
                                                            <td colspan="2" align="left">Datos de la empresa:</td>
                                                        </tr>
                                                        <tr><td height="3px" colspan="2"></td></tr>
                                                        <tr>
                                                            <td width="10%" align="right">Ruc:</td>
                                                            
                                                            <td align="left"><bean:write name="crearPedidoEspecialForm" property="rucEmpresa"/></td>  
                                                        </tr>
                                                        <tr> 
                                                            <td width="10%" align="right">Empresa:</td>
                                                            <td align="left"><bean:write name="crearPedidoEspecialForm" property="nombreEmpresa"/></td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </logic:notEmpty>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td height="7px"></td></tr>
                            <tr>
                                <td align="right">
                                    <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td align="left">
                                                Lugar y fecha:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&#32;<bean:write name="ec.com.smx.sic.sispe.pedido.fechaPedido" formatKey="formatos.fecha"/>
                                            </td>
                                            <td align="left">
                                                Elaborado para:&#32;<bean:write name="crearPedidoEspecialForm" property="localResponsable"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <logic:empty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
                                                <td align="left">
                                                    Tel&eacute;fono local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="telefonoLocal"/>
                                                </td>
                                                <td align="left">
                                                    Administrador local:&#32;<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAdministrador"/>
                                                </td>
                                            </logic:empty>
                                            <logic:notEmpty name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal">
                                                <td align="left">
                                                    Tel&eacute;fono local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.telefonoLocal"/>
                                                </td>
                                                <td align="left">
                                                    Administrador local:&#32;<bean:write name="ec.com.smx.sic.sispe.vistaLocalDTO.administradorLocal"/>
                                                </td>
                                            </logic:notEmpty>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr><td><hr/></td></tr> 
                            <tr><td font-size="7pt">Detalle de los art&iacute;culos</td></tr>
                            <tr><td><hr/></td></tr> 
                            <tr>
                                <td>
                                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                        <tr>
                                            <td align="left" width="20px">No</td>
                                            <td align="left">CODIGO</td>
                                            <td align="left" width="150px">ARTICULO</td>
                                            <td align="right" width="40px">CANT.</td>
                                            <td align="right">PESO KG.</td>
                                            <td align="right">V.UNIT.</td>
                                            <td align="right">OBSERVACION</td>
                                        </tr>
                                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol">
                                            <logic:iterate name="ec.com.smx.sic.sispe.pedido.detallePedidoDTOCol" id="detalle" indexId="numPedido">
                                                <bean:define id="fila" value="${numPedido + 1}"/>
                                                <tr valign="top">
                                                    <td align="left" width="20px"><bean:write name="fila"/></td>
                                                    <td align="left"><bean:write name="detalle" property="articuloDTO.codigoBarras"/></td>
                                                    <td align="left"><bean:write name="detalle" property="articuloDTO.descripcionArticulo"/></td>
                                                    <td align="right">		                                                <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado == 0.00}">
	                                                    	<bean:write name="detalle" property="estadoDetallePedidoDTO.cantidadEstado"/>
	                                                    </c:if>	
	                                                    <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado > 0.00}">
	                                                    	NA
	                                                    </c:if>
	                                                </td>
                                                    <td align="right">	                                                	<c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado == 0.00}">
	                                                    	NA
	                                                    </c:if>	
	                                                    <c:if test="${detalle.estadoDetallePedidoDTO.pesoArticuloEstado > 0.00}">
	                                                    	<bean:write name="detalle" property="estadoDetallePedidoDTO.pesoArticuloEstado"/>
	                                                    </c:if>
	                                                	
	                                                </td>  
                                                    <td align="right">
                                                    	<fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${detalle.estadoDetallePedidoDTO.valorUnitarioIVAEstado}"/>
                                                    </td>
                                                    <td align="left">
                                                       <bean:write name="detalle" property="estadoDetallePedidoDTO.observacionArticulo"/>
                                                    </td>
                                                </tr>
                                                <%---------------- secci&oacute;n que muestra las entregas ------------%>
                                            </logic:iterate>
                                        </logic:notEmpty>
                                        <tr><td height="40px" align="right">.&#32;</td></tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <%-------- mensaje final del reporte (depende del tipo de establecimiento)----------------%>
                <tr><td height="40px" align="right">.&#32;</td></tr>
                <tr><td align="left" font-size="8pt">OBSERVACIONES:<br/></td></tr>
                <tr>
                    <td font-size="8pt">
                        <table cellpadding="1" cellspacing="0">
                            <%-------- mensaje para los establecimientos que no son AKI ------------%>
                            <logic:notEqual name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">                            
                                <tr>
                                    <td width="20px">1.- </td>
                                    <td>Para obtener el precio de afiliado, debe presentar la tarjeta de afiliaci&oacute;n (aplican restricciones).</td>
                                </tr>
                                <tr>
                                    <td>2.- </td>
                                    <td>Los precios anotados son de afiliado 
                                    <bean:define id="preciosAfiliado" name="ec.com.smx.sic.sispe.pedido.preciosAfiliado"></bean:define>
									<c:if test="${preciosAfiliado != null}">
										__X__ No afiliado ___</td>
									</c:if>
		
									<c:if test="${preciosAfiliado == null}">
										___ No afiliado __X__</td>
									</c:if>
                                </tr>
                                <tr>
                                    <td>3.- </td>
                                    <td>El pago podra ser: efectivo, tarjeta de cr&eacute;dito, cheque, etc.</td>
                                </tr>
                                <tr>
                                    <td valign="top">4.- </td>
                                    <td>
                                        Para el pago con cheque presentar la tarjeta de afiliaci&oacute;n que deber&aacute; ser del titular de la cuenta<br/>
                                        corriente y el cheque a nombre de Supermaxi, Megamaxi o Corporaci&oacute;n Favorita C.A. (aplican restricciones).
                                    </td>
                                </tr>
                                <tr>
                                    <td>5.- </td>
                                    <td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
                                </tr>
                                <tr>
                                    <td valign="top">6.- </td>
                                    <td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
                                        el RUC y la raz&oacute;n social.
                                    </td>
                                </tr>
                                <tr>
                                    <td>7.- </td>
                                    <td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
                                </tr>
                                <tr>
                                    <td>8.- </td>
                                    <td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
                                </tr>
                                <tr>
                                    <td>9.- </td>
                                    <td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
                                </tr>
                                <tr>
                                    <td>10.- </td>
                                    <td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
                                </tr>
                                <tr>
									<td>11.-</td>
									<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
								</tr>                              
                            </logic:notEqual>
                            <%-------- mensaje para los establecimientos AKI ------------%>
                            <logic:equal name="codigoEstablecimiento" value="${codigoEstablecimientoAki}">
                                <%-------- mensaje para los establecimientos AKI ------------%>
                                <tr>
                                    <td width="20px">1.- </td>
                                    <td>El pago podra ser: efectivo, tarjeta de cr&eacute;dito, cheque, etc.</td>
                                </tr>
                                <tr>
                                    <td>2.- </td>
                                    <td>En caso de efectuarse retenci&oacute;n a la fuente deber&aacute; registrarse a nombre de Corporaci&oacute;n Favorita C.A.</td>
                                </tr>
                                <tr>
                                    <td valign="top">3.- </td>
                                    <td>Si se va a requerir la factura debe canjearse la nota de venta, acercandose a servicios al cliente y presentar<br/>
                                        el RUC y la raz&oacute;n social.
                                    </td>
                                </tr>
                                <tr>
                                    <td>4.- </td>
                                    <td>La confirmaci&oacute;n del cliente para surtir esta proforma, debe ser con un m&iacute;nimo de 72 horas de anticipaci&oacute;n.</td>
                                </tr>
                                <tr>
                                    <td>5.- </td>
                                    <td>Al confirmar debe ser cancelado el valor total de la proforma.</td>
                                </tr>
                                <tr>
                                    <td>6.- </td>
                                    <td>La mercader&iacute;a est&aacute; sujeta a disponibilidad.</td>
                                </tr>
                                <tr>
                                    <td>7.- </td>
                                    <td>Los precios ser&aacute;n ajustados a favor del cliente.</td>
                                </tr>
                                <tr>
									<td>8.-</td>
									<td>Por la restricci&oacute;n de la ley, EST&Aacute; PROHIBIDA LA VENTA Y ENTREGA DE LICORES LOS D&Iacute;AS DOMINGOS</td>
								</tr> 
                            </logic:equal>
                        </table>
                    </td>
                </tr>
                <tr><td height="40px" align="right">.&#32;</td></tr>
                <tr><td align="left" font-size="8pt">Firma y sello: _________________________________&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;Elaborado por: <bean:write name="vistaEntidadResponsableDTO" property="nombreCompletoUsuario"/></td></tr>
            </table>
        </body>
    </html>
</kreport:pdfout>