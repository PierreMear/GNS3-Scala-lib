package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 

@Test
class Tests_Multiples_Projects {
  ////// Fonctions d'aide
  def returnServerAddress(): String = {
    return "http://148.60.11.201:3080"
  }
    
  def returnUrlAPI(): String = {
    return returnServerAddress() + "/v2/projects"
  }
    
  def checkProjectsAPI(added:String): String = {
    val http = Http(returnUrlAPI() + added)
    val response: HttpResponse[String] = http.asString    
    return response.body
  }

  def test_no_sharing() = {
    // Création des deux projets avec un seul GNS3Manager
    val projTest = new GNS3_Manager(returnServerAddress())
    val p_one = projTest.createProject("projTestOne")
    var check = checkProjectsAPI("")
    val proj_id_one = p_one.ProjectId
    assert(check != "[]", "The project 'projTestOne' should have been created")
    val p_two = projTest.createProject("projTestTwo")
    val proj_id_two = p_two.ProjectId

    //TODO Création de nodes, qui ne doivent pas apparaîtrent dans un projet ou elle ne sont pas, etc etc ...
    
    //On supprime les deux projets pour rester clean sur le serveur
    projTest.deleteProject(proj_id_one)
    projTest.deleteProject(proj_id_two)
    check = checkProjectsAPI("")
    assert(check != "[]", "The projects should have been deleted")
  }
}