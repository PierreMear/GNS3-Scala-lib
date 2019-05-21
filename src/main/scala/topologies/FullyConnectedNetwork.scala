package topologies

import GAPY.ProjectManager
import objectTypes._

class FullyConnectedNetwork(val devices:List[Node]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(devices(0))
    var addedDevices:List[Node] = devices(0) :: List()
    for(i <- 1 until devices.length){
      val node:Node = devices(i)
      projectManager.addNode(node)
      for(j <- 0 until addedDevices.length){
        projectManager.addLink(SimpleLink(addedDevices(j),node, i, j))
      }
      addedDevices = node :: addedDevices
    }
  }
}
