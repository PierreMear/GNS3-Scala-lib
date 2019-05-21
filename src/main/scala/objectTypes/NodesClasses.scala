package objectTypes

case class Vpcs (override val name:String, override val compute_id:String) extends Node(name,"vpcs",compute_id)
case class Hub (override val name:String, override val compute_id:String) extends Node(name,"ethernet_hub",compute_id)
case class Switch (override val name:String, override val compute_id:String) extends Node(name,"ethernet_switch",compute_id)