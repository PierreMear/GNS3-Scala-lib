package topologies

import GAPY.ProjectManager

class StarNetwork(val nodeNamePrefix:String, val center:String, val others:List[String]) extends Topology{
  override def create(projectManager:ProjectManager): Unit = {
    projectManager.addNode(nodeNamePrefix+center, center, "local")
    for(i <- 0 until others.length){
      val nodetype:String = others(i)
      projectManager.addNode(nodeNamePrefix+nodetype, nodetype, "local")
      projectManager.addLink(nodeNamePrefix+center, 0, i, nodeNamePrefix+nodetype, 0, 0)
    }
  }
}