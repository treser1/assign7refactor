/**
 * CalendarDate.java
 * Created on 11.02.2003, 18:02:02 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.date;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import main.java.memoranda.EventsManager;
import main.java.memoranda.date.CalendarDate.Day;
import main.java.memoranda.date.CalendarDate.Month;
import main.java.memoranda.date.CalendarDate.Year;
import main.java.memoranda.util.Local;
import main.java.memoranda.util.Util;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 */
/*$Id: CalendarDate.java,v 1.3 2004/01/30 12:17:41 alexeya Exp $*/
public class CalendarDate {

    //TASK 2-2 SMELL BETWEEN CLASSES <Divergent change>
    public static class Year {
    	Element yearElement = null;
    
    	public Year(Element el) {
    		yearElement = el;
    	}
    
    	public int getValue() {
    		return new Integer(yearElement.getAttribute("year").getValue())
    			.intValue();
    	}
    
    	public CalendarDate.Month getMonth(int m) {
    		Elements ms = yearElement.getChildElements("month");
    		String mm = new Integer(m).toString();
    		for (int i = 0; i < ms.size(); i++)
    			if (ms.get(i).getAttribute("month").getValue().equals(mm))
    				return new CalendarDate.Month(ms.get(i));
    		//return createMonth(m);
    		return null;
    	}
    
    	public CalendarDate.Month createMonth(int m) {
    		Element el = new Element("month");
    		el.addAttribute(new Attribute("month", new Integer(m).toString()));
    		yearElement.appendChild(el);
    		return new CalendarDate.Month(el);
    	}
    
    	public Vector getMonths() {
    		Vector v = new Vector();
    		Elements ms = yearElement.getChildElements("month");
    		for (int i = 0; i < ms.size(); i++)
    			v.add(new CalendarDate.Month(ms.get(i)));
    		return v;
    	}
    
    	public Element getElement() {
    		return yearElement;
    	}
    
    }

    public static class Month {
    	Element mElement = null;
    
    	public Month(Element el) {
    		mElement = el;
    	}
    
    	public int getValue() {
    		return new Integer(mElement.getAttribute("month").getValue())
    			.intValue();
    	}
    
    	public CalendarDate.Day getDay(int d) {
    		if (mElement == null)
    			return null;
    		Elements ds = mElement.getChildElements("day");
    		String dd = new Integer(d).toString();
    		for (int i = 0; i < ds.size(); i++)
    			if (ds.get(i).getAttribute("day").getValue().equals(dd))
    				return new CalendarDate.Day(ds.get(i));
    		//return createDay(d);
    		return null;
    	}
    
    	public CalendarDate.Day createDay(int d) {
    		Element el = new Element("day");
    		el.addAttribute(new Attribute("day", new Integer(d).toString()));
    		el.addAttribute(
    			new Attribute(
    				"date",
    				new CalendarDate(
    					d,
    					getValue(),
    					new Integer(
    						((Element) mElement.getParent())
    							.getAttribute("year")
    							.getValue())
    						.intValue())
    					.toString()));
    
    		mElement.appendChild(el);
    		return new CalendarDate.Day(el);
    	}
    
    	public Vector getDays() {
    		if (mElement == null)
    			return null;
    		Vector v = new Vector();
    		Elements ds = mElement.getChildElements("day");
    		for (int i = 0; i < ds.size(); i++)
    			v.add(new CalendarDate.Day(ds.get(i)));
    		return v;
    	}
    
    	public Element getElement() {
    		return mElement;
    	}
    
    }

    public static class Day {
    		Element dEl = null;
    
    		public Day(Element el) {
    			dEl = el;
    		}
    
    		public int getValue() {
    			return new Integer(dEl.getAttribute("day").getValue()).intValue();
    		}
    
    		/*
    		 * public Note getNote() { return new NoteImpl(dEl);
    		 */
    
    		public Element getElement() {
    			return dEl;
    		}
    	}
    /*
    	static class EventsVectorSorter {
    
    		private static Vector keys = null;
    
    		private static int toMinutes(Object obj) {
    			Event ev = (Event) obj;
    			return ev.getHour() * 60 + ev.getMinute();
    		}
    
    		private static void doSort(int L, int R) { // Hoar's QuickSort
    			int i = L;
    			int j = R;
    			int x = toMinutes(keys.get((L + R) / 2));
    			Object w = null;
    			do {
    				while (toMinutes(keys.get(i)) < x) {
    					i++;
    				}
    				while (x < toMinutes(keys.get(j))) {
    					j--;
    				}
    				if (i <= j) {
    					w = keys.get(i);
    					keys.set(i, keys.get(j));
    					keys.set(j, w);
    					i++;
    					j--;
    				}
    			}
    			while (i <= j);
    			if (L < j) {
    				doSort(L, j);
    			}
    			if (i < R) {
    				doSort(i, R);
    			}
    		}
    
    		public static void sort(Vector theKeys) {
    			if (theKeys == null)
    				return;
    			if (theKeys.size() <= 0)
    				return;
    			keys = theKeys;
    			doSort(0, keys.size() - 1);
    		}
    
    	}
    */

    private int _year;
    private int _month;
    private int _day;

    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public CalendarDate() {
        this(Calendar.getInstance());
    }

    public CalendarDate(int day, int month, int year) {
        _year = year;
        _month = month;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, _year);
        cal.set(Calendar.MONTH, _month);cal.getTime();
        int dmax = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (day <= dmax)
          _day = day;
        else
          _day = dmax;

    }

    public CalendarDate(Calendar cal) {
        _year = cal.get(Calendar.YEAR);
        _day = cal.get(Calendar.DAY_OF_MONTH);
        _month = cal.get(Calendar.MONTH);
    }

    public CalendarDate(Date date) {
        this(dateToCalendar(date));
    }

    public CalendarDate(String date) {
        int[] d = Util.parseDateStamp(date);
        _day = d[0];
        _month = d[1];
        _year = d[2];

    }

    public static CalendarDate today() {
        return new CalendarDate();
    }

    public static CalendarDate yesterday() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DATE, false);
        return new CalendarDate(cal);
    }

    public static CalendarDate tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DATE, true);
        return new CalendarDate(cal);
    }

    public static Calendar toCalendar(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.getTime();
        return cal;
    }

    public static Date toDate(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public Calendar getCalendar() {
        return toCalendar(_day, _month, _year);
    }

    public Date getDate() {
        return toDate(_day, _month, _year);
    }

    public int getDay() {
        return _day;
    }

    public int getMonth() {
        return _month;
    }

    public int getYear() {
        return _year;
    }

    public boolean equals(Object object) {
        if (object.getClass().isInstance(CalendarDate.class)) {
            CalendarDate d2 = (CalendarDate) object;
            return ((d2.getDay() == getDay()) && (d2.getMonth() == getMonth()) && (d2.getYear() == getYear()));
        }
        else if (object.getClass().isInstance(Calendar.class)) {
            Calendar cal = (Calendar) object;
            return this.equals(new CalendarDate(cal));
        }
        else if (object.getClass().isInstance(Date.class)) {
            Date d = (Date) object;
            return this.equals(new CalendarDate(d));
        }
        return super.equals(object);
    }

    public boolean equals(CalendarDate date) {
        if (date == null) return false;
        return ((date.getDay() == getDay()) && (date.getMonth() == getMonth()) && (date.getYear() == getYear()));
    }

    public boolean before(CalendarDate date) {
        if (date == null) return true;
        return this.getCalendar().before(date.getCalendar());
    }

    public boolean after(CalendarDate date) {
        if (date == null) return true;
        return this.getCalendar().after(date.getCalendar());
    }

    public boolean inPeriod(CalendarDate startDate, CalendarDate endDate) {
        return (after(startDate) && before(endDate)) || equals(startDate) || equals(endDate);
    }

    public String toString() {
        return Util.getDateStamp(this);
    }  
    
    public String getFullDateString() {
        return Local.getDateString(this, DateFormat.FULL);
    }
    
    public String getMediumDateString() {
        return Local.getDateString(this, DateFormat.MEDIUM);
    }
    
    public String getLongDateString() {
        return Local.getDateString(this, DateFormat.LONG);
    }
    
    public String getShortDateString() {
        return Local.getDateString(this, DateFormat.SHORT);
    }

    public static CalendarDate.Day getDay(CalendarDate date) {
    	CalendarDate.Year y = getYear(date.getYear());
    	if (y == null)
    		return null;
    	CalendarDate.Month m = y.getMonth(date.getMonth());
    	if (m == null)
    		return null;
    	return m.getDay(date.getDay());
    }

    public static CalendarDate.Year getYear(int y) {
    	Elements yrs = EventsManager._root.getChildElements("year");
    	String yy = new Integer(y).toString();
    	for (int i = 0; i < yrs.size(); i++)
    		if (yrs.get(i).getAttribute("year").getValue().equals(yy))
    			return new CalendarDate.Year(yrs.get(i));
    	//return createYear(y);
    	return null;
    }

    public static CalendarDate.Year createYear(int y) {
    	Element el = new Element("year");
    	el.addAttribute(new Attribute("year", new Integer(y).toString()));
    	EventsManager._root.appendChild(el);
    	return new CalendarDate.Year(el);
    }

    //TASK 2-2 SMELL BETWEEN CLASSES <Divergent Change>
    public static CalendarDate.Day createDay(CalendarDate date) {
    	CalendarDate.Year y = CalendarDate.getYear(date.getYear());
    	if (y == null)
    		y = CalendarDate.createYear(date.getYear());
    	CalendarDate.Month m = y.getMonth(date.getMonth());
    	if (m == null)
    		m = y.createMonth(date.getMonth());
    	CalendarDate.Day d = m.getDay(date.getDay());
    	if (d == null)
    		d = m.createDay(date.getDay());
    	return d;
    }
    

}
