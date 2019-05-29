package gapy.objectTypes

/**
 * @author Gwandalff
 *
 * One of the direct subclass of Node
 * This class is the root of all the non-built-in nodes
 * If you want to use appliance nodes you can subclass this one to make your work easier
 * To see how you can do that go check {@link LocalNode} and its subclasses in LocalNodesClasses.scala
 */
abstract class Appliance(
             val appliance_name  :String,
    override val name            :String,
    override val node_type       :String,
    override val compute_id      :String
    )
    extends Node(name, node_type, compute_id){}
