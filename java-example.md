# Java example

Here is an example on how to use the library in Java to create a project, add a few nodes and create links between them.

However this library is meant to be used in Scala and is not comfortable to use in Java

```java
import gapy.GNS3_Manager;
import gapy.ProjectManager;
import gapy.objectTypes.*;

public class Main {

    public static void main(String args[]){
        //first create the project manager and give it the address of your GNS3 server
        GNS3_Manager manager = new GNS3_Manager("http://localhost:8080");
        // then create the projet and give it a name
        ProjectManager project = manager.createProject("myFirstProject");

        //this example shows how to create a network with 4 local VPCs connected to a local hub
        Node PC1 = new LocalVpcs("PC1");
        Node PC2 = new LocalVpcs("PC2");
        Node PC3 = new LocalVpcs("PC3");
        Node PC4 = new LocalVpcs("PC4");

        Node localHub = new LocalHub("LocalHub");

        //This link connects the PC1 on port 0 to the LocalHub on port 0
        Link link1 = new SimpleLink(PC1,localHub,0,0);
        Link link2 = new SimpleLink(PC2,localHub,0,1);
        //This link connects the PC1 on port 0 to the LocalHub on port 02
        Link link3 = new SimpleLink(PC3,localHub,0,2);
        Link link4 = new SimpleLink(PC4,localHub,0,3);

        //Now add all the nodes and links to your project
        project.addNode(PC1);
        project.addNode(PC2);
        project.addNode(PC3);
        project.addNode(PC4);
        project.addNode(localHub);
        project.addLink(link1);
        project.addLink(link2);
        project.addLink(link3);
        project.addLink(link4);
    }
}
```
