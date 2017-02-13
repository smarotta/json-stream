package com.avenuecode.talk.stream.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avenuecode.talk.stream.model.Pixel;
import com.avenuecode.talk.stream.service.IteratorListener;

@Repository
public class ImageDAORawJdbc {
	
	@Autowired
	private DataSource dataSource;
	
	public void getPixels(IteratorListener listener, String imageId) {
		Connection connection = null;
		Exception error = null;
		
		try {
			listener.onStart();
			
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT x, y, a, r, g, b FROM \"PIXEL\" WHERE \"IMAGE_ID\"=? ORDER BY x, y ASC");
			statement.setString(1, imageId);
			statement.setFetchSize(10);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				Pixel pixel = new Pixel();
				pixel.setX(resultSet.getInt(1));
				pixel.setY(resultSet.getInt(2));
				pixel.setA(resultSet.getInt(3));
				pixel.setR(resultSet.getInt(4));
				pixel.setG(resultSet.getInt(5));
				pixel.setB(resultSet.getInt(6));
				
				listener.onPixelRead(pixel);
			}
			
		} catch(Exception e) {
			error = e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				}catch(Exception e){}
			}
			listener.onFinish(error);
		}
	}
	
	public void getPixels(IteratorListener listener, String imageId, int offset, int count) {
		Connection connection = null;
		Exception error = null;
		
		try {
			listener.onStart();
			
			connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT x, y, a, r, g, b FROM \"PIXEL\" WHERE \"IMAGE_ID\"=? ORDER BY x, y ASC OFFSET ? LIMIT ?");
			statement.setString(1, imageId);
			statement.setInt(2, offset);
			statement.setInt(3, count);
			statement.setFetchSize(10);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				Pixel pixel = new Pixel();
				pixel.setX(resultSet.getInt(1));
				pixel.setY(resultSet.getInt(2));
				pixel.setA(resultSet.getInt(3));
				pixel.setR(resultSet.getInt(4));
				pixel.setG(resultSet.getInt(5));
				pixel.setB(resultSet.getInt(6));
				
				listener.onPixelRead(pixel);
			}
			
		} catch(Exception e) {
			error = e;
		} finally {
			if (connection != null) {
				try {
					connection.close();
				}catch(Exception e){}
			}
			listener.onFinish(error);
		}
	}
}
