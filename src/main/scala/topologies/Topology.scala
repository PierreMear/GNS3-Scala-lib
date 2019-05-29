package topologies

import GAPY._

/**
 * @author Gwandalff
 * 
 * Trait to extends if you want to create your own topology
 * 
 * Here we just ask for a function create that take a {@link ProjectManager} because when you will create
 * a topology on a project, the {@link ProjectManager} will call this function with itself as parameter
 * 
 * We hardly recommend to take a prefix parameter in the constructer to ensure that two topologies of the 
 * same type will not enter in conflict because of the nodes name
 */
trait Topology {
  def create(projectManager:ProjectManager)
}