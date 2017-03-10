package com.possethings.quickdraw;

public class BuzzerNote {
	private double frequency;
	private long duration;
	private double volume;

	public BuzzerNote(double frequency, long duration, double volume) {
		this.frequency = frequency;
		this.duration = duration;
		this.volume = volume;
	}

	public double getFrequency() {
		return frequency;
	}

	public long getDuration() {
		return duration;
	}

	public double getVolume() {
		return volume;
	}
}
