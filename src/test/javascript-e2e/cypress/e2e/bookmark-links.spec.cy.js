
describe('Bookmark Links', () => {

    beforeEach(() => {
        cy.viewport(1101, 1750);
        cy.visit('/test/test-bookmarks.html?template=none');
        cy.get('#div-bookmarking').as('div-bookmarking');
        cy.get('#link-bookmarking').as('link-bookmarking');
    })

    it('test over link if icon popup', () => {
        cy.get('.bookmark-link').should('be.not.exist');
        cy.get('@div-bookmarking').trigger('mouseover');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link.active').should('be.not.exist');
    })

    it('test over no bookmarking link', () => {
        cy.get('#not-bookmarking').trigger('mouseover');
        cy.get('.bookmark-link').should('be.not.exist');
    })

    it('mouse over #div-bookmarking  link icon to be active', () => {
        cy.get('@div-bookmarking').trigger('mouseover');
        cy.get('@div-bookmarking').trigger('mouseleave');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link').trigger('mouseover');
        // cy.get('.bookmark-link').should('have.class', 'active');
    })

    it('mouse over #link-bookmarking  link icon to be active', () => {
        cy.get('@link-bookmarking').trigger('mouseover');
        cy.get('@link-bookmarking').trigger('mouseleave');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link').trigger('mouseover');
        // cy.get('.bookmark-link').should('have.class', 'active');
    })

    it('test leave icon but back to the link', () => {
        cy.get('@div-bookmarking').trigger('mouseover');
        cy.get('@div-bookmarking').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover').as('bookmarkLink');
        cy.get('@bookmarkLink').trigger('mouseleave');
        cy.get('@div-bookmarking').trigger('mouseover');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link.active').should('be.not.exist');
    })

    it('test leave icon and after leaving  link ', () => {
        cy.get('@link-bookmarking').trigger('mouseover');
        cy.get('@link-bookmarking').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover').as('bookmarkLink');
        cy.get('@bookmarkLink').trigger('mouseleave');
        cy.get('#not-bookmarking').trigger('mouseover');
        // cy.wait(300);
        // cy.get('@bookmarkLink').should('be.not.exist');
    })

    it('test click on icon', () => {
        cy.intercept(
            'POST',
            '/bookmarks',
            {
                statusCode: 200
            }).as('addBookmark');
        cy.get('@div-bookmarking').trigger('mouseover');
        cy.get('@div-bookmarking').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
        // cy.wait('@addBookmark');
    })

});