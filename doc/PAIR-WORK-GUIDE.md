# Library Desk: two-student assignment guide

This guide follows the lecturer's pasted assignment. The supplied Java application is the starting point. The assignment is complete only after **each student** has a course GitHub account, a public repository, a locally running application, a real contribution from the other student merged through a pull request, screenshots, and their own Platon submission.

**Deadline: Sunday, 25 October 2026, 17:00.** The lecturer did not specify a timezone in the message; check the deadline displayed in Platon. Early completion excuses the next laboratory only when the required solution is uploaded before your scheduled laboratory.

## 1. Your roles and what each person creates

| Person | Main responsibility | Must create or complete personally |
| --- | --- | --- |
| Student A: you | Explain the Java model, controller, service, repository, validation, database and tests. | Course account; public `java-library-crud` repository; running local copy; a branch and pull request improving B's subtitle; review and merge B's pull request; screenshots; own Platon upload. |
| Student B: your partner | Explain the Thymeleaf pages, form fields, layout, statuses, success messages and empty state. | Course account; public `java-library-crud` repository; running local copy; a branch and pull request improving A's empty state; review and merge A's pull request; screenshots; own Platon upload. |

Both students use the complete supplied project. There is no separate incomplete “backend-only” or “frontend-only” submission. The role split gives you a first area to study; both of you must understand and demonstrate the whole CRUD flow.

The two actual code changes for your reciprocal pull requests are provided in sections 5 and 6. Apply each one **as the named student, in the other student's project**. Do not create commits under your partner's name or email.

**Partner handoff:** Moemen74378 first creates `Moemen74378/java-library-crud` from the clean source, runs it, and commits under Moemen's own verified identity. Then Moemen follows section 6 to contribute to `alisher74014/java-library-crud`, reviews A's incoming PR using section 7, and completes section 8 with Moemen's own screenshots and Platon upload.

### Files you receive

- `library-desk-source.zip`: clean source package; its top-level folder is `java-library-crud`.
- `java-library-crud/`: the same complete application source.
- `PAIR-WORK-GUIDE.md`: this guide, also included in the project's `doc` folder.

If this chat has already published A's repository, A should use that repository and skip the duplicate initial publication in section 3. B still needs an independent repository and initial commit under B's own account. The supplied source archive contains no `.git` history, compiled `target` folder, or local `data` database.

## 2. Accounts, tools and local application

1. Each student creates or uses their **own course GitHub account**. Your own Student ID must be part of your GitHub username. Your confirmed course accounts are Student A: `alisher74014` and Student B: `Moemen74378`.
2. Each student installs Java **17 or later** and Git. Use a JDK, not a runtime-only JRE. Maven is supplied through the wrapper, so a separate Maven installation is unnecessary. The first build needs internet access to download Maven and dependencies.
3. Each student saves `library-desk-source.zip` outside the new project folder, for example in Downloads. Keep the original archive available for the other student.
4. Open PowerShell. Each student independently extracts and copies the clean source:

```powershell
$SourceZip = Join-Path $env:USERPROFILE 'Downloads\library-desk-source.zip'
$StudyRoot = Join-Path $env:USERPROFILE 'Documents\LibraryDesk'
$ExtractedRoot = Join-Path $StudyRoot 'source-package'
$MyProject = Join-Path $StudyRoot 'my-java-library-crud'

New-Item -ItemType Directory -Path $StudyRoot -Force | Out-Null
Expand-Archive -LiteralPath $SourceZip -DestinationPath $ExtractedRoot
$SourceProject = Join-Path $ExtractedRoot 'java-library-crud'
if (Test-Path -LiteralPath $MyProject) { throw 'Choose a new MyProject folder; do not overwrite an existing project.' }
New-Item -ItemType Directory -Path $MyProject | Out-Null
Get-ChildItem -LiteralPath $SourceProject -Force |
    Where-Object { $_.Name -ne '.git' } |
    Copy-Item -Destination $MyProject -Recurse
Set-Location -LiteralPath $MyProject
java -version
git --version
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

If `source-package` already exists from an earlier extraction, use a fresh extraction folder. Do not extract into the new repository or nest the ZIP inside it.

Open **http://localhost:8080/books** in your browser. The title is **Library Desk**. Keep the terminal open while using the application. Stop the app with **Ctrl+C** in that terminal.

On macOS/Linux the equivalent wrapper commands are `./mvnw test` and `./mvnw spring-boot:run`. If needed, give the wrapper executable permission with `chmod +x mvnw` before running it.

An alternative, after stopping the development server, is:

```powershell
.\mvnw.cmd clean package
java -jar target/library-desk-1.0.0.jar
```

Run commands from the project root. The file database is stored relative to the current working directory in `data/`. Each student has a separate local database; books are not shared through GitHub. Restarting the app preserves your books. Sample data is inserted once; deleting all books does not make the samples return on restart.

If port 8080 is occupied, stop the earlier copy first. To deliberately use a second port:

```powershell
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.arguments=--server.port=8081'
```

Then open `http://localhost:8081/books`. If Java is not found or Maven reports an unsupported Java version, install/select a JDK 17+ and reopen PowerShell; `java -version` must show the intended JDK.

## 3. Each student publishes an independent public repository

If A's repository was already created and pushed in this chat, A should clone it into a new local folder and continue from section 4. Do not reinitialize the published repository or replace its existing history. B follows this section using B's own clean source copy.

1. Sign into your own course account in GitHub.
2. Create a new repository named **`java-library-crud`**. Set visibility to **Public**. Leave the repository empty: do not ask GitHub to add a README, license or `.gitignore`, since the supplied project already has its files.
3. In GitHub **Settings → Emails**, find an email verified for that account, or copy the exact GitHub-provided `noreply` address. Use that address for your local Git author identity so commits link to the correct profile. Do not invent a `noreply` address.
4. Run the commands below from your own source project. Use the line for your own account.

```powershell
$A_USER = 'alisher74014'
$B_USER = 'Moemen74378'

# Student A uses this line:
$MY_USER = $A_USER
# Student B uses this line INSTEAD:
# $MY_USER = $B_USER

$MyGitName = Read-Host 'Your name for your own commits'
$MyGitEmail = Read-Host 'Your verified GitHub email or exact GitHub noreply address'

git init -b main
git config --local user.name $MyGitName
git config --local user.email $MyGitEmail
git config --local --get user.name
git config --local --get user.email
git status --short
git add .
git commit -m 'Create Library Desk Java CRUD application'
git remote add origin "https://github.com/$MY_USER/java-library-crud.git"
git remote -v
git push -u origin main
```

Authenticate as the same course account that owns this repository when Git/Git Credential Manager asks you to sign in. A second student's computer must authenticate as the second student. A Git author name alone does not switch the account used for pushing.

Check the repository in GitHub: the source, README and `doc` directory should be visible. Open the first commit and confirm its author links to your own course profile. Each repository needs its **own initial history**; do not copy A's `.git` directory to B or clone A and call that B's independent starting repository.

For an already published A repository, clone it and set the local author identity before making any new commit:

```powershell
$A_USER = 'alisher74014'
$AProject = Join-Path $env:USERPROFILE 'Documents\LibraryDesk\my-java-library-crud'
git clone "https://github.com/$A_USER/java-library-crud.git" $AProject
Set-Location -LiteralPath $AProject
git config --local user.name (Read-Host 'Your name for your own commits')
git config --local user.email (Read-Host 'Your verified GitHub email or exact GitHub noreply address')
```

Clone into an empty/new destination. If you already have the published checkout on this computer, use that checkout instead.

## 4. Test CRUD and understand your own application

Each student does this personally in their own running application:

1. **Read:** show the initial list and the counts for all, available and borrowed books.
2. **Create:** add `The Hobbit`, author `J. R. R. Tolkien`, year `1937`, status `Available`.
3. **Update:** edit that book, change status to `Borrowed`, and save. Confirm the list and counts update.
4. **Validate:** try saving a blank title, blank author or a year outside `1–2100`. Confirm an error appears and the invalid book is not saved. Correct it and save.
5. **Delete:** delete the example book and accept the confirmation. Confirm it disappears and the success message appears.
6. **Persist:** add a book, stop the app, run it again, and confirm the book remains.

Take screenshots while doing these steps. Section 8 gives the required evidence names and uploads. Use your own running app; the partner also needs screenshots from the partner's local app.

## 5. Student A contributes to Student B's repository

**A does every action in this section while signed into A's account.** This change belongs in **B's** application.

### A1. Create a fork with a different name

Open B's public `java-library-crud` repository. Click **Fork → Create a new fork**. Choose A as owner and change the fork repository name to **`partner-java-library-crud`**.

A already owns a repository named `java-library-crud`, so the fork needs a unique name in A's account. If the fork form initially uses the original name, edit its repository-name field before creating it. Forking is the contribution route here; no collaborator invitation is required.

### A2. Clone A's fork of B and make a real change

```powershell
$A_USER = 'alisher74014'
$B_USER = 'Moemen74378'
$AContribution = Join-Path $env:USERPROFILE 'Documents\LibraryDesk\a-contributes-to-b'

git clone "https://github.com/$A_USER/partner-java-library-crud.git" $AContribution
Set-Location -LiteralPath $AContribution
git config --local user.name (Read-Host 'Student A: your own name')
git config --local user.email (Read-Host 'Student A: your own verified GitHub or exact noreply email')
git remote add upstream "https://github.com/$B_USER/java-library-crud.git"
git switch -c a-improve-library-description
```

Open `src/main/resources/templates/books/list.html` in your editor. Find this exact line:

```html
<p class="page-description">A thoughtful place to keep track of the books on your shelves.</p>
```

Replace it with:

```html
<p class="page-description">Keep every title, author, and reading status in one place.</p>
```

Verify the change, then commit and push:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Visit `http://localhost:8080/books` and confirm the new description. Stop this app with Ctrl+C before proceeding or launching a different checkout.

```powershell
git diff -- src/main/resources/templates/books/list.html
git add src/main/resources/templates/books/list.html
git commit -m 'Clarify the library collection description'
git push -u origin a-improve-library-description
```

### A3. Open a pull request targeting B

In A's `partner-java-library-crud` fork, choose **Compare & pull request**, or open **Pull requests → New pull request → compare across forks**.

- **Base repository:** `Moemen74378/java-library-crud`
- **Base branch:** `main`
- **Head repository:** `alisher74014/partner-java-library-crud`
- **Compare branch:** `a-improve-library-description`
- **Title:** `Clarify the library collection description`
- **Body:** `Updates the book-list subtitle to explain which information the app tracks. Verified the page locally and ran the Maven tests.` Use that validation sentence only after you actually performed it.

Check the diff shows the one text change. Create the pull request. B reviews and merges it using section 7. Creating a PR in A's own main project would not satisfy A's contribution to B.

## 6. Student B contributes to Student A's repository

**B does every action in this section while signed into B's account.** This change belongs in **A's** application.

### B1. Create B's fork of A

Open `alisher74014/java-library-crud`, click **Fork → Create a new fork**, choose B as owner, and name this fork **`partner-java-library-crud`**. B's independent main repository remains named `java-library-crud`.

### B2. Clone B's fork of A and make a real change

```powershell
$A_USER = 'alisher74014'
$B_USER = 'Moemen74378'
$BContribution = Join-Path $env:USERPROFILE 'Documents\LibraryDesk\b-contributes-to-a'

git clone "https://github.com/$B_USER/partner-java-library-crud.git" $BContribution
Set-Location -LiteralPath $BContribution
git config --local user.name (Read-Host 'Student B: your own name')
git config --local user.email (Read-Host 'Student B: your own verified GitHub or exact noreply email')
git remote add upstream "https://github.com/$A_USER/java-library-crud.git"
git switch -c b-improve-empty-library-message
```

Open `src/main/resources/templates/books/list.html`. Find this exact line:

```html
<p>Add your first book to start building your collection.</p>
```

Replace it with:

```html
<p>Your next great read belongs here. Add a book to begin.</p>
```

Run and check the change:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Open the app and delete all books through its Delete buttons to display the empty state. Confirm the new message and Add book button appear. Then add a book again. This fork's local database is separate from your main project's database. Stop the app with Ctrl+C.

```powershell
git diff -- src/main/resources/templates/books/list.html
git add src/main/resources/templates/books/list.html
git commit -m 'Improve the empty library message'
git push -u origin b-improve-empty-library-message
```

### B3. Open a pull request targeting A

Use **Compare & pull request** in B's fork or **compare across forks**:

- **Base repository:** `alisher74014/java-library-crud`
- **Base branch:** `main`
- **Head repository:** `Moemen74378/partner-java-library-crud`
- **Compare branch:** `b-improve-empty-library-message`
- **Title:** `Improve the empty library message`
- **Body:** `Makes the empty-library message more welcoming and gives the user a clear next step. Checked the empty state locally and ran the Maven tests.` Use that validation sentence only after you actually performed it.

Check the diff shows the one text change. Create the pull request. A reviews and merges it using section 7.

## 7. Each owner reviews and merges the partner's pull request

1. Sign into the account that owns the **base** repository: A owns A's main repository; B owns B's.
2. Open the incoming pull request. Check the contributor is your partner and that the base repository is your own `java-library-crud`.
3. Read **Files changed**. Compare the original text and new text. Check the PR contains the intended small change.
4. Run the partner's fork/branch locally if you need to verify the behavior yourself. A reviewer can clone the fork into a separate new folder and check out its named branch; stop other app copies before starting it.
5. In GitHub, use **Merge pull request → Create a merge commit → Confirm merge**. This keeps the partner's original authored commit visible in the history along with the merge commit.
6. Go back to your own original local project, then update it:

```powershell
# Use your own original project folder, not your partner-contribution fork.
Set-Location -LiteralPath (Join-Path $env:USERPROFILE 'Documents\LibraryDesk\my-java-library-crud')
git switch main
git pull --ff-only origin main
git log --oneline --decorate -8
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

A should now see B's changed empty-state message when A's library is empty. B should see A's changed subtitle. GitHub's **main branch commit history** must now show commits authored by both students. A merge commit authored only by the repository owner is not enough without the partner's real commit underneath it.

If your partner's commit does not link to the partner's GitHub profile, check the commit's author email against the partner's verified GitHub emails. Fix author identity before submission; do not make a fake replacement commit in the other person's name.

## 8. Screenshots, repository evidence and Platon submission

Each student saves their **own** screenshots into their **own main repository's** `doc` directory. A screenshot may cover more than one item, but the evidence must be readable.

| Suggested filename | What to show |
| --- | --- |
| `doc/01-running-app.png` | Browser address `localhost`, Library Desk, and a visible list of books. |
| `doc/02-create-book.png` | The Add book form filled with an example and/or its successful result. |
| `doc/03-update-book.png` | A changed book or status and the successful updated list. |
| `doc/04-delete-book.png` | The successful deletion message and the resulting list/empty state. |
| `doc/05-startup-console.png` | The app's terminal showing successful startup, including the server port and `Started LibraryApplication`. |
| `doc/06-github-history.png` | Your public repository's **main** commit history after merging your partner's PR, with at least one of your commits and one of your partner's commits. |

The first five demonstrate the required local running app, example CRUD actions, and startup console. Include the history screenshot too so all submission evidence is preserved in the repository. Do not substitute a README statement for an actual screenshot.

After saving your files, stop the app if convenient and commit from your own main checkout:

```powershell
git status --short
git add doc
git commit -m 'Document local CRUD application and collaboration evidence'
git push origin main
```

Check the images open correctly on GitHub. The `data/` database and `target/` build outputs should be ignored; they are not screenshot evidence.

Each student personally uploads **two screenshots to Platon**:

1. **GitHub commit history** clearly showing at least one of your own commits and at least one commit by your partner, taken after the partner PR has been merged into your repository's main branch.
2. **Your application running locally**.

Also keep the additional CRUD and startup screenshots committed in `doc` as required by the lecturer. Verify your repository is **Public**. Include the public repository link in the submission text if Platon provides a text field. A submits A's own evidence; B submits B's own evidence.

## 9. What the code does, file by file

| File or directory | Explanation you should be able to give |
| --- | --- |
| `pom.xml` | Maven build configuration: Java 17 baseline, Spring Boot 3.5.16, Spring MVC, Thymeleaf, validation, Spring Data JPA, H2 and tests. |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/` | Maven wrapper: downloads and runs the configured Maven version. Windows uses `mvnw.cmd`. |
| `LibraryApplication.java` | `main` method starts Spring Boot and discovers the application's components. |
| `book/Book.java` | JPA entity: a database row with ID, title, author, publication year and status. Validation requires title/author, limits them to 120 characters, and restricts years to 1–2100. |
| `book/BookStatus.java` | The two allowed states, Available and Borrowed; stored as readable enum names. |
| `book/BookRepository.java` | Spring Data JPA repository; Spring supplies the database operations, so we do not write ordinary CRUD SQL ourselves. |
| `book/BookService.java` | Retrieves, creates, updates and deletes books. Sorts the list; missing IDs produce HTTP 404. Transactions group database work. |
| `book/BookController.java` | Maps HTTP requests, validates form input, calls the service, selects a template, and redirects after successful changes. Counts available and borrowed books. |
| `config/SampleDataInitializer.java` | Inserts the example books once when the app starts. |
| `config/SeedMarker.java`, `SeedMarkerRepository.java` | Store the initialization marker, so deleting books does not cause examples to return at the next startup. |
| `resources/templates/books/list.html` | Thymeleaf list, totals, status badges, edit/delete controls and empty state. Both reciprocal PR changes are here. |
| `resources/templates/books/form.html` | Shared Create/Edit form; shows validation errors. |
| `resources/static/css/library.css` | The page's layout, colors, cards, table, form and responsive styling. |
| `resources/static/js/library.js` | Intercepts a Delete form submission to ask for confirmation before sending it. |
| `resources/application.properties` | App name, loopback binding, port 8080, H2 file database and JPA configuration. The H2 web console is disabled. |
| `src/test/` | Automated checks for the app's behavior. Run them with the Maven wrapper. |
| `.gitignore` | Keeps generated build output and local database files out of version control. |
| `doc/` | Working screenshots and this collaboration guide. |

The Java paths above are under `src/main/java/com/example/library/`; resource paths are under `src/main/`.

### Request flow and CRUD routes

`Browser → BookController → BookService → BookRepository → H2 database`

For display requests, the controller puts data in a model; Thymeleaf renders HTML, which the browser displays. Form submissions send data back to the controller. Invalid fields redisplay the form with errors. Successful changes redirect to `/books`, which avoids repeating a save when refreshing the result page.

| Action | HTTP route | What happens |
| --- | --- | --- |
| Home | `GET /` | Redirects to the book list. |
| Read | `GET /books` | Loads books and calculates totals. |
| Create form | `GET /books/new` | Displays an empty form. |
| Create save | `POST /books` | Validates and saves a new book. |
| Update form | `GET /books/{id}/edit` | Loads the existing book for editing. |
| Update save | `POST /books/{id}` | Validates and updates that existing book. |
| Delete | `POST /books/{id}/delete` | Deletes that book and redirects. |

### Questions to practise answering aloud

- **What is CRUD?** Create, Read, Update and Delete; show each action in your browser.
- **Why Java and Spring Boot?** Java provides the application code; Spring Boot configures the web server and connects the MVC, template and database libraries.
- **What is Thymeleaf?** A server-side template engine that combines HTML with model values before sending the finished page to the browser.
- **Where is the data stored?** In each student's local H2 file database in `data/`; restarting retains the data.
- **What do `@Entity` and `@Id` mean?** This class is persisted as database rows, and its generated ID identifies a row.
- **Why a service between controller and repository?** The controller handles web input/output; the service handles the book operations and transaction boundaries; the repository handles persistence.
- **Why validate on the server?** Browser constraints can be bypassed; the server checks the input before saving it.
- **What does `@Transactional` do?** It defines database work that completes as a unit; a managed entity's changed fields are written when the update transaction commits.
- **Why is deleting a POST?** A normal page/link visit should not delete data. The form sends a deliberate mutation request.
- **What is a branch?** A movable reference to a line of commits, used here to isolate the small contribution.
- **What is a fork?** Your GitHub-hosted copy of your partner's repository, from which you can propose changes without write access to their main repository.
- **What is a pull request?** A request to review and merge one branch's changes into a target repository/branch.
- **What is `origin` versus `upstream`?** In a contribution checkout, `origin` is your fork; `upstream` is the original partner-owned repository. In your main checkout, `origin` is your own main repository.
- **What did you personally change?** A changed B's subtitle; B changed A's empty-state message. Show the actual PR, diff, author and merged commit.
- **Why two independent main repositories?** The lecturer requires every student to own a public Java project and run it locally; the reciprocal PRs add real collaboration history to each one.

## 10. Final individual checklist

- [ ] My own Student ID appears in my own course GitHub username.
- [ ] My own `java-library-crud` repository is public.
- [ ] I ran my own app and demonstrated create, read, update and delete.
- [ ] I can explain the structure, fields, validation, persistence and my actual change.
- [ ] I authored a real commit and PR to my partner's repository.
- [ ] My partner authored a real commit and PR to my repository.
- [ ] I reviewed and merged the partner PR; my partner reviewed and merged mine.
- [ ] My main branch history visibly contains both authors' work.
- [ ] My own `doc` directory contains running app, CRUD, startup console and history screenshots, committed and pushed.
- [ ] I uploaded my own two required screenshots to Platon before the deadline.
- [ ] I checked access to **Laboratory 1 – Git Basics** on Platon and reported inaccessible material to the lecturer if necessary.
- [ ] If using today's attendance activity, I personally marked attendance during **16:50–18:50** on Platon. Nobody else can truthfully mark it on my behalf.

The lecture is optional; laboratories are normally mandatory. The early-completion exception applies only after the required work has been submitted before your scheduled laboratory. This guide and generated code do not themselves mark attendance or upload a Platon submission.
