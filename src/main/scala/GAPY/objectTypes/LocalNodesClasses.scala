package gapy.objectTypes

/**
 * @author Gwandalff
 *
 * Case classes representing local built-in nodes
 */

case class LocalVpcs   (override val name:String) extends LocalNode(name, "vpcs")
case class LocalHub    (override val name:String) extends LocalNode(name, "ethernet_hub")
case class LocalSwitch (override val name:String) extends LocalNode(name, "ethernet_switch")
