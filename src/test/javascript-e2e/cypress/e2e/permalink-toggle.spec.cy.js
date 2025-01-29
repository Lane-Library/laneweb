describe('Permalink Toggle Test', () => {
    it('should toggle permalinks correctly', () => {
        cy.viewport(1101, 750);

        // intercept the GA POST and make sure "permalinkCopied" is present in the body (below)
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*', (req) => {
            if (req.body.includes('permalinkCopied')) {
                req.alias = 'gaCollect';
            }
        });

        cy.visit('/cypress-test/search.html?source=all-all&q=12&facets=recordType:%22bib%22');

        //hover over the more-detail-container container to make the permalink visible
        cy.get('.more-detail-container').trigger('mouseover');
        cy.get('.more-detail-container .sourceInfo').invoke('show');

        // intentionally use the containing div instead of the anchor
        // because clicking temporarily changes the innerHTML (in permalink-toggle.js)
        cy.get('.permalink').as('permalink');

        cy.get('@permalink').invoke('text').should('contain', 'Get Shareable Link');

        cy.get('@permalink').click();

        cy.get('@permalink').invoke('text').should('contain', 'Link copied');

        cy.wait(2100);

        cy.get('.permalink').invoke('text').should('contain', 'Get Shareable Link');

        cy.wait('@gaCollect').then((interception) => {
            expect(interception.request.body).to.include('lane%3ApermalinkCopied');
        });

    });
});