describe('Lane Search Indicator Test Case', () => {
    beforeEach(() => {
        cy.visit('/index.html');
        cy.get('.search-indicator').as('searchIndicator');
        cy.get('#main-search').as('searchDropdown');
    });

    it('test default search indicator not appearing', () => {
        cy.get('@searchIndicator').should('be.not.visible');
    });

    it('test search indicator appearing', () => {
        cy.get('input[name=q]').type('skin');
        cy.window().then((win) => {
            const searchButton = win.document.querySelector('.search-button');
            searchButton.click();
        });
        cy.get('@searchIndicator').should('be.visible');
    });

    it('should click on the link without waiting for the response', () => {
        cy.get('input[name=q]').type('skin');
        cy.window().then((win) => {
            const searchButton = win.document.querySelector('.search-button');
            searchButton.click();
        });
        cy.get('@searchIndicator').should('be.visible');
        cy.wait(500);
        cy.get('@searchIndicator').should('be.not.visible');
    });

    it('test search indicator NOT appearing on back page', () => {
        cy.get('input[name=q]').type('skin');
        cy.get('.search-button').click();
        // Navigate back to the previous page
        cy.go('back');
        cy.get('@searchIndicator').should('be.not.visible');
    });
});