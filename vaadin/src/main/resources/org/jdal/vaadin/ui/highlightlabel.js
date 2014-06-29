org_jdal_vaadin_ui_HighlightLabel = function() {
	var e = this.getElement();
		
	this.onStateChange = function() {
		e.innerHTML = this.getState().xhtml;
		var codes = e.getElementsByTagName("code");
		var len = codes.length;

		for (var i = 0; i < len; i++) {
			hljs.highlightBlock(codes[i]);
		}
	}
}