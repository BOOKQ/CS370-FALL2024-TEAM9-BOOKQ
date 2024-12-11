# CS370-FALL2024-TEAM9-BOOKQ
## PROJECT BOOKQ

TEAM 9
MEMBERS: REECE HARRIS, BRIAN KING, OLUWATOBI BADEJO, JESSICA HERNANDEZ


# STEP1

install BookQ dependencies on your computer

## mac0S 
  - use brew -> brew install openjdk
  - use brew -> brew install mvn
  - use brew -> brew install mysql
## window (Must be in Powershell [Administrator])
  - download chocolatey
    
Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = `
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

  - install Maven -> choco install maven -y
  - install Jetty -> choco install jetty -y
  - install mysql -> choco install mysql -y
  - install JDK   -> choco install openjdk -y

## IMPORTANT 
### Before you run the below syntax ----> //Make sure you are in the *********SERVELET-EXAMPLE************ folder// before you perform next steps.

Access the database and run the sql script
-> mysql -u root -p -> to access the MySQL (mysql must have these credentials)

Run the below command
-> mysql> source ./db.sql;

# STEP3

1.  mvn clean install
2.  mvn clean package
3.  mvn jetty:run


once your server is running, access using 127.0.0.1:8080/login.html
