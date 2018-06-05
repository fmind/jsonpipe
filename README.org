# JSON Pipe

A small project that demonstrates how to process jsonlines files with Clojure.

The project requires cheshire and core.async librairies to parse the file in parallel.

## Usage

With boot:

```clojure
cat episodes/got.jsonlines | boot run
```

With java:

```clojure
cat episodes/got.jsonlines | java -jar target/jsonpipe.jar
```

Feel free to use any jsonlines files in the episodes folder.

Note that most file contains errors to demonstrate exception handling.
