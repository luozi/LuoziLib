package com.luozi.lib;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class AppController {

	// This uses a LinkedHashMap so that we can
	// replace
	// fragments based on the
	// view id they are being expanded into since
	// we can't
	// guarantee a reference
	// to the handler will be findable
	private LinkedHashMap<Integer, EventHandler> eventHandlers = new LinkedHashMap<Integer, EventHandler>(
			5);
	private LinkedList<Integer> mToBeRemovedEventHandlers = new LinkedList<Integer>();
	private LinkedHashMap<Integer, EventHandler> mToBeAddedEventHandlers = new LinkedHashMap<Integer, EventHandler>();
	private Pair<Integer, EventHandler> mFirstEventHandler;
	private Pair<Integer, EventHandler> mToBeAddedFirstEventHandler;
	private volatile int mDispatchInProgressCounter = 0;

	private static WeakHashMap<Context, AppController> instances = new WeakHashMap<Context, AppController>();

	/**
	 * One of the event types that are sent to or
	 * from the controller
	 */
	public interface EventType {

		final long ACTION_USED = 1L;

		final long ACTION_REFRESH = 1L << 1;

//		final long REVIEW_LIST = 1L << 2;

//		final long REVIEW_REPLY = 1L << 3;

//		final long REVIEW_ITEM_EDIT = 1L << 4;

//		final long ORDER_SELECT = 1L << 5;
	}

	/**
	 * One of the Agenda/Day/Week/Month view types
	 */
	public interface ViewType {
		final int DETAIL = -1;
		final int CURRENT = 0;
		final int AGENDA = 1;
		final int DAY = 2;
		final int WEEK = 3;
		final int MONTH = 4;
		final int EDIT = 5;
	}

	public static class EventInfo {
		public long eventType; // one of the
								// EventType
		public int viewType; // one of the
								// ViewType
		public Object obj; // an object transfer

		public ArrayList<String> strList;
	}

	public interface EventHandler {
		long getSupportedEventTypes();

		void handleEvent(EventInfo event);

		/**
		 * This notifies the handler that the
		 * database has changed and it should
		 * update its view.
		 */
		void eventsChanged();
	}

	/**
	 * Creates and/or returns an instance of
	 * CalendarController associated with the
	 * supplied context. It is best to pass in the
	 * current Activity.
	 * 
	 * @param context
	 *            The activity if at all possible.
	 */
	public static AppController getInstance(Context context) {
		synchronized (instances) {
			AppController controller = instances.get(context);
			if (controller == null) {
				controller = new AppController();
				instances.put(context, controller);
			}
			return controller;
		}
	}

	/**
	 * Removes an instance when it is no longer
	 * needed. This should be called in an
	 * activity's onDestroy method.
	 * 
	 * @param context
	 *            The activity used to create the
	 *            controller
	 */
	public static void removeInstance(Context context) {
		instances.remove(context);
	}

	public void sendEvent(long eventType) {
		EventInfo info = new EventInfo();
		info.eventType = eventType;
		this.sendEvent(info);
	}

	public void sendEvent(long eventType, ArrayList<String> strList) {
		EventInfo info = new EventInfo();
		info.eventType = eventType;
		info.strList = strList;
		this.sendEvent(info);
	}

	public void sendEvent(long eventType, Object c) {
		EventInfo info = new EventInfo();
		info.eventType = eventType;
		info.obj = c;
		this.sendEvent(info);
	}

	public void sendEvent(final EventInfo event) {

		synchronized (this) {
			mDispatchInProgressCounter++;

			// Dispatch to event handler(s)
			if (mFirstEventHandler != null) {
				// Handle the 'first' one before
				// handling
				// the others
				EventHandler handler = mFirstEventHandler.second;
				if (handler != null && (handler.getSupportedEventTypes() & event.eventType) != 0
						&& !mToBeRemovedEventHandlers.contains(mFirstEventHandler.first)) {
					handler.handleEvent(event);
				}
			}
			for (Iterator<Entry<Integer, EventHandler>> handlers = eventHandlers.entrySet()
					.iterator(); handlers.hasNext();) {
				Entry<Integer, EventHandler> entry = handlers.next();
				int key = entry.getKey();
				if (mFirstEventHandler != null && key == mFirstEventHandler.first) {
					// If this was the 'first'
					// handler it
					// was already handled
					continue;
				}
				EventHandler eventHandler = entry.getValue();
				if (eventHandler != null
						&& (eventHandler.getSupportedEventTypes() & event.eventType) != 0) {
					if (mToBeRemovedEventHandlers.contains(key)) {
						continue;
					}
					eventHandler.handleEvent(event);
				}
			}

			mDispatchInProgressCounter--;

			if (mDispatchInProgressCounter == 0) {

				// Deregister removed handlers
				if (mToBeRemovedEventHandlers.size() > 0) {
					for (Integer zombie : mToBeRemovedEventHandlers) {
						eventHandlers.remove(zombie);
						if (mFirstEventHandler != null && zombie.equals(mFirstEventHandler.first)) {
							mFirstEventHandler = null;
						}
					}
					mToBeRemovedEventHandlers.clear();
				}
				// Add new handlers
				if (mToBeAddedFirstEventHandler != null) {
					mFirstEventHandler = mToBeAddedFirstEventHandler;
					mToBeAddedFirstEventHandler = null;
				}
				if (mToBeAddedEventHandlers.size() > 0) {
					for (Entry<Integer, EventHandler> food : mToBeAddedEventHandlers.entrySet()) {
						eventHandlers.put(food.getKey(), food.getValue());
					}
				}
			}
		}

	}

	/**
	 * Adds or updates an event handler. This uses
	 * a LinkedHashMap so that we can replace
	 * fragments based on the view id they are
	 * being expanded into.
	 * 
	 * @param key
	 *            The view id or placeholder for
	 *            this handler
	 * @param eventHandler
	 *            Typically a fragment or activity
	 *            in the calendar app
	 */
	public void registerEventHandler(int key, EventHandler eventHandler) {
		synchronized (this) {
			if (mDispatchInProgressCounter > 0) {
				mToBeAddedEventHandlers.put(key, eventHandler);
			} else {
				eventHandlers.put(key, eventHandler);
			}
		}
	}

	public void registerFirstEventHandler(int key, EventHandler eventHandler) {
		synchronized (this) {
			registerEventHandler(key, eventHandler);
			if (mDispatchInProgressCounter > 0) {
				mToBeAddedFirstEventHandler = new Pair<Integer, EventHandler>(key, eventHandler);
			} else {
				mFirstEventHandler = new Pair<Integer, EventHandler>(key, eventHandler);
			}
		}
	}

	public void deregisterEventHandler(Integer key) {
		synchronized (this) {
			if (mDispatchInProgressCounter > 0) {
				// To avoid ConcurrencyException,
				// stash away
				// the event handler for now.
				mToBeRemovedEventHandlers.add(key);
			} else {
				eventHandlers.remove(key);
				if (mFirstEventHandler != null && mFirstEventHandler.first == key) {
					mFirstEventHandler = null;
				}
			}
		}
	}

	public void deregisterAllEventHandlers() {
		synchronized (this) {
			if (mDispatchInProgressCounter > 0) {
				// To avoid ConcurrencyException,
				// stash away
				// the event handler for now.
				mToBeRemovedEventHandlers.addAll(eventHandlers.keySet());
			} else {
				eventHandlers.clear();
				mFirstEventHandler = null;
			}
		}
	}
}
