describe('Guide Menus', () => {

    beforeEach(() => {
        cy.viewport(2101, 3000);
        cy.visit('/cypress-test/help/guides/index.html#event-guides');

        // Parameters
        cy.get('#event-guides a[href="#off"]').as('hideMenu');
        cy.get('#event-guides a[href="#event-guides"]').as('showMenu');

        cy.get('.menu-container a[href="#all-guides"').as('allGuidesLink');
        cy.get('a[href="#event-guides"').as('eventGuidesLink');

        cy.get('#event-guides div.guide-container .guide').as('eventGuidesContainer');
        cy.get('#all-guides div.guide-container .guide').as('allGuidesContainer');
    });

    it('test the guide page whit ash #event-guides', () => {


        // Check if items status in left menu
        cy.get('@eventGuidesLink').should('have.class', 'menuitem-active');
        cy.get('@allGuidesLink').should('have.not.class', 'menuitem-active');


        // Check if the link to close menu  is displayed
        cy.get('@hideMenu').should('be.visible');
        cy.get('@showMenu').should('be.not.visible');
        cy.get('@eventGuidesContainer').should('be.visible');
        cy.get('@allGuidesContainer').should('be.not.visible');

        // Close menu
        cy.get('@hideMenu').click();

        // Check if items status in left menu
        cy.get('@eventGuidesLink').should('have.not.class', 'menuitem-active');
        cy.get('@allGuidesLink').should('have.not.class', 'menuitem-active');

        // Check if items are active in left menu
        cy.get('@hideMenu').should('be.not.visible');
        cy.get('@showMenu').should('be.visible');

        // Open menu from left menu
        cy.get('@allGuidesLink').click();

        // Check if items status in left menu
        cy.get('@allGuidesLink').should('have.class', 'menuitem-active');
        cy.get('@eventGuidesLink').should('have.not.class', 'menuitem-active');

        // Check if the link to close menu  is displayed
        cy.get('@allGuidesContainer').should('be.visible');
        cy.get('#speciality-guides div.guide-container .guide').should('be.not.visible');

    });

    it('test the guide menu whitout ash', () => {
        cy.visit('/cypress-test/help/guides/index.html');

        cy.get('#all-guides a[href="#off"]').as('hideMenu');
        cy.get('#all-guides a[href="#all-guides"]').as('showMenu');

        // Check if items status in left menu
        cy.get('@allGuidesLink').should('have.class', 'menuitem-active');
        cy.get('@eventGuidesLink').should('have.not.class', 'menuitem-active');


        // Check if the link to close menu  is displayed
        cy.get('@hideMenu').should('be.visible');
        cy.get('@showMenu').should('be.not.visible');
        cy.get('@allGuidesContainer').should('be.visible');
        cy.get('@eventGuidesContainer').should('be.not.visible');

    })
})