describe('Search pico toogle', () => {

    beforeEach(() => {
        cy.viewport(1200, 1000);
        cy.visit('/index.html');
    })


    it('Pico should not appear by default on index  page', () => {
        cy.get('.pico-on').should('be.not.visible');
        cy.get('.pico-off').should('be.not.visible');
        cy.get('.search-pico').should('be.not.visible');
    })

    it('Pico Link should appear  after selecting "clinical Search', () => {
        //select all-clinical
        cy.get('#main-search').select('clinical-all', { force: true });
        cy.get('.pico-on').should('be.visible');
        cy.get('.pico-off').should('be.not.visible');
        cy.get('.search-pico').should('be.visible');
    })


    it('Pico fields should appear  after selecting "clinical Search', () => {
        //select all-clinical
        cy.get('#main-search').select('clinical-all', { force: true });
        //parameters
        cy.get('fieldset input').as('picoField');

        cy.get('@picoField').should('have.attr', 'disabled');
        cy.get('.pico-on').should('be.visible');
        //Click on Pico link
        cy.get('.pico-on').click();
        cy.get('.pico-off').should('be.visible');
        cy.get('.pico-on').should('be.not.visible');
        cy.get('@picoField').should('have.not.attr', 'disabled');
        cy.get('@picoField').should('be.visible');
    })

    //click on pico off link
    it('Pico fields should disappear after clicking on Pico off link', () => {
        //select all-clinical
        cy.get('#main-search').select('clinical-all', { force: true });
        //parameters
        cy.get('fieldset input').as('picoField');
        //Click on Pico link
        cy.get('.pico-on').click();
        cy.get('.pico-off').click();
        cy.get('.pico-on').should('be.visible');
        cy.get('.pico-off').should('be.not.visible');
        cy.get('@picoField').should('have.attr', 'disabled');
        cy.get('@picoField').should('be.not.visible');
    })

})
