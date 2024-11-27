describe('Lane Help Link Test Case', () => {
    beforeEach(() => {
        cy.visit('/index.html');
        cy.get('.search-help a').as('searchHelp');
        cy.get('#main-search').as('searchDropdown');
    });

    it('test default help link', () => {
        cy.get('@searchHelp').should('have.attr', 'href', '/help/search/lanesearch.html');
        cy.get('@searchHelp').should('be.visible');
    });

    it('test help link for Clinical Search', () => {
        cy.get('@searchDropdown').select('clinical-all', { force: true });
        cy.get('.pico-toggle a').should('have.attr', 'href', '/help/search/picosearch.html');
        cy.get('.pico-toggle a').should('be.visible');
    });

    it('test help link for Laen Catalog', () => {
        cy.get('@searchDropdown').select('catalog-all', { force: true });
        cy.get('@searchHelp').should('have.attr', 'href', '/help/search/lanecatalog.html');
        cy.get('@searchHelp').should('be.visible');
    });


});