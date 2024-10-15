(function () {

    "use strict";


    class BookmarkEvent {
        constructor() {
            this.events = {};
        }

        first(event, listener) {
            if (!this.events[event]) {
                this.events[event] = [];
            }
            this.events[event].unshift(listener);
        }

        on(event, listener) {
            if (!this.events[event]) {
                this.events[event] = [];
            }
            this.events[event].push(listener);
        }


        emit(event, ...args) {
            if (this.events[event]) {
                this.events[event].forEach(listener => listener.apply(this, args));
            }
        }
    }
    L.BookmarkEvent = BookmarkEvent;

})();
