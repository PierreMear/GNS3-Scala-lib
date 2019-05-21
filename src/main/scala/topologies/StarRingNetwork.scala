package topologies

import GAPY.ProjectManager
import objectTypes._

class StarRingNetwork(val center:Node, val devices:List[Node]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager
   .addTopology(new StarNetwork(center,devices))
   for(i <- 1 until devices.length){
      val node:Node = devices(i)
      projectManager.addLink(SimpleLink(devices(i-1), node, 2, 3))
    }
    projectManager.addLink(SimpleLink(devices(devices.length-1), devices(0), 2, 3))
  }
}
