package graph;

import graph.entity.Graph;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final int NUMBER_OF_VERTEX = 12;
    public static void main( String[] args )
    {
        System.out.println("1 Hello World!");
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Graph investigatedGraphSpring = (Graph) context.getBean("graph");
        investigatedGraphSpring.createUndirectedGraph(NUMBER_OF_VERTEX);

        Graph investigatedGraph = new Graph(NUMBER_OF_VERTEX);

        investigatedGraphSpring.showGraph();
        investigatedGraph.showGraph();

        investigatedGraphSpring.showGraphInSwingWindow();
        investigatedGraph.showGraphInSwingWindow();

        investigatedGraphSpring.calculateGraph();
        investigatedGraph.calculateGraph();
        System.out.println("4 Buy World!");
    }


}
