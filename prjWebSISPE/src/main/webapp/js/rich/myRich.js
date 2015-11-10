/**
 *	Inicializa los acordeones. 
 *	@param idAcordeon	- El identificador de la seccion donde se crea el acordeon.
 *	@param height		- La altura total del acordeon.	
 */
function acordeon(idAcordeon, height){
	new Rico.Accordion(idAcordeon,
                                   {
                                    	panelHeight	:height,
                                        expandedBg          : '#CCD0A4',
                                        hoverBg             : '#DBDEC0',
                                        collapsedBg         : '#E9EBD8',
                                        expandedTextColor   : '#990000',
                                        expandedFontWeight  : 'bold',
                                        hoverTextColor      : '#990000',
                                        collapsedTextColor  : '#003366',
                                        collapsedFontWeight : 'bold',
                                        hoverTextColor      : '#990000',
                                        borderColor         : '#D2D6AD'
                                    });
}

/**
 *	Inicializa los divisores horizontales
 *	@param idDivisor				- El identificador de la seccion que forma el divisor.
 *  @param idSeccionAOcultar		- El identificador de la seccion que se muestra o se oculta.
 *	@param idSeccionASobreponer		- El identificador de la seccion que se sobrepone.
 *	@param idImgOcultar				- El identificador de la seccion que contiene la imagen para ocultar.
 *	@param idImgMostrar				- El identificador de la seccion que contiene la imagen para mostrar.
 */
function divisor(idDivisor, idSeccionAOcultar, idSeccionASobreponer, idImgOcultar, idImgMostrar){
	new Rico.SpliterH(idDivisor, idSeccionAOcultar, idSeccionASobreponer, idImgOcultar, idImgMostrar,
						{
         					hoverBg             : '#b8bfd3',
         					collapsedBg         : '#c8cddd',
							borderColor         : '#b8bfd3'
						});
}