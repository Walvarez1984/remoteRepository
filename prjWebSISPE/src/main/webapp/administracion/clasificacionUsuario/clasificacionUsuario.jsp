<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../../include/top.jsp"/>

<%------------ INICIO - Fomulario de permisos sobre clasificaciones ------------%>
<bean:define id="estadoActivo" name="sispe.estado.activo"/>

<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
    <html:form action="clasificacionUsuario" method="post">
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <html:hidden property="ayuda" value=""/>
                    <tr>
                        <td  width="3%" align="center"><img src="./images/permisosClasificaciones.gif" border="0"></td>
                        <td height="35" valign="middle">Permisos de Usuarios sobre Clasificaciones</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="nuevoA" onclick="enviarFormulario('nuevaClaUsu');">Nuevo</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="10px"></td></tr>
        <logic:notEmpty name="ec.com.smx.sic.sispe.colResumenUsuarioClasificacionDTO">
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" width="98%" align="center" class="tabla_informacion">
                        <tr>
                            <td>
                                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                    <tr class="tituloTablas" align="center">
                                        <td width="10%" class="columna_contenido" align="center">ID</td>
                                        <td width="15%" class="columna_contenido" align="center">NOMBRE</td>
                                        <td width="59%" class="columna_contenido" align="center">NOMBRE COMPLETO</td>
                                        <td width="8%" class="columna_contenido" align="center">ESTADO</td>
                                        <td width="8%" class="columna_contenido" align="center">EDICI&Oacute;N</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <div id="div_listado" style="width:100%;height:400px;overflow-y:auto;overflow-x:hidden">
                                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                        <logic:iterate name="ec.com.smx.sic.sispe.colResumenUsuarioClasificacionDTO" id="clasificacionUsuarioDTO" indexId="indiceRegistro">
                                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                                            <logic:equal name="residuo" value="0">
                                                <bean:define id="clase" value="blanco10"/>
                                            </logic:equal>	
                                            <logic:notEqual name="residuo" value="0">
                                                <bean:define id="clase" value="grisClaro10"/>
                                            </logic:notEqual>
                                            <tr class="${clase}">
                                                <td align="left" width="10%" class="columna_contenido fila_contenido"><bean:write name="clasificacionUsuarioDTO" property="user.userId"/></td>
                                                <td align="left" width="15%" class="columna_contenido fila_contenido"><bean:write name="clasificacionUsuarioDTO" property="user.userName"/></td>
                                                <td align="left" width="59%" class="columna_contenido fila_contenido"><bean:write name="clasificacionUsuarioDTO" property="user.userCompleteName"/></td>
                                                <td align="center" width="8%" class="columna_contenido fila_contenido">
                                                    <logic:equal name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}">
                                                        <img src="./images/exito_16.gif" alt="estado activo">
                                                    </logic:equal>
                                                    <logic:notEqual name="clasificacionUsuarioDTO" property="estadoClasificacionUsuario" value="${estadoActivo}">
                                                        <img src="./images/parado.gif" alt="estado inactivo">
                                                    </logic:notEqual>
                                                </td>
                                                <td align="center" width="8%" class="columna_contenido fila_contenido columna_contenido_der"><html:link action="clasificacionUsuario" paramName="indiceRegistro" paramId="indiceUsuCla"><img src="./images/editar16.gif" alt="editar configuraci&oacute;n" border="0"></html:link></td>
                                            </tr>
                                        </logic:iterate>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </logic:notEmpty>
    </html:form>        
</table>
<tiles:insert page="../../include/bottom.jsp" />