package topologies

import GAPY.ProjectManager

class MeshNetwork(val nodeNamePrefix:String, val devices:List[String]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(nodeNamePrefix+devices(0), nodetype, "local")
    val addedDevices:List[String] += devices(0)
    for(i <- 1 until devices.length){
      val nodetype:String = devices(i)
      projectManager.addNode(nodeNamePrefix+nodetype, nodetype, "local")
      for(j <- 0 until addedDevices.length){
        // TODO : The addLink function will be updated shorlty so this will have to be updated as well
        projectManager.addLink(nodeNamePrefix+addedDevices(j), 0, i, nodeNamePrefix+nodetype, 0, j)
      }
    }
  }
}
