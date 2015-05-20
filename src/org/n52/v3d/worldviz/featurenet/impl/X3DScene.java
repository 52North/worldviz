package org.n52.v3d.worldviz.featurenet.impl;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;

public class X3DScene extends Parse{

    public ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
    public ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();

    public X3DScene(ArrayList<Vertex> vertexArrayList,ArrayList<Edge> edgeArrayList){
        this.vertexArrayList = vertexArrayList;
        this.edgeArrayList = edgeArrayList;
    }

    private BufferedWriter document;
    
    private static final Double radius = 0.1500;

    private void writeLine(String pLine) {
        try {
            document.write(pLine);
            document.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void writeLine(){
        try {
            document.newLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }




    public void writeToX3dFile(String pFilename) {

        try {
            document = new BufferedWriter(new FileWriter(pFilename));

            writeLine("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            writeLine("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
            writeLine("  <head>");
            writeLine("    <link rel=\"stylesheet\" type=\"text/css\" href=\"http://www.x3dom.org/x3dom/release/x3dom.css\" />");
            writeLine("    <script type=\"text/javascript\" src=\"http://www.x3dom.org/x3dom/release/x3dom.js\"></script>");
            writeLine("  </head>");
            writeLine("  <body>");
            writeLine();
            writeLine();
            writeLine("<X3D xmlns=\"http://www.web3d.org/specifications/x3d-namespace\" showStat=\"false\" showriteLineog=\"true\"");
            writeLine("  x=\"0px\" y=\"0px\" width=\"400px\" height=\"400px\">");
            writeLine();
            writeLine("  <Scene>");
            writeLine();

            for(Edge edge:edgeArrayList){
                Vertex firstVertex = vertexArrayList.get(edge.firstVertex-1);
                Vertex secondVertex = vertexArrayList.get(edge.secondVertex-1);
                writeLine("    <Transform>");
                writeLine("      <Shape>");
                writeLine("        <Appearance>");
                writeLine("          <Material emissiveColor=\"" + "0 0 1" + "\"/>");
                writeLine("        </Appearance>");
                writeLine("        <LineSet vertexCount='2'" + ">");
                writeLine("          <Coordinate point='");
                writeLine("                             " + firstVertex.getX() + " " + firstVertex.getY() + " " + firstVertex.getZ());
                writeLine("                             " + secondVertex.getX() + " " + secondVertex.getY() + " " + secondVertex.getZ());
                writeLine("            '></Coordinate>");
                writeLine("        </LineSet>");
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();
            }

            for(Vertex vertex:vertexArrayList){
                writeLine("    <Transform translation='" + vertex.getX() + " " + vertex.getY() + " " + vertex.getZ() + "'>");
                writeLine("      <Shape>");
                writeLine("        <Appearance>");
                writeLine("          <Material diffuseColor=\"" + "1 0 0" + "\"/>");
                writeLine("        </Appearance>");
                writeLine("        <Sphere radius='0.1500'/>");
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();

                writeLine("    <Transform translation='" + (vertex.getX()+0.1500)+" "+vertex.getY()+" " +vertex.getZ()+"'>");
                writeLine("      <Shape>");
                writeLine("        <Appearance>");
                writeLine("          <Material diffuseColor=\"" +"0 0 0"+ "\"/>");
                writeLine("        </Appearance>");
                writeLine("        <Text string='"+vertex.getLabel()+"'/>");
                writeLine("            <FontStyle family='MS Sans Serif' size='0.40000'/>");
                writeLine("        </Text>");
                writeLine("      </Shape>");
                writeLine("    </Transform>");
                writeLine();

            }

            writeLine("  </Scene>");
            writeLine();
            writeLine("</X3D>");
            writeLine();
            writeLine();
            writeLine("  </body>");
            writeLine("</html>");
            document.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("Could not access file \"" + pFilename + "\".");
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


}
