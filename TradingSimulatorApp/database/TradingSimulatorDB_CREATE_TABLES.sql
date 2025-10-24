START TRANSACTION;

DROP TABLE IF EXISTS trade;
DROP TABLE IF EXISTS user_profile;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id serial NOT NULL,
	username VARCHAR (50) NOT NULL UNIQUE,
	password_hash VARCHAR (200) NOT NULL,
	role VARCHAR (50) NOT NULL,
	CONSTRAINT PK_users PRIMARY KEY (id)
);

CREATE TABLE user_profile (
	id serial NOT NULL,
	user_id int NOT NULL,
	name VARCHAR (50) NOT NULL,
	phone_number VARCHAR (20) NOT NULL,
	address VARCHAR (50) NOT NULL,
	city VARCHAR (20) NOT NULL,
	state VARCHAR (2) NOT NULL,
	zip_code VARCHAR (5) NOT NULL,
	avatar_url VARCHAR DEFAULT 'https://www.strasys.uk/wp-content/uploads/2022/02/Depositphotos_484354208_S.jpg',
	CONSTRAINT PK_user_profile PRIMARY KEY (id),
	CONSTRAINT FK_user_profile_users FOREIGN KEY (user_id) REFERENCES users (id),
	CONSTRAINT UQ_users_profile UNIQUE (user_id)
);

CREATE TABLE account (
	id serial NOT NULL,
	user_id int NOT NULL,
	balance DECIMAL (15,2) NOT NULL DEFAULT 10000.00,
	CONSTRAINT PK_account PRIMARY KEY (id),
	CONSTRAINT FK_account_users FOREIGN KEY (user_id) REFERENCES users (id),
	CONSTRAINT UQ_account UNIQUE (user_id)
);

CREATE TABLE trade (
	id serial NOT NULL,
	user_id int NOT NULL,
	symbol VARCHAR (15) NOT NULL,
	trade_type VARCHAR (5) NOT NULL,
	quantity int NOT NULL,
	price DECIMAL (10,2) NOT NULL,
	trade_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PK_trade PRIMARY KEY (id),
	CONSTRAINT FK_trade_users FOREIGN KEY (user_id) REFERENCES users (id)
);

COMMIT TRANSACTION;