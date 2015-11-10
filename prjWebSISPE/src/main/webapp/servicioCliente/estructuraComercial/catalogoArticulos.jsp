<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

 <div id="popUpBusquedaArticulosEstructuraComercial" class="popup" style="top:60px;">
     <bean:define name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" id="nivel"/>
     <bean:define name="ec.com.smx.sic.sispe.catalogoArticulos.estructura" id="clasificacion"/>
     <bean:define id="tituloDivision"><bean:message key="ec.com.smx.sic.sispe.catalogo.tituloDivision"/></bean:define>
     <bean:define id="tituloClasificacion"><bean:message key="ec.com.smx.sic.sispe.catalogo.tituloClasificacion"/></bean:define>
     
     <TABLE border="0" cellspacing="0" cellpadding="2" align="center" class="tabla_informacion" width="80%" bgcolor="#F1F3F5">            
             <tr>
				  <td background="images/barralogin.gif" align="center">
					  <table cellpadding="0" cellspacing="0" width="100%" border="0">			
						  <tr>
							  <td class="textoBlanco11" align="left">
								&nbsp;<b>B&uacute;squeda de art&iacute;culos</b>
							  </td>
							  <!-- Boton Cerrar -->
							  <td align="right">
								
								<html:link title="Cerrar" href="#" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','div_pagina'],{parameters: 'actualizarDetalle=ok&buscarPorEstructura=ok',evalScripts: true});">
									<img src="./images/close.gif" border="0" style="padding-top:3px;"/>
								</html:link>&nbsp;
							  </td>		                           
						  </tr>
					  </table>
				  </td>
			 </tr>
			 <tr>
					<td>
						<div id="mensajesPopUpArticulosEstCom" style="font-size:1px;position:relative;">
					<jsp:include page="/include/mensajes.jsp"/>	
						</div>
					</td>
			 </tr>
             <tr>
                 <td height="35px">
                     <table class="titulosAccion" border="0" width="100%" cellspacing="0" cellpadding="0">
                         <tr>
                             <td  width="4%" align="center"><img src="./images/lista.gif" border="0"></td>
                             <td align="left">Cat&aacute;logo de Art&iacute;culos</td>
		
                             <td align="right" width="5%">
                                 <table border="0" cellspacing="0" cellpadding="1" width="100%">
                                     <tr>
                                         <%-- esta condici&oacute;n permite mostrar el bot&oacute;n de agregar clasificaciones --%>
                                         <logic:notEmpty name="ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda">
                                             <logic:equal name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" value="${tituloClasificacion}">
                                                 <td>
                                                     <div id="botonA">
                                                         <a class="aceptarA" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','div_pagina'],{parameters: 'agregarClasificacion=ok&buscarPorEstructura=ok'});">Agregar</a>
                                                     </div>
                                                 </td>
                                             </logic:equal>
                                         </logic:notEmpty>
                                         <logic:notEqual name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" value="${tituloDivision}">
                                             <td>  
                                                 <div id="botonA">
                                                     <a class="atrasA" href="#" onclick="requestAjax('crearCotizacion.do',['mensajes','popUpBusquedaArticulosEstructuraComercial'],{parameters: 'atrasArticulos=ok&nivel=${nivel}&buscarPorEstructura=ok'});">Atras</a>
                                                 </div>
                                             </td>
                                         </logic:notEqual>
                                         <td>
                                             <bean:define id="exit" value="ok"/>
                                             <div id="botonA">	
                                                 <a href="#" class="cancelarA" onclick="javascript:requestAjax('crearCotizacion.do',['pregunta','div_pagina'],{parameters: 'actualizarDetalle=ok&buscarPorEstructura=ok',evalScripts: true});">Cerrar</a>
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
                       <table border="0" align="center" width="100%" cellspacing="0" cellpadding="0">
                           <logic:notEmpty name="ec.com.smx.sic.sispe.catalogoArticulos.catalogo">
                             <tr>
                                 <td>
                                     <table cellpadding="0" cellspacing="0" class="tabla_informacion" width="100%">
                                         <tr>
                                             <td align="left" height="20px" valign="middle" class="textoNegro11" colspan="2"><b><bean:write name="clasificacion"/></b></td>
                                         </tr>
                                     </table>
                                 </td>
                             </tr>
                             <tr><td height="5px"></td></tr>
                             <tr class="fila_titulo">
                                 <td>
                                     <table cellpadding="1" cellspacing="0" class="tabla_informacion" width="100%">
                                         <tr>
                                             <logic:notEmpty name="ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda">
                                                 <logic:equal name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" value="${tituloClasificacion}">
                                                     <td width="2%"><input type="checkbox" name="checkTodo" onclick="activarDesactivarTodo(this, buscarArticuloForm.checksClasificaciones);"></td>
                                                 </logic:equal>
                                             </logic:notEmpty>
                                             <td height="20px" align="left" class="textoAzul10" colspan="2"><b><bean:write name="nivel"/></b></td>
                                         </tr>
                                     </table>
                                 </td>
                             </tr>
                             <tr>
                                 <td colspan="2">
                                     <div style="width:100%;height:450px;overflow:auto;border:1px solid #cccccc">
                                         <table border="0" align="left" cellpadding="1" cellspacing="0" width="100%">
                                             <logic:iterate name="ec.com.smx.sic.sispe.catalogoArticulos.catalogo" id="clasificacionDTO" indexId="numeroRegistro">
                                                 <tr>
                                                     <td align="left">
                                                         <logic:empty name="ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda">
                                                             <html:link href="#" onclick="requestAjax('crearCotizacion.do',['popUpBusquedaArticulosEstructuraComercial','pregunta'],{parameters: 'numeroRegistro=${numeroRegistro}&descripcionNivel=${nivel}&clasificacion=${clasificacion}&buscarPorEstructura=ok'});">
                                                                 <bean:write name="clasificacionDTO" property="id.codigoClasificacion"/>&nbsp;-&nbsp;<bean:write name="clasificacionDTO" property="descripcionClasificacion"/>
                                                             </html:link>
                                                         </logic:empty>
                                                         <logic:notEmpty name="ec.com.smx.sic.sispe.clasificaciones.habilitarBusqueda">
                                                             <logic:notEqual name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" value="${tituloClasificacion}">
                                                                 <html:link href="#" onclick="requestAjax('crearCotizacion.do',['popUpBusquedaArticulosEstructuraComercial','pregunta'],{parameters: 'numeroRegistro=${numeroRegistro}&descripcionNivel=${nivel}&clasificacion=${clasificacion}&buscarPorEstructura=ok'});">
                                                                     <bean:write name="clasificacionDTO" property="id.codigoClasificacion"/>&nbsp;-&nbsp;<bean:write name="clasificacionDTO" property="descripcionClasificacion"/>
                                                                 </html:link>
                                                             </logic:notEqual>
                                                             <logic:equal name="ec.com.smx.sic.sispe.catalogoArticulos.nivel" value="${tituloClasificacion}">
                                                                 <table cellpadding="0" cellspacing="0" border="0">
                                                                     <tr>
                                                                         <td><html:multibox property="checksClasificaciones" value="${numeroRegistro}"/></td>
                                                                         <td><b><label class="textoCafe10"><bean:write name="clasificacionDTO" property="id.codigoClasificacion"/>&nbsp;-&nbsp;<bean:write name="clasificacionDTO" property="descripcionClasificacion"/></label></b></td>
                                                                     </tr>
                                                                 </table>
                                                             </logic:equal>
                                                         </logic:notEmpty>
                                                     </td>
                                                 </tr>
                                             </logic:iterate>
                                         </table>
                                     </div>
                                 </td>
                             </tr>
                         </logic:notEmpty>
                         <logic:empty name="ec.com.smx.sic.sispe.catalogoArticulos.catalogo">
                         	<table class="fondoBlanco textoNegro11" align="left" cellspacing="0" cellpadding="0" width="100%" height="400px">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
							</table>
                         </logic:empty>
                     </table>
                 </td>
             </tr>
     </TABLE>
 </div>