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
      val projNodeTest = new GNS3_Manager(returnServerAddress())
      val p = projNodeTest.createProject("projNode")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projNode' should have been created")
      val one = objectTypes.LocalHub("Hyrule")
      p.addNode(one);
      val returned = checkProjectsAPI("/" + proj_id + "/nodes" )
      val obj = JSONApi.parseJSONArray(returned).value[JSONArray]
      println("here :") 
      println(obj.toJSONString())
      val obj2 = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("Name")
      
      //TODO
      
      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
      assert(obj2 == "Hyrule", "The project should have a Node nammed Hyrule, and it's not there.\n")            
    }
    
}