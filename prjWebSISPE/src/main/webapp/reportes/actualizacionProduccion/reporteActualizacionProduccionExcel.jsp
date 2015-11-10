<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@page contentType="application/ms-excel" language="java"%>
<%response.addHeader("Content-Disposition", "attachment; filename=\""+request.getAttribute("ec.com.smx.sic.sispe.reporte.nombreArchivo")+"\"");%>

<c:set var="localAnterior" value=""/>

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
            font-weight: bolder;
            }
            
            .borde{
            border: thin solid #999999;
            }
            
            .titulo{
            font-size: 15px;
            font-family: Verdana, Arial, Helvetica;
            color: #000000;
            font-weight: bolder;
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
            font-weight: bolder;
            }
            .morado10 {
            background-color: #dae2e9;
            font-family: Verdana;
            font-size: 9px;
            color: #000000;
            border: thin solid #999999;
            font-weight: bolder;
            }
            
        </style>
    </head>
    <body>
        <table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
            <tr>
                <td class="nombreCompania" colspan="6">
                    CORPORACIÓN FAVORITA C.A.
                </td>
            </tr>
            <tr >
                <td colspan="6" class="titulo">REPORTE DE ART&Iacute;CULOS CON PRODUCCI&Oacute;N PENDIENTE</td>
            </tr>
            
            <%--Datos de Produccion--%>
            <logic:notEmpty name="ec.com.smx.sic.sispe.articulos">
                <tr><td height="6"></td></tr>
                
                <tr align="left">
                    <td class="tituloTablas" align="left" colspan="3" >C&Oacute;DIGO BARRAS</td>
                    <td class="tituloTablas" align="left" >ART&Iacute;CULO</td>
                    <td class="tituloTablas" align="left" >UND. MANEJO</td>
                    <td class="tituloTablas" align="center" >PENDIENTE</td>
                    <td class="tituloTablas" align="right" >FECHA DESPACHO</td>
                </tr>
                <!--
                               <tr align="left" class="tituloTablas">
                                   <td align="center" class="columna_contenido" width="50px">TOTAL</td>
                                   <td align="center" class="columna_contenido" width="50px">PRODUCIDO</td>
                                   <td align="center" class="columna_contenido" width="50px">PENDIENTE</td>
                               </tr>
                               -->
                <logic:iterate name="ec.com.smx.sic.sispe.articulos" id="vistaArticuloDTO" indexId="indiceArticulo">
                    <tr class="textoNegro10"> 
                        <td class="borde" align="left" colspan="3">&nbsp;<bean:write name="vistaArticuloDTO" property="codigoBarras"/></td>
                        <td class="borde" align="left" ><bean:write name="vistaArticuloDTO" property="descripcionArticulo"/></td>
                        <td class="borde" align="left" ><bean:write name="vistaArticuloDTO" property="unidadManejo"/></td>
                        <%--<td class=" " align="center" width="50px"><bean:write name="vistaArticuloDTO" property="cantidadReservadaEstado"/></td>   					    
                        <td class=" " align="center" width="50px"><bean:write name="vistaArticuloDTO" property="cantidadParcialEstado"/></td>--%>
                        <td class="borde" align="center"><b><bean:write name="vistaArticuloDTO" property="diferenciaCantidadEstado"/></b></td>
                        <td class="borde" align="right" ><bean:write name="vistaArticuloDTO" property="fechaDespachoBodega" formatKey="formatos.fecha"/></td>  					       							    
                    </tr> 
                    
                    <logic:notEmpty name="vistaArticuloDTO" property="articulos">
                        <tr align="left">
                            <td width="3px" class="blanco">.</td>
                            
                            <td class="tituloTablasCeleste" align="left" colspan="2">C&Oacute;DIGO BARRAS</td>
                            <td class="tituloTablasCeleste" align="left" colspan="4">ART&Iacute;CULO</td>
                        </tr>
                        <logic:iterate name="vistaArticuloDTO" property="articulos" id="articuloDTO">
                            <tr class="textoNegro10"> 
                                <td width="3px" class="blanco">.</td>
                                
                                <td class="borde" align="left" colspan="2">&nbsp;<bean:write name="articuloDTO" property="codigoBarrasActivo.id.codigoBarras"/></td>
                                <td class="borde" align="left" colspan="4"><bean:write name="articuloDTO" property="descripcionArticulo"/></td>
                            </tr>
                        </logic:iterate>
                        <tr><td height="12"></td></tr>   
                    </logic:notEmpty>
                    <logic:notEmpty name="vistaArticuloDTO" property="npDesplegarItemsReceta">
                        <tr align="left">
                            <td width="3px" class="blanco">.</td>
                            <td class="amarilloClaro10" align="left" colspan="2">C&Oacute;DIGO BARRAS</td>
                            <td class="amarilloClaro10" align="left" >ART&Iacute;CULO</td>
                            <td class="amarilloClaro10" align="center">CANTIDAD</td>
                            <td class="amarilloClaro10" align="center" colspan="2" >CANT. MAX. PROD.</td>
                        </tr>
                        <logic:iterate name="vistaArticuloDTO" property="recetaArticulos" id="recetaArticuloDTO" indexId="indiceDetalleArticulo">
                            <logic:notEmpty name="recetaArticuloDTO" property="articulo">
                                <tr class="textoNegro10"> 
                                    <td width="3px" class="blanco">.</td>
                                    <td class="borde" align="left"colspan="2">&nbsp;<bean:write name="recetaArticuloDTO" property="articuloRelacion.codigoBarrasActivo.id.codigoBarras"/></td>
                                    <td class="borde" align="left" ><bean:write name="recetaArticuloDTO" property="articuloRelacion.descripcionArticulo"/></td>
                                    <td class="borde" align="center"><bean:write name="recetaArticuloDTO" property="cantidad"/></td>																								
                                    <td class="borde" align="center" colspan="2">
                                        <bean:write name="recetaArticuloDTO" property="npCantidadMaximaProducir"/>
                                    </td>
                                </tr>
                                
                            </logic:notEmpty>
                        </logic:iterate>
                        
                    </logic:notEmpty>		
                </logic:iterate>
                <tr><td height="12"></td></tr>      
            </logic:notEmpty>
        </table>
    </body>
</html>