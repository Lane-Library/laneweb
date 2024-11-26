describe('Slideshow functionality', () => {
	beforeEach(() => {
		cy.visit('/index.html');
	});

	it('should load images when in viewport', () => {
		// not in viewport
		cy.get('#slide-show .slide img').each(($img) => {
			cy.wrap($img).should('have.attr', 'src').and('not.be.empty');
			cy.wrap($img).should('have.attr', 'src').and('include', 'unknown-staff.svg');
		});
		// in viewport
		cy.get('#slide-show').scrollIntoView();
		cy.get('#slide-show .slide img').each(($img) => {
			cy.wrap($img).should('have.attr', 'src').and('not.be.empty');
			cy.wrap($img).should('have.attr', 'src').and('not.include', 'unknown-staff.svg');
		});
	});

	it('should disable previous button on first slide', () => {
		cy.get('#previous-slide').should('have.class', 'disable');
	});

	it('should navigate to next slide on next button click', () => {
		cy.get('#next-slide').click();
		cy.get('#slide-show .slide').first().should('have.class', 'desactive-next');
	});

	it('should disable next button on last slide', () => {
		cy.get('#next-slide').should('be.visible');
		cy.get('#next-slide').then(($nextSlide) => {
			while ($nextSlide.is(':visible')) {
				console.log('clicking next slide');
				$nextSlide.click();
			}
		});
		cy.get('#next-slide').should('have.class', 'disable');

	});

	it('should navigate to previous slide on previous button click', () => {
		cy.get('#next-slide').click();
		cy.get('#previous-slide').click();
		cy.get('#slide-show .slide').first().should('not.have.class', 'desactive-next');
	});

	it('should recalculate displayed images on resize', () => {
		// mobile view
		cy.viewport(320, 480);
		// only the first slide should be displayed
		cy.get('#slide-show .slide').first().should('be.visible');
		// all other slides should be hidden
		cy.get('#slide-show .slide').not(':first').should('not.be.visible');
		// desktop view
		cy.viewport(1280, 720);
		// at least the first 3 slides should be visible
		cy.get('#slide-show .slide:nth-child(-n+3)').each(($el) => {
			expect($el).to.be.visible;
		})
		// at least the last 5 slides should be hidden
		cy.get('#slide-show .slide:nth-last-child(-n+5)').each(($el) => {
			expect($el).to.not.be.visible;
		})
	});
});