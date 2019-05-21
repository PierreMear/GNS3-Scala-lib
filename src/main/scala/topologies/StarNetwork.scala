package topologies

import GAPY.ProjectManager

class StarNetwork(val nodeNamePrefix:String, val center:String, val devices:List[String]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(nodeNamePrefix+center, center, "local")
    for(i <- 0 until devices.length){
      val nodetype:String = devices(i)
      projectManager.addNode(nodeNamePrefix+nodetype, nodetype, "local")
      // TODO : The addLink function will be updated shorlty so this will have to be updated as well
      projectManager.addLink(nodeNamePrefix+center, 0, i, nodeNamePrefix+nodetype, 0, 0)
    }
  }
}
