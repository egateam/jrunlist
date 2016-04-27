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
import java.io.PrintStream;

public class GenomeTest {
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

    @Test
    public void testNoArgs() throws Exception {
        String[] args = {"genome"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test
    public void testRedundantArgs() throws Exception {
        String fileName = new ExpandResource("chr.sizes").invoke();
        String[] args = {"genome", fileName, fileName, "--outfile", "stdout"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("input file"),
            "Except parameters");
    }

    @Test(description = "Test command with chr.sizes")
    public void testExecute() throws Exception {
        String fileName = new ExpandResource("chr.sizes").invoke();
        String[] args = {"genome", fileName, "--outfile", "stdout"};
        Runlist.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 17, "line count");
        Assert.assertTrue(this.stdoutContent.toString().contains("I: \"1-230218\""), "first chromosome");
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
