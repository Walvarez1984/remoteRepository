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
                <td valign="top" colspan="2">
                		Algunos precios y detalles de recetas son DIFERENTES a los guardados inicialmente, si usted desea puede actualizar estos datos haciendo clic en SI, si desea conservar los anteriores haga clic en NO.
                </td>
                </tr>
                <tr>
                	<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
					<td align="left">
	                 	¿Desea actualizar el valor de los datos?
	                </td>
                </tr>
            </table>
    	</td>
    </tr>                                 
</table>	