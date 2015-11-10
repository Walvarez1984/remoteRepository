<%@page autoFlush="true" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<tiles:useAttribute name="frameContent" ignore="true" classname="java.lang.String"/>
<tiles:useAttribute name="frameHeight" ignore="true" classname="java.lang.String"/>
<tiles:useAttribute name="bodyContent" ignore="true" classname="java.lang.String"/>
<tiles:useAttribute name="bodyHeight" ignore="true" classname="java.lang.String"/>
<tiles:useAttribute name="bodyClass" ignore="true" classname="java.lang.String"/>
<tiles:useAttribute name="overflowY" ignore="true" classname="java.lang.String"/>
<logic:notEmpty name="bodyClass"><bean:define id="bodyClass">${bodyClass}</bean:define></logic:notEmpty>
<logic:empty name="bodyClass"><bean:define id="bodyClass">fondoBlanco</bean:define></logic:empty>
<logic:empty name="frameHeight"><bean:define id="frameHeight">24</bean:define></logic:empty>
<logic:notEmpty name="overflowY"><bean:define id="overflowY">${overflowY}</bean:define></logic:notEmpty>
<logic:empty name="overflowY"><bean:define id="overflowY">auto</bean:define></logic:empty>

<table border="0" width="100%" cellspacing="0" cellpadding="0">     
    <tr>
        <td style="border-style:solid;border-color:#CCCCCC;border-width:1px;border-bottom-width:0px;background-color: #f1f3f5;color: #666666;" height="${frameHeight}"><span class="textosVerdana12B">${frameContent}</span></td>
    </tr>
    <tr class="fila_contenido">
    	<td class="${bodyClass}" height="${bodyHeight}" style="padding-top:2px; padding-right:2px; padding-bottom:2px; padding-left:2px;">
        	<div style="width:100%;height:${bodyHeight};overflow-x:hidden;overflow-y:${overflowY};">
           		${bodyContent}                        
            </div> 
    	</td>
    </tr>
</table>