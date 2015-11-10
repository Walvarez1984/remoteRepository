<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<b>
    Bienvenido: &nbsp;
    <logic:notEmpty name="security.currenLogonUser">
        <bean:define id="currenLogonUser" name="security.currenLogonUser"/>
        <bean:write name="currenLogonUser" property="userCompleteName"/>
    </logic:notEmpty>
    
    <logic:empty name="security.currenLogonUser">
        Visitante.
    </logic:empty>
</b>