//most the tests was done in bookmark-links.spec.cy.js

describe('Bookmark Widget', () => {

    beforeEach(() => {
        cy.viewport(1101, 1750);
        cy.visit('/cypress-test/test/test-bookmarks.html?template=none');
        cy.get('#long-bookmarking').as('long-bookmarking');
    })

    it('add bookmark and test the li text length', () => {
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

    it('add bookmarks and test for manage bookmarks link', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            {
                statusCode: 200
            }).as('addBookmark');

        cy.get('#bookmarks li').should('have.length', 7);

        cy.get('#favorites-test').find('.dropdown-content').as('favorites-dropdown');
        cy.get('@favorites-dropdown').invoke('show');

        // Manage Bookmarks button should not be visible since fewer than 10 bookmarks
        cy.get('.manageBookmarks').should('not.be.visible');

        // add 4 bookmarks
        cy.get('#div-bookmarking').trigger('mouseover');
        cy.get('#div-bookmarking').trigger('mouseout');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        cy.wait('@addBookmark');
        cy.get('#div-bookmarking2').trigger('mouseover');
        cy.get('#div-bookmarking2').trigger('mouseout');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        cy.wait('@addBookmark');
        cy.get('#div-bookmarking3').trigger('mouseover');
        cy.get('#div-bookmarking3').trigger('mouseout');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        cy.wait('@addBookmark');
        cy.get('#link-bookmarking').trigger('mouseover');
        cy.get('#link-bookmarking').trigger('mouseout');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        cy.wait('@addBookmark');
        // should now have 11 bookmarks
        cy.get('#bookmarks li').should('have.length', 11);

        // Manage Bookmarks button should now be visible
        cy.get('@favorites-dropdown').invoke('show');
        cy.get('.manageBookmarks').should('be.visible');
    })
})