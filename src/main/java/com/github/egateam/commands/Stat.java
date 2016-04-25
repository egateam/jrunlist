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

@SuppressWarnings({"CanBeFinal"})
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
        Map<String, Integer> lengthOf = new ReadSizes(files.get(0), remove).invoke();

        YAMLInfo yaml = new YAMLInfo();
        Map<String, Map<String, IntSpan>> setOf = yaml.invoke(files.get(1), remove);

        //----------------------------
        // Calcing
        //----------------------------
        List<String> lines = new ArrayList<>();

        String header = "key,chr,chr_length,size,coverage\n";
        if ( !yaml.isMultiKey() ) header = header.replaceFirst("key,", "");
        if ( all ) header = header.replaceFirst("chr,", "");
        lines.add(header);

        for ( String name : yaml.getSortedNames() ) {
            Map<String, IntSpan> curSet = setOf.get(name);
            List<ChrCoverage> coverages = new ArrayList<>();
            List<String> curLines = new ArrayList<>();

            for ( String chr : curSet.keySet() ) {
                ChrCoverage coverage = new ChrCoverage(chr, lengthOf.get(chr), curSet.get(chr));
                if ( !all ) {
                    String line = coverage.csvLine();
                    if ( yaml.isMultiKey() ) line = name + "," + line;
                    curLines.add(line);
                }
                coverages.add(coverage);
            }

            String allLine = ChrCoverage.allLine(coverages);
            if ( yaml.isMultiKey() ) allLine = name + "," + allLine;
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
