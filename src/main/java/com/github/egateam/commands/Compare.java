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
import com.github.egateam.commons.Utils;
import com.github.egateam.util.FileConverterIn;
import com.github.egateam.util.StaticUtils;
import com.github.egateam.util.RlInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Compare 2 YAML files.\n"
    + "\t\tOnly the *first* file can contain multiple sets of runlists.")
public class Compare {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile1> <infile2>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--op"}, description = "operations: intersect, union, diff or xor.")
    private String op = "intersect";

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    private void validateArgs() {
        if ( files.size() != 2 ) {
            throw new ParameterException("This command need two input files.");
        }

        op = StaticUtils.validateOpCompare(op);

        if ( outfile == null ) {
            outfile = files.get(0) + "." + op + ".yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        RlInfo                            yaml      = new RlInfo();
        Map<String, Map<String, IntSpan>> setOf     = yaml.load(files.get(0), remove);
        Map<String, IntSpan>              setSingle = yaml.loadSingle(files.get(1), remove);

        //----------------------------
        // Operating
        //----------------------------
        Map<String, Map<String, IntSpan>> opResultOf = yaml.opResult(op, setOf, setSingle);

        //----------------------------
        // Output
        //----------------------------
        if ( yaml.isMulti() ) {
            StaticUtils.writeRl(
                outfile,
                StaticUtils.toRunlistMulti(opResultOf)
            );
        } else {
            StaticUtils.writeRl(
                outfile,
                Utils.toRunlist(opResultOf.get(RlInfo.getSingleKey()))
            );
        }
    }
}
