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

public class CompareTest {
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
    public void testNoArgs() throws Exception {
        String[] args = {"compare"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Main parameters are required"),
            "Expect parameters");
    }

    @Test
    public void testInsufficientArgs() throws Exception {
        String   fileName1 = new ExpandResource("intergenic.yml").invoke();
        String[] args      = {"compare", fileName1};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("input file"),
            "Expect parameters");
    }

    @Test(description = "Intersect of intergenic.yml and repeat.yml")
    public void testExecuteIntersect1() throws Exception {
        String   fileName1 = new ExpandResource("intergenic.yml").invoke();
        String   fileName2 = new ExpandResource("repeat.yml").invoke();
        String[] args      = {"compare", "--op", "intersect", fileName1, fileName2, "--outfile", "stdout"};
        Runlist.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 17, "line count");
        Assert.assertTrue(this.stdoutContent.toString().contains("878539-878709"), "runlist exists");
        Assert.assertTrue(this.stdoutContent.toString().matches("(?s).*I:.+XVI:.*"), "chromosomes exists");
    }

    @Test(description = "Union of intergenic.yml and repeat.yml")
    public void testExecuteUnion() throws Exception {
        String   fileName1 = new ExpandResource("intergenic.yml").invoke();
        String   fileName2 = new ExpandResource("repeat.yml").invoke();
        String[] args      = {"compare", "--op", "union", fileName1, fileName2, "--outfile", "stdout"};
        Runlist.main(args);

        Assert.assertEquals(this.stdoutContent.toString().split("\r\n|\r|\n").length, 17, "line count");
        Assert.assertFalse(this.stdoutContent.toString().contains("\"-\""), "no empty runlists");
        Assert.assertTrue(this.stdoutContent.toString().matches("(?s).*I:.+XVI:.*"), "chromosomes exists");
    }

    @Test(description = "I.II.yml and intergenic.yml")
    public void testExecuteIntersect2() throws Exception {
        String   fileName1 = new ExpandResource("I.II.yml").invoke();
        String   fileName2 = new ExpandResource("intergenic.yml").invoke();
        String[] args      = {"compare", fileName1, fileName2, "--outfile", "stdout"};
        Runlist.main(args);

        String lines = this.stdoutContent.toString();
        Assert.assertEquals(lines.split("\r\n|\r|\n").length, 35, "line count");
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
