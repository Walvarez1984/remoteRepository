<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<html:form action="actualizarProduccion" method="post">	
	<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                <html:hidden property="ayuda" value=""/>
                                	
                    <tr>
                        <td  width="3%" align="center"><img src="./images/actualizarProduccion.gif" border="0"></img></td>
                        <td height="35" valign="middle">Actualizaci&oacute;n de producci&oacute;n</td>
                        <td align="right" valign="top">
                            <table border="0" cellpadding="1" cellspacing="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="excelA" title="generar reporte de art&iacute;culos en producci&oacute;n" onclick="enviarFormulario('exportarXLS', 0, false);">Reporte</html:link>
                                        </div>
                                    </td>
                                    <%--
                                	<td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="pdfA" title="generar reporte de art&iacute;culos en producci&oacute;n" onclick="enviarFormulario('exportarPDF', 0, false);">Crear PDF</html:link>
                                        </div>
                                    </td>
                                    --%>
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
            <td align="center" valign="top">
                <table border="0" class="textoNegro11" width="99%" align="center">
                    <tr><td height="5px"></td></tr>
                    <tr>
                        <td>
                            <tiles:insert page="/controlesUsuario/controlTab.jsp"/>
                        </td>
                    </tr>   
                </table>
           </td> 
        </tr>	
	</TABLE>
</html:form>	
<tiles:insert page="/include/bottom.jsp"/>