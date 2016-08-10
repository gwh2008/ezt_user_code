package com.zxing.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

public final class ScanResultPointCallback implements ResultPointCallback {

	// private final ScanView scanView;

	public ScanResultPointCallback(ScanView scanView) {
		// this.scanView = scanView;
	}

	public void foundPossibleResultPoint(ResultPoint point) {
		// scanView.addPossibleResultPoint(point);
	}

}
