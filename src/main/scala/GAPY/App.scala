package GAPY

import topologies._

object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    val p = new ProjectManager("e5e67027-ab19-4a4b-a883-774c028aa90d","http://localhost:3080")
    p.addTopology(new StarNetwork("SN1-","ethernet_hub",List("vpcs","vpcs","vpcs","vpcs","vpcs","vpcs","vpcs","vpcs")))
    /*p.addNode("PC1", "vpcs", "local")
    .addNode("PC2", "vpcs", "local")
    .addNode("PC3", "vpcs", "local")
    .addNode("PC4", "vpcs", "local")
    .addNode("hub", "ethernet_hub", "local")
    .addLink("PC1", 0, 0, "hub", 0, 0)
    .addLink("PC2", 0, 0, "hub", 0, 1)
    .addLink("PC3", 0, 0, "hub", 0, 2)
    .addLink("PC4", 0, 0, "hub", 0, 3)
    .removeLink("PC1", "hub")
    .removeLink("PC2", "hub")
    .addLink("PC1", 0, 0, "PC2", 0, 0)
    .removeNode("PC3")*/
  }

}
