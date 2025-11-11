describe('Search Dropdown TestCase', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/index.html');
        cy.get('#main-search').as('searchDropdown');
        cy.get('#search-dropdown-label').as('searchDropdownLabel');

    });

    it('dropdown click activates', () => {
        cy.get('@searchDropdownLabel').should('include.text', 'All');
        cy.get('@searchDropdown').select('catalog-all', { force: true });
        cy.get('@searchDropdownLabel').should('have.text', 'Lane Catalog');
    });

    it('tracking', () => {
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*').as('gaCollect');

        cy.get('#main-search').select('catalog-all', { force: true });

        cy.waitForInterceptions('@gaCollect', (interception) => {
            return interception.request.body.match(/searchDropdownSelection.*from%20all-all%20to%20catalog-all/g);
        }, 1).then((filteredInterceptions) => {
            expect(filteredInterceptions).to.have.length(1);
        });
    });
});