describe('Mobile Menu', () => {

    beforeEach(() => {
        cy.viewport(801, 1000);
        cy.visit('/cypress-test/index.html');

        // Parameters
        cy.get('.fa-bars').as('hamburger');
        cy.get('#nav-toggle-off .fa-xmark').as('offButton');
        cy.get('#navigation nav .nav-menu:nth-child(3)').as('menu1');
        cy.get('#navigation nav .nav-menu:nth-child(4)').as('menu2');
        cy.get('#navigation nav .nav-menu:nth-child(3) .nav-menu-content').as('menuContent1');
        cy.get('#navigation nav .nav-menu:nth-child(4) .nav-menu-content').as('menuContent2');

    })

    it('Test initial  hamburger status the menu', () => {
        cy.get('@hamburger').should('be.visible');
        cy.get('@offButton').should('be.not.visible');
        cy.get('@menu1').should('be.not.visible');
        cy.get('@menu2').should('be.not.visible');
    })

    it('Test click on hamburger icon', () => {
        cy.get('@hamburger').click();
        cy.get('@hamburger').should('be.not.visible');
        cy.get('@offButton').should('be.visible');
        cy.get('@menu1').should('be.visible');
        cy.get('@menu2').should('be.visible');
    })


    it('Test click on OFF icon', () => {
        cy.get('@hamburger').click();
        cy.get('@offButton').click();
        cy.get('@hamburger').should('be.visible');
        cy.get('@offButton').should('be.not.visible');
        cy.get('@menu1').should('be.not.visible');
        cy.get('@menu2').should('be.not.visible');
    })

    it('Test click on one menu', () => {
        cy.get('@hamburger').click();
        cy.get('@menu1').click();
        cy.get('@menu1').should('be.visible');
        cy.get('@menu2').should('be.visible');
        cy.get('@menuContent1').should('be.visible');
        cy.get('@menuContent2').should('be.not.visible');
        cy.get('@menuContent1').click();
        cy.get('@menuContent1').should('be.not.visible');
        cy.get('@menuContent2').should('be.not.visible');
    })

    it('Test click on  two menus', () => {
        cy.get('@hamburger').click();
        cy.get('@menu1').click();
        cy.get('@menu1').should('be.visible');
        cy.get('@menu2').should('be.visible');
        cy.get('@menuContent1').should('be.visible');
        cy.get('@menuContent2').should('be.not.visible');
        cy.get('@menu2').click();
        cy.get('@menuContent1').should('be.visible');
        cy.get('@menuContent2').should('be.visible');
    })


    it('Test Blur effect on click on hamburger icon', () => {
        cy.get('@hamburger').click();
        cy.get('.content').should('have.class', 'blur');
        cy.get('footer').should('have.class', 'blur');
        cy.get('.mobile-screen-menu.lrg-screen-hide').should('have.class', 'blur');
    })

    it('Test Blur effect on click on OFF icon', () => {
        cy.get('@hamburger').click();
        cy.get('@offButton').click();
        cy.get('.content').should('not.have.class', 'blur');
        cy.get('footer').should('not.have.class', 'blur');
        cy.get('.mobile-screen-menu.lrg-screen-hide').should('not.have.class', 'blur');
    })

    it('Test Blur effect on click on OFF icon and loaded with hash #navigation', () => {
        cy.visit('/cypress-test/index.html#navigation');
        cy.get('@offButton').click();
        cy.get('@hamburger').click();
        cy.get('.content').should('have.class', 'blur');
        cy.get('footer').should('have.class', 'blur');
        cy.get('.mobile-screen-menu.lrg-screen-hide').should('have.class', 'blur');
    })

})