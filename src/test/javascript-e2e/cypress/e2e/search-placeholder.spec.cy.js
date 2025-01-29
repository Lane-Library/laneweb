describe('Lane Search Place Holder Test Case', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/index.html');
        cy.get('input[name=q]').as('queryInput');
        cy.get('#main-search').as('searchDropdown');
    });

    it('test default search placeholder', () => {
        cy.get('@queryInput').should('have.attr', 'placeholder', 'Search articles, books, journals, databases, our website, and more');
    });

    it('test  placeholder for Clinical Search', () => {
        cy.get('@searchDropdown').select('clinical-all', { force: true });
        cy.get('@queryInput').should('have.attr', 'placeholder', 'Search evidence-based resources by patient condition or intervention');
    });

    it('test  placeholder for Catalog Search', () => {
        cy.get('@searchDropdown').select('catalog-all', { force: true });
        cy.get('@queryInput').should('have.attr', 'placeholder', 'Search books, journals, databases including print at Lane');
    });

    it('test  placeholder from Catalog Search to All Search', () => {
        cy.get('@searchDropdown').select('catalog-all', { force: true });
        cy.get('@searchDropdown').select('all-all', { force: true });
        cy.get('@queryInput').should('have.attr', 'placeholder', 'Search articles, books, journals, databases, our website, and more');
    });

});
