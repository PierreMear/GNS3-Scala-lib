package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 

@Test
class Tests_Basic_Nodes {
  
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
    
    //Test si les nodes et liens sont bien créées, avec le bon nom et le bon type
    @Test
    def testProjectAndCNodesAndLinks() = {
      val projEmptyTest = new GNS3_Manager(returnServerAddress())
      val p = projEmptyTest.createProject("projEmpty")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projEmpty' should have been created")
      val one = objectTypes.Vpcs("Hyrule","id")
      p.addNode(one);
      val returned = checkProjectsAPI("/" + proj_id + "/nodes" )
      val obj = JSONValue.parse(returned);
      println(obj)
      
      //TODO
      
      projEmptyTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projEmpty' should have been destroyed")
            
    }
    
}