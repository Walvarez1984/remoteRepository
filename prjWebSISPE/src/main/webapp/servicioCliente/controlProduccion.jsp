<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>

<html:form action="controlProduccion" method="post">
	<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    	<html:hidden property="ayuda" value=""/>
    	<tr>
            <td colspan="2" align="right">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                    <tr >
                        <td align="center" >
                            <div id="pregunta">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
                                    <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
                                    <script language="javascript">mostrarModal();</script>
                                </logic:notEmpty>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%--T&iacute;tulos, botones: atr&aacute;s - regresar--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="./images/control_produccion.gif" border="0"></img></td>
                        <td height="35" valign="middle">Control de producci&oacute;n</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA">Inicio</html:link>
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