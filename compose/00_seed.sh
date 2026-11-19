#!/bin/bash
set -e

cd /sql
psql -v ON_ERROR_STOP=1 -U postgres -d postgres -f 00_install.sql
