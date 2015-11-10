<%@page import="java.util.HashMap, ec.com.smx.sic.sispe.common.util.MessagesWebSISPE" %>
<%
{
	HashMap menus=(HashMap)session.getAttribute("menus");
	if(menus!=null){
		try{
			String menusetkey=MessagesWebSISPE.getString("security.CURRENT_SYSTEM_ID_PANEL");
		%>
		<%=(String)menus.get(menusetkey)%>
		<%
		}catch(Exception ex){
		}
	}
}
%>