describe('Description Toggle', () => {

    it('should show/hide abstract content and report the clicks to GA', () => {

        cy.visit('/cypress-test/search.html?q=25195623&source=all-all');

        cy.window().then((win) => {
            cy.spy(win.L, 'fire').as('lanewebSpy');
        });

        cy.get('.descriptionTrigger').as('descriptionTrigger');
        cy.get('@descriptionTrigger').invoke('text').should('contain', 'Abstract');
        cy.get('.description').as('description');
        cy.get('@description').should('not.be.visible');

        // Perform both clicks to trigger the events
        cy.get('@descriptionTrigger').click();

        cy.get('@descriptionTrigger').click();


        cy.get('@lanewebSpy').then((spy) => {

            // Count calls with 'tracker:trackableEvent'
            const trackableEventCallCount = spy.getCalls().reduce((count, call) => {
                return call.args[0] === 'tracker:trackableEvent' ? count + 1 : count;
            }, 0);

            expect(trackableEventCallCount).to.equal(2);

            // Verify it was called with the specific arguments
            expect(spy).to.have.been.calledWith('tracker:trackableEvent', {
                category: 'lane:descriptionTrigger',
                action: 'Abstract',
                label: 'Musical rhythm discrimination explains individual differences in grammar skills in children.'
            });
        });



    });
});
