describe('lightbox', () => {

    beforeEach(() => {
        cy.viewport(1101, 1050);
        cy.visit('/cypress-test/index.html');
    })

    it('should show lightbox', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'test');
        cy.get('.lightbox').should('be.not.visible');
        cy.get('.lightboxbg').should('be.not.visible');
        cy.get('body').should('not.have.class', 'lightbox-active');
        cy.window().its('L.Lightbox').invoke('show');
        cy.get('body').should('have.class', 'lightbox-active');
        cy.get('.lightbox').should('be.visible').and('have.text', 'test');
        cy.get('.lightboxbg').should('have.css', 'opacity', '0.75');
    })


    it('should hide lightbox', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'test');
        cy.window().its('L.Lightbox').invoke('show');
        cy.window().its('L.Lightbox').invoke('hide');
        cy.get('.lightbox').should('be.not.visible');
        cy.get('.lightboxbg').should('be.not.visible');
    })


    it('should show lightbox content', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'Here is the content');
        cy.window().its('L.Lightbox').invoke('show');
        cy.get('.lightbox').should('have.text', 'Here is the content');
    })
});