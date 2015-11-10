<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp" />

<logic:notEmpty name="sispe.estado.activo">
    <bean:define id="estadoActivo" name="sispe.estado.activo" />
</logic:notEmpty>

<html:form action="administracionAutorizadores" method="post">
    <bean:define id="estadoActivo" name="sispe.estado.activo"/>
    <TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center">
                            <img src="images/comprador.gif" border="0"></img>
                        </td>
                        <td height="35" valign="middle">Administraci&oacute;n de Autorizadores</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <logic:empty name="ec.com.smx.sic.sispe.adminAutorizadores">
                                        <td>
                                            <input type="hidden" name="ayuda" value="">
                                            <div id="botonA">
                                                <html:link href="#" styleClass="nuevoA" onclick="realizarEnvio('nuevoAdminAutorizadores')">Nuevo
                                                </html:link>
                                            </div>
                                        </td>
                                    </logic:empty>
                                    <td>
                                        <bean:define id="exit" value="exit" />
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">
                                                Inicio
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
        <tr>
            <td height="20px"></td>
        </tr>
        <%--Cuerpo de la tabla--%>
        <logic:notEmpty name="ec.com.smx.sic.sispe.adminAutorizadores">
            <tr>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0" width="98%" align="center" align="center" class="tabla_informacion">
                        <tr>
                            <td>
                                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                    <tr class="tituloTablas">
                                        <td width="10%" align="center" class="columna_contenido">IDENTIFICADOR</td>
                                        <td width="60%" align="center" class="columna_contenido">NOMBRE COMPLETO</td>
                                        <td width="10%" align="center" class="columna_contenido">ESTADO</td>
                                        <td width="10%" align="center" class="columna_contenido">EDICI&Oacute;N</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                        <logic:iterate name="ec.com.smx.sic.sispe.adminAutorizadores" id="adminAut" indexId="indiceRegistro">
                                            <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}" />
                                            <bean:define id="residuo" value="${indiceRegistro % 2}" />
                                            <logic:equal name="residuo" value="0">
                                                <bean:define id="colorBack" value="blanco10" />
                                            </logic:equal>
                                            <logic:notEqual name="residuo" value="0">
                                                <bean:define id="colorBack" value="grisClaro10" />
                                            </logic:notEqual>
                                            <tr class="${colorBack}">
                                                <td align="center" class="columna_contenido fila_contenido" width="10%">
                                                    <bean:write name="adminAut" property="usuarioAutorizadorDTO.userName" />
                                                </td>
                                                <td align="center" class="columna_contenido fila_contenido" width="60%">
                                                    <bean:write name="adminAut" property="usuarioAutorizadorDTO.userCompleteName" />
                                                </td>
                                                <td align="center" width="10%"class="columna_contenido fila_contenido">
                                                    <logic:equal name="adminAut" property="estadoAutorizador" value="${estadoActivo}">
                                                        <img src="./images/exito_16.gif" alt="estado activo">
                                                    </logic:equal> 
                                                    <logic:notEqual name="adminAut" property="estadoAutorizador" value="${estadoActivo}">
                                                        <img src="./images/parado.gif" alt="estado inactivo">
                                                    </logic:notEqual>
                                                </td> 
                                                <td align="center" class="columna_contenido fila_contenido columna_contenido_der" width="10%">
                                                    <html:link action="/administracionAutorizadores" paramName="indice" paramId="indice">
                                                        <img src="images/editar16.gif" border="0" alt="Actualizar Registro">
                                                    </html:link>
                                                </td>
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
    </TABLE>	
</html:form>
<tiles:insert page="../include/bottom.jsp" />