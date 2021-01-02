#!/usr/bin/env bash

# exit script on failure
set -e

############
### VARS ###
############

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)

#################
### FUNCTIONS ###
#################

function prettyPrint() {
  echo "=== $1 ==="
  echo ""
}

function waitPrint() {
  echo "~~~ $1 "
}

function deployServer() {
  cd "$SCRIPT_DIR"
  kubectl apply -f server-kubernetes.yml
  prettyPrint "Server component deployment executed"
}

function deployClient() {
  cd "$SCRIPT_DIR"
  kubectl apply -f client-kubernetes.yml
  waitPrint "Expose client as service..."
  sleep 5s
  kubectl expose deployment grpc-client --type=LoadBalancer --name=grpc-client
  prettyPrint "Client component deployment executed"
}

############
### MAIN ###
############

deployServer
waitPrint "Deploying client..."
sleep 5s
deployClient
prettyPrint "Kubernetes deployment executed.."
