<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>

<html:form action="actualizarParametro" focus="descripcionParametro" method="post" onsubmit="return enviado()">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="./images/editarParametros48.gif" border="0"></img></td>
                        <td height="35" valign="middle">Actualizar Par&aacute;metro General</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('registrarParametro')">Guardar</html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="actualizarParametro" paramId="volver" paramName="atras" styleClass="cancelarA">Cancelar</html:link>
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
        <tr><td colspan="8" height="8"></td></tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" cellpadding="2" width="98%" class="tabla_informacion textoNegro11" cellspacing="0" align="center">	
                    <tr>
                        <td class="textoAzul11" width="10%" align="right"><b>Descripci&oacute;n:*</b></td>
                        <td align="left">
                            <html:hidden property="descripcionParametro" write="true"/>
                            <%--<smx:text property="descripcionParametro" styleClass="textObligatorio" styleError="campoError" size="174" maxlength="200"/>--%>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11" align="right"><b>Valor:*</b></td>
                        <td align="left">
                            <smx:text property="valorParametro" styleClass="textObligatorio" styleError="campoError" size="120" maxlength="1000"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </TABLE>
</html:form>

<tiles:insert page="../include/bottom.jsp"/>