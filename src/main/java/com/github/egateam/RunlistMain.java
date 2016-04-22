/**
 * <tt>jrunlist</tt> operates chromosome runlist files.
 * <p>
 * <strong>AUTHOR</strong>
 * Qiang Wang, wang-q@outlook.com
 * <p>
 * <strong>COPYRIGHT AND LICENSE</strong>
 * This software is copyright (c) 2016 by Qiang Wang.
 * <p>
 * This is free software; you can redistribute it and/or modify it under the same terms as the Perl
 * 5 programming language system itself.
 *
 * @author Qiang Wang
 * @since 1.7
 */

package com.github.egateam;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class RunlistMain {

    private static class Args {
        @Parameter(names = "--verbose")
        boolean verbose;

        @Parameter(names = "--name")
        String name;

        @Parameter(names = {"--output", "-o"}, required = true, description = "The output file")
        String file;
    }

    /*
    mvn clean verify
    java -jar target/jrunlist-0.1.0-SNAPSHOT-jar-with-dependencies.jar
     */
    public static void main(String[] argv) {
        Args args = new Args();
        new JCommander(args).parse(argv);
        System.out.printf("Hello %s, output is: %s, verbose is: %s%n", args.name, args.file,
            args.verbose);
    }
}
