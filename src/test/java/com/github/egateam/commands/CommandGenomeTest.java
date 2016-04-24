/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.github.egateam.RunlistMain;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;

public class CommandGenomeTest {
    // Store the original standard out before changing it.
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();


    @BeforeMethod
    public void beforeTest() {
        // Redirect all System.out to consoleContent.
        System.setOut(new PrintStream(this.consoleContent));
    }

    @Test(description = "Test genome command")
    public void testExecute() throws Exception {
        // http://stackoverflow.com/questions/5529532/how-to-get-a-test-resource-file
        URL url = Thread.currentThread().getContextClassLoader().getResource("chr.sizes");
        File file;
        if ( url != null ) {
            file = new File(url.getPath());
            String[] args = {"genome", file.toString(), "--outfile", "stdout"};
            new RunlistMain().execute(args);
        }

        Assert.assertTrue(this.consoleContent.toString().contains("I: \"1-230218\""), "first chromosome");
    }

    @AfterMethod
    public void afterTest() {
        // Put back the standard out.
        System.setOut(this.originalStdOut);

        // Print what has been captured.
        System.out.println(this.consoleContent.toString());
        System.out.printf("    ==>Captured Console length=%d\n", this.consoleContent.toString().length());

        // Clear the consoleContent.
        this.consoleContent = new ByteArrayOutputStream();
    }

}
