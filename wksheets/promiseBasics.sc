import scala.concurrent.{Future, Promise}
import scala.util.{Success, Failure}

/*

  PROMISE basics:   where design pattern is Producer-Consumer and FUTURE is the Consumer/Reader,
                    but PROMISE is the Producer/Writer which can WRITE-ONCE only!

  http://danielwestheide.com/blog/2013/01/16/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice.html

  http://stackoverflow.com/questions/18960339/clarification-needed-about-futures-and-promises-in-scala

  ALSO NOTE:  Singletons via object
  http://docs.scala-lang.org/tutorials/tour/singleton-objects.html

 */

/*

In the previous article on futures, we had a sequential block of code that we passed to the apply method of the Future companion object,
and, given an ExecutionContext was in scope, it magically executed that code block asynchronously, returning its result as a Future.

While this is an easy way to get a Future when you want one, there is an alternative way to create Future instances and have them complete
with a success or failure. Where Future provides an interface exclusively for querying,
Promise is a companion type that allows you to complete a Future by putting a value into it.
This can be done exactly once. Once a Promise has been completed, it’s not possible to change it any more.

A Promise instance is always linked to exactly one instance of Future. If you call the apply method of Future again in the REPL,
you will indeed notice that the Future returned is a Promise, too:

The object you get back is a DefaultPromise, which implements both Future and Promise.
This is an implementation detail, however. The Future and the Promise to which it belongs may very well be separate objects.

What this little example shows is that there is obviously no way to complete a Future other than through a Promise
– the apply method on Future is just a nice helper function that shields you from this.


 */

/* EXAMPLE1:  1:1 relationship between Future and Promise!  */
val f: Future[String] = Future { "Hello world!" }
// REPL output:
// f: scala.concurrent.Future[String] = scala.concurrent.impl.Promise$DefaultPromise@793e6657


/*
     EXAMPLE2:  Create a Promise, and it will have a default associated Future with it!
                eg Politician creates a Promise for Future TaxCuts once elected!
*/
case class TaxCut(reduction: Int)

// either give the type as a type parameter to the factory method, and the compiler can INFER the val type accordingly!
val taxcutP1 = Promise[TaxCut]()
// OR give the compiler a hint by specifying the type of your val:
// val taxcutP2: Promise[TaxCut] = Promise()

// NOW, you can retrieve the 1:1 associated Future companion object!
val taxcutF1: Future[TaxCut] = taxcutP1.future

/*
   NOTE:  The returned Future might not be the same object as the Promise,
   but calling the future method of a Promise multiple times will definitely always
   return the same object to make sure the one-to-one relationship
   between a Promise and its Future is preserved!

 */

/* NOTE:  lets define how we PUBLISH the Promise */
case class LameExcuse(msg: String) extends Exception(msg)

// ATTENTION:  instantiation of singleton object for invocation of function member in its namespace!
// ATTENTION:  instantiation of Promise, supplying embedded future! SO Promise is NOT completed on the caling thread!
object Government {
  def redeemCampaignPledgeWell(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
            Future {
              println("Starting the new legislative period.")
              Thread.sleep(2000)
              // NOTE:  COMPLETION of promise PUSHES data onto this complementary Future async op!
              p.success(TaxCut(20))
              println("We reduced our taxes! You must reelect us!!!!")
            }
    p.future
  }
  def redeemCampaignPledgePoorly(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
                    Future {
                      println("Starting the new legislative period.")
                      Thread.sleep(2000)
                      // NOTE:  COMPLETION of promise PUSHES data onto this complementary Future async op!
                      p.failure(LameExcuse("global economy crisis"))
                      println("We didn't fulfill our promises, but surely they'll understand.")
                    }
    p.future
  }
}

/*

TODO:  so Try can wrap a Future itself,
       and this done automatically when Try block is placed around code block for Promise?


If you already have a Try, you can also complete a Promise by calling its complete method.
If the Try is a Success, the associated Future will be completed successfully,
with the value inside the Success.
If it’s a Failure, the Future will completed with that failure.

*/

/*

NOTE: Below operation is done ASYNC; and COMPLETES the Promise!  NOTE:  that Promise ISA Future;
and is run from a SEPARATE thread ff=rom this calling thread!

If you try this out multiple times, you will see that the order of the console output is not deterministic.
Eventually, the completion handler will be executed and finally run into the success case.

HOWEVER, FUTURE COMPLETES on the calling Client thread; and its data gets marshalled there!

NOTE:  that PROMISE is a COMPANION object to a Future; although Future can have operation that just runs WITHOUG
creating completable a Promise; eg call out to remote Web Service!

*/
val taxCutWellF: Future[TaxCut] = Government.redeemCampaignPledgeWell()

val taxCutFailF: Future[TaxCut] = Government.redeemCampaignPledgePoorly()

/*
    NOW, we setup handlers (NON-BLOCKING, PREEMPTABLE) from calling thread;
    on the ASYNC RETURN from each Future!

    The most general form of registering a callback is by using the onComplete method,
    which takes a callback function of type Try[T] => U. The callback is applied to the value of type Success[T]
    if the future completes successfully, or to a value of type Failure[T] otherwise.

 */

println("Now that they're elected, let's see if they remember their promises...")
taxCutWellF.onComplete {
  case Success(TaxCut(reduction)) =>
    println(s"A miracle! They really cut our taxes by $reduction percentage points!")
  case Failure(ex) =>
    println(s"They broke their promises! Again! Because of a ${ex.getMessage}")
}


/*
   TODO:
   - DECOUPLE Producer from Consumer; AND allow Streaming-Change of Promise fulfillments!
*/


