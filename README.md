# Seminar Records Management System

## Project Description

This repository contains my implementation of a seminar records management system, an assignment for CS 3114. The key challenge was to create a system that manages variable-length records of seminars, including a memory management package and a hash table for efficient record access and retrieval.

### Key Features

- **Memory Management Package**: Utilizes the buddy method for allocating space in a memory pool, handling variable-length records.
- **Dynamic Hash Table**: Implements a closed hash table with double hashing, designed to be extensible to accommodate growing data.
- **Seminar Record Structure**: Manages records containing multiple fields like title, date/time, length, keywords, location, description, cost, and unique ID.

## Learning Outcomes

- **Data Structure Implementation**: Developed skills in implementing and manipulating complex data structures like hash tables and memory pools.
- **Efficient Memory Usage**: Gained insights into memory management, particularly in handling variable-length records and optimizing space allocation.
- **Dynamic System Design**: Learned about designing systems that dynamically adapt to changing data sizes, ensuring efficient data management and retrieval.

## Usage

Run the program via the command line:

```java SemManager {initial-memory-size} {initial-hash-size} {command-file}```

- `{initial-memory-size}`: Specifies the initial size of the memory pool (power of two).
- `{initial-hash-size}`: Determines the initial size of the hash table (power of two).
- `{command-file}`: Text file containing a series of commands for record management.

## Commands

The system supports various commands like:

- **Insert**: Adds a new seminar record.
- **Delete**: Removes a record by ID.
- **Search**: Retrieves a record by ID.
- **Print**: Displays the hash table or the list of free blocks in the memory pool.

## Output

The program outputs appropriate messages for each command, indicating the success or failure of operations.

---

This project showcases my ability to manage and manipulate complex data structures and design efficient memory management systems. Feel free to explore the code and understand the intricacies of its implementation. For any queries or feedback, you can reach me at brettn@vt.edu!
