package GAPY

import org.junit._
import Assert._
import scalaj.http._

@Test
class Test_Basic {

    @Test
    def testNoProject() = {
        val gns3_server_url = "148.60.11.201"
        val gns3_server_port = "3080"

        val http = Http(gns3_server_url + ":" + gns3_server_port + "/v2/projects/")
        val response: HttpResponse[String] = http.asString

        assert(response.body == "[]", "No project should exist. Should had [] but had " + response)
    }

    @Test
    def testNodesBasic() = {
        
        /*
        id_project = ""
        val p = new ProjectManager(id_project,"http://148.60.11.201:3080")
        p.addNode("PC1", "vpcs", "local")
        .addNode("PC2", "vpcs", "local")
        .addNode("PC3", "vpcs", "local")
        .addNode("PC4", "vpcs", "local")

        var returned = RESTCall("/v2/projects/" + id_project + "/nodes","GET")


        p.removeNode("PC1")
        .removeNode("PC2")
        .removeNode("PC3")
        .removeNode("PC4")
        */
    }
}