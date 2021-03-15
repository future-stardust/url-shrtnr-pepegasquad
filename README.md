# Pepega Squad - URL shortener 🐸

This is a university project for "Software testing" course labs made by team **Pepega Squad**.

## Students group

- Федоряченко Ярослав `yar.fed99@gmail.com`
- Феофанов Іван `vanya6677@gmail.com`
- Гуменюк Олександр `sanya190900@gmail.com`

## Design document

The [design document] that describes architecture and implementation details of this project.

### System structure

There are four modules:
- `auth` **authentication module** - creates new users, authenticates existing ones
- `bigtable` - **big table** - a key-value persistence storage (please, pay attention that you should implement it by
  yourself. It means that it is not allowed to use data bases, another key-value storages 
  implementation, etc)
- `logic` - **business logic** - logic of URL shortening
- `rest` - **REST API** - a module that provides a REST API. [Micronaut] framework is already added
  to project dependencies. It simplifies creation of REST API and provides built-in JWT 
  authentication.
#### Main scenario endpoints

1. Sign up

```shell
curl --location --request POST 'http://localhost:8080/users/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email" : "alexandr@gmail.com",
    "password" : "myPassword"
}'
```

2. Login

```shell
curl --location --request POST 'http://localhost:8080/users/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email" : "alexandr@gmail.com",
    "password" : "myPassword"
}'
```

3. Shorten URL

```shell
curl --location --request POST 'http://localhost:8080/urls/shorten' \
--header 'Authorization: Bearer <TOKEN FROM THE LOGIN RESPONSE>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "url" : "https://www.youtube.com/feed/subscriptions"
}'
```

4. Redirect

```shell
curl --location --request GET 'http://localhost:8080/r/b' \
--data-raw ''
```
## Environment prerequisites

### Java
This is a Java project, so you will need an environment with installed [JDK] 15. For installation, 
you could use:
- [sdkman] on Linux/MacOS 
- [AdoptOpenJDK] on Windows

### IDE  
As IDE use [IntelliJ Idea Edu].

### Checkstyle
We use [checkstyle] to ensure coding standards. To get real-time detection in IDE you could use [Checkstyle-IDEA] 
plugin. We use Google rules (local copy `./config/checkstyle/checkstyle.xml`).

## How to start development

1. Clone this repo
2. Open the project directory in IntelliJ Idea Edu
3. Configure IDE code style settings
  
    1. Open `Settings`
    2. Go to `Editor` -> `Code Style` -> `Import Scheme`
       ![Settings screenshot](./media/code-style-import.png)
    3. Import scheme from `./config/idea/intellij-java-google-style.xml`

## Commit messages

Write commit messages accordingly by [7 rules of good commit messages].
  
[JDK]: https://en.wikipedia.org/wiki/Java_Development_Kit
[IntelliJ Idea Edu]: https://www.jetbrains.com/idea-edu/
[sdkman]: https://sdkman.io/
[AdoptOpenJDK]: https://adoptopenjdk.net/
[7 rules of good commit messages]: https://chris.beams.io/posts/git-commit/#seven-rules
[Micronaut]: https://micronaut.io/
[checkstyle]: https://checkstyle.org/
[Checkstyle-IDEA]: https://plugins.jetbrains.com/plugin/1065-checkstyle-idea
[design document]: https://docs.google.com/document/d/1RLWUjbNFWLdV0cQyk8X_D1ySTUcvOPi4ituxAmTkd5A/edit#heading=h.k57aj13un2t
