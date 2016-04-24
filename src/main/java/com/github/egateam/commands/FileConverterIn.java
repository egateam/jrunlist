/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.IStringConverter;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class FileConverterIn implements IStringConverter<File> {
    @Override
    public File convert(String value) {
        File inFile = new File(value);
        if ( !inFile.isFile() ) {
            throw new RuntimeException(String.format("The input file [%s] doesn't exist.", value));
        }
        return inFile;
    }
}
