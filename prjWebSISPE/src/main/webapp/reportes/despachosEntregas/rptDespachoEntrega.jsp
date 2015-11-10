<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dTD">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="Content-Style-Type" content="text/css">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="max-age" CONTENT="0">
        <meta HTTP-EQUIV="Expires" CONTENT="0">
        <link href="../../css/textos.css" rel="stylesheet" type="text/css">
        <title>Reporte</title>
    </head>
    <body>
        <%---------------------- lista de definiciones para las acciones -----------------------------%>
        <bean:define id="despacho"><bean:message key="ec.com.smx.sic.sispe.accion.despacho"/></bean:define>
        <bean:define id="entrega"><bean:message key="ec.com.smx.sic.sispe.accion.entrega"/></bean:define>
        
        <table border="0" cellspacing="0" cellpadding="0" width="100%" class="textoNegro11" align="left">
            <tr>
                <td valign="top">
                    <table border="0" align="left" width="100%" cellpadding="1" cellspacing="0">
                        <tr>
                            <td align="left">
                                <logic:equal name="ec.com.smx.sic.sispe.accion" value="${despacho}">
                                    Reporte de despachos
                                    <bean:define id="etiquetaFecha" value="Fecha de despacho:"/>
                                </logic:equal>
                                <logic:equal name="ec.com.smx.sic.sispe.accion" value="${entrega}">
                                    Reporte de entregas
                                    <bean:define id="etiquetaFecha" value="Fecha de despacho:"/>
                                </logic:equal>			
                            </td>
                        </tr>
                        <tr><td height="5px" colspan="2"></td></tr>
                    </table>
                </td>	
            </tr>	
            <tr>
                <td>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.DespachosEntregas.reporte">
                        <logic:iterate name="ec.com.smx.sic.sispe.DespachosEntregas.reporte" id="ordenDespachoEntregaRDTO" indexId="indexOrdenEntrega">
                            <table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
                                <tr><td height="15px"></td></tr>
                                <tr>
                                    <td align="left" width="98%">Local or&iacute;gen:&nbsp;<bean:write name="ordenDespachoEntregaRDTO" property="descripcionLocal"/></td>
                                </tr>
                                <tr><td><hr/></td></tr>
                                <tr>
                                    <td colspan="2">
                                        <table cellpadding="1" cellspacing="0" width="100%" border="0">
                                            <tr>
                                                <td width="2%">&nbsp;</td>
                                                <td width="98%">
                                                    <logic:notEmpty name="ordenDespachoEntregaRDTO" property="pedidos">
                                                        <logic:iterate name="ordenDespachoEntregaRDTO"  property="pedidos" id="pedidoRDTO" indexId="indexPedido">
                                                            <table cellspacing="0" cellpadding="1" border="0" width="100%">
                                                                <tr><td height="5px"></td></tr>
                                                                <tr>
                                                                    <td align="left" width="98%">No de pedido:&nbsp;<bean:write name="pedidoRDTO" property="numeroPedido"/></td>
                                                                </tr>
                                                                <tr><td height="4px"></td></tr>
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <table cellpadding="0" cellspacing="0" width="100%">
                                                                            <tr>
                                                                                <td colspan="2">
                                                                           			<table cellpadding="0" cellspacing="0" width="100%">    
                                                                                        <tr>
																							<logic:notEmpty name="pedidoRDTO" property="cedulaContacto">
																								<td align="left" width="50%" class="textoNaranja11"><b>Datos del cliente</b></td>
																							</logic:notEmpty>
																							<logic:notEmpty name="pedidoRDTO" property="rucEmpresa">
																								<td align="left" width="50%" class="textoNaranja11"><b>Datos de la empresa</b></td>
																							</logic:notEmpty>
                                                                                        </tr>
                                                                                        <tr><td height="5px" colspan="2"></td></tr>
                                                                                        <tr>
																							<logic:notEmpty name="pedidoRDTO" property="cedulaContacto">
																								<td width="50%">
																									<table cellpadding="1" cellspacing="0" border="0">																					
																										<tr>
																											<td align="right"><b>C&eacute;dula/Pasaporte:</b></td>
																											<td><bean:write name="pedidoRDTO" property="cedulaContacto"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Nombre:</b></td>
																											<td><bean:write name="pedidoRDTO" property="nombreContacto"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Tel&eacute;fono:</b></td>
																											<td><bean:write name="pedidoRDTO" property="telefonoContacto"/></td>
																										</tr>   																								
																									</table>
																								</td>
																							</logic:notEmpty>
																							<logic:notEmpty name="pedidoRDTO" property="rucEmpresa">
																								<td width="50%">
																									<table cellpadding="1" cellspacing="0" border="0">
																									
																										<tr>
																											<td align="right"><b>Ruc:</b></td>
																											<td><bean:write name="pedidoRDTO" property="rucEmpresa"/></td>
																										</tr>
																										<tr>
																											<td align="right"><b>Empresa:</b></td>
																											<td><bean:write name="pedidoRDTO" property="nombreEmpresa"/></td>
																										</tr>
																										<tr><td colspan="2">&nbsp;</td></tr>
																									</table>
																								</td>
																							</logic:notEmpty>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td colspan="2">
                                                                                    <table cellpadding="1" cellspacing="0" width="100%" border="0">
                                                                                        <td width="2%">&nbsp;</td>
                                                                                        <td width="98%">
                                                                                            <table border="0" width="100%" align="left" cellpadding="1" cellspacing="0">
                                                                                                <logic:notEmpty name="pedidoRDTO" property="entregas">
                                                                                                    <logic:iterate name="pedidoRDTO" property="entregas" id="entregaRDTO" indexId="indexEntrega">
                                                                                                        <tr><td height="5px"></td></tr>
                                                                                                        <tr>                                                                                             
                                                                                                            <td width="100%" align="left" class="textoNegro10">Lugar entrega:&nbsp;<bean:write name="entregaRDTO" property="lugarEntrega"/></td>
                                                                                                        </tr>
                                                                                                        <tr>
                                                                                                            <td>
                                                                                                                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                    <tr>
                                                                                                                        <td width="2%">&nbsp;</td>
                                                                                                                        <td width="98%">
                                                                                                                            <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                                <logic:notEmpty name="entregaRDTO" property="entregasFecha">
                                                                                                                                    <logic:iterate name="entregaRDTO" property="entregasFecha" id="entregaFechaRDTO" indexId="indexEntregaFecha">
                                                                                                                                        <tr><td height="5px"></td></tr>
                                                                                                                                        <tr>
                                                                                                                                            <td width="98%" align="left" class="textoNegro10">${etiquetaFecha}&nbsp;<bean:write name="entregaFechaRDTO" property="fechaDespachoEntrega"/></td>
                                                                                                                                        </tr>
                                                                                                                                        <tr>
                                                                                                                                            <td>
                                                                                                                                                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                                                    <tr>
                                                                                                                                                        <td width="2%">&nbsp;</td>
                                                                                                                                                        <td width="98%">
                                                                                                                                                            <table border="0" width="100%" cellpadding="1" cellspacing="0" class="textoNegro10">
                                                                                                                                                                <logic:notEmpty name="entregaFechaRDTO" property="beneficiarios">
                                                                                                                                                                    <logic:iterate name="entregaFechaRDTO" property="beneficiarios" id="beneficiarioRDTO">
                                                                                                                                                                        <tr><td height="5px" colspan="2"></td></tr>
                                                                                                                                                                        <tr><td valign="bottom" colspan="2">Listado de art&iacute;culos:</td></tr>
                                                                                                                                                                        <tr>
                                                                                                                                                                            <td colspan="2">
                                                                                                                                                                                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                                                                                    <tr>
                                                                                                                                                                                        <td align="left" width="25%">C&Oacute;DIGO BARRAS</td>
                                                                                                                                                                                        <td align="left" width="60%">DESCRIPCI&Oacute;N</td>
                                                                                                                                                                                        <td width="15%" align="right">CANTIDAD</td>
                                                                                                                                                                                    </tr>
                                                                                                                                                                                    <tr><td height="5px"></td></tr>
                                                                                                                                                                                    <logic:notEmpty name="beneficiarioRDTO" property="articulos">	
                                                                                                                                                                                        <logic:iterate name="beneficiarioRDTO" property="articulos" id="articuloRDTO" indexId="indiceArticulo">
                                                                                                                                                                                            <tr>
                                                                                                                                                                                                <td align="left"><bean:write name="articuloRDTO" property="codigoBarras"/></td>
                                                                                                                                                                                                <td align="left"><bean:write name="articuloRDTO" property="descripcionArticulo"/></td>
                                                                                                                                                                                                <td align="right"><bean:write name="articuloRDTO" property="cantidadArticulo"/></td>
                                                                                                                                                                                            </tr>
                                                                                                                                                                                        </logic:iterate>
                                                                                                                                                                                    </logic:notEmpty>
                                                                                                                                                                                </table>	
                                                                                                                                                                            </td>
                                                                                                                                                                        </tr>
                                                                                                                                                                        <tr>
                                                                                                                                                                            <td height="40px" align="left"></td>
                                                                                                                                                                            <td valign="middle" align="left">Firma:_______________</td>
                                                                                                                                                                        </tr>
                                                                                                                                                                    </logic:iterate>
                                                                                                                                                                </logic:notEmpty>
                                                                                                                                                            </table>
                                                                                                                                                        </td>
                                                                                                                                                    </tr>
                                                                                                                                                </table>
                                                                                                                                            </td>
                                                                                                                                        </tr>
                                                                                                                                    </logic:iterate>
                                                                                                                                </logic:notEmpty>
                                                                                                                            </table>
                                                                                                                        </td>
                                                                                                                    </tr>
                                                                                                                </table>
                                                                                                            </td>
                                                                                                        </tr>
                                                                                                    </logic:iterate>
                                                                                                </logic:notEmpty>									
                                                                                            </table>
                                                                                        </td>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </logic:iterate>
                                                    </logic:notEmpty>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>	
                        </logic:iterate>
                    </logic:notEmpty>
                </td>		
            </tr>	
        </table>
    </body>
</html>
