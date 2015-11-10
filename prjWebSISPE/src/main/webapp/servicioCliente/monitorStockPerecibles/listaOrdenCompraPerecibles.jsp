<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/smx-formulario.tld" prefix="smx"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:insert page="/include/top.jsp"/>
<div id="divTabs">
<html:form action="enviarOCPerecibles" method="post">
<TABLE border="0" cellspacing="0" cellpadding="0" class="textoNegro11" width="100%" align="center">
        <html:hidden property="ayuda" value=""/>
		<tr>
			<td>
				<div id="pregunta">
					<logic:notEmpty name="ec.com.smx.sic.sispe.mostrarConfirmacion">
						<jsp:include page="../confirmacion/confirmacion.jsp"/>
						<script language="javascript">mostrarModal();</script>
					</logic:notEmpty>
					<logic:notEmpty name="ec.com.smx.sic.sispe.popUpConfirmacion">
						<tiles:insert page="/confirmacion/popUpConfirmacion.jsp">
							<tiles:put name="vtformAction" value="crearCotizacion"/>
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
                        <td  width="3%" align="center"><img src="./css/images/monitorPerecibles.gif" border="0"></img></td>
                        <td height="35" valign="middle">
                       		Monitor de stock/Perecibles
                        </td>
                        <td align="right" valign="top">
                            <table border="0">
                                <tr>
									<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.filtros.locales">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="excelA" 
													onclick="requestAjax('enviarOCPerecibles.do', ['pregunta','mensajes','idArtTemDet'], {parameters: 'incluirDetalle=ok', popWait:false, evalScripts:true});ocultarModal();">
													Crear XLS
												</html:link>
											</div>
										</td>
									</logic:notEmpty>
									
									<logic:notEmpty name="ec.com.smx.sic.sispe.mostrar.orden.compra">
										<td>
											<div id="botonA">
												<html:link href="#" styleClass="ordenCompraA"
													onclick="requestAjax('enviarOCPerecibles.do', ['pregunta','mensajes','idArtTemDet','div_pagina'], {parameters: 'ordenCompra=ok', evalScripts:true});">
													O.compra
												</html:link>
											</div>
										</td>
									</logic:notEmpty>
									
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
        <%--Contenido de la p&aacute;gina--%>
        <tr>
            <td align="center" valign="top">
            	<tiles:insert page="/controlesUsuario/controlTab.jsp"/>
            </td>
        </tr>

</TABLE>
</html:form>
</div>
<tiles:insert page="/include/bottom.jsp"/>