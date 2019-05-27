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
    JSONApi.parseJSONObject(returned).getFromObject("project_id")
    val projectId = JSONApi.value[String];
    JSONApi.parseJSONObject(returned).getFromObject("status")
    val status = JSONApi.value[Long];
    if(status != null){
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

    JSONApi.parseJSONObject(returned).get("status")
    val status = JSONApi.value[Long];
    if(status != null){
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
    JSONApi.parseJSONObject(returned).get("status")
    val status = JSONApi.value[Long];
    val projects = JSONApi.parseJSONArray(returned).value[Array]
    var id:String = "ID"
    for(project <- projects){
      if(project.asInstanceOf[JSONObject].get("name").asInstanceOf[String] == name){
        id = project.asInstanceOf[JSONObject].get("project_id").asInstanceOf[String]
      }
    }
    if(id != null){
      return id
    }else{
      if(status != null){
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
