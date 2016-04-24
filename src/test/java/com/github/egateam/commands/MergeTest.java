/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.github.egateam.Runlist;
import com.github.egateam.util.ExpandResource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;

public class MergeTest {
    // Store the original standard out before changing it.
    private final PrintStream originalStdout = System.out;
    private final PrintStream originalStderr = System.err;
    private ByteArrayOutputStream stdoutContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream stderrContent = new ByteArrayOutputStream();

    @BeforeMethod
    public void beforeTest() {
        // Redirect all System.out to stdoutContent.
        System.setOut(new PrintStream(this.stdoutContent));
        System.setErr(new PrintStream(this.stderrContent));
    }

    @Test(description = "Test command without parameters")
    public void testFailed() throws Exception {
        String[] args = {"merge"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test(description = "Test command with I.yml and II.yml")
    public void testExecute() throws Exception {
        try {
            String fileName1 = new ExpandResource("I.yml").converter();
            String fileName2 = new ExpandResource("II.yml").converter();
            String[] args = {"merge", fileName1, fileName2, "--outfile", "stdout"};
            Runlist.main(args);
        } catch ( Exception err ) {
            err.printStackTrace();
        }

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 5, "line count");
        Assert.assertTrue(this.stdoutContent.toString().contains("28547-29194"), "runlist exists");
        Assert.assertTrue(this.stdoutContent.toString().matches("(?s).*I:.+II:.*"), "chromosomes exist");
    }

    @AfterMethod
    public void afterTest() {
        // Put back the standard out.
        System.setOut(this.originalStdout);
        System.setErr(this.originalStderr);

        // Clear the stdoutContent.
        this.stdoutContent = new ByteArrayOutputStream();
        this.stderrContent = new ByteArrayOutputStream();
    }

}
