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

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

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

        if ( op.startsWith("dif") ) {
            op = "diff";
        } else if ( op.startsWith("uni") ) {
            op = "union";
        } else if ( op.startsWith("int") ) {
            op = "intersect";
        } else if ( op.startsWith("xor") ) {
            op = "xor";
        } else {
            throw new RuntimeException(String.format("op [%s] is invalid", op));
        }

        if ( outfile == null ) {
            outfile = files.get(0) + "." + op + ".yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        YAMLInfo yaml = new YAMLInfo();
        Map<String, Map<String, IntSpan>> setOf = yaml.invoke(files.get(0), remove);
        Map<String, IntSpan> setSingle = yaml.invokeSingle(files.get(1), remove);

        for ( String chr : yaml.getSortedChrs() ) {
            if ( !setSingle.containsKey(chr) ) {
                setSingle.put(chr, new IntSpan());
            }
        }

        //----------------------------
        // Operating
        //----------------------------
        // Create empty IntSpan for each name:chr
        Map<String, Map<String, IntSpan>> opResultOf = new HashMap<>();
        for ( String name : yaml.getSortedNames() ) {
            Map<String, IntSpan> setOne = setOf.get(name);

            for ( String chr : yaml.getSortedChrs() ) {
                if ( !setOne.containsKey(chr) ) {
                    setOne.put(chr, new IntSpan());
                }
            }

            Map<String, IntSpan> setOP = new HashMap<>();
            for ( String chr : yaml.getSortedChrs() ) {
                Method methodOP = IntSpan.class.getMethod(op, IntSpan.class);
                IntSpan opIntSpan = (IntSpan) methodOP.invoke(setOne.get(chr), setSingle.get(chr));
                setOP.put(chr, opIntSpan);
            }

            opResultOf.put(name, setOP);
        }

        //----------------------------
        // Output
        //----------------------------
        if ( yaml.isMultiKey() ) {
            new WriteYAML(
                outfile,
                new Transform(opResultOf, remove).toRunlist()
            ).invoke();
        } else {
            new WriteYAML(
                outfile,
                new Transform(opResultOf.get(YAMLInfo.getSingleKey()), remove).toRunlist()
            ).invoke();
        }
    }
}
