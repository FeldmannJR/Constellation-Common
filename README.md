# Constellation Common

## Why a common package?
Is good to have a decoupled java package from the Spigot plugin with the common features and actions, than i can reuse these in the BungeeCord, Discord Bot, MatchMaking(if needed), and other java programs.

# Responsibilities
* Abstract database pool connections and create a standard for repositories.
* Handle communication between services, using Redis message broker.
* Load/Edit users, take care of punishments, permissions,  all things related to the user that could be useful outside of the game.
* Define the HTTP requests for the APIs.