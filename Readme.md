WeatherCurrency
==============

Отображение температуры и курса валют.

Для запуска
========

[Maven](https://maven.apache.org/)
[MongoDB](https://www.mongodb.com/)

Запуск
========

Компилировать проект ```mvn install```.
Запустить приложение можно через ```mvn jetty:run```, оно расположится на http://localhost:8080/ .

Чтобы создать WAR используйте ```mvn clean package```.
Запустить WAR файл можно через ```mvn jetty:run-war```.

В файле ```/resources/META-INF/apache-deltaspike.properties``` находится API ключ http://openweathermap.org (мой указан пока что для удобства).

Лог файл находится в ```/logs/tzlogger.log```

Автор
========
Дондоков Бэликто - [Email](mailto:zulek24@gmail.com) [LinkedIn](https://www.linkedin.com/in/belikto-dondokov/)
