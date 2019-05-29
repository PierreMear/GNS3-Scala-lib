package GAPY


object JSON_Exceptions {
  /** abstract class parent of all the errors linked to JSON */
  abstract class JSON_Exception(val message:String) extends Exception(message) {}

  /** Exception throwed when you try to get from the wrong type of structure */
  case class JSONCastError(override val message:String) extends JSON_Exception(message)
  /** Exception throwed when the last get resulted in a null */
  case class JSONNotExisting(override val message:String) extends JSON_Exception(message)
}