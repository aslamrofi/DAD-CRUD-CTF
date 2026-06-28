# DAD CRUD CTF

DAD CRUD CTF is a web-based Capture The Flag (CTF) platform built with **Java Spring Boot**, **Thymeleaf**, **Spring Data JPA**, and **Oracle Database**. The application allows admins to create and manage CTF challenges, while players can register, log in, download challenge files, submit flags, request AI hints, view solved challenges, and compete on a leaderboard.

## Features

### Player Features

- Team/player registration and login
- Player dashboard with available CTF challenges
- Flag submission system
- Duplicate-solve prevention
- Challenge file download
- AI hint request for each challenge
- Personal solved-challenges page
- Public leaderboard ranking teams by total points

### Admin Features

- Admin dashboard
- Add new CTF challenges
- Upload challenge files/payloads
- View all active challenges
- Delete existing challenges

### AI Hint Feature

The project includes an `OpenAiService` that can generate short CTF hints based on the challenge title and description. Each AI request is also logged into the database through the `AI_REQUESTS` table.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot |
| Frontend | Thymeleaf, HTML, CSS |
| Database | Oracle Database |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Optional AI Integration | OpenAI Chat Completions API |

## Project Structure

```text
DAD-CRUD-CTF-master/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/crudctf/
        │   ├── CrudCtfApplication.java
        │   ├── controller/
        │   │   ├── AdminController.java
        │   │   ├── AuthController.java
        │   │   ├── ChallengeController.java
        │   │   ├── HomeController.java
        │   │   ├── LeaderboardController.java
        │   │   └── PlayerController.java
        │   ├── model/
        │   │   ├── AiRequest.java
        │   │   ├── Challenge.java
        │   │   ├── Solve.java
        │   │   └── User.java
        │   ├── repository/
        │   │   ├── AiRequestRepository.java
        │   │   ├── ChallengeRepository.java
        │   │   ├── SolveRepository.java
        │   │   └── UserRepository.java
        │   └── service/
        │       └── OpenAiService.java
        └── resources/
            ├── application.properties
            └── templates/
                ├── about.html
                ├── admin-dashboard.html
                ├── leaderboard.html
                ├── login.html
                ├── player-dashboard.html
                ├── player-solves.html
                └── register.html
```

## Main Routes

| Route | Description |
|---|---|
| `/` | Redirects to login page |
| `/login` | User login page |
| `/register` | Team/player registration page |
| `/logout` | Logs out the current session |
| `/about` | System information / about page |
| `/leaderboard` | Public team leaderboard |
| `/admin/dashboard` | Admin challenge management dashboard |
| `/admin/challenge/add` | Adds a new challenge with optional file upload |
| `/admin/delete/{id}` | Deletes a challenge |
| `/player/dashboard` | Player dashboard with active challenges |
| `/player/submit` | Submits a flag for validation |
| `/player/download/{id}` | Downloads an attached challenge file |
| `/player/ask-ai` | Requests an AI-generated hint |
| `/player/solves` | Shows the player’s solved challenges |

## Database Tables

The application uses JPA entities and can automatically create or update the required tables when `spring.jpa.hibernate.ddl-auto=update` is enabled.

| Table | Purpose |
|---|---|
| `USERS` | Stores registered users, passwords, and roles |
| `CHALLENGES` | Stores challenge details, flags, points, and uploaded files |
| `SOLVES` | Stores completed challenge records for each player |
| `AI_REQUESTS` | Stores AI hint request logs |

## Prerequisites

Before running the project, make sure you have:

- Java 17 or newer
- Maven
- Oracle Database access
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code
- Optional: an OpenAI API key for AI hints

## Configuration

Edit `src/main/resources/application.properties` and configure your database connection:

```properties
spring.datasource.url=jdbc:oracle:thin:@//HOST:PORT/SERVICE_NAME
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

For the AI hint feature, set the following environment variable:

```bash
CRUD_OPENAI_API=your_openai_api_key_here
```

If the variable is not provided, the application will still start, but AI hint requests may return a system error message.

> Important: Do not commit real database credentials or API keys to GitHub. Use environment variables or a local-only configuration file for sensitive values.

## How to Run Locally

Clone or extract the project, then open a terminal in the project root.

```bash
mvn clean install
mvn spring-boot:run
```

After the application starts, open:

```text
http://localhost:8080
```

The app will redirect to the login page.

## How to Build a JAR File

To package the application:

```bash
mvn clean package
```

Run the generated JAR:

```bash
java -jar target/crudctf-0.0.1-SNAPSHOT.jar
```

## Basic Usage Flow

### 1. Register a Player

Go to `/register`, create a team name and password, then log in. New accounts are automatically assigned the `PLAYER` role.

### 2. Create an Admin Account

Since registration creates player accounts by default, an admin account must be created or promoted manually in the database.

Example:

```sql
UPDATE USERS
SET ROLE = 'ADMIN'
WHERE USERNAME = 'your_admin_username';

COMMIT;
```

After that, log in with the promoted admin account and visit `/admin/dashboard`.

### 3. Add Challenges

From the admin dashboard, enter the challenge title, category, description, flag, points, and optionally upload a challenge file.

### 4. Solve Challenges

Players can open `/player/dashboard`, download files, request AI hints, and submit flags. Correct submissions are saved in the `SOLVES` table and reflected on the leaderboard.

### 5. View Leaderboard

Visit `/leaderboard` to see teams ranked by total points.

## Notes for Development

- The project currently uses simple session-based authentication.
- Passwords are stored as plain text in the current implementation.
- Challenge deletion is handled through a GET route.
- There are no automated tests included yet.
- Uploaded challenge files are stored directly in the database as BLOB data.

## Recommended Improvements

For a production-ready version, consider adding:

- Password hashing with BCrypt
- Spring Security authentication and authorization
- CSRF protection
- Input validation for all forms
- Role management page for admins
- Edit challenge functionality
- Better error handling and user feedback
- Unit and integration tests
- Environment-based configuration for database and API credentials
- File size validation for challenge uploads

## License

No license file is currently included in this repository. Add a license before publishing or distributing the project publicly.
