<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/> 

<bean:define id="estadoValidacionPago" name="ec.com.smx.sic.sispe.validacion.pagoTotal.despacho"/>
<bean:define name="sispe.estado.activo" id="estadoActivo"/>
<c:set var="alto_divs" value="480px"/>

<html:form action="despachoEspecial" method="post" enctype="multipart/form-data">	
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
                        <td  width="3%" align="center"><img src="./images/despachoEspecial.gif" border="0"></img></td>
                        <td height="35" valign="middle">Despachos especiales</td>
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
                        <td class="datos" width="16%" id="izquierda">
                        	<div style="width:100%;height:${alto_divs};overflow:auto;border-bottom:1px solid #cccccc">
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
	                                                            <tr title="la reserva debe despacharse antes de la fecha indicada">
	                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="amarilloClaro10" width="100%" height="12px"></td></tr></table></td>
	                                                                <td align="left" class="textoAzul10">Reserva con despacho próximo.</td>
	                                                            </tr>
	                                                            <tr title="la reserva no ha sido despachada">
	                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="rojoClaro10" width="100%" height="12px"></td></tr></table></td>
	                                                                <td align="left" class="textoAzul10">Reserva con despacho atrasado.</td>
	                                                            </tr>
	                                                            <tr title="la reserva en día de despacho">
	                                                                <td width="18%" valign="top"><table class="tabla_informacion" cellpadding="0" cellspacing="0" width="100%"><tr><td class="verdeClaro10" width="100%" height="12px"></td></tr></table></td>
	                                                                <td align="left" class="textoAzul10">Reserva en día de despacho.</td>
	                                                            </tr>
	                                                        </table>
	                                                    </td>
	                                                </tr>
	                                            </table>
	                                        </div>
	                                    </td>
	                                </tr>
	                            </table>
                            </div>                      
                        </td>
                        <%--Fin Barra Izquierda--%>
						
						<td id="divisor" height="${alto_divs}">
								<span style="display:block" id="img_ocultar">
									<a href="#"><img src="./images/spliter_izq.png" border="0"></a>
								</span>
								<span style="display:none" id="img_mostrar">
									<a href="#"><img src="./images/spliter_der.png" border="0"></a>
								</span>
						</td>
						
						<!-- se muestra el contenido de las paginas reservasPorDespachar.jsp y reservasDespachados.jsp-->
                        <td width="84%" valign="top" id="derecha" >
                        	<div style="height:${alto_divs};border-bottom:1px solid #cccccc">
                            	<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
                            </div>
                        </td>
                        <script language="JavaScript" type="text/javascript">divisor('divisor','izquierda','derecha','img_ocultar','img_mostrar');</script>
                    </tr>   
                </table>
            </td>
        </tr>
	</table>
</html:form>	

<tiles:insert page="/include/bottom.jsp"/>