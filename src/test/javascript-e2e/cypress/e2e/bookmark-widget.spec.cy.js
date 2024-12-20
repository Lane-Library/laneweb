//most the tests was done in bookmark-links.spec.cy.js

describe('Bookmark Widget', () => {

    beforeEach(() => {
        cy.viewport(1101, 1750);
        cy.visit('/cypress-test/test/test-bookmarks.html?template=none');
        cy.get('#long-bookmarking').as('long-bookmarking');
    })

    it('add bookmark and tes the li text length', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            {
                statusCode: 200
            }).as('addBookmark');
        cy.get('#bookmarks li').should('have.length', 7);
        cy.get('@long-bookmarking').trigger('mouseover');
        cy.get('@long-bookmarking').trigger('mouseout');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        cy.get('.favorites .fa.fa-bookmark').should('have.class', 'shake');
        cy.wait('@addBookmark');
        cy.get('.favorites .fa.fa-bookmark').should('not.have.class', 'shake');
        cy.get('#bookmarks li').should('have.length', 8);
        cy.get('#bookmarks li').first().should('have.text', 'This is a long Bookmarking to te...');
    })


})