/*
    Copyright 2009-2011 Michael Graessle

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 
 */

package com.mohaine.brewcontroller.bean;

import java.util.List;

import com.mohaine.brewcontroller.json.ListType;

public class ControllerStatus implements Cloneable {

	public enum Mode {
		ON, HOLD, OFF, UNKNOWN
	}

	private Mode mode = Mode.UNKNOWN;
	private long controlId;
	private long millis;
	private int maxAmps;
	private boolean turnOffOnCommLoss;

	@ListType(HeaterStep.class)
	private List<HeaterStep> steps;

	public long getControlId() {
		return controlId;
	}

	public void setControlId(long controlId) {
		this.controlId = controlId;
	}

	public int getMaxAmps() {
		return maxAmps;
	}

	public void setMaxAmps(int maxAmps) {
		this.maxAmps = maxAmps;
	}

	public void setMaxAmps(byte maxAmps) {
		this.maxAmps = (int) maxAmps & 0xff;
	}

	public boolean isTurnOffOnCommLoss() {
		return turnOffOnCommLoss;
	}

	public void setTurnOffOnCommLoss(boolean turnOffOnCommLoss) {
		this.turnOffOnCommLoss = turnOffOnCommLoss;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public List<HeaterStep> getSteps() {
		return steps;
	}

	public void setSteps(List<HeaterStep> steps) {
		this.steps = steps;
	}

	public long getMillis() {
		return millis;
	}

	public void setMillis(long millis) {
		this.millis = millis;
	}

	public ControllerStatus getClone() throws CloneNotSupportedException {
		return (ControllerStatus) clone();
	}

}