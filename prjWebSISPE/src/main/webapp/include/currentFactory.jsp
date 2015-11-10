<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCompania"/>
</logic:notEmpty>

