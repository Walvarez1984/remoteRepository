<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<table class="textoNegro11"  border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
	<tr><td height="10px"></td></tr>
	<tr>
    	<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
	     <td align="left">
	      	Ud. realizó una modificación en el pedido, Presione el boton guardar para no perder los cambios.
	     </td>
    </tr>
	<tr><td height="10px"></td></tr>	
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
	<tr><td height="10px"></td></tr>
	<tr>
		<td align="center" valign="bottom" colspan="1">
			<div id="botonD"><html:link href="#" styleClass="aceptarD" onclick="requestAjax('crearCotizacion.do', ['pregunta'], {parameters: 'ocultarPopUp=ok', popWait:false, evalScripts:true});ocultarModal();">Aceptar</html:link></div>
		</td>					
	</tr>
	<tr><td height="5px"></td></tr>
</table>