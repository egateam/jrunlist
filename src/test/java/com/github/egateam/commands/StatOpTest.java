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

public class StatOpTest {
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
    public void testStatOpFailed() throws Exception {
        String[] args = {"statop"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Except parameters");
    }

    @Test(description = "Test command with intergenic.yml and repeat.yml")
    public void testExecute1() throws Exception {
        String fileName1 = Utils.expendResource("chr.sizes");
        String fileName2 = Utils.expendResource("intergenic.yml");
        String fileName3 = Utils.expendResource("repeat.yml");

        String[] args = {"statop", fileName1, fileName2, fileName3, "--op", "intersect", "--outfile", "stdout"};
        Runlist.main(args);

        String[] lines = this.stdoutContent.toString().split("\r\n|\r|\n");
        Assert.assertEquals(lines.length, 18, "line count");
        Assert.assertEquals(lines[1].split(",").length, 8, "field count");
        Assert.assertTrue(this.stdoutContent.toString().contains("36721"), "sum exists");
        Assert.assertTrue(this.stdoutContent.toString().matches("(?s).*I.+XVI.*"), "chromosomes exists");
    }

    @Test(description = "Test command with intergenic.yml, repeat.yml and --all")
    public void testExecute2() throws Exception {
        String fileName1 = Utils.expendResource("chr.sizes");
        String fileName2 = Utils.expendResource("intergenic.yml");
        String fileName3 = Utils.expendResource("repeat.yml");

        String[] args = {"statop", fileName1, fileName2, fileName3, "--all", "--op", "intersect", "--outfile", "stdout"};
        Runlist.main(args);

        String[] lines = this.stdoutContent.toString().split("\r\n|\r|\n");
        Assert.assertEquals(lines.length, 2, "line count");
        Assert.assertEquals(lines[1].split(",").length, 7, "field count");
        Assert.assertTrue(this.stdoutContent.toString().contains("36721"), "sum exists");
        Assert.assertFalse(this.stdoutContent.toString().matches("(?s).*I.+XVI.*"), "chromosomes do not exists");
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
