# Smart India Hackathon - AI-Based Internship Recommendation Engine

## Project Structure

```
SIH/
├── docs/                                   # Documentation
│   ├── ALGORITHM_IMPLEMENTATION.md         # ML Algorithm details
│   └── SETUP_INSTRUCTIONS.md              # Setup and installation guide
│
├── ml-research/                            # Machine Learning Research
│   └── InternshipRecommendationAlgorithm.py  # Original Python ML algorithm
│
├── scripts/                               # Build and test scripts
│   ├── run-simple.bat                     # Application startup script
│   ├── test-algorithm-simple.ps1          # ML algorithm test
│   ├── test-api.ps1                       # API testing script
│   ├── test-database-setup.ps1            # Database setup test
│   └── test-ml-algorithm.ps1              # ML functionality test
│
├── src/                                   # Java source code
│   └── main/
│       ├── java/com/example/demo/
│       │   ├── algorithm/                 # ML Algorithm Implementation
│       │   │   ├── FairnessWeights.java
│       │   │   ├── FeatureVector.java
│       │   │   └── InternshipRecommendationAlgorithm.java
│       │   │
│       │   ├── config/                    # Configuration classes
│       │   │   ├── CacheConfig.java       # Redis cache configuration
│       │   │   └── DataInitializer.java   # Database initialization
│       │   │
│       │   ├── controller/                # REST API controllers
│       │   │   └── RecommendationController.java
│       │   │
│       │   ├── model/                     # Data models/entities
│       │   │   ├── Internship.java
│       │   │   ├── Recommendation.java
│       │   │   └── StudentProfile.java
│       │   │
│       │   ├── repository/                # Data access layer
│       │   │   ├── InternshipRepository.java
│       │   │   ├── RecommendationRepository.java
│       │   │   └── StudentProfileRepository.java
│       │   │
│       │   ├── service/                   # Business logic
│       │   │   └── RecommendationService.java
│       │   │
│       │   └── DemoApplication.java       # Spring Boot main class
│       │
│       └── resources/
│           └── application.properties     # Application configuration
│
├── target/                               # Build output (generated)
├── .gitignore                           # Git ignore rules
├── .mvn/                                # Maven wrapper
├── mvnw.cmd                             # Maven wrapper script
├── pom.xml                              # Maven project configuration
└── README.md                            # Project overview
```

## Architecture Overview

### **Core Components**

1. **ML Algorithm Layer** (`algorithm/`)
   - `InternshipRecommendationAlgorithm.java` - Core ML engine
   - `FeatureVector.java` - Feature extraction and representation  
   - `FairnessWeights.java` - Fairness parameters configuration

2. **Data Layer** (`model/` + `repository/`)
   - JPA entities for database persistence
   - Repository interfaces for data access
   - MySQL database with Redis caching

3. **Business Logic** (`service/`)
   - `RecommendationService.java` - Orchestrates ML algorithm with data layer

4. **API Layer** (`controller/`)
   - RESTful endpoints for web/mobile client integration

5. **Configuration** (`config/`)
   - Database and cache configuration
   - Application initialization and data seeding

### **Technology Stack**

- **Backend**: Spring Boot 2.7.0, Java 11
- **Database**: MySQL 8.0+ with HikariCP connection pooling
- **Caching**: Redis 7+ for performance optimization
- **ML Algorithm**: Neural network-based fairness algorithm (Java adaptation of Python)
- **Build Tool**: Maven 3.8+
- **API**: REST with JSON

### **Key Features**

✅ **Intelligent Matching**: ML algorithm with 70% prediction + 30% fairness weighting  
✅ **Fairness Considerations**: Automatic boosts for rural/tribal students  
✅ **Skill Analysis**: Jaccard similarity for sophisticated skill matching  
✅ **Performance**: Redis caching for sub-second response times  
✅ **Scalability**: Database persistence with entity relationships  
✅ **Transparency**: Detailed scoring breakdown via API

## Quick Start

1. **Setup Database** (Optional - falls back to mock data)
   ```bash
   docker run --name mysql-internship -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -e MYSQL_DATABASE=internship_db -p 3306:3306 -d mysql:8.0
   docker run --name redis-internship -p 6379:6379 -d redis:7-alpine
   ```

2. **Run Application**
   ```bash
   scripts\run-simple.bat
   ```

3. **Test API**
   ```bash
   scripts\test-api.ps1
   ```

## API Endpoints

- `POST /recommend` - Get ML-powered recommendations
- `GET /internships` - List all available internships
- `POST /students` - Save student profile
- `POST /algorithm/score` - Get detailed ML scoring breakdown
- `GET /health` - System health check

---

**Built for Smart India Hackathon 2025**  
*AI-Based Internship Recommendation Engine for PM Internship Scheme*
