# Tic tac toe test application

## Requirements

1. The application must be developed in the JVM language.
2. Any technology of the developer's choice can be used.
3. The application should be developed with best practices for writing code: formatting, testing, comments as needed.
4. The connection between instances can be broken and re-established at any time.
5. The application must have an interface (REST or HTML) that allows the user to retrieve the state of the playing field at any time.
6. At any time, both instances must show the same state of the playing field or explicitly indicate that the state is inconsistent, regardless of the state of the connection between the instances.
7. A delay must be provided so that instances' moves can be tracked.
8. The application must make moves according to the rules.
9. The application must not allow another instance to make moves not according to the rules.
10. You can choose any other turn-based game with 2 or more players: naval combat, chess, sticks (each player takes 1,2,3 sticks from the pile, whoever takes the last one loses).
11. The algorithm of the game is not important, you can use random strategy. What is important is how synchronization between instances takes place.

## How to run

1. Build maven project with command `mvn clean package`
2. Start two instances of command line from `target` directory
3. Start instances of application in each command line by executing `java -jar tictactoe-0.0.1-SNAPSHOT.jar`
4. Wait till spring boot starts correctly. Application will choose random port, it's possible to check it from log ![example of port](/assets/images/port.png)
5. Choose one of application(this instance will be a client) and type port of another application(this will be a host).
6. Client can be killed and another instance of client can reconnect to host: start new application and enter port number of host. Host will share game state and game will continue
7. State of field is possible to retrieve at any time of game by executing rest call `http://localhost:{port}/state`
