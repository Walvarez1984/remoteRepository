<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table cellpadding="0" cellspacing="0">

  <tr>
      <td id="colores" colspan="3" align="center">
          <tiles:insert page="/administracion/plantillaCalendario/paleta.jsp"/>
      </td>
  </tr>
  <tr>
      <td colspan="3" id="confirmar">
          <tiles:insert page="/administracion/plantillaCalendario/mensajes.jsp"/>
      </td>
  </tr>
  <tr>
      <td valign="top" id="seccionNuevaPlan">
          <tiles:insert page="/administracion/plantillaCalendario/nuevaPlantilla.jsp"/>
      </td>
      <td width="2px">&nbsp;</td>
      <td valign="top" id="seccionLocalesPlan">
          <tiles:insert page="/administracion/plantillaCalendario/localesPlantilla.jsp">
		  	<tiles:put name="vtformName" beanName="vformName"/>
		  	<tiles:put name="vtformAction" beanName="vtformAction"/>
		  </tiles:insert>	
      </td>
  </tr>
</table>  