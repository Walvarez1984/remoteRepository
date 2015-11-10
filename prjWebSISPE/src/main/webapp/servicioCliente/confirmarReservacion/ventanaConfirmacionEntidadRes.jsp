<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<tiles:useAttribute id="vformName" name="vtformName" classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction" name="vtformAction" classname="java.lang.String" ignore="true"/>
<table class="textoNegro11" border="0" width="100%" cellpadding="0" cellspacing="5">
    <tr>
        <td align="left">
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                	<td valign="top" width="3%"><img src="images/pregunta24.gif" border="0">&nbsp;&nbsp;</td> 
	                 <logic:notEmpty name="ec.com.smx.sic.sispe.tipoMensajeValidacion">
						<td align="left">
		                 	Existen solo canastas de catálago en el pedido y no existen entregas a domicilio, es preferible que la entidad responsable sea el Local. 
		                </td>	                 
	                 </logic:notEmpty>
	                 <logic:empty name="ec.com.smx.sic.sispe.tipoMensajeValidacion">
	                 	<td align="left">
		                 	La entidad responsable no puede ser la bodega de canastos porque no existen CANASTOS cuya cantidad individual sea mayor o igual a 50 o la sumatoria de estos sea mayor o igual a 80. 
		                </td>
	                 </logic:empty>   
                </tr>
                <tr>
                	<td valign="top" width="3%">&nbsp;&nbsp;</td>
                	<td align="left" colspan="2">
                	 ¿Desea cambiar como el responsable del pedido al Local?
                	</td>
                </tr>
                <tr>
                	<td valign="top" width="3%">&nbsp;&nbsp;</td>
                	<td align="left" colspan="2">
                	 &nbsp;&nbsp;-Si presiona el boton Si, La bodega le despachará el pedido.
                	</td>
                </tr>
                <tr>
                	<td valign="top" width="3%">&nbsp;&nbsp;</td>
                	<td align="left" colspan="2">
	                 &nbsp;&nbsp;-Si presiona el boton No, regresará al formulario donde deberá configurar de nuevo las entregas del pedido.
                	</td>
                </tr>
            </table>
        </td>
    </tr>                                 
</table>	