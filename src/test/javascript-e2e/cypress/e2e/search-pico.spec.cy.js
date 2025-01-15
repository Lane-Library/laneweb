describe('Search pico', () => {

    beforeEach(() => {
        cy.viewport(1200, 1000);
        cy.visit('/cypress-test/index.html');
        cy.get('#main-search').select('clinical-all', { force: true });
        cy.get('.pico-on').click();
    })



    it('Check if the query field is set ', () => {
        cy.get('#main-search').select('clinical-all', { force: true });
        cy.get('input[name=p]').type('condition');
        cy.get('input[name=i]').type('intervention');
        cy.get('input[name=c]').type('comparison');
        cy.get('input[name=o]').type('outcome');
        cy.get('input[name=q]').should('have.value', '(condition) AND (intervention) AND (comparison) AND (outcome)');
    })

    it('Check if all the input are reset', () => {
        cy.get('#main-search').select('clinical-all', { force: true });
        cy.get('input[name=p]').type('condition');
        cy.get('input[name=i]').type('intervention');
        cy.get('input[name=c]').type('comparison');
        cy.get('input[name=o]').type('outcome');
        cy.get('.search-reset.search-reset-active').click();
        cy.get('input[name=p]').should('have.value', '');
        cy.get('input[name=i]').should('have.value', '');
        cy.get('input[name=c]').should('have.value', '');
        cy.get('input[name=o]').should('have.value', '');
        cy.get('input[name=q]').should('have.value', '');
    })

    it('Check a select suggestion', () => {
        cy.get('#main-search').select('clinical-all', { force: true });
        cy.get('input[name=p]').type('skin');
        cy.get('.aclist-list li').first().click();
        cy.get('input[name=p]').should('have.value', 'Skin');
    })


})