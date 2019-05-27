package GAPY 

import topologies.Topology

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._   

import objectTypes._
import GAPY.GNS3_Exceptions._

/**
 * Manager of a GNS3 project
 * @author Gwandalff
 */
class ProjectManager(val ProjectId: String, val serverAddress:String) {
  
    // Map Node/ID of the nodes in this project
    private val nodesId = Map[Node,String]()
    
    // Map Nodes/LinkID : the Tuple2 is the two nodes connected 
    private val linksId = Map[Link,String]()
  
    /**
     * addNode : create a new node in the GNS3 project and save its ID in the name/id Map
     * @param name : the name of the node
     * @param nodeType : the type of the node
     * @param computeId : the computeId of the node
     * @return ProjectManager to be fluent
     */
    def addNode(n:Node): ProjectManager = {
      val body = "{\"name\":\"%s\",\"node_type\":\"%s\",\"compute_id\":\"%s\"}".format(n.name,n.node_type,n.compute_id)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes",body,serverAddress)
      JSONApi.parseJSONObject(returned).getFromObject("node_id")
      nodesId += (n -> JSONApi.value[String])
      this
    }
    
    /**
     * addLink : create a new link in the GNS3 project and save its ID in the (Node,Node)/id Map
     * @param name1 : the name of the first node
     * @param adaptater1 : adaptater number of the first node
     * @param port1 : ethernet port of the first node
     * @param name2 : the name of the second node
     * @param adaptater2 : adaptater number of the second node
     * @param port2 : ethernet port of the second node
     * @return ProjectManager to be fluent
     */
    def addLink(link:Link): ProjectManager = {
      if(!nodesId.contains(link.from)){
        throw NodeNotFoundException("Node not found : you wanted to link two nodes but one of them wasn't created : " + link.from)
      }
      if(!nodesId.contains(link.to)){
        throw NodeNotFoundException("Node not found : you wanted to link two nodes but one of them wasn't created : " + link.to)
      }
      val node1 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(link.fromAdapter,nodesId.getOrElse(link.from, ""),link.fromPort)
      val node2 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(link.toAdapter,nodesId.getOrElse(link.to, ""),link.toPort)
      val body = "{\"nodes\":[%s,%s]}".format(node1,node2)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/links",body,serverAddress)
      JSONApi.getFromObject(returned).getFromObject("link_id")
      linksId += ( link -> JSONApi.value[String])
      this
    }
    
    /**
     * addTopology : create a topology
     * @param topology : the topology to create
     * @return ProjectManager to be fluent
     */
    def addTopology(topology:Topology): ProjectManager = {
      topology.create(this)
      this
    }
    
    /**
     * removeNode : remove a node in the GNS3 project and remove its ID in the name/id Map
     * @param name : the name of the node to remove
     * @return ProjectManager to be fluent
     */
    def removeNode(n:Node): ProjectManager = {
      if(!nodesId.contains(n)){
        throw NodeNotFoundException("Node not found : you wanted to remove an innexisting node : " + n)
      }
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(n, ""),serverAddress)
      nodesId -= n
      this
    }
    
    /**
     * removeLink : remove a link in the GNS3 project and remove its ID in the (Node,Node)/id Map
     * @param name1 : the name of the first node
     * @param name2 : the name of the second node
     * @return ProjectManager to be fluent
     */
    def removeLink(link:Link): ProjectManager = {
      val zelda:Link = RawLink(link.to,link.from,link.toPort,link.fromPort,link.toAdapter,link.fromAdapter)
      if(!linksId.contains(link) && !linksId.contains(zelda)){
        throw LinkNotFoundException("Link not found : you wanted to remove an innexisting link : " + link)
      }
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/links/" + linksId.getOrElse(link, linksId.getOrElse(zelda, "")),serverAddress)
      linksId -= link
      this
    }
    
    /**
     * startNode : start the node
     * @param node : node to start
     * @return ProjectManager to be fluent
     */
    def startNode(node:Node): ProjectManager = {
      if(!nodesId.contains(node)){
        throw NodeNotFoundException("Node not found : you wanted to remove an innexisting node : " + node)
      }
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(node, "") + "/start","{}",serverAddress)
      this
    }
    
    /**
     * stopNode : stop the node
     * @param node : node to stop
     * @return ProjectManager to be fluent
     */
    def stopNode(node:Node): ProjectManager = {
      if(!nodesId.contains(node)){
        throw NodeNotFoundException("Node not found : you wanted to remove an innexisting node : " + node)
      }
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(node, "") + "/stop","{}",serverAddress)
      this
    }
    
    /**
     * startAll : start all the nodes of the project
     * @return ProjectManager to be fluent
     */
    def startAll(): ProjectManager = {
      for((node,id) <- nodesId){
        startNode(node)
      }
      this
    }
    
    /**
     * stopAll : stop all the nodes of the project
     * @return ProjectManager to be fluent
     */
    def stopAll(): ProjectManager = {
      for((node,id) <- nodesId){
        stopNode(node)
      }
      this
    }
     
    /**
     * copyProject : copy the project given
     * @param projectManager : the manager of the project you want to copy
     * @return ProjectManager to be fluent
     */
    def copyProject(projectManager:ProjectManager): ProjectManager = {
      for(node:Node <- projectManager.nodesId.keys){
        this.addNode(node)
      }
      for(link:Link <- projectManager.linksId.keys){
        this.addLink(link)
      }
      this
    }
}