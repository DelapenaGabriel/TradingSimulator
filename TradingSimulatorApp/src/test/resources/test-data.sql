BEGIN TRANSACTION;

-- Users
INSERT INTO users (username, password_hash, role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('user3','user3','ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('user4','user4','ROLE_USER');

INSERT INTO user_profile (user_id, name, phone_number, address, city, state, zip_code) VALUES
(1, 'user profile 1', '1231', 'Example Address 1', 'city', 'ST', '00000'),
(2, 'user profile 2', '1232', 'Example Address 2', 'city', 'ST', '00000'),
(3, 'user profile 3', '1233', 'Example Address 3', 'city', 'ST', '00000');

INSERT INTO account (user_id, balance) VALUES
(1,'10000.00'),
(2,'20000.00');

INSERT INTO trade (user_id, symbol, trade_type, quantity, price, trade_date) VALUES
(1, 'TSLA', 'BUY' , 10, '10.00', '2007-12-03T10:15:30'),
(1, 'TSLA', 'SELL' , 10, '30.00', '2007-12-03T10:15:30'),
(2, 'AAPL', 'BUY' , 10, '100.00', '2007-12-03T10:15:30');

COMMIT TRANSACTION;
