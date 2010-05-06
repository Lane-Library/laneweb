YUI().use('node', 'event', 'overlay','io-base',function(Y) {
    var panel, createPanel, showPanel, popupWindow, showWindow, createEventHandlers;
    createPanel = function() {
        var container = Y.Node.create('<div id="popupContainer"/>');
        Y.one('body').append(container);
        panel = new Y.Overlay({
            srcNode: container
        });
//            panel = new Y.YUI2.widget.Panel('popupContainer', {
//                underlay: 'none',
//                close: true,
//                visible: false,
//                draggable: true,
//                constraintoviewport: true,
//                modal: false
//            });
    };
    showPanel = function(title, body, X, Y) {
        //FIXME: Hard coded this width value for beta feedback form.
        var width = (title.length * 7 > 334) ? title.length * 7 : 334;
        panel.set('headerContent',title + '<a>Close</a>');
        panel.set('bodyContent', body);
        panel.set('x', X);
        panel.set('y', Y);
        //TODO: set the width somehow.
        
        panel.render();
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
        anchors = Y.all('a');
        for (i = 0; i < anchors.size(); i++) {
            if (anchors.item(i).get('rel') && anchors.item(i).get('rel').indexOf('popup') === 0) {
                popupAnchors.push(anchors.item(i));
            }
        }
        for (i = 0; i < popupAnchors.length; i++) {
            args = popupAnchors[i].get('rel').split(' ');
            if (!panel && (args[1] == 'local' || args[1] == 'faq')) {
                createPanel();
            }
            if (args[1] == 'standard' || args[1] == 'console' || args[1] == 'console-with-scrollbars' || args[1] == 'fullscreen') {
                Y.on('click', function(e) {
                    var args = this.get('rel').split(' ');
                    e.preventDefault();
                    showWindow(this.get('href'), args[1], args[2], args[3]);
                }, popupAnchors[i]);
            } else if (args[1] == 'local') {
                Y.on('click', function(e) {
                    var id, elm, title, body;
                    e.preventDefault();
                    id = this.get('rel').split(' ')[2];
                    elm = Y.one('#' + id);
                    title = elm && elm.get('title') ? elm.get('title') : '';
                    body = elm ? elm.get('innerHTML') : '';
                    showPanel(title, body, e.pageX, e.pageY);
                }, popupAnchors[i]);
            } else if (args[1] == 'faq') {
                Y.on('click', function(e) {
                    var id = this.get('rel').split(' ')[2];
                    e.preventDefault();
                    Y.io('/././content/popup.html?id=' + id, {
                        on: {
                            success: function(tansactionId, o, argument) {
                                var id = argument.id,
                                    X = argument.X,
                                    Y = argument.Y,
                                    f = o.responseXML.documentElement,
                                    title = f.getElementsByTagName('a')[0].firstChild.data,
                                    body = f.getElementsByTagName('dd')[0].firstChild.data + '&nbsp;<a href="/././howto/index.html?id=' + id + '">More</a>';
                                argument.showPanel(title, body, X, Y);
                            }
                        },
                        argument: {
                            showPanel: showPanel,
                            X: e.pageX,
                            Y: e.pageY,
                            id: id
                        }
                    });
                }, popupAnchors[i]);
            }
        }
    };
    createEventHandlers();
    Y.Global.on('lane:change', createEventHandlers);
});
