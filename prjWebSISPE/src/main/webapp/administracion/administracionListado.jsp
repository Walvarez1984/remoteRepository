<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%---------------------- lista de definiciones para las acciones -----------------------------%>
<bean:define id="parametro"><bean:message key="ec.com.smx.sic.sispe.accion.parametro"/></bean:define>
<bean:define id="temporada"><bean:message key="ec.com.smx.sic.sispe.accion.temporada"/></bean:define>
<bean:define id="autorizacion"><bean:message key="ec.com.smx.sic.sispe.accion.autorizacion"/></bean:define>
<bean:define id="descuento"><bean:message key="ec.com.smx.sic.sispe.accion.descuento"/></bean:define>
<bean:define id="tipoDescuento"><bean:message key="ec.com.smx.sic.sispe.accion.tipoDescuento"/></bean:define>
<bean:define id="motivoDescuento"><bean:message key="ec.com.smx.sic.sispe.accion.motivoDescuento"/></bean:define>		
<bean:define id="especiales"><bean:message key="ec.com.smx.sic.sispe.accion.especial"/></bean:define>		
<bean:define id="exit" value="ok"/>
<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
    <tr>
        <td valign="top">
            <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                <html:form action="administracionListado">
                    <html:hidden property="ayuda" value=""/>
                    <tr>
                        <logic:notEmpty  name="ec.com.smx.sic.sispe.imagenFormularioAdministracion">
                            <bean:define id="imagen" name="ec.com.smx.sic.sispe.imagenFormularioAdministracion"/>
                            <td  width="3%" align="center"><img src="images/${imagen}" border="0"></img></td>
                        </logic:notEmpty>    
                        <td height="35" valign="middle"><bean:write name="WebSISPE.tituloVentana"/></td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${motivoDescuento}">
                                            <logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${parametro}">
                                                <div id="botonA">
                                                    <html:link href="#" styleClass="nuevoA" onclick="realizarEnvio('registrarNuevo')">
                                                        Nuevo
                                                    </html:link>
                                                </div>
                                            </logic:notEqual>
                                        </logic:notEqual>
                                    </td>
                                    <logic:equal name="ec.com.smx.sic.sispe.accion" value="${descuento}">
                                        <td>
                                            <bean:define id="atras" value="ok"/>
                                            <div id="botonA">
                                                <html:link action="administracionListado" paramId="volver" paramName="atras" styleClass="cancelarA">
                                                    Cancelar
                                                </html:link> 
                                            </div>
                                        </td>
                                        <td>
                                            <div id="botonA">
                                                <html:link action="menuPrincipal" paramId="exit" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
                                            </div>
                                        </td>
                                    </logic:equal>
                                    <logic:notEqual name="ec.com.smx.sic.sispe.accion" value="${descuento}">
                                        <td>
                                            <div id="botonA">	
                                                <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link>
                                            </div>
                                        </td>
                                    </logic:notEqual>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </html:form>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table border="0" width="98%" align="center" cellspacing="0" cellpadding="0">
                <logic:notEmpty name="ec.com.smx.sic.sispe.paginaAdministracion">
                    <logic:notEmpty name="administracionListadoForm" property="datos">
                        <tr><td height="7px"></td></tr>
                        <tr>
                            <td align="right">
                                <smx:paginacion start="${administracionListadoForm.start}" range="${administracionListadoForm.range}" results="${administracionListadoForm.size}" styleClass="textos" url="administracionListado.do"/>
                            </td>
                        </tr>
                    </logic:notEmpty>
                    <tr>
                        <td>
                            <bean:define id="pagina" name="ec.com.smx.sic.sispe.paginaAdministracion"/>
                            <tiles:insert page='${pagina}'/>
                        </td>
                    </tr>
                </logic:notEmpty>
            </table>
        </td>
    </tr>
</TABLE>
<tiles:insert page="../include/bottom.jsp"/>