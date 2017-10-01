# REST sentence generator
Web application exposing REST API acting as a generator of the sentences from the words.

Check out the project using a Git clone command:

```
git clone https://github.com/mverbovsky/gd-sentence-generator.git
```

## Build Instructions
Project is tested to build on Java 8. It uses Maven as its build system
and should run on Maven 3.0 and above.

## Run Instructions
Application needs running MongoDB on port 27017. If you don`t have any, use Docker image for example.

```bash
$ docker pull mongo
$ docker run --rm -p 27017:27017 --name sentence-generator-db mongo
``` 

For testing is used embedded MongoDB.

