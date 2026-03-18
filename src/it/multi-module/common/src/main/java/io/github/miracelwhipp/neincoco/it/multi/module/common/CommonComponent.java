package io.github.miracelwhipp.neincoco.it.multi.module.common;

public class CommonComponent {

    public int fibonacci(int n) {

        if (n == 0) {
            return 1;
        }

        if (n == 1) {
            return 1;
        }

        return fibonacci(n - 1) + fibonacci(n - 2);
    }

}
