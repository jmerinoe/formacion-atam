/// <reference types="cypress" />
// ***********************************************************
// This example plugins/index.js can be used to load plugins
//
// You can change the location of this file or turn off loading
// the plugins file with the 'pluginsFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/plugins-guide
// ***********************************************************

// This function is called when a project is opened or re-opened (e.g. due to
// the project's config changing)

/**
 * @type {Cypress.PluginConfig}
 */

const resultsFile = "./cypress/configE2E/aiotests/testResults.json"
const configFile = "./cypress/configE2E/configE2ETests.json"

const { beforeRunHook, afterRunHook } = require('cypress-mochawesome-reporter/lib');
const exec = require('child_process').execSync;

module.exports = (on, config) => {
  config.baseUrl = config.env.environmentUrl || 'https://www.atam.es';

  on('before:run', async (details) => {
    console.log('override before:run');
    await beforeRunHook(details);
  });

  on('after:run', async () => {
    console.log('override after:run');
    await afterRunHook();
  });

  on('task', {
    writeResult(obj) {
      var fs = require("fs")
      var fileObj
      fs.readFile(resultsFile, "utf-8", function readFileCallback(err, data) {
        if (err) {
          console.log(err)
          fileObj = obj
        } else {
          fileObj = JSON.parse(data)
          fileObj.push(obj)
        }
        fs.writeFile(resultsFile, JSON.stringify(fileObj), "utf-8");
      })
      return null
    }
  })

  on('task', {
    setupConfigFiles(obj) {
      var fs = require("fs")
      fs.writeFile(resultsFile, '[]', { flag: 'w' }, function (err) {
          if (err) throw err;
        });
      
      if (!fs.existsSync(configFile)) {
        fs.writeFileSync(configFile, JSON.stringify({}))
      }
      
      return null
    }
  })

  // IMPORTANT return the updated config object
  return config;
}