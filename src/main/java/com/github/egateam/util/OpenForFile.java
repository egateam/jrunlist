/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class OpenForFile {
    public static Writer convert(String fileName) {
        Object writer = null;
        if ( !fileName.equals("stdout") )
            try {
                writer = new PrintWriter(fileName);
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            }
        else {
            writer = new OutputStreamWriter(System.out);
        }
        return (Writer) writer;
    }
}
