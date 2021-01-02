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

function deployServer() {
  cd "$SCRIPT_DIR"
  kubectl apply -f server-kubernetes.yml
  prettyPrint "Server component deployment executed"
}

function deployClient() {
  cd "$SCRIPT_DIR"
  kubectl apply -f client-kubernetes.yml
  prettyPrint "Client component deployment executed"
}

############
### MAIN ###
############

deployServer
#deployClient
sleep 5s
prettyPrint "Kubernetes deployment executed.."
