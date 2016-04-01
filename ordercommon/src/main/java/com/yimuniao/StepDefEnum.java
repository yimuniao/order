package com.yimuniao;

public enum StepDefEnum {
    NEW(0),
	SCHEDULING(1), 
	PREPROCESSING(2),
	PROCESSING(3), 
	POSTPROCESSING(4),
	FAILED(9),
	COMPLETED(10),
	UNKNOW(99);

	private int step;

	private StepDefEnum(int step) {
		this.step = step;
	}

	public int getStep() {
		return step;
	}
}
