<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld"  prefix="bean" %>
<tiles:useAttribute id="vnombre"    name="vtnombre"    classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vicono"     name="vticono"     classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformName"  name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellspacing="0" cellpadding="0">
<tr>
	<td width="3%" align="center"><img src="${vicono}" border="0"></img></td>
	<td align="left">&nbsp;&nbsp;<bean:write name="vnombre"/></td>
	<td align="right">
    	<tiles:insert attribute="titleBar">
        	<tiles:put name="vtformName" beanName="vformName"/>
            <tiles:put name="vtformAction" beanName="vformAction"/>
        </tiles:insert>
    </td>
</tr>
</table>