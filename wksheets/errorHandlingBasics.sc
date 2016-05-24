/**
  * Created by daphneeng on 5/2/16.
  */

// Error-Handling Basics:
// - https://blog.knoldus.com/2014/04/11/idiomatic-error-handling-in-scala/
// - http://danielwestheide.com/blog/2012/12/26/the-neophytes-guide-to-scala-part-6-error-handling-with-try.html
// - http://stackoverflow.com/questions/18250888/map-the-exception-of-a-failed-future

// Differences between Map, Transform, MapValues
// NOTE:  identity is shorthand for x => x
/*
   http://stackoverflow.com/questions/25635803/difference-between-mapvalues-and-transform-in-map
 */

/*

    TRY as a datatype for idiomatic Error-Handling in Scala

    Idiomatic error handling in Scala is quite different from the paradigm known from languages like Java or Ruby.
    The Try type allows you to encapsulate computations that result in errors in a container and to chain operations
    on the computed values in a very elegant way. You can transfer what you know from working with collections and
    with Option values to how you deal with code that may result in errors – all in a uniform way.

    There are two different types of Try:
    - If an instance of Try[A] represents a successful computation,
    it is an instance of Success[A], simply wrapping a value of type A.
    - If, on the other hand, it represents a computation in which an error has occurred,
    it is an instance of Failure[A], wrapping a Throwable, i.e. an exception or other kind of error.

    If we know that a computation may result in an error,
    we can simply use Try[A] as the return type of our function.
    This makes the possibility explicit and forces clients of our function to deal with the possibility of an error in some way.

    Try was originally invented at Twitter to solve a specific problem: when using Future,
    the exception may be thrown on a different thread than the caller,
    and so can’t be returned through the stack. By returning an exception instead of throwing it,
    the system is able to reify the bottom type and let it cross thread boundaries to the calling context.

 */


/*

SIMPLE EXAMPLE:

Here, we have a method called sayHello which misbehaves when you do not pass a string to it.
If you call it without a string, it blows up and hence the letMeSayHello invocation blows up as well.

Placing a Try block around this logic will yield a results srray of the Try option-type wrapper automatically!

Try can be thought of as a container type that results in either a Success(value) or a Failure(exception)

TODO:  unsure how to handle finally {...} to close resources no matter what!
https://tersesystems.com/2012/12/27/error-handling-in-scala/

*/
import scala.util.{Try, Success, Failure}

// STEP 1:  handle exception, wrapped in Try Option-ish struct
def sayHello(any: Any): Try[String] = {
  Try {
    any match {
      case x: String => "Hello " + any
      case _ => throw new Exception("Huh!")
    }
  }
}

// STEP2:  recovery steps for exceptions on Try array result with partial function match in exception!
def letMeSayHello(someone:Any): Try[String] = {

  val results: Try[String] = sayHello(someone)

  results.recover {
    case ex: Exception => "Symbolic Names Rock; like PRINCE!"
  }
}

// STEP3:  unpack Try contents!
def translateTryToContents(origResult:Try[String]): String = {
  origResult match {
    case Failure(ex) =>
      ex.getMessage()
    case Success(greeting) =>
      greeting
  }
}
// STEP4: create an array of inputs; THEN BATCH results with an array on invoking function!
val inputs = List("Bad Kitty", 3.1415)
val origOutputs = inputs.map(letMeSayHello)

// STEP5:  translate contents of Try BATCH results and print them!
val displayResults:List[String] = origOutputs.map( translateTryToContents )

// STEP5: display results w for each!
displayResults.foreach(println)


