package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Convert chr.size to runlists")
public class CommandGenome {

    @Parameter(description = "<infile>")
    private List<String> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    public void run() {

        System.out.print("Genome() success\n");
        System.out.printf("output is: %s, remove is: %s%n", outfile, remove);
    }

}
