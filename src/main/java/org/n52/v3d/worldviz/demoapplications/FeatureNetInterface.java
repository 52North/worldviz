package org.n52.v3d.worldviz.demoapplications;

/**
 *
 * @author Adhitya Kamakshidasan
 */
public interface FeatureNetInterface {
    public void setConfig(String file, String config, boolean mode);
    public void setOutputFile(String outputFile);
    public String getOutputFile();
    public void run();
}
