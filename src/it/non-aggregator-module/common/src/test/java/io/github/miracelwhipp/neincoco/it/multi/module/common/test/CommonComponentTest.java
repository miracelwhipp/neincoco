package io.github.miracelwhipp.neincoco.it.multi.module.common.test;

import io.github.miracelwhipp.neincoco.it.multi.module.common.CommonComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommonComponentTest {

    @Test
    public void testFibonacciTrivial1() {

        CommonComponent commonComponent = new CommonComponent();

        Assert.assertEquals(commonComponent.fibonacci(0), 1);
    }

    @Test
    public void testFibonacciTrivial2() {

        CommonComponent commonComponent = new CommonComponent();

        Assert.assertEquals(commonComponent.fibonacci(1), 1);
    }

    @Test
    public void testFibonacciNonTrivial() {

        CommonComponent commonComponent = new CommonComponent();

        Assert.assertEquals(commonComponent.fibonacci(4), 5);
    }
}
