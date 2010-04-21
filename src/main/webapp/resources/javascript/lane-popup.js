YUI().use('yui2-container','yui2-event','yui2-connection',function(Y) {
    Y.YUI2.util.Event.onDOMReady(function() {
        var panel, createPanel, showPanel, popupWindow, showWindow, createEventHandlers,
            YUE = Y.YUI2.util.Event;
        createPanel = function() {
            var container = document.createElement('div');
            container.setAttribute('id', 'popupContainer');
            document.body.appendChild(container);
            panel = new Y.YUI2.widget.Panel('popupContainer', {
                underlay: 'none',
                close: true,
                visible: false,
                draggable: true,
                constraintoviewport: true,
                modal: false
            });
        };
        showPanel = function(title, body, X, Y) {
            //FIXME: Hard coded this width value for beta feedback form.
            var width = (title.length * 7 > 334) ? title.length * 7 : 334;
            panel.setHeader(title);
            panel.setBody(body);
            panel.cfg.setProperty('width', width + 'px');
            panel.cfg.setProperty('X', X);
            panel.cfg.setProperty('Y', Y);
            panel.render();
            panel.show();
        };
        showWindow = function(url, type, strWidth, strHeight) {
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
            if (type == 'console-with-scrollbars') {
                tools = 'resizable,toolbar=no,location=no,scrollbars=yes';
            }
            if (strWidth && strHeight) {
                tools += ',width=' + strWidth + ',height=' + strHeight;
            }
            popupWindow = window.open(url, 'newWin', tools);
            popupWindow.focus();
        };
        createEventHandlers = function() {
            var i, anchors, args, popupAnchors = [];
            anchors = document.getElementsByTagName('A');
            for (i = 0; i < anchors.length; i++) {
                if (anchors[i].rel && anchors[i].rel.indexOf('popup') === 0) {
                    popupAnchors.push(anchors[i]);
                }
            }
            for (i = 0; i < popupAnchors.length; i++) {
                args = popupAnchors[i].rel.split(' ');
                if (!panel && (args[1] == 'local' || args[1] == 'faq')) {
                    createPanel();
                }
                if (args[1] == 'standard' || args[1] == 'console' || args[1] == 'console-with-scrollbars' || args[1] == 'fullscreen') {
                    popupAnchors[i].clicked = function(e) {
                        var args = this.rel.split(' ');
                        YUE.preventDefault(e);
                        showWindow(this.href, args[1], args[2], args[3]);
                    };
                } else if (args[1] == 'local') {
                    popupAnchors[i].clicked = function(e) {
                        var id, elm, title, body;
                        YUE.preventDefault(e);
                        id = this.rel.split(' ')[2];
                        elm = (document.getElementById(id)) ? document.getElementById(id) : 0;
                        title = (elm.getAttribute('title')) ? elm.getAttribute('title') : '';
                        body = (document.getElementById(id)) ? document.getElementById(id).innerHTML : '';
                        showPanel(title, body, YUE.getPageX(e), YUE.getPageY(e));
                    };
                } else if (args[1] == 'faq') {
                    popupAnchors[i].clicked = function(e) {
                        var id = this.rel.split(' ')[2];
                        YUE.preventDefault(e);
                        Y.YUI2.util.Connect.asyncRequest('GET', '/././content/popup.html?id=' + id, {
                            success: function(o) {
                                var id = o.argument.id, X = o.argument.X, Y = o.argument.Y, f = o.responseXML.documentElement, title = f.getElementsByTagName('a')[0].firstChild.data, body = f.getElementsByTagName('dd')[0].firstChild.data + '&nbsp;<a href="/././howto/index.html?id=' + id + '">More</a>';
                                o.argument.showPanel(title, body, X, Y);
                            },
                            argument: {
                                showPanel: showPanel,
                                X: YUE.getPageX(e),
                                Y: YUE.getPageY(e),
                                id: id
                            }
                        });
                    };
                }
            }
        };
        createEventHandlers();
        LANE.core.getChangeEvent().subscribe(createEventHandlers);
    });
});
