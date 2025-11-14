describe('Popup functionality', () => {

    it('should open a local popup from HTML found on page', () => {
        cy.visit('/cypress-test/patron-registration/index.html');

        // popup should not be present on the page initially
        cy.get('.popup').should('not.exist');

        cy.get('a[rel="popup local popup-id-shc"]').click();

        cy.get('.popup').should('exist');

        // verify popup is draggable
        cy.get('.popup').should('have.attr', 'draggable', 'true');

        cy.get('.popup .fa-close').click();

        // verify popup is closed
        cy.get('.popup').should('not.exist');

    });

    it('should open a new window with the correct URL; expect focus to be called on new window', () => {
        cy.visit('/cypress-test/biomed-resources/bassett/bassettView.html?bn=132-5');
        // test will fail unless we stub window.open and window.focus
        cy.window().then((win) => {
            cy.stub(win, 'open').as('windowOpen');
        });
        cy.get('@windowOpen').then((stub) => {
            const newWindow = { focus: cy.stub() };
            stub.returns(newWindow);
        });
        cy.get('#larger-view-link a').click();
        cy.get('@windowOpen')
            .should('be.calledWithMatch', /biomed-resources\/bassett\/raw\/bassettLargerView.html\?t=largerView&bn=132-5/,
                'newWin',
                'resizable,toolbar=no,location=no,scrollbars=yes,width=1320,height=1925');
    });

    it('should move the popup when dragged and dropped', () => {
        cy.viewport(1101, 1000);

        cy.visit('/cypress-test/patron-registration/index.html');

        cy.get('a[rel="popup local popup-id-shc"]').click();

        cy.get('.popup').should('be.visible').invoke('position').then((initialPosition) => {
            // `initialPosition` will be an object like { top: 123, left: 456 }

            console.log('Initial popup position:', initialPosition);

            // Define our drag-and-drop coordinates
            // Where the user's cursor "clicks" to start the drag
            const dragStartCoords = {
                x: initialPosition.left + 20, // 20px inside the popup
                y: initialPosition.top + 10,   // 10px from the top of the popup
            };

            // How far the user's cursor moves during the drag
            const dragMovement = { x: -600, y: 150 };

            // Where the user's cursor will be when the drop happens
            const dropCoords = {
                x: dragStartCoords.x + dragMovement.x,
                y: dragStartCoords.y + dragMovement.y,
            };

            // The DataTransfer object is required for drag-and-drop events to work
            const dataTransfer = new DataTransfer();

            // Simulate the drag and drop
            cy.get('.popup').trigger('dragstart', {
                force: true,
                clientX: dragStartCoords.x,
                clientY: dragStartCoords.y,
                dataTransfer,
            });

            // The drop listener is on the document, so triggering on the body is effective
            cy.get('body').trigger('drop', {
                force: true,
                clientX: dropCoords.x,
                clientY: dropCoords.y,
                dataTransfer
            });

            // Calculate the expected final position and verify
            // The component's logic is: finalPos = dropPos - (dragStartPos - initialPos)
            // This simplifies to: finalPos = initialPos + (dropPos - dragStartPos)
            // Which is: finalPos = initialPos + dragMovement
            // Need to round because position values can be fractional
            const expectedFinalPosition = {
                left: Math.round(initialPosition.left + dragMovement.x),
                top: Math.round(initialPosition.top + dragMovement.y),
            };

            console.log('Expected final popup position:', expectedFinalPosition);

            // Assert that the popup has moved to the correct new location
            cy.get('.popup')
                .should('have.css', 'left', `${expectedFinalPosition.left}px`)
                .and('have.css', 'top', `${expectedFinalPosition.top}px`);
        });
    });

});