<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/kreport.tld" prefix="kreport"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<bean:define id="opFechas"><bean:message key="ec.com.smx.sic.sispe.opcion.fechas"/></bean:define>
<bean:define id="opNumPedido"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroPedido"/></bean:define>
<bean:define id="opCodArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.codigoArticulo"/></bean:define>
<bean:define id="opCodLocal"><bean:message key="ec.com.smx.sic.sispe.opcion.codigoLocal"/></bean:define>
<bean:define id="opTodos"><bean:message key="ec.com.smx.sic.sispe.opcion.todos"/></bean:define>
<bean:define id="fechaEntrega"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEntrega"/></bean:define>
<bean:define id="fechaReservacion"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaReservacion"/></bean:define>
<bean:define id="fechaTemporada"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaTemporada"/></bean:define>
<bean:define id="fechaEntre"><bean:message key="ec.com.smx.sic.sispe.opcion.fechaEntre"/></bean:define>

<%------- condici&oacute;n para el foco de la p&aacute;gina --------%>
<c:set var="campo" value="numeroPedido"/>
<html:form action="filtrosDespachoReservaciones" method="post" focus="numeroPedido">
    <TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
        
        <html:hidden property="ayuda" value=""/>
        <%--T&iacute;tulos, botones: cancelar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/reporteDespachosReservacion.gif" border="0"></img></td>
                        <td height="35" valign="middle">Reporte Despacho Reservaci&oacute;n</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="excelA" onclick="enviarFormulario('exportarExcel', 0, false);">Crear XLS</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="pdfA" onclick="enviarFormulario('exportarPDF', 0, false);">Crear PDF</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
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
            <td valign="top">
                <table border="0" class="textos" width="99%" align="center">
                    
                    <tr height="2"><td></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td class="datos" width="20%">
                            <table width="100%" align="center" border="0" class="tabla_informacion" cellpadding="1" cellspacing="0" bgcolor="white">
                                <%-- B&uacute;squeda--%>
                                <tr>
                                    <td class="fila_titulo" colspan="3">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td width="15%"><img src="images/buscar24.gif" border="0"/></td>
                                                <td width="85%" align="left">B&uacute;squeda</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td><br /></td></tr>
                                <tr  align="left">
                                    <td class="textoAzul11" colspan="3">C&oacute;digo de Local:</td>
                                </tr>
                                <logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
                                    <tr>
                                        
                                        <td colspan="3">
                                            <table width="100%" align="center" border="0">
                                                <tr>
                                                    <td align="left" id="localResponsable">
                                                        <smx:select property="codigoLocal" styleClass="comboObligatorio">
                                                            <html:option value="">Seleccione</html:option>
                                                            <logic:notEmpty name="sispe.vistaEstablecimientoCiudadLocalDTO">
                                                                <logic:iterate name="sispe.vistaEstablecimientoCiudadLocalDTO" id="vistaEstablecimientoCiudadLocalDTO" indexId="indiceCiudad">
                                                                    <html:option value="ciudad" styleClass="comboDescripcionCiudad">${vistaEstablecimientoCiudadLocalDTO.nombreCiudad}</html:option>
                                                                    <logic:notEmpty name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales">
                                                                        <logic:iterate name="vistaEstablecimientoCiudadLocalDTO" property="vistaLocales" id="vistaLocalDTO" indexId="indiceLocal">
                                                                            <html:option value="${vistaLocalDTO.id.codigoLocal}">&nbsp;&nbsp;&nbsp;${vistaLocalDTO.id.codigoLocal}-${vistaLocalDTO.nombreLocal}</html:option>
                                                                        </logic:iterate>
                                                                    </logic:notEmpty>
                                                                </logic:iterate>
                                                            </logic:notEmpty>
                                                        </smx:select>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr><td colspan="3" class="fila_contenido">&nbsp;</td></tr>
                                    <tr  align="left">
                                        <td width="4%" >
                                            <html:checkbox name="reporteDespachoReservacionForm" property="opcionNumeroPedido" value="${opNumPedido}" onclick="reporteDespachoReservacionForm.numeroPedido.focus();"/>
                                        </td>
                                        <td class="textoAzul11" colspan="2">N&uacute;mero de Pedido:</td>
                                    </tr>
                                    <tr  align="left">
                                        <td width="4%" ></td>
                                        <td colspan="2">
                                            <html:text name="reporteDespachoReservacionForm" property="numeroPedido" styleClass="combos" size="25" maxlength="20" onclick="reporteDespachoReservacionForm.opcionNumeroPedido.checked = true;"/>
                                        </td>
                                    </tr>
                                    <tr><td colspan="3" class="fila_contenido">&nbsp;</td></tr>
                                    <tr  align="left">
                                        <td width="4%">
                                            <html:checkbox name="reporteDespachoReservacionForm" property="opcionCodigoArticulo" value="${opCodArticulo}" onclick="reporteDespachoReservacionForm.codigoArticulo.focus();"/>
                                        </td>
                                        <td class="textoAzul11" colspan="2">C&oacute;digo de Art&iacute;culo:</td>
                                    </tr>
                                    <tr  align="left">
                                        <td width="4%" ></td>
                                        <td colspan="2">
                                            <html:text name="reporteDespachoReservacionForm" property="codigoArticulo" styleClass="combos" size="25" maxlength="20" onclick="reporteDespachoReservacionForm.opcionCodigoArticulo.checked = true;"/>
                                        </td>
                                    </tr>
                                </logic:notEmpty>
                                <tr><td colspan="3" class="fila_contenido">&nbsp;</td></tr>
                                <tr  align="left">
                                    <td width="4%">
                                        <html:checkbox name="reporteDespachoReservacionForm" property="opcionFecha" value="${opFechas}" />
                                    </td>
                                    <td align="left" width="22%" class="textoAzul11">Fecha:</td>
                                    <td width="74%">
                                        <html:select property="fecha" styleClass="combos" onclick="reporteDespachoReservacionForm.opcionFecha.checked = true;">
                                            <html:option value="">Seleccione</html:option>
                                            <html:option value="${fechaReservacion}">Reservaci&oacute;n</html:option>
                                            <html:option value="${fechaEntrega}">Primera Fecha Despacho</html:option>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="4%">
                                        <html:radio name="reporteDespachoReservacionForm" property="opcionesFecha" value="${fechaTemporada}" />
                                    </td>	
                                    <td align="left" class="textoAzul11" colspan="2">En Temporada</td>
                                </tr>
                                <tr>
                                    <td width="4%">
                                        <html:radio name="reporteDespachoReservacionForm" property="opcionesFecha" value="${fechaEntre}" onclick="reporteDespachoReservacionForm.fechaInicial.focus();"/>
                                    </td>	
                                    <td align="left" class="textoAzul11" colspan="2">Entre: (aaaa-mm-dd)</td>
                                </tr>
                                <tr>	
                                    <td colspan ="3"> 
                                        <table border="0" width="100%">
                                            <tr>
                                                <td width="10%">&nbsp;</td>
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0">
                                                        <tr>
                                                            <td width="30%" class="textoNegro11" align="left">Inicial:</td>
                                                            <td align="left">
                                                                <table border="0" cellspacing="0">
                                                                    <tr>
                                                                        <td class="textoAzul11">
                                                                            <html:text name="reporteDespachoReservacionForm" property="fechaInicial" styleClass="combos" size="12" maxlength="10"/>
                                                                        </td>
                                                                        <td>
                                                                            <smx:calendario property="fechaInicial" key="formatos.fecha"/>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td width="10%">&nbsp;</td>	
                                                <td>
                                                    <table border="0" width="100%" cellspacing="0">
                                                        <tr>
                                                            <td width="30%" class="textoNegro11" align="left">Final:&nbsp;</td>
                                                            <td align="left">
                                                                <table border="0" cellspacing="0">
                                                                    <tr>
                                                                        <td class="textoAzul11">
                                                                            <html:text name="reporteDespachoReservacionForm" property="fechaFinal" styleClass="combos" size="12" maxlength="10" onkeypress="requestAjaxEnter('anulacionPedido.do', ['datosBusqueda','mensajes'], {parameters: 'botonBuscar=ok'});"/>
                                                                        </td>
                                                                        <td>
                                                                            <smx:calendario property="fechaFinal" key="formatos.fecha"/>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr><td colspan="3">&nbsp;</td></tr>
                                            <%--Bot&oacute;n Buscar--%>
                                            <tr>
                                                <td>&nbsp;</td>
                                                <td align="right" colspan="2">
                                                    <div id="botonD">	
                                                        <html:hidden property="botonBuscar" value=""/>
                                                        <html:link styleClass="buscarD" href="#" onclick="requestAjax('filtrosDespachoReservaciones.do',['datosBusqueda','mensajes'],{parameters: 'botonBuscar=ok',popWait:true})">Buscar</html:link>
                                                        <%--<html:link styleClass="buscarD" href="#" onclick="window.open('reporteDespachoReservaciones.do','Reporte','menubar=no,directories=no,location=no,toolbar=no,scrollbars=yes,titlebar=no,top=0,left=0,height=700,width=950,resizable=no,status=yes')">Buscar</html:link>--%>
                                                        <%--<html:link styleClass="buscarD" href="#" onclick="requestAjax('reporteDespachoReservaciones.do',['datosBusqueda','mensajes'],{parameters: 'botonBuscar=ok',popWait:true});"
																   onkeypress="requestAjax('reporteDespachoReservaciones.do',['datosBusqueda','mensajes'],{parameters: 'botonBuscar=ok'});">Buscar</html:link>--%>
                                                        <%--<html:link styleClass="buscarD" href="#" onclick="reporteDespachoReservacionForm.botonBuscar.value='ok';reporteDespachoReservacionForm.submit();">Buscar</html:link>--%>
																   
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr><td height="4" colspan="2"></td></tr>
                                        </table>
                                    </td>
                                </tr>	
                            </table>	
                        </td>
                        <td width="2px"></td>
                        <td width="80%" valign="top" id="datosBusqueda">
                            <tiles:insert page="/reportes/produccionPedidos/reporteDespachoReservacion.jsp"/>
                        </td>
                    </tr>	
                </table>
            </td>
        </tr>
    </TABLE>			
</html:form>
<tiles:insert page="/include/bottom.jsp"/>