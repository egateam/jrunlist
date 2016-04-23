package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Merge runlist yaml files")
public class CommandMerge {

    @Parameter(description = "<infiles>")
    private List<String> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    public void run() {

        System.out.print("Merge() success\n");
        System.out.printf("output is: %s%n", outfile);
    }

}
