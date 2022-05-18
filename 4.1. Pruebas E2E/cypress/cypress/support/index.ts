// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands';
import 'crypto-js';
import 'cypress-wait-until';
import 'cypress-xpath';
import './cypress-helpers/commands_tab_helpers';
import './cypress-helpers/frame_helpers';
import './custom-commands/home';
import 'cypress-mochawesome-reporter/register';

Cypress.on('window:before:load', (win) => {
    cy.spy(win.console, 'log').as('consoleLog');
  });


// Alternatively you can use CommonJS syntax:
/* require('./commands') */
