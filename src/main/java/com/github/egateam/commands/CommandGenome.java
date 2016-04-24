package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import java.io.File;
import java.util.List;

@Parameters(commandDescription = "Convert chr.size to runlists")
public class CommandGenome {

    @Parameter(description = "<infile>", converter = FileConverterIn.class)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = {"--help"}, help = true)
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

        System.err.printf("infile is: %s%n", inFile);
        System.err.printf("output is: %s, remove is: %s%n", outfile, remove);
        System.out.print("Genome() success\n");
    }

}
