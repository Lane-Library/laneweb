(function() {

    "use strict";

    var nodelist, iframes = [], iframeNode, i,

    /**
     * A class for representing an embeded youtube iframe that replaces the iframe
     * with a thumbnail that can be clicked upon to get the video.
     *
     * @class YouTubeNode
     * @constructor
     * @param iframe
     *            {node} the youtube iframe node
     */
    YouTubeNode = function(iframe) {
        var id, url,
                src = iframe.src,
                temp = document.createElement("div"),
                self = this,
                click = function() {
                    self.replaceThumbnail();
                };
        this._iframe = iframe;
        this._parent = iframe.parentNode;
        id = src.substring(src.lastIndexOf("/", src.length) + 1, src.length);
        if (id.indexOf("?") > -1) {
            id = id.substring(0, id.indexOf("?"));
        }
        url = "//img.youtube.com/vi/" + id + "/mqdefault.jpg";
        temp.innerHTML = "<div class=\"youtube-thumbnail\"><img src=\"" + url + "\"/><i class=\"fa fa-play\"></i></div>";
        this._thumbnail = temp.firstChild;
        if (Element.prototype.addEventListener) {
            this._thumbnail.addEventListener("click", click);
        } else {
            this._thumbnail.attachEvent("onclick", click);
        }
        this._parent.replaceChild(this._thumbnail, this._iframe);
        this._iframe.src = this._iframe.src + "?autoplay=1";
    };

    YouTubeNode.prototype = {

        /**
         * causes the thumbnail to be replaced by the original iframe node
         *
         * @method replaceThumbnail
         */
        replaceThumbnail: function() {
            this._parent.replaceChild(this._iframe, this._thumbnail);
        }
    };

    // find youtube iframes and create YouTubeNodes using them.
    nodelist = document.getElementsByTagName("iframe");
    for (i = 0; i < nodelist.length; i++) {
        iframeNode = nodelist.item(i);
        if (iframeNode.src && /youtube/.test(iframeNode.src)) {
            iframes.push(iframeNode);
        }
    }
    for (i = 0; i < iframes.length; i++) {
        (new YouTubeNode(iframes[i]));
    }

})();
