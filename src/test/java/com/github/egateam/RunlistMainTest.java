/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class RunlistMainTest {
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

    @Test(description = "Test no command")
    public void testMain() throws Exception {

        String[] args = {};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("No command specified"), "No command");
    }

    @Test(description = "Test usage")
    public void testUsage() throws Exception {

        String[] args = {"--help"};
        Runlist.main(args);

        Assert.assertTrue(this.stdoutContent.toString().contains("Options:"), "Usage");
    }

    @Test(description = "Test non-existing")
    public void testNonExisting() throws Exception {

        String[] args = {"non-existing"};
        Runlist.main(args);

        Assert.assertTrue(this.stderrContent.toString().contains("Expected a command"), "Non-existing command");
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