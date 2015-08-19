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
package org.n52.v3d.worldviz.featurenet.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileIO extends Parse {

    public File inputFile;

    public BufferedReader bufferedReader;

    public static final String FILE_EXTENSION = "net";

    public String getInputFileExtension() {
        return FILE_EXTENSION;
    }

    public boolean checkFileExtension(File input) {
        String extension = "";
        String fileName = input.getName();
        int i = fileName.lastIndexOf('.');
        if (fileName.lastIndexOf('.') > 0) {
            extension = fileName.substring(i + 1);
        }
        if (extension.equals(getInputFileExtension())) {
            return true;
        } else {
            System.err.println("Unsupported Extension");
            return false;
        }
    }

    public void read(File input) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(input));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        this.bufferedReader = bufferedReader;
    }

    public BufferedReader getReader() {
        return bufferedReader;
    }

    public void init(File inputFile) {
        this.inputFile = inputFile;
    }

    public void init(String filePath) {
        init(new File(filePath));
        if (checkFileExtension(inputFile)) {
            read(inputFile);
        }

    }
}
