<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="estadoActivo" name="estadoActivo" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="token" name="token" classname="java.lang.String" ignore="true"/>
<%-- Contenido de la ventana de confirmacion --%>					
<table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="100%">
	<tr>
		<td bgcolor="#F4F5EB" valign="top" >                          
			<tiles:insert page="/controlesUsuario/controlTabPopUp.jsp" />
		</td>							
	</tr>			            
</table>