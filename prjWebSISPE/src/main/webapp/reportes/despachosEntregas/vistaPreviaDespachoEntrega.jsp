<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<% long id = (new java.util.Date()).getTime();   %>

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
        <link href="css/textos.css" rel="stylesheet" type="text/css">
        <link href="css/componentes.css" rel="stylesheet" type="text/css">
        <link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
        <link href="css/estilosBotones.css" rel="stylesheet" type="text/css">
        <script language="JavaScript" src="js/util/util.js" type="text/javascript"></script>
        <%----- solo cuando se imprime una orden de despacho o entrega -------%>
        <link rel=alternate media=print href="reportes/despachosEntregas/rptDespachoEntrega.jsp?id=<%=id%>">
        
        <bean:define id="despacho"><bean:message key="ec.com.smx.sic.sispe.accion.despacho"/></bean:define>
        <bean:define id="entrega"><bean:message key="ec.com.smx.sic.sispe.accion.entrega"/></bean:define>
        <title>
            <logic:equal name="ec.com.smx.sic.sispe.accion" value="${despacho}">
                Reporte de despachos
                <bean:define id="tituloBarra" value="Reporte del despacho de pedidos"/>
                <bean:define id="iconoBarra" value="despachoPedidos.gif"/>
                <bean:define id="etiquetaFecha" value="Fecha de despacho:"/>
            </logic:equal>
            <logic:equal name="ec.com.smx.sic.sispe.accion" value="${entrega}">
                Reporte de entregas
                <bean:define id="tituloBarra" value="Reporte de la entrega de pedidos"/>
                <bean:define id="iconoBarra" value="entregar.gif"/>
                <bean:define id="etiquetaFecha" value="Fecha de entrega:"/>
            </logic:equal>
        </title>
    </head>
    <body>
        <%---------------------- lista de definiciones para las acciones -----------------------------%>
        <table border="0" cellspacing="0" cellpadding="1" width="100%" class="textoNegro11">
            <html:form action="entregaPedido" method="post">
			<input type="hidden" name="ayuda" value="">
            <%-- barra principal --%>
            <tr>
                <td align="left" valign="top" width="100%">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                        <tr>
                            <td  width="3%" align="center"><img src="images/${iconoBarra}" border="0"></img></td>
                            <td height="35" valign="middle">${tituloBarra}</td>
                            <td align="right" valign="top">
                                <table border="0" cellpadding="1" cellspacing="0">
                                    <tr>
                                        <td>
											<div id="botonA">
												<html:link href="#" styleClass="excelA" onclick="enviarFormulario('xls', 0, false);">Crear XLS</html:link>
											</div>
										</td>
                                        <td>
                                            <div id="botonA">	
                                                <html:link href="#" styleClass="imprimirA" onclick="window.print();">Imprimir</html:link>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>	
            <tr>
                <td>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.DespachosEntregas.reporte">
                        <logic:iterate name="ec.com.smx.sic.sispe.DespachosEntregas.reporte" id="ordenDespachoEntregaRDTO" indexId="indexOrdenEntrega">
                            <table border="0" width="100%" cellspacing="0" cellpadding="0" class="textoNegro11">
                                <tr><td height="10px" colspan="2"></td></tr>
                                <tr>
                                    <td width="2%">
                                        <div style="display:none" id="desplegar_${indexOrdenEntrega}">
                                            <img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indexOrdenEntrega}']);show(['plegar_${indexOrdenEntrega}','sec_pedidos_${indexOrdenEntrega}']);">
                                        </div>
                                        <div style="display:block" id="plegar_${indexOrdenEntrega}">
                                            <img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indexOrdenEntrega}','sec_pedidos_${indexOrdenEntrega}']);show(['desplegar_${indexOrdenEntrega}']);">
                                        </div>
                                    </td>
                                    <td align="left" width="98%"><b><label class="textoAzul11">Local origen:&nbsp;</label></b><bean:write name="ordenDespachoEntregaRDTO" property="descripcionLocal"/></td>
                                </tr>
                                <tr>
                                    <td colspan="2" id="sec_pedidos_${indexOrdenEntrega}">
                                        <table cellpadding="1" cellspacing="0" width="100%" border="0" class="tabla_informacion" bgcolor="#E8F2FF">
                                            <tr>
                                                <td width="2%">&nbsp;</td>
                                                <td width="98%">
                                                    <logic:notEmpty name="ordenDespachoEntregaRDTO" property="pedidos">
                                                        <logic:iterate name="ordenDespachoEntregaRDTO"  property="pedidos" id="pedidoRDTO" indexId="indexPedido">
                                                            <table cellspacing="0" cellpadding="1" border="0" width="100%">
                                                                <tr><td height="5px" colspan="2"></td></tr>
                                                                <tr>
                                                                    <td width="2%">
                                                                        <div style="display:none" id="desplegar_${indexOrdenEntrega}_${indexPedido}">
                                                                            <img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indexOrdenEntrega}_${indexPedido}']);show(['plegar_${indexOrdenEntrega}_${indexPedido}','sec_entregas_${indexOrdenEntrega}_${indexPedido}']);">
                                                                        </div>
                                                                        <div style="display:block" id="plegar_${indexOrdenEntrega}_${indexPedido}">
                                                                            <img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indexOrdenEntrega}_${indexPedido}','sec_entregas_${indexOrdenEntrega}_${indexPedido}']);show(['desplegar_${indexOrdenEntrega}_${indexPedido}']);">
                                                                        </div>
                                                                    </td>
                                                                    <td align="left" width="98%"><b><label class="textoRojo11">No de pedido:&nbsp;</label></b><bean:write name="pedidoRDTO" property="numeroPedido"/></td>
                                                                </tr>
                                                                <tr><td height="2px" colspan="2"></td></tr>
                                                                <tr>
                                                                    <td colspan="2" id="sec_entregas_${indexOrdenEntrega}_${indexPedido}">
                                                                        <table cellpadding="0" cellspacing="0" width="100%" class="tabla_informacion" bgcolor="#f3ebe4">
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
                                                                                        <tr>
                                                                                            <td width="2%">&nbsp;</td>
                                                                                            <td width="98%">
                                                                                                <table border="0" width="100%" align="left" cellpadding="1" cellspacing="0">
                                                                                                    <logic:notEmpty name="pedidoRDTO" property="entregas">
                                                                                                        <logic:iterate name="pedidoRDTO" property="entregas" id="entregaRDTO" indexId="indexEntrega">
                                                                                                            <tr><td height="5px" colspan="2"></td></tr>
                                                                                                            <tr>
                                                                                                                <td width="2%">
                                                                                                                    <div style="display:none" id="desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}">
                                                                                                                        <img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}']);show(['plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}','sec_fechasEntrega_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}']);">
                                                                                                                    </div>
                                                                                                                    <div style="display:block" id="plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}">
                                                                                                                        <img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}','sec_fechasEntrega_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}']);show(['desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}']);">
                                                                                                                    </div>
                                                                                                                </td>                                                                                                
                                                                                                                <td width="98%" align="left" class="textoNegro10"><b><label class="textoVerde11">Lugar entrega:&nbsp;</label></b><bean:write name="entregaRDTO" property="lugarEntrega"/></td>
                                                                                                            </tr>
                                                                                                            <tr>
                                                                                                                <td colspan="2" id="sec_fechasEntrega_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}">
                                                                                                                    <table border="0" width="100%" cellpadding="1" cellspacing="0" class="tabla_informacion" bgcolor="#ECF9E6">
                                                                                                                        <tr>
                                                                                                                            <td width="2%">&nbsp;</td>
                                                                                                                            <td width="98%">
                                                                                                                                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                                    <logic:notEmpty name="entregaRDTO" property="entregasFecha">
                                                                                                                                        <logic:iterate name="entregaRDTO" property="entregasFecha" id="entregaFechaRDTO" indexId="indexEntregaFecha">
                                                                                                                                            <tr><td height="5px"></td></tr>
                                                                                                                                            <tr>
                                                                                                                                                <td width="2%">
                                                                                                                                                    <div style="display:none" id="desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}">
                                                                                                                                                        <img src="images/desplegar.gif" border="0" onclick="hide(['desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}']);show(['plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}','sec_articulos_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}']);">
                                                                                                                                                    </div>
                                                                                                                                                    <div style="display:block" id="plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}">
                                                                                                                                                        <img src="images/plegar.gif" border="0" onclick="hide(['plegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}','sec_articulos_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}']);show(['desplegar_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}']);">
                                                                                                                                                    </div>
                                                                                                                                                </td>                                                                                                
                                                                                                                                                <td width="98%" align="left" class="textoNegro10"><b><label class="textoCafe11">${etiquetaFecha}&nbsp;</label></b><bean:write name="entregaFechaRDTO" property="fechaDespachoEntrega"/></td>
                                                                                                                                            </tr>
                                                                                                                                            <tr>
                                                                                                                                                <td colspan="2" id="sec_articulos_${indexOrdenEntrega}_${indexPedido}_${indexEntrega}_${indexEntregaFecha}">
                                                                                                                                                    <table border="0" width="100%" cellpadding="1" cellspacing="0" class="tabla_informacion" bgcolor="#eff1f1">
                                                                                                                                                        <tr>
                                                                                                                                                            <td width="2%">&nbsp;</td>
                                                                                                                                                            <td width="98%">
                                                                                                                                                                <table border="0" width="100%" cellpadding="1" cellspacing="0">
                                                                                                                                                                    <logic:notEmpty name="entregaFechaRDTO" property="beneficiarios">
                                                                                                                                                                        <logic:iterate name="entregaFechaRDTO" property="beneficiarios" id="beneficiarioRDTO">
                                                                                                                                                                            <%--<logic:notEmpty name="beneficiarioRDTO" property="cedulaBeneficiario">
                                                                                                                                                                                <tr>
                                                                                                                                                                                    <td width="2%"></td>
                                                                                                                                                                                    <td class="textoAzul11" width="23%"><b>C&eacute;dula Beneficiario:</b></td>
                                                                                                                                                                                    <td align="left"><bean:write name="beneficiarioRDTO" property="cedulaBeneficiario"/></td>
                                                                                                                                                                                </tr>
                                                                                                                                                                                <tr>
                                                                                                                                                                                    <td width="2%"></td>
                                                                                                                                                                                    <td class="textoAzul11" width="23%"><b>Nombre Beneficiario:</b></td>
                                                                                                                                                                                    <td align="left"><bean:write name="beneficiarioRDTO" property="nombreBeneficiario"/></td>
                                                                                                                                                                                </tr>
                                                                                                                                                                            </logic:notEmpty>--%>
                                                                                                                                                                            <tr><td height="5px" colspan="2"></td></tr>
                                                                                                                                                                            <tr>
                                                                                                                                                                                <td valign="bottom"><b>Listado de art&iacute;culos:</b></td>
                                                                                                                                                                            </tr>
                                                                                                                                                                            <tr>
                                                                                                                                                                                <td>
                                                                                                                                                                                    <table border="0" width="100%" class="tabla_informacion" cellpadding="1" cellspacing="0">
                                                                                                                                                                                        <tr class="tituloTablas" align="left">
                                                                                                                                                                                            <td align="left" width="25%" class="columna_contenido">C&Oacute;DIGO BARRAS</td>
                                                                                                                                                                                            <td align="left" width="60%" class="columna_contenido">DESCRIPCI&Oacute;N</td>
                                                                                                                                                                                            <td width="15%" align="right" class="columna_contenido">CANTIDAD</td>
                                                                                                                                                                                        </tr>
                                                                                                                                                                                        <logic:notEmpty name="beneficiarioRDTO" property="articulos">	
                                                                                                                                                                                            <logic:iterate name="beneficiarioRDTO" property="articulos" id="articuloRDTO" indexId="indiceArticulo">
                                                                                                                                                                                                <%--------- control del estilo para el color de las filas --------------%>
                                                                                                                                                                                                <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                                                                                                                                                                <logic:equal name="residuo" value="0">
                                                                                                                                                                                                    <bean:define id="clase" value="blanco10"/>
                                                                                                                                                                                                </logic:equal>
                                                                                                                                                                                                <logic:notEqual name="residuo" value="0">
                                                                                                                                                                                                    <bean:define id="clase" value="grisClaro10"/>
                                                                                                                                                                                                </logic:notEqual>
                                                                                                                                                                                                <tr class="${clase}">
                                                                                                                                                                                                    <td align="left" class="columna_contenido fila_contenido"><bean:write name="articuloRDTO" property="codigoBarras"/></td>
                                                                                                                                                                                                    <td align="left" class="columna_contenido fila_contenido"><bean:write name="articuloRDTO" property="descripcionArticulo"/></td>
                                                                                                                                                                                                    <td align="right" class="columna_contenido fila_contenido"><bean:write name="articuloRDTO" property="cantidadArticulo"/></td>
                                                                                                                                                                                                </tr>
                                                                                                                                                                                            </logic:iterate>
                                                                                                                                                                                        </logic:notEmpty>
                                                                                                                                                                                    </table>	
                                                                                                                                                                                </td>
                                                                                                                                                                            </tr>
                                                                                                                                                                            <tr><td height="5px"></td></tr>
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
                                                                                        </tr>
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
          </html:form>
        </table>
    </body>
</html>