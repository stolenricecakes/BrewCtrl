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

package com.mohaine.brewcontroller.swing;

import java.io.File;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mohaine.brewcontroller.BrewControllerStartup;
import com.mohaine.brewcontroller.Configuration;

public class SwingController {
	public static void main(String[] args) {

		File configFile = new File("BreweryLayout.json");

		Injector injector = Guice.createInjector(new BrewControllerSwingModule(configFile));

		BrewControllerStartup bc = injector.getInstance(BrewControllerStartup.class);
		bc.startup();
	}
}
