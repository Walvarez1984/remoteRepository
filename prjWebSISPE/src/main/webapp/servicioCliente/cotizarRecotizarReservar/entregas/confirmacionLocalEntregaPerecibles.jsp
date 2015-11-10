<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!-- ventana insertada desde entregaArticuloLocal.jsp -->
            <table border="0" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
            <tr>
                <td bgcolor="#F4F5EB">
                    <table class="tabla_informacion textoNegro11" border="0" width="100%" height="100px" align="center">
                        <tr>
                            <td width="5%"><img src="images/pregunta24.gif" border="0"></td>
                            <td align="left">¿Está seguro(a) que el local seleccionado es el correcto?</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <table cellpadding="0" cellspacing="0" align="center" width="50%">
                                    <td align="center">
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonDmin">
                                            <html:link styleClass="siDmin" href="#" onclick="requestAjax('calendarioBodega.do', ['guardarEntregas','mensajesEntregas'], {parameters: 'botonGuardarCambios=guardar&actualizar=ok',evalScripts:true});">Si</html:link>
                                        </div>
                                    </td>
                                    <td align="center"> 
                                        <div id="botonDmin">
                                            <input type="hidden" name="regresar" value="">
                                            <html:link styleClass="noDmin" href="#" onclick="ocultarConfigEntregas();">No</html:link>
                                        </div>
                                    </td>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>