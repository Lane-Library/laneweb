describe('lightbox', () => {

    beforeEach(() => {
        cy.viewport(1101, 1050);
        cy.visit('/cypress-test/index.html');
    })

    it('test lightbox show', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'test');
        cy.window().its('L.Lightbox').invoke('show');
        cy.get('.lightbox').should('be.visible');
        cy.get('.lightboxbg').should('be.visible');
    })


    it('test lightbox hide', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'test');
        cy.window().its('L.Lightbox').invoke('show');
        cy.window().its('L.Lightbox').invoke('hide');
        cy.get('.lightbox').should('be.not.visible');
        cy.get('.lightboxbg').should('be.not.visible');
    })


    it('test lightbox content', () => {
        cy.window().its('L.Lightbox').invoke('setContent', 'Hree is the content');
        cy.window().its('L.Lightbox').invoke('show');
        cy.get('.lightbox').should('have.text', 'Hree is the content');
    })
});