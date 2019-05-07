package GAPY

import scalaj.http._
import org.json.simple._

class GNS3_Manager(val serverAddress:String) {
  
  def createProject(name : String) : ProjectManager = {
    val returned = RESTCall("/v2/projects", "POST", "{\"name\": \"" + name + "\"}");
    val obj = JSONValue.parse(returned); 
    val jsonObj:JSONObject = obj.asInstanceOf[JSONObject];
    val projectId = jsonObj.get("project_id").asInstanceOf[String]
    return new ProjectManager(projectId, serverAddress);
  }
  
  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTCall("/v2/projects/" + projectId, "DELETE", "");
    this
  }
  
  def getProjectId(name : String) : String = {
    return null
  }
  
  private def RESTCall(url:String, method:String, body:String) : String = {
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