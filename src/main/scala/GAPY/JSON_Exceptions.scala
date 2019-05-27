package GAPY


object JSON_Exceptions {
  abstract class JSON_Exception(val message:String) extends Exception(message) {}

  case class JSONCastError(override val message:String) extends JSON_Exception(message)
  case class JSONNotExisting(override val message:String) extends JSON_Exception(message)
}