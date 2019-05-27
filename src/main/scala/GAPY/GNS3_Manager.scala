package GAPY

import scalaj.http._
import org.json.simple._
import GAPY.GNS3_Exceptions.NotFoundException
import GAPY.GNS3_Exceptions.InternalServerErrorException
import GAPY.GNS3_Exceptions.UnknownException
import GAPY.GNS3_Exceptions.ConflictException

class GNS3_Manager(val serverAddress:String) {

  def createProject(name : String) : ProjectManager = {
    val returned = RESTApi.post("/v2/projects", "{\"name\": \"" + name + "\"}",serverAddress);
    val obj = JSONValue.parse(returned);
    val jsonObj:JSONObject = obj.asInstanceOf[JSONObject];
    val projectId = jsonObj.get("project_id").asInstanceOf[String];
    val statusObj = jsonObj.get("status");
    if(statusObj != null){
      val status: Long = statusObj.asInstanceOf[Long];
      status match {
        case 404 => throw new NotFoundException("project not found");
        case 500 => throw new InternalServerErrorException("server unreachable");
        case 409 => throw new ConflictException("Conflict with existing project");
       }
    }
    return new ProjectManager(projectId, serverAddress);
  }

  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTApi.delete("/v2/projects/" + projectId, serverAddress);
    val jsonObj:JSONObject = JSONValue.parse(returned).asInstanceOf[JSONObject];
    val statusObj = jsonObj.get("status");
    if(statusObj != null){
      val status: Long = statusObj.asInstanceOf[Long];
      status match {
        case 404 => throw new NotFoundException("project not found");
        case 500 => throw new InternalServerErrorException("server unreachable");
        case 409 => throw new ConflictException("Conflict with existing project");
       }
    }
    this
  }

  def getProjectId(name : String) : String = {
    val returned = RESTApi.get("/v2/projects/", serverAddress);
    val jsonObj:JSONObject = JSONValue.parse(returned).asInstanceOf[JSONObject];
    val jsonProjet:JSONObject = jsonObj.getJSONObject(name);
    val statusObj = jsonObj.get("status");
    if(jsonProjet != null){
      return jsonProjet.getString("Id");
    }else{
      if(statusObj != null){
        val status: Long = statusObj.asInstanceOf[Long];
        status match {
          case 404 => throw new NotFoundException("project not found");
          case 500 => throw new InternalServerErrorException("server unreachable");
          case 409 => throw new ConflictException("Conflict with existing project");
        }
      }
      return null;
    }

  }

}
