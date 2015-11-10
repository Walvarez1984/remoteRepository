<!-- POPUP QUE ALERTA CUANDO SE VA A BORRAR UN ARTICULO QUE TIENE ENTREGAS A DOMICILIO DESDE EL CD DESPACHADAS -->

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>

<logic:notEmpty name="vformAction">
	
	<table border="0" width="100%" cellpadding="0" cellspacing="5" >
	    <tr>
	    	<td>
	    	<table border="0" cellpadding="0" cellspacing="0">
	                <tr>
	                	<td valign="top" width="3%"><img src="images/advertencia.gif" border="0">&nbsp;&nbsp;</td> 
						<td align="left">
						<c:if test="${vformAction == 'crearCotizacion'}">
							El art&iacute;culo(s) que desea eliminar tiene entregas a domicilio que ya han sido despachadas,  
		                 	<b> ¿Est&aacute; seguro que desea eliminar el art&iacute;culo(s)?.</b>
						</c:if>
						<c:if test="${vformAction == 'detalleCanasta'}">
							  El canasto tiene entregas a domicilio que ya han sido despachadas. Al modificar el canasto se eliminar&aacute;n las entregas. 
		                 	<b> ¿Est&aacute; seguro que desea modificar el canasto de cat&aacute;logo?.</b>
						</c:if>
		                </td>
	                </tr>
	            </table>
	    	</td>
	    </tr>
	</table>	
</logic:notEmpty>