(function() {

    var nodelist, iframes = [], iframe, i;

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
        var src = iframe.src,
                id = src.substring(src.lastIndexOf("/", src.length) + 1, src.length),
                url = "http://img.youtube.com/vi/" + id + "/mqdefault.jpg",
                temp = document.createElement("div"),
                self = this,
                click = function() {
                    self.replaceThumbnail();
                };
        this._iframe = iframe;
        this._parent = iframe.parentNode;
        temp.innerHTML = "<img class=\"youtube-thumbnail\" src=\"" + url + "\"/>";
        this._thumbnail = temp.firstChild;
        if (Element.prototype.addEventListener) {
            this._thumbnail.addEventListener("click", click);
        } else {
            this._thumbnail.attachEvent("onclick", click);
        }
        this._parent.replaceChild(this._thumbnail, this._iframe);
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
        iframe = nodelist.item(i);
        if (iframe.src && /youtube/.test(iframe.src)) {
            iframes.push(iframe);
        }
    }
    for (i = 0; i < iframes.length; i++) {
        (new YouTubeNode(iframes[i]));
    }

})();