# Constellation Common

## Why a common package?
Is good to have a decoupled java package from the Spigot plugin with the common features and actions, than i can reuse these in the BungeeCord, Discord Bot, MatchMaking(if needed), and other java programs.

## Responsibilities
* Abstract database pool connections and create a standard for repositories.
* Handle communication between services, using Redis message broker.
* Load/Edit users, take care of punishments, permissions,  all things related to the user that could be useful outside of the game.
* Define the HTTP requests for the APIs.

## Libraries
#### Base
* Java 8
* [Lombok](https://github.com/rzwitserloot/lombok) for boilerplate, because i never used, i thought it was a good opportunity to do so
#### Database
* [HikariCP](https://github.com/brettwooldridge/HikariCP) for connection pools
* [jOOQ](https://github.com/jOOQ/jOOQ) for query builder
* [FlyWay](https://github.com/flyway/flyway) for database migrations
#### Http Client
To be decided
#### Message Broker
To be decided

