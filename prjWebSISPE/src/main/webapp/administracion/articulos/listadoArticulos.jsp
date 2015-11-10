<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../../include/top.jsp"/>

<bean:define id="opNumeroClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.numeroClasificacion"/></bean:define>
<bean:define id="opNombreClasificacion"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreClasificacion"/></bean:define>
<bean:define id="opNombreArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.nombreArticulo"/></bean:define>
<bean:define id="opCodigoArticulo"><bean:message key="ec.com.smx.sic.sispe.opcion.codigoArticulo"/></bean:define>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <html:form action="mantenimientoArticulos" method="post">
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="./images/articulos.gif" border="0"></img></td>
                        <td height="35" valign="middle">Mantenimiento de Art&iacute;culos</td>
                        <td align="right" valign="top">
                            <table border="0" cellpadding="1" cellspacing="0">
                                <tr>
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
        <%--Cuerpo--%>
        <tr>
            <td align="center" valign="top">
                <table border="0" class="textoNegro12" width="99%" align="center" cellspacing="0" cellpadding="0">
                    <tr><td height="7px" colspan="3"></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td align="center" width="20%" valign="top">
                            <table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0" bgcolor="white">
                                <%-- B&oacute;squeda--%>
                                <tr>
                                    <td class="fila_titulo" colspan="2">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td width="15%"><img src="./images/buscar24.gif" border="0"/></td>
                                                <td width="85%" align="left">B&oacute;squeda</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <%--C&oacute;digo de clasificaci&oacute;n--%>
                                <tr>
                                    <td width="2%">
                                        <html:radio property="opTipoBusqueda" value="${opNumeroClasificacion}" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);document.forms[0].textoBusqueda.focus();"/>
                                    </td>
                                    <td align="left" class="textoAzul11" width="18%" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);chequear(document.forms[0].opTipoBusqueda[0]);document.forms[0].textoBusqueda.focus();">C&oacute;digo Clasificaci&oacute;n</td>
                                </tr>
                                <%--Descripcion de la clasificaci&oacute;n--%>
                                <tr>
                                    <td>
                                        <html:radio property="opTipoBusqueda" value="${opNombreClasificacion}" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);document.forms[0].textoBusqueda.focus();"/>
                                    </td>
                                    <td align="left"  class="textoAzul11" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);chequear(document.forms[0].opTipoBusqueda[1]);document.forms[0].textoBusqueda.focus();">Descripci&oacute;n Clasificaci&oacute;n</td>
                                </tr>
                                <%-- Descripcion Art&iacute;culo --%>
                                <tr>
                                    <td>
                                        <html:radio property="opTipoBusqueda" value="${opNombreArticulo}" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);document.forms[0].textoBusqueda.focus();"/>
                                    </td>
                                    <td align="left" class="textoAzul11" onclick="show(['txt_busqueda1','boton_busqueda1']);hide(['txt_busqueda2','boton_busqueda2']);chequear(document.forms[0].opTipoBusqueda[2]);document.forms[0].textoBusqueda.focus();">Descripci&oacute;n Art&iacute;culo</td>
                                </tr>
                                <%--C&oacute;digo del art&iacute;culo--%>
                                <tr>
                                    <td width="2%">
                                        <html:radio property="opTipoBusqueda" value="${opCodigoArticulo}" onclick="show(['txt_busqueda2','boton_busqueda2']);hide(['txt_busqueda1','boton_busqueda1']);document.forms[0].codigoArticulo.focus();"/>
                                    </td>
                                    <td align="left"  class="textoAzul11" onclick="show(['txt_busqueda2','boton_busqueda2']);hide(['txt_busqueda1','boton_busqueda1']);chequear(document.forms[0].opTipoBusqueda[3]);document.forms[0].codigoArticulo.focus();">C&oacute;digo barras</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td width="2%" align="left" id="txt_busqueda1" style="display:block">
                                        <html:text property="textoBusqueda" onkeyup="resultadosBusquedaENTER('buscarArticulo.do', 'WIN_RBUS', ['opTipoBusqueda','textoBusqueda'], 'buscarArtMant');requestAjaxEnter('mantenimientoArticulos.do',['mensajes','resultadosBusqueda'],{parameters: 'agregarArticulos=ok', evalScripts: true});" styleClass="textNormal" size="30" maxlength="100"/>
                                    </td>
                                    <td width="2%" align="left" id="txt_busqueda2" style="display:none">
                                        <html:text property="codigoArticulo" onkeyup="requestAjaxEnter('mantenimientoArticulos.do',['mensajes','resultadosBusqueda'],{parameters: 'agregarArticulo=ok', evalScripts: true});" styleClass="textNormal" size="30" maxlength="100"/>
                                    </td>
                                </tr>
                                <tr><td height="8px" colspan="2"></td></tr>
                                <%--- link hacia la estructura comercial ---%>
                                <tr>
                                    <td align="left" colspan="2">&nbsp;<html:link href="#" onclick="dialogoWeb('catalogoArticulos.do','WIN_RBUS','dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');requestAjax('mantenimientoArticulos.do',['mensajes','resultadosBusqueda'],{parameters: 'agregarArticulos=ok', evalScripts: true});">Cat&aacute;logo de Art&iacute;culos</html:link>
                                </tr>
                                <tr><td height="10px" colspan="2"></td></tr>
                                <%--Bot&oacute;n Buscar--%>
                                <tr>
                                    <td colspan="2">
                                        <table cellpadding="1" cellspacing="0" width="100%">
                                            <tr>
                                                <td align="right" style="display:block" id="boton_busqueda1">
                                                    <div id="botonD">
                                                        <html:link styleClass="buscarD" title="iniciar b&oacute;squeda" href="#" onclick="resultadosBusqueda('buscarArticulo.do', 'WIN_RBUS', ['opTipoBusqueda','textoBusqueda'], 'buscarArtMant');requestAjax('mantenimientoArticulos.do',['mensajes','resultadosBusqueda'],{parameters: 'agregarArticulos=ok', evalScripts: true});">Buscar</html:link>
                                                    </div>
                                                </td>
                                                <td align="right" style="display:none" id="boton_busqueda2">
                                                    <div id="botonD">
                                                        <html:link styleClass="buscarD" title="buscar un art&iacute;culo" href="#" onclick="requestAjax('mantenimientoArticulos.do',['mensajes','resultadosBusqueda'],{parameters: 'agregarArticulo=ok', evalScripts: true});">Buscar</html:link>
                                                    </div>                                                    
                                                </td>
                                                <td width="3px"></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr><td height="4" colspan="2"></td></tr>
                                <%--Fin B&oacute;squeda--%>
                            </table>			
                        </td>
                        <td width="1%">&nbsp;</td>
                        <%-- Datos --%>
                        <td width="79%" valign="top">
                            <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                <%--Titulo de los datos--%>
                                <tr>
                                    <td class="fila_titulo">
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td><img src="./images/articulos24.gif" border="0"/></td>
                                                <td height="23" width="100%" align="left">&nbsp;Detalle de Articulos</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <%--Datos de pedidos--%>
                                <tr>
                                    <td>
                                        <div id="resultadosBusqueda">
                                            <table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
                                                <tr><td height="10px"></td></tr>
                                                <logic:notEmpty name="ec.com.smx.sic.sispe.mantenimiento.listaArticulos">
                                                    <tr>
                                                        <td>	
                                                            <table border="0" cellspacing="0" width="100%" cellpadding="1">	
                                                                <tr class="tituloTablas">
                                                                    <td class="columna_contenido" width="5%" align="center">No</td>
                                                                    <td class="columna_contenido" width="12%" align="center">CLASIFICACION</td>
                                                                    <td class="columna_contenido" width="18%" align="center">CODIGO</td>
                                                                    <td class="columna_contenido" width="49%" align="center">DESCRIPCION</td>
                                                                    <td class="columna_contenido" width="8%" align="center">ESTADO</td>
                                                                    <td class="columna_contenido" width="8%" align="center">EDICION</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <div id="div_listado" style="width:100%;height:420px;overflow:auto">
                                                                <table border="0" cellspacing="0" width="100%" cellpadding="1">
                                                                    <logic:iterate name="ec.com.smx.sic.sispe.mantenimiento.listaArticulos" id="articuloDTO" indexId="indiceArticulo">
                                                                        <bean:define id="fila" value="${indiceArticulo + 1}"/>
                                                                        <%--------- control del estilo para el color de las filas --------------%>
                                                                        <bean:define id="residuo" value="${indiceArticulo % 2}"/>
                                                                        <logic:equal name="residuo" value="0">
                                                                            <bean:define id="colorBack" value="blanco10"/>
                                                                        </logic:equal>
                                                                        <logic:notEqual name="residuo" value="0">
                                                                            <bean:define id="colorBack" value="grisClaro10"/>
                                                                        </logic:notEqual>
                                                                        <%--------------------------------------------------------------------%>
                                                                        <tr class="${colorBack}">
                                                                            <td class="columna_contenido fila_contenido " width="5%" align="center"><bean:write name="fila"/></td>
                                                                            <td width="12%" class="fila_contenido columna_contenido" align="center">
                                                                                <bean:write name="articuloDTO" property="codigoClasificacion"/>
                                                                            </td>
                                                                            <td width="18%" class="fila_contenido columna_contenido" align="center">
                                                                                <bean:write name="articuloDTO" property="codigoBarras"/>
                                                                            </td>
                                                                            <td width="49%" class="fila_contenido columna_contenido" align="left">
                                                                                <bean:write name="articuloDTO" property="descripcionArticulo"/>
                                                                            </td>
                                                                            <td width="8%" class="fila_contenido columna_contenido" align="center">
                                                                                <logic:equal name="articuloDTO" property="estadoArticulo" value="${estadoActivo}">
                                                                                    <img src="./images/exito_16.gif" alt="estado activo">
                                                                                </logic:equal>
                                                                                <logic:notEqual name="articuloDTO" property="estadoArticulo" value="${estadoActivo}">
                                                                                    <img src="./images/parado.gif" alt="estado inactivo">
                                                                                </logic:notEqual> 
                                                                            </td>
                                                                            <td width="8%" class="fila_contenido columna_contenido columna_contenido_der" align="center">
                                                                                <html:link action="mantenimientoArticulos" paramName="indiceArticulo" paramId="indiceArticulo"><img src="./images/editar16.gif" alt="editar art&iacute;culo" border="0"></html:link>
                                                                            </td>
                                                                        </tr>
                                                                    </logic:iterate>
                                                                </table>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td  height="5px"></td>
                                                    </tr>
                                                </logic:notEmpty>
                                                <logic:empty name="ec.com.smx.sic.sispe.mantenimiento.listaArticulos">
                                                    <tr>
                                                        <td class="textoNegro11" align="left">Seleccione un criterio de b&oacute;squeda</td>
                                                    </tr>
                                                </logic:empty>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                                <%--Fin datos de pedidos--%>
                            </table>
                        </td>
                    </tr>
                    <%--Fin P&aacute;gina--%>
                </table>
            </td>
        </tr>
    </html:form>
</TABLE>

<tiles:insert page="../../include/bottom.jsp"/>