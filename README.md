# IBM SkillsBuild Gamification

Brief description of what this project does and who it's for. Provide context and add a link to any relevant resources or documentation.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them. For example:

- Java JDK 17 or newer
- Gradle
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse, VS Code)

### Installing

A step-by-step series of examples that tell you how to get a development environment running.

1. **Clone the repository**

```bash
git clone https://campus.cs.le.ac.uk/gitlab/co2201-2024/group-12.git
```

2. **Navigate to the project directory**

```bash
 cd .\IBM_Project_Code\
```

3. **Set up environment variables**

Copy the `.env.example` file to a new file named `.env`, and edit it to fill in your specific settings:

```bash
cp .env.example .env
```

Make sure to replace placeholder values with your actual environment variables.

4. **Install dependencies**

```bash
./gradlew build
```

### Running the Program

How to run the program on your local machine.

1. **Start the application**

```bash
./gradlew bootRun
```

2. **Accessing the application**

Point your web browser or use a tool like Postman to interact with the application at:

```
http://localhost:8080
```

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Gradle](https://gradle.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning.  For the versions available, see the [tags on this repository](https://campus.cs.le.ac.uk/gitlab/co2201-2024/group-12/-/tags).

## Authors

- **Jack Partridge** - @jp589 - *Initial work* - [JackPartridge](https://github.com/JackPartridge)
- **A M A Pathan** - @ap833 - *Initial work*
- **E Borthwick** - @eb430 - *Initial work*
- **J Benoist** - @jb931 - *Initial work*
- **M A Y Abuharira** - @maya2 - *Initial work*
- **M N Ahmed** - @mna22 - *Initial work*
- **S Jadav** - @sj426 - *Initial work*
- **Y G Allen** - @yga1 - *Initial work*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
