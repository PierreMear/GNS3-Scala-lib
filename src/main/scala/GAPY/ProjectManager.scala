package GAPY 

import topologies.Topology

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._   

import objectTypes._

/**
 * Manager of a GNS3 project
 * @author Gwandalff
 */
class ProjectManager(var ProjectId: String, var serverAddress:String) {
  
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
      val obj=JSONValue.parse(returned); 
      val node:JSONObject=obj.asInstanceOf[JSONObject];
      nodesId += (n -> node.get("node_id").asInstanceOf[String])
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
      val node1 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(link.fromAdapter,nodesId.getOrElse(link.from, ""),link.fromPort)
      val node2 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(link.toAdapter,nodesId.getOrElse(link.to, ""),link.toPort)
      val body = "{\"nodes\":[%s,%s]}".format(node1,node2)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/links",body,serverAddress)
      val obj=JSONValue.parse(returned); 
      val json_link:JSONObject=obj.asInstanceOf[JSONObject];
      linksId += ( link -> json_link.get("link_id").asInstanceOf[String])
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
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/links/" + linksId.getOrElse(link, ""),serverAddress)
      linksId -= link
      this
    }
    
    /**
     * startNode : start the node
     * @param node : node to start
     * @return ProjectManager to be fluent
     */
    def startNode(node:Node): ProjectManager = {
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/node/" + nodesId.getOrElse(node, "") + "/start","{}",serverAddress)
      this
    }
    
    /**
     * stopNode : stop the node
     * @param node : node to stop
     * @return ProjectManager to be fluent
     */
    def stopNode(node:Node): ProjectManager = {
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/node/" + nodesId.getOrElse(node, "") + "/stop","{}",serverAddress)
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