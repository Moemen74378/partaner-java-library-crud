# Library Desk

A small Java CRUD application for the Git and GitHub pair assignment. Add books, read the catalogue, edit book details and availability, and delete records. Data is stored in a local H2 database and survives a restart.

## Run

Install a JDK (Java 17 or later). Git and an internet connection are needed for the first build. Maven is downloaded automatically by the included official Maven Wrapper.

Windows PowerShell, inside this folder:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS / Linux:

```sh
chmod +x mvnw
./mvnw spring-boot:run
```

Open **http://localhost:8080/books**. Stop the application with **Ctrl+C** in its terminal. If Java is not on PATH, set `JAVA_HOME` to your JDK folder (the folder containing `bin/java`) and add its `bin` directory to PATH, or run the application from IntelliJ IDEA using its configured JDK. In PowerShell, the PATH command is `$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"`.

The application binds to this computer only. It is a classroom application for local use.

## Build and test

```powershell
.\mvnw.cmd clean verify
java -jar target/library-desk-1.0.0.jar
```

On macOS/Linux replace `.\mvnw.cmd` with `./mvnw`. Run from the project folder so the database stays in the same `data/` directory. Each student uses their own database. Never commit `data/` or `target/`.

## Try the CRUD operations

1. **Create:** click **Add book**, enter a title, author, publication year, and status, and save.
2. **Read:** the catalogue shows the saved book and updated summary counts.
3. **Update:** click **Edit**, change the status to Borrowed or update a detail, and save.
4. **Delete:** click **Delete** and confirm. The row disappears and the counts change.

Try an empty title or a year outside 1–2100 to see validation. Missing record URLs return HTTP 404.

## Understand the code

| File / directory | Purpose |
| --- | --- |
| `pom.xml` | Java version and Spring Boot, MVC, Thymeleaf, JPA, validation, H2, and test dependencies. |
| `src/main/java/com/example/library/` | Application entry point, book model, database repository, and web controller. |
| `src/main/resources/templates/books/` | Thymeleaf catalogue and add/edit forms rendered by the server. |
| `src/main/resources/static/` | Local styles and the delete confirmation script. |
| `src/main/resources/application.properties` | Local server and persistent database settings. |
| `src/test/java/` | Integration tests for CRUD, form validation, and missing records. |
| `doc/` | Screenshots, evidence notes, and the complete two-student workflow. |

Request flow: **browser → controller → service → repository → H2 database**. The controller supplies the result to a **Thymeleaf page → browser**. An entity maps a Java object to a database row. Spring Data implements repository methods. The service handles database transactions. The controller decides which operation and page to use. Thymeleaf fills HTML with model values.

The assignment offers [Spring's Vaadin CRUD guide](https://spring.io/guides/gs/crud-with-vaadin/) as an optional tutorial. This implementation uses Spring MVC and Thymeleaf for its interface, with the same basic Java/JPA CRUD learning goals.

## Pair work and submission

Read **[doc/PAIR-WORK-GUIDE.md](doc/PAIR-WORK-GUIDE.md)** for exact roles, independent repositories, both reciprocal pull requests, example changes, review steps, screenshots, and Platon submission.

Each student must have their own course account with their own Student ID in the username, their own public repository, and their own running app. Both repositories must receive a real commit and pull request from the other student. This initial project alone does not complete the collaboration requirement.

**Deadline from the assignment: Sunday, 25 October 2026, 17:00.** Upload the running-app screenshot and a GitHub history screenshot showing both students to Platon after the partner contribution is merged. Keep extra CRUD and startup screenshots committed in `doc/`.

AI assistance was used to prepare this starter implementation and documentation. Each student should understand it, run it independently, and make their own real contribution.
