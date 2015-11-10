<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="org.apache.struts.Globals"%>
<%@ page import="org.apache.struts.taglib.TagUtils"%>
<%@ page import="org.apache.struts.action.ActionMessages"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
final int TAMANIO_LETRA = 14;
final int TAMANIO_ICONO = 26;
/*
Integer nSucces = new Integer(0);
Integer nInfos = new Integer(0);
Integer nWarnings = new Integer(0);
Integer nErrors = new Integer(0);

Integer cantidadMensajes = new Integer(0);
session.setAttribute("cantidadMensajes1", cantidadMensajes);
*/
session.setAttribute("nSucces", new Integer(0));
session.setAttribute("nInfos", new Integer(0));
session.setAttribute("nWarnings", new Integer(0));
session.setAttribute("nErrors", new Integer(0));
 
try{
    ActionMessages errors = TagUtils.getInstance().getActionMessages(pageContext, Globals.ERROR_KEY);
    ActionMessages messages =  TagUtils.getInstance().getActionMessages(pageContext, Globals.MESSAGE_KEY);
    ActionMessages infos = (ActionMessages) request.getAttribute(ec.com.smx.framework.web.action.Globals.INFOS_KEY);
    ActionMessages warnings = (ActionMessages) request.getAttribute(ec.com.smx.framework.web.action.Globals.WARNINGS_KEY);
    
    infos = (infos == null) ? new ActionMessages() : infos;
    warnings = (warnings == null) ? new ActionMessages() : warnings;
    
    //Mensajes de EXITO
    if (!messages.isEmpty()) {
        
        /*
        cantidadMensajes = (Integer) session.getAttribute("cantidadMensajes1");
        cantidadMensajes =  new Integer(cantidadMensajes.intValue() + messages.size() * TAMANIO_LETRA + TAMANIO_ICONO);
        session.setAttribute("cantidadMensajes1", cantidadMensajes);
        */
        session.setAttribute("nSucces", new Integer(messages.size() * TAMANIO_LETRA + TAMANIO_ICONO));
        
%>
		<div id="succes" style="font-size:1px;position:absolute;left:0px;width:100%;z-index:119;">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			    <tr>
			        <td style="background:url(images/exito_esq_sup_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/exito_sup.gif)"></td>
			        <td style="background:url(images/exito_esq_sup_der.gif)" height="5" width="5"></td>
			    </tr>
			    <tr>
			        <td style="background:url(images/exito_izq.gif)" height="5" width="5"></td>
			        <td bgcolor="#E4F1D8">
			            <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			                <tr>
			                    <td width="26" align="center"><img src="./images/exito.gif"/></td>
			                    <td class="textoNegro13" align="left">&nbsp;Exito<br></td>
                                            <td align="right">
                                                <div id="botonC">
                                                    <html:link href="#" styleClass="exitoC" onclick="closeMessages('succes')" title="cerrar mensaje"/>
                                                </div>
                                            </td>
			                </tr>
			            </table>
			        </td>
			        <td style="background:url(images/exito_der.gif)" height="5" width="5"></td>
			    </tr>
			    <!-- Mensajes -->
			    <html:messages id="message" message="true">
			        <tr>
			            <td style="background:url(images/exito_izq.gif)" height="5" width="5"></td>
			            <td bgcolor="#E4F1D8">
			                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="left" class="textoNegro11">
			                    <tr>
			                        <td width="32"></td>
			                        <td align="left"><img src="./images/vinieta.gif"/>&nbsp;<bean:write name="message"/><br></td>
			                    </tr>
			                </table>
			            </td>
			            <td style="background:url(images/exito_der.gif)" height="5" width="5"></td>
			        </tr>
			    </html:messages>
			    <tr>
			        <td style="background:url(images/exito_esq_inf_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/exito_inf.gif)"></td>
			        <td style="background:url(images/exito_esq_inf_der.gif)" height="5" width="5"></td>
			    </tr>
			</table>
		</div>
<%
    }
    
    //Mensajes de INFORMACION
    if (!infos.isEmpty()) {
        /*
        cantidadMensajes = (Integer) session.getAttribute("cantidadMensajes1");
        cantidadMensajes =  new Integer(cantidadMensajes.intValue() + infos.size() * TAMANIO_LETRA + TAMANIO_ICONO);
        session.setAttribute("cantidadMensajes1", cantidadMensajes);
        */
        session.setAttribute("nInfos", new Integer(infos.size() * TAMANIO_LETRA + TAMANIO_ICONO));
%>
		<div id="infos" style="font-size:1px;position:absolute;left:0px;width:100%;z-index:119;">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			    <tr>
			        <td style="background:url(images/info_esq_sup_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/info_sup.gif)"></td>
			        <td style="background:url(images/info_esq_sup_der.gif)" height="5" width="5"></td>
			    </tr>
			    <tr>
			        <td style="background:url(images/info_izq.gif)" height="5" width="5"></td>
			        <td bgcolor="#E0F2FE">
			            <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			                <tr>
			                    <td width="26" align="center"><img src="./images/info.gif"/></td>
			                    <td class="textoNegro13" align="left">&nbsp;Informaci&oacute;n<br></td>
                                            <td align="right">
                                                <div id="botonC">
                                                    <html:link href="#" styleClass="informacionC" onclick="closeMessages('infos')" title="cerrar mensaje"/>
                                                </div>
                                            </td>
			                </tr>
			            </table>
			        </td>
			        <td style="background:url(images/info_der.gif)" height="5" width="5"></td>
			    </tr>
			    <!-- Mensajes -->
			    <html:messages id="info" name="<%=ec.com.smx.framework.web.action.Globals.INFOS_KEY %>">
			        <tr>
			            <td style="background:url(images/info_izq.gif)" height="5" width="5"></td>
			            <td bgcolor="#E0F2FE">
			                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" class="textoNegro11">
			                    <tr>
			                        <td width="32"></td>
			                        <td align="left"><img src="./images/vinieta.gif"/>&nbsp;<bean:write name="info"/><br></td>
			                    </tr>
			                </table>
			            </td>
			            <td style="background:url(images/info_der.gif)" height="5" width="5"></td>
			        </tr>
			    </html:messages>
			    <tr>
			        <td style="background:url(images/info_esq_inf_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/info_inf.gif)"></td>
			        <td style="background:url(images/info_esq_inf_der.gif)" height="5" width="5"></td>
			    </tr>
			</table>
		</div>

<%
    }
    
    //Mensajes de ADVERTENCIA
    if (!warnings.isEmpty()) {
        /*
        cantidadMensajes = (Integer) session.getAttribute("cantidadMensajes1");
        cantidadMensajes =  new Integer(cantidadMensajes.intValue() + warnings.size() * TAMANIO_LETRA + TAMANIO_ICONO);
        session.setAttribute("cantidadMensajes1", cantidadMensajes);
        */
        session.setAttribute("nWarnings", new Integer(warnings.size() * TAMANIO_LETRA + TAMANIO_ICONO));
%>

		<div id="warnings" style="font-size:1px;position:absolute;left:0px;width:100%;z-index:119;">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			    <tr>
			        <td style="background:url(images/warning_esq_sup_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/warning_sup.gif)"></td>
			        <td style="background:url(images/warning_esq_sup_der.gif)" height="5" width="5"></td>
			    </tr>
			    <tr>
			        <td style="background:url(images/warning_izq.gif)" height="5" width="5"></td>
			        <td bgcolor="#FFF8CE">
			            <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			                <tr>
			                    <td width="26" align="center"><img src="./images/advertencia.gif"/></td>
			                    <td class="textoNegro13" align="left">&nbsp;Advertencia<br></td>
                                            <td align="right">
                                                <div id="botonC">
                                                    <html:link href="#" styleClass="advertenciaC" onclick="closeMessages('warnings')" title="cerrar mensaje"/>
                                                </div>
                                            </td>
                                            
			                </tr>
			            </table>
			        </td>
			        <td style="background:url(images/warning_der.gif)" height="5" width="5"></td>
			    </tr>
			    <!-- Mensajes -->
			    <html:messages id="warning" name="<%=ec.com.smx.framework.web.action.Globals.WARNINGS_KEY %>">
			        <tr>
			            <td style="background:url(images/warning_izq.gif)" height="5" width="5"></td>
			            <td bgcolor="#FFF8CE">
			                <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" class="textoNegro11">
			                    <tr>
			                        <td width="32"></td>
			                        <td align="left"><img src="./images/vinieta.gif"/>&nbsp;<bean:write name="warning"/><br></td>
			                    </tr>
			                </table>
			            </td>
			            <td style="background:url(images/warning_der.gif)" height="5" width="5"></td>
			        </tr>
			    </html:messages>
			    <tr>
			        <td style="background:url(images/warning_esq_inf_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/warning_inf.gif)"></td>
			        <td style="background:url(images/warning_esq_inf_der.gif)" height="5" width="5"></td>
			    </tr>
			</table>
		</div>

<%
    }
    
    //Mensajes de ERROR
    if (!errors.isEmpty()){
        /*
        cantidadMensajes = (Integer) session.getAttribute("cantidadMensajes1");
        cantidadMensajes =  new Integer(cantidadMensajes.intValue() + errors.size() * TAMANIO_LETRA + TAMANIO_ICONO);
        session.setAttribute("cantidadMensajes1", cantidadMensajes);
        */
        session.setAttribute("nErrors", new Integer(errors.size() * TAMANIO_LETRA + TAMANIO_ICONO));
%>

		<div id="errors" style="font-size:1px;position:absolute;left:0px;width:100%;z-index:119;">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
			    <tr>
			        <td style="background:url(images/error_esq_sup_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/error_sup.gif)"></td>
			        <td style="background:url(images/error_esq_sup_der.gif)" height="5" width="5"></td>
			    </tr>
			    <tr>
			        <td style="background:url(images/error_izq.gif)" height="5" width="5"></td>
			        <td bgcolor="#F6E1E8">
			            <table border="0" cellspacing="0" cellpadding="0" width="100%" align="center" class="textoNegro13">
			                <tr>
			                    <td width="26" align="center"><img src="./images/error.gif"/></td>
			                    <td align="left">&nbsp;Error<br></td>
                                            <td align="right">
                                                <div id="botonC">
                                                    <html:link href="#" styleClass="errorC" onclick="closeMessages('errors')" title="cerrar mensaje"/>
                                                </div>
                                            </td>
			                </tr>
			            </table>
			        </td>
			        <td style="background:url(images/error_der.gif)" height="5" width="5"></td>
			    </tr>
			    <!-- Mensajes -->
			    <tr>
			        <td style="background:url(images/error_izq.gif)" height="5" width="5"></td>
			        <td bgcolor="#F6E1E8" align="left">
			            <html:errors/>
			        </td>
			        <td style="background:url(images/error_der.gif)" height="5" width="5"></td>
			    </tr>
			    <tr>
			        <td style="background:url(images/error_esq_inf_izq.gif)" height="5" width="5"></td>
			        <td style="background:url(images/error_inf.gif)"></td>
			        <td style="background:url(images/error_esq_inf_der.gif)" height="5" width="5"></td>
			    </tr>
			</table>
		</div>

<%
	}

} catch(Exception e) {
	
	}
%>

    <%--
    <input id="hiddensucces" type="hidden" value="<%=((Integer) session.getAttribute("nSucces")).toString()%>"/>
    <input id="hiddeninfos" type="hidden" value="<%=((Integer) session.getAttribute("nInfos"))%>"/>
    <input id="hiddenwarnings" type="hidden" value="<%=((Integer) session.getAttribute("nWarnings")).toString()%>"/>
    <input id="hiddenerrors" type="hidden" value="<%=((Integer) session.getAttribute("nErrors")).toString()%>"/>
    <input id="hiddenMessages" type="hidden" value="<%=570 - ((Integer) session.getAttribute("nSucces")).intValue() - ((Integer) session.getAttribute("nInfos")).intValue() - ((Integer) session.getAttribute("nWarnings")).intValue() - ((Integer) session.getAttribute("nErrors")).intValue()%>px"/>
    --%>
    <input id="hiddensucces" type="hidden" value="${nSucces}"/>
    <input id="hiddeninfos" type="hidden" value="${nInfos}"/>
    <input id="hiddenwarnings" type="hidden" value="${nWarnings}"/>
    <input id="hiddenerrors" type="hidden" value="${nErrors}"/>
    <input id="hiddenMessages" type="hidden" value="${567}px"/>