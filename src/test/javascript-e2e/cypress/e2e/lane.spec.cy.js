describe('Persistent Login Checkbox', () => {
    beforeEach(() => {
        cy.visit('/cypress-test/discoveryLoginPage.html');
    });

    it('should set cookie', () => {
        cy.window().then(win => {
            win.L.Cookie.set('test', 'testValue');
        });
        cy.getCookie('test').should('have.property', 'value', 'testValue');

    });
    it('should get cookie value', () => {
        cy.window().then(win => {
            win.L.Cookie.set('test', 'testValue');
            cy.window().its('L.Cookie').invoke('get', 'test').should('equal', 'testValue');
        });
    });


    it('should remove cookie value', () => {
        cy.window().then(win => {
            win.L.Cookie.set('test', 'testValue');
            win.L.Cookie.remove('test');
        });
        cy.getCookie('test').should('not.exist');
    });
});