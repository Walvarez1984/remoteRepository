 <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table cellpadding="0" cellspacing="0" width="100%">		
	<tr>
         <td align="center" valign="top">
             <table border="1" class="textoNegro11" width="99%" align="center">
                 <tr><td height="5px"></td></tr>
                 <tr>
                     <td>
                         <tiles:insert page="/controlesUsuario/controlTab.jsp">
	                         <tiles:put name="vtformName" beanName="vformName"/>
						  	 <tiles:put name="vtformAction" beanName="vtformAction"/>
						 </tiles:insert> 	 
                     </td>
                 </tr>   
             </table>
         </td> 
      </tr>	
</table>