describe('Holdings Toggle', () => {

    it('should show and then hide holdings info and report the clicks to GA', () => {

        cy.visit('/cypress-test/search.html?q=12&source=all-all&facets=recordType:"bib"');

        cy.intercept('/apps/*', {
            statusCode: 200,
            body: '{}'
        });

        cy.window().then((win) => {
            cy.spy(win.L, 'fire').as('lanewebSpy');
        });

        cy.get('.hldgsTrigger').first().as('hldgsTrigger');
        cy.get('.table-main').first().as('tableMain');
        cy.get('@tableMain').should('be.visible');
        cy.get('@hldgsTrigger').click();
        cy.get('@tableMain').should('not.be.visible');

        cy.get('@lanewebSpy').should('have.been.calledWith', 'tracker:trackableEvent', {
            category: 'lane:hldgsTrigger',
            action: 'Digital Access -- close',
            label: 'Lancet'
        });

        cy.get('@hldgsTrigger').click();
        cy.get('@tableMain').should('be.visible');

        cy.get('@lanewebSpy').should('have.been.calledWith', 'tracker:trackableEvent', {
            category: 'lane:hldgsTrigger',
            action: 'Digital Access -- open',
            label: 'Lancet'
        });
    })
})