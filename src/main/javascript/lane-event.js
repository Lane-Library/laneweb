(function () {

    "use strict";


    class LaneEvent {
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

        off(event, listener) {
            if (!this.events[event]) return;
            this.events[event] = this.events[event].filter(l => l !== listener);
        }

        emit(event, ...args) {
            if (this.events[event]) {
                this.events[event].forEach(listener => listener.apply(this, args));
            }
        }
    }
    L.LaneEvent = LaneEvent;

})();
