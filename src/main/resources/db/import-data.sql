CREATE TABLE IF NOT EXISTS client (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      firstname VARCHAR(255),
    lastname VARCHAR(255),
    company VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS client_contact (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              client_id INT,
                                              category VARCHAR(255),
    text TEXT,
    FOREIGN KEY(client_id) REFERENCES client(id),
    FOREIGN KEY(category) REFERENCES category(name)
    );


INSERT INTO category (name) VALUES ('LETTER');
INSERT INTO category (name) VALUES ('INVOICE');
INSERT INTO category (name) VALUES ('PHONE');


INSERT INTO client (firstname, lastname, company) VALUES ('John', 'Doe', 'Example Corp');
INSERT INTO client (firstname, lastname, company) VALUES ('Jane', 'Smith', 'Another Corp');
INSERT INTO client (firstname, lastname, company) VALUES ('Alice', 'Johnson', 'Tech Co');
INSERT INTO client (firstname, lastname, company) VALUES ('Bob', 'Brown', 'Innovate Ltd');
INSERT INTO client (firstname, lastname, company) VALUES ('Charlie', 'Davis', 'Future Inc');


INSERT INTO client_contact (client_id, category, text) VALUES (1, 'LETTER', 'Letter contact details');
INSERT INTO client_contact (client_id, category, text) VALUES (2, 'INVOICE', 'Invoice contact details');
INSERT INTO client_contact (client_id, category, text) VALUES (3, 'PHONE', 'Phone contact details');
