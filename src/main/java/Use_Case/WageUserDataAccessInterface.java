package Use_Case.WaiterWageIncrease;

import sizzleAndServe.Waiter;
/**
 *  The output boundary interface for waiter wage increase,
 * gives current waiter wage, and current rating effect
 /**
 *
 */
public interface WaiterWageIncreaseUserDataAccessInterface {

Waiter getCurrentWaiter();

void setCurrentWaiter(Waiter currentWaiter);
}
