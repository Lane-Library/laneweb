describe('Permalink Toggle Test', () => {
    it('should toggle permalinks correctly and fire a tracking event', () => {
        cy.visit('/cypress-test/search.html?source=all-all&q=12&facets=recordType:%22bib%22', {
            // Stub the clipboard API before the page loads
            onBeforeLoad(win) {
                // The Clipboard API may be undefined in non-secure contexts (like http in CLI)
                // so we need to create a mock object to stub if it doesn't exist.
                if (!win.navigator.clipboard) {
                    win.navigator.clipboard = {
                        writeText: () => {} // does nothing
                    };
                }

                cy.stub(win.navigator.clipboard, 'writeText').resolves();
            }
        });

        // intercept the GA POST
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*').as('gaCollect');

        cy.viewport(1101, 750);

        cy.get('.more-detail-container').trigger('mouseover');
        cy.get('.more-detail-container .sourceInfo').invoke('show');

        cy.get('.permalink').as('permalink')
            .should('contain.text', 'Get Shareable Link');

        cy.get('@permalink').click();

        // Assert that the stubbed function was called.
        // Cypress will automatically retry this assertion, effectively pausing
        // the test until the click handler calls `writeText`.
        cy.window().then((win) => {
            expect(win.navigator.clipboard.writeText).to.have.been.calledOnce;
        });

        cy.get('@permalink').should('contain.text', 'Link copied');

        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.includes('permalinkCopied');
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });

        // Assert the text reverts
        cy.get('@permalink', { timeout: 2100 })
            .should('contain.text', 'Get Shareable Link');
    });
});