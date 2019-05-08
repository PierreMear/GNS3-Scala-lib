package topologies

import GAPY._

trait Topology {
  var nodeNamePrefix:String
  def create(projectManager:ProjectManager)
}