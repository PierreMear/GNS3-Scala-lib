package topologies

import GAPY.ProjectManager
import objectTypes._

class LinearNetwork(val devices:List[Node]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(devices(0))
    for(i <- 1 until devices.length){
      val node:Node = devices(i)
      projectManager.addNode(node)
      projectManager.addLink(devices(i-1), node, 1, 0)
    }
  }
}
