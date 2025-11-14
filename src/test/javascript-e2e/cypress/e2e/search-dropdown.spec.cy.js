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

        cy.window().then((win) => {
            cy.spy(win.L, 'fire').as('lanewebSpy');
        });

        cy.get('#main-search').select('catalog-all', { force: true });

        cy.get('@lanewebSpy').should('have.been.calledWith', 'tracker:trackableEvent', {
            category: 'lane:searchDropdownSelection',
            action: 'catalog-all',
            label: 'from all-all to catalog-all'
        });
    });
});