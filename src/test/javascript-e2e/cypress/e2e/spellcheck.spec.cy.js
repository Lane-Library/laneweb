describe('Spellcheck', () => {

    it('displays spelling suggestion', () => {

        cy.intercept('/apps/spellcheck/json*', { fixture: 'spellcheck/result.json' });

        cy.visit('/cypress-test/search.html?q=ards+cacner&source=all-all');

        cy.get('.spellCheck a').should('be.visible');
    })

    it('displays no spelling suggestion', () => {

        cy.intercept('/apps/spellcheck/json*', { fixture: 'spellcheck/no-result.json' });

        cy.visit('/cypress-test/search.html?q=ards&source=all-all');

        cy.get('.spellCheck a').should('not.be.visible');
    })

    it('display no spelling suggestion even when the service is down', () => {

        cy.intercept('/apps/spellcheck/json*', { statusCode: 500 });

        cy.visit('/cypress-test/search.html?q=ards&source=all-all');

        cy.get('.spellCheck a').should('not.be.visible');
    })
})