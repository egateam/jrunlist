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
import com.github.egateam.util.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

@SuppressWarnings("CanBeFinal")
@Parameters(commandDescription = "Coverage on chromosomes for runlists")
public class Stat {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<chr.size> <infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = {"--all"}, description = "Only write whole genome stats.")
    private Boolean all = false;

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

    private void validateArgs() {
        if ( files.size() != 2 ) {
            throw new ParameterException("This command need two input files.");
        }

        if ( outfile == null ) {
            outfile = files.get(1) + ".csv";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, Integer> lengthOf = new ReadSizes(files.get(0), remove).read();
        Map<String, ?> yml = new ReadYAML(files.get(1)).read();
        Set<String> allNames = new HashSet<>();

        // check depth of YAML
        // get one (maybe not first) value from Map
        boolean notMultiKey = yml.entrySet().iterator().next().getValue() instanceof String;

        Map<String, Map<String, IntSpan>> setOf = new HashMap<>();
        if ( notMultiKey ) {
            setOf.put("__single", new Transform(yml, remove).toIntSpan());
            allNames.add("__single");
        } else {
            for ( Map.Entry<String, ?> entry : yml.entrySet() ) {
                String key = entry.getKey();
                //noinspection unchecked
                HashMap<String, String> value = (HashMap<String, String>) entry.getValue();

                setOf.put(key, new Transform(value, remove).toIntSpan());
                allNames.add(key);
            }
        }

        ArrayList<String> sortedNames = new ArrayList<>(allNames);
        Collections.sort(sortedNames);

        //----------------------------
        // Calcing
        //----------------------------
        List<String> lines = new ArrayList<>();

        String header = "key,chr,chr_length,size,coverage\n";
        if ( notMultiKey ) header = header.replaceFirst("key,", "");
        if ( all ) header = header.replaceFirst("chr,", "");
        lines.add(header);

        for ( String name : sortedNames ) {
            Map<String, IntSpan> curSet = setOf.get(name);
            List<ChrCoverage> coverages = new ArrayList<>();
            List<String> curLines = new ArrayList<>();

            for ( String chr : curSet.keySet() ) {
                ChrCoverage coverage = new ChrCoverage(chr, lengthOf.get(chr), curSet.get(chr));
                if ( !all ) {
                    String line = coverage.csvLine();
                    if ( !notMultiKey ) line = name + "," + line;
                    curLines.add(line);
                }
                coverages.add(coverage);
            }

            String allLine = ChrCoverage.allLine(coverages);
            if ( !notMultiKey ) allLine = name + "," + allLine;
            if ( all ) allLine = allLine.replaceFirst("all,", "");
            curLines.add(allLine);

            Collections.sort(curLines);
            lines.addAll(curLines);
        }

        //----------------------------
        // Output
        //----------------------------
        if ( outfile.equals("stdout") )
            for ( String line : lines ) {
                System.out.print(line);
            }
        else {
            FileUtils.writeLines(new File(outfile), "UTF-8", lines);
        }
    }
}
