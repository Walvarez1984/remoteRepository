<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>

<logic:notEmpty name="ec.com.smx.sic.sispe.procesarTareas.codigo.caducarPedidos">
    <%--------------------- c&oacute;digos de los tipos de tareas ------------------------%>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.caducarPedidos" id="codCaducarPedidos"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.actualizarProveedores" id="codActualizarProveedores"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.actualizarClasificaciones" id="codActualizarClasificaciones"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.actualizarArticulosGenerales" id="codActualizarArticulosGenerales"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.actualizarArticulosLocales" id="codActualizarArticulosLocales"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.codigo.todos" id="codTodos"/>
    
    <%--------------------- descripciones de los tipos de tareas ------------------------%>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.descripcion.caducarPedidos" id="desCaducarPedidos"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.descripcion.actualizarProveedores" id="desActualizarProveedores"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.descripcion.actualizarClasificaciones" id="desActualizarClasificaciones"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.descripcion.actualizarArticulosGenerales" id="desActualizarArticulosGenerales"/>
    <bean:define name="ec.com.smx.sic.sispe.procesarTareas.descripcion.actualizarArticulosLocales" id="desActualizarArticulosLocales"/>
</logic:notEmpty>

<%------------ INICIO - Formulario de Gestion de Tareas ------------%>
<html:form action="procesarTareas" method="post">
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textos">
    
    
    <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/procesos48.gif" border="0"></img></td>
                        <td height="35" valign="middle">Gesti&oacute;n de Tareas</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">	
                                            <html:link href="#" styleClass="ejecutar_procesosA" onclick="requestAjax('procesarTareas.do', ['mensajes'], {parameters: 'botonRegistrar=ok'});">
                                                Ejecutar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="cancelarA">
                                                Salir
                                            </html:link>
                                            
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td colspan="8" height="8"></td></tr>
        
    
    <tr>
    
    <td align="center" valign="top">
        
        
            
            <table border="0" width="98%" cellspacing="0" cellpadding="2" class="tabla_informacion">
                <tr>
                    <td class="textoAzul11N" width="12%" align="left">Tipo de Tarea:*</td>
                    <td align="left">
                        <html:select property="tipoTarea" styleClass="combos">
                            <html:option value="">Seleccione</html:option>
                            <html:option value="${codCaducarPedidos}">${desCaducarPedidos}</html:option>
                            <html:option value="${codActualizarProveedores}">${desActualizarProveedores}</html:option>
                            <html:option value="${codActualizarClasificaciones}">${desActualizarClasificaciones}</html:option>
                            <html:option value="${codActualizarArticulosGenerales}">${desActualizarArticulosGenerales}</html:option>
                            <html:option value="${codActualizarArticulosLocales}">${desActualizarArticulosLocales}</html:option>
                            <html:option value="${codTodos}">Todas</html:option>
                        </html:select>
                    </td>
                </tr>	
            </table>
        
    </td>
</TABLE>
</html:form>
<%-- ---------- FIN - Formulario de Gestion de Tareas ---------- --%>
<tiles:insert page="../include/bottom.jsp"/>