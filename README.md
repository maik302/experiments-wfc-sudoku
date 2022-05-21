# Sudoku WFC

A very basic implementation of the _Wave Function Collapse_ algorithm for solving a Sudoku board.

It is based on [this video](https://youtu.be/2SuvO4Gi7uY) by [Martin Donald](https://twitter.com/bolddunkley).

---

In this first approach, when a _contradiction_ is met during the _sudoku-solving phase_, the algorithm restarts itself until no contradiction happens. Maybe, in the future, a proper way to prevent or recover from a contradiction would be nice to try.

The goal for this experiment is to get to know how the basic structure of a WFC model works before using it as mechanism for creating procedural content.