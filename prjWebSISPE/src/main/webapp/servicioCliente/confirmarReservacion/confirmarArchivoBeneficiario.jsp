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
                	<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
	                <td align="left">
	                 	¿Desea adjuntar un archivo del beneficiario al pedido?
	                </td>
                </tr>
                <tr> 
                 <td align="left" colspan="2">									
                   &nbsp;&nbsp;&nbsp;&nbsp;<html:checkbox property="checkSubirArchivo" value="1" disabled="${disabled}"/>
                   <b>Adjuntar archivo ahora</b>                                                        
                 </td>
                </tr>
            </table>
        </td>
    </tr>                                 
</table>	