package com.eztcn.user.eztcn.utils;

import java.io.IOException;

import android.media.MediaRecorder;

/**
 * @title 语音录制工具类
 * @describe
 * @author ezt
 * @created 2014年11月18日
 */
public class SoundMeterUtil {
	static final private double EMA_FILTER = 0.6;
	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(String audioPath) {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mRecorder.setOutputFile(audioPath);
			try {
				mRecorder.prepare();
				mRecorder.start();

				mEMA = 0.0;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}
}