/*
 * Calendario de Bodega 0.1
 *
 * KrugerCorporation 2010, Jhonatan Villacis
 *
 */

//variable para identificar si ya fue establecio el Ajuste Automatico en onResize
this.ajusteActivo = false;
this.desplazarDia = false;
this.desplazarDiaS = false;
this.desplazarCalendario = false;
this.desplazarDiaTodos = false;
this.idSeccionCorregir = null;
this.codigoPedidoSeleccionado = null;
this.indiceDetalleCanasta = null;
this.codigoArticuloSeleccionado = null;
this.secuencialEstadoPedidoSeleccionado = null;
this.codigoPedidoDetalleSeleccionado = null;
this.mostrarDetCanDir = false;

//Funcion para calcular el tamanio de la ventana
function tamVentana() {
	var tamanio = [0, 0];
	if (typeof window.innerWidth != 'undefined'){
		//alert(1);
		tamanio = [window.innerWidth, window.innerHeight];
	}else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0){
		tamanio = [document.documentElement.clientWidth, document.documentElement.clientHeight];
		//alert(2);
	}else{
		tamanio = [document.getElementsByTagName('body')[0].clientWidth, document.getElementsByTagName('body')[0].clientHeight];
		//alert(3);
	}
	//alert('t:'+tamanio);
	//alert('s:'+screen.height);
	return tamanio;
}

//Funcion para ajustar el alto del contenido a la pantalla
function ajustarAltoContenido(onLoad) {
	//obtiene el tamanio de la ventana
	var tam = tamVentana();
	//obtiene la sumatoria de los altos de las secciones adicionales al cuerpo de la pagina
	var sumAlto = 0;
	sumAlto = sumAlto + (document.getElementById('encabezado_pagina') ? jQuery.iUtil.getSizeLite(document.getElementById('encabezado_pagina')).hb : 0);
	sumAlto = sumAlto + (document.getElementById('fecha_pagina') ? jQuery.iUtil.getSizeLite(document.getElementById('fecha_pagina')).hb : 0);
	sumAlto = sumAlto + (document.getElementById('mensajes') ? jQuery.iUtil.getSizeLite(document.getElementById('mensajes')).hb : 0);
	sumAlto = sumAlto + (document.getElementById('titleBar') ? jQuery.iUtil.getSizeLite(document.getElementById('titleBar')).hb : 0);
	sumAlto = sumAlto + (document.getElementById('bottom_pagina') ? jQuery.iUtil.getSizeLite(document.getElementById('bottom_pagina')).hb : 0);
	
	//cambia el tamanio del cuerpo de la pagina
	document.getElementById('div_pagina2').style.height = (tam[1] - sumAlto) + 'px';
	
	//alert('PagH: '+document.getElementById('div_pagina2').style.height);
	
	//si viene del onLoad de la pagina
	if(onLoad){
		//ajusta los contenedores
		ajusteInicial();
		//oculta el calendario siguiente
		var div_calendarioPosterior = document.getElementById('div_calendarioPosterior');
		if(div_calendarioPosterior && idSeccionCorregir == null){
			div_calendarioPosterior.style.display = 'none';
		}
		//desplaza el dia hoy
		setTimeout('desplazarSeccionDia()', 1000);
		//si aun no se ha establecido el ajuste en el onResize
		if(!ajusteActivo){
			//establece que se llame a la funcion ajustarAltoContenido cada vez que haya un cambio en el tamanio de la ventana
			window.onresize = function(){ajustarAltoContenido();};
			ajusteActivo = true;
		}
		//se crea el plugin para reestablecer la pantalla al estado original
		$j('#div_pagina2').inactividad();
	}
}

//Funcion para ajusta el tamanio de los contenedores que se sobrepasan en el alto por contener medidas en pixeles
function ajusteInicial(noAjustar){
	//valida si realiza el ajuste
	if(noAjustar == null || (noAjustar && idSeccionCorregir)){
		var seccionAjuste = 'div_pagina2';
		//verifica si ajusta toda la pagina o una seccion
		if(idSeccionCorregir){
			seccionAjuste = idSeccionCorregir;
		}
		//corrige alturas de las ventanas contenedores de nivel 1
		ajustarContenedores(seccionAjuste, 'div.cont1N', 17, false);
		//corrige alturas de las ventanas contenedores de nivel 2
		ajustarContenedores(seccionAjuste, 'div.cont2N', 17, false);
		//corrige la altura en la seccion de los dias del calendario actual
		var diasCalendario = document.getElementById('div_diasCalendario_sel');
		if(diasCalendario && idSeccionCorregir == null){
			diasCalendario.style.height = (((jQuery.iUtil.getSizeLite(diasCalendario).hb - 69) * 100 / jQuery.iUtil.getSizeLite(diasCalendario).hb) - 1) + '%';
		}
		//corrige la altura en la seccion de los dias del calendario siguiente
		diasCalendario = document.getElementById('div_diasCalendario_pos');
		if(diasCalendario && idSeccionCorregir == null){
			diasCalendario.style.height = (((jQuery.iUtil.getSizeLite(diasCalendario).hb - 69) * 100 / jQuery.iUtil.getSizeLite(diasCalendario).hb) - 1) + '%';
		}
		//elimina el id de la seccion a corregir si existe
		idSeccionCorregir = null;
	}
}

//Funcion para ajustar los contenedores
function ajustarContenedores(seccionAjuste, criterioBusqueda, alturaResta, animar){
	//obtiene los contenedores
	var contenedores = $j('#'+seccionAjuste).find(criterioBusqueda);
	//recorre los contenedores encontrados
	for(var i = 0; i < contenedores.length; i++){
		//obtiene la altura del padre
		var alturaPadre = jQuery.iUtil.getSizeLite(contenedores[i].parentNode).hb;
		//variable que almacena la altura corregida
		var alturaCorregida = (alturaPadre - alturaResta) * 100 / alturaPadre;
		//verifica si anima la correccion
		if(animar){
			$j(contenedores[i]).animate({height: alturaCorregida+'%'}, 500);
		}else{
			contenedores[i].style.height = alturaCorregida + '%';
		}
	}
}

//Funcion para desplazar las secciones del dia Hoy
function desplazarSeccionDia(){
	if(!desplazarDia){
		//esconde la seccion de desplazamiento
		$j('#div_desplazarDia').hide();
		//oculta la seccion del dia seleccionado
		$j('#div_diaPosHoy').hide('clip',{},500, function(){
			//agranda las seccion hoy
			$j("#div_diaHoy").animate({height: '96%'}, 500, function(){
				//corrige alturas de las ventanas contenedores de nivel 1
				ajustarContenedores('div_diaHoy', 'div.cont1N', 17, true);
				//corrige alturas de las ventanas contenedores de nivel 2
				ajustarContenedores('div_diaHoy', 'div.cont2N', 17, true);
				//mueve la seccion de desplazamiento
				document.getElementById('div_desplazarDia').style.top = '97.3%';
				//cambia de imagen
				document.getElementById('img_desplazarDiaH').setAttribute('src','images/flechaAmarillaArr.gif');
				//esconde la seccion de desplazamiento del dia seleccionado para que se pueda elegir
				$j('#img_desplazarDiaS').hide();
				//muestra la seccion de desplazamiento
				$j('#div_desplazarDia').show();
				desplazarDia = true;
			});
		});
	}else{
		//esconder la seccion de desplazamiento
		$j('#div_desplazarDia').hide();
		//restablece el tamanio original del dia hoy
		$j("#div_diaHoy").animate({height: '48%'}, 500, function(){
			//corrige alturas de las ventanas contenedores de nivel 1
			ajustarContenedores('div_diaHoy', 'div.cont1N', 17, true);
			//corrige alturas de las ventanas contenedores de nivel 2
			ajustarContenedores('div_diaHoy', 'div.cont2N', 17, true);
			//muestra el dia posterior
			$j('#div_diaPosHoy').show('clip',{},500, function(){
				//muestra la seccion de desplazamiento
				document.getElementById('div_desplazarDia').style.top = '49.3%';
				document.getElementById('img_desplazarDiaH').setAttribute('src','images/flechaAmarillaAba.gif');
				$j('#img_desplazarDiaS').show();
				$j('#div_desplazarDia').show();
				desplazarDia = false;
			});
		});
	}
}

//Funcion para desplazar las secciones del dia Seleccionado
function desplazarSeccionDiaSel(){
	if(!desplazarDiaS){
		//esconde la seccion de desplazamiento
		$j('#div_desplazarDia').hide();
		//oculta la seccion del dia hoy
		$j('#div_diaHoy').hide('clip',{},500, function(){
			//agranda las seccion del dia seleccionado
			$j("#div_diaPosHoy").animate({height: '96%', top: '3%'}, 500, function(){
				//corrige alturas de las ventanas contenedores de nivel 1
				ajustarContenedores('div_diaPosHoy', 'div.cont1N', 17, true);
				//corrige alturas de las ventanas contenedores de nivel 2
				ajustarContenedores('div_diaPosHoy', 'div.cont2N', 17, true);
				document.getElementById('div_desplazarDia').style.top = '1%';
				document.getElementById('img_desplazarDiaS').setAttribute('src','images/flechaAzulAba.gif');
				$j('#img_desplazarDiaH').hide();
				$j('#div_desplazarDia').show();
				desplazarDiaS = true;
			});
		});
	}else{
		//esconder la seccion de desplazamiento
		$j('#div_desplazarDia').hide();
		//restablece el tamanio original del dia seleccionado
		$j("#div_diaPosHoy").animate({height: '48%', top: '51%'}, 500, function(){
			//corrige alturas de las ventanas contenedores de nivel 1
			ajustarContenedores('div_diaPosHoy', 'div.cont1N', 17, true);
			//corrige alturas de las ventanas contenedores de nivel 2
			ajustarContenedores('div_diaPosHoy', 'div.cont2N', 17, true);
			//muestra el dia hoy
			$j('#div_diaHoy').show('clip',{},500, function(){
				//muestra la seccion de desplazamiento
				document.getElementById('div_desplazarDia').style.top = '49.3%';
				document.getElementById('img_desplazarDiaS').setAttribute('src','images/flechaAzulArr.gif');
				$j('#img_desplazarDiaH').show();
				$j('#div_desplazarDia').show();
				desplazarDiaS = false;
			});
		});
	}
}

//Funcion para desplazar la seccion del calendario
function desplazarSeccionCalendario(){
	if(!desplazarCalendario){
		//oculta la seccion de desplazamiento
		$j('#div_desplazarCalendario').hide();
		//esconde la seccion del calendario
		$j('#div_calendario').hide('clip',{},500, function(){
			//agranda las seccion hoyPos
			if(!desplazarDia){
				$j("#div_diaPosHoy").animate({width: '98%',left: '1.5%'}, 500);
			}else{
				document.getElementById('div_diaPosHoy').style.width = '98%';
				document.getElementById('div_diaPosHoy').style.left = '1.5%';
			}
			//agranda la seccion hoy
			if(!desplazarDiaS){
				$j("#div_diaHoy").animate({width: '98%',left: '1.5%'}, 500);
			}else{
				document.getElementById('div_diaHoy').style.width = '98%';
				document.getElementById('div_diaHoy').style.left = '1.5%';
			}
			document.getElementById('div_desplazarCalendario').style.width = '98%';
			document.getElementById('div_desplazarCalendario').style.left = '0.5%';
			document.getElementById('img_desplazarCalendario').setAttribute('src','images/flechaVerdeDer.gif');
			$j('#img_desplazarDiaTodos').hide();
			$j('#div_desplazarCalendario').show();
			desplazarCalendario = true;
		});
	}else{
		//esconder la seccion de desplazamiento
		$j('#div_desplazarCalendario').hide();
		//restablece el tamanio original del dia seleccionado
		if(!desplazarDia){
			$j("#div_diaPosHoy").animate({width: '49%', left: '50.5%'}, 500);
		}else{
			document.getElementById('div_diaPosHoy').style.width = '49%';
			document.getElementById('div_diaPosHoy').style.left = '50.5%';
		}
		//restablece el tamanio original del dia hoy
		if(!desplazarDiaS){
			$j("#div_diaHoy").animate({width: '49%', left: '50.5%'}, 500);
		}else{
			document.getElementById('div_diaHoy').style.width = '49%';
			document.getElementById('div_diaHoy').style.left = '50.5%';
		}
		document.getElementById('div_desplazarCalendario').style.width = '49%';
		//muestra el calendario
		$j('#div_calendario').show('clip',{},500, function(){
			//muestra la seccion de desplazamiento
			document.getElementById('div_desplazarCalendario').style.left = '49.7%';
			document.getElementById('img_desplazarCalendario').setAttribute('src','images/flechaVerdeIzq.gif');
			$j('#img_desplazarDiaTodos').show();
			$j('#div_desplazarCalendario').show();
			desplazarCalendario = false;
		});
	}
}

function desplazarSeccionDias(){
	if(!desplazarDiaTodos){
		//oculta la seccion de desplazamiento
		$j('#div_desplazarCalendario').hide();
		//esconde las secciones de los dias
		$j('#div_desplazarDia').hide();
		//verifica si esta oculto el hoy
		if(desplazarDia && !desplazarDiaS){
			$j('#div_diaHoy').hide('slide',{},500);
		}else if(desplazarDiaS && !desplazarDia){
			$j('#div_diaPosHoy').hide('slide',{},500);
		}else if (!desplazarDia && !desplazarDiaS){
			$j('#div_diaHoy').hide('slide',{},500);
			$j('#div_diaPosHoy').hide('slide',{},500);
		}
		//para mostrar el siguiente mes
		setTimeout(function(){
			$j('#div_calendarioPosterior').show('slide',{},500,function(){
				$j('#img_desplazarCalendario').hide();
				document.getElementById('img_desplazarDiaTodos').setAttribute('src','images/flechaRojaIzq.gif');
				document.getElementById('img_desplazarDiaTodos').setAttribute('title','Ocultar Mes Siguiente');
				$j('#div_desplazarCalendario').show();
			});
		},300);
		desplazarDiaTodos = true;
	}else{
		//oculta la seccion de desplazamiento
		$j('#div_desplazarCalendario').hide();
		//oculta el mes siguiente
		$j('#div_calendarioPosterior').hide('slide',{},500,function(){
			//verifica si esta oculto el hoy
			if(desplazarDia && !desplazarDiaS){
				$j('#div_diaHoy').show('slide',{},500);
			}else if(desplazarDiaS && !desplazarDia){
				$j('#div_diaPosHoy').show('slide',{},500);
			}else if (!desplazarDia && !desplazarDiaS){
				$j('#div_diaHoy').show('slide',{},500);
				$j('#div_diaPosHoy').show('slide',{},500);
			}
			setTimeout(function(){
				document.getElementById('img_desplazarDiaTodos').setAttribute('src','images/flechaRojaDer.gif');
				document.getElementById('img_desplazarDiaTodos').setAttribute('title','Mostrar Mes Siguiente');
				$j('#img_desplazarCalendario').show();
				$j('#div_desplazarCalendario').show();
				$j('#div_desplazarDia').show();
			},500);
		});
		desplazarDiaTodos = false;
	}
}

//Funcion para maximizar los contenedores
function maximizarContenedor(contenedorMax, contenedorMin1, contenedorMin2, topMax, heightMax, zIndexMax, topMin, heightMin, zIndexMin){
	var esMax = document.getElementById(contenedorMax).getAttribute('esMax');
	if(esMax == 'false'){
		//sobrepone la seccion a maximizar
		$j('#'+contenedorMax).css('zIndex', zIndexMax);
		//maximiza el contenedor
		$j('#'+contenedorMax).animate({top: topMax, height: heightMax},500, function(){
			document.getElementById(contenedorMax).setAttribute('esMax','true');
			//corrige alturas de las ventanas contenedores de nivel 1
			ajustarContenedores(contenedorMax, 'div.cont1N', 17, true);
			//corrige alturas de las ventanas contenedores de nivel 2
			ajustarContenedores(contenedorMax, 'div.cont2N', 17, true);
		});
	}else{
		//minimiza el contenedor
		$j('#'+contenedorMax).animate({top: topMin, height: heightMin},500, function(){
			document.getElementById(contenedorMax).setAttribute('esMax','false');
			//corrige alturas de las ventanas contenedores de nivel 1
			ajustarContenedores(contenedorMax, 'div.cont1N', 17, true);
			//corrige alturas de las ventanas contenedores de nivel 2
			ajustarContenedores(contenedorMax, 'div.cont2N', 17, true);
			//sobrepone la seccion a maximizar
			$j('#'+contenedorMax).css('zIndex', zIndexMin);
		});
	}
}

//Funcion para cambiar los datos del dia seleccionado
function onClickSeleccionarDia(indiceDia, esDiaActual, esSeleccionado){
	if (request != null && request.readyState != 0 && request.readyState != 4){
		//hay una conexion activa
		document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Se esta ejecutando una actualizacion!!';
		$j('#'+jQuery.iInactividad.seccionMensaje).hide('bounce', {}, 3000, function(){
			document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
		});
		return false;
	}else{
		if(!esDiaActual && !esSeleccionado){
			if(desplazarDiaTodos){
				desplazarSeccionDias();
			}
			setTimeout(function(){
				if(desplazarDia){
					//esconder la seccion de desplazamiento
					$j('#div_desplazarDia').hide();
					//restablece el tamanio original del dia hoy
					$j("#div_diaHoy").animate({height: '48%'}, 500, function(){
						//corrige alturas de las ventanas contenedores de nivel 1
						ajustarContenedores('div_diaHoy', 'div.cont1N', 17, true);
						//corrige alturas de las ventanas contenedores de nivel 2
						ajustarContenedores('div_diaHoy', 'div.cont2N', 17, true);
						//muestra el dia posterior
						$j('#div_diaPosHoy').show('clip',{},500, function(){
							//muestra la seccion de desplazamiento
							document.getElementById('div_desplazarDia').style.top = '49.3%';
							document.getElementById('img_desplazarDiaH').setAttribute('src','images/flechaAmarillaAba.gif');
							$j('#img_desplazarDiaS').show();
							$j('#div_desplazarDia').show();
							idSeccionCorregir = 'div_diaPosHoy';
							requestAjax('calendarioBodega.do', ['div_diasCalendario_sel','div_diasCalendario_pos','div_diaPosHoy'], {parameters: 'diaSeleccionado='+indiceDia, evalScripts: true});
							desplazarDia = false;
						});
					});
				}else{
					idSeccionCorregir = 'div_diaPosHoy';
					requestAjax('calendarioBodega.do', ['div_diasCalendario_sel','div_diasCalendario_pos','div_diaPosHoy'], {parameters: 'diaSeleccionado='+indiceDia, evalScripts: true});
				}
			},1000);
		}else{
			return false;
		}
	}
}

//Funcion para abrir popUp de DetallePedido
function mostrarPopUpDetalle(codigoPedido){
	//si el va a abrir el mismo pedido
	if(codigoPedidoSeleccionado != null && codigoPedidoSeleccionado == codigoPedido){
		$j('#div_detallePedido').show();
		$j('#div_detalleCanasta').hide();
		//solo muestra el popUp
		$j('#dialog').dialog('open');
	}else if(codigoPedidoSeleccionado == null || (codigoPedidoSeleccionado != null && codigoPedidoSeleccionado != codigoPedido)){
		codigoPedidoSeleccionado = codigoPedido;
		indiceDetalleCanasta = null;
		//si va a buscar un pedido diferente
		requestAjax('calendarioBodega.do', ['dialog','openDialog'], {parameters: 'codigoPedido='+codigoPedido, evalScripts: true});
	}
}

//Funcion para abrir el detalle de la canasta
function mostrarDetalleCanasta(indiceDetalle){
	//si el va a abrir el mismo pedido
	if(indiceDetalleCanasta != null && indiceDetalleCanasta == indiceDetalle){
		/*$j('#div_detallePedido').hide('drop',{}, 500, function(){
			$j('#div_detalleCanasta').show('drop',{}, 500);
		});*/
		$j('#div_detallePedido').hide();
		$j('#div_detalleCanasta').show();
	}else if(indiceDetalleCanasta == null || (indiceDetalleCanasta != null && indiceDetalleCanasta != indiceDetalle)){
		indiceDetalleCanasta = indiceDetalle;
		//si va a buscar un pedido diferente
		requestAjax('calendarioBodega.do', ['div_detalleCanasta'], {parameters: 'indiceDetallePedido='+indiceDetalle, evalScripts: true});
	}
	codigoArticuloSeleccionado = null;
}

//Funcion para abrir pantalla para configurar las entregas de productos perecibles
function mostrarConfigEntregas(codigoArticulo,codigoPedido){
		//si el va a abrir el mismo pedido
	if(codigoArticuloSeleccionado != null && codigoArticuloSeleccionado == codigoArticulo){
			$j('#div_configEntregas').show();
			//solo muestra el popUp
			$j('#dialogConfigEntr').dialog('open');
	} else if(codigoArticuloSeleccionado == null || (codigoArticuloSeleccionado != null && codigoArticuloSeleccionado != codigoArticulo)){
			codigoArticuloSeleccionado = codigoArticulo;
			indiceDetalleCanasta = null;
			//si va a buscar un pedido diferente
			requestAjax('calendarioBodega.do', ['dialogConfigEntr','openDialogConfigEntr'], {parameters: 'codigoArticulo='+codigoArticulo+'&codigoPedidoEntr='+codigoPedido, evalScripts: true});
			$j('#div_configEntregas').show();
			//solo muestra el popUp
			$j('#dialogConfigEntr').dialog('open');
		}
}

//Abrir pantalla de confirmacion para guardar las entregas
function guardarConfigEntregas(){
			//si el va a abrir el mismo pedido
			$j('#div_configEntregas').hide();
			//solo muestra el popUp
			$j('#dialogConfigEntr').dialog('close');
			requestAjax('calendarioBodega.do', ['dialogGuardarEntr','openDialogGuardarEntr'], {parameters: 'botonMostrarConfirmacion=OK&actualizar=OK',evalScripts:true});
			$j('#div_configEntregas').show();
			//solo muestra el popUp
			$j('#dialogGuardarEntr').dialog('open');
}
function ocultarConfigEntregas(){
	//si el va a abrir el mismo pedido
	$j('#dialogGuardarEntr').dialog('close');
	$j('#div_guardarEntregas').hide();
	//requestAjax('calendarioBodega.do', ['dialogConfigEntr'], {parameters: 'ocultarConfirmacion=OK&actualizar=OK',evalScripts:true});
	//si el va a abrir el mismo pedido
	$j('#div_configEntregas').show();
	//solo muestra el popUp
	$j('#dialogConfigEntr').dialog('open');
	
}
//Funcion para ver el detalle de la canasta desde las pantallas del calendario y no del estadoPedido
function mostrarDetalleCanastaDirecto(codigoArticulo, secuencialEstadoPedido, codigoPedido, codigoArticuloOriginal){
	//si va a abrir el mismo pedido
	if(codigoArticulo != null && secuencialEstadoPedido != null && codigoPedido != null){
		//alert('1');
		//alert(codigoArticulo + ';'+secuencialEstadoPedido+';'+codigoPedido);
		if(codigoArticuloSeleccionado != null && codigoArticuloSeleccionado == codigoArticulo 
		&& secuencialEstadoPedidoSeleccionado != null && secuencialEstadoPedidoSeleccionado == secuencialEstadoPedido 
		&& codigoPedidoDetalleSeleccionado != null && codigoPedidoDetalleSeleccionado == codigoPedido){
			//alert('1.1');
			$j('#div_detallePedido').hide();
			$j('#div_detalleCanasta').show();
			//solo muestra el popUp
			$j('#dialog').dialog('open');
		}else if(codigoArticuloSeleccionado == null || (codigoArticuloSeleccionado != null && codigoArticuloSeleccionado != codigoArticulo)){
			//alert('1.2');
			codigoArticuloSeleccionado = codigoArticulo;
			secuencialEstadoPedidoSeleccionado = secuencialEstadoPedido;
			codigoPedidoDetalleSeleccionado = codigoPedido;
			indiceDetalleCanasta = null;
			codigoPedidoSeleccionado = null;
			//si va a buscar un pedido diferente
			requestAjax('calendarioBodega.do', ['dialog','openDialog'], {parameters: 'codigoArticulo='+codigoArticulo+'&secuencialEstadoPedido='+secuencialEstadoPedido+'&codigoArtOri='+codigoArticuloOriginal, evalScripts: true});
		}
	}else if(codigoArticulo != null && secuencialEstadoPedido == null && codigoPedido == null){
		//alert('2');
		if(codigoArticuloSeleccionado != null && codigoArticuloSeleccionado == codigoArticulo
		&& secuencialEstadoPedidoSeleccionado == null && codigoPedidoDetalleSeleccionado == null){
			//alert('2.1');
			$j('#div_detallePedido').hide();
			$j('#div_detalleCanasta').show();
			//solo muestra el popUp
			$j('#dialog').dialog('open');
		}else{
			//alert('2.2');
			codigoArticuloSeleccionado = codigoArticulo;
			mostrarDetCanDir = true;
			//si va a buscar un pedido diferente
			requestAjax('calendarioBodega.do', ['dialog','openDialog'], {parameters: 'codigoArticuloLP='+codigoArticulo, evalScripts: true});
		}
		secuencialEstadoPedidoSeleccionado = null;
		codigoPedidoDetalleSeleccionado = null;
		indiceDetalleCanasta = null;
		codigoPedidoSeleccionado = null;
	}else{
		alert('3');
	}
}


//plugin para detectar inactividad sobre la aplicacion
jQuery.iInactividad = {
	//el id de la seccion que servira para mostrar cualquier mensaje
	seccionMensaje : 'div_mensajes_inactividad',
	//tiempo de espera en inactividad (en milisegundos)[15 min]
	tiempoEsperaEstadoOriginal: 900000,
	//tiempoEsperaEstadoOriginal: 30000,
	//tiempo de espera para actualizar la pantalla (en milisegundos)[5 min]
	tiempoEsperaActualizar: 300000,
	//objeto con el timer para volver al estadoOriginal
	objetoTimeoutEstadoOriginal: null,
	//objeto con el timer para actualizar la pantalla
	objetoTimeoutActualizar: null,
	//contador auxiliar para pruebas
	contadorAux: 0,
	//funcion iniciar el timer para volver al estado original
	iniciarTimerEstadoOriginal: function(){
		//inicial el timer
		jQuery.iInactividad.objetoTimeoutEstadoOriginal = setTimeout('jQuery.iInactividad.volverEstadoOriginal()', jQuery.iInactividad.tiempoEsperaEstadoOriginal);
	},
	//funcion iniciar el timer para actualizar la pantalla
	iniciarTimerActualizar: function(){
		//inicial el timer
		jQuery.iInactividad.objetoTimeoutActualizar = setTimeout('jQuery.iInactividad.actualizar()', jQuery.iInactividad.tiempoEsperaActualizar);
	},
	//funcion para reiniciar el timer del estado original
	reiniciarTimerEstadoOriginal: function(){
		//detiene el timer
		clearTimeout(jQuery.iInactividad.objetoTimeoutEstadoOriginal);
		//inicia nuevamente el timer
  		jQuery.iInactividad.iniciarTimerEstadoOriginal();
  		
  		//prueba de que se reinicia
  		//jQuery.iInactividad.contadorAux++;
  		//document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = jQuery.iInactividad.contadorAux;
	},
	//funcion para reiniciar el timer para actualizar
	reiniciarTimerActualizar: function(){
		//detiene el timer
		clearTimeout(jQuery.iInactividad.objetoTimeoutActualizar);
		//inicia nuevamente el timer
  		jQuery.iInactividad.iniciarTimerActualizar();
  		
  		//prueba de que se reinicia
  		jQuery.iInactividad.contadorAux++;
  		//document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'reinicia contador actualizar';
	},
	//funcion para volver al estado original de la pantalla
	volverEstadoOriginal: function(){
		//mensaje para volver al estado original
		if(desplazarCalendario || desplazarDiaS || desplazarDia){
			document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'La pantalla volvera a su estado original';
			$j('#'+jQuery.iInactividad.seccionMensaje).hide('bounce', {}, 3000, function(){
				//se elimina el mensaje
				document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
				
				//se ejecuta los metodos para volver al estado original
				if(desplazarDiaTodos){
					desplazarSeccionDias();
				}
				setTimeout(function(){
					//verifica si esta oculto el calendario
					if(desplazarCalendario){
						desplazarSeccionCalendario();
					}
					//verifica si esta oculto el dia seleccionado 
					if(desplazarDiaS){
						desplazarSeccionDiaSel();
					}
					//verifica si esta oculto el hoy
					if(desplazarDia){
						desplazarSeccionDia();
					}
					
					//desplaza el dia hoy
					setTimeout('desplazarSeccionDia()', 3000);
				},600);
			});
		}
		//se reinicia el timer
		jQuery.iInactividad.reiniciarTimerEstadoOriginal();
	},
	//funcion para actualizar toda la informacion de la pantalla
	actualizar: function(){
		//actualizarPantalla();
		//Chequeando conexiones existentes
		if (request != null && request.readyState != 0 && request.readyState != 4){
			//hay una conexion activa
			//reinicia el timer de actualizar
			jQuery.iInactividad.reiniciarTimerActualizar();
			document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Se esta ejecutando actualmente otro proceso';
			$j('#'+jQuery.iInactividad.seccionMensaje).hide('bounce', {}, 3000, function(){
				document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
			});
		}else{
			if(window.XMLHttpRequest){ // No Internet Explorer
	        	request = new XMLHttpRequest();
	    	}else if (window.ActiveXObject){ // Internet Explorer
	        	request = new ActiveXObject("Msxml2.XMLHTTP");
	        	if (!request){
	            	request=new ActiveXObject("Microsoft.XMLHTTP");
	        	}
	     	}
	    	if(request){ // el objeto de peticion se creo correctamente
	            //se procesan los cambios de estado
	            request.onreadystatechange = function(){
	            	switch(request.readyState){
						case 1: 
							break;
						case 2: 
							break;
						case 3: 
							break;
						case 4:	//completa	
			    			if (request.status == 200){ //respuesta OK
								//llamada a la funcion que actualiza la pagina
			    				updateHTML(['div_diasCalendario_sel','div_diasCalendario_pos','div_desDom_diaActual_datos','div_desDom_diaSeleccionado_datos','div_desLoc_diaActual_datos','div_desLoc_diaSeleccionado_datos'
			    				,'tabs_pro_diaActual_canasta1','tabs_pro_diaActual_canasta2','tabs_pro_diaActual_canasta3'
			    				,'tabs_pro_diaSeleccionado_canasta1','tabs_pro_diaSeleccionado_canasta2','tabs_pro_diaSeleccionado_canasta3'
			    				,'tabs_pro_diaActual_despensa1','tabs_pro_diaActual_despensa2','tabs_pro_diaActual_despensa3'
			    				,'tabs_pro_diaSeleccionado_despensa1','tabs_pro_diaSeleccionado_despensa2','tabs_pro_diaSeleccionado_despensa3'], request.responseText);
								
			    				document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Actualizacion: OK!!!!';
			    				$j('#'+jQuery.iInactividad.seccionMensaje).hide('slide', {}, 1000, function(){
			    					document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
			    					//reinicia el timer de actualizar
	    							jQuery.iInactividad.reiniciarTimerActualizar();
			    				});
				  			}else{
				  				//reinicia el timer de actualizar
	   							jQuery.iInactividad.reiniciarTimerActualizar();
				  			}
				    }
	            };
	            initOptions();
	            setOptions({evalScripts: true});
		  		request.open('post', 'calendarioBodega.do', true); // se inicializa el objeto de la peticion
		  		request.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
		        request.send('actualizar=true'); //se envia la peticion
		        
		        document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Actualizando....';
				$j('#'+jQuery.iInactividad.seccionMensaje).effect('pulsate', {}, 3000);
	    	}else{
	    		//reinicia el timer de actualizar
	    		jQuery.iInactividad.reiniciarTimerActualizar();
	    		//alert('No se pudo realizar la peticion');
	    	}
	    }
	},
	//destruye el plugin
	destroy: function(){
		//elimina la ejecucion de las funciones en los eventos especificados
		jQuery(this)
			.unbind('mousedown', jQuery.iInactividad.reiniciarTimerEstadoOriginal)
			.unbind('mousemove', jQuery.iInactividad.reiniciarTimerEstadoOriginal)
			.unbind('keydown', jQuery.iInactividad.reiniciarTimerEstadoOriginal);	
	},
	//construye el plugin
	build: function() {
		//iniciar el timer de estado original
		jQuery.iInactividad.iniciarTimerEstadoOriginal();
		//iniciar el timer de actualizar
		jQuery.iInactividad.iniciarTimerActualizar();
		//inicia los eventos en los que el contador se reiniciara
		jQuery(this)
			.bind('mousedown', jQuery.iInactividad.reiniciarTimerEstadoOriginal)
			.bind('mousemove', jQuery.iInactividad.reiniciarTimerEstadoOriginal)
			.bind('keydown', jQuery.iInactividad.reiniciarTimerEstadoOriginal);	
	}
};

jQuery.fn.extend({
	inactividad : jQuery.iInactividad.build
});

//funcion para actualizar la pantalla
function actualizarPantalla(){
	jQuery.iInactividad.actualizar();
}

//funcion para actualizar el calendario de bodega despues de guardar la entrega
function actualizarCalendarioEntregas(){
		mostrarModal();
		$j('#'+jQuery.iInactividad.seccionMensaje).hide('bounce', {}, 500, function(){
		if (request != null && request.readyState != 0 && request.readyState != 4){
			
			//hay una conexion activa
			//reinicia el timer de actualizar
			jQuery.iInactividad.reiniciarTimerActualizar();
			document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Se esta ejecutando actualmente otro proceso';
			$j('#'+jQuery.iInactividad.seccionMensaje).hide('bounce', {}, 3000, function(){
				document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
			});
		}else{
		
			if(window.XMLHttpRequest){ // No Internet Explorer
	        	request = new XMLHttpRequest();
	    	}else if (window.ActiveXObject){ // Internet Explorer
	        	request = new ActiveXObject("Msxml2.XMLHTTP");
	        	if (!request){
	            	request=new ActiveXObject("Microsoft.XMLHTTP");
	        	}
	     	}
	    	if(request){ // el objeto de peticion se creo correctamente
	            //se procesan los cambios de estado
	            request.onreadystatechange = function(){
	            	switch(request.readyState){
						case 1: 
							break;
						case 2: 
							break;
						case 3: 
							break;
						case 4:	//completa	
			    			if (request.status == 200){ //respuesta OK
								//llamada a la funcion que actualiza la pagina
			    				updateHTML(['div_diasCalendario_sel','div_diasCalendario_pos','div_desDom_diaActual_datos','div_desDom_diaSeleccionado_datos','div_desLoc_diaActual_datos','div_desLoc_diaSeleccionado_datos'
			    				,'tabs_pro_diaActual_canasta1','tabs_pro_diaActual_canasta2','tabs_pro_diaActual_canasta3'
			    				,'tabs_pro_diaSeleccionado_canasta1','tabs_pro_diaSeleccionado_canasta2','tabs_pro_diaSeleccionado_canasta3'
			    				,'tabs_pro_diaActual_despensa1','tabs_pro_diaActual_despensa2','tabs_pro_diaActual_despensa3'
			    				,'tabs_pro_diaSeleccionado_despensa1','tabs_pro_diaSeleccionado_despensa2','tabs_pro_diaSeleccionado_despensa3'], request.responseText);
								ocultarModal();
								document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Actualizacion: OK!!!!';
			    				$j('#'+jQuery.iInactividad.seccionMensaje).hide('slide', {}, 1000, function(){
			    					document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = '';
			    					//reinicia el timer de actualizar
	    							jQuery.iInactividad.reiniciarTimerActualizar();
			    				});
				  			}else{
				  				//reinicia el timer de actualizar
	   							jQuery.iInactividad.reiniciarTimerActualizar();
				  			}
				    }
	            };
	            initOptions();
	            setOptions({evalScripts: true});
		  		request.open('post', 'calendarioBodega.do', true); // se inicializa el objeto de la peticion
		  		request.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8');
		        request.send('actualizar=true'); //se envia la peticion
		        
		        document.getElementById(jQuery.iInactividad.seccionMensaje).innerHTML = 'Actualizando....';
				$j('#'+jQuery.iInactividad.seccionMensaje).effect('pulsate', {}, 3000);
	    	}else{
	    		//reinicia el timer de actualizar
	    		jQuery.iInactividad.reiniciarTimerActualizar();
	    		//alert('No se pudo realizar la peticion');
	    	}
	    }
	});
}