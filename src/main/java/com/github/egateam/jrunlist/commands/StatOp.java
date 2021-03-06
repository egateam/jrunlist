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
import com.github.egateam.commons.Utils;
import com.github.egateam.jrunlist.util.ChrCoverageOp;
import com.github.egateam.jrunlist.util.RlInfo;
import com.github.egateam.jrunlist.util.StaticUtils;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Coverage on chromosomes for one YAML crossed another\n"
    + "\t\tOnly the *first* file can contain multiple sets of runlists.")
public class StatOp {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<chr.size> <infile1> <infile2>", required = true)
    private List<String> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--op"}, description = "operations: intersect, union, diff or xor.")
    private String op = "intersect";

    @Parameter(names = {"--base", "-b"}, description = "basename of infile2")
    private String base;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = {"--all"}, description = "Only write whole genome stats.")
    private Boolean all = false;

    private void validateArgs() {
        if ( files.size() != 3 ) {
            throw new ParameterException("This command need three input files.");
        }

        op = StaticUtils.validateOpCompare(op);

        if ( base == null ) {
            base = FilenameUtils.getBaseName(files.get(2));
        }

        if ( outfile == null ) {
            outfile = files.get(1) + "." + op + ".csv";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, Integer> lengthOf = Utils.readSizes(files.get(0), remove);

        RlInfo                            yaml      = new RlInfo();
        Map<String, Map<String, IntSpan>> setOf     = yaml.load(files.get(1), remove);
        Map<String, IntSpan>              setSingle = yaml.loadSingle(files.get(2), remove);

        //----------------------------
        // Operating
        //----------------------------
        Map<String, Map<String, IntSpan>> opResultOf = yaml.opResult(op, setOf, setSingle);

        //----------------------------
        // Calcing
        //----------------------------
        List<String> lines = new ArrayList<>();

        String header = String.format("key,chr,chrLength,size,%sLength,%sSize,c1,c2,ratio", base, base);
        if ( !yaml.isMulti() ) header = header.replaceFirst("key,", "");
        if ( all ) header = header.replaceFirst("chr,", "");
        lines.add(header);

        for ( String name : yaml.getSortedNames() ) {
            Map<String, IntSpan> setOne    = setOf.get(name);
            Map<String, IntSpan> resultOne = opResultOf.get(name);

            List<ChrCoverageOp> coverages = new ArrayList<>();
            List<String>        curLines  = new ArrayList<>();

            for ( String chr : yaml.getSortedChrs() ) {
                if ( setOne.containsKey(chr) ) {
                    ChrCoverageOp coverage = new ChrCoverageOp(
                        chr,
                        lengthOf.get(chr),
                        setOne.get(chr).size(),
                        setSingle.get(chr).size(),
                        resultOne.get(chr).size()
                    );
                    if ( !all ) {
                        String line = coverage.csvLine();
                        if ( yaml.isMulti() ) line = name + "," + line;
                        curLines.add(line);
                    }
                    coverages.add(coverage);
                }
            }

            String allLine = ChrCoverageOp.allLine(coverages);
            if ( yaml.isMulti() ) allLine = name + "," + allLine;
            if ( all ) allLine = allLine.replaceFirst("all,", "");
            curLines.add(allLine);

            lines.addAll(curLines);
        }

        //----------------------------
        // Output
        //----------------------------
        Utils.writeLines(outfile, lines);
    }
}
