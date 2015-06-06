package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.core.T3dException;
import org.n52.v3d.triturus.core.T3dNotYetImplException;
import org.n52.v3d.triturus.core.T3dProcRendererMapper;

/**
 * Mapper to perform the transformation of an abstract 3-d connection-map scene to a 
 * concrete scene description (e.g. X3D, X3DOM, KML, etc.). 
 * 
 * @author Adhitya Kamakshidasan
 */
public class MprConnectionMapGenerator extends T3dProcRendererMapper
{
	// TODO Maybe it would be better to have a MprConnectionMapGeneratorX3D, let us see...
	
	public enum TargetFormats {
		X3D, KML, TEST
	}
	private TargetFormats targetFormat; 
	// TODO Benno: better use existing class OutputFormatEnum!
	
	public void setTargetFormat(TargetFormats targetFormat) {
		this.targetFormat = targetFormat;
	}

	/**
     * performs the mapping process.
     */
    public Object transform(WvizVirtualConnectionMapScene s) throws T3dException
    {
    	System.out.println(this.log()); // just to see something...
    	
    	throw new T3dNotYetImplException();
    }

	@Override
	public String log() {
		return "Mapping abstract WvizVirtualConnectionMapScene (" + this.targetFormat + ")";
	}
}
