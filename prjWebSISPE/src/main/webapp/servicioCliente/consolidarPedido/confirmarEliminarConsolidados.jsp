<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellpadding="0" cellspacing="5" class="fondoblanco">
    <tr>
    	<td>
    	<table border="0" cellpadding="0" cellspacing="0">
                <tr>
                	<td valign="top" width="3%"><img src="images/info48.gif" border="0">&nbsp;&nbsp;</td> 
					<td align="left">
	                 	Ud est&aacute; eliminando los pedidos agrupados. Recuerde que se eliminarán los descuentos consolidados y se restablecer&aacute;n los descuentos originales de cada pedido.
	                </td>
                </tr>
            </table>
    	</td>
    </tr>                                 
</table>	