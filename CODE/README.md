*****STEP1*******

install maven on your computer

mac0S - use brew -> brew install mvn
window - download maven zip, unzip and add to your path via system environemntal variables or path

****STEP2*******
install mysql on your computer

mac00S - use brew -> brew install mysql
window - download use choco or download mysql binary and add to your environment varibales or path

Access the database and create a database
-> mysql -u root -p -> to access the MySQL
-> create database bookq;


follow this steps 
--> load into your database

- create a table in the databse based on how the sql is set up

- CREATE TABLE book ( book_id INT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each book title VARCHAR(255) NOT NULL,  -- Title of the book isbn13 CHAR(13) UNIQUE,  -- 13-character ISBN, unique to each book language_id INT NOT NULL,  -- Reference to the language of the book num_pages INT,  -- Number of pages in the book publication_date DATE,  -- Publication date of the book publisher_id INT  -- Reference to the publisher );
- source /path/where/you/saved/your.sql;

*******STEP3*********

Before you run the below syntax ----> //Make sure you are in the *********SERVELET-EXAMPLE************ folder// before you perform next steps.

1.  mvn clean install
2.  mvn clean package
3.  mvn jetty:run


once your server is running, access using 127.0.0.1:8080/login.html
