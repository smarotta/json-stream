package com.avenuecode.talk.stream.service.dao;

import java.util.Iterator;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.avenuecode.talk.stream.model.Pixel;

public class ImageDAOPixelIterator implements Iterator<Pixel>{

	public static interface Decoder {
		Pixel decode(SqlRowSet rowSet);
	}
	
	private SqlRowSet rowSet;
	private Decoder decoder;
	
	public ImageDAOPixelIterator(Decoder decoder, SqlRowSet rowSet) {
		this.rowSet = rowSet;
		this.decoder = decoder;
	}
	
	@Override
	public boolean hasNext() {
		return rowSet.next();
	}

	@Override
	public Pixel next() {
		return decoder.decode(rowSet);
	}

}
