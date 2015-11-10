<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp" />

<html:form action="actualizarFormaPago" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%"><html:hidden
                    property="ayuda" value="" />
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="images/editarFormaPago.gif" border="0"></td>
                        <td height="35" valign="middle">Actualizar Forma de Pago</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('guardarFormaPago')">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td><bean:define id="atras" value="ok" />
                                        <div id="botonA">
                                            <html:link action="formaPago" paramId="volver" paramName="atras" styleClass="cancelarA">
                                                Cancelar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="exit" paramName="exit" styleClass="inicioA" title="ir al men&uacute; principal">Inicio</html:link>
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
            <td colspan="8" height="8"></td>
        </tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" width="98%" cellpadding="0" cellspacing="0" class="tabla_informacion">
                    <logic:notEmpty name="sispe.estado.activo">
                        <bean:define name="sispe.estado.inactivo" id="estadoInactivo" />
                        <bean:define name="sispe.estado.activo" id="estadoActivo" />
                    </logic:notEmpty>
                    <tr>
                        <td>
                            <table border="0" align="left">
                                <tr>
                                    <td width="20%" class="textoAzul11N" align="right">Estado:*</td>
                                    <td align="left">
                                        <smx:select property="estadoFormaPago" styleClass="comboObligatorio" styleError="campoError">
                                            <html:option value="">Seleccione</html:option>
                                            <html:option value="${estadoActivo}">Activo</html:option>
                                            <html:option value="${estadoInactivo}">Inactivo</html:option>
                                        </smx:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="textoAzul11N" align="right">Descripci&oacute;n:*</td>
                                    <td align="left">
                                        <smx:text property="descripcionFormaPago"styleClass="textObligatorio" size="100" maxlength="100" styleError="campoError"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </TABLE>
</html:form>
<tiles:insert page="../include/bottom.jsp" />
