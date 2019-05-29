package GAPY 

import topologies.Topology

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._  
import forcelayout._

import objectTypes._
import GAPY.GNS3_Exceptions._

/**
 * Manager of a GNS3 project
 * @author Gwandalff
 */
class ProjectManager(val ProjectId: String, val serverAddress:String) {
  
    // Map Node/ID of the nodes in this project
    private val nodesId = Map[objectTypes.Node,String]()
    
    // Map Appliance/ID of the nodes in this project
    private val appliancesId = Map[Appliance,String]()
    
    // Map Nodes/LinkID 
    private val linksId = Map[Link,String]()
  
    /**
     * addNode : create a new node in the GNS3 project and save its ID in the name/id Map
     * @param name : the name of the node
     * @param nodeType : the type of the node
     * @param computeId : the computeId of the node
     * @return ProjectManager to be fluent
     */
    def addNode(n:objectTypes.Node): ProjectManager = {
      if(nodesId.filter((entry) => entry._1.name == n.name).size > 0){
        throw NodeNameConflictException("Conflict in nodes names : an other node already have this name -> " + n.name)
      }
      val body = "{\"name\":\"%s\",\"node_type\":\"%s\",\"compute_id\":\"%s\"}".format(n.name,n.node_type,n.compute_id)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes",body,serverAddress)
      JSONApi.parseJSONObject(returned).getFromObject("node_id")
      nodesId += (n -> JSONApi.value[String])
      this
    }
    
    /**
     * addNode : create a new node in the GNS3 project and save its ID in the name/id Map
     * @param name : the name of the node
     * @param nodeType : the type of the node
     * @param computeId : the computeId of the node
     * @return ProjectManager to be fluent
     */
    def addNode(a:Appliance): ProjectManager = {
      if(nodesId.filter((entry) => entry._1.name == a.name).size > 0){
        throw NodeNameConflictException("Conflict in nodes names : an other node already have this name -> " + a.name)
      }
      val returned = RESTApi.get("/v2/appliances",serverAddress)
      val appliances = JSONApi.parseJSONArray(returned).value[JSONArray]
      var applianceID:String = ""
      for(i <- 0 to appliances.size()){
        val appliance = appliances.get(i).asInstanceOf[JSONObject]
        if(appliance.get("name").asInstanceOf[String] == a.appliance_name){
          applianceID = appliance.get("appliance_id").asInstanceOf[String]
        }
      }
      val createdNode = RESTApi.post("/v2/projects/" + ProjectId + "/appliances/" + applianceID, "{}", serverAddress)
      JSONApi.parseJSONObject(createdNode).getFromObject("node_id")
      appliancesId += (a -> JSONApi.value[String])
      nodesId += (a -> JSONApi.value[String])
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
      JSONApi.parseJSONObject(returned).getFromObject("link_id")
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
    def removeNode(n:objectTypes.Node): ProjectManager = {
      if(!nodesId.contains(n)){
        throw NodeNotFoundException("Node not found : you wanted to remove an innexisting node : " + n)
      }
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(n, ""),serverAddress)
      nodesId -= n
      for((appliance,id) <- appliancesId){
        if(appliance.name == n.name){
          appliancesId -= appliance
        }
      }
      linksId --= linksId.filterKeys((link) => (link.from == n || link.to == n)).keys
      this
    }
    
    /**
     * removeLink : remove a link in the GNS3 project and remove its ID in the (Node,Node)/id Map
     * @param link : Object representing a link : {@link Link}
     * @return ProjectManager to be fluent
     */
    def removeLink(link:Link): ProjectManager = {
      val zelda:Link = link.reverse
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
    def startNode(node:objectTypes.Node): ProjectManager = {
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
    def stopNode(node:objectTypes.Node): ProjectManager = {
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
      var applianceIDs:List[String] = List()
      for((appliance:Appliance, id:String) <- projectManager.appliancesId){
        this.addNode(appliance)
        applianceIDs = id :: applianceIDs
      }
      println(applianceIDs)
      for((node:objectTypes.Node, id:String) <- projectManager.nodesId){
        if(!applianceIDs.contains(id)) this.addNode(node)
      }
      println(projectManager.nodesId)
      
      for(link:Link <- projectManager.linksId.keys){
        this.addLink(link)
      }
      
      this
    }
    
    def layout(scale:Int = 5, maxIteration:Int = 1000): ProjectManager = {
      var nodes:Seq[forcelayout.Node] = Seq()
      for((node:objectTypes.Node, id:String) <- nodesId){
        nodes = nodes :+ Node(id,node.name,1.0,0)
      }
      var edges:Seq[Edge] = Seq()
      for((link:objectTypes.Link, id:String) <- linksId){
        var linkedNodes:List[forcelayout.Node] = List()
        val ID1:String = nodesId.getOrElse(link.from,"")
        val ID2:String = nodesId.getOrElse(link.to,"")
        for(n:forcelayout.Node <- nodes){
          if(n.id == ID1 || n.id == ID2){
            linkedNodes = n :: linkedNodes
          }
        }
        edges = edges :+ Edge(linkedNodes(0),linkedNodes(1))
      }
      
      val graph:SpringGraph = new SpringGraph(nodes, edges)
      graph.doLayout(maxIterations = maxIteration)
      
      for(n:forcelayout.Node <- graph.nodes){
        val x:Int = n.state.pos.x.toInt * scale
        val y:Int = n.state.pos.y.toInt * scale
        val body = "{\"x\":"+x+",\"y\":"+y+"}"
        RESTApi.put("/v2/projects/" + ProjectId + "/nodes/"+n.id, body, serverAddress)
      }
      this
    }
}