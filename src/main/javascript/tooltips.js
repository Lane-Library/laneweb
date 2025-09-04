(() => {

    "use strict";

    const SHOW_DELAY = 250;
    const HIDE_DELAY = 100;
    const OFFSET_X = 15;
    const OFFSET_Y = 15;
    const TRIGGER_CLASS = 'tooltip-trigger';

    /**
     * @class Tooltip
     * @description Manages the lifecycle of a single tooltip element on the page,
     * handling all triggers within a specified delegate container.
     */
    class Tooltip {
        /**
         * @param {object} options
         * @param {HTMLElement} options.delegate - The container element to listen for events on.
         * @param {Map<string, string>} options.contentMap - A map of trigger IDs to their HTML content.
         */
        constructor({ delegate, contentMap }) {
            this.delegate = delegate;
            this.contentMap = contentMap;

            this.tooltipElement = null;
            this.activeTrigger = null;
            this.timers = { show: null, hide: null };

            // Bind the core event handler once to the delegate container.
            // 'mouseover' is used for reliable delegation of entering an element and its children.
            this.delegate.addEventListener('mouseover', this._handleMouseOver);
            // also need to detect when the mouse leaves an element and its children.
            this.delegate.addEventListener('mouseout', this._handleMouseOut);
        }

        /**
         * The primary event handler. It determines if the mouse is over a new trigger
         * or has left a trigger area, then initiates the correct action.
         */
        _handleMouseOver = (event) => {
            const trigger = event.target.closest(`.${TRIGGER_CLASS}`);
            if (!trigger) return;

            // If we are moving over the same trigger, just cancel any pending hide.
            if (trigger === this.activeTrigger) {
                this._clearTimers();
                return;
            }

            // A new trigger has been entered.
            this._startShowTimer(trigger);
        }

        /**
         * Handles the mouse leaving a potential trigger zone.
         */
        _handleMouseOut = (event) => {
            const trigger = event.target.closest(`.${TRIGGER_CLASS}`);

            // Only act if we are leaving the currently active trigger.
            if (trigger === this.activeTrigger) {
                // If the mouse is moving to the tooltip itself, don't hide.
                const toElement = event.relatedTarget;
                if (toElement && this.tooltipElement?.contains(toElement)) {
                    return;
                }
                this._startHideTimer();
            }
        }

        /**
         * Initiates the process of showing a tooltip for a given trigger element.
         * @param {HTMLElement} trigger
         */
        _startShowTimer(trigger) {
            // Hide any currently visible tooltip immediately before proceeding with the new one. 
            // This prevents tooltip stacking.
            if (this.tooltipElement) {
                this._hide();
            }

            this._clearTimers();
            this.activeTrigger = trigger;

            this.timers.show = setTimeout(() => {
                this._show();
            }, SHOW_DELAY);
        }

        /**
         * Starts the timer to hide the currently active tooltip.
         */
        _startHideTimer() {
            this._clearTimers();
            this.timers.hide = setTimeout(this._hide, HIDE_DELAY);
        }

        /**
         * Creates, positions, and displays the tooltip element on the page.
         */
        _show = () => {
            if (!this.activeTrigger) return;

            const originalTitle = this.activeTrigger.title || '';
            const content = this.contentMap.get(this.activeTrigger.id) ?? originalTitle;
             // Don't show an empty tooltip.
            if (!content) return;

            // Temporarily disable the browser's native tooltip to prevent it from overlapping.
            if (originalTitle) {
                this.activeTrigger.setAttribute('data-original-title', originalTitle);
                this.activeTrigger.title = '';
            }

            this.tooltipElement = document.createElement('div');
            this.tooltipElement.className = 'tooltip tooltip-content';
            this.tooltipElement.innerHTML = content;
            this.tooltipElement.style.zIndex = '1000';

            // Add listeners to the tooltip itself to keep it alive when the user hovers over it.
            this.tooltipElement.addEventListener('mouseenter', this._clearTimers);
            this.tooltipElement.addEventListener('mouseleave', this._startHideTimer);

            document.body.appendChild(this.tooltipElement);
            this._position();
        }

        /**
         * Hides and completely destroys the tooltip element and resets state.
         */
        _hide = () => {
            // Restore the native title attribute on the trigger.
            if (this.activeTrigger) {
                const originalTitle = this.activeTrigger.getAttribute('data-original-title');
                if (originalTitle) {
                    this.activeTrigger.title = originalTitle;
                    this.activeTrigger.removeAttribute('data-original-title');
                }
            }

            this.tooltipElement?.remove();

            // Reset state properties.
            this.tooltipElement = null;
            this.activeTrigger = null;
            this._clearTimers();
        }

        /**
         * Positions the tooltip element relative to its active trigger.
         */
        _position() {
            if (!this.tooltipElement || !this.activeTrigger) return;

            const triggerRect = this.activeTrigger.getBoundingClientRect();
            const ttRect = this.tooltipElement.getBoundingClientRect();

            let x = window.scrollX + triggerRect.left + (triggerRect.width / 2) - (ttRect.width / 2);
            let y = window.scrollY + triggerRect.bottom + OFFSET_Y;

            // Basic viewport collision detection
            if (y + ttRect.height > window.innerHeight + window.scrollY) {
                y = window.scrollY + triggerRect.top - ttRect.height - OFFSET_Y;
            }
            if (x < window.scrollX) {
                x = window.scrollX + OFFSET_X;
            }
            if (x + ttRect.width > window.innerWidth + window.scrollX) {
                x = window.innerWidth + window.scrollX - ttRect.width - OFFSET_X;
            }

            this.tooltipElement.style.left = `${x}px`;
            this.tooltipElement.style.top = `${y}px`;
        }

        /**
         * A utility to clear all active timers.
         */
        _clearTimers = () => {
            clearTimeout(this.timers.show);
            clearTimeout(this.timers.hide);
        }

        /**
         * Public method to completely remove all listeners and clean up the instance.
         */
        destroy() {
            this._hide();
            this.delegate.removeEventListener('mouseover', this._handleMouseOver);
        }
    }

    /**
     * Scans the DOM for tooltip content and trigger elements, then initializes the Tooltip manager.
     */
    const initializeTooltips = () => {
        const delegate = document.querySelector('.content') || document.body;
        const contentMap = new Map();

        document.querySelectorAll('.tooltips > *[id]').forEach(contentEl => {
            const triggerId = contentEl.id.replace(/Tooltip$/, '');
            if (triggerId) {
                const triggerEl = document.getElementById(triggerId);
                if (triggerEl) {
                    contentMap.set(triggerId, contentEl.innerHTML);
                    triggerEl.classList.add(TRIGGER_CLASS);
                }
            }
        });

        if (contentMap.size > 0) {
            L.ToolTips = new Tooltip({ delegate, contentMap });
        }
    };

    initializeTooltips();

    // Re-initialize when new content is loaded.
    L.on("lane:new-content", () => {
        L.ToolTips?.destroy();
        initializeTooltips();
    });

})();
