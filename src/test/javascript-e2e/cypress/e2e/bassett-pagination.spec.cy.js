describe('Bassett Pagination', () => {
    beforeEach(() => {
        cy.viewport(1500, 2000);
        cy.visit('/cypress-test/biomed-resources/bassett/bassettsView.html?r=Head--Brain');
    })


    it('check next page button', () => {
        cy.intercept('get', '/plain/biomed-resources/bassett/raw/bassettsView.html?r=Head--Brain&page=2')
            .as('nextResult');
        cy.get('#next-page').click();
        cy.wait('@nextResult');
        cy.intercept('get', '/plain/biomed-resources/bassett/raw/bassettsView.html?r=Head--Brain&page=3')
            .as('nextResult');
        cy.get('#next-page').click();
        cy.wait('@nextResult');
        cy.get('input[name=page]').should('have.value', '3');
    });


    it('check set  page input to 8', () => {
        cy.intercept('get', '/biomed-resources/bassett/bassettsView.html?r=Head--Brain&page=8')
            .as('nextResult');
        cy.get('input[name=page]').first().type('{backspace}').type('8').type('{enter}');
        cy.wait('@nextResult');
        cy.get('input[name=page]').should('have.value', '8');
    });

    it('check error message if the page input greatest than the page limit', () => {
        cy.get('input[name=page]').first().type('{backspace}').type('18').type('{enter}');
        cy.get('.bassett-error').first().should('have.text', 'ERROR: page out of range');
    });
});