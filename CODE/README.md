First install maven on your computer

mac0S - use brew -> brew install mvn
window - download maven zip, unzip and add to your path via system environemntal variables or path


steps to run //Make sure you are in the servelet-example folder//

1.  mvn clean install
2.  mvn clean package
3.  mvn jetty:run


once your server is running, access using 127.0.0.1:8080/login.html
