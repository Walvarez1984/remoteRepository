<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>
<%-- ---------- INICIO - Formulario de Actualizar Frecuencia ---------- --%>
<html:form action="actualizarFrecuenciaArticulos" method="post">
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center"><img src="images/frecuenciaArticulos.gif" border="0"></img></td>
                        <td height="35" valign="middle">Actualizar Frecuencia Art&iacute;culos</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <html:hidden property="ayuda" value=""/> 
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('guardarFrecuencia')" title="Guardar frecuencia">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="cancel" value="cancel" />
                                        <div id="botonA">
                                            <html:link action="frecuenciaArticulos" styleClass="cancelarA" title="Cancelar la actualizaci&oacute;n">Cancelar</html:link>
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
                <table border="0" width="98%" cellspacing="0" cellpadding="2"  class="tabla_informacion" align="center">
                    <%-- estado de la frecuencia de art&iacute;culos --%>
                    <logic:notEmpty name="sispe.estado.activo">
                        <bean:define name="sispe.estado.activo" id="estadoActivo" />
                        <bean:define name="sispe.estado.inactivo" id="estadoInactivo" />
                    </logic:notEmpty>
                    <tr>
                        <td class="textoAzul11N" align="right" width="10%">Estado:*</td>
                        <td width="80%" align="left">
                            <smx:select property="estadoFrecuencia" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <html:option value="${estadoActivo}">Activo</html:option>
                                <html:option value="${estadoInactivo}">Inactivo</html:option>
                            </smx:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="textoAzul11N" align="right" width="10%">Descripci&oacute;n:*</td>
                        <td width="80%" align="left">
                            <bean:define id="opcionFrecuenciaQuincenal">
                                <bean:message key="ec.com.smx.sic.sispe.administracion.frecuencias.quincenal" />
                            </bean:define> 
                            <bean:define id="opcionFrecuenciaMensual">
                                <bean:message key="ec.com.smx.sic.sispe.administracion.frecuencias.mensual" />
                            </bean:define> 
                            <smx:select property="descripcionFrecuencia" styleClass="comboObligatorio" styleError="campoError">
                                <html:option value="">Seleccione</html:option>
                                <html:option value="${opcionFrecuenciaQuincenal}">
                                    <bean:write name="opcionFrecuenciaQuincenal" />
                                </html:option>
                                <html:option value="${opcionFrecuenciaMensual}">
                                    <bean:write name="opcionFrecuenciaMensual" />
                                </html:option>
                            </smx:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>    
        <tr>
            <td colspan="8" height="8"></td>
        </tr>
        <tr>
            <td>
                <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="98%" align="center">                                    
                    <tr>
                        
                        <td class="fila_titulo" colspan="3" >
                            <table cellpadding="0" cellspacing="0" border="0" width="100%"  align="center" class="textoNegro11">
                                <tr>
                                    <td><img src="./images/detalle_pedidos24.gif" border="0"></td>
                                    <td height="23" width="56%">&nbsp;Lista de Art&iacute;culos</td>
                                    <td>
                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                            <tr>
                                                <td align="left" class="textoAzul11N">C&oacute;digo barras del Art&iacute;culo:&nbsp;</td>
                                                <td>
                                                	<input style="display:none;" type="text" name="oculto">
                                                	<html:text property="codigoArticulo" size="30" onkeypress="realizarEnvioEnter('agregarArticulo')" styleClass="textNormal"/>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td>
                                                    <div id="botonD">
                                                        <html:link styleClass="agregarD" href="#" onclick="realizarEnvio('agregarArticulo')" title="Agregar un art&iacute;culo a la frecuencia">Agregar</html:link>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    </tr> 
                    <tr>
                        <td colspan="8" height="8"></td>
                    </tr>
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0" width="98%" align="center">
                                <tr>
                                    <td>
                                        <table border="0" cellpadding="3" cellspacing="0" width="100%" align="center" class="tabla_informacion">
                                            <tr class="tituloTablas">
                                                <td align="center" class="columna_contenido" width="5%">N&Uacute;MERO</td>
                                                <td align="center" class="columna_contenido" width="15%">C&Oacute;DIGO</td>
                                                <td align="center" class="columna_contenido" width="53%">DESCRIPCI&Oacute;N</td>
                                                <td align="center" class="columna_contenido" width="17%">ACCI&Oacute;N</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="div_listado" style="width:100%;height:370px;overflow:auto">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%" class="tabla_informacion">
                                                <html:hidden property="indiceArticuloElegido" value=""/> 
                                                <logic:iterate name="ec.com.smx.sic.sispe.administracion.frecuenciaArticulos" id="frecuenciaArticulosCol" indexId="indiceRegistro">
                                                    <bean:define id="residuo" value="${indiceRegistro % 2}" />
                                                    <logic:equal name="residuo" value="0">
                                                        <bean:define id="colorBack" value="blanco10" />
                                                    </logic:equal>
                                                    <logic:notEqual name="residuo" value="0">
                                                        <bean:define id="colorBack" value="grisClaro10" />
                                                    </logic:notEqual>
                                                    <logic:notEmpty name="frecuenciaArticulosCol" property="estadoFrecuenciaArticulo">
                                                        <tr class="${colorBack}" >
                                                            <td width="5%" align="center" class="columna_contenido fila_contenido">
                                                                ${indiceRegistro + 1}
                                                            </td>
                                                            <td width="15%" align="center" class="columna_contenido fila_contenido">
                                                                <bean:write name="frecuenciaArticulosCol" property="articuloDTO.codigoBarrasActivo.id.codigoBarras"/>                                                                
                                                            </td>
                                                            <td width="53%" class="columna_contenido fila_contenido">
                                                                &nbsp;<bean:write name="frecuenciaArticulosCol" property="articuloDTO.descripcionArticulo" />
                                                            </td>
                                                            <td width="17%" class="columna_contenido fila_contenido">
                                                                <table border="0" align="center">
                                                                    <tr>
                                                                        <td align="center">
                                                                            <logic:equal name="frecuenciaArticulosCol" property="estadoFrecuenciaArticulo" value="${estadoActivo}">
                                                                                <html:link href="#" onclick="realizarEnvio('desactivar${indiceRegistro}')" title ="Desactivar el art&iacute;culo de la fila ${indiceRegistro + 1}">
                                                                                    Desactivar
                                                                                </html:link>																	
                                                                            </logic:equal> 
                                                                            <logic:notEqual name="frecuenciaArticulosCol" property="estadoFrecuenciaArticulo" value="${estadoActivo}">
                                                                                <html:link href="#" onclick="realizarEnvio('activar${indiceRegistro}')" title ="Activar el art&iacute;culo de la fila ${indiceRegistro + 1}">
                                                                                    Activar
                                                                                </html:link>
                                                                            </logic:notEqual>
                                                                        </td>
                                                                        <td align="center">&nbsp;/&nbsp;</td>
                                                                        <td align="center">
                                                                        	<html:link  href="#" onclick="requestAjax('actualizarFrecuenciaArticulos.do',['mensajes','div_listado'],{parameters: 'indiceEliminacion=${indiceRegistro}'});">
                                                                                Eliminar
                                                                            </html:link>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </logic:notEmpty>
                                                </logic:iterate>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>                
                </table>
            </td>
        </tr>
    </table>
</html:form>
<%-- ---------- FIN - Formulario de Actualizar Frecuencia ---------- --%>
<tiles:insert page="../include/bottom.jsp" />