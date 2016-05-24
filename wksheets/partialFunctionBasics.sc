

// Partial Functions vs Function Currying
// http://stackoverflow.com/questions/14309501/scala-currying-vs-partially-applied-functions

// TODO:  as related to Case match clauses

// TODO:  definition of Partial function which takes INPUT Throwable, and returns Result!

def authenticationErrorHandler: PartialFunction[Throwable, Result] = {

  case UserNotFoundException(userId) =>
    NotFound(
      Json.obj("error" -> s"User with ID $userId was not found")
    )

  case UserDisabledException(userId) =>
    Unauthorized(
      Json.obj("error" -> s"User with ID $userId is disabled")
    )

}