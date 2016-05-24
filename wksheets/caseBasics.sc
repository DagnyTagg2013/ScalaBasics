


/* 'CASE basics' => class type-based SWITCH
                 => allows centralized logic handling based on type VS Java
                    class hierarchy with vtable switch on virtual function overrides
                 => more powerful pattern-match and variable-parse-binding capabilities
*/

/*

    REFERENCES:   from Artima Developer site
    Chapter 15 of Programming in Scala, First Edition
    Case Classes and Pattern Matching
    by Martin Odersky, Lex Spoon, and Bill Venners

 */

// - AUTOMATICALLY generates support for hashCode, deep ==,
// factory ctor, ctor args become private mbmers
// - similar to Java switch
// BUT no 'fall-through', no 'break'
// need ALL cases accounted for; so case _: DEFAULT
// - match expression matches on class TYPE, and can PARSE out
//   variables in match
// - matching on collection type:  can do for Array[Int], NOT for Map[String, Int] b/c
//   type erasure at runtime
// - variable binding:
//   case UnOp("abs", e @ UnOp("abs", _)) => e c
// - can add SEALED to BASE class hierarchy so that compiler will check ALL combos;
//   BUT if want to suppress this, add (x:@unchecked) match {...}
// SEALED CLASS
// - a sealed class cannot have any new subclasses added except the ones in the same file
// RECURSION USAGE PATTERN
// - can match on EXIT/END recursive case; and the ITERATIVE case

// e.g.
sealed abstract class Expr
case class Var(name: String) extends Expr
case class Number(num: Double) extends Expr
case class UnOp(operator: String, arg: Expr) extends Expr
case class BinOp(operator: String, left:Expr, right:Expr) extends Expr
def describeExpression(x: Any) =
  (x:@unchecked) match {
       case BinOp(op, left, right) =>
         println(x + " is a binary op with args left:  " + left + " and" + " right:  " + right)
       case 5 =>
         println(x + " is constant 5.")
       case _ =>
         println(x + " is an unrecognized pattern.")
     };

// CASE 1:  Match constant
describeExpression(5)

// CASE 2:  Match expression, parse elements
val one = Number(1)
val two = Number(2)
val OnePlusTwo = BinOp("+", one, two)
describeExpression(OnePlusTwo)

// CASE 3:  DEFAULT match and msg!
val OneIncrement = UnOp("+", one)
describeExpression(OneIncrement)


/*
    EXAMPLE USAGE with RECURSION:

    def recurseInsertInAscendingOrder(newMergedTree: CodeTree, workingTreeListSortedAsc: List[CodeTree]): List[CodeTree] = {

    // println("--- DEBUG TRACE:  called INTO RECURSE INSERT IN ASC ORDER method, with input of:  ")
    // println(newMergedTree.toString)
    // println(workingTreeListSortedAsc.mkString)

    // handle HEAD case
    val result:List[CodeTree] = workingTreeListSortedAsc match {

      // for this case,newMergedTree is inserted at the END of the final sorted list
      case Nil => List(newMergedTree)

      case currentNodeAsc :: restOfNodesAsc => {
                // for this case,newMergedTree is inserted at the BEGINNING of the remaining sorted list to scan
    	  		if (weight(newMergedTree) < weight(workingTreeListSortedAsc.head)) {
    	  			val compressedTreeList = newMergedTree :: workingTreeListSortedAsc
    	  			compressedTreeList
    	  		}
    	  		// for this case, we need to scan further to see where to insert newMergedTree
    	  		else {
    	  		    val compressedTreeList = currentNodeAsc :: recurseInsertInAscendingOrder(newMergedTree, workingTreeListSortedAsc.tail)
    	  		    compressedTreeList
    	  		}
      } // end case non-empty list

    } // match

    result
  }

*/
