<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="ec.com.smx.sispe.conexion">
    <bean:define id="urlSystem" name="ec.com.smx.sispe.conexion" property="urlConexion"/>
    <logic:redirect href="${urlSystem}" scope="request"/>
</logic:present>