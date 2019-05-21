package topologies

import GAPY.ProjectManager
import objectTypes._

class StarLinearNetwork(val center:Node, val devices:List[Node]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
   projectManager
   .addTopology(new StarNetwork(center,devices))
   for(i <- 1 until devices.length){
      val node:Node = devices(i)
      projectManager.addLink(SimpleLink(devices(i-1), node, 2, 3))
    }
  }
}
