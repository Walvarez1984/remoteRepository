<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<logic:notEmpty name="system.corp.contactos">
	<bean:define id="urlJSF" name="system.corp.contactos" />	
</logic:notEmpty>

<script language="javascript">mostrarModal();</script>
<div style="width: 950px; height: 605px;" id="popUpCorporativo"> 
	<iframe src="${urlJSF}" align="middle" name="Contactos" width="100%" height="100%" frameborder="0" onload="killWait('div_wait');">
		Iframes inactivos en su navegador. <br>
		Por favor activelos<br>
		<script type="text/javascript">
		 popWait('div_wait');
		</script>
	</iframe>
</div>