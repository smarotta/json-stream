package com.avenuecode.talk.stream.service.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.dao.ImageDAOPixelIterator.Decoder;

@Repository
public class ImageDAOJdbcTemplate {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Iterator<Pixel> getPixels(String imageId) {
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
				"SELECT x, y, a, r, g, b FROM \"PIXEL\" WHERE \"IMAGE_ID\"=?", imageId);
		
		return new ImageDAOPixelIterator(new Decoder() {
			@Override
			public Pixel decode(SqlRowSet rowSet) {
				Pixel pixel = new Pixel();
				pixel.setX(rowSet.getInt(1));
				pixel.setY(rowSet.getInt(2));
				pixel.setA(rowSet.getInt(3));
				pixel.setR(rowSet.getInt(4));
				pixel.setG(rowSet.getInt(5));
				pixel.setB(rowSet.getInt(6));
				return pixel;
			}}, rowSet);
	}
	
	public Iterator<Pixel> getPixels(String imageId, int offset, int count) {
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
				"SELECT x, y, a, r, g, b FROM \"PIXEL\" WHERE \"IMAGE_ID\"=? OFFSET ? LIMIT ?", imageId, offset, count);
		
		return new ImageDAOPixelIterator(new Decoder() {
			@Override
			public Pixel decode(SqlRowSet rowSet) {
				Pixel pixel = new Pixel();
				pixel.setX(rowSet.getInt(1));
				pixel.setY(rowSet.getInt(2));
				pixel.setA(rowSet.getInt(3));
				pixel.setR(rowSet.getInt(4));
				pixel.setG(rowSet.getInt(5));
				pixel.setB(rowSet.getInt(6));
				return pixel;
			}}, rowSet);
	}
	
	public int saveImage(List<Pixel> pixels, String path, String name) {
		final Pixel [] pixelArray = pixels.toArray(new Pixel[]{});
		
		String imageId = UUID.randomUUID().toString();
		
		//insert image
		int imageResult = jdbcTemplate.update("INSERT INTO \"IMAGE\" (id, path, name) VALUES (?,?,?)", imageId, path, name);
		//jdbcTemplate.execute("INSERT INTO \"IMAGE\" (id, path, name) VALUES ('" + imageId + "', '" + path + "', '" + name + "')");
		
		//insert pixels
		int [] pixelsResult = jdbcTemplate.batchUpdate("INSERT INTO \"PIXEL\" (\"IMAGE_ID\", x, y, a, r, g, b) VALUES (?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Pixel pixel = pixelArray[i];
				ps.setString(1, imageId);
				ps.setInt(2, pixel.getX());
				ps.setInt(3, pixel.getY());
				ps.setInt(4, pixel.getA());
				ps.setInt(5, pixel.getR());
				ps.setInt(6, pixel.getG());
				ps.setInt(7, pixel.getB());
			}
			
			@Override
			public int getBatchSize() {
				return pixels.size();
			}
		});		
		
		int result = imageResult;
		if (pixelsResult != null) {
			for(int i=0; i < pixelsResult.length; i++) {
				result += pixelsResult[i];
			}
		}
		return result;
	}
}
