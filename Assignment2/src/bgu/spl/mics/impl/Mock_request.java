package bgu.spl.mics.impl;

import bgu.spl.mics.Request;

/**
 * Created by kobi on 12/17/2015.
 */
public class Mock_request implements Request<String>, Comparable

{
	public int compareTo(Object o) {
		return this.getClass().getName().compareTo(o.getClass().getName());
	}

}
