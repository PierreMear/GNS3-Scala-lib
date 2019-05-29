package GAPY

import GAPY.topologies.Topology

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._
import GAPY.forcelayout._

import GAPY.objectTypes._
import GAPY.GNS3_Exceptions._

/**
 * @author Gwandalff
 *
 * Manager of a GNS3 project
 *
 * This class don't need to be instanciated in itself because it's created by the {@link GNS3_Manager}
 *
 * @param ProjectId the ID of the project we want to work on
 * @param serverAddress the address of the GNS3 server(with port ex: 127.0.0.1:3080)
 */
class ProjectManager(val ProjectId: String, val serverAddress:String, val username:String, val password:String) {

    // Map Node/ID of the nodes in this project
    private val nodesId = Map[objectTypes.Node,String]()

    // Map Appliance/ID of the nodes in this project
    private val appliancesId = Map[Appliance,String]()

    // Map Nodes/LinkID
    private val linksId = Map[Link,String]()

    /**
     * addNode : create a new node in the GNS3 project and save its ID in the Node/id Map
     *
     * @param n the {@link Node} object you want to create
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNameConflictException if the name is already used for an other node
     */
    def addNode(n:objectTypes.Node): ProjectManager = {
      if(nodesId.filter((entry) => entry._1.name == n.name).size > 0){
        throw NodeNameConflictException("Conflict in nodes names : an other node already have this name -> " + n.name)
      }
      val body = "{\"name\":\"%s\",\"node_type\":\"%s\",\"compute_id\":\"%s\"}".format(n.name,n.node_type,n.compute_id)
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes",body,serverAddress,this.username,this.password)
      JSONApi.parseJSONObject(returned).getFromObject("node_id")
      nodesId += (n -> JSONApi.value[String])
      this
    }

    /**
     * addNode : create a node in the project from an appliance template
     *
     * Take an appliance object as parameter and check with the REST API if this appliance template exist
     * if this template exist, then we get its ID and create it.
     * <p>
     * After that we will add the {@link Node} to the Node/ID map to ensure that it will be well used after.
     * We add the {@link Appliance} object and the id of the node to the Appliance/ID to save the type of appliance
     * we want to create when we copy the project.
     * The ID saved in both maps is the node ID to not duplicate appliance nodes in a copy of the project
     *
     * @param a the {@link Appliance} object you want to create
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNameConflictException if the name is already used for an other node
     */
    def addNode(a:Appliance): ProjectManager = {
      if(nodesId.filter((entry) => entry._1.name == a.name).size > 0){
        throw NodeNameConflictException("Conflict in nodes names : an other node already have this name -> " + a.name)
      }
      val returned = RESTApi.get("/v2/appliances",serverAddress,this.username,this.password)
      val appliances = JSONApi.parseJSONArray(returned).value[JSONArray].toArray()
      var applianceID:String = ""
      for(obj_appliance <- appliances){
        val appliance = obj_appliance.asInstanceOf[JSONObject]
        if(appliance.get("name").asInstanceOf[String] == a.appliance_name){
          applianceID = appliance.get("appliance_id").asInstanceOf[String]
        }
      }
      val createdNode = RESTApi.post("/v2/projects/" + ProjectId + "/appliances/" + applianceID, "{\"x\":0,\"y\":0}",serverAddress,this.username,this.password)
>>>>>>> trying to add an optionnal authentification to gns3 server
      JSONApi.parseJSONObject(createdNode).getFromObject("node_id")
      appliancesId += (a -> JSONApi.value[String])
      nodesId += (a -> JSONApi.value[String])
      this
    }

    /**
     * addLink : create a new link in the GNS3 project and save its ID in the Link/id Map
     *
     * @param link  the {@link Link} object which represent the connection we want to create
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNotFoundException if one of the specified node doesn't exist
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
      val returned = RESTApi.post("/v2/projects/" + ProjectId + "/links",body,serverAddress,this.username,this.password)
      JSONApi.parseJSONObject(returned).getFromObject("link_id")
      linksId += ( link -> JSONApi.value[String])
      this
    }

    /**
     * addTopology : create a topology
     *
     * @param topology  the {@link Topology} to create
     * @return {@link ProjectManager} to be fluent
     */
    def addTopology(topology:Topology): ProjectManager = {
      topology.create(this)
      this
    }

    /**
     * removeNode : remove a node in the GNS3 project
     *
     * Remove the node in GNS3 and in all the Maps of this lib including :<br>
     *  - Node/ID<br>
     *  - Appliance/ID if it was an {@link Appliance}
     *  - Links/ID if this node was connected to other nodes
     *
     * @param name  the name of the {@link Node} to remove
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNotFoundException if the node don't exist
     */
    def removeNode(n:objectTypes.Node): ProjectManager = {
      if(!nodesId.contains(n)){
        throw NodeNotFoundException("Node not found : you wanted to remove an innexisting node : " + n)
      }
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(n, ""),serverAddress,this.username,this.password)
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
     *
     * @param link  the {@link Link} representing the connection which is gonna be deleted
     * @return {@link ProjectManager} to be fluent
     * @throws LinkNotFoundException if the link or the inverse link wasn't created before
     */
    def removeLink(link:Link): ProjectManager = {
      val zelda:Link = link.reverse
      if(!linksId.contains(link) && !linksId.contains(zelda)){
        throw LinkNotFoundException("Link not found : you wanted to remove an innexisting link : " + link)
      }
      var returned = RESTApi.delete("/v2/projects/" + ProjectId + "/links/" + linksId.getOrElse(link, linksId.getOrElse(zelda, "")),serverAddress,this.username,this.password)
      linksId -= link
      this
    }

    /**
     * startNode : start the node
     *
     * @param node  the {@link Node} to start
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNotFoundException if the node doesn't exist
     */
    def startNode(node:objectTypes.Node): ProjectManager = {
      if(!nodesId.contains(node)){
        throw NodeNotFoundException("Node not found : you wanted to start an innexisting node : " + node)
      }
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(node, "") + "/start","{}",serverAddress,this.username,this.password)
      this
    }

    /**
     * stopNode : stop the node
     *
     * @param node  the {@link Node} to stop
     * @return {@link ProjectManager} to be fluent
     * @throws NodeNotFoundException if the node doesn't exist
     */
    def stopNode(node:objectTypes.Node): ProjectManager = {
      if(!nodesId.contains(node)){
        throw NodeNotFoundException("Node not found : you wanted to stop an innexisting node : " + node)
      }
      var returned = RESTApi.post("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(node, "") + "/stop","{}",serverAddress,this.username,this.password)
      this
    }

    /**
     * startAll : start all the nodes of the project
     *
     * @return {@link ProjectManager} to be fluent
     */
    def startAll(): ProjectManager = {
      for((node,id) <- nodesId){
        startNode(node)
      }
      this
    }

    /**
     * stopAll : stop all the nodes of the project
     *
     * @return {@link ProjectManager} to be fluent
     */
    def stopAll(): ProjectManager = {
      for((node,id) <- nodesId){
        stopNode(node)
      }
      this
    }

    /**
     * copyProject : copy the project given into this project
     *
     * First create all the {@link Appliance} nodes because of their specificity
     * Then create all the remaining {@link Node} without duplicating the {@link Appliance} nodes
     * Then create all the {@link Link} between the nodes that we created
     *
     * @param projectManager : the {@link ProjectManager} of the project you want to copy
     * @return {@link ProjectManager} to be fluent
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

    /**
     * layout : lay out the network on GNS3 GUI
     *
     * Use the forcelayout Scala API (https://github.com/rsimon/scala-force-layout)
     *
     * create a graph with forcelayout API and call its doLayout function to find the positions of the nodes
     *
     * @param scale(Optional) expantion coefficient of the graph
     * @param maxIteration(Optional) the number of the system forces and positions update
     * @return {@link ProjectManager} to be fluent
     */
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
        RESTApi.put("/v2/projects/" + ProjectId + "/nodes/"+n.id, body, serverAddress,this.username,this.password)
      }
      this
    }

    /**
     * clean : delete all the nodes and links in the project
     *
     * @return {@link ProjectManager} to be fluent
     */
    def clean() : ProjectManager = {
      for(node:objectTypes.Node <- nodesId.keys){
        this.removeNode(node)
      }
      this
    }

    /**
     * delete : delete the current project
     */
    def delete() : Unit = {
      var returned = RESTApi.delete("/v2/projects/" + ProjectId,serverAddress,this.username,this.password)
    }
}
