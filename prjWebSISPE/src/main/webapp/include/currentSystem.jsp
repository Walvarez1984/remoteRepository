<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<logic:notEmpty name="multicompany.currentSystem">
    <b><bean:define id="currentSystem" name="multicompany.currentSystem"/>
    <bean:write name="currentSystem" property="systemName"/></b>
</logic:notEmpty>
<logic:empty name="multicompany.currentSystem"><b>SESION EXPIRADA</b></logic:empty>
