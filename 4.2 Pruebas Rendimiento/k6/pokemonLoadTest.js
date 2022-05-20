import http from 'k6/http';
import { sleep, check } from 'k6';
import { SharedArray } from 'k6/data';
import exec from 'k6/execution';
import { Counter } from 'k6/metrics';

const testData = JSON.parse(open('./pokemonData.json'));
const pokemons = new SharedArray('pokemons', function () {
    return testData;
});

// A simple counter for http requests
export const requests = new Counter('http_reqs');

// you can specify stages of your test (ramp up/down patterns) through the options object
// target is the number of VUs you are aiming for

export const options = {
  /*
  ext: {
        loadimpact: {
          projectID: 3583318,
          // Test runs with the same name groups test runs together
          name: "PokeApi Test"
        }
  },
  */
  stages: [
    { target: 10, duration: '1m' },
    { target: 20, duration: '30s' },
    { target: 30, duration: '30s' },
    { target: 40, duration: '30s' },
    { target: 30, duration: '30s' },
    { target: 20, duration: '30s' },
    { target: 10, duration: '30s' }
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'], // http errors should be less than 1%
    http_req_duration: ['p(90)<500'], // 90% of requests should be below 200ms
  },
};

export default function () {
  // our HTTP request, note that we are saving the response to res, which can be accessed later
  const pokemonIndex = exec.scenario.iterationInTest % (pokemons.length);
  const pokemonID = pokemons[pokemonIndex].id;
  const res = http.get('https://pokeapi.co/api/v2/pokemon/' + pokemonID);

  sleep(1);

  const checkRes = check(res, {
    'status is 200': (r) => r.status === 200,
    'response body': (r) => r.body.length > 0,
  });
}
