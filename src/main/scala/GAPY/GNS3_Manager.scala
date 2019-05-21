package GAPY

import scalaj.http._
import org.json.simple._
import GAPY.GNS3_Exceptions.NotFoundException
import GAPY.GNS3_Exceptions.InternalServerErrorException
import GAPY.GNS3_Exceptions.UnknownException

class GNS3_Manager(val serverAddress:String) {
  
  def createProject(name : String) : ProjectManager = {
    val returned = RESTApi.post("/v2/projects", "{\"name\": \"" + name + "\"}",serverAddress);
    val obj = JSONValue.parse(returned); 
    val jsonObj:JSONObject = obj.asInstanceOf[JSONObject];
    val projectId = jsonObj.get("project_id").asInstanceOf[String];
    return new ProjectManager(projectId, serverAddress);
  }
  
  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTApi.delete("/v2/projects/" + projectId, serverAddress);
    val jsonObj:JSONObject = JSONValue.parse(returned).asInstanceOf[JSONObject];
    val status: String = jsonObj.get("status").asInstanceOf[String];
    status match {
      case code if code.equals("404") => throw new NotFoundException("project not found");
      case code if code.equals("500") => throw new InternalServerErrorException("server unreachable");
      case _ => throw new UnknownException("unknown exception");
    }
    this
  }
  
  def getProjectId(name : String) : String = {
    return null
  }
  
}