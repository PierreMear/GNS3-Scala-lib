package GAPY 

import scalaj.http._
import scala.collection.mutable.Map
import org.json.simple._               

class ProjectManager(var ProjectId: String, var serverAddress:String) {
  
    private val nodesId = Map[String,String]()
    private val linksId = Map[Tuple2[String,String],String]()
  
    def addNode(name:String, nodeType:String, computeId:String): ProjectManager = {
      val body = "{\"name\":\"%s\",\"node_type\":\"%s\",\"compute_id\":\"%s\"}".format(name,nodeType,computeId)
      var returned = RESTCall("/v2/projects/" + ProjectId + "/nodes","POST",body)
      var obj=JSONValue.parse(returned); 
      var node:JSONObject=obj.asInstanceOf[JSONObject];
      nodesId += (name -> node.get("node_id").asInstanceOf[String])
      this
    }
    
    def addLink(name1:String, adaptater1:Int, port1:Int, name2:String, adaptater2:Int, port2:Int): ProjectManager = {
      val node1 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater1,nodesId.getOrElse(name1, ""),port1)
      val node2 = "{\"adapter_number\":%s,\"node_id\":\"%s\",\"port_number\":%s}".format(adaptater2,nodesId.getOrElse(name2, ""),port2)
      val body = "{\"nodes\":[%s,%s]}".format(node1,node2)
      var returned = RESTCall("/v2/projects/" + ProjectId + "/links","POST",body)
      var obj=JSONValue.parse(returned); 
      var link:JSONObject=obj.asInstanceOf[JSONObject];
      linksId += (new Tuple2(name1,name2) -> link.get("link_id").asInstanceOf[String])
      this
    }
    
    def removeNode(name:String): ProjectManager = {
      var returned = RESTCall("/v2/projects/" + ProjectId + "/nodes/" + nodesId.getOrElse(name, ""),"DELETE","{}")
      nodesId -= name
      this
    }
    
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
              val response: HttpResponse[String] = http.postData(body).asString    //.postForm(body).asString
              return response.body
            }
            case "DELETE" => {
              val response: HttpResponse[String] = http.method("delete").asString    //.postForm(body).asString
              return response.body
            }
        }
    }  
}