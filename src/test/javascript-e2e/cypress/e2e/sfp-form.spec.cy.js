describe('Test contact sfp-form', () => {




    beforeEach(() => {
        cy.viewport(1101, 1050);
        cy.visit('/cypress-test/contacts/sfp.html');
    })

    it('Check default config', () => {
        cy.get('#book').should('have.class', 'active');
        cy.get('#other').should('not.have.class', 'active');
        cy.get('#journal').should('not.have.class', 'active');
        cy.get('input[name=author]').should("be.visible");
        cy.get('input[name=title]').should("be.visible");
        cy.get('input[name=ISBN]').should("be.visible");
        cy.get('input[name=publisher]').should("be.visible");
    })


    it('Check after cliking on journal config', () => {
        cy.get('input[name="requestedBy.fullName"]').as('fullName').type('alain');
        cy.get('#journal').click();
        cy.get('#journal').should('have.class', 'active');
        cy.get('#other').should('not.have.class', 'active');
        cy.get('#book').should('not.have.class', 'active');
        cy.get('input[name=publisher]').should("be.visible");
        cy.get('input[name=ISBN]').should("be.visible");
        cy.get('input[name=author]').should("not.visible");
        cy.get('@fullName').should('have.value', 'alain');
    })


    it('Check after cliking on other config', () => {
        cy.get('input[name="requestedBy.fullName"]').as('fullName').type('alain');
        cy.get('#other').click();
        cy.get('#other').should('have.class', 'active');
        cy.get('#journal').should('not.have.class', 'active');
        cy.get('#book').should('not.have.class', 'active');
        cy.get('input[name=publisher]').should("not.visible");
        cy.get('input[name=ISBN]').should("not.visible");
        cy.get('input[name=author]').should("not.visible");
        cy.get('@fullName').should('have.value', 'alain');
    })


    it('Check submit config', () => {
        cy.intercept('POST', 'https://www.google-analytics.com/g/collect*',
            {
                statusCode: 204
            }).as('gaCollect');
        cy.intercept('POST', '/apps/lanelibacqs',
            (req) => {
                expect(req.body).to.contains('title=TEST');
                expect(req.body).to.not.contains('publisher');
                //to not send email but get some javascript for ga to work
                req.url = `https://lane.stanford.edu`;
                req.continue();
            }).as('submit');
        cy.get('input[name="requestedBy.fullName"]').type('alain');
        cy.get('input[name="requestedBy.email"]').type('alainb@stanford.edu');
        cy.get('input[name=title]').type('TEST');
        cy.get('#other').click();
        cy.get('#other').should('have.class', 'active');
        cy.get('button[type=submit]').click();
        cy.wait('@submit');
        cy.wait('@gaCollect');
    })
})