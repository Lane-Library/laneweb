// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add('login', (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })

// -- Lane added custom commands --

/**
 * Recursively waits for a specific number of intercepted requests that match a filter.
 * This is useful for testing asynchronous events where the exact timing and total number
 * of network requests are unpredictable.
 *
 * @param {string} alias - The alias of the cy.intercept() command (e.g., '@gaCollect').
 * @param {Function} filterFn - A function that takes an interception object and returns true if it matches the desired criteria.
 * @param {number} expectedCount - The number of matching interceptions to wait for.
 * @param {Object} [options] - Optional configuration for the wait behavior.
 * @param {number} [options.retries=10] - The number of times to retry before failing.
 * @param {number} [options.delay=500] - The delay in milliseconds between each retry.
 * @returns {Cypress.Chainable<Array>} - Yields the array of filtered interceptions for further assertions.
 *
 * @example
 * // Wait for 2 GA events related to 'descriptionTrigger'
 * cy.waitForInterceptions('@gaCollect', (interception) => {
 *   return interception.request.body.includes('descriptionTrigger');
 * }, 2).then((filteredInterceptions) => {
 *   expect(filteredInterceptions).to.have.length(2);
 * });
 */
Cypress.Commands.add('waitForInterceptions', (alias, filterFn, expectedCount, options = {}) => {
    const { retries = 10, delay = 500 } = options;

    const checkInterceptions = (retryCount) => {
        // Use cy.get with a timeout of 0 to avoid waiting for the alias itself,
        // as we are managing the wait manually.
        return cy.get(`${alias}.all`, { timeout: 0 }).then((interceptions) => {
            const filteredInterceptions = interceptions.filter(filterFn);

            if (filteredInterceptions.length >= expectedCount) {
                // Condition met, yield the filtered results.
                return cy.wrap(filteredInterceptions);
            }

            if (retryCount > 0) {
                // Wait for the specified delay and then recurse.
                cy.wait(delay, { log: true });
                return checkInterceptions(retryCount - 1);
            }

            // If retries are exhausted, fail the test with a clear message.
            return cy.wrap(filteredInterceptions).then((finalInterceptions) => {
                throw new Error(`Timed out after ${retries * delay}ms. Expected ${expectedCount} matching interceptions for alias '${alias}', but found ${finalInterceptions.length}.`);
            });
        });
    };

    return checkInterceptions(retries);
});