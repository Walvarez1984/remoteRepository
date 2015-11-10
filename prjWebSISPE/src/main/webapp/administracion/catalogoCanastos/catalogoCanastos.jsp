<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<fmt:setLocale value="en_US"/>
<tiles:useAttribute id="vformName"  name="vtformName"  classname="java.lang.String" ignore="true"/>

<logic:notEmpty name="sispe.estado.activo">	
    <bean:define id="estadoActivo" name="sispe.estado.activo"/>
</logic:notEmpty>
<logic:notEmpty name="ec.com.smx.sic.sispe.codigoCanastoVacio">
    <bean:define id="codigoCanastoVacio" name="ec.com.smx.sic.sispe.codigoCanastoVacio"/>
</logic:notEmpty>
<bean:define id="formatosNumeros"><bean:message key="formatos.numeros"/></bean:define>

<!-- contenido -->
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td>
            <div id="envioMail">
                <logic:notEmpty name="ec.com.smx.sic.sispe.envioMailVentana">
                    <logic:empty name="ec.com.smx.sic.sispe.ventanaActiva">
                        <script language="javascript">mostrarModal()</script>
                    </logic:empty>
                    
                    <tiles:insert page="/administracion/catalogoCanastos/envioMail.jsp">
                        <tiles:put name="vtformName" beanName="vformName"/>
                    </tiles:insert>
                </logic:notEmpty>
                
                <logic:empty name="ec.com.smx.sic.sispe.envioMailVentana">
                    <script language="javascript">ocultarModal();</script>
                </logic:empty>
            </div>
        </td>
    </tr>
    <logic:notEmpty name="ec.com.smx.sic.sispe.canastos">
        <tr>
            <td>
                <div style="width:100%;height:500px;overflow-x:hidden;overflow-y:auto;" id="divCanastos">
                    <table border="0" width="100%" height="15" class="fila_contenido">
                        <tr>
                            <td width="3%" height="15">
                                <html:checkbox title="Seleccionar todos los canastos" property="opEscogerTodos" value="t" onclick="activarDesactivarTodo(this,catalogoCanastosForm.opEscogerSeleccionado);"></html:checkbox>
                            </td>
                            <td width="130" height="15" align="left" class="textoNegro10">
                                Seleccionar todos
                            </td>
                        </tr>
                    </table>
                    <table cellpadding="1" cellspacing="0" width="100%">    
                        <%--Cuerpo--%>
                        <tr>
                            <td>
                                <table border="0" cellpadding="1" cellspacing="0" width="97%" align="center">
                                    <tr>
                                        <c:set var="contador" value="0"/>
                                        <c:set var="fila_inicio" value=""/>
                                        <c:set var="fila_fin" value=""/>
                                        <logic:iterate id="articuloDTO" name="ec.com.smx.sic.sispe.canastos" indexId="indiceArticulo">
                                            <logic:notEqual name="contador" value="3">
                                                <c:set var="fila_inicio" value=""/>
                                                <c:set var="fila_fin" value=""/>
                                            </logic:notEqual>                                    
                                            <logic:equal name="contador" value="3">
                                                <c:set var="contador" value="0"/>
                                                <c:set var="fila_inicio" value="<tr>"/>
                                                <c:set var="fila_fin" value="</tr>"/>
                                            </logic:equal>
                                            
                                            <logic:notEqual name="articuloDTO" property="id.codigoArticulo" value="${codigoCanastoVacio}">
                                                ${fila_fin}
                                                ${fila_inicio}
                                                <td height="150px">
                                                    <%-- inicio tabla para cada canasta --%>
                                                    <table border="1" width="310px" height="140px" align="center" class="tabla_informacion" cellpadding="1" cellspacing="0" bgcolor="#dae2e9">
                                                        <tr>
                                                            <td class="columna_contenido_der" valign="top" width="140px">
                                                                <table cellpadding="1" cellspacing="0" width="100%">
                                                                    <tr>
                                                                        <td valign="top">
                                                                            <html:multibox property="opEscogerSeleccionado" value="${indiceArticulo}" title="${indiceArticulo}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td valign="middle" align="center">
                                                                            <img id="idImagen" src="obtenerImagen.do?indiceImagenCanasto=${indiceArticulo}" width="130" height="110" border="0">
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                            <td valign="top">
                                                                <%-- tabla de detalles --%>
                                                                <table border="0" class="textoAzul9" cellspacing="0" cellpadding="1" width="100%">
                                                                    <tr>
                                                                        <td height="20" valign="top" align="right" class="textoNegro13" >
                                                                            <b><bean:write name="articuloDTO" property="descripcionArticulo"/></b>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td valign="top" align="right" class="textoAzul12" >
                                                                        	<b><fmt:formatNumber type="number" pattern="${formatosNumeros}" value="${articuloDTO.articulo.precioBaseImp}"/></b>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td valign="top" align="right" class="textoAzul9" height="90px">Incluye IVA.</td>
                                                                    </tr>
                                                                      
                                                                    <%--
                                                                    <logic:notEmpty name="articuloDTO" property="recetaArticulos">
                                                                        <logic:iterate id="recetaArticuloDTO" name="articuloDTO" property="recetaArticulos" length="5">
                                                                            <tr>
                                                                                <td>
                                                                                    <li>
                                                                                        <bean:write name="recetaArticuloDTO" property="articuloDTO.descripcionArticulo"/>
                                                                                    </li>
                                                                                </td>
                                                                            </tr>
                                                                        </logic:iterate>
                                                                        <tr>
                                                                            <td>
                                                                                <li>...</li>
                                                                            </td>
                                                                        </tr>
                                                                    </logic:notEmpty>
                                                                    --%>
                                                                    <tr>
                                                                        <td height="15" align="right">
                                                                            <a href="#" onclick="window.showModalDialog('catalogoCanastos.do?detalleCanasto=${indiceArticulo}','Detalle_canasto','scroll:no;dialogHeight:450px;dialogWidth:650px;help:no');">Ver detalle completo</a>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <%-- fin tabla para cada canasta --%>
                                                </td>
                                                <c:set var="contador" value="${contador + 1}"/>
                                            </logic:notEqual>
                                        </logic:iterate>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
    </logic:notEmpty> 
</table>