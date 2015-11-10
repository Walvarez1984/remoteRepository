<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario de Actualizar Especial ---------- --%>
<html:form action="actualizarEspecial" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        <logic:notEmpty name="sispe.estado.activo">	
            <bean:define id="estadoActivo" name="sispe.estado.activo"/>
            <bean:define id="estadoInactivo" name="sispe.estado.inactivo"/>
        </logic:notEmpty>
        
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/editarEspeciales.gif" border="0"></img></td>
                        <td height="35" valign="middle">Actualizar Especial</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">	
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('actualizarEspecial')">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="atras" value="ok"/>
                                        <div id="botonA">
                                            <html:link action="actualizarEspecial" paramId="volver" paramName="atras" styleClass="cancelarA">
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
        <tr><td colspan="8" height="8"></td></tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" width="98%" cellpadding="2" cellspacing="0" class="tabla_informacion">
                    <tr>
                        <td class="textoAzul11" align="right" width="10%">Estado:*</td>
                        <td align="left">
                            <smx:select property="estadoEspecial" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>										
                                <html:option value="${estadoActivo}">Activo</html:option>
                                <html:option value="${estadoInactivo}">Inactivo</html:option>
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11" align="right">Descripci&oacute;n:*</td>
                        <td align="left"><smx:text name="especialForm" property="descripcionEspecial" styleClass="textObligatorio" styleError="campoError" maxlength="100" size="40"/></td>
                    </tr>
                    <tr>
                        <td class="textoAzul11" align="right" width="10%">Usado en:*</td>
                        <td align="left">
                            <smx:select property="tipoPedido" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <html:options collection="ec.com.smx.sic.sispe.tiposPedido" property="id.codigoTipoPedido" labelProperty="descripcionTipoPedido"/>
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11" align="right">
                            <html:checkbox property="opPublicar" value="${estadoActivo}">&nbsp;Publicado</html:checkbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr><td height="10"></td></tr>
        <tr>
            <td>
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="98%" align="center">
                    <%--Titulo de los datos--%>
                    <tr>
                        <td class="fila_titulo" colspan="3">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                <tr>
                                    <td>&nbsp;</td>
                                    <td height="23" width="63%" class="textoNegro11">&nbsp;<b>Lista de Clasificaciones</b></td>
                                    <td>
                                        <table cellpadding="1" cellspacing="0" border="0" width="100%">
                                            <tr>
                                                <td align="left" class="textoAzul11N">Clasificaci&oacute;n:&nbsp;</td>
                                                <td>
                                                    <html:text property="codigoClasificacionEspecial" size="10" styleClass="combos" onkeyup="requestAjaxEnter('actualizarEspecial.do',['mensajes','div_listado'],{parameters: 'botonAgregarClasificacion=ok', evalScripts: true});"/>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <div id="botonD">
                                                        <html:link styleClass="agregarD" href="#" onclick="requestAjax('actualizarEspecial.do',['mensajes','div_listado'],{parameters: 'botonAgregarClasificacion=ok', evalScripts: true});">Agregar</html:link>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div id="botonD">
                                                        <html:link styleClass="buscarD" href="#" onclick="dialogoWeb('catalogoArticulos.do','WIN_RBUS','dialogHeight:650px;dialogWidth:900px;help:no;scroll:no');requestAjax('actualizarEspecial.do',['mensajes','div_listado'],{parameters: 'agregarClasificaciones=ok', evalScripts: true});">Buscar</html:link>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="3" height="8"></td></tr>
                    <tr>
                        <td>
                            <table border="0" cellspacing="0" width="98%" align="center">
                                <tr class="tituloTablas">
                                    <td align="center" class="columna_contenido" width="10%">C&Oacute;DIGO</td>	
                                    <td align="center" class="columna_contenido" width="60%">DESCRIPCI&Oacute;N</td>
                                    <td align="center" class="columna_contenido" width="10%">ESTADO</td>
                                    <td align="center" class="columna_contenido" width="20%">ACCI&Oacute;N</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <div id="div_listado" style="width:98%;height:320px;overflow:auto">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.especialesClasificaciones">
                                    <table border="0" cellspacing="0" width="98%">
                                        <c:set var="index" value="0"/>
                                        <logic:iterate name="ec.com.smx.sic.sispe.especialesClasificaciones" id="especialClasificacionDTO" indexId="numeroRegistro">	
                                            <%--------- control del estilo para el color de las filas --------------%>
                                            <bean:define id="residuo" value="${index % 2}"/>
                                            <logic:equal name="residuo" value="0">
                                                <bean:define id="colorBack" value="blanco10"/>
                                            </logic:equal>
                                            <logic:notEqual name="residuo" value="0">
                                                <bean:define id="colorBack" value="grisClaro10"/>
                                            </logic:notEqual>
                                            <logic:notEmpty name="especialClasificacionDTO" property="estadoEspecialClasificacion">
                                                <tr class="${colorBack}">
                                                    <td align="center" class="columna_contenido fila_contenido" width="10%"><bean:write name="especialClasificacionDTO" property="clasificacionDTO.id.codigoClasificacion"/></td>	
                                                    <td align="left" class="columna_contenido fila_contenido" width="60%"><bean:write name="especialClasificacionDTO" property="clasificacionDTO.descripcionClasificacion"/></td>
                                                    <td align="center" class="columna_contenido fila_contenido" width="10%" id="estado_${numeroRegistro}">
                                                        <logic:equal name="especialClasificacionDTO" property="estadoEspecialClasificacion" value="${estadoActivo}">
                                                            <img src="./images/exito_16.gif" alt="estado activo">
                                                        </logic:equal> 
                                                        <logic:notEqual name="especialClasificacionDTO" property="estadoEspecialClasificacion" value="${estadoActivo}">
                                                            <img src="./images/parado.gif" alt="estado inactivo">
                                                        </logic:notEqual>
                                                    </td>                                                    
                                                    <td align="center" class="columna_contenido fila_contenido columna_contenido_der" width="20%" id="accion_${numeroRegistro}">
                                                        <logic:equal name="especialClasificacionDTO" property="estadoEspecialClasificacion" value="${estadoActivo}">
                                                            <html:link href="#" onclick="requestAjax('actualizarEspecial.do',['estado_${numeroRegistro}','accion_${numeroRegistro}'], {parameters: 'indiceDesactivacionActivacion=${numeroRegistro}'});">
                                                                Desactivar
                                                            </html:link>
                                                        </logic:equal>
                                                        <logic:notEqual name="especialClasificacionDTO" property="estadoEspecialClasificacion" value="${estadoActivo}">
                                                            <html:link href="#" onclick="requestAjax('actualizarEspecial.do',['estado_${numeroRegistro}','accion_${numeroRegistro}'], {parameters: 'indiceDesactivacionActivacion=${numeroRegistro}'});">
                                                                Activar
                                                            </html:link>
                                                        </logic:notEqual>&nbsp;/&nbsp;
                                                        <html:link href="#" onclick="requestAjax('actualizarEspecial.do',['div_listado'], {parameters: 'indiceEliminacion=${numeroRegistro}'});">
                                                            Eliminar
                                                        </html:link>
                                                    </td>
                                                </tr>	
                                                <c:set var="index" value="${index + 1}"/>
                                            </logic:notEmpty>
                                        </logic:iterate>
                                    </table>
                                    <logic:notEmpty name="clasificacionAgregada">
                                        <script language="JavaScript" type="text/javascript">document.getElementById("div_listado").scrollTop=document.getElementById("div_listado").scrollHeight;</script>
                                    </logic:notEmpty>
                                </logic:notEmpty>
                            </div>
                        </td>
                    </tr>
                    <tr><td height="8"></td></tr>
                </table>	
            </td>
        </tr>
    </TABLE>
</html:form>
<%-- ---------- FIN - Formulario de Actualizar Especial ---------- --%>
<tiles:insert page="../include/bottom.jsp"/>