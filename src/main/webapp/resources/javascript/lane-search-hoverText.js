// hover text for search result abstracts and descriptions
(function() {
    var Y = LANE.Y, registerHoverTriggers = function() {
        var hoverTargets = Y.all('.hvrTarg'), parentUl, label, i;
        for (i = 0; i < hoverTargets.size(); i++) {
            parentUl = hoverTargets.item(i).ancestor('ul');
            parentUl.addClass('hvrTrig');
            parentUl.on("mouseenter", function(e) {
                this.addClass('active');
                var elm = this, evt = e;
                function f(){
                    if(elm.hasClass("active")){
                        elm.setStyle("backgroundColor", "#F2F3ED");
                        elm.setStyle("padding", "4px");
                        elm.setStyle("border", "1px solid");
                        evt.currentTarget.one('.hvrTarg').setStyle("display", "block");
                        if (elm.one(".showAbstract")) {
                            elm.one(".showAbstract").setStyle("display", "none");
                        }
                        if(!Y.UA.ie && !Y.DOM.inViewportRegion(Y.Node.getDOMNode(elm),true)){
                            // scroll abstract/description into viewport for all but IE
                            // scrollIntoView in IE makes for bouncy experience b/c IE scrolls to top of window
                            // IE even bouncy with:
                            //scrollTo(0,Y.DOM.docScrollY() + this.get('offsetHeight'));
                            elm.scrollIntoView();
                        }
                    }
                }
                // delay for all but iPhone/p*d
                setTimeout(f,navigator.platform.match(/(iPhone|iP.d)/) ? 0 : 1000);
            });
            parentUl.on("mouseleave", function(e) {
                this.removeClass('active');
                this.setStyle("backgroundColor", "#fff");
                this.setStyle("padding", "5px");
                this.setStyle("border", "none");
                e.currentTarget.one('.hvrTarg').setStyle("display", "none");
                if (this.one(".showAbstract")) {
                    this.one(".showAbstract").setStyle("display", "block");
                }
            });
            if (navigator.platform.match(/(iPhone|iP.d)/)
                    && !parentUl.one(".showAbstract")) {
                label = (parentUl.one(".pmid")) ? 'Abstract' : 'Description';
                hoverTargets.item(i).insert("<li class='showAbstract'>[<a href='#'>Show " + label + "</a>]</li>", "before");
            }
        }
    };
    Y.Global.on('lane:change', function() {
        registerHoverTriggers();
    });
    registerHoverTriggers();

})();
