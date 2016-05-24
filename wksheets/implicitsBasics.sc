
// TODO:  IMPLICITs are like Magic global variables
//        - HOW to DEBUG-SHOW IMPLICITs being used?
//        - HOW to OVERRIDE global implicit variable;
//          COMPILER will catch ambiguity if you have more than one active
//          within the compilation unit!
/*

Implicits are difficult to understand because they have many different uses.
In an earlier post, we looked at implicit parameters and type tags.
Now, we’ll take a look at another usage pattern every Scala programmer sees:
implicits as an alternative to passing the same argument over and over.
Scala Futures use implicit parameters in this way.

 */
// http://engineering.monsanto.com/2015/05/14/implicits-intro/
// http://engineering.monsanto.com/2015/06/15/implicits-futures/

// NOTE:  compiler-deduced TypeTag to carry INTO nested method; WITHOUT affecting caller syntax!
import scala.reflect.runtime.universe._

def getInnerType[T](list:List[T])(implicit tag:TypeTag[T]) = tag.tpe.toString

val genericList: List[String] = List("A")
val type1Name = getInnerType(genericList)
// NOTE:  special String formatting of typed variables!
println( s"Genericlist1 is a list of $type1Name")

// NOTE:  WITHOUT implicit-pass-thru for arglist; will get compilation error!
// ERROR, no TypeTag available for T; not enough arguments for getInnerType!
// def gratuitousIntermediateMethod[T](list:List[T]) = getInnerType(list)
// SOLUTION:  add implicit!
def gratuitousIntermediateMethod[T](list:List[T])(implicit tag:TypeTag[T]) = getInnerType(list)
val type2Name = gratuitousIntermediateMethod(genericList)
println( s"GenericList2 is a list of $type2Name")

