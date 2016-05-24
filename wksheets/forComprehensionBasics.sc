// TODO:  find out IMMUTABLE, FUNCTIONAL way to do this?
// http://www.ybrikman.com/writing/2013/05/31/10-recipes-for-turning-imperative-java/

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

/*

START WITH A LIST!
http://alvinalexander.com/scala/how-create-scala-list-range-fill-tabulate-constructors

GETTING STARTED with FUNCTIONAL PROGRAMMING in SCALA
http://nerd.kelseyinnis.com/blog/2013/01/07/resources-for-getting-started-with-functional-programming-and-scala/

IDIOMATIC SCALA with For Comprehensions!
http://nerd.kelseyinnis.com/blog/2013/11/12/idiomatic-scala-the-for-comprehension/
http://docs.scala-lang.org/tutorials/FAQ/yield.html

SCALA YIELD vs PYTHON YIELD!
http://docs.scala-lang.org/tutorials/FAQ/yield.html

DECLARING VALs and VARs!
http://www.informit.com/articles/article.aspx?p=1849235&seqNum=2
http://www.scala-lang.org/files/archive/spec/2.11/04-basic-declarations-and-definitions.html

FUNCTIONAL WAY TO USE HASHMAP:
http://docs.scala-lang.org/overviews/collections/concrete-mutable-collection-classes.html
http://docs.scala-lang.org/overviews/collections/maps.html
https://www.cs.helsinki.fi/u/wikla/OTS/Sisalto/examples/html/ch17.html#sec3

 */

// ****************************************************************************
/*

      SENDING POSTCARDS from each visited State to Relatives

      VERSION 0: OLD JAVA-ish way
      - boilerplate for loop
      - non-reusable code within loop
      - mutable array data
      - NESTED for loops

 */

// NOTE:  instead of Object, can use Any, AnyRef, AnyVal
// NOTE:  val is constant REFERENCE vs var of changeable reference
//        BUT of MUTABLE type contents!
// NOTE:  IMPLICIT types for i, j

/*
val statesVisited = ListBuffer.empty[String]
statesVisited += "Arizona"
statesVisited += "California"
statesVisited += "New York"
statesVisited += "Alaska"

val relatives = ListBuffer[String]("Mom Eng", "Uncle Rex", "Grandma Eng", "Cousin Jen")

def sendPostcards = {
  for (i <- 0 until statesVisited.length) {
    val currentState = statesVisited(i)
    for (j <- 0 until relatives.length) {
      val currentRelative = relatives(j)
      println("Dear " + currentRelative + ", " + "Wish you were here in:  " + currentState + "!\n")
    }
  }
}

sendPostcards

*/

// ******************************************************************************
/*
      FUNCTIONAL REFACTOR
      VERSION 1:
      - introduce IMMUTABLE collections for stuff which doesn't change
      - introduce MUTABLE collections for stuff which DOES change; then convert to IMMUTABLE
      - introduce PostCards collection as OUTPUT RESULT of operations pipeline;
        with saved Objects INSTEAD of print
      - use MAP as function/alternate to explicit iteration on a LIST
      - NESTED functions with MAP to REPLACE Nested Loops!
      - apply customizable input FUNCTION object as function to EACH item of LIST
      - leverage LIST map, flatten, println support
      http://alvinalexander.com/scala/scala-list-class-examples
 */
val travellers = List("Cartman", "Kyle", "Stan")
//val travellers = List("Kyle")
val statesVisited = List("Arizona", "California", "New York", "Alaska")
//val statesVisited = List("Arizona")
val relatives = List("Mom LaFleur", "Uncle Guy", "Grandma Trudeau", "Cousin Annie")
//val relatives = List("Uncle Guy", "Cousin Annie")

def writeAPostcard (relative:  String, stateVisited: String): String = {
  val formMessage : String = "Dear %s, Wish you were here in:  %s!"
  val customMessage = formMessage.format(relative, stateVisited)
  customMessage
}

/*
def writePostcards1 (statesVisited: List[String], relatives:  List[String]): List[String] = {

  // anonymous function to apply to each element in relatives
  relatives.map { (aRelative: String) =>

    // CHAINED, as nested function to apply to each element in states
    statesVisited.map { (aState: String) =>

      // uses BOTH iteration values to apply function to combination
      writeAPostcard(aRelative, aState)

    }

  // nested maps result in nested List of List results, and we want a FLAT list instead
  }.flatten

}

val postCards = writePostcards1(statesVisited, relatives)
// NOTE:  print TRICK for List of Strings!
postCards.foreach( println )

*/

// ******************************************************************************
/*
      FUNCTIONAL REFACTOR
      VERSION 2:
      - map and flatten can be condensed to flatmap!
*/

/*

def writePostcards2 (statesVisited: List[String], relatives:  List[String]): List[String] = {

  // anonymous function to apply to each element in List of relatives; then flatmap to condense List of Lists!
  relatives.flatMap { (aRelative: String) =>

    // CHAINED, as nested function to apply to each element in List of states
    statesVisited.map { (aState: String) =>

      // uses above, BOTH iteration values to apply function to combination
      writeAPostcard(aRelative, aState)

    }

  }

}

val postCards2 = writePostcards2(statesVisited, relatives)
// NOTE:  print TRICK for List of Strings!
postCards2.foreach( println )

*/

// ******************************************************************************
/*
      FUNCTIONAL REFACTOR
      VERSION 3:
      - FOR COMPREHENSION; LAST line is MAP, whereas each line ABOVE is a FLATMAP!
      therefore, NICE condensed syntax for nested For Loops!
      - YIELD GENERATES output result element for OUTPUT results LIST
      - if-conditional FILTERS on input
      - assignment-equals to assign to temp variable based on CURRENT iteratee in for loop item

      - TODO:  functional way to use hashmap IMMUTABLE structure?
      http://docs.scala-lang.org/overviews/collections/concrete-mutable-collection-classes.html
      http://docs.scala-lang.org/overviews/collections/maps.html
      https://www.cs.helsinki.fi/u/wikla/OTS/Sisalto/examples/html/ch17.html#sec3
*/
val relativesLikedBy = scala.collection.mutable.HashMap[String, List[String]]()
relativesLikedBy += "Cartman" -> List("Mom LaFleur", "Grandma Trudeau")
relativesLikedBy.put("Kyle", List("Cousin Annie"))
relativesLikedBy.getOrElseUpdate("Stan", List("Mom LaFleur", "Grandma Trudeau", "Uncle Guy", "Cousin Annie"))
// NOTE:  print TRICK for Map!
println("DEBUG MAP CONTENTS of Relatives Liked By ALL Travellers:  ")
relativesLikedBy.foreach( println )
println

// NOTE:  trickiness when retrieving Map options!
//println("DEBUG List that Kyle likes:  ")
//val likedByKyle = relativesLikedBy.get("Kyle").get
//likedByKyle.foreach( println )
//println("DEBUG does Kyle like Annie  ")
//val isKyleLikesAnnie = likedByKyle.contains("Cousin Annie")
//println(isKyleLikesAnnie)

def writeAPostcard2 (relative:  String, stateVisited: String, traveller:  String): String = {
  val formMessage : String = "Dear %s, Wish you were here in:  %s!  Love, %s"
  val customMessage = formMessage.format(relative, stateVisited, traveller)
  customMessage
}

// NOTE:  Had to change return List type to Any to handle OPTIONS!
def writePostcards3 (travellers:  List[String], statesVisited: List[String], relatives:  List[String]): List[String] = {
  for {

    aTraveller <- travellers
    postcardSender = aTraveller + " (your favorite)"
    aRelative <- relatives
    aState <- statesVisited
  } yield {

    // NOTE:  how to access Option SOME element from a MAP, and does NOTHING for None
    // TODO:  check that Yield does NOT generate dummy None element in this case!
    val optionLikedRelatives = relativesLikedBy.get(aTraveller)

    // println("DEBUG:  %s".format(optionLikedRelatives))

    // TODO:  need to reference this accumulated result LATER in this OUTER scope, so check if OK to use var!
    var aPostcardFromATravellerToOneLikedRelative: String = "NOTHING"

    // NOTE:  iterates over option list of ONE element, where Some(List[String]) or None is permitted as Map value
    // ForEach UNWRAPS enclosed List element from Some()
    optionLikedRelatives.foreach(

          allLikedRelatives => {

            // println ("DEBUG:  likedRelativesList %s".format(allLikedRelatives))

            // ATTENTION:  this essentially does a MATCH operation to FILTER aRelatives ONLY that are liked by current aTraveller!
            // ATTENTION:  OK to use O(N)  List.contains() for FEW number of relatives!
            if ( allLikedRelatives.contains(aRelative) ) {

              // println ("DEBUG:  IS %b if likedRelativesList %s contains aRelative %s".format(allLikedRelatives.contains(aRelative), allLikedRelatives, aRelative))

              // uses BOTH iteration values to apply function to combination
              // println("DEBUG written postcard:  ")
              aPostcardFromATravellerToOneLikedRelative = writeAPostcard2(aRelative, aState, aTraveller)
              // println(aPostcard)

            } // END process ONE liked relative

            // ELSE, does NOTHING, BUT initialized NOTHING gets propagated to yielded results!

          } // END process ONE liked relative

    )  // END processing of encapsulated List of Liked Relatives in SOME Map value

    // NOTE:  generates result to store in yielded for..comp results list!
    // ATTENTION: in the case of a NON-liked relative, will retain "NOTHING" value!
    // TODO:  need easier way to process OPTION from Map WITHIN for clause to FILTER results BEFORE generating dummy NOTHING result values!
    aPostcardFromATravellerToOneLikedRelative

  } // END yield result

}
val postCards3:List[String] = writePostcards3(travellers, statesVisited, relatives)
// NOTE:  print TRICK for List of Strings!
postCards3.filter(_ != "NOTHING").foreach( println )