package io.github.miracelwhipp.neincoco.it.multi.module.client.test;

import io.github.miracelwhipp.neincoco.it.multi.module.client.ClientComponent;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClientComponentTest {

    @Test
    public void testTrivial() {

        ClientComponent clientComponent = new ClientComponent();

        Assert.assertEquals(clientComponent.fire(10000, 10000), 0);
    }

    @Test
    public void testNonTrivial() {

        ClientComponent clientComponent = new ClientComponent();

        Assert.assertEquals(clientComponent.fire(5, 2), 21);
    }
}
