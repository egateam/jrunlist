/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.github.egateam.Runlist;
import com.github.egateam.commons.Utils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SpanTest {
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
    public void testSpanFailed() throws Exception {
        String[] args = {"span"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test(description = "Test command with cover brca2.yml")
    public void testExecuteCover() throws Exception {
        String   fileName = Utils.expendResource("brca2.yml");
        String[] args     = {"span", fileName, "--op", "cover", "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(lines.contains("32316461-32398770"), "cover");
    }

    @Test(description = "Test command with fill brca2.yml")
    public void testExecuteFill() throws Exception {
        String   fileName = Utils.expendResource("brca2.yml");
        String[] args     = {"span", "--op", "fill", "-n", "1000", fileName, "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(lines.contains("32325076-32326613"), "emerged");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() != 25, "original");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() == 18, "new");
    }

    @Test(description = "Test command with trim brca2.yml")
    public void testExecuteTrim() throws Exception {
        String   fileName = Utils.expendResource("brca2.yml");
        String[] args     = {"span", "--op", "trim", "-n", "200", fileName, "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() != 25, "original");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() == 3, "new");
    }

    @Test(description = "Test command with pad brca2.yml")
    public void testExecutePad() throws Exception {
        String   fileName = Utils.expendResource("brca2.yml");
        String[] args     = {"span", "--op", "pad", "-n", "2000", fileName, "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() != 25, "original");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() == 6, "new");
    }

    @Test(description = "Test command with excise brca2.yml")
    public void testExecuteExcise() throws Exception {
        String   fileName = Utils.expendResource("brca2.yml");
        String[] args     = {"span", "--op", "excise", "-n", "400", fileName, "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 2, "line count");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() != 25, "original");
        Assert.assertTrue(lines.length() - lines.replace(",", "").length() == 3, "new");
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
