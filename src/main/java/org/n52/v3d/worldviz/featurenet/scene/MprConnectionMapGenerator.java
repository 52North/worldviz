/**
 * Copyright (C) 2015-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *  - Apache License, version 2.0
 *  - Apache Software License, version 1.0
 *  - GNU Lesser General Public License, version 3
 *  - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *  - Common Development and Distribution License (CDDL), version 1.0.
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.v3d.worldviz.featurenet.scene;

import org.n52.v3d.triturus.core.T3dException;
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
        
        public TargetFormats getTargetFormat(){
            return targetFormat;
        }

    /**
     * performs the mapping process.
     */
    public Object transform(WvizVirtualConnectionMapScene s) throws T3dException{
    	
        if( getTargetFormat() == TargetFormats.X3D ){
            return new WvizConnectionMapSceneX3d(s);
        }
        else{
            return null;
        }
        
    }

	@Override
	public String log() {
		return "Mapping abstract WvizVirtualConnectionMapScene (" + this.targetFormat + ")";
	}
}
