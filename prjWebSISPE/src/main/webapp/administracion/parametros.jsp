<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>  
<%-- ---------- INICIO - Lista de par&aacute;metros ---------- --%>
<logic:notEmpty name="administracionListadoForm" property="datos">
    <table border="0" width="98%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
        <tr>
            <td>
                <table id="cabeceraListaConceptos"  border="0" cellpadding="1" cellspacing="0" width="100%">
                    <tr class="tituloTablas"  align="left">
                        <td width="5%" align="center">C&Oacute;DIGO</td>
                        <td id= width="73%" class="columna_contenido" align="center">DESCRIPCI&Oacute;N</td>
                        <td width="12%" class="columna_contenido" align="center">VALOR</td>
                        <td width="10%" class="columna_contenido" align="center">EDICI&Oacute;N</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                    <table id="contenidoListaConceptos" border="0" cellpadding="1" cellspacing="0" width="100%">
                        <logic:iterate name="administracionListadoForm" property="datos" id="parametro" indexId="indiceRegistro">
                            <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                            <logic:equal name="residuo" value="0">
                                <bean:define id="colorBack" value="blanco10"/>
                            </logic:equal>
                            <logic:notEqual name="residuo" value="0">
                                <bean:define id="colorBack" value="grisClaro10"/>
                            </logic:notEqual>
                            <tr class="${colorBack}">
                                <td align="left" width="5%" class="columna_contenido fila_contenido"><bean:write name="parametro" property="id.codigoParametro"/></td>
                                <td align="left" width="73%" class="columna_contenido fila_contenido">
                                	<div id="descripcionP${indiceRegistro}">
                      					<%--bean:write name="parametro" property="descripcionParametro"/--%>
                      					${parametro.descripcionParametro}
                  					</div>
                                </td>
								<script language="JavaScript" type="text/javascript">truncar('descripcionP${indiceRegistro}', '${parametro.descripcionParametro}' ,105);</script>
                                <td align="right" width="12%" class="columna_contenido fila_contenido">
	                                <div id="descripcionV${indiceRegistro}">
	                   					<%--bean:write name="parametro" property="valorParametro"/--%>
	                   					${parametro.valorParametro}
	               					</div>
	               				</td>
	               				<script language="JavaScript" type="text/javascript">truncar('descripcionV${indiceRegistro}', '${parametro.valorParametro}' ,13);</script> 
                                <td align="center" width="10%" class="columna_contenido fila_contenido columna_contenido_der">
                                    <html:link action="actualizarParametro" paramName="indice" paramId="indice"><img src="./images/editar16.gif" border="0" alt="Editar Registro"></html:link>
                                </td>        	      					
                            </tr>	
                        </logic:iterate>
                    </table>
                </div>
            </td>
        </tr>    
    </table>	
</logic:notEmpty>

<%-- ---------- FIN - Lista de par&aacute;metros ---------- --%>  