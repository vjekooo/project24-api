#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
cd $SCRIPT_DIR

# HOST=ec2-11111111111.compute-1.amazonaws.com
# PORT=111111
#HOST=db-dev-01.test.us-east-1.rds.amazonaws.com
#PORT=5432

USER=${DB_USERNAME}
DB=${DB_NAME}
SCHEMA=public
PORT=5432

#pg_dump -h $HOST -p $PORT -n $SCHEMA -U $USER $DB | gzip - >${DB}_${SCHEMA}.sql.gz
echo "Db backup started..."
pg_dump -p $PORT -n $SCHEMA -U $USER $DB | gzip - > ${DB}_${SCHEMA}.sql.gz
echo "Db backup done."
