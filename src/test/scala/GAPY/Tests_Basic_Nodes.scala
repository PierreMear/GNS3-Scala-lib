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

      //On créé les objets Nodes que l'on va ajouter au projet
      val one = objectTypes.LocalHub("Hyrule")
      val two = objectTypes.LocalVpcs("Ganon")
      val three = objectTypes.LocalVpcs("Zelda")
      val four = objectTypes.LocalVpcs("Link")
      p.addNode(one);
      p.addNode(two)
      p.addNode(three)
      p.addNode(four)

      //On récupère les données sur les nodes du projet

      var returned = checkProjectsAPI("/" + proj_id + "/nodes" )

      //On récupère les données des
      val obj_hyrule = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("name").value[String]
      val obj_ganon = JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("name").value[String]
      val obj_zelda = JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("name").value[String]
      val obj_link = JSONApi.parseJSONArray(returned).getFromArray(3).getFromObject("name").value[String]

      val link_one = objectTypes.SimpleLink(one, two, 0,0)
      val link_two = objectTypes.SimpleLink(one, three, 1,0)
      val link_three = objectTypes.SimpleLink(one, four, 2,0)

<<<<<<< HEAD
      try {
        p.addLink(link_one)
        p.addLink(link_two).addLink(link_three)
      } catch {
        case ex: Exception => throw new Exception("Error : \n" + ex.printStackTrace()) 
      }
=======
      p.addLink(link_one)
      p.addLink(link_two).addLink(link_three)
>>>>>>> b1e5f7a4379b59d61ae83615c979d00ef1d15e9e

      returned = checkProjectsAPI("/" + proj_id + "/nodes" )
      val obj = JSONApi.parseJSONArray(returned).value[JSONArray]
      println(obj.toJSONString()) //On print les données pour observer la structure du document renvoyé
      
      //TODO
      
      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
      assert(obj_hyrule == "Hyrule", "The project should have a Node nammed Hyrule, and it's not there. " + obj_hyrule)
      assert(obj_ganon == "Ganon", "The project should have a Node nammed Ganon, and it's not there. " + obj_ganon) 
      assert(obj_zelda == "Zelda", "The project should have a Node nammed Zelda, and it's not there. " + obj_zelda) 
      assert(obj_link == "Link", "The project should have a Node nammed Link, and it's not there. " + obj_link)             
    }
    
}