
/*

    REFERENCE:  Manning's Reactive Web Applications
    - Ch3, p 55; Functional Programming 101



 */

/*
FUNCTIONAL
- NOT like OOP where data is bound to functions that operate on it
- PURE functions are objects; can be passed around, composed & chained
(no side effects, produces expression results)
- produce immutable RESULTS; which can in turn be pipelined into other
functions
- Options handle null
- List, Try, Option are Monads which support map, flatmap, filter
- loops are IMPLICIT, NOT EXPLICIT
- For Comprehension allows for nested loops and YIELDs expression results
 */

/*
ATTENTION
- use Val instead of Var
- OK to use Var WITHIN a function to BUILD result; but DO NOT SELF-MODIFY method INPUTs!
ALWAYS have RESULT EXPRESSION be immutable and independent of function inputs!
- use IMMUTABLE collections instead of MUTABLE
- use OPTION instead of Null
- use MAP,FLATMAP OR TAIL RECURSION with PATTERN-MATCHING insead of Loops
- use CASE on class type matches; AND on breakdown of List segments for recursion
 instead of dynamic overridden methods across a rigid CLASS hierarchy;
 OR STATIC methods on UTILITY Class!
- COMPOSE/PIPELINE functions and immutable expression results
- HORIZONTAL-SLICE data and related methods in TRAITs
 */

// SYMBOL => means FUNCTION OBJECT definition,
//           where INPUT args on LHS,
//           OUTPUT args on RHS

// #1:  function
val x = 1                                       //> x  : Int = 1
def increase(i: Int) = i + 1                    //> increase: (i: Int)Int
increase(x)

// #2:  higher-order function, taking function as param
def summation(f: Int => Int, a: Int, b: Int): Int =
  if (a > b)
    0
  else
    f(a) + summation(f, a+1, b)

def square(x: Int): Int = x * x

def sumSquares(a: Int, b: Int): Int = summation(square, a, b)

sumSquares(1,2)



