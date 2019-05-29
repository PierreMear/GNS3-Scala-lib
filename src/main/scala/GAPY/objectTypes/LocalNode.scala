package gapy.objectTypes

/**
 * @author Gwandalff
 *
 * One of the direct subclass of Node
 * This class bind the compute_id parameter for the user
 */
abstract class LocalNode(override val name:String, override val node_type:String) extends Node(name,node_type,"local"){}
