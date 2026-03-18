package io.github.miracelwhipp.neincoco.it.mutli.module.server.integration.test;

import io.github.miracelwhipp.neincoco.it.multi.module.generated.LowPerformingMultiplication;
import io.github.miracelwhipp.neincoco.it.multi.module.server.implementation.ServerComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

public class IntegrationTest {

    @Test
    public void testComponentBranch1() {

        ServerComponent serverComponent = new ServerComponent();

        int result = serverComponent.integrationTestedMethod(20, 10);

        Assert.assertEquals(result, 2);
    }

    @Test
    public void testLowPerformingMultiplication() {

        LowPerformingMultiplication lowPerformingMultiplication = new LowPerformingMultiplication();

        int result = lowPerformingMultiplication.times(2, 2);

        Assert.assertEquals(result, 4);
    }
}
