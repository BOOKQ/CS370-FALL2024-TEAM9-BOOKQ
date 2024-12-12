# CS370-FALL2024-TEAM9-BOOKQ
## PROJECT BOOKQ

Team 9
Members: Reece Harris, Brian King, Oluwatobi Badejo, Jessica Hernandez 


# STEP1

Install BookQ dependencies on your computer

## Mac0S 
  - use brew -> brew install openjdk
  - use brew -> brew install mvn
  - use brew -> brew install mysql
## Windows (Must be in Powershell [Administrator])
  - download chocolatey (run below command)
    
Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = `
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

  - install Maven -> choco install maven -y
  - install Jetty -> choco install jetty -y
  - install mysql -> choco install mysql -y
  - install JDK   -> choco install openjdk -y

## [Important]
### Before you run the below syntax, make sure you are in the following directory
/[Main Project Folder]/CODE/servlet-example

Access the database<br>
(mysql must have these credentials)<br>
-> mysql -u root -p 

Run the below command<br>
-> mysql> source ./db.sql;

# STEP3

1.  mvn clean install
2.  mvn clean package
3.  mvn jetty:run


once your server is running, access using localhost:8080/login.html


# Demo

https://github.com/user-attachments/assets/1e3eb16d-25eb-48f8-93fd-ff2abafb4fa2


