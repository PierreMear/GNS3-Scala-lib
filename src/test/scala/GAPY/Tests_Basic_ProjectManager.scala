package GAPY

import org.junit._
import Assert._
import GAPY._

@Test
class Test_Basic {

    @Test
    def testNoProject() = {
        val gns3_server_url = "http://148.60.11.201"
        val gns3_server_port = "3080"

        val all_projects = RESTCall("/v2/projects/" ,"GET")
        assert(all_projects === "[]", "No project should exist")
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