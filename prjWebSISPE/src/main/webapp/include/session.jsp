<br><br>
<table border="1" align="center" width="80%" class="textos">
	<tr>
		<td><b>Variables de sessi&oacute;n</b>
		</td>
	</tr>
	<tr>
		<td>

			<%
						for (java.util.Enumeration enume = session.getAttributeNames();enume.hasMoreElements();) {
						out.println("  "+enume.nextElement()+"<br>");
					} 
			%>
		</td>
	</tr>
</table>