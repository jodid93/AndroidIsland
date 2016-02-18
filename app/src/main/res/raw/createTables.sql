PRAGMA foreign_keys = ON;

-- DEVELOPMENT COMMANDS --
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Gamestate;
DROP TABLE IF EXISTS Friends;
DROP TABLE IF EXISTS Pending;
---------------------------


CREATE TABLE Users
( 
	name	TEXT PRIMARY KEY
);


CREATE TABLE Gamestate
(
	username	TEXT,
	gamestate 	TEXT,
	FOREIGN KEY(username) REFERENCES artist(artistid)
);


CREATE TABLE Gamestate
(
	username	TEXT,
	gamestate 	TEXT,
	FOREIGN KEY(username) REFERENCES artist(artistid)
);