package GAPY

object GNS3_Exceptions {
  // Network Exceptions
  // 400
  case class BadRequestException(private val message: String) extends Exception(message)
  // 404
  case class NotFoundException(private val message: String) extends Exception(message)
  // 409
  case class ConflictException(private val message: String) extends Exception(message)
  // 500
  case class InternalServerErrorException(private val message: String) extends Exception(message)
  // 503
  case class ServiceUnavailableException(private val message: String) extends Exception(message)
  
  // API Exceptions
  // thrown when you want to use a link that wasn't created
  case class LinkNotFoundException(private val message: String) extends Exception(message)
  // thrown when you want to use a node that wasn't created
  case class NodeNotFoundException(private val message: String) extends Exception(message)
  
  
  // default
  case class UnknownException(private val message: String) extends Exception(message)
}

/*
catch {
  case e: BadRequestException => println(e.getMessage());
  case e: NotFoundException   => println(e.getMessage());
  case e: Exception           => println(e.getMessage());
}
*/