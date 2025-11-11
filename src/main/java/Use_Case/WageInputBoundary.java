package Use_Case;

import sizzleAndServe.Employee;

public interface WageInputBoundary {

        /**
         * Executes the Login use case.
         * Either Increase or decrease wage of waiter or cook.
         */

        void increaseWage(String position);

        void decreaseWage(String position);

}
