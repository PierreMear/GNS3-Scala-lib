package GAPY

object SSHApi_Exceptions {
  /** abstract class parent of all the errors linked to JSON */
  abstract class SSH_Exception(val message:String) extends Exception(message) {}

  /** Exception throwed when you try to add a node with a config without enabling SSH */
  case class SSHDisabledException(override val message:String) extends SSH_Exception(message)
}