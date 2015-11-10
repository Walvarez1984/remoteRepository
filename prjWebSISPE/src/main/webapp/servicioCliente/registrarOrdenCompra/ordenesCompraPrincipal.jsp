<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
	<tr>
	    <td>
	        <div id="pregunta">
	            <logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
	                <jsp:include page="../../confirmacion/confirmacion.jsp"/>
	            </logic:notEmpty>
	        </div>
	    </td>
	</tr>
        
        <%--Contenido de la p&aacute;gina--%>
        <tr>
            <td align="center" valign="top">
                <table border="0" class="textoNegro12" width="98%" align="center" cellspacing="0" cellpadding="0">
                    <html:form action="registrarOrdenCompra" method="post">
                        <tr><td height="7px" colspan="3"></td></tr>
                        <tr>
                            <%--Barra Izquierda--%>
                            <td class="datos" width="15%">
                                <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                            </td>
                            <TD class="datos" width="1%">&nbsp;</TD>
                            <td>
                                <tiles:insert page="/controlesUsuario/controlTab.jsp"/>
                            </td>
                        </tr>
                        <%--Fin P&aacute;gina--%>
                    </html:form>
	        </table>
	    </td>
	</tr>
</TABLE>
