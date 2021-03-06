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

public class StatTest {
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
    public void testStatFailed() throws Exception {
        String[] args = {"stat"};
        Cli.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test(description = "Test command with intergenic.yml")
    public void testExecute1() throws Exception {
        String fileName1 = Utils.expendResource("chr.sizes");
        String fileName2 = Utils.expendResource("intergenic.yml");
        String[] args = {"stat", fileName1, fileName2, "--outfile", "stdout"};
        Cli.main(args);

        String[] lines = this.stdoutContent.toString().split("\r\n|\r|\n");
        Assert.assertEquals(lines.length, 18, "line count");
        Assert.assertEquals(lines[1].split(",").length, 4, "field count");
        Assert.assertTrue(this.stdoutContent.toString().contains("all,12071326,1059702,"), "result calced");
    }

    @Test(description = "Test command with intergenic.yml and --all")
    public void testExecute2() throws Exception {
        String fileName1 = Utils.expendResource("chr.sizes");
        String fileName2 = Utils.expendResource("intergenic.yml");
        String[] args = {"stat", fileName1, fileName2, "--all", "--outfile", "stdout"};
        Cli.main(args);

        String[] lines = this.stdoutContent.toString().split("\r\n|\r|\n");
        Assert.assertEquals(lines.length, 2, "line count");
        Assert.assertEquals(lines[1].split(",").length, 3, "field count");
        Assert.assertFalse(this.stdoutContent.toString().contains("all"), "no literal all");
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
