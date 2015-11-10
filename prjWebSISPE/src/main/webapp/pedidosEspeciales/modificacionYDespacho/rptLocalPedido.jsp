<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
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
    </head>
    <body>
        <TABLE  class="textoNegro10" border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td>
                    <table  class="textoNegro12" border="0" cellspacing="0" cellpadding="0" width="100%">
                        <tr>
                            <td><bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/></td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td>PEDIDOS ESPECIALES</td>
                        </tr>
                        <tr><td height="5px"></td></tr>
                        <tr>
                            <td>DETALLE DE LOS PEDIDOS POR LOCAL</td>
                        </tr>
                        <tr><td height="10px"></td></tr>
                    </table>
                </td>
            </tr>
            <tr><td height="15px"></td></tr>
            <tr valign="top">
                <td>
                    <logic:notEmpty name="ec.com.smx.sic.sispe.reporte.pedidoLocal">
                        <table  class="textoNegro10" border="0" cellspacing="0"  align="center" width="100%" cellpadding="1"> 
                            <logic:iterate name="ec.com.smx.sic.sispe.reporte.pedidoLocal" id="vistaReporteGeneralDTO1" indexId="indice" >
                                <logic:notEmpty name="vistaReporteGeneralDTO1" property="detallesReporte">
                                    <tr>
                                        <td align="left" width="35%">Local</td>
                                        <td align="left" width="15%">Cantidad total</td>
                                        <td align="center" >Peso total</td>
                                    </tr>
                                    <tr valign="top">
                                        <td align="left">
                                            <label class="textoNegro12">
                                                <bean:write  name="vistaReporteGeneralDTO1" property="id.codigoAreaTrabajo"/>&nbsp;-
                                                <bean:write  name="vistaReporteGeneralDTO1" property="nombreLocal"/>
                                            </label>
                                        </td>
                                        <td align="left">
                                            <label class="textoNegro12">
                                                &nbsp;<bean:write  name="vistaReporteGeneralDTO1" property="cantidadTotal"/>
                                            </label>
                                        </td>
                                        <td align="center">
                                            <label class="textoNegro12">
                                                <bean:write  name="vistaReporteGeneralDTO1" property="pesoTotal"/>&nbsp;&nbsp;
                                            </label>
                                        </td>
                                    </tr>
                                    <tr valign="top">
                                        <td colspan="4">
                                            <table cellpadding="0" border="0" width="96%" align="center">
                                                <logic:iterate name="vistaReporteGeneralDTO1" property="detallesReporte" id="vistaReporteGeneralDTO2">
                                                    <logic:notEmpty name="vistaReporteGeneralDTO2" property="detallesReporte">
                                                        <tr>
                                                            <td align="left" width="25%">Pedido</td>
                                                            <td align="left" width="35%">Cliente</td>
                                                            <td align="left" width="8%">Cantidad</td>
                                                            <td align="center" width="8%">Peso</td>
                                                            <td width="2%"></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="left"><bean:write  name="vistaReporteGeneralDTO2" property="id.codigoPedido"/></td>                                                            
                                                                <!-- PARA EL CASO DE EMPRESA -->
                                                                <logic:notEmpty name="vistaReporteGeneralDTO2" property="nombreEmpresa">                                                                                    	                                                                                   	
																	<td align="left" width="35%">RUC:
																		<bean:write name="vistaReporteGeneralDTO2" property="rucEmpresa"/>&nbsp;- NE:
																		<bean:write name="vistaReporteGeneralDTO2" property="nombreEmpresa"/>&nbsp;-
																	</td>																	
																</logic:notEmpty>
																	
																<!-- PARA EL CASO DE DATOS DE PERSONA -->
																<logic:notEmpty name="vistaReporteGeneralDTO2" property="nombrePersona">
																	<td align="left" width="35%">
																		<bean:write name="vistaReporteGeneralDTO2" property="tipoDocumentoContacto"/>:
																		<bean:write name="vistaReporteGeneralDTO2" property="numeroDocumentoPersona"/>&nbsp;- NC:
																		<bean:write name="vistaReporteGeneralDTO2" property="nombrePersona"/>&nbsp;															
																	</td>
																</logic:notEmpty>
                                                            <td align="left" width="10%">&nbsp;<bean:write  name="vistaReporteGeneralDTO2" property="cantidadTotal"/></td>
                                                            <td align="center"><bean:write  name="vistaReporteGeneralDTO2" property="pesoTotal"/>&nbsp;</td>
                                                            <td width="2%"></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="left" colspan="4">
                                                                <table cellpadding="0" cellspacing="0" border="0" width="94%" align="center">
                                                                    <tr>
                                                                        <td align="left" width="12%">C&oacute;digo barras</td>
                                                                        <td align="left" width="38%">Descripci&oacute;n</td>
                                                                        <td align="left" width="10%">Cant.</td>
                                                                        <td align="right" width="8%">Peso&nbsp;</td>
                                                                        <td align="left" width="35%">  &nbsp;Observación</td>
                                                                    </tr>
                                                                    <logic:iterate name="vistaReporteGeneralDTO2" property="detallesReporte" id="vistaReporteGeneralDTO3">
                                                                        <tr>
                                                                            <td align="left">
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="codigoBarras"/>
                                                                            </td>
                                                                            <td align="left" >
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="descripcionArticulo"/>
                                                                            </td>
                                                                            <td align="left">
                                                                                &nbsp;<bean:write  name="vistaReporteGeneralDTO3" property="cantidadEstado"/>
                                                                            </td>
                                                                            <td align="right">
                                                                                <bean:write  name="vistaReporteGeneralDTO3" property="pesoArticuloEstado"/>&nbsp;
                                                                            </td>
                                                                            <td align="left">
                                                                            &nbsp;<bean:write  name="vistaReporteGeneralDTO3" property="observacionArticulo"/>
                                                                        </tr>
                                                                    </logic:iterate>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <%--<tr><td><p style="page-break-after: always;"></p></td></tr>--%>
                                                        <tr><td width="40px"></td><tr>
                                                    </logic:notEmpty>
                                                </logic:iterate>
                                            </table>
                                        </td>
                                    </tr>
                                </logic:notEmpty>
                            </logic:iterate>
                        </table>
                    </logic:notEmpty>
                </td>
            </tr>
        </TABLE>
    </body>
</html>