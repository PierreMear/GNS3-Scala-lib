package GAPY

import scalaj.http._
import org.json.simple._
import GAPY.GNS3_Exceptions.NotFoundException
import GAPY.GNS3_Exceptions.InternalServerErrorException
import GAPY.GNS3_Exceptions.UnknownException

class GNS3_Manager(val serverAddress:String) {
  
  def createProject(name : String) : ProjectManager = {
    val returned = RESTApi.post("/v2/projects", "{\"name\": \"" + name + "\"}",serverAddress);
    JSONApi.getFromObject(returned).getFromObject("project_id")
    return new ProjectManager(JSONApi.value[String], serverAddress);
  }
  
  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTApi.delete("/v2/projects/" + projectId, serverAddress);
    JSONApi.getFromObject(returned).getFromObject("status")
    val status: String = JSONApi.value[String]
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