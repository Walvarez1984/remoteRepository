<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>

<!-- Pantalla principal para el registro del pedido especial -->

<table cellpadding="0" cellspacing="0" border="0" width="90%" align="center">
    
    <tr><td height="10px"></td></tr>
    <tr  class="fila_titulo">
        <td>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
                <tr>
                    <td align="left" width="5%">
                        <html:checkbox property="opConfirmarPedidoPopUp" title="active esta opci&oacute;n para confirmar el pedido" onclick="mostraryOcultar(this,['confirmarFechas']);" />
                    </td>
                    <td class="textoAzul11" align="left" >&nbsp;Confirmar</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr >
        <td height="45px" valign="top">
            <c:set var="despliegaF" value="none"/>																		
            <logic:notEmpty name="ec.com.smx.sic.fechas.popUp.visible">
                <c:set var="despliegaF" value="block" />
            </logic:notEmpty>
            <div id="confirmarFechas" style="display:${despliegaF}">
                <table border="0" width="100%" cellpadding="0" cellspacing="0">
                    <tr  class="fila_titulo">
                        <td width="50%" align="right">Fecha Despacho:*</td>
                        <td width="18%" align="left" class="textoAzul10">
                        	<html:hidden property="fechaDespachoPopUp" write="true"/>
                            <%--<html:text property="fechaDespachoPopUp" styleClass="textObligatorio"  size="12" maxlength="13"></html:text>--%>
                        </td>
                        <td align="left">
                        	&nbsp;
                            <%--<smx:calendario property="fechaDespachoPopUp" key="formatos.fecha"/>--%>
                        </td>
                    </tr>
                    <tr  class="fila_titulo">
                        <td width="50%" align="right">Fecha Entrega Cliente:*</td>
                        <td width="18%" align="left">
                            <html:text property="fechaEntregaPopUp" styleClass="textObligatorio"  size="12" maxlength="13"></html:text>
                            <%--    <smx:text name="${vformName}" property="fechaEntrega" styleClass="textObligatorio" styleError="campoError" size="12" maxlength="13"/>	--%>
                        </td>
                        <td align="left">	
                            <smx:calendario property="fechaEntregaPopUp" key="formatos.fecha"/>
                        </td> 
                    </tr>
                </table>
            </div>
            
        </td>
    </tr>
</table>