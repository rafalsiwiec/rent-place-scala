
Example Play scala application
===================================

## Running
> sbt run
Then go to browser "http://localhost:9000" and apply evolution to create schema.

## Modules

Data is persisted in H2 database.
This is a special engine capable of emulating different databases, best for testing and prototyping.

### User Management module
Available under address "http://localhost:9000/users/list"
Allows to add/delete/confirm users.
Every created user is not activated.
You need to create activation secret key and use it to activate given user.

This module shows how to use Anorm with raw SQL queries in Play application.
Queries used are SELECT / UPDATE / DELETE.

Also shows how to map Form parameters into standard tuple or complex Object.
How to create Form with helper utils and display failed alidation.

