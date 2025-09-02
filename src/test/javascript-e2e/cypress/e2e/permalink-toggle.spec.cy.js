describe('Permalink Toggle Test', () => {
    it('should toggle permalinks correctly and fire a tracking event', () => {
        cy.visit('/cypress-test/search.html?source=all-all&q=12&facets=recordType:%22bib%22', {
            // Stub the clipboard API before the page loads
            onBeforeLoad(win) {
                cy.stub(win.navigator.clipboard, 'writeText').resolves();
            },
        });

        // Intercept the GA event
        cy.intercept('POST', '**/collect*').as('gaCollect');

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

        cy.wait('@gaCollect').its('request.body').should('include', 'lane%3ApermalinkCopied');

        // Assert the text reverts
        cy.get('@permalink', { timeout: 2100 })
            .should('contain.text', 'Get Shareable Link');
    });
});