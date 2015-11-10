<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<!-- ventana llamada desde calendarizacion.jsp -->
<logic:notEmpty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
    <bean:define id="visible" value="visible"/>
    <script language="javascript">mostrarModalZ('frameModal21',120);</script>
</logic:notEmpty>	
<logic:empty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
    <bean:define id="visible" value="hidden"/>
   
</logic:empty>	

 
 
<div id="popup" class="popup" style="visibility:${visible};" style="top:50px;z-index:121;"> 
    <div id="center" class="popupcontenido">
        <table id="pp" border="0" width="300px" align="center" cellspacing="0" cellpadding="0" bgcolor="#F1F3F5" class="tabla_informacion">
            <tr>
                <td background="images/barralogin.gif" height="22px"></td>
            </tr>
            <tr>
                <td bgcolor="#F4F5EB">
					<div id="mensaje_popUp122" style="width:420px;position:relative;" >					
						<tiles:insert  page="/include/mensajes.jsp" />
					</div>
                    <table class="tabla_informacion" border="0" cellpadding="0" cellspacing="0" width="100%" align="center">
                        <tr>
                            <td class="textoNegro11" align="center">
                                <table border="0" width="99%">
                                    <tr>
                                        <td colspan="2">
                                            <table cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td width="3%"><img src="images/pregunta24.gif" border="0"></td>
                                                    <td align="center">
                                                        <logic:notEmpty name="ec.com.smx.calendarizacion.eliminarPlantilla" scope="request">
                                                            <bean:write name="ec.com.smx.calendarizacion.eliminarPlantilla"/>
                                                        </logic:notEmpty>	
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="center">
											<logic:notEmpty name="ec.com.smx.sic.sispe.mensajes">												
												<div id="botonDmin">
													<html:link styleClass="siDmin" href="#" onclick="requestAjax('calendarizacion.do', ['nuevoListado','confirmar','mensaje_popUp12'], {parameters: 'condicionSi=ok',popWait:true});ocultarModalID('frameModal21');">Si</html:link>
												</div>
											</logic:notEmpty>
											<logic:empty name="ec.com.smx.sic.sispe.mensajes">												
												<div id="botonDmin">
													<html:link styleClass="siDmin" href="#" onclick="requestAjax('calendarizacion.do', ['listado','confirmar','mensaje_popUp12','listadoPlantilla','plantillas','calendarioDiv','mensajes'], {parameters: 'condicionSi=ok',popWait:true});ocultarModalID('frameModal21');">Si</html:link>
												</div>
											</logic:empty>
                                        </td>
                                        <td align="center">
                                            <div id="botonDmin">                                                
                                                <html:link styleClass="noDmin" href="#"  onclick="hide(['popup']);ocultarModalID('frameModal21');">No</html:link>
                                            </div>	
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</div>