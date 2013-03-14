/*
    Copyright 2009-2013 Michael Graessle

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

package com.mohaine.brewcontroller.client.event.bus;

import java.util.ArrayList;
import java.util.HashMap;

import com.mohaine.brewcontroller.client.event.HandlerRegistration;

public class EventBus {

	private HashMap<Event.Type<?>, ArrayList<? extends EventHandler>> handlers = new HashMap<Event.Type<?>, ArrayList<? extends EventHandler>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <H extends EventHandler> HandlerRegistration addHandler(Event.Type<H> type, final H handler) {
		synchronized (handlers) {
			synchronized (handlers) {
				Event.Type<?> unknownType = type;
				ArrayList<? extends EventHandler> handlerList = handlers.get(unknownType);
				if (handlerList == null) {
					handlerList = new ArrayList();
					handlers.put(unknownType, handlerList);
				}
				final ArrayList<H> typedHandlerList = (ArrayList<H>) handlerList;
				synchronized (typedHandlerList) {
					typedHandlerList.add(handler);
					return new HandlerRegistration() {
						@Override
						public void removeHandler() {
							synchronized (typedHandlerList) {
								typedHandlerList.remove(handler);
							}
						}
					};
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fireEvent(Event event) {
		ArrayList<? extends EventHandler> handlerList;
		synchronized (handlers) {
			handlerList = handlers.get(event.getAssociatedType());
		}
		if (handlerList != null) {
			ArrayList<? extends EventHandler> handlerListCopy;
			synchronized (handlerList) {
				handlerListCopy = new ArrayList(handlerList);
			}
			for (EventHandler eventHandler : handlerListCopy) {
				event.dispatch(eventHandler);
			}

		}
	}
}
