# XML Parser

A command-line application for reading and manipulating XML files, built entirely in Java without external libraries.

Developed as a coursework project for the **OOP 1** course at Technical University of Varna.

## Table of Contents

- [Overview](#overview)
- [Getting Started](#getting-started)
- [Supported XML Structure](#supported-xml-structure)
- [Unique ID Assignment](#unique-id-assignment)
- [Commands](#commands)
  - [File Operations](#file-operations)
  - [Element Operations](#element-operations)
  - [XPath Queries](#xpath-queries)
- [XPath Operators](#xpath-operators)
- [Project Structure](#project-structure)
- [Author](#author)

## Overview

The program provides a CLI for opening XML files, navigating their structure, modifying attributes, adding/removing elements, and saving changes. It implements a custom XML parser (no external libraries like DOM or SAX are used) and supports a simplified XPath query language.

Key features:
- Manual XML parsing and serialization
- Automatic unique ID assignment for all elements
- Attribute and element manipulation via commands
- Full support for XML Namespaces and prefix resolution
- Simplified XPath 2.0 query support with `/`, `[]`, `@`, and `=` operators
- Full XPath axis navigation (`ancestor::`, `descendant::`, `parent::`, etc.)
- File management with `open`, `save`, `save as`, and `close`

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 17 or higher — [download here](https://www.oracle.com/java/technologies/downloads/)
- Verify installation by running: `java -version`

### Option 1: Run with IntelliJ IDEA (Recommended)

1. Open IntelliJ IDEA and select **File → Open**
2. Navigate to the `xml-parser` folder and click **Open**
3. Wait for the IDE to index the project
4. Open `src/bg/tu_varna/sit/f24621627/Application.java`
5. Click the green **▶ Run** button next to the `main` method
6. The interactive CLI will start in the **Run** panel at the bottom

### Option 2: Run from Terminal

1. Open a terminal and navigate to the project root:
   ```bash
   cd path/to/xml-parser
   ```

2. Compile all source files:
   ```bash
   find src -name "*.java" | xargs javac -d out
   ```

3. Run the application:
   ```bash
   java -cp out bg.tu_varna.sit.f24621627.Application
   ```

4. You should see:
   ```
   Welcome to XML Parser. Type 'help' for commands.
   >
   ```

### Quick Start Example

```
Welcome to XML Parser. Type 'help' for commands.
> open library.xml
Successfully opened library.xml
> print
<library name="City Central Library" id="gen-1">
    <section category="Science Fiction" id="gen-2">
        <book id="1_1" year="1965">
            <title id="gen-3">Dune</title>
            <author id="gen-4">Frank Herbert</author>
        </book>
    </section>
</library>

> select 1_1 year
1965
> set 1_1 year 2020
Successfully set attribute 'year' to '2020' for element '1_1'.
> save
Successfully saved library.xml
> close
Successfully closed the document.
> exit
Exiting the program...
```

## Supported XML Structure

The program supports XML elements with the following properties:

- **Identifier** (`id` attribute)
- **Attributes** (key-value pairs)
- **Nested child elements** or **text content** (but not both simultaneously)

> **Note:** The parser does not support the full XML specification. It handles a simplified subset sufficient for structured data files — no namespaces, DTDs, CDATA, or processing instructions.

## Unique ID Assignment

When a file is opened, the program ensures every element has a unique ID:

| Scenario | Behavior | Example |
|---|---|---|
| Element has a unique `id` | Keeps the original value | `id="5"` → `id="5"` |
| Two elements share the same `id` | Appends a suffix to make them unique | `id="1"` → `id="1_1"`, `id="1_2"` |
| Element has no `id` | Generates a new one automatically | → `id="gen-1"`, `id="gen-2"` |

## Commands

All commands return a message indicating success or failure. Commands (except `open`, `help`, `exit`) require a file to be opened first.

### File Operations

#### `open <file>`
Opens an XML file and loads its content into memory. If the file does not exist, a new empty file is created.
```
> open library.xml
Successfully opened library.xml
```

#### `close`
Closes the currently opened document. Unsaved changes are discarded.
```
> close
Successfully closed the document.
```

#### `save`
Saves all changes back to the current file.
```
> save
Successfully saved library.xml
```

#### `save as <file>`
Saves the document to a new file path. Supports paths with spaces using quotes.
```
> save as "my documents/backup.xml"
Successfully saved my documents/backup.xml
```

#### `help`
Displays a list of all available commands.

#### `exit`
Exits the program.

### Element Operations

#### `print`
Prints the entire XML document with proper indentation.
```
> print
<library name="City Central Library" id="gen-1">
    <section category="Science Fiction" id="gen-2">
        ...
    </section>
</library>
```

#### `select <id> <key>`
Retrieves the value of an attribute from an element.
```
> select 1_1 year
1965
```

#### `set <id> <key> <value>`
Sets or updates the value of an attribute. If the key is `id`, the ID registry is updated accordingly.
```
> set 1_1 year 2020
Successfully set attribute 'year' to '2020' for element '1_1'.
```

#### `delete <id> <key>`
Removes an attribute from an element.
```
> delete 1_1 year
Successfully deleted attribute 'year' from element '1_1'.
```

#### `children <id>`
Lists all child elements with their tag names and attributes.
```
> children gen-2
Children of element 'gen-2':
1. XmlElement { tag='book', attributes=[id="1_1", year="1965"], children=none }
2. book -> id="2" year="1951"
```

#### `child <id> <n>`
Accesses the n-th child element (0-indexed).
```
> child gen-2 0
XmlElement { tag='book', attributes=[id="1_1", year="1965"], children=none }
```

#### `text <id>`
Prints the text content of an element.
```
> text gen-3
Dune
```

#### `newchild <id>`
Adds a new empty child element to the specified parent. The new element receives an auto-generated ID.
```
> newchild gen-2
Successfully added new child to element 'gen-2'. Its new ID is 'new-1'.
```

### XPath Queries

#### `xpath <id> <expression>`
Executes an XPath query starting from the element with the given ID.
```
> xpath gen-1 section/book/title
Dune
Foundation
Harry Potter
The Hobbit
```

## XPath Operators & Axes

The following XPath operators and axes are supported:

| Operator/Axis | Syntax | Description | Example |
|---|---|---|---|
| `/` | `tag/tag` | Navigate to child elements by tag name | `section/book/title` |
| `[]` | `tag[n]` | Select the n-th matching element (0-indexed) | `section[0]/book[1]` |
| `=` | `tag(key="value")` | Filter elements by attribute or child text content | `section(category="Fantasy")` |
| `@` | `tag(@attr)` | Extract attribute values from matching elements | `section/book(@id)` |
| `Axis::` | `axis::tag` | Navigate up or down the document tree. Supported axes: `child::`, `parent::`, `ancestor::`, `ancestor-or-self::`, `descendant::`, `descendant-or-self::`, `self::` | `b1 ancestor::section` |

> **Note on Namespaces:** The XPath engine fully supports XML namespaces. You can query elements and attributes using their prefixes (e.g., `bk:book(bk:status="available")`).

### XPath Examples

Given a library XML with Science Fiction and Fantasy sections:

```
> xpath gen-1 section/book/title
Dune
Foundation
Harry Potter
The Hobbit

> xpath gen-1 section(category="Fantasy")/book/title
Harry Potter
The Hobbit

> xpath gen-1 section[0]/book[0]
<book id="1_1" year="1965">...</book>

> xpath gen-1 section/book(@id)
1_1
2
1_2
gen-11
```

## Project Structure

```
src/bg/tu_varna/sit/f24621627/
│
├── Application.java              # Entry point
├── CommandLineInterface.java     # CLI loop and command routing
│
├── models/
│   ├── XmlDocument.java          # Document lifecycle and memory state
│   └── XmlElement.java           # XML element data model (tree node)
│
├── parsers/
│   ├── XmlParser.java            # Custom XML string parser (tree builder)
│   ├── AttributeTagParser.java   # Strategy for parsing attributes
│   ├── SimpleTagParser.java      # Strategy for parsing simple tags
│   └── IdAssigner.java           # Unique ID generation and assignment
│
├── io/
│   ├── FileHandler.java          # File system I/O operations
│   └── XmlSerializer.java        # XML text formatting and serialization
│
├── exceptions/
│   └── XmlParseException.java    # Custom exception for parse errors
│
├── commands/                     # Command Pattern — one class per command
│   ├── Command.java              #   Abstract base class
│   ├── OpenCommand.java
│   ├── CloseCommand.java
│   ├── SaveCommand.java
│   ├── PrintCommand.java
│   ├── SelectCommand.java
│   ├── SetCommand.java
│   ├── DeleteCommand.java
│   ├── ChildCommand.java
│   ├── ChildrenCommand.java
│   ├── TextCommand.java
│   ├── NewChildCommand.java
│   ├── XPathCommand.java
│   ├── HelpCommand.java
│   └── ExitCommand.java
│
└── xpath/                        # Strategy Pattern — one class per XPath operator
    ├── XPathOperator.java        #   Abstract base class
    ├── TagNavigationOperator.java
    ├── IndexOperator.java
    ├── AttributeFilterOperator.java
    ├── AttributeAccessor.java
    └── AxisResolver.java             # Resolves XPath axes
```

## Author

**Student ID:** 24621627  
**Course:** OOP 1 — Technical University of Varna
