START TRANSACTION;

INSERT INTO users (username, password_hash, role)
VALUES ('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER'),
    ('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_ADMIN');


INSERT INTO user_profile (user_id, name, phone_number, address, city, state, zip_code) VALUES
(1, 'user profile 1', '1231', 'Example Address 1', 'city', 'ST', '00000'),
(2, 'admin profile 2', '1232', 'Example Address 2', 'city', 'ST', '00000');

INSERT INTO account (user_id, balance) VALUES 
(1,'10000.00'),
(2,'20000.00');

INSERT INTO trade (user_id, symbol, trade_type, quantity, price, trade_date) VALUES 
(1, 'TSLA', 'BUY' , 10, '10.00', CURRENT_TIMESTAMP),
(1, 'TSLA', 'SELL' , 10, '30.00', CURRENT_TIMESTAMP);

COMMIT TRANSACTION;
   
