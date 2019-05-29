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

    //Test si la fonction getProjectId renvois bien la bonne id
    @Test
    def testProjectId() = {
      val projEmptyTest = new GNS3_Manager(returnServerAddress())
      val p = projEmptyTest.createProject("projEmpty2")
      var check = checkProjectsAPI()
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projEmpty2' should have been created")

      var proj_id_test = ""
      try {
        proj_id_test = projEmptyTest.getProjectId("projEmpty2")
      } catch {
        case exc: Exception => println("Error : \n" + exc.printStackTrace()) 
      }
      
      projEmptyTest.deleteProject(proj_id)
      assert(proj_id == proj_id_test, "The project id returned by getprojectId() isn't the same ID ! Id returned : " + proj_id_test + " and should have been " + proj_id)

      assert(check == "[]", "The project 'projEmpty2' should have been destroyed")
    }

    @Test
    def testProjectErrors() = {
      val projError = new GNS3_Manager(returnServerAddress())
      val p = projError.createProject("projError")
      var check = checkProjectsAPI()
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projError' should have been created")

      // On test la ConflictException de création de projet 
      var good_error = false
      try {
        val w = projError.createProject("projError")
      } catch {
        case ext: GAPY.GNS3_Exceptions.ConflictException => good_error = true
        case exc: Exception => throw new Exception("Error : \n" + exc.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a ConflictException ! We created a project with the same name as another existing project")

      // On test la ConflictException de création de projet 
      good_error = false
      try {
        projError.deleteProject(proj_id + "42")
      } catch {
        case ext: GAPY.GNS3_Exceptions.NotFoundException => good_error = true
        case exc: Exception => throw new Exception("Error : \n" + exc.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a NotFoundExceptions ! We deleted a project that doesn't exist")

      projError.deleteProject(proj_id)
      check = checkProjectsAPI()
      assert(check == "[]", "The project 'projError' should have been destroyed")

    }

}