package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 


@Test
class Test_Basic {
    
    // Fonctions d'aides
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

      
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      
      assert(check != "[]", "The project 'projEmpty' should have been created")
    
      projEmptyTest.deleteProject(proj_id)    
      check = checkProjectsAPI("")

      assert(check == "[]", "The project 'projEmpty' should have been destroyed")

      
    }

    //Test si la fonction getProjectId renvois bien la bonne id
    @Test
    def testProjectId() = {
      val projEmptyTest = new GNS3_Manager(returnServerAddress())
      val p = projEmptyTest.createProject("projEmpty2")
      var check = checkProjectsAPI("")
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
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projEmpty2' should have been destroyed")
    }

    @Test
    def testProjectErrors() = {
      val projError = new GNS3_Manager(returnServerAddress())
      val p = projError.createProject("projError")
      var check = checkProjectsAPI("")
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
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projError' should have been destroyed")

    }

    //On test la fonction de deletion de projectManager et pas de GNS3Manager
    @Test
    def testDeleteProjMana() = {
      val projDel = new GNS3_Manager(returnServerAddress())
      val p = projDel.createProject("projDel")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projDel' should have been created")

      p.delete()

      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projDel' should have been destroyed")
    }

    //On test si un projet est bien copié comme il le devrait
    @Test
    def testCopyProj() = {
      val projCopy = new GNS3_Manager(returnServerAddress())
      val p = projCopy.createProject("projCopy")
      val p_bis = projCopy.createProject("projCopyBis")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      val proj_id_bis = p_bis.ProjectId
      assert(check != "[]", "The project 'projCopy' should have been created")

      p.addTopology(new topologies.StarNetwork(objectTypes.LocalHub("PC1"),List(objectTypes.LocalVpcs("PC2"),objectTypes.LocalVpcs("PC3"),objectTypes.LocalVpcs("PC4"),objectTypes.LocalVpcs("PC5"),objectTypes.LocalVpcs("PC6"))))

      var shouldnt = false
      var res_nodes = ""
      var res_links = ""
      try {
        p_bis.copyProject(p)
        val returned_nodes = checkProjectsAPI("/" + proj_id + "/nodes" )
        val returned_links = checkProjectsAPI("/" + proj_id + "/links" )
        res_nodes = JSONApi.parseJSONArray(returned_nodes).getFromArray(5).getFromObject("name").value[String]
        res_links = JSONApi.parseJSONArray(returned_links).getFromArray(4).getFromObject("link_id").value[String]
      } catch {
        case ex: Exception => println("Erreur " + ex.printStackTrace()) ; shouldnt = true
      }

      projCopy.deleteProject(proj_id)
      projCopy.deleteProject(proj_id_bis)
      check = checkProjectsAPI("")
      assert(res_nodes == "PC6", "The last node created should have been PC6 and not " + res_nodes)
      assert(res_links != null, "The last link created should have the 2n-3 th but it doesn't exist, res = " + res_links)
      assert(check == "[]", "The project 'projCopy' should have been destroyed")
      assert(!shouldnt, "A error happened during the Copy, please check ealier logs")
      assert
    }


}