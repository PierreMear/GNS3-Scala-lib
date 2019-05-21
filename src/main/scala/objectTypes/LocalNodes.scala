package objectTypes

case class Vpcs (override val name:String) extends LocalNode(name,"vpcs")
case class Hub (override val name:String) extends LocalNode(name,"ethernet_hub")
case class Switch (override val name:String) extends LocalNode(name,"ethernet_switch")