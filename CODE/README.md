# CS370-FALL2024-TEAM9-BOOKQ

TEAM 9
PROJECT BOOKQ



MEMBERS: REECE HARRIS, BRIAN KING, OLUWATOBI BADEJO, JESSICA HERNANDEZ

# *****STEP1*******

install maven on your computer

# mac0S 
use brew -> brew install mvn
# window 
 download maven zip, unzip and add to your path via system environemntal variables or path

# ****STEP2*******
install mysql on your computer

# macOS 
use brew -> brew install mysql
# window 
download use choco or download mysql binary and add to your environment varibales or path

# Access the database and create a database
-  mysql -u root -p -> to access the MySQL (mysql must have these credentials)
-   create database bookq;
-   use bookq;


# LOAD THE SAMPLE DATA LOCATED HERE /CODE/insert_books.sql INTO THE DATABASE 
DOWNLOAD THE INSERT_BOOKS.SQL TO YOUR DOWNLOADS FOLDER
- mysql -u root -p
-  Enter Password
-  use bookq;
-  source ~/downloads/insert_books.sql;

# *******STEP3*********

Before you run the below syntax ----> //Make sure you are in the *********SERVELET-EXAMPLE************ folder// before you perform next steps.

1.  mvn clean install
2.  mvn clean package
3.  mvn jetty:run


once your server is running, access using 127.0.0.1:8080/login.html
