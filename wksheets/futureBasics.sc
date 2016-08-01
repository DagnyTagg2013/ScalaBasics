// TODO:  Akka Docs
// - starving threads!

// TODO:
// - difference between Play Promise and Scala Promise!


/*

REFERENCES:  Baseline Futures, Promises

Future is the event PUBLISHER
Promise is the event SUBSCRIBER

***** in other words, by returning a Promise you are giving the right to complete it to somebody else,
and by returning the Future you are giving the right to subscribe to it.



WHEN TO USE:
- blocking I/O from DB connections
- long-running calculations
- Network calls (e.g. remote Http Requests)


- Manning's Reactive Web Applications
    - Ch3, p 55; Functional Programming 101
    - Ch5, p 130; FUTURES
- http://engineering.monsanto.com/2015/06/15/implicits-futures/
- http://danielwestheide.com/blog/2013/01/09/the-neophytes-guide-to-scala-part-8-welcome-to-the-future.html
- http://danielwestheide.com/blog/2013/01/16/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice.html
- http://docs.scala-lang.org/overviews/core/futures.html
- http://alvinalexander.com/scala/future-example-scala-cookbook-oncomplete-callback
- http://doc.akka.io/docs/akka/snapshot/scala/futures.html

*/

/*

  INTRO to FUTURES:

  The gist of it is that a Future contains a value that may or may not have been computed yet.
  Futures let us spin off work into other threads,
  chain/pipeline more operations that should be performed on the result,
  define what should happen after failure,
  and (if we really must) wait (block, or not) for the operation to complete.

  Everything we do asynchronously happens on some other thread upon DECLARATION/CONSTRUCTION!
  Creating a future,
  adding operations after success,
  adding failure handling –
  in each case, we need to tell it what thread to run on.
  The futures library lets us specify this using implicit parameters.

  Also, the Future type only provides an interface for reading the value to be computed.
  The task of writing the computed value is achieved via a Promise.

  Where Future provides an interface exclusively for querying,
  Promise is a companion type that allows you to complete a Future
  by putting a value into it. This can be done exactly once.
  Once a Promise has been completed, it’s not possible to change it any more.

*/

/*
  BLOCKING marker:
  There’s a blocking marker that allows you to tell the ExecutionContext that a certain portion of code is blocking.
  This is useful in that the ExecutionContext will then be able to respond appropriately to this situation,
  for example by creating more threads (in the case of a fork-join ThreadPool).
  The other advantage to using this marker is also that it becomes clear to other developers (as well as to your future self)
  that a given portion of code is blocking.
  e.g.
  Future {
    blocking {
      new java.io.File(path).exists
    }
  }
 */

/*

EXECUTION CONTEXT as Implicit Params!

ExecutionContexts everywhere. They’re important, and sometimes we need to be specific about where each operation should run,
but the common case is that they can all run in the same pool.
It is tedious, cluttered, and error-prone to repeat that same bit of information over and over.

This implicit-parameter-supplying feature only works if there is exactly one value of the needed type in the compiler’s magic hat
when the method that declares the implicit parameter is called.
If none are available, you get that “Cannot find an implicit” compile error.
If more than one are available, you get an “ambiguous implicit” error

Read the following to see how to properly instantiate a CUSTOM ExecutionContext with ThreadPool, rather than the default one!
http://stackoverflow.com/questions/31030119/blocking-keyword-in-scala

import java.util.concurrent.Executors
import scala.concurrent._
val ec = scala.concurrent.ExecutionContext.Implicits.global
val executorService = Executors.newFixedThreadPool(4)
val ec2 = ExecutionContext.fromExecutorService(executorService)

// NOTE:  passing custom ec into second paren implicit param list!
val aFut = Future(blocking(someBlockingCallCode))(ec2)

 */

/*

BUILD DEPENDENCIES!

NOTE1:  on Activator; ONLINE, create seed or template application to determine what to import to access Play-Scala!

NOTE2:  in IntelliJ; enter entry in *.sbt to download Play dependencies!
to insert Play dependencies
- http://stackoverflow.com/questions/27057884/scala-play-framework-dependency-in-clean-sbt-project
- open *.sbt; and top RHS AutoEnable dependency imports; and Refresh the build to download!
- https://www.jetbrains.com/help/idea/2016.1/getting-started-with-sbt.html#add_refresh_dependencies
- import scala.concurrent.{ ExecutionContext, Promise }
- https://www.playframework.com/documentation/2.5.x/ScalaWS
- reference build.sbt for related packages to import!

*/


// NOTE:  necessary to bring current Application in scope to access Play
// http://stackoverflow.com/questions/18834723/you-do-not-have-an-implicit-application-in-scope-playframework-with-oracle
// TODO:  what's NOT deprecated way to do this?
import play.api.Play.current

import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.forkjoin._
import scala.concurrent.duration._

import play.api.libs.concurrent.Promise
import scala.concurrent.duration._

import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString


import scala.concurrent.ExecutionContext

import java.util.concurrent.Executors
import scala.concurrent._

import scala.util.{Try, Success, Failure}

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
JAVA vs SCALA
        on Java
        - java.util.concurrent.Future<V> lets one check if Future is done, or block with a get() method
        (another variety doesn't block)
        on Scala
        - starts running on declaration
        - doesn't block calling thread
        - callbacks registered to handle EITHER failure or success
*/

/*

    EXAMPLE:  ASYNC calls to external web service!

 */

// ATTENTION: access IMPLICIT VAL; DEFAULT global execution context
// PREFER to create separate contexts per type of Worker per characteristics e.g. DB access or NETWORK access
implicit val ec =  scala.concurrent.ExecutionContext.Implicits.global

/* ALTERNATIVELY:
import scala.concurrent.ExecutionContext.Implicits.global
Inside scala.concurrent.ExecutionContext.Implicits, global is an implicit val, so into the magic hat it goes.
Adding this to the top of the file chooses the default execution context for asynchronous operations.
 */

 // initiates async call
 val response:  Future[WSResponse] = {  blocking { WS.url("http://www.playframework.com").get() } }

  // CHAIN-PIPELINE transform on array of ONE result once available
  val isOnlineResults: Future[Boolean] = response.map{ r => r == 200 }

    // process results on generic Monad-ish return collection
    isOnlineResults.foreach { isOneOnlineResult =>
      // TODO:  test if this is recommended way to unpack Monad!
    if (isOneOnlineResult.asInstanceOf[Boolean] ) {
      println("The Play site is up")
    }
    else {
      println("The Play site is down")
    }

  }

    // NOTE:  composing transform on dependency on MORE than one async result
    // with a For Comprehension

    // We declare the Futures outside of the for comprehension.
    // This is because Futures start to run as soon as it is declared

    def isSiteUp(url: String): Future[Boolean] =
      // NOTE:  the following is an HTTP GET operation, rather than a Future blocking get() operation!
      WS.url(url).get().map { rslt:WSResponse => rslt == 200 }


    // NOTE:  Declare MULTIPLE Futures OUTSIDE of the for...comprehension,
    //        so that parallel execution will occur; otherwise second won't run until first finishes
    val isPlayMainUp =
      isSiteUp("http://www.playframework.com")

    val isPlayGitUp =
      isSiteUp("https://github.com/playframework")

    // using for ... comp to run Futures concurrently
    // yield conditional WAITs-JOINs until BOTH have returned and we can do a dependent task!
    val allSitesAvailable: Future[Boolean] = for {
      isEachOnePlayMainUp <- isPlayMainUp
      isEachOnePlayGitUp <- isPlayGitUp

    } yield (isEachOnePlayMainUp && isEachOnePlayGitUp)

    // processing competed Future to result by MATCHING on resulting Try values!
    //  Try can be thought of as a container type that results in either a Success(value) or a Failure(exception)
    // TODO:  does Play ws automatically WRAP the Promise results part for the WS call?
    // TODO:  does this code have to run WITHIN a Web Server instance; or is that instantiated under-the-hood by importing the Play libraries?
    allSitesAvailable.onComplete {
          case Success(isAllSitesUp) => println( s"Is All Sites Up $isAllSitesUp" )
          case Failure(ex) => println(ex.getMessage())
    }

// HANDLE ERROR-RECOVERY en BATCH via case pattern-matching; CHAINED onto RESULTs of the above operation!
// (see errorHandlingBasics.sc)
// TODO: verify that Future method instiantiated for its apply() method is auto-wrapped in Try   THEN, do a recover(...) on the original Try-encapsulated result of that blocking function!
// TODO:  How to setup the Finite State Machine functional CHAINING/PIPELINING based on BRANCHING from on Success vs Failure switches at each node?
val overallAvailability:Future[Option[Boolean]] =
      allSitesAvailable.map {
        availItem => Option(availItem)
      }.recover {
        case ce: java.net.ConnectException => None
      }


/*

HANDLE TIMEOUTS!

- define how long we are willing to wait for a service to answer
- define alternative response in case of time out
- allow client to respond appropriately (by retrying call after some delay)

=> Page 130 of Manning Reactive Web Applications

 */

// TODO:  lookup usage of .timeout Play Promise API vs Scala Promise!
// TODO:  lookup usage of Play Action!
val timeoutFuture = Promise.timeout("Authentication service unresponsive", 2.seconds)

/*
Future.firstCompletedOf(
  Seq(authentication, timeoutFuture)
).map {
  ...
}
*/

/*

  PARALELLIZING FUTURES
  *********************
  - Fig. 5.9 on Pg 136 of Manning Reactive Web Applications

  COMPOSING FUTURES
  ******************
  - Section 5.2.3 on Pg 142 of Manning Reactive Web Applications
  - declare futures independently, OUTSIDE of for...loop for them to START execution indepedently
  - combine multiple future actions in for ... yield to run concurrently, then combine results to YIELD a TUPLE
  - invoke FLATMAP to generate Future Output on a Future Input WITHOUT result in NESTED futures!
  - for...yield with _ <- abc means we just want abc to be executed; and don't care about the results

  PROPAGATING AND HANDLING ERRORS
  *******************************
  - Section 5.2.3 on Pg 143

  EXAMPLE ERRORS
  - cognizant of timeouts!
  - DB not reachable
  - Twitter API not reachable due to network problem, or unauthorized credentials
  - User doesn't exist on Twitter
  ***** NOTE:  Future returns with RESULTs OPTIONS for Success/Failure(Ex); or Try type automatically!
  SO, can recoverWith( ...)

  - use RECOVERWITH to try retries, etc
  - use RECOVER to catch multiple exception types with cases; THEN wrap them in a unified type!
  - LAST resort is to return Future.failed Try!

  eg1
  => Page 125 of Manning Reactive Web Applications

  // TODO:  lookup usage of .timeout Play Promise API vs Scala Promise!
  // TODO:  lookup usage of Play Action!
  val timeoutFuture = Promise.timeout("Authentication service unresponsive", 2.seconds)
  Future.firstCompletedOf(
    Seq(authentication, timeoutFuture)
  ).map {
    ...
  }

  eg2
  => page 143 of Manning Reactive Web Applications
  val result = for {
    _ <- storedCounts
    _ <- publishedMessage
  } yield {}


  eg3
  => page 146 of Manning Reactive Web Applications
  result recoverWith {
    case CountStorageException(countsToStore) => retryStoring(...)
   } recover {
    case A =>
      throw ...
    case B =>
      throw ...
    case Nonfatal(t) =>
      throw StatisticsServiceFailed(...)
      ...
   }

 */

/*

TODO
- need async Scala Cassandra driver API with FUNCTIONAL Error returned;
  eg PHANTOM
- NOTE how typed collection is retrieved via collection.one[StoredCounts]
- NOTE how error or exception is thrown from DB Driver
  MAY need to catch NonFatal exceptions to rethrow our own custom application RuntimeException!
  where FATAL errors include:
  - OutOfMemoryError
  - StackOverflowError
- NOTE how to find record not found case; MAY generate DEFAULT
  via Scala .getOrElse(...) method!
*/

/*

TESTING FUTURES!
- Page 132 of Manning Reactive Web Applications; using specs2 tool; having test extend Specification
- caveat of using .await(numRetries, timeoutInMS)

*/


