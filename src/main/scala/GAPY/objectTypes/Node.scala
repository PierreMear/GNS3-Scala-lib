package GAPY.objectTypes

/**
 * @author Gwandalff
 *
 * Root class for the node's representation
 */
abstract class Node(val name:String, val node_type:String, val compute_id:String) {
  override def toString:String = {
    "<"+name+","+node_type+","+compute_id+">"
  }
}
