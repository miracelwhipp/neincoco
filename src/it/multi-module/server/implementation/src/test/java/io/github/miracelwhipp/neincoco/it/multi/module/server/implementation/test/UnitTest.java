package io.github.miracelwhipp.neincoco.it.multi.module.server.implementation.test;

import io.github.miracelwhipp.neincoco.it.multi.module.server.implementation.ServerComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UnitTest {

    @Test
    public void testBranch1() {

        ServerComponent serverComponent = new ServerComponent();

        int result = serverComponent.unitTestedMethod(10, 20);

        Assert.assertEquals(result, -10);
    }

    @Test
    public void testBranch2() {

        ServerComponent serverComponent = new ServerComponent();

        int result = serverComponent.unitTestedMethod(20, 10);

        Assert.assertEquals(result, -10);
    }

}
