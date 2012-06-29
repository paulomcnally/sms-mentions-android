--
-- Create Tables
--
CREATE TABLE tblFriends ( 
	uid INTEGER NOT NULL PRIMARY KEY,
	name TEXT NOT NULL
	);


CREATE TABLE tblConfig (
	config_id INTEGER NOT NULL PRIMARY KEY,
	config_key TEXT NOT NULL,
	config_value TEXT NOT NULL
	);


--
-- Insert Rows
--

INSERT INTO tblConfig VALUES (
	null,
	'shortcode_movistar',
	'32665'
	);

INSERT INTO tblConfig VALUES (
	null,
	'shortcode_claro',
	'32665'
	);