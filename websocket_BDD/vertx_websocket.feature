Feature: Vert.x WebSocket Testing

  Scenario: Connect to Vert.x WebSocket and send/receive a message
    Given a Vert.x WebSocket server is running on "ws://localhost:8080/socket"
    When the client sends the message "Hello, Vert.x WebSocket!"
    Then the server should respond with the message "Message received: Hello, Vert.x WebSocket!"
    Then Close the server