package GAPY

import scalaj.http._
import org.json.simple._

class GNS3_Manager(val serverAddress:String) {
  
  def createProject(name : String) : ProjectManager = {
    val returned = RESTApi.post("/v2/projects", "{\"name\": \"" + name + "\"}",serverAddress);
    val obj = JSONValue.parse(returned); 
    val jsonObj:JSONObject = obj.asInstanceOf[JSONObject];
    val projectId = jsonObj.get("project_id").asInstanceOf[String]
    return new ProjectManager(projectId, serverAddress);
  }
  
  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTApi.delete("/v2/projects/" + projectId, serverAddress);
    this
  }
  
  def getProjectId(name : String) : String = {
    return null
  }
  
}