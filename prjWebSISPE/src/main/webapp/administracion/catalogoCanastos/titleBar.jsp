<%@ taglib uri="/WEB-INF/struts-html.tld"  prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<tiles:useAttribute id="vformName"   name="vtformName"  classname="java.lang.String" ignore="true"/>
<tiles:useAttribute id="vformAction"   name="vtformAction"  classname="java.lang.String" ignore="true"/>

<table cellspacing="3" cellpadding="0">
    <tr>		
        <logic:equal name="vformAction" value="catalogoCanastos">
            <td><div id="botonA"><html:link styleClass="enviar_mailA" href="#"  onclick="requestAjax('catalogoCanastos.do',['mensajes','envioMail'],{parameters: 'ingresoMailContacto=ok', evalScripts: true});">Enviar</html:link></div></td>
            <!-- <td><div id="botonA"><html:link styleClass="enviar_mailA"   href="#" onclick="ealizarEnvioDesdeBarraDeTitulo;('nuevo','${vformName}');">Enviar</html:link></div></td> -->
            <td><div id="botonA"><html:link styleClass="inicioA" action="menuPrincipal.do">Inicio</html:link></div></td>
        </logic:equal>       
    </tr>
</table>