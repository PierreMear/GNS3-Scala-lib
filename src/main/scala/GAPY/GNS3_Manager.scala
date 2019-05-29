package GAPY

import scalaj.http._
import org.json.simple._
import GAPY.GNS3_Exceptions.NotFoundException
import GAPY.GNS3_Exceptions.InternalServerErrorException
import GAPY.GNS3_Exceptions.UnknownException
import GAPY.GNS3_Exceptions.ConflictException
import GAPY.JSON_Exceptions.JSONCastError

/**
 * Manager of a GNS3 server
 *
 * Manage all the aspect of the project like create or delete a project
 *
 * @param serverAddress the GNS3 server address(with port ex : 127.0.0.1:3080)
 * @param username the username used to connect to the GNS3 server
 * @param password the password used to connect to the GNS3 server
 */
class GNS3_Manager(val serverAddress:String, val username:String = "", val password:String = "") {

  /**
   * createProject : create a project on the server with the specified name
   *
   * @param name the name of the project we want to create
   * @param username the username used to connect to the GNS3 server
   * @param password the password used to connect to the GNS3 server
   * @return the project manager of the project we've just created
   * @throws NotFoundException if the project is not found
   * @throws InternalServerErrorException if an error occur on the server side
   * @throws ConflictException if a project with the same name has already been created
   */
  def createProject(name : String) : ProjectManager = {
    val returned = RESTApi.post("/v2/projects", "{\"name\": \"" + name + "\"}",serverAddress,this.username,this.password)
    JSONApi.parseJSONObject(returned).getFromObject("project_id")
    val projectId = JSONApi.value[String];
    JSONApi.parseJSONObject(returned).getFromObject("message");
    if(!JSONApi.isNullPointer()) {
      val message = JSONApi.value[String];
      JSONApi.parseJSONObject(returned).getFromObject("status")
      val status = JSONApi.value[Long]();
      status match {
        case 404 => throw new NotFoundException("project not found");
        case 500 => throw new InternalServerErrorException("server unreachable");
        case 409 => throw new ConflictException("Conflict with existing project");
      }
    }
    return new ProjectManager(projectId, serverAddress,this.username,this.password);
  }

  /**
   * deleteProject : create a project on the server with the specified name
   *
   * @param projectId the ID of the project we want to delete
   * @return the {@link GNS3_Manager} to be able to fluently create a new project
   * @throws NotFoundException if the project is not found
   * @throws InternalServerErrorException if an error occur on the server side
   * @throws ConflictException if there is a conflict with an other project
   */
  def deleteProject(projectId: String) : GNS3_Manager = {
    val returned = RESTApi.delete("/v2/projects/" + projectId, serverAddress,this.username,this.password)
    if (returned != ""){
      JSONApi.parseJSONObject(returned).getFromObject("message");
      if(!JSONApi.isNullPointer()) {
        val message = JSONApi.value[String];
        JSONApi.parseJSONObject(returned).getFromObject("status")
        val status = JSONApi.value[Long]();
        status match {
          case 404 => throw new NotFoundException("project not found");
          case 500 => throw new InternalServerErrorException("server unreachable");
          case 409 => throw new ConflictException("Conflict with existing project");
        }
      }
    }
    this
  }

  /**
   * getProjectId : get the ID of the project with the specified name
   *
   * @param name the name of the project we want the ID
   * @return the ID
   * @throws NotFoundException if the project is not found
   * @throws InternalServerErrorException if an error occur on the server side
   * @throws ConflictException if there is a conflict with an other project
   */
  def getProjectId(name : String) : String = {
    val returned = RESTApi.get("/v2/projects", serverAddress,this.username,this.password)
    try {
      JSONApi.parseJSONArray(returned);
      val projects = JSONApi.value[JSONArray].toArray();
      var id:String = "ID"
      for(project <- projects){
        if(project.asInstanceOf[JSONObject].get("name").asInstanceOf[String] == name){
          id = project.asInstanceOf[JSONObject].get("project_id").asInstanceOf[String]
        }
      }
      if(id != null){
        return id
      }
    } catch {
      case e: JSONCastError => {
        JSONApi.parseJSONObject(returned).getFromObject("message");
        if(!JSONApi.isNullPointer()) {
          val message = JSONApi.value[String];
          JSONApi.parseJSONObject(returned).getFromObject("status")
          val status = JSONApi.value[Long]();
          status match {
            case 404 => throw new NotFoundException("project not found");
            case 500 => throw new InternalServerErrorException("server unreachable");
            case 409 => throw new ConflictException("Conflict with existing project");
          }
        }
      }
    }
    return "noId";
  }

}
