// show a message to the user.  Initially just calls alert()
{

    "use strict";

    /**
     * Shows a message to the user if the message is not empty.
     * @param {string} [message] - The message to display.
     */
    L.showMessage = (message) => {
        // exit if called without a message
        if (!message) {
            return;
        }

        alert(message);
    };
}