# Nova Programming Language Compiler
Specification, compiler made in Java 1.8 and some other stuff. For Compilers classes.

Author: Barros Filho, Rubens. 17/09/2016

## Overview

* NOVA is a **general purpose** programming language. 

* Structured, imperative and strongly typed. 

* Inspired by the C programming language.

* Each statement must end with a semicolon. 

* To improve readability, setting functions is allowed.

## Data types

* Identifier
  * Begins necessarily with an uppercase or lowercase.
  * Other characters can be letters, numbers or underscores. 
  * Unlimited size.
  * The use of white spaces is prohibited.
* Comments
  * Line comment: Demarcated by "#"
  * Block comment: Doesn't exist in NOVA programming language.
* Integer
* Floating Point
* Characters & Strings
* Boolean
* Unidimensional Vectors
* Void

## Operators

* Arithmetical
  * "+"
  * "-"
  * "/"
  * "*"
  * "%"
  
* Relational
  * "=="
  * "!="
  * ">"
  * "<"
  * ">="
  * "<="

* Logical
  * "and"
  * "or"
  * "not"
  
## Instructions

* Conditional
  * if: 
  ```c
  if (condition) { 
    /* Do something 
  }
  ```
  * if-else: 
  ```c
  if (condition) { 
  /* Do something 
  } else { 
  /* Do something else
  }
  ```
  
* Iterative
  * For loop:
  ```c
  for (index, limit, step) { 
  /* Do something 
  }
  ```
  * While loop:
  
  ```c
  while (condition) {
  /* Do something
  }
  ```
* Functions (Hint: NOVA does not support function overloading.)
  * Declaration:
  
    ```c
    returnType functionName(type firstParameter, type secondParameter, ..., type nParameter) { 
      /* Do something
    }
    ```

    * Must always have return type, even if it is *void*.

## Code examples

### There are code examples in the repository.

