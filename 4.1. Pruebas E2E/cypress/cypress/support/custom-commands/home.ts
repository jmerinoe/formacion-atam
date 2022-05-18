Cypress.Commands.add('closeCookiesBanner', () => {
  cy.get('div.cookie-notice-container').should('be.visible');
  cy.get('#cn-close-notice').click();
  cy.get('div.cookie-notice-container').should('not.be.visible');
});

Cypress.Commands.add('loadMoreNews', () => {
  cy.intercept('POST', '/wp-content/themes/atam/template-parts/blog-ajax.php', {
    fixture: 'response.data'
  });
  
  cy.get('button[name="button"]')
    .click()
    .invoke('attr', 'data-enabled')
    .should('eq', 'true');
});

Cypress.Commands.add('loadMoreNewsRecursive', () => {
  cy.numArticlesDisplayed().then(initialArticles => {
    cy.get('button[name="button"]')
    .click()
    .invoke('attr', 'data-enabled')
    .should('eq', 'true');
    cy.numArticlesDisplayed().then(afterArticlesNumber => {
      if (afterArticlesNumber === initialArticles + 6){
        cy.log('Se muestran ' + afterArticlesNumber + ' noticias');
        cy.loadMoreNewsRecursive();
      } else{
        cy.get('button[name="button"]').click();
        cy.get('button[name="button"]').should('not.exist');
      }
    })
  })
});

Cypress.Commands.add('numArticlesDisplayed', () => {
  cy.get('body').then($body => {
    return $body.find('article').length;
  });
});