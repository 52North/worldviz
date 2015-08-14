package org.n52.v3d.worldviz.featurenet.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("BackgroundWorldMap")
public class BackgroundWorldMap {
	
	@XStreamAlias("useWorldMap")
	@XStreamAsAttribute
	boolean useWorldMap;
	
	@XStreamAlias("texturePath")
	@XStreamAsAttribute
	String texturePath;

	public boolean isUseWorldMap() {
		return useWorldMap;
	}

	public String getTexturePath() {
		return texturePath;
	}

}
