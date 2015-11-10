<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp" />

<bean:define id="codPadre" name="ec.com.smx.sic.sispe.administracion.codigoBodegaPadre"/>

<logic:notEmpty name="sispe.estado.activo">
    <bean:define id="estadoActivo" name="sispe.estado.activo" />
</logic:notEmpty>

<html:form action="bodegas" method="post">
    <table border="0" width="100%" align="center" cellspacing="0" cellpadding="0">
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0"  align="center" class="titulosAccion">
                    <tr>
                        <td width="3%" align="center">
                            <img src="./images/bodegas.gif" border="0"/>
                        </td>
                        <td height="35" valign="middle">Bodegas</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <input type="hidden" name="ayuda" value=""/>
                                        <div id="botonA">                                        	
                                            <html:link action="bodegas" paramId="codigoBodegaPadre" paramName="codPadre" styleClass="nuevoA" title="Crear nueva bodega">
                                                Nuevo
                                            </html:link>
                                        </div>
                                    </td>
                                    <bean:define id="codAtras" name="ec.com.smx.sic.sispe.administracion.codigoBodegaAnterior"/>                                                                                	
                                    <logic:notEqual name="codPadre" value="-1">
                                        <td>
                                            <div id="botonA">
                                                <html:link action="bodegas" paramId="codigoBodegaAtras" paramName="codAtras" styleClass="atrasA" title="Regresar a la bodega superior">
                                                    Atras
                                                </html:link>
                                            </div>
                                        </td>
                                    </logic:notEqual>
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
        <tr>
            <td height="10px"></td>
        </tr>
        
        <%-- Ruta de las bodegas en las que se puede navegar --%>
        <tr>
            <td align="center" valign="top">
                <table border="0" width="98%" cellspacing="0" cellpadding="2"  class="tabla_informacion" align="center">
                    <tr>
                        <td align="left" width="10%">
                            <logic:iterate name="ec.com.smx.sic.sispe.administracion.pathBodegas" id="pathCol">                                   
                                ${pathCol}
                            </logic:iterate>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>  
        <tr><td height="10px"></td></tr>
        <%-- Lista de las bodegas --%>
        <tr>
            <td>
                <table cellpadding="0" cellspacing="0" border="0" width="98%" align="center" class="tabla_informacion">
                    <tr>
                        <td>
                            <table border="0" cellpadding="1" cellspacing="0" width="100%">
                                <tr class="tituloTablas">
                                    <td align="center" class="columna_contenido" width="5%">N&Uacute;MERO</td>
                                    <td align="center" class="columna_contenido" width="5%">C&Oacute;DIGO</td>
                                    <td align="center" class="columna_contenido" width="15%">C&Oacute;DIGO BODEGA PADRE</td>							
                                    <td align="center" class="columna_contenido" width="58%">DESCRIPCI&Oacute;N</td>
                                    <td align="center" class="columna_contenido" width="7%">ESTADO</td>
                                    <td align="center" class="columna_contenido" width="10%">EDICI&Oacute;N</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="div_listado" style="width:100%;height:400px;overflow:auto">
                                <table border="0" cellpadding="1" cellspacing="0" width="100%"> 
                                    <logic:notEmpty name="ec.com.smx.sic.sispe.administracion.bodegasPadre">                                    
                                        <logic:iterate name="ec.com.smx.sic.sispe.administracion.bodegasPadre" id="bodegasCol" indexId="indiceRegistro"> 
                                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                                            <logic:equal name="residuo" value="0">
                                                <bean:define id="colorBack" value="blanco10"/>
                                            </logic:equal>
                                            <logic:notEqual name="residuo" value="0">
                                                <bean:define id="colorBack" value="grisClaro10"/>
                                            </logic:notEqual>
                                            
                                            <tr class="${colorBack}">
                                                <td align="center" width="5%" class="columna_contenido fila_contenido">                                                   
                                                    ${indiceRegistro + 1}
                                                </td>
                                                <td align="center" width="5%"
                                                    class="columna_contenido fila_contenido">
                                                    <bean:define id="codBodega" name="bodegasCol" property="id.codigoBodega"/>                                                            
                                                    <html:link action="bodegas" paramId="codigoBodega" paramName="codBodega" title="Ver Bodegas Subordinadas">                                                                            
                                                        <bean:write name="bodegasCol" property="id.codigoBodega" />
                                                    </html:link>
                                                </td>
                                                <td align="center" width="15%" class="columna_contenido fila_contenido">
                                                    <logic:empty name="bodegasCol" property="codigoBodegaPadre">
                                                        ---
                                                    </logic:empty>
                                                    <logic:notEmpty name="bodegasCol" property="codigoBodegaPadre"> 
                                                        <bean:write name="bodegasCol" property="codigoBodegaPadre" />
                                                    </logic:notEmpty>
                                                </td>
                                                <td align="center" width="58%" class="columna_contenido fila_contenido">
                                                    <bean:write name="bodegasCol" property="descripcionBodega" />
                                                </td>
                                                <td align="center" width="7%" class="columna_contenido fila_contenido">
                                                    <logic:equal name="bodegasCol" property="estadoBodega" value="${estadoActivo}">
                                                        <img src="./images/exito_16.gif" alt="estado activo">
                                                    </logic:equal>
                                                    <logic:notEqual name="bodegasCol" property="estadoBodega" value="${estadoActivo}">
                                                        <img src="./images/parado.gif" alt="estado inactivo">
                                                    </logic:notEqual>
                                                </td>
                                                <td align="center" width="10%"
                                                    class="columna_contenido fila_contenido columna_contenido_der">
                                                    <html:link action="bodegas" paramId="codigoBodegaEditar" paramName="codBodega" title="Editar Bodega de la fila ${indiceRegistro +1}">
                                                        <img src="./images/editar16.gif" border="0"/>
                                                    </html:link>
                                                </td>
                                            </tr>
                                        </logic:iterate>
                                    </logic:notEmpty>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</html:form>

<tiles:insert page="../include/bottom.jsp" />