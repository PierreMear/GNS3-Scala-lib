package topologies

import GAPY.ProjectManager
import objectTypes._

class StarNetwork(val center:Node, val devices:List[Node]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(center)
    for(i <- 0 until devices.length){
      val node:Node = devices(i)
      projectManager.addNode(node)
      projectManager.addLink(SimpleLink(center, node, i, 0))
    }
  }
}
