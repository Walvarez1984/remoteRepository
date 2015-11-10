<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>

<%--*********************** CORRESPONDE A LAS OPCIONES DE VISTA ARTICULO***************** ---%>

<html>
    <head>
        <meta http-equiv="Content-Style-Type" content="text/css"/>
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
        <meta HTTP-EQUIV="max-age" CONTENT="0"/>
        <meta HTTP-EQUIV="Expires" CONTENT="0"/>
        
        <style type="text/css">
            .nombreCompania{
            font-size: 20px;
            font-family: Verdana, Arial, Helvetica;
            color: #FF0033;
            font-weight: bolder
            }
            
            .borde{
            border: thin solid #999999;
            }
            
            .titulo{
            font-size: 15px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            font-weight: bolder
            }
            
            .tituloTablas{
            background-color:#333333;
            color: #FFFFFF;
            font-size: 9px;
            font-family: Verdana, Arial, Helvetica;
            font-weight: bold;
            border: thin solid #999999;
            }
            
            .textoNegro10{
            font-size: 10px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            }
            
            .textoNegro9{
            font-family: Verdana, Arial, Helvetica;
            font-size: 9px;
            color: #000000;
            }
            
            .tituloTablasCeleste{
            background-color:#DDEEFF;
            color: #000000;
            font-size: 9px;
            font-style: normal;
            line-height: normal;
            font-family: Verdana;
            font-weight: bolder;
            border: thin solid #999999;
            }
            
            .tituloTablasVerde{
                background-color:#ccffcc;
                color: #000000;
                font-size: 9px;
                font-style: normal;
                line-height: normal;
                font-family: Verdana;
                font-weight: bolder;
                border: thin solid #999999;
            }

            .grisClaro {
            background-color: #eaeaea;
            font-family: Verdana;
            font-size: 11px;
            color: #000000;
            border: thin solid #999999;
            border: thin solid #999999;
            }
            
            .blanco {
            font-family: Verdana;
            font-size: 11px;
            color: #ffffff;
            }
            
            .amarilloClaro10 {
            background-color: #ffffca;
            font-family: Verdana;
            font-size: 9px;
            color: #000000;
            border: thin solid #999999;
            font-weight: bolder
            }
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
                <td class="nombreCompania" colspan="5">
                    CORPORACI&Oacute;N FAVORITA C.A.
                </td>
                
            </tr>
            <tr >
                <td class="titulo" colspan="5">Reporte de Despachos</td>
            </tr>
            <%--Datos de Despachos--%>
            <logic:notEmpty name="ec.com.smx.sic.sispe.articulosReporte">
                <tr><td height="6"></td></tr>
                <tr>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                        <td align="left" class="tituloTablas" colspan="4">Fecha de despacho</td>
                        <td align="right" class="tituloTablas" colspan="4">Pendiente</td>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                        <td align="left" class="tituloTablas" colspan="4">C&oacute;digo barras</td>
                        <td align="left" class="tituloTablas" colspan="2">Descripci&oacute;n</td>
                        <td align="right" class="tituloTablas" colspan="2">Pendiente</td>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                        <td align="left" class="tituloTablas" colspan="3">Local</td>
                        <td align="right" class="tituloTablas" colspan="3">Pendiente</td>
                    </logic:notEmpty>
                    <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                        <td align="left" class="tituloTablas" colspan="3">Local</td>
                        <td align="right" class="tituloTablas" colspan="2">Pendiente</td>
                    </logic:notEmpty>
                </tr>	
                
                <logic:iterate name="ec.com.smx.sic.sispe.articulosReporte" id="vistaArticuloDTO">
                    <tr class="textoNegro10">
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                            <td align="left" class="borde" colspan="4">
                                <bean:write name="vistaArticuloDTO"property="fechaDespachoBodega" formatKey="formatos.fecha"/>
                            </td>
                            <td class="borde" colspan="4">
                                <b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b>
                            </td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                            <td class="grisClaro" align="left" colspan="4" >&nbsp;<bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                            <td class="grisClaro" align="left" colspan="2"><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                            <td class="grisClaro" colspan="2">
                                <b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b>
                            </td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                            <td align="left" class="grisClaro" colspan="3"><bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/></td>
                            <td class="grisClaro" colspan="3">
                                <b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b>
                            </td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                            <td align="left" class="grisClaro" colspan="3"><bean:write name="vistaArticuloDTO" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO" property="nombreLocalOrigen"/></td>
                            <td class="grisClaro" colspan="2">
                                <b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b>
                            </td>
                        </logic:notEmpty>
                    </tr>
                    <tr>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                            <td width="3px" class="blanco" >.</td>
                            <td align="left" class="tituloTablasCeleste" colspan="3">C&oacute;digo barras</td>
                            <td align="left" class="tituloTablasCeleste" colspan="2">Descripci&oacute;n</td>
                            <td align="right" class="tituloTablasCeleste" colspan="2">Pendiente</td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                            <td width="3px" class="blanco" >.</td>
                            <td  align="left" class="tituloTablasCeleste" colspan="5">Fecha de despacho</td>
                            <td align="right" class="tituloTablasCeleste" colspan="2">Pendiente</td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                            <td width="3px" class="blanco">.</td>
                            <td  align="left" class="tituloTablasCeleste" colspan="2">Fecha de despacho</td>
                            <td align="right" class="tituloTablasCeleste" colspan="3">Pendiente</td>
                        </logic:notEmpty>
                        <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                            <td width="3px" class="blanco" >.</td>
                            <td align="left" class="tituloTablasCeleste"  colspan="2">C&oacute;digo barras</td>
                            <td align="left" class="tituloTablasCeleste">Descripci&oacute;n</td>
                            <td align="right" class="tituloTablasCeleste" >Pendiente</td>
                        </logic:notEmpty>
                    </tr>
                    <logic:iterate  name="vistaArticuloDTO" property="colVistaArticuloDTO" id="vistaArticuloDTO1">
                        <tr class="textoNegro10">
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                                <td width="3px" class="blanco">.</td>
                                <td align="left" class="borde" colspan="3">&nbsp;
                                    <bean:write name="vistaArticuloDTO1" property="codigoBarras"/>
                                </td>
                                <td align="left" class="borde" colspan="2">
                                    <bean:write name="vistaArticuloDTO1" property="descripcionArticulo"/>
                                </td>
                                <td align="right" class="borde" colspan="2">
                                    <b> <bean:write name="vistaArticuloDTO1" property="diferenciaCantidadEstado"/></b>
                                </td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                                <td width="3px" class="blanco" >.</td>
                                <td align="left" class="borde" colspan="5" >
                                    <bean:write name="vistaArticuloDTO1"property="fechaDespachoBodega" formatKey="formatos.fecha"/>
                                </td>
                                <td align="right" class="borde" colspan="2">
                                    <b><bean:write name="vistaArticuloDTO1" property="diferenciaCantidadEstado"/></b>
                                </td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                                <td width="3px" class="blanco" >.</td>
                                <td align="left" class="borde" colspan="2">
                                    <bean:write name="vistaArticuloDTO1"property="fechaDespachoBodega" formatKey="formatos.fecha"/>
                                </td>
                                <td align="right" class="borde" colspan="3">
                                    <b><bean:write name="vistaArticuloDTO1" property="diferenciaCantidadEstado"/></b>
                                </td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                                <td width="3px" class="blanco">.</td>
                                <td align="left" class="borde"  colspan="2">
                                    <bean:write name="vistaArticuloDTO1" property="codigoBarras"/>
                                </td>
                                <td align="left" class="borde">
                                    <bean:write name="vistaArticuloDTO1" property="descripcionArticulo"/>
                                </td>
                                <td align="right" class="borde" >
                                    <b> <bean:write name="vistaArticuloDTO1" property="diferenciaCantidadEstado"/></b>
                                </td>
                            </logic:notEmpty>
                        </tr>
                        <tr>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                                <td width="3px" class="blanco">.</td>
                                <td width="3px" class="blanco">.</td>
                                <td align="left" class="amarilloClaro10" colspan="2">Local</td>
                                <td align="left" class="amarilloClaro10" colspan="2">Descripci&oacute;n</td>
                                <td align="right" class="amarilloClaro10" colspan="2">Pendiente</td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                                <td width="3px" class="blanco">.</td>
                                <td width="3px" class="blanco" >.</td>
                                <td align="left" class="amarilloClaro10" colspan="4">Local</td>
                                <td align="center" class="amarilloClaro10" colspan="2">Pendiente</td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                                <td width="3px" class="blanco" >.</td>
                                <td width="3px" class="blanco" >.</td>
                                <td align="left" class="amarilloClaro10" >C&oacute;digo barras</td>
                                <td align="left" class="amarilloClaro10">Descripci&oacute;n</td>
                                <td align="right" class="amarilloClaro10">Pendiente</td>
                                <td align="center" class="amarilloClaro10">Fecha Entrega</td>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                                <td width="3px" class="blanco">.</td>
                                <td width="3px" class="blanco">.</td>
                                <td  align="left" class="amarilloClaro10">Fecha de despacho</td>
                                <td align="right" class="amarilloClaro10">Pendiente</td>
                                <td align="center" class="amarilloClaro10">Fecha Entrega</td>
                            </logic:notEmpty>
                        </tr>
                        <logic:iterate name="vistaArticuloDTO1" property="colVistaArticuloDTO" id="vistaArticuloDTO2">
                            <tr class="textoNegro10">
                                <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                                    <td width="3px" class="blanco" >.</td>
                                    <td width="3px" class="blanco">.</td>
                                    <td class="borde" align="left" colspan="2"><bean:write name="vistaArticuloDTO2" property="id.codigoAreaTrabajo"/></td>
                                    <td class="borde" align="left" colspan="2"><bean:write name="vistaArticuloDTO2" property="nombreLocalOrigen"/></td>
                                    <td class="borde" align="right" colspan="2"><b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b></td>
                                    <%--<td class="borde" align="center"><bean:write name="vistaArticuloDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>--%>
                                </logic:notEmpty>
                                <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                                    <td width="3px" class="blanco">.</td>
                                    <td width="3px" class="blanco" >.</td>
                                    <td class="borde" align="left" colspan="4"><bean:write name="vistaArticuloDTO2" property="id.codigoAreaTrabajo"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO2" property="nombreLocalOrigen"/></td>
                                    <td class="borde" align="right" colspan="2"><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></td>
                                </logic:notEmpty>
                                <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo3">
                                    <td width="3px" class="blanco" >.</td>
                                    <td width="3px" class="blanco" >.</td>
                                    <td class="borde" align="left" >&nbsp;<bean:write name="vistaArticuloDTO2" property="codigoBarras"/></td>
                                    <td class="borde" align="left"><bean:write name="vistaArticuloDTO2" property="descripcionArticulo"/></td>
                                    <td class="borde" align="right"><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></td>
                                    <td class="borde" align="center" ><bean:write name="vistaArticuloDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
                                </logic:notEmpty>
                                <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo4">
                                    <td width="3px" class="blanco" >.</td>
                                    <td width="3px" class="blanco">.</td>
                                    <td align="left" class="borde" >
                                        <bean:write name="vistaArticuloDTO2"property="fechaDespachoBodega" formatKey="formatos.fecha"/>
                                    </td>
                                    <td align="right" class="borde" >
                                        <b><bean:write name="vistaArticuloDTO2" property="diferenciaCantidadEstado"/></b>
                                    </td>
                                    <td class="borde" align="center" ><bean:write name="vistaArticuloDTO2" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
                                </logic:notEmpty>
                            </tr>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo1">
                                <bean:define id="cuartoNivel" value="1"/>
                            </logic:notEmpty>
                            <logic:notEmpty name="ec.com.sic.sispe.reporte.rptdespachoArticulo2">
                                <bean:define id="cuartoNivel" value="1"/>
                            </logic:notEmpty>
                            
                            <logic:notEmpty name="cuartoNivel">
                                <tr class="textoNegro10">
                                    <td width="3px" class="blanco" >.</td>
                                    <td width="3px" class="blanco" >.</td>
                                    <td width="3px" class="blanco" >.</td>
                                    <td align="left" class="tituloTablasVerde">Pedido</td>
                                    <td align="left" class="tituloTablasVerde">Reserva</td>
                                    <td align="left" class="tituloTablasVerde">Lugar Entrega</td>
                                    <td align="center" class="tituloTablasVerde">Fecha Entrega</td>
                                    <td align="right" class="tituloTablasVerde">Pendiente</td>
                                    <logic:iterate name="vistaArticuloDTO2" property="colVistaArticuloDTO" id="vistaArticuloDTO3">
                                        <tr class="textoNegro10">
                                            <td width="3px" class="blanco" >.</td>
                                            <td width="3px" class="blanco" >.</td>
                                            <td width="3px" class="blanco" >.</td>
                                            <td class="borde" align="left" >&nbsp;<bean:write name="vistaArticuloDTO3" property="id.codigoPedido"/></td>
                                            <td class="borde" align="left" ><bean:write name="vistaArticuloDTO3" property="llaveContratoPOS"/></td>
                                            <td class="borde" align="left"><bean:write name="vistaArticuloDTO3" property="codigoLocalReferencia"/>&nbsp;-&nbsp;<bean:write name="vistaArticuloDTO3" property="nombreLocalReferencia"/></td>
                                            <td class="borde" align="center"><bean:write name="vistaArticuloDTO3" property="fechaEntregaCliente" formatKey="formatos.fecha"/></td>
                                            <td class="borde" align="right"><bean:write name="vistaArticuloDTO3" property="diferenciaCantidadEstado"/></td>
                                        </tr>
                                    </logic:iterate>
                                </tr>
                                <tr><td height="5"></td></tr>
                            </logic:notEmpty>
                        </logic:iterate>
                        <tr><td height="10"></td></tr>
                    </logic:iterate>
                </logic:iterate>
            </logic:notEmpty>
        </table>
    </body>
</html>