package com.easyiot.gpio.protocol.api;

/**
 * Available pin numbers
 * 
 * @author daghan
 *
 */
public enum PinNumberEnum {
	pin1(1), pin2(2), pin3(3), pin4(4), pin5(5), pin6(6), pin7(7), pin8(8), pin9(9), pin10(10), pin11(11), pin12(
			12), pin13(13), pin14(14), pin15(15), pin16(16), pin17(17), pin18(18), pin19(19), pin20(20), pin21(
					21), pin22(22), pin23(23), pin24(24), pin25(25), pin26(26), pin27(27), pin28(28), pin29(29);
	int val;

	PinNumberEnum(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}
}
