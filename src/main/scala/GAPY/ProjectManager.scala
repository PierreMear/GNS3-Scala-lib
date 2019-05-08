package GAPY 

import topologies.Topology

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._    

/**
 * Manager of a GNS3 project
 * @author Gwandalff
 */
class ProjectManager(var ProjectId: String, var serverAddress:String) {
  
    // Map Name/ID of the nodes in this project
    private val nodesId = Map[String,String]()
    
    // Map Nodes/LinkID : the Tuple2 is the two nodes connected 
    private val linksId = Map[Tuple2[String,String],String]()
  
    /**
     * addNode : create a new node in the GNS3 project and save its ID in the name/id Map
     * @param name : the name of the node
     * @param nodeType : the type of the node
     * @param computeId : the computeId of the node
     * @return ProjectManager to be fluent
     */
    def addNode(name:String, nodeType:String, computeId:String): ProjectManager = {
      val body = "{\"name\":\"%s\",\"node_type\":\"%s\",\"compute_id\":\"%s\"}".format(name,nodeType,computeId)
      val returned = RESTCall("/v2/projects/" + ProjectId + "/nodes","POST",body)
      val obj=JSONValue.parse(returned); 
      val node:JSONObject=obj.asInstanceOf[JSONObject];
      nodesId += (name -> node.get("node_id").asInstanceOf[String])
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
    def addLink(name1:String, adaptater1:Int, port1:Int, name2:String, adaptater2:Int, port2:Int): ProjectManager = {
      val node1 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater1,nodesId.getOrElse(name1, ""),port1)
      val node2 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater2,nodesId.getOrElse(name2, ""),port2)
      val body = "{\"nodes\":[%s,%s]}".format(node1,node2)
      val returned = RESTCall("/v2/projects/" + ProjectId + "/links","POST",body)
      val obj=JSONValue.parse(returned); 
      val link:JSONObject=obj.asInstanceOf[JSONObject];
      linksId += (new Tuple2(name1,name2) -> link.get("link_id").asInstanceOf[String])
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
    def removeNode(name:String): ProjectManager = {
      var returned = RESTCall("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(name, ""),"DELETE","{}")
      nodesId -= name
      this
    }
    
    /**
     * removeLink : remove a link in the GNS3 project and remove its ID in the (Node,Node)/id Map
     * @param name1 : the name of the first node
     * @param name2 : the name of the second node
     * @return ProjectManager to be fluent
     */
    def removeLink(name1:String, name2:String): ProjectManager = {
      var returned = RESTCall("/v2/projects/" + ProjectId + "/links/" + linksId.getOrElse(new Tuple2(name1,name2), ""),"DELETE","{}")
      linksId -= new Tuple2(name1,name2)
      this
    }
    
    private def RESTCall(url:String, method:String, body:String): String = {
        var http = Http(serverAddress + url)
        method match {
            case "GET" => {
              val response: HttpResponse[String] = http.asString
              return response.body
            }
            case "POST" => {
              val response: HttpResponse[String] = http.postData(body).asString
              return response.body
            }
            case "DELETE" => {
              val response: HttpResponse[String] = http.method("delete").asString
              return response.body
            }
        }
    }  
}