<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<tiles:useAttribute id="vformName" name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vtformAction" name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table  border="0"  cellspacing="1" cellpadding="0" width="98%" align="center">
    <tr>
        <td colspan="3">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                <tr>
                    <td align="center">
                        <div id="pregunta">
                            <logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
                                <tiles:insert page="/confirmacion/popUpConfirmacion.jsp"/>
                                <script language="javascript">mostrarModal();</script>
                            </logic:notEmpty>
                        </div>
                    </td>
                </tr>
            </table>
        </td>
    </tr>	
    <tr>
        <td colspan="3">
            <div id="mensajeDespacho">
                <table class="textoAzul11" border="0" width="100%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="3%" align="center"><img src="images/info_16.gif"></td>
                        <logic:notEmpty name="ec.com.smx.sic.sispe.pedidoEspecial.tipoPedidoUsuario">
                            <td align="left">
                                <logic:notEmpty name="ec.com.smx.sic.sispe.info.cerrarDia">
                                    <b>Primero debe cerrar el d&iacute;a, para realizar los despachos</b>
                                </logic:notEmpty>
                                <logic:empty name="ec.com.smx.sic.sispe.info.cerrarDia">
                                    <b>El día ya fue cerrado, ahora puede realizar los despachos</b>
                                </logic:empty>
                            </td>
                        </logic:notEmpty>
                        <logic:empty name="ec.com.smx.sic.sispe.pedidoEspecial.tipoPedidoUsuario">
                            <td align="left" class="textoRojo11">No se encuentra configurado el Rol de este usuario junto con el Tipo de Pedido en la tabla de Parámetros del sistema, comuníquese con el administrador.</td>
                        </logic:empty>
                    </tr>    
                </table>
            </div>
        </td>
    </tr>
    <tr><td height="5px"></td></tr>
    <tr>
        <%--Barra Izquierda--%>
        <td class="datos" id="izquierda" align="center">
            <table>
                <tr>
                    <td>
                        <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#ffffff" align="left" valign="top">
                        <div style="width:100%;height:85px;overflow:auto;solid #cccccc">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tabla_informacion">
                                <tr class="fila_titulo">
                                    <td width="15%">
                                        <img src="./images/datos_informacion24.gif" border="0">
                                    </td>
                                    <td align="left" class="textoNegro11">Informaci&oacute;n</td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <table align="left" cellpadding="0" border="0">
                                            <tr>
                                                <td width="20%" valign="top" align="center"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="80%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
                                                <td align="left" class="textoAzul10">Local de facturaci&oacute;n diferente a local de despacho</td>
                                            </tr>
                                            <tr>
                                                <td width="20%" valign="top" align="center"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="80%"><tr><td class="naranjaClaro10" width="100%" height="12px"></td></tr></table></td>
                                                <td align="left" class="textoAzul10">Inhabilitado (La fecha de despacho es mayor a mañana, debe habilitar el pedido)</td>
                                            </tr>
                                            <tr>
                                                <td width="20%" valign="top" align="center"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="80%"><tr><td class="rojoObsuro10" width="100%" height="12px"></td></tr></table></td>
                                                <td align="left" class="textoAzul10">Cantidades ingresadas en cero</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
            
        </td>
        <td class="datos" width="8px">&nbsp;</td>
        <logic:notEmpty name="ec.com.smx.sic.sispe.paginaTab">
            <td valign="top" width="76%">
                <tiles:insert page="/controlesUsuario/controlTab.jsp"/>
            </td>
        </logic:notEmpty>
    </tr>
    <tr>
        <td id="seccionImpresion">
            <logic:notEmpty name="ec.com.smx.sic.sispe.funcionImprimir">
                <script language="JavaScript">window.print();</script>
            </logic:notEmpty>
        </td>
    </tr>
</table>