package Driver

import DP.{Point, DynamicPathfinder}

import scala.collection.immutable.Map
import scala.collection.mutable.ListBuffer

// ATTN: precompile dependencies before accessing via script!
// - http://stackoverflow.com/questions/12028590/scala-not-a-member-of-package
// ATTN: println
// - http://stackoverflow.com/questions/7219316/println-vs-system-out-println-in-scala

object Driver extends App {

  // INITIALIZE Blocked Points
  println("TEST 1")

  // ******* TEST 1:  BLOCKED PATH ***********
  // ATTN:  initializing a LIST
  // - http://alvinalexander.com/scala/how-create-scala-list-range-fill-tabulate-constructors
  // ATTN:  initializing a MAP
  // -  http://alvinalexander.com/scala/scala-maps-map-class-examples

  // INITIALIZE Blocked Points
  println("TEST 1")
  val blockedSpots1: Map[Point, Boolean] = Map(Point(1, 1) -> true,
                                               Point(2, 2) -> true,
                                               Point(3, 3) -> true)

  val aDynamicPathfinder1: DynamicPathfinder = new DynamicPathfinder(3, blockedSpots1)
  val partialPath1: ListBuffer[Point] = new ListBuffer[Point]()
  var isFoundPath1: Boolean = aDynamicPathfinder1.getPath(3, 3, partialPath1)
  if (isFoundPath1) {
    println("FOUND Path is:  ")
    // ATTN: printing elements of list!
    println(partialPath1.foreach(println))
  }
  else {
    println("Path NOT Found!")
  }

  // ****** TEST 2: VALID PATH ***************
  // INITIALIZE Blocked Points
  println("\nTEST 2")
  val blockedSpots2: Map[Point, Boolean] = Map(Point(1, 1) -> true,
    Point(2, 2) -> true)

  val aDynamicPathfinder2: DynamicPathfinder = new DynamicPathfinder(3, blockedSpots2)
  val partialPath2: ListBuffer[Point] = new ListBuffer[Point]()
  var isFoundPath2: Boolean = aDynamicPathfinder2.getPath(3, 3, partialPath2)
  if (isFoundPath2) {
    println("FOUND Path is:  ")
    println(partialPath2)
  }
  else {
    println("Path NOT Found!")
  }
}
