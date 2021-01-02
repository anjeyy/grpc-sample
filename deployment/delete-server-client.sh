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

function deleteServiceAndDeployment() {
  cd "$SCRIPT_DIR"
  kubectl delete services "$1"
  kubectl delete deployments "$1"
  prettyPrint "[$1] component removed"
}

############
### MAIN ###
############

deleteServiceAndDeployment grpc-server
deleteServiceAndDeployment grpc-client
prettyPrint "Kubernetes deployment removed.."
