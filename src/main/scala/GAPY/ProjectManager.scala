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
    private val linksId = Map[Tuple2[Node,Node],String]()
  
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
    def addLink(n1:Node, n2:Node, port1:Int, port2:Int, adaptater1:Int = 0, adaptater2:Int = 0): ProjectManager = {
      val node1 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater1,nodesId.getOrElse(n1, ""),port1)
      val node2 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater2,nodesId.getOrElse(n2, ""),port2)
      val body = "{\"nodes\":[%s,%s]}".format(node1,node2)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/links",body,serverAddress)
      val obj=JSONValue.parse(returned); 
      val link:JSONObject=obj.asInstanceOf[JSONObject];
      linksId += (new Tuple2(n1,n2) -> link.get("link_id").asInstanceOf[String])
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
    def removeLink(n1:Node, n2:Node): ProjectManager = {
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/links/" + linksId.getOrElse(new Tuple2(n1,n2), ""),serverAddress)
      linksId -= new Tuple2(n1,n2)
      this
    }
     
}