
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:present name="system.redireccionarsispev2">
    <bean:define id="urlSystem" name="system.redireccionarsispev2"/>
    <logic:redirect href="${urlSystem}" scope="request"/>
</logic:present>