/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class ReadSizes {
    private File file;
    private boolean remove;

    public ReadSizes(File file, boolean remove) {
        this.file = file;
        this.remove = remove;
    }

    public Map<String, Integer> read() {
        HashMap<String, Integer> lengthOf = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                line = line.trim();
                String[] fields = line.split("\\t");
                if ( fields.length == 2 ) {
                    if ( remove ) {
                        fields[0] = fields[0].replaceFirst("chr0?", "");
                    }
                    lengthOf.put(fields[0], Integer.parseInt(fields[1]));
                }
            }
        } catch ( Exception err ) {
            err.printStackTrace();
        }

        return lengthOf;
    }
}
