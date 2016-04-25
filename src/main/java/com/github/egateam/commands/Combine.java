/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.github.egateam.IntSpan;
import com.github.egateam.util.FileConverterIn;
import com.github.egateam.util.ReadYAML;
import com.github.egateam.util.Transform;
import com.github.egateam.util.WriteYAML;

import java.io.File;
import java.util.*;

@SuppressWarnings("CanBeFinal")
@Parameters(commandDescription = "Combine multiple sets of runlists in a yaml file.\n"
    + "\t\tIt's expected that the YAML file is --mk.\n"
    + "\t\tOtherwise this command will make no effects.")
public class Combine {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

    private void validateArgs() {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".combine.yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        File inFile = files.get(0);
        Map<String, ?> yml = new ReadYAML(inFile).read();
        Set<String> allChrs = new HashSet<>();
        Set<String> allNames = new HashSet<>();

        // check depth of YAML
        // get one (maybe not first) value from Map
        boolean notMultiKey = yml.entrySet().iterator().next().getValue() instanceof String;

        Map<String, Map<String, IntSpan>> setOf = new HashMap<>();
        if ( notMultiKey ) {
            allChrs.addAll(yml.keySet());
            allNames.add("__single");

            setOf.put("__single", new Transform(yml, remove).toIntSpan());
        } else {
            for ( Map.Entry<String, ?> entry : yml.entrySet() ) {
                String key = entry.getKey();
                //noinspection unchecked
                HashMap<String, String> value = (HashMap<String, String>) entry.getValue();

                setOf.put(key, new Transform(value, remove).toIntSpan());
                allChrs.addAll(value.keySet());

                allNames.add(key);
            }
        }

        //----------------------------
        // Operating
        //----------------------------
        Map<String, IntSpan> opResultOf = new HashMap<>();
        for ( String key : allChrs ) {
            opResultOf.put(key, new IntSpan());
        }

        for ( String name : allNames ) {
            Map<String, IntSpan> curSet = setOf.get(name);
            for ( String chr : curSet.keySet() ) {
                IntSpan curResult = opResultOf.get(chr);
                IntSpan curIntSpan = curSet.get(chr);
                opResultOf.put(chr, curResult.add(curIntSpan));
            }
        }

        //----------------------------
        // Output
        //----------------------------
        new WriteYAML(outfile, new Transform(opResultOf, remove).toRunlist()).write();
    }
}
