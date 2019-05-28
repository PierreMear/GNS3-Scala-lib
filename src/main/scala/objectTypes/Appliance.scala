package objectTypes

abstract class Appliance(
             val appliance_name  :String, 
    override val name            :String, 
    override val node_type       :String, 
    override val compute_id      :String
    ) 
    extends Node(name, node_type, compute_id){}