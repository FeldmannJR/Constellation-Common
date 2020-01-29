#!/bin/sh

docker run -it -d \
  --rm \
  --name=constellation_db \
  -p 3306:3306 \
  -e MARIADB_ROOT_PASSWORD="senha123" \
  -e MARIADB_DATABASE="constellation_common" \
  bitnami/mariadb
