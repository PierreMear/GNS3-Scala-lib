package GAPY

import org.junit._
import Assert._
import scalaj.http._

@Test
class Test_Basic {
    
    // Fonctions d'aides
    def returnServerAddress(): String = {
      return "http://148.60.11.201:3080"
    }
    
    def returnUrlAPI(): String = {
      return returnServerAddress() + "/v2/projects"
    }
    
    def checkProjectsAPI(): String = {
      val http = Http(returnUrlAPI())
      val response: HttpResponse[String] = http.asString
      
      return response.body
    }
    
    //Test si le server ne contient effectivement aucun projet
    @Test
    def testNoProject() = {
        val gns3_server_url = "http://148.60.11.201"
        val gns3_server_port = "3080"

        val http = Http(gns3_server_url + ":" + gns3_server_port + "/v2/projects")
        val response: HttpResponse[String] = http.asString

        assert(response.body == "[]", "No project should exist. Should had [] but had " + response)
    }

    //Test de création puis deletion d'un projet
    @Test
    def testCreateEmptyProject() = {
      val projEmptyTest = new GNS3_Manager(returnServerAddress())
      
      val p = projEmptyTest.createProject("projEmpty")

      
      var check = checkProjectsAPI()
      val proj_id = p.ProjectId
      
      assert(check != "[]", "The project 'projEmpty' should have been created")
      
      projEmptyTest.deleteProject(proj_id)
        
      check = checkProjectsAPI()

      assert(check == "[]", "The project 'projEmpty' should have been destroyed")

      
    }

}