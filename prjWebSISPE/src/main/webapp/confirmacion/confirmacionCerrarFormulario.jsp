<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<div id="popupCerrarForm" class="popup" style="visibility:hidden">
    <div id="center" class="popupcontenido">
        <table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F4F5EB" class="tabla_informacion">
            <tr>
                <td background="images/barralogin.gif" height="22px">
                    <table cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td class="textoBlanco11">
                                <b>&nbsp;&nbsp;Confirmaci&oacute;n</b>
                            </td>
                            <td align="right">
                                <div id="botonWin">
                                    <a href="#" class="linkBlanco8" onclick="hide(['popupCerrarForm']);ocultarModal();">X</a>
                                </div>
                            </td>
                            <td width="3px"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
				<table border="0" align="center" cellspacing="0" cellpadding="5" bgcolor="#F4F5EB" class="tabla_informacion">
				<tr><td>
                    <table border="0" width="100%" align="center" cellpadding="0" cellspacing="5" class="fondoBlanco textoNegro11">
                        <tr>
                            <td align="left" valign="top" colspan="2" >
                                Si usted vuelve al men&uacute; principal perder&aacute; los datos ingresados.
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" width="5%" ><img src="images/pregunta24.gif" border="0"></td>
                            <td align="left" >¿Desea volver al men&uacute; principal?</td>
                        </tr>
                    </table>
					</td><tr>
					<tr><td>
					<table cellpadding="0" cellspacing="0" align="center" width="50%">
                                    <td align="center">
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonDmin">
                                            <html:link styleClass="siDmin" action="menuPrincipal" paramId="salir" paramName="exit">Si</html:link>
                                        </div>
                                    </td>
                                    <td align="center"> 
                                        <div id="botonDmin">
                                            <html:link styleClass="noDmin" href="#" onclick="hide(['popupCerrarForm']);ocultarModal();">No</html:link>
                                        </div>
                                    </td>
                    </table>
					</td><tr>
				</table>
                </td>
            </tr>
        </table>
    </div>
</div>