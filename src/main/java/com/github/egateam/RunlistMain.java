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
import com.beust.jcommander.Parameters;

import com.github.egateam.commands.*;

@Parameters
public class RunlistMain {


    @Parameter(names = {"--help", "-h"}, description = "Print this help and quit", help = true)
    private boolean help = false;

    private void run(String[] args) throws Exception {

        JCommander jCommander = new JCommander(this);

        jCommander.addCommand("genome", new CommandGenome());
        jCommander.addCommand("merge", new CommandMerge());

        String parsedCommand;
        try {
            jCommander.parse(args);
            parsedCommand = jCommander.getParsedCommand();

            if ( help ) {
                jCommander.usage();
                return;
            }

            if ( parsedCommand == null ) throw new Exception("No command specified");
        } catch ( Exception e ) {
            System.err.println(e.getMessage());
            jCommander.usage();
            return;
        }

        Object command = jCommander.getCommands().get(parsedCommand).getObjects().get(0);

        if ( command instanceof CommandGenome ) {
            CommandGenome commandNew = (CommandGenome) command;
            commandNew.run();
        } else if ( command instanceof CommandMerge ) {
            CommandMerge commandNew = (CommandMerge) command;
            commandNew.run();
        }


    }

    /*
    mvn clean verify
    java -jar target/jrunlist-0.1.0-SNAPSHOT-with-dependencies.jar
     */
    public static void main(String[] args) throws Exception {
        new RunlistMain().run(args);
    }

}
