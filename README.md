
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

### Flat Management module
Available under address "http://localhost:9000/flats"
Single html page fetching data by JSON calls to REST service.

Fields are editable and after any change a data is persisted on server.
Changing Id shows how failure is handled.

Two approaches to parsing JSON used.

### Rent Management module
Available under address "http://localhost:9000/rents/list"
ORM used is Slick. Simple presentation of CRUD operations on data.
Database schema automatically generated but manually merged with the one from Anorm.
Form in view contains data picker and a checkbox.
Bootstrap theme integrated.

### Chat module
Visit links in 2 windows:
"http://localhost:9000/room/tomek"
"http://localhost:9000/room/jacek"
Make interesting conversation :)
WebSockets with Iteratee concept used to handle chat interaction.
Single Actor from Akka to handle chat state.

