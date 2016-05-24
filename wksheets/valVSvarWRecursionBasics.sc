

/*

1) http://stackoverflow.com/questions/1791408/what-is-the-difference-between-a-var-and-val-definition-in-scala

 - essentially, PREFER 'val'/'immutable collections' for safety
 (cannot change its contents, or what it points to)

 *** PREFER recursion on new val creation to loops; to prevent mutation of variables; as well as chaining of operations without side-effects!

 - EXCEPT CAN use 'var' for results built iteratively;

 - OR can CREATE LOCAL COPY of queue while
 consuming it, for example,

 - THUS no need to copy input PRIOR to usage WITHIN method
 to preserve immutability!


2) Scala variable usage conventions:
http://michele.sciabarra.com/2015/11/17/scala/Quick-start-Scala-syntax-from-Java-background/
https://www.safaribooksonline.com/library/view/scala-cookbook/9781449340292/

*/

import scala.collection.immutable.Queue
// SANE way, with var
def toNum1(q: scala.collection.immutable.Queue[Int]) = {
  var qr = q
  var num = 0
  while (!qr.isEmpty) {
    val (digit, newQ) = qr.dequeue
    num *= 10
    num += digit
    qr = newQ
  }
  // automatically returns last val referenced!
  num
}

val a = toNum1(Queue[Int](3,4,5,2))

// PEDANTIC way, with val, and object creation;
// for IMMULTABLE object queue,
// where TAIL RECURSION is used to track LATEST state!
def toNum2(q: scala.collection.immutable.Queue[Int]) = {
  def recurse(qr: scala.collection.immutable.Queue[Int], num: Int): Int = {
    if (qr.isEmpty)
      num
    else {
      val (digit, newTailQ) = qr.dequeue
      recurse(newTailQ, num * 10 + digit)
    }
  }
  recurse(q, 0)
}
val b = toNum2(Queue[Int](1,2,4))