/*
 * Creado el 03/05/2006
 */
package ec.com.smx.sic.sispe.web.servicioCliente.struts.action;

import static ec.com.smx.sic.sispe.common.constantes.GlobalsStatics.CALCULO_PRECIOS_AFILIADO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ec.com.smx.framework.web.action.BaseAction;
import ec.com.smx.framework.web.action.Globals;
import ec.com.smx.mensajeria.estructura.MailMensajeEST;
import ec.com.smx.sic.sispe.common.util.EstadoPedidoUtil;
import ec.com.smx.sic.sispe.common.util.MessagesWebSISPE;
import ec.com.smx.sic.sispe.common.util.WebSISPEUtil;
import ec.com.smx.sic.sispe.commons.util.LogSISPE;
import ec.com.smx.sic.sispe.dto.VistaPedidoDTO;
import ec.com.smx.sic.sispe.web.servicioCliente.struts.form.EnvioMailForm;
import ec.com.smx.sic.sispe.web.session.SessionManagerSISPE;

/**
 * La clase <code>EnvioPedidooAction</code> controla el popUp que permite enviar mails
 * desde p\u00E1ginas que utilizan <code>VistaPedidoDTO</code>.
 * 
 * @author 	fmunoz
 * @author 	mbraganza
 * @version	3.0
 * @since	JSDK 1.5.0
 */
public class EnvioMailAction extends BaseAction
{
	/**
	 * Procesa la petici\u00F3n HTTP (request) especificada y genera su correspondiente respuesta HTTP (response) (o lo redirige a otro componente web que podr\u00EDa crear)
	 * Devuelve una instancia <code>ActionForward</code> que describe d\u00F3nde y c\u00F3mo se redirige el control
	 * (si la respuesta se ha completado <code>null</code>)
	 * 
	 * @param mapping					El mapeo utilizado para seleccionar esta instancia
	 * @param form						El formulario asociado a esta acci\u00F3n de donde se toman y establecen valores de
	 *          							campos
	 * @param request 				La petici&oacute;n que estamos procesando
	 * @param response				La respuesta HTTP que se genera
	 * @return ActionForward	Acci&oacuten o p&aacute;gina jsp donde se redirige el control 
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		ActionMessages errors = new ActionMessages();
		ActionMessages infos = new ActionMessages();
//		ActionMessages exitos = new ActionMessages();
		HttpSession session = request.getSession();
		String salida = "desplegar";
		String ayuda= request.getParameter(Globals.AYUDA);
//		BeanSession beanSession = SessionManagerSISPE.getBeanSession(request);
		EnvioMailForm formulario = (EnvioMailForm)form;
		String estadoActivo = SessionManagerSISPE.getEstadoActivo(request);
		
		LogSISPE.getLog().info("ayuda: {}",ayuda);
		try{
			
			if(request.getParameter("cancelarMail")!=null){
				session.removeAttribute("ec.com.smx.sic.sispe.redactarMail");	
				session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
			}else if(ayuda!=null && ayuda.equals("siEnviarEmail")){
				VistaPedidoDTO vistaPedido=(VistaPedidoDTO)request.getSession().getAttribute(EstadoPedidoUtil.VISTA_PEDIDO);
				request.getSession().setAttribute(EstadoPedidoUtil.ACCION_ORIGEN, "ESTADO_PED");
				if(vistaPedido.getEstadoPreciosAfiliado()!=null && vistaPedido.getEstadoPreciosAfiliado().equals(estadoActivo)){
					session.setAttribute(CALCULO_PRECIOS_AFILIADO, "ok");
				}
				
				EstadoPedidoUtil.actualizarArticuloStockIntegracionSic(request, errors);
				
				//envio automatico del mail
				LogSISPE.getLog().info("empieza el envio..");				
				//obtengo el mail del Local y mail del Usuario
				String mailLocal = SessionManagerSISPE.getCurrentEntidadResponsable(request).getEmailLocal();
				String mailUsuario = SessionManagerSISPE.getCurrentEntidadResponsable(request).getEmailUser();
				
				LogSISPE.getLog().info("Mail Local---{}",mailLocal);
				LogSISPE.getLog().info("Mail Usuario---{}",mailUsuario);
				
				WebSISPEUtil.inicializarParametrosImpresion(request, estadoActivo, false);
				try{
					
					MailMensajeEST mailMensajeEST = WebSISPEUtil.construirEstructuraMail(request, MessagesWebSISPE.getString("ec.com.smx.sic.sispe.evento.plantillaMail.envioPedido"));
					if(formulario.getCcMail()!=null && !formulario.getCcMail().isEmpty()){
						mailMensajeEST.setCc(new String [] {formulario.getCcMail()});
					}
					mailMensajeEST.setPara(new String [] {formulario.getEmailEnviarCotizacion()});
					session.setAttribute("ec.com.smx.sic.sispe.pedido.fechaPedido",vistaPedido.getFechaInicialEstado());
					mailMensajeEST.setAsunto(formulario.getAsuntoMail());
//					mailMensajeEST.setMensaje(formulario.getTextoMail());
					mailMensajeEST.setFormatoHTML(true);
					LogSISPE.getLog().info("Nombre del Mail seteado en el objeto---{}",mailMensajeEST.getDe().toString());
					LogSISPE.getLog().info("CodigoEvento:::::: {}",mailMensajeEST.getEventoDTO().getId().getCodigoEvento());
					request.setAttribute("ec.com.smx.sic.sispe.envioMail", mailMensajeEST);
					formulario.setTextoMail(formulario.getTextoMail().replace("\n", "<br>"));
					request.setAttribute("ec.com.smx.sic.sispe.textoMail", formulario.getTextoMail());
					infos.add("envioMail",new ActionMessage("message.mail.send.exito"));
					session.removeAttribute("ec.com.smx.sic.sispe.redactarMail");
					session.removeAttribute(SessionManagerSISPE.MENSAJES_SISPE);
				}catch(Exception ex){
					LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
					errors.add("envioMail",new ActionMessage("errors.mail.send.error",ex.getMessage()));
				}
			}
		}
		catch(Exception ex){
			//excepcion desconocida
			LogSISPE.getLog().error("error de aplicaci\u00F3n",ex);
			salida = "errorGlobal";
		}
		saveErrors(request, errors);
		saveInfos(request, infos);
		// se envia el control a la p\u00E1gina correspondiente
		return mapping.findForward(salida);
	}
}
