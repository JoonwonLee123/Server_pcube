package com.pcube;

// file transfer
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

// current time generator
import java.text.SimpleDateFormat;
import java.util.Calendar;

// transfer data from SQLite db to MySQL db
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class DBFile {
	File dbFile;
	String fileName;
	
	public DBFile(File newDBFile){
		
		this.dbFile = newDBFile;
		this.fileName = newDBFile.getName();
		
	}
	
	public DBFile(MultipartFile newDBFile) throws IOException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		this.fileName = sdf.format(cal.getTime()) + "_db"; // 1

		byte[] bytes = newDBFile.getBytes();
		this.dbFile = new File(fileName);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(dbFile));
		stream.write(bytes);
		stream.close();
	}

	public String getFileName() {
		return this.fileName;
	}

	public void delete() {
		this.dbFile.delete();
	}

	/*
	public void integrateData() throws Exception {
		try {
          Class.forName("org.sqlite.JDBC");
          Class.forName("com.mysql.jdbc.Driver");

          // connection to SQLite db file
          Connection connSqlite = DriverManager.getConnection("jdbc:sqlite:" + this.fileName);
          Statement statSqlite = connSqlite.createStatement();
          ResultSet rs;

          // connection to MySQL database
          Connection connMysql = DriverManager.getConnection("jdbc:mysql://imlab-ws1.snu.ac.kr:3306/CAR_IF?" + "user=kakaoadmin&password=zkzkdh8");
          PreparedStatement ps;
          String sql;

          rs = statSqlite.executeQuery("SELECT expId, addingTime, newsId, length, isPhoto, dwellTime, isLike FROM itemClick;");
          sql = "INSERT INTO _mixContents (expId, currentTime, newsId, length, ";





        } catch (Exception e) {
            throw e;
        }
	}
	 */


	/*
	 * Transfer data in itemList table from SQLite db to MySQL db
	 */
	// http://www.tutorialspoint.com/sqlite/sqlite_java.htm Âü°í
	public void transferData() throws Exception {
		try {
			System.out.println("0");
			Class.forName("org.sqlite.JDBC");
			System.out.println("0");
			Class.forName("com.mysql.jdbc.Driver");
			
			// connection to SQLite db file
			Connection connSqlite = DriverManager.getConnection("jdbc:sqlite:" + this.fileName);
			DatabaseMetaData md = connSqlite.getMetaData();
			ResultSet tables = md.getTables(null, null, "%", null); // 2
			Statement statSqlite = connSqlite.createStatement();
			String tableName;
			System.out.println("1");
			// connection to MySQL database
			Connection connMysql = DriverManager.getConnection("jdbc:mysql://imlab-ws2.snu.ac.kr:3306/pcube_TEST?" + "user=pcubeuser&password=vntl@dkdldpafoq1");
			PreparedStatement ps;
			String sql;
			System.out.println("2");

			while (tables.next()) {
				tableName = tables.getString(3);
				if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence") && !tableName.equals("ringerModeData")) {
					System.out.println("-----------------------");
					System.out.println(tableName);
					List<String[]> columnNames = new ArrayList<String[]>();
					ResultSet rsColumns = statSqlite.executeQuery("PRAGMA table_info(" + tableName + ");"); // 3
					while (rsColumns.next()) {
						String[] cols = new String[2];
						cols[0] = rsColumns.getString("name");
						cols[1] = rsColumns.getString("type");
						columnNames.add(cols);
					}
					rsColumns.close();
					ResultSet rsRecords = statSqlite.executeQuery("SELECT * FROM " + tableName + ";");

					// Forming SQL query INSERT
					sql = "INSERT INTO " + tableName + " (";
					// Iterator #0
					Iterator<String[]> it0 = columnNames.iterator();
					while (it0.hasNext()) {
						String[] currCol0 = it0.next();
						if (!currCol0[0].equals("_id")) {
							sql += currCol0[0] + ",";
						}
					}
					sql = sql.substring(0,sql.length()-1);
					sql += ") VALUES (";
					// Iterator #1
					Iterator<String[]> it1 = columnNames.iterator();
					while (it1.hasNext()) {
						String[] currCol1 = it1.next();
						if (!currCol1[0].equals("_id")) {
							sql += "?,";
						}
					}
					sql = sql.substring(0,sql.length()-1);
					sql += ")";

					ps = connMysql.prepareStatement(sql);
					while (rsRecords.next()) {
						System.out.println("--------------------newRecord");
						Iterator<String[]> it2 = columnNames.iterator();
						int count = 0;
						while (it2.hasNext()) {
							String[] currCol2 = it2.next();
							if (!currCol2[0].equals("_id")) {
								count += 1;
								switch (currCol2[1]) {
								case "INTEGER":
								case "INT":
									ps.setInt(count, rsRecords.getInt(currCol2[0]));
									if(rsRecords.getInt(currCol2[0]) == 0){
										
									}
									System.out.println(rsRecords.getInt(currCol2[0]));
									break;
								case "TEXT":
								case "DATE":
									ps.setString(count, rsRecords.getString(currCol2[0]));
									System.out.println(rsRecords.getString(currCol2[0]));
									break;
								case "FLOAT":
									ps.setFloat(count, rsRecords.getFloat(currCol2[0]));
									System.out.println(rsRecords.getFloat(currCol2[0]));
									break;
								case "DOUBLE":
									ps.setDouble(count, rsRecords.getDouble(currCol2[0]));
									System.out.println(rsRecords.getDouble(currCol2[0]));
									break;
								case "REAL":
									ps.setDouble(count, rsRecords.getDouble(currCol2[0]));
									System.out.println(rsRecords.getDouble(currCol2[0]));
									break;
								case "LONG":
									ps.setLong(count, rsRecords.getLong(currCol2[0]));
									System.out.println(rsRecords.getLong(currCol2[0]));
									break;
								}
							}
						}
						ps.addBatch();
						//                      ps.executeUpdate();
					}
					ps.executeBatch();
					ps.close();
					rsRecords.close();
				}
			}

			connSqlite.close();
			connMysql.close();

		} catch (Exception e) {
			throw e;
		}

	}
}
