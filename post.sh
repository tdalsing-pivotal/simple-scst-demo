#!/bin/bash

post() {
  echo "Sending $1"
  curl -X POST -H "Content-Type: application/json" -d "$1" http://localhost:8104/publish
}

post '{"id":"id-1","name":"name-1","count":1,"amount":11.1,"childObject":{"description":"desc-1"}}'
post '{"id":"id-2","name":"name-2","count":2,"amount":22.2,"childObject":{"description":"desc-2"}}'
post '{"id":"id-3","name":"name-3","count":3,"amount":33.3,"childObject":{"description":"desc-3"}}'
post '{"id":"id-4","name":"name-4","count":4,"amount":44.4,"childObject":{"description":"desc-4"}}'
post '{"id":"id-5","name":"name-5","count":5,"amount":55.5,"childObject":{"description":"desc-5"}}'
post '{"id":"id-6","name":"name-6","count":6,"amount":66.6,"childObject":{"description":"desc-6"}}'
post '{"id":"id-7","name":"name-7","count":7,"amount":77.7,"childObject":{"description":"desc-7"}}'
post '{"id":"id-8","name":"name-8","count":8,"amount":88.8,"childObject":{"description":"desc-8"}}'
post '{"id":"id-9","name":"name-9","count":9,"amount":99.9,"childObject":{"description":"desc-9"}}'
post '{"id":"id-10","name":"name-10","count":10,"amount":110.10,"childObject":{"description":"desc-10"}}'