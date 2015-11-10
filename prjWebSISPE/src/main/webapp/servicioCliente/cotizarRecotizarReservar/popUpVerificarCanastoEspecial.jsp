<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table border="0" width="100%" cellpadding="0" cellspacing="5">
    <tr>
        <td align="left">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                	<td valign="top" width="3%"><img src="images/info48.gif" border="0">&nbsp;&nbsp;</td>
                	 
	                <td align="left">
	                 	El canasto especial ya ha sido creado anteriormente con el código de barras: <b><bean:write name="ec.com.smx.sic.sispe.pedido.articulo.canastaespecial" property="codigoBarrasActivo.id.codigoBarras"/></b>, en la fecha: <b><bean:write name="ec.com.smx.sic.sispe.pedido.articulo.canastaespecial" property="codigoBarrasActivo.fechaRegistro" formatKey="formatos.fechahora" /></b>, si presiona Aceptar se verificará la existencia de éste artículo en el SIC.
	                </td>
                </tr>

            </table>
        </td>
    </tr>                                 
</table>