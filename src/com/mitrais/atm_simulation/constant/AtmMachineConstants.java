package com.mitrais.atm_simulation.constant;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AtmMachineConstants {
	public static class LoginValidationConstant {
		private LoginValidationConstant() {}
		
		public static final int PIN_LENGTH = 6;
		public static final int ACCOUNT_NUMBER_LENGTH = 6;
	}
	
	public static class WithDrawalConstant {
		public static final Map<String, BigDecimal> WITHDRAWAL_AMOUNT_MAP = initMap();
		
		private static Map<String, BigDecimal> initMap(){
			Map<String, BigDecimal> defaultWithdrawalAmountMap = new HashMap<String, BigDecimal>();
			defaultWithdrawalAmountMap.put("1", new BigDecimal(10));
			defaultWithdrawalAmountMap.put("2", new BigDecimal(50));
			defaultWithdrawalAmountMap.put("3", new BigDecimal(100));
			return Collections.unmodifiableMap(defaultWithdrawalAmountMap);
		}

	}
}
