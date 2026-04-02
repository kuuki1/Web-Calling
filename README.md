# 🌐 Six Degrees Web Calling

## 📌 Overview

Six Degrees Web Calling is a full-stack web application that finds the shortest connection path between two public figures based on the **Six Degrees of Separation** theory.

The system models a **graph network** built from Wikipedia data, where:

* Each person is represented as a node
* Relationships between people are represented as edges

The application uses the **Breadth-First Search (BFS)** algorithm to compute the shortest path between two nodes in the graph.

---

## 🎯 Objectives

* Demonstrate understanding of **graph data structures**
* Implement **BFS for shortest path search**
* Design a **scalable backend architecture**
* Build a **full-stack application (Spring Boot + React)**

---

## 🚀 Features

* 🔍 Search connection between two people
* 🔗 Return shortest path (if exists)
* 📏 Show degree of separation
* ⚡ Fast query using in-memory graph
* 🌐 Wikipedia-based data crawling (offline)

---

## 🧠 System Design

### Architecture

```
[ React Frontend ]
        ↓
[ Spring Boot API ]
        ↓
[ Relational Database ]
```

---

### Data Flow

1. User inputs two names
2. Backend loads graph from database
3. BFS algorithm finds shortest path
4. API returns result
5. Frontend displays path

---

## 🏗️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* RESTful API

### Frontend

* ReactJS
* Axios

### Database

* MySQL / PostgreSQL

---

## 🧩 Key Technical Decisions

### 1. BFS for Shortest Path

* Graph is unweighted
* BFS guarantees shortest path
* Time complexity: `O(V + E)`

---

### 2. In-Memory Graph (Performance Optimization)

Instead of querying the database during traversal:

* Load all relationships into memory
* Build adjacency list (`Map<String, List<String>>`)
* Run BFS in-memory

✅ Benefits:

* Avoids multiple DB calls
* Reduces latency significantly

---

### 3. Offline Data Crawling

* Wikipedia API used to collect relationships
* Data is stored locally in DB
* Avoids:

  * Rate limiting
  * High latency
  * Unstable runtime scraping

---

## 🗄️ Database Design

### Table: `person`

| Column | Type   | Description |
| ------ | ------ | ----------- |
| id     | Long   | Primary key |
| name   | String | Unique name |

---

### Table: `relationship`

| Column     | Type | Description  |
| ---------- | ---- | ------------ |
| id         | Long | Primary key  |
| person1_id | Long | FK to person |
| person2_id | Long | FK to person |

---

## 🔌 API Specification

### GET `/api/connection`

#### Request

```
/api/connection?from=Elon Musk&to=Bill Gates
```

#### Response

```json
{
  "path": ["Elon Musk", "Peter Thiel", "Bill Gates"],
  "distance": 2
}
```

---

## ⚙️ Setup Instructions

### 1. Clone repository

```
git clone https://github.com/your-username/six-degrees-web-calling.git
```

---

### 2. Backend Setup

#### Configure database

```
spring.datasource.url=jdbc:mysql://localhost:3306/connection_db
spring.datasource.username=root
spring.datasource.password=your_password
```

#### Run server

```
mvn spring-boot:run
```

---

### 3. Frontend Setup

```
cd frontend
npm install
npm start
```

---

## 📊 Algorithm Implementation

### Breadth-First Search (BFS)

* Queue-based traversal
* Tracks visited nodes
* Uses parent mapping to reconstruct path

Pseudo flow:

```
1. Start from source node
2. Explore all neighbors
3. Continue level by level
4. Stop when target is found
5. Reconstruct path using parent map
```

---

## 🌍 Data Source

* Wikipedia API
* Extract internal links between people
* Build graph edges from co-occurrence

---

## ⚠️ Limitations

* Dataset size is limited (not full Wikipedia graph)
* Relationship definition is simplified (co-occurrence)
* No weighted edges (all connections equal)

---

## 📈 Future Improvements

* 🔄 Use Graph Database (Neo4j)
* ⚖️ Add weighted relationships (Dijkstra)
* 📊 Graph visualization (D3.js)
* ⚡ Caching layer (Redis)
* 🔍 Improve entity extraction accuracy

---

## 💡 What I Learned

* Graph modeling and traversal
* BFS algorithm in real-world use case
* Backend performance optimization
* API design and frontend integration
* Working with external data sources (Wikipedia)

---

## 👨‍💻 Author

* Kuuki1

---

## 📌 Notes

This project is designed as a **portfolio-ready system** to demonstrate backend engineering fundamentals and problem-solving skills in graph-based applications.
