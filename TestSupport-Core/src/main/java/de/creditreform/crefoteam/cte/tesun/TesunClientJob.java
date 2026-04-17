package de.creditreform.crefoteam.cte.tesun;

import de.creditreform.crefoteam.cte.tesun.util.EnvironmentConfig;

import java.util.concurrent.Callable;

public interface TesunClientJob extends Callable<TesunClientJob.JOB_RESULT> {

    enum JOB_RESULT {
        OK(0), ERROR(-1);

        private final int numericValue;
        private Object userObject;

        JOB_RESULT(int numericValue) {
            this.numericValue = numericValue;
        }

        public int getNumericValue() { return numericValue; }

        public Object getUserObject() { return userObject; }

        public JOB_RESULT setUserObject(Object userObject) {
            this.userObject = userObject;
            return this;
        }
    }

    String getJobCommandName();

    String getJobCommandDescription();

    void init(EnvironmentConfig envConfig) throws Exception;
}
