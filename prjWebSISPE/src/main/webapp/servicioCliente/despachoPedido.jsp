<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/> 

<bean:define id="estadoValidacionPago" name="ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"/>
<bean:define name="sispe.estado.activo" id="estadoActivo"/>

<html:form action="despachoPedido" method="post" enctype="multipart/form-data">
	<table border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
    	<html:hidden property="ayuda" value=""/>
        <%-- barra principal --%>
        <tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="despachoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacionAux">
						<tiles:insert page="/confirmacion/popUpConfirmacionAux.jsp">
							<tiles:put name="vtformAction" value="despachoPedido"/>
						</tiles:insert>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
				</div>
			</td>
		</tr>
        <tr>
            <td align="left" valign="top" width="100%">
                <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" class="titulosAccion">
                    <tr>
                        <td  width="3%" align="center"><img src="./images/despachoPedidos.gif" border="0"></img></td>
                        <td height="35" valign="middle">Despacho de pedidos</td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
                                    <td>
                                        <bean:define id="exit" value="exit"/>
                                        <div id="botonA">	
                                            <html:link action="menuPrincipal" paramId="salir" paramName="exit" styleClass="inicioA" >Inicio</html:link>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top">
                <table border="0" class="textos" width="99%" align="center">
                    <tr height="2"><td></td></tr>
                    <tr>
                        <%--Barra Izquierda--%>
                        <td class="datos" width="25%">
                            <table width="100%" border="0"  cellpadding="1" cellspacing="0" bgcolor="white">
                                <%-- B&uacute;squeda--%>
                                <tr>
                                    <td colspan="2">
                                        <tiles:insert page="/servicioCliente/busqueda/seccionBusqueda.jsp"/>
                                    </td>
                                </tr>
                            </table>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td height="5px"></td></tr></table>
                            <table width="100%" border="0" class="tabla_informacion" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td>
                                        <div style="width:100%;height:82px;overflow-x:hidden;overflow:auto">
                                            <table cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td class="fila_titulo" colspan="2">
                                                        <table cellpadding="0" cellspacing="0" border="0" width="100%" align="center">
                                                            <tr>
                                                                <td width="18%"><img src="images/datos_informacion24.gif" border="0"/></td>
                                                                <td width="85%" class="textoNegro11">Informaci&oacute;n</td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="left" height="25px" width="100%">
                                                        <table border="0" align="left">
                                                            <tr title="todos los art&iacute;culos del pedido han sido seleccionados para el despacho">
                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
                                                                <td align="left" class="textoAzul10">Despacho total</td>
                                                            </tr>
                                                            <tr title="solo algunos art&iacute;culos del pedido se han seleccionado para el despacho">
                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="amarilloClaro10" width="100%" height="12px"></td></tr></table></td>
                                                                <td align="left" class="textoAzul10">Despacho parcial</td>
                                                            </tr>
                                                            <tr title="ning&uacute;n art&iacute;culo del pedido ha sido despachado">
                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
                                                                <td align="left" class="textoAzul10">Sin despacho</td>
                                                            </tr>
                                                            <logic:equal name="estadoValidacionPago" value="${estadoActivo}">
                                                                <tr>
                                                                    <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="naranjaClaro10" width="100%" height="12px"></td></tr></table></td>
                                                                    <td align="left" class="textoAzul10">No se ha abonado totalmente</td>
                                                                </tr>
                                                            </logic:equal>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </table>                            
                        </td>
                        <%--Fin Barra Izquierda--%>
						
                        <td class="datos" width="2px">&nbsp;</td>
                        <td valign="top">
                            <tiles:insert page="/controlesUsuario/controlTab.jsp"/>
                        </td>
                    </tr>   
                </table>
            </td>
        </tr>
	</table>
</html:form>
<tiles:insert page="/include/bottom.jsp"/>