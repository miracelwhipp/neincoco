package io.github.miracelwhipp.neincoco.it.multi.module.generated;


@WarningThisIsGeneratedNoTestCoverageNeeded
public class LowPerformingMultiplication {

    public int times(int a, int b) {

        int result = 0;

        for (int outerIndex = 0; outerIndex < a; outerIndex++) {

            for (int innerIndex = 0; innerIndex < b; innerIndex++) {

                result++;
            }
        }

        return result;
    }

}
