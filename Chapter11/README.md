# Cats Hostel - Ktor

This is an example application for managing cats written in Ktor framework

## Setup
You need PostgreSQL installed.
  
On OSX run:
```bash 
brew install postgresql@16
```

On Windows, follow those instructions:
https://www.postgresql.org/download/windows/

After you have PostgreSQL, run the following commands to set up your database:
```bash
createuser cats_admin -W -d
createdb cats_db -U cats_admin
psql -U cats_admin -c "create table if not exists cats(id bigserial primary key, name varchar(20) not null unique, age integer)" cats_db
```