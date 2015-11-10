<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<bean:define id="estadoActivo" name="sispe.estado.activo"/>
<%-- ---------- INICIO - Lista de Temporadas ---------- --%>
<logic:notEmpty name="administracionListadoForm" property="datos">
    <table border="0" width="99%" align="center" cellspacing="0" cellpadding="0" class="tabla_informacion">
        <tr>
            <td>
                <table border="0" cellpadding="1" cellspacing="0" width="100%">
                    <tr class="tituloTablas">
                        <td align="center" class="columna_contenido" width="5%">CODIGO</td>
                        <td align="center" class="columna_contenido" width="58%">DESCRIPCION</td>
                        <td align="center" class="columna_contenido" width="9%">FECHA INICIAL</td>
                        <td align="center" class="columna_contenido" width="9%">FECHA FINAL</td>
                        <td align="center" class="columna_contenido" width="7%">ESTADO</td>
                        <!-- <td align="center" class="columna_contenido" width="12%">ACCION</td> -->
                    </tr>
                </table>
            </td>
        </tr>
        
        <tr>
            <td>
                <div id="div_listado" style="width:100%;height:440px;overflow:auto">
                    <table border="0" cellpadding="1" cellspacing="0" width="100%">
                        <logic:iterate name="administracionListadoForm" property="datos" id="temporadas" indexId="indiceRegistro">
                            <bean:define id="indice" value="${indiceRegistro + administracionListadoForm.start}"/>
                            <bean:define id="residuo" value="${indiceRegistro % 2}"/>
                            <logic:equal name="residuo" value="0">
                                <bean:define id="colorBack" value="blanco10"/>
                            </logic:equal>	
                            <logic:notEqual name="residuo" value="0">
                                <bean:define id="colorBack" value="grisClaro10"/>
                            </logic:notEqual>
                            <tr class="${colorBack}">
                                <td align="left" width="5%" class="fila_contenido columna_contenido"><bean:write name="temporadas" property="id.codigoTemporada"/></td>
                                <td align="left" width="58%" class="fila_contenido columna_contenido"><bean:write name="temporadas" property="descripcionTemporada"/></td>
                                <td align="center" width="9%" class="fila_contenido columna_contenido"><bean:write name="temporadas" property="fechaInicialTemporada" formatKey="formatos.fecha"/></td>
                                <td align="center" width="9%" class="fila_contenido columna_contenido"><bean:write name="temporadas" property="fechaFinalTemporada" formatKey="formatos.fecha"/></td>
                                <td width="7%" align="center" class="fila_contenido columna_contenido" id="filaE_${indiceRegistro}">
                                    <logic:equal name="temporadas" property="estadoTemporada" value="${estadoActivo}">
                                        <img src="./images/exito_16.gif" alt="estado activo">
                                    </logic:equal>
                                    <logic:notEqual name="temporadas" property="estadoTemporada" value="${estadoActivo}">
                                        <img src="./images/parado.gif" alt="estado inactivo">
                                    </logic:notEqual>
                                </td>
                                <!-- 
                                <td align="center" width="12%" class="fila_contenido columna_contenido columna_contenido_der" id="filaA_${indiceRegistro}">
                                    <logic:equal name="temporadas" property="estadoTemporada" value="${estadoActivo}">
                                        <html:link href="#" onclick="requestAjax('temporadas.do', ['mensajes', 'filaE_${indiceRegistro}', 'filaA_${indiceRegistro}'], {parameters: 'indice=${indice}'});">
                                            Desactivar
                                        </html:link>
                                    </logic:equal>
                                    <logic:notEqual name="temporadas" property="estadoTemporada" value="${estadoActivo}"><b>Ninguna</b></logic:notEqual>
                                </td>  
                                 -->      	      					
                            </tr>	
                        </logic:iterate>
                        
                    </table>
                </div>
            </td>
        </tr>
        
    </table>
    
</logic:notEmpty> 
<%-- ---------- FIN - Lista de Temporadas ---------- --%>
