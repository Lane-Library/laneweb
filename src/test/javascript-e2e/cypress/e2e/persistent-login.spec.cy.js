describe('Persistent Login Checkbox', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/discoveryLoginPage.html');
    });

    it('should set cookie when checkbox is checked', () => {
        cy.get('#is-persistent-login').check();
        cy.getCookie('isPersistent').should('have.property', 'value', 'yes');
    });

    it('should remove cookie when checkbox is unchecked', () => {
        cy.get('#is-persistent-login').check();
        cy.get('#is-persistent-login').uncheck();
        cy.getCookie('isPersistent').should('not.exist');
    });

});