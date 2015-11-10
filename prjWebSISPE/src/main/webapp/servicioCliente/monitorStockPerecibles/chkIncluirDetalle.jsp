<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- Pantalla principal para el registro del pedido especial -->

<div id="popupConfirmarDetalleExcel">
<table class="" cellpadding="0" cellspacing="0" border="0" width="90%" align="center">
<tr>
	<td height="10px">
	Seleccione la opción si desea mostrar en el reporte los detalles de cada movimiento
	</td>
</tr>
</table>
</br>
	<table class="tabla_informacion fondoBlanco" cellpadding="0" cellspacing="0" border="0" width="90%" align="center">
	    <tr >
	        <td>
	            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" >
	                <tr>
	                    <td align="left" width="5%">
	                        <html:checkbox name="listadoPedidosForm" property="opConfirmarIncluirDetalle" title="active esta opci&oacute;n para incluir detalle movimientos" />
	                    </td>
	                    <td class="textoAzul11" align="left" >&nbsp;Incluir detalles de movimientos</td>
	                </tr>
	            </table>
	        </td>
	    </tr>
	   
	</table>
</div>