<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<tiles:useAttribute id="vnombre"     name="vtnombre"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vicono"      name="vticono"   classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtitleBar"   name="titleBar"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>

<tiles:insert attribute="header"/>

<html:form action="${vformAction}" method="post">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	
	<tr><!-- BARRA TITULO -->
		<td id="titleBar" class="titulosAccion" height="57px">    
		    <tiles:insert attribute="title">
				<tiles:put name="vtnombre"   beanName="vnombre"/>
				<tiles:put name="vticono"    beanName="vicono"/>
				<tiles:put name="vtformName" beanName="vformName"/>
				<tiles:put name="vtformAction" beanName="vformAction"/>
		        <tiles:put name="titleBar"   beanName="vtitleBar"/>
			</tiles:insert>
		</td>
	</tr><!-- /BARRA TITULO -->
	
	<tr><!-- CONTENIDO -->
		<td align="left" valign="top">
			<!-- BODY -->
			<div id="div_pagina2" style="width:100%;">
				
					<input type="hidden" name="ayuda" value=""/>
					<tiles:insert attribute="body">
						<tiles:put name="vtformName" beanName="vformName"/>
						<tiles:put name="vtformAction" beanName="vformAction"/>
					</tiles:insert>
				
			</div>
			<!-- FIN BODY-->
		</td>		
	</tr><!-- /CONTENIDO -->
</table>
</html:form>
<tiles:insert attribute="footer"/>