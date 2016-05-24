
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

/*

  (from Scala in Depth; Section 2.4:  Using None instead of Null)
  http://alvinalexander.com/scala/scala-null-values-option-uninitialized-variables

  OPTION vs NULL
  - most often used as OPTIONAL function input parameter
  - typed COLLECTION of ONE(Some) or EMPTY(None)
  - can construct via Option Factory
  - can access internal VALUE with DEFAULT x.getOrElse("default")
    e.g. map.getOrElse(...) vs throwing a NoSuchElementException
    with regular .get(...)
  - can operate as on a List collection via map, flatMap, foreach
  to PIPE functional operations on EACH element in List;
  terminating with getOrElse("default") if no input was supplied!
  - NOTE:  for...loops can:

    - embed conditional logic that SWITCHES on Option.SOME,
      and does nothing on Option.NONE
    - embed for-comprehension that GENERATES results per item, like MAP on
      a Lambda function
    - run through MULTIPLE, PARALLEL generators

 */

/*
    Option returned from Map:


    http://stackoverflow.com/questions/9389902/scala-mapget-and-the-return-of-some

    Im using scala Map#get function, and for every accurate query it returns as Some[String]
    IS there an easy way to remove the Some?

    *** the simplest and riskiest way is this:

mymap.get(something).get
Calling .get on a Some object retrieves the object inside. It does, however, give you a runtime exception if you had a None instead (for example, if the key was not in your map).

A much cleaner way is to use Option.foreach or Option.map like this:

scala> val map = Map(1 -> 2)
map: scala.collection.immutable.Map[Int,Int] = Map(1 -> 2)

scala> map.get(1).foreach( i => println("Got: " + i))
Got: 2

scala> map.get(2).foreach( i => println("Got: " + i))

scala>
As you can see, this allows you to execute a statement if and only if you have an actual value. If the Option is None instead, nothing will happen.

Finally, it is also popular to use pattern matching on Option types like this:

scala> map.get(1) match {
     |  case Some(i) => println("Got something")
     |  case None => println("Got nothing")
     | }
Got something

 */



/*
    EXPERIMENTS with Basic Options
 */
val someOption: Option[String] =  Some("this is a value")
val noneOption: Option[String] = None
try {
  val theSomeValue = someOption.get // returns "this is a value"
  val someIsDefined = someOption.isDefined // returns true
  val theNoneValue = noneOption.get // throws NoSuchElementException
  val noneIsDefined = noneOption.isDefined // returns false

} catch {
  case nse: NoSuchElementException =>
    println("YAY!  Detected NONE Option on referencing it!")
  case _: Any =>
    println("Should not get here!")
}


/*
    EXPERIMENTS with HashMap and Options
 */
// setup the MAP; where Values per key are Some(Type), or None(Type); NEVER Null!
val relativesLikedBy = scala.collection.mutable.HashMap[String, List[String]]()
relativesLikedBy += "Cartman" -> List("Mom LaFleur", "Grandma Trudeau")
relativesLikedBy.put("Kyle", List("Cousin Annie"))
relativesLikedBy.getOrElseUpdate("Stan", List("Mom LaFleur", "Grandma Trudeau", "Uncle Guy", "Cousin Annie"))
// NOTE:  print TRICK for Map!
println("DEBUG MAP CONTENTS of Relatives Liked By ALL Travellers:  ")
relativesLikedBy.foreach( println )
println

// APPROACH1:  ACCESS the Map via Options List!
val optionCartmansFaveRelatives = relativesLikedBy.get("Cartman")
optionCartmansFaveRelatives.foreach(
    allLikedRelatives => {
      println("DEBUG:  UNPACKING Cartman's Liked Relatives List => %s".format(allLikedRelatives))
    }
)

// APPROACH2:  ACCESS the Map directly!
println("DEBUG:  DIRECT ACCESS with RISKY None Exception for Cartman's Liked Relatives List => %s".format(optionCartmansFaveRelatives.get))

/*
    EXPERIMENTS with Map, Flatmap, Filter ops on List of OPTIONS!
 */

val winePairings: Map[String, String] = HashMap(("ribeye", "cabernet"), ("turkey", "pinot noir"), ("sea bass", "sauvignon blanc"))
val theMeat : Option[String] = Some("ribeye")

// NOTE:  MAP operation DOES CASE MATCH via unwrap the option to specific content!
// processing meat available with wine pairing
theMeat.map { (meat: String) => winePairings.get(meat) }

val theWineToPairWithTodaysMeat1 = theMeat.map { (meat: String) => winePairings.get(meat) }
// returns Some(Some(cabernet sauvignon)) -- whoops!

val theWineToPairWithTodaysMeat2 = theMeat.flatMap { (meat: String) => winePairings.get(meat) }
// returns Some(cabernet sauvignon)

val todaysDessertSpecial:  List[Option[String]] = List( Some("donut"), Some("ice cream"), Some("flan"), Some("financier"), Some("chocolat pot de creme"), None )
// NOTE:  filter for case match on option does NOT UNWRAP content!
val myOrder = todaysDessertSpecial.filter(
  (aSpecial:  Option[String]) => (aSpecial == Some("flan"))
)

/*
    PIPELINE operations on function RETURN
*/
val foundFood: Option[String] = None
val tonitesDinner = foundFood.getOrElse("takeout Pizza")
println("Tonight's Dinner will be:  %s".format(tonitesDinner))

/*
    ATTENTION!
    WRAP in Try(...) actually catches Exception,
    allows return of COLLECTION of RESULTS of Success/Failure with Exception
*/

/*
    MONAD:
    - supports map, flatMap with consistent results
    - associative/commutative
    - parallelizable
    - order of ops doesn't matter, grouping doesn't matter to RESULT
 */



