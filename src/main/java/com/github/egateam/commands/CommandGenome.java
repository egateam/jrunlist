package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.github.egateam.IntSpan;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parameters(commandDescription = "Convert chr.size to runlists")
public class CommandGenome {

    @Parameter(description = "<infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = "--help", help = true)
    private boolean help;

    private void validateArgs() {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".yml";
        }
    }

    public void execute() {
        validateArgs();

        File inFile = files.get(0);

        Map<String, Integer> lengthOf = new ReadSizes(inFile, remove).read();
        Map<String, IntSpan> setOF = new HashMap<>();

        for ( Map.Entry<String, Integer> entry : lengthOf.entrySet() ) {
            IntSpan set = new IntSpan();
            set.addPair(1, entry.getValue());
            setOF.put(entry.getKey(), set);
        }

        System.err.printf("infile is: %s%n", inFile);
        System.err.printf("number of entries is: %d%n", setOF.size());
        System.err.printf("output is: %s, remove is: %s%n", outfile, remove);
        System.out.print("Genome() success\n");
    }

}
