package io.github.miracelwhipp.neincoco.it.simple.test;

import io.github.miracelwhipp.neincoco.it.simple.Covered;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CoverTest {

    @Test
    public void testAddition() {

        System.out.println("testAddition");

        Covered covered = new Covered();

        Assert.assertEquals(covered.add(1, 2), 3);
    }
}
