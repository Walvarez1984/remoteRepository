<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
    Local:&nbsp;<bean:write name="sispe.vistaEntidadResponsableDTO" property="codigoLocal"/> - <bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreAreaTrabajo"/>
</logic:notEmpty>    