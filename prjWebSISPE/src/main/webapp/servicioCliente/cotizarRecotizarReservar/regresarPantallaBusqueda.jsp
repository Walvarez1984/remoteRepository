<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellpadding="0" cellspacing="5">
    <tr>
        <td align="left">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                	<td valign="top" width="3%"><img src="images/advertencia_16.gif" border="0">&nbsp;&nbsp;</td> 
	                <td align="left">
	                 	<b>EL PEDIDO NO HA SIDO GUARDADO,</b> porque ha sido modificado en otra sesi&oacute;n. Se regresar&aacute; a la pantalla de b&uacute;squeda para actualizar los datos.
	                </td>
                </tr>

            </table>
        </td>
    </tr>                                 
</table>