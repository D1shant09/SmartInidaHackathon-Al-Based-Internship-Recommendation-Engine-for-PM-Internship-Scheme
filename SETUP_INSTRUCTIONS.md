# MySQL and Redis Setup Instructions

## Prerequisites for Testing

Before running the application with MySQL and Redis, you need to install and configure:

### 1. MySQL Database
- Install MySQL Server 8.0+ from https://dev.mysql.com/downloads/mysql/
- Create a database called `internship_db` (the app will create it automatically if it doesn't exist)
- Default configuration expects:
  - Host: localhost:3306
  - Username: root
  - Password: (empty - you can change this in application.properties)

### 2. Redis Server
- Install Redis from https://redis.io/download or for Windows: https://github.com/MicrosoftArchive/redis/releases
- Default configuration expects Redis on localhost:6379
- Make sure Redis server is running before starting the application

### 3. Quick Installation Commands (Windows)

#### MySQL (using Chocolatey):
```powershell
choco install mysql
```

#### Redis (using Chocolatey):
```powershell
choco install redis-64
```

### 4. Alternative: Docker Setup (Recommended)

If you have Docker installed, you can run both MySQL and Redis using these commands:

#### MySQL Container:
```powershell
docker run --name mysql-internship -e MYSQL_ROOT_PASSWORD= -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=internship_db -p 3306:3306 -d mysql:8.0
```

#### Redis Container:
```powershell
docker run --name redis-internship -p 6379:6379 -d redis:7-alpine
```

### 5. Testing Without External Dependencies

The application is designed to fall back gracefully:
- If MySQL is not available, it will show connection errors but the app will still start
- If Redis is not available, caching will be disabled
- The app includes mock data as fallback for internships

## Running the Application

1. Start MySQL and Redis services
2. Run the application:
   ```powershell
   .\run-simple.bat
   ```
3. The application will:
   - Connect to MySQL and create tables automatically
   - Connect to Redis for caching
   - Initialize sample data if the database is empty
   - Start on http://localhost:8080

## New API Endpoints

- `POST /recommend` - Get recommendations for a student profile
- `POST /students` - Save a student profile to database
- `GET /internships` - Get all available internships
- `POST /internships/search` - Search internships by skills
- `POST /cache/clear` - Clear Redis cache
- `GET /health` - Health check (now shows MySQL and Redis status)

## Configuration

You can modify database and Redis settings in `src/main/resources/application.properties`:
- Change MySQL connection details
- Modify Redis configuration
- Adjust cache timeout settings
- Configure JPA/Hibernate behavior