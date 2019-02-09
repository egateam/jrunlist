/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.jrunlist.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.github.egateam.IntSpan;
import com.github.egateam.commons.ChrRange;
import com.github.egateam.commons.Utils;
import com.github.egateam.jrunlist.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings({"CanBeFinal", "unused"})
@Parameters(commandDescription = "Output covers on chromosomes.\n"
    + "\t\tLike `command combine`, but <infiles> are chromosome positions.\n"
    + "\t\tI:1-100\n"
    + "\t\tI(+):90-150\t Strands will be omitted.\n"
    + "\t\tS288c.I(-):190-200\tSpecies names will be omitted."
)
public class Cover {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infiles>", required = true)
    private List<String> files;

    @Parameter(names = {"--coverage", "-c"}, description = "minimal coverage")
    private int coverage = 1;

    @Parameter(names = {"--outfile", "-o"}, description = "output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    private void validateArgs() throws Exception {
        if ( files.size() < 1 ) {
            throw new ParameterException("This command need one or more input files.");
        }

        for ( String inFile : files ) {
            if ( inFile.toLowerCase().equals("stdin") ) {
                continue;
            }
            if ( !new File(inFile).isFile() ) {
                throw new IOException(String.format("The input file [%s] doesn't exist.", inFile));
            }
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        // seq_name => tier_of => IntSpan
        Map<String, Map<Integer, IntSpan>> covered = new HashMap<>();

        //----------------------------
        // Loading
        //----------------------------
        for ( String inFile : files ) {

            // Don't slurp file contents. inFile can be huge.
            // List<String> lines = Utils.readLines(inFile);

            LineIterator iter;
            if ( inFile.toLowerCase().equals("stdin") ) {
                iter = IOUtils.lineIterator(System.in, "utf-8");
            } else {
                iter = FileUtils.lineIterator(new File(inFile), "utf-8");
            }

            try {
                //----------------------------
                // Operating
                //----------------------------
                while ( iter.hasNext() ) {
                    String line = iter.nextLine();

                    ChrRange chrRange = new ChrRange(line);
                    if ( chrRange.isEmpty() ) {
                        continue;
                    }

                    String chrName = chrRange.getChr();

                    if ( !covered.containsKey(chrName) ) {
                        Map<Integer, IntSpan> tier_of = new HashMap<>();
                        tier_of.put(-1, new IntSpan(1, 1_000_000_000));
                        tier_of.put(0, new IntSpan(1, 1_000_000_000));

                        for ( int i = 1; i <= coverage; i++ ) {
                            tier_of.put(i, new IntSpan());
                        }

                        covered.put(chrName, tier_of);
                    }

                    Utils.bumpCoverage(
                        covered.get(chrName),
                        chrRange.getStart(),
                        chrRange.getEnd()
                    );

                }
            } finally {
                iter.close();
            }

        }

        //----------------------------
        // Output
        //----------------------------
        Map<String, IntSpan> setSingle = new HashMap<>();
        for ( String name : covered.keySet() ) {
            setSingle.put(name, covered.get(name).get(coverage));
        }

        StaticUtils.writeRl(outfile, Utils.toRunlist(setSingle));
    }
}
