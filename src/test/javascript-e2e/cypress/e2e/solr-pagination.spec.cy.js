describe('Solr Pagination', () => {

    beforeEach(() => {
        cy.viewport(1200, 1000);
        cy.visit('/cypress-test/search.html?q=Skin+and+Connective+Tissue+Diseases&source=all-all&facets=type:"Book%20Print"');
        cy.get('input[name=page]').first().as('page');
    })

    it('should have a error message after a 13 page pagination', () => {
        cy.get('@page').should('have.value', '1');
        cy.get('@page').type('3').type('{enter}');
        cy.get('.s-pagination .error').should('have.text', 'ERROR: page out of range');
    });


    it('should not  have a error message', () => {
        cy.get('@page').should('have.value', '1');
        cy.get('@page').type('{backspace}').type('3').type('{enter}');
        cy.get('.s-pagination .error').should('not.exist');
    });


})