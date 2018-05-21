#!/bin/bash

# export $(cat .credentials | grep -o '^[^#]*' | xargs)
source <(sed -E -n 's/[^#]+/export &/ p' ./.credentials)
exec c8y deploy:app docker
