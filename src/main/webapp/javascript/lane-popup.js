(function(){
    YAHOO.util.Event.addListener(window, 'load', function(){
        var i, anchors, args, panel, createPanel, showPanel, popupWindow, showWindow;
        createPanel = function(){
            var container = document.createElement('div');
            container.setAttribute('id', 'popupContainer');
            document.body.appendChild(container);
            panel = new YAHOO.widget.Panel('popupContainer', {
                underlay: 'none',
                close: true,
                visible: false,
                draggable: false,
                constraintoviewport: true,
                modal: false
            });
        };
        showPanel = function(title, body, X, Y){
            var width = (title.length * 7 > 250) ? title.length * 7 : 250;
            panel.setHeader(title);
            panel.setBody(body);
            panel.cfg.setProperty('width', width + 'px');
            panel.cfg.setProperty('X', X);
            panel.cfg.setProperty('Y', Y);
            panel.render();
            panel.show();
        };
        showWindow = function(url, type, strWidth, strHeight){
            if (popupWindow !== undefined && !popupWindow.closed) {
                popupWindow.close();
            }
            if (type == 'fullscreen') {
                strWidth = screen.availWidth;
                strHeight = screen.availHeight;
            }
            var tools = '';
            if (type == 'standard') {
                tools = 'resizable,toolbar=yes,location=yes,scrollbars=yes,menubar=yes';
            }
            if (type == 'console' || type == 'fullscreen') {
                tools = 'resizable,toolbar=no,location=no,scrollbars=no';
            }
            if (strWidth && strHeight) {
                tools += ',width=' +strWidth + ',height=' + strHeight;
            }
            popupWindow = window.open(url, 'newWin', tools);
            popupWindow.focus();
        };
        anchors = document.getElementsByTagName('a');
        for (i = 0; i < anchors.length; i++) {
            if (anchors[i].rel) {
                args = anchors[i].rel.split(' ');
                if (args[0] == 'popup') {
                    if (args[1] == 'standard' || args[1] == 'console' || args[1] == 'fullscreen') {
                        anchors[i].clicked = function(e) {
                            var args = this.rel.split(' ');
                            YAHOO.util.Event.preventDefault(e);
                            showWindow(this.href,args[1],args[2],args[3]);
                        };
                    }
                    if (args[1] == 'local') {
                        if (!panel) {
                            createPanel();
                        }
                        anchors[i].clicked = function(e){
                            var id, elm, title, body, E = YAHOO.util.Event;
                            E.preventDefault(e);
                            id = this.rel.split(' ')[2];
                            elm = (document.getElementById(id)) ? document.getElementById(id) : 0;
                            title = (elm.getAttribute('title')) ? elm.getAttribute('title') : '';
                            body = (document.getElementById(id)) ? document.getElementById(id).innerHTML : '';
                            showPanel(title, body, E.getPageX(e), E.getPageY(e));
                        };
                    } else 
                        if (args[1] == 'faq') {
                            if (!panel) {
                                createPanel();
                            }
                            anchors[i].clicked = function(e){
                                var id = this.rel.split(' ')[2];
                                YAHOO.util.Event.preventDefault(e);
                                YAHOO.util.Connect.asyncRequest('GET', '/././plain/howto/index.html?mode=dl&id=_' + id, {
                                    success: function(o){
                                        var id = o.argument.id,
                                            X = o.argument.X,
                                            Y = o.argument.Y,
                                            f = o.responseXML.documentElement,
                                            title = f.getElementsByTagName('a')[0].firstChild.data,
                                            body = f.getElementsByTagName('dd')[0].firstChild.data + '&nbsp;<a href="/././howto/index.html?id=_' + id + '">More</a>';
                                        o.argument.showPanel(title, body, X, Y);

                                    },
                                    argument: {
                                        showPanel: showPanel,
                                        X: YAHOO.util.Event.getPageX(e),
                                        Y: YAHOO.util.Event.getPageY(e),
                                        id: id
                                    }
                                });
                            };
                        }
                }
            }
        }
    });
})();
