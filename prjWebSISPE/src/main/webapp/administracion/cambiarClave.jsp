<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="../include/top.jsp"/>

<html:form action="cambiarClave" method="post">
    <TABLE border="0" cellspacing="0" cellpadding="0" width="100%" class="textos">
        <%--Titulo de los datos--%>
        <tr>
            <td align="left" valign="top" width="100%">
                <html:hidden property="ayuda" value=""/>
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="images/cambiar_password2.gif" border="0"></img></td>
                        <td height="35" valign="middle">Cambiar Clave</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <div id="botonA">
                                            <html:link href="#" styleClass="guardarA" onclick="realizarEnvio('cambiarClave')">
                                                Guardar
                                            </html:link>
                                        </div>
                                    </td>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="cancelarA">
                                                Cancelar
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
        <tr><td colspan="8" height="8"></td></tr>
        <tr>
            <td valign="top" align="center" valign="top">
                <table border="0" cellspacing="0" cellpadding="0" align="center" width="96%">
                	<tr height="75">        		
                	</tr>
                    <tr>
                    	<td>
                            <table align="center" border="1" cellpadding="4" cellspacing="1" class="tabla_informacion">
                                <tr border="1" height="30">	
                                    <td align="right" class="textoAzul11N">Clave anterior:*</td>
                                    <td align="left"><smx:password property="passwordActual" size="38" maxlength="32" styleClass="textObligatorio" styleError="campoError"/></td>
                                </tr>
                                <tr border="1" height="30">
                                    <td align="right" class="textoAzul11N">Clave nueva:*</td>
                                    <td align="left"><smx:password property="nuevoPassword" size="38" maxlength="32" styleClass="textObligatorio" styleError="campoError"/></td>
                                </tr>	
                                <tr border="1" height="30">
                                    <td align="right" class="textoAzul11N">Confirmar clave nueva:*</td>
                                    <td align="left"><smx:password property="confirmarPassword" size="38" maxlength="32" styleClass="textObligatorio" styleError="campoError"/></td>
                                </tr>
                                <tr >
						        	<td hidden="yes">
						        		<div id="pregunta">
											<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
												<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
												</tiles:insert>
												<script language="javascript">mostrarModal();</script>
											</logic:notEmpty>
										</div>
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
<tiles:insert page="../include/bottom.jsp"/>