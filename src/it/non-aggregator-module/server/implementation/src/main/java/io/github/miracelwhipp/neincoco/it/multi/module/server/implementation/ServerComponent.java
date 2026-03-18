package io.github.miracelwhipp.neincoco.it.multi.module.server.implementation;

public class ServerComponent {

    public int unitTestedMethod(int a, int b) {

        if (a > b) {

            return b - a;
        }

        return a - b;
    }


    public int integrationTestedMethod(int a, int b) {

        if (a < b) {
            return b / a;
        }

        return a / b;
    }
}
