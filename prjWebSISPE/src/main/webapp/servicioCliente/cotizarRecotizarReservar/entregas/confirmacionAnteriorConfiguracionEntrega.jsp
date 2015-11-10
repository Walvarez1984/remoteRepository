<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!-- ventana insertada desde entregaArticuloLocal.jsp -->
<logic:notEmpty name="ec.com.smx.sic.confirmacionanteriorentregas" scope="request">
	<bean:define id="visible" value="visible"/>
	<script language="javascript">
		//mostrarModalZ('frameModal2',120);
	</script>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.sic.confirmacionanteriorentregas" scope="request">
	<bean:define id="visible" value="hidden"/>	
</logic:empty>	
<div id="popup" class="popup" style="visibility:${visible};" style="top:100px;z-index:121;"> 	
	<table border="0" cellpadding="0" cellspacing="0" align="center" width="50%">
		<tr>
			<td valign="top" align="center">
			    <div id="center" class="popupcontenido">
					<%-- Contenido de la ventana de confirmacion --%>					
			        <table border="0" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" width="62%">
			            <tr>
			                <td background="images/barralogin.gif" align="center">
			                    <table cellpadding="0" cellspacing="0" width="100%" border="0">			                        
                                    <tr>
			                            <td class="textoBlanco11" align="left" height="20px">&nbsp;
			                                <b>Confirmaci&oacute;n</b>
			                            </td>
			                            <td align="right">										

											 <html:link styleClass="noDmin" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta2','popupConfirmar'], {parameters: 'seleccionaCANCEL=ok',popWait:false});ocultarModalID('frameModal2');">
												<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
											 </html:link>										                                                                                    
			                            </td>		                           
			                        </tr>
			                    </table>
			                </td>
			            </tr>
			            <tr>
			                <td bgcolor="#F4F5EB" valign="top">	                            
			                    <table class="tabla_informacion textoNegro11" border="0" width="100%" align="center" cellpadding="0" cellspacing="5">			                       								
	                                <tr>								   		                                                 
                                        <td>
                                        	<table class="tabla_informacion fondoBlanco" border="0" width="500px" align="center" cellpadding="0" cellspacing="5">
                                            	<tr>
                                                	<td valign="top"><img src="./images/advertencia.gif" border="0" style="padding-top:3px;"/></td>
                                                	<td class="textoAzul12" align="left">
                                                    	Est&aacute; seguro que desea regresar, se perder&aacute; la configuraci&oacute;n ya realizada.
                                                    </td>
                                                </tr>                                                
                                            </table>                                            
                                        </td>
                                    </tr>
                                    <tr>
                                    	<td align="center">
                                            <table cellpadding="0" cellspacing="0" border="0">
                                                <tr>                                                                                                   
                                                    <td><div id="botonD">
													
													<html:link styleClass="aceptarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta2','popupConfirmar'], {parameters: 'seleccionaOK=ok',popWait:true,evalScripts:true});ocultarModalID('frameModal2');mostrarModal();">Si</html:link>
													</div></td>
													<td>&nbsp;</td>
                                                    <td><div id="botonD">
													<html:link styleClass="cancelarD" href="#" onclick="requestAjax('entregaLocalCalendario.do', ['pregunta2','popupConfirmar'], {parameters: 'seleccionaCANCEL=ok',popWait:false});ocultarModalID('frameModal2');mostrarModal();">No</html:link></div></td>
                                                </tr>
                                            </table>
                                        </td>
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