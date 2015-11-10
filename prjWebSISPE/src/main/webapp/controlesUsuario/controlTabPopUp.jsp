<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<div id="divTabs">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>		
            <td valign="top" align="left" width="100%">
                <table border="0" class="textos" width="100%" align="center" cellspacing="0" cellpadding="0">
                    <tr><td height="4"></td></tr>
                    <tr>
                        <!-- Cuerpo -->
                        <TD class="datos" width="100%">
                            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                                <!-- Tabs-->                                
                                <tr>
                                    <td background="images/fondoTabs.gif">
                                        <table cellpadding="0" cellspacing="0" border="0">
                                            <tr>
                                                <bean:define id="controlTab" name="ec.com.smx.sic.controlesusuario.tabPopUp"/>
                                                <logic:iterate id="tab" name="controlTab" property="tabs" indexId="nTab" >
                                                    <td class="textoRojo11">&nbsp;</td>
                                                    <!--Mostrar seleccionado-->
                                                    <logic:equal name="tab" property="seleccionado" value="true">
                                                        <bean:define id="nombrePaginaMostrar" name="tab" property="nombrePaginaMostrar"/>
                                                        <td >
                                                            <table cellpadding="0" cellspacing="0" border="0" BGCOLOR="#f7f7f7">
                                                                <tr>
                                                                    <td class="columna_contenido_negro fila_contenido_sup_negro textoRojo11">&nbsp;</td>
                                                                    <td class="textoRojo11 fila_contenido_sup_negro">
                                                                        <b><bean:write name="tab" property="titulo"/></b>
                                                                    </td>
                                                                    <td><html:img src="images/imgDerechaTabOn.gif"/></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </logic:equal>
                                                    <!--Mostrar no seleccionado-->
                                                    <logic:equal name="tab" property="seleccionado" value="false">
                                                        <td >
                                                            <table cellpadding="0" cellspacing="0" border="0">
                                                                <tr>
                                                                    <td class="columna_contenido fila_contenido_sup fila_contenido_negro textoRojo11" bgcolor="#e0f2fe" >&nbsp;</td>
                                                                    <td class="fila_contenido_sup fila_contenido_negro textoRojo11" bgcolor="#e0f2fe">
                                                                        <logic:equal name="tab" property="activado" value="false">
                                                                            <span class="link"><bean:write name="tab" property="titulo"/></span>
                                                                        </logic:equal>
                                                                        <logic:equal name="tab" property="activado" value="true">
                                                                            <html:link href="#" onclick="requestAjax('${tab.nombreActionIr}.do', ['divTabs','mensajes','popupConfirmar'], {parameters: '${controlTab.parametroRequest}=${nTab}',popWait:true, evalScripts: true});">
                                                                                <span class="link"><bean:write name="tab" property="titulo"/></span>
                                                                            </html:link>
                                                                        </logic:equal>
                                                                    </td>
                                                                    <td><html:img src="images/imgDerechaTabOff.gif"/></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </logic:equal>
                                                </logic:iterate>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <!-- Fin Tabs-->
                                <tr>
                                    <td>
                                        <table cellpadding="1" cellspacing="2" border="0" width="100%" align="center" class="columna_contenido_negro fila_contenido_negro columna_contenido_der_negro" BGCOLOR="#f7f7f7">
                                            <tr>
                                                <td>
                                                    <div id="div_pagina_tab" style="width:100%;height:100%;overflow:hidden;">
                                                        <tiles:insert page="${nombrePaginaMostrar}"/>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </tr>
                </table>
            </td>
        </tr>
    </TABLE>
</div>