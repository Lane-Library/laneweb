describe('Suggest', () => {

    beforeEach(() => {
        cy.viewport(1200, 3000);
        cy.visit('/search.html?q=skin&source=all-all');
        // Parameters
        cy.get('.solrFacets input[data-facet=type]').as('input');
    })

    it('suggestion should not exist on loading page', () => {
        cy.get('.aclist-item').should('not.exist');
    })

    it('suggestion should exist after typing 1 characters', () => {
        cy.get('@input').type('b');
        cy.get('.aclist-item').should('exist');
    })


    it('suggestion should exist after typing 3 characters', () => {
        cy.get('@input').type('book');
        cy.get('.aclist-item').should('exist');
        //count how many suggestions
        cy.get('.aclist-item').should('have.length', 3);
    })

    it('suggestion should not exist after typing an unknown word', () => {
        cy.intercept('/apps/solr/facet/suggest*', { fixture: 'solr-suggest-not-found.json' }).as('suggestNoMatch');
        cy.get('@input').type('inconnu');
        cy.wait('@suggestNoMatch');
        cy.get('.aclist-item').first().should('have.text', 'No match found');
    })

    //click on the suggest and check the input value
    it('click on suggestion', () => {
        cy.get('@input').type('book');
        cy.get('.aclist-item').first().click();
        cy.wait(500);
        //Check url after click
        cy.url().should('include', '/search.html?q=skin&source=all-all&facets=type%3A%22Book%22');
        //check Filter Applied Value
        cy.get('.filter-facet div a').should('have.text', 'Book');
        //check the Resource Type value is selected
        cy.get('.facet-container .facetLabel i').first().should('have.class', 'fa-square-check');
    })

    //Test suggestion after the search input loosing the focus
    it('suggestion should not exist after loosing focus', () => {
        cy.get('@input').type('skin');
        cy.get('.aclist-item').should('exist');
        cy.get('section a').first().trigger('mouseover');
        cy.get('.aclist-item').should('have.text', 'No match found');
    })



})