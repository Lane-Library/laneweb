
describe('Bookmark Links', () => {

    beforeEach(() => {
        cy.visit('/index.html');
        cy.get('ul.bookmarking li a').first().as('pubmedLink');

    })

    it('test over link if icon popup', () => {
        cy.get('.bookmark-link').should('be.not.exist');
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link.active').should('be.not.exist');
    })


    it('test over icon to be active', () => {
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('@pubmedLink').trigger('mouseleave');

        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link.active').should('be.visible');
    })

    it('test leave icon but back to the link', () => {
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('@pubmedLink').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover').as('bookmarkLink');
        cy.get('@bookmarkLink').trigger('mouseleave');
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('.bookmark-link').should('be.visible');
        cy.get('.bookmark-link.active').should('be.not.exist');
    })

    it('test leave icon and after leaving pubmed link ', () => {
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('@pubmedLink').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover').as('bookmarkLink');
        cy.get('@bookmarkLink').trigger('mouseleave');
        cy.get('.lane-header').trigger('mouseover');
        cy.get('@bookmarkLink').should('be.not.exist');
    })

    it('test click on icon', () => {
        cy.get('@pubmedLink').trigger('mouseover');
        cy.get('@pubmedLink').trigger('mouseleave');
        cy.get('.bookmark-link').trigger('mouseover');
        cy.get('.bookmark-link').click();
    })

});