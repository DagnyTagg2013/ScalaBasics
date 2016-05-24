
/*

    Trait BASICS => interfaces that can provide concrete members and methods
                 => extended; not just implemented as in Java
                 *** AWESOME for COMPOSITION without tie-down to
                 implementation hierarchy (as in Java) for data members

    - https://www.safaribooksonline.com/blog/2013/05/30/traits-how-scala-tames-multiple-inheritance/

    - class extends only ONE class in both Scala and in Java; single CLASS INHERITANCE
    - *** MULTIPLE TRAIT implementation inheritance OK in Scala; via EXTENDs
    - MULTIPLE ABSTRACT interface inheritance LIMIT in Java; via IMPLEMENTs
    - *** MIXIN DYNAMICALLY 'WITH" keyword on new class instantiation!
    - OVERRIDE shadowed method!
    - *** SCALA COMPILER CATCHES DIAMOND MULTIPLE INHERITANCE OVERRIDE CONFLICT!
    - *** TYPE-LINEARLIZATION TO RESOLVE METHOD!
   ” In performing a linearization, you start with the type of the instantiated (leaf) class and
   recursively expand each of its supertypes into a list of their supertypes
   (the resulting list should be flat, not nested).
   You then scan that left-to-right and,
   whenever you come across a type that you’ve already seen,
   you delete it from the list. "
   *** THUS, DERIVED on RHS OVERRIDEs BASE on LHS; UNLESS super() is invoked!

 */

trait Friendly {
  def greet():String  = { return "Hi" }
}

class Dog extends Friendly {
  override def greet() = "Woof"
}

// NOTE:  how MEMBER data is setup
trait EnthusiasticFriendly extends Friendly {
  def countryGreet: String = "HOWDY"
  override def greet() = { super.greet() + ", " + countryGreet + "!" }
}
trait Affectionate {
  def greet() = { "PURRRR!" }
  def jumpOn() = { "JUMPING!"}
}

// NOTE:  needs WITH keyword to specify MULTIPLE implementation inheritance!
// NOTE:  COLLISION on greet() override from BOTH parent traits!
trait Adorable extends EnthusiasticFriendly with Affectionate {
  // NOTE:  WITHOUT override of greet(), get DIAMOND-NAME COLLISION from MULTIPLE parents!
  // ATTENTION:  Scala compiler will detect CONFLICTING members inherited!
  override def greet() = { "I'm so ADORBS!" }
}

class Cat extends Adorable {
  // override def greet() = "Meow"
}
// TEST1: Friendly acts as interface to view Dog implementation!
val petDog1: Friendly = new Dog
println(petDog1.greet())
// TEST2:  DYNAMIC MIXIN!
var petDog2 = new Dog with EnthusiasticFriendly
println(petDog2.greet())

// TEST3:  STATIC MULTIPLE TRAIT INHERITANCE,
//         - with COLLISION on method implementation
//         - TRYING UPCASTING to PARENT traits from MOST-DERIVED child traits!
//         - NOTE:  MOST DERIVED greet() implementation from Cat is used;
//           and this is NOT derived from trait-casted members!
// RESULT is ALWAYs what's implemented for MOST DERIVED LEAF-CHILD!
val petCatView1: Adorable = new Cat
val petCatView2: Affectionate = petCatView1
val petCatView3: EnthusiasticFriendly = petCatView1
val petCatView4: Friendly = petCatView1
println(petCatView1.greet())
println(petCatView2.greet())
println(petCatView3.greet())
println(petCatView4.greet())

// NOTE:  can also be used DYNAMICALLY, WITH for method INPUT ARGs signature,
// without creating a whole new STATIC-derived class!

