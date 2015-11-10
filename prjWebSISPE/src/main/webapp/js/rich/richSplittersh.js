// JavaScript Document
Rico.SpliterH = Class.create();

Rico.SpliterH.prototype = {
	
	initialize: function(divisor, td1, td2, img1, img2, options) {
	  	this.divisor        = $(divisor);
	 	this.td1            = $(td1);	
		this.td2            = $(td2);
		this.img1 			= $(img1);
		this.img2 			= $(img2);
		this.carga 			= false;
		this.anchoPanelIzq = "";
	    this.anchoPanelDer = "";
	    this.setOptions(options);
	 	this._attachBehaviors();
	    	if(!divisor || !td1 || !td2) return
	},
      
	setOptions: function(options) {
	   this.options = {
	      expandedBg          : '#63699c',
	      hoverBg             : '#63699c',
	      collapsedBg         : '#6b79a5',
	      expandedTextColor   : '#ffffff',
	      expandedFontWeight  : 'bold',
	      hoverTextColor      : '#ffffff',
	      collapsedTextColor  : '#ced7ef',
	      collapsedFontWeight : 'normal',
	      hoverTextColor      : '#ffffff',
	      borderColor         : '#1f669b',
	      panelHeight         : 200,
	      onHideTab           : null,
	      onShowTab           : null,
	      onLoadShowTab       : 0
	   }
	   Object.extend(this.options, options || {});
	},
	 
	_attachBehaviors: function() {
	 
		this.divisor.style.border = "1px solid " + this.options.borderColor;
	   	this.divisor.style.borderTopWidth    = "0px";
	  	this.divisor.style.borderBottomWidth = "0px";
	   	this.divisor.style.margin            = "0px";
	 	this.divisor.style.backgroundColor = this.options.collapsedBg;
	
	   	this.divisor.onclick     = this.titleBarClicked.bindAsEventListener(this);
	   	this.divisor.onmouseover = this.hover.bindAsEventListener(this);
	   	this.divisor.onmouseout  = this.unhover.bindAsEventListener(this);
		this.anchoPanelIzq = this.td1.style.width;
	  	this.anchoPanelDer = this.td2.style.width;
	 },

	 titleBarClicked: function(e) {
		this.actions();
	 },
	
	 hover: function(e) {
		this.divisor.style.backgroundColor = this.options.hoverBg;
		this.divisor.style.color           = this.options.hoverTextColor;
	 },
	
	 unhover: function(e) {
	    if(this.expanded){
	       this.divisor.style.backgroundColor = this.options.expandedBg;
	       this.divisor.style.color           = this.options.expandedTextColor;
	    }
	    else{
	       this.divisor.style.backgroundColor = this.options.collapsedBg;
	       this.divisor.style.color           = this.options.collapsedTextColor;
	    }
	 },
	
	 _getDirectChildrenByTag: function(e, tagName) {
	    var kids = new Array();
	    var allKids = e.childNodes;
	    for( var i = 0 ; i < allKids.length ; i++ )
	       if ( allKids[i] && allKids[i].tagName && allKids[i].tagName == tagName )
	          kids.push(allKids[i]);
	    return kids;
	 },
	 
	 actions: function(){
	  	if(!this.carga){
			this.td1.style.display = "none";
			this.td1.style.visibility = "hidden";
			this.img1.style.display = "none";
			this.img1.style.visibility = "hidden";
			this.img2.style.display = "block";
			this.img2.style.visibility = "visible";
			//nuevas dimensiones para las secciones
			this.td1.style.width = "0%";
			this.td2.style.width = "100%"
			this.carga = true;
	  	}else{
			this.td1.style.display = "block";
			this.td1.style.visibility = "visible";
			this.img2.style.display = "none";
			this.img2.style.visibility = "hidden";
			this.img1.style.display = "block";
			this.img1.style.visibility = "visible";
			//se restablecen las dimensiones de las secciones
			this.td1.style.width = this.anchoPanelIzq;
			this.td2.style.width = this.anchoPanelDer;
			this.carga = false;
	  	}
	 }
};