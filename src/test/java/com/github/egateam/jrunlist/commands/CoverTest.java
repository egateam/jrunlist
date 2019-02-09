/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.jrunlist.commands;

import com.github.egateam.commons.Utils;
import com.github.egateam.jrunlist.Cli;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CoverTest {
    // Store the original standard out before changing it.
    private final PrintStream           originalStdout = System.out;
    private final PrintStream           originalStderr = System.err;
    private       ByteArrayOutputStream stdoutContent  = new ByteArrayOutputStream();
    private       ByteArrayOutputStream stderrContent  = new ByteArrayOutputStream();

    @BeforeMethod
    public void beforeTest() {
        // Redirect all System.out to stdoutContent.
        System.setOut(new PrintStream(this.stdoutContent));
        System.setErr(new PrintStream(this.stderrContent));
    }

    @Test
    public void testCoverFailed() throws Exception {
        String[] args = {"cover"};
        Cli.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test(description = "Test command with S288c.txt")
    public void testExecute() throws Exception {
        String   fileName = Utils.expendResource("S288c.txt");
        String[] args     = {"cover", fileName, "--outfile", "stdout"};
        Cli.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 3, "line count");
        Assert.assertFalse(this.stdoutContent.toString().contains("S288c"), "species name");
        Assert.assertFalse(this.stdoutContent.toString().contains("1-100"), "merged");
        Assert.assertTrue(this.stdoutContent.toString().contains("1-150"), "covered");
    }

    @Test(description = "Test command with S288c.txt -c 2")
    public void testExecute2() throws Exception {
        String   fileName = Utils.expendResource("S288c.txt");
        String[] args     = {"cover", fileName, "--coverage", "2", "--outfile", "stdout"};
        Cli.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 3, "line count");
        Assert.assertFalse(this.stdoutContent.toString().contains("S288c"), "species name");
        Assert.assertFalse(this.stdoutContent.toString().contains("1-150"), "coverage 1");
        Assert.assertTrue(this.stdoutContent.toString().contains("90-100"), "coverage 2");
    }

    @Test(description = "Test command with dazzname.txt")
    public void testExecute3() throws Exception {
        String   fileName = Utils.expendResource("dazzname.txt");
        String[] args     = {"cover", fileName, "--outfile", "stdout"};
        Cli.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(this.stdoutContent.toString().contains("infile_0/1/0_514"), "chr name");
        Assert.assertTrue(this.stdoutContent.toString().contains("19-499"), "covered");
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
