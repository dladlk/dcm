#!/bin/bash -ex
CURDIR=`dirname $0`
helm tiller run helm upgrade dcm --install --force ${CURDIR}/dcm/ --values ${CURDIR}/install-dcm-values.yaml 
#--dry-run --debug
