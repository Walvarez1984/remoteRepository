<%@ page import="java.io.IOException"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notEmpty name="sispe.vistaEntidadResponsableDTO">
	<bean:write name="sispe.vistaEntidadResponsableDTO" property="nombreCiudad"/>,&nbsp;
</logic:notEmpty>
<%
try{
	java.util.Date dat=new java.util.Date();
		String meses[]= {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
		int anio = dat.getYear() + 1900;
		out.print(meses[dat.getMonth()] + " " + dat.getDate() + " del " + anio);
}
catch(IOException e){}
%>